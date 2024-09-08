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
import com.landawn.abacus.util.stream.DoubleStream;

@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class DoubleTuple<TP extends DoubleTuple<TP>> extends PrimitiveTuple<TP> {

    private static final DoubleTuple0 EMPTY = new DoubleTuple0();

    protected double[] elements;

    /**
     *
     *
     * @param _1
     * @return
     */
    public static DoubleTuple1 of(final double _1) {
        return new DoubleTuple1(_1);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @return
     */
    public static DoubleTuple2 of(final double _1, final double _2) {
        return new DoubleTuple2(_1, _2);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @param _3
     * @return
     */
    public static DoubleTuple3 of(final double _1, final double _2, final double _3) {
        return new DoubleTuple3(_1, _2, _3);
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
    public static DoubleTuple4 of(final double _1, final double _2, final double _3, final double _4) {
        return new DoubleTuple4(_1, _2, _3, _4);
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
    public static DoubleTuple5 of(final double _1, final double _2, final double _3, final double _4, final double _5) {
        return new DoubleTuple5(_1, _2, _3, _4, _5);
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
    public static DoubleTuple6 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6) {
        return new DoubleTuple6(_1, _2, _3, _4, _5, _6);
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
    public static DoubleTuple7 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7) {
        return new DoubleTuple7(_1, _2, _3, _4, _5, _6, _7);
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
    public static DoubleTuple8 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7, final double _8) {
        return new DoubleTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
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
    public static DoubleTuple9 of(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7, final double _8, final double _9) {
        return new DoubleTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     *
     *
     * @param <TP>
     * @param a
     * @return
     */
    public static <TP extends DoubleTuple<TP>> TP create(final double[] a) {
        if (a == null || a.length == 0) {
            return (TP) EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) DoubleTuple.of(a[0]);

            case 2:
                return (TP) DoubleTuple.of(a[0], a[1]);

            case 3:
                return (TP) DoubleTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) DoubleTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     *
     *
     * @return
     */
    public double min() {
        return N.min(elements());
    }

    /**
     *
     *
     * @return
     */
    public double max() {
        return N.max(elements());
    }

    /**
     *
     *
     * @return
     */
    public double median() {
        return N.median(elements());
    }

    /**
     *
     *
     * @return
     */
    public double sum() {
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
    public abstract boolean contains(double valueToFind);

    /**
     *
     *
     * @return
     */
    public double[] toArray() {
        return elements().clone();
    }

    /**
     *
     *
     * @return
     */
    public DoubleList toList() {
        return DoubleList.of(elements().clone());
    }

    /**
     *
     *
     * @param <E>
     * @param comsumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> comsumer) throws E {
        for (final double e : elements()) {
            comsumer.accept(e);
        }
    }

    /**
     *
     *
     * @return
     */
    public DoubleStream stream() {
        return DoubleStream.of(elements());
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
            return N.equals(elements(), ((DoubleTuple<TP>) obj).elements());
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

    protected abstract double[] elements();

    static final class DoubleTuple0 extends DoubleTuple<DoubleTuple0> {

        DoubleTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public double min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public double max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public double median() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public double sum() {
            return 0;
        }

        @Override
        public double average() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public DoubleTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final double valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected double[] elements() {
            return N.EMPTY_DOUBLE_ARRAY;
        }
    }

    public static final class DoubleTuple1 extends DoubleTuple<DoubleTuple1> {

        public final double _1;

        DoubleTuple1() {
            this(0);
        }

        DoubleTuple1(final double _1) {
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
        public double min() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double max() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double median() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double sum() {
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
        public DoubleTuple1 reverse() {
            return new DoubleTuple1(_1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final double valueToFind) {
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
            } else if (!(obj instanceof final DoubleTuple1 other)) {
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
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1 };
            }

            return elements;
        }
    }

    public static final class DoubleTuple2 extends DoubleTuple<DoubleTuple2> {

        public final double _1;
        public final double _2;

        DoubleTuple2() {
            this(0, 0);
        }

        DoubleTuple2(final double _1, final double _2) {
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
        public double min() {
            return N.min(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double max() {
            return N.max(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double median() {
            return N.median(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double sum() {
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
        public DoubleTuple2 reverse() {
            return new DoubleTuple2(_2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final double valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> comsumer) throws E {
            comsumer.accept(_1);
            comsumer.accept(_2);
        }

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(final Throwables.DoubleBiConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.DoubleBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<DoubleTuple2> filter(final Throwables.DoubleBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.<DoubleTuple2> empty();
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
            } else if (!(obj instanceof final DoubleTuple2 other)) {
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
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2 };
            }

            return elements;
        }
    }

    public static final class DoubleTuple3 extends DoubleTuple<DoubleTuple3> {

        public final double _1;
        public final double _2;
        public final double _3;

        DoubleTuple3() {
            this(0, 0, 0);
        }

        DoubleTuple3(final double _1, final double _2, final double _3) {
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
        public double min() {
            return N.min(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double max() {
            return N.max(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double median() {
            return N.median(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public double sum() {
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
        public DoubleTuple3 reverse() {
            return new DoubleTuple3(_3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final double valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> comsumer) throws E {
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
        public <E extends Exception> void accept(final Throwables.DoubleTriConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.DoubleTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<DoubleTuple3> filter(final Throwables.DoubleTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.<DoubleTuple3> empty();
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
            } else if (!(obj instanceof final DoubleTuple3 other)) {
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
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    public static final class DoubleTuple4 extends DoubleTuple<DoubleTuple4> {

        public final double _1;
        public final double _2;
        public final double _3;
        public final double _4;

        DoubleTuple4() {
            this(0, 0, 0, 0);
        }

        DoubleTuple4(final double _1, final double _2, final double _3, final double _4) {
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
        public DoubleTuple4 reverse() {
            return new DoubleTuple4(_4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind);
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    public static final class DoubleTuple5 extends DoubleTuple<DoubleTuple5> {

        public final double _1;
        public final double _2;
        public final double _3;
        public final double _4;
        public final double _5;

        DoubleTuple5() {
            this(0, 0, 0, 0, 0);
        }

        DoubleTuple5(final double _1, final double _2, final double _3, final double _4, final double _5) {
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
        public DoubleTuple5 reverse() {
            return new DoubleTuple5(_5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind)
                    || N.equals(_5, valueToFind);
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    public static final class DoubleTuple6 extends DoubleTuple<DoubleTuple6> {

        public final double _1;
        public final double _2;
        public final double _3;
        public final double _4;
        public final double _5;
        public final double _6;

        DoubleTuple6() {
            this(0, 0, 0, 0, 0, 0);
        }

        DoubleTuple6(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6) {
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
        public DoubleTuple6 reverse() {
            return new DoubleTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind)
                    || N.equals(_5, valueToFind) || N.equals(_6, valueToFind);
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    public static final class DoubleTuple7 extends DoubleTuple<DoubleTuple7> {

        public final double _1;
        public final double _2;
        public final double _3;
        public final double _4;
        public final double _5;
        public final double _6;
        public final double _7;

        DoubleTuple7() {
            this(0, 0, 0, 0, 0, 0, 0);
        }

        DoubleTuple7(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7) {
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
        public DoubleTuple7 reverse() {
            return new DoubleTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind)
                    || N.equals(_5, valueToFind) || N.equals(_6, valueToFind) || N.equals(_7, valueToFind);
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    public static final class DoubleTuple8 extends DoubleTuple<DoubleTuple8> {

        public final double _1;
        public final double _2;
        public final double _3;
        public final double _4;
        public final double _5;
        public final double _6;
        public final double _7;
        public final double _8;

        DoubleTuple8() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }

        DoubleTuple8(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7, final double _8) {
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
        public DoubleTuple8 reverse() {
            return new DoubleTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind)
                    || N.equals(_5, valueToFind) || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind);
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    public static final class DoubleTuple9 extends DoubleTuple<DoubleTuple9> {

        public final double _1;
        public final double _2;
        public final double _3;
        public final double _4;
        public final double _5;
        public final double _6;
        public final double _7;
        public final double _8;
        public final double _9;

        DoubleTuple9() {
            this(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        DoubleTuple9(final double _1, final double _2, final double _3, final double _4, final double _5, final double _6, final double _7, final double _8, final double _9) {
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
        public DoubleTuple9 reverse() {
            return new DoubleTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final double valueToFind) {
            return N.equals(_1, valueToFind) || N.equals(_2, valueToFind) || N.equals(_3, valueToFind) || N.equals(_4, valueToFind)
                    || N.equals(_5, valueToFind) || N.equals(_6, valueToFind) || N.equals(_7, valueToFind) || N.equals(_8, valueToFind)
                    || N.equals(_9, valueToFind);
        }

        @Override
        protected double[] elements() {
            if (elements == null) {
                elements = new double[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
