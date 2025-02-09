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

import com.landawn.abacus.util.stream.ShortStream;
import com.landawn.abacus.util.u.Optional;

import java.util.NoSuchElementException;

@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class ShortTuple<TP extends ShortTuple<TP>> extends PrimitiveTuple<TP> {

    protected short[] elements;

    /**
     *
     *
     * @param _1
     * @return
     */
    public static ShortTuple1 of(final short _1) {
        return new ShortTuple1(_1);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @return
     */
    public static ShortTuple2 of(final short _1, final short _2) {
        return new ShortTuple2(_1, _2);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @param _3
     * @return
     */
    public static ShortTuple3 of(final short _1, final short _2, final short _3) {
        return new ShortTuple3(_1, _2, _3);
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
    public static ShortTuple4 of(final short _1, final short _2, final short _3, final short _4) {
        return new ShortTuple4(_1, _2, _3, _4);
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
    public static ShortTuple5 of(final short _1, final short _2, final short _3, final short _4, final short _5) {
        return new ShortTuple5(_1, _2, _3, _4, _5);
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
    public static ShortTuple6 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6) {
        return new ShortTuple6(_1, _2, _3, _4, _5, _6);
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
    public static ShortTuple7 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7) {
        return new ShortTuple7(_1, _2, _3, _4, _5, _6, _7);
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
     * @param _8
     * @return
     * @deprecated you should consider using <code>class SomeClass { final T1 propName1, final T2 propName2...}</code>
     */
    @Deprecated
    public static ShortTuple8 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7,
            final short _8) {
        return new ShortTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
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
    public static ShortTuple9 of(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7, final short _8,
            final short _9) {
        return new ShortTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     *
     *
     * @param <TP>
     * @param a
     * @return
     */
    public static <TP extends ShortTuple<TP>> TP create(final short[] a) {
        if (a == null || a.length == 0) {
            return (TP) ShortTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) ShortTuple.of(a[0]);

            case 2:
                return (TP) ShortTuple.of(a[0], a[1]);

            case 3:
                return (TP) ShortTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) ShortTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     *
     *
     * @return
     */
    public short min() {
        return N.min(elements());
    }

    /**
     *
     *
     * @return
     */
    public short max() {
        return N.max(elements());
    }

    /**
     *
     *
     * @return
     */
    public short median() {
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
    public abstract boolean contains(short valueToFind);

    /**
     *
     *
     * @return
     */
    public short[] toArray() {
        return elements().clone();
    }

    /**
     *
     *
     * @return
     */
    public ShortList toList() {
        return ShortList.of(elements().clone());
    }

    /**
     *
     *
     * @param <E>
     * @param consumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
        for (final short e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     *
     *
     * @return
     */
    public ShortStream stream() {
        return ShortStream.of(elements());
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
            return N.equals(elements(), ((ShortTuple<TP>) obj).elements());
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

    protected abstract short[] elements();

    static final class ShortTuple0 extends ShortTuple<ShortTuple0> {

        private static final ShortTuple0 EMPTY = new ShortTuple0();

        ShortTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public short min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public short max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public short median() {
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
        public ShortTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final short valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected short[] elements() {
            return N.EMPTY_SHORT_ARRAY;
        }
    }

    public static final class ShortTuple1 extends ShortTuple<ShortTuple1> {

        public final short _1;

        ShortTuple1() {
            this((short) 0);
        }

        ShortTuple1(final short _1) {
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
        public short min() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public short max() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public short median() {
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
        public ShortTuple1 reverse() {
            return new ShortTuple1(_1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final short valueToFind) {
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
            } else if (!(obj instanceof final ShortTuple1 other)) {
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
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1 };
            }

            return elements;
        }
    }

    public static final class ShortTuple2 extends ShortTuple<ShortTuple2> {

        public final short _1;
        public final short _2;

        ShortTuple2() {
            this((short) 0, (short) 0);
        }

        ShortTuple2(final short _1, final short _2) {
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
        public short min() {
            return N.min(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public short max() {
            return N.max(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public short median() {
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
        public ShortTuple2 reverse() {
            return new ShortTuple2(_2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final short valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(final Throwables.ShortBiConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.ShortBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<ShortTuple2> filter(final Throwables.ShortBiPredicate<E> predicate) throws E {
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
            } else if (!(obj instanceof final ShortTuple2 other)) {
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
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2 };
            }

            return elements;
        }
    }

    public static final class ShortTuple3 extends ShortTuple<ShortTuple3> {

        public final short _1;
        public final short _2;
        public final short _3;

        ShortTuple3() {
            this((short) 0, (short) 0, (short) 0);
        }

        ShortTuple3(final short _1, final short _2, final short _3) {
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
        public short min() {
            return N.min(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public short max() {
            return N.max(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public short median() {
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
        public ShortTuple3 reverse() {
            return new ShortTuple3(_3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final short valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> consumer) throws E {
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
        public <E extends Exception> void accept(final Throwables.ShortTriConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.ShortTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<ShortTuple3> filter(final Throwables.ShortTriPredicate<E> predicate) throws E {
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
            } else if (!(obj instanceof final ShortTuple3 other)) {
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
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    public static final class ShortTuple4 extends ShortTuple<ShortTuple4> {

        public final short _1;
        public final short _2;
        public final short _3;
        public final short _4;

        ShortTuple4() {
            this((short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple4(final short _1, final short _2, final short _3, final short _4) {
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
        public ShortTuple4 reverse() {
            return new ShortTuple4(_4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    public static final class ShortTuple5 extends ShortTuple<ShortTuple5> {

        public final short _1;
        public final short _2;
        public final short _3;
        public final short _4;
        public final short _5;

        ShortTuple5() {
            this((short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple5(final short _1, final short _2, final short _3, final short _4, final short _5) {
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
        public ShortTuple5 reverse() {
            return new ShortTuple5(_5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    public static final class ShortTuple6 extends ShortTuple<ShortTuple6> {

        public final short _1;
        public final short _2;
        public final short _3;
        public final short _4;
        public final short _5;
        public final short _6;

        ShortTuple6() {
            this((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple6(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6) {
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
        public ShortTuple6 reverse() {
            return new ShortTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    public static final class ShortTuple7 extends ShortTuple<ShortTuple7> {

        public final short _1;
        public final short _2;
        public final short _3;
        public final short _4;
        public final short _5;
        public final short _6;
        public final short _7;

        ShortTuple7() {
            this((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple7(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7) {
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
        public ShortTuple7 reverse() {
            return new ShortTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    public static final class ShortTuple8 extends ShortTuple<ShortTuple8> {

        public final short _1;
        public final short _2;
        public final short _3;
        public final short _4;
        public final short _5;
        public final short _6;
        public final short _7;
        public final short _8;

        ShortTuple8() {
            this((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple8(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7, final short _8) {
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
        public ShortTuple8 reverse() {
            return new ShortTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    public static final class ShortTuple9 extends ShortTuple<ShortTuple9> {

        public final short _1;
        public final short _2;
        public final short _3;
        public final short _4;
        public final short _5;
        public final short _6;
        public final short _7;
        public final short _8;
        public final short _9;

        ShortTuple9() {
            this((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);
        }

        ShortTuple9(final short _1, final short _2, final short _3, final short _4, final short _5, final short _6, final short _7, final short _8,
                final short _9) {
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
        public ShortTuple9 reverse() {
            return new ShortTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final short valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        @Override
        protected short[] elements() {
            if (elements == null) {
                elements = new short[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
