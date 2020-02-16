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
import com.landawn.abacus.util.ShortTuple.ShortTuple2;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.ShortStream;

public class ShortPair {
    public short left;

    public short right;

    public ShortPair() {
    }

    ShortPair(final short l, final short r) {
        this.left = l;
        this.right = r;
    }

    public static <L, R> ShortPair of(final short l, final short r) {
        return new ShortPair(l, r);
    }

    public short getLeft() {
        return left;
    }

    public void setLeft(final short left) {
        this.left = left;
    }

    public short getRight() {
        return right;
    }

    public void setRight(final short right) {
        this.right = right;
    }

    public void set(final short left, final short right) {
        this.left = left;
        this.right = right;
    }

    public short getAndSetLeft(final short newLeft) {
        final short res = left;
        left = newLeft;
        return res;
    }

    public short setAndGetLeft(final short newLeft) {
        left = newLeft;
        return left;
    }

    public short getAndSetRight(final short newRight) {
        final short res = newRight;
        right = newRight;
        return res;
    }

    public short setAndGetRight(final short newRight) {
        right = newRight;
        return right;
    }

    @Beta
    public ShortPair reverse() {
        return new ShortPair(this.right, this.left);
    }

    public ShortPair copy() {
        return new ShortPair(this.left, this.right);
    }

    public short[] toArray() {
        return new short[] { left, right };
    }

    public <E extends Exception> void forEach(Throwables.ShortConsumer<E> comsumer) throws E {
        comsumer.accept(left);
        comsumer.accept(right);
    }

    public <E extends Exception> void accept(final Throwables.ShortBiConsumer<E> action) throws E {
        action.accept(left, right);
    }

    public <E extends Exception> void accept(final Throwables.Consumer<? super ShortPair, E> action) throws E {
        action.accept(this);
    }

    public <U, E extends Exception> U map(final Throwables.ShortBiFunction<U, E> mapper) throws E {
        return mapper.apply(left, right);
    }

    public <U, E extends Exception> U map(final Throwables.Function<? super ShortPair, U, E> mapper) throws E {
        return mapper.apply(this);
    }

    public <E extends Exception> Optional<ShortPair> filter(final Throwables.ShortBiPredicate<E> predicate) throws E {
        return predicate.test(left, right) ? Optional.of(this) : Optional.<ShortPair> empty();
    }

    public <E extends Exception> Optional<ShortPair> filter(final Throwables.Predicate<? super ShortPair, E> predicate) throws E {
        return predicate.test(this) ? Optional.of(this) : Optional.<ShortPair> empty();
    }

    public ShortStream stream() {
        return ShortStream.of(left, right);
    }

    public Optional<ShortPair> toOptional() {
        return Optional.of(this);
    }

    public ShortTuple2 toTuple() {
        return ShortTuple.of(left, right);
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

        if (obj instanceof ShortPair) {
            final ShortPair other = (ShortPair) obj;

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
