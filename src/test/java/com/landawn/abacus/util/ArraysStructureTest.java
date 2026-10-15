/*
 * Copyright (C) 2026 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.landawn.abacus.util;

import static com.landawn.abacus.util.ArrayTestSupport.newArray;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.landawn.abacus.util.ArrayTestSupport.Value;
import com.landawn.abacus.util.Arrays.f;
import com.landawn.abacus.util.Arrays.ff;
import com.landawn.abacus.util.Arrays.fff;

class ArraysStructureTest {

    @Nested
    class TotalElementCountTests {

        @Test
        void booleanTotalCountOfElements2D() {
            // Line 2833-2835
            boolean[][] array = { { true, false, true }, null, { false, true, false } };
            assertEquals(6, Arrays.totalElementCount(array));
        }

        @Test
        void booleanTotalCountOfElements3D() {
            // Line 2861-2863
            boolean[][][] array = { { { true, false }, { true } }, { { false, true, false } } };
            assertEquals(6, Arrays.totalElementCount(array));
        }

        @Test
        void byteTotalCountOfElements2D() {
            // Line 5451-5452: {{1,2,3}, null, {4,5}} => 5
            byte[][] array = { { 1, 2, 3 }, null, { 4, 5 } };
            assertEquals(5, Arrays.totalElementCount(array));
        }

        @Test
        void byteTotalCountOfElements3D() {
            // Line 5478-5479: {{{1,2},{3}},{{4,5,6}}} => 6
            byte[][][] array = { { { 1, 2 }, { 3 } }, { { 4, 5, 6 } } };
            assertEquals(6, Arrays.totalElementCount(array));
        }

        @Test
        void classJavadoc_totalElementCount3D() {
            // Line 152-153: 2x3x4 cube => 24 total elements
            int[][][] cube = new int[2][3][4];
            long totalElements = Arrays.totalElementCount(cube);
            assertEquals(24, totalElements);
        }

        @Test
        void doubleTotalElementCountAndMinMax() {
            double[][] grid = { { 1, 2 }, { 3, 4, 5 }, null, {} };
            assertEquals(5L, Arrays.totalElementCount(grid));
            double[][][] cube = { { { 1 }, { 2, 3 } }, null, { { 4 } } };
            assertEquals(4L, Arrays.totalElementCount(cube));
            assertEquals(0, Arrays.minRowLength(grid));
            assertEquals(3, Arrays.maxRowLength(grid));
        }

        @Test
        void fF_totalElementCount() {
            Object[][] arr = { { "a", "b" }, { "c" }, { "d", "e", "f" } };
            long count = Arrays.ff.totalElementCount(arr);
            assertEquals(6L, count);
        }

        @Test
        void fF_totalElementCount_handlesNullSubArrays() {
            Object[][] arr = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
            assertEquals(6L, ff.totalElementCount(arr));
        }

        @Test
        void ff_totalElementCount_skipsNulls() {
            final Integer[][] a = new Integer[][] { null, { 1, 2 }, null };
            assertEquals(2L, ff.totalElementCount(a));
        }

        @Test
        void fFF_totalElementCount() {
            Object[][][] arr = { { { "a", "b" }, { "c" } }, { { "d", "e", "f" } } };
            long count = Arrays.fff.totalElementCount(arr);
            assertEquals(6L, count);
        }

        @Test
        void fFF_totalElementCount_handlesNullsAtAllLevels() {
            Object[][][] arr = { { { 1, 2 }, { 3 } }, null, { null, { 4, 5, 6 } } };
            assertEquals(6L, fff.totalElementCount(arr));
        }

        @Test
        void floatTotalElementCount() {
            float[][] grid = { { 1.0f }, { 2.0f, 3.0f }, null };
            assertEquals(3L, Arrays.totalElementCount(grid));
            float[][][] cube = { { { 1.0f } }, { { 2.0f, 3.0f }, null }, null };
            assertEquals(3L, Arrays.totalElementCount(cube));
        }

        @Test
        void intTotalCountOfElements2D() {
            // Line 8020-8022: {{1,2},{3,4,5},null,{}} => 5
            int[][] a = { { 1, 2 }, { 3, 4, 5 }, null, {} };
            assertEquals(5, Arrays.totalElementCount(a));
        }

        @Test
        void intTotalCountOfElements3D() {
            // Line 8048-8050: {{{1},{2,3}},null,{{4,5,6}}} => 6
            int[][][] a = { { { 1 }, { 2, 3 } }, null, { { 4, 5, 6 } } };
            assertEquals(6, Arrays.totalElementCount(a));
        }

        @Test
        void longTotalElementCount2DAnd3D() {
            long[][] a2 = { { 1, 2 }, { 3, 4, 5 }, null };
            assertEquals(5L, Arrays.totalElementCount(a2));
            long[][][] a3 = { { { 1 }, { 2, 3 } }, null, { { 4, 5, 6 } } };
            assertEquals(6L, Arrays.totalElementCount(a3));
        }

        @Test
        void totalCountOfElements() {
            // Test normal array
            Object[][] array = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
            long total = ff.totalElementCount(array);
            assertEquals(6, total);

            // Test empty array
            Object[][] emptyArray = {};
            assertEquals(0, ff.totalElementCount(emptyArray));

            // Test null array
            Object[][] nullArray = null;
            assertEquals(0, ff.totalElementCount(nullArray));

            // Test array with all nulls
            Object[][] isAllNulls = { null, null, null };
            assertEquals(0, ff.totalElementCount(isAllNulls));

            // Test array with empty sub-arrays
            Object[][] withEmpty = { {}, { 1, 2 }, {}, { 3, 4, 5 } };
            assertEquals(5, ff.totalElementCount(withEmpty));

            // Test large array
            Object[][] largeArray = new Object[1000][];
            for (int i = 0; i < 1000; i++) {
                largeArray[i] = new Object[10];
            }
            assertEquals(10000, ff.totalElementCount(largeArray));
        }

        @Test
        void totalCountOfElements_2D_boolean() {
            boolean[][] arr = { { true, false }, { true, false, true } };
            long count = Arrays.totalElementCount(arr);
            assertEquals(5L, count);
        }

        @Test
        void totalCountOfElements_2D_boolean_null() {
            boolean[][] arr = null;
            long count = Arrays.totalElementCount(arr);
            assertEquals(0L, count);
        }

        @Test
        void totalCountOfElements_2D_int() {
            int[][] arr = { { 1, 2, 3 }, { 4, 5 } };
            long count = Arrays.totalElementCount(arr);
            assertEquals(5L, count);
        }

        @Test
        void totalCountOfElements_3D_boolean() {
            boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
            long count = Arrays.totalElementCount(arr);
            assertEquals(4L, count);
        }

        @Test
        void totalCountOfElements_3D_double() {
            double[][][] arr = { { { 1.0, 2.0 }, { 3.0 } }, { { 4.0, 5.0 } } };
            long count = Arrays.totalElementCount(arr);
            assertEquals(5L, count);
        }

        @Test
        void totalCountOfElements_boolean_2D() {
            boolean[][] arr = { { true, false }, { true }, { false, true, false } };
            long count = Arrays.totalElementCount(arr);
            assertEquals(6L, count);
        }

        @Test
        void totalCountOfElements_boolean_2D_withNulls() {
            boolean[][] arr = { { true }, null, { false, false } };
            long count = Arrays.totalElementCount(arr);
            assertEquals(3L, count);
        }

        @Test
        void totalCountOfElements_boolean2D() {
            boolean[][] a = { { true, false }, { true }, { true, false, true } };
            long result = Arrays.totalElementCount(a);
            assertEquals(6, result);
        }

        @Test
        void totalCountOfElements_boolean3D() {
            boolean[][][] a = { { { true, false }, { true } }, { { false } } };
            long result = Arrays.totalElementCount(a);
            assertEquals(4, result);
        }

        @Test
        void totalCountOfElements_byte2D() {
            byte[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
            long result = Arrays.totalElementCount(a);
            assertEquals(6, result);
        }

        @Test
        void totalCountOfElements_byte3D() {
            byte[][][] a = { { { 1, 2 }, { 3 } }, { { 4 } } };
            long result = Arrays.totalElementCount(a);
            assertEquals(4, result);
        }

        @Test
        void totalCountOfElements_char2D() {
            char[][] a = { { 'a', 'b' }, { 'c' }, { 'd', 'e', 'f' } };
            long result = Arrays.totalElementCount(a);
            assertEquals(6, result);
        }

        @Test
        void totalCountOfElements_char3D() {
            char[][][] a = { { { 'a', 'b' }, { 'c' } }, { { 'd' } } };
            long result = Arrays.totalElementCount(a);
            assertEquals(4, result);
        }

        @Test
        void totalCountOfElements_double2D() {
            double[][] a = { { 1.0, 2.0 }, { 3.0 }, { 4.0, 5.0, 6.0 } };
            long result = Arrays.totalElementCount(a);
            assertEquals(6, result);
        }

        @Test
        void totalCountOfElements_double3D() {
            double[][][] a = { { { 1.0, 2.0 }, { 3.0 } }, { { 4.0 } } };
            long result = Arrays.totalElementCount(a);
            assertEquals(4, result);
        }

        @Test
        void totalCountOfElements_float2D() {
            float[][] a = { { 1.0f, 2.0f }, { 3.0f }, { 4.0f, 5.0f, 6.0f } };
            long result = Arrays.totalElementCount(a);
            assertEquals(6, result);
        }

        @Test
        void totalCountOfElements_float3D() {
            float[][][] a = { { { 1.0f, 2.0f }, { 3.0f } }, { { 4.0f } } };
            long result = Arrays.totalElementCount(a);
            assertEquals(4, result);
        }

        @Test
        void totalCountOfElements_int2D() {
            int[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
            long result = Arrays.totalElementCount(a);
            assertEquals(6, result);
        }

        @Test
        void totalCountOfElements_int3D() {
            int[][][] a = { { { 1, 2 }, { 3 } }, { { 4 } } };
            long result = Arrays.totalElementCount(a);
            assertEquals(4, result);
        }

        @Test
        void totalCountOfElements_long2D() {
            long[][] a = { { 1L, 2L }, { 3L }, { 4L, 5L, 6L } };
            long result = Arrays.totalElementCount(a);
            assertEquals(6, result);
        }

        @Test
        void totalCountOfElements_long3D() {
            long[][][] a = { { { 1L, 2L }, { 3L } }, { { 4L } } };
            long result = Arrays.totalElementCount(a);
            assertEquals(4, result);
        }

        @Test
        void totalCountOfElements_short2D() {
            short[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
            long result = Arrays.totalElementCount(a);
            assertEquals(6, result);
        }

        @Test
        void totalCountOfElements_short3D() {
            short[][][] a = { { { 1, 2 }, { 3 } }, { { 4 } } };
            long result = Arrays.totalElementCount(a);
            assertEquals(4, result);
        }

        @Test
        void totalCountOfElements_values_1_2_3_null_4_5_6() {
            // Test with regular array
            Object[][][] array = { { { 1, 2 }, { 3 } }, null, { { 4, 5, 6 } } };
            long count = fff.totalElementCount(array);
            assertEquals(6, count);

            // Test with empty array
            Object[][][] emptyArray = new Object[0][][];
            long emptyCount = fff.totalElementCount(emptyArray);
            assertEquals(0, emptyCount);

            // Test with all null/empty elements
            Object[][][] nullArray = { null, { null, {} }, { {} } };
            long nullCount = fff.totalElementCount(nullArray);
            assertEquals(0, nullCount);

            // Test with mixed elements
            Object[][][] mixedArray = { { { 1, 2, 3 } }, { {} }, { { 4 } }, null, { { 5, 6, 7, 8 } } };
            long mixedCount = fff.totalElementCount(mixedArray);
            assertEquals(8, mixedCount);

            // Test with jagged array
            Object[][][] jaggedArray = { { { 1 }, { 2, 3 }, { 4, 5, 6 } }, { { 7, 8 } }, { { 9, 10, 11, 12 } } };
            long jaggedCount = fff.totalElementCount(jaggedArray);
            assertEquals(12, jaggedCount);
        }

        @Test
        void totalElementCount_2D_skipsNulls_boolean() {
            final boolean[][] a = new boolean[][] { null, { true, false, true }, null, {} };
            assertEquals(3L, Arrays.totalElementCount(a));
        }

        @Test
        void totalElementCount_2D_skipsNulls_int() {
            final int[][] a = new int[][] { null, { 1, 2 }, null };
            assertEquals(2L, Arrays.totalElementCount(a));
        }

        @Test
        void totalElementCount_3D_rejectsLongOverflow() {
            assertEquals(Long.MAX_VALUE, Arrays.addElementCountExact(Long.MAX_VALUE - 1, 1));
            assertThrows(ArithmeticException.class, () -> Arrays.addElementCountExact(Long.MAX_VALUE, 1));
        }

        @Test
        void totalElementCount_3D_skipsNulls_int() {
            final int[][][] a = new int[][][] { { { 1 }, { 2, 3 } }, null, { { 4, 5, 6 } } };
            assertEquals(6L, Arrays.totalElementCount(a));
        }

        @Test
        void totalElementCount_booleanArray2D() {
            boolean[][] arr = { { true, false }, { true, false, true } };
            long count = Arrays.totalElementCount(arr);

            assertEquals(5, count);
        }

        @Test
        void totalElementCount_booleanArray2DEmpty() {
            boolean[][] arr = {};
            long count = Arrays.totalElementCount(arr);
            assertEquals(0, count);
        }

        @Test
        void totalElementCount_booleanArray2DNull() {
            long count = Arrays.totalElementCount((boolean[][]) null);
            assertEquals(0, count);
        }

        @Test
        void totalElementCount_booleanArray3D() {
            boolean[][][] arr = { { { true, false }, { true } } };
            long count = Arrays.totalElementCount(arr);

            assertEquals(3, count);
        }

        @Test
        void totalElementCount_booleanArray3DNull() {
            long count = Arrays.totalElementCount((boolean[][][]) null);
            assertEquals(0, count);
        }

        @Test
        void totalElementCount_charArray2D() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd', 'e' } };
            long count = Arrays.totalElementCount(arr);

            assertEquals(5, count);
        }

        @Test
        void totalElementCount_float3DWithOnlyNullTwoDimensionalSubArraysIsZero() {
            assertEquals(0L, Arrays.totalElementCount(new float[][][] { null, null }));
        }
    }

    @Nested
    class MinRowLengthTests {

        @Test
        void booleanminRowLength() {
            // Line 2899-2901
            boolean[][] array = { { true, false, true }, { false }, { true, false } };
            assertEquals(1, Arrays.minRowLength(array));
        }

        @Test
        void byteminRowLength() {
            // Line 5516-5517: {{1,2,3},{4,5},{6,7,8,9}} => minLen 2
            byte[][] array = { { 1, 2, 3 }, { 4, 5 }, { 6, 7, 8, 9 } };
            assertEquals(2, Arrays.minRowLength(array));
        }

        @Test
        void fF_minRowLength() {
            Object[][] arr = { { "a", "b", "c" }, { "d" }, { "e", "f" } };
            int minLen = Arrays.ff.minRowLength(arr);
            assertEquals(1, minLen);
        }

        @Test
        void genericMinimumRowLengthHandlesNullAndEmptyRowsAtEveryPosition() {
            final Object[][] rows = { new String[3], new Number[2], new Value[4] };
            assertEquals(2, Arrays.ff.minRowLength(rows));
            assertEquals(0, Arrays.ff.minRowLength(null));
            assertEquals(0, Arrays.ff.minRowLength(new Object[0][]));

            for (int i = 0; i < rows.length; i++) {
                final Object[] original = rows[i];
                rows[i] = null;
                assertEquals(0, Arrays.ff.minRowLength(rows));
                rows[i] = new Object[0];
                assertEquals(0, Arrays.ff.minRowLength(rows));
                rows[i] = original;
                assertEquals(2, Arrays.ff.minRowLength(rows));
            }
        }

        @Test
        void intminRowLength() {
            // Line 8085-8088: {{1,2,3},{4,5},null,{6}} => 0 (null sub-array has length 0)
            int[][] a = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
            assertEquals(0, Arrays.minRowLength(a));
        }

        @ParameterizedTest
        @MethodSource("com.landawn.abacus.util.ArrayTestSupport#primitiveTypes")
        void minimumRowLengthPreservesResultsForEveryPrimitiveType(final Class<?> type) throws Exception {
            final Object[] rows = { newArray(type, 3), newArray(type, 2), newArray(type, 4) };
            final Object matrix = newArray(rows[0].getClass(), rows.length);
            final Method minimum = Arrays.class.getMethod("minRowLength", matrix.getClass());

            for (int i = 0; i < rows.length; i++) {
                java.lang.reflect.Array.set(matrix, i, rows[i]);
            }

            assertEquals(2, minimum.invoke(null, matrix));
            assertEquals(0, minimum.invoke(null, new Object[] { null }));
            assertEquals(0, minimum.invoke(null, newArray(rows[0].getClass(), 0)));

            for (int i = 0; i < rows.length; i++) {
                java.lang.reflect.Array.set(matrix, i, null);
                assertEquals(0, minimum.invoke(null, matrix));
                java.lang.reflect.Array.set(matrix, i, newArray(type, 0));
                assertEquals(0, minimum.invoke(null, matrix));
                java.lang.reflect.Array.set(matrix, i, rows[i]);
                assertEquals(2, minimum.invoke(null, matrix));
            }
        }

        @Test
        void minRowLength() {
            final String[][] a = { { "a", "b" }, { "c", "d", "d" } };
            final String[][][] b = { { { "a", "b" } }, { { "1", "2" }, { "3", "4" } } };
            final int[][][] c = Arrays.reshape(Array.rangeClosed(1, 9), 2, 3);

            assertEquals(2, ff.minRowLength(a));
            assertEquals(3, ff.maxRowLength(a));
            assertEquals(1, ff.minRowLength(b));
            assertEquals(2, ff.maxRowLength(b));
            assertEquals(1, ff.minRowLength(c));
            assertEquals(2, ff.maxRowLength(c));

        }

        @Test
        void minRowLength_2D_boolean() {
            boolean[][] arr = { { true, false, true }, { false }, { true, false } };
            int minLen = Arrays.minRowLength(arr);
            assertEquals(1, minLen);
        }

        @Test
        void minRowLength_2D_boolean_null() {
            boolean[][] arr = null;
            int minLen = Arrays.minRowLength(arr);
            assertEquals(0, minLen);
        }

        @Test
        void minRowLength_2D_double() {
            double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0, 5.0 }, { 6.0 } };
            int minLen = Arrays.minRowLength(arr);
            assertEquals(1, minLen);
        }

        @Test
        void minRowLength_2D_int() {
            int[][] arr = { { 1, 2, 3 }, { 4, 5 }, { 6, 7, 8, 9 } };
            int minLen = Arrays.minRowLength(arr);
            assertEquals(2, minLen);
        }

        @Test
        void minRowLength_boolean_2D_withNulls() {
            boolean[][] arr = { { true, false }, null, { true } };
            int minLen = Arrays.minRowLength(arr);
            assertEquals(0, minLen);
        }

        @Test
        void minRowLength_boolean2D() {
            boolean[][] a = { { true, false }, { true }, { true, false, true } };
            int result = Arrays.minRowLength(a);

            assertEquals(1, result);
        }

        @Test
        void minRowLength_booleanArray2D() {
            boolean[][] arr = { { true, false, true }, { true } };
            int min = Arrays.minRowLength(arr);

            assertEquals(1, min);
        }

        @Test
        void minRowLength_booleanArray2DEmpty() {
            boolean[][] arr = {};
            int min = Arrays.minRowLength(arr);
            assertEquals(0, min);
        }

        @Test
        void minRowLength_booleanArray2DNull() {
            int min = Arrays.minRowLength((boolean[][]) null);
            assertEquals(0, min);
        }

        @Test
        void minRowLength_char2D() {
            char[][] a = { { 'a', 'b' }, { 'c' }, { 'd', 'e', 'f' } };
            int result = Arrays.minRowLength(a);

            assertEquals(1, result);
        }

        @Test
        void minRowLength_emptyOuter_returnsZero_int() {
            assertEquals(0, Arrays.minRowLength(new int[0][]));
            assertEquals(0, Arrays.minRowLength((int[][]) null));
        }

        @Test
        void minRowLength_emptyRow_returnsZero_int() {
            final int[][] a = new int[][] { {} };
            assertEquals(0, Arrays.minRowLength(a));
        }

        @Test
        void minRowLength_int2D() {
            int[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
            int result = Arrays.minRowLength(a);

            assertEquals(1, result);
        }

        @Test
        void minRowLength_nullRow_returnsZero_int() {
            // javadoc: "A null sub-array is considered to have a length of 0"
            final int[][] a = new int[][] { null, { 1 }, null, { 1, 2 } };
            assertEquals(0, Arrays.minRowLength(a));
        }

        @Test
        void minRowLength_values_1_2_3_4_5_null_6() {
            // Test normal array
            Object[][] array = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
            int minLen = ff.minRowLength(array);
            assertEquals(0, minLen); // null counts as 0

            // Test without null
            Object[][] array2 = { { 1, 2, 3 }, { 4, 5 }, { 6 } };
            int minLen2 = ff.minRowLength(array2);
            assertEquals(1, minLen2);

            // Test empty array
            Object[][] emptyArray = {};
            assertEquals(0, ff.minRowLength(emptyArray));

            // Test null array
            Object[][] nullArray = null;
            assertEquals(0, ff.minRowLength(nullArray));

            // Test with empty sub-arrays
            Object[][] withEmpty = { {}, { 1, 2 }, { 3, 4, 5 } };
            assertEquals(0, ff.minRowLength(withEmpty));

            // Test uniform length
            Object[][] uniform = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            assertEquals(3, ff.minRowLength(uniform));
        }
    }

    @Nested
    class MaxRowLengthTests {

        @Test
        void booleanmaxRowLength() {
            // Line 2927-2929
            boolean[][] array = { { true, false, true }, { false }, { true, false } };
            assertEquals(3, Arrays.maxRowLength(array));
        }

        @Test
        void bytemaxRowLength() {
            // Line 5544-5545: {{1,2}, null, {3,4,5,6}} => maxLen 4
            byte[][] array = { { 1, 2 }, null, { 3, 4, 5, 6 } };
            assertEquals(4, Arrays.maxRowLength(array));
        }

        @Test
        void classJavadoc_minmaxRowLength() {
            // Line 156-158: {{1,2,3,4},{5,6,7,8},{9,10,11,12}} => minLen=4, maxLen=4
            int[][] array2D = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 } };
            assertEquals(4, Arrays.minRowLength(array2D));
            assertEquals(4, Arrays.maxRowLength(array2D));
        }

        @Test
        void fF_maxRowLength() {
            Object[][] arr = { { "a", "b", "c" }, { "d" }, { "e", "f" } };
            int maxLen = Arrays.ff.maxRowLength(arr);
            assertEquals(3, maxLen);
        }

        @Test
        void fF_minmaxRowLength() {
            Object[][] arr = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
            assertEquals(0, ff.minRowLength(arr));
            assertEquals(3, ff.maxRowLength(arr));
        }

        @Test
        void floatMinmaxRowLength() {
            float[][] grid = { { 1.0f, 2.0f }, { 3.0f }, null };
            assertEquals(0, Arrays.minRowLength(grid));
            float[][] grid2 = { { 1.0f }, { 2.0f, 3.0f, 4.0f }, null };
            assertEquals(3, Arrays.maxRowLength(grid2));
        }

        @Test
        void intmaxRowLength() {
            // Line 8113-8116: {{1},{2,3},null,{4,5,6}} => 3
            int[][] a = { { 1 }, { 2, 3 }, null, { 4, 5, 6 } };
            assertEquals(3, Arrays.maxRowLength(a));
        }

        @Test
        void longMinmaxRowLength() {
            long[][] a = { { 1L, 2L, 3L }, { 4L, 5L }, null, { 6L } };
            assertEquals(0, Arrays.minRowLength(a));
            assertEquals(3, Arrays.maxRowLength(a));
        }

        @Test
        void maxRowLength() {
            // Test normal array
            Object[][] array = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
            int maxLen = ff.maxRowLength(array);
            assertEquals(3, maxLen);

            // Test empty array
            Object[][] emptyArray = {};
            assertEquals(0, ff.maxRowLength(emptyArray));

            // Test null array
            Object[][] nullArray = null;
            assertEquals(0, ff.maxRowLength(nullArray));

            // Test with all nulls
            Object[][] isAllNulls = { null, null, null };
            assertEquals(0, ff.maxRowLength(isAllNulls));

            // Test with empty sub-arrays
            Object[][] withEmpty = { {}, { 1, 2 }, { 3, 4, 5, 6, 7 } };
            assertEquals(5, ff.maxRowLength(withEmpty));

            // Test uniform length
            Object[][] uniform = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            assertEquals(2, ff.maxRowLength(uniform));
        }

        @Test
        void maxRowLength_2D_boolean() {
            boolean[][] arr = { { true, false }, { true, false, true }, { false } };
            int maxLen = Arrays.maxRowLength(arr);
            assertEquals(3, maxLen);
        }

        @Test
        void maxRowLength_2D_boolean_null() {
            boolean[][] arr = null;
            int maxLen = Arrays.maxRowLength(arr);
            assertEquals(0, maxLen);
        }

        @Test
        void maxRowLength_2D_double() {
            double[][] arr = { { 1.0 }, { 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
            int maxLen = Arrays.maxRowLength(arr);
            assertEquals(3, maxLen);
        }

        @Test
        void maxRowLength_2D_int() {
            int[][] arr = { { 1, 2 }, { 3, 4, 5, 6 }, { 7 } };
            int maxLen = Arrays.maxRowLength(arr);
            assertEquals(4, maxLen);
        }

        @Test
        void maxRowLength_allNullRows_returnsZero_int() {
            final int[][] a = new int[][] { null, null };
            assertEquals(0, Arrays.maxRowLength(a));
        }

        @Test
        void maxRowLength_boolean_2D() {
            boolean[][] arr = { { true, false, true }, { false }, { true, false } };
            int maxLen = Arrays.maxRowLength(arr);
            assertEquals(3, maxLen);
        }

        @Test
        void maxRowLength_boolean_2D_withNulls() {
            boolean[][] arr = { { true }, null, { true, false, false } };
            int maxLen = Arrays.maxRowLength(arr);
            assertEquals(3, maxLen);
        }

        @Test
        void maxRowLength_boolean2D() {
            boolean[][] a = { { true, false }, { true }, { true, false, true } };
            int result = Arrays.maxRowLength(a);

            assertEquals(3, result);
        }

        @Test
        void maxRowLength_booleanArray2D() {
            boolean[][] arr = { { true, false, true }, { true } };
            int max = Arrays.maxRowLength(arr);

            assertEquals(3, max);
        }

        @Test
        void maxRowLength_booleanArray2DEmpty() {
            boolean[][] arr = {};
            int max = Arrays.maxRowLength(arr);
            assertEquals(0, max);
        }

        @Test
        void maxRowLength_booleanArray2DNull() {
            int max = Arrays.maxRowLength((boolean[][]) null);
            assertEquals(0, max);
        }

        @Test
        void maxRowLength_char2D() {
            char[][] a = { { 'a', 'b' }, { 'c' }, { 'd', 'e', 'f' } };
            int result = Arrays.maxRowLength(a);

            assertEquals(3, result);
        }

        @Test
        void maxRowLength_int2D() {
            int[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
            int result = Arrays.maxRowLength(a);

            assertEquals(3, result);
        }

        @Test
        void maxRowLength_mixedRows_int() {
            final int[][] a = new int[][] { { 1 }, { 2, 3 }, null, { 4, 5, 6 } };
            assertEquals(3, Arrays.maxRowLength(a));
        }
    }
}
