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
import com.landawn.abacus.util.stream.ByteStream;

public abstract class ByteTuple<TP extends ByteTuple<TP>> extends PrimitiveTuple<TP> {

    private static final ByteTuple0 EMPTY = new ByteTuple0();

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
    public static ByteTuple8 of(byte _1, byte _2, byte _3, byte _4, byte _5, byte _6, byte _7, byte _8) {
        return new ByteTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
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
    public static ByteTuple9 of(byte _1, byte _2, byte _3, byte _4, byte _5, byte _6, byte _7, byte _8, byte _9) {
        return new ByteTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    public static <TP extends ByteTuple<TP>> TP from(final byte[] a) {
        if (a == null || a.length == 0) {
            return (TP) EMPTY;
        }

        switch (a.length) {
            case 1:
                return (TP) ByteTuple.of(a[0]);

            case 2:
                return (TP) ByteTuple.of(a[0], a[1]);

            case 3:
                return (TP) ByteTuple.of(a[0], a[1], a[2]);

            case 4:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3]);

            case 5:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4]);

            case 6:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5]);

            case 7:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);

            case 8:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

            case 9:
                return (TP) ByteTuple.of(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);

            default:
                throw new RuntimeException("Too many elements(" + a.length + ") to fill in Tuple.");
        }
    }

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
        } else if (obj == null || !(this.getClass().equals(obj.getClass()))) {
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

    static final class ByteTuple0 extends ByteTuple<ByteTuple0> {

        ByteTuple0() {
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public byte min() {
            throw new NoSuchElementException();
        }

        @Override
        public byte max() {
            throw new NoSuchElementException();
        }

        @Override
        public byte median() {
            throw new NoSuchElementException();
        }

        @Override
        public int sum() {
            return 0;
        }

        @Override
        public double average() {
            throw new NoSuchElementException();
        }

        @Override
        public ByteTuple0 reverse() {
            return this;
        }

        @Override
        public boolean contains(final byte elementToFind) {
            return false;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        protected byte[] elements() {
            return N.EMPTY_BYTE_ARRAY;
        }
    }

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
            return N.sum(_1, _2);
        }

        @Override
        public double average() {
            return N.average(_1, _2);
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

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(Throwables.ByteBiConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(Throwables.ByteBiFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<ByteTuple2> filter(final Throwables.ByteBiPredicate<E> predicate) throws E {
            return predicate.test(_1, _2) ? Optional.of(this) : Optional.<ByteTuple2> empty();
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
            return N.sum(_1, _2, _3);
        }

        @Override
        public double average() {
            return N.average(_1, _2, _3);
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

        /**
         *
         * @param <E>
         * @param action
         * @throws E the e
         */
        public <E extends Exception> void accept(Throwables.ByteTriConsumer<E> action) throws E {
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
        public <U, E extends Exception> U map(Throwables.ByteTriFunction<U, E> mapper) throws E {
            return mapper.apply(_1, _2, _3);
        }

        /**
         *
         * @param <E>
         * @param predicate
         * @return
         * @throws E the e
         */
        public <E extends Exception> Optional<ByteTuple3> filter(final Throwables.ByteTriPredicate<E> predicate) throws E {
            return predicate.test(_1, _2, _3) ? Optional.of(this) : Optional.<ByteTuple3> empty();
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
