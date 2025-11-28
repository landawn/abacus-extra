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
import com.landawn.abacus.util.stream.FloatStream;

/**
 * Abstract base class for immutable tuple implementations that hold primitive float values.
 * <p>
 * This class provides common functionality for float-based tuples of various sizes (0 to 9 elements).
 * FloatTuple is designed to be a lightweight, type-safe container for multiple float values
 * that can be used as a composite key, return multiple values from a method, or group related
 * float values together. All float tuple implementations extend this class and are immutable by design.
 * </p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Creating tuples
 * FloatTuple1 single = FloatTuple.of(3.14f);
 * FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
 * FloatTuple3 triple = FloatTuple.of(1.0f, 2.0f, 3.0f);
 *
 * // Using statistical operations
 * float min = triple.min();  // 1.0f
 * float max = triple.max();  // 3.0f
 * double avg = triple.average();  // 2.0
 *
 * // Using functional operations
 * pair.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
 * float product = triple.map((a, b, c) -> a * b * c);  // 6.0f
 * }</pre>
 *
 * @param <TP> the specific FloatTuple subtype for fluent method chaining
 * @see PrimitiveTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class FloatTuple<TP extends FloatTuple<TP>> extends PrimitiveTuple<TP> {

    protected float[] elements;

    /**
     * Constructor for subclasses.
     * <p>
     * This constructor is protected to prevent direct instantiation of the abstract class.
     * Subclasses should use this constructor to initialize their instances.
     * </p>
     */
    protected FloatTuple() {
    }

    /**
     * Creates a FloatTuple1 containing a single float value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple1 single = FloatTuple.of(3.14f);
     * float value = single._1;  // 3.14f
     * }</pre>
     *
     * @param _1 the float value to store in the tuple
     * @return a new FloatTuple1 containing the provided value
     */
    public static FloatTuple1 of(final float _1) {
        return new FloatTuple1(_1);
    }

    /**
     * Creates a FloatTuple2 containing two float values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
     * float sum = pair._1 + pair._2;  // 4.0f
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple3 triple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * double average = triple.average();  // 2.0
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple4 quad = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
     * float sum = quad.sum();  // 10.0f
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
     * float median = tuple.median();  // 3.0f
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
     * double average = tuple.average();  // 3.5
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
     * float max = tuple.max();  // 7.0f
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
     * float min = tuple.min();  // 1.0f
     * }</pre>
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
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more float values
     */
    @Deprecated
    public static FloatTuple8 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7,
            final float _8) {
        return new FloatTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a FloatTuple9 containing nine float values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
     * float sum = tuple.sum();  // 45.0f
     * }</pre>
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
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more float values
     */
    @Deprecated
    public static FloatTuple9 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7, final float _8,
            final float _9) {
        return new FloatTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a FloatTuple from an array of float values.
     * <p>
     * The size of the returned tuple depends on the length of the input array.
     * This factory method supports arrays with 0 to 9 elements. For empty or null
     * arrays, returns an empty FloatTuple0. For arrays with 1-9 elements, returns
     * the corresponding FloatTuple1-9 instance.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create from array
     * float[] values = {1.0f, 2.0f, 3.0f};
     * FloatTuple3 tuple = FloatTuple.create(values);
     *
     * // Empty array returns FloatTuple0
     * FloatTuple0 empty = FloatTuple.create(new float[0]);
     *
     * // Single element
     * FloatTuple1 single = FloatTuple.create(new float[]{3.14f});
     * }</pre>
     *
     * @param <TP> the specific FloatTuple subtype to return
     * @param a the array of float values (must have length 0-9), may be {@code null}
     * @return a FloatTuple of appropriate size containing the array elements
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
     * Returns the minimum float value in this tuple.
     * <p>
     * This method finds and returns the smallest float value among all elements
     * in the tuple. For tuples with a single element, returns that element.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
     * float min = tuple.min();  // 1.0f
     *
     * FloatTuple2 pair = FloatTuple.of(2.5f, 1.5f);
     * float minPair = pair.min();  // 1.5f
     * }</pre>
     *
     * @return the minimum float value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public float min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum float value in this tuple.
     * <p>
     * This method finds and returns the largest float value among all elements
     * in the tuple. For tuples with a single element, returns that element.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(3.0f, 1.0f, 2.0f);
     * float max = tuple.max();  // 3.0f
     *
     * FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
     * float maxPair = pair.max();  // 2.5f
     * }</pre>
     *
     * @return the maximum float value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public float max() {
        return N.max(elements());
    }

    /**
     * Returns the median value. For tuples with an even number of elements, returns the lower middle element.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Odd number of elements
     * FloatTuple3 tuple3 = FloatTuple.of(30.0f, 10.0f, 20.0f);
     * float median = tuple3.median();  // 20.0f (middle value when sorted: 10.0f, 20.0f, 30.0f)
     *
     * // Even number of elements
     * FloatTuple4 tuple4 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
     * float median2 = tuple4.median();  // 2.0f (lower middle value)
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
     * <p>
     * This method calculates the sum by adding all float elements together.
     * For an empty tuple, returns 0.0f.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * float sum = tuple.sum();  // 6.0f
     *
     * FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
     * float pairSum = pair.sum();  // 4.0f
     * }</pre>
     *
     * @return the sum of all float values in this tuple
     */
    public float sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all float values in this tuple.
     * <p>
     * This method calculates the arithmetic mean of all elements in the tuple.
     * The result is always returned as a double to preserve precision, even when
     * the average is a whole number.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * double avg = tuple.average();  // 2.0
     *
     * FloatTuple2 pair = FloatTuple.of(1.0f, 2.0f);
     * double avgPair = pair.average();  // 1.5
     * }</pre>
     *
     * @return the average of all float values in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public double average() {
        return N.average(elements());
    }

    /**
     * Returns a new tuple with the elements in reverse order.
     * <p>
     * This method creates and returns a new tuple instance with all elements in reversed order.
     * The original tuple remains unchanged. For example, a tuple (1.0f, 2.0f, 3.0f) becomes
     * (3.0f, 2.0f, 1.0f) when reversed.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * FloatTuple3 reversed = tuple.reverse();  // (3.0f, 2.0f, 1.0f)
     *
     * FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
     * FloatTuple2 reversedPair = pair.reverse();  // (2.5f, 1.5f)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified float value.
     * <p>
     * This method performs a linear search through all elements in the tuple to determine
     * if any element matches the specified value. Uses {@link N#equals(float, float)} for
     * comparison to handle NaN and precision correctly. Returns {@code true} if at least one
     * element equals the search value, {@code false} otherwise.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * boolean hasTwo = tuple.contains(2.0f);  // true
     * boolean hasFive = tuple.contains(5.0f);  // false
     *
     * FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
     * boolean has1_5 = pair.contains(1.5f);  // true
     * boolean has3_5 = pair.contains(3.5f);  // false
     * }</pre>
     *
     * @param valueToFind the float value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(float valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * <p>
     * This method creates a defensive copy of the internal array. Modifications to the
     * returned array will not affect the tuple since tuples are immutable.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * float[] array = tuple.toArray();  // [1.0f, 2.0f, 3.0f]
     * array[0] = 5.0f;  // Does not modify the original tuple
     *
     * FloatTuple0 empty = FloatTuple.create(new float[0]);
     * float[] emptyArray = empty.toArray();  // []
     * }</pre>
     *
     * @return a new float array containing all tuple elements
     */
    public float[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new FloatList containing all elements of this tuple.
     * <p>
     * This method converts the tuple into a mutable FloatList. The returned list is a new
     * instance, and modifications to it will not affect the original tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * FloatList list = tuple.toList();
     * list.add(4.0f);  // Does not affect the original tuple
     *
     * FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
     * FloatList pairList = pair.toList();  // [1.5f, 2.5f]
     * }</pre>
     *
     * @return a new FloatList containing all tuple elements
     */
    public FloatList toList() {
        return FloatList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     * <p>
     * This method iterates through all elements in the tuple in order, applying the specified
     * consumer action to each element. The action is performed for its side effects only.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * tuple.forEach(f -> System.out.print(f + " "));  // prints "1.0 2.0 3.0 "
     *
     * FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
     * FloatList list = FloatList.empty();
     * pair.forEach(list::add);  // adds 1.5f and 2.5f to the list
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the consumer
     * @param consumer the action to be performed for each element
     * @throws E if the consumer throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
        for (final float e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns a FloatStream of all elements in this tuple.
     * <p>
     * This method creates a sequential FloatStream with all elements from the tuple.
     * The stream provides a functional programming interface for processing the tuple elements
     * through operations like filter, map, and reduce.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * float sum = tuple.stream().sum();  // 6.0f
     *
     * FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
     * long count = pair.stream().filter(f -> f > 2.0f).count();  // 1
     * }</pre>
     *
     * @return a FloatStream containing all tuple elements
     */
    public FloatStream stream() {
        return FloatStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * <p>
     * The hash code is computed based on the contents of the tuple's elements.
     * Tuples with identical elements in the same order will have the same hash code.
     * This implementation ensures consistency with the {@link #equals(Object)} method.
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
     * <p>
     * Two tuples are considered equal if and only if:
     * </p>
     * <ul>
     * <li>They are of the exact same class (e.g., both FloatTuple2)</li>
     * <li>They contain the same elements in the same order</li>
     * </ul>
     * <p>
     * This method adheres to the general contract of {@link Object#equals(Object)}.
     * </p>
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
            return N.equals(elements(), ((FloatTuple<TP>) obj).elements());
        }
    }

    /**
     * Returns a string representation of this tuple.
     * <p>
     * The string representation consists of the tuple elements enclosed in parentheses "( )"
     * and separated by commas and spaces. This format provides a clear and readable
     * representation of the tuple's contents.
     * </p>
     *
     * <p><b>Example output:</b></p>
     * <ul>
     * <li>{@code (1.0, 2.0, 3.0)} - for a FloatTuple3</li>
     * <li>{@code (1.5, 2.5)} - for a FloatTuple2</li>
     * <li>{@code (3.14)} - for a FloatTuple1</li>
     * <li>{@code ()} - for an empty FloatTuple0</li>
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
     * underlying array of float values. The returned array is lazily initialized
     * on first access and cached for subsequent calls.
     * </p>
     * <p>
     * Subclasses must implement this method to provide access to their elements.
     * Modifications to the returned array will affect the tuple's internal state.
     * </p>
     *
     * @return the array of float elements stored in this tuple
     */
    protected abstract float[] elements();

    /**
     * An empty FloatTuple containing no elements.
     * <p>
     * This class represents a tuple with arity 0 (zero elements). It follows the singleton pattern,
     * with a single shared instance accessed via {@code FloatTuple.create(new float[0])} or returned
     * when creating tuples from null/empty arrays. All statistical operations on FloatTuple0 either
     * return 0 (for sum) or throw {@link NoSuchElementException} (for min, max, median, average).
     * </p>
     */
    static final class FloatTuple0 extends FloatTuple<FloatTuple0> {

        private static final FloatTuple0 EMPTY = new FloatTuple0();

        FloatTuple0() {
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
         * Returns the minimum float value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public float min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the maximum float value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public float max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the median float value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public float median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the sum of all float values in this tuple.
         * Since this tuple is empty, this method always returns 0.
         *
         * @return 0
         */
        @Override
        public float sum() {
            return 0;
        }

        /**
         * Returns the average of all float values in this tuple.
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
         * Returns a new tuple with the elements in reverse order.
         * Since this tuple is empty, returns itself.
         *
         * @return this FloatTuple0 instance
         */
        @Override
        public FloatTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified float value.
         * Since this tuple is empty, always returns false.
         *
         * @param valueToFind the float value to search for
         * @return {@code false} always, because the tuple is empty
         */
        @Override
        public boolean contains(final float valueToFind) {
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
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if _1 equals valueToFind, {@code false} otherwise
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
            return Float.hashCode(_1);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple1 with equal value
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
         * @return "(value)" where value is _1
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
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
         * Returns the median float value in this tuple.
         * For a tuple of two elements, returns the lower value.
         *
         * @return the median (lower) float value
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
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if either element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         * Performs the given bi-consumer on the two elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
         * tuple.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the bi-consumer to perform on the two elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.FloatBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given bi-function to the two elements and returns the result.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
         * float product = tuple.map((a, b) -> a * b);  // 12.0f
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the bi-function to apply to the two elements
         * @return the result of applying the mapper to _1 and _2
         * @throws E if the mapper throws an exception
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.FloatBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
         * Optional<FloatTuple2> result = tuple.filter((a, b) -> a + b > 5);  // present
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the bi-predicate to test the two elements
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
            return 31 * Float.floatToIntBits(_1) + Float.floatToIntBits(_2);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple2 with equal elements
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
         * @return "(_1, _2)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
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
         * Returns a new tuple with the elements in reverse order.
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
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
        }

        /**
         * Performs the given tri-consumer on the three elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * tuple.accept((a, b, c) -> System.out.println("Sum: " + (a + b + c)));
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the tri-consumer to perform on the three elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.FloatTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given tri-function to the three elements and returns the result.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * float product = tuple.map((a, b, c) -> a * b * c);  // 6.0f
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the tri-function to apply to the three elements
         * @return the result of applying the mapper to _1, _2, and _3
         * @throws E if the mapper throws an exception
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.FloatTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * Optional<FloatTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);  // present
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the tri-predicate to test the three elements
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
            return 31 * (31 * Float.floatToIntBits(_1) + Float.floatToIntBits(_2)) + Float.floatToIntBits(_3);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple3 with equal elements
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
         * @return "(_1, _2, _3)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
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
     * Provides direct access to elements via public final fields {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
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
         * Returns the minimum value among the four elements.
         *
         * @return the smallest of _1, _2, _3, and _4
         */
        @Override
        public float min() {
            return N.min(_1, _2, _3, _4);
        }

        /**
         * Returns the maximum value among the four elements.
         *
         * @return the largest of _1, _2, _3, and _4
         */
        @Override
        public float max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the median value of the four elements.
         * For an even number of elements, returns the lower middle value.
         *
         * @return the median float value
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of the four elements.
         *
         * @return _1 + _2 + _3 + _4
         */
        @Override
        public float sum() {
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * FloatTuple4 reversed = tuple.reverse();  // (4.0f, 3.0f, 2.0f, 1.0f)
         * }</pre>
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
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
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
            int result = 31 * Float.floatToIntBits(_1) + Float.floatToIntBits(_2);
            result = 31 * result + Float.floatToIntBits(_3);
            return 31 * result + Float.floatToIntBits(_4);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both FloatTuple4 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple4 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final FloatTuple4 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly five float values.
     * Provides direct access to elements via public final fields {@code _1} through {@code _5}.
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
         * Returns the minimum value among the five elements.
         *
         * @return the smallest of _1, _2, _3, _4, and _5
         */
        @Override
        public float min() {
            return N.min(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the maximum value among the five elements.
         *
         * @return the largest of _1, _2, _3, _4, and _5
         */
        @Override
        public float max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the median value of the five elements.
         *
         * @return the middle value when sorted
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of the five elements.
         *
         * @return _1 + _2 + _3 + _4 + _5
         */
        @Override
        public float sum() {
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * FloatTuple5 reversed = tuple.reverse();  // (5.0f, 4.0f, 3.0f, 2.0f, 1.0f)
         * }</pre>
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
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
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
            int result = 31 * Float.floatToIntBits(_1) + Float.floatToIntBits(_2);
            result = 31 * result + Float.floatToIntBits(_3);
            result = 31 * result + Float.floatToIntBits(_4);
            return 31 * result + Float.floatToIntBits(_5);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both FloatTuple5 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple5 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final FloatTuple5 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4) && N.equals(_5, other._5);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly six float values.
     * Provides direct access to elements via public final fields {@code _1} through {@code _6}.
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
         * Returns the minimum value among the six elements.
         *
         * @return the smallest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public float min() {
            return N.min(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the maximum value among the six elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public float max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median value of the six elements.
         * For an even number of elements, returns the lower middle value.
         *
         * @return the median float value
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of the six elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6
         */
        @Override
        public float sum() {
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * FloatTuple6 reversed = tuple.reverse();  // (6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f)
         * }</pre>
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
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
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
            int result = 31 * Float.floatToIntBits(_1) + Float.floatToIntBits(_2);
            result = 31 * result + Float.floatToIntBits(_3);
            result = 31 * result + Float.floatToIntBits(_4);
            result = 31 * result + Float.floatToIntBits(_5);
            return 31 * result + Float.floatToIntBits(_6);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both FloatTuple6 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple6 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final FloatTuple6 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4) && N.equals(_5, other._5)
                        && N.equals(_6, other._6);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly seven float values.
     * Provides direct access to elements via public final fields {@code _1} through {@code _7}.
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
         * Returns the minimum value among the seven elements.
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public float min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the maximum value among the seven elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public float max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the median value of the seven elements.
         *
         * @return the middle value when sorted
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of the seven elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7
         */
        @Override
        public float sum() {
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * FloatTuple7 reversed = tuple.reverse();  // (7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f)
         * }</pre>
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
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
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
            int result = 31 * Float.floatToIntBits(_1) + Float.floatToIntBits(_2);
            result = 31 * result + Float.floatToIntBits(_3);
            result = 31 * result + Float.floatToIntBits(_4);
            result = 31 * result + Float.floatToIntBits(_5);
            result = 31 * result + Float.floatToIntBits(_6);
            return 31 * result + Float.floatToIntBits(_7);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both FloatTuple7 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple7 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final FloatTuple7 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4) && N.equals(_5, other._5)
                        && N.equals(_6, other._6) && N.equals(_7, other._7);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly eight float values.
     * Provides direct access to elements via public final fields {@code _1} through {@code _8}.
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more float values
     */
    @Deprecated
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
         * Returns the minimum value among the eight elements.
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, _7, and _8
         */
        @Override
        public float min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the maximum value among the eight elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, and _8
         */
        @Override
        public float max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the median value of the eight elements.
         * For an even number of elements, returns the lower middle value.
         *
         * @return the median float value
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of the eight elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8
         */
        @Override
        public float sum() {
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * FloatTuple8 reversed = tuple.reverse();  // (8.0f, 7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f)
         * }</pre>
         *
         * @return a new FloatTuple8 with (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public FloatTuple8 reverse() {
            return new FloatTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
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
            int result = 31 * Float.floatToIntBits(_1) + Float.floatToIntBits(_2);
            result = 31 * result + Float.floatToIntBits(_3);
            result = 31 * result + Float.floatToIntBits(_4);
            result = 31 * result + Float.floatToIntBits(_5);
            result = 31 * result + Float.floatToIntBits(_6);
            result = 31 * result + Float.floatToIntBits(_7);
            return 31 * result + Float.floatToIntBits(_8);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both FloatTuple8 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple8 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final FloatTuple8 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4) && N.equals(_5, other._5)
                        && N.equals(_6, other._6) && N.equals(_7, other._7) && N.equals(_8, other._8);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A FloatTuple containing exactly nine float values.
     * Provides direct access to elements via public final fields {@code _1} through {@code _9}.
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more float values
     */
    @Deprecated
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
         * Returns the minimum value among the nine elements.
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, _7, _8, and _9
         */
        @Override
        public float min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the maximum value among the nine elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, _8, and _9
         */
        @Override
        public float max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the median value of the nine elements.
         *
         * @return the middle value when sorted
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of the nine elements.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9
         */
        @Override
        public float sum() {
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * FloatTuple9 reversed = tuple.reverse();  // (9.0f, 8.0f, 7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f)
         * }</pre>
         *
         * @return a new FloatTuple9 with (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public FloatTuple9 reverse() {
            return new FloatTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks whether this tuple contains the specified value.
         *
         * @param valueToFind the value to search for
         * @return {@code true} if any element equals valueToFind
         */
        @Override
        public boolean contains(final float valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> consumer) throws E {
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
            int result = 31 * Float.floatToIntBits(_1) + Float.floatToIntBits(_2);
            result = 31 * result + Float.floatToIntBits(_3);
            result = 31 * result + Float.floatToIntBits(_4);
            result = 31 * result + Float.floatToIntBits(_5);
            result = 31 * result + Float.floatToIntBits(_6);
            result = 31 * result + Float.floatToIntBits(_7);
            result = 31 * result + Float.floatToIntBits(_8);
            return 31 * result + Float.floatToIntBits(_9);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both FloatTuple9 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple9 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final FloatTuple9 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3) && N.equals(_4, other._4) && N.equals(_5, other._5)
                        && N.equals(_6, other._6) && N.equals(_7, other._7) && N.equals(_8, other._8) && N.equals(_9, other._9);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}