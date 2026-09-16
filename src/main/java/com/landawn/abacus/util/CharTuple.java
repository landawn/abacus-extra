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
import com.landawn.abacus.util.CharTuple.CharTuple0;
import com.landawn.abacus.util.CharTuple.CharTuple1;
import com.landawn.abacus.util.CharTuple.CharTuple2;
import com.landawn.abacus.util.CharTuple.CharTuple3;
import com.landawn.abacus.util.CharTuple.CharTuple4;
import com.landawn.abacus.util.CharTuple.CharTuple5;
import com.landawn.abacus.util.CharTuple.CharTuple6;
import com.landawn.abacus.util.CharTuple.CharTuple7;
import com.landawn.abacus.util.CharTuple.CharTuple8;
import com.landawn.abacus.util.CharTuple.CharTuple9;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.u.OptionalDouble;
import com.landawn.abacus.util.stream.CharStream;

/**
 * Base class for immutable tuples of primitive {@code char} values.
 *
 * <p>The nested tuple types model fixed arities from 0 through 9. Prefer the {@code of(...)} factory
 * overloads to create a specific arity; {@link #from(char[])} is deprecated and retained only for
 * compatibility. The base class supplies aggregate, reversal, containment, conversion, and
 * element-wise functional helpers; {@link PrimitiveTuple} adds whole-tuple
 * {@link PrimitiveTuple#accept(Throwables.Consumer) accept},
 * {@link PrimitiveTuple#map(Throwables.Function) map},
 * {@link PrimitiveTuple#filter(Throwables.Predicate) filter}, and
 * {@link PrimitiveTuple#toOptional() toOptional}.</p>
 *
 * <p>Arity-specific nested types ({@code CharTuple1} through {@code CharTuple9}) expose their components as
 * public final fields {@code _1}, {@code _2}, and so on matching that arity. This sealed hierarchy permits only
 * the built-in nested types; all instances are immutable.</p>
 *
 * <p><b>Numeric semantics:</b> Values are unsigned UTF-16 code units (range {@code 0..65535}).
 * Ordering and arithmetic ({@link #min()}, {@link #max()}, {@link #lowerMedian()}, {@link #sum()},
 * and {@link #average()}) treat each {@code char} as its code-unit value; surrogate
 * code units are not paired or interpreted as code points. {@link #min()}, {@link #max()}, and
 * {@link #lowerMedian()} return {@code char}; {@link #sum()} returns {@code int}; {@link #average()}
 * uses {@code double} precision via {@link OptionalDouble}.
 * Empty-tuple contracts: {@code sum()} is {@code 0}, {@code average()} is empty, and
 * {@code min}/{@code max}/{@code lowerMedian} throw {@link NoSuchElementException}.</p>
 *
 * @param <TP> the concrete {@code CharTuple} subtype that fluent operations such as {@link #reversed()} return
 * @see PrimitiveTuple
 * @see BooleanTuple
 * @see ByteTuple
 * @see ShortTuple
 * @see IntTuple
 * @see LongTuple
 * @see FloatTuple
 * @see DoubleTuple
 */
@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract sealed class CharTuple<TP extends CharTuple<TP>> extends PrimitiveTuple<TP>
        permits CharTuple0, CharTuple1, CharTuple2, CharTuple3, CharTuple4, CharTuple5, CharTuple6, CharTuple7, CharTuple8, CharTuple9 {

    /** Internal element storage; lazily initialized when {@link #elements()} is first called. */
    protected volatile char[] elements;

    /**
     * Protected constructor for subclass instantiation.
     * This constructor is not intended for direct use. Use the static factory methods
     * such as {@link #of(char)}, {@link #of(char, char)}, etc., to create tuple instances.
     */
    protected CharTuple() {
    }

    /**
     * Creates a CharTuple.CharTuple1 containing a single char value.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple1 t = CharTuple.of('A');
     * char value = t._1;                   // 'A'
     * int sum = t.sum();                   // 65 ('A' code unit)
     *
     * // Edge: NUL / zero code unit '\0'
     * CharTuple.CharTuple1 t2 = CharTuple.of('\0');
     * char v2 = t2._1;                     // '\0'
     * int s2 = t2.sum();                   // 0
     * }</pre>
     *
     * @param _1 the char value to store in the tuple
     * @return a new CharTuple.CharTuple1 containing the specified value
     */
    public static CharTuple1 of(final char _1) {
        return new CharTuple1(_1);
    }

    /**
     * Creates a CharTuple.CharTuple2 containing two char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple2 t = CharTuple.of('A', 'B');
     * char first = t._1;                   // 'A'
     * char second = t._2;                  // 'B'
     * int sum = t.sum();                   // 131 (65 + 66)
     *
     * // Edge: uppercase vs lowercase ordering ('Z'=90 < 'a'=97)
     * CharTuple.CharTuple2 t2 = CharTuple.of('Z', 'a');
     * char min = t2.min();                 // 'Z'
     * char max = t2.max();                 // 'a'
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @return a new CharTuple.CharTuple2 containing the specified values
     */
    public static CharTuple2 of(final char _1, final char _2) {
        return new CharTuple2(_1, _2);
    }

    /**
     * Creates a CharTuple.CharTuple3 containing three char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple3 t = CharTuple.of('A', 'B', 'C');
     * char third = t._3;                   // 'C'
     * int sum = t.sum();                   // 198 (65 + 66 + 67)
     *
     * // Edge: out-of-order elements - min/max/lowerMedian operate on sorted values
     * CharTuple.CharTuple3 t2 = CharTuple.of('C', 'A', 'B');
     * char min = t2.min();                 // 'A'
     * char max = t2.max();                 // 'C'
     * char median = t2.lowerMedian();           // 'B'
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @return a new CharTuple.CharTuple3 containing the specified values
     */
    public static CharTuple3 of(final char _1, final char _2, final char _3) {
        return new CharTuple3(_1, _2, _3);
    }

    /**
     * Creates a CharTuple.CharTuple4 containing four char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple4 t = CharTuple.of('A', 'B', 'C', 'D');
     * char fourth = t._4;                  // 'D'
     * int sum = t.sum();                   // 266 (65 + 66 + 67 + 68)
     *
     * // Edge: even arity, lowerMedian returns the lower of two middle values
     * CharTuple.CharTuple4 t2 = CharTuple.of('D', 'A', 'C', 'B');
     * char median = t2.lowerMedian();           // 'B' (sorted: A,B,C,D -> lower middle)
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @param _4 the fourth char value
     * @return a new CharTuple.CharTuple4 containing the specified values
     */
    public static CharTuple4 of(final char _1, final char _2, final char _3, final char _4) {
        return new CharTuple4(_1, _2, _3, _4);
    }

    /**
     * Creates a CharTuple.CharTuple5 containing five char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple5 t = CharTuple.of('A', 'B', 'C', 'D', 'E');
     * char median = t.lowerMedian();             // 'C' (sorted: A,B,C,D,E -> middle index 2)
     * boolean has = t.contains('E');        // true
     *
     * // Edge: all same character
     * CharTuple.CharTuple5 t2 = CharTuple.of('X', 'X', 'X', 'X', 'X');
     * char min = t2.min();                 // 'X'
     * char max = t2.max();                 // 'X'
     * int sum = t2.sum();                  // 440 (88 * 5)
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @param _4 the fourth char value
     * @param _5 the fifth char value
     * @return a new CharTuple.CharTuple5 containing the specified values
     */
    public static CharTuple5 of(final char _1, final char _2, final char _3, final char _4, final char _5) {
        return new CharTuple5(_1, _2, _3, _4, _5);
    }

    /**
     * Creates a CharTuple.CharTuple6 containing six char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple6 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
     * int sum = t.sum();                    // 405 (65+66+67+68+69+70)
     * t.average();                        // returns OptionalDouble.of(67.5)
     *
     * // Edge: reversed order input - result is same-arity tuple
     * CharTuple.CharTuple6 t2 = CharTuple.of('F', 'E', 'D', 'C', 'B', 'A');
     * char min = t2.min();                 // 'A'
     * char max = t2.max();                 // 'F'
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @param _4 the fourth char value
     * @param _5 the fifth char value
     * @param _6 the sixth char value
     * @return a new CharTuple.CharTuple6 containing the specified values
     */
    public static CharTuple6 of(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6) {
        return new CharTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Creates a CharTuple.CharTuple7 containing seven char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple7 t = CharTuple.of('M', 'O', 'N', 'D', 'A', 'Y', 'S');
     * CharTuple.CharTuple7 rev = t.reversed();   // ('S', 'Y', 'A', 'D', 'N', 'O', 'M')
     * char first = rev._1;                      // 'S'
     *
     * // Edge: all distinct chars, arity is exactly 7
     * CharTuple.CharTuple7 t2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
     * int arity = t2.arity();              // 7
     * boolean has = t2.contains('g');      // true
     * }</pre>
     *
     * @param _1 the first char value
     * @param _2 the second char value
     * @param _3 the third char value
     * @param _4 the fourth char value
     * @param _5 the fifth char value
     * @param _6 the sixth char value
     * @param _7 the seventh char value
     * @return a new CharTuple.CharTuple7 containing the specified values
     */
    public static CharTuple7 of(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6, final char _7) {
        return new CharTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * Creates a CharTuple.CharTuple8 containing eight char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple8 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
     * boolean hasX = t.contains('X');      // false
     * boolean hasA = t.contains('A');      // true
     *
     * // Edge: check arity and array length consistency
     * int arity = t.arity();               // 8
     * char[] a = t.toArray();
     * int len = a.length;                // 8
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
     * @return a new CharTuple.CharTuple8 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 8 or more char values
     */
    @Deprecated
    public static CharTuple8 of(final char _1, final char _2, final char _3, final char _4, final char _5, final char _6, final char _7, final char _8) {
        return new CharTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * Creates a CharTuple.CharTuple9 containing nine char values.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple9 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
     * char[] array = t.toArray();       // ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I']
     * int arity = t.arity();            // 9
     *
     * // Edge: maximum arity; no of() overload exists beyond 9
     * CharTuple.CharTuple9 t2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
     * boolean hasI = t2.contains('i');  // true
     * boolean hasZ = t2.contains('z');  // false
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
     * @return a new CharTuple.CharTuple9 containing the specified values
     * @deprecated Consider using a custom class with meaningful property names for better code clarity when dealing with 9 or more char values
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
     * This factory method supports arrays with 0 to 9 elements. For {@code null} or empty
     * arrays, returns the shared empty tuple. For arrays with 1-9 elements, returns the
     * corresponding {@code CharTuple1}..{@code CharTuple9} instance. The values are copied
     * into the new tuple; subsequent modifications to the input array do not affect it.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: 3-element array creates a CharTuple3
     * CharTuple.CharTuple3 t3 = CharTuple.from(new char[]{'A', 'B', 'C'});
     * int arity = t3.arity();              // 3
     * char min = t3.min();                 // 'A'
     *
     * // Basic: 1-element array creates a CharTuple1
     * CharTuple.CharTuple1 t1 = CharTuple.from(new char[]{'X'});
     * char v = t1._1;                      // 'X'
     *
     * // Edge: null returns empty tuple (arity 0)
     * CharTuple<?> fromNull = CharTuple.from(null);
     * int n1 = fromNull.arity();           // 0
     *
     * // Edge: empty array returns empty tuple
     * CharTuple<?> fromEmpty = CharTuple.from(new char[0]);
     * int n2 = fromEmpty.arity();          // 0
     *
     * // Edge: array length 10 throws IllegalArgumentException
     * CharTuple.from(new char[10]);      // throws IllegalArgumentException
     * }</pre>
     *
     * <p><b>&#9888;&#65039; Warning:</b> The runtime tuple implementation is chosen solely by {@code values.length}.
     * The generic return type is only type-safe when assigned to the matching arity-specific subtype,
     * or to the base tuple type. Assigning to the wrong arity-specific subtype will result in a
     * {@link ClassCastException} at the assignment site.</p>
     *
     * @param <TP> the base tuple type or matching arity-specific subtype expected by the caller
     * @param values the array of char values; may be {@code null} or empty, in which case the shared empty tuple is returned
     * @return a {@code CharTuple} of the appropriate arity containing the array values, or the shared empty tuple if the array is {@code null} or empty
     * @throws IllegalArgumentException if {@code values} has more than 9 elements
     * @deprecated Use the {@code of(...)} factory methods instead; this method may be removed in a future release.
     * @see #of(char)
     */
    @Deprecated
    @SuppressWarnings({ "unchecked" })
    public static <TP extends CharTuple<TP>> TP from(final char[] values) {
        if (values == null || values.length == 0) {
            return (TP) CharTuple0.EMPTY;
        }

        switch (values.length) {
            case 1:
                return (TP) CharTuple.of(values[0]);

            case 2:
                return (TP) CharTuple.of(values[0], values[1]);

            case 3:
                return (TP) CharTuple.of(values[0], values[1], values[2]);

            case 4:
                return (TP) CharTuple.of(values[0], values[1], values[2], values[3]);

            case 5:
                return (TP) CharTuple.of(values[0], values[1], values[2], values[3], values[4]);

            case 6:
                return (TP) CharTuple.of(values[0], values[1], values[2], values[3], values[4], values[5]);

            case 7:
                return (TP) CharTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);

            case 8:
                return (TP) CharTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);

            case 9:
                return (TP) CharTuple.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);

            default:
                throw new IllegalArgumentException("Too many elements (" + values.length + "). Maximum: 9");
        }
    }

    /**
     * Returns the minimum char value in this tuple, compared as unsigned 16-bit code units.
     * <p>
     * This method finds and returns the smallest char value among all elements
     * in the tuple. For tuples with a single element, that element is returned.
     * Note that {@code 'Z'} (90) is therefore considered less than {@code 'a'} (97).
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple3 t = CharTuple.of('Z', 'A', 'M');
     * char min = t.min();                  // 'A'
     *
     * // Basic: uppercase precedes lowercase ('Z'=90 < 'a'=97)
     * CharTuple.CharTuple2 t2 = CharTuple.of('Z', 'a');
     * char min2 = t2.min();               // 'Z'
     *
     * // Edge: single-element tuple - min equals the element itself
     * CharTuple.CharTuple1 t3 = CharTuple.of('\0');
     * char min3 = t3.min();               // '\0'
     *
     * // Edge: empty tuple throws NoSuchElementException
     * CharTuple<?> empty = CharTuple.from(new char[0]);
     * empty.min();                         // throws NoSuchElementException
     * }</pre>
     *
     * @return the minimum char value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #max()
     * @see #lowerMedian()
     */
    public char min() {
        final char[] a = elements();

        if (a.length == 0) {
            throw new NoSuchElementException("Cannot compute min() for an empty tuple");
        }

        return N.min(a);
    }

    /**
     * Returns the maximum char value in this tuple, compared as unsigned 16-bit code units.
     * <p>
     * This method finds and returns the largest char value among all elements
     * in the tuple. For tuples with a single element, that element is returned.
     * Note that {@code 'a'} (97) is therefore considered greater than {@code 'Z'} (90).
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple3 t = CharTuple.of('Z', 'A', 'M');
     * char max = t.max();                  // 'Z'
     *
     * // Basic: lowercase chars have higher code units than uppercase
     * CharTuple.CharTuple2 t2 = CharTuple.of('Z', 'a');
     * char max2 = t2.max();               // 'a' (97 > 90)
     *
     * // Edge: single-element tuple - max equals the element itself
     * CharTuple.CharTuple1 t3 = CharTuple.of('z');
     * char max3 = t3.max();               // 'z'
     *
     * // Edge: empty tuple throws NoSuchElementException
     * CharTuple<?> empty = CharTuple.from(new char[0]);
     * empty.max();                         // throws NoSuchElementException
     * }</pre>
     *
     * @return the maximum char value in this tuple
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see #lowerMedian()
     */
    public char max() {
        final char[] a = elements();

        if (a.length == 0) {
            throw new NoSuchElementException("Cannot compute max() for an empty tuple");
        }

        return N.max(a);
    }

    /**
     * Returns the sum of all char values in this tuple as an {@code int}.
     * <p>
     * Elements are summed as unsigned 16-bit code-unit values. For an empty tuple, the sum is
     * {@code 0}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: 'A'=65, 'B'=66, 'C'=67 -> sum = 198
     * CharTuple.CharTuple3 t = CharTuple.of('A', 'B', 'C');
     * int sum = t.sum();                    // 198
     *
     * // Basic: 'a'=97, 'b'=98 -> sum = 195
     * CharTuple.CharTuple2 t2 = CharTuple.of('a', 'b');
     * int sum2 = t2.sum();                  // 195
     *
     * // Edge: empty tuple - sum returns 0 (no exception)
     * CharTuple<?> empty = CharTuple.from(new char[0]);
     * int emptySum = empty.sum();           // 0
     *
     * // Edge: single NUL / zero code unit '\0'
     * CharTuple.CharTuple1 t3 = CharTuple.of('\0');
     * int nullCharSum = t3.sum();           // 0
     * }</pre>
     *
     * @return the sum of all unsigned code-unit values as an {@code int}; {@code 0} for an empty tuple
     * @see #average()
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     * Returns the arithmetic mean of all char values in this tuple as an {@code OptionalDouble}.
     * <p>
     * The average is computed from the unsigned 16-bit code-unit values of the elements.
     * For an empty tuple, returns an empty {@code OptionalDouble}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: 'A'=65, 'B'=66, 'C'=67 -> average = 66.0
     * CharTuple.CharTuple3 t = CharTuple.of('A', 'B', 'C');
     * t.average();                                      // returns OptionalDouble.of(66.0)
     *
     * // Basic: 'a'=97, 'c'=99 -> average = 98.0
     * CharTuple.CharTuple2 t2 = CharTuple.of('a', 'c');
     * t2.average();                                     // returns OptionalDouble.of(98.0)
     *
     * // Edge: even element count yields fractional result
     * CharTuple.CharTuple2 t3 = CharTuple.of('A', 'D'); // 65 + 68 = 133
     * t3.average();                                     // returns OptionalDouble.of(66.5)
     *
     * // Edge: empty tuple returns an empty OptionalDouble
     * CharTuple<?> empty = CharTuple.from(new char[0]);
     * empty.average();                                  // returns OptionalDouble.empty()
     * }</pre>
     *
     * @return the average of all unsigned code-unit values as an {@code OptionalDouble}, or an empty {@code OptionalDouble} if this tuple is empty
     * @see #sum()
     */
    public OptionalDouble average() {
        final char[] a = elements();

        return a.length == 0 ? OptionalDouble.empty() : OptionalDouble.of(N.average(a));
    }

    /**
     * Returns the lower median of this tuple as an unsigned UTF-16 code unit ({@code char}).
     * <p>
     * Elements are ordered by unsigned code-unit value (range {@code 0..65535}). For an odd
     * arity, this is the middle value when sorted. For an even arity, this is the lower of the two
     * middle values when sorted (not their average).
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // odd number of elements: exact middle of sorted sequence
     * CharTuple.CharTuple3 t3 = CharTuple.of('Z', 'A', 'M');
     * char median = t3.lowerMedian();   // 'M' (sorted: A, M, Z; index 1)
     *
     * // even number of elements: lower middle of sorted sequence
     * CharTuple.CharTuple4 t4 = CharTuple.of('A', 'B', 'C', 'D');
     * char evenMedian = t4.lowerMedian();   // 'B' (sorted: A, [B], C, D; lower of the two middles)
     *
     * // single element
     * CharTuple.CharTuple1 single = CharTuple.of('K');
     * char singleMedian = single.lowerMedian();   // 'K'
     *
     * // empty tuple -> throws NoSuchElementException
     * CharTuple<?> empty = CharTuple.from(new char[0]);
     * empty.lowerMedian();   // throws NoSuchElementException
     * }</pre>
     *
     * @return the lower-median char (middle when sorted for odd arity; lower-middle when sorted for even arity; unsigned code-unit order)
     * @throws NoSuchElementException if the tuple is empty
     * @see #min()
     * @see #max()
     * @see N#lowerMedian(char...)
     */
    public char lowerMedian() {
        final char[] a = elements();

        if (a.length == 0) {
            throw new NoSuchElementException("Cannot compute lowerMedian() for an empty tuple");
        }

        return N.lowerMedian(a);
    }

    /**
     * Returns a tuple with the elements in reverse order.
     * <p>
     * Non-empty built-in tuples return a new tuple instance of the same arity-specific
     * subtype with all elements in reversed order; the empty tuple returns itself. The
     * original tuple remains unchanged as tuples are immutable.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: three-element reversal
     * CharTuple.CharTuple3 t = CharTuple.of('A', 'B', 'C');
     * CharTuple.CharTuple3 rev = t.reversed();  // ('C', 'B', 'A')
     * char first = rev._1;                     // 'C'
     *
     * // Basic: two-element reversal
     * CharTuple.CharTuple2 t2 = CharTuple.of('X', 'Y');
     * CharTuple.CharTuple2 rev2 = t2.reversed();
     * char r2 = rev2._1;                   // 'Y'
     *
     * // Edge: single-element - reverse returns a new equal instance
     * CharTuple.CharTuple1 t3 = CharTuple.of('Z');
     * CharTuple.CharTuple1 rev3 = t3.reversed();
     * char r3 = rev3._1;                   // 'Z'
     *
     * // Edge: empty tuple returns itself
     * CharTuple<?> empty = CharTuple.from(new char[0]);
     * int emptyArity = empty.reversed().arity(); // 0
     *
     * // Edge: original tuple is not modified by reverse
     * char origFirst = t._1;               // still 'A'
     * }</pre>
     *
     * @return a tuple of the same arity with the elements in reverse order
     */
    public abstract TP reversed();

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
     * // Basic: element present
     * CharTuple.CharTuple3 t = CharTuple.of('A', 'B', 'C');
     * boolean hasB = t.contains('B');      // true
     *
     * // Basic: element absent
     * boolean hasZ = t.contains('Z');      // false
     *
     * // Edge: empty tuple never contains any value
     * CharTuple<?> empty = CharTuple.from(new char[0]);
     * boolean inEmpty = empty.contains('A'); // false
     *
     * // Edge: searching for first and last elements
     * CharTuple.CharTuple2 t2 = CharTuple.of('X', 'Y');
     * boolean hasX = t2.contains('X');     // true
     * boolean hasY = t2.contains('Y');     // true
     * }</pre>
     *
     * @param value the char value to search for
     * @return {@code true} if the value is found in this tuple, {@code false} otherwise
     */
    public abstract boolean contains(char value);

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
     * // Basic: returns array matching the tuple's elements in order
     * CharTuple.CharTuple3 t = CharTuple.of('A', 'B', 'C');
     * char[] a = t.toArray();            // ['A', 'B', 'C']
     * int len = a.length;                // 3
     *
     * // Basic: returned array is a defensive copy - mutation does not affect tuple
     * a[0] = 'X';
     * char first = t.toArray()[0];         // still 'A'
     *
     * // Edge: empty tuple returns empty array
     * CharTuple<?> empty = CharTuple.from(new char[0]);
     * char[] emptyArr = empty.toArray();
     * int emptyLen = emptyArr.length;      // 0
     *
     * // Edge: single-element tuple
     * CharTuple.CharTuple1 t1 = CharTuple.of('Z');
     * char[] single = t1.toArray();        // ['Z']
     * }</pre>
     *
     * @return a new char array containing all tuple elements (length equals arity)
     * @see #toList()
     * @see #stream()
     */
    public char[] toArray() {
        return elements().clone();
    }

    /**
     * Returns a new CharList containing all elements of this tuple.
     * <p>
     * Converts this tuple to a mutable {@link CharList} containing all elements
     * in their original order. The returned list is a new instance and modifications
     * to it do not affect the original tuple. The list size equals the tuple's arity.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: converts tuple to a mutable list
     * CharTuple.CharTuple3 t = CharTuple.of('A', 'B', 'C');
     * CharList list = t.toList();
     * int size = list.size();              // 3
     * char c = list.get(0);                // 'A'
     *
     * // Basic: modifications to the list do not affect the original tuple
     * list.add('D');                       // modifies the copy, not the tuple
     * int tupleArity = t.arity();          // still 3
     *
     * // Edge: empty tuple returns empty list
     * CharTuple<?> empty = CharTuple.from(new char[0]);
     * CharList emptyList = empty.toList();
     * int emptySize = emptyList.size();    // 0
     *
     * // Edge: single-element tuple
     * CharTuple.CharTuple1 t1 = CharTuple.of('Z');
     * CharList single = t1.toList();
     * int singleSize = single.size();      // 1
     * }</pre>
     *
     * @return a new CharList containing all tuple elements (size equals arity)
     * @see #toArray()
     * @see #stream()
     */
    public CharList toList() {
        return CharList.of(elements().clone());
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
     * // Basic: iterate and collect each element
     * CharTuple.CharTuple3 t = CharTuple.of('A', 'B', 'C');
     * StringBuilder sb = new StringBuilder();
     * t.forEach(sb::append);               // sb becomes "ABC"
     *
     * // Basic: use a counter to count elements visited
     * int[] count = {0};
     * t.forEach(ch -> count[0]++);         // increments count for each element
     * int visited = count[0];              // 3
     *
     * // Edge: empty tuple - action is never invoked
     * CharTuple<?> empty = CharTuple.from(new char[0]);
     * int[] cnt = {0};
     * empty.forEach(ch -> cnt[0]++);       // action not invoked (empty)
     * int notCalled = cnt[0];              // 0
     *
     * // Edge: null action throws IllegalArgumentException
     * // t.forEach(null);                     // throws IllegalArgumentException
     * }</pre>
     *
     * @param <E> the type of exception that may be thrown by the action
     * @param action the action to be performed for each element, must not be {@code null}
     * @throws IllegalArgumentException if {@code action} is {@code null}
     * @throws E if the action throws an exception during execution
     * @see #stream()
     */
    public <E extends Exception> void forEach(final Throwables.CharConsumer<E> action) throws E {
        N.checkArgNotNull(action, cs.action);

        for (final char element : elements()) {
            action.accept(element);
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
     * // Basic: stream sum equals sum()
     * CharTuple.CharTuple3 t = CharTuple.of('A', 'B', 'C');
     * int streamSum = t.stream().sum();    // 198 (65 + 66 + 67)
     *
     * // Basic: filter elements by predicate
     * CharTuple.CharTuple2 t2 = CharTuple.of('a', 'b');
     * long count = t2.stream().filter(c -> c > 'a').count();  // 1 (only 'b')
     *
     * // Edge: empty tuple produces an empty stream
     * CharTuple<?> empty = CharTuple.from(new char[0]);
     * long emptyCount = empty.stream().count();  // 0
     *
     * // Edge: stream count equals arity
     * CharTuple.CharTuple1 t1 = CharTuple.of('Z');
     * long cnt = t1.stream().count();      // 1
     * }</pre>
     *
     * @return a CharStream containing all tuple elements
     * @see #toArray()
     * @see #toList()
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
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: equal tuples have identical hash codes
     * CharTuple.CharTuple2 t1 = CharTuple.of('A', 'B');
     * CharTuple.CharTuple2 t2 = CharTuple.of('A', 'B');
     * boolean sameHash = (t1.hashCode() == t2.hashCode()); // true
     *
     * // Basic: different element order produces different hash code
     * CharTuple.CharTuple2 t3 = CharTuple.of('B', 'A');
     * boolean diffHash = (t1.hashCode() == t3.hashCode()); // typically false
     *
     * // Edge: empty tuple has a consistent hash code
     * CharTuple<?> empty1 = CharTuple.from(new char[0]);
     * CharTuple<?> empty2 = CharTuple.from(new char[0]);
     * boolean emptyHash = (empty1.hashCode() == empty2.hashCode()); // true
     *
     * // Edge: single-element tuple
     * CharTuple.CharTuple1 s1 = CharTuple.of('X');
     * CharTuple.CharTuple1 s2 = CharTuple.of('X');
     * boolean singleHash = (s1.hashCode() == s2.hashCode()); // true
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
     * <li>They are the same object (reference equality), or</li>
     * <li>They are of the exact same runtime class (e.g., both CharTuple.CharTuple2), and</li>
     * <li>They contain the same char elements in the same order</li>
     * </ul>
     * <p>
     * Tuples of different arities (e.g., {@code CharTuple2} and {@code CharTuple3}) are never equal,
     * even if their elements would otherwise match. This method adheres to the general contract
     * of {@link Object#equals(Object)}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Basic: same arity and same elements - equal
     * CharTuple.CharTuple2 t1 = CharTuple.of('A', 'B');
     * CharTuple.CharTuple2 t2 = CharTuple.of('A', 'B');
     * boolean eq = t1.equals(t2);          // true
     *
     * // Basic: same arity but different elements - not equal
     * CharTuple.CharTuple2 t3 = CharTuple.of('B', 'A');
     * boolean neq = t1.equals(t3);         // false
     *
     * // Edge: different arities are never equal
     * CharTuple.CharTuple1 t4 = CharTuple.of('A');
     * boolean diffArity = t1.equals(t4);   // false
     *
     * // Edge: comparing with null returns false
     * boolean nullEq = t1.equals(null);    // false
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
            return N.equals(elements(), ((CharTuple<?>) obj).elements());
        }
    }

    /**
     * Returns the internal array containing all char elements in this tuple.
     * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
     * Modifying the returned array will compromise the immutability of this tuple.
     * Use {@link #toArray()} instead if you need an array that can be safely modified.
     * </p>
     *
     * @return the internal array of char elements
     */
    protected abstract char[] elements();

    /**
     * An empty {@code CharTuple} (arity 0).
     * <p>
     * Package-private; callers obtain the shared instance only via {@link #from(char[])} with a
     * {@code null} or zero-length array (deprecated). Aggregate contracts for the empty instance:
     * {@link #sum()} is {@code 0}, {@link #average()} is empty, and
     * {@link #min()}, {@link #max()}, and {@link #lowerMedian()} throw
     * {@link NoSuchElementException}. {@link #reversed()} returns this same instance.
     * </p>
     */
    static final class CharTuple0 extends CharTuple<CharTuple0> {

        /** The shared empty char tuple. */
        private static final CharTuple0 EMPTY = new CharTuple0();

        /**
         * Package-private constructor for internal use; creates an empty tuple.
         * The shared empty tuple is normally obtained via {@link CharTuple#from(char[])}.
         */
        CharTuple0() {
        }

        /**
         * Returns the number of elements in this tuple, which is always 0.
         *
         * @return {@code 0}
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
            throw new NoSuchElementException("Cannot compute min() for an empty tuple");
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
            throw new NoSuchElementException("Cannot compute max() for an empty tuple");
        }

        /**
         * Returns the lower median char value in this tuple.
         * Since this tuple is empty, this method always throws an exception.
         *
         * @return never returns normally
         * @throws NoSuchElementException always, because the tuple is empty
         */
        @Override
        public char lowerMedian() {
            throw new NoSuchElementException("Cannot compute lowerMedian() for an empty tuple");
        }

        /**
         * Returns the sum of all char values in this tuple.
         * For an empty tuple, the sum is {@code 0}.
         *
         * @return {@code 0}
         */
        @Override
        public int sum() {
            return 0;
        }

        /**
         * Returns the average of all char values in this tuple.
         * Since this tuple is empty, this method always returns an empty {@code OptionalDouble}.
         *
         * @return an empty {@code OptionalDouble}
         */
        @Override
        public OptionalDouble average() {
            return OptionalDouble.empty();
        }

        /**
         * Returns this empty tuple instance.
         * Since this tuple has no elements, reversing has no effect.
         *
         * @return this {@code CharTuple0} instance
         */
        @Override
        public CharTuple0 reversed() {
            return this;
        }

        /**
         * Checks if this tuple contains the specified char value.
         * Since this tuple is empty, this method always returns {@code false}.
         *
         * @param value the char value to search for
         * @return {@code false} always, because the tuple is empty
         */
        @Override
        public boolean contains(final char value) {
            return false;
        }

        /**
         * Returns a string representation of this empty tuple.
         *
         * @return {@code "()"}
         */
        @Override
        public String toString() {
            return "()";
        }

        /**
         * Returns the shared empty char array.
         *
         * @return an empty char array
         */
        @Override
        protected char[] elements() {
            return N.EMPTY_CHAR_ARRAY;
        }
    }

    /**
     * A {@code CharTuple} containing exactly one {@code char} element.
     * <p>
     * The value is the public final field {@code _1}. Aggregates such as {@link #min()},
     * {@link #max()}, {@link #lowerMedian()}, {@link #sum()}, and {@link #average()} all reflect
     * that single element.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple1 tuple = CharTuple.of((char) 42);
     * char value = tuple._1;   // 42
     * char min = tuple.min();  // 42 (single element)
     * char max = tuple.max();  // 42 (single element)
     * }</pre>
     */
    public static final class CharTuple1 extends CharTuple<CharTuple1> {

        /** The single char value stored in this tuple. */
        public final char _1;

        /**
         * Package-private no-argument constructor; initializes the element to {@code (char) 0}.
         * Tuples are normally created via the {@code CharTuple.of(...)} factory methods.
         */
        CharTuple1() {
            this((char) 0);
        }

        /**
         * Package-private constructor that creates a tuple with the specified element.
         *
         * @param _1 the char value to store in this tuple
         */
        CharTuple1(final char _1) {
            this._1 = _1;
        }

        /**
         * Returns the number of elements in this tuple, which is always 1.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // Basic: arity is constant regardless of element value
         * CharTuple.CharTuple1 t1 = CharTuple.of('A');
         * int a1 = t1.arity();                 // returns 1
         *
         * CharTuple.CharTuple1 t2 = CharTuple.of('z');
         * int a2 = t2.arity();                 // returns 1
         *
         * // Edge: arity equals toArray().length
         * assert t1.arity() == t1.toArray().length; // true
         *
         * // Edge: NUL / zero code unit '\0' - arity still 1
         * CharTuple.CharTuple1 t3 = CharTuple.of('\0');
         * int a3 = t3.arity();                 // returns 1
         * }</pre>
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
         * CharTuple.CharTuple1 t = CharTuple.of('A');
         * char min = t.min();              // 'A'
         *
         * // Edge: NUL / zero code unit '\0' is valid and returned as-is
         * CharTuple.CharTuple1 t2 = CharTuple.of('\0');
         * char min2 = t2.min();            // '\0'
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
         * CharTuple.CharTuple1 t = CharTuple.of('Z');
         * char max = t.max();              // 'Z'
         *
         * // Edge: high code-unit char is returned unchanged
         * CharTuple.CharTuple1 t2 = CharTuple.of('\uFFFF');
         * char max2 = t2.max();            // '\uFFFF' (65535)
         * }</pre>
         *
         * @return the single char value in this tuple
         */
        @Override
        public char max() {
            return _1;
        }

        /**
         * Returns the sum of all char values in this tuple.
         * Since this tuple contains only one element, it returns the numeric value of that element.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple1 tupleA = CharTuple.of('A');   // 'A' = 65
         * int sumA = tupleA.sum();                           // 65
         *
         * CharTuple.CharTuple1 tupleZ = CharTuple.of('Z');   // 'Z' = 90
         * int sumZ = tupleZ.sum();                           // 90
         *
         * // Lower-case char
         * CharTuple.CharTuple1 tupleLower = CharTuple.of('a');   // 'a' = 97
         * int sumLower = tupleLower.sum();                       // 97
         *
         * // Boundary: max char (code unit 65535)
         * CharTuple.CharTuple1 tupleMax = CharTuple.of('\uFFFF');   // '\uFFFF' = 65535
         * int sumMax = tupleMax.sum();                              // 65535
         * }</pre>
         *
         * @return the unsigned code-unit value of the single char as an {@code int}
         */
        @Override
        public int sum() {
            return _1;
        }

        /**
         * Returns the average of all char values in this tuple.
         * Since this tuple contains only one element, it returns the numeric value of that element as an {@code OptionalDouble}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple1 tupleA = CharTuple.of('A');   // 'A' = 65
         * tupleA.average();                                  // returns OptionalDouble.of(65.0)
         *
         * CharTuple.CharTuple1 tupleZ = CharTuple.of('Z');   // 'Z' = 90
         * tupleZ.average();                                  // returns OptionalDouble.of(90.0)
         *
         * // Lower-case char
         * CharTuple.CharTuple1 tupleLower = CharTuple.of('a');  // 'a' = 97
         * tupleLower.average();                                 // returns OptionalDouble.of(97.0)
         *
         * // Boundary: max char (code unit 65535)
         * CharTuple.CharTuple1 tupleMax = CharTuple.of('\uFFFF');   // '\uFFFF' = 65535
         * tupleMax.average();                                   // returns OptionalDouble.of(65535.0)
         * }</pre>
         *
         * @return the unsigned code-unit value of the single char as an {@code OptionalDouble}
         */
        @Override
        public OptionalDouble average() {
            return OptionalDouble.of(_1);
        }

        /**
         * Returns the lower median of this one-element tuple, which is the element itself.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple1 t = CharTuple.of('M');
         * char median = t.lowerMedian();        // 'M'
         *
         * // Edge: any single char is its own lower median
         * CharTuple.CharTuple1 t2 = CharTuple.of('a');
         * char median2 = t2.lowerMedian();      // 'a'
         * }</pre>
         *
         * @return the single char value in this tuple
         */
        @Override
        public char lowerMedian() {
            return _1;
        }

        /**
         * Returns a new CharTuple.CharTuple1 with the same element.
         * Since this tuple has only one element, reversing has no effect on the value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple1 tuple = CharTuple.of('A');
         * CharTuple.CharTuple1 reversed = tuple.reversed();
         * char val = reversed._1;   // 'A'
         *
         * // reversed() returns a NEW instance
         * CharTuple.CharTuple1 original = CharTuple.of('Z');
         * CharTuple.CharTuple1 rev = original.reversed();
         * boolean sameInstance = (original == rev);   // false
         * boolean equalValue = original.equals(rev);  // true
         *
         * // Boundary: max char is preserved
         * CharTuple.CharTuple1 tupleMax = CharTuple.of('\uFFFF');
         * char valMax = tupleMax.reversed()._1;     // '\uFFFF'
         *
         * // Lower-case char
         * CharTuple.CharTuple1 tupleLower = CharTuple.of('a');
         * char valLower = tupleLower.reversed()._1; // 'a'
         * }</pre>
         *
         * @return a new CharTuple.CharTuple1 with the same element
         */
        @Override
        public CharTuple1 reversed() {
            return new CharTuple1(_1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple1 tuple = CharTuple.of('A');
         * boolean hasA = tuple.contains('A');   // true
         * boolean hasB = tuple.contains('B');   // false
         *
         * // Case-sensitive: 'a' != 'A'
         * boolean hasLower = tuple.contains('a');   // false
         *
         * // Boundary: max char in its own tuple
         * CharTuple.CharTuple1 tupleMax = CharTuple.of('\uFFFF');
         * boolean hasMax = tupleMax.contains('\uFFFF');   // true
         * boolean hasMiss = tupleMax.contains('A');       // false
         * }</pre>
         *
         * @param value the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char value) {
            return _1 == value;
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple1 tuple = CharTuple.of('A');   // 'A' = 65
         * int hash = tuple.hashCode();                      // 65
         *
         * // Equal tuples have equal hash codes
         * CharTuple.CharTuple1 t1 = CharTuple.of('Z');
         * CharTuple.CharTuple1 t2 = CharTuple.of('Z');
         * boolean sameHash = (t1.hashCode() == t2.hashCode()); // true
         *
         * // Different elements - different hash codes
         * CharTuple.CharTuple1 t3 = CharTuple.of('A');
         * CharTuple.CharTuple1 t4 = CharTuple.of('B');         // 'B' = 66
         * boolean diffHash = (t3.hashCode() != t4.hashCode()); // true
         * }</pre>
         *
         * @return the {@code int} value of the single char element
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
         * CharTuple.CharTuple1 t1 = CharTuple.of('A');
         * CharTuple.CharTuple1 t2 = CharTuple.of('A');
         * boolean eq = t1.equals(t2);   // true
         *
         * CharTuple.CharTuple1 t3 = CharTuple.of('B');
         * boolean neq = t1.equals(t3);   // false
         *
         * // Different arity is never equal
         * CharTuple.CharTuple2 t2arity = CharTuple.of('A', 'A');
         * boolean diffArity = t1.equals(t2arity);   // false
         *
         * // null is never equal
         * boolean nullEq = t1.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a CharTuple.CharTuple1 with the same element, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple1 tuple = CharTuple.of('A');
         * String s = tuple.toString();   // "(A)"
         *
         * CharTuple.CharTuple1 tupleZ = CharTuple.of('Z');
         * String sZ = tupleZ.toString();   // "(Z)"
         *
         * // Lowercase char
         * CharTuple.CharTuple1 tupleA = CharTuple.of('a');
         * String sA = tupleA.toString();   // "(a)"
         *
         * // Digit char
         * CharTuple.CharTuple1 tupleDigit = CharTuple.of('0');
         * String sDigit = tupleDigit.toString();   // "(0)"
         * }</pre>
         *
         * @return a string representation in the format "(element)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ")";
        }

        /**
         * Returns the internal array of char elements.
         * The array is lazily initialized on first access.
         * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
         * Modifying the returned array will compromise the immutability of this tuple.
         * Use {@link #toArray()} instead if you need an array that can be safely modified.
         * </p>
         *
         * @return a char array containing the single element
         */
        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1 };
            }

            return elements;
        }
    }

    /**
     * A {@code CharTuple} containing exactly two {@code char} elements.
     * <p>
     * Components are the public final fields {@code _1} and {@code _2}. Besides the operations from
     * {@link CharTuple} and {@link PrimitiveTuple}, this type adds element-unpacking helpers that
     * pass both values to a bi-consumer / bi-function / bi-predicate (distinct from the whole-tuple
     * {@code accept}/{@code map}/{@code filter} on {@link PrimitiveTuple}):
     * </p>
     * <ul>
     *   <li>{@link #accept(Throwables.CharBiConsumer)} — both elements as primitives</li>
     *   <li>{@link #map(Throwables.CharBiFunction)} — map both elements to one result</li>
     *   <li>{@link #filter(Throwables.CharBiPredicate)} — keep this pair in an {@link Optional} if the predicate holds</li>
     * </ul>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple2 tuple = CharTuple.of((char) 10, (char) 20);
     * char first = tuple._1;   // 10
     * char second = tuple._2;  // 20
     *
     * // Element-wise functional helpers
     * tuple.accept((a, b) -> System.out.println(a + " + " + b));
     * int sum = tuple.map((a, b) -> a + b);   // 30
     * }</pre>
     */
    public static final class CharTuple2 extends CharTuple<CharTuple2> {

        /** The first char value stored in this tuple. */
        public final char _1;
        /** The second char value stored in this tuple. */
        public final char _2;

        /**
         * Package-private no-argument constructor; initializes all elements to {@code (char) 0}.
         * Tuples are normally created via the {@code CharTuple.of(...)} factory methods.
         */
        CharTuple2() {
            this((char) 0, (char) 0);
        }

        /**
         * Package-private constructor that creates a tuple with the specified elements.
         *
         * @param _1 the first char value
         * @param _2 the second char value
         */
        CharTuple2(final char _1, final char _2) {
            this._1 = _1;
            this._2 = _2;
        }

        /**
         * Returns the number of elements in this tuple, which is always 2.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // Basic: arity is constant regardless of element values
         * CharTuple.CharTuple2 t1 = CharTuple.of('A', 'B');
         * int a1 = t1.arity();                 // returns 2
         *
         * CharTuple.CharTuple2 t2 = CharTuple.of('Z', 'a');
         * int a2 = t2.arity();                 // returns 2
         *
         * // Edge: both elements equal - arity still 2
         * CharTuple.CharTuple2 t3 = CharTuple.of('X', 'X');
         * int a3 = t3.arity();                 // returns 2
         *
         * // Edge: arity equals toArray().length
         * assert t1.arity() == t1.toArray().length; // true
         * }</pre>
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
         * CharTuple.CharTuple2 tuple = CharTuple.of('Z', 'A');
         * char min = tuple.min();   // 'A'
         *
         * // Upper vs lower: 'Z'(90) < 'a'(97)
         * CharTuple.CharTuple2 mixedCase = CharTuple.of('a', 'Z');
         * char minMixed = mixedCase.min();   // 'Z'
         *
         * // Duplicate elements
         * CharTuple.CharTuple2 same = CharTuple.of('B', 'B');
         * char minSame = same.min();   // 'B'
         *
         * // Boundary: max char vs 'A'
         * CharTuple.CharTuple2 boundary = CharTuple.of('\uFFFF', 'A');
         * char minBoundary = boundary.min();   // 'A'
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
         * CharTuple.CharTuple2 tuple = CharTuple.of('Z', 'A');
         * char max = tuple.max();   // 'Z'
         *
         * // Upper vs lower: 'a'(97) > 'Z'(90)
         * CharTuple.CharTuple2 mixedCase = CharTuple.of('a', 'Z');
         * char maxMixed = mixedCase.max();   // 'a'
         *
         * // Duplicate elements
         * CharTuple.CharTuple2 same = CharTuple.of('B', 'B');
         * char maxSame = same.max();   // 'B'
         *
         * // Boundary: max char is larger
         * CharTuple.CharTuple2 boundary = CharTuple.of('\uFFFF', 'A');
         * char maxBoundary = boundary.max();   // '\uFFFF'
         * }</pre>
         *
         * @return the larger of the two char values
         */
        @Override
        public char max() {
            return N.max(_1, _2);
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple2 tuple = CharTuple.of('A', 'B');   // 'A'=65, 'B'=66
         * int sum = tuple.sum();                                 // 131
         *
         * // lower-case a + b = 97 + 98 = 195
         * CharTuple.CharTuple2 lower = CharTuple.of('a', 'b');
         * int sumLower = lower.sum();                  // 195
         *
         * // Duplicates: 'A' + 'A' = 65 + 65 = 130
         * CharTuple.CharTuple2 dup = CharTuple.of('A', 'A');
         * int sumDup = dup.sum();                      // 130
         *
         * // Upper and lower: 'A' + 'a' = 65 + 97 = 162
         * CharTuple.CharTuple2 mixed = CharTuple.of('A', 'a');
         * int sumMixed = mixed.sum();                  // 162
         * }</pre>
         *
         * @return the sum of both unsigned code-unit values as an {@code int}
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
         * CharTuple.CharTuple2 tuple = CharTuple.of('A', 'C');   // 'A'=65, 'C'=67
         * tuple.average();                                     // returns OptionalDouble.of(66.0)
         *
         * // Non-integer average: ('A'=65 + 'B'=66) / 2 = 65.5
         * CharTuple.CharTuple2 ab = CharTuple.of('A', 'B');
         * ab.average();                              // returns OptionalDouble.of(65.5)
         *
         * // Duplicates: same value, same average
         * CharTuple.CharTuple2 dup = CharTuple.of('Z', 'Z');   // 'Z'=90
         * dup.average();                                     // returns OptionalDouble.of(90.0)
         *
         * // Upper and lower: (65 + 97) / 2 = 81.0
         * CharTuple.CharTuple2 mixed = CharTuple.of('A', 'a');
         * mixed.average();                           // returns OptionalDouble.of(81.0)
         * }</pre>
         *
         * @return the average of both unsigned code-unit values as an {@code OptionalDouble}
         */
        @Override
        public OptionalDouble average() {
            return OptionalDouble.of(N.average(_1, _2));
        }

        /**
         * Returns the lower median of this pair: the smaller of the two unsigned code-unit values.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple2 tuple = CharTuple.of('B', 'D');
         * char median = tuple.lowerMedian();   // 'B' (lower value)
         *
         * // Same value
         * CharTuple.CharTuple2 same = CharTuple.of('C', 'C');
         * char medianSame = same.lowerMedian();   // 'C'
         *
         * // Reversed order - still returns the lower
         * CharTuple.CharTuple2 rev = CharTuple.of('Z', 'A');
         * char medianRev = rev.lowerMedian();   // 'A'
         *
         * // Boundary: lower of 'A' and max char is 'A'
         * CharTuple.CharTuple2 boundary = CharTuple.of('\uFFFF', 'A');
         * char medianBoundary = boundary.lowerMedian();   // 'A'
         * }</pre>
         *
         * @return {@code min(_1, _2)} in unsigned code-unit order
         */
        @Override
        public char lowerMedian() {
            return N.min(_1, _2);
        }

        /**
         * Returns a new CharTuple.CharTuple2 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple2 tuple = CharTuple.of('A', 'B');
         * CharTuple.CharTuple2 reversed = tuple.reversed();
         * char r1 = reversed._1;   // 'B'
         * char r2 = reversed._2;   // 'A'
         *
         * // Duplicate elements - reverse equals original
         * CharTuple.CharTuple2 same = CharTuple.of('X', 'X');
         * CharTuple.CharTuple2 revSame = same.reversed();
         * boolean eq = same.equals(revSame);   // true
         *
         * // Boundary: max char and 'A' reversed
         * CharTuple.CharTuple2 boundary = CharTuple.of('A', '\uFFFF');
         * CharTuple.CharTuple2 revBoundary = boundary.reversed();
         * char rb1 = revBoundary._1;   // '\uFFFF'
         * char rb2 = revBoundary._2;   // 'A'
         * }</pre>
         *
         * @return a new CharTuple.CharTuple2 with the elements in reverse order
         */
        @Override
        public CharTuple2 reversed() {
            return new CharTuple2(_2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple2 tuple = CharTuple.of('A', 'B');
         * boolean hasA = tuple.contains('A');   // true
         * boolean hasB = tuple.contains('B');   // true
         * boolean hasC = tuple.contains('C');   // false
         *
         * // Case-sensitive: 'a' != 'A'
         * boolean hasLower = tuple.contains('a');   // false
         *
         * // Boundary: max char not in tuple
         * boolean hasMax = tuple.contains('\uFFFF');   // false
         * }</pre>
         *
         * @param value the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char value) {
            return _1 == value || _2 == value;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple2 tuple = CharTuple.of('A', 'B');
         * // Collect both chars into a list in order
         * java.util.List<Character> list = new java.util.ArrayList<>();
         * tuple.forEach(c -> list.add(c));  // list is ['A', 'B']
         *
         * // Sum char code units: 'A'=65, 'B'=66
         * int[] total = {0};
         * tuple.forEach(c -> total[0] += c);
         * int sum = total[0];   // 131
         *
         * // Duplicate chars: 'Z'=90, sum = 180
         * CharTuple.CharTuple2 dup = CharTuple.of('Z', 'Z');
         * int[] dupSum = {0};
         * dup.forEach(c -> dupSum[0] += c);
         * int dupTotal = dupSum[0];   // 180
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> action) throws E {
            N.checkArgNotNull(action, cs.action);

            action.accept(_1);
            action.accept(_2);
        }

        /**
         * Invokes {@code action} once with both elements ({@code _1}, {@code _2}).
         * <p>
         * This is the element-unpacking overload for pairs. It differs from
         * {@link PrimitiveTuple#accept(Throwables.Consumer)}, which receives this tuple object, and from
         * {@link #forEach(Throwables.CharConsumer)}, which invokes a consumer once per element.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple2 tuple = CharTuple.of('A', 'B');
         * // Concatenate both chars into a string
         * String[] result = {""};
         * tuple.accept((a, b) -> result[0] = "" + a + b);
         * String s = result[0];   // "AB"
         *
         * // Compare both elements: 'A' < 'B'
         * boolean[] ascending = {false};
         * tuple.accept((a, b) -> ascending[0] = a < b);
         * boolean isAsc = ascending[0];   // true
         *
         * // Duplicate elements - both equal
         * CharTuple.CharTuple2 dup = CharTuple.of('X', 'X');
         * boolean[] same = {false};
         * dup.accept((a, b) -> same[0] = (a == b));
         * boolean areSame = same[0];   // true
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the bi-consumer to apply to both elements, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #forEach(Throwables.CharConsumer)
         * @see #map(Throwables.CharBiFunction)
         * @see PrimitiveTuple#accept(Throwables.Consumer)
         */
        public <E extends Exception> void accept(final Throwables.CharBiConsumer<E> action) throws E {
            N.checkArgNotNull(action, cs.action);

            action.accept(_1, _2);
        }

        /**
         * Applies {@code mapper} to both elements ({@code _1}, {@code _2}) and returns the result.
         * <p>
         * Element-unpacking overload for pairs. Distinct from
         * {@link PrimitiveTuple#map(Throwables.Function)}, which receives this tuple object as a single argument.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple2 tuple = CharTuple.of('A', 'B');
         * String result = tuple.map((a, b) -> "" + a + b);   // "AB"
         *
         * // Code-unit difference: 'Z'(90) - 'A'(65) = 25
         * CharTuple.CharTuple2 zTuple = CharTuple.of('Z', 'A');
         * int diff = zTuple.map((a, b) -> (int) a - (int) b);   // 25
         *
         * // Sum of code units: 'A'=65, 'B'=66
         * int sumVal = tuple.map((a, b) -> (int) a + (int) b);   // 131
         *
         * // Duplicate chars: concatenation
         * CharTuple.CharTuple2 dup = CharTuple.of('X', 'X');
         * String dup2 = dup.map((a, b) -> "" + a + b);   // "XX"
         * }</pre>
         *
         * @param <U> the type of the result value
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the bi-function to apply to both elements, must not be {@code null}
         * @return the result of applying the bi-function to both elements (may be {@code null})
         * @throws IllegalArgumentException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.CharBiConsumer)
         * @see #filter(Throwables.CharBiPredicate)
         * @see PrimitiveTuple#map(Throwables.Function)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.CharBiFunction<U, E> mapper) throws E {
            N.checkArgNotNull(mapper, cs.mapper);

            return mapper.apply(_1, _2);
        }

        /**
         * Returns an {@link Optional} containing this pair if {@code predicate} holds for
         * ({@code _1}, {@code _2}); otherwise {@link Optional#empty()}.
         * <p>
         * Element-unpacking overload for pairs. Distinct from
         * {@link PrimitiveTuple#filter(Throwables.Predicate)}, which tests the tuple object as a whole.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple2 tuple = CharTuple.of('A', 'B');
         * Optional<CharTuple.CharTuple2> present = tuple.filter((a, b) -> a < b);
         * boolean hasTuple = present.isPresent();   // true
         *
         * Optional<CharTuple.CharTuple2> empty = tuple.filter((a, b) -> a > b);
         * boolean isEmpty = empty.isPresent();   // false
         *
         * // Duplicate elements - equality predicate passes
         * CharTuple.CharTuple2 dup = CharTuple.of('Z', 'Z');
         * Optional<CharTuple.CharTuple2> dupPresent = dup.filter((a, b) -> a == b);
         * boolean dupHas = dupPresent.isPresent();   // true
         *
         * // Predicate never matches when checking for '\uFFFF'
         * Optional<CharTuple.CharTuple2> neverMatch = tuple.filter((a, b) -> a == '\uFFFF');
         * boolean neverHas = neverMatch.isPresent();   // false
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the bi-predicate to test both elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws IllegalArgumentException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception during evaluation
         * @see #accept(Throwables.CharBiConsumer)
         * @see #map(Throwables.CharBiFunction)
         * @see PrimitiveTuple#filter(Throwables.Predicate)
         */
        public <E extends Exception> Optional<CharTuple2> filter(final Throwables.CharBiPredicate<E> predicate) throws E {
            N.checkArgNotNull(predicate, cs.predicate);

            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple2 t1 = CharTuple.of('A', 'B');   // 'A'=65, 'B'=66
         * int hash = t1.hashCode();                           // 31 * 65 + 66 = 2081
         *
         * // Equal tuples have equal hash codes
         * CharTuple.CharTuple2 t2 = CharTuple.of('A', 'B');
         * boolean sameHash = (t1.hashCode() == t2.hashCode()); // true
         *
         * // Different order - different hash
         * CharTuple.CharTuple2 t3 = CharTuple.of('B', 'A');
         * boolean diffHash = (t1.hashCode() != t3.hashCode()); // true
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
         * CharTuple.CharTuple2 t1 = CharTuple.of('A', 'B');
         * CharTuple.CharTuple2 t2 = CharTuple.of('A', 'B');
         * boolean eq = t1.equals(t2);   // true
         *
         * // Different second element
         * CharTuple.CharTuple2 t3 = CharTuple.of('A', 'C');
         * boolean neq = t1.equals(t3);   // false
         *
         * // Different order is not equal
         * CharTuple.CharTuple2 t4 = CharTuple.of('B', 'A');
         * boolean orderMatters = t1.equals(t4);   // false
         *
         * // null is never equal
         * boolean nullEq = t1.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a CharTuple.CharTuple2 with the same elements in the same order, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple2 tuple = CharTuple.of('A', 'B');
         * String s = tuple.toString();   // "(A, B)"
         *
         * // Duplicate chars
         * CharTuple.CharTuple2 dup = CharTuple.of('Z', 'Z');
         * String sDup = dup.toString();   // "(Z, Z)"
         *
         * // Lower-case vs upper-case
         * CharTuple.CharTuple2 mixed = CharTuple.of('a', 'A');
         * String sMixed = mixed.toString();   // "(a, A)"
         *
         * // Digit chars
         * CharTuple.CharTuple2 digits = CharTuple.of('1', '2');
         * String sDigits = digits.toString();   // "(1, 2)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ")";
        }

        /**
         * Returns the internal array of char elements.
         * The array is lazily initialized on first access.
         * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
         * Modifying the returned array will compromise the immutability of this tuple.
         * Use {@link #toArray()} instead if you need an array that can be safely modified.
         * </p>
         *
         * @return a char array containing all elements in order
         */
        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2 };
            }

            return elements;
        }
    }

    /**
     * A {@code CharTuple} containing exactly three {@code char} elements.
     * <p>
     * Components are the public final fields {@code _1}, {@code _2}, and {@code _3}. In addition to
     * {@link CharTuple} / {@link PrimitiveTuple} operations, this type provides element-unpacking
     * {@link #accept(Throwables.CharTriConsumer)}, {@link #map(Throwables.CharTriFunction)}, and
     * {@link #filter(Throwables.CharTriPredicate)} helpers that pass all three values as primitives
     * (distinct from the whole-tuple methods on {@link PrimitiveTuple}).
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple3 tuple = CharTuple.of((char) 10, (char) 20, (char) 30);
     * char first = tuple._1;   // 10
     * char second = tuple._2;  // 20
     * char third = tuple._3;   // 30
     *
     * // Using statistical operations
     * char min = tuple.min();         // 10
     * char max = tuple.max();         // 30
     * OptionalDouble avg = tuple.average();   // OptionalDouble.of(20.0)
     * }</pre>
     */
    public static final class CharTuple3 extends CharTuple<CharTuple3> {

        /** The first char value stored in this tuple. */
        public final char _1;
        /** The second char value stored in this tuple. */
        public final char _2;
        /** The third char value stored in this tuple. */
        public final char _3;

        /**
         * Package-private no-argument constructor; initializes all elements to {@code (char) 0}.
         * Tuples are normally created via the {@code CharTuple.of(...)} factory methods.
         */
        CharTuple3() {
            this((char) 0, (char) 0, (char) 0);
        }

        /**
         * Package-private constructor that creates a tuple with the specified elements.
         *
         * @param _1 the first char value
         * @param _2 the second char value
         * @param _3 the third char value
         */
        CharTuple3(final char _1, final char _2, final char _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        /**
         * Returns the number of elements in this tuple, which is always 3.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * // Basic: arity is constant regardless of element values
         * CharTuple.CharTuple3 t1 = CharTuple.of('A', 'B', 'C');
         * int a1 = t1.arity();                 // returns 3
         *
         * CharTuple.CharTuple3 t2 = CharTuple.of('x', 'y', 'z');
         * int a2 = t2.arity();                 // returns 3
         *
         * // Edge: all elements equal - arity still 3
         * CharTuple.CharTuple3 t3 = CharTuple.of('X', 'X', 'X');
         * int a3 = t3.arity();                 // returns 3
         *
         * // Edge: arity equals toArray().length
         * assert t1.arity() == t1.toArray().length; // true
         * }</pre>
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
         * CharTuple.CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
         * char min = tuple.min();   // 'A'
         *
         * // Duplicates: all same
         * CharTuple.CharTuple3 same = CharTuple.of('B', 'B', 'B');
         * char minSame = same.min();   // 'B'
         *
         * // Upper vs lower: 'Z'(90) < 'a'(97)
         * CharTuple.CharTuple3 mixed = CharTuple.of('a', 'Z', 'z');
         * char minMixed = mixed.min();   // 'Z'
         *
         * // Boundary: max char vs regular chars
         * CharTuple.CharTuple3 boundary = CharTuple.of('\uFFFF', 'A', 'Z');
         * char minBoundary = boundary.min();   // 'A'
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
         * CharTuple.CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
         * char max = tuple.max();   // 'Z'
         *
         * // Duplicates: all same
         * CharTuple.CharTuple3 same = CharTuple.of('B', 'B', 'B');
         * char maxSame = same.max();   // 'B'
         *
         * // Upper vs lower: 'z'(122) > 'a'(97) > 'Z'(90)
         * CharTuple.CharTuple3 mixed = CharTuple.of('a', 'Z', 'z');
         * char maxMixed = mixed.max();   // 'z'
         *
         * // Boundary: max char is largest
         * CharTuple.CharTuple3 boundary = CharTuple.of('\uFFFF', 'A', 'Z');
         * char maxBoundary = boundary.max();   // '\uFFFF'
         * }</pre>
         *
         * @return the largest of the three char values
         */
        @Override
        public char max() {
            return N.max(_1, _2, _3);
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple3 tuple = CharTuple.of('A', 'B', 'C');   // 65+66+67
         * int sum = tuple.sum();                                      // 198
         *
         * // Lower-case: 97+98+99
         * CharTuple.CharTuple3 lower = CharTuple.of('a', 'b', 'c');
         * int sumLower = lower.sum();                       // 294
         *
         * // All same chars: 90*3
         * CharTuple.CharTuple3 same = CharTuple.of('Z', 'Z', 'Z');
         * int sumSame = same.sum();                         // 270
         *
         * // Mixed case: 'A'=65, 'B'=66, 'a'=97
         * CharTuple.CharTuple3 mixed = CharTuple.of('A', 'B', 'a');
         * int sumMixed = mixed.sum();                       // 228
         * }</pre>
         *
         * @return the sum of all three unsigned code-unit values as an {@code int}
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
         * CharTuple.CharTuple3 tuple = CharTuple.of('A', 'B', 'C');   // 65+66+67=198, /3
         * tuple.average();                                          // returns OptionalDouble.of(66.0)
         *
         * // Non-integer average: 65+66+68=199, /3
         * CharTuple.CharTuple3 nonInt = CharTuple.of('A', 'B', 'D');
         * nonInt.average();                               // returns OptionalDouble.of(66.33333333333333)
         *
         * // All same chars: 90*3/3
         * CharTuple.CharTuple3 same = CharTuple.of('Z', 'Z', 'Z');
         * same.average();                                 // returns OptionalDouble.of(90.0)
         *
         * // Mixed case: (65+66+97)/3 = 228/3
         * CharTuple.CharTuple3 mixed = CharTuple.of('A', 'B', 'a');
         * mixed.average();                                // returns OptionalDouble.of(76.0)
         * }</pre>
         *
         * @return the average of all three unsigned code-unit values as an {@code OptionalDouble}
         */
        @Override
        public OptionalDouble average() {
            return OptionalDouble.of(N.average(_1, _2, _3));
        }

        /**
         * Returns the lower median of this triple: the middle unsigned code-unit value when the three
         * elements are ordered by code-unit value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple3 tuple = CharTuple.of('Z', 'A', 'M');
         * char median = tuple.lowerMedian();   // 'M' (middle value when sorted: A, M, Z)
         *
         * // Duplicates: two same values - lower median is the duplicate value
         * CharTuple.CharTuple3 dup = CharTuple.of('B', 'B', 'D');
         * char medianDup = dup.lowerMedian();   // 'B'
         *
         * // All same values
         * CharTuple.CharTuple3 same = CharTuple.of('C', 'C', 'C');
         * char medianSame = same.lowerMedian();   // 'C'
         *
         * // Boundary: 'A', 'Z', max char - lower median is 'Z'
         * CharTuple.CharTuple3 boundary = CharTuple.of('A', 'Z', '\uFFFF');
         * char medianBoundary = boundary.lowerMedian();   // 'Z'
         * }</pre>
         *
         * @return the middle char value when the three elements are sorted (unsigned code-unit order)
         */
        @Override
        public char lowerMedian() {
            return N.median(_1, _2, _3);
        }

        /**
         * Returns a new CharTuple.CharTuple3 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
         * CharTuple.CharTuple3 reversed = tuple.reversed();
         * char r1 = reversed._1;   // 'C'
         * char r2 = reversed._2;   // 'B'
         * char r3 = reversed._3;   // 'A'
         *
         * // Duplicate elements - reverse equals original
         * CharTuple.CharTuple3 same = CharTuple.of('X', 'X', 'X');
         * CharTuple.CharTuple3 revSame = same.reversed();
         * boolean eq = same.equals(revSame);   // true
         *
         * // Palindrome - also equals reversed
         * CharTuple.CharTuple3 palindrome = CharTuple.of('A', 'B', 'A');
         * CharTuple.CharTuple3 revPalin = palindrome.reversed();
         * boolean palinEq = palindrome.equals(revPalin);   // true
         * }</pre>
         *
         * @return a new CharTuple.CharTuple3 with the elements in reverse order
         */
        @Override
        public CharTuple3 reversed() {
            return new CharTuple3(_3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
         * boolean hasA = tuple.contains('A');   // true
         * boolean hasC = tuple.contains('C');   // true
         * boolean hasZ = tuple.contains('Z');   // false
         *
         * // Case-sensitive: 'a' != 'A'
         * boolean hasLower = tuple.contains('a');   // false
         *
         * // Boundary: max char not present
         * boolean hasMax = tuple.contains('\uFFFF');   // false
         * }</pre>
         *
         * @param value the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char value) {
            return _1 == value || _2 == value || _3 == value;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
         * // Collect chars into a list in order
         * java.util.List<Character> list = new java.util.ArrayList<>();
         * tuple.forEach(c -> list.add(c));  // list is ['A', 'B', 'C']
         *
         * // Sum char code units: 65+66+67
         * int[] total = {0};
         * tuple.forEach(c -> total[0] += c);
         * int sum = total[0];   // 198
         *
         * // Duplicate chars: 'Z'=90, 3 * 90 = 270
         * CharTuple.CharTuple3 dup = CharTuple.of('Z', 'Z', 'Z');
         * int[] dupSum = {0};
         * dup.forEach(c -> dupSum[0] += c);
         * int dupTotal = dupSum[0];   // 270
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> action) throws E {
            N.checkArgNotNull(action, cs.action);

            action.accept(_1);
            action.accept(_2);
            action.accept(_3);
        }

        /**
         * Invokes {@code action} once with all three elements ({@code _1}, {@code _2}, {@code _3}).
         * <p>
         * Element-unpacking overload for triples. Distinct from
         * {@link PrimitiveTuple#accept(Throwables.Consumer)} (whole tuple) and
         * {@link #forEach(Throwables.CharConsumer)} (one call per element).
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
         * // Concatenate all three chars
         * String[] result = {""};
         * tuple.accept((a, b, c) -> result[0] = "" + a + b + c);
         * String s = result[0];   // "ABC"
         *
         * // Check ascending order
         * boolean[] ascending = {false};
         * tuple.accept((a, b, c) -> ascending[0] = a < b && b < c);
         * boolean isAsc = ascending[0];   // true
         *
         * // Duplicate elements - all equal
         * CharTuple.CharTuple3 dup = CharTuple.of('X', 'X', 'X');
         * boolean[] allSame = {false};
         * dup.accept((a, b, c) -> allSame[0] = (a == b && b == c));
         * boolean areSame = allSame[0];   // true
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the tri-consumer to apply to all three elements, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         * @see #forEach(Throwables.CharConsumer)
         * @see #map(Throwables.CharTriFunction)
         * @see PrimitiveTuple#accept(Throwables.Consumer)
         */
        public <E extends Exception> void accept(final Throwables.CharTriConsumer<E> action) throws E {
            N.checkArgNotNull(action, cs.action);

            action.accept(_1, _2, _3);
        }

        /**
         * Applies {@code mapper} to all three elements ({@code _1}, {@code _2}, {@code _3}) and returns the result.
         * <p>
         * Element-unpacking overload for triples. Distinct from
         * {@link PrimitiveTuple#map(Throwables.Function)}, which receives this tuple object as a single argument.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
         * String result = tuple.map((a, b, c) -> "" + a + b + c);   // "ABC"
         *
         * // Sum of code units: char arithmetic widens to int
         * int sumVal = tuple.map((a, b, c) -> (int) a + b + c);   // 198
         *
         * // Duplicate chars - concatenated
         * CharTuple.CharTuple3 dup = CharTuple.of('Z', 'Z', 'Z');
         * String dupStr = dup.map((a, b, c) -> "" + a + b + c);   // "ZZZ"
         *
         * // Upper vs lower: concatenation preserves case
         * CharTuple.CharTuple3 mixed = CharTuple.of('A', 'B', 'a');
         * String mixedStr = mixed.map((a, b, c) -> "" + a + b + c);   // "ABa"
         * }</pre>
         *
         * @param <U> the type of the result value
         * @param <E> the type of exception that may be thrown by the mapper
         * @param mapper the tri-function to apply to all three elements, must not be {@code null}
         * @return the result of applying the tri-function to all three elements (may be {@code null})
         * @throws IllegalArgumentException if {@code mapper} is {@code null}
         * @throws E if the mapper throws an exception
         * @see #accept(Throwables.CharTriConsumer)
         * @see #filter(Throwables.CharTriPredicate)
         * @see PrimitiveTuple#map(Throwables.Function)
         */
        @MayReturnNull
        public <U, E extends Exception> U map(final Throwables.CharTriFunction<U, E> mapper) throws E {
            N.checkArgNotNull(mapper, cs.mapper);

            return mapper.apply(_1, _2, _3);
        }

        /**
         * Returns an {@link Optional} containing this triple if {@code predicate} holds for
         * ({@code _1}, {@code _2}, {@code _3}); otherwise {@link Optional#empty()}.
         * <p>
         * Element-unpacking overload for triples. Distinct from
         * {@link PrimitiveTuple#filter(Throwables.Predicate)}, which tests the tuple object as a whole.
         * </p>
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
         * Optional<CharTuple.CharTuple3> present = tuple.filter((a, b, c) -> a < b && b < c);
         * boolean hasTuple = present.isPresent();   // true
         *
         * Optional<CharTuple.CharTuple3> empty = tuple.filter((a, b, c) -> a > b);
         * boolean isEmpty = empty.isPresent();   // false
         *
         * // Duplicate elements - equality predicate passes
         * CharTuple.CharTuple3 dup = CharTuple.of('Z', 'Z', 'Z');
         * Optional<CharTuple.CharTuple3> dupPresent = dup.filter((a, b, c) -> a == b && b == c);
         * boolean dupHas = dupPresent.isPresent();   // true
         *
         * // Predicate never matches when looking for '\uFFFF'
         * Optional<CharTuple.CharTuple3> neverMatch = tuple.filter((a, b, c) -> a == '\uFFFF');
         * boolean neverHas = neverMatch.isPresent();   // false
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the predicate
         * @param predicate the tri-predicate to test all three elements, must not be {@code null}
         * @return an Optional containing this tuple if the predicate returns {@code true}, empty Optional otherwise
         * @throws IllegalArgumentException if {@code predicate} is {@code null}
         * @throws E if the predicate throws an exception during evaluation
         * @see #accept(Throwables.CharTriConsumer)
         * @see #map(Throwables.CharTriFunction)
         * @see PrimitiveTuple#filter(Throwables.Predicate)
         */
        public <E extends Exception> Optional<CharTuple3> filter(final Throwables.CharTriPredicate<E> predicate) throws E {
            N.checkArgNotNull(predicate, cs.predicate);

            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         * Returns a hash code value for this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple3 t1 = CharTuple.of('A', 'B', 'C');   // 'A'=65, 'B'=66, 'C'=67
         * int hash = t1.hashCode();                                // (31 * (31 * 65 + 66)) + 67 = 64578
         *
         * // Equal tuples have equal hash codes
         * CharTuple.CharTuple3 t2 = CharTuple.of('A', 'B', 'C');
         * boolean sameHash = (t1.hashCode() == t2.hashCode()); // true
         *
         * // Different order - different hash
         * CharTuple.CharTuple3 t3 = CharTuple.of('C', 'B', 'A');
         * boolean diffHash = (t1.hashCode() != t3.hashCode()); // true
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
         * CharTuple.CharTuple3 t1 = CharTuple.of('A', 'B', 'C');
         * CharTuple.CharTuple3 t2 = CharTuple.of('A', 'B', 'C');
         * boolean eq = t1.equals(t2);   // true
         *
         * // Different third element
         * CharTuple.CharTuple3 t3 = CharTuple.of('A', 'B', 'D');
         * boolean neq = t1.equals(t3);   // false
         *
         * // Different order is not equal
         * CharTuple.CharTuple3 t4 = CharTuple.of('C', 'B', 'A');
         * boolean orderMatters = t1.equals(t4);   // false
         *
         * // null is never equal
         * boolean nullEq = t1.equals(null);   // false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a CharTuple.CharTuple3 with the same elements in the same order, {@code false} otherwise
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
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple3 tuple = CharTuple.of('A', 'B', 'C');
         * String s = tuple.toString();   // "(A, B, C)"
         *
         * // Duplicate chars
         * CharTuple.CharTuple3 dup = CharTuple.of('Z', 'Z', 'Z');
         * String sDup = dup.toString();   // "(Z, Z, Z)"
         *
         * // Lower-case
         * CharTuple.CharTuple3 lower = CharTuple.of('a', 'b', 'c');
         * String sLower = lower.toString();   // "(a, b, c)"
         *
         * // Mixed case
         * CharTuple.CharTuple3 mixed = CharTuple.of('A', 'b', 'C');
         * String sMixed = mixed.toString();   // "(A, b, C)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ")";
        }

        /**
         * Returns the internal array of char elements.
         * The array is lazily initialized on first access.
         * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
         * Modifying the returned array will compromise the immutability of this tuple.
         * Use {@link #toArray()} instead if you need an array that can be safely modified.
         * </p>
         *
         * @return a char array containing all elements in order
         */
        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    /**
     * A {@code CharTuple} containing exactly four {@code char} elements.
     * <p>
     * Components are public final fields {@code _1}…{@code _4}. Unlike arity 2–3, this type does not
     * add element-unpacking {@code accept}/{@code map}/{@code filter} overloads; use
     * {@link #forEach(Throwables.CharConsumer)}, {@link #stream()}, or the whole-tuple helpers from
     * {@link PrimitiveTuple}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple4 tuple = CharTuple.of((char) 10, (char) 20, (char) 30, (char) 40);
     * char first = tuple._1;         // 10
     * char fourth = tuple._4;        // 40
     * // even arity: sorted 10,20,30,40 -> lower middle 20
     * char lowerMed = tuple.lowerMedian();  // 20
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

        /**
         * Package-private no-argument constructor; initializes all elements to {@code (char) 0}.
         * Tuples are normally created via the {@code CharTuple.of(...)} factory methods.
         */
        CharTuple4() {
            this((char) 0, (char) 0, (char) 0, (char) 0);
        }

        /**
         * Package-private constructor that creates a tuple with the specified elements.
         *
         * @param _1 the first char value
         * @param _2 the second char value
         * @param _3 the third char value
         * @param _4 the fourth char value
         */
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
         * // Basic: arity is constant regardless of element values
         * CharTuple.CharTuple4 t1 = CharTuple.of('A', 'B', 'C', 'D');
         * int a1 = t1.arity();                 // returns 4
         *
         * CharTuple.CharTuple4 t2 = CharTuple.of('W', 'X', 'Y', 'Z');
         * int a2 = t2.arity();                 // returns 4
         *
         * // Edge: all elements equal - arity still 4
         * CharTuple.CharTuple4 t3 = CharTuple.of('X', 'X', 'X', 'X');
         * int a3 = t3.arity();                 // returns 4
         *
         * // Edge: arity equals toArray().length
         * assert t1.arity() == t1.toArray().length; // true
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
         * CharTuple.CharTuple4 tuple = CharTuple.of('D', 'A', 'C', 'B');
         * char min = tuple.min();   // 'A'
         *
         * CharTuple.CharTuple4 t2 = CharTuple.of('Z', 'M', 'A', 'P');
         * char min2 = t2.min();   // 'A'
         *
         * // boundary: space char (lowest printable) and max char
         * CharTuple.CharTuple4 t3 = CharTuple.of(' ', 'Z', 'a', '\uFFFF');
         * char min3 = t3.min();   // ' '
         *
         * // all duplicates
         * CharTuple.CharTuple4 t4 = CharTuple.of('X', 'X', 'X', 'X');
         * char min4 = t4.min();   // 'X'
         * }</pre>
         *
         * @return the smallest of the four char values
         */
        @Override
        public char min() {
            return N.min(_1, _2, _3, _4);
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple4 tuple = CharTuple.of('D', 'A', 'C', 'B');
         * char max = tuple.max();   // 'D'
         *
         * CharTuple.CharTuple4 t2 = CharTuple.of('Z', 'M', 'A', 'P');
         * char max2 = t2.max();   // 'Z'
         *
         * // boundary: max char '\uFFFF'
         * CharTuple.CharTuple4 t3 = CharTuple.of(' ', 'Z', 'a', '\uFFFF');
         * char max3 = t3.max();   // '\uFFFF'
         *
         * // all duplicates
         * CharTuple.CharTuple4 t4 = CharTuple.of('M', 'M', 'M', 'M');
         * char max4 = t4.max();   // 'M'
         * }</pre>
         *
         * @return the largest of the four char values
         */
        @Override
        public char max() {
            return N.max(_1, _2, _3, _4);
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');   // 'A'=65, 'B'=66, 'C'=67, 'D'=68
         * int sum = tuple.sum();                                           // 266
         *
         * CharTuple.CharTuple4 t2 = CharTuple.of('a', 'b', 'c', 'd');   // 97, 98, 99, 100
         * int sum2 = t2.sum();                                          // 394
         *
         * // all duplicates: 'A'=65 * 4
         * CharTuple.CharTuple4 t3 = CharTuple.of('A', 'A', 'A', 'A');
         * int sum3 = t3.sum();   // 260
         *
         * // space char (32): 32 * 4
         * CharTuple.CharTuple4 t4 = CharTuple.of(' ', ' ', ' ', ' ');
         * int sum4 = t4.sum();   // 128
         * }</pre>
         *
         * @return the sum of all four unsigned code-unit values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4);
        }

        /**
         * Returns the average of all char values in this tuple as an {@code OptionalDouble}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');   // 65+66+67+68 = 266
         * tuple.average();                                               // returns OptionalDouble.of(66.5)
         *
         * // all same chars
         * CharTuple.CharTuple4 t2 = CharTuple.of('M', 'M', 'M', 'M');
         * t2.average();   // returns OptionalDouble.of(77.0)
         *
         * // non-integer average
         * CharTuple.CharTuple4 t3 = CharTuple.of('A', 'B', 'C', 'E');   // 65+66+67+69 = 267
         * t3.average();                                               // returns OptionalDouble.of(66.75)
         *
         * // boundary: space char (32)
         * CharTuple.CharTuple4 t4 = CharTuple.of(' ', ' ', 'A', 'A');   // 32+32+65+65 = 194
         * t4.average();                                               // returns OptionalDouble.of(48.5)
         * }</pre>
         *
         * @return the average of all four unsigned code-unit values as an {@code OptionalDouble}
         */
        @Override
        public OptionalDouble average() {
            return OptionalDouble.of(N.average(_1, _2, _3, _4));
        }

        /**
         * Returns a new CharTuple.CharTuple4 with the elements in reverse order.
         * The original tuple is not modified.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
         * CharTuple.CharTuple4 reversed = tuple.reversed();   // ('D', 'C', 'B', 'A')
         *
         * CharTuple.CharTuple4 t2 = CharTuple.of('Z', 'M', 'a', ' ');
         * CharTuple.CharTuple4 rev2 = t2.reversed();   // (' ', 'a', 'M', 'Z')
         *
         * // palindrome-like ABBA: reverse equals original
         * CharTuple.CharTuple4 t3 = CharTuple.of('A', 'B', 'B', 'A');
         * boolean same = t3.reversed().equals(t3);   // true
         *
         * // all duplicates: reverse equals original
         * CharTuple.CharTuple4 t4 = CharTuple.of('X', 'X', 'X', 'X');
         * boolean same2 = t4.reversed().equals(t4);   // true
         * }</pre>
         *
         * @return a new CharTuple.CharTuple4 with the elements in reverse order
         */
        @Override
        public CharTuple4 reversed() {
            return new CharTuple4(_4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
         * boolean hasB = tuple.contains('B');   // true
         * boolean hasD = tuple.contains('D');   // true (last element)
         *
         * boolean hasZ = tuple.contains('Z');       // false
         * boolean hasSpace = tuple.contains(' ');   // false
         *
         * // boundary chars
         * CharTuple.CharTuple4 t2 = CharTuple.of(' ', 'Z', 'a', '\uFFFF');
         * boolean hasMin = t2.contains(' ');        // true
         * boolean hasMax = t2.contains('\uFFFF');   // true
         * boolean hasMid = t2.contains('A');        // false
         * }</pre>
         *
         * @param value the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char value) {
            return _1 == value || _2 == value || _3 == value || _4 == value;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple4 tuple = CharTuple.of('A', 'B', 'C', 'D');
         * // Collect chars into a list in order
         * java.util.List<Character> list = new java.util.ArrayList<>();
         * tuple.forEach(c -> list.add(c));  // list is ['A', 'B', 'C', 'D']
         *
         * // Sum char code units: 65+66+67+68
         * int[] total = {0};
         * tuple.forEach(c -> total[0] += c);
         * int sum = total[0];                  // 266
         *
         * // Duplicate chars: 'Z'=90, 4 * 90 = 360
         * CharTuple.CharTuple4 dup = CharTuple.of('Z', 'Z', 'Z', 'Z');
         * int[] dupSum = {0};
         * dup.forEach(c -> dupSum[0] += c);
         * int dupTotal = dupSum[0];            // 360
         *
         * // Edge: null action throws IllegalArgumentException
         * // tuple.forEach(null);                 // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> action) throws E {
            N.checkArgNotNull(action, cs.action);

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
         * // Basic: equal tuples produce equal hashCodes
         * CharTuple.CharTuple4 t1 = CharTuple.of('A', 'B', 'C', 'D');
         * CharTuple.CharTuple4 t2 = CharTuple.of('A', 'B', 'C', 'D');
         * int h1 = t1.hashCode();              // returns 2001986
         * int h2 = t2.hashCode();              // returns 2001986
         *
         * // Basic: tuple equals -> hashCode equals
         * assert t1.hashCode() == t2.hashCode(); // true
         *
         * // Edge: different element order produces different hashCode
         * CharTuple.CharTuple4 t3 = CharTuple.of('D', 'C', 'B', 'A');
         * int h3 = t3.hashCode();              // returns 2092286
         *
         * // Edge: all same elements - hashCode is well-defined
         * CharTuple.CharTuple4 t4 = CharTuple.of('A', 'A', 'A', 'A');
         * int h4 = t4.hashCode();                                   // consistent value
         * assert CharTuple.of('A', 'A', 'A', 'A').hashCode() == h4; // true
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
         * CharTuple.CharTuple4 t1 = CharTuple.of('A', 'B', 'C', 'D');
         * CharTuple.CharTuple4 t2 = CharTuple.of('A', 'B', 'C', 'D');
         * boolean eq = t1.equals(t2);          // true
         *
         * // Different fourth element
         * CharTuple.CharTuple4 t3 = CharTuple.of('A', 'B', 'C', 'E');
         * boolean neq = t1.equals(t3);         // false
         *
         * // Different order is not equal
         * CharTuple.CharTuple4 t4 = CharTuple.of('D', 'C', 'B', 'A');
         * boolean orderMatters = t1.equals(t4); // false
         *
         * // null is never equal
         * boolean nullEq = t1.equals(null);    // false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a CharTuple.CharTuple4 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final CharTuple4 other)) {
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
         * // Basic: standard ASCII chars
         * CharTuple.CharTuple4 t = CharTuple.of('A', 'B', 'C', 'D');
         * String s = t.toString();             // returns "(A, B, C, D)"
         *
         * // Basic: lowercase chars
         * CharTuple.CharTuple4 t2 = CharTuple.of('a', 'b', 'c', 'd');
         * String s2 = t2.toString();           // returns "(a, b, c, d)"
         *
         * // Edge: all same elements
         * CharTuple.CharTuple4 t3 = CharTuple.of('X', 'X', 'X', 'X');
         * String s3 = t3.toString();           // returns "(X, X, X, X)"
         *
         * // Edge: reversed order
         * CharTuple.CharTuple4 t4 = CharTuple.of('D', 'C', 'B', 'A');
         * String s4 = t4.toString();           // returns "(D, C, B, A)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3, element4)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
        }

        /**
         * Returns the internal array of char elements.
         * The array is lazily initialized on first access.
         * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
         * Modifying the returned array will compromise the immutability of this tuple.
         * Use {@link #toArray()} instead if you need an array that can be safely modified.
         * </p>
         *
         * @return a char array containing all elements in order
         */
        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    /**
     * A {@code CharTuple} containing exactly five {@code char} elements.
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _5}.
     * Unlike arity 2–3, this type does not add element-unpacking {@code accept}/{@code map}/{@code filter}
     * overloads; use {@link #forEach(Throwables.CharConsumer)}, {@link #stream()}, or the whole-tuple helpers from
     * {@link PrimitiveTuple}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple5 tuple = CharTuple.of((char) 10, (char) 20, (char) 30, (char) 40, (char) 50);
     * char first = tuple._1;  // 10
     * char fifth = tuple._5;  // 50
     * int sum = tuple.sum();  // 150
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

        /**
         * Package-private no-argument constructor; initializes all elements to {@code (char) 0}.
         * Tuples are normally created via the {@code CharTuple.of(...)} factory methods.
         */
        CharTuple5() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        /**
         * Package-private constructor that creates a tuple with the specified elements.
         *
         * @param _1 the first char value
         * @param _2 the second char value
         * @param _3 the third char value
         * @param _4 the fourth char value
         * @param _5 the fifth char value
         */
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
         * // Basic: arity is constant regardless of element values
         * CharTuple.CharTuple5 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * int a1 = t1.arity();                 // returns 5
         *
         * CharTuple.CharTuple5 t2 = CharTuple.of('V', 'W', 'X', 'Y', 'Z');
         * int a2 = t2.arity();                 // returns 5
         *
         * // Edge: all elements equal - arity still 5
         * CharTuple.CharTuple5 t3 = CharTuple.of('X', 'X', 'X', 'X', 'X');
         * int a3 = t3.arity();                 // returns 5
         *
         * // Edge: arity equals toArray().length
         * assert t1.arity() == t1.toArray().length; // true
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
         * CharTuple.CharTuple5 tuple = CharTuple.of('E', 'A', 'C', 'B', 'D');
         * char min = tuple.min();   // 'A'
         *
         * CharTuple.CharTuple5 t2 = CharTuple.of('z', 'Z', 'A', 'a', 'M');
         * char min2 = t2.min();   // 'A'
         *
         * // boundary: space char
         * CharTuple.CharTuple5 t3 = CharTuple.of('\uFFFF', 'Z', 'A', 'a', ' ');
         * char min3 = t3.min();   // ' '
         *
         * // all duplicates
         * CharTuple.CharTuple5 t4 = CharTuple.of('B', 'B', 'B', 'B', 'B');
         * char min4 = t4.min();   // 'B'
         * }</pre>
         *
         * @return the smallest of the five char values
         */
        @Override
        public char min() {
            return N.min(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple5 tuple = CharTuple.of('E', 'A', 'C', 'B', 'D');
         * char max = tuple.max();   // 'E'
         *
         * CharTuple.CharTuple5 t2 = CharTuple.of('z', 'Z', 'A', 'a', 'M');
         * char max2 = t2.max();   // 'z'
         *
         * // boundary: max char '\uFFFF'
         * CharTuple.CharTuple5 t3 = CharTuple.of('\uFFFF', 'Z', 'A', 'a', ' ');
         * char max3 = t3.max();   // '\uFFFF'
         *
         * // all duplicates
         * CharTuple.CharTuple5 t4 = CharTuple.of('M', 'M', 'M', 'M', 'M');
         * char max4 = t4.max();   // 'M'
         * }</pre>
         *
         * @return the largest of the five char values
         */
        @Override
        public char max() {
            return N.max(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');   // 65, 66, 67, 68, 69
         * int sum = tuple.sum();                                                // 335
         *
         * CharTuple.CharTuple5 t2 = CharTuple.of('a', 'b', 'c', 'd', 'e');   // 97, 98, 99, 100, 101
         * int sum2 = t2.sum();                                               // 495
         *
         * // all same: 'A'=65 * 5
         * CharTuple.CharTuple5 t3 = CharTuple.of('A', 'A', 'A', 'A', 'A');
         * int sum3 = t3.sum();   // 325
         *
         * // space char (32): 32 * 5
         * CharTuple.CharTuple5 t4 = CharTuple.of(' ', ' ', ' ', ' ', ' ');
         * int sum4 = t4.sum();   // 160
         * }</pre>
         *
         * @return the sum of all five unsigned code-unit values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5);
        }

        /**
         * Returns the average of all char values in this tuple as an {@code OptionalDouble}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');   // 65+66+67+68+69 = 335
         * tuple.average();                                                    // returns OptionalDouble.of(67.0)
         *
         * CharTuple.CharTuple5 t2 = CharTuple.of('a', 'b', 'c', 'd', 'e');   // 97+98+99+100+101 = 495
         * t2.average();                                                    // returns OptionalDouble.of(99.0)
         *
         * // all same chars
         * CharTuple.CharTuple5 t3 = CharTuple.of('M', 'M', 'M', 'M', 'M');
         * t3.average();   // returns OptionalDouble.of(77.0)
         *
         * // boundary: space char (32)
         * CharTuple.CharTuple5 t4 = CharTuple.of(' ', ' ', ' ', ' ', ' ');
         * t4.average();   // returns OptionalDouble.of(32.0)
         * }</pre>
         *
         * @return the average of all five unsigned code-unit values as an {@code OptionalDouble}
         */
        @Override
        public OptionalDouble average() {
            return OptionalDouble.of(N.average(_1, _2, _3, _4, _5));
        }

        /**
         * Returns a new CharTuple.CharTuple5 with the elements in reverse order.
         * The original tuple is not modified.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * CharTuple.CharTuple5 reversed = tuple.reversed();   // ('E', 'D', 'C', 'B', 'A')
         *
         * CharTuple.CharTuple5 t2 = CharTuple.of('Z', 'a', ' ', '\uFFFF', 'M');
         * CharTuple.CharTuple5 rev2 = t2.reversed();   // ('M', '\uFFFF', ' ', 'a', 'Z')
         *
         * // palindrome A,B,C,B,A: reverse equals original
         * CharTuple.CharTuple5 t3 = CharTuple.of('A', 'B', 'C', 'B', 'A');
         * boolean same = t3.reversed().equals(t3);   // true
         *
         * // all duplicates: reverse equals original
         * CharTuple.CharTuple5 t4 = CharTuple.of('X', 'X', 'X', 'X', 'X');
         * boolean same2 = t4.reversed().equals(t4);   // true
         * }</pre>
         *
         * @return a new CharTuple.CharTuple5 with the elements in reverse order
         */
        @Override
        public CharTuple5 reversed() {
            return new CharTuple5(_5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * boolean hasC = tuple.contains('C');   // true
         * boolean hasE = tuple.contains('E');   // true (last element)
         *
         * boolean hasZ = tuple.contains('Z');       // false
         * boolean hasSpace = tuple.contains(' ');   // false
         *
         * // boundary chars
         * CharTuple.CharTuple5 t2 = CharTuple.of(' ', 'Z', '\uFFFF', 'a', 'b');
         * boolean hasMin = t2.contains(' ');        // true
         * boolean hasMax = t2.contains('\uFFFF');   // true
         * boolean hasMid = t2.contains('A');        // false
         * }</pre>
         *
         * @param value the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char value) {
            return _1 == value || _2 == value || _3 == value || _4 == value || _5 == value;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple5 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * // Collect chars into a list in order
         * java.util.List<Character> list = new java.util.ArrayList<>();
         * tuple.forEach(c -> list.add(c));  // list is ['A', 'B', 'C', 'D', 'E']
         *
         * // Sum char code units: 65+66+67+68+69
         * int[] total = {0};
         * tuple.forEach(c -> total[0] += c);
         * int sum = total[0];                  // 335
         *
         * // Duplicate chars: 'Z'=90, 5 * 90 = 450
         * CharTuple.CharTuple5 dup = CharTuple.of('Z', 'Z', 'Z', 'Z', 'Z');
         * int[] dupSum = {0};
         * dup.forEach(c -> dupSum[0] += c);
         * int dupTotal = dupSum[0];            // 450
         *
         * // Edge: null action throws IllegalArgumentException
         * // tuple.forEach(null);                 // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> action) throws E {
            N.checkArgNotNull(action, cs.action);

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
         * // Basic: equal tuples produce equal hashCodes
         * CharTuple.CharTuple5 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * CharTuple.CharTuple5 t2 = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * int h1 = t1.hashCode();              // returns 62061635
         * int h2 = t2.hashCode();              // returns 62061635
         *
         * // Basic: tuple equals -> hashCode equals
         * assert t1.hashCode() == t2.hashCode(); // true
         *
         * // Edge: different element order produces different hashCode
         * CharTuple.CharTuple5 t3 = CharTuple.of('E', 'D', 'C', 'B', 'A');
         * int h3 = t3.hashCode();              // returns 65815235
         *
         * // Edge: all same elements - hashCode is well-defined
         * CharTuple.CharTuple5 t4 = CharTuple.of('A', 'A', 'A', 'A', 'A');
         * assert CharTuple.of('A', 'A', 'A', 'A', 'A').hashCode() == t4.hashCode(); // true
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
         * CharTuple.CharTuple5 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * CharTuple.CharTuple5 t2 = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * boolean eq = t1.equals(t2);          // true
         *
         * // Different fifth element
         * CharTuple.CharTuple5 t3 = CharTuple.of('A', 'B', 'C', 'D', 'F');
         * boolean neq = t1.equals(t3);         // false
         *
         * // Different order is not equal
         * CharTuple.CharTuple5 t4 = CharTuple.of('E', 'D', 'C', 'B', 'A');
         * boolean orderMatters = t1.equals(t4); // false
         *
         * // null is never equal
         * boolean nullEq = t1.equals(null);    // false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a CharTuple.CharTuple5 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final CharTuple5 other)) {
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
         * // Basic: standard ASCII chars
         * CharTuple.CharTuple5 t = CharTuple.of('A', 'B', 'C', 'D', 'E');
         * String s = t.toString();             // returns "(A, B, C, D, E)"
         *
         * // Basic: lowercase chars
         * CharTuple.CharTuple5 t2 = CharTuple.of('a', 'b', 'c', 'd', 'e');
         * String s2 = t2.toString();           // returns "(a, b, c, d, e)"
         *
         * // Edge: all same elements
         * CharTuple.CharTuple5 t3 = CharTuple.of('Z', 'Z', 'Z', 'Z', 'Z');
         * String s3 = t3.toString();           // returns "(Z, Z, Z, Z, Z)"
         *
         * // Edge: mixed case chars
         * CharTuple.CharTuple5 t4 = CharTuple.of('H', 'e', 'l', 'l', 'o');
         * String s4 = t4.toString();           // returns "(H, e, l, l, o)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3, element4, element5)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ")";
        }

        /**
         * Returns the internal array of char elements.
         * The array is lazily initialized on first access.
         * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
         * Modifying the returned array will compromise the immutability of this tuple.
         * Use {@link #toArray()} instead if you need an array that can be safely modified.
         * </p>
         *
         * @return a char array containing all elements in order
         */
        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    /**
     * A {@code CharTuple} containing exactly six {@code char} elements.
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _6}.
     * Unlike arity 2–3, this type does not add element-unpacking {@code accept}/{@code map}/{@code filter}
     * overloads; use {@link #forEach(Throwables.CharConsumer)}, {@link #stream()}, or the whole-tuple helpers from
     * {@link PrimitiveTuple}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple6 tuple = CharTuple.of((char) 10, (char) 20, (char) 30, (char) 40, (char) 50, (char) 60);
     * char first = tuple._1;                            // 10
     * char sixth = tuple._6;                            // 60
     * CharTuple.CharTuple6 reversed = tuple.reversed();  // (60, 50, 40, 30, 20, 10)
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

        /**
         * Package-private no-argument constructor; initializes all elements to {@code (char) 0}.
         * Tuples are normally created via the {@code CharTuple.of(...)} factory methods.
         */
        CharTuple6() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        /**
         * Package-private constructor that creates a tuple with the specified elements.
         *
         * @param _1 the first char value
         * @param _2 the second char value
         * @param _3 the third char value
         * @param _4 the fourth char value
         * @param _5 the fifth char value
         * @param _6 the sixth char value
         */
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
         * // Basic: arity is constant regardless of element values
         * CharTuple.CharTuple6 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * int a1 = t1.arity();                 // returns 6
         *
         * CharTuple.CharTuple6 t2 = CharTuple.of('U', 'V', 'W', 'X', 'Y', 'Z');
         * int a2 = t2.arity();                 // returns 6
         *
         * // Edge: all elements equal - arity still 6
         * CharTuple.CharTuple6 t3 = CharTuple.of('X', 'X', 'X', 'X', 'X', 'X');
         * int a3 = t3.arity();                 // returns 6
         *
         * // Edge: arity equals toArray().length
         * assert t1.arity() == t1.toArray().length; // true
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
         * CharTuple.CharTuple6 tuple = CharTuple.of('F', 'A', 'C', 'E', 'B', 'D');
         * char min = tuple.min();   // 'A'
         *
         * CharTuple.CharTuple6 t2 = CharTuple.of('z', 'Z', 'A', 'a', 'M', 'N');
         * char min2 = t2.min();   // 'A'
         *
         * // boundary: space char
         * CharTuple.CharTuple6 t3 = CharTuple.of('\uFFFF', 'Z', 'A', 'a', ' ', 'b');
         * char min3 = t3.min();   // ' '
         *
         * // all duplicates
         * CharTuple.CharTuple6 t4 = CharTuple.of('C', 'C', 'C', 'C', 'C', 'C');
         * char min4 = t4.min();   // 'C'
         * }</pre>
         *
         * @return the smallest of the six char values
         */
        @Override
        public char min() {
            return N.min(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple6 tuple = CharTuple.of('F', 'A', 'C', 'E', 'B', 'D');
         * char max = tuple.max();   // 'F'
         *
         * CharTuple.CharTuple6 t2 = CharTuple.of('z', 'Z', 'A', 'a', 'M', 'N');
         * char max2 = t2.max();   // 'z'
         *
         * // boundary: max char '\uFFFF'
         * CharTuple.CharTuple6 t3 = CharTuple.of('\uFFFF', 'Z', 'A', 'a', ' ', 'b');
         * char max3 = t3.max();   // '\uFFFF'
         *
         * // all duplicates
         * CharTuple.CharTuple6 t4 = CharTuple.of('M', 'M', 'M', 'M', 'M', 'M');
         * char max4 = t4.max();   // 'M'
         * }</pre>
         *
         * @return the largest of the six char values
         */
        @Override
        public char max() {
            return N.max(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');   // 65, 66, 67, 68, 69, 70
         * int sum = tuple.sum();                                                     // 405
         *
         * CharTuple.CharTuple6 t2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');   // 97, 98, 99, 100, 101, 102
         * int sum2 = t2.sum();                                                    // 597
         *
         * // all same: 'A'=65 * 6
         * CharTuple.CharTuple6 t3 = CharTuple.of('A', 'A', 'A', 'A', 'A', 'A');
         * int sum3 = t3.sum();   // 390
         *
         * // space char (32): 32 * 6
         * CharTuple.CharTuple6 t4 = CharTuple.of(' ', ' ', ' ', ' ', ' ', ' ');
         * int sum4 = t4.sum();   // 192
         * }</pre>
         *
         * @return the sum of all six unsigned code-unit values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6);
        }

        /**
         * Returns the average of all char values in this tuple as an {@code OptionalDouble}.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');   // 65+66+67+68+69+70 = 405
         * tuple.average();                                                         // returns OptionalDouble.of(67.5)
         *
         * CharTuple.CharTuple6 t2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f');   // 97+...+102 = 597
         * t2.average();                                                         // returns OptionalDouble.of(99.5)
         *
         * // all same chars
         * CharTuple.CharTuple6 t3 = CharTuple.of('M', 'M', 'M', 'M', 'M', 'M');
         * t3.average();   // returns OptionalDouble.of(77.0)
         *
         * // boundary: space char (32)
         * CharTuple.CharTuple6 t4 = CharTuple.of(' ', ' ', ' ', ' ', ' ', ' ');
         * t4.average();   // returns OptionalDouble.of(32.0)
         * }</pre>
         *
         * @return the average of all six unsigned code-unit values as an {@code OptionalDouble}
         */
        @Override
        public OptionalDouble average() {
            return OptionalDouble.of(N.average(_1, _2, _3, _4, _5, _6));
        }

        /**
         * Returns a new CharTuple.CharTuple6 with the elements in reverse order.
         * The original tuple is not modified.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * CharTuple.CharTuple6 reversed = tuple.reversed();   // ('F', 'E', 'D', 'C', 'B', 'A')
         *
         * CharTuple.CharTuple6 t2 = CharTuple.of(' ', 'A', 'B', 'C', 'Z', '\uFFFF');
         * CharTuple.CharTuple6 rev2 = t2.reversed();   // ('\uFFFF', 'Z', 'C', 'B', 'A', ' ')
         *
         * // palindrome A,B,C,C,B,A: reverse equals original
         * CharTuple.CharTuple6 t3 = CharTuple.of('A', 'B', 'C', 'C', 'B', 'A');
         * boolean same = t3.reversed().equals(t3);   // true
         *
         * // all duplicates: reverse equals original
         * CharTuple.CharTuple6 t4 = CharTuple.of('X', 'X', 'X', 'X', 'X', 'X');
         * boolean same2 = t4.reversed().equals(t4);   // true
         * }</pre>
         *
         * @return a new CharTuple.CharTuple6 with the elements in reverse order
         */
        @Override
        public CharTuple6 reversed() {
            return new CharTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * boolean hasD = tuple.contains('D');   // true
         * boolean hasF = tuple.contains('F');   // true (last element)
         *
         * boolean hasZ = tuple.contains('Z');       // false
         * boolean hasSpace = tuple.contains(' ');   // false
         *
         * // boundary chars
         * CharTuple.CharTuple6 t2 = CharTuple.of(' ', 'Z', '\uFFFF', 'a', 'b', 'c');
         * boolean hasMin = t2.contains(' ');        // true
         * boolean hasMax = t2.contains('\uFFFF');   // true
         * boolean hasMid = t2.contains('A');        // false
         * }</pre>
         *
         * @param value the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char value) {
            return _1 == value || _2 == value || _3 == value || _4 == value || _5 == value || _6 == value;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple6 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * // Collect chars into a list in order
         * java.util.List<Character> list = new java.util.ArrayList<>();
         * tuple.forEach(c -> list.add(c));  // list is ['A', 'B', 'C', 'D', 'E', 'F']
         *
         * // Sum char code units: 65+66+67+68+69+70
         * int[] total = {0};
         * tuple.forEach(c -> total[0] += c);
         * int sum = total[0];                  // 405
         *
         * // Duplicate chars: 'Z'=90, 6 * 90 = 540
         * CharTuple.CharTuple6 dup = CharTuple.of('Z', 'Z', 'Z', 'Z', 'Z', 'Z');
         * int[] dupSum = {0};
         * dup.forEach(c -> dupSum[0] += c);
         * int dupTotal = dupSum[0];            // 540
         *
         * // Edge: null action throws IllegalArgumentException
         * // tuple.forEach(null);                 // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> action) throws E {
            N.checkArgNotNull(action, cs.action);

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
         * // Basic: equal tuples produce equal hashCodes
         * CharTuple.CharTuple6 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * CharTuple.CharTuple6 t2 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * int h1 = t1.hashCode();              // returns 1923910755
         * int h2 = t2.hashCode();              // returns 1923910755
         *
         * // Basic: tuple equals -> hashCode equals
         * assert t1.hashCode() == t2.hashCode(); // true
         *
         * // Edge: different element order produces different hashCode
         * CharTuple.CharTuple6 t3 = CharTuple.of('F', 'E', 'D', 'C', 'B', 'A');
         * int h3 = t3.hashCode();              // returns 2069855805
         *
         * // Edge: all same elements - hashCode is well-defined
         * CharTuple.CharTuple6 t4 = CharTuple.of('A', 'A', 'A', 'A', 'A', 'A');
         * assert CharTuple.of('A', 'A', 'A', 'A', 'A', 'A').hashCode() == t4.hashCode(); // true
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
         * CharTuple.CharTuple6 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * CharTuple.CharTuple6 t2 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * boolean eq = t1.equals(t2);          // true
         *
         * // Different sixth element
         * CharTuple.CharTuple6 t3 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'G');
         * boolean neq = t1.equals(t3);         // false
         *
         * // Different order is not equal
         * CharTuple.CharTuple6 t4 = CharTuple.of('F', 'E', 'D', 'C', 'B', 'A');
         * boolean orderMatters = t1.equals(t4); // false
         *
         * // null is never equal
         * boolean nullEq = t1.equals(null);    // false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a CharTuple.CharTuple6 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final CharTuple6 other)) {
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
         * // Basic: standard ASCII chars
         * CharTuple.CharTuple6 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F');
         * String s = t.toString();             // returns "(A, B, C, D, E, F)"
         *
         * // Basic: digit chars
         * CharTuple.CharTuple6 t2 = CharTuple.of('1', '2', '3', '4', '5', '6');
         * String s2 = t2.toString();           // returns "(1, 2, 3, 4, 5, 6)"
         *
         * // Edge: reversed order
         * CharTuple.CharTuple6 t3 = CharTuple.of('F', 'E', 'D', 'C', 'B', 'A');
         * String s3 = t3.toString();           // returns "(F, E, D, C, B, A)"
         *
         * // Edge: all same elements
         * CharTuple.CharTuple6 t4 = CharTuple.of('X', 'X', 'X', 'X', 'X', 'X');
         * String s4 = t4.toString();           // returns "(X, X, X, X, X, X)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, element3, element4, element5, element6)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ")";
        }

        /**
         * Returns the internal array of char elements.
         * The array is lazily initialized on first access.
         * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
         * Modifying the returned array will compromise the immutability of this tuple.
         * Use {@link #toArray()} instead if you need an array that can be safely modified.
         * </p>
         *
         * @return a char array containing all elements in order
         */
        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    /**
     * A {@code CharTuple} containing exactly seven {@code char} elements.
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _7}.
     * Unlike arity 2–3, this type does not add element-unpacking {@code accept}/{@code map}/{@code filter}
     * overloads; use {@link #forEach(Throwables.CharConsumer)}, {@link #stream()}, or the whole-tuple helpers from
     * {@link PrimitiveTuple}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple7 tuple = CharTuple.of((char) 10, (char) 20, (char) 30, (char) 40, (char) 50, (char) 60, (char) 70);
     * char first = tuple._1;           // 10
     * char seventh = tuple._7;         // 70
     * char[] array = tuple.toArray();  // [10, 20, 30, 40, 50, 60, 70]
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

        /**
         * Package-private no-argument constructor; initializes all elements to {@code (char) 0}.
         * Tuples are normally created via the {@code CharTuple.of(...)} factory methods.
         */
        CharTuple7() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        /**
         * Package-private constructor that creates a tuple with the specified elements.
         *
         * @param _1 the first char value
         * @param _2 the second char value
         * @param _3 the third char value
         * @param _4 the fourth char value
         * @param _5 the fifth char value
         * @param _6 the sixth char value
         * @param _7 the seventh char value
         */
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
         * // Basic: arity is constant regardless of element values
         * CharTuple.CharTuple7 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * int a1 = t1.arity();                 // returns 7
         *
         * CharTuple.CharTuple7 t2 = CharTuple.of('T', 'U', 'V', 'W', 'X', 'Y', 'Z');
         * int a2 = t2.arity();                 // returns 7
         *
         * // Edge: all elements equal - arity still 7
         * CharTuple.CharTuple7 t3 = CharTuple.of('X', 'X', 'X', 'X', 'X', 'X', 'X');
         * int a3 = t3.arity();                 // returns 7
         *
         * // Edge: arity equals toArray().length
         * assert t1.arity() == t1.toArray().length; // true
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
         * CharTuple.CharTuple7 tuple = CharTuple.of('G', 'A', 'C', 'E', 'B', 'D', 'F');
         * char min = tuple.min();   // 'A'
         *
         * CharTuple.CharTuple7 asc = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * char min2 = asc.min();   // 'A'
         *
         * // all elements identical
         * CharTuple.CharTuple7 same = CharTuple.of('x', 'x', 'x', 'x', 'x', 'x', 'x');
         * char min3 = same.min();   // 'x'
         *
         * // boundary: space char (' ' = 32) is minimum
         * CharTuple.CharTuple7 bnd = CharTuple.of(' ', 'a', 'b', 'c', 'd', 'e', 'f');
         * char min4 = bnd.min();   // ' '
         * }</pre>
         *
         * @return the smallest of the seven char values
         */
        @Override
        public char min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple7 tuple = CharTuple.of('G', 'A', 'C', 'E', 'B', 'D', 'F');
         * char max = tuple.max();   // 'G'
         *
         * CharTuple.CharTuple7 asc = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * char max2 = asc.max();   // 'G'
         *
         * // all elements identical
         * CharTuple.CharTuple7 same = CharTuple.of('z', 'z', 'z', 'z', 'z', 'z', 'z');
         * char max3 = same.max();   // 'z'
         *
         * // boundary: '\uFFFF' (U+FFFF = 65535) is max possible char
         * CharTuple.CharTuple7 bnd = CharTuple.of('\uFFFF', 'a', 'b', 'c', 'd', 'e', 'f');
         * char max4 = bnd.max();   // '\uFFFF'
         * }</pre>
         *
         * @return the largest of the seven char values
         */
        @Override
        public char max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');   // 65, 66, 67, 68, 69, 70, 71
         * int sum = tuple.sum();                                                          // 476
         *
         * CharTuple.CharTuple7 same = CharTuple.of('a', 'a', 'a', 'a', 'a', 'a', 'a');   // 'a' = 97
         * int sum2 = same.sum();                                                         // 679
         *
         * // boundary: all space chars (' ' = 32)
         * CharTuple.CharTuple7 spaces = CharTuple.of(' ', ' ', ' ', ' ', ' ', ' ', ' ');
         * int sum3 = spaces.sum();   // 224
         *
         * // boundary: '\uFFFF' (65535) + six 'A' (65) = 65535 + 390 = 65925
         * CharTuple.CharTuple7 mixed = CharTuple.of('\uFFFF', 'A', 'A', 'A', 'A', 'A', 'A');
         * int sum4 = mixed.sum();   // 65925
         * }</pre>
         *
         * @return the sum of all seven unsigned code-unit values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7);
        }

        /**
         * Returns the average of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');   // 65, 66, 67, 68, 69, 70, 71
         * tuple.average();                                                              // returns OptionalDouble.of(68.0)
         *
         * CharTuple.CharTuple7 same = CharTuple.of('a', 'a', 'a', 'a', 'a', 'a', 'a');   // 'a' = 97
         * same.average();                                                              // returns OptionalDouble.of(97.0)
         *
         * // boundary: all space chars (' ' = 32)
         * CharTuple.CharTuple7 spaces = CharTuple.of(' ', ' ', ' ', ' ', ' ', ' ', ' ');
         * spaces.average();   // returns OptionalDouble.of(32.0)
         *
         * // non-integer result: five 'A' (65) + two 'B' (66) = 457/7 ~ 65.285...
         * CharTuple.CharTuple7 nonInt = CharTuple.of('A', 'A', 'A', 'A', 'A', 'B', 'B');
         * nonInt.average();   // returns OptionalDouble.of(65.285714...)
         * }</pre>
         *
         * @return the average of all seven unsigned code-unit values as an {@code OptionalDouble}
         */
        @Override
        public OptionalDouble average() {
            return OptionalDouble.of(N.average(_1, _2, _3, _4, _5, _6, _7));
        }

        /**
         * Returns a new CharTuple.CharTuple7 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * CharTuple.CharTuple7 reversed = tuple.reversed();   // ('G', 'F', 'E', 'D', 'C', 'B', 'A')
         *
         * CharTuple.CharTuple7 bnd = CharTuple.of(' ', '\uFFFF', 'a', 'b', 'c', 'd', 'e');
         * CharTuple.CharTuple7 br = bnd.reversed();   // ('e', 'd', 'c', 'b', 'a', '\uFFFF', ' ')
         *
         * // all same: reverse is identical
         * CharTuple.CharTuple7 same = CharTuple.of('x', 'x', 'x', 'x', 'x', 'x', 'x');
         * CharTuple.CharTuple7 sr = same.reversed();   // ('x', 'x', 'x', 'x', 'x', 'x', 'x')
         *
         * // with duplicates: (A, A, B, C, B, A, A) reversed = (A, A, B, C, B, A, A)
         * CharTuple.CharTuple7 dup = CharTuple.of('A', 'A', 'B', 'C', 'B', 'A', 'A');
         * CharTuple.CharTuple7 dr = dup.reversed();   // ('A', 'A', 'B', 'C', 'B', 'A', 'A')
         * }</pre>
         *
         * @return a new CharTuple.CharTuple7 with the elements in reverse order
         */
        @Override
        public CharTuple7 reversed() {
            return new CharTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * boolean hasE = tuple.contains('E');   // true
         * boolean hasZ = tuple.contains('Z');   // false
         *
         * // boundary chars
         * CharTuple.CharTuple7 bnd = CharTuple.of(' ', '\uFFFF', 'a', 'b', 'c', 'd', 'e');
         * boolean hasSpace = bnd.contains(' ');      // true
         * boolean hasMax = bnd.contains('\uFFFF');   // true
         * boolean hasMiss = bnd.contains('f');       // false
         *
         * // all duplicates: only that char is found
         * CharTuple.CharTuple7 dup = CharTuple.of('A', 'A', 'A', 'A', 'A', 'A', 'A');
         * boolean hasA = dup.contains('A');   // true
         * boolean hasB = dup.contains('B');   // false
         * }</pre>
         *
         * @param value the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char value) {
            return _1 == value || _2 == value || _3 == value || _4 == value || _5 == value || _6 == value || _7 == value;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple7 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * // Collect chars into a list in order
         * java.util.List<Character> list = new java.util.ArrayList<>();
         * tuple.forEach(c -> list.add(c));  // list is ['A', 'B', 'C', 'D', 'E', 'F', 'G']
         *
         * // Sum char code units: 65+66+67+68+69+70+71
         * int[] total = {0};
         * tuple.forEach(c -> total[0] += c);
         * int sum = total[0];                  // 476
         *
         * // Duplicate chars: 'Z'=90, 7 * 90 = 630
         * CharTuple.CharTuple7 dup = CharTuple.of('Z', 'Z', 'Z', 'Z', 'Z', 'Z', 'Z');
         * int[] dupSum = {0};
         * dup.forEach(c -> dupSum[0] += c);
         * int dupTotal = dupSum[0];            // 630
         *
         * // Edge: null action throws IllegalArgumentException
         * // tuple.forEach(null);                 // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> action) throws E {
            N.checkArgNotNull(action, cs.action);

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
         * // Basic: equal tuples produce equal hashCodes
         * CharTuple.CharTuple7 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * CharTuple.CharTuple7 t2 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * int h1 = t1.hashCode();              // returns -488308668
         * int h2 = t2.hashCode();              // returns -488308668
         *
         * // Basic: tuple equals -> hashCode equals
         * assert t1.hashCode() == t2.hashCode(); // true
         *
         * // Edge: different element order produces different hashCode
         * CharTuple.CharTuple7 t3 = CharTuple.of('G', 'F', 'E', 'D', 'C', 'B', 'A');
         * int h3 = t3.hashCode();              // returns 658107716
         *
         * // Edge: all same elements - hashCode is well-defined
         * CharTuple.CharTuple7 t4 = CharTuple.of('A', 'A', 'A', 'A', 'A', 'A', 'A');
         * assert CharTuple.of('A', 'A', 'A', 'A', 'A', 'A', 'A').hashCode() == t4.hashCode(); // true
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
         * CharTuple.CharTuple7 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * CharTuple.CharTuple7 t2 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * boolean eq = t1.equals(t2);          // true
         *
         * // Different seventh element
         * CharTuple.CharTuple7 t3 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'H');
         * boolean neq = t1.equals(t3);         // false
         *
         * // Different order is not equal
         * CharTuple.CharTuple7 t4 = CharTuple.of('G', 'F', 'E', 'D', 'C', 'B', 'A');
         * boolean orderMatters = t1.equals(t4); // false
         *
         * // null is never equal
         * boolean nullEq = t1.equals(null);    // false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a CharTuple.CharTuple7 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final CharTuple7 other)) {
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
         * // Basic: standard ASCII chars
         * CharTuple.CharTuple7 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G');
         * String s = t.toString();             // returns "(A, B, C, D, E, F, G)"
         *
         * // Basic: lowercase chars
         * CharTuple.CharTuple7 t2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
         * String s2 = t2.toString();           // returns "(a, b, c, d, e, f, g)"
         *
         * // Edge: reversed order
         * CharTuple.CharTuple7 t3 = CharTuple.of('G', 'F', 'E', 'D', 'C', 'B', 'A');
         * String s3 = t3.toString();           // returns "(G, F, E, D, C, B, A)"
         *
         * // Edge: meaningful word chars
         * CharTuple.CharTuple7 t4 = CharTuple.of('M', 'O', 'N', 'D', 'A', 'Y', 'S');
         * String s4 = t4.toString();           // returns "(M, O, N, D, A, Y, S)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, ..., element7)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ")";
        }

        /**
         * Returns the internal array of char elements.
         * The array is lazily initialized on first access.
         * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
         * Modifying the returned array will compromise the immutability of this tuple.
         * Use {@link #toArray()} instead if you need an array that can be safely modified.
         * </p>
         *
         * @return a char array containing all elements in order
         */
        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    /**
     * A {@code CharTuple} containing exactly eight {@code char} elements.
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _8}.
     * Unlike arity 2–3, this type does not add element-unpacking {@code accept}/{@code map}/{@code filter}
     * overloads; use {@link #forEach(Throwables.CharConsumer)}, {@link #stream()}, or the whole-tuple helpers from
     * {@link PrimitiveTuple}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple8 tuple = CharTuple.of((char) 10, (char) 20, (char) 30, (char) 40, (char) 50, (char) 60, (char) 70, (char) 80);
     * char first = tuple._1;   // 10
     * char eighth = tuple._8;  // 80
     * CharList list = tuple.toList();
     * }</pre>
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

        /**
         * Package-private no-argument constructor; initializes all elements to {@code (char) 0}.
         * Tuples are normally created via the {@code CharTuple.of(...)} factory methods.
         */
        CharTuple8() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        /**
         * Package-private constructor that creates a tuple with the specified elements.
         *
         * @param _1 the first char value
         * @param _2 the second char value
         * @param _3 the third char value
         * @param _4 the fourth char value
         * @param _5 the fifth char value
         * @param _6 the sixth char value
         * @param _7 the seventh char value
         * @param _8 the eighth char value
         */
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
         * // Basic: arity is constant regardless of element values
         * CharTuple.CharTuple8 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * int a1 = t1.arity();                 // returns 8
         *
         * CharTuple.CharTuple8 t2 = CharTuple.of('S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
         * int a2 = t2.arity();                 // returns 8
         *
         * // Edge: all elements equal - arity still 8
         * CharTuple.CharTuple8 t3 = CharTuple.of('X', 'X', 'X', 'X', 'X', 'X', 'X', 'X');
         * int a3 = t3.arity();                 // returns 8
         *
         * // Edge: arity equals toArray().length
         * assert t1.arity() == t1.toArray().length; // true
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
         * CharTuple.CharTuple8 tuple = CharTuple.of('H', 'A', 'C', 'E', 'B', 'D', 'F', 'G');
         * char min = tuple.min();   // 'A'
         *
         * CharTuple.CharTuple8 asc = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * char min2 = asc.min();   // 'A'
         *
         * // all elements identical
         * CharTuple.CharTuple8 same = CharTuple.of('x', 'x', 'x', 'x', 'x', 'x', 'x', 'x');
         * char min3 = same.min();   // 'x'
         *
         * // boundary: space char (' ' = 32) is minimum
         * CharTuple.CharTuple8 bnd = CharTuple.of(' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g');
         * char min4 = bnd.min();   // ' '
         * }</pre>
         *
         * @return the smallest of the eight char values
         */
        @Override
        public char min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple8 tuple = CharTuple.of('H', 'A', 'C', 'E', 'B', 'D', 'F', 'G');
         * char max = tuple.max();   // 'H'
         *
         * CharTuple.CharTuple8 asc = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * char max2 = asc.max();   // 'H'
         *
         * // all elements identical
         * CharTuple.CharTuple8 same = CharTuple.of('z', 'z', 'z', 'z', 'z', 'z', 'z', 'z');
         * char max3 = same.max();   // 'z'
         *
         * // boundary: '\uFFFF' (U+FFFF = 65535) is max possible char
         * CharTuple.CharTuple8 bnd = CharTuple.of('\uFFFF', 'a', 'b', 'c', 'd', 'e', 'f', 'g');
         * char max4 = bnd.max();   // '\uFFFF'
         * }</pre>
         *
         * @return the largest of the eight char values
         */
        @Override
        public char max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');   // 65-72
         * int sum = tuple.sum();                                                               // 548
         *
         * CharTuple.CharTuple8 same = CharTuple.of('a', 'a', 'a', 'a', 'a', 'a', 'a', 'a');   // 'a' = 97
         * int sum2 = same.sum();                                                              // 776
         *
         * // boundary: all space chars (' ' = 32)
         * CharTuple.CharTuple8 spaces = CharTuple.of(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ');
         * int sum3 = spaces.sum();   // 256
         *
         * // boundary: '\uFFFF' (65535) + seven 'A' (65) = 65535 + 455 = 65990
         * CharTuple.CharTuple8 mixed = CharTuple.of('\uFFFF', 'A', 'A', 'A', 'A', 'A', 'A', 'A');
         * int sum4 = mixed.sum();   // 65990
         * }</pre>
         *
         * @return the sum of all eight unsigned code-unit values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8);
        }

        /**
         * Returns the average of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');   // 65-72
         * tuple.average();                                                                   // returns OptionalDouble.of(68.5)
         *
         * CharTuple.CharTuple8 same = CharTuple.of('a', 'a', 'a', 'a', 'a', 'a', 'a', 'a');   // 'a' = 97
         * same.average();                                                                   // returns OptionalDouble.of(97.0)
         *
         * // boundary: all space chars (' ' = 32)
         * CharTuple.CharTuple8 spaces = CharTuple.of(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ');
         * spaces.average();   // returns OptionalDouble.of(32.0)
         *
         * // boundary: (65535 + 65 * 7) / 8 = 65990 / 8 = 8248.75
         * CharTuple.CharTuple8 mixed = CharTuple.of('\uFFFF', 'A', 'A', 'A', 'A', 'A', 'A', 'A');
         * mixed.average();   // returns OptionalDouble.of(8248.75)
         * }</pre>
         *
         * @return the average of all eight unsigned code-unit values as an {@code OptionalDouble}
         */
        @Override
        public OptionalDouble average() {
            return OptionalDouble.of(N.average(_1, _2, _3, _4, _5, _6, _7, _8));
        }

        /**
         * Returns a new CharTuple.CharTuple8 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * CharTuple.CharTuple8 reversed = tuple.reversed();   // ('H', 'G', 'F', 'E', 'D', 'C', 'B', 'A')
         *
         * CharTuple.CharTuple8 bnd = CharTuple.of(' ', '\uFFFF', 'a', 'b', 'c', 'd', 'e', 'f');
         * CharTuple.CharTuple8 br = bnd.reversed();   // ('f', 'e', 'd', 'c', 'b', 'a', '\uFFFF', ' ')
         *
         * // all same: reverse is identical
         * CharTuple.CharTuple8 same = CharTuple.of('x', 'x', 'x', 'x', 'x', 'x', 'x', 'x');
         * CharTuple.CharTuple8 sr = same.reversed();   // ('x', 'x', 'x', 'x', 'x', 'x', 'x', 'x')
         *
         * // palindrome: (A, B, C, D, D, C, B, A) reversed = (A, B, C, D, D, C, B, A)
         * CharTuple.CharTuple8 pal = CharTuple.of('A', 'B', 'C', 'D', 'D', 'C', 'B', 'A');
         * CharTuple.CharTuple8 pr = pal.reversed();   // same as pal
         * }</pre>
         *
         * @return a new CharTuple.CharTuple8 with the elements in reverse order
         */
        @Override
        public CharTuple8 reversed() {
            return new CharTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * boolean hasF = tuple.contains('F');   // true
         * boolean hasZ = tuple.contains('Z');   // false
         *
         * // boundary chars
         * CharTuple.CharTuple8 bnd = CharTuple.of(' ', '\uFFFF', 'a', 'b', 'c', 'd', 'e', 'f');
         * boolean hasSpace = bnd.contains(' ');      // true
         * boolean hasMax = bnd.contains('\uFFFF');   // true
         * boolean hasMiss = bnd.contains('g');       // false
         *
         * // all duplicates: only that char is found
         * CharTuple.CharTuple8 dup = CharTuple.of('A', 'A', 'A', 'A', 'A', 'A', 'A', 'A');
         * boolean hasA = dup.contains('A');   // true
         * boolean hasB = dup.contains('B');   // false
         * }</pre>
         *
         * @param value the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char value) {
            return _1 == value || _2 == value || _3 == value || _4 == value || _5 == value || _6 == value || _7 == value || _8 == value;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple8 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * // Collect chars into a list in order
         * java.util.List<Character> list = new java.util.ArrayList<>();
         * tuple.forEach(c -> list.add(c));  // list is ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
         *
         * // Sum char code units: 65+66+67+68+69+70+71+72
         * int[] total = {0};
         * tuple.forEach(c -> total[0] += c);
         * int sum = total[0];                  // 548
         *
         * // Duplicate chars: 'Z'=90, 8 * 90 = 720
         * CharTuple.CharTuple8 dup = CharTuple.of('Z', 'Z', 'Z', 'Z', 'Z', 'Z', 'Z', 'Z');
         * int[] dupSum = {0};
         * dup.forEach(c -> dupSum[0] += c);
         * int dupTotal = dupSum[0];            // 720
         *
         * // Edge: null action throws IllegalArgumentException
         * // tuple.forEach(null);                 // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> action) throws E {
            N.checkArgNotNull(action, cs.action);

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
         * // Basic: equal tuples produce equal hashCodes
         * CharTuple.CharTuple8 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * CharTuple.CharTuple8 t2 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * int h1 = t1.hashCode();              // returns 2042300548
         * int h2 = t2.hashCode();              // returns 2042300548
         *
         * // Basic: tuple equals -> hashCode equals
         * assert t1.hashCode() == t2.hashCode(); // true
         *
         * // Edge: different element order produces different hashCode
         * CharTuple.CharTuple8 t3 = CharTuple.of('H', 'G', 'F', 'E', 'D', 'C', 'B', 'A');
         * int h3 = t3.hashCode();              // returns 1586400252
         *
         * // Edge: all same elements - hashCode is well-defined
         * CharTuple.CharTuple8 t4 = CharTuple.of('A', 'A', 'A', 'A', 'A', 'A', 'A', 'A');
         * assert CharTuple.of('A', 'A', 'A', 'A', 'A', 'A', 'A', 'A').hashCode() == t4.hashCode(); // true
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
         * CharTuple.CharTuple8 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * CharTuple.CharTuple8 t2 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * boolean eq = t1.equals(t2);          // true
         *
         * // Different eighth element
         * CharTuple.CharTuple8 t3 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'I');
         * boolean neq = t1.equals(t3);         // false
         *
         * // Different order is not equal
         * CharTuple.CharTuple8 t4 = CharTuple.of('H', 'G', 'F', 'E', 'D', 'C', 'B', 'A');
         * boolean orderMatters = t1.equals(t4); // false
         *
         * // null is never equal
         * boolean nullEq = t1.equals(null);    // false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a CharTuple.CharTuple8 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final CharTuple8 other)) {
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
         * // Basic: standard ASCII chars
         * CharTuple.CharTuple8 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H');
         * String s = t.toString();             // returns "(A, B, C, D, E, F, G, H)"
         *
         * // Basic: lowercase chars
         * CharTuple.CharTuple8 t2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
         * String s2 = t2.toString();           // returns "(a, b, c, d, e, f, g, h)"
         *
         * // Edge: reversed order
         * CharTuple.CharTuple8 t3 = CharTuple.of('H', 'G', 'F', 'E', 'D', 'C', 'B', 'A');
         * String s3 = t3.toString();           // returns "(H, G, F, E, D, C, B, A)"
         *
         * // Edge: digit chars
         * CharTuple.CharTuple8 t4 = CharTuple.of('1', '2', '3', '4', '5', '6', '7', '8');
         * String s4 = t4.toString();           // returns "(1, 2, 3, 4, 5, 6, 7, 8)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, ..., element8)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")";
        }

        /**
         * Returns the internal array of char elements.
         * The array is lazily initialized on first access.
         * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
         * Modifying the returned array will compromise the immutability of this tuple.
         * Use {@link #toArray()} instead if you need an array that can be safely modified.
         * </p>
         *
         * @return a char array containing all elements in order
         */
        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    /**
     * A {@code CharTuple} containing exactly nine {@code char} elements.
     * <p>
     * Provides direct access to elements through public final fields {@code _1} through {@code _9}.
     * Unlike arity 2–3, this type does not add element-unpacking {@code accept}/{@code map}/{@code filter}
     * overloads; use {@link #forEach(Throwables.CharConsumer)}, {@link #stream()}, or the whole-tuple helpers from
     * {@link PrimitiveTuple}.
     * </p>
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * CharTuple.CharTuple9 tuple = CharTuple.of((char) 10, (char) 20, (char) 30, (char) 40, (char) 50, (char) 60, (char) 70, (char) 80, (char) 90);
     * char first = tuple._1;      // 10
     * char ninth = tuple._9;      // 90
     * int arity = tuple.arity();  // 9
     * }</pre>
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

        /**
         * Package-private no-argument constructor; initializes all elements to {@code (char) 0}.
         * Tuples are normally created via the {@code CharTuple.of(...)} factory methods.
         */
        CharTuple9() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        /**
         * Package-private constructor that creates a tuple with the specified elements.
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
         */
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
         * // Basic: arity is constant regardless of element values
         * CharTuple.CharTuple9 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * int a1 = t1.arity();                 // returns 9
         *
         * CharTuple.CharTuple9 t2 = CharTuple.of('R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
         * int a2 = t2.arity();                 // returns 9
         *
         * // Edge: all elements equal - arity still 9
         * CharTuple.CharTuple9 t3 = CharTuple.of('X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X');
         * int a3 = t3.arity();                 // returns 9
         *
         * // Edge: arity equals toArray().length
         * assert t1.arity() == t1.toArray().length; // true
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
         * CharTuple.CharTuple9 tuple = CharTuple.of('I', 'A', 'C', 'E', 'B', 'D', 'F', 'G', 'H');
         * char min = tuple.min();   // 'A'
         *
         * CharTuple.CharTuple9 asc = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * char min2 = asc.min();   // 'A'
         *
         * // all elements identical
         * CharTuple.CharTuple9 same = CharTuple.of('x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x');
         * char min3 = same.min();   // 'x'
         *
         * // boundary: space char (' ' = 32) is minimum
         * CharTuple.CharTuple9 bnd = CharTuple.of(' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
         * char min4 = bnd.min();   // ' '
         * }</pre>
         *
         * @return the smallest of the nine char values
         */
        @Override
        public char min() {
            return N.min(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the maximum char value in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple9 tuple = CharTuple.of('I', 'A', 'C', 'E', 'B', 'D', 'F', 'G', 'H');
         * char max = tuple.max();   // 'I'
         *
         * CharTuple.CharTuple9 asc = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * char max2 = asc.max();   // 'I'
         *
         * // all elements identical
         * CharTuple.CharTuple9 same = CharTuple.of('z', 'z', 'z', 'z', 'z', 'z', 'z', 'z', 'z');
         * char max3 = same.max();   // 'z'
         *
         * // boundary: '\uFFFF' (U+FFFF = 65535) is max possible char
         * CharTuple.CharTuple9 bnd = CharTuple.of('\uFFFF', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
         * char max4 = bnd.max();   // '\uFFFF'
         * }</pre>
         *
         * @return the largest of the nine char values
         */
        @Override
        public char max() {
            return N.max(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the sum of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');   // 65-73
         * int sum = tuple.sum();                                                                    // 621
         *
         * CharTuple.CharTuple9 same = CharTuple.of('a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a');   // 'a' = 97
         * int sum2 = same.sum();                                                                   // 873
         *
         * // boundary: all space chars (' ' = 32)
         * CharTuple.CharTuple9 spaces = CharTuple.of(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ');
         * int sum3 = spaces.sum();   // 288
         *
         * // boundary: '\uFFFF' (65535) + eight 'A' (65) = 65535 + 520 = 66055
         * CharTuple.CharTuple9 mixed = CharTuple.of('\uFFFF', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A');
         * int sum4 = mixed.sum();   // 66055
         * }</pre>
         *
         * @return the sum of all nine unsigned code-unit values as an {@code int}
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3, _4, _5, _6, _7, _8, _9);
        }

        /**
         * Returns the average of all char values in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');   // 65-73
         * tuple.average();                                                                        // returns OptionalDouble.of(69.0)
         *
         * CharTuple.CharTuple9 same = CharTuple.of('a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a');   // 'a' = 97
         * same.average();                                                                        // returns OptionalDouble.of(97.0)
         *
         * // boundary: all space chars (' ' = 32)
         * CharTuple.CharTuple9 spaces = CharTuple.of(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ');
         * spaces.average();   // returns OptionalDouble.of(32.0)
         *
         * // boundary: (65535 + 65 * 8) / 9 = 66055 / 9 ~ 7339.444...
         * CharTuple.CharTuple9 mixed = CharTuple.of('\uFFFF', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A');
         * mixed.average();   // returns OptionalDouble.of(7339.444...)
         * }</pre>
         *
         * @return the average of all nine unsigned code-unit values as an {@code OptionalDouble}
         */
        @Override
        public OptionalDouble average() {
            return OptionalDouble.of(N.average(_1, _2, _3, _4, _5, _6, _7, _8, _9));
        }

        /**
         * Returns a new CharTuple.CharTuple9 with the elements in reverse order.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * CharTuple.CharTuple9 reversed = tuple.reversed();   // ('I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A')
         *
         * CharTuple.CharTuple9 bnd = CharTuple.of(' ', '\uFFFF', 'a', 'b', 'c', 'd', 'e', 'f', 'g');
         * CharTuple.CharTuple9 br = bnd.reversed();   // ('g', 'f', 'e', 'd', 'c', 'b', 'a', '\uFFFF', ' ')
         *
         * // all same: reverse is identical
         * CharTuple.CharTuple9 same = CharTuple.of('x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x');
         * CharTuple.CharTuple9 sr = same.reversed();   // ('x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x')
         *
         * // palindrome: (A, B, C, D, E, D, C, B, A) reversed = same
         * CharTuple.CharTuple9 pal = CharTuple.of('A', 'B', 'C', 'D', 'E', 'D', 'C', 'B', 'A');
         * CharTuple.CharTuple9 pr = pal.reversed();   // same as pal
         * }</pre>
         *
         * @return a new CharTuple.CharTuple9 with the elements in reverse order
         */
        @Override
        public CharTuple9 reversed() {
            return new CharTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         * Checks if this tuple contains the specified char value.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * boolean hasG = tuple.contains('G');   // true
         * boolean hasZ = tuple.contains('Z');   // false
         *
         * // boundary chars
         * CharTuple.CharTuple9 bnd = CharTuple.of(' ', '\uFFFF', 'a', 'b', 'c', 'd', 'e', 'f', 'g');
         * boolean hasSpace = bnd.contains(' ');      // true
         * boolean hasMax = bnd.contains('\uFFFF');   // true
         * boolean hasMiss = bnd.contains('h');       // false
         *
         * // all duplicates: only that char is found
         * CharTuple.CharTuple9 dup = CharTuple.of('A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A');
         * boolean hasA = dup.contains('A');   // true
         * boolean hasB = dup.contains('B');   // false
         * }</pre>
         *
         * @param value the char value to search for
         * @return {@code true} if the value is found in this tuple, {@code false} otherwise
         */
        @Override
        public boolean contains(final char value) {
            return _1 == value || _2 == value || _3 == value || _4 == value || _5 == value || _6 == value || _7 == value || _8 == value || _9 == value;
        }

        /**
         * Performs the given action for each element in this tuple.
         *
         * <p><b>Usage Examples:</b></p>
         * <pre>{@code
         * CharTuple.CharTuple9 tuple = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * // Collect chars into a list in order
         * java.util.List<Character> list = new java.util.ArrayList<>();
         * tuple.forEach(c -> list.add(c));  // list is ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I']
         *
         * // Sum char code units: 65+66+67+68+69+70+71+72+73
         * int[] total = {0};
         * tuple.forEach(c -> total[0] += c);
         * int sum = total[0];                  // 621
         *
         * // Duplicate chars: 'Z'=90, 9 * 90 = 810
         * CharTuple.CharTuple9 dup = CharTuple.of('Z', 'Z', 'Z', 'Z', 'Z', 'Z', 'Z', 'Z', 'Z');
         * int[] dupSum = {0};
         * dup.forEach(c -> dupSum[0] += c);
         * int dupTotal = dupSum[0];            // 810
         *
         * // Edge: null action throws IllegalArgumentException
         * // tuple.forEach(null);                 // throws IllegalArgumentException
         * }</pre>
         *
         * @param <E> the type of exception that may be thrown by the action
         * @param action the action to be performed for each element, must not be {@code null}
         * @throws IllegalArgumentException if {@code action} is {@code null}
         * @throws E if the action throws an exception
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.CharConsumer<E> action) throws E {
            N.checkArgNotNull(action, cs.action);

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
         * // Basic: equal tuples produce equal hashCodes
         * CharTuple.CharTuple9 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * CharTuple.CharTuple9 t2 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * int h1 = t1.hashCode();              // returns -1113192379
         * int h2 = t2.hashCode();              // returns -1113192379
         *
         * // Basic: tuple equals -> hashCode equals
         * assert t1.hashCode() == t2.hashCode(); // true
         *
         * // Edge: different element order produces different hashCode
         * CharTuple.CharTuple9 t3 = CharTuple.of('I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A');
         * int h3 = t3.hashCode();              // returns -1508756667
         *
         * // Edge: all same elements - hashCode is well-defined
         * CharTuple.CharTuple9 t4 = CharTuple.of('A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A');
         * assert CharTuple.of('A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A').hashCode() == t4.hashCode(); // true
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
         * CharTuple.CharTuple9 t1 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * CharTuple.CharTuple9 t2 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * boolean eq = t1.equals(t2);          // true
         *
         * // Different ninth element
         * CharTuple.CharTuple9 t3 = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J');
         * boolean neq = t1.equals(t3);         // false
         *
         * // Different order is not equal
         * CharTuple.CharTuple9 t4 = CharTuple.of('I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A');
         * boolean orderMatters = t1.equals(t4); // false
         *
         * // null is never equal
         * boolean nullEq = t1.equals(null);    // false
         * }</pre>
         *
         * @param obj the object to be compared for equality with this tuple
         * @return {@code true} if the specified object is a CharTuple.CharTuple9 with the same elements in the same order, {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final CharTuple9 other)) {
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
         * // Basic: standard ASCII chars
         * CharTuple.CharTuple9 t = CharTuple.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I');
         * String s = t.toString();             // returns "(A, B, C, D, E, F, G, H, I)"
         *
         * // Basic: lowercase chars
         * CharTuple.CharTuple9 t2 = CharTuple.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i');
         * String s2 = t2.toString();           // returns "(a, b, c, d, e, f, g, h, i)"
         *
         * // Edge: reversed order
         * CharTuple.CharTuple9 t3 = CharTuple.of('I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A');
         * String s3 = t3.toString();           // returns "(I, H, G, F, E, D, C, B, A)"
         *
         * // Edge: digit chars
         * CharTuple.CharTuple9 t4 = CharTuple.of('1', '2', '3', '4', '5', '6', '7', '8', '9');
         * String s4 = t4.toString();           // returns "(1, 2, 3, 4, 5, 6, 7, 8, 9)"
         * }</pre>
         *
         * @return a string representation in the format "(element1, element2, ..., element9)"
         */
        @Override
        public String toString() {
            return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ")";
        }

        /**
         * Returns the internal array of char elements.
         * The array is lazily initialized on first access.
         * <p><b>&#9888;&#65039; Warning:</b> The returned array is the internal representation of this tuple.
         * Modifying the returned array will compromise the immutability of this tuple.
         * Use {@link #toArray()} instead if you need an array that can be safely modified.
         * </p>
         *
         * @return a char array containing all elements in order
         */
        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
