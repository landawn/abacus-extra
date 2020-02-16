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

public class MutableIntPair {
    public int left;
    public int right;

    public MutableIntPair() {
    }

    MutableIntPair(final int l, final int r) {
        this.left = l;
        this.right = r;
    }

    public static <L, R> MutableIntPair of(final int l, final int r) {
        return new MutableIntPair(l, r);
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(final int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(final int right) {
        this.right = right;
    }

    public void set(final int left, final int right) {
        this.left = left;
        this.right = right;
    }

    public int getAndSetLeft(final int newLeft) {
        final int res = left;
        left = newLeft;
        return res;
    }

    public int setAndGetLeft(final int newLeft) {
        left = newLeft;
        return left;
    }

    public int getAndSetRight(final int newRight) {
        final int res = newRight;
        right = newRight;
        return res;
    }

    public int setAndGetRight(final int newRight) {
        right = newRight;
        return right;
    }

    @Beta
    public MutableIntPair reverse() {
        return new MutableIntPair(this.right, this.left);
    }

    public MutableIntPair copy() {
        return new MutableIntPair(this.left, this.right);
    }

    public int[] toArray() {
        return new int[] { left, right };
    }

    public <E extends Exception> void forEach(Throwables.IntConsumer<E> comsumer) throws E {
        comsumer.accept(left);
        comsumer.accept(right);
    }

    public <E extends Exception> void accept(final Throwables.IntBiConsumer<E> action) throws E {
        action.accept(left, right);
    }

    public <E extends Exception> void accept(final Throwables.Consumer<? super MutableIntPair, E> action) throws E {
        action.accept(this);
    }

    public <U, E extends Exception> U map(final Throwables.IntBiFunction<U, E> mapper) throws E {
        return mapper.apply(left, right);
    }

    public <U, E extends Exception> U map(final Throwables.Function<? super MutableIntPair, U, E> mapper) throws E {
        return mapper.apply(this);
    }

    public <E extends Exception> Optional<MutableIntPair> filter(final Throwables.IntBiPredicate<E> predicate) throws E {
        return predicate.test(left, right) ? Optional.of(this) : Optional.<MutableIntPair> empty();
    }

    public <E extends Exception> Optional<MutableIntPair> filter(final Throwables.Predicate<? super MutableIntPair, E> predicate) throws E {
        return predicate.test(this) ? Optional.of(this) : Optional.<MutableIntPair> empty();
    }

    public IntStream stream() {
        return IntStream.of(left, right);
    }

    public Optional<MutableIntPair> toOptional() {
        return Optional.of(this);
    }

    public IntTuple2 toTuple() {
        return IntTuple.of(left, right);
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

        if (obj instanceof MutableIntPair) {
            final MutableIntPair other = (MutableIntPair) obj;

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
