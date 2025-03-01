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

import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.u.Optional;

import java.util.NoSuchElementException;

@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class IntTuple<TP extends IntTuple<TP>> extends PrimitiveTuple<TP> {

    protected int[] elements;

    /**
     *
     *
     * @param _1
     * @return
     */
    public static IntTuple1 of(final int _1) {
        return new IntTuple1(_1);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @return
     */
    public static IntTuple2 of(final int _1, final int _2) {
        return new IntTuple2(_1, _2);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @param _3
     * @return
     */
    public static IntTuple3 of(final int _1, final int _2, final int _3) {
        return new IntTuple3(_1, _2, _3);
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
    public static IntTuple4 of(final int _1, final int _2, final int _3, final int _4) {
        return new IntTuple4(_1, _2, _3, _4);
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
    public static IntTuple5 of(final int _1, final int _2, final int _3, final int _4, final int _5) {
        return new IntTuple5(_1, _2, _3, _4, _5);
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
    public static IntTuple6 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6) {
        return new IntTuple6(_1, _2, _3, _4, _5, _6);
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
    public static IntTuple7 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7) {
        return new IntTuple7(_1, _2, _3, _4, _5, _6, _7);
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
    public static IntTuple8 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8) {
        return new IntTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
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
    public static IntTuple9 of(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8, final int _9) {
        return new IntTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     *
     *
     * @param <TP>
     * @param a
     * @return
     */
    public static <TP extends IntTuple<TP>> TP create(final int[] a) {
        if (a == null || a.length == 0) {
            return (TP) IntTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) IntTuple.of(a[0]);

            case 2:
                return (TP) IntTuple.of(a[0], a[1]);

            case 3:
                return (TP) IntTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) IntTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
    }

    /**
     *
     *
     * @return
     */
    public int min() {
        return N.min(elements());
    }

    /**
     *
     *
     * @return
     */
    public int max() {
        return N.max(elements());
    }

    /**
     *
     *
     * @return
     */
    public int median() {
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
    public abstract boolean contains(int valueToFind);

    /**
     *
     *
     * @return
     */
    public int[] toArray() {
        return elements().clone();
    }

    /**
     *
     *
     * @return
     */
    public IntList toList() {
        return IntList.of(elements().clone());
    }

    /**
     *
     *
     * @param <E>
     * @param consumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
        for (final int e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     *
     *
     * @return
     */
    public IntStream stream() {
        return IntStream.of(elements());
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
            return N.equals(elements(), ((IntTuple<TP>) obj).elements());
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

    protected abstract int[] elements();

    static final class IntTuple0 extends IntTuple<IntTuple0> {

        private static final IntTuple0 EMPTY = new IntTuple0();

        IntTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public int min() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public int max() {
            throw new NoSuchElementException(InternalUtil.ERROR_MSG_FOR_NO_SUCH_EX);
        }

        @Override
        public int median() {
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
        public IntTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final int valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected int[] elements() {
            return N.EMPTY_INT_ARRAY;
        }
    }

    public static final class IntTuple1 extends IntTuple<IntTuple1> {

        public final int _1;

        IntTuple1() {
            this(0);
        }

        IntTuple1(final int _1) {
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
        public int min() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int max() {
            return _1;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int median() {
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
        public IntTuple1 reverse() {
            return new IntTuple1(_1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final int valueToFind) {
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
            } else if (!(obj instanceof final IntTuple1 other)) {
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
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1 };
            }

            return elements;
        }
    }

    public static final class IntTuple2 extends IntTuple<IntTuple2> {

        public final int _1;
        public final int _2;

        IntTuple2() {
            this(0, 0);
        }

        IntTuple2(final int _1, final int _2) {
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
        public int min() {
            return N.min(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int max() {
            return N.max(_1, _2);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int median() {
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
        public IntTuple2 reverse() {
            return new IntTuple2(_2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final int valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(final Throwables.IntBiConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.IntBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<IntTuple2> filter(final Throwables.IntBiPredicate<E> predicate) throws E {
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
            } else if (!(obj instanceof final IntTuple2 other)) {
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
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2 };
            }

            return elements;
        }
    }

    public static final class IntTuple3 extends IntTuple<IntTuple3> {

        public final int _1;
        public final int _2;
        public final int _3;

        IntTuple3() {
            this(0, 0, 0);
        }

        IntTuple3(final int _1, final int _2, final int _3) {
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
        public int min() {
            return N.min(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int max() {
            return N.max(_1, _2, _3);
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int median() {
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
        public IntTuple3 reverse() {
            return new IntTuple3(_3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final int valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.IntConsumer<E> consumer) throws E {
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
        public <E extends Exception> void accept(final Throwables.IntTriConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.IntTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<IntTuple3> filter(final Throwables.IntTriPredicate<E> predicate) throws E {
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
            } else if (!(obj instanceof final IntTuple3 other)) {
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
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    public static final class IntTuple4 extends IntTuple<IntTuple4> {

        public final int _1;
        public final int _2;
        public final int _3;
        public final int _4;

        IntTuple4() {
            this(0, 0, 0, 0);
        }

        IntTuple4(final int _1, final int _2, final int _3, final int _4) {
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
        public IntTuple4 reverse() {
            return new IntTuple4(_4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    public static final class IntTuple5 extends IntTuple<IntTuple5> {

        public final int _1;
        public final int _2;
        public final int _3;
        public final int _4;
        public final int _5;

        IntTuple5() {
            this(0, 0, 0, 0, 0);
        }

        IntTuple5(final int _1, final int _2, final int _3, final int _4, final int _5) {
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
        public IntTuple5 reverse() {
            return new IntTuple5(_5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    public static final class IntTuple6 extends IntTuple<IntTuple6> {

        public final int _1;
        public final int _2;
        public final int _3;
        public final int _4;
        public final int _5;
        public final int _6;

        IntTuple6() {
            this(0, 0, 0, 0, 0, 0);
        }

        IntTuple6(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6) {
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
        public IntTuple6 reverse() {
            return new IntTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    public static final class IntTuple7 extends IntTuple<IntTuple7> {

        public final int _1;
        public final int _2;
        public final int _3;
        public final int _4;
        public final int _5;
        public final int _6;
        public final int _7;

        IntTuple7() {
            this(0, 0, 0, 0, 0, 0, 0);
        }

        IntTuple7(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7) {
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
        public IntTuple7 reverse() {
            return new IntTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    public static final class IntTuple8 extends IntTuple<IntTuple8> {

        public final int _1;
        public final int _2;
        public final int _3;
        public final int _4;
        public final int _5;
        public final int _6;
        public final int _7;
        public final int _8;

        IntTuple8() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }

        IntTuple8(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8) {
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
        public IntTuple8 reverse() {
            return new IntTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    public static final class IntTuple9 extends IntTuple<IntTuple9> {

        public final int _1;
        public final int _2;
        public final int _3;
        public final int _4;
        public final int _5;
        public final int _6;
        public final int _7;
        public final int _8;
        public final int _9;

        IntTuple9() {
            this(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        IntTuple9(final int _1, final int _2, final int _3, final int _4, final int _5, final int _6, final int _7, final int _8, final int _9) {
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
        public IntTuple9 reverse() {
            return new IntTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final int valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        @Override
        protected int[] elements() {
            if (elements == null) {
                elements = new int[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
