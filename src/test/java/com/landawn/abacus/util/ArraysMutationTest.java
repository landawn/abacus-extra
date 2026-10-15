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

import static com.landawn.abacus.util.ArrayTestSupport.arrayMethod;
import static com.landawn.abacus.util.ArrayTestSupport.callback;
import static com.landawn.abacus.util.ArrayTestSupport.invokeArrayMethod;
import static com.landawn.abacus.util.ArrayTestSupport.nestedArray;
import static com.landawn.abacus.util.ArrayTestSupport.primitiveArray;
import static com.landawn.abacus.util.ArrayTestSupport.primitiveValue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.landawn.abacus.util.Arrays.ff;
import com.landawn.abacus.util.Arrays.fff;

class ArraysMutationTest {

    @Nested
    class UpdateAllTests {

        @ParameterizedTest(name = "{0}, dimension {1}: updateAll skips empty input")
        @MethodSource("com.landawn.abacus.util.ArrayTestSupport#primitiveArrayCases")
        void updateAll_nullOrEmptyOuterArray_doesNotInvokeCallback(Class<?> elementType, int dimensions) throws Throwable {
            java.lang.reflect.Method method = arrayMethod("updateAll", elementType, dimensions, 2);
            int[] calls = { 0 };
            Object action = callback(method.getParameterTypes()[1], values -> {
                calls[0]++;
                return primitiveValue(elementType, 1);
            });
            for (Object input : new Object[] { null, java.lang.reflect.Array.newInstance(elementType, new int[dimensions]) }) {
                invokeArrayMethod(method, input, action);
            }
            assertEquals(0, calls[0]);
        }

        @Test
        void classJavadoc_updateAll() {
            // Line 125-128: updateAll({1,2,3,4,5}, x -> x * x) => {1,4,9,16,25}
            int[] numbers = { 1, 2, 3, 4, 5 };
            Arrays.updateAll(numbers, x -> x * x);
            assertArrayEquals(new int[] { 1, 4, 9, 16, 25 }, numbers);
        }

        @Test
        void doubleUpdateAll_naN_behavior() throws Exception {
            double[] a = { 1.0, Double.NaN, -2.0 };
            Arrays.updateAll(a, x -> Math.abs(x));
            assertEquals(1.0, a[0], 0.0);
            assertTrue(Double.isNaN(a[1])); // abs(NaN) = NaN
            assertEquals(2.0, a[2], 0.0);
        }

        @Test
        void fF_updateAll_null() {
            String[][] arr = null;
            Arrays.ff.updateAll(arr, String::toUpperCase);
            assertNull(arr);
        }

        @Test
        void fF_updateAll_skipsNullSubArrays() throws Exception {
            String[][] arr = { { "a", "b" }, null, { "c" } };
            ff.updateAll(arr, String::toUpperCase);
            assertArrayEquals(new String[] { "A", "B" }, arr[0]);
            assertNull(arr[1]);
            assertArrayEquals(new String[] { "C" }, arr[2]);
        }

        @Test
        void fF_updateAll_valid() {
            String[][] arr = { { "hello", "world" }, { "foo", "bar" } };
            Arrays.ff.updateAll(arr, String::toUpperCase);
            assertEquals("HELLO", arr[0][0]);
            assertEquals("WORLD", arr[0][1]);
            assertEquals("FOO", arr[1][0]);
            assertEquals("BAR", arr[1][1]);
        }

        @Test
        void fFF_updateAll_null() {
            String[][][] arr = null;
            Arrays.fff.updateAll(arr, String::toUpperCase);
            assertNull(arr);
        }

        @Test
        void fFF_updateAll_valid() {
            String[][][] arr = { { { "hello", "world" } }, { { "foo", "bar" } } };
            Arrays.fff.updateAll(arr, String::toUpperCase);
            assertEquals("HELLO", arr[0][0][0]);
            assertEquals("WORLD", arr[0][0][1]);
            assertEquals("FOO", arr[1][0][0]);
            assertEquals("BAR", arr[1][0][1]);
        }

        @Test
        void floatUpdateAll_all_dims() throws Exception {
            float[] a1 = { 1.0f, -2.0f, 3.0f };
            Arrays.updateAll(a1, x -> Math.abs(x));
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, a1, 0.0f);

            float[][] a2 = { { 1.0f, -2.0f }, { -3.0f, 4.0f } };
            Arrays.updateAll(a2, x -> x * x);
            assertArrayEquals(new float[] { 1.0f, 4.0f }, a2[0], 0.0f);
            assertArrayEquals(new float[] { 9.0f, 16.0f }, a2[1], 0.0f);

            float[][][] a3 = { { { -1.0f } }, { { 2.0f } } };
            Arrays.updateAll(a3, x -> -x);
            assertArrayEquals(new float[] { 1.0f }, a3[0][0], 0.0f);
            assertArrayEquals(new float[] { -2.0f }, a3[1][0], 0.0f);
        }

        @Test
        void floatUpdateAllNaNExamples() {
            final float[] row = { 1.0f, Float.NaN, 3.0f };
            final float[][] grid = { { Float.NaN, 2.0f } };
            final float[][][] cube = { { { Float.NaN, 1.0f } } };
            Arrays.updateAll(row, x -> x * 2.0f);
            Arrays.updateAll(grid, x -> x * 2.0f);
            Arrays.updateAll(cube, x -> x / 0f);

            assertTrue(Float.isNaN(row[1]));
            assertTrue(Float.isNaN(grid[0][0]));
            assertTrue(Float.isNaN(cube[0][0][0]));
            assertEquals(2.0f, row[0]);
            assertEquals(6.0f, row[2]);
            assertEquals(4.0f, grid[0][1]);
            assertEquals(Float.POSITIVE_INFINITY, cube[0][0][1]);
        }

        @Test
        void intUpdateAll1D() {
            // Line 7095-7096: updateAll({1,2,3}, x -> x*x) => {1,4,9}
            int[] arr = { 1, 2, 3 };
            Arrays.updateAll(arr, x -> x * x);
            assertArrayEquals(new int[] { 1, 4, 9 }, arr);
        }

        @Test
        void intUpdateAll2D() {
            // Line 7122-7125: updateAll({{1,2},{3,4}}, x -> x+10) => {{11,12},{13,14}}
            int[][] arr = { { 1, 2 }, { 3, 4 } };
            Arrays.updateAll(arr, x -> x + 10);
            assertArrayEquals(new int[] { 11, 12 }, arr[0]);
            assertArrayEquals(new int[] { 13, 14 }, arr[1]);
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
        void symmetry_updateAll_replaceIf_2D_nullSubsAreSkipped() {
            // Both should silently skip null sub-arrays (no NPE) — verify symmetry.
            final int[][] a1 = { { 1 }, null, { 2 } };
            final int[][] a2 = { { 1 }, null, { 2 } };
            assertDoesNotThrow(() -> Arrays.updateAll(a1, i -> i));
            assertDoesNotThrow(() -> Arrays.replaceIf(a2, i -> false, 0));
            // null still null in both
            assertNull(a1[1]);
            assertNull(a2[1]);
        }

        @Test
        void symmetry_updateAll_replaceIf_3D_nullSubsAreSkipped() {
            final int[][][] a1 = { { { 1 } }, null };
            final int[][][] a2 = { { { 1 } }, null };
            assertDoesNotThrow(() -> Arrays.updateAll(a1, i -> i));
            assertDoesNotThrow(() -> Arrays.replaceIf(a2, i -> false, 0));
            assertNull(a1[1]);
            assertNull(a2[1]);
        }

        @Test
        void updateAll() throws Exception {
            // Test with normal array
            String[][] array = { { "hello", "world" }, { "foo", "bar" } };
            ff.updateAll(array, str -> str.toUpperCase());
            assertArrayEquals(new String[] { "HELLO", "WORLD" }, array[0]);
            assertArrayEquals(new String[] { "FOO", "BAR" }, array[1]);

            // Test with null array
            String[][] nullArray = null;
            ff.updateAll(nullArray, str -> str.toUpperCase()); // Should not throw

            // Test with empty array
            String[][] emptyArray = {};
            ff.updateAll(emptyArray, str -> str.toUpperCase()); // Should not throw

            // Test with array containing null sub-arrays
            String[][] arrayWithNulls = { { "a", "b" }, null, { "c", "d" } };
            ff.updateAll(arrayWithNulls, str -> str.toUpperCase());
            assertArrayEquals(new String[] { "A", "B" }, arrayWithNulls[0]);
            assertNull(arrayWithNulls[1]);
            assertArrayEquals(new String[] { "C", "D" }, arrayWithNulls[2]);

            // Test with array containing empty sub-arrays
            String[][] arrayWithEmpty = { { "a", "b" }, {}, { "c", "d" } };
            ff.updateAll(arrayWithEmpty, str -> str.toUpperCase());
            assertArrayEquals(new String[] { "A", "B" }, arrayWithEmpty[0]);
            assertArrayEquals(new String[] {}, arrayWithEmpty[1]);
            assertArrayEquals(new String[] { "C", "D" }, arrayWithEmpty[2]);

            // Test with Integer array
            Integer[][] intArray = { { 1, 2 }, { 3, 4 } };
            ff.updateAll(intArray, i -> i * 2);
            assertArrayEquals(new Integer[] { 2, 4 }, intArray[0]);
            assertArrayEquals(new Integer[] { 6, 8 }, intArray[1]);
        }

        @Test
        void updateAll_1D_boolean_normal() {
            boolean[] arr = { true, false, true, false };
            Arrays.updateAll(arr, b -> !b);
            assertFalse(arr[0]);
            assertTrue(arr[1]);
            assertFalse(arr[2]);
            assertTrue(arr[3]);
        }

        @Test
        void updateAll_1D_boolean_null() {
            boolean[] arr = null;
            assertDoesNotThrow(() -> Arrays.updateAll(arr, b -> !b));
        }

        @Test
        void updateAll_1D_byte_normal() {
            byte[] arr = { 1, 2, 3, 4 };
            Arrays.updateAll(arr, b -> (byte) (b * 2));
            assertEquals(2, arr[0]);
            assertEquals(4, arr[1]);
            assertEquals(8, arr[3]);
        }

        @Test
        void updateAll_1D_char_normal() {
            char[] arr = { 'a', 'b', 'c' };
            Arrays.updateAll(arr, c -> Character.toUpperCase(c));
            assertEquals('A', arr[0]);
            assertEquals('B', arr[1]);
            assertEquals('C', arr[2]);
        }

        @Test
        void updateAll_1D_double_normal() {
            double[] arr = { 1.5, 2.5, 3.5 };
            Arrays.updateAll(arr, d -> d * 2.0);
            assertEquals(3.0, arr[0], 0.001);
            assertEquals(7.0, arr[2], 0.001);
        }

        @Test
        void updateAll_1D_double_null() {
            double[] arr = null;
            assertDoesNotThrow(() -> Arrays.updateAll(arr, d -> d * 2));
        }

        @Test
        void updateAll_1D_float_normal() {
            float[] arr = { 1.0f, 2.0f, 3.0f };
            Arrays.updateAll(arr, f -> f * 2.5f);
            assertEquals(2.5f, arr[0], 0.001f);
            assertEquals(7.5f, arr[2], 0.001f);
        }

        @Test
        void updateAll_1D_int_normal() {
            int[] arr = { 1, 2, 3, 4, 5 };
            Arrays.updateAll(arr, i -> i * 10);
            assertEquals(10, arr[0]);
            assertEquals(50, arr[4]);
        }

        @Test
        void updateAll_1D_int_null() {
            int[] arr = null;
            assertDoesNotThrow(() -> Arrays.updateAll(arr, i -> i * 2));
        }

        @Test
        void updateAll_1D_long_normal() {
            long[] arr = { 100L, 200L, 300L };
            Arrays.updateAll(arr, l -> l * 2);
            assertEquals(200L, arr[0]);
            assertEquals(600L, arr[2]);
        }

        @Test
        void updateAll_1D_short_normal() {
            short[] arr = { 10, 20, 30 };
            Arrays.updateAll(arr, s -> (short) (s * 2));
            assertEquals(20, arr[0]);
            assertEquals(40, arr[1]);
            assertEquals(60, arr[2]);
        }

        @Test
        void updateAll_2D_boolean_normal() {
            boolean[][] arr = { { true, false }, { false, true } };
            Arrays.updateAll(arr, b -> !b);
            assertFalse(arr[0][0]);
            assertTrue(arr[0][1]);
            assertTrue(arr[1][0]);
            assertFalse(arr[1][1]);
        }

        @Test
        void updateAll_2D_byte_normal() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            Arrays.updateAll(arr, b -> (byte) (b + 10));
            assertEquals(11, arr[0][0]);
            assertEquals(14, arr[1][1]);
        }

        @Test
        void updateAll_2D_char_normal() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            Arrays.updateAll(arr, c -> (char) (c + 1));
            assertEquals('b', arr[0][0]);
            assertEquals('e', arr[1][1]);
        }

        @Test
        void updateAll_2D_double_normal() {
            double[][] arr = { { 10.5, 20.5 }, { 30.5, 40.5 } };
            Arrays.updateAll(arr, d -> d - 0.5);
            assertEquals(10.0, arr[0][0], 0.001);
            assertEquals(40.0, arr[1][1], 0.001);
        }

        @Test
        void updateAll_2D_float_normal() {
            float[][] arr = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            Arrays.updateAll(arr, f -> f + 0.5f);
            assertEquals(2.0f, arr[0][0], 0.001f);
            assertEquals(5.0f, arr[1][1], 0.001f);
        }

        @Test
        void updateAll_2D_int_normal() {
            int[][] arr = { { 1, 2 }, { 3, 4 } };
            Arrays.updateAll(arr, i -> i + 100);
            assertEquals(101, arr[0][0]);
            assertEquals(104, arr[1][1]);
        }

        @Test
        void updateAll_2D_long_normal() {
            long[][] arr = { { 1000L, 2000L }, { 3000L, 4000L } };
            Arrays.updateAll(arr, l -> l / 1000);
            assertEquals(1L, arr[0][0]);
            assertEquals(4L, arr[1][1]);
        }

        @Test
        void updateAll_2D_short_normal() {
            short[][] arr = { { 100, 200 }, { 300, 400 } };
            Arrays.updateAll(arr, s -> (short) (s / 10));
            assertEquals(10, arr[0][0]);
            assertEquals(40, arr[1][1]);
        }

        @Test
        void updateAll_3D_boolean_normal() {
            boolean[][][] arr = { { { true, false } } };
            Arrays.updateAll(arr, b -> true);
            assertTrue(arr[0][0][0]);
            assertTrue(arr[0][0][1]);
        }

        @Test
        void updateAll_3D_byte_normal() {
            byte[][][] arr = { { { 5, 10 } } };
            Arrays.updateAll(arr, b -> (byte) (b / 5));
            assertEquals(1, arr[0][0][0]);
            assertEquals(2, arr[0][0][1]);
        }

        @Test
        void updateAll_3D_char_normal() {
            char[][][] arr = { { { 'x', 'y' } } };
            Arrays.updateAll(arr, c -> 'z');
            assertEquals('z', arr[0][0][0]);
            assertEquals('z', arr[0][0][1]);
        }

        @Test
        void updateAll_3D_double_normal() {
            double[][][] arr = { { { 100.0, 200.0 } } };
            Arrays.updateAll(arr, d -> d / 100.0);
            assertEquals(1.0, arr[0][0][0], 0.001);
            assertEquals(2.0, arr[0][0][1], 0.001);
        }

        @Test
        void updateAll_3D_float_normal() {
            float[][][] arr = { { { 10.0f, 20.0f } } };
            Arrays.updateAll(arr, f -> f / 10.0f);
            assertEquals(1.0f, arr[0][0][0], 0.001f);
            assertEquals(2.0f, arr[0][0][1], 0.001f);
        }

        @Test
        void updateAll_3D_int_normal() {
            int[][][] arr = { { { 10, 20 }, { 30 } } };
            Arrays.updateAll(arr, i -> i / 10);
            assertEquals(1, arr[0][0][0]);
            assertEquals(3, arr[0][1][0]);
        }

        @Test
        void updateAll_3D_long_normal() {
            long[][][] arr = { { { 50L, 100L } } };
            Arrays.updateAll(arr, l -> l + 5L);
            assertEquals(55L, arr[0][0][0]);
            assertEquals(105L, arr[0][0][1]);
        }

        @Test
        void updateAll_3D_short_normal() {
            short[][][] arr = { { { 50, 75 } } };
            Arrays.updateAll(arr, s -> (short) (s + 5));
            assertEquals(55, arr[0][0][0]);
            assertEquals(80, arr[0][0][1]);
        }

        @Test
        void updateAll_boolean_1D_null() {
            boolean[] arr = null;
            Arrays.updateAll(arr, b -> !b);
            assertNull(arr);
        }

        @Test
        void updateAll_boolean_2D_withNullSub() {
            final boolean[][] a = { { true, false }, null };
            Arrays.updateAll(a, b -> !b);
            assertArrayEquals(new boolean[] { false, true }, a[0]);
            assertNull(a[1]);
        }

        @Test
        void updateAll_boolean_3D_valid() {
            boolean[][][] arr = { { { true }, { false } } };
            Arrays.updateAll(arr, b -> !b);
            assertFalse(arr[0][0][0]);
            assertTrue(arr[0][1][0]);
        }

        @Test
        void updateAll_boolean2D() {
            boolean[][] a = { { true, false }, { true, false } };
            Arrays.updateAll(a, b -> !b);

            assertFalse(a[0][0]);
            assertTrue(a[0][1]);
            assertFalse(a[1][0]);
            assertTrue(a[1][1]);
        }

        @Test
        void updateAll_boolean2DArray() {
            // Test with normal two-dimensional array
            boolean[][] arr = { { true, false }, { false, true } };
            Arrays.updateAll(arr, b -> !b);
            assertArrayEquals(new boolean[] { false, true }, arr[0]);
            assertArrayEquals(new boolean[] { true, false }, arr[1]);

            // Test with empty two-dimensional array
            boolean[][] emptyArr = {};
            Arrays.updateAll(emptyArr, b -> !b);
            assertEquals(0, emptyArr.length);

            // Test with jagged array
            boolean[][] jaggedArr = { { true }, { false, true, false } };
            Arrays.updateAll(jaggedArr, b -> !b);
            assertArrayEquals(new boolean[] { false }, jaggedArr[0]);
            assertArrayEquals(new boolean[] { true, false, true }, jaggedArr[1]);
        }

        @Test
        void updateAll_boolean3D() {
            boolean[][][] a = { { { true, false } } };
            Arrays.updateAll(a, b -> !b);

            assertFalse(a[0][0][0]);
            assertTrue(a[0][0][1]);
        }

        @Test
        void updateAll_boolean3DArray() {
            // Test with normal three-dimensional array
            boolean[][][] arr = { { { true, false } }, { { false, true }, { true, true } } };
            Arrays.updateAll(arr, b -> !b);
            assertArrayEquals(new boolean[] { false, true }, arr[0][0]);
            assertArrayEquals(new boolean[] { true, false }, arr[1][0]);
            assertArrayEquals(new boolean[] { false, false }, arr[1][1]);

            // Test with empty three-dimensional array
            boolean[][][] emptyArr = {};
            Arrays.updateAll(emptyArr, b -> !b);
            assertEquals(0, emptyArr.length);
        }

        @Test
        void updateAll_booleanArray() {
            // Test with normal array - negate all
            boolean[] arr = { true, false, true, false };
            Arrays.updateAll(arr, b -> !b);
            assertArrayEquals(new boolean[] { false, true, false, true }, arr);

            // Test with empty array
            boolean[] emptyArr = {};
            Arrays.updateAll(emptyArr, b -> !b);
            assertArrayEquals(new boolean[] {}, emptyArr);

            // Test with all true
            boolean[] allTrue = { true, true, true };
            Arrays.updateAll(allTrue, b -> false);
            assertArrayEquals(new boolean[] { false, false, false }, allTrue);

            // Test with all false
            boolean[] allFalse = { false, false, false };
            Arrays.updateAll(allFalse, b -> true);
            assertArrayEquals(new boolean[] { true, true, true }, allFalse);
        }

        @Test
        void updateAll_booleanArray_values_true_false_true() {
            boolean[] arr = { true, false, true };
            Arrays.updateAll(arr, b -> !b);

            assertArrayEquals(new boolean[] { false, true, false }, arr);
        }

        @Test
        void updateAll_booleanArray_values_true_false_true_false() {
            boolean[] a = { true, false, true, false };
            Arrays.updateAll(a, b -> !b);

            assertArrayEquals(new boolean[] { false, true, false, true }, a);
        }

        @Test
        void updateAll_booleanArray2D() {
            boolean[][] arr = { { true, false }, { false, true } };
            Arrays.updateAll(arr, b -> !b);

            assertArrayEquals(new boolean[] { false, true }, arr[0]);
            assertArrayEquals(new boolean[] { true, false }, arr[1]);
        }

        @Test
        void updateAll_booleanArray2DNull() {
            assertDoesNotThrow(() -> Arrays.updateAll((boolean[][]) null, b -> b));
        }

        @Test
        void updateAll_booleanArrayEmpty() {
            boolean[] arr = {};
            Arrays.updateAll(arr, b -> !b);
            assertEquals(0, arr.length);
        }

        @Test
        void updateAll_booleanArrayNull() {
            assertDoesNotThrow(() -> Arrays.updateAll((boolean[]) null, b -> b));
        }

        @Test
        void updateAll_byte_basic() {
            final byte[] a = { 1, 2 };
            Arrays.updateAll(a, b -> (byte) (b * 2));
            assertArrayEquals(new byte[] { 2, 4 }, a);
        }

        @Test
        void updateAll_byte2D() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            Arrays.updateAll(a, b -> (byte) (b + 10));

            assertEquals(11, a[0][0]);
            assertEquals(12, a[0][1]);
            assertEquals(13, a[1][0]);
            assertEquals(14, a[1][1]);
        }

        @Test
        void updateAll_byte3D() {
            byte[][][] a = { { { 1, 2 } } };
            Arrays.updateAll(a, b -> (byte) (b * 3));

            assertEquals(3, a[0][0][0]);
            assertEquals(6, a[0][0][1]);
        }

        @Test
        void updateAll_byteArray() {
            byte[] a = { 1, 2, 3, 4 };
            Arrays.updateAll(a, b -> (byte) (b * 2));

            assertArrayEquals(new byte[] { 2, 4, 6, 8 }, a);
        }

        @Test
        void updateAll_char_2D_valid() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            Arrays.updateAll(arr, c -> Character.toUpperCase(c));
            assertArrayEquals(new char[] { 'A', 'B' }, arr[0]);
            assertArrayEquals(new char[] { 'C', 'D' }, arr[1]);
        }

        @Test
        void updateAll_char_basic() {
            final char[] a = { 'a', 'b' };
            Arrays.updateAll(a, c -> (char) (c + 1));
            assertArrayEquals(new char[] { 'b', 'c' }, a);
        }

        @Test
        void updateAll_char2D() {
            char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
            Arrays.updateAll(a, c -> Character.toUpperCase(c));

            assertEquals('A', a[0][0]);
            assertEquals('B', a[0][1]);
            assertEquals('C', a[1][0]);
            assertEquals('D', a[1][1]);
        }

        @Test
        void updateAll_char3D() {
            char[][][] a = { { { 'x', 'y' } } };
            Arrays.updateAll(a, c -> Character.toUpperCase(c));

            assertEquals('X', a[0][0][0]);
            assertEquals('Y', a[0][0][1]);
        }

        @Test
        void updateAll_charArray() {
            char[] a = { 'a', 'b', 'c' };
            Arrays.updateAll(a, c -> Character.toUpperCase(c));

            assertArrayEquals(new char[] { 'A', 'B', 'C' }, a);
        }

        @Test
        void updateAll_charArray_values_a_b_c() {
            char[] arr = { 'a', 'b', 'c' };
            Arrays.updateAll(arr, c -> Character.toUpperCase(c));

            assertArrayEquals(new char[] { 'A', 'B', 'C' }, arr);
        }

        @Test
        void updateAll_double_3D_basic() {
            final double[][][] a = { { { 1.0 } }, { { 2.0 } } };
            Arrays.updateAll(a, d -> d * 2.0);
            assertEquals(2.0, a[0][0][0], 0.0);
            assertEquals(4.0, a[1][0][0], 0.0);
        }

        @Test
        void updateAll_double2D() {
            double[][] a = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            Arrays.updateAll(a, d -> d * 5.0);

            assertEquals(5.0, a[0][0], 0.0001);
            assertEquals(10.0, a[0][1], 0.0001);
            assertEquals(15.0, a[1][0], 0.0001);
            assertEquals(20.0, a[1][1], 0.0001);
        }

        @Test
        void updateAll_double3D() {
            double[][][] a = { { { 1.0, 2.0 } } };
            Arrays.updateAll(a, d -> d * 3.0);

            assertEquals(3.0, a[0][0][0], 0.0001);
            assertEquals(6.0, a[0][0][1], 0.0001);
        }

        @Test
        void updateAll_doubleArray() {
            double[] a = { 1.0, 2.0, 3.0 };
            Arrays.updateAll(a, d -> d * 1.5);

            assertArrayEquals(new double[] { 1.5, 3.0, 4.5 }, a, 0.0001);
        }

        @Test
        void updateAll_emptyArray() {
            int[] a = {};
            Arrays.updateAll(a, i -> i * 2);

            assertEquals(0, a.length);
        }

        @Test
        void updateAll_emptyArray_values_new_boolean_0() {
            boolean[] empty = new boolean[0];
            Arrays.updateAll(empty, b -> !b);
            assertEquals(0, empty.length);
        }

        @Test
        void updateAll_float_basic() {
            final float[] a = { 1.5f, 2.5f };
            Arrays.updateAll(a, f -> f * 2.0f);
            assertArrayEquals(new float[] { 3.0f, 5.0f }, a, 0.0f);
        }

        @Test
        void updateAll_float2D() {
            float[][] a = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            Arrays.updateAll(a, f -> f * 10.0f);

            assertEquals(10.0f, a[0][0], 0.0001f);
            assertEquals(20.0f, a[0][1], 0.0001f);
            assertEquals(30.0f, a[1][0], 0.0001f);
            assertEquals(40.0f, a[1][1], 0.0001f);
        }

        @Test
        void updateAll_float3D() {
            float[][][] a = { { { 1.0f, 2.0f } } };
            Arrays.updateAll(a, f -> f * 2.5f);

            assertEquals(2.5f, a[0][0][0], 0.0001f);
            assertEquals(5.0f, a[0][0][1], 0.0001f);
        }

        @Test
        void updateAll_floatArray() {
            float[] a = { 1.0f, 2.0f, 3.0f };
            Arrays.updateAll(a, f -> f * 2.0f);

            assertArrayEquals(new float[] { 2.0f, 4.0f, 6.0f }, a, 0.0001f);
        }

        @Test
        void updateAll_int_2D_null_isNoOp() {
            assertDoesNotThrow(() -> Arrays.updateAll((int[][]) null, i -> i + 1));
        }

        @Test
        void updateAll_int_2D_withNullSubArrays_skipped() {
            // Primitive 2D updateAll uses for-each, then 1D variant checks N.isEmpty(null) → returns.
            // So null sub-arrays are silently skipped (no NPE).
            final int[][] a = { { 1, 2 }, null, { 3 } };
            Arrays.updateAll(a, i -> i * 10);
            assertArrayEquals(new int[] { 10, 20 }, a[0]);
            assertNull(a[1]);
            assertArrayEquals(new int[] { 30 }, a[2]);
        }

        @Test
        void updateAll_int_3D_withNullSubArrays_skipped() {
            final int[][][] a = { { { 1 }, null }, null, { { 2 } } };
            Arrays.updateAll(a, i -> i + 100);
            assertArrayEquals(new int[] { 101 }, a[0][0]);
            assertNull(a[0][1]);
            assertNull(a[1]);
            assertArrayEquals(new int[] { 102 }, a[2][0]);
        }

        @Test
        void updateAll_int_basic() {
            final int[] a = { 1, 2, 3 };
            Arrays.updateAll(a, i -> i * 10);
            assertArrayEquals(new int[] { 10, 20, 30 }, a);
        }

        @Test
        void updateAll_int_closureMutatesOtherElement_seesNewValues() {
            // updateAll reads a[i], applies op, writes back. If op (via closure)
            // mutates a different index, subsequent iterations see the new value.
            final int[] a = { 1, 2, 3 };
            Arrays.updateAll(a, i -> {
                if (i == 1) {
                    a[2] = 999; // mutate element ahead
                }
                return i + 100;
            });
            // a[0] processed first: 1 → 101
            // a[1] processed: 2 → 102, and side-effect sets a[2] = 999
            // a[2] processed: reads 999 (already mutated), 999 → 1099
            assertEquals(101, a[0]);
            assertEquals(102, a[1]);
            assertEquals(1099, a[2]);
        }

        @Test
        void updateAll_int_empty_isNoOp() {
            final int[] a = new int[0];
            Arrays.updateAll(a, i -> i + 1);
            assertEquals(0, a.length);
        }

        @Test
        void updateAll_int_null_isNoOp() {
            assertDoesNotThrow(() -> Arrays.updateAll((int[]) null, i -> i + 1));
        }

        @Test
        void updateAll_int_operatorThrows_partialMutationVisible() {
            final int[] a = { 1, 2, 3, 4 };
            assertThrows(RuntimeException.class, () -> Arrays.updateAll(a, i -> {
                if (i == 3) {
                    throw new RuntimeException("boom");
                }
                return i * 10;
            }));
            // First two elements mutated; index 2 threw before write; index 3 untouched
            assertEquals(10, a[0]);
            assertEquals(20, a[1]);
            assertEquals(3, a[2]);
            assertEquals(4, a[3]);
        }

        @Test
        void updateAll_int2D() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            Arrays.updateAll(a, i -> i * 10);

            assertEquals(10, a[0][0]);
            assertEquals(20, a[0][1]);
            assertEquals(30, a[1][0]);
            assertEquals(40, a[1][1]);
        }

        @Test
        void updateAll_int3D() {
            int[][][] a = { { { 1, 2 } } };
            Arrays.updateAll(a, i -> i + 10);

            assertEquals(11, a[0][0][0]);
            assertEquals(12, a[0][0][1]);
        }

        @Test
        void updateAll_intArray() {
            int[] a = { 1, 2, 3, 4, 5 };
            Arrays.updateAll(a, i -> i * 2);

            assertArrayEquals(new int[] { 2, 4, 6, 8, 10 }, a);
        }

        @Test
        void updateAll_long_2D_basic() {
            final long[][] a = { { 1L, 2L }, { 3L } };
            Arrays.updateAll(a, l -> l + 100L);
            assertArrayEquals(new long[] { 101L, 102L }, a[0]);
            assertArrayEquals(new long[] { 103L }, a[1]);
        }

        @Test
        void updateAll_long2D() {
            long[][] a = { { 100L, 200L }, { 300L, 400L } };
            Arrays.updateAll(a, l -> l / 100);

            assertEquals(1L, a[0][0]);
            assertEquals(2L, a[0][1]);
            assertEquals(3L, a[1][0]);
            assertEquals(4L, a[1][1]);
        }

        @Test
        void updateAll_long3D() {
            long[][][] a = { { { 100L, 200L } } };
            Arrays.updateAll(a, l -> l + 50L);

            assertEquals(150L, a[0][0][0]);
            assertEquals(250L, a[0][0][1]);
        }

        @Test
        void updateAll_longArray() {
            long[] a = { 10L, 20L, 30L };
            Arrays.updateAll(a, l -> l / 2);

            assertArrayEquals(new long[] { 5L, 10L, 15L }, a);
        }

        @Test
        void updateAll_short_basic() {
            final short[] a = { 10, 20 };
            Arrays.updateAll(a, s -> (short) (s + 1));
            assertArrayEquals(new short[] { 11, 21 }, a);
        }

        @Test
        void updateAll_short2D() {
            short[][] a = { { 10, 20 }, { 30, 40 } };
            Arrays.updateAll(a, s -> (short) (s / 10));

            assertEquals(1, a[0][0]);
            assertEquals(2, a[0][1]);
            assertEquals(3, a[1][0]);
            assertEquals(4, a[1][1]);
        }

        @Test
        void updateAll_short3D() {
            short[][][] a = { { { 10, 20 } } };
            Arrays.updateAll(a, s -> (short) (s + 5));

            assertEquals(15, a[0][0][0]);
            assertEquals(25, a[0][0][1]);
        }

        @Test
        void updateAll_shortArray() {
            short[] a = { 10, 20, 30 };
            Arrays.updateAll(a, s -> (short) (s / 2));

            assertArrayEquals(new short[] { 5, 10, 15 }, a);
        }

        @Test
        void updateAll_values_hello_world_foo_bar() throws Exception {
            // Test with non-empty array
            String[][][] arr = { { { "hello", "world" } }, { { "foo", "bar" } } };
            fff.updateAll(arr, str -> str.toUpperCase());
            assertEquals("HELLO", arr[0][0][0]);
            assertEquals("WORLD", arr[0][0][1]);
            assertEquals("FOO", arr[1][0][0]);
            assertEquals("BAR", arr[1][0][1]);

            // Test with empty array
            String[][][] emptyArr = new String[0][][];
            fff.updateAll(emptyArr, str -> str.toUpperCase());
            assertEquals(0, emptyArr.length);

            // Test with null elements
            String[][][] nullArr = { null, { null, { "test" } } };
            fff.updateAll(nullArr, str -> str.toUpperCase());
            assertNull(nullArr[0]);
            assertNull(nullArr[1][0]);
            assertEquals("TEST", nullArr[1][1][0]);

            // Test with Integer array
            Integer[][][] intArr = { { { 1, 2, 3 } }, { { 4, 5, 6 } } };
            fff.updateAll(intArr, n -> n * 2);
            assertEquals(2, intArr[0][0][0]);
            assertEquals(4, intArr[0][0][1]);
            assertEquals(6, intArr[0][0][2]);
            assertEquals(8, intArr[1][0][0]);
            assertEquals(10, intArr[1][0][1]);
            assertEquals(12, intArr[1][0][2]);
        }

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
    }

    @Nested
    class ReplaceIfTests {

        @ParameterizedTest(name = "{0}, dimension {1}: replaceIf skips empty input")
        @MethodSource("com.landawn.abacus.util.ArrayTestSupport#primitiveArrayCases")
        void replaceIf_nullOrEmptyOuterArray_doesNotInvokeCallback(Class<?> elementType, int dimensions) throws Throwable {
            java.lang.reflect.Method method = arrayMethod("replaceIf", elementType, dimensions, 3);
            int[] calls = { 0 };
            Object action = callback(method.getParameterTypes()[1], values -> {
                calls[0]++;
                return true;
            });
            for (Object input : new Object[] { null, java.lang.reflect.Array.newInstance(elementType, new int[dimensions]) }) {
                invokeArrayMethod(method, input, action, primitiveValue(elementType, 1));
            }
            assertEquals(0, calls[0]);
        }

        @Test
        void doubleReplaceIf_naN_infinity() throws Exception {
            double[][] grid = { { 1.0, -2.0 }, { Double.NaN, 4.0 } };
            Arrays.replaceIf(grid, x -> Double.isNaN(x), 0.0);
            assertArrayEquals(new double[] { 1.0, -2.0 }, grid[0], 0.0);
            assertArrayEquals(new double[] { 0.0, 4.0 }, grid[1], 0.0);

            double[][][] cube = { { { 1.0 }, { Double.POSITIVE_INFINITY } }, { { 3.0 }, { -4.0 } } };
            Arrays.replaceIf(cube, x -> !Double.isFinite(x), -1.0);
            assertEquals(1.0, cube[0][0][0], 0.0);
            assertEquals(-1.0, cube[0][1][0], 0.0);
            assertEquals(3.0, cube[1][0][0], 0.0);
            assertEquals(-4.0, cube[1][1][0], 0.0);
        }

        @Test
        void fF_replaceIf_null() {
            Integer[][] arr = null;
            Arrays.ff.replaceIf(arr, val -> val == null, 0);
            assertNull(arr);
        }

        @Test
        void fF_replaceIf_skipsNullSubArrays() throws Exception {
            Integer[][] arr = { { 1, null, 3 }, null, { null, 5, 6 } };
            ff.replaceIf(arr, val -> val == null, 0);
            assertArrayEquals(new Integer[] { 1, 0, 3 }, arr[0]);
            assertNull(arr[1]);
            assertArrayEquals(new Integer[] { 0, 5, 6 }, arr[2]);
        }

        @Test
        void fF_replaceIf_valid() {
            Integer[][] arr = { { 1, null, 3 }, { null, 5, null } };
            Arrays.ff.replaceIf(arr, val -> val == null, 0);
            assertEquals(1, arr[0][0]);
            assertEquals(0, arr[0][1]);
            assertEquals(3, arr[0][2]);
            assertEquals(0, arr[1][0]);
            assertEquals(5, arr[1][1]);
            assertEquals(0, arr[1][2]);
        }

        @Test
        void fFF_replaceIf_null() {
            Integer[][][] arr = null;
            Arrays.fff.replaceIf(arr, val -> val == null, 0);
            assertNull(arr);
        }

        @Test
        void fFF_replaceIf_skipsNullsAtAllLevels() throws Exception {
            Integer[][][] arr = { { { 1, null, 3 } }, null, { null, { null, 5 } } };
            fff.replaceIf(arr, v -> v == null, 0);
            assertArrayEquals(new Integer[] { 1, 0, 3 }, arr[0][0]);
            assertNull(arr[1]);
            assertNull(arr[2][0]);
            assertArrayEquals(new Integer[] { 0, 5 }, arr[2][1]);
        }

        @Test
        void fFF_replaceIf_valid() {
            Integer[][][] arr = { { { 1, 2, null } }, { { 3, null, 5 } } };
            Arrays.fff.replaceIf(arr, val -> val == null, 0);
            assertEquals(1, arr[0][0][0]);
            assertEquals(2, arr[0][0][1]);
            assertEquals(0, arr[0][0][2]);
            assertEquals(3, arr[1][0][0]);
            assertEquals(0, arr[1][0][1]);
            assertEquals(5, arr[1][0][2]);
        }

        @Test
        void floatReplaceIfWithNaN() throws Exception {
            float[] a1 = { 1.0f, Float.NaN, 3.0f, Float.NaN };
            Arrays.replaceIf(a1, Float::isNaN, 0.0f);
            assertArrayEquals(new float[] { 1.0f, 0.0f, 3.0f, 0.0f }, a1, 0.0f);
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
        void replaceIf() throws Exception {
            // Test replacing null values
            Integer[][] array = { { 1, null, 3 }, { null, 5, 6 } };
            ff.replaceIf(array, val -> val == null, 0);
            assertArrayEquals(new Integer[] { 1, 0, 3 }, array[0]);
            assertArrayEquals(new Integer[] { 0, 5, 6 }, array[1]);

            // Test replacing based on condition
            Integer[][] array2 = { { 1, 2, 3 }, { 4, 5, 6 } };
            ff.replaceIf(array2, val -> val > 3, -1);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, array2[0]);
            assertArrayEquals(new Integer[] { -1, -1, -1 }, array2[1]);

            // Test with null array
            Integer[][] nullArray = null;
            ff.replaceIf(nullArray, val -> val == null, 0); // Should not throw

            // Test with empty array
            Integer[][] emptyArray = {};
            ff.replaceIf(emptyArray, val -> val == null, 0); // Should not throw

            // Test with array containing null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            ff.replaceIf(arrayWithNulls, val -> val > 2, 0);
            assertArrayEquals(new Integer[] { 1, 2 }, arrayWithNulls[0]);
            assertNull(arrayWithNulls[1]);
            assertArrayEquals(new Integer[] { 0, 0 }, arrayWithNulls[2]);

            // Test with String array
            String[][] stringArray = { { "a", "b", null }, { "c", null, "d" } };
            ff.replaceIf(stringArray, val -> val == null, "empty");
            assertArrayEquals(new String[] { "a", "b", "empty" }, stringArray[0]);
            assertArrayEquals(new String[] { "c", "empty", "d" }, stringArray[1]);
        }

        @Test
        void replaceIf_1D_boolean_normal() {
            boolean[] arr = { true, false, true, false };
            Arrays.replaceIf(arr, b -> b, false);
            assertFalse(arr[0]);
            assertFalse(arr[1]);
            assertFalse(arr[2]);
            assertFalse(arr[3]);
        }

        @Test
        void replaceIf_1D_boolean_null() {
            boolean[] arr = null;
            assertDoesNotThrow(() -> Arrays.replaceIf(arr, b -> b, false));
        }

        @Test
        void replaceIf_1D_byte_normal() {
            byte[] arr = { 1, 2, 3, 4, 5 };
            Arrays.replaceIf(arr, b -> b % 2 == 0, (byte) 0);
            assertEquals(1, arr[0]);
            assertEquals(0, arr[1]);
            assertEquals(3, arr[2]);
            assertEquals(0, arr[3]);
            assertEquals(5, arr[4]);
        }

        @Test
        void replaceIf_1D_char_normal() {
            char[] arr = { 'a', 'b', 'c', 'd' };
            Arrays.replaceIf(arr, c -> c < 'c', 'x');
            assertEquals('x', arr[0]);
            assertEquals('x', arr[1]);
            assertEquals('c', arr[2]);
            assertEquals('d', arr[3]);
        }

        @Test
        void replaceIf_1D_double_normal() {
            double[] arr = { 1.5, 2.5, 3.5, 4.5 };
            Arrays.replaceIf(arr, d -> d > 3.0, 0.0);
            assertEquals(1.5, arr[0], 0.001);
            assertEquals(2.5, arr[1], 0.001);
            assertEquals(0.0, arr[2], 0.001);
            assertEquals(0.0, arr[3], 0.001);
        }

        @Test
        void replaceIf_1D_double_null() {
            double[] arr = null;
            assertDoesNotThrow(() -> Arrays.replaceIf(arr, d -> d > 0, 0.0));
        }

        @Test
        void replaceIf_1D_float_normal() {
            float[] arr = { 1.0f, 2.0f, 3.0f, 4.0f };
            Arrays.replaceIf(arr, f -> f > 2.5f, 0.0f);
            assertEquals(1.0f, arr[0], 0.001f);
            assertEquals(2.0f, arr[1], 0.001f);
            assertEquals(0.0f, arr[2], 0.001f);
            assertEquals(0.0f, arr[3], 0.001f);
        }

        @Test
        void replaceIf_1D_int_normal() {
            int[] arr = { 1, 2, 3, 4, 5 };
            Arrays.replaceIf(arr, i -> i > 3, 0);
            assertEquals(1, arr[0]);
            assertEquals(2, arr[1]);
            assertEquals(3, arr[2]);
            assertEquals(0, arr[3]);
            assertEquals(0, arr[4]);
        }

        @Test
        void replaceIf_1D_int_null() {
            int[] arr = null;
            assertDoesNotThrow(() -> Arrays.replaceIf(arr, i -> i > 0, 0));
        }

        @Test
        void replaceIf_1D_long_normal() {
            long[] arr = { 100L, 200L, 300L, 400L };
            Arrays.replaceIf(arr, l -> l >= 300L, 0L);
            assertEquals(100L, arr[0]);
            assertEquals(200L, arr[1]);
            assertEquals(0L, arr[2]);
            assertEquals(0L, arr[3]);
        }

        @Test
        void replaceIf_1D_short_normal() {
            short[] arr = { 10, 20, 30, 40 };
            Arrays.replaceIf(arr, s -> s > 20, (short) 0);
            assertEquals(10, arr[0]);
            assertEquals(20, arr[1]);
            assertEquals(0, arr[2]);
            assertEquals(0, arr[3]);
        }

        @Test
        void replaceIf_2D_boolean_normal() {
            boolean[][] arr = { { true, false }, { false, true } };
            Arrays.replaceIf(arr, b -> !b, true);
            assertTrue(arr[0][0]);
            assertTrue(arr[0][1]);
            assertTrue(arr[1][0]);
            assertTrue(arr[1][1]);
        }

        @Test
        void replaceIf_2D_byte_normal() {
            byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            Arrays.replaceIf(arr, b -> b > 3, (byte) 99);
            assertEquals(1, arr[0][0]);
            assertEquals(2, arr[0][1]);
            assertEquals(99, arr[1][0]);
        }

        @Test
        void replaceIf_2D_char_normal() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            Arrays.replaceIf(arr, c -> c == 'b' || c == 'd', 'z');
            assertEquals('a', arr[0][0]);
            assertEquals('z', arr[0][1]);
            assertEquals('c', arr[1][0]);
            assertEquals('z', arr[1][1]);
        }

        @Test
        void replaceIf_2D_double_normal() {
            double[][] arr = { { 10.5, 20.5 }, { 30.5, 40.5 } };
            Arrays.replaceIf(arr, d -> d < 25.0, 100.0);
            assertEquals(100.0, arr[0][0], 0.001);
            assertEquals(100.0, arr[0][1], 0.001);
            assertEquals(30.5, arr[1][0], 0.001);
            assertEquals(40.5, arr[1][1], 0.001);
        }

        @Test
        void replaceIf_2D_float_normal() {
            float[][] arr = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            Arrays.replaceIf(arr, f -> f < 3.0f, 9.9f);
            assertEquals(9.9f, arr[0][0], 0.001f);
            assertEquals(9.9f, arr[0][1], 0.001f);
            assertEquals(3.5f, arr[1][0], 0.001f);
            assertEquals(4.5f, arr[1][1], 0.001f);
        }

        @Test
        void replaceIf_2D_int_normal() {
            int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            Arrays.replaceIf(arr, i -> i % 2 == 0, -1);
            assertEquals(1, arr[0][0]);
            assertEquals(-1, arr[0][1]);
            assertEquals(3, arr[0][2]);
            assertEquals(-1, arr[1][0]);
        }

        @Test
        void replaceIf_2D_long_normal() {
            long[][] arr = { { 1000L, 2000L }, { 3000L, 4000L } };
            Arrays.replaceIf(arr, l -> l < 3000L, 999L);
            assertEquals(999L, arr[0][0]);
            assertEquals(999L, arr[0][1]);
            assertEquals(3000L, arr[1][0]);
            assertEquals(4000L, arr[1][1]);
        }

        @Test
        void replaceIf_2D_short_normal() {
            short[][] arr = { { 100, 200 }, { 300, 400 } };
            Arrays.replaceIf(arr, s -> s >= 300, (short) 999);
            assertEquals(100, arr[0][0]);
            assertEquals(200, arr[0][1]);
            assertEquals(999, arr[1][0]);
            assertEquals(999, arr[1][1]);
        }

        @Test
        void replaceIf_3D_boolean_normal() {
            boolean[][][] arr = { { { true, false } } };
            Arrays.replaceIf(arr, b -> b, false);
            assertFalse(arr[0][0][0]);
            assertFalse(arr[0][0][1]);
        }

        @Test
        void replaceIf_3D_byte_normal() {
            byte[][][] arr = { { { 10, 20, 30 } } };
            Arrays.replaceIf(arr, b -> b >= 20, (byte) 0);
            assertEquals(10, arr[0][0][0]);
            assertEquals(0, arr[0][0][1]);
            assertEquals(0, arr[0][0][2]);
        }

        @Test
        void replaceIf_3D_char_normal() {
            char[][][] arr = { { { 'p', 'q' } } };
            Arrays.replaceIf(arr, c -> c == 'p', 'r');
            assertEquals('r', arr[0][0][0]);
            assertEquals('q', arr[0][0][1]);
        }

        @Test
        void replaceIf_3D_double_normal() {
            double[][][] arr = { { { 5.5, 10.5 } } };
            Arrays.replaceIf(arr, d -> d == 10.5, 99.9);
            assertEquals(5.5, arr[0][0][0], 0.001);
            assertEquals(99.9, arr[0][0][1], 0.001);
        }

        @Test
        void replaceIf_3D_float_normal() {
            float[][][] arr = { { { 10.0f, 20.0f } } };
            Arrays.replaceIf(arr, f -> f == 20.0f, 200.0f);
            assertEquals(10.0f, arr[0][0][0], 0.001f);
            assertEquals(200.0f, arr[0][0][1], 0.001f);
        }

        @Test
        void replaceIf_3D_int_normal() {
            int[][][] arr = { { { 10, 20, 30 } } };
            Arrays.replaceIf(arr, i -> i == 20, 200);
            assertEquals(10, arr[0][0][0]);
            assertEquals(200, arr[0][0][1]);
            assertEquals(30, arr[0][0][2]);
        }

        @Test
        void replaceIf_3D_long_normal() {
            long[][][] arr = { { { 50L, 100L, 150L } } };
            Arrays.replaceIf(arr, l -> l == 100L, 999L);
            assertEquals(50L, arr[0][0][0]);
            assertEquals(999L, arr[0][0][1]);
            assertEquals(150L, arr[0][0][2]);
        }

        @Test
        void replaceIf_3D_short_normal() {
            short[][][] arr = { { { 50, 60, 70 } } };
            Arrays.replaceIf(arr, s -> s == 60, (short) 100);
            assertEquals(50, arr[0][0][0]);
            assertEquals(100, arr[0][0][1]);
            assertEquals(70, arr[0][0][2]);
        }

        @Test
        void replaceIf_allMatches() {
            int[] a = { 2, 4, 6, 8 };
            Arrays.replaceIf(a, i -> i % 2 == 0, 0);

            assertArrayEquals(new int[] { 0, 0, 0, 0 }, a);
        }

        @Test
        void replaceIf_boolean_1D_null() {
            boolean[] arr = null;
            Arrays.replaceIf(arr, b -> b, false);
            assertNull(arr);
        }

        @Test
        void replaceIf_boolean_1D_valid() {
            boolean[] arr = { true, false, true, false };
            Arrays.replaceIf(arr, b -> b, false);
            assertArrayEquals(new boolean[] { false, false, false, false }, arr);
        }

        @Test
        void replaceIf_boolean_2D_valid() {
            boolean[][] arr = { { true, false }, { true, true } };
            Arrays.replaceIf(arr, b -> !b, true);
            assertArrayEquals(new boolean[] { true, true }, arr[0]);
            assertArrayEquals(new boolean[] { true, true }, arr[1]);
        }

        @Test
        void replaceIf_boolean_2D_withNullSub() {
            final boolean[][] a = { { true, false, true }, null };
            Arrays.replaceIf(a, b -> b, false);
            assertArrayEquals(new boolean[] { false, false, false }, a[0]);
            assertNull(a[1]);
        }

        @Test
        void replaceIf_boolean2D() {
            boolean[][] a = { { true, false }, { true, false } };
            Arrays.replaceIf(a, b -> b, false);

            assertFalse(a[0][0]);
            assertFalse(a[0][1]);
            assertFalse(a[1][0]);
            assertFalse(a[1][1]);
        }

        @Test
        void replaceIf_boolean2DArray() {
            // Test with normal two-dimensional array
            boolean[][] arr = { { true, false }, { true, true } };
            Arrays.replaceIf(arr, b -> b, false);
            assertArrayEquals(new boolean[] { false, false }, arr[0]);
            assertArrayEquals(new boolean[] { false, false }, arr[1]);

            // Test with empty two-dimensional array
            boolean[][] emptyArr = {};
            Arrays.replaceIf(emptyArr, b -> b, false);
            assertEquals(0, emptyArr.length);
        }

        @Test
        void replaceIf_boolean3D() {
            boolean[][][] a = { { { true, false, true } } };
            Arrays.replaceIf(a, b -> b, false);

            assertFalse(a[0][0][0]);
            assertFalse(a[0][0][1]);
            assertFalse(a[0][0][2]);
        }

        @Test
        void replaceIf_boolean3DArray() {
            // Test with normal three-dimensional array
            boolean[][][] arr = { { { true, false } }, { { false, true } } };
            Arrays.replaceIf(arr, b -> !b, true);
            assertArrayEquals(new boolean[] { true, true }, arr[0][0]);
            assertArrayEquals(new boolean[] { true, true }, arr[1][0]);

            // Test with empty three-dimensional array
            boolean[][][] emptyArr = {};
            Arrays.replaceIf(emptyArr, b -> b, false);
            assertEquals(0, emptyArr.length);
        }

        @Test
        void replaceIf_booleanArray() {
            // Test replacing all true values with false
            boolean[] arr = { true, false, true, false, true };
            Arrays.replaceIf(arr, b -> b, false);
            assertArrayEquals(new boolean[] { false, false, false, false, false }, arr);

            // Test replacing all false values with true
            boolean[] arr2 = { true, false, true, false };
            Arrays.replaceIf(arr2, b -> !b, true);
            assertArrayEquals(new boolean[] { true, true, true, true }, arr2);

            // Test with empty array
            boolean[] emptyArr = {};
            Arrays.replaceIf(emptyArr, b -> b, false);
            assertArrayEquals(new boolean[] {}, emptyArr);

            // Test with no matches
            boolean[] noMatch = { false, false, false };
            Arrays.replaceIf(noMatch, b -> b, false);
            assertArrayEquals(new boolean[] { false, false, false }, noMatch);
        }

        @Test
        void replaceIf_booleanArray_values_true_false_true_false_true() {
            boolean[] a = { true, false, true, false, true };
            Arrays.replaceIf(a, b -> b, false);

            assertArrayEquals(new boolean[] { false, false, false, false, false }, a);
        }

        @Test
        void replaceIf_booleanArrayEmpty() {
            boolean[] arr = {};
            Arrays.replaceIf(arr, b -> true, false);
            assertEquals(0, arr.length);
        }

        @Test
        void replaceIf_booleanArrayNull() {
            assertDoesNotThrow(() -> Arrays.replaceIf((boolean[]) null, b -> true, false));
        }

        @Test
        void replaceIf_byte_basic() {
            final byte[] a = { 1, 2, 3 };
            Arrays.replaceIf(a, b -> b > 1, (byte) 9);
            assertArrayEquals(new byte[] { 1, 9, 9 }, a);
        }

        @Test
        void replaceIf_byte2D() {
            byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
            Arrays.replaceIf(a, b -> b % 2 == 0, (byte) 0);

            assertEquals(1, a[0][0]);
            assertEquals(0, a[0][1]);
            assertEquals(3, a[0][2]);
            assertEquals(0, a[1][0]);
            assertEquals(5, a[1][1]);
            assertEquals(0, a[1][2]);
        }

        @Test
        void replaceIf_byte3D() {
            byte[][][] a = { { { 1, 2, 3, 4, 5 } } };
            Arrays.replaceIf(a, b -> b > 3, (byte) 9);

            assertEquals(1, a[0][0][0]);
            assertEquals(2, a[0][0][1]);
            assertEquals(3, a[0][0][2]);
            assertEquals(9, a[0][0][3]);
            assertEquals(9, a[0][0][4]);
        }

        @Test
        void replaceIf_byteArray() {
            byte[] a = { 1, 2, 3, 4, 5 };
            Arrays.replaceIf(a, b -> b > 3, (byte) 0);

            assertArrayEquals(new byte[] { 1, 2, 3, 0, 0 }, a);
        }

        @Test
        void replaceIf_char_1D_valid() {
            char[] arr = { 'a', 'b', 'a', 'c' };
            Arrays.replaceIf(arr, c -> c == 'a', 'X');
            assertArrayEquals(new char[] { 'X', 'b', 'X', 'c' }, arr);
        }

        @Test
        void replaceIf_char_2D_valid() {
            char[][] arr = { { 'a', 'b' }, { 'a', 'a' } };
            Arrays.replaceIf(arr, c -> c == 'a', 'Z');
            assertArrayEquals(new char[] { 'Z', 'b' }, arr[0]);
            assertArrayEquals(new char[] { 'Z', 'Z' }, arr[1]);
        }

        @Test
        void replaceIf_char_basic() {
            final char[] a = { 'a', 'b', 'c' };
            Arrays.replaceIf(a, c -> c == 'b', 'X');
            assertArrayEquals(new char[] { 'a', 'X', 'c' }, a);
        }

        @Test
        void replaceIf_char2D() {
            char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
            Arrays.replaceIf(a, c -> c == 'b' || c == 'd', 'x');

            assertEquals('a', a[0][0]);
            assertEquals('x', a[0][1]);
            assertEquals('c', a[1][0]);
            assertEquals('x', a[1][1]);
        }

        @Test
        void replaceIf_char3D() {
            char[][][] a = { { { 'a', 'b', 'c' } } };
            Arrays.replaceIf(a, c -> c == 'b', 'z');

            assertEquals('a', a[0][0][0]);
            assertEquals('z', a[0][0][1]);
            assertEquals('c', a[0][0][2]);
        }

        @Test
        void replaceIf_charArray() {
            char[] a = { 'a', 'b', 'c', 'd' };
            Arrays.replaceIf(a, c -> c == 'b' || c == 'd', 'x');

            assertArrayEquals(new char[] { 'a', 'x', 'c', 'x' }, a);
        }

        @Test
        void replaceIf_charArray_values_a_b_c_a() {
            char[] arr = { 'a', 'b', 'c', 'a' };
            Arrays.replaceIf(arr, c -> c == 'a', 'x');

            assertArrayEquals(new char[] { 'x', 'b', 'c', 'x' }, arr);
        }

        @Test
        void replaceIf_double_naNReplacement_isWritten() {
            final double[] a = { 1.0, 2.0, 3.0 };
            Arrays.replaceIf(a, d -> d == 2.0, Double.NaN);
            assertEquals(1.0, a[0], 0.0);
            assertTrue(Double.isNaN(a[1]), "Element at index 1 should be NaN");
            assertEquals(3.0, a[2], 0.0);
        }

        @Test
        void replaceIf_double_predicateOnNaN_quirk() {
            // NaN != NaN. A predicate `d -> d == Double.NaN` will NEVER match NaN.
            // Use Double.isNaN(d) instead.
            final double[] a = { 1.0, Double.NaN, 3.0 };
            Arrays.replaceIf(a, d -> d == Double.NaN, -1.0); // bogus predicate
            // Nothing replaced — because NaN == NaN is false
            assertEquals(1.0, a[0], 0.0);
            assertTrue(Double.isNaN(a[1]));
            assertEquals(3.0, a[2], 0.0);

            // Now use proper Double.isNaN predicate — NaN gets replaced
            Arrays.replaceIf(a, Double::isNaN, -1.0);
            assertEquals(-1.0, a[1], 0.0);
        }

        @Test
        void replaceIf_double2D() {
            double[][] a = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
            Arrays.replaceIf(a, d -> d < 3.0, 0.0);

            assertEquals(0.0, a[0][0], 0.0001);
            assertEquals(0.0, a[0][1], 0.0001);
            assertEquals(3.0, a[0][2], 0.0001);
            assertEquals(4.0, a[1][0], 0.0001);
        }

        @Test
        void replaceIf_double3D() {
            double[][][] a = { { { 1.1, 2.2, 3.3, 4.4 } } };
            Arrays.replaceIf(a, d -> d > 3.0, 0.0);

            assertEquals(1.1, a[0][0][0], 0.0001);
            assertEquals(2.2, a[0][0][1], 0.0001);
            assertEquals(0.0, a[0][0][2], 0.0001);
            assertEquals(0.0, a[0][0][3], 0.0001);
        }

        @Test
        void replaceIf_doubleArray() {
            double[] a = { 1.0, 2.0, 3.0, 4.0, 5.0 };
            Arrays.replaceIf(a, d -> d > 3.0, 0.0);

            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 0.0, 0.0 }, a, 0.0001);
        }

        @Test
        void replaceIf_emptyArray() {
            boolean[] empty = new boolean[0];
            Arrays.replaceIf(empty, b -> true, false);
            assertEquals(0, empty.length);
        }

        @Test
        void replaceIf_float_naNReplacement() {
            final float[] a = { 1.0f, 2.0f };
            Arrays.replaceIf(a, f -> f == 1.0f, Float.NaN);
            assertTrue(Float.isNaN(a[0]));
            assertEquals(2.0f, a[1], 0.0f);
        }

        @Test
        void replaceIf_float2D() {
            float[][] a = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            Arrays.replaceIf(a, f -> f > 2.5f, 0.0f);

            assertEquals(1.0f, a[0][0], 0.0001f);
            assertEquals(2.0f, a[0][1], 0.0001f);
            assertEquals(0.0f, a[1][0], 0.0001f);
            assertEquals(0.0f, a[1][1], 0.0001f);
        }

        @Test
        void replaceIf_float3D() {
            float[][][] a = { { { 1.0f, 2.0f, 3.0f } } };
            Arrays.replaceIf(a, f -> f >= 2.0f, 0.0f);

            assertEquals(1.0f, a[0][0][0], 0.0001f);
            assertEquals(0.0f, a[0][0][1], 0.0001f);
            assertEquals(0.0f, a[0][0][2], 0.0001f);
        }

        @Test
        void replaceIf_floatArray() {
            float[] a = { 1.0f, 2.0f, 3.0f, 4.0f };
            Arrays.replaceIf(a, f -> f > 2.5f, 0.0f);

            assertArrayEquals(new float[] { 1.0f, 2.0f, 0.0f, 0.0f }, a, 0.0001f);
        }

        @Test
        void replaceIf_int_2D_withNullSubArrays_skipped() {
            final int[][] a = { { 1, 2, 3 }, null, { 4, 5 } };
            Arrays.replaceIf(a, i -> i % 2 == 0, 0);
            assertArrayEquals(new int[] { 1, 0, 3 }, a[0]);
            assertNull(a[1]);
            assertArrayEquals(new int[] { 0, 5 }, a[2]);
        }

        @Test
        void replaceIf_int_3D_withNullSubArrays_skipped() {
            final int[][][] a = { { { 1, 2 }, null }, null, { { 3, 4 } } };
            Arrays.replaceIf(a, i -> i > 2, 99);
            assertArrayEquals(new int[] { 1, 2 }, a[0][0]);
            assertNull(a[0][1]);
            assertNull(a[1]);
            assertArrayEquals(new int[] { 99, 99 }, a[2][0]);
        }

        @Test
        void replaceIf_int_null_isNoOp() {
            assertDoesNotThrow(() -> Arrays.replaceIf((int[]) null, i -> true, 999));
        }

        @Test
        void replaceIf_int_predicateMatchesAll_allReplaced() {
            final int[] a = { 1, 2, 3 };
            Arrays.replaceIf(a, i -> true, -1);
            assertArrayEquals(new int[] { -1, -1, -1 }, a);
        }

        @Test
        void replaceIf_int_predicateMatchesNone_unchanged() {
            final int[] a = { 1, 2, 3 };
            Arrays.replaceIf(a, i -> i > 100, 0);
            assertArrayEquals(new int[] { 1, 2, 3 }, a);
        }

        @Test
        void replaceIf_int2D() {
            int[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
            Arrays.replaceIf(a, i -> i % 2 == 0, 0);

            assertEquals(1, a[0][0]);
            assertEquals(0, a[0][1]);
            assertEquals(3, a[0][2]);
            assertEquals(0, a[1][0]);
        }

        @Test
        void replaceIf_int3D() {
            int[][][] a = { { { 1, 2, 3, 4, 5 } } };
            Arrays.replaceIf(a, i -> i > 3, 99);

            assertEquals(1, a[0][0][0]);
            assertEquals(2, a[0][0][1]);
            assertEquals(3, a[0][0][2]);
            assertEquals(99, a[0][0][3]);
            assertEquals(99, a[0][0][4]);
        }

        @Test
        void replaceIf_intArray() {
            int[] a = { 1, 2, 3, 4, 5 };
            Arrays.replaceIf(a, i -> i % 2 == 0, 0);

            assertArrayEquals(new int[] { 1, 0, 3, 0, 5 }, a);
        }

        @Test
        void replaceIf_long_2D_basic() {
            final long[][] a = { { 1L, 2L }, { 3L, 4L } };
            Arrays.replaceIf(a, l -> l % 2 == 0, 999L);
            assertArrayEquals(new long[] { 1L, 999L }, a[0]);
            assertArrayEquals(new long[] { 3L, 999L }, a[1]);
        }

        @Test
        void replaceIf_long2D() {
            long[][] a = { { 100L, 200L }, { 300L, 400L } };
            Arrays.replaceIf(a, l -> l >= 300L, 0L);

            assertEquals(100L, a[0][0]);
            assertEquals(200L, a[0][1]);
            assertEquals(0L, a[1][0]);
            assertEquals(0L, a[1][1]);
        }

        @Test
        void replaceIf_long3D() {
            long[][][] a = { { { 100L, 200L, 300L } } };
            Arrays.replaceIf(a, l -> l == 200L, 999L);

            assertEquals(100L, a[0][0][0]);
            assertEquals(999L, a[0][0][1]);
            assertEquals(300L, a[0][0][2]);
        }

        @Test
        void replaceIf_longArray() {
            long[] a = { 100L, 200L, 300L };
            Arrays.replaceIf(a, l -> l == 200L, 0L);

            assertArrayEquals(new long[] { 100L, 0L, 300L }, a);
        }

        @Test
        void replaceIf_noMatches() {
            int[] a = { 1, 3, 5, 7 };
            Arrays.replaceIf(a, i -> i % 2 == 0, 0);

            assertArrayEquals(new int[] { 1, 3, 5, 7 }, a);
        }

        @Test
        void replaceIf_predicateThrows_propagates() {
            final int[] a = { 1, 2, 3 };
            assertThrows(RuntimeException.class, () -> Arrays.replaceIf(a, i -> {
                if (i == 2) {
                    throw new RuntimeException("boom");
                }
                return false;
            }, 0));
            // Before exception, only one element was tested and not replaced
            assertArrayEquals(new int[] { 1, 2, 3 }, a);
        }

        @Test
        void replaceIf_short_basic() {
            final short[] a = { 1, 2, 3 };
            Arrays.replaceIf(a, s -> s == 2, (short) 99);
            assertArrayEquals(new short[] { 1, 99, 3 }, a);
        }

        @Test
        void replaceIf_short2D() {
            short[][] a = { { 10, 20, 30 }, { 40, 50, 60 } };
            Arrays.replaceIf(a, s -> s > 30, (short) 0);

            assertEquals(10, a[0][0]);
            assertEquals(20, a[0][1]);
            assertEquals(30, a[0][2]);
            assertEquals(0, a[1][0]);
            assertEquals(0, a[1][1]);
            assertEquals(0, a[1][2]);
        }

        @Test
        void replaceIf_short3D() {
            short[][][] a = { { { 10, 20, 30, 40 } } };
            Arrays.replaceIf(a, s -> s <= 20, (short) 0);

            assertEquals(0, a[0][0][0]);
            assertEquals(0, a[0][0][1]);
            assertEquals(30, a[0][0][2]);
            assertEquals(40, a[0][0][3]);
        }

        @Test
        void replaceIf_shortArray() {
            short[] a = { 10, 20, 30, 40 };
            Arrays.replaceIf(a, s -> s >= 30, (short) 0);

            assertArrayEquals(new short[] { 10, 20, 0, 0 }, a);
        }

        @Test
        void replaceIf_values_1_2_null_3_null_5() throws Exception {
            // Test replacing null values
            Integer[][][] arr = { { { 1, 2, null } }, { { 3, null, 5 } } };
            fff.replaceIf(arr, val -> val == null, 0);
            assertEquals(1, arr[0][0][0]);
            assertEquals(2, arr[0][0][1]);
            assertEquals(0, arr[0][0][2]);
            assertEquals(3, arr[1][0][0]);
            assertEquals(0, arr[1][0][1]);
            assertEquals(5, arr[1][0][2]);

            // Test replacing based on condition
            Integer[][][] arr2 = { { { 1, 2, 3 } }, { { 4, 5, 6 } } };
            fff.replaceIf(arr2, val -> val > 3, -1);
            assertEquals(1, arr2[0][0][0]);
            assertEquals(2, arr2[0][0][1]);
            assertEquals(3, arr2[0][0][2]);
            assertEquals(-1, arr2[1][0][0]);
            assertEquals(-1, arr2[1][0][1]);
            assertEquals(-1, arr2[1][0][2]);

            // Test with empty array
            Integer[][][] emptyArr = new Integer[0][][];
            fff.replaceIf(emptyArr, val -> val == null, 0);
            assertEquals(0, emptyArr.length);

            // Test with String array
            String[][][] strArr = { { { "apple", "banana" } }, { { "cat", "dog" } } };
            fff.replaceIf(strArr, str -> str.length() > 3, "***");
            assertEquals("***", strArr[0][0][0]);
            assertEquals("***", strArr[0][0][1]);
            assertEquals("cat", strArr[1][0][0]);
            assertEquals("dog", strArr[1][0][1]);
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
    }

    @Nested
    class MutateViaFlatArrayTests {

        @ParameterizedTest(name = "{0}, dimension {1}: empty flat mutation")
        @MethodSource("com.landawn.abacus.util.ArrayTestSupport#nestedPrimitiveArrayCases")
        void mutateViaFlatArray_nullOrEmptyOuterArray_skipsAction(Class<?> elementType, int dimensions) throws Throwable {
            java.lang.reflect.Method method = arrayMethod("mutateViaFlatArray", elementType, dimensions, 2);
            int[] calls = { 0 };
            Object action = callback(method.getParameterTypes()[1], values -> {
                calls[0]++;
                return null;
            });
            invokeArrayMethod(method, null, action);
            invokeArrayMethod(method, java.lang.reflect.Array.newInstance(elementType, new int[dimensions]), action);
            assertEquals(0, calls[0]);
        }

        @ParameterizedTest(name = "{0}, dimension {1}: ragged flat mutation")
        @MethodSource("com.landawn.abacus.util.ArrayTestSupport#nestedPrimitiveArrayCases")
        void mutateViaFlatArray_raggedInput_updatesValuesAndPreservesEmptyBranches(Class<?> elementType, int dimensions) throws Throwable {
            Object row = primitiveArray(elementType, 1, 2);
            Object rows = nestedArray(elementType, 2, null, primitiveArray(elementType), row);
            Object input = dimensions == 2 ? rows : nestedArray(elementType, 3, null, nestedArray(elementType, 2), rows);
            java.lang.reflect.Method method = arrayMethod("mutateViaFlatArray", elementType, dimensions, 2);
            int[] calls = { 0 };
            Object action = callback(method.getParameterTypes()[1], values -> {
                calls[0]++;
                Object flat = values[0];
                assertEquals(2, java.lang.reflect.Array.getLength(flat));
                assertEquals(primitiveValue(elementType, 1), java.lang.reflect.Array.get(flat, 0));
                assertEquals(primitiveValue(elementType, 2), java.lang.reflect.Array.get(flat, 1));
                java.lang.reflect.Array.set(flat, 0, primitiveValue(elementType, 3));
                java.lang.reflect.Array.set(flat, 1, primitiveValue(elementType, 4));
                return null;
            });
            invokeArrayMethod(method, input, action);
            assertEquals(1, calls[0]);
            assertEquals(primitiveValue(elementType, 3), java.lang.reflect.Array.get(row, 0));
            assertEquals(primitiveValue(elementType, 4), java.lang.reflect.Array.get(row, 1));
            assertNull(java.lang.reflect.Array.get(rows, 0));
            assertEquals(0, java.lang.reflect.Array.getLength(java.lang.reflect.Array.get(rows, 1)));
            assertSame(row, java.lang.reflect.Array.get(rows, 2));
        }

        @Test
        void ff_mutateViaFlatArray_assignableValue_succeeds() {
            final Object[][] a = { new Object[] { 3, 1 }, new Object[] { 2 } };

            Arrays.ff.mutateViaFlatArray(a, flat -> java.util.Arrays.sort(flat));

            assertArrayEquals(new Object[] { 1, 2 }, a[0]);
            assertArrayEquals(new Object[] { 3 }, a[1]);
        }

        @Test
        void ff_mutateViaFlatArray_narrowerRowType_throwsArrayStoreException() {
            // flatten() allocates by the OUTER component type (Object[]), so storing an Integer is
            // legal in the flat array but illegal for the String[] row it is copied back into.
            final Object[][] a = { new String[] { "x", "y" } };

            assertThrows(ArrayStoreException.class, () -> Arrays.ff.mutateViaFlatArray(a, flat -> flat[0] = Integer.valueOf(1)));
        }

        @Test
        void fF_mutateViaFlatArray_null() {
            String[][] arr = null;
            assertDoesNotThrow(() -> Arrays.ff.mutateViaFlatArray(arr, flat -> {
                throw new RuntimeException("Should not be called");
            }));
        }

        @Test
        void ff_mutateViaFlatArray_nullAndEmpty_areNoOps() {
            assertDoesNotThrow(() -> Arrays.ff.mutateViaFlatArray((Object[][]) null, flat -> flat[0] = 1));
            assertDoesNotThrow(() -> Arrays.fff.mutateViaFlatArray((Object[][][]) null, flat -> flat[0] = 1));

            final Object[][] empty = {};
            assertDoesNotThrow(() -> Arrays.ff.mutateViaFlatArray(empty, flat -> java.util.Arrays.sort(flat)));
            assertEquals(0, empty.length);

            final Object[][] emptyRows = { new Object[0], null };
            assertDoesNotThrow(() -> Arrays.ff.mutateViaFlatArray(emptyRows, flat -> java.util.Arrays.sort(flat)));
            assertEquals(0, emptyRows[0].length);
            assertNull(emptyRows[1]);
        }

        @Test
        void fF_mutateViaFlatArray_sortsAcrossRows() throws Exception {
            Integer[][] arr = { { 3, 1, 4 }, { 1, 5, 9 } };
            ff.mutateViaFlatArray(arr, t -> java.util.Arrays.sort(t));
            assertArrayEquals(new Integer[] { 1, 1, 3 }, arr[0]);
            assertArrayEquals(new Integer[] { 4, 5, 9 }, arr[1]);
        }

        @Test
        void ff_mutateViaFlatArray_storeFailure_alsoPartiallyUpdatesTheFailingRow() {
            // System.arraycopy copies the assignable prefix before throwing, so the row that fails is
            // itself left half-written when the offending element is not the first one.
            final Object[][] a = { new Object[] { "a", "b" }, new String[] { "c", "d" } };

            assertThrows(ArrayStoreException.class, () -> Arrays.ff.mutateViaFlatArray(a, flat -> {
                flat[0] = 0;
                flat[1] = 1;
                flat[2] = "KEPT";
                flat[3] = Integer.valueOf(3);
            }));

            assertArrayEquals(new Object[] { 0, 1 }, a[0]);
            assertArrayEquals(new Object[] { "KEPT", "d" }, a[1], "the failing row keeps the elements copied before the offending one");
        }

        @Test
        void ff_mutateViaFlatArray_storeFailure_leavesArrayPartiallyModified() {
            // row 0 is a plain Object[] and is copied back successfully; row 1 is a String[] and fails.
            final Object[][] a = { new Object[] { "a", "b" }, new String[] { "c", "d" } };

            assertThrows(ArrayStoreException.class, () -> Arrays.ff.mutateViaFlatArray(a, flat -> {
                for (int i = 0; i < flat.length; i++) {
                    flat[i] = Integer.valueOf(i);
                }
            }));

            assertArrayEquals(new Object[] { 0, 1 }, a[0], "rows copied before the failure keep their new values");
            assertArrayEquals(new Object[] { "c", "d" }, a[1], "this failing row is untouched only because its very first element fails");
        }

        @Test
        void ff_mutateViaFlatArray_uniformRowType_hasNoStoreHazard() {
            // when the declared type already matches the rows there is no widening gap at all
            final String[][] a = { { "b", "a" } };

            Arrays.ff.mutateViaFlatArray(a, flat -> java.util.Arrays.sort(flat));

            assertArrayEquals(new String[] { "a", "b" }, a[0]);
        }

        @Test
        void fF_mutateViaFlatArray_valid() {
            Integer[][] arr = { { 3, 1, 4 }, { 1, 5, 9 } };
            Arrays.ff.mutateViaFlatArray(arr, flat -> java.util.Arrays.sort(flat));
            assertArrayEquals(new Integer[] { 1, 1, 3 }, arr[0]);
            assertArrayEquals(new Integer[] { 4, 5, 9 }, arr[1]);
        }

        @Test
        void fff_mutateViaFlatArray_narrowerRowType_throwsArrayStoreException() {
            final Object[][][] a = { { new String[] { "p", "q" } } };

            assertThrows(ArrayStoreException.class, () -> Arrays.fff.mutateViaFlatArray(a, flat -> flat[0] = Integer.valueOf(9)));
        }

        @Test
        void fFF_mutateViaFlatArray_null() {
            String[][][] arr = null;
            assertDoesNotThrow(() -> Arrays.fff.mutateViaFlatArray(arr, flat -> {
                throw new RuntimeException("Should not be called");
            }));
        }

        @Test
        void fFF_mutateViaFlatArray_sortsAcrossAllLevels() throws Exception {
            Integer[][][] arr = { { { 5, 2 } }, { { 9, 1 } }, { { 3, 7 } } };
            fff.mutateViaFlatArray(arr, t -> java.util.Arrays.sort(t));
            assertArrayEquals(new Integer[] { 1, 2 }, arr[0][0]);
            assertArrayEquals(new Integer[] { 3, 5 }, arr[1][0]);
            assertArrayEquals(new Integer[] { 7, 9 }, arr[2][0]);
        }

        @Test
        void fFF_mutateViaFlatArray_valid() {
            Integer[][][] arr = { { { 5, 2 } }, { { 9, 1 } }, { { 3, 7 } } };
            Arrays.fff.mutateViaFlatArray(arr, flat -> java.util.Arrays.sort(flat));
            assertArrayEquals(new Integer[] { 1, 2 }, arr[0][0]);
            assertArrayEquals(new Integer[] { 3, 5 }, arr[1][0]);
            assertArrayEquals(new Integer[] { 7, 9 }, arr[2][0]);
        }

        @Test
        void flatMutationCopiesIntoRowsPresentAfterAction() {
            final int[][] grid = { { 1, 2 }, { 3 } };
            Arrays.mutateViaFlatArray(grid, flat -> {
                flat[0] = 10;
                flat[1] = 20;
                flat[2] = 30;
                grid[0] = new int[1];
            });

            // The shorter replacement row consumes one element; the unused suffix is not copied.
            assertArrayEquals(new int[] { 10 }, grid[0]);
            assertArrayEquals(new int[] { 20 }, grid[1]);

            final String[][][] cube = { { { "a", "b" } } };
            Arrays.fff.mutateViaFlatArray(cube, flat -> {
                flat[0] = "updated";
                cube[0] = new String[][] { new String[1], new String[1] };
            });

            // Copy-back follows the replacement block's two rows, not the original one-row shape.
            assertEquals(2, cube[0].length);
            assertArrayEquals(new String[] { "updated" }, cube[0][0]);
            assertArrayEquals(new String[] { "b" }, cube[0][1]);
        }

        @Test
        void flatMutationDoesNotCopyBackWhenActionThrows() {
            final IOException failure = new IOException("action failed after editing the temporary array");
            final int[][] grid = { { 1, 2 } };
            final int[][][] cube = { { { 3, 4 } } };
            final String[][] objects = { { "a", "b" } };
            final String[][][] objectCube = { { { "c", "d" } } };
            final List<Executable> calls = List.of(//
                    () -> Arrays.mutateViaFlatArray(grid, flat -> {
                        flat[0] = 99;
                        throw failure;
                    }), () -> Arrays.mutateViaFlatArray(cube, flat -> {
                        flat[0] = 99;
                        throw failure;
                    }), () -> Arrays.ff.mutateViaFlatArray(objects, flat -> {
                        flat[0] = "changed";
                        throw failure;
                    }), () -> Arrays.fff.mutateViaFlatArray(objectCube, flat -> {
                        flat[0] = "changed";
                        throw failure;
                    }));

            for (final Executable call : calls) {
                assertSame(failure, assertThrows(IOException.class, call));
            }
            assertArrayEquals(new int[] { 1, 2 }, grid[0]);
            assertArrayEquals(new int[] { 3, 4 }, cube[0][0]);
            assertArrayEquals(new String[] { "a", "b" }, objects[0]);
            assertArrayEquals(new String[] { "c", "d" }, objectCube[0][0]);
        }

        @Test
        void flatMutationDoesNotRollbackStructuralChangesWhenActionThrows() {
            final IOException failure = new IOException("action changed the structure");
            final int[] originalRow = { 1, 2 };
            final int[] replacementRow = { 7 };
            final int[][] grid = { originalRow };
            assertSame(failure, assertThrows(IOException.class, () -> Arrays.mutateViaFlatArray(grid, flat -> {
                flat[0] = 99;
                grid[0] = replacementRow;
                throw failure;
            })));
            assertSame(replacementRow, grid[0]);
            assertArrayEquals(new int[] { 7 }, replacementRow);
            assertArrayEquals(new int[] { 1, 2 }, originalRow);

            final String[][] originalBlock = { { "original" } };
            final String[][] replacementBlock = { { "replacement" }, { "kept" } };
            final String[][][] cube = { originalBlock };
            assertSame(failure, assertThrows(IOException.class, () -> Arrays.fff.mutateViaFlatArray(cube, flat -> {
                flat[0] = "temporary";
                cube[0] = replacementBlock;
                throw failure;
            })));
            assertSame(replacementBlock, cube[0]);
            assertArrayEquals(new String[] { "replacement" }, replacementBlock[0]);
            assertArrayEquals(new String[] { "kept" }, replacementBlock[1]);
            assertArrayEquals(new String[] { "original" }, originalBlock[0]);
        }

        @Test
        void flatMutationInvokesActionForNonemptyOuterArraysWithoutElements() {
            final int[] calls = { 0 };
            Arrays.mutateViaFlatArray(new int[][] { null, {} }, flat -> {
                assertEquals(0, flat.length);
                calls[0]++;
            });
            Arrays.mutateViaFlatArray(new int[][][] { null, { null, {} } }, flat -> {
                assertEquals(0, flat.length);
                calls[0]++;
            });
            Arrays.ff.mutateViaFlatArray(new String[][] { null, {} }, flat -> {
                assertEquals(String[].class, flat.getClass());
                assertEquals(0, flat.length);
                calls[0]++;
            });
            Arrays.fff.mutateViaFlatArray(new String[][][] { null, { null, {} } }, flat -> {
                assertEquals(String[].class, flat.getClass());
                assertEquals(0, flat.length);
                calls[0]++;
            });
            assertEquals(4, calls[0]);
        }

        @Test
        void mutateViaFlatArray() throws Exception {
            // Test sorting all elements
            Integer[][] array = { { 3, 1, 4 }, { 1, 5, 9 } };
            ff.mutateViaFlatArray(array, arr -> java.util.Arrays.sort(arr));
            assertArrayEquals(new Integer[] { 1, 1, 3 }, array[0]);
            assertArrayEquals(new Integer[] { 4, 5, 9 }, array[1]);

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 5, 3 }, null, { 1, 4 } };
            ff.mutateViaFlatArray(arrayWithNulls, arr -> java.util.Arrays.sort(arr));
            assertArrayEquals(new Integer[] { 1, 3 }, arrayWithNulls[0]);
            assertNull(arrayWithNulls[1]);
            assertArrayEquals(new Integer[] { 4, 5 }, arrayWithNulls[2]);

            // Test with empty sub-arrays
            Integer[][] arrayWithEmpty = { { 5, 3 }, {}, { 1, 4 } };
            ff.mutateViaFlatArray(arrayWithEmpty, arr -> java.util.Arrays.sort(arr));
            assertArrayEquals(new Integer[] { 1, 3 }, arrayWithEmpty[0]);
            assertArrayEquals(new Integer[] {}, arrayWithEmpty[1]);
            assertArrayEquals(new Integer[] { 4, 5 }, arrayWithEmpty[2]);

            // Test with empty array
            Integer[][] emptyArray = {};
            ff.mutateViaFlatArray(emptyArray, arr -> java.util.Arrays.sort(arr)); // Should not throw

            // Test with null array
            Integer[][] nullArray = null;
            ff.mutateViaFlatArray(nullArray, arr -> java.util.Arrays.sort(arr)); // Should not throw

            // Test with custom operation
            Integer[][] array2 = { { 1, 2 }, { 3, 4 } };
            ff.mutateViaFlatArray(array2, arr -> {
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = arr[i] * 10;
                }
            });
            assertArrayEquals(new Integer[] { 10, 20 }, array2[0]);
            assertArrayEquals(new Integer[] { 30, 40 }, array2[1]);
        }

        @Test
        void mutateViaFlatArray_2D_boolean_normal() {
            boolean[][] arr = { { true, false }, { false, true } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(4, count[0]);
        }

        @Test
        void mutateViaFlatArray_2D_byte_normal() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(4, count[0]);
        }

        @Test
        void mutateViaFlatArray_2D_char_normal() {
            char[][] arr = { { 'a', 'b' }, { 'c' } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        void mutateViaFlatArray_2D_double_normal() {
            double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            final double[] sum = { 0.0 };
            Arrays.mutateViaFlatArray(arr, subArr -> {
                for (double val : subArr) {
                    sum[0] += val;
                }
            });
            assertEquals(12.0, sum[0], 0.001);
        }

        @Test
        void mutateViaFlatArray_2D_emptyOuter_isNoop() {
            // null input -> no-op (and no NPE)
            Arrays.mutateViaFlatArray((int[][]) null, t -> java.util.Arrays.sort(t));
            // empty outer -> no-op
            final int[][] empty = new int[0][];
            Arrays.mutateViaFlatArray(empty, t -> java.util.Arrays.sort(t));
            assertEquals(0, empty.length);
        }

        @Test
        void mutateViaFlatArray_2D_float_normal() {
            float[][] arr = { { 1.1f, 2.2f }, { 3.3f } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        void mutateViaFlatArray_2D_int_normal() {
            int[][] arr = { { 1, 2, 3 }, { 4, 5 } };
            final int[] sum = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> {
                for (int val : subArr) {
                    sum[0] += val;
                }
            });
            assertEquals(15, sum[0]);
        }

        @Test
        void mutateViaFlatArray_2D_int_null() {
            int[][] arr = null;
            final int[] sum = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> sum[0]++); // Should not throw exception
            assertEquals(0, sum[0]);
        }

        @Test
        void mutateViaFlatArray_2D_long_normal() {
            long[][] arr = { { 100L, 200L }, { 300L } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        void mutateViaFlatArray_2D_nullAction_throws() {
            final int[][] a = { { 1, 2 } };
            assertThrows(IllegalArgumentException.class, () -> Arrays.mutateViaFlatArray(a, null));
        }

        @Test
        void mutateViaFlatArray_2D_nullRowsArePreserved_byte() {
            final byte[][] a = new byte[][] { { 3, 1 }, null, {}, { 7, 2 } };
            Arrays.mutateViaFlatArray(a, t -> java.util.Arrays.sort(t));
            assertNull(a[1]);
            assertEquals(0, a[2].length);
            assertArrayEquals(new byte[] { 1, 2 }, a[0]);
            assertArrayEquals(new byte[] { 3, 7 }, a[3]);
        }

        @Test
        void mutateViaFlatArray_2D_nullRowsArePreserved_int() {
            // null rows must remain null after the round-trip.
            final int[][] a = new int[][] { { 3, 1 }, null, {}, { 7, 2 } };
            Arrays.mutateViaFlatArray(a, t -> java.util.Arrays.sort(t));
            // flattened = {3,1,7,2} -> sorted = {1,2,3,7}; copy back row-by-row, null/empty skipped.
            assertNull(a[1]);
            assertEquals(0, a[2].length);
            assertArrayEquals(new int[] { 1, 2 }, a[0]);
            assertArrayEquals(new int[] { 3, 7 }, a[3]);
        }

        @Test
        void mutateViaFlatArray_2D_short_normal() {
            short[][] arr = { { 10, 20 }, { 30 } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        void mutateViaFlatArray_2D_sortPreservesJaggedStructure_double() {
            final double[][] a = new double[][] { { 3.0, 1.0, 4.0 }, { 1.5, 5.0, 9.5 } };
            Arrays.mutateViaFlatArray(a, t -> java.util.Arrays.sort(t));
            assertArrayEquals(new double[] { 1.0, 1.5, 3.0 }, a[0]);
            assertArrayEquals(new double[] { 4.0, 5.0, 9.5 }, a[1]);
        }

        @Test
        void mutateViaFlatArray_2D_sortPreservesJaggedStructure_int() {
            final int[][] a = new int[][] { { 3, 1, 4 }, { 1, 5, 9 } };
            Arrays.mutateViaFlatArray(a, t -> java.util.Arrays.sort(t));
            assertArrayEquals(new int[] { 1, 1, 3 }, a[0]);
            assertArrayEquals(new int[] { 4, 5, 9 }, a[1]);
        }

        @Test
        void mutateViaFlatArray_3D_boolean_normal() {
            boolean[][][] arr = { { { true, false } }, { { true } } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        void mutateViaFlatArray_3D_byte_normal() {
            byte[][][] arr = { { { 1, 2 } }, { { 3 } } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        void mutateViaFlatArray_3D_char_normal() {
            char[][][] arr = { { { 'x', 'y' } }, { { 'z' } } };
            final int[] sum = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> sum[0] += subArr.length);
            assertEquals(3, sum[0]);
        }

        @Test
        void mutateViaFlatArray_3D_double_normal() {
            double[][][] arr = { { { 10.5, 20.5 } }, { { 30.5 } } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        void mutateViaFlatArray_3D_float_normal() {
            float[][][] arr = { { { 1.0f, 2.0f } }, { { 3.0f } } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        void mutateViaFlatArray_3D_int_normal() {
            int[][][] arr = { { { 1, 2 }, { 3 } }, { { 4 } } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(4, count[0]);
        }

        @Test
        void mutateViaFlatArray_3D_long_normal() {
            long[][][] arr = { { { 1000L, 2000L } }, { { 3000L } } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        void mutateViaFlatArray_3D_short_normal() {
            short[][][] arr = { { { 100, 200 } }, { { 300 } } };
            final int[] count = { 0 };
            Arrays.mutateViaFlatArray(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        void mutateViaFlatArray_3D_sortPreservesNested_int() {
            final int[][][] a = new int[][][] { { { 5, 2 }, null }, { {}, { 8, 1 } } };
            Arrays.mutateViaFlatArray(a, t -> java.util.Arrays.sort(t));
            // flattened: {5,2,8,1} -> sorted {1,2,5,8}
            assertArrayEquals(new int[] { 1, 2 }, a[0][0]);
            assertNull(a[0][1]);
            assertEquals(0, a[1][0].length);
            assertArrayEquals(new int[] { 5, 8 }, a[1][1]);
        }

        @Test
        void mutateViaFlatArray_3D_sortPreservesNested_short() {
            final short[][][] a = new short[][][] { { { 5, 2 }, null }, { {}, { 8, 1 } } };
            Arrays.mutateViaFlatArray(a, t -> java.util.Arrays.sort(t));
            assertArrayEquals(new short[] { 1, 2 }, a[0][0]);
            assertNull(a[0][1]);
            assertEquals(0, a[1][0].length);
            assertArrayEquals(new short[] { 5, 8 }, a[1][1]);
        }

        @Test
        void mutateViaFlatArray_boolean_2D_null() {
            boolean[][] arr = null;
            assertDoesNotThrow(() -> Arrays.mutateViaFlatArray(arr, flat -> {
                // Should not execute
                throw new RuntimeException("Should not be called");
            }));
        }

        @Test
        void mutateViaFlatArray_boolean_2D_valid() {
            boolean[][] arr = { { true, false, true }, { false, true, false } };
            Arrays.mutateViaFlatArray(arr, flat -> {
                for (int i = 0; i < flat.length; i++) {
                    flat[i] = !flat[i];
                }
            });
            assertArrayEquals(new boolean[] { false, true, false }, arr[0]);
            assertArrayEquals(new boolean[] { true, false, true }, arr[1]);
        }

        @Test
        void mutateViaFlatArray_boolean_3D_valid() {
            boolean[][][] arr = { { { true, false } }, { { false, true } } };
            Arrays.mutateViaFlatArray(arr, flat -> {
                for (int i = 0; i < flat.length; i++) {
                    flat[i] = true;
                }
            });
            assertTrue(arr[0][0][0]);
            assertTrue(arr[0][0][1]);
            assertTrue(arr[1][0][0]);
            assertTrue(arr[1][0][1]);
        }

        @Test
        void mutateViaFlatArray_boolean2D() {
            boolean[][] a = { { true, false }, { true, false } };
            List<Boolean> result = new ArrayList<>();

            Arrays.mutateViaFlatArray(a, subArray -> {
                for (boolean val : subArray) {
                    result.add(val);
                }
            });

            assertEquals(4, result.size());
            assertTrue(result.get(0));
            assertFalse(result.get(1));
        }

        @Test
        void mutateViaFlatArray_boolean2DArray() {
            // Test sorting operation
            boolean[][] arr = { { true, false, true }, { false, true } };
            Arrays.mutateViaFlatArray(arr, flatArr -> {
                // Sort to have all false values first
                CommonUtil.sort(flatArr);
            });
            // After sorting, false values come first
            assertArrayEquals(new boolean[] { false, false, true }, arr[0]);
            assertArrayEquals(new boolean[] { true, true }, arr[1]);

            // Test with operation that sets all to true
            boolean[][] arr2 = { { true, false }, { false, true } };
            Arrays.mutateViaFlatArray(arr2, flatArr -> {
                for (int i = 0; i < flatArr.length; i++) {
                    flatArr[i] = true;
                }
            });
            assertArrayEquals(new boolean[] { true, true }, arr2[0]);
            assertArrayEquals(new boolean[] { true, true }, arr2[1]);

            // Test with empty array
            boolean[][] emptyArr = {};
            Arrays.mutateViaFlatArray(emptyArr, flatArr -> {
                // Should not be called
                fail("Operation should not be called on empty array");
            });

            // Test with array containing empty sub-arrays
            boolean[][] mixedArr = { { true, false }, {}, { true } };
            Arrays.mutateViaFlatArray(mixedArr, flatArr -> {
                for (int i = 0; i < flatArr.length; i++) {
                    flatArr[i] = !flatArr[i];
                }
            });
            assertArrayEquals(new boolean[] { false, true }, mixedArr[0]);
            assertArrayEquals(new boolean[] {}, mixedArr[1]);
            assertArrayEquals(new boolean[] { false }, mixedArr[2]);
        }

        @Test
        void mutateViaFlatArray_boolean3D() {
            boolean[][][] a = { { { true, false } }, { { true } } };
            List<Boolean> result = new ArrayList<>();

            Arrays.mutateViaFlatArray(a, subArray -> {
                for (boolean val : subArray) {
                    result.add(val);
                }
            });

            assertEquals(3, result.size());
        }

        @Test
        void mutateViaFlatArray_boolean3DArray() {
            // Test with normal three-dimensional array
            boolean[][][] arr = { { { true, false } }, { { false, true }, { true, true } } };
            Arrays.mutateViaFlatArray(arr, flatArr -> {
                // Reverse all values
                for (int i = 0; i < flatArr.length; i++) {
                    flatArr[i] = !flatArr[i];
                }
            });
            assertArrayEquals(new boolean[] { false, true }, arr[0][0]);
            assertArrayEquals(new boolean[] { true, false }, arr[1][0]);
            assertArrayEquals(new boolean[] { false, false }, arr[1][1]);

            // Test with empty three-dimensional array
            boolean[][][] emptyArr = {};
            Arrays.mutateViaFlatArray(emptyArr, flatArr -> {
                // Should not be called
                fail("Operation should not be called on empty array");
            });

            // Test counting operation
            boolean[][][] countArr = { { { true, false, true } }, { { false, false } } };
            int[] trueCount = { 0 };
            Arrays.mutateViaFlatArray(countArr, flatArr -> {
                for (boolean b : flatArr) {
                    if (b) {
                        trueCount[0]++;
                    }
                }
            });
            assertEquals(2, trueCount[0]);
        }

        @Test
        void mutateViaFlatArray_booleanArray2D() {
            boolean[][] arr = { { true, false }, { true, false } };
            int[] count = { 0 };

            Arrays.mutateViaFlatArray(arr, subArray -> count[0] += subArray.length);

            assertEquals(4, count[0]);
        }

        @Test
        void mutateViaFlatArray_booleanArray2DNull() {
            boolean[] invoked = { false };
            assertDoesNotThrow(() -> Arrays.mutateViaFlatArray((boolean[][]) null, subArray -> invoked[0] = true));
            assertFalse(invoked[0], "The callback should not run for a null 2D array");
        }

        @Test
        void mutateViaFlatArray_double2D() {
            double[][] a = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            List<Double> result = new ArrayList<>();

            Arrays.mutateViaFlatArray(a, subArray -> {
                for (double val : subArray) {
                    result.add(val);
                }
            });

            assertEquals(4, result.size());
            assertEquals(1.0, result.get(0), 0.0001);
        }

        @Test
        void mutateViaFlatArray_int2D() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            List<Integer> result = new ArrayList<>();

            Arrays.mutateViaFlatArray(a, subArray -> {
                for (int val : subArray) {
                    result.add(val);
                }
            });

            assertEquals(4, result.size());
            assertEquals(1, result.get(0));
            assertEquals(4, result.get(3));
        }

        @Test
        void mutateViaFlatArray_int3D() {
            int[][][] a = { { { 1, 2 } }, { { 3 } } };
            List<Integer> result = new ArrayList<>();

            Arrays.mutateViaFlatArray(a, subArray -> {
                for (int val : subArray) {
                    result.add(val);
                }
            });

            assertEquals(3, result.size());
        }

        @Test
        void mutateViaFlatArray_rejectsNullActionForEmptyInputs() {
            assertThrows(IllegalArgumentException.class, () -> Arrays.mutateViaFlatArray((boolean[][]) null, null));
            assertThrows(IllegalArgumentException.class, () -> Arrays.mutateViaFlatArray(new int[0][][], null));
            assertThrows(IllegalArgumentException.class, () -> ff.mutateViaFlatArray((Integer[][]) null, null));
            assertThrows(IllegalArgumentException.class, () -> ff.mutateViaFlatArray(new Integer[0][], null));
            assertThrows(IllegalArgumentException.class, () -> fff.mutateViaFlatArray((Integer[][][]) null, null));
            assertThrows(IllegalArgumentException.class, () -> fff.mutateViaFlatArray(new Integer[0][][], null));
        }

        @Test
        void mutateViaFlatArray_values_5_2_9_1_3_7() throws Exception {
            // Test sorting all elements
            Integer[][][] arr = { { { 5, 2 } }, { { 9, 1 } }, { { 3, 7 } } };
            fff.mutateViaFlatArray(arr, flat -> java.util.Arrays.sort(flat));
            assertEquals(1, arr[0][0][0]);
            assertEquals(2, arr[0][0][1]);
            assertEquals(3, arr[1][0][0]);
            assertEquals(5, arr[1][0][1]);
            assertEquals(7, arr[2][0][0]);
            assertEquals(9, arr[2][0][1]);

            // Test reversing elements
            Integer[][][] arr2 = { { { 1, 2 } }, { { 3, 4 } } };
            fff.mutateViaFlatArray(arr2, flat -> {
                for (int i = 0; i < flat.length / 2; i++) {
                    Integer temp = flat[i];
                    flat[i] = flat[flat.length - 1 - i];
                    flat[flat.length - 1 - i] = temp;
                }
            });
            assertEquals(4, arr2[0][0][0]);
            assertEquals(3, arr2[0][0][1]);
            assertEquals(2, arr2[1][0][0]);
            assertEquals(1, arr2[1][0][1]);

            // Test with empty array
            Integer[][][] emptyArr = new Integer[0][][];
            fff.mutateViaFlatArray(emptyArr, flat -> java.util.Arrays.sort(flat));
            assertEquals(0, emptyArr.length);

            // Test with null and empty sub-arrays
            Integer[][][] mixedArr = { { { 1, 2 } }, null, { {} }, { { 3, 4 } } };
            fff.mutateViaFlatArray(mixedArr, flat -> {
                for (int i = 0; i < flat.length; i++) {
                    flat[i] = flat[i] * 10;
                }
            });
            assertEquals(10, mixedArr[0][0][0]);
            assertEquals(20, mixedArr[0][0][1]);
            assertEquals(30, mixedArr[3][0][0]);
            assertEquals(40, mixedArr[3][0][1]);
        }

        @Test
        void mutateViaFlatArray_withException() {
            boolean[][] arr = { { true, false }, { true } };

            assertThrows(Exception.class, () -> {
                Arrays.mutateViaFlatArray(arr, flatArr -> {
                    throw new Exception("Test exception");
                });
            });
        }

        @Test
        void objectFlatMutationReportsRowsGrownByTheCallback() {
            final String[][][] cube = { { { "a" }, { "b" } } };

            assertThrows(IndexOutOfBoundsException.class, () -> Arrays.fff.mutateViaFlatArray(cube, flat -> {
                flat[0] = "updated";
                cube[0][1] = new String[2];
            }));
            assertArrayEquals(new String[] { "updated" }, cube[0][0]);
            assertArrayEquals(new String[2], cube[0][1]);
        }

        @Test
        void objectFlatMutationSharesElementObjectsEvenWhenActionThrows() {
            final StringBuilder value = new StringBuilder("before");
            final StringBuilder[][] grid = { { value } };
            final StringBuilder[][][] cube = { { { value } } };
            final IOException failure = new IOException("action failed");
            final Throwables.Consumer<StringBuilder[], IOException> action = flat -> {
                flat[0].append("-changed");
                flat[0] = new StringBuilder("replacement");
                throw failure;
            };

            assertSame(failure, assertThrows(IOException.class, () -> Arrays.ff.mutateViaFlatArray(grid, action)));
            assertSame(value, grid[0][0]);
            assertEquals("before-changed", value.toString());
            assertSame(failure, assertThrows(IOException.class, () -> Arrays.fff.mutateViaFlatArray(cube, action)));
            assertSame(value, cube[0][0][0]);
            assertEquals("before-changed-changed", value.toString());
        }

        @Test
        void objectFlatMutationUsesTemporaryCopyAndLastSharedRowWins() {
            final String[] row = { "a", "b" };
            final String[][] grid = { row, null, row };
            Arrays.ff.mutateViaFlatArray(grid, flat -> {
                assertArrayEquals(new String[] { "a", "b", "a", "b" }, flat);
                for (int i = 0; i < flat.length; i++) {
                    flat[i] = "v" + i;
                }
                assertArrayEquals(new String[] { "a", "b" }, row);
            });
            assertArrayEquals(new String[] { "v2", "v3" }, row);
            assertSame(grid[0], grid[2]);

            final String[] cubeRow = { "c", "d" };
            final String[][][] cube = { { cubeRow }, null, { cubeRow } };
            Arrays.fff.mutateViaFlatArray(cube, flat -> {
                for (int i = 0; i < flat.length; i++) {
                    flat[i] = "w" + i;
                }
                assertArrayEquals(new String[] { "c", "d" }, cubeRow);
            });
            assertArrayEquals(new String[] { "w2", "w3" }, cubeRow);
            assertSame(cube[0][0], cube[2][0]);
        }

        @Test
        void objectFlattenAndFlatMutationPreserveArrayValuedElementTypes() {
            final int[] value = { 1, 2 };
            final int[][][] grid = { { value }, null, {} };
            final int[][][][] cube = { grid, null };
            final int[][] flatGrid = Arrays.ff.flatten(grid);
            final int[][] flatCube = Arrays.fff.flatten(cube);

            assertEquals(int[][].class, flatGrid.getClass());
            assertEquals(int[][].class, flatCube.getClass());
            assertEquals(1, flatGrid.length);
            assertEquals(1, flatCube.length);
            assertSame(value, flatGrid[0]);
            assertSame(value, flatCube[0]);
            assertEquals(int[][].class, Arrays.ff.flatten(new int[0][][]).getClass());
            assertEquals(int[][].class, Arrays.fff.flatten(new int[0][][][]).getClass());

            final int[] replacement = { 3 };
            Arrays.ff.mutateViaFlatArray(grid, flat -> {
                assertEquals(int[][].class, flat.getClass());
                flat[0] = replacement;
                assertSame(value, grid[0][0]);
            });
            assertSame(replacement, grid[0][0]);
            Arrays.fff.mutateViaFlatArray(cube, flat -> {
                assertEquals(int[][].class, flat.getClass());
                flat[0] = value;
                assertSame(replacement, cube[0][0][0]);
            });
            assertSame(value, cube[0][0][0]);
        }

        @Test
        void primitiveFlatMutationReportsRowsGrownByTheCallback() {
            final int[][] grid = { { 1 }, { 2 } };

            assertThrows(IndexOutOfBoundsException.class, () -> Arrays.mutateViaFlatArray(grid, flat -> {
                flat[0] = 7;
                grid[1] = new int[2];
            }));
            assertArrayEquals(new int[] { 7 }, grid[0]);
            assertArrayEquals(new int[2], grid[1]);
        }

        @Test
        void primitiveFlatMutationUsesTemporaryCopyAndLastSharedRowWins() {
            final int[] row = { 1, 2 };
            final int[][] grid = { row, null, row };
            Arrays.mutateViaFlatArray(grid, flat -> {
                assertArrayEquals(new int[] { 1, 2, 1, 2 }, flat);
                for (int i = 0; i < flat.length; i++) {
                    flat[i] = i + 10;
                }
                assertArrayEquals(new int[] { 1, 2 }, row);
            });
            assertArrayEquals(new int[] { 12, 13 }, row);
            assertSame(grid[0], grid[2]);

            final int[] cubeRow = { 3, 4 };
            final int[][][] cube = { { cubeRow }, null, { cubeRow } };
            Arrays.mutateViaFlatArray(cube, flat -> {
                for (int i = 0; i < flat.length; i++) {
                    flat[i] = i + 20;
                }
                assertArrayEquals(new int[] { 3, 4 }, cubeRow);
            });
            assertArrayEquals(new int[] { 22, 23 }, cubeRow);
            assertSame(cube[0][0], cube[2][0]);
        }
    }
}
