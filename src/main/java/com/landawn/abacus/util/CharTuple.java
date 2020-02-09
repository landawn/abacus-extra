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
import com.landawn.abacus.util.stream.CharStream;

public abstract class CharTuple<TP> {
    protected transient char[] elements;

    public static CharTuple1 of(char _1) {
        return new CharTuple1(_1);
    }

    public static CharTuple2 of(char _1, char _2) {
        return new CharTuple2(_1, _2);
    }

    public static CharTuple3 of(char _1, char _2, char _3) {
        return new CharTuple3(_1, _2, _3);
    }

    public static CharTuple4 of(char _1, char _2, char _3, char _4) {
        return new CharTuple4(_1, _2, _3, _4);
    }

    public static CharTuple5 of(char _1, char _2, char _3, char _4, char _5) {
        return new CharTuple5(_1, _2, _3, _4, _5);
    }

    public static CharTuple6 of(char _1, char _2, char _3, char _4, char _5, char _6) {
        return new CharTuple6(_1, _2, _3, _4, _5, _6);
    }

    public static CharTuple7 of(char _1, char _2, char _3, char _4, char _5, char _6, char _7) {
        return new CharTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    public static CharTuple8 of(char _1, char _2, char _3, char _4, char _5, char _6, char _7, char _8) {
        return new CharTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    public static CharTuple9 of(char _1, char _2, char _3, char _4, char _5, char _6, char _7, char _8, char _9) {
        return new CharTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    public abstract int arity();

    public char min() {
        return N.min(elements());
    }

    public char max() {
        return N.max(elements());
    }

    public char median() {
        return N.median(elements());
    }

    public int sum() {
        return N.sum(elements());
    }

    public double average() {
        return N.average(elements());
    }

    public abstract TP reverse();

    public abstract boolean contains(char elementToFind);

    public char[] toArray() {
        return elements().clone();
    }

    public CharList toList() {
        return CharList.of(elements().clone());
    }

    public <E extends Exception> void forEach(Throwables.CharConsumer<E> comsumer) throws E {
        for (char e : elements()) {
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

    public CharStream stream() {
        return CharStream.of(elements());
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
            return N.equals(elements(), ((CharTuple<TP>) obj).elements());
        }
    }

    @Override
    public String toString() {
        return N.toString(elements());
    }

    protected abstract char[] elements();

    public static final class CharTuple1 extends CharTuple<CharTuple1> {

        public final char _1;

        CharTuple1() {
            this((char) 0);
        }

        CharTuple1(char _1) {
            this._1 = _1;
        }

        @Override
        public int arity() {
            return 1;
        }

        @Override
        public char min() {
            return _1;
        }

        @Override
        public char max() {
            return _1;
        }

        @Override
        public char median() {
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
        public CharTuple1 reverse() {
            return new CharTuple1(_1);
        }

        @Override
        public boolean contains(final char elementToFind) {
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
            } else if (!(obj instanceof CharTuple1)) {
                return false;
            } else {
                CharTuple1 other = (CharTuple1) obj;
                return this._1 == other._1;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + "]";
        }

        @Override
        protected char[] elements() {
            if (this.elements == null) {
                elements = new char[] { _1 };
            }

            return elements;
        }
    }

    public static final class CharTuple2 extends CharTuple<CharTuple2> {

        public final char _1;
        public final char _2;

        CharTuple2() {
            this((char) 0, (char) 0);
        }

        CharTuple2(char _1, char _2) {
            this._1 = _1;
            this._2 = _2;
        }

        @Override
        public int arity() {
            return 2;
        }

        @Override
        public char min() {
            return N.min(_1, _2);
        }

        @Override
        public char max() {
            return N.max(_1, _2);
        }

        @Override
        public char median() {
            return N.median(_1, _2);
        }

        @Override
        public int sum() {
            return _1 + _2;
        }

        @Override
        public CharTuple2 reverse() {
            return new CharTuple2(_2, _1);
        }

        @Override
        public boolean contains(final char elementToFind) {
            return _1 == elementToFind || _2 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.CharConsumer<E> comsumer) throws E {
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
            } else if (!(obj instanceof CharTuple2)) {
                return false;
            } else {
                CharTuple2 other = (CharTuple2) obj;
                return this._1 == other._1 && this._2 == other._2;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + "]";
        }

        @Override
        protected char[] elements() {
            if (this.elements == null) {
                elements = new char[] { _1, _2 };
            }

            return elements;
        }
    }

    public static final class CharTuple3 extends CharTuple<CharTuple3> {

        public final char _1;
        public final char _2;
        public final char _3;

        CharTuple3() {
            this((char) 0, (char) 0, (char) 0);
        }

        CharTuple3(char _1, char _2, char _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        @Override
        public int arity() {
            return 3;
        }

        @Override
        public char min() {
            return N.min(_1, _2, _3);
        }

        @Override
        public char max() {
            return N.max(_1, _2, _3);
        }

        @Override
        public char median() {
            return N.median(_1, _2, _3);
        }

        @Override
        public int sum() {
            return _1 + _2 + _3;
        }

        @Override
        public CharTuple3 reverse() {
            return new CharTuple3(_3, _2, _1);
        }

        @Override
        public boolean contains(final char elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.CharConsumer<E> comsumer) throws E {
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
            } else if (!(obj instanceof CharTuple3)) {
                return false;
            } else {
                CharTuple3 other = (CharTuple3) obj;
                return this._1 == other._1 && this._2 == other._2 && this._3 == other._3;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + "]";
        }

        @Override
        protected char[] elements() {
            if (this.elements == null) {
                elements = new char[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    public static final class CharTuple4 extends CharTuple<CharTuple4> {

        public final char _1;
        public final char _2;
        public final char _3;
        public final char _4;

        CharTuple4() {
            this((char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple4(char _1, char _2, char _3, char _4) {
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
        public CharTuple4 reverse() {
            return new CharTuple4(_4, _3, _2, _1);
        }

        @Override
        public boolean contains(final char elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind;
        }

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    public static final class CharTuple5 extends CharTuple<CharTuple5> {

        public final char _1;
        public final char _2;
        public final char _3;
        public final char _4;
        public final char _5;

        CharTuple5() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple5(char _1, char _2, char _3, char _4, char _5) {
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
        public CharTuple5 reverse() {
            return new CharTuple5(_5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final char elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind;
        }

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    public static final class CharTuple6 extends CharTuple<CharTuple6> {

        public final char _1;
        public final char _2;
        public final char _3;
        public final char _4;
        public final char _5;
        public final char _6;

        CharTuple6() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple6(char _1, char _2, char _3, char _4, char _5, char _6) {
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
        public CharTuple6 reverse() {
            return new CharTuple6(_6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final char elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind;
        }

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    public static final class CharTuple7 extends CharTuple<CharTuple7> {

        public final char _1;
        public final char _2;
        public final char _3;
        public final char _4;
        public final char _5;
        public final char _6;
        public final char _7;

        CharTuple7() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple7(char _1, char _2, char _3, char _4, char _5, char _6, char _7) {
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
        public CharTuple7 reverse() {
            return new CharTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final char elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind;
        }

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    public static final class CharTuple8 extends CharTuple<CharTuple8> {

        public final char _1;
        public final char _2;
        public final char _3;
        public final char _4;
        public final char _5;
        public final char _6;
        public final char _7;
        public final char _8;

        CharTuple8() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple8(char _1, char _2, char _3, char _4, char _5, char _6, char _7, char _8) {
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
        public CharTuple8 reverse() {
            return new CharTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final char elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind;
        }

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    public static final class CharTuple9 extends CharTuple<CharTuple9> {

        public final char _1;
        public final char _2;
        public final char _3;
        public final char _4;
        public final char _5;
        public final char _6;
        public final char _7;
        public final char _8;
        public final char _9;

        CharTuple9() {
            this((char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0, (char) 0);
        }

        CharTuple9(char _1, char _2, char _3, char _4, char _5, char _6, char _7, char _8, char _9) {
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
        public CharTuple9 reverse() {
            return new CharTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final char elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind || _9 == elementToFind;
        }

        @Override
        protected char[] elements() {
            if (elements == null) {
                elements = new char[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
