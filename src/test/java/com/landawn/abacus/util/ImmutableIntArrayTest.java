package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import com.landawn.abacus.util.stream.IntStream;

class ImmutableIntArrayTest {

    @Nested
    class UnsafeWrapTests {

        @Test
        void property_unsafeWrap_reflectsSourceMutation() {
            final int[] src = { 1, 2, 3 };
            final ImmutableIntArray array = ImmutableIntArray.unsafeWrap(src);

            src[0] = 99; // documented: unsafeWrap shares the backing array

            assertEquals(99, array.get(0), "unsafeWrap intentionally shares the backing array");
        }

        @Test
        void unsafeWrap_asMapKeyBecomesUnreachableAfterMutation() {
            final int[] backing = { 1, 2, 3 };
            final ImmutableIntArray key = ImmutableIntArray.unsafeWrap(backing);
            final java.util.Map<ImmutableIntArray, String> map = new java.util.HashMap<>();
            map.put(key, "v");

            backing[0] = 9;

            assertNull(map.get(key), "mutating the wrapped array strands the entry in its original bucket");
        }

        @Test
        void unsafeWrap_doesNotCopyArray() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            // Modify original array
            data[0] = 99;

            // The immutable array reflects the change (no defensive copy)
            assertEquals(99, immutable.get(0));
        }

        @Test
        void unsafeWrap_equalityFollowsCallerMutation() {
            final int[] backing = { 1, 2, 3 };
            final ImmutableIntArray a = ImmutableIntArray.unsafeWrap(backing);
            final ImmutableIntArray b = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });
            assertEquals(a, b);

            backing[0] = 9;

            assertNotEquals(a, b);
        }

        @Test
        void unsafeWrap_get() {
            // From unsafeWrap Javadoc
            int[] data = new int[] { 10, 20, 30 };
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);
            assertEquals(20, array.get(1)); // prints: 20

            // CAUTION: Modifying the original array affects the ImmutableIntArray
            data[1] = 99;
            assertEquals(99, array.get(1)); // prints: 99 (not recommended!)
        }

        @Test
        void unsafeWrap_hashCodeFollowsCallerMutation() {
            final int[] backing = { 1, 2, 3 };
            final ImmutableIntArray wrapped = ImmutableIntArray.unsafeWrap(backing);
            final int before = wrapped.hashCode();

            backing[0] = 9;

            assertNotEquals(before, wrapped.hashCode(), "hash is content-derived, so it tracks the wrapped array");
        }

        @Test
        void unsafeWrap_mutationCanPreserveHashCode() {
            final int[] backing = { 0, 31 };
            final ImmutableIntArray wrapped = ImmutableIntArray.unsafeWrap(backing);
            final ImmutableIntArray original = ImmutableIntArray.copyOf(backing);

            backing[0] = 1;
            backing[1] = 0;

            assertNotEquals(original, wrapped);
            assertEquals(original.hashCode(), wrapped.hashCode());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void unsafeWrap_nullOrEmpty_returnsEmptyValue(int[] input) {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(input);

            assertTrue(array.isEmpty());
            assertEquals(0, array.length());
            assertArrayEquals(new int[0], array.stream().toArray());
        }

        @Test
        void unsafeWrap_withValidArray() {
            int[] data = { 1, 2, 3, 4, 5 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            assertEquals(5, immutable.length());
            assertEquals(1, immutable.get(0));
            assertEquals(5, immutable.get(4));
        }

        @Test
        void unsafeWrapGet() {
            // From unsafeWrap() Javadoc: "System.out.println(array.get(1));   // prints: 20"
            int[] data = new int[] { 10, 20, 30 };
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);
            assertEquals(20, array.get(1));
        }
    }

    @Nested
    class CopyOfTests {

        @Test
        void copyOf() {
            // From copyOf Javadoc
            int[] data = { 10, 20, 30 };
            ImmutableIntArray array = ImmutableIntArray.copyOf(data);
            assertEquals(10, array.get(0)); // prints: 10

            // Modifying the original array does NOT affect the ImmutableIntArray
            data[0] = 99;
            assertEquals(10, array.get(0)); // still prints: 10
        }

        @Test
        void copyOf_asMapKeyStaysReachable() {
            final int[] backing = { 1, 2, 3 };
            final ImmutableIntArray key = ImmutableIntArray.copyOf(backing);
            final java.util.Map<ImmutableIntArray, String> map = new java.util.HashMap<>();
            map.put(key, "v");

            backing[0] = 9;

            assertEquals("v", map.get(key));
        }

        @Test
        void copyOf_createsDefensiveCopy() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);

            // Modify original array
            data[0] = 99;

            // The immutable array is not affected (defensive copy was made)
            assertEquals(1, immutable.get(0));
        }

        @Test
        void copyOf_isUnaffectedByCallerMutation() {
            final int[] backing = { 1, 2, 3 };
            final ImmutableIntArray snapshot = ImmutableIntArray.copyOf(backing);
            final int before = snapshot.hashCode();

            backing[0] = 9;

            assertEquals(before, snapshot.hashCode());
            assertArrayEquals(new int[] { 1, 2, 3 }, snapshot.copyOfRange(0, snapshot.length()));
        }

        @Test
        void copyOf_largeArray() {
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

        @Test
        void copyOf_largeInput_preservesValuesAndTraversal() {
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

        @ParameterizedTest
        @NullAndEmptySource
        void copyOf_nullOrEmpty_returnsEmptyValue(int[] input) {
            ImmutableIntArray array = ImmutableIntArray.copyOf(input);

            assertTrue(array.isEmpty());
            assertEquals(0, array.length());
            assertArrayEquals(new int[0], array.stream().toArray());
        }

        @Test
        void copyOf_withValidArray() {
            int[] data = { 10, 20, 30 };
            ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);

            assertEquals(3, immutable.length());
            assertEquals(10, immutable.get(0));
            assertEquals(30, immutable.get(2));
        }

        @Test
        void copyOfGet() {
            // From class-level Javadoc: "int value = immutable.get(2);   // returns 3"
            int[] data = { 1, 2, 3, 4, 5 };
            ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);
            assertEquals(3, immutable.get(2));
        }

        @Test
        void immutability_copyOf() {
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
        void property_copyOf_isolatesFromSourceMutation() {
            final int[] src = { 1, 2, 3, 4, 5 };
            final ImmutableIntArray array = ImmutableIntArray.copyOf(src);

            java.util.Arrays.fill(src, 0); // mutate the source after copying

            assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, array.copyOfRange(0, array.length()), "copyOf must take a defensive snapshot");
        }
    }

    @Nested
    class IsEmptyTests {

        @Test
        void isEmpty() {
            // From isEmpty() Javadoc
            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
            assertTrue(empty.isEmpty()); // returns true

            ImmutableIntArray nonEmpty = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            assertFalse(nonEmpty.isEmpty()); // returns false

            ImmutableIntArray fromNull = ImmutableIntArray.unsafeWrap(null);
            assertTrue(fromNull.isEmpty()); // returns true (null becomes empty array)
        }

        @Test
        void isEmpty_nonEmptyArray() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            assertFalse(array.isEmpty());
        }

        @Test
        void isEmpty_singleElement() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });
            assertFalse(array.isEmpty());
        }
    }

    @Nested
    class LengthTests {

        @Test
        void length() {
            // From length Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            assertEquals(3, array.length()); // returns 3
        }

        @Test
        void length_multipleElements() {
            int[] data = new int[100];
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

            assertEquals(100, array.length());
        }

        @Test
        void length_singleElement() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });

            assertEquals(1, array.length());
        }
    }

    @Nested
    class ContainsTests {

        @Test
        void contains() {
            // From contains() Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });
            assertTrue(array.contains(30)); // returns true
            assertFalse(array.contains(99)); // returns false

            ImmutableIntArray emptyArr = ImmutableIntArray.unsafeWrap(new int[0]);
            assertFalse(emptyArr.contains(1)); // returns false
        }

        @Test
        void contains_boundaryValues() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { Integer.MIN_VALUE, 0, Integer.MAX_VALUE });
            assertTrue(array.contains(Integer.MIN_VALUE));
            assertTrue(array.contains(0));
            assertTrue(array.contains(Integer.MAX_VALUE));
        }

        @Test
        void contains_emptyArray() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[0]);
            assertFalse(array.contains(1));
            assertFalse(array.contains(0));
        }

        @Test
        void contains_singleElement() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });
            assertTrue(array.contains(42));
            assertFalse(array.contains(43));
        }

        @Test
        void contains_valueAbsent() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });
            assertFalse(array.contains(99));
            assertFalse(array.contains(0));
            assertFalse(array.contains(-1));
        }

        @Test
        void contains_valuePresent() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });
            assertTrue(array.contains(30));
            assertTrue(array.contains(10));
            assertTrue(array.contains(50));
        }

        @Test
        void contains_values_ImmutableIntArray_unsafeWrap() {
            // From contains Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });
            assertTrue(array.contains(30)); // returns true
            assertFalse(array.contains(99)); // returns false

            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
            assertFalse(empty.contains(1)); // returns false
        }
    }

    @Nested
    class MinTests {

        @Test
        void min_allNegative() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { -5, -10, -1, -100 });
            assertEquals(-100, array.min());
        }

        @Test
        void min_boundaryValues() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { Integer.MAX_VALUE, 0, Integer.MIN_VALUE });
            assertEquals(Integer.MIN_VALUE, array.min());
        }

        @Test
        void min_emptyArrayThrows() {
            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
            assertThrows(java.util.NoSuchElementException.class, empty::min);
        }

        @Test
        void min_nullInputThrows() {
            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
            assertThrows(java.util.NoSuchElementException.class, empty::min);
        }

        @Test
        void min_singleElement() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });
            assertEquals(42, array.min());
        }

        @Test
        void min_typicalValues() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 3, 1, 4, 1, 5, 9, 2, 6 });
            assertEquals(1, array.min());
        }
    }

    @Nested
    class MaxTests {

        @Test
        void boundaryValues_maxInt() {
            int[] data = { Integer.MAX_VALUE, Integer.MIN_VALUE, 0 };
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

            assertEquals(Integer.MAX_VALUE, array.get(0));
            assertEquals(Integer.MIN_VALUE, array.get(1));
            assertEquals(0, array.get(2));
        }

        @Test
        void max_allNegative() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { -5, -10, -1, -100 });
            assertEquals(-1, array.max());
        }

        @Test
        void max_boundaryValues() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { Integer.MIN_VALUE, 0, Integer.MAX_VALUE });
            assertEquals(Integer.MAX_VALUE, array.max());
        }

        @Test
        void max_emptyArrayThrows() {
            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
            assertThrows(java.util.NoSuchElementException.class, empty::max);
        }

        @Test
        void max_nullInputThrows() {
            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
            assertThrows(java.util.NoSuchElementException.class, empty::max);
        }

        @Test
        void max_singleElement() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });
            assertEquals(42, array.max());
        }

        @Test
        void max_typicalValues() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 3, 1, 4, 1, 5, 9, 2, 6 });
            assertEquals(9, array.max());
        }
    }

    @Nested
    class SumTests {

        @Test
        void sum_emptyArrayReturnsZero() {
            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
            assertEquals(0, empty.sum());
        }

        @Test
        void sum_intermediateOverflowCanCancel() {
            assertEquals(Integer.MAX_VALUE, ImmutableIntArray.copyOf(new int[] { Integer.MAX_VALUE, 1, -1 }).sum());
            assertEquals(Integer.MIN_VALUE, ImmutableIntArray.copyOf(new int[] { Integer.MIN_VALUE, -1, 1 }).sum());
        }

        @Test
        void sum_negativeAndPositive() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { -10, 5, -3, 8 });
            assertEquals(0, array.sum());
        }

        @Test
        void sum_nullInputReturnsZero() {
            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
            assertEquals(0, empty.sum());
        }

        @Test
        void sum_overflowThrows() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { Integer.MAX_VALUE, 1 });
            assertThrows(ArithmeticException.class, array::sum);
        }

        @Test
        void sum_singleElement() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });
            assertEquals(42, array.sum());
        }

        @Test
        void sum_typicalValues() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
            assertEquals(15, array.sum());
        }
    }

    @Nested
    class AverageTests {

        @Test
        void average_allSameValue() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 7, 7, 7, 7, 7 });
            assertEquals(7.0d, array.average().getAsDouble(), 0.0);
        }

        @Test
        void average_emptyArrayReturnsEmpty() {
            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(new int[0]);
            assertTrue(empty.average().isEmpty());
        }

        @Test
        void average_negativeValues() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { -2, -4, -6 });
            assertEquals(-4.0d, array.average().getAsDouble(), 0.0);
        }

        @Test
        void average_noOverflowOnLargeValues() {
            // sum() would overflow int, but average() uses long internally, so it should not
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE });
            assertEquals((double) Integer.MAX_VALUE, array.average().getAsDouble(), 0.0);
        }

        @Test
        void average_nullInputReturnsEmpty() {
            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
            assertTrue(empty.average().isEmpty());
        }

        @Test
        void average_singleElement() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });
            assertEquals(42.0d, array.average().getAsDouble(), 0.0);
        }

        @Test
        void average_typicalValues() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4 });
            assertEquals(2.5d, array.average().getAsDouble(), 0.0);
        }

        @Test
        void average_zeroMeanIsPresent() {
            final ImmutableIntArray balanced = ImmutableIntArray.copyOf(new int[] { -1, 1 });
            assertTrue(balanced.average().isPresent());
            assertEquals(0.0, balanced.average().getAsDouble());
        }
    }

    @Nested
    class GetTests {

        @Test
        void get() {
            // From get() Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 5, 10, 15, 20 });
            assertEquals(10, array.get(1)); // returns 10
            assertEquals(5, array.get(0)); // returns 5
            assertEquals(20, array.get(array.length() - 1)); // returns 20
        }

        @Test
        void get_emptyArray() {
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[0]);

            assertThrows(IndexOutOfBoundsException.class, () -> immutable.get(0));
        }

        @Test
        void get_indexOutOfBounds() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            assertThrows(IndexOutOfBoundsException.class, () -> immutable.get(3));
            assertThrows(IndexOutOfBoundsException.class, () -> immutable.get(100));
        }

        @Test
        void get_negativeIndex() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            assertThrows(IndexOutOfBoundsException.class, () -> immutable.get(-1));
        }

        @Test
        void get_throwsArrayIndexOutOfBoundsByDesign() {
            // By design, get(index) is a bare array access (no N.checkElementIndex call), so an
            // out-of-range index surfaces as ArrayIndexOutOfBoundsException.
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });

            for (int badIndex : new int[] { -1, 3, 100 }) {
                assertThrows(ArrayIndexOutOfBoundsException.class, () -> immutable.get(badIndex));
            }
        }

        @Test
        void get_validIndex() {
            int[] data = { 5, 10, 15, 20, 25 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            assertEquals(5, immutable.get(0));
            assertEquals(10, immutable.get(1));
            assertEquals(15, immutable.get(2));
            assertEquals(20, immutable.get(3));
            assertEquals(25, immutable.get(4));
        }
    }

    @Nested
    class ForEachTests {

        @Test
        void forEach_emptyArray() {
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[0]);

            List<Integer> result = new ArrayList<>();
            immutable.forEach(v -> result.add(v));

            assertEquals(0, result.size());
        }

        @Test
        void forEach_nullAction() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            assertThrows(IllegalArgumentException.class, () -> immutable.forEach(null));
        }

        @Test
        void forEach_orderPreserved() {
            int[] data = { 10, 20, 30, 40, 50 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            List<Integer> result = new ArrayList<>();
            immutable.forEach(v -> result.add(v));

            for (int i = 0; i < data.length; i++) {
                assertEquals(data[i], result.get(i));
            }
        }

        @Test
        void forEach_propagatesCheckedExceptionAndStops() {
            final ImmutableIntArray array = ImmutableIntArray.copyOf(new int[] { 10, 20, 30 });
            final IOException failure = new IOException("stop at the second element");
            final List<Integer> visited = new ArrayList<>();

            assertSame(failure, assertThrows(IOException.class, () -> array.forEach(value -> {
                visited.add(value);
                if (value == 20) {
                    throw failure;
                }
            })));
            assertEquals(List.of(10, 20), visited);
        }

        @Test
        void forEach_validArray() {
            int[] data = { 1, 2, 3, 4, 5 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            List<Integer> result = new ArrayList<>();
            immutable.forEach(v -> result.add(v));

            assertEquals(5, result.size());
            assertEquals(1, result.get(0));
            assertEquals(5, result.get(4));
        }

        @Test
        void forEach_withSideEffects() {
            int[] data = { 1, 2, 3, 4, 5 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            AtomicInteger sum = new AtomicInteger(0);
            immutable.forEach(v -> sum.addAndGet(v));

            assertEquals(15, sum.get());
        }

        @Test
        void forEachSum() {
            // From forEach() Javadoc: accumulate sum
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
            int[] sum = { 0 };
            array.forEach(value -> sum[0] += value);
            assertEquals(15, sum[0]); // prints: 15
        }

        @Test
        void unsafeTraversalObservesChangedValuesWithinFixedBounds() {
            final int[] backing = { 1, 2, 3 };
            final ImmutableIntArray array = ImmutableIntArray.unsafeWrap(backing);
            final ImmutableIntArray snapshot = array.subArray(0, array.length());
            final int[] visited = new int[backing.length];
            final int[] nextIndex = { 0 };

            array.forEach(value -> {
                visited[nextIndex[0]++] = value;
                backing[1] = 20;
            });

            assertArrayEquals(new int[] { 1, 20, 3 }, visited);
            assertEquals(3, nextIndex[0]);
            assertEquals(3, array.length());
            assertFalse(array.isEmpty());
            assertTrue(array.contains(20));
            assertFalse(array.contains(2));

            array.forEachIndexed((index, value) -> {
                visited[index] = value;
                backing[2] = 30;
            });

            assertArrayEquals(new int[] { 1, 20, 30 }, visited);
            assertArrayEquals(new int[] { 1, 2, 3 }, snapshot.stream().toArray());
            assertEquals(1, array.min());
            assertEquals(30, array.max());
        }
    }

    @Nested
    class ForEachIndexedTests {

        @Test
        void forEachIndexed_emptyArray() {
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[0]);

            List<String> result = new ArrayList<>();
            immutable.forEachIndexed((index, value) -> result.add(index + ":" + value));

            assertEquals(0, result.size());
        }

        @Test
        void forEachIndexed_nullAction() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            assertThrows(IllegalArgumentException.class, () -> immutable.forEachIndexed(null));
        }

        @Test
        void forEachIndexed_orderPreserved() {
            int[] data = { 5, 10, 15, 20 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            AtomicInteger expectedIndex = new AtomicInteger(0);
            immutable.forEachIndexed((index, value) -> {
                assertEquals(expectedIndex.get(), index);
                expectedIndex.incrementAndGet();
            });
        }

        @Test
        void forEachIndexed_propagatesCheckedExceptionAndStops() {
            final ImmutableIntArray array = ImmutableIntArray.copyOf(new int[] { 10, 20, 30 });
            final IOException failure = new IOException("stop at index one");
            final List<String> visited = new ArrayList<>();

            assertSame(failure, assertThrows(IOException.class, () -> array.forEachIndexed((index, value) -> {
                visited.add(index + ":" + value);
                if (index == 1) {
                    throw failure;
                }
            })));
            assertEquals(List.of("0:10", "1:20"), visited);
        }

        @Test
        void forEachIndexed_validArray() {
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
        void forEachIndexed_verifyIndices() {
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
    }

    @Nested
    class StreamTests {

        @Test
        void emptyCopiesKeepIndependentStreamLifecycles() {
            final ImmutableIntArray first = ImmutableIntArray.copyOf(new int[0]);
            final ImmutableIntArray second = ImmutableIntArray.copyOf(new int[0]);
            assertEquals(first, second);
            assertEquals(first.hashCode(), second.hashCode());

            final var closedStream = first.stream();
            final var liveStream = first.stream();
            closedStream.close();

            assertEquals(0, liveStream.count());
            assertEquals(0, second.stream().count());
            assertEquals(0, first.sum());
            assertTrue(first.average().isEmpty());
            assertThrows(IllegalArgumentException.class, () -> first.forEach(null));
            assertThrows(IllegalArgumentException.class, () -> first.forEachIndexed(null));
        }

        @Test
        void property_stream_isolatedAfterCopyOf() {
            final int[] src = { 1, 2, 3 };
            final ImmutableIntArray array = ImmutableIntArray.copyOf(src);

            java.util.Arrays.fill(src, 0); // mutate the source after copying

            assertArrayEquals(new int[] { 1, 2, 3 }, array.stream().toArray(), "stream over a copyOf snapshot must be isolated from the source");
        }

        @Test
        void property_stream_sharesBackingAfterUnsafeWrap() {
            final int[] src = { 1, 2, 3 };
            final ImmutableIntArray array = ImmutableIntArray.unsafeWrap(src);

            src[0] = 99; // documented: stream() is constructed directly over the backing array

            assertArrayEquals(new int[] { 99, 2, 3 }, array.stream().toArray(), "stream over an unsafeWrap view must observe source mutations");
        }

        @Test
        void stream_emptyArray() {
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[0]);

            IntStream stream = immutable.stream();

            assertEquals(0, stream.count());
        }

        @Test
        void stream_filter() {
            int[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            long count = immutable.stream().filter(v -> v % 2 == 0).count();

            assertEquals(5, count);
        }

        @Test
        void stream_filter_values_ImmutableIntArray_unsafeWrap() {
            // From stream Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });

            int[] evens = array.stream().filter(x -> x % 2 == 0).toArray(); // returns {2, 4}
            assertArrayEquals(new int[] { 2, 4 }, evens);
        }

        @Test
        void stream_independentLifecycleForEmptyAndNonEmptyArrays() {
            for (final int[] data : new int[][] { {}, { 3, 1, 2 } }) {
                final ImmutableIntArray immutable = ImmutableIntArray.copyOf(data);
                final IntStream first = immutable.stream();
                final IntStream second = immutable.stream();

                assertNotSame(first, second);
                first.close();
                assertThrows(IllegalStateException.class, first::count);
                assertArrayEquals(data, second.toArray());
                assertThrows(IllegalStateException.class, second::count);
                assertArrayEquals(data, immutable.stream().toArray());
            }
        }

        @Test
        void stream_map() {
            int[] data = { 1, 2, 3, 4, 5 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            int sum = immutable.stream().map(v -> v * 2).sum();

            assertEquals(30, sum);
        }

        @Test
        void stream_max() {
            int[] data = { 10, 50, 30, 20, 40 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            int max = immutable.stream().max().orElse(0);

            assertEquals(50, max);
        }

        @Test
        void stream_min() {
            int[] data = { 30, 10, 50, 20, 40 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            int min = immutable.stream().min().orElse(0);

            assertEquals(10, min);
        }

        @Test
        void stream_toArray() {
            int[] data = { 5, 4, 3, 2, 1 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            int[] result = immutable.stream().sorted().toArray();

            assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, result);
        }

        @Test
        void stream_validArray() {
            int[] data = { 1, 2, 3, 4, 5 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            IntStream stream = immutable.stream();

            int sum = stream.sum();
            assertEquals(15, sum);
        }

        @Test
        void streamMax() {
            // From stream() Javadoc: "int max = array.stream().max().orElse(0);   // returns 5"
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
            int max = array.stream().max().orElse(0);
            assertEquals(5, max);
        }

        @Test
        void streamSum() {
            // From stream() Javadoc: "int sum = array.stream().sum();   // returns 15"
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
            int sum = array.stream().sum();
            assertEquals(15, sum);
        }

        @Test
        void streamSumOfSquares() {
            // From stream() Javadoc: "int sumOfSquares = ... // returns 55 (1 + 4 + 9 + 16 + 25)"
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4, 5 });
            int sumOfSquares = array.stream().map(x -> x * x).sum();
            assertEquals(55, sumOfSquares);
        }
    }

    @Nested
    class SubArrayTests {

        @Test
        void allFactoriesAndSubrangesUseTheBackingArrayBounds() {
            for (final int[] input : new int[][] { null, {}, { 7 }, { Integer.MIN_VALUE, 0, Integer.MAX_VALUE, -1 } }) {
                final int[] expected = input == null ? new int[0] : input;

                for (final ImmutableIntArray array : new ImmutableIntArray[] { new ImmutableIntArray(input), ImmutableIntArray.unsafeWrap(input),
                        ImmutableIntArray.copyOf(input) }) {
                    assertEquals(expected.length, array.length());
                    assertEquals(expected.length == 0, array.isEmpty());

                    for (int from = 0; from <= expected.length; from++) {
                        for (int to = from; to <= expected.length; to++) {
                            final int[] range = java.util.Arrays.copyOfRange(expected, from, to);
                            final ImmutableIntArray subArray = array.subArray(from, to);
                            final int[] visited = new int[range.length];
                            final int[] nextIndex = { 0 };

                            assertEquals(range.length, subArray.length());
                            assertEquals(range.length == 0, subArray.isEmpty());
                            assertArrayEquals(range, array.copyOfRange(from, to));
                            assertArrayEquals(range, subArray.stream().toArray());
                            subArray.forEachIndexed((index, value) -> {
                                assertEquals(nextIndex[0]++, index);
                                visited[index] = value;
                            });
                            assertEquals(range.length, nextIndex[0]);
                            assertArrayEquals(range, visited);
                            assertThrows(ArrayIndexOutOfBoundsException.class, () -> subArray.get(range.length));
                        }
                    }

                    assertThrows(IndexOutOfBoundsException.class, () -> array.subArray(0, expected.length + 1));
                    assertThrows(IndexOutOfBoundsException.class, () -> array.copyOfRange(0, expected.length + 1));
                }
            }
        }

        @Test
        void subArray_fullAndEmptyRanges() {
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });

            assertEquals(immutable, immutable.subArray(0, immutable.length()));
            assertTrue(immutable.subArray(1, 1).isEmpty());
        }

        @Test
        void subArray_invalidRange() {
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });

            assertThrows(IndexOutOfBoundsException.class, () -> immutable.subArray(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> immutable.subArray(0, 4));
            assertThrows(IndexOutOfBoundsException.class, () -> immutable.subArray(2, 1));
        }

        @Test
        void subArray_partialRange() {
            int[] data = { 10, 20, 30, 40, 50 };
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(data);

            ImmutableIntArray subArray = immutable.subArray(1, 4);

            assertArrayEquals(new int[] { 20, 30, 40 }, subArray.stream().toArray());
            data[2] = 99;
            assertEquals(30, subArray.get(1));
        }
    }

    @Nested
    class CopyOfRangeTests {

        @Test
        void copyOfRange() {
            // From copyOfRange() Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });

            int[] range = array.copyOfRange(1, 4);
            assertArrayEquals(new int[] { 20, 30, 40 }, range); // {20, 30, 40} with length 3
            assertEquals(3, range.length);

            int[] first3 = array.copyOfRange(0, 3);
            assertArrayEquals(new int[] { 10, 20, 30 }, first3);

            int[] empty = array.copyOfRange(2, 2);
            assertEquals(0, empty.length); // empty array with length 0
        }

        @SuppressWarnings("deprecation")
        @Test
        void copyOfRange_compatibilityAlias() {
            ImmutableIntArray immutable = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });

            assertArrayEquals(new int[] { 20, 30, 40 }, immutable.copyOfRange(1, 4));
            assertThrows(IndexOutOfBoundsException.class, () -> immutable.copyOfRange(0, 6));
        }

        @Test
        void copyOfRange_values_ImmutableIntArray_unsafeWrap() {
            // From copyOfRange Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 10, 20, 30, 40, 50 });

            int[] range = array.copyOfRange(1, 4);
            assertArrayEquals(new int[] { 20, 30, 40 }, range); // contains {20, 30, 40}

            int[] first3 = array.copyOfRange(0, 3);
            assertArrayEquals(new int[] { 10, 20, 30 }, first3); // contains {10, 20, 30}

            int[] empty = array.copyOfRange(2, 2);
            assertEquals(0, empty.length); // empty array with length 0
        }

        @Test
        void property_copyOfRange_returnsIndependentCopy() {
            final ImmutableIntArray array = ImmutableIntArray.copyOf(new int[] { 10, 20, 30, 40 });
            final int[] range = array.copyOfRange(1, 4);
            java.util.Arrays.fill(range, -1);
            // Mutating the returned range must not affect the immutable instance.
            assertArrayEquals(new int[] { 10, 20, 30, 40 }, array.copyOfRange(0, 4));
        }
    }

    @Nested
    class HashCodeTests {

        @Test
        void hashCode_consistency() {
            int[] data = { 10, 20, 30 };
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

            int hash1 = array.hashCode();
            int hash2 = array.hashCode();

            assertEquals(hash1, hash2);
        }

        @Test
        void hashCode_differentArrays() {
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            ImmutableIntArray array2 = ImmutableIntArray.unsafeWrap(new int[] { 4, 5, 6 });

            assertNotEquals(array1.hashCode(), array2.hashCode());
        }

        @Test
        void hashCode_emptyArrays() {
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[0]);
            ImmutableIntArray array2 = ImmutableIntArray.copyOf(null);

            assertEquals(array1.hashCode(), array2.hashCode());
        }

        @Test
        void hashCode_equalArrays() {
            int[] data = { 1, 2, 3, 4, 5 };
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(data);
            ImmutableIntArray array2 = ImmutableIntArray.copyOf(data);

            assertEquals(array1.hashCode(), array2.hashCode());
        }

        @Test
        void hashCodeEquals() {
            // From hashCode() Javadoc: equal arrays have same hash
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });

            assertEquals(array1.hashCode(), array2.hashCode()); // "prints: true"
        }
    }

    @Nested
    class EqualsTests {

        @Test
        void equals() {
            // From equals() Javadoc
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            ImmutableIntArray array2 = ImmutableIntArray.copyOf(new int[] { 1, 2, 3 });
            ImmutableIntArray array3 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 4 });

            assertTrue(array1.equals(array2)); // prints: true
            assertFalse(array1.equals(array3)); // prints: false
            assertFalse(array1.equals(new int[] { 1, 2, 3 })); // prints: false (different type)
        }

        @Test
        void equals_differentArrays() {
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            ImmutableIntArray array2 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 4 });

            assertFalse(array1.equals(array2));
        }

        @Test
        void equals_differentLengths() {
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            ImmutableIntArray array2 = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3, 4 });

            assertFalse(array1.equals(array2));
        }

        @Test
        void equals_differentType() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            String notAnArray = "not an array";

            assertFalse(array.equals(notAnArray));
        }

        @Test
        void equals_emptyArrays() {
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(new int[0]);
            ImmutableIntArray array2 = ImmutableIntArray.copyOf(null);

            assertTrue(array1.equals(array2));
        }

        @Test
        void equals_equalArrays() {
            int[] data = { 1, 2, 3, 4, 5 };
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(data);
            ImmutableIntArray array2 = ImmutableIntArray.copyOf(data);

            assertTrue(array1.equals(array2));
            assertTrue(array2.equals(array1));
        }

        @Test
        void equals_null() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });

            assertFalse(array.equals(null));
        }

        @Test
        void equals_sameInstance() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

            assertTrue(array.equals(array));
        }

        @Test
        void equals_transitivity() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray array1 = ImmutableIntArray.unsafeWrap(data);
            ImmutableIntArray array2 = ImmutableIntArray.copyOf(data);
            ImmutableIntArray array3 = ImmutableIntArray.unsafeWrap(data.clone());

            assertTrue(array1.equals(array2));
            assertTrue(array2.equals(array3));
            assertTrue(array1.equals(array3));
        }
    }

    @Nested
    class ToStringTests {

        @Test
        void toString_formatsValue() {
            // From toString Javadoc
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 1, 2, 3 });
            assertEquals("[1, 2, 3]", array.toString()); // prints: [1, 2, 3]

            ImmutableIntArray empty = ImmutableIntArray.unsafeWrap(null);
            assertEquals("[]", empty.toString()); // prints: []

            ImmutableIntArray single = ImmutableIntArray.unsafeWrap(new int[] { 42 });
            assertEquals("[42]", single.toString()); // prints: [42]
        }

        @Test
        void toString_emptyArray() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[0]);

            String result = array.toString();

            assertNotNull(result);
            assertTrue(result.contains("[]") || result.length() == 2);
        }

        @Test
        void toString_negativeNumbers() {
            int[] data = { -1, -2, -3 };
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

            String result = array.toString();

            assertTrue(result.contains("-1"));
            assertTrue(result.contains("-2"));
            assertTrue(result.contains("-3"));
        }

        @Test
        void toString_singleElement() {
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(new int[] { 42 });

            String result = array.toString();

            assertTrue(result.contains("42"));
        }

        @Test
        void toString_validArray() {
            int[] data = { 1, 2, 3 };
            ImmutableIntArray array = ImmutableIntArray.unsafeWrap(data);

            String result = array.toString();

            assertNotNull(result);
            assertTrue(result.contains("1"));
            assertTrue(result.contains("2"));
            assertTrue(result.contains("3"));
        }
    }

    @Nested
    class ContractTests {

        @Test
        void emptyWrappers_shareStableHash() {
            final ImmutableIntArray fromNull = ImmutableIntArray.unsafeWrap(null);
            final ImmutableIntArray fromEmpty = ImmutableIntArray.unsafeWrap(new int[0]);

            assertEquals(fromNull.hashCode(), fromEmpty.hashCode());
            assertEquals(fromNull, fromEmpty);
        }

        @Test
        void property_aggregatesAndAccessorsAreConsistent() {
            final java.util.Random r = new java.util.Random(20260516L);

            for (int it = 0; it < 300; it++) {
                final int len = 1 + r.nextInt(64); // non-empty
                final int[] data = randomData(r, len);
                final ImmutableIntArray array = ImmutableIntArray.copyOf(data);

                long expectedSum = 0;
                int expectedMin = data[0];
                int expectedMax = data[0];
                for (final int v : data) {
                    expectedSum += v;
                    expectedMin = Math.min(expectedMin, v);
                    expectedMax = Math.max(expectedMax, v);
                }

                assertEquals(len, array.length());
                assertFalse(array.isEmpty());
                assertEquals((int) expectedSum, array.sum());
                assertEquals(expectedMin, array.min());
                assertEquals(expectedMax, array.max());
                assertEquals(expectedSum / (double) len, array.average().getAsDouble(), 1e-9);

                // get(i), stream(), copyOfRange(0,len) and forEach must all agree.
                assertArrayEquals(data, array.stream().toArray());
                assertArrayEquals(data, array.copyOfRange(0, len));
                for (int i = 0; i < len; i++) {
                    assertEquals(data[i], array.get(i));
                }
                final int[] viaForEach = new int[len];
                final int[] idx = { 0 };
                array.forEach(v -> viaForEach[idx[0]++] = v);
                assertArrayEquals(data, viaForEach);
            }
        }

        @Test
        void property_concurrentReadsAreConsistent() throws Exception {
            final java.util.Random r = new java.util.Random(7L);
            final int[] data = randomData(r, 1000);
            final ImmutableIntArray array = ImmutableIntArray.copyOf(data);

            long expectedSum = 0;
            int expectedMin = data[0];
            int expectedMax = data[0];
            for (final int v : data) {
                expectedSum += v;
                expectedMin = Math.min(expectedMin, v);
                expectedMax = Math.max(expectedMax, v);
            }
            final int finalSum = (int) expectedSum;
            final int finalMin = expectedMin;
            final int finalMax = expectedMax;

            final int threads = 16;
            final java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newFixedThreadPool(threads);
            try {
                final java.util.concurrent.CountDownLatch start = new java.util.concurrent.CountDownLatch(1);
                final java.util.List<java.util.concurrent.Future<Boolean>> futures = new ArrayList<>();

                for (int t = 0; t < threads; t++) {
                    futures.add(pool.submit(() -> {
                        start.await();
                        for (int i = 0; i < 2000; i++) {
                            if (array.sum() != finalSum || array.min() != finalMin || array.max() != finalMax) {
                                return Boolean.FALSE;
                            }
                            if (!java.util.Arrays.equals(data, array.stream().toArray())) {
                                return Boolean.FALSE;
                            }
                        }
                        return Boolean.TRUE;
                    }));
                }

                start.countDown();
                for (final java.util.concurrent.Future<Boolean> f : futures) {
                    assertTrue(f.get(30, java.util.concurrent.TimeUnit.SECONDS), "concurrent reads of an immutable instance must stay consistent");
                }
            } finally {
                pool.shutdownNow();
            }
        }

        @Test
        void property_emptyAggregatesAndExceptions() {
            final ImmutableIntArray empty = ImmutableIntArray.copyOf(new int[0]);
            assertTrue(empty.isEmpty());
            assertEquals(0, empty.length());
            assertEquals(0, empty.sum());
            assertTrue(empty.average().isEmpty());
            assertThrows(java.util.NoSuchElementException.class, empty::min);
            assertThrows(java.util.NoSuchElementException.class, empty::max);
        }
    }

    private int[] randomData(final java.util.Random r, final int len) {
        final int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = r.nextInt(2001) - 1000;
        }
        return a;
    }
}
