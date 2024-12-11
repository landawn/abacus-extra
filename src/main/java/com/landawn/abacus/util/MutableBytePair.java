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
import com.landawn.abacus.util.ByteTuple.ByteTuple2;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.ByteStream;

/**
 *
 * @deprecated Not a good idea?
 */
@Deprecated
@Beta
public class MutableBytePair implements Mutable {
    public byte left; // NOSONAR
    public byte right; // NOSONAR

    public MutableBytePair() {
    }

    @SuppressFBWarnings({"PA_PUBLIC_PRIMITIVE_ATTRIBUTE", "PA_PUBLIC_PRIMITIVE_ATTRIBUTE"})
    MutableBytePair(final byte l, final byte r) {
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
    public static MutableBytePair of(final byte l, final byte r) {
        return new MutableBytePair(l, r);
    }

    /**
     *
     *
     * @return
     */
    public byte getLeft() {
        return left;
    }

    /**
     *
     *
     * @param left
     */
    public void setLeft(final byte left) {
        this.left = left;
    }

    /**
     *
     *
     * @return
     */
    public byte getRight() {
        return right;
    }

    /**
     *
     *
     * @param right
     */
    public void setRight(final byte right) {
        this.right = right;
    }

    /**
     *
     *
     * @param left
     * @param right
     */
    public void set(final byte left, final byte right) {
        this.left = left;
        this.right = right;
    }

    /**
     *
     *
     * @param newLeft
     * @return
     */
    public byte getAndSetLeft(final byte newLeft) {
        final byte res = left;
        left = newLeft;
        return res;
    }

    /**
     *
     *
     * @param newLeft
     * @return
     */
    public byte setAndGetLeft(final byte newLeft) {
        left = newLeft;
        return left;
    }

    /**
     *
     *
     * @param newRight
     * @return
     */
    public byte getAndSetRight(final byte newRight) {
        final byte res = newRight;
        right = newRight;
        return res;
    }

    /**
     *
     *
     * @param newRight
     * @return
     */
    public byte setAndGetRight(final byte newRight) {
        right = newRight;
        return right;
    }

    /**
     *
     *
     * @return
     */
    @Beta
    public MutableBytePair reverse() {
        return new MutableBytePair(right, left);
    }

    /**
     *
     *
     * @return
     */
    public MutableBytePair copy() {
        return new MutableBytePair(left, right);
    }

    /**
     *
     *
     * @return
     */
    public byte[] toArray() {
        return new byte[] { left, right };
    }

    /**
     *
     *
     * @param <E>
     * @param comsumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.ByteConsumer<E> comsumer) throws E {
        comsumer.accept(left);
        comsumer.accept(right);
    }

    /**
     *
     *
     * @param <E>
     * @param action
     * @throws E
     */
    public <E extends Exception> void accept(final Throwables.ByteBiConsumer<E> action) throws E {
        action.accept(left, right);
    }

    /**
     *
     *
     * @param <E>
     * @param action
     * @throws E
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super MutableBytePair, E> action) throws E {
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
    public <U, E extends Exception> U map(final Throwables.ByteBiFunction<U, E> mapper) throws E {
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
    public <U, E extends Exception> U map(final Throwables.Function<? super MutableBytePair, U, E> mapper) throws E {
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
    public <E extends Exception> Optional<MutableBytePair> filter(final Throwables.ByteBiPredicate<E> predicate) throws E {
        return predicate.test(left, right) ? Optional.of(this) : Optional.<MutableBytePair> empty();
    }

    /**
     *
     *
     * @param <E>
     * @param predicate
     * @return
     * @throws E
     */
    public <E extends Exception> Optional<MutableBytePair> filter(final Throwables.Predicate<? super MutableBytePair, E> predicate) throws E {
        return predicate.test(this) ? Optional.of(this) : Optional.<MutableBytePair> empty();
    }

    /**
     *
     *
     * @return
     */
    public ByteStream stream() {
        return ByteStream.of(left, right);
    }

    /**
     *
     *
     * @return
     */
    public Optional<MutableBytePair> toOptional() {
        return Optional.of(this);
    }

    /**
     *
     *
     * @return
     */
    public ByteTuple2 toTuple() {
        return ByteTuple.of(left, right);
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

        if (obj instanceof final MutableBytePair other) {
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
