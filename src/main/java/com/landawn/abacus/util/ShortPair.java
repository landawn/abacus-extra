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
import com.landawn.abacus.util.ShortTuple.ShortTuple2;
import com.landawn.abacus.util.stream.ShortStream;
import com.landawn.abacus.util.u.Optional;

public class ShortPair {
    public short left; // NOSONAR
    public short right; // NOSONAR

    public ShortPair() {
    }

    @SuppressFBWarnings({ "PA_PUBLIC_PRIMITIVE_ATTRIBUTE", "PA_PUBLIC_PRIMITIVE_ATTRIBUTE" })
    ShortPair(final short l, final short r) {
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
    public static ShortPair of(final short l, final short r) {
        return new ShortPair(l, r);
    }

    /**
     *
     *
     * @return
     */
    public short getLeft() {
        return left;
    }

    /**
     *
     *
     * @param left
     */
    public void setLeft(final short left) {
        this.left = left;
    }

    /**
     *
     *
     * @return
     */
    public short getRight() {
        return right;
    }

    /**
     *
     *
     * @param right
     */
    public void setRight(final short right) {
        this.right = right;
    }

    /**
     *
     *
     * @param left
     * @param right
     */
    public void set(final short left, final short right) {
        this.left = left;
        this.right = right;
    }

    /**
     *
     *
     * @param newLeft
     * @return
     */
    public short getAndSetLeft(final short newLeft) {
        final short res = left;
        left = newLeft;
        return res;
    }

    /**
     *
     *
     * @param newLeft
     * @return
     */
    public short setAndGetLeft(final short newLeft) {
        left = newLeft;
        return left;
    }

    /**
     *
     *
     * @param newRight
     * @return
     */
    public short getAndSetRight(final short newRight) {
        final short res = newRight;
        right = newRight;
        return res;
    }

    /**
     *
     *
     * @param newRight
     * @return
     */
    public short setAndGetRight(final short newRight) {
        right = newRight;
        return right;
    }

    /**
     *
     *
     * @return
     */
    @Beta
    public ShortPair reverse() {
        return new ShortPair(right, left);
    }

    /**
     *
     *
     * @return
     */
    public ShortPair copy() {
        return new ShortPair(left, right);
    }

    /**
     *
     *
     * @return
     */
    public short[] toArray() {
        return new short[] { left, right };
    }

    /**
     *
     *
     * @param <E>
     * @param comsumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.ShortConsumer<E> comsumer) throws E {
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
    public <E extends Exception> void accept(final Throwables.ShortBiConsumer<E> action) throws E {
        action.accept(left, right);
    }

    /**
     *
     *
     * @param <E>
     * @param action
     * @throws E
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super ShortPair, E> action) throws E {
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
    public <U, E extends Exception> U map(final Throwables.ShortBiFunction<U, E> mapper) throws E {
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
    public <U, E extends Exception> U map(final Throwables.Function<? super ShortPair, U, E> mapper) throws E {
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
    public <E extends Exception> Optional<ShortPair> filter(final Throwables.ShortBiPredicate<E> predicate) throws E {
        return predicate.test(left, right) ? Optional.of(this) : Optional.<ShortPair> empty();
    }

    /**
     *
     *
     * @param <E>
     * @param predicate
     * @return
     * @throws E
     */
    public <E extends Exception> Optional<ShortPair> filter(final Throwables.Predicate<? super ShortPair, E> predicate) throws E {
        return predicate.test(this) ? Optional.of(this) : Optional.<ShortPair> empty();
    }

    /**
     *
     *
     * @return
     */
    public ShortStream stream() {
        return ShortStream.of(left, right);
    }

    /**
     *
     *
     * @return
     */
    public Optional<ShortPair> toOptional() {
        return Optional.of(this);
    }

    /**
     *
     *
     * @return
     */
    public ShortTuple2 toTuple() {
        return ShortTuple.of(left, right);
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

        if (obj instanceof final ShortPair other) {
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
