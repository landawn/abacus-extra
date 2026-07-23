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

import com.landawn.abacus.annotation.MayReturnNull;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple0;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple1;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple2;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple3;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple4;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple5;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple6;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple7;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple8;
import com.landawn.abacus.util.BooleanTuple.BooleanTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.Stream;

/**
 * Base class for immutable tuples of primitive {@code boolean} values.
 *
 * <p>The nested tuple types model fixed arities from 0 through 9. Factory methods such as
 * {@link #from(boolean[])} and the {@code of(...)} overloads select the matching subtype, while the
 * base class supplies reversal, containment, and functional helper operations.</p>
 *
 * <p>This sealed base class permits only the built-in arity-specific nested tuple types.</p>
 *
 * <p><b>Note:</b> unlike the numeric tuple families ({@code ByteTuple}, {@code ShortTuple},
 * {@code IntTuple}, {@code LongTuple}, {@code CharTuple}, {@code FloatTuple}, {@code DoubleTuple}),
 * {@code BooleanTuple} does not expose aggregate operations such as {@code min()}, {@code max()},
 * {@code median()}, {@code sum()}, or {@code average()}. These are intentionally omitted because
 * {@code boolean} values lack a natural numeric ordering and have no meaningful sum or average.</p>
 *
 * @param <TP> the concrete {@code BooleanTuple} subtype that fluent operations such as {@link #reverse()} return
 * @see PrimitiveTuple
 * @see ByteTuple
 * @see CharTuple
 * @see ShortTuple
 * @see IntTuple
 * @see LongTuple
 * @see FloatTuple
 * @see DoubleTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract sealed class BooleanTuple<TP extends BooleanTuple<TP>> extends PrimitiveTuple<TP> permits BooleanTuple0, BooleanTuple1, BooleanTuple2,
        BooleanTuple3, BooleanTuple4, BooleanTuple5, BooleanTuple6, BooleanTuple7, BooleanTuple8, BooleanTuple9 {

    /** Lazily initialized cached array view of all tuple elements. */
    protected volatile boolean[] elements;

    /**
     * Protected constructor for subclass instantiation.
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link #of(boolean)}, {@link #of(boolean, boolean)}, etc., to create tuple instances.
     */
    protected BooleanTuple() {
    }

    /**
     * Creates a {@link BooleanTuple1} containing a single boolean value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple1 t = BooleanTuple.of(true);
     * boolean value = t._1;                // true
     * int arity = t.arity();               // 1
     *
     * BooleanTuple.BooleanTuple1 f = BooleanTuple.of(false);
     * boolean fVal = f._1;                 // false
     * String s = f.toString();             // "(false)"
     *
     * boolean hasTrue = t.contains(true);   // true
     * boolean hasFalse = t.contains(false); // false
     *
     * BooleanTuple.BooleanTuple1 rev = t.reverse();
     * boolean revVal = rev._1;             // true (single-element reverse unchanged)
     * }</pre>
     *
     * @param _1 the boolean value to store in the tuple
     * @return a new {@link BooleanTuple1} containing the specified value
     */
    public static BooleanTuple1 of(final boolean _1) {
        return new BooleanTuple1(_1);
    }

    /**
     * Creates a {@link BooleanTuple2} containing two boolean values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple2 t = BooleanTuple.of(true, false);
     * boolean first = t._1;                // true
     * boolean second = t._2;               // false
     * String s = t.toString();             // "(true, false)"
     *
     * BooleanTuple.BooleanTuple2 allFalse = BooleanTuple.of(false, false);
     * boolean hasFalse = allFalse.contains(false); // true
     * boolean hasTrue = allFalse.contains(true);   // false
     *
     * BooleanTuple.BooleanTuple2 allTrue = BooleanTuple.of(true, true);
     * BooleanTuple.BooleanTuple2 rev = allTrue.reverse();
     * String revStr = rev.toString();      // "(true, true)"
     *
     * BooleanTuple.BooleanTuple2 mixed = BooleanTuple.of(true, false);
     * BooleanTuple.BooleanTuple2 mixedRev = mixed.reverse();
     * String mixedRevStr = mixedRev.toString(); // "(false, true)"
     * }</pre>
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @return a new {@link BooleanTuple2} containing the specified values
     */
    public static BooleanTuple2 of(final boolean _1, final boolean _2) {
        return new BooleanTuple2(_1, _2);
    }

    /**
     * Creates a {@link BooleanTuple3} containing three boolean values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple3 t = BooleanTuple.of(true, false, true);
     * boolean first = t._1;               // true
     * boolean second = t._2;              // false
     * boolean third = t._3;               // true
     * String s = t.toString();            // "(true, false, true)"
     *
     * // all-false tuple
     * BooleanTuple.BooleanTuple3 allFalse = BooleanTuple.of(false, false, false);
     * boolean hasTrue = allFalse.contains(true);    // false
     * boolean hasFalse = allFalse.contains(false);  // true
     * String af = allFalse.toString();              // "(false, false, false)"
     *
     * // reverse
     * BooleanTuple.BooleanTuple3 r = BooleanTuple.of(true, false, false);
     * String revStr = r.reverse().toString(); // "(false, false, true)"
     * }</pre>
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @return a new {@link BooleanTuple3} containing the specified values
     */
    public static BooleanTuple3 of(final boolean _1, final boolean _2, final boolean _3) {
        return new BooleanTuple3(_1, _2, _3);
    }

    /**
     * Creates a {@link BooleanTuple4} containing four boolean values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple4 t = BooleanTuple.of(true, false, true, false);
     * boolean first = t._1;                         // true
     * boolean fourth = t._4;                        // false
     * String s = t.toString();                      // "(true, false, true, false)"
     * BooleanTuple.BooleanTuple4 rev = t.reverse(); // (false, true, false, true)
     * String revStr = rev.toString();               // "(false, true, false, true)"
     *
     * // all-true tuple
     * BooleanTuple.BooleanTuple4 allTrue = BooleanTuple.of(true, true, true, true);
     * boolean hasFalse = allTrue.contains(false);  // false
     * boolean[] arr = allTrue.toArray();           // [true, true, true, true]
     *
     * // all-false tuple
     * BooleanTuple.BooleanTuple4 allFalse = BooleanTuple.of(false, false, false, false);
     * boolean hasTrue = allFalse.contains(true);    // false
     * }</pre>
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @param _4 the fourth boolean value
     * @return a new {@link BooleanTuple4} containing the specified values
     */
    public static BooleanTuple4 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4) {
        return new BooleanTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a {@link BooleanTuple5} containing five boolean values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple5 t = BooleanTuple.of(true, false, true, false, true);
     * boolean first = t._1;                      // true
     * boolean fifth = t._5;                      // true
     * boolean containsFalse = t.contains(false); // true
     * String s = t.toString();                   // "(true, false, true, false, true)"
     *
     * // palindrome - reverse equals original
     * BooleanTuple.BooleanTuple5 rev = t.reverse();
     * String revStr = rev.toString();        // "(true, false, true, false, true)"
     *
     * // all-false
     * BooleanTuple.BooleanTuple5 allFalse = BooleanTuple.of(false, false, false, false, false);
     * boolean hasTrue = allFalse.contains(true);  // false
     * int arity = allFalse.arity();               // 5
     * }</pre>
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @param _4 the fourth boolean value
     * @param _5 the fifth boolean value
     * @return a new {@link BooleanTuple5} containing the specified values
     */
    public static BooleanTuple5 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5) {
        return new BooleanTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a {@link BooleanTuple6} containing six boolean values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple6 t = BooleanTuple.of(true, false, true, false, true, false);
     * boolean first = t._1;                  // true
     * boolean sixth = t._6;                  // false
     * String s = t.toString();               // "(true, false, true, false, true, false)"
     * BooleanTuple.BooleanTuple6 rev = t.reverse();
     * String revStr = rev.toString();        // "(false, true, false, true, false, true)"
     *
     * // all-true
     * BooleanTuple.BooleanTuple6 allTrue = BooleanTuple.of(true, true, true, true, true, true);
     * boolean hasFalse = allTrue.contains(false); // false
     * int arity = allTrue.arity();                // 6
     *
     * // all-false
     * BooleanTuple.BooleanTuple6 allFalse = BooleanTuple.of(false, false, false, false, false, false);
     * boolean hasTrue = allFalse.contains(true);  // false
     * boolean[] arr = allFalse.toArray();         // [false, false, false, false, false, false]
     * }</pre>
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @param _4 the fourth boolean value
     * @param _5 the fifth boolean value
     * @param _6 the sixth boolean value
     * @return a new {@link BooleanTuple6} containing the specified values
     */
    public static BooleanTuple6 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6) {
        return new BooleanTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a {@link BooleanTuple7} containing seven boolean values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple7 t = BooleanTuple.of(true, false, true, false, true, false, true);
     * boolean first = t._1;                    // true
     * boolean seventh = t._7;                  // true
     * boolean[] array = t.toArray();           // [true, false, true, false, true, false, true]
     * int arity = t.arity();                   // 7
     *
     * // all-true
     * BooleanTuple.BooleanTuple7 allTrue = BooleanTuple.of(true, true, true, true, true, true, true);
     * boolean hasFalse = allTrue.contains(false); // false
     *
     * // all-false
     * BooleanTuple.BooleanTuple7 allFalse = BooleanTuple.of(false, false, false, false, false, false, false);
     * boolean hasTrue = allFalse.contains(true);  // false
     * boolean[] falseArr = allFalse.toArray();    // [false, false, false, false, false, false, false]
     * }</pre>
     *
     * @param _1 the first boolean value
     * @param _2 the second boolean value
     * @param _3 the third boolean value
     * @param _4 the fourth boolean value
     * @param _5 the fifth boolean value
     * @param _6 the sixth boolean value
     * @param _7 the seventh boolean value
     * @return a new {@link BooleanTuple7} containing the specified values
     */
    public static BooleanTuple7 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6,
            final boolean _7) {
        return new BooleanTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a {@link BooleanTuple8} containing eight boolean values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple8 t = BooleanTuple.of(true, false, true, false, true, false, true, false);
     * boolean first = t._1;                 // true
     * boolean eighth = t._8;                // false
     * int arity = t.arity();                // 8
     * BooleanList list = t.toList();        // [true, false, true, false, true, false, true, false]
     *
     * // all-true
     * BooleanTuple.BooleanTuple8 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true);
     * boolean hasTrue = allTrue.contains(true);   // true
     * boolean hasFalse = allTrue.contains(false); // false
     *
     * // all-false
     * BooleanTuple.BooleanTuple8 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false);
     * boolean[] arr = allFalse.toArray();    // [false, false, false, false, false, false, false, false]
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
     * @return a new {@link BooleanTuple8} containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more boolean values
     */
    @Deprecated
    public static BooleanTuple8 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7,
            final boolean _8) {
        return new BooleanTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a {@link BooleanTuple9} containing nine boolean values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple9 t = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
     * boolean first = t._1;                  // true
     * boolean ninth = t._9;                  // true
     * int arity = t.arity();                 // 9
     * String s = t.toString();               // "(true, false, true, false, true, false, true, false, true)"
     *
     * // all-true (maximum arity)
     * BooleanTuple.BooleanTuple9 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true, true);
     * boolean hasTrue = allTrue.contains(true);   // true
     * boolean hasFalse = allTrue.contains(false); // false
     *
     * // all-false
     * BooleanTuple.BooleanTuple9 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false, false);
     * boolean[] arr = allFalse.toArray(); // [false, false, false, false, false, false, false, false, false]
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
     * @return a new {@link BooleanTuple9} containing the specified values
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
     * This factory method supports arrays with 0 to 9 elements. For {@code null} or empty
     * arrays, returns the shared empty tuple. For arrays with 1-9 elements, returns the
     * corresponding {@code BooleanTuple1}..{@code BooleanTuple9} instance. The values are copied
     * into the new tuple; subsequent modifications to the input array do not affect it.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create from 3-element array
     * boolean[] values = {true, false, true};
     * BooleanTuple<?> t3 = BooleanTuple.from(values);
     * int arity3 = t3.arity();                // 3
     * String s3 = t3.toString();              // "(true, false, true)"
     *
     * // Single element
     * BooleanTuple<?> t1 = BooleanTuple.from(new boolean[]{true});
     * int arity1 = t1.arity();                // 1
     *
     * // Empty array returns empty tuple
     * BooleanTuple<?> empty = BooleanTuple.from(new boolean[0]);
     * int emptyArity = empty.arity();         // 0
     * String emptyStr = empty.toString();     // "()"
     *
     * // null array also returns empty tuple
     * BooleanTuple<?> fromNull = BooleanTuple.from(null);
     * int nullArity = fromNull.arity();       // 0
     *
     * // length > 9 throws IllegalArgumentException
     * // BooleanTuple.from(new boolean[10]); // throws IllegalArgumentException
     * }</pre>
     *
     * <p><b>&#9888;&#65039; Warning:</b> The runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type. Assigning to the wrong arity-specific subtype will result in a
     * {@link ClassCastException} at the assignment site.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of boolean values; may be {@code null} or empty, in which case the shared empty tuple is returned
     * @return a {@code BooleanTuple} of the appropriate arity containing the array values, or the shared empty tuple if the array is {@code null} or empty
     * @throws IllegalArgumentException if {@code values} has more than 9 elements
     * @see #of(boolean)
     */
    @SuppressWarnings("deprecation")
    public static <TP extends BooleanTuple<TP>> TP from(final boolean[] values) {
        if (values == null || values.length == 0) {
            return (TP) BooleanTuple0.EMPTY;
        }

        switch (values.length) {
            case 1:
                return (TP) BooleanTuple.of(values[0]);

            case 2:
                return (TP) BooleanTuple.of(values[0], values[1]);

            case 3:
                return (TP) BooleanTuple.of(values[0], values[1], values[2]);

            case 4:
                return (TP) BooleanTuple.of(values[0], values[1], values[2], values[3]);

            case 5:
                return (TP) BooleanTuple.of(values[0], values[1], values[2], values[3], values[4]);

            case 6:
                return (TP) BooleanTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);

            case 7:
                return (TP) BooleanTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);

            case 8:
                return (TP) BooleanTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);

            case 9:
                return (TP) BooleanTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);

            default:
                throw new IllegalArgumentException("Too many elements (" + values.length + "). Maximum: 9");
        }
    }

    /**
     * Returns a new tuple with the elements in reverse order.
     * <p>
     * This method returns a tuple containing all elements in reversed order. The original
     * tuple remains unchanged as tuples are immutable.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple2 pair = BooleanTuple.of(true, false);
     * BooleanTuple.BooleanTuple2 reversedPair = pair.reverse();
     * String revStr = reversedPair.toString();          // "(false, true)"
     *
     * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * BooleanTuple.BooleanTuple3 reversed = tuple.reverse();
     * String palStr = reversed.toString();              // "(true, false, true)" - palindrome
     *
     * // edge: empty tuple reverses to itself
     * BooleanTuple<?> empty = BooleanTuple.from(new boolean[0]);
     * String emptyRev = empty.reverse().toString();     // "()"
     *
     * // edge: single-element reverse has same value
     * BooleanTuple.BooleanTuple1 single = BooleanTuple.of(false);
     * BooleanTuple.BooleanTuple1 singleRev = single.reverse();
     * boolean revVal = singleRev._1;                   // false
     * }</pre>
     *
     * <p>For tuples of arity 0 or 1, the returned tuple is equal to this one (reversing has no effect).
     * The empty tuple returns itself; arity-1 tuples return a new instance with the same value.</p>
     *
     * @return a tuple of the same arity with the elements in reverse order
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
     * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * boolean hasTrue = tuple.contains(true);      // true
     * boolean hasFalse = tuple.contains(false);    // true
     *
     * // all-true: contains(false) returns false
     * BooleanTuple.BooleanTuple2 flags = BooleanTuple.of(true, true);
     * boolean allHaveTrue = flags.contains(true);  // true
     * boolean anyFalse = flags.contains(false);    // false
     *
     * // edge: empty tuple never contains any value
     * BooleanTuple<?> empty = BooleanTuple.from(new boolean[0]);
     * boolean emptyHasTrue = empty.contains(true);   // false
     * boolean emptyHasFalse = empty.contains(false); // false
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
     * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * boolean[] array = tuple.toArray();    // [true, false, true]
     * array[0] = false;                     // Does not modify the tuple
     * boolean still = tuple.contains(true); // true
     *
     * BooleanTuple.BooleanTuple2 pair = BooleanTuple.of(true, false);
     * boolean[] pairArray = pair.toArray(); // [true, false]
     *
     * // edge: empty tuple returns zero-length array
     * BooleanTuple<?> empty = BooleanTuple.from(new boolean[0]);
     * boolean[] emptyArr = empty.toArray(); // []
     * int len = emptyArr.length;            // 0
     *
     * // edge: single false element
     * BooleanTuple.BooleanTuple1 single = BooleanTuple.of(false);
     * boolean[] singleArr = single.toArray(); // [false]
     * }</pre>
     *
     * @return a new boolean array containing all tuple elements
     * @see #toList()
     * @see #stream()
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
     * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * BooleanList list = tuple.toList();      // [true, false, true]
     * int size = list.size();                 // 3
     * list.add(false);                        // Adds to the list, tuple remains unchanged
     *
     * // all-true tuple
     * BooleanTuple.BooleanTuple2 flags = BooleanTuple.of(true, true);
     * BooleanList flagList = flags.toList();  // [true, true]
     * boolean first = flagList.get(0);        // true
     *
     * // edge: empty tuple produces empty list
     * BooleanTuple<?> empty = BooleanTuple.from(new boolean[0]);
     * BooleanList emptyList = empty.toList();
     * int emptySize = emptyList.size();       // 0
     *
     * // edge: single false element
     * BooleanTuple.BooleanTuple1 single = BooleanTuple.of(false);
     * BooleanList singleList = single.toList();
     * boolean val = singleList.get(0);        // false
     * }</pre>
     *
     * @return a new BooleanList containing all tuple elements
     * @see #toArray()
     * @see #stream()
     */
    public BooleanList toList() {
        return BooleanList.of(elements().clone());
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
     * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * tuple.forEach(b -> System.out.println("Value: " + b));  // Prints three lines: "Value: true", "Value: false", "Value: true"
     *
     * // Count true values
     * java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger();
     * tuple.forEach(b -> { if (b) count.incrementAndGet(); });
     * // count is now 2
     *
     * // edge: empty tuple - consumer is never called
     * BooleanTuple<?> empty = BooleanTuple.from(new boolean[0]);
     * java.util.concurrent.atomic.AtomicInteger emptyCount = new java.util.concurrent.atomic.AtomicInteger();
     * empty.forEach(b -> emptyCount.incrementAndGet());  // emptyCount is still 0
     *
     * // edge: null action throws IllegalArgumentException
     * // tuple.forEach(null); // throws IllegalArgumentException
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the action to be performed for each element, must not be {@code null}
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     * @see #stream()
     */
    public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
        N.checkArgNotNull(action, "action");

        for (final boolean element : elements()) {
            action.accept(element);
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
     * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * long trueCount = tuple.stream().filter(b -> b).count();             // 2
     * List<Boolean> list = tuple.stream().collect(Collectors.toList());   // [true, false, true]
     *
     * BooleanTuple.BooleanTuple2 flags = BooleanTuple.of(true, false);
     * boolean anyTrue = flags.stream().anyMatch(b -> b);   // true
     *
     * // edge: empty tuple stream has count 0
     * BooleanTuple<?> empty = BooleanTuple.from(new boolean[0]);
     * long emptyCount = empty.stream().count();             // 0
     *
     * // edge: all-false tuple - allMatch(b -> !b) returns true
     * BooleanTuple.BooleanTuple2 allFalse = BooleanTuple.of(false, false);
     * boolean allNegated = allFalse.stream().allMatch(b -> !b); // true
     * }</pre>
     *
     * @return a Stream containing all tuple elements as Boolean objects
     * @see #toArray()
     * @see #toList()
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple2 a = BooleanTuple.of(true, false);
     * BooleanTuple.BooleanTuple2 b = BooleanTuple.of(true, false);
     * boolean same = (a.hashCode() == b.hashCode()); // true (equal tuples have equal hash codes)
     *
     * BooleanTuple.BooleanTuple1 t = BooleanTuple.of(true);
     * int h = t.hashCode();               // 1231 (Boolean.hashCode(true))
     *
     * BooleanTuple.BooleanTuple1 f = BooleanTuple.of(false);
     * int hf = f.hashCode();              // 1237 (Boolean.hashCode(false))
     *
     * // edge: empty tuple has a consistent hash code
     * BooleanTuple<?> empty = BooleanTuple.from(new boolean[0]);
     * int emptyHash1 = empty.hashCode();
     * int emptyHash2 = empty.hashCode();
     * boolean consistent = (emptyHash1 == emptyHash2); // true
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
     * <ul>
     *   <li>they are the same object (reference equality), or</li>
     *   <li>they are instances of the exact same runtime class (so tuples of different arity are never equal), and</li>
     *   <li>they contain the same boolean values in the same order.</li>
     * </ul>
     *
     * <p>This method is consistent with {@link #hashCode()}. The non-empty arity-specific subclasses
     * override this method with an equivalent but specialized implementation that compares fields directly.</p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple2 a = BooleanTuple.of(true, false);
     * BooleanTuple.BooleanTuple2 b = BooleanTuple.of(true, false);
     * boolean eq = a.equals(b);            // true
     *
     * BooleanTuple.BooleanTuple2 c = BooleanTuple.of(false, true);
     * boolean neq = a.equals(c);           // false (different order)
     *
     * // edge: different arities are never equal
     * BooleanTuple.BooleanTuple1 t1 = BooleanTuple.of(true);
     * boolean diffArity = t1.equals(a);   // false
     *
     * // edge: null comparison
     * boolean nullEq = a.equals(null);     // false
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
            return N.equals(elements(), ((BooleanTuple<TP>) obj).elements());
        }
    }

    /**
     * Returns the internal array containing all boolean elements in this tuple.
     * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
     * Modifying the returned array will compromise the immutability of this tuple.
     * Use {@link #toArray()} instead if you need an array that can be safely modified.
     * </p>
     *
     * @return the internal array of boolean elements
     */
    protected abstract boolean[] elements();

    /**
     * An empty BooleanTuple containing no elements (arity 0).
     * <p>
     * This package-private class is exposed only through the base {@code BooleanTuple} type
     * via the singleton instance returned by {@link #from(boolean[])} when invoked with a
     * {@code null} or zero-length array.
     * </p>
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
         * Returns this empty tuple instance.
         * Since this tuple has no elements, reversing has no effect.
         *
         * @return this {@code BooleanTuple0} instance
         */
        @Override
        public BooleanTuple0 reverse() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         * Since this tuple is empty, this method always returns {@code false}.
         *
         * @param valueToFind the boolean value to search for
         * @return {@code false} always, because the tuple is empty
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
         * Returns the shared empty boolean array.
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
     * BooleanTuple.BooleanTuple1 tuple = BooleanTuple.of(true);
     * boolean value = tuple._1;               // true
     * boolean reversed = tuple.reverse()._1;  // true (same for single element)
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple1 t = BooleanTuple.of(true);
         * int arity = t.arity();   // 1
         *
         * BooleanTuple.BooleanTuple1 f = BooleanTuple.of(false);
         * int arityF = f.arity();  // 1
         * }</pre>
         *
         * @return 1
         */
        @Override
        public int arity() {
            return 1;
        }

        /**
         * Returns a new {@code BooleanTuple1} with the same element.
         * Since this tuple has only one element, reversing has no effect on the contained value;
         * however, a new instance is still returned for consistency with the {@link #reverse()} contract.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple1 t = BooleanTuple.of(true);
         * BooleanTuple.BooleanTuple1 rev = t.reverse();
         * boolean val = rev._1;   // true (single-element reverse is unchanged)
         *
         * BooleanTuple.BooleanTuple1 f = BooleanTuple.of(false);
         * BooleanTuple.BooleanTuple1 revF = f.reverse();
         * boolean valF = revF._1; // false
         * }</pre>
         *
         * @return a new BooleanTuple.BooleanTuple1 with the same element
         */
        @Override
        public BooleanTuple1 reverse() {
            return new BooleanTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple1 t = BooleanTuple.of(true);
         * boolean hasTrue = t.contains(true);   // true
         * boolean hasFalse = t.contains(false); // false
         *
         * BooleanTuple.BooleanTuple1 f = BooleanTuple.of(false);
         * boolean fHasFalse = f.contains(false); // true
         * boolean fHasTrue = f.contains(true);   // false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple1 t = BooleanTuple.of(true);
         * int h = t.hashCode();   // 1231 (same as Boolean.hashCode(true))
         *
         * BooleanTuple.BooleanTuple1 f = BooleanTuple.of(false);
         * int hf = f.hashCode();  // 1237 (same as Boolean.hashCode(false))
         *
         * // equal tuples have same hash code
         * BooleanTuple.BooleanTuple1 t2 = BooleanTuple.of(true);
         * boolean sameHash = (t.hashCode() == t2.hashCode()); // true
         * }</pre>
         *
         * @return {@code 1231} if {@code _1} is {@code true}, {@code 1237} otherwise (the same values used by {@link Boolean#hashCode(boolean)})
         */
        @Override
        public int hashCode() {
            return _1 ? 1231 : 1237;
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple1 a = BooleanTuple.of(true);
         * BooleanTuple.BooleanTuple1 b = BooleanTuple.of(true);
         * boolean eq = a.equals(b);    // true
         *
         * BooleanTuple.BooleanTuple1 c = BooleanTuple.of(false);
         * boolean neq = a.equals(c);   // false
         *
         * // edge: null returns false
         * boolean nullEq = a.equals(null); // false
         *
         * // edge: different arity never equal
         * BooleanTuple.BooleanTuple2 t2 = BooleanTuple.of(true, false);
         * boolean diffArity = a.equals(t2); // false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a BooleanTuple.BooleanTuple1 with the same element, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple1 t = BooleanTuple.of(true);
         * String s = t.toString();   // "(true)"
         *
         * BooleanTuple.BooleanTuple1 f = BooleanTuple.of(false);
         * String sf = f.toString();  // "(false)"
         * }</pre>
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
     * A tuple containing exactly two boolean values.
     * The values are accessible through the public final fields {@code _1} and {@code _2}.
     *
     * <p>In addition to the operations inherited from {@link BooleanTuple}, this class provides
     * functional helpers for working with pairs:</p>
     * <ul>
     *   <li>{@link #accept(Throwables.BooleanBiConsumer)} - consume both values</li>
     *   <li>{@link #map(Throwables.BooleanBiFunction)} - transform the pair to a single value</li>
     *   <li>{@link #filter(Throwables.BooleanBiPredicate)} - conditionally wrap in {@link Optional}</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
     * boolean first = tuple._1;   // true
     * boolean second = tuple._2;  // false
     *
     * // Using functional operations
     * tuple.accept((a, b) -> System.out.println(a + " XOR " + b));
     * boolean xor = tuple.map((a, b) -> a ^ b);   // true
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple2 t = BooleanTuple.of(true, false);
         * int arity = t.arity();              // 2
         *
         * BooleanTuple.BooleanTuple2 allTrue = BooleanTuple.of(true, true);
         * int arityAll = allTrue.arity();     // 2
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple2 t = BooleanTuple.of(true, false);
         * BooleanTuple.BooleanTuple2 rev = t.reverse();
         * boolean r1 = rev._1;       // false
         * boolean r2 = rev._2;       // true
         * String s = rev.toString(); // "(false, true)"
         *
         * // all-same: reverse equals original
         * BooleanTuple.BooleanTuple2 allTrue = BooleanTuple.of(true, true);
         * BooleanTuple.BooleanTuple2 revAll = allTrue.reverse();
         * String revAllStr = revAll.toString(); // "(true, true)"
         * }</pre>
         *
         * @return a new BooleanTuple.BooleanTuple2 with the elements in reverse order
         */
        @Override
        public BooleanTuple2 reverse() {
            return new BooleanTuple2(_2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple2 mixed = BooleanTuple.of(true, false);
         * mixed.contains(true);    // returns true
         * mixed.contains(false);   // returns true
         *
         * // All-true tuple: false is absent
         * BooleanTuple.BooleanTuple2 allTrue = BooleanTuple.of(true, true);
         * allTrue.contains(true);    // returns true
         * allTrue.contains(false);   // returns false
         *
         * // All-false tuple: true is absent
         * BooleanTuple.BooleanTuple2 allFalse = BooleanTuple.of(false, false);
         * allFalse.contains(true);   // returns false
         * allFalse.contains(false);  // returns true
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
         * List<Boolean> list = new ArrayList<>();
         * tuple.forEach(b -> list.add(b));   // list becomes [true, false]
         *
         * // Count true values
         * BooleanTuple.BooleanTuple2 flags = BooleanTuple.of(true, true);
         * java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger();
         * flags.forEach(b -> { if (b) count.incrementAndGet(); });
         * // count is now 2
         *
         * // All-false: count stays 0
         * BooleanTuple.BooleanTuple2 allFalse = BooleanTuple.of(false, false);
         * java.util.concurrent.atomic.AtomicInteger count2 = new java.util.concurrent.atomic.AtomicInteger();
         * allFalse.forEach(b -> { if (b) count2.incrementAndGet(); });
         * // count2 is 0
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

            action.accept(_1);
            action.accept(_2);
        }

        /**
         * Applies the given action to both elements of this tuple.
         * <p>
         * This method executes the provided bi-consumer action with both tuple elements as arguments.
         * It is useful for performing operations that require access to both values simultaneously,
         * such as logging, comparison, or updating external state based on the pair of values.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
         * tuple.accept((a, b) -> System.out.println(a + " AND " + b));   // prints "true AND false"
         *
         * // Record both values into an array
         * boolean[] out = new boolean[2];
         * tuple.accept((a, b) -> { out[0] = a; out[1] = b; });   // out[0]=true, out[1]=false
         *
         * // All-false pair: action still fires with (false, false)
         * BooleanTuple.BooleanTuple2 allFalse = BooleanTuple.of(false, false);
         * allFalse.accept((a, b) -> System.out.println(a || b));   // prints "false"
         *
         * // Same-value pair: detect equal values
         * BooleanTuple.BooleanTuple2 same = BooleanTuple.of(true, true);
         * same.accept((a, b) -> System.out.println(a == b)); // prints "true"
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the bi-consumer action to be performed on both elements, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #forEach(Throwables.BooleanConsumer)
         * @see #map(Throwables.BooleanBiFunction)
         */
        public <E extends Exception> void accept(final Throwables.BooleanBiConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

            action.accept(_1, _2);
        }

        /**
         * Applies the given function to both elements of this tuple and returns the result.
         * <p>
         * This method transforms both tuple elements into a single result value by applying
         * the provided bi-function. It enables functional-style processing of the tuple's
         * values, such as combining them with logical operations or computing derived values.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
         * boolean and = tuple.map((a, b) -> a && b);                     // returns false
         * boolean or  = tuple.map((a, b) -> a || b);                     // returns true
         * String str  = tuple.map((a, b) -> "(" + a + ", " + b + ")");   // returns "(true, false)"
         *
         * // XOR of the two values
         * boolean xor = tuple.map((a, b) -> a ^ b);   // returns true
         *
         * // Mapper returning null is allowed (@MayReturnNull)
         * Object nullResult = tuple.map((a, b) -> null);   // returns null
         *
         * // All-false pair
         * BooleanTuple.BooleanTuple2 allFalse = BooleanTuple.of(false, false);
         * boolean neitherTrue = allFalse.map((a, b) -> a || b);   // returns false
         * }</pre>
         *
         * @param <U> the type of the result returned by the mapper function
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the bi-function to apply to both elements, must not be {@code null}
         * @return the result of applying the mapping function to both elements; may be {@code null} if the mapper returns {@code null}
         * @throws IllegalArgumentException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.BooleanBiConsumer)
         * @see #filter(Throwables.BooleanBiPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.BooleanBiFunction<U, E> mapper) throws E {
            N.checkArgNotNull(mapper, "mapper");

            return mapper.apply(_1, _2);
        }

        /**
         * Returns an Optional containing this tuple if it matches the given predicate,
         * otherwise returns an empty Optional.
         * <p>
         * This method evaluates the provided bi-predicate against both tuple elements.
         * If the predicate returns {@code true}, the tuple is wrapped in an Optional;
         * otherwise, an empty Optional is returned. This enables conditional processing
         * and chaining of operations based on the tuple's values.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple2 tuple = BooleanTuple.of(true, false);
         * Optional<BooleanTuple.BooleanTuple2> present = tuple.filter((a, b) -> a || b);   // Optional containing the tuple
         * Optional<BooleanTuple.BooleanTuple2> empty   = tuple.filter((a, b) -> a && b);   // Optional.empty()
         *
         * // Different values: predicate matches
         * Optional<BooleanTuple.BooleanTuple2> diffVals = tuple.filter((a, b) -> a != b);   // Optional containing tuple
         *
         * // All-false pair: OR predicate never matches
         * BooleanTuple.BooleanTuple2 allFalse = BooleanTuple.of(false, false);
         * Optional<BooleanTuple.BooleanTuple2> noMatch = allFalse.filter((a, b) -> a || b);   // Optional.empty()
         *
         * // All-same pair: equality predicate matches
         * Optional<BooleanTuple.BooleanTuple2> sameMatch = allFalse.filter((a, b) -> a == b);   // Optional containing tuple
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the bi-predicate to test both elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty otherwise
         * @throws IllegalArgumentException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception during evaluation
         * @see #accept(Throwables.BooleanBiConsumer)
         * @see #map(Throwables.BooleanBiFunction)
         */
        public <E extends Exception> Optional<BooleanTuple2> filter(final Throwables.BooleanBiPredicate<E> predicate) throws E {
            N.checkArgNotNull(predicate, "predicate");

            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple2 t1 = BooleanTuple.of(true, false);
         * BooleanTuple.BooleanTuple2 t2 = BooleanTuple.of(true, false);
         * assert t1.hashCode() == t2.hashCode(); // returns true (equal tuples have equal hash codes)
         * t1.hashCode();                         // returns 39398
         *
         * // All-false
         * BooleanTuple.BooleanTuple2 allFalse = BooleanTuple.of(false, false);
         * allFalse.hashCode();   // returns 39584
         * }</pre>
         *
         * @return a hash code value calculated from both elements
         */
        @Override
        public int hashCode() {
            return 31 * (_1 ? 1231 : 1237) + (_2 ? 1231 : 1237);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple2 t1 = BooleanTuple.of(true, false);
         * BooleanTuple.BooleanTuple2 t2 = BooleanTuple.of(true, false);
         * t1.equals(t2);   // returns true
         * t1.equals(t1);   // returns true (reflexive)
         *
         * // Different order is not equal
         * BooleanTuple.BooleanTuple2 reversed = BooleanTuple.of(false, true);
         * t1.equals(reversed);   // returns false
         *
         * // Null and non-tuple types are never equal
         * t1.equals(null);              // returns false
         * t1.equals("(true, false)");   // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a BooleanTuple.BooleanTuple2 with the same elements in the same order, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple2 t = BooleanTuple.of(true, false);
         * t.toString();   // returns "(true, false)"
         *
         * BooleanTuple.BooleanTuple2 allTrue = BooleanTuple.of(true, true);
         * allTrue.toString();   // returns "(true, true)"
         *
         * BooleanTuple.BooleanTuple2 allFalse = BooleanTuple.of(false, false);
         * allFalse.toString();   // returns "(false, false)"
         * }</pre>
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
     * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
     * boolean first = tuple._1;   // true
     * boolean second = tuple._2;  // false
     * boolean third = tuple._3;   // true
     *
     * // Using functional operations
     * boolean allTrue = tuple.map((a, b, c) -> a && b && c);   // false
     * boolean anyTrue = tuple.map((a, b, c) -> a || b || c);   // true
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
         * tuple.arity();   // returns 3
         *
         * BooleanTuple.BooleanTuple3 allFalse = BooleanTuple.of(false, false, false);
         * allFalse.arity();   // returns 3
         *
         * // Arity is fixed regardless of element values
         * BooleanTuple.BooleanTuple3 allTrue = BooleanTuple.of(true, true, true);
         * allTrue.arity();   // returns 3
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, false);
         * BooleanTuple.BooleanTuple3 reversed = tuple.reverse();
         * reversed.toString();   // returns "(false, false, true)"
         *
         * // Palindrome: reversing yields the same sequence
         * BooleanTuple.BooleanTuple3 palindrome = BooleanTuple.of(true, false, true);
         * palindrome.reverse().toString();   // returns "(true, false, true)"
         *
         * // All-same values: reverse is identical in value
         * BooleanTuple.BooleanTuple3 allTrue = BooleanTuple.of(true, true, true);
         * allTrue.reverse().toString();   // returns "(true, true, true)"
         *
         * // reverse() returns a NEW instance (not the same object)
         * assert palindrome.reverse() != palindrome; // true
         * }</pre>
         *
         * @return a new BooleanTuple.BooleanTuple3 with the elements in reverse order
         */
        @Override
        public BooleanTuple3 reverse() {
            return new BooleanTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple3 mixed = BooleanTuple.of(true, false, true);
         * mixed.contains(true);    // returns true
         * mixed.contains(false);   // returns true
         *
         * // All-true tuple: false is absent
         * BooleanTuple.BooleanTuple3 allTrue = BooleanTuple.of(true, true, true);
         * allTrue.contains(true);    // returns true
         * allTrue.contains(false);   // returns false
         *
         * // All-false tuple: true is absent
         * BooleanTuple.BooleanTuple3 allFalse = BooleanTuple.of(false, false, false);
         * allFalse.contains(true);   // returns false
         * allFalse.contains(false);  // returns true
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
         * List<Boolean> list = new ArrayList<>();
         * tuple.forEach(b -> list.add(b));   // list becomes [true, false, true]
         *
         * // Count true values
         * java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger();
         * tuple.forEach(b -> { if (b) count.incrementAndGet(); });
         * // count is 2
         *
         * // All-false: count stays 0
         * BooleanTuple.BooleanTuple3 allFalse = BooleanTuple.of(false, false, false);
         * java.util.concurrent.atomic.AtomicInteger count2 = new java.util.concurrent.atomic.AtomicInteger();
         * allFalse.forEach(b -> { if (b) count2.incrementAndGet(); });
         * // count2 is 0
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
        }

        /**
         * Applies the given action to all three elements of this tuple.
         * <p>
         * This method executes the provided tri-consumer action with all three tuple elements as arguments.
         * It is useful for performing operations that require access to all three values simultaneously,
         * such as logging, complex validation, or updating external state based on the triple of values.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
         * tuple.accept((a, b, c) -> System.out.println(a + ", " + b + ", " + c));   // prints "true, false, true"
         *
         * // Record all three values into an array
         * boolean[] out = new boolean[3];
         * tuple.accept((a, b, c) -> { out[0] = a; out[1] = b; out[2] = c; });   // out[0]=true, out[1]=false, out[2]=true
         *
         * // All-false triple: action still fires with (false, false, false)
         * BooleanTuple.BooleanTuple3 allFalse = BooleanTuple.of(false, false, false);
         * allFalse.accept((a, b, c) -> System.out.println(a || b || c));   // prints "false"
         *
         * // All-true triple: AND result is true
         * BooleanTuple.BooleanTuple3 allTrue = BooleanTuple.of(true, true, true);
         * allTrue.accept((a, b, c) -> System.out.println(a && b && c));   // prints "true"
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the tri-consumer action to be performed on all three elements, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #forEach(Throwables.BooleanConsumer)
         * @see #map(Throwables.BooleanTriFunction)
         */
        public <E extends Exception> void accept(final Throwables.BooleanTriConsumer<E> action) throws E {
            N.checkArgNotNull(action, "action");

            action.accept(_1, _2, _3);
        }

        /**
         * Applies the given function to all three elements of this tuple and returns the result.
         * <p>
         * This method transforms all three tuple elements into a single result value by applying
         * the provided tri-function. It enables functional-style processing of the tuple's
         * values, such as combining them with logical operations, computing aggregate values,
         * or creating derived objects from the three boolean values.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
         * boolean and = tuple.map((a, b, c) -> a && b && c);                                 // returns false
         * boolean or  = tuple.map((a, b, c) -> a || b || c);                                 // returns true
         * int trueCount = tuple.map((a, b, c) -> (a ? 1 : 0) + (b ? 1 : 0) + (c ? 1 : 0));   // returns 2
         *
         * // Mapper returning null is allowed (@MayReturnNull)
         * Object nullResult = tuple.map((a, b, c) -> null);   // returns null
         *
         * // All-false: AND and OR both false
         * BooleanTuple.BooleanTuple3 allFalse = BooleanTuple.of(false, false, false);
         * allFalse.map((a, b, c) -> a && b && c);   // returns false
         * allFalse.map((a, b, c) -> a || b || c);   // returns false
         * }</pre>
         *
         * @param <U> the type of the result returned by the mapper function
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the tri-function to apply to all three elements, must not be {@code null}
         * @return the result of applying the mapping function to all three elements; may be {@code null} if the mapper returns {@code null}
         * @throws IllegalArgumentException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.BooleanTriConsumer)
         * @see #filter(Throwables.BooleanTriPredicate)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.BooleanTriFunction<U, E> mapper) throws E {
            N.checkArgNotNull(mapper, "mapper");

            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an Optional containing this tuple if it matches the given predicate,
         * otherwise returns an empty Optional.
         * <p>
         * This method evaluates the provided tri-predicate against all three tuple elements.
         * If the predicate returns {@code true}, the tuple is wrapped in an Optional;
         * otherwise, an empty Optional is returned. This enables conditional processing
         * and chaining of operations based on the tuple's values.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple3 tuple = BooleanTuple.of(true, false, true);
         * Optional<BooleanTuple.BooleanTuple3> present = tuple.filter((a, b, c) -> a || c);       // Optional containing the tuple
         * Optional<BooleanTuple.BooleanTuple3> empty   = tuple.filter((a, b, c) -> a && b && c);  // Optional.empty()
         *
         * // Mixed values: not-all-equal predicate matches
         * Optional<BooleanTuple.BooleanTuple3> mixed = tuple.filter((a, b, c) -> a != b || b != c);   // Optional containing tuple
         *
         * // All-false triple: OR predicate never matches
         * BooleanTuple.BooleanTuple3 allFalse = BooleanTuple.of(false, false, false);
         * Optional<BooleanTuple.BooleanTuple3> noMatch = allFalse.filter((a, b, c) -> a || b || c);   // Optional.empty()
         *
         * // All-same values: equality predicate matches
         * Optional<BooleanTuple.BooleanTuple3> sameMatch = allFalse.filter((a, b, c) -> a == b && b == c);   // Optional containing tuple
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the tri-predicate to test all three elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty otherwise
         * @throws IllegalArgumentException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception during evaluation
         * @see #accept(Throwables.BooleanTriConsumer)
         * @see #map(Throwables.BooleanTriFunction)
         */
        public <E extends Exception> Optional<BooleanTuple3> filter(final Throwables.BooleanTriPredicate<E> predicate) throws E {
            N.checkArgNotNull(predicate, "predicate");

            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple3 t1 = BooleanTuple.of(true, false, true);
         * BooleanTuple.BooleanTuple3 t2 = BooleanTuple.of(true, false, true);
         * assert t1.hashCode() == t2.hashCode(); // returns true (equal tuples have equal hash codes)
         * t1.hashCode();                         // returns 1222569
         *
         * // All-false
         * BooleanTuple.BooleanTuple3 allFalse = BooleanTuple.of(false, false, false);
         * allFalse.hashCode();   // returns 1228341
         * }</pre>
         *
         * @return a hash code value calculated from all three elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (_1 ? 1231 : 1237) + (_2 ? 1231 : 1237))) + (_3 ? 1231 : 1237);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple3 t1 = BooleanTuple.of(true, false, true);
         * BooleanTuple.BooleanTuple3 t2 = BooleanTuple.of(true, false, true);
         * t1.equals(t2);   // returns true
         * t1.equals(t1);   // returns true (reflexive)
         *
         * // Different first element
         * BooleanTuple.BooleanTuple3 t3 = BooleanTuple.of(false, false, true);
         * t1.equals(t3);   // returns false
         *
         * // Null and different arity are never equal
         * t1.equals(null);                          // returns false
         * t1.equals(BooleanTuple.of(true, false));  // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a BooleanTuple.BooleanTuple3 with the same elements in the same order, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple3 t = BooleanTuple.of(true, false, true);
         * t.toString();   // returns "(true, false, true)"
         *
         * BooleanTuple.BooleanTuple3 allTrue = BooleanTuple.of(true, true, true);
         * allTrue.toString();   // returns "(true, true, true)"
         *
         * BooleanTuple.BooleanTuple3 allFalse = BooleanTuple.of(false, false, false);
         * allFalse.toString();   // returns "(false, false, false)"
         * }</pre>
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
     * BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
     * boolean first = tuple._1;                // true
     * boolean fourth = tuple._4;               // false
     * boolean hasTrue = tuple.contains(true);  // true
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
         * tuple.arity();   // returns 4
         *
         * BooleanTuple.BooleanTuple4 allTrue = BooleanTuple.of(true, true, true, true);
         * allTrue.arity();   // returns 4
         *
         * // Arity is independent of element values
         * BooleanTuple.BooleanTuple4 allFalse = BooleanTuple.of(false, false, false, false);
         * allFalse.arity();   // returns 4
         * }</pre>
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
         * BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
         * BooleanTuple.BooleanTuple4 reversed = tuple.reverse();
         * reversed.toString();   // returns "(false, true, false, true)"
         *
         * // All-same values: reverse looks identical
         * BooleanTuple.BooleanTuple4 allTrue = BooleanTuple.of(true, true, true, true);
         * allTrue.reverse().toString();   // returns "(true, true, true, true)"
         *
         * // reverse() returns a NEW instance
         * assert tuple.reverse() != tuple; // true
         * }</pre>
         *
         * @return a new BooleanTuple.BooleanTuple4 with the elements in reverse order
         */
        @Override
        public BooleanTuple4 reverse() {
            return new BooleanTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple4 mixed = BooleanTuple.of(true, false, true, false);
         * mixed.contains(true);    // returns true
         * mixed.contains(false);   // returns true
         *
         * // All-true: false is absent
         * BooleanTuple.BooleanTuple4 allTrue = BooleanTuple.of(true, true, true, true);
         * allTrue.contains(true);    // returns true
         * allTrue.contains(false);   // returns false
         *
         * // All-false: true is absent
         * BooleanTuple.BooleanTuple4 allFalse = BooleanTuple.of(false, false, false, false);
         * allFalse.contains(true);   // returns false
         * allFalse.contains(false);  // returns true
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple4 tuple = BooleanTuple.of(true, false, true, false);
         * List<Boolean> list = new ArrayList<>();
         * tuple.forEach(b -> list.add(b));   // list becomes [true, false, true, false]
         *
         * // Count true values
         * java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger();
         * tuple.forEach(b -> { if (b) count.incrementAndGet(); });
         * // count is 2
         *
         * // All-false: count stays 0
         * BooleanTuple.BooleanTuple4 allFalse = BooleanTuple.of(false, false, false, false);
         * java.util.concurrent.atomic.AtomicInteger count2 = new java.util.concurrent.atomic.AtomicInteger();
         * allFalse.forEach(b -> { if (b) count2.incrementAndGet(); });
         * // count2 is 0
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
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
         * BooleanTuple.BooleanTuple4 t1 = BooleanTuple.of(true, false, true, false);
         * BooleanTuple.BooleanTuple4 t2 = BooleanTuple.of(true, false, true, false);
         * assert t1.hashCode() == t2.hashCode(); // returns true (equal tuples have equal hash codes)
         *
         * // All-true
         * BooleanTuple.BooleanTuple4 allTrue = BooleanTuple.of(true, true, true, true);
         * allTrue.hashCode();   // returns 37895104
         * }</pre>
         *
         * @return a hash code value calculated from all four elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (_1 ? 1231 : 1237) + (_2 ? 1231 : 1237)) + (_3 ? 1231 : 1237))) + (_4 ? 1231 : 1237);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple4 t1 = BooleanTuple.of(true, false, true, false);
         * BooleanTuple.BooleanTuple4 t2 = BooleanTuple.of(true, false, true, false);
         * t1.equals(t2);   // returns true
         * t1.equals(t1);   // returns true (reflexive)
         *
         * // Different first element is not equal
         * BooleanTuple.BooleanTuple4 t3 = BooleanTuple.of(false, false, true, false);
         * t1.equals(t3);   // returns false
         *
         * // Null and different arity are never equal
         * t1.equals(null);                                    // returns false
         * t1.equals(BooleanTuple.of(true, false, true));      // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a BooleanTuple.BooleanTuple4 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final BooleanTuple4 other)) {
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
         * BooleanTuple.BooleanTuple4 t = BooleanTuple.of(true, false, true, false);
         * t.toString();   // returns "(true, false, true, false)"
         *
         * BooleanTuple.BooleanTuple4 allTrue = BooleanTuple.of(true, true, true, true);
         * allTrue.toString();   // returns "(true, true, true, true)"
         *
         * BooleanTuple.BooleanTuple4 allFalse = BooleanTuple.of(false, false, false, false);
         * allFalse.toString();   // returns "(false, false, false, false)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3, element4)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
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
     * BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
     * boolean first = tuple._1;                  // true
     * boolean fifth = tuple._5;                  // true
     * boolean hasFalse = tuple.contains(false);  // true
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
         * tuple.arity();   // returns 5
         *
         * BooleanTuple.BooleanTuple5 allTrue = BooleanTuple.of(true, true, true, true, true);
         * allTrue.arity();   // returns 5
         *
         * // Arity is independent of element values
         * BooleanTuple.BooleanTuple5 allFalse = BooleanTuple.of(false, false, false, false, false);
         * allFalse.arity();   // returns 5
         * }</pre>
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
         * // Palindrome: reversing yields the same sequence
         * BooleanTuple.BooleanTuple5 palindrome = BooleanTuple.of(true, false, true, false, true);
         * palindrome.reverse().toString();   // returns "(true, false, true, false, true)"
         *
         * // Non-palindrome: reversed differs from original
         * BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, false, false, false);
         * tuple.reverse().toString();   // returns "(false, false, false, false, true)"
         *
         * // All-same: reverse is identical in value
         * BooleanTuple.BooleanTuple5 allTrue = BooleanTuple.of(true, true, true, true, true);
         * allTrue.reverse().toString();   // returns "(true, true, true, true, true)"
         *
         * // reverse() returns a NEW instance
         * assert palindrome.reverse() != palindrome; // true
         * }</pre>
         *
         * @return a new BooleanTuple.BooleanTuple5 with the elements in reverse order
         */
        @Override
        public BooleanTuple5 reverse() {
            return new BooleanTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple5 mixed = BooleanTuple.of(true, false, true, false, true);
         * mixed.contains(true);    // returns true
         * mixed.contains(false);   // returns true
         *
         * // All-true: false is absent
         * BooleanTuple.BooleanTuple5 allTrue = BooleanTuple.of(true, true, true, true, true);
         * allTrue.contains(true);    // returns true
         * allTrue.contains(false);   // returns false
         *
         * // All-false: true is absent
         * BooleanTuple.BooleanTuple5 allFalse = BooleanTuple.of(false, false, false, false, false);
         * allFalse.contains(true);   // returns false
         * allFalse.contains(false);  // returns true
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple5 tuple = BooleanTuple.of(true, false, true, false, true);
         * List<Boolean> list = new ArrayList<>();
         * tuple.forEach(b -> list.add(b));   // list becomes [true, false, true, false, true]
         *
         * // Count true values
         * java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger();
         * tuple.forEach(b -> { if (b) count.incrementAndGet(); });
         * // count is 3
         *
         * // All-false: count stays 0
         * BooleanTuple.BooleanTuple5 allFalse = BooleanTuple.of(false, false, false, false, false);
         * java.util.concurrent.atomic.AtomicInteger count2 = new java.util.concurrent.atomic.AtomicInteger();
         * allFalse.forEach(b -> { if (b) count2.incrementAndGet(); });
         * // count2 is 0
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
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
         * BooleanTuple.BooleanTuple5 t1 = BooleanTuple.of(true, false, true, false, true);
         * BooleanTuple.BooleanTuple5 t2 = BooleanTuple.of(true, false, true, false, true);
         * assert t1.hashCode() == t2.hashCode(); // returns true (equal tuples have equal hash codes)
         *
         * // All-true
         * BooleanTuple.BooleanTuple5 allTrue = BooleanTuple.of(true, true, true, true, true);
         * allTrue.hashCode();   // returns 1174749455
         * }</pre>
         *
         * @return a hash code value calculated from all five elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (_1 ? 1231 : 1237) + (_2 ? 1231 : 1237)) + (_3 ? 1231 : 1237)) + (_4 ? 1231 : 1237))) + (_5 ? 1231 : 1237);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple5 t1 = BooleanTuple.of(true, false, true, false, true);
         * BooleanTuple.BooleanTuple5 t2 = BooleanTuple.of(true, false, true, false, true);
         * t1.equals(t2);   // returns true
         * t1.equals(t1);   // returns true (reflexive)
         *
         * // Different last element is not equal
         * BooleanTuple.BooleanTuple5 t3 = BooleanTuple.of(true, false, true, false, false);
         * t1.equals(t3);   // returns false
         *
         * // Null and different arity are never equal
         * t1.equals(null);                                         // returns false
         * t1.equals(BooleanTuple.of(true, false, true, false));    // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a BooleanTuple.BooleanTuple5 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final BooleanTuple5 other)) {
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
         * // Basic: alternating true/false
         * BooleanTuple.BooleanTuple5 t = BooleanTuple.of(true, false, true, false, true);
         * String s = t.toString();             // returns "(true, false, true, false, true)"
         *
         * // Basic: all false
         * BooleanTuple.BooleanTuple5 t2 = BooleanTuple.of(false, false, false, false, false);
         * String s2 = t2.toString();           // returns "(false, false, false, false, false)"
         *
         * // Edge: all true
         * BooleanTuple.BooleanTuple5 t3 = BooleanTuple.of(true, true, true, true, true);
         * String s3 = t3.toString();           // returns "(true, true, true, true, true)"
         *
         * // Edge: false-leading pattern
         * BooleanTuple.BooleanTuple5 t4 = BooleanTuple.of(false, true, false, true, false);
         * String s4 = t4.toString();           // returns "(false, true, false, true, false)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3, element4, element5)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ")";
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
     * BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
     * boolean first = tuple._1;                               // true
     * boolean sixth = tuple._6;                               // false
     * BooleanTuple.BooleanTuple6 reversed = tuple.reverse();  // (false, true, false, true, false, true)
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
         * int n = tuple.arity();   // returns 6
         *
         * // All-true tuple also has arity 6
         * BooleanTuple.BooleanTuple6 allTrue = BooleanTuple.of(true, true, true, true, true, true);
         * int n2 = allTrue.arity();   // returns 6
         *
         * // All-false tuple also has arity 6
         * BooleanTuple.BooleanTuple6 allFalse = BooleanTuple.of(false, false, false, false, false, false);
         * int n3 = allFalse.arity();   // returns 6
         *
         * // Arity is independent of element values
         * BooleanTuple.BooleanTuple6 mixed = BooleanTuple.of(true, false, false, true, true, false);
         * int n4 = mixed.arity();   // returns 6
         * }</pre>
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
         * // Basic reversal of alternating values
         * BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
         * BooleanTuple.BooleanTuple6 reversed = tuple.reverse();   // returns (false, true, false, true, false, true)
         *
         * // Reversing all-true tuple yields same result
         * BooleanTuple.BooleanTuple6 allTrue = BooleanTuple.of(true, true, true, true, true, true);
         * BooleanTuple.BooleanTuple6 rev2 = allTrue.reverse();   // returns (true, true, true, true, true, true)
         *
         * // Reversing all-false tuple yields same result
         * BooleanTuple.BooleanTuple6 allFalse = BooleanTuple.of(false, false, false, false, false, false);
         * BooleanTuple.BooleanTuple6 rev3 = allFalse.reverse();   // returns (false, false, false, false, false, false)
         *
         * // Reversing a mixed tuple; result is a different object
         * BooleanTuple.BooleanTuple6 mixed = BooleanTuple.of(true, false, false, true, false, true);
         * BooleanTuple.BooleanTuple6 rev4 = mixed.reverse();   // returns (true, false, true, false, false, true)
         * }</pre>
         *
         * @return a new BooleanTuple.BooleanTuple6 with the elements in reverse order
         */
        @Override
        public BooleanTuple6 reverse() {
            return new BooleanTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // Tuple with mixed values contains both true and false
         * BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
         * boolean hasTrue = tuple.contains(true);    // returns true
         * boolean hasFalse = tuple.contains(false);  // returns true
         *
         * // All-true tuple does not contain false
         * BooleanTuple.BooleanTuple6 allTrue = BooleanTuple.of(true, true, true, true, true, true);
         * boolean t2 = allTrue.contains(true);    // returns true
         * boolean f2 = allTrue.contains(false);   // returns false
         *
         * // All-false tuple does not contain true
         * BooleanTuple.BooleanTuple6 allFalse = BooleanTuple.of(false, false, false, false, false, false);
         * boolean f3 = allFalse.contains(false);  // returns true
         * boolean t3 = allFalse.contains(true);   // returns false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // Collect all elements into a list
         * BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
         * java.util.List<Boolean> list = new java.util.ArrayList<>();
         * tuple.forEach(list::add);   // list becomes [true, false, true, false, true, false]
         *
         * // Count how many elements are true
         * BooleanTuple.BooleanTuple6 allTrue = BooleanTuple.of(true, true, true, true, true, true);
         * java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger();
         * allTrue.forEach(v -> { if (v) count.incrementAndGet(); });   // count becomes 6
         *
         * // All-false tuple: count of true remains 0
         * BooleanTuple.BooleanTuple6 allFalse = BooleanTuple.of(false, false, false, false, false, false);
         * java.util.concurrent.atomic.AtomicInteger count2 = new java.util.concurrent.atomic.AtomicInteger();
         * allFalse.forEach(v -> { if (v) count2.incrementAndGet(); });   // count2 remains 0
         *
         * // Null action throws IllegalArgumentException
         * // tuple.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
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
         * // Alternating true/false tuple
         * BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
         * int h1 = tuple.hashCode();   // returns 2063042866
         *
         * // All-true tuple has a distinct hash
         * BooleanTuple.BooleanTuple6 allTrue = BooleanTuple.of(true, true, true, true, true, true);
         * int h2 = allTrue.hashCode();   // returns 2057495968
         *
         * // All-false tuple has a distinct hash
         * BooleanTuple.BooleanTuple6 allFalse = BooleanTuple.of(false, false, false, false, false, false);
         * int h3 = allFalse.hashCode();   // returns -2059970592
         *
         * // Equal tuples always produce the same hash code
         * BooleanTuple.BooleanTuple6 copy = BooleanTuple.of(true, false, true, false, true, false);
         * boolean sameHash = tuple.hashCode() == copy.hashCode(); // returns true
         * }</pre>
         *
         * @return a hash code value calculated from all six elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31 * (31 * (31 * (_1 ? 1231 : 1237) + (_2 ? 1231 : 1237)) + (_3 ? 1231 : 1237)) + (_4 ? 1231 : 1237)) + (_5 ? 1231 : 1237)))
                    + (_6 ? 1231 : 1237);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple6 t1 = BooleanTuple.of(true, false, true, false, true, false);
         * BooleanTuple.BooleanTuple6 t2 = BooleanTuple.of(true, false, true, false, true, false);
         * BooleanTuple.BooleanTuple6 t3 = BooleanTuple.of(false, true, false, true, false, true);
         *
         * // Equal tuples with identical element values
         * boolean eq1 = t1.equals(t2);   // returns true
         *
         * // Different element values yield false
         * boolean eq2 = t1.equals(t3);   // returns false
         *
         * // Reflexive: a tuple equals itself
         * boolean eq3 = t1.equals(t1);   // returns true
         *
         * // Comparison with null or different type returns false
         * boolean eq4 = t1.equals(null);             // returns false
         * boolean eq5 = t1.equals("not a tuple");    // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a BooleanTuple.BooleanTuple6 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final BooleanTuple6 other)) {
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
         * // Alternating values
         * BooleanTuple.BooleanTuple6 tuple = BooleanTuple.of(true, false, true, false, true, false);
         * String s = tuple.toString();   // returns "(true, false, true, false, true, false)"
         *
         * // All-true tuple
         * BooleanTuple.BooleanTuple6 allTrue = BooleanTuple.of(true, true, true, true, true, true);
         * String s2 = allTrue.toString();   // returns "(true, true, true, true, true, true)"
         *
         * // All-false tuple
         * BooleanTuple.BooleanTuple6 allFalse = BooleanTuple.of(false, false, false, false, false, false);
         * String s3 = allFalse.toString();   // returns "(false, false, false, false, false, false)"
         *
         * // Mixed tuple with duplicates
         * BooleanTuple.BooleanTuple6 mixed = BooleanTuple.of(true, true, false, false, true, false);
         * String s4 = mixed.toString();   // returns "(true, true, false, false, true, false)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3, element4, element5, element6)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ")";
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
     * BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
     * boolean first = tuple._1;           // true
     * boolean seventh = tuple._7;         // true
     * boolean[] array = tuple.toArray();  // [true, false, true, false, true, false, true]
     * }</pre>
     *
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
         * int n = tuple.arity();   // returns 7
         *
         * // All-true tuple also has arity 7
         * BooleanTuple.BooleanTuple7 allTrue = BooleanTuple.of(true, true, true, true, true, true, true);
         * int n2 = allTrue.arity();   // returns 7
         *
         * // All-false tuple also has arity 7
         * BooleanTuple.BooleanTuple7 allFalse = BooleanTuple.of(false, false, false, false, false, false, false);
         * int n3 = allFalse.arity();   // returns 7
         *
         * // Arity is independent of element values
         * BooleanTuple.BooleanTuple7 mixed = BooleanTuple.of(true, false, false, true, false, true, false);
         * int n4 = mixed.arity();   // returns 7
         * }</pre>
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
         * // Non-palindrome tuple; reverse is not equal to original
         * BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, false);
         * BooleanTuple.BooleanTuple7 reversed = tuple.reverse();   // returns (false, false, true, false, true, false, true)
         *
         * // Palindrome tuple reverses to itself
         * BooleanTuple.BooleanTuple7 palindrome = BooleanTuple.of(true, false, true, false, true, false, true);
         * BooleanTuple.BooleanTuple7 rev2 = palindrome.reverse();   // returns (true, false, true, false, true, false, true)
         *
         * // All-true tuple reverses to itself
         * BooleanTuple.BooleanTuple7 allTrue = BooleanTuple.of(true, true, true, true, true, true, true);
         * BooleanTuple.BooleanTuple7 rev3 = allTrue.reverse();   // returns (true, true, true, true, true, true, true)
         *
         * // All-false tuple reverses to itself
         * BooleanTuple.BooleanTuple7 allFalse = BooleanTuple.of(false, false, false, false, false, false, false);
         * BooleanTuple.BooleanTuple7 rev4 = allFalse.reverse();   // returns (false, false, false, false, false, false, false)
         * }</pre>
         *
         * @return a new BooleanTuple.BooleanTuple7 with the elements in reverse order
         */
        @Override
        public BooleanTuple7 reverse() {
            return new BooleanTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // Tuple with mixed values contains both true and false
         * BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
         * boolean hasTrue = tuple.contains(true);    // returns true
         * boolean hasFalse = tuple.contains(false);  // returns true
         *
         * // All-true tuple does not contain false
         * BooleanTuple.BooleanTuple7 allTrue = BooleanTuple.of(true, true, true, true, true, true, true);
         * boolean t2 = allTrue.contains(true);    // returns true
         * boolean f2 = allTrue.contains(false);   // returns false
         *
         * // All-false tuple does not contain true
         * BooleanTuple.BooleanTuple7 allFalse = BooleanTuple.of(false, false, false, false, false, false, false);
         * boolean f3 = allFalse.contains(false);  // returns true
         * boolean t3 = allFalse.contains(true);   // returns false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // Collect all elements into a list
         * BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
         * java.util.List<Boolean> list = new java.util.ArrayList<>();
         * tuple.forEach(list::add);   // list becomes [true, false, true, false, true, false, true]
         *
         * // Count how many elements are true
         * BooleanTuple.BooleanTuple7 allTrue = BooleanTuple.of(true, true, true, true, true, true, true);
         * java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger();
         * allTrue.forEach(v -> { if (v) count.incrementAndGet(); });   // count becomes 7
         *
         * // All-false tuple: count of true remains 0
         * BooleanTuple.BooleanTuple7 allFalse = BooleanTuple.of(false, false, false, false, false, false, false);
         * java.util.concurrent.atomic.AtomicInteger count2 = new java.util.concurrent.atomic.AtomicInteger();
         * allFalse.forEach(v -> { if (v) count2.incrementAndGet(); });   // count2 remains 0
         *
         * // Null action throws IllegalArgumentException
         * // tuple.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
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
         * // Alternating true/false tuple
         * BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
         * int h1 = tuple.hashCode();   // returns -470179363
         *
         * // All-true tuple has a distinct hash
         * BooleanTuple.BooleanTuple7 allTrue = BooleanTuple.of(true, true, true, true, true, true, true);
         * int h2 = allTrue.hashCode();   // returns -642133201
         *
         * // All-false tuple has a distinct hash
         * BooleanTuple.BooleanTuple7 allFalse = BooleanTuple.of(false, false, false, false, false, false, false);
         * int h3 = allFalse.hashCode();   // returns 565422325
         *
         * // Equal tuples always produce the same hash code
         * BooleanTuple.BooleanTuple7 copy = BooleanTuple.of(true, false, true, false, true, false, true);
         * boolean sameHash = tuple.hashCode() == copy.hashCode(); // returns true
         * }</pre>
         *
         * @return a hash code value calculated from all seven elements
         */
        @Override
        public int hashCode() {
            return (31
                    * (31 * (31 * (31 * (31 * (31 * (_1 ? 1231 : 1237) + (_2 ? 1231 : 1237)) + (_3 ? 1231 : 1237)) + (_4 ? 1231 : 1237)) + (_5 ? 1231 : 1237))
                            + (_6 ? 1231 : 1237)))
                    + (_7 ? 1231 : 1237);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple7 t1 = BooleanTuple.of(true, false, true, false, true, false, true);
         * BooleanTuple.BooleanTuple7 t2 = BooleanTuple.of(true, false, true, false, true, false, true);
         * BooleanTuple.BooleanTuple7 t3 = BooleanTuple.of(false, true, false, true, false, true, false);
         *
         * // Equal tuples with identical element values
         * boolean eq1 = t1.equals(t2);   // returns true
         *
         * // Different element values yield false
         * boolean eq2 = t1.equals(t3);   // returns false
         *
         * // Reflexive: a tuple equals itself
         * boolean eq3 = t1.equals(t1);   // returns true
         *
         * // Comparison with null or different type returns false
         * boolean eq4 = t1.equals(null);             // returns false
         * boolean eq5 = t1.equals("not a tuple");    // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a BooleanTuple.BooleanTuple7 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final BooleanTuple7 other)) {
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
         * // Alternating values
         * BooleanTuple.BooleanTuple7 tuple = BooleanTuple.of(true, false, true, false, true, false, true);
         * String s = tuple.toString();   // returns "(true, false, true, false, true, false, true)"
         *
         * // All-true tuple
         * BooleanTuple.BooleanTuple7 allTrue = BooleanTuple.of(true, true, true, true, true, true, true);
         * String s2 = allTrue.toString();   // returns "(true, true, true, true, true, true, true)"
         *
         * // All-false tuple
         * BooleanTuple.BooleanTuple7 allFalse = BooleanTuple.of(false, false, false, false, false, false, false);
         * String s3 = allFalse.toString();   // returns "(false, false, false, false, false, false, false)"
         *
         * // Mixed tuple with duplicates
         * BooleanTuple.BooleanTuple7 mixed = BooleanTuple.of(true, true, false, false, true, false, true);
         * String s4 = mixed.toString();   // returns "(true, true, false, false, true, false, true)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3, element4, element5, element6, element7)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ")";
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
     * BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
     * boolean first = tuple._1;   // true
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
         * int n = tuple.arity();   // returns 8
         *
         * // All-true tuple also has arity 8
         * BooleanTuple.BooleanTuple8 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true);
         * int n2 = allTrue.arity();   // returns 8
         *
         * // All-false tuple also has arity 8
         * BooleanTuple.BooleanTuple8 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false);
         * int n3 = allFalse.arity();   // returns 8
         *
         * // Arity is independent of element values
         * BooleanTuple.BooleanTuple8 mixed = BooleanTuple.of(true, false, false, true, false, true, false, true);
         * int n4 = mixed.arity();   // returns 8
         * }</pre>
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
         * // Basic reversal of alternating values
         * BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
         * BooleanTuple.BooleanTuple8 reversed = tuple.reverse();   // returns (false, true, false, true, false, true, false, true)
         *
         * // All-true tuple reverses to itself
         * BooleanTuple.BooleanTuple8 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true);
         * BooleanTuple.BooleanTuple8 rev2 = allTrue.reverse();   // returns (true, true, true, true, true, true, true, true)
         *
         * // All-false tuple reverses to itself
         * BooleanTuple.BooleanTuple8 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false);
         * BooleanTuple.BooleanTuple8 rev3 = allFalse.reverse();   // returns (false, false, false, false, false, false, false, false)
         *
         * // Palindrome tuple; reversal equals the original
         * BooleanTuple.BooleanTuple8 palindrome = BooleanTuple.of(true, false, false, false, false, false, false, true);
         * BooleanTuple.BooleanTuple8 rev4 = palindrome.reverse();   // returns (true, false, false, false, false, false, false, true)
         * }</pre>
         *
         * @return a new BooleanTuple.BooleanTuple8 with the elements in reverse order
         */
        @Override
        public BooleanTuple8 reverse() {
            return new BooleanTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // Tuple with mixed values contains both true and false
         * BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
         * boolean hasTrue = tuple.contains(true);    // returns true
         * boolean hasFalse = tuple.contains(false);  // returns true
         *
         * // All-true tuple does not contain false
         * BooleanTuple.BooleanTuple8 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true);
         * boolean t2 = allTrue.contains(true);    // returns true
         * boolean f2 = allTrue.contains(false);   // returns false
         *
         * // All-false tuple does not contain true
         * BooleanTuple.BooleanTuple8 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false);
         * boolean f3 = allFalse.contains(false);  // returns true
         * boolean t3 = allFalse.contains(true);   // returns false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // Collect all elements into a list
         * BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
         * java.util.List<Boolean> list = new java.util.ArrayList<>();
         * tuple.forEach(list::add);   // list becomes [true, false, true, false, true, false, true, false]
         *
         * // Count how many elements are true
         * BooleanTuple.BooleanTuple8 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true);
         * java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger();
         * allTrue.forEach(v -> { if (v) count.incrementAndGet(); });   // count becomes 8
         *
         * // All-false tuple: count of true remains 0
         * BooleanTuple.BooleanTuple8 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false);
         * java.util.concurrent.atomic.AtomicInteger count2 = new java.util.concurrent.atomic.AtomicInteger();
         * allFalse.forEach(v -> { if (v) count2.incrementAndGet(); });   // count2 remains 0
         *
         * // Null action throws IllegalArgumentException
         * // tuple.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
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
         * // Alternating true/false tuple
         * BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
         * int h1 = tuple.hashCode();   // returns -1690657128
         *
         * // All-true tuple has a distinct hash
         * BooleanTuple.BooleanTuple8 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true);
         * int h2 = allTrue.hashCode();   // returns 1568708480
         *
         * // All-false tuple has a distinct hash
         * BooleanTuple.BooleanTuple8 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false);
         * int h3 = allFalse.hashCode();   // returns 348224128
         *
         * // Equal tuples always produce the same hash code
         * BooleanTuple.BooleanTuple8 copy = BooleanTuple.of(true, false, true, false, true, false, true, false);
         * boolean sameHash = tuple.hashCode() == copy.hashCode(); // returns true
         * }</pre>
         *
         * @return a hash code value calculated from all eight elements
         */
        @Override
        public int hashCode() {
            return (31 * (31
                    * (31 * (31 * (31 * (31 * (31 * (_1 ? 1231 : 1237) + (_2 ? 1231 : 1237)) + (_3 ? 1231 : 1237)) + (_4 ? 1231 : 1237)) + (_5 ? 1231 : 1237))
                            + (_6 ? 1231 : 1237))
                    + (_7 ? 1231 : 1237))) + (_8 ? 1231 : 1237);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple8 t1 = BooleanTuple.of(true, false, true, false, true, false, true, false);
         * BooleanTuple.BooleanTuple8 t2 = BooleanTuple.of(true, false, true, false, true, false, true, false);
         * BooleanTuple.BooleanTuple8 t3 = BooleanTuple.of(false, true, false, true, false, true, false, true);
         *
         * // Equal tuples with identical element values
         * boolean eq1 = t1.equals(t2);   // returns true
         *
         * // Different element values yield false
         * boolean eq2 = t1.equals(t3);   // returns false
         *
         * // Reflexive: a tuple equals itself
         * boolean eq3 = t1.equals(t1);   // returns true
         *
         * // Comparison with null or different type returns false
         * boolean eq4 = t1.equals(null);             // returns false
         * boolean eq5 = t1.equals("not a tuple");    // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a BooleanTuple.BooleanTuple8 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final BooleanTuple8 other)) {
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
         * // Alternating values
         * BooleanTuple.BooleanTuple8 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false);
         * String s = tuple.toString();   // returns "(true, false, true, false, true, false, true, false)"
         *
         * // All-true tuple
         * BooleanTuple.BooleanTuple8 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true);
         * String s2 = allTrue.toString();   // returns "(true, true, true, true, true, true, true, true)"
         *
         * // All-false tuple
         * BooleanTuple.BooleanTuple8 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false);
         * String s3 = allFalse.toString();   // returns "(false, false, false, false, false, false, false, false)"
         *
         * // Mixed tuple with duplicates
         * BooleanTuple.BooleanTuple8 mixed = BooleanTuple.of(true, true, false, false, true, false, true, false);
         * String s4 = mixed.toString();   // returns "(true, true, false, false, true, false, true, false)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, ..., element8)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")";
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
     * BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
     * boolean first = tuple._1;   // true
     * boolean ninth = tuple._9;   // true
     * int arity = tuple.arity();  // 9
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
         * int n = tuple.arity();   // returns 9
         *
         * // All-true tuple also has arity 9
         * BooleanTuple.BooleanTuple9 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true, true);
         * int n2 = allTrue.arity();   // returns 9
         *
         * // All-false tuple also has arity 9
         * BooleanTuple.BooleanTuple9 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false, false);
         * int n3 = allFalse.arity();   // returns 9
         *
         * // Arity is independent of element values
         * BooleanTuple.BooleanTuple9 mixed = BooleanTuple.of(true, false, false, true, false, true, false, true, false);
         * int n4 = mixed.arity();   // returns 9
         * }</pre>
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
         * // Palindrome tuple reverses to itself
         * BooleanTuple.BooleanTuple9 palindrome = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
         * BooleanTuple.BooleanTuple9 rev1 = palindrome.reverse();   // returns (true, false, true, false, true, false, true, false, true)
         *
         * // Non-palindrome reversal
         * BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, false, false, false, false, false, false, false);
         * BooleanTuple.BooleanTuple9 rev2 = tuple.reverse();   // returns (false, false, false, false, false, false, false, false, true)
         *
         * // All-true tuple reverses to itself
         * BooleanTuple.BooleanTuple9 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true, true);
         * BooleanTuple.BooleanTuple9 rev3 = allTrue.reverse();   // returns (true, true, true, true, true, true, true, true, true)
         *
         * // All-false tuple reverses to itself
         * BooleanTuple.BooleanTuple9 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false, false);
         * BooleanTuple.BooleanTuple9 rev4 = allFalse.reverse();   // returns (false, false, false, false, false, false, false, false, false)
         * }</pre>
         *
         * @return a new BooleanTuple.BooleanTuple9 with the elements in reverse order
         */
        @Override
        public BooleanTuple9 reverse() {
            return new BooleanTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified boolean value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // Tuple with mixed values contains both true and false
         * BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
         * boolean hasTrue = tuple.contains(true);    // returns true
         * boolean hasFalse = tuple.contains(false);  // returns true
         *
         * // All-true tuple does not contain false
         * BooleanTuple.BooleanTuple9 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true, true);
         * boolean t2 = allTrue.contains(true);    // returns true
         * boolean f2 = allTrue.contains(false);   // returns false
         *
         * // All-false tuple does not contain true
         * BooleanTuple.BooleanTuple9 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false, false);
         * boolean f3 = allFalse.contains(false);  // returns true
         * boolean t3 = allFalse.contains(true);   // returns false
         * }</pre>
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // Collect all elements into a list
         * BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
         * java.util.List<Boolean> list = new java.util.ArrayList<>();
         * tuple.forEach(list::add);   // list becomes [true, false, true, false, true, false, true, false, true]
         *
         * // Count how many elements are true
         * BooleanTuple.BooleanTuple9 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true, true);
         * java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger();
         * allTrue.forEach(v -> { if (v) count.incrementAndGet(); });   // count becomes 9
         *
         * // All-false tuple: count of true remains 0
         * BooleanTuple.BooleanTuple9 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false, false);
         * java.util.concurrent.atomic.AtomicInteger count2 = new java.util.concurrent.atomic.AtomicInteger();
         * allFalse.forEach(v -> { if (v) count2.incrementAndGet(); });   // count2 remains 0
         *
         * // Null action throws IllegalArgumentException
         * // tuple.forEach(null);   // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> action) throws E {
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
         * // Alternating true/false tuple
         * BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
         * int h1 = tuple.hashCode();   // returns -870762185
         *
         * // All-true tuple has a distinct hash
         * BooleanTuple.BooleanTuple9 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true, true);
         * int h2 = allTrue.hashCode();   // returns 1385323855
         *
         * // All-false tuple has a distinct hash
         * BooleanTuple.BooleanTuple9 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false, false);
         * int h3 = allFalse.hashCode();   // returns -2089952683
         *
         * // Equal tuples always produce the same hash code
         * BooleanTuple.BooleanTuple9 copy = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
         * boolean sameHash = tuple.hashCode() == copy.hashCode(); // returns true
         * }</pre>
         *
         * @return a hash code value calculated from all nine elements
         */
        @Override
        public int hashCode() {
            return (31 * (31 * (31
                    * (31 * (31 * (31 * (31 * (31 * (_1 ? 1231 : 1237) + (_2 ? 1231 : 1237)) + (_3 ? 1231 : 1237)) + (_4 ? 1231 : 1237)) + (_5 ? 1231 : 1237))
                            + (_6 ? 1231 : 1237))
                    + (_7 ? 1231 : 1237)) + (_8 ? 1231 : 1237))) + (_9 ? 1231 : 1237);
        }

        /**
         * Compares this tuple to the specified object for equality.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * BooleanTuple.BooleanTuple9 t1 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
         * BooleanTuple.BooleanTuple9 t2 = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
         * BooleanTuple.BooleanTuple9 t3 = BooleanTuple.of(false, true, false, true, false, true, false, true, false);
         *
         * // Equal tuples with identical element values
         * boolean eq1 = t1.equals(t2);   // returns true
         *
         * // Different element values yield false
         * boolean eq2 = t1.equals(t3);   // returns false
         *
         * // Reflexive: a tuple equals itself
         * boolean eq3 = t1.equals(t1);   // returns true
         *
         * // Comparison with null or different type returns false
         * boolean eq4 = t1.equals(null);             // returns false
         * boolean eq5 = t1.equals("not a tuple");    // returns false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a BooleanTuple.BooleanTuple9 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final BooleanTuple9 other)) {
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
         * // Alternating values
         * BooleanTuple.BooleanTuple9 tuple = BooleanTuple.of(true, false, true, false, true, false, true, false, true);
         * String s = tuple.toString();   // returns "(true, false, true, false, true, false, true, false, true)"
         *
         * // All-true tuple
         * BooleanTuple.BooleanTuple9 allTrue = BooleanTuple.of(true, true, true, true, true, true, true, true, true);
         * String s2 = allTrue.toString();   // returns "(true, true, true, true, true, true, true, true, true)"
         *
         * // All-false tuple
         * BooleanTuple.BooleanTuple9 allFalse = BooleanTuple.of(false, false, false, false, false, false, false, false, false);
         * String s3 = allFalse.toString();   // returns "(false, false, false, false, false, false, false, false, false)"
         *
         * // Mixed tuple with duplicates
         * BooleanTuple.BooleanTuple9 mixed = BooleanTuple.of(true, true, false, false, true, false, true, false, true);
         * String s4 = mixed.toString();   // returns "(true, true, false, false, true, false, true, false, true)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, ..., element9)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ")";
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
