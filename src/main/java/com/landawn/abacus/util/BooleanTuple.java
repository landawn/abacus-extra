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

import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.Stream;

/**
 * Abstract base class for immutable tuple implementations that hold primitive boolean values.
 * This class provides common functionality for boolean-based tuples of various sizes (0 to 9 elements).
 * 
 * <p>BooleanTuple is designed to be a lightweight, type-safe container for multiple boolean values
 * that can be used as a composite key, return multiple values from a method, or group related
 * boolean flags together.</p>
 * 
 * <p>All tuple implementations are immutable and thread-safe.</p>
 * 
 * @param <TP> the specific BooleanTuple subtype
 * @author HaiYang Li
 * @since 1.0
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class BooleanTuple<TP extends BooleanTuple<TP>> extends PrimitiveTuple<TP> {

    protected boolean[] elements;

    /**
     * Creates a BooleanTuple1 containing a single boolean value.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * BooleanTuple1 tuple = BooleanTuple.of(true);
     * boolean value = tuple._1; // true
     * }</pre>
     *
     * @param _1 the boolean value to store in the tuple
     * @return a new BooleanTuple1 containing the specified value
     */
    public static BooleanTuple1 of(final boolean _1) {
        return new BooleanTuple1(_1);
    }

    /**
     * Creates a BooleanTuple2 containing two boolean values.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * BooleanTuple2 tuple = BooleanTuple.of(true, false);
     * boolean first = tuple._1;  // true
     * boolean second = tuple._2; // false
     * }</pre>
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @return a new BooleanTuple2 containing the specified values
     */
    public static BooleanTuple2 of(final boolean _1, final boolean _2) {
        return new BooleanTuple2(_1, _2);
    }

    /**
     * Creates a BooleanTuple3 containing three boolean values.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * boolean third = tuple._3; // true
     * }</pre>
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @return a new BooleanTuple3 containing the specified values
     */
    public static BooleanTuple3 of(final boolean _1, final boolean _2, final boolean _3) {
        return new BooleanTuple3(_1, _2, _3);
    }

    /**
     * Creates a BooleanTuple4 containing four boolean values.
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @param _4 the fourth boolean value
     * @return a new BooleanTuple4 containing the specified values
     */
    public static BooleanTuple4 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4) {
        return new BooleanTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a BooleanTuple5 containing five boolean values.
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @param _4 the fourth boolean value
     * @param _5 the fifth boolean value
     * @return a new BooleanTuple5 containing the specified values
     */
    public static BooleanTuple5 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5) {
        return new BooleanTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a BooleanTuple6 containing six boolean values.
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @param _4 the fourth boolean value
     * @param _5 the fifth boolean value
     * @param _6 the sixth boolean value
     * @return a new BooleanTuple6 containing the specified values
     */
    public static BooleanTuple6 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6) {
        return new BooleanTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a BooleanTuple7 containing seven boolean values.
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @param _4 the fourth boolean value
     * @param _5 the fifth boolean value
     * @param _6 the sixth boolean value
     * @param _7 the seventh boolean value
     * @return a new BooleanTuple7 containing the specified values
     */
    public static BooleanTuple7 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6,
            final boolean _7) {
        return new BooleanTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a BooleanTuple8 containing eight boolean values.
     * 
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @param _4 the fourth boolean value
     * @param _5 the fifth boolean value
     * @param _6 the sixth boolean value
     * @param _7 the seventh boolean value
     * @param _8 the eighth boolean value
     * @return a new BooleanTuple8 containing the specified values
     * @deprecated you should consider using {@code class SomeClass { final T1 propName1, final T2 propName2...}}
     *             for better readability and maintainability when dealing with many values
     */
    @Deprecated
    public static BooleanTuple8 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7,
            final boolean _8) {
        return new BooleanTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a BooleanTuple9 containing nine boolean values.
     * 
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @param _4 the fourth boolean value
     * @param _5 the fifth boolean value
     * @param _6 the sixth boolean value
     * @param _7 the seventh boolean value
     * @param _8 the eighth boolean value
     * @param _9 the ninth boolean value
     * @return a new BooleanTuple9 containing the specified values
     * @deprecated you should consider using {@code class SomeClass { final T1 propName1, final T2 propName2...}}
     *             for better readability and maintainability when dealing with many values
     */
    @Deprecated
    public static BooleanTuple9 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7,
            final boolean _8, final boolean _9) {
        return new BooleanTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a BooleanTuple from an array of boolean values.
     * The size of the returned tuple depends on the length of the input array (0-9 elements).
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * boolean[] values = {true, false, true};
     * BooleanTuple3 tuple = BooleanTuple.create(values);
     * }</pre>
     *
     * @param <TP> the specific BooleanTuple subtype to return
     * @param a the array of boolean values (must contain 0-9 elements)
     * @return a BooleanTuple of appropriate size containing the array elements
     * @throws IllegalArgumentException if the array contains more than 9 elements
     */
    public static <TP extends BooleanTuple<TP>> TP create(final boolean[] a) {
        if (a == null || a.length == 0) {
            return (TP) BooleanTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) BooleanTuple.of(a[0]);

            case 2:
                return (TP) BooleanTuple.of(a[0], a[1]);

            case 3:
                return (TP) BooleanTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     * Creates a new tuple with the elements in reversed order.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * BooleanTuple3 reversed = tuple.reverse(); // contains true, false, true
     * }</pre>
     *
     * @return a new tuple with elements in reversed order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified boolean value.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * boolean hasTrue = tuple.contains(true);   // true
     * boolean hasFalse = tuple.contains(false); // true
     * }</pre>
     *
     * @param valueToFind the boolean value to search for
     * @return true if the value is found, false otherwise
     */
    public abstract boolean contains(boolean valueToFind);

    /**
     * Returns a new array containing all elements of this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * boolean[] array = tuple.toArray(); // [true, false, true]
     * }</pre>
     *
     * @return a new boolean array containing all tuple elements
     */
    public boolean[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new BooleanList containing all elements of this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * BooleanList list = tuple.toList();
     * }</pre>
     *
     * @return a new BooleanList containing all tuple elements
     */
    public BooleanList toList() {
        return BooleanList.of(elements().clone());
    }

    /**
     * Performs the given action for each element in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * tuple.forEach(b -> System.out.println("Value: " + b));
     * }</pre>
     *
     * @param <E> the type of exception that the consumer may throw
     * @param consumer the action to be performed for each element
     * @throws E if the consumer throws an exception
     */
    public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
        for (final boolean e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns a Stream of Boolean objects containing the elements in this tuple.
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * long trueCount = tuple.stream().filter(b -> b).count(); // 2
     * }</pre>
     *
     * @return a Stream containing all tuple elements as Boolean objects
     */
    public Stream<Boolean> stream() {
        return Stream.of(elements());
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
     * @return true if the specified object is equal to this tuple
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        } else {
            return N.equals(elements(), ((BooleanTuple<TP>) obj).elements());
        }
    }

    /**
     * Returns a string representation of this tuple.
     * The string representation consists of the tuple elements enclosed in square brackets
     * and separated by commas and spaces.
     * 
     * <p>Example: {@code [true, false, true]}</p>
     *
     * @return a string representation of this tuple
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    protected abstract boolean[] elements();

    /**
     * An empty BooleanTuple containing no elements.
     * This class represents a tuple with arity 0.
     */
    static final class BooleanTuple0 extends BooleanTuple<BooleanTuple0> {
        private static final BooleanTuple0 EMPTY = new BooleanTuple0();

        BooleanTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public BooleanTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final boolean valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected boolean[] elements() {
            return N.EMPTY_BOOLEAN_ARRAY;
        }
    }

    /**
     * A BooleanTuple containing exactly one boolean element.
     * Provides direct access to the element through the public final field {@code _1}.
     */
    public static final class BooleanTuple1 extends BooleanTuple<BooleanTuple1> {

        /** The single boolean value stored in this tuple. */
        public final boolean _1;

        BooleanTuple1() {
            this(false);
        }

        BooleanTuple1(final boolean _1) {
            this._1 = _1;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 1
         */
        @Override
        public int arity() {
            return 1;
        }

        /**
         * Creates a new BooleanTuple1 with the same element.
         * Since this tuple has only one element, reversing has no effect.
         *
         * @return a new BooleanTuple1 with the same element
         */
        @Override
        public BooleanTuple1 reverse() {
            return new BooleanTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return true if the tuple's element equals valueToFind, false otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind;
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return 1231 if the value is true, 1237 if false
         */
        @Override
        public int hashCode() {
            return _1 ? 1231 : 1237;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return true if the specified object is a BooleanTuple1 with the same element
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final BooleanTuple1 other)) {
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
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A BooleanTuple containing exactly two boolean elements.
     * Provides direct access to elements through public final fields {@code _1} and {@code _2}.
     */
    public static final class BooleanTuple2 extends BooleanTuple<BooleanTuple2> {

        /** The first boolean value stored in this tuple. */
        public final boolean _1;
        /** The second boolean value stored in this tuple. */
        public final boolean _2;

        BooleanTuple2() {
            this(false, false);
        }

        BooleanTuple2(final boolean _1, final boolean _2) {
            this._1 = _1;
            this._2 = _2;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 2
         */
        @Override
        public int arity() {
            return 2;
        }

        /**
         * Creates a new BooleanTuple2 with the elements in reversed order.
         *
         * @return a new BooleanTuple2 with elements swapped
         */
        @Override
        public BooleanTuple2 reverse() {
            return new BooleanTuple2(_2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return true if either element equals valueToFind, false otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         * Applies the given action to both elements of this tuple.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * BooleanTuple2 tuple = BooleanTuple.of(true, false);
         * tuple.accept((a, b) -> System.out.println(a + " AND " + b)); // prints "true AND false"
         * }</pre>
         * 
         * @param <E> the type of exception that the action may throw
         * @param action the action to be performed on both elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.BooleanBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given function to both elements of this tuple and returns the result.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * BooleanTuple2 tuple = BooleanTuple.of(true, false);
         * boolean result = tuple.map((a, b) -> a && b); // false
         * }</pre>
         * 
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the mapping function to apply to both elements
         * @return the result of applying the mapping function
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.BooleanBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if it matches the given predicate,
         * otherwise returns an empty Optional.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * BooleanTuple2 tuple = BooleanTuple.of(true, false);
         * Optional<BooleanTuple2> result = tuple.filter((a, b) -> a || b); // Optional containing the tuple
         * }</pre>
         * 
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the predicate to test both elements
         * @return an Optional containing this tuple if the predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<BooleanTuple2> filter(final Throwables.BooleanBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return a hash code value calculated from both elements
         */
        @Override
        public int hashCode() {
            int result = 1;

            result = 31 * result + (_1 ? 1231 : 1237);
            return 31 * result + (_2 ? 1231 : 1237);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return true if the specified object is a BooleanTuple2 with the same elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final BooleanTuple2 other)) {
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
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A BooleanTuple containing exactly three boolean elements.
     * Provides direct access to elements through public final fields {@code _1}, {@code _2}, and {@code _3}.
     */
    public static final class BooleanTuple3 extends BooleanTuple<BooleanTuple3> {

        /** The first boolean value stored in this tuple. */
        public final boolean _1;
        /** The second boolean value stored in this tuple. */
        public final boolean _2;
        /** The third boolean value stored in this tuple. */
        public final boolean _3;

        BooleanTuple3() {
            this(false, false, false);
        }

        BooleanTuple3(final boolean _1, final boolean _2, final boolean _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 3
         */
        @Override
        public int arity() {
            return 3;
        }

        /**
         * Creates a new BooleanTuple3 with the elements in reversed order.
         *
         * @return a new BooleanTuple3 with elements in reversed order
         */
        @Override
        public BooleanTuple3 reverse() {
            return new BooleanTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return true if any element equals valueToFind, false otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * @param <E> the type of exception that the consumer may throw
         * @param consumer the action to be performed for each element
         * @throws E if the consumer throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
        }

        /**
         * Applies the given action to all three elements of this tuple.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
         * tuple.accept((a, b, c) -> System.out.println(a + ", " + b + ", " + c));
         * }</pre>
         * 
         * @param <E> the type of exception that the action may throw
         * @param action the action to be performed on all three elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.BooleanTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given function to all three elements of this tuple and returns the result.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
         * boolean result = tuple.map((a, b, c) -> a && b && c); // false
         * }</pre>
         * 
         * @param <U> the type of the result
         * @param <E> the type of exception that the mapper may throw
         * @param mapper the mapping function to apply to all three elements
         * @return the result of applying the mapping function
         * @throws E if the mapper throws an exception
         */
        public <U, E extends Exception> U map(final Throwables.BooleanTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if it matches the given predicate,
         * otherwise returns an empty Optional.
         * 
         * <p>Example usage:</p>
         * <pre>{@code
         * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
         * Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a || c); // Optional containing the tuple
         * }</pre>
         * 
         * @param <E> the type of exception that the predicate may throw
         * @param predicate the predicate to test all three elements
         * @return an Optional containing this tuple if the predicate returns true, empty otherwise
         * @throws E if the predicate throws an exception
         */
        public <E extends Exception> Optional<BooleanTuple3> filter(final Throwables.BooleanTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * @return a hash code value calculated from all three elements
         */
        @Override
        public int hashCode() {
            int result = 1;

            result = 31 * result + (_1 ? 1231 : 1237);
            result = 31 * result + (_2 ? 1231 : 1237);
            return 31 * result + (_3 ? 1231 : 1237);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * @param obj the object to be compared for equality with this tuple
         * @return true if the specified object is a BooleanTuple3 with the same elements
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final BooleanTuple3 other)) {
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
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A BooleanTuple containing exactly four boolean elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class BooleanTuple4 extends BooleanTuple<BooleanTuple4> {

        /** The first boolean value stored in this tuple. */
        public final boolean _1;
        /** The second boolean value stored in this tuple. */
        public final boolean _2;
        /** The third boolean value stored in this tuple. */
        public final boolean _3;
        /** The fourth boolean value stored in this tuple. */
        public final boolean _4;

        BooleanTuple4() {
            this(false, false, false, false);
        }

        BooleanTuple4(final boolean _1, final boolean _2, final boolean _3, final boolean _4) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 4
         */
        @Override
        public int arity() {
            return 4;
        }

        /**
         * Creates a new BooleanTuple4 with the elements in reversed order.
         *
         * @return a new BooleanTuple4 with elements in reversed order
         */
        @Override
        public BooleanTuple4 reverse() {
            return new BooleanTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return true if any element equals valueToFind, false otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A BooleanTuple containing exactly five boolean elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class BooleanTuple5 extends BooleanTuple<BooleanTuple5> {

        /** The first boolean value stored in this tuple. */
        public final boolean _1;
        /** The second boolean value stored in this tuple. */
        public final boolean _2;
        /** The third boolean value stored in this tuple. */
        public final boolean _3;
        /** The fourth boolean value stored in this tuple. */
        public final boolean _4;
        /** The fifth boolean value stored in this tuple. */
        public final boolean _5;

        BooleanTuple5() {
            this(false, false, false, false, false);
        }

        BooleanTuple5(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 5
         */
        @Override
        public int arity() {
            return 5;
        }

        /**
         * Creates a new BooleanTuple5 with the elements in reversed order.
         *
         * @return a new BooleanTuple5 with elements in reversed order
         */
        @Override
        public BooleanTuple5 reverse() {
            return new BooleanTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return true if any element equals valueToFind, false otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A BooleanTuple containing exactly six boolean elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class BooleanTuple6 extends BooleanTuple<BooleanTuple6> {

        /** The first boolean value stored in this tuple. */
        public final boolean _1;
        /** The second boolean value stored in this tuple. */
        public final boolean _2;
        /** The third boolean value stored in this tuple. */
        public final boolean _3;
        /** The fourth boolean value stored in this tuple. */
        public final boolean _4;
        /** The fifth boolean value stored in this tuple. */
        public final boolean _5;
        /** The sixth boolean value stored in this tuple. */
        public final boolean _6;

        BooleanTuple6() {
            this(false, false, false, false, false, false);
        }

        BooleanTuple6(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 6
         */
        @Override
        public int arity() {
            return 6;
        }

        /**
         * Creates a new BooleanTuple6 with the elements in reversed order.
         *
         * @return a new BooleanTuple6 with elements in reversed order
         */
        @Override
        public BooleanTuple6 reverse() {
            return new BooleanTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return true if any element equals valueToFind, false otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A BooleanTuple containing exactly seven boolean elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class BooleanTuple7 extends BooleanTuple<BooleanTuple7> {

        /** The first boolean value stored in this tuple. */
        public final boolean _1;
        /** The second boolean value stored in this tuple. */
        public final boolean _2;
        /** The third boolean value stored in this tuple. */
        public final boolean _3;
        /** The fourth boolean value stored in this tuple. */
        public final boolean _4;
        /** The fifth boolean value stored in this tuple. */
        public final boolean _5;
        /** The sixth boolean value stored in this tuple. */
        public final boolean _6;
        /** The seventh boolean value stored in this tuple. */
        public final boolean _7;

        BooleanTuple7() {
            this(false, false, false, false, false, false, false);
        }

        BooleanTuple7(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
        }

        /**
         * Returns the number of elements in this tuple.
         *
         * @return always returns 7
         */
        @Override
        public int arity() {
            return 7;
        }

        /**
         * Creates a new BooleanTuple7 with the elements in reversed order.
         *
         * @return a new BooleanTuple7 with elements in reversed order
         */
        @Override
        public BooleanTuple7 reverse() {
            return new BooleanTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return true if any element equals valueToFind, false otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A BooleanTuple containing exactly eight boolean elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class BooleanTuple8 extends BooleanTuple<BooleanTuple8> {

        /** The first boolean value stored in this tuple. */
        public final boolean _1;
        /** The second boolean value stored in this tuple. */
        public final boolean _2;
        /** The third boolean value stored in this tuple. */
        public final boolean _3;
        /** The fourth boolean value stored in this tuple. */
        public final boolean _4;
        /** The fifth boolean value stored in this tuple. */
        public final boolean _5;
        /** The sixth boolean value stored in this tuple. */
        public final boolean _6;
        /** The seventh boolean value stored in this tuple. */
        public final boolean _7;
        /** The eighth boolean value stored in this tuple. */
        public final boolean _8;

        BooleanTuple8() {
            this(false, false, false, false, false, false, false, false);
        }

        BooleanTuple8(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7,
                final boolean _8) {
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
         * Returns the number of elements in this tuple.
         *
         * @return always returns 8
         */
        @Override
        public int arity() {
            return 8;
        }

        /**
         * Creates a new BooleanTuple8 with the elements in reversed order.
         *
         * @return a new BooleanTuple8 with elements in reversed order
         */
        @Override
        public BooleanTuple8 reverse() {
            return new BooleanTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return true if any element equals valueToFind, false otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A BooleanTuple containing exactly nine boolean elements.
     * Provides direct access to elements through public final fields.
     */
    public static final class BooleanTuple9 extends BooleanTuple<BooleanTuple9> {

        /** The first boolean value stored in this tuple. */
        public final boolean _1;
        /** The second boolean value stored in this tuple. */
        public final boolean _2;
        /** The third boolean value stored in this tuple. */
        public final boolean _3;
        /** The fourth boolean value stored in this tuple. */
        public final boolean _4;
        /** The fifth boolean value stored in this tuple. */
        public final boolean _5;
        /** The sixth boolean value stored in this tuple. */
        public final boolean _6;
        /** The seventh boolean value stored in this tuple. */
        public final boolean _7;
        /** The eighth boolean value stored in this tuple. */
        public final boolean _8;
        /** The ninth boolean value stored in this tuple. */
        public final boolean _9;

        BooleanTuple9() {
            this(false, false, false, false, false, false, false, false, false);
        }

        BooleanTuple9(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7,
                final boolean _8, final boolean _9) {
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
         * Returns the number of elements in this tuple.
         *
         * @return always returns 9
         */
        @Override
        public int arity() {
            return 9;
        }

        /**
         * Creates a new BooleanTuple9 with the elements in reversed order.
         *
         * @return a new BooleanTuple9 with elements in reversed order
         */
        @Override
        public BooleanTuple9 reverse() {
            return new BooleanTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return true if any element equals valueToFind, false otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}