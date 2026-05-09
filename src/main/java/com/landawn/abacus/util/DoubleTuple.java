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
import com.landawn.abacus.util.stream.DoubleStream;

/**
 * Base class for immutable tuples of primitive {@code double} values.
 *
 * <p>The nested tuple types model fixed arities from 0 through 9. Factory methods such as
 * {@link #copyOf(double[])} and the {@code of(...)} overloads select the matching subtype, while the
 * base class supplies aggregate, reversal, containment, and functional helper operations.</p>
 *
 * @param <TP> the specific DoubleTuple subtype
 * @see PrimitiveTuple
 * @see FloatTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class DoubleTuple<TP extends DoubleTuple<TP>> extends PrimitiveTuple<TP> {

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
     * double value = single._1;  // 3.14
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
     * double first = pair._1;  // 1.5
     * double second = pair._2;  // 2.5
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
     * double third = triple._3;  // 3.0
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
     * double fourth = tuple._4;  // 4.0
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
     * double sum = tuple.sum();   // 21.0
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
     * This factory method supports arrays with 0 to 9 elements. For empty or {@code null}
     * arrays, returns an empty {@code DoubleTuple<?>}. For arrays with 1-9 elements, returns
     * the corresponding {@code DoubleTuple.DoubleTuple1} through {@code DoubleTuple.DoubleTuple9} instance.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create from array
     * double[] values = {1.0, 2.0, 3.0};
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.copyOf(values);
     *
     * // Empty array returns DoubleTuple<?>
     * DoubleTuple<?> empty = DoubleTuple.copyOf(new double[0]);
     *
     * // Single element
     * DoubleTuple.DoubleTuple1 single = DoubleTuple.copyOf(new double[]{3.14});
     * }</pre>
     *
     * <p><strong>Type note:</strong> the runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of double values (must have length 0-9), may be {@code null}
     * @return a DoubleTuple of appropriate size containing the array values, or an empty DoubleTuple if the array is {@code null} or empty
     * @throws IllegalArgumentException if the array has more than 9 elements
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
     * // Odd number of elements
     * DoubleTuple.DoubleTuple3 tuple3 = DoubleTuple.of(30.0, 10.0, 20.0);
     * double median = tuple3.median();   // 20.0 (middle value when sorted: 10.0, 20.0, 30.0)
     *
     * // Even number of elements
     * DoubleTuple.DoubleTuple4 tuple4 = DoubleTuple.of(1.0, 2.0, 3.0, 4.0);
     * double median2 = tuple4.median();   // 2.0 (lower middle value when sorted)
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
     * Returns a new tuple with the elements in reverse order.
     * <p>
     * This method creates and returns a new tuple instance with all elements in reversed order.
     * The original tuple remains unchanged. For example, a tuple (1.0, 2.0, 3.0) becomes
     * (3.0, 2.0, 1.0) when reversed.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * DoubleTuple.DoubleTuple3 reversed = tuple.reverse();   // (3.0, 2.0, 1.0)
     *
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * DoubleTuple.DoubleTuple2 reversedPair = pair.reverse();   // (2.5, 1.5)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
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
     * array[0] = 5.0;  // Does not modify the original tuple
     *
     * DoubleTuple<?> empty = DoubleTuple.copyOf(new double[0]);
     * double[] emptyArray = empty.toArray();   // []
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
     * list.add(4.0);   // Does not affect the original tuple
     *
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * DoubleList pairList = pair.toList();   // [1.5, 2.5]
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
     * DoubleTuple.DoubleTuple3 tuple = DoubleTuple.of(1.0, 2.0, 3.0);
     * tuple.forEach(d -> System.out.print(d + " "));   // prints "1.0 2.0 3.0 "
     *
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * List<Double> list = new ArrayList<>();
     * pair.forEach(list::add);   // adds 1.5 and 2.5 to the list
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the action to be performed for each element; must not be {@code null}
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception
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
     * DoubleTuple.DoubleTuple2 pair = DoubleTuple.of(1.5, 2.5);
     * long count = pair.stream().filter(d -> d > 2.0).count();   // 1
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
     * Returns a string representation of this tuple.
     * <p>
     * The string representation consists of the tuple elements enclosed in parentheses
     * and separated by commas and spaces, in the format {@code (element1, element2, ...)}.
     * This format provides a clear and readable representation of the tuple's contents.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <ul>
     * <li>{@code (1.0, 2.0, 3.0)} - for a DoubleTuple.DoubleTuple3</li>
     * <li>{@code (1.5, 2.5)} - for a DoubleTuple.DoubleTuple2</li>
     * <li>{@code (3.14)} - for a DoubleTuple.DoubleTuple1</li>
     * <li>{@code ()} - for an empty {@code DoubleTuple<?>}</li>
     * </ul>
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
     * @return the array of double elements stored in this tuple
     */
    protected abstract double[] elements();

    /**
     * An empty DoubleTuple containing no elements.
     * <p>
     * This class represents a tuple with arity 0 (zero elements). It follows the singleton pattern,
     * with a single shared instance returned by {@link DoubleTuple#copyOf(double[])} when called
     * with a {@code null} or zero-length array. The class itself is package-private; callers see it
     * as a {@code DoubleTuple<?>}. The aggregate operation {@link #sum()} returns {@code 0.0};
     * {@link #min()}, {@link #max()}, {@link #median()}, and {@link #average()} all throw
     * {@link NoSuchElementException}.
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
         * @throws NoSuchElementException always, as there are no elements
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
         * @throws NoSuchElementException always, as there are no elements
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
         * @throws NoSuchElementException always, as there are no elements
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
         * @throws NoSuchElementException always, as there are no elements
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
         * @return {@code false} always, as there are no elements
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
         * Returns the internal (empty) array of double elements.
         * Returns the shared {@code N.EMPTY_DOUBLE_ARRAY} singleton.
         *
         * @return a shared empty double array
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
         * @return 1
         */
        @Override
        public int arity() {
            return 1;
        }

        /**
         * Returns the minimum value in this tuple, which is the single element.
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
         * @return the value of {@code _1}
         */
        @Override
        public double max() {
            return _1;
        }

        /**
         * Returns the median value in this tuple, which is the single element.
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
         * @return the value of {@code _1}
         */
        @Override
        public double sum() {
            return _1;
        }

        /**
         * Returns the average of elements in this tuple, which is the single element.
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
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return Double.hashCode(_1);
        }

        /**
         * Compares this tuple to another object for equality.
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
     * A DoubleTuple containing exactly two double values.
     * <p>
     * This class provides direct access to elements through public final fields {@code _1} and {@code _2}.
     * DoubleTuple.DoubleTuple2 offers additional functional methods like {@link #accept(Throwables.DoubleBiConsumer)},
     * {@link #map(Throwables.DoubleBiFunction)}, and {@link #filter(Throwables.DoubleBiPredicate)} that
     * operate on both elements simultaneously.
     * </p>
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
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to perform
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
         * coordinates.accept((x, y) -> System.out.printf("Point: (%.1f, %.1f)%n", x, y));
         * // Prints: Point: (10.5, 20.3)
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the bi-consumer to perform on the two elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception
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
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the bi-function to apply to the two elements, must not be {@code null}
         * @return the result of applying the mapper to _1 and _2 (may be {@code null} if the mapper returns {@code null})
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
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
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the bi-predicate to test the two elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<DoubleTuple2> filter(final Throwables.DoubleBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on both elements.
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
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to perform
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
         * tuple.accept((a, b, c) -> System.out.println("Sum: " + (a + b + c)));
         * // Prints: Sum: 6.0
         *
         * DoubleTuple.DoubleTuple3 dimensions = DoubleTuple.of(5.0, 3.0, 2.0);
         * dimensions.accept((l, w, h) -> System.out.printf("Volume: %.1f%n", l * w * h));
         * // Prints: Volume: 30.0
         *
         * DoubleTuple.DoubleTuple3 rgb = DoubleTuple.of(0.5, 0.7, 0.3);
         * rgb.accept((r, g, b) -> System.out.printf("Color: RGB(%.1f, %.1f, %.1f)%n", r, g, b));
         * // Prints: Color: RGB(0.5, 0.7, 0.3)
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the tri-consumer to perform on the three elements, must not be {@code null}
         * @throws NullPointerException if {@code action} is {@code null}
         * @throws E if the action throws an exception
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
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the tri-function to apply to the three elements, must not be {@code null}
         * @return the result of applying the mapper to _1, _2, and _3 (may be {@code null} if the mapper returns {@code null})
         * @throws NullPointerException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
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
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the tri-predicate to test the three elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws NullPointerException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<DoubleTuple3> filter(final Throwables.DoubleTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code for this tuple based on all three elements.
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
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to perform
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
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind)
                    || N.equals(_5, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to perform
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
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to perform
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
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to perform
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
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to perform
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
         *
         * @param valueToFind the double value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind) || N.equals(_9, valueToFind);
        }

        /**
         * Performs the given action for each element in order.
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to perform
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
