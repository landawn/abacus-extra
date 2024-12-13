/*
 * Copyright (c) 2019, Haiyang Li.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.landawn.abacus.util;

// TODO: Auto-generated Javadoc

import com.landawn.abacus.annotation.SuppressFBWarnings;

/**
 *
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
         * The Class ByteBytePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteBytePoint implements Immutable {

            /** The x. */
            public final byte x;

            /** The y. */
            public final byte y;

            /** The v. */
            public final byte v; // value

            /**
             * Instantiates a new byte byte point.
             *
             * @param x
             * @param y
             * @param v
             */
            ByteBytePoint(final byte x, final byte y, final byte v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static ByteBytePoint of(final byte x, final byte y, final byte v) {
                return new ByteBytePoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class ByteIntPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteIntPoint implements Immutable {

            /** The x. */
            public final byte x;

            /** The y. */
            public final byte y;

            /** The v. */
            public final int v; // value

            /**
             * Instantiates a new byte int point.
             *
             * @param x
             * @param y
             * @param v
             */
            ByteIntPoint(final byte x, final byte y, final int v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static ByteIntPoint of(final byte x, final byte y, final int v) {
                return new ByteIntPoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class ByteLongPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteLongPoint implements Immutable {

            /** The x. */
            public final byte x;

            /** The y. */
            public final byte y;

            /** The v. */
            public final long v; // value

            /**
             * Instantiates a new byte long point.
             *
             * @param x
             * @param y
             * @param v
             */
            ByteLongPoint(final byte x, final byte y, final long v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static ByteLongPoint of(final byte x, final byte y, final long v) {
                return new ByteLongPoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class ByteDoublePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteDoublePoint implements Immutable {

            /** The x. */
            public final byte x;

            /** The y. */
            public final byte y;

            /** The v. */
            public final double v; // value

            /**
             * Instantiates a new byte double point.
             *
             * @param x
             * @param y
             * @param v
             */
            ByteDoublePoint(final byte x, final byte y, final double v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static ByteDoublePoint of(final byte x, final byte y, final double v) {
                return new ByteDoublePoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class ByteObjPoint.
         *
         * @param <T>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteObjPoint<T> implements Immutable {

            /** The x. */
            public final byte x;

            /** The y. */
            public final byte y;

            /** The v. */
            public final T v; // value

            /**
             * Instantiates a new byte obj point.
             *
             * @param x
             * @param y
             * @param v
             */
            ByteObjPoint(final byte x, final byte y, final T v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param <T>
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static <T> ByteObjPoint<T> of(final byte x, final byte y, final T v) {
                return new ByteObjPoint<>(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + N.hashCode(v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
            }
        }

        /**
         * The Class IntBytePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntBytePoint implements Immutable {

            /** The x. */
            public final int x;

            /** The y. */
            public final int y;

            /** The v. */
            public final byte v; // value

            /**
             * Instantiates a new int byte point.
             *
             * @param x
             * @param y
             * @param v
             */
            IntBytePoint(final int x, final int y, final byte v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static IntBytePoint of(final int x, final int y, final byte v) {
                return new IntBytePoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class IntIntPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntIntPoint implements Immutable {

            /** The x. */
            public final int x;

            /** The y. */
            public final int y;

            /** The v. */
            public final int v; // value

            /**
             * Instantiates a new int int point.
             *
             * @param x
             * @param y
             * @param v
             */
            IntIntPoint(final int x, final int y, final int v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static IntIntPoint of(final int x, final int y, final int v) {
                return new IntIntPoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + v;
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class IntLongPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntLongPoint implements Immutable {

            /** The x. */
            public final int x;

            /** The y. */
            public final int y;

            /** The v. */
            public final long v; // value

            /**
             * Instantiates a new int long point.
             *
             * @param x
             * @param y
             * @param v
             */
            IntLongPoint(final int x, final int y, final long v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static IntLongPoint of(final int x, final int y, final long v) {
                return new IntLongPoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class IntDoublePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntDoublePoint implements Immutable {

            /** The x. */
            public final int x;

            /** The y. */
            public final int y;

            /** The v. */
            public final double v; // value

            /**
             * Instantiates a new int double point.
             *
             * @param x
             * @param y
             * @param v
             */
            IntDoublePoint(final int x, final int y, final double v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static IntDoublePoint of(final int x, final int y, final double v) {
                return new IntDoublePoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class IntObjPoint.
         *
         * @param <T>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntObjPoint<T> implements Immutable {

            /** The x. */
            public final int x;

            /** The y. */
            public final int y;

            /** The v. */
            public final T v; // value

            /**
             * Instantiates a new int obj point.
             *
             * @param x
             * @param y
             * @param v
             */
            IntObjPoint(final int x, final int y, final T v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param <T>
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static <T> IntObjPoint<T> of(final int x, final int y, final T v) {
                return new IntObjPoint<>(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (x * 31 + y) * 31 + N.hashCode(v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
            }
        }

        /**
         * The Class LongBytePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongBytePoint implements Immutable {

            /** The x. */
            public final long x;

            /** The y. */
            public final long y;

            /** The v. */
            public final byte v; // value

            /**
             * Instantiates a new long byte point.
             *
             * @param x
             * @param y
             * @param v
             */
            LongBytePoint(final long x, final long y, final byte v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static LongBytePoint of(final long x, final long y, final byte v) {
                return new LongBytePoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class LongIntPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongIntPoint implements Immutable {

            /** The x. */
            public final long x;

            /** The y. */
            public final long y;

            /** The v. */
            public final int v; // value

            /**
             * Instantiates a new long int point.
             *
             * @param x
             * @param y
             * @param v
             */
            LongIntPoint(final long x, final long y, final int v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static LongIntPoint of(final long x, final long y, final int v) {
                return new LongIntPoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class LongLongPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongLongPoint implements Immutable {

            /** The x. */
            public final long x;

            /** The y. */
            public final long y;

            /** The v. */
            public final long v; // value

            /**
             * Instantiates a new long long point.
             *
             * @param x
             * @param y
             * @param v
             */
            LongLongPoint(final long x, final long y, final long v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static LongLongPoint of(final long x, final long y, final long v) {
                return new LongLongPoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class LongDoublePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongDoublePoint implements Immutable {

            /** The x. */
            public final long x;

            /** The y. */
            public final long y;

            /** The v. */
            public final double v; // value

            /**
             * Instantiates a new long double point.
             *
             * @param x
             * @param y
             * @param v
             */
            LongDoublePoint(final long x, final long y, final double v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static LongDoublePoint of(final long x, final long y, final double v) {
                return new LongDoublePoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class LongObjPoint.
         *
         * @param <T>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongObjPoint<T> implements Immutable {

            /** The x. */
            public final long x;

            /** The y. */
            public final long y;

            /** The v. */
            public final T v; // value

            /**
             * Instantiates a new long obj point.
             *
             * @param x
             * @param y
             * @param v
             */
            LongObjPoint(final long x, final long y, final T v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param <T>
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static <T> LongObjPoint<T> of(final long x, final long y, final T v) {
                return new LongObjPoint<>(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + N.hashCode(v));
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + N.toString(v) + "]";
            }
        }

        /**
         * The Class DoubleBytePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleBytePoint implements Immutable {

            /** The x. */
            public final double x;

            /** The y. */
            public final double y;

            /** The v. */
            public final byte v; // value

            /**
             * Instantiates a new double byte point.
             *
             * @param x
             * @param y
             * @param v
             */
            DoubleBytePoint(final double x, final double y, final byte v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static DoubleBytePoint of(final double x, final double y, final byte v) {
                return new DoubleBytePoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class DoubleIntPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleIntPoint implements Immutable {

            /** The x. */
            public final double x;

            /** The y. */
            public final double y;

            /** The v. */
            public final int v; // value

            /**
             * Instantiates a new double int point.
             *
             * @param x
             * @param y
             * @param v
             */
            DoubleIntPoint(final double x, final double y, final int v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static DoubleIntPoint of(final double x, final double y, final int v) {
                return new DoubleIntPoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class DoubleLongPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleLongPoint implements Immutable {

            /** The x. */
            public final double x;

            /** The y. */
            public final double y;

            /** The v. */
            public final long v; // value

            /**
             * Instantiates a new double long point.
             *
             * @param x
             * @param y
             * @param v
             */
            DoubleLongPoint(final double x, final double y, final long v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static DoubleLongPoint of(final double x, final double y, final long v) {
                return new DoubleLongPoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class DoubleDoublePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleDoublePoint implements Immutable {

            /** The x. */
            public final double x;

            /** The y. */
            public final double y;

            /** The v. */
            public final double v; // value

            /**
             * Instantiates a new double double point.
             *
             * @param x
             * @param y
             * @param v
             */
            DoubleDoublePoint(final double x, final double y, final double v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static DoubleDoublePoint of(final double x, final double y, final double v) {
                return new DoubleDoublePoint(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + v + "]";
            }
        }

        /**
         * The Class DoubleObjPoint.
         *
         * @param <T>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleObjPoint<T> implements Immutable {

            /** The x. */
            public final double x;

            /** The y. */
            public final double y;

            /** The v. */
            public final T v; // value

            /**
             * Instantiates a new double obj point.
             *
             * @param x
             * @param y
             * @param v
             */
            DoubleObjPoint(final double x, final double y, final T v) {
                this.x = x;
                this.y = y;
                this.v = v;
            }

            /**
             *
             * @param <T>
             * @param x
             * @param y
             * @param v
             * @return
             */
            public static <T> DoubleObjPoint<T> of(final double x, final double y, final T v) {
                return new DoubleObjPoint<>(x, y, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) ((x * 31 + y) * 31 + N.hashCode(v));
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
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
             *
             * @return
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

    /**
     * The Class xyz.
     */
    @SuppressFBWarnings("NM_CLASS_NAMING_CONVENTION")
    @com.landawn.abacus.annotation.Immutable
    public static final class xyz { // NOSONAR

        /**
         * Instantiates a new xyz.
         */
        private xyz() {
            // singleton.
        }

        /**
         * The Class ByteBytePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteBytePoint implements Immutable {

            /** The x. */
            public final byte x;

            /** The y. */
            public final byte y;

            /** The z. */
            public final byte z;

            /** The v. */
            public final byte v; // value

            /**
             * Instantiates a new byte byte point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            ByteBytePoint(final byte x, final byte y, final byte z, final byte v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static ByteBytePoint of(final byte x, final byte y, final byte z, final byte v) {
                return new ByteBytePoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return ((x * 31 + y) * 31 + z) * 31 + v;
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final ByteBytePoint other) {
                    return x == other.x && y == other.y && z == other.z && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class ByteIntPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteIntPoint implements Immutable {

            /** The x. */
            public final byte x;

            /** The y. */
            public final byte y;

            /** The z. */
            public final byte z;

            /** The v. */
            public final int v; // value

            /**
             * Instantiates a new byte int point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            ByteIntPoint(final byte x, final byte y, final byte z, final int v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static ByteIntPoint of(final byte x, final byte y, final byte z, final int v) {
                return new ByteIntPoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return ((x * 31 + y) * 31 + z) * 31 + v;
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final ByteIntPoint other) {
                    return x == other.x && y == other.y && z == other.z && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class ByteLongPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteLongPoint implements Immutable {

            /** The x. */
            public final byte x;

            /** The y. */
            public final byte y;

            /** The z. */
            public final byte z;

            /** The v. */
            public final long v; // value

            /**
             * Instantiates a new byte long point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            ByteLongPoint(final byte x, final byte y, final byte z, final long v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static ByteLongPoint of(final byte x, final byte y, final byte z, final long v) {
                return new ByteLongPoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final ByteLongPoint other) {
                    return x == other.x && y == other.y && z == other.z && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class ByteDoublePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteDoublePoint implements Immutable {

            /** The x. */
            public final byte x;

            /** The y. */
            public final byte y;

            /** The z. */
            public final byte z;

            /** The v. */
            public final double v; // value

            /**
             * Instantiates a new byte double point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            ByteDoublePoint(final byte x, final byte y, final byte z, final double v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static ByteDoublePoint of(final byte x, final byte y, final byte z, final double v) {
                return new ByteDoublePoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final ByteDoublePoint other) {
                    return x == other.x && y == other.y && z == other.z && N.equals(v, other.v);
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class ByteObjPoint.
         *
         * @param <T>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class ByteObjPoint<T> {

            /** The x. */
            public final byte x;

            /** The y. */
            public final byte y;

            /** The z. */
            public final byte z;

            /** The v. */
            public final T v; // value

            /**
             * Instantiates a new byte obj point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            ByteObjPoint(final byte x, final byte y, final byte z, final T v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param <T>
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static <T> ByteObjPoint<T> of(final byte x, final byte y, final byte z, final T v) {
                return new ByteObjPoint<>(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return ((x * 31 + y) * 31 + z) * 31 + N.hashCode(v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof ByteObjPoint<?> other) {
                    return x == other.x && y == other.y && z == other.z && N.equals(v, other.v);
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + N.toString(v) + "]";
            }
        }

        /**
         * The Class IntBytePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntBytePoint implements Immutable {

            /** The x. */
            public final int x;

            /** The y. */
            public final int y;

            /** The z. */
            public final int z;

            /** The v. */
            public final byte v; // value

            /**
             * Instantiates a new int byte point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            IntBytePoint(final int x, final int y, final int z, final byte v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static IntBytePoint of(final int x, final int y, final int z, final byte v) {
                return new IntBytePoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return ((x * 31 + y) * 31 + z) * 31 + v;
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final IntBytePoint other) {
                    return x == other.x && y == other.y && z == other.z && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class IntIntPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntIntPoint implements Immutable {

            /** The x. */
            public final int x;

            /** The y. */
            public final int y;

            /** The z. */
            public final int z;

            /** The v. */
            public final int v; // value

            /**
             * Instantiates a new int int point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            IntIntPoint(final int x, final int y, final int z, final int v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static IntIntPoint of(final int x, final int y, final int z, final int v) {
                return new IntIntPoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return ((x * 31 + y) * 31 + z) * 31 + v;
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final IntIntPoint other) {
                    return x == other.x && y == other.y && z == other.z && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class IntLongPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntLongPoint implements Immutable {

            /** The x. */
            public final int x;

            /** The y. */
            public final int y;

            /** The z. */
            public final int z;

            /** The v. */
            public final long v; // value

            /**
             * Instantiates a new int long point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            IntLongPoint(final int x, final int y, final int z, final long v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static IntLongPoint of(final int x, final int y, final int z, final long v) {
                return new IntLongPoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final IntLongPoint other) {
                    return x == other.x && y == other.y && z == other.z && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class IntDoublePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntDoublePoint implements Immutable {

            /** The x. */
            public final int x;

            /** The y. */
            public final int y;

            /** The z. */
            public final int z;

            /** The v. */
            public final double v; // value

            /**
             * Instantiates a new int double point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            IntDoublePoint(final int x, final int y, final int z, final double v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static IntDoublePoint of(final int x, final int y, final int z, final double v) {
                return new IntDoublePoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final IntDoublePoint other) {
                    return x == other.x && y == other.y && z == other.z && N.equals(v, other.v);
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class IntObjPoint.
         *
         * @param <T>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class IntObjPoint<T> {

            /** The x. */
            public final int x;

            /** The y. */
            public final int y;

            /** The z. */
            public final int z;

            /** The v. */
            public final T v; // value

            /**
             * Instantiates a new int obj point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            IntObjPoint(final int x, final int y, final int z, final T v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param <T>
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static <T> IntObjPoint<T> of(final int x, final int y, final int z, final T v) {
                return new IntObjPoint<>(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return ((x * 31 + y) * 31 + z) * 31 + N.hashCode(v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof IntObjPoint<?> other) {
                    return x == other.x && y == other.y && z == other.z && N.equals(v, other.v);
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + N.toString(v) + "]";
            }
        }

        /**
         * The Class LongBytePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongBytePoint implements Immutable {

            /** The x. */
            public final long x;

            /** The y. */
            public final long y;

            /** The z. */
            public final long z;

            /** The v. */
            public final byte v; // value

            /**
             * Instantiates a new long byte point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            LongBytePoint(final long x, final long y, final long z, final byte v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static LongBytePoint of(final long x, final long y, final long z, final byte v) {
                return new LongBytePoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final LongBytePoint other) {
                    return x == other.x && y == other.y && z == other.z && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class LongIntPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongIntPoint implements Immutable {

            /** The x. */
            public final long x;

            /** The y. */
            public final long y;

            /** The z. */
            public final long z;

            /** The v. */
            public final int v; // value

            /**
             * Instantiates a new long int point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            LongIntPoint(final long x, final long y, final long z, final int v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static LongIntPoint of(final long x, final long y, final long z, final int v) {
                return new LongIntPoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final LongIntPoint other) {
                    return x == other.x && y == other.y && z == other.z && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class LongLongPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongLongPoint implements Immutable {

            /** The x. */
            public final long x;

            /** The y. */
            public final long y;

            /** The z. */
            public final long z;

            /** The v. */
            public final long v; // value

            /**
             * Instantiates a new long long point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            LongLongPoint(final long x, final long y, final long z, final long v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static LongLongPoint of(final long x, final long y, final long z, final long v) {
                return new LongLongPoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final LongLongPoint other) {
                    return x == other.x && y == other.y && z == other.z && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class LongDoublePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongDoublePoint implements Immutable {

            /** The x. */
            public final long x;

            /** The y. */
            public final long y;

            /** The z. */
            public final long z;

            /** The v. */
            public final double v; // value

            /**
             * Instantiates a new long double point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            LongDoublePoint(final long x, final long y, final long z, final double v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static LongDoublePoint of(final long x, final long y, final long z, final double v) {
                return new LongDoublePoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final LongDoublePoint other) {
                    return x == other.x && y == other.y && z == other.z && N.equals(v, other.v);
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class LongObjPoint.
         *
         * @param <T>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class LongObjPoint<T> implements Immutable {

            /** The x. */
            public final long x;

            /** The y. */
            public final long y;

            /** The z. */
            public final long z;

            /** The v. */
            public final T v; // value

            /**
             * Instantiates a new long obj point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            LongObjPoint(final long x, final long y, final long z, final T v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param <T>
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static <T> LongObjPoint<T> of(final long x, final long y, final long z, final T v) {
                return new LongObjPoint<>(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + N.hashCode(v));
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof LongObjPoint<?> other) {
                    return x == other.x && y == other.y && z == other.z && N.equals(v, other.v);
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + N.toString(v) + "]";
            }
        }

        /**
         * The Class DoubleBytePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleBytePoint implements Immutable {

            /** The x. */
            public final double x;

            /** The y. */
            public final double y;

            /** The z. */
            public final double z;

            /** The v. */
            public final byte v; // value

            /**
             * Instantiates a new double byte point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            DoubleBytePoint(final double x, final double y, final double z, final byte v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static DoubleBytePoint of(final double x, final double y, final double z, final byte v) {
                return new DoubleBytePoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final DoubleBytePoint other) {
                    return N.equals(x, other.x) && N.equals(y, other.y) && N.equals(z, other.z) && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class DoubleIntPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleIntPoint implements Immutable {

            /** The x. */
            public final double x;

            /** The y. */
            public final double y;

            /** The z. */
            public final double z;

            /** The v. */
            public final int v; // value

            /**
             * Instantiates a new double int point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            DoubleIntPoint(final double x, final double y, final double z, final int v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static DoubleIntPoint of(final double x, final double y, final double z, final int v) {
                return new DoubleIntPoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final DoubleIntPoint other) {
                    return N.equals(x, other.x) && N.equals(y, other.y) && N.equals(z, other.z) && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class DoubleLongPoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleLongPoint implements Immutable {

            /** The x. */
            public final double x;

            /** The y. */
            public final double y;

            /** The z. */
            public final double z;

            /** The v. */
            public final long v; // value

            /**
             * Instantiates a new double long point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            DoubleLongPoint(final double x, final double y, final double z, final long v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static DoubleLongPoint of(final double x, final double y, final double z, final long v) {
                return new DoubleLongPoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final DoubleLongPoint other) {
                    return N.equals(x, other.x) && N.equals(y, other.y) && N.equals(z, other.z) && v == other.v;
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class DoubleDoublePoint.
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleDoublePoint implements Immutable {

            /** The x. */
            public final double x;

            /** The y. */
            public final double y;

            /** The z. */
            public final double z;

            /** The v. */
            public final double v; // value

            /**
             * Instantiates a new double double point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            DoubleDoublePoint(final double x, final double y, final double z, final double v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static DoubleDoublePoint of(final double x, final double y, final double z, final double v) {
                return new DoubleDoublePoint(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof final DoubleDoublePoint other) {
                    return N.equals(x, other.x) && N.equals(y, other.y) && N.equals(z, other.z) && N.equals(v, other.v);
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
            }
        }

        /**
         * The Class DoubleObjPoint.
         *
         * @param <T>
         */
        @com.landawn.abacus.annotation.Immutable
        public static final class DoubleObjPoint<T> implements Mutable {

            /** The x. */
            public final double x;

            /** The y. */
            public final double y;

            /** The z. */
            public final double z;

            /** The v. */
            public final T v; // value

            /**
             * Instantiates a new double obj point.
             *
             * @param x
             * @param y
             * @param z
             * @param v
             */
            DoubleObjPoint(final double x, final double y, final double z, final T v) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.v = v;
            }

            /**
             *
             * @param <T>
             * @param x
             * @param y
             * @param z
             * @param v
             * @return
             */
            public static <T> DoubleObjPoint<T> of(final double x, final double y, final double z, final T v) {
                return new DoubleObjPoint<>(x, y, z, v);
            }

            /**
             *
             * @return
             */
            @Override
            public int hashCode() {
                return (int) (((x * 31 + y) * 31 + z) * 31 + N.hashCode(v));
            }

            /**
             *
             * @param obj
             * @return {@code true}, if successful
             */
            @Override
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }

                if (obj instanceof DoubleObjPoint<?> other) {
                    return N.equals(x, other.x) && N.equals(y, other.y) && N.equals(z, other.z) && N.equals(v, other.v);
                }

                return false;
            }

            /**
             *
             * @return
             */
            @Override
            public String toString() {
                return "[" + x + ", " + y + ", " + z + ", " + N.toString(v) + "]";
            }
        }

        //        public static final class ByteByteTimePoint implements Immutable {
        //            public final byte x;
        //             public final byte y;
        //             public final byte z;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final ByteByteTimePoint() {
        //            }
        //
        //            public final ByteByteTimePoint(byte x, byte y, byte z, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ByteByteTimePoint of(byte x, byte y, byte z, long t, byte v) {
        //                return new ByteByteTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ByteIntTimePoint implements Immutable {
        //            public final byte x;
        //             public final byte y;
        //             public final byte z;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final ByteIntTimePoint() {
        //            }
        //
        //            public final ByteIntTimePoint(byte x, byte y, byte z, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ByteIntTimePoint of(byte x, byte y, byte z, long t, int v) {
        //                return new ByteIntTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ByteLongTimePoint implements Immutable {
        //            public final byte x;
        //             public final byte y;
        //             public final byte z;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final ByteLongTimePoint() {
        //            }
        //
        //            public final ByteLongTimePoint(byte x, byte y, byte z, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ByteLongTimePoint of(byte x, byte y, byte z, long t, long v) {
        //                return new ByteLongTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ByteDoubleTimePoint implements Immutable {
        //            public final byte x;
        //             public final byte y;
        //             public final byte z;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final ByteDoubleTimePoint() {
        //            }
        //
        //            public final ByteDoubleTimePoint(byte x, byte y, byte z, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ByteDoubleTimePoint of(byte x, byte y, byte z, long t, double v) {
        //                return new ByteDoubleTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ByteObjTimePoint<T> {
        //            public final byte x;
        //             public final byte y;
        //             public final byte z;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final ByteObjTimePoint() {
        //            }
        //
        //            public final ByteObjTimePoint(byte x, byte y, byte z, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ByteObjTimePoint<T> of(byte x, byte y, byte z, long t, T v) {
        //                return new ByteObjTimePoint<>(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + N.hashCode(v));
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class IntByteTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final IntByteTimePoint() {
        //            }
        //
        //            public final IntByteTimePoint(int x, int y, int z, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static IntByteTimePoint of(int x, int y, int z, long t, byte v) {
        //                return new IntByteTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class IntIntTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final IntIntTimePoint() {
        //            }
        //
        //            public final IntIntTimePoint(int x, int y, int z, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static IntIntTimePoint of(int x, int y, int z, long t, int v) {
        //                return new IntIntTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class IntLongTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final IntLongTimePoint() {
        //            }
        //
        //            public final IntLongTimePoint(int x, int y, int z, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static IntLongTimePoint of(int x, int y, int z, long t, long v) {
        //                return new IntLongTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class IntDoubleTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final IntDoubleTimePoint() {
        //            }
        //
        //            public final IntDoubleTimePoint(int x, int y, int z, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static IntDoubleTimePoint of(int x, int y, int z, long t, double v) {
        //                return new IntDoubleTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class IntObjTimePoint<T> {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final IntObjTimePoint() {
        //            }
        //
        //            public final IntObjTimePoint(int x, int y, int z, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> IntObjTimePoint<T> of(int x, int y, int z, long t, T v) {
        //                return new IntObjTimePoint<>(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + N.hashCode(v));
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class LongByteTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final LongByteTimePoint() {
        //            }
        //
        //            public final LongByteTimePoint(long x, long y, long z, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static LongByteTimePoint of(long x, long y, long z, long t, byte v) {
        //                return new LongByteTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class LongIntTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final LongIntTimePoint() {
        //            }
        //
        //            public final LongIntTimePoint(long x, long y, long z, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static LongIntTimePoint of(long x, long y, long z, long t, int v) {
        //                return new LongIntTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class LongLongTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final LongLongTimePoint() {
        //            }
        //
        //            public final LongLongTimePoint(long x, long y, long z, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static LongLongTimePoint of(long x, long y, long z, long t, long v) {
        //                return new LongLongTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class LongDoubleTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final LongDoubleTimePoint() {
        //            }
        //
        //            public final LongDoubleTimePoint(long x, long y, long z, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static LongDoubleTimePoint of(long x, long y, long z, long t, double v) {
        //                return new LongDoubleTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class LongObjTimePoint<T> {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final LongObjTimePoint() {
        //            }
        //
        //            public final LongObjTimePoint(long x, long y, long z, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> LongObjTimePoint<T> of(long x, long y, long z, long t, T v) {
        //                return new LongObjTimePoint<>(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + N.hashCode(v));
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class DoubleByteTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final DoubleByteTimePoint() {
        //            }
        //
        //            public final DoubleByteTimePoint(double x, double y, double z, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static DoubleByteTimePoint of(double x, double y, double z, long t, byte v) {
        //                return new DoubleByteTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class DoubleIntTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final DoubleIntTimePoint() {
        //            }
        //
        //            public final DoubleIntTimePoint(double x, double y, double z, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static DoubleIntTimePoint of(double x, double y, double z, long t, int v) {
        //                return new DoubleIntTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class DoubleLongTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final DoubleLongTimePoint() {
        //            }
        //
        //            public final DoubleLongTimePoint(double x, double y, double z, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static DoubleLongTimePoint of(double x, double y, double z, long t, long v) {
        //                return new DoubleLongTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class DoubleDoubleTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final DoubleDoubleTimePoint() {
        //            }
        //
        //            public final DoubleDoubleTimePoint(double x, double y, double z, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static DoubleDoubleTimePoint of(double x, double y, double z, long t, double v) {
        //                return new DoubleDoubleTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.t == other.t
        //                            && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class DoubleObjTimePoint<T> {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final DoubleObjTimePoint() {
        //            }
        //
        //            public final DoubleObjTimePoint(double x, double y, double z, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> DoubleObjTimePoint<T> of(double x, double y, double z, long t, T v) {
        //                return new DoubleObjTimePoint<>(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + N.hashCode(v));
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.t == other.t
        //                            && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteBytePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final byte z;
        //            public final byte v; // value
        //
        //            public final ImmutableByteBytePoint(byte x, byte y, byte z, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteBytePoint of(byte x, byte y, byte z, byte v) {
        //                return new ImmutableByteBytePoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return ((x * 31 + y) * 31 + z) * 31 + v;
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteIntPoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final byte z;
        //            public final int v; // value
        //
        //            public final ImmutableByteIntPoint(byte x, byte y, byte z, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteIntPoint of(byte x, byte y, byte z, int v) {
        //                return new ImmutableByteIntPoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return ((x * 31 + y) * 31 + z) * 31 + v;
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteLongPoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final byte z;
        //            public final long v; // value
        //
        //            public final ImmutableByteLongPoint(byte x, byte y, byte z, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteLongPoint of(byte x, byte y, byte z, long v) {
        //                return new ImmutableByteLongPoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteDoublePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final byte z;
        //            public final double v; // value
        //
        //            public final ImmutableByteDoublePoint(byte x, byte y, byte z, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteDoublePoint of(byte x, byte y, byte z, double v) {
        //                return new ImmutableByteDoublePoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteObjPoint<T> {
        //            public final byte x;
        //            public final byte y;
        //            public final byte z;
        //            public final T v; // value
        //
        //            public final ImmutableByteObjPoint(byte x, byte y, byte z, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableByteObjPoint<T> of(byte x, byte y, byte z, T v) {
        //                return new ImmutableByteObjPoint<>(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return ((x * 31 + y) * 31 + z) * 31 + N.hashCode(v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntBytePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final byte v; // value
        //
        //            public final ImmutableIntBytePoint(int x, int y, int z, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntBytePoint of(int x, int y, int z, byte v) {
        //                return new ImmutableIntBytePoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return ((x * 31 + y) * 31 + z) * 31 + v;
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntIntPoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final int v; // value
        //
        //            public final ImmutableIntIntPoint(int x, int y, int z, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntIntPoint of(int x, int y, int z, int v) {
        //                return new ImmutableIntIntPoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return ((x * 31 + y) * 31 + z) * 31 + v;
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntLongPoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final long v; // value
        //
        //            public final ImmutableIntLongPoint(int x, int y, int z, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntLongPoint of(int x, int y, int z, long v) {
        //                return new ImmutableIntLongPoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntDoublePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final double v; // value
        //
        //            public final ImmutableIntDoublePoint(int x, int y, int z, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntDoublePoint of(int x, int y, int z, double v) {
        //                return new ImmutableIntDoublePoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntObjPoint<T> {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final T v; // value
        //
        //            public final ImmutableIntObjPoint(int x, int y, int z, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableIntObjPoint<T> of(int x, int y, int z, T v) {
        //                return new ImmutableIntObjPoint<>(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return ((x * 31 + y) * 31 + z) * 31 + N.hashCode(v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongBytePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final byte v; // value
        //
        //            public final ImmutableLongBytePoint(long x, long y, long z, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongBytePoint of(long x, long y, long z, byte v) {
        //                return new ImmutableLongBytePoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongIntPoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final int v; // value
        //
        //            public final ImmutableLongIntPoint(long x, long y, long z, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongIntPoint of(long x, long y, long z, int v) {
        //                return new ImmutableLongIntPoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongLongPoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final long v; // value
        //
        //            public final ImmutableLongLongPoint(long x, long y, long z, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongLongPoint of(long x, long y, long z, long v) {
        //                return new ImmutableLongLongPoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongDoublePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final double v; // value
        //
        //            public final ImmutableLongDoublePoint(long x, long y, long z, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongDoublePoint of(long x, long y, long z, double v) {
        //                return new ImmutableLongDoublePoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongObjPoint<T> {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final T v; // value
        //
        //            public final ImmutableLongObjPoint(long x, long y, long z, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableLongObjPoint<T> of(long x, long y, long z, T v) {
        //                return new ImmutableLongObjPoint<>(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + N.hashCode(v));
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleBytePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final byte v; // value
        //
        //            public final ImmutableDoubleBytePoint(double x, double y, double z, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleBytePoint of(double x, double y, double z, byte v) {
        //                return new ImmutableDoubleBytePoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleIntPoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final int v; // value
        //
        //            public final ImmutableDoubleIntPoint(double x, double y, double z, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleIntPoint of(double x, double y, double z, int v) {
        //                return new ImmutableDoubleIntPoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleLongPoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final long v; // value
        //
        //            public final ImmutableDoubleLongPoint(double x, double y, double z, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleLongPoint of(double x, double y, double z, long v) {
        //                return new ImmutableDoubleLongPoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleDoublePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final double v; // value
        //
        //            public final ImmutableDoubleDoublePoint(double x, double y, double z, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleDoublePoint of(double x, double y, double z, double v) {
        //                return new ImmutableDoubleDoublePoint(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleObjPoint<T> {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final T v; // value
        //
        //            public final ImmutableDoubleObjPoint(double x, double y, double z, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableDoubleObjPoint<T> of(double x, double y, double z, T v) {
        //                return new ImmutableDoubleObjPoint<>(x, y, z, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) (((x * 31 + y) * 31 + z) * 31 + N.hashCode(v));
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteByteTimePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final byte z;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final ImmutableByteByteTimePoint(byte x, byte y, byte z, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteByteTimePoint of(byte x, byte y, byte z, long t, byte v) {
        //                return new ImmutableByteByteTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteIntTimePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final byte z;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final ImmutableByteIntTimePoint(byte x, byte y, byte z, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteIntTimePoint of(byte x, byte y, byte z, long t, int v) {
        //                return new ImmutableByteIntTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteLongTimePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final byte z;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final ImmutableByteLongTimePoint(byte x, byte y, byte z, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteLongTimePoint of(byte x, byte y, byte z, long t, long v) {
        //                return new ImmutableByteLongTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteDoubleTimePoint implements Immutable {
        //            public final byte x;
        //            public final byte y;
        //            public final byte z;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final ImmutableByteDoubleTimePoint(byte x, byte y, byte z, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableByteDoubleTimePoint of(byte x, byte y, byte z, long t, double v) {
        //                return new ImmutableByteDoubleTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableByteObjTimePoint<T> {
        //            public final byte x;
        //            public final byte y;
        //            public final byte z;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final ImmutableByteObjTimePoint(byte x, byte y, byte z, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableByteObjTimePoint<T> of(byte x, byte y, byte z, long t, T v) {
        //                return new ImmutableByteObjTimePoint<>(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + N.hashCode(v));
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntByteTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final ImmutableIntByteTimePoint(int x, int y, int z, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntByteTimePoint of(int x, int y, int z, long t, byte v) {
        //                return new ImmutableIntByteTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntIntTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final ImmutableIntIntTimePoint(int x, int y, int z, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntIntTimePoint of(int x, int y, int z, long t, int v) {
        //                return new ImmutableIntIntTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntLongTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final ImmutableIntLongTimePoint(int x, int y, int z, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntLongTimePoint of(int x, int y, int z, long t, long v) {
        //                return new ImmutableIntLongTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntDoubleTimePoint implements Immutable {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final ImmutableIntDoubleTimePoint(int x, int y, int z, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableIntDoubleTimePoint of(int x, int y, int z, long t, double v) {
        //                return new ImmutableIntDoubleTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableIntObjTimePoint<T> {
        //            public final int x;
        //            public final int y;
        //            public final int z;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final ImmutableIntObjTimePoint(int x, int y, int z, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableIntObjTimePoint<T> of(int x, int y, int z, long t, T v) {
        //                return new ImmutableIntObjTimePoint<>(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + N.hashCode(v));
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongByteTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final ImmutableLongByteTimePoint(long x, long y, long z, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongByteTimePoint of(long x, long y, long z, long t, byte v) {
        //                return new ImmutableLongByteTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongIntTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final ImmutableLongIntTimePoint(long x, long y, long z, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongIntTimePoint of(long x, long y, long z, long t, int v) {
        //                return new ImmutableLongIntTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongLongTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final ImmutableLongLongTimePoint(long x, long y, long z, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongLongTimePoint of(long x, long y, long z, long t, long v) {
        //                return new ImmutableLongLongTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongDoubleTimePoint implements Immutable {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final ImmutableLongDoubleTimePoint(long x, long y, long z, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableLongDoubleTimePoint of(long x, long y, long z, long t, double v) {
        //                return new ImmutableLongDoubleTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableLongObjTimePoint<T> {
        //            public final long x;
        //            public final long y;
        //            public final long z;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final ImmutableLongObjTimePoint(long x, long y, long z, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableLongObjTimePoint<T> of(long x, long y, long z, long t, T v) {
        //                return new ImmutableLongObjTimePoint<>(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + N.hashCode(v));
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
        //                    return this.x == other.x && this.y == other.y && this.z == other.z && this.t == other.t && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + N.toString(v) + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleByteTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final long t; // timestamp
        //            public final byte v; // value
        //
        //            public final ImmutableDoubleByteTimePoint(double x, double y, double z, long t, byte v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleByteTimePoint of(double x, double y, double z, long t, byte v) {
        //                return new ImmutableDoubleByteTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleIntTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final long t; // timestamp
        //            public final int v; // value
        //
        //            public final ImmutableDoubleIntTimePoint(double x, double y, double z, long t, int v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleIntTimePoint of(double x, double y, double z, long t, int v) {
        //                return new ImmutableDoubleIntTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleLongTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final long t; // timestamp
        //            public final long v; // value
        //
        //            public final ImmutableDoubleLongTimePoint(double x, double y, double z, long t, long v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleLongTimePoint of(double x, double y, double z, long t, long v) {
        //                return new ImmutableDoubleLongTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.t == other.t && this.v == other.v;
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleDoubleTimePoint implements Immutable {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final long t; // timestamp
        //            public final double v; // value
        //
        //            public final ImmutableDoubleDoubleTimePoint(double x, double y, double z, long t, double v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static ImmutableDoubleDoubleTimePoint of(double x, double y, double z, long t, double v) {
        //                return new ImmutableDoubleDoubleTimePoint(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + v);
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.t == other.t
        //                            && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + v + "]";
        //            }
        //        }
        //
        //        public static final class ImmutableDoubleObjTimePoint<T> {
        //            public final double x;
        //            public final double y;
        //            public final double z;
        //            public final long t; // timestamp
        //            public final T v; // value
        //
        //            public final ImmutableDoubleObjTimePoint(double x, double y, double z, long t, T v) {
        //                this.x = x;
        //                this.y = y;
        //                this.z = z;
        //                this.t = t;
        //                this.v = v;
        //            }
        //
        //            public final static <T> ImmutableDoubleObjTimePoint<T> of(double x, double y, double z, long t, T v) {
        //                return new ImmutableDoubleObjTimePoint<>(x, y, z, t, v);
        //            }
        //
        //            @Override
        //            public final int hashCode() {
        //                return (int) ((((x * 31 + y) * 31 + z) * 31 + t) * 31 + N.hashCode(v));
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
        //                    return N.equals(this.x, other.x) && N.equals(this.y, other.y) && N.equals(this.z, other.z) && this.t == other.t
        //                            && N.equals(this.v, other.v);
        //                }
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public final String toString() {
        //                return "[" + x + ", " + y + ", " + z + ", " + t + ", " + N.toString(v) + "]";
        //            }
        //        }
    }
}
