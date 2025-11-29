/*
 * Copyright (C) 2024 HaiYang Li
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.stream.IntStream;

public class ImmutableIntArrayTest extends TestBase {

    @Test
    public void testOfWithArray() {
        int[] array = { 1, 2, 3, 4, 5 };
        ImmutableIntArray immutable = ImmutableIntArray.of(array);

        assertEquals(5, immutable.length);
        assertEquals(1, immutable.get(0));
        assertEquals(3, immutable.get(2));
        assertEquals(5, immutable.get(4));

        // Test that changes to original array affect immutable (no copy)
        array[0] = 10;
        assertEquals(10, immutable.get(0));
    }

    @Test
    public void testOfWithNullArray() {
        ImmutableIntArray immutable = ImmutableIntArray.of(null);
        assertEquals(0, immutable.length);
    }

    @Test
    public void testCopyOfWithArray() {
        int[] array = { 10, 20, 30, 40, 50 };
        ImmutableIntArray immutable = ImmutableIntArray.copyOf(array);

        assertEquals(5, immutable.length);
        assertEquals(10, immutable.get(0));
        assertEquals(30, immutable.get(2));
        assertEquals(50, immutable.get(4));

        // Test that changes to original array do NOT affect immutable (defensive copy)
        array[0] = 100;
        assertEquals(10, immutable.get(0));   // Should still be 10
    }

    @Test
    public void testCopyOfWithNullArray() {
        ImmutableIntArray immutable = ImmutableIntArray.copyOf(null);
        assertEquals(0, immutable.length);
    }

    @Test
    public void testGetValidIndex() {
        ImmutableIntArray immutable = ImmutableIntArray.of(new int[] { 5, 10, 15 });
        assertEquals(5, immutable.get(0));
        assertEquals(10, immutable.get(1));
        assertEquals(15, immutable.get(2));
    }

    @Test
    public void testGetInvalidIndex() {
        ImmutableIntArray immutable = ImmutableIntArray.of(new int[] { 1, 2, 3 });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> immutable.get(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> immutable.get(3));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> immutable.get(10));
    }

    @Test
    public void testForEach() {
        ImmutableIntArray immutable = ImmutableIntArray.of(new int[] { 1, 2, 3, 4 });
        AtomicInteger sum = new AtomicInteger(0);

        immutable.forEach(value -> sum.addAndGet(value));
        assertEquals(10, sum.get());
    }

    @Test
    public void testForEachWithNullAction() {
        ImmutableIntArray immutable = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        assertThrows(IllegalArgumentException.class, () -> immutable.forEach(null));
    }

    @Test
    public void testForEachIndexed() {
        ImmutableIntArray immutable = ImmutableIntArray.of(new int[] { 10, 20, 30 });
        StringBuilder result = new StringBuilder();

        immutable.forEachIndexed((index, value) -> result.append(index).append(":").append(value).append(","));
        assertEquals("0:10,1:20,2:30,", result.toString());
    }

    @Test
    public void testForEachIndexedWithNullAction() {
        ImmutableIntArray immutable = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        assertThrows(IllegalArgumentException.class, () -> immutable.forEachIndexed(null));
    }

    @Test
    public void testStream() {
        ImmutableIntArray immutable = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5 });
        IntStream stream = immutable.stream();

        assertEquals(15, stream.sum());

        // Test another stream operation
        IntStream stream2 = immutable.stream();
        assertEquals(5, stream2.max().orElse(0));
    }

    @Test
    public void testCopyWithValidRange() {
        ImmutableIntArray immutable = ImmutableIntArray.of(new int[] { 10, 20, 30, 40, 50 });
        ImmutableIntArray subArray = immutable.copy(1, 4);

        assertEquals(3, subArray.length);
        assertEquals(20, subArray.get(0));
        assertEquals(30, subArray.get(1));
        assertEquals(40, subArray.get(2));
    }

    @Test
    public void testCopyWithInvalidRange() {
        ImmutableIntArray immutable = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5 });

        assertThrows(IndexOutOfBoundsException.class, () -> immutable.copy(-1, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> immutable.copy(2, 6));
        assertThrows(IndexOutOfBoundsException.class, () -> immutable.copy(3, 2));
    }

    @Test
    public void testCopyToArrayWithValidRange() {
        ImmutableIntArray immutable = ImmutableIntArray.of(new int[] { 10, 20, 30, 40, 50 });
        int[] array = immutable.copyToArray(1, 4);

        assertEquals(3, array.length);
        assertEquals(20, array[0]);
        assertEquals(30, array[1]);
        assertEquals(40, array[2]);

        // Test that returned array is mutable
        array[0] = 999;
        assertEquals(999, array[0]);
        assertEquals(20, immutable.get(1));   // Original should be unchanged
    }

    @Test
    public void testCopyToArrayWithInvalidRange() {
        ImmutableIntArray immutable = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5 });

        assertThrows(IndexOutOfBoundsException.class, () -> immutable.copyToArray(-1, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> immutable.copyToArray(2, 6));
        assertThrows(IndexOutOfBoundsException.class, () -> immutable.copyToArray(3, 2));
    }

    @Test
    public void testEquals() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array3 = ImmutableIntArray.of(new int[] { 1, 2, 4 });
        ImmutableIntArray empty1 = ImmutableIntArray.of(new int[] {});
        ImmutableIntArray empty2 = ImmutableIntArray.of(new int[] {});

        assertTrue(array1.equals(array2));
        assertFalse(array1.equals(array3));
        assertTrue(empty1.equals(empty2));
        assertFalse(array1.equals(null));
        assertFalse(array1.equals("not an array"));

        // Test self-equality
        assertTrue(array1.equals(array1));
    }

    @Test
    public void testHashCode() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array3 = ImmutableIntArray.of(new int[] { 1, 2, 4 });

        assertEquals(array1.hashCode(), array2.hashCode());
        assertNotEquals(array1.hashCode(), array3.hashCode());
    }

    @Test
    public void testToString() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray empty = ImmutableIntArray.of(new int[] {});

        String str1 = array1.toString();
        String strEmpty = empty.toString();

        assertTrue(str1.contains("1"));
        assertTrue(str1.contains("2"));
        assertTrue(str1.contains("3"));
        assertEquals("[]", strEmpty);
    }

    @Test
    public void testEmptyArray() {
        ImmutableIntArray empty = ImmutableIntArray.of(new int[] {});
        assertEquals(0, empty.length);

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> empty.get(0));

        // ForEach should work on empty array (do nothing)
        AtomicInteger counter = new AtomicInteger(0);
        empty.forEach(value -> counter.incrementAndGet());
        assertEquals(0, counter.get());

        // Stream operations should work on empty array
        assertEquals(0, empty.stream().count());
    }

    @Test
    public void testLengthField() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5 });
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[] {});
        ImmutableIntArray array3 = ImmutableIntArray.of(null);

        assertEquals(5, array1.length);
        assertEquals(0, array2.length);
        assertEquals(0, array3.length);
    }

    @Test
    public void testImmutability() {
        int[] original = { 1, 2, 3, 4, 5 };
        ImmutableIntArray immutable = ImmutableIntArray.copyOf(original);

        // Modify original array - should not affect immutable
        original[0] = 999;
        original[4] = 888;

        assertEquals(1, immutable.get(0));
        assertEquals(5, immutable.get(4));

        // Test that we can't modify through any means
        // (The class doesn't expose any mutator methods)
        // This is verified by the design - no setters or modification methods exist
    }
}