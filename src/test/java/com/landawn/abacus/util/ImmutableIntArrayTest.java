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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.stream.IntStream;

public class ImmutableIntArrayTest extends TestBase {

    @Nested
    /**
     * Comprehensive unit tests for ImmutableIntArray.
     * Tests cover all public methods including factory methods, access methods,
     * iteration methods, stream operations, copy methods, and Object methods.
     */
    @Tag("2025")
    class ImmutableIntArray2025Test extends TestBase {

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
        // Tests for isEmpty() method
        // ============================================

        @Test
        public void testIsEmpty_EmptyArray() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[0]);
            assertTrue(array.isEmpty());
        }

        @Test
        public void testIsEmpty_NonEmptyArray() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            assertFalse(array.isEmpty());
        }

        @Test
        public void testIsEmpty_NullInput() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(null);
            assertTrue(array.isEmpty());
        }

        @Test
        public void testIsEmpty_SingleElement() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });
            assertFalse(array.isEmpty());
        }

        // ============================================
        // Tests for length() method
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
        // Tests for contains() method
        // ============================================

        @Test
        public void testContains_ValuePresent() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });
            assertTrue(array.contains(30));
            assertTrue(array.contains(10));
            assertTrue(array.contains(50));
        }

        @Test
        public void testContains_ValueAbsent() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });
            assertFalse(array.contains(99));
            assertFalse(array.contains(0));
            assertFalse(array.contains(-1));
        }

        @Test
        public void testContains_EmptyArray() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[0]);
            assertFalse(array.contains(1));
            assertFalse(array.contains(0));
        }

        @Test
        public void testContains_BoundaryValues() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { Integer.MIN_VALUE, 0, Integer.MAX_VALUE });
            assertTrue(array.contains(Integer.MIN_VALUE));
            assertTrue(array.contains(0));
            assertTrue(array.contains(Integer.MAX_VALUE));
        }

        @Test
        public void testContains_SingleElement() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });
            assertTrue(array.contains(42));
            assertFalse(array.contains(43));
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
        // Tests for subArray() method
        // ============================================

        @Test
        public void testSubArray_FullRange() {
            int[] data = { 1, 2, 3, 4, 5 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            int[] copy = immutable.subArray(0, 5);

            assertEquals(5, copy.length);
            assertEquals(1, copy[0]);
            assertEquals(5, copy[4]);
        }

        @Test
        public void testSubArray_PartialRange() {
            int[] data = { 10, 20, 30, 40, 50 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            int[] copy = immutable.subArray(1, 4);

            assertEquals(3, copy.length);
            assertEquals(20, copy[0]);
            assertEquals(30, copy[1]);
            assertEquals(40, copy[2]);
        }

        @Test
        public void testSubArray_EmptyRange() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            int[] copy = immutable.subArray(1, 1);

            assertEquals(0, copy.length);
        }

        @Test
        public void testSubArray_InvalidFromIndex() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            assertThrows(IndexOutOfBoundsException.class, () -> immutable.subArray(-1, 2));
        }

        @Test
        public void testSubArray_InvalidToIndex() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            assertThrows(IndexOutOfBoundsException.class, () -> immutable.subArray(0, 10));
        }

        @Test
        public void testSubArray_FromIndexGreaterThanToIndex() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            assertThrows(IndexOutOfBoundsException.class, () -> immutable.subArray(2, 1));
        }

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

    @Nested
    class JavadocExampleOtherTest_ImmutableIntArray extends TestBase {
        // ===== ImmutableIntArray examples =====

        @Test
        public void testImmutableIntArrayCopyOfGet() {
            // From class-level Javadoc: "int value = immutable.get(2);   // returns 3"
            int[] data = { 1, 2, 3, 4, 5 };
            ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);
            assertEquals(3, immutable.get(2));
        }

        @Test
        public void testImmutableIntArrayWrapGet() {
            // From unsafeWrap() Javadoc: "System.out.println(array.get(1));   // prints: 20"
            int[] data = new int[] { 10, 20, 30 };
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);
            assertEquals(20, array.get(1));
        }

        @Test
        public void testImmutableIntArrayWrapMutability() {
            // From unsafeWrap() Javadoc: modifying original array affects the wrapper
            int[] data = new int[] { 10, 20, 30 };
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);
            assertEquals(20, array.get(1));

            data[1] = 99;
            assertEquals(99, array.get(1)); // "prints: 99 (not recommended!)"
        }

        @Test
        public void testImmutableIntArrayCopyOfImmutability() {
            // From copyOf() Javadoc: modifying original array does NOT affect the copy
            int[] data = { 10, 20, 30 };
            ImmutableIntArray array = ImmutableIntArray.copyOf(data);
            assertEquals(10, array.get(0));

            data[0] = 99;
            assertEquals(10, array.get(0)); // "still prints: 10"
        }

        @Test
        public void testImmutableIntArrayIsEmpty() {
            // From isEmpty() Javadoc
            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
            assertTrue(empty.isEmpty()); // returns true

            ImmutableIntArray nonEmpty = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            assertFalse(nonEmpty.isEmpty()); // returns false

            ImmutableIntArray fromNull = ImmutableIntArray.unsafeWrap(null);
            assertTrue(fromNull.isEmpty()); // returns true (null becomes empty array)
        }

        @Test
        public void testImmutableIntArrayContains() {
            // From contains() Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });
            assertTrue(array.contains(30)); // returns true
            assertFalse(array.contains(99)); // returns false

            ImmutableIntArray emptyArr = ImmutableIntArray.unsafeWrap(new int[0]);
            assertFalse(emptyArr.contains(1)); // returns false
        }

        @Test
        public void testImmutableIntArrayGet() {
            // From get() Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 5, 10, 15, 20 });
            assertEquals(10, array.get(1)); // returns 10
            assertEquals(5, array.get(0)); // returns 5
            assertEquals(20, array.get(array.length() - 1)); // returns 20
        }

        @Test
        public void testImmutableIntArrayForEachSum() {
            // From forEach() Javadoc: accumulate sum
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
            int[] sum = { 0 };
            array.forEach(value -> sum[0] += value);
            assertEquals(15, sum[0]); // prints: 15
        }

        @Test
        public void testImmutableIntArrayStreamSum() {
            // From stream() Javadoc: "int sum = array.stream().sum();   // returns 15"
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
            int sum = array.stream().sum();
            assertEquals(15, sum);
        }

        @Test
        public void testImmutableIntArrayStreamMax() {
            // From stream() Javadoc: "int max = array.stream().max().orElse(0);   // returns 5"
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
            int max = array.stream().max().orElse(0);
            assertEquals(5, max);
        }

        @Test
        public void testImmutableIntArrayStreamSumOfSquares() {
            // From stream() Javadoc: "int sumOfSquares = ... // returns 55 (1 + 4 + 9 + 16 + 25)"
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
            int sumOfSquares = array.stream().map(x -> x * x).sum();
            assertEquals(55, sumOfSquares);
        }

        @Test
        public void testImmutableIntArraySubArray() {
            // From subArray() Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });

            int[] subArray = array.subArray(1, 4);
            assertArrayEquals(new int[] { 20, 30, 40 }, subArray); // {20, 30, 40} with length 3
            assertEquals(3, subArray.length);

            int[] first3 = array.subArray(0, 3);
            assertArrayEquals(new int[] { 10, 20, 30 }, first3);

            int[] empty = array.subArray(2, 2);
            assertEquals(0, empty.length); // empty array with length 0
        }

        @Test
        public void testImmutableIntArrayHashCodeEquals() {
            // From hashCode() Javadoc: equal arrays have same hash
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });

            assertEquals(array1.hashCode(), array2.hashCode()); // "prints: true"
        }

        @Test
        public void testImmutableIntArrayEquals() {
            // From equals() Javadoc
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });
            ImmutableIntArray array3 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 4 });

            assertTrue(array1.equals(array2)); // prints: true
            assertFalse(array1.equals(array3)); // prints: false
            assertFalse(array1.equals(new int[] { 1, 2, 3 })); // prints: false (different type)
        }

        @Test
        public void testImmutableIntArrayToString() {
            // From toString() Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            assertEquals("[1, 2, 3]", array.toString());

            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
            assertEquals("[]", empty.toString());

            ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] { 42 });
            assertEquals("[42]", single.toString());
        }
    }

    @Nested
    class JavadocExampleUtilsTest_ImmutableIntArray extends TestBase {
        // ==================== ImmutableIntArray Javadoc Examples ====================

        @Test
        public void testImmutableIntArray_unsafeWrap_get() {
            // From unsafeWrap Javadoc
            int[] data = new int[] { 10, 20, 30 };
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);
            assertEquals(20, array.get(1)); // prints: 20

            // CAUTION: Modifying the original array affects the ImmutableIntArray
            data[1] = 99;
            assertEquals(99, array.get(1)); // prints: 99 (not recommended!)
        }

        @Test
        public void testImmutableIntArray_copyOf() {
            // From copyOf Javadoc
            int[] data = { 10, 20, 30 };
            ImmutableIntArray array = ImmutableIntArray.copyOf(data);
            assertEquals(10, array.get(0)); // prints: 10

            // Modifying the original array does NOT affect the ImmutableIntArray
            data[0] = 99;
            assertEquals(10, array.get(0)); // still prints: 10
        }

        @Test
        public void testImmutableIntArray_isEmpty() {
            // From isEmpty Javadoc
            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
            assertTrue(empty.isEmpty()); // returns true

            ImmutableIntArray nonEmpty = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            assertFalse(nonEmpty.isEmpty()); // returns false

            ImmutableIntArray fromNull = ImmutableIntArray.unsafeWrap(null);
            assertTrue(fromNull.isEmpty()); // returns true (null becomes empty array)
        }

        @Test
        public void testImmutableIntArray_length() {
            // From length Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            assertEquals(3, array.length()); // returns 3
        }

        @Test
        public void testImmutableIntArray_contains() {
            // From contains Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });
            assertTrue(array.contains(30)); // returns true
            assertFalse(array.contains(99)); // returns false

            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
            assertFalse(empty.contains(1)); // returns false
        }

        @Test
        public void testImmutableIntArray_get() {
            // From get Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 5, 10, 15, 20 });
            assertEquals(10, array.get(1)); // returns 10
            assertEquals(5, array.get(0)); // returns 5
            assertEquals(20, array.get(array.length() - 1)); // returns 20
        }

        @Test
        public void testImmutableIntArray_forEach_sum() {
            // From forEach Javadoc: Accumulate sum
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
            int[] sum = { 0 };
            array.forEach(value -> sum[0] += value);
            assertEquals(15, sum[0]); // prints: 15
        }

        @Test
        public void testImmutableIntArray_stream_sum() {
            // From stream Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });

            int sum = array.stream().sum(); // returns 15
            assertEquals(15, sum);
        }

        @Test
        public void testImmutableIntArray_stream_max() {
            // From stream Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });

            int max = array.stream().max().orElse(0); // returns 5
            assertEquals(5, max);
        }

        @Test
        public void testImmutableIntArray_stream_filter() {
            // From stream Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });

            int[] evens = array.stream().filter(x -> x % 2 == 0).toArray(); // returns {2, 4}
            assertArrayEquals(new int[] { 2, 4 }, evens);
        }

        @Test
        public void testImmutableIntArray_stream_mapAndSum() {
            // From stream Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });

            int sumOfSquares = array.stream().map(x -> x * x).sum(); // returns 55 (1 + 4 + 9 + 16 + 25)
            assertEquals(55, sumOfSquares);
        }

        @Test
        public void testImmutableIntArray_subArray() {
            // From subArray Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });

            int[] subArray = array.subArray(1, 4);
            assertArrayEquals(new int[] { 20, 30, 40 }, subArray); // contains {20, 30, 40}

            int[] first3 = array.subArray(0, 3);
            assertArrayEquals(new int[] { 10, 20, 30 }, first3); // contains {10, 20, 30}

            int[] empty = array.subArray(2, 2);
            assertEquals(0, empty.length); // empty array with length 0
        }

        @Test
        public void testImmutableIntArray_hashCode_equals() {
            // From hashCode Javadoc
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });

            assertEquals(array1.hashCode(), array2.hashCode()); // equal arrays have same hash
        }

        @Test
        public void testImmutableIntArray_equals() {
            // From equals Javadoc
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });
            ImmutableIntArray array3 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 4 });

            assertTrue(array1.equals(array2)); // true
            assertFalse(array1.equals(array3)); // false
            assertFalse(array1.equals(new int[] { 1, 2, 3 })); // false (different type)
        }

        @Test
        public void testImmutableIntArray_toString() {
            // From toString Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            assertEquals("[1, 2, 3]", array.toString()); // prints: [1, 2, 3]

            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
            assertEquals("[]", empty.toString()); // prints: []

            ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] { 42 });
            assertEquals("[42]", single.toString()); // prints: [42]
        }
    }

}
