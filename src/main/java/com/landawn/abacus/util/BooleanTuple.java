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

import com.landawn.abacus.util.stream.Stream;
import com.landawn.abacus.util.u.Optional;

@SuppressWarnings({ "java:S116", "java:S2160", "java:S1845" })
public abstract class BooleanTuple<TP extends BooleanTuple<TP>> extends PrimitiveTuple<TP> {

    protected boolean[] elements;

    /**
     *
     *
     * @param _1
     * @return
     */
    public static BooleanTuple1 of(final boolean _1) {
        return new BooleanTuple1(_1);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @return
     */
    public static BooleanTuple2 of(final boolean _1, final boolean _2) {
        return new BooleanTuple2(_1, _2);
    }

    /**
     *
     *
     * @param _1
     * @param _2
     * @param _3
     * @return
     */
    public static BooleanTuple3 of(final boolean _1, final boolean _2, final boolean _3) {
        return new BooleanTuple3(_1, _2, _3);
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
    public static BooleanTuple4 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4) {
        return new BooleanTuple4(_1, _2, _3, _4);
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
    public static BooleanTuple5 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5) {
        return new BooleanTuple5(_1, _2, _3, _4, _5);
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
    public static BooleanTuple6 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6) {
        return new BooleanTuple6(_1, _2, _3, _4, _5, _6);
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
    public static BooleanTuple7 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6,
            final boolean _7) {
        return new BooleanTuple7(_1, _2, _3, _4, _5, _6, _7);
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
    public static BooleanTuple8 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7,
            final boolean _8) {
        return new BooleanTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
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
    public static BooleanTuple9 of(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7,
            final boolean _8, final boolean _9) {
        return new BooleanTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     *
     *
     * @param <TP>
     * @param a
     * @return
     */
    public static <TP extends BooleanTuple<TP>> TP create(final boolean[] a) {
        if (a == null || a.length == 0) {
            return (TP) BooleanTuple0.EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) BooleanTuple.of(a[0]);

            case 2:
                return (TP) BooleanTuple.of(a[0], a[1]);

            case 3:
                return (TP) BooleanTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) BooleanTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new IllegalArgumentException("Too many elements((" + a.length + ") to fill in Tuple.");
        }
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
    public abstract boolean contains(boolean valueToFind);

    /**
     *
     *
     * @return
     */
    public boolean[] toArray() {
        return elements().clone();
    }

    /**
     *
     *
     * @return
     */
    public BooleanList toList() {
        return BooleanList.of(elements().clone());
    }

    /**
     *
     *
     * @param <E>
     * @param consumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
        for (final boolean e : elements()) {
            consumer.accept(e);
        }
    }

    /**
     *
     *
     * @return
     */
    public Stream<Boolean> stream() {
        return Stream.of(elements());
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
            return N.equals(elements(), ((BooleanTuple<TP>) obj).elements());
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

    protected abstract boolean[] elements();

    static final class BooleanTuple0 extends BooleanTuple<BooleanTuple0> {
        private static final BooleanTuple0 EMPTY = new BooleanTuple0();

        BooleanTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public BooleanTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final boolean valueToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected boolean[] elements() {
            return N.EMPTY_BOOLEAN_ARRAY;
        }
    }

    public static final class BooleanTuple1 extends BooleanTuple<BooleanTuple1> {

        public final boolean _1;

        BooleanTuple1() {
            this(false);
        }

        BooleanTuple1(final boolean _1) {
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
        public BooleanTuple1 reverse() {
            return new BooleanTuple1(_1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind;
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int hashCode() {
            return _1 ? 1231 : 1237;
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
            } else if (!(obj instanceof final BooleanTuple1 other)) {
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
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1 };
            }

            return elements;
        }
    }

    public static final class BooleanTuple2 extends BooleanTuple<BooleanTuple2> {

        public final boolean _1;
        public final boolean _2;

        BooleanTuple2() {
            this(false, false);
        }

        BooleanTuple2(final boolean _1, final boolean _2) {
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
        public BooleanTuple2 reverse() {
            return new BooleanTuple2(_2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final boolean valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
            consumer.accept(_1);
            consumer.accept(_2);
        }

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(final Throwables.BooleanBiConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.BooleanBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<BooleanTuple2> filter(final Throwables.BooleanBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.empty();
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int hashCode() {
            int result = 1;

            result = 31 * result + (_1 ? 1231 : 1237);
            return 31 * result + (_2 ? 1231 : 1237);
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
            } else if (!(obj instanceof final BooleanTuple2 other)) {
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
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2 };
            }

            return elements;
        }
    }

    public static final class BooleanTuple3 extends BooleanTuple<BooleanTuple3> {

        public final boolean _1;
        public final boolean _2;
        public final boolean _3;

        BooleanTuple3() {
            this(false, false, false);
        }

        BooleanTuple3(final boolean _1, final boolean _2, final boolean _3) {
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
        public BooleanTuple3 reverse() {
            return new BooleanTuple3(_3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final boolean valueToFind) {
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
        public <E extends Exception> void forEach(final Throwables.BooleanConsumer<E> consumer) throws E {
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
        public <E extends Exception> void accept(final Throwables.BooleanTriConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(final Throwables.BooleanTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<BooleanTuple3> filter(final Throwables.BooleanTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.empty();
        }

        /**
         *
         *
         * @return
         */
        @Override
        public int hashCode() {
            int result = 1;

            result = 31 * result + (_1 ? 1231 : 1237);
            result = 31 * result + (_2 ? 1231 : 1237);
            return 31 * result + (_3 ? 1231 : 1237);
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
            } else if (!(obj instanceof final BooleanTuple3 other)) {
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
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    public static final class BooleanTuple4 extends BooleanTuple<BooleanTuple4> {

        public final boolean _1;
        public final boolean _2;
        public final boolean _3;
        public final boolean _4;

        BooleanTuple4() {
            this(false, false, false, false);
        }

        BooleanTuple4(final boolean _1, final boolean _2, final boolean _3, final boolean _4) {
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
        public BooleanTuple4 reverse() {
            return new BooleanTuple4(_4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    public static final class BooleanTuple5 extends BooleanTuple<BooleanTuple5> {

        public final boolean _1;
        public final boolean _2;
        public final boolean _3;
        public final boolean _4;
        public final boolean _5;

        BooleanTuple5() {
            this(false, false, false, false, false);
        }

        BooleanTuple5(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5) {
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
        public BooleanTuple5 reverse() {
            return new BooleanTuple5(_5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    public static final class BooleanTuple6 extends BooleanTuple<BooleanTuple6> {

        public final boolean _1;
        public final boolean _2;
        public final boolean _3;
        public final boolean _4;
        public final boolean _5;
        public final boolean _6;

        BooleanTuple6() {
            this(false, false, false, false, false, false);
        }

        BooleanTuple6(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6) {
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
        public BooleanTuple6 reverse() {
            return new BooleanTuple6(_6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    public static final class BooleanTuple7 extends BooleanTuple<BooleanTuple7> {

        public final boolean _1;
        public final boolean _2;
        public final boolean _3;
        public final boolean _4;
        public final boolean _5;
        public final boolean _6;
        public final boolean _7;

        BooleanTuple7() {
            this(false, false, false, false, false, false, false);
        }

        BooleanTuple7(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7) {
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
        public BooleanTuple7 reverse() {
            return new BooleanTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    public static final class BooleanTuple8 extends BooleanTuple<BooleanTuple8> {

        public final boolean _1;
        public final boolean _2;
        public final boolean _3;
        public final boolean _4;
        public final boolean _5;
        public final boolean _6;
        public final boolean _7;
        public final boolean _8;

        BooleanTuple8() {
            this(false, false, false, false, false, false, false, false);
        }

        BooleanTuple8(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7,
                final boolean _8) {
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
        public BooleanTuple8 reverse() {
            return new BooleanTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    public static final class BooleanTuple9 extends BooleanTuple<BooleanTuple9> {

        public final boolean _1;
        public final boolean _2;
        public final boolean _3;
        public final boolean _4;
        public final boolean _5;
        public final boolean _6;
        public final boolean _7;
        public final boolean _8;
        public final boolean _9;

        BooleanTuple9() {
            this(false, false, false, false, false, false, false, false, false);
        }

        BooleanTuple9(final boolean _1, final boolean _2, final boolean _3, final boolean _4, final boolean _5, final boolean _6, final boolean _7,
                final boolean _8, final boolean _9) {
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
        public BooleanTuple9 reverse() {
            return new BooleanTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        /**
         *
         *
         * @param valueToFind
         * @return
         */
        @Override
        public boolean contains(final boolean valueToFind) {
            return _1 == valueToFind || _2 == valueToFind || _3 == valueToFind || _4 == valueToFind || _5 == valueToFind || _6 == valueToFind
                    || _7 == valueToFind || _8 == valueToFind || _9 == valueToFind;
        }

        @Override
        protected boolean[] elements() {
            if (elements == null) {
                elements = new boolean[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
