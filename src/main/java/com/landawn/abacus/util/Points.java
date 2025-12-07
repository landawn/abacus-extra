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
 * int x = point.x();  // 10
 * int y = point.y();  // 20
 * int value = point.v();  // 100
 * }</pre>
 */
public final class Points {

    private Points() {
        // singleton.
    }

    /**
     * The Class xy.
     */
    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
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
        public record ByteBytePoint(byte x, byte y, byte v) {

            /**
             * Creates a new ByteBytePoint with the specified x, y coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new ByteBytePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static ByteBytePoint of(final byte x, final byte y, final byte v) {
                return new ByteBytePoint(x, y, v);
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
        public record ByteIntPoint(byte x, byte y, int v) {

            /**
             * Creates a new ByteIntPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new ByteIntPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static ByteIntPoint of(final byte x, final byte y, final int v) {
                return new ByteIntPoint(x, y, v);
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
        public record ByteLongPoint(byte x, byte y, long v) {

            /**
             * Creates a new ByteLongPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new ByteLongPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static ByteLongPoint of(final byte x, final byte y, final long v) {
                return new ByteLongPoint(x, y, v);
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
        public record ByteDoublePoint(byte x, byte y, double v) {

            /**
             * Creates a new ByteDoublePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new ByteDoublePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static ByteDoublePoint of(final byte x, final byte y, final double v) {
                return new ByteDoublePoint(x, y, v);
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
        public record ByteObjPoint<T>(byte x, byte y, T v) {

            /**
             * Creates a new ByteObjPoint with the specified coordinates and value.
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
        public record IntBytePoint(int x, int y, byte v) {

            /**
             * Creates a new IntBytePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new IntBytePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static IntBytePoint of(final int x, final int y, final byte v) {
                return new IntBytePoint(x, y, v);
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
        public record IntIntPoint(int x, int y, int v) {

            /**
             * Creates a new IntIntPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new IntIntPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static IntIntPoint of(final int x, final int y, final int v) {
                return new IntIntPoint(x, y, v);
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
        public record IntLongPoint(int x, int y, long v) {

            /**
             * Creates a new IntLongPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new IntLongPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static IntLongPoint of(final int x, final int y, final long v) {
                return new IntLongPoint(x, y, v);
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
        public record IntDoublePoint(int x, int y, double v) {

            /**
             * Creates a new IntDoublePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new IntDoublePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static IntDoublePoint of(final int x, final int y, final double v) {
                return new IntDoublePoint(x, y, v);
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
        public record IntObjPoint<T>(int x, int y, T v) {

            /**
             * Creates a new IntObjPoint with the specified coordinates and value.
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
        public record LongBytePoint(long x, long y, byte v) {

            /**
             * Creates a new LongBytePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new LongBytePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static LongBytePoint of(final long x, final long y, final byte v) {
                return new LongBytePoint(x, y, v);
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
        public record LongIntPoint(long x, long y, int v) {

            /**
             * Creates a new LongIntPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new LongIntPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static LongIntPoint of(final long x, final long y, final int v) {
                return new LongIntPoint(x, y, v);
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
        public record LongLongPoint(long x, long y, long v) {

            /**
             * Creates a new LongLongPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new LongLongPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static LongLongPoint of(final long x, final long y, final long v) {
                return new LongLongPoint(x, y, v);
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
        public record LongDoublePoint(long x, long y, double v) {

            /**
             * Creates a new LongDoublePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new LongDoublePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static LongDoublePoint of(final long x, final long y, final double v) {
                return new LongDoublePoint(x, y, v);
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
        public record LongObjPoint<T>(long x, long y, T v) {

            /**
             * Creates a new LongObjPoint with the specified coordinates and value.
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
        public record DoubleBytePoint(double x, double y, byte v) {

            /**
             * Creates a new DoubleBytePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new DoubleBytePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static DoubleBytePoint of(final double x, final double y, final byte v) {
                return new DoubleBytePoint(x, y, v);
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
        public record DoubleIntPoint(double x, double y, int v) {

            /**
             * Creates a new DoubleIntPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new DoubleIntPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static DoubleIntPoint of(final double x, final double y, final int v) {
                return new DoubleIntPoint(x, y, v);
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
        public record DoubleLongPoint(double x, double y, long v) {

            /**
             * Creates a new DoubleLongPoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new DoubleLongPoint instance with the specified coordinates and value; never {@code null}.
             */
            public static DoubleLongPoint of(final double x, final double y, final long v) {
                return new DoubleLongPoint(x, y, v);
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
        public record DoubleDoublePoint(double x, double y, double v) {

            /**
             * Creates a new DoubleDoublePoint with the specified coordinates and value.
             *
             * @param x the x-coordinate
             * @param y the y-coordinate
             * @param v the value associated with this point
             * @return a new DoubleDoublePoint instance with the specified coordinates and value; never {@code null}.
             */
            public static DoubleDoublePoint of(final double x, final double y, final double v) {
                return new DoubleDoublePoint(x, y, v);
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
        public record DoubleObjPoint<T>(double x, double y, T v) {

            /**
             * Creates a new DoubleObjPoint with the specified coordinates and value.
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
        }
    }
}
