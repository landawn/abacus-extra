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
 * Base class for immutable tuples of primitive {@code short} values.
 *
 * <p>The nested tuple types model fixed arities from 0 through 9. Factory methods such as
 * {@link #copyOf(short[])} and the {@code of(...)} overloads select the matching subtype, while the
 * base class supplies aggregate, reversal, containment, and functional helper operations.</p>
 *
 * @param <TP> the specific ShortTuple subtype
 * @see PrimitiveTuple
 * @see IntTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class ShortTuple<TP extends ShortTuple<TP>> extends PrimitiveTuple<TP> {

    /** Lazily initialized cached array view of all tuple elements. */
    protected volatile short[] elements;

    /**
     * Protected constructor for subclass instantiation.
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link #of(short)}, {@link #of(short, short)}, etc., to create tuple instances.
     */
    protected ShortTuple() {
    }

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
     * This factory method supports arrays with 0 to 9 elements. For {@code null} or empty
     * arrays, returns the shared empty tuple. For arrays with 1-9 elements, returns the
     * corresponding {@code ShortTuple1}..{@code ShortTuple9} instance. The values are copied
     * into the new tuple; subsequent modifications to the input array do not affect it.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create from array
     * short[] values = {1, 2, 3};
     * ShortTuple.ShortTuple3 tuple = ShortTuple.copyOf(values);
     *
     * // Empty array returns the empty ShortTuple
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     *
     * // Single element
     * ShortTuple.ShortTuple1 single = ShortTuple.copyOf(new short[]{1});
     * }</pre>
     *
     * <p><strong>Type note:</strong> the runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type. Assigning to the wrong arity-specific subtype will result in a
     * {@link ClassCastException} at the assignment site.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of short values (must have length 0-9), may be {@code null}
     * @return a ShortTuple of appropriate size containing the array values, or the empty ShortTuple if the array is {@code null} or empty
     * @throws IllegalArgumentException if the array has more than 9 elements
     * @see #of(short)
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
     * Iterates through all elements in the tuple and returns the smallest value
     * using signed short comparison. For single-element tuples, the element itself
     * is returned.
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
     * @see #max()
     * @see #median()
     */
    public short min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum short value in this tuple.
     * <p>
     * Iterates through all elements in the tuple and returns the largest value
     * using signed short comparison. For single-element tuples, the element itself
     * is returned.
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
     * @see #min()
     * @see #median()
     */
    public short max() {
        return N.max(elements());
    }

    /**
     * Returns the median short value in this tuple.
     * <p>
     * For tuples with an odd number of elements, returns the middle value when sorted.
     * For tuples with an even number of elements, returns the lower of the two middle
     * values when sorted. The original tuple is not modified by this operation.
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
     * @see #min()
     * @see #max()
     * @see N#median(short...)
     */
    public short median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all short values in this tuple as an {@code int}.
     * <p>
     * While this tuple stores short values, the sum is returned as an {@code int}
     * to prevent overflow when adding multiple short values together. For an empty
     * tuple the sum is {@code 0}.
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
     * @see #average()
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all short values in this tuple as a {@code double}.
     * <p>
     * The result is returned as a {@code double} to preserve precision; the values are
     * widened to {@code double} during computation.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple4 tuple = ShortTuple.of((short)1, (short)2, (short)3, (short)4);
     * double avg = tuple.average();   // 2.5
     * }</pre>
     *
     * @return the average of all short values in this tuple as a {@code double}
     * @throws NoSuchElementException if the tuple is empty
     * @see #sum()
     */
    public double average() {
        return N.average(elements());
    }

    /**
     * Returns a new tuple with the elements in reverse order.
     * <p>
     * This method creates and returns a new tuple instance of the same arity-specific
     * subtype with all elements in reversed order. The original tuple remains unchanged
     * as tuples are immutable. The empty tuple returns itself.
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
     * @return a new tuple of the same arity with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified short value.
     * <p>
     * Each subclass implements this by comparing {@code valueToFind} against every
     * element of the tuple. Returns {@code true} if at least one element equals the
     * search value, {@code false} otherwise. For an empty tuple, always returns {@code false}.
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
     * to the returned array do not affect the tuple, preserving immutability. The
     * returned array has length equal to the tuple's arity.
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
     * @return a new {@code short[]} array containing all tuple elements in order
     * @see #toList()
     * @see #stream()
     */
    public short[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new {@link ShortList} containing all elements of this tuple.
     * <p>
     * Converts this tuple to a mutable {@code ShortList} containing all elements
     * in their original order. The returned list is backed by a fresh array;
     * modifications to it do not affect this tuple.
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
     * @return a new {@code ShortList} containing all tuple elements in order
     * @see #toArray()
     * @see #stream()
     */
    public ShortList toList() {
        return ShortList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     * <p>
     * Iterates through all elements in this tuple in order, executing the provided
     * consumer action for each element. This method is primarily used for side effects
     * such as logging, printing, or updating external state. Because tuples are immutable,
     * the iteration does not modify this tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 tuple = ShortTuple.of((short)1, (short)2, (short)3);
     * tuple.forEach(v -> System.out.println("Value: " + v));
     * // Prints, one per line: "Value: 1", then "Value: 2", then "Value: 3"
     *
     * // Accumulate sum externally
     * java.util.concurrent.atomic.AtomicInteger total = new java.util.concurrent.atomic.AtomicInteger();
     * tuple.forEach(v -> total.addAndGet(v));
     * // total is now 6
     * }</pre>
     *
     * @param <E> the type of exception that the consumer may throw
     * @param consumer the action to be performed for each element, must not be {@code null}
     * @throws IllegalArgumentException if {@code consumer} is {@code null}
     * @throws E if the consumer throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
        N.checkArgNotNull(consumer);

        for (final short element : elements()) {
            consumer.accept(element);
        }
    }

    /**
     * Returns a {@link ShortStream} of all elements in this tuple.
     * <p>
     * Converts this tuple to a sequential {@code ShortStream} containing all elements
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
     * @return a sequential {@code ShortStream} containing all tuple elements in order
     * @see #toArray()
     * @see #toList()
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
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        return N.hashCode(elements());
    }

    /**
     * Compares this tuple to the specified object for equality.
     *
     * <p>Two tuples are considered equal if and only if either:</p>
     * <ul>
     *   <li>They are the same object (reference equality), or</li>
     *   <li>The other object is non-null, is an instance of the exact same runtime class
     *       (so a {@code ShortTuple2} never equals a {@code ShortTuple3}), and contains
     *       the same short values in the same order.</li>
     * </ul>
     *
     * <p>This method is consistent with {@link #hashCode()}.</p>
     *
     * @param obj the object to be compared for equality with this tuple
     * @return {@code true} if the specified object is equal to this tuple, {@code false} otherwise
     * @see #hashCode()
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
     * The empty tuple renders as {@code "()"}.
     * </p>
     *
     * @return a string representation of this tuple
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    /**
     * Returns the cached array view of the tuple contents.
     * <p>
     * Implementations lazily initialize this array on first access and then reuse it on subsequent
     * calls. The returned array is a live internal cache, not a defensive copy. Callers must not
     * modify it; mutation would corrupt the tuple's apparent immutability and the result of
     * {@link #hashCode()}.
     * </p>
     *
     * @return the internal {@code short[]} array of elements stored in this tuple (never {@code null};
     *         length equals {@link #arity()})
     */
    protected abstract short[] elements();

    /**
     * An empty ShortTuple containing no elements (arity 0).
     * <p>
     * This package-private class is exposed only through the base {@code ShortTuple} type
     * via the singleton instance returned by {@link #copyOf(short[])} when invoked with a
     * {@code null} or zero-length array. {@link #sum()} returns 0, while {@link #min()},
     * {@link #max()}, {@link #median()}, and {@link #average()} all throw {@link java.util.NoSuchElementException}.
     * </p>
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
         * Returns the sum of all short values in this tuple as an int.
         * Since this tuple is empty, always returns 0.
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
         * Returns this empty tuple (reversing an empty tuple has no effect).
         *
         * @return this {@code ShortTuple0} instance
         */
        @Override
        public ShortTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified short value.
         * An empty tuple never contains any value.
         *
         * @param valueToFind the short value to search for
         * @return {@code false} always, because this tuple contains no elements
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

        /**
         * Returns the shared empty short array.
         *
         * @return {@link N#EMPTY_SHORT_ARRAY}, the canonical empty short array
         */
        @Override
        protected short[] elements() {
            return N.EMPTY_SHORT_ARRAY;
        }
    }

    /**
     * A ShortTuple containing exactly one short element.
     * <p>
     * Provides direct access to the element through the public final field {@code _1}.
     * This is the simplest non-empty tuple type, useful for wrapping a single short value
     * in a tuple context.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple1 single = ShortTuple.of((short) 42);
     * short value = single._1;  // 42
     * short min = single.min();  // 42 (single element)
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
         * Returns the minimum short value in this tuple.
         * Since this tuple contains only one element, it returns that element.
         *
         * @return the single short value in this tuple
         */
        @Override
        public short min() {
            return _1;
        }

        /**
         * Returns the maximum short value in this tuple.
         * Since this tuple contains only one element, it returns that element.
         *
         * @return the single short value in this tuple
         */
        @Override
        public short max() {
            return _1;
        }

        /**
         * Returns the median short value in this tuple.
         * Since this tuple contains only one element, it returns that element.
         *
         * @return the single short value in this tuple
         */
        @Override
        public short median() {
            return _1;
        }

        /**
         * Returns the sum of all short values in this tuple as an int.
         * Since this tuple contains only one element, it returns that element widened to an {@code int}.
         *
         * @return the single short value widened to an {@code int}
         */
        @Override
        public int sum() {
            return _1;
        }

        /**
         * Returns the average of all short values in this tuple as a double.
         * Since this tuple contains only one element, it returns that element converted to a {@code double}.
         *
         * @return the single short value as a {@code double}
         */
        @Override
        public double average() {
            return _1;
        }

        /**
         * Returns a new ShortTuple.ShortTuple1 with the same element.
         * Since this tuple has only one element, reversing has no effect on the contained value.
         *
         * @return a new ShortTuple.ShortTuple1 with the same element
         */
        @Override
        public ShortTuple1 reverse() {
            return new ShortTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified short value.
         *
         * @param valueToFind the short value to search for
         * @return {@code true} if the element equals {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind;
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return the single short element widened to an {@code int}
         */
        @Override
        public int hashCode() {
            return _1;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ShortTuple.ShortTuple1 with the same element, {@code false} otherwise
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
         * @return a string representation in the format "(element)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
        }

        /**
         * Returns the internal array of short elements.
         * The array is lazily initialized on first access.
         *
         * @return a short array containing the single element
         */
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
     * <p>In addition to the operations inherited from {@link ShortTuple}, this class provides
     * functional helpers for working with pairs:</p>
     * <ul>
     *   <li>{@link #accept(Throwables.ShortBiConsumer)} - consume both values</li>
     *   <li>{@link #map(Throwables.ShortBiFunction)} - transform the pair to a single value</li>
     *   <li>{@link #filter(Throwables.ShortBiPredicate)} - conditionally wrap in {@link Optional}</li>
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
         * Returns the minimum short value in this tuple.
         *
         * @return the smaller of the two short values
         */
        @Override
        public short min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum short value in this tuple.
         *
         * @return the larger of the two short values
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
         * Returns the sum of all short values in this tuple as an int.
         *
         * @return the sum of both short values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2);
        }

        /**
         * Returns the average of all short values in this tuple as a double.
         *
         * @return the average of both short values as a {@code double}
         */
        @Override
        public double average() {
            return N.average(_1, _2);
        }

        /**
         * Returns a new ShortTuple.ShortTuple2 with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple2 with the elements swapped
         */
        @Override
        public ShortTuple2 reverse() {
            return new ShortTuple2(_2, _1);
        }

        /**
         * Checks if this tuple contains the specified short value.
         *
         * @param valueToFind the short value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code consumer} is {@code null}
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
            N.checkArgNotNull(consumer);

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
         * @param action the bi-consumer to perform on the two elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         * @see #map(Throwables.ShortBiFunction)
         * @see #filter(Throwables.ShortBiPredicate)
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
         * @param mapper the bi-function to apply to the two elements, must not be {@code null}
         * @return the result of applying the mapper function (may be {@code null} if the mapper returns {@code null})
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception during execution
         * @see #accept(Throwables.ShortBiConsumer)
         * @see #filter(Throwables.ShortBiPredicate)
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
         * Optional<ShortTuple.ShortTuple2> result = pair.filter((a, b) -> a < b);
         * // Returns: Optional containing the pair (since 3 < 5)
         *
         * Optional<ShortTuple.ShortTuple2> empty = pair.filter((a, b) -> a > b);
         * // Returns: Optional.empty() (since 3 is not > 5)
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the bi-predicate to test the two elements, must not be {@code null}
         * @return an {@code Optional} containing this tuple if the predicate returns {@code true}, otherwise an empty {@code Optional}
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception during evaluation
         * @see #accept(Throwables.ShortBiConsumer)
         * @see #map(Throwables.ShortBiFunction)
         */
        public <E extends Exception> Optional<ShortTuple2> filter(final Throwables.ShortBiPredicate<E> predicate) throws E {
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
         * @return {@code true} if the specified object is a ShortTuple.ShortTuple2 with the same elements in the same order, {@code false} otherwise
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
         * @return a string representation in the format "(element1, element2)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
        }

        /**
         * Returns the internal array of short elements.
         * The array is lazily initialized on first access.
         *
         * @return a short array containing all elements in order
         */
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
     * <p>In addition to the operations inherited from {@link ShortTuple}, this class provides
     * functional helpers for working with triples:</p>
     * <ul>
     *   <li>{@link #accept(Throwables.ShortTriConsumer)} - consume all three values</li>
     *   <li>{@link #map(Throwables.ShortTriFunction)} - transform the triple to a single value</li>
     *   <li>{@link #filter(Throwables.ShortTriPredicate)} - conditionally wrap in {@link Optional}</li>
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
         * Returns the minimum short value in this tuple.
         *
         * @return the smallest of the three short values
         */
        @Override
        public short min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum short value in this tuple.
         *
         * @return the largest of the three short values
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median short value in this tuple.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the middle short value when sorted
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of all short values in this tuple as an int.
         *
         * @return the sum of all three short values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3);
        }

        /**
         * Returns the average of all short values in this tuple as a double.
         *
         * @return the average of all three short values as a {@code double}
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3);
        }

        /**
         * Returns a new ShortTuple.ShortTuple3 with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple3 with the elements in reverse order
         */
        @Override
        public ShortTuple3 reverse() {
            return new ShortTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified short value.
         *
         * @param valueToFind the short value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code consumer} is {@code null}
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
            N.checkArgNotNull(consumer);

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
         * @param action the tri-consumer to perform on the three elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         * @see #map(Throwables.ShortTriFunction)
         * @see #filter(Throwables.ShortTriPredicate)
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
         * @param mapper the tri-function to apply to the three elements, must not be {@code null}
         * @return the result of applying the mapper function (may be {@code null} if the mapper returns {@code null})
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception during execution
         * @see #accept(Throwables.ShortTriConsumer)
         * @see #filter(Throwables.ShortTriPredicate)
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
         * Optional<ShortTuple.ShortTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
         * // Returns: Optional containing the triple (since 2 < 3 < 5)
         *
         * Optional<ShortTuple.ShortTuple3> empty = triple.filter((a, b, c) -> a + b + c > 20);
         * // Returns: Optional.empty() (since 2 + 3 + 5 = 10 is not > 20)
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the tri-predicate to test the three elements, must not be {@code null}
         * @return an {@code Optional} containing this tuple if the predicate returns {@code true}, otherwise an empty {@code Optional}
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception during evaluation
         * @see #accept(Throwables.ShortTriConsumer)
         * @see #map(Throwables.ShortTriFunction)
         */
        public <E extends Exception> Optional<ShortTuple3> filter(final Throwables.ShortTriPredicate<E> predicate) throws E {
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
         * @return {@code true} if the specified object is a ShortTuple.ShortTuple3 with the same elements in the same order, {@code false} otherwise
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
         * @return a string representation in the format "(element1, element2, element3)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
        }

        /**
         * Returns the internal array of short elements.
         * The array is lazily initialized on first access.
         *
         * @return a short array containing all elements in order
         */
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
         * Returns the minimum short value in this tuple.
         *
         * @return the smallest of the four short values
         */
        @Override
        public short min() {
            return N.min(_1, _2, _3, _4);
        }

        /**
         * Returns the maximum short value in this tuple.
         *
         * @return the largest of the four short values
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the median short value in this tuple.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) short value when sorted
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of all short values in this tuple as an int.
         *
         * @return the sum of all four short values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4);
        }

        /**
         * Returns the average of all short values in this tuple as a double.
         *
         * @return the average of all four short values as a {@code double}
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4);
        }

        /**
         * Returns a new ShortTuple.ShortTuple4 with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple4 with the elements in reverse order
         */
        @Override
        public ShortTuple4 reverse() {
            return new ShortTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified short value.
         *
         * @param valueToFind the short value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code consumer} is {@code null}
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
            N.checkArgNotNull(consumer);

            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return a hash code value calculated from all four elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * _1 + _2) + _3)) + _4;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ShortTuple.ShortTuple4 with the same elements in the same order, {@code false} otherwise
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
         *
         * @return a string representation in the format "(element1, element2, element3, element4)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
        }

        /**
         * Returns the internal array of short elements.
         * The array is lazily initialized on first access.
         *
         * @return a short array containing all elements in order
         */
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
         * Returns the minimum short value in this tuple.
         *
         * @return the smallest of the five short values
         */
        @Override
        public short min() {
            return N.min(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the maximum short value in this tuple.
         *
         * @return the largest of the five short values
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the median short value in this tuple.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the middle short value when sorted
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of all short values in this tuple as an int.
         *
         * @return the sum of all five short values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the average of all short values in this tuple as a double.
         *
         * @return the average of all five short values as a {@code double}
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5);
        }

        /**
         * Returns a new ShortTuple.ShortTuple5 with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple5 with the elements in reverse order
         */
        @Override
        public ShortTuple5 reverse() {
            return new ShortTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified short value.
         *
         * @param valueToFind the short value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code consumer} is {@code null}
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
            N.checkArgNotNull(consumer);

            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return a hash code value calculated from all five elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4)) + _5;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ShortTuple.ShortTuple5 with the same elements in the same order, {@code false} otherwise
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
         *
         * @return a string representation in the format "(element1, element2, element3, element4, element5)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ")";
        }

        /**
         * Returns the internal array of short elements.
         * The array is lazily initialized on first access.
         *
         * @return a short array containing all elements in order
         */
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
         * Returns the minimum short value in this tuple.
         *
         * @return the smallest of the six short values
         */
        @Override
        public short min() {
            return N.min(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the maximum short value in this tuple.
         *
         * @return the largest of the six short values
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median short value in this tuple.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) short value when sorted
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of all short values in this tuple as an int.
         *
         * @return the sum of all six short values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the average of all short values in this tuple as a double.
         *
         * @return the average of all six short values as a {@code double}
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns a new ShortTuple.ShortTuple6 with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple6 with the elements in reverse order
         */
        @Override
        public ShortTuple6 reverse() {
            return new ShortTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified short value.
         *
         * @param valueToFind the short value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code consumer} is {@code null}
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
            N.checkArgNotNull(consumer);

            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return a hash code value calculated from all six elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5)) + _6;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ShortTuple.ShortTuple6 with the same elements in the same order, {@code false} otherwise
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
         *
         * @return a string representation in the format "(element1, element2, element3, element4, element5, element6)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ")";
        }

        /**
         * Returns the internal array of short elements.
         * The array is lazily initialized on first access.
         *
         * @return a short array containing all elements in order
         */
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
         * Returns the minimum short value in this tuple.
         *
         * @return the smallest of the seven short values
         */
        @Override
        public short min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the maximum short value in this tuple.
         *
         * @return the largest of the seven short values
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the median short value in this tuple.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the middle short value when sorted
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of all short values in this tuple as an int.
         *
         * @return the sum of all seven short values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the average of all short values in this tuple as a double.
         *
         * @return the average of all seven short values as a {@code double}
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns a new ShortTuple.ShortTuple7 with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple7 with the elements in reverse order
         */
        @Override
        public ShortTuple7 reverse() {
            return new ShortTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified short value.
         *
         * @param valueToFind the short value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code consumer} is {@code null}
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
            N.checkArgNotNull(consumer);

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
         *
         * @return a hash code value calculated from all seven elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6)) + _7;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ShortTuple.ShortTuple7 with the same elements in the same order, {@code false} otherwise
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
         *
         * @return a string representation in the format "(element1, element2, element3, element4, element5, element6, element7)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ")";
        }

        /**
         * Returns the internal array of short elements.
         * The array is lazily initialized on first access.
         *
         * @return a short array containing all elements in order
         */
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
         * Returns the minimum short value in this tuple.
         *
         * @return the smallest of the eight short values
         */
        @Override
        public short min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the maximum short value in this tuple.
         *
         * @return the largest of the eight short values
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the median short value in this tuple.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * @return the median (lower middle) short value when sorted
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of all short values in this tuple as an int.
         *
         * @return the sum of all eight short values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the average of all short values in this tuple as a double.
         *
         * @return the average of all eight short values as a {@code double}
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns a new ShortTuple.ShortTuple8 with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple8 with the elements in reverse order
         */
        @Override
        public ShortTuple8 reverse() {
            return new ShortTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified short value.
         *
         * @param valueToFind the short value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code consumer} is {@code null}
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
            N.checkArgNotNull(consumer);

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
         *
         * @return a hash code value calculated from all eight elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6) + _7)) + _8;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ShortTuple.ShortTuple8 with the same elements in the same order, {@code false} otherwise
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
         *
         * @return a string representation in the format "(element1, element2, ..., element8)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")";
        }

        /**
         * Returns the internal array of short elements.
         * The array is lazily initialized on first access.
         *
         * @return a short array containing all elements in order
         */
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
         * Returns the minimum short value in this tuple.
         *
         * @return the smallest of the nine short values
         */
        @Override
        public short min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the maximum short value in this tuple.
         *
         * @return the largest of the nine short values
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the median short value in this tuple.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * @return the middle short value when sorted
         */
        @Override
        public short median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of all short values in this tuple as an int.
         *
         * @return the sum of all nine short values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the average of all short values in this tuple as a double.
         *
         * @return the average of all nine short values as a {@code double}
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns a new ShortTuple.ShortTuple9 with the elements in reverse order.
         *
         * @return a new ShortTuple.ShortTuple9 with the elements in reverse order
         */
        @Override
        public ShortTuple9 reverse() {
            return new ShortTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified short value.
         *
         * @param valueToFind the short value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code consumer} is {@code null}
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
            N.checkArgNotNull(consumer);

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
         *
         * @return a hash code value calculated from all nine elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6) + _7) + _8)) + _9;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ShortTuple.ShortTuple9 with the same elements in the same order, {@code false} otherwise
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
         *
         * @return a string representation in the format "(element1, element2, ..., element9)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ")";
        }

        /**
         * Returns the internal array of short elements.
         * The array is lazily initialized on first access.
         *
         * @return a short array containing all elements in order
         */
        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
