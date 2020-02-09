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
import com.landawn.abacus.util.stream.LongStream;

public abstract class LongTuple<TP> {
    protected transient long[] elements;

    public static LongTuple1 of(long _1) {
        return new LongTuple1(_1);
    }

    public static LongTuple2 of(long _1, long _2) {
        return new LongTuple2(_1, _2);
    }

    public static LongTuple3 of(long _1, long _2, long _3) {
        return new LongTuple3(_1, _2, _3);
    }

    public static LongTuple4 of(long _1, long _2, long _3, long _4) {
        return new LongTuple4(_1, _2, _3, _4);
    }

    public static LongTuple5 of(long _1, long _2, long _3, long _4, long _5) {
        return new LongTuple5(_1, _2, _3, _4, _5);
    }

    public static LongTuple6 of(long _1, long _2, long _3, long _4, long _5, long _6) {
        return new LongTuple6(_1, _2, _3, _4, _5, _6);
    }

    public static LongTuple7 of(long _1, long _2, long _3, long _4, long _5, long _6, long _7) {
        return new LongTuple7(_1, _2, _3, _4, _5, _6, _7);
    }

    public static LongTuple8 of(long _1, long _2, long _3, long _4, long _5, long _6, long _7, long _8) {
        return new LongTuple8(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    public static LongTuple9 of(long _1, long _2, long _3, long _4, long _5, long _6, long _7, long _8, long _9) {
        return new LongTuple9(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    public abstract int arity();

    public long min() {
        return N.min(elements());
    }

    public long max() {
        return N.max(elements());
    }

    public long median() {
        return N.median(elements());
    }

    public long sum() {
        return N.sum(elements());
    }

    public double average() {
        return N.average(elements());
    }

    public abstract TP reverse();

    public abstract boolean contains(long elementToFind);

    public long[] toArray() {
        return elements().clone();
    }

    public LongList toList() {
        return LongList.of(elements().clone());
    }

    public <E extends Exception> void forEach(Throwables.LongConsumer<E> comsumer) throws E {
        for (long e : elements()) {
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

    public LongStream stream() {
        return LongStream.of(elements());
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
            return N.equals(elements(), ((LongTuple<TP>) obj).elements());
        }
    }

    @Override
    public String toString() {
        return N.toString(elements());
    }

    protected abstract long[] elements();

    public static final class LongTuple1 extends LongTuple<LongTuple1> {

        public final long _1;

        LongTuple1() {
            this(0);
        }

        LongTuple1(long _1) {
            this._1 = _1;
        }

        @Override
        public int arity() {
            return 1;
        }

        @Override
        public long min() {
            return _1;
        }

        @Override
        public long max() {
            return _1;
        }

        @Override
        public long median() {
            return _1;
        }

        @Override
        public long sum() {
            return _1;
        }

        @Override
        public double average() {
            return _1;
        }

        @Override
        public LongTuple1 reverse() {
            return new LongTuple1(_1);
        }

        @Override
        public boolean contains(final long elementToFind) {
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
            } else if (!(obj instanceof LongTuple1)) {
                return false;
            } else {
                LongTuple1 other = (LongTuple1) obj;
                return this._1 == other._1;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + "]";
        }

        @Override
        protected long[] elements() {
            if (this.elements == null) {
                elements = new long[] { _1 };
            }

            return elements;
        }
    }

    public static final class LongTuple2 extends LongTuple<LongTuple2> {

        public final long _1;
        public final long _2;

        LongTuple2() {
            this(0, 0);
        }

        LongTuple2(long _1, long _2) {
            this._1 = _1;
            this._2 = _2;
        }

        @Override
        public int arity() {
            return 2;
        }

        @Override
        public long min() {
            return N.min(_1, _2);
        }

        @Override
        public long max() {
            return N.max(_1, _2);
        }

        @Override
        public long median() {
            return N.median(_1, _2);
        }

        @Override
        public long sum() {
            return _1 + _2;
        }

        @Override
        public LongTuple2 reverse() {
            return new LongTuple2(_2, _1);
        }

        @Override
        public boolean contains(final long elementToFind) {
            return _1 == elementToFind || _2 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.LongConsumer<E> comsumer) throws E {
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
            } else if (!(obj instanceof LongTuple2)) {
                return false;
            } else {
                LongTuple2 other = (LongTuple2) obj;
                return this._1 == other._1 && this._2 == other._2;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + "]";
        }

        @Override
        protected long[] elements() {
            if (this.elements == null) {
                elements = new long[] { _1, _2 };
            }

            return elements;
        }
    }

    public static final class LongTuple3 extends LongTuple<LongTuple3> {

        public final long _1;
        public final long _2;
        public final long _3;

        LongTuple3() {
            this(0, 0, 0);
        }

        LongTuple3(long _1, long _2, long _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        @Override
        public int arity() {
            return 3;
        }

        @Override
        public long min() {
            return N.min(_1, _2, _3);
        }

        @Override
        public long max() {
            return N.max(_1, _2, _3);
        }

        @Override
        public long median() {
            return N.median(_1, _2, _3);
        }

        @Override
        public long sum() {
            return _1 + _2 + _3;
        }

        @Override
        public LongTuple3 reverse() {
            return new LongTuple3(_3, _2, _1);
        }

        @Override
        public boolean contains(final long elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind;
        }

        @Override
        public <E extends Exception> void forEach(Throwables.LongConsumer<E> comsumer) throws E {
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
            } else if (!(obj instanceof LongTuple3)) {
                return false;
            } else {
                LongTuple3 other = (LongTuple3) obj;
                return this._1 == other._1 && this._2 == other._2 && this._3 == other._3;
            }
        }

        @Override
        public String toString() {
            return "[" + _1 + ", " + _2 + ", " + _3 + "]";
        }

        @Override
        protected long[] elements() {
            if (this.elements == null) {
                elements = new long[] { _1, _2, _3 };
            }

            return elements;
        }
    }

    public static final class LongTuple4 extends LongTuple<LongTuple4> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;

        LongTuple4() {
            this(0, 0, 0, 0);
        }

        LongTuple4(long _1, long _2, long _3, long _4) {
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
        public LongTuple4 reverse() {
            return new LongTuple4(_4, _3, _2, _1);
        }

        @Override
        public boolean contains(final long elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4 };
            }

            return elements;
        }
    }

    public static final class LongTuple5 extends LongTuple<LongTuple5> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;
        public final long _5;

        LongTuple5() {
            this(0, 0, 0, 0, 0);
        }

        LongTuple5(long _1, long _2, long _3, long _4, long _5) {
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
        public LongTuple5 reverse() {
            return new LongTuple5(_5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final long elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5 };
            }

            return elements;
        }
    }

    public static final class LongTuple6 extends LongTuple<LongTuple6> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;
        public final long _5;
        public final long _6;

        LongTuple6() {
            this(0, 0, 0, 0, 0, 0);
        }

        LongTuple6(long _1, long _2, long _3, long _4, long _5, long _6) {
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
        public LongTuple6 reverse() {
            return new LongTuple6(_6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final long elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6 };
            }

            return elements;
        }
    }

    public static final class LongTuple7 extends LongTuple<LongTuple7> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;
        public final long _5;
        public final long _6;
        public final long _7;

        LongTuple7() {
            this(0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple7(long _1, long _2, long _3, long _4, long _5, long _6, long _7) {
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
        public LongTuple7 reverse() {
            return new LongTuple7(_7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final long elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7 };
            }

            return elements;
        }
    }

    public static final class LongTuple8 extends LongTuple<LongTuple8> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;
        public final long _5;
        public final long _6;
        public final long _7;
        public final long _8;

        LongTuple8() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple8(long _1, long _2, long _3, long _4, long _5, long _6, long _7, long _8) {
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
        public LongTuple8 reverse() {
            return new LongTuple8(_8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final long elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7, _8 };
            }

            return elements;
        }
    }

    public static final class LongTuple9 extends LongTuple<LongTuple9> {

        public final long _1;
        public final long _2;
        public final long _3;
        public final long _4;
        public final long _5;
        public final long _6;
        public final long _7;
        public final long _8;
        public final long _9;

        LongTuple9() {
            this(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        LongTuple9(long _1, long _2, long _3, long _4, long _5, long _6, long _7, long _8, long _9) {
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
        public LongTuple9 reverse() {
            return new LongTuple9(_9, _8, _7, _6, _5, _4, _3, _2, _1);
        }

        @Override
        public boolean contains(final long elementToFind) {
            return _1 == elementToFind || _2 == elementToFind || _3 == elementToFind || _4 == elementToFind || _5 == elementToFind || _6 == elementToFind
                    || _7 == elementToFind || _8 == elementToFind || _9 == elementToFind;
        }

        @Override
        protected long[] elements() {
            if (elements == null) {
                elements = new long[] { _1, _2, _3, _4, _5, _6, _7, _8, _9 };
            }

            return elements;
        }
    }

}
