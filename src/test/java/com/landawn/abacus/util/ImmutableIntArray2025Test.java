package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.stream.IntStream;

/**
 * Comprehensive unit tests for ImmutableIntArray.
 * Tests cover all public methods including factory methods, access methods,
 * iteration methods, stream operations, copy methods, and Object methods.
 */
@Tag("2025")
public class ImmutableIntArray2025Test extends TestBase {

    // ============================================
    // Tests for factory method: unsafeWrap()
    // ============================================

    @Test
    public void testWrap_WithValidArray() {
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        assertEquals(5, immutable.length());
        assertEquals(1, immutable.get(0));
        assertEquals(5, immutable.get(4));
    }

    @Test
    public void testWrap_WithNullArray() {
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(null);

        assertEquals(0, immutable.length());
    }

    @Test
    public void testWrap_WithEmptyArray() {
        int[] data = {};
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        assertEquals(0, immutable.length());
    }

    @Test
    public void testWrap_DoesNotCopyArray() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        // Modify original array
        data[0] = 99;

        // The immutable array reflects the change (no defensive copy)
        assertEquals(99, immutable.get(0));
    }

    // ============================================
    // Tests for factory method: copyOf()
    // ============================================

    @Test
    public void testCopyOf_WithValidArray() {
        int[] data = { 10, 20, 30 };
        ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);

        assertEquals(3, immutable.length());
        assertEquals(10, immutable.get(0));
        assertEquals(30, immutable.get(2));
    }

    @Test
    public void testCopyOf_WithNullArray() {
        ImmutableIntArray immutable = ImmutableIntArray.copyOf(null);

        assertEquals(0, immutable.length());
    }

    @Test
    public void testCopyOf_WithEmptyArray() {
        int[] data = {};
        ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);

        assertEquals(0, immutable.length());
    }

    @Test
    public void testCopyOf_CreatesDefensiveCopy() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);

        // Modify original array
        data[0] = 99;

        // The immutable array is not affected (defensive copy was made)
        assertEquals(1, immutable.get(0));
    }

    @Test
    public void testCopyOf_LargeArray() {
        int[] data = new int[10000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i;
        }

        ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);

        assertEquals(10000, immutable.length());
        assertEquals(0, immutable.get(0));
        assertEquals(9999, immutable.get(9999));

        // Modify original to ensure copy was made
        data[5000] = -1;
        assertEquals(5000, immutable.get(5000));
    }

    // ============================================
    // Tests for get() method
    // ============================================

    @Test
    public void testGet_ValidIndex() {
        int[] data = { 5, 10, 15, 20, 25 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        assertEquals(5, immutable.get(0));
        assertEquals(10, immutable.get(1));
        assertEquals(15, immutable.get(2));
        assertEquals(20, immutable.get(3));
        assertEquals(25, immutable.get(4));
    }

    @Test
    public void testGet_NegativeIndex() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> immutable.get(-1));
    }

    @Test
    public void testGet_IndexOutOfBounds() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> immutable.get(3));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> immutable.get(100));
    }

    @Test
    public void testGet_EmptyArray() {
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[0]);

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> immutable.get(0));
    }

    // ============================================
    // Tests for forEach() method
    // ============================================

    @Test
    public void testForEach_ValidArray() {
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        List<Integer> result = new ArrayList<>();
        immutable.forEach(v -> result.add(v));

        assertEquals(5, result.size());
        assertEquals(1, result.get(0));
        assertEquals(5, result.get(4));
    }

    @Test
    public void testForEach_EmptyArray() {
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[0]);

        List<Integer> result = new ArrayList<>();
        immutable.forEach(v -> result.add(v));

        assertEquals(0, result.size());
    }

    @Test
    public void testForEach_NullAction() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        assertThrows(IllegalArgumentException.class, () -> immutable.forEach(null));
    }

    @Test
    public void testForEach_OrderPreserved() {
        int[] data = { 10, 20, 30, 40, 50 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        List<Integer> result = new ArrayList<>();
        immutable.forEach(v -> result.add(v));

        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], result.get(i));
        }
    }

    @Test
    public void testForEach_WithSideEffects() {
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        AtomicInteger sum = new AtomicInteger(0);
        immutable.forEach(v -> sum.addAndGet(v));

        assertEquals(15, sum.get());
    }

    // ============================================
    // Tests for forEachIndexed() method
    // ============================================

    @Test
    public void testForEachIndexed_ValidArray() {
        int[] data = { 10, 20, 30 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        List<String> result = new ArrayList<>();
        immutable.forEachIndexed((index, value) -> result.add(index + ":" + value));

        assertEquals(3, result.size());
        assertEquals("0:10", result.get(0));
        assertEquals("1:20", result.get(1));
        assertEquals("2:30", result.get(2));
    }

    @Test
    public void testForEachIndexed_EmptyArray() {
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[0]);

        List<String> result = new ArrayList<>();
        immutable.forEachIndexed((index, value) -> result.add(index + ":" + value));

        assertEquals(0, result.size());
    }

    @Test
    public void testForEachIndexed_NullAction() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        assertThrows(IllegalArgumentException.class, () -> immutable.forEachIndexed(null));
    }

    @Test
    public void testForEachIndexed_VerifyIndices() {
        int[] data = { 100, 200, 300, 400 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        List<Integer> indices = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        immutable.forEachIndexed((index, value) -> {
            indices.add(index);
            values.add(value);
        });

        for (int i = 0; i < data.length; i++) {
            assertEquals(i, indices.get(i));
            assertEquals(data[i], values.get(i));
        }
    }

    @Test
    public void testForEachIndexed_OrderPreserved() {
        int[] data = { 5, 10, 15, 20 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        AtomicInteger expectedIndex = new AtomicInteger(0);
        immutable.forEachIndexed((index, value) -> {
            assertEquals(expectedIndex.get(), index);
            expectedIndex.incrementAndGet();
        });
    }

    // ============================================
    // Tests for stream() method
    // ============================================

    @Test
    public void testStream_ValidArray() {
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        IntStream stream = immutable.stream();

        int sum = stream.sum();
        assertEquals(15, sum);
    }

    @Test
    public void testStream_EmptyArray() {
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[0]);

        IntStream stream = immutable.stream();

        assertEquals(0, stream.count());
    }

    @Test
    public void testStream_Max() {
        int[] data = { 10, 50, 30, 20, 40 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        int max = immutable.stream().max().orElse(0);

        assertEquals(50, max);
    }

    @Test
    public void testStream_Min() {
        int[] data = { 30, 10, 50, 20, 40 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        int min = immutable.stream().min().orElse(0);

        assertEquals(10, min);
    }

    @Test
    public void testStream_Filter() {
        int[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        long count = immutable.stream().filter(v -> v % 2 == 0).count();

        assertEquals(5, count);
    }

    @Test
    public void testStream_Map() {
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        int sum = immutable.stream().map(v -> v * 2).sum();

        assertEquals(30, sum);
    }

    @Test
    public void testStream_ToArray() {
        int[] data = { 5, 4, 3, 2, 1 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        int[] result = immutable.stream().sorted().toArray();

        assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, result);
    }

    // ============================================
    // Tests for copy() method
    // ============================================

    @Test
    public void testCopy_FullRange() {
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        int[] copy = immutable.copyOfRange(0, 5);

        assertEquals(5, copy.length);
        assertEquals(1, copy[0]);
        assertEquals(5, copy[4]);
    }

    @Test
    public void testCopy_PartialRange() {
        int[] data = { 10, 20, 30, 40, 50 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        int[] copy = immutable.copyOfRange(1, 4);

        assertEquals(3, copy.length);
        assertEquals(20, copy[0]);
        assertEquals(30, copy[1]);
        assertEquals(40, copy[2]);
    }

    @Test
    public void testCopy_EmptyRange() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        int[] copy = immutable.copyOfRange(1, 1);

        assertEquals(0, copy.length);
    }

    @Test
    public void testCopy_InvalidFromIndex() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        assertThrows(IndexOutOfBoundsException.class, () -> immutable.copyOfRange(-1, 2));
    }

    @Test
    public void testCopy_InvalidToIndex() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        assertThrows(IndexOutOfBoundsException.class, () -> immutable.copyOfRange(0, 10));
    }

    @Test
    public void testCopy_FromIndexGreaterThanToIndex() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

        assertThrows(IndexOutOfBoundsException.class, () -> immutable.copyOfRange(2, 1));
    }

    // ============================================
    // Tests for copyToArray() method
    // ============================================

    // ============================================
    // Tests for hashCode() method
    // ============================================

    @Test
    public void testHashCode_EqualArrays() {
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(data);
        ImmutableIntArray array2 = ImmutableIntArray.copyOf(data);

        assertEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void testHashCode_DifferentArrays() {
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.unsafeWrap(new int[] { 4, 5, 6 });

        assertNotEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void testHashCode_EmptyArrays() {
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[0]);
        ImmutableIntArray array2 = ImmutableIntArray.copyOf(null);

        assertEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    public void testHashCode_Consistency() {
        int[] data = { 10, 20, 30 };
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

        int hash1 = array.hashCode();
        int hash2 = array.hashCode();

        assertEquals(hash1, hash2);
    }

    // ============================================
    // Tests for equals() method
    // ============================================

    @Test
    public void testEquals_SameInstance() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

        assertTrue(array.equals(array));
    }

    @Test
    public void testEquals_EqualArrays() {
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(data);
        ImmutableIntArray array2 = ImmutableIntArray.copyOf(data);

        assertTrue(array1.equals(array2));
        assertTrue(array2.equals(array1));
    }

    @Test
    public void testEquals_DifferentArrays() {
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 4 });

        assertFalse(array1.equals(array2));
    }

    @Test
    public void testEquals_DifferentLengths() {
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        ImmutableIntArray array2 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4 });

        assertFalse(array1.equals(array2));
    }

    @Test
    public void testEquals_EmptyArrays() {
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[0]);
        ImmutableIntArray array2 = ImmutableIntArray.copyOf(null);

        assertTrue(array1.equals(array2));
    }

    @Test
    public void testEquals_Null() {
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });

        assertFalse(array.equals(null));
    }

    @Test
    public void testEquals_DifferentType() {
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
        String notAnArray = "not an array";

        assertFalse(array.equals(notAnArray));
    }

    @Test
    public void testEquals_Transitivity() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(data);
        ImmutableIntArray array2 = ImmutableIntArray.copyOf(data);
        ImmutableIntArray array3 = ImmutableIntArray.unsafeWrap(data.clone());

        assertTrue(array1.equals(array2));
        assertTrue(array2.equals(array3));
        assertTrue(array1.equals(array3));
    }

    // ============================================
    // Tests for toString() method
    // ============================================

    @Test
    public void testToString_ValidArray() {
        int[] data = { 1, 2, 3 };
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

        String result = array.toString();

        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
        assertTrue(result.contains("3"));
    }

    @Test
    public void testToString_EmptyArray() {
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[0]);

        String result = array.toString();

        assertNotNull(result);
        assertTrue(result.contains("[]") || result.length() == 2);
    }

    @Test
    public void testToString_SingleElement() {
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });

        String result = array.toString();

        assertTrue(result.contains("42"));
    }

    @Test
    public void testToString_NegativeNumbers() {
        int[] data = { -1, -2, -3 };
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

        String result = array.toString();

        assertTrue(result.contains("-1"));
        assertTrue(result.contains("-2"));
        assertTrue(result.contains("-3"));
    }

    // ============================================
    // Tests for length field
    // ============================================

    @Test
    public void testLength_EmptyArray() {
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[0]);

        assertEquals(0, array.length());
    }

    @Test
    public void testLength_SingleElement() {
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });

        assertEquals(1, array.length());
    }

    @Test
    public void testLength_MultipleElements() {
        int[] data = new int[100];
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

        assertEquals(100, array.length());
    }

    @Test
    public void testLength_NullInput() {
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(null);

        assertEquals(0, array.length());
    }

    // ============================================
    // Integration and edge case tests
    // ============================================

    @Test
    public void testImmutability_CopyOf() {
        int[] data = { 1, 2, 3, 4, 5 };
        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        // Modify original
        data[0] = 999;
        data[4] = -999;

        // Array should remain unchanged
        assertEquals(1, array.get(0));
        assertEquals(5, array.get(4));
    }

    @Test
    public void testBoundaryValues_MaxInt() {
        int[] data = { Integer.MAX_VALUE, Integer.MIN_VALUE, 0 };
        ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

        assertEquals(Integer.MAX_VALUE, array.get(0));
        assertEquals(Integer.MIN_VALUE, array.get(1));
        assertEquals(0, array.get(2));
    }

    @Test
    public void testLargeArray_Performance() {
        int size = 100000;
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = i;
        }

        ImmutableIntArray array = ImmutableIntArray.copyOf(data);

        assertEquals(size, array.length());
        assertEquals(0, array.get(0));
        assertEquals(size - 1, array.get(size - 1));

        // Test stream on large array
        long sum = array.stream().filter(v -> v % 2 == 0).count();
        assertEquals(size / 2, sum);
    }
}
