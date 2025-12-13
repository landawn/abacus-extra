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

import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.CharStream;

/**
 * Abstract base class for immutable tuple implementations that hold primitive char values.
 * <p>
 * This class provides common functionality for char-based tuples of various sizes (0 to 9 elements).
 * CharTuple is designed to be a lightweight, type-safe container for multiple char values
 * that can be used as a composite key, return multiple values from a method, or group related
 * char values together. All char tuple implementations extend this class and are immutable by design.
 * </p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Creating tuples
 * CharTuple1 single = CharTuple.of('A');
 * CharTuple2 pair = CharTuple.of('A', 'B');
 * CharTuple3 triple = CharTuple.of('A', 'B', 'C');
 *
 * // Using statistical operations
 * char min = triple.min();   // 'A'
 * char max = triple.max();   // 'C'
 * double avg = triple.average();   // 66.0 (average of ASCII values)
 *
 * // Using functional operations
 * pair.accept((a, b) -> System.out.println(a + " and " + b));
 * String combined = triple.map((a, b, c) -> "" + a + b + c);
 * }</pre>
 *
 * @param <TP> the specific CharTuple subtype
 * @see PrimitiveTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class CharTuple<TP extends CharTuple<TP>> extends PrimitiveTuple<TP> {

    protected char[] elements;

    /**
     * Constructor for subclasses.
     * <p>
     * This constructor is protected to prevent direct instantiation of the abstract class.
     * Subclasses should use this constructor to initialize their instances.
     * </p>
     */
    protected CharTuple() {
    }

    /**
     * Creates a CharTuple1 containing a single char value.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple1 tuple = CharTuple.of('A');
     * char value = tuple._1;  // 'A'
     * }</pre>
     *
     * @param _1 the char value to store in the tuple
     * @return a new CharTuple1 containing the specified value
     */
    public static CharTuple1 of(final char _1) {
        return new CharTuple1(_1);
    }

    /**
     * Creates a CharTuple2 containing two char values.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple2 tuple = CharTuple.of('A', 'B');
     * char first = tuple._1;  // 'A'
     * char second = tuple._2;  // 'B'
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @return a new CharTuple2 containing the specified values
     */
    public static CharTuple2 of(final char _1, final char _2) {
        return new CharTuple2(_1, _2);
    }

    /**
     * Creates a CharTuple3 containing three char values.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * char third = tuple._3;  // 'C'
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @return a new CharTuple3 containing the specified values
     */
    public static CharTuple3 of(final char _1, final char _2, final char _3) {
        return new CharTuple3(_1, _2, _3);
    }

    /**
     * Creates a CharTuple4 containing four char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
     * char fourth = tuple._4;  // 'D'
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @param _4 the fourth char value
     * @return a new CharTuple4 containing the specified values
     */
    public static CharTuple4 of(final char _1, final char _2, final char _3, final char _4) {
        return new CharTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a CharTuple5 containing five char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
     * char median = tuple.median();   // 'C' (middle value when sorted)
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @param _4 the fourth char value
     * @param _5 the fifth char value
     * @return a new CharTuple5 containing the specified values
     */
    public static CharTuple5 of(final char _1, final char _2, final char _3, final char _4, final char _5) {
        return new CharTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a CharTuple6 containing six char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
     * int sum = tuple.sum();   // Sum of ASCII values
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @param _4 the fourth char value
     * @param _5 the fifth char value
     * @param _6 the sixth char value
     * @return a new CharTuple6 containing the specified values
     */
    public static CharTuple6 of(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6) {
        return new CharTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a CharTuple7 containing seven char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple7 tuple = CharTuple.of('M', 'O', 'N', 'D', 'A', 'Y', 'S');
     * CharTuple7 reversed = tuple.reverse();   // ('S', 'Y', 'A', 'D', 'N', 'O', 'M')
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @param _4 the fourth char value
     * @param _5 the fifth char value
     * @param _6 the sixth char value
     * @param _7 the seventh char value
     * @return a new CharTuple7 containing the specified values
     */
    public static CharTuple7 of(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6, final char _7) {
        return new CharTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a CharTuple8 containing eight char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
     * boolean hasX = tuple.contains('X');   // false
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @param _4 the fourth char value
     * @param _5 the fifth char value
     * @param _6 the sixth char value
     * @param _7 the seventh char value
     * @param _8 the eighth char value
     * @return a new CharTuple8 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more char values
     */
    @Deprecated
    public static CharTuple8 of(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6, final char _7, final char _8) {
        return new CharTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a CharTuple9 containing nine char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
     * char[] array = tuple.toArray();   // ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I']
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @param _4 the fourth char value
     * @param _5 the fifth char value
     * @param _6 the sixth char value
     * @param _7 the seventh char value
     * @param _8 the eighth char value
     * @param _9 the ninth char value
     * @return a new CharTuple9 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more char values
     */
    @Deprecated
    public static CharTuple9 of(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6, final char _7, final char _8,
            final char _9) {
        return new CharTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a CharTuple from an array of char values.
     * <p>
     * The size of the returned tuple depends on the length of the input array.
     * This factory method supports arrays with 0 to 9 elements. For empty or null
     * arrays, returns an empty CharTuple0. For arrays with 1-9 elements, returns
     * the corresponding CharTuple1-9 instance.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create from array
     * char[] values = {'A', 'B', 'C'};
     * CharTuple3 tuple = CharTuple.create(values);
     *
     * // Empty array returns CharTuple0
     * CharTuple0 empty = CharTuple.create(new char[0]);
     *
     * // Single element
     * CharTuple1 single = CharTuple.create(new char[]{'X'});
     * }</pre>
     *
     * @param <TP> the specific CharTuple subtype to return
     * @param a the array of char values (must have length 0-9), may be {@code null}
     * @return a CharTuple of appropriate size containing the array values, or an empty CharTuple if the array is null or empty
     * @throws IllegalArgumentException if the array has more than 9 elements
     */
    @SuppressWarnings("deprecation")
    public static <TP extends CharTuple<TP>> TP create(final char[] a) {
        if (a == null || a.length == 0) {
            return (TP) CharTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) CharTuple.of(a[0]);

            case 2:
                return (TP) CharTuple.of(a[0], a[1]);

            case 3:
                return (TP) CharTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) CharTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) CharTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) CharTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) CharTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) CharTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) CharTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     * Returns the minimum char value in this tuple.
     * <p>
     * This method finds and returns the smallest char value among all elements
     * in the tuple. For tuples with a single element, returns that element.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
     * char min = tuple.min();   // 'A'
     *
     * CharTuple2 pair = CharTuple.of('B', 'D');
     * char minPair = pair.min();   // 'B'
     * }</pre>
     *
     * @return the minimum char value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public char min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum char value in this tuple.
     * <p>
     * This method finds and returns the largest char value among all elements
     * in the tuple. For tuples with a single element, returns that element.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
     * char max = tuple.max();   // 'Z'
     *
     * CharTuple2 pair = CharTuple.of('B', 'D');
     * char maxPair = pair.max();   // 'D'
     * }</pre>
     *
     * @return the maximum char value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public char max() {
        return N.max(elements());
    }

    /**
     * Returns the median char value in this tuple.
     * <p>
     * The median is the middle value when all elements are sorted. For tuples with
     * an odd number of elements, returns the exact middle value. For tuples with an
     * even number of elements, returns the lower of the two middle values.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Odd number of elements
     * CharTuple3 tuple3 = CharTuple.of('Z', 'A', 'M');
     * char median = tuple3.median();   // 'M' (middle value when sorted: A, M, Z)
     *
     * // Even number of elements
     * CharTuple4 tuple4 = CharTuple.of('A', 'B', 'C', 'D');
     * char median2 = tuple4.median();   // 'B' (lower middle value)
     * }</pre>
     *
     * @return the median char value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public char median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all char values in this tuple as an integer.
     * <p>
     * This method calculates the sum by adding the numeric values of all char elements together.
     * The result is returned as an int to prevent overflow issues that could occur if the sum
     * exceeds the char range (0 to 65535).
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');   // 'A'=65, 'B'=66, 'C'=67
     * int sum = tuple.sum();                            // 198
     *
     * CharTuple2 pair = CharTuple.of('X', 'Y');         // 'X'=88, 'Y'=89
     * int pairSum = pair.sum();                         // 177
     * }</pre>
     *
     * @return the sum of all char values in this tuple as an integer
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all char values in this tuple as a double.
     * <p>
     * Note: The result is returned as a double to preserve precision. The average is
     * calculated by converting char values to double during computation.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');   // 'A'=65, 'B'=66, 'C'=67
     * double avg = tuple.average();                     // 66.0
     *
     * CharTuple2 pair = CharTuple.of('A', 'D');         // 'A'=65, 'D'=68
     * double avgPair = pair.average();                  // 66.5
     * }</pre>
     *
     * @return the average of all char values in this tuple as a double
     * @throws NoSuchElementException if the tuple is empty
     */
    public double average() {
        return N.average(elements());
    }

    /**
     * Returns a new tuple with the elements in reverse order.
     * <p>
     * This method creates and returns a new tuple instance with all elements in reversed order.
     * The original tuple remains unchanged. For example, a tuple ('A', 'B', 'C') becomes
     * ('C', 'B', 'A') when reversed.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * CharTuple3 reversed = tuple.reverse();   // ('C', 'B', 'A')
     *
     * CharTuple2 pair = CharTuple.of('X', 'Y');
     * CharTuple2 reversedPair = pair.reverse();   // ('Y', 'X')
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified char value.
     * <p>
     * This method performs a linear search through all elements in the tuple to determine
     * if any element matches the specified value. Returns {@code true} if at least one
     * element equals the search value, {@code false} otherwise.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * boolean hasB = tuple.contains('B');   // true
     * boolean hasZ = tuple.contains('Z');   // false
     *
     * CharTuple2 pair = CharTuple.of('X', 'Y');
     * boolean hasX = pair.contains('X');   // true
     * boolean hasA = pair.contains('A');   // false
     * }</pre>
     *
     * @param valueToFind the char value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(char valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * <p>
     * This method creates a defensive copy of the internal array. Modifications to the
     * returned array do not affect the tuple since tuples are immutable.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * char[] array = tuple.toArray();   // ['A', 'B', 'C']
     * array[0] = 'X';  // Does not modify the original tuple
     *
     * CharTuple0 empty = CharTuple.create(new char[0]);
     * char[] emptyArray = empty.toArray();   // []
     * }</pre>
     *
     * @return a new char array containing all tuple elements
     */
    public char[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new CharList containing all elements of this tuple.
     * <p>
     * This method converts the tuple into a mutable CharList. The returned list is a new
     * instance, and modifications to it do not affect the original tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * CharList list = tuple.toList();
     * list.add('D');   // Does not affect the original tuple
     *
     * CharTuple2 pair = CharTuple.of('X', 'Y');
     * CharList pairList = pair.toList();   // [X, Y]
     * }</pre>
     *
     * @return a new CharList containing all tuple elements
     */
    public CharList toList() {
        return CharList.of(elements().clone());
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
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * tuple.forEach(ch -> System.out.print(ch + " "));   // prints "A B C "
     *
     * CharTuple2 pair = CharTuple.of('X', 'Y');
     * List<Character> chars = new ArrayList<>();
     * pair.forEach(chars::add);   // adds 'X' and 'Y' to the list
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the consumer
     * @param consumer the action to be performed for each element, must not be {@code null}
     * @throws E if the consumer throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.CharConsumer<E> consumer) throws E {
        for (final char e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns a CharStream of all elements in this tuple.
     * <p>
     * This method creates a sequential CharStream with all elements from the tuple.
     * The stream provides a functional programming interface for processing the tuple elements
     * through operations like filter, map, and reduce.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * int sum = tuple.stream().sum();   // 198 (sum of ASCII values)
     *
     * CharTuple2 pair = CharTuple.of('a', 'b');
     * long count = pair.stream().filter(c -> c > 'a').count();   // 1
     * }</pre>
     *
     * @return a CharStream containing all tuple elements
     */
    public CharStream stream() {
        return CharStream.of(elements());
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
     * <li>They are of the exact same class (e.g., both CharTuple2)</li>
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
            return N.equals(elements(), ((CharTuple<TP>) obj).elements());
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
     * <li>{@code (A, B, C)} - for a CharTuple3</li>
     * <li>{@code (X, Y)} - for a CharTuple2</li>
     * <li>{@code (A)} - for a CharTuple1</li>
     * <li>{@code ()} - for an empty CharTuple0</li>
     * </ul>
     *
     * @return a string representation of this tuple
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    /**
     * Returns the internal array containing all char elements in this tuple.
     * <p>
     * This method provides direct access to the underlying char array used by the tuple.
     * Subclasses implement this method to return their internal storage. The returned array
     * should not be modified directly as it may be shared or cached by the tuple implementation.
     * For safe array access, use {@link #toArray()} instead, which returns a defensive copy.
     * </p>
     *
     * @return the internal array of char elements
     */
    protected abstract char[] elements();

    /**
     * An empty CharTuple containing no elements.
     * <p>
     * This class represents a tuple with arity 0 (zero elements). It follows the singleton pattern,
     * with a single shared instance accessed via {@code CharTuple.create(new char[0])} or returned
     * when creating tuples from null/empty arrays. All statistical operations on CharTuple0 either
     * return 0 (for sum) or throw {@link NoSuchElementException} (for min, max, median, average).
     * </p>
     */
    static final class CharTuple0 extends CharTuple<CharTuple0> {

        private static final CharTuple0 EMPTY = new CharTuple0();

        CharTuple0() {
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
         * Returns the minimum char value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple<CharTuple0> emptyTuple = CharTuple.create(new char[0]);
         * char min = emptyTuple.min();   // throws NoSuchElementException
         * }</pre>
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public char min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the maximum char value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple<CharTuple0> emptyTuple = CharTuple.create(new char[0]);
         * char max = emptyTuple.max();   // throws NoSuchElementException
         * }</pre>
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public char max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the median char value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple<CharTuple0> emptyTuple = CharTuple.create(new char[0]);
         * char median = emptyTuple.median();   // throws NoSuchElementException
         * }</pre>
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public char median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        /**
         * Returns the sum of all char values in this tuple.
         * Since this tuple is empty, this method always returns 0.
         *
         * @return 0
         */
        @Override
        public int sum() {
            return 0;
        }

        /**
         * Returns the average of all char values in this tuple.
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
         * @return this CharTuple0 instance
         */
        @Override
        public CharTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified char value.
         * Since this tuple is empty, this method always returns false.
         *
         * @param valueToFind the char value to search for
         * @return {@code false} always, because the tuple is empty
         */
        @Override
        public boolean contains(final char valueToFind) {
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
        protected char[] elements() {
            return N.EMPTY_CHAR_ARRAY;
        }
    }

    /**
     * A CharTuple containing exactly one char element.
     * <p>
     * This class provides direct access to the single element through the public final field {@code _1}.
     * For single-element tuples, all statistical operations (min, max, median, sum, average) return
     * or are based on that single element.
     * </p>
     */
    public static final class CharTuple1 extends CharTuple<CharTuple1> {

        /** The single char value stored in this tuple. */
        public final char _1;

        CharTuple1() {
            this((char) 0);
        }

        CharTuple1(final char _1) {
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
         * Returns the minimum char value in this tuple.
         * Since this tuple contains only one element, it returns that element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple1 tuple = CharTuple.of('A');
         * char min = tuple.min();   // 'A'
         * }</pre>
         *
         * @return the single char value in this tuple
         */
        @Override
        public char min() {
            return _1;
        }

        /**
         * Returns the maximum char value in this tuple.
         * Since this tuple contains only one element, it returns that element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple1 tuple = CharTuple.of('A');
         * char max = tuple.max();   // 'A'
         * }</pre>
         *
         * @return the single char value in this tuple
         */
        @Override
        public char max() {
            return _1;
        }

        /**
         * Returns the median char value in this tuple.
         * Since this tuple contains only one element, it returns that element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple1 tuple = CharTuple.of('A');
         * char median = tuple.median();   // 'A'
         * }</pre>
         *
         * @return the single char value in this tuple
         */
        @Override
        public char median() {
            return _1;
        }

        /**
         * Returns the sum of all char values in this tuple.
         * Since this tuple contains only one element, it returns the numeric value of that element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple1 tuple = CharTuple.of('A');   // 'A' = 65
         * int sum = tuple.sum();                  // 65
         * }</pre>
         *
         * @return the numeric value of the single char in this tuple
         */
        @Override
        public int sum() {
            return _1;
        }

        /**
         * Returns the average of all char values in this tuple.
         * Since this tuple contains only one element, it returns the numeric value of that element as a double.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple1 tuple = CharTuple.of('A');   // 'A' = 65
         * double avg = tuple.average();           // 65.0
         * }</pre>
         *
         * @return the numeric value of the single char in this tuple as a double
         */
        @Override
        public double average() {
            return _1;
        }

        /**
         * Returns a new CharTuple1 with the same element.
         * Since this tuple has only one element, reversing has no effect.
         *
         * @return a new CharTuple1 with the same element
         */
        @Override
        public CharTuple1 reverse() {
            return new CharTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * @param valueToFind the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char valueToFind) {
            return _1 == valueToFind;
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return the numeric value of the char element
         */
        @Override
        public int hashCode() {
            return _1;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a CharTuple1 with the same element
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final CharTuple1 other)) {
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

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A CharTuple containing exactly two char elements.
     * <p>
     * This class provides direct access to elements through public final fields {@code _1} and {@code _2}.
     * CharTuple2 offers additional functional methods like {@link #accept(Throwables.CharBiConsumer)},
     * {@link #map(Throwables.CharBiFunction)}, and {@link #filter(Throwables.CharBiPredicate)} that
     * operate on both elements simultaneously.
     * </p>
     */
    public static final class CharTuple2 extends CharTuple<CharTuple2> {

        /** The first char value stored in this tuple. */
        public final char _1;
        /** The second char value stored in this tuple. */
        public final char _2;

        CharTuple2() {
            this((char) 0, (char) 0);
        }

        CharTuple2(final char _1, final char _2) {
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
         * Returns the minimum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple2 tuple = CharTuple.of('Z', 'A');
         * char min = tuple.min();   // 'A'
         * }</pre>
         *
         * @return the smaller of the two char values
         */
        @Override
        public char min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple2 tuple = CharTuple.of('Z', 'A');
         * char max = tuple.max();   // 'Z'
         * }</pre>
         *
         * @return the larger of the two char values
         */
        @Override
        public char max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the median char value in this tuple.
         * For a tuple of two elements, returns the lower value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple2 tuple = CharTuple.of('B', 'D');
         * char median = tuple.median();   // 'B' (lower value)
         * }</pre>
         *
         * @return the median char value in this tuple
         */
        @Override
        public char median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple2 tuple = CharTuple.of('A', 'B');   // 'A'=65, 'B'=66
         * int sum = tuple.sum();                       // 131
         * }</pre>
         *
         * @return the sum of the numeric values of both chars
         */
        @Override
        public int sum() {
            return N.sum(_1, _2);
        }

        /**
         * Returns the average of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple2 tuple = CharTuple.of('A', 'C');   // 'A'=65, 'C'=67
         * double avg = tuple.average();                // 66.0
         * }</pre>
         *
         * @return the average of the numeric values of both chars
         */
        @Override
        public double average() {
            return N.average(_1, _2);
        }

        /**
         * Returns a new CharTuple2 with the elements in reverse order.
         *
         * @return a new CharTuple2 with the elements in reverse order
         */
        @Override
        public CharTuple2 reverse() {
            return new CharTuple2(_2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * @param valueToFind the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         * Applies the given action to both elements of this tuple.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple2 tuple = CharTuple.of('A', 'B');
         * tuple.accept((a, b) -> System.out.println(a + " and " + b));   // prints "A and B"
         * }</pre>
         * 
         * @param <E> the type of exception that may be thrown
         * @param action the action to be performed on both elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.CharBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given function to both elements of this tuple and returns the result.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple2 tuple = CharTuple.of('A', 'B');
         * String result = tuple.map((a, b) -> "" + a + b);   // "AB"
         * }</pre>
         * 
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown
         * @param mapper the mapping function to apply to both elements
         * @return the result of applying the mapping function
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.CharBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if it matches the given predicate,
         * otherwise returns an empty Optional.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple2 tuple = CharTuple.of('A', 'B');
         * Optional<CharTuple2> result = tuple.filter((a, b) -> a < b);   // Optional containing the tuple
         * }</pre>
         * 
         * @param <E> the type of exception that may be thrown
         * @param predicate the predicate to test both elements
         * @return an Optional containing this tuple if the predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<CharTuple2> filter(final Throwables.CharBiPredicate<E> predicate) throws E {
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
         * @return {@code true} if the specified object is a CharTuple2 with the same elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final CharTuple2 other)) {
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

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A CharTuple containing exactly three char elements.
     * <p>
     * This class provides direct access to elements through public final fields {@code _1}, {@code _2}, and {@code _3}.
     * CharTuple3 offers additional functional methods like {@link #accept(Throwables.CharTriConsumer)},
     * {@link #map(Throwables.CharTriFunction)}, and {@link #filter(Throwables.CharTriPredicate)} that
     * operate on all three elements simultaneously.
     * </p>
     */
    public static final class CharTuple3 extends CharTuple<CharTuple3> {

        /** The first char value stored in this tuple. */
        public final char _1;
        /** The second char value stored in this tuple. */
        public final char _2;
        /** The third char value stored in this tuple. */
        public final char _3;

        CharTuple3() {
            this((char) 0, (char) 0, (char) 0);
        }

        CharTuple3(final char _1, final char _2, final char _3) {
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
         * Returns the minimum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
         * char min = tuple.min();   // 'A'
         * }</pre>
         *
         * @return the smallest of the three char values
         */
        @Override
        public char min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
         * char max = tuple.max();   // 'Z'
         * }</pre>
         *
         * @return the largest of the three char values
         */
        @Override
        public char max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the median char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
         * char median = tuple.median();   // 'M' (middle value when sorted)
         * }</pre>
         *
         * @return the middle char value when sorted
         */
        @Override
        public char median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');   // 'A'=65, 'B'=66, 'C'=67
         * int sum = tuple.sum();                            // 198
         * }</pre>
         *
         * @return the sum of the numeric values of all three chars
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3);
        }

        /**
         * Returns the average of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');   // 'A'=65, 'B'=66, 'C'=67
         * double avg = tuple.average();                     // 66.0
         * }</pre>
         *
         * @return the average of the numeric values of all three chars
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3);
        }

        /**
         * Returns a new CharTuple3 with the elements in reverse order.
         *
         * @return a new CharTuple3 with the elements in reverse order
         */
        @Override
        public CharTuple3 reverse() {
            return new CharTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * @param valueToFind the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
        }

        /**
         * Applies the given action to all three elements of this tuple.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
         * tuple.accept((a, b, c) -> System.out.println(a + ", " + b + ", " + c));
         * }</pre>
         * 
         * @param <E> the type of exception that may be thrown
         * @param action the action to be performed on all three elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.CharTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given function to all three elements of this tuple and returns the result.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
         * String result = tuple.map((a, b, c) -> "" + a + b + c);   // "ABC"
         * }</pre>
         * 
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown
         * @param mapper the mapping function to apply to all three elements
         * @return the result of applying the mapping function
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.CharTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if it matches the given predicate,
         * otherwise returns an empty Optional.
         * 
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
         * Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c);   // Optional containing the tuple
         * }</pre>
         * 
         * @param <E> the type of exception that may be thrown
         * @param predicate the predicate to test all three elements
         * @return an Optional containing this tuple if the predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<CharTuple3> filter(final Throwables.CharTriPredicate<E> predicate) throws E {
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
         * @return {@code true} if the specified object is a CharTuple3 with the same elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final CharTuple3 other)) {
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

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A CharTuple containing exactly four char elements.
     * <p>
     * This class provides direct access to elements through public final fields
     * {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple4 tuple = CharTuple.of('a', 'b', 'c', 'd');
     * }</pre>
     */
    public static final class CharTuple4 extends CharTuple<CharTuple4> {

        /** The first char value stored in this tuple. */
        public final char _1;
        /** The second char value stored in this tuple. */
        public final char _2;
        /** The third char value stored in this tuple. */
        public final char _3;
        /** The fourth char value stored in this tuple. */
        public final char _4;

        CharTuple4() {
            this((char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple4(final char _1, final char _2, final char _3, final char _4) {
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
         * CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
         * int size = tuple.arity();   // 4
         * }</pre>
         *
         * @return 4
         */
        @Override
        public int arity() {
            return 4;
        }

        /**
         * Returns the minimum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple4 tuple = CharTuple.of('D', 'A', 'C', 'B');
         * char min = tuple.min();   // 'A'
         * }</pre>
         *
         * @return the smallest of the four char values
         */
        @Override
        public char min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple4 tuple = CharTuple.of('D', 'A', 'C', 'B');
         * char max = tuple.max();   // 'D'
         * }</pre>
         *
         * @return the largest of the four char values
         */
        @Override
        public char max() {
            return N.max(elements());
        }

        /**
         * Returns the median char value in this tuple.
         * For a tuple of four elements, returns the lower of the two middle values when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
         * char median = tuple.median();   // 'B' (lower middle value)
         * }</pre>
         *
         * @return the median char value in this tuple
         */
        @Override
        public char median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');   // 'A'=65, 'B'=66, 'C'=67, 'D'=68
         * int sum = tuple.sum();                                 // 266
         * }</pre>
         *
         * @return the sum of the numeric values of all four chars
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');   // 'A'=65, 'B'=66, 'C'=67, 'D'=68
         * double avg = tuple.average();                          // 66.5
         * }</pre>
         *
         * @return the average of the numeric values of all four chars
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new CharTuple4 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
         * CharTuple4 reversed = tuple.reverse();   // ('D', 'C', 'B', 'A')
         * }</pre>
         *
         * @return a new CharTuple4 with the elements in reverse order
         */
        @Override
        public CharTuple4 reverse() {
            return new CharTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
         * boolean hasB = tuple.contains('B');   // true
         * boolean hasZ = tuple.contains('Z');   // false
         * }</pre>
         *
         * @param valueToFind the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
        }

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A CharTuple containing exactly five char elements.
     * <p>
     * This class provides direct access to elements through public final fields
     * {@code _1} through {@code _5}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple5 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e');
     * }</pre>
     */
    public static final class CharTuple5 extends CharTuple<CharTuple5> {

        /** The first char value stored in this tuple. */
        public final char _1;
        /** The second char value stored in this tuple. */
        public final char _2;
        /** The third char value stored in this tuple. */
        public final char _3;
        /** The fourth char value stored in this tuple. */
        public final char _4;
        /** The fifth char value stored in this tuple. */
        public final char _5;

        CharTuple5() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple5(final char _1, final char _2, final char _3, final char _4, final char _5) {
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
         * CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * int size = tuple.arity();   // 5
         * }</pre>
         *
         * @return 5
         */
        @Override
        public int arity() {
            return 5;
        }

        /**
         * Returns the minimum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple5 tuple = CharTuple.of('E', 'A', 'C', 'B', 'D');
         * char min = tuple.min();   // 'A'
         * }</pre>
         *
         * @return the smallest of the five char values
         */
        @Override
        public char min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple5 tuple = CharTuple.of('E', 'A', 'C', 'B', 'D');
         * char max = tuple.max();   // 'E'
         * }</pre>
         *
         * @return the largest of the five char values
         */
        @Override
        public char max() {
            return N.max(elements());
        }

        /**
         * Returns the median char value in this tuple.
         * For a tuple of five elements, returns the exact middle value when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple5 tuple = CharTuple.of('E', 'A', 'C', 'B', 'D');
         * char median = tuple.median();   // 'C' (middle value when sorted: A, B, C, D, E)
         * }</pre>
         *
         * @return the median char value in this tuple
         */
        @Override
        public char median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');   // 65, 66, 67, 68, 69
         * int sum = tuple.sum();                                      // 335
         * }</pre>
         *
         * @return the sum of the numeric values of all five chars
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');   // 65, 66, 67, 68, 69
         * double avg = tuple.average();                               // 67.0
         * }</pre>
         *
         * @return the average of the numeric values of all five chars
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new CharTuple5 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * CharTuple5 reversed = tuple.reverse();   // ('E', 'D', 'C', 'B', 'A')
         * }</pre>
         *
         * @return a new CharTuple5 with the elements in reverse order
         */
        @Override
        public CharTuple5 reverse() {
            return new CharTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * boolean hasC = tuple.contains('C');   // true
         * boolean hasZ = tuple.contains('Z');   // false
         * }</pre>
         *
         * @param valueToFind the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
        }

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A CharTuple containing exactly six char elements.
     * <p>
     * This class provides direct access to elements through public final fields
     * {@code _1} through {@code _6}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple6 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');
     * }</pre>
     */
    public static final class CharTuple6 extends CharTuple<CharTuple6> {

        /** The first char value stored in this tuple. */
        public final char _1;
        /** The second char value stored in this tuple. */
        public final char _2;
        /** The third char value stored in this tuple. */
        public final char _3;
        /** The fourth char value stored in this tuple. */
        public final char _4;
        /** The fifth char value stored in this tuple. */
        public final char _5;
        /** The sixth char value stored in this tuple. */
        public final char _6;

        CharTuple6() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple6(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6) {
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
         * CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * int size = tuple.arity();   // 6
         * }</pre>
         *
         * @return 6
         */
        @Override
        public int arity() {
            return 6;
        }

        /**
         * Returns the minimum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple6 tuple = CharTuple.of('F', 'A', 'C', 'E', 'B', 'D');
         * char min = tuple.min();   // 'A'
         * }</pre>
         *
         * @return the smallest of the six char values
         */
        @Override
        public char min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple6 tuple = CharTuple.of('F', 'A', 'C', 'E', 'B', 'D');
         * char max = tuple.max();   // 'F'
         * }</pre>
         *
         * @return the largest of the six char values
         */
        @Override
        public char max() {
            return N.max(elements());
        }

        /**
         * Returns the median char value in this tuple.
         * For a tuple of six elements, returns the lower of the two middle values when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * char median = tuple.median();   // 'C' (lower middle value when sorted)
         * }</pre>
         *
         * @return the median char value in this tuple
         */
        @Override
        public char median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');   // 65, 66, 67, 68, 69, 70
         * int sum = tuple.sum();                                           // 405
         * }</pre>
         *
         * @return the sum of the numeric values of all six chars
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');   // 65, 66, 67, 68, 69, 70
         * double avg = tuple.average();                                    // 67.5
         * }</pre>
         *
         * @return the average of the numeric values of all six chars
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new CharTuple6 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * CharTuple6 reversed = tuple.reverse();   // ('F', 'E', 'D', 'C', 'B', 'A')
         * }</pre>
         *
         * @return a new CharTuple6 with the elements in reverse order
         */
        @Override
        public CharTuple6 reverse() {
            return new CharTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * boolean hasD = tuple.contains('D');   // true
         * boolean hasZ = tuple.contains('Z');   // false
         * }</pre>
         *
         * @param valueToFind the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
        }

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A CharTuple containing exactly seven char elements.
     * <p>
     * This class provides direct access to elements through public final fields
     * {@code _1} through {@code _7}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple7 tuple = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
     * }</pre>
     */
    public static final class CharTuple7 extends CharTuple<CharTuple7> {

        /** The first char value stored in this tuple. */
        public final char _1;
        /** The second char value stored in this tuple. */
        public final char _2;
        /** The third char value stored in this tuple. */
        public final char _3;
        /** The fourth char value stored in this tuple. */
        public final char _4;
        /** The fifth char value stored in this tuple. */
        public final char _5;
        /** The sixth char value stored in this tuple. */
        public final char _6;
        /** The seventh char value stored in this tuple. */
        public final char _7;

        CharTuple7() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple7(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6, final char _7) {
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
         * CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * int size = tuple.arity();   // 7
         * }</pre>
         *
         * @return 7
         */
        @Override
        public int arity() {
            return 7;
        }

        /**
         * Returns the minimum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple7 tuple = CharTuple.of('G', 'A', 'C', 'E', 'B', 'D', 'F');
         * char min = tuple.min();   // 'A'
         * }</pre>
         *
         * @return the smallest of the seven char values
         */
        @Override
        public char min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple7 tuple = CharTuple.of('G', 'A', 'C', 'E', 'B', 'D', 'F');
         * char max = tuple.max();   // 'G'
         * }</pre>
         *
         * @return the largest of the seven char values
         */
        @Override
        public char max() {
            return N.max(elements());
        }

        /**
         * Returns the median char value in this tuple.
         * For a tuple of seven elements, returns the exact middle value when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple7 tuple = CharTuple.of('G', 'A', 'C', 'E', 'B', 'D', 'F');
         * char median = tuple.median();   // 'D' (middle value when sorted: A, B, C, D, E, F, G)
         * }</pre>
         *
         * @return the median char value in this tuple
         */
        @Override
        public char median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');   // 65, 66, 67, 68, 69, 70, 71
         * int sum = tuple.sum();                                                // 476
         * }</pre>
         *
         * @return the sum of the numeric values of all seven chars
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');   // 65, 66, 67, 68, 69, 70, 71
         * double avg = tuple.average();                                         // 68.0
         * }</pre>
         *
         * @return the average of the numeric values of all seven chars
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new CharTuple7 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * CharTuple7 reversed = tuple.reverse();   // ('G', 'F', 'E', 'D', 'C', 'B', 'A')
         * }</pre>
         *
         * @return a new CharTuple7 with the elements in reverse order
         */
        @Override
        public CharTuple7 reverse() {
            return new CharTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * boolean hasE = tuple.contains('E');   // true
         * boolean hasZ = tuple.contains('Z');   // false
         * }</pre>
         *
         * @param valueToFind the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
            consumer.accept(_7);
        }

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A CharTuple containing exactly eight char elements.
     * <p>
     * This class provides direct access to elements through public final fields
     * {@code _1} through {@code _8}.
     * </p>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more char values
     */
    @Deprecated
    public static final class CharTuple8 extends CharTuple<CharTuple8> {

        /** The first char value stored in this tuple. */
        public final char _1;
        /** The second char value stored in this tuple. */
        public final char _2;
        /** The third char value stored in this tuple. */
        public final char _3;
        /** The fourth char value stored in this tuple. */
        public final char _4;
        /** The fifth char value stored in this tuple. */
        public final char _5;
        /** The sixth char value stored in this tuple. */
        public final char _6;
        /** The seventh char value stored in this tuple. */
        public final char _7;
        /** The eighth char value stored in this tuple. */
        public final char _8;

        CharTuple8() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple8(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6, final char _7, final char _8) {
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
         * CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * int size = tuple.arity();   // 8
         * }</pre>
         *
         * @return 8
         */
        @Override
        public int arity() {
            return 8;
        }

        /**
         * Returns the minimum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple8 tuple = CharTuple.of('H', 'A', 'C', 'E', 'B', 'D', 'F', 'G');
         * char min = tuple.min();   // 'A'
         * }</pre>
         *
         * @return the smallest of the eight char values
         */
        @Override
        public char min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple8 tuple = CharTuple.of('H', 'A', 'C', 'E', 'B', 'D', 'F', 'G');
         * char max = tuple.max();   // 'H'
         * }</pre>
         *
         * @return the largest of the eight char values
         */
        @Override
        public char max() {
            return N.max(elements());
        }

        /**
         * Returns the median char value in this tuple.
         * For a tuple of eight elements, returns the lower of the two middle values when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * char median = tuple.median();   // 'D' (lower middle value when sorted)
         * }</pre>
         *
         * @return the median char value in this tuple
         */
        @Override
        public char median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');   // 65-72
         * int sum = tuple.sum();                                                     // 548
         * }</pre>
         *
         * @return the sum of the numeric values of all eight chars
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');   // 65-72
         * double avg = tuple.average();                                              // 68.5
         * }</pre>
         *
         * @return the average of the numeric values of all eight chars
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new CharTuple8 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * CharTuple8 reversed = tuple.reverse();   // ('H', 'G', 'F', 'E', 'D', 'C', 'B', 'A')
         * }</pre>
         *
         * @return a new CharTuple8 with the elements in reverse order
         */
        @Override
        public CharTuple8 reverse() {
            return new CharTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * boolean hasF = tuple.contains('F');   // true
         * boolean hasZ = tuple.contains('Z');   // false
         * }</pre>
         *
         * @param valueToFind the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
            consumer.accept(_7);
            consumer.accept(_8);
        }

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A CharTuple containing exactly nine char elements.
     * <p>
     * This class provides direct access to elements through public final fields
     * {@code _1} through {@code _9}.
     * </p>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more char values
     */
    @Deprecated
    public static final class CharTuple9 extends CharTuple<CharTuple9> {

        /** The first char value stored in this tuple. */
        public final char _1;
        /** The second char value stored in this tuple. */
        public final char _2;
        /** The third char value stored in this tuple. */
        public final char _3;
        /** The fourth char value stored in this tuple. */
        public final char _4;
        /** The fifth char value stored in this tuple. */
        public final char _5;
        /** The sixth char value stored in this tuple. */
        public final char _6;
        /** The seventh char value stored in this tuple. */
        public final char _7;
        /** The eighth char value stored in this tuple. */
        public final char _8;
        /** The ninth char value stored in this tuple. */
        public final char _9;

        CharTuple9() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple9(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6, final char _7, final char _8, final char _9) {
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
         * CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * int size = tuple.arity();   // 9
         * }</pre>
         *
         * @return 9
         */
        @Override
        public int arity() {
            return 9;
        }

        /**
         * Returns the minimum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple9 tuple = CharTuple.of('I', 'A', 'C', 'E', 'B', 'D', 'F', 'G', 'H');
         * char min = tuple.min();   // 'A'
         * }</pre>
         *
         * @return the smallest of the nine char values
         */
        @Override
        public char min() {
            return N.min(elements());
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple9 tuple = CharTuple.of('I', 'A', 'C', 'E', 'B', 'D', 'F', 'G', 'H');
         * char max = tuple.max();   // 'I'
         * }</pre>
         *
         * @return the largest of the nine char values
         */
        @Override
        public char max() {
            return N.max(elements());
        }

        /**
         * Returns the median char value in this tuple.
         * For a tuple of nine elements, returns the exact middle value when sorted.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple9 tuple = CharTuple.of('I', 'A', 'C', 'E', 'B', 'D', 'F', 'G', 'H');
         * char median = tuple.median();   // 'E' (middle value when sorted: A, B, C, D, E, F, G, H, I)
         * }</pre>
         *
         * @return the median char value in this tuple
         */
        @Override
        public char median() {
            return N.median(elements());
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');   // 65-73
         * int sum = tuple.sum();                                                          // 621
         * }</pre>
         *
         * @return the sum of the numeric values of all nine chars
         */
        @Override
        public int sum() {
            return N.sum(elements());
        }

        /**
         * Returns the average of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');   // 65-73
         * double avg = tuple.average();                                                   // 69.0
         * }</pre>
         *
         * @return the average of the numeric values of all nine chars
         */
        @Override
        public double average() {
            return N.average(elements());
        }

        /**
         * Returns a new CharTuple9 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * CharTuple9 reversed = tuple.reverse();   // ('I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A')
         * }</pre>
         *
         * @return a new CharTuple9 with the elements in reverse order
         */
        @Override
        public CharTuple9 reverse() {
            return new CharTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * boolean hasG = tuple.contains('G');   // true
         * boolean hasZ = tuple.contains('Z');   // false
         * }</pre>
         *
         * @param valueToFind the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that may be thrown
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> consumer) throws E {
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

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
