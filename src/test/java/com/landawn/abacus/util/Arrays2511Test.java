package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

/**
 * Comprehensive unit tests for Arrays class and its inner classes (f, ff, fff).
 * Tests cover printing methods, mapToObj methods, updateAll, replaceIf, reshape, flatten, and more.
 */
@Tag("2511")
public class Arrays2511Test extends TestBase {

    // ============ Arrays.println Tests (1D, 2D, 3D) ============

    @Test
    public void testPrintln_1D_withNullArray() {
        String result = Arrays.println((Object[]) null);
        assertEquals("null", result);
    }

    @Test
    public void testPrintln_1D_withEmptyArray() {
        String result = Arrays.println(new Object[0]);
        assertEquals("[]", result);
    }

    @Test
    public void testPrintln_1D_withValidArray() {
        Object[] arr = { "Hello", 123, "World" };
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("123"));
        assertTrue(result.contains("World"));
    }

    @Test
    public void testPrintln_2D_withNullArray() {
        String result = Arrays.println((Object[][]) null);
        assertEquals("null", result);
    }

    @Test
    public void testPrintln_2D_withEmptyArray() {
        String result = Arrays.println(new Object[0][]);
        assertEquals("[]", result);
    }

    @Test
    public void testPrintln_2D_withValidArray() {
        Object[][] arr = { { "A", "B" }, { "C", "D" } };
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("A"));
        assertTrue(result.contains("D"));
    }

    @Test
    public void testPrintln_3D_withNullArray() {
        String result = Arrays.println((Object[][][]) null);
        assertEquals("null", result);
    }

    @Test
    public void testPrintln_3D_withEmptyArray() {
        String result = Arrays.println(new Object[0][][]);
        assertEquals("[]", result);
    }

    @Test
    public void testPrintln_3D_withValidArray() {
        Object[][][] arr = { { { "A", "B" }, { "C" } }, { { "D" } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("A"));
        assertTrue(result.contains("D"));
    }

    // ============ Arrays.mapToObj Tests (boolean) ============

    @Test
    public void testMapToObj_boolean_1D_null() {
        boolean[] arr = null;
        String[] result = Arrays.mapToObj(arr, b -> b ? "YES" : "NO", String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_boolean_1D_empty() {
        boolean[] arr = new boolean[0];
        String[] result = Arrays.mapToObj(arr, b -> b ? "YES" : "NO", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_boolean_1D_valid() {
        boolean[] arr = { true, false, true };
        String[] result = Arrays.mapToObj(arr, b -> b ? "YES" : "NO", String.class);
        assertArrayEquals(new String[] { "YES", "NO", "YES" }, result);
    }

    @Test
    public void testMapToObj_boolean_2D_null() {
        boolean[][] arr = null;
        String[][] result = Arrays.mapToObj(arr, b -> b ? "T" : "F", String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_boolean_2D_valid() {
        boolean[][] arr = { { true, false }, { false, true } };
        String[][] result = Arrays.mapToObj(arr, b -> b ? "T" : "F", String.class);
        assertEquals(2, result.length);
        assertArrayEquals(new String[] { "T", "F" }, result[0]);
        assertArrayEquals(new String[] { "F", "T" }, result[1]);
    }

    @Test
    public void testMapToObj_boolean_3D_null() {
        boolean[][][] arr = null;
        Integer[][][] result = Arrays.mapToObj(arr, b -> b ? 1 : 0, Integer.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_boolean_3D_valid() {
        boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
        Integer[][][] result = Arrays.mapToObj(arr, b -> b ? 1 : 0, Integer.class);
        assertEquals(2, result.length);
        assertEquals(1, result[0][0][0]);
        assertEquals(0, result[0][0][1]);
        assertEquals(1, result[0][1][0]);
        assertEquals(0, result[1][0][0]);
    }

    // ============ Arrays.mapToObj Tests (char) ============

    @Test
    public void testMapToObj_char_1D_null() {
        char[] arr = null;
        String[] result = Arrays.mapToObj(arr, c -> String.valueOf(c).toUpperCase(), String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_char_1D_valid() {
        char[] arr = { 'a', 'b', 'c' };
        String[] result = Arrays.mapToObj(arr, c -> String.valueOf(c).toUpperCase(), String.class);
        assertArrayEquals(new String[] { "A", "B", "C" }, result);
    }

    @Test
    public void testMapToObj_char_2D_valid() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        Integer[][] result = Arrays.mapToObj(arr, c -> (int) c, Integer.class);
        assertEquals(2, result.length);
        assertEquals(97, result[0][0]);
        assertEquals(100, result[1][1]);
    }

    @Test
    public void testMapToObj_char_3D_valid() {
        char[][][] arr = { { { 'x', 'y' } } };
        String[][][] result = Arrays.mapToObj(arr, c -> String.valueOf(c), String.class);
        assertEquals(1, result.length);
        assertEquals("x", result[0][0][0]);
        assertEquals("y", result[0][0][1]);
    }

    // ============ Arrays.mapToObj Tests (byte) ============

    @Test
    public void testMapToObj_byte_1D_null() {
        byte[] arr = null;
        String[] result = Arrays.mapToObj(arr, b -> String.format("%02X", b), String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_byte_1D_valid() {
        byte[] arr = { 1, 2, 15, 16 };
        String[] result = Arrays.mapToObj(arr, b -> String.format("%02X", b), String.class);
        assertEquals("01", result[0]);
        assertEquals("02", result[1]);
        assertEquals("0F", result[2]);
        assertEquals("10", result[3]);
    }

    @Test
    public void testMapToObj_byte_2D_valid() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        Integer[][] result = Arrays.mapToObj(arr, b -> (int) b * 2, Integer.class);
        assertEquals(2, result[0][0]);
        assertEquals(4, result[0][1]);
        assertEquals(6, result[1][0]);
        assertEquals(8, result[1][1]);
    }

    @Test
    public void testMapToObj_byte_3D_valid() {
        byte[][][] arr = { { { 1, 2 } } };
        String[][][] result = Arrays.mapToObj(arr, b -> "byte:" + b, String.class);
        assertEquals("byte:1", result[0][0][0]);
        assertEquals("byte:2", result[0][0][1]);
    }

    // ============ Arrays.mapToObj Tests (int) ============

    @Test
    public void testMapToObj_int_1D_null() {
        int[] arr = null;
        String[] result = Arrays.mapToObj(arr, i -> "num:" + i, String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_int_1D_valid() {
        int[] arr = { 1, 2, 3 };
        String[] result = Arrays.mapToObj(arr, i -> "num:" + i, String.class);
        assertArrayEquals(new String[] { "num:1", "num:2", "num:3" }, result);
    }

    @Test
    public void testMapToObj_int_2D_valid() {
        int[][] arr = { { 10, 20 }, { 30, 40 } };
        String[][] result = Arrays.mapToObj(arr, i -> String.valueOf(i), String.class);
        assertEquals("10", result[0][0]);
        assertEquals("40", result[1][1]);
    }

    @Test
    public void testMapToObj_int_3D_valid() {
        int[][][] arr = { { { 1, 2 }, { 3 } } };
        Double[][][] result = Arrays.mapToObj(arr, i -> i * 1.5, Double.class);
        assertEquals(1.5, result[0][0][0], 0.001);
        assertEquals(3.0, result[0][0][1], 0.001);
        assertEquals(4.5, result[0][1][0], 0.001);
    }

    // ============ Arrays.mapToObj Tests (long) ============

    @Test
    public void testMapToObj_long_1D_null() {
        long[] arr = null;
        String[] result = Arrays.mapToObj(arr, l -> "L" + l, String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_long_1D_valid() {
        long[] arr = { 100L, 200L, 300L };
        String[] result = Arrays.mapToObj(arr, l -> "L" + l, String.class);
        assertArrayEquals(new String[] { "L100", "L200", "L300" }, result);
    }

    @Test
    public void testMapToObj_long_2D_valid() {
        long[][] arr = { { 1L, 2L }, { 3L, 4L } };
        Integer[][] result = Arrays.mapToObj(arr, l -> (int) (l * 10), Integer.class);
        assertEquals(10, result[0][0]);
        assertEquals(40, result[1][1]);
    }

    @Test
    public void testMapToObj_long_3D_valid() {
        long[][][] arr = { { { 5L } } };
        String[][][] result = Arrays.mapToObj(arr, l -> "long:" + l, String.class);
        assertEquals("long:5", result[0][0][0]);
    }

    // ============ Arrays.mapToObj Tests (double) ============

    @Test
    public void testMapToObj_double_1D_null() {
        double[] arr = null;
        String[] result = Arrays.mapToObj(arr, d -> String.format("%.2f", d), String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_double_1D_valid() {
        double[] arr = { 1.5, 2.7, 3.9 };
        String[] result = Arrays.mapToObj(arr, d -> String.format("%.1f", d), String.class);
        assertArrayEquals(new String[] { "1.5", "2.7", "3.9" }, result);
    }

    @Test
    public void testMapToObj_double_2D_valid() {
        double[][] arr = { { 1.1, 2.2 }, { 3.3, 4.4 } };
        Integer[][] result = Arrays.mapToObj(arr, d -> (int) Math.round(d), Integer.class);
        assertEquals(1, result[0][0]);
        assertEquals(4, result[1][1]);
    }

    @Test
    public void testMapToObj_double_3D_valid() {
        double[][][] arr = { { { 1.5, 2.5 } } };
        String[][][] result = Arrays.mapToObj(arr, d -> "D" + d, String.class);
        assertEquals("D1.5", result[0][0][0]);
        assertEquals("D2.5", result[0][0][1]);
    }

    // ============ Arrays.mapToLong Tests (int) ============

    @Test
    public void testMapToLong_int_1D_null() {
        int[] arr = null;
        long[] result = Arrays.mapToLong(arr, i -> i * 2L);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToLong_int_1D_valid() {
        int[] arr = { 1, 2, 3 };
        long[] result = Arrays.mapToLong(arr, i -> i * 10L);
        assertArrayEquals(new long[] { 10L, 20L, 30L }, result);
    }

    @Test
    public void testMapToLong_int_2D_valid() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        long[][] result = Arrays.mapToLong(arr, i -> i * 100L);
        assertEquals(2, result.length);
        assertArrayEquals(new long[] { 100L, 200L }, result[0]);
        assertArrayEquals(new long[] { 300L, 400L }, result[1]);
    }

    @Test
    public void testMapToLong_int_3D_valid() {
        int[][][] arr = { { { 1, 2 } } };
        long[][][] result = Arrays.mapToLong(arr, i -> i * 5L);
        assertEquals(5L, result[0][0][0]);
        assertEquals(10L, result[0][0][1]);
    }

    // ============ Arrays.mapToDouble Tests (int) ============

    @Test
    public void testMapToDouble_int_1D_null() {
        int[] arr = null;
        double[] result = Arrays.mapToDouble(arr, i -> i * 1.5);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToDouble_int_1D_valid() {
        int[] arr = { 1, 2, 3 };
        double[] result = Arrays.mapToDouble(arr, i -> i * 1.5);
        assertArrayEquals(new double[] { 1.5, 3.0, 4.5 }, result, 0.001);
    }

    @Test
    public void testMapToDouble_int_2D_valid() {
        int[][] arr = { { 2, 4 }, { 6, 8 } };
        double[][] result = Arrays.mapToDouble(arr, i -> i / 2.0);
        assertEquals(1.0, result[0][0], 0.001);
        assertEquals(4.0, result[1][1], 0.001);
    }

    @Test
    public void testMapToDouble_int_3D_valid() {
        int[][][] arr = { { { 10 } } };
        double[][][] result = Arrays.mapToDouble(arr, i -> i * 0.1);
        assertEquals(1.0, result[0][0][0], 0.001);
    }

    // ============ Arrays.updateAll Tests (boolean) ============

    @Test
    public void testUpdateAll_boolean_1D_null() {
        boolean[] arr = null;
        Arrays.updateAll(arr, b -> !b);
        assertNull(arr);
    }

    @Test
    public void testUpdateAll_boolean_1D_valid() {
        boolean[] arr = { true, false, true };
        Arrays.updateAll(arr, b -> !b);
        assertArrayEquals(new boolean[] { false, true, false }, arr);
    }

    @Test
    public void testUpdateAll_boolean_2D_valid() {
        boolean[][] arr = { { true, false }, { false, true } };
        Arrays.updateAll(arr, b -> !b);
        assertArrayEquals(new boolean[] { false, true }, arr[0]);
        assertArrayEquals(new boolean[] { true, false }, arr[1]);
    }

    @Test
    public void testUpdateAll_boolean_3D_valid() {
        boolean[][][] arr = { { { true }, { false } } };
        Arrays.updateAll(arr, b -> !b);
        assertFalse(arr[0][0][0]);
        assertTrue(arr[0][1][0]);
    }

    // ============ Arrays.updateAll Tests (char) ============

    @Test
    public void testUpdateAll_char_1D_valid() {
        char[] arr = { 'a', 'b', 'c' };
        Arrays.updateAll(arr, c -> Character.toUpperCase(c));
        assertArrayEquals(new char[] { 'A', 'B', 'C' }, arr);
    }

    @Test
    public void testUpdateAll_char_2D_valid() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        Arrays.updateAll(arr, c -> Character.toUpperCase(c));
        assertArrayEquals(new char[] { 'A', 'B' }, arr[0]);
        assertArrayEquals(new char[] { 'C', 'D' }, arr[1]);
    }

    // ============ Arrays.replaceIf Tests (boolean) ============

    @Test
    public void testReplaceIf_boolean_1D_null() {
        boolean[] arr = null;
        Arrays.replaceIf(arr, b -> b, false);
        assertNull(arr);
    }

    @Test
    public void testReplaceIf_boolean_1D_valid() {
        boolean[] arr = { true, false, true, false };
        Arrays.replaceIf(arr, b -> b, false);
        assertArrayEquals(new boolean[] { false, false, false, false }, arr);
    }

    @Test
    public void testReplaceIf_boolean_2D_valid() {
        boolean[][] arr = { { true, false }, { true, true } };
        Arrays.replaceIf(arr, b -> !b, true);
        assertArrayEquals(new boolean[] { true, true }, arr[0]);
        assertArrayEquals(new boolean[] { true, true }, arr[1]);
    }

    @Test
    public void testReplaceIf_boolean_3D_valid() {
        boolean[][][] arr = { { { true, false } } };
        Arrays.replaceIf(arr, b -> b, false);
        assertFalse(arr[0][0][0]);
        assertFalse(arr[0][0][1]);
    }

    // ============ Arrays.replaceIf Tests (char) ============

    @Test
    public void testReplaceIf_char_1D_valid() {
        char[] arr = { 'a', 'b', 'a', 'c' };
        Arrays.replaceIf(arr, c -> c == 'a', 'X');
        assertArrayEquals(new char[] { 'X', 'b', 'X', 'c' }, arr);
    }

    @Test
    public void testReplaceIf_char_2D_valid() {
        char[][] arr = { { 'a', 'b' }, { 'a', 'a' } };
        Arrays.replaceIf(arr, c -> c == 'a', 'Z');
        assertArrayEquals(new char[] { 'Z', 'b' }, arr[0]);
        assertArrayEquals(new char[] { 'Z', 'Z' }, arr[1]);
    }

    // ============ Arrays.reshape Tests (boolean) ============

    @Test
    public void testReshape_boolean_2D_validEvenDivision() {
        boolean[] arr = { true, false, true, false, true, false };
        boolean[][] result = Arrays.reshape(arr, 2);
        assertEquals(3, result.length);
        assertArrayEquals(new boolean[] { true, false }, result[0]);
        assertArrayEquals(new boolean[] { true, false }, result[1]);
        assertArrayEquals(new boolean[] { true, false }, result[2]);
    }

    @Test
    public void testReshape_boolean_2D_unevenDivision() {
        boolean[] arr = { true, false, true, false, true };
        boolean[][] result = Arrays.reshape(arr, 2);
        assertEquals(3, result.length);
        assertArrayEquals(new boolean[] { true, false }, result[0]);
        assertArrayEquals(new boolean[] { true, false }, result[1]);
        assertArrayEquals(new boolean[] { true }, result[2]);
    }

    @Test
    public void testReshape_boolean_2D_invalidCols() {
        boolean[] arr = { true, false };
        assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, 0));
        assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, -1));
    }

    @Test
    public void testReshape_boolean_3D_valid() {
        boolean[] arr = { true, false, true, false, true, false, true, false };
        boolean[][][] result = Arrays.reshape(arr, 2, 2);
        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertArrayEquals(new boolean[] { true, false }, result[0][0]);
        assertArrayEquals(new boolean[] { true, false }, result[0][1]);
        assertArrayEquals(new boolean[] { true, false }, result[1][0]);
        assertArrayEquals(new boolean[] { true, false }, result[1][1]);
    }

    @Test
    public void testReshape_boolean_3D_invalidDimensions() {
        boolean[] arr = { true };
        assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, -1, 1));
    }

    // ============ Arrays.reshape Tests (char) ============

    @Test
    public void testReshape_char_2D_valid() {
        char[] arr = { 'a', 'b', 'c', 'd', 'e', 'f' };
        char[][] result = Arrays.reshape(arr, 3);
        assertEquals(2, result.length);
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, result[0]);
        assertArrayEquals(new char[] { 'd', 'e', 'f' }, result[1]);
    }

    @Test
    public void testReshape_char_3D_valid() {
        char[] arr = { 'a', 'b', 'c', 'd' };
        char[][][] result = Arrays.reshape(arr, 1, 2);
        assertEquals(2, result.length);
        assertArrayEquals(new char[] { 'a', 'b' }, result[0][0]);
        assertArrayEquals(new char[] { 'c', 'd' }, result[1][0]);
    }

    // ============ Arrays.flatten Tests (boolean) ============

    @Test
    public void testFlatten_boolean_2D_valid() {
        boolean[][] arr = { { true, false }, { true }, { false, true, false } };
        boolean[] result = Arrays.flatten(arr);
        assertArrayEquals(new boolean[] { true, false, true, false, true, false }, result);
    }

    @Test
    public void testFlatten_boolean_2D_withNullSubArrays() {
        boolean[][] arr = { { true }, null, { false } };
        boolean[] result = Arrays.flatten(arr);
        assertArrayEquals(new boolean[] { true, false }, result);
    }

    @Test
    public void testFlatten_boolean_3D_valid() {
        boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
        boolean[] result = Arrays.flatten(arr);
        assertArrayEquals(new boolean[] { true, false, true, false }, result);
    }

    // ============ Arrays.flatten Tests (char) ============

    @Test
    public void testFlatten_char_2D_valid() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd', 'e' } };
        char[] result = Arrays.flatten(arr);
        assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e' }, result);
    }

    @Test
    public void testFlatten_char_3D_valid() {
        char[][][] arr = { { { 'x' }, { 'y', 'z' } } };
        char[] result = Arrays.flatten(arr);
        assertArrayEquals(new char[] { 'x', 'y', 'z' }, result);
    }

    // ============ Arrays.flatOp Tests (boolean) ============

    @Test
    public void testFlatOp_boolean_2D_null() {
        boolean[][] arr = null;
        Arrays.flatOp(arr, flat -> {
            // Should not execute
            throw new RuntimeException("Should not be called");
        });
    }

    @Test
    public void testFlatOp_boolean_2D_valid() {
        boolean[][] arr = { { true, false, true }, { false, true, false } };
        Arrays.flatOp(arr, flat -> {
            for (int i = 0; i < flat.length; i++) {
                flat[i] = !flat[i];
            }
        });
        assertArrayEquals(new boolean[] { false, true, false }, arr[0]);
        assertArrayEquals(new boolean[] { true, false, true }, arr[1]);
    }

    @Test
    public void testFlatOp_boolean_3D_valid() {
        boolean[][][] arr = { { { true, false } }, { { false, true } } };
        Arrays.flatOp(arr, flat -> {
            for (int i = 0; i < flat.length; i++) {
                flat[i] = true;
            }
        });
        assertTrue(arr[0][0][0]);
        assertTrue(arr[0][0][1]);
        assertTrue(arr[1][0][0]);
        assertTrue(arr[1][0][1]);
    }

    // ============ Arrays.zip Tests (boolean) ============

    @Test
    public void testZip_boolean_1D_sameLength() {
        boolean[] a = { true, false, true };
        boolean[] b = { false, false, true };
        boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);
        assertArrayEquals(new boolean[] { false, false, true }, result);
    }

    @Test
    public void testZip_boolean_1D_differentLength() {
        boolean[] a = { true, false };
        boolean[] b = { false, false, true };
        boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);
        assertArrayEquals(new boolean[] { true, false, true }, result);
    }

    @Test
    public void testZip_boolean_1D_threeArrays() {
        boolean[] a = { true, false };
        boolean[] b = { false, true };
        boolean[] c = { true, true };
        boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> (x || y) && z);
        assertArrayEquals(new boolean[] { true, true }, result);
    }

    @Test
    public void testZip_boolean_2D_sameLength() {
        boolean[][] a = { { true, false }, { true, true } };
        boolean[][] b = { { false, false }, { true, false } };
        boolean[][] result = Arrays.zip(a, b, (x, y) -> x && y);
        assertEquals(2, result.length);
        assertArrayEquals(new boolean[] { false, false }, result[0]);
        assertArrayEquals(new boolean[] { true, false }, result[1]);
    }

    @Test
    public void testZip_boolean_3D_valid() {
        boolean[][][] a = { { { true } } };
        boolean[][][] b = { { { false } } };
        boolean[][][] result = Arrays.zip(a, b, (x, y) -> x || y);
        assertTrue(result[0][0][0]);
    }

    // ============ Arrays.zip Tests (char) ============

    @Test
    public void testZip_char_1D_sameLength() {
        char[] a = { 'a', 'b', 'c' };
        char[] b = { 'x', 'y', 'z' };
        char[] result = Arrays.zip(a, b, (x, y) -> x);
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, result);
    }

    @Test
    public void testZip_char_1D_differentLength() {
        char[] a = { 'a', 'b' };
        char[] b = { 'x', 'y', 'z' };
        char[] result = Arrays.zip(a, b, '-', '+', (x, y) -> x);
        assertArrayEquals(new char[] { 'a', 'b', '-' }, result);
    }

    @Test
    public void testZip_char_2D_valid() {
        char[][] a = { { 'a', 'b' } };
        char[][] b = { { 'x', 'y' } };
        char[][] result = Arrays.zip(a, b, (x, y) -> y);
        assertArrayEquals(new char[] { 'x', 'y' }, result[0]);
    }

    // ============ Arrays.totalCountOfElements Tests ============

    @Test
    public void testTotalCountOfElements_boolean_2D() {
        boolean[][] arr = { { true, false }, { true }, { false, true, false } };
        long count = Arrays.totalCountOfElements(arr);
        assertEquals(6L, count);
    }

    @Test
    public void testTotalCountOfElements_boolean_2D_withNulls() {
        boolean[][] arr = { { true }, null, { false, false } };
        long count = Arrays.totalCountOfElements(arr);
        assertEquals(3L, count);
    }

    @Test
    public void testTotalCountOfElements_boolean_3D() {
        boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
        long count = Arrays.totalCountOfElements(arr);
        assertEquals(4L, count);
    }

    // ============ Arrays.minSubArrayLen Tests ============

    @Test
    public void testMinSubArrayLen_boolean_2D() {
        boolean[][] arr = { { true, false, true }, { false }, { true, false } };
        int minLen = Arrays.minSubArrayLen(arr);
        assertEquals(1, minLen);
    }

    @Test
    public void testMinSubArrayLen_boolean_2D_withNulls() {
        boolean[][] arr = { { true, false }, null, { true } };
        int minLen = Arrays.minSubArrayLen(arr);
        assertEquals(0, minLen);
    }

    // ============ Arrays.maxSubArrayLen Tests ============

    @Test
    public void testMaxSubArrayLen_boolean_2D() {
        boolean[][] arr = { { true, false, true }, { false }, { true, false } };
        int maxLen = Arrays.maxSubArrayLen(arr);
        assertEquals(3, maxLen);
    }

    @Test
    public void testMaxSubArrayLen_boolean_2D_withNulls() {
        boolean[][] arr = { { true }, null, { true, false, false } };
        int maxLen = Arrays.maxSubArrayLen(arr);
        assertEquals(3, maxLen);
    }

    // ============ Arrays.println Tests for primitives ============

    @Test
    public void testPrintln_boolean_1D_null() {
        String result = Arrays.println((boolean[]) null);
        assertEquals("null", result);
    }

    @Test
    public void testPrintln_boolean_1D_empty() {
        String result = Arrays.println(new boolean[0]);
        assertEquals("[]", result);
    }

    @Test
    public void testPrintln_boolean_1D_valid() {
        boolean[] arr = { true, false, true };
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("true"));
        assertTrue(result.contains("false"));
    }

    @Test
    public void testPrintln_boolean_2D_valid() {
        boolean[][] arr = { { true, false }, { false, true } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_boolean_3D_valid() {
        boolean[][][] arr = { { { true } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============ Inner class f Tests ============

    @Test
    public void testF_map_null() {
        String[] arr = null;
        Integer[] result = Arrays.f.map(arr, Integer::valueOf, Integer.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testF_map_valid() {
        String[] arr = { "1", "2", "3" };
        Integer[] result = Arrays.f.map(arr, Integer::valueOf, Integer.class);
        assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
    }

    @Test
    public void testF_mapToBoolean_null() {
        String[] arr = null;
        boolean[] result = Arrays.f.mapToBoolean(arr, s -> s.length() > 3);
        assertEquals(0, result.length);
    }

    @Test
    public void testF_mapToBoolean_valid() {
        String[] arr = { "hello", "hi", "world", "go" };
        boolean[] result = Arrays.f.mapToBoolean(arr, s -> s.length() > 3);
        assertArrayEquals(new boolean[] { true, false, true, false }, result);
    }

    @Test
    public void testF_mapToChar_null() {
        String[] arr = null;
        char[] result = Arrays.f.mapToChar(arr, s -> s.charAt(0));
        assertEquals(0, result.length);
    }

    @Test
    public void testF_mapToChar_valid() {
        String[] arr = { "apple", "banana", "cherry" };
        char[] result = Arrays.f.mapToChar(arr, s -> s.charAt(0));
        assertArrayEquals(new char[] { 'a', 'b', 'c' }, result);
    }

    @Test
    public void testF_mapToByte_null() {
        String[] arr = null;
        byte[] result = Arrays.f.mapToByte(arr, s -> (byte) s.length());
        assertEquals(0, result.length);
    }

    @Test
    public void testF_mapToByte_valid() {
        String[] arr = { "a", "ab", "abc" };
        byte[] result = Arrays.f.mapToByte(arr, s -> (byte) s.length());
        assertArrayEquals(new byte[] { 1, 2, 3 }, result);
    }

    @Test
    public void testF_mapToShort_null() {
        String[] arr = null;
        short[] result = Arrays.f.mapToShort(arr, s -> (short) s.length());
        assertEquals(0, result.length);
    }

    @Test
    public void testF_mapToShort_valid() {
        String[] arr = { "x", "xy", "xyz" };
        short[] result = Arrays.f.mapToShort(arr, s -> (short) s.length());
        assertArrayEquals(new short[] { 1, 2, 3 }, result);
    }

    @Test
    public void testF_mapToInt_null() {
        String[] arr = null;
        int[] result = Arrays.f.mapToInt(arr, String::length);
        assertEquals(0, result.length);
    }

    @Test
    public void testF_mapToInt_valid() {
        String[] arr = { "a", "ab", "abc", "abcd" };
        int[] result = Arrays.f.mapToInt(arr, String::length);
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, result);
    }

    @Test
    public void testF_mapToLong_null() {
        String[] arr = null;
        long[] result = Arrays.f.mapToLong(arr, s -> (long) s.length());
        assertEquals(0, result.length);
    }

    @Test
    public void testF_mapToLong_valid() {
        String[] arr = { "abc", "defg" };
        long[] result = Arrays.f.mapToLong(arr, s -> (long) s.length());
        assertArrayEquals(new long[] { 3L, 4L }, result);
    }

    @Test
    public void testF_mapToFloat_null() {
        String[] arr = null;
        float[] result = Arrays.f.mapToFloat(arr, s -> (float) s.length());
        assertEquals(0, result.length);
    }

    @Test
    public void testF_mapToFloat_valid() {
        String[] arr = { "ab", "abc" };
        float[] result = Arrays.f.mapToFloat(arr, s -> (float) s.length());
        assertArrayEquals(new float[] { 2.0f, 3.0f }, result, 0.001f);
    }

    @Test
    public void testF_mapToDouble_null() {
        String[] arr = null;
        double[] result = Arrays.f.mapToDouble(arr, s -> (double) s.length());
        assertEquals(0, result.length);
    }

    @Test
    public void testF_mapToDouble_valid() {
        String[] arr = { "x", "xyz" };
        double[] result = Arrays.f.mapToDouble(arr, s -> (double) s.length());
        assertArrayEquals(new double[] { 1.0, 3.0 }, result, 0.001);
    }

    // ============ Inner class ff Tests ============

    @Test
    public void testFF_updateAll_null() {
        String[][] arr = null;
        Arrays.ff.updateAll(arr, String::toUpperCase);
        assertNull(arr);
    }

    @Test
    public void testFF_updateAll_valid() {
        String[][] arr = { { "hello", "world" }, { "foo", "bar" } };
        Arrays.ff.updateAll(arr, String::toUpperCase);
        assertEquals("HELLO", arr[0][0]);
        assertEquals("WORLD", arr[0][1]);
        assertEquals("FOO", arr[1][0]);
        assertEquals("BAR", arr[1][1]);
    }

    @Test
    public void testFF_replaceIf_null() {
        Integer[][] arr = null;
        Arrays.ff.replaceIf(arr, val -> val == null, 0);
        assertNull(arr);
    }

    @Test
    public void testFF_replaceIf_valid() {
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
    public void testFF_reshape_validEvenDivision() {
        Integer[] arr = { 1, 2, 3, 4, 5, 6 };
        Integer[][] result = Arrays.ff.reshape(arr, 3);
        assertEquals(2, result.length);
        assertArrayEquals(new Integer[] { 1, 2, 3 }, result[0]);
        assertArrayEquals(new Integer[] { 4, 5, 6 }, result[1]);
    }

    @Test
    public void testFF_reshape_unevenDivision() {
        Integer[] arr = { 1, 2, 3, 4, 5, 6, 7 };
        Integer[][] result = Arrays.ff.reshape(arr, 3);
        assertEquals(3, result.length);
        assertArrayEquals(new Integer[] { 1, 2, 3 }, result[0]);
        assertArrayEquals(new Integer[] { 4, 5, 6 }, result[1]);
        assertArrayEquals(new Integer[] { 7 }, result[2]);
    }

    @Test
    public void testFF_reshape_invalidCols() {
        Integer[] arr = { 1, 2, 3 };
        assertThrows(IllegalArgumentException.class, () -> Arrays.ff.reshape(arr, 0));
        assertThrows(IllegalArgumentException.class, () -> Arrays.ff.reshape(arr, -1));
    }

    @Test
    public void testFF_flatten_valid() {
        Integer[][] arr = { { 1, 2 }, { 3, 4, 5 }, { 6 } };
        Integer[] result = Arrays.ff.flatten(arr);
        assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, result);
    }

    @Test
    public void testFF_flatten_withNullSubArrays() {
        Integer[][] arr = { { 1, 2 }, null, { 3 } };
        Integer[] result = Arrays.ff.flatten(arr);
        assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
    }

    @Test
    public void testFF_flatten_nullArray() {
        Integer[][] arr = null;
        assertThrows(IllegalArgumentException.class, () -> Arrays.ff.flatten(arr));
    }

    @Test
    public void testFF_flatOp_null() {
        String[][] arr = null;
        Arrays.ff.flatOp(arr, flat -> {
            throw new RuntimeException("Should not be called");
        });
    }

    @Test
    public void testFF_flatOp_valid() {
        Integer[][] arr = { { 3, 1, 4 }, { 1, 5, 9 } };
        Arrays.ff.flatOp(arr, flat -> java.util.Arrays.sort(flat));
        assertArrayEquals(new Integer[] { 1, 1, 3 }, arr[0]);
        assertArrayEquals(new Integer[] { 4, 5, 9 }, arr[1]);
    }

    @Test
    public void testFF_map_unaryOperator() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        String[][] result = Arrays.ff.map(arr, String::toUpperCase);
        assertEquals("A", result[0][0]);
        assertEquals("B", result[0][1]);
        assertEquals("C", result[1][0]);
        assertEquals("D", result[1][1]);
    }

    @Test
    public void testFF_map_function() {
        String[][] arr = { { "1", "2" }, { "3", "4" } };
        Integer[][] result = Arrays.ff.map(arr, Integer::valueOf, Integer.class);
        assertEquals(1, result[0][0]);
        assertEquals(2, result[0][1]);
        assertEquals(3, result[1][0]);
        assertEquals(4, result[1][1]);
    }

    @Test
    public void testFF_mapToBoolean() {
        String[][] arr = { { "hello", "hi" }, { "world", "go" } };
        boolean[][] result = Arrays.ff.mapToBoolean(arr, s -> s.length() > 3);
        assertArrayEquals(new boolean[] { true, false }, result[0]);
        assertArrayEquals(new boolean[] { true, false }, result[1]);
    }

    @Test
    public void testFF_mapToChar() {
        String[][] arr = { { "apple", "banana" }, { "cherry", "date" } };
        char[][] result = Arrays.ff.mapToChar(arr, s -> s.charAt(0));
        assertArrayEquals(new char[] { 'a', 'b' }, result[0]);
        assertArrayEquals(new char[] { 'c', 'd' }, result[1]);
    }

    @Test
    public void testFF_mapToByte() {
        String[][] arr = { { "a", "ab" }, { "abc", "abcd" } };
        byte[][] result = Arrays.ff.mapToByte(arr, s -> (byte) s.length());
        assertArrayEquals(new byte[] { 1, 2 }, result[0]);
        assertArrayEquals(new byte[] { 3, 4 }, result[1]);
    }

    @Test
    public void testFF_mapToShort() {
        String[][] arr = { { "x", "xy" } };
        short[][] result = Arrays.ff.mapToShort(arr, s -> (short) s.length());
        assertArrayEquals(new short[] { 1, 2 }, result[0]);
    }

    @Test
    public void testFF_mapToInt() {
        String[][] arr = { { "a", "ab", "abc" } };
        int[][] result = Arrays.ff.mapToInt(arr, String::length);
        assertArrayEquals(new int[] { 1, 2, 3 }, result[0]);
    }

    @Test
    public void testFF_mapToLong() {
        String[][] arr = { { "abc", "defg" } };
        long[][] result = Arrays.ff.mapToLong(arr, s -> (long) s.length());
        assertArrayEquals(new long[] { 3L, 4L }, result[0]);
    }

    @Test
    public void testFF_mapToFloat() {
        String[][] arr = { { "ab", "abc" } };
        float[][] result = Arrays.ff.mapToFloat(arr, s -> (float) s.length());
        assertArrayEquals(new float[] { 2.0f, 3.0f }, result[0], 0.001f);
    }

    @Test
    public void testFF_mapToDouble() {
        String[][] arr = { { "x", "xyz" } };
        double[][] result = Arrays.ff.mapToDouble(arr, s -> (double) s.length());
        assertArrayEquals(new double[] { 1.0, 3.0 }, result[0], 0.001);
    }

    @Test
    public void testFF_zip_twoArrays_sameLength() {
        Integer[][] a = { { 1, 2 }, { 3, 4 } };
        Integer[][] b = { { 10, 20 }, { 30, 40 } };
        Integer[][] result = Arrays.ff.zip(a, b, (x, y) -> x + y);
        assertArrayEquals(new Integer[] { 11, 22 }, result[0]);
        assertArrayEquals(new Integer[] { 33, 44 }, result[1]);
    }

    @Test
    public void testFF_zip_twoArrays_differentTypes() {
        Integer[][] a = { { 1, 2 } };
        String[][] b = { { "A", "B" } };
        String[][] result = Arrays.ff.zip(a, b, (x, y) -> x + y, String.class);
        assertArrayEquals(new String[] { "1A", "2B" }, result[0]);
    }

    @Test
    public void testFF_zip_twoArrays_withDefaults() {
        Integer[][] a = { { 1, 2 } };
        Integer[][] b = { { 10 } };
        Integer[][] result = Arrays.ff.zip(a, b, 0, 0, (x, y) -> x + y);
        assertArrayEquals(new Integer[] { 11, 2 }, result[0]);
    }

    @Test
    public void testFF_zip_threeArrays() {
        Integer[][] a = { { 1, 2 } };
        Integer[][] b = { { 10, 20 } };
        Integer[][] c = { { 100, 200 } };
        Integer[][] result = Arrays.ff.zip(a, b, c, (x, y, z) -> x + y + z);
        assertArrayEquals(new Integer[] { 111, 222 }, result[0]);
    }

    @Test
    public void testFF_totalCountOfElements() {
        Object[][] arr = { { "a", "b" }, { "c" }, { "d", "e", "f" } };
        long count = Arrays.ff.totalCountOfElements(arr);
        assertEquals(6L, count);
    }

    @Test
    public void testFF_minSubArrayLen() {
        Object[][] arr = { { "a", "b", "c" }, { "d" }, { "e", "f" } };
        int minLen = Arrays.ff.minSubArrayLen(arr);
        assertEquals(1, minLen);
    }

    @Test
    public void testFF_maxSubArrayLen() {
        Object[][] arr = { { "a", "b", "c" }, { "d" }, { "e", "f" } };
        int maxLen = Arrays.ff.maxSubArrayLen(arr);
        assertEquals(3, maxLen);
    }

    // ============ Inner class fff Tests ============

    @Test
    public void testFFF_updateAll_null() {
        String[][][] arr = null;
        Arrays.fff.updateAll(arr, String::toUpperCase);
        assertNull(arr);
    }

    @Test
    public void testFFF_updateAll_valid() {
        String[][][] arr = { { { "hello", "world" } }, { { "foo", "bar" } } };
        Arrays.fff.updateAll(arr, String::toUpperCase);
        assertEquals("HELLO", arr[0][0][0]);
        assertEquals("WORLD", arr[0][0][1]);
        assertEquals("FOO", arr[1][0][0]);
        assertEquals("BAR", arr[1][0][1]);
    }

    @Test
    public void testFFF_replaceIf_null() {
        Integer[][][] arr = null;
        Arrays.fff.replaceIf(arr, val -> val == null, 0);
        assertNull(arr);
    }

    @Test
    public void testFFF_replaceIf_valid() {
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
    public void testFFF_reshape_valid() {
        Integer[] arr = { 1, 2, 3, 4, 5, 6, 7, 8 };
        Integer[][][] result = Arrays.fff.reshape(arr, 2, 2);
        assertEquals(2, result.length);
        assertArrayEquals(new Integer[] { 1, 2 }, result[0][0]);
        assertArrayEquals(new Integer[] { 3, 4 }, result[0][1]);
        assertArrayEquals(new Integer[] { 5, 6 }, result[1][0]);
        assertArrayEquals(new Integer[] { 7, 8 }, result[1][1]);
    }

    @Test
    public void testFFF_reshape_unevenDivision() {
        Integer[] arr = { 1, 2, 3, 4, 5 };
        Integer[][][] result = Arrays.fff.reshape(arr, 2, 2);
        assertEquals(2, result.length);
        assertArrayEquals(new Integer[] { 1, 2 }, result[0][0]);
        assertArrayEquals(new Integer[] { 3, 4 }, result[0][1]);
        assertArrayEquals(new Integer[] { 5 }, result[1][0]);
    }

    @Test
    public void testFFF_reshape_invalidDimensions() {
        Integer[] arr = { 1, 2, 3 };
        assertThrows(IllegalArgumentException.class, () -> Arrays.fff.reshape(arr, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> Arrays.fff.reshape(arr, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> Arrays.fff.reshape(arr, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> Arrays.fff.reshape(arr, 1, -1));
    }

    @Test
    public void testFFF_flatten_valid() {
        Integer[][][] arr = { { { 1, 2 } }, { { 3 } }, { { 4, 5, 6 } } };
        Integer[] result = Arrays.fff.flatten(arr);
        assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, result);
    }

    @Test
    public void testFFF_flatten_withNullSubArrays() {
        Integer[][][] arr = { { { 1, 2 }, null }, null, { { 3 } } };
        Integer[] result = Arrays.fff.flatten(arr);
        assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
    }

    @Test
    public void testFFF_flatten_nullArray() {
        Integer[][][] arr = null;
        assertThrows(IllegalArgumentException.class, () -> Arrays.fff.flatten(arr));
    }

    @Test
    public void testFFF_flatOp_null() {
        String[][][] arr = null;
        Arrays.fff.flatOp(arr, flat -> {
            throw new RuntimeException("Should not be called");
        });
    }

    @Test
    public void testFFF_flatOp_valid() {
        Integer[][][] arr = { { { 5, 2 } }, { { 9, 1 } }, { { 3, 7 } } };
        Arrays.fff.flatOp(arr, flat -> java.util.Arrays.sort(flat));
        assertArrayEquals(new Integer[] { 1, 2 }, arr[0][0]);
        assertArrayEquals(new Integer[] { 3, 5 }, arr[1][0]);
        assertArrayEquals(new Integer[] { 7, 9 }, arr[2][0]);
    }

    @Test
    public void testFFF_map_unaryOperator() {
        String[][][] arr = { { { "a", "b" } }, { { "c", "d" } } };
        String[][][] result = Arrays.fff.map(arr, String::toUpperCase);
        assertEquals("A", result[0][0][0]);
        assertEquals("B", result[0][0][1]);
        assertEquals("C", result[1][0][0]);
        assertEquals("D", result[1][0][1]);
    }

    @Test
    public void testFFF_map_function() {
        String[][][] arr = { { { "1", "2" } }, { { "3", "4" } } };
        Integer[][][] result = Arrays.fff.map(arr, Integer::valueOf, Integer.class);
        assertEquals(1, result[0][0][0]);
        assertEquals(2, result[0][0][1]);
        assertEquals(3, result[1][0][0]);
        assertEquals(4, result[1][0][1]);
    }

    @Test
    public void testFFF_mapToBoolean() {
        String[][][] arr = { { { "hello", "hi" } }, { { "world", "go" } } };
        boolean[][][] result = Arrays.fff.mapToBoolean(arr, s -> s.length() > 3);
        assertArrayEquals(new boolean[] { true, false }, result[0][0]);
        assertArrayEquals(new boolean[] { true, false }, result[1][0]);
    }

    @Test
    public void testFFF_mapToChar() {
        String[][][] arr = { { { "apple", "banana" } } };
        char[][][] result = Arrays.fff.mapToChar(arr, s -> s.charAt(0));
        assertArrayEquals(new char[] { 'a', 'b' }, result[0][0]);
    }

    @Test
    public void testFFF_mapToByte() {
        String[][][] arr = { { { "a", "ab" } } };
        byte[][][] result = Arrays.fff.mapToByte(arr, s -> (byte) s.length());
        assertArrayEquals(new byte[] { 1, 2 }, result[0][0]);
    }

    @Test
    public void testFFF_mapToShort() {
        String[][][] arr = { { { "x", "xy" } } };
        short[][][] result = Arrays.fff.mapToShort(arr, s -> (short) s.length());
        assertArrayEquals(new short[] { 1, 2 }, result[0][0]);
    }

    @Test
    public void testFFF_mapToInt() {
        String[][][] arr = { { { "a", "ab", "abc" } } };
        int[][][] result = Arrays.fff.mapToInt(arr, String::length);
        assertArrayEquals(new int[] { 1, 2, 3 }, result[0][0]);
    }

    @Test
    public void testFFF_mapToLong() {
        String[][][] arr = { { { "abc", "defg" } } };
        long[][][] result = Arrays.fff.mapToLong(arr, s -> (long) s.length());
        assertArrayEquals(new long[] { 3L, 4L }, result[0][0]);
    }

    @Test
    public void testFFF_mapToFloat() {
        String[][][] arr = { { { "ab", "abc" } } };
        float[][][] result = Arrays.fff.mapToFloat(arr, s -> (float) s.length());
        assertArrayEquals(new float[] { 2.0f, 3.0f }, result[0][0], 0.001f);
    }

    @Test
    public void testFFF_mapToDouble() {
        String[][][] arr = { { { "x", "xyz" } } };
        double[][][] result = Arrays.fff.mapToDouble(arr, s -> (double) s.length());
        assertArrayEquals(new double[] { 1.0, 3.0 }, result[0][0], 0.001);
    }

    @Test
    public void testFFF_zip_twoArrays() {
        Integer[][][] a = { { { 1, 2 } } };
        Integer[][][] b = { { { 10, 20 } } };
        Integer[][][] result = Arrays.fff.zip(a, b, (x, y) -> x + y);
        assertArrayEquals(new Integer[] { 11, 22 }, result[0][0]);
    }

    @Test
    public void testFFF_zip_twoArrays_differentTypes() {
        Integer[][][] a = { { { 1, 2 } } };
        String[][][] b = { { { "A", "B" } } };
        String[][][] result = Arrays.fff.zip(a, b, (x, y) -> x + y, String.class);
        assertArrayEquals(new String[] { "1A", "2B" }, result[0][0]);
    }

    @Test
    public void testFFF_zip_twoArrays_withDefaults() {
        Integer[][][] a = { { { 1, 2 } } };
        Integer[][][] b = { { { 10 } } };
        Integer[][][] result = Arrays.fff.zip(a, b, 0, 0, (x, y) -> x + y);
        assertArrayEquals(new Integer[] { 11, 2 }, result[0][0]);
    }

    @Test
    public void testFFF_zip_threeArrays() {
        Integer[][][] a = { { { 1, 2 } } };
        Integer[][][] b = { { { 10, 20 } } };
        Integer[][][] c = { { { 100, 200 } } };
        Integer[][][] result = Arrays.fff.zip(a, b, c, (x, y, z) -> x + y + z);
        assertArrayEquals(new Integer[] { 111, 222 }, result[0][0]);
    }

    @Test
    public void testFFF_totalCountOfElements() {
        Object[][][] arr = { { { "a", "b" }, { "c" } }, { { "d", "e", "f" } } };
        long count = Arrays.fff.totalCountOfElements(arr);
        assertEquals(6L, count);
    }

    // ============ Edge Cases - Empty Arrays ============

    @Test
    public void testMapToObj_emptyArrays() {
        boolean[] empty = new boolean[0];
        String[] result = Arrays.mapToObj(empty, b -> "X", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testUpdateAll_emptyArray() {
        boolean[] empty = new boolean[0];
        Arrays.updateAll(empty, b -> !b);
        assertEquals(0, empty.length);
    }

    @Test
    public void testReplaceIf_emptyArray() {
        boolean[] empty = new boolean[0];
        Arrays.replaceIf(empty, b -> true, false);
        assertEquals(0, empty.length);
    }

    @Test
    public void testFF_reshape_emptyArray() {
        Integer[] empty = new Integer[0];
        Integer[][] result = Arrays.ff.reshape(empty, 1);
        assertEquals(0, result.length);
    }

    @Test
    public void testFFF_reshape_emptyArray() {
        Integer[] empty = new Integer[0];
        Integer[][][] result = Arrays.fff.reshape(empty, 1, 1);
        assertEquals(0, result.length);
    }

    // ============ Edge Cases - Single Element ============

    @Test
    public void testReshape_singleElement_2D() {
        boolean[] arr = { true };
        boolean[][] result = Arrays.reshape(arr, 1);
        assertEquals(1, result.length);
        assertArrayEquals(new boolean[] { true }, result[0]);
    }

    @Test
    public void testReshape_singleElement_3D() {
        boolean[] arr = { false };
        boolean[][][] result = Arrays.reshape(arr, 1, 1);
        assertEquals(1, result.length);
        assertEquals(1, result[0].length);
        assertArrayEquals(new boolean[] { false }, result[0][0]);
    }

    @Test
    public void testFlatten_singleElement_2D() {
        boolean[][] arr = { { true } };
        boolean[] result = Arrays.flatten(arr);
        assertArrayEquals(new boolean[] { true }, result);
    }

    @Test
    public void testFlatten_singleElement_3D() {
        boolean[][][] arr = { { { false } } };
        boolean[] result = Arrays.flatten(arr);
        assertArrayEquals(new boolean[] { false }, result);
    }

    // ============ Additional mapToInt/Long/Double Tests ============

    @Test
    public void testMapToInt_long_1D() {
        long[] arr = { 1L, 2L, 3L };
        int[] result = Arrays.mapToInt(arr, l -> (int) (l * 2));
        assertArrayEquals(new int[] { 2, 4, 6 }, result);
    }

    @Test
    public void testMapToInt_double_1D() {
        double[] arr = { 1.5, 2.7, 3.9 };
        int[] result = Arrays.mapToInt(arr, d -> (int) Math.round(d));
        assertArrayEquals(new int[] { 2, 3, 4 }, result);
    }

    @Test
    public void testMapToLong_double_1D() {
        double[] arr = { 1.5, 2.7, 3.9 };
        long[] result = Arrays.mapToLong(arr, d -> (long) Math.round(d));
        assertArrayEquals(new long[] { 2L, 3L, 4L }, result);
    }

    @Test
    public void testMapToDouble_long_1D() {
        long[] arr = { 1L, 2L, 3L };
        double[] result = Arrays.mapToDouble(arr, l -> l * 1.5);
        assertArrayEquals(new double[] { 1.5, 3.0, 4.5 }, result, 0.001);
    }
}
