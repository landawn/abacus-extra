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
import com.landawn.abacus.util.stream.IntStream;

/**
 * Base class for immutable tuples of primitive {@code int} values.
 *
 * <p>The nested tuple types model fixed arities from 0 through 9. Factory methods such as
 * {@link #copyOf(int[])} and the {@code of(...)} overloads select the matching subtype, while the base
 * class supplies aggregate, reversal, containment, and functional helper operations.</p>
 *
 * @param <TP> the concrete {@code IntTuple} subtype that fluent operations such as {@link #reverse()} return
 * @see PrimitiveTuple
 * @see ShortTuple
 * @see LongTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class IntTuple<TP extends IntTuple<TP>> extends PrimitiveTuple<TP> {

    /** Lazily initialized cached array view of all tuple elements. */
    protected volatile int[] elements;

    /**
     * Protected constructor for subclass instantiation.
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link #of(int)}, {@link #of(int, int)}, etc., to create tuple instances.
     */
    protected IntTuple() {
    }

    /**
     * Creates an IntTuple.IntTuple1 containing a single int value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple1 single = IntTuple.of(42);
     * int value = single._1;  // returns 42
     *
     * IntTuple.IntTuple1 neg = IntTuple.of(-7);
     * int nv = neg._1;  // returns -7
     *
     * IntTuple.IntTuple1 zero = IntTuple.of(0);
     * int zv = zero._1;  // returns 0
     *
     * IntTuple.IntTuple1 big = IntTuple.of(Integer.MAX_VALUE);
     * int bv = big._1;  // returns 2147483647
     * }</pre>
     *
     * @param _1 the int value to store in the tuple
     * @return a new IntTuple.IntTuple1 containing the specified value
     */
    public static IntTuple1 of(final int _1) {
        return new IntTuple1(_1);
    }

    /**
     * Creates an IntTuple.IntTuple2 containing two int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple2 pair = IntTuple.of(1, 2);
     * int sum = pair._1 + pair._2;  // returns 3
     *
     * IntTuple.IntTuple2 neg = IntTuple.of(-10, 10);
     * int nv1 = neg._1;  // returns -10
     * int nv2 = neg._2;  // returns 10
     *
     * IntTuple.IntTuple2 zeros = IntTuple.of(0, 0);
     * int zsum = zeros.sum();  // returns 0
     *
     * IntTuple.IntTuple2 bounds = IntTuple.of(Integer.MIN_VALUE, Integer.MAX_VALUE);
     * int bmin = bounds.min();  // returns -2147483648
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @return a new IntTuple.IntTuple2 containing the specified values
     */
    public static IntTuple2 of(final int _1, final int _2) {
        return new IntTuple2(_1, _2);
    }

    /**
     * Creates an IntTuple.IntTuple3 containing three int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 triple = IntTuple.of(1, 2, 3);
     * double average = triple.average();  // returns 2.0
     *
     * IntTuple.IntTuple3 rev = IntTuple.of(1, 2, 3).reverse();
     * int last = rev._1;  // returns 3
     *
     * IntTuple.IntTuple3 neg = IntTuple.of(-5, 0, 5);
     * int mn = neg.min();  // returns -5
     *
     * IntTuple.IntTuple3 neg2 = IntTuple.of(-5, 0, 5);
     * int mx = neg2.max();  // returns 5
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @return a new IntTuple.IntTuple3 containing the specified values
     */
    public static IntTuple3 of(final int _1, final int _2, final int _3) {
        return new IntTuple3(_1, _2, _3);
    }

    /**
     * Creates an IntTuple.IntTuple4 containing four int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple4 quad = IntTuple.of(1, 2, 3, 4);
     * int first = quad._1;   // returns 1
     * int fourth = quad._4;  // returns 4
     *
     * IntTuple.IntTuple4 avg4 = IntTuple.of(1, 2, 3, 4);
     * double avg = avg4.average();  // returns 2.5
     *
     * IntTuple.IntTuple4 neg = IntTuple.of(-4, -3, -2, -1);
     * int mn = neg.min();  // returns -4
     *
     * IntTuple.IntTuple4 even = IntTuple.of(4, 1, 3, 2);
     * int med = even.median();  // returns 2 (lower-middle of sorted: 1, 2, 3, 4)
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @return a new IntTuple.IntTuple4 containing the specified values
     */
    public static IntTuple4 of(final int _1, final int _2, final int _3, final int _4) {
        return new IntTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates an IntTuple.IntTuple5 containing five int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple5 quint = IntTuple.of(1, 2, 3, 4, 5);
     * int fifth = quint._5;  // returns 5
     *
     * IntTuple.IntTuple5 rev = IntTuple.of(1, 2, 3, 4, 5).reverse();
     * int first = rev._1;  // returns 5
     *
     * IntTuple.IntTuple5 neg = IntTuple.of(-2, -1, 0, 1, 2);
     * int mn = neg.min();  // returns -2
     *
     * IntTuple.IntTuple5 neg2 = IntTuple.of(-2, -1, 0, 1, 2);
     * int sum = neg2.sum();  // returns 0
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @param _5 the fifth int value
     * @return a new IntTuple.IntTuple5 containing the specified values
     */
    public static IntTuple5 of(final int _1, final int _2, final int _3, final int _4, final int _5) {
        return new IntTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates an IntTuple.IntTuple6 containing six int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple6 sext = IntTuple.of(1, 2, 3, 4, 5, 6);
     * int sixth = sext._6;  // returns 6
     *
     * IntTuple.IntTuple6 sext2 = IntTuple.of(1, 2, 3, 4, 5, 6);
     * int sum = sext2.sum();  // returns 21
     *
     * IntTuple.IntTuple6 neg = IntTuple.of(-3, -2, -1, 0, 1, 2);
     * int mn = neg.min();  // returns -3
     *
     * IntTuple.IntTuple6 neg2 = IntTuple.of(-3, -2, -1, 0, 1, 2);
     * boolean has = neg2.contains(0);  // returns true
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @param _5 the fifth int value
     * @param _6 the sixth int value
     * @return a new IntTuple.IntTuple6 containing the specified values
     */
    public static IntTuple6 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6) {
        return new IntTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates an IntTuple.IntTuple7 containing seven int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple7 sept = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
     * int seventh = sept._7;  // returns 7
     *
     * IntTuple.IntTuple7 sept2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
     * int sum = sept2.sum();  // returns 28
     *
     * IntTuple.IntTuple7 neg = IntTuple.of(-3, -2, -1, 0, 1, 2, 3);
     * int med = neg.median();  // returns 0 (middle of 7 sorted elements)
     *
     * IntTuple.IntTuple7 neg2 = IntTuple.of(-3, -2, -1, 0, 1, 2, 3);
     * int mx = neg2.max();  // returns 3
     * }</pre>
     *
     * @param _1 the first int value
     * @param _2 the second int value
     * @param _3 the third int value
     * @param _4 the fourth int value
     * @param _5 the fifth int value
     * @param _6 the sixth int value
     * @param _7 the seventh int value
     * @return a new IntTuple.IntTuple7 containing the specified values
     */
    public static IntTuple7 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7) {
        return new IntTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates an IntTuple.IntTuple8 containing eight int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple8 oct = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
     * int eighth = oct._8;  // returns 8
     *
     * IntTuple.IntTuple8 oct2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
     * int sum = oct2.sum();  // returns 36
     *
     * IntTuple.IntTuple8 neg = IntTuple.of(-4, -3, -2, -1, 0, 1, 2, 3);
     * int mn = neg.min();  // returns -4
     *
     * IntTuple.IntTuple8 neg2 = IntTuple.of(-4, -3, -2, -1, 0, 1, 2, 3);
     * boolean has = neg2.contains(0);  // returns true
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
     * @return a new IntTuple.IntTuple8 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more int values
     */
    @Deprecated
    public static IntTuple8 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8) {
        return new IntTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates an IntTuple.IntTuple9 containing nine int values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple9 non = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
     * int ninth = non._9;  // returns 9
     *
     * IntTuple.IntTuple9 non2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
     * int sum = non2.sum();  // returns 45
     *
     * IntTuple.IntTuple9 neg = IntTuple.of(-4, -3, -2, -1, 0, 1, 2, 3, 4);
     * int med = neg.median();  // returns 0 (middle of 9 sorted elements)
     *
     * IntTuple.IntTuple9 neg2 = IntTuple.of(-4, -3, -2, -1, 0, 1, 2, 3, 4);
     * boolean has = neg2.contains(-4);  // returns true
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
     * @return a new IntTuple.IntTuple9 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more int values
     */
    @Deprecated
    public static IntTuple9 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8, final int _9) {
        return new IntTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates an IntTuple from an array of int values.
     * <p>
     * The size of the returned tuple depends on the length of the input array.
     * This factory method supports arrays with 0 to 9 elements. For {@code null} or empty
     * arrays, returns the shared empty tuple. For arrays with 1-9 elements, returns the
     * corresponding {@code IntTuple1}..{@code IntTuple9} instance. The values are copied
     * into the new tuple; subsequent modifications to the input array do not affect it.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * int[] values = {1, 2, 3};
     * IntTuple.IntTuple3 tuple = IntTuple.copyOf(values);
     * int first = tuple._1;  // returns 1
     *
     * IntTuple.IntTuple1 single = IntTuple.copyOf(new int[]{42});
     * int sv = single._1;  // returns 42
     *
     * // null or empty array returns the shared empty tuple (arity 0)
     * IntTuple<?> empty = IntTuple.copyOf(new int[0]);
     * int arity = empty.arity();  // returns 0
     *
     * IntTuple<?> fromNull = IntTuple.copyOf(null);
     * int nullArity = fromNull.arity();  // returns 0
     *
     * // array length > 9 throws IllegalArgumentException
     * IntTuple.copyOf(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});  // throws IllegalArgumentException
     * }</pre>
     *
     * <p><strong>Type note:</strong> the runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type. Assigning to the wrong arity-specific subtype will result in a
     * {@link ClassCastException} at the assignment site.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of int values; may be {@code null} or empty, in which case the shared empty tuple is returned
     * @return an {@code IntTuple} of the appropriate arity containing the array values, or the shared empty tuple if the array is {@code null} or empty
     * @throws IllegalArgumentException if {@code values} has more than 9 elements
     * @see #of(int)
     */
    @SuppressWarnings("deprecation")
    public static <TP extends IntTuple<TP>> TP copyOf(final int[] values) {
        if (values == null || values.length == 0) {
            return (TP) IntTuple0.EMPTY;
        }

        switch (values.length) {
            case 1:
                return (TP) IntTuple.of(values[0]);

            case 2:
                return (TP) IntTuple.of(values[0], values[1]);

            case 3:
                return (TP) IntTuple.of(values[0], values[1], values[2]);

            case 4:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3]);

            case 5:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3], values[4]);

            case 6:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);

            case 7:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);

            case 8:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);

            case 9:
                return (TP) IntTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);

            default:
                throw new IllegalArgumentException("Too many elements (" + values.length + "). Maximum: 9");
        }
    }

    /**
     * Returns the minimum int value in this tuple.
     * <p>
     * Iterates through all elements in the tuple and returns the smallest value.
     * For single-element tuples, the element itself is returned.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
     * int min = tuple.min();  // returns 1
     *
     * IntTuple.IntTuple1 single = IntTuple.of(42);
     * int singleMin = single.min();  // returns 42
     *
     * IntTuple.IntTuple3 neg = IntTuple.of(-5, 0, 5);
     * int negMin = neg.min();  // returns -5
     *
     * // empty tuple throws NoSuchElementException
     * IntTuple.copyOf(new int[0]).min();  // throws NoSuchElementException
     * }</pre>
     *
     * @return the minimum int value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #max()
     * @see #median()
     */
    public int min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum int value in this tuple.
     * <p>
     * Iterates through all elements in the tuple and returns the largest value.
     * For single-element tuples, the element itself is returned.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(3, 1, 2);
     * int max = tuple.max();  // returns 3
     *
     * IntTuple.IntTuple1 single = IntTuple.of(42);
     * int singleMax = single.max();  // returns 42
     *
     * IntTuple.IntTuple3 neg = IntTuple.of(-5, 0, 5);
     * int negMax = neg.max();  // returns 5
     *
     * // empty tuple throws NoSuchElementException
     * IntTuple.copyOf(new int[0]).max();  // throws NoSuchElementException
     * }</pre>
     *
     * @return the maximum int value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see #median()
     */
    public int max() {
        return N.max(elements());
    }

    /**
     * Returns the median int value in this tuple.
     * <p>
     * The median is calculated by sorting the elements internally. For tuples with an odd
     * number of elements, returns the middle value when sorted. For tuples with an even
     * number of elements, returns the lower middle value when sorted.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 3, 2);
     * int median = tuple.median();  // returns 2 (middle value when sorted: 1, 2, 3)
     *
     * IntTuple.IntTuple4 evenTuple = IntTuple.of(4, 1, 3, 2);
     * int evenMedian = evenTuple.median();  // returns 2 (lower-middle of sorted: 1, 2, 3, 4)
     *
     * IntTuple.IntTuple1 single = IntTuple.of(7);
     * int singleMedian = single.median();  // returns 7
     *
     * // empty tuple throws NoSuchElementException
     * IntTuple.copyOf(new int[0]).median();  // throws NoSuchElementException
     * }</pre>
     *
     * @return the median int value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see #max()
     * @see N#median(int...)
     */
    public int median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all int values in this tuple as an {@code int}.
     * <p>
     * The summation is performed in {@code long} precision and then narrowed back to {@code int};
     * if the true total does not fit in an {@code int}, an {@link ArithmeticException} is thrown
     * rather than the result silently wrapping around.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * int sum = tuple.sum();  // returns 6
     *
     * IntTuple.IntTuple2 pair = IntTuple.of(100, 200);
     * int total = pair.sum();  // returns 300
     *
     * // empty tuple sum is 0
     * int emptySum = IntTuple.copyOf(new int[0]).sum();  // returns 0
     *
     * // overflow throws ArithmeticException
     * IntTuple.of(Integer.MAX_VALUE, 1).sum();  // throws ArithmeticException
     * }</pre>
     *
     * @return the sum of all int values in this tuple as an {@code int}
     * @throws ArithmeticException if the total does not fit in an {@code int}
     * @see #average()
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the arithmetic mean of all int values in this tuple as a {@code double}.
     * <p>
     * The result is returned as a {@code double} to preserve fractional precision.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple4 tuple = IntTuple.of(1, 2, 3, 4);
     * double avg = tuple.average();  // returns 2.5
     *
     * IntTuple.IntTuple3 triple = IntTuple.of(1, 2, 3);
     * double avg3 = triple.average();  // returns 2.0
     *
     * IntTuple.IntTuple1 single = IntTuple.of(42);
     * double avgSingle = single.average();  // returns 42.0
     *
     * // empty tuple throws NoSuchElementException
     * IntTuple.copyOf(new int[0]).average();  // throws NoSuchElementException
     * }</pre>
     *
     * @return the arithmetic mean of all int values in this tuple as a {@code double}
     * @throws NoSuchElementException if the tuple is empty
     * @see #sum()
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
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * IntTuple.IntTuple3 reversed = tuple.reverse();
     * // reversed equals IntTuple.of(3, 2, 1)
     *
     * IntTuple.IntTuple2 pair = IntTuple.of(1, 2);
     * IntTuple.IntTuple2 reversedPair = pair.reverse();
     * // reversedPair equals IntTuple.of(2, 1)
     *
     * // single-element tuple reverses to an equal tuple
     * IntTuple.IntTuple1 single = IntTuple.of(42);
     * IntTuple.IntTuple1 revSingle = single.reverse();
     * // revSingle equals IntTuple.of(42)
     *
     * // empty tuple reverses to the same empty tuple instance
     * IntTuple<?> empty = IntTuple.copyOf(new int[0]);
     * IntTuple<?> revEmpty = empty.reverse();
     * // revEmpty == empty (same object)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified int value.
     * <p>
     * This method performs a linear search through all elements in the tuple to determine
     * if any element matches the specified value. Returns {@code true} if at least one
     * element equals the search value, {@code false} otherwise.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * boolean has2 = tuple.contains(2);  // returns true
     * boolean has5 = tuple.contains(5);  // returns false
     *
     * IntTuple.IntTuple3 neg = IntTuple.of(-5, 0, 5);
     * boolean hasZero = neg.contains(0);  // returns true
     *
     * IntTuple.IntTuple2 pair = IntTuple.of(10, 10);
     * boolean hasOne = pair.contains(1);  // returns false
     *
     * // empty tuple never contains any value
     * boolean emptyHas = IntTuple.copyOf(new int[0]).contains(0);  // returns false
     * }</pre>
     *
     * @param valueToFind the int value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(int valueToFind);

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
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * int[] array = tuple.toArray();  // returns [1, 2, 3]
     * array[0] = 99;                  // mutation does not affect the tuple
     *
     * IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
     * int[] pairArray = pair.toArray();  // returns [10, 20]
     *
     * // single-element tuple
     * int[] single = IntTuple.of(42).toArray();  // returns [42]
     *
     * // empty tuple returns a zero-length array
     * int[] emptyArr = IntTuple.copyOf(new int[0]).toArray();  // returns []
     * }</pre>
     *
     * @return a new {@code int[]} array containing all tuple elements in order
     * @see #toList()
     * @see #stream()
     */
    public int[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new IntList containing all elements of this tuple.
     * <p>
     * Converts this tuple to a mutable {@link IntList} containing all elements
     * in their original order. The returned list is a new instance and modifications
     * to it do not affect the original tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * IntList list = tuple.toList();
     * int first = list.get(0);  // returns 1
     *
     * IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
     * IntList pairList = pair.toList();
     * int size = pairList.size();  // returns 2
     *
     * // list is independent from the tuple; mutations don't affect the tuple
     * list.add(4);  // tuple is still IntTuple.of(1, 2, 3)
     *
     * // empty tuple produces an empty list
     * IntList emptyList = IntTuple.copyOf(new int[0]).toList();
     * int emptySize = emptyList.size();  // returns 0
     * }</pre>
     *
     * @return a new {@code IntList} containing all tuple elements in order
     * @see #toArray()
     * @see #stream()
     */
    public IntList toList() {
        return IntList.of(elements().clone());
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
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * // accumulate sum externally
     * java.util.concurrent.atomic.AtomicInteger total = new java.util.concurrent.atomic.AtomicInteger();
     * tuple.forEach(total::addAndGet);
     * // total.get() returns 6
     *
     * // collect elements into a list
     * java.util.List<Integer> collected = new java.util.ArrayList<>();
     * IntTuple.of(10, 20).forEach(v -> collected.add(v));
     * // collected contains [10, 20]
     *
     * // empty tuple: action is never called
     * IntTuple.copyOf(new int[0]).forEach(v -> { throw new RuntimeException("unreachable"); });
     * // no exception thrown
     *
     * // null action throws IllegalArgumentException immediately
     * IntTuple.of(1).forEach(null);  // throws IllegalArgumentException
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the action to be performed for each element, must not be {@code null}
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws E {
        N.checkArgNotNull(action);

        for (final int element : elements()) {
            action.accept(element);
        }
    }

    /**
     * Returns an IntStream of all elements in this tuple.
     * <p>
     * Converts this tuple to a sequential {@link IntStream} containing all elements
     * in their original order. This allows using standard stream operations like filter,
     * map, and reduce on the tuple elements.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 tuple = IntTuple.of(1, 2, 3);
     * int sum = tuple.stream().sum();  // returns 6
     *
     * IntTuple.IntTuple2 pair = IntTuple.of(10, 20);
     * int max = pair.stream().max().getAsInt();  // returns 20
     *
     * // filter and collect
     * long posCount = IntTuple.of(-1, 0, 1, 2).stream().filter(v -> v > 0).count();
     * // returns 2
     *
     * // empty tuple produces an empty stream
     * long emptyCount = IntTuple.copyOf(new int[0]).stream().count();  // returns 0
     * }</pre>
     *
     * @return a sequential {@code IntStream} containing all tuple elements in order
     * @see #toArray()
     * @see #toList()
     */
    public IntStream stream() {
        return IntStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * <p>
     * The hash code is computed based on the contents of the tuple using a standard
     * algorithm that ensures equal tuples have equal hash codes. This implementation
     * is consistent with {@link #equals(Object)}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // equal tuples have the same hash code
     * int h1 = IntTuple.of(1, 2, 3).hashCode();
     * int h2 = IntTuple.of(1, 2, 3).hashCode();
     * // h1 == h2  (returns true)
     *
     * // tuples with different elements have different hash codes (in practice)
     * int ha = IntTuple.of(1, 2, 3).hashCode();
     * int hb = IntTuple.of(3, 2, 1).hashCode();
     * // ha != hb  (returns true for these specific values)
     *
     * // suitable for use as Map keys
     * java.util.Map<IntTuple<?>, String> map = new java.util.HashMap<>();
     * map.put(IntTuple.of(1, 2), "pair");
     * // map.get(IntTuple.of(1, 2)) returns "pair"
     *
     * // empty tuple has a stable hash code
     * int emptyHash = IntTuple.copyOf(new int[0]).hashCode();
     * // emptyHash == IntTuple.copyOf(new int[0]).hashCode()  (same each call)
     * }</pre>
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
     *       (so an {@code IntTuple2} never equals an {@code IntTuple3}), and contains
     *       the same int values in the same order.</li>
     * </ul>
     *
     * <p>This method is consistent with {@link #hashCode()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // same elements in same order are equal
     * boolean eq1 = IntTuple.of(1, 2, 3).equals(IntTuple.of(1, 2, 3));  // returns true
     *
     * // different order is not equal
     * boolean eq2 = IntTuple.of(1, 2, 3).equals(IntTuple.of(3, 2, 1));  // returns false
     *
     * // different arity is never equal, even with common prefix elements
     * boolean eq3 = IntTuple.of(1, 2).equals(IntTuple.of(1, 2, 3));  // returns false
     *
     * // null is never equal
     * boolean eq4 = IntTuple.of(1, 2).equals(null);  // returns false
     * }</pre>
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
            return N.equals(elements(), ((IntTuple<TP>) obj).elements());
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
     * <pre>{@code
     * String s3 = IntTuple.of(1, 2, 3).toString();  // returns "(1, 2, 3)"
     *
     * String s2 = IntTuple.of(1, 2).toString();  // returns "(1, 2)"
     *
     * String s1 = IntTuple.of(1).toString();  // returns "(1)"
     *
     * // empty tuple
     * String s0 = IntTuple.copyOf(new int[0]).toString();  // returns "()"
     * }</pre>
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
     * calls. The returned array is therefore a live internal cache, not a defensive copy.
     * </p>
     *
     * @return the array of int elements stored in this tuple
     */
    protected abstract int[] elements();

    /**
     * An empty tuple containing no elements.
     * This class is used to represent a tuple with zero elements
     * and is returned by {@link #copyOf(int[])} when passed a {@code null} or empty array.
     */
    static final class IntTuple0 extends IntTuple<IntTuple0> {

        private static final IntTuple0 EMPTY = new IntTuple0();

        IntTuple0() {
        }

        /**
         * Returns the number of elements in this tuple, which is always 0.
         *
         * @return {@code 0}
         */
        @Override
        public int arity() {
            return 0;
        }

        /**
         * Returns the minimum int value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public int min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the maximum int value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public int max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the median int value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public int median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the sum of all values in this tuple as an int.
         * For an empty tuple, the sum is {@code 0}.
         *
         * @return {@code 0}
         */
        @Override
        public int sum() {
            return 0;
        }

        /**
         * Returns the average of all int values in this tuple.
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
        public IntTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified value.
         * An empty tuple contains no values.
         *
         * @param valueToFind the int value to search for
         * @return {@code false} always, as the tuple is empty
         */
        @Override
        public boolean contains(final int valueToFind) {
            return false;
        }

        /**
         * Returns a string representation of this empty tuple.
         *
         * @return {@code "()"}
         */
        @Override
        public String toString() {
            return "()";
        }

        /**
         * Returns the shared empty int array.
         *
         * @return an empty int array
         */
        @Override
        protected int[] elements() {
            return N.EMPTY_INT_ARRAY;
        }
    }

    /**
     * A tuple containing exactly one int value.
     * The value is accessible through the public final field {@code _1}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple1 single = IntTuple.of(42);
     * int value = single._1;  // 42
     * }</pre>
     */
    public static final class IntTuple1 extends IntTuple<IntTuple1> {

        /** The single int value stored in this tuple. */
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple1 t = IntTuple.of(42);
         * int a = t.arity();  // returns 1
         *
         * IntTuple.IntTuple1 neg = IntTuple.of(-1);
         * int a2 = neg.arity();  // returns 1
         *
         * // contrasts with other arities
         * int a3 = IntTuple.of(1, 2).arity();  // returns 2
         *
         * int a0 = IntTuple.copyOf(new int[0]).arity();  // returns 0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * int m = IntTuple.of(42).min();  // returns 42
         *
         * int mn = IntTuple.of(-5).min();  // returns -5
         *
         * int mz = IntTuple.of(0).min();  // returns 0
         *
         * int mmax = IntTuple.of(Integer.MAX_VALUE).min();  // returns 2147483647
         * }</pre>
         *
         * @return the single element value
         */
        @Override
        public int min() {
            return _1;
        }

        /**
         * Returns the maximum value in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * int m = IntTuple.of(42).max();  // returns 42
         *
         * int mn = IntTuple.of(-5).max();  // returns -5
         *
         * int mz = IntTuple.of(0).max();  // returns 0
         *
         * int mmax = IntTuple.of(Integer.MIN_VALUE).max();  // returns -2147483648
         * }</pre>
         *
         * @return the single element value
         */
        @Override
        public int max() {
            return _1;
        }

        /**
         * Returns the median value in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * int m = IntTuple.of(42).median();  // returns 42
         *
         * int mn = IntTuple.of(-5).median();  // returns -5
         *
         * int mz = IntTuple.of(0).median();  // returns 0
         *
         * int mmax = IntTuple.of(Integer.MAX_VALUE).median();  // returns 2147483647
         * }</pre>
         *
         * @return the single element value
         */
        @Override
        public int median() {
            return _1;
        }

        /**
         * Returns the sum of all values in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(7).sum()                    // returns 7
         * IntTuple.of(0).sum()                    // returns 0
         * IntTuple.of(-3).sum()                   // returns -3
         * IntTuple.of(Integer.MAX_VALUE).sum()    // returns Integer.MAX_VALUE
         * }</pre>
         *
         * @return the single element value
         */
        @Override
        public int sum() {
            return _1;
        }

        /**
         * Returns the average of all values in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(7).average()                    // returns 7.0
         * IntTuple.of(0).average()                    // returns 0.0
         * IntTuple.of(-3).average()                   // returns -3.0
         * IntTuple.of(Integer.MAX_VALUE).average()    // returns 2147483647.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple1 r = IntTuple.of(5).reverse();
         * r._1                                           // returns 5 (same value, new instance)
         * IntTuple.of(-99).reverse()._1                  // returns -99
         * IntTuple.of(0).reverse()._1                    // returns 0
         * IntTuple.of(Integer.MIN_VALUE).reverse()._1    // returns Integer.MIN_VALUE
         * }</pre>
         *
         * @return a new IntTuple.IntTuple1 with the same value
         */
        @Override
        public IntTuple1 reverse() {
            return new IntTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(5).contains(5)                                    // returns true
         * IntTuple.of(5).contains(6)                                    // returns false
         * IntTuple.of(0).contains(0)                                    // returns true
         * IntTuple.of(Integer.MAX_VALUE).contains(Integer.MIN_VALUE)    // returns false
         * }</pre>
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if the value equals _1, {@code false} otherwise
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind;
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(7).hashCode()                    // returns 7
         * IntTuple.of(0).hashCode()                    // returns 0
         * IntTuple.of(-3).hashCode()                   // returns -3
         * IntTuple.of(Integer.MAX_VALUE).hashCode()    // returns Integer.MAX_VALUE
         * }</pre>
         *
         * @return {@code _1} (the {@code int} value itself, which is its own hash code)
         */
        @Override
        public int hashCode() {
            return _1;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(5).equals(IntTuple.of(5))     // returns true
         * IntTuple.of(5).equals(IntTuple.of(6))     // returns false
         * IntTuple.IntTuple1 t = IntTuple.of(5);
         * t.equals(t)                               // returns true (same instance)
         * IntTuple.of(5).equals(null)               // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is an IntTuple.IntTuple1 with the same element, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(7).toString()                    // returns "(7)"
         * IntTuple.of(-3).toString()                   // returns "(-3)"
         * IntTuple.of(0).toString()                    // returns "(0)"
         * IntTuple.of(Integer.MAX_VALUE).toString()    // returns "(2147483647)"
         * }</pre>
         *
         * @return a string in the format "(value)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly two int values.
     * The values are accessible through the public final fields {@code _1} and {@code _2}.
     *
     * <p>In addition to the operations inherited from {@link IntTuple}, this class provides
     * functional helpers for working with pairs:</p>
     * <ul>
     *   <li>{@link #accept(Throwables.IntBiConsumer)} - consume both values</li>
     *   <li>{@link #map(Throwables.IntBiFunction)} - transform the pair to a single value</li>
     *   <li>{@link #filter(Throwables.IntBiPredicate)} - conditionally wrap in {@link Optional}</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple2 pair = IntTuple.of(3, 5);
     * int product = pair.map((a, b) -> a * b);   // 15
     * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(3, 5).arity()                                    // returns 2
         * IntTuple.of(-1, 0).arity()                                   // returns 2
         * IntTuple.of(0, 0).arity()                                    // returns 2
         * IntTuple.of(Integer.MAX_VALUE, Integer.MIN_VALUE).arity()    // returns 2
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(3, 5).min()                                   // returns 3
         * IntTuple.of(5, 3).min()                                   // returns 3
         * IntTuple.of(-5, -1).min()                                 // returns -5
         * IntTuple.of(Integer.MIN_VALUE, Integer.MAX_VALUE).min()   // returns Integer.MIN_VALUE
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(3, 5).max()                                   // returns 5
         * IntTuple.of(5, 3).max()                                   // returns 5
         * IntTuple.of(-5, -1).max()                                 // returns -1
         * IntTuple.of(Integer.MIN_VALUE, Integer.MAX_VALUE).max()   // returns Integer.MAX_VALUE
         * }</pre>
         *
         * @return the larger of _1 and _2
         */
        @Override
        public int max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median of the two elements.
         * For tuples with an even number of elements, returns the lower of the two middle
         * elements; with only two values this is equivalent to {@link #min()}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(3, 5).median()     // returns 3 (lower middle of sorted [3, 5])
         * IntTuple.of(5, 3).median()     // returns 3 (lower middle of sorted [3, 5])
         * IntTuple.of(4, 4).median()     // returns 4 (duplicates)
         * IntTuple.of(-5, -1).median()   // returns -5
         * }</pre>
         *
         * @return the smaller of {@code _1} and {@code _2}
         */
        @Override
        public int median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of the two elements as an int.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(3, 5).sum()                   // returns 8
         * IntTuple.of(-3, 3).sum()                  // returns 0
         * IntTuple.of(-3, -5).sum()                 // returns -8
         * IntTuple.of(Integer.MAX_VALUE, 1).sum()   // throws ArithmeticException
         * }</pre>
         *
         * @return _1 + _2 as an int
         * @throws ArithmeticException if the total does not fit in an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2);
        }

        /**
         * Returns the average of the two elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(3, 5).average()     // returns 4.0
         * IntTuple.of(-3, 3).average()    // returns 0.0
         * IntTuple.of(2, 3).average()     // returns 2.5
         * IntTuple.of(-5, -1).average()   // returns -3.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple2 r = IntTuple.of(3, 5).reverse();
         * r._1                                // returns 5
         * r._2                                // returns 3
         * IntTuple.of(-1, -2).reverse()._1    // returns -2
         * IntTuple.of(0, 0).reverse()._1      // returns 0 (symmetric)
         * }</pre>
         *
         * @return a new IntTuple.IntTuple2 with values (_2, _1)
         */
        @Override
        public IntTuple2 reverse() {
            return new IntTuple2(_2, _1);
        }

        /**
         * Checks if either element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(3, 5).contains(3)     // returns true
         * IntTuple.of(3, 5).contains(5)     // returns true
         * IntTuple.of(3, 5).contains(4)     // returns false
         * IntTuple.of(3, 5).contains(-3)    // returns false
         * }</pre>
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals _1 or _2
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * List<Integer> list = new ArrayList<>();
         * IntTuple.of(10, 20).forEach(list::add);    // list becomes [10, 20]
         *
         * int[] sum = {0};
         * IntTuple.of(-5, 5).forEach(v -> sum[0] += v);    // sum[0] == 0
         *
         * // null action - throws exception
         * IntTuple.of(3, 5).forEach(null);    // throws IllegalArgumentException
         *
         * // boundary values
         * int[] store = new int[2]; int[] idx = {0};
         * IntTuple.of(Integer.MIN_VALUE, Integer.MAX_VALUE).forEach(v -> store[idx[0]++] = v);
         * // store[0] == Integer.MIN_VALUE, store[1] == Integer.MAX_VALUE
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the action to perform on each element
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
        }

        /**
         * Performs the given bi-consumer action on the two elements of this tuple.
         * <p>
         * This is a convenience method that passes both elements (_1 and _2) to the
         * provided bi-consumer action. It's useful for operations that need to process
         * both values together.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * int[] result = {0};
         * IntTuple.of(3, 4).accept((a, b) -> result[0] = a + b);    // result[0] == 7
         *
         * IntTuple.IntTuple2 coordinates = IntTuple.of(10, 20);
         * coordinates.accept((x, y) -> System.out.println("Point at (" + x + ", " + y + ")"));
         *
         * // negative values
         * int[] diff = {0};
         * IntTuple.of(-3, -5).accept((a, b) -> diff[0] = a - b);    // diff[0] == 2
         *
         * // boundary values
         * int[] store = {0};
         * IntTuple.of(Integer.MAX_VALUE, Integer.MIN_VALUE).accept((a, b) -> store[0] = a);    // store[0] == Integer.MAX_VALUE
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the bi-consumer to perform on the two elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #map(Throwables.IntBiFunction)
         * @see #filter(Throwables.IntBiPredicate)
         */
        public <E extends Exception> void accept(final Throwables.IntBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given bi-function to the two elements and returns the result.
         * <p>
         * This method transforms the pair of int values into a single value of type U
         * using the provided mapper function. The mapper receives both _1 and _2 as arguments
         * and can return any type, including primitive wrapper types, objects, or null.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * int product = IntTuple.of(3, 4).map((a, b) -> a * b);    // returns 12
         *
         * String desc = IntTuple.of(5, 10).map((w, h) -> w + "x" + h);    // returns "5x10"
         *
         * // negative values
         * int diff = IntTuple.of(-3, -5).map((a, b) -> a - b);    // returns 2
         *
         * // mapper returning null is allowed
         * Object nullResult = IntTuple.of(0, 0).map((a, b) -> null);    // returns null
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the bi-function to apply to the two elements, must not be {@code null}
         * @return the result of applying the mapper function, may be {@code null}
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.IntBiConsumer)
         * @see #filter(Throwables.IntBiPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.IntBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         * <p>
         * This method tests the two elements using the provided bi-predicate. If the predicate
         * returns {@code true}, an Optional containing this tuple is returned. Otherwise, an
         * empty Optional is returned. This is useful for conditional processing in functional chains.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // predicate passes - Optional is present
         * IntTuple.of(3, 4).filter((a, b) -> a + b > 5).isPresent()    // returns true
         *
         * // predicate fails - Optional is empty
         * IntTuple.of(1, 2).filter((a, b) -> a + b > 10).isPresent()   // returns false
         *
         * // both elements equal
         * IntTuple.of(5, 5).filter((a, b) -> a == b).isPresent()       // returns true
         *
         * // negative values, predicate fails
         * IntTuple.of(-3, -5).filter((a, b) -> a > 0).isPresent()      // returns false
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the bi-predicate to test the two elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception
         * @see #accept(Throwables.IntBiConsumer)
         * @see #map(Throwables.IntBiFunction)
         */
        public <E extends Exception> Optional<IntTuple2> filter(final Throwables.IntBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(3, 5).hashCode()     // returns 31 * 3 + 5 == 98
         * IntTuple.of(0, 0).hashCode()     // returns 0
         * IntTuple.of(-1, 0).hashCode()    // returns 31 * (-1) + 0 == -31
         * IntTuple.of(0, 1).hashCode()     // returns 1 (order matters)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(3, 5).equals(IntTuple.of(3, 5))    // returns true
         * IntTuple.of(3, 5).equals(IntTuple.of(5, 3))    // returns false (order matters)
         * IntTuple.IntTuple2 t = IntTuple.of(3, 5);
         * t.equals(t)                                     // returns true (same instance)
         * IntTuple.of(3, 5).equals(null)                  // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is an IntTuple.IntTuple2 with the same elements in the same order, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(3, 5).toString()                                    // returns "(3, 5)"
         * IntTuple.of(-1, -2).toString()                                  // returns "(-1, -2)"
         * IntTuple.of(0, 0).toString()                                    // returns "(0, 0)"
         * IntTuple.of(Integer.MAX_VALUE, Integer.MIN_VALUE).toString()    // returns "(2147483647, -2147483648)"
         * }</pre>
         *
         * @return a string in the format "(_1, _2)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly three int values.
     * The values are accessible through the public final fields {@code _1}, {@code _2}, and {@code _3}.
     *
     * <p>In addition to the operations inherited from {@link IntTuple}, this class provides
     * functional helpers for working with triples:</p>
     * <ul>
     *   <li>{@link #accept(Throwables.IntTriConsumer)} - consume all three values</li>
     *   <li>{@link #map(Throwables.IntTriFunction)} - transform the triple to a single value</li>
     *   <li>{@link #filter(Throwables.IntTriPredicate)} - conditionally wrap in {@link Optional}</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple3 triple = IntTuple.of(2, 3, 5);
     * int sum = triple.map((a, b, c) -> a + b + c);   // 10
     * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(1, 2, 3).arity()                                    // returns 3
         * IntTuple.of(-1, 0, 1).arity()                                   // returns 3
         * IntTuple.of(0, 0, 0).arity()                                    // returns 3
         * IntTuple.of(Integer.MAX_VALUE, 0, Integer.MIN_VALUE).arity()    // returns 3
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(1, 2, 3).min()                                     // returns 1
         * IntTuple.of(3, 1, 2).min()                                     // returns 1
         * IntTuple.of(-5, 0, 5).min()                                    // returns -5
         * IntTuple.of(Integer.MIN_VALUE, 0, Integer.MAX_VALUE).min()     // returns Integer.MIN_VALUE
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(1, 2, 3).max()                                     // returns 3
         * IntTuple.of(3, 1, 2).max()                                     // returns 3
         * IntTuple.of(-5, 0, 5).max()                                    // returns 5
         * IntTuple.of(Integer.MIN_VALUE, 0, Integer.MAX_VALUE).max()     // returns Integer.MAX_VALUE
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(1, 2, 3).median()     // returns 2 (middle of sorted [1, 2, 3])
         * IntTuple.of(3, 1, 2).median()     // returns 2 (middle of sorted [1, 2, 3])
         * IntTuple.of(-5, 0, 5).median()    // returns 0
         * IntTuple.of(3, 3, 3).median()     // returns 3 (all duplicates)
         * }</pre>
         *
         * @return the middle int value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of all three elements as an int.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(1, 2, 3).sum()                                    // returns 6
         * IntTuple.of(-1, 0, 1).sum()                                   // returns 0
         * IntTuple.of(-1, -2, -3).sum()                                 // returns -6
         * IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE, 1).sum()    // throws ArithmeticException
         * }</pre>
         *
         * @return _1 + _2 + _3 as an int
         * @throws ArithmeticException if the total does not fit in an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3);
        }

        /**
         * Returns the average of all three elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(1, 2, 3).average()      // returns 2.0
         * IntTuple.of(-1, 0, 1).average()     // returns 0.0
         * IntTuple.of(1, 2, 4).average()      // returns 2.3333333333333335
         * IntTuple.of(-3, -3, -3).average()   // returns -3.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple3 r = IntTuple.of(1, 2, 3).reverse();
         * r._1                                    // returns 3
         * r._2                                    // returns 2
         * r._3                                    // returns 1
         * IntTuple.of(-1, -2, -3).reverse()._1    // returns -3
         * }</pre>
         *
         * @return a new IntTuple.IntTuple3 with values (_3, _2, _1)
         */
        @Override
        public IntTuple3 reverse() {
            return new IntTuple3(_3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(1, 2, 3).contains(1)     // returns true
         * IntTuple.of(1, 2, 3).contains(3)     // returns true
         * IntTuple.of(1, 2, 3).contains(4)     // returns false
         * IntTuple.of(1, 2, 3).contains(-1)    // returns false
         * }</pre>
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals _1, _2, or _3
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * List<Integer> list = new ArrayList<>();
         * IntTuple.of(1, 2, 3).forEach(list::add);    // list becomes [1, 2, 3]
         *
         * int[] sum = {0};
         * IntTuple.of(-1, 0, 1).forEach(v -> sum[0] += v);    // sum[0] == 0
         *
         * // null action - throws exception
         * IntTuple.of(1, 2, 3).forEach(null);    // throws IllegalArgumentException
         *
         * // boundary values - all three elements visited in order
         * int[] store = new int[3]; int[] idx = {0};
         * IntTuple.of(Integer.MIN_VALUE, 0, Integer.MAX_VALUE).forEach(v -> store[idx[0]++] = v);
         * // store[0] == Integer.MIN_VALUE, store[1] == 0, store[2] == Integer.MAX_VALUE
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the action to perform on each element
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
        }

        /**
         * Performs the given tri-consumer action on the three elements of this tuple.
         * <p>
         * This is a convenience method that passes all three elements (_1, _2, and _3) to the
         * provided tri-consumer action. It's useful for operations that need to process
         * all three values together.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * int[] result = {0};
         * IntTuple.of(1, 2, 3).accept((a, b, c) -> result[0] = a + b + c);    // result[0] == 6
         *
         * IntTuple.IntTuple3 rgb = IntTuple.of(255, 128, 64);
         * rgb.accept((r, g, b) -> System.out.println("RGB(" + r + ", " + g + ", " + b + ")"));
         *
         * // negative values
         * int[] product = {0};
         * IntTuple.of(-1, -2, -3).accept((a, b, c) -> product[0] = a * b * c);    // product[0] == -6
         *
         * // boundary values
         * int[] store = {0};
         * IntTuple.of(Integer.MIN_VALUE, 0, Integer.MAX_VALUE).accept((a, b, c) -> store[0] = c);    // store[0] == Integer.MAX_VALUE
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the tri-consumer to perform on the three elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #map(Throwables.IntTriFunction)
         * @see #filter(Throwables.IntTriPredicate)
         */
        public <E extends Exception> void accept(final Throwables.IntTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given tri-function to the three elements and returns the result.
         * <p>
         * This method transforms the three int values into a single value of type U
         * using the provided mapper function. The mapper receives all three elements
         * (_1, _2, and _3) as arguments and can return any type, including primitive
         * wrapper types, objects, or null.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * int product = IntTuple.of(1, 2, 3).map((a, b, c) -> a * b * c);    // returns 6
         *
         * String formatted = IntTuple.of(10, 20, 30).map((x, y, z) -> x + "x" + y + "x" + z);    // returns "10x20x30"
         *
         * // negative values
         * int sum = IntTuple.of(-1, -2, -3).map((a, b, c) -> a + b + c);    // returns -6
         *
         * // mapper returning null is allowed
         * Object nullResult = IntTuple.of(0, 0, 0).map((a, b, c) -> null);    // returns null
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the tri-function to apply to the three elements, must not be {@code null}
         * @return the result of applying the mapper function, may be {@code null}
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.IntTriConsumer)
         * @see #filter(Throwables.IntTriPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.IntTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if the predicate is satisfied,
         * or an empty Optional otherwise.
         * <p>
         * This method tests the three elements using the provided tri-predicate. If the predicate
         * returns {@code true}, an Optional containing this tuple is returned. Otherwise, an
         * empty Optional is returned. This is useful for conditional processing in functional chains.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // ascending order - predicate passes
         * IntTuple.of(1, 2, 3).filter((a, b, c) -> a < b && b < c).isPresent()    // returns true
         *
         * // descending order - predicate fails
         * IntTuple.of(3, 2, 1).filter((a, b, c) -> a < b && b < c).isPresent()    // returns false
         *
         * // all equal
         * IntTuple.of(5, 5, 5).filter((a, b, c) -> a == b && b == c).isPresent()   // returns true
         *
         * // negative values, sum predicate fails
         * IntTuple.of(-3, -2, -1).filter((a, b, c) -> a + b + c > 0).isPresent()   // returns false
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the tri-predicate to test the three elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception
         * @see #accept(Throwables.IntTriConsumer)
         * @see #map(Throwables.IntTriFunction)
         */
        public <E extends Exception> Optional<IntTuple3> filter(final Throwables.IntTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(1, 2, 3).hashCode()     // returns (31 * (31 * 1 + 2)) + 3 == 30720
         * IntTuple.of(0, 0, 0).hashCode()     // returns 0
         * IntTuple.of(-1, 0, 0).hashCode()    // returns 31 * (31 * (-1) + 0) + 0 == -961
         * IntTuple.of(0, 0, 1).hashCode()     // returns 1 (order matters)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(1, 2, 3).equals(IntTuple.of(1, 2, 3))    // returns true
         * IntTuple.of(1, 2, 3).equals(IntTuple.of(3, 2, 1))    // returns false (order matters)
         * IntTuple.IntTuple3 t = IntTuple.of(1, 2, 3);
         * t.equals(t)                                           // returns true (same instance)
         * IntTuple.of(1, 2, 3).equals(null)                     // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is an IntTuple.IntTuple3 with the same elements in the same order, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.of(1, 2, 3).toString()                                    // returns "(1, 2, 3)"
         * IntTuple.of(-1, -2, -3).toString()                                 // returns "(-1, -2, -3)"
         * IntTuple.of(0, 0, 0).toString()                                    // returns "(0, 0, 0)"
         * IntTuple.of(Integer.MAX_VALUE, 0, Integer.MIN_VALUE).toString()    // returns "(2147483647, 0, -2147483648)"
         * }</pre>
         *
         * @return a string in the format "(_1, _2, _3)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly four int values.
     * The values are accessible through the public final fields {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple4 quad = IntTuple.of(1, 2, 3, 4);
     * double avg = quad.average();   // 2.5
     * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 4);
         * t.arity(); // returns 4
         *
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(-1, 0, 1, 2);
         * t2.arity(); // returns 4
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t = (IntTuple.IntTuple4) IntTuple.of(3, 1, 4, 2);
         * t.min(); // returns 1
         *
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(-5, -1, 0, 10);
         * t2.min(); // returns -5
         *
         * IntTuple.IntTuple4 t3 = (IntTuple.IntTuple4) IntTuple.of(Integer.MIN_VALUE, 0, 1, 2);
         * t3.min(); // returns Integer.MIN_VALUE
         *
         * IntTuple.IntTuple4 t4 = (IntTuple.IntTuple4) IntTuple.of(5, 5, 5, 5);
         * t4.min(); // returns 5
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t = (IntTuple.IntTuple4) IntTuple.of(3, 1, 4, 2);
         * t.max(); // returns 4
         *
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(-5, -1, 0, 10);
         * t2.max(); // returns 10
         *
         * IntTuple.IntTuple4 t3 = (IntTuple.IntTuple4) IntTuple.of(0, 1, 2, Integer.MAX_VALUE);
         * t3.max(); // returns Integer.MAX_VALUE
         *
         * IntTuple.IntTuple4 t4 = (IntTuple.IntTuple4) IntTuple.of(5, 5, 5, 5);
         * t4.max(); // returns 5
         * }</pre>
         *
         * @return the largest of _1, _2, _3, and _4
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the median value of the four elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t = (IntTuple.IntTuple4) IntTuple.of(3, 1, 4, 2);
         * t.median(); // returns 2  (sorted: [1,2,3,4], lower-middle = index 1 = 2)
         *
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(-1, 5, -3, 0);
         * t2.median(); // returns -1  (sorted: [-3,-1,0,5], lower-middle = index 1 = -1)
         *
         * IntTuple.IntTuple4 t3 = (IntTuple.IntTuple4) IntTuple.of(7, 7, 7, 7);
         * t3.median(); // returns 7  (all equal)
         *
         * IntTuple.IntTuple4 t4 = (IntTuple.IntTuple4) IntTuple.of(Integer.MAX_VALUE, 0, 1, Integer.MIN_VALUE);
         * t4.median(); // returns 0  (sorted: [MIN,0,1,MAX], lower-middle = 0)
         * }</pre>
         *
         * @return the median (lower middle) int value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of all four elements as an int.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 4);
         * t.sum(); // returns 10
         *
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(-1, -2, 3, 4);
         * t2.sum(); // returns 4
         *
         * IntTuple.IntTuple4 t3 = (IntTuple.IntTuple4) IntTuple.of(0, 0, 0, 0);
         * t3.sum(); // returns 0
         *
         * IntTuple.IntTuple4 t4 = (IntTuple.IntTuple4) IntTuple.of(Integer.MAX_VALUE, 1, 0, 0);
         * t4.sum(); // throws ArithmeticException (overflow)
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 as an int
         * @throws ArithmeticException if the total does not fit in an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4);
        }

        /**
         * Returns the average of all four elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 4);
         * t.average(); // returns 2.5
         *
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(-2, -1, 1, 2);
         * t2.average(); // returns 0.0
         *
         * IntTuple.IntTuple4 t3 = (IntTuple.IntTuple4) IntTuple.of(5, 5, 5, 5);
         * t3.average(); // returns 5.0
         *
         * IntTuple.IntTuple4 t4 = (IntTuple.IntTuple4) IntTuple.of(0, 0, 0, 4);
         * t4.average(); // returns 1.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 4);
         * t.reverse().toString(); // returns "(4, 3, 2, 1)"
         *
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(-1, 0, 5, -3);
         * t2.reverse().toString(); // returns "(-3, 5, 0, -1)"
         *
         * IntTuple.IntTuple4 t3 = (IntTuple.IntTuple4) IntTuple.of(9, 9, 9, 9);
         * t3.reverse().toString(); // returns "(9, 9, 9, 9)"  (palindrome)
         *
         * IntTuple.IntTuple4 t4 = (IntTuple.IntTuple4) IntTuple.of(Integer.MIN_VALUE, 0, 0, Integer.MAX_VALUE);
         * t4.reverse()._1; // returns Integer.MAX_VALUE
         * }</pre>
         *
         * @return a new IntTuple.IntTuple4 with values (_4, _3, _2, _1)
         */
        @Override
        public IntTuple4 reverse() {
            return new IntTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 4);
         * t.contains(1); // returns true
         * t.contains(4); // returns true
         *
         * t.contains(0); // returns false
         * t.contains(5); // returns false
         *
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(-5, 0, 3, 10);
         * t2.contains(-5); // returns true
         * t2.contains(-1); // returns false
         * }</pre>
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the four elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 4);
         * List<Integer> result = new ArrayList<>();
         * t.forEach(result::add); // result becomes [1, 2, 3, 4]
         *
         * int[] sum = {0};
         * t.forEach(v -> sum[0] += v); // sum[0] becomes 10
         *
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(-1, 0, 5, -3);
         * List<Integer> result2 = new ArrayList<>();
         * t2.forEach(result2::add); // result2 becomes [-1, 0, 5, -3]
         *
         * t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the action to perform on each element
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
            action.accept(_4);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all four elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t1 = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 4);
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 4);
         * t1.hashCode() == t2.hashCode(); // returns true (equal tuples have equal hash codes)
         *
         * IntTuple.IntTuple4 t3 = (IntTuple.IntTuple4) IntTuple.of(4, 3, 2, 1);
         * t1.hashCode() == t3.hashCode(); // returns false (different element order)
         *
         * IntTuple.IntTuple4 t4 = (IntTuple.IntTuple4) IntTuple.of(0, 0, 0, 0);
         * t4.hashCode(); // returns 0
         * }</pre>
         *
         * @return a hash code based on all four elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * _1 + _2) + _3)) + _4;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple4 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t1 = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 4);
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 4);
         * t1.equals(t2); // returns true
         *
         * t1.equals(t1); // returns true (same reference)
         *
         * IntTuple.IntTuple4 t3 = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 5);
         * t1.equals(t3); // returns false (last element differs)
         *
         * t1.equals(null);     // returns false
         * t1.equals("string"); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple4 with equal elements, {@code false} otherwise
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
         * The format is (_1, _2, _3, _4) where each element is displayed in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple4 t = (IntTuple.IntTuple4) IntTuple.of(1, 2, 3, 4);
         * t.toString(); // returns "(1, 2, 3, 4)"
         *
         * IntTuple.IntTuple4 t2 = (IntTuple.IntTuple4) IntTuple.of(-1, 0, -3, 4);
         * t2.toString(); // returns "(-1, 0, -3, 4)"
         *
         * IntTuple.IntTuple4 t3 = (IntTuple.IntTuple4) IntTuple.of(5, 5, 5, 5);
         * t3.toString(); // returns "(5, 5, 5, 5)"
         *
         * IntTuple.IntTuple4 t4 = (IntTuple.IntTuple4) IntTuple.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, -1);
         * t4.toString(); // returns "(-2147483648, 2147483647, 0, -1)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly five int values.
     * The values are accessible through the public final fields {@code _1} through {@code _5}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple5 tuple = IntTuple.of(1, 2, 3, 4, 5);
     * int median = tuple.median();   // 3
     * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 5);
         * t.arity(); // returns 5
         *
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(0, 0, 0, 0, 0);
         * t2.arity(); // returns 5
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t = (IntTuple.IntTuple5) IntTuple.of(3, 1, 4, 1, 5);
         * t.min(); // returns 1
         *
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(-10, -5, 0, 5, 10);
         * t2.min(); // returns -10
         *
         * IntTuple.IntTuple5 t3 = (IntTuple.IntTuple5) IntTuple.of(Integer.MIN_VALUE, 0, 1, 2, 3);
         * t3.min(); // returns Integer.MIN_VALUE
         *
         * IntTuple.IntTuple5 t4 = (IntTuple.IntTuple5) IntTuple.of(3, 3, 3, 3, 3);
         * t4.min(); // returns 3
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t = (IntTuple.IntTuple5) IntTuple.of(3, 1, 4, 1, 5);
         * t.max(); // returns 5
         *
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(-10, -5, 0, 5, 10);
         * t2.max(); // returns 10
         *
         * IntTuple.IntTuple5 t3 = (IntTuple.IntTuple5) IntTuple.of(0, 1, 2, 3, Integer.MAX_VALUE);
         * t3.max(); // returns Integer.MAX_VALUE
         *
         * IntTuple.IntTuple5 t4 = (IntTuple.IntTuple5) IntTuple.of(3, 3, 3, 3, 3);
         * t4.max(); // returns 3
         * }</pre>
         *
         * @return the largest of _1, _2, _3, _4, and _5
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the median value of the five elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t = (IntTuple.IntTuple5) IntTuple.of(3, 1, 4, 2, 5);
         * t.median(); // returns 3  (sorted: [1,2,3,4,5], middle = index 2 = 3)
         *
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(-1, 3, -5, 0, 10);
         * t2.median(); // returns 0  (sorted: [-5,-1,0,3,10], middle = 0)
         *
         * IntTuple.IntTuple5 t3 = (IntTuple.IntTuple5) IntTuple.of(7, 7, 7, 7, 7);
         * t3.median(); // returns 7  (all equal)
         *
         * IntTuple.IntTuple5 t4 = (IntTuple.IntTuple5) IntTuple.of(1, 3, 2, 1, 3);
         * t4.median(); // returns 2  (sorted: [1,1,2,3,3])
         * }</pre>
         *
         * @return the middle int value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of all five elements as an int.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 5);
         * t.sum(); // returns 15
         *
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(-1, -2, -3, 4, 5);
         * t2.sum(); // returns 3
         *
         * IntTuple.IntTuple5 t3 = (IntTuple.IntTuple5) IntTuple.of(0, 0, 0, 0, 0);
         * t3.sum(); // returns 0
         *
         * IntTuple.IntTuple5 t4 = (IntTuple.IntTuple5) IntTuple.of(Integer.MAX_VALUE, 1, 0, 0, 0);
         * t4.sum(); // throws ArithmeticException (overflow)
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 + _5 as an int
         * @throws ArithmeticException if the total does not fit in an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the average of all five elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 5);
         * t.average(); // returns 3.0
         *
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(-2, -1, 0, 1, 2);
         * t2.average(); // returns 0.0
         *
         * IntTuple.IntTuple5 t3 = (IntTuple.IntTuple5) IntTuple.of(1, 1, 1, 1, 2);
         * t3.average(); // returns 1.2
         *
         * IntTuple.IntTuple5 t4 = (IntTuple.IntTuple5) IntTuple.of(4, 4, 4, 4, 4);
         * t4.average(); // returns 4.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 5);
         * t.reverse().toString(); // returns "(5, 4, 3, 2, 1)"
         *
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(-1, 0, 5, -3, 2);
         * t2.reverse().toString(); // returns "(2, -3, 5, 0, -1)"
         *
         * IntTuple.IntTuple5 t3 = (IntTuple.IntTuple5) IntTuple.of(9, 9, 9, 9, 9);
         * t3.reverse().toString(); // returns "(9, 9, 9, 9, 9)"  (palindrome)
         *
         * IntTuple.IntTuple5 t4 = (IntTuple.IntTuple5) IntTuple.of(Integer.MIN_VALUE, 0, 0, 0, Integer.MAX_VALUE);
         * t4.reverse()._1; // returns Integer.MAX_VALUE
         * }</pre>
         *
         * @return a new IntTuple.IntTuple5 with values (_5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple5 reverse() {
            return new IntTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 5);
         * t.contains(1); // returns true
         * t.contains(5); // returns true
         *
         * t.contains(0); // returns false
         * t.contains(6); // returns false
         *
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(-5, 0, 3, 7, 10);
         * t2.contains(-5); // returns true
         * t2.contains(-1); // returns false
         * }</pre>
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the five elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 5);
         * List<Integer> result = new ArrayList<>();
         * t.forEach(result::add); // result becomes [1, 2, 3, 4, 5]
         *
         * int[] sum = {0};
         * t.forEach(v -> sum[0] += v); // sum[0] becomes 15
         *
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(-1, 0, 5, -3, 2);
         * List<Integer> result2 = new ArrayList<>();
         * t2.forEach(result2::add); // result2 becomes [-1, 0, 5, -3, 2]
         *
         * t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the action to perform on each element
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
            action.accept(_4);
            action.accept(_5);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all five elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t1 = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 5);
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 5);
         * t1.hashCode() == t2.hashCode(); // returns true (equal tuples have equal hash codes)
         *
         * IntTuple.IntTuple5 t3 = (IntTuple.IntTuple5) IntTuple.of(5, 4, 3, 2, 1);
         * t1.hashCode() == t3.hashCode(); // returns false (different element order)
         *
         * IntTuple.IntTuple5 t4 = (IntTuple.IntTuple5) IntTuple.of(0, 0, 0, 0, 0);
         * t4.hashCode(); // returns 0
         * }</pre>
         *
         * @return a hash code based on all five elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4)) + _5;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple5 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t1 = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 5);
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 5);
         * t1.equals(t2); // returns true
         *
         * t1.equals(t1); // returns true (same reference)
         *
         * IntTuple.IntTuple5 t3 = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 6);
         * t1.equals(t3); // returns false (last element differs)
         *
         * t1.equals(null);     // returns false
         * t1.equals("string"); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple5 with equal elements, {@code false} otherwise
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
         * The format is (_1, _2, _3, _4, _5) where each element is displayed in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple5 t = (IntTuple.IntTuple5) IntTuple.of(1, 2, 3, 4, 5);
         * t.toString(); // returns "(1, 2, 3, 4, 5)"
         *
         * IntTuple.IntTuple5 t2 = (IntTuple.IntTuple5) IntTuple.of(-1, 0, -3, 4, 5);
         * t2.toString(); // returns "(-1, 0, -3, 4, 5)"
         *
         * IntTuple.IntTuple5 t3 = (IntTuple.IntTuple5) IntTuple.of(5, 5, 5, 5, 5);
         * t3.toString(); // returns "(5, 5, 5, 5, 5)"
         *
         * IntTuple.IntTuple5 t4 = (IntTuple.IntTuple5) IntTuple.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, -1, 1);
         * t4.toString(); // returns "(-2147483648, 2147483647, 0, -1, 1)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly six int values.
     * The values are accessible through the public final fields {@code _1} through {@code _6}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple6 tuple = IntTuple.of(1, 2, 3, 4, 5, 6);
     * int sum = tuple.sum();   // 21
     * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 6);
         * t.arity(); // returns 6
         *
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(0, 0, 0, 0, 0, 0);
         * t2.arity(); // returns 6
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t = (IntTuple.IntTuple6) IntTuple.of(3, 1, 4, 1, 5, 9);
         * t.min(); // returns 1
         *
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(-10, -5, 0, 5, 10, 15);
         * t2.min(); // returns -10
         *
         * IntTuple.IntTuple6 t3 = (IntTuple.IntTuple6) IntTuple.of(Integer.MIN_VALUE, 0, 1, 2, 3, 4);
         * t3.min(); // returns Integer.MIN_VALUE
         *
         * IntTuple.IntTuple6 t4 = (IntTuple.IntTuple6) IntTuple.of(3, 3, 3, 3, 3, 3);
         * t4.min(); // returns 3
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t = (IntTuple.IntTuple6) IntTuple.of(3, 1, 4, 1, 5, 9);
         * t.max(); // returns 9
         *
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(-10, -5, 0, 5, 10, 15);
         * t2.max(); // returns 15
         *
         * IntTuple.IntTuple6 t3 = (IntTuple.IntTuple6) IntTuple.of(0, 1, 2, 3, 4, Integer.MAX_VALUE);
         * t3.max(); // returns Integer.MAX_VALUE
         *
         * IntTuple.IntTuple6 t4 = (IntTuple.IntTuple6) IntTuple.of(3, 3, 3, 3, 3, 3);
         * t4.max(); // returns 3
         * }</pre>
         *
         * @return the largest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median value of the six elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t = (IntTuple.IntTuple6) IntTuple.of(3, 1, 4, 2, 5, 6);
         * t.median(); // returns 3  (sorted: [1,2,3,4,5,6], lower-middle = index 2 = 3)
         *
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(-1, 3, -5, 0, 7, 10);
         * t2.median(); // returns 0  (sorted: [-5,-1,0,3,7,10], lower-middle = index 2 = 0)
         *
         * IntTuple.IntTuple6 t3 = (IntTuple.IntTuple6) IntTuple.of(7, 7, 7, 7, 7, 7);
         * t3.median(); // returns 7  (all equal)
         *
         * IntTuple.IntTuple6 t4 = (IntTuple.IntTuple6) IntTuple.of(Integer.MAX_VALUE, 0, 1, 2, 3, Integer.MIN_VALUE);
         * t4.median(); // returns 1  (sorted: [MIN,0,1,2,3,MAX], lower-middle = index 2 = 1)
         * }</pre>
         *
         * @return the median (lower middle) int value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of all six elements as an int.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 6);
         * t.sum(); // returns 21
         *
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(-1, -2, -3, 4, 5, 6);
         * t2.sum(); // returns 9
         *
         * IntTuple.IntTuple6 t3 = (IntTuple.IntTuple6) IntTuple.of(0, 0, 0, 0, 0, 0);
         * t3.sum(); // returns 0
         *
         * IntTuple.IntTuple6 t4 = (IntTuple.IntTuple6) IntTuple.of(Integer.MAX_VALUE, 1, 0, 0, 0, 0);
         * t4.sum(); // throws ArithmeticException (overflow)
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 as an int
         * @throws ArithmeticException if the total does not fit in an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the average of all six elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 6);
         * t.average(); // returns 3.5
         *
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(-3, -1, 0, 1, 2, 4);
         * t2.average(); // returns 0.5
         *
         * IntTuple.IntTuple6 t3 = (IntTuple.IntTuple6) IntTuple.of(4, 4, 4, 4, 4, 4);
         * t3.average(); // returns 4.0
         *
         * IntTuple.IntTuple6 t4 = (IntTuple.IntTuple6) IntTuple.of(0, 0, 0, 0, 0, 0);
         * t4.average(); // returns 0.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 6);
         * t.reverse().toString(); // returns "(6, 5, 4, 3, 2, 1)"
         *
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(-1, 0, 5, -3, 2, 7);
         * t2.reverse().toString(); // returns "(7, 2, -3, 5, 0, -1)"
         *
         * IntTuple.IntTuple6 t3 = (IntTuple.IntTuple6) IntTuple.of(9, 9, 9, 9, 9, 9);
         * t3.reverse().toString(); // returns "(9, 9, 9, 9, 9, 9)"  (palindrome)
         *
         * IntTuple.IntTuple6 t4 = (IntTuple.IntTuple6) IntTuple.of(Integer.MIN_VALUE, 0, 0, 0, 0, Integer.MAX_VALUE);
         * t4.reverse()._1; // returns Integer.MAX_VALUE
         * }</pre>
         *
         * @return a new IntTuple.IntTuple6 with values (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple6 reverse() {
            return new IntTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 6);
         * t.contains(1); // returns true
         * t.contains(6); // returns true
         *
         * t.contains(0); // returns false
         * t.contains(7); // returns false
         *
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(-5, 0, 3, 7, 10, 15);
         * t2.contains(-5); // returns true
         * t2.contains(-1); // returns false
         * }</pre>
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the six elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 6);
         * List<Integer> result = new ArrayList<>();
         * t.forEach(result::add); // result becomes [1, 2, 3, 4, 5, 6]
         *
         * int[] sum = {0};
         * t.forEach(v -> sum[0] += v); // sum[0] becomes 21
         *
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(-1, 0, 5, -3, 2, 7);
         * List<Integer> result2 = new ArrayList<>();
         * t2.forEach(result2::add); // result2 becomes [-1, 0, 5, -3, 2, 7]
         *
         * t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the action to perform on each element
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
            action.accept(_4);
            action.accept(_5);
            action.accept(_6);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all six elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t1 = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 6);
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 6);
         * t1.hashCode() == t2.hashCode(); // returns true (equal tuples have equal hash codes)
         *
         * IntTuple.IntTuple6 t3 = (IntTuple.IntTuple6) IntTuple.of(6, 5, 4, 3, 2, 1);
         * t1.hashCode() == t3.hashCode(); // returns false (different element order)
         *
         * IntTuple.IntTuple6 t4 = (IntTuple.IntTuple6) IntTuple.of(0, 0, 0, 0, 0, 0);
         * t4.hashCode(); // returns 0
         * }</pre>
         *
         * @return a hash code based on all six elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5)) + _6;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple6 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t1 = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 6);
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 6);
         * t1.equals(t2); // returns true
         *
         * t1.equals(t1); // returns true (same reference)
         *
         * IntTuple.IntTuple6 t3 = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 7);
         * t1.equals(t3); // returns false (last element differs)
         *
         * t1.equals(null);     // returns false
         * t1.equals("string"); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple6 with equal elements, {@code false} otherwise
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
         * The format is (_1, _2, _3, _4, _5, _6) where each element is displayed in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple6 t = (IntTuple.IntTuple6) IntTuple.of(1, 2, 3, 4, 5, 6);
         * t.toString(); // returns "(1, 2, 3, 4, 5, 6)"
         *
         * IntTuple.IntTuple6 t2 = (IntTuple.IntTuple6) IntTuple.of(-1, 0, -3, 4, 5, 6);
         * t2.toString(); // returns "(-1, 0, -3, 4, 5, 6)"
         *
         * IntTuple.IntTuple6 t3 = (IntTuple.IntTuple6) IntTuple.of(5, 5, 5, 5, 5, 5);
         * t3.toString(); // returns "(5, 5, 5, 5, 5, 5)"
         *
         * IntTuple.IntTuple6 t4 = (IntTuple.IntTuple6) IntTuple.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, -1, 1, 2);
         * t4.toString(); // returns "(-2147483648, 2147483647, 0, -1, 1, 2)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly seven int values.
     * The values are accessible through the public final fields {@code _1} through {@code _7}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple7 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
     * IntTuple.IntTuple7 reversed = tuple.reverse();   // (7, 6, 5, 4, 3, 2, 1)
     * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t = (IntTuple.IntTuple7) IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * t.arity(); // returns 7
         *
         * IntTuple.IntTuple7 t2 = (IntTuple.IntTuple7) IntTuple.of(0, 0, 0, 0, 0, 0, 0);
         * t2.arity(); // returns 7
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t = IntTuple.of(3, 1, 4, 1, 5, 9, 2);
         * int min = t.min();   // 1
         *
         * IntTuple.IntTuple7 neg = IntTuple.of(-10, -5, 0, 5, 10, 3, 7);
         * int negMin = neg.min();   // -10
         *
         * IntTuple.IntTuple7 same = IntTuple.of(7, 7, 7, 7, 7, 7, 7);
         * int sameMin = same.min();   // 7
         *
         * IntTuple.IntTuple7 boundary = IntTuple.of(Integer.MIN_VALUE, 0, 1, 2, 3, 4, 5);
         * int boundMin = boundary.min();   // Integer.MIN_VALUE
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t = IntTuple.of(3, 1, 4, 1, 5, 9, 2);
         * int max = t.max();   // 9
         *
         * IntTuple.IntTuple7 neg = IntTuple.of(-10, -5, -3, -2, -1, -7, -9);
         * int negMax = neg.max();   // -1
         *
         * IntTuple.IntTuple7 same = IntTuple.of(7, 7, 7, 7, 7, 7, 7);
         * int sameMax = same.max();   // 7
         *
         * IntTuple.IntTuple7 boundary = IntTuple.of(0, 1, 2, 3, 4, 5, Integer.MAX_VALUE);
         * int boundMax = boundary.max();   // Integer.MAX_VALUE
         * }</pre>
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the median value of the seven elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * int med = t.median();   // 4
         *
         * IntTuple.IntTuple7 unsorted = IntTuple.of(7, 3, 5, 1, 6, 2, 4);
         * int unsortedMed = unsorted.median();   // 4
         *
         * IntTuple.IntTuple7 neg = IntTuple.of(-3, -1, 0, 2, 4, 6, 8);
         * int negMed = neg.median();   // 2
         *
         * IntTuple.IntTuple7 dup = IntTuple.of(5, 5, 5, 5, 5, 5, 5);
         * int dupMed = dup.median();   // 5
         * }</pre>
         *
         * @return the middle int value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of all seven elements as an int.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * int s = t.sum();   // 28
         *
         * IntTuple.IntTuple7 neg = IntTuple.of(-1, -2, -3, -4, -5, -6, -7);
         * int negSum = neg.sum();   // -28
         *
         * IntTuple.IntTuple7 zeros = IntTuple.of(0, 0, 0, 0, 0, 0, 0);
         * int zeroSum = zeros.sum();   // 0
         *
         * // overflow: throws ArithmeticException
         * IntTuple.IntTuple7 overflow = IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 0, 0, 0, 0);
         * overflow.sum();   // throws ArithmeticException
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 as an int
         * @throws ArithmeticException if the total does not fit in an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the average of all seven elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * double avg = t.average();   // 4.0
         *
         * IntTuple.IntTuple7 evens = IntTuple.of(2, 4, 6, 8, 10, 12, 14);
         * double evenAvg = evens.average();   // 8.0
         *
         * IntTuple.IntTuple7 neg = IntTuple.of(-7, -6, -5, -4, -3, -2, -1);
         * double negAvg = neg.average();   // -4.0
         *
         * IntTuple.IntTuple7 mixed = IntTuple.of(0, 0, 0, 0, 0, 0, 7);
         * double mixedAvg = mixed.average();   // 1.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * IntTuple.IntTuple7 rev = t.reverse();
         * // rev.toString() returns "(7, 6, 5, 4, 3, 2, 1)"
         *
         * IntTuple.IntTuple7 same = IntTuple.of(5, 5, 5, 5, 5, 5, 5);
         * IntTuple.IntTuple7 sameRev = same.reverse();
         * // sameRev.toString() returns "(5, 5, 5, 5, 5, 5, 5)"
         *
         * IntTuple.IntTuple7 neg = IntTuple.of(-3, -2, -1, 0, 1, 2, 3);
         * IntTuple.IntTuple7 negRev = neg.reverse();
         * // negRev.toString() returns "(3, 2, 1, 0, -1, -2, -3)"
         *
         * IntTuple.IntTuple7 boundary = IntTuple.of(Integer.MIN_VALUE, 0, 0, 0, 0, 0, Integer.MAX_VALUE);
         * IntTuple.IntTuple7 boundRev = boundary.reverse();
         * // boundRev._1 == Integer.MAX_VALUE, boundRev._7 == Integer.MIN_VALUE
         * }</pre>
         *
         * @return a new IntTuple.IntTuple7 with values (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple7 reverse() {
            return new IntTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * boolean has4 = t.contains(4);   // true
         * boolean has8 = t.contains(8);   // false
         *
         * IntTuple.IntTuple7 dup = IntTuple.of(3, 3, 3, 3, 3, 3, 3);
         * boolean has3 = dup.contains(3);   // true
         * boolean has0 = dup.contains(0);   // false
         *
         * IntTuple.IntTuple7 neg = IntTuple.of(-1, -2, -3, 0, 1, 2, 3);
         * boolean hasNeg = neg.contains(-2);        // true
         * boolean hasMissing = neg.contains(-10);   // false
         * }</pre>
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the seven elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * java.util.List<Integer> collected = new java.util.ArrayList<>();
         * t.forEach(collected::add);
         * // collected == [1, 2, 3, 4, 5, 6, 7]
         *
         * int[] sum = {0};
         * t.forEach(v -> sum[0] += v);
         * // sum[0] == 28
         *
         * // null action throws IllegalArgumentException
         * t.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the action to perform on each element
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
            action.accept(_4);
            action.accept(_5);
            action.accept(_6);
            action.accept(_7);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all seven elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * IntTuple.IntTuple7 t2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * // t1.hashCode() == t2.hashCode()
         *
         * IntTuple.IntTuple7 t3 = IntTuple.of(7, 6, 5, 4, 3, 2, 1);
         * // t1.hashCode() != t3.hashCode() (different element order)
         *
         * IntTuple.IntTuple7 zeros = IntTuple.of(0, 0, 0, 0, 0, 0, 0);
         * // zeros.hashCode() == 0
         * }</pre>
         *
         * @return a hash code based on all seven elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6)) + _7;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple7 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * IntTuple.IntTuple7 t2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * boolean eq = t1.equals(t2);   // true
         *
         * IntTuple.IntTuple7 t3 = IntTuple.of(7, 6, 5, 4, 3, 2, 1);
         * boolean neq = t1.equals(t3);   // false
         *
         * boolean selfEq = t1.equals(t1);   // true
         *
         * boolean nullEq = t1.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple7 with equal elements, {@code false} otherwise
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
         * The format is (_1, _2, _3, _4, _5, _6, _7) where each element is displayed in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple7 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7);
         * String s = t.toString();   // "(1, 2, 3, 4, 5, 6, 7)"
         *
         * IntTuple.IntTuple7 neg = IntTuple.of(-3, -2, -1, 0, 1, 2, 3);
         * String negS = neg.toString();   // "(-3, -2, -1, 0, 1, 2, 3)"
         *
         * IntTuple.IntTuple7 same = IntTuple.of(0, 0, 0, 0, 0, 0, 0);
         * String sameS = same.toString();   // "(0, 0, 0, 0, 0, 0, 0)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6, _7)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly eight int values.
     * The values are accessible through the public final fields {@code _1} through {@code _8}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple8 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
     * boolean contains5 = tuple.contains(5);   // true
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more int values
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * int arity = t.arity();   // 8
         *
         * IntTuple.IntTuple8 neg = IntTuple.of(-1, -2, -3, -4, -5, -6, -7, -8);
         * int negArity = neg.arity();   // 8
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t = IntTuple.of(3, 1, 4, 1, 5, 9, 2, 6);
         * int min = t.min();   // 1
         *
         * IntTuple.IntTuple8 neg = IntTuple.of(-10, -5, 0, 5, 10, 3, 7, -8);
         * int negMin = neg.min();   // -10
         *
         * IntTuple.IntTuple8 same = IntTuple.of(4, 4, 4, 4, 4, 4, 4, 4);
         * int sameMin = same.min();   // 4
         *
         * IntTuple.IntTuple8 boundary = IntTuple.of(Integer.MIN_VALUE, 0, 1, 2, 3, 4, 5, 6);
         * int boundMin = boundary.min();   // Integer.MIN_VALUE
         * }</pre>
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, _7, and _8
         */
        @Override
        public int min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the maximum value among the eight elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t = IntTuple.of(3, 1, 4, 1, 5, 9, 2, 6);
         * int max = t.max();   // 9
         *
         * IntTuple.IntTuple8 neg = IntTuple.of(-10, -5, -3, -2, -1, -7, -9, -4);
         * int negMax = neg.max();   // -1
         *
         * IntTuple.IntTuple8 same = IntTuple.of(4, 4, 4, 4, 4, 4, 4, 4);
         * int sameMax = same.max();   // 4
         *
         * IntTuple.IntTuple8 boundary = IntTuple.of(0, 1, 2, 3, 4, 5, 6, Integer.MAX_VALUE);
         * int boundMax = boundary.max();   // Integer.MAX_VALUE
         * }</pre>
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, and _8
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the median value of the eight elements.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * int med = t.median();   // 4
         *
         * IntTuple.IntTuple8 unsorted = IntTuple.of(8, 3, 6, 1, 7, 2, 5, 4);
         * int unsortedMed = unsorted.median();   // 4
         *
         * IntTuple.IntTuple8 neg = IntTuple.of(-4, -3, -2, -1, 1, 2, 3, 4);
         * int negMed = neg.median();   // -1
         *
         * IntTuple.IntTuple8 dup = IntTuple.of(5, 5, 5, 5, 5, 5, 5, 5);
         * int dupMed = dup.median();   // 5
         * }</pre>
         *
         * @return the median (lower middle) int value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of all eight elements as an int.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * int s = t.sum();   // 36
         *
         * IntTuple.IntTuple8 neg = IntTuple.of(-1, -2, -3, -4, -5, -6, -7, -8);
         * int negSum = neg.sum();   // -36
         *
         * IntTuple.IntTuple8 zeros = IntTuple.of(0, 0, 0, 0, 0, 0, 0, 0);
         * int zeroSum = zeros.sum();   // 0
         *
         * // overflow: throws ArithmeticException
         * IntTuple.IntTuple8 overflow = IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 0, 0, 0, 0, 0);
         * overflow.sum();   // throws ArithmeticException
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 as an int
         * @throws ArithmeticException if the total does not fit in an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the average of all eight elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * double avg = t.average();   // 4.5
         *
         * IntTuple.IntTuple8 evens = IntTuple.of(2, 4, 6, 8, 10, 12, 14, 16);
         * double evenAvg = evens.average();   // 9.0
         *
         * IntTuple.IntTuple8 neg = IntTuple.of(-8, -7, -6, -5, -4, -3, -2, -1);
         * double negAvg = neg.average();   // -4.5
         *
         * IntTuple.IntTuple8 zeros = IntTuple.of(0, 0, 0, 0, 0, 0, 0, 0);
         * double zeroAvg = zeros.average();   // 0.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * IntTuple.IntTuple8 rev = t.reverse();
         * // rev.toString() returns "(8, 7, 6, 5, 4, 3, 2, 1)"
         *
         * IntTuple.IntTuple8 same = IntTuple.of(5, 5, 5, 5, 5, 5, 5, 5);
         * IntTuple.IntTuple8 sameRev = same.reverse();
         * // sameRev.toString() returns "(5, 5, 5, 5, 5, 5, 5, 5)"
         *
         * IntTuple.IntTuple8 neg = IntTuple.of(-4, -3, -2, -1, 1, 2, 3, 4);
         * IntTuple.IntTuple8 negRev = neg.reverse();
         * // negRev.toString() returns "(4, 3, 2, 1, -1, -2, -3, -4)"
         *
         * IntTuple.IntTuple8 boundary = IntTuple.of(Integer.MIN_VALUE, 0, 0, 0, 0, 0, 0, Integer.MAX_VALUE);
         * IntTuple.IntTuple8 boundRev = boundary.reverse();
         * // boundRev._1 == Integer.MAX_VALUE, boundRev._8 == Integer.MIN_VALUE
         * }</pre>
         *
         * @return a new IntTuple.IntTuple8 with values (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple8 reverse() {
            return new IntTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * boolean has5 = t.contains(5);   // true
         * boolean has9 = t.contains(9);   // false
         *
         * IntTuple.IntTuple8 dup = IntTuple.of(3, 3, 3, 3, 3, 3, 3, 3);
         * boolean has3 = dup.contains(3);   // true
         * boolean has0 = dup.contains(0);   // false
         *
         * IntTuple.IntTuple8 neg = IntTuple.of(-4, -3, -2, -1, 1, 2, 3, 4);
         * boolean hasNeg = neg.contains(-3);      // true
         * boolean hasMissing = neg.contains(0);   // false
         * }</pre>
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the eight elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * java.util.List<Integer> collected = new java.util.ArrayList<>();
         * t.forEach(collected::add);
         * // collected == [1, 2, 3, 4, 5, 6, 7, 8]
         *
         * int[] sum = {0};
         * t.forEach(v -> sum[0] += v);
         * // sum[0] == 36
         *
         * // null action throws IllegalArgumentException
         * t.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the action to perform on each element
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
            action.accept(_4);
            action.accept(_5);
            action.accept(_6);
            action.accept(_7);
            action.accept(_8);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all eight elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * IntTuple.IntTuple8 t2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * // t1.hashCode() == t2.hashCode()
         *
         * IntTuple.IntTuple8 t3 = IntTuple.of(8, 7, 6, 5, 4, 3, 2, 1);
         * // t1.hashCode() != t3.hashCode() (different element order)
         *
         * IntTuple.IntTuple8 zeros = IntTuple.of(0, 0, 0, 0, 0, 0, 0, 0);
         * // zeros.hashCode() == 0
         * }</pre>
         *
         * @return a hash code based on all eight elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6) + _7)) + _8;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple8 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * IntTuple.IntTuple8 t2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * boolean eq = t1.equals(t2);   // true
         *
         * IntTuple.IntTuple8 t3 = IntTuple.of(8, 7, 6, 5, 4, 3, 2, 1);
         * boolean neq = t1.equals(t3);   // false
         *
         * boolean selfEq = t1.equals(t1);   // true
         *
         * boolean nullEq = t1.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple8 with equal elements, {@code false} otherwise
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
         * The format is (_1, _2, _3, _4, _5, _6, _7, _8) where each element is displayed in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple8 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8);
         * String s = t.toString();   // "(1, 2, 3, 4, 5, 6, 7, 8)"
         *
         * IntTuple.IntTuple8 neg = IntTuple.of(-4, -3, -2, -1, 1, 2, 3, 4);
         * String negS = neg.toString();   // "(-4, -3, -2, -1, 1, 2, 3, 4)"
         *
         * IntTuple.IntTuple8 same = IntTuple.of(0, 0, 0, 0, 0, 0, 0, 0);
         * String sameS = same.toString();   // "(0, 0, 0, 0, 0, 0, 0, 0)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6, _7, _8)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly nine int values.
     * The values are accessible through the public final fields {@code _1} through {@code _9}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * IntTuple.IntTuple9 tuple = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
     * double avg = tuple.average();   // 5.0
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more int values
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * int arity = t.arity();   // 9
         *
         * IntTuple.IntTuple9 neg = IntTuple.of(-1, -2, -3, -4, -5, -6, -7, -8, -9);
         * int negArity = neg.arity();   // 9
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t = IntTuple.of(3, 1, 4, 1, 5, 9, 2, 6, 5);
         * int min = t.min();   // 1
         *
         * IntTuple.IntTuple9 neg = IntTuple.of(-10, -5, 0, 5, 10, 3, 7, -8, 2);
         * int negMin = neg.min();   // -10
         *
         * IntTuple.IntTuple9 same = IntTuple.of(4, 4, 4, 4, 4, 4, 4, 4, 4);
         * int sameMin = same.min();   // 4
         *
         * IntTuple.IntTuple9 boundary = IntTuple.of(Integer.MIN_VALUE, 0, 1, 2, 3, 4, 5, 6, 7);
         * int boundMin = boundary.min();   // Integer.MIN_VALUE
         * }</pre>
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, _7, _8, and _9
         */
        @Override
        public int min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the maximum value among the nine elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t = IntTuple.of(3, 1, 4, 1, 5, 9, 2, 6, 5);
         * int max = t.max();   // 9
         *
         * IntTuple.IntTuple9 neg = IntTuple.of(-10, -5, -3, -2, -1, -7, -9, -4, -6);
         * int negMax = neg.max();   // -1
         *
         * IntTuple.IntTuple9 same = IntTuple.of(4, 4, 4, 4, 4, 4, 4, 4, 4);
         * int sameMax = same.max();   // 4
         *
         * IntTuple.IntTuple9 boundary = IntTuple.of(0, 1, 2, 3, 4, 5, 6, 7, Integer.MAX_VALUE);
         * int boundMax = boundary.max();   // Integer.MAX_VALUE
         * }</pre>
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, _8, and _9
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the median value of the nine elements.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * int med = t.median();   // 5
         *
         * IntTuple.IntTuple9 unsorted = IntTuple.of(9, 3, 7, 1, 5, 2, 8, 4, 6);
         * int unsortedMed = unsorted.median();   // 5
         *
         * IntTuple.IntTuple9 neg = IntTuple.of(-4, -3, -2, -1, 0, 1, 2, 3, 4);
         * int negMed = neg.median();   // 0
         *
         * IntTuple.IntTuple9 dup = IntTuple.of(5, 5, 5, 5, 5, 5, 5, 5, 5);
         * int dupMed = dup.median();   // 5
         * }</pre>
         *
         * @return the middle int value when sorted
         */
        @Override
        public int median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of all nine elements as an int.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * int s = t.sum();   // 45
         *
         * IntTuple.IntTuple9 neg = IntTuple.of(-1, -2, -3, -4, -5, -6, -7, -8, -9);
         * int negSum = neg.sum();   // -45
         *
         * IntTuple.IntTuple9 zeros = IntTuple.of(0, 0, 0, 0, 0, 0, 0, 0, 0);
         * int zeroSum = zeros.sum();   // 0
         *
         * // overflow: throws ArithmeticException
         * IntTuple.IntTuple9 overflow = IntTuple.of(Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 0, 0, 0, 0, 0, 0);
         * overflow.sum();   // throws ArithmeticException
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9 as an int
         * @throws ArithmeticException if the total does not fit in an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the average of all nine elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * double avg = t.average();   // 5.0
         *
         * IntTuple.IntTuple9 evens = IntTuple.of(2, 4, 6, 8, 10, 12, 14, 16, 18);
         * double evenAvg = evens.average();   // 10.0
         *
         * IntTuple.IntTuple9 neg = IntTuple.of(-9, -8, -7, -6, -5, -4, -3, -2, -1);
         * double negAvg = neg.average();   // -5.0
         *
         * IntTuple.IntTuple9 zeros = IntTuple.of(0, 0, 0, 0, 0, 0, 0, 0, 0);
         * double zeroAvg = zeros.average();   // 0.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * IntTuple.IntTuple9 rev = t.reverse();
         * // rev.toString() returns "(9, 8, 7, 6, 5, 4, 3, 2, 1)"
         *
         * IntTuple.IntTuple9 same = IntTuple.of(5, 5, 5, 5, 5, 5, 5, 5, 5);
         * IntTuple.IntTuple9 sameRev = same.reverse();
         * // sameRev.toString() returns "(5, 5, 5, 5, 5, 5, 5, 5, 5)"
         *
         * IntTuple.IntTuple9 neg = IntTuple.of(-4, -3, -2, -1, 0, 1, 2, 3, 4);
         * IntTuple.IntTuple9 negRev = neg.reverse();
         * // negRev.toString() returns "(4, 3, 2, 1, 0, -1, -2, -3, -4)"
         *
         * IntTuple.IntTuple9 boundary = IntTuple.of(Integer.MIN_VALUE, 0, 0, 0, 0, 0, 0, 0, Integer.MAX_VALUE);
         * IntTuple.IntTuple9 boundRev = boundary.reverse();
         * // boundRev._1 == Integer.MAX_VALUE, boundRev._9 == Integer.MIN_VALUE
         * }</pre>
         *
         * @return a new IntTuple.IntTuple9 with values (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public IntTuple9 reverse() {
            return new IntTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * boolean has5 = t.contains(5);     // true
         * boolean has10 = t.contains(10);   // false
         *
         * IntTuple.IntTuple9 dup = IntTuple.of(3, 3, 3, 3, 3, 3, 3, 3, 3);
         * boolean has3 = dup.contains(3);   // true
         * boolean has0 = dup.contains(0);   // false
         *
         * IntTuple.IntTuple9 neg = IntTuple.of(-4, -3, -2, -1, 0, 1, 2, 3, 4);
         * boolean hasNeg = neg.contains(-3);      // true
         * boolean hasMissing = neg.contains(5);   // false
         * }</pre>
         *
         * @param valueToFind the int value to search for
         * @return {@code true} if valueToFind equals any of the nine elements
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * java.util.List<Integer> collected = new java.util.ArrayList<>();
         * t.forEach(collected::add);
         * // collected == [1, 2, 3, 4, 5, 6, 7, 8, 9]
         *
         * int[] sum = {0};
         * t.forEach(v -> sum[0] += v);
         * // sum[0] == 45
         *
         * // null action throws IllegalArgumentException
         * t.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the action to perform on each element
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
            action.accept(_4);
            action.accept(_5);
            action.accept(_6);
            action.accept(_7);
            action.accept(_8);
            action.accept(_9);
        }

        /**
         * Returns a hash code value for this tuple.
         * The hash code is computed using a polynomial hash function
         * based on all nine elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * IntTuple.IntTuple9 t2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * // t1.hashCode() == t2.hashCode()
         *
         * IntTuple.IntTuple9 t3 = IntTuple.of(9, 8, 7, 6, 5, 4, 3, 2, 1);
         * // t1.hashCode() != t3.hashCode() (different element order)
         *
         * IntTuple.IntTuple9 zeros = IntTuple.of(0, 0, 0, 0, 0, 0, 0, 0, 0);
         * // zeros.hashCode() == 0
         * }</pre>
         *
         * @return a hash code based on all nine elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * _1 + _2) + _3) + _4) + _5) + _6) + _7) + _8)) + _9;
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both IntTuple.IntTuple9 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t1 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * IntTuple.IntTuple9 t2 = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * boolean eq = t1.equals(t2);   // true
         *
         * IntTuple.IntTuple9 t3 = IntTuple.of(9, 8, 7, 6, 5, 4, 3, 2, 1);
         * boolean neq = t1.equals(t3);   // false
         *
         * boolean selfEq = t1.equals(t1);   // true
         *
         * boolean nullEq = t1.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is an IntTuple.IntTuple9 with equal elements, {@code false} otherwise
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
         * The format is (_1, _2, _3, _4, _5, _6, _7, _8, _9) where each element is displayed in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * IntTuple.IntTuple9 t = IntTuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
         * String s = t.toString();   // "(1, 2, 3, 4, 5, 6, 7, 8, 9)"
         *
         * IntTuple.IntTuple9 neg = IntTuple.of(-4, -3, -2, -1, 0, 1, 2, 3, 4);
         * String negS = neg.toString();   // "(-4, -3, -2, -1, 0, 1, 2, 3, 4)"
         *
         * IntTuple.IntTuple9 same = IntTuple.of(0, 0, 0, 0, 0, 0, 0, 0, 0);
         * String sameS = same.toString();   // "(0, 0, 0, 0, 0, 0, 0, 0, 0)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6, _7, _8, _9)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ")";
        }

        /**
         * Returns the internal array of int elements.
         * The array is lazily initialized on first access.
         *
         * @return an int array containing all elements of this tuple
         */
        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
