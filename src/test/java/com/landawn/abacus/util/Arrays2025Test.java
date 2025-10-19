package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

/**
 * Comprehensive unit tests for the Arrays utility class.
 * This test class covers a representative sample of the extensive array manipulation methods
 * provided by the Arrays class, including reshaping, flattening, mapping, statistical operations,
 * and various transformations across different primitive types and Object arrays.
 */
@Tag("2025")
public class Arrays2025Test extends TestBase {

    // ============================================
    // Tests for println methods
    // ============================================

    @Test
    public void testPrintln_1D_ObjectArray() {
        Object[] arr = { "Hello", "World", 123 };
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("World"));
    }

    @Test
    public void testPrintln_1D_NullArray() {
        Object[] arr = null;
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("null"));
    }

    @Test
    public void testPrintln_1D_EmptyArray() {
        Object[] arr = {};
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("[]"));
    }

    @Test
    public void testPrintln_2D_ObjectArray() {
        Object[][] arr = { { "A", "B" }, { "C", "D" } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_ObjectArray() {
        Object[][][] arr = { { { "A" } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_1D_BooleanArray() {
        boolean[] arr = { true, false, true };
        String result = Arrays.println(arr);
        assertNotNull(result);
        assertTrue(result.contains("true"));
        assertTrue(result.contains("false"));
    }

    @Test
    public void testPrintln_2D_BooleanArray() {
        boolean[][] arr = { { true, false }, { false, true } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_BooleanArray() {
        boolean[][][] arr = { { { true, false } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_1D_CharArray() {
        char[] arr = { 'a', 'b', 'c' };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_CharArray() {
        char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_CharArray() {
        char[][][] arr = { { { 'a', 'b' } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_1D_ByteArray() {
        byte[] arr = { 1, 2, 3 };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_ByteArray() {
        byte[][] arr = { { 1, 2 }, { 3, 4 } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_ByteArray() {
        byte[][][] arr = { { { 1, 2 } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_1D_ShortArray() {
        short[] arr = { 1, 2, 3 };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_ShortArray() {
        short[][] arr = { { 1, 2 }, { 3, 4 } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_ShortArray() {
        short[][][] arr = { { { 1, 2 } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_1D_IntArray() {
        int[] arr = { 1, 2, 3 };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_IntArray() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_IntArray() {
        int[][][] arr = { { { 1, 2 } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_1D_LongArray() {
        long[] arr = { 1L, 2L, 3L };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_LongArray() {
        long[][] arr = { { 1L, 2L }, { 3L, 4L } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_LongArray() {
        long[][][] arr = { { { 1L, 2L } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_1D_FloatArray() {
        float[] arr = { 1.0f, 2.0f, 3.0f };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_FloatArray() {
        float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_FloatArray() {
        float[][][] arr = { { { 1.0f, 2.0f } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_1D_DoubleArray() {
        double[] arr = { 1.0, 2.0, 3.0 };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_2D_DoubleArray() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    @Test
    public void testPrintln_3D_DoubleArray() {
        double[][][] arr = { { { 1.0, 2.0 } } };
        String result = Arrays.println(arr);
        assertNotNull(result);
    }

    // ============================================
    // Tests for reshape methods - boolean arrays
    // ============================================

    @Test
    public void testReshape_Boolean_2D() {
        boolean[] a = { true, false, true, false, true, false };
        boolean[][] result = Arrays.reshape(a, 3);

        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertTrue(result[0][0]);
        assertFalse(result[0][1]);
        assertTrue(result[0][2]);
        assertFalse(result[1][0]);
    }

    @Test
    public void testReshape_Boolean_2D_InvalidCols() {
        boolean[] a = { true, false, true, false, true };
        boolean[][] b = Arrays.reshape(a, 3);

        assertArrayEquals(new boolean[][] { { true, false, true }, { false, true } }, b);

    }

    @Test
    public void testReshape_Boolean_3D() {
        boolean[] a = new boolean[12];
        for (int i = 0; i < 12; i++) {
            a[i] = (i % 2 == 0);
        }
        boolean[][][] result = Arrays.reshape(a, 2, 3);

        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(3, result[0][0].length);
    }

    // ============================================
    // Tests for flatten methods - boolean arrays
    // ============================================

    @Test
    public void testFlatten_Boolean_2D() {
        boolean[][] a = { { true, false }, { true, false } };
        boolean[] result = Arrays.flatten(a);

        assertEquals(4, result.length);
        assertTrue(result[0]);
        assertFalse(result[1]);
        assertTrue(result[2]);
        assertFalse(result[3]);
    }

    @Test
    public void testFlatten_Boolean_3D() {
        boolean[][][] a = { { { true, false }, { true } }, { { false } } };
        boolean[] result = Arrays.flatten(a);

        assertEquals(4, result.length);
        assertTrue(result[0]);
        assertFalse(result[1]);
        assertTrue(result[2]);
        assertFalse(result[3]);
    }

    // ============================================
    // Tests for reshape methods - char arrays
    // ============================================

    @Test
    public void testReshape_Char_2D() {
        char[] a = { 'a', 'b', 'c', 'd', 'e', 'f' };
        char[][] result = Arrays.reshape(a, 3);

        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals('a', result[0][0]);
        assertEquals('d', result[1][0]);
    }

    @Test
    public void testReshape_Char_3D() {
        char[] a = "abcdefghijkl".toCharArray();
        char[][][] result = Arrays.reshape(a, 2, 3);

        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(3, result[0][0].length);
        assertEquals('a', result[0][0][0]);
    }

    // ============================================
    // Tests for flatten methods - char arrays
    // ============================================

    @Test
    public void testFlatten_Char_2D() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        char[] result = Arrays.flatten(a);

        assertArrayEquals("abcd".toCharArray(), result);
    }

    @Test
    public void testFlatten_Char_3D() {
        char[][][] a = { { { 'a', 'b' }, { 'c' } }, { { 'd' } } };
        char[] result = Arrays.flatten(a);

        assertArrayEquals("abcd".toCharArray(), result);
    }

    // ============================================
    // Tests for reshape methods - byte arrays
    // ============================================

    @Test
    public void testReshape_Byte_2D() {
        byte[] a = { 1, 2, 3, 4, 5, 6 };
        byte[][] result = Arrays.reshape(a, 3);

        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals(1, result[0][0]);
        assertEquals(4, result[1][0]);
    }

    @Test
    public void testReshape_Byte_3D() {
        byte[] a = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        byte[][][] result = Arrays.reshape(a, 2, 3);

        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(3, result[0][0].length);
    }

    // ============================================
    // Tests for flatten methods - byte arrays
    // ============================================

    @Test
    public void testFlatten_Byte_2D() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        byte[] result = Arrays.flatten(a);

        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, result);
    }

    @Test
    public void testFlatten_Byte_3D() {
        byte[][][] a = { { { 1, 2 }, { 3 } }, { { 4 } } };
        byte[] result = Arrays.flatten(a);

        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, result);
    }

    // ============================================
    // Tests for reshape methods - short arrays
    // ============================================

    @Test
    public void testReshape_Short_2D() {
        short[] a = { 1, 2, 3, 4, 5, 6 };
        short[][] result = Arrays.reshape(a, 3);

        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals(1, result[0][0]);
        assertEquals(4, result[1][0]);
    }

    @Test
    public void testReshape_Short_3D() {
        short[] a = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        short[][][] result = Arrays.reshape(a, 2, 3);

        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(3, result[0][0].length);
    }

    // ============================================
    // Tests for flatten methods - short arrays
    // ============================================

    @Test
    public void testFlatten_Short_2D() {
        short[][] a = { { 1, 2 }, { 3, 4 } };
        short[] result = Arrays.flatten(a);

        assertArrayEquals(new short[] { 1, 2, 3, 4 }, result);
    }

    @Test
    public void testFlatten_Short_3D() {
        short[][][] a = { { { 1, 2 }, { 3 } }, { { 4 } } };
        short[] result = Arrays.flatten(a);

        assertArrayEquals(new short[] { 1, 2, 3, 4 }, result);
    }

    // ============================================
    // Tests for reshape methods - int arrays
    // ============================================

    @Test
    public void testReshape_Int_2D() {
        int[] a = { 1, 2, 3, 4, 5, 6 };
        int[][] result = Arrays.reshape(a, 3);

        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals(1, result[0][0]);
        assertEquals(2, result[0][1]);
        assertEquals(3, result[0][2]);
        assertEquals(4, result[1][0]);
        assertEquals(5, result[1][1]);
        assertEquals(6, result[1][2]);
    }

    @Test
    public void testReshape_Int_2D_SingleRow() {
        int[] a = { 1, 2, 3 };
        int[][] result = Arrays.reshape(a, 3);

        assertEquals(1, result.length);
        assertEquals(3, result[0].length);
        assertArrayEquals(new int[] { 1, 2, 3 }, result[0]);
    }

    @Test
    public void testReshape_Int_3D() {
        int[] a = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        int[][][] result = Arrays.reshape(a, 2, 3);

        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(3, result[0][0].length);
        assertEquals(1, result[0][0][0]);
        assertEquals(12, result[1][1][2]);
    }

    // ============================================
    // Tests for flatten methods - int arrays
    // ============================================

    @Test
    public void testFlatten_Int_2D() {
        int[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        int[] result = Arrays.flatten(a);

        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, result);
    }

    @Test
    public void testFlatten_Int_2D_JaggedArray() {
        int[][] a = { { 1, 2 }, { 3, 4, 5 }, { 6 } };
        int[] result = Arrays.flatten(a);

        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, result);
    }

    @Test
    public void testFlatten_Int_3D() {
        int[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } } };
        int[] result = Arrays.flatten(a);

        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, result);
    }

    // ============================================
    // Tests for reshape methods - long arrays
    // ============================================

    @Test
    public void testReshape_Long_2D() {
        long[] a = { 1L, 2L, 3L, 4L, 5L, 6L };
        long[][] result = Arrays.reshape(a, 3);

        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals(1L, result[0][0]);
        assertEquals(4L, result[1][0]);
    }

    @Test
    public void testReshape_Long_3D() {
        long[] a = { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L };
        long[][][] result = Arrays.reshape(a, 2, 3);

        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(3, result[0][0].length);
    }

    // ============================================
    // Tests for flatten methods - long arrays
    // ============================================

    @Test
    public void testFlatten_Long_2D() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        long[] result = Arrays.flatten(a);

        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L }, result);
    }

    @Test
    public void testFlatten_Long_3D() {
        long[][][] a = { { { 1L, 2L }, { 3L } }, { { 4L } } };
        long[] result = Arrays.flatten(a);

        assertArrayEquals(new long[] { 1L, 2L, 3L, 4L }, result);
    }

    // ============================================
    // Tests for reshape methods - float arrays
    // ============================================

    @Test
    public void testReshape_Float_2D() {
        float[] a = { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f };
        float[][] result = Arrays.reshape(a, 3);

        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals(1.0f, result[0][0], 0.0001f);
        assertEquals(4.0f, result[1][0], 0.0001f);
    }

    @Test
    public void testReshape_Float_3D() {
        float[] a = { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f };
        float[][][] result = Arrays.reshape(a, 2, 3);

        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(3, result[0][0].length);
    }

    // ============================================
    // Tests for flatten methods - float arrays
    // ============================================

    @Test
    public void testFlatten_Float_2D() {
        float[][] a = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        float[] result = Arrays.flatten(a);

        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, result, 0.0001f);
    }

    @Test
    public void testFlatten_Float_3D() {
        float[][][] a = { { { 1.0f, 2.0f }, { 3.0f } }, { { 4.0f } } };
        float[] result = Arrays.flatten(a);

        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, result, 0.0001f);
    }

    // ============================================
    // Tests for reshape methods - double arrays
    // ============================================

    @Test
    public void testReshape_Double_2D() {
        double[] a = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
        double[][] result = Arrays.reshape(a, 3);

        assertEquals(2, result.length);
        assertEquals(3, result[0].length);
        assertEquals(1.0, result[0][0], 0.0001);
        assertEquals(4.0, result[1][0], 0.0001);
    }

    @Test
    public void testReshape_Double_3D() {
        double[] a = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0 };
        double[][][] result = Arrays.reshape(a, 2, 3);

        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals(3, result[0][0].length);
    }

    // ============================================
    // Tests for flatten methods - double arrays
    // ============================================

    @Test
    public void testFlatten_Double_2D() {
        double[][] a = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        double[] result = Arrays.flatten(a);

        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, result, 0.0001);
    }

    @Test
    public void testFlatten_Double_3D() {
        double[][][] a = { { { 1.0, 2.0 }, { 3.0 } }, { { 4.0 } } };
        double[] result = Arrays.flatten(a);

        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, result, 0.0001);
    }

    // ============================================
    // Note: Arrays.java only has reshape/flatten for primitive types,
    // not for Object arrays. Object array tests are omitted.
    // ============================================

    // ============================================
    // Tests for mapToObj methods
    // ============================================

    @Test
    public void testMapToObj_IntArray() {
        int[] a = { 1, 2, 3, 4, 5 };
        String[] result = Arrays.mapToObj(a, i -> "Val:" + i, String.class);

        assertEquals(5, result.length);
        assertEquals("Val:1", result[0]);
        assertEquals("Val:5", result[4]);
    }

    @Test
    public void testMapToObj_LongArray() {
        long[] a = { 1L, 2L, 3L };
        String[] result = Arrays.mapToObj(a, l -> "L" + l, String.class);

        assertEquals(3, result.length);
        assertEquals("L1", result[0]);
        assertEquals("L3", result[2]);
    }

    @Test
    public void testMapToObj_DoubleArray() {
        double[] a = { 1.5, 2.5, 3.5 };
        String[] result = Arrays.mapToObj(a, d -> String.format("%.1f", d), String.class);

        assertEquals(3, result.length);
        assertEquals("1.5", result[0]);
        assertEquals("3.5", result[2]);
    }

    @Test
    public void testMapToObj_BooleanArray() {
        boolean[] a = { true, false, true };
        String[] result = Arrays.mapToObj(a, b -> b ? "T" : "F", String.class);

        assertArrayEquals(new String[] { "T", "F", "T" }, result);
    }

    @Test
    public void testMapToObj_CharArray() {
        char[] a = { 'a', 'b', 'c' };
        String[] result = Arrays.mapToObj(a, c -> String.valueOf(c).toUpperCase(), String.class);

        assertArrayEquals(new String[] { "A", "B", "C" }, result);
    }

    @Test
    public void testMapToObj_ByteArray() {
        byte[] a = { 1, 2, 3 };
        String[] result = Arrays.mapToObj(a, b -> "Byte:" + b, String.class);

        assertArrayEquals(new String[] { "Byte:1", "Byte:2", "Byte:3" }, result);
    }

    @Test
    public void testMapToObj_ShortArray() {
        short[] a = { 10, 20, 30 };
        String[] result = Arrays.mapToObj(a, s -> "S" + s, String.class);

        assertArrayEquals(new String[] { "S10", "S20", "S30" }, result);
    }

    @Test
    public void testMapToObj_FloatArray() {
        float[] a = { 1.5f, 2.5f, 3.5f };
        String[] result = Arrays.mapToObj(a, f -> String.format("%.1f", f), String.class);

        assertArrayEquals(new String[] { "1.5", "2.5", "3.5" }, result);
    }

    // ============================================
    // Tests for mapToObj 2D arrays
    // ============================================

    @Test
    public void testMapToObj_Boolean2D() {
        boolean[][] a = { { true, false }, { true, false } };
        String[][] result = Arrays.mapToObj(a, b -> b ? "TRUE" : "FALSE", String.class);

        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals("TRUE", result[0][0]);
        assertEquals("FALSE", result[0][1]);
        assertEquals("TRUE", result[1][0]);
        assertEquals("FALSE", result[1][1]);
    }

    @Test
    public void testMapToObj_Char2D() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        String[][] result = Arrays.mapToObj(a, c -> String.valueOf(c).toUpperCase(), String.class);

        assertEquals(2, result.length);
        assertEquals("A", result[0][0]);
        assertEquals("B", result[0][1]);
        assertEquals("C", result[1][0]);
        assertEquals("D", result[1][1]);
    }

    @Test
    public void testMapToObj_Byte2D() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        String[][] result = Arrays.mapToObj(a, b -> "B" + b, String.class);

        assertEquals(2, result.length);
        assertEquals("B1", result[0][0]);
        assertEquals("B2", result[0][1]);
        assertEquals("B3", result[1][0]);
        assertEquals("B4", result[1][1]);
    }

    @Test
    public void testMapToObj_Short2D() {
        short[][] a = { { 10, 20 }, { 30, 40 } };
        String[][] result = Arrays.mapToObj(a, s -> "Short:" + s, String.class);

        assertEquals(2, result.length);
        assertEquals("Short:10", result[0][0]);
        assertEquals("Short:20", result[0][1]);
        assertEquals("Short:30", result[1][0]);
        assertEquals("Short:40", result[1][1]);
    }

    @Test
    public void testMapToObj_Float2D() {
        float[][] a = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
        String[][] result = Arrays.mapToObj(a, f -> String.format("%.1f", f), String.class);

        assertEquals(2, result.length);
        assertEquals("1.5", result[0][0]);
        assertEquals("2.5", result[0][1]);
        assertEquals("3.5", result[1][0]);
        assertEquals("4.5", result[1][1]);
    }

    @Test
    public void testMapToObj_Long2D() {
        long[][] a = { { 100L, 200L }, { 300L, 400L } };
        String[][] result = Arrays.mapToObj(a, l -> "L" + l, String.class);

        assertEquals(2, result.length);
        assertEquals("L100", result[0][0]);
        assertEquals("L200", result[0][1]);
        assertEquals("L300", result[1][0]);
        assertEquals("L400", result[1][1]);
    }

    @Test
    public void testMapToObj_Double2D() {
        double[][] a = { { 1.1, 2.2 }, { 3.3, 4.4 } };
        String[][] result = Arrays.mapToObj(a, d -> String.format("%.1f", d), String.class);

        assertEquals(2, result.length);
        assertEquals("1.1", result[0][0]);
        assertEquals("2.2", result[0][1]);
        assertEquals("3.3", result[1][0]);
        assertEquals("4.4", result[1][1]);
    }

    // ============================================
    // Tests for mapToObj 3D arrays
    // ============================================

    @Test
    public void testMapToObj_Boolean3D() {
        boolean[][][] a = { { { true, false }, { true } } };
        String[][][] result = Arrays.mapToObj(a, b -> b ? "T" : "F", String.class);

        assertEquals(1, result.length);
        assertEquals(2, result[0].length);
        assertEquals("T", result[0][0][0]);
        assertEquals("F", result[0][0][1]);
        assertEquals("T", result[0][1][0]);
    }

    @Test
    public void testMapToObj_Char3D() {
        char[][][] a = { { { 'a', 'b' }, { 'c' } } };
        String[][][] result = Arrays.mapToObj(a, c -> String.valueOf(c).toUpperCase(), String.class);

        assertEquals(1, result.length);
        assertEquals("A", result[0][0][0]);
        assertEquals("B", result[0][0][1]);
        assertEquals("C", result[0][1][0]);
    }

    @Test
    public void testMapToObj_Byte3D() {
        byte[][][] a = { { { 1, 2 }, { 3 } } };
        String[][][] result = Arrays.mapToObj(a, b -> "Byte" + b, String.class);

        assertEquals(1, result.length);
        assertEquals("Byte1", result[0][0][0]);
        assertEquals("Byte2", result[0][0][1]);
        assertEquals("Byte3", result[0][1][0]);
    }

    @Test
    public void testMapToObj_Short3D() {
        short[][][] a = { { { 10, 20 }, { 30 } } };
        String[][][] result = Arrays.mapToObj(a, s -> "S" + s, String.class);

        assertEquals(1, result.length);
        assertEquals("S10", result[0][0][0]);
        assertEquals("S20", result[0][0][1]);
        assertEquals("S30", result[0][1][0]);
    }

    @Test
    public void testMapToObj_Float3D() {
        float[][][] a = { { { 1.5f, 2.5f }, { 3.5f } } };
        String[][][] result = Arrays.mapToObj(a, f -> String.format("%.1f", f), String.class);

        assertEquals(1, result.length);
        assertEquals("1.5", result[0][0][0]);
        assertEquals("2.5", result[0][0][1]);
        assertEquals("3.5", result[0][1][0]);
    }

    @Test
    public void testMapToObj_Long3D() {
        long[][][] a = { { { 100L, 200L }, { 300L } } };
        String[][][] result = Arrays.mapToObj(a, l -> "Long" + l, String.class);

        assertEquals(1, result.length);
        assertEquals("Long100", result[0][0][0]);
        assertEquals("Long200", result[0][0][1]);
        assertEquals("Long300", result[0][1][0]);
    }

    @Test
    public void testMapToObj_Double3D() {
        double[][][] a = { { { 1.1, 2.2 }, { 3.3 } } };
        String[][][] result = Arrays.mapToObj(a, d -> String.format("%.1f", d), String.class);

        assertEquals(1, result.length);
        assertEquals("1.1", result[0][0][0]);
        assertEquals("2.2", result[0][0][1]);
        assertEquals("3.3", result[0][1][0]);
    }

    // ============================================
    // Tests for type conversion methods
    // ============================================

    @Test
    public void testMapToLong_IntArray() {
        int[] a = { 1, 2, 3, 4, 5 };
        long[] result = Arrays.mapToLong(a, i -> (long) i * 2);

        assertArrayEquals(new long[] { 2L, 4L, 6L, 8L, 10L }, result);
    }

    @Test
    public void testMapToDouble_IntArray() {
        int[] a = { 1, 2, 3 };
        double[] result = Arrays.mapToDouble(a, i -> i * 1.5);

        assertArrayEquals(new double[] { 1.5, 3.0, 4.5 }, result, 0.0001);
    }

    @Test
    public void testMapToInt_LongArray() {
        long[] a = { 100L, 200L, 300L };
        int[] result = Arrays.mapToInt(a, l -> (int) (l / 10));

        assertArrayEquals(new int[] { 10, 20, 30 }, result);
    }

    @Test
    public void testMapToDouble_LongArray() {
        long[] a = { 10L, 20L, 30L };
        double[] result = Arrays.mapToDouble(a, l -> l * 0.5);

        assertArrayEquals(new double[] { 5.0, 10.0, 15.0 }, result, 0.0001);
    }

    @Test
    public void testMapToInt_DoubleArray() {
        double[] a = { 1.9, 2.9, 3.9 };
        int[] result = Arrays.mapToInt(a, d -> (int) d);

        assertArrayEquals(new int[] { 1, 2, 3 }, result);
    }

    @Test
    public void testMapToLong_DoubleArray() {
        double[] a = { 1.5, 2.5, 3.5 };
        long[] result = Arrays.mapToLong(a, d -> (long) (d * 10));

        assertArrayEquals(new long[] { 15L, 25L, 35L }, result);
    }

    // ============================================
    // Tests for updateAll methods
    // ============================================

    @Test
    public void testUpdateAll_BooleanArray() {
        boolean[] a = { true, false, true, false };
        Arrays.updateAll(a, b -> !b);

        assertArrayEquals(new boolean[] { false, true, false, true }, a);
    }

    @Test
    public void testUpdateAll_CharArray() {
        char[] a = { 'a', 'b', 'c' };
        Arrays.updateAll(a, c -> Character.toUpperCase(c));

        assertArrayEquals(new char[] { 'A', 'B', 'C' }, a);
    }

    @Test
    public void testUpdateAll_IntArray() {
        int[] a = { 1, 2, 3, 4, 5 };
        Arrays.updateAll(a, i -> i * 2);

        assertArrayEquals(new int[] { 2, 4, 6, 8, 10 }, a);
    }

    @Test
    public void testUpdateAll_LongArray() {
        long[] a = { 10L, 20L, 30L };
        Arrays.updateAll(a, l -> l / 2);

        assertArrayEquals(new long[] { 5L, 10L, 15L }, a);
    }

    @Test
    public void testUpdateAll_DoubleArray() {
        double[] a = { 1.0, 2.0, 3.0 };
        Arrays.updateAll(a, d -> d * 1.5);

        assertArrayEquals(new double[] { 1.5, 3.0, 4.5 }, a, 0.0001);
    }

    // ============================================
    // Tests for replaceIf methods
    // ============================================

    @Test
    public void testReplaceIf_BooleanArray() {
        boolean[] a = { true, false, true, false, true };
        Arrays.replaceIf(a, b -> b, false);

        assertArrayEquals(new boolean[] { false, false, false, false, false }, a);
    }

    @Test
    public void testReplaceIf_IntArray() {
        int[] a = { 1, 2, 3, 4, 5 };
        Arrays.replaceIf(a, i -> i % 2 == 0, 0);

        assertArrayEquals(new int[] { 1, 0, 3, 0, 5 }, a);
    }

    @Test
    public void testReplaceIf_DoubleArray() {
        double[] a = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        Arrays.replaceIf(a, d -> d > 3.0, 0.0);

        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 0.0, 0.0 }, a, 0.0001);
    }

    // Note: replaceIf for Object arrays is not available in Arrays.java

    // ============================================
    // Tests for minSubArrayLen and maxSubArrayLen
    // ============================================

    @Test
    public void testMinSubArrayLen_Boolean2D() {
        boolean[][] a = { { true, false }, { true }, { true, false, true } };
        int result = Arrays.minSubArrayLen(a);

        assertEquals(1, result);
    }

    @Test
    public void testMaxSubArrayLen_Boolean2D() {
        boolean[][] a = { { true, false }, { true }, { true, false, true } };
        int result = Arrays.maxSubArrayLen(a);

        assertEquals(3, result);
    }

    @Test
    public void testMinSubArrayLen_Char2D() {
        char[][] a = { { 'a', 'b' }, { 'c' }, { 'd', 'e', 'f' } };
        int result = Arrays.minSubArrayLen(a);

        assertEquals(1, result);
    }

    @Test
    public void testMaxSubArrayLen_Char2D() {
        char[][] a = { { 'a', 'b' }, { 'c' }, { 'd', 'e', 'f' } };
        int result = Arrays.maxSubArrayLen(a);

        assertEquals(3, result);
    }

    @Test
    public void testMinSubArrayLen_Int2D() {
        int[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
        int result = Arrays.minSubArrayLen(a);

        assertEquals(1, result);
    }

    @Test
    public void testMaxSubArrayLen_Int2D() {
        int[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
        int result = Arrays.maxSubArrayLen(a);

        assertEquals(3, result);
    }

    // ============================================
    // Tests for flatOp methods
    // ============================================

    @Test
    public void testFlatOp_Boolean2D() {
        boolean[][] a = { { true, false }, { true, false } };
        List<Boolean> result = new ArrayList<>();

        Arrays.flatOp(a, subArray -> {
            for (boolean val : subArray) {
                result.add(val);
            }
        });

        assertEquals(4, result.size());
        assertTrue(result.get(0));
        assertFalse(result.get(1));
    }

    @Test
    public void testFlatOp_Int2D() {
        int[][] a = { { 1, 2 }, { 3, 4 } };
        List<Integer> result = new ArrayList<>();

        Arrays.flatOp(a, subArray -> {
            for (int val : subArray) {
                result.add(val);
            }
        });

        assertEquals(4, result.size());
        assertEquals(1, result.get(0));
        assertEquals(4, result.get(3));
    }

    @Test
    public void testFlatOp_Double2D() {
        double[][] a = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        List<Double> result = new ArrayList<>();

        Arrays.flatOp(a, subArray -> {
            for (double val : subArray) {
                result.add(val);
            }
        });

        assertEquals(4, result.size());
        assertEquals(1.0, result.get(0), 0.0001);
    }

    @Test
    public void testFlatOp_Boolean3D() {
        boolean[][][] a = { { { true, false } }, { { true } } };
        List<Boolean> result = new ArrayList<>();

        Arrays.flatOp(a, subArray -> {
            for (boolean val : subArray) {
                result.add(val);
            }
        });

        assertEquals(3, result.size());
    }

    @Test
    public void testFlatOp_Int3D() {
        int[][][] a = { { { 1, 2 } }, { { 3 } } };
        List<Integer> result = new ArrayList<>();

        Arrays.flatOp(a, subArray -> {
            for (int val : subArray) {
                result.add(val);
            }
        });

        assertEquals(3, result.size());
    }

    // ============================================
    // Tests for 2D array mapping
    // ============================================

    @Test
    public void testMapToObj_Int2D() {
        int[][] a = { { 1, 2 }, { 3, 4 } };
        String[][] result = Arrays.mapToObj(a, i -> "V" + i, String.class);

        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        assertEquals("V1", result[0][0]);
        assertEquals("V4", result[1][1]);
    }

    @Test
    public void testMapToLong_Int2D() {
        int[][] a = { { 1, 2 }, { 3, 4 } };
        long[][] result = Arrays.mapToLong(a, i -> (long) i * 10);

        assertEquals(2, result.length);
        assertEquals(10L, result[0][0]);
        assertEquals(40L, result[1][1]);
    }

    @Test
    public void testMapToDouble_Int2D() {
        int[][] a = { { 1, 2 }, { 3, 4 } };
        double[][] result = Arrays.mapToDouble(a, i -> i * 0.5);

        assertEquals(2, result.length);
        assertEquals(0.5, result[0][0], 0.0001);
        assertEquals(2.0, result[1][1], 0.0001);
    }

    // ============================================
    // Tests for 3D array mapping
    // ============================================

    @Test
    public void testMapToObj_Int3D() {
        int[][][] a = { { { 1, 2 } } };
        String[][][] result = Arrays.mapToObj(a, i -> "X" + i, String.class);

        assertEquals(1, result.length);
        assertEquals(1, result[0].length);
        assertEquals("X1", result[0][0][0]);
    }

    @Test
    public void testMapToLong_Int3D() {
        int[][][] a = { { { 1, 2 } } };
        long[][][] result = Arrays.mapToLong(a, i -> (long) i * 100);

        assertEquals(100L, result[0][0][0]);
    }

    @Test
    public void testMapToDouble_Int3D() {
        int[][][] a = { { { 10, 20 } } };
        double[][][] result = Arrays.mapToDouble(a, i -> i * 0.1);

        assertEquals(1.0, result[0][0][0], 0.0001);
        assertEquals(2.0, result[0][0][1], 0.0001);
    }

    // ============================================
    // Tests for 2D updateAll methods
    // ============================================

    @Test
    public void testUpdateAll_Boolean2D() {
        boolean[][] a = { { true, false }, { true, false } };
        Arrays.updateAll(a, b -> !b);

        assertFalse(a[0][0]);
        assertTrue(a[0][1]);
        assertFalse(a[1][0]);
        assertTrue(a[1][1]);
    }

    @Test
    public void testUpdateAll_Int2D() {
        int[][] a = { { 1, 2 }, { 3, 4 } };
        Arrays.updateAll(a, i -> i * 10);

        assertEquals(10, a[0][0]);
        assertEquals(20, a[0][1]);
        assertEquals(30, a[1][0]);
        assertEquals(40, a[1][1]);
    }

    // ============================================
    // Tests for 3D updateAll methods
    // ============================================

    @Test
    public void testUpdateAll_Boolean3D() {
        boolean[][][] a = { { { true, false } } };
        Arrays.updateAll(a, b -> !b);

        assertFalse(a[0][0][0]);
        assertTrue(a[0][0][1]);
    }

    @Test
    public void testUpdateAll_Int3D() {
        int[][][] a = { { { 1, 2 } } };
        Arrays.updateAll(a, i -> i + 10);

        assertEquals(11, a[0][0][0]);
        assertEquals(12, a[0][0][1]);
    }

    // ============================================
    // Tests for 2D replaceIf methods
    // ============================================

    @Test
    public void testReplaceIf_Boolean2D() {
        boolean[][] a = { { true, false }, { true, false } };
        Arrays.replaceIf(a, b -> b, false);

        assertFalse(a[0][0]);
        assertFalse(a[0][1]);
        assertFalse(a[1][0]);
        assertFalse(a[1][1]);
    }

    @Test
    public void testReplaceIf_Int2D() {
        int[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        Arrays.replaceIf(a, i -> i % 2 == 0, 0);

        assertEquals(1, a[0][0]);
        assertEquals(0, a[0][1]);
        assertEquals(3, a[0][2]);
        assertEquals(0, a[1][0]);
    }

    // ============================================
    // Tests for 3D replaceIf methods
    // ============================================

    @Test
    public void testReplaceIf_Boolean3D() {
        boolean[][][] a = { { { true, false, true } } };
        Arrays.replaceIf(a, b -> b, false);

        assertFalse(a[0][0][0]);
        assertFalse(a[0][0][1]);
        assertFalse(a[0][0][2]);
    }

    @Test
    public void testReplaceIf_Int3D() {
        int[][][] a = { { { 1, 2, 3, 4, 5 } } };
        Arrays.replaceIf(a, i -> i > 3, 99);

        assertEquals(1, a[0][0][0]);
        assertEquals(2, a[0][0][1]);
        assertEquals(3, a[0][0][2]);
        assertEquals(99, a[0][0][3]);
        assertEquals(99, a[0][0][4]);
    }

    // ============================================
    // Edge case tests
    // ============================================

    @Test
    public void testReshape_EmptyArray() {
        int[] a = {};
        int[][] b = Arrays.reshape(a, 1);
        assertEquals(0, b.length);

    }

    @Test
    public void testFlatten_EmptyArray() {
        int[][] a = {};
        int[] result = Arrays.flatten(a);

        assertEquals(0, result.length);
    }

    @Test
    public void testMapToObj_EmptyArray() {
        int[] a = {};
        String[] result = Arrays.mapToObj(a, i -> "X", String.class);

        assertEquals(0, result.length);
    }

    @Test
    public void testUpdateAll_EmptyArray() {
        int[] a = {};
        Arrays.updateAll(a, i -> i * 2);

        assertEquals(0, a.length);
    }

    @Test
    public void testReplaceIf_NoMatches() {
        int[] a = { 1, 3, 5, 7 };
        Arrays.replaceIf(a, i -> i % 2 == 0, 0);

        assertArrayEquals(new int[] { 1, 3, 5, 7 }, a);
    }

    @Test
    public void testReplaceIf_AllMatches() {
        int[] a = { 2, 4, 6, 8 };
        Arrays.replaceIf(a, i -> i % 2 == 0, 0);

        assertArrayEquals(new int[] { 0, 0, 0, 0 }, a);
    }

    // ============================================
    // Tests for null handling in mapToObj methods
    // ============================================

    @Test
    public void testMapToObj_NullBooleanArray() {
        boolean[] a = null;
        String[] result = Arrays.mapToObj(a, b -> "X", String.class);
        assertEquals(null, result);
    }

    @Test
    public void testMapToObj_NullCharArray() {
        char[] a = null;
        String[] result = Arrays.mapToObj(a, c -> "X", String.class);
        assertEquals(null, result);
    }

    @Test
    public void testMapToObj_NullByteArray() {
        byte[] a = null;
        String[] result = Arrays.mapToObj(a, b -> "X", String.class);
        assertEquals(null, result);
    }

    @Test
    public void testMapToObj_NullShortArray() {
        short[] a = null;
        String[] result = Arrays.mapToObj(a, s -> "X", String.class);
        assertEquals(null, result);
    }

    @Test
    public void testMapToObj_NullIntArray() {
        int[] a = null;
        String[] result = Arrays.mapToObj(a, i -> "X", String.class);
        assertEquals(null, result);
    }

    @Test
    public void testMapToObj_NullLongArray() {
        long[] a = null;
        String[] result = Arrays.mapToObj(a, l -> "X", String.class);
        assertEquals(null, result);
    }

    @Test
    public void testMapToObj_NullFloatArray() {
        float[] a = null;
        String[] result = Arrays.mapToObj(a, f -> "X", String.class);
        assertEquals(null, result);
    }

    @Test
    public void testMapToObj_NullDoubleArray() {
        double[] a = null;
        String[] result = Arrays.mapToObj(a, d -> "X", String.class);
        assertEquals(null, result);
    }

    @Test
    public void testMapToObj_Null2DIntArray() {
        int[][] a = null;
        String[][] result = Arrays.mapToObj(a, i -> "X", String.class);
        assertEquals(null, result);
    }

    @Test
    public void testMapToObj_Null3DIntArray() {
        int[][][] a = null;
        String[][][] result = Arrays.mapToObj(a, i -> "X", String.class);
        assertEquals(null, result);
    }

    // ============================================
    // Additional edge case tests for updateAll with byte, short, float
    // ============================================

    @Test
    public void testUpdateAll_ByteArray() {
        byte[] a = { 1, 2, 3, 4 };
        Arrays.updateAll(a, b -> (byte) (b * 2));

        assertArrayEquals(new byte[] { 2, 4, 6, 8 }, a);
    }

    @Test
    public void testUpdateAll_ShortArray() {
        short[] a = { 10, 20, 30 };
        Arrays.updateAll(a, s -> (short) (s / 2));

        assertArrayEquals(new short[] { 5, 10, 15 }, a);
    }

    @Test
    public void testUpdateAll_FloatArray() {
        float[] a = { 1.0f, 2.0f, 3.0f };
        Arrays.updateAll(a, f -> f * 2.0f);

        assertArrayEquals(new float[] { 2.0f, 4.0f, 6.0f }, a, 0.0001f);
    }

    // ============================================
    // Additional edge case tests for replaceIf
    // ============================================

    @Test
    public void testReplaceIf_CharArray() {
        char[] a = { 'a', 'b', 'c', 'd' };
        Arrays.replaceIf(a, c -> c == 'b' || c == 'd', 'x');

        assertArrayEquals(new char[] { 'a', 'x', 'c', 'x' }, a);
    }

    @Test
    public void testReplaceIf_ByteArray() {
        byte[] a = { 1, 2, 3, 4, 5 };
        Arrays.replaceIf(a, b -> b > 3, (byte) 0);

        assertArrayEquals(new byte[] { 1, 2, 3, 0, 0 }, a);
    }

    @Test
    public void testReplaceIf_ShortArray() {
        short[] a = { 10, 20, 30, 40 };
        Arrays.replaceIf(a, s -> s >= 30, (short) 0);

        assertArrayEquals(new short[] { 10, 20, 0, 0 }, a);
    }

    @Test
    public void testReplaceIf_LongArray() {
        long[] a = { 100L, 200L, 300L };
        Arrays.replaceIf(a, l -> l == 200L, 0L);

        assertArrayEquals(new long[] { 100L, 0L, 300L }, a);
    }

    @Test
    public void testReplaceIf_FloatArray() {
        float[] a = { 1.0f, 2.0f, 3.0f, 4.0f };
        Arrays.replaceIf(a, f -> f > 2.5f, 0.0f);

        assertArrayEquals(new float[] { 1.0f, 2.0f, 0.0f, 0.0f }, a, 0.0001f);
    }

    // ============================================
    // Additional 2D/3D edge case tests
    // ============================================

    @Test
    public void testUpdateAll_Byte2D() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        Arrays.updateAll(a, b -> (byte) (b + 10));

        assertEquals(11, a[0][0]);
        assertEquals(12, a[0][1]);
        assertEquals(13, a[1][0]);
        assertEquals(14, a[1][1]);
    }

    @Test
    public void testUpdateAll_Short2D() {
        short[][] a = { { 10, 20 }, { 30, 40 } };
        Arrays.updateAll(a, s -> (short) (s / 10));

        assertEquals(1, a[0][0]);
        assertEquals(2, a[0][1]);
        assertEquals(3, a[1][0]);
        assertEquals(4, a[1][1]);
    }

    @Test
    public void testUpdateAll_Float2D() {
        float[][] a = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        Arrays.updateAll(a, f -> f * 10.0f);

        assertEquals(10.0f, a[0][0], 0.0001f);
        assertEquals(20.0f, a[0][1], 0.0001f);
        assertEquals(30.0f, a[1][0], 0.0001f);
        assertEquals(40.0f, a[1][1], 0.0001f);
    }

    @Test
    public void testUpdateAll_Long2D() {
        long[][] a = { { 100L, 200L }, { 300L, 400L } };
        Arrays.updateAll(a, l -> l / 100);

        assertEquals(1L, a[0][0]);
        assertEquals(2L, a[0][1]);
        assertEquals(3L, a[1][0]);
        assertEquals(4L, a[1][1]);
    }

    @Test
    public void testUpdateAll_Double2D() {
        double[][] a = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        Arrays.updateAll(a, d -> d * 5.0);

        assertEquals(5.0, a[0][0], 0.0001);
        assertEquals(10.0, a[0][1], 0.0001);
        assertEquals(15.0, a[1][0], 0.0001);
        assertEquals(20.0, a[1][1], 0.0001);
    }

    @Test
    public void testUpdateAll_Char2D() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        Arrays.updateAll(a, c -> Character.toUpperCase(c));

        assertEquals('A', a[0][0]);
        assertEquals('B', a[0][1]);
        assertEquals('C', a[1][0]);
        assertEquals('D', a[1][1]);
    }

    @Test
    public void testUpdateAll_Byte3D() {
        byte[][][] a = { { { 1, 2 } } };
        Arrays.updateAll(a, b -> (byte) (b * 3));

        assertEquals(3, a[0][0][0]);
        assertEquals(6, a[0][0][1]);
    }

    @Test
    public void testUpdateAll_Short3D() {
        short[][][] a = { { { 10, 20 } } };
        Arrays.updateAll(a, s -> (short) (s + 5));

        assertEquals(15, a[0][0][0]);
        assertEquals(25, a[0][0][1]);
    }

    @Test
    public void testUpdateAll_Float3D() {
        float[][][] a = { { { 1.0f, 2.0f } } };
        Arrays.updateAll(a, f -> f * 2.5f);

        assertEquals(2.5f, a[0][0][0], 0.0001f);
        assertEquals(5.0f, a[0][0][1], 0.0001f);
    }

    @Test
    public void testUpdateAll_Long3D() {
        long[][][] a = { { { 100L, 200L } } };
        Arrays.updateAll(a, l -> l + 50L);

        assertEquals(150L, a[0][0][0]);
        assertEquals(250L, a[0][0][1]);
    }

    @Test
    public void testUpdateAll_Double3D() {
        double[][][] a = { { { 1.0, 2.0 } } };
        Arrays.updateAll(a, d -> d * 3.0);

        assertEquals(3.0, a[0][0][0], 0.0001);
        assertEquals(6.0, a[0][0][1], 0.0001);
    }

    @Test
    public void testUpdateAll_Char3D() {
        char[][][] a = { { { 'x', 'y' } } };
        Arrays.updateAll(a, c -> Character.toUpperCase(c));

        assertEquals('X', a[0][0][0]);
        assertEquals('Y', a[0][0][1]);
    }

    // ============================================
    // Additional 2D/3D replaceIf tests
    // ============================================

    @Test
    public void testReplaceIf_Byte2D() {
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
    public void testReplaceIf_Short2D() {
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
    public void testReplaceIf_Long2D() {
        long[][] a = { { 100L, 200L }, { 300L, 400L } };
        Arrays.replaceIf(a, l -> l >= 300L, 0L);

        assertEquals(100L, a[0][0]);
        assertEquals(200L, a[0][1]);
        assertEquals(0L, a[1][0]);
        assertEquals(0L, a[1][1]);
    }

    @Test
    public void testReplaceIf_Float2D() {
        float[][] a = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        Arrays.replaceIf(a, f -> f > 2.5f, 0.0f);

        assertEquals(1.0f, a[0][0], 0.0001f);
        assertEquals(2.0f, a[0][1], 0.0001f);
        assertEquals(0.0f, a[1][0], 0.0001f);
        assertEquals(0.0f, a[1][1], 0.0001f);
    }

    @Test
    public void testReplaceIf_Double2D() {
        double[][] a = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        Arrays.replaceIf(a, d -> d < 3.0, 0.0);

        assertEquals(0.0, a[0][0], 0.0001);
        assertEquals(0.0, a[0][1], 0.0001);
        assertEquals(3.0, a[0][2], 0.0001);
        assertEquals(4.0, a[1][0], 0.0001);
    }

    @Test
    public void testReplaceIf_Char2D() {
        char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
        Arrays.replaceIf(a, c -> c == 'b' || c == 'd', 'x');

        assertEquals('a', a[0][0]);
        assertEquals('x', a[0][1]);
        assertEquals('c', a[1][0]);
        assertEquals('x', a[1][1]);
    }

    @Test
    public void testReplaceIf_Byte3D() {
        byte[][][] a = { { { 1, 2, 3, 4, 5 } } };
        Arrays.replaceIf(a, b -> b > 3, (byte) 9);

        assertEquals(1, a[0][0][0]);
        assertEquals(2, a[0][0][1]);
        assertEquals(3, a[0][0][2]);
        assertEquals(9, a[0][0][3]);
        assertEquals(9, a[0][0][4]);
    }

    @Test
    public void testReplaceIf_Short3D() {
        short[][][] a = { { { 10, 20, 30, 40 } } };
        Arrays.replaceIf(a, s -> s <= 20, (short) 0);

        assertEquals(0, a[0][0][0]);
        assertEquals(0, a[0][0][1]);
        assertEquals(30, a[0][0][2]);
        assertEquals(40, a[0][0][3]);
    }

    @Test
    public void testReplaceIf_Long3D() {
        long[][][] a = { { { 100L, 200L, 300L } } };
        Arrays.replaceIf(a, l -> l == 200L, 999L);

        assertEquals(100L, a[0][0][0]);
        assertEquals(999L, a[0][0][1]);
        assertEquals(300L, a[0][0][2]);
    }

    @Test
    public void testReplaceIf_Float3D() {
        float[][][] a = { { { 1.0f, 2.0f, 3.0f } } };
        Arrays.replaceIf(a, f -> f >= 2.0f, 0.0f);

        assertEquals(1.0f, a[0][0][0], 0.0001f);
        assertEquals(0.0f, a[0][0][1], 0.0001f);
        assertEquals(0.0f, a[0][0][2], 0.0001f);
    }

    @Test
    public void testReplaceIf_Double3D() {
        double[][][] a = { { { 1.1, 2.2, 3.3, 4.4 } } };
        Arrays.replaceIf(a, d -> d > 3.0, 0.0);

        assertEquals(1.1, a[0][0][0], 0.0001);
        assertEquals(2.2, a[0][0][1], 0.0001);
        assertEquals(0.0, a[0][0][2], 0.0001);
        assertEquals(0.0, a[0][0][3], 0.0001);
    }

    @Test
    public void testReplaceIf_Char3D() {
        char[][][] a = { { { 'a', 'b', 'c' } } };
        Arrays.replaceIf(a, c -> c == 'b', 'z');

        assertEquals('a', a[0][0][0]);
        assertEquals('z', a[0][0][1]);
        assertEquals('c', a[0][0][2]);
    }
}
