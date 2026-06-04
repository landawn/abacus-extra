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
import com.landawn.abacus.util.stream.ByteStream;

/**
 * Base class for immutable tuples of primitive {@code byte} values.
 *
 * <p>The nested tuple types model fixed arities from 0 through 9. Factory methods such as
 * {@link #copyOf(byte[])} and the {@code of(...)} overloads select the matching subtype, while the base
 * class supplies aggregate, reversal, containment, and functional helper operations.</p>
 *
 * <p>All {@code byte} arithmetic in this class follows Java's signed semantics (range {@code -128}
 * to {@code 127}). Aggregates such as {@link #sum()} are widened to {@code int} and {@link #average()}
 * to {@code double} to avoid overflow.</p>
 *
 * @param <TP> the concrete {@code ByteTuple} subtype that fluent operations such as {@link #reverse()} return
 * @see PrimitiveTuple
 * @see BooleanTuple
 * @see CharTuple
 * @see ShortTuple
 * @see IntTuple
 * @see LongTuple
 * @see FloatTuple
 * @see DoubleTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class ByteTuple<TP extends ByteTuple<TP>> extends PrimitiveTuple<TP> {

    /** Lazily initialized cached array view of all tuple elements. */
    protected volatile byte[] elements;

    /**
     * Protected constructor for subclass instantiation.
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link #of(byte)}, {@link #of(byte, byte)}, etc., to create tuple instances.
     */
    protected ByteTuple() {
    }

    /**
     * Creates a ByteTuple.ByteTuple1 containing a single byte value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple1 t1 = ByteTuple.of((byte) 10);
     * byte value = t1._1;      // 10
     * int arity = t1.arity();  // 1
     *
     * ByteTuple.ByteTuple1 t2 = ByteTuple.of((byte) -5);
     * byte neg = t2._1;   // -5
     *
     * // boundary: minimum byte value
     * ByteTuple.ByteTuple1 tMin = ByteTuple.of((byte) -128);
     * byte minVal = tMin._1;   // -128
     *
     * // boundary: maximum byte value
     * ByteTuple.ByteTuple1 tMax = ByteTuple.of((byte) 127);
     * byte maxVal = tMax._1;   // 127
     * }</pre>
     *
     * @param _1 the byte value to store in the tuple
     * @return a new ByteTuple.ByteTuple1 containing the specified value
     */
    public static ByteTuple1 of(final byte _1) {
        return new ByteTuple1(_1);
    }

    /**
     * Creates a ByteTuple.ByteTuple2 containing two byte values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple2 t = ByteTuple.of((byte) 10, (byte) 20);
     * byte first = t._1;      // 10
     * byte second = t._2;     // 20
     * int arity = t.arity();  // 2
     *
     * ByteTuple.ByteTuple2 sorted = ByteTuple.of((byte) 5, (byte) 15);
     * byte min = sorted.min();   // 5
     * byte max = sorted.max();   // 15
     *
     * // boundary values
     * ByteTuple.ByteTuple2 bounds = ByteTuple.of((byte) -128, (byte) 127);
     * byte lo = bounds._1;   // -128
     * byte hi = bounds._2;   // 127
     *
     * // both elements the same
     * ByteTuple.ByteTuple2 same = ByteTuple.of((byte) 5, (byte) 5);
     * byte sameMin = same.min();   // 5
     * byte sameMax = same.max();   // 5
     * }</pre>
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @return a new ByteTuple.ByteTuple2 containing the specified values
     */
    public static ByteTuple2 of(final byte _1, final byte _2) {
        return new ByteTuple2(_1, _2);
    }

    /**
     * Creates a ByteTuple.ByteTuple3 containing three byte values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * byte third = t._3;         // 30
     * int arity = t.arity();     // 3
     * byte median = t.median();  // 20 (middle when sorted)
     *
     * ByteTuple.ByteTuple3 t2 = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
     * byte min = t2.min();   // 10
     * byte max = t2.max();   // 30
     *
     * // negative values
     * ByteTuple.ByteTuple3 neg = ByteTuple.of((byte) -10, (byte) 0, (byte) 10);
     * int sum = neg.sum();   // 0
     *
     * // all same value
     * ByteTuple.ByteTuple3 same = ByteTuple.of((byte) 5, (byte) 5, (byte) 5);
     * byte sameMedian = same.median();   // 5
     * }</pre>
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @return a new ByteTuple.ByteTuple3 containing the specified values
     */
    public static ByteTuple3 of(final byte _1, final byte _2, final byte _3) {
        return new ByteTuple3(_1, _2, _3);
    }

    /**
     * Creates a ByteTuple.ByteTuple4 containing four byte values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
     * byte first = t._1;   // 10
     * byte fourth = t._4;  // 40
     * // even arity: median returns the lower middle of the sorted elements
     * byte median = t.median();   // 20
     *
     * ByteTuple.ByteTuple4 t2 = ByteTuple.of((byte) 40, (byte) 30, (byte) 20, (byte) 10);
     * byte min = t2.min();   // 10
     * byte max = t2.max();   // 40
     *
     * // negative values
     * ByteTuple.ByteTuple4 neg = ByteTuple.of((byte) -50, (byte) -10, (byte) 0, (byte) 50);
     * // sorted: -50, -10, 0, 50 => lower middle => -10
     * byte negMedian = neg.median();   // -10
     *
     * // boundary bytes
     * ByteTuple.ByteTuple4 bounds = ByteTuple.of((byte) -128, (byte) -1, (byte) 0, (byte) 127);
     * int boundsSum = bounds.sum();   // -2
     * }</pre>
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @param _4 the fourth byte value
     * @return a new ByteTuple.ByteTuple4 containing the specified values
     */
    public static ByteTuple4 of(final byte _1, final byte _2, final byte _3, final byte _4) {
        return new ByteTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a ByteTuple.ByteTuple5 containing five byte values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple5 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
     * byte first = t._1;         // 10
     * byte fifth = t._5;         // 50
     * int sum = t.sum();         // 150 (exceeds byte range; returned as int)
     * byte median = t.median();  // 30 (middle element when sorted)
     *
     * // negative values with zero sum
     * ByteTuple.ByteTuple5 neg = ByteTuple.of((byte) -10, (byte) -5, (byte) 0, (byte) 5, (byte) 10);
     * int negSum = neg.sum();          // 0
     * byte negMedian = neg.median();   // 0
     *
     * // all negative
     * ByteTuple.ByteTuple5 allNeg = ByteTuple.of((byte) -50, (byte) -40, (byte) -30, (byte) -20, (byte) -10);
     * byte allNegMin = allNeg.min();   // -50
     * byte allNegMax = allNeg.max();   // -10
     * }</pre>
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @param _4 the fourth byte value
     * @param _5 the fifth byte value
     * @return a new ByteTuple.ByteTuple5 containing the specified values
     */
    public static ByteTuple5 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5) {
        return new ByteTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a ByteTuple.ByteTuple6 containing six byte values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple6 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
     * byte first = t._1;  // 10
     * byte sixth = t._6;  // 60
     * // reverse() returns a new tuple with elements in reverse order
     * ByteTuple.ByteTuple6 reversed = t.reverse();   // (60, 50, 40, 30, 20, 10)
     *
     * ByteTuple.ByteTuple6 t2 = ByteTuple.of((byte) 60, (byte) 50, (byte) 40, (byte) 30, (byte) 20, (byte) 10);
     * byte min = t2.min();   // 10
     * byte max = t2.max();   // 60
     *
     * // negative values
     * ByteTuple.ByteTuple6 neg = ByteTuple.of((byte) -3, (byte) -2, (byte) -1, (byte) 1, (byte) 2, (byte) 3);
     * byte negMin = neg.min();   // -3
     * byte negMax = neg.max();   // 3
     *
     * // contains check
     * boolean found = t.contains((byte) 30);     // true
     * boolean missing = t.contains((byte) 99);   // false
     * }</pre>
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @param _4 the fourth byte value
     * @param _5 the fifth byte value
     * @param _6 the sixth byte value
     * @return a new ByteTuple.ByteTuple6 containing the specified values
     */
    public static ByteTuple6 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6) {
        return new ByteTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a ByteTuple.ByteTuple7 containing seven byte values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple7 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
     * byte first = t._1;           // 10
     * byte seventh = t._7;         // 70
     * byte[] array = t.toArray();  // [10, 20, 30, 40, 50, 60, 70]
     * byte median = t.median();    // 40 (middle of 7 elements when sorted)
     *
     * ByteTuple.ByteTuple7 t2 = ByteTuple.of((byte) 70, (byte) 60, (byte) 50, (byte) 40, (byte) 30, (byte) 20, (byte) 10);
     * byte min = t2.min();   // 10
     * byte max = t2.max();   // 70
     *
     * // all zeros
     * ByteTuple.ByteTuple7 zeros = ByteTuple.of((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
     * int zeroSum = zeros.sum();   // 0
     *
     * // contains check
     * boolean found = t.contains((byte) 40);     // true
     * boolean missing = t.contains((byte) 99);   // false
     * }</pre>
     *
     * @param _1 the first byte value
     * @param _2 the second byte value
     * @param _3 the third byte value
     * @param _4 the fourth byte value
     * @param _5 the fifth byte value
     * @param _6 the sixth byte value
     * @param _7 the seventh byte value
     * @return a new ByteTuple.ByteTuple7 containing the specified values
     */
    public static ByteTuple7 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7) {
        return new ByteTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a ByteTuple.ByteTuple8 containing eight byte values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple8 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
     * byte first = t._1;      // 10
     * byte eighth = t._8;     // 80
     * int arity = t.arity();  // 8
     * int sum = t.sum();      // 360
     *
     * ByteList list = t.toList();
     * int listSize = list.size();   // 8
     *
     * // negative boundary values
     * ByteTuple.ByteTuple8 neg = ByteTuple.of((byte) -128, (byte) -64, (byte) -32, (byte) -16,
     *                                          (byte) -8, (byte) -4, (byte) -2, (byte) -1);
     * byte negMin = neg.min();   // -128
     *
     * // contains check
     * boolean found = t.contains((byte) 50);     // true
     * boolean missing = t.contains((byte) 99);   // false
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
     * @return a new ByteTuple.ByteTuple8 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more byte values
     */
    @Deprecated
    public static ByteTuple8 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7, final byte _8) {
        return new ByteTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a ByteTuple.ByteTuple9 containing nine byte values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple9 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50,
     *                                        (byte) 60, (byte) 70, (byte) 80, (byte) 90);
     * byte first = t._1;      // 10
     * byte ninth = t._9;      // 90
     * int arity = t.arity();  // 9
     * int sum = t.sum();      // 450
     *
     * // median of 9 elements (odd): sorted [10..90], middle index 4 => 50
     * byte median = t.median();   // 50
     *
     * // all negative values
     * ByteTuple.ByteTuple9 neg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5,
     *                                          (byte) -6, (byte) -7, (byte) -8, (byte) -9);
     * byte negMin = neg.min();   // -9
     * byte negMax = neg.max();   // -1
     *
     * // contains check
     * boolean found = t.contains((byte) 50);     // true
     * boolean missing = t.contains((byte) 99);   // false
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
     * @return a new ByteTuple.ByteTuple9 containing the specified values
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
     * This factory method supports arrays with 0 to 9 elements. For {@code null} or empty
     * arrays, returns the shared empty tuple. For arrays with 1-9 elements, returns the
     * corresponding {@code ByteTuple1}..{@code ByteTuple9} instance. The values are copied
     * into the new tuple; subsequent modifications to the input array do not affect it.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // typical: 3-element array
     * byte[] values = {(byte) 10, (byte) 20, (byte) 30};
     * ByteTuple.ByteTuple3 t3 = ByteTuple.copyOf(values);
     * byte third = t3._3;   // 30
     *
     * // single element
     * ByteTuple.ByteTuple1 t1 = ByteTuple.copyOf(new byte[]{(byte) 42});
     * byte val = t1._1;   // 42
     *
     * // null -> empty tuple (arity 0)
     * ByteTuple<?> fromNull = ByteTuple.copyOf(null);
     * int nullArity = fromNull.arity();   // 0
     *
     * // empty array -> empty tuple (arity 0)
     * ByteTuple<?> fromEmpty = ByteTuple.copyOf(new byte[0]);
     * int emptyArity = fromEmpty.arity();   // 0
     *
     * // mutation safety: modifying the source array does not affect the tuple
     * byte[] src = {(byte) 1, (byte) 2};
     * ByteTuple.ByteTuple2 t2 = ByteTuple.copyOf(src);
     * src[0] = (byte) 99;
     * byte unchanged = t2._1;   // 1
     *
     * // length > 9 -> throws IllegalArgumentException
     * ByteTuple.copyOf(new byte[10]);   // throws IllegalArgumentException
     * }</pre>
     *
     * <p><strong>Type note:</strong> the runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type. Assigning to the wrong arity-specific subtype will result in a
     * {@link ClassCastException} at the assignment site.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of byte values; may be {@code null} or empty, in which case the shared empty tuple is returned
     * @return a {@code ByteTuple} of the appropriate arity containing the array values, or the shared empty tuple if the array is {@code null} or empty
     * @throws IllegalArgumentException if {@code values} has more than 9 elements
     * @see #of(byte)
     */
    @SuppressWarnings("deprecation")
    public static <TP extends ByteTuple<TP>> TP copyOf(final byte[] values) {
        if (values == null || values.length == 0) {
            return (TP) ByteTuple0.EMPTY;
        }

        switch (values.length) {
            case 1:
                return (TP) ByteTuple.of(values[0]);

            case 2:
                return (TP) ByteTuple.of(values[0], values[1]);

            case 3:
                return (TP) ByteTuple.of(values[0], values[1], values[2]);

            case 4:
                return (TP) ByteTuple.of(values[0], values[1], values[2], values[3]);

            case 5:
                return (TP) ByteTuple.of(values[0], values[1], values[2], values[3], values[4]);

            case 6:
                return (TP) ByteTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);

            case 7:
                return (TP) ByteTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);

            case 8:
                return (TP) ByteTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);

            case 9:
                return (TP) ByteTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);

            default:
                throw new IllegalArgumentException("Too many elements (" + values.length + "). Maximum: 9");
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
     * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
     * byte min = t.min();   // 10
     *
     * // single-element tuple
     * ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 42);
     * byte singleMin = single.min();   // 42
     *
     * // negative values
     * ByteTuple.ByteTuple3 neg = ByteTuple.of((byte) -5, (byte) -10, (byte) -1);
     * byte negMin = neg.min();   // -10
     *
     * // boundary: byte range extremes
     * ByteTuple.ByteTuple2 bounds = ByteTuple.of((byte) -128, (byte) 127);
     * byte boundsMin = bounds.min();   // -128
     *
     * // empty tuple -> throws NoSuchElementException
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * empty.min();   // throws NoSuchElementException
     * }</pre>
     *
     * @return the minimum byte value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #max()
     * @see #median()
     */
    public byte min() {
        final byte[] arr = elements();

        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        return N.min(arr);
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
     * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
     * byte max = t.max();   // 30
     *
     * // single-element tuple
     * ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 42);
     * byte singleMax = single.max();   // 42
     *
     * // negative values
     * ByteTuple.ByteTuple3 neg = ByteTuple.of((byte) -5, (byte) -10, (byte) -1);
     * byte negMax = neg.max();   // -1
     *
     * // boundary: byte range extremes
     * ByteTuple.ByteTuple2 bounds = ByteTuple.of((byte) -128, (byte) 127);
     * byte boundsMax = bounds.max();   // 127
     *
     * // empty tuple -> throws NoSuchElementException
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * empty.max();   // throws NoSuchElementException
     * }</pre>
     *
     * @return the maximum byte value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see #median()
     */
    public byte max() {
        final byte[] arr = elements();

        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        return N.max(arr);
    }

    /**
     * Returns the median byte value in this tuple.
     * <p>
     * The median is the middle value when all elements are sorted. For tuples with
     * an odd number of elements, returns the exact middle value. For tuples with an
     * even number of elements, returns the lower middle element.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // odd number of elements: exact middle of sorted sequence
     * ByteTuple.ByteTuple3 t3 = ByteTuple.of((byte) 30, (byte) 10, (byte) 20);
     * byte median = t3.median();   // 20 (sorted: 10, 20, 30; index 1)
     *
     * // even number of elements: lower middle of sorted sequence
     * ByteTuple.ByteTuple4 t4 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
     * byte evenMedian = t4.median();   // 20 (sorted: 10, [20], 30, 40; lower of the two middles)
     *
     * // single element
     * ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 7);
     * byte singleMedian = single.median();   // 7
     *
     * // negative values
     * ByteTuple.ByteTuple3 neg = ByteTuple.of((byte) -30, (byte) -10, (byte) -20);
     * byte negMedian = neg.median();   // -20 (sorted: -30, -20, -10)
     *
     * // empty tuple -> throws NoSuchElementException
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * empty.median();   // throws NoSuchElementException
     * }</pre>
     *
     * @return the median byte value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see #max()
     * @see N#median(byte...)
     */
    public byte median() {
        final byte[] arr = elements();

        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        return N.median(arr);
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
     * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * int sum = t.sum();   // 60
     *
     * // result can exceed byte range; int prevents overflow
     * ByteTuple.ByteTuple2 large = ByteTuple.of((byte) 100, (byte) 50);
     * int largeSum = large.sum();   // 150
     *
     * // negative values
     * ByteTuple.ByteTuple3 neg = ByteTuple.of((byte) -10, (byte) -20, (byte) -30);
     * int negSum = neg.sum();   // -60
     *
     * // empty tuple -> 0
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * int emptySum = empty.sum();   // 0
     * }</pre>
     *
     * @return the sum of all byte values in this tuple as an {@code int}; {@code 0} for an empty tuple
     * @see #average()
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all byte values in this tuple as a double.
     * <p>
     * Note: The result is returned as a double to preserve precision. The average is
     * calculated by converting byte values to double during computation.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * double avg = t.average();   // 20.0
     *
     * // non-integer average
     * ByteTuple.ByteTuple2 pair = ByteTuple.of((byte) 5, (byte) 10);
     * double pairAvg = pair.average();   // 7.5
     *
     * // single element
     * ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 42);
     * double singleAvg = single.average();   // 42.0
     *
     * // negative and positive values
     * ByteTuple.ByteTuple2 negPos = ByteTuple.of((byte) -10, (byte) 10);
     * double negPosAvg = negPos.average();   // 0.0
     *
     * // empty tuple -> throws NoSuchElementException
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * empty.average();   // throws NoSuchElementException
     * }</pre>
     *
     * @return the average of all byte values in this tuple as a {@code double}
     * @throws NoSuchElementException if the tuple is empty
     * @see #sum()
     */
    public double average() {
        final byte[] arr = elements();

        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        return N.average(arr);
    }

    /**
     * Returns a tuple with the elements in reverse order.
     * <p>
     * This method returns a NEW tuple containing all elements in reversed order. The original
     * tuple remains unchanged as tuples are immutable.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple2 pair = ByteTuple.of((byte) 5, (byte) 15);
     * ByteTuple.ByteTuple2 reversedPair = pair.reverse();   // (15, 5)
     *
     * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * ByteTuple.ByteTuple3 reversed = t.reverse();   // (30, 20, 10)
     *
     * // single-element: reverse returns a new instance with the same value
     * ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 42);
     * ByteTuple.ByteTuple1 reversedSingle = single.reverse();   // (42)
     *
     * // empty tuple: reverse returns the same singleton empty tuple
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * int emptyArity = empty.reverse().arity();   // 0
     * }</pre>
     *
     * <p>For tuples of arity 0 or 1, the returned tuple is equal to this one (reversing has no effect).
     * The empty tuple returns itself; arity-1 tuples return a new instance with the same value.</p>
     *
     * @return a tuple of the same arity with the elements in reverse order
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
     * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * boolean has20 = t.contains((byte) 20);   // true
     * boolean has40 = t.contains((byte) 40);   // false
     *
     * // boundary values
     * ByteTuple.ByteTuple2 bounds = ByteTuple.of((byte) -128, (byte) 127);
     * boolean hasMin = bounds.contains((byte) -128);   // true
     * boolean hasZero = bounds.contains((byte) 0);     // false
     *
     * // single-element
     * ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 5);
     * boolean hasMatch = single.contains((byte) 5);   // true
     * boolean noMatch = single.contains((byte) 6);    // false
     *
     * // empty tuple always returns false
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * boolean emptyContains = empty.contains((byte) 0);   // false
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
     * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * byte[] array = t.toArray();   // [10, 20, 30]
     *
     * // mutation safety: modifying the returned array does not change the tuple
     * array[0] = (byte) 99;
     * byte unchanged = t._1;   // 10
     *
     * // single element
     * ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 42);
     * byte[] singleArr = single.toArray();   // [42]
     *
     * // empty tuple
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * byte[] emptyArr = empty.toArray();
     * int emptyLen = emptyArr.length;   // 0
     * }</pre>
     *
     * @return a new byte array containing all tuple elements
     * @see #toList()
     * @see #stream()
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
     * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * ByteList list = t.toList();
     * int listSize = list.size();   // 3
     * byte first = list.get(0);     // 10
     *
     * // modifying the list does not affect the tuple
     * list.add((byte) 40);          // modifies the copy, not the tuple
     * int tupleArity = t.arity();   // still 3
     *
     * // single element
     * ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 5);
     * ByteList singleList = single.toList();
     * int singleSize = singleList.size();   // 1
     *
     * // empty tuple
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * ByteList emptyList = empty.toList();
     * int emptySize = emptyList.size();   // 0
     * }</pre>
     *
     * @return a new ByteList containing all tuple elements
     * @see #toArray()
     * @see #stream()
     */
    public ByteList toList() {
        return ByteList.of(elements().clone());
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
     * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     *
     * // accumulate sum using external array
     * int[] sum = {0};
     * t.forEach(b -> sum[0] += b);
     * // sum[0] is now 60
     *
     * // collect elements into a list
     * java.util.List<Byte> collected = new java.util.ArrayList<>();
     * t.forEach(b -> collected.add(b));  // collected is [10, 20, 30]
     *
     * // empty tuple: consumer is never invoked
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * int[] count = {0};
     * empty.forEach(b -> count[0]++);  // count[0] remains 0
     *
     * // null action -> throws IllegalArgumentException
     * // t.forEach(null);   // throws IllegalArgumentException
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the action to be performed for each element, must not be {@code null}
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     * @see #stream()
     */
    public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> action) throws E {
        N.checkArgNotNull(action);

        for (final byte element : elements()) {
            action.accept(element);
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
     * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * int sum = t.stream().sum();                            // 60
     * long count = t.stream().filter(b -> b > 15).count();   // 2
     *
     * // single element
     * ByteTuple.ByteTuple1 single = ByteTuple.of((byte) 5);
     * long singleCount = single.stream().count();   // 1
     *
     * // empty tuple
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * long emptyCount = empty.stream().count();   // 0
     * }</pre>
     *
     * @return a ByteStream containing all tuple elements
     * @see #toArray()
     * @see #toList()
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple3 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
     * ByteTuple.ByteTuple3 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
     * // equal tuples have equal hash codes
     * boolean sameHash = (t1.hashCode() == t2.hashCode());   // true
     *
     * // different values -> typically different hash codes
     * ByteTuple.ByteTuple3 t3 = ByteTuple.of((byte) 1, (byte) 2, (byte) 4);
     * // t1.hashCode() != t3.hashCode() in most cases (not guaranteed, but typical)
     *
     * // empty tuple has a stable hash code
     * ByteTuple<?> empty = ByteTuple.copyOf(new byte[0]);
     * int emptyHash = empty.hashCode();   // consistent across calls
     * }</pre>
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
     *   <li>They are instances of the exact same runtime class (so tuples of different arity are never equal), and</li>
     *   <li>They contain the same byte values in the same order</li>
     * </ul>
     *
     * <p>This method is consistent with {@link #hashCode()}. The non-empty arity-specific subclasses
     * override this method with an equivalent but specialized implementation that compares fields directly.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple3 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
     * ByteTuple.ByteTuple3 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
     * boolean equal = t1.equals(t2);   // true
     *
     * // different values
     * ByteTuple.ByteTuple3 t3 = ByteTuple.of((byte) 1, (byte) 2, (byte) 4);
     * boolean notEqual = t1.equals(t3);   // false
     *
     * // different arity: never equal even with same prefix values
     * ByteTuple.ByteTuple2 t4 = ByteTuple.of((byte) 1, (byte) 2);
     * boolean arityDiff = t1.equals(t4);   // false
     *
     * // null and wrong type
     * boolean nullCheck = t1.equals(null);          // false
     * boolean typeCheck = t1.equals("(1, 2, 3)");   // false
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * String s3 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30).toString();   // "(10, 20, 30)"
     * String s2 = ByteTuple.of((byte) 5, (byte) 10).toString();               // "(5, 10)"
     * String s1 = ByteTuple.of((byte) 42).toString();                         // "(42)"
     *
     * // empty tuple
     * String s0 = ByteTuple.copyOf(new byte[0]).toString();   // "()"
     *
     * // negative values
     * String sNeg = ByteTuple.of((byte) -5, (byte) 0, (byte) 5).toString();   // "(-5, 0, 5)"
     * }</pre>
     *
     * @return a string representation of this tuple
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    /**
     * Returns the internal array containing all byte elements in this tuple.
     * <p>
     * <b>Warning:</b> The returned array is the internal representation of this tuple.
     * Modifying the returned array will compromise the immutability of this tuple.
     * Use {@link #toArray()} instead if you need an array that can be safely modified.
     * </p>
     *
     * @return the internal array of byte elements
     */
    protected abstract byte[] elements();

    /**
     * An empty ByteTuple containing no elements (arity 0).
     * <p>
     * This package-private class is exposed only through the base {@code ByteTuple} type
     * via the singleton instance returned by {@link #copyOf(byte[])} when invoked with a
     * {@code null} or zero-length array. {@link #sum()} returns 0, while {@link #min()},
     * {@link #max()}, {@link #median()}, and {@link #average()} all throw {@link java.util.NoSuchElementException}.
     * </p>
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
         * @return this {@code ByteTuple0} instance
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

        /**
         * Returns the internal array of byte elements.
         * For an empty tuple, returns an empty byte array.
         *
         * @return an empty byte array
         */
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
     * ByteTuple.ByteTuple1 tuple = ByteTuple.of((byte) 42);
     * byte value = tuple._1;   // 42
     * byte min = tuple.min();  // 42 (single element)
     * byte max = tuple.max();  // 42 (single element)
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple1 t = ByteTuple.of((byte) 42);
         * int arity = t.arity();   // 1
         *
         * ByteTuple.ByteTuple1 zero = ByteTuple.of((byte) 0);
         * int zeroArity = zero.arity();   // 1
         *
         * // boundary byte values still yield arity 1
         * ByteTuple.ByteTuple1 minByte = ByteTuple.of((byte) -128);
         * int minArity = minByte.arity();   // 1
         *
         * ByteTuple.ByteTuple1 maxByte = ByteTuple.of((byte) 127);
         * int maxArity = maxByte.arity();   // 1
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple1 t = ByteTuple.of((byte) 42);
         * byte min = t.min();   // 42
         *
         * ByteTuple.ByteTuple1 zero = ByteTuple.of((byte) 0);
         * byte zeroMin = zero.min();   // 0
         *
         * // minimum byte value
         * ByteTuple.ByteTuple1 minByte = ByteTuple.of((byte) -128);
         * byte minMin = minByte.min();   // -128
         *
         * // maximum byte value
         * ByteTuple.ByteTuple1 maxByte = ByteTuple.of((byte) 127);
         * byte maxMin = maxByte.min();   // 127
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple1 t = ByteTuple.of((byte) 42);
         * byte max = t.max();   // 42
         *
         * ByteTuple.ByteTuple1 zero = ByteTuple.of((byte) 0);
         * byte zeroMax = zero.max();   // 0
         *
         * // minimum byte value
         * ByteTuple.ByteTuple1 minByte = ByteTuple.of((byte) -128);
         * byte minMax = minByte.max();   // -128
         *
         * // maximum byte value
         * ByteTuple.ByteTuple1 maxByte = ByteTuple.of((byte) 127);
         * byte maxMax = maxByte.max();   // 127
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple1 t = ByteTuple.of((byte) 42);
         * byte median = t.median();   // 42
         *
         * ByteTuple.ByteTuple1 zero = ByteTuple.of((byte) 0);
         * byte zeroMedian = zero.median();   // 0
         *
         * // minimum byte value
         * ByteTuple.ByteTuple1 minByte = ByteTuple.of((byte) -128);
         * byte minMedian = minByte.median();   // -128
         *
         * // maximum byte value
         * ByteTuple.ByteTuple1 maxByte = ByteTuple.of((byte) 127);
         * byte maxMedian = maxByte.median();   // 127
         * }</pre>
         *
         * @return the single byte value in this tuple
         */
        @Override
        public byte median() {
            return _1;
        }

        /**
         * Returns the sum of all byte values in this tuple.
         * Since this tuple contains only one element, it returns that element widened to an {@code int}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple1 t = ByteTuple.of((byte) 5);
         * t.sum();   // returns 5
         *
         * ByteTuple.of((byte) -3).sum();   // returns -3
         *
         * // boundary values
         * ByteTuple.of(Byte.MAX_VALUE).sum();   // returns 127
         * ByteTuple.of(Byte.MIN_VALUE).sum();   // returns -128
         * }</pre>
         *
         * @return the single byte value widened to an {@code int} (sign-extended)
         */
        @Override
        public int sum() {
            return _1;
        }

        /**
         * Returns the average of all byte values in this tuple.
         * Since this tuple contains only one element, it returns that element converted to a {@code double}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple1 t = ByteTuple.of((byte) 5);
         * t.average();   // returns 5.0
         *
         * ByteTuple.of((byte) -3).average();   // returns -3.0
         *
         * // boundary values
         * ByteTuple.of(Byte.MAX_VALUE).average();   // returns 127.0
         * ByteTuple.of(Byte.MIN_VALUE).average();   // returns -128.0
         * }</pre>
         *
         * @return the single byte value as a {@code double}
         */
        @Override
        public double average() {
            return _1;
        }

        /**
         * Returns a new ByteTuple.ByteTuple1 with the same element.
         * Since this tuple has only one element, reversing has no effect.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple1 t = ByteTuple.of((byte) 7);
         * ByteTuple.ByteTuple1 r = t.reverse();
         * r._1;   // returns 7  (same value, new instance)
         *
         * // negative value
         * ByteTuple.of((byte) -5).reverse()._1;   // returns -5
         *
         * // boundary
         * ByteTuple.of(Byte.MIN_VALUE).reverse()._1;   // returns -128
         * ByteTuple.of(Byte.MAX_VALUE).reverse()._1;   // returns 127
         * }</pre>
         *
         * @return a new ByteTuple.ByteTuple1 with the same element
         */
        @Override
        public ByteTuple1 reverse() {
            return new ByteTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple1 t = ByteTuple.of((byte) 42);
         * t.contains((byte) 42);   // returns true
         * t.contains((byte) 0);    // returns false
         *
         * // negative and boundary values
         * ByteTuple.of((byte) -1).contains((byte) -1);              // returns true
         * ByteTuple.of(Byte.MIN_VALUE).contains(Byte.MIN_VALUE);    // returns true
         * ByteTuple.of(Byte.MIN_VALUE).contains(Byte.MAX_VALUE);    // returns false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 10).hashCode();    // returns 10
         * ByteTuple.of((byte) -1).hashCode();    // returns -1
         *
         * // boundary values
         * ByteTuple.of(Byte.MIN_VALUE).hashCode();   // returns -128
         * ByteTuple.of(Byte.MAX_VALUE).hashCode();   // returns 127
         * }</pre>
         *
         * @return the single byte element widened to an {@code int} (sign-extended)
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
         * ByteTuple.ByteTuple1 t1 = ByteTuple.of((byte) 5);
         * ByteTuple.ByteTuple1 t2 = ByteTuple.of((byte) 5);
         * t1.equals(t2);    // returns true
         *
         * ByteTuple.of((byte) 5).equals(ByteTuple.of((byte) 6));   // returns false
         *
         * // reflexivity and null/type checks
         * t1.equals(t1);                                                       // returns true
         * t1.equals(null);                                                     // returns false
         * ByteTuple.of(Byte.MIN_VALUE).equals(ByteTuple.of(Byte.MIN_VALUE));   // returns true
         * ByteTuple.of(Byte.MIN_VALUE).equals(ByteTuple.of(Byte.MAX_VALUE));   // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple.ByteTuple1 with the same element, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 5).toString();     // returns "(5)"
         * ByteTuple.of((byte) -3).toString();    // returns "(-3)"
         *
         * // boundary values
         * ByteTuple.of(Byte.MAX_VALUE).toString();    // returns "(127)"
         * ByteTuple.of(Byte.MIN_VALUE).toString();    // returns "(-128)"
         * }</pre>
         *
         * @return a string representation in the format "(element)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
        }

        /**
         * Returns the internal array of byte elements.
         * The array is lazily initialized on first access.
         *
         * @return a byte array containing the single element
         */
        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly two byte values.
     * The values are accessible through the public final fields {@code _1} and {@code _2}.
     *
     * <p>In addition to the operations inherited from {@link ByteTuple}, this class provides
     * functional helpers for working with pairs:</p>
     * <ul>
     *   <li>{@link #accept(Throwables.ByteBiConsumer)} - consume both values</li>
     *   <li>{@link #map(Throwables.ByteBiFunction)} - transform the pair to a single value</li>
     *   <li>{@link #filter(Throwables.ByteBiPredicate)} - conditionally wrap in {@link Optional}</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
     * byte first = tuple._1;   // 10
     * byte second = tuple._2;  // 20
     *
     * // Using functional operations
     * tuple.accept((a, b) -> System.out.println(a + " + " + b));
     * int sum = tuple.map((a, b) -> a + b);   // 30
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 1, (byte) 2).arity();   // returns 2
         *
         * // arity is constant regardless of element values
         * ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE).arity();   // returns 2
         * ByteTuple.of((byte) 0, (byte) 0).arity();               // returns 2
         * ByteTuple.of((byte) -1, (byte) -2).arity();             // returns 2
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 3, (byte) 7).min();    // returns 3
         * ByteTuple.of((byte) 7, (byte) 3).min();    // returns 3
         *
         * // same values and boundary
         * ByteTuple.of((byte) 5, (byte) 5).min();                       // returns 5
         * ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE).min();           // returns -128
         * ByteTuple.of((byte) -5, (byte) -10).min();                    // returns -10
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 3, (byte) 7).max();    // returns 7
         * ByteTuple.of((byte) 7, (byte) 3).max();    // returns 7
         *
         * // same values and boundary
         * ByteTuple.of((byte) 5, (byte) 5).max();                       // returns 5
         * ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE).max();           // returns 127
         * ByteTuple.of((byte) -5, (byte) -10).max();                    // returns -5
         * }</pre>
         *
         * @return the larger of the two byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median byte value in this tuple.
         * For tuples with an even number of elements, returns the lower middle element of the sorted values.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // sorted(3, 7) -> lower middle = 3
         * ByteTuple.of((byte) 3, (byte) 7).median();    // returns 3
         * ByteTuple.of((byte) 7, (byte) 3).median();    // returns 3
         *
         * // same values
         * ByteTuple.of((byte) 5, (byte) 5).median();    // returns 5
         *
         * // negative and boundary
         * ByteTuple.of((byte) -5, (byte) -10).median();               // returns -10
         * ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE).median();      // returns -128
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 3, (byte) 7).sum();         // returns 10
         * ByteTuple.of((byte) -5, (byte) 5).sum();        // returns 0
         *
         * // duplicate and boundary values
         * ByteTuple.of((byte) 5, (byte) 5).sum();         // returns 10
         * ByteTuple.of((byte) -5, (byte) -10).sum();      // returns -15
         * // MIN_VALUE + MAX_VALUE = -128 + 127 = -1
         * ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE).sum();   // returns -1
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 3, (byte) 7).average();          // returns 5.0
         * ByteTuple.of((byte) -5, (byte) 5).average();         // returns 0.0
         *
         * // duplicate and boundary values
         * ByteTuple.of((byte) 5, (byte) 5).average();          // returns 5.0
         * ByteTuple.of((byte) -5, (byte) -10).average();       // returns -7.5
         * // (-128 + 127) / 2 = -0.5
         * ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE).average();   // returns -0.5
         * }</pre>
         *
         * @return the average of both byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2);
        }

        /**
         * Returns a new ByteTuple.ByteTuple2 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple2 t = ByteTuple.of((byte) 3, (byte) 7);
         * ByteTuple.ByteTuple2 r = t.reverse();
         * r._1;   // returns 7
         * r._2;   // returns 3
         *
         * // same element values - reverse is a new instance
         * ByteTuple.of((byte) 5, (byte) 5).reverse()._1;   // returns 5
         *
         * // negative values
         * ByteTuple.ByteTuple2 neg = ByteTuple.of((byte) -1, (byte) -2).reverse();
         * neg._1;   // returns -2
         * neg._2;   // returns -1
         * }</pre>
         *
         * @return a new ByteTuple.ByteTuple2 with elements swapped
         */
        @Override
        public ByteTuple2 reverse() {
            return new ByteTuple2(_2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple2 t = ByteTuple.of((byte) 3, (byte) 7);
         * t.contains((byte) 3);    // returns true
         * t.contains((byte) 7);    // returns true
         * t.contains((byte) 5);    // returns false
         *
         * // boundary values
         * ByteTuple.ByteTuple2 tb = ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE);
         * tb.contains(Byte.MIN_VALUE);   // returns true
         * tb.contains(Byte.MAX_VALUE);   // returns true
         * tb.contains((byte) 0);         // returns false
         * }</pre>
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
         * <p>
         * Iterates through both elements in order (_1, then _2), executing the provided
         * consumer action for each element individually. This method is useful for side effects
         * that should be applied to each element separately.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
         *
         * // Print each element (prints "Value: 10" then "Value: 20" on separate lines)
         * tuple.forEach(b -> System.out.println("Value: " + b));   // prints each element
         *
         * // Collect to list
         * List<Byte> list = new ArrayList<>();
         * tuple.forEach(list::add);   // list contains [10, 20]
         *
         * // Sum using accumulator
         * java.util.concurrent.atomic.AtomicInteger sum = new java.util.concurrent.atomic.AtomicInteger();
         * tuple.forEach(sum::addAndGet);   // sum is 30
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
        }

        /**
         * Applies the given bi-consumer to both elements of this tuple.
         * <p>
         * This method executes the provided action with both tuple elements as arguments.
         * It is useful for performing side effects or operations that require access to
         * both elements simultaneously. Unlike {@link #forEach}, which processes elements
         * individually, this method processes both elements together in a single call.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
         *
         * // Print both values
         * tuple.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * // Output: 10 + 20 = 30
         *
         * // Update external state with both values
         * Map<String, Integer> results = new HashMap<>();
         * tuple.accept((a, b) -> results.put("sum", (int)(a + b)));   // stores the sum in results
         *
         * // Compare and log
         * tuple.accept((a, b) -> {
         *     if (a < b) {
         *         System.out.println(a + " is less than " + b);   // prints when the condition holds
         *     }
         * });
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the bi-consumer to apply to both elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         * @see #forEach(Throwables.ByteConsumer)
         * @see #map(Throwables.ByteBiFunction)
         */
        public <E extends Exception> void accept(final Throwables.ByteBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given bi-function to both elements of this tuple and returns the result.
         * <p>
         * This method transforms both tuple elements into a single result value using the provided
         * bi-function. It is useful for combining, computing, or deriving new values from the tuple
         * elements. The result can be of any type, providing flexibility in how the tuple elements
         * are processed and combined.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
         *
         * // Calculate sum
         * int sum = tuple.map((a, b) -> a + b);   // 30
         *
         * // Calculate product
         * int product = tuple.map((a, b) -> a * b);   // 200
         *
         * // Create a formatted string
         * String formatted = tuple.map((a, b) -> String.format("(%d, %d)", a, b));   // "(10, 20)"
         *
         * // Compare values
         * boolean isAscending = tuple.map((a, b) -> a < b);   // true
         *
         * // Create a custom object
         * java.awt.Point point = tuple.map((x, y) -> new java.awt.Point(x, y));
         * }</pre>
         *
         * @param <U> the type of the result value
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the bi-function to apply to both elements, must not be {@code null}
         * @return the result of applying the bi-function to both elements (may be {@code null})
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception during execution
         * @see #accept(Throwables.ByteBiConsumer)
         * @see #filter(Throwables.ByteBiPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.ByteBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if it matches the given bi-predicate,
         * otherwise returns an empty Optional.
         * <p>
         * This method conditionally returns the tuple based on a test of both elements.
         * If the bi-predicate evaluates to {@code true} when applied to both elements,
         * the tuple is wrapped in an Optional and returned. Otherwise, an empty Optional
         * is returned. This is useful for conditional processing and filtering tuples
         * based on relationships between their elements.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple2 tuple = ByteTuple.of((byte) 10, (byte) 20);
         *
         * // Filter where first < second
         * u.Optional<ByteTuple.ByteTuple2> result = tuple.filter((a, b) -> a < b);
         * // result contains the tuple
         *
         * // Filter where sum exceeds threshold
         * u.Optional<ByteTuple.ByteTuple2> highSum = tuple.filter((a, b) -> (a + b) > 25);
         * // highSum contains the tuple (10 + 20 = 30 > 25)
         *
         * // Filter with equality check
         * u.Optional<ByteTuple.ByteTuple2> equal = tuple.filter((a, b) -> a == b);
         * // equal is empty (10 != 20)
         *
         * // Chain with other operations
         * tuple.filter((a, b) -> a > 0 && b > 0)
         *      .map(t -> t.sum())
         *      .ifPresent(System.out::println);   // Prints: 30
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the bi-predicate to test both elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception during evaluation
         * @see #accept(Throwables.ByteBiConsumer)
         * @see #map(Throwables.ByteBiFunction)
         */
        public <E extends Exception> Optional<ByteTuple2> filter(final Throwables.ByteBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // hashCode = 31 * _1 + _2
         * ByteTuple.of((byte) 10, (byte) 20).hashCode();   // returns 31 * 10 + 20 = 330
         * ByteTuple.of((byte) 0, (byte) 0).hashCode();     // returns 0
         *
         * // order matters: different order yields different hash
         * ByteTuple.of((byte) 3, (byte) 7).hashCode();     // returns 31 * 3 + 7 = 100
         * ByteTuple.of((byte) 7, (byte) 3).hashCode();     // returns 31 * 7 + 3 = 220
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple2 t1 = ByteTuple.of((byte) 3, (byte) 7);
         * t1.equals(ByteTuple.of((byte) 3, (byte) 7));   // returns true
         * t1.equals(ByteTuple.of((byte) 7, (byte) 3));   // returns false  (order matters)
         *
         * // reflexivity and null/type checks
         * t1.equals(t1);     // returns true
         * t1.equals(null);   // returns false
         * ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE)
         *         .equals(ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE));   // returns true
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple.ByteTuple2 with the same elements in the same order, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 3, (byte) 7).toString();          // returns "(3, 7)"
         * ByteTuple.of((byte) -1, (byte) -2).toString();        // returns "(-1, -2)"
         *
         * // zero and boundary
         * ByteTuple.of((byte) 0, (byte) 0).toString();               // returns "(0, 0)"
         * ByteTuple.of(Byte.MIN_VALUE, Byte.MAX_VALUE).toString();   // returns "(-128, 127)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
        }

        /**
         * Returns the internal array of byte elements.
         * The array is lazily initialized on first access.
         *
         * @return a byte array containing all elements in order
         */
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
     * ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
     * byte first = tuple._1;   // 10
     * byte second = tuple._2;  // 20
     * byte third = tuple._3;   // 30
     *
     * // Using statistical operations
     * byte min = tuple.min();         // 10
     * byte max = tuple.max();         // 30
     * double avg = tuple.average();   // 20.0
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 1, (byte) 2, (byte) 3).arity();   // returns 3
         *
         * // arity is constant regardless of element values
         * ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE).arity();   // returns 3
         * ByteTuple.of((byte) 0, (byte) 0, (byte) 0).arity();               // returns 3
         * ByteTuple.of((byte) -1, (byte) -2, (byte) -3).arity();            // returns 3
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 1, (byte) 2, (byte) 3).min();    // returns 1
         * ByteTuple.of((byte) -5, (byte) -10, (byte) 0).min(); // returns -10
         *
         * // all same and boundary
         * ByteTuple.of((byte) 5, (byte) 5, (byte) 5).min();                          // returns 5
         * ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE).min();              // returns -128
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 1, (byte) 2, (byte) 3).max();    // returns 3
         * ByteTuple.of((byte) -5, (byte) -10, (byte) 0).max(); // returns 0
         *
         * // all same and boundary
         * ByteTuple.of((byte) 5, (byte) 5, (byte) 5).max();                          // returns 5
         * ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE).max();              // returns 127
         * }</pre>
         *
         * @return the largest of the three byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median byte value in this tuple.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // sorted(1, 2, 3) -> middle = 2
         * ByteTuple.of((byte) 1, (byte) 2, (byte) 3).median();   // returns 2
         * ByteTuple.of((byte) 3, (byte) 1, (byte) 2).median();   // returns 2
         *
         * // all same
         * ByteTuple.of((byte) 5, (byte) 5, (byte) 5).median();   // returns 5
         *
         * // negative and boundary
         * ByteTuple.of((byte) -5, (byte) -10, (byte) 0).median();              // returns -5
         * ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE).median();     // returns 0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 1, (byte) 2, (byte) 3).sum();         // returns 6
         * ByteTuple.of((byte) -5, (byte) -10, (byte) 0).sum();      // returns -15
         *
         * // all same and boundary
         * ByteTuple.of((byte) 5, (byte) 5, (byte) 5).sum();         // returns 15
         * // MIN_VALUE + 0 + MAX_VALUE = -128 + 0 + 127 = -1
         * ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE).sum();   // returns -1
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 1, (byte) 2, (byte) 3).average();          // returns 2.0
         * ByteTuple.of((byte) -5, (byte) -10, (byte) 0).average();       // returns -5.0
         *
         * // all same
         * ByteTuple.of((byte) 5, (byte) 5, (byte) 5).average();          // returns 5.0
         * // (-128 + 0 + 127) / 3 = -1/3 ~ -0.3333...
         * ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE).average();   // returns -1.0/3.0
         * }</pre>
         *
         * @return the average of all three byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3);
        }

        /**
         * Returns a new ByteTuple.ByteTuple3 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
         * ByteTuple.ByteTuple3 r = t.reverse();
         * r._1;   // returns 3
         * r._2;   // returns 2
         * r._3;   // returns 1
         *
         * // negative and boundary
         * ByteTuple.ByteTuple3 neg = ByteTuple.of((byte) -1, (byte) 0, (byte) 1).reverse();
         * neg._1;                                                                // returns 1
         * neg._3;                                                                // returns -1
         * ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE).reverse()._1;   // returns 127
         * }</pre>
         *
         * @return a new ByteTuple.ByteTuple3 with the elements in reverse order
         */
        @Override
        public ByteTuple3 reverse() {
            return new ByteTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple3 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
         * t.contains((byte) 1);    // returns true
         * t.contains((byte) 3);    // returns true
         * t.contains((byte) 0);    // returns false
         *
         * // boundary values
         * ByteTuple.ByteTuple3 tb = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE);
         * tb.contains(Byte.MIN_VALUE);   // returns true
         * tb.contains((byte) 0);         // returns true
         * tb.contains((byte) 1);         // returns false
         * }</pre>
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
         * <p>
         * Iterates through all three elements in order (_1, _2, then _3), executing the provided
         * consumer action for each element individually. This method is useful for side effects
         * that should be applied to each element separately.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
         *
         * // Print each element (prints "Value: 10", "Value: 20", "Value: 30" on separate lines)
         * tuple.forEach(b -> System.out.println("Value: " + b));   // prints each element
         *
         * // Collect to list
         * List<Byte> list = new ArrayList<>();
         * tuple.forEach(list::add);   // list contains [10, 20, 30]
         *
         * // Sum using accumulator
         * java.util.concurrent.atomic.AtomicInteger sum = new java.util.concurrent.atomic.AtomicInteger();
         * tuple.forEach(sum::addAndGet);   // sum is 60
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
        }

        /**
         * Applies the given tri-consumer to all three elements of this tuple.
         * <p>
         * This method executes the provided action with all three tuple elements as arguments.
         * It is useful for performing side effects or operations that require access to
         * all three elements simultaneously. Unlike {@link #forEach}, which processes elements
         * individually, this method processes all three elements together in a single call.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
         *
         * // Print all three values
         * tuple.accept((a, b, c) -> System.out.println(a + ", " + b + ", " + c));  // Output: 10, 20, 30
         *
         * // Calculate and store result
         * Map<String, Integer> results = new HashMap<>();
         * tuple.accept((a, b, c) -> results.put("sum", (int)(a + b + c)));   // stores the sum in results
         *
         * // Validate and log
         * tuple.accept((a, b, c) -> {
         *     if (a < b && b < c) {
         *         System.out.println("Values are in ascending order");   // prints when the condition holds
         *     }
         * });
         *
         * // Update external state
         * java.util.concurrent.atomic.AtomicInteger max = new java.util.concurrent.atomic.AtomicInteger();
         * tuple.accept((a, b, c) -> max.set(Math.max(Math.max(a, b), c)));   // stores the max in max
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the tri-consumer to apply to all three elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         * @see #forEach(Throwables.ByteConsumer)
         * @see #map(Throwables.ByteTriFunction)
         */
        public <E extends Exception> void accept(final Throwables.ByteTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given tri-function to all three elements of this tuple and returns the result.
         * <p>
         * This method transforms all three tuple elements into a single result value using the provided
         * tri-function. It is useful for combining, computing, or deriving new values from all three
         * tuple elements simultaneously. The result can be of any type, providing flexibility in how
         * the elements are processed and combined.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
         *
         * // Calculate sum
         * int sum = tuple.map((a, b, c) -> a + b + c);   // 60
         *
         * // Calculate average
         * double avg = tuple.map((a, b, c) -> (a + b + c) / 3.0);   // 20.0
         *
         * // Create formatted string
         * String formatted = tuple.map((a, b, c) ->
         *     String.format("RGB(%d, %d, %d)", a, b, c));   // "RGB(10, 20, 30)"
         *
         * // Find maximum
         * byte max = tuple.map((a, b, c) -> (byte) Math.max(Math.max(a, b), c));   // 30
         *
         * // Create custom object
         * java.awt.Color color = tuple.map((r, g, b) -> new java.awt.Color(r, g, b));
         *
         * // Complex calculation
         * boolean inRange = tuple.map((a, b, c) ->
         *     a >= 0 && a <= 100 && b >= 0 && b <= 100 && c >= 0 && c <= 100);
         * }</pre>
         *
         * @param <U> the type of the result value
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the tri-function to apply to all three elements, must not be {@code null}
         * @return the result of applying the tri-function to all three elements (may be {@code null})
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception during execution
         * @see #accept(Throwables.ByteTriConsumer)
         * @see #filter(Throwables.ByteTriPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.ByteTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if it matches the given tri-predicate,
         * otherwise returns an empty Optional.
         * <p>
         * This method conditionally returns the tuple based on a test of all three elements.
         * If the tri-predicate evaluates to {@code true} when applied to all three elements,
         * the tuple is wrapped in an Optional and returned. Otherwise, an empty Optional
         * is returned. This is useful for conditional processing and filtering tuples
         * based on relationships or conditions involving all three elements.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple3 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30);
         *
         * // Filter for ascending order
         * u.Optional<ByteTuple.ByteTuple3> ascending = tuple.filter((a, b, c) -> a < b && b < c);
         * // ascending contains the tuple (10 < 20 < 30)
         *
         * // Filter where sum exceeds threshold
         * u.Optional<ByteTuple.ByteTuple3> highSum = tuple.filter((a, b, c) -> (a + b + c) > 50);
         * // highSum contains the tuple (10 + 20 + 30 = 60 > 50)
         *
         * // Filter with range check
         * u.Optional<ByteTuple.ByteTuple3> inRange = tuple.filter((a, b, c) ->
         *     a >= 0 && b >= 0 && c >= 0 && a <= 100 && b <= 100 && c <= 100);
         * // inRange contains the tuple (all values in [0, 100])
         *
         * // Filter for equality
         * u.Optional<ByteTuple.ByteTuple3> allEqual = tuple.filter((a, b, c) -> a == b && b == c);
         * // allEqual is empty (10 != 20 != 30)
         *
         * // Chain with other operations
         * tuple.filter((a, b, c) -> a + b + c > 0)
         *      .map(t -> t.average())
         *      .ifPresent(System.out::println);   // Prints: 20.0
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the tri-predicate to test all three elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception during evaluation
         * @see #accept(Throwables.ByteTriConsumer)
         * @see #map(Throwables.ByteTriFunction)
         */
        public <E extends Exception> Optional<ByteTuple3> filter(final Throwables.ByteTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // hashCode = (31 * (31 * _1 + _2)) + _3
         * ByteTuple.of((byte) 1, (byte) 2, (byte) 3).hashCode();   // returns (31 * (31 * 1 + 2)) + 3 = 1026
         * ByteTuple.of((byte) 0, (byte) 0, (byte) 0).hashCode();   // returns 0
         *
         * // different order yields different hash
         * ByteTuple.of((byte) 1, (byte) 2, (byte) 3).hashCode();   // returns 1026
         * ByteTuple.of((byte) 3, (byte) 2, (byte) 1).hashCode();   // returns (31 * (31 * 3 + 2)) + 1 = 2946
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple3 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3);
         * t1.equals(ByteTuple.of((byte) 1, (byte) 2, (byte) 3));   // returns true
         * t1.equals(ByteTuple.of((byte) 3, (byte) 2, (byte) 1));   // returns false  (order matters)
         *
         * // reflexivity and null/type checks
         * t1.equals(t1);     // returns true
         * t1.equals(null);   // returns false
         * ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE)
         *         .equals(ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE));   // returns true
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple.ByteTuple3 with the same elements in the same order, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.of((byte) 1, (byte) 2, (byte) 3).toString();         // returns "(1, 2, 3)"
         * ByteTuple.of((byte) -5, (byte) -10, (byte) 0).toString();      // returns "(-5, -10, 0)"
         *
         * // zero and boundary
         * ByteTuple.of((byte) 0, (byte) 0, (byte) 0).toString();               // returns "(0, 0, 0)"
         * ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE).toString();   // returns "(-128, 0, 127)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
        }

        /**
         * Returns the internal array of byte elements.
         * The array is lazily initialized on first access.
         *
         * @return a byte array containing all elements in order
         */
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
     * ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
     * byte first = tuple._1;         // 10
     * byte fourth = tuple._4;        // 40
     * byte median = tuple.median();  // 20
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
         * int n = t.arity();   // returns 4
         *
         * ByteTuple.ByteTuple4 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 0, Byte.MAX_VALUE);
         * int n2 = neg.arity();   // returns 4
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 3, (byte) 1, (byte) 4, (byte) 2);
         * byte m = t.min();   // returns (byte) 1
         *
         * ByteTuple.ByteTuple4 boundary = ByteTuple.of(Byte.MAX_VALUE, (byte) 0, Byte.MIN_VALUE, (byte) 5);
         * byte m2 = boundary.min();   // returns Byte.MIN_VALUE (-128)
         *
         * ByteTuple.ByteTuple4 dup = ByteTuple.of((byte) -5, (byte) -5, (byte) -3, (byte) -3);
         * byte m3 = dup.min();   // returns (byte) -5
         *
         * ByteTuple.ByteTuple4 allSame = ByteTuple.of((byte) 7, (byte) 7, (byte) 7, (byte) 7);
         * byte m4 = allSame.min();   // returns (byte) 7
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 3, (byte) 1, (byte) 4, (byte) 2);
         * byte m = t.max();   // returns (byte) 4
         *
         * ByteTuple.ByteTuple4 boundary = ByteTuple.of(Byte.MAX_VALUE, (byte) 0, Byte.MIN_VALUE, (byte) 5);
         * byte m2 = boundary.max();   // returns Byte.MAX_VALUE (127)
         *
         * ByteTuple.ByteTuple4 dup = ByteTuple.of((byte) -5, (byte) -5, (byte) -3, (byte) -3);
         * byte m3 = dup.max();   // returns (byte) -3
         *
         * ByteTuple.ByteTuple4 allSame = ByteTuple.of((byte) 7, (byte) 7, (byte) 7, (byte) 7);
         * byte m4 = allSame.max();   // returns (byte) 7
         * }</pre>
         *
         * @return the largest of the four byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the median byte value in this tuple.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
         * byte med = t.median();   // returns (byte) 2  (lower middle of sorted [1,2,3,4])
         *
         * ByteTuple.ByteTuple4 rev = ByteTuple.of((byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * byte med2 = rev.median();   // returns (byte) 2  (same sorted result)
         *
         * ByteTuple.ByteTuple4 dup = ByteTuple.of((byte) 5, (byte) 5, (byte) 5, (byte) 5);
         * byte med3 = dup.median();   // returns (byte) 5
         *
         * ByteTuple.ByteTuple4 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 1, Byte.MAX_VALUE);
         * byte med4 = neg.median();   // returns (byte) 0  (lower middle of sorted [-128,0,1,127])
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
         * int s = t.sum();   // returns 10
         *
         * ByteTuple.ByteTuple4 neg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4);
         * int s2 = neg.sum();   // returns -10
         *
         * ByteTuple.ByteTuple4 boundary = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 1, Byte.MAX_VALUE);
         * int s3 = boundary.sum();   // returns 0
         *
         * ByteTuple.ByteTuple4 allSame = ByteTuple.of((byte) 10, (byte) 10, (byte) 10, (byte) 10);
         * int s4 = allSame.sum();   // returns 40
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
         * double avg = t.average();   // returns 2.5
         *
         * ByteTuple.ByteTuple4 neg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4);
         * double avg2 = neg.average();   // returns -2.5
         *
         * ByteTuple.ByteTuple4 boundary = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 1, Byte.MAX_VALUE);
         * double avg3 = boundary.average();   // returns 0.0
         *
         * ByteTuple.ByteTuple4 allSame = ByteTuple.of((byte) 10, (byte) 10, (byte) 10, (byte) 10);
         * double avg4 = allSame.average();   // returns 10.0
         * }</pre>
         *
         * @return the average of all four byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4);
        }

        /**
         * Returns a new ByteTuple.ByteTuple4 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
         * ByteTuple.ByteTuple4 reversed = tuple.reverse();   // returns (40, 30, 20, 10)
         *
         * ByteTuple.ByteTuple4 allSame = ByteTuple.of((byte) 5, (byte) 5, (byte) 5, (byte) 5);
         * ByteTuple.ByteTuple4 rev2 = allSame.reverse();   // returns (5, 5, 5, 5)
         *
         * ByteTuple.ByteTuple4 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 1, Byte.MAX_VALUE);
         * ByteTuple.ByteTuple4 rev3 = neg.reverse();   // returns (127, 1, 0, -128)
         *
         * // reverse of a reverse yields the original
         * ByteTuple.ByteTuple4 orig = ByteTuple.of((byte) -10, (byte) 20, (byte) -30, (byte) 40);
         * boolean same = orig.equals(orig.reverse().reverse());   // returns true
         * }</pre>
         *
         * @return a new ByteTuple.ByteTuple4 with the elements in reverse order
         */
        @Override
        public ByteTuple4 reverse() {
            return new ByteTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
         * boolean b1 = t.contains((byte) 3);   // returns true
         * boolean b2 = t.contains((byte) 5);   // returns false
         *
         * ByteTuple.ByteTuple4 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, Byte.MAX_VALUE);
         * boolean b3 = neg.contains(Byte.MIN_VALUE);   // returns true
         * boolean b4 = neg.contains((byte) 1);         // returns false
         * }</pre>
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
         * <p>
         * Iterates through all four elements in order (_1, _2, _3, then _4), executing the provided
         * consumer action for each element individually. This method is useful for side effects
         * that should be applied to each element separately.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40);
         *
         * // Print each element (each println produces its own line)
         * tuple.forEach(b -> System.out.println("Value: " + b));   // prints each element
         *
         * // Collect to list
         * List<Byte> list = new ArrayList<>();
         * tuple.forEach(list::add);   // list contains [10, 20, 30, 40]
         *
         * // Sum using a counter
         * int[] total = {0};
         * tuple.forEach(b -> total[0] += b);   // total[0] == 100
         *
         * // Null action throws IllegalArgumentException
         * // tuple.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
            action.accept(_4);
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
         * ByteTuple.ByteTuple4 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
         * boolean sameHash = (t1.hashCode() == t2.hashCode());   // returns true
         *
         * ByteTuple.ByteTuple4 t3 = ByteTuple.of((byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean diffHash = (t1.hashCode() == t3.hashCode());   // returns false (order matters)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
         * ByteTuple.ByteTuple4 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
         * boolean eq1 = t1.equals(t2);   // returns true
         *
         * ByteTuple.ByteTuple4 t3 = ByteTuple.of((byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean eq2 = t1.equals(t3);   // returns false (order matters)
         *
         * boolean eq3 = t1.equals(null);   // returns false
         *
         * ByteTuple.ByteTuple4 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, Byte.MAX_VALUE);
         * boolean eq4 = neg.equals(ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, Byte.MAX_VALUE));   // returns true
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple.ByteTuple4 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple4 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple4 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);
         * String s = t.toString();   // returns "(1, 2, 3, 4)"
         *
         * ByteTuple.ByteTuple4 neg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4);
         * String s2 = neg.toString();   // returns "(-1, -2, -3, -4)"
         *
         * ByteTuple.ByteTuple4 boundary = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, Byte.MAX_VALUE);
         * String s3 = boundary.toString();   // returns "(-128, 0, -1, 127)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3, element4)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
        }

        /**
         * Returns the internal array of byte elements.
         * The array is lazily initialized on first access.
         *
         * @return a byte array containing all elements in order
         */
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
     * ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
     * byte first = tuple._1;  // 10
     * byte fifth = tuple._5;  // 50
     * int sum = tuple.sum();  // 150
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
         * int n = t.arity();   // returns 5
         *
         * ByteTuple.ByteTuple5 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 0, (byte) 0, Byte.MAX_VALUE);
         * int n2 = neg.arity();   // returns 5
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 t = ByteTuple.of((byte) 3, (byte) 1, (byte) 5, (byte) 2, (byte) 4);
         * byte m = t.min();   // returns (byte) 1
         *
         * ByteTuple.ByteTuple5 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 0, (byte) 0, Byte.MAX_VALUE);
         * byte m2 = neg.min();   // returns Byte.MIN_VALUE (-128)
         *
         * ByteTuple.ByteTuple5 dup = ByteTuple.of((byte) -5, (byte) -5, (byte) -3, (byte) -3, (byte) -1);
         * byte m3 = dup.min();   // returns (byte) -5
         *
         * ByteTuple.ByteTuple5 allSame = ByteTuple.of((byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7);
         * byte m4 = allSame.min();   // returns (byte) 7
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 t = ByteTuple.of((byte) 3, (byte) 1, (byte) 5, (byte) 2, (byte) 4);
         * byte m = t.max();   // returns (byte) 5
         *
         * ByteTuple.ByteTuple5 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 0, (byte) 0, Byte.MAX_VALUE);
         * byte m2 = neg.max();   // returns Byte.MAX_VALUE (127)
         *
         * ByteTuple.ByteTuple5 dup = ByteTuple.of((byte) -5, (byte) -5, (byte) -3, (byte) -3, (byte) -1);
         * byte m3 = dup.max();   // returns (byte) -1
         *
         * ByteTuple.ByteTuple5 allSame = ByteTuple.of((byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7);
         * byte m4 = allSame.max();   // returns (byte) 7
         * }</pre>
         *
         * @return the largest of the five byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the median byte value in this tuple.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
         * byte med = t.median();   // returns (byte) 3  (middle of sorted [1,2,3,4,5])
         *
         * ByteTuple.ByteTuple5 rev = ByteTuple.of((byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * byte med2 = rev.median();   // returns (byte) 3  (same sorted result)
         *
         * ByteTuple.ByteTuple5 dup = ByteTuple.of((byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5);
         * byte med3 = dup.median();   // returns (byte) 5
         *
         * ByteTuple.ByteTuple5 neg = ByteTuple.of((byte) -10, (byte) -5, (byte) 0, (byte) 5, (byte) 10);
         * byte med4 = neg.median();   // returns (byte) 0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
         * int s = t.sum();   // returns 15
         *
         * ByteTuple.ByteTuple5 neg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5);
         * int s2 = neg.sum();   // returns -15
         *
         * ByteTuple.ByteTuple5 mixed = ByteTuple.of((byte) -10, (byte) -5, (byte) 0, (byte) 5, (byte) 10);
         * int s3 = mixed.sum();   // returns 0
         *
         * ByteTuple.ByteTuple5 allSame = ByteTuple.of((byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10);
         * int s4 = allSame.sum();   // returns 50
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
         * double avg = t.average();   // returns 3.0
         *
         * ByteTuple.ByteTuple5 neg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5);
         * double avg2 = neg.average();   // returns -3.0
         *
         * ByteTuple.ByteTuple5 mixed = ByteTuple.of((byte) -10, (byte) -5, (byte) 0, (byte) 5, (byte) 10);
         * double avg3 = mixed.average();   // returns 0.0
         *
         * ByteTuple.ByteTuple5 allSame = ByteTuple.of((byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10);
         * double avg4 = allSame.average();   // returns 10.0
         * }</pre>
         *
         * @return the average of all five byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5);
        }

        /**
         * Returns a new ByteTuple.ByteTuple5 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
         * ByteTuple.ByteTuple5 reversed = tuple.reverse();   // returns (50, 40, 30, 20, 10)
         *
         * ByteTuple.ByteTuple5 allSame = ByteTuple.of((byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5);
         * ByteTuple.ByteTuple5 rev2 = allSame.reverse();   // returns (5, 5, 5, 5, 5)
         *
         * ByteTuple.ByteTuple5 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 1, (byte) -1, Byte.MAX_VALUE);
         * ByteTuple.ByteTuple5 rev3 = neg.reverse();   // returns (127, -1, 1, 0, -128)
         *
         * // reverse of a reverse yields the original
         * ByteTuple.ByteTuple5 orig = ByteTuple.of((byte) -10, (byte) 20, (byte) -30, (byte) 40, (byte) -50);
         * boolean same = orig.equals(orig.reverse().reverse());   // returns true
         * }</pre>
         *
         * @return a new ByteTuple.ByteTuple5 with the elements in reverse order
         */
        @Override
        public ByteTuple5 reverse() {
            return new ByteTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
         * boolean b1 = t.contains((byte) 3);   // returns true
         * boolean b2 = t.contains((byte) 6);   // returns false
         *
         * ByteTuple.ByteTuple5 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, (byte) 1, Byte.MAX_VALUE);
         * boolean b3 = neg.contains(Byte.MAX_VALUE);   // returns true
         * boolean b4 = neg.contains((byte) 50);        // returns false
         * }</pre>
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
         * <p>
         * Iterates through all five elements in order (_1, _2, _3, _4, then _5), executing the provided
         * consumer action for each element individually. This method is useful for side effects
         * that should be applied to each element separately.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50);
         *
         * // Print each element
         * tuple.forEach(b -> System.out.println("Value: " + b));   // prints each element
         *
         * // Collect to list
         * List<Byte> list = new ArrayList<>();
         * tuple.forEach(list::add);   // list contains [10, 20, 30, 40, 50]
         *
         * // Sum using a counter
         * int[] total = {0};
         * tuple.forEach(b -> total[0] += b);   // total[0] == 150
         *
         * // Null action throws IllegalArgumentException
         * // tuple.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
            action.accept(_4);
            action.accept(_5);
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
         * ByteTuple.ByteTuple5 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
         * boolean sameHash = (t1.hashCode() == t2.hashCode());   // returns true
         *
         * ByteTuple.ByteTuple5 t3 = ByteTuple.of((byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean diffHash = (t1.hashCode() == t3.hashCode());   // returns false (order matters)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
         * ByteTuple.ByteTuple5 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
         * boolean eq1 = t1.equals(t2);   // returns true
         *
         * ByteTuple.ByteTuple5 t3 = ByteTuple.of((byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean eq2 = t1.equals(t3);   // returns false (order matters)
         *
         * boolean eq3 = t1.equals(null);   // returns false
         *
         * ByteTuple.ByteTuple5 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, (byte) 1, Byte.MAX_VALUE);
         * boolean eq4 = neg.equals(ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, (byte) 1, Byte.MAX_VALUE));   // returns true
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple.ByteTuple5 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple5 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4 && _5 == other._5;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple5 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
         * String s = t.toString();   // returns "(1, 2, 3, 4, 5)"
         *
         * ByteTuple.ByteTuple5 neg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5);
         * String s2 = neg.toString();   // returns "(-1, -2, -3, -4, -5)"
         *
         * ByteTuple.ByteTuple5 boundary = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, (byte) 1, Byte.MAX_VALUE);
         * String s3 = boundary.toString();   // returns "(-128, 0, -1, 1, 127)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3, element4, element5)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ")";
        }

        /**
         * Returns the internal array of byte elements.
         * The array is lazily initialized on first access.
         *
         * @return a byte array containing all elements in order
         */
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
     * ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
     * byte first = tuple._1;                            // 10
     * byte sixth = tuple._6;                            // 60
     * ByteTuple.ByteTuple6 reversed = tuple.reverse();  // (60, 50, 40, 30, 20, 10)
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
         * int n = t.arity();   // returns 6
         *
         * ByteTuple.ByteTuple6 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 0, (byte) 0, (byte) 0, Byte.MAX_VALUE);
         * int n2 = neg.arity();   // returns 6
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 t = ByteTuple.of((byte) 3, (byte) 1, (byte) 5, (byte) 2, (byte) 4, (byte) 6);
         * byte m = t.min();   // returns (byte) 1
         *
         * ByteTuple.ByteTuple6 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 0, (byte) 0, (byte) 0, Byte.MAX_VALUE);
         * byte m2 = neg.min();   // returns Byte.MIN_VALUE (-128)
         *
         * ByteTuple.ByteTuple6 dup = ByteTuple.of((byte) -5, (byte) -5, (byte) -3, (byte) -3, (byte) -1, (byte) -1);
         * byte m3 = dup.min();   // returns (byte) -5
         *
         * ByteTuple.ByteTuple6 allSame = ByteTuple.of((byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7);
         * byte m4 = allSame.min();   // returns (byte) 7
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 t = ByteTuple.of((byte) 3, (byte) 1, (byte) 5, (byte) 2, (byte) 4, (byte) 6);
         * byte m = t.max();   // returns (byte) 6
         *
         * ByteTuple.ByteTuple6 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 0, (byte) 0, (byte) 0, Byte.MAX_VALUE);
         * byte m2 = neg.max();   // returns Byte.MAX_VALUE (127)
         *
         * ByteTuple.ByteTuple6 dup = ByteTuple.of((byte) -5, (byte) -5, (byte) -3, (byte) -3, (byte) -1, (byte) -1);
         * byte m3 = dup.max();   // returns (byte) -1
         *
         * ByteTuple.ByteTuple6 allSame = ByteTuple.of((byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7);
         * byte m4 = allSame.max();   // returns (byte) 7
         * }</pre>
         *
         * @return the largest of the six byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median byte value in this tuple.
         * For tuples with an even number of elements, returns the lower middle element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
         * byte med = t.median();   // returns (byte) 3  (lower middle of sorted [1,2,3,4,5,6])
         *
         * ByteTuple.ByteTuple6 rev = ByteTuple.of((byte) 6, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * byte med2 = rev.median();   // returns (byte) 3  (same sorted result)
         *
         * ByteTuple.ByteTuple6 dup = ByteTuple.of((byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5);
         * byte med3 = dup.median();   // returns (byte) 5
         *
         * ByteTuple.ByteTuple6 neg = ByteTuple.of((byte) -10, (byte) -5, (byte) -1, (byte) 1, (byte) 5, (byte) 10);
         * byte med4 = neg.median();   // returns (byte) -1  (lower middle of sorted [-10,-5,-1,1,5,10])
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
         * int s = t.sum();   // returns 21
         *
         * ByteTuple.ByteTuple6 neg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6);
         * int s2 = neg.sum();   // returns -21
         *
         * ByteTuple.ByteTuple6 mixed = ByteTuple.of((byte) -10, (byte) -5, (byte) -1, (byte) 1, (byte) 5, (byte) 10);
         * int s3 = mixed.sum();   // returns 0
         *
         * ByteTuple.ByteTuple6 allSame = ByteTuple.of((byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10);
         * int s4 = allSame.sum();   // returns 60
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
         * double avg = t.average();   // returns 3.5
         *
         * ByteTuple.ByteTuple6 neg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6);
         * double avg2 = neg.average();   // returns -3.5
         *
         * ByteTuple.ByteTuple6 mixed = ByteTuple.of((byte) -10, (byte) -5, (byte) -1, (byte) 1, (byte) 5, (byte) 10);
         * double avg3 = mixed.average();   // returns 0.0
         *
         * ByteTuple.ByteTuple6 allSame = ByteTuple.of((byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10, (byte) 10);
         * double avg4 = allSame.average();   // returns 10.0
         * }</pre>
         *
         * @return the average of all six byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns a new ByteTuple.ByteTuple6 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
         * ByteTuple.ByteTuple6 reversed = tuple.reverse();   // returns (60, 50, 40, 30, 20, 10)
         *
         * ByteTuple.ByteTuple6 allSame = ByteTuple.of((byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5);
         * ByteTuple.ByteTuple6 rev2 = allSame.reverse();   // returns (5, 5, 5, 5, 5, 5)
         *
         * ByteTuple.ByteTuple6 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 1, (byte) -1, (byte) 0, Byte.MAX_VALUE);
         * ByteTuple.ByteTuple6 rev3 = neg.reverse();   // returns (127, 0, -1, 1, 0, -128)
         *
         * // reverse of a reverse yields the original
         * ByteTuple.ByteTuple6 orig = ByteTuple.of((byte) -10, (byte) 20, (byte) -30, (byte) 40, (byte) -50, (byte) 60);
         * boolean same = orig.equals(orig.reverse().reverse());   // returns true
         * }</pre>
         *
         * @return a new ByteTuple.ByteTuple6 with the elements in reverse order
         */
        @Override
        public ByteTuple6 reverse() {
            return new ByteTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
         * boolean b1 = t.contains((byte) 4);   // returns true
         * boolean b2 = t.contains((byte) 7);   // returns false
         *
         * ByteTuple.ByteTuple6 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, (byte) 1, (byte) 0, Byte.MAX_VALUE);
         * boolean b3 = neg.contains(Byte.MIN_VALUE);   // returns true
         * boolean b4 = neg.contains((byte) 50);        // returns false
         * }</pre>
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
         * <p>
         * Iterates through all six elements in order (_1 through _6), executing the provided
         * consumer action for each element individually. This method is useful for side effects
         * that should be applied to each element separately.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60);
         *
         * // Print each element
         * tuple.forEach(b -> System.out.println("Value: " + b));   // prints each element
         *
         * // Collect to list
         * List<Byte> list = new ArrayList<>();
         * tuple.forEach(list::add);   // list contains [10, 20, 30, 40, 50, 60]
         *
         * // Sum using a counter
         * int[] total = {0};
         * tuple.forEach(b -> total[0] += b);   // total[0] == 210
         *
         * // Null action throws IllegalArgumentException
         * // tuple.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> action) throws E {
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
         * ByteTuple.ByteTuple6 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
         * boolean sameHash = (t1.hashCode() == t2.hashCode());   // returns true
         *
         * ByteTuple.ByteTuple6 t3 = ByteTuple.of((byte) 6, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean diffHash = (t1.hashCode() == t3.hashCode());   // returns false (order matters)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
         * ByteTuple.ByteTuple6 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
         * boolean eq1 = t1.equals(t2);   // returns true
         *
         * ByteTuple.ByteTuple6 t3 = ByteTuple.of((byte) 6, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean eq2 = t1.equals(t3);   // returns false (order matters)
         *
         * boolean eq3 = t1.equals(null);   // returns false
         *
         * ByteTuple.ByteTuple6 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, (byte) 1, (byte) 0, Byte.MAX_VALUE);
         * boolean eq4 = neg.equals(ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, (byte) 1, (byte) 0, Byte.MAX_VALUE));   // returns true
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple.ByteTuple6 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple6 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4 && _5 == other._5 && _6 == other._6;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple6 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);
         * String s = t.toString();   // returns "(1, 2, 3, 4, 5, 6)"
         *
         * ByteTuple.ByteTuple6 neg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6);
         * String s2 = neg.toString();   // returns "(-1, -2, -3, -4, -5, -6)"
         *
         * ByteTuple.ByteTuple6 boundary = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) -1, (byte) 1, (byte) 0, Byte.MAX_VALUE);
         * String s3 = boundary.toString();   // returns "(-128, 0, -1, 1, 0, 127)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3, element4, element5, element6)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ")";
        }

        /**
         * Returns the internal array of byte elements.
         * The array is lazily initialized on first access.
         *
         * @return a byte array containing all elements in order
         */
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
     * ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
     * byte first = tuple._1;           // 10
     * byte seventh = tuple._7;         // 70
     * byte[] array = tuple.toArray();  // [10, 20, 30, 40, 50, 60, 70]
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * int n = t.arity();   // returns 7
         *
         * ByteTuple.ByteTuple7 neg = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, Byte.MAX_VALUE);
         * int n2 = neg.arity();   // returns 7
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * byte result = t.min();   // returns 1
         *
         * ByteTuple.ByteTuple7 t2 = ByteTuple.of((byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5);
         * byte result2 = t2.min();   // returns 5 (all same)
         *
         * ByteTuple.ByteTuple7 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1, Byte.MAX_VALUE, (byte) 64, (byte) 32);
         * byte result3 = t3.min();   // returns -128 (Byte.MIN_VALUE)
         *
         * ByteTuple.ByteTuple7 t4 = ByteTuple.of((byte) -7, (byte) -6, (byte) -5, (byte) -4, (byte) -3, (byte) -2, (byte) -1);
         * byte result4 = t4.min();   // returns -7
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * byte result = t.max();   // returns 7
         *
         * ByteTuple.ByteTuple7 t2 = ByteTuple.of((byte) -3, (byte) -3, (byte) -3, (byte) -3, (byte) -3, (byte) -3, (byte) -3);
         * byte result2 = t2.max();   // returns -3 (all same)
         *
         * ByteTuple.ByteTuple7 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1, Byte.MAX_VALUE, (byte) 64, (byte) 32);
         * byte result3 = t3.max();   // returns 127 (Byte.MAX_VALUE)
         *
         * ByteTuple.ByteTuple7 t4 = ByteTuple.of((byte) -7, (byte) -6, (byte) -5, (byte) -4, (byte) -3, (byte) -2, (byte) -1);
         * byte result4 = t4.max();   // returns -1
         * }</pre>
         *
         * @return the largest of the seven byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the median byte value in this tuple.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * byte result = t.median();   // returns 4 (middle of sorted [1,2,3,4,5,6,7])
         *
         * ByteTuple.ByteTuple7 t2 = ByteTuple.of((byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7);
         * byte result2 = t2.median();   // returns 7 (all same)
         *
         * ByteTuple.ByteTuple7 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1, Byte.MAX_VALUE, (byte) 64, (byte) 32);
         * byte result3 = t3.median();   // returns 1 (middle of sorted [-128,-1,0,1,32,64,127])
         *
         * ByteTuple.ByteTuple7 t4 = ByteTuple.of((byte) 7, (byte) 1, (byte) 5, (byte) 3, (byte) 9, (byte) 2, (byte) 4);
         * byte result4 = t4.median();   // returns 4 (middle of sorted [1,2,3,4,5,7,9])
         * }</pre>
         *
         * @return the median byte value
         */
        @Override
        public byte median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         * The result is returned as an {@code int} to accommodate sums that may exceed
         * the {@code byte} range (e.g., 127 * 7 = 889 fits in {@code int} but not {@code byte}).
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * int result = t.sum();   // returns 28
         *
         * ByteTuple.ByteTuple7 t2 = ByteTuple.of((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * int result2 = t2.sum();   // returns 0
         *
         * ByteTuple.ByteTuple7 t3 = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7);
         * int result3 = t3.sum();   // returns -28
         *
         * ByteTuple.ByteTuple7 t4 = ByteTuple.of(Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE);
         * int result4 = t4.sum();   // returns 889 (127 * 7, safely stored as int)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * double result = t.average();   // returns 4.0
         *
         * ByteTuple.ByteTuple7 t2 = ByteTuple.of((byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5);
         * double result2 = t2.average();   // returns 5.0
         *
         * ByteTuple.ByteTuple7 t3 = ByteTuple.of((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * double result3 = t3.average();   // returns 0.0
         *
         * ByteTuple.ByteTuple7 t4 = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7);
         * double result4 = t4.average();   // returns -4.0
         * }</pre>
         *
         * @return the average of all seven byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns a new ByteTuple.ByteTuple7 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * ByteTuple.ByteTuple7 r = t.reverse();
         * // r.toString() returns "(7, 6, 5, 4, 3, 2, 1)"
         *
         * ByteTuple.ByteTuple7 t2 = ByteTuple.of((byte) -3, (byte) -2, (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3);
         * ByteTuple.ByteTuple7 r2 = t2.reverse();
         * // r2.toString() returns "(3, 2, 1, 0, -1, -2, -3)"
         *
         * // boundary values are preserved
         * ByteTuple.ByteTuple7 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * ByteTuple.ByteTuple7 r3 = t3.reverse();
         * // r3._7 == Byte.MIN_VALUE, r3._5 == Byte.MAX_VALUE
         *
         * // palindrome: reverse equals original
         * ByteTuple.ByteTuple7 t4 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * ByteTuple.ByteTuple7 r4 = t4.reverse();
         * // r4.equals(t4) == true
         * }</pre>
         *
         * @return a new ByteTuple.ByteTuple7 with the elements in reverse order
         */
        @Override
        public ByteTuple7 reverse() {
            return new ByteTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70);
         * boolean a = t.contains((byte) 10);    // returns true (first element)
         * boolean b = t.contains((byte) 70);    // returns true (last element)
         * boolean c = t.contains((byte) 0);     // returns false
         * boolean d = t.contains((byte) 100);   // returns false
         *
         * // boundary values
         * ByteTuple.ByteTuple7 t2 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1, Byte.MAX_VALUE, (byte) 64, (byte) 32);
         * boolean e = t2.contains(Byte.MIN_VALUE);   // returns true
         * boolean f = t2.contains(Byte.MAX_VALUE);   // returns true
         * boolean g = t2.contains((byte) 2);         // returns false
         * }</pre>
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
         * <p>
         * Iterates through all seven elements in order (_1 through _7), executing the provided
         * consumer action for each element individually. This method is useful for side effects
         * that should be applied to each element separately.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         *
         * // Collect elements into a list
         * List<Byte> list = new ArrayList<>();
         * tuple.forEach(list::add);   // list contains [1, 2, 3, 4, 5, 6, 7]
         *
         * // Compute running sum
         * int[] sum = {0};
         * tuple.forEach(b -> sum[0] += b);   // sum[0] == 28
         *
         * // Passing null throws IllegalArgumentException
         * // tuple.forEach(null);   // throws IllegalArgumentException
         *
         * // Boundary values are passed as-is
         * ByteTuple.ByteTuple7 t2 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1, Byte.MAX_VALUE, (byte) 64, (byte) 32);
         * List<Byte> list2 = new ArrayList<>();
         * t2.forEach(list2::add);   // list2 contains [-128, -1, 0, 1, 127, 64, 32]
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> action) throws E {
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * ByteTuple.ByteTuple7 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * // equal tuples have same hash code
         * boolean sameHash = t1.hashCode() == t2.hashCode();   // returns true
         *
         * // hash code is self-consistent
         * boolean selfConsistent = t1.hashCode() == t1.hashCode();   // returns true
         *
         * // different element order produces different hash codes
         * ByteTuple.ByteTuple7 t3 = ByteTuple.of((byte) 7, (byte) 6, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean diffHash = t1.hashCode() == t3.hashCode();   // returns false
         *
         * // negative values produce a well-defined int hash code
         * ByteTuple.ByteTuple7 tNeg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7);
         * boolean negDistinct = tNeg.hashCode() != t1.hashCode();   // returns true
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * ByteTuple.ByteTuple7 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * boolean eq = t1.equals(t2);   // returns true
         *
         * ByteTuple.ByteTuple7 t3 = ByteTuple.of((byte) 7, (byte) 6, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean neq = t1.equals(t3);   // returns false (different order)
         *
         * boolean self = t1.equals(t1);   // returns true (same reference)
         *
         * boolean nullCase = t1.equals(null);   // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple.ByteTuple7 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple7 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4 && _5 == other._5 && _6 == other._6 && _7 == other._7;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple7 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
         * String s = t.toString();   // returns "(1, 2, 3, 4, 5, 6, 7)"
         *
         * ByteTuple.ByteTuple7 t2 = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7);
         * String s2 = t2.toString();   // returns "(-1, -2, -3, -4, -5, -6, -7)"
         *
         * ByteTuple.ByteTuple7 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * String s3 = t3.toString();   // returns "(-128, 0, 127, 0, 0, 0, 0)"
         *
         * ByteTuple.ByteTuple7 t4 = ByteTuple.of((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * String s4 = t4.toString();   // returns "(0, 0, 0, 0, 0, 0, 0)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, ..., element7)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ")";
        }

        /**
         * Returns the internal array of byte elements.
         * The array is lazily initialized on first access.
         *
         * @return a byte array containing all elements in order
         */
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
     * ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
     * byte first = tuple._1;   // 10
     * byte eighth = tuple._8;  // 80
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple8 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * int n = t.arity();   // returns 8
         *
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
         * int n2 = t2.arity();   // returns 8
         *
         * // arity is independent of element values - negative values
         * ByteTuple.ByteTuple8 tNeg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7, (byte) -8);
         * int n3 = tNeg.arity();   // returns 8
         *
         * // boundary values make no difference
         * ByteTuple.ByteTuple8 tBoundary = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1,
         *         Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16);
         * int n4 = tBoundary.arity();   // returns 8
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple8 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * byte result = t.min();   // returns 1
         *
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of((byte) -5, (byte) -5, (byte) -5, (byte) -5, (byte) -5, (byte) -5, (byte) -5, (byte) -5);
         * byte result2 = t2.min();   // returns -5 (all same)
         *
         * ByteTuple.ByteTuple8 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1, Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16);
         * byte result3 = t3.min();   // returns -128 (Byte.MIN_VALUE)
         *
         * ByteTuple.ByteTuple8 t4 = ByteTuple.of((byte) -8, (byte) -7, (byte) -6, (byte) -5, (byte) -4, (byte) -3, (byte) -2, (byte) -1);
         * byte result4 = t4.min();   // returns -8
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple8 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * byte result = t.max();   // returns 8
         *
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of((byte) -8, (byte) -7, (byte) -6, (byte) -5, (byte) -4, (byte) -3, (byte) -2, (byte) -1);
         * byte result2 = t2.max();   // returns -1
         *
         * ByteTuple.ByteTuple8 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1, Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16);
         * byte result3 = t3.max();   // returns 127 (Byte.MAX_VALUE)
         *
         * ByteTuple.ByteTuple8 t4 = ByteTuple.of((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * byte result4 = t4.max();   // returns 0
         * }</pre>
         *
         * @return the largest of the eight byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the median byte value in this tuple.
         * For tuples with an even number of elements, returns the lower middle element
         * (the element at position {@code n/2 - 1} in the sorted order, 0-indexed).
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // 1-8 sorted -> [1,2,3,4,5,6,7,8]; even count: lower middle (index 3) = 4
         * ByteTuple.ByteTuple8 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * byte result = t.median();   // returns 4
         *
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of((byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3);
         * byte result2 = t2.median();   // returns 3 (all same)
         *
         * // descending order - same result as ascending
         * ByteTuple.ByteTuple8 t3 = ByteTuple.of((byte) 8, (byte) 7, (byte) 6, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * byte result3 = t3.median();   // returns 4
         *
         * ByteTuple.ByteTuple8 t4 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1, Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16);
         * byte result4 = t4.median();   // returns 1 (lower middle of sorted [-128,-1,0,1,16,32,64,127])
         * }</pre>
         *
         * @return the median byte value
         */
        @Override
        public byte median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         * The result is returned as an {@code int} to accommodate sums that may exceed
         * the {@code byte} range (e.g., 127 * 8 = 1016 fits in {@code int} but not {@code byte}).
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple8 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * int result = t.sum();   // returns 36
         *
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * int result2 = t2.sum();   // returns 0
         *
         * ByteTuple.ByteTuple8 t3 = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7, (byte) -8);
         * int result3 = t3.sum();   // returns -36
         *
         * ByteTuple.ByteTuple8 t4 = ByteTuple.of(Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE,
         *         Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE);
         * int result4 = t4.sum();   // returns 1016 (127 * 8, safely stored as int)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple8 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * double result = t.average();   // returns 4.5
         *
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of((byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6);
         * double result2 = t2.average();   // returns 6.0
         *
         * ByteTuple.ByteTuple8 t3 = ByteTuple.of((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * double result3 = t3.average();   // returns 0.0
         *
         * ByteTuple.ByteTuple8 t4 = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7, (byte) -8);
         * double result4 = t4.average();   // returns -4.5
         * }</pre>
         *
         * @return the average of all eight byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns a new ByteTuple.ByteTuple8 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple8 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * ByteTuple.ByteTuple8 r = t.reverse();
         * // r.toString() returns "(8, 7, 6, 5, 4, 3, 2, 1)"
         *
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of((byte) -4, (byte) -3, (byte) -2, (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3);
         * ByteTuple.ByteTuple8 r2 = t2.reverse();
         * // r2.toString() returns "(3, 2, 1, 0, -1, -2, -3, -4)"
         *
         * // boundary values preserved
         * ByteTuple.ByteTuple8 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, Byte.MAX_VALUE);
         * ByteTuple.ByteTuple8 r3 = t3.reverse();
         * // r3._1 == Byte.MAX_VALUE, r3._8 == Byte.MIN_VALUE
         *
         * // palindrome: reverse equals original
         * ByteTuple.ByteTuple8 t4 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * ByteTuple.ByteTuple8 r4 = t4.reverse();
         * // r4.equals(t4) == true
         * }</pre>
         *
         * @return a new ByteTuple.ByteTuple8 with the elements in reverse order
         */
        @Override
        public ByteTuple8 reverse() {
            return new ByteTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple8 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80);
         * boolean a = t.contains((byte) 10);    // returns true (first element)
         * boolean b = t.contains((byte) 80);    // returns true (last element)
         * boolean c = t.contains((byte) 0);     // returns false
         * boolean d = t.contains((byte) -1);    // returns false
         *
         * // boundary values
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1, Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16);
         * boolean e = t2.contains(Byte.MIN_VALUE);   // returns true
         * boolean f = t2.contains(Byte.MAX_VALUE);   // returns true
         * boolean g = t2.contains((byte) 2);         // returns false
         * }</pre>
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
         * <p>
         * Iterates through all eight elements in order (_1 through _8), executing the provided
         * consumer action for each element individually. This method is useful for side effects
         * that should be applied to each element separately.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple8 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         *
         * // Collect elements into a list
         * List<Byte> list = new ArrayList<>();
         * tuple.forEach(list::add);   // list contains [1, 2, 3, 4, 5, 6, 7, 8]
         *
         * // Compute sum via forEach
         * int[] sum = {0};
         * tuple.forEach(b -> sum[0] += b);   // sum[0] == 36
         *
         * // Passing null throws IllegalArgumentException
         * // tuple.forEach(null);   // throws IllegalArgumentException
         *
         * // Boundary values are passed as-is
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1, Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16);
         * List<Byte> list2 = new ArrayList<>();
         * t2.forEach(list2::add);   // list2 contains [-128, -1, 0, 1, 127, 64, 32, 16]
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> action) throws E {
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple8 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * // equal tuples have same hash code
         * boolean sameHash = t1.hashCode() == t2.hashCode();   // returns true
         *
         * // hash code is self-consistent
         * boolean selfConsistent = t1.hashCode() == t1.hashCode();   // returns true
         *
         * // different element order produces different hash codes
         * ByteTuple.ByteTuple8 t3 = ByteTuple.of((byte) 8, (byte) 7, (byte) 6, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean diffHash = t1.hashCode() == t3.hashCode();   // returns false
         *
         * // negative values produce a well-defined int hash code, distinct from positive counterpart
         * ByteTuple.ByteTuple8 tNeg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7, (byte) -8);
         * boolean negDistinct = tNeg.hashCode() != t1.hashCode();   // returns true
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple8 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * boolean eq = t1.equals(t2);   // returns true
         *
         * ByteTuple.ByteTuple8 t3 = ByteTuple.of((byte) 8, (byte) 7, (byte) 6, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean neq = t1.equals(t3);   // returns false (different order)
         *
         * boolean self = t1.equals(t1);   // returns true (same reference)
         *
         * boolean nullCase = t1.equals(null);   // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple.ByteTuple8 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple8 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4 && _5 == other._5 && _6 == other._6 && _7 == other._7
                        && _8 == other._8;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple8 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8);
         * String s = t.toString();   // returns "(1, 2, 3, 4, 5, 6, 7, 8)"
         *
         * ByteTuple.ByteTuple8 t2 = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7, (byte) -8);
         * String s2 = t2.toString();   // returns "(-1, -2, -3, -4, -5, -6, -7, -8)"
         *
         * ByteTuple.ByteTuple8 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * String s3 = t3.toString();   // returns "(-128, 0, 127, 0, 0, 0, 0, 0)"
         *
         * ByteTuple.ByteTuple8 t4 = ByteTuple.of((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * String s4 = t4.toString();   // returns "(0, 0, 0, 0, 0, 0, 0, 0)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, ..., element8)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")";
        }

        /**
         * Returns the internal array of byte elements.
         * The array is lazily initialized on first access.
         *
         * @return a byte array containing all elements in order
         */
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
     * ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
     * byte first = tuple._1;      // 10
     * byte ninth = tuple._9;      // 90
     * int arity = tuple.arity();  // 9
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple9 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * int n = t.arity();   // returns 9
         *
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
         * int n2 = t2.arity();   // returns 9
         *
         * // arity is independent of element values - negative values
         * ByteTuple.ByteTuple9 tNeg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7, (byte) -8, (byte) -9);
         * int n3 = tNeg.arity();   // returns 9
         *
         * // boundary values make no difference
         * ByteTuple.ByteTuple9 tBoundary = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1,
         *         Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16, (byte) 8);
         * int n4 = tBoundary.arity();   // returns 9
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple9 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * byte result = t.min();   // returns 1
         *
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of((byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9);
         * byte result2 = t2.min();   // returns -9 (all same)
         *
         * ByteTuple.ByteTuple9 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1,
         *         Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16, (byte) 8);
         * byte result3 = t3.min();   // returns -128 (Byte.MIN_VALUE)
         *
         * ByteTuple.ByteTuple9 t4 = ByteTuple.of((byte) -9, (byte) -8, (byte) -7, (byte) -6, (byte) -5,
         *         (byte) -4, (byte) -3, (byte) -2, (byte) -1);
         * byte result4 = t4.min();   // returns -9
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple9 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * byte result = t.max();   // returns 9
         *
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of((byte) 99, (byte) 99, (byte) 99, (byte) 99, (byte) 99, (byte) 99, (byte) 99, (byte) 99, (byte) 99);
         * byte result2 = t2.max();   // returns 99 (all same)
         *
         * ByteTuple.ByteTuple9 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1,
         *         Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16, (byte) 8);
         * byte result3 = t3.max();   // returns 127 (Byte.MAX_VALUE)
         *
         * ByteTuple.ByteTuple9 t4 = ByteTuple.of((byte) -9, (byte) -8, (byte) -7, (byte) -6, (byte) -5,
         *         (byte) -4, (byte) -3, (byte) -2, (byte) -1);
         * byte result4 = t4.max();   // returns -1
         * }</pre>
         *
         * @return the largest of the nine byte values
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the median byte value in this tuple.
         * For tuples with an odd number of elements, returns the middle value when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // 1-9 sorted -> [1,2,3,4,5,6,7,8,9]; middle index 4 = 5
         * ByteTuple.ByteTuple9 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * byte result = t.median();   // returns 5
         *
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of((byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4);
         * byte result2 = t2.median();   // returns 4 (all same)
         *
         * // boundary mix: sorted [-128,-1,0,1,8,16,32,64,127] -> middle index 4 = 8
         * ByteTuple.ByteTuple9 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1,
         *         Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16, (byte) 8);
         * byte result3 = t3.median();   // returns 8
         *
         * // descending order - same result as ascending
         * ByteTuple.ByteTuple9 t4 = ByteTuple.of((byte) 9, (byte) 8, (byte) 7, (byte) 6, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * byte result4 = t4.median();   // returns 5
         * }</pre>
         *
         * @return the median byte value
         */
        @Override
        public byte median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of all byte values in this tuple.
         * The result is returned as an {@code int} to accommodate sums that may exceed
         * the {@code byte} range (e.g., 127 * 9 = 1143 fits in {@code int} but not {@code byte}).
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple9 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * int result = t.sum();   // returns 45
         *
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * int result2 = t2.sum();   // returns 0
         *
         * ByteTuple.ByteTuple9 t3 = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7, (byte) -8, (byte) -9);
         * int result3 = t3.sum();   // returns -45
         *
         * ByteTuple.ByteTuple9 t4 = ByteTuple.of(Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE,
         *         Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE);
         * int result4 = t4.sum();   // returns 1143 (127 * 9, safely stored as int)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple9 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * double result = t.average();   // returns 5.0
         *
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of((byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3);
         * double result2 = t2.average();   // returns 3.0
         *
         * ByteTuple.ByteTuple9 t3 = ByteTuple.of((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * double result3 = t3.average();   // returns 0.0
         *
         * ByteTuple.ByteTuple9 t4 = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7, (byte) -8, (byte) -9);
         * double result4 = t4.average();   // returns -5.0
         * }</pre>
         *
         * @return the average of all nine byte values as a double
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns a new ByteTuple.ByteTuple9 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple9 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * ByteTuple.ByteTuple9 r = t.reverse();
         * // r.toString() returns "(9, 8, 7, 6, 5, 4, 3, 2, 1)"
         *
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of((byte) -4, (byte) -3, (byte) -2, (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4);
         * ByteTuple.ByteTuple9 r2 = t2.reverse();
         * // r2.toString() returns "(4, 3, 2, 1, 0, -1, -2, -3, -4)"
         *
         * // boundary values preserved
         * ByteTuple.ByteTuple9 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, Byte.MAX_VALUE);
         * ByteTuple.ByteTuple9 r3 = t3.reverse();
         * // r3._1 == Byte.MAX_VALUE, r3._9 == Byte.MIN_VALUE
         *
         * // palindrome: reverse equals original
         * ByteTuple.ByteTuple9 t4 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * ByteTuple.ByteTuple9 r4 = t4.reverse();
         * // r4.equals(t4) == true
         * }</pre>
         *
         * @return a new ByteTuple.ByteTuple9 with the elements in reverse order
         */
        @Override
        public ByteTuple9 reverse() {
            return new ByteTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified byte value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple9 t = ByteTuple.of((byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50, (byte) 60, (byte) 70, (byte) 80, (byte) 90);
         * boolean a = t.contains((byte) 10);    // returns true (first element)
         * boolean b = t.contains((byte) 90);    // returns true (last element)
         * boolean c = t.contains((byte) 0);     // returns false
         * boolean d = t.contains((byte) -1);    // returns false
         *
         * // boundary values
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1,
         *         Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16, (byte) 8);
         * boolean e = t2.contains(Byte.MIN_VALUE);   // returns true
         * boolean f = t2.contains(Byte.MAX_VALUE);   // returns true
         * boolean g = t2.contains((byte) 2);         // returns false
         * }</pre>
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
         * <p>
         * Iterates through all nine elements in order (_1 through _9), executing the provided
         * consumer action for each element individually. This method is useful for side effects
         * that should be applied to each element separately.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple9 tuple = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         *
         * // Collect elements into a list
         * List<Byte> list = new ArrayList<>();
         * tuple.forEach(list::add);   // list contains [1, 2, 3, 4, 5, 6, 7, 8, 9]
         *
         * // Compute sum via forEach
         * int[] sum = {0};
         * tuple.forEach(b -> sum[0] += b);   // sum[0] == 45
         *
         * // Passing null throws IllegalArgumentException
         * // tuple.forEach(null);   // throws IllegalArgumentException
         *
         * // Boundary values are passed as-is
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1,
         *         Byte.MAX_VALUE, (byte) 64, (byte) 32, (byte) 16, (byte) 8);
         * List<Byte> list2 = new ArrayList<>();
         * t2.forEach(list2::add);   // list2 contains [-128, -1, 0, 1, 127, 64, 32, 16, 8]
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception during execution
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> action) throws E {
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple9 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * // equal tuples have same hash code
         * boolean sameHash = t1.hashCode() == t2.hashCode();   // returns true
         *
         * // hash code is self-consistent
         * boolean selfConsistent = t1.hashCode() == t1.hashCode();   // returns true
         *
         * // different element order produces different hash codes
         * ByteTuple.ByteTuple9 t3 = ByteTuple.of((byte) 9, (byte) 8, (byte) 7, (byte) 6, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean diffHash = t1.hashCode() == t3.hashCode();   // returns false
         *
         * // negative values produce a well-defined int hash code, distinct from positive counterpart
         * ByteTuple.ByteTuple9 tNeg = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5,
         *         (byte) -6, (byte) -7, (byte) -8, (byte) -9);
         * boolean negDistinct = tNeg.hashCode() != t1.hashCode();   // returns true
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple9 t1 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * boolean eq = t1.equals(t2);   // returns true
         *
         * ByteTuple.ByteTuple9 t3 = ByteTuple.of((byte) 9, (byte) 8, (byte) 7, (byte) 6, (byte) 5, (byte) 4, (byte) 3, (byte) 2, (byte) 1);
         * boolean neq = t1.equals(t3);   // returns false (different order)
         *
         * boolean self = t1.equals(t1);   // returns true (same reference)
         *
         * boolean nullCase = t1.equals(null);   // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a ByteTuple.ByteTuple9 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple9 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3 && _4 == other._4 && _5 == other._5 && _6 == other._6 && _7 == other._7
                        && _8 == other._8 && _9 == other._9;
            }
        }

        /**
         * Returns a string representation of this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ByteTuple.ByteTuple9 t = ByteTuple.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9);
         * String s = t.toString();   // returns "(1, 2, 3, 4, 5, 6, 7, 8, 9)"
         *
         * ByteTuple.ByteTuple9 t2 = ByteTuple.of((byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7, (byte) -8, (byte) -9);
         * String s2 = t2.toString();   // returns "(-1, -2, -3, -4, -5, -6, -7, -8, -9)"
         *
         * ByteTuple.ByteTuple9 t3 = ByteTuple.of(Byte.MIN_VALUE, (byte) 0, Byte.MAX_VALUE, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * String s3 = t3.toString();   // returns "(-128, 0, 127, 0, 0, 0, 0, 0, 0)"
         *
         * ByteTuple.ByteTuple9 t4 = ByteTuple.of((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
         * String s4 = t4.toString();   // returns "(0, 0, 0, 0, 0, 0, 0, 0, 0)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, ..., element9)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ")";
        }

        /**
         * Returns the internal array of byte elements.
         * The array is lazily initialized on first access.
         *
         * @return a byte array containing all elements in order
         */
        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
