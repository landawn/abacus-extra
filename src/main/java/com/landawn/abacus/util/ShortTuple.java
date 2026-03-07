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
 * Abstract base class for immutable tuple implementations that hold primitive short values.
 * <p>
 * This class provides common functionality for short-based tuples of various sizes (0 to 9 elements).
 * ShortTuple is designed to be a lightweight, type-safe container for multiple short values
 * that can be used as a composite key, return multiple values from a method, or group related
 * short values together. All short tuple implementations extend this class and are immutable by design.
 * </p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Creating tuples
 * ShortTuple.ShortTuple1 single = ShortTuple.of((short) 42);
 * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 1, (short) 2);
 * ShortTuple.ShortTuple3 triple = ShortTuple.of((short) 1, (short) 2, (short) 3);
 *
 * // Using statistical operations
 * short min = triple.min();         // 1
 * short max = triple.max();         // 3
 * double avg = triple.average();    // 2.0
 * }</pre>
 *
 * @param <TP> the specific ShortTuple subtype
 * @see PrimitiveTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class ShortTuple<TP extends ShortTuple<TP>> extends PrimitiveTuple<TP> {

    /**
     * Protected constructor for subclass instantiation.
     * <p>
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link #of(short)}, {@link #of(short, short)}, etc.,
     * to create tuple instances.
     * </p>
     */
    protected ShortTuple() {
    }

    protected volatile short[] elements;

    /**
     * Creates a ShortTuple.ShortTuple1 containing a single short value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple1 single = ShortTuple.of((short)42);
     * short value = single._1;  // 42
     * }</pre>
     *
     * @param _1 the short value to store in the tuple
     * @return a new ShortTuple.ShortTuple1 containing the specified value
     */
    public static ShortTuple1 of(final short _1) {
        return new ShortTuple1(_1);
    }

    /**
     * Creates a ShortTuple.ShortTuple2 containing two short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short)1, (short)2);
     * int sum = pair._1 + pair._2;  // 3
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @return a new ShortTuple.ShortTuple2 containing the specified values
     */
    public static ShortTuple2 of(final short _1, final short _2) {
        return new ShortTuple2(_1, _2);
    }

    /**
     * Creates a ShortTuple.ShortTuple3 containing three short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 triple = ShortTuple.of((short)1, (short)2, (short)3);
     * double average = triple.average();   // 2.0
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @return a new ShortTuple.ShortTuple3 containing the specified values
     */
    public static ShortTuple3 of(final short _1, final short _2, final short _3) {
        return new ShortTuple3(_1, _2, _3);
    }

    /**
     * Creates a ShortTuple.ShortTuple4 containing four short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple4 quad = ShortTuple.of((short)1, (short)2, (short)3, (short)4);
     * // quad._1 == 1, quad._2 == 2, quad._3 == 3, quad._4 == 4
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @return a new ShortTuple.ShortTuple4 containing the specified values
     */
    public static ShortTuple4 of(final short _1, final short _2, final short _3, final short _4) {
        return new ShortTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a ShortTuple.ShortTuple5 containing five short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple5 quint = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5);
     * // quint._5 == 5
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @param _5 the fifth short value
     * @return a new ShortTuple.ShortTuple5 containing the specified values
     */
    public static ShortTuple5 of(final short _1, final short _2, final short _3, final short _4, final short _5) {
        return new ShortTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a ShortTuple.ShortTuple6 containing six short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple6 sext = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6);
     * // sext._6 == 6
     * }</pre>
     *
     * @param _1 the first short value
     * @param _2 the second short value
     * @param _3 the third short value
     * @param _4 the fourth short value
     * @param _5 the fifth short value
     * @param _6 the sixth short value
     * @return a new ShortTuple.ShortTuple6 containing the specified values
     */
    public static ShortTuple6 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6) {
        return new ShortTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a ShortTuple.ShortTuple7 containing seven short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple7 sept = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
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
     * @return a new ShortTuple.ShortTuple7 containing the specified values
     */
    public static ShortTuple7 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7) {
        return new ShortTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a ShortTuple.ShortTuple8 containing eight short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple8 oct = ShortTuple.of((short)1, (short)2, (short)3, (short)4,
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
     * @return a new ShortTuple.ShortTuple8 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more short values
     */
    @Deprecated
    public static ShortTuple8 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7,
            final short _8) {
        return new ShortTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a ShortTuple.ShortTuple9 containing nine short values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple9 non = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5,
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
     * @return a new ShortTuple.ShortTuple9 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more short values
     */
    @Deprecated
    public static ShortTuple9 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7, final short _8,
            final short _9) {
        return new ShortTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a ShortTuple from an array of short values.
     * <p>
     * The size of the returned tuple depends on the length of the input array.
     * This factory method supports arrays with 0 to 9 elements. For empty or null
     * arrays, returns an empty {@code ShortTuple<?>}. For arrays with 1-9 elements, returns
     * the corresponding ShortTuple.ShortTuple1-9 instance.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create from array
     * short[] values = {1, 2, 3};
     * ShortTuple.ShortTuple3 tuple = ShortTuple.copyOf(values);
     *
     * // Empty array returns ShortTuple<?>
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     *
     * // Single element
     * ShortTuple.ShortTuple1 single = ShortTuple.copyOf(new short[]{1});
     * }</pre>
     *
     * <p><strong>Type note:</strong> the runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of short values (must have length 0-9), may be {@code null}
     * @return a ShortTuple of appropriate size containing the array values, or an empty ShortTuple if the array is null or empty
     * @throws IllegalArgumentException if the array has more than 9 elements
     */
    @SuppressWarnings("deprecation")
    public static <TP extends ShortTuple<TP>> TP copyOf(final short[] values) {
        if (values == null || values.length == 0) {
            return (TP) ShortTuple0.EMPTY;
        }

        switch (values.length) {
            case 1:
                return (TP) ShortTuple.of(values[0]);

            case 2:
                return (TP) ShortTuple.of(values[0], values[1]);

            case 3:
                return (TP) ShortTuple.of(values[0], values[1], values[2]);

            case 4:
                return (TP) ShortTuple.of(values[0], values[1], values[2], values[3]);

            case 5:
                return (TP) ShortTuple.of(values[0], values[1], values[2], values[3], values[4]);

            case 6:
                return (TP) ShortTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);

            case 7:
                return (TP) ShortTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);

            case 8:
                return (TP) ShortTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);

            case 9:
                return (TP) ShortTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);

            default:
                throw new IllegalArgumentException("Too many elements (" + values.length + "). Maximum: 9");
        }
    }

    /**
     * Returns the minimum short value in this tuple.
     * <p>
     * Iterates through all elements in the tuple and returns the smallest value.
     * For single-element tuples, the element itself is returned.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 tuple = ShortTuple.of((short)3, (short)1, (short)2);
     * short min = tuple.min();   // 1
     *
     * ShortTuple.ShortTuple1 single = ShortTuple.of((short)42);
     * short singleMin = single.min();   // 42
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
     * <p>
     * Iterates through all elements in the tuple and returns the largest value.
     * For single-element tuples, the element itself is returned.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 tuple = ShortTuple.of((short)3, (short)1, (short)2);
     * short max = tuple.max();   // 3
     *
     * ShortTuple.ShortTuple1 single = ShortTuple.of((short)42);
     * short singleMax = single.max();   // 42
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
     * <p>
     * The median is calculated by sorting the elements internally. For tuples with an odd
     * number of elements, returns the middle value when sorted. For tuples with an even
     * number of elements, returns the lower middle value when sorted.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 tuple = ShortTuple.of((short)1, (short)3, (short)2);
     * short median = tuple.median();   // 2 (middle value when sorted: 1, 2, 3)
     *
     * ShortTuple.ShortTuple4 evenTuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4);
     * short evenMedian = evenTuple.median();   // 2 (lower middle when sorted: 1, [2], 3, 4)
     * }</pre>
     *
     * @return the median short value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public short median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all elements in this tuple as an {@code int}.
     * <p>
     * While this tuple stores short values, the sum is returned as an {@code int}
     * to prevent overflow when adding multiple short values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * int sum = tuple.sum();   // 6 (returned as int, not short)
     *
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short)100, (short)200);
     * int total = pair.sum();  // 300
     * }</pre>
     *
     * @return the sum of all short values in this tuple as an {@code int}
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all short values in this tuple as a double.
     * <p>
     * Note: The result is returned as a double to preserve precision. The average is
     * calculated by converting short values to double during computation.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple4 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4);
     * double avg = tuple.average();   // 2.5
     * }</pre>
     *
     * @return the average of all short values in this tuple as a double
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
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 1, (short) 2);
     * ShortTuple.ShortTuple2 reversedPair = pair.reverse();   // (2, 1)
     *
     * ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * ShortTuple.ShortTuple3 reversed = tuple.reverse();   // (3, 2, 1)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified short value.
     * <p>
     * This method performs a linear search through all elements in the tuple to determine
     * if any element matches the specified value. Returns {@code true} if at least one
     * element equals the search value, {@code false} otherwise.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * boolean has2 = tuple.contains((short) 2);   // true
     * boolean has5 = tuple.contains((short) 5);   // false
     *
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 10, (short) 10);
     * boolean hasTen = pair.contains((short) 10);   // true
     * boolean hasOne = pair.contains((short) 1);    // false
     * }</pre>
     *
     * @param valueToFind the short value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(short valueToFind);

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
     * ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * short[] array = tuple.toArray();   // [1, 2, 3]
     * array[0] = 99;  // Does not modify the tuple
     *
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 10, (short) 20);
     * short[] pairArray = pair.toArray();   // [10, 20]
     * }</pre>
     *
     * @return a new short array containing all tuple elements
     */
    public short[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new ShortList containing all elements of this tuple.
     * <p>
     * Converts this tuple to a mutable {@link ShortList} containing all elements
     * in their original order. The returned list is a new instance and modifications
     * to it do not affect the original tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * ShortList list = tuple.toList();
     * list.add((short) 4);   // Adds to the list, tuple remains unchanged
     *
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 10, (short) 20);
     * ShortList pairList = pair.toList();   // [10, 20]
     * }</pre>
     *
     * @return a new ShortList containing all tuple elements
     */
    public ShortList toList() {
        return ShortList.of(elements().clone());
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
     * ShortTuple.ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * tuple.forEach(v -> System.out.println("Value: " + v));
     * // Output: Value: 1, Value: 2, Value: 3
     *
     * // Accumulate sum externally
     * java.util.concurrent.atomic.AtomicInteger total = new java.util.concurrent.atomic.AtomicInteger();
     * tuple.forEach(v -> total.addAndGet(v));
     * // total is now 6
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the consumer
     * @param consumer the action to be performed for each element, must not be {@code null}
     * @throws E if the consumer throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
        for (final short element : elements()) {
            consumer.accept(element);
        }
    }

    /**
     * Returns a ShortStream of all elements in this tuple.
     * <p>
     * Converts this tuple to a sequential {@link ShortStream} containing all elements
     * in their original order. This allows using standard stream operations like filter,
     * map, and reduce on the tuple elements.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 tuple = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * int sum = tuple.stream().sum();   // 6
     *
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 10, (short) 20);
     * short max = pair.stream().max().getAsShort();   // 20
     * }</pre>
     *
     * @return a ShortStream containing all tuple elements
     */
    public ShortStream stream() {
        return ShortStream.of(elements());
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
     *   <li>They contain the same short values in the same order</li>
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
            return N.equals(elements(), ((ShortTuple<TP>) obj).elements());
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
     *   <li>{@code (1, 2, 3)} for a ShortTuple.ShortTuple3</li>
     *   <li>{@code (1, 2)} for a ShortTuple.ShortTuple2</li>
     *   <li>{@code (1)} for a ShortTuple.ShortTuple1</li>
     *   <li>{@code ()} for an empty {@code ShortTuple<?>}</li>
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
     * and is returned by {@link #copyOf(short[])} when passed a null or empty array.
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
         * Returns the sum of all values in this tuple as an int.
         * For an empty tuple, the sum is 0.
         *
         * @return 0 as an int
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
         * @param valueToFind the short value to search for
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
     * ShortTuple.ShortTuple1 single = ShortTuple.of((short)42);
     * short value = single._1;  // 42
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
         * Returns the sum of all values in this tuple as an int.
         * For a single-element tuple, this is the element itself converted to int.
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
         * @return a new ShortTuple.ShortTuple1 with the same value
         */
        @Override
        public ShortTuple1 reverse() {
            return new ShortTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * @param valueToFind the short value to search for
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
         * @return {@code true} if the object is a ShortTuple.ShortTuple1 with the same value
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
     *   <li>{@link #filter(Throwables.ShortBiPredicate)} - conditionally wrap in u.Optional</li>
     * </ul>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short)3, (short)5);
     * int product = pair.map((a, b) -> a * b);   // 15
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
         * Returns the sum of the two elements as an int.
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
         * @return a new ShortTuple.ShortTuple2 with values (_2, _1)
         */
        @Override
        public ShortTuple2 reverse() {
            return new ShortTuple2(_2, _1);
        }

        /**
         * Checks if either element equals the specified value.
         *
         * @param valueToFind the short value to search for
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
         * Performs the given bi-consumer action on the two elements of this tuple.
         * <p>
         * This method applies the specified bi-consumer to both elements simultaneously,
         * allowing operations that need to work with both values together. The action is
         * executed for its side effects only.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 pair = ShortTuple.of((short)3, (short)5);
         * pair.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * // Prints: 3 + 5 = 8
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
         * <p>
         * This method transforms both elements of the tuple into a single result value
         * of type {@code U}. The mapper function receives both elements as parameters and
         * can perform any calculation or transformation on them.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 pair = ShortTuple.of((short)3, (short)5);
         * int product = pair.map((a, b) -> a * b);   // 15
         *
         * String desc = pair.map((a, b) -> a + " and " + b);   // "3 and 5"
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
         * <p>
         * This method evaluates the given bi-predicate against both elements of the tuple.
         * If the predicate returns {@code true}, returns an Optional containing this tuple.
         * If it returns {@code false}, returns an empty Optional.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 pair = ShortTuple.of((short)3, (short)5);
         * u.Optional<ShortTuple.ShortTuple2> result = pair.filter((a, b) -> a < b);
         * // Returns: Optional containing the pair (since 3 < 5)
         *
         * u.Optional<ShortTuple.ShortTuple2> empty = pair.filter((a, b) -> a > b);
         * // Returns: Optional.empty() (since 3 is not > 5)
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
         * @return {@code true} if the object is a ShortTuple.ShortTuple2 with the same values
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
     *   <li>{@link #filter(Throwables.ShortTriPredicate)} - conditionally wrap in u.Optional</li>
     * </ul>
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 triple = ShortTuple.of((short)2, (short)3, (short)5);
     * int sum = triple.map((a, b, c) -> a + b + c);   // 10
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
         * Returns the sum of all three elements as an int.
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
         * @return a new ShortTuple.ShortTuple3 with values (_3, _2, _1)
         */
        @Override
        public ShortTuple3 reverse() {
            return new ShortTuple3(_3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the short value to search for
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
         * Performs the given tri-consumer action on the three elements of this tuple.
         * <p>
         * This method applies the specified tri-consumer to all three elements simultaneously,
         * allowing operations that need to work with all values together. The action is
         * executed for its side effects only.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 triple = ShortTuple.of((short)2, (short)3, (short)5);
         * triple.accept((a, b, c) -> System.out.println(a + " + " + b + " + " + c + " = " + (a + b + c)));
         * // Prints: 2 + 3 + 5 = 10
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
         * <p>
         * This method transforms all three elements of the tuple into a single result value
         * of type {@code U}. The mapper function receives all three elements as parameters and
         * can perform any calculation or transformation on them.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 triple = ShortTuple.of((short)2, (short)3, (short)5);
         * int product = triple.map((a, b, c) -> a * b * c);   // 30
         *
         * String desc = triple.map((a, b, c) -> a + ", " + b + ", " + c);   // "2, 3, 5"
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
         * <p>
         * This method evaluates the given tri-predicate against all three elements of the tuple.
         * If the predicate returns {@code true}, returns an Optional containing this tuple.
         * If it returns {@code false}, returns an empty Optional.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 triple = ShortTuple.of((short)2, (short)3, (short)5);
         * u.Optional<ShortTuple.ShortTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
         * // Returns: Optional containing the triple (since 2 < 3 < 5)
         *
         * u.Optional<ShortTuple.ShortTuple3> empty = triple.filter((a, b, c) -> a + b + c > 20);
         * // Returns: Optional.empty() (since 2 + 3 + 5 = 10 is not > 20)
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
         * @return {@code true} if the object is a ShortTuple.ShortTuple3 with the same values
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
     * ShortTuple.ShortTuple4 quad = ShortTuple.of((short)1, (short)2, (short)3, (short)4);
     * double avg = quad.average();   // 2.5
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
            return N.min(_1, _2, _3, _4);
        }

        /**
         * Returns the maximum value among the four elements.
         *
         * @return the largest of _1, _2, _3, and _4
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the median value of the four elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) short value when sorted
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of all four elements as an int.
         *
         * @return _1 + _2 + _3 + _4 as an int
         */
        @Override
        public int sum() {
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
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple4 with values (_4, _3, _2, _1)
         */
        @Override
        public ShortTuple4 reverse() {
            return new ShortTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the short value to search for
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
         * Two tuples are equal if they are both ShortTuple.ShortTuple4 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple.ShortTuple4 with equal elements, {@code false} otherwise
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
     * ShortTuple.ShortTuple5 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5);
     * short median = tuple.median();   // 3
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
            return N.min(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the maximum value among the five elements.
         *
         * @return the largest of _1, _2, _3, _4, and _5
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the median value of the five elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median short value
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of all five elements as an int.
         *
         * @return _1 + _2 + _3 + _4 + _5 as an int
         */
        @Override
        public int sum() {
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
         * @return a new ShortTuple.ShortTuple5 with values (_5, _4, _3, _2, _1)
         */
        @Override
        public ShortTuple5 reverse() {
            return new ShortTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the short value to search for
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
         * Two tuples are equal if they are both ShortTuple.ShortTuple5 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple.ShortTuple5 with equal elements, {@code false} otherwise
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
     * ShortTuple.ShortTuple6 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6);
     * int sum = tuple.sum();   // 21
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
            return N.min(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the maximum value among the six elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median value of the six elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) short value when sorted
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of all six elements as an int.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 as an int
         */
        @Override
        public int sum() {
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
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple6 with values (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public ShortTuple6 reverse() {
            return new ShortTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the short value to search for
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
         * Two tuples are equal if they are both ShortTuple.ShortTuple6 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple.ShortTuple6 with equal elements, {@code false} otherwise
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
     * ShortTuple.ShortTuple7 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
     * ShortTuple.ShortTuple7 reversed = tuple.reverse();   // (7, 6, 5, 4, 3, 2, 1)
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
            return N.min(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the maximum value among the seven elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the median value of the seven elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median short value
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of all seven elements as an int.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 as an int
         */
        @Override
        public int sum() {
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
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple7 with values (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public ShortTuple7 reverse() {
            return new ShortTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the short value to search for
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
         * Two tuples are equal if they are both ShortTuple.ShortTuple7 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple.ShortTuple7 with equal elements, {@code false} otherwise
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
     * ShortTuple.ShortTuple8 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4,
     *                                    (short)5, (short)6, (short)7, (short)8);
     * boolean contains5 = tuple.contains((short)5);   // true
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
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the maximum value among the eight elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, and _8
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the median value of the eight elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) short value when sorted
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of all eight elements as an int.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 as an int
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the average of all eight elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8) / 8.0 as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple8 with values (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public ShortTuple8 reverse() {
            return new ShortTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the short value to search for
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
         * Two tuples are equal if they are both ShortTuple.ShortTuple8 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple.ShortTuple8 with equal elements, {@code false} otherwise
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
     * ShortTuple.ShortTuple9 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5,
     *                                    (short)6, (short)7, (short)8, (short)9);
     * double avg = tuple.average();   // 5.0
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more short values
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
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the maximum value among the nine elements.
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, _8, and _9
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the median value of the nine elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the median short value
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of all nine elements as an int.
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9 as an int
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the average of all nine elements.
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9) / 9.0 as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple9 with values (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public ShortTuple9 reverse() {
            return new ShortTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * @param valueToFind the short value to search for
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
         * Two tuples are equal if they are both ShortTuple.ShortTuple9 instances
         * and all corresponding elements are equal.
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a ShortTuple.ShortTuple9 with equal elements, {@code false} otherwise
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
