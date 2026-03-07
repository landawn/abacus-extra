package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests that verify the code examples in Arrays.java Javadoc comments.
 */
public class JavadocExampleArraysTest {

    // ========================
    // Class-level Javadoc examples
    // ========================

    @Test
    public void testClassJavadoc_flattenIntMatrix() {
        // Line 101-102: int[][] matrix = {{1, 2, 3}, {4, 5, 6}}; => flatten => {1, 2, 3, 4, 5, 6}
        int[][] matrix = { { 1, 2, 3 }, { 4, 5, 6 } };
        int[] flattened = Arrays.flatten(matrix);
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, flattened);
    }

    @Test
    public void testClassJavadoc_toDouble() {
        // Line 105-106: int[] integers = {1, 2, 3, 4}; => toDouble => {1.0, 2.0, 3.0, 4.0}
        int[] integers = { 1, 2, 3, 4 };
        double[] doubles = Arrays.toDouble(integers);
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, doubles);
    }

    @Test
    public void testClassJavadoc_toFloat() {
        // Line 107: int[] integers = {1, 2, 3, 4}; => toFloat => {1.0f, 2.0f, 3.0f, 4.0f}
        int[] integers = { 1, 2, 3, 4 };
        float[] floats = Arrays.toFloat(integers);
        assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, floats);
    }

    @Test
    public void testClassJavadoc_reshapeInt2D() {
        // Line 114-115: reshape({1,2,3,4,5,6}, 2) => {{1,2},{3,4},{5,6}}
        int[] linear = { 1, 2, 3, 4, 5, 6 };
        int[][] reshaped2D = Arrays.reshape(linear, 2);
        assertArrayEquals(new int[] { 1, 2 }, reshaped2D[0]);
        assertArrayEquals(new int[] { 3, 4 }, reshaped2D[1]);
        assertArrayEquals(new int[] { 5, 6 }, reshaped2D[2]);
    }

    @Test
    public void testClassJavadoc_reshapeInt3D() {
        // Line 118-119: reshape({1,2,3,4,5,6,7,8}, 2, 2) => {{{1,2},{3,4}},{{5,6},{7,8}}}
        int[] arr = { 1, 2, 3, 4, 5, 6, 7, 8 };
        int[][][] reshaped3D = Arrays.reshape(arr, 2, 2);
        assertArrayEquals(new int[] { 1, 2 }, reshaped3D[0][0]);
        assertArrayEquals(new int[] { 3, 4 }, reshaped3D[0][1]);
        assertArrayEquals(new int[] { 5, 6 }, reshaped3D[1][0]);
        assertArrayEquals(new int[] { 7, 8 }, reshaped3D[1][1]);
    }

    @Test
    public void testClassJavadoc_updateAll() {
        // Line 125-128: updateAll({1,2,3,4,5}, x -> x * x) => {1,4,9,16,25}
        int[] numbers = { 1, 2, 3, 4, 5 };
        Arrays.updateAll(numbers, x -> x * x);
        assertArrayEquals(new int[] { 1, 4, 9, 16, 25 }, numbers);
    }

    @Test
    public void testClassJavadoc_zipIntBinary() {
        // Line 133-136: zip({1,2,3}, {4,5,6}, (x,y) -> x + y*2) => {9,12,15}
        int[] a = { 1, 2, 3 };
        int[] b = { 4, 5, 6 };
        int[] zipped = Arrays.zip(a, b, (x, y) -> x + y * 2);
        assertArrayEquals(new int[] { 9, 12, 15 }, zipped);
    }

    @Test
    public void testClassJavadoc_totalCountOfElements3D() {
        // Line 152-153: 2x3x4 cube => 24 total elements
        int[][][] cube = new int[2][3][4];
        long totalElements = Arrays.totalCountOfElements(cube);
        assertEquals(24, totalElements);
    }

    @Test
    public void testClassJavadoc_minMaxSubArrayLength() {
        // Line 156-158: {{1,2,3,4},{5,6,7,8},{9,10,11,12}} => minLen=4, maxLen=4
        int[][] matrix2D = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 } };
        assertEquals(4, Arrays.minSubArrayLength(matrix2D));
        assertEquals(4, Arrays.maxSubArrayLength(matrix2D));
    }

    // ========================
    // mapToObj examples
    // ========================

    @Test
    public void testMapToObj_booleanToString() {
        // Line 420-422: mapToObj({true,false,true}, b -> b?"YES":"NO", String.class) => ["YES","NO","YES"]
        boolean[] flags = { true, false, true };
        String[] strings = Arrays.mapToObj(flags, b -> b ? "YES" : "NO", String.class);
        assertArrayEquals(new String[] { "YES", "NO", "YES" }, strings);
    }

    @Test
    public void testMapToObj_charToString() {
        // Line 532-534: mapToObj({'a','b','c'}, ...) => ["A","B","C"]
        char[] chars = { 'a', 'b', 'c' };
        String[] strings = Arrays.mapToObj(chars, c -> String.valueOf(c).toUpperCase(), String.class);
        assertArrayEquals(new String[] { "A", "B", "C" }, strings);
    }

    @Test
    public void testMapToObj_charToInt3D() {
        // Line 604-606: char[][][] cube => Integer[][][] with ASCII values
        char[][][] cube = { { { 'a', 'b' }, { 'c', 'd' } }, { { 'e', 'f' }, { 'g', 'h' } } };
        Integer[][][] result = Arrays.mapToObj(cube, c -> (int) c, Integer.class);
        assertEquals(97, result[0][0][0]); // 'a'
        assertEquals(98, result[0][0][1]); // 'b'
        assertEquals(99, result[0][1][0]); // 'c'
        assertEquals(104, result[1][1][1]); // 'h'
    }

    @Test
    public void testMapToObj_byteToHex() {
        // Line 638-641: mapToObj({1,2,3,4}, b -> format("%02X", b), String.class) => ["01","02","03","04"]
        byte[] bytes = { 1, 2, 3, 4 };
        String[] hex = Arrays.mapToObj(bytes, b -> String.format("%02X", b), String.class);
        assertArrayEquals(new String[] { "01", "02", "03", "04" }, hex);
    }

    @Test
    public void testMapToObj_intToString() {
        // Line 854-856: mapToObj({1,2,3}, i -> "Number: " + i, String.class) => ["Number: 1",...]
        int[] ints = { 1, 2, 3 };
        String[] result = Arrays.mapToObj(ints, i -> "Number: " + i, String.class);
        assertArrayEquals(new String[] { "Number: 1", "Number: 2", "Number: 3" }, result);
    }

    @Test
    public void testMapToObj_longToString() {
        // Line 961-963: mapToObj({1000000L,...}, l -> l/1000000 + "M", String.class) => ["1M","2M","3M"]
        long[] longs = { 1000000L, 2000000L, 3000000L };
        String[] result = Arrays.mapToObj(longs, l -> l / 1000000 + "M", String.class);
        assertArrayEquals(new String[] { "1M", "2M", "3M" }, result);
    }

    @Test
    public void testMapToObj_floatToString() {
        // Line 1180-1182: mapToObj({1.5f,2.5f,3.5f}, f -> String.valueOf(f), String.class) => ["1.5","2.5","3.5"]
        float[] floats = { 1.5f, 2.5f, 3.5f };
        String[] result = Arrays.mapToObj(floats, f -> String.valueOf(f), String.class);
        assertArrayEquals(new String[] { "1.5", "2.5", "3.5" }, result);
    }

    // ========================
    // mapToLong examples
    // ========================

    @Test
    public void testMapToLong_intToLong() {
        // Line 1293-1296: mapToLong({1,2,3}, i -> i * 1000000000L) => {1000000000L,...}
        int[] ints = { 1, 2, 3 };
        long[] longs = Arrays.mapToLong(ints, i -> i * 1000000000L);
        assertArrayEquals(new long[] { 1000000000L, 2000000000L, 3000000000L }, longs);
    }

    @Test
    public void testMapToLong_intToLong3D() {
        // Line 1362-1364: mapToLong(cube, i -> (long)i*i) => {{{1L,4L},{9L,16L}},{{25L,36L},{49L,64L}}}
        int[][][] cube = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } } };
        long[][][] result = Arrays.mapToLong(cube, i -> (long) i * i);
        assertArrayEquals(new long[] { 1L, 4L }, result[0][0]);
        assertArrayEquals(new long[] { 9L, 16L }, result[0][1]);
        assertArrayEquals(new long[] { 25L, 36L }, result[1][0]);
        assertArrayEquals(new long[] { 49L, 64L }, result[1][1]);
    }

    // ========================
    // mapToDouble examples
    // ========================

    @Test
    public void testMapToDouble_intToDouble() {
        // Line 1396-1398: mapToDouble({1,2,3}, i -> i/2.0) => {0.5, 1.0, 1.5}
        int[] ints = { 1, 2, 3 };
        double[] doubles = Arrays.mapToDouble(ints, i -> i / 2.0);
        assertArrayEquals(new double[] { 0.5, 1.0, 1.5 }, doubles);
    }

    @Test
    public void testMapToDouble_longToDouble() {
        // Line 1591-1593: mapToDouble({100L,200L,300L}, l -> l/100.0) => {1.0,2.0,3.0}
        long[] values = { 100L, 200L, 300L };
        double[] result = Arrays.mapToDouble(values, l -> l / 100.0);
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, result);
    }

    // ========================
    // mapToInt examples
    // ========================

    @Test
    public void testMapToInt_longToInt() {
        // Line 1492-1494: mapToInt({1000L,2000L,3000L}, l -> (int)(l/1000)) => {1,2,3}
        long[] longs = { 1000L, 2000L, 3000L };
        int[] ints = Arrays.mapToInt(longs, l -> (int) (l / 1000));
        assertArrayEquals(new int[] { 1, 2, 3 }, ints);
    }

    @Test
    public void testMapToInt_doubleToInt() {
        // Line 1688-1690: mapToInt({1.7,2.3,3.9}, d -> (int)Math.round(d)) => {2,2,4}
        double[] doubles = { 1.7, 2.3, 3.9 };
        int[] ints = Arrays.mapToInt(doubles, d -> (int) Math.round(d));
        assertArrayEquals(new int[] { 2, 2, 4 }, ints);
    }

    @Test
    public void testMapToInt_doubleToInt2D() {
        // Line 1720-1723: mapToInt({{1.5,2.7},{3.2,4.9}}, d -> (int)Math.round(d)) => {{2,3},{3,5}}
        double[][] matrix = { { 1.5, 2.7 }, { 3.2, 4.9 } };
        int[][] result = Arrays.mapToInt(matrix, d -> (int) Math.round(d));
        assertArrayEquals(new int[] { 2, 3 }, result[0]);
        assertArrayEquals(new int[] { 3, 5 }, result[1]);
    }

    @Test
    public void testMapToInt_doubleToInt3D() {
        // Line 1753-1756: mapToInt(cube, d -> (int)Math.ceil(d)) => {{{2,3},{4,5}},{{6,7},{8,9}}}
        double[][][] cube = { { { 1.5, 2.7 }, { 3.2, 4.9 } }, { { 5.1, 6.8 }, { 7.3, 8.6 } } };
        int[][][] result = Arrays.mapToInt(cube, d -> (int) Math.ceil(d));
        assertArrayEquals(new int[] { 2, 3 }, result[0][0]);
        assertArrayEquals(new int[] { 4, 5 }, result[0][1]);
        assertArrayEquals(new int[] { 6, 7 }, result[1][0]);
        assertArrayEquals(new int[] { 8, 9 }, result[1][1]);
    }

    // ========================
    // mapToLong from double examples
    // ========================

    @Test
    public void testMapToLong_doubleToLong() {
        // Line 1787-1789: mapToLong({10.50,20.75,30.25}, d -> (long)(d*100)) => {1050,2075,3025}
        double[] prices = { 10.50, 20.75, 30.25 };
        long[] cents = Arrays.mapToLong(prices, d -> (long) (d * 100));
        assertArrayEquals(new long[] { 1050, 2075, 3025 }, cents);
    }

    @Test
    public void testMapToLong_doubleToLong2D() {
        // Line 1819-1822: mapToLong({{10.50,20.75},{30.25,40.50}}, d -> (long)(d*100))
        double[][] prices = { { 10.50, 20.75 }, { 30.25, 40.50 } };
        long[][] cents = Arrays.mapToLong(prices, d -> (long) (d * 100));
        assertArrayEquals(new long[] { 1050, 2075 }, cents[0]);
        assertArrayEquals(new long[] { 3025, 4050 }, cents[1]);
    }

    @Test
    public void testMapToLong_doubleToLong3D() {
        // Line 1852-1855: mapToLong(cube, d -> (long)(d*1000))
        double[][][] cube = { { { 1.5, 2.7 }, { 3.2, 4.9 } }, { { 5.1, 6.8 }, { 7.3, 8.6 } } };
        long[][][] result = Arrays.mapToLong(cube, d -> (long) (d * 1000));
        assertArrayEquals(new long[] { 1500, 2700 }, result[0][0]);
        assertArrayEquals(new long[] { 3200, 4900 }, result[0][1]);
        assertArrayEquals(new long[] { 5100, 6800 }, result[1][0]);
        assertArrayEquals(new long[] { 7300, 8600 }, result[1][1]);
    }

    // ========================
    // boolean reshape/flatten examples
    // ========================

    @Test
    public void testBooleanReshape2D() {
        // Line 2057-2060: reshape({true,false,true,false,true}, 2)
        boolean[] arr = { true, false, true, false, true };
        boolean[][] reshaped = Arrays.reshape(arr, 2);
        assertEquals(3, reshaped.length);
        assertArrayEquals(new boolean[] { true, false }, reshaped[0]);
        assertArrayEquals(new boolean[] { true, false }, reshaped[1]);
        assertArrayEquals(new boolean[] { true }, reshaped[2]);
    }

    @Test
    public void testBooleanReshape3D() {
        // Line 2091-2094: reshape({true,false,true,false,true,false}, 2, 2)
        boolean[] arr = { true, false, true, false, true, false };
        boolean[][][] reshaped = Arrays.reshape(arr, 2, 2);
        assertEquals(2, reshaped.length);
        assertArrayEquals(new boolean[] { true, false }, reshaped[0][0]);
        assertArrayEquals(new boolean[] { true, false }, reshaped[0][1]);
        assertArrayEquals(new boolean[] { true, false }, reshaped[1][0]);
    }

    @Test
    public void testBooleanFlatten2D() {
        // Line 2131-2134: flatten({{true,false},{true},{false,true}}) => {true,false,true,false,true}
        boolean[][] arr = { { true, false }, { true }, { false, true } };
        boolean[] flattened = Arrays.flatten(arr);
        assertArrayEquals(new boolean[] { true, false, true, false, true }, flattened);
    }

    @Test
    public void testBooleanFlatten3D() {
        // Line 2170-2173: flatten({{{true,false},{true}},{{false,true}}}) => {true,false,true,false,true}
        boolean[][][] cube = { { { true, false }, { true } }, { { false, true } } };
        boolean[] flattened = Arrays.flatten(cube);
        assertArrayEquals(new boolean[] { true, false, true, false, true }, flattened);
    }

    // ========================
    // boolean zip examples
    // ========================

    @Test
    public void testBooleanZipBinary() {
        // Line 2296-2299: zip({true,false,true,false}, {false,true,false}, (x,y) -> x&&y) => {false,false,false}
        boolean[] a = { true, false, true, false };
        boolean[] b = { false, true, false };
        boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);
        assertArrayEquals(new boolean[] { false, false, false }, result);
    }

    @Test
    public void testBooleanZipBinaryWithDefaults() {
        // Line 2332-2336: zip({true,false,true,false}, {false,true}, false, true, (x,y) -> x||y) => {true,true,true,false}
        // (using true for missing b elements): index0: true||false=true, index1: false||true=true, index2: true||true=true, index3: false||true(default)=true
        // Wait - Javadoc says result is {true, true, true, false}
        // Let me check: a={true,false,true,false}, b={false,true}, defaultA=false, defaultB=true
        // i=0: true||false=true, i=1: false||true=true, i=2: true||true(defaultB)=true, i=3: false||true(defaultB)=true
        // That should be {true,true,true,true}, but Javadoc says {true,true,true,false}
        // The Javadoc comment says "(using true for missing b elements)" which would mean defaultB=true
        // With defaultB=true: {true||false, false||true, true||true, false||true} = {true, true, true, true}
        // But Javadoc says {true, true, true, false}. This seems like a Javadoc bug.
        // Let me test what actually happens:
        boolean[] a = { true, false, true, false };
        boolean[] b = { false, true };
        boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);
        // Actual result should be {true, true, true, true} since false||true=true
        assertArrayEquals(new boolean[] { true, true, true, true }, result);
    }

    @Test
    public void testBooleanZipTernary() {
        // Line 2383-2386: zip({true,false,true,false}, {false,true,false}, {true,true}, (x,y,z)->x||y||z) => {true,true}
        boolean[] a = { true, false, true, false };
        boolean[] b = { false, true, false };
        boolean[] c = { true, true };
        boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);
        assertArrayEquals(new boolean[] { true, true }, result);
    }

    // ========================
    // boolean totalCountOfElements / minSubArrayLength / maxSubArrayLength
    // ========================

    @Test
    public void testBooleanTotalCountOfElements2D() {
        // Line 2833-2835
        boolean[][] array = { { true, false, true }, null, { false, true, false } };
        assertEquals(6, Arrays.totalCountOfElements(array));
    }

    @Test
    public void testBooleanTotalCountOfElements3D() {
        // Line 2861-2863
        boolean[][][] array = { { { true, false }, { true } }, { { false, true, false } } };
        assertEquals(6, Arrays.totalCountOfElements(array));
    }

    @Test
    public void testBooleanMinSubArrayLength() {
        // Line 2899-2901
        boolean[][] array = { { true, false, true }, { false }, { true, false } };
        assertEquals(1, Arrays.minSubArrayLength(array));
    }

    @Test
    public void testBooleanMaxSubArrayLength() {
        // Line 2927-2929
        boolean[][] array = { { true, false, true }, { false }, { true, false } };
        assertEquals(3, Arrays.maxSubArrayLength(array));
    }

    // ========================
    // byte flatten/reshape examples
    // ========================

    @Test
    public void testByteFlatten2D() {
        // Line 4722-4725
        byte[][] arr = { { 1, 2 }, { 3 }, { 4, 5 } };
        byte[] flattened = Arrays.flatten(arr);
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, flattened);
    }

    @Test
    public void testByteReshape2D() {
        // Line 4885-4888
        byte[] arr = { 1, 2, 3, 4, 5 };
        byte[][] reshaped = Arrays.reshape(arr, 2);
        assertArrayEquals(new byte[] { 1, 2 }, reshaped[0]);
        assertArrayEquals(new byte[] { 3, 4 }, reshaped[1]);
        assertArrayEquals(new byte[] { 5 }, reshaped[2]);
    }

    @Test
    public void testByteReshape3D() {
        // Line 4920-4922: reshape({1,2,3,4,5,6}, 2, 2) => {{{1,2},{3,4}},{{5,6}}}
        byte[] arr = { 1, 2, 3, 4, 5, 6 };
        byte[][][] reshaped = Arrays.reshape(arr, 2, 2);
        assertEquals(2, reshaped.length);
        assertArrayEquals(new byte[] { 1, 2 }, reshaped[0][0]);
        assertArrayEquals(new byte[] { 3, 4 }, reshaped[0][1]);
        assertArrayEquals(new byte[] { 5, 6 }, reshaped[1][0]);
    }

    // ========================
    // byte zip examples
    // ========================

    @Test
    public void testByteZipBinary() {
        // Line 4960-4963: zip({1,2,3,4}, {5,6,7}, (x,y) -> (byte)(x+y)) => {6,8,10}
        byte[] a = { 1, 2, 3, 4 };
        byte[] b = { 5, 6, 7 };
        byte[] result = Arrays.zip(a, b, (x, y) -> (byte) (x + y));
        assertArrayEquals(new byte[] { 6, 8, 10 }, result);
    }

    @Test
    public void testByteZipBinaryWithDefaults() {
        // Line 4992-4996: zip({1,2,3,4}, {5,6}, 0, 10, (x,y) -> (byte)(x+y)) => {6,8,13,14}
        byte[] a = { 1, 2, 3, 4 };
        byte[] b = { 5, 6 };
        byte[] result = Arrays.zip(a, b, (byte) 0, (byte) 10, (x, y) -> (byte) (x + y));
        assertArrayEquals(new byte[] { 6, 8, 13, 14 }, result);
    }

    @Test
    public void testByteZipTernary() {
        // Line 5040-5044: zip({1,2,3,4}, {5,6,7}, {8,9}, (x,y,z) -> (byte)(x+y+z)) => {14,17}
        byte[] a = { 1, 2, 3, 4 };
        byte[] b = { 5, 6, 7 };
        byte[] c = { 8, 9 };
        byte[] result = Arrays.zip(a, b, c, (x, y, z) -> (byte) (x + y + z));
        assertArrayEquals(new byte[] { 14, 17 }, result);
    }

    @Test
    public void testByteZipTernaryWithDefaults() {
        // Line 5077-5081: zip({1,2,3,4}, {5,6}, {8,9,10}, 0, 10, 20, ...) => {14,17,23,34}
        byte[] a = { 1, 2, 3, 4 };
        byte[] b = { 5, 6 };
        byte[] c = { 8, 9, 10 };
        byte[] result = Arrays.zip(a, b, c, (byte) 0, (byte) 10, (byte) 20, (x, y, z) -> (byte) (x + y + z));
        assertArrayEquals(new byte[] { 14, 17, 23, 34 }, result);
    }

    // ========================
    // byte totalCountOfElements / minSubArrayLength / maxSubArrayLength
    // ========================

    @Test
    public void testByteTotalCountOfElements2D() {
        // Line 5451-5452: {{1,2,3}, null, {4,5}} => 5
        byte[][] array = { { 1, 2, 3 }, null, { 4, 5 } };
        assertEquals(5, Arrays.totalCountOfElements(array));
    }

    @Test
    public void testByteTotalCountOfElements3D() {
        // Line 5478-5479: {{{1,2},{3}},{{4,5,6}}} => 6
        byte[][][] array = { { { 1, 2 }, { 3 } }, { { 4, 5, 6 } } };
        assertEquals(6, Arrays.totalCountOfElements(array));
    }

    @Test
    public void testByteMinSubArrayLength() {
        // Line 5516-5517: {{1,2,3},{4,5},{6,7,8,9}} => minLen 2
        byte[][] array = { { 1, 2, 3 }, { 4, 5 }, { 6, 7, 8, 9 } };
        assertEquals(2, Arrays.minSubArrayLength(array));
    }

    @Test
    public void testByteMaxSubArrayLength() {
        // Line 5544-5545: {{1,2}, null, {3,4,5,6}} => maxLen 4
        byte[][] array = { { 1, 2 }, null, { 3, 4, 5, 6 } };
        assertEquals(4, Arrays.maxSubArrayLength(array));
    }

    // ========================
    // int reshape/flatten examples
    // ========================

    @Test
    public void testIntReshape2D() {
        // Line 7278-7281: reshape({1,2,3,4,5,6,7}, 3) => {{1,2,3},{4,5,6},{7}}
        int[] arr = { 1, 2, 3, 4, 5, 6, 7 };
        int[][] result = Arrays.reshape(arr, 3);
        assertArrayEquals(new int[] { 1, 2, 3 }, result[0]);
        assertArrayEquals(new int[] { 4, 5, 6 }, result[1]);
        assertArrayEquals(new int[] { 7 }, result[2]);
    }

    @Test
    public void testIntReshape3D() {
        // Line 7316-7318: reshape({1,2,3,4,5,6,7,8}, 2, 2) => {{{1,2},{3,4}},{{5,6},{7,8}}}
        int[] arr = { 1, 2, 3, 4, 5, 6, 7, 8 };
        int[][][] result = Arrays.reshape(arr, 2, 2);
        assertArrayEquals(new int[] { 1, 2 }, result[0][0]);
        assertArrayEquals(new int[] { 3, 4 }, result[0][1]);
        assertArrayEquals(new int[] { 5, 6 }, result[1][0]);
        assertArrayEquals(new int[] { 7, 8 }, result[1][1]);
    }

    @Test
    public void testIntFlatten2D() {
        // Line 7358-7361: flatten({{1,2,3},{4,5},{6,7,8}}) => {1,2,3,4,5,6,7,8}
        int[][] arr = { { 1, 2, 3 }, { 4, 5 }, { 6, 7, 8 } };
        int[] result = Arrays.flatten(arr);
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, result);
    }

    // ========================
    // int updateAll examples
    // ========================

    @Test
    public void testIntUpdateAll1D() {
        // Line 7095-7096: updateAll({1,2,3}, x -> x*x) => {1,4,9}
        int[] arr = { 1, 2, 3 };
        Arrays.updateAll(arr, x -> x * x);
        assertArrayEquals(new int[] { 1, 4, 9 }, arr);
    }

    @Test
    public void testIntUpdateAll2D() {
        // Line 7122-7125: updateAll({{1,2},{3,4}}, x -> x+10) => {{11,12},{13,14}}
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        Arrays.updateAll(arr, x -> x + 10);
        assertArrayEquals(new int[] { 11, 12 }, arr[0]);
        assertArrayEquals(new int[] { 13, 14 }, arr[1]);
    }

    // ========================
    // int zip examples
    // ========================

    @Test
    public void testIntZipBinary() {
        // Line 7530-7533: zip({1,2,3,4}, {5,6,7}, (x,y) -> x+y) => {6,8,10}
        int[] a = { 1, 2, 3, 4 };
        int[] b = { 5, 6, 7 };
        int[] result = Arrays.zip(a, b, (x, y) -> x + y);
        assertArrayEquals(new int[] { 6, 8, 10 }, result);
    }

    @Test
    public void testIntZipBinaryWithDefaults() {
        // Line 7562-7566: zip({1,2,3,4}, {5,6}, 0, 10, (x,y) -> x+y) => {6,8,13,14}
        int[] a = { 1, 2, 3, 4 };
        int[] b = { 5, 6 };
        int[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
        assertArrayEquals(new int[] { 6, 8, 13, 14 }, result);
    }

    // ========================
    // int totalCountOfElements / minSubArrayLength / maxSubArrayLength
    // ========================

    @Test
    public void testIntTotalCountOfElements2D() {
        // Line 8020-8022: {{1,2},{3,4,5},null,{}} => 5
        int[][] a = { { 1, 2 }, { 3, 4, 5 }, null, {} };
        assertEquals(5, Arrays.totalCountOfElements(a));
    }

    @Test
    public void testIntTotalCountOfElements3D() {
        // Line 8048-8050: {{{1},{2,3}},null,{{4,5,6}}} => 6
        int[][][] a = { { { 1 }, { 2, 3 } }, null, { { 4, 5, 6 } } };
        assertEquals(6, Arrays.totalCountOfElements(a));
    }

    @Test
    public void testIntMinSubArrayLength() {
        // Line 8085-8088: {{1,2,3},{4,5},null,{6}} => 0 (null sub-array has length 0)
        int[][] a = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
        assertEquals(0, Arrays.minSubArrayLength(a));
    }

    @Test
    public void testIntMaxSubArrayLength() {
        // Line 8113-8116: {{1},{2,3},null,{4,5,6}} => 3
        int[][] a = { { 1 }, { 2, 3 }, null, { 4, 5, 6 } };
        assertEquals(3, Arrays.maxSubArrayLength(a));
    }

    // ========================
    // println examples
    // ========================

    @Test
    public void testPrintlnObjectArrayNull() {
        // Line 279-280: println((Object[]) null) => "null"
        String result = Arrays.println((Object[]) null);
        assertTrue(result.contains("null"));
    }

    @Test
    public void testPrintlnObjectArrayEmpty() {
        // Line 284-285: println(empty Object[]) => "[]"
        Object[] empty = new Object[0];
        String result = Arrays.println(empty);
        assertTrue(result.contains("[]"));
    }

    @Test
    public void testPrintlnBooleanArray() {
        // Line 2965-2967: println({true,false,true}) => "[true, false, true]"
        boolean[] arr = { true, false, true };
        String result = Arrays.println(arr);
        assertTrue(result.contains("[true, false, true]"));
    }

    @Test
    public void testPrintlnByteArray() {
        // Line 5581-5583: println({1,2,3,4,5}) => "[1, 2, 3, 4, 5]"
        byte[] array = { 1, 2, 3, 4, 5 };
        String result = Arrays.println(array);
        assertTrue(result.contains("[1, 2, 3, 4, 5]"));
    }

    // ========================
    // short reshape/flatten examples
    // ========================

    @Test
    public void testShortReshape2D() {
        // Line 5993: reshape({1,2,3,4,5}, 2) => {{1,2},{3,4},{5}}
        short[] array = { 1, 2, 3, 4, 5 };
        short[][] reshaped = Arrays.reshape(array, 2);
        assertArrayEquals(new short[] { 1, 2 }, reshaped[0]);
        assertArrayEquals(new short[] { 3, 4 }, reshaped[1]);
        assertArrayEquals(new short[] { 5 }, reshaped[2]);
    }

    @Test
    public void testShortFlatten2D() {
        // Line 6066: flatten({{1,2,3},{4,5}}) => {1,2,3,4,5}
        short[][] array = { { 1, 2, 3 }, { 4, 5 } };
        short[] flat = Arrays.flatten(array);
        assertArrayEquals(new short[] { 1, 2, 3, 4, 5 }, flat);
    }

    // ========================
    // Additional conversion tests from different int section
    // ========================

    @Test
    public void testIntToDoubleConversion2D() {
        // Line 14195-14197: toDouble({{1,2},{3,4}}) => {{1.0,2.0},{3.0,4.0}}
        int[][] source = { { 1, 2 }, { 3, 4 } };
        double[][] result = Arrays.toDouble(source);
        assertArrayEquals(new double[] { 1.0, 2.0 }, result[0]);
        assertArrayEquals(new double[] { 3.0, 4.0 }, result[1]);
    }

    @Test
    public void testIntToFloatConversion2D() {
        // Line 13847-13849: toFloat({{1,2},{3,4}}) => {{1.0f,2.0f},{3.0f,4.0f}}
        int[][] source = { { 1, 2 }, { 3, 4 } };
        float[][] result = Arrays.toFloat(source);
        assertArrayEquals(new float[] { 1.0f, 2.0f }, result[0]);
        assertArrayEquals(new float[] { 3.0f, 4.0f }, result[1]);
    }

    // ========================
    // applyOnFlattened examples
    // ========================

    @Test
    public void testByteApplyOnFlattened() {
        // Line 4807-4809: applyOnFlattened({{3,1},{4,2}}, t -> sort(t)) => {{1,2},{3,4}}
        byte[][] arr = { { 3, 1 }, { 4, 2 } };
        Arrays.applyOnFlattened(arr, t -> java.util.Arrays.sort(t));
        assertArrayEquals(new byte[] { 1, 2 }, arr[0]);
        assertArrayEquals(new byte[] { 3, 4 }, arr[1]);
    }
}
