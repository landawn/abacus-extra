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
import com.landawn.abacus.util.FloatTuple.FloatTuple0;
import com.landawn.abacus.util.FloatTuple.FloatTuple1;
import com.landawn.abacus.util.FloatTuple.FloatTuple2;
import com.landawn.abacus.util.FloatTuple.FloatTuple3;
import com.landawn.abacus.util.FloatTuple.FloatTuple4;
import com.landawn.abacus.util.FloatTuple.FloatTuple5;
import com.landawn.abacus.util.FloatTuple.FloatTuple6;
import com.landawn.abacus.util.FloatTuple.FloatTuple7;
import com.landawn.abacus.util.FloatTuple.FloatTuple8;
import com.landawn.abacus.util.FloatTuple.FloatTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.FloatStream;

/**
 * Base class for immutable tuples of primitive {@code float} values.
 *
 * <p>The nested tuple types model fixed arities from 0 through 9. Factory methods such as
 * {@link #copyOf(float[])} and the {@code of(...)} overloads select the matching subtype, while the
 * base class supplies aggregate, reversal, containment, and functional helper operations.</p>
 *
 * <p>This sealed base class permits only the built-in arity-specific nested tuple types.</p>
 *
 * @param <TP> the concrete {@code FloatTuple} subtype that fluent operations such as {@link #reverse()} return
 * @see PrimitiveTuple
 * @see BooleanTuple
 * @see ByteTuple
 * @see CharTuple
 * @see ShortTuple
 * @see IntTuple
 * @see LongTuple
 * @see DoubleTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract sealed class FloatTuple<TP extends FloatTuple<TP>> extends PrimitiveTuple<TP>
        permits FloatTuple0, FloatTuple1, FloatTuple2, FloatTuple3, FloatTuple4, FloatTuple5, FloatTuple6, FloatTuple7, FloatTuple8, FloatTuple9 {

    /** Lazily initialized cached array view of all tuple elements. */
    protected volatile float[] elements;

    /**
     * Protected constructor for subclass instantiation.
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link #of(float)}, {@link #of(float, float)}, etc., to create tuple instances.
     */
    protected FloatTuple() {
    }

    /**
     * Creates a FloatTuple.FloatTuple1 containing a single float value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.FloatTuple1 single = FloatTuple.of(3.14f);
     * single._1                                       // returns 3.14f
     * single.arity()                                  // returns 1
     *
     * // Edge: NaN is a valid element
     * FloatTuple.FloatTuple1 nanTuple = FloatTuple.of(Float.NaN);
     * Float.isNaN(nanTuple._1)                        // returns true
     *
     * // Edge: positive infinity
     * FloatTuple.FloatTuple1 infTuple = FloatTuple.of(Float.POSITIVE_INFINITY);
     * infTuple._1 == Float.POSITIVE_INFINITY          // returns true
     *
     * // Edge: single-element operations all delegate to _1
     * FloatTuple.of(5.0f).min()                       // returns 5.0f
     * FloatTuple.of(5.0f).max()                       // returns 5.0f
     * }</pre>
     *
     * @param _1 the float value to store in the tuple
     * @return a new FloatTuple.FloatTuple1 containing the specified value
     */
    public static FloatTuple1 of(final float _1) {
        return new FloatTuple1(_1);
    }

    /**
     * Creates a FloatTuple.FloatTuple2 containing two float values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.FloatTuple2 pair = FloatTuple.of(1.5f, 2.5f);
     * pair._1                                         // returns 1.5f
     * pair._2                                         // returns 2.5f
     *
     * // Reverse produces a new tuple with elements swapped
     * pair.reverse()._1                               // returns 2.5f
     *
     * // Edge: NaN element - min/max/median propagate NaN
     * FloatTuple.FloatTuple2 nanPair = FloatTuple.of(1.0f, Float.NaN);
     * Float.isNaN(nanPair.min())                      // returns true
     *
     * // Edge: infinity element
     * FloatTuple.of(1.0f, Float.POSITIVE_INFINITY).max()  // returns Float.POSITIVE_INFINITY
     * }</pre>
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @return a new FloatTuple.FloatTuple2 containing the specified values
     */
    public static FloatTuple2 of(final float _1, final float _2) {
        return new FloatTuple2(_1, _2);
    }

    /**
     * Creates a FloatTuple.FloatTuple3 containing three float values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.FloatTuple3 triple = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * triple._3                                       // returns 3.0f
     * triple.sum()                                    // returns 6.0f
     *
     * // Median of three: middle value when sorted
     * FloatTuple.of(30.0f, 10.0f, 20.0f).median()    // returns 20.0f
     *
     * // Edge: NaN propagates through sum
     * FloatTuple.of(1.0f, Float.NaN, 3.0f).sum()     // returns NaN (Float.isNaN == true)
     *
     * // Edge: reversed triple
     * FloatTuple.of(1.0f, 2.0f, 3.0f).reverse()._1   // returns 3.0f
     * }</pre>
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @return a new FloatTuple.FloatTuple3 containing the specified values
     */
    public static FloatTuple3 of(final float _1, final float _2, final float _3) {
        return new FloatTuple3(_1, _2, _3);
    }

    /**
     * Creates a FloatTuple.FloatTuple4 containing four float values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.FloatTuple4 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
     * tuple._4                                        // returns 4.0f
     * tuple.sum()                                     // returns 10.0f
     *
     * // Median of four (even count): lower middle value when sorted
     * FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f).median() // returns 2.0f
     *
     * // Edge: all negative values
     * FloatTuple.of(-4.0f, -1.0f, -3.0f, -2.0f).min() // returns -4.0f
     *
     * // Edge: NaN propagates through average
     * Double.isNaN(FloatTuple.of(1.0f, Float.NaN, 3.0f, 4.0f).average()) // returns true
     * }</pre>
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @param _4 the fourth float value
     * @return a new FloatTuple.FloatTuple4 containing the specified values
     */
    public static FloatTuple4 of(final float _1, final float _2, final float _3, final float _4) {
        return new FloatTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a FloatTuple.FloatTuple5 containing five float values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.FloatTuple5 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
     * tuple.median()                                  // returns 3.0f
     * tuple.average()                                 // returns 3.0
     *
     * // Reverse produces a new tuple
     * tuple.reverse()._1                              // returns 5.0f
     *
     * // Edge: NaN propagates through max
     * Float.isNaN(FloatTuple.of(1.0f, 2.0f, Float.NaN, 4.0f, 5.0f).max()) // returns true
     *
     * // Edge: infinity - max is infinity
     * FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, Float.POSITIVE_INFINITY).max() // returns Float.POSITIVE_INFINITY
     * }</pre>
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @param _4 the fourth float value
     * @param _5 the fifth float value
     * @return a new FloatTuple.FloatTuple5 containing the specified values
     */
    public static FloatTuple5 of(final float _1, final float _2, final float _3, final float _4, final float _5) {
        return new FloatTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a FloatTuple.FloatTuple6 containing six float values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.FloatTuple6 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
     * tuple.sum()                                     // returns 21.0f
     * tuple.average()                                 // returns 3.5
     *
     * // Median of six (even count): lower middle value when sorted
     * FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f).median()  // returns 3.0f
     *
     * // Edge: NaN in sum
     * Float.isNaN(FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, Float.NaN).sum()) // returns true
     *
     * // Edge: contains check with a specific value
     * FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f).contains(4.0f) // returns true
     * }</pre>
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @param _4 the fourth float value
     * @param _5 the fifth float value
     * @param _6 the sixth float value
     * @return a new FloatTuple.FloatTuple6 containing the specified values
     */
    public static FloatTuple6 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6) {
        return new FloatTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a FloatTuple.FloatTuple7 containing seven float values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.FloatTuple7 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
     * tuple.median()                                  // returns 4.0f
     * tuple.sum()                                     // returns 28.0f
     *
     * // Reverse produces a new tuple with elements in opposite order
     * FloatTuple.FloatTuple7 reversed = tuple.reverse();
     * reversed._1                                     // returns 7.0f
     * reversed._7                                     // returns 1.0f
     *
     * // Edge: NaN propagates through min
     * Float.isNaN(FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, Float.NaN).min()) // returns true
     *
     * // Edge: negative infinity is the minimum
     * FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, Float.NEGATIVE_INFINITY).min() // returns Float.NEGATIVE_INFINITY
     * }</pre>
     *
     * @param _1 the first float value
     * @param _2 the second float value
     * @param _3 the third float value
     * @param _4 the fourth float value
     * @param _5 the fifth float value
     * @param _6 the sixth float value
     * @param _7 the seventh float value
     * @return a new FloatTuple.FloatTuple7 containing the specified values
     */
    public static FloatTuple7 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7) {
        return new FloatTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a FloatTuple.FloatTuple8 containing eight float values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.FloatTuple8 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
     * tuple.toArray().length                          // returns 8
     * tuple.sum()                                     // returns 36.0f
     *
     * // Median of eight (even count): lower middle value when sorted
     * tuple.median()                                  // returns 4.0f
     *
     * // Edge: contains check
     * tuple.contains(8.0f)                            // returns true
     * tuple.contains(9.0f)                            // returns false
     *
     * // Edge: NaN element - average produces NaN
     * Double.isNaN(FloatTuple.of(1.0f,2.0f,3.0f,4.0f,5.0f,6.0f,7.0f,Float.NaN).average()) // returns true
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
     * @return a new FloatTuple.FloatTuple8 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more float values
     */
    @Deprecated
    public static FloatTuple8 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7,
            final float _8) {
        return new FloatTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a FloatTuple.FloatTuple9 containing nine float values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.FloatTuple9 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
     * tuple.sum()                                     // returns 45.0f
     * tuple.median()                                  // returns 5.0f
     *
     * // Reverse produces a new tuple with elements in opposite order
     * FloatTuple.FloatTuple9 reversed = tuple.reverse();
     * reversed._1                                     // returns 9.0f
     * reversed._9                                     // returns 1.0f
     *
     * // Edge: NaN propagates through sum
     * Float.isNaN(FloatTuple.of(1.0f,2.0f,3.0f,4.0f,Float.NaN,6.0f,7.0f,8.0f,9.0f).sum()) // returns true
     *
     * // Edge: infinity - max is infinity
     * FloatTuple.of(1.0f,2.0f,3.0f,4.0f,5.0f,6.0f,7.0f,8.0f,Float.POSITIVE_INFINITY).max() // returns Float.POSITIVE_INFINITY
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
     * @return a new FloatTuple.FloatTuple9 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more float values
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
     * This factory method supports arrays with 0 to 9 elements. For {@code null} or empty
     * arrays, returns the shared empty tuple. For arrays with 1-9 elements, returns the
     * corresponding {@code FloatTuple1}..{@code FloatTuple9} instance. The values are copied
     * into the new tuple; subsequent modifications to the input array do not affect it.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Three-element array produces a FloatTuple3
     * FloatTuple.FloatTuple3 t3 = FloatTuple.copyOf(new float[]{1.0f, 2.0f, 3.0f});
     * t3._1                                           // returns 1.0f
     *
     * // Single-element array produces a FloatTuple1
     * FloatTuple.FloatTuple1 t1 = FloatTuple.copyOf(new float[]{3.14f});
     * t1._1                                           // returns 3.14f
     *
     * // null or empty array produces the empty (arity-0) tuple
     * FloatTuple.copyOf(null).arity()                 // returns 0
     * FloatTuple.copyOf(new float[0]).arity()         // returns 0
     *
     * // Array longer than 9 throws IllegalArgumentException
     * FloatTuple.copyOf(new float[10])                // throws IllegalArgumentException
     * }</pre>
     *
     * <p><strong>Type note:</strong> the runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type. Assigning to the wrong arity-specific subtype will result in a
     * {@link ClassCastException} at the assignment site.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of float values; may be {@code null} or empty, in which case the shared empty tuple is returned
     * @return a {@code FloatTuple} of the appropriate arity containing the array values, or the shared empty tuple if the array is {@code null} or empty
     * @throws IllegalArgumentException if {@code values} has more than 9 elements
     * @see #of(float)
     */
    @SuppressWarnings("deprecation")
    public static <TP extends FloatTuple<TP>> TP copyOf(final float[] values) {
        if (values == null || values.length == 0) {
            return (TP) FloatTuple0.EMPTY;
        }

        switch (values.length) {
            case 1:
                return (TP) FloatTuple.of(values[0]);

            case 2:
                return (TP) FloatTuple.of(values[0], values[1]);

            case 3:
                return (TP) FloatTuple.of(values[0], values[1], values[2]);

            case 4:
                return (TP) FloatTuple.of(values[0], values[1], values[2], values[3]);

            case 5:
                return (TP) FloatTuple.of(values[0], values[1], values[2], values[3], values[4]);

            case 6:
                return (TP) FloatTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);

            case 7:
                return (TP) FloatTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);

            case 8:
                return (TP) FloatTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);

            case 9:
                return (TP) FloatTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);

            default:
                throw new IllegalArgumentException("Too many elements (" + values.length + "). Maximum: 9");
        }
    }

    /**
     * Returns the minimum float value in this tuple.
     * <p>
     * This method finds and returns the smallest float value among all elements
     * in the tuple. For tuples with a single element, returns that element.
     * </p>
     * <p>
     * Comparisons are performed pairwise with {@link Math#min(float, float)}, so any
     * {@code NaN} element causes the result to be {@code NaN}, and {@code -0.0f} is
     * treated as smaller than {@code +0.0f}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.of(3.0f, 1.0f, 2.0f).min()          // returns 1.0f
     * FloatTuple.of(2.5f, 1.5f).min()                // returns 1.5f
     *
     * // Edge: NaN propagates - any NaN element makes result NaN
     * Float.isNaN(FloatTuple.of(1.0f, Float.NaN).min())           // returns true
     *
     * // Edge: negative infinity is the minimum
     * FloatTuple.of(0.0f, Float.NEGATIVE_INFINITY).min()           // returns Float.NEGATIVE_INFINITY
     *
     * // Edge: empty tuple throws NoSuchElementException
     * FloatTuple.copyOf(new float[0]).min()                        // throws NoSuchElementException
     * }</pre>
     *
     * @return the minimum float value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #max()
     * @see Math#min(float, float)
     */
    public float min() {
        final float[] arr = elements();
        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }
        float result = arr[0];
        for (int i = 1; i < arr.length; i++) {
            result = Math.min(result, arr[i]);
        }
        return result;
    }

    /**
     * Returns the maximum float value in this tuple.
     * <p>
     * This method finds and returns the largest float value among all elements
     * in the tuple. For tuples with a single element, returns that element.
     * </p>
     * <p>
     * Comparisons are performed pairwise with {@link Math#max(float, float)}, so any
     * {@code NaN} element causes the result to be {@code NaN}, and {@code +0.0f} is
     * treated as larger than {@code -0.0f}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.of(3.0f, 1.0f, 2.0f).max()          // returns 3.0f
     * FloatTuple.of(1.5f, 2.5f).max()                // returns 2.5f
     *
     * // Edge: NaN propagates - any NaN element makes result NaN
     * Float.isNaN(FloatTuple.of(1.0f, Float.NaN).max())           // returns true
     *
     * // Edge: positive infinity is the maximum
     * FloatTuple.of(1.0f, Float.POSITIVE_INFINITY).max()           // returns Float.POSITIVE_INFINITY
     *
     * // Edge: empty tuple throws NoSuchElementException
     * FloatTuple.copyOf(new float[0]).max()                        // throws NoSuchElementException
     * }</pre>
     *
     * @return the maximum float value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see Math#max(float, float)
     */
    public float max() {
        final float[] arr = elements();
        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }
        float result = arr[0];
        for (int i = 1; i < arr.length; i++) {
            result = Math.max(result, arr[i]);
        }
        return result;
    }

    /**
     * Returns the median value of the elements in this tuple.
     * <p>
     * For tuples with an odd number of elements, returns the middle value when sorted.
     * For tuples with an even number of elements, returns the lower middle value
     * (not the average of the two middle values).
     * </p>
     * <p>
     * For tuples with three or more elements, ordering follows {@link Float#compare(float, float)}
     * semantics, so {@code NaN} is treated as the largest value (and equal to itself), and
     * {@code -0.0f} is treated as less than {@code +0.0f}. For two-element tuples the result
     * is computed via {@link Math#min(float, float)}, so the result is {@code NaN} if either
     * element is {@code NaN}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Odd number of elements: middle value of the sorted sequence
     * FloatTuple.of(30.0f, 10.0f, 20.0f).median()          // returns 20.0f  (sorted: 10, 20, 30)
     * FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f).median() // returns 3.0f
     *
     * // Even number of elements: lower middle value of the sorted sequence
     * FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f).median() // returns 2.0f
     *
     * // Edge: two-element tuple - uses Math.min, so NaN propagates
     * Float.isNaN(FloatTuple.of(1.0f, Float.NaN).median())            // returns true
     *
     * // Edge: empty tuple throws NoSuchElementException
     * FloatTuple.copyOf(new float[0]).median()                        // throws NoSuchElementException
     * }</pre>
     *
     * @return the median float element in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see N#median(float...)
     */
    public float median() {
        final float[] arr = elements();
        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }
        return N.median(arr);
    }

    /**
     * Returns the sum of all float values in this tuple.
     * <p>
     * For an empty tuple, returns {@code 0.0f}. If any element is {@code NaN},
     * the result is {@code NaN}. Infinities follow standard IEEE-754 addition rules
     * (e.g. {@code +INF + -INF} produces {@code NaN}).
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.of(1.0f, 2.0f, 3.0f).sum()          // returns 6.0f
     * FloatTuple.of(1.5f, 2.5f).sum()                // returns 4.0f
     *
     * // Edge: empty tuple returns 0.0f (no exception)
     * FloatTuple.copyOf(new float[0]).sum()            // returns 0.0f
     *
     * // Edge: NaN propagates through sum
     * Float.isNaN(FloatTuple.of(1.0f, Float.NaN).sum())           // returns true
     *
     * // Edge: infinity
     * FloatTuple.of(1.0f, Float.POSITIVE_INFINITY).sum()          // returns Float.POSITIVE_INFINITY
     * }</pre>
     *
     * @return the sum of all float values in this tuple, or {@code 0.0f} if empty
     * @see #average()
     */
    public float sum() {
        return N.sum(elements());
    }

    /**
     * Returns the arithmetic mean of all float values in this tuple.
     * <p>
     * The result is returned as a {@code double} to preserve precision.
     * If any element is {@code NaN}, the result is {@code NaN}. Infinities
     * follow standard IEEE-754 rules.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.of(1.0f, 2.0f, 3.0f).average()      // returns 2.0
     * FloatTuple.of(1.0f, 2.0f).average()            // returns 1.5
     *
     * // Edge: empty tuple throws NoSuchElementException
     * FloatTuple.copyOf(new float[0]).average()        // throws NoSuchElementException
     *
     * // Edge: NaN propagates - result is Double NaN
     * Double.isNaN(FloatTuple.of(1.0f, Float.NaN).average())      // returns true
     *
     * // Edge: infinity
     * FloatTuple.of(1.0f, Float.POSITIVE_INFINITY).average()      // returns Double.POSITIVE_INFINITY
     * }</pre>
     *
     * @return the average of all float values in this tuple as a {@code double}
     * @throws NoSuchElementException if the tuple is empty
     * @see #sum()
     */
    public double average() {
        final float[] arr = elements();
        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }
        return N.average(arr);
    }

    /**
     * Returns a tuple with the elements in reverse order.
     * <p>
     * Built-in non-empty tuples return a new tuple instance with all elements in reversed order;
     * the empty tuple returns itself. The original tuple remains unchanged. For example, a tuple
     * (1.0f, 2.0f, 3.0f) becomes (3.0f, 2.0f, 1.0f) when reversed.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.FloatTuple3 t = FloatTuple.of(1.0f, 2.0f, 3.0f);
     * FloatTuple.FloatTuple3 r = t.reverse();
     * r._1                                            // returns 3.0f
     * r._3                                            // returns 1.0f
     *
     * // Original is unchanged
     * t._1                                            // still returns 1.0f
     *
     * // Edge: single-element tuple reversed is the same element
     * FloatTuple.of(5.0f).reverse()._1               // returns 5.0f
     *
     * // Edge: empty tuple reverses to itself
     * FloatTuple.copyOf(new float[0]).reverse().arity() // returns 0
     * }</pre>
     *
     * @return a tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified float value.
     * <p>
     * This method performs a linear search through all elements in the tuple to determine
     * if any element matches the specified value. Returns {@code true} if at least one
     * element equals the search value, {@code false} otherwise.
     * </p>
     * <p>
     * Comparisons follow {@link Float#compare(float, float)} semantics: {@code NaN} is
     * considered equal to {@code NaN}, and {@code +0.0f} and {@code -0.0f} are not considered equal.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.of(1.0f, 2.0f, 3.0f).contains(2.0f)  // returns true
     * FloatTuple.of(1.0f, 2.0f, 3.0f).contains(5.0f)  // returns false
     *
     * // Edge: NaN is found when NaN is present (Float.compare semantics: NaN == NaN)
     * FloatTuple.of(1.0f, Float.NaN).contains(Float.NaN)          // returns true
     *
     * // Edge: empty tuple never contains any value
     * FloatTuple.copyOf(new float[0]).contains(0.0f)               // returns false
     * }</pre>
     *
     * @param valueToFind the float value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     * @see Float#compare(float, float)
     */
    public abstract boolean contains(float valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * <p>
     * This method creates a defensive copy of the internal array. Changes to the
     * returned array do not affect the tuple because tuples are immutable.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * float[] arr = FloatTuple.of(1.0f, 2.0f, 3.0f).toArray();
     * arr.length                                      // returns 3
     * arr[0]                                          // returns 1.0f
     *
     * // Mutating the returned array does not affect the tuple
     * FloatTuple.FloatTuple2 t = FloatTuple.of(1.0f, 2.0f);
     * float[] copy = t.toArray();
     * copy[0] = 99.0f;
     * t._1                                            // still returns 1.0f
     *
     * // Edge: empty tuple returns a zero-length array (not null)
     * FloatTuple.copyOf(new float[0]).toArray().length // returns 0
     *
     * // Edge: single element
     * FloatTuple.of(Float.NaN).toArray()[0]           // returns NaN (Float.isNaN == true)
     * }</pre>
     *
     * @return a new float array containing all tuple elements
     * @see #toList()
     * @see #stream()
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
     * FloatList list = FloatTuple.of(1.0f, 2.0f, 3.0f).toList();
     * list.size()                                     // returns 3
     * list.get(0)                                     // returns 1.0f
     *
     * // Mutating the returned list does not affect the tuple
     * FloatTuple.FloatTuple2 t = FloatTuple.of(1.5f, 2.5f);
     * FloatList l = t.toList();
     * l.add(4.0f);                                    // modifies the copy, not the tuple
     * t.arity()                                       // still returns 2
     *
     * // Edge: empty tuple returns an empty list (not null)
     * FloatTuple.copyOf(new float[0]).toList().size() // returns 0
     *
     * // Edge: NaN element is preserved in the list
     * Float.isNaN(FloatTuple.of(Float.NaN).toList().get(0))        // returns true
     * }</pre>
     *
     * @return a new FloatList containing all tuple elements
     * @see #toArray()
     * @see #stream()
     */
    public FloatList toList() {
        return FloatList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple, in order from
     * {@code _1} to the highest-indexed field.
     * <p>
     * This method iterates through all elements in the tuple in order, applying the specified
     * consumer action to each element. The action is performed for its side effects only.
     * For an empty tuple this method returns immediately without invoking the consumer.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Collect elements into a list in encounter order
     * FloatList collected = FloatList.of();
     * FloatTuple.of(1.0f, 2.0f, 3.0f).forEach(collected::add); // adds each value to collected
     * collected.size()                                         // returns 3
     * collected.get(0)                                         // returns 1.0f
     *
     * // Action receives elements left-to-right
     * FloatTuple.of(1.5f, 2.5f).forEach(f -> System.out.print(f + " ")); // prints "1.5 2.5 "
     *
     * // Edge: empty tuple - action is never invoked
     * FloatList empty = FloatList.of();
     * FloatTuple.copyOf(new float[0]).forEach(empty::add); // action not invoked (empty tuple)
     * empty.size()                                         // returns 0
     *
     * // Edge: NaN element is passed to the action as-is
     * FloatList nanList = FloatList.of();
     * FloatTuple.of(Float.NaN).forEach(nanList::add); // adds the NaN element to nanList
     * Float.isNaN(nanList.get(0))                     // returns true
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the action to be performed for each element; must not be {@code null}
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> action) throws E {
        N.checkArgNotNull(action);

        for (final float element : elements()) {
            action.accept(element);
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
     * FloatTuple.of(1.0f, 2.0f, 3.0f).stream().sum()                   // returns 6.0
     * FloatTuple.of(1.5f, 2.5f).stream().filter(f -> f > 2.0f).count() // returns 1
     *
     * // Edge: empty tuple produces an empty stream
     * FloatTuple.copyOf(new float[0]).stream().count()             // returns 0
     *
     * // Edge: NaN element is streamed as-is
     * FloatTuple.of(Float.NaN).stream().count()                    // returns 1
     * }</pre>
     *
     * @return a FloatStream containing all tuple elements
     * @see #toArray()
     * @see #toList()
     */
    public FloatStream stream() {
        return FloatStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * <p>
     * The hash code is computed from the contents of the tuple's elements using
     * {@link Float#hashCode(float)} for each element (which is bit-pattern based,
     * so {@code +0.0f} and {@code -0.0f} hash differently and {@code NaN} hashes
     * to a single canonical value). Tuples with identical elements in the same
     * order have the same hash code. This implementation is consistent with
     * {@link #equals(Object)}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Equal tuples have equal hash codes
     * FloatTuple.of(1.0f, 2.0f).hashCode() == FloatTuple.of(1.0f, 2.0f).hashCode() // returns true
     *
     * // Same-content tuples of different arity have different hash codes (implementation detail,
     * // not contractually guaranteed, but practically true)
     * FloatTuple.of(1.0f).hashCode() == FloatTuple.of(1.0f, 0.0f).hashCode() // typically false
     *
     * // Edge: NaN hashes to a canonical non-zero value
     * FloatTuple.of(Float.NaN).hashCode() == FloatTuple.of(Float.NaN).hashCode() // returns true
     *
     * // Edge: empty tuple has a stable hash code
     * FloatTuple.copyOf(new float[0]).hashCode() == FloatTuple.copyOf(new float[0]).hashCode() // returns true
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
     * <p>
     * Two tuples are considered equal if and only if:
     * </p>
     * <ul>
     * <li>They are of the exact same runtime class (e.g., both {@code FloatTuple.FloatTuple2});
     *     a {@code FloatTuple1} is never equal to a {@code FloatTuple2}, regardless of element values</li>
     * <li>They contain the same elements in the same order</li>
     * </ul>
     * <p>
     * Element comparisons follow {@link Float#compare(float, float)} semantics:
     * {@code NaN} is treated as equal to {@code NaN}, and {@code +0.0f} is not equal to {@code -0.0f}.
     * This method adheres to the general contract of {@link Object#equals(Object)}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Same arity and same element values
     * FloatTuple.of(1.0f, 2.0f).equals(FloatTuple.of(1.0f, 2.0f))   // returns true
     *
     * // Different element order - not equal
     * FloatTuple.of(1.0f, 2.0f).equals(FloatTuple.of(2.0f, 1.0f))   // returns false
     *
     * // Edge: different arity - never equal even if elements overlap
     * FloatTuple.of(1.0f).equals(FloatTuple.of(1.0f, 0.0f))          // returns false
     *
     * // Edge: NaN is equal to NaN (Float.compare semantics)
     * FloatTuple.of(Float.NaN).equals(FloatTuple.of(Float.NaN))       // returns true
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
            return N.equals(elements(), ((FloatTuple<TP>) obj).elements());
        }
    }

    /**
     * Returns the internal array containing all float elements in this tuple.
     * <p>
     * <b>Warning:</b> The returned array is the internal representation of this tuple.
     * Modifying the returned array will compromise the immutability of this tuple.
     * Use {@link #toArray()} instead if you need an array that can be safely modified.
     * </p>
     *
     * @return the internal array of float elements
     */
    protected abstract float[] elements();

    /**
     * An empty FloatTuple containing no elements (arity 0).
     * <p>
     * This package-private class is exposed only through the base {@code FloatTuple} type
     * via the singleton instance returned by {@link #copyOf(float[])} when invoked with a
     * {@code null} or zero-length array. {@link #sum()} returns 0.0f, while {@link #min()},
     * {@link #max()}, {@link #median()}, and {@link #average()} all throw {@link java.util.NoSuchElementException}.
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
         * Returns the minimum value in this tuple.
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
         * Returns the maximum value in this tuple.
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
         * Returns the median value in this tuple.
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
         * Returns the sum of all elements in this tuple.
         * For an empty tuple, the sum is {@code 0.0f}.
         *
         * @return {@code 0.0f}
         */
        @Override
        public float sum() {
            return 0;
        }

        /**
         * Returns the average of all elements in this tuple.
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
         * @return this {@code FloatTuple0} instance
         */
        @Override
        public FloatTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified float value.
         * Since this tuple is empty, this method always returns {@code false}.
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

        /**
         * Returns the shared empty float array.
         *
         * @return an empty float array
         */
        @Override
        protected float[] elements() {
            return N.EMPTY_FLOAT_ARRAY;
        }
    }

    /**
     * A FloatTuple containing exactly one float value.
     * <p>
     * This class provides direct access to the single element through the public final field {@code _1}.
     * For single-element tuples, all statistical operations (min, max, median, sum, average) return
     * or are based on that single element.
     * </p>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.of(3.0f).arity()                     // returns 1
         * FloatTuple.of(Float.NaN).arity()                // returns 1
         *
         * // Edge: arity is always 1 regardless of value
         * FloatTuple.of(Float.POSITIVE_INFINITY).arity()  // returns 1
         *
         * // Edge: compare to empty tuple arity
         * FloatTuple.copyOf(new float[0]).arity()         // returns 0
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.of(3.0f).min()                       // returns 3.0f
         * FloatTuple.of(-1.5f).min()                      // returns -1.5f
         *
         * // Edge: NaN - single-element min is the element itself
         * Float.isNaN(FloatTuple.of(Float.NaN).min())     // returns true
         *
         * // Edge: infinity
         * FloatTuple.of(Float.NEGATIVE_INFINITY).min()    // returns Float.NEGATIVE_INFINITY
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.of(3.0f).max()                       // returns 3.0f
         * FloatTuple.of(-1.5f).max()                      // returns -1.5f
         *
         * // Edge: NaN - single-element max is the element itself
         * Float.isNaN(FloatTuple.of(Float.NaN).max())     // returns true
         *
         * // Edge: infinity
         * FloatTuple.of(Float.POSITIVE_INFINITY).max()    // returns Float.POSITIVE_INFINITY
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.of(3.0f).median()                    // returns 3.0f
         * FloatTuple.of(-1.5f).median()                   // returns -1.5f
         *
         * // Edge: NaN - single-element median is the element itself
         * Float.isNaN(FloatTuple.of(Float.NaN).median())  // returns true
         *
         * // Edge: infinity
         * FloatTuple.of(Float.POSITIVE_INFINITY).median() // returns Float.POSITIVE_INFINITY
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple1 t = FloatTuple.of(2.5f);
         * float s = t.sum();   // returns 2.5f
         *
         * FloatTuple.FloatTuple1 neg = FloatTuple.of(-3.0f);
         * float ns = neg.sum();   // returns -3.0f
         *
         * FloatTuple.FloatTuple1 inf = FloatTuple.of(Float.POSITIVE_INFINITY);
         * float is = inf.sum();   // returns Float.POSITIVE_INFINITY
         *
         * FloatTuple.FloatTuple1 nan = FloatTuple.of(Float.NaN);
         * float ns2 = nan.sum();   // returns NaN
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple1 t = FloatTuple.of(4.0f);
         * double avg = t.average();   // returns 4.0
         *
         * FloatTuple.FloatTuple1 neg = FloatTuple.of(-1.0f);
         * double navg = neg.average();   // returns -1.0
         *
         * FloatTuple.FloatTuple1 inf = FloatTuple.of(Float.POSITIVE_INFINITY);
         * double iavg = inf.average();   // returns Double.POSITIVE_INFINITY
         *
         * FloatTuple.FloatTuple1 nan = FloatTuple.of(Float.NaN);
         * double navg2 = nan.average();   // returns NaN (as double)
         * }</pre>
         *
         * @return the value of _1 as a double
         */
        @Override
        public double average() {
            return _1;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         * For a single-element tuple, the returned tuple has the same content,
         * but a fresh instance is allocated rather than returning {@code this}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple1 tuple = FloatTuple.of(3.14f);
         * FloatTuple.FloatTuple1 reversed = tuple.reverse();   // returns a new tuple (3.14f)
         * boolean sameInstance = (tuple == reversed);          // returns false
         *
         * FloatTuple.FloatTuple1 neg = FloatTuple.of(-7.5f);
         * FloatTuple.FloatTuple1 negRev = neg.reverse();   // returns a new tuple (-7.5f)
         *
         * FloatTuple.FloatTuple1 nanTuple = FloatTuple.of(Float.NaN);
         * FloatTuple.FloatTuple1 nanRev = nanTuple.reverse();   // returns a new tuple (NaN)
         * }</pre>
         *
         * @return a new {@code FloatTuple1} with the same value as this tuple
         */
        @Override
        public FloatTuple1 reverse() {
            return new FloatTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified float value.
         * Uses {@link Float#compare(float, float)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0f} does not match {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple1 t = FloatTuple.of(1.5f);
         * boolean found = t.contains(1.5f);     // returns true
         * boolean missing = t.contains(2.0f);   // returns false
         *
         * FloatTuple.FloatTuple1 nanTuple = FloatTuple.of(Float.NaN);
         * boolean nanFound = nanTuple.contains(Float.NaN);   // returns true (NaN matches NaN)
         *
         * FloatTuple.FloatTuple1 infTuple = FloatTuple.of(Float.POSITIVE_INFINITY);
         * boolean infFound = infTuple.contains(Float.POSITIVE_INFINITY);        // returns true
         * boolean negInfMissing = infTuple.contains(Float.NEGATIVE_INFINITY);   // returns false
         * }</pre>
         *
         * @param valueToFind the float value to search for
         * @return {@code true} if {@code _1} equals {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind);
        }

        /**
         * Returns a hash code for this tuple based on its single element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple1 t = FloatTuple.of(1.0f);
         * int h = t.hashCode();   // returns Float.hashCode(1.0f)
         *
         * FloatTuple.FloatTuple1 same = FloatTuple.of(1.0f);
         * boolean sameHash = (t.hashCode() == same.hashCode());   // returns true
         *
         * FloatTuple.FloatTuple1 neg = FloatTuple.of(-1.0f);
         * boolean diffHash = (t.hashCode() != neg.hashCode());   // returns true
         *
         * FloatTuple.FloatTuple1 nanTuple = FloatTuple.of(Float.NaN);
         * int nanHash = nanTuple.hashCode();   // returns Float.hashCode(Float.NaN)
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple1 t1 = FloatTuple.of(2.0f);
         * FloatTuple.FloatTuple1 t2 = FloatTuple.of(2.0f);
         * boolean equal = t1.equals(t2);   // returns true
         *
         * FloatTuple.FloatTuple1 t3 = FloatTuple.of(3.0f);
         * boolean notEqual = t1.equals(t3);   // returns false
         *
         * boolean notSameType = t1.equals(FloatTuple.of(2.0f, 0.0f));   // returns false
         *
         * FloatTuple.FloatTuple1 nanA = FloatTuple.of(Float.NaN);
         * FloatTuple.FloatTuple1 nanB = FloatTuple.of(Float.NaN);
         * boolean nanEqual = nanA.equals(nanB);   // returns true (NaN matches NaN via Float.compare)
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple.FloatTuple1 with equal value
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple1 t = FloatTuple.of(1.0f);
         * String s = t.toString();   // returns "(1.0)"
         *
         * FloatTuple.FloatTuple1 neg = FloatTuple.of(-3.5f);
         * String ns = neg.toString();   // returns "(-3.5)"
         *
         * FloatTuple.FloatTuple1 inf = FloatTuple.of(Float.POSITIVE_INFINITY);
         * String is = inf.toString();   // returns "(Infinity)"
         *
         * FloatTuple.FloatTuple1 nan = FloatTuple.of(Float.NaN);
         * String ns2 = nan.toString();   // returns "(NaN)"
         * }</pre>
         *
         * @return "(value)" where value is _1
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
        }

        /**
         * Returns the internal array of float elements.
         * The array is lazily initialized on first access.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple1 t = FloatTuple.of(5.0f);
         * float[] arr = t.toArray();   // returns [5.0f]
         *
         * FloatTuple.FloatTuple1 neg = FloatTuple.of(-2.5f);
         * float[] negArr = neg.toArray();   // returns [-2.5f]
         *
         * FloatTuple.FloatTuple1 inf = FloatTuple.of(Float.POSITIVE_INFINITY);
         * float[] infArr = inf.toArray();   // returns [Float.POSITIVE_INFINITY]
         *
         * FloatTuple.FloatTuple1 nan = FloatTuple.of(Float.NaN);
         * float[] nanArr = nan.toArray();   // returns array with NaN element
         * }</pre>
         *
         * @return a float array containing all elements of this tuple
         */
        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly two float values.
     * The values are accessible through the public final fields {@code _1} and {@code _2}.
     *
     * <p>In addition to the operations inherited from {@link FloatTuple}, this class provides
     * functional helpers for working with pairs:</p>
     * <ul>
     *   <li>{@link #accept(Throwables.FloatBiConsumer)} - consume both values</li>
     *   <li>{@link #map(Throwables.FloatBiFunction)} - transform the pair to a single value</li>
     *   <li>{@link #filter(Throwables.FloatBiPredicate)} - conditionally wrap in {@link Optional}</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * FloatTuple.FloatTuple2 t = FloatTuple.of(1.5f, 2.5f);
     * float first = t._1;        // 1.5f
     * float second = t._2;       // 2.5f
     * float sum = t.sum();       // 4.0f
     * float min = t.min();       // 1.5f
     * float max = t.max();       // 2.5f
     * double avg = t.average();  // 2.0
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t = FloatTuple.of(1.0f, 2.0f);
         * int n = t.arity();   // returns 2
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-1.0f, -2.0f);
         * int n2 = neg.arity();   // returns 2
         *
         * FloatTuple.FloatTuple2 nan = FloatTuple.of(Float.NaN, Float.NaN);
         * int n3 = nan.arity();   // returns 2
         *
         * FloatTuple.FloatTuple2 inf = FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
         * int n4 = inf.arity();   // returns 2
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
         * If either element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t = FloatTuple.of(3.0f, 1.5f);
         * float mn = t.min();   // returns 1.5f
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-5.0f, -1.0f);
         * float mnNeg = neg.min();   // returns -5.0f
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 0.0f);
         * float mnInf = infT.min();   // returns 0.0f
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, 1.0f);
         * float mnNan = nanT.min();   // returns NaN
         * }</pre>
         *
         * @return the smaller of {@code _1} and {@code _2}
         */
        @Override
        public float min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum value among the two elements.
         * If either element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t = FloatTuple.of(3.0f, 1.5f);
         * float mx = t.max();   // returns 3.0f
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-5.0f, -1.0f);
         * float mxNeg = neg.max();   // returns -1.0f
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 0.0f);
         * float mxInf = infT.max();   // returns Float.POSITIVE_INFINITY
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, 1.0f);
         * float mxNan = nanT.max();   // returns NaN
         * }</pre>
         *
         * @return the larger of {@code _1} and {@code _2}
         */
        @Override
        public float max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median of the two elements.
         * Because there is an even number of elements, this is the lower of the
         * two (i.e., {@code Math.min(_1, _2)}), not their average. If either
         * element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t = FloatTuple.of(3.0f, 1.0f);
         * float med = t.median();   // returns 1.0f (the lower of the two)
         *
         * FloatTuple.FloatTuple2 eq = FloatTuple.of(2.0f, 2.0f);
         * float medEq = eq.median();   // returns 2.0f (equal values)
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-4.0f, -1.0f);
         * float medNeg = neg.median();   // returns -4.0f (the lower value)
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, 1.0f);
         * float medNan = nanT.median();   // returns NaN
         * }</pre>
         *
         * @return the smaller of {@code _1} and {@code _2}, or {@code NaN} if either is {@code NaN}
         */
        @Override
        public float median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of the two elements.
         * If either element is {@code NaN} the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t = FloatTuple.of(1.5f, 2.5f);
         * float s = t.sum();   // returns 4.0f
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-3.0f, 1.0f);
         * float ns = neg.sum();   // returns -2.0f
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f);
         * float is = infT.sum();   // returns Float.POSITIVE_INFINITY
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, 1.0f);
         * float nanS = nanT.sum();   // returns NaN
         * }</pre>
         *
         * @return the sum of {@code _1} and {@code _2}
         */
        @Override
        public float sum() {
            return N.sum(_1, _2);
        }

        /**
         * Returns the arithmetic mean of the two elements as a {@code double}.
         * If either element is {@code NaN} the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t = FloatTuple.of(1.0f, 3.0f);
         * double avg = t.average();   // returns 2.0
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-3.0f, -1.0f);
         * double navg = neg.average();   // returns -2.0
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
         * double iavg = infT.average();   // returns Double.POSITIVE_INFINITY
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, 1.0f);
         * double navg2 = nanT.average();   // returns NaN (as double)
         * }</pre>
         *
         * @return the average of {@code _1} and {@code _2}
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
         * FloatTuple.FloatTuple2 tuple = FloatTuple.of(1.5f, 2.5f);
         * FloatTuple.FloatTuple2 reversed = tuple.reverse();   // returns (2.5, 1.5)
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-3.0f, 4.0f);
         * FloatTuple.FloatTuple2 negRev = neg.reverse();   // returns (4.0, -3.0)
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, 1.0f);
         * FloatTuple.FloatTuple2 nanRev = nanT.reverse();   // returns (1.0, NaN)
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(0.0f, Float.POSITIVE_INFINITY);
         * FloatTuple.FloatTuple2 infRev = infT.reverse();   // returns (Infinity, 0.0)
         * }</pre>
         *
         * @return a new FloatTuple.FloatTuple2 with (_2, _1)
         */
        @Override
        public FloatTuple2 reverse() {
            return new FloatTuple2(_2, _1);
        }

        /**
         * Checks if this tuple contains the specified float value.
         * Comparisons use {@link Float#compare(float, float)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0f} does not match {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t = FloatTuple.of(1.0f, 2.0f);
         * boolean found = t.contains(2.0f);     // returns true
         * boolean missing = t.contains(3.0f);   // returns false
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, 1.0f);
         * boolean nanFound = nanT.contains(Float.NaN);   // returns true
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 0.0f);
         * boolean infFound = infT.contains(Float.POSITIVE_INFINITY);        // returns true
         * boolean negInfMissing = infT.contains(Float.NEGATIVE_INFINITY);   // returns false
         * }</pre>
         *
         * @param valueToFind the float value to search for
         * @return {@code true} if {@code _1} or {@code _2} equals {@code valueToFind},
         *         {@code false} otherwise
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t = FloatTuple.of(1.0f, 2.0f);
         * List<Float> list = new ArrayList<>();
         * t.forEach(list::add);   // list becomes [1.0f, 2.0f]
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-3.0f, 5.0f);
         * float[] sum = {0f};
         * neg.forEach(v -> sum[0] += v);   // sum[0] becomes 2.0f
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, 1.0f);
         * List<Float> nanList = new ArrayList<>();
         * nanT.forEach(nanList::add);   // nanList contains [NaN, 1.0f]
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 2.0f);
         * List<Float> infList = new ArrayList<>();
         * infT.forEach(infList::add);   // infList contains [Infinity, 2.0f]
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> action) throws E {
            N.checkArgNotNull(action);

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
         * FloatTuple.FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
         * tuple.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * // Prints: 3.0 + 4.0 = 7.0
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-1.0f, -2.0f);
         * float[] result = {0f};
         * neg.accept((a, b) -> result[0] = a + b);   // result[0] becomes -3.0f
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, 1.0f);
         * float[] nanResult = {0f};
         * nanT.accept((a, b) -> nanResult[0] = a + b);   // nanResult[0] is NaN
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f);
         * float[] infResult = {0f};
         * infT.accept((a, b) -> infResult[0] = a + b);   // infResult[0] is Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the bi-consumer to perform on the two elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #map(Throwables.FloatBiFunction)
         * @see #filter(Throwables.FloatBiPredicate)
         */
        public <E extends Exception> void accept(final Throwables.FloatBiConsumer<E> action) throws E {
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
         * FloatTuple.FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
         * float product = tuple.map((a, b) -> a * b);   // returns 12.0f
         *
         * FloatTuple.FloatTuple2 point = FloatTuple.of(3.0f, 4.0f);
         * Double distance = point.map((x, y) -> Math.sqrt(x * x + y * y));   // returns 5.0
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, 1.0f);
         * float nanProd = nanT.map((a, b) -> a * b);   // returns NaN
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 2.0f);
         * float infProd = infT.map((a, b) -> a * b);   // returns Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the bi-function to apply to the two elements, must not be {@code null}
         * @return the result of applying the mapper to _1 and _2 (may be {@code null} if the mapper returns {@code null})
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.FloatBiConsumer)
         * @see #filter(Throwables.FloatBiPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.FloatBiFunction<U, E> mapper) throws E {
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
         * FloatTuple.FloatTuple2 tuple = FloatTuple.of(3.0f, 4.0f);
         * u.Optional<FloatTuple.FloatTuple2> result = tuple.filter((a, b) -> a + b > 5);
         * // result.isPresent() returns true (since 3.0f + 4.0f = 7.0f > 5)
         *
         * FloatTuple.FloatTuple2 small = FloatTuple.of(1.0f, 2.0f);
         * u.Optional<FloatTuple.FloatTuple2> empty = small.filter((a, b) -> a + b > 10);
         * // empty.isPresent() returns false (since 1.0f + 2.0f = 3.0f is not > 10)
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-1.0f, -2.0f);
         * u.Optional<FloatTuple.FloatTuple2> negResult = neg.filter((a, b) -> a < 0 && b < 0);
         * // negResult.isPresent() returns true (both are negative)
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f);
         * u.Optional<FloatTuple.FloatTuple2> infResult = infT.filter((a, b) -> Float.isInfinite(a));
         * // infResult.isPresent() returns true
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the bi-predicate to test the two elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception
         * @see #accept(Throwables.FloatBiConsumer)
         * @see #map(Throwables.FloatBiFunction)
         */
        public <E extends Exception> Optional<FloatTuple2> filter(final Throwables.FloatBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on both elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t1 = FloatTuple.of(1.0f, 2.0f);
         * FloatTuple.FloatTuple2 t2 = FloatTuple.of(1.0f, 2.0f);
         * boolean sameHash = (t1.hashCode() == t2.hashCode());   // returns true
         *
         * FloatTuple.FloatTuple2 t3 = FloatTuple.of(2.0f, 1.0f);
         * boolean diffHash = (t1.hashCode() != t3.hashCode());   // returns true (order matters)
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, Float.NaN);
         * int nanHash = nanT.hashCode();   // consistent hash for NaN values
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
         * int infHash = infT.hashCode();   // consistent hash for infinity values
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Float.floatToIntBits(_1);
            return 31 * result + Float.floatToIntBits(_2);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t1 = FloatTuple.of(1.0f, 2.0f);
         * FloatTuple.FloatTuple2 t2 = FloatTuple.of(1.0f, 2.0f);
         * boolean equal = t1.equals(t2);   // returns true
         *
         * FloatTuple.FloatTuple2 t3 = FloatTuple.of(2.0f, 1.0f);
         * boolean notEqual = t1.equals(t3);   // returns false (different order)
         *
         * boolean notSameType = t1.equals(FloatTuple.of(1.0f, 2.0f, 0.0f));   // returns false
         *
         * FloatTuple.FloatTuple2 nanA = FloatTuple.of(Float.NaN, 1.0f);
         * FloatTuple.FloatTuple2 nanB = FloatTuple.of(Float.NaN, 1.0f);
         * boolean nanEqual = nanA.equals(nanB);   // returns true (NaN matches NaN via Float.compare)
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple.FloatTuple2 with equal elements
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t = FloatTuple.of(1.0f, 2.0f);
         * String s = t.toString();   // returns "(1.0, 2.0)"
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-3.5f, 4.5f);
         * String ns = neg.toString();   // returns "(-3.5, 4.5)"
         *
         * FloatTuple.FloatTuple2 inf = FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
         * String is = inf.toString();   // returns "(Infinity, -Infinity)"
         *
         * FloatTuple.FloatTuple2 nan = FloatTuple.of(Float.NaN, 0.0f);
         * String ns2 = nan.toString();   // returns "(NaN, 0.0)"
         * }</pre>
         *
         * @return "(_1, _2)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
        }

        /**
         * Returns the internal array of float elements.
         * The array is lazily initialized on first access.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple2 t = FloatTuple.of(1.5f, 2.5f);
         * float[] arr = t.toArray();   // returns [1.5f, 2.5f]
         *
         * FloatTuple.FloatTuple2 neg = FloatTuple.of(-1.0f, -2.0f);
         * float[] negArr = neg.toArray();   // returns [-1.0f, -2.0f]
         *
         * FloatTuple.FloatTuple2 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 0.0f);
         * float[] infArr = infT.toArray();   // returns [Float.POSITIVE_INFINITY, 0.0f]
         *
         * FloatTuple.FloatTuple2 nanT = FloatTuple.of(Float.NaN, 1.0f);
         * float[] nanArr = nanT.toArray();   // returns array with [NaN, 1.0f]
         * }</pre>
         *
         * @return a float array containing all elements of this tuple
         */
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
     * <p>
     * This class provides direct access to elements through public final fields {@code _1}, {@code _2}, and {@code _3}.
     * FloatTuple.FloatTuple3 offers additional functional methods like {@link #accept(Throwables.FloatTriConsumer)},
     * {@link #map(Throwables.FloatTriFunction)}, and {@link #filter(Throwables.FloatTriPredicate)} that
     * operate on all three elements simultaneously.
     * </p>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * int n = t.arity();   // returns 3
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f);
         * int n2 = neg.arity();   // returns 3
         *
         * FloatTuple.FloatTuple3 nan = FloatTuple.of(Float.NaN, Float.NaN, Float.NaN);
         * int n3 = nan.arity();   // returns 3
         *
         * FloatTuple.FloatTuple3 mixed = FloatTuple.of(0.0f, Float.POSITIVE_INFINITY, Float.NaN);
         * int n4 = mixed.arity();   // returns 3
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
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t = FloatTuple.of(3.0f, 1.0f, 2.0f);
         * float mn = t.min();   // returns 1.0f
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-5.0f, -1.0f, -3.0f);
         * float mnNeg = neg.min();   // returns -5.0f
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 0.0f, 1.0f);
         * float mnInf = infT.min();   // returns 0.0f
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * float mnNan = nanT.min();   // returns NaN
         * }</pre>
         *
         * @return the smallest of {@code _1}, {@code _2}, and {@code _3}
         */
        @Override
        public float min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum value among the three elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t = FloatTuple.of(3.0f, 1.0f, 2.0f);
         * float mx = t.max();   // returns 3.0f
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-5.0f, -1.0f, -3.0f);
         * float mxNeg = neg.max();   // returns -1.0f
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 0.0f, 1.0f);
         * float mxInf = infT.max();   // returns Float.POSITIVE_INFINITY
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * float mxNan = nanT.max();   // returns NaN
         * }</pre>
         *
         * @return the largest of {@code _1}, {@code _2}, and {@code _3}
         */
        @Override
        public float max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median value of the three elements.
         * Comparison uses {@link Float#compare(float, float)} semantics, so
         * {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t = FloatTuple.of(3.0f, 1.0f, 2.0f);
         * float med = t.median();   // returns 2.0f (middle value when sorted)
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-5.0f, -1.0f, -3.0f);
         * float medNeg = neg.median();   // returns -3.0f
         *
         * FloatTuple.FloatTuple3 dup = FloatTuple.of(2.0f, 2.0f, 2.0f);
         * float medDup = dup.median();   // returns 2.0f
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * float medNan = nanT.median();   // NaN is treated as largest; median returns 2.0f
         * }</pre>
         *
         * @return the middle value when the three elements are sorted
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of the three elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * float s = t.sum();   // returns 6.0f
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f);
         * float ns = neg.sum();   // returns -6.0f
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f);
         * float is = infT.sum();   // returns Float.POSITIVE_INFINITY
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * float nanS = nanT.sum();   // returns NaN
         * }</pre>
         *
         * @return the sum of {@code _1}, {@code _2}, and {@code _3}
         */
        @Override
        public float sum() {
            return N.sum(_1, _2, _3);
        }

        /**
         * Returns the arithmetic mean of the three elements as a {@code double}.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * double avg = t.average();   // returns 2.0
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-3.0f, 0.0f, 3.0f);
         * double navg = neg.average();   // returns 0.0
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
         * double iavg = infT.average();   // returns Double.POSITIVE_INFINITY
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * double navg2 = nanT.average();   // returns NaN (as double)
         * }</pre>
         *
         * @return the average of {@code _1}, {@code _2}, and {@code _3}
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
         * FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * FloatTuple.FloatTuple3 reversed = tuple.reverse();   // returns (3.0, 2.0, 1.0)
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-1.0f, 0.0f, 1.0f);
         * FloatTuple.FloatTuple3 negRev = neg.reverse();   // returns (1.0, 0.0, -1.0)
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * FloatTuple.FloatTuple3 nanRev = nanT.reverse();   // returns (2.0, 1.0, NaN)
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(0.0f, Float.POSITIVE_INFINITY, 1.0f);
         * FloatTuple.FloatTuple3 infRev = infT.reverse();   // returns (1.0, Infinity, 0.0)
         * }</pre>
         *
         * @return a new FloatTuple.FloatTuple3 with (_3, _2, _1)
         */
        @Override
        public FloatTuple3 reverse() {
            return new FloatTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified float value.
         * Comparisons use {@link Float#compare(float, float)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0f} does not match {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * boolean found = t.contains(2.0f);     // returns true
         * boolean missing = t.contains(4.0f);   // returns false
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * boolean nanFound = nanT.contains(Float.NaN);   // returns true
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 0.0f, 1.0f);
         * boolean infFound = infT.contains(Float.POSITIVE_INFINITY);        // returns true
         * boolean negInfMissing = infT.contains(Float.NEGATIVE_INFINITY);   // returns false
         * }</pre>
         *
         * @param valueToFind the float value to search for
         * @return {@code true} if any of {@code _1}, {@code _2}, {@code _3} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * List<Float> list = new ArrayList<>();
         * t.forEach(list::add);   // list becomes [1.0f, 2.0f, 3.0f]
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-1.0f, 0.0f, 1.0f);
         * float[] sum = {0f};
         * neg.forEach(v -> sum[0] += v);   // sum[0] becomes 0.0f
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * List<Float> nanList = new ArrayList<>();
         * nanT.forEach(nanList::add);   // nanList contains [NaN, 1.0f, 2.0f]
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 2.0f, 3.0f);
         * List<Float> infList = new ArrayList<>();
         * infT.forEach(infList::add);   // infList contains [Infinity, 2.0f, 3.0f]
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> action) throws E {
            N.checkArgNotNull(action);

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
         * FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * tuple.accept((a, b, c) -> System.out.println("Sum: " + (a + b + c)));  // Prints: Sum: 6.0
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f);
         * float[] result = {0f};
         * neg.accept((a, b, c) -> result[0] = a + b + c);   // result[0] becomes -6.0f
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * float[] nanResult = {0f};
         * nanT.accept((a, b, c) -> nanResult[0] = a + b + c);   // nanResult[0] is NaN
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f);
         * float[] infResult = {0f};
         * infT.accept((a, b, c) -> infResult[0] = a + b + c);   // infResult[0] is Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the tri-consumer to perform on the three elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #map(Throwables.FloatTriFunction)
         * @see #filter(Throwables.FloatTriPredicate)
         */
        public <E extends Exception> void accept(final Throwables.FloatTriConsumer<E> action) throws E {
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
         * FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * float product = tuple.map((a, b, c) -> a * b * c);   // returns 6.0f
         *
         * FloatTuple.FloatTuple3 point = FloatTuple.of(1.0f, 2.0f, 2.0f);
         * Double distance = point.map((x, y, z) -> Math.sqrt(x*x + y*y + z*z));   // returns 3.0
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * float nanProd = nanT.map((a, b, c) -> a * b * c);   // returns NaN
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f);
         * float infProd = infT.map((a, b, c) -> a * b * c);   // returns Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the tri-function to apply to the three elements, must not be {@code null}
         * @return the result of applying the mapper to _1, _2, and _3 (may be {@code null} if the mapper returns {@code null})
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.FloatTriConsumer)
         * @see #filter(Throwables.FloatTriPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.FloatTriFunction<U, E> mapper) throws E {
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
         * FloatTuple.FloatTuple3 tuple = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * u.Optional<FloatTuple.FloatTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
         * // result.isPresent() returns true (since 1.0f + 2.0f + 3.0f = 6.0f > 5)
         *
         * FloatTuple.FloatTuple3 small = FloatTuple.of(1.0f, 1.0f, 1.0f);
         * u.Optional<FloatTuple.FloatTuple3> empty = small.filter((a, b, c) -> a + b + c > 10);
         * // empty.isPresent() returns false (since 1.0f + 1.0f + 1.0f = 3.0f is not > 10)
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f);
         * u.Optional<FloatTuple.FloatTuple3> negResult = neg.filter((a, b, c) -> a < 0 && b < 0 && c < 0);
         * // negResult.isPresent() returns true (all are negative)
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f);
         * u.Optional<FloatTuple.FloatTuple3> infResult = infT.filter((a, b, c) -> Float.isInfinite(a));
         * // infResult.isPresent() returns true
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the tri-predicate to test the three elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception
         * @see #accept(Throwables.FloatTriConsumer)
         * @see #map(Throwables.FloatTriFunction)
         */
        public <E extends Exception> Optional<FloatTuple3> filter(final Throwables.FloatTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on all three elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * FloatTuple.FloatTuple3 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * boolean sameHash = (t1.hashCode() == t2.hashCode());   // returns true
         *
         * FloatTuple.FloatTuple3 t3 = FloatTuple.of(3.0f, 2.0f, 1.0f);
         * boolean diffHash = (t1.hashCode() != t3.hashCode());   // returns true (order matters)
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, Float.NaN, Float.NaN);
         * int nanHash = nanT.hashCode();   // consistent hash for NaN values
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 0.0f, Float.NEGATIVE_INFINITY);
         * int infHash = infT.hashCode();   // consistent hash for infinity values
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Float.floatToIntBits(_1);
            result = 31 * result + Float.floatToIntBits(_2);
            return 31 * result + Float.floatToIntBits(_3);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * FloatTuple.FloatTuple3 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * boolean equal = t1.equals(t2);   // returns true
         *
         * FloatTuple.FloatTuple3 t3 = FloatTuple.of(3.0f, 2.0f, 1.0f);
         * boolean notEqual = t1.equals(t3);   // returns false (different order)
         *
         * boolean notSameType = t1.equals(FloatTuple.of(1.0f, 2.0f));   // returns false
         *
         * FloatTuple.FloatTuple3 nanA = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * FloatTuple.FloatTuple3 nanB = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * boolean nanEqual = nanA.equals(nanB);   // returns true (NaN matches NaN via Float.compare)
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple.FloatTuple3 with equal elements
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * String s = t.toString();   // returns "(1.0, 2.0, 3.0)"
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f);
         * String ns = neg.toString();   // returns "(-1.0, -2.0, -3.0)"
         *
         * FloatTuple.FloatTuple3 inf = FloatTuple.of(Float.POSITIVE_INFINITY, 0.0f, Float.NEGATIVE_INFINITY);
         * String is = inf.toString();   // returns "(Infinity, 0.0, -Infinity)"
         *
         * FloatTuple.FloatTuple3 nan = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * String ns2 = nan.toString();   // returns "(NaN, 1.0, 2.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
        }

        /**
         * Returns the internal array of float elements.
         * The array is lazily initialized on first access.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple3 t = FloatTuple.of(1.0f, 2.0f, 3.0f);
         * float[] arr = t.toArray();   // returns [1.0f, 2.0f, 3.0f]
         *
         * FloatTuple.FloatTuple3 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f);
         * float[] negArr = neg.toArray();   // returns [-1.0f, -2.0f, -3.0f]
         *
         * FloatTuple.FloatTuple3 infT = FloatTuple.of(Float.POSITIVE_INFINITY, 0.0f, 1.0f);
         * float[] infArr = infT.toArray();   // returns [Float.POSITIVE_INFINITY, 0.0f, 1.0f]
         *
         * FloatTuple.FloatTuple3 nanT = FloatTuple.of(Float.NaN, 1.0f, 2.0f);
         * float[] nanArr = nanT.toArray();   // returns array with [NaN, 1.0f, 2.0f]
         * }</pre>
         *
         * @return a float array containing all elements of this tuple
         */
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
     * <p>
     * Provides direct access to elements via public final fields {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
     * This arity does not expose the bi/tri-arg functional helpers that
     * {@link FloatTuple2} and {@link FloatTuple3} provide.
     * </p>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple4 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * int n = t.arity();   // returns 4
         *
         * FloatTuple.FloatTuple4 neg = FloatTuple.of(-1.5f, -2.5f, -3.5f, -4.5f);
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
         * Returns the minimum value among the four elements.
         * Uses {@link Math#min(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0f} is
         * treated as less than {@code +0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple4 t = FloatTuple.of(3.0f, 1.0f, 4.0f, 2.0f);
         * float m = t.min();   // returns 1.0f
         *
         * FloatTuple.FloatTuple4 dups = FloatTuple.of(5.0f, 5.0f, 5.0f, 5.0f);
         * float m2 = dups.min();   // returns 5.0f
         *
         * FloatTuple.FloatTuple4 withNeg = FloatTuple.of(-3.0f, 1.0f, 2.0f, -1.0f);
         * float m3 = withNeg.min();   // returns -3.0f
         *
         * FloatTuple.FloatTuple4 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f);
         * float m4 = withNaN.min();   // returns NaN
         * }</pre>
         *
         * @return the smallest of {@code _1}, {@code _2}, {@code _3}, and {@code _4}
         */
        @Override
        public float min() {
            return Math.min(Math.min(_1, _2), Math.min(_3, _4));
        }

        /**
         * Returns the maximum value among the four elements.
         * Uses {@link Math#max(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0f} is
         * treated as greater than {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple4 t = FloatTuple.of(3.0f, 1.0f, 4.0f, 2.0f);
         * float m = t.max();   // returns 4.0f
         *
         * FloatTuple.FloatTuple4 dups = FloatTuple.of(5.0f, 5.0f, 5.0f, 5.0f);
         * float m2 = dups.max();   // returns 5.0f
         *
         * FloatTuple.FloatTuple4 withNeg = FloatTuple.of(-3.0f, -1.0f, -2.0f, -4.0f);
         * float m3 = withNeg.max();   // returns -1.0f
         *
         * FloatTuple.FloatTuple4 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f);
         * float m4 = withNaN.max();   // returns NaN
         * }</pre>
         *
         * @return the largest of {@code _1}, {@code _2}, {@code _3}, and {@code _4}
         */
        @Override
        public float max() {
            return Math.max(Math.max(_1, _2), Math.max(_3, _4));
        }

        /**
         * Returns the median value of the four elements.
         * For an even number of elements, returns the lower of the two middle values
         * (not their average). Ordering uses {@link Float#compare(float, float)}
         * semantics, so {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple4 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * float med = t.median();   // returns 2.0f  (lower of the two middle values)
         *
         * FloatTuple.FloatTuple4 t2 = FloatTuple.of(4.0f, 1.0f, 3.0f, 2.0f);
         * float med2 = t2.median();   // returns 2.0f
         *
         * FloatTuple.FloatTuple4 neg = FloatTuple.of(-4.0f, -3.0f, -2.0f, -1.0f);
         * float med3 = neg.median();   // returns -3.0f
         *
         * FloatTuple.FloatTuple4 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f);
         * float med4 = withNaN.median();   // returns 2.0f  (NaN sorts last)
         * }</pre>
         *
         * @return the lower middle value when the four elements are sorted
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of the four elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple4 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * float s = t.sum();   // returns 10.0f
         *
         * FloatTuple.FloatTuple4 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f);
         * float s2 = neg.sum();   // returns -10.0f
         *
         * FloatTuple.FloatTuple4 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f);
         * float s3 = withNaN.sum();   // returns NaN
         *
         * FloatTuple.FloatTuple4 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f);
         * float s4 = withInf.sum();   // returns Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1}, {@code _2}, {@code _3}, and {@code _4}
         */
        @Override
        public float sum() {
            return N.sum(_1, _2, _3, _4);
        }

        /**
         * Returns the arithmetic mean of the four elements as a {@code double}.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple4 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * double avg = t.average();   // returns 2.5
         *
         * FloatTuple.FloatTuple4 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f);
         * double avg2 = neg.average();   // returns -2.5
         *
         * FloatTuple.FloatTuple4 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f);
         * double avg3 = withNaN.average();   // returns NaN
         *
         * FloatTuple.FloatTuple4 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f);
         * double avg4 = withInf.average();   // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the average of {@code _1}, {@code _2}, {@code _3}, and {@code _4}
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
         * FloatTuple.FloatTuple4 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * FloatTuple.FloatTuple4 r = t.reverse();
         * // r._1 == 4.0f, r._2 == 3.0f, r._3 == 2.0f, r._4 == 1.0f
         *
         * FloatTuple.FloatTuple4 dups = FloatTuple.of(1.5f, 1.5f, 2.5f, 2.5f);
         * FloatTuple.FloatTuple4 r2 = dups.reverse();
         * // r2._1 == 2.5f, r2._2 == 2.5f, r2._3 == 1.5f, r2._4 == 1.5f
         *
         * FloatTuple.FloatTuple4 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f);
         * FloatTuple.FloatTuple4 r3 = neg.reverse();
         * // r3._1 == -4.0f, r3._2 == -3.0f, r3._3 == -2.0f, r3._4 == -1.0f
         *
         * FloatTuple.FloatTuple4 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f);
         * FloatTuple.FloatTuple4 r4 = withNaN.reverse();
         * // r4._1 == 3.0f, r4._4 is NaN
         * }</pre>
         *
         * @return a new FloatTuple.FloatTuple4 with (_4, _3, _2, _1)
         */
        @Override
        public FloatTuple4 reverse() {
            return new FloatTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified float value.
         * Comparisons use {@link Float#compare(float, float)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0f} does not match {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple4 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * boolean b1 = t.contains(2.0f);   // returns true
         * boolean b2 = t.contains(5.0f);   // returns false
         *
         * FloatTuple.FloatTuple4 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f);
         * boolean b3 = withNaN.contains(Float.NaN);   // returns true
         *
         * FloatTuple.FloatTuple4 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f);
         * boolean b4 = withInf.contains(Float.POSITIVE_INFINITY);   // returns true
         * boolean b5 = withInf.contains(Float.NEGATIVE_INFINITY);   // returns false
         * }</pre>
         *
         * @param valueToFind the float value to search for
         * @return {@code true} if any of {@code _1} through {@code _4} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple4 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * List<Float> list = new java.util.ArrayList<>();
         * t.forEach(e -> list.add(e));   // list becomes [1.0, 2.0, 3.0, 4.0]
         *
         * float[] sum = {0.0f};
         * t.forEach(e -> sum[0] += e);   // sum[0] == 10.0f
         *
         * FloatTuple.FloatTuple4 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f);
         * List<Float> neg2 = new java.util.ArrayList<>();
         * neg.forEach(e -> neg2.add(e));   // neg2 becomes [-1.0, -2.0, -3.0, -4.0]
         *
         * // forEach(null) throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
            action.accept(_4);
        }

        /**
         * Returns a hash code for this tuple based on all four elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple4 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * FloatTuple.FloatTuple4 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * boolean same = (t1.hashCode() == t2.hashCode());   // returns true
         *
         * FloatTuple.FloatTuple4 t3 = FloatTuple.of(4.0f, 3.0f, 2.0f, 1.0f);
         * boolean diff = (t1.hashCode() == t3.hashCode());   // returns false (different order)
         *
         * FloatTuple.FloatTuple4 withNaN = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f);
         * int h = withNaN.hashCode();   // well-defined (NaN values use the canonical NaN hash)
         *
         * FloatTuple.FloatTuple4 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f);
         * int h2 = neg.hashCode();   // well-defined, differs from positive counterpart
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Float.floatToIntBits(_1);
            result = 31 * result + Float.floatToIntBits(_2);
            result = 31 * result + Float.floatToIntBits(_3);
            return 31 * result + Float.floatToIntBits(_4);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple4 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * FloatTuple.FloatTuple4 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * boolean eq = t1.equals(t2);   // returns true
         *
         * FloatTuple.FloatTuple4 t3 = FloatTuple.of(4.0f, 3.0f, 2.0f, 1.0f);
         * boolean ne = t1.equals(t3);   // returns false
         *
         * FloatTuple.FloatTuple4 withNaN = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f);
         * boolean nanEq = withNaN.equals(FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f));   // returns true
         *
         * boolean notEq = t1.equals("not a tuple");   // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple.FloatTuple4 with equal elements
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple4 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f);
         * String s = t.toString();   // returns "(1.0, 2.0, 3.0, 4.0)"
         *
         * FloatTuple.FloatTuple4 neg = FloatTuple.of(-1.5f, -2.5f, -3.5f, -4.5f);
         * String s2 = neg.toString();   // returns "(-1.5, -2.5, -3.5, -4.5)"
         *
         * FloatTuple.FloatTuple4 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f);
         * String s3 = withNaN.toString();   // returns "(NaN, 1.0, 2.0, 3.0)"
         *
         * FloatTuple.FloatTuple4 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, 0.0f, 0.0f);
         * String s4 = withInf.toString();   // returns "(Infinity, -Infinity, 0.0, 0.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
        }

        /**
         * Returns the internal array of float elements.
         * The array is lazily initialized on first access.
         *
         * @return a float array containing all elements of this tuple
         */
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
     * <p>
     * Provides direct access to elements via public final fields {@code _1} through {@code _5}.
     * </p>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple5 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * int n = t.arity();   // returns 5
         *
         * FloatTuple.FloatTuple5 neg = FloatTuple.of(-1.5f, -2.5f, -3.5f, -4.5f, -5.5f);
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
         * Returns the minimum value among the five elements.
         * Uses {@link Math#min(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0f} is
         * treated as less than {@code +0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple5 t = FloatTuple.of(3.0f, 1.0f, 5.0f, 2.0f, 4.0f);
         * float m = t.min();   // returns 1.0f
         *
         * FloatTuple.FloatTuple5 dups = FloatTuple.of(7.0f, 7.0f, 7.0f, 7.0f, 7.0f);
         * float m2 = dups.min();   // returns 7.0f
         *
         * FloatTuple.FloatTuple5 withNeg = FloatTuple.of(-5.0f, 1.0f, 2.0f, 3.0f, 4.0f);
         * float m3 = withNeg.min();   // returns -5.0f
         *
         * FloatTuple.FloatTuple5 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f);
         * float m4 = withNaN.min();   // returns NaN
         * }</pre>
         *
         * @return the smallest of {@code _1} through {@code _5}
         */
        @Override
        public float min() {
            return Math.min(Math.min(Math.min(_1, _2), Math.min(_3, _4)), _5);
        }

        /**
         * Returns the maximum value among the five elements.
         * Uses {@link Math#max(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0f} is
         * treated as greater than {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple5 t = FloatTuple.of(3.0f, 1.0f, 5.0f, 2.0f, 4.0f);
         * float m = t.max();   // returns 5.0f
         *
         * FloatTuple.FloatTuple5 dups = FloatTuple.of(7.0f, 7.0f, 7.0f, 7.0f, 7.0f);
         * float m2 = dups.max();   // returns 7.0f
         *
         * FloatTuple.FloatTuple5 withNeg = FloatTuple.of(-5.0f, -1.0f, -2.0f, -3.0f, -4.0f);
         * float m3 = withNeg.max();   // returns -1.0f
         *
         * FloatTuple.FloatTuple5 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f);
         * float m4 = withNaN.max();   // returns NaN
         * }</pre>
         *
         * @return the largest of {@code _1} through {@code _5}
         */
        @Override
        public float max() {
            return Math.max(Math.max(Math.max(_1, _2), Math.max(_3, _4)), _5);
        }

        /**
         * Returns the median value of the five elements.
         * For an odd number of elements, this is the exact middle value when sorted.
         * Ordering uses {@link Float#compare(float, float)} semantics, so
         * {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple5 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * float med = t.median();   // returns 3.0f  (exact middle)
         *
         * FloatTuple.FloatTuple5 t2 = FloatTuple.of(5.0f, 1.0f, 3.0f, 2.0f, 4.0f);
         * float med2 = t2.median();   // returns 3.0f
         *
         * FloatTuple.FloatTuple5 neg = FloatTuple.of(-5.0f, -4.0f, -3.0f, -2.0f, -1.0f);
         * float med3 = neg.median();   // returns -3.0f
         *
         * FloatTuple.FloatTuple5 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f);
         * float med4 = withNaN.median();   // returns 3.0f  (NaN sorts last)
         * }</pre>
         *
         * @return the middle value when the five elements are sorted
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of the five elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple5 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * float s = t.sum();   // returns 15.0f
         *
         * FloatTuple.FloatTuple5 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f);
         * float s2 = neg.sum();   // returns -15.0f
         *
         * FloatTuple.FloatTuple5 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f);
         * float s3 = withNaN.sum();   // returns NaN
         *
         * FloatTuple.FloatTuple5 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f);
         * float s4 = withInf.sum();   // returns Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1} through {@code _5}
         */
        @Override
        public float sum() {
            return N.sum(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the arithmetic mean of the five elements as a {@code double}.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple5 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * double avg = t.average();   // returns 3.0
         *
         * FloatTuple.FloatTuple5 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f);
         * double avg2 = neg.average();   // returns -3.0
         *
         * FloatTuple.FloatTuple5 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f);
         * double avg3 = withNaN.average();   // returns NaN
         *
         * FloatTuple.FloatTuple5 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f);
         * double avg4 = withInf.average();   // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the average of {@code _1} through {@code _5}
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
         * FloatTuple.FloatTuple5 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * FloatTuple.FloatTuple5 r = t.reverse();
         * // r._1 == 5.0f, r._2 == 4.0f, r._3 == 3.0f, r._4 == 2.0f, r._5 == 1.0f
         *
         * FloatTuple.FloatTuple5 dups = FloatTuple.of(1.5f, 1.5f, 2.5f, 2.5f, 3.5f);
         * FloatTuple.FloatTuple5 r2 = dups.reverse();
         * // r2._1 == 3.5f, r2._5 == 1.5f
         *
         * FloatTuple.FloatTuple5 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f);
         * FloatTuple.FloatTuple5 r3 = neg.reverse();
         * // r3._1 == -5.0f, r3._5 == -1.0f
         *
         * FloatTuple.FloatTuple5 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f);
         * FloatTuple.FloatTuple5 r4 = withNaN.reverse();
         * // r4._1 == 4.0f, r4._5 is NaN
         * }</pre>
         *
         * @return a new FloatTuple.FloatTuple5 with (_5, _4, _3, _2, _1)
         */
        @Override
        public FloatTuple5 reverse() {
            return new FloatTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified float value.
         * Comparisons use {@link Float#compare(float, float)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0f} does not match {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple5 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * boolean b1 = t.contains(3.0f);   // returns true
         * boolean b2 = t.contains(6.0f);   // returns false
         *
         * FloatTuple.FloatTuple5 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f);
         * boolean b3 = withNaN.contains(Float.NaN);   // returns true
         *
         * FloatTuple.FloatTuple5 withInf = FloatTuple.of(Float.NEGATIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f);
         * boolean b4 = withInf.contains(Float.NEGATIVE_INFINITY);   // returns true
         * boolean b5 = withInf.contains(Float.POSITIVE_INFINITY);   // returns false
         * }</pre>
         *
         * @param valueToFind the float value to search for
         * @return {@code true} if any of {@code _1} through {@code _5} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind)
                    || N.equals(_5, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple5 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * List<Float> list = new java.util.ArrayList<>();
         * t.forEach(e -> list.add(e));   // list becomes [1.0, 2.0, 3.0, 4.0, 5.0]
         *
         * float[] sum = {0.0f};
         * t.forEach(e -> sum[0] += e);   // sum[0] == 15.0f
         *
         * FloatTuple.FloatTuple5 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f);
         * List<Float> neg2 = new java.util.ArrayList<>();
         * neg.forEach(e -> neg2.add(e));   // neg2 becomes [-1.0, -2.0, -3.0, -4.0, -5.0]
         *
         * // forEach(null) throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
            action.accept(_4);
            action.accept(_5);
        }

        /**
         * Returns a hash code for this tuple based on all five elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple5 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * FloatTuple.FloatTuple5 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * boolean same = (t1.hashCode() == t2.hashCode());   // returns true
         *
         * FloatTuple.FloatTuple5 t3 = FloatTuple.of(5.0f, 4.0f, 3.0f, 2.0f, 1.0f);
         * boolean diff = (t1.hashCode() == t3.hashCode());   // returns false
         *
         * FloatTuple.FloatTuple5 withNaN = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f);
         * int h = withNaN.hashCode();   // well-defined (NaN values use the canonical NaN hash)
         *
         * FloatTuple.FloatTuple5 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f);
         * int h2 = neg.hashCode();   // well-defined, differs from positive counterpart
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Float.floatToIntBits(_1);
            result = 31 * result + Float.floatToIntBits(_2);
            result = 31 * result + Float.floatToIntBits(_3);
            result = 31 * result + Float.floatToIntBits(_4);
            return 31 * result + Float.floatToIntBits(_5);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple5 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * FloatTuple.FloatTuple5 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * boolean eq = t1.equals(t2);   // returns true
         *
         * FloatTuple.FloatTuple5 t3 = FloatTuple.of(5.0f, 4.0f, 3.0f, 2.0f, 1.0f);
         * boolean ne = t1.equals(t3);   // returns false
         *
         * FloatTuple.FloatTuple5 withNaN = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f);
         * boolean nanEq = withNaN.equals(FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f));   // returns true
         *
         * boolean notEq = t1.equals("not a tuple");   // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple.FloatTuple5 with equal elements
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple5 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * String s = t.toString();   // returns "(1.0, 2.0, 3.0, 4.0, 5.0)"
         *
         * FloatTuple.FloatTuple5 neg = FloatTuple.of(-1.5f, -2.5f, -3.5f, -4.5f, -5.5f);
         * String s2 = neg.toString();   // returns "(-1.5, -2.5, -3.5, -4.5, -5.5)"
         *
         * FloatTuple.FloatTuple5 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f);
         * String s3 = withNaN.toString();   // returns "(NaN, 1.0, 2.0, 3.0, 4.0)"
         *
         * FloatTuple.FloatTuple5 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, 0.0f, 0.0f, 1.0f);
         * String s4 = withInf.toString();   // returns "(Infinity, -Infinity, 0.0, 0.0, 1.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4, _5)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ")";
        }

        /**
         * Returns the internal array of float elements.
         * The array is lazily initialized on first access.
         *
         * @return a float array containing all elements of this tuple
         */
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
     * <p>
     * Provides direct access to elements via public final fields {@code _1} through {@code _6}.
     * </p>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple6 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * int n = t.arity();   // returns 6
         *
         * FloatTuple.FloatTuple6 neg = FloatTuple.of(-1.5f, -2.5f, -3.5f, -4.5f, -5.5f, -6.5f);
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
         * Returns the minimum value among the six elements.
         * Uses {@link Math#min(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0f} is
         * treated as less than {@code +0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple6 t = FloatTuple.of(3.0f, 1.0f, 6.0f, 2.0f, 5.0f, 4.0f);
         * float m = t.min();   // returns 1.0f
         *
         * FloatTuple.FloatTuple6 dups = FloatTuple.of(9.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f);
         * float m2 = dups.min();   // returns 9.0f
         *
         * FloatTuple.FloatTuple6 withNeg = FloatTuple.of(-6.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * float m3 = withNeg.min();   // returns -6.0f
         *
         * FloatTuple.FloatTuple6 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * float m4 = withNaN.min();   // returns NaN
         * }</pre>
         *
         * @return the smallest of {@code _1} through {@code _6}
         */
        @Override
        public float min() {
            return Math.min(Math.min(Math.min(_1, _2), Math.min(_3, _4)), Math.min(_5, _6));
        }

        /**
         * Returns the maximum value among the six elements.
         * Uses {@link Math#max(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0f} is
         * treated as greater than {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple6 t = FloatTuple.of(3.0f, 1.0f, 6.0f, 2.0f, 5.0f, 4.0f);
         * float m = t.max();   // returns 6.0f
         *
         * FloatTuple.FloatTuple6 dups = FloatTuple.of(9.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f);
         * float m2 = dups.max();   // returns 9.0f
         *
         * FloatTuple.FloatTuple6 withNeg = FloatTuple.of(-6.0f, -5.0f, -4.0f, -3.0f, -2.0f, -1.0f);
         * float m3 = withNeg.max();   // returns -1.0f
         *
         * FloatTuple.FloatTuple6 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * float m4 = withNaN.max();   // returns NaN
         * }</pre>
         *
         * @return the largest of {@code _1} through {@code _6}
         */
        @Override
        public float max() {
            return Math.max(Math.max(Math.max(_1, _2), Math.max(_3, _4)), Math.max(_5, _6));
        }

        /**
         * Returns the median value of the six elements.
         * For an even number of elements, returns the lower of the two middle values
         * (not their average). Ordering uses {@link Float#compare(float, float)}
         * semantics, so {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple6 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * float med = t.median();   // returns 3.0f  (lower of the two middle values 3 and 4)
         *
         * FloatTuple.FloatTuple6 t2 = FloatTuple.of(6.0f, 1.0f, 4.0f, 2.0f, 5.0f, 3.0f);
         * float med2 = t2.median();   // returns 3.0f
         *
         * FloatTuple.FloatTuple6 neg = FloatTuple.of(-6.0f, -5.0f, -4.0f, -3.0f, -2.0f, -1.0f);
         * float med3 = neg.median();   // returns -4.0f
         *
         * FloatTuple.FloatTuple6 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * float med4 = withNaN.median();   // returns 3.0f  (NaN sorts last)
         * }</pre>
         *
         * @return the lower middle value when the six elements are sorted
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of the six elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple6 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * float s = t.sum();   // returns 21.0f
         *
         * FloatTuple.FloatTuple6 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f);
         * float s2 = neg.sum();   // returns -21.0f
         *
         * FloatTuple.FloatTuple6 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * float s3 = withNaN.sum();   // returns NaN
         *
         * FloatTuple.FloatTuple6 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * float s4 = withInf.sum();   // returns Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1} through {@code _6}
         */
        @Override
        public float sum() {
            return N.sum(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the arithmetic mean of the six elements as a {@code double}.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple6 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * double avg = t.average();   // returns 3.5
         *
         * FloatTuple.FloatTuple6 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f);
         * double avg2 = neg.average();   // returns -3.5
         *
         * FloatTuple.FloatTuple6 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * double avg3 = withNaN.average();   // returns NaN
         *
         * FloatTuple.FloatTuple6 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * double avg4 = withInf.average();   // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the average of {@code _1} through {@code _6}
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
         * FloatTuple.FloatTuple6 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * FloatTuple.FloatTuple6 r = t.reverse();
         * // r._1 == 6.0f, r._2 == 5.0f, r._3 == 4.0f, r._4 == 3.0f, r._5 == 2.0f, r._6 == 1.0f
         *
         * FloatTuple.FloatTuple6 dups = FloatTuple.of(1.5f, 1.5f, 2.5f, 2.5f, 3.5f, 3.5f);
         * FloatTuple.FloatTuple6 r2 = dups.reverse();
         * // r2._1 == 3.5f, r2._6 == 1.5f
         *
         * FloatTuple.FloatTuple6 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f);
         * FloatTuple.FloatTuple6 r3 = neg.reverse();
         * // r3._1 == -6.0f, r3._6 == -1.0f
         *
         * FloatTuple.FloatTuple6 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * FloatTuple.FloatTuple6 r4 = withNaN.reverse();
         * // r4._1 == 5.0f, r4._6 is NaN
         * }</pre>
         *
         * @return a new FloatTuple.FloatTuple6 with (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public FloatTuple6 reverse() {
            return new FloatTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified float value.
         * Comparisons use {@link Float#compare(float, float)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0f} does not match {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple6 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * boolean b1 = t.contains(4.0f);   // returns true
         * boolean b2 = t.contains(7.0f);   // returns false
         *
         * FloatTuple.FloatTuple6 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * boolean b3 = withNaN.contains(Float.NaN);   // returns true
         *
         * FloatTuple.FloatTuple6 withInf = FloatTuple.of(Float.NEGATIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * boolean b4 = withInf.contains(Float.NEGATIVE_INFINITY);   // returns true
         * boolean b5 = withInf.contains(Float.POSITIVE_INFINITY);   // returns false
         * }</pre>
         *
         * @param valueToFind the float value to search for
         * @return {@code true} if any of {@code _1} through {@code _6} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple6 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * List<Float> list = new java.util.ArrayList<>();
         * t.forEach(e -> list.add(e));   // list becomes [1.0, 2.0, 3.0, 4.0, 5.0, 6.0]
         *
         * float[] sum = {0.0f};
         * t.forEach(e -> sum[0] += e);   // sum[0] == 21.0f
         *
         * FloatTuple.FloatTuple6 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f);
         * List<Float> neg2 = new java.util.ArrayList<>();
         * neg.forEach(e -> neg2.add(e));   // neg2 becomes [-1.0, -2.0, -3.0, -4.0, -5.0, -6.0]
         *
         * // forEach(null) throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> action) throws E {
            N.checkArgNotNull(action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
            action.accept(_4);
            action.accept(_5);
            action.accept(_6);
        }

        /**
         * Returns a hash code for this tuple based on all six elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple6 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * FloatTuple.FloatTuple6 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * boolean same = (t1.hashCode() == t2.hashCode());   // returns true
         *
         * FloatTuple.FloatTuple6 t3 = FloatTuple.of(6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f);
         * boolean diff = (t1.hashCode() == t3.hashCode());   // returns false
         *
         * FloatTuple.FloatTuple6 withNaN = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * int h = withNaN.hashCode();   // well-defined (NaN values use the canonical NaN hash)
         *
         * FloatTuple.FloatTuple6 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f);
         * int h2 = neg.hashCode();   // well-defined, differs from positive counterpart
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Float.floatToIntBits(_1);
            result = 31 * result + Float.floatToIntBits(_2);
            result = 31 * result + Float.floatToIntBits(_3);
            result = 31 * result + Float.floatToIntBits(_4);
            result = 31 * result + Float.floatToIntBits(_5);
            return 31 * result + Float.floatToIntBits(_6);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple6 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * FloatTuple.FloatTuple6 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * boolean eq = t1.equals(t2);   // returns true
         *
         * FloatTuple.FloatTuple6 t3 = FloatTuple.of(6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f);
         * boolean ne = t1.equals(t3);   // returns false
         *
         * FloatTuple.FloatTuple6 withNaN = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * boolean nanEq = withNaN.equals(FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f));   // returns true
         *
         * boolean notEq = t1.equals("not a tuple");   // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple.FloatTuple6 with equal elements
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple6 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * String s = t.toString();   // returns "(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)"
         *
         * FloatTuple.FloatTuple6 neg = FloatTuple.of(-1.5f, -2.5f, -3.5f, -4.5f, -5.5f, -6.5f);
         * String s2 = neg.toString();   // returns "(-1.5, -2.5, -3.5, -4.5, -5.5, -6.5)"
         *
         * FloatTuple.FloatTuple6 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * String s3 = withNaN.toString();   // returns "(NaN, 1.0, 2.0, 3.0, 4.0, 5.0)"
         *
         * FloatTuple.FloatTuple6 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, 0.0f, 0.0f, 1.0f, 2.0f);
         * String s4 = withInf.toString();   // returns "(Infinity, -Infinity, 0.0, 0.0, 1.0, 2.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4, _5, _6)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ")";
        }

        /**
         * Returns the internal array of float elements.
         * The array is lazily initialized on first access.
         *
         * @return a float array containing all elements of this tuple
         */
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
     * <p>
     * Provides direct access to elements via public final fields {@code _1} through {@code _7}.
     * </p>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple7 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * int n = t.arity();   // returns 7
         *
         * FloatTuple.FloatTuple7 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f);
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
         * Returns the minimum value among the seven elements.
         * Uses {@link Math#min(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0f} is
         * treated as less than {@code +0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple7 t = FloatTuple.of(3.0f, 1.0f, 4.0f, 1.5f, 9.0f, 2.6f, 5.3f);
         * float mn = t.min(); // returns 1.0f
         *
         * FloatTuple.FloatTuple7 neg = FloatTuple.of(-3.0f, -1.0f, -4.0f, -1.5f, -9.0f, -2.6f, -5.3f);
         * float negMn = neg.min(); // returns -9.0f
         *
         * FloatTuple.FloatTuple7 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * float nanMn = withNaN.min(); // returns NaN
         *
         * FloatTuple.FloatTuple7 withInf = FloatTuple.of(Float.NEGATIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * float infMn = withInf.min(); // returns Float.NEGATIVE_INFINITY
         * }</pre>
         *
         * @return the smallest of {@code _1} through {@code _7}
         */
        @Override
        public float min() {
            return Math.min(Math.min(Math.min(_1, _2), Math.min(_3, _4)), Math.min(Math.min(_5, _6), _7));
        }

        /**
         * Returns the maximum value among the seven elements.
         * Uses {@link Math#max(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0f} is
         * treated as greater than {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple7 t = FloatTuple.of(3.0f, 1.0f, 4.0f, 1.5f, 9.0f, 2.6f, 5.3f);
         * float mx = t.max(); // returns 9.0f
         *
         * FloatTuple.FloatTuple7 neg = FloatTuple.of(-3.0f, -1.0f, -4.0f, -1.5f, -9.0f, -2.6f, -5.3f);
         * float negMx = neg.max(); // returns -1.0f
         *
         * FloatTuple.FloatTuple7 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * float nanMx = withNaN.max(); // returns NaN
         *
         * FloatTuple.FloatTuple7 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * float infMx = withInf.max(); // returns Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the largest of {@code _1} through {@code _7}
         */
        @Override
        public float max() {
            return Math.max(Math.max(Math.max(_1, _2), Math.max(_3, _4)), Math.max(Math.max(_5, _6), _7));
        }

        /**
         * Returns the median value of the seven elements.
         * For an odd number of elements, this is the exact middle value when sorted.
         * Ordering uses {@link Float#compare(float, float)} semantics, so
         * {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple7 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * float med = t.median(); // returns 4.0f  (middle of sorted [1,2,3,4,5,6,7])
         *
         * FloatTuple.FloatTuple7 dup = FloatTuple.of(1.0f, 1.0f, 1.0f, 5.0f, 9.0f, 9.0f, 9.0f);
         * float dupMed = dup.median(); // returns 5.0f
         *
         * FloatTuple.FloatTuple7 neg = FloatTuple.of(-7.0f, -5.0f, -3.0f, -1.0f, 1.0f, 3.0f, 5.0f);
         * float negMed = neg.median(); // returns -1.0f
         *
         * FloatTuple.FloatTuple7 withNaN = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, Float.NaN);
         * float nanMed = withNaN.median(); // returns 4.0f  (NaN treated as largest)
         * }</pre>
         *
         * @return the middle value when the seven elements are sorted
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of the seven elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple7 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * float s = t.sum(); // returns 28.0f
         *
         * FloatTuple.FloatTuple7 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f);
         * float negS = neg.sum(); // returns -28.0f
         *
         * FloatTuple.FloatTuple7 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * float nanS = withNaN.sum(); // returns NaN
         *
         * FloatTuple.FloatTuple7 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * float infS = withInf.sum(); // returns Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1} through {@code _7}
         */
        @Override
        public float sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the arithmetic mean of the seven elements as a {@code double}.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple7 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * double avg = t.average(); // returns 4.0
         *
         * FloatTuple.FloatTuple7 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f);
         * double negAvg = neg.average(); // returns -4.0
         *
         * FloatTuple.FloatTuple7 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * double nanAvg = withNaN.average(); // returns NaN
         *
         * FloatTuple.FloatTuple7 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
         * double infAvg = withInf.average(); // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the average of {@code _1} through {@code _7}
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
         * FloatTuple.FloatTuple7 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * FloatTuple.FloatTuple7 reversed = t.reverse(); // returns (7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0)
         *
         * FloatTuple.FloatTuple7 dup = FloatTuple.of(1.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 5.0f);
         * FloatTuple.FloatTuple7 dupRev = dup.reverse(); // returns (5.0, 5.0, 4.0, 3.0, 2.0, 1.0, 1.0)
         *
         * FloatTuple.FloatTuple7 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, Float.NaN, Float.POSITIVE_INFINITY, -6.0f, -7.0f);
         * float revFirst = neg.reverse()._1; // returns -7.0f
         *
         * FloatTuple.FloatTuple7 same = FloatTuple.of(5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f);
         * FloatTuple.FloatTuple7 sameRev = same.reverse(); // returns (5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0)
         * }</pre>
         *
         * @return a new FloatTuple.FloatTuple7 with (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public FloatTuple7 reverse() {
            return new FloatTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified float value.
         * Comparisons use {@link Float#compare(float, float)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0f} does not match {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple7 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * boolean has3 = t.contains(3.0f); // returns true
         * boolean has8 = t.contains(8.0f); // returns false
         *
         * FloatTuple.FloatTuple7 withNaN = FloatTuple.of(1.0f, 2.0f, Float.NaN, 4.0f, 5.0f, 6.0f, 7.0f);
         * boolean hasNaN = withNaN.contains(Float.NaN); // returns true
         *
         * FloatTuple.FloatTuple7 withInf = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, Float.POSITIVE_INFINITY);
         * boolean hasInf = withInf.contains(Float.POSITIVE_INFINITY); // returns true
         * boolean hasMissing = withInf.contains(-1.0f);               // returns false
         * }</pre>
         *
         * @param valueToFind the float value to search for
         * @return {@code true} if any of {@code _1} through {@code _7} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple7 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * float[] collected = new float[7];
         * int[] idx = {0};
         * t.forEach(v -> collected[idx[0]++] = v); // collected == {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f}
         *
         * float[] sum = {0.0f};
         * t.forEach(v -> sum[0] += v); // sum[0] == 28.0f
         *
         * // null action throws IllegalArgumentException
         * // t.forEach(null); // throws IllegalArgumentException
         *
         * FloatTuple.FloatTuple7 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f);
         * float[] negSum = {0.0f};
         * neg.forEach(v -> negSum[0] += v); // negSum[0] == -28.0f
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> action) throws E {
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
         * Returns a hash code for this tuple based on all seven elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple7 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * FloatTuple.FloatTuple7 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * boolean sameHash = t1.hashCode() == t2.hashCode(); // returns true (equal tuples)
         *
         * FloatTuple.FloatTuple7 t3 = FloatTuple.of(7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f);
         * boolean diffHash = t1.hashCode() == t3.hashCode(); // returns false (order matters)
         *
         * FloatTuple.FloatTuple7 nanTuple = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * int nanHash = nanTuple.hashCode(); // consistent for canonical NaN values
         *
         * FloatTuple.FloatTuple7 negZero = FloatTuple.of(-0.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * FloatTuple.FloatTuple7 posZero = FloatTuple.of(0.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * boolean zeroDiff = negZero.hashCode() == posZero.hashCode(); // returns false (-0.0 vs +0.0)
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Float.floatToIntBits(_1);
            result = 31 * result + Float.floatToIntBits(_2);
            result = 31 * result + Float.floatToIntBits(_3);
            result = 31 * result + Float.floatToIntBits(_4);
            result = 31 * result + Float.floatToIntBits(_5);
            result = 31 * result + Float.floatToIntBits(_6);
            return 31 * result + Float.floatToIntBits(_7);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple7 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * FloatTuple.FloatTuple7 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * boolean eq = t1.equals(t2); // returns true
         *
         * FloatTuple.FloatTuple7 t3 = FloatTuple.of(7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f);
         * boolean neq = t1.equals(t3); // returns false (different order)
         *
         * FloatTuple.FloatTuple7 nanTuple1 = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * FloatTuple.FloatTuple7 nanTuple2 = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * boolean nanEq = nanTuple1.equals(nanTuple2); // returns true (NaN equals NaN via Float.compare)
         *
         * boolean nullEq = t1.equals(null); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple.FloatTuple7 with equal elements
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple7 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * String s = t.toString(); // returns "(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0)"
         *
         * FloatTuple.FloatTuple7 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f);
         * String negS = neg.toString(); // returns "(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0)"
         *
         * FloatTuple.FloatTuple7 special = FloatTuple.of(Float.NaN, Float.POSITIVE_INFINITY, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
         * String specS = special.toString(); // returns "(NaN, Infinity, 0.0, 0.0, 0.0, 0.0, 0.0)"
         *
         * FloatTuple.FloatTuple7 zeros = FloatTuple.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
         * String zeroS = zeros.toString(); // returns "(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4, _5, _6, _7)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ")";
        }

        /**
         * Returns the internal array of float elements.
         * The array is lazily initialized on first access.
         *
         * @return a float array containing all elements of this tuple
         */
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
     * <p>
     * Provides direct access to elements via public final fields {@code _1} through {@code _8}.
     * </p>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple8 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * int a = t.arity(); // returns 8
         *
         * FloatTuple.FloatTuple8 zeros = FloatTuple.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
         * int za = zeros.arity(); // returns 8
         *
         * FloatTuple.FloatTuple8 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f, -8.0f);
         * int na = neg.arity(); // returns 8
         *
         * FloatTuple.FloatTuple8 mixed = FloatTuple.of(Float.NaN, Float.POSITIVE_INFINITY, 0.0f, -0.0f, 1.0f, 2.0f, 3.0f, 4.0f);
         * int ma = mixed.arity(); // returns 8
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
         * Uses {@link Math#min(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0f} is
         * treated as less than {@code +0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple8 t = FloatTuple.of(3.0f, 1.0f, 4.0f, 1.5f, 9.0f, 2.6f, 5.3f, 8.0f);
         * float mn = t.min(); // returns 1.0f
         *
         * FloatTuple.FloatTuple8 neg = FloatTuple.of(-3.0f, -1.0f, -4.0f, -1.5f, -9.0f, -2.6f, -5.3f, -8.0f);
         * float negMn = neg.min(); // returns -9.0f
         *
         * FloatTuple.FloatTuple8 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * float nanMn = withNaN.min(); // returns NaN
         *
         * FloatTuple.FloatTuple8 withInf = FloatTuple.of(Float.NEGATIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * float infMn = withInf.min(); // returns Float.NEGATIVE_INFINITY
         * }</pre>
         *
         * @return the smallest of {@code _1} through {@code _8}
         */
        @Override
        public float min() {
            return Math.min(Math.min(Math.min(_1, _2), Math.min(_3, _4)), Math.min(Math.min(_5, _6), Math.min(_7, _8)));
        }

        /**
         * Returns the maximum value among the eight elements.
         * Uses {@link Math#max(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0f} is
         * treated as greater than {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple8 t = FloatTuple.of(3.0f, 1.0f, 4.0f, 1.5f, 9.0f, 2.6f, 5.3f, 8.0f);
         * float mx = t.max(); // returns 9.0f
         *
         * FloatTuple.FloatTuple8 neg = FloatTuple.of(-3.0f, -1.0f, -4.0f, -1.5f, -9.0f, -2.6f, -5.3f, -8.0f);
         * float negMx = neg.max(); // returns -1.0f
         *
         * FloatTuple.FloatTuple8 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * float nanMx = withNaN.max(); // returns NaN
         *
         * FloatTuple.FloatTuple8 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * float infMx = withInf.max(); // returns Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the largest of {@code _1} through {@code _8}
         */
        @Override
        public float max() {
            return Math.max(Math.max(Math.max(_1, _2), Math.max(_3, _4)), Math.max(Math.max(_5, _6), Math.max(_7, _8)));
        }

        /**
         * Returns the median value of the eight elements.
         * For an even number of elements, returns the lower of the two middle values
         * (not their average). Ordering uses {@link Float#compare(float, float)}
         * semantics, so {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple8 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * float med = t.median(); // returns 4.0f  (lower of two middle values 4,5 in sorted [1..8])
         *
         * FloatTuple.FloatTuple8 dup = FloatTuple.of(1.0f, 1.0f, 1.0f, 5.0f, 5.0f, 9.0f, 9.0f, 9.0f);
         * float dupMed = dup.median(); // returns 5.0f
         *
         * FloatTuple.FloatTuple8 neg = FloatTuple.of(-8.0f, -6.0f, -4.0f, -2.0f, 1.0f, 3.0f, 5.0f, 7.0f);
         * float negMed = neg.median(); // returns -2.0f
         *
         * FloatTuple.FloatTuple8 withNaN = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, Float.NaN);
         * float nanMed = withNaN.median(); // returns 4.0f  (NaN treated as largest)
         * }</pre>
         *
         * @return the lower middle value when the eight elements are sorted
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of the eight elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple8 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * float s = t.sum(); // returns 36.0f
         *
         * FloatTuple.FloatTuple8 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f, -8.0f);
         * float negS = neg.sum(); // returns -36.0f
         *
         * FloatTuple.FloatTuple8 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * float nanS = withNaN.sum(); // returns NaN
         *
         * FloatTuple.FloatTuple8 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * float infS = withInf.sum(); // returns Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1} through {@code _8}
         */
        @Override
        public float sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the arithmetic mean of the eight elements as a {@code double}.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple8 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * double avg = t.average(); // returns 4.5
         *
         * FloatTuple.FloatTuple8 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f, -8.0f);
         * double negAvg = neg.average(); // returns -4.5
         *
         * FloatTuple.FloatTuple8 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * double nanAvg = withNaN.average(); // returns NaN
         *
         * FloatTuple.FloatTuple8 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f);
         * double infAvg = withInf.average(); // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the average of {@code _1} through {@code _8}
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
         * FloatTuple.FloatTuple8 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * FloatTuple.FloatTuple8 reversed = t.reverse(); // returns (8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0)
         *
         * FloatTuple.FloatTuple8 dup = FloatTuple.of(1.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 5.0f, 6.0f);
         * FloatTuple.FloatTuple8 dupRev = dup.reverse(); // returns (6.0, 5.0, 5.0, 4.0, 3.0, 2.0, 1.0, 1.0)
         *
         * FloatTuple.FloatTuple8 withSpecial = FloatTuple.of(Float.NaN, Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, Float.NEGATIVE_INFINITY);
         * float revFirst = withSpecial.reverse()._1; // returns Float.NEGATIVE_INFINITY
         *
         * FloatTuple.FloatTuple8 same = FloatTuple.of(5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f);
         * FloatTuple.FloatTuple8 sameRev = same.reverse(); // returns (5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0)
         * }</pre>
         *
         * @return a new FloatTuple.FloatTuple8 with (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public FloatTuple8 reverse() {
            return new FloatTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified float value.
         * Comparisons use {@link Float#compare(float, float)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0f} does not match {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple8 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * boolean has3 = t.contains(3.0f); // returns true
         * boolean has9 = t.contains(9.0f); // returns false
         *
         * FloatTuple.FloatTuple8 withNaN = FloatTuple.of(1.0f, 2.0f, Float.NaN, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * boolean hasNaN = withNaN.contains(Float.NaN); // returns true
         *
         * FloatTuple.FloatTuple8 withInf = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, Float.POSITIVE_INFINITY);
         * boolean hasInf = withInf.contains(Float.POSITIVE_INFINITY); // returns true
         * boolean hasMissing = withInf.contains(-1.0f);               // returns false
         * }</pre>
         *
         * @param valueToFind the float value to search for
         * @return {@code true} if any of {@code _1} through {@code _8} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple8 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * float[] collected = new float[8];
         * int[] idx = {0};
         * t.forEach(v -> collected[idx[0]++] = v); // collected == {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f}
         *
         * float[] sum = {0.0f};
         * t.forEach(v -> sum[0] += v); // sum[0] == 36.0f
         *
         * // null action throws IllegalArgumentException
         * // t.forEach(null); // throws IllegalArgumentException
         *
         * FloatTuple.FloatTuple8 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f, -8.0f);
         * float[] negSum = {0.0f};
         * neg.forEach(v -> negSum[0] += v); // negSum[0] == -36.0f
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> action) throws E {
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
         * Returns a hash code for this tuple based on all eight elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple8 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * FloatTuple.FloatTuple8 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * boolean sameHash = t1.hashCode() == t2.hashCode(); // returns true (equal tuples)
         *
         * FloatTuple.FloatTuple8 t3 = FloatTuple.of(8.0f, 7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f);
         * boolean diffHash = t1.hashCode() == t3.hashCode(); // returns false (order matters)
         *
         * FloatTuple.FloatTuple8 nanTuple = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * int nanHash = nanTuple.hashCode(); // consistent for canonical NaN values
         *
         * FloatTuple.FloatTuple8 negZero = FloatTuple.of(-0.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * FloatTuple.FloatTuple8 posZero = FloatTuple.of(0.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * boolean zeroDiff = negZero.hashCode() == posZero.hashCode(); // returns false (-0.0 vs +0.0)
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Float.floatToIntBits(_1);
            result = 31 * result + Float.floatToIntBits(_2);
            result = 31 * result + Float.floatToIntBits(_3);
            result = 31 * result + Float.floatToIntBits(_4);
            result = 31 * result + Float.floatToIntBits(_5);
            result = 31 * result + Float.floatToIntBits(_6);
            result = 31 * result + Float.floatToIntBits(_7);
            return 31 * result + Float.floatToIntBits(_8);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple8 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * FloatTuple.FloatTuple8 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * boolean eq = t1.equals(t2); // returns true
         *
         * FloatTuple.FloatTuple8 t3 = FloatTuple.of(8.0f, 7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f);
         * boolean neq = t1.equals(t3); // returns false (different order)
         *
         * FloatTuple.FloatTuple8 nanTuple1 = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * FloatTuple.FloatTuple8 nanTuple2 = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * boolean nanEq = nanTuple1.equals(nanTuple2); // returns true (NaN equals NaN via Float.compare)
         *
         * boolean nullEq = t1.equals(null); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple.FloatTuple8 with equal elements
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple8 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * String s = t.toString(); // returns "(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0)"
         *
         * FloatTuple.FloatTuple8 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f, -8.0f);
         * String negS = neg.toString(); // returns "(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0)"
         *
         * FloatTuple.FloatTuple8 special = FloatTuple.of(Float.NaN, Float.POSITIVE_INFINITY, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
         * String specS = special.toString(); // returns "(NaN, Infinity, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)"
         *
         * FloatTuple.FloatTuple8 zeros = FloatTuple.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
         * String zeroS = zeros.toString(); // returns "(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4, _5, _6, _7, _8)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")";
        }

        /**
         * Returns the internal array of float elements.
         * The array is lazily initialized on first access.
         *
         * @return a float array containing all elements of this tuple
         */
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
     * <p>
     * Provides direct access to elements via public final fields {@code _1} through {@code _9}.
     * </p>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more float values
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple9 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * int a = t.arity(); // returns 9
         *
         * FloatTuple.FloatTuple9 zeros = FloatTuple.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
         * int za = zeros.arity(); // returns 9
         *
         * FloatTuple.FloatTuple9 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f, -8.0f, -9.0f);
         * int na = neg.arity(); // returns 9
         *
         * FloatTuple.FloatTuple9 mixed = FloatTuple.of(Float.NaN, Float.POSITIVE_INFINITY, 0.0f, -0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
         * int ma = mixed.arity(); // returns 9
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
         * Uses {@link Math#min(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0f} is
         * treated as less than {@code +0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple9 t = FloatTuple.of(3.0f, 1.0f, 4.0f, 1.5f, 9.0f, 2.6f, 5.3f, 8.0f, 7.0f);
         * float mn = t.min(); // returns 1.0f
         *
         * FloatTuple.FloatTuple9 neg = FloatTuple.of(-3.0f, -1.0f, -4.0f, -1.5f, -9.0f, -2.6f, -5.3f, -8.0f, -7.0f);
         * float negMn = neg.min(); // returns -9.0f
         *
         * FloatTuple.FloatTuple9 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * float nanMn = withNaN.min(); // returns NaN
         *
         * FloatTuple.FloatTuple9 withInf = FloatTuple.of(Float.NEGATIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * float infMn = withInf.min(); // returns Float.NEGATIVE_INFINITY
         * }</pre>
         *
         * @return the smallest of {@code _1} through {@code _9}
         */
        @Override
        public float min() {
            return Math.min(Math.min(Math.min(Math.min(_1, _2), Math.min(_3, _4)), Math.min(Math.min(_5, _6), Math.min(_7, _8))), _9);
        }

        /**
         * Returns the maximum value among the nine elements.
         * Uses {@link Math#max(float, float)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0f} is
         * treated as greater than {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple9 t = FloatTuple.of(3.0f, 1.0f, 4.0f, 1.5f, 9.0f, 2.6f, 5.3f, 8.0f, 7.0f);
         * float mx = t.max(); // returns 9.0f
         *
         * FloatTuple.FloatTuple9 neg = FloatTuple.of(-3.0f, -1.0f, -4.0f, -1.5f, -9.0f, -2.6f, -5.3f, -8.0f, -7.0f);
         * float negMx = neg.max(); // returns -1.0f
         *
         * FloatTuple.FloatTuple9 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * float nanMx = withNaN.max(); // returns NaN
         *
         * FloatTuple.FloatTuple9 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * float infMx = withInf.max(); // returns Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the largest of {@code _1} through {@code _9}
         */
        @Override
        public float max() {
            return Math.max(Math.max(Math.max(Math.max(_1, _2), Math.max(_3, _4)), Math.max(Math.max(_5, _6), Math.max(_7, _8))), _9);
        }

        /**
         * Returns the median value of the nine elements.
         * For an odd number of elements, this is the exact middle value when sorted.
         * Ordering uses {@link Float#compare(float, float)} semantics, so
         * {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple9 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * float med = t.median(); // returns 5.0f  (middle of sorted [1..9])
         *
         * FloatTuple.FloatTuple9 dup = FloatTuple.of(1.0f, 1.0f, 1.0f, 5.0f, 5.0f, 9.0f, 9.0f, 9.0f, 9.0f);
         * float dupMed = dup.median(); // returns 5.0f
         *
         * FloatTuple.FloatTuple9 neg = FloatTuple.of(-9.0f, -7.0f, -5.0f, -3.0f, -1.0f, 1.0f, 3.0f, 5.0f, 7.0f);
         * float negMed = neg.median(); // returns -1.0f
         *
         * FloatTuple.FloatTuple9 withNaN = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, Float.NaN);
         * float nanMed = withNaN.median(); // returns 5.0f  (NaN treated as largest)
         * }</pre>
         *
         * @return the middle value when the nine elements are sorted
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of the nine elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple9 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * float s = t.sum(); // returns 45.0f
         *
         * FloatTuple.FloatTuple9 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f, -8.0f, -9.0f);
         * float negS = neg.sum(); // returns -45.0f
         *
         * FloatTuple.FloatTuple9 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * float nanS = withNaN.sum(); // returns NaN
         *
         * FloatTuple.FloatTuple9 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * float infS = withInf.sum(); // returns Float.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1} through {@code _9}
         */
        @Override
        public float sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the arithmetic mean of the nine elements as a {@code double}.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple9 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * double avg = t.average(); // returns 5.0
         *
         * FloatTuple.FloatTuple9 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f, -8.0f, -9.0f);
         * double negAvg = neg.average(); // returns -5.0
         *
         * FloatTuple.FloatTuple9 withNaN = FloatTuple.of(Float.NaN, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * double nanAvg = withNaN.average(); // returns NaN
         *
         * FloatTuple.FloatTuple9 withInf = FloatTuple.of(Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
         * double infAvg = withInf.average(); // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the average of {@code _1} through {@code _9}
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
         * FloatTuple.FloatTuple9 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * FloatTuple.FloatTuple9 reversed = t.reverse(); // returns (9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0)
         *
         * FloatTuple.FloatTuple9 dup = FloatTuple.of(1.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 5.0f, 6.0f, 7.0f);
         * FloatTuple.FloatTuple9 dupRev = dup.reverse(); // returns (7.0, 6.0, 5.0, 5.0, 4.0, 3.0, 2.0, 1.0, 1.0)
         *
         * FloatTuple.FloatTuple9 withSpecial = FloatTuple.of(Float.NaN, Float.POSITIVE_INFINITY, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, Float.NEGATIVE_INFINITY);
         * float revFirst = withSpecial.reverse()._1; // returns Float.NEGATIVE_INFINITY
         *
         * FloatTuple.FloatTuple9 same = FloatTuple.of(5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f);
         * FloatTuple.FloatTuple9 sameRev = same.reverse(); // returns (5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0)
         * }</pre>
         *
         * @return a new FloatTuple.FloatTuple9 with (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public FloatTuple9 reverse() {
            return new FloatTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified float value.
         * Comparisons use {@link Float#compare(float, float)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0f} does not match {@code -0.0f}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple9 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * boolean has3 = t.contains(3.0f);   // returns true
         * boolean has10 = t.contains(10.0f); // returns false
         *
         * FloatTuple.FloatTuple9 withNaN = FloatTuple.of(1.0f, 2.0f, Float.NaN, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * boolean hasNaN = withNaN.contains(Float.NaN); // returns true
         *
         * FloatTuple.FloatTuple9 withInf = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, Float.POSITIVE_INFINITY);
         * boolean hasInf = withInf.contains(Float.POSITIVE_INFINITY); // returns true
         * boolean hasMissing = withInf.contains(-1.0f);               // returns false
         * }</pre>
         *
         * @param valueToFind the float value to search for
         * @return {@code true} if any of {@code _1} through {@code _9} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind) || N.equals(_9, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple9 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * float[] collected = new float[9];
         * int[] idx = {0};
         * t.forEach(v -> collected[idx[0]++] = v); // collected == {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f}
         *
         * float[] sum = {0.0f};
         * t.forEach(v -> sum[0] += v); // sum[0] == 45.0f
         *
         * // null action throws IllegalArgumentException
         * // t.forEach(null); // throws IllegalArgumentException
         *
         * FloatTuple.FloatTuple9 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f, -8.0f, -9.0f);
         * float[] negSum = {0.0f};
         * neg.forEach(v -> negSum[0] += v); // negSum[0] == -45.0f
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> action) throws E {
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
         * Returns a hash code for this tuple based on all nine elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple9 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * FloatTuple.FloatTuple9 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * boolean sameHash = t1.hashCode() == t2.hashCode(); // returns true (equal tuples)
         *
         * FloatTuple.FloatTuple9 t3 = FloatTuple.of(9.0f, 8.0f, 7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f);
         * boolean diffHash = t1.hashCode() == t3.hashCode(); // returns false (order matters)
         *
         * FloatTuple.FloatTuple9 nanTuple = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * int nanHash = nanTuple.hashCode(); // consistent for canonical NaN values
         *
         * FloatTuple.FloatTuple9 negZero = FloatTuple.of(-0.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * FloatTuple.FloatTuple9 posZero = FloatTuple.of(0.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * boolean zeroDiff = negZero.hashCode() == posZero.hashCode(); // returns false (-0.0 vs +0.0)
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Float.floatToIntBits(_1);
            result = 31 * result + Float.floatToIntBits(_2);
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple9 t1 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * FloatTuple.FloatTuple9 t2 = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * boolean eq = t1.equals(t2); // returns true
         *
         * FloatTuple.FloatTuple9 t3 = FloatTuple.of(9.0f, 8.0f, 7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f);
         * boolean neq = t1.equals(t3); // returns false (different order)
         *
         * FloatTuple.FloatTuple9 nanTuple1 = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * FloatTuple.FloatTuple9 nanTuple2 = FloatTuple.of(Float.NaN, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * boolean nanEq = nanTuple1.equals(nanTuple2); // returns true (NaN equals NaN via Float.compare)
         *
         * boolean nullEq = t1.equals(null); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a FloatTuple.FloatTuple9 with equal elements
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
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * FloatTuple.FloatTuple9 t = FloatTuple.of(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
         * String s = t.toString(); // returns "(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)"
         *
         * FloatTuple.FloatTuple9 neg = FloatTuple.of(-1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f, -7.0f, -8.0f, -9.0f);
         * String negS = neg.toString(); // returns "(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0, -9.0)"
         *
         * FloatTuple.FloatTuple9 special = FloatTuple.of(Float.NaN, Float.POSITIVE_INFINITY, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
         * String specS = special.toString(); // returns "(NaN, Infinity, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)"
         *
         * FloatTuple.FloatTuple9 zeros = FloatTuple.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
         * String zeroS = zeros.toString(); // returns "(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4, _5, _6, _7, _8, _9)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ")";
        }

        /**
         * Returns the internal array of float elements.
         * The array is lazily initialized on first access.
         *
         * @return a float array containing all elements of this tuple
         */
        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
