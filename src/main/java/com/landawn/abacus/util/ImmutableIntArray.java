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

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.util.stream.IntStream;

@Beta
public final class ImmutableIntArray implements Immutable {
    private final int[] elements;

    public final int length;

    ImmutableIntArray(final int[] a) {
        elements = a == null ? N.EMPTY_INT_ARRAY : a;
        length = elements.length;
    }

    public static ImmutableIntArray of(final int[] a) {
        return new ImmutableIntArray(a);
    }

    public static ImmutableIntArray copyOf(final int[] a) {
        return new ImmutableIntArray(a == null ? N.EMPTY_INT_ARRAY : a.clone());
    }

    public int get(int index) {
        return elements[index];
    }

    public <E extends Exception> void forEach(final Throwables.IntConsumer<E> action) throws E {
        N.checkArgNotNull(action);

        for (int i = 0; i < length; i++) {
            action.accept(elements[i]);
        }
    }

    public <E extends Exception> void forEachIndexed(final Throwables.IndexedIntConsumer<E> action) throws E {
        N.checkArgNotNull(action);

        for (int i = 0; i < length; i++) {
            action.accept(i, elements[i]);
        }
    }

    public IntStream stream() {
        return IntStream.of(elements);
    }

    public ImmutableIntArray copy(final int fromIndex, final int toIndex) {
        N.checkFromToIndex(fromIndex, toIndex, length);

        return ImmutableIntArray.of(N.copyOfRange(elements, fromIndex, toIndex));
    }

    public int[] copyToArray(final int fromIndex, final int toIndex) {
        N.checkFromToIndex(fromIndex, toIndex, length);

        return N.copyOfRange(elements, fromIndex, toIndex);
    }

    @Override
    public int hashCode() {
        return N.hashCode(elements);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ImmutableIntArray && N.equals(this.elements, ((ImmutableIntArray) obj).elements);
    }

    @Override
    public String toString() {
        return N.toString(elements);
    }
}
