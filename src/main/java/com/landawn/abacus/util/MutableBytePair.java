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
import com.landawn.abacus.util.ByteTuple.ByteTuple2;
import com.landawn.abacus.util.u.Optional;
import com.landawn.abacus.util.stream.ByteStream;

public class MutableBytePair {
    public byte left;
    public byte right;

    public MutableBytePair() {
    }

    MutableBytePair(final byte l, final byte r) {
        this.left = l;
        this.right = r;
    }

    public static <L, R> MutableBytePair of(final byte l, final byte r) {
        return new MutableBytePair(l, r);
    }

    public byte getLeft() {
        return left;
    }

    public void setLeft(final byte left) {
        this.left = left;
    }

    public byte getRight() {
        return right;
    }

    public void setRight(final byte right) {
        this.right = right;
    }

    public void set(final byte left, final byte right) {
        this.left = left;
        this.right = right;
    }

    public byte getAndSetLeft(final byte newLeft) {
        final byte res = left;
        left = newLeft;
        return res;
    }

    public byte setAndGetLeft(final byte newLeft) {
        left = newLeft;
        return left;
    }

    public byte getAndSetRight(final byte newRight) {
        final byte res = newRight;
        right = newRight;
        return res;
    }

    public byte setAndGetRight(final byte newRight) {
        right = newRight;
        return right;
    }

    @Beta
    public MutableBytePair reverse() {
        return new MutableBytePair(this.right, this.left);
    }

    public MutableBytePair copy() {
        return new MutableBytePair(this.left, this.right);
    }

    public byte[] toArray() {
        return new byte[] { left, right };
    }
    
    public <E extends Exception> void forEach(Throwables.ByteConsumer<E> comsumer) throws E {
        comsumer.accept(left);
        comsumer.accept(right);
    }

    public <E extends Exception> void accept(final Throwables.ByteBiConsumer<E> action) throws E {
        action.accept(left, right);
    }

    public <E extends Exception> void accept(final Throwables.Consumer<? super MutableBytePair, E> action) throws E {
        action.accept(this);
    }

    public <U, E extends Exception> U map(final Throwables.ByteBiFunction<U, E> mapper) throws E {
        return mapper.apply(left, right);
    }

    public <U, E extends Exception> U map(final Throwables.Function<? super MutableBytePair, U, E> mapper) throws E {
        return mapper.apply(this);
    }

    public <E extends Exception> Optional<MutableBytePair> filter(final Throwables.ByteBiPredicate<E> predicate) throws E {
        return predicate.test(left, right) ? Optional.of(this) : Optional.<MutableBytePair> empty();
    }

    public <E extends Exception> Optional<MutableBytePair> filter(final Throwables.Predicate<? super MutableBytePair, E> predicate) throws E {
        return predicate.test(this) ? Optional.of(this) : Optional.<MutableBytePair> empty();
    }

    public ByteStream stream() {
        return ByteStream.of(left, right);
    }

    public Optional<MutableBytePair> toOptional() {
        return Optional.of(this);
    }

    public ByteTuple2 toTuple() {
        return ByteTuple.of(left, right);
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

        if (obj instanceof MutableBytePair) {
            final MutableBytePair other = (MutableBytePair) obj;

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
