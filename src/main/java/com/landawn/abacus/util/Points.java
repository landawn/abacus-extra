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
 * int x = point.x; // 10
 * int y = point.y; // 20
 * int value = point.v; // 100
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteBytePoint implements Immutable {

            /** The x-coordinate of this point. */
            public final byte x;

            /** The y-coordinate of this point. */
            public final byte y;

            /** The value associated with this point. */
            public final byte v; // value

            /**
             * Constructs a new ByteBytePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            ByteBytePoint(final byte x, final byte y, final byte v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new ByteBytePoint with the specified x, y coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteBytePoint point = ByteBytePoint.of((byte)10, (byte)20, (byte)100);
             * byte x = point.x; // 10
             * byte y = point.y; // 20
             * byte value = point.v; // 100
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final ByteBytePoint other) {
                    return x == other.x && y == other.y && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteIntPoint implements Immutable {

            /** The x-coordinate of this point. */
            public final byte x;

            /** The y-coordinate of this point. */
            public final byte y;

            /** The value associated with this point. */
            public final int v; // value

            /**
             * Constructs a new ByteIntPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            ByteIntPoint(final byte x, final byte y, final int v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new ByteIntPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteIntPoint point = ByteIntPoint.of((byte)10, (byte)20, 100);
             * byte x = point.x; // 10
             * byte y = point.y; // 20
             * int value = point.v; // 100
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final ByteIntPoint other) {
                    return x == other.x && y == other.y && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteLongPoint implements Immutable {

            /** The x-coordinate of this point. */
            public final byte x;

            /** The y-coordinate of this point. */
            public final byte y;

            /** The value associated with this point. */
            public final long v; // value

            /**
             * Constructs a new ByteLongPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            ByteLongPoint(final byte x, final byte y, final long v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new ByteLongPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteLongPoint point = ByteLongPoint.of((byte)10, (byte)20, 100L);
             * byte x = point.x; // 10
             * byte y = point.y; // 20
             * long value = point.v; // 100L
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final ByteLongPoint other) {
                    return x == other.x && y == other.y && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteDoublePoint implements Immutable {

            /** The x-coordinate of this point. */
            public final byte x;

            /** The y-coordinate of this point. */
            public final byte y;

            /** The value associated with this point. */
            public final double v; // value

            /**
             * Constructs a new ByteDoublePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            ByteDoublePoint(final byte x, final byte y, final double v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new ByteDoublePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteDoublePoint point = ByteDoublePoint.of((byte)10, (byte)20, 100.5);
             * byte x = point.x; // 10
             * byte y = point.y; // 20
             * double value = point.v; // 100.5
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
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
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteObjPoint<T> implements Immutable {

            /** The x-coordinate of this point. */
            public final byte x;

            /** The y-coordinate of this point. */
            public final byte y;

            /** The value associated with this point. */
            public final T v; // value

            /**
             * Constructs a new ByteObjPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            ByteObjPoint(final byte x, final byte y, final T v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new ByteObjPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * ByteObjPoint<String> point = ByteObjPoint.of((byte)5, (byte)10, "marker");
             * byte x = point.x; // 5
             * byte y = point.y; // 10
             * String value = point.v; // "marker"
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + N.hashCode(v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
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
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntBytePoint implements Immutable {

            /** The x-coordinate of this point. */
            public final int x;

            /** The y-coordinate of this point. */
            public final int y;

            /** The value associated with this point. */
            public final byte v; // value

            /**
             * Constructs a new IntBytePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            IntBytePoint(final int x, final int y, final byte v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new IntBytePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntBytePoint point = IntBytePoint.of(100, 200, (byte)50);
             * int x = point.x; // 100
             * int y = point.y; // 200
             * byte value = point.v; // 50
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final IntBytePoint other) {
                    return x == other.x && y == other.y && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntIntPoint implements Immutable {

            /** The x-coordinate of this point. */
            public final int x;

            /** The y-coordinate of this point. */
            public final int y;

            /** The value associated with this point. */
            public final int v; // value

            /**
             * Constructs a new IntIntPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            IntIntPoint(final int x, final int y, final int v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new IntIntPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntIntPoint point = IntIntPoint.of(100, 200, 300);
             * int x = point.x; // 100
             * int y = point.y; // 200
             * int value = point.v; // 300
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final IntIntPoint other) {
                    return x == other.x && y == other.y && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntLongPoint implements Immutable {

            /** The x-coordinate of this point. */
            public final int x;

            /** The y-coordinate of this point. */
            public final int y;

            /** The value associated with this point. */
            public final long v; // value

            /**
             * Constructs a new IntLongPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            IntLongPoint(final int x, final int y, final long v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new IntLongPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntLongPoint point = IntLongPoint.of(100, 200, 1000000L);
             * int x = point.x; // 100
             * int y = point.y; // 200
             * long value = point.v; // 1000000L
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final IntLongPoint other) {
                    return x == other.x && y == other.y && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntDoublePoint implements Immutable {

            /** The x-coordinate of this point. */
            public final int x;

            /** The y-coordinate of this point. */
            public final int y;

            /** The value associated with this point. */
            public final double v; // value

            /**
             * Constructs a new IntDoublePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            IntDoublePoint(final int x, final int y, final double v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new IntDoublePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntDoublePoint point = IntDoublePoint.of(100, 200, 3.14159);
             * int x = point.x; // 100
             * int y = point.y; // 200
             * double value = point.v; // 3.14159
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
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
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntObjPoint<T> implements Immutable {

            /** The x-coordinate of this point. */
            public final int x;

            /** The y-coordinate of this point. */
            public final int y;

            /** The value associated with this point. */
            public final T v; // value

            /**
             * Constructs a new IntObjPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            IntObjPoint(final int x, final int y, final T v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new IntObjPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * IntObjPoint<String> point = IntObjPoint.of(10, 20, "marker");
             * int x = point.x; // 10
             * int y = point.y; // 20
             * String value = point.v; // "marker"
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + N.hashCode(v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
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
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with long integer coordinates and a byte value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value is constrained to byte range (-128 to 127).
         *
         * <p>All instances are immutable and thread-safe.</p>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongBytePoint implements Immutable {

            /** The x-coordinate of this point. */
            public final long x;

            /** The y-coordinate of this point. */
            public final long y;

            /** The value associated with this point. */
            public final byte v; // value

            /**
             * Constructs a new LongBytePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            LongBytePoint(final long x, final long y, final byte v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new LongBytePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongBytePoint point = LongBytePoint.of(1000L, 2000L, (byte)50);
             * long x = point.x; // 1000L
             * long y = point.y; // 2000L
             * byte value = point.v; // 50
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final LongBytePoint other) {
                    return x == other.x && y == other.y && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with long integer coordinates and an integer value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value fits within the integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongIntPoint implements Immutable {

            /** The x-coordinate of this point. */
            public final long x;

            /** The y-coordinate of this point. */
            public final long y;

            /** The value associated with this point. */
            public final int v; // value

            /**
             * Constructs a new LongIntPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            LongIntPoint(final long x, final long y, final int v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new LongIntPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongIntPoint point = LongIntPoint.of(1000L, 2000L, 500);
             * long x = point.x; // 1000L
             * long y = point.y; // 2000L
             * int value = point.v; // 500
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final LongIntPoint other) {
                    return x == other.x && y == other.y && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with long integer coordinates and a long value.
         * This class is useful for large-scale coordinate systems where both coordinates
         * and values require the full long integer range.
         *
         * <p>All instances are immutable and thread-safe.</p>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongLongPoint implements Immutable {

            /** The x-coordinate of this point. */
            public final long x;

            /** The y-coordinate of this point. */
            public final long y;

            /** The value associated with this point. */
            public final long v; // value

            /**
             * Constructs a new LongLongPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            LongLongPoint(final long x, final long y, final long v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new LongLongPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongLongPoint point = LongLongPoint.of(1000L, 2000L, 3000L);
             * long x = point.x; // 1000L
             * long y = point.y; // 2000L
             * long value = point.v; // 3000L
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
             *
             * @param obj the reference object with which to compare
             * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final LongLongPoint other) {
                    return x == other.x && y == other.y && v == other.v;
                }

                return false;
            }

            /**
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with long integer coordinates and a double-precision floating-point value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value requires floating-point precision.
         *
         * <p>All instances are immutable and thread-safe.</p>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongDoublePoint implements Immutable {

            /** The x-coordinate of this point. */
            public final long x;

            /** The y-coordinate of this point. */
            public final long y;

            /** The value associated with this point. */
            public final double v; // value

            /**
             * Constructs a new LongDoublePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            LongDoublePoint(final long x, final long y, final double v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new LongDoublePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongDoublePoint point = LongDoublePoint.of(1000L, 2000L, 99.99);
             * long x = point.x; // 1000L
             * long y = point.y; // 2000L
             * double value = point.v; // 99.99
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
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
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with long integer coordinates and a generic object value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value can be any object type.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param <T> the type of the value object associated with this point
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongObjPoint<T> implements Immutable {

            /** The x-coordinate of this point. */
            public final long x;

            /** The y-coordinate of this point. */
            public final long y;

            /** The value associated with this point. */
            public final T v; // value

            /**
             * Constructs a new LongObjPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            LongObjPoint(final long x, final long y, final T v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new LongObjPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * LongObjPoint<String> point = LongObjPoint.of(1000L, 2000L, "marker");
             * long x = point.x; // 1000L
             * long y = point.y; // 2000L
             * String value = point.v; // "marker"
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + N.hashCode(v));
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
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
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleBytePoint implements Immutable {

            /** The x-coordinate of this point. */
            public final double x;

            /** The y-coordinate of this point. */
            public final double y;

            /** The value associated with this point. */
            public final byte v; // value

            /**
             * Constructs a new DoubleBytePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            DoubleBytePoint(final double x, final double y, final byte v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new DoubleBytePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleBytePoint point = DoubleBytePoint.of(10.5, 20.3, (byte)100);
             * double x = point.x; // 10.5
             * double y = point.y; // 20.3
             * byte value = point.v; // 100
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
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
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleIntPoint implements Immutable {

            /** The x-coordinate of this point. */
            public final double x;

            /** The y-coordinate of this point. */
            public final double y;

            /** The value associated with this point. */
            public final int v; // value

            /**
             * Constructs a new DoubleIntPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            DoubleIntPoint(final double x, final double y, final int v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new DoubleIntPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleIntPoint point = DoubleIntPoint.of(10.5, 20.3, 500);
             * double x = point.x; // 10.5
             * double y = point.y; // 20.3
             * int value = point.v; // 500
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
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
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleLongPoint implements Immutable {

            /** The x-coordinate of this point. */
            public final double x;

            /** The y-coordinate of this point. */
            public final double y;

            /** The value associated with this point. */
            public final long v; // value

            /**
             * Constructs a new DoubleLongPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            DoubleLongPoint(final double x, final double y, final long v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new DoubleLongPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleLongPoint point = DoubleLongPoint.of(10.5, 20.3, 1000000L);
             * double x = point.x; // 10.5
             * double y = point.y; // 20.3
             * long value = point.v; // 1000000L
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
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
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * Represents an immutable two-dimensional point with double-precision floating-point coordinates and a double value.
         * This class is useful for high-precision floating-point coordinate systems and scientific computations
         * where both coordinates and values require double precision.
         *
         * <p>All instances are immutable and thread-safe.</p>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleDoublePoint implements Immutable {

            /** The x-coordinate of this point. */
            public final double x;

            /** The y-coordinate of this point. */
            public final double y;

            /** The value associated with this point. */
            public final double v; // value

            /**
             * Constructs a new DoubleDoublePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            DoubleDoublePoint(final double x, final double y, final double v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new DoubleDoublePoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleDoublePoint point = DoubleDoublePoint.of(10.5, 20.3, 3.14159);
             * double x = point.x; // 10.5
             * double y = point.y; // 20.3
             * double value = point.v; // 3.14159
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
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
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
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
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleObjPoint<T> implements Immutable {

            /** The x-coordinate of this point. */
            public final double x;

            /** The y-coordinate of this point. */
            public final double y;

            /** The value associated with this point. */
            public final T v; // value

            /**
             * Constructs a new DoubleObjPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             */
            DoubleObjPoint(final double x, final double y, final T v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             * Creates a new DoubleObjPoint with the specified coordinates and value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * DoubleObjPoint<String> point = DoubleObjPoint.of(10.5, 20.3, "marker");
             * double x = point.x; // 10.5
             * double y = point.y; // 20.3
             * String value = point.v; // "marker"
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
             * @return a hash code value for this point
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + N.hashCode(v));
            }

            /**
             * Indicates whether some other object is \"equal to\" this one.
             * Two points are equal if they have the same coordinates and value.
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
             * Returns a string representation of this point in the format \"[x, y, v]\".
             *
             * @return a string representation of this point
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
            }
        }

        //        public static final class ByteByteTimePoint  implements Immutable {
        //            public final byte x;
        //             public final byte y;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final ByteByteTimePoint() {
        //            }
        //
        //            public final ByteByteTimePoint(byte x, byte y, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ByteByteTimePoint of(byte x, byte y, long t, byte v) {
        //                return new ByteByteTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ByteByteTimePoint) {
        //                    ByteByteTimePoint other = (ByteByteTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ByteIntTimePoint implements Immutable {
        //            public final byte x;
        //             public final byte y;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final ByteIntTimePoint() {
        //            }
        //
        //            public final ByteIntTimePoint(byte x, byte y, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ByteIntTimePoint of(byte x, byte y, long t, int v) {
        //                return new ByteIntTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ByteIntTimePoint) {
        //                    ByteIntTimePoint other = (ByteIntTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ByteLongTimePoint implements Immutable {
        //            public final byte x;
        //             public final byte y;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final ByteLongTimePoint() {
        //            }
        //
        //            public final ByteLongTimePoint(byte x, byte y, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ByteLongTimePoint of(byte x, byte y, long t, long v) {
        //                return new ByteLongTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ByteLongTimePoint) {
        //                    ByteLongTimePoint other = (ByteLongTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ByteDoubleTimePoint implements Immutable {
        //            public final byte x;
        //             public final byte y;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final ByteDoubleTimePoint() {
        //            }
        //
        //            public final ByteDoubleTimePoint(byte x, byte y, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ByteDoubleTimePoint of(byte x, byte y, long t, double v) {
        //                return new ByteDoubleTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ByteDoubleTimePoint) {
        //                    ByteDoubleTimePoint other = (ByteDoubleTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ByteObjTimePoint<T> implements Immutable {
        //            public final byte x;
        //             public final byte y;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final ByteObjTimePoint() {
        //            }
        //
        //            public final ByteObjTimePoint(byte x, byte y, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ByteObjTimePoint<T> of(byte x, byte y, long t, T v) {
        //                return new ByteObjTimePoint<>(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + t) * 31 + N.hashCode(v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ByteObjTimePoint) {
        //                    ByteObjTimePoint<?> other = (ByteObjTimePoint<?>) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class IntByteTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final IntByteTimePoint() {
        //            }
        //
        //            public final IntByteTimePoint(int x, int y, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static IntByteTimePoint of(int x, int y, long t, byte v) {
        //                return new IntByteTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof IntByteTimePoint) {
        //                    IntByteTimePoint other = (IntByteTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class IntIntTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final IntIntTimePoint() {
        //            }
        //
        //            public final IntIntTimePoint(int x, int y, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static IntIntTimePoint of(int x, int y, long t, int v) {
        //                return new IntIntTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof IntIntTimePoint) {
        //                    IntIntTimePoint other = (IntIntTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class IntLongTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final IntLongTimePoint() {
        //            }
        //
        //            public final IntLongTimePoint(int x, int y, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static IntLongTimePoint of(int x, int y, long t, long v) {
        //                return new IntLongTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof IntLongTimePoint) {
        //                    IntLongTimePoint other = (IntLongTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class IntDoubleTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final IntDoubleTimePoint() {
        //            }
        //
        //            public final IntDoubleTimePoint(int x, int y, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static IntDoubleTimePoint of(int x, int y, long t, double v) {
        //                return new IntDoubleTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof IntDoubleTimePoint) {
        //                    IntDoubleTimePoint other = (IntDoubleTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class IntObjTimePoint<T> implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final IntObjTimePoint() {
        //            }
        //
        //            public final IntObjTimePoint(int x, int y, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> IntObjTimePoint<T> of(int x, int y, long t, T v) {
        //                return new IntObjTimePoint<>(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + t) * 31 + N.hashCode(v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof IntObjTimePoint) {
        //                    IntObjTimePoint<?> other = (IntObjTimePoint<?>) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class LongByteTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final LongByteTimePoint() {
        //            }
        //
        //            public final LongByteTimePoint(long x, long y, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static LongByteTimePoint of(long x, long y, long t, byte v) {
        //                return new LongByteTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof LongByteTimePoint) {
        //                    LongByteTimePoint other = (LongByteTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class LongIntTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final LongIntTimePoint() {
        //            }
        //
        //            public final LongIntTimePoint(long x, long y, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static LongIntTimePoint of(long x, long y, long t, int v) {
        //                return new LongIntTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof LongIntTimePoint) {
        //                    LongIntTimePoint other = (LongIntTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class LongLongTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final LongLongTimePoint() {
        //            }
        //
        //            public final LongLongTimePoint(long x, long y, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static LongLongTimePoint of(long x, long y, long t, long v) {
        //                return new LongLongTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof LongLongTimePoint) {
        //                    LongLongTimePoint other = (LongLongTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class LongDoubleTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final LongDoubleTimePoint() {
        //            }
        //
        //            public final LongDoubleTimePoint(long x, long y, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static LongDoubleTimePoint of(long x, long y, long t, double v) {
        //                return new LongDoubleTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof LongDoubleTimePoint) {
        //                    LongDoubleTimePoint other = (LongDoubleTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class LongObjTimePoint<T> implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final LongObjTimePoint() {
        //            }
        //
        //            public final LongObjTimePoint(long x, long y, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> LongObjTimePoint<T> of(long x, long y, long t, T v) {
        //                return new LongObjTimePoint<>(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + N.hashCode(v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof LongObjTimePoint) {
        //                    LongObjTimePoint<?> other = (LongObjTimePoint<?>) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class DoubleByteTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final DoubleByteTimePoint() {
        //            }
        //
        //            public final DoubleByteTimePoint(double x, double y, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static DoubleByteTimePoint of(double x, double y, long t, byte v) {
        //                return new DoubleByteTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof DoubleByteTimePoint) {
        //                    DoubleByteTimePoint other = (DoubleByteTimePoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class DoubleIntTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final DoubleIntTimePoint() {
        //            }
        //
        //            public final DoubleIntTimePoint(double x, double y, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static DoubleIntTimePoint of(double x, double y, long t, int v) {
        //                return new DoubleIntTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof DoubleIntTimePoint) {
        //                    DoubleIntTimePoint other = (DoubleIntTimePoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class DoubleLongTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final DoubleLongTimePoint() {
        //            }
        //
        //            public final DoubleLongTimePoint(double x, double y, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static DoubleLongTimePoint of(double x, double y, long t, long v) {
        //                return new DoubleLongTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof DoubleLongTimePoint) {
        //                    DoubleLongTimePoint other = (DoubleLongTimePoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class DoubleDoubleTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final DoubleDoubleTimePoint() {
        //            }
        //
        //            public final DoubleDoubleTimePoint(double x, double y, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static DoubleDoubleTimePoint of(double x, double y, long t, double v) {
        //                return new DoubleDoubleTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof DoubleDoubleTimePoint) {
        //                    DoubleDoubleTimePoint other = (DoubleDoubleTimePoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class DoubleObjTimePoint<T> implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final DoubleObjTimePoint() {
        //            }
        //
        //            public final DoubleObjTimePoint(double x, double y, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> DoubleObjTimePoint<T> of(double x, double y, long t, T v) {
        //                return new DoubleObjTimePoint<>(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + N.hashCode(v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof DoubleObjTimePoint) {
        //                    DoubleObjTimePoint<?> other = (DoubleObjTimePoint<?>) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteBytePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final byte v; // value
        //
        //            public final ImmutableByteBytePoint(byte x, byte y, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteBytePoint of(byte x, byte y, byte v) {
        //                return new ImmutableByteBytePoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (x * 31 + y) * 31 + v;
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableByteBytePoint) {
        //                    ImmutableByteBytePoint other = (ImmutableByteBytePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteIntPoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final int v; // value
        //
        //            public final ImmutableByteIntPoint(byte x, byte y, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteIntPoint of(byte x, byte y, int v) {
        //                return new ImmutableByteIntPoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (x * 31 + y) * 31 + v;
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableByteIntPoint) {
        //                    ImmutableByteIntPoint other = (ImmutableByteIntPoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteLongPoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final long v; // value
        //
        //            public final ImmutableByteLongPoint(byte x, byte y, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteLongPoint of(byte x, byte y, long v) {
        //                return new ImmutableByteLongPoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableByteLongPoint) {
        //                    ImmutableByteLongPoint other = (ImmutableByteLongPoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteDoublePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final double v; // value
        //
        //            public final ImmutableByteDoublePoint(byte x, byte y, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteDoublePoint of(byte x, byte y, double v) {
        //                return new ImmutableByteDoublePoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableByteDoublePoint) {
        //                    ImmutableByteDoublePoint other = (ImmutableByteDoublePoint) obj;
        //                    return this.x == other.x && this.y == other.y && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteObjPoint<T> implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final T v; // value
        //
        //            public final ImmutableByteObjPoint(byte x, byte y, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableByteObjPoint<T> of(byte x, byte y, T v) {
        //                return new ImmutableByteObjPoint<>(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (x * 31 + y) * 31 + N.hashCode(v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableByteObjPoint) {
        //                    ImmutableByteObjPoint<?> other = (ImmutableByteObjPoint<?>) obj;
        //                    return this.x == other.x && this.y == other.y && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntBytePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final byte v; // value
        //
        //            public final ImmutableIntBytePoint(int x, int y, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntBytePoint of(int x, int y, byte v) {
        //                return new ImmutableIntBytePoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (x * 31 + y) * 31 + v;
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableIntBytePoint) {
        //                    ImmutableIntBytePoint other = (ImmutableIntBytePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntIntPoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int v; // value
        //
        //            public final ImmutableIntIntPoint(int x, int y, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntIntPoint of(int x, int y, int v) {
        //                return new ImmutableIntIntPoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (x * 31 + y) * 31 + v;
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableIntIntPoint) {
        //                    ImmutableIntIntPoint other = (ImmutableIntIntPoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntLongPoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final long v; // value
        //
        //            public final ImmutableIntLongPoint(int x, int y, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntLongPoint of(int x, int y, long v) {
        //                return new ImmutableIntLongPoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableIntLongPoint) {
        //                    ImmutableIntLongPoint other = (ImmutableIntLongPoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntDoublePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final double v; // value
        //
        //            public final ImmutableIntDoublePoint(int x, int y, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntDoublePoint of(int x, int y, double v) {
        //                return new ImmutableIntDoublePoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableIntDoublePoint) {
        //                    ImmutableIntDoublePoint other = (ImmutableIntDoublePoint) obj;
        //                    return this.x == other.x && this.y == other.y && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntObjPoint<T> implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final T v; // value
        //
        //            public final ImmutableIntObjPoint(int x, int y, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableIntObjPoint<T> of(int x, int y, T v) {
        //                return new ImmutableIntObjPoint<>(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (x * 31 + y) * 31 + N.hashCode(v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableIntObjPoint) {
        //                    ImmutableIntObjPoint<?> other = (ImmutableIntObjPoint<?>) obj;
        //                    return this.x == other.x && this.y == other.y && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongBytePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final byte v; // value
        //
        //            public final ImmutableLongBytePoint(long x, long y, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongBytePoint of(long x, long y, byte v) {
        //                return new ImmutableLongBytePoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableLongBytePoint) {
        //                    ImmutableLongBytePoint other = (ImmutableLongBytePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongIntPoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final int v; // value
        //
        //            public final ImmutableLongIntPoint(long x, long y, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongIntPoint of(long x, long y, int v) {
        //                return new ImmutableLongIntPoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableLongIntPoint) {
        //                    ImmutableLongIntPoint other = (ImmutableLongIntPoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongLongPoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long v; // value
        //
        //            public final ImmutableLongLongPoint(long x, long y, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongLongPoint of(long x, long y, long v) {
        //                return new ImmutableLongLongPoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableLongLongPoint) {
        //                    ImmutableLongLongPoint other = (ImmutableLongLongPoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongDoublePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final double v; // value
        //
        //            public final ImmutableLongDoublePoint(long x, long y, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongDoublePoint of(long x, long y, double v) {
        //                return new ImmutableLongDoublePoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableLongDoublePoint) {
        //                    ImmutableLongDoublePoint other = (ImmutableLongDoublePoint) obj;
        //                    return this.x == other.x && this.y == other.y && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongObjPoint<T> implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final T v; // value
        //
        //            public final ImmutableLongObjPoint(long x, long y, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableLongObjPoint<T> of(long x, long y, T v) {
        //                return new ImmutableLongObjPoint<>(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + N.hashCode(v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableLongObjPoint) {
        //                    ImmutableLongObjPoint<?> other = (ImmutableLongObjPoint<?>) obj;
        //                    return this.x == other.x && this.y == other.y && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleBytePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final byte v; // value
        //
        //            public final ImmutableDoubleBytePoint(double x, double y, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleBytePoint of(double x, double y, byte v) {
        //                return new ImmutableDoubleBytePoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableDoubleBytePoint) {
        //                    ImmutableDoubleBytePoint other = (ImmutableDoubleBytePoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleIntPoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final int v; // value
        //
        //            public final ImmutableDoubleIntPoint(double x, double y, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleIntPoint of(double x, double y, int v) {
        //                return new ImmutableDoubleIntPoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableDoubleIntPoint) {
        //                    ImmutableDoubleIntPoint other = (ImmutableDoubleIntPoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleLongPoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final long v; // value
        //
        //            public final ImmutableDoubleLongPoint(double x, double y, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleLongPoint of(double x, double y, long v) {
        //                return new ImmutableDoubleLongPoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableDoubleLongPoint) {
        //                    ImmutableDoubleLongPoint other = (ImmutableDoubleLongPoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleDoublePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double v; // value
        //
        //            public final ImmutableDoubleDoublePoint(double x, double y, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleDoublePoint of(double x, double y, double v) {
        //                return new ImmutableDoubleDoublePoint(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableDoubleDoublePoint) {
        //                    ImmutableDoubleDoublePoint other = (ImmutableDoubleDoublePoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleObjPoint<T> {
        //            public final double x;
        //            public final double y;
        //            public final T v; // value
        //
        //            public final ImmutableDoubleObjPoint(double x, double y, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableDoubleObjPoint<T> of(double x, double y, T v) {
        //                return new ImmutableDoubleObjPoint<>(x, y, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + N.hashCode(v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableDoubleObjPoint) {
        //                    ImmutableDoubleObjPoint<?> other = (ImmutableDoubleObjPoint<?>) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteByteTimePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final ImmutableByteByteTimePoint(byte x, byte y, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteByteTimePoint of(byte x, byte y, long t, byte v) {
        //                return new ImmutableByteByteTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableByteByteTimePoint) {
        //                    ImmutableByteByteTimePoint other = (ImmutableByteByteTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteIntTimePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final ImmutableByteIntTimePoint(byte x, byte y, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteIntTimePoint of(byte x, byte y, long t, int v) {
        //                return new ImmutableByteIntTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableByteIntTimePoint) {
        //                    ImmutableByteIntTimePoint other = (ImmutableByteIntTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteLongTimePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final ImmutableByteLongTimePoint(byte x, byte y, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteLongTimePoint of(byte x, byte y, long t, long v) {
        //                return new ImmutableByteLongTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableByteLongTimePoint) {
        //                    ImmutableByteLongTimePoint other = (ImmutableByteLongTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteDoubleTimePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final ImmutableByteDoubleTimePoint(byte x, byte y, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteDoubleTimePoint of(byte x, byte y, long t, double v) {
        //                return new ImmutableByteDoubleTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableByteDoubleTimePoint) {
        //                    ImmutableByteDoubleTimePoint other = (ImmutableByteDoubleTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteObjTimePoint<T> {
        //            public final byte x;
        //            public final byte y;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final ImmutableByteObjTimePoint(byte x, byte y, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableByteObjTimePoint<T> of(byte x, byte y, long t, T v) {
        //                return new ImmutableByteObjTimePoint<>(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + t) * 31 + N.hashCode(v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableByteObjTimePoint) {
        //                    ImmutableByteObjTimePoint<?> other = (ImmutableByteObjTimePoint<?>) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntByteTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final ImmutableIntByteTimePoint(int x, int y, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntByteTimePoint of(int x, int y, long t, byte v) {
        //                return new ImmutableIntByteTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableIntByteTimePoint) {
        //                    ImmutableIntByteTimePoint other = (ImmutableIntByteTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntIntTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final ImmutableIntIntTimePoint(int x, int y, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntIntTimePoint of(int x, int y, long t, int v) {
        //                return new ImmutableIntIntTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableIntIntTimePoint) {
        //                    ImmutableIntIntTimePoint other = (ImmutableIntIntTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntLongTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final ImmutableIntLongTimePoint(int x, int y, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntLongTimePoint of(int x, int y, long t, long v) {
        //                return new ImmutableIntLongTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableIntLongTimePoint) {
        //                    ImmutableIntLongTimePoint other = (ImmutableIntLongTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntDoubleTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final ImmutableIntDoubleTimePoint(int x, int y, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntDoubleTimePoint of(int x, int y, long t, double v) {
        //                return new ImmutableIntDoubleTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableIntDoubleTimePoint) {
        //                    ImmutableIntDoubleTimePoint other = (ImmutableIntDoubleTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntObjTimePoint<T> {
        //            public final int x;
        //            public final int y;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final ImmutableIntObjTimePoint(int x, int y, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableIntObjTimePoint<T> of(int x, int y, long t, T v) {
        //                return new ImmutableIntObjTimePoint<>(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((x * 31 + y) * 31 + t) * 31 + N.hashCode(v);
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableIntObjTimePoint) {
        //                    ImmutableIntObjTimePoint<?> other = (ImmutableIntObjTimePoint<?>) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongByteTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final ImmutableLongByteTimePoint(long x, long y, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongByteTimePoint of(long x, long y, long t, byte v) {
        //                return new ImmutableLongByteTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableLongByteTimePoint) {
        //                    ImmutableLongByteTimePoint other = (ImmutableLongByteTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongIntTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final ImmutableLongIntTimePoint(long x, long y, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongIntTimePoint of(long x, long y, long t, int v) {
        //                return new ImmutableLongIntTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableLongIntTimePoint) {
        //                    ImmutableLongIntTimePoint other = (ImmutableLongIntTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongLongTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final ImmutableLongLongTimePoint(long x, long y, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongLongTimePoint of(long x, long y, long t, long v) {
        //                return new ImmutableLongLongTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableLongLongTimePoint) {
        //                    ImmutableLongLongTimePoint other = (ImmutableLongLongTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongDoubleTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final ImmutableLongDoubleTimePoint(long x, long y, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongDoubleTimePoint of(long x, long y, long t, double v) {
        //                return new ImmutableLongDoubleTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableLongDoubleTimePoint) {
        //                    ImmutableLongDoubleTimePoint other = (ImmutableLongDoubleTimePoint) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongObjTimePoint<T> {
        //            public final long x;
        //            public final long y;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final ImmutableLongObjTimePoint(long x, long y, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableLongObjTimePoint<T> of(long x, long y, long t, T v) {
        //                return new ImmutableLongObjTimePoint<>(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + N.hashCode(v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableLongObjTimePoint) {
        //                    ImmutableLongObjTimePoint<?> other = (ImmutableLongObjTimePoint<?>) obj;
        //                    return this.x == other.x && this.y == other.y && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleByteTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final ImmutableDoubleByteTimePoint(double x, double y, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleByteTimePoint of(double x, double y, long t, byte v) {
        //                return new ImmutableDoubleByteTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableDoubleByteTimePoint) {
        //                    ImmutableDoubleByteTimePoint other = (ImmutableDoubleByteTimePoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleIntTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final ImmutableDoubleIntTimePoint(double x, double y, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleIntTimePoint of(double x, double y, long t, int v) {
        //                return new ImmutableDoubleIntTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableDoubleIntTimePoint) {
        //                    ImmutableDoubleIntTimePoint other = (ImmutableDoubleIntTimePoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleLongTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final ImmutableDoubleLongTimePoint(double x, double y, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleLongTimePoint of(double x, double y, long t, long v) {
        //                return new ImmutableDoubleLongTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableDoubleLongTimePoint) {
        //                    ImmutableDoubleLongTimePoint other = (ImmutableDoubleLongTimePoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleDoubleTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final ImmutableDoubleDoubleTimePoint(double x, double y, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleDoubleTimePoint of(double x, double y, long t, double v) {
        //                return new ImmutableDoubleDoubleTimePoint(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + t) * 31 + v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableDoubleDoubleTimePoint) {
        //                    ImmutableDoubleDoubleTimePoint other = (ImmutableDoubleDoubleTimePoint) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleObjTimePoint<T> {
        //            public final double x;
        //            public final double y;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final ImmutableDoubleObjTimePoint(double x, double y, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableDoubleObjTimePoint<T> of(double x, double y, long t, T v) {
        //                return new ImmutableDoubleObjTimePoint<>(x, y, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + t) * 31 + N.hashCode(v));
        //            }
        //
        //            @Override
        //            public final boolean equals(Object obj) {
        //                if (this == obj) {
        //                    return true;
        //                }
        //
        //                if (obj instanceof ImmutableDoubleObjTimePoint) {
        //                    ImmutableDoubleObjTimePoint<?> other = (ImmutableDoubleObjTimePoint<?>) obj;
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + t + "," + N.toString(v) + "]";
        //            }
        //        }
    }

}
