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

/**
 * Namespace for small record-based point/value carriers used by geometry and coordinate helpers.
 *
 * <p>{@link D2} groups two-dimensional variants (coordinates {@code x}, {@code y}) and {@link D3}
 * groups three-dimensional variants (coordinates {@code x}, {@code y}, {@code z}). Every record
 * also carries a {@code value} payload, whose type is reflected in the record name (e.g.,
 * {@code IntDoublePoint} has {@code int} coordinates and a {@code double} value, while
 * {@code DoubleObjPoint<T>} has {@code double} coordinates and a generic object value).</p>
 *
 * <p>Primitive-valued records are fully immutable and thread-safe. Object-valued variants
 * ({@code *ObjPoint}) are shallowly immutable: the coordinate fields are fixed but the referenced
 * value is stored as supplied without defensive copying, so thread-safety depends on the payload.
 * Each nested record provides record-based equality and a static {@code of(...)} factory for
 * concise construction.</p>
 *
 * <p>This class cannot be instantiated.</p>
 *
 * @see D2
 * @see D3
 */
public final class Points {

    private Points() {
        // utility class.
    }

    /**
     * Namespace for two-dimensional point/value records.
     *
     * <p>The nested records cover common coordinate widths ({@code byte}, {@code int}, {@code long},
     * and {@code double}) together with primitive or object payload types so callers can choose a
     * compact carrier without defining a custom type. Object-valued variants retain their payload
     * references without defensive copying.</p>
     *
     * @see D3
     */
    public static final class D2 {

        private D2() {
            // utility class.
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
         * @param value the value associated with this point
         */
        public record ByteBytePoint(byte x, byte y, byte value) {

            /**
             * Creates a new ByteBytePoint with the specified coordinates and value.
             * This factory method provides a convenient way to construct two-dimensional points
             * with byte-range coordinates and values, optimized for memory efficiency.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical positive coordinates and value
             * Points.D2.ByteBytePoint p1 = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 5);
             * p1.x()     // returns (byte) 10
             * p1.y()     // returns (byte) 20
             * p1.value() // returns (byte) 5
             *
             * // Basic: zero coordinates and value
             * Points.D2.ByteBytePoint p2 = Points.D2.ByteBytePoint.of((byte) 0, (byte) 0, (byte) 0);
             * p2.x()     // returns (byte) 0
             * p2.y()     // returns (byte) 0
             * p2.value() // returns (byte) 0
             *
             * // Edge: minimum boundary values (Byte.MIN_VALUE = -128)
             * Points.D2.ByteBytePoint p3 = Points.D2.ByteBytePoint.of((byte) -128, (byte) -128, (byte) -128);
             * p3.x()     // returns (byte) -128
             * p3.y()     // returns (byte) -128
             * p3.value() // returns (byte) -128
             *
             * // Edge: maximum boundary values (Byte.MAX_VALUE = 127)
             * Points.D2.ByteBytePoint p4 = Points.D2.ByteBytePoint.of((byte) 127, (byte) 127, (byte) 127);
             * p4.x()     // returns (byte) 127
             * p4.y()     // returns (byte) 127
             * p4.value() // returns (byte) 127
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the byte value associated with this point
             * @return a new ByteBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteBytePoint of(final byte x, final byte y, final byte value) {
                return new ByteBytePoint(x, y, value);
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
         * @param value the value associated with this point
         */
        public record ByteIntPoint(byte x, byte y, int value) {

            /**
             * Creates a new ByteIntPoint with the specified coordinates and value.
             * This factory method is ideal when working with small-range coordinates but requiring
             * a full integer range for the associated value, such as counting or indexing operations.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical byte coordinates with a large integer value
             * Points.D2.ByteIntPoint p1 = Points.D2.ByteIntPoint.of((byte) 5, (byte) 10, 1000);
             * p1.x()     // returns (byte) 5
             * p1.y()     // returns (byte) 10
             * p1.value() // returns 1000
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.ByteIntPoint p2 = Points.D2.ByteIntPoint.of((byte) 0, (byte) 0, 0);
             * p2.x()     // returns (byte) 0
             * p2.y()     // returns (byte) 0
             * p2.value() // returns 0
             *
             * // Edge: Byte.MIN_VALUE x, Byte.MAX_VALUE y, Integer.MIN_VALUE
             * Points.D2.ByteIntPoint p3 = Points.D2.ByteIntPoint.of((byte) -128, (byte) 127, Integer.MIN_VALUE);
             * p3.x()     // returns (byte) -128
             * p3.y()     // returns (byte) 127
             * p3.value() // returns Integer.MIN_VALUE (-2147483648)
             *
             * // Edge: negative byte coords with Integer.MAX_VALUE
             * Points.D2.ByteIntPoint p4 = Points.D2.ByteIntPoint.of((byte) -1, (byte) -1, Integer.MAX_VALUE);
             * p4.x()     // returns (byte) -1
             * p4.y()     // returns (byte) -1
             * p4.value() // returns Integer.MAX_VALUE (2147483647)
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the integer value associated with this point
             * @return a new ByteIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteIntPoint of(final byte x, final byte y, final int value) {
                return new ByteIntPoint(x, y, value);
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
         * @param value the value associated with this point
         */
        public record ByteLongPoint(byte x, byte y, long value) {

            /**
             * Creates a new ByteLongPoint with the specified coordinates and value.
             * This factory method is ideal when working with small-range coordinates but requiring
             * a full long integer range for the associated value, such as timestamps or large counts.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical byte coordinates with a large long value
             * Points.D2.ByteLongPoint p1 = Points.D2.ByteLongPoint.of((byte) 3, (byte) 7, 1_000_000_000L);
             * p1.x()     // returns (byte) 3
             * p1.y()     // returns (byte) 7
             * p1.value() // returns 1000000000L
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.ByteLongPoint p2 = Points.D2.ByteLongPoint.of((byte) 0, (byte) 0, 0L);
             * p2.x()     // returns (byte) 0
             * p2.y()     // returns (byte) 0
             * p2.value() // returns 0L
             *
             * // Edge: Byte.MIN_VALUE coords, Long.MIN_VALUE
             * Points.D2.ByteLongPoint p3 = Points.D2.ByteLongPoint.of((byte) -128, (byte) -128, Long.MIN_VALUE);
             * p3.x()     // returns (byte) -128
             * p3.y()     // returns (byte) -128
             * p3.value() // returns Long.MIN_VALUE (-9223372036854775808L)
             *
             * // Edge: Byte.MAX_VALUE coords, Long.MAX_VALUE
             * Points.D2.ByteLongPoint p4 = Points.D2.ByteLongPoint.of((byte) 127, (byte) 127, Long.MAX_VALUE);
             * p4.x()     // returns (byte) 127
             * p4.y()     // returns (byte) 127
             * p4.value() // returns Long.MAX_VALUE (9223372036854775807L)
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the long value associated with this point
             * @return a new ByteLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteLongPoint of(final byte x, final byte y, final long value) {
                return new ByteLongPoint(x, y, value);
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
         * @param value the value associated with this point
         */
        public record ByteDoublePoint(byte x, byte y, double value) {

            /**
             * Creates a new ByteDoublePoint with the specified coordinates and value.
             * This factory method is ideal when working with small-range coordinates but requiring
             * floating-point precision for the associated value, such as probabilities, weights, or measurements.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical byte coordinates with a floating-point value
             * Points.D2.ByteDoublePoint p1 = Points.D2.ByteDoublePoint.of((byte) 2, (byte) 4, 3.14159);
             * p1.x()     // returns (byte) 2
             * p1.y()     // returns (byte) 4
             * p1.value() // returns 3.14159
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.ByteDoublePoint p2 = Points.D2.ByteDoublePoint.of((byte) 0, (byte) 0, 0.0);
             * p2.x()     // returns (byte) 0
             * p2.y()     // returns (byte) 0
             * p2.value() // returns 0.0
             *
             * // Edge: Byte.MIN_VALUE and Byte.MAX_VALUE coords, Double.NaN value
             * Points.D2.ByteDoublePoint p3 = Points.D2.ByteDoublePoint.of((byte) -128, (byte) 127, Double.NaN);
             * p3.x()                   // returns (byte) -128
             * p3.y()                   // returns (byte) 127
             * Double.isNaN(p3.value()) // returns true
             *
             * // Edge: negative byte coords, Double.POSITIVE_INFINITY value
             * Points.D2.ByteDoublePoint p4 = Points.D2.ByteDoublePoint.of((byte) -1, (byte) -1, Double.POSITIVE_INFINITY);
             * p4.x()     // returns (byte) -1
             * p4.y()     // returns (byte) -1
             * p4.value() // returns Double.POSITIVE_INFINITY
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the double value associated with this point
             * @return a new ByteDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteDoublePoint of(final byte x, final byte y, final double value) {
                return new ByteDoublePoint(x, y, value);
            }
        }

        /**
         * Represents a two-dimensional point with byte coordinates and an object payload.
         * This record is a shallowly immutable carrier: the coordinates are fixed, while the
         * referenced value is stored as supplied and may itself be mutable.
         *
         * <p>Instances are thread-safe only when the referenced value is thread-safe or otherwise
         * not mutated concurrently.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param value the value associated with this point
         */
        public record ByteObjPoint<T>(byte x, byte y, T value) {

            /**
             * Creates a new ByteObjPoint with the specified coordinates and value reference.
             * The supplied value is stored as-is; this factory does not clone or otherwise protect
             * mutable payloads.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: byte coordinates with a String value
             * Points.D2.ByteObjPoint<String> p1 = Points.D2.ByteObjPoint.of((byte) 1, (byte) 2, "label");
             * p1.x()     // returns (byte) 1
             * p1.y()     // returns (byte) 2
             * p1.value() // returns "label"
             *
             * // Basic: byte coordinates with an Integer value
             * Points.D2.ByteObjPoint<Integer> p2 = Points.D2.ByteObjPoint.of((byte) 10, (byte) 20, 42);
             * p2.x()     // returns (byte) 10
             * p2.y()     // returns (byte) 20
             * p2.value() // returns 42
             *
             * // Edge: Byte.MIN_VALUE / Byte.MAX_VALUE coords, null value (records permit null components)
             * Points.D2.ByteObjPoint<String> p3 = Points.D2.ByteObjPoint.of((byte) -128, (byte) 127, null);
             * p3.x()     // returns (byte) -128
             * p3.y()     // returns (byte) 127
             * p3.value() // returns null
             *
             * // Edge: negative byte coords, arbitrary object value
             * Points.D2.ByteObjPoint<int[]> p4 = Points.D2.ByteObjPoint.of((byte) -1, (byte) -1, new int[]{1, 2, 3});
             * p4.x()            // returns (byte) -1
             * p4.y()            // returns (byte) -1
             * p4.value().length // returns 3
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the object value associated with this point, may be {@code null}
             * @return a new ByteObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> ByteObjPoint<T> of(final byte x, final byte y, final T value) {
                return new ByteObjPoint<>(x, y, value);
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
         * @param value the value associated with this point
         */
        public record IntBytePoint(int x, int y, byte value) {

            /**
             * Creates a new IntBytePoint with the specified coordinates and value.
             * This factory method is ideal when working with standard integer coordinates but the
             * associated value fits within a byte range, providing memory efficiency for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical integer coordinates with a byte value
             * Points.D2.IntBytePoint p1 = Points.D2.IntBytePoint.of(100, 200, (byte) 10);
             * p1.x()     // returns 100
             * p1.y()     // returns 200
             * p1.value() // returns (byte) 10
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.IntBytePoint p2 = Points.D2.IntBytePoint.of(0, 0, (byte) 0);
             * p2.x()     // returns 0
             * p2.y()     // returns 0
             * p2.value() // returns (byte) 0
             *
             * // Edge: Integer.MIN_VALUE x, Integer.MAX_VALUE y, Byte.MIN_VALUE value
             * Points.D2.IntBytePoint p3 = Points.D2.IntBytePoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, (byte) -128);
             * p3.x()     // returns Integer.MIN_VALUE (-2147483648)
             * p3.y()     // returns Integer.MAX_VALUE (2147483647)
             * p3.value() // returns (byte) -128
             *
             * // Edge: negative integer coords with Byte.MAX_VALUE
             * Points.D2.IntBytePoint p4 = Points.D2.IntBytePoint.of(-1, -1, (byte) 127);
             * p4.x()     // returns -1
             * p4.y()     // returns -1
             * p4.value() // returns (byte) 127
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the byte value associated with this point
             * @return a new IntBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntBytePoint of(final int x, final int y, final byte value) {
                return new IntBytePoint(x, y, value);
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
         * @param value the value associated with this point
         */
        public record IntIntPoint(int x, int y, int value) {

            /**
             * Creates a new IntIntPoint with the specified coordinates and value.
             * This is the most commonly used point type for general-purpose integer-based
             * coordinate systems, providing a balanced approach with full integer range for
             * both coordinates and the associated value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical positive coordinates and value
             * Points.D2.IntIntPoint p1 = Points.D2.IntIntPoint.of(100, 200, 300);
             * p1.x()     // returns 100
             * p1.y()     // returns 200
             * p1.value() // returns 300
             *
             * // Basic: zero coordinates and value
             * Points.D2.IntIntPoint p2 = Points.D2.IntIntPoint.of(0, 0, 0);
             * p2.x()     // returns 0
             * p2.y()     // returns 0
             * p2.value() // returns 0
             *
             * // Edge: Integer.MIN_VALUE for all fields
             * Points.D2.IntIntPoint p3 = Points.D2.IntIntPoint.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
             * p3.x()     // returns Integer.MIN_VALUE (-2147483648)
             * p3.y()     // returns Integer.MIN_VALUE (-2147483648)
             * p3.value() // returns Integer.MIN_VALUE (-2147483648)
             *
             * // Edge: Integer.MAX_VALUE for all fields
             * Points.D2.IntIntPoint p4 = Points.D2.IntIntPoint.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
             * p4.x()     // returns Integer.MAX_VALUE (2147483647)
             * p4.y()     // returns Integer.MAX_VALUE (2147483647)
             * p4.value() // returns Integer.MAX_VALUE (2147483647)
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the integer value associated with this point
             * @return a new IntIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntIntPoint of(final int x, final int y, final int value) {
                return new IntIntPoint(x, y, value);
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
         * @param value the value associated with this point
         */
        public record IntLongPoint(int x, int y, long value) {

            /**
             * Creates a new IntLongPoint with the specified coordinates and value.
             * This factory method is ideal when working with standard integer coordinates but requiring
             * a long value for timestamps, large counts, or identifiers that exceed the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical integer coordinates with a large long value
             * Points.D2.IntLongPoint p1 = Points.D2.IntLongPoint.of(50, 75, 10_000_000_000L);
             * p1.x()     // returns 50
             * p1.y()     // returns 75
             * p1.value() // returns 10000000000L
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.IntLongPoint p2 = Points.D2.IntLongPoint.of(0, 0, 0L);
             * p2.x()     // returns 0
             * p2.y()     // returns 0
             * p2.value() // returns 0L
             *
             * // Edge: Integer.MIN_VALUE x, Integer.MAX_VALUE y, Long.MIN_VALUE
             * Points.D2.IntLongPoint p3 = Points.D2.IntLongPoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, Long.MIN_VALUE);
             * p3.x()     // returns Integer.MIN_VALUE (-2147483648)
             * p3.y()     // returns Integer.MAX_VALUE (2147483647)
             * p3.value() // returns Long.MIN_VALUE (-9223372036854775808L)
             *
             * // Edge: negative integer coords with Long.MAX_VALUE
             * Points.D2.IntLongPoint p4 = Points.D2.IntLongPoint.of(-1, -1, Long.MAX_VALUE);
             * p4.x()     // returns -1
             * p4.y()     // returns -1
             * p4.value() // returns Long.MAX_VALUE (9223372036854775807L)
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the long value associated with this point
             * @return a new IntLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntLongPoint of(final int x, final int y, final long value) {
                return new IntLongPoint(x, y, value);
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
         * @param value the value associated with this point
         */
        public record IntDoublePoint(int x, int y, double value) {

            /**
             * Creates a new IntDoublePoint with the specified coordinates and value.
             * This factory method is ideal when working with standard integer grid coordinates but requiring
             * floating-point precision for the associated value, such as weights, distances, or probabilities.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical integer coordinates with a double value
             * Points.D2.IntDoublePoint p1 = Points.D2.IntDoublePoint.of(10, 20, 3.14159);
             * p1.x()     // returns 10
             * p1.y()     // returns 20
             * p1.value() // returns 3.14159
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.IntDoublePoint p2 = Points.D2.IntDoublePoint.of(0, 0, 0.0);
             * p2.x()     // returns 0
             * p2.y()     // returns 0
             * p2.value() // returns 0.0
             *
             * // Edge: Integer extremes, Double.NaN value
             * Points.D2.IntDoublePoint p3 = Points.D2.IntDoublePoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, Double.NaN);
             * p3.x()                   // returns Integer.MIN_VALUE (-2147483648)
             * p3.y()                   // returns Integer.MAX_VALUE (2147483647)
             * Double.isNaN(p3.value()) // returns true
             *
             * // Edge: negative integer coords with Double.NEGATIVE_INFINITY
             * Points.D2.IntDoublePoint p4 = Points.D2.IntDoublePoint.of(-1, -1, Double.NEGATIVE_INFINITY);
             * p4.x()     // returns -1
             * p4.y()     // returns -1
             * p4.value() // returns Double.NEGATIVE_INFINITY
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the double value associated with this point
             * @return a new IntDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntDoublePoint of(final int x, final int y, final double value) {
                return new IntDoublePoint(x, y, value);
            }
        }

        /**
         * Represents a two-dimensional point with integer coordinates and an object payload.
         * This record is a shallowly immutable carrier: the coordinates are fixed, while the
         * referenced value is stored as supplied and may itself be mutable.
         *
         * <p>Instances are thread-safe only when the referenced value is thread-safe or otherwise
         * not mutated concurrently.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param value the value associated with this point
         */
        public record IntObjPoint<T>(int x, int y, T value) {

            /**
             * Creates a new IntObjPoint with the specified coordinates and value reference.
             * The supplied value is stored as-is; this factory does not clone or otherwise protect
             * mutable payloads.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: integer coordinates with a String value
             * Points.D2.IntObjPoint<String> p1 = Points.D2.IntObjPoint.of(10, 20, "label");
             * p1.x()     // returns 10
             * p1.y()     // returns 20
             * p1.value() // returns "label"
             *
             * // Basic: integer coordinates with an Integer value
             * Points.D2.IntObjPoint<Integer> p2 = Points.D2.IntObjPoint.of(5, 8, 99);
             * p2.x()     // returns 5
             * p2.y()     // returns 8
             * p2.value() // returns 99
             *
             * // Edge: Integer extremes, null value (records permit null components)
             * Points.D2.IntObjPoint<String> p3 = Points.D2.IntObjPoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, null);
             * p3.x()     // returns Integer.MIN_VALUE (-2147483648)
             * p3.y()     // returns Integer.MAX_VALUE (2147483647)
             * p3.value() // returns null
             *
             * // Edge: zero coords, list as value
             * Points.D2.IntObjPoint<int[]> p4 = Points.D2.IntObjPoint.of(0, 0, new int[]{1, 2, 3});
             * p4.x()            // returns 0
             * p4.y()            // returns 0
             * p4.value().length // returns 3
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the object value associated with this point, may be {@code null}
             * @return a new IntObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> IntObjPoint<T> of(final int x, final int y, final T value) {
                return new IntObjPoint<>(x, y, value);
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
         * @param value the value associated with this point
         */
        public record LongBytePoint(long x, long y, byte value) {

            /**
             * Creates a new LongBytePoint with the specified coordinates and value.
             * This factory method is ideal when working with very large coordinate spaces requiring
             * long values but the associated value fits within a byte range, optimizing memory for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical large long coordinates with a byte value
             * Points.D2.LongBytePoint p1 = Points.D2.LongBytePoint.of(1_000_000L, 2_000_000L, (byte) 5);
             * p1.x()     // returns 1000000L
             * p1.y()     // returns 2000000L
             * p1.value() // returns (byte) 5
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.LongBytePoint p2 = Points.D2.LongBytePoint.of(0L, 0L, (byte) 0);
             * p2.x()     // returns 0L
             * p2.y()     // returns 0L
             * p2.value() // returns (byte) 0
             *
             * // Edge: Long.MIN_VALUE x, Long.MAX_VALUE y, Byte.MIN_VALUE
             * Points.D2.LongBytePoint p3 = Points.D2.LongBytePoint.of(Long.MIN_VALUE, Long.MAX_VALUE, (byte) -128);
             * p3.x()     // returns Long.MIN_VALUE (-9223372036854775808L)
             * p3.y()     // returns Long.MAX_VALUE (9223372036854775807L)
             * p3.value() // returns (byte) -128
             *
             * // Edge: negative long coords with Byte.MAX_VALUE
             * Points.D2.LongBytePoint p4 = Points.D2.LongBytePoint.of(-1L, -1L, (byte) 127);
             * p4.x()     // returns -1L
             * p4.y()     // returns -1L
             * p4.value() // returns (byte) 127
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the byte value associated with this point
             * @return a new LongBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongBytePoint of(final long x, final long y, final byte value) {
                return new LongBytePoint(x, y, value);
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
         * @param value the value associated with this point
         */
        public record LongIntPoint(long x, long y, int value) {

            /**
             * Creates a new LongIntPoint with the specified coordinates and value.
             * This factory method is ideal when working with very large coordinate spaces requiring
             * long values but the associated value fits within the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical large long coordinates with an integer value
             * Points.D2.LongIntPoint p1 = Points.D2.LongIntPoint.of(1_000_000L, 2_000_000L, 500);
             * p1.x()     // returns 1000000L
             * p1.y()     // returns 2000000L
             * p1.value() // returns 500
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.LongIntPoint p2 = Points.D2.LongIntPoint.of(0L, 0L, 0);
             * p2.x()     // returns 0L
             * p2.y()     // returns 0L
             * p2.value() // returns 0
             *
             * // Edge: Long.MIN_VALUE x, Long.MAX_VALUE y, Integer.MIN_VALUE
             * Points.D2.LongIntPoint p3 = Points.D2.LongIntPoint.of(Long.MIN_VALUE, Long.MAX_VALUE, Integer.MIN_VALUE);
             * p3.x()     // returns Long.MIN_VALUE (-9223372036854775808L)
             * p3.y()     // returns Long.MAX_VALUE (9223372036854775807L)
             * p3.value() // returns Integer.MIN_VALUE (-2147483648)
             *
             * // Edge: negative long coords with Integer.MAX_VALUE
             * Points.D2.LongIntPoint p4 = Points.D2.LongIntPoint.of(-1L, -1L, Integer.MAX_VALUE);
             * p4.x()     // returns -1L
             * p4.y()     // returns -1L
             * p4.value() // returns Integer.MAX_VALUE (2147483647)
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the integer value associated with this point
             * @return a new LongIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongIntPoint of(final long x, final long y, final int value) {
                return new LongIntPoint(x, y, value);
            }
        }

        /**
         * Represents an immutable two-dimensional point with long coordinates and a long value.
         * This class provides full long integer range for both coordinates and the associated value.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param value the value associated with this point
         */
        public record LongLongPoint(long x, long y, long value) {

            /**
             * Creates a new LongLongPoint with the specified coordinates and value.
             * This factory method provides the maximum integer range for both coordinates and the
             * associated value, suitable for very large coordinate spaces with large identifiers or timestamps.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical large long coordinates and long value
             * Points.D2.LongLongPoint p1 = Points.D2.LongLongPoint.of(1_000_000L, 2_000_000L, 3_000_000_000L);
             * p1.x()     // returns 1000000L
             * p1.y()     // returns 2000000L
             * p1.value() // returns 3000000000L
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.LongLongPoint p2 = Points.D2.LongLongPoint.of(0L, 0L, 0L);
             * p2.x()     // returns 0L
             * p2.y()     // returns 0L
             * p2.value() // returns 0L
             *
             * // Edge: Long.MIN_VALUE for all fields
             * Points.D2.LongLongPoint p3 = Points.D2.LongLongPoint.of(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);
             * p3.x()     // returns Long.MIN_VALUE (-9223372036854775808L)
             * p3.y()     // returns Long.MIN_VALUE (-9223372036854775808L)
             * p3.value() // returns Long.MIN_VALUE (-9223372036854775808L)
             *
             * // Edge: Long.MAX_VALUE for all fields
             * Points.D2.LongLongPoint p4 = Points.D2.LongLongPoint.of(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);
             * p4.x()     // returns Long.MAX_VALUE (9223372036854775807L)
             * p4.y()     // returns Long.MAX_VALUE (9223372036854775807L)
             * p4.value() // returns Long.MAX_VALUE (9223372036854775807L)
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the long value associated with this point
             * @return a new LongLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongLongPoint of(final long x, final long y, final long value) {
                return new LongLongPoint(x, y, value);
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
         * @param value the value associated with this point
         */
        public record LongDoublePoint(long x, long y, double value) {

            /**
             * Creates a new LongDoublePoint with the specified coordinates and value.
             * This factory method is ideal when working with very large coordinate spaces requiring
             * long values but needing floating-point precision for the associated value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical large long coordinates with a double value
             * Points.D2.LongDoublePoint p1 = Points.D2.LongDoublePoint.of(1_000_000L, 2_000_000L, 3.14159);
             * p1.x()     // returns 1000000L
             * p1.y()     // returns 2000000L
             * p1.value() // returns 3.14159
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.LongDoublePoint p2 = Points.D2.LongDoublePoint.of(0L, 0L, 0.0);
             * p2.x()     // returns 0L
             * p2.y()     // returns 0L
             * p2.value() // returns 0.0
             *
             * // Edge: Long.MIN_VALUE x, Long.MAX_VALUE y, Double.NaN value
             * Points.D2.LongDoublePoint p3 = Points.D2.LongDoublePoint.of(Long.MIN_VALUE, Long.MAX_VALUE, Double.NaN);
             * p3.x()                   // returns Long.MIN_VALUE (-9223372036854775808L)
             * p3.y()                   // returns Long.MAX_VALUE (9223372036854775807L)
             * Double.isNaN(p3.value()) // returns true
             *
             * // Edge: negative long coords with Double.NEGATIVE_INFINITY
             * Points.D2.LongDoublePoint p4 = Points.D2.LongDoublePoint.of(-1L, -1L, Double.NEGATIVE_INFINITY);
             * p4.x()     // returns -1L
             * p4.y()     // returns -1L
             * p4.value() // returns Double.NEGATIVE_INFINITY
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the double value associated with this point
             * @return a new LongDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongDoublePoint of(final long x, final long y, final double value) {
                return new LongDoublePoint(x, y, value);
            }
        }

        /**
         * Represents a two-dimensional point with long coordinates and an object payload.
         * This record is a shallowly immutable carrier: the coordinates are fixed, while the
         * referenced value is stored as supplied and may itself be mutable.
         *
         * <p>Instances are thread-safe only when the referenced value is thread-safe or otherwise
         * not mutated concurrently.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param value the value associated with this point
         */
        public record LongObjPoint<T>(long x, long y, T value) {

            /**
             * Creates a new LongObjPoint with the specified coordinates and value reference.
             * The supplied value is stored as-is; this factory does not clone or otherwise protect
             * mutable payloads.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: large long coordinates with a String value
             * Points.D2.LongObjPoint<String> p1 = Points.D2.LongObjPoint.of(1_000_000L, 2_000_000L, "marker");
             * p1.x()     // returns 1000000L
             * p1.y()     // returns 2000000L
             * p1.value() // returns "marker"
             *
             * // Basic: small long coordinates with an Integer value
             * Points.D2.LongObjPoint<Integer> p2 = Points.D2.LongObjPoint.of(5L, 8L, 99);
             * p2.x()     // returns 5L
             * p2.y()     // returns 8L
             * p2.value() // returns 99
             *
             * // Edge: Long.MIN_VALUE x, Long.MAX_VALUE y, null value (records permit null components)
             * Points.D2.LongObjPoint<String> p3 = Points.D2.LongObjPoint.of(Long.MIN_VALUE, Long.MAX_VALUE, null);
             * p3.x()     // returns Long.MIN_VALUE (-9223372036854775808L)
             * p3.y()     // returns Long.MAX_VALUE (9223372036854775807L)
             * p3.value() // returns null
             *
             * // Edge: zero coords, array as value
             * Points.D2.LongObjPoint<int[]> p4 = Points.D2.LongObjPoint.of(0L, 0L, new int[]{10, 20});
             * p4.x()            // returns 0L
             * p4.y()            // returns 0L
             * p4.value().length // returns 2
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the object value associated with this point, may be {@code null}
             * @return a new LongObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> LongObjPoint<T> of(final long x, final long y, final T value) {
                return new LongObjPoint<>(x, y, value);
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
         * @param value the value associated with this point
         */
        public record DoubleBytePoint(double x, double y, byte value) {

            /**
             * Creates a new DoubleBytePoint with the specified coordinates and value.
             * This factory method is ideal when working with floating-point coordinate spaces but the
             * associated value fits within a byte range, optimizing memory for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical double coordinates with a byte value
             * Points.D2.DoubleBytePoint p1 = Points.D2.DoubleBytePoint.of(10.5, 20.7, (byte) 3);
             * p1.x()     // returns 10.5
             * p1.y()     // returns 20.7
             * p1.value() // returns (byte) 3
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.DoubleBytePoint p2 = Points.D2.DoubleBytePoint.of(0.0, 0.0, (byte) 0);
             * p2.x()     // returns 0.0
             * p2.y()     // returns 0.0
             * p2.value() // returns (byte) 0
             *
             * // Edge: Double.NaN x, Double.POSITIVE_INFINITY y, Byte.MIN_VALUE
             * Points.D2.DoubleBytePoint p3 = Points.D2.DoubleBytePoint.of(Double.NaN, Double.POSITIVE_INFINITY, (byte) -128);
             * Double.isNaN(p3.x())    // returns true
             * p3.y()                  // returns Double.POSITIVE_INFINITY
             * p3.value()              // returns (byte) -128
             *
             * // Edge: negative double coords with Byte.MAX_VALUE
             * Points.D2.DoubleBytePoint p4 = Points.D2.DoubleBytePoint.of(-1.0, -1.0, (byte) 127);
             * p4.x()     // returns -1.0
             * p4.y()     // returns -1.0
             * p4.value() // returns (byte) 127
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the byte value associated with this point
             * @return a new DoubleBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleBytePoint of(final double x, final double y, final byte value) {
                return new DoubleBytePoint(x, y, value);
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
         * @param value the value associated with this point
         */
        public record DoubleIntPoint(double x, double y, int value) {

            /**
             * Creates a new DoubleIntPoint with the specified coordinates and value.
             * This factory method is ideal when working with floating-point coordinate spaces but the
             * associated value fits within the integer range, such as counts or indices in continuous spaces.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical double coordinates with an integer value
             * Points.D2.DoubleIntPoint p1 = Points.D2.DoubleIntPoint.of(10.5, 20.7, 100);
             * p1.x()     // returns 10.5
             * p1.y()     // returns 20.7
             * p1.value() // returns 100
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.DoubleIntPoint p2 = Points.D2.DoubleIntPoint.of(0.0, 0.0, 0);
             * p2.x()     // returns 0.0
             * p2.y()     // returns 0.0
             * p2.value() // returns 0
             *
             * // Edge: Double.NEGATIVE_INFINITY x, Double.NaN y, Integer.MIN_VALUE
             * Points.D2.DoubleIntPoint p3 = Points.D2.DoubleIntPoint.of(Double.NEGATIVE_INFINITY, Double.NaN, Integer.MIN_VALUE);
             * p3.x()               // returns Double.NEGATIVE_INFINITY
             * Double.isNaN(p3.y()) // returns true
             * p3.value()           // returns Integer.MIN_VALUE (-2147483648)
             *
             * // Edge: negative double coords with Integer.MAX_VALUE
             * Points.D2.DoubleIntPoint p4 = Points.D2.DoubleIntPoint.of(-1.0, -1.0, Integer.MAX_VALUE);
             * p4.x()     // returns -1.0
             * p4.y()     // returns -1.0
             * p4.value() // returns Integer.MAX_VALUE (2147483647)
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the integer value associated with this point
             * @return a new DoubleIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleIntPoint of(final double x, final double y, final int value) {
                return new DoubleIntPoint(x, y, value);
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
         * @param value the value associated with this point
         */
        public record DoubleLongPoint(double x, double y, long value) {

            /**
             * Creates a new DoubleLongPoint with the specified coordinates and value.
             * This factory method is ideal when working with floating-point coordinate spaces and requiring
             * long values for timestamps, large identifiers, or counts that exceed the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical double coordinates with a large long value
             * Points.D2.DoubleLongPoint p1 = Points.D2.DoubleLongPoint.of(10.5, 20.7, 1_000_000_000L);
             * p1.x()     // returns 10.5
             * p1.y()     // returns 20.7
             * p1.value() // returns 1000000000L
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.DoubleLongPoint p2 = Points.D2.DoubleLongPoint.of(0.0, 0.0, 0L);
             * p2.x()     // returns 0.0
             * p2.y()     // returns 0.0
             * p2.value() // returns 0L
             *
             * // Edge: Double.NaN x, Double.POSITIVE_INFINITY y, Long.MIN_VALUE
             * Points.D2.DoubleLongPoint p3 = Points.D2.DoubleLongPoint.of(Double.NaN, Double.POSITIVE_INFINITY, Long.MIN_VALUE);
             * Double.isNaN(p3.x()) // returns true
             * p3.y()               // returns Double.POSITIVE_INFINITY
             * p3.value()           // returns Long.MIN_VALUE (-9223372036854775808L)
             *
             * // Edge: negative double coords with Long.MAX_VALUE
             * Points.D2.DoubleLongPoint p4 = Points.D2.DoubleLongPoint.of(-1.0, -1.0, Long.MAX_VALUE);
             * p4.x()     // returns -1.0
             * p4.y()     // returns -1.0
             * p4.value() // returns Long.MAX_VALUE (9223372036854775807L)
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the long value associated with this point
             * @return a new DoubleLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleLongPoint of(final double x, final double y, final long value) {
                return new DoubleLongPoint(x, y, value);
            }
        }

        /**
         * Represents an immutable two-dimensional point with double-precision floating-point coordinates and a double-precision floating-point value.
         * This class provides full double-precision floating-point support for both coordinates and the associated value.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param value the value associated with this point
         */
        public record DoubleDoublePoint(double x, double y, double value) {

            /**
             * Creates a new DoubleDoublePoint with the specified coordinates and value.
             * This factory method provides full double-precision floating-point support for both coordinates
             * and the associated value, ideal for continuous mathematical and scientific applications.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical double coordinates and double value
             * Points.D2.DoubleDoublePoint p1 = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 3.14159);
             * p1.x()     // returns 10.5
             * p1.y()     // returns 20.7
             * p1.value() // returns 3.14159
             *
             * // Basic: zero coordinates and zero value
             * Points.D2.DoubleDoublePoint p2 = Points.D2.DoubleDoublePoint.of(0.0, 0.0, 0.0);
             * p2.x()     // returns 0.0
             * p2.y()     // returns 0.0
             * p2.value() // returns 0.0
             *
             * // Edge: Double.NaN for all fields
             * Points.D2.DoubleDoublePoint p3 = Points.D2.DoubleDoublePoint.of(Double.NaN, Double.NaN, Double.NaN);
             * Double.isNaN(p3.x())     // returns true
             * Double.isNaN(p3.y())     // returns true
             * Double.isNaN(p3.value()) // returns true
             *
             * // Edge: positive/negative infinity coords with Double.MIN_VALUE
             * Points.D2.DoubleDoublePoint p4 = Points.D2.DoubleDoublePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.MIN_VALUE);
             * p4.x()     // returns Double.POSITIVE_INFINITY
             * p4.y()     // returns Double.NEGATIVE_INFINITY
             * p4.value() // returns Double.MIN_VALUE (4.9E-324)
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the double value associated with this point
             * @return a new DoubleDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleDoublePoint of(final double x, final double y, final double value) {
                return new DoubleDoublePoint(x, y, value);
            }
        }

        /**
         * Represents a two-dimensional point with double-precision floating-point coordinates and an object payload.
         * This record is a shallowly immutable carrier: the coordinates are fixed, while the
         * referenced value is stored as supplied and may itself be mutable.
         *
         * <p>Instances are thread-safe only when the referenced value is thread-safe or otherwise
         * not mutated concurrently.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param value the value associated with this point
         */
        public record DoubleObjPoint<T>(double x, double y, T value) {

            /**
             * Creates a new DoubleObjPoint with the specified coordinates and value reference.
             * The supplied value is stored as-is; this factory does not clone or otherwise protect
             * mutable payloads.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: double coordinates with a String value
             * Points.D2.DoubleObjPoint<String> p1 = Points.D2.DoubleObjPoint.of(10.5, 20.7, "location");
             * p1.x()     // returns 10.5
             * p1.y()     // returns 20.7
             * p1.value() // returns "location"
             *
             * // Basic: double coordinates with an Integer value
             * Points.D2.DoubleObjPoint<Integer> p2 = Points.D2.DoubleObjPoint.of(3.14159, 2.71828, 42);
             * p2.x()     // returns 3.14159
             * p2.y()     // returns 2.71828
             * p2.value() // returns 42
             *
             * // Edge: Double.NaN coords, null value (records permit null components)
             * Points.D2.DoubleObjPoint<String> p3 = Points.D2.DoubleObjPoint.of(Double.NaN, Double.POSITIVE_INFINITY, null);
             * Double.isNaN(p3.x())    // returns true
             * p3.y()                  // returns Double.POSITIVE_INFINITY
             * p3.value()              // returns null
             *
             * // Edge: zero coords, array as value
             * Points.D2.DoubleObjPoint<int[]> p4 = Points.D2.DoubleObjPoint.of(0.0, 0.0, new int[]{10, 20});
             * p4.x()            // returns 0.0
             * p4.y()            // returns 0.0
             * p4.value().length // returns 2
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param value the object value associated with this point, may be {@code null}
             * @return a new DoubleObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> DoubleObjPoint<T> of(final double x, final double y, final T value) {
                return new DoubleObjPoint<>(x, y, value);
            }
        }
    }

    /**
     * Namespace for three-dimensional point/value records.
     *
     * <p>The nested records cover common coordinate widths ({@code byte}, {@code int}, {@code long},
     * and {@code double}) together with primitive or object payload types so callers can choose a
     * compact spatial carrier without defining a custom type. Object-valued variants retain their
     * payload references without defensive copying.</p>
     *
     * @see D2
     */
    public static final class D3 {

        private D3() {
            // utility class.
        }

        /**
         * Represents an immutable three-dimensional point with byte coordinates and a byte value.
         * This class is optimized for memory-constrained scenarios where coordinates
         * and values fit within the byte range (-128 to 127).
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record ByteBytePoint(byte x, byte y, byte z, byte value) {

            /**
             * Creates a new ByteBytePoint with the specified coordinates and value.
             * This factory method provides a convenient way to construct three-dimensional points
             * with byte-range coordinates and values, optimized for maximum memory efficiency.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical positive coordinates and value
             * Points.D3.ByteBytePoint p1 = Points.D3.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30, (byte) 5);
             * p1.x();      // returns (byte) 10
             * p1.y();      // returns (byte) 20
             * p1.z();      // returns (byte) 30
             * p1.value();  // returns (byte) 5
             *
             * // Basic: negative coordinates
             * Points.D3.ByteBytePoint p2 = Points.D3.ByteBytePoint.of((byte) -10, (byte) -20, (byte) -30, (byte) -1);
             * p2.x();      // returns (byte) -10
             * p2.value();  // returns (byte) -1
             *
             * // Edge: all zeros
             * Points.D3.ByteBytePoint p3 = Points.D3.ByteBytePoint.of((byte) 0, (byte) 0, (byte) 0, (byte) 0);
             * p3.x();      // returns (byte) 0
             * p3.value();  // returns (byte) 0
             *
             * // Edge: boundary values Byte.MIN_VALUE and Byte.MAX_VALUE
             * Points.D3.ByteBytePoint p4 = Points.D3.ByteBytePoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0, Byte.MAX_VALUE);
             * p4.x();      // returns (byte) -128
             * p4.y();      // returns (byte) 127
             * p4.value();  // returns (byte) 127
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the byte value associated with this point
             * @return a new ByteBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteBytePoint of(final byte x, final byte y, final byte z, final byte value) {
                return new ByteBytePoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with byte coordinates and an integer value.
         * This class is useful when coordinates are constrained to byte range (-128 to 127)
         * but the associated value requires the full integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record ByteIntPoint(byte x, byte y, byte z, int value) {

            /**
             * Creates a new ByteIntPoint with the specified coordinates and value.
             * This factory method is ideal when working with small-range 3D coordinates but requiring
             * a full integer range for the associated value, such as counting or indexing in 3D space.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical coordinates and large integer value
             * Points.D3.ByteIntPoint p1 = Points.D3.ByteIntPoint.of((byte) 5, (byte) 10, (byte) 15, 1000);
             * p1.x();      // returns (byte) 5
             * p1.y();      // returns (byte) 10
             * p1.z();      // returns (byte) 15
             * p1.value();  // returns 1000
             *
             * // Basic: negative byte coordinates with positive value
             * Points.D3.ByteIntPoint p2 = Points.D3.ByteIntPoint.of((byte) -5, (byte) -10, (byte) -15, 999999);
             * p2.x();      // returns (byte) -5
             * p2.value();  // returns 999999
             *
             * // Edge: all zero coordinates and zero value
             * Points.D3.ByteIntPoint p3 = Points.D3.ByteIntPoint.of((byte) 0, (byte) 0, (byte) 0, 0);
             * p3.value();  // returns 0
             *
             * // Edge: byte boundary coordinates and Integer boundary values
             * Points.D3.ByteIntPoint p4 = Points.D3.ByteIntPoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0, Integer.MIN_VALUE);
             * p4.x();      // returns (byte) -128
             * p4.y();      // returns (byte) 127
             * p4.value();  // returns -2147483648
             *
             * Points.D3.ByteIntPoint p5 = Points.D3.ByteIntPoint.of((byte) 1, (byte) 2, (byte) 3, Integer.MAX_VALUE);
             * p5.value();  // returns 2147483647
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the integer value associated with this point
             * @return a new ByteIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteIntPoint of(final byte x, final byte y, final byte z, final int value) {
                return new ByteIntPoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with byte coordinates and a long value.
         * This class is useful when coordinates are constrained to byte range (-128 to 127)
         * but the associated value requires the full long integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record ByteLongPoint(byte x, byte y, byte z, long value) {

            /**
             * Creates a new ByteLongPoint with the specified coordinates and value.
             * This factory method is ideal when working with small-range 3D coordinates but requiring
             * a full long integer range for the associated value, such as timestamps or large identifiers in 3D space.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical coordinates and large long value
             * Points.D3.ByteLongPoint p1 = Points.D3.ByteLongPoint.of((byte) 3, (byte) 7, (byte) 11, 1000000000L);
             * p1.x();      // returns (byte) 3
             * p1.y();      // returns (byte) 7
             * p1.z();      // returns (byte) 11
             * p1.value();  // returns 1000000000L
             *
             * // Basic: negative byte coordinates with long value
             * Points.D3.ByteLongPoint p2 = Points.D3.ByteLongPoint.of((byte) -50, (byte) -60, (byte) -70, 5000000000L);
             * p2.x();      // returns (byte) -50
             * p2.value();  // returns 5000000000L
             *
             * // Edge: all zero coordinates and zero value
             * Points.D3.ByteLongPoint p3 = Points.D3.ByteLongPoint.of((byte) 0, (byte) 0, (byte) 0, 0L);
             * p3.value();  // returns 0L
             *
             * // Edge: byte boundary coords and Long boundary values
             * Points.D3.ByteLongPoint p4 = Points.D3.ByteLongPoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0, Long.MIN_VALUE);
             * p4.x();      // returns (byte) -128
             * p4.value();  // returns -9223372036854775808L
             *
             * Points.D3.ByteLongPoint p5 = Points.D3.ByteLongPoint.of((byte) 1, (byte) 2, (byte) 3, Long.MAX_VALUE);
             * p5.value();  // returns 9223372036854775807L
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the long value associated with this point
             * @return a new ByteLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteLongPoint of(final byte x, final byte y, final byte z, final long value) {
                return new ByteLongPoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with byte coordinates and a double-precision floating-point value.
         * This class is useful when coordinates are constrained to byte range (-128 to 127)
         * but the associated value requires floating-point precision.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record ByteDoublePoint(byte x, byte y, byte z, double value) {

            /**
             * Creates a new ByteDoublePoint with the specified coordinates and value.
             * This factory method is ideal when working with small-range 3D coordinates but requiring
             * floating-point precision for the associated value, such as densities, probabilities, or physical measurements in 3D space.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical coordinates and floating-point value
             * Points.D3.ByteDoublePoint p1 = Points.D3.ByteDoublePoint.of((byte) 2, (byte) 4, (byte) 6, 3.14159);
             * p1.x();      // returns (byte) 2
             * p1.y();      // returns (byte) 4
             * p1.z();      // returns (byte) 6
             * p1.value();  // returns 3.14159
             *
             * // Basic: negative coordinates with fractional value
             * Points.D3.ByteDoublePoint p2 = Points.D3.ByteDoublePoint.of((byte) -5, (byte) -10, (byte) -15, 0.85);
             * p2.x();      // returns (byte) -5
             * p2.value();  // returns 0.85
             *
             * // Edge: zero coordinates and zero value
             * Points.D3.ByteDoublePoint p3 = Points.D3.ByteDoublePoint.of((byte) 0, (byte) 0, (byte) 0, 0.0);
             * p3.value();  // returns 0.0
             *
             * // Edge: byte boundary coords and special double values
             * Points.D3.ByteDoublePoint p4 = Points.D3.ByteDoublePoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0, Double.NaN);
             * Double.isNaN(p4.value());  // returns true
             *
             * Points.D3.ByteDoublePoint p5 = Points.D3.ByteDoublePoint.of((byte) 1, (byte) 2, (byte) 3, Double.POSITIVE_INFINITY);
             * p5.value();  // returns Double.POSITIVE_INFINITY
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the double value associated with this point
             * @return a new ByteDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteDoublePoint of(final byte x, final byte y, final byte z, final double value) {
                return new ByteDoublePoint(x, y, z, value);
            }
        }

        /**
         * Represents a three-dimensional point with byte coordinates and an object payload.
         * This record is a shallowly immutable carrier: the coordinates are fixed, while the
         * referenced value is stored as supplied and may itself be mutable.
         *
         * <p>Instances are thread-safe only when the referenced value is thread-safe or otherwise
         * not mutated concurrently.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record ByteObjPoint<T>(byte x, byte y, byte z, T value) {

            /**
             * Creates a new ByteObjPoint with the specified coordinates and value reference.
             * The supplied value is stored as-is; this factory does not clone or otherwise protect
             * mutable payloads.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: byte coordinates with a String value
             * Points.D3.ByteObjPoint<String> p1 = Points.D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "voxel");
             * p1.x();      // returns (byte) 1
             * p1.y();      // returns (byte) 2
             * p1.z();      // returns (byte) 3
             * p1.value();  // returns "voxel"
             *
             * // Basic: negative coords with Integer wrapper value
             * Points.D3.ByteObjPoint<Integer> p2 = Points.D3.ByteObjPoint.of((byte) -10, (byte) -20, (byte) -30, 42);
             * p2.x();      // returns (byte) -10
             * p2.value();  // returns 42
             *
             * // Edge: boundary byte coords and null value
             * Points.D3.ByteObjPoint<String> p3 = Points.D3.ByteObjPoint.of(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0, null);
             * p3.x();      // returns (byte) -128
             * p3.y();      // returns (byte) 127
             * p3.value();  // returns null
             *
             * // Edge: all zero coords and empty string
             * Points.D3.ByteObjPoint<String> p4 = Points.D3.ByteObjPoint.of((byte) 0, (byte) 0, (byte) 0, "");
             * p4.value();  // returns ""
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the object value associated with this point, may be {@code null}
             * @return a new ByteObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> ByteObjPoint<T> of(final byte x, final byte y, final byte z, final T value) {
                return new ByteObjPoint<>(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with integer coordinates and a byte value.
         * This class is useful when coordinates require the full integer range
         * but the associated value is constrained to byte range (-128 to 127).
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record IntBytePoint(int x, int y, int z, byte value) {

            /**
             * Creates a new IntBytePoint with the specified coordinates and value.
             * This factory method is ideal when working with standard integer 3D coordinates but the
             * associated value fits within a byte range, providing memory efficiency for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical integer coordinates with byte value
             * Points.D3.IntBytePoint p1 = Points.D3.IntBytePoint.of(100, 200, 300, (byte) 10);
             * p1.x();      // returns 100
             * p1.y();      // returns 200
             * p1.z();      // returns 300
             * p1.value();  // returns (byte) 10
             *
             * // Basic: negative integer coordinates
             * Points.D3.IntBytePoint p2 = Points.D3.IntBytePoint.of(-100, -200, -300, (byte) -5);
             * p2.x();      // returns -100
             * p2.value();  // returns (byte) -5
             *
             * // Edge: zero coordinates and zero byte value
             * Points.D3.IntBytePoint p3 = Points.D3.IntBytePoint.of(0, 0, 0, (byte) 0);
             * p3.value();  // returns (byte) 0
             *
             * // Edge: Integer boundary coordinates with byte boundary values
             * Points.D3.IntBytePoint p4 = Points.D3.IntBytePoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, Byte.MIN_VALUE);
             * p4.x();      // returns -2147483648
             * p4.y();      // returns 2147483647
             * p4.value();  // returns (byte) -128
             *
             * Points.D3.IntBytePoint p5 = Points.D3.IntBytePoint.of(1, 2, 3, Byte.MAX_VALUE);
             * p5.value();  // returns (byte) 127
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the byte value associated with this point
             * @return a new IntBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntBytePoint of(final int x, final int y, final int z, final byte value) {
                return new IntBytePoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with integer coordinates and an integer value.
         * This class is the most commonly used point type for general-purpose integer-based
         * coordinate systems and grid operations.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record IntIntPoint(int x, int y, int z, int value) {

            /**
             * Creates a new IntIntPoint with the specified coordinates and value.
             * This is the most commonly used 3D point type for general-purpose integer-based
             * coordinate systems, providing a balanced approach with full integer range for
             * all three coordinates and the associated value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical integer coordinates and value
             * Points.D3.IntIntPoint p1 = Points.D3.IntIntPoint.of(100, 200, 300, 400);
             * p1.x();      // returns 100
             * p1.y();      // returns 200
             * p1.z();      // returns 300
             * p1.value();  // returns 400
             *
             * // Basic: negative coordinates and value
             * Points.D3.IntIntPoint p2 = Points.D3.IntIntPoint.of(-10, -20, -30, -100);
             * p2.x();      // returns -10
             * p2.value();  // returns -100
             *
             * // Edge: all zeros
             * Points.D3.IntIntPoint p3 = Points.D3.IntIntPoint.of(0, 0, 0, 0);
             * p3.x();      // returns 0
             * p3.value();  // returns 0
             *
             * // Edge: Integer boundary values
             * Points.D3.IntIntPoint p4 = Points.D3.IntIntPoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, Integer.MIN_VALUE);
             * p4.x();      // returns -2147483648
             * p4.y();      // returns 2147483647
             * p4.value();  // returns -2147483648
             *
             * Points.D3.IntIntPoint p5 = Points.D3.IntIntPoint.of(1, 2, 3, Integer.MAX_VALUE);
             * p5.value();  // returns 2147483647
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the integer value associated with this point
             * @return a new IntIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntIntPoint of(final int x, final int y, final int z, final int value) {
                return new IntIntPoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with integer coordinates and a long value.
         * This class is useful when coordinates fit within the integer range
         * but the associated value requires the full long integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record IntLongPoint(int x, int y, int z, long value) {

            /**
             * Creates a new IntLongPoint with the specified coordinates and value.
             * This factory method is ideal when working with standard integer 3D coordinates but requiring
             * a long value for timestamps, large counts, or identifiers that exceed the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical integer coordinates with long value
             * Points.D3.IntLongPoint p1 = Points.D3.IntLongPoint.of(50, 75, 100, 10000000000L);
             * p1.x();      // returns 50
             * p1.y();      // returns 75
             * p1.z();      // returns 100
             * p1.value();  // returns 10000000000L
             *
             * // Basic: negative coordinates with large long value
             * Points.D3.IntLongPoint p2 = Points.D3.IntLongPoint.of(-50, -75, -100, 9876543210L);
             * p2.x();      // returns -50
             * p2.value();  // returns 9876543210L
             *
             * // Edge: zero coordinates and zero long value
             * Points.D3.IntLongPoint p3 = Points.D3.IntLongPoint.of(0, 0, 0, 0L);
             * p3.value();  // returns 0L
             *
             * // Edge: Integer boundary coordinates and Long boundary values
             * Points.D3.IntLongPoint p4 = Points.D3.IntLongPoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, Long.MIN_VALUE);
             * p4.x();      // returns -2147483648
             * p4.y();      // returns 2147483647
             * p4.value();  // returns -9223372036854775808L
             *
             * Points.D3.IntLongPoint p5 = Points.D3.IntLongPoint.of(1, 2, 3, Long.MAX_VALUE);
             * p5.value();  // returns 9223372036854775807L
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the long value associated with this point
             * @return a new IntLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntLongPoint of(final int x, final int y, final int z, final long value) {
                return new IntLongPoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with integer coordinates and a double-precision floating-point value.
         * This class is useful when coordinates fit within the integer range
         * but the associated value requires floating-point precision.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record IntDoublePoint(int x, int y, int z, double value) {

            /**
             * Creates a new IntDoublePoint with the specified coordinates and value.
             * This factory method is ideal when working with standard integer 3D grid coordinates but requiring
             * floating-point precision for the associated value, such as densities, distances, or probabilities.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical integer coordinates with double value
             * Points.D3.IntDoublePoint p1 = Points.D3.IntDoublePoint.of(10, 20, 30, 3.14159);
             * p1.x();      // returns 10
             * p1.y();      // returns 20
             * p1.z();      // returns 30
             * p1.value();  // returns 3.14159
             *
             * // Basic: negative coordinates with fractional value
             * Points.D3.IntDoublePoint p2 = Points.D3.IntDoublePoint.of(-10, -20, -30, 0.85);
             * p2.x();      // returns -10
             * p2.value();  // returns 0.85
             *
             * // Edge: zero coordinates and zero double value
             * Points.D3.IntDoublePoint p3 = Points.D3.IntDoublePoint.of(0, 0, 0, 0.0);
             * p3.value();  // returns 0.0
             *
             * // Edge: Integer boundary coordinates and special double values
             * Points.D3.IntDoublePoint p4 = Points.D3.IntDoublePoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, Double.NaN);
             * Double.isNaN(p4.value());  // returns true
             *
             * Points.D3.IntDoublePoint p5 = Points.D3.IntDoublePoint.of(1, 2, 3, Double.NEGATIVE_INFINITY);
             * p5.value();  // returns Double.NEGATIVE_INFINITY
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the double value associated with this point
             * @return a new IntDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntDoublePoint of(final int x, final int y, final int z, final double value) {
                return new IntDoublePoint(x, y, z, value);
            }
        }

        /**
         * Represents a three-dimensional point with integer coordinates and an object payload.
         * This record is a shallowly immutable carrier: the coordinates are fixed, while the
         * referenced value is stored as supplied and may itself be mutable.
         *
         * <p>Instances are thread-safe only when the referenced value is thread-safe or otherwise
         * not mutated concurrently.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record IntObjPoint<T>(int x, int y, int z, T value) {

            /**
             * Creates a new IntObjPoint with the specified coordinates and value reference.
             * The supplied value is stored as-is; this factory does not clone or otherwise protect
             * mutable payloads.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: integer coordinates with a String value
             * Points.D3.IntObjPoint<String> p1 = Points.D3.IntObjPoint.of(10, 20, 30, "block");
             * p1.x();      // returns 10
             * p1.y();      // returns 20
             * p1.z();      // returns 30
             * p1.value();  // returns "block"
             *
             * // Basic: negative coordinates with Integer wrapper value
             * Points.D3.IntObjPoint<Integer> p2 = Points.D3.IntObjPoint.of(-10, -20, -30, 99);
             * p2.x();      // returns -10
             * p2.value();  // returns 99
             *
             * // Edge: Integer boundary coordinates and null value
             * Points.D3.IntObjPoint<String> p3 = Points.D3.IntObjPoint.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, null);
             * p3.x();      // returns -2147483648
             * p3.y();      // returns 2147483647
             * p3.value();  // returns null
             *
             * // Edge: zero coordinates and empty string value
             * Points.D3.IntObjPoint<String> p4 = Points.D3.IntObjPoint.of(0, 0, 0, "");
             * p4.value();  // returns ""
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the object value associated with this point, may be {@code null}
             * @return a new IntObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> IntObjPoint<T> of(final int x, final int y, final int z, final T value) {
                return new IntObjPoint<>(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with long coordinates and a byte value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value is constrained to byte range (-128 to 127).
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record LongBytePoint(long x, long y, long z, byte value) {

            /**
             * Creates a new LongBytePoint with the specified coordinates and value.
             * This factory method is ideal when working with very large 3D coordinate spaces requiring
             * long values but the associated value fits within a byte range, optimizing memory for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: large long coordinates with byte value
             * Points.D3.LongBytePoint p1 = Points.D3.LongBytePoint.of(1000000L, 2000000L, 3000000L, (byte) 5);
             * p1.x();      // returns 1000000L
             * p1.y();      // returns 2000000L
             * p1.z();      // returns 3000000L
             * p1.value();  // returns (byte) 5
             *
             * // Basic: negative long coordinates with negative byte value
             * Points.D3.LongBytePoint p2 = Points.D3.LongBytePoint.of(-1000000L, -2000000L, -3000000L, (byte) -10);
             * p2.x();      // returns -1000000L
             * p2.value();  // returns (byte) -10
             *
             * // Edge: zero coordinates and zero byte value
             * Points.D3.LongBytePoint p3 = Points.D3.LongBytePoint.of(0L, 0L, 0L, (byte) 0);
             * p3.value();  // returns (byte) 0
             *
             * // Edge: Long boundary coordinates and byte boundary values
             * Points.D3.LongBytePoint p4 = Points.D3.LongBytePoint.of(Long.MIN_VALUE, Long.MAX_VALUE, 0L, Byte.MIN_VALUE);
             * p4.x();      // returns -9223372036854775808L
             * p4.y();      // returns 9223372036854775807L
             * p4.value();  // returns (byte) -128
             *
             * Points.D3.LongBytePoint p5 = Points.D3.LongBytePoint.of(1L, 2L, 3L, Byte.MAX_VALUE);
             * p5.value();  // returns (byte) 127
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the byte value associated with this point
             * @return a new LongBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongBytePoint of(final long x, final long y, final long z, final byte value) {
                return new LongBytePoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with long coordinates and an integer value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value fits within the integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record LongIntPoint(long x, long y, long z, int value) {

            /**
             * Creates a new LongIntPoint with the specified coordinates and value.
             * This factory method is ideal when working with very large 3D coordinate spaces requiring
             * long values but the associated value fits within the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: large long coordinates with integer value
             * Points.D3.LongIntPoint p1 = Points.D3.LongIntPoint.of(1000000L, 2000000L, 3000000L, 500);
             * p1.x();      // returns 1000000L
             * p1.y();      // returns 2000000L
             * p1.z();      // returns 3000000L
             * p1.value();  // returns 500
             *
             * // Basic: negative long coordinates with integer value
             * Points.D3.LongIntPoint p2 = Points.D3.LongIntPoint.of(-1000000L, -2000000L, -3000000L, 1000000);
             * p2.x();      // returns -1000000L
             * p2.value();  // returns 1000000
             *
             * // Edge: zero coordinates and zero integer value
             * Points.D3.LongIntPoint p3 = Points.D3.LongIntPoint.of(0L, 0L, 0L, 0);
             * p3.value();  // returns 0
             *
             * // Edge: Long boundary coordinates and Integer boundary values
             * Points.D3.LongIntPoint p4 = Points.D3.LongIntPoint.of(Long.MIN_VALUE, Long.MAX_VALUE, 0L, Integer.MIN_VALUE);
             * p4.x();      // returns -9223372036854775808L
             * p4.y();      // returns 9223372036854775807L
             * p4.value();  // returns -2147483648
             *
             * Points.D3.LongIntPoint p5 = Points.D3.LongIntPoint.of(1L, 2L, 3L, Integer.MAX_VALUE);
             * p5.value();  // returns 2147483647
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the integer value associated with this point
             * @return a new LongIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongIntPoint of(final long x, final long y, final long z, final int value) {
                return new LongIntPoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with long coordinates and a long value.
         * This class provides full long integer range for all three coordinates and the associated value.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record LongLongPoint(long x, long y, long z, long value) {

            /**
             * Creates a new LongLongPoint with the specified coordinates and value.
             * This factory method provides the maximum integer range for all three coordinates and the
             * associated value, suitable for very large 3D coordinate spaces with large identifiers or timestamps.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: large long coordinates and long value
             * Points.D3.LongLongPoint p1 = Points.D3.LongLongPoint.of(1000000L, 2000000L, 3000000L, 4000000000L);
             * p1.x();      // returns 1000000L
             * p1.y();      // returns 2000000L
             * p1.z();      // returns 3000000L
             * p1.value();  // returns 4000000000L
             *
             * // Basic: negative long coordinates and value
             * Points.D3.LongLongPoint p2 = Points.D3.LongLongPoint.of(-1000000L, -2000000L, -3000000L, -9876543210L);
             * p2.x();      // returns -1000000L
             * p2.value();  // returns -9876543210L
             *
             * // Edge: all zeros
             * Points.D3.LongLongPoint p3 = Points.D3.LongLongPoint.of(0L, 0L, 0L, 0L);
             * p3.value();  // returns 0L
             *
             * // Edge: Long boundary values for all fields
             * Points.D3.LongLongPoint p4 = Points.D3.LongLongPoint.of(Long.MIN_VALUE, Long.MAX_VALUE, 0L, Long.MIN_VALUE);
             * p4.x();      // returns -9223372036854775808L
             * p4.y();      // returns 9223372036854775807L
             * p4.value();  // returns -9223372036854775808L
             *
             * Points.D3.LongLongPoint p5 = Points.D3.LongLongPoint.of(1L, 2L, 3L, Long.MAX_VALUE);
             * p5.value();  // returns 9223372036854775807L
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the long value associated with this point
             * @return a new LongLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongLongPoint of(final long x, final long y, final long z, final long value) {
                return new LongLongPoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with long coordinates and a double-precision floating-point value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value requires floating-point precision.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record LongDoublePoint(long x, long y, long z, double value) {

            /**
             * Creates a new LongDoublePoint with the specified coordinates and value.
             * This factory method is ideal when working with very large 3D coordinate spaces requiring
             * long values but needing floating-point precision for the associated value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: large long coordinates with double value
             * Points.D3.LongDoublePoint p1 = Points.D3.LongDoublePoint.of(1000000L, 2000000L, 3000000L, 3.14159);
             * p1.x();      // returns 1000000L
             * p1.y();      // returns 2000000L
             * p1.z();      // returns 3000000L
             * p1.value();  // returns 3.14159
             *
             * // Basic: negative long coordinates with fractional value
             * Points.D3.LongDoublePoint p2 = Points.D3.LongDoublePoint.of(-1000000L, -2000000L, -3000000L, 0.85);
             * p2.x();      // returns -1000000L
             * p2.value();  // returns 0.85
             *
             * // Edge: zero coordinates and zero double value
             * Points.D3.LongDoublePoint p3 = Points.D3.LongDoublePoint.of(0L, 0L, 0L, 0.0);
             * p3.value();  // returns 0.0
             *
             * // Edge: Long boundary coordinates and special double values
             * Points.D3.LongDoublePoint p4 = Points.D3.LongDoublePoint.of(Long.MIN_VALUE, Long.MAX_VALUE, 0L, Double.NaN);
             * Double.isNaN(p4.value());  // returns true
             *
             * Points.D3.LongDoublePoint p5 = Points.D3.LongDoublePoint.of(1L, 2L, 3L, Double.POSITIVE_INFINITY);
             * p5.value();  // returns Double.POSITIVE_INFINITY
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the double value associated with this point
             * @return a new LongDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongDoublePoint of(final long x, final long y, final long z, final double value) {
                return new LongDoublePoint(x, y, z, value);
            }
        }

        /**
         * Represents a three-dimensional point with long coordinates and an object payload.
         * This record is a shallowly immutable carrier: the coordinates are fixed, while the
         * referenced value is stored as supplied and may itself be mutable.
         *
         * <p>Instances are thread-safe only when the referenced value is thread-safe or otherwise
         * not mutated concurrently.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record LongObjPoint<T>(long x, long y, long z, T value) {

            /**
             * Creates a new LongObjPoint with the specified coordinates and value reference.
             * The supplied value is stored as-is; this factory does not clone or otherwise protect
             * mutable payloads.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: long coordinates with a String value
             * Points.D3.LongObjPoint<String> p1 = Points.D3.LongObjPoint.of(1000000L, 2000000L, 3000000L, "region");
             * p1.x();      // returns 1000000L
             * p1.y();      // returns 2000000L
             * p1.z();      // returns 3000000L
             * p1.value();  // returns "region"
             *
             * // Basic: negative long coordinates with Integer wrapper value
             * Points.D3.LongObjPoint<Integer> p2 = Points.D3.LongObjPoint.of(-1000000L, -2000000L, -3000000L, 42);
             * p2.x();      // returns -1000000L
             * p2.value();  // returns 42
             *
             * // Edge: Long boundary coordinates and null value
             * Points.D3.LongObjPoint<String> p3 = Points.D3.LongObjPoint.of(Long.MIN_VALUE, Long.MAX_VALUE, 0L, null);
             * p3.x();      // returns -9223372036854775808L
             * p3.y();      // returns 9223372036854775807L
             * p3.value();  // returns null
             *
             * // Edge: zero coordinates and empty string
             * Points.D3.LongObjPoint<String> p4 = Points.D3.LongObjPoint.of(0L, 0L, 0L, "");
             * p4.value();  // returns ""
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the object value associated with this point, may be {@code null}
             * @return a new LongObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> LongObjPoint<T> of(final long x, final long y, final long z, final T value) {
                return new LongObjPoint<>(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with double-precision floating-point coordinates and a byte value.
         * This class is useful when coordinates require floating-point precision
         * but the associated value is constrained to byte range (-128 to 127).
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record DoubleBytePoint(double x, double y, double z, byte value) {

            /**
             * Creates a new DoubleBytePoint with the specified coordinates and value.
             * This factory method is ideal when working with floating-point 3D coordinate spaces but the
             * associated value fits within a byte range, optimizing memory for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: double coordinates with positive byte value
             * Points.D3.DoubleBytePoint p1 = Points.D3.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 3);
             * p1.x();      // returns 10.5
             * p1.y();      // returns 20.7
             * p1.z();      // returns 30.9
             * p1.value();  // returns (byte) 3
             *
             * // Basic: negative double coordinates with negative byte value
             * Points.D3.DoubleBytePoint p2 = Points.D3.DoubleBytePoint.of(-3.14159, -2.71828, -1.41421, (byte) -5);
             * p2.x();      // returns -3.14159
             * p2.value();  // returns (byte) -5
             *
             * // Edge: zero double coordinates and zero byte value
             * Points.D3.DoubleBytePoint p3 = Points.D3.DoubleBytePoint.of(0.0, 0.0, 0.0, (byte) 0);
             * p3.value();  // returns (byte) 0
             *
             * // Edge: special double coordinates (NaN/Infinity) and byte boundary values
             * Points.D3.DoubleBytePoint p4 = Points.D3.DoubleBytePoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN, Byte.MIN_VALUE);
             * p4.value();            // returns (byte) -128
             * Double.isNaN(p4.z());  // returns true
             *
             * Points.D3.DoubleBytePoint p5 = Points.D3.DoubleBytePoint.of(1.0, 2.0, 3.0, Byte.MAX_VALUE);
             * p5.value();  // returns (byte) 127
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the byte value associated with this point
             * @return a new DoubleBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleBytePoint of(final double x, final double y, final double z, final byte value) {
                return new DoubleBytePoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with double-precision floating-point coordinates and an integer value.
         * This class is useful when coordinates require floating-point precision
         * but the associated value fits within the integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record DoubleIntPoint(double x, double y, double z, int value) {

            /**
             * Creates a new DoubleIntPoint with the specified coordinates and value.
             * This factory method is ideal when working with floating-point 3D coordinate spaces but the
             * associated value fits within the integer range, such as counts or indices in continuous 3D spaces.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: double coordinates with integer value
             * Points.D3.DoubleIntPoint p1 = Points.D3.DoubleIntPoint.of(10.5, 20.7, 30.9, 100);
             * p1.x();      // returns 10.5
             * p1.y();      // returns 20.7
             * p1.z();      // returns 30.9
             * p1.value();  // returns 100
             *
             * // Basic: negative double coordinates with negative integer value
             * Points.D3.DoubleIntPoint p2 = Points.D3.DoubleIntPoint.of(-3.14159, -2.71828, -1.41421, -500);
             * p2.x();      // returns -3.14159
             * p2.value();  // returns -500
             *
             * // Edge: zero coordinates and zero integer value
             * Points.D3.DoubleIntPoint p3 = Points.D3.DoubleIntPoint.of(0.0, 0.0, 0.0, 0);
             * p3.value();  // returns 0
             *
             * // Edge: special double coordinates and Integer boundary values
             * Points.D3.DoubleIntPoint p4 = Points.D3.DoubleIntPoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0, Integer.MIN_VALUE);
             * p4.value();  // returns -2147483648
             * p4.x();      // returns Double.POSITIVE_INFINITY
             *
             * Points.D3.DoubleIntPoint p5 = Points.D3.DoubleIntPoint.of(1.0, 2.0, 3.0, Integer.MAX_VALUE);
             * p5.value();  // returns 2147483647
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the integer value associated with this point
             * @return a new DoubleIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleIntPoint of(final double x, final double y, final double z, final int value) {
                return new DoubleIntPoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with double-precision floating-point coordinates and a long value.
         * This class is useful when coordinates require floating-point precision
         * but the associated value requires the full long integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record DoubleLongPoint(double x, double y, double z, long value) {

            /**
             * Creates a new DoubleLongPoint with the specified coordinates and value.
             * This factory method is ideal when working with floating-point 3D coordinate spaces and requiring
             * long values for timestamps, large identifiers, or counts that exceed the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: double coordinates with large long value
             * Points.D3.DoubleLongPoint p1 = Points.D3.DoubleLongPoint.of(10.5, 20.7, 30.9, 1000000000L);
             * p1.x();      // returns 10.5
             * p1.y();      // returns 20.7
             * p1.z();      // returns 30.9
             * p1.value();  // returns 1000000000L
             *
             * // Basic: negative double coordinates with large long value
             * Points.D3.DoubleLongPoint p2 = Points.D3.DoubleLongPoint.of(-3.14159, -2.71828, -1.41421, 9876543210L);
             * p2.x();      // returns -3.14159
             * p2.value();  // returns 9876543210L
             *
             * // Edge: zero coordinates and zero long value
             * Points.D3.DoubleLongPoint p3 = Points.D3.DoubleLongPoint.of(0.0, 0.0, 0.0, 0L);
             * p3.value();  // returns 0L
             *
             * // Edge: special double coordinates and Long boundary values
             * Points.D3.DoubleLongPoint p4 = Points.D3.DoubleLongPoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0, Long.MIN_VALUE);
             * p4.value();  // returns -9223372036854775808L
             * p4.x();      // returns Double.POSITIVE_INFINITY
             *
             * Points.D3.DoubleLongPoint p5 = Points.D3.DoubleLongPoint.of(1.0, 2.0, 3.0, Long.MAX_VALUE);
             * p5.value();  // returns 9223372036854775807L
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the long value associated with this point
             * @return a new DoubleLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleLongPoint of(final double x, final double y, final double z, final long value) {
                return new DoubleLongPoint(x, y, z, value);
            }
        }

        /**
         * Represents an immutable three-dimensional point with double-precision floating-point coordinates and a double-precision floating-point value.
         * This class provides full double-precision floating-point support for all three coordinates and the associated value.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record DoubleDoublePoint(double x, double y, double z, double value) {

            /**
             * Creates a new DoubleDoublePoint with the specified coordinates and value.
             * This factory method provides full double-precision floating-point support for all three coordinates
             * and the associated value, ideal for continuous mathematical, physical, and scientific 3D applications.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: typical double coordinates and double value
             * Points.D3.DoubleDoublePoint p1 = Points.D3.DoubleDoublePoint.of(10.5, 20.7, 30.9, 3.14159);
             * p1.x();      // returns 10.5
             * p1.y();      // returns 20.7
             * p1.z();      // returns 30.9
             * p1.value();  // returns 3.14159
             *
             * // Basic: negative double coordinates and fractional value
             * Points.D3.DoubleDoublePoint p2 = Points.D3.DoubleDoublePoint.of(-3.14159, -2.71828, -1.41421, 0.95);
             * p2.x();      // returns -3.14159
             * p2.value();  // returns 0.95
             *
             * // Edge: all zero coordinates and zero value
             * Points.D3.DoubleDoublePoint p3 = Points.D3.DoubleDoublePoint.of(0.0, 0.0, 0.0, 0.0);
             * p3.value();  // returns 0.0
             *
             * // Edge: special double values (NaN, Infinity) for both coords and value
             * Points.D3.DoubleDoublePoint p4 = Points.D3.DoubleDoublePoint.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN);
             * Double.isNaN(p4.x());      // returns true
             * Double.isNaN(p4.value());  // returns true
             *
             * Points.D3.DoubleDoublePoint p5 = Points.D3.DoubleDoublePoint.of(1.0, 2.0, 3.0, Double.NEGATIVE_INFINITY);
             * p5.value();  // returns Double.NEGATIVE_INFINITY
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the double value associated with this point
             * @return a new DoubleDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleDoublePoint of(final double x, final double y, final double z, final double value) {
                return new DoubleDoublePoint(x, y, z, value);
            }
        }

        /**
         * Represents a three-dimensional point with double-precision floating-point coordinates and an object payload.
         * This record is a shallowly immutable carrier: the coordinates are fixed, while the
         * referenced value is stored as supplied and may itself be mutable.
         *
         * <p>Instances are thread-safe only when the referenced value is thread-safe or otherwise
         * not mutated concurrently.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param value the value associated with this point
         */
        public record DoubleObjPoint<T>(double x, double y, double z, T value) {

            /**
             * Creates a new DoubleObjPoint with the specified coordinates and value reference.
             * The supplied value is stored as-is; this factory does not clone or otherwise protect
             * mutable payloads.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Basic: double coordinates with a String value
             * Points.D3.DoubleObjPoint<String> p1 = Points.D3.DoubleObjPoint.of(10.5, 20.7, 30.9, "marker");
             * p1.x();      // returns 10.5
             * p1.y();      // returns 20.7
             * p1.z();      // returns 30.9
             * p1.value();  // returns "marker"
             *
             * // Basic: negative double coordinates with Integer wrapper value
             * Points.D3.DoubleObjPoint<Integer> p2 = Points.D3.DoubleObjPoint.of(-3.14159, -2.71828, -1.41421, 99);
             * p2.x();      // returns -3.14159
             * p2.value();  // returns 99
             *
             * // Edge: double coordinates and null value
             * Points.D3.DoubleObjPoint<String> p3 = Points.D3.DoubleObjPoint.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN, null);
             * p3.value();            // returns null
             * Double.isNaN(p3.z());  // returns true
             *
             * // Edge: zero coordinates and empty string value
             * Points.D3.DoubleObjPoint<String> p4 = Points.D3.DoubleObjPoint.of(0.0, 0.0, 0.0, "");
             * p4.value();  // returns ""
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param value the object value associated with this point, may be {@code null}
             * @return a new DoubleObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> DoubleObjPoint<T> of(final double x, final double y, final double z, final T value) {
                return new DoubleObjPoint<>(x, y, z, value);
            }
        }
    }
}
