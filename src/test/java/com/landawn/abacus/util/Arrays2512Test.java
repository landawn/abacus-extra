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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

/**
 * Comprehensive unit tests for Arrays utility class.
 * This class provides extensive array manipulation methods for primitive and object arrays.
 * Tests cover println, mapToObj, mapToLong, mapToDouble, mapToInt, updateAll, replaceIf,
 * reshape, flatten, flatOp, zip, totalCountOfElements, minSubArrayLen, maxSubArrayLen methods.
 */
@Tag("2512")
public class Arrays2512Test extends TestBase {

    // ============================================
    // Tests for println(Object[])
    // ============================================

    @Test
    public void test_println_objectArray() {
        Object[] arr = { "a", "b", "c" };
        String result = Arrays.println(arr);

        assertNotNull(result);
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

    @Test
    public void test_println_objectArrayNull() {
        String result = Arrays.println((Object[]) null);
        assertNotNull(result);
    }

    @Test
    public void test_println_objectArrayEmpty() {
        Object[] arr = {};
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println(Object[][])
    // ============================================

    @Test
    public void test_println_objectArray2D() {
        Object[][] arr = { { "a", "b" }, { "c", "d" } };
        String result = Arrays.println(arr);

        assertNotNull(result);
    }

    @Test
    public void test_println_objectArray2DNull() {
        String result = Arrays.println((Object[][]) null);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println(Object[][][])
    // ============================================

    @Test
    public void test_println_objectArray3D() {
        Object[][][] arr = { { { "a" } } };
        String result = Arrays.println(arr);

        assertNotNull(result);
    }

    @Test
    public void test_println_objectArray3DNull() {
        String result = Arrays.println((Object[][][]) null);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println(boolean[])
    // ============================================

    @Test
    public void test_println_booleanArray() {
        boolean[] arr = { true, false, true };
        String result = Arrays.println(arr);

        assertNotNull(result);
        assertTrue(result.contains("true"));
        assertTrue(result.contains("false"));
    }

    @Test
    public void test_println_booleanArrayNull() {
        String result = Arrays.println((boolean[]) null);
        assertNotNull(result);
    }

    @Test
    public void test_println_booleanArrayEmpty() {
        boolean[] arr = {};
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println(boolean[][])
    // ============================================

    @Test
    public void test_println_booleanArray2D() {
        boolean[][] arr = { { true, false }, { false, true } };
        String result = Arrays.println(arr);

        assertNotNull(result);
    }

    @Test
    public void test_println_booleanArray2DNull() {
        String result = Arrays.println((boolean[][]) null);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println(boolean[][][])
    // ============================================

    @Test
    public void test_println_booleanArray3D() {
        boolean[][][] arr = { { { true } } };
        String result = Arrays.println(arr);

        assertNotNull(result);
    }

    @Test
    public void test_println_booleanArray3DNull() {
        String result = Arrays.println((boolean[][][]) null);
        assertNotNull(result);
    }

    // ============================================
    // Tests for mapToObj(boolean[])
    // ============================================

    @Test
    public void test_mapToObj_booleanArray() {
        boolean[] arr = { true, false, true };
        String[] result = Arrays.mapToObj(arr, b -> b ? "T" : "F", String.class);

        assertArrayEquals(new String[] { "T", "F", "T" }, result);
    }

    @Test
    public void test_mapToObj_booleanArrayNull() {
        String[] result = Arrays.mapToObj((boolean[]) null, b -> "test", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void test_mapToObj_booleanArrayEmpty() {
        boolean[] arr = {};
        String[] result = Arrays.mapToObj(arr, b -> "test", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToObj(boolean[][])
    // ============================================

    @Test
    public void test_mapToObj_booleanArray2D() {
        boolean[][] arr = { { true, false }, { false, true } };
        String[][] result = Arrays.mapToObj(arr, b -> b ? "T" : "F", String.class);

        assertEquals(2, result.length);
        assertArrayEquals(new String[] { "T", "F" }, result[0]);
        assertArrayEquals(new String[] { "F", "T" }, result[1]);
    }

    @Test
    public void test_mapToObj_booleanArray2DNull() {
        String[][] result = Arrays.mapToObj((boolean[][]) null, b -> "test", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToObj(boolean[][][])
    // ============================================

    @Test
    public void test_mapToObj_booleanArray3D() {
        boolean[][][] arr = { { { true, false } } };
        String[][][] result = Arrays.mapToObj(arr, b -> b ? "T" : "F", String.class);

        assertEquals(1, result.length);
        assertEquals(1, result[0].length);
        assertArrayEquals(new String[] { "T", "F" }, result[0][0]);
    }

    @Test
    public void test_mapToObj_booleanArray3DNull() {
        String[][][] result = Arrays.mapToObj((boolean[][][]) null, b -> "test", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToObj(char[])
    // ============================================

    @Test
    public void test_mapToObj_charArray() {
        char[] arr = { 'a', 'b', 'c' };
        String[] result = Arrays.mapToObj(arr, c -> String.valueOf(c).toUpperCase(), String.class);

        assertArrayEquals(new String[] { "A", "B", "C" }, result);
    }

    @Test
    public void test_mapToObj_charArrayNull() {
        String[] result = Arrays.mapToObj((char[]) null, c -> "test", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToObj(byte[])
    // ============================================

    @Test
    public void test_mapToObj_byteArray() {
        byte[] arr = { 1, 2, 3 };
        String[] result = Arrays.mapToObj(arr, b -> "Byte:" + b, String.class);

        assertArrayEquals(new String[] { "Byte:1", "Byte:2", "Byte:3" }, result);
    }

    @Test
    public void test_mapToObj_byteArrayNull() {
        String[] result = Arrays.mapToObj((byte[]) null, b -> "test", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToObj(short[])
    // ============================================

    @Test
    public void test_mapToObj_shortArray() {
        short[] arr = { 10, 20, 30 };
        String[] result = Arrays.mapToObj(arr, s -> "Short:" + s, String.class);

        assertArrayEquals(new String[] { "Short:10", "Short:20", "Short:30" }, result);
    }

    @Test
    public void test_mapToObj_shortArrayNull() {
        String[] result = Arrays.mapToObj((short[]) null, s -> "test", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToObj(int[])
    // ============================================

    @Test
    public void test_mapToObj_intArray() {
        int[] arr = { 1, 2, 3 };
        String[] result = Arrays.mapToObj(arr, i -> "Int:" + i, String.class);

        assertArrayEquals(new String[] { "Int:1", "Int:2", "Int:3" }, result);
    }

    @Test
    public void test_mapToObj_intArrayNull() {
        String[] result = Arrays.mapToObj((int[]) null, i -> "test", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToObj(long[])
    // ============================================

    @Test
    public void test_mapToObj_longArray() {
        long[] arr = { 100L, 200L, 300L };
        String[] result = Arrays.mapToObj(arr, l -> "Long:" + l, String.class);

        assertArrayEquals(new String[] { "Long:100", "Long:200", "Long:300" }, result);
    }

    @Test
    public void test_mapToObj_longArrayNull() {
        String[] result = Arrays.mapToObj((long[]) null, l -> "test", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToObj(float[])
    // ============================================

    @Test
    public void test_mapToObj_floatArray() {
        float[] arr = { 1.1f, 2.2f, 3.3f };
        String[] result = Arrays.mapToObj(arr, f -> String.format("%.1f", f), String.class);

        assertArrayEquals(new String[] { "1.1", "2.2", "3.3" }, result);
    }

    @Test
    public void test_mapToObj_floatArrayNull() {
        String[] result = Arrays.mapToObj((float[]) null, f -> "test", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToObj(double[])
    // ============================================

    @Test
    public void test_mapToObj_doubleArray() {
        double[] arr = { 1.5, 2.5, 3.5 };
        String[] result = Arrays.mapToObj(arr, d -> String.format("%.1f", d), String.class);

        assertArrayEquals(new String[] { "1.5", "2.5", "3.5" }, result);
    }

    @Test
    public void test_mapToObj_doubleArrayNull() {
        String[] result = Arrays.mapToObj((double[]) null, d -> "test", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToLong(int[])
    // ============================================

    @Test
    public void test_mapToLong_intArray() {
        int[] arr = { 1, 2, 3 };
        long[] result = Arrays.mapToLong(arr, i -> i * 1000L);

        assertArrayEquals(new long[] { 1000L, 2000L, 3000L }, result);
    }

    @Test
    public void test_mapToLong_intArrayNull() {
        long[] result = Arrays.mapToLong((int[]) null, i -> 0L);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void test_mapToLong_intArrayEmpty() {
        int[] arr = {};
        long[] result = Arrays.mapToLong(arr, i -> i * 1000L);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToLong(int[][])
    // ============================================

    @Test
    public void test_mapToLong_intArray2D() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        long[][] result = Arrays.mapToLong(arr, i -> i * 1000L);

        assertEquals(2, result.length);
        assertArrayEquals(new long[] { 1000L, 2000L }, result[0]);
        assertArrayEquals(new long[] { 3000L, 4000L }, result[1]);
    }

    @Test
    public void test_mapToLong_intArray2DNull() {
        long[][] result = Arrays.mapToLong((int[][]) null, i -> 0L);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToDouble(int[])
    // ============================================

    @Test
    public void test_mapToDouble_intArray() {
        int[] arr = { 1, 2, 3 };
        double[] result = Arrays.mapToDouble(arr, i -> i * 1.5);

        assertArrayEquals(new double[] { 1.5, 3.0, 4.5 }, result);
    }

    @Test
    public void test_mapToDouble_intArrayNull() {
        double[] result = Arrays.mapToDouble((int[]) null, i -> 0.0);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToInt(long[])
    // ============================================

    @Test
    public void test_mapToInt_longArray() {
        long[] arr = { 1000L, 2000L, 3000L };
        int[] result = Arrays.mapToInt(arr, l -> (int) (l / 1000));

        assertArrayEquals(new int[] { 1, 2, 3 }, result);
    }

    @Test
    public void test_mapToInt_longArrayNull() {
        int[] result = Arrays.mapToInt((long[]) null, l -> 0);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for mapToInt(double[])
    // ============================================

    @Test
    public void test_mapToInt_doubleArray() {
        double[] arr = { 1.9, 2.9, 3.9 };
        int[] result = Arrays.mapToInt(arr, d -> (int) d);

        assertArrayEquals(new int[] { 1, 2, 3 }, result);
    }

    @Test
    public void test_mapToInt_doubleArrayNull() {
        int[] result = Arrays.mapToInt((double[]) null, d -> 0);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for updateAll(boolean[])
    // ============================================

    @Test
    public void test_updateAll_booleanArray() {
        boolean[] arr = { true, false, true };
        Arrays.updateAll(arr, b -> !b);

        assertArrayEquals(new boolean[] { false, true, false }, arr);
    }

    @Test
    public void test_updateAll_booleanArrayNull() {
        // Should not throw
        Arrays.updateAll((boolean[]) null, b -> b);
    }

    @Test
    public void test_updateAll_booleanArrayEmpty() {
        boolean[] arr = {};
        Arrays.updateAll(arr, b -> !b);
        assertEquals(0, arr.length);
    }

    // ============================================
    // Tests for updateAll(boolean[][])
    // ============================================

    @Test
    public void test_updateAll_booleanArray2D() {
        boolean[][] arr = { { true, false }, { false, true } };
        Arrays.updateAll(arr, b -> !b);

        assertArrayEquals(new boolean[] { false, true }, arr[0]);
        assertArrayEquals(new boolean[] { true, false }, arr[1]);
    }

    @Test
    public void test_updateAll_booleanArray2DNull() {
        Arrays.updateAll((boolean[][]) null, b -> b);
    }

    // ============================================
    // Tests for replaceIf(boolean[])
    // ============================================

    @Test
    public void test_replaceIf_booleanArray() {
        boolean[] arr = { true, false, true, false };
        Arrays.replaceIf(arr, b -> b == true, false);

        assertArrayEquals(new boolean[] { false, false, false, false }, arr);
    }

    @Test
    public void test_replaceIf_booleanArrayNull() {
        Arrays.replaceIf((boolean[]) null, b -> true, false);
    }

    @Test
    public void test_replaceIf_booleanArrayEmpty() {
        boolean[] arr = {};
        Arrays.replaceIf(arr, b -> true, false);
        assertEquals(0, arr.length);
    }

    // ============================================
    // Tests for reshape(boolean[], int)
    // ============================================

    @Test
    public void test_reshape_booleanArray() {
        boolean[] arr = { true, false, true, false };
        boolean[][] result = Arrays.reshape(arr, 2);

        assertEquals(2, result.length);
        assertArrayEquals(new boolean[] { true, false }, result[0]);
        assertArrayEquals(new boolean[] { true, false }, result[1]);
    }

    @Test
    public void test_reshape_booleanArrayNull() {
        boolean[][] result = Arrays.reshape((boolean[]) null, 2);
        assertEquals(0, result.length);
    }

    @Test
    public void test_reshape_booleanArrayInvalidCols() {
        boolean[] arr = { true, false, true };
        // The reshape method allows non-evenly divisible arrays using CEILING rounding
        boolean[][] result = Arrays.reshape(arr, 2);
        assertEquals(2, result.length);   // 2 rows with cols=2 each (last row has 1 element)
        assertArrayEquals(new boolean[] { true, false }, result[0]);
        assertArrayEquals(new boolean[] { true }, result[1]);
    }

    // ============================================
    // Tests for reshape(boolean[], int, int)
    // ============================================

    @Test
    public void test_reshape_booleanArray3D() {
        boolean[] arr = { true, false, true, false, true, false };
        boolean[][][] result = Arrays.reshape(arr, 2, 3);

        // With 6 elements and rows=2, cols=3 (2*3=6 per block), we get 1 block
        assertEquals(1, result.length);
        assertEquals(2, result[0].length);   // 2 rows in the block
        assertEquals(3, result[0][0].length);   // 3 columns per row
        assertArrayEquals(new boolean[] { true, false, true }, result[0][0]);
        assertArrayEquals(new boolean[] { false, true, false }, result[0][1]);
    }

    @Test
    public void test_reshape_booleanArray3DNull() {
        boolean[][][] result = Arrays.reshape((boolean[]) null, 2, 2);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for flatten(boolean[][])
    // ============================================

    @Test
    public void test_flatten_booleanArray2D() {
        boolean[][] arr = { { true, false }, { true, false } };
        boolean[] result = Arrays.flatten(arr);

        assertArrayEquals(new boolean[] { true, false, true, false }, result);
    }

    @Test
    public void test_flatten_booleanArray2DNull() {
        boolean[] result = Arrays.flatten((boolean[][]) null);
        assertEquals(0, result.length);
    }

    @Test
    public void test_flatten_booleanArray2DEmpty() {
        boolean[][] arr = {};
        boolean[] result = Arrays.flatten(arr);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for flatten(boolean[][][])
    // ============================================

    @Test
    public void test_flatten_booleanArray3D() {
        boolean[][][] arr = { { { true, false } } };
        boolean[] result = Arrays.flatten(arr);

        assertArrayEquals(new boolean[] { true, false }, result);
    }

    @Test
    public void test_flatten_booleanArray3DNull() {
        boolean[] result = Arrays.flatten((boolean[][][]) null);
        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for flatOp(boolean[][])
    // ============================================

    @Test
    public void test_flatOp_booleanArray2D() {
        boolean[][] arr = { { true, false }, { true, false } };
        int[] count = { 0 };

        Arrays.flatOp(arr, subArray -> count[0] += subArray.length);

        assertEquals(4, count[0]);
    }

    @Test
    public void test_flatOp_booleanArray2DNull() {
        Arrays.flatOp((boolean[][]) null, subArray -> {
        });
    }

    // ============================================
    // Tests for zip(boolean[], boolean[])
    // ============================================

    @Test
    public void test_zip_booleanArrays() {
        boolean[] a = { true, false, true };
        boolean[] b = { false, false, true };
        boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

        assertArrayEquals(new boolean[] { false, false, true }, result);
    }

    @Test
    public void test_zip_booleanArraysNull() {
        boolean[] a = null;
        boolean[] b = { true, false };
        boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

        assertEquals(0, result.length);
    }

    @Test
    public void test_zip_booleanArraysEmpty() {
        boolean[] a = {};
        boolean[] b = {};
        boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

        assertEquals(0, result.length);
    }

    // ============================================
    // Tests for zip(boolean[], boolean[], boolean, boolean)
    // ============================================

    @Test
    public void test_zip_booleanArraysWithDefaults() {
        boolean[] a = { true, false };
        boolean[] b = { false };
        boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

        assertEquals(2, result.length);
        assertTrue(result[0]);
        assertTrue(result[1]);
    }

    // ============================================
    // Tests for totalCountOfElements(boolean[][])
    // ============================================

    @Test
    public void test_totalCountOfElements_booleanArray2D() {
        boolean[][] arr = { { true, false }, { true, false, true } };
        long count = Arrays.totalCountOfElements(arr);

        assertEquals(5, count);
    }

    @Test
    public void test_totalCountOfElements_booleanArray2DNull() {
        long count = Arrays.totalCountOfElements((boolean[][]) null);
        assertEquals(0, count);
    }

    @Test
    public void test_totalCountOfElements_booleanArray2DEmpty() {
        boolean[][] arr = {};
        long count = Arrays.totalCountOfElements(arr);
        assertEquals(0, count);
    }

    // ============================================
    // Tests for totalCountOfElements(boolean[][][])
    // ============================================

    @Test
    public void test_totalCountOfElements_booleanArray3D() {
        boolean[][][] arr = { { { true, false }, { true } } };
        long count = Arrays.totalCountOfElements(arr);

        assertEquals(3, count);
    }

    @Test
    public void test_totalCountOfElements_booleanArray3DNull() {
        long count = Arrays.totalCountOfElements((boolean[][][]) null);
        assertEquals(0, count);
    }

    // ============================================
    // Tests for minSubArrayLen(boolean[][])
    // ============================================

    @Test
    public void test_minSubArrayLen_booleanArray2D() {
        boolean[][] arr = { { true, false, true }, { true } };
        int min = Arrays.minSubArrayLen(arr);

        assertEquals(1, min);
    }

    @Test
    public void test_minSubArrayLen_booleanArray2DNull() {
        int min = Arrays.minSubArrayLen((boolean[][]) null);
        assertEquals(0, min);
    }

    @Test
    public void test_minSubArrayLen_booleanArray2DEmpty() {
        boolean[][] arr = {};
        int min = Arrays.minSubArrayLen(arr);
        assertEquals(0, min);
    }

    // ============================================
    // Tests for maxSubArrayLen(boolean[][])
    // ============================================

    @Test
    public void test_maxSubArrayLen_booleanArray2D() {
        boolean[][] arr = { { true, false, true }, { true } };
        int max = Arrays.maxSubArrayLen(arr);

        assertEquals(3, max);
    }

    @Test
    public void test_maxSubArrayLen_booleanArray2DNull() {
        int max = Arrays.maxSubArrayLen((boolean[][]) null);
        assertEquals(0, max);
    }

    @Test
    public void test_maxSubArrayLen_booleanArray2DEmpty() {
        boolean[][] arr = {};
        int max = Arrays.maxSubArrayLen(arr);
        assertEquals(0, max);
    }

    // ============================================
    // Tests for char array operations
    // ============================================

    @Test
    public void test_updateAll_charArray() {
        char[] arr = { 'a', 'b', 'c' };
        Arrays.updateAll(arr, c -> Character.toUpperCase(c));

        assertArrayEquals(new char[] { 'A', 'B', 'C' }, arr);
    }

    @Test
    public void test_replaceIf_charArray() {
        char[] arr = { 'a', 'b', 'c', 'a' };
        Arrays.replaceIf(arr, c -> c == 'a', 'x');

        assertArrayEquals(new char[] { 'x', 'b', 'c', 'x' }, arr);
    }

    @Test
    public void test_reshape_charArray() {
        char[] arr = { 'a', 'b', 'c', 'd' };
        char[][] result = Arrays.reshape(arr, 2);

        assertEquals(2, result.length);
        assertArrayEquals(new char[] { 'a', 'b' }, result[0]);
        assertArrayEquals(new char[] { 'c', 'd' }, result[1]);
    }

    @Test
    public void test_flatten_charArray2D() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        char[] result = Arrays.flatten(arr);

        assertArrayEquals(new char[] { 'a', 'b', 'c', 'd' }, result);
    }

    @Test
    public void test_zip_charArrays() {
        char[] a = { 'a', 'b', 'c' };
        char[] b = { 'x', 'y', 'z' };
        char[] result = Arrays.zip(a, b, (x, y) -> Character.isLowerCase(x) ? x : y);

        assertArrayEquals(new char[] { 'a', 'b', 'c' }, result);
    }

    @Test
    public void test_totalCountOfElements_charArray2D() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd', 'e' } };
        long count = Arrays.totalCountOfElements(arr);

        assertEquals(5, count);
    }

    // ============================================
    // Edge cases and integration tests
    // ============================================

    @Test
    public void test_edgeCase_largeArray() {
        int[] arr = new int[10000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }

        String[] result = Arrays.mapToObj(arr, i -> "Item:" + i, String.class);
        assertEquals(10000, result.length);
        assertEquals("Item:0", result[0]);
        assertEquals("Item:9999", result[9999]);
    }

    @Test
    public void test_integration_reshapeAndFlatten() {
        boolean[] original = { true, false, true, false, true, false };
        boolean[][] reshaped = Arrays.reshape(original, 3);
        boolean[] flattened = Arrays.flatten(reshaped);

        assertArrayEquals(original, flattened);
    }

    @Test
    public void test_integration_mapAndUpdate() {
        int[] arr = { 1, 2, 3 };
        long[] mapped = Arrays.mapToLong(arr, i -> i * 1000L);

        assertArrayEquals(new long[] { 1000L, 2000L, 3000L }, mapped);
    }
}
