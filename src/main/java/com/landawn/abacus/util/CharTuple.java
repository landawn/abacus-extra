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
 * This class provides common functionality for char-based tuples of various sizes (0 to 9 elements).
 * 
 * <p>CharTuple is designed to be a lightweight, type-safe container for multiple char values
 * that can be used as a composite key, return multiple values from a method, or group related
 * char values together.</p>
 * 
 * <p>All tuple implementations are immutable and thread-safe.</p>
 * 
 * @param <TP> the specific CharTuple subtype
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class CharTuple<TP extends CharTuple<TP>> extends PrimitiveTuple<TP> {

    protected char[] elements;

    /**
     * Constructor for subclasses.
     * This constructor is protected to prevent direct instantiation of the abstract class.
     */
    protected CharTuple() {
    }

    /**
     * Creates a CharTuple1 containing a single char value.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple1 tuple = CharTuple.of('A');
     * char value = tuple._1; // 'A'
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
     * char second = tuple._2; // 'B'
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
     * char third = tuple._3; // 'C'
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
     * char fourth = tuple._4; // 'D'
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
     * char median = tuple.median(); // 'C' (middle value when sorted)
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
     * int sum = tuple.sum(); // Sum of ASCII values
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
     * CharTuple7 reversed = tuple.reverse(); // ('S', 'Y', 'A', 'D', 'N', 'O', 'M')
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
     * boolean hasX = tuple.contains('X'); // false
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
     * char[] array = tuple.toArray(); // ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I']
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
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more char values
     */
    @Deprecated
    public static CharTuple9 of(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6, final char _7, final char _8,
            final char _9) {
        return new CharTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a CharTuple from an array of char values.
     * The size of the returned tuple depends on the length of the input array (0-9 elements).
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * char[] values = {'A', 'B', 'C'};
     * CharTuple3 tuple = CharTuple.create(values);
     * }</pre>
     *
     * @param <TP> the specific CharTuple subtype to return
     * @param a the array of char values (must contain 0-9 elements)
     * @return a CharTuple of appropriate size containing the array elements
     * @throws IllegalArgumentException if the array contains more than 9 elements
     */
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
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
     * char min = tuple.min(); // 'A'
     * }</pre>
     *
     * @return the minimum char value
     * @throws NoSuchElementException if the tuple is empty
     */
    public char min() {
        return N.min(elements());
    }

    /**
     * Returns the maximum char value in this tuple.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
     * char max = tuple.max(); // 'Z'
     * }</pre>
     *
     * @return the maximum char value
     * @throws NoSuchElementException if the tuple is empty
     */
    public char max() {
        return N.max(elements());
    }

    /**
     * Returns the median char value in this tuple.
     * For tuples with an even number of elements, returns the lower middle value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple3 = CharTuple.of('Z', 'A', 'M');
     * char median = tuple3.median(); // 'M'
     *
     * CharTuple4 tuple4 = CharTuple.of('A', 'B', 'C', 'D');
     * char median2 = tuple4.median(); // 'B'
     * }</pre>
     *
     * @return the median char value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     */
    public char median() {
        return N.median(elements());
    }

    /**
     * Returns the sum of all char values in this tuple as an int.
     * The sum is calculated by adding the numeric values of the chars.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple2 tuple = CharTuple.of('A', 'B'); // 'A'=65, 'B'=66
     * int sum = tuple.sum(); // 131
     * }</pre>
     *
     * @return the sum of all char values as an int
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the average of all char values in this tuple as a double.
     * The average is calculated based on the numeric values of the chars.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple2 tuple = CharTuple.of('A', 'C'); // 'A'=65, 'C'=67
     * double avg = tuple.average(); // 66.0
     * }</pre>
     *
     * @return the average of all char values
     * @throws NoSuchElementException if the tuple is empty
     */
    public double average() {
        return N.average(elements());
    }

    /**
     * Returns a new tuple with the elements in reverse order.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * CharTuple3 reversed = tuple.reverse(); // ('C', 'B', 'A')
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified char value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * boolean hasB = tuple.contains('B'); // true
     * boolean hasZ = tuple.contains('Z'); // false
     * }</pre>
     *
     * @param valueToFind the char value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(char valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * Modifications to the returned array do not affect the tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * char[] array = tuple.toArray(); // ['A', 'B', 'C']
     * }</pre>
     *
     * @return a new char array containing all tuple elements
     */
    public char[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new CharList containing all elements of this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * CharList list = tuple.toList();
     * }</pre>
     *
     * @return a new CharList containing all tuple elements
     */
    public CharList toList() {
        return CharList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * tuple.forEach(ch -> System.out.print(ch + " ")); // prints "A B C "
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown
     * @param consumer the action to be performed for each element
     * @throws E if the consumer throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.CharConsumer<E> consumer) throws E {
        for (final char e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns a CharStream of all elements in this tuple.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
     * int sum = tuple.stream().sum(); // 198
     * }</pre>
     *
     * @return a CharStream containing all tuple elements
     */
    public CharStream stream() {
        return CharStream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * The hash code is computed based on the contents of the tuple.
     *
     * @return a hash code value for this tuple
     */
    @Override
    public int hashCode() {
        return N.hashCode(elements());
    }

    /**
     * Compares this tuple to the specified object for equality.
     * Two tuples are equal if they are of the same class and contain the same elements in the same order.
     *
     * @param obj the object to be compared for equality with this tuple
     * @return {@code true} if the specified object is equal to this tuple
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
     * The string representation consists of the tuple elements enclosed in square brackets
     * and separated by commas and spaces.
     * 
     * <p>Example: {@code [A, B, C]}</p>
     *
     * @return a string representation of this tuple
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    protected abstract char[] elements();

    /**
     * An empty CharTuple containing no elements.
     * This class represents a tuple with arity 0.
     *
     * <p>This is a singleton class accessed via {@code CharTuple.create(new char[0])}.</p>
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
         * @return false always, because the tuple is empty
         */
        @Override
        public boolean contains(final char valueToFind) {
            return false;
        }

        /**
         * Returns a string representation of this empty tuple.
         *
         * @return "[]"
         */
        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected char[] elements() {
            return N.EMPTY_CHAR_ARRAY;
        }
    }

    /**
     * A CharTuple containing exactly one char element.
     * Provides direct access to the element through the public final field {@code _1}.
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
         * @return a string representation in the format "[element]"
         */
        @Override
        public String toString() {
            return "[" + _1 + "]";
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
     * Provides direct access to elements through public final fields {@code _1} and {@code _2}.
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
         * @return the smaller of the two char values
         */
        @Override
        public char min() {
            return N.min(_1, _2);
        }

        /**
         * Returns the maximum char value in this tuple.
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
         * @return the median (lower) char value
         */
        @Override
        public char median() {
            return N.median(_1, _2);
        }

        /**
         * Returns the sum of all char values in this tuple.
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
         * @return the average of the numeric values of both chars
         */
        @Override
        public double average() {
            return N.average(_1, _2);
        }

        /**
         * Returns a new CharTuple2 with the elements in reverse order.
         *
         * @return a new CharTuple2 with elements swapped
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
         * tuple.accept((a, b) -> System.out.println(a + " and " + b)); // prints "A and B"
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
         * String result = tuple.map((a, b) -> "" + a + b); // "AB"
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
         * Optional<CharTuple2> result = tuple.filter((a, b) -> a < b); // Optional containing the tuple
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
         * @return a string representation in the format "[element1, element2]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + "]";
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
     * Provides direct access to elements through public final fields {@code _1}, {@code _2}, and {@code _3}.
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
         * @return the smallest of the three char values
         */
        @Override
        public char min() {
            return N.min(_1, _2, _3);
        }

        /**
         * Returns the maximum char value in this tuple.
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
         * @return the middle char value when sorted
         */
        @Override
        public char median() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns the sum of all char values in this tuple.
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
         * String result = tuple.map((a, b, c) -> "" + a + b + c); // "ABC"
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
         * Optional<CharTuple3> result = tuple.filter((a, b, c) -> a < b && b < c); // Optional containing the tuple
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
         * @return a string representation in the format "[element1, element2, element3]"
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + "]";
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
     * Provides direct access to elements through public final fields {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
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
         * @return 4
         */
        @Override
        public int arity() {
            return 4;
        }

        /**
         * Returns a new CharTuple4 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
         * CharTuple4 reversed = tuple.reverse(); // ('D', 'C', 'B', 'A')
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
     * Provides direct access to elements through public final fields {@code _1} through {@code _5}.
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
         * @return 5
         */
        @Override
        public int arity() {
            return 5;
        }

        /**
         * Returns a new CharTuple5 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * CharTuple5 reversed = tuple.reverse(); // ('E', 'D', 'C', 'B', 'A')
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
     * Provides direct access to elements through public final fields {@code _1} through {@code _6}.
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
         * @return 6
         */
        @Override
        public int arity() {
            return 6;
        }

        /**
         * Returns a new CharTuple6 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * CharTuple6 reversed = tuple.reverse(); // ('F', 'E', 'D', 'C', 'B', 'A')
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
     * Provides direct access to elements through public final fields {@code _1} through {@code _7}.
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
         * @return 7
         */
        @Override
        public int arity() {
            return 7;
        }

        /**
         * Returns a new CharTuple7 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * CharTuple7 reversed = tuple.reverse(); // ('G', 'F', 'E', 'D', 'C', 'B', 'A')
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
     * Provides direct access to elements through public final fields {@code _1} through {@code _8}.
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
         * @return 8
         */
        @Override
        public int arity() {
            return 8;
        }

        /**
         * Returns a new CharTuple8 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * CharTuple8 reversed = tuple.reverse(); // ('H', 'G', 'F', 'E', 'D', 'C', 'B', 'A')
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
     * Provides direct access to elements through public final fields {@code _1} through {@code _9}.
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more char values
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
         * @return 9
         */
        @Override
        public int arity() {
            return 9;
        }

        /**
         * Returns a new CharTuple9 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * CharTuple9 reversed = tuple.reverse(); // ('I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A')
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