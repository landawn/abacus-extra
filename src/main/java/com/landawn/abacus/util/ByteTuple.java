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
 * <p>
 * This class provides common functionality for byte-based tuples of various sizes (0 to 9 elements).
 * ByteTuple is designed to be a lightweight, type-safe container for multiple byte values
 * that can be used as a composite key, return multiple values from a method, or group related
 * byte values together. All byte tuple implementations extend this class and are immutable by design.
 * </p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Creating tuples
 * ByteTuple1 single = ByteTuple.of((byte) 10);
 * ByteTuple2 pair = ByteTuple.of((byte) 10, (byte) 20);
 * ByteTuple3 triple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
 *
 * // Using statistical operations
 * byte min = triple.min();     // 10
 * byte max = triple.max();     // 30
 * double avg = triple.average(); // 20.0
 *
 * // Using functional operations
 * pair.accept((a, b) -> System.out.println(a + " + " + b));
 * int sum = triple.map((a, b, c) -> a + b + c);
 * }</pre>
 *
 * @param <TP> the specific ByteTuple subtype
 * @see PrimitiveTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class ByteTuple<TP extends ByteTuple<TP>> extends PrimitiveTuple<TP> {

    protected byte[] elements;

    /**
     * Constructor for subclasses.
     * <p>
     * This constructor is protected to prevent direct instantiation of the abstract class.
     * Subclasses should use this constructor to initialize their instances.
     * </p>
     */
    protected ByteTuple() {
    }

    /**
     * Creates a ByteTuple1 containing a single byte value.
     * 
     * <p><b>Usage Examples:</b></p>
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
     * <p><b>Usage Examples:</b></p>
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
     * <p><b>Usage Examples:</b></p>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
     * byte first = tuple._1;  // 10
     * byte fourth = tuple._4; // 40
     * byte median = tuple.median(); // 20
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
     * byte first = tuple._1;  // 10
     * byte fifth = tuple._5;  // 50
     * int sum = tuple.sum();  // 150
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple6 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
     * byte first = tuple._1;    // 10
     * byte sixth = tuple._6;    // 60
     * ByteTuple6 reversed = tuple.reverse(); // (60, 50, 40, 30, 20, 10)
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple7 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
     * byte first = tuple._1;   // 10
     * byte seventh = tuple._7; // 70
     * byte[] array = tuple.toArray(); // [10, 20, 30, 40, 50, 60, 70]
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple8 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
     * byte first = tuple._1;  // 10
     * byte eighth = tuple._8; // 80
     * ByteList list = tuple.toList();
     * }</pre>
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
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more byte values
     */
    @Deprecated
    public static ByteTuple8 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7, final byte _8) {
        return new ByteTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a ByteTuple9 containing nine byte values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple9 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
     * byte first = tuple._1; // 10
     * byte ninth = tuple._9; // 90
     * int arity = tuple.arity(); // 9
     * }</pre>
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
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more byte values
     */
    @Deprecated
    public static ByteTuple9 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7, final byte _8,
            final byte _9) {
        return new ByteTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a ByteTuple from an array of byte values.
     * <p>
     * The size of the returned tuple depends on the length of the input array.
     * This factory method supports arrays with 0 to 9 elements. For empty or null
     * arrays, returns an empty ByteTuple0. For arrays with 1-9 elements, returns
     * the corresponding ByteTuple1-9 instance.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create from array
     * byte[] values = {10, 20, 30};
     * ByteTuple3 tuple = ByteTuple.create(values);
     *
     * // Empty array returns ByteTuple0
     * ByteTuple0 empty = ByteTuple.create(new byte[0]);
     *
     * // Single element
     * ByteTuple1 single = ByteTuple.create(new byte[]{(byte) 42});
     * }</pre>
     *
     * @param <TP> the specific ByteTuple subtype to return
     * @param a the array of byte values (must have length 0-9), may be {@code null}
     * @return a ByteTuple of appropriate size containing the array elements
     * @throws IllegalArgumentException if the array has more than 9 elements
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
     * <p>
     * This method finds and returns the smallest byte value among all elements
     * in the tuple. For tuples with a single element, returns that element.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
     * byte min = tuple.min(); // 10
     *
     * ByteTuple2 pair = ByteTuple.of((byte) 5, (byte) 15);
     * byte minPair = pair.min(); // 5
     * }</pre>
     *
     * @return the minimum byte value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public byte min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum byte value in this tuple.
     * <p>
     * This method finds and returns the largest byte value among all elements
     * in the tuple. For tuples with a single element, returns that element.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
     * byte max = tuple.max(); // 30
     *
     * ByteTuple2 pair = ByteTuple.of((byte) 5, (byte) 15);
     * byte maxPair = pair.max(); // 15
     * }</pre>
     *
     * @return the maximum byte value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public byte max() {
        return N.max(elements());
    }

    /**
     * Returns the median byte value in this tuple.
     * <p>
     * The median is the middle value when all elements are sorted. For tuples with
     * an odd number of elements, returns the exact middle value. For tuples with an
     * even number of elements, returns the lower of the two middle values.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Odd number of elements
     * ByteTuple3 tuple3 = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
     * byte median = tuple3.median(); // 20 (middle value when sorted: 10, 20, 30)
     *
     * // Even number of elements
     * ByteTuple4 tuple4 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
     * byte median2 = tuple4.median(); // 20 (lower middle value)
     * }</pre>
     *
     * @return the median byte value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public byte median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all byte values in this tuple as an integer.
     * <p>
     * This method calculates the sum by adding all byte values together. The result
     * is returned as an int to prevent overflow issues that could occur if the sum
     * exceeds the byte range (-128 to 127).
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * int sum = tuple.sum(); // 60
     *
     * ByteTuple2 pair = ByteTuple.of((byte) 100, (byte) 50);
     * int pairSum = pair.sum(); // 150 (exceeds byte range, so returned as int)
     * }</pre>
     *
     * @return the sum of all byte values in this tuple as an integer
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all byte values in this tuple as a double.
     * <p>
     * This method calculates the arithmetic mean of all elements in the tuple.
     * The result is always returned as a double to preserve precision, even when
     * the average is a whole number.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * double avg = tuple.average(); // 20.0
     *
     * ByteTuple2 pair = ByteTuple.of((byte) 5, (byte) 10);
     * double avgPair = pair.average(); // 7.5
     * }</pre>
     *
     * @return the average of all byte values in this tuple as a double
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
     * ByteTuple2 pair = ByteTuple.of((byte) 5, (byte) 15);
     * ByteTuple2 reversedPair = pair.reverse(); // (15, 5)
     *
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * ByteTuple3 reversed = tuple.reverse(); // (30, 20, 10)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified byte value.
     * <p>
     * This method performs a linear search through all elements in the tuple to determine
     * if any element matches the specified value. Returns {@code true} if at least one
     * element equals the search value, {@code false} otherwise.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * boolean has20 = tuple.contains((byte) 20); // true
     * boolean has40 = tuple.contains((byte) 40); // false
     *
     * ByteTuple2 pair = ByteTuple.of((byte) 5, (byte) 10);
     * boolean has5 = pair.contains((byte) 5);   // true
     * boolean has15 = pair.contains((byte) 15); // false
     * }</pre>
     *
     * @param valueToFind the byte value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(byte valueToFind);

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
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * byte[] array = tuple.toArray(); // [10, 20, 30]
     * array[0] = (byte) 99; // Does not modify the tuple
     *
     * ByteTuple2 pair = ByteTuple.of((byte) 5, (byte) 10);
     * byte[] pairArray = pair.toArray(); // [5, 10]
     * }</pre>
     *
     * @return a new byte array containing all tuple elements
     */
    public byte[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new ByteList containing all elements of this tuple.
     * <p>
     * Converts this tuple to a mutable {@link ByteList} containing all elements
     * in their original order. The returned list is a new instance and modifications
     * to it do not affect the original tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * ByteList list = tuple.toList();
     * list.add((byte) 40); // Adds to the list, tuple remains unchanged
     *
     * ByteTuple2 pair = ByteTuple.of((byte) 5, (byte) 10);
     * ByteList pairList = pair.toList(); // [5, 10]
     * }</pre>
     *
     * @return a new ByteList containing all tuple elements
     */
    public ByteList toList() {
        return ByteList.of(elements().clone());
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
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * tuple.forEach(b -> System.out.println("Value: " + b));
     * // Output: Value: 10, Value: 20, Value: 30
     *
     * // Sum values using external accumulator
     * AtomicInteger sum = new AtomicInteger();
     * tuple.forEach(b -> sum.addAndGet(b));
     * // sum is now 60
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the consumer
     * @param consumer the action to be performed for each element, must not be {@code null}
     * @throws E if the consumer throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
        for (final byte e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns a ByteStream of all elements in this tuple.
     * <p>
     * Converts this tuple to a sequential {@link ByteStream} for primitive byte values.
     * This allows using stream operations like filter, map, and statistical operations
     * on the tuple elements. ByteStream works with primitive bytes directly, avoiding
     * the boxing overhead that would occur with a regular Stream.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * int sum = tuple.stream().sum(); // 60
     * long count = tuple.stream().filter(b -> b > 15).count(); // 2
     *
     * ByteTuple2 pair = ByteTuple.of((byte) 5, (byte) 10);
     * byte max = pair.stream().max().orElse((byte) 0); // 10
     * }</pre>
     *
     * @return a ByteStream containing all tuple elements
     */
    public ByteStream stream() {
        return ByteStream.of(elements());
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
     *   <li>They contain the same byte values in the same order</li>
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
            return N.equals(elements(), ((ByteTuple<TP>) obj).elements());
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
     * <p><b>Examples:</b></p>
     * <ul>
     *   <li>{@code (10, 20, 30)} for a ByteTuple3</li>
     *   <li>{@code (5, 10)} for a ByteTuple2</li>
     *   <li>{@code (42)} for a ByteTuple1</li>
     *   <li>{@code ()} for an empty ByteTuple0</li>
     * </ul>
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
     * <p>
     * This class represents a tuple with arity 0, serving as a singleton instance
     * for cases where an empty byte tuple is needed. It is typically returned
     * by the {@link #create(byte[])} method when a null or empty array is provided.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple0 empty = ByteTuple.create(null);
     * ByteTuple0 empty2 = ByteTuple.create(new byte[0]);
     * int size = empty.arity(); // 0
     * }</pre>
     */
    static final class ByteTuple0 extends ByteTuple<ByteTuple0> {

        private static final ByteTuple0 EMPTY = new ByteTuple0();

        ByteTuple0() {
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
         * Returns the minimum byte value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public byte min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the maximum byte value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public byte max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the median byte value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public byte median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         * Since this tuple is empty, returns 0.
         *
         * @return 0
         */
        @Override
        public int sum() {
            return 0;
        }

        /**
         * Returns the average of all byte values in this tuple.
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
         * Returns this empty tuple instance.
         * Since this tuple has no elements, reversing has no effect.
         *
         * @return this ByteTuple0 instance
         */
        @Override
        public ByteTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified byte value.
         * Since this tuple is empty, always returns false.
         *
         * @param valueToFind the byte value to search for
         * @return {@code false} always, because the tuple is empty
         */
        @Override
        public boolean contains(final byte valueToFind) {
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
        protected byte[] elements() {
            return N.EMPTY_BYTE_ARRAY;
        }
    }

    /**
     * A ByteTuple containing exactly one byte element.
     * <p>
     * Provides direct access to the element through the public final field {@code _1}.
     * This is the simplest non-empty tuple type, useful for wrapping a single byte
     * value in a tuple context.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple1 tuple = ByteTuple.of((byte) 42);
     * byte value = tuple._1; // 42
     * byte min = tuple.min(); // 42 (single element)
     * byte max = tuple.max(); // 42 (single element)
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 1.
         *
         * @return 1
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
         * Returns a new ByteTuple1 with the same element.
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
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
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
         * @return a string representation in the format "(element)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1} and {@code _2}.
     * This tuple type is commonly used for representing pairs of byte values,
     * and supports specialized functional operations through {@link #accept}, {@link #map}, and
     * {@link #filter} methods that work with both elements.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
     * byte first = tuple._1;  // 10
     * byte second = tuple._2; // 20
     *
     * // Using functional operations
     * tuple.accept((a, b) -> System.out.println(a + " + " + b));
     * int sum = tuple.map((a, b) -> a + b); // 30
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 2.
         *
         * @return 2
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
         * Returns a new ByteTuple2 with the elements in reverse order.
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
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
         * tuple.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to be performed on both elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.ByteBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given function to both elements of this tuple and returns the result.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
         * int sum = tuple.map((a, b) -> a + b); // 30
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
         * Optional<ByteTuple2> result = tuple.filter((a, b) -> a < b); // Optional containing the tuple
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
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
         * @return a string representation in the format "(element1, element2)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1}, {@code _2}, and {@code _3}.
     * This tuple type supports specialized functional operations through {@link #accept}, {@link #map}, and
     * {@link #filter} methods that work with all three elements simultaneously.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * byte first = tuple._1;  // 10
     * byte second = tuple._2; // 20
     * byte third = tuple._3;  // 30
     *
     * // Using statistical operations
     * byte min = tuple.min();     // 10
     * byte max = tuple.max();     // 30
     * double avg = tuple.average(); // 20.0
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 3.
         *
         * @return 3
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
         * Returns a new ByteTuple3 with the elements in reverse order.
         *
         * @return a new ByteTuple3 with the elements in reverse order
         */
        @Override
        public ByteTuple3 reverse() {
            return new ByteTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
         * tuple.accept((a, b, c) -> System.out.println(a + ", " + b + ", " + c));
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to be performed on all three elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.ByteTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given function to all three elements of this tuple and returns the result.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
         * int sum = tuple.map((a, b, c) -> a + b + c); // 60
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
         * Optional<ByteTuple3> result = tuple.filter((a, b, c) -> a < b && b < c); // Optional containing the tuple
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
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
         * @return a string representation in the format "(element1, element2, element3)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
     * This tuple type is useful for grouping four related byte values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
     * byte first = tuple._1;  // 10
     * byte fourth = tuple._4; // 40
     * byte median = tuple.median(); // 20
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 4.
         *
         * @return 4
         */
        @Override
        public int arity() {
            return 4;
        }

        /**
         * Returns the minimum byte value in this tuple.
         *
         * @return the smallest of the four byte values
         */
        @Override
        public byte min() {
            return N.min(_1, _2, _3, _4);
        }

        /**
         * Returns the maximum byte value in this tuple.
         *
         * @return the largest of the four byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the median byte value in this tuple.
         * For a tuple of four elements, returns the lower of the two middle values when sorted.
         *
         * @return the median byte value
         */
        @Override
        public byte median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         *
         * @return the sum of all four byte values as an integer
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4);
        }

        /**
         * Returns the average of all byte values in this tuple.
         *
         * @return the average of all four byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4);
        }

        /**
         * Returns a new ByteTuple4 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
         * ByteTuple4 reversed = tuple.reverse(); // (40, 30, 20, 10)
         * }</pre>
         *
         * @return a new ByteTuple4 with the elements in reverse order
         */
        @Override
        public ByteTuple4 reverse() {
            return new ByteTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _5}.
     * This tuple type is useful for grouping five related byte values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
     * byte first = tuple._1;  // 10
     * byte fifth = tuple._5;  // 50
     * int sum = tuple.sum();  // 150
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 5.
         *
         * @return 5
         */
        @Override
        public int arity() {
            return 5;
        }

        /**
         * Returns the minimum byte value in this tuple.
         *
         * @return the smallest of the five byte values
         */
        @Override
        public byte min() {
            return N.min(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the maximum byte value in this tuple.
         *
         * @return the largest of the five byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the median byte value in this tuple.
         * For a tuple of five elements, returns the middle value when sorted.
         *
         * @return the median byte value
         */
        @Override
        public byte median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         *
         * @return the sum of all five byte values as an integer
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the average of all byte values in this tuple.
         *
         * @return the average of all five byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5);
        }

        /**
         * Returns a new ByteTuple5 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
         * ByteTuple5 reversed = tuple.reverse(); // (50, 40, 30, 20, 10)
         * }</pre>
         *
         * @return a new ByteTuple5 with the elements in reverse order
         */
        @Override
        public ByteTuple5 reverse() {
            return new ByteTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _6}.
     * This tuple type is useful for grouping six related byte values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple6 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
     * byte first = tuple._1;    // 10
     * byte sixth = tuple._6;    // 60
     * ByteTuple6 reversed = tuple.reverse(); // (60, 50, 40, 30, 20, 10)
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 6.
         *
         * @return 6
         */
        @Override
        public int arity() {
            return 6;
        }

        /**
         * Returns the minimum byte value in this tuple.
         *
         * @return the smallest of the six byte values
         */
        @Override
        public byte min() {
            return N.min(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the maximum byte value in this tuple.
         *
         * @return the largest of the six byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median byte value in this tuple.
         * For a tuple of six elements, returns the lower of the two middle values when sorted.
         *
         * @return the median byte value
         */
        @Override
        public byte median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         *
         * @return the sum of all six byte values as an integer
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the average of all byte values in this tuple.
         *
         * @return the average of all six byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns a new ByteTuple6 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple6 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
         * ByteTuple6 reversed = tuple.reverse(); // (60, 50, 40, 30, 20, 10)
         * }</pre>
         *
         * @return a new ByteTuple6 with the elements in reverse order
         */
        @Override
        public ByteTuple6 reverse() {
            return new ByteTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _7}.
     * This tuple type is useful for grouping seven related byte values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple7 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
     * byte first = tuple._1;   // 10
     * byte seventh = tuple._7; // 70
     * byte[] array = tuple.toArray(); // [10, 20, 30, 40, 50, 60, 70]
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 7.
         *
         * @return 7
         */
        @Override
        public int arity() {
            return 7;
        }

        /**
         * Returns the minimum byte value in this tuple.
         *
         * @return the smallest of the seven byte values
         */
        @Override
        public byte min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the maximum byte value in this tuple.
         *
         * @return the largest of the seven byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the median byte value in this tuple.
         * For a tuple of seven elements, returns the middle value when sorted.
         *
         * @return the median byte value
         */
        @Override
        public byte median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         *
         * @return the sum of all seven byte values as an integer
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the average of all byte values in this tuple.
         *
         * @return the average of all seven byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns a new ByteTuple7 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple7 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
         * ByteTuple7 reversed = tuple.reverse(); // (70, 60, 50, 40, 30, 20, 10)
         * }</pre>
         *
         * @return a new ByteTuple7 with the elements in reverse order
         */
        @Override
        public ByteTuple7 reverse() {
            return new ByteTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
            consumer.accept(_7);
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _8}.
     * This tuple type is useful for grouping eight related byte values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple8 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
     * byte first = tuple._1;  // 10
     * byte eighth = tuple._8; // 80
     * ByteList list = tuple.toList();
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more byte values
     */
    @Deprecated
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
         * Returns the number of elements in this tuple, which is always 8.
         *
         * @return 8
         */
        @Override
        public int arity() {
            return 8;
        }

        /**
         * Returns the minimum byte value in this tuple.
         *
         * @return the smallest of the eight byte values
         */
        @Override
        public byte min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the maximum byte value in this tuple.
         *
         * @return the largest of the eight byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the median byte value in this tuple.
         * For a tuple of eight elements, returns the lower of the two middle values when sorted.
         *
         * @return the median byte value
         */
        @Override
        public byte median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         *
         * @return the sum of all eight byte values as an integer
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the average of all byte values in this tuple.
         *
         * @return the average of all eight byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns a new ByteTuple8 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple8 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
         * ByteTuple8 reversed = tuple.reverse(); // (80, 70, 60, 50, 40, 30, 20, 10)
         * }</pre>
         *
         * @return a new ByteTuple8 with the elements in reverse order
         */
        @Override
        public ByteTuple8 reverse() {
            return new ByteTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
            consumer.accept(_7);
            consumer.accept(_8);
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _9}.
     * This tuple type is useful for grouping nine related byte values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple9 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
     * byte first = tuple._1; // 10
     * byte ninth = tuple._9; // 90
     * int arity = tuple.arity(); // 9
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more byte values
     */
    @Deprecated
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
         * Returns the number of elements in this tuple, which is always 9.
         *
         * @return 9
         */
        @Override
        public int arity() {
            return 9;
        }

        /**
         * Returns the minimum byte value in this tuple.
         *
         * @return the smallest of the nine byte values
         */
        @Override
        public byte min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the maximum byte value in this tuple.
         *
         * @return the largest of the nine byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the median byte value in this tuple.
         * For a tuple of nine elements, returns the middle value when sorted.
         *
         * @return the median byte value
         */
        @Override
        public byte median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         *
         * @return the sum of all nine byte values as an integer
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the average of all byte values in this tuple.
         *
         * @return the average of all nine byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns a new ByteTuple9 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple9 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
         * ByteTuple9 reversed = tuple.reverse(); // (90, 80, 70, 60, 50, 40, 30, 20, 10)
         * }</pre>
         *
         * @return a new ByteTuple9 with the elements in reverse order
         */
        @Override
        public ByteTuple9 reverse() {
            return new ByteTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * @param valueToFind the byte value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
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

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}