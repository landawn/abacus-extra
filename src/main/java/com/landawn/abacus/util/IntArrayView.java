/*
 * Copyright (C) 2026 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.landawn.abacus.util;

import java.util.Objects;

import com.landawn.abacus.annotation.Beta;
import com.landawn.abacus.util.stream.IntStream;

/**
 * A read-only, zero-copy view of a caller-owned {@code int[]}.
 *
 * <p>The view retains the supplied array. Changes made through any retained reference to that array
 * are immediately observable through this view, including through streams created after the change.
 * The view is therefore not an immutable value and is not thread-safe while the backing array is
 * being modified.</p>
 *
 * <p>This class intentionally retains identity-based {@link #equals(Object)} and {@link #hashCode()}
 * semantics. Content-based hashing would be unsafe because the viewed contents can change while an
 * instance is stored in a hash-based collection.</p>
 *
 * @see ImmutableIntArray#copyOf(int[])
 */
@Beta
public final class IntArrayView {
    private final int[] elements;

    private IntArrayView(final int[] elements) {
        this.elements = elements;
    }

    /**
     * Creates a zero-copy view of the specified array.
     *
     * @param array the array to view
     * @return a new view backed by {@code array}
     * @throws NullPointerException if {@code array} is {@code null}
     */
    public static IntArrayView wrap(final int[] array) {
        return new IntArrayView(Objects.requireNonNull(array, "array"));
    }

    /**
     * Returns the number of elements in the viewed array.
     *
     * @return the array length
     */
    public int length() {
        return elements.length;
    }

    /**
     * Returns whether the viewed array has no elements.
     *
     * @return {@code true} when {@link #length()} is zero
     */
    public boolean isEmpty() {
        return elements.length == 0;
    }

    /**
     * Returns the value currently stored at the specified index.
     *
     * @param index the zero-based index
     * @return the current value at {@code index}
     * @throws ArrayIndexOutOfBoundsException if the index is outside the viewed array
     */
    public int get(final int index) {
        return elements[index];
    }

    /**
     * Returns a defensive snapshot of the currently viewed contents.
     *
     * @return a newly allocated array containing the current values
     */
    public int[] toArray() {
        return elements.clone();
    }

    /**
     * Returns an immutable snapshot of the currently viewed contents.
     *
     * @return an immutable value that is independent of subsequent backing-array changes
     */
    public ImmutableIntArray toImmutableIntArray() {
        return ImmutableIntArray.copyOf(elements);
    }

    /**
     * Returns a stream over the backing array.
     *
     * <p>The stream is not a snapshot. Concurrent modification during traversal has no synchronization
     * guarantees; callers must arrange external synchronization or take a snapshot first.</p>
     *
     * @return a stream over the viewed contents in encounter order
     */
    public IntStream stream() {
        return IntStream.of(elements);
    }

    /**
     * Returns the current viewed contents in the same format as {@link java.util.Arrays#toString(int[])}.
     *
     * @return a string representation of the current contents
     */
    @Override
    public String toString() {
        return N.toString(elements);
    }
}
