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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.stream.IntStream;

@Tag("2025")
class IntArrayViewTest extends TestBase {

    @Test
    void wrapRetainsBackingArray() {
        final int[] backing = { 1, 2, 3 };
        final IntArrayView view = IntArrayView.wrap(backing);

        backing[0] = 9;

        assertEquals(3, view.length());
        assertFalse(view.isEmpty());
        assertEquals(9, view.get(0));
        assertArrayEquals(new int[] { 9, 2, 3 }, view.stream().toArray());
        assertEquals("[9, 2, 3]", view.toString());
    }

    @Test
    void streamIsALiveViewUntilTraversal() {
        final int[] backing = { 1, 2, 3 };
        final IntStream stream = IntArrayView.wrap(backing).stream();

        backing[1] = 8;

        assertArrayEquals(new int[] { 1, 8, 3 }, stream.toArray());
    }

    @Test
    void snapshotsDoNotExposeOrRetainBackingArray() {
        final int[] backing = { 1, 2, 3 };
        final IntArrayView view = IntArrayView.wrap(backing);
        final int[] arraySnapshot = view.toArray();
        final ImmutableIntArray immutableSnapshot = view.toImmutableIntArray();

        assertNotSame(backing, arraySnapshot);
        arraySnapshot[0] = 8;
        backing[1] = 7;

        assertArrayEquals(new int[] { 1, 2, 3 }, immutableSnapshot.stream().toArray());
        assertEquals(1, view.get(0));
        assertEquals(7, view.get(1));
    }

    @Test
    void emptyAndInvalidInputs() {
        final IntArrayView empty = IntArrayView.wrap(new int[0]);

        assertTrue(empty.isEmpty());
        assertEquals(0, empty.length());
        assertArrayEquals(new int[0], empty.toArray());
        assertThrows(NullPointerException.class, () -> IntArrayView.wrap(null));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> empty.get(0));
    }

    @Test
    void equalityIsIdentityBased() {
        final int[] backing = { 1, 2 };
        final IntArrayView first = IntArrayView.wrap(backing);
        final IntArrayView second = IntArrayView.wrap(backing);
        final java.util.Map<IntArrayView, String> map = new java.util.HashMap<>();
        map.put(first, "value");

        assertEquals(first, first);
        assertNotEquals(first, second);
        assertFalse(Immutable.class.isAssignableFrom(IntArrayView.class));

        backing[0] = 9;

        assertEquals("value", map.get(first));
    }
}
