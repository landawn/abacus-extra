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
import com.landawn.abacus.util.stream.ByteStream;

public abstract class ByteTuple<TP> {
    protected transient byte[] elements;

    public static ByteTuple1 of(byte _1) {
        return new ByteTuple1(_1);
    }

    public static ByteTuple2 of(byte _1, byte _2) {
        return new ByteTuple2(_1, _2);
    }

    public static ByteTuple3 of(byte _1, byte _2, byte _3) {
        return new ByteTuple3(_1, _2, _3);
    }

    public static ByteTuple4 of(byte _1, byte _2, byte _3, byte _4) {
        return new ByteTuple4(_1, _2, _3, _4);
    }

    public static ByteTuple5 of(byte _1, byte _2, byte _3, byte _4, byte _5) {
        return new ByteTuple5(_1, _2, _3, _4, _5);
    }

    public static ByteTuple6 of(byte _1, byte _2, byte _3, byte _4, byte _5, byte _6) {
        return new ByteTuple6(_1, _2, _3, _4, _5, _6);
    }

    public static ByteTuple7 of(byte _1, byte _2, byte _3, byte _4, byte _5, byte _6, byte _7) {
        return new ByteTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    public static ByteTuple8 of(byte _1, byte _2, byte _3, byte _4, byte _5, byte _6, byte _7, byte _8) {
        return new ByteTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    public static ByteTuple9 of(byte _1, byte _2, byte _3, byte _4, byte _5, byte _6, byte _7, byte _8, byte _9) {
        return new ByteTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    public abstract int arity();

    public byte min() {
        return N.min(elements());
    }

    public byte max() {
        return N.max(elements());
    }

    public byte median() {
        return N.median(elements());
    }

    public int sum() {
        return N.sum(elements());
    }

    public double average() {
        return N.average(elements());
    }

    public abstract TP reverse();

    public abstract boolean contains(byte elementToFind);

    public byte[] toArray() {
        return elements().clone();
    }

    public ByteList toList() {
        return ByteList.of(elements().clone());
    }

    public <E extends Exception> void forEach(Throwables.ByteConsumer<E> comsumer) throws E {
        for (byte e : elements()) {
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

    public ByteStream stream() {
        return ByteStream.of(elements());
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
            return N.equals(elements(), ((ByteTuple<TP>) obj).elements());
        }
    }

    @Override
    public String toString() {
        return N.toString(elements());
    }

    protected abstract byte[] elements();

    public static final class ByteTuple1 extends ByteTuple<ByteTuple1> {

        public final byte _1;

        ByteTuple1() {
            this((byte) 0);
        }

        ByteTuple1(byte _1) {
            this._1 = _1;
        }

        @Override
        public int arity() {
            return 1;
        }

        @Override
        public byte min() {
            return _1;
        }

        @Override
        public byte max() {
            return _1;
        }

        @Override
        public byte median() {
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
        public ByteTuple1 reverse() {
            return new ByteTuple1(_1);
        }

        @Override
        public boolean contains(final byte elementToFind) {
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
            } else if (!(obj instanceof ByteTuple1)) {
                return false;
            } else {
                ByteTuple1 other = (ByteTuple1) obj;
                return this._1 == other._1;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + "]";
        }

        @Override
        protected byte[] elements() {
            if (this.elements == null) {
                elements = new byte[] { _1 };
            }

            return elements;
        }
    }

    public static final class ByteTuple2 extends ByteTuple<ByteTuple2> {

        public final byte _1;
        public final byte _2;

        ByteTuple2() {
            this((byte) 0, (byte) 0);
        }

        ByteTuple2(byte _1, byte _2) {
            this._1 = _1;
            this._2 = _2;
        }

        @Override
        public int arity() {
            return 2;
        }

        @Override
        public byte min() {
            return N.min(_1, _2);
        }

        @Override
        public byte max() {
            return N.max(_1, _2);
        }

        @Override
        public byte median() {
            return N.median(_1, _2);
        }

        @Override
        public int sum() {
            return _1 + _2;
        }

        @Override
        public ByteTuple2 reverse() {
            return new ByteTuple2(_2, _1);
        }

        @Override
        public boolean contains(final byte elementToFind) {
            return _1 == elementToFind || _2 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.ByteConsumer<E> comsumer) throws E {
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
            } else if (!(obj instanceof ByteTuple2)) {
                return false;
            } else {
                ByteTuple2 other = (ByteTuple2) obj;
                return this._1 == other._1 && this._2 == other._2;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + "]";
        }

        @Override
        protected byte[] elements() {
            if (this.elements == null) {
                elements = new byte[] { _1, _2 };
            }

            return elements;
        }
    }

    public static final class ByteTuple3 extends ByteTuple<ByteTuple3> {

        public final byte _1;
        public final byte _2;
        public final byte _3;

        ByteTuple3() {
            this((byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple3(byte _1, byte _2, byte _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        @Override
        public int arity() {
            return 3;
        }

        @Override
        public byte min() {
            return N.min(_1, _2, _3);
        }

        @Override
        public byte max() {
            return N.max(_1, _2, _3);
        }

        @Override
        public byte median() {
            return N.median(_1, _2, _3);
        }

        @Override
        public int sum() {
            return _1 + _2 + _3;
        }

        @Override
        public ByteTuple3 reverse() {
            return new ByteTuple3(_3, _2, _1);
        }

        @Override
        public boolean contains(final byte elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.ByteConsumer<E> comsumer) throws E {
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
            } else if (!(obj instanceof ByteTuple3)) {
                return false;
            } else {
                ByteTuple3 other = (ByteTuple3) obj;
                return this._1 == other._1 && this._2 == other._2 && this._3 == other._3;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + "]";
        }

        @Override
        protected byte[] elements() {
            if (this.elements == null) {
                elements = new byte[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    public static final class ByteTuple4 extends ByteTuple<ByteTuple4> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;

        ByteTuple4() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple4(byte _1, byte _2, byte _3, byte _4) {
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
        public ByteTuple4 reverse() {
            return new ByteTuple4(_4, _3, _2, _1);
        }

        @Override
        public boolean contains(final byte elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    public static final class ByteTuple5 extends ByteTuple<ByteTuple5> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;
        public final byte _5;

        ByteTuple5() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple5(byte _1, byte _2, byte _3, byte _4, byte _5) {
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
        public ByteTuple5 reverse() {
            return new ByteTuple5(_5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final byte elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    public static final class ByteTuple6 extends ByteTuple<ByteTuple6> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;
        public final byte _5;
        public final byte _6;

        ByteTuple6() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple6(byte _1, byte _2, byte _3, byte _4, byte _5, byte _6) {
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
        public ByteTuple6 reverse() {
            return new ByteTuple6(_6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final byte elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    public static final class ByteTuple7 extends ByteTuple<ByteTuple7> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;
        public final byte _5;
        public final byte _6;
        public final byte _7;

        ByteTuple7() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple7(byte _1, byte _2, byte _3, byte _4, byte _5, byte _6, byte _7) {
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
        public ByteTuple7 reverse() {
            return new ByteTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final byte elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    public static final class ByteTuple8 extends ByteTuple<ByteTuple8> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;
        public final byte _5;
        public final byte _6;
        public final byte _7;
        public final byte _8;

        ByteTuple8() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple8(byte _1, byte _2, byte _3, byte _4, byte _5, byte _6, byte _7, byte _8) {
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
        public ByteTuple8 reverse() {
            return new ByteTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final byte elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    public static final class ByteTuple9 extends ByteTuple<ByteTuple9> {

        public final byte _1;
        public final byte _2;
        public final byte _3;
        public final byte _4;
        public final byte _5;
        public final byte _6;
        public final byte _7;
        public final byte _8;
        public final byte _9;

        ByteTuple9() {
            this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }

        ByteTuple9(byte _1, byte _2, byte _3, byte _4, byte _5, byte _6, byte _7, byte _8, byte _9) {
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
        public ByteTuple9 reverse() {
            return new ByteTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final byte elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind || _9 == elementToFind;
        }

        @Override
        protected byte[] elements() {
            if (elements == null) {
                elements = new byte[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
