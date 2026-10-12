package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

class ImmutableIntArrayFullReviewRegressionTest extends TestBase {

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
}
