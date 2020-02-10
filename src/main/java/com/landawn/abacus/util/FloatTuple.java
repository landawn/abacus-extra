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

import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.FloatStream;

public abstract class FloatTuple<TP extends FloatTuple<TP>> extends PrimitiveTuple<TP> {
    protected transient float[] elements;

    public static FloatTuple1 of(float _1) {
        return new FloatTuple1(_1);
    }

    public static FloatTuple2 of(float _1, float _2) {
        return new FloatTuple2(_1, _2);
    }

    public static FloatTuple3 of(float _1, float _2, float _3) {
        return new FloatTuple3(_1, _2, _3);
    }

    public static FloatTuple4 of(float _1, float _2, float _3, float _4) {
        return new FloatTuple4(_1, _2, _3, _4);
    }

    public static FloatTuple5 of(float _1, float _2, float _3, float _4, float _5) {
        return new FloatTuple5(_1, _2, _3, _4, _5);
    }

    public static FloatTuple6 of(float _1, float _2, float _3, float _4, float _5, float _6) {
        return new FloatTuple6(_1, _2, _3, _4, _5, _6);
    }

    public static FloatTuple7 of(float _1, float _2, float _3, float _4, float _5, float _6, float _7) {
        return new FloatTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    public static FloatTuple8 of(float _1, float _2, float _3, float _4, float _5, float _6, float _7, float _8) {
        return new FloatTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    public static FloatTuple9 of(float _1, float _2, float _3, float _4, float _5, float _6, float _7, float _8, float _9) {
        return new FloatTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    @Override
    public abstract int arity();

    public float min() {
        return N.min(elements());
    }

    public float max() {
        return N.max(elements());
    }

    public float median() {
        return N.median(elements());
    }

    public float sum() {
        return N.sum(elements());
    }

    public double average() {
        return N.average(elements());
    }

    public abstract TP reverse();

    public abstract boolean contains(float elementToFind);

    public float[] toArray() {
        return elements().clone();
    }

    public FloatList toList() {
        return FloatList.of(elements().clone());
    }

    public <E extends Exception> void forEach(Throwables.FloatConsumer<E> comsumer) throws E {
        for (float e : elements()) {
            comsumer.accept(e);
        }
    }

    public FloatStream stream() {
        return FloatStream.of(elements());
    }

    @Override
    public int hashCode() {
        return N.hashCode(elements());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(this.getClass().equals(obj.getClass()))) {
            return false;
        } else {
            return N.equals(elements(), ((FloatTuple<TP>) obj).elements());
        }
    }

    @Override
    public String toString() {
        return N.toString(elements());
    }

    protected abstract float[] elements();

    public static final class FloatTuple1 extends FloatTuple<FloatTuple1> {

        public final float _1;

        FloatTuple1() {
            this(0);
        }

        FloatTuple1(float _1) {
            this._1 = _1;
        }

        @Override
        public int arity() {
            return 1;
        }

        @Override
        public float min() {
            return _1;
        }

        @Override
        public float max() {
            return _1;
        }

        @Override
        public float median() {
            return _1;
        }

        @Override
        public float sum() {
            return _1;
        }

        @Override
        public double average() {
            return _1;
        }

        @Override
        public FloatTuple1 reverse() {
            return new FloatTuple1(_1);
        }

        @Override
        public boolean contains(final float elementToFind) {
            return _1 == elementToFind;
        }

        @Override
        public int hashCode() {
            return (int) _1;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof FloatTuple1)) {
                return false;
            } else {
                FloatTuple1 other = (FloatTuple1) obj;
                return this._1 == other._1;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + "]";
        }

        @Override
        protected float[] elements() {
            if (this.elements == null) {
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

        FloatTuple2(float _1, float _2) {
            this._1 = _1;
            this._2 = _2;
        }

        @Override
        public int arity() {
            return 2;
        }

        @Override
        public float min() {
            return N.min(_1, _2);
        }

        @Override
        public float max() {
            return N.max(_1, _2);
        }

        @Override
        public float median() {
            return N.median(_1, _2);
        }

        @Override
        public float sum() {
            return N.sum(_1, _2);
        }

        @Override
        public double average() {
            return N.average(_1, _2);
        }

        @Override
        public FloatTuple2 reverse() {
            return new FloatTuple2(_2, _1);
        }

        @Override
        public boolean contains(final float elementToFind) {
            return _1 == elementToFind || _2 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.FloatConsumer<E> comsumer) throws E {
            comsumer.accept(this._1);
            comsumer.accept(this._2);
        }

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(Throwables.FloatBiConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(Throwables.FloatBiFunction<U, E> mapper) throws E {
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

        @Override
        public int hashCode() {
            return (int) (31 * _1 + _2);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof FloatTuple2)) {
                return false;
            } else {
                FloatTuple2 other = (FloatTuple2) obj;
                return this._1 == other._1 && this._2 == other._2;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + "]";
        }

        @Override
        protected float[] elements() {
            if (this.elements == null) {
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

        FloatTuple3(float _1, float _2, float _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        @Override
        public int arity() {
            return 3;
        }

        @Override
        public float min() {
            return N.min(_1, _2, _3);
        }

        @Override
        public float max() {
            return N.max(_1, _2, _3);
        }

        @Override
        public float median() {
            return N.median(_1, _2, _3);
        }

        @Override
        public float sum() {
            return N.sum(_1, _2, _3);
        }

        @Override
        public double average() {
            return N.average(_1, _2, _3);
        }

        @Override
        public FloatTuple3 reverse() {
            return new FloatTuple3(_3, _2, _1);
        }

        @Override
        public boolean contains(final float elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.FloatConsumer<E> comsumer) throws E {
            comsumer.accept(this._1);
            comsumer.accept(this._2);
            comsumer.accept(this._3);
        }

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(Throwables.FloatTriConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(Throwables.FloatTriFunction<U, E> mapper) throws E {
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

        @Override
        public int hashCode() {
            return (int) ((31 * (31 * _1 + _2)) + _3);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof FloatTuple3)) {
                return false;
            } else {
                FloatTuple3 other = (FloatTuple3) obj;
                return this._1 == other._1 && this._2 == other._2 && this._3 == other._3;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + "]";
        }

        @Override
        protected float[] elements() {
            if (this.elements == null) {
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

        FloatTuple4(float _1, float _2, float _3, float _4) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
        }

        @Override
        public int arity() {
            return 4;
        }

        @Override
        public FloatTuple4 reverse() {
            return new FloatTuple4(_4, _3, _2, _1);
        }

        @Override
        public boolean contains(final float elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind;
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

        FloatTuple5(float _1, float _2, float _3, float _4, float _5) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
        }

        @Override
        public int arity() {
            return 5;
        }

        @Override
        public FloatTuple5 reverse() {
            return new FloatTuple5(_5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final float elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind;
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

        FloatTuple6(float _1, float _2, float _3, float _4, float _5, float _6) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
        }

        @Override
        public int arity() {
            return 6;
        }

        @Override
        public FloatTuple6 reverse() {
            return new FloatTuple6(_6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final float elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind;
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

        FloatTuple7(float _1, float _2, float _3, float _4, float _5, float _6, float _7) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
        }

        @Override
        public int arity() {
            return 7;
        }

        @Override
        public FloatTuple7 reverse() {
            return new FloatTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final float elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind;
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

        FloatTuple8(float _1, float _2, float _3, float _4, float _5, float _6, float _7, float _8) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
            this._4 = _4;
            this._5 = _5;
            this._6 = _6;
            this._7 = _7;
            this._8 = _8;
        }

        @Override
        public int arity() {
            return 8;
        }

        @Override
        public FloatTuple8 reverse() {
            return new FloatTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final float elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind;
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

        FloatTuple9(float _1, float _2, float _3, float _4, float _5, float _6, float _7, float _8, float _9) {
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

        @Override
        public int arity() {
            return 9;
        }

        @Override
        public FloatTuple9 reverse() {
            return new FloatTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final float elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind || _9 == elementToFind;
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
