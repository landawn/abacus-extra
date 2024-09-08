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
import com.landawn.abacus.util.IntTuple.IntTuple2;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.IntStream;

/**
 *
 * @deprecated Not a good idea?
 */
@Deprecated
@Beta
public class MutableIntPair implements Mutable {
    public int left; // NOSONAR
    public int right; // NOSONAR

    /**
     *
     */
    public MutableIntPair() {
    }

    MutableIntPair(final int l, final int r) {
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
    public static MutableIntPair of(final int l, final int r) {
        return new MutableIntPair(l, r);
    }

    /**
     *
     *
     * @return
     */
    public int getLeft() {
        return left;
    }

    /**
     *
     *
     * @param left
     */
    public void setLeft(final int left) {
        this.left = left;
    }

    /**
     *
     *
     * @return
     */
    public int getRight() {
        return right;
    }

    /**
     *
     *
     * @param right
     */
    public void setRight(final int right) {
        this.right = right;
    }

    /**
     *
     *
     * @param left
     * @param right
     */
    public void set(final int left, final int right) {
        this.left = left;
        this.right = right;
    }

    /**
     *
     *
     * @param newLeft
     * @return
     */
    public int getAndSetLeft(final int newLeft) {
        final int res = left;
        left = newLeft;
        return res;
    }

    /**
     *
     *
     * @param newLeft
     * @return
     */
    public int setAndGetLeft(final int newLeft) {
        left = newLeft;
        return left;
    }

    /**
     *
     *
     * @param newRight
     * @return
     */
    public int getAndSetRight(final int newRight) {
        final int res = newRight;
        right = newRight;
        return res;
    }

    /**
     *
     *
     * @param newRight
     * @return
     */
    public int setAndGetRight(final int newRight) {
        right = newRight;
        return right;
    }

    /**
     *
     *
     * @return
     */
    @Beta
    public MutableIntPair reverse() {
        return new MutableIntPair(right, left);
    }

    /**
     *
     *
     * @return
     */
    public MutableIntPair copy() {
        return new MutableIntPair(left, right);
    }

    /**
     *
     *
     * @return
     */
    public int[] toArray() {
        return new int[] { left, right };
    }

    /**
     *
     *
     * @param <E>
     * @param comsumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.IntConsumer<E> comsumer) throws E {
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
    public <E extends Exception> void accept(final Throwables.IntBiConsumer<E> action) throws E {
        action.accept(left, right);
    }

    /**
     *
     *
     * @param <E>
     * @param action
     * @throws E
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super MutableIntPair, E> action) throws E {
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
    public <U, E extends Exception> U map(final Throwables.IntBiFunction<U, E> mapper) throws E {
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
    public <U, E extends Exception> U map(final Throwables.Function<? super MutableIntPair, U, E> mapper) throws E {
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
    public <E extends Exception> Optional<MutableIntPair> filter(final Throwables.IntBiPredicate<E> predicate) throws E {
        return predicate.test(left, right) ? Optional.of(this) : Optional.<MutableIntPair> empty();
    }

    /**
     *
     *
     * @param <E>
     * @param predicate
     * @return
     * @throws E
     */
    public <E extends Exception> Optional<MutableIntPair> filter(final Throwables.Predicate<? super MutableIntPair, E> predicate) throws E {
        return predicate.test(this) ? Optional.of(this) : Optional.<MutableIntPair> empty();
    }

    /**
     *
     *
     * @return
     */
    public IntStream stream() {
        return IntStream.of(left, right);
    }

    /**
     *
     *
     * @return
     */
    public Optional<MutableIntPair> toOptional() {
        return Optional.of(this);
    }

    /**
     *
     *
     * @return
     */
    public IntTuple2 toTuple() {
        return IntTuple.of(left, right);
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

        if (obj instanceof final MutableIntPair other) {
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
