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
import com.landawn.abacus.util.DoubleTuple.DoubleTuple2;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.DoubleStream;

public class MutableDoublePair implements Mutable {
    public double left;
    public double right;

    public MutableDoublePair() {
    }

    MutableDoublePair(final double l, final double r) {
        this.left = l;
        this.right = r;
    }

    public static <L, R> MutableDoublePair of(final double l, final double r) {
        return new MutableDoublePair(l, r);
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(final double left) {
        this.left = left;
    }

    public double getRight() {
        return right;
    }

    public void setRight(final double right) {
        this.right = right;
    }

    public void set(final double left, final double right) {
        this.left = left;
        this.right = right;
    }

    public double getAndSetLeft(final double newLeft) {
        final double res = left;
        left = newLeft;
        return res;
    }

    public double setAndGetLeft(final double newLeft) {
        left = newLeft;
        return left;
    }

    public double getAndSetRight(final double newRight) {
        final double res = newRight;
        right = newRight;
        return res;
    }

    public double setAndGetRight(final double newRight) {
        right = newRight;
        return right;
    }

    @Beta
    public MutableDoublePair reverse() {
        return new MutableDoublePair(this.right, this.left);
    }

    public MutableDoublePair copy() {
        return new MutableDoublePair(this.left, this.right);
    }

    public double[] toArray() {
        return new double[] { left, right };
    }

    public <E extends Exception> void forEach(Throwables.DoubleConsumer<E> comsumer) throws E {
        comsumer.accept(left);
        comsumer.accept(right);
    }

    public <E extends Exception> void accept(final Throwables.DoubleBiConsumer<E> action) throws E {
        action.accept(left, right);
    }

    public <E extends Exception> void accept(final Throwables.Consumer<? super MutableDoublePair, E> action) throws E {
        action.accept(this);
    }

    public <U, E extends Exception> U map(final Throwables.DoubleBiFunction<U, E> mapper) throws E {
        return mapper.apply(left, right);
    }

    public <U, E extends Exception> U map(final Throwables.Function<? super MutableDoublePair, U, E> mapper) throws E {
        return mapper.apply(this);
    }

    public <E extends Exception> Optional<MutableDoublePair> filter(final Throwables.DoubleBiPredicate<E> predicate) throws E {
        return predicate.test(left, right) ? Optional.of(this) : Optional.<MutableDoublePair> empty();
    }

    public <E extends Exception> Optional<MutableDoublePair> filter(final Throwables.Predicate<? super MutableDoublePair, E> predicate) throws E {
        return predicate.test(this) ? Optional.of(this) : Optional.<MutableDoublePair> empty();
    }

    public DoubleStream stream() {
        return DoubleStream.of(left, right);
    }

    public Optional<MutableDoublePair> toOptional() {
        return Optional.of(this);
    }

    public DoubleTuple2 toTuple() {
        return DoubleTuple.of(left, right);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + N.hashCode(left);
        result = prime * result + N.hashCode(right);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof MutableDoublePair) {
            final MutableDoublePair other = (MutableDoublePair) obj;

            return N.equals(left, other.left) && N.equals(right, other.right);
        }

        return false;
    }

    @Override
    public String toString() {
        return "[" + N.toString(left) + ", " + N.toString(right) + "]";
        // return N.toString(left) + "=" + N.toString(right);
    }
}
