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
             * // Create a point at coordinates (10, 20) with value 5
             * Points.D2.ByteBytePoint point = Points.D2.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 5);
             * byte x = point.x();  // 10
             * byte y = point.y();  // 20
             * byte value = point.value();  // 5
             *
             * // Useful for memory-efficient grid representations
             * Points.D2.ByteBytePoint gridCell = Points.D2.ByteBytePoint.of((byte) 0, (byte) 0, (byte) 127);
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
             * // Create a point with byte coordinates and integer value
             * Points.D2.ByteIntPoint point = Points.D2.ByteIntPoint.of((byte) 5, (byte) 10, 1000);
             * byte x = point.x();  // 5
             * byte y = point.y();  // 10
             * int value = point.value();  // 1000
             *
             * // Useful for small grids with large counts or indices
             * Points.D2.ByteIntPoint cellWithCount = Points.D2.ByteIntPoint.of((byte) 0, (byte) 0, 1000000);
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
             * // Create a point with byte coordinates and long value
             * Points.D2.ByteLongPoint point = Points.D2.ByteLongPoint.of((byte) 3, (byte) 7, 1000000000L);
             * byte x = point.x();  // 3
             * byte y = point.y();  // 7
             * long value = point.value();  // 1000000000
             *
             * // Useful for small grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.D2.ByteLongPoint cellWithTime = Points.D2.ByteLongPoint.of((byte) 0, (byte) 0, timestamp);
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
             * // Create a point with byte coordinates and double value
             * Points.D2.ByteDoublePoint point = Points.D2.ByteDoublePoint.of((byte) 2, (byte) 4, 3.14159);
             * byte x = point.x();  // 2
             * byte y = point.y();  // 4
             * double value = point.value();  // 3.14159
             *
             * // Useful for small grids with probability or weight values
             * Points.D2.ByteDoublePoint cellWithProbability = Points.D2.ByteDoublePoint.of((byte) 1, (byte) 1, 0.75);
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
             * // Create a point with byte coordinates and a String value
             * Points.D2.ByteObjPoint<String> point = Points.D2.ByteObjPoint.of((byte) 1, (byte) 2, "label");
             * byte x = point.x();  // 1
             * byte y = point.y();  // 2
             * String value = point.value();  // "label"
             *
             * // Useful for small grids with complex metadata
             * record Metadata(String name, int priority) {}
             * Metadata meta = new Metadata("important", 10);
             * Points.D2.ByteObjPoint<Metadata> cellWithMeta = Points.D2.ByteObjPoint.of((byte) 0, (byte) 0, meta);
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
             * // Create a point with integer coordinates and byte value
             * Points.D2.IntBytePoint point = Points.D2.IntBytePoint.of(100, 200, (byte) 10);
             * int x = point.x();  // 100
             * int y = point.y();  // 200
             * byte value = point.value();  // 10
             *
             * // Useful for large grids with small enumeration values
             * Points.D2.IntBytePoint cellType = Points.D2.IntBytePoint.of(1000, 2000, (byte) 3);  // type = 3
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
             * // Create a point with integer coordinates and integer value
             * Points.D2.IntIntPoint point = Points.D2.IntIntPoint.of(100, 200, 300);
             * int x = point.x();  // 100
             * int y = point.y();  // 200
             * int value = point.value();  // 300
             *
             * // Common use case: grid cells with counts or indices
             * Points.D2.IntIntPoint gridCell = Points.D2.IntIntPoint.of(10, 20, 5000);
             *
             * // Use in pathfinding with cost values
             * Points.D2.IntIntPoint pathNode = Points.D2.IntIntPoint.of(5, 8, 15);  // cost = 15
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
             * // Create a point with integer coordinates and long value
             * Points.D2.IntLongPoint point = Points.D2.IntLongPoint.of(50, 75, 10000000000L);
             * int x = point.x();  // 50
             * int y = point.y();  // 75
             * long value = point.value();  // 10000000000
             *
             * // Useful for grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.D2.IntLongPoint cellWithTime = Points.D2.IntLongPoint.of(10, 20, timestamp);
             *
             * // Track large identifiers in a grid
             * Points.D2.IntLongPoint cellWithId = Points.D2.IntLongPoint.of(5, 8, 9876543210L);
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
             * // Create a point with integer coordinates and double value
             * Points.D2.IntDoublePoint point = Points.D2.IntDoublePoint.of(10, 20, 3.14159);
             * int x = point.x();  // 10
             * int y = point.y();  // 20
             * double value = point.value();  // 3.14159
             *
             * // Useful for grids with probability values
             * Points.D2.IntDoublePoint cellProbability = Points.D2.IntDoublePoint.of(5, 8, 0.85);
             *
             * // Distance-based calculations
             * double distance = Math.sqrt(10 * 10 + 20 * 20);
             * Points.D2.IntDoublePoint nodeWithDistance = Points.D2.IntDoublePoint.of(10, 20, distance);
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
             * // Create a point with integer coordinates and a String value
             * Points.D2.IntObjPoint<String> point = Points.D2.IntObjPoint.of(10, 20, "label");
             * int x = point.x();  // 10
             * int y = point.y();  // 20
             * String value = point.value();  // "label"
             *
             * // Grid with custom objects
             * record Cell(String type, int priority, boolean active) {}
             * Cell cell = new Cell("wall", 5, true);
             * Points.D2.IntObjPoint<Cell> gridCell = Points.D2.IntObjPoint.of(5, 8, cell);
             *
             * // Store collections at grid positions
             * List<String> items = List.of("item1", "item2");
             * Points.D2.IntObjPoint<List<String>> cellItems = Points.D2.IntObjPoint.of(3, 7, items);
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
             * // Create a point with long coordinates and byte value
             * Points.D2.LongBytePoint point = Points.D2.LongBytePoint.of(1000000L, 2000000L, (byte) 5);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * byte value = point.value();  // 5
             *
             * // Useful for very large grids with small enumeration values
             * Points.D2.LongBytePoint cellType = Points.D2.LongBytePoint.of(999999999L, 888888888L, (byte) 2);
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
             * // Create a point with long coordinates and integer value
             * Points.D2.LongIntPoint point = Points.D2.LongIntPoint.of(1000000L, 2000000L, 500);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * int value = point.value();  // 500
             *
             * // Useful for very large grids with counts or indices
             * Points.D2.LongIntPoint cellCount = Points.D2.LongIntPoint.of(999999999L, 888888888L, 1000000);
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
             * // Create a point with long coordinates and long value
             * Points.D2.LongLongPoint point = Points.D2.LongLongPoint.of(1000000L, 2000000L, 3000000000L);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long value = point.value();  // 3000000000
             *
             * // Useful for very large grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.D2.LongLongPoint cellTime = Points.D2.LongLongPoint.of(999999999L, 888888888L, timestamp);
             *
             * // Large coordinate space with large identifiers
             * Points.D2.LongLongPoint cellId = Points.D2.LongLongPoint.of(1L << 40, 1L << 41, 1L << 50);
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
             * // Create a point with long coordinates and double value
             * Points.D2.LongDoublePoint point = Points.D2.LongDoublePoint.of(1000000L, 2000000L, 3.14159);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * double value = point.value();  // 3.14159
             *
             * // Useful for very large grids with probability or weight values
             * Points.D2.LongDoublePoint cellWeight = Points.D2.LongDoublePoint.of(999999999L, 888888888L, 0.85);
             *
             * // Large coordinate space with distance calculations
             * double distance = Math.sqrt(1000000.0 * 1000000.0 + 2000000.0 * 2000000.0);
             * Points.D2.LongDoublePoint cellDistance = Points.D2.LongDoublePoint.of(1000000L, 2000000L, distance);
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
             * // Create a point with long coordinates and a String value
             * Points.D2.LongObjPoint<String> point = Points.D2.LongObjPoint.of(1000000L, 2000000L, "marker");
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * String value = point.value();  // "marker"
             *
             * // Very large grid with custom objects
             * record Region(String name, int population) {}
             * Region region = new Region("Zone-A", 1000000);
             * Points.D2.LongObjPoint<Region> gridRegion = Points.D2.LongObjPoint.of(999999999L, 888888888L, region);
             *
             * // Spatial indexing with metadata
             * Map<String, Object> metadata = Map.of("type", "city", "size", 500000);
             * Points.D2.LongObjPoint<Map<String, Object>> location = Points.D2.LongObjPoint.of(1L << 40, 1L << 41, metadata);
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
             * // Create a point with double coordinates and byte value
             * Points.D2.DoubleBytePoint point = Points.D2.DoubleBytePoint.of(10.5, 20.7, (byte) 3);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * byte value = point.value();  // 3
             *
             * // Useful for continuous coordinate spaces with small enumeration values
             * Points.D2.DoubleBytePoint region = Points.D2.DoubleBytePoint.of(3.14159, 2.71828, (byte) 1);
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
             * // Create a point with double coordinates and integer value
             * Points.D2.DoubleIntPoint point = Points.D2.DoubleIntPoint.of(10.5, 20.7, 100);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * int value = point.value();  // 100
             *
             * // Useful for continuous coordinate spaces with counts
             * Points.D2.DoubleIntPoint sample = Points.D2.DoubleIntPoint.of(3.14159, 2.71828, 1000);
             *
             * // Geographic coordinates with elevation
             * Points.D2.DoubleIntPoint geoPoint = Points.D2.DoubleIntPoint.of(40.7128, -74.0060, 10);  // elevation in meters
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
             * // Create a point with double coordinates and long value
             * Points.D2.DoubleLongPoint point = Points.D2.DoubleLongPoint.of(10.5, 20.7, 1000000000L);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * long value = point.value();  // 1000000000
             *
             * // Useful for continuous coordinate spaces with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.D2.DoubleLongPoint sample = Points.D2.DoubleLongPoint.of(3.14159, 2.71828, timestamp);
             *
             * // Geographic coordinates with large identifiers
             * Points.D2.DoubleLongPoint location = Points.D2.DoubleLongPoint.of(40.7128, -74.0060, 9876543210L);
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
             * // Create a point with double coordinates and double value
             * Points.D2.DoubleDoublePoint point = Points.D2.DoubleDoublePoint.of(10.5, 20.7, 3.14159);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double value = point.value();  // 3.14159
             *
             * // Useful for continuous spaces with probability or weight values
             * Points.D2.DoubleDoublePoint sample = Points.D2.DoubleDoublePoint.of(3.14159, 2.71828, 0.95);
             *
             * // Geographic coordinates with temperature
             * Points.D2.DoubleDoublePoint weather = Points.D2.DoubleDoublePoint.of(40.7128, -74.0060, 22.5);
             *
             * // Mathematical calculations
             * double distance = Math.sqrt(10.5 * 10.5 + 20.7 * 20.7);
             * Points.D2.DoubleDoublePoint vector = Points.D2.DoubleDoublePoint.of(10.5, 20.7, distance);
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
             * // Create a point with double coordinates and a String value
             * Points.D2.DoubleObjPoint<String> point = Points.D2.DoubleObjPoint.of(10.5, 20.7, "location");
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * String value = point.value();  // "location"
             *
             * // Geographic coordinates with custom objects
             * record Place(String name, int population, String country) {}
             * Place place = new Place("New York", 8000000, "USA");
             * Points.D2.DoubleObjPoint<Place> geoPoint = Points.D2.DoubleObjPoint.of(40.7128, -74.0060, place);
             *
             * // Continuous space with metadata
             * Map<String, Object> metadata = Map.of("temperature", 22.5, "humidity", 65);
             * Points.D2.DoubleObjPoint<Map<String, Object>> sensor = Points.D2.DoubleObjPoint.of(3.14159, 2.71828, metadata);
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
             * // Create a 3D point at coordinates (10, 20, 30) with value 5
             * Points.D3.ByteBytePoint point = Points.D3.ByteBytePoint.of((byte) 10, (byte) 20, (byte) 30, (byte) 5);
             * byte x = point.x();  // 10
             * byte y = point.y();  // 20
             * byte z = point.z();  // 30
             * byte value = point.value();  // 5
             *
             * // Useful for memory-efficient 3D grid representations
             * Points.D3.ByteBytePoint voxel = Points.D3.ByteBytePoint.of((byte) 0, (byte) 0, (byte) 0, (byte) 127);
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
             * // Create a 3D point with byte coordinates and integer value
             * Points.D3.ByteIntPoint point = Points.D3.ByteIntPoint.of((byte) 5, (byte) 10, (byte) 15, 1000);
             * byte x = point.x();  // 5
             * byte y = point.y();  // 10
             * byte z = point.z();  // 15
             * int value = point.value();  // 1000
             *
             * // Useful for small 3D grids with large counts
             * Points.D3.ByteIntPoint voxelCount = Points.D3.ByteIntPoint.of((byte) 0, (byte) 0, (byte) 0, 1000000);
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
             * // Create a 3D point with byte coordinates and long value
             * Points.D3.ByteLongPoint point = Points.D3.ByteLongPoint.of((byte) 3, (byte) 7, (byte) 11, 1000000000L);
             * byte x = point.x();  // 3
             * byte y = point.y();  // 7
             * byte z = point.z();  // 11
             * long value = point.value();  // 1000000000
             *
             * // Useful for small 3D grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.D3.ByteLongPoint voxelTime = Points.D3.ByteLongPoint.of((byte) 0, (byte) 0, (byte) 0, timestamp);
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
             * // Create a 3D point with byte coordinates and double value
             * Points.D3.ByteDoublePoint point = Points.D3.ByteDoublePoint.of((byte) 2, (byte) 4, (byte) 6, 3.14159);
             * byte x = point.x();  // 2
             * byte y = point.y();  // 4
             * byte z = point.z();  // 6
             * double value = point.value();  // 3.14159
             *
             * // Useful for small 3D grids with density values
             * Points.D3.ByteDoublePoint voxelDensity = Points.D3.ByteDoublePoint.of((byte) 1, (byte) 1, (byte) 1, 0.85);
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
             * // Create a 3D point with byte coordinates and a String value
             * Points.D3.ByteObjPoint<String> point = Points.D3.ByteObjPoint.of((byte) 1, (byte) 2, (byte) 3, "voxel");
             * byte x = point.x();  // 1
             * byte y = point.y();  // 2
             * byte z = point.z();  // 3
             * String value = point.value();  // "voxel"
             *
             * // Useful for small 3D grids with complex metadata
             * record Material(String type, double density) {}
             * Material material = new Material("iron", 7.87);
             * Points.D3.ByteObjPoint<Material> voxelMaterial = Points.D3.ByteObjPoint.of((byte) 0, (byte) 0, (byte) 0, material);
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
             * // Create a 3D point with integer coordinates and byte value
             * Points.D3.IntBytePoint point = Points.D3.IntBytePoint.of(100, 200, 300, (byte) 10);
             * int x = point.x();  // 100
             * int y = point.y();  // 200
             * int z = point.z();  // 300
             * byte value = point.value();  // 10
             *
             * // Useful for large 3D grids with small enumeration values
             * Points.D3.IntBytePoint voxelType = Points.D3.IntBytePoint.of(1000, 2000, 3000, (byte) 3);  // type = 3
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
             * // Create a 3D point with integer coordinates and integer value
             * Points.D3.IntIntPoint point = Points.D3.IntIntPoint.of(100, 200, 300, 400);
             * int x = point.x();  // 100
             * int y = point.y();  // 200
             * int z = point.z();  // 300
             * int value = point.value();  // 400
             *
             * // Common use case: 3D voxel grids with counts
             * Points.D3.IntIntPoint voxel = Points.D3.IntIntPoint.of(10, 20, 30, 5000);
             *
             * // 3D pathfinding with cost values
             * Points.D3.IntIntPoint pathNode = Points.D3.IntIntPoint.of(5, 8, 12, 25);  // cost = 25
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
             * // Create a 3D point with integer coordinates and long value
             * Points.D3.IntLongPoint point = Points.D3.IntLongPoint.of(50, 75, 100, 10000000000L);
             * int x = point.x();  // 50
             * int y = point.y();  // 75
             * int z = point.z();  // 100
             * long value = point.value();  // 10000000000
             *
             * // Useful for 3D grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.D3.IntLongPoint voxelTime = Points.D3.IntLongPoint.of(10, 20, 30, timestamp);
             *
             * // Track large identifiers in a 3D grid
             * Points.D3.IntLongPoint voxelId = Points.D3.IntLongPoint.of(5, 8, 12, 9876543210L);
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
             * // Create a 3D point with integer coordinates and double value
             * Points.D3.IntDoublePoint point = Points.D3.IntDoublePoint.of(10, 20, 30, 3.14159);
             * int x = point.x();  // 10
             * int y = point.y();  // 20
             * int z = point.z();  // 30
             * double value = point.value();  // 3.14159
             *
             * // Useful for 3D grids with density values
             * Points.D3.IntDoublePoint voxelDensity = Points.D3.IntDoublePoint.of(5, 8, 12, 0.85);
             *
             * // 3D distance-based calculations
             * double distance = Math.sqrt(10 * 10 + 20 * 20 + 30 * 30);
             * Points.D3.IntDoublePoint nodeDistance = Points.D3.IntDoublePoint.of(10, 20, 30, distance);
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
             * // Create a 3D point with integer coordinates and a String value
             * Points.D3.IntObjPoint<String> point = Points.D3.IntObjPoint.of(10, 20, 30, "block");
             * int x = point.x();  // 10
             * int y = point.y();  // 20
             * int z = point.z();  // 30
             * String value = point.value();  // "block"
             *
             * // 3D voxel grid with custom objects
             * record Voxel(String material, double density, boolean solid) {}
             * Voxel voxel = new Voxel("stone", 2.5, true);
             * Points.D3.IntObjPoint<Voxel> gridVoxel = Points.D3.IntObjPoint.of(5, 8, 12, voxel);
             *
             * // Store collections at 3D positions
             * List<String> items = List.of("chest", "gold");
             * Points.D3.IntObjPoint<List<String>> voxelItems = Points.D3.IntObjPoint.of(3, 7, 10, items);
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
             * // Create a 3D point with long coordinates and byte value
             * Points.D3.LongBytePoint point = Points.D3.LongBytePoint.of(1000000L, 2000000L, 3000000L, (byte) 5);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long z = point.z();  // 3000000
             * byte value = point.value();  // 5
             *
             * // Useful for very large 3D grids with small enumeration values
             * Points.D3.LongBytePoint voxelType = Points.D3.LongBytePoint.of(999999999L, 888888888L, 777777777L, (byte) 2);
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
             * // Create a 3D point with long coordinates and integer value
             * Points.D3.LongIntPoint point = Points.D3.LongIntPoint.of(1000000L, 2000000L, 3000000L, 500);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long z = point.z();  // 3000000
             * int value = point.value();  // 500
             *
             * // Useful for very large 3D grids with counts or indices
             * Points.D3.LongIntPoint voxelCount = Points.D3.LongIntPoint.of(999999999L, 888888888L, 777777777L, 1000000);
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
             * // Create a 3D point with long coordinates and long value
             * Points.D3.LongLongPoint point = Points.D3.LongLongPoint.of(1000000L, 2000000L, 3000000L, 4000000000L);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long z = point.z();  // 3000000
             * long value = point.value();  // 4000000000
             *
             * // Useful for very large 3D grids with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.D3.LongLongPoint voxelTime = Points.D3.LongLongPoint.of(999999999L, 888888888L, 777777777L, timestamp);
             *
             * // Large 3D coordinate space with large identifiers
             * Points.D3.LongLongPoint voxelId = Points.D3.LongLongPoint.of(1L << 40, 1L << 41, 1L << 42, 1L << 50);
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
             * // Create a 3D point with long coordinates and double value
             * Points.D3.LongDoublePoint point = Points.D3.LongDoublePoint.of(1000000L, 2000000L, 3000000L, 3.14159);
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long z = point.z();  // 3000000
             * double value = point.value();  // 3.14159
             *
             * // Useful for very large 3D grids with density or probability values
             * Points.D3.LongDoublePoint voxelDensity = Points.D3.LongDoublePoint.of(999999999L, 888888888L, 777777777L, 0.85);
             *
             * // Large 3D coordinate space with distance calculations
             * double distance = Math.sqrt(1000000.0 * 1000000.0 + 2000000.0 * 2000000.0 + 3000000.0 * 3000000.0);
             * Points.D3.LongDoublePoint voxelDistance = Points.D3.LongDoublePoint.of(1000000L, 2000000L, 3000000L, distance);
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
             * // Create a 3D point with long coordinates and a String value
             * Points.D3.LongObjPoint<String> point = Points.D3.LongObjPoint.of(1000000L, 2000000L, 3000000L, "region");
             * long x = point.x();  // 1000000
             * long y = point.y();  // 2000000
             * long z = point.z();  // 3000000
             * String value = point.value();  // "region"
             *
             * // Very large 3D grid with custom objects
             * record Chunk(String biome, int height, boolean generated) {}
             * Chunk chunk = new Chunk("plains", 64, true);
             * Points.D3.LongObjPoint<Chunk> gridChunk = Points.D3.LongObjPoint.of(999999999L, 888888888L, 777777777L, chunk);
             *
             * // 3D spatial indexing with metadata
             * Map<String, Object> metadata = Map.of("type", "structure", "volume", 1000);
             * Points.D3.LongObjPoint<Map<String, Object>> voxel = Points.D3.LongObjPoint.of(1L << 40, 1L << 41, 1L << 42, metadata);
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
             * // Create a 3D point with double coordinates and byte value
             * Points.D3.DoubleBytePoint point = Points.D3.DoubleBytePoint.of(10.5, 20.7, 30.9, (byte) 3);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double z = point.z();  // 30.9
             * byte value = point.value();  // 3
             *
             * // Useful for continuous 3D coordinate spaces with small enumeration values
             * Points.D3.DoubleBytePoint region = Points.D3.DoubleBytePoint.of(3.14159, 2.71828, 1.41421, (byte) 1);
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
             * // Create a 3D point with double coordinates and integer value
             * Points.D3.DoubleIntPoint point = Points.D3.DoubleIntPoint.of(10.5, 20.7, 30.9, 100);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double z = point.z();  // 30.9
             * int value = point.value();  // 100
             *
             * // Useful for continuous 3D coordinate spaces with counts
             * Points.D3.DoubleIntPoint sample = Points.D3.DoubleIntPoint.of(3.14159, 2.71828, 1.41421, 1000);
             *
             * // 3D physical coordinates with count values
             * Points.D3.DoubleIntPoint position = Points.D3.DoubleIntPoint.of(1.5, 2.7, 3.9, 42);
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
             * // Create a 3D point with double coordinates and long value
             * Points.D3.DoubleLongPoint point = Points.D3.DoubleLongPoint.of(10.5, 20.7, 30.9, 1000000000L);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double z = point.z();  // 30.9
             * long value = point.value();  // 1000000000
             *
             * // Useful for continuous 3D coordinate spaces with timestamps
             * long timestamp = System.currentTimeMillis();
             * Points.D3.DoubleLongPoint sample = Points.D3.DoubleLongPoint.of(3.14159, 2.71828, 1.41421, timestamp);
             *
             * // 3D physical coordinates with large identifiers
             * Points.D3.DoubleLongPoint position = Points.D3.DoubleLongPoint.of(1.5, 2.7, 3.9, 9876543210L);
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
             * // Create a 3D point with double coordinates and double value
             * Points.D3.DoubleDoublePoint point = Points.D3.DoubleDoublePoint.of(10.5, 20.7, 30.9, 3.14159);
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double z = point.z();  // 30.9
             * double value = point.value();  // 3.14159
             *
             * // Useful for continuous 3D spaces with density or probability values
             * Points.D3.DoubleDoublePoint sample = Points.D3.DoubleDoublePoint.of(3.14159, 2.71828, 1.41421, 0.95);
             *
             * // 3D physical coordinates with measurements
             * Points.D3.DoubleDoublePoint position = Points.D3.DoubleDoublePoint.of(1.5, 2.7, 3.9, 22.5);
             *
             * // Mathematical calculations in 3D space
             * double distance = Math.sqrt(10.5 * 10.5 + 20.7 * 20.7 + 30.9 * 30.9);
             * Points.D3.DoubleDoublePoint vector = Points.D3.DoubleDoublePoint.of(10.5, 20.7, 30.9, distance);
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
             * // Create a 3D point with double coordinates and a String value
             * Points.D3.DoubleObjPoint<String> point = Points.D3.DoubleObjPoint.of(10.5, 20.7, 30.9, "marker");
             * double x = point.x();  // 10.5
             * double y = point.y();  // 20.7
             * double z = point.z();  // 30.9
             * String value = point.value();  // "marker"
             *
             * // 3D physical space with custom objects
             * record Particle(String type, double mass, double charge) {}
             * Particle particle = new Particle("electron", 9.109e-31, -1.602e-19);
             * Points.D3.DoubleObjPoint<Particle> position = Points.D3.DoubleObjPoint.of(1.5, 2.7, 3.9, particle);
             *
             * // Continuous 3D space with metadata
             * Map<String, Object> metadata = Map.of("temperature", 22.5, "pressure", 101.3, "humidity", 65);
             * Points.D3.DoubleObjPoint<Map<String, Object>> sensor = Points.D3.DoubleObjPoint.of(3.14159, 2.71828, 1.41421, metadata);
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
