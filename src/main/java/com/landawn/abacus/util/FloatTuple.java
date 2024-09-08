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
import com.landawn.abacus.util.stream.FloatStream;

@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class FloatTuple<TP extends FloatTuple<TP>> extends PrimitiveTuple<TP> {

    private static final FloatTuple0 EMPTY = new FloatTuple0();

    protected float[] elements;

    /**
     *
     *
     * @param _1
     * @return
     */
    public static FloatTuple1 of(final float _1) {
        return new FloatTuple1(_1);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @return
     */
    public static FloatTuple2 of(final float _1, final float _2) {
        return new FloatTuple2(_1, _2);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @param _3
     * @return
     */
    public static FloatTuple3 of(final float _1, final float _2, final float _3) {
        return new FloatTuple3(_1, _2, _3);
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
    public static FloatTuple4 of(final float _1, final float _2, final float _3, final float _4) {
        return new FloatTuple4(_1, _2, _3, _4);
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
    public static FloatTuple5 of(final float _1, final float _2, final float _3, final float _4, final float _5) {
        return new FloatTuple5(_1, _2, _3, _4, _5);
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
    public static FloatTuple6 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6) {
        return new FloatTuple6(_1, _2, _3, _4, _5, _6);
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
    public static FloatTuple7 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7) {
        return new FloatTuple7(_1, _2, _3, _4, _5, _6, _7);
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
    public static FloatTuple8 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7,
            final float _8) {
        return new FloatTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
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
    public static FloatTuple9 of(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7, final float _8,
            final float _9) {
        return new FloatTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     *
     *
     * @param <TP>
     * @param a
     * @return
     */
    public static <TP extends FloatTuple<TP>> TP create(final float[] a) {
        if (a == null || a.length == 0) {
            return (TP) EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) FloatTuple.of(a[0]);

            case 2:
                return (TP) FloatTuple.of(a[0], a[1]);

            case 3:
                return (TP) FloatTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) FloatTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     *
     *
     * @return
     */
    public float min() {
        return N.min(elements());
    }

    /**
     *
     *
     * @return
     */
    public float max() {
        return N.max(elements());
    }

    /**
     *
     *
     * @return
     */
    public float median() {
        return N.median(elements());
    }

    /**
     *
     *
     * @return
     */
    public float sum() {
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
    public abstract boolean contains(float valueToFind);

    /**
     *
     *
     * @return
     */
    public float[] toArray() {
        return elements().clone();
    }

    /**
     *
     *
     * @return
     */
    public FloatList toList() {
        return FloatList.of(elements().clone());
    }

    /**
     *
     *
     * @param <E>
     * @param comsumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> comsumer) throws E {
        for (final float e : elements()) {
            comsumer.accept(e);
        }
    }

    /**
     *
     *
     * @return
     */
    public FloatStream stream() {
        return FloatStream.of(elements());
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
            return N.equals(elements(), ((FloatTuple<TP>) obj).elements());
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

    protected abstract float[] elements();

    static final class FloatTuple0 extends FloatTuple<FloatTuple0> {

        FloatTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public float min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public float max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public float median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public float sum() {
            return 0;
        }

        @Override
        public double average() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public FloatTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final float valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected float[] elements() {
            return N.EMPTY_FLOAT_ARRAY;
        }
    }

    public static final class FloatTuple1 extends FloatTuple<FloatTuple1> {

        public final float _1;

        FloatTuple1() {
            this(0);
        }

        FloatTuple1(final float _1) {
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
        public float min() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public float max() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public float median() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public float sum() {
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
        public FloatTuple1 reverse() {
            return new FloatTuple1(_1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind);
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
            } else if (!(obj instanceof final FloatTuple1 other)) {
                return false;
            } else {
                return N.equals(_1, other._1);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1 };
            }

            return elements;
        }
    }

    public static final class FloatTuple2 extends FloatTuple<FloatTuple2> {

        public final float _1;
        public final float _2;

        FloatTuple2() {
            this(0, 0);
        }

        FloatTuple2(final float _1, final float _2) {
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
        public float min() {
            return N.min(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public float max() {
            return N.max(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public float median() {
            return N.median(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public float sum() {
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
        public FloatTuple2 reverse() {
            return new FloatTuple2(_2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind);
        }

        /**
         *
         *
         * @param <E>
         * @param comsumer
         * @throws E
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> comsumer) throws E {
            comsumer.accept(_1);
            comsumer.accept(_2);
        }

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(final Throwables.FloatBiConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.FloatBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<FloatTuple2> filter(final Throwables.FloatBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.<FloatTuple2> empty();
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
            } else if (!(obj instanceof final FloatTuple2 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2 };
            }

            return elements;
        }
    }

    public static final class FloatTuple3 extends FloatTuple<FloatTuple3> {

        public final float _1;
        public final float _2;
        public final float _3;

        FloatTuple3() {
            this(0, 0, 0);
        }

        FloatTuple3(final float _1, final float _2, final float _3) {
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
        public float min() {
            return N.min(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public float max() {
            return N.max(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public float median() {
            return N.median(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public float sum() {
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
        public FloatTuple3 reverse() {
            return new FloatTuple3(_3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind);
        }

        /**
         *
         *
         * @param <E>
         * @param comsumer
         * @throws E
         */
        @Override
        public <E extends Exception> void forEach(final Throwables.FloatConsumer<E> comsumer) throws E {
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
        public <E extends Exception> void accept(final Throwables.FloatTriConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.FloatTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<FloatTuple3> filter(final Throwables.FloatTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.<FloatTuple3> empty();
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
            } else if (!(obj instanceof final FloatTuple3 other)) {
                return false;
            } else {
                return N.equals(_1, other._1) && N.equals(_2, other._2) && N.equals(_3, other._3);
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
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    public static final class FloatTuple4 extends FloatTuple<FloatTuple4> {

        public final float _1;
        public final float _2;
        public final float _3;
        public final float _4;

        FloatTuple4() {
            this(0, 0, 0, 0);
        }

        FloatTuple4(final float _1, final float _2, final float _3, final float _4) {
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
        public FloatTuple4 reverse() {
            return new FloatTuple4(_4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    public static final class FloatTuple5 extends FloatTuple<FloatTuple5> {

        public final float _1;
        public final float _2;
        public final float _3;
        public final float _4;
        public final float _5;

        FloatTuple5() {
            this(0, 0, 0, 0, 0);
        }

        FloatTuple5(final float _1, final float _2, final float _3, final float _4, final float _5) {
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
        public FloatTuple5 reverse() {
            return new FloatTuple5(_5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind)
                    || N.equals(_5, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    public static final class FloatTuple6 extends FloatTuple<FloatTuple6> {

        public final float _1;
        public final float _2;
        public final float _3;
        public final float _4;
        public final float _5;
        public final float _6;

        FloatTuple6() {
            this(0, 0, 0, 0, 0, 0);
        }

        FloatTuple6(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6) {
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
        public FloatTuple6 reverse() {
            return new FloatTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    public static final class FloatTuple7 extends FloatTuple<FloatTuple7> {

        public final float _1;
        public final float _2;
        public final float _3;
        public final float _4;
        public final float _5;
        public final float _6;
        public final float _7;

        FloatTuple7() {
            this(0, 0, 0, 0, 0, 0, 0);
        }

        FloatTuple7(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7) {
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
        public FloatTuple7 reverse() {
            return new FloatTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    public static final class FloatTuple8 extends FloatTuple<FloatTuple8> {

        public final float _1;
        public final float _2;
        public final float _3;
        public final float _4;
        public final float _5;
        public final float _6;
        public final float _7;
        public final float _8;

        FloatTuple8() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }

        FloatTuple8(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7, final float _8) {
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
        public FloatTuple8 reverse() {
            return new FloatTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    public static final class FloatTuple9 extends FloatTuple<FloatTuple9> {

        public final float _1;
        public final float _2;
        public final float _3;
        public final float _4;
        public final float _5;
        public final float _6;
        public final float _7;
        public final float _8;
        public final float _9;

        FloatTuple9() {
            this(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        FloatTuple9(final float _1, final float _2, final float _3, final float _4, final float _5, final float _6, final float _7, final float _8,
                final float _9) {
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
        public FloatTuple9 reverse() {
            return new FloatTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final float valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind) || N.equals(_5, valueToFind)
                    || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind) || N.equals(_9, valueToFind);
        }

        @Override
        protected float[] elements() {
            if (elements == null) {
                elements = new float[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
