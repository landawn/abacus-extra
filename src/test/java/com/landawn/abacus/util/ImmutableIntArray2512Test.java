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

/**
 * Comprehensive unit tests for ImmutableIntArray class.
 * Tests all public methods including factory methods, accessors, stream operations,
 * and immutability guarantees. Covers: of(), copyOf(), get(), forEach(), forEachIndexed(),
 * stream(), copy(), copyToArray(), hashCode(), equals(), toString().
 */
@Tag("2512")
public class ImmutableIntArray2512Test extends TestBase {

    // ============================================
    // Tests for of() factory method
    // ============================================

    @Test
    public void test_of_withValidArray() {
        int[] data = {1, 2, 3, 4, 5};
        ImmutableIntArray array = ImmutableIntArray.of(data);

        assertNotNull(array);
        assertEquals(5, array.length);
        assertEquals(1, array.get(0));
        assertEquals(5, array.get(4));
    }

    @Test
    public void test_of_withNullArray() {
        ImmutableIntArray array = ImmutableIntArray.of(null);

        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void test_of_withEmptyArray() {
        int[] data = {};
        ImmutableIntArray array = ImmutableIntArray.of(data);

        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void test_of_sharesReference() {
        int[] data = {10, 20, 30};
        ImmutableIntArray array = ImmutableIntArray.of(data);

        data[1] = 99;

        assertEquals(99, array.get(1));
    }

    @Test
    public void test_of_withSingleElement() {
        int[] data = {42};
        ImmutableIntArray array = ImmutableIntArray.of(data);

        assertNotNull(array);
        assertEquals(1, array.length);
        assertEquals(42, array.get(0));
    }

    @Test
    public void test_of_withNegativeValues() {
        int[] data = {-5, -10, -15};
        ImmutableIntArray array = ImmutableIntArray.of(data);

        assertEquals(3, array.length);
        assertEquals(-5, array.get(0));
        assertEquals(-15, array.get(2));
    }

    @Test
    public void test_of_withZeroValues() {
        int[] data = {0, 0, 0};
        ImmutableIntArray array = ImmutableIntArray.of(data);

        assertEquals(3, array.length);
        assertEquals(0, array.get(0));
        assertEquals(0, array.get(2));
    }

    @Test
    public void test_of_withExtremeValues() {
        int[] data = {Integer.MIN_VALUE, Integer.MAX_VALUE, 0};
        ImmutableIntArray array = ImmutableIntArray.of(data);

        assertEquals(3, array.length);
        assertEquals(Integer.MIN_VALUE, array.get(0));
        assertEquals(Integer.MAX_VALUE, array.get(1));
        assertEquals(0, array.get(2));
    }

    // ============================================
    // Tests for copyOf() factory method
    // ============================================

    @Test
    public void test_copyOf_withValidArray() {
        int[] data = {1, 2, 3, 4, 5};
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        assertNotNull(array);
        assertEquals(5, array.length);
        assertEquals(1, array.get(0));
        assertEquals(5, array.get(4));
    }

    @Test
    public void test_copyOf_withNullArray() {
        ImmutableIntArray array = ImmutableIntArray.copyOf(null);

        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void test_copyOf_withEmptyArray() {
        int[] data = {};
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        assertNotNull(array);
        assertEquals(0, array.length);
    }

    @Test
    public void test_copyOf_doesNotShareReference() {
        int[] data = {10, 20, 30};
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        data[1] = 99;

        assertEquals(20, array.get(1));
    }

    @Test
    public void test_copyOf_createsIndependentCopy() {
        int[] data = {1, 2, 3};
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        data[0] = 100;
        data[1] = 200;
        data[2] = 300;

        assertEquals(1, array.get(0));
        assertEquals(2, array.get(1));
        assertEquals(3, array.get(2));
    }

    @Test
    public void test_copyOf_withSingleElement() {
        int[] data = {999};
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        assertEquals(1, array.length);
        assertEquals(999, array.get(0));
    }

    @Test
    public void test_copyOf_withLargeArray() {
        int[] data = new int[10000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i;
        }

        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        assertEquals(10000, array.length);
        assertEquals(0, array.get(0));
        assertEquals(9999, array.get(9999));
    }

    // ============================================
    // Tests for get() method
    // ============================================

    @Test
    public void test_get_validIndex() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30, 40, 50});

        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
        assertEquals(40, array.get(3));
        assertEquals(50, array.get(4));
    }

    @Test
    public void test_get_firstElement() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{100, 200, 300});

        assertEquals(100, array.get(0));
    }

    @Test
    public void test_get_lastElement() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{100, 200, 300});

        assertEquals(300, array.get(array.length - 1));
    }

    @Test
    public void test_get_invalidIndexNegative() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> array.get(-1));
    }

    @Test
    public void test_get_invalidIndexTooLarge() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> array.get(3));
    }

    @Test
    public void test_get_emptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{});

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> array.get(0));
    }

    @Test
    public void test_get_boundaryIndex() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5});

        assertEquals(1, array.get(0));
        assertEquals(5, array.get(4));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> array.get(5));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> array.get(-1));
    }

    // ============================================
    // Tests for length field
    // ============================================

    @Test
    public void test_length_nonEmptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5});

        assertEquals(5, array.length);
    }

    @Test
    public void test_length_emptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{});

        assertEquals(0, array.length);
    }

    @Test
    public void test_length_nullArray() {
        ImmutableIntArray array = ImmutableIntArray.of(null);

        assertEquals(0, array.length);
    }

    @Test
    public void test_length_singleElement() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{42});

        assertEquals(1, array.length);
    }

    @Test
    public void test_length_largeArray() {
        int[] data = new int[100000];
        ImmutableIntArray array = ImmutableIntArray.of(data);

        assertEquals(100000, array.length);
    }

    // ============================================
    // Tests for forEach() method
    // ============================================

    @Test
    public void test_forEach_withElements() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5});
        AtomicInteger sum = new AtomicInteger(0);

        array.forEach(value -> sum.addAndGet(value));

        assertEquals(15, sum.get());
    }

    @Test
    public void test_forEach_emptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{});
        AtomicInteger count = new AtomicInteger(0);

        array.forEach(value -> count.incrementAndGet());

        assertEquals(0, count.get());
    }

    @Test
    public void test_forEach_nullAction() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertThrows(IllegalArgumentException.class, () -> array.forEach(null));
    }

    @Test
    public void test_forEach_maintainsOrder() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});
        StringBuilder sb = new StringBuilder();

        array.forEach(value -> sb.append(value).append(","));

        assertEquals("10,20,30,", sb.toString());
    }

    @Test
    public void test_forEach_withSideEffects() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{2, 4, 6, 8});
        int[] result = new int[1];

        array.forEach(value -> result[0] += value);

        assertEquals(20, result[0]);
    }

    @Test
    public void test_forEach_singleElement() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{42});
        AtomicInteger count = new AtomicInteger(0);

        array.forEach(value -> count.addAndGet(value));

        assertEquals(42, count.get());
    }

    @Test
    public void test_forEach_exceptionInConsumer() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertThrows(RuntimeException.class, () -> {
            array.forEach(value -> {
                if (value == 2) {
                    throw new RuntimeException("Test exception");
                }
            });
        });
    }

    // ============================================
    // Tests for forEachIndexed() method
    // ============================================

    @Test
    public void test_forEachIndexed_withElements() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});
        StringBuilder sb = new StringBuilder();

        array.forEachIndexed((index, value) -> sb.append(index).append(":").append(value).append(","));

        assertEquals("0:10,1:20,2:30,", sb.toString());
    }

    @Test
    public void test_forEachIndexed_emptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{});
        AtomicInteger count = new AtomicInteger(0);

        array.forEachIndexed((index, value) -> count.incrementAndGet());

        assertEquals(0, count.get());
    }

    @Test
    public void test_forEachIndexed_nullAction() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertThrows(IllegalArgumentException.class, () -> array.forEachIndexed(null));
    }

    @Test
    public void test_forEachIndexed_correctIndexValues() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{100, 200, 300, 400});
        AtomicInteger indexSum = new AtomicInteger(0);
        AtomicInteger valueSum = new AtomicInteger(0);

        array.forEachIndexed((index, value) -> {
            indexSum.addAndGet(index);
            valueSum.addAndGet(value);
        });

        assertEquals(6, indexSum.get());
        assertEquals(1000, valueSum.get());
    }

    @Test
    public void test_forEachIndexed_verifyOrder() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{100, 200, 300});
        int[] indices = new int[3];
        int[] values = new int[3];
        AtomicInteger counter = new AtomicInteger(0);

        array.forEachIndexed((index, value) -> {
            int pos = counter.getAndIncrement();
            indices[pos] = index;
            values[pos] = value;
        });

        assertArrayEquals(new int[]{0, 1, 2}, indices);
        assertArrayEquals(new int[]{100, 200, 300}, values);
    }

    @Test
    public void test_forEachIndexed_singleElement() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{999});
        int[] capturedIndex = {-1};
        int[] capturedValue = {-1};

        array.forEachIndexed((index, value) -> {
            capturedIndex[0] = index;
            capturedValue[0] = value;
        });

        assertEquals(0, capturedIndex[0]);
        assertEquals(999, capturedValue[0]);
    }

    @Test
    public void test_forEachIndexed_exceptionInConsumer() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertThrows(RuntimeException.class, () -> {
            array.forEachIndexed((index, value) -> {
                if (index == 1) {
                    throw new RuntimeException("Test exception");
                }
            });
        });
    }

    // ============================================
    // Tests for stream() method
    // ============================================

    @Test
    public void test_stream_sum() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5});

        int sum = array.stream().sum();

        assertEquals(15, sum);
    }

    @Test
    public void test_stream_max() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{3, 7, 2, 9, 1});

        int max = array.stream().max().orElse(-1);

        assertEquals(9, max);
    }

    @Test
    public void test_stream_min() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{3, 7, 2, 9, 1});

        int min = array.stream().min().orElse(-1);

        assertEquals(1, min);
    }

    @Test
    public void test_stream_filter() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5, 6, 7, 8});

        int[] evens = array.stream().filter(x -> x % 2 == 0).toArray();

        assertArrayEquals(new int[]{2, 4, 6, 8}, evens);
    }

    @Test
    public void test_stream_map() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4});

        int[] squared = array.stream().map(x -> x * x).toArray();

        assertArrayEquals(new int[]{1, 4, 9, 16}, squared);
    }

    @Test
    public void test_stream_emptyArray() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{});

        int sum = array.stream().sum();

        assertEquals(0, sum);
    }

    @Test
    public void test_stream_count() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30, 40, 50});

        long count = array.stream().count();

        assertEquals(5, count);
    }

    @Test
    public void test_stream_anyMatch() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5});

        boolean hasEven = array.stream().anyMatch(x -> x % 2 == 0);

        assertTrue(hasEven);
    }

    @Test
    public void test_stream_allMatch() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{2, 4, 6, 8});

        boolean allEven = array.stream().allMatch(x -> x % 2 == 0);

        assertTrue(allEven);
    }

    @Test
    public void test_stream_noneMatch() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 3, 5, 7});

        boolean noneEven = array.stream().noneMatch(x -> x % 2 == 0);

        assertTrue(noneEven);
    }

    @Test
    public void test_stream_average() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{2, 4, 6, 8});

        double avg = array.stream().average().orElse(0.0);

        assertEquals(5.0, avg, 0.001);
    }

    @Test
    public void test_stream_distinct() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 2, 3, 3, 3, 4});

        int[] distinct = array.stream().distinct().toArray();

        assertArrayEquals(new int[]{1, 2, 3, 4}, distinct);
    }

    @Test
    public void test_stream_sorted() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{5, 2, 8, 1, 9});

        int[] sorted = array.stream().sorted().toArray();

        assertArrayEquals(new int[]{1, 2, 5, 8, 9}, sorted);
    }

    @Test
    public void test_stream_limit() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5});

        int[] limited = array.stream().limit(3).toArray();

        assertArrayEquals(new int[]{1, 2, 3}, limited);
    }

    @Test
    public void test_stream_skip() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5});

        int[] skipped = array.stream().skip(2).toArray();

        assertArrayEquals(new int[]{3, 4, 5}, skipped);
    }

    @Test
    public void test_stream_chainedOperations() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        int result = array.stream().filter(x -> x % 2 == 0).map(x -> x * 2).sum();

        assertEquals(60, result);
    }

    // ============================================
    // Tests for copy() method
    // ============================================

    @Test
    public void test_copy_validRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30, 40, 50});

        ImmutableIntArray subArray = array.copy(1, 4);

        assertEquals(3, subArray.length);
        assertEquals(20, subArray.get(0));
        assertEquals(30, subArray.get(1));
        assertEquals(40, subArray.get(2));
    }

    @Test
    public void test_copy_fullRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        ImmutableIntArray copy = array.copy(0, 3);

        assertEquals(3, copy.length);
        assertEquals(10, copy.get(0));
        assertEquals(30, copy.get(2));
    }

    @Test
    public void test_copy_emptyRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        ImmutableIntArray empty = array.copy(2, 2);

        assertEquals(0, empty.length);
    }

    @Test
    public void test_copy_invalidRangeNegativeStart() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        assertThrows(IndexOutOfBoundsException.class, () -> array.copy(-1, 2));
    }

    @Test
    public void test_copy_invalidRangeEndTooLarge() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        assertThrows(IndexOutOfBoundsException.class, () -> array.copy(0, 4));
    }

    @Test
    public void test_copy_invalidRangeStartGreaterThanEnd() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        assertThrows(IndexOutOfBoundsException.class, () -> array.copy(2, 1));
    }

    @Test
    public void test_copy_isIndependent() {
        int[] original = {10, 20, 30, 40, 50};
        ImmutableIntArray array = ImmutableIntArray.of(original);

        ImmutableIntArray copy = array.copy(1, 4);

        original[2] = 999;

        assertEquals(30, copy.get(1));
    }

    @Test
    public void test_copy_createsNewInstance() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5});
        ImmutableIntArray copy = array.copy(0, 5);

        assertNotSame(array, copy);
        assertEquals(array, copy);
    }

    @Test
    public void test_copy_singleElement() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        ImmutableIntArray single = array.copy(1, 2);

        assertEquals(1, single.length);
        assertEquals(20, single.get(0));
    }

    // ============================================
    // Tests for copyToArray() method
    // ============================================

    @Test
    public void test_copyToArray_validRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30, 40, 50});

        int[] result = array.copyToArray(1, 4);

        assertArrayEquals(new int[]{20, 30, 40}, result);
    }

    @Test
    public void test_copyToArray_fullRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        int[] result = array.copyToArray(0, 3);

        assertArrayEquals(new int[]{10, 20, 30}, result);
    }

    @Test
    public void test_copyToArray_emptyRange() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        int[] result = array.copyToArray(2, 2);

        assertArrayEquals(new int[]{}, result);
    }

    @Test
    public void test_copyToArray_invalidRangeNegativeStart() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        assertThrows(IndexOutOfBoundsException.class, () -> array.copyToArray(-1, 2));
    }

    @Test
    public void test_copyToArray_invalidRangeEndTooLarge() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        assertThrows(IndexOutOfBoundsException.class, () -> array.copyToArray(0, 4));
    }

    @Test
    public void test_copyToArray_invalidRangeStartGreaterThanEnd() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30});

        assertThrows(IndexOutOfBoundsException.class, () -> array.copyToArray(2, 1));
    }

    @Test
    public void test_copyToArray_isMutable() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{10, 20, 30, 40, 50});

        int[] result = array.copyToArray(1, 4);
        result[0] = 999;

        assertEquals(20, array.get(1));
        assertEquals(999, result[0]);
    }

    @Test
    public void test_copyToArray_singleElement() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{42});

        int[] result = array.copyToArray(0, 1);

        assertArrayEquals(new int[]{42}, result);
    }

    @Test
    public void test_copyToArray_middleSection() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5, 6, 7});

        int[] result = array.copyToArray(2, 5);

        assertArrayEquals(new int[]{3, 4, 5}, result);
    }

    // ============================================
    // Tests for hashCode() method
    // ============================================

    @Test
    public void test_hashCode_sameContent() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{1, 2, 3});
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void test_hashCode_differentContent() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{1, 2, 3});
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[]{1, 2, 4});

        assertNotEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void test_hashCode_emptyArrays() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{});
        ImmutableIntArray array2 = ImmutableIntArray.of(null);

        assertEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void test_hashCode_consistent() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});

        int hash1 = array.hashCode();
        int hash2 = array.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    public void test_hashCode_singleElement() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{42});
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[]{42});

        assertEquals(array1.hashCode(), array2.hashCode());
    }

    // ============================================
    // Tests for equals() method
    // ============================================

    @Test
    public void test_equals_sameContent() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{1, 2, 3});
        ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[]{1, 2, 3});

        assertEquals(array1, array2);
        assertTrue(array1.equals(array2));
    }

    @Test
    public void test_equals_sameObject() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertEquals(array, array);
        assertTrue(array.equals(array));
    }

    @Test
    public void test_equals_differentContent() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{1, 2, 3});
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[]{1, 2, 4});

        assertNotEquals(array1, array2);
        assertFalse(array1.equals(array2));
    }

    @Test
    public void test_equals_differentLength() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{1, 2, 3});
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[]{1, 2});

        assertNotEquals(array1, array2);
        assertFalse(array1.equals(array2));
    }

    @Test
    public void test_equals_null() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertNotEquals(null, array);
        assertFalse(array.equals(null));
    }

    @Test
    public void test_equals_differentType() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});
        int[] primitiveArray = {1, 2, 3};

        assertNotEquals(array, primitiveArray);
        assertFalse(array.equals(primitiveArray));
    }

    @Test
    public void test_equals_emptyArrays() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{});
        ImmutableIntArray array2 = ImmutableIntArray.of(null);

        assertEquals(array1, array2);
        assertTrue(array1.equals(array2));
    }

    @Test
    public void test_equals_singleElement() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{42});
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[]{42});

        assertEquals(array1, array2);
    }

    @Test
    public void test_equals_hashCodeContract() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{1, 2, 3});
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertTrue(array1.equals(array2));
        assertEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void test_equals_symmetry() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{1, 2, 3});
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertTrue(array1.equals(array2));
        assertTrue(array2.equals(array1));
    }

    @Test
    public void test_equals_transitivity() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{1, 2, 3});
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[]{1, 2, 3});
        ImmutableIntArray array3 = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertTrue(array1.equals(array2));
        assertTrue(array2.equals(array3));
        assertTrue(array1.equals(array3));
    }

    // ============================================
    // Tests for toString() method
    // ============================================

    @Test
    public void test_toString_nonEmpty() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertEquals("[1, 2, 3]", array.toString());
    }

    @Test
    public void test_toString_empty() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{});

        assertEquals("[]", array.toString());
    }

    @Test
    public void test_toString_null() {
        ImmutableIntArray array = ImmutableIntArray.of(null);

        assertEquals("[]", array.toString());
    }

    @Test
    public void test_toString_singleElement() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{42});

        assertEquals("[42]", array.toString());
    }

    @Test
    public void test_toString_largeValues() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE});

        String result = array.toString();
        assertTrue(result.contains(String.valueOf(Integer.MAX_VALUE)));
        assertTrue(result.contains(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void test_toString_consistency() {
        ImmutableIntArray array1 = ImmutableIntArray.of(new int[]{1, 2, 3});
        ImmutableIntArray array2 = ImmutableIntArray.of(new int[]{1, 2, 3});

        assertEquals(array1.toString(), array2.toString());
    }

    @Test
    public void test_toString_negativeValues() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{-1, -2, -3});

        assertEquals("[-1, -2, -3]", array.toString());
    }

    // ============================================
    // Immutability and edge case tests
    // ============================================

    @Test
    public void test_immutability_copyOfProtectsAgainstExternalMutation() {
        int[] original = {1, 2, 3, 4, 5};
        ImmutableIntArray array = ImmutableIntArray.copyOf(original);

        original[0] = 999;
        original[4] = 888;

        assertEquals(1, array.get(0));
        assertEquals(5, array.get(4));
    }

    @Test
    public void test_immutability_ofAllowsExternalMutation() {
        int[] original = {1, 2, 3, 4, 5};
        ImmutableIntArray array = ImmutableIntArray.of(original);

        original[2] = 999;

        assertEquals(999, array.get(2));
    }

    @Test
    public void test_immutability_copyToArrayAllowsMutation() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3});
        int[] copy = array.copyToArray(0, 3);

        copy[0] = 999;

        assertEquals(1, array.get(0));
    }

    @Test
    public void test_edgeCase_largeArray() {
        int[] largeArray = new int[100000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = i;
        }

        ImmutableIntArray array = ImmutableIntArray.copyOf(largeArray);

        assertEquals(100000, array.length);
        assertEquals(0, array.get(0));
        assertEquals(99999, array.get(99999));
    }

    @Test
    public void test_integration_multipleOperations() {
        int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        data[0] = 999;
        assertEquals(1, array.get(0));

        int evenSum = array.stream().filter(x -> x % 2 == 0).sum();
        assertEquals(30, evenSum);

        ImmutableIntArray subArray = array.copy(2, 5);
        assertEquals(3, subArray.length);
        assertEquals(3, subArray.get(0));
    }

    @Test
    public void test_integration_forEachWithStream() {
        ImmutableIntArray array = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5});

        AtomicInteger forEachSum = new AtomicInteger(0);
        array.forEach(forEachSum::addAndGet);

        int streamSum = array.stream().sum();

        assertEquals(forEachSum.get(), streamSum);
    }

    @Test
    public void test_integration_copyAndEquals() {
        ImmutableIntArray original = ImmutableIntArray.of(new int[]{1, 2, 3, 4, 5});
        ImmutableIntArray fullCopy = original.copy(0, 5);

        assertEquals(original, fullCopy);
        assertNotSame(original, fullCopy);
        assertEquals(original.hashCode(), fullCopy.hashCode());
        assertEquals(original.toString(), fullCopy.toString());
    }
}
