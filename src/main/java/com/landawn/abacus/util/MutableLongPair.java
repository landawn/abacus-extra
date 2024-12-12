/*
 * Copyright (c) 2020, Haiyang Li.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.landawn.abacus.util;

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.annotation.SuppressFBWarnings;
import com.landawn.abacus.util.LongTuple.LongTuple2;
import com.landawn.abacus.util.stream.LongStream;
import com.landawn.abacus.util.u.Optional;

/**
 *
 * @deprecated Not a good idea?
 */
@Deprecated
@Beta
public class MutableLongPair implements Mutable {
    public long left; // NOSONAR
    public long right; // NOSONAR

    public MutableLongPair() {
    }

    @SuppressFBWarnings({ "PA_PUBLIC_PRIMITIVE_ATTRIBUTE", "PA_PUBLIC_PRIMITIVE_ATTRIBUTE" })
    MutableLongPair(final long l, final long r) {
        left = l;
        right = r;
    }

    /**
     *
     *
     * @param l
     * @param r
     * @return
     */
    public static MutableLongPair of(final long l, final long r) {
        return new MutableLongPair(l, r);
    }

    /**
     *
     *
     * @return
     */
    public long getLeft() {
        return left;
    }

    /**
     *
     *
     * @param left
     */
    public void setLeft(final long left) {
        this.left = left;
    }

    /**
     *
     *
     * @return
     */
    public long getRight() {
        return right;
    }

    /**
     *
     *
     * @param right
     */
    public void setRight(final long right) {
        this.right = right;
    }

    /**
     *
     *
     * @param left
     * @param right
     */
    public void set(final long left, final long right) {
        this.left = left;
        this.right = right;
    }

    /**
     *
     *
     * @param newLeft
     * @return
     */
    public long getAndSetLeft(final long newLeft) {
        final long res = left;
        left = newLeft;
        return res;
    }

    /**
     *
     *
     * @param newLeft
     * @return
     */
    public long setAndGetLeft(final long newLeft) {
        left = newLeft;
        return left;
    }

    /**
     *
     *
     * @param newRight
     * @return
     */
    public long getAndSetRight(final long newRight) {
        final long res = newRight;
        right = newRight;
        return res;
    }

    /**
     *
     *
     * @param newRight
     * @return
     */
    public long setAndGetRight(final long newRight) {
        right = newRight;
        return right;
    }

    /**
     *
     *
     * @return
     */
    @Beta
    public MutableLongPair reverse() {
        return new MutableLongPair(right, left);
    }

    /**
     *
     *
     * @return
     */
    public MutableLongPair copy() {
        return new MutableLongPair(left, right);
    }

    /**
     *
     *
     * @return
     */
    public long[] toArray() {
        return new long[] { left, right };
    }

    /**
     *
     *
     * @param <E>
     * @param consumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.LongConsumer<E> consumer) throws E {
        consumer.accept(left);
        consumer.accept(right);
    }

    /**
     *
     *
     * @param <E>
     * @param action
     * @throws E
     */
    public <E extends Exception> void accept(final Throwables.LongBiConsumer<E> action) throws E {
        action.accept(left, right);
    }

    /**
     *
     *
     * @param <E>
     * @param action
     * @throws E
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super MutableLongPair, E> action) throws E {
        action.accept(this);
    }

    /**
     *
     *
     * @param <U>
     * @param <E>
     * @param mapper
     * @return
     * @throws E
     */
    public <U, E extends Exception> U map(final Throwables.LongBiFunction<U, E> mapper) throws E {
        return mapper.apply(left, right);
    }

    /**
     *
     *
     * @param <U>
     * @param <E>
     * @param mapper
     * @return
     * @throws E
     */
    public <U, E extends Exception> U map(final Throwables.Function<? super MutableLongPair, U, E> mapper) throws E {
        return mapper.apply(this);
    }

    /**
     *
     *
     * @param <E>
     * @param predicate
     * @return
     * @throws E
     */
    public <E extends Exception> Optional<MutableLongPair> filter(final Throwables.LongBiPredicate<E> predicate) throws E {
        return predicate.test(left, right) ? Optional.of(this) : Optional.<MutableLongPair> empty();
    }

    /**
     *
     *
     * @param <E>
     * @param predicate
     * @return
     * @throws E
     */
    public <E extends Exception> Optional<MutableLongPair> filter(final Throwables.Predicate<? super MutableLongPair, E> predicate) throws E {
        return predicate.test(this) ? Optional.of(this) : Optional.<MutableLongPair> empty();
    }

    /**
     *
     *
     * @return
     */
    public LongStream stream() {
        return LongStream.of(left, right);
    }

    /**
     *
     *
     * @return
     */
    public Optional<MutableLongPair> toOptional() {
        return Optional.of(this);
    }

    /**
     *
     *
     * @return
     */
    public LongTuple2 toTuple() {
        return LongTuple.of(left, right);
    }

    /**
     *
     *
     * @return
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + N.hashCode(left);
        return prime * result + N.hashCode(right);
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
        }

        if (obj instanceof final MutableLongPair other) {
            return N.equals(left, other.left) && N.equals(right, other.right);
        }

        return false;
    }

    /**
     *
     *
     * @return
     */
    @Override
    public String toString() {
        return "[" + N.toString(left) + ", " + N.toString(right) + "]";
    }
}
