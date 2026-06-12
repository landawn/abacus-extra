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
import com.landawn.abacus.util.DoubleTuple.DoubleTuple0;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple1;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple3;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple4;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple5;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple6;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple7;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple8;
import com.landawn.abacus.util.DoubleTuple.DoubleTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.DoubleStream;

/**
 * Base class for immutable tuples of primitive {@code double} values.
 *
 * <p>The nested tuple types model fixed arities from 0 through 9. Factory methods such as
 * {@link #copyOf(double[])} and the {@code of(...)} overloads select the matching subtype, while the
 * base class supplies aggregate, reversal, containment, and functional helper operations.</p>
 *
 * <p>This sealed base class permits only the built-in arity-specific nested tuple types.</p>
 *
 * @param <TP> the concrete {@code DoubleTuple} subtype that fluent operations such as {@link #reverse()} return
 * @see PrimitiveTuple
 * @see BooleanTuple
 * @see ByteTuple
 * @see CharTuple
 * @see ShortTuple
 * @see IntTuple
 * @see LongTuple
 * @see FloatTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract sealed class DoubleTuple<TP extends DoubleTuple<TP>> extends PrimitiveTuple<TP>
        permits DoubleTuple0, DoubleTuple1, DoubleTuple2, DoubleTuple3, DoubleTuple4, DoubleTuple5, DoubleTuple6, DoubleTuple7, DoubleTuple8, DoubleTuple9 {

    /** Lazily initialized cached array view of all tuple elements. */
    protected volatile double[] elements;

    /**
     * Protected constructor for subclass instantiation.
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link #of(double)}, {@link #of(double, double)}, etc., to create tuple instances.
     */
    protected DoubleTuple() {
    }

    /**
     * Creates a DoubleTuple.DoubleTuple1 containing a single double value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple1 single = DoubleTuple.of(3.14);
     * double value = single._1;     // 3.14
     * int arity = single.arity();   // 1
     *
     * // Negative value
     * DoubleTuple.DoubleTuple1 neg = DoubleTuple.of(-1.5);
     * double v = neg._1;   // -1.5
     *
     * // NaN is a valid element
     * DoubleTuple.DoubleTuple1 nan = DoubleTuple.of(Double.NaN);
     * assertTrue(Double.isNaN(nan._1));   // _1 is NaN
     * }</pre>
     *
     * @param _1 the double value to store in the tuple
     * @return a new DoubleTuple.DoubleTuple1 containing the specified value
     */
    public static DoubleTuple1 of(final double _1) {
        return new DoubleTuple1(_1);
    }

    /**
     * Creates a DoubleTuple.DoubleTuple2 containing two double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * double first = pair._1;    // 1.5
     * double second = pair._2;   // 2.5
     *
     * // Sum and average
     * double sum = pair.sum();       // 4.0
     * double avg = pair.average();   // 2.0
     *
     * // Infinity is a valid element
     * DoubleTuple.DoubleTuple2 inf = DoubleTuple.of(Double.POSITIVE_INFINITY, 1.0);
     * double maxVal = inf.max();   // Double.POSITIVE_INFINITY
     *
     * // Negative values
     * DoubleTuple.DoubleTuple2 neg = DoubleTuple.of(-3.0, -1.0);
     * double minVal = neg.min();   // -3.0
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @return a new DoubleTuple.DoubleTuple2 containing the specified values
     */
    public static DoubleTuple2 of(final double _1, final double _2) {
        return new DoubleTuple2(_1, _2);
    }

    /**
     * Creates a DoubleTuple.DoubleTuple3 containing three double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple3 triple = DoubleTuple.of(1.0, 2.0, 3.0);
     * double third = triple._3;          // 3.0
     * double median = triple.median();   // 2.0
     *
     * // Reverse produces a new tuple
     * DoubleTuple.DoubleTuple3 rev = triple.reverse();   // (3.0, 2.0, 1.0)
     *
     * // Out-of-order elements - min/max still correct
     * DoubleTuple.DoubleTuple3 unordered = DoubleTuple.of(30.0, 10.0, 20.0);
     * double min = unordered.min();   // 10.0
     * double max = unordered.max();   // 30.0
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @return a new DoubleTuple.DoubleTuple3 containing the specified values
     */
    public static DoubleTuple3 of(final double _1, final double _2, final double _3) {
        return new DoubleTuple3(_1, _2, _3);
    }

    /**
     * Creates a DoubleTuple.DoubleTuple4 containing four double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
     * double fourth = tuple._4;   // 4.0
     * double sum = tuple.sum();   // 10.0
     *
     * // Even-arity median returns the lower middle value (sorted order)
     * double median = tuple.median();   // 2.0
     *
     * // Negative values
     * DoubleTuple.DoubleTuple4 neg = DoubleTuple.of(-4.0, -1.0, -3.0, -2.0);
     * double min = neg.min();   // -4.0
     * double max = neg.max();   // -1.0
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @return a new DoubleTuple.DoubleTuple4 containing the specified values
     */
    public static DoubleTuple4 of(final double _1, final double _2, final double _3, final double _4) {
        return new DoubleTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a DoubleTuple.DoubleTuple5 containing five double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
     * double median = tuple.median();   // 3.0
     * double sum = tuple.sum();         // 15.0
     *
     * // Average of five elements
     * double avg = tuple.average();   // 3.0
     *
     * // Verify arity and containment
     * int arity = tuple.arity();               // 5
     * boolean found = tuple.contains(4.0);     // true
     * boolean missing = tuple.contains(6.0);   // false
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @param _5 the fifth double value
     * @return a new DoubleTuple.DoubleTuple5 containing the specified values
     */
    public static DoubleTuple5 of(final double _1, final double _2, final double _3, final double _4, final double _5) {
        return new DoubleTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a DoubleTuple.DoubleTuple6 containing six double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
     * double sum = tuple.sum();       // 21.0
     * double avg = tuple.average();   // 3.5
     *
     * // Even arity: median returns lower middle value when sorted
     * double median = tuple.median();   // 3.0
     *
     * // toString format
     * String s = tuple.toString();   // "(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)"
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @param _5 the fifth double value
     * @param _6 the sixth double value
     * @return a new DoubleTuple.DoubleTuple6 containing the specified values
     */
    public static DoubleTuple6 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6) {
        return new DoubleTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a DoubleTuple.DoubleTuple7 containing seven double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
     * DoubleTuple.DoubleTuple7 reversed = tuple.reverse();   // (7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0)
     * double median = tuple.median();                        // 4.0
     *
     * // toString format
     * String s = tuple.toString();   // "(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0)"
     *
     * // Infinity element is supported
     * DoubleTuple.DoubleTuple7 inf = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, Double.POSITIVE_INFINITY);
     * double max = inf.max();   // Double.POSITIVE_INFINITY
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @param _5 the fifth double value
     * @param _6 the sixth double value
     * @param _7 the seventh double value
     * @return a new DoubleTuple.DoubleTuple7 containing the specified values
     */
    public static DoubleTuple7 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7) {
        return new DoubleTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a DoubleTuple.DoubleTuple8 containing eight double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
     * double[] array = tuple.toArray();   // [1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0]
     * double sum = tuple.sum();           // 36.0
     *
     * // Even arity: median returns lower middle value (index 3 of sorted array)
     * double median = tuple.median();   // 4.0
     *
     * // Negatives are stored as-is
     * DoubleTuple.DoubleTuple8 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0);
     * double min = neg.min();   // -8.0
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @param _5 the fifth double value
     * @param _6 the sixth double value
     * @param _7 the seventh double value
     * @param _8 the eighth double value
     * @return a new DoubleTuple.DoubleTuple8 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more double values
     */
    @Deprecated
    public static DoubleTuple8 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7,
            final double _8) {
        return new DoubleTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a DoubleTuple.DoubleTuple9 containing nine double values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
     * DoubleTuple.DoubleTuple9 reversed = tuple.reverse();   // (9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0)
     * double median = tuple.median();                        // 5.0
     *
     * // toString format
     * String s = tuple.toString();   // "(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)"
     *
     * // Nine is the maximum arity; copyOf with 10 elements throws IllegalArgumentException
     * // DoubleTuple.copyOf(new double[10]);   // throws IllegalArgumentException
     * }</pre>
     *
     * @param _1 the first double value
     * @param _2 the second double value
     * @param _3 the third double value
     * @param _4 the fourth double value
     * @param _5 the fifth double value
     * @param _6 the sixth double value
     * @param _7 the seventh double value
     * @param _8 the eighth double value
     * @param _9 the ninth double value
     * @return a new DoubleTuple.DoubleTuple9 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more double values
     */
    @Deprecated
    public static DoubleTuple9 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7,
            final double _8, final double _9) {
        return new DoubleTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a DoubleTuple from an array of double values.
     * <p>
     * The size of the returned tuple depends on the length of the input array.
     * This factory method supports arrays with 0 to 9 elements. For {@code null} or empty
     * arrays, returns the shared empty tuple. For arrays with 1-9 elements, returns the
     * corresponding {@code DoubleTuple1}..{@code DoubleTuple9} instance. The values are copied
     * into the new tuple; subsequent modifications to the input array do not affect it.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create from 3-element array
     * double[] values = {1.0, 2.0, 3.0};
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.copyOf(values);
     * double third = tuple._3;   // 3.0
     *
     * // Single element
     * DoubleTuple.DoubleTuple1 single = DoubleTuple.copyOf(new double[]{3.14});
     * double v = single._1;   // 3.14
     *
     * // null or empty array returns the empty singleton (arity 0)
     * DoubleTuple<?> emptyFromNull = DoubleTuple.copyOf(null);
     * int arityNull = emptyFromNull.arity();   // 0
     *
     * DoubleTuple<?> emptyFromArr = DoubleTuple.copyOf(new double[0]);
     * String s = emptyFromArr.toString();   // "()"
     *
     * // Array with more than 9 elements throws IllegalArgumentException
     * // DoubleTuple.copyOf(new double[10]);   // throws IllegalArgumentException
     * }</pre>
     *
     * <p><strong>Type note:</strong> the runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type. Assigning to the wrong arity-specific subtype will result in a
     * {@link ClassCastException} at the assignment site.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of double values; may be {@code null} or empty, in which case the shared empty tuple is returned
     * @return a {@code DoubleTuple} of the appropriate arity containing the array values, or the shared empty tuple if the array is {@code null} or empty
     * @throws IllegalArgumentException if {@code values} has more than 9 elements
     * @see #of(double)
     */
    @SuppressWarnings("deprecation")
    public static <TP extends DoubleTuple<TP>> TP copyOf(final double[] values) {
        if (values == null || values.length == 0) {
            return (TP) DoubleTuple0.EMPTY;
        }

        switch (values.length) {
            case 1:
                return (TP) DoubleTuple.of(values[0]);

            case 2:
                return (TP) DoubleTuple.of(values[0], values[1]);

            case 3:
                return (TP) DoubleTuple.of(values[0], values[1], values[2]);

            case 4:
                return (TP) DoubleTuple.of(values[0], values[1], values[2], values[3]);

            case 5:
                return (TP) DoubleTuple.of(values[0], values[1], values[2], values[3], values[4]);

            case 6:
                return (TP) DoubleTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);

            case 7:
                return (TP) DoubleTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);

            case 8:
                return (TP) DoubleTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);

            case 9:
                return (TP) DoubleTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);

            default:
                throw new IllegalArgumentException("Too many elements (" + values.length + "). Maximum: 9");
        }
    }

    /**
     * Returns the minimum double value in this tuple.
     * <p>
     * This method finds and returns the smallest double value among all elements
     * in the tuple. For tuples with a single element, returns that element.
     * </p>
     * <p>
     * Comparisons are performed pairwise with {@link Math#min(double, double)}, so any
     * {@code NaN} element causes the result to be {@code NaN}, and {@code -0.0} is
     * treated as smaller than {@code +0.0}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
     * double min = tuple.min();   // 1.0
     *
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(2.5, 1.5);
     * double minPair = pair.min();   // 1.5
     *
     * // NaN propagates through Math.min
     * DoubleTuple.DoubleTuple2 nanTuple = DoubleTuple.of(1.0, Double.NaN);
     * double nanMin = nanTuple.min();   // NaN
     *
     * // Empty tuple throws NoSuchElementException
     * DoubleTuple<?> empty = DoubleTuple.copyOf(new double[0]);
     * // empty.min();   // throws NoSuchElementException
     * }</pre>
     *
     * @return the minimum double value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #max()
     * @see Math#min(double, double)
     */
    public double min() {
        final double[] arr = elements();
        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }
        double result = arr[0];
        for (int i = 1; i < arr.length; i++) {
            result = Math.min(result, arr[i]);
        }
        return result;
    }

    /**
     * Returns the maximum double value in this tuple.
     * <p>
     * This method finds and returns the largest double value among all elements
     * in the tuple. For tuples with a single element, returns that element.
     * </p>
     * <p>
     * Comparisons are performed pairwise with {@link Math#max(double, double)}, so any
     * {@code NaN} element causes the result to be {@code NaN}, and {@code +0.0} is
     * treated as larger than {@code -0.0}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(3.0, 1.0, 2.0);
     * double max = tuple.max();   // 3.0
     *
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * double maxPair = pair.max();   // 2.5
     *
     * // Positive infinity is the largest finite/infinite value
     * DoubleTuple.DoubleTuple3 inf = DoubleTuple.of(1.0, Double.POSITIVE_INFINITY, 3.0);
     * double maxInf = inf.max();   // Double.POSITIVE_INFINITY
     *
     * // Empty tuple throws NoSuchElementException
     * DoubleTuple<?> empty = DoubleTuple.copyOf(new double[0]);
     * // empty.max();   // throws NoSuchElementException
     * }</pre>
     *
     * @return the maximum double value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see Math#max(double, double)
     */
    public double max() {
        final double[] arr = elements();
        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }
        double result = arr[0];
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
     * For tuples with three or more elements, ordering is performed with
     * {@link Double#compare(double, double)} semantics, so {@code NaN} is treated as the
     * largest value (and equal to itself), and {@code -0.0} is treated as less than
     * {@code +0.0}. The two-element case is special: it returns
     * {@link Math#min(double, double) Math.min(_1, _2)}, which propagates {@code NaN}
     * (i.e. if either element is {@code NaN}, the result is {@code NaN}).
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Odd number of elements - returns middle value in sorted order
     * DoubleTuple.DoubleTuple3 tuple3 = DoubleTuple.of(30.0, 10.0, 20.0);
     * double median = tuple3.median();   // 20.0 (sorted: 10.0, 20.0, 30.0)
     *
     * // Even number of elements - returns lower middle value (index n/2 - 1 in sorted array)
     * DoubleTuple.DoubleTuple4 tuple4 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
     * double median2 = tuple4.median();   // 2.0
     *
     * // NaN sorts as the largest value (Double.compare semantics)
     * // so median of {1.0, NaN, 2.0} sorted = {1.0, 2.0, NaN} -> middle = 2.0
     * DoubleTuple.DoubleTuple3 nanTuple = DoubleTuple.of(1.0, Double.NaN, 2.0);
     * double medNaN = nanTuple.median();   // 2.0
     *
     * // Empty tuple throws NoSuchElementException
     * DoubleTuple<?> empty = DoubleTuple.copyOf(new double[0]);
     * // empty.median();   // throws NoSuchElementException
     * }</pre>
     *
     * @return the median double element in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see N#median(double...)
     */
    public double median() {
        final double[] arr = elements();
        if (arr.length == 0) {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }
        return N.median(arr);
    }

    /**
     * Returns the sum of all double values in this tuple.
     * <p>
     * For an empty tuple, returns {@code 0.0}. If any element is {@code NaN},
     * the result is {@code NaN}. Infinities follow standard IEEE-754 addition
     * rules (e.g. {@code +INF + -INF} produces {@code NaN}).
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * double sum = tuple.sum();   // 6.0
     *
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * double pairSum = pair.sum();   // 4.0
     *
     * // Empty tuple returns 0.0 - no exception
     * DoubleTuple<?> empty = DoubleTuple.copyOf(new double[0]);
     * double emptySum = empty.sum();   // 0.0
     *
     * // NaN element propagates
     * DoubleTuple.DoubleTuple2 nanTuple = DoubleTuple.of(1.0, Double.NaN);
     * double nanSum = nanTuple.sum();   // NaN
     * }</pre>
     *
     * @return the sum of all double values in this tuple, or {@code 0.0} if empty
     * @see #average()
     */
    public double sum() {
        return N.sum(elements());
    }

    /**
     * Returns the arithmetic mean of all double values in this tuple.
     * <p>
     * If any element is {@code NaN}, the result is {@code NaN}. Infinities
     * follow standard IEEE-754 rules.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * double avg = tuple.average();   // 2.0
     *
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.0, 2.0);
     * double avgPair = pair.average();   // 1.5
     *
     * // NaN element propagates to the result
     * DoubleTuple.DoubleTuple2 nanTuple = DoubleTuple.of(1.0, Double.NaN);
     * double nanAvg = nanTuple.average();   // NaN
     *
     * // Empty tuple throws NoSuchElementException
     * DoubleTuple<?> empty = DoubleTuple.copyOf(new double[0]);
     * // empty.average();   // throws NoSuchElementException
     * }</pre>
     *
     * @return the arithmetic mean of all double values in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #sum()
     */
    public double average() {
        final double[] arr = elements();
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
     * (1.0, 2.0, 3.0) becomes (3.0, 2.0, 1.0) when reversed.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * DoubleTuple.DoubleTuple3 reversed = tuple.reverse();   // (3.0, 2.0, 1.0)
     *
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * DoubleTuple.DoubleTuple2 reversedPair = pair.reverse();   // (2.5, 1.5)
     *
     * // reverse() returns a new object for non-empty tuples (the empty tuple returns itself)
     * DoubleTuple.DoubleTuple3 orig = DoubleTuple.of(1.0, 2.0, 3.0);
     * DoubleTuple.DoubleTuple3 rev = orig.reverse();
     * boolean same = (orig == rev);   // false
     *
     * // Single-element reverse is effectively the same value
     * DoubleTuple.DoubleTuple1 single = DoubleTuple.of(5.0);
     * DoubleTuple.DoubleTuple1 revSingle = single.reverse();
     * double val = revSingle._1;   // 5.0
     * }</pre>
     *
     * @return a tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified double value.
     * <p>
     * This method performs a linear search through all elements in the tuple to determine
     * if any element matches the specified value. Returns {@code true} if at least one
     * element equals the search value, {@code false} otherwise.
     * </p>
     * <p>
     * Comparisons follow {@link Double#compare(double, double)} semantics: {@code NaN} is
     * considered equal to {@code NaN}, and {@code +0.0} and {@code -0.0} are not considered equal.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * boolean hasTwo = tuple.contains(2.0);    // true
     * boolean hasFive = tuple.contains(5.0);   // false
     *
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * boolean has1_5 = pair.contains(1.5);   // true
     * boolean has3_5 = pair.contains(3.5);   // false
     *
     * // NaN is equal to NaN under Double.compare semantics
     * DoubleTuple.DoubleTuple2 nanTuple = DoubleTuple.of(1.0, Double.NaN);
     * boolean hasNaN = nanTuple.contains(Double.NaN);   // true
     *
     * // -0.0 and +0.0 are NOT equal under Double.compare semantics
     * DoubleTuple.DoubleTuple1 negZero = DoubleTuple.of(-0.0);
     * boolean hasNegZero = negZero.contains(-0.0);   // true
     * boolean hasPosZero = negZero.contains(0.0);    // false
     * }</pre>
     *
     * @param valueToFind the double value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     * @see Double#compare(double, double)
     */
    public abstract boolean contains(double valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * <p>
     * This method creates a defensive copy of the internal array. Changes to the
     * returned array do not affect the tuple because tuples are immutable.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * double[] array = tuple.toArray();   // [1.0, 2.0, 3.0]
     *
     * // Modifying the returned array does not affect the original tuple
     * array[0] = 5.0;
     * double first = tuple._1;   // still 1.0
     *
     * // Empty tuple returns a zero-length array
     * DoubleTuple<?> empty = DoubleTuple.copyOf(new double[0]);
     * double[] emptyArray = empty.toArray();
     * int len = emptyArray.length;   // 0
     *
     * // Successive calls return independent copies
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * double[] copy1 = pair.toArray();
     * double[] copy2 = pair.toArray();
     * boolean same = (copy1 == copy2);   // false
     * }</pre>
     *
     * @return a new double array containing all tuple elements
     * @see #toList()
     * @see #stream()
     */
    public double[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new DoubleList containing all elements of this tuple.
     * <p>
     * This method converts the tuple into a mutable DoubleList. The returned list is a new
     * instance, and modifications to it will not affect the original tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * DoubleList list = tuple.toList();
     * int size = list.size();       // 3
     * double first = list.get(0);   // 1.0
     *
     * // Mutating the list does not affect the original tuple
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * DoubleList pairList = pair.toList();
     * pairList.add(3.0);   // pairList now has 3 elements; tuple still has 2
     *
     * // Empty tuple produces an empty list
     * DoubleTuple<?> empty = DoubleTuple.copyOf(new double[0]);
     * int emptySize = empty.toList().size();   // 0
     * }</pre>
     *
     * @return a new DoubleList containing all tuple elements
     * @see #toArray()
     * @see #stream()
     */
    public DoubleList toList() {
        return DoubleList.of(elements().clone());
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
     * // Accumulate elements into an array
     * double[] acc = {0.0};
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * tuple.forEach(d -> acc[0] += d);
     * double total = acc[0];   // 6.0
     *
     * // Print each element in order
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * pair.forEach(d -> System.out.print(d + " "));   // prints "1.5 2.5 "
     *
     * // Empty tuple: consumer is never called
     * int[] count = {0};
     * DoubleTuple.copyOf(new double[0]).forEach(d -> count[0]++);   // action not invoked (empty tuple)
     * int callCount = count[0];                                     // 0
     *
     * // null action throws IllegalArgumentException
     * // tuple.forEach(null);   // throws IllegalArgumentException
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the action to be performed for each element; must not be {@code null}
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> action) throws E {
        N.checkArgNotNull(action);

        for (final double element : elements()) {
            action.accept(element);
        }
    }

    /**
     * Returns a DoubleStream of all elements in this tuple.
     * <p>
     * This method creates a sequential DoubleStream with all elements from the tuple.
     * The stream provides a functional programming interface for processing the tuple elements
     * through operations like filter, map, and reduce.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * double sum = tuple.stream().sum();   // 6.0
     *
     * // Filter elements greater than a threshold
     * DoubleTuple.DoubleTuple3 t = DoubleTuple.of(1.0, 2.0, 3.0);
     * long count = t.stream().filter(d -> d > 1.5).count();   // 2
     *
     * // Empty tuple produces an empty stream
     * DoubleTuple<?> empty = DoubleTuple.copyOf(new double[0]);
     * long emptyCount = empty.stream().count();   // 0
     *
     * // Map and collect
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * double[] doubled = pair.stream().map(d -> d * 2).toArray();   // [3.0, 5.0]
     * }</pre>
     *
     * @return a DoubleStream containing all tuple elements
     * @see #toArray()
     * @see #toList()
     */
    public DoubleStream stream() {
        return DoubleStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * <p>
     * The hash code is computed from the contents of the tuple's elements using
     * {@link Double#hashCode(double)} for each element (which is bit-pattern based,
     * so {@code +0.0} and {@code -0.0} hash differently and {@code NaN} hashes to a
     * single canonical value). Tuples with identical elements in the same order
     * have the same hash code. This implementation is consistent with
     * {@link #equals(Object)}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Equal tuples always have the same hash code
     * DoubleTuple.DoubleTuple3 a = DoubleTuple.of(1.0, 2.0, 3.0);
     * DoubleTuple.DoubleTuple3 b = DoubleTuple.of(1.0, 2.0, 3.0);
     * boolean sameHash = (a.hashCode() == b.hashCode());   // true
     *
     * // Tuples with different element order have different hash codes (generally)
     * DoubleTuple.DoubleTuple3 c = DoubleTuple.of(3.0, 2.0, 1.0);
     * boolean diffHash = (a.hashCode() != c.hashCode());   // true in practice
     *
     * // +0.0 and -0.0 are bit-pattern distinct, so their hashes differ
     * DoubleTuple.DoubleTuple1 pos = DoubleTuple.of(0.0);
     * DoubleTuple.DoubleTuple1 neg = DoubleTuple.of(-0.0);
     * boolean differentHash = (pos.hashCode() != neg.hashCode());   // true
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
     * <li>They are of the exact same runtime class (e.g., both {@code DoubleTuple.DoubleTuple2});
     *     a {@code DoubleTuple1} is never equal to a {@code DoubleTuple2}, regardless of element values</li>
     * <li>They contain the same elements in the same order</li>
     * </ul>
     * <p>
     * Element comparisons follow {@link Double#compare(double, double)} semantics:
     * {@code NaN} is treated as equal to {@code NaN}, and {@code +0.0} is not equal to {@code -0.0}.
     * This method adheres to the general contract of {@link Object#equals(Object)}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Two tuples with identical elements are equal
     * DoubleTuple.DoubleTuple3 a = DoubleTuple.of(1.0, 2.0, 3.0);
     * DoubleTuple.DoubleTuple3 b = DoubleTuple.of(1.0, 2.0, 3.0);
     * boolean equal = a.equals(b);   // true
     *
     * // Different arity -> not equal even if element values overlap
     * DoubleTuple.DoubleTuple2 c = DoubleTuple.of(1.0, 2.0);
     * boolean diffArity = a.equals(c);   // false
     *
     * // null is never equal
     * boolean equalsNull = a.equals(null);   // false
     *
     * // NaN == NaN under these semantics
     * DoubleTuple.DoubleTuple1 nan1 = DoubleTuple.of(Double.NaN);
     * DoubleTuple.DoubleTuple1 nan2 = DoubleTuple.of(Double.NaN);
     * boolean nanEqual = nan1.equals(nan2);   // true
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
            return N.equals(elements(), ((DoubleTuple<TP>) obj).elements());
        }
    }

    /**
     * Returns the internal array containing all double elements in this tuple.
     * <p>
     * <b>Warning:</b> The returned array is the internal representation of this tuple.
     * Modifying the returned array will compromise the immutability of this tuple.
     * Use {@link #toArray()} instead if you need an array that can be safely modified.
     * </p>
     *
     * @return the internal array of double elements
     */
    protected abstract double[] elements();

    /**
     * An empty DoubleTuple containing no elements (arity 0).
     * <p>
     * This package-private class is exposed only through the base {@code DoubleTuple} type
     * via the singleton instance returned by {@link #copyOf(double[])} when invoked with a
     * {@code null} or zero-length array. {@link #sum()} returns 0.0, while {@link #min()},
     * {@link #max()}, {@link #median()}, and {@link #average()} all throw {@link java.util.NoSuchElementException}.
     * </p>
     */
    static final class DoubleTuple0 extends DoubleTuple<DoubleTuple0> {

        private static final DoubleTuple0 EMPTY = new DoubleTuple0();

        DoubleTuple0() {
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
        public double min() {
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
        public double max() {
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
        public double median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the sum of all elements in this tuple.
         * For an empty tuple, the sum is {@code 0.0}.
         *
         * @return {@code 0.0}
         */
        @Override
        public double sum() {
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
         * @return this {@code DoubleTuple0} instance
         */
        @Override
        public DoubleTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified double value.
         * Since this tuple is empty, this method always returns {@code false}.
         *
         * @param valueToFind the double value to search for
         * @return {@code false} always, because the tuple is empty
         */
        @Override
        public boolean contains(final double valueToFind) {
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
         * Returns the shared empty double array.
         *
         * @return an empty double array
         */
        @Override
        protected double[] elements() {
            return N.EMPTY_DOUBLE_ARRAY;
        }
    }

    /**
     * A DoubleTuple containing exactly one double value.
     * <p>
     * This class provides direct access to the single element through the public final field {@code _1}.
     * For single-element tuples, all statistical operations (min, max, median, sum, average) return
     * or are based on that single element.
     * </p>
     */
    public static final class DoubleTuple1 extends DoubleTuple<DoubleTuple1> {

        /** The single double value in this tuple. */
        public final double _1;

        DoubleTuple1() {
            this(0);
        }

        DoubleTuple1(final double _1) {
            this._1 = _1;
        }

        /**
         * Returns the number of elements in this tuple, which is always 1.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple1 t = DoubleTuple.of(3.14);
         * int arity = t.arity();   // 1
         *
         * // arity is fixed regardless of element value
         * DoubleTuple.DoubleTuple1 zero = DoubleTuple.of(0.0);
         * int arityZero = zero.arity();   // 1
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
         * DoubleTuple.DoubleTuple1 t = DoubleTuple.of(3.14);
         * double min = t.min();   // 3.14
         *
         * // Negative value
         * DoubleTuple.DoubleTuple1 neg = DoubleTuple.of(-5.0);
         * double minNeg = neg.min();   // -5.0
         *
         * // NaN element: min() returns NaN
         * DoubleTuple.DoubleTuple1 nan = DoubleTuple.of(Double.NaN);
         * double minNaN = nan.min();   // NaN
         * }</pre>
         *
         * @return the value of {@code _1}
         */
        @Override
        public double min() {
            return _1;
        }

        /**
         * Returns the maximum value in this tuple, which is the single element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple1 t = DoubleTuple.of(3.14);
         * double max = t.max();   // 3.14
         *
         * // Positive infinity
         * DoubleTuple.DoubleTuple1 inf = DoubleTuple.of(Double.POSITIVE_INFINITY);
         * double maxInf = inf.max();   // Double.POSITIVE_INFINITY
         *
         * // NaN element: max() returns NaN
         * DoubleTuple.DoubleTuple1 nan = DoubleTuple.of(Double.NaN);
         * double maxNaN = nan.max();   // NaN
         * }</pre>
         *
         * @return the value of {@code _1}
         */
        @Override
        public double max() {
            return _1;
        }

        /**
         * Returns the median value in this tuple, which is the single element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple1 t = DoubleTuple.of(7.5);
         * double median = t.median();   // 7.5
         *
         * // Zero is a valid element
         * DoubleTuple.DoubleTuple1 zero = DoubleTuple.of(0.0);
         * double medianZero = zero.median();   // 0.0
         *
         * // NaN element: median() returns NaN
         * DoubleTuple.DoubleTuple1 nan = DoubleTuple.of(Double.NaN);
         * double medianNaN = nan.median();   // NaN
         * }</pre>
         *
         * @return the value of {@code _1}
         */
        @Override
        public double median() {
            return _1;
        }

        /**
         * Returns the sum of elements in this tuple, which is the single element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple1 t = DoubleTuple.of(3.5);
         * double s = t.sum();   // 3.5
         *
         * DoubleTuple.DoubleTuple1 neg = DoubleTuple.of(-1.5);
         * double sn = neg.sum();   // -1.5
         *
         * DoubleTuple.DoubleTuple1 nan = DoubleTuple.of(Double.NaN);
         * double snan = nan.sum();   // NaN
         *
         * DoubleTuple.DoubleTuple1 inf = DoubleTuple.of(Double.POSITIVE_INFINITY);
         * double sinf = inf.sum();   // Infinity
         * }</pre>
         *
         * @return the value of {@code _1}
         */
        @Override
        public double sum() {
            return _1;
        }

        /**
         * Returns the average of elements in this tuple, which is the single element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple1 t = DoubleTuple.of(3.5);
         * double avg = t.average();   // 3.5
         *
         * DoubleTuple.DoubleTuple1 neg = DoubleTuple.of(-1.5);
         * double avgNeg = neg.average();   // -1.5
         *
         * DoubleTuple.DoubleTuple1 nan = DoubleTuple.of(Double.NaN);
         * double avgNan = nan.average();   // NaN
         *
         * DoubleTuple.DoubleTuple1 inf = DoubleTuple.of(Double.POSITIVE_INFINITY);
         * double avgInf = inf.average();   // Infinity
         * }</pre>
         *
         * @return the value of {@code _1}
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
         * DoubleTuple.DoubleTuple1 tuple = DoubleTuple.of(5.0);
         * DoubleTuple.DoubleTuple1 reversed = tuple.reverse();   // (5.0)
         *
         * // reverse() always allocates a fresh instance, never returns this
         * DoubleTuple.DoubleTuple1 t = DoubleTuple.of(5.0);
         * boolean same = (t == t.reverse());   // false
         *
         * // negative value preserved
         * DoubleTuple.DoubleTuple1 neg = DoubleTuple.of(-3.0).reverse();   // (-3.0)
         *
         * // NaN and Infinity are preserved
         * DoubleTuple.DoubleTuple1 nanTuple = DoubleTuple.of(Double.NaN).reverse();   // (NaN)
         * }</pre>
         *
         * @return a new {@code DoubleTuple1} with the same value as this tuple
         */
        @Override
        public DoubleTuple1 reverse() {
            return new DoubleTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified double value.
         * Uses {@link Double#compare(double, double)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0} does not match {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple1 t = DoubleTuple.of(5.0);
         * boolean found = t.contains(5.0);    // true
         * boolean miss  = t.contains(3.0);    // false
         *
         * // NaN matches NaN (Double.compare semantics)
         * boolean hasNaN = DoubleTuple.of(Double.NaN).contains(Double.NaN);   // true
         *
         * // -0.0 and +0.0 are distinct (Double.compare(-0.0, 0.0) != 0)
         * boolean negZero = DoubleTuple.of(-0.0).contains(0.0);   // false
         * }</pre>
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if {@code _1} equals {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind);
        }

        /**
         * Returns a hash code for this tuple based on its single element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple1 t = DoubleTuple.of(5.0);
         * int h = t.hashCode();   // Double.hashCode(5.0)
         *
         * // equal tuples have equal hash codes
         * boolean sameHash = DoubleTuple.of(5.0).hashCode() == DoubleTuple.of(5.0).hashCode();   // true
         *
         * // NaN has a consistent hash code
         * int nanHash = DoubleTuple.of(Double.NaN).hashCode();   // Double.hashCode(Double.NaN)
         *
         * // negative value
         * int negHash = DoubleTuple.of(-1.5).hashCode();   // Double.hashCode(-1.5)
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return Double.hashCode(_1);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple1 a = DoubleTuple.of(5.0);
         * DoubleTuple.DoubleTuple1 b = DoubleTuple.of(5.0);
         * boolean eq  = a.equals(b);                     // true
         * boolean neq = a.equals(DoubleTuple.of(3.0));   // false
         *
         * // same instance is always equal to itself
         * boolean self = a.equals(a);   // true
         *
         * // NaN is equal to NaN (Double.compare semantics)
         * boolean nanEq = DoubleTuple.of(Double.NaN).equals(DoubleTuple.of(Double.NaN));   // true
         *
         * // null and different types are never equal
         * boolean nullEq = a.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple.DoubleTuple1 with equal value
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple1 other)) {
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
         * String s1 = DoubleTuple.of(5.0).toString();    // "(5.0)"
         * String s2 = DoubleTuple.of(-1.5).toString();   // "(-1.5)"
         *
         * // NaN and Infinity are represented using Java's standard double string form
         * String sNaN = DoubleTuple.of(Double.NaN).toString();                // "(NaN)"
         * String sInf = DoubleTuple.of(Double.POSITIVE_INFINITY).toString();  // "(Infinity)"
         * }</pre>
         *
         * @return "(value)" where value is _1
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
        }

        /**
         * Returns the internal array of double elements.
         * The array is lazily initialized on first access.
         *
         * @return a double array containing all elements of this tuple
         */
        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A tuple containing exactly two double values.
     * The values are accessible through the public final fields {@code _1} and {@code _2}.
     *
     * <p>In addition to the operations inherited from {@link DoubleTuple}, this class provides
     * functional helpers for working with pairs:</p>
     * <ul>
     *   <li>{@link #accept(Throwables.DoubleBiConsumer)} - consume both values</li>
     *   <li>{@link #map(Throwables.DoubleBiFunction)} - transform the pair to a single value</li>
     *   <li>{@link #filter(Throwables.DoubleBiPredicate)} - conditionally wrap in {@link Optional}</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple2 t = DoubleTuple.of(1.5, 2.5);
     * double first = t._1;        // 1.5
     * double second = t._2;       // 2.5
     * double sum = t.sum();       // 4.0
     * double min = t.min();       // 1.5
     * double max = t.max();       // 2.5
     * double avg = t.average();   // 2.0
     * }</pre>
     *
     */
    public static final class DoubleTuple2 extends DoubleTuple<DoubleTuple2> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;

        DoubleTuple2() {
            this(0, 0);
        }

        DoubleTuple2(final double _1, final double _2) {
            this._1 = _1;
            this._2 = _2;
        }

        /**
         * Returns the number of elements in this tuple, which is always 2.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple2 t = DoubleTuple.of(1.0, 2.0);
         * int n = t.arity();   // 2
         *
         * // arity is independent of the stored values
         * int n2 = DoubleTuple.of(Double.NaN, Double.POSITIVE_INFINITY).arity();   // 2
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
         * double m1 = DoubleTuple.of(1.0, 2.0).min();    // 1.0
         * double m2 = DoubleTuple.of(2.0, 1.0).min();    // 1.0
         *
         * // negative values
         * double m3 = DoubleTuple.of(-3.0, -1.0).min();  // -3.0
         *
         * // NaN propagates
         * double mNaN = DoubleTuple.of(Double.NaN, 2.0).min();   // NaN
         *
         * // Infinity
         * double mInf = DoubleTuple.of(Double.NEGATIVE_INFINITY, 0.0).min();   // -Infinity
         * }</pre>
         *
         * @return the smaller of {@code _1} and {@code _2}
         */
        @Override
        public double min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum value among the two elements.
         * If either element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * double m1 = DoubleTuple.of(1.0, 2.0).max();    // 2.0
         * double m2 = DoubleTuple.of(2.0, 1.0).max();    // 2.0
         *
         * // negative values
         * double m3 = DoubleTuple.of(-3.0, -1.0).max();  // -1.0
         *
         * // NaN propagates
         * double mNaN = DoubleTuple.of(Double.NaN, 2.0).max();   // NaN
         *
         * // Infinity
         * double mInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 0.0).max();   // Infinity
         * }</pre>
         *
         * @return the larger of {@code _1} and {@code _2}
         */
        @Override
        public double max() {
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
         * double med1 = DoubleTuple.of(3.0, 4.0).median();   // 3.0  (lower of the two)
         * double med2 = DoubleTuple.of(4.0, 3.0).median();   // 3.0  (order does not matter)
         *
         * // negative values - still returns the lesser
         * double med3 = DoubleTuple.of(-3.0, -1.0).median();   // -3.0
         *
         * // NaN propagates (Math.min semantics)
         * double medNaN = DoubleTuple.of(3.0, Double.NaN).median();   // NaN
         *
         * // equal values
         * double medEq = DoubleTuple.of(2.5, 2.5).median();   // 2.5
         * }</pre>
         *
         * @return the smaller of {@code _1} and {@code _2}, or {@code NaN} if either is {@code NaN}
         */
        @Override
        public double median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of the two elements.
         * If either element is {@code NaN} the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * double s1 = DoubleTuple.of(3.0, 4.0).sum();    // 7.0
         * double s2 = DoubleTuple.of(-1.0, -3.0).sum();  // -4.0
         *
         * // NaN propagates
         * double sNaN = DoubleTuple.of(Double.NaN, 1.0).sum();   // NaN
         *
         * // overflow produces Infinity
         * double sInf = DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE).sum();   // Infinity
         * }</pre>
         *
         * @return the sum of {@code _1} and {@code _2}
         */
        @Override
        public double sum() {
            return N.sum(_1, _2);
        }

        /**
         * Returns the arithmetic mean of the two elements.
         * If either element is {@code NaN} the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * double avg1 = DoubleTuple.of(3.0, 4.0).average();    // 3.5
         * double avg2 = DoubleTuple.of(-1.0, -3.0).average();  // -2.0
         *
         * // NaN propagates
         * double avgNaN = DoubleTuple.of(Double.NaN, 1.0).average();   // NaN
         *
         * // two Infinities of the same sign
         * double avgInf = DoubleTuple.of(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).average();   // Infinity
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
         * DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
         * DoubleTuple.DoubleTuple2 reversed = tuple.reverse();   // (4.0, 3.0)
         *
         * // negative values
         * DoubleTuple.DoubleTuple2 r2 = DoubleTuple.of(-1.5, -2.5).reverse();   // (-2.5, -1.5)
         *
         * // NaN is preserved in the reversed position
         * DoubleTuple.DoubleTuple2 r3 = DoubleTuple.of(Double.NaN, 1.0).reverse();   // (1.0, NaN)
         *
         * // Infinity is preserved in the reversed position
         * DoubleTuple.DoubleTuple2 r4 = DoubleTuple.of(Double.POSITIVE_INFINITY, 0.0).reverse();   // (0.0, Infinity)
         * }</pre>
         *
         * @return a new DoubleTuple.DoubleTuple2 with (_2, _1)
         */
        @Override
        public DoubleTuple2 reverse() {
            return new DoubleTuple2(_2, _1);
        }

        /**
         * Checks if this tuple contains the specified double value.
         * Comparisons use {@link Double#compare(double, double)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0} does not match {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple2 t = DoubleTuple.of(3.0, 4.0);
         * boolean f1 = t.contains(3.0);   // true
         * boolean f2 = t.contains(4.0);   // true
         * boolean f3 = t.contains(5.0);   // false
         *
         * // NaN matches NaN (Double.compare semantics)
         * boolean hasNaN = DoubleTuple.of(Double.NaN, 1.0).contains(Double.NaN);   // true
         *
         * // -0.0 and +0.0 are distinct
         * boolean negZero = DoubleTuple.of(-0.0, 1.0).contains(0.0);   // false
         * }</pre>
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if {@code _1} or {@code _2} equals {@code valueToFind},
         *         {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple2 t = DoubleTuple.of(3.0, 4.0);
         * double[] sum = {0};
         * t.forEach(v -> sum[0] += v);   // sum[0] == 7.0 after the call
         *
         * // elements are visited in declaration order: _1 first, then _2
         * java.util.List<Double> list = new java.util.ArrayList<>();
         * DoubleTuple.of(-1.5, 2.5).forEach(list::add);   // list == [-1.5, 2.5]
         *
         * // NaN is passed through as-is
         * DoubleTuple.of(Double.NaN, 1.0).forEach(v -> assertTrue(Double.isNaN(v) || v == 1.0));
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> action) throws E {
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
         * DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
         * tuple.accept((a, b) -> System.out.println(a + " + " + b + " = " + (a + b)));
         * // Prints: 3.0 + 4.0 = 7.0
         *
         * DoubleTuple.DoubleTuple2 coordinates = DoubleTuple.of(10.5, 20.3);
         * coordinates.accept((x, y) -> System.out.printf("Point: (%.1f, %.1f)%n", x, y));  // Prints: Point: (10.5, 20.3)
         *
         * // accumulating a result via a mutable container
         * double[] product = {1.0};
         * DoubleTuple.of(-2.0, 3.5).accept((a, b) -> product[0] = a * b);   // product[0] == -7.0
         *
         * // NaN is passed to the consumer unchanged
         * boolean[] sawNaN = {false};
         * DoubleTuple.of(Double.NaN, 1.0).accept((a, b) -> sawNaN[0] = Double.isNaN(a));   // sawNaN[0] == true
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the bi-consumer to perform on the two elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #map(Throwables.DoubleBiFunction)
         * @see #filter(Throwables.DoubleBiPredicate)
         */
        public <E extends Exception> void accept(final Throwables.DoubleBiConsumer<E> action) throws E {
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
         * DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
         * double product = tuple.map((a, b) -> a * b);   // 12.0
         *
         * DoubleTuple.DoubleTuple2 dimensions = DoubleTuple.of(5.0, 3.0);
         * String description = dimensions.map((w, h) -> String.format("%.0f x %.0f", w, h));
         * // Returns: "5 x 3"
         *
         * DoubleTuple.DoubleTuple2 point = DoubleTuple.of(3.0, 4.0);
         * Double distance = point.map((x, y) -> Math.sqrt(x * x + y * y));   // 5.0
         *
         * // mapper may return null
         * String nullResult = DoubleTuple.of(1.0, 2.0).map((a, b) -> null);   // null
         *
         * // negative operands
         * double diff = DoubleTuple.of(-3.0, 1.5).map((a, b) -> a + b);   // -1.5
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the bi-function to apply to the two elements, must not be {@code null}
         * @return the result of applying the mapper to {@code _1} and {@code _2} (may be {@code null} if the mapper returns {@code null})
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.DoubleBiConsumer)
         * @see #filter(Throwables.DoubleBiPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.DoubleBiFunction<U, E> mapper) throws E {
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
         * DoubleTuple.DoubleTuple2 tuple = DoubleTuple.of(3.0, 4.0);
         * u.Optional<DoubleTuple.DoubleTuple2> result = tuple.filter((a, b) -> a + b > 5);
         * // Returns: Optional containing tuple (since 3.0 + 4.0 = 7.0 > 5)
         *
         * DoubleTuple.DoubleTuple2 small = DoubleTuple.of(1.0, 2.0);
         * u.Optional<DoubleTuple.DoubleTuple2> empty = small.filter((a, b) -> a + b > 10);
         * // Returns: Optional.empty() (since 1.0 + 2.0 = 3.0 is not > 10)
         *
         * DoubleTuple.DoubleTuple2 point = DoubleTuple.of(3.0, 4.0);
         * u.Optional<DoubleTuple.DoubleTuple2> inRange = point.filter((x, y) -> x >= 0 && y >= 0);
         * // Returns: Optional containing point (both coordinates are positive)
         *
         * // negative values: predicate evaluates correctly
         * u.Optional<DoubleTuple.DoubleTuple2> neg = DoubleTuple.of(-1.0, -2.0).filter((a, b) -> a < 0 && b < 0);
         * // Returns: Optional containing (-1.0, -2.0)
         *
         * // when predicate always returns false, result is empty
         * u.Optional<DoubleTuple.DoubleTuple2> none = DoubleTuple.of(1.0, 2.0).filter((a, b) -> false);
         * // Returns: Optional.empty()
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the bi-predicate to test the two elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception
         * @see #accept(Throwables.DoubleBiConsumer)
         * @see #map(Throwables.DoubleBiFunction)
         */
        public <E extends Exception> Optional<DoubleTuple2> filter(final Throwables.DoubleBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on both elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * int h = DoubleTuple.of(3.0, 4.0).hashCode();   // 31 * Double.hashCode(3.0) + Double.hashCode(4.0)
         *
         * // equal tuples always have equal hash codes
         * boolean same = DoubleTuple.of(3.0, 4.0).hashCode() == DoubleTuple.of(3.0, 4.0).hashCode();   // true
         *
         * // order matters: (3.0, 4.0) and (4.0, 3.0) have different hash codes
         * boolean diff = DoubleTuple.of(3.0, 4.0).hashCode() != DoubleTuple.of(4.0, 3.0).hashCode();   // true
         *
         * // NaN has a consistent hash code
         * int nanHash = DoubleTuple.of(Double.NaN, 1.0).hashCode();   // 31 * Double.hashCode(Double.NaN) + Double.hashCode(1.0)
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            return 31 * result + Double.hashCode(_2);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple2 a = DoubleTuple.of(3.0, 4.0);
         * DoubleTuple.DoubleTuple2 b = DoubleTuple.of(3.0, 4.0);
         * boolean eq  = a.equals(b);   // true
         *
         * // order matters
         * boolean neq = a.equals(DoubleTuple.of(4.0, 3.0));   // false
         *
         * // NaN equals NaN (Double.compare semantics)
         * boolean nanEq = DoubleTuple.of(Double.NaN, 1.0).equals(DoubleTuple.of(Double.NaN, 1.0));   // true
         *
         * // null and different types are never equal
         * boolean nullEq = a.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple.DoubleTuple2 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple2 other)) {
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
         * String s1 = DoubleTuple.of(3.0, 4.0).toString();    // "(3.0, 4.0)"
         * String s2 = DoubleTuple.of(-1.5, 2.5).toString();   // "(-1.5, 2.5)"
         *
         * // NaN and Infinity use Java's standard double string forms
         * String sNaN = DoubleTuple.of(Double.NaN, 1.0).toString();                                      // "(NaN, 1.0)"
         * String sInf = DoubleTuple.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).toString();   // "(Infinity, -Infinity)"
         * }</pre>
         *
         * @return "(_1, _2)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
        }

        /**
         * Returns the internal array of double elements.
         * The array is lazily initialized on first access.
         *
         * @return a double array containing all elements of this tuple
         */
        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly three double values.
     * <p>
     * This class provides direct access to elements through public final fields {@code _1}, {@code _2}, and {@code _3}.
     * DoubleTuple.DoubleTuple3 offers additional functional methods like {@link #accept(Throwables.DoubleTriConsumer)},
     * {@link #map(Throwables.DoubleTriFunction)}, and {@link #filter(Throwables.DoubleTriPredicate)} that
     * operate on all three elements simultaneously.
     * </p>
     */
    public static final class DoubleTuple3 extends DoubleTuple<DoubleTuple3> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;

        DoubleTuple3() {
            this(0, 0, 0);
        }

        DoubleTuple3(final double _1, final double _2, final double _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        /**
         * Returns the number of elements in this tuple, which is always 3.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple3 t = DoubleTuple.of(1.0, 2.0, 3.0);
         * int n = t.arity();   // 3
         *
         * // arity is independent of the stored values
         * int n2 = DoubleTuple.of(Double.NaN, Double.POSITIVE_INFINITY, -1.0).arity();   // 3
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
         * double m1 = DoubleTuple.of(1.0, 2.0, 3.0).min();     // 1.0
         * double m2 = DoubleTuple.of(-5.0, 0.0, 3.0).min();    // -5.0
         *
         * // NaN propagates
         * double mNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0).min();   // NaN
         *
         * // Infinity
         * double mInf = DoubleTuple.of(Double.NEGATIVE_INFINITY, 0.0, 1.0).min();   // -Infinity
         * }</pre>
         *
         * @return the smallest of {@code _1}, {@code _2}, and {@code _3}
         */
        @Override
        public double min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum value among the three elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * double m1 = DoubleTuple.of(1.0, 2.0, 3.0).max();     // 3.0
         * double m2 = DoubleTuple.of(-5.0, 0.0, 3.0).max();    // 3.0
         *
         * // NaN propagates
         * double mNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0).max();   // NaN
         *
         * // Infinity
         * double mInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 0.0, 1.0).max();   // Infinity
         * }</pre>
         *
         * @return the largest of {@code _1}, {@code _2}, and {@code _3}
         */
        @Override
        public double max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median value of the three elements.
         * Comparison uses {@link Double#compare(double, double)} semantics, so
         * {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * double med1 = DoubleTuple.of(1.0, 2.0, 3.0).median();   // 2.0
         * double med2 = DoubleTuple.of(-5.0, 0.0, 3.0).median();  // 0.0
         *
         * // NaN sorts as largest (Double.compare semantics), so it does not become the median
         * // when the other two bracket it; median of (1.0, 3.0, NaN) is 3.0
         * double medNaN = DoubleTuple.of(1.0, 3.0, Double.NaN).median();   // 3.0
         *
         * // duplicate middle value
         * double medDup = DoubleTuple.of(2.0, 2.0, 5.0).median();   // 2.0
         * }</pre>
         *
         * @return the middle value when the three elements are sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of the three elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * double s1 = DoubleTuple.of(1.0, 2.0, 3.0).sum();    // 6.0
         * double s2 = DoubleTuple.of(-1.0, -1.0, -1.0).sum(); // -3.0
         *
         * // NaN propagates
         * double sNaN = DoubleTuple.of(Double.NaN, 1.0, 2.0).sum();   // NaN
         *
         * // overflow produces Infinity
         * double sInf = DoubleTuple.of(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE).sum();   // Infinity
         * }</pre>
         *
         * @return the sum of {@code _1}, {@code _2}, and {@code _3}
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3);
        }

        /**
         * Returns the arithmetic mean of the three elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * double avg1 = DoubleTuple.of(1.0, 2.0, 3.0).average();    // 2.0
         * double avg2 = DoubleTuple.of(-1.0, 0.0, 1.0).average();   // 0.0
         *
         * // NaN propagates
         * double avgNaN = DoubleTuple.of(Double.NaN, 1.0, 2.0).average();   // NaN
         *
         * // all-negative
         * double avgNeg = DoubleTuple.of(-1.0, -1.0, -1.0).average();   // -1.0
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
         * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
         * DoubleTuple.DoubleTuple3 reversed = tuple.reverse();   // (3.0, 2.0, 1.0)
         *
         * // negative values preserved
         * DoubleTuple.DoubleTuple3 r2 = DoubleTuple.of(-1.0, 0.0, 1.0).reverse();   // (1.0, 0.0, -1.0)
         *
         * // NaN preserved in reversed position
         * DoubleTuple.DoubleTuple3 r3 = DoubleTuple.of(Double.NaN, 1.0, 2.0).reverse();   // (2.0, 1.0, NaN)
         *
         * // Infinity preserved in reversed position
         * DoubleTuple.DoubleTuple3 r4 = DoubleTuple.of(Double.POSITIVE_INFINITY, 0.0, -1.0).reverse();   // (-1.0, 0.0, Infinity)
         * }</pre>
         *
         * @return a new DoubleTuple.DoubleTuple3 with (_3, _2, _1)
         */
        @Override
        public DoubleTuple3 reverse() {
            return new DoubleTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified double value.
         * Comparisons use {@link Double#compare(double, double)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0} does not match {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple3 t = DoubleTuple.of(1.0, 2.0, 3.0);
         * boolean f1 = t.contains(2.0);   // true
         * boolean f2 = t.contains(4.0);   // false
         *
         * // NaN matches NaN (Double.compare semantics)
         * boolean hasNaN = DoubleTuple.of(Double.NaN, 1.0, 2.0).contains(Double.NaN);   // true
         *
         * // -0.0 and +0.0 are distinct
         * boolean negZero = DoubleTuple.of(-0.0, 1.0, 2.0).contains(0.0);   // false
         * }</pre>
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if any of {@code _1}, {@code _2}, {@code _3} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple3 t = DoubleTuple.of(1.0, 2.0, 3.0);
         * double[] sum = {0};
         * t.forEach(v -> sum[0] += v);   // sum[0] == 6.0 after the call
         *
         * // elements are visited in declaration order: _1, _2, _3
         * java.util.List<Double> list = new java.util.ArrayList<>();
         * DoubleTuple.of(-1.5, 0.0, 2.5).forEach(list::add);   // list == [-1.5, 0.0, 2.5]
         *
         * // NaN is passed through as-is
         * int[] count = {0};
         * DoubleTuple.of(Double.NaN, 1.0, 2.0).forEach(v -> count[0]++);   // count[0] == 3
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> action) throws E {
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
         * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
         * tuple.accept((a, b, c) -> System.out.println("Sum: " + (a + b + c)));  // Prints: Sum: 6.0
         *
         * DoubleTuple.DoubleTuple3 dimensions = DoubleTuple.of(5.0, 3.0, 2.0);
         * dimensions.accept((l, w, h) -> System.out.printf("Volume: %.1f%n", l * w * h));  // Prints: Volume: 30.0
         *
         * DoubleTuple.DoubleTuple3 rgb = DoubleTuple.of(0.5, 0.7, 0.3);
         * rgb.accept((r, g, b) -> System.out.printf("Color: RGB(%.1f, %.1f, %.1f)%n", r, g, b));  // Prints: Color: RGB(0.5, 0.7, 0.3)
         *
         * // accumulating into a mutable container; negative values
         * double[] result = {0};
         * DoubleTuple.of(-1.0, -2.0, -3.0).accept((a, b, c) -> result[0] = a + b + c);   // result[0] == -6.0
         *
         * // detecting NaN in any position
         * boolean[] hasNaN = {false};
         * DoubleTuple.of(Double.NaN, 1.0, 2.0).accept((a, b, c) -> hasNaN[0] = Double.isNaN(a));   // hasNaN[0] == true
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the tri-consumer to perform on the three elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #map(Throwables.DoubleTriFunction)
         * @see #filter(Throwables.DoubleTriPredicate)
         */
        public <E extends Exception> void accept(final Throwables.DoubleTriConsumer<E> action) throws E {
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
         * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
         * double product = tuple.map((a, b, c) -> a * b * c);   // 6.0
         *
         * DoubleTuple.DoubleTuple3 dimensions = DoubleTuple.of(5.0, 3.0, 2.0);
         * String description = dimensions.map((l, w, h) ->
         *     String.format("Box: %.0f x %.0f x %.0f", l, w, h));
         * // Returns: "Box: 5 x 3 x 2"
         *
         * DoubleTuple.DoubleTuple3 point = DoubleTuple.of(1.0, 2.0, 2.0);
         * Double distance = point.map((x, y, z) -> Math.sqrt(x*x + y*y + z*z));   // 3.0
         *
         * // mapper may return null
         * String nullResult = DoubleTuple.of(1.0, 2.0, 3.0).map((a, b, c) -> null);   // null
         *
         * // negative operands
         * double sum = DoubleTuple.of(-1.0, -2.0, -3.0).map((a, b, c) -> a + b + c);   // -6.0
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the tri-function to apply to the three elements, must not be {@code null}
         * @return the result of applying the mapper to {@code _1}, {@code _2}, and {@code _3} (may be {@code null} if the mapper returns {@code null})
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.DoubleTriConsumer)
         * @see #filter(Throwables.DoubleTriPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.DoubleTriFunction<U, E> mapper) throws E {
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
         * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
         * u.Optional<DoubleTuple.DoubleTuple3> result = tuple.filter((a, b, c) -> a + b + c > 5);
         * // Returns: Optional containing tuple (since 1.0 + 2.0 + 3.0 = 6.0 > 5)
         *
         * DoubleTuple.DoubleTuple3 small = DoubleTuple.of(1.0, 1.0, 1.0);
         * u.Optional<DoubleTuple.DoubleTuple3> empty = small.filter((a, b, c) -> a + b + c > 10);
         * // Returns: Optional.empty() (since 1.0 + 1.0 + 1.0 = 3.0 is not > 10)
         *
         * DoubleTuple.DoubleTuple3 dimensions = DoubleTuple.of(5.0, 3.0, 2.0);
         * u.Optional<DoubleTuple.DoubleTuple3> valid = dimensions.filter((l, w, h) -> l > 0 && w > 0 && h > 0);
         * // Returns: Optional containing dimensions (all values are positive)
         *
         * // negative values: predicate evaluates correctly
         * u.Optional<DoubleTuple.DoubleTuple3> neg = DoubleTuple.of(-1.0, -2.0, -3.0).filter((a, b, c) -> a < 0);
         * // Returns: Optional containing (-1.0, -2.0, -3.0)
         *
         * // when predicate always returns false, result is empty
         * u.Optional<DoubleTuple.DoubleTuple3> none = DoubleTuple.of(1.0, 2.0, 3.0).filter((a, b, c) -> false);
         * // Returns: Optional.empty()
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the tri-predicate to test the three elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception
         * @see #accept(Throwables.DoubleTriConsumer)
         * @see #map(Throwables.DoubleTriFunction)
         */
        public <E extends Exception> Optional<DoubleTuple3> filter(final Throwables.DoubleTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on all three elements.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * int h = DoubleTuple.of(1.0, 2.0, 3.0).hashCode();
         * // h == 31 * (31 * Double.hashCode(1.0) + Double.hashCode(2.0)) + Double.hashCode(3.0)
         *
         * // equal tuples always have equal hash codes
         * boolean same = DoubleTuple.of(1.0, 2.0, 3.0).hashCode() == DoubleTuple.of(1.0, 2.0, 3.0).hashCode();   // true
         *
         * // order matters: different permutations yield different hash codes
         * boolean diff = DoubleTuple.of(1.0, 2.0, 3.0).hashCode() != DoubleTuple.of(3.0, 2.0, 1.0).hashCode();   // true
         *
         * // NaN has a consistent hash code
         * int nanHash = DoubleTuple.of(Double.NaN, 1.0, 2.0).hashCode();
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            return 31 * result + Double.hashCode(_3);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple3 a = DoubleTuple.of(1.0, 2.0, 3.0);
         * DoubleTuple.DoubleTuple3 b = DoubleTuple.of(1.0, 2.0, 3.0);
         * boolean eq  = a.equals(b);   // true
         *
         * // order matters
         * boolean neq = a.equals(DoubleTuple.of(3.0, 2.0, 1.0));   // false
         *
         * // NaN equals NaN (Double.compare semantics)
         * boolean nanEq = DoubleTuple.of(Double.NaN, 1.0, 2.0).equals(DoubleTuple.of(Double.NaN, 1.0, 2.0));   // true
         *
         * // null and different types are never equal
         * boolean nullEq = a.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple.DoubleTuple3 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple3 other)) {
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
         * String s1 = DoubleTuple.of(1.0, 2.0, 3.0).toString();    // "(1.0, 2.0, 3.0)"
         * String s2 = DoubleTuple.of(-1.5, 0.0, 1.5).toString();   // "(-1.5, 0.0, 1.5)"
         *
         * // NaN and Infinity use Java's standard double string forms
         * String sNaN = DoubleTuple.of(Double.NaN, 1.0, 2.0).toString();                                     // "(NaN, 1.0, 2.0)"
         * String sInf = DoubleTuple.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0).toString();  // "(Infinity, -Infinity, 0.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
        }

        /**
         * Returns the internal array of double elements.
         * The array is lazily initialized on first access.
         *
         * @return a double array containing all elements of this tuple
         */
        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly four double values.
     * <p>
     * Provides direct access to elements via public final fields {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
     * This arity does not expose the bi/tri-arg functional helpers that
     * {@link DoubleTuple2} and {@link DoubleTuple3} provide.
     * </p>
     */
    public static final class DoubleTuple4 extends DoubleTuple<DoubleTuple4> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;

        DoubleTuple4() {
            this(0, 0, 0, 0);
        }

        DoubleTuple4(final double _1, final double _2, final double _3, final double _4) {
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
         * DoubleTuple.DoubleTuple4 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
         * t.arity(); // returns 4
         * DoubleTuple.DoubleTuple4 t2 = DoubleTuple.of(-1.0, 0.0, 1.0, Double.NaN);
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
         * Uses {@link Math#min(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0} is
         * treated as less than {@code +0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple4 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5);
         * t.min(); // returns 1.0
         * DoubleTuple.DoubleTuple4 t2 = DoubleTuple.of(-5.0, -3.0, -1.0, -2.0);
         * t2.min(); // returns -5.0
         * DoubleTuple.DoubleTuple4 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0);
         * Double.isNaN(tNaN.min()); // returns true (NaN propagates)
         * DoubleTuple.DoubleTuple4 tInf = DoubleTuple.of(1.0, Double.NEGATIVE_INFINITY, 3.0, 4.0);
         * tInf.min(); // returns Double.NEGATIVE_INFINITY
         * }</pre>
         *
         * @return the smallest of {@code _1}, {@code _2}, {@code _3}, and {@code _4}
         */
        @Override
        public double min() {
            return Math.min(Math.min(_1, _2), Math.min(_3, _4));
        }

        /**
         * Returns the maximum value among the four elements.
         * Uses {@link Math#max(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0} is
         * treated as greater than {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple4 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5);
         * t.max(); // returns 4.0
         * DoubleTuple.DoubleTuple4 t2 = DoubleTuple.of(-5.0, -3.0, -1.0, -2.0);
         * t2.max(); // returns -1.0
         * DoubleTuple.DoubleTuple4 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0);
         * Double.isNaN(tNaN.max()); // returns true (NaN propagates)
         * DoubleTuple.DoubleTuple4 tInf = DoubleTuple.of(1.0, Double.POSITIVE_INFINITY, 3.0, 4.0);
         * tInf.max(); // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the largest of {@code _1}, {@code _2}, {@code _3}, and {@code _4}
         */
        @Override
        public double max() {
            return Math.max(Math.max(_1, _2), Math.max(_3, _4));
        }

        /**
         * Returns the median value of the four elements.
         * For an even number of elements, returns the lower of the two middle values
         * (not their average). Ordering uses {@link Double#compare(double, double)}
         * semantics, so {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // sorted: 1.0, 2.0, 3.0, 4.0 -> lower middle = 2.0
         * DoubleTuple.DoubleTuple4 t = DoubleTuple.of(3.0, 1.0, 4.0, 2.0);
         * t.median(); // returns 2.0
         * // sorted: -3.0, -1.0, 0.0, 1.0 -> lower middle = -1.0
         * DoubleTuple.DoubleTuple4 t2 = DoubleTuple.of(0.0, -1.0, 1.0, -3.0);
         * t2.median(); // returns -1.0
         * // NaN treated as largest: sorted: 1.0, 2.0, 3.0, NaN -> lower middle = 2.0
         * DoubleTuple.DoubleTuple4 tNaN = DoubleTuple.of(1.0, Double.NaN, 2.0, 3.0);
         * tNaN.median(); // returns 2.0
         * // Duplicates: sorted: 1.0, 1.0, 2.0, 2.0 -> lower middle = 1.0
         * DoubleTuple.DoubleTuple4 tDup = DoubleTuple.of(2.0, 1.0, 1.0, 2.0);
         * tDup.median(); // returns 1.0
         * }</pre>
         *
         * @return the lower middle value when the four elements are sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of the four elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple4 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
         * t.sum(); // returns 10.0
         * DoubleTuple.DoubleTuple4 t2 = DoubleTuple.of(-1.0, -2.0, 3.0, 4.0);
         * t2.sum(); // returns 4.0
         * DoubleTuple.DoubleTuple4 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0);
         * Double.isNaN(tNaN.sum()); // returns true
         * DoubleTuple.DoubleTuple4 tInf = DoubleTuple.of(1.0, Double.POSITIVE_INFINITY, 3.0, 4.0);
         * tInf.sum(); // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1}, {@code _2}, {@code _3}, and {@code _4}
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4);
        }

        /**
         * Returns the arithmetic mean of the four elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple4 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
         * t.average(); // returns 2.5
         * DoubleTuple.DoubleTuple4 t2 = DoubleTuple.of(-2.0, -1.0, 1.0, 2.0);
         * t2.average(); // returns 0.0
         * DoubleTuple.DoubleTuple4 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0);
         * Double.isNaN(tNaN.average()); // returns true
         * DoubleTuple.DoubleTuple4 tSame = DoubleTuple.of(3.0, 3.0, 3.0, 3.0);
         * tSame.average(); // returns 3.0
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
         * DoubleTuple.DoubleTuple4 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
         * DoubleTuple.DoubleTuple4 reversed = tuple.reverse();   // (4.0, 3.0, 2.0, 1.0)
         * DoubleTuple.DoubleTuple4 t2 = DoubleTuple.of(-1.5, 0.0, 2.5, -3.0);
         * t2.reverse().toString(); // returns "(-3.0, 2.5, 0.0, -1.5)"
         * // NaN survives reversal: (1.0, NaN, 3.0, 4.0) reversed = (4.0, 3.0, NaN, 1.0)
         * DoubleTuple.DoubleTuple4 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0);
         * Double.isNaN(tNaN.reverse()._3); // returns true
         * // Duplicates: (5.0, 5.0, 6.0, 6.0) reversed = (6.0, 6.0, 5.0, 5.0)
         * DoubleTuple.DoubleTuple4 tDup = DoubleTuple.of(5.0, 5.0, 6.0, 6.0);
         * tDup.reverse().toString(); // returns "(6.0, 6.0, 5.0, 5.0)"
         * }</pre>
         *
         * @return a new DoubleTuple.DoubleTuple4 with (_4, _3, _2, _1)
         */
        @Override
        public DoubleTuple4 reverse() {
            return new DoubleTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified double value.
         * Comparisons use {@link Double#compare(double, double)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0} does not match {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple4 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
         * t.contains(3.0); // returns true
         * t.contains(5.0); // returns false
         * // NaN matches NaN
         * DoubleTuple.DoubleTuple4 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0);
         * tNaN.contains(Double.NaN); // returns true
         * // +0.0 does not match -0.0
         * DoubleTuple.DoubleTuple4 tZero = DoubleTuple.of(1.0, -0.0, 3.0, 4.0);
         * tZero.contains(0.0); // returns false
         * }</pre>
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if any of {@code _1} through {@code _4} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple4 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
         * double[] sum = {0.0};
         * t.forEach(v -> sum[0] += v);
         * sum[0]; // returns 10.0 (elements visited in order: 1.0, 2.0, 3.0, 4.0)
         * // null action throws IllegalArgumentException
         * // t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> action) throws E {
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
         * DoubleTuple.DoubleTuple4 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
         * DoubleTuple.DoubleTuple4 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
         * t1.hashCode() == t2.hashCode(); // returns true (equal tuples have equal hash codes)
         * DoubleTuple.DoubleTuple4 t3 = DoubleTuple.of(4.0, 3.0, 2.0, 1.0);
         * t1.hashCode() == t3.hashCode(); // returns false (different element order)
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            return 31 * result + Double.hashCode(_4);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple4 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
         * DoubleTuple.DoubleTuple4 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
         * t1.equals(t2); // returns true
         * DoubleTuple.DoubleTuple4 t3 = DoubleTuple.of(1.0, 2.0, 3.0, 5.0);
         * t1.equals(t3);            // returns false (last element differs)
         * t1.equals(null);          // returns false
         * t1.equals("not a tuple"); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple.DoubleTuple4 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple4 other)) {
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
         * DoubleTuple.DoubleTuple4 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
         * t.toString(); // returns "(1.0, 2.0, 3.0, 4.0)"
         * DoubleTuple.DoubleTuple4 t2 = DoubleTuple.of(-1.5, 0.0, 3.5, -2.0);
         * t2.toString(); // returns "(-1.5, 0.0, 3.5, -2.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
        }

        /**
         * Returns the internal array of double elements.
         * The array is lazily initialized on first access.
         *
         * @return a double array containing all elements of this tuple
         */
        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly five double values.
     * <p>
     * Provides direct access to elements via public final fields {@code _1} through {@code _5}.
     * </p>
     */
    public static final class DoubleTuple5 extends DoubleTuple<DoubleTuple5> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;
        /** The fifth double value in this tuple. */
        public final double _5;

        DoubleTuple5() {
            this(0, 0, 0, 0, 0);
        }

        DoubleTuple5(final double _1, final double _2, final double _3, final double _4, final double _5) {
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
         * DoubleTuple.DoubleTuple5 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
         * t.arity(); // returns 5
         * DoubleTuple.DoubleTuple5 t2 = DoubleTuple.of(-1.0, 0.0, 1.0, Double.NaN, Double.POSITIVE_INFINITY);
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
         * Uses {@link Math#min(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0} is
         * treated as less than {@code +0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple5 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5, 2.0);
         * t.min(); // returns 1.0
         * DoubleTuple.DoubleTuple5 t2 = DoubleTuple.of(-5.0, -3.0, -1.0, -2.0, -4.0);
         * t2.min(); // returns -5.0
         * DoubleTuple.DoubleTuple5 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0);
         * Double.isNaN(tNaN.min()); // returns true (NaN propagates)
         * DoubleTuple.DoubleTuple5 tInf = DoubleTuple.of(1.0, Double.NEGATIVE_INFINITY, 3.0, 4.0, 5.0);
         * tInf.min(); // returns Double.NEGATIVE_INFINITY
         * }</pre>
         *
         * @return the smallest of {@code _1} through {@code _5}
         */
        @Override
        public double min() {
            return Math.min(Math.min(Math.min(_1, _2), Math.min(_3, _4)), _5);
        }

        /**
         * Returns the maximum value among the five elements.
         * Uses {@link Math#max(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0} is
         * treated as greater than {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple5 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5, 2.0);
         * t.max(); // returns 4.0
         * DoubleTuple.DoubleTuple5 t2 = DoubleTuple.of(-5.0, -3.0, -1.0, -2.0, -4.0);
         * t2.max(); // returns -1.0
         * DoubleTuple.DoubleTuple5 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0);
         * Double.isNaN(tNaN.max()); // returns true (NaN propagates)
         * DoubleTuple.DoubleTuple5 tInf = DoubleTuple.of(1.0, Double.POSITIVE_INFINITY, 3.0, 4.0, 5.0);
         * tInf.max(); // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the largest of {@code _1} through {@code _5}
         */
        @Override
        public double max() {
            return Math.max(Math.max(Math.max(_1, _2), Math.max(_3, _4)), _5);
        }

        /**
         * Returns the median value of the five elements.
         * For an odd number of elements, this is the exact middle value when sorted.
         * Ordering uses {@link Double#compare(double, double)} semantics, so
         * {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // sorted: 1.0, 2.0, 3.0, 4.0, 5.0 -> middle = 3.0
         * DoubleTuple.DoubleTuple5 t = DoubleTuple.of(3.0, 1.0, 5.0, 2.0, 4.0);
         * t.median(); // returns 3.0
         * // sorted: -2.0, -1.0, 0.0, 1.0, 2.0 -> middle = 0.0
         * DoubleTuple.DoubleTuple5 t2 = DoubleTuple.of(0.0, -1.0, 1.0, -2.0, 2.0);
         * t2.median(); // returns 0.0
         * // NaN treated as largest: sorted: 1.0, 2.0, 3.0, 4.0, NaN -> middle = 3.0
         * DoubleTuple.DoubleTuple5 tNaN = DoubleTuple.of(1.0, Double.NaN, 2.0, 3.0, 4.0);
         * tNaN.median(); // returns 3.0
         * // Duplicates: sorted: 1.0, 1.0, 2.0, 2.0, 3.0 -> middle = 2.0
         * DoubleTuple.DoubleTuple5 tDup = DoubleTuple.of(2.0, 1.0, 1.0, 3.0, 2.0);
         * tDup.median(); // returns 2.0
         * }</pre>
         *
         * @return the middle value when the five elements are sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of the five elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple5 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
         * t.sum(); // returns 15.0
         * DoubleTuple.DoubleTuple5 t2 = DoubleTuple.of(-1.0, -2.0, 3.0, 4.0, 5.0);
         * t2.sum(); // returns 9.0
         * DoubleTuple.DoubleTuple5 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0);
         * Double.isNaN(tNaN.sum()); // returns true
         * DoubleTuple.DoubleTuple5 tInf = DoubleTuple.of(1.0, Double.POSITIVE_INFINITY, 3.0, 4.0, 5.0);
         * tInf.sum(); // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1} through {@code _5}
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the arithmetic mean of the five elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple5 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
         * t.average(); // returns 3.0
         * DoubleTuple.DoubleTuple5 t2 = DoubleTuple.of(-2.0, -1.0, 0.0, 1.0, 2.0);
         * t2.average(); // returns 0.0
         * DoubleTuple.DoubleTuple5 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0);
         * Double.isNaN(tNaN.average()); // returns true
         * DoubleTuple.DoubleTuple5 tSame = DoubleTuple.of(4.0, 4.0, 4.0, 4.0, 4.0);
         * tSame.average(); // returns 4.0
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
         * DoubleTuple.DoubleTuple5 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
         * DoubleTuple.DoubleTuple5 reversed = tuple.reverse();   // (5.0, 4.0, 3.0, 2.0, 1.0)
         * DoubleTuple.DoubleTuple5 t2 = DoubleTuple.of(-1.5, 0.0, 2.5, -3.0, 1.0);
         * t2.reverse().toString(); // returns "(1.0, -3.0, 2.5, 0.0, -1.5)"
         * // NaN survives reversal: (1.0, NaN, 3.0, 4.0, 5.0) reversed = (5.0, 4.0, 3.0, NaN, 1.0)
         * DoubleTuple.DoubleTuple5 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0);
         * Double.isNaN(tNaN.reverse()._4); // returns true
         * // Duplicates: (5.0, 5.0, 6.0, 6.0, 7.0) reversed = (7.0, 6.0, 6.0, 5.0, 5.0)
         * DoubleTuple.DoubleTuple5 tDup = DoubleTuple.of(5.0, 5.0, 6.0, 6.0, 7.0);
         * tDup.reverse().toString(); // returns "(7.0, 6.0, 6.0, 5.0, 5.0)"
         * }</pre>
         *
         * @return a new DoubleTuple.DoubleTuple5 with (_5, _4, _3, _2, _1)
         */
        @Override
        public DoubleTuple5 reverse() {
            return new DoubleTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified double value.
         * Comparisons use {@link Double#compare(double, double)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0} does not match {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple5 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
         * t.contains(3.0); // returns true
         * t.contains(6.0); // returns false
         * // NaN matches NaN
         * DoubleTuple.DoubleTuple5 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0);
         * tNaN.contains(Double.NaN); // returns true
         * // +0.0 does not match -0.0
         * DoubleTuple.DoubleTuple5 tZero = DoubleTuple.of(1.0, -0.0, 3.0, 4.0, 5.0);
         * tZero.contains(0.0); // returns false
         * }</pre>
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if any of {@code _1} through {@code _5} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind)
                    || N.equals(_5, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple5 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
         * double[] sum = {0.0};
         * t.forEach(v -> sum[0] += v);
         * sum[0]; // returns 15.0 (elements visited in order: 1.0, 2.0, 3.0, 4.0, 5.0)
         * // null action throws IllegalArgumentException
         * // t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> action) throws E {
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
         * DoubleTuple.DoubleTuple5 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
         * DoubleTuple.DoubleTuple5 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
         * t1.hashCode() == t2.hashCode(); // returns true (equal tuples have equal hash codes)
         * DoubleTuple.DoubleTuple5 t3 = DoubleTuple.of(5.0, 4.0, 3.0, 2.0, 1.0);
         * t1.hashCode() == t3.hashCode(); // returns false (different element order)
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            result = 31 * result + Double.hashCode(_4);
            return 31 * result + Double.hashCode(_5);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple5 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
         * DoubleTuple.DoubleTuple5 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
         * t1.equals(t2); // returns true
         * DoubleTuple.DoubleTuple5 t3 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 6.0);
         * t1.equals(t3);            // returns false (last element differs)
         * t1.equals(null);          // returns false
         * t1.equals("not a tuple"); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple.DoubleTuple5 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple5 other)) {
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
         * DoubleTuple.DoubleTuple5 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0);
         * t.toString(); // returns "(1.0, 2.0, 3.0, 4.0, 5.0)"
         * DoubleTuple.DoubleTuple5 t2 = DoubleTuple.of(-1.5, 0.0, 3.5, -2.0, 1.0);
         * t2.toString(); // returns "(-1.5, 0.0, 3.5, -2.0, 1.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4, _5)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ")";
        }

        /**
         * Returns the internal array of double elements.
         * The array is lazily initialized on first access.
         *
         * @return a double array containing all elements of this tuple
         */
        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly six double values.
     * <p>
     * Provides direct access to elements via public final fields {@code _1} through {@code _6}.
     * </p>
     */
    public static final class DoubleTuple6 extends DoubleTuple<DoubleTuple6> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;
        /** The fifth double value in this tuple. */
        public final double _5;
        /** The sixth double value in this tuple. */
        public final double _6;

        DoubleTuple6() {
            this(0, 0, 0, 0, 0, 0);
        }

        DoubleTuple6(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6) {
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
         * DoubleTuple.DoubleTuple6 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * t.arity(); // returns 6
         * DoubleTuple.DoubleTuple6 t2 = DoubleTuple.of(-1.0, 0.0, 1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
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
         * Uses {@link Math#min(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0} is
         * treated as less than {@code +0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple6 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5, 2.0, 0.5);
         * t.min(); // returns 0.5
         * DoubleTuple.DoubleTuple6 t2 = DoubleTuple.of(-5.0, -3.0, -1.0, -2.0, -4.0, -6.0);
         * t2.min(); // returns -6.0
         * DoubleTuple.DoubleTuple6 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0, 6.0);
         * Double.isNaN(tNaN.min()); // returns true (NaN propagates)
         * DoubleTuple.DoubleTuple6 tInf = DoubleTuple.of(1.0, Double.NEGATIVE_INFINITY, 3.0, 4.0, 5.0, 6.0);
         * tInf.min(); // returns Double.NEGATIVE_INFINITY
         * }</pre>
         *
         * @return the smallest of {@code _1} through {@code _6}
         */
        @Override
        public double min() {
            return Math.min(Math.min(Math.min(_1, _2), Math.min(_3, _4)), Math.min(_5, _6));
        }

        /**
         * Returns the maximum value among the six elements.
         * Uses {@link Math#max(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0} is
         * treated as greater than {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple6 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5, 2.0, 0.5);
         * t.max(); // returns 4.0
         * DoubleTuple.DoubleTuple6 t2 = DoubleTuple.of(-5.0, -3.0, -1.0, -2.0, -4.0, -6.0);
         * t2.max(); // returns -1.0
         * DoubleTuple.DoubleTuple6 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0, 6.0);
         * Double.isNaN(tNaN.max()); // returns true (NaN propagates)
         * DoubleTuple.DoubleTuple6 tInf = DoubleTuple.of(1.0, Double.POSITIVE_INFINITY, 3.0, 4.0, 5.0, 6.0);
         * tInf.max(); // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the largest of {@code _1} through {@code _6}
         */
        @Override
        public double max() {
            return Math.max(Math.max(Math.max(_1, _2), Math.max(_3, _4)), Math.max(_5, _6));
        }

        /**
         * Returns the median value of the six elements.
         * For an even number of elements, returns the lower of the two middle values
         * (not their average). Ordering uses {@link Double#compare(double, double)}
         * semantics, so {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // sorted: 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 -> lower middle = 3.0
         * DoubleTuple.DoubleTuple6 t = DoubleTuple.of(3.0, 1.0, 5.0, 2.0, 4.0, 6.0);
         * t.median(); // returns 3.0
         * // sorted: -3.0, -2.0, -1.0, 0.0, 1.0, 2.0 -> lower middle = -1.0
         * DoubleTuple.DoubleTuple6 t2 = DoubleTuple.of(0.0, -1.0, 1.0, -2.0, 2.0, -3.0);
         * t2.median(); // returns -1.0
         * // NaN treated as largest: sorted: 1.0, 2.0, 3.0, 4.0, 5.0, NaN -> lower middle = 3.0
         * DoubleTuple.DoubleTuple6 tNaN = DoubleTuple.of(1.0, Double.NaN, 2.0, 3.0, 4.0, 5.0);
         * tNaN.median(); // returns 3.0
         * // Duplicates: sorted: 1.0, 1.0, 2.0, 2.0, 3.0, 3.0 -> lower middle = 2.0
         * DoubleTuple.DoubleTuple6 tDup = DoubleTuple.of(2.0, 1.0, 3.0, 1.0, 3.0, 2.0);
         * tDup.median(); // returns 2.0
         * }</pre>
         *
         * @return the lower middle value when the six elements are sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of the six elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple6 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * t.sum(); // returns 21.0
         * DoubleTuple.DoubleTuple6 t2 = DoubleTuple.of(-1.0, -2.0, 3.0, 4.0, 5.0, 6.0);
         * t2.sum(); // returns 15.0
         * DoubleTuple.DoubleTuple6 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0, 6.0);
         * Double.isNaN(tNaN.sum()); // returns true
         * DoubleTuple.DoubleTuple6 tInf = DoubleTuple.of(1.0, Double.POSITIVE_INFINITY, 3.0, 4.0, 5.0, 6.0);
         * tInf.sum(); // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1} through {@code _6}
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the arithmetic mean of the six elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple6 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * t.average(); // returns 3.5
         * DoubleTuple.DoubleTuple6 t2 = DoubleTuple.of(-2.0, -1.0, 0.0, 1.0, 2.0, 3.0);
         * t2.average(); // returns 0.5
         * DoubleTuple.DoubleTuple6 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0, 6.0);
         * Double.isNaN(tNaN.average()); // returns true
         * DoubleTuple.DoubleTuple6 tSame = DoubleTuple.of(5.0, 5.0, 5.0, 5.0, 5.0, 5.0);
         * tSame.average(); // returns 5.0
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
         * DoubleTuple.DoubleTuple6 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * DoubleTuple.DoubleTuple6 reversed = tuple.reverse();   // (6.0, 5.0, 4.0, 3.0, 2.0, 1.0)
         * DoubleTuple.DoubleTuple6 t2 = DoubleTuple.of(-1.5, 0.0, 2.5, -3.0, 1.0, 4.0);
         * t2.reverse().toString(); // returns "(4.0, 1.0, -3.0, 2.5, 0.0, -1.5)"
         * // NaN survives reversal: (1.0, NaN, 3.0, 4.0, 5.0, 6.0) reversed = (6.0, 5.0, 4.0, 3.0, NaN, 1.0)
         * DoubleTuple.DoubleTuple6 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0, 6.0);
         * Double.isNaN(tNaN.reverse()._5); // returns true
         * // Duplicates: (5.0, 5.0, 6.0, 6.0, 7.0, 7.0) reversed = (7.0, 7.0, 6.0, 6.0, 5.0, 5.0)
         * DoubleTuple.DoubleTuple6 tDup = DoubleTuple.of(5.0, 5.0, 6.0, 6.0, 7.0, 7.0);
         * tDup.reverse().toString(); // returns "(7.0, 7.0, 6.0, 6.0, 5.0, 5.0)"
         * }</pre>
         *
         * @return a new DoubleTuple.DoubleTuple6 with (_6, _5, _4, _3, _2, _1)
         */
        @Override
        public DoubleTuple6 reverse() {
            return new DoubleTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified double value.
         * Comparisons use {@link Double#compare(double, double)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0} does not match {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple6 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * t.contains(5.0); // returns true
         * t.contains(7.0); // returns false
         * // NaN matches NaN
         * DoubleTuple.DoubleTuple6 tNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0, 6.0);
         * tNaN.contains(Double.NaN); // returns true
         * // +0.0 does not match -0.0
         * DoubleTuple.DoubleTuple6 tZero = DoubleTuple.of(1.0, -0.0, 3.0, 4.0, 5.0, 6.0);
         * tZero.contains(0.0); // returns false
         * }</pre>
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if any of {@code _1} through {@code _6} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple6 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * double[] sum = {0.0};
         * t.forEach(v -> sum[0] += v);
         * sum[0]; // returns 21.0 (elements visited in order: 1.0, 2.0, 3.0, 4.0, 5.0, 6.0)
         * // null action throws IllegalArgumentException
         * // t.forEach(null); // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> action) throws E {
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
         * DoubleTuple.DoubleTuple6 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * DoubleTuple.DoubleTuple6 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * t1.hashCode() == t2.hashCode(); // returns true (equal tuples have equal hash codes)
         * DoubleTuple.DoubleTuple6 t3 = DoubleTuple.of(6.0, 5.0, 4.0, 3.0, 2.0, 1.0);
         * t1.hashCode() == t3.hashCode(); // returns false (different element order)
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            result = 31 * result + Double.hashCode(_4);
            result = 31 * result + Double.hashCode(_5);
            return 31 * result + Double.hashCode(_6);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple6 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * DoubleTuple.DoubleTuple6 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * t1.equals(t2); // returns true
         * DoubleTuple.DoubleTuple6 t3 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 7.0);
         * t1.equals(t3);            // returns false (last element differs)
         * t1.equals(null);          // returns false
         * t1.equals("not a tuple"); // returns false
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple.DoubleTuple6 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple6 other)) {
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
         * DoubleTuple.DoubleTuple6 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * t.toString(); // returns "(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)"
         * DoubleTuple.DoubleTuple6 t2 = DoubleTuple.of(-1.5, 0.0, 3.5, -2.0, 1.0, 4.0);
         * t2.toString(); // returns "(-1.5, 0.0, 3.5, -2.0, 1.0, 4.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4, _5, _6)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ")";
        }

        /**
         * Returns the internal array of double elements.
         * The array is lazily initialized on first access.
         *
         * @return a double array containing all elements of this tuple
         */
        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly seven double values.
     * <p>
     * Provides direct access to elements via public final fields {@code _1} through {@code _7}.
     * </p>
     */
    public static final class DoubleTuple7 extends DoubleTuple<DoubleTuple7> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;
        /** The fifth double value in this tuple. */
        public final double _5;
        /** The sixth double value in this tuple. */
        public final double _6;
        /** The seventh double value in this tuple. */
        public final double _7;

        DoubleTuple7() {
            this(0, 0, 0, 0, 0, 0, 0);
        }

        DoubleTuple7(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7) {
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
         * DoubleTuple.DoubleTuple7 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * t.arity(); // returns 7
         * DoubleTuple.DoubleTuple7 t2 = DoubleTuple.of(-1.0, 0.0, 1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 2.5);
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
         * Uses {@link Math#min(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0} is
         * treated as less than {@code +0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple7 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5, 9.0, 2.0, 6.0);
         * double m = t.min();   // returns 1.0
         *
         * DoubleTuple.DoubleTuple7 neg = DoubleTuple.of(-5.0, -3.0, -1.0, 0.0, 2.0, 4.0, 6.0);
         * double mn = neg.min();   // returns -5.0
         *
         * DoubleTuple.DoubleTuple7 withNaN = DoubleTuple.of(Double.NaN, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * assertTrue(Double.isNaN(withNaN.min()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple7 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * double mi = withInf.min();   // returns 1.0
         * }</pre>
         *
         * @return the smallest of {@code _1} through {@code _7}
         */
        @Override
        public double min() {
            return Math.min(Math.min(Math.min(_1, _2), Math.min(_3, _4)), Math.min(Math.min(_5, _6), _7));
        }

        /**
         * Returns the maximum value among the seven elements.
         * Uses {@link Math#max(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0} is
         * treated as greater than {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple7 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5, 9.0, 2.0, 6.0);
         * double m = t.max();   // returns 9.0
         *
         * DoubleTuple.DoubleTuple7 neg = DoubleTuple.of(-5.0, -3.0, -1.0, 0.0, 2.0, 4.0, 6.0);
         * double mx = neg.max();   // returns 6.0
         *
         * DoubleTuple.DoubleTuple7 withNaN = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, Double.NaN);
         * assertTrue(Double.isNaN(withNaN.max()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple7 withNegInf = DoubleTuple.of(Double.NEGATIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * double mx2 = withNegInf.max();   // returns 6.0
         * }</pre>
         *
         * @return the largest of {@code _1} through {@code _7}
         */
        @Override
        public double max() {
            return Math.max(Math.max(Math.max(_1, _2), Math.max(_3, _4)), Math.max(Math.max(_5, _6), _7));
        }

        /**
         * Returns the median value of the seven elements.
         * For an odd number of elements, this is the exact middle value when sorted.
         * Ordering uses {@link Double#compare(double, double)} semantics, so
         * {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple7 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * double med = t.median();   // returns 4.0 (4th of 7 when sorted)
         *
         * DoubleTuple.DoubleTuple7 unsorted = DoubleTuple.of(7.0, 3.0, 5.0, 1.0, 6.0, 2.0, 4.0);
         * double med2 = unsorted.median();   // returns 4.0
         *
         * DoubleTuple.DoubleTuple7 withNeg = DoubleTuple.of(-3.0, -1.0, 0.0, 1.0, 2.0, 3.0, 4.0);
         * double med3 = withNeg.median();   // returns 1.0
         *
         * DoubleTuple.DoubleTuple7 dups = DoubleTuple.of(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0);
         * double med4 = dups.median();   // returns 2.0
         * }</pre>
         *
         * @return the middle value when the seven elements are sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of the seven elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple7 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * double s = t.sum();   // returns 28.0
         *
         * DoubleTuple.DoubleTuple7 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0);
         * double sn = neg.sum();   // returns -28.0
         *
         * DoubleTuple.DoubleTuple7 withNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0, 6.0, 7.0);
         * assertTrue(Double.isNaN(withNaN.sum()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple7 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * double si = withInf.sum();   // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1} through {@code _7}
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the arithmetic mean of the seven elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple7 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * double avg = t.average();   // returns 4.0  (28.0 / 7)
         *
         * DoubleTuple.DoubleTuple7 neg = DoubleTuple.of(-3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0);
         * double avgn = neg.average();   // returns 0.0
         *
         * DoubleTuple.DoubleTuple7 withNaN = DoubleTuple.of(1.0, 2.0, Double.NaN, 4.0, 5.0, 6.0, 7.0);
         * assertTrue(Double.isNaN(withNaN.average()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple7 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
         * double avgi = withInf.average();   // returns Double.POSITIVE_INFINITY
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
         * DoubleTuple.DoubleTuple7 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * DoubleTuple.DoubleTuple7 reversed = tuple.reverse();   // (7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0)
         *
         * DoubleTuple.DoubleTuple7 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0);
         * DoubleTuple.DoubleTuple7 revNeg = neg.reverse();   // (-7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0)
         *
         * DoubleTuple.DoubleTuple7 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * DoubleTuple.DoubleTuple7 revNaN = withNaN.reverse();   // (7.0, 6.0, 5.0, 4.0, 3.0, 2.0, NaN)
         *
         * DoubleTuple.DoubleTuple7 dups = DoubleTuple.of(1.0, 1.0, 2.0, 3.0, 4.0, 5.0, 5.0);
         * DoubleTuple.DoubleTuple7 revDups = dups.reverse();   // (5.0, 5.0, 4.0, 3.0, 2.0, 1.0, 1.0)
         * }</pre>
         *
         * @return a new DoubleTuple.DoubleTuple7 with (_7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public DoubleTuple7 reverse() {
            return new DoubleTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified double value.
         * Comparisons use {@link Double#compare(double, double)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0} does not match {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple7 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * boolean found = t.contains(4.0);      // returns true
         * boolean notFound = t.contains(8.0);   // returns false
         *
         * DoubleTuple.DoubleTuple7 withNaN = DoubleTuple.of(1.0, 2.0, Double.NaN, 4.0, 5.0, 6.0, 7.0);
         * boolean hasNaN = withNaN.contains(Double.NaN);   // returns true (NaN matches NaN)
         *
         * DoubleTuple.DoubleTuple7 withNeg = DoubleTuple.of(1.0, 2.0, 3.0, -4.0, 5.0, 6.0, 7.0);
         * boolean hasNeg = withNeg.contains(-4.0);   // returns true
         * boolean notNeg = withNeg.contains(4.0);    // returns false
         * }</pre>
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if any of {@code _1} through {@code _7} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple7 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * double[] sum = {0.0};
         * t.forEach(v -> sum[0] += v);   // sum[0] == 28.0
         *
         * java.util.List<Double> list = new java.util.ArrayList<>();
         * t.forEach(v -> list.add(v));   // list == [1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0]
         *
         * DoubleTuple.DoubleTuple7 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0);
         * double[] sumNeg = {0.0};
         * neg.forEach(v -> sumNeg[0] += v);   // sumNeg[0] == -28.0
         *
         * DoubleTuple.DoubleTuple7 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * // t2.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> action) throws E {
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
         * DoubleTuple.DoubleTuple7 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * DoubleTuple.DoubleTuple7 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * assertEquals(t1.hashCode(), t2.hashCode());   // equal tuples have equal hash codes
         *
         * DoubleTuple.DoubleTuple7 t3 = DoubleTuple.of(7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0);
         * // t1.hashCode() != t3.hashCode() for different element orders (likely)
         *
         * DoubleTuple.DoubleTuple7 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * DoubleTuple.DoubleTuple7 withNaN2 = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * assertEquals(withNaN.hashCode(), withNaN2.hashCode());   // NaN has consistent hash
         *
         * DoubleTuple.DoubleTuple7 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0);
         * DoubleTuple.DoubleTuple7 neg2 = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0);
         * assertEquals(neg.hashCode(), neg2.hashCode());   // negative values hash consistently
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            result = 31 * result + Double.hashCode(_4);
            result = 31 * result + Double.hashCode(_5);
            result = 31 * result + Double.hashCode(_6);
            return 31 * result + Double.hashCode(_7);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple7 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * DoubleTuple.DoubleTuple7 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * assertTrue(t1.equals(t2));   // same values -> equal
         *
         * DoubleTuple.DoubleTuple7 t3 = DoubleTuple.of(7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0);
         * assertFalse(t1.equals(t3));   // different order -> not equal
         *
         * assertFalse(t1.equals(null));       // null -> not equal
         * assertFalse(t1.equals("string"));   // wrong type -> not equal
         *
         * DoubleTuple.DoubleTuple7 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * DoubleTuple.DoubleTuple7 withNaN2 = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * assertTrue(withNaN.equals(withNaN2));   // NaN equals NaN via Double.compare semantics
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple.DoubleTuple7 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple7 other)) {
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
         * DoubleTuple.DoubleTuple7 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * String s = t.toString();   // returns "(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0)"
         *
         * DoubleTuple.DoubleTuple7 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0);
         * String sn = neg.toString();   // returns "(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0)"
         *
         * DoubleTuple.DoubleTuple7 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * String sNaN = withNaN.toString();   // returns "(NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0)"
         *
         * DoubleTuple.DoubleTuple7 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * String sInf = withInf.toString();   // returns "(Infinity, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4, _5, _6, _7)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ")";
        }

        /**
         * Returns the internal array of double elements.
         * The array is lazily initialized on first access.
         *
         * @return a double array containing all elements of this tuple
         */
        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly eight double values.
     * <p>
     * Provides direct access to elements via public final fields {@code _1} through {@code _8}.
     * </p>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more double values
     */
    @Deprecated
    public static final class DoubleTuple8 extends DoubleTuple<DoubleTuple8> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;
        /** The fifth double value in this tuple. */
        public final double _5;
        /** The sixth double value in this tuple. */
        public final double _6;
        /** The seventh double value in this tuple. */
        public final double _7;
        /** The eighth double value in this tuple. */
        public final double _8;

        DoubleTuple8() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }

        DoubleTuple8(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7, final double _8) {
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
         * DoubleTuple.DoubleTuple8 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * int n = t.arity();   // returns 8
         *
         * DoubleTuple.DoubleTuple8 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0);
         * int n2 = neg.arity();   // returns 8
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
         * Uses {@link Math#min(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0} is
         * treated as less than {@code +0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple8 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5, 9.0, 2.0, 6.0, 0.5);
         * double m = t.min();   // returns 0.5
         *
         * DoubleTuple.DoubleTuple8 neg = DoubleTuple.of(-5.0, -3.0, -1.0, 0.0, 2.0, 4.0, 6.0, 8.0);
         * double mn = neg.min();   // returns -5.0
         *
         * DoubleTuple.DoubleTuple8 withNaN = DoubleTuple.of(Double.NaN, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * assertTrue(Double.isNaN(withNaN.min()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple8 withInf = DoubleTuple.of(Double.NEGATIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * double mi = withInf.min();   // returns Double.NEGATIVE_INFINITY
         * }</pre>
         *
         * @return the smallest of {@code _1} through {@code _8}
         */
        @Override
        public double min() {
            return Math.min(Math.min(Math.min(_1, _2), Math.min(_3, _4)), Math.min(Math.min(_5, _6), Math.min(_7, _8)));
        }

        /**
         * Returns the maximum value among the eight elements.
         * Uses {@link Math#max(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0} is
         * treated as greater than {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple8 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5, 9.0, 2.0, 6.0, 0.5);
         * double m = t.max();   // returns 9.0
         *
         * DoubleTuple.DoubleTuple8 neg = DoubleTuple.of(-5.0, -3.0, -1.0, 0.0, 2.0, 4.0, 6.0, 8.0);
         * double mx = neg.max();   // returns 8.0
         *
         * DoubleTuple.DoubleTuple8 withNaN = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, Double.NaN);
         * assertTrue(Double.isNaN(withNaN.max()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple8 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * double mx2 = withInf.max();   // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the largest of {@code _1} through {@code _8}
         */
        @Override
        public double max() {
            return Math.max(Math.max(Math.max(_1, _2), Math.max(_3, _4)), Math.max(Math.max(_5, _6), Math.max(_7, _8)));
        }

        /**
         * Returns the median value of the eight elements.
         * For an even number of elements, returns the lower of the two middle values
         * (not their average). Ordering uses {@link Double#compare(double, double)}
         * semantics, so {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple8 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * double med = t.median();   // returns 4.0 (lower of 4th and 5th when sorted)
         *
         * DoubleTuple.DoubleTuple8 unsorted = DoubleTuple.of(8.0, 3.0, 5.0, 1.0, 6.0, 2.0, 4.0, 7.0);
         * double med2 = unsorted.median();   // returns 4.0
         *
         * DoubleTuple.DoubleTuple8 withNeg = DoubleTuple.of(-4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0);
         * double med3 = withNeg.median();   // returns -1.0
         *
         * DoubleTuple.DoubleTuple8 dups = DoubleTuple.of(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0);
         * double med4 = dups.median();   // returns 2.0
         * }</pre>
         *
         * @return the lower middle value when the eight elements are sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of the eight elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple8 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * double s = t.sum();   // returns 36.0
         *
         * DoubleTuple.DoubleTuple8 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0);
         * double sn = neg.sum();   // returns -36.0
         *
         * DoubleTuple.DoubleTuple8 withNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * assertTrue(Double.isNaN(withNaN.sum()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple8 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * double si = withInf.sum();   // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1} through {@code _8}
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the arithmetic mean of the eight elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple8 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * double avg = t.average();   // returns 4.5  (36.0 / 8)
         *
         * DoubleTuple.DoubleTuple8 neg = DoubleTuple.of(-4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0);
         * double avgn = neg.average();   // returns 0.0
         *
         * DoubleTuple.DoubleTuple8 withNaN = DoubleTuple.of(1.0, 2.0, Double.NaN, 4.0, 5.0, 6.0, 7.0, 8.0);
         * assertTrue(Double.isNaN(withNaN.average()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple8 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
         * double avgi = withInf.average();   // returns Double.POSITIVE_INFINITY
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
         * DoubleTuple.DoubleTuple8 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * DoubleTuple.DoubleTuple8 reversed = tuple.reverse();   // (8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0)
         *
         * DoubleTuple.DoubleTuple8 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0);
         * DoubleTuple.DoubleTuple8 revNeg = neg.reverse();   // (-8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0)
         *
         * DoubleTuple.DoubleTuple8 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * DoubleTuple.DoubleTuple8 revNaN = withNaN.reverse();   // (8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, NaN)
         *
         * DoubleTuple.DoubleTuple8 dups = DoubleTuple.of(1.0, 1.0, 2.0, 3.0, 4.0, 5.0, 5.0, 5.0);
         * DoubleTuple.DoubleTuple8 revDups = dups.reverse();   // (5.0, 5.0, 5.0, 4.0, 3.0, 2.0, 1.0, 1.0)
         * }</pre>
         *
         * @return a new DoubleTuple.DoubleTuple8 with (_8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public DoubleTuple8 reverse() {
            return new DoubleTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified double value.
         * Comparisons use {@link Double#compare(double, double)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0} does not match {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple8 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * boolean found = t.contains(5.0);      // returns true
         * boolean notFound = t.contains(9.0);   // returns false
         *
         * DoubleTuple.DoubleTuple8 withNaN = DoubleTuple.of(1.0, 2.0, Double.NaN, 4.0, 5.0, 6.0, 7.0, 8.0);
         * boolean hasNaN = withNaN.contains(Double.NaN);   // returns true (NaN matches NaN)
         *
         * DoubleTuple.DoubleTuple8 withNeg = DoubleTuple.of(1.0, 2.0, 3.0, -4.0, 5.0, 6.0, 7.0, 8.0);
         * boolean hasNeg = withNeg.contains(-4.0);   // returns true
         * boolean notNeg = withNeg.contains(4.0);    // returns false
         * }</pre>
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if any of {@code _1} through {@code _8} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple8 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * double[] sum = {0.0};
         * t.forEach(v -> sum[0] += v);   // sum[0] == 36.0
         *
         * java.util.List<Double> list = new java.util.ArrayList<>();
         * t.forEach(v -> list.add(v));   // list == [1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0]
         *
         * DoubleTuple.DoubleTuple8 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0);
         * double[] sumNeg = {0.0};
         * neg.forEach(v -> sumNeg[0] += v);   // sumNeg[0] == -36.0
         *
         * DoubleTuple.DoubleTuple8 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * // t2.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> action) throws E {
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
         * DoubleTuple.DoubleTuple8 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * DoubleTuple.DoubleTuple8 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * assertEquals(t1.hashCode(), t2.hashCode());   // equal tuples have equal hash codes
         *
         * DoubleTuple.DoubleTuple8 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * DoubleTuple.DoubleTuple8 withNaN2 = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * assertEquals(withNaN.hashCode(), withNaN2.hashCode());   // NaN has consistent hash
         *
         * DoubleTuple.DoubleTuple8 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0);
         * DoubleTuple.DoubleTuple8 neg2 = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0);
         * assertEquals(neg.hashCode(), neg2.hashCode());   // negative values hash consistently
         *
         * DoubleTuple.DoubleTuple8 t3 = DoubleTuple.of(8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0);
         * // t1.hashCode() and t3.hashCode() differ for different element orders (likely)
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            result = 31 * result + Double.hashCode(_4);
            result = 31 * result + Double.hashCode(_5);
            result = 31 * result + Double.hashCode(_6);
            result = 31 * result + Double.hashCode(_7);
            return 31 * result + Double.hashCode(_8);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple8 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * DoubleTuple.DoubleTuple8 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * assertTrue(t1.equals(t2));   // same values -> equal
         *
         * DoubleTuple.DoubleTuple8 t3 = DoubleTuple.of(8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0);
         * assertFalse(t1.equals(t3));   // different order -> not equal
         *
         * assertFalse(t1.equals(null));       // null -> not equal
         * assertFalse(t1.equals("string"));   // wrong type -> not equal
         *
         * DoubleTuple.DoubleTuple8 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * DoubleTuple.DoubleTuple8 withNaN2 = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * assertTrue(withNaN.equals(withNaN2));   // NaN equals NaN via Double.compare semantics
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple.DoubleTuple8 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple8 other)) {
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
         * DoubleTuple.DoubleTuple8 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * String s = t.toString();   // returns "(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0)"
         *
         * DoubleTuple.DoubleTuple8 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0);
         * String sn = neg.toString();   // returns "(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0)"
         *
         * DoubleTuple.DoubleTuple8 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * String sNaN = withNaN.toString();   // returns "(NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0)"
         *
         * DoubleTuple.DoubleTuple8 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * String sInf = withInf.toString();   // returns "(Infinity, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4, _5, _6, _7, _8)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")";
        }

        /**
         * Returns the internal array of double elements.
         * The array is lazily initialized on first access.
         *
         * @return a double array containing all elements of this tuple
         */
        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A DoubleTuple containing exactly nine double values.
     * <p>
     * Provides direct access to elements via public final fields {@code _1} through {@code _9}.
     * </p>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more double values
     */
    @Deprecated
    public static final class DoubleTuple9 extends DoubleTuple<DoubleTuple9> {

        /** The first double value in this tuple. */
        public final double _1;
        /** The second double value in this tuple. */
        public final double _2;
        /** The third double value in this tuple. */
        public final double _3;
        /** The fourth double value in this tuple. */
        public final double _4;
        /** The fifth double value in this tuple. */
        public final double _5;
        /** The sixth double value in this tuple. */
        public final double _6;
        /** The seventh double value in this tuple. */
        public final double _7;
        /** The eighth double value in this tuple. */
        public final double _8;
        /** The ninth double value in this tuple. */
        public final double _9;

        DoubleTuple9() {
            this(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        DoubleTuple9(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7, final double _8,
                final double _9) {
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
         * DoubleTuple.DoubleTuple9 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * int n = t.arity();   // returns 9
         *
         * DoubleTuple.DoubleTuple9 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0, -9.0);
         * int n2 = neg.arity();   // returns 9
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
         * Uses {@link Math#min(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code -0.0} is
         * treated as less than {@code +0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple9 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5, 9.0, 2.0, 6.0, 0.5, 7.0);
         * double m = t.min();   // returns 0.5
         *
         * DoubleTuple.DoubleTuple9 neg = DoubleTuple.of(-5.0, -3.0, -1.0, 0.0, 2.0, 4.0, 6.0, 8.0, 10.0);
         * double mn = neg.min();   // returns -5.0
         *
         * DoubleTuple.DoubleTuple9 withNaN = DoubleTuple.of(Double.NaN, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * assertTrue(Double.isNaN(withNaN.min()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple9 withInf = DoubleTuple.of(Double.NEGATIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * double mi = withInf.min();   // returns Double.NEGATIVE_INFINITY
         * }</pre>
         *
         * @return the smallest of {@code _1} through {@code _9}
         */
        @Override
        public double min() {
            return Math.min(Math.min(Math.min(Math.min(_1, _2), Math.min(_3, _4)), Math.min(Math.min(_5, _6), Math.min(_7, _8))), _9);
        }

        /**
         * Returns the maximum value among the nine elements.
         * Uses {@link Math#max(double, double)} pairwise: any {@code NaN}
         * element causes the result to be {@code NaN}, and {@code +0.0} is
         * treated as greater than {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple9 t = DoubleTuple.of(3.0, 1.0, 4.0, 1.5, 9.0, 2.0, 6.0, 0.5, 7.0);
         * double m = t.max();   // returns 9.0
         *
         * DoubleTuple.DoubleTuple9 neg = DoubleTuple.of(-5.0, -3.0, -1.0, 0.0, 2.0, 4.0, 6.0, 8.0, 10.0);
         * double mx = neg.max();   // returns 10.0
         *
         * DoubleTuple.DoubleTuple9 withNaN = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, Double.NaN);
         * assertTrue(Double.isNaN(withNaN.max()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple9 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * double mx2 = withInf.max();   // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the largest of {@code _1} through {@code _9}
         */
        @Override
        public double max() {
            return Math.max(Math.max(Math.max(Math.max(_1, _2), Math.max(_3, _4)), Math.max(Math.max(_5, _6), Math.max(_7, _8))), _9);
        }

        /**
         * Returns the median value of the nine elements.
         * For an odd number of elements, this is the exact middle value when sorted.
         * Ordering uses {@link Double#compare(double, double)} semantics, so
         * {@code NaN} is treated as the largest value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple9 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * double med = t.median();   // returns 5.0 (5th of 9 when sorted)
         *
         * DoubleTuple.DoubleTuple9 unsorted = DoubleTuple.of(9.0, 3.0, 5.0, 1.0, 6.0, 2.0, 4.0, 7.0, 8.0);
         * double med2 = unsorted.median();   // returns 5.0
         *
         * DoubleTuple.DoubleTuple9 withNeg = DoubleTuple.of(-4.0, -3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0, 4.0);
         * double med3 = withNeg.median();   // returns 0.0
         *
         * DoubleTuple.DoubleTuple9 dups = DoubleTuple.of(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0);
         * double med4 = dups.median();   // returns 2.0
         * }</pre>
         *
         * @return the middle value when the nine elements are sorted
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of the nine elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple9 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * double s = t.sum();   // returns 45.0
         *
         * DoubleTuple.DoubleTuple9 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0, -9.0);
         * double sn = neg.sum();   // returns -45.0
         *
         * DoubleTuple.DoubleTuple9 withNaN = DoubleTuple.of(1.0, Double.NaN, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * assertTrue(Double.isNaN(withNaN.sum()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple9 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * double si = withInf.sum();   // returns Double.POSITIVE_INFINITY
         * }</pre>
         *
         * @return the sum of {@code _1} through {@code _9}
         */
        @Override
        public double sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the arithmetic mean of the nine elements.
         * If any element is {@code NaN}, the result is {@code NaN}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple9 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * double avg = t.average();   // returns 5.0  (45.0 / 9)
         *
         * DoubleTuple.DoubleTuple9 neg = DoubleTuple.of(-4.0, -3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0, 4.0);
         * double avgn = neg.average();   // returns 0.0
         *
         * DoubleTuple.DoubleTuple9 withNaN = DoubleTuple.of(1.0, 2.0, Double.NaN, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * assertTrue(Double.isNaN(withNaN.average()));   // NaN propagates
         *
         * DoubleTuple.DoubleTuple9 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
         * double avgi = withInf.average();   // returns Double.POSITIVE_INFINITY
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
         * DoubleTuple.DoubleTuple9 tuple = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * DoubleTuple.DoubleTuple9 reversed = tuple.reverse();   // (9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0)
         *
         * DoubleTuple.DoubleTuple9 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0, -9.0);
         * DoubleTuple.DoubleTuple9 revNeg = neg.reverse();   // (-9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0)
         *
         * DoubleTuple.DoubleTuple9 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * DoubleTuple.DoubleTuple9 revNaN = withNaN.reverse();   // (9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, NaN)
         *
         * DoubleTuple.DoubleTuple9 dups = DoubleTuple.of(1.0, 1.0, 2.0, 3.0, 4.0, 5.0, 5.0, 6.0, 6.0);
         * DoubleTuple.DoubleTuple9 revDups = dups.reverse();   // (6.0, 6.0, 5.0, 5.0, 4.0, 3.0, 2.0, 1.0, 1.0)
         * }</pre>
         *
         * @return a new DoubleTuple.DoubleTuple9 with (_9, _8, _7, _6, _5, _4, _3, _2, _1)
         */
        @Override
        public DoubleTuple9 reverse() {
            return new DoubleTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified double value.
         * Comparisons use {@link Double#compare(double, double)} semantics, so {@code NaN}
         * matches {@code NaN} and {@code +0.0} does not match {@code -0.0}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple9 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * boolean found = t.contains(5.0);       // returns true
         * boolean notFound = t.contains(10.0);   // returns false
         *
         * DoubleTuple.DoubleTuple9 withNaN = DoubleTuple.of(1.0, 2.0, Double.NaN, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * boolean hasNaN = withNaN.contains(Double.NaN);   // returns true (NaN matches NaN)
         *
         * DoubleTuple.DoubleTuple9 withNeg = DoubleTuple.of(1.0, 2.0, 3.0, -4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * boolean hasNeg = withNeg.contains(-4.0);   // returns true
         * boolean notNeg = withNeg.contains(4.0);    // returns false
         * }</pre>
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if any of {@code _1} through {@code _9} equals
         *         {@code valueToFind}, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind) || N.equals(_9, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple9 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * double[] sum = {0.0};
         * t.forEach(v -> sum[0] += v);   // sum[0] == 45.0
         *
         * java.util.List<Double> list = new java.util.ArrayList<>();
         * t.forEach(v -> list.add(v));   // list == [1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0]
         *
         * DoubleTuple.DoubleTuple9 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0, -9.0);
         * double[] sumNeg = {0.0};
         * neg.forEach(v -> sumNeg[0] += v);   // sumNeg[0] == -45.0
         *
         * DoubleTuple.DoubleTuple9 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * // t2.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to perform; must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> action) throws E {
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
         * DoubleTuple.DoubleTuple9 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * DoubleTuple.DoubleTuple9 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * assertEquals(t1.hashCode(), t2.hashCode());   // equal tuples have equal hash codes
         *
         * DoubleTuple.DoubleTuple9 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * DoubleTuple.DoubleTuple9 withNaN2 = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * assertEquals(withNaN.hashCode(), withNaN2.hashCode());   // NaN has consistent hash
         *
         * DoubleTuple.DoubleTuple9 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0, -9.0);
         * DoubleTuple.DoubleTuple9 neg2 = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0, -9.0);
         * assertEquals(neg.hashCode(), neg2.hashCode());   // negative values hash consistently
         *
         * DoubleTuple.DoubleTuple9 t3 = DoubleTuple.of(9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0);
         * // t1.hashCode() and t3.hashCode() differ for different element orders (likely)
         * }</pre>
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            int result = Double.hashCode(_1);
            result = 31 * result + Double.hashCode(_2);
            result = 31 * result + Double.hashCode(_3);
            result = 31 * result + Double.hashCode(_4);
            result = 31 * result + Double.hashCode(_5);
            result = 31 * result + Double.hashCode(_6);
            result = 31 * result + Double.hashCode(_7);
            result = 31 * result + Double.hashCode(_8);
            return 31 * result + Double.hashCode(_9);
        }

        /**
         * Compares this tuple to another object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * DoubleTuple.DoubleTuple9 t1 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * DoubleTuple.DoubleTuple9 t2 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * assertTrue(t1.equals(t2));   // same values -> equal
         *
         * DoubleTuple.DoubleTuple9 t3 = DoubleTuple.of(9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0);
         * assertFalse(t1.equals(t3));   // different order -> not equal
         *
         * assertFalse(t1.equals(null));       // null -> not equal
         * assertFalse(t1.equals("string"));   // wrong type -> not equal
         *
         * DoubleTuple.DoubleTuple9 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * DoubleTuple.DoubleTuple9 withNaN2 = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * assertTrue(withNaN.equals(withNaN2));   // NaN equals NaN via Double.compare semantics
         * }</pre>
         *
         * @param obj the object to compare with
         * @return {@code true} if obj is a DoubleTuple.DoubleTuple9 with equal elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final DoubleTuple9 other)) {
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
         * DoubleTuple.DoubleTuple9 t = DoubleTuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * String s = t.toString();   // returns "(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)"
         *
         * DoubleTuple.DoubleTuple9 neg = DoubleTuple.of(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0, -9.0);
         * String sn = neg.toString();   // returns "(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0, -9.0)"
         *
         * DoubleTuple.DoubleTuple9 withNaN = DoubleTuple.of(Double.NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * String sNaN = withNaN.toString();   // returns "(NaN, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)"
         *
         * DoubleTuple.DoubleTuple9 withInf = DoubleTuple.of(Double.POSITIVE_INFINITY, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
         * String sInf = withInf.toString();   // returns "(Infinity, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)"
         * }</pre>
         *
         * @return "(_1, _2, _3, _4, _5, _6, _7, _8, _9)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ")";
        }

        /**
         * Returns the internal array of double elements.
         * The array is lazily initialized on first access.
         *
         * @return a double array containing all elements of this tuple
         */
        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
