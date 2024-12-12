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
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.stream.DoubleStream;
import com.landawn.abacus.util.u.Optional;

/**
 *
 * @deprecated Not a good idea?
 */
@Deprecated
@Beta
public class MutableDoublePair implements Mutable {
    public double left; // NOSONAR
    public double right; // NOSONAR

    public MutableDoublePair() {
    }

    @SuppressFBWarnings({ "PA_PUBLIC_PRIMITIVE_ATTRIBUTE", "PA_PUBLIC_PRIMITIVE_ATTRIBUTE" })
    MutableDoublePair(final double l, final double r) {
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
    public static MutableDoublePair of(final double l, final double r) {
        return new MutableDoublePair(l, r);
    }

    /**
     *
     *
     * @return
     */
    public double getLeft() {
        return left;
    }

    /**
     *
     *
     * @param left
     */
    public void setLeft(final double left) {
        this.left = left;
    }

    /**
     *
     *
     * @return
     */
    public double getRight() {
        return right;
    }

    /**
     *
     *
     * @param right
     */
    public void setRight(final double right) {
        this.right = right;
    }

    /**
     *
     *
     * @param left
     * @param right
     */
    public void set(final double left, final double right) {
        this.left = left;
        this.right = right;
    }

    /**
     *
     *
     * @param newLeft
     * @return
     */
    public double getAndSetLeft(final double newLeft) {
        final double res = left;
        left = newLeft;
        return res;
    }

    /**
     *
     *
     * @param newLeft
     * @return
     */
    public double setAndGetLeft(final double newLeft) {
        left = newLeft;
        return left;
    }

    /**
     *
     *
     * @param newRight
     * @return
     */
    public double getAndSetRight(final double newRight) {
        final double res = newRight;
        right = newRight;
        return res;
    }

    /**
     *
     *
     * @param newRight
     * @return
     */
    public double setAndGetRight(final double newRight) {
        right = newRight;
        return right;
    }

    /**
     *
     *
     * @return
     */
    @Beta
    public MutableDoublePair reverse() {
        return new MutableDoublePair(right, left);
    }

    /**
     *
     *
     * @return
     */
    public MutableDoublePair copy() {
        return new MutableDoublePair(left, right);
    }

    /**
     *
     *
     * @return
     */
    public double[] toArray() {
        return new double[] { left, right };
    }

    /**
     *
     *
     * @param <E>
     * @param comsumer
     * @throws E
     */
    public <E extends Exception> void forEach(final Throwables.DoubleConsumer<E> comsumer) throws E {
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
    public <E extends Exception> void accept(final Throwables.DoubleBiConsumer<E> action) throws E {
        action.accept(left, right);
    }

    /**
     *
     *
     * @param <E>
     * @param action
     * @throws E
     */
    public <E extends Exception> void accept(final Throwables.Consumer<? super MutableDoublePair, E> action) throws E {
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
    public <U, E extends Exception> U map(final Throwables.DoubleBiFunction<U, E> mapper) throws E {
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
    public <U, E extends Exception> U map(final Throwables.Function<? super MutableDoublePair, U, E> mapper) throws E {
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
    public <E extends Exception> Optional<MutableDoublePair> filter(final Throwables.DoubleBiPredicate<E> predicate) throws E {
        return predicate.test(left, right) ? Optional.of(this) : Optional.<MutableDoublePair> empty();
    }

    /**
     *
     *
     * @param <E>
     * @param predicate
     * @return
     * @throws E
     */
    public <E extends Exception> Optional<MutableDoublePair> filter(final Throwables.Predicate<? super MutableDoublePair, E> predicate) throws E {
        return predicate.test(this) ? Optional.of(this) : Optional.<MutableDoublePair> empty();
    }

    /**
     *
     *
     * @return
     */
    public DoubleStream stream() {
        return DoubleStream.of(left, right);
    }

    /**
     *
     *
     * @return
     */
    public Optional<MutableDoublePair> toOptional() {
        return Optional.of(this);
    }

    /**
     *
     *
     * @return
     */
    public DoubleTuple2 toTuple() {
        return DoubleTuple.of(left, right);
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

        if (obj instanceof final MutableDoublePair other) {
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
