/*
 * Copyright (C) 2020 HaiYang Li
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

@Tag("2510")
public class ImmutableIntArray2510Test extends TestBase {

    @Test
    public void testOf_withValidArray() {
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray array = ImmutableIntArray.of(data);

        assertNotNull(array);
        assertEquals(5, array.length);
        assertEquals(1, array.get(0));
        assertEquals(5, array.get(4));
    }

    @Test
    public void testOf_withNullArray() {
        ImmutableIntArray array = ImmutableIntArray.of(null);

        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void testOf_withEmptyArray() {
        int[] data = {};
        ImmutableIntArray array = ImmutableIntArray.of(data);

        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void testOf_sharesReference() {
        int[] data = { 10, 20, 30 };
        ImmutableIntArray array = ImmutableIntArray.of(data);

        // Modify original array
        data[1] = 99;

        // Verify that the ImmutableIntArray sees the change (no defensive copy)
        assertEquals(99, array.get(1));
    }

    @Test
    public void testCopyOf_withValidArray() {
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        assertNotNull(array);
        assertEquals(5, array.length);
        assertEquals(1, array.get(0));
        assertEquals(5, array.get(4));
    }

    @Test
    public void testCopyOf_withNullArray() {
        ImmutableIntArray array = ImmutableIntArray.copyOf(null);

        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void testCopyOf_withEmptyArray() {
        int[] data = {};
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void testCopyOf_doesNotShareReference() {
        int[] data = { 10, 20, 30 };
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        // Modify original array
        data[1] = 99;

        // Verify that the ImmutableIntArray does NOT see the change (defensive copy)
        assertEquals(20, array.get(1));
    }

    @Test
    public void testGet_validIndex() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30, 40, 50 });

        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
        assertEquals(40, array.get(3));
        assertEquals(50, array.get(4));
    }

    @Test
    public void testGet_invalidIndexNegative() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> array.get(-1));
    }

    @Test
    public void testGet_invalidIndexTooLarge() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> array.get(3));
    }

    @Test
    public void testGet_emptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] {});

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> array.get(0));
    }

    @Test
    public void testLength_nonEmptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5 });

        assertEquals(5, array.length);
    }

    @Test
    public void testLength_emptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] {});

        assertEquals(0, array.length);
    }

    @Test
    public void testLength_nullArray() {
        ImmutableIntArray array = ImmutableIntArray.of(null);

        assertEquals(0, array.length);
    }

    @Test
    public void testForEach_withElements() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5 });
        AtomicInteger sum = new AtomicInteger(0);

        array.forEach(value -> sum.addAndGet(value));

        assertEquals(15, sum.get());
    }

    @Test
    public void testForEach_emptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] {});
        AtomicInteger count = new AtomicInteger(0);

        array.forEach(value -> count.incrementAndGet());

        assertEquals(0, count.get());
    }

    @Test
    public void testForEach_nullAction() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3 });

        assertThrows(IllegalArgumentException.class, () -> array.forEach(null));
    }

    @Test
    public void testForEach_maintainsOrder() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });
        StringBuilder sb = new StringBuilder();

        array.forEach(value -> sb.append(value).append(","));

        assertEquals("10,20,30,", sb.toString());
    }

    @Test
    public void testForEachIndexed_withElements() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });
        StringBuilder sb = new StringBuilder();

        array.forEachIndexed((index, value) -> sb.append(index).append(":").append(value).append(","));

        assertEquals("0:10,1:20,2:30,", sb.toString());
    }

    @Test
    public void testForEachIndexed_emptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] {});
        AtomicInteger count = new AtomicInteger(0);

        array.forEachIndexed((index, value) -> count.incrementAndGet());

        assertEquals(0, count.get());
    }

    @Test
    public void testForEachIndexed_nullAction() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3 });

        assertThrows(IllegalArgumentException.class, () -> array.forEachIndexed(null));
    }

    @Test
    public void testForEachIndexed_correctIndexValues() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 100, 200, 300, 400 });
        AtomicInteger indexSum = new AtomicInteger(0);
        AtomicInteger valueSum = new AtomicInteger(0);

        array.forEachIndexed((index, value) -> {
            indexSum.addAndGet(index);
            valueSum.addAndGet(value);
        });

        assertEquals(6, indexSum.get());   // 0 + 1 + 2 + 3
        assertEquals(1000, valueSum.get());   // 100 + 200 + 300 + 400
    }

    @Test
    public void testStream_sum() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5 });

        int sum = array.stream().sum();

        assertEquals(15, sum);
    }

    @Test
    public void testStream_max() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 3, 7, 2, 9, 1 });

        int max = array.stream().max().orElse(-1);

        assertEquals(9, max);
    }

    @Test
    public void testStream_min() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 3, 7, 2, 9, 1 });

        int min = array.stream().min().orElse(-1);

        assertEquals(1, min);
    }

    @Test
    public void testStream_filter() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 });

        int[] evens = array.stream().filter(x -> x % 2 == 0).toArray();

        assertArrayEquals(new int[] { 2, 4, 6, 8 }, evens);
    }

    @Test
    public void testStream_map() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3, 4 });

        int[] squared = array.stream().map(x -> x * x).toArray();

        assertArrayEquals(new int[] { 1, 4, 9, 16 }, squared);
    }

    @Test
    public void testStream_emptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] {});

        int sum = array.stream().sum();

        assertEquals(0, sum);
    }

    @Test
    public void testStream_count() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30, 40, 50 });

        long count = array.stream().count();

        assertEquals(5, count);
    }

    @Test
    public void testCopy_validRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30, 40, 50 });

        ImmutableIntArray subArray = array.copy(1, 4);

        assertEquals(3, subArray.length);
        assertEquals(20, subArray.get(0));
        assertEquals(30, subArray.get(1));
        assertEquals(40, subArray.get(2));
    }

    @Test
    public void testCopy_fullRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        ImmutableIntArray copy = array.copy(0, 3);

        assertEquals(3, copy.length);
        assertEquals(10, copy.get(0));
        assertEquals(30, copy.get(2));
    }

    @Test
    public void testCopy_emptyRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        ImmutableIntArray empty = array.copy(2, 2);

        assertEquals(0, empty.length);
    }

    @Test
    public void testCopy_invalidRangeNegativeStart() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        assertThrows(IndexOutOfBoundsException.class, () -> array.copy(-1, 2));
    }

    @Test
    public void testCopy_invalidRangeEndTooLarge() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        assertThrows(IndexOutOfBoundsException.class, () -> array.copy(0, 4));
    }

    @Test
    public void testCopy_invalidRangeStartGreaterThanEnd() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        assertThrows(IndexOutOfBoundsException.class, () -> array.copy(2, 1));
    }

    @Test
    public void testCopy_isIndependent() {
        int[] original = { 10, 20, 30, 40, 50 };
        ImmutableIntArray array = ImmutableIntArray.of(original);

        ImmutableIntArray copy = array.copy(1, 4);

        // Modify original array
        original[2] = 999;

        // Copy should not be affected (defensive copy is made)
        assertEquals(30, copy.get(1));
    }

    @Test
    public void testCopyToArray_validRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30, 40, 50 });

        int[] result = array.copyToArray(1, 4);

        assertArrayEquals(new int[] { 20, 30, 40 }, result);
    }

    @Test
    public void testCopyToArray_fullRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        int[] result = array.copyToArray(0, 3);

        assertArrayEquals(new int[] { 10, 20, 30 }, result);
    }

    @Test
    public void testCopyToArray_emptyRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        int[] result = array.copyToArray(2, 2);

        assertArrayEquals(new int[] {}, result);
    }

    @Test
    public void testCopyToArray_invalidRangeNegativeStart() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        assertThrows(IndexOutOfBoundsException.class, () -> array.copyToArray(-1, 2));
    }

    @Test
    public void testCopyToArray_invalidRangeEndTooLarge() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        assertThrows(IndexOutOfBoundsException.class, () -> array.copyToArray(0, 4));
    }

    @Test
    public void testCopyToArray_invalidRangeStartGreaterThanEnd() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30 });

        assertThrows(IndexOutOfBoundsException.class, () -> array.copyToArray(2, 1));
    }

    @Test
    public void testCopyToArray_isMutable() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30, 40, 50 });

        int[] result = array.copyToArray(1, 4);
        result[0] = 999;

        // Original array should not be affected
        assertEquals(20, array.get(1));
        // Returned array is mutable
        assertEquals(999, result[0]);
    }

    @Test
    public void testHashCode_sameContent() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[] { 1, 2, 3 });

        assertEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void testHashCode_differentContent() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[] { 1, 2, 4 });

        assertNotEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void testHashCode_emptyArrays() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] {});
        ImmutableIntArray array2 = ImmutableIntArray.of(null);

        assertEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void testHashCode_differentLength() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[] { 1, 2 });

        assertNotEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void testEquals_sameContent() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });

        assertEquals(array1, array2);
        assertTrue(array1.equals(array2));
    }

    @Test
    public void testEquals_sameObject() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3 });

        assertEquals(array, array);
        assertTrue(array.equals(array));
    }

    @Test
    public void testEquals_differentContent() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[] { 1, 2, 4 });

        assertNotEquals(array1, array2);
        assertFalse(array1.equals(array2));
    }

    @Test
    public void testEquals_differentLength() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[] { 1, 2 });

        assertNotEquals(array1, array2);
        assertFalse(array1.equals(array2));
    }

    @Test
    public void testEquals_null() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3 });

        assertNotEquals(null, array);
        assertFalse(array.equals(null));
    }

    @Test
    public void testEquals_differentType() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        int[] primitiveArray = { 1, 2, 3 };

        assertNotEquals(array, primitiveArray);
        assertFalse(array.equals(primitiveArray));
    }

    @Test
    public void testEquals_emptyArrays() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] {});
        ImmutableIntArray array2 = ImmutableIntArray.of(null);

        assertEquals(array1, array2);
        assertTrue(array1.equals(array2));
    }

    @Test
    public void testEquals_singleElement() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 42 });
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[] { 42 });

        assertEquals(array1, array2);
    }

    @Test
    public void testToString_nonEmpty() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3 });

        assertEquals("[1, 2, 3]", array.toString());
    }

    @Test
    public void testToString_empty() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] {});

        assertEquals("[]", array.toString());
    }

    @Test
    public void testToString_null() {
        ImmutableIntArray array = ImmutableIntArray.of(null);

        assertEquals("[]", array.toString());
    }

    @Test
    public void testToString_singleElement() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 42 });

        assertEquals("[42]", array.toString());
    }

    @Test
    public void testToString_largeValues() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { Integer.MAX_VALUE, Integer.MIN_VALUE });

        String result = array.toString();
        assertTrue(result.contains(String.valueOf(Integer.MAX_VALUE)));
        assertTrue(result.contains(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void testImmutability_copyOfProtectsAgainstExternalMutation() {
        int[] original = { 1, 2, 3, 4, 5 };
        ImmutableIntArray array = ImmutableIntArray.copyOf(original);

        // Mutate the original array
        original[0] = 999;
        original[4] = 888;

        // ImmutableIntArray should not be affected
        assertEquals(1, array.get(0));
        assertEquals(5, array.get(4));
    }

    @Test
    public void testMultipleOperations_chained() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });

        int result = array.stream().filter(x -> x % 2 == 0).map(x -> x * 2).sum();

        // Even numbers: 2, 4, 6, 8, 10
        // Doubled: 4, 8, 12, 16, 20
        // Sum: 60
        assertEquals(60, result);
    }

    @Test
    public void testEdgeCase_largeArray() {
        int[] largeArray = new int[10000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = i;
        }

        ImmutableIntArray array = ImmutableIntArray.copyOf(largeArray);

        assertEquals(10000, array.length);
        assertEquals(0, array.get(0));
        assertEquals(9999, array.get(9999));
    }

    @Test
    public void testEdgeCase_negativeValues() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { -5, -10, -15, -20 });

        assertEquals(4, array.length);
        assertEquals(-5, array.get(0));
        assertEquals(-20, array.get(3));
    }

    @Test
    public void testEdgeCase_mixedValues() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { -100, 0, 100, Integer.MIN_VALUE, Integer.MAX_VALUE });

        assertEquals(5, array.length);
        assertEquals(-100, array.get(0));
        assertEquals(0, array.get(1));
        assertEquals(100, array.get(2));
        assertEquals(Integer.MIN_VALUE, array.get(3));
        assertEquals(Integer.MAX_VALUE, array.get(4));
    }

    @Test
    public void testCopy_startIndex() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30, 40, 50 });

        ImmutableIntArray copy = array.copy(2, 5);

        assertEquals(3, copy.length);
        assertEquals(30, copy.get(0));
        assertEquals(50, copy.get(2));
    }

    @Test
    public void testCopy_endIndex() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30, 40, 50 });

        ImmutableIntArray copy = array.copy(0, 3);

        assertEquals(3, copy.length);
        assertEquals(10, copy.get(0));
        assertEquals(30, copy.get(2));
    }

    @Test
    public void testForEach_exceptionInConsumer() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3 });

        assertThrows(RuntimeException.class, () -> {
            array.forEach(value -> {
                if (value == 2) {
                    throw new RuntimeException("Test exception");
                }
            });
        });
    }

    @Test
    public void testForEachIndexed_exceptionInConsumer() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3 });

        assertThrows(RuntimeException.class, () -> {
            array.forEachIndexed((index, value) -> {
                if (index == 1) {
                    throw new RuntimeException("Test exception");
                }
            });
        });
    }

    @Test
    public void testStream_anyMatch() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5 });

        boolean hasEven = array.stream().anyMatch(x -> x % 2 == 0);

        assertTrue(hasEven);
    }

    @Test
    public void testStream_allMatch() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 2, 4, 6, 8 });

        boolean allEven = array.stream().allMatch(x -> x % 2 == 0);

        assertTrue(allEven);
    }

    @Test
    public void testStream_noneMatch() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 3, 5, 7 });

        boolean noneEven = array.stream().noneMatch(x -> x % 2 == 0);

        assertTrue(noneEven);
    }

    @Test
    public void testCopyToArray_singleElement() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 42 });

        int[] result = array.copyToArray(0, 1);

        assertArrayEquals(new int[] { 42 }, result);
    }

    @Test
    public void testForEach_withLargeArray() {
        int[] largeData = new int[1000];
        for (int i = 0; i < largeData.length; i++) {
            largeData[i] = i;
        }
        ImmutableIntArray array = ImmutableIntArray.of(largeData);

        AtomicInteger count = new AtomicInteger(0);
        array.forEach(value -> count.incrementAndGet());

        assertEquals(1000, count.get());
    }

    @Test
    public void testForEachIndexed_verifyOrder() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 100, 200, 300 });
        int[] indices = new int[3];
        int[] values = new int[3];
        AtomicInteger counter = new AtomicInteger(0);

        array.forEachIndexed((index, value) -> {
            int pos = counter.getAndIncrement();
            indices[pos] = index;
            values[pos] = value;
        });

        assertArrayEquals(new int[] { 0, 1, 2 }, indices);
        assertArrayEquals(new int[] { 100, 200, 300 }, values);
    }

    @Test
    public void testStream_average() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 2, 4, 6, 8 });

        double avg = array.stream().average().orElse(0.0);

        assertEquals(5.0, avg, 0.001);
    }

    @Test
    public void testStream_distinct() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 2, 3, 3, 3, 4 });

        int[] distinct = array.stream().distinct().toArray();

        assertArrayEquals(new int[] { 1, 2, 3, 4 }, distinct);
    }

    @Test
    public void testStream_sorted() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 5, 2, 8, 1, 9 });

        int[] sorted = array.stream().sorted().toArray();

        assertArrayEquals(new int[] { 1, 2, 5, 8, 9 }, sorted);
    }

    @Test
    public void testStream_limit() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5 });

        int[] limited = array.stream().limit(3).toArray();

        assertArrayEquals(new int[] { 1, 2, 3 }, limited);
    }

    @Test
    public void testStream_skip() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5 });

        int[] skipped = array.stream().skip(2).toArray();

        assertArrayEquals(new int[] { 3, 4, 5 }, skipped);
    }

    @Test
    public void testEquals_hashCodeContract() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[] { 1, 2, 3 });

        // If two objects are equal, their hash codes must be equal
        assertTrue(array1.equals(array2));
        assertEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void testCopy_createsNewInstance() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 1, 2, 3, 4, 5 });
        ImmutableIntArray copy = array.copy(0, 5);

        assertNotSame(array, copy);
        assertEquals(array, copy);
    }

    @Test
    public void testToString_consistency() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[] { 1, 2, 3 });

        assertEquals(array1.toString(), array2.toString());
    }

    @Test
    public void testGet_boundaryCases() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[] { 10, 20, 30, 40, 50 });

        // First element
        assertEquals(10, array.get(0));

        // Last element
        assertEquals(50, array.get(array.length - 1));

        // Just before last
        assertEquals(40, array.get(array.length - 2));
    }
}
