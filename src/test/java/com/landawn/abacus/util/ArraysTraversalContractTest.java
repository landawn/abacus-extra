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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

class ArraysTraversalContractTest extends TestBase {

    @Test
    void updateAllVisitsSharedRowsAtEveryPosition() {
        final boolean[] flags = { true, false };
        final boolean[][] grid = { flags, null, flags };
        final List<Boolean> visitedFlags = new ArrayList<>();

        Arrays.updateAll(grid, value -> {
            visitedFlags.add(value);
            return !value;
        });

        assertEquals(List.of(true, false, false, true), visitedFlags);
        assertArrayEquals(new boolean[] { true, false }, flags);
        assertSame(grid[0], grid[2]);

        final Integer[] row = { 1, 2 };
        final Integer[][][] cube = { { row }, null, { row } };
        final List<Integer> visitedNumbers = new ArrayList<>();

        Arrays.fff.updateAll(cube, value -> {
            visitedNumbers.add(value);
            return value * 10;
        });

        assertEquals(List.of(1, 2, 10, 20), visitedNumbers);
        assertArrayEquals(new Integer[] { 100, 200 }, row);
        assertSame(cube[0][0], cube[2][0]);
    }

    @Test
    void replaceIfObservesEarlierReplacementsInSharedRows() {
        final int[] row = { 1, 2 };
        final int[][][] cube = { { row }, null, { row } };
        final List<Integer> visitedNumbers = new ArrayList<>();

        Arrays.replaceIf(cube, value -> {
            visitedNumbers.add(value);
            return value > 0;
        }, 0);

        assertEquals(List.of(1, 2, 0, 0), visitedNumbers);
        assertArrayEquals(new int[] { 0, 0 }, row);

        final String[] words = { "old" };
        final String[][] grid = { words, null, words };
        final List<String> visitedWords = new ArrayList<>();

        Arrays.ff.replaceIf(grid, value -> {
            visitedWords.add(value);
            return value.equals("old");
        }, "new");

        assertEquals(List.of("old", "new"), visitedWords);
        assertArrayEquals(new String[] { "new" }, words);
    }

    @Test
    void replacementCallbackFailureRetainsEarlierRowsAndStopsTraversal() {
        final int[][] grid = { { 1, 2 }, null, { 3, 4 }, { 5 } };
        final IOException failure = new IOException("stop replacing");
        final List<Integer> visited = new ArrayList<>();

        assertSame(failure, assertThrows(IOException.class, () -> Arrays.replaceIf(grid, value -> {
            visited.add(value);
            if (value == 4) {
                throw failure;
            }
            return true;
        }, 0)));

        assertEquals(List.of(1, 2, 3, 4), visited);
        assertArrayEquals(new int[] { 0, 0 }, grid[0]);
        assertArrayEquals(new int[] { 0, 4 }, grid[2]);
        assertArrayEquals(new int[] { 5 }, grid[3]);
    }

    @Test
    void objectUpdateStoreFailureRetainsEarlierWritesInTheFailingRow() {
        final Number[][][] cube = { { new Number[] { 1 }, new Integer[] { 2, 3 }, new Number[] { 4 } } };
        final List<Number> visited = new ArrayList<>();

        assertThrows(ArrayStoreException.class, () -> Arrays.fff.updateAll(cube, value -> {
            visited.add(value);
            if (value.intValue() == 3) {
                return Double.valueOf(3.5);
            }
            return Integer.valueOf(value.intValue() * 10);
        }));

        assertEquals(List.of(1, 2, 3), visited);
        assertArrayEquals(new Number[] { 10 }, cube[0][0]);
        assertArrayEquals(new Number[] { 20, 3 }, cube[0][1]);
        assertArrayEquals(new Number[] { 4 }, cube[0][2]);
    }

    @Test
    void objectReplacementStoreFailureRetainsEarlierRows() {
        final Object[][] grid = { { "first" }, new String[] { "second" }, { "third" } };
        final List<Object> visited = new ArrayList<>();

        assertThrows(ArrayStoreException.class, () -> Arrays.ff.replaceIf(grid, value -> {
            visited.add(value);
            return true;
        }, Integer.valueOf(1)));

        assertEquals(List.of("first", "second"), visited);
        assertArrayEquals(new Object[] { 1 }, grid[0]);
        assertArrayEquals(new Object[] { "second" }, grid[1]);
        assertArrayEquals(new Object[] { "third" }, grid[2]);
    }

    @Test
    void objectMappingAllocatesRowsButRetainsReturnedElementReferences() {
        final StringBuilder value = new StringBuilder("original");
        final StringBuilder[] row = { value };
        final StringBuilder[][] grid = { row, row };
        final StringBuilder[][] inferred = Arrays.ff.map(grid, element -> element.append('!'));

        assertNotSame(grid, inferred);
        assertNotSame(row, inferred[0]);
        assertNotSame(inferred[0], inferred[1]);
        assertSame(value, inferred[0][0]);
        assertSame(value, inferred[1][0]);
        assertEquals("original!!", grid[0][0].toString());

        final StringBuilder[][][] cube = { grid };
        final StringBuilder[][][] explicit = Arrays.fff.map(cube, element -> element, StringBuilder.class);

        assertNotSame(cube, explicit);
        assertNotSame(grid, explicit[0]);
        assertNotSame(row, explicit[0][0]);
        assertNotSame(explicit[0][0], explicit[0][1]);
        assertSame(value, explicit[0][0][0]);
        assertSame(value, explicit[0][1][0]);
    }
}
