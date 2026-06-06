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
import com.landawn.abacus.util.stream.LongStream;

/**
 * Base class for immutable tuples of primitive {@code long} values.
 *
 * <p>The nested tuple types model fixed arities from 0 through 9. Factory methods such as
 * {@link #copyOf(long[])} and the {@code of(...)} overloads select the matching subtype, while the base
 * class supplies aggregate, reversal, containment, and functional helper operations.</p>
 *
 * @param <TP> the concrete {@code LongTuple} subtype that fluent operations such as {@link #reverse()} return
 * @see PrimitiveTuple
 * @see BooleanTuple
 * @see ByteTuple
 * @see CharTuple
 * @see ShortTuple
 * @see IntTuple
 * @see FloatTuple
 * @see DoubleTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class LongTuple<TP extends LongTuple<TP>> extends PrimitiveTuple<TP> {

    /** Lazily initialized cached array view of all tuple elements. */
    protected volatile long[] elements;

    /**
     * Protected constructor for subclass instantiation.
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link #of(long)}, {@link #of(long, long)}, etc., to create tuple instances.
     */
    protected LongTuple() {
    }

    /**
     * Creates a LongTuple.LongTuple1 containing a single long value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple1 single = LongTuple.of(42L);
     * long value = single._1;            // returns 42
     *
     * LongTuple.LongTuple1 neg = LongTuple.of(-1L);
     * long negValue = neg._1;            // returns -1
     *
     * LongTuple.LongTuple1 max = LongTuple.of(Long.MAX_VALUE);
     * long maxValue = max._1;            // returns 9223372036854775807
     *
     * LongTuple.LongTuple1 zero = LongTuple.of(0L);
     * zero.arity();                      // returns 1
     * }</pre>
     *
     * @param _1 the long value to store in the tuple
     * @return a new LongTuple.LongTuple1 containing the specified value
     */
    public static LongTuple1 of(final long _1) {
        return new LongTuple1(_1);
    }

    /**
     * Creates a LongTuple.LongTuple2 containing two long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple2 pair = LongTuple.of(1L, 2L);
     * long sum = pair._1 + pair._2;  // returns 3
     *
     * LongTuple.LongTuple2 negPair = LongTuple.of(-5L, 5L);
     * negPair.sum();                 // returns 0
     *
     * LongTuple.LongTuple2 same = LongTuple.of(7L, 7L);
     * same.min();                    // returns 7
     *
     * LongTuple.LongTuple2 bounds = LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE);
     * bounds.arity();                // returns 2
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @return a new LongTuple.LongTuple2 containing the specified values
     */
    public static LongTuple2 of(final long _1, final long _2) {
        return new LongTuple2(_1, _2);
    }

    /**
     * Creates a LongTuple.LongTuple3 containing three long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
     * double average = triple.average();   // returns 2.0
     *
     * LongTuple.LongTuple3 desc = LongTuple.of(5L, 3L, 1L);
     * desc.min();                          // returns 1
     * desc.max();                          // returns 5
     *
     * LongTuple.LongTuple3 neg = LongTuple.of(-3L, 0L, 3L);
     * neg.sum();                           // returns 0
     *
     * LongTuple.LongTuple3 allNeg = LongTuple.of(-3L, -2L, -1L);
     * allNeg.median();                     // returns -2
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @return a new LongTuple.LongTuple3 containing the specified values
     */
    public static LongTuple3 of(final long _1, final long _2, final long _3) {
        return new LongTuple3(_1, _2, _3);
    }

    /**
     * Creates a LongTuple.LongTuple4 containing four long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
     * quad._1;                        // returns 1
     * quad._4;                        // returns 4
     *
     * LongTuple.LongTuple4 even = LongTuple.of(1L, 2L, 3L, 4L);
     * even.median();                  // returns 2 (lower of the two middle values when sorted)
     *
     * LongTuple.LongTuple4 neg = LongTuple.of(-4L, -3L, -2L, -1L);
     * neg.sum();                      // returns -10
     *
     * LongTuple.LongTuple4 mixed = LongTuple.of(0L, 0L, 0L, 0L);
     * mixed.min();                    // returns 0
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @return a new LongTuple.LongTuple4 containing the specified values
     */
    public static LongTuple4 of(final long _1, final long _2, final long _3, final long _4) {
        return new LongTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a LongTuple.LongTuple5 containing five long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple5 quint = LongTuple.of(1L, 2L, 3L, 4L, 5L);
     * quint._5;                       // returns 5
     * quint.sum();                    // returns 15
     *
     * LongTuple.LongTuple5 asc = LongTuple.of(10L, 20L, 30L, 40L, 50L);
     * asc.median();                   // returns 30 (middle value when sorted)
     *
     * LongTuple.LongTuple5 neg = LongTuple.of(-5L, -4L, -3L, -2L, -1L);
     * neg.max();                      // returns -1
     *
     * LongTuple.LongTuple5 mixed = LongTuple.of(-2L, -1L, 0L, 1L, 2L);
     * mixed.average();                // returns 0.0
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @return a new LongTuple.LongTuple5 containing the specified values
     */
    public static LongTuple5 of(final long _1, final long _2, final long _3, final long _4, final long _5) {
        return new LongTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a LongTuple.LongTuple6 containing six long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple6 sext = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
     * sext._6;                        // returns 6
     * sext.sum();                     // returns 21
     *
     * LongTuple.LongTuple6 even = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
     * even.median();                  // returns 3 (lower of the two middle values when sorted)
     *
     * LongTuple.LongTuple6 neg = LongTuple.of(-6L, -5L, -4L, -3L, -2L, -1L);
     * neg.min();                      // returns -6
     *
     * LongTuple.LongTuple6 zeros = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L);
     * zeros.average();                // returns 0.0
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @param _6 the sixth long value
     * @return a new LongTuple.LongTuple6 containing the specified values
     */
    public static LongTuple6 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6) {
        return new LongTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a LongTuple.LongTuple7 containing seven long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple7 sept = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
     * sept._7;                        // returns 7
     * sept.sum();                     // returns 28
     *
     * LongTuple.LongTuple7 asc = LongTuple.of(10L, 20L, 30L, 40L, 50L, 60L, 70L);
     * asc.median();                   // returns 40 (middle value when sorted)
     *
     * LongTuple.LongTuple7 allSame = LongTuple.of(5L, 5L, 5L, 5L, 5L, 5L, 5L);
     * allSame.min();                  // returns 5
     * allSame.max();                  // returns 5
     *
     * LongTuple.LongTuple7 neg = LongTuple.of(-7L, -6L, -5L, -4L, -3L, -2L, -1L);
     * neg.average();                  // returns -4.0
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @param _6 the sixth long value
     * @param _7 the seventh long value
     * @return a new LongTuple.LongTuple7 containing the specified values
     */
    public static LongTuple7 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7) {
        return new LongTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a LongTuple.LongTuple8 containing eight long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple8 oct = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
     * oct._8;                         // returns 8
     * oct.arity();                    // returns 8
     *
     * LongTuple.LongTuple8 asc = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
     * asc.sum();                      // returns 36
     *
     * LongTuple.LongTuple8 neg = LongTuple.of(-8L, -7L, -6L, -5L, -4L, -3L, -2L, -1L);
     * neg.min();                      // returns -8
     * neg.max();                      // returns -1
     *
     * LongTuple.LongTuple8 zeros = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
     * zeros.sum();                    // returns 0
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @param _6 the sixth long value
     * @param _7 the seventh long value
     * @param _8 the eighth long value
     * @return a new LongTuple.LongTuple8 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more long values
     */
    @Deprecated
    public static LongTuple8 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8) {
        return new LongTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a LongTuple.LongTuple9 containing nine long values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple9 non = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
     * non._9;                         // returns 9
     * non.arity();                    // returns 9
     *
     * LongTuple.LongTuple9 asc = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
     * asc.sum();                      // returns 45
     * asc.median();                   // returns 5 (middle element when sorted)
     *
     * LongTuple.LongTuple9 neg = LongTuple.of(-9L, -8L, -7L, -6L, -5L, -4L, -3L, -2L, -1L);
     * neg.min();                      // returns -9
     *
     * LongTuple.LongTuple9 mixed = LongTuple.of(-4L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L);
     * mixed.sum();                    // returns 0
     * }</pre>
     *
     * @param _1 the first long value
     * @param _2 the second long value
     * @param _3 the third long value
     * @param _4 the fourth long value
     * @param _5 the fifth long value
     * @param _6 the sixth long value
     * @param _7 the seventh long value
     * @param _8 the eighth long value
     * @param _9 the ninth long value
     * @return a new LongTuple.LongTuple9 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more long values
     */
    @Deprecated
    public static LongTuple9 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8,
            final long _9) {
        return new LongTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a {@code LongTuple} from an array of long values.
     * <p>
     * The size of the returned tuple depends on the length of the input array.
     * This factory method supports arrays with 0 to 9 elements. For {@code null} or empty
     * arrays, returns the shared empty tuple. For arrays with 1-9 elements, returns the
     * corresponding {@code LongTuple1}..{@code LongTuple9} instance. The values are copied
     * into the new tuple; subsequent modifications to the input array do not affect it.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create a 3-element tuple from an array
     * long[] values = {1L, 2L, 3L};
     * LongTuple.LongTuple3 tuple = LongTuple.copyOf(values);
     * tuple.sum();                                           // returns 6
     *
     * // Single element
     * LongTuple.LongTuple1 single = LongTuple.copyOf(new long[]{42L});
     * single._1;                                            // returns 42
     *
     * // null or empty array returns the shared empty tuple (arity == 0)
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     * empty.arity();                                        // returns 0
     * LongTuple<?> fromNull = LongTuple.copyOf(null);
     * fromNull.arity();                                     // returns 0
     *
     * // Arrays longer than 9 elements throw
     * LongTuple.copyOf(new long[10]);                       // throws IllegalArgumentException
     * }</pre>
     *
     * <p><strong>Type note:</strong> the runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type. Assigning to the wrong arity-specific subtype will result in a
     * {@link ClassCastException} at the assignment site.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of long values; may be {@code null} or empty, in which case the shared empty tuple is returned
     * @return a {@code LongTuple} of the appropriate arity containing the array values, or the shared empty tuple if the array is {@code null} or empty
     * @throws IllegalArgumentException if {@code values} has more than 9 elements
     * @see #of(long)
     */
    @SuppressWarnings("deprecation")
    public static <TP extends LongTuple<TP>> TP copyOf(final long[] values) {
        if (values == null || values.length == 0) {
            return (TP) LongTuple0.EMPTY;
        }

        switch (values.length) {
            case 1:
                return (TP) LongTuple.of(values[0]);

            case 2:
                return (TP) LongTuple.of(values[0], values[1]);

            case 3:
                return (TP) LongTuple.of(values[0], values[1], values[2]);

            case 4:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3]);

            case 5:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3], values[4]);

            case 6:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);

            case 7:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);

            case 8:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);

            case 9:
                return (TP) LongTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);

            default:
                throw new IllegalArgumentException("Too many elements (" + values.length + "). Maximum: 9");
        }
    }

    private static double averageOf(final long... values) {
        if (values.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        final int count = values.length;
        long quotientSum = 0;
        long remainderSum = 0;

        for (final long value : values) {
            quotientSum += value / count;
            remainderSum += value % count;
        }

        return quotientSum + ((double) remainderSum) / count;
    }

    /**
     * Returns the minimum long value in this tuple.
     * <p>
     * Iterates through all elements in the tuple and returns the smallest value.
     * For single-element tuples, the element itself is returned.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
     * long min = tuple.min();                          // returns 1
     *
     * LongTuple.LongTuple1 single = LongTuple.of(42L);
     * long singleMin = single.min();                   // returns 42
     *
     * LongTuple.LongTuple2 negPair = LongTuple.of(-10L, -5L);
     * negPair.min();                                   // returns -10
     *
     * LongTuple.LongTuple2 bounds = LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE);
     * bounds.min();                                    // returns Long.MIN_VALUE
     *
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     * empty.min();                                     // throws NoSuchElementException
     * }</pre>
     *
     * @return the minimum long value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #max()
     * @see #median()
     */
    public long min() {
        final long[] arr = elements();

        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        return N.min(arr);
    }

    /**
     * Returns the maximum long value in this tuple.
     * <p>
     * Iterates through all elements in the tuple and returns the largest value.
     * For single-element tuples, the element itself is returned.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
     * long max = tuple.max();                          // returns 3
     *
     * LongTuple.LongTuple1 single = LongTuple.of(42L);
     * long singleMax = single.max();                   // returns 42
     *
     * LongTuple.LongTuple2 negPair = LongTuple.of(-10L, -5L);
     * negPair.max();                                   // returns -5
     *
     * LongTuple.LongTuple2 bounds = LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE);
     * bounds.max();                                    // returns Long.MAX_VALUE
     *
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     * empty.max();                                     // throws NoSuchElementException
     * }</pre>
     *
     * @return the maximum long value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see #median()
     */
    public long max() {
        final long[] arr = elements();

        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        return N.max(arr);
    }

    /**
     * Returns the median long value in this tuple.
     * <p>
     * The median is calculated by sorting the elements internally. For tuples with an odd
     * number of elements, returns the middle value when sorted. For tuples with an even
     * number of elements, returns the lower of the two middle values when sorted.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Odd-count tuple: middle value when sorted
     * LongTuple.LongTuple3 tuple = LongTuple.of(3L, 1L, 2L);
     * long median = tuple.median();                    // returns 2
     *
     * // Even-count tuple: lower of the two middle values when sorted
     * LongTuple.LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
     * long median2 = quad.median();                    // returns 2
     *
     * // Single-element tuple: the element itself
     * LongTuple.LongTuple1 single = LongTuple.of(42L);
     * single.median();                                 // returns 42
     *
     * // Even pair: lower of the two
     * LongTuple.LongTuple2 pair = LongTuple.of(1L, 3L);
     * pair.median();                                   // returns 1
     *
     * // Empty tuple throws
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     * empty.median();                                  // throws NoSuchElementException
     * }</pre>
     *
     * @return the median long value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see #max()
     * @see N#median(long...)
     */
    public long median() {
        final long[] arr = elements();

        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        return N.median(arr);
    }

    /**
     * Returns the sum of all long values in this tuple as a {@code long}.
     * <p>
     * Note: this method does not check for overflow. If the true total exceeds the range of
     * {@code long}, the result wraps around according to standard two's-complement long arithmetic
     * rather than throwing an exception. For an empty tuple this method returns {@code 0L}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * long sum = tuple.sum();                          // returns 6
     *
     * LongTuple.LongTuple2 pair = LongTuple.of(100L, 200L);
     * long total = pair.sum();                         // returns 300
     *
     * // Empty tuple returns 0
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     * empty.sum();                                     // returns 0
     *
     * // Overflow wraps silently (two's-complement)
     * LongTuple.LongTuple2 overflow = LongTuple.of(Long.MAX_VALUE, 1L);
     * overflow.sum();                                  // returns Long.MIN_VALUE
     *
     * // Negative values
     * LongTuple.LongTuple2 neg = LongTuple.of(-3L, -7L);
     * neg.sum();                                       // returns -10
     * }</pre>
     *
     * @return the sum of all long values in this tuple as a {@code long}
     * @see #average()
     */
    public long sum() {
        return N.sum(elements());
    }

    /**
     * Returns the arithmetic mean of all long values in this tuple as a {@code double}.
     * <p>
     * The result is returned as a {@code double} to preserve fractional precision.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * double avg = tuple.average();                    // returns 2.0
     *
     * // Fractional result
     * LongTuple.LongTuple2 pair = LongTuple.of(1L, 2L);
     * pair.average();                                  // returns 1.5
     *
     * // Single-element tuple
     * LongTuple.LongTuple1 single = LongTuple.of(7L);
     * single.average();                                // returns 7.0
     *
     * // All-negative tuple
     * LongTuple.LongTuple2 neg = LongTuple.of(-3L, -1L);
     * neg.average();                                   // returns -2.0
     *
     * // Empty tuple throws
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     * empty.average();                                 // throws NoSuchElementException
     * }</pre>
     *
     * @return the arithmetic mean of all long values in this tuple as a {@code double}
     * @throws NoSuchElementException if the tuple is empty
     * @see #sum()
     */
    public double average() {
        return averageOf(elements());
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
     * LongTuple.LongTuple2 pair = LongTuple.of(1L, 2L);
     * LongTuple.LongTuple2 reversedPair = pair.reverse();
     * reversedPair.toString();                         // returns "(2, 1)"
     *
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * LongTuple.LongTuple3 reversed = tuple.reverse();
     * reversed.toString();                             // returns "(3, 2, 1)"
     *
     * // Single-element tuple: reverse returns a new tuple with the same element
     * LongTuple.LongTuple1 single = LongTuple.of(42L);
     * single.reverse()._1;                            // returns 42
     *
     * // Original is unmodified (immutable)
     * pair._1;                                        // still returns 1
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified long value.
     * <p>
     * This method performs a linear search through all elements in the tuple to determine
     * if any element matches the specified value. Returns {@code true} if at least one
     * element equals the search value, {@code false} otherwise.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * boolean has2 = tuple.contains(2L);               // returns true
     * boolean has5 = tuple.contains(5L);               // returns false
     *
     * LongTuple.LongTuple2 pair = LongTuple.of(10L, 10L);
     * pair.contains(10L);                              // returns true
     * pair.contains(1L);                               // returns false
     *
     * // Negative value search
     * LongTuple.LongTuple2 neg = LongTuple.of(-1L, -2L);
     * neg.contains(-1L);                               // returns true
     * neg.contains(1L);                                // returns false
     *
     * // Empty tuple never contains anything
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     * empty.contains(0L);                              // returns false
     * }</pre>
     *
     * @param valueToFind the long value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(long valueToFind);

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
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * long[] array = tuple.toArray();                  // returns [1, 2, 3]
     *
     * // Returned array is a defensive copy; mutating it does not affect the tuple
     * array[0] = 99L;
     * tuple._1;                                        // still returns 1
     *
     * LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
     * long[] pairArray = pair.toArray();               // returns [10, 20]
     *
     * // Empty tuple returns a zero-length array
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     * empty.toArray().length;                          // returns 0
     * }</pre>
     *
     * @return a new {@code long[]} array containing all tuple elements in order
     * @see #toList()
     * @see #stream()
     */
    public long[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new LongList containing all elements of this tuple.
     * <p>
     * Converts this tuple to a mutable {@link LongList} containing all elements
     * in their original order. The returned list is a new instance and modifications
     * to it do not affect the original tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * LongList list = tuple.toList();
     * list.size();                                     // returns 3
     * list.get(0);                                     // returns 1
     *
     * // Mutating the list does not change the tuple
     * list.add(4L);                                    // modifies the copy, not the tuple
     * tuple.arity();                                   // still returns 3
     *
     * LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
     * LongList pairList = pair.toList();
     * pairList.get(1);                                 // returns 20
     *
     * // Empty tuple returns an empty list
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     * empty.toList().size();                           // returns 0
     * }</pre>
     *
     * @return a new {@code LongList} containing all tuple elements in order
     * @see #toArray()
     * @see #stream()
     */
    public LongList toList() {
        return LongList.of(elements().clone());
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
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * tuple.forEach(v -> System.out.println("Value: " + v));  // Prints (one per line):
     * //   Value: 1
     * //   Value: 2
     * //   Value: 3
     *
     * // Accumulate sum externally
     * long[] total = {0L};
     * tuple.forEach(v -> total[0] += v);
     * total[0];                                        // returns 6
     *
     * // Empty tuple: action is never called
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     * long[] count = {0L};
     * empty.forEach(v -> count[0]++);                  // action not invoked (empty tuple)
     * count[0];                                        // returns 0
     *
     * // Null action throws immediately
     * // tuple.forEach(null);                             // throws IllegalArgumentException
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the action to be performed for each element, must not be {@code null}
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.LongConsumer<E> action) throws E {
        N.checkArgNotNull(action);

        for (final long element : elements()) {
            action.accept(element);
        }
    }

    /**
     * Returns a LongStream of all elements in this tuple.
     * <p>
     * Converts this tuple to a sequential {@link LongStream} containing all elements
     * in their original order. This allows using standard stream operations like filter,
     * map, and reduce on the tuple elements.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 tuple = LongTuple.of(1L, 2L, 3L);
     * long sum = tuple.stream().sum();                 // returns 6
     *
     * LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
     * long max = pair.stream().max().getAsLong();      // returns 20
     *
     * // Filter and count elements greater than 1
     * long count = tuple.stream().filter(v -> v > 1L).count();   // returns 2
     *
     * // Empty tuple produces an empty stream
     * LongTuple<?> empty = LongTuple.copyOf(new long[0]);
     * empty.stream().sum();                            // returns 0
     * }</pre>
     *
     * @return a sequential {@code LongStream} containing all tuple elements in order
     * @see #toArray()
     * @see #toList()
     */
    public LongStream stream() {
        return LongStream.of(elements());
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
     * LongTuple.LongTuple3 a = LongTuple.of(1L, 2L, 3L);
     * LongTuple.LongTuple3 b = LongTuple.of(1L, 2L, 3L);
     * a.hashCode() == b.hashCode();                    // returns true (equal tuples have equal hash codes)
     *
     * LongTuple.LongTuple2 c = LongTuple.of(1L, 2L);
     * LongTuple.LongTuple3 d = LongTuple.of(1L, 2L, 3L);
     * c.hashCode() == d.hashCode();                    // typically false (different arity)
     *
     * // Different element values produce different hash codes (generally)
     * LongTuple.LongTuple2 e = LongTuple.of(1L, 2L);
     * LongTuple.LongTuple2 f = LongTuple.of(2L, 1L);
     * e.hashCode() == f.hashCode();                    // typically false (different order)
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
     *       (so a {@code LongTuple2} never equals a {@code LongTuple3}), and contains
     *       the same long values in the same order.</li>
     * </ul>
     *
     * <p>This method is consistent with {@link #hashCode()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 a = LongTuple.of(1L, 2L, 3L);
     * LongTuple.LongTuple3 b = LongTuple.of(1L, 2L, 3L);
     * a.equals(b);                                     // returns true
     *
     * // Same reference always equal
     * a.equals(a);                                     // returns true
     *
     * // Different element order is not equal
     * LongTuple.LongTuple2 c = LongTuple.of(1L, 2L);
     * LongTuple.LongTuple2 d = LongTuple.of(2L, 1L);
     * c.equals(d);                                     // returns false
     *
     * // Different arity is not equal even if elements overlap
     * LongTuple.LongTuple2 e = LongTuple.of(1L, 2L);
     * LongTuple.LongTuple3 f = LongTuple.of(1L, 2L, 3L);
     * e.equals(f);                                     // returns false
     *
     * // null is never equal
     * a.equals(null);                                  // returns false
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
            return N.equals(elements(), ((LongTuple<TP>) obj).elements());
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
     * LongTuple.of(1L, 2L, 3L).toString();            // returns "(1, 2, 3)"
     * LongTuple.of(1L, 2L).toString();                // returns "(1, 2)"
     * LongTuple.of(1L).toString();                    // returns "(1)"
     *
     * // Empty tuple
     * LongTuple.copyOf(new long[0]).toString();        // returns "()"
     *
     * // Negative values
     * LongTuple.of(-3L, 0L, 3L).toString();           // returns "(-3, 0, 3)"
     * }</pre>
     *
     * @return a string representation of this tuple
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    /**
     * Returns the internal array containing all long elements in this tuple.
     * <p>
     * <b>Warning:</b> The returned array is the internal representation of this tuple.
     * Modifying the returned array will compromise the immutability of this tuple.
     * Use {@link #toArray()} instead if you need an array that can be safely modified.
     * </p>
     *
     * @return the internal array of long elements
     */
    protected abstract long[] elements();

    /**
     * An empty LongTuple containing no elements (arity 0).
     * <p>
     * This package-private class is exposed only through the base {@code LongTuple} type
     * via the singleton instance returned by {@link #copyOf(long[])} when invoked with a
     * {@code null} or zero-length array. {@link #sum()} returns 0L, while {@link #min()},
     * {@link #max()}, {@link #median()}, and {@link #average()} all throw {@link java.util.NoSuchElementException}.
     * </p>
     */
    static final class LongTuple0 extends LongTuple<LongTuple0> {

        private static final LongTuple0 EMPTY = new LongTuple0();

        LongTuple0() {
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
         * Returns the minimum long value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public long min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the maximum long value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public long max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the median long value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public long median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the sum of all values in this tuple.
         * For an empty tuple, the sum is {@code 0L}.
         *
         * @return {@code 0L}
         */
        @Override
        public long sum() {
            return 0;
        }

        /**
         * Returns the average of all long values in this tuple.
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
         * @return this {@code LongTuple0} instance
         */
        @Override
        public LongTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified long value.
         * Since this tuple is empty, this method always returns {@code false}.
         *
         * @param valueToFind the long value to search for
         * @return {@code false} always, because the tuple is empty
         */
        @Override
        public boolean contains(final long valueToFind) {
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
         * Returns the shared empty long array.
         *
         * @return an empty long array
         */
        @Override
        protected long[] elements() {
            return N.EMPTY_LONG_ARRAY;
        }
    }

    /**
     * A tuple containing exactly one long value.
     * The value is accessible through the public final field {@code _1}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple1 single = LongTuple.of(42L);
     * long value = single._1;  // 42
     * }</pre>
     *
     */
    public static final class LongTuple1 extends LongTuple<LongTuple1> {

        /** The single long value stored in this tuple. */
        public final long _1;

        LongTuple1() {
            this(0);
        }

        LongTuple1(final long _1) {
            this._1 = _1;
        }

        /**
         * Returns the number of elements in this tuple, which is always 1.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple1 t = LongTuple.of(42L);
         * t.arity();                                       // returns 1
         *
         * LongTuple.LongTuple1 neg = LongTuple.of(-1L);
         * neg.arity();                                     // returns 1
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
         * LongTuple.LongTuple1 t = LongTuple.of(42L);
         * t.min();                                         // returns 42
         *
         * LongTuple.LongTuple1 neg = LongTuple.of(-5L);
         * neg.min();                                       // returns -5
         *
         * LongTuple.LongTuple1 minVal = LongTuple.of(Long.MIN_VALUE);
         * minVal.min();                                    // returns Long.MIN_VALUE
         * }</pre>
         *
         * @return the single element value
         */
        @Override
        public long min() {
            return _1;
        }

        /**
         * Returns the maximum value in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple1 t = LongTuple.of(42L);
         * t.max();                                         // returns 42
         *
         * LongTuple.LongTuple1 neg = LongTuple.of(-5L);
         * neg.max();                                       // returns -5
         *
         * LongTuple.LongTuple1 maxVal = LongTuple.of(Long.MAX_VALUE);
         * maxVal.max();                                    // returns Long.MAX_VALUE
         * }</pre>
         *
         * @return the single element value
         */
        @Override
        public long max() {
            return _1;
        }

        /**
         * Returns the median value in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple1 t = LongTuple.of(42L);
         * t.median();                                      // returns 42
         *
         * LongTuple.LongTuple1 neg = LongTuple.of(-5L);
         * neg.median();                                    // returns -5
         *
         * LongTuple.LongTuple1 zero = LongTuple.of(0L);
         * zero.median();                                   // returns 0
         * }</pre>
         *
         * @return the single element value
         */
        @Override
        public long median() {
            return _1;
        }

        /**
         * Returns the sum of all values in this tuple.
         * For a single-element tuple, this is the element itself.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple1 t = LongTuple.of(7L);
         * long s = t.sum();   // returns 7L
         *
         * LongTuple.LongTuple1 neg = LongTuple.of(-3L);
         * long sn = neg.sum();   // returns -3L
         *
         * LongTuple.LongTuple1 zero = LongTuple.of(0L);
         * long sz = zero.sum();   // returns 0L
         *
         * LongTuple.LongTuple1 boundary = LongTuple.of(Long.MIN_VALUE);
         * long sb = boundary.sum();   // returns Long.MIN_VALUE
         * }</pre>
         *
         * @return the single element value
         */
        @Override
        public long sum() {
            return _1;
        }

        /**
         * Returns the average of all values in this tuple.
         * For a single-element tuple, this is the element value converted to {@code double}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple1 t = LongTuple.of(7L);
         * double avg = t.average();   // returns 7.0
         *
         * LongTuple.LongTuple1 neg = LongTuple.of(-5L);
         * double avgNeg = neg.average();   // returns -5.0
         *
         * LongTuple.LongTuple1 zero = LongTuple.of(0L);
         * double avgZero = zero.average();   // returns 0.0
         *
         * LongTuple.LongTuple1 boundary = LongTuple.of(Long.MAX_VALUE);
         * double avgMax = boundary.average();   // returns (double) Long.MAX_VALUE
         * }</pre>
         *
         * @return {@code _1} converted to {@code double}
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
         * LongTuple.LongTuple1 t = LongTuple.of(42L);
         * LongTuple.LongTuple1 rev = t.reverse();   // new tuple; rev._1 == 42L
         *
         * // reversed tuple is a distinct object
         * LongTuple.LongTuple1 orig = LongTuple.of(1L);
         * LongTuple.LongTuple1 r = orig.reverse();   // r != orig, r._1 == 1L
         *
         * LongTuple.LongTuple1 neg = LongTuple.of(-99L);
         * LongTuple.LongTuple1 rn = neg.reverse();   // rn._1 == -99L
         *
         * LongTuple.LongTuple1 boundary = LongTuple.of(Long.MIN_VALUE);
         * LongTuple.LongTuple1 rb = boundary.reverse();   // rb._1 == Long.MIN_VALUE
         * }</pre>
         *
         * @return a new LongTuple.LongTuple1 with the same value
         */
        @Override
        public LongTuple1 reverse() {
            return new LongTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified long value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple1 t = LongTuple.of(5L);
         * boolean yes = t.contains(5L);   // returns true
         * boolean no  = t.contains(4L);   // returns false
         *
         * LongTuple.LongTuple1 neg = LongTuple.of(-9L);
         * boolean hasNeg = neg.contains(-9L);   // returns true
         * boolean hasMirror = neg.contains(9L); // returns false
         * }</pre>
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if the value equals _1, {@code false} otherwise
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind;
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple1 a = LongTuple.of(10L);
         * LongTuple.LongTuple1 b = LongTuple.of(10L);
         * int ha = a.hashCode();   // == Long.hashCode(10L)
         * int hb = b.hashCode();   // == ha (equal tuples have equal hash codes)
         *
         * LongTuple.LongTuple1 neg = LongTuple.of(-1L);
         * int hn = neg.hashCode();   // == Long.hashCode(-1L)
         *
         * LongTuple.LongTuple1 zero = LongTuple.of(0L);
         * int hz = zero.hashCode();   // == Long.hashCode(0L)
         * }</pre>
         *
         * @return the hash code of the single element
         */
        @Override
        public int hashCode() {
            return Long.hashCode(_1);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple1 a = LongTuple.of(10L);
         * LongTuple.LongTuple1 b = LongTuple.of(10L);
         * boolean eq = a.equals(b);    // returns true
         *
         * LongTuple.LongTuple1 c = LongTuple.of(20L);
         * boolean ne = a.equals(c);    // returns false
         *
         * boolean self = a.equals(a);  // returns true (same reference)
         *
         * boolean nul = a.equals(null); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is a LongTuple.LongTuple1 with the same element, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple1 other)) {
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
         * LongTuple.LongTuple1 t = LongTuple.of(42L);
         * String s = t.toString();   // returns "(42)"
         *
         * LongTuple.LongTuple1 neg = LongTuple.of(-1L);
         * String sn = neg.toString();   // returns "(-1)"
         *
         * LongTuple.LongTuple1 zero = LongTuple.of(0L);
         * String sz = zero.toString();   // returns "(0)"
         *
         * LongTuple.LongTuple1 big = LongTuple.of(Long.MAX_VALUE);
         * String sb = big.toString();   // returns "(9223372036854775807)"
         * }</pre>
         *
         * @return a string in the format {@code "(_1)"}
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
        }

        /**
         * Returns the internal array of long elements.
         * The array is lazily initialized on first access.
         *
         * @return a long array containing all elements of this tuple
         */
        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly two long values.
     * The values are accessible through the public final fields {@code _1} and {@code _2}.
     *
     * <p>In addition to the operations inherited from {@link LongTuple}, this class provides
     * functional helpers for working with pairs:</p>
     * <ul>
     *   <li>{@link #accept(Throwables.LongBiConsumer)} - consume both values</li>
     *   <li>{@link #map(Throwables.LongBiFunction)} - transform the pair to a single value</li>
     *   <li>{@link #filter(Throwables.LongBiPredicate)} - conditionally wrap in {@link Optional}</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
     * pair.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
     * long product = pair.map((a, b) -> a * b);
     * }</pre>
     *
     */
    public static final class LongTuple2 extends LongTuple<LongTuple2> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;

        LongTuple2() {
            this(0, 0);
        }

        LongTuple2(final long _1, final long _2) {
            this._1 = _1;
            this._2 = _2;
        }

        /**
         * Returns the number of elements in this tuple, which is always 2.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 t = LongTuple.of(10L, 20L);
         * int n = t.arity();   // returns 2
         *
         * LongTuple.LongTuple2 dup = LongTuple.of(5L, 5L);
         * int nd = dup.arity();   // returns 2
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
         * LongTuple.LongTuple2 t = LongTuple.of(1L, 2L);
         * long mn = t.min();   // returns 1L
         *
         * LongTuple.LongTuple2 neg = LongTuple.of(-5L, 3L);
         * long mnNeg = neg.min();   // returns -5L
         *
         * LongTuple.LongTuple2 dup = LongTuple.of(3L, 3L);
         * long mnDup = dup.min();   // returns 3L (duplicates)
         *
         * LongTuple.LongTuple2 boundary = LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE);
         * long mnB = boundary.min();   // returns Long.MIN_VALUE
         * }</pre>
         *
         * @return the smaller of _1 and _2
         */
        @Override
        public long min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum value among the two elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 t = LongTuple.of(1L, 2L);
         * long mx = t.max();   // returns 2L
         *
         * LongTuple.LongTuple2 neg = LongTuple.of(-5L, 3L);
         * long mxNeg = neg.max();   // returns 3L
         *
         * LongTuple.LongTuple2 dup = LongTuple.of(3L, 3L);
         * long mxDup = dup.max();   // returns 3L (duplicates)
         *
         * LongTuple.LongTuple2 boundary = LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE);
         * long mxB = boundary.max();   // returns Long.MAX_VALUE
         * }</pre>
         *
         * @return the larger of _1 and _2
         */
        @Override
        public long max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median value of the two elements.
         * Because this tuple has an even number of elements, this returns the lower of {@code _1} and {@code _2}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 t = LongTuple.of(1L, 2L);
         * long med = t.median();   // returns 1L (lower-middle of sorted [1, 2])
         *
         * LongTuple.LongTuple2 rev = LongTuple.of(5L, -3L);
         * long medRev = rev.median();   // returns -3L (lower-middle of sorted [-3, 5])
         *
         * LongTuple.LongTuple2 dup = LongTuple.of(4L, 4L);
         * long medDup = dup.median();   // returns 4L
         *
         * LongTuple.LongTuple2 boundary = LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE);
         * long medB = boundary.median();   // returns Long.MIN_VALUE (lower-middle)
         * }</pre>
         *
         * @return the lower of {@code _1} and {@code _2}
         */
        @Override
        public long median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of the two elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 t = LongTuple.of(1L, 2L);
         * long s = t.sum();   // returns 3L
         *
         * LongTuple.LongTuple2 cancel = LongTuple.of(-5L, 5L);
         * long sc = cancel.sum();   // returns 0L
         *
         * // overflow wraps silently (no exception)
         * LongTuple.LongTuple2 overflow = LongTuple.of(Long.MAX_VALUE, 1L);
         * long so = overflow.sum();   // returns Long.MIN_VALUE (wrap-around)
         *
         * LongTuple.LongTuple2 neg = LongTuple.of(-3L, -4L);
         * long sn = neg.sum();   // returns -7L
         * }</pre>
         *
         * @return _1 + _2 as a long
         */
        @Override
        public long sum() {
            return N.sum(_1, _2);
        }

        /**
         * Returns the average of the two elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 t = LongTuple.of(1L, 2L);
         * double avg = t.average();   // returns 1.5
         *
         * LongTuple.LongTuple2 cancel = LongTuple.of(-1L, 1L);
         * double avgC = cancel.average();   // returns 0.0
         *
         * LongTuple.LongTuple2 same = LongTuple.of(6L, 6L);
         * double avgS = same.average();   // returns 6.0
         *
         * LongTuple.LongTuple2 negPair = LongTuple.of(-10L, -4L);
         * double avgN = negPair.average();   // returns -7.0
         * }</pre>
         *
         * @return (_1 + _2) / 2.0 as a double
         */
        @Override
        public double average() {
            return averageOf(_1, _2);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 t = LongTuple.of(10L, 20L);
         * LongTuple.LongTuple2 rev = t.reverse();   // rev._1 == 20L, rev._2 == 10L
         *
         * // reversed tuple is a distinct object
         * LongTuple.LongTuple2 orig = LongTuple.of(1L, 2L);
         * LongTuple.LongTuple2 r = orig.reverse();   // r != orig
         *
         * LongTuple.LongTuple2 dup = LongTuple.of(5L, 5L);
         * LongTuple.LongTuple2 rd = dup.reverse();   // rd._1 == 5L, rd._2 == 5L
         *
         * LongTuple.LongTuple2 neg = LongTuple.of(-1L, 0L);
         * LongTuple.LongTuple2 rn = neg.reverse();   // rn._1 == 0L, rn._2 == -1L
         * }</pre>
         *
         * @return a new LongTuple.LongTuple2 with values (_2, _1)
         */
        @Override
        public LongTuple2 reverse() {
            return new LongTuple2(_2, _1);
        }

        /**
         * Checks if either element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 t = LongTuple.of(10L, 20L);
         * boolean has10 = t.contains(10L);   // returns true
         * boolean has20 = t.contains(20L);   // returns true
         * boolean has0  = t.contains(0L);    // returns false
         *
         * LongTuple.LongTuple2 dup = LongTuple.of(7L, 7L);
         * boolean hasDup = dup.contains(7L);   // returns true
         * boolean noVal  = dup.contains(6L);   // returns false
         * }</pre>
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals _1 or _2
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 t = LongTuple.of(10L, 20L);
         * long[] acc = {0L};
         * t.forEach(v -> acc[0] += v);   // acc[0] == 30L after call
         *
         * LongTuple.LongTuple2 neg = LongTuple.of(-3L, -7L);
         * long[] sum = {0L};
         * neg.forEach(v -> sum[0] += v);   // sum[0] == -10L after call
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> action) throws E {
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
         * LongTuple.LongTuple2 pair = LongTuple.of(3L, 4L);
         * long[] result = {0L};
         * pair.accept((a, b) -> result[0] = a + b);   // result[0] == 7L
         *
         * LongTuple.LongTuple2 coordinates = LongTuple.of(10L, 20L);
         * long[] out = {0L};
         * coordinates.accept((x, y) -> out[0] = x * y);   // out[0] == 200L
         *
         * // negative values
         * LongTuple.LongTuple2 neg = LongTuple.of(-5L, -3L);
         * long[] negOut = {0L};
         * neg.accept((a, b) -> negOut[0] = a - b);   // negOut[0] == -2L
         *
         * // duplicate values
         * LongTuple.LongTuple2 dup = LongTuple.of(7L, 7L);
         * long[] dupOut = {0L};
         * dup.accept((a, b) -> dupOut[0] = a + b);   // dupOut[0] == 14L
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the bi-consumer to perform on the two elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #map(Throwables.LongBiFunction)
         * @see #filter(Throwables.LongBiPredicate)
         */
        public <E extends Exception> void accept(final Throwables.LongBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given bi-function to the two elements and returns the result.
         * <p>
         * This method transforms the pair of long values into a single value of type U
         * using the provided mapper function. The mapper receives both _1 and _2 as arguments
         * and can return any type, including primitive wrapper types, objects, or null.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 pair = LongTuple.of(10L, 3L);
         * Long remainder = pair.map((a, b) -> a % b);   // returns 1L
         *
         * LongTuple.LongTuple2 dimensions = LongTuple.of(5L, 10L);
         * Long area = dimensions.map((width, height) -> width * height);   // returns 50L
         *
         * // negative operands
         * LongTuple.LongTuple2 neg = LongTuple.of(-6L, 2L);
         * Long quot = neg.map((a, b) -> a / b);   // returns -3L
         *
         * // mapper may return null
         * LongTuple.LongTuple2 zero = LongTuple.of(0L, 0L);
         * String nullResult = zero.map((a, b) -> a == b ? null : "different");   // returns null
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the bi-function to apply to the two elements, must not be {@code null}
         * @return the result of applying the mapper function, may be {@code null}
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.LongBiConsumer)
         * @see #filter(Throwables.LongBiPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.LongBiFunction<U, E> mapper) throws E {
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
         * LongTuple.LongTuple2 pair = LongTuple.of(10L, 20L);
         * Optional<LongTuple.LongTuple2> present = pair.filter((a, b) -> a < b);   // Optional containing the tuple
         *
         * LongTuple.LongTuple2 values = LongTuple.of(5L, 3L);
         * Optional<LongTuple.LongTuple2> empty = values.filter((a, b) -> a < b);   // empty Optional
         *
         * // negative values: -5 < 0 is true
         * LongTuple.LongTuple2 neg = LongTuple.of(-5L, 0L);
         * Optional<LongTuple.LongTuple2> negPresent = neg.filter((a, b) -> a < b);   // Optional containing the tuple
         *
         * // boundary: both equal Long.MAX_VALUE -> not strictly less
         * LongTuple.LongTuple2 boundary = LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE);
         * Optional<LongTuple.LongTuple2> boundEmpty = boundary.filter((a, b) -> a < b);   // empty Optional
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the bi-predicate to test the two elements, must not be {@code null}
         * @return an {@code Optional} containing this tuple if the predicate returns {@code true}, an empty {@code Optional} otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception
         * @see #accept(Throwables.LongBiConsumer)
         * @see #map(Throwables.LongBiFunction)
         */
        public <E extends Exception> Optional<LongTuple2> filter(final Throwables.LongBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 a = LongTuple.of(3L, 4L);
         * LongTuple.LongTuple2 b = LongTuple.of(3L, 4L);
         * int ha = a.hashCode();   // == 31 * Long.hashCode(3L) + Long.hashCode(4L)
         * int hb = b.hashCode();   // == ha (equal tuples have equal hash codes)
         *
         * // order matters: (3, 4) and (4, 3) have different hash codes
         * LongTuple.LongTuple2 c = LongTuple.of(4L, 3L);
         * int hc = c.hashCode();   // != ha
         *
         * LongTuple.LongTuple2 neg = LongTuple.of(-1L, -2L);
         * int hn = neg.hashCode();   // == 31 * Long.hashCode(-1L) + Long.hashCode(-2L)
         * }</pre>
         *
         * @return 31 * Long.hashCode(_1) + Long.hashCode(_2)
         */
        @Override
        public int hashCode() {
            return 31 * Long.hashCode(_1) + Long.hashCode(_2);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple2 a = LongTuple.of(3L, 4L);
         * LongTuple.LongTuple2 b = LongTuple.of(3L, 4L);
         * boolean eq = a.equals(b);   // returns true
         *
         * // order matters
         * LongTuple.LongTuple2 c = LongTuple.of(4L, 3L);
         * boolean ne = a.equals(c);   // returns false
         *
         * boolean nul = a.equals(null);   // returns false
         *
         * boolean self = a.equals(a);   // returns true (same reference)
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is a LongTuple.LongTuple2 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple2 other)) {
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
         * LongTuple.LongTuple2 t = LongTuple.of(1L, 2L);
         * String s = t.toString();   // returns "(1, 2)"
         *
         * LongTuple.LongTuple2 neg = LongTuple.of(-1L, 0L);
         * String sn = neg.toString();   // returns "(-1, 0)"
         *
         * LongTuple.LongTuple2 big = LongTuple.of(Long.MAX_VALUE, Long.MIN_VALUE);
         * String sb = big.toString();   // returns "(9223372036854775807, -9223372036854775808)"
         *
         * LongTuple.LongTuple2 dup = LongTuple.of(5L, 5L);
         * String sd = dup.toString();   // returns "(5, 5)"
         * }</pre>
         *
         * @return a string in the format {@code "(_1, _2)"}
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
        }

        /**
         * Returns the internal array of long elements.
         * The array is lazily initialized on first access.
         *
         * @return a long array containing all elements of this tuple
         */
        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly three long values.
     * The values are accessible through the public final fields {@code _1}, {@code _2}, and {@code _3}.
     *
     * <p>In addition to the operations inherited from {@link LongTuple}, this class provides
     * functional helpers for working with triples:</p>
     * <ul>
     *   <li>{@link #accept(Throwables.LongTriConsumer)} - consume all three values</li>
     *   <li>{@link #map(Throwables.LongTriFunction)} - transform the triple to a single value</li>
     *   <li>{@link #filter(Throwables.LongTriPredicate)} - conditionally wrap in {@link Optional}</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
     * triple.accept((a, b, c) -> System.out.println("Sum: " + (a + b + c)));
     * long product = triple.map((a, b, c) -> a * b * c);
     * }</pre>
     *
     */
    public static final class LongTuple3 extends LongTuple<LongTuple3> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;

        LongTuple3() {
            this(0, 0, 0);
        }

        LongTuple3(final long _1, final long _2, final long _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        /**
         * Returns the number of elements in this tuple, which is always 3.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 t = LongTuple.of(1L, 2L, 3L);
         * int n = t.arity();   // returns 3
         *
         * LongTuple.LongTuple3 all = LongTuple.of(-1L, 0L, 1L);
         * int na = all.arity();   // returns 3
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
         * LongTuple.LongTuple3 t = LongTuple.of(3L, 1L, 2L);
         * long mn = t.min();   // returns 1L
         *
         * LongTuple.LongTuple3 neg = LongTuple.of(-9L, 0L, 5L);
         * long mnN = neg.min();   // returns -9L
         *
         * LongTuple.LongTuple3 dup = LongTuple.of(4L, 4L, 4L);
         * long mnD = dup.min();   // returns 4L (all same)
         *
         * LongTuple.LongTuple3 boundary = LongTuple.of(Long.MIN_VALUE, 0L, Long.MAX_VALUE);
         * long mnB = boundary.min();   // returns Long.MIN_VALUE
         * }</pre>
         *
         * @return the smallest of _1, _2, and _3
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum value among the three elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 t = LongTuple.of(3L, 1L, 2L);
         * long mx = t.max();   // returns 3L
         *
         * LongTuple.LongTuple3 neg = LongTuple.of(-9L, 0L, 5L);
         * long mxN = neg.max();   // returns 5L
         *
         * LongTuple.LongTuple3 dup = LongTuple.of(4L, 4L, 4L);
         * long mxD = dup.max();   // returns 4L (all same)
         *
         * LongTuple.LongTuple3 boundary = LongTuple.of(Long.MIN_VALUE, 0L, Long.MAX_VALUE);
         * long mxB = boundary.max();   // returns Long.MAX_VALUE
         * }</pre>
         *
         * @return the largest of _1, _2, and _3
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median value of the three elements.
         * Returns the middle value of {@code _1}, {@code _2}, and {@code _3} when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 t = LongTuple.of(3L, 1L, 2L);
         * long med = t.median();   // returns 2L (middle of sorted [1, 2, 3])
         *
         * LongTuple.LongTuple3 neg = LongTuple.of(-9L, 0L, 5L);
         * long medN = neg.median();   // returns 0L (middle of sorted [-9, 0, 5])
         *
         * LongTuple.LongTuple3 dup = LongTuple.of(7L, 7L, 7L);
         * long medD = dup.median();   // returns 7L (all same)
         *
         * LongTuple.LongTuple3 sorted = LongTuple.of(1L, 2L, 3L);
         * long medS = sorted.median();   // returns 2L
         * }</pre>
         *
         * @return the middle long value when sorted
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of all three elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 t = LongTuple.of(1L, 2L, 3L);
         * long s = t.sum();   // returns 6L
         *
         * LongTuple.LongTuple3 cancel = LongTuple.of(-1L, 0L, 1L);
         * long sc = cancel.sum();   // returns 0L
         *
         * // overflow wraps silently (no exception)
         * LongTuple.LongTuple3 overflow = LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE, 2L);
         * long so = overflow.sum();   // wraps: Long.MAX_VALUE + Long.MAX_VALUE + 2L
         *
         * LongTuple.LongTuple3 neg = LongTuple.of(-1L, -2L, -3L);
         * long sn = neg.sum();   // returns -6L
         * }</pre>
         *
         * @return _1 + _2 + _3 as a long
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3);
        }

        /**
         * Returns the average of all three elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 t = LongTuple.of(1L, 2L, 3L);
         * double avg = t.average();   // returns 2.0
         *
         * LongTuple.LongTuple3 cancel = LongTuple.of(-1L, 0L, 1L);
         * double avgC = cancel.average();   // returns 0.0
         *
         * LongTuple.LongTuple3 same = LongTuple.of(6L, 6L, 6L);
         * double avgS = same.average();   // returns 6.0
         *
         * LongTuple.LongTuple3 negTri = LongTuple.of(-3L, -6L, -9L);
         * double avgN = negTri.average();   // returns -6.0
         * }</pre>
         *
         * @return (_1 + _2 + _3) / 3.0 as a double
         */
        @Override
        public double average() {
            return averageOf(_1, _2, _3);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 t = LongTuple.of(1L, 2L, 3L);
         * LongTuple.LongTuple3 rev = t.reverse();   // rev._1==3L, rev._2==2L, rev._3==1L
         *
         * // reversed tuple is a distinct object
         * LongTuple.LongTuple3 orig = LongTuple.of(10L, 20L, 30L);
         * LongTuple.LongTuple3 r = orig.reverse();   // r != orig
         *
         * LongTuple.LongTuple3 dup = LongTuple.of(5L, 5L, 5L);
         * LongTuple.LongTuple3 rd = dup.reverse();   // rd._1==5L, rd._2==5L, rd._3==5L
         *
         * LongTuple.LongTuple3 neg = LongTuple.of(-1L, 0L, 1L);
         * LongTuple.LongTuple3 rn = neg.reverse();   // rn._1==1L, rn._2==0L, rn._3==-1L
         * }</pre>
         *
         * @return a new LongTuple.LongTuple3 with values (_3, _2, _1)
         */
        @Override
        public LongTuple3 reverse() {
            return new LongTuple3(_3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 t = LongTuple.of(1L, 2L, 3L);
         * boolean has1 = t.contains(1L);   // returns true
         * boolean has3 = t.contains(3L);   // returns true
         * boolean has4 = t.contains(4L);   // returns false
         *
         * LongTuple.LongTuple3 dup = LongTuple.of(5L, 5L, 5L);
         * boolean hasDup = dup.contains(5L);   // returns true
         * boolean noVal  = dup.contains(0L);   // returns false
         * }</pre>
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals _1, _2, or _3
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 t = LongTuple.of(1L, 2L, 3L);
         * long[] acc = {0L};
         * t.forEach(v -> acc[0] += v);   // acc[0] == 6L after call
         *
         * LongTuple.LongTuple3 neg = LongTuple.of(-1L, -2L, -3L);
         * long[] sum = {0L};
         * neg.forEach(v -> sum[0] += v);   // sum[0] == -6L after call
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> action) throws E {
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
         * LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
         * long[] result = {0L};
         * triple.accept((a, b, c) -> result[0] = a + b + c);   // result[0] == 6L
         *
         * LongTuple.LongTuple3 coords = LongTuple.of(2L, 3L, 4L);
         * long[] vol = {0L};
         * coords.accept((l, w, h) -> vol[0] = l * w * h);   // vol[0] == 24L
         *
         * // negative values
         * LongTuple.LongTuple3 neg = LongTuple.of(-1L, -2L, -3L);
         * long[] negOut = {0L};
         * neg.accept((a, b, c) -> negOut[0] = a + b + c);   // negOut[0] == -6L
         *
         * // duplicate values
         * LongTuple.LongTuple3 dup = LongTuple.of(5L, 5L, 5L);
         * long[] dupOut = {0L};
         * dup.accept((a, b, c) -> dupOut[0] = a + b + c);   // dupOut[0] == 15L
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the tri-consumer to perform on the three elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #map(Throwables.LongTriFunction)
         * @see #filter(Throwables.LongTriPredicate)
         */
        public <E extends Exception> void accept(final Throwables.LongTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given tri-function to the three elements and returns the result.
         * <p>
         * This method transforms the three long values into a single value of type U
         * using the provided mapper function. The mapper receives all three elements
         * (_1, _2, and _3) as arguments and can return any type, including primitive
         * wrapper types, objects, or null.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 triple = LongTuple.of(2L, 3L, 4L);
         * Long volume = triple.map((l, w, h) -> l * w * h);   // returns 24L
         *
         * LongTuple.LongTuple3 values = LongTuple.of(1L, 2L, 3L);
         * Double avg = values.map((a, b, c) -> (a + b + c) / 3.0);   // returns 2.0
         *
         * // negative operands
         * LongTuple.LongTuple3 neg = LongTuple.of(-1L, -2L, -3L);
         * Long negSum = neg.map((a, b, c) -> a + b + c);   // returns -6L
         *
         * // mapper may return null
         * LongTuple.LongTuple3 zero = LongTuple.of(0L, 0L, 0L);
         * String nullResult = zero.map((a, b, c) -> a == 0 && b == 0 && c == 0 ? null : "non-zero");   // returns null
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the tri-function to apply to the three elements, must not be {@code null}
         * @return the result of applying the mapper function, may be {@code null}
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.LongTriConsumer)
         * @see #filter(Throwables.LongTriPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.LongTriFunction<U, E> mapper) throws E {
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
         * LongTuple.LongTuple3 triple = LongTuple.of(1L, 2L, 3L);
         * Optional<LongTuple.LongTuple3> present = triple.filter((a, b, c) -> a < b && b < c);   // Optional containing the tuple
         *
         * LongTuple.LongTuple3 descending = LongTuple.of(5L, 4L, 3L);
         * Optional<LongTuple.LongTuple3> empty = descending.filter((a, b, c) -> a < b && b < c);   // empty Optional
         *
         * // negative values: all negative, sum < 0 is true
         * LongTuple.LongTuple3 neg = LongTuple.of(-3L, -2L, -1L);
         * Optional<LongTuple.LongTuple3> negPresent = neg.filter((a, b, c) -> a + b + c < 0);   // Optional containing the tuple
         *
         * // duplicate values: a == b == c is true only when all same
         * LongTuple.LongTuple3 dup = LongTuple.of(7L, 7L, 7L);
         * Optional<LongTuple.LongTuple3> dupPresent = dup.filter((a, b, c) -> a == b && b == c);   // Optional containing the tuple
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the tri-predicate to test the three elements, must not be {@code null}
         * @return an {@code Optional} containing this tuple if the predicate returns {@code true}, an empty {@code Optional} otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception
         * @see #accept(Throwables.LongTriConsumer)
         * @see #map(Throwables.LongTriFunction)
         */
        public <E extends Exception> Optional<LongTuple3> filter(final Throwables.LongTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 a = LongTuple.of(1L, 2L, 3L);
         * LongTuple.LongTuple3 b = LongTuple.of(1L, 2L, 3L);
         * int ha = a.hashCode();   // == (31 * (31 * Long.hashCode(1L) + Long.hashCode(2L))) + Long.hashCode(3L)
         * int hb = b.hashCode();   // == ha (equal tuples have equal hash codes)
         *
         * // order matters: (1, 2, 3) and (3, 2, 1) have different hash codes
         * LongTuple.LongTuple3 c = LongTuple.of(3L, 2L, 1L);
         * int hc = c.hashCode();   // != ha
         *
         * LongTuple.LongTuple3 neg = LongTuple.of(-1L, 0L, 1L);
         * int hn = neg.hashCode();   // consistent with equals
         * }</pre>
         *
         * @return (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2))) + Long.hashCode(_3)
         */
        @Override
        public int hashCode() {
            return (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2))) + Long.hashCode(_3);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple3 a = LongTuple.of(1L, 2L, 3L);
         * LongTuple.LongTuple3 b = LongTuple.of(1L, 2L, 3L);
         * boolean eq = a.equals(b);   // returns true
         *
         * // order matters
         * LongTuple.LongTuple3 c = LongTuple.of(3L, 2L, 1L);
         * boolean ne = a.equals(c);   // returns false
         *
         * boolean nul = a.equals(null);   // returns false
         *
         * boolean self = a.equals(a);   // returns true (same reference)
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if the object is a LongTuple.LongTuple3 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple3 other)) {
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
         * LongTuple.LongTuple3 t = LongTuple.of(1L, 2L, 3L);
         * String s = t.toString();   // returns "(1, 2, 3)"
         *
         * LongTuple.LongTuple3 neg = LongTuple.of(-1L, 0L, 1L);
         * String sn = neg.toString();   // returns "(-1, 0, 1)"
         *
         * LongTuple.LongTuple3 dup = LongTuple.of(5L, 5L, 5L);
         * String sd = dup.toString();   // returns "(5, 5, 5)"
         *
         * LongTuple.LongTuple3 boundary = LongTuple.of(Long.MIN_VALUE, 0L, Long.MAX_VALUE);
         * String sb = boundary.toString();   // returns "(-9223372036854775808, 0, 9223372036854775807)"
         * }</pre>
         *
         * @return a string in the format {@code "(_1, _2, _3)"}
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
        }

        /**
         * Returns the internal array of long elements.
         * The array is lazily initialized on first access.
         *
         * @return a long array containing all elements of this tuple
         */
        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly four long values.
     * The values are accessible through the public final fields {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple4 quad = LongTuple.of(1L, 2L, 3L, 4L);
     * long sum = quad.sum();                            // 10L
     * long min = quad.min();                            // 1L
     * LongTuple.LongTuple4 reversed = quad.reverse();   // (4, 3, 2, 1)
     * }</pre>
     *
     */
    public static final class LongTuple4 extends LongTuple<LongTuple4> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;

        LongTuple4() {
            this(0, 0, 0, 0);
        }

        LongTuple4(final long _1, final long _2, final long _3, final long _4) {
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
         * LongTuple.LongTuple4 t = LongTuple.of(10L, 20L, 30L, 40L);
         * int n = t.arity();   // 4
         *
         * LongTuple.LongTuple4 neg = LongTuple.of(-1L, -2L, -3L, -4L);
         * int n2 = neg.arity();   // 4
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
         * LongTuple.LongTuple4 t = LongTuple.of(3L, 1L, 4L, 2L);
         * long min = t.min();   // 1
         *
         * LongTuple.LongTuple4 neg = LongTuple.of(-5L, -3L, -10L, -1L);
         * long minNeg = neg.min();   // -10
         *
         * LongTuple.LongTuple4 mixed = LongTuple.of(Long.MIN_VALUE, 0L, Long.MAX_VALUE, 1L);
         * long minBound = mixed.min();   // Long.MIN_VALUE
         *
         * LongTuple.LongTuple4 dup = LongTuple.of(7L, 7L, 7L, 7L);
         * long minDup = dup.min();   // 7
         * }</pre>
         *
         * @return the smallest of _1, _2, _3, and _4
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4);
        }

        /**
         * Returns the maximum value among the four elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple4 t = LongTuple.of(3L, 1L, 4L, 2L);
         * long max = t.max();   // 4
         *
         * LongTuple.LongTuple4 neg = LongTuple.of(-5L, -3L, -10L, -1L);
         * long maxNeg = neg.max();   // -1
         *
         * LongTuple.LongTuple4 mixed = LongTuple.of(Long.MIN_VALUE, 0L, Long.MAX_VALUE, 1L);
         * long maxBound = mixed.max();   // Long.MAX_VALUE
         *
         * LongTuple.LongTuple4 dup = LongTuple.of(7L, 7L, 7L, 7L);
         * long maxDup = dup.max();   // 7
         * }</pre>
         *
         * @return the largest of _1, _2, _3, and _4
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the median value of the four elements.
         * Because this tuple has an even number of elements, this returns the lower of the two middle values when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple4 t = LongTuple.of(1L, 2L, 3L, 4L);
         * long med = t.median();   // 2  (sorted=[1,2,3,4], lower middle=2)
         *
         * LongTuple.LongTuple4 rev = LongTuple.of(4L, 3L, 2L, 1L);
         * long medRev = rev.median();   // 2  (same elements, same result)
         *
         * LongTuple.LongTuple4 neg = LongTuple.of(-3L, -1L, 0L, 2L);
         * long medNeg = neg.median();   // -1  (sorted=[-3,-1,0,2], lower middle=-1)
         *
         * LongTuple.LongTuple4 dup = LongTuple.of(5L, 5L, 5L, 5L);
         * long medDup = dup.median();   // 5
         * }</pre>
         *
         * @return the lower-middle long value of the four elements
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of all four elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple4 t = LongTuple.of(1L, 2L, 3L, 4L);
         * long sum = t.sum();   // 10
         *
         * LongTuple.LongTuple4 neg = LongTuple.of(-1L, -2L, -3L, -4L);
         * long sumNeg = neg.sum();   // -10
         *
         * // Sum wraps silently on overflow - no exception is thrown
         * LongTuple.LongTuple4 overflow = LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE, 0L, 0L);
         * long wrapped = overflow.sum();   // -2  (two's-complement wrap-around)
         *
         * LongTuple.LongTuple4 zeros = LongTuple.of(0L, 0L, 0L, 0L);
         * long zeroSum = zeros.sum();   // 0
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 as a long
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3, _4);
        }

        /**
         * Returns the average of all four elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple4 t = LongTuple.of(1L, 2L, 3L, 4L);
         * double avg = t.average();   // 2.5
         *
         * LongTuple.LongTuple4 neg = LongTuple.of(-3L, -1L, 0L, 2L);
         * double avgNeg = neg.average();   // -0.5
         *
         * LongTuple.LongTuple4 zeros = LongTuple.of(0L, 0L, 0L, 0L);
         * double avgZero = zeros.average();   // 0.0
         *
         * LongTuple.LongTuple4 dup = LongTuple.of(6L, 6L, 6L, 6L);
         * double avgDup = dup.average();   // 6.0
         * }</pre>
         *
         * @return (_1 + _2 + _3 + _4) / 4.0 as a double
         */
        @Override
        public double average() {
            return averageOf(_1, _2, _3, _4);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple4 t = LongTuple.of(1L, 2L, 3L, 4L);
         * LongTuple.LongTuple4 rev = t.reverse();
         * // rev._1==4, rev._2==3, rev._3==2, rev._4==1
         * // rev.toString() returns "(4, 3, 2, 1)"
         *
         * LongTuple.LongTuple4 neg = LongTuple.of(-4L, -3L, -2L, -1L);
         * LongTuple.LongTuple4 revNeg = neg.reverse();
         * // revNeg.toString() returns "(-1, -2, -3, -4)"
         *
         * // Reversing a uniform tuple yields the same values
         * LongTuple.LongTuple4 dup = LongTuple.of(7L, 7L, 7L, 7L);
         * LongTuple.LongTuple4 revDup = dup.reverse();
         * // revDup.toString() returns "(7, 7, 7, 7)"
         *
         * // reverse() returns a new instance, original is unchanged
         * LongTuple.LongTuple4 orig = LongTuple.of(10L, 20L, 30L, 40L);
         * LongTuple.LongTuple4 reversed = orig.reverse();
         * boolean notSame = orig != reversed;   // true
         * }</pre>
         *
         * @return a new LongTuple.LongTuple4 with values (_4, _3, _2, _1)
         */
        @Override
        public LongTuple4 reverse() {
            return new LongTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple4 t = LongTuple.of(1L, 2L, 3L, 4L);
         * boolean has3 = t.contains(3L);   // true
         * boolean has9 = t.contains(9L);   // false
         *
         * // Negative values
         * LongTuple.LongTuple4 neg = LongTuple.of(-4L, -3L, -2L, -1L);
         * boolean hasNeg3 = neg.contains(-3L);   // true
         * boolean hasZero = neg.contains(0L);    // false
         *
         * // Boundary values
         * LongTuple.LongTuple4 bounds = LongTuple.of(Long.MIN_VALUE, 0L, Long.MAX_VALUE, 1L);
         * boolean hasMin = bounds.contains(Long.MIN_VALUE);   // true
         * boolean hasMax = bounds.contains(Long.MAX_VALUE);   // true
         * }</pre>
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals any of the four elements
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple4 t = LongTuple.of(10L, 20L, 30L, 40L);
         * long[] collected = new long[4];
         * int[] idx = {0};
         * t.forEach(v -> collected[idx[0]++] = v);
         * // collected == [10, 20, 30, 40]
         *
         * // Negative values visited in order
         * LongTuple.LongTuple4 neg = LongTuple.of(-4L, -3L, -2L, -1L);
         * java.util.concurrent.atomic.AtomicLong sum = new java.util.concurrent.atomic.AtomicLong();
         * neg.forEach(sum::addAndGet);  // sum.get() == -10
         *
         * // Passing null throws IllegalArgumentException
         * LongTuple.LongTuple4 t2 = LongTuple.of(1L, 2L, 3L, 4L);
         * // t2.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> action) throws E {
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
         * LongTuple.LongTuple4 t1 = LongTuple.of(1L, 2L, 3L, 4L);
         * LongTuple.LongTuple4 t2 = LongTuple.of(1L, 2L, 3L, 4L);
         * boolean sameHash = t1.hashCode() == t2.hashCode();   // true (equal tuples have same hash)
         *
         * LongTuple.LongTuple4 t3 = LongTuple.of(4L, 3L, 2L, 1L);
         * boolean diffHash = t1.hashCode() != t3.hashCode();   // true (different element order)
         * }</pre>
         *
         * @return a hash code based on all four elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3))) + Long.hashCode(_4);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple4 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple4 t1 = LongTuple.of(1L, 2L, 3L, 4L);
         * LongTuple.LongTuple4 t2 = LongTuple.of(1L, 2L, 3L, 4L);
         * boolean eq = t1.equals(t2);   // true
         *
         * LongTuple.LongTuple4 t3 = LongTuple.of(4L, 3L, 2L, 1L);
         * boolean neq = t1.equals(t3);   // false (different order)
         *
         * // Different arity - never equal
         * LongTuple.LongTuple3 shorter = LongTuple.of(1L, 2L, 3L);
         * boolean diffArity = t1.equals(shorter);   // false
         *
         * boolean notNull = t1.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple4 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple4 other)) {
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
         * LongTuple.LongTuple4 t = LongTuple.of(1L, 2L, 3L, 4L);
         * String s = t.toString();   // "(1, 2, 3, 4)"
         *
         * LongTuple.LongTuple4 neg = LongTuple.of(-4L, -3L, -2L, -1L);
         * String sNeg = neg.toString();   // "(-4, -3, -2, -1)"
         *
         * LongTuple.LongTuple4 zeros = LongTuple.of(0L, 0L, 0L, 0L);
         * String sZero = zeros.toString();   // "(0, 0, 0, 0)"
         *
         * LongTuple.LongTuple4 mixed = LongTuple.of(Long.MIN_VALUE, -1L, 0L, Long.MAX_VALUE);
         * String sMixed = mixed.toString();   // "(-9223372036854775808, -1, 0, 9223372036854775807)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
        }

        /**
         * Returns the internal array of long elements.
         * The array is lazily initialized on first access.
         *
         * @return a long array containing all elements of this tuple
         */
        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly five long values.
     * The values are accessible through the public final fields {@code _1} through {@code _5}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple5 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L);
     * double avg = tuple.average();                      // 3.0
     * long median = tuple.median();                      // 3
     * LongTuple.LongTuple5 reversed = tuple.reverse();   // (5, 4, 3, 2, 1)
     * }</pre>
     *
     */
    public static final class LongTuple5 extends LongTuple<LongTuple5> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;
        /** The fifth long value in this tuple. */
        public final long _5;

        LongTuple5() {
            this(0, 0, 0, 0, 0);
        }

        LongTuple5(final long _1, final long _2, final long _3, final long _4, final long _5) {
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
         * LongTuple.LongTuple5 t = LongTuple.of(10L, 20L, 30L, 40L, 50L);
         * int n = t.arity();   // 5
         *
         * LongTuple.LongTuple5 neg = LongTuple.of(-1L, -2L, -3L, -4L, -5L);
         * int n2 = neg.arity();   // 5
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
         * LongTuple.LongTuple5 t = LongTuple.of(3L, 1L, 4L, 1L, 5L);
         * long min = t.min();   // 1
         *
         * LongTuple.LongTuple5 neg = LongTuple.of(-5L, -3L, -10L, -1L, -7L);
         * long minNeg = neg.min();   // -10
         *
         * LongTuple.LongTuple5 bounds = LongTuple.of(Long.MIN_VALUE, 0L, 1L, 2L, Long.MAX_VALUE);
         * long minBound = bounds.min();   // Long.MIN_VALUE
         *
         * LongTuple.LongTuple5 dup = LongTuple.of(9L, 9L, 9L, 9L, 9L);
         * long minDup = dup.min();   // 9
         * }</pre>
         *
         * @return the smallest of _1, _2, _3, _4, and _5
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the maximum value among the five elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple5 t = LongTuple.of(3L, 1L, 4L, 1L, 5L);
         * long max = t.max();   // 5
         *
         * LongTuple.LongTuple5 neg = LongTuple.of(-5L, -3L, -10L, -1L, -7L);
         * long maxNeg = neg.max();   // -1
         *
         * LongTuple.LongTuple5 bounds = LongTuple.of(Long.MIN_VALUE, 0L, 1L, 2L, Long.MAX_VALUE);
         * long maxBound = bounds.max();   // Long.MAX_VALUE
         *
         * LongTuple.LongTuple5 dup = LongTuple.of(9L, 9L, 9L, 9L, 9L);
         * long maxDup = dup.max();   // 9
         * }</pre>
         *
         * @return the largest of _1, _2, _3, _4, and _5
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the median value of the five elements.
         * Returns the middle value of the five elements when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple5 t = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * long med = t.median();   // 3  (middle of sorted [1,2,3,4,5])
         *
         * LongTuple.LongTuple5 rev = LongTuple.of(5L, 4L, 3L, 2L, 1L);
         * long medRev = rev.median();   // 3  (order of input does not matter)
         *
         * LongTuple.LongTuple5 neg = LongTuple.of(-5L, -2L, 0L, 3L, 7L);
         * long medNeg = neg.median();   // 0  (middle of sorted [-5,-2,0,3,7])
         *
         * LongTuple.LongTuple5 dup = LongTuple.of(4L, 4L, 4L, 4L, 4L);
         * long medDup = dup.median();   // 4
         * }</pre>
         *
         * @return the middle long value when sorted
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of all five elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple5 t = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * long sum = t.sum();   // 15
         *
         * LongTuple.LongTuple5 neg = LongTuple.of(-1L, -2L, -3L, -4L, -5L);
         * long sumNeg = neg.sum();   // -15
         *
         * // Sum wraps silently on overflow - no exception is thrown
         * LongTuple.LongTuple5 overflow = LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE, 0L, 0L, 0L);
         * long wrapped = overflow.sum();   // -2  (two's-complement wrap-around)
         *
         * LongTuple.LongTuple5 zeros = LongTuple.of(0L, 0L, 0L, 0L, 0L);
         * long zeroSum = zeros.sum();   // 0
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 + _5 as a long
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the average of all five elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple5 t = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * double avg = t.average();   // 3.0
         *
         * LongTuple.LongTuple5 neg = LongTuple.of(-5L, -2L, 0L, 3L, 7L);
         * double avgNeg = neg.average();   // 0.6
         *
         * LongTuple.LongTuple5 zeros = LongTuple.of(0L, 0L, 0L, 0L, 0L);
         * double avgZero = zeros.average();   // 0.0
         *
         * LongTuple.LongTuple5 dup = LongTuple.of(8L, 8L, 8L, 8L, 8L);
         * double avgDup = dup.average();   // 8.0
         * }</pre>
         *
         * @return (_1 + _2 + _3 + _4 + _5) / 5.0 as a double
         */
        @Override
        public double average() {
            return averageOf(_1, _2, _3, _4, _5);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple5 t = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * LongTuple.LongTuple5 rev = t.reverse();
         * // rev.toString() returns "(5, 4, 3, 2, 1)"
         *
         * LongTuple.LongTuple5 neg = LongTuple.of(-5L, -4L, -3L, -2L, -1L);
         * LongTuple.LongTuple5 revNeg = neg.reverse();
         * // revNeg.toString() returns "(-1, -2, -3, -4, -5)"
         *
         * // Reversing a uniform tuple yields the same values
         * LongTuple.LongTuple5 dup = LongTuple.of(6L, 6L, 6L, 6L, 6L);
         * LongTuple.LongTuple5 revDup = dup.reverse();
         * // revDup.toString() returns "(6, 6, 6, 6, 6)"
         *
         * // reverse() returns a new instance, original is unchanged
         * LongTuple.LongTuple5 orig = LongTuple.of(10L, 20L, 30L, 40L, 50L);
         * LongTuple.LongTuple5 reversed = orig.reverse();
         * boolean notSame = orig != reversed;   // true
         * }</pre>
         *
         * @return a new LongTuple.LongTuple5 with values (_5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple5 reverse() {
            return new LongTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple5 t = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * boolean has3 = t.contains(3L);   // true
         * boolean has9 = t.contains(9L);   // false
         *
         * // Negative values
         * LongTuple.LongTuple5 neg = LongTuple.of(-5L, -4L, -3L, -2L, -1L);
         * boolean hasNeg3 = neg.contains(-3L);   // true
         * boolean hasZero = neg.contains(0L);    // false
         *
         * // Duplicate elements - found if any match
         * LongTuple.LongTuple5 dup = LongTuple.of(7L, 7L, 7L, 7L, 7L);
         * boolean hasSeven = dup.contains(7L);   // true
         * boolean hasEight = dup.contains(8L);   // false
         * }</pre>
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals any of the five elements
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple5 t = LongTuple.of(10L, 20L, 30L, 40L, 50L);
         * long[] collected = new long[5];
         * int[] idx = {0};
         * t.forEach(v -> collected[idx[0]++] = v);
         * // collected == [10, 20, 30, 40, 50]
         *
         * // Accumulate sum over negative values
         * LongTuple.LongTuple5 neg = LongTuple.of(-5L, -4L, -3L, -2L, -1L);
         * java.util.concurrent.atomic.AtomicLong sum = new java.util.concurrent.atomic.AtomicLong();
         * neg.forEach(sum::addAndGet);  // sum.get() == -15
         *
         * // Passing null throws IllegalArgumentException
         * LongTuple.LongTuple5 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * // t2.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> action) throws E {
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
         * LongTuple.LongTuple5 t1 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * LongTuple.LongTuple5 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * boolean sameHash = t1.hashCode() == t2.hashCode();   // true (equal tuples have same hash)
         *
         * LongTuple.LongTuple5 t3 = LongTuple.of(5L, 4L, 3L, 2L, 1L);
         * boolean diffHash = t1.hashCode() != t3.hashCode();   // true (different element order)
         * }</pre>
         *
         * @return a hash code based on all five elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3)) + Long.hashCode(_4))) + Long.hashCode(_5);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple5 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple5 t1 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * LongTuple.LongTuple5 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * boolean eq = t1.equals(t2);   // true
         *
         * LongTuple.LongTuple5 t3 = LongTuple.of(5L, 4L, 3L, 2L, 1L);
         * boolean neq = t1.equals(t3);   // false (different order)
         *
         * // Different arity - never equal
         * LongTuple.LongTuple4 shorter = LongTuple.of(1L, 2L, 3L, 4L);
         * boolean diffArity = t1.equals(shorter);   // false
         *
         * boolean notNull = t1.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple5 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple5 other)) {
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
         * LongTuple.LongTuple5 t = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * String s = t.toString();   // "(1, 2, 3, 4, 5)"
         *
         * LongTuple.LongTuple5 neg = LongTuple.of(-5L, -4L, -3L, -2L, -1L);
         * String sNeg = neg.toString();   // "(-5, -4, -3, -2, -1)"
         *
         * LongTuple.LongTuple5 zeros = LongTuple.of(0L, 0L, 0L, 0L, 0L);
         * String sZero = zeros.toString();   // "(0, 0, 0, 0, 0)"
         *
         * LongTuple.LongTuple5 mixed = LongTuple.of(Long.MIN_VALUE, -1L, 0L, 1L, Long.MAX_VALUE);
         * String sMixed = mixed.toString();   // "(-9223372036854775808, -1, 0, 1, 9223372036854775807)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ")";
        }

        /**
         * Returns the internal array of long elements.
         * The array is lazily initialized on first access.
         *
         * @return a long array containing all elements of this tuple
         */
        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly six long values.
     * The values are accessible through the public final fields {@code _1} through {@code _6}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple6 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
     * long sum = tuple.sum();         // 21
     * double avg = tuple.average();   // 3.5
     * }</pre>
     *
     */
    public static final class LongTuple6 extends LongTuple<LongTuple6> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;
        /** The fifth long value in this tuple. */
        public final long _5;
        /** The sixth long value in this tuple. */
        public final long _6;

        LongTuple6() {
            this(0, 0, 0, 0, 0, 0);
        }

        LongTuple6(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6) {
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
         * LongTuple.LongTuple6 t = LongTuple.of(10L, 20L, 30L, 40L, 50L, 60L);
         * int n = t.arity();   // 6
         *
         * LongTuple.LongTuple6 neg = LongTuple.of(-1L, -2L, -3L, -4L, -5L, -6L);
         * int n2 = neg.arity();   // 6
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
         * LongTuple.LongTuple6 t = LongTuple.of(3L, 1L, 4L, 1L, 5L, 9L);
         * long min = t.min();   // 1
         *
         * LongTuple.LongTuple6 neg = LongTuple.of(-5L, -3L, -10L, -1L, -7L, -2L);
         * long minNeg = neg.min();   // -10
         *
         * LongTuple.LongTuple6 bounds = LongTuple.of(Long.MIN_VALUE, 0L, 1L, 2L, 3L, Long.MAX_VALUE);
         * long minBound = bounds.min();   // Long.MIN_VALUE
         *
         * LongTuple.LongTuple6 dup = LongTuple.of(9L, 9L, 9L, 9L, 9L, 9L);
         * long minDup = dup.min();   // 9
         * }</pre>
         *
         * @return the smallest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the maximum value among the six elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple6 t = LongTuple.of(3L, 1L, 4L, 1L, 5L, 9L);
         * long max = t.max();   // 9
         *
         * LongTuple.LongTuple6 neg = LongTuple.of(-5L, -3L, -10L, -1L, -7L, -2L);
         * long maxNeg = neg.max();   // -1
         *
         * LongTuple.LongTuple6 bounds = LongTuple.of(Long.MIN_VALUE, 0L, 1L, 2L, 3L, Long.MAX_VALUE);
         * long maxBound = bounds.max();   // Long.MAX_VALUE
         *
         * LongTuple.LongTuple6 dup = LongTuple.of(9L, 9L, 9L, 9L, 9L, 9L);
         * long maxDup = dup.max();   // 9
         * }</pre>
         *
         * @return the largest of _1, _2, _3, _4, _5, and _6
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median value of the six elements.
         * Because this tuple has an even number of elements, this returns the lower of the two middle values when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple6 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
         * long med = t.median();   // 3  (sorted=[1,2,3,4,5,6], lower middle=3)
         *
         * LongTuple.LongTuple6 rev = LongTuple.of(6L, 5L, 4L, 3L, 2L, 1L);
         * long medRev = rev.median();   // 3  (same elements, same result)
         *
         * LongTuple.LongTuple6 neg = LongTuple.of(-3L, -2L, -1L, 0L, 1L, 2L);
         * long medNeg = neg.median();   // -1  (sorted=[-3,-2,-1,0,1,2], lower middle=-1)
         *
         * LongTuple.LongTuple6 dup = LongTuple.of(5L, 5L, 5L, 5L, 5L, 5L);
         * long medDup = dup.median();   // 5
         * }</pre>
         *
         * @return the lower-middle long value of the six elements
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of all six elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple6 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
         * long sum = t.sum();   // 21
         *
         * LongTuple.LongTuple6 neg = LongTuple.of(-1L, -2L, -3L, -4L, -5L, -6L);
         * long sumNeg = neg.sum();   // -21
         *
         * // Sum wraps silently on overflow - no exception is thrown
         * LongTuple.LongTuple6 overflow = LongTuple.of(Long.MAX_VALUE, Long.MAX_VALUE, 0L, 0L, 0L, 0L);
         * long wrapped = overflow.sum();   // -2  (two's-complement wrap-around)
         *
         * LongTuple.LongTuple6 zeros = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L);
         * long zeroSum = zeros.sum();   // 0
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 as a long
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the average of all six elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple6 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
         * double avg = t.average();   // 3.5
         *
         * LongTuple.LongTuple6 neg = LongTuple.of(-3L, -2L, -1L, 0L, 1L, 2L);
         * double avgNeg = neg.average();   // -0.5
         *
         * LongTuple.LongTuple6 zeros = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L);
         * double avgZero = zeros.average();   // 0.0
         *
         * LongTuple.LongTuple6 dup = LongTuple.of(7L, 7L, 7L, 7L, 7L, 7L);
         * double avgDup = dup.average();   // 7.0
         * }</pre>
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6) / 6.0 as a double
         */
        @Override
        public double average() {
            return averageOf(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple6 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
         * LongTuple.LongTuple6 rev = t.reverse();
         * // rev.toString() returns "(6, 5, 4, 3, 2, 1)"
         *
         * LongTuple.LongTuple6 neg = LongTuple.of(-6L, -5L, -4L, -3L, -2L, -1L);
         * LongTuple.LongTuple6 revNeg = neg.reverse();
         * // revNeg.toString() returns "(-1, -2, -3, -4, -5, -6)"
         *
         * // Reversing a uniform tuple yields the same values
         * LongTuple.LongTuple6 dup = LongTuple.of(3L, 3L, 3L, 3L, 3L, 3L);
         * LongTuple.LongTuple6 revDup = dup.reverse();
         * // revDup.toString() returns "(3, 3, 3, 3, 3, 3)"
         *
         * // reverse() returns a new instance, original is unchanged
         * LongTuple.LongTuple6 orig = LongTuple.of(10L, 20L, 30L, 40L, 50L, 60L);
         * LongTuple.LongTuple6 reversed = orig.reverse();
         * boolean notSame = orig != reversed;   // true
         * }</pre>
         *
         * @return a new LongTuple.LongTuple6 with values (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple6 reverse() {
            return new LongTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple6 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
         * boolean has4 = t.contains(4L);   // true
         * boolean has9 = t.contains(9L);   // false
         *
         * // Negative values
         * LongTuple.LongTuple6 neg = LongTuple.of(-6L, -5L, -4L, -3L, -2L, -1L);
         * boolean hasNeg4 = neg.contains(-4L);   // true
         * boolean hasZero = neg.contains(0L);    // false
         *
         * // Duplicate elements - found if any match
         * LongTuple.LongTuple6 dup = LongTuple.of(7L, 7L, 7L, 7L, 7L, 7L);
         * boolean hasSeven = dup.contains(7L);   // true
         * boolean hasEight = dup.contains(8L);   // false
         * }</pre>
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals any of the six elements
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple6 t = LongTuple.of(10L, 20L, 30L, 40L, 50L, 60L);
         * long[] collected = new long[6];
         * int[] idx = {0};
         * t.forEach(v -> collected[idx[0]++] = v);
         * // collected == [10, 20, 30, 40, 50, 60]
         *
         * // Accumulate sum over negative values
         * LongTuple.LongTuple6 neg = LongTuple.of(-6L, -5L, -4L, -3L, -2L, -1L);
         * java.util.concurrent.atomic.AtomicLong sum = new java.util.concurrent.atomic.AtomicLong();
         * neg.forEach(sum::addAndGet);  // sum.get() == -21
         *
         * // Passing null throws IllegalArgumentException
         * LongTuple.LongTuple6 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
         * // t2.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> action) throws E {
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
         * LongTuple.LongTuple6 t1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
         * LongTuple.LongTuple6 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
         * boolean sameHash = t1.hashCode() == t2.hashCode();   // true (equal tuples have same hash)
         *
         * LongTuple.LongTuple6 t3 = LongTuple.of(6L, 5L, 4L, 3L, 2L, 1L);
         * boolean diffHash = t1.hashCode() != t3.hashCode();   // true (different element order)
         * }</pre>
         *
         * @return a hash code based on all six elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3)) + Long.hashCode(_4)) + Long.hashCode(_5)))
                    + Long.hashCode(_6);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple6 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple6 t1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
         * LongTuple.LongTuple6 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
         * boolean eq = t1.equals(t2);   // true
         *
         * LongTuple.LongTuple6 t3 = LongTuple.of(6L, 5L, 4L, 3L, 2L, 1L);
         * boolean neq = t1.equals(t3);   // false (different order)
         *
         * // Different arity - never equal
         * LongTuple.LongTuple5 shorter = LongTuple.of(1L, 2L, 3L, 4L, 5L);
         * boolean diffArity = t1.equals(shorter);   // false
         *
         * boolean notNull = t1.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple6 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple6 other)) {
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
         * LongTuple.LongTuple6 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L);
         * String s = t.toString();   // "(1, 2, 3, 4, 5, 6)"
         *
         * LongTuple.LongTuple6 neg = LongTuple.of(-6L, -5L, -4L, -3L, -2L, -1L);
         * String sNeg = neg.toString();   // "(-6, -5, -4, -3, -2, -1)"
         *
         * LongTuple.LongTuple6 zeros = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L);
         * String sZero = zeros.toString();   // "(0, 0, 0, 0, 0, 0)"
         *
         * LongTuple.LongTuple6 mixed = LongTuple.of(Long.MIN_VALUE, -1L, 0L, 1L, 2L, Long.MAX_VALUE);
         * String sMixed = mixed.toString();   // "(-9223372036854775808, -1, 0, 1, 2, 9223372036854775807)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ")";
        }

        /**
         * Returns the internal array of long elements.
         * The array is lazily initialized on first access.
         *
         * @return a long array containing all elements of this tuple
         */
        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly seven long values.
     * The values are accessible through the public final fields {@code _1} through {@code _7}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple7 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
     * long sum = tuple.sum();                            // 28
     * long median = tuple.median();                      // 4
     * LongTuple.LongTuple7 reversed = tuple.reverse();   // (7, 6, 5, 4, 3, 2, 1)
     * }</pre>
     *
     */
    public static final class LongTuple7 extends LongTuple<LongTuple7> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;
        /** The fifth long value in this tuple. */
        public final long _5;
        /** The sixth long value in this tuple. */
        public final long _6;
        /** The seventh long value in this tuple. */
        public final long _7;

        LongTuple7() {
            this(0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple7(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7) {
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
         * LongTuple.LongTuple7 t = LongTuple.of(10L, 20L, 30L, 40L, 50L, 60L, 70L);
         * int n = t.arity();   // 7
         *
         * LongTuple.LongTuple7 neg = LongTuple.of(-1L, -2L, -3L, -4L, -5L, -6L, -7L);
         * int n2 = neg.arity();   // 7
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
         * LongTuple.LongTuple7 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * t.min(); // returns 1
         * LongTuple.LongTuple7 t2 = LongTuple.of(-3L, -2L, -1L, 0L, 1L, 2L, 3L);
         * t2.min(); // returns -3
         * LongTuple.LongTuple7 t3 = LongTuple.of(Long.MIN_VALUE, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.min(); // returns Long.MIN_VALUE
         * LongTuple.LongTuple7 t4 = LongTuple.of(5L, 5L, 5L, 5L, 5L, 5L, 5L);
         * t4.min(); // returns 5
         * }</pre>
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the maximum value among the seven elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple7 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * t.max(); // returns 7
         * LongTuple.LongTuple7 t2 = LongTuple.of(-3L, -2L, -1L, 0L, 1L, 2L, 3L);
         * t2.max(); // returns 3
         * LongTuple.LongTuple7 t3 = LongTuple.of(Long.MAX_VALUE, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.max(); // returns Long.MAX_VALUE
         * LongTuple.LongTuple7 t4 = LongTuple.of(-5L, -5L, -5L, -5L, -5L, -5L, -5L);
         * t4.max(); // returns -5
         * }</pre>
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, and _7
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the median value of the seven elements.
         * Returns the middle value of the seven elements when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple7 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * t.median(); // returns 4
         * LongTuple.LongTuple7 t2 = LongTuple.of(-3L, -2L, -1L, 0L, 1L, 2L, 3L);
         * t2.median(); // returns 0
         * LongTuple.LongTuple7 t3 = LongTuple.of(7L, 6L, 5L, 4L, 3L, 2L, 1L);
         * t3.median(); // returns 4  (order of input does not affect result)
         * LongTuple.LongTuple7 t4 = LongTuple.of(-10L, -10L, -10L, 100L, 200L, 300L, 400L);
         * t4.median(); // returns 100  (4th of 7 sorted values)
         * }</pre>
         *
         * @return the middle long value when sorted
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of all seven elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple7 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * t.sum(); // returns 28
         * LongTuple.LongTuple7 t2 = LongTuple.of(-3L, -2L, -1L, 0L, 1L, 2L, 3L);
         * t2.sum(); // returns 0
         * LongTuple.LongTuple7 t3 = LongTuple.of(Long.MAX_VALUE, 1L, 0L, 0L, 0L, 0L, 0L);
         * t3.sum(); // returns Long.MIN_VALUE  (overflow wraps silently)
         * LongTuple.LongTuple7 t4 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t4.sum(); // returns 0
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 as a long
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the average of all seven elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple7 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * t.average(); // returns 4.0
         * LongTuple.LongTuple7 t2 = LongTuple.of(-3L, -2L, -1L, 0L, 1L, 2L, 3L);
         * t2.average(); // returns 0.0
         * LongTuple.LongTuple7 t3 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 7L);
         * t3.average(); // returns 1.0
         * LongTuple.LongTuple7 t4 = LongTuple.of(-7L, -6L, -5L, -4L, -3L, -2L, -1L);
         * t4.average(); // returns -4.0
         * }</pre>
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7) / 7.0 as a double
         */
        @Override
        public double average() {
            return averageOf(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple7 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * t.reverse();           // returns (7, 6, 5, 4, 3, 2, 1)
         * t.reverse().reverse(); // returns (1, 2, 3, 4, 5, 6, 7)  (round-trip)
         * LongTuple.LongTuple7 t2 = LongTuple.of(-1L, -2L, -3L, -4L, -5L, -6L, -7L);
         * t2.reverse(); // returns (-7, -6, -5, -4, -3, -2, -1)
         * LongTuple.LongTuple7 t3 = LongTuple.of(5L, 5L, 5L, 5L, 5L, 5L, 5L);
         * t3.reverse().equals(t3); // returns true  (all-duplicate tuple)
         * }</pre>
         *
         * @return a new LongTuple.LongTuple7 with values (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple7 reverse() {
            return new LongTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple7 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * t.contains(4L); // returns true
         * t.contains(7L); // returns true  (boundary: last element)
         * t.contains(0L); // returns false
         * t.contains(8L); // returns false
         * }</pre>
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals any of the seven elements
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple7 t = LongTuple.of(10L, 20L, 30L, 40L, 50L, 60L, 70L);
         * long[] sum = {0};
         * t.forEach(v -> sum[0] += v);
         * sum[0]; // returns 280
         * long[] count = {0};
         * t.forEach(v -> count[0]++);   // counts each element
         * count[0];                     // returns 7
         * // t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> action) throws E {
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
         * LongTuple.LongTuple7 t1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * LongTuple.LongTuple7 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * t1.hashCode() == t2.hashCode(); // returns true  (equal tuples have equal hash codes)
         * LongTuple.LongTuple7 t3 = LongTuple.of(7L, 6L, 5L, 4L, 3L, 2L, 1L);
         * t1.hashCode() == t3.hashCode(); // returns false  (different element order)
         * LongTuple.LongTuple7 t4 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t4.hashCode() == LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L).hashCode(); // returns true
         * }</pre>
         *
         * @return a hash code based on all seven elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3)) + Long.hashCode(_4)) + Long.hashCode(_5))
                    + Long.hashCode(_6))) + Long.hashCode(_7);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple7 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple7 t1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * LongTuple.LongTuple7 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * t1.equals(t2); // returns true
         * t1.equals(t1); // returns true  (reflexive)
         * LongTuple.LongTuple7 t3 = LongTuple.of(7L, 6L, 5L, 4L, 3L, 2L, 1L);
         * t1.equals(t3);   // returns false  (different element order)
         * t1.equals(null); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple7 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple7 other)) {
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
         * LongTuple.LongTuple7 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
         * t.toString(); // returns "(1, 2, 3, 4, 5, 6, 7)"
         * LongTuple.LongTuple7 t2 = LongTuple.of(-1L, -2L, -3L, -4L, -5L, -6L, -7L);
         * t2.toString(); // returns "(-1, -2, -3, -4, -5, -6, -7)"
         * LongTuple.LongTuple7 t3 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.toString(); // returns "(0, 0, 0, 0, 0, 0, 0)"
         * LongTuple.LongTuple7 t4 = LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE, 0L, 0L, 0L, 0L, 0L);
         * t4.toString(); // returns "(-9223372036854775808, 9223372036854775807, 0, 0, 0, 0, 0)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6, _7)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ")";
        }

        /**
         * Returns the internal array of long elements.
         * The array is lazily initialized on first access.
         *
         * @return a long array containing all elements of this tuple
         */
        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly eight long values.
     * The values are accessible through the public final fields {@code _1} through {@code _8}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple8 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
     * long sum = tuple.sum();                   // 36
     * double avg = tuple.average();             // 4.5
     * boolean contains5 = tuple.contains(5L);   // true
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more long values
     */
    @Deprecated
    public static final class LongTuple8 extends LongTuple<LongTuple8> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;
        /** The fifth long value in this tuple. */
        public final long _5;
        /** The sixth long value in this tuple. */
        public final long _6;
        /** The seventh long value in this tuple. */
        public final long _7;
        /** The eighth long value in this tuple. */
        public final long _8;

        LongTuple8() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple8(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8) {
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
         * LongTuple.LongTuple8 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * t.arity(); // returns 8
         * LongTuple.LongTuple8 t2 = LongTuple.of(-1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L);
         * t2.arity(); // returns 8
         * LongTuple.LongTuple8 t3 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.arity(); // returns 8
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
         * LongTuple.LongTuple8 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * t.min(); // returns 1
         * LongTuple.LongTuple8 t2 = LongTuple.of(-4L, -3L, -2L, -1L, 1L, 2L, 3L, 4L);
         * t2.min(); // returns -4
         * LongTuple.LongTuple8 t3 = LongTuple.of(Long.MIN_VALUE, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.min(); // returns Long.MIN_VALUE
         * LongTuple.LongTuple8 t4 = LongTuple.of(3L, 3L, 3L, 3L, 3L, 3L, 3L, 3L);
         * t4.min(); // returns 3
         * }</pre>
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, _7, and _8
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the maximum value among the eight elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple8 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * t.max(); // returns 8
         * LongTuple.LongTuple8 t2 = LongTuple.of(-4L, -3L, -2L, -1L, 1L, 2L, 3L, 4L);
         * t2.max(); // returns 4
         * LongTuple.LongTuple8 t3 = LongTuple.of(Long.MAX_VALUE, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.max(); // returns Long.MAX_VALUE
         * LongTuple.LongTuple8 t4 = LongTuple.of(-3L, -3L, -3L, -3L, -3L, -3L, -3L, -3L);
         * t4.max(); // returns -3
         * }</pre>
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, and _8
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the median value of the eight elements.
         * Because this tuple has an even number of elements, this returns the lower of the two middle values when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple8 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * t.median(); // returns 4  (sorted: [1,2,3,4,5,6,7,8], lower-middle = 4)
         * LongTuple.LongTuple8 t2 = LongTuple.of(-4L, -3L, -2L, -1L, 1L, 2L, 3L, 4L);
         * t2.median(); // returns -1  (sorted: [-4,-3,-2,-1,1,2,3,4], lower-middle = -1)
         * LongTuple.LongTuple8 t3 = LongTuple.of(8L, 7L, 6L, 5L, 4L, 3L, 2L, 1L);
         * t3.median(); // returns 4  (order of input does not affect result)
         * LongTuple.LongTuple8 t4 = LongTuple.of(-8L, -7L, -6L, -5L, -4L, -3L, -2L, -1L);
         * t4.median(); // returns -5  (sorted: [-8,-7,-6,-5,-4,-3,-2,-1], lower-middle = -5)
         * }</pre>
         *
         * @return the lower-middle long value of the eight elements
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of all eight elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple8 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * t.sum(); // returns 36
         * LongTuple.LongTuple8 t2 = LongTuple.of(-4L, -3L, -2L, -1L, 1L, 2L, 3L, 4L);
         * t2.sum(); // returns 0
         * LongTuple.LongTuple8 t3 = LongTuple.of(Long.MAX_VALUE, 1L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.sum(); // returns Long.MIN_VALUE  (overflow wraps silently)
         * LongTuple.LongTuple8 t4 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t4.sum(); // returns 0
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 as a long
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the average of all eight elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple8 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * t.average(); // returns 4.5
         * LongTuple.LongTuple8 t2 = LongTuple.of(-4L, -3L, -2L, -1L, 1L, 2L, 3L, 4L);
         * t2.average(); // returns 0.0
         * LongTuple.LongTuple8 t3 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 8L);
         * t3.average(); // returns 1.0
         * LongTuple.LongTuple8 t4 = LongTuple.of(-8L, -7L, -6L, -5L, -4L, -3L, -2L, -1L);
         * t4.average(); // returns -4.5
         * }</pre>
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8) / 8.0 as a double
         */
        @Override
        public double average() {
            return averageOf(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple8 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * t.reverse();           // returns (8, 7, 6, 5, 4, 3, 2, 1)
         * t.reverse().reverse(); // returns (1, 2, 3, 4, 5, 6, 7, 8)  (round-trip)
         * LongTuple.LongTuple8 t2 = LongTuple.of(-1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L);
         * t2.reverse(); // returns (-8, -7, -6, -5, -4, -3, -2, -1)
         * LongTuple.LongTuple8 t3 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.reverse().equals(t3); // returns true  (all-zero tuple)
         * }</pre>
         *
         * @return a new LongTuple.LongTuple8 with values (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple8 reverse() {
            return new LongTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple8 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * t.contains(1L); // returns true  (boundary: first element)
         * t.contains(8L); // returns true  (boundary: last element)
         * t.contains(0L); // returns false
         * t.contains(9L); // returns false
         * }</pre>
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals any of the eight elements
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple8 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * long[] sum = {0};
         * t.forEach(v -> sum[0] += v);
         * sum[0]; // returns 36
         * long[] count = {0};
         * t.forEach(v -> count[0]++);   // counts each element
         * count[0];                     // returns 8
         * // t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> action) throws E {
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
         * LongTuple.LongTuple8 t1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * LongTuple.LongTuple8 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * t1.hashCode() == t2.hashCode(); // returns true  (equal tuples have equal hash codes)
         * LongTuple.LongTuple8 t3 = LongTuple.of(8L, 7L, 6L, 5L, 4L, 3L, 2L, 1L);
         * t1.hashCode() == t3.hashCode(); // returns false  (different element order)
         * LongTuple.LongTuple8 t4 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t4.hashCode() == LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L).hashCode(); // returns true
         * }</pre>
         *
         * @return a hash code based on all eight elements
         */
        @Override
        public int hashCode() {
            return (31
                    * (31 * (31 * (31 * (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3)) + Long.hashCode(_4)) + Long.hashCode(_5))
                            + Long.hashCode(_6)) + Long.hashCode(_7)))
                    + Long.hashCode(_8);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple8 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple8 t1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * LongTuple.LongTuple8 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * t1.equals(t2); // returns true
         * t1.equals(t1); // returns true  (reflexive)
         * LongTuple.LongTuple8 t3 = LongTuple.of(8L, 7L, 6L, 5L, 4L, 3L, 2L, 1L);
         * t1.equals(t3);   // returns false  (different element order)
         * t1.equals(null); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple8 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple8 other)) {
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
         * LongTuple.LongTuple8 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
         * t.toString(); // returns "(1, 2, 3, 4, 5, 6, 7, 8)"
         * LongTuple.LongTuple8 t2 = LongTuple.of(-1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L);
         * t2.toString(); // returns "(-1, -2, -3, -4, -5, -6, -7, -8)"
         * LongTuple.LongTuple8 t3 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.toString(); // returns "(0, 0, 0, 0, 0, 0, 0, 0)"
         * LongTuple.LongTuple8 t4 = LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE, 0L, 0L, 0L, 0L, 0L, 0L);
         * t4.toString(); // returns "(-9223372036854775808, 9223372036854775807, 0, 0, 0, 0, 0, 0)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6, _7, _8)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")";
        }

        /**
         * Returns the internal array of long elements.
         * The array is lazily initialized on first access.
         *
         * @return a long array containing all elements of this tuple
         */
        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly nine long values.
     * The values are accessible through the public final fields {@code _1} through {@code _9}.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * LongTuple.LongTuple9 tuple = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
     * long sum = tuple.sum();         // 45
     * long median = tuple.median();   // 5
     * double avg = tuple.average();   // 5.0
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more long values
     */
    @Deprecated
    public static final class LongTuple9 extends LongTuple<LongTuple9> {

        /** The first long value in this tuple. */
        public final long _1;
        /** The second long value in this tuple. */
        public final long _2;
        /** The third long value in this tuple. */
        public final long _3;
        /** The fourth long value in this tuple. */
        public final long _4;
        /** The fifth long value in this tuple. */
        public final long _5;
        /** The sixth long value in this tuple. */
        public final long _6;
        /** The seventh long value in this tuple. */
        public final long _7;
        /** The eighth long value in this tuple. */
        public final long _8;
        /** The ninth long value in this tuple. */
        public final long _9;

        LongTuple9() {
            this(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple9(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8, final long _9) {
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
         * LongTuple.LongTuple9 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * t.arity(); // returns 9
         * LongTuple.LongTuple9 t2 = LongTuple.of(-1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L, -9L);
         * t2.arity(); // returns 9
         * LongTuple.LongTuple9 t3 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.arity(); // returns 9
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
         * LongTuple.LongTuple9 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * t.min(); // returns 1
         * LongTuple.LongTuple9 t2 = LongTuple.of(-4L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L);
         * t2.min(); // returns -4
         * LongTuple.LongTuple9 t3 = LongTuple.of(Long.MIN_VALUE, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.min(); // returns Long.MIN_VALUE
         * LongTuple.LongTuple9 t4 = LongTuple.of(2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L);
         * t4.min(); // returns 2
         * }</pre>
         *
         * @return the smallest of _1, _2, _3, _4, _5, _6, _7, _8, and _9
         */
        @Override
        public long min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the maximum value among the nine elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple9 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * t.max(); // returns 9
         * LongTuple.LongTuple9 t2 = LongTuple.of(-4L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L);
         * t2.max(); // returns 4
         * LongTuple.LongTuple9 t3 = LongTuple.of(Long.MAX_VALUE, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.max(); // returns Long.MAX_VALUE
         * LongTuple.LongTuple9 t4 = LongTuple.of(-2L, -2L, -2L, -2L, -2L, -2L, -2L, -2L, -2L);
         * t4.max(); // returns -2
         * }</pre>
         *
         * @return the largest of _1, _2, _3, _4, _5, _6, _7, _8, and _9
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the median value of the nine elements.
         * Returns the middle value of the nine elements when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple9 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * t.median(); // returns 5  (sorted: [1..9], middle = 5)
         * LongTuple.LongTuple9 t2 = LongTuple.of(-4L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L);
         * t2.median(); // returns 0  (sorted: [-4..4], middle = 0)
         * LongTuple.LongTuple9 t3 = LongTuple.of(9L, 8L, 7L, 6L, 5L, 4L, 3L, 2L, 1L);
         * t3.median(); // returns 5  (order of input does not affect result)
         * LongTuple.LongTuple9 t4 = LongTuple.of(-10L, -9L, -8L, -7L, -6L, -5L, -4L, -3L, -2L);
         * t4.median(); // returns -6  (sorted: [-10..-2], middle = -6)
         * }</pre>
         *
         * @return the middle long value when sorted
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of all nine elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple9 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * t.sum(); // returns 45
         * LongTuple.LongTuple9 t2 = LongTuple.of(-4L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L);
         * t2.sum(); // returns 0
         * LongTuple.LongTuple9 t3 = LongTuple.of(Long.MAX_VALUE, 1L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.sum(); // returns Long.MIN_VALUE  (overflow wraps silently)
         * LongTuple.LongTuple9 t4 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t4.sum(); // returns 0
         * }</pre>
         *
         * @return _1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9 as a long
         */
        @Override
        public long sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the average of all nine elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple9 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * t.average(); // returns 5.0
         * LongTuple.LongTuple9 t2 = LongTuple.of(-4L, -3L, -2L, -1L, 0L, 1L, 2L, 3L, 4L);
         * t2.average(); // returns 0.0
         * LongTuple.LongTuple9 t3 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 9L);
         * t3.average(); // returns 1.0
         * LongTuple.LongTuple9 t4 = LongTuple.of(-9L, -8L, -7L, -6L, -5L, -4L, -3L, -2L, -1L);
         * t4.average(); // returns -5.0
         * }</pre>
         *
         * @return (_1 + _2 + _3 + _4 + _5 + _6 + _7 + _8 + _9) / 9.0 as a double
         */
        @Override
        public double average() {
            return averageOf(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple9 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * t.reverse();           // returns (9, 8, 7, 6, 5, 4, 3, 2, 1)
         * t.reverse().reverse(); // returns (1, 2, 3, 4, 5, 6, 7, 8, 9)  (round-trip)
         * LongTuple.LongTuple9 t2 = LongTuple.of(-1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L, -9L);
         * t2.reverse(); // returns (-9, -8, -7, -6, -5, -4, -3, -2, -1)
         * LongTuple.LongTuple9 t3 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.reverse().equals(t3); // returns true  (all-zero tuple)
         * }</pre>
         *
         * @return a new LongTuple.LongTuple9 with values (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public LongTuple9 reverse() {
            return new LongTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if any element equals the specified value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple9 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * t.contains(1L);  // returns true  (boundary: first element)
         * t.contains(9L);  // returns true  (boundary: last element)
         * t.contains(0L);  // returns false
         * t.contains(10L); // returns false
         * }</pre>
         *
         * @param valueToFind the long value to search for
         * @return {@code true} if valueToFind equals any of the nine elements
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple9 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * long[] sum = {0};
         * t.forEach(v -> sum[0] += v);
         * sum[0]; // returns 45
         * long[] count = {0};
         * t.forEach(v -> count[0]++);   // counts each element
         * count[0];                     // returns 9
         * // t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> action) throws E {
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
         * LongTuple.LongTuple9 t1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * LongTuple.LongTuple9 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * t1.hashCode() == t2.hashCode(); // returns true  (equal tuples have equal hash codes)
         * LongTuple.LongTuple9 t3 = LongTuple.of(9L, 8L, 7L, 6L, 5L, 4L, 3L, 2L, 1L);
         * t1.hashCode() == t3.hashCode(); // returns false  (different element order)
         * LongTuple.LongTuple9 t4 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t4.hashCode() == LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L).hashCode(); // returns true
         * }</pre>
         *
         * @return a hash code based on all nine elements
         */
        @Override
        public int hashCode() {
            return (31 * (31
                    * (31 * (31 * (31 * (31 * (31 * (31 * Long.hashCode(_1) + Long.hashCode(_2)) + Long.hashCode(_3)) + Long.hashCode(_4)) + Long.hashCode(_5))
                            + Long.hashCode(_6)) + Long.hashCode(_7))
                    + Long.hashCode(_8))) + Long.hashCode(_9);
        }

        /**
         * Compares this tuple to another object for equality.
         * Two tuples are equal if they are both LongTuple.LongTuple9 instances
         * and all corresponding elements are equal.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * LongTuple.LongTuple9 t1 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * LongTuple.LongTuple9 t2 = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * t1.equals(t2); // returns true
         * t1.equals(t1); // returns true  (reflexive)
         * LongTuple.LongTuple9 t3 = LongTuple.of(9L, 8L, 7L, 6L, 5L, 4L, 3L, 2L, 1L);
         * t1.equals(t3);   // returns false  (different element order)
         * t1.equals(null); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a LongTuple.LongTuple9 with equal elements, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final LongTuple9 other)) {
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
         * LongTuple.LongTuple9 t = LongTuple.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
         * t.toString(); // returns "(1, 2, 3, 4, 5, 6, 7, 8, 9)"
         * LongTuple.LongTuple9 t2 = LongTuple.of(-1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L, -9L);
         * t2.toString(); // returns "(-1, -2, -3, -4, -5, -6, -7, -8, -9)"
         * LongTuple.LongTuple9 t3 = LongTuple.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t3.toString(); // returns "(0, 0, 0, 0, 0, 0, 0, 0, 0)"
         * LongTuple.LongTuple9 t4 = LongTuple.of(Long.MIN_VALUE, Long.MAX_VALUE, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
         * t4.toString(); // returns "(-9223372036854775808, 9223372036854775807, 0, 0, 0, 0, 0, 0, 0)"
         * }</pre>
         *
         * @return a string representation in the format "(_1, _2, _3, _4, _5, _6, _7, _8, _9)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ")";
        }

        /**
         * Returns the internal array of long elements.
         * The array is lazily initialized on first access.
         *
         * @return a long array containing all elements of this tuple
         */
        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
