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

import com.landawn.abacus.util.stream.DoubleStream;

public abstract class DoubleTuple<TP extends DoubleTuple<TP>> extends PrimitiveTuple<TP> {
    protected transient double[] elements;

    public static DoubleTuple1 of(double _1) {
        return new DoubleTuple1(_1);
    }

    public static DoubleTuple2 of(double _1, double _2) {
        return new DoubleTuple2(_1, _2);
    }

    public static DoubleTuple3 of(double _1, double _2, double _3) {
        return new DoubleTuple3(_1, _2, _3);
    }

    public static DoubleTuple4 of(double _1, double _2, double _3, double _4) {
        return new DoubleTuple4(_1, _2, _3, _4);
    }

    public static DoubleTuple5 of(double _1, double _2, double _3, double _4, double _5) {
        return new DoubleTuple5(_1, _2, _3, _4, _5);
    }

    public static DoubleTuple6 of(double _1, double _2, double _3, double _4, double _5, double _6) {
        return new DoubleTuple6(_1, _2, _3, _4, _5, _6);
    }

    public static DoubleTuple7 of(double _1, double _2, double _3, double _4, double _5, double _6, double _7) {
        return new DoubleTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    public static DoubleTuple8 of(double _1, double _2, double _3, double _4, double _5, double _6, double _7, double _8) {
        return new DoubleTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    public static DoubleTuple9 of(double _1, double _2, double _3, double _4, double _5, double _6, double _7, double _8, double _9) {
        return new DoubleTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    @Override
    public abstract int arity();

    public double min() {
        return N.min(elements());
    }

    public double max() {
        return N.max(elements());
    }

    public double median() {
        return N.median(elements());
    }

    public double sum() {
        return N.sum(elements());
    }

    public double average() {
        return N.average(elements());
    }

    public abstract TP reverse();

    public abstract boolean contains(double elementToFind);

    public double[] toArray() {
        return elements().clone();
    }

    public DoubleList toList() {
        return DoubleList.of(elements().clone());
    }

    public <E extends Exception> void forEach(Throwables.DoubleConsumer<E> comsumer) throws E {
        for (double e : elements()) {
            comsumer.accept(e);
        }
    }

    public DoubleStream stream() {
        return DoubleStream.of(elements());
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
            return N.equals(elements(), ((DoubleTuple<TP>) obj).elements());
        }
    }

    @Override
    public String toString() {
        return N.toString(elements());
    }

    protected abstract double[] elements();

    public static final class DoubleTuple1 extends DoubleTuple<DoubleTuple1> {

        public final double _1;

        DoubleTuple1() {
            this(0);
        }

        DoubleTuple1(double _1) {
            this._1 = _1;
        }

        @Override
        public int arity() {
            return 1;
        }

        @Override
        public double min() {
            return _1;
        }

        @Override
        public double max() {
            return _1;
        }

        @Override
        public double median() {
            return _1;
        }

        @Override
        public double sum() {
            return _1;
        }

        @Override
        public double average() {
            return _1;
        }

        @Override
        public DoubleTuple1 reverse() {
            return new DoubleTuple1(_1);
        }

        @Override
        public boolean contains(final double elementToFind) {
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
            } else if (!(obj instanceof DoubleTuple1)) {
                return false;
            } else {
                DoubleTuple1 other = (DoubleTuple1) obj;
                return this._1 == other._1;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + "]";
        }

        @Override
        protected double[] elements() {
            if (this.elements == null) {
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

        DoubleTuple2(double _1, double _2) {
            this._1 = _1;
            this._2 = _2;
        }

        @Override
        public int arity() {
            return 2;
        }

        @Override
        public double min() {
            return N.min(_1, _2);
        }

        @Override
        public double max() {
            return N.max(_1, _2);
        }

        @Override
        public double median() {
            return N.median(_1, _2);
        }

        @Override
        public double sum() {
            return N.sum(_1, _2);
        }

        @Override
        public double average() {
            return N.average(_1, _2);
        }

        @Override
        public DoubleTuple2 reverse() {
            return new DoubleTuple2(_2, _1);
        }

        @Override
        public boolean contains(final double elementToFind) {
            return _1 == elementToFind || _2 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.DoubleConsumer<E> comsumer) throws E {
            comsumer.accept(this._1);
            comsumer.accept(this._2);
        }

        @Override
        public int hashCode() {
            return (int) (31 * _1 + _2);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof DoubleTuple2)) {
                return false;
            } else {
                DoubleTuple2 other = (DoubleTuple2) obj;
                return this._1 == other._1 && this._2 == other._2;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + "]";
        }

        @Override
        protected double[] elements() {
            if (this.elements == null) {
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

        DoubleTuple3(double _1, double _2, double _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        @Override
        public int arity() {
            return 3;
        }

        @Override
        public double min() {
            return N.min(_1, _2, _3);
        }

        @Override
        public double max() {
            return N.max(_1, _2, _3);
        }

        @Override
        public double median() {
            return N.median(_1, _2, _3);
        }

        @Override
        public double sum() {
            return N.sum(_1, _2, _3);
        }

        @Override
        public double average() {
            return N.average(_1, _2, _3);
        }

        @Override
        public DoubleTuple3 reverse() {
            return new DoubleTuple3(_3, _2, _1);
        }

        @Override
        public boolean contains(final double elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.DoubleConsumer<E> comsumer) throws E {
            comsumer.accept(this._1);
            comsumer.accept(this._2);
            comsumer.accept(this._3);
        }

        @Override
        public int hashCode() {
            return (int) ((31 * (31 * _1 + _2)) + _3);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof DoubleTuple3)) {
                return false;
            } else {
                DoubleTuple3 other = (DoubleTuple3) obj;
                return this._1 == other._1 && this._2 == other._2 && this._3 == other._3;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + "]";
        }

        @Override
        protected double[] elements() {
            if (this.elements == null) {
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

        DoubleTuple4(double _1, double _2, double _3, double _4) {
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
        public DoubleTuple4 reverse() {
            return new DoubleTuple4(_4, _3, _2, _1);
        }

        @Override
        public boolean contains(final double elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind;
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

        DoubleTuple5(double _1, double _2, double _3, double _4, double _5) {
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
        public DoubleTuple5 reverse() {
            return new DoubleTuple5(_5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final double elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind;
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

        DoubleTuple6(double _1, double _2, double _3, double _4, double _5, double _6) {
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
        public DoubleTuple6 reverse() {
            return new DoubleTuple6(_6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final double elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind;
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

        DoubleTuple7(double _1, double _2, double _3, double _4, double _5, double _6, double _7) {
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
        public DoubleTuple7 reverse() {
            return new DoubleTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final double elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind;
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

        DoubleTuple8(double _1, double _2, double _3, double _4, double _5, double _6, double _7, double _8) {
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
        public DoubleTuple8 reverse() {
            return new DoubleTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final double elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind;
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

        DoubleTuple9(double _1, double _2, double _3, double _4, double _5, double _6, double _7, double _8, double _9) {
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
        public DoubleTuple9 reverse() {
            return new DoubleTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final double elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind || _9 == elementToFind;
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
