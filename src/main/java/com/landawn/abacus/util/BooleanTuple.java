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
 * <p>
 * This class provides common functionality for boolean-based tuples of various sizes (0 to 9 elements).
 * BooleanTuple is designed to be a lightweight, type-safe container for multiple boolean values
 * that can be used as a composite key, return multiple values from a method, or group related
 * boolean flags together. All boolean tuple implementations extend this class and are immutable by design.
 * </p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Creating tuples
 * BooleanTuple1 single = BooleanTuple.of(true);
 * BooleanTuple2 pair = BooleanTuple.of(true, false);
 * BooleanTuple3 triple = BooleanTuple.of(true, false, true);
 *
 * // Using functional operations
 * pair.accept((a, b) -> System.out.println(a + " AND " + b));
 * boolean result = triple.map((a, b, c) -> a || b || c);
 * Optional<BooleanTuple2> filtered = pair.filter((a, b) -> a != b);
 * }</pre>
 *
 * @param <TP> the specific BooleanTuple subtype
 * @see PrimitiveTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class BooleanTuple<TP extends BooleanTuple<TP>> extends PrimitiveTuple<TP> {

    protected boolean[] elements;

    /**
     * Constructor for subclasses.
     * <p>
     * This constructor is protected to prevent direct instantiation of the abstract class.
     * Subclasses should use this constructor to initialize their instances.
     * </p>
     */
    protected BooleanTuple() {
    }

    /**
     * Creates a BooleanTuple1 containing a single boolean value.
     * 
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple1 tuple = BooleanTuple.of(true);
     * boolean value = tuple._1;  // true
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple2 tuple = BooleanTuple.of(true, false);
     * boolean first = tuple._1;  // true
     * boolean second = tuple._2;  // false
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * boolean third = tuple._3;  // true
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
     * boolean first = tuple._1;  // true
     * boolean fourth = tuple._4;  // false
     * BooleanTuple4 reversed = tuple.reverse();   // (false, true, false, true)
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
     * boolean first = tuple._1;  // true
     * boolean fifth = tuple._5;  // true
     * boolean contains = tuple.contains(false);   // true
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
     * boolean first = tuple._1;  // true
     * boolean sixth = tuple._6;  // false
     * BooleanTuple6 reversed = tuple.reverse();   // (false, true, false, true, false, true)
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
     * boolean first = tuple._1;  // true
     * boolean seventh = tuple._7;  // true
     * boolean[] array = tuple.toArray();   // [true, false, true, false, true, false, true]
     * }</pre>
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
     * boolean first = tuple._1;  // true
     * boolean eighth = tuple._8;  // false
     * BooleanList list = tuple.toList();
     * }</pre>
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
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more boolean values
     */
    @Deprecated
    public static BooleanTuple8 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7,
            final boolean _8) {
        return new BooleanTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a BooleanTuple9 containing nine boolean values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
     * boolean first = tuple._1;  // true
     * boolean ninth = tuple._9;  // true
     * int arity = tuple.arity();   // 9
     * }</pre>
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
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more boolean values
     */
    @Deprecated
    public static BooleanTuple9 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7,
            final boolean _8, final boolean _9) {
        return new BooleanTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * Creates a BooleanTuple from an array of boolean values.
     * <p>
     * The size of the returned tuple depends on the length of the input array.
     * This factory method supports arrays with 0 to 9 elements. For empty or null
     * arrays, returns an empty BooleanTuple0. For arrays with 1-9 elements, returns
     * the corresponding BooleanTuple1-9 instance.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create from array
     * boolean[] values = {true, false, true};
     * BooleanTuple3 tuple = BooleanTuple.create(values);
     *
     * // Empty array returns BooleanTuple0
     * BooleanTuple0 empty = BooleanTuple.create(new boolean[0]);
     *
     * // Single element
     * BooleanTuple1 single = BooleanTuple.create(new boolean[]{true});
     * }</pre>
     *
     * @param <TP> the specific BooleanTuple subtype to return
     * @param a the array of boolean values (must have length 0-9), may be {@code null}
     * @return a BooleanTuple of appropriate size containing the array elements
     * @throws IllegalArgumentException if the array has more than 9 elements
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
     * Returns a new tuple with the elements in reverse order.
     * <p>
     * This method creates and returns a new tuple instance with all elements in reversed order.
     * The original tuple remains unchanged as tuples are immutable.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple2 pair = BooleanTuple.of(true, false);
     * BooleanTuple2 reversedPair = pair.reverse();   // (false, true)
     *
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * BooleanTuple3 reversed = tuple.reverse();   // (true, false, true)
     * }</pre>
     *
     * @return a new tuple with the elements in reverse order
     */
    public abstract TP reverse();

    /**
     * Checks if this tuple contains the specified boolean value.
     * <p>
     * This method performs a linear search through all elements in the tuple to determine
     * if any element matches the specified value. Returns {@code true} if at least one
     * element equals the search value, {@code false} otherwise.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * boolean hasTrue = tuple.contains(true);     // true
     * boolean hasFalse = tuple.contains(false);   // true
     *
     * BooleanTuple2 flags = BooleanTuple.of(true, true);
     * boolean hasTrue = flags.contains(true);     // true
     * boolean hasFalse = flags.contains(false);   // false
     * }</pre>
     *
     * @param valueToFind the boolean value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(boolean valueToFind);

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
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * boolean[] array = tuple.toArray();   // [true, false, true]
     * array[0] = false;  // Does not modify the tuple
     *
     * BooleanTuple2 pair = BooleanTuple.of(true, false);
     * boolean[] pairArray = pair.toArray();   // [true, false]
     * }</pre>
     *
     * @return a new boolean array containing all tuple elements
     */
    public boolean[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new BooleanList containing all elements of this tuple.
     * <p>
     * Converts this tuple to a mutable {@link BooleanList} containing all elements
     * in their original order. The returned list is a new instance and modifications
     * to it do not affect the original tuple.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * BooleanList list = tuple.toList();
     * list.add(false);   // Adds to the list, tuple remains unchanged
     *
     * BooleanTuple2 flags = BooleanTuple.of(true, true);
     * BooleanList flagList = flags.toList();   // [true, true]
     * }</pre>
     *
     * @return a new BooleanList containing all tuple elements
     */
    public BooleanList toList() {
        return BooleanList.of(elements().clone());
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
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * tuple.forEach(b -> System.out.println("Value: " + b));
     * // Output: Value: true, Value: false, Value: true
     *
     * // Count true values
     * AtomicInteger count = new AtomicInteger();
     * tuple.forEach(b -> { if (b) count.incrementAndGet(); });
     * // count is now 2
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the consumer
     * @param consumer the action to be performed for each element, must not be {@code null}
     * @throws E if the consumer throws an exception during execution
     */
    public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
        for (final boolean e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     * Returns a Stream of Boolean objects containing all elements in this tuple.
     * <p>
     * Converts this tuple to a sequential {@link Stream} of boxed Boolean objects.
     * This allows using standard stream operations like filter, map, and collect
     * on the tuple elements. Note that primitive boolean values are boxed to Boolean
     * objects, which may have performance implications for large-scale operations.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * long trueCount = tuple.stream().filter(b -> b).count();             // 2
     * List<Boolean> list = tuple.stream().collect(Collectors.toList());   // [true, false, true]
     *
     * BooleanTuple2 flags = BooleanTuple.of(true, false);
     * boolean anyTrue = flags.stream().anyMatch(b -> b);   // true
     * }</pre>
     *
     * @return a Stream containing all tuple elements as Boolean objects
     */
    public Stream<Boolean> stream() {
        return Stream.of(elements());
    }

    /**
     * Returns a hash code value for this tuple.
     * <p>
     * The hash code is computed based on the contents of the tuple using a standard
     * algorithm that ensures equal tuples have equal hash codes. This implementation
     * is consistent with {@link #equals(Object)}.
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
     * 
     * <p>
     * Two tuples are considered equal if and only if:
     * </p>
     *
     * <ul>
     *   <li>They are the same object (reference equality), or</li>
     *   <li>They are instances of the exact same class, and</li>
     *   <li>They contain the same boolean values in the same order</li>
     * </ul>
     * This method is consistent with {@link #hashCode()}.
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
            return N.equals(elements(), ((BooleanTuple<TP>) obj).elements());
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
     * <p><b>Examples:</b></p>
     * <ul>
     *   <li>{@code (true, false, true)} for a BooleanTuple3</li>
     *   <li>{@code (true, false)} for a BooleanTuple2</li>
     *   <li>{@code (true)} for a BooleanTuple1</li>
     *   <li>{@code ()} for an empty BooleanTuple0</li>
     * </ul>
     *
     * @return a string representation of this tuple
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    /**
     * Returns the internal array containing all boolean elements in this tuple.
     * <p>
     * This method provides direct access to the underlying boolean array used by the tuple.
     * Subclasses implement this method to return their internal storage. The returned array
     * should not be modified directly as it may be shared or cached by the tuple implementation.
     * For safe array access, use {@link #toArray()} instead, which returns a defensive copy.
     * </p>
     *
     * @return the internal array of boolean elements
     */
    protected abstract boolean[] elements();

    /**
     * An empty BooleanTuple containing no elements.
     * <p>
     * This class represents a tuple with arity 0, serving as a singleton instance
     * for cases where an empty boolean tuple is needed. It is typically returned
     * by the {@link #create(boolean[])} method when a null or empty array is provided.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple0 empty = BooleanTuple.create(null);
     * BooleanTuple0 empty2 = BooleanTuple.create(new boolean[0]);
     * int size = empty.arity();   // 0
     * }</pre>
     */
    static final class BooleanTuple0 extends BooleanTuple<BooleanTuple0> {
        private static final BooleanTuple0 EMPTY = new BooleanTuple0();

        BooleanTuple0() {
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
         * Returns this empty tuple (reversing an empty tuple yields itself).
         *
         * @return this BooleanTuple0 instance
         */
        @Override
        public BooleanTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         * An empty tuple never contains any value.
         *
         * @param valueToFind the boolean value to search for
         * @return {@code false} always, since this tuple contains no elements
         */
        @Override
        public boolean contains(final boolean valueToFind) {
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
         * Returns the internal array of boolean elements.
         * For an empty tuple, returns an empty boolean array.
         *
         * @return an empty boolean array
         */
        @Override
        protected boolean[] elements() {
            return N.EMPTY_BOOLEAN_ARRAY;
        }
    }

    /**
     * A BooleanTuple containing exactly one boolean element.
     * <p>
     * Provides direct access to the element through the public final field {@code _1}.
     * This is the simplest non-empty tuple type, useful for wrapping a single boolean
     * value in a tuple context.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple1 tuple = BooleanTuple.of(true);
     * boolean value = tuple._1;  // true
     * boolean reversed = tuple.reverse()._1;  // true (same for single element)
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 1.
         *
         * @return 1
         */
        @Override
        public int arity() {
            return 1;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         * For a single-element tuple, returns a copy of itself.
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
         * @return {@code true} if the element equals valueToFind, {@code false} otherwise
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
         * @return {@code true} if the specified object is a BooleanTuple1 with the same element
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
         * @return a string representation in the format "(element)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
        }

        /**
         * Returns the internal array of boolean elements.
         * The array is lazily initialized on first access.
         *
         * @return a boolean array containing the single element
         */
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1} and {@code _2}.
     * This tuple type is commonly used for representing pairs of boolean flags or binary conditions,
     * and supports specialized functional operations through {@link #accept}, {@link #map}, and
     * {@link #filter} methods that work with both elements.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple2 tuple = BooleanTuple.of(true, false);
     * boolean first = tuple._1;  // true
     * boolean second = tuple._2;  // false
     *
     * // Using functional operations
     * tuple.accept((a, b) -> System.out.println(a + " XOR " + b));
     * boolean xor = tuple.map((a, b) -> a ^ b);   // true
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 2.
         *
         * @return 2
         */
        @Override
        public int arity() {
            return 2;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new BooleanTuple2 with the elements in reverse order
         */
        @Override
        public BooleanTuple2 reverse() {
            return new BooleanTuple2(_2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         * Applies the given action to both elements of this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple2 tuple = BooleanTuple.of(true, false);
         * tuple.accept((a, b) -> System.out.println(a + " AND " + b));   // prints "true AND false"
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to be performed on both elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.BooleanBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         * Applies the given function to both elements of this tuple and returns the result.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple2 tuple = BooleanTuple.of(true, false);
         * boolean result = tuple.map((a, b) -> a && b);   // false
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple2 tuple = BooleanTuple.of(true, false);
         * Optional<BooleanTuple2> result = tuple.filter((a, b) -> a || b);   // Optional containing the tuple
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
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
         * @return {@code true} if the specified object is a BooleanTuple2 with the same elements
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
         * @return a string representation in the format "(element1, element2)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
        }

        /**
         * Returns the internal array of boolean elements.
         * The array is lazily initialized on first access.
         *
         * @return a boolean array containing all elements in order
         */
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1}, {@code _2}, and {@code _3}.
     * This tuple type supports specialized functional operations through {@link #accept}, {@link #map}, and
     * {@link #filter} methods that work with all three elements simultaneously.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * boolean first = tuple._1;  // true
     * boolean second = tuple._2;  // false
     * boolean third = tuple._3;  // true
     *
     * // Using functional operations
     * boolean allTrue = tuple.map((a, b, c) -> a && b && c);   // false
     * boolean anyTrue = tuple.map((a, b, c) -> a || b || c);   // true
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 3.
         *
         * @return 3
         */
        @Override
        public int arity() {
            return 3;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * @return a new BooleanTuple3 with the elements in reverse order
         */
        @Override
        public BooleanTuple3 reverse() {
            return new BooleanTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
        }

        /**
         * Applies the given action to all three elements of this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
         * tuple.accept((a, b, c) -> System.out.println(a + ", " + b + ", " + c));
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
         * @param action the action to be performed on all three elements
         * @throws E if the action throws an exception
         */
        public <E extends Exception> void accept(final Throwables.BooleanTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given function to all three elements of this tuple and returns the result.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
         * boolean result = tuple.map((a, b, c) -> a && b && c);   // false
         * }</pre>
         *
         * @param <U> the type of the result
         * @param <E> the type of exception that may be thrown
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
         * Optional<BooleanTuple3> result = tuple.filter((a, b, c) -> a || c);   // Optional containing the tuple
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown
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
         * @return {@code true} if the specified object is a BooleanTuple3 with the same elements
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
         * @return a string representation in the format "(element1, element2, element3)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
        }

        /**
         * Returns the internal array of boolean elements.
         * The array is lazily initialized on first access.
         *
         * @return a boolean array containing all elements in order
         */
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1}, {@code _2}, {@code _3}, and {@code _4}.
     * This tuple type is useful for grouping four related boolean values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
     * boolean first = tuple._1;  // true
     * boolean fourth = tuple._4;  // false
     * boolean hasTrue = tuple.contains(true);   // true
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 4.
         *
         * @return 4
         */
        @Override
        public int arity() {
            return 4;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
         * BooleanTuple4 reversed = tuple.reverse();   // (false, true, false, true)
         * }</pre>
         *
         * @return a new BooleanTuple4 with the elements in reverse order
         */
        @Override
        public BooleanTuple4 reverse() {
            return new BooleanTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
        }

        /**
         * Returns the internal array of boolean elements.
         * The array is lazily initialized on first access.
         *
         * @return a boolean array containing all elements in order
         */
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1}, {@code _2}, {@code _3}, {@code _4}, and {@code _5}.
     * This tuple type is useful for grouping five related boolean values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
     * boolean first = tuple._1;  // true
     * boolean fifth = tuple._5;  // true
     * boolean hasFalse = tuple.contains(false);   // true
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 5.
         *
         * @return 5
         */
        @Override
        public int arity() {
            return 5;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
         * BooleanTuple5 reversed = tuple.reverse();   // (true, false, true, false, true) - reversed
         * }</pre>
         *
         * @return a new BooleanTuple5 with the elements in reverse order
         */
        @Override
        public BooleanTuple5 reverse() {
            return new BooleanTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
        }

        /**
         * Returns the internal array of boolean elements.
         * The array is lazily initialized on first access.
         *
         * @return a boolean array containing all elements in order
         */
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1}, {@code _2}, {@code _3}, {@code _4}, {@code _5}, and {@code _6}.
     * This tuple type is useful for grouping six related boolean values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
     * boolean first = tuple._1;  // true
     * boolean sixth = tuple._6;  // false
     * BooleanTuple6 reversed = tuple.reverse();   // (false, true, false, true, false, true)
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 6.
         *
         * @return 6
         */
        @Override
        public int arity() {
            return 6;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
         * BooleanTuple6 reversed = tuple.reverse();   // (false, true, false, true, false, true)
         * }</pre>
         *
         * @return a new BooleanTuple6 with the elements in reverse order
         */
        @Override
        public BooleanTuple6 reverse() {
            return new BooleanTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
        }

        /**
         * Returns the internal array of boolean elements.
         * The array is lazily initialized on first access.
         *
         * @return a boolean array containing all elements in order
         */
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1}, {@code _2}, {@code _3}, {@code _4}, {@code _5}, {@code _6}, and {@code _7}.
     * This tuple type is useful for grouping seven related boolean values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
     * boolean first = tuple._1;  // true
     * boolean seventh = tuple._7;  // true
     * boolean[] array = tuple.toArray();   // [true, false, true, false, true, false, true]
     * }</pre>
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
         * Returns the number of elements in this tuple, which is always 7.
         *
         * @return 7
         */
        @Override
        public int arity() {
            return 7;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
         * BooleanTuple7 reversed = tuple.reverse();   // (true, false, true, false, true, false, true) - reversed
         * }</pre>
         *
         * @return a new BooleanTuple7 with the elements in reverse order
         */
        @Override
        public BooleanTuple7 reverse() {
            return new BooleanTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
            consumer.accept(_7);
        }

        /**
         * Returns the internal array of boolean elements.
         * The array is lazily initialized on first access.
         *
         * @return a boolean array containing all elements in order
         */
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _8}.
     * This tuple type is useful for grouping eight related boolean values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
     * boolean first = tuple._1;  // true
     * boolean eighth = tuple._8;  // false
     * BooleanList list = tuple.toList();
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more boolean values
     */
    @Deprecated
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
         * Returns the number of elements in this tuple, which is always 8.
         *
         * @return 8
         */
        @Override
        public int arity() {
            return 8;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
         * BooleanTuple8 reversed = tuple.reverse();   // (false, true, false, true, false, true, false, true)
         * }</pre>
         *
         * @return a new BooleanTuple8 with the elements in reverse order
         */
        @Override
        public BooleanTuple8 reverse() {
            return new BooleanTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
            consumer.accept(_4);
            consumer.accept(_5);
            consumer.accept(_6);
            consumer.accept(_7);
            consumer.accept(_8);
        }

        /**
         * Returns the internal array of boolean elements.
         * The array is lazily initialized on first access.
         *
         * @return a boolean array containing all elements in order
         */
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
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _9}.
     * This tuple type is useful for grouping nine related boolean values together.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
     * boolean first = tuple._1;  // true
     * boolean ninth = tuple._9;  // true
     * int arity = tuple.arity();   // 9
     * }</pre>
     *
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more boolean values
     */
    @Deprecated
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
         * Returns the number of elements in this tuple, which is always 9.
         *
         * @return 9
         */
        @Override
        public int arity() {
            return 9;
        }

        /**
         * Returns a new tuple with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
         * BooleanTuple9 reversed = tuple.reverse();   // (true, false, true, false, true, false, true, false, true) - reversed
         * }</pre>
         *
         * @return a new BooleanTuple9 with the elements in reverse order
         */
        @Override
        public BooleanTuple9 reverse() {
            return new BooleanTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * @param valueToFind the boolean value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final boolean valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
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

        /**
         * Returns the internal array of boolean elements.
         * The array is lazily initialized on first access.
         *
         * @return a boolean array containing all elements in order
         */
        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
