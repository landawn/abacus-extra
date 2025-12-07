/*
 * Copyright (c) 2019, Haiyang Li.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.landawn.abacus.util;

import com.landawn.abacus.annotation.SuppressFBWarnings;

/**
 * Utility class containing point classes for two-dimensional coordinate systems.
 * This class provides immutable point implementations with value storage for various coordinate
 * types and use cases in geometric computations, matrix operations, and spatial data structures.
 *
 * <p>The Points class contains the {@code xy} nested static class that provides two-dimensional coordinate
 * point implementations with associated values.</p>
 *
 * <p>The {@code xy} class provides point implementations for different primitive types
 * (byte, int, long, double) to optimize memory usage and performance for specific use cases.
 * Each point stores x and y coordinates along with an associated value (v) which can be of
 * various types including primitives and generic objects.</p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Create a two-dimensional integer point with value
 * Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(10, 20, 100);
 * int x = point.x;  // 10
 * int y = point.y;  // 20
 * int value = point.v;  // 100
 * }</pre>
 */
@com.landawn.abacus.annotation.Immutable
public final class Points {

    private Points() {
        // singleton.
    }

    /**
     * The Class xy.
     */
    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
    @com.landawn.abacus.annotation.Immutable
    public static final class xy { // NOSONAR

        /**
         * Instantiates a new xy.
         */
        private xy() {
            // singleton.
        }

        /**
         * Represents an immutable two-dimensional point with byte coordinates and a byte value.
         * This class is optimized for memory-constrained scenarios where coordinates
         * and values fit within the byte range (-128 to 127).
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record ByteBytePoint(byte x, byte y, byte v) implements Immutable {

            /**
             * Creates a new ByteBytePoint with the specified x, y coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteBytePoint point = ByteBytePoint.of((byte)10, (byte)20, (byte)100);
             * byte x = point.x;  // 10
             * byte y = point.y;  // 20
             * byte value = point.v;  // 100
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new ByteBytePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static ByteBytePoint of(final byte x, final byte y, final byte v) {
                return new ByteBytePoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code ((x * 31) + y) * 31 + v}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two ByteBytePoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             * Returns a string representation of this ByteBytePoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteBytePoint point = ByteBytePoint.of((byte)10, (byte)20, (byte)100);
             * System.out.println(point.toString());   // prints: [10, 20, 100]
             * System.out.println(point);              // prints: [10, 20, 100]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with byte coordinates and an integer value.
         * This class is useful when coordinates are constrained to byte range (-128 to 127)
         * but the associated value requires the full integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record ByteIntPoint(byte x, byte y, int v) implements Immutable {

            /**
             * Creates a new ByteIntPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteIntPoint point = ByteIntPoint.of((byte)10, (byte)20, 100);
             * byte x = point.x;  // 10
             * byte y = point.y;  // 20
             * int value = point.v;  // 100
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new ByteIntPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static ByteIntPoint of(final byte x, final byte y, final int v) {
                return new ByteIntPoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code ((x * 31) + y) * 31 + v}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two ByteIntPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             * Returns a string representation of this ByteIntPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteIntPoint point = ByteIntPoint.of((byte)10, (byte)20, 100);
             * System.out.println(point.toString());   // prints: [10, 20, 100]
             * System.out.println(point);              // prints: [10, 20, 100]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with byte coordinates and a long value.
         * This class is useful when coordinates are constrained to byte range (-128 to 127)
         * but the associated value requires the full long integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record ByteLongPoint(byte x, byte y, long v) implements Immutable {

            /**
             * Creates a new ByteLongPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteLongPoint point = ByteLongPoint.of((byte)10, (byte)20, 100L);
             * byte x = point.x;  // 10
             * byte y = point.y;  // 20
             * long value = point.v;  // 100L
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new ByteLongPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static ByteLongPoint of(final byte x, final byte y, final long v) {
                return new ByteLongPoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two ByteLongPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Returns a string representation of this ByteLongPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteLongPoint point = ByteLongPoint.of((byte)10, (byte)20, 100L);
             * System.out.println(point.toString());   // prints: [10, 20, 100]
             * System.out.println(point);              // prints: [10, 20, 100]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with byte coordinates and a double-precision floating-point value.
         * This class is useful when coordinates are constrained to byte range (-128 to 127)
         * but the associated value requires floating-point precision.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record ByteDoublePoint(byte x, byte y, double v) implements Immutable {

            /**
             * Creates a new ByteDoublePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteDoublePoint point = ByteDoublePoint.of((byte)10, (byte)20, 100.5);
             * byte x = point.x;  // 10
             * byte y = point.y;  // 20
             * double value = point.v;  // 100.5
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new ByteDoublePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static ByteDoublePoint of(final byte x, final byte y, final double v) {
                return new ByteDoublePoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two ByteDoublePoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Compares this ByteDoublePoint with the specified object for equality.
             *
             * <p>Returns {@code true} if and only if the specified object is also a ByteDoublePoint
             * and both points have the same coordinates and value. Two ByteDoublePoint instances are
             * considered equal if their x, y coordinates and v value are all equal.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteDoublePoint point1 = ByteDoublePoint.of((byte)10, (byte)20, 100.5);
             * ByteDoublePoint point2 = ByteDoublePoint.of((byte)10, (byte)20, 100.5);
             * ByteDoublePoint point3 = ByteDoublePoint.of((byte)10, (byte)20, 200.5);
             *
             * System.out.println(point1.equals(point2));          // prints: true
             * System.out.println(point1.equals(point3));          // prints: false
             * System.out.println(point1.equals("not a point"));   // prints: false
             * }</pre>
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final ByteDoublePoint other) {
                    return x == other.x && y == other.y && N.equals(v, other.v);
                }

                return false;
            }

            /**
             * Returns a string representation of this ByteDoublePoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteDoublePoint point = ByteDoublePoint.of((byte)10, (byte)20, 100.5);
             * System.out.println(point.toString());   // prints: [10, 20, 100.5]
             * System.out.println(point);              // prints: [10, 20, 100.5]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with byte coordinates and a generic object value.
         * This class is useful when coordinates are constrained to byte range (-128 to 127)
         * but the associated value can be any object type.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record ByteObjPoint<T>(byte x, byte y, T v) implements Immutable {

            /**
             * Creates a new ByteObjPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteObjPoint<String> point = ByteObjPoint.of((byte)5, (byte)10, "marker");
             * byte x = point.x;  // 5
             * byte y = point.y;  // 10
             * String value = point.v;  // "marker"
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new ByteObjPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static <T> ByteObjPoint<T> of(final byte x, final byte y, final T v) {
                return new ByteObjPoint<>(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code ((x * 31) + y) * 31 + N.hashCode(v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The value's hash code is computed using
             * {@link N#hashCode(Object)}, which handles null values safely.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two ByteObjPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + N.hashCode(v);
            }

            /**
             * Compares this ByteObjPoint with the specified object for equality.
             *
             * <p>Returns {@code true} if and only if the specified object is also a ByteObjPoint
             * and both points have the same coordinates and value. Two ByteObjPoint instances are
             * considered equal if their x, y coordinates and v value are all equal.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteObjPoint<String> point1 = ByteObjPoint.of((byte)5, (byte)10, "marker");
             * ByteObjPoint<String> point2 = ByteObjPoint.of((byte)5, (byte)10, "marker");
             * ByteObjPoint<String> point3 = ByteObjPoint.of((byte)5, (byte)10, "different");
             *
             * System.out.println(point1.equals(point2));          // prints: true
             * System.out.println(point1.equals(point3));          // prints: false
             * System.out.println(point1.equals("not a point"));   // prints: false
             * }</pre>
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof ByteObjPoint<?> other) {
                    return x == other.x && y == other.y && N.equals(v, other.v);
                }

                return false;
            }

            /**
             * Returns a string representation of this ByteObjPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteObjPoint<String> point = ByteObjPoint.of((byte)5, (byte)10, "marker");
             * System.out.println(point.toString());   // prints: [5, 10, marker]
             * System.out.println(point);              // prints: [5, 10, marker]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with integer coordinates and a byte value.
         * This class is useful when coordinates require the full integer range
         * but the associated value is constrained to byte range (-128 to 127).
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record IntBytePoint(int x, int y, byte v) implements Immutable {

            /**
             * Creates a new IntBytePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntBytePoint point = IntBytePoint.of(100, 200, (byte)50);
             * int x = point.x;  // 100
             * int y = point.y;  // 200
             * byte value = point.v;  // 50
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new IntBytePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static IntBytePoint of(final int x, final int y, final byte v) {
                return new IntBytePoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code ((x * 31) + y) * 31 + v}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two IntBytePoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             * Returns a string representation of this IntBytePoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntBytePoint point = IntBytePoint.of(100, 200, (byte)50);
             * System.out.println(point.toString());   // prints: [100, 200, 50]
             * System.out.println(point);              // prints: [100, 200, 50]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with integer coordinates and an integer value.
         * This class is the most commonly used point type for general-purpose integer-based
         * coordinate systems and grid operations.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record IntIntPoint(int x, int y, int v) implements Immutable {

            /**
             * Creates a new IntIntPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntIntPoint point = IntIntPoint.of(100, 200, 300);
             * int x = point.x;  // 100
             * int y = point.y;  // 200
             * int value = point.v;  // 300
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new IntIntPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static IntIntPoint of(final int x, final int y, final int v) {
                return new IntIntPoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code ((x * 31) + y) * 31 + v}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two IntIntPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             * Returns a string representation of this IntIntPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntIntPoint point = IntIntPoint.of(100, 200, 300);
             * System.out.println(point.toString());   // prints: [100, 200, 300]
             * System.out.println(point);              // prints: [100, 200, 300]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with integer coordinates and a long value.
         * This class is useful when coordinates fit within the integer range
         * but the associated value requires the full long integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record IntLongPoint(int x, int y, long v) implements Immutable {

            /**
             * Creates a new IntLongPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntLongPoint point = IntLongPoint.of(100, 200, 1000000L);
             * int x = point.x;  // 100
             * int y = point.y;  // 200
             * long value = point.v;  // 1000000L
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new IntLongPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static IntLongPoint of(final int x, final int y, final long v) {
                return new IntLongPoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two IntLongPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Returns a string representation of this IntLongPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntLongPoint point = IntLongPoint.of(100, 200, 1000000L);
             * System.out.println(point.toString());   // prints: [100, 200, 1000000]
             * System.out.println(point);              // prints: [100, 200, 1000000]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with integer coordinates and a double-precision floating-point value.
         * This class is useful when coordinates fit within the integer range
         * but the associated value requires floating-point precision.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record IntDoublePoint(int x, int y, double v) implements Immutable {

            /**
             * Creates a new IntDoublePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntDoublePoint point = IntDoublePoint.of(100, 200, 3.14159);
             * int x = point.x;  // 100
             * int y = point.y;  // 200
             * double value = point.v;  // 3.14159
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new IntDoublePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static IntDoublePoint of(final int x, final int y, final double v) {
                return new IntDoublePoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two IntDoublePoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Compares this IntDoublePoint with the specified object for equality.
             *
             * <p>Returns {@code true} if and only if the specified object is also an IntDoublePoint
             * and both points have the same coordinates and value. Two IntDoublePoint instances are
             * considered equal if their x, y coordinates and v value are all equal.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntDoublePoint point1 = IntDoublePoint.of(100, 200, 3.14159);
             * IntDoublePoint point2 = IntDoublePoint.of(100, 200, 3.14159);
             * IntDoublePoint point3 = IntDoublePoint.of(100, 200, 2.71828);
             *
             * System.out.println(point1.equals(point2));          // prints: true
             * System.out.println(point1.equals(point3));          // prints: false
             * System.out.println(point1.equals("not a point"));   // prints: false
             * }</pre>
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final IntDoublePoint other) {
                    return x == other.x && y == other.y && N.equals(v, other.v);
                }

                return false;
            }

            /**
             * Returns a string representation of this IntDoublePoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntDoublePoint point = IntDoublePoint.of(100, 200, 3.14159);
             * System.out.println(point.toString());   // prints: [100, 200, 3.14159]
             * System.out.println(point);              // prints: [100, 200, 3.14159]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with integer coordinates and a generic object value.
         * This class is useful when coordinates fit within the integer range
         * but the associated value can be any object type.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record IntObjPoint<T>(int x, int y, T v) implements Immutable {

            /**
             * Creates a new IntObjPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntObjPoint<String> point = IntObjPoint.of(100, 200, "marker");
             * int x = point.x;  // 100
             * int y = point.y;  // 200
             * String value = point.v;  // "marker"
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new IntObjPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static <T> IntObjPoint<T> of(final int x, final int y, final T v) {
                return new IntObjPoint<>(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code ((x * 31) + y) * 31 + N.hashCode(v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The value's hash code is computed using
             * {@link N#hashCode(Object)}, which handles null values safely.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two IntObjPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + N.hashCode(v);
            }

            /**
             * Compares this IntObjPoint with the specified object for equality.
             *
             * <p>Returns {@code true} if and only if the specified object is also an IntObjPoint
             * and both points have the same coordinates and value. Two IntObjPoint instances are
             * considered equal if their x, y coordinates and v value are all equal.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntObjPoint<String> point1 = IntObjPoint.of(100, 200, "marker");
             * IntObjPoint<String> point2 = IntObjPoint.of(100, 200, "marker");
             * IntObjPoint<String> point3 = IntObjPoint.of(100, 200, "different");
             *
             * System.out.println(point1.equals(point2));          // prints: true
             * System.out.println(point1.equals(point3));          // prints: false
             * System.out.println(point1.equals("not a point"));   // prints: false
             * }</pre>
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof IntObjPoint<?> other) {
                    return x == other.x && y == other.y && N.equals(v, other.v);
                }

                return false;
            }

            /**
             * Returns a string representation of this IntObjPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntObjPoint<String> point = IntObjPoint.of(100, 200, "marker");
             * System.out.println(point.toString());   // prints: [100, 200, marker]
             * System.out.println(point);              // prints: [100, 200, marker]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with long coordinates and a byte value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value is constrained to byte range (-128 to 127).
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record LongBytePoint(long x, long y, byte v) implements Immutable {

            /**
             * Creates a new LongBytePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongBytePoint point = LongBytePoint.of(100L, 200L, (byte)50);
             * long x = point.x;  // 100L
             * long y = point.y;  // 200L
             * byte value = point.v;  // 50
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new LongBytePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static LongBytePoint of(final long x, final long y, final byte v) {
                return new LongBytePoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two LongBytePoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Returns a string representation of this LongBytePoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongBytePoint point = LongBytePoint.of(100L, 200L, (byte)50);
             * System.out.println(point.toString());   // prints: [100, 200, 50]
             * System.out.println(point);              // prints: [100, 200, 50]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with long coordinates and an integer value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value fits within the integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record LongIntPoint(long x, long y, int v) implements Immutable {

            /**
             * Creates a new LongIntPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongIntPoint point = LongIntPoint.of(100L, 200L, 300);
             * long x = point.x;  // 100L
             * long y = point.y;  // 200L
             * int value = point.v;  // 300
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new LongIntPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static LongIntPoint of(final long x, final long y, final int v) {
                return new LongIntPoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two LongIntPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Returns a string representation of this LongIntPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongIntPoint point = LongIntPoint.of(100L, 200L, 300);
             * System.out.println(point.toString());   // prints: [100, 200, 300]
             * System.out.println(point);              // prints: [100, 200, 300]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with long coordinates and a long value.
         * This class provides full long integer range for both coordinates and associated value.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record LongLongPoint(long x, long y, long v) implements Immutable {

            /**
             * Creates a new LongLongPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongLongPoint point = LongLongPoint.of(100L, 200L, 300L);
             * long x = point.x;  // 100L
             * long y = point.y;  // 200L
             * long value = point.v;  // 300L
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new LongLongPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static LongLongPoint of(final long x, final long y, final long v) {
                return new LongLongPoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two LongLongPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Returns a string representation of this LongLongPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongLongPoint point = LongLongPoint.of(100L, 200L, 300L);
             * System.out.println(point.toString());   // prints: [100, 200, 300]
             * System.out.println(point);              // prints: [100, 200, 300]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with long coordinates and a double-precision floating-point value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value requires floating-point precision.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record LongDoublePoint(long x, long y, double v) implements Immutable {

            /**
             * Creates a new LongDoublePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongDoublePoint point = LongDoublePoint.of(100L, 200L, 3.14159);
             * long x = point.x;  // 100L
             * long y = point.y;  // 200L
             * double value = point.v;  // 3.14159
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new LongDoublePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static LongDoublePoint of(final long x, final long y, final double v) {
                return new LongDoublePoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two LongDoublePoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Compares this LongDoublePoint with the specified object for equality.
             *
             * <p>Returns {@code true} if and only if the specified object is also a LongDoublePoint
             * and both points have the same coordinates and value. Two LongDoublePoint instances are
             * considered equal if their x, y coordinates and v value are all equal.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongDoublePoint point1 = LongDoublePoint.of(100L, 200L, 3.14159);
             * LongDoublePoint point2 = LongDoublePoint.of(100L, 200L, 3.14159);
             * LongDoublePoint point3 = LongDoublePoint.of(100L, 200L, 2.71828);
             *
             * System.out.println(point1.equals(point2));          // prints: true
             * System.out.println(point1.equals(point3));          // prints: false
             * System.out.println(point1.equals("not a point"));   // prints: false
             * }</pre>
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final LongDoublePoint other) {
                    return x == other.x && y == other.y && N.equals(v, other.v);
                }

                return false;
            }

            /**
             * Returns a string representation of this LongDoublePoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongDoublePoint point = LongDoublePoint.of(100L, 200L, 3.14159);
             * System.out.println(point.toString());   // prints: [100, 200, 3.14159]
             * System.out.println(point);              // prints: [100, 200, 3.14159]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with long coordinates and a generic object value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value can be any object type.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record LongObjPoint<T>(long x, long y, T v) implements Immutable {

            /**
             * Creates a new LongObjPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongObjPoint<String> point = LongObjPoint.of(100L, 200L, "marker");
             * long x = point.x;  // 100L
             * long y = point.y;  // 200L
             * String value = point.v;  // "marker"
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new LongObjPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static <T> LongObjPoint<T> of(final long x, final long y, final T v) {
                return new LongObjPoint<>(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + N.hashCode(v))}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The value's hash code is computed using
             * {@link N#hashCode(Object)}, which handles null values safely. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two LongObjPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + N.hashCode(v));
            }

            /**
             * Compares this LongObjPoint with the specified object for equality.
             *
             * <p>Returns {@code true} if and only if the specified object is also a LongObjPoint
             * and both points have the same coordinates and value. Two LongObjPoint instances are
             * considered equal if their x, y coordinates and v value are all equal.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongObjPoint<String> point1 = LongObjPoint.of(100L, 200L, "marker");
             * LongObjPoint<String> point2 = LongObjPoint.of(100L, 200L, "marker");
             * LongObjPoint<String> point3 = LongObjPoint.of(100L, 200L, "different");
             *
             * System.out.println(point1.equals(point2));          // prints: true
             * System.out.println(point1.equals(point3));          // prints: false
             * System.out.println(point1.equals("not a point"));   // prints: false
             * }</pre>
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof LongObjPoint<?> other) {
                    return x == other.x && y == other.y && N.equals(v, other.v);
                }

                return false;
            }

            /**
             * Returns a string representation of this LongObjPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongObjPoint<String> point = LongObjPoint.of(100L, 200L, "marker");
             * System.out.println(point.toString());   // prints: [100, 200, marker]
             * System.out.println(point);              // prints: [100, 200, marker]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with double-precision floating-point coordinates and a byte value.
         * This class is useful when coordinates require floating-point precision
         * but the associated value is constrained to byte range (-128 to 127).
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record DoubleBytePoint(double x, double y, byte v) implements Immutable {

            /**
             * Creates a new DoubleBytePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleBytePoint point = DoubleBytePoint.of(10.5, 20.3, (byte)50);
             * double x = point.x;  // 10.5
             * double y = point.y;  // 20.3
             * byte value = point.v;  // 50
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new DoubleBytePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static DoubleBytePoint of(final double x, final double y, final byte v) {
                return new DoubleBytePoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two DoubleBytePoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Compares this DoubleBytePoint with the specified object for equality.
             *
             * <p>Returns {@code true} if and only if the specified object is also a DoubleBytePoint
             * and both points have the same coordinates and value. Two DoubleBytePoint instances are
             * considered equal if their x, y coordinates and v value are all equal.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleBytePoint point1 = DoubleBytePoint.of(10.5, 20.3, (byte)50);
             * DoubleBytePoint point2 = DoubleBytePoint.of(10.5, 20.3, (byte)50);
             * DoubleBytePoint point3 = DoubleBytePoint.of(10.5, 20.3, (byte)60);
             *
             * System.out.println(point1.equals(point2));          // prints: true
             * System.out.println(point1.equals(point3));          // prints: false
             * System.out.println(point1.equals("not a point"));   // prints: false
             * }</pre>
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final DoubleBytePoint other) {
                    return N.equals(x, other.x) && N.equals(y, other.y) && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this DoubleBytePoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleBytePoint point = DoubleBytePoint.of(10.5, 20.3, (byte)50);
             * System.out.println(point.toString());   // prints: [10.5, 20.3, 50]
             * System.out.println(point);              // prints: [10.5, 20.3, 50]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with double-precision floating-point coordinates and an integer value.
         * This class is useful when coordinates require floating-point precision
         * but the associated value fits within the integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record DoubleIntPoint(double x, double y, int v) implements Immutable {

            /**
             * Creates a new DoubleIntPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleIntPoint point = DoubleIntPoint.of(10.5, 20.3, 100);
             * double x = point.x;  // 10.5
             * double y = point.y;  // 20.3
             * int value = point.v;  // 100
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new DoubleIntPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static DoubleIntPoint of(final double x, final double y, final int v) {
                return new DoubleIntPoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two DoubleIntPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Compares this DoubleIntPoint with the specified object for equality.
             *
             * <p>Returns {@code true} if and only if the specified object is also a DoubleIntPoint
             * and both points have the same coordinates and value. Two DoubleIntPoint instances are
             * considered equal if their x, y coordinates and v value are all equal.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleIntPoint point1 = DoubleIntPoint.of(10.5, 20.3, 100);
             * DoubleIntPoint point2 = DoubleIntPoint.of(10.5, 20.3, 100);
             * DoubleIntPoint point3 = DoubleIntPoint.of(10.5, 20.3, 200);
             *
             * System.out.println(point1.equals(point2));          // prints: true
             * System.out.println(point1.equals(point3));          // prints: false
             * System.out.println(point1.equals("not a point"));   // prints: false
             * }</pre>
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final DoubleIntPoint other) {
                    return N.equals(x, other.x) && N.equals(y, other.y) && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this DoubleIntPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleIntPoint point = DoubleIntPoint.of(10.5, 20.3, 100);
             * System.out.println(point.toString());   // prints: [10.5, 20.3, 100]
             * System.out.println(point);              // prints: [10.5, 20.3, 100]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with double-precision floating-point coordinates and a long value.
         * This class is useful when coordinates require floating-point precision
         * but the associated value requires the full long integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record DoubleLongPoint(double x, double y, long v) implements Immutable {

            /**
             * Creates a new DoubleLongPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleLongPoint point = DoubleLongPoint.of(10.5, 20.3, 1000000L);
             * double x = point.x;  // 10.5
             * double y = point.y;  // 20.3
             * long value = point.v;  // 1000000L
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new DoubleLongPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static DoubleLongPoint of(final double x, final double y, final long v) {
                return new DoubleLongPoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two DoubleLongPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Compares this DoubleLongPoint with the specified object for equality.
             *
             * <p>Returns {@code true} if and only if the specified object is also a DoubleLongPoint
             * and both points have the same coordinates and value. Two DoubleLongPoint instances are
             * considered equal if their x, y coordinates and v value are all equal.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleLongPoint point1 = DoubleLongPoint.of(10.5, 20.3, 1000000L);
             * DoubleLongPoint point2 = DoubleLongPoint.of(10.5, 20.3, 1000000L);
             * DoubleLongPoint point3 = DoubleLongPoint.of(10.5, 20.3, 2000000L);
             *
             * System.out.println(point1.equals(point2));          // prints: true
             * System.out.println(point1.equals(point3));          // prints: false
             * System.out.println(point1.equals("not a point"));   // prints: false
             * }</pre>
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final DoubleLongPoint other) {
                    return N.equals(x, other.x) && N.equals(y, other.y) && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this DoubleLongPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleLongPoint point = DoubleLongPoint.of(10.5, 20.3, 1000000L);
             * System.out.println(point.toString());   // prints: [10.5, 20.3, 1000000]
             * System.out.println(point);              // prints: [10.5, 20.3, 1000000]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with double-precision floating-point coordinates and value.
         * This class provides full double-precision floating-point support for both coordinates and the associated value.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record DoubleDoublePoint(double x, double y, double v) implements Immutable {

            /**
             * Creates a new DoubleDoublePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleDoublePoint point = DoubleDoublePoint.of(10.5, 20.3, 3.14159);
             * double x = point.x;  // 10.5
             * double y = point.y;  // 20.3
             * double value = point.v;  // 3.14159
             * }</pre>
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new DoubleDoublePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static DoubleDoublePoint of(final double x, final double y, final double v) {
                return new DoubleDoublePoint(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + v)}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two DoubleDoublePoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Compares this DoubleDoublePoint with the specified object for equality.
             *
             * <p>Returns {@code true} if and only if the specified object is also a DoubleDoublePoint
             * and both points have the same coordinates and value. Two DoubleDoublePoint instances are
             * considered equal if their x, y coordinates and v value are all equal.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleDoublePoint point1 = DoubleDoublePoint.of(10.5, 20.3, 3.14159);
             * DoubleDoublePoint point2 = DoubleDoublePoint.of(10.5, 20.3, 3.14159);
             * DoubleDoublePoint point3 = DoubleDoublePoint.of(10.5, 20.3, 2.71828);
             *
             * System.out.println(point1.equals(point2));          // prints: true
             * System.out.println(point1.equals(point3));          // prints: false
             * System.out.println(point1.equals("not a point"));   // prints: false
             * }</pre>
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final DoubleDoublePoint other) {
                    return N.equals(x, other.x) && N.equals(y, other.y) && N.equals(v, other.v);
                }

                return false;
            }

            /**
             * Returns a string representation of this DoubleDoublePoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleDoublePoint point = DoubleDoublePoint.of(10.5, 20.3, 3.14159);
             * System.out.println(point.toString());   // prints: [10.5, 20.3, 3.14159]
             * System.out.println(point);              // prints: [10.5, 20.3, 3.14159]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with double-precision floating-point coordinates and a generic object value.
         * This class is useful when coordinates require floating-point precision
         * but the associated value can be any object type.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param v the value associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public record DoubleObjPoint<T>(double x, double y, T v) implements Immutable {

            /**
             * Creates a new DoubleObjPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleObjPoint<String> point = DoubleObjPoint.of(10.5, 20.3, "marker");
             * double x = point.x;  // 10.5
             * double y = point.y;  // 20.3
             * String value = point.v;  // "marker"
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new DoubleObjPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static <T> DoubleObjPoint<T> of(final double x, final double y, final T v) {
                return new DoubleObjPoint<>(x, y, v);
            }

            /**
             * Returns a hash code for this point based on its coordinates and value.
             *
             * <p>The hash code is computed using a polynomial rolling hash algorithm with a prime multiplier (31):
             * {@code (int)(((x * 31) + y) * 31 + N.hashCode(v))}. This algorithm efficiently combines all three fields to produce
             * a well-distributed hash code value while minimizing collisions. The value's hash code is computed using
             * {@link N#hashCode(Object)}, which handles null values safely. The result is cast to int to fit the
             * return type.</p>
             *
             * <p>This method satisfies the general contract of {@link Object#hashCode()}: if two DoubleObjPoint
             * instances are equal according to {@link #equals(Object)}, then calling this method on each will
             * produce the same integer result.</p>
             *
             * @return a hash code value for this point
             * @see #equals(Object)
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + N.hashCode(v));
            }

            /**
             * Compares this DoubleObjPoint with the specified object for equality.
             *
             * <p>Returns {@code true} if and only if the specified object is also a DoubleObjPoint
             * and both points have the same coordinates and value. Two DoubleObjPoint instances are
             * considered equal if their x, y coordinates and v value are all equal.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleObjPoint<String> point1 = DoubleObjPoint.of(10.5, 20.3, "marker");
             * DoubleObjPoint<String> point2 = DoubleObjPoint.of(10.5, 20.3, "marker");
             * DoubleObjPoint<String> point3 = DoubleObjPoint.of(10.5, 20.3, "different");
             *
             * System.out.println(point1.equals(point2));          // prints: true
             * System.out.println(point1.equals(point3));          // prints: false
             * System.out.println(point1.equals("not a point"));   // prints: false
             * }</pre>
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof DoubleObjPoint<?> other) {
                    return N.equals(x, other.x) && N.equals(y, other.y) && N.equals(v, other.v);
                }

                return false;
            }

            /**
             * Returns a string representation of this DoubleObjPoint in the format "[x, y, v]".
             *
             * <p>The string representation consists of the x-coordinate, y-coordinate, and value
             * enclosed in square brackets and separated by commas and spaces. This format provides
             * a human-readable representation of the point's coordinates and value.</p>
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleObjPoint<String> point = DoubleObjPoint.of(10.5, 20.3, "marker");
             * System.out.println(point.toString());   // prints: [10.5, 20.3, marker]
             * System.out.println(point);              // prints: [10.5, 20.3, marker]
             * }</pre>
             *
             * @return a string representation of this point in the format "[x, y, v]"
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
            }
        }
    }
}
