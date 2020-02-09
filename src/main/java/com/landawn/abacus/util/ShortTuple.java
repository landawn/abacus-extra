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
import com.landawn.abacus.util.stream.ShortStream;

public abstract class ShortTuple<TP> {
    protected transient short[] elements;

    public static ShortTuple1 of(short _1) {
        return new ShortTuple1(_1);
    }

    public static ShortTuple2 of(short _1, short _2) {
        return new ShortTuple2(_1, _2);
    }

    public static ShortTuple3 of(short _1, short _2, short _3) {
        return new ShortTuple3(_1, _2, _3);
    }

    public static ShortTuple4 of(short _1, short _2, short _3, short _4) {
        return new ShortTuple4(_1, _2, _3, _4);
    }

    public static ShortTuple5 of(short _1, short _2, short _3, short _4, short _5) {
        return new ShortTuple5(_1, _2, _3, _4, _5);
    }

    public static ShortTuple6 of(short _1, short _2, short _3, short _4, short _5, short _6) {
        return new ShortTuple6(_1, _2, _3, _4, _5, _6);
    }

    public static ShortTuple7 of(short _1, short _2, short _3, short _4, short _5, short _6, short _7) {
        return new ShortTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    public static ShortTuple8 of(short _1, short _2, short _3, short _4, short _5, short _6, short _7, short _8) {
        return new ShortTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    public static ShortTuple9 of(short _1, short _2, short _3, short _4, short _5, short _6, short _7, short _8, short _9) {
        return new ShortTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    public abstract int arity();

    public short min() {
        return N.min(elements());
    }

    public short max() {
        return N.max(elements());
    }

    public short median() {
        return N.median(elements());
    }

    public int sum() {
        return N.sum(elements());
    }

    public double average() {
        return N.average(elements());
    }

    public abstract TP reverse();

    public abstract boolean contains(short elementToFind);

    public short[] toArray() {
        return elements().clone();
    }

    public ShortList toList() {
        return ShortList.of(elements().clone());
    }

    public <E extends Exception> void forEach(Throwables.ShortConsumer<E> comsumer) throws E {
        for (short e : elements()) {
            comsumer.accept(e);
        }
    }

    public <E extends Exception> void accept(Throwables.Consumer<TP, E> action) throws E {
        action.accept((TP) this);
    }

    public <U, E extends Exception> U map(Throwables.Function<TP, U, E> mapper) throws E {
        return mapper.apply((TP) this);
    }

    public <E extends Exception> Optional<TP> filter(final Throwables.Predicate<TP, E> predicate) throws E {
        return predicate.test((TP) this) ? Optional.of((TP) this) : Optional.<TP> empty();
    }

    public ShortStream stream() {
        return ShortStream.of(elements());
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
            return N.equals(elements(), ((ShortTuple<TP>) obj).elements());
        }
    }

    @Override
    public String toString() {
        return N.toString(elements());
    }

    protected abstract short[] elements();

    public static final class ShortTuple1 extends ShortTuple<ShortTuple1> {

        public final short _1;

        ShortTuple1() {
            this((short) 0);
        }

        ShortTuple1(short _1) {
            this._1 = _1;
        }

        @Override
        public int arity() {
            return 1;
        }

        @Override
        public short min() {
            return _1;
        }

        @Override
        public short max() {
            return _1;
        }

        @Override
        public short median() {
            return _1;
        }

        @Override
        public int sum() {
            return _1;
        }

        @Override
        public double average() {
            return _1;
        }

        @Override
        public ShortTuple1 reverse() {
            return new ShortTuple1(_1);
        }

        @Override
        public boolean contains(final short elementToFind) {
            return _1 == elementToFind;
        }

        @Override
        public int hashCode() {
            return _1;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof ShortTuple1)) {
                return false;
            } else {
                ShortTuple1 other = (ShortTuple1) obj;
                return this._1 == other._1;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + "]";
        }

        @Override
        protected short[] elements() {
            if (this.elements == null) {
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

        ShortTuple2(short _1, short _2) {
            this._1 = _1;
            this._2 = _2;
        }

        @Override
        public int arity() {
            return 2;
        }

        @Override
        public short min() {
            return N.min(_1, _2);
        }

        @Override
        public short max() {
            return N.max(_1, _2);
        }

        @Override
        public short median() {
            return N.median(_1, _2);
        }

        @Override
        public int sum() {
            return _1 + _2;
        }

        @Override
        public ShortTuple2 reverse() {
            return new ShortTuple2(_2, _1);
        }

        @Override
        public boolean contains(final short elementToFind) {
            return _1 == elementToFind || _2 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.ShortConsumer<E> comsumer) throws E {
            comsumer.accept(this._1);
            comsumer.accept(this._2);
        }

        @Override
        public int hashCode() {
            return 31 * _1 + _2;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof ShortTuple2)) {
                return false;
            } else {
                ShortTuple2 other = (ShortTuple2) obj;
                return this._1 == other._1 && this._2 == other._2;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + "]";
        }

        @Override
        protected short[] elements() {
            if (this.elements == null) {
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

        ShortTuple3(short _1, short _2, short _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        @Override
        public int arity() {
            return 3;
        }

        @Override
        public short min() {
            return N.min(_1, _2, _3);
        }

        @Override
        public short max() {
            return N.max(_1, _2, _3);
        }

        @Override
        public short median() {
            return N.median(_1, _2, _3);
        }

        @Override
        public int sum() {
            return _1 + _2 + _3;
        }

        @Override
        public ShortTuple3 reverse() {
            return new ShortTuple3(_3, _2, _1);
        }

        @Override
        public boolean contains(final short elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.ShortConsumer<E> comsumer) throws E {
            comsumer.accept(this._1);
            comsumer.accept(this._2);
            comsumer.accept(this._3);
        }

        @Override
        public int hashCode() {
            return (31 * (31 * _1 + _2)) + _3;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof ShortTuple3)) {
                return false;
            } else {
                ShortTuple3 other = (ShortTuple3) obj;
                return this._1 == other._1 && this._2 == other._2 && this._3 == other._3;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + "]";
        }

        @Override
        protected short[] elements() {
            if (this.elements == null) {
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

        ShortTuple4(short _1, short _2, short _3, short _4) {
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
        public ShortTuple4 reverse() {
            return new ShortTuple4(_4, _3, _2, _1);
        }

        @Override
        public boolean contains(final short elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind;
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

        ShortTuple5(short _1, short _2, short _3, short _4, short _5) {
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
        public ShortTuple5 reverse() {
            return new ShortTuple5(_5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final short elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind;
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

        ShortTuple6(short _1, short _2, short _3, short _4, short _5, short _6) {
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
        public ShortTuple6 reverse() {
            return new ShortTuple6(_6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final short elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind;
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

        ShortTuple7(short _1, short _2, short _3, short _4, short _5, short _6, short _7) {
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
        public ShortTuple7 reverse() {
            return new ShortTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final short elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind;
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

        ShortTuple8(short _1, short _2, short _3, short _4, short _5, short _6, short _7, short _8) {
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
        public ShortTuple8 reverse() {
            return new ShortTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final short elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind;
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

        ShortTuple9(short _1, short _2, short _3, short _4, short _5, short _6, short _7, short _8, short _9) {
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
        public ShortTuple9 reverse() {
            return new ShortTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final short elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind || _9 == elementToFind;
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
