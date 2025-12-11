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
 * Utility class containing point classes for two-dimensional and three-dimensional coordinate systems.
 * This class provides immutable point implementations with value storage for various coordinate
 * types and use cases in geometric computations, matrix operations, and spatial data structures.
 *
 * <p>The Points class contains:</p>
 * <ul>
 *   <li>{@code xy} - two-dimensional coordinate point implementations with associated values (x, y, v)</li>
 *   <li>{@code xyz} - three-dimensional coordinate point implementations with associated values (x, y, z, v)</li>
 * </ul>
 *
 * <p>Both nested classes provide point implementations for different primitive types
 * (byte, int, long, double) to optimize memory usage and performance for specific use cases.</p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Create a two-dimensional integer point with value
 * Points.xy.IntIntPoint point2D = Points.xy.IntIntPoint.of(10, 20, 100);
 * int x = point2D.x();      // 10
 * int y = point2D.y();      // 20
 * int value = point2D.v();  // 100
 *
 * // Create a three-dimensional integer point with value
 * Points.xyz.IntIntPoint point3D = Points.xyz.IntIntPoint.of(10, 20, 30, 100);
 * int x3 = point3D.x();     // 10
 * int y3 = point3D.y();     // 20
 * int z3 = point3D.z();     // 30
 * int value3 = point3D.v(); // 100
 * }</pre>
 */
public final class Points {

    private Points() {
        // singleton.
    }

    /**
     * This class provides two-dimensional coordinate point implementations.
     * Each point stores x and y coordinates of various primitive types
     * (byte, int, long, double) to optimize memory usage and performance for specific use cases.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create a two-dimensional integer point with value
     * Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(10, 20, 100);
     * int x = point.x();  // 10
     * int y = point.y();  // 20
     * int value = point.v();  // 100
     * }</pre>
     * 
     * @see xyz
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
             * This factory method provides a convenient way to construct two-dimensional points
             * with byte-range coordinates and values, optimized for memory efficiency.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point at coordinates (10, 20) with value 5
             * Points.xy.ByteBytePoint point = Points.xy.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 5);
             * byte x = point.x();  // 10
             * byte y = point.y();  // 20
             * byte value = point.v();  // 5
             *
             * // Useful for memory-efficient grid representations
             * Points.xy.ByteBytePoint gridCell = Points.xy.ByteBytePoint.of((byte) 0, (byte) 0, (byte) 127);
             * }</pre>
             *
             * @param x the x-coordinate of the point, must be in the range [-128, 127]
             * @param y the y-coordinate of the point, must be in the range [-128, 127]
             * @param v the value associated with this point, must be in the range [-128, 127]
             * @return a new ByteBytePoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with small-range coordinates but requiring
             * a full integer range for the associated value, such as counting or indexing operations.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with byte coordinates and integer value
             * Points.xy.ByteIntPoint point = Points.xy.ByteIntPoint.of((byte) 5, (byte) 10, 1000);
             * byte x = point.x();  // 5
             * byte y = point.y();  // 10
             * int value = point.v();  // 1000
             *
             * // Useful for small grids with large counts or indices
             * Points.xy.ByteIntPoint cellWithCount = Points.xy.ByteIntPoint.of((byte) 0, (byte) 0, 1000000);
             * }</pre>
             *
             * @param x the x-coordinate of the point, must be in the range [-128, 127]
             * @param y the y-coordinate of the point, must be in the range [-128, 127]
             * @param v the integer value associated with this point
             * @return a new ByteIntPoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with small-range coordinates but requiring
             * a full long integer range for the associated value, such as timestamps or large counts.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with byte coordinates and long value
             * Points.xy.ByteLongPoint point = Points.xy.ByteLongPoint.of((byte) 3, (byte) 7, 1000000000L);
             * byte x = point.x();  // 3
             * byte y = point.y();  // 7
             * long value = point.v();  // 1000000000
             *
             * // Useful for small grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.xy.ByteLongPoint cellWithTime = Points.xy.ByteLongPoint.of((byte) 0, (byte) 0, timestamp);
             * }</pre>
             *
             * @param x the x-coordinate of the point, must be in the range [-128, 127]
             * @param y the y-coordinate of the point, must be in the range [-128, 127]
             * @param v the long value associated with this point
             * @return a new ByteLongPoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with small-range coordinates but requiring
             * floating-point precision for the associated value, such as probabilities, weights, or measurements.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with byte coordinates and double value
             * Points.xy.ByteDoublePoint point = Points.xy.ByteDoublePoint.of((byte) 2, (byte) 4, 3.14159);
             * byte x = point.x();  // 2
             * byte y = point.y();  // 4
             * double value = point.v();  // 3.14159
             *
             * // Useful for small grids with probability or weight values
             * Points.xy.ByteDoublePoint cellWithProbability = Points.xy.ByteDoublePoint.of((byte) 1, (byte) 1, 0.75);
             * }</pre>
             *
             * @param x the x-coordinate of the point, must be in the range [-128, 127]
             * @param y the y-coordinate of the point, must be in the range [-128, 127]
             * @param v the double value associated with this point
             * @return a new ByteDoublePoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with small-range coordinates but requiring
             * any object type for the associated value, providing maximum flexibility for storing
             * complex data structures at specific coordinates.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with byte coordinates and a String value
             * Points.xy.ByteObjPoint<String> point = Points.xy.ByteObjPoint.of((byte) 1, (byte) 2, "label");
             * byte x = point.x();  // 1
             * byte y = point.y();  // 2
             * String value = point.v();  // "label"
             *
             * // Useful for small grids with complex metadata
             * record Metadata(String name, int priority) {}
             * Metadata meta = new Metadata("important", 10);
             * Points.xy.ByteObjPoint<Metadata> cellWithMeta = Points.xy.ByteObjPoint.of((byte) 0, (byte) 0, meta);
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point, must be in the range [-128, 127]
             * @param y the y-coordinate of the point, must be in the range [-128, 127]
             * @param v the object value associated with this point, may be {@code null}
             * @return a new ByteObjPoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with standard integer coordinates but the
             * associated value fits within a byte range, providing memory efficiency for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with integer coordinates and byte value
             * Points.xy.IntBytePoint point = Points.xy.IntBytePoint.of(100, 200, (byte) 10);
             * int x = point.x();  // 100
             * int y = point.y();  // 200
             * byte value = point.v();  // 10
             *
             * // Useful for large grids with small enumeration values
             * Points.xy.IntBytePoint cellType = Points.xy.IntBytePoint.of(1000, 2000, (byte) 3);  // type = 3
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the byte value associated with this point, must be in the range [-128, 127]
             * @return a new IntBytePoint instance with the specified coordinates and value, never {@code null}
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
             * This is the most commonly used point type for general-purpose integer-based
             * coordinate systems, providing a balanced approach with full integer range for
             * both coordinates and the associated value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with integer coordinates and integer value
             * Points.xy.IntIntPoint point = Points.xy.IntIntPoint.of(100, 200, 300);
             * int x = point.x();  // 100
             * int y = point.y();  // 200
             * int value = point.v();  // 300
             *
             * // Common use case: grid cells with counts or indices
             * Points.xy.IntIntPoint gridCell = Points.xy.IntIntPoint.of(10, 20, 5000);
             *
             * // Use in pathfinding with cost values
             * Points.xy.IntIntPoint pathNode = Points.xy.IntIntPoint.of(5, 8, 15);  // cost = 15
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the integer value associated with this point
             * @return a new IntIntPoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with standard integer coordinates but requiring
             * a long value for timestamps, large counts, or identifiers that exceed the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with integer coordinates and long value
             * Points.xy.IntLongPoint point = Points.xy.IntLongPoint.of(50, 75, 10000000000L);
             * int x = point.x();  // 50
             * int y = point.y();  // 75
             * long value = point.v();  // 10000000000
             *
             * // Useful for grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.xy.IntLongPoint cellWithTime = Points.xy.IntLongPoint.of(10, 20, timestamp);
             *
             * // Track large identifiers in a grid
             * Points.xy.IntLongPoint cellWithId = Points.xy.IntLongPoint.of(5, 8, 9876543210L);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the long value associated with this point
             * @return a new IntLongPoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with standard integer grid coordinates but requiring
             * floating-point precision for the associated value, such as weights, distances, or probabilities.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with integer coordinates and double value
             * Points.xy.IntDoublePoint point = Points.xy.IntDoublePoint.of(10, 20, 3.14159);
             * int x = point.x();  // 10
             * int y = point.y();  // 20
             * double value = point.v();  // 3.14159
             *
             * // Useful for grids with probability values
             * Points.xy.IntDoublePoint cellProbability = Points.xy.IntDoublePoint.of(5, 8, 0.85);
             *
             * // Distance-based calculations
             * double distance = Math.sqrt(10 * 10 + 20 * 20);
             * Points.xy.IntDoublePoint nodeWithDistance = Points.xy.IntDoublePoint.of(10, 20, distance);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the double value associated with this point
             * @return a new IntDoublePoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method provides maximum flexibility by allowing any object type as the value
             * while using standard integer coordinates, making it suitable for complex grid-based data structures.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with integer coordinates and a String value
             * Points.xy.IntObjPoint<String> point = Points.xy.IntObjPoint.of(10, 20, "label");
             * int x = point.x();  // 10
             * int y = point.y();  // 20
             * String value = point.v();  // "label"
             *
             * // Grid with custom objects
             * record Cell(String type, int priority, boolean active) {}
             * Cell cell = new Cell("wall", 5, true);
             * Points.xy.IntObjPoint<Cell> gridCell = Points.xy.IntObjPoint.of(5, 8, cell);
             *
             * // Store collections at grid positions
             * List<String> items = List.of("item1", "item2");
             * Points.xy.IntObjPoint<List<String>> cellItems = Points.xy.IntObjPoint.of(3, 7, items);
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the object value associated with this point, may be {@code null}
             * @return a new IntObjPoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with very large coordinate spaces requiring
             * long values but the associated value fits within a byte range, optimizing memory for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with long coordinates and byte value
             * Points.xy.LongBytePoint point = Points.xy.LongBytePoint.of(1000000L, 2000000L, (byte) 5);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * byte value = point.v();  // 5
             *
             * // Useful for very large grids with small enumeration values
             * Points.xy.LongBytePoint cellType = Points.xy.LongBytePoint.of(999999999L, 888888888L, (byte) 2);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the byte value associated with this point, must be in the range [-128, 127]
             * @return a new LongBytePoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with very large coordinate spaces requiring
             * long values but the associated value fits within the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with long coordinates and integer value
             * Points.xy.LongIntPoint point = Points.xy.LongIntPoint.of(1000000L, 2000000L, 500);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * int value = point.v();  // 500
             *
             * // Useful for very large grids with counts or indices
             * Points.xy.LongIntPoint cellCount = Points.xy.LongIntPoint.of(999999999L, 888888888L, 1000000);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the integer value associated with this point
             * @return a new LongIntPoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method provides the maximum integer range for both coordinates and the
             * associated value, suitable for very large coordinate spaces with large identifiers or timestamps.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with long coordinates and long value
             * Points.xy.LongLongPoint point = Points.xy.LongLongPoint.of(1000000L, 2000000L, 3000000000L);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long value = point.v();  // 3000000000
             *
             * // Useful for very large grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.xy.LongLongPoint cellTime = Points.xy.LongLongPoint.of(999999999L, 888888888L, timestamp);
             *
             * // Large coordinate space with large identifiers
             * Points.xy.LongLongPoint cellId = Points.xy.LongLongPoint.of(1L << 40, 1L << 41, 1L << 50);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the long value associated with this point
             * @return a new LongLongPoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with very large coordinate spaces requiring
             * long values but needing floating-point precision for the associated value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with long coordinates and double value
             * Points.xy.LongDoublePoint point = Points.xy.LongDoublePoint.of(1000000L, 2000000L, 3.14159);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * double value = point.v();  // 3.14159
             *
             * // Useful for very large grids with probability or weight values
             * Points.xy.LongDoublePoint cellWeight = Points.xy.LongDoublePoint.of(999999999L, 888888888L, 0.85);
             *
             * // Large coordinate space with distance calculations
             * double distance = Math.sqrt(1000000.0 * 1000000.0 + 2000000.0 * 2000000.0);
             * Points.xy.LongDoublePoint cellDistance = Points.xy.LongDoublePoint.of(1000000L, 2000000L, distance);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the double value associated with this point
             * @return a new LongDoublePoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method provides maximum flexibility by allowing any object type as the value
             * while using long integer coordinates, suitable for very large coordinate spaces with complex data.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with long coordinates and a String value
             * Points.xy.LongObjPoint<String> point = Points.xy.LongObjPoint.of(1000000L, 2000000L, "marker");
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * String value = point.v();  // "marker"
             *
             * // Very large grid with custom objects
             * record Region(String name, int population) {}
             * Region region = new Region("Zone-A", 1000000);
             * Points.xy.LongObjPoint<Region> gridRegion = Points.xy.LongObjPoint.of(999999999L, 888888888L, region);
             *
             * // Spatial indexing with metadata
             * Map<String, Object> metadata = Map.of("type", "city", "size", 500000);
             * Points.xy.LongObjPoint<Map<String, Object>> location = Points.xy.LongObjPoint.of(1L << 40, 1L << 41, metadata);
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the object value associated with this point, may be {@code null}
             * @return a new LongObjPoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with floating-point coordinate spaces but the
             * associated value fits within a byte range, optimizing memory for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with double coordinates and byte value
             * Points.xy.DoubleBytePoint point = Points.xy.DoubleBytePoint.of(10.5, 20.7, (byte) 3);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * byte value = point.v();  // 3
             *
             * // Useful for continuous coordinate spaces with small enumeration values
             * Points.xy.DoubleBytePoint region = Points.xy.DoubleBytePoint.of(3.14159, 2.71828, (byte) 1);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the byte value associated with this point, must be in the range [-128, 127]
             * @return a new DoubleBytePoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with floating-point coordinate spaces but the
             * associated value fits within the integer range, such as counts or indices in continuous spaces.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with double coordinates and integer value
             * Points.xy.DoubleIntPoint point = Points.xy.DoubleIntPoint.of(10.5, 20.7, 100);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * int value = point.v();  // 100
             *
             * // Useful for continuous coordinate spaces with counts
             * Points.xy.DoubleIntPoint sample = Points.xy.DoubleIntPoint.of(3.14159, 2.71828, 1000);
             *
             * // Geographic coordinates with elevation
             * Points.xy.DoubleIntPoint geoPoint = Points.xy.DoubleIntPoint.of(40.7128, -74.0060, 10);  // elevation in meters
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the integer value associated with this point
             * @return a new DoubleIntPoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method is ideal when working with floating-point coordinate spaces and requiring
             * long values for timestamps, large identifiers, or counts that exceed the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with double coordinates and long value
             * Points.xy.DoubleLongPoint point = Points.xy.DoubleLongPoint.of(10.5, 20.7, 1000000000L);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * long value = point.v();  // 1000000000
             *
             * // Useful for continuous coordinate spaces with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.xy.DoubleLongPoint sample = Points.xy.DoubleLongPoint.of(3.14159, 2.71828, timestamp);
             *
             * // Geographic coordinates with large identifiers
             * Points.xy.DoubleLongPoint location = Points.xy.DoubleLongPoint.of(40.7128, -74.0060, 9876543210L);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the long value associated with this point
             * @return a new DoubleLongPoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method provides full double-precision floating-point support for both coordinates
             * and the associated value, ideal for continuous mathematical and scientific applications.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with double coordinates and double value
             * Points.xy.DoubleDoublePoint point = Points.xy.DoubleDoublePoint.of(10.5, 20.7, 3.14159);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double value = point.v();  // 3.14159
             *
             * // Useful for continuous spaces with probability or weight values
             * Points.xy.DoubleDoublePoint sample = Points.xy.DoubleDoublePoint.of(3.14159, 2.71828, 0.95);
             *
             * // Geographic coordinates with temperature
             * Points.xy.DoubleDoublePoint weather = Points.xy.DoubleDoublePoint.of(40.7128, -74.0060, 22.5);
             *
             * // Mathematical calculations
             * double distance = Math.sqrt(10.5 * 10.5 + 20.7 * 20.7);
             * Points.xy.DoubleDoublePoint vector = Points.xy.DoubleDoublePoint.of(10.5, 20.7, distance);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the double value associated with this point
             * @return a new DoubleDoublePoint instance with the specified coordinates and value, never {@code null}
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
             * This factory method provides maximum flexibility by allowing any object type as the value
             * while using double-precision floating-point coordinates, suitable for continuous spaces with complex data.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a point with double coordinates and a String value
             * Points.xy.DoubleObjPoint<String> point = Points.xy.DoubleObjPoint.of(10.5, 20.7, "location");
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * String value = point.v();  // "location"
             *
             * // Geographic coordinates with custom objects
             * record Place(String name, int population, String country) {}
             * Place place = new Place("New York", 8000000, "USA");
             * Points.xy.DoubleObjPoint<Place> geoPoint = Points.xy.DoubleObjPoint.of(40.7128, -74.0060, place);
             *
             * // Continuous space with metadata
             * Map<String, Object> metadata = Map.of("temperature", 22.5, "humidity", 65);
             * Points.xy.DoubleObjPoint<Map<String, Object>> sensor = Points.xy.DoubleObjPoint.of(3.14159, 2.71828, metadata);
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param v the object value associated with this point, may be {@code null}
             * @return a new DoubleObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> DoubleObjPoint<T> of(final double x, final double y, final T v) {
                return new DoubleObjPoint<>(x, y, v);
            }
        }
    }

    /**
     * This class provides three-dimensional coordinate point implementations.
     * Each point stores x, y, and z coordinates of various primitive types
     * (byte, int, long, double) to optimize memory usage and performance for specific use cases.
     *
     * <p><b>Usage Examples:</b></p>
     * <pre>{@code
     * // Create a three-dimensional integer point with value
     * Points.xyz.IntIntPoint point = Points.xyz.IntIntPoint.of(10, 20, 30, 100);
     * int x = point.x();  // 10
     * int y = point.y();  // 20
     * int z = point.z();  // 30
     * int value = point.v();  // 100
     * }</pre>
     * 
     * @see xy
     */
    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
    public static final class xyz { // NOSONAR

        /**
         * Instantiates a new xyz.
         */
        private xyz() {
            // singleton.
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
         * @param v the value associated with this point
         */
        public record ByteBytePoint(byte x, byte y, byte z, byte v) {

            /**
             * Creates a new ByteBytePoint with the specified x, y, z coordinates and value.
             * This factory method provides a convenient way to construct three-dimensional points
             * with byte-range coordinates and values, optimized for maximum memory efficiency.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point at coordinates (10, 20, 30) with value 5
             * Points.xyz.ByteBytePoint point = Points.xyz.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30, (byte) 5);
             * byte x = point.x();  // 10
             * byte y = point.y();  // 20
             * byte z = point.z();  // 30
             * byte value = point.v();  // 5
             *
             * // Useful for memory-efficient 3D grid representations
             * Points.xyz.ByteBytePoint voxel = Points.xyz.ByteBytePoint.of((byte) 0, (byte) 0, (byte) 0, (byte) 127);
             * }</pre>
             *
             * @param x the x-coordinate of the point, must be in the range [-128, 127]
             * @param y the y-coordinate of the point, must be in the range [-128, 127]
             * @param z the z-coordinate of the point, must be in the range [-128, 127]
             * @param v the value associated with this point, must be in the range [-128, 127]
             * @return a new ByteBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteBytePoint of(final byte x, final byte y, final byte z, final byte v) {
                return new ByteBytePoint(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record ByteIntPoint(byte x, byte y, byte z, int v) {

            /**
             * Creates a new ByteIntPoint with the specified coordinates and value.
             * This factory method is ideal when working with small-range 3D coordinates but requiring
             * a full integer range for the associated value, such as counting or indexing in 3D space.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with byte coordinates and integer value
             * Points.xyz.ByteIntPoint point = Points.xyz.ByteIntPoint.of((byte) 5, (byte) 10, (byte) 15, 1000);
             * byte x = point.x();  // 5
             * byte y = point.y();  // 10
             * byte z = point.z();  // 15
             * int value = point.v();  // 1000
             *
             * // Useful for small 3D grids with large counts
             * Points.xyz.ByteIntPoint voxelCount = Points.xyz.ByteIntPoint.of((byte) 0, (byte) 0, (byte) 0, 1000000);
             * }</pre>
             *
             * @param x the x-coordinate of the point, must be in the range [-128, 127]
             * @param y the y-coordinate of the point, must be in the range [-128, 127]
             * @param z the z-coordinate of the point, must be in the range [-128, 127]
             * @param v the integer value associated with this point
             * @return a new ByteIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteIntPoint of(final byte x, final byte y, final byte z, final int v) {
                return new ByteIntPoint(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record ByteLongPoint(byte x, byte y, byte z, long v) {

            /**
             * Creates a new ByteLongPoint with the specified coordinates and value.
             * This factory method is ideal when working with small-range 3D coordinates but requiring
             * a full long integer range for the associated value, such as timestamps or large identifiers in 3D space.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with byte coordinates and long value
             * Points.xyz.ByteLongPoint point = Points.xyz.ByteLongPoint.of((byte) 3, (byte) 7, (byte) 11, 1000000000L);
             * byte x = point.x();  // 3
             * byte y = point.y();  // 7
             * byte z = point.z();  // 11
             * long value = point.v();  // 1000000000
             *
             * // Useful for small 3D grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.xyz.ByteLongPoint voxelTime = Points.xyz.ByteLongPoint.of((byte) 0, (byte) 0, (byte) 0, timestamp);
             * }</pre>
             *
             * @param x the x-coordinate of the point, must be in the range [-128, 127]
             * @param y the y-coordinate of the point, must be in the range [-128, 127]
             * @param z the z-coordinate of the point, must be in the range [-128, 127]
             * @param v the long value associated with this point
             * @return a new ByteLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteLongPoint of(final byte x, final byte y, final byte z, final long v) {
                return new ByteLongPoint(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record ByteDoublePoint(byte x, byte y, byte z, double v) {

            /**
             * Creates a new ByteDoublePoint with the specified coordinates and value.
             * This factory method is ideal when working with small-range 3D coordinates but requiring
             * floating-point precision for the associated value, such as densities, probabilities, or physical measurements in 3D space.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with byte coordinates and double value
             * Points.xyz.ByteDoublePoint point = Points.xyz.ByteDoublePoint.of((byte) 2, (byte) 4, (byte) 6, 3.14159);
             * byte x = point.x();  // 2
             * byte y = point.y();  // 4
             * byte z = point.z();  // 6
             * double value = point.v();  // 3.14159
             *
             * // Useful for small 3D grids with density values
             * Points.xyz.ByteDoublePoint voxelDensity = Points.xyz.ByteDoublePoint.of((byte) 1, (byte) 1, (byte) 1, 0.85);
             * }</pre>
             *
             * @param x the x-coordinate of the point, must be in the range [-128, 127]
             * @param y the y-coordinate of the point, must be in the range [-128, 127]
             * @param z the z-coordinate of the point, must be in the range [-128, 127]
             * @param v the double value associated with this point
             * @return a new ByteDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static ByteDoublePoint of(final byte x, final byte y, final byte z, final double v) {
                return new ByteDoublePoint(x, y, z, v);
            }
        }

        /**
         * Represents an immutable three-dimensional point with byte coordinates and a generic object value.
         * This class is useful when coordinates are constrained to byte range (-128 to 127)
         * but the associated value can be any object type.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param v the value associated with this point
         */
        public record ByteObjPoint<T>(byte x, byte y, byte z, T v) {

            /**
             * Creates a new ByteObjPoint with the specified coordinates and value.
             * This factory method is ideal when working with small-range 3D coordinates but requiring
             * any object type for the associated value, providing maximum flexibility for storing
             * complex data structures at specific 3D positions.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with byte coordinates and a String value
             * Points.xyz.ByteObjPoint<String> point = Points.xyz.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "voxel");
             * byte x = point.x();  // 1
             * byte y = point.y();  // 2
             * byte z = point.z();  // 3
             * String value = point.v();  // "voxel"
             *
             * // Useful for small 3D grids with complex metadata
             * record Material(String type, double density) {}
             * Material material = new Material("iron", 7.87);
             * Points.xyz.ByteObjPoint<Material> voxelMaterial = Points.xyz.ByteObjPoint.of((byte) 0, (byte) 0, (byte) 0, material);
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point, must be in the range [-128, 127]
             * @param y the y-coordinate of the point, must be in the range [-128, 127]
             * @param z the z-coordinate of the point, must be in the range [-128, 127]
             * @param v the object value associated with this point, may be {@code null}
             * @return a new ByteObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> ByteObjPoint<T> of(final byte x, final byte y, final byte z, final T v) {
                return new ByteObjPoint<>(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record IntBytePoint(int x, int y, int z, byte v) {

            /**
             * Creates a new IntBytePoint with the specified coordinates and value.
             * This factory method is ideal when working with standard integer 3D coordinates but the
             * associated value fits within a byte range, providing memory efficiency for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with integer coordinates and byte value
             * Points.xyz.IntBytePoint point = Points.xyz.IntBytePoint.of(100, 200, 300, (byte) 10);
             * int x = point.x();  // 100
             * int y = point.y();  // 200
             * int z = point.z();  // 300
             * byte value = point.v();  // 10
             *
             * // Useful for large 3D grids with small enumeration values
             * Points.xyz.IntBytePoint voxelType = Points.xyz.IntBytePoint.of(1000, 2000, 3000, (byte) 3);  // type = 3
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the byte value associated with this point, must be in the range [-128, 127]
             * @return a new IntBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntBytePoint of(final int x, final int y, final int z, final byte v) {
                return new IntBytePoint(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record IntIntPoint(int x, int y, int z, int v) {

            /**
             * Creates a new IntIntPoint with the specified coordinates and value.
             * This is the most commonly used 3D point type for general-purpose integer-based
             * coordinate systems, providing a balanced approach with full integer range for
             * all three coordinates and the associated value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with integer coordinates and integer value
             * Points.xyz.IntIntPoint point = Points.xyz.IntIntPoint.of(100, 200, 300, 400);
             * int x = point.x();  // 100
             * int y = point.y();  // 200
             * int z = point.z();  // 300
             * int value = point.v();  // 400
             *
             * // Common use case: 3D voxel grids with counts
             * Points.xyz.IntIntPoint voxel = Points.xyz.IntIntPoint.of(10, 20, 30, 5000);
             *
             * // 3D pathfinding with cost values
             * Points.xyz.IntIntPoint pathNode = Points.xyz.IntIntPoint.of(5, 8, 12, 25);  // cost = 25
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the integer value associated with this point
             * @return a new IntIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntIntPoint of(final int x, final int y, final int z, final int v) {
                return new IntIntPoint(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record IntLongPoint(int x, int y, int z, long v) {

            /**
             * Creates a new IntLongPoint with the specified coordinates and value.
             * This factory method is ideal when working with standard integer 3D coordinates but requiring
             * a long value for timestamps, large counts, or identifiers that exceed the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with integer coordinates and long value
             * Points.xyz.IntLongPoint point = Points.xyz.IntLongPoint.of(50, 75, 100, 10000000000L);
             * int x = point.x();  // 50
             * int y = point.y();  // 75
             * int z = point.z();  // 100
             * long value = point.v();  // 10000000000
             *
             * // Useful for 3D grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.xyz.IntLongPoint voxelTime = Points.xyz.IntLongPoint.of(10, 20, 30, timestamp);
             *
             * // Track large identifiers in a 3D grid
             * Points.xyz.IntLongPoint voxelId = Points.xyz.IntLongPoint.of(5, 8, 12, 9876543210L);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the long value associated with this point
             * @return a new IntLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntLongPoint of(final int x, final int y, final int z, final long v) {
                return new IntLongPoint(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record IntDoublePoint(int x, int y, int z, double v) {

            /**
             * Creates a new IntDoublePoint with the specified coordinates and value.
             * This factory method is ideal when working with standard integer 3D grid coordinates but requiring
             * floating-point precision for the associated value, such as densities, distances, or probabilities.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with integer coordinates and double value
             * Points.xyz.IntDoublePoint point = Points.xyz.IntDoublePoint.of(10, 20, 30, 3.14159);
             * int x = point.x();  // 10
             * int y = point.y();  // 20
             * int z = point.z();  // 30
             * double value = point.v();  // 3.14159
             *
             * // Useful for 3D grids with density values
             * Points.xyz.IntDoublePoint voxelDensity = Points.xyz.IntDoublePoint.of(5, 8, 12, 0.85);
             *
             * // 3D distance-based calculations
             * double distance = Math.sqrt(10 * 10 + 20 * 20 + 30 * 30);
             * Points.xyz.IntDoublePoint nodeDistance = Points.xyz.IntDoublePoint.of(10, 20, 30, distance);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the double value associated with this point
             * @return a new IntDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static IntDoublePoint of(final int x, final int y, final int z, final double v) {
                return new IntDoublePoint(x, y, z, v);
            }
        }

        /**
         * Represents an immutable three-dimensional point with integer coordinates and a generic object value.
         * This class is useful when coordinates fit within the integer range
         * but the associated value can be any object type.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param v the value associated with this point
         */
        public record IntObjPoint<T>(int x, int y, int z, T v) {

            /**
             * Creates a new IntObjPoint with the specified coordinates and value.
             * This factory method provides maximum flexibility by allowing any object type as the value
             * while using standard integer 3D coordinates, making it suitable for complex voxel-based data structures.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with integer coordinates and a String value
             * Points.xyz.IntObjPoint<String> point = Points.xyz.IntObjPoint.of(10, 20, 30, "block");
             * int x = point.x();  // 10
             * int y = point.y();  // 20
             * int z = point.z();  // 30
             * String value = point.v();  // "block"
             *
             * // 3D voxel grid with custom objects
             * record Voxel(String material, double density, boolean solid) {}
             * Voxel voxel = new Voxel("stone", 2.5, true);
             * Points.xyz.IntObjPoint<Voxel> gridVoxel = Points.xyz.IntObjPoint.of(5, 8, 12, voxel);
             *
             * // Store collections at 3D positions
             * List<String> items = List.of("chest", "gold");
             * Points.xyz.IntObjPoint<List<String>> voxelItems = Points.xyz.IntObjPoint.of(3, 7, 10, items);
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the object value associated with this point, may be {@code null}
             * @return a new IntObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> IntObjPoint<T> of(final int x, final int y, final int z, final T v) {
                return new IntObjPoint<>(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record LongBytePoint(long x, long y, long z, byte v) {

            /**
             * Creates a new LongBytePoint with the specified coordinates and value.
             * This factory method is ideal when working with very large 3D coordinate spaces requiring
             * long values but the associated value fits within a byte range, optimizing memory for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with long coordinates and byte value
             * Points.xyz.LongBytePoint point = Points.xyz.LongBytePoint.of(1000000L, 2000000L, 3000000L, (byte) 5);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long z = point.z();  // 3000000
             * byte value = point.v();  // 5
             *
             * // Useful for very large 3D grids with small enumeration values
             * Points.xyz.LongBytePoint voxelType = Points.xyz.LongBytePoint.of(999999999L, 888888888L, 777777777L, (byte) 2);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the byte value associated with this point, must be in the range [-128, 127]
             * @return a new LongBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongBytePoint of(final long x, final long y, final long z, final byte v) {
                return new LongBytePoint(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record LongIntPoint(long x, long y, long z, int v) {

            /**
             * Creates a new LongIntPoint with the specified coordinates and value.
             * This factory method is ideal when working with very large 3D coordinate spaces requiring
             * long values but the associated value fits within the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with long coordinates and integer value
             * Points.xyz.LongIntPoint point = Points.xyz.LongIntPoint.of(1000000L, 2000000L, 3000000L, 500);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long z = point.z();  // 3000000
             * int value = point.v();  // 500
             *
             * // Useful for very large 3D grids with counts or indices
             * Points.xyz.LongIntPoint voxelCount = Points.xyz.LongIntPoint.of(999999999L, 888888888L, 777777777L, 1000000);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the integer value associated with this point
             * @return a new LongIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongIntPoint of(final long x, final long y, final long z, final int v) {
                return new LongIntPoint(x, y, z, v);
            }
        }

        /**
         * Represents an immutable three-dimensional point with long coordinates and a long value.
         * This class provides full long integer range for both coordinates and associated value.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param v the value associated with this point
         */
        public record LongLongPoint(long x, long y, long z, long v) {

            /**
             * Creates a new LongLongPoint with the specified coordinates and value.
             * This factory method provides the maximum integer range for both 3D coordinates and the
             * associated value, suitable for very large coordinate spaces with large identifiers or timestamps.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with long coordinates and long value
             * Points.xyz.LongLongPoint point = Points.xyz.LongLongPoint.of(1000000L, 2000000L, 3000000L, 4000000000L);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long z = point.z();  // 3000000
             * long value = point.v();  // 4000000000
             *
             * // Useful for very large 3D grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.xyz.LongLongPoint voxelTime = Points.xyz.LongLongPoint.of(999999999L, 888888888L, 777777777L, timestamp);
             *
             * // Large 3D coordinate space with large identifiers
             * Points.xyz.LongLongPoint voxelId = Points.xyz.LongLongPoint.of(1L << 40, 1L << 41, 1L << 42, 1L << 50);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the long value associated with this point
             * @return a new LongLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongLongPoint of(final long x, final long y, final long z, final long v) {
                return new LongLongPoint(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record LongDoublePoint(long x, long y, long z, double v) {

            /**
             * Creates a new LongDoublePoint with the specified coordinates and value.
             * This factory method is ideal when working with very large 3D coordinate spaces requiring
             * long values but needing floating-point precision for the associated value.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with long coordinates and double value
             * Points.xyz.LongDoublePoint point = Points.xyz.LongDoublePoint.of(1000000L, 2000000L, 3000000L, 3.14159);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long z = point.z();  // 3000000
             * double value = point.v();  // 3.14159
             *
             * // Useful for very large 3D grids with density or probability values
             * Points.xyz.LongDoublePoint voxelDensity = Points.xyz.LongDoublePoint.of(999999999L, 888888888L, 777777777L, 0.85);
             *
             * // Large 3D coordinate space with distance calculations
             * double distance = Math.sqrt(1000000.0 * 1000000.0 + 2000000.0 * 2000000.0 + 3000000.0 * 3000000.0);
             * Points.xyz.LongDoublePoint voxelDistance = Points.xyz.LongDoublePoint.of(1000000L, 2000000L, 3000000L, distance);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the double value associated with this point
             * @return a new LongDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static LongDoublePoint of(final long x, final long y, final long z, final double v) {
                return new LongDoublePoint(x, y, z, v);
            }
        }

        /**
         * Represents an immutable three-dimensional point with long coordinates and a generic object value.
         * This class is useful when coordinates require the full long integer range
         * but the associated value can be any object type.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param v the value associated with this point
         */
        public record LongObjPoint<T>(long x, long y, long z, T v) {

            /**
             * Creates a new LongObjPoint with the specified coordinates and value.
             * This factory method provides maximum flexibility by allowing any object type as the value
             * while using long integer 3D coordinates, suitable for very large coordinate spaces with complex data.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with long coordinates and a String value
             * Points.xyz.LongObjPoint<String> point = Points.xyz.LongObjPoint.of(1000000L, 2000000L, 3000000L, "region");
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long z = point.z();  // 3000000
             * String value = point.v();  // "region"
             *
             * // Very large 3D grid with custom objects
             * record Chunk(String biome, int height, boolean generated) {}
             * Chunk chunk = new Chunk("plains", 64, true);
             * Points.xyz.LongObjPoint<Chunk> gridChunk = Points.xyz.LongObjPoint.of(999999999L, 888888888L, 777777777L, chunk);
             *
             * // 3D spatial indexing with metadata
             * Map<String, Object> metadata = Map.of("type", "structure", "volume", 1000);
             * Points.xyz.LongObjPoint<Map<String, Object>> voxel = Points.xyz.LongObjPoint.of(1L << 40, 1L << 41, 1L << 42, metadata);
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the object value associated with this point, may be {@code null}
             * @return a new LongObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> LongObjPoint<T> of(final long x, final long y, final long z, final T v) {
                return new LongObjPoint<>(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record DoubleBytePoint(double x, double y, double z, byte v) {

            /**
             * Creates a new DoubleBytePoint with the specified coordinates and value.
             * This factory method is ideal when working with floating-point 3D coordinate spaces but the
             * associated value fits within a byte range, optimizing memory for the value component.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with double coordinates and byte value
             * Points.xyz.DoubleBytePoint point = Points.xyz.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 3);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double z = point.z();  // 30.9
             * byte value = point.v();  // 3
             *
             * // Useful for continuous 3D coordinate spaces with small enumeration values
             * Points.xyz.DoubleBytePoint region = Points.xyz.DoubleBytePoint.of(3.14159, 2.71828, 1.41421, (byte) 1);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the byte value associated with this point, must be in the range [-128, 127]
             * @return a new DoubleBytePoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleBytePoint of(final double x, final double y, final double z, final byte v) {
                return new DoubleBytePoint(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record DoubleIntPoint(double x, double y, double z, int v) {

            /**
             * Creates a new DoubleIntPoint with the specified coordinates and value.
             * This factory method is ideal when working with floating-point 3D coordinate spaces but the
             * associated value fits within the integer range, such as counts or indices in continuous 3D spaces.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with double coordinates and integer value
             * Points.xyz.DoubleIntPoint point = Points.xyz.DoubleIntPoint.of(10.5, 20.7, 30.9, 100);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double z = point.z();  // 30.9
             * int value = point.v();  // 100
             *
             * // Useful for continuous 3D coordinate spaces with counts
             * Points.xyz.DoubleIntPoint sample = Points.xyz.DoubleIntPoint.of(3.14159, 2.71828, 1.41421, 1000);
             *
             * // 3D physical coordinates with count values
             * Points.xyz.DoubleIntPoint position = Points.xyz.DoubleIntPoint.of(1.5, 2.7, 3.9, 42);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the integer value associated with this point
             * @return a new DoubleIntPoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleIntPoint of(final double x, final double y, final double z, final int v) {
                return new DoubleIntPoint(x, y, z, v);
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
         * @param v the value associated with this point
         */
        public record DoubleLongPoint(double x, double y, double z, long v) {

            /**
             * Creates a new DoubleLongPoint with the specified coordinates and value.
             * This factory method is ideal when working with floating-point 3D coordinate spaces and requiring
             * long values for timestamps, large identifiers, or counts that exceed the integer range.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with double coordinates and long value
             * Points.xyz.DoubleLongPoint point = Points.xyz.DoubleLongPoint.of(10.5, 20.7, 30.9, 1000000000L);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double z = point.z();  // 30.9
             * long value = point.v();  // 1000000000
             *
             * // Useful for continuous 3D coordinate spaces with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.xyz.DoubleLongPoint sample = Points.xyz.DoubleLongPoint.of(3.14159, 2.71828, 1.41421, timestamp);
             *
             * // 3D physical coordinates with large identifiers
             * Points.xyz.DoubleLongPoint position = Points.xyz.DoubleLongPoint.of(1.5, 2.7, 3.9, 9876543210L);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the long value associated with this point
             * @return a new DoubleLongPoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleLongPoint of(final double x, final double y, final double z, final long v) {
                return new DoubleLongPoint(x, y, z, v);
            }
        }

        /**
         * Represents an immutable three-dimensional point with double-precision floating-point coordinates and value.
         * This class provides full double-precision floating-point support for both coordinates and the associated value.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param v the value associated with this point
         */
        public record DoubleDoublePoint(double x, double y, double z, double v) {

            /**
             * Creates a new DoubleDoublePoint with the specified coordinates and value.
             * This factory method provides full double-precision floating-point support for both 3D coordinates
             * and the associated value, ideal for continuous mathematical, physical, and scientific 3D applications.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with double coordinates and double value
             * Points.xyz.DoubleDoublePoint point = Points.xyz.DoubleDoublePoint.of(10.5, 20.7, 30.9, 3.14159);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double z = point.z();  // 30.9
             * double value = point.v();  // 3.14159
             *
             * // Useful for continuous 3D spaces with density or probability values
             * Points.xyz.DoubleDoublePoint sample = Points.xyz.DoubleDoublePoint.of(3.14159, 2.71828, 1.41421, 0.95);
             *
             * // 3D physical coordinates with measurements
             * Points.xyz.DoubleDoublePoint position = Points.xyz.DoubleDoublePoint.of(1.5, 2.7, 3.9, 22.5);
             *
             * // Mathematical calculations in 3D space
             * double distance = Math.sqrt(10.5 * 10.5 + 20.7 * 20.7 + 30.9 * 30.9);
             * Points.xyz.DoubleDoublePoint vector = Points.xyz.DoubleDoublePoint.of(10.5, 20.7, 30.9, distance);
             * }</pre>
             *
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the double value associated with this point
             * @return a new DoubleDoublePoint instance with the specified coordinates and value, never {@code null}
             */
            public static DoubleDoublePoint of(final double x, final double y, final double z, final double v) {
                return new DoubleDoublePoint(x, y, z, v);
            }
        }

        /**
         * Represents an immutable three-dimensional point with double-precision floating-point coordinates and a generic object value.
         * This class is useful when coordinates require floating-point precision
         * but the associated value can be any object type.
         *
         * <p>All instances are immutable and thread-safe.</p>
         *
         * @param <T> the type of the value object associated with this point
         * @param x the x-coordinate of this point
         * @param y the y-coordinate of this point
         * @param z the z-coordinate of this point
         * @param v the value associated with this point
         */
        public record DoubleObjPoint<T>(double x, double y, double z, T v) {

            /**
             * Creates a new DoubleObjPoint with the specified coordinates and value.
             * This factory method provides maximum flexibility by allowing any object type as the value
             * while using double-precision floating-point 3D coordinates, suitable for continuous spaces with complex data.
             *
             * <p><b>Usage Examples:</b></p>
             * <pre>{@code
             * // Create a 3D point with double coordinates and a String value
             * Points.xyz.DoubleObjPoint<String> point = Points.xyz.DoubleObjPoint.of(10.5, 20.7, 30.9, "marker");
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double z = point.z();  // 30.9
             * String value = point.v();  // "marker"
             *
             * // 3D physical space with custom objects
             * record Particle(String type, double mass, double charge) {}
             * Particle particle = new Particle("electron", 9.109e-31, -1.602e-19);
             * Points.xyz.DoubleObjPoint<Particle> position = Points.xyz.DoubleObjPoint.of(1.5, 2.7, 3.9, particle);
             *
             * // Continuous 3D space with metadata
             * Map<String, Object> metadata = Map.of("temperature", 22.5, "pressure", 101.3, "humidity", 65);
             * Points.xyz.DoubleObjPoint<Map<String, Object>> sensor = Points.xyz.DoubleObjPoint.of(3.14159, 2.71828, 1.41421, metadata);
             * }</pre>
             *
             * @param <T> the type of the value associated with this point
             * @param x the x-coordinate of the point
             * @param y the y-coordinate of the point
             * @param z the z-coordinate of the point
             * @param v the object value associated with this point, may be {@code null}
             * @return a new DoubleObjPoint instance with the specified coordinates and value, never {@code null}
             */
            public static <T> DoubleObjPoint<T> of(final double x, final double y, final double z, final T v) {
                return new DoubleObjPoint<>(x, y, z, v);
            }
        }
    }
}
