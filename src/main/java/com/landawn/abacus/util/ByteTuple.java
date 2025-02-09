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

import com.landawn.abacus.util.stream.ByteStream;
import com.landawn.abacus.util.u.Optional;

import java.util.NoSuchElementException;

@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class ByteTuple<TP extends ByteTuple<TP>> extends PrimitiveTuple<TP> {

    protected byte[] elements;

    /**
     *
     *
     * @param _1
     * @return
     */
    public static ByteTuple1 of(final byte _1) {
        return new ByteTuple1(_1);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @return
     */
    public static ByteTuple2 of(final byte _1, final byte _2) {
        return new ByteTuple2(_1, _2);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @param _3
     * @return
     */
    public static ByteTuple3 of(final byte _1, final byte _2, final byte _3) {
        return new ByteTuple3(_1, _2, _3);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @param _3
     * @param _4
     * @return
     */
    public static ByteTuple4 of(final byte _1, final byte _2, final byte _3, final byte _4) {
        return new ByteTuple4(_1, _2, _3, _4);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @param _3
     * @param _4
     * @param _5
     * @return
     */
    public static ByteTuple5 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5) {
        return new ByteTuple5(_1, _2, _3, _4, _5);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @param _3
     * @param _4
     * @param _5
     * @param _6
     * @return
     */
    public static ByteTuple6 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6) {
        return new ByteTuple6(_1, _2, _3, _4, _5, _6);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @param _3
     * @param _4
     * @param _5
     * @param _6
     * @param _7
     * @return
     */
    public static ByteTuple7 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7) {
        return new ByteTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    /**
     *
     * @param _1
     * @param _2
     * @param _3
     * @param _4
     * @param _5
     * @param _6
     * @param _7
     * @param _8
     * @return
     * @deprecated you should consider using <code>class SomeClass { final T1 propName1, final T2 propName2...}</code>
     */
    @Deprecated
    public static ByteTuple8 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7, final byte _8) {
        return new ByteTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     *
     * @param _1
     * @param _2
     * @param _3
     * @param _4
     * @param _5
     * @param _6
     * @param _7
     * @param _8
     * @param _9
     * @return
     * @deprecated you should consider using <code>class SomeClass { final T1 propName1, final T2 propName2...}</code>
     */
    @Deprecated
    public static ByteTuple9 of(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7, final byte _8,
            final byte _9) {
        return new ByteTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     *
     *
     * @param <TP>
     * @param a
     * @return
     */
    public static <TP extends ByteTuple<TP>> TP create(final byte[] a) {
        if (a == null || a.length == 0) {
            return (TP) ByteTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) ByteTuple.of(a[0]);

            case 2:
                return (TP) ByteTuple.of(a[0], a[1]);

            case 3:
                return (TP) ByteTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     *
     *
     * @return
     */
    public byte min() {
        return N.min(elements());
    }

    /**
     *
     *
     * @return
     */
    public byte max() {
        return N.max(elements());
    }

    /**
     *
     *
     * @return
     */
    public byte median() {
        return N.median(elements());
    }

    /**
     *
     *
     * @return
     */
    public int sum() {
        return N.sum(elements());
    }

    /**
     *
     *
     * @return
     */
    public double average() {
        return N.average(elements());
    }

    /**
     *
     *
     * @return
     */
    public abstract TP reverse();

    /**
     *
     *
     * @param valueToFind
     * @return
     */
    public abstract boolean contains(byte valueToFind);

    /**
     *
     *
     * @return
     */
    public byte[] toArray() {
        return elements().clone();
    }

    /**
     *
     *
     * @return
     */
    public ByteList toList() {
        return ByteList.of(elements().clone());
    }

    /**
     *
     *
     * @param <E>
     * @param consumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
        for (final byte e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     *
     *
     * @return
     */
    public ByteStream stream() {
        return ByteStream.of(elements());
    }

    /**
     *
     *
     * @return
     */
    @Override
    public int hashCode() {
        return N.hashCode(elements());
    }

    /**
     *
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        } else {
            return N.equals(elements(), ((ByteTuple<TP>) obj).elements());
        }
    }

    /**
     *
     *
     * @return
     */
    @Override
    public String toString() {
        return N.toString(elements());
    }

    protected abstract byte[] elements();

    static final class ByteTuple0 extends ByteTuple<ByteTuple0> {

        private static final ByteTuple0 EMPTY = new ByteTuple0();

        ByteTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public byte min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public byte max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public byte median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public int sum() {
            return 0;
        }

        @Override
        public double average() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public ByteTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final byte valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected byte[] elements() {
            return N.EMPTY_BYTE_ARRAY;
        }
    }

    public static final class ByteTuple1 extends ByteTuple<ByteTuple1> {

        public final byte _1;

        ByteTuple1() {
            this((byte) 0);
        }

        ByteTuple1(final byte _1) {
            this._1 = _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int arity() {
            return 1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public byte min() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public byte max() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public byte median() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int sum() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double average() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public ByteTuple1 reverse() {
            return new ByteTuple1(_1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int hashCode() {
            return _1;
        }

        /**
         *
         *
         * @param obj
         * @return
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple1 other)) {
                return false;
            } else {
                return _1 == other._1;
            }
        }

        /**
         *
         *
         * @return
         */
        @Override
        public String toString() {
            return "[" + _1 + "]";
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1 };
            }

            return elements;
        }
    }

    public static final class ByteTuple2 extends ByteTuple<ByteTuple2> {

        public final byte _1;
        public final byte _2;

        ByteTuple2() {
            this((byte) 0, (byte) 0);
        }

        ByteTuple2(final byte _1, final byte _2) {
            this._1 = _1;
            this._2 = _2;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int arity() {
            return 2;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public byte min() {
            return N.min(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public byte max() {
            return N.max(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public byte median() {
            return N.median(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int sum() {
            return N.sum(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double average() {
            return N.average(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public ByteTuple2 reverse() {
            return new ByteTuple2(_2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
        }

        /**
         *
         *
         * @param <E>
         * @param consumer
         * @throws E
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(final Throwables.ByteBiConsumer<E> action) throws E {
            action.accept(_1, _2);
        }

        /**
         *
         * @param <U>
         * @param <E>
         * @param mapper
         * @return
         * @throws E the e
         */
        public <U, E extends Exception> U map(final Throwables.ByteBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<ByteTuple2> filter(final Throwables.ByteBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int hashCode() {
            return 31 * _1 + _2;
        }

        /**
         *
         *
         * @param obj
         * @return
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple2 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2;
            }
        }

        /**
         *
         *
         * @return
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + "]";
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2 };
            }

            return elements;
        }
    }

    public static final class ByteTuple3 extends ByteTuple<ByteTuple3> {

        public final byte _1;
        public final byte _2;
        public final byte _3;

        ByteTuple3() {
            this((byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple3(final byte _1, final byte _2, final byte _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int arity() {
            return 3;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public byte min() {
            return N.min(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public byte max() {
            return N.max(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public byte median() {
            return N.median(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int sum() {
            return N.sum(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double average() {
            return N.average(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public ByteTuple3 reverse() {
            return new ByteTuple3(_3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
        }

        /**
         *
         *
         * @param <E>
         * @param consumer
         * @throws E
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
            consumer.accept(_3);
        }

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(final Throwables.ByteTriConsumer<E> action) throws E {
            action.accept(_1, _2, _3);
        }

        /**
         *
         * @param <U>
         * @param <E>
         * @param mapper
         * @return
         * @throws E the e
         */
        public <U, E extends Exception> U map(final Throwables.ByteTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<ByteTuple3> filter(final Throwables.ByteTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int hashCode() {
            return (31 * (31 * _1 + _2)) + _3;
        }

        /**
         *
         *
         * @param obj
         * @return
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof final ByteTuple3 other)) {
                return false;
            } else {
                return _1 == other._1 && _2 == other._2 && _3 == other._3;
            }
        }

        /**
         *
         *
         * @return
         */
        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + "]";
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    public static final class ByteTuple4 extends ByteTuple<ByteTuple4> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;

        ByteTuple4() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple4(final byte _1, final byte _2, final byte _3, final byte _4) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int arity() {
            return 4;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public ByteTuple4 reverse() {
            return new ByteTuple4(_4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    public static final class ByteTuple5 extends ByteTuple<ByteTuple5> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;
        public final byte _5;

        ByteTuple5() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple5(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int arity() {
            return 5;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public ByteTuple5 reverse() {
            return new ByteTuple5(_5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    public static final class ByteTuple6 extends ByteTuple<ByteTuple6> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;
        public final byte _5;
        public final byte _6;

        ByteTuple6() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple6(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int arity() {
            return 6;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public ByteTuple6 reverse() {
            return new ByteTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    public static final class ByteTuple7 extends ByteTuple<ByteTuple7> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;
        public final byte _5;
        public final byte _6;
        public final byte _7;

        ByteTuple7() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple7(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int arity() {
            return 7;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public ByteTuple7 reverse() {
            return new ByteTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    public static final class ByteTuple8 extends ByteTuple<ByteTuple8> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;
        public final byte _5;
        public final byte _6;
        public final byte _7;
        public final byte _8;

        ByteTuple8() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple8(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7, final byte _8) {
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
         *
         *
         * @return
         */
        @Override
        public int arity() {
            return 8;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public ByteTuple8 reverse() {
            return new ByteTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    public static final class ByteTuple9 extends ByteTuple<ByteTuple9> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;
        public final byte _5;
        public final byte _6;
        public final byte _7;
        public final byte _8;
        public final byte _9;

        ByteTuple9() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple9(final byte _1, final byte _2, final byte _3, final byte _4, final byte _5, final byte _6, final byte _7, final byte _8, final byte _9) {
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
         *
         *
         * @return
         */
        @Override
        public int arity() {
            return 9;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public ByteTuple9 reverse() {
            return new ByteTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final byte valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
