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
import com.landawn.abacus.util.ShortTuple.ShortTuple0;
import com.landawn.abacus.util.ShortTuple.ShortTuple1;
import com.landawn.abacus.util.ShortTuple.ShortTuple2;
import com.landawn.abacus.util.ShortTuple.ShortTuple3;
import com.landawn.abacus.util.ShortTuple.ShortTuple4;
import com.landawn.abacus.util.ShortTuple.ShortTuple5;
import com.landawn.abacus.util.ShortTuple.ShortTuple6;
import com.landawn.abacus.util.ShortTuple.ShortTuple7;
import com.landawn.abacus.util.ShortTuple.ShortTuple8;
import com.landawn.abacus.util.ShortTuple.ShortTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.ShortStream;

/**
 * Base class for immutable tuples of primitive {@code short} values.
 *
 * <p>The nested tuple types model fixed arities from 0 through 9. Factory methods such as
 * {@link #copyOf(short[])} and the {@code of(...)} overloads select the matching subtype, while the
 * base class supplies aggregate, reversal, containment, and functional helper operations.</p>
 *
 * <p>This sealed base class permits only the built-in arity-specific nested tuple types.</p>
 *
 * <p>All {@code short} arithmetic in this class follows Java's signed semantics (range {@code -32768}
 * to {@code 32767}). {@link #sum()} is widened to {@code int} to avoid overflow, and {@link #average()}
 * to {@code double} to preserve precision.</p>
 *
 * @param <TP> the concrete {@code ShortTuple} subtype that fluent operations such as {@link #reverse()} return
 * @see PrimitiveTuple
 * @see BooleanTuple
 * @see ByteTuple
 * @see CharTuple
 * @see IntTuple
 * @see LongTuple
 * @see FloatTuple
 * @see DoubleTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract sealed class ShortTuple<TP extends ShortTuple<TP>> extends PrimitiveTuple<TP>
        permits ShortTuple0, ShortTuple1, ShortTuple2, ShortTuple3, ShortTuple4, ShortTuple5, ShortTuple6, ShortTuple7, ShortTuple8, ShortTuple9 {

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
     * ShortTuple.ShortTuple1 t = ShortTuple.of((short) 42);
     * short val = t._1;                  // returns 42
     * int arity = t.arity();             // returns 1
     *
     * // Edge: zero value
     * ShortTuple.ShortTuple1 zero = ShortTuple.of((short) 0);
     * assert zero._1 == 0;
     *
     * // Edge: minimum short value
     * ShortTuple.ShortTuple1 minVal = ShortTuple.of(Short.MIN_VALUE);
     * assert minVal._1 == -32768;
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
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 1, (short) 2);
     * assert pair._1 == 1;
     * assert pair._2 == 2;
     * pair.sum();                        // returns 3
     *
     * // Edge: negative values
     * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -5, (short) 5);
     * neg.sum();                         // returns 0
     * neg.min();                         // returns -5
     *
     * // Edge: equal values
     * ShortTuple.ShortTuple2 same = ShortTuple.of((short) 10, (short) 10);
     * same.min();                        // returns 10
     * same.max();                        // returns 10
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
     * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * t.sum();                           // returns 6
     * t.average();                       // returns 2.0
     * t.toString();                      // returns "(1, 2, 3)"
     *
     * // Edge: unsorted input - min/max/median still correct
     * ShortTuple.ShortTuple3 unordered = ShortTuple.of((short) 3, (short) 1, (short) 2);
     * unordered.min();                   // returns 1
     * unordered.max();                   // returns 3
     * unordered.median();                // returns 2
     *
     * // Edge: all negative
     * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -3, (short) -1, (short) -2);
     * neg.min();                         // returns -3
     * neg.max();                         // returns -1
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
     * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
     * assert t._1 == 1;
     * assert t._4 == 4;
     * t.sum();                           // returns 10
     * t.average();                       // returns 2.5
     *
     * // Edge: even arity median returns lower middle
     * t.median();                        // returns 2 (lower of the two middle values when sorted)
     *
     * // Edge: boundary short values
     * ShortTuple.ShortTuple4 bounds = ShortTuple.of(Short.MIN_VALUE, (short) 0, (short) 0, Short.MAX_VALUE);
     * bounds.min();                      // returns -32768
     * bounds.max();                      // returns 32767
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
     * ShortTuple.ShortTuple5 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
     * assert t._5 == 5;
     * t.arity();                         // returns 5
     * t.sum();                           // returns 15
     * t.median();                        // returns 3
     *
     * // Edge: reverse preserves all elements
     * ShortTuple.ShortTuple5 rev = t.reverse();
     * assert rev._1 == 5;
     * assert rev._5 == 1;
     *
     * // Edge: contains check
     * t.contains((short) 3);             // returns true
     * t.contains((short) 9);             // returns false
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
     * ShortTuple.ShortTuple6 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
     * assert t._6 == 6;
     * t.arity();                         // returns 6
     * t.sum();                           // returns 21
     *
     * // Edge: even arity - median returns lower middle value
     * t.median();                        // returns 3 (lower of middle pair [3,4] when sorted)
     *
     * // Edge: toArray has correct length
     * assert t.toArray().length == 6;
     *
     * // Edge: stream count
     * t.stream().count();                // returns 6
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
     * ShortTuple.ShortTuple7 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
     * assert t._7 == 7;
     * t.arity();                         // returns 7
     * t.sum();                           // returns 28
     *
     * // Edge: odd arity median is true middle element
     * t.median();                        // returns 4
     *
     * // Edge: reverse has correct endpoints
     * ShortTuple.ShortTuple7 rev = t.reverse();
     * assert rev._1 == 7;
     * assert rev._7 == 1;
     *
     * // Edge: toList size
     * t.toList().size();                 // returns 7
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
     * ShortTuple.ShortTuple8 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4,
     *         (short) 5, (short) 6, (short) 7, (short) 8);
     * assert t._8 == 8;
     * t.arity();                         // returns 8
     * t.sum();                           // returns 36
     *
     * // Edge: even arity median returns lower middle value
     * t.median();                        // returns 4 (lower of middle pair [4,5] when sorted)
     *
     * // Edge: contains boundary
     * t.contains((short) 1);             // returns true
     * t.contains((short) 9);             // returns false
     *
     * // Edge: reverse endpoints
     * ShortTuple.ShortTuple8 rev = t.reverse();
     * assert rev._1 == 8;
     * assert rev._8 == 1;
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
     * ShortTuple.ShortTuple9 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5,
     *         (short) 6, (short) 7, (short) 8, (short) 9);
     * assert t._9 == 9;
     * t.arity();                         // returns 9
     * t.sum();                           // returns 45
     *
     * // Edge: odd arity median is the true middle element
     * t.median();                        // returns 5
     *
     * // Edge: reverse endpoints
     * ShortTuple.ShortTuple9 rev = t.reverse();
     * assert rev._1 == 9;
     * assert rev._9 == 1;
     *
     * // Edge: toArray length
     * assert t.toArray().length == 9;
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
     * // Typical 3-element array
     * short[] values = {1, 2, 3};
     * ShortTuple.ShortTuple3 t3 = ShortTuple.copyOf(values);
     * assert t3._1 == 1;
     * assert t3._3 == 3;
     *
     * // Typical 1-element array
     * ShortTuple.ShortTuple1 t1 = ShortTuple.copyOf(new short[]{42});
     * assert t1._1 == 42;
     *
     * // Edge: null input returns the empty tuple
     * ShortTuple<?> fromNull = ShortTuple.copyOf(null);
     * fromNull.arity();                  // returns 0
     *
     * // Edge: empty array returns the empty tuple
     * ShortTuple<?> fromEmpty = ShortTuple.copyOf(new short[0]);
     * fromEmpty.toString();              // returns "()"
     *
     * // Edge: array length > 9 throws IllegalArgumentException
     * ShortTuple.copyOf(new short[10]); // throws IllegalArgumentException
     * }</pre>
     *
     * <p><b>&#9888;&#65039; Warning:</b> The runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type. Assigning to the wrong arity-specific subtype will result in a
     * {@link ClassCastException} at the assignment site.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of short values; may be {@code null} or empty, in which case the shared empty tuple is returned
     * @return a {@code ShortTuple} of the appropriate arity containing the array values, or the shared empty tuple if the array is {@code null} or empty
     * @throws IllegalArgumentException if {@code values} has more than 9 elements
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
     * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 3, (short) 1, (short) 2);
     * t.min();                           // returns 1
     *
     * ShortTuple.ShortTuple1 single = ShortTuple.of((short) 42);
     * single.min();                      // returns 42
     *
     * // Edge: negative values
     * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -3, (short) -2);
     * neg.min();                         // returns -3
     *
     * // Edge: empty tuple throws
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * empty.min();                       // throws NoSuchElementException
     * }</pre>
     *
     * @return the minimum short value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #max()
     * @see #median()
     */
    public short min() {
        final short[] arr = elements();

        if (arr.length == 0) {
            throw new NoSuchElementException("Cannot compute min() for an empty tuple");
        }

        return N.min(arr);
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
     * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 3, (short) 1, (short) 2);
     * t.max();                           // returns 3
     *
     * ShortTuple.ShortTuple1 single = ShortTuple.of((short) 42);
     * single.max();                      // returns 42
     *
     * // Edge: negative values
     * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -3, (short) -2);
     * neg.max();                         // returns -1
     *
     * // Edge: empty tuple throws
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * empty.max();                       // throws NoSuchElementException
     * }</pre>
     *
     * @return the maximum short value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see #median()
     */
    public short max() {
        final short[] arr = elements();

        if (arr.length == 0) {
            throw new NoSuchElementException("Cannot compute max() for an empty tuple");
        }

        return N.max(arr);
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
     * ShortTuple.ShortTuple3 odd = ShortTuple.of((short) 1, (short) 3, (short) 2);
     * odd.median();                      // returns 2 (middle value when sorted: 1, 2, 3)
     *
     * ShortTuple.ShortTuple4 even = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
     * even.median();                     // returns 2 (lower of middle pair [2,3] when sorted)
     *
     * // Edge: single element
     * ShortTuple.ShortTuple1 single = ShortTuple.of((short) 7);
     * single.median();                   // returns 7
     *
     * // Edge: empty tuple throws
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * empty.median();                    // throws NoSuchElementException
     * }</pre>
     *
     * @return the median short value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see #max()
     * @see N#median(short...)
     */
    public short median() {
        final short[] arr = elements();

        if (arr.length == 0) {
            throw new NoSuchElementException("Cannot compute median() for an empty tuple");
        }

        return N.median(arr);
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
     * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * t.sum();                           // returns 6  (int, not short)
     *
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 100, (short) 200);
     * pair.sum();                        // returns 300
     *
     * // Edge: empty tuple returns 0
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * empty.sum();                       // returns 0
     *
     * // Edge: negative values cancel
     * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -5, (short) 5);
     * neg.sum();                         // returns 0
     * }</pre>
     *
     * @return the sum of all short values in this tuple as an {@code int}; {@code 0} for an empty tuple
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
     * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
     * t.average();                       // returns 2.5
     *
     * ShortTuple.ShortTuple3 whole = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * whole.average();                   // returns 2.0
     *
     * // Edge: single element
     * ShortTuple.ShortTuple1 single = ShortTuple.of((short) 7);
     * single.average();                  // returns 7.0
     *
     * // Edge: empty tuple returns 0D
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * empty.average();                   // returns 0.0
     * }</pre>
     *
     * @return the average of all short values in this tuple as a {@code double}, or {@code 0D} if this tuple is empty
     * @see #sum()
     */
    public double average() {
        final short[] arr = elements();

        return arr.length == 0 ? 0D : N.average(arr);
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
     * ShortTuple.ShortTuple2 rev2 = pair.reverse();
     * assert rev2._1 == 2;
     * assert rev2._2 == 1;
     *
     * ShortTuple.ShortTuple3 t3 = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * ShortTuple.ShortTuple3 rev3 = t3.reverse();
     * rev3.toString();                   // returns "(3, 2, 1)"
     *
     * // single-element tuple reverses to an equal tuple
     * ShortTuple.ShortTuple1 t1 = ShortTuple.of((short) 9);
     * assert t1.reverse()._1 == 9;
     *
     * // Edge: empty tuple - returns the same instance
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * empty.reverse().arity();           // returns 0
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
     * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * t.contains((short) 2);             // returns true
     * t.contains((short) 5);             // returns false
     *
     * // Duplicate values - still found
     * ShortTuple.ShortTuple2 dup = ShortTuple.of((short) 10, (short) 10);
     * dup.contains((short) 10);          // returns true
     *
     * // Edge: empty tuple never contains anything
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * empty.contains((short) 0);         // returns false
     *
     * // Edge: negative value search
     * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -1, (short) -2);
     * neg.contains((short) -1);          // returns true
     * neg.contains((short) 1);           // returns false
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
     * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * short[] arr = t.toArray();         // returns [1, 2, 3]
     * arr[0] = 99;                       // does NOT modify the tuple - it is a defensive copy
     * assert t._1 == 1;
     *
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 10, (short) 20);
     * pair.toArray();                    // returns [10, 20]
     *
     * // Edge: empty tuple returns zero-length array
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * assert empty.toArray().length == 0;
     *
     * // Edge: single element
     * ShortTuple.ShortTuple1 single = ShortTuple.of((short) 5);
     * single.toArray();                  // returns [5]
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
     * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * ShortList list = t.toList();
     * list.size();                       // returns 3
     * list.get(0);                       // returns 1
     *
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 10, (short) 20);
     * pair.toList().get(1);              // returns 20
     *
     * // Edge: empty tuple gives empty list
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * empty.toList().size();             // returns 0
     *
     * // Edge: list is mutable but independent - adding does not affect tuple
     * list.add((short) 4);               // modifies the copy, not the tuple
     * t.arity();                         // still returns 3
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
     * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
     *
     * // Accumulate sum externally
     * java.util.concurrent.atomic.AtomicInteger total = new java.util.concurrent.atomic.AtomicInteger();
     * t.forEach(v -> total.addAndGet(v));   // adds each value to total
     * total.get();                          // returns 6
     *
     * // Collect visited values
     * java.util.List<Short> visited = new java.util.ArrayList<>();
     * t.forEach(v -> visited.add(v));   // collects each value into visited
     * assert visited.equals(java.util.List.of((short) 1, (short) 2, (short) 3));
     *
     * // Edge: empty tuple - consumer is never called
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * java.util.concurrent.atomic.AtomicInteger cnt = new java.util.concurrent.atomic.AtomicInteger();
     * empty.forEach(v -> cnt.incrementAndGet());   // action not invoked (empty tuple)
     * cnt.get();                                   // returns 0
     *
     * // Edge: null action throws
     * // t.forEach(null);                   // throws IllegalArgumentException
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the action to be performed for each element, must not be {@code null}
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> action) throws E {
        N.checkArgNotNull(action, "action");

        for (final short element : elements()) {
            action.accept(element);
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
     * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * t.stream().sum();                  // returns 6
     *
     * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 10, (short) 20);
     * pair.stream().max().getAsShort();  // returns 20
     *
     * // Edge: empty tuple produces empty stream
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * empty.stream().sum();              // returns 0
     * empty.stream().count();            // returns 0
     *
     * // Edge: filter and count
     * ShortTuple.ShortTuple3 t2 = ShortTuple.of((short) 1, (short) -2, (short) 3);
     * t2.stream().filter(v -> v > 0).count();  // returns 2
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 a = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * ShortTuple.ShortTuple3 b = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * assert a.hashCode() == b.hashCode(); // returns true (equal content -> equal hash)
     *
     *
     * // Edge: empty tuple has a stable hash
     * ShortTuple<?> empty = ShortTuple.copyOf(new short[0]);
     * empty.hashCode();                  // does not throw
     *
     * // Edge: single-element tuple
     * ShortTuple.ShortTuple1 t1 = ShortTuple.of((short) 0);
     * t1.hashCode();                     // does not throw
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
     *       (so a {@code ShortTuple2} never equals a {@code ShortTuple3}), and contains
     *       the same short values in the same order.</li>
     * </ul>
     *
     * <p>This method is consistent with {@link #hashCode()}.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * ShortTuple.ShortTuple3 a = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * ShortTuple.ShortTuple3 b = ShortTuple.of((short) 1, (short) 2, (short) 3);
     * a.equals(b);                       // returns true
     *
     * // Different values in same arity
     * ShortTuple.ShortTuple3 c = ShortTuple.of((short) 3, (short) 2, (short) 1);
     * a.equals(c);                       // returns false
     *
     * // Edge: different arity is never equal
     * ShortTuple.ShortTuple2 d = ShortTuple.of((short) 1, (short) 2);
     * a.equals(d);                       // returns false
     *
     * // Edge: null is never equal
     * a.equals(null);                    // returns false
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
            return N.equals(elements(), ((ShortTuple<TP>) obj).elements());
        }
    }

    /**
     * Returns the internal array containing all short elements in this tuple.
     * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
     * Modifying the returned array will compromise the immutability of this tuple.
     * Use {@link #toArray()} instead if you need an array that can be safely modified.
     * </p>
     *
     * @return the internal array of short elements
     */
    protected abstract short[] elements();

    /**
     * An empty ShortTuple containing no elements (arity 0).
     * <p>
     * This package-private class is exposed only through the base {@code ShortTuple} type
     * via the singleton instance returned by {@link #copyOf(short[])} when invoked with a
     * {@code null} or zero-length array. {@link #sum()} returns 0 and {@link #average()} returns {@code 0D}, while
     * {@link #min()}, {@link #max()}, and {@link #median()} all throw {@link java.util.NoSuchElementException}.
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
            throw new NoSuchElementException("Cannot compute min() for an empty tuple");
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
            throw new NoSuchElementException("Cannot compute max() for an empty tuple");
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
            throw new NoSuchElementException("Cannot compute median() for an empty tuple");
        }

        /**
         * Returns the sum of all short values in this tuple as an int.
         * For an empty tuple, the sum is {@code 0}.
         *
         * @return {@code 0}
         */
        @Override
        public int sum() {
            return 0;
        }

        /**
         * Returns the average of all short values in this tuple.
         * Since this tuple is empty, this method always returns {@code 0D}.
         *
         * @return {@code 0D}
         */
        @Override
        public double average() {
            return 0D;
        }

        /**
         * Returns this empty tuple instance.
         * Since this tuple has no elements, reversing has no effect.
         *
         * @return this {@code ShortTuple0} instance
         */
        @Override
        public ShortTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified short value.
         * Since this tuple is empty, this method always returns {@code false}.
         *
         * @param valueToFind the short value to search for
         * @return {@code false} always, because the tuple is empty
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
         * @return an empty short array
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
     * short value = single._1;   // 42
     * short min = single.min();  // 42 (single element)
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple1 t = ShortTuple.of((short) 42);
         * t.arity();                         // returns 1
         *
         * // Edge: zero value - arity still 1
         * ShortTuple.of((short) 0).arity();  // returns 1
         *
         * // Edge: negative value - arity still 1
         * ShortTuple.of((short) -1).arity(); // returns 1
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple1 t = ShortTuple.of((short) 7);
         * t.min();                           // returns 7
         *
         * ShortTuple.ShortTuple1 t2 = ShortTuple.of((short) -100);
         * t2.min();                          // returns -100
         *
         * // Edge: zero
         * ShortTuple.of((short) 0).min();    // returns 0
         *
         * // Edge: Short.MAX_VALUE
         * ShortTuple.of(Short.MAX_VALUE).min(); // returns 32767
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple1 t = ShortTuple.of((short) 7);
         * t.max();                           // returns 7
         *
         * ShortTuple.ShortTuple1 t2 = ShortTuple.of((short) -100);
         * t2.max();                          // returns -100
         *
         * // Edge: zero
         * ShortTuple.of((short) 0).max();    // returns 0
         *
         * // Edge: Short.MIN_VALUE
         * ShortTuple.of(Short.MIN_VALUE).max(); // returns -32768
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple1 t = ShortTuple.of((short) 7);
         * t.median();                        // returns 7
         *
         * ShortTuple.ShortTuple1 t2 = ShortTuple.of((short) -50);
         * t2.median();                       // returns -50
         *
         * // Edge: zero
         * ShortTuple.of((short) 0).median(); // returns 0
         *
         * // Edge: Short.MAX_VALUE
         * ShortTuple.of(Short.MAX_VALUE).median(); // returns 32767
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple1 t = ShortTuple.of((short) 7);
         * int s = t.sum();   // returns 7
         *
         * ShortTuple.ShortTuple1 neg = ShortTuple.of((short) -3);
         * int ns = neg.sum();   // returns -3
         *
         * ShortTuple.ShortTuple1 maxVal = ShortTuple.of(Short.MAX_VALUE);
         * int ms = maxVal.sum();   // returns 32767 (widened to int, no overflow)
         *
         * ShortTuple.ShortTuple1 minVal = ShortTuple.of(Short.MIN_VALUE);
         * int mn = minVal.sum();   // returns -32768
         * }</pre>
         *
         * @return the single short value widened to an {@code int} (sign-extended)
         */
        @Override
        public int sum() {
            return _1;
        }

        /**
         * Returns the average of all short values in this tuple as a double.
         * Since this tuple contains only one element, it returns that element converted to a {@code double}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple1 t = ShortTuple.of((short) 5);
         * double avg = t.average();   // returns 5.0
         *
         * ShortTuple.ShortTuple1 neg = ShortTuple.of((short) -10);
         * double navg = neg.average();   // returns -10.0
         *
         * ShortTuple.ShortTuple1 zero = ShortTuple.of((short) 0);
         * double zavg = zero.average();   // returns 0.0
         *
         * ShortTuple.ShortTuple1 maxVal = ShortTuple.of(Short.MAX_VALUE);
         * double mavg = maxVal.average();   // returns 32767.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple1 t = ShortTuple.of((short) 42);
         * ShortTuple.ShortTuple1 rev = t.reverse();
         * // rev._1 == 42  (same value, new instance)
         *
         * ShortTuple.ShortTuple1 neg = ShortTuple.of((short) -1);
         * ShortTuple.ShortTuple1 negRev = neg.reverse();
         * // negRev._1 == -1
         *
         * ShortTuple.ShortTuple1 maxVal = ShortTuple.of(Short.MAX_VALUE);
         * ShortTuple.ShortTuple1 maxRev = maxVal.reverse();
         * // maxRev._1 == Short.MAX_VALUE
         *
         * ShortTuple.ShortTuple1 dup = ShortTuple.of((short) 0);
         * // dup.reverse()._1 == 0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple1 t = ShortTuple.of((short) 5);
         * boolean hasFive = t.contains((short) 5);   // returns true
         * boolean hasSix  = t.contains((short) 6);   // returns false
         *
         * ShortTuple.ShortTuple1 neg = ShortTuple.of((short) -1);
         * boolean hasNeg  = neg.contains((short) -1);   // returns true
         * boolean hasZero = neg.contains((short) 0);    // returns false
         *
         * ShortTuple.ShortTuple1 minVal = ShortTuple.of(Short.MIN_VALUE);
         * boolean hasMin = minVal.contains(Short.MIN_VALUE);   // returns true
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple1 t = ShortTuple.of((short) 7);
         * int h = t.hashCode();   // returns 7
         *
         * ShortTuple.ShortTuple1 neg = ShortTuple.of((short) -3);
         * int nh = neg.hashCode();   // returns -3
         *
         * ShortTuple.ShortTuple1 zero = ShortTuple.of((short) 0);
         * int zh = zero.hashCode();   // returns 0
         *
         * // Equal tuples have equal hash codes
         * ShortTuple.ShortTuple1 a = ShortTuple.of((short) 10);
         * ShortTuple.ShortTuple1 b = ShortTuple.of((short) 10);
         * // a.hashCode() == b.hashCode()  -> true
         * }</pre>
         *
         * @return the single short element widened to an {@code int} (sign-extended)
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
         * ShortTuple.ShortTuple1 a = ShortTuple.of((short) 5);
         * ShortTuple.ShortTuple1 b = ShortTuple.of((short) 5);
         * boolean eq = a.equals(b);   // returns true
         *
         * ShortTuple.ShortTuple1 c = ShortTuple.of((short) 6);
         * boolean neq = a.equals(c);   // returns false
         *
         * boolean selfEq = a.equals(a);   // returns true (same reference)
         *
         * boolean nullEq = a.equals(null);   // returns false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple1 t = ShortTuple.of((short) 3);
         * String s = t.toString();   // returns "(3)"
         *
         * ShortTuple.ShortTuple1 neg = ShortTuple.of((short) -7);
         * String ns = neg.toString();   // returns "(-7)"
         *
         * ShortTuple.ShortTuple1 zero = ShortTuple.of((short) 0);
         * String zs = zero.toString();   // returns "(0)"
         *
         * ShortTuple.ShortTuple1 maxVal = ShortTuple.of(Short.MAX_VALUE);
         * String ms = maxVal.toString();   // returns "(32767)"
         * }</pre>
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
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 t = ShortTuple.of((short) 1, (short) 2);
         * int n = t.arity();   // returns 2
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -5, (short) -3);
         * int nn = neg.arity();   // returns 2
         *
         * ShortTuple.ShortTuple2 bounds = ShortTuple.of(Short.MIN_VALUE, Short.MAX_VALUE);
         * int bn = bounds.arity();   // returns 2
         *
         * ShortTuple.ShortTuple2 dups = ShortTuple.of((short) 0, (short) 0);
         * int dn = dups.arity();   // returns 2
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 t = ShortTuple.of((short) 3, (short) 7);
         * short min = t.min();   // returns 3
         *
         * ShortTuple.ShortTuple2 rev = ShortTuple.of((short) 7, (short) 3);
         * short rmin = rev.min();   // returns 3
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -5, (short) -1);
         * short nmin = neg.min();   // returns -5
         *
         * ShortTuple.ShortTuple2 bounds = ShortTuple.of(Short.MIN_VALUE, Short.MAX_VALUE);
         * short bmin = bounds.min();   // returns Short.MIN_VALUE (-32768)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 t = ShortTuple.of((short) 3, (short) 7);
         * short max = t.max();   // returns 7
         *
         * ShortTuple.ShortTuple2 rev = ShortTuple.of((short) 7, (short) 3);
         * short rmax = rev.max();   // returns 7
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -5, (short) -1);
         * short nmax = neg.max();   // returns -1
         *
         * ShortTuple.ShortTuple2 bounds = ShortTuple.of(Short.MIN_VALUE, Short.MAX_VALUE);
         * short bmax = bounds.max();   // returns Short.MAX_VALUE (32767)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 t = ShortTuple.of((short) 3, (short) 7);
         * short med = t.median();   // returns 3 (lower of the two when sorted: [3], 7)
         *
         * ShortTuple.ShortTuple2 rev = ShortTuple.of((short) 7, (short) 3);
         * short rmed = rev.median();   // returns 3 (lower middle regardless of input order)
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -5, (short) -1);
         * short nmed = neg.median();   // returns -5 (lower of -5 and -1)
         *
         * ShortTuple.ShortTuple2 dups = ShortTuple.of((short) 4, (short) 4);
         * short dmed = dups.median();   // returns 4 (equal values)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 t = ShortTuple.of((short) 3, (short) 7);
         * int sum = t.sum();   // returns 10
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -3, (short) -7);
         * int nsum = neg.sum();   // returns -10
         *
         * ShortTuple.ShortTuple2 mixed = ShortTuple.of((short) -5, (short) 5);
         * int msum = mixed.sum();   // returns 0
         *
         * ShortTuple.ShortTuple2 bounds = ShortTuple.of(Short.MAX_VALUE, Short.MAX_VALUE);
         * int bsum = bounds.sum();   // returns 65534 (no int overflow)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 t = ShortTuple.of((short) 3, (short) 7);
         * double avg = t.average();   // returns 5.0
         *
         * ShortTuple.ShortTuple2 odd = ShortTuple.of((short) 1, (short) 2);
         * double oa = odd.average();   // returns 1.5
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -3, (short) -7);
         * double na = neg.average();   // returns -5.0
         *
         * ShortTuple.ShortTuple2 mixed = ShortTuple.of((short) -1, (short) 1);
         * double ma = mixed.average();   // returns 0.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 t = ShortTuple.of((short) 1, (short) 2);
         * ShortTuple.ShortTuple2 rev = t.reverse();
         * // rev._1 == 2, rev._2 == 1
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -5, (short) 10);
         * ShortTuple.ShortTuple2 nrev = neg.reverse();
         * // nrev._1 == 10, nrev._2 == -5
         *
         * ShortTuple.ShortTuple2 dups = ShortTuple.of((short) 3, (short) 3);
         * ShortTuple.ShortTuple2 drev = dups.reverse();
         * // drev._1 == 3, drev._2 == 3
         *
         * ShortTuple.ShortTuple2 bounds = ShortTuple.of(Short.MIN_VALUE, Short.MAX_VALUE);
         * ShortTuple.ShortTuple2 brev = bounds.reverse();
         * // brev._1 == Short.MAX_VALUE, brev._2 == Short.MIN_VALUE
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 t = ShortTuple.of((short) 3, (short) 7);
         * boolean hasThree = t.contains((short) 3);   // returns true
         * boolean hasSeven = t.contains((short) 7);   // returns true
         * boolean hasFive  = t.contains((short) 5);   // returns false
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -5, (short) -1);
         * boolean hasNeg = neg.contains((short) -5);   // returns true
         * boolean hasPos = neg.contains((short) 5);    // returns false
         *
         * ShortTuple.ShortTuple2 bounds = ShortTuple.of(Short.MIN_VALUE, Short.MAX_VALUE);
         * boolean hasMin = bounds.contains(Short.MIN_VALUE);   // returns true
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 t = ShortTuple.of((short) 3, (short) 7);
         * // Iterate and print each element
         * t.forEach(v -> System.out.println(v));  // Prints: 3 then 7
         *
         * // Accumulate sum externally
         * int[] total = {0};
         * t.forEach(v -> total[0] += v);
         * // total[0] == 10
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -5, (short) -1);
         * int[] nTotal = {0};
         * neg.forEach(v -> nTotal[0] += v);
         * // nTotal[0] == -6
         *
         * ShortTuple.ShortTuple2 bounds = ShortTuple.of(Short.MIN_VALUE, Short.MAX_VALUE);
         * int[] count = {0};
         * bounds.forEach(v -> count[0]++);  // count[0] == 2
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

            action.accept(_1);
            action.accept(_2);
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
         * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 3, (short) 5);
         * pair.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * // Prints: 3 + 5 = 8
         *
         * int[] result = {0};
         * pair.accept((a, b) -> result[0] = a * b);
         * // result[0] == 15
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -2, (short) 4);
         * int[] diff = {0};
         * neg.accept((a, b) -> diff[0] = b - a);
         * // diff[0] == 6
         *
         * ShortTuple.ShortTuple2 bounds = ShortTuple.of(Short.MIN_VALUE, Short.MAX_VALUE);
         * int[] sum = {0};
         * bounds.accept((a, b) -> sum[0] = a + b);
         * // sum[0] == -1
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the bi-consumer to perform on the two elements, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #map(Throwables.ShortBiFunction)
         * @see #filter(Throwables.ShortBiPredicate)
         */
        public <E extends Exception> void accept(final Throwables.ShortBiConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

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
         * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 3, (short) 5);
         * int product = pair.map((a, b) -> a * b);   // returns 15
         *
         * String desc = pair.map((a, b) -> a + " and " + b);   // returns "3 and 5"
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -2, (short) 4);
         * int diff = neg.map((a, b) -> b - a);   // returns 6
         *
         * // Mapper returning null is allowed (@MayReturnNull)
         * Object nullResult = pair.map((a, b) -> null);   // returns null
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the bi-function to apply to the two elements, must not be {@code null}
         * @return the result of applying the mapper function (may be {@code null} if the mapper returns {@code null})
         * @throws IllegalArgumentException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.ShortBiConsumer)
         * @see #filter(Throwables.ShortBiPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.ShortBiFunction<U, E> mapper) throws E {
            N.checkArgNotNull(mapper, "mapper");

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
         * ShortTuple.ShortTuple2 pair = ShortTuple.of((short) 3, (short) 5);
         * Optional<ShortTuple.ShortTuple2> result = pair.filter((a, b) -> a < b);
         * // result.isPresent() == true (since 3 < 5)
         *
         * Optional<ShortTuple.ShortTuple2> empty = pair.filter((a, b) -> a > b);
         * // empty.isPresent() == false (since 3 is not > 5)
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -5, (short) -1);
         * Optional<ShortTuple.ShortTuple2> negResult = neg.filter((a, b) -> a < 0 && b < 0);
         * // negResult.isPresent() == true (both negative)
         *
         * ShortTuple.ShortTuple2 eq = ShortTuple.of((short) 4, (short) 4);
         * Optional<ShortTuple.ShortTuple2> eqEmpty = eq.filter((a, b) -> a != b);
         * // eqEmpty.isPresent() == false (values are equal)
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the bi-predicate to test the two elements, must not be {@code null}
         * @return an {@code Optional} containing this tuple if the predicate returns {@code true}, otherwise an empty {@code Optional}
         * @throws IllegalArgumentException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception during evaluation
         * @see #accept(Throwables.ShortBiConsumer)
         * @see #map(Throwables.ShortBiFunction)
         */
        public <E extends Exception> Optional<ShortTuple2> filter(final Throwables.ShortBiPredicate<E> predicate) throws E {
            N.checkArgNotNull(predicate, "predicate");

            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 t = ShortTuple.of((short) 1, (short) 2);
         * int h = t.hashCode();   // returns 31 * 1 + 2 == 33
         *
         * ShortTuple.ShortTuple2 same = ShortTuple.of((short) 1, (short) 2);
         * // t.hashCode() == same.hashCode()  -> true
         *
         * ShortTuple.ShortTuple2 diff = ShortTuple.of((short) 2, (short) 1);
         * // t.hashCode() != diff.hashCode()  -> likely true (order matters)
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -1, (short) -2);
         * int nh = neg.hashCode();   // returns 31 * (-1) + (-2) == -33
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
         * ShortTuple.ShortTuple2 a = ShortTuple.of((short) 3, (short) 5);
         * ShortTuple.ShortTuple2 b = ShortTuple.of((short) 3, (short) 5);
         * boolean eq = a.equals(b);   // returns true
         *
         * ShortTuple.ShortTuple2 c = ShortTuple.of((short) 5, (short) 3);
         * boolean neq = a.equals(c);   // returns false (order matters)
         *
         * boolean selfEq = a.equals(a);   // returns true (same reference)
         *
         * boolean nullEq = a.equals(null);   // returns false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple2 t = ShortTuple.of((short) 3, (short) 5);
         * String s = t.toString();   // returns "(3, 5)"
         *
         * ShortTuple.ShortTuple2 neg = ShortTuple.of((short) -1, (short) -2);
         * String ns = neg.toString();   // returns "(-1, -2)"
         *
         * ShortTuple.ShortTuple2 zero = ShortTuple.of((short) 0, (short) 0);
         * String zs = zero.toString();   // returns "(0, 0)"
         *
         * ShortTuple.ShortTuple2 bounds = ShortTuple.of(Short.MIN_VALUE, Short.MAX_VALUE);
         * String bs = bounds.toString();   // returns "(-32768, 32767)"
         * }</pre>
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
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
         * int n = t.arity();   // returns 3
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -2, (short) -3);
         * int nn = neg.arity();   // returns 3
         *
         * ShortTuple.ShortTuple3 bounds = ShortTuple.of(Short.MIN_VALUE, (short) 0, Short.MAX_VALUE);
         * int bn = bounds.arity();   // returns 3
         *
         * ShortTuple.ShortTuple3 dups = ShortTuple.of((short) 5, (short) 5, (short) 5);
         * int dn = dups.arity();   // returns 3
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 3, (short) 1, (short) 2);
         * short min = t.min();   // returns 1
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -5, (short) -3);
         * short nmin = neg.min();   // returns -5
         *
         * ShortTuple.ShortTuple3 dups = ShortTuple.of((short) 4, (short) 4, (short) 4);
         * short dmin = dups.min();   // returns 4
         *
         * ShortTuple.ShortTuple3 bounds = ShortTuple.of(Short.MIN_VALUE, (short) 0, Short.MAX_VALUE);
         * short bmin = bounds.min();   // returns Short.MIN_VALUE (-32768)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 3, (short) 1, (short) 2);
         * short max = t.max();   // returns 3
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -5, (short) -3);
         * short nmax = neg.max();   // returns -1
         *
         * ShortTuple.ShortTuple3 dups = ShortTuple.of((short) 4, (short) 4, (short) 4);
         * short dmax = dups.max();   // returns 4
         *
         * ShortTuple.ShortTuple3 bounds = ShortTuple.of(Short.MIN_VALUE, (short) 0, Short.MAX_VALUE);
         * short bmax = bounds.max();   // returns Short.MAX_VALUE (32767)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 3, (short) 1, (short) 2);
         * short med = t.median();   // returns 2 (sorted: 1, [2], 3)
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -5, (short) -3);
         * short nmed = neg.median();   // returns -3 (sorted: -5, [-3], -1)
         *
         * ShortTuple.ShortTuple3 dups = ShortTuple.of((short) 4, (short) 4, (short) 4);
         * short dmed = dups.median();   // returns 4 (all equal)
         *
         * ShortTuple.ShortTuple3 bounds = ShortTuple.of(Short.MIN_VALUE, (short) 0, Short.MAX_VALUE);
         * short bmed = bounds.median();   // returns 0 (middle value)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
         * int sum = t.sum();   // returns 6
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -2, (short) -3);
         * int nsum = neg.sum();   // returns -6
         *
         * ShortTuple.ShortTuple3 mixed = ShortTuple.of((short) -5, (short) 0, (short) 5);
         * int msum = mixed.sum();   // returns 0
         *
         * ShortTuple.ShortTuple3 maxVals = ShortTuple.of(Short.MAX_VALUE, Short.MAX_VALUE, Short.MAX_VALUE);
         * int bsum = maxVals.sum();   // returns 98301 (no int overflow)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
         * double avg = t.average();   // returns 2.0
         *
         * ShortTuple.ShortTuple3 odd = ShortTuple.of((short) 0, (short) 1, (short) 2);
         * double oa = odd.average();   // returns 1.0
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -3, (short) -3, (short) -3);
         * double na = neg.average();   // returns -3.0
         *
         * ShortTuple.ShortTuple3 mixed = ShortTuple.of((short) -1, (short) 0, (short) 1);
         * double ma = mixed.average();   // returns 0.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
         * ShortTuple.ShortTuple3 rev = t.reverse();
         * // rev._1 == 3, rev._2 == 2, rev._3 == 1
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -2, (short) -3);
         * ShortTuple.ShortTuple3 nrev = neg.reverse();
         * // nrev._1 == -3, nrev._2 == -2, nrev._3 == -1
         *
         * ShortTuple.ShortTuple3 dups = ShortTuple.of((short) 5, (short) 5, (short) 5);
         * ShortTuple.ShortTuple3 drev = dups.reverse();
         * // drev._1 == 5, drev._2 == 5, drev._3 == 5
         *
         * ShortTuple.ShortTuple3 bounds = ShortTuple.of(Short.MIN_VALUE, (short) 0, Short.MAX_VALUE);
         * ShortTuple.ShortTuple3 brev = bounds.reverse();
         * // brev._1 == Short.MAX_VALUE, brev._2 == 0, brev._3 == Short.MIN_VALUE
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
         * boolean hasTwo   = t.contains((short) 2);   // returns true
         * boolean hasFour  = t.contains((short) 4);   // returns false
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -5, (short) -3, (short) -1);
         * boolean hasNeg   = neg.contains((short) -3);   // returns true
         * boolean hasZero  = neg.contains((short) 0);    // returns false
         *
         * ShortTuple.ShortTuple3 bounds = ShortTuple.of(Short.MIN_VALUE, (short) 0, Short.MAX_VALUE);
         * boolean hasMax   = bounds.contains(Short.MAX_VALUE);   // returns true
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
         * // Print each element
         * t.forEach(v -> System.out.println(v));  // Prints: 1 then 2 then 3
         *
         * // Accumulate sum externally
         * int[] total = {0};
         * t.forEach(v -> total[0] += v);
         * // total[0] == 6
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -2, (short) -3);
         * int[] nTotal = {0};
         * neg.forEach(v -> nTotal[0] += v);
         * // nTotal[0] == -6
         *
         * ShortTuple.ShortTuple3 bounds = ShortTuple.of(Short.MIN_VALUE, (short) 0, Short.MAX_VALUE);
         * int[] count = {0};
         * bounds.forEach(v -> count[0]++);  // count[0] == 3
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
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
         * ShortTuple.ShortTuple3 triple = ShortTuple.of((short) 2, (short) 3, (short) 5);
         * triple.accept((a, b, c) -> System.out.println(a + " + " + b + " + " + c + " = " + (a + b + c)));
         * // Prints: 2 + 3 + 5 = 10
         *
         * int[] product = {1};
         * triple.accept((a, b, c) -> product[0] = a * b * c);
         * // product[0] == 30
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -2, (short) -3);
         * int[] sum = {0};
         * neg.accept((a, b, c) -> sum[0] = a + b + c);
         * // sum[0] == -6
         *
         * ShortTuple.ShortTuple3 bounds = ShortTuple.of(Short.MIN_VALUE, (short) 0, Short.MAX_VALUE);
         * int[] count = {0};
         * bounds.accept((a, b, c) -> { count[0] = (a < 0) ? 1 : 0; count[0] += (b == 0) ? 1 : 0; count[0] += (c > 0) ? 1 : 0; });
         * // count[0] == 3 (all three conditions true)
         * }</pre>
         *
         * @param <E> the type of exception that the action may throw
         * @param action the tri-consumer to perform on the three elements, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #map(Throwables.ShortTriFunction)
         * @see #filter(Throwables.ShortTriPredicate)
         */
        public <E extends Exception> void accept(final Throwables.ShortTriConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

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
         * ShortTuple.ShortTuple3 triple = ShortTuple.of((short) 2, (short) 3, (short) 5);
         * int product = triple.map((a, b, c) -> a * b * c);   // returns 30
         *
         * String desc = triple.map((a, b, c) -> a + ", " + b + ", " + c);   // returns "2, 3, 5"
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -2, (short) -3);
         * int negSum = neg.map((a, b, c) -> a + b + c);   // returns -6
         *
         * // Mapper returning null is allowed (@MayReturnNull)
         * Object nullResult = triple.map((a, b, c) -> null);   // returns null
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the tri-function to apply to the three elements, must not be {@code null}
         * @return the result of applying the mapper function (may be {@code null} if the mapper returns {@code null})
         * @throws IllegalArgumentException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.ShortTriConsumer)
         * @see #filter(Throwables.ShortTriPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.ShortTriFunction<U, E> mapper) throws E {
            N.checkArgNotNull(mapper, "mapper");

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
         * ShortTuple.ShortTuple3 triple = ShortTuple.of((short) 2, (short) 3, (short) 5);
         * Optional<ShortTuple.ShortTuple3> result = triple.filter((a, b, c) -> a < b && b < c);
         * // result.isPresent() == true (since 2 < 3 < 5)
         *
         * Optional<ShortTuple.ShortTuple3> empty = triple.filter((a, b, c) -> a + b + c > 20);
         * // empty.isPresent() == false (since 2 + 3 + 5 = 10 is not > 20)
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -3, (short) -2, (short) -1);
         * Optional<ShortTuple.ShortTuple3> negResult = neg.filter((a, b, c) -> a < 0);
         * // negResult.isPresent() == true (a == -3 < 0)
         *
         * ShortTuple.ShortTuple3 dups = ShortTuple.of((short) 5, (short) 5, (short) 5);
         * Optional<ShortTuple.ShortTuple3> dupsEmpty = dups.filter((a, b, c) -> a != b);
         * // dupsEmpty.isPresent() == false (all equal)
         * }</pre>
         *
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the tri-predicate to test the three elements, must not be {@code null}
         * @return an {@code Optional} containing this tuple if the predicate returns {@code true}, otherwise an empty {@code Optional}
         * @throws IllegalArgumentException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception during evaluation
         * @see #accept(Throwables.ShortTriConsumer)
         * @see #map(Throwables.ShortTriFunction)
         */
        public <E extends Exception> Optional<ShortTuple3> filter(final Throwables.ShortTriPredicate<E> predicate) throws E {
            N.checkArgNotNull(predicate, "predicate");

            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
         * int h = t.hashCode();   // returns (31 * (31 * 1 + 2)) + 3 == 1026
         *
         * ShortTuple.ShortTuple3 same = ShortTuple.of((short) 1, (short) 2, (short) 3);
         * // t.hashCode() == same.hashCode()  -> true
         *
         * ShortTuple.ShortTuple3 diff = ShortTuple.of((short) 3, (short) 2, (short) 1);
         * // t.hashCode() != diff.hashCode()  -> likely true (order matters)
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -2, (short) -3);
         * int nh = neg.hashCode();   // returns (31 * (31 * (-1) + (-2))) + (-3) == -1026
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
         * ShortTuple.ShortTuple3 a = ShortTuple.of((short) 1, (short) 2, (short) 3);
         * ShortTuple.ShortTuple3 b = ShortTuple.of((short) 1, (short) 2, (short) 3);
         * boolean eq = a.equals(b);   // returns true
         *
         * ShortTuple.ShortTuple3 c = ShortTuple.of((short) 3, (short) 2, (short) 1);
         * boolean neq = a.equals(c);   // returns false (order matters)
         *
         * boolean selfEq = a.equals(a);   // returns true (same reference)
         *
         * boolean nullEq = a.equals(null);   // returns false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple3 t = ShortTuple.of((short) 1, (short) 2, (short) 3);
         * String s = t.toString();   // returns "(1, 2, 3)"
         *
         * ShortTuple.ShortTuple3 neg = ShortTuple.of((short) -1, (short) -2, (short) -3);
         * String ns = neg.toString();   // returns "(-1, -2, -3)"
         *
         * ShortTuple.ShortTuple3 zero = ShortTuple.of((short) 0, (short) 0, (short) 0);
         * String zs = zero.toString();   // returns "(0, 0, 0)"
         *
         * ShortTuple.ShortTuple3 bounds = ShortTuple.of(Short.MIN_VALUE, (short) 0, Short.MAX_VALUE);
         * String bs = bounds.toString();   // returns "(-32768, 0, 32767)"
         * }</pre>
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
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
         * t.arity(); // returns 4
         *
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of((short) -1, (short) 0, (short) 5, (short) 10);
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
         * Returns the minimum short value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 3, (short) 1, (short) 4, (short) 2);
         * t.min(); // returns (short) 1
         *
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, Short.MAX_VALUE);
         * t2.min(); // returns Short.MIN_VALUE (-32768)
         *
         * ShortTuple.ShortTuple4 t3 = ShortTuple.of((short) 5, (short) 5, (short) 5, (short) 5);
         * t3.min(); // returns (short) 5  (all duplicates)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 3, (short) 1, (short) 4, (short) 2);
         * t.max(); // returns (short) 4
         *
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, Short.MAX_VALUE);
         * t2.max(); // returns Short.MAX_VALUE (32767)
         *
         * ShortTuple.ShortTuple4 t3 = ShortTuple.of((short) -5, (short) -5, (short) -5, (short) -5);
         * t3.max(); // returns (short) -5  (all duplicates)
         * }</pre>
         *
         * @return the largest of the four short values
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the median short value in this tuple.
         * For tuples with an even number of elements, returns the lower middle element when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 3, (short) 1, (short) 4, (short) 2);
         * t.median(); // returns (short) 2  (sorted: [1,2,3,4], lower middle at index 1)
         *
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of((short) 5, (short) 5, (short) 5, (short) 5);
         * t2.median(); // returns (short) 5  (all duplicates)
         *
         * ShortTuple.ShortTuple4 t3 = ShortTuple.of((short) -4, (short) -3, (short) -2, (short) -1);
         * t3.median(); // returns (short) -3  (sorted: [-4,-3,-2,-1], lower middle)
         *
         * ShortTuple.ShortTuple4 t4 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, Short.MAX_VALUE);
         * t4.median(); // returns (short) -1  (sorted: [MIN_VALUE,-1,0,MAX_VALUE], lower middle)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
         * t.sum(); // returns 10
         *
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of((short) -1, (short) -2, (short) -3, (short) -4);
         * t2.sum(); // returns -10
         *
         * ShortTuple.ShortTuple4 t3 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, Short.MAX_VALUE);
         * t3.sum(); // returns -2  (int arithmetic; no overflow with 4 elements)
         *
         * ShortTuple.ShortTuple4 t4 = ShortTuple.of((short) 0, (short) 0, (short) 0, (short) 0);
         * t4.sum(); // returns 0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
         * t.average(); // returns 2.5
         *
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of((short) -1, (short) -2, (short) -3, (short) -4);
         * t2.average(); // returns -2.5
         *
         * ShortTuple.ShortTuple4 t3 = ShortTuple.of((short) 0, (short) 0, (short) 0, (short) 0);
         * t3.average(); // returns 0.0
         *
         * ShortTuple.ShortTuple4 t4 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, Short.MAX_VALUE);
         * t4.average(); // returns -0.5
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
         * t.reverse(); // returns ShortTuple4 with toString() "(4, 3, 2, 1)"
         *
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of((short) -1, (short) 0, Short.MIN_VALUE, Short.MAX_VALUE);
         * ShortTuple.ShortTuple4 r2 = t2.reverse();
         * // r2._1 == Short.MAX_VALUE, r2._2 == Short.MIN_VALUE, r2._3 == (short) 0, r2._4 == (short) -1
         *
         * ShortTuple.ShortTuple4 t3 = ShortTuple.of((short) 5, (short) 5, (short) 5, (short) 5);
         * t3.reverse(); // returns ShortTuple4 equal to original (all duplicates)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
         * t.contains((short) 1); // returns true
         * t.contains((short) 4); // returns true
         * t.contains((short) 5); // returns false
         * t.contains((short) 0); // returns false
         *
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, Short.MAX_VALUE);
         * t2.contains(Short.MIN_VALUE); // returns true
         * t2.contains((short) 1);       // returns false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
         * List<Short> list = new ArrayList<>();
         * t.forEach(v -> list.add(v)); // list becomes [1, 2, 3, 4] in order
         *
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of((short) -1, (short) 0, (short) 1, (short) 2);
         * int[] sum = {0};
         * t2.forEach(v -> sum[0] += v); // sum[0] becomes 2
         *
         * // t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

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
         * ShortTuple.ShortTuple4 t1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
         * assert t1.hashCode() == t2.hashCode(); // returns true (equal tuples have same hash)
         *
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
         * ShortTuple.ShortTuple4 t1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
         * t1.equals(t2); // returns true
         *
         * ShortTuple.ShortTuple4 t3 = ShortTuple.of((short) 4, (short) 3, (short) 2, (short) 1);
         * t1.equals(t3); // returns false (different order)
         *
         * t1.equals(null);   // returns false
         * t1.equals("text"); // returns false (different type)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple4 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4);
         * t.toString(); // returns "(1, 2, 3, 4)"
         *
         * ShortTuple.ShortTuple4 t2 = ShortTuple.of((short) -1, (short) 0, (short) -100, (short) 100);
         * t2.toString(); // returns "(-1, 0, -100, 100)"
         * }</pre>
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
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple5 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
         * t.arity(); // returns 5
         *
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, Short.MAX_VALUE);
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
         * Returns the minimum short value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple5 t = ShortTuple.of((short) 3, (short) 1, (short) 4, (short) 2, (short) 5);
         * t.min(); // returns (short) 1
         *
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, Short.MAX_VALUE);
         * t2.min(); // returns Short.MIN_VALUE (-32768)
         *
         * ShortTuple.ShortTuple5 t3 = ShortTuple.of((short) 7, (short) 7, (short) 7, (short) 7, (short) 7);
         * t3.min(); // returns (short) 7  (all duplicates)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple5 t = ShortTuple.of((short) 3, (short) 1, (short) 4, (short) 2, (short) 5);
         * t.max(); // returns (short) 5
         *
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, Short.MAX_VALUE);
         * t2.max(); // returns Short.MAX_VALUE (32767)
         *
         * ShortTuple.ShortTuple5 t3 = ShortTuple.of((short) -3, (short) -3, (short) -3, (short) -3, (short) -3);
         * t3.max(); // returns (short) -3  (all duplicates)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple5 t = ShortTuple.of((short) 3, (short) 1, (short) 5, (short) 2, (short) 4);
         * t.median(); // returns (short) 3  (sorted: [1,2,3,4,5], middle at index 2)
         *
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of((short) 7, (short) 7, (short) 7, (short) 7, (short) 7);
         * t2.median(); // returns (short) 7  (all duplicates)
         *
         * ShortTuple.ShortTuple5 t3 = ShortTuple.of((short) -5, (short) -3, (short) -1, (short) -4, (short) -2);
         * t3.median(); // returns (short) -3  (sorted: [-5,-4,-3,-2,-1], middle)
         *
         * ShortTuple.ShortTuple5 t4 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, Short.MAX_VALUE);
         * t4.median(); // returns (short) 0  (middle of 5 values)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple5 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
         * t.sum(); // returns 15
         *
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of((short) -1, (short) -2, (short) -3, (short) -4, (short) -5);
         * t2.sum(); // returns -15
         *
         * ShortTuple.ShortTuple5 t3 = ShortTuple.of((short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
         * t3.sum(); // returns 0
         *
         * ShortTuple.ShortTuple5 t4 = ShortTuple.of((short) -5, (short) -3, (short) -1, (short) 1, (short) 3);
         * t4.sum(); // returns -5
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple5 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
         * t.average(); // returns 3.0
         *
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of((short) -5, (short) -3, (short) -1, (short) 1, (short) 3);
         * t2.average(); // returns -1.0
         *
         * ShortTuple.ShortTuple5 t3 = ShortTuple.of((short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
         * t3.average(); // returns 0.0
         *
         * ShortTuple.ShortTuple5 t4 = ShortTuple.of((short) -2, (short) -1, (short) 0, (short) 1, (short) 2);
         * t4.average(); // returns 0.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple5 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
         * t.reverse(); // returns ShortTuple5 with toString() "(5, 4, 3, 2, 1)"
         *
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of((short) -1, (short) 0, (short) 1, (short) 2, (short) 3);
         * t2.reverse(); // returns ShortTuple5 with toString() "(3, 2, 1, 0, -1)"
         *
         * ShortTuple.ShortTuple5 t3 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, Short.MAX_VALUE);
         * ShortTuple.ShortTuple5 r3 = t3.reverse();
         * // r3._1 == Short.MAX_VALUE, r3._5 == Short.MIN_VALUE
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple5 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
         * t.contains((short) 1); // returns true
         * t.contains((short) 5); // returns true
         * t.contains((short) 0); // returns false
         * t.contains((short) 6); // returns false
         *
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, Short.MAX_VALUE);
         * t2.contains(Short.MIN_VALUE); // returns true
         * t2.contains((short) 2);       // returns false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple5 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
         * List<Short> list = new ArrayList<>();
         * t.forEach(v -> list.add(v)); // list becomes [1, 2, 3, 4, 5] in order
         *
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of((short) -2, (short) -1, (short) 0, (short) 1, (short) 2);
         * int[] sum = {0};
         * t2.forEach(v -> sum[0] += v); // sum[0] becomes 0
         *
         * // t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

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
         * ShortTuple.ShortTuple5 t1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
         * assert t1.hashCode() == t2.hashCode(); // returns true (equal tuples have same hash)
         *
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
         * ShortTuple.ShortTuple5 t1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
         * t1.equals(t2); // returns true
         *
         * ShortTuple.ShortTuple5 t3 = ShortTuple.of((short) 5, (short) 4, (short) 3, (short) 2, (short) 1);
         * t1.equals(t3); // returns false (different order)
         *
         * t1.equals(null);   // returns false
         * t1.equals("text"); // returns false (different type)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple5 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
         * t.toString(); // returns "(1, 2, 3, 4, 5)"
         *
         * ShortTuple.ShortTuple5 t2 = ShortTuple.of((short) -1, (short) 0, (short) -100, (short) 100, (short) 50);
         * t2.toString(); // returns "(-1, 0, -100, 100, 50)"
         * }</pre>
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
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple6 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
         * t.arity(); // returns 6
         *
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, (short) 2, Short.MAX_VALUE);
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
         * Returns the minimum short value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple6 t = ShortTuple.of((short) 3, (short) 1, (short) 4, (short) 2, (short) 6, (short) 5);
         * t.min(); // returns (short) 1
         *
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, (short) 2, Short.MAX_VALUE);
         * t2.min(); // returns Short.MIN_VALUE (-32768)
         *
         * ShortTuple.ShortTuple6 t3 = ShortTuple.of((short) 5, (short) 5, (short) 5, (short) 5, (short) 5, (short) 5);
         * t3.min(); // returns (short) 5  (all duplicates)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple6 t = ShortTuple.of((short) 3, (short) 1, (short) 4, (short) 2, (short) 6, (short) 5);
         * t.max(); // returns (short) 6
         *
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, (short) 2, Short.MAX_VALUE);
         * t2.max(); // returns Short.MAX_VALUE (32767)
         *
         * ShortTuple.ShortTuple6 t3 = ShortTuple.of((short) -7, (short) -7, (short) -7, (short) -7, (short) -7, (short) -7);
         * t3.max(); // returns (short) -7  (all duplicates)
         * }</pre>
         *
         * @return the largest of the six short values
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the median short value in this tuple.
         * For tuples with an even number of elements, returns the lower middle element when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple6 t = ShortTuple.of((short) 3, (short) 1, (short) 4, (short) 2, (short) 6, (short) 5);
         * t.median(); // returns (short) 3  (sorted: [1,2,3,4,5,6], lower middle at index 2)
         *
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of((short) 5, (short) 5, (short) 5, (short) 5, (short) 5, (short) 5);
         * t2.median(); // returns (short) 5  (all duplicates)
         *
         * ShortTuple.ShortTuple6 t3 = ShortTuple.of((short) -6, (short) -5, (short) -4, (short) -3, (short) -2, (short) -1);
         * t3.median(); // returns (short) -4  (sorted: [-6,-5,-4,-3,-2,-1], lower middle)
         *
         * ShortTuple.ShortTuple6 t4 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, (short) 2, Short.MAX_VALUE);
         * t4.median(); // returns (short) 0  (lower middle of 6 boundary values)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple6 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
         * t.sum(); // returns 21
         *
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of((short) -1, (short) -2, (short) -3, (short) -4, (short) -5, (short) -6);
         * t2.sum(); // returns -21
         *
         * ShortTuple.ShortTuple6 t3 = ShortTuple.of((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
         * t3.sum(); // returns 0
         *
         * ShortTuple.ShortTuple6 t4 = ShortTuple.of((short) -3, (short) -2, (short) -1, (short) 1, (short) 2, (short) 3);
         * t4.sum(); // returns 0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple6 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
         * t.average(); // returns 3.5
         *
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
         * t2.average(); // returns 0.0
         *
         * ShortTuple.ShortTuple6 t3 = ShortTuple.of((short) -3, (short) -2, (short) -1, (short) 1, (short) 2, (short) 3);
         * t3.average(); // returns 0.0
         *
         * ShortTuple.ShortTuple6 t4 = ShortTuple.of((short) -1, (short) -2, (short) -3, (short) -4, (short) -5, (short) -6);
         * t4.average(); // returns -3.5
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple6 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
         * t.reverse(); // returns ShortTuple6 with toString() "(6, 5, 4, 3, 2, 1)"
         *
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, (short) 2, Short.MAX_VALUE);
         * ShortTuple.ShortTuple6 r2 = t2.reverse();
         * // r2._1 == Short.MAX_VALUE, r2._6 == Short.MIN_VALUE
         *
         * ShortTuple.ShortTuple6 t3 = ShortTuple.of((short) 5, (short) 5, (short) 5, (short) 5, (short) 5, (short) 5);
         * t3.reverse(); // returns ShortTuple6 equal to original (all duplicates)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple6 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
         * t.contains((short) 1); // returns true
         * t.contains((short) 6); // returns true
         * t.contains((short) 0); // returns false
         * t.contains((short) 7); // returns false
         *
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, (short) 2, Short.MAX_VALUE);
         * t2.contains(Short.MIN_VALUE); // returns true
         * t2.contains((short) 3);       // returns false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple6 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
         * List<Short> list = new ArrayList<>();
         * t.forEach(v -> list.add(v)); // list becomes [1, 2, 3, 4, 5, 6] in order
         *
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of((short) -3, (short) -2, (short) -1, (short) 1, (short) 2, (short) 3);
         * int[] sum = {0};
         * t2.forEach(v -> sum[0] += v); // sum[0] becomes 0
         *
         * // t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

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
         * ShortTuple.ShortTuple6 t1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
         * assert t1.hashCode() == t2.hashCode(); // returns true (equal tuples have same hash)
         *
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
         * ShortTuple.ShortTuple6 t1 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
         * t1.equals(t2); // returns true
         *
         * ShortTuple.ShortTuple6 t3 = ShortTuple.of((short) 6, (short) 5, (short) 4, (short) 3, (short) 2, (short) 1);
         * t1.equals(t3); // returns false (different order)
         *
         * t1.equals(null);   // returns false
         * t1.equals("text"); // returns false (different type)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple6 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6);
         * t.toString(); // returns "(1, 2, 3, 4, 5, 6)"
         *
         * ShortTuple.ShortTuple6 t2 = ShortTuple.of((short) -1, (short) 0, (short) -100, (short) 100, (short) 50, (short) -50);
         * t2.toString(); // returns "(-1, 0, -100, 100, 50, -50)"
         * }</pre>
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
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple7 t = ShortTuple.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7);
         * t.arity(); // returns 7
         *
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, (short) 2, (short) 3, Short.MAX_VALUE);
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
         * Returns the minimum short value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple7 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * short min = t.min();   // returns 1
         *
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of((short)-5, (short)-3, (short)0, (short)2, (short)4, (short)6, (short)8);
         * short min2 = t2.min();   // returns -5
         *
         * ShortTuple.ShortTuple7 t3 = ShortTuple.of(Short.MIN_VALUE, (short)0, (short)1, (short)2, (short)3, (short)4, (short)5);
         * short min3 = t3.min();   // returns Short.MIN_VALUE
         *
         * ShortTuple.ShortTuple7 t4 = ShortTuple.of((short)7, (short)7, (short)7, (short)7, (short)7, (short)7, (short)7);
         * short min4 = t4.min();   // returns 7 (all equal)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple7 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * short max = t.max();   // returns 7
         *
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of((short)-5, (short)-3, (short)0, (short)2, (short)4, (short)6, (short)8);
         * short max2 = t2.max();   // returns 8
         *
         * ShortTuple.ShortTuple7 t3 = ShortTuple.of((short)0, (short)1, (short)2, (short)3, (short)4, (short)5, Short.MAX_VALUE);
         * short max3 = t3.max();   // returns Short.MAX_VALUE
         *
         * ShortTuple.ShortTuple7 t4 = ShortTuple.of((short)3, (short)3, (short)3, (short)3, (short)3, (short)3, (short)3);
         * short max4 = t4.max();   // returns 3 (all equal)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple7 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * short med = t.median();   // returns 4 (middle of sorted [1,2,3,4,5,6,7])
         *
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of((short)7, (short)5, (short)3, (short)1, (short)4, (short)6, (short)2);
         * short med2 = t2.median();   // returns 4 (order does not matter, still middle of sorted)
         *
         * ShortTuple.ShortTuple7 t3 = ShortTuple.of((short)-3, (short)-2, (short)-1, (short)0, (short)1, (short)2, (short)3);
         * short med3 = t3.median();   // returns 0
         *
         * ShortTuple.ShortTuple7 t4 = ShortTuple.of((short)5, (short)5, (short)5, (short)5, (short)5, (short)5, (short)5);
         * short med4 = t4.median();   // returns 5 (all equal)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple7 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * int sum = t.sum();   // returns 28
         *
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of((short)-3, (short)-2, (short)-1, (short)0, (short)1, (short)2, (short)3);
         * int sum2 = t2.sum();   // returns 0
         *
         * ShortTuple.ShortTuple7 t3 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int sum3 = t3.sum();   // returns 0
         *
         * ShortTuple.ShortTuple7 t4 = ShortTuple.of(Short.MAX_VALUE, Short.MAX_VALUE, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int sum4 = t4.sum();   // returns 65534 (no overflow since result is int)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple7 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * double avg = t.average();   // returns 4.0
         *
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of((short)-3, (short)-2, (short)-1, (short)0, (short)1, (short)2, (short)3);
         * double avg2 = t2.average();   // returns 0.0
         *
         * ShortTuple.ShortTuple7 t3 = ShortTuple.of((short)1, (short)1, (short)1, (short)1, (short)1, (short)1, (short)1);
         * double avg3 = t3.average();   // returns 1.0
         *
         * ShortTuple.ShortTuple7 t4 = ShortTuple.of((short)0, (short)0, (short)0, (short)1, (short)1, (short)1, (short)1);
         * double avg4 = t4.average();   // returns 4.0 / 7.0 (approximately 0.5714)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple7 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * ShortTuple.ShortTuple7 r = t.reverse();   // returns (7, 6, 5, 4, 3, 2, 1)
         *
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of((short)-1, (short)0, (short)1, (short)2, (short)3, (short)4, (short)5);
         * ShortTuple.ShortTuple7 r2 = t2.reverse();   // returns (5, 4, 3, 2, 1, 0, -1)
         *
         * ShortTuple.ShortTuple7 t3 = ShortTuple.of((short)3, (short)3, (short)3, (short)3, (short)3, (short)3, (short)3);
         * ShortTuple.ShortTuple7 r3 = t3.reverse();   // returns (3, 3, 3, 3, 3, 3, 3) (identical, all same)
         *
         * ShortTuple.ShortTuple7 t4 = ShortTuple.of(Short.MIN_VALUE, (short)0, (short)0, (short)0, (short)0, (short)0, Short.MAX_VALUE);
         * ShortTuple.ShortTuple7 r4 = t4.reverse();   // r4._1 == Short.MAX_VALUE, r4._7 == Short.MIN_VALUE
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple7 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * boolean b1 = t.contains((short)1);   // returns true (first element)
         * boolean b2 = t.contains((short)7);   // returns true (last element)
         * boolean b3 = t.contains((short)0);   // returns false
         * boolean b4 = t.contains((short)8);   // returns false
         *
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of((short)-1, (short)-2, (short)-3, (short)-4, (short)-5, (short)-6, (short)-7);
         * boolean b5 = t2.contains((short)-7);   // returns true
         * boolean b6 = t2.contains((short)7);    // returns false (wrong sign)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple7 t = ShortTuple.of((short)10, (short)20, (short)30, (short)40, (short)50, (short)60, (short)70);
         * int[] sum = {0};
         * t.forEach(v -> sum[0] += v);   // sum[0] == 280 after iteration
         *
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of((short)-1, (short)-2, (short)-3, (short)-4, (short)-5, (short)-6, (short)-7);
         * int[] count = {0};
         * t2.forEach(v -> { if (v < 0) count[0]++; });   // count[0] == 7 (all negative)
         *
         * // throws IllegalArgumentException if action is null
         * // t.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

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
         * ShortTuple.ShortTuple7 t1 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * boolean same = (t1.hashCode() == t2.hashCode()); // returns true (equal tuples have equal hash)
         *
         * ShortTuple.ShortTuple7 t3 = ShortTuple.of((short)7, (short)6, (short)5, (short)4, (short)3, (short)2, (short)1);
         * boolean diff = (t1.hashCode() == t3.hashCode());   // returns false (order matters)
         *
         * ShortTuple.ShortTuple7 t4 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int h = t4.hashCode();   // returns 0 (all-zero tuple)
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
         * ShortTuple.ShortTuple7 t1 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * boolean eq = t1.equals(t2);   // returns true
         *
         * boolean self = t1.equals(t1);   // returns true (reflexive)
         *
         * ShortTuple.ShortTuple7 t3 = ShortTuple.of((short)7, (short)6, (short)5, (short)4, (short)3, (short)2, (short)1);
         * boolean neq = t1.equals(t3);   // returns false (different order)
         *
         * boolean nullCheck = t1.equals(null);       // returns false
         * boolean typeCheck = t1.equals("string");   // returns false (wrong type)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple7 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * String s = t.toString();   // returns "(1, 2, 3, 4, 5, 6, 7)"
         *
         * ShortTuple.ShortTuple7 t2 = ShortTuple.of((short)-1, (short)-2, (short)-3, (short)-4, (short)-5, (short)-6, (short)-7);
         * String s2 = t2.toString();   // returns "(-1, -2, -3, -4, -5, -6, -7)"
         *
         * ShortTuple.ShortTuple7 t3 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * String s3 = t3.toString();   // returns "(0, 0, 0, 0, 0, 0, 0)"
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple8 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * int n = t.arity();   // returns 8
         *
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int n2 = t2.arity();   // returns 8 (always 8, regardless of values)
         *
         * ShortTuple.ShortTuple8 t3 = ShortTuple.of(Short.MIN_VALUE, Short.MAX_VALUE, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int n3 = t3.arity();   // returns 8
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple8 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * short min = t.min();   // returns 1
         *
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of(Short.MIN_VALUE, (short)0, (short)1, (short)2, (short)3, (short)4, (short)5, (short)6);
         * short min2 = t2.min();   // returns Short.MIN_VALUE
         *
         * ShortTuple.ShortTuple8 t3 = ShortTuple.of((short)-8, (short)-6, (short)-4, (short)-2, (short)0, (short)2, (short)4, (short)6);
         * short min3 = t3.min();   // returns -8
         *
         * ShortTuple.ShortTuple8 t4 = ShortTuple.of((short)5, (short)5, (short)5, (short)5, (short)5, (short)5, (short)5, (short)5);
         * short min4 = t4.min();   // returns 5 (all equal)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple8 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * short max = t.max();   // returns 8
         *
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of((short)0, (short)1, (short)2, (short)3, (short)4, (short)5, (short)6, Short.MAX_VALUE);
         * short max2 = t2.max();   // returns Short.MAX_VALUE
         *
         * ShortTuple.ShortTuple8 t3 = ShortTuple.of((short)-8, (short)-6, (short)-4, (short)-2, (short)0, (short)2, (short)4, (short)6);
         * short max3 = t3.max();   // returns 6
         *
         * ShortTuple.ShortTuple8 t4 = ShortTuple.of((short)9, (short)9, (short)9, (short)9, (short)9, (short)9, (short)9, (short)9);
         * short max4 = t4.max();   // returns 9 (all equal)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple8 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * short med = t.median();   // returns 4 (lower middle of sorted [1,2,3,4,5,6,7,8])
         *
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of((short)8, (short)7, (short)6, (short)5, (short)4, (short)3, (short)2, (short)1);
         * short med2 = t2.median();   // returns 4 (order does not matter)
         *
         * ShortTuple.ShortTuple8 t3 = ShortTuple.of((short)-4, (short)-3, (short)-2, (short)-1, (short)0, (short)1, (short)2, (short)3);
         * short med3 = t3.median();   // returns -1 (lower middle of sorted [-4,-3,-2,-1,0,1,2,3])
         *
         * ShortTuple.ShortTuple8 t4 = ShortTuple.of((short)5, (short)5, (short)5, (short)5, (short)5, (short)5, (short)5, (short)5);
         * short med4 = t4.median();   // returns 5 (all equal)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple8 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * int sum = t.sum();   // returns 36
         *
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of((short)-4, (short)-3, (short)-2, (short)-1, (short)0, (short)1, (short)2, (short)3);
         * int sum2 = t2.sum();   // returns -4
         *
         * ShortTuple.ShortTuple8 t3 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int sum3 = t3.sum();   // returns 0
         *
         * ShortTuple.ShortTuple8 t4 = ShortTuple.of(Short.MAX_VALUE, Short.MAX_VALUE, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int sum4 = t4.sum();   // returns 65534 (no overflow since result is int)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple8 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * double avg = t.average();   // returns 4.5
         *
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * double avg2 = t2.average();   // returns 0.0
         *
         * ShortTuple.ShortTuple8 t3 = ShortTuple.of((short)-4, (short)-3, (short)-2, (short)-1, (short)0, (short)1, (short)2, (short)3);
         * double avg3 = t3.average();   // returns -0.5 (sum=-4, divided by 8)
         *
         * ShortTuple.ShortTuple8 t4 = ShortTuple.of((short)1, (short)1, (short)1, (short)1, (short)1, (short)1, (short)1, (short)1);
         * double avg4 = t4.average();   // returns 1.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple8 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * ShortTuple.ShortTuple8 r = t.reverse();   // returns (8, 7, 6, 5, 4, 3, 2, 1)
         *
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of((short)-1, (short)0, (short)1, (short)2, (short)3, (short)4, (short)5, (short)6);
         * ShortTuple.ShortTuple8 r2 = t2.reverse();   // returns (6, 5, 4, 3, 2, 1, 0, -1)
         *
         * ShortTuple.ShortTuple8 t3 = ShortTuple.of((short)3, (short)3, (short)3, (short)3, (short)3, (short)3, (short)3, (short)3);
         * ShortTuple.ShortTuple8 r3 = t3.reverse();   // returns (3, 3, 3, 3, 3, 3, 3, 3) (identical, all same)
         *
         * ShortTuple.ShortTuple8 t4 = ShortTuple.of(Short.MIN_VALUE, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, Short.MAX_VALUE);
         * ShortTuple.ShortTuple8 r4 = t4.reverse();   // r4._1 == Short.MAX_VALUE, r4._8 == Short.MIN_VALUE
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple8 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * boolean b1 = t.contains((short)1);   // returns true (first element)
         * boolean b2 = t.contains((short)8);   // returns true (last element)
         * boolean b3 = t.contains((short)0);   // returns false
         * boolean b4 = t.contains((short)9);   // returns false
         *
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of((short)-1, (short)-2, (short)-3, (short)-4, (short)-5, (short)-6, (short)-7, (short)-8);
         * boolean b5 = t2.contains((short)-8);   // returns true
         * boolean b6 = t2.contains((short)8);    // returns false (wrong sign)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple8 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * int[] sum = {0};
         * t.forEach(v -> sum[0] += v);   // sum[0] == 36 after iteration
         *
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of((short)-1, (short)-2, (short)-3, (short)-4, (short)-5, (short)-6, (short)-7, (short)-8);
         * int[] count = {0};
         * t2.forEach(v -> { if (v < 0) count[0]++; });   // count[0] == 8 (all negative)
         *
         * // throws IllegalArgumentException if action is null
         * // t.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

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
         * ShortTuple.ShortTuple8 t1 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * boolean same = (t1.hashCode() == t2.hashCode()); // returns true (equal tuples have equal hash)
         *
         * ShortTuple.ShortTuple8 t3 = ShortTuple.of((short)8, (short)7, (short)6, (short)5, (short)4, (short)3, (short)2, (short)1);
         * boolean diff = (t1.hashCode() == t3.hashCode());   // returns false (order matters)
         *
         * ShortTuple.ShortTuple8 t4 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int h = t4.hashCode();   // returns 0 (all-zero tuple)
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
         * ShortTuple.ShortTuple8 t1 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * boolean eq = t1.equals(t2);   // returns true
         *
         * boolean self = t1.equals(t1);   // returns true (reflexive)
         *
         * ShortTuple.ShortTuple8 t3 = ShortTuple.of((short)8, (short)7, (short)6, (short)5, (short)4, (short)3, (short)2, (short)1);
         * boolean neq = t1.equals(t3);   // returns false (different order)
         *
         * boolean nullCheck = t1.equals(null);       // returns false
         * boolean typeCheck = t1.equals("string");   // returns false (wrong type)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple8 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8);
         * String s = t.toString();   // returns "(1, 2, 3, 4, 5, 6, 7, 8)"
         *
         * ShortTuple.ShortTuple8 t2 = ShortTuple.of((short)-1, (short)-2, (short)-3, (short)-4, (short)-5, (short)-6, (short)-7, (short)-8);
         * String s2 = t2.toString();   // returns "(-1, -2, -3, -4, -5, -6, -7, -8)"
         *
         * ShortTuple.ShortTuple8 t3 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * String s3 = t3.toString();   // returns "(0, 0, 0, 0, 0, 0, 0, 0)"
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple9 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * int n = t.arity();   // returns 9
         *
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int n2 = t2.arity();   // returns 9 (always 9, regardless of values)
         *
         * ShortTuple.ShortTuple9 t3 = ShortTuple.of(Short.MIN_VALUE, Short.MAX_VALUE, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int n3 = t3.arity();   // returns 9
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple9 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * short min = t.min();   // returns 1
         *
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of(Short.MIN_VALUE, (short)0, (short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * short min2 = t2.min();   // returns Short.MIN_VALUE
         *
         * ShortTuple.ShortTuple9 t3 = ShortTuple.of((short)-9, (short)-7, (short)-5, (short)-3, (short)-1, (short)1, (short)3, (short)5, (short)7);
         * short min3 = t3.min();   // returns -9
         *
         * ShortTuple.ShortTuple9 t4 = ShortTuple.of((short)4, (short)4, (short)4, (short)4, (short)4, (short)4, (short)4, (short)4, (short)4);
         * short min4 = t4.min();   // returns 4 (all equal)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple9 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * short max = t.max();   // returns 9
         *
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of((short)0, (short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, Short.MAX_VALUE);
         * short max2 = t2.max();   // returns Short.MAX_VALUE
         *
         * ShortTuple.ShortTuple9 t3 = ShortTuple.of((short)-9, (short)-7, (short)-5, (short)-3, (short)-1, (short)1, (short)3, (short)5, (short)7);
         * short max3 = t3.max();   // returns 7
         *
         * ShortTuple.ShortTuple9 t4 = ShortTuple.of((short)2, (short)2, (short)2, (short)2, (short)2, (short)2, (short)2, (short)2, (short)2);
         * short max4 = t4.max();   // returns 2 (all equal)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple9 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * short med = t.median();   // returns 5 (middle of sorted [1,2,3,4,5,6,7,8,9])
         *
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of((short)9, (short)8, (short)7, (short)6, (short)5, (short)4, (short)3, (short)2, (short)1);
         * short med2 = t2.median();   // returns 5 (order does not matter)
         *
         * ShortTuple.ShortTuple9 t3 = ShortTuple.of((short)-4, (short)-3, (short)-2, (short)-1, (short)0, (short)1, (short)2, (short)3, (short)4);
         * short med3 = t3.median();   // returns 0
         *
         * ShortTuple.ShortTuple9 t4 = ShortTuple.of((short)7, (short)7, (short)7, (short)7, (short)7, (short)7, (short)7, (short)7, (short)7);
         * short med4 = t4.median();   // returns 7 (all equal)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple9 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * int sum = t.sum();   // returns 45
         *
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of((short)-4, (short)-3, (short)-2, (short)-1, (short)0, (short)1, (short)2, (short)3, (short)4);
         * int sum2 = t2.sum();   // returns 0
         *
         * ShortTuple.ShortTuple9 t3 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int sum3 = t3.sum();   // returns 0
         *
         * ShortTuple.ShortTuple9 t4 = ShortTuple.of(Short.MAX_VALUE, Short.MAX_VALUE, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int sum4 = t4.sum();   // returns 65534 (no overflow since result is int)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple9 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * double avg = t.average();   // returns 5.0
         *
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of((short)-4, (short)-3, (short)-2, (short)-1, (short)0, (short)1, (short)2, (short)3, (short)4);
         * double avg2 = t2.average();   // returns 0.0
         *
         * ShortTuple.ShortTuple9 t3 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * double avg3 = t3.average();   // returns 0.0
         *
         * ShortTuple.ShortTuple9 t4 = ShortTuple.of((short)1, (short)1, (short)1, (short)1, (short)1, (short)1, (short)1, (short)1, (short)1);
         * double avg4 = t4.average();   // returns 1.0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple9 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * ShortTuple.ShortTuple9 r = t.reverse();   // returns (9, 8, 7, 6, 5, 4, 3, 2, 1)
         *
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of((short)-1, (short)0, (short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7);
         * ShortTuple.ShortTuple9 r2 = t2.reverse();   // returns (7, 6, 5, 4, 3, 2, 1, 0, -1)
         *
         * ShortTuple.ShortTuple9 t3 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * ShortTuple.ShortTuple9 r3 = t3.reverse();   // returns (0, 0, 0, 0, 0, 0, 0, 0, 0) (identical, all zero)
         *
         * ShortTuple.ShortTuple9 t4 = ShortTuple.of(Short.MIN_VALUE, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, Short.MAX_VALUE);
         * ShortTuple.ShortTuple9 r4 = t4.reverse();   // r4._1 == Short.MAX_VALUE, r4._9 == Short.MIN_VALUE
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple9 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * boolean b1 = t.contains((short)1);    // returns true (first element)
         * boolean b2 = t.contains((short)9);    // returns true (last element)
         * boolean b3 = t.contains((short)0);    // returns false
         * boolean b4 = t.contains((short)10);   // returns false
         *
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of((short)-1, (short)-2, (short)-3, (short)-4, (short)-5, (short)-6, (short)-7, (short)-8, (short)-9);
         * boolean b5 = t2.contains((short)-9);   // returns true
         * boolean b6 = t2.contains((short)9);    // returns false (wrong sign)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple9 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * int[] sum = {0};
         * t.forEach(v -> sum[0] += v);   // sum[0] == 45 after iteration
         *
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of((short)-1, (short)-2, (short)-3, (short)-4, (short)-5, (short)-6, (short)-7, (short)-8, (short)-9);
         * int[] count = {0};
         * t2.forEach(v -> { if (v < 0) count[0]++; });   // count[0] == 9 (all negative)
         *
         * // throws IllegalArgumentException if action is null
         * // t.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

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
         * ShortTuple.ShortTuple9 t1 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * boolean same = (t1.hashCode() == t2.hashCode()); // returns true (equal tuples have equal hash)
         *
         * ShortTuple.ShortTuple9 t3 = ShortTuple.of((short)9, (short)8, (short)7, (short)6, (short)5, (short)4, (short)3, (short)2, (short)1);
         * boolean diff = (t1.hashCode() == t3.hashCode());   // returns false (order matters)
         *
         * ShortTuple.ShortTuple9 t4 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * int h = t4.hashCode();   // returns 0 (all-zero tuple)
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
         * ShortTuple.ShortTuple9 t1 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * boolean eq = t1.equals(t2);   // returns true
         *
         * boolean self = t1.equals(t1);   // returns true (reflexive)
         *
         * ShortTuple.ShortTuple9 t3 = ShortTuple.of((short)9, (short)8, (short)7, (short)6, (short)5, (short)4, (short)3, (short)2, (short)1);
         * boolean neq = t1.equals(t3);   // returns false (different order)
         *
         * boolean nullCheck = t1.equals(null);       // returns false
         * boolean typeCheck = t1.equals("string");   // returns false (wrong type)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * ShortTuple.ShortTuple9 t = ShortTuple.of((short)1, (short)2, (short)3, (short)4, (short)5, (short)6, (short)7, (short)8, (short)9);
         * String s = t.toString();   // returns "(1, 2, 3, 4, 5, 6, 7, 8, 9)"
         *
         * ShortTuple.ShortTuple9 t2 = ShortTuple.of((short)-1, (short)-2, (short)-3, (short)-4, (short)-5, (short)-6, (short)-7, (short)-8, (short)-9);
         * String s2 = t2.toString();   // returns "(-1, -2, -3, -4, -5, -6, -7, -8, -9)"
         *
         * ShortTuple.ShortTuple9 t3 = ShortTuple.of((short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
         * String s3 = t3.toString();   // returns "(0, 0, 0, 0, 0, 0, 0, 0, 0)"
         * }</pre>
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
