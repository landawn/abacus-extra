package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

/**
 * Comprehensive unit tests for the Arrays utility class (2510 test suite).
 * This test class provides extensive coverage of array manipulation methods including:
 * - Printing operations for various dimensions
 * - Mapping functions (mapToObj, mapToInt, mapToLong, mapToDouble, etc.)
 * - Array transformations (reshape, flatten, zip)
 * - In-place operations (updateAll, replaceIf)
 * - Type conversions (toBoolean, toByte, toChar, etc.)
 * - Statistical operations (totalCountOfElements, minSubArrayLen, maxSubArrayLen)
 */
@Tag("2510")
public class Arrays2510Test extends TestBase {

    // ============================================
    // Tests for println methods - Object arrays
    // ============================================

    @Test
    public void testPrintln_1D_ObjectArray_normal() {
        Object[] arr = { "Hello", "World", 123, 45.6 };
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("World"));
        assertTrue(result.contains("123"));
    }

    @Test
    public void testPrintln_1D_ObjectArray_null() {
        Object[] arr = null;
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("null"));
    }

    @Test
    public void testPrintln_1D_ObjectArray_empty() {
        Object[] arr = {};
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("[]"));
    }

    @Test
    public void testPrintln_2D_ObjectArray_normal() {
        Object[][] arr = { { "A", "B" }, { "C", "D" }, { "E", "F" } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_ObjectArray_null() {
        Object[][] arr = null;
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("null"));
    }

    @Test
    public void testPrintln_2D_ObjectArray_withNullSubArray() {
        Object[][] arr = { { "A", "B" }, null, { "C" } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_ObjectArray_normal() {
        Object[][][] arr = { { { "A", "B" }, { "C" } }, { { "D" } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_ObjectArray_empty() {
        Object[][][] arr = {};
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println methods - boolean arrays
    // ============================================

    @Test
    public void testPrintln_1D_BooleanArray_normal() {
        boolean[] arr = { true, false, true, false };
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("true"));
        assertTrue(result.contains("false"));
    }

    @Test
    public void testPrintln_1D_BooleanArray_null() {
        boolean[] arr = null;
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_BooleanArray_normal() {
        boolean[][] arr = { { true, false }, { false, true } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_BooleanArray_normal() {
        boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println methods - char arrays
    // ============================================

    @Test
    public void testPrintln_1D_CharArray_normal() {
        char[] arr = { 'a', 'b', 'c', 'd' };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_CharArray_normal() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_CharArray_normal() {
        char[][][] arr = { { { 'a', 'b' } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println methods - byte arrays
    // ============================================

    @Test
    public void testPrintln_1D_ByteArray_normal() {
        byte[] arr = { 1, 2, 3, 4 };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_ByteArray_normal() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_ByteArray_normal() {
        byte[][][] arr = { { { 1, 2 } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println methods - short arrays
    // ============================================

    @Test
    public void testPrintln_1D_ShortArray_normal() {
        short[] arr = { 10, 20, 30 };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_ShortArray_normal() {
        short[][] arr = { { 10, 20 }, { 30, 40 } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_ShortArray_normal() {
        short[][][] arr = { { { 10, 20 } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println methods - int arrays
    // ============================================

    @Test
    public void testPrintln_1D_IntArray_normal() {
        int[] arr = { 1, 2, 3, 4, 5 };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_IntArray_normal() {
        int[][] arr = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_IntArray_normal() {
        int[][][] arr = { { { 1, 2 }, { 3 } }, { { 4 } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println methods - long arrays
    // ============================================

    @Test
    public void testPrintln_1D_LongArray_normal() {
        long[] arr = { 100L, 200L, 300L };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_LongArray_normal() {
        long[][] arr = { { 100L, 200L }, { 300L, 400L } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_LongArray_normal() {
        long[][][] arr = { { { 100L, 200L } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println methods - float arrays
    // ============================================

    @Test
    public void testPrintln_1D_FloatArray_normal() {
        float[] arr = { 1.1f, 2.2f, 3.3f };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_FloatArray_normal() {
        float[][] arr = { { 1.1f, 2.2f }, { 3.3f, 4.4f } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_FloatArray_normal() {
        float[][][] arr = { { { 1.1f, 2.2f } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for println methods - double arrays
    // ============================================

    @Test
    public void testPrintln_1D_DoubleArray_normal() {
        double[] arr = { 1.5, 2.5, 3.5 };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_DoubleArray_normal() {
        double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_DoubleArray_normal() {
        double[][][] arr = { { { 1.5, 2.5 } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for mapToObj methods - boolean
    // ============================================

    @Test
    public void testMapToObj_1D_Boolean_normal() {
        boolean[] arr = { true, false, true };
        String[] result = Arrays.mapToObj(arr, b -> b ? "YES" : "NO", String.class);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("YES", result[0]);
        assertEquals("NO", result[1]);
        assertEquals("YES", result[2]);
    }

    @Test
    public void testMapToObj_1D_Boolean_null() {
        boolean[] arr = null;
        String[] result = Arrays.mapToObj(arr, b -> b ? "YES" : "NO", String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_1D_Boolean_empty() {
        boolean[] arr = {};
        String[] result = Arrays.mapToObj(arr, b -> b ? "YES" : "NO", String.class);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_2D_Boolean_normal() {
        boolean[][] arr = { { true, false }, { false, true } };
        String[][] result = Arrays.mapToObj(arr, b -> b ? "1" : "0", String.class);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("1", result[0][0]);
        assertEquals("0", result[0][1]);
    }

    @Test
    public void testMapToObj_3D_Boolean_normal() {
        boolean[][][] arr = { { { true, false } } };
        Integer[][][] result = Arrays.mapToObj(arr, b -> b ? 1 : 0, Integer.class);
        assertNotNull(result);
        assertEquals(1, result[0][0][0]);
        assertEquals(0, result[0][0][1]);
    }

    // ============================================
    // Tests for mapToObj methods - char
    // ============================================

    @Test
    public void testMapToObj_1D_Char_normal() {
        char[] arr = { 'a', 'b', 'c' };
        String[] result = Arrays.mapToObj(arr, c -> String.valueOf(c).toUpperCase(), String.class);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("A", result[0]);
        assertEquals("B", result[1]);
        assertEquals("C", result[2]);
    }

    @Test
    public void testMapToObj_1D_Char_null() {
        char[] arr = null;
        String[] result = Arrays.mapToObj(arr, c -> String.valueOf(c), String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_2D_Char_normal() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        Integer[][] result = Arrays.mapToObj(arr, c -> (int) c, Integer.class);
        assertNotNull(result);
        assertEquals(97, result[0][0]);
        assertEquals(98, result[0][1]);
    }

    @Test
    public void testMapToObj_3D_Char_normal() {
        char[][][] arr = { { { 'x', 'y' } } };
        String[][][] result = Arrays.mapToObj(arr, c -> c + "!", String.class);
        assertNotNull(result);
        assertEquals("x!", result[0][0][0]);
    }

    // ============================================
    // Tests for mapToObj methods - byte
    // ============================================

    @Test
    public void testMapToObj_1D_Byte_normal() {
        byte[] arr = { 1, 2, 3, 4 };
        String[] result = Arrays.mapToObj(arr, b -> String.format("%02X", b), String.class);
        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals("01", result[0]);
        assertEquals("02", result[1]);
    }

    @Test
    public void testMapToObj_1D_Byte_null() {
        byte[] arr = null;
        String[] result = Arrays.mapToObj(arr, b -> String.valueOf(b), String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_2D_Byte_normal() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        Integer[][] result = Arrays.mapToObj(arr, b -> b * 10, Integer.class);
        assertNotNull(result);
        assertEquals(10, result[0][0]);
        assertEquals(20, result[0][1]);
    }

    @Test
    public void testMapToObj_3D_Byte_normal() {
        byte[][][] arr = { { { 5, 6 } } };
        String[][][] result = Arrays.mapToObj(arr, b -> "byte:" + b, String.class);
        assertNotNull(result);
        assertEquals("byte:5", result[0][0][0]);
    }

    // ============================================
    // Tests for mapToObj methods - short
    // ============================================

    @Test
    public void testMapToObj_1D_Short_normal() {
        short[] arr = { 10, 20, 30 };
        String[] result = Arrays.mapToObj(arr, s -> "S" + s, String.class);
        assertNotNull(result);
        assertEquals("S10", result[0]);
        assertEquals("S20", result[1]);
    }

    @Test
    public void testMapToObj_2D_Short_normal() {
        short[][] arr = { { 100, 200 }, { 300, 400 } };
        Integer[][] result = Arrays.mapToObj(arr, s -> s / 10, Integer.class);
        assertNotNull(result);
        assertEquals(10, result[0][0]);
    }

    @Test
    public void testMapToObj_3D_Short_normal() {
        short[][][] arr = { { { 50, 60 } } };
        Long[][][] result = Arrays.mapToObj(arr, s -> (long) s * 2, Long.class);
        assertNotNull(result);
        assertEquals(100L, result[0][0][0]);
    }

    // ============================================
    // Tests for mapToObj methods - int
    // ============================================

    @Test
    public void testMapToObj_1D_Int_normal() {
        int[] arr = { 1, 2, 3, 4, 5 };
        String[] result = Arrays.mapToObj(arr, i -> "num:" + i, String.class);
        assertNotNull(result);
        assertEquals(5, result.length);
        assertEquals("num:1", result[0]);
        assertEquals("num:5", result[4]);
    }

    @Test
    public void testMapToObj_1D_Int_null() {
        int[] arr = null;
        String[] result = Arrays.mapToObj(arr, i -> String.valueOf(i), String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_2D_Int_normal() {
        int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        String[][] result = Arrays.mapToObj(arr, i -> Integer.toHexString(i), String.class);
        assertNotNull(result);
        assertEquals("1", result[0][0]);
        assertEquals("6", result[1][2]);
    }

    @Test
    public void testMapToObj_3D_Int_normal() {
        int[][][] arr = { { { 10, 20 }, { 30 } } };
        Double[][][] result = Arrays.mapToObj(arr, i -> i * 0.5, Double.class);
        assertNotNull(result);
        assertEquals(5.0, result[0][0][0]);
    }

    // ============================================
    // Tests for mapToObj methods - long
    // ============================================

    @Test
    public void testMapToObj_1D_Long_normal() {
        long[] arr = { 100L, 200L, 300L };
        String[] result = Arrays.mapToObj(arr, l -> "L" + l, String.class);
        assertNotNull(result);
        assertEquals("L100", result[0]);
        assertEquals("L300", result[2]);
    }

    @Test
    public void testMapToObj_1D_Long_null() {
        long[] arr = null;
        String[] result = Arrays.mapToObj(arr, l -> String.valueOf(l), String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_2D_Long_normal() {
        long[][] arr = { { 1000L, 2000L }, { 3000L, 4000L } };
        Integer[][] result = Arrays.mapToObj(arr, l -> (int) (l / 1000), Integer.class);
        assertNotNull(result);
        assertEquals(1, result[0][0]);
        assertEquals(4, result[1][1]);
    }

    @Test
    public void testMapToObj_3D_Long_normal() {
        long[][][] arr = { { { 500L, 600L } } };
        String[][][] result = Arrays.mapToObj(arr, l -> l + "ms", String.class);
        assertNotNull(result);
        assertEquals("500ms", result[0][0][0]);
    }

    // ============================================
    // Tests for mapToObj methods - float
    // ============================================

    @Test
    public void testMapToObj_1D_Float_normal() {
        float[] arr = { 1.1f, 2.2f, 3.3f };
        String[] result = Arrays.mapToObj(arr, f -> String.format("%.1f", f), String.class);
        assertNotNull(result);
        assertEquals("1.1", result[0]);
        assertEquals("3.3", result[2]);
    }

    @Test
    public void testMapToObj_1D_Float_null() {
        float[] arr = null;
        String[] result = Arrays.mapToObj(arr, f -> String.valueOf(f), String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_2D_Float_normal() {
        float[][] arr = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
        Integer[][] result = Arrays.mapToObj(arr, f -> Math.round(f), Integer.class);
        assertNotNull(result);
        assertEquals(2, result[0][0]);
        assertEquals(5, result[1][1]);
    }

    @Test
    public void testMapToObj_3D_Float_normal() {
        float[][][] arr = { { { 0.5f, 0.75f } } };
        Double[][][] result = Arrays.mapToObj(arr, f -> (double) f * 2, Double.class);
        assertNotNull(result);
        assertEquals(1.0, result[0][0][0], 0.01);
    }

    // ============================================
    // Tests for mapToObj methods - double
    // ============================================

    @Test
    public void testMapToObj_1D_Double_normal() {
        double[] arr = { 1.5, 2.5, 3.5 };
        String[] result = Arrays.mapToObj(arr, d -> String.format("%.2f", d), String.class);
        assertNotNull(result);
        assertEquals("1.50", result[0]);
        assertEquals("3.50", result[2]);
    }

    @Test
    public void testMapToObj_1D_Double_null() {
        double[] arr = null;
        String[] result = Arrays.mapToObj(arr, d -> String.valueOf(d), String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_2D_Double_normal() {
        double[][] arr = { { 10.5, 20.5 }, { 30.5, 40.5 } };
        Long[][] result = Arrays.mapToObj(arr, d -> Math.round(d), Long.class);
        assertNotNull(result);
        assertEquals(11L, result[0][0]);
        assertEquals(41L, result[1][1]);
    }

    @Test
    public void testMapToObj_3D_Double_normal() {
        double[][][] arr = { { { 100.25, 200.75 } } };
        String[][][] result = Arrays.mapToObj(arr, d -> "$" + d, String.class);
        assertNotNull(result);
        assertEquals("$100.25", result[0][0][0]);
    }

    // ============================================
    // Tests for mapToLong methods
    // ============================================

    @Test
    public void testMapToLong_1D_Int_normal() {
        int[] arr = { 1, 2, 3, 4, 5 };
        long[] result = Arrays.mapToLong(arr, i -> (long) i * 100);
        assertNotNull(result);
        assertEquals(5, result.length);
        assertEquals(100L, result[0]);
        assertEquals(500L, result[4]);
    }

    @Test
    public void testMapToLong_1D_Int_null() {
        int[] arr = null;
        long[] result = Arrays.mapToLong(arr, i -> (long) i);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToLong_2D_Int_normal() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        long[][] result = Arrays.mapToLong(arr, i -> (long) i * 1000);
        assertNotNull(result);
        assertEquals(1000L, result[0][0]);
        assertEquals(4000L, result[1][1]);
    }

    @Test
    public void testMapToLong_3D_Int_normal() {
        int[][][] arr = { { { 10, 20 } } };
        long[][][] result = Arrays.mapToLong(arr, i -> (long) i + 5);
        assertNotNull(result);
        assertEquals(15L, result[0][0][0]);
    }

    // ============================================
    // Tests for mapToDouble methods - int
    // ============================================

    @Test
    public void testMapToDouble_1D_Int_normal() {
        int[] arr = { 1, 2, 3, 4 };
        double[] result = Arrays.mapToDouble(arr, i -> i * 0.5);
        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals(0.5, result[0], 0.001);
        assertEquals(2.0, result[3], 0.001);
    }

    @Test
    public void testMapToDouble_1D_Int_null() {
        int[] arr = null;
        double[] result = Arrays.mapToDouble(arr, i -> (double) i);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToDouble_2D_Int_normal() {
        int[][] arr = { { 10, 20 }, { 30, 40 } };
        double[][] result = Arrays.mapToDouble(arr, i -> i / 10.0);
        assertNotNull(result);
        assertEquals(1.0, result[0][0], 0.001);
        assertEquals(4.0, result[1][1], 0.001);
    }

    @Test
    public void testMapToDouble_3D_Int_normal() {
        int[][][] arr = { { { 100, 200 } } };
        double[][][] result = Arrays.mapToDouble(arr, i -> i * 1.5);
        assertNotNull(result);
        assertEquals(150.0, result[0][0][0], 0.001);
    }

    // ============================================
    // Tests for mapToInt methods - long
    // ============================================

    @Test
    public void testMapToInt_1D_Long_normal() {
        long[] arr = { 100L, 200L, 300L };
        int[] result = Arrays.mapToInt(arr, l -> (int) (l / 10));
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(10, result[0]);
        assertEquals(30, result[2]);
    }

    @Test
    public void testMapToInt_1D_Long_null() {
        long[] arr = null;
        int[] result = Arrays.mapToInt(arr, l -> (int) l);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToInt_2D_Long_normal() {
        long[][] arr = { { 1000L, 2000L }, { 3000L, 4000L } };
        int[][] result = Arrays.mapToInt(arr, l -> (int) (l / 100));
        assertNotNull(result);
        assertEquals(10, result[0][0]);
        assertEquals(40, result[1][1]);
    }

    @Test
    public void testMapToInt_3D_Long_normal() {
        long[][][] arr = { { { 500L, 600L } } };
        int[][][] result = Arrays.mapToInt(arr, l -> (int) l - 100);
        assertNotNull(result);
        assertEquals(400, result[0][0][0]);
    }

    // ============================================
    // Tests for mapToDouble methods - long
    // ============================================

    @Test
    public void testMapToDouble_1D_Long_normal() {
        long[] arr = { 1000L, 2000L, 3000L };
        double[] result = Arrays.mapToDouble(arr, l -> l / 100.0);
        assertNotNull(result);
        assertEquals(10.0, result[0], 0.001);
        assertEquals(30.0, result[2], 0.001);
    }

    @Test
    public void testMapToDouble_2D_Long_normal() {
        long[][] arr = { { 100L, 200L }, { 300L, 400L } };
        double[][] result = Arrays.mapToDouble(arr, l -> l * 1.5);
        assertNotNull(result);
        assertEquals(150.0, result[0][0], 0.001);
    }

    @Test
    public void testMapToDouble_3D_Long_normal() {
        long[][][] arr = { { { 50L, 75L } } };
        double[][][] result = Arrays.mapToDouble(arr, l -> l + 0.5);
        assertNotNull(result);
        assertEquals(50.5, result[0][0][0], 0.001);
    }

    // ============================================
    // Tests for mapToInt methods - double
    // ============================================

    @Test
    public void testMapToInt_1D_Double_normal() {
        double[] arr = { 1.5, 2.7, 3.2, 4.9 };
        int[] result = Arrays.mapToInt(arr, d -> (int) Math.round(d));
        assertNotNull(result);
        assertEquals(2, result[0]);
        assertEquals(5, result[3]);
    }

    @Test
    public void testMapToInt_1D_Double_null() {
        double[] arr = null;
        int[] result = Arrays.mapToInt(arr, d -> (int) d);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToInt_2D_Double_normal() {
        double[][] arr = { { 10.5, 20.5 }, { 30.5, 40.5 } };
        int[][] result = Arrays.mapToInt(arr, d -> (int) d);
        assertNotNull(result);
        assertEquals(10, result[0][0]);
        assertEquals(40, result[1][1]);
    }

    @Test
    public void testMapToInt_3D_Double_normal() {
        double[][][] arr = { { { 100.8, 200.2 } } };
        int[][][] result = Arrays.mapToInt(arr, d -> (int) Math.ceil(d));
        assertNotNull(result);
        assertEquals(101, result[0][0][0]);
    }

    // ============================================
    // Tests for mapToLong methods - double
    // ============================================

    @Test
    public void testMapToLong_1D_Double_normal() {
        double[] arr = { 1.5, 2.7, 3.9 };
        long[] result = Arrays.mapToLong(arr, d -> Math.round(d * 100));
        assertNotNull(result);
        assertEquals(150L, result[0]);
        assertEquals(390L, result[2]);
    }

    @Test
    public void testMapToLong_1D_Double_null() {
        double[] arr = null;
        long[] result = Arrays.mapToLong(arr, d -> (long) d);
        assertEquals(0, result.length);
    }

    @Test
    public void testMapToLong_2D_Double_normal() {
        double[][] arr = { { 10.5, 20.5 }, { 30.5, 40.5 } };
        long[][] result = Arrays.mapToLong(arr, d -> Math.round(d));
        assertNotNull(result);
        assertEquals(11L, result[0][0]);
        assertEquals(41L, result[1][1]);
    }

    @Test
    public void testMapToLong_3D_Double_normal() {
        double[][][] arr = { { { 99.9, 199.9 } } };
        long[][][] result = Arrays.mapToLong(arr, d -> (long) Math.floor(d));
        assertNotNull(result);
        assertEquals(99L, result[0][0][0]);
    }

    // ============================================
    // Tests for updateAll methods - boolean
    // ============================================

    @Test
    public void testUpdateAll_1D_Boolean_normal() {
        boolean[] arr = { true, false, true, false };
        Arrays.updateAll(arr, b -> !b);
        assertFalse(arr[0]);
        assertTrue(arr[1]);
        assertFalse(arr[2]);
        assertTrue(arr[3]);
    }

    @Test
    public void testUpdateAll_1D_Boolean_null() {
        boolean[] arr = null;
        Arrays.updateAll(arr, b -> !b);   // Should not throw exception
    }

    @Test
    public void testUpdateAll_2D_Boolean_normal() {
        boolean[][] arr = { { true, false }, { false, true } };
        Arrays.updateAll(arr, b -> !b);
        assertFalse(arr[0][0]);
        assertTrue(arr[0][1]);
        assertTrue(arr[1][0]);
        assertFalse(arr[1][1]);
    }

    @Test
    public void testUpdateAll_3D_Boolean_normal() {
        boolean[][][] arr = { { { true, false } } };
        Arrays.updateAll(arr, b -> true);
        assertTrue(arr[0][0][0]);
        assertTrue(arr[0][0][1]);
    }

    // ============================================
    // Tests for updateAll methods - char
    // ============================================

    @Test
    public void testUpdateAll_1D_Char_normal() {
        char[] arr = { 'a', 'b', 'c' };
        Arrays.updateAll(arr, c -> Character.toUpperCase(c));
        assertEquals('A', arr[0]);
        assertEquals('B', arr[1]);
        assertEquals('C', arr[2]);
    }

    @Test
    public void testUpdateAll_2D_Char_normal() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        Arrays.updateAll(arr, c -> (char) (c + 1));
        assertEquals('b', arr[0][0]);
        assertEquals('e', arr[1][1]);
    }

    @Test
    public void testUpdateAll_3D_Char_normal() {
        char[][][] arr = { { { 'x', 'y' } } };
        Arrays.updateAll(arr, c -> 'z');
        assertEquals('z', arr[0][0][0]);
        assertEquals('z', arr[0][0][1]);
    }

    // ============================================
    // Tests for updateAll methods - byte
    // ============================================

    @Test
    public void testUpdateAll_1D_Byte_normal() {
        byte[] arr = { 1, 2, 3, 4 };
        Arrays.updateAll(arr, b -> (byte) (b * 2));
        assertEquals(2, arr[0]);
        assertEquals(4, arr[1]);
        assertEquals(8, arr[3]);
    }

    @Test
    public void testUpdateAll_2D_Byte_normal() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        Arrays.updateAll(arr, b -> (byte) (b + 10));
        assertEquals(11, arr[0][0]);
        assertEquals(14, arr[1][1]);
    }

    @Test
    public void testUpdateAll_3D_Byte_normal() {
        byte[][][] arr = { { { 5, 10 } } };
        Arrays.updateAll(arr, b -> (byte) (b / 5));
        assertEquals(1, arr[0][0][0]);
        assertEquals(2, arr[0][0][1]);
    }

    // ============================================
    // Tests for updateAll methods - short
    // ============================================

    @Test
    public void testUpdateAll_1D_Short_normal() {
        short[] arr = { 10, 20, 30 };
        Arrays.updateAll(arr, s -> (short) (s * 2));
        assertEquals(20, arr[0]);
        assertEquals(40, arr[1]);
        assertEquals(60, arr[2]);
    }

    @Test
    public void testUpdateAll_2D_Short_normal() {
        short[][] arr = { { 100, 200 }, { 300, 400 } };
        Arrays.updateAll(arr, s -> (short) (s / 10));
        assertEquals(10, arr[0][0]);
        assertEquals(40, arr[1][1]);
    }

    @Test
    public void testUpdateAll_3D_Short_normal() {
        short[][][] arr = { { { 50, 75 } } };
        Arrays.updateAll(arr, s -> (short) (s + 5));
        assertEquals(55, arr[0][0][0]);
        assertEquals(80, arr[0][0][1]);
    }

    // ============================================
    // Tests for updateAll methods - int
    // ============================================

    @Test
    public void testUpdateAll_1D_Int_normal() {
        int[] arr = { 1, 2, 3, 4, 5 };
        Arrays.updateAll(arr, i -> i * 10);
        assertEquals(10, arr[0]);
        assertEquals(50, arr[4]);
    }

    @Test
    public void testUpdateAll_1D_Int_null() {
        int[] arr = null;
        Arrays.updateAll(arr, i -> i * 2);   // Should not throw exception
    }

    @Test
    public void testUpdateAll_2D_Int_normal() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        Arrays.updateAll(arr, i -> i + 100);
        assertEquals(101, arr[0][0]);
        assertEquals(104, arr[1][1]);
    }

    @Test
    public void testUpdateAll_3D_Int_normal() {
        int[][][] arr = { { { 10, 20 }, { 30 } } };
        Arrays.updateAll(arr, i -> i / 10);
        assertEquals(1, arr[0][0][0]);
        assertEquals(3, arr[0][1][0]);
    }

    // ============================================
    // Tests for updateAll methods - long
    // ============================================

    @Test
    public void testUpdateAll_1D_Long_normal() {
        long[] arr = { 100L, 200L, 300L };
        Arrays.updateAll(arr, l -> l * 2);
        assertEquals(200L, arr[0]);
        assertEquals(600L, arr[2]);
    }

    @Test
    public void testUpdateAll_2D_Long_normal() {
        long[][] arr = { { 1000L, 2000L }, { 3000L, 4000L } };
        Arrays.updateAll(arr, l -> l / 1000);
        assertEquals(1L, arr[0][0]);
        assertEquals(4L, arr[1][1]);
    }

    @Test
    public void testUpdateAll_3D_Long_normal() {
        long[][][] arr = { { { 50L, 100L } } };
        Arrays.updateAll(arr, l -> l + 5L);
        assertEquals(55L, arr[0][0][0]);
        assertEquals(105L, arr[0][0][1]);
    }

    // ============================================
    // Tests for updateAll methods - float
    // ============================================

    @Test
    public void testUpdateAll_1D_Float_normal() {
        float[] arr = { 1.0f, 2.0f, 3.0f };
        Arrays.updateAll(arr, f -> f * 2.5f);
        assertEquals(2.5f, arr[0], 0.001f);
        assertEquals(7.5f, arr[2], 0.001f);
    }

    @Test
    public void testUpdateAll_2D_Float_normal() {
        float[][] arr = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
        Arrays.updateAll(arr, f -> f + 0.5f);
        assertEquals(2.0f, arr[0][0], 0.001f);
        assertEquals(5.0f, arr[1][1], 0.001f);
    }

    @Test
    public void testUpdateAll_3D_Float_normal() {
        float[][][] arr = { { { 10.0f, 20.0f } } };
        Arrays.updateAll(arr, f -> f / 10.0f);
        assertEquals(1.0f, arr[0][0][0], 0.001f);
        assertEquals(2.0f, arr[0][0][1], 0.001f);
    }

    // ============================================
    // Tests for updateAll methods - double
    // ============================================

    @Test
    public void testUpdateAll_1D_Double_normal() {
        double[] arr = { 1.5, 2.5, 3.5 };
        Arrays.updateAll(arr, d -> d * 2.0);
        assertEquals(3.0, arr[0], 0.001);
        assertEquals(7.0, arr[2], 0.001);
    }

    @Test
    public void testUpdateAll_1D_Double_null() {
        double[] arr = null;
        Arrays.updateAll(arr, d -> d * 2);   // Should not throw exception
    }

    @Test
    public void testUpdateAll_2D_Double_normal() {
        double[][] arr = { { 10.5, 20.5 }, { 30.5, 40.5 } };
        Arrays.updateAll(arr, d -> d - 0.5);
        assertEquals(10.0, arr[0][0], 0.001);
        assertEquals(40.0, arr[1][1], 0.001);
    }

    @Test
    public void testUpdateAll_3D_Double_normal() {
        double[][][] arr = { { { 100.0, 200.0 } } };
        Arrays.updateAll(arr, d -> d / 100.0);
        assertEquals(1.0, arr[0][0][0], 0.001);
        assertEquals(2.0, arr[0][0][1], 0.001);
    }

    // ============================================
    // Tests for replaceIf methods - boolean
    // ============================================

    @Test
    public void testReplaceIf_1D_Boolean_normal() {
        boolean[] arr = { true, false, true, false };
        Arrays.replaceIf(arr, b -> b, false);
        assertFalse(arr[0]);
        assertFalse(arr[1]);
        assertFalse(arr[2]);
        assertFalse(arr[3]);
    }

    @Test
    public void testReplaceIf_1D_Boolean_null() {
        boolean[] arr = null;
        Arrays.replaceIf(arr, b -> b, false);   // Should not throw exception
    }

    @Test
    public void testReplaceIf_2D_Boolean_normal() {
        boolean[][] arr = { { true, false }, { false, true } };
        Arrays.replaceIf(arr, b -> !b, true);
        assertTrue(arr[0][0]);
        assertTrue(arr[0][1]);
        assertTrue(arr[1][0]);
        assertTrue(arr[1][1]);
    }

    @Test
    public void testReplaceIf_3D_Boolean_normal() {
        boolean[][][] arr = { { { true, false } } };
        Arrays.replaceIf(arr, b -> b, false);
        assertFalse(arr[0][0][0]);
        assertFalse(arr[0][0][1]);
    }

    // ============================================
    // Tests for replaceIf methods - char
    // ============================================

    @Test
    public void testReplaceIf_1D_Char_normal() {
        char[] arr = { 'a', 'b', 'c', 'd' };
        Arrays.replaceIf(arr, c -> c < 'c', 'x');
        assertEquals('x', arr[0]);
        assertEquals('x', arr[1]);
        assertEquals('c', arr[2]);
        assertEquals('d', arr[3]);
    }

    @Test
    public void testReplaceIf_2D_Char_normal() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        Arrays.replaceIf(arr, c -> c == 'b' || c == 'd', 'z');
        assertEquals('a', arr[0][0]);
        assertEquals('z', arr[0][1]);
        assertEquals('c', arr[1][0]);
        assertEquals('z', arr[1][1]);
    }

    @Test
    public void testReplaceIf_3D_Char_normal() {
        char[][][] arr = { { { 'p', 'q' } } };
        Arrays.replaceIf(arr, c -> c == 'p', 'r');
        assertEquals('r', arr[0][0][0]);
        assertEquals('q', arr[0][0][1]);
    }

    // ============================================
    // Tests for replaceIf methods - byte
    // ============================================

    @Test
    public void testReplaceIf_1D_Byte_normal() {
        byte[] arr = { 1, 2, 3, 4, 5 };
        Arrays.replaceIf(arr, b -> b % 2 == 0, (byte) 0);
        assertEquals(1, arr[0]);
        assertEquals(0, arr[1]);
        assertEquals(3, arr[2]);
        assertEquals(0, arr[3]);
        assertEquals(5, arr[4]);
    }

    @Test
    public void testReplaceIf_2D_Byte_normal() {
        byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        Arrays.replaceIf(arr, b -> b > 3, (byte) 99);
        assertEquals(1, arr[0][0]);
        assertEquals(2, arr[0][1]);
        assertEquals(99, arr[1][0]);
    }

    @Test
    public void testReplaceIf_3D_Byte_normal() {
        byte[][][] arr = { { { 10, 20, 30 } } };
        Arrays.replaceIf(arr, b -> b >= 20, (byte) 0);
        assertEquals(10, arr[0][0][0]);
        assertEquals(0, arr[0][0][1]);
        assertEquals(0, arr[0][0][2]);
    }

    // ============================================
    // Tests for replaceIf methods - short
    // ============================================

    @Test
    public void testReplaceIf_1D_Short_normal() {
        short[] arr = { 10, 20, 30, 40 };
        Arrays.replaceIf(arr, s -> s > 20, (short) 0);
        assertEquals(10, arr[0]);
        assertEquals(20, arr[1]);
        assertEquals(0, arr[2]);
        assertEquals(0, arr[3]);
    }

    @Test
    public void testReplaceIf_2D_Short_normal() {
        short[][] arr = { { 100, 200 }, { 300, 400 } };
        Arrays.replaceIf(arr, s -> s >= 300, (short) 999);
        assertEquals(100, arr[0][0]);
        assertEquals(200, arr[0][1]);
        assertEquals(999, arr[1][0]);
        assertEquals(999, arr[1][1]);
    }

    @Test
    public void testReplaceIf_3D_Short_normal() {
        short[][][] arr = { { { 50, 60, 70 } } };
        Arrays.replaceIf(arr, s -> s == 60, (short) 100);
        assertEquals(50, arr[0][0][0]);
        assertEquals(100, arr[0][0][1]);
        assertEquals(70, arr[0][0][2]);
    }

    // ============================================
    // Tests for replaceIf methods - int
    // ============================================

    @Test
    public void testReplaceIf_1D_Int_normal() {
        int[] arr = { 1, 2, 3, 4, 5 };
        Arrays.replaceIf(arr, i -> i > 3, 0);
        assertEquals(1, arr[0]);
        assertEquals(2, arr[1]);
        assertEquals(3, arr[2]);
        assertEquals(0, arr[3]);
        assertEquals(0, arr[4]);
    }

    @Test
    public void testReplaceIf_1D_Int_null() {
        int[] arr = null;
        Arrays.replaceIf(arr, i -> i > 0, 0);   // Should not throw exception
    }

    @Test
    public void testReplaceIf_2D_Int_normal() {
        int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        Arrays.replaceIf(arr, i -> i % 2 == 0, -1);
        assertEquals(1, arr[0][0]);
        assertEquals(-1, arr[0][1]);
        assertEquals(3, arr[0][2]);
        assertEquals(-1, arr[1][0]);
    }

    @Test
    public void testReplaceIf_3D_Int_normal() {
        int[][][] arr = { { { 10, 20, 30 } } };
        Arrays.replaceIf(arr, i -> i == 20, 200);
        assertEquals(10, arr[0][0][0]);
        assertEquals(200, arr[0][0][1]);
        assertEquals(30, arr[0][0][2]);
    }

    // ============================================
    // Tests for replaceIf methods - long
    // ============================================

    @Test
    public void testReplaceIf_1D_Long_normal() {
        long[] arr = { 100L, 200L, 300L, 400L };
        Arrays.replaceIf(arr, l -> l >= 300L, 0L);
        assertEquals(100L, arr[0]);
        assertEquals(200L, arr[1]);
        assertEquals(0L, arr[2]);
        assertEquals(0L, arr[3]);
    }

    @Test
    public void testReplaceIf_2D_Long_normal() {
        long[][] arr = { { 1000L, 2000L }, { 3000L, 4000L } };
        Arrays.replaceIf(arr, l -> l < 3000L, 999L);
        assertEquals(999L, arr[0][0]);
        assertEquals(999L, arr[0][1]);
        assertEquals(3000L, arr[1][0]);
        assertEquals(4000L, arr[1][1]);
    }

    @Test
    public void testReplaceIf_3D_Long_normal() {
        long[][][] arr = { { { 50L, 100L, 150L } } };
        Arrays.replaceIf(arr, l -> l == 100L, 999L);
        assertEquals(50L, arr[0][0][0]);
        assertEquals(999L, arr[0][0][1]);
        assertEquals(150L, arr[0][0][2]);
    }

    // ============================================
    // Tests for replaceIf methods - float
    // ============================================

    @Test
    public void testReplaceIf_1D_Float_normal() {
        float[] arr = { 1.0f, 2.0f, 3.0f, 4.0f };
        Arrays.replaceIf(arr, f -> f > 2.5f, 0.0f);
        assertEquals(1.0f, arr[0], 0.001f);
        assertEquals(2.0f, arr[1], 0.001f);
        assertEquals(0.0f, arr[2], 0.001f);
        assertEquals(0.0f, arr[3], 0.001f);
    }

    @Test
    public void testReplaceIf_2D_Float_normal() {
        float[][] arr = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
        Arrays.replaceIf(arr, f -> f < 3.0f, 9.9f);
        assertEquals(9.9f, arr[0][0], 0.001f);
        assertEquals(9.9f, arr[0][1], 0.001f);
        assertEquals(3.5f, arr[1][0], 0.001f);
        assertEquals(4.5f, arr[1][1], 0.001f);
    }

    @Test
    public void testReplaceIf_3D_Float_normal() {
        float[][][] arr = { { { 10.0f, 20.0f } } };
        Arrays.replaceIf(arr, f -> f == 20.0f, 200.0f);
        assertEquals(10.0f, arr[0][0][0], 0.001f);
        assertEquals(200.0f, arr[0][0][1], 0.001f);
    }

    // ============================================
    // Tests for replaceIf methods - double
    // ============================================

    @Test
    public void testReplaceIf_1D_Double_normal() {
        double[] arr = { 1.5, 2.5, 3.5, 4.5 };
        Arrays.replaceIf(arr, d -> d > 3.0, 0.0);
        assertEquals(1.5, arr[0], 0.001);
        assertEquals(2.5, arr[1], 0.001);
        assertEquals(0.0, arr[2], 0.001);
        assertEquals(0.0, arr[3], 0.001);
    }

    @Test
    public void testReplaceIf_1D_Double_null() {
        double[] arr = null;
        Arrays.replaceIf(arr, d -> d > 0, 0.0);   // Should not throw exception
    }

    @Test
    public void testReplaceIf_2D_Double_normal() {
        double[][] arr = { { 10.5, 20.5 }, { 30.5, 40.5 } };
        Arrays.replaceIf(arr, d -> d < 25.0, 100.0);
        assertEquals(100.0, arr[0][0], 0.001);
        assertEquals(100.0, arr[0][1], 0.001);
        assertEquals(30.5, arr[1][0], 0.001);
        assertEquals(40.5, arr[1][1], 0.001);
    }

    @Test
    public void testReplaceIf_3D_Double_normal() {
        double[][][] arr = { { { 5.5, 10.5 } } };
        Arrays.replaceIf(arr, d -> d == 10.5, 99.9);
        assertEquals(5.5, arr[0][0][0], 0.001);
        assertEquals(99.9, arr[0][0][1], 0.001);
    }

    // ============================================
    // Tests for reshape methods - boolean
    // ============================================

    @Test
    public void testReshape_1Dto2D_Boolean_normal() {
        boolean[] arr = { true, false, true, false, true, false };
        boolean[][] result = Arrays.reshape(arr, 3);
        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertTrue(result[0][0]);
        assertFalse(result[0][1]);
        assertTrue(result[0][2]);
        assertFalse(result[1][0]);
    }

    @Test
    public void testReshape_1Dto2D_Boolean_invalidSize() {
        boolean[] arr = { true, false, true, false, true };
        boolean[][] result = Arrays.reshape(arr, 3);
        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals(2, result[1].length);
    }

    @Test
    public void testReshape_1Dto3D_Boolean_normal() {
        boolean[] arr = { true, false, true, false, true, false };
        boolean[][][] result = Arrays.reshape(arr, 2, 3);
        assertEquals(1, result.length);
        assertEquals(2, result[0].length);
        assertEquals(3, result[0][0].length);
        assertTrue(result[0][0][0]);
        assertFalse(result[0][0][1]);
    }

    // ============================================
    // Tests for reshape methods - char
    // ============================================

    @Test
    public void testReshape_1Dto2D_Char_normal() {
        char[] arr = { 'a', 'b', 'c', 'd', 'e', 'f' };
        char[][] result = Arrays.reshape(arr, 3);
        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals('a', result[0][0]);
        assertEquals('d', result[1][0]);
    }

    @Test
    public void testReshape_1Dto3D_Char_normal() {
        char[] arr = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
        char[][][] result = Arrays.reshape(arr, 2, 2);
        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals('a', result[0][0][0]);
        assertEquals('e', result[1][0][0]);
    }

    // ============================================
    // Tests for reshape methods - byte
    // ============================================

    @Test
    public void testReshape_1Dto2D_Byte_normal() {
        byte[] arr = { 1, 2, 3, 4, 5, 6 };
        byte[][] result = Arrays.reshape(arr, 2);
        assertEquals(3, result.length);
        assertEquals(2, result[0].length);
        assertEquals(1, result[0][0]);
        assertEquals(5, result[2][0]);
    }

    @Test
    public void testReshape_1Dto3D_Byte_normal() {
        byte[] arr = { 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[][][] result = Arrays.reshape(arr, 2, 2);
        assertEquals(2, result.length);
        assertEquals(1, result[0][0][0]);
        assertEquals(5, result[1][0][0]);
    }

    // ============================================
    // Tests for reshape methods - short
    // ============================================

    @Test
    public void testReshape_1Dto2D_Short_normal() {
        short[] arr = { 10, 20, 30, 40, 50, 60 };
        short[][] result = Arrays.reshape(arr, 3);
        assertEquals(2, result.length);
        assertEquals(10, result[0][0]);
        assertEquals(40, result[1][0]);
    }

    @Test
    public void testReshape_1Dto3D_Short_normal() {
        short[] arr = { 10, 20, 30, 40, 50, 60 };
        short[][][] result = Arrays.reshape(arr, 2, 3);
        assertEquals(1, result.length);
        assertEquals(10, result[0][0][0]);
    }

    // ============================================
    // Tests for reshape methods - int
    // ============================================

    @Test
    public void testReshape_1Dto2D_Int_normal() {
        int[] arr = { 1, 2, 3, 4, 5, 6 };
        int[][] result = Arrays.reshape(arr, 3);
        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals(1, result[0][0]);
        assertEquals(4, result[1][0]);
        assertEquals(6, result[1][2]);
    }

    @Test
    public void testReshape_1Dto2D_Int_singleRow() {
        int[] arr = { 1, 2, 3 };
        int[][] result = Arrays.reshape(arr, 3);
        assertEquals(1, result.length);
        assertEquals(3, result[0].length);
    }

    @Test
    public void testReshape_1Dto3D_Int_normal() {
        int[] arr = { 1, 2, 3, 4, 5, 6, 7, 8 };
        int[][][] result = Arrays.reshape(arr, 2, 2);
        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(1, result[0][0][0]);
        assertEquals(5, result[1][0][0]);
    }

    // ============================================
    // Tests for reshape methods - long
    // ============================================

    @Test
    public void testReshape_1Dto2D_Long_normal() {
        long[] arr = { 100L, 200L, 300L, 400L };
        long[][] result = Arrays.reshape(arr, 2);
        assertEquals(2, result.length);
        assertEquals(100L, result[0][0]);
        assertEquals(300L, result[1][0]);
    }

    @Test
    public void testReshape_1Dto3D_Long_normal() {
        long[] arr = { 1L, 2L, 3L, 4L, 5L, 6L };
        long[][][] result = Arrays.reshape(arr, 2, 3);
        assertEquals(1, result.length);
        assertEquals(1L, result[0][0][0]);
    }

    // ============================================
    // Tests for reshape methods - float
    // ============================================

    @Test
    public void testReshape_1Dto2D_Float_normal() {
        float[] arr = { 1.1f, 2.2f, 3.3f, 4.4f };
        float[][] result = Arrays.reshape(arr, 2);
        assertEquals(2, result.length);
        assertEquals(1.1f, result[0][0], 0.001f);
        assertEquals(3.3f, result[1][0], 0.001f);
    }

    @Test
    public void testReshape_1Dto3D_Float_normal() {
        float[] arr = { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f };
        float[][][] result = Arrays.reshape(arr, 2, 3);
        assertEquals(1, result.length);
        assertEquals(1.0f, result[0][0][0], 0.001f);
    }

    // ============================================
    // Tests for reshape methods - double
    // ============================================

    @Test
    public void testReshape_1Dto2D_Double_normal() {
        double[] arr = { 1.5, 2.5, 3.5, 4.5 };
        double[][] result = Arrays.reshape(arr, 2);
        assertEquals(2, result.length);
        assertEquals(1.5, result[0][0], 0.001);
        assertEquals(3.5, result[1][0], 0.001);
    }

    @Test
    public void testReshape_1Dto3D_Double_normal() {
        double[] arr = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
        double[][][] result = Arrays.reshape(arr, 2, 3);
        assertEquals(1, result.length);
        assertEquals(1.0, result[0][0][0], 0.001);
    }

    // ============================================
    // Tests for flatten methods - boolean
    // ============================================

    @Test
    public void testFlatten_2D_Boolean_normal() {
        boolean[][] arr = { { true, false }, { true, false } };
        boolean[] result = Arrays.flatten(arr);
        assertEquals(4, result.length);
        assertTrue(result[0]);
        assertFalse(result[1]);
        assertTrue(result[2]);
        assertFalse(result[3]);
    }

    @Test
    public void testFlatten_2D_Boolean_null() {
        boolean[][] arr = null;
        boolean[] result = Arrays.flatten(arr);
        assertArrayEquals(N.EMPTY_BOOLEAN_ARRAY, result);
    }

    @Test
    public void testFlatten_3D_Boolean_normal() {
        boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
        boolean[] result = Arrays.flatten(arr);
        assertEquals(4, result.length);
        assertTrue(result[0]);
        assertFalse(result[1]);
        assertTrue(result[2]);
        assertFalse(result[3]);
    }

    // ============================================
    // Tests for flatten methods - char
    // ============================================

    @Test
    public void testFlatten_2D_Char_normal() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        char[] result = Arrays.flatten(arr);
        assertEquals(4, result.length);
        assertEquals('a', result[0]);
        assertEquals('d', result[3]);
    }

    @Test
    public void testFlatten_3D_Char_normal() {
        char[][][] arr = { { { 'x', 'y' } }, { { 'z' } } };
        char[] result = Arrays.flatten(arr);
        assertEquals(3, result.length);
        assertEquals('x', result[0]);
        assertEquals('z', result[2]);
    }

    // ============================================
    // Tests for flatten methods - byte
    // ============================================

    @Test
    public void testFlatten_2D_Byte_normal() {
        byte[][] arr = { { 1, 2, 3 }, { 4, 5 } };
        byte[] result = Arrays.flatten(arr);
        assertEquals(5, result.length);
        assertEquals(1, result[0]);
        assertEquals(5, result[4]);
    }

    @Test
    public void testFlatten_3D_Byte_normal() {
        byte[][][] arr = { { { 1, 2 } }, { { 3 } } };
        byte[] result = Arrays.flatten(arr);
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
        assertEquals(3, result[2]);
    }

    // ============================================
    // Tests for flatten methods - short
    // ============================================

    @Test
    public void testFlatten_2D_Short_normal() {
        short[][] arr = { { 10, 20 }, { 30, 40 } };
        short[] result = Arrays.flatten(arr);
        assertEquals(4, result.length);
        assertEquals(10, result[0]);
        assertEquals(40, result[3]);
    }

    @Test
    public void testFlatten_3D_Short_normal() {
        short[][][] arr = { { { 100, 200 } }, { { 300 } } };
        short[] result = Arrays.flatten(arr);
        assertEquals(3, result.length);
        assertEquals(100, result[0]);
        assertEquals(300, result[2]);
    }

    // ============================================
    // Tests for flatten methods - int
    // ============================================

    @Test
    public void testFlatten_2D_Int_normal() {
        int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        int[] result = Arrays.flatten(arr);
        assertEquals(6, result.length);
        assertEquals(1, result[0]);
        assertEquals(6, result[5]);
    }

    @Test
    public void testFlatten_2D_Int_null() {
        int[][] arr = null;
        int[] result = Arrays.flatten(arr);
        assertArrayEquals(N.EMPTY_INT_ARRAY, result);
    }

    @Test
    public void testFlatten_2D_Int_jaggedArray() {
        int[][] arr = { { 1, 2 }, { 3, 4, 5 }, { 6 } };
        int[] result = Arrays.flatten(arr);
        assertEquals(6, result.length);
        assertEquals(1, result[0]);
        assertEquals(6, result[5]);
    }

    @Test
    public void testFlatten_3D_Int_normal() {
        int[][][] arr = { { { 1, 2 }, { 3 } }, { { 4, 5 } } };
        int[] result = Arrays.flatten(arr);
        assertEquals(5, result.length);
        assertEquals(1, result[0]);
        assertEquals(5, result[4]);
    }

    // ============================================
    // Tests for flatten methods - long
    // ============================================

    @Test
    public void testFlatten_2D_Long_normal() {
        long[][] arr = { { 100L, 200L }, { 300L, 400L } };
        long[] result = Arrays.flatten(arr);
        assertEquals(4, result.length);
        assertEquals(100L, result[0]);
        assertEquals(400L, result[3]);
    }

    @Test
    public void testFlatten_3D_Long_normal() {
        long[][][] arr = { { { 1000L, 2000L } }, { { 3000L } } };
        long[] result = Arrays.flatten(arr);
        assertEquals(3, result.length);
        assertEquals(1000L, result[0]);
        assertEquals(3000L, result[2]);
    }

    // ============================================
    // Tests for flatten methods - float
    // ============================================

    @Test
    public void testFlatten_2D_Float_normal() {
        float[][] arr = { { 1.1f, 2.2f }, { 3.3f, 4.4f } };
        float[] result = Arrays.flatten(arr);
        assertEquals(4, result.length);
        assertEquals(1.1f, result[0], 0.001f);
        assertEquals(4.4f, result[3], 0.001f);
    }

    @Test
    public void testFlatten_3D_Float_normal() {
        float[][][] arr = { { { 1.0f, 2.0f } }, { { 3.0f } } };
        float[] result = Arrays.flatten(arr);
        assertEquals(3, result.length);
        assertEquals(1.0f, result[0], 0.001f);
        assertEquals(3.0f, result[2], 0.001f);
    }

    // ============================================
    // Tests for flatten methods - double
    // ============================================

    @Test
    public void testFlatten_2D_Double_normal() {
        double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
        double[] result = Arrays.flatten(arr);
        assertEquals(4, result.length);
        assertEquals(1.5, result[0], 0.001);
        assertEquals(4.5, result[3], 0.001);
    }

    @Test
    public void testFlatten_2D_Double_null() {
        double[][] arr = null;
        double[] result = Arrays.flatten(arr);
        assertArrayEquals(N.EMPTY_DOUBLE_ARRAY, result, 0.001);
    }

    @Test
    public void testFlatten_3D_Double_normal() {
        double[][][] arr = { { { 10.5, 20.5 } }, { { 30.5 } } };
        double[] result = Arrays.flatten(arr);
        assertEquals(3, result.length);
        assertEquals(10.5, result[0], 0.001);
        assertEquals(30.5, result[2], 0.001);
    }

    // ============================================
    // Tests for flatOp methods - boolean
    // ============================================

    @Test
    public void testFlatOp_2D_Boolean_normal() {
        boolean[][] arr = { { true, false }, { false, true } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(4, count[0]);
    }

    @Test
    public void testFlatOp_3D_Boolean_normal() {
        boolean[][][] arr = { { { true, false } }, { { true } } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(3, count[0]);
    }

    // ============================================
    // Tests for flatOp methods - char
    // ============================================

    @Test
    public void testFlatOp_2D_Char_normal() {
        char[][] arr = { { 'a', 'b' }, { 'c' } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(3, count[0]);
    }

    @Test
    public void testFlatOp_3D_Char_normal() {
        char[][][] arr = { { { 'x', 'y' } }, { { 'z' } } };
        final int[] sum = { 0 };
        Arrays.flatOp(arr, subArr -> sum[0] += subArr.length);
        assertEquals(3, sum[0]);
    }

    // ============================================
    // Tests for flatOp methods - byte
    // ============================================

    @Test
    public void testFlatOp_2D_Byte_normal() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(4, count[0]);
    }

    @Test
    public void testFlatOp_3D_Byte_normal() {
        byte[][][] arr = { { { 1, 2 } }, { { 3 } } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(3, count[0]);
    }

    // ============================================
    // Tests for flatOp methods - short
    // ============================================

    @Test
    public void testFlatOp_2D_Short_normal() {
        short[][] arr = { { 10, 20 }, { 30 } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(3, count[0]);
    }

    @Test
    public void testFlatOp_3D_Short_normal() {
        short[][][] arr = { { { 100, 200 } }, { { 300 } } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(3, count[0]);
    }

    // ============================================
    // Tests for flatOp methods - int
    // ============================================

    @Test
    public void testFlatOp_2D_Int_normal() {
        int[][] arr = { { 1, 2, 3 }, { 4, 5 } };
        final int[] sum = { 0 };
        Arrays.flatOp(arr, subArr -> {
            for (int val : subArr) {
                sum[0] += val;
            }
        });
        assertEquals(15, sum[0]);
    }

    @Test
    public void testFlatOp_2D_Int_null() {
        int[][] arr = null;
        final int[] sum = { 0 };
        Arrays.flatOp(arr, subArr -> sum[0]++);   // Should not throw exception
        assertEquals(0, sum[0]);
    }

    @Test
    public void testFlatOp_3D_Int_normal() {
        int[][][] arr = { { { 1, 2 }, { 3 } }, { { 4 } } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(4, count[0]);
    }

    // ============================================
    // Tests for flatOp methods - long
    // ============================================

    @Test
    public void testFlatOp_2D_Long_normal() {
        long[][] arr = { { 100L, 200L }, { 300L } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(3, count[0]);
    }

    @Test
    public void testFlatOp_3D_Long_normal() {
        long[][][] arr = { { { 1000L, 2000L } }, { { 3000L } } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(3, count[0]);
    }

    // ============================================
    // Tests for flatOp methods - float
    // ============================================

    @Test
    public void testFlatOp_2D_Float_normal() {
        float[][] arr = { { 1.1f, 2.2f }, { 3.3f } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(3, count[0]);
    }

    @Test
    public void testFlatOp_3D_Float_normal() {
        float[][][] arr = { { { 1.0f, 2.0f } }, { { 3.0f } } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(3, count[0]);
    }

    // ============================================
    // Tests for flatOp methods - double
    // ============================================

    @Test
    public void testFlatOp_2D_Double_normal() {
        double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
        final double[] sum = { 0.0 };
        Arrays.flatOp(arr, subArr -> {
            for (double val : subArr) {
                sum[0] += val;
            }
        });
        assertEquals(12.0, sum[0], 0.001);
    }

    @Test
    public void testFlatOp_3D_Double_normal() {
        double[][][] arr = { { { 10.5, 20.5 } }, { { 30.5 } } };
        final int[] count = { 0 };
        Arrays.flatOp(arr, subArr -> count[0] += subArr.length);
        assertEquals(3, count[0]);
    }

    // ============================================
    // Tests for zip methods - boolean (1D)
    // ============================================

    @Test
    public void testZip_1D_Boolean_twoArrays() {
        boolean[] a = { true, false, true };
        boolean[] b = { false, false, true };
        boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);
        assertEquals(3, result.length);
        assertFalse(result[0]);
        assertFalse(result[1]);
        assertTrue(result[2]);
    }

    @Test
    public void testZip_1D_Boolean_twoArrays_withDefaults() {
        boolean[] a = { true, false };
        boolean[] b = { false, false, true };
        boolean[] result = Arrays.zip(a, b, true, false, (x, y) -> x || y);
        assertEquals(3, result.length);
        assertTrue(result[0]);
        assertFalse(result[1]);
        assertTrue(result[2]);
    }

    @Test
    public void testZip_1D_Boolean_threeArrays() {
        boolean[] a = { true, false };
        boolean[] b = { false, true };
        boolean[] c = { true, true };
        boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> (x || y) && z);
        assertEquals(2, result.length);
        assertTrue(result[0]);
        assertTrue(result[1]);
    }

    @Test
    public void testZip_1D_Boolean_threeArrays_withDefaults() {
        boolean[] a = { true, false };
        boolean[] b = { false, true, true };
        boolean[] c = { true };
        boolean[] result = Arrays.zip(a, b, c, false, false, true, (x, y, z) -> x || y || z);
        assertEquals(3, result.length);
        assertTrue(result[0]);
        assertTrue(result[1]);
        assertTrue(result[2]);
    }

    // ============================================
    // Tests for zip methods - char (1D)
    // ============================================

    @Test
    public void testZip_1D_Char_twoArrays() {
        char[] a = { 'a', 'b', 'c' };
        char[] b = { '1', '2', '3' };
        char[] result = Arrays.zip(a, b, (x, y) -> x);
        assertEquals(3, result.length);
        assertEquals('a', result[0]);
        assertEquals('c', result[2]);
    }

    @Test
    public void testZip_1D_Char_twoArrays_withDefaults() {
        char[] a = { 'a', 'b' };
        char[] b = { 'x', 'y', 'z' };
        char[] result = Arrays.zip(a, b, 'A', 'X', (x, y) -> x);
        assertEquals(3, result.length);
        assertEquals('a', result[0]);
        assertEquals('A', result[2]);
    }

    @Test
    public void testZip_1D_Char_threeArrays() {
        char[] a = { 'a', 'b' };
        char[] b = { 'c', 'd' };
        char[] c = { 'e', 'f' };
        char[] result = Arrays.zip(a, b, c, (x, y, z) -> x);
        assertEquals(2, result.length);
        assertEquals('a', result[0]);
    }

    // ============================================
    // Tests for zip methods - byte (1D)
    // ============================================

    @Test
    public void testZip_1D_Byte_twoArrays() {
        byte[] a = { 1, 2, 3 };
        byte[] b = { 4, 5, 6 };
        byte[] result = Arrays.zip(a, b, (x, y) -> (byte) (x + y));
        assertEquals(3, result.length);
        assertEquals(5, result[0]);
        assertEquals(9, result[2]);
    }

    @Test
    public void testZip_1D_Byte_twoArrays_withDefaults() {
        byte[] a = { 1, 2 };
        byte[] b = { 3, 4, 5 };
        byte[] result = Arrays.zip(a, b, (byte) 0, (byte) 0, (x, y) -> (byte) (x + y));
        assertEquals(3, result.length);
        assertEquals(4, result[0]);
        assertEquals(5, result[2]);
    }

    @Test
    public void testZip_1D_Byte_threeArrays() {
        byte[] a = { 1, 2 };
        byte[] b = { 3, 4 };
        byte[] c = { 5, 6 };
        byte[] result = Arrays.zip(a, b, c, (x, y, z) -> (byte) (x + y + z));
        assertEquals(2, result.length);
        assertEquals(9, result[0]);
        assertEquals(12, result[1]);
    }

    // ============================================
    // Tests for zip methods - short (1D)
    // ============================================

    @Test
    public void testZip_1D_Short_twoArrays() {
        short[] a = { 10, 20, 30 };
        short[] b = { 5, 10, 15 };
        short[] result = Arrays.zip(a, b, (x, y) -> (short) (x + y));
        assertEquals(3, result.length);
        assertEquals(15, result[0]);
        assertEquals(45, result[2]);
    }

    @Test
    public void testZip_1D_Short_twoArrays_withDefaults() {
        short[] a = { 10, 20 };
        short[] b = { 5, 10, 15 };
        short[] result = Arrays.zip(a, b, (short) 0, (short) 0, (x, y) -> (short) (x + y));
        assertEquals(3, result.length);
        assertEquals(15, result[0]);
        assertEquals(15, result[2]);
    }

    @Test
    public void testZip_1D_Short_threeArrays() {
        short[] a = { 10, 20 };
        short[] b = { 5, 10 };
        short[] c = { 1, 2 };
        short[] result = Arrays.zip(a, b, c, (x, y, z) -> (short) (x + y + z));
        assertEquals(2, result.length);
        assertEquals(16, result[0]);
    }

    // ============================================
    // Tests for zip methods - int (1D)
    // ============================================

    @Test
    public void testZip_1D_Int_twoArrays() {
        int[] a = { 1, 2, 3 };
        int[] b = { 4, 5, 6 };
        int[] result = Arrays.zip(a, b, (x, y) -> x + y);
        assertEquals(3, result.length);
        assertEquals(5, result[0]);
        assertEquals(9, result[2]);
    }

    @Test
    public void testZip_1D_Int_twoArrays_withDefaults() {
        int[] a = { 1, 2 };
        int[] b = { 3, 4, 5 };
        int[] result = Arrays.zip(a, b, 0, 0, (x, y) -> x + y);
        assertEquals(3, result.length);
        assertEquals(4, result[0]);
        assertEquals(5, result[2]);
    }

    @Test
    public void testZip_1D_Int_threeArrays() {
        int[] a = { 1, 2, 3 };
        int[] b = { 4, 5, 6 };
        int[] c = { 7, 8, 9 };
        int[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
        assertEquals(3, result.length);
        assertEquals(12, result[0]);
        assertEquals(18, result[2]);
    }

    @Test
    public void testZip_1D_Int_threeArrays_withDefaults() {
        int[] a = { 1, 2 };
        int[] b = { 3, 4, 5 };
        int[] c = { 6 };
        int[] result = Arrays.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
        assertEquals(3, result.length);
        assertEquals(10, result[0]);
        assertEquals(5, result[2]);
    }

    // ============================================
    // Tests for zip methods - long (1D)
    // ============================================

    @Test
    public void testZip_1D_Long_twoArrays() {
        long[] a = { 100L, 200L, 300L };
        long[] b = { 10L, 20L, 30L };
        long[] result = Arrays.zip(a, b, (x, y) -> x + y);
        assertEquals(3, result.length);
        assertEquals(110L, result[0]);
        assertEquals(330L, result[2]);
    }

    @Test
    public void testZip_1D_Long_twoArrays_withDefaults() {
        long[] a = { 100L, 200L };
        long[] b = { 10L, 20L, 30L };
        long[] result = Arrays.zip(a, b, 0L, 0L, (x, y) -> x + y);
        assertEquals(3, result.length);
        assertEquals(110L, result[0]);
        assertEquals(30L, result[2]);
    }

    @Test
    public void testZip_1D_Long_threeArrays() {
        long[] a = { 1L, 2L };
        long[] b = { 3L, 4L };
        long[] c = { 5L, 6L };
        long[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
        assertEquals(2, result.length);
        assertEquals(9L, result[0]);
        assertEquals(12L, result[1]);
    }

    // ============================================
    // Tests for zip methods - float (1D)
    // ============================================

    @Test
    public void testZip_1D_Float_twoArrays() {
        float[] a = { 1.5f, 2.5f, 3.5f };
        float[] b = { 0.5f, 1.5f, 2.5f };
        float[] result = Arrays.zip(a, b, (x, y) -> x + y);
        assertEquals(3, result.length);
        assertEquals(2.0f, result[0], 0.001f);
        assertEquals(6.0f, result[2], 0.001f);
    }

    @Test
    public void testZip_1D_Float_twoArrays_withDefaults() {
        float[] a = { 1.0f, 2.0f };
        float[] b = { 3.0f, 4.0f, 5.0f };
        float[] result = Arrays.zip(a, b, 0.0f, 0.0f, (x, y) -> x + y);
        assertEquals(3, result.length);
        assertEquals(4.0f, result[0], 0.001f);
        assertEquals(5.0f, result[2], 0.001f);
    }

    @Test
    public void testZip_1D_Float_threeArrays() {
        float[] a = { 1.0f, 2.0f };
        float[] b = { 3.0f, 4.0f };
        float[] c = { 5.0f, 6.0f };
        float[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
        assertEquals(2, result.length);
        assertEquals(9.0f, result[0], 0.001f);
        assertEquals(12.0f, result[1], 0.001f);
    }

    // ============================================
    // Tests for zip methods - double (1D)
    // ============================================

    @Test
    public void testZip_1D_Double_twoArrays() {
        double[] a = { 1.5, 2.5, 3.5 };
        double[] b = { 0.5, 1.5, 2.5 };
        double[] result = Arrays.zip(a, b, (x, y) -> x + y);
        assertEquals(3, result.length);
        assertEquals(2.0, result[0], 0.001);
        assertEquals(6.0, result[2], 0.001);
    }

    @Test
    public void testZip_1D_Double_twoArrays_withDefaults() {
        double[] a = { 1.0, 2.0 };
        double[] b = { 3.0, 4.0, 5.0 };
        double[] result = Arrays.zip(a, b, 0.0, 0.0, (x, y) -> x + y);
        assertEquals(3, result.length);
        assertEquals(4.0, result[0], 0.001);
        assertEquals(5.0, result[2], 0.001);
    }

    @Test
    public void testZip_1D_Double_threeArrays() {
        double[] a = { 1.0, 2.0, 3.0 };
        double[] b = { 4.0, 5.0, 6.0 };
        double[] c = { 7.0, 8.0, 9.0 };
        double[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
        assertEquals(3, result.length);
        assertEquals(12.0, result[0], 0.001);
        assertEquals(18.0, result[2], 0.001);
    }

    @Test
    public void testZip_1D_Double_threeArrays_withDefaults() {
        double[] a = { 1.0, 2.0 };
        double[] b = { 3.0, 4.0, 5.0 };
        double[] c = { 6.0 };
        double[] result = Arrays.zip(a, b, c, 0.0, 0.0, 0.0, (x, y, z) -> x + y + z);
        assertEquals(3, result.length);
        assertEquals(10.0, result[0], 0.001);
        assertEquals(5.0, result[2], 0.001);
    }

    // ============================================
    // Tests for zip methods - two-dimensional arrays
    // ============================================

    @Test
    public void testZip_2D_Int_twoArrays() {
        int[][] a = { { 1, 2 }, { 3, 4 } };
        int[][] b = { { 5, 6 }, { 7, 8 } };
        int[][] result = Arrays.zip(a, b, (x, y) -> x + y);
        assertEquals(2, result.length);
        assertEquals(6, result[0][0]);
        assertEquals(12, result[1][1]);
    }

    @Test
    public void testZip_2D_Double_twoArrays() {
        double[][] a = { { 1.5, 2.5 }, { 3.5, 4.5 } };
        double[][] b = { { 0.5, 1.5 }, { 2.5, 3.5 } };
        double[][] result = Arrays.zip(a, b, (x, y) -> x + y);
        assertEquals(2, result.length);
        assertEquals(2.0, result[0][0], 0.001);
        assertEquals(8.0, result[1][1], 0.001);
    }

    // ============================================
    // Tests for zip methods - three-dimensional arrays
    // ============================================

    @Test
    public void testZip_3D_Int_twoArrays() {
        int[][][] a = { { { 1, 2 } } };
        int[][][] b = { { { 3, 4 } } };
        int[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
        assertEquals(1, result.length);
        assertEquals(4, result[0][0][0]);
        assertEquals(6, result[0][0][1]);
    }

    @Test
    public void testZip_3D_Double_twoArrays() {
        double[][][] a = { { { 1.5, 2.5 } } };
        double[][][] b = { { { 0.5, 1.5 } } };
        double[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
        assertEquals(1, result.length);
        assertEquals(2.0, result[0][0][0], 0.001);
        assertEquals(4.0, result[0][0][1], 0.001);
    }

    // ============================================
    // Tests for totalCountOfElements methods
    // ============================================

    @Test
    public void testTotalCountOfElements_2D_Boolean() {
        boolean[][] arr = { { true, false }, { true, false, true } };
        long count = Arrays.totalCountOfElements(arr);
        assertEquals(5L, count);
    }

    @Test
    public void testTotalCountOfElements_2D_Boolean_null() {
        boolean[][] arr = null;
        long count = Arrays.totalCountOfElements(arr);
        assertEquals(0L, count);
    }

    @Test
    public void testTotalCountOfElements_3D_Boolean() {
        boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
        long count = Arrays.totalCountOfElements(arr);
        assertEquals(4L, count);
    }

    @Test
    public void testTotalCountOfElements_2D_Int() {
        int[][] arr = { { 1, 2, 3 }, { 4, 5 } };
        long count = Arrays.totalCountOfElements(arr);
        assertEquals(5L, count);
    }

    @Test
    public void testTotalCountOfElements_3D_Double() {
        double[][][] arr = { { { 1.0, 2.0 }, { 3.0 } }, { { 4.0, 5.0 } } };
        long count = Arrays.totalCountOfElements(arr);
        assertEquals(5L, count);
    }

    // ============================================
    // Tests for minSubArrayLen methods
    // ============================================

    @Test
    public void testMinSubArrayLen_2D_Boolean() {
        boolean[][] arr = { { true, false, true }, { false }, { true, false } };
        int minLen = Arrays.minSubArrayLen(arr);
        assertEquals(1, minLen);
    }

    @Test
    public void testMinSubArrayLen_2D_Boolean_null() {
        boolean[][] arr = null;
        int minLen = Arrays.minSubArrayLen(arr);
        assertEquals(0, minLen);
    }

    @Test
    public void testMinSubArrayLen_2D_Int() {
        int[][] arr = { { 1, 2, 3 }, { 4, 5 }, { 6, 7, 8, 9 } };
        int minLen = Arrays.minSubArrayLen(arr);
        assertEquals(2, minLen);
    }

    @Test
    public void testMinSubArrayLen_2D_Double() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0, 5.0 }, { 6.0 } };
        int minLen = Arrays.minSubArrayLen(arr);
        assertEquals(1, minLen);
    }

    // ============================================
    // Tests for maxSubArrayLen methods
    // ============================================

    @Test
    public void testMaxSubArrayLen_2D_Boolean() {
        boolean[][] arr = { { true, false }, { true, false, true }, { false } };
        int maxLen = Arrays.maxSubArrayLen(arr);
        assertEquals(3, maxLen);
    }

    @Test
    public void testMaxSubArrayLen_2D_Boolean_null() {
        boolean[][] arr = null;
        int maxLen = Arrays.maxSubArrayLen(arr);
        assertEquals(0, maxLen);
    }

    @Test
    public void testMaxSubArrayLen_2D_Int() {
        int[][] arr = { { 1, 2 }, { 3, 4, 5, 6 }, { 7 } };
        int maxLen = Arrays.maxSubArrayLen(arr);
        assertEquals(4, maxLen);
    }

    @Test
    public void testMaxSubArrayLen_2D_Double() {
        double[][] arr = { { 1.0 }, { 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        int maxLen = Arrays.maxSubArrayLen(arr);
        assertEquals(3, maxLen);
    }

    // ============================================
    // Tests for type conversion methods - toBoolean
    // ============================================

    @Test
    public void testToBoolean_1D_Byte() {
        byte[] arr = { 1, 0, 1, 0 };
        boolean[] result = Arrays.toBoolean(arr);
        assertNotNull(result);
        assertEquals(4, result.length);
        assertTrue(result[0]);
        assertFalse(result[1]);
    }

    @Test
    public void testToBoolean_2D_Byte() {
        byte[][] arr = { { 1, 0 }, { 0, 1 } };
        boolean[][] result = Arrays.toBoolean(arr);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertTrue(result[0][0]);
        assertFalse(result[0][1]);
    }

    @Test
    public void testToBoolean_3D_Int() {
        int[][][] arr = { { { 1, 0 } } };
        boolean[][][] result = Arrays.toBoolean(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for type conversion methods - toByte
    // ============================================

    @Test
    public void testToByte_1D_Boolean() {
        boolean[] arr = { true, false, true };
        byte[] result = Arrays.toByte(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
        assertEquals(0, result[1]);
    }

    @Test
    public void testToByte_2D_Boolean() {
        boolean[][] arr = { { true, false }, { false, true } };
        byte[][] result = Arrays.toByte(arr);
        assertNotNull(result);
        assertEquals(1, result[0][0]);
        assertEquals(0, result[0][1]);
    }

    @Test
    public void testToByte_3D_Boolean() {
        boolean[][][] arr = { { { true, false } } };
        byte[][][] result = Arrays.toByte(arr);
        assertNotNull(result);
        assertEquals(1, result[0][0][0]);
        assertEquals(0, result[0][0][1]);
    }

    // ============================================
    // Tests for type conversion methods - toChar
    // ============================================

    @Test
    public void testToChar_1D_Int() {
        int[] arr = { 65, 66, 67 };
        char[] result = Arrays.toChar(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals('A', result[0]);
        assertEquals('B', result[1]);
        assertEquals('C', result[2]);
    }

    @Test
    public void testToChar_2D_Int() {
        int[][] arr = { { 65, 66 }, { 67, 68 } };
        char[][] result = Arrays.toChar(arr);
        assertNotNull(result);
        assertEquals('A', result[0][0]);
        assertEquals('D', result[1][1]);
    }

    @Test
    public void testToChar_3D_Int() {
        int[][][] arr = { { { 65, 66 } } };
        char[][][] result = Arrays.toChar(arr);
        assertNotNull(result);
        assertEquals('A', result[0][0][0]);
    }

    // ============================================
    // Tests for type conversion methods - toShort
    // ============================================

    @Test
    public void testToShort_1D_Byte() {
        byte[] arr = { 1, 2, 3 };
        short[] result = Arrays.toShort(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
        assertEquals(3, result[2]);
    }

    @Test
    public void testToShort_2D_Byte() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        short[][] result = Arrays.toShort(arr);
        assertNotNull(result);
        assertEquals(1, result[0][0]);
        assertEquals(4, result[1][1]);
    }

    @Test
    public void testToShort_3D_Byte() {
        byte[][][] arr = { { { 10, 20 } } };
        short[][][] result = Arrays.toShort(arr);
        assertNotNull(result);
        assertEquals(10, result[0][0][0]);
    }

    // ============================================
    // Tests for type conversion methods - toInt
    // ============================================

    @Test
    public void testToInt_1D_Char() {
        char[] arr = { 'A', 'B', 'C' };
        int[] result = Arrays.toInt(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(65, result[0]);
    }

    @Test
    public void testToInt_1D_Byte() {
        byte[] arr = { 1, 2, 3, 4 };
        int[] result = Arrays.toInt(arr);
        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals(1, result[0]);
        assertEquals(4, result[3]);
    }

    @Test
    public void testToInt_1D_Short() {
        short[] arr = { 10, 20, 30 };
        int[] result = Arrays.toInt(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(10, result[0]);
    }

    @Test
    public void testToInt_1D_Float() {
        float[] arr = { 1.5f, 2.7f, 3.2f };
        int[] result = Arrays.toInt(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
    }

    @Test
    public void testToInt_1D_Double() {
        double[] arr = { 1.5, 2.7, 3.9 };
        int[] result = Arrays.toInt(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
    }

    @Test
    public void testToInt_3D_Double() {
        double[][][] arr = { { { 10.5, 20.5 } } };
        int[][][] result = Arrays.toInt(arr);
        assertNotNull(result);
        assertEquals(10, result[0][0][0]);
    }

    // ============================================
    // Tests for type conversion methods - toLong
    // ============================================

    @Test
    public void testToLong_1D_Byte() {
        byte[] arr = { 1, 2, 3 };
        long[] result = Arrays.toLong(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1L, result[0]);
    }

    @Test
    public void testToLong_1D_Short() {
        short[] arr = { 10, 20, 30 };
        long[] result = Arrays.toLong(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(10L, result[0]);
    }

    @Test
    public void testToLong_1D_Int() {
        int[] arr = { 100, 200, 300 };
        long[] result = Arrays.toLong(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(100L, result[0]);
        assertEquals(300L, result[2]);
    }

    @Test
    public void testToLong_1D_Float() {
        float[] arr = { 1.5f, 2.7f, 3.2f };
        long[] result = Arrays.toLong(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1L, result[0]);
    }

    @Test
    public void testToLong_1D_Double() {
        double[] arr = { 1.5, 2.7, 3.9 };
        long[] result = Arrays.toLong(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1L, result[0]);
    }

    @Test
    public void testToLong_2D_Int() {
        int[][] arr = { { 10, 20 }, { 30, 40 } };
        long[][] result = Arrays.toLong(arr);
        assertNotNull(result);
        assertEquals(10L, result[0][0]);
    }

    @Test
    public void testToLong_3D_Double() {
        double[][][] arr = { { { 100.5, 200.5 } } };
        long[][][] result = Arrays.toLong(arr);
        assertNotNull(result);
        assertEquals(100L, result[0][0][0]);
    }

    // ============================================
    // Tests for type conversion methods - toFloat
    // ============================================

    @Test
    public void testToFloat_1D_Byte() {
        byte[] arr = { 1, 2, 3 };
        float[] result = Arrays.toFloat(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1.0f, result[0], 0.001f);
    }

    @Test
    public void testToFloat_1D_Short() {
        short[] arr = { 10, 20, 30 };
        float[] result = Arrays.toFloat(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(10.0f, result[0], 0.001f);
    }

    @Test
    public void testToFloat_1D_Int() {
        int[] arr = { 100, 200, 300 };
        float[] result = Arrays.toFloat(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(100.0f, result[0], 0.001f);
    }

    @Test
    public void testToFloat_1D_Long() {
        long[] arr = { 1000L, 2000L, 3000L };
        float[] result = Arrays.toFloat(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1000.0f, result[0], 0.001f);
    }

    @Test
    public void testToFloat_2D_Int() {
        int[][] arr = { { 10, 20 }, { 30, 40 } };
        float[][] result = Arrays.toFloat(arr);
        assertNotNull(result);
        assertEquals(10.0f, result[0][0], 0.001f);
    }

    @Test
    public void testToFloat_3D_Long() {
        long[][][] arr = { { { 100L, 200L } } };
        float[][][] result = Arrays.toFloat(arr);
        assertNotNull(result);
        assertEquals(100.0f, result[0][0][0], 0.001f);
    }

    // ============================================
    // Tests for type conversion methods - toDouble
    // ============================================

    @Test
    public void testToDouble_1D_Byte() {
        byte[] arr = { 1, 2, 3 };
        double[] result = Arrays.toDouble(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1.0, result[0], 0.001);
    }

    @Test
    public void testToDouble_1D_Short() {
        short[] arr = { 10, 20, 30 };
        double[] result = Arrays.toDouble(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(10.0, result[0], 0.001);
    }

    @Test
    public void testToDouble_1D_Int() {
        int[] arr = { 100, 200, 300 };
        double[] result = Arrays.toDouble(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(100.0, result[0], 0.001);
        assertEquals(300.0, result[2], 0.001);
    }

    @Test
    public void testToDouble_1D_Long() {
        long[] arr = { 1000L, 2000L, 3000L };
        double[] result = Arrays.toDouble(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1000.0, result[0], 0.001);
    }

    @Test
    public void testToDouble_1D_Float() {
        float[] arr = { 1.5f, 2.5f, 3.5f };
        double[] result = Arrays.toDouble(arr);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertTrue(Math.abs(1.5 - result[0]) < 0.01);
    }

    @Test
    public void testToDouble_2D_Int() {
        int[][] arr = { { 10, 20 }, { 30, 40 } };
        double[][] result = Arrays.toDouble(arr);
        assertNotNull(result);
        assertEquals(10.0, result[0][0], 0.001);
        assertEquals(40.0, result[1][1], 0.001);
    }

    @Test
    public void testToDouble_3D_Float() {
        float[][][] arr = { { { 1.5f, 2.5f } } };
        double[][][] result = Arrays.toDouble(arr);
        assertNotNull(result);
        assertTrue(Math.abs(1.5 - result[0][0][0]) < 0.01);
    }

    // ============================================
    // Tests for null handling across methods
    // ============================================

    @Test
    public void testNullHandling_mapToObj() {
        int[] arr = null;
        String[] result = Arrays.mapToObj(arr, i -> String.valueOf(i), String.class);
        assertEquals(0, result.length);
    }

    @Test
    public void testNullHandling_flatten() {
        int[][] arr = null;
        int[] result = Arrays.flatten(arr);
        assertEquals(0, result.length);
    }

    @Test
    public void testNullHandling_reshape() {
        int[] arr = null;
        int[][] result = Arrays.reshape(arr, 2);
        assertEquals(0, result.length);
    }

    @Test
    public void testNullHandling_updateAll() {
        int[] arr = null;
        // Should not throw exception
        Arrays.updateAll(arr, i -> i * 2);
    }

    @Test
    public void testNullHandling_replaceIf() {
        int[] arr = null;
        // Should not throw exception
        Arrays.replaceIf(arr, i -> i > 0, 0);
    }

    @Test
    public void testNullHandling_typeConversion() {
        int[] arr = null;
        double[] result = Arrays.toDouble(arr);
        assertEquals(0, result.length);
    }
}
