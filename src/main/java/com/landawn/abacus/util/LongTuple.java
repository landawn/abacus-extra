/*
 * Copyright (C) 2020 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.landawn.abacus.util;

import java.util.NoSuchElementException;

import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.LongStream;

@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class LongTuple<TP extends LongTuple<TP>> extends PrimitiveTuple<TP> {

    private static final LongTuple0 EMPTY = new LongTuple0();

    protected long[] elements;

    /**
     *
     *
     * @param _1
     * @return
     */
    public static LongTuple1 of(final long _1) {
        return new LongTuple1(_1);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @return
     */
    public static LongTuple2 of(final long _1, final long _2) {
        return new LongTuple2(_1, _2);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @param _3
     * @return
     */
    public static LongTuple3 of(final long _1, final long _2, final long _3) {
        return new LongTuple3(_1, _2, _3);
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
    public static LongTuple4 of(final long _1, final long _2, final long _3, final long _4) {
        return new LongTuple4(_1, _2, _3, _4);
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
    public static LongTuple5 of(final long _1, final long _2, final long _3, final long _4, final long _5) {
        return new LongTuple5(_1, _2, _3, _4, _5);
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
    public static LongTuple6 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6) {
        return new LongTuple6(_1, _2, _3, _4, _5, _6);
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
    public static LongTuple7 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7) {
        return new LongTuple7(_1, _2, _3, _4, _5, _6, _7);
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
    public static LongTuple8 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8) {
        return new LongTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
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
    public static LongTuple9 of(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8,
            final long _9) {
        return new LongTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     *
     *
     * @param <TP>
     * @param a
     * @return
     */
    public static <TP extends LongTuple<TP>> TP create(final long[] a) {
        if (a == null || a.length == 0) {
            return (TP) EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) LongTuple.of(a[0]);

            case 2:
                return (TP) LongTuple.of(a[0], a[1]);

            case 3:
                return (TP) LongTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) LongTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     *
     *
     * @return
     */
    public long min() {
        return N.min(elements());
    }

    /**
     *
     *
     * @return
     */
    public long max() {
        return N.max(elements());
    }

    /**
     *
     *
     * @return
     */
    public long median() {
        return N.median(elements());
    }

    /**
     *
     *
     * @return
     */
    public long sum() {
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
    public abstract boolean contains(long valueToFind);

    /**
     *
     *
     * @return
     */
    public long[] toArray() {
        return elements().clone();
    }

    /**
     *
     *
     * @return
     */
    public LongList toList() {
        return LongList.of(elements().clone());
    }

    /**
     *
     *
     * @param <E>
     * @param comsumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.LongConsumer<E> comsumer) throws E {
        for (final long e : elements()) {
            comsumer.accept(e);
        }
    }

    /**
     *
     *
     * @return
     */
    public LongStream stream() {
        return LongStream.of(elements());
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
            return N.equals(elements(), ((LongTuple<TP>) obj).elements());
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

    protected abstract long[] elements();

    static final class LongTuple0 extends LongTuple<LongTuple0> {

        LongTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public long min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public long max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public long median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public long sum() {
            return 0;
        }

        @Override
        public double average() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public LongTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final long valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected long[] elements() {
            return N.EMPTY_LONG_ARRAY;
        }
    }

    public static final class LongTuple1 extends LongTuple<LongTuple1> {

        public final long _1;

        LongTuple1() {
            this(0);
        }

        LongTuple1(final long _1) {
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
        public long min() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public long max() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public long median() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public long sum() {
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
        public LongTuple1 reverse() {
            return new LongTuple1(_1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int hashCode() {
            return (int) _1;
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
            } else if (!(obj instanceof final LongTuple1 other)) {
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
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1 };
            }

            return elements;
        }
    }

    public static final class LongTuple2 extends LongTuple<LongTuple2> {

        public final long _1;
        public final long _2;

        LongTuple2() {
            this(0, 0);
        }

        LongTuple2(final long _1, final long _2) {
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
        public long min() {
            return N.min(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public long max() {
            return N.max(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public long median() {
            return N.median(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public long sum() {
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
        public LongTuple2 reverse() {
            return new LongTuple2(_2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind;
        }

        /**
         *
         *
         * @param <E>
         * @param comsumer
         * @throws E
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> comsumer) throws E {
            comsumer.accept(_1);
            comsumer.accept(_2);
        }

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(final Throwables.LongBiConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.LongBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<LongTuple2> filter(final Throwables.LongBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.<LongTuple2> empty();
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int hashCode() {
            return (int) (31 * _1 + _2);
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
            } else if (!(obj instanceof final LongTuple2 other)) {
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
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2 };
            }

            return elements;
        }
    }

    public static final class LongTuple3 extends LongTuple<LongTuple3> {

        public final long _1;
        public final long _2;
        public final long _3;

        LongTuple3() {
            this(0, 0, 0);
        }

        LongTuple3(final long _1, final long _2, final long _3) {
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
        public long min() {
            return N.min(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public long max() {
            return N.max(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public long median() {
            return N.median(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public long sum() {
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
        public LongTuple3 reverse() {
            return new LongTuple3(_3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind;
        }

        /**
         *
         *
         * @param <E>
         * @param comsumer
         * @throws E
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.LongConsumer<E> comsumer) throws E {
            comsumer.accept(_1);
            comsumer.accept(_2);
            comsumer.accept(_3);
        }

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(final Throwables.LongTriConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.LongTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<LongTuple3> filter(final Throwables.LongTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.<LongTuple3> empty();
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int hashCode() {
            return (int) ((31 * (31 * _1 + _2)) + _3);
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
            } else if (!(obj instanceof final LongTuple3 other)) {
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
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    public static final class LongTuple4 extends LongTuple<LongTuple4> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;

        LongTuple4() {
            this(0, 0, 0, 0);
        }

        LongTuple4(final long _1, final long _2, final long _3, final long _4) {
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
        public LongTuple4 reverse() {
            return new LongTuple4(_4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    public static final class LongTuple5 extends LongTuple<LongTuple5> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;
        public final long _5;

        LongTuple5() {
            this(0, 0, 0, 0, 0);
        }

        LongTuple5(final long _1, final long _2, final long _3, final long _4, final long _5) {
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
        public LongTuple5 reverse() {
            return new LongTuple5(_5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    public static final class LongTuple6 extends LongTuple<LongTuple6> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;
        public final long _5;
        public final long _6;

        LongTuple6() {
            this(0, 0, 0, 0, 0, 0);
        }

        LongTuple6(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6) {
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
        public LongTuple6 reverse() {
            return new LongTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    public static final class LongTuple7 extends LongTuple<LongTuple7> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;
        public final long _5;
        public final long _6;
        public final long _7;

        LongTuple7() {
            this(0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple7(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7) {
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
        public LongTuple7 reverse() {
            return new LongTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    public static final class LongTuple8 extends LongTuple<LongTuple8> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;
        public final long _5;
        public final long _6;
        public final long _7;
        public final long _8;

        LongTuple8() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple8(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8) {
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
        public LongTuple8 reverse() {
            return new LongTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    public static final class LongTuple9 extends LongTuple<LongTuple9> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;
        public final long _5;
        public final long _6;
        public final long _7;
        public final long _8;
        public final long _9;

        LongTuple9() {
            this(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple9(final long _1, final long _2, final long _3, final long _4, final long _5, final long _6, final long _7, final long _8, final long _9) {
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
        public LongTuple9 reverse() {
            return new LongTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final long valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
