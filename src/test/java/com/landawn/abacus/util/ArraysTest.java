/*
 * Copyright (C) 2024 HaiYang Li
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Arrays.f;
import com.landawn.abacus.util.Arrays.ff;
import com.landawn.abacus.util.Arrays.fff;
import com.landawn.abacus.util.stream.Stream;

class ArraysTest extends TestBase {

    @Test
    public void test_minSubArrayLength() {
        final String[][] a = { { "a", "b" }, { "c", "d", "d" } };
        final String[][][] b = { { { "a", "b" } }, { { "1", "2" }, { "3", "4" } } };
        final int[][][] c = Arrays.reshape(Array.rangeClosed(1, 9), 2, 3);

        assertEquals(2, ff.minSubArrayLength(a));
        assertEquals(3, ff.maxSubArrayLength(a));
        assertEquals(1, ff.minSubArrayLength(b));
        assertEquals(2, ff.maxSubArrayLength(b));
        assertEquals(1, ff.minSubArrayLength(c));
        assertEquals(2, ff.maxSubArrayLength(c));

    }

    @Test
    public void test_zip() {
        {
            final String[][][] a = { { { "a", "b" }, { "c", "d" } }, { { "1", "2" }, { "3", "4" } } };
            final String[][][] b = { { { "a", "b" }, { "c", "d" } }, { { "1", "2" }, { "3", "4" } } };
            N.println(a);
            fff.println(a);

            final String[][][] c = fff.zip(a, b, (t, u) -> t + u);
            fff.println(c);
            assertArrayEquals(new String[][][] { { { "aa", "bb" }, { "cc", "dd" } }, { { "11", "22" }, { "33", "44" } } }, c);
        }
        {
            final int[][][] a = Arrays.reshape(Array.rangeClosed(1, 9), 2, 3);
            final int[][][] b = Arrays.reshape(Array.repeat(0, 9), 3, 2);
            final int[][][] c = Arrays.zip(a, b, -1, -1, (p1, p2) -> p1 + p2);
            Arrays.printAndReturn(c);
            assertEquals(2, c.length);
            assertArrayEquals(new int[][] { { 1, 2, 2 }, { 4, 5, 5 }, { -1, -1 } }, c[0]);
            assertArrayEquals(new int[][] { { 7, 8, 8 }, { -1 } }, c[1]);
        }

        {

            Integer[][][] a = { { { 1 } } };
            Integer[][][] b = { { { 10, 20 } } };
            Integer[][][] c = { { {}, { 100 } } };
            Integer[][][] result = fff.zip(a, b, c, -1, -1, -1, (x, y, z) -> x + y + z);

            fff.println(result);
            assertEquals(1, result.length);
            assertArrayEquals(new Integer[] { 10, 18 }, result[0][0]);
            assertArrayEquals(new Integer[] { 98 }, result[0][1]);
        }

    }

    @Test
    public void test_reshape() {
        {
            final int[] a = Array.rangeClosed(1, 9);
            final int[][] b = Arrays.reshape(a, 2);
            final int[][][] c = Arrays.reshape(a, 2, 2);
            final int[][][] d = Arrays.reshape(a, 3, 2);
            final int[][][] e = Arrays.reshape(a, 2, 3);
            final int[][][] f = Arrays.reshape(a, 3, 3);
            Arrays.printAndReturn(a);
            N.println(Strings.repeat('-', 80));
            Arrays.printAndReturn(b);
            N.println(Strings.repeat('-', 80));
            Arrays.printAndReturn(c);
            N.println(Strings.repeat('-', 80));
            Arrays.printAndReturn(d);
            N.println(Strings.repeat('-', 80));
            Arrays.printAndReturn(e);
            N.println(Strings.repeat('-', 80));
            Arrays.printAndReturn(f);

            assertArrayEquals(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 }, { 7, 8 }, { 9 } }, b);
            assertArrayEquals(new int[][][] { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } }, { { 9 } } }, c);
            assertArrayEquals(new int[][][] { { { 1, 2 }, { 3, 4 }, { 5, 6 } }, { { 7, 8 }, { 9 } } }, d);
            assertArrayEquals(new int[][][] { { { 1, 2, 3 }, { 4, 5, 6 } }, { { 7, 8, 9 } } }, e);
            assertArrayEquals(new int[][][] { { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } } }, f);
        }
    }

    @Test
    public void testFfReshapeRejectsNullInput() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ff.reshape((String[]) null, 2));
    }

    @Test
    public void testFffReshapeRejectsNullInput() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> fff.reshape((String[]) null, 2, 2));
    }

    @Test
    public void testFffZipWithDefaultsSupportsNullLeftInput() throws Exception {
        Integer[][][] right = { { { 1, 2 } } };

        Integer[][][] result = fff.zip(null, right, 0, 0, Integer::sum);

        Assertions.assertArrayEquals(new Integer[][][] { { { 1, 2 } } }, result);
    }

    @Test
    public void testFffZipWithDefaultsRejectsMissingTypeHints() {
        Integer[][][] right = { { { 1 } } };

        Assertions.assertThrows(IllegalArgumentException.class, () -> fff.zip(null, right, null, 0, Integer::sum));
    }

    @Test
    public void testFfZipTypeInferenceRejectsNullLeftArray() {
        Integer[][] right = { { 1, 2 } };
        Integer[][] third = { { 3, 4 } };

        Assertions.assertThrows(IllegalArgumentException.class, () -> ff.zip((Integer[][]) null, right, Integer::sum));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ff.zip((Integer[][]) null, right, third, (a, b, c) -> a + b + c));
    }

    @Test
    public void testFffZipTypeInferenceRejectsNullLeftArray() {
        Integer[][][] right = { { { 1, 2 } } };
        Integer[][][] third = { { { 3, 4 } } };

        Assertions.assertThrows(IllegalArgumentException.class, () -> fff.zip((Integer[][][]) null, right, Integer::sum));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fff.zip((Integer[][][]) null, right, third, (a, b, c) -> a + b + c));
    }

    @Test
    public void testFfMapInferredTypeSupportsCovariantInput() throws Exception {
        Number[][] input = new Integer[][] { { 1, 2 } };

        Number[][] out = ff.map(input, n -> n.longValue() + 1L);

        Assertions.assertEquals(2L, out[0][0]);
        Assertions.assertEquals(3L, out[0][1]);
    }

    @Test
    public void testFffMapInferredTypeSupportsCovariantInput() throws Exception {
        Number[][][] input = new Integer[][][] { { { 1, 2 } } };

        Number[][][] out = fff.map(input, n -> n.longValue() + 1L);

        Assertions.assertEquals(2L, out[0][0][0]);
        Assertions.assertEquals(3L, out[0][0][1]);
    }

    @Test
    public void testFfZipInferredTypeSupportsCovariantInput() throws Exception {
        Number[][] left = new Integer[][] { { 1, 2 } };
        Number[][] right = new Number[][] { { 10L, 20L } };

        Number[][] out = ff.zip(left, right, (x, y) -> y);

        Assertions.assertEquals(10L, out[0][0]);
        Assertions.assertEquals(20L, out[0][1]);
    }

    @Test
    public void testFfZipWithDefaultsInferredTypeSupportsCovariantInput() throws Exception {
        Number[][] right = new Number[][] { { 10L, 20L } };

        Number[][] out = ff.zip((Number[][]) null, right, (Number) 0, (Number) 0L, (x, y) -> y);

        Assertions.assertEquals(10L, out[0][0]);
        Assertions.assertEquals(20L, out[0][1]);
    }

    // Cover nested empty/null array shape handling and trailing zip branches.
    @Test
    public void testMapToObjNestedEmptyInputs_CharAndByte() throws Exception {
        String[][] mappedChars2d = Arrays.mapToObj((char[][]) null, c -> String.valueOf(c), String.class);
        Integer[][][] mappedChars3d = Arrays.mapToObj(new char[0][][], c -> (int) c, Integer.class);
        String[][] mappedBytes2d = Arrays.mapToObj(new byte[0][], b -> "B" + b, String.class);
        Integer[][][] mappedBytes3d = Arrays.mapToObj((byte[][][]) null, b -> (int) b, Integer.class);

        Assertions.assertEquals(0, mappedChars2d.length);
        Assertions.assertEquals(String[].class, mappedChars2d.getClass().getComponentType());
        Assertions.assertEquals(0, mappedChars3d.length);
        Assertions.assertEquals(Integer[][].class, mappedChars3d.getClass().getComponentType());
        Assertions.assertEquals(0, mappedBytes2d.length);
        Assertions.assertEquals(String[].class, mappedBytes2d.getClass().getComponentType());
        Assertions.assertEquals(0, mappedBytes3d.length);
        Assertions.assertEquals(Integer[][].class, mappedBytes3d.getClass().getComponentType());
    }

    @Test
    public void testZipWithDefaultValuesForTrailingRows_ByteArrays() throws Exception {
        byte[][] left2d = { { 1, 2 }, { 3 } };
        byte[][] right2d = { { 10 }, { 20, 30 }, { 40, 50 } };

        byte[][] zipped2d = Arrays.zip(left2d, right2d, (byte) 100, (byte) 0, (x, y) -> (byte) (x + y));

        Assertions.assertArrayEquals(new byte[] { 11, 2 }, zipped2d[0]);
        Assertions.assertArrayEquals(new byte[] { 23, -126 }, zipped2d[1]);
        Assertions.assertArrayEquals(new byte[] { -116, -106 }, zipped2d[2]);

        byte[][][] left3d = { { { 1 }, { 2 } } };
        byte[][][] right3d = { { { 10, 20 } }, { { 30 } } };

        byte[][][] zipped3d = Arrays.zip(left3d, right3d, (byte) 100, (byte) 0, (x, y) -> (byte) (x + y));

        Assertions.assertArrayEquals(new byte[] { 11, 120 }, zipped3d[0][0]);
        Assertions.assertArrayEquals(new byte[] { 2 }, zipped3d[0][1]);
        Assertions.assertArrayEquals(new byte[] { -126 }, zipped3d[1][0]);
    }

    // Exercise nested default-filling overloads that expand to the longest outer shape.
    @Test
    public void testZipWithDefaultValuesForTrailingRows_ShortArrays() throws Exception {
        short[][] left2d = { { 1, 2 }, { 3 } };
        short[][] right2d = { { 10 }, { 20, 30 }, { 40, 50 } };

        short[][] zipped2d = Arrays.zip(left2d, right2d, (short) 100, (short) 0, (x, y) -> (short) (x + y));

        Assertions.assertArrayEquals(new short[] { 11, 2 }, zipped2d[0]);
        Assertions.assertArrayEquals(new short[] { 23, 130 }, zipped2d[1]);
        Assertions.assertArrayEquals(new short[] { 140, 150 }, zipped2d[2]);

        short[][][] left3d = { { { 1 }, { 2 } } };
        short[][][] right3d = { { { 10, 20 } }, { { 30 } } };

        short[][][] zipped3d = Arrays.zip(left3d, right3d, (short) 100, (short) 0, (x, y) -> (short) (x + y));

        Assertions.assertArrayEquals(new short[] { 11, 120 }, zipped3d[0][0]);
        Assertions.assertArrayEquals(new short[] { 2 }, zipped3d[0][1]);
        Assertions.assertArrayEquals(new short[] { 130 }, zipped3d[1][0]);

        short[][] first2d = { { 1 }, { 2, 3 } };
        short[][] second2d = { { 10, 20 } };
        short[][] third2d = { { 100 }, { 200, 300 }, { 400 } };

        short[][] zippedTernary2d = Arrays.zip(first2d, second2d, third2d, (short) 1000, (short) 2000, (short) 3000, (x, y, z) -> (short) (x + y + z));

        Assertions.assertArrayEquals(new short[] { 111, 4020 }, zippedTernary2d[0]);
        Assertions.assertArrayEquals(new short[] { 2202, 2303 }, zippedTernary2d[1]);
        Assertions.assertArrayEquals(new short[] { 3400 }, zippedTernary2d[2]);
    }

    @Test
    public void testZipWithDefaultValuesForTrailingRows_FloatAndDoubleArrays() throws Exception {
        float[][] leftFloat2d = { { 1f, 2f }, { 3f } };
        float[][] rightFloat2d = { { 10f }, { 20f, 30f }, { 40f } };

        float[][] zippedFloat2d = Arrays.zip(leftFloat2d, rightFloat2d, 100f, 0f, (x, y) -> x + y);

        Assertions.assertArrayEquals(new float[] { 11f, 2f }, zippedFloat2d[0], 0.0001f);
        Assertions.assertArrayEquals(new float[] { 23f, 130f }, zippedFloat2d[1], 0.0001f);
        Assertions.assertArrayEquals(new float[] { 140f }, zippedFloat2d[2], 0.0001f);

        float[][][] leftFloat3d = { { { 1f }, { 2f } } };
        float[][][] rightFloat3d = { { { 10f, 20f } }, { { 30f } } };

        float[][][] zippedFloat3d = Arrays.zip(leftFloat3d, rightFloat3d, 100f, 0f, (x, y) -> x + y);

        Assertions.assertArrayEquals(new float[] { 11f, 120f }, zippedFloat3d[0][0], 0.0001f);
        Assertions.assertArrayEquals(new float[] { 2f }, zippedFloat3d[0][1], 0.0001f);
        Assertions.assertArrayEquals(new float[] { 130f }, zippedFloat3d[1][0], 0.0001f);

        double[][] firstDouble2d = { { 1d }, { 2d, 3d } };
        double[][] secondDouble2d = { { 10d, 20d } };
        double[][] thirdDouble2d = { { 100d }, { 200d, 300d }, { 400d } };

        double[][] zippedDoubleTernary2d = Arrays.zip(firstDouble2d, secondDouble2d, thirdDouble2d, 1000d, 2000d, 3000d, (x, y, z) -> x + y + z);

        Assertions.assertArrayEquals(new double[] { 111d, 4020d }, zippedDoubleTernary2d[0], 0.0001d);
        Assertions.assertArrayEquals(new double[] { 2202d, 2303d }, zippedDoubleTernary2d[1], 0.0001d);
        Assertions.assertArrayEquals(new double[] { 3400d }, zippedDoubleTernary2d[2], 0.0001d);

        double[][][] leftDouble3d = { { { 1d }, { 2d } } };
        double[][][] rightDouble3d = { { { 10d, 20d } }, { { 30d } } };

        double[][][] zippedDouble3d = Arrays.zip(leftDouble3d, rightDouble3d, 100d, 0d, (x, y) -> x + y);

        Assertions.assertArrayEquals(new double[] { 11d, 120d }, zippedDouble3d[0][0], 0.0001d);
        Assertions.assertArrayEquals(new double[] { 2d }, zippedDouble3d[0][1], 0.0001d);
        Assertions.assertArrayEquals(new double[] { 130d }, zippedDouble3d[1][0], 0.0001d);
    }

    @Test
    public void testZipWithDefaultValuesForTrailingBlocks_Int3DArrays() throws Exception {
        int[][][] first = { { { 1 }, { 2, 3 } } };
        int[][][] second = { { { 10, 20 } } };
        int[][][] third = { { { 100 } }, { { 200, 300 } }, { { 400 } } };

        int[][][] zipped = Arrays.zip(first, second, third, 1000, 2000, 3000, (x, y, z) -> x + y + z);

        Assertions.assertArrayEquals(new int[] { 111, 4020 }, zipped[0][0]);
        Assertions.assertArrayEquals(new int[] { 5002, 5003 }, zipped[0][1]);
        Assertions.assertArrayEquals(new int[] { 3200, 3300 }, zipped[1][0]);
        Assertions.assertArrayEquals(new int[] { 3400 }, zipped[2][0]);
    }

    @Test
    public void testFlattenSkipsNullAndEmptyNestedCharArrays() {
        char[][] emptyRows = { null, new char[0], { 'a', 'b' }, { 'c' } };
        char[][][] nested = { null, new char[0][], { null, new char[0], { 'x', 'y' } }, { { 'z' }, null } };

        Assertions.assertArrayEquals(new char[] { 'a', 'b', 'c' }, Arrays.flatten(emptyRows));
        Assertions.assertArrayEquals(new char[] { 'x', 'y', 'z' }, Arrays.flatten(nested));
    }

    @Test
    public void testZipWithDefaultValuesForTrailingRows_ShortFloatAndDoubleArrays() throws Exception {
        short[][] shortLeft2d = { { 1, 2 }, { 3 } };
        short[][] shortRight2d = { { 10 }, { 20, 30 }, { 40 } };
        short[][] shortZipped2d = Arrays.zip(shortLeft2d, shortRight2d, (short) 100, (short) 1000, (x, y) -> (short) (x - y));
        Assertions.assertArrayEquals(new short[] { -9, -998 }, shortZipped2d[0]);
        Assertions.assertArrayEquals(new short[] { -17, 70 }, shortZipped2d[1]);
        Assertions.assertArrayEquals(new short[] { 60 }, shortZipped2d[2]);

        short[][][] shortLeft3d = { { { 1, 2 } }, { { 3 } } };
        short[][][] shortRight3d = { { { 10 }, { 20 } }, { { 30, 40 } }, { { 50 } } };
        short[][][] shortZipped3d = Arrays.zip(shortLeft3d, shortRight3d, (short) 100, (short) 1000, (x, y) -> (short) (x + y));
        Assertions.assertArrayEquals(new short[] { 11, 1002 }, shortZipped3d[0][0]);
        Assertions.assertArrayEquals(new short[] { 120 }, shortZipped3d[0][1]);
        Assertions.assertArrayEquals(new short[] { 33, 140 }, shortZipped3d[1][0]);
        Assertions.assertArrayEquals(new short[] { 150 }, shortZipped3d[2][0]);

        float[][] floatLeft2d = { { 1.5f }, { 2.5f, 3.5f } };
        float[][] floatRight2d = { { 10.0f, 20.0f }, { 30.0f } };
        float[][] floatThird2d = { { 100.0f }, { 200.0f, 300.0f }, { 400.0f } };
        float[][] floatZipped2d = Arrays.zip(floatLeft2d, floatRight2d, floatThird2d, 1.0f, 2.0f, 3.0f, (x, y, z) -> x + y + z);
        Assertions.assertArrayEquals(new float[] { 111.5f, 24.0f }, floatZipped2d[0], 0.0001f);
        Assertions.assertArrayEquals(new float[] { 232.5f, 305.5f }, floatZipped2d[1], 0.0001f);
        Assertions.assertArrayEquals(new float[] { 403.0f }, floatZipped2d[2], 0.0001f);

        float[][][] floatLeft3d = { { { 1.0f } }, { { 2.0f } } };
        float[][][] floatRight3d = { { { 10.0f }, { 20.0f } }, { { 30.0f } } };
        float[][][] floatThird3d = { { { 100.0f } }, { { 200.0f, 300.0f } }, { { 400.0f } } };
        float[][][] floatZipped3d = Arrays.zip(floatLeft3d, floatRight3d, floatThird3d, 1.0f, 2.0f, 3.0f, (x, y, z) -> x + y + z);
        Assertions.assertArrayEquals(new float[] { 111.0f }, floatZipped3d[0][0], 0.0001f);
        Assertions.assertArrayEquals(new float[] { 24.0f }, floatZipped3d[0][1], 0.0001f);
        Assertions.assertArrayEquals(new float[] { 232.0f, 303.0f }, floatZipped3d[1][0], 0.0001f);
        Assertions.assertArrayEquals(new float[] { 403.0f }, floatZipped3d[2][0], 0.0001f);

        double[][] doubleLeft2d = { { 1.0, 2.0 }, { 3.0 } };
        double[][] doubleRight2d = { { 10.0 }, { 20.0, 30.0 }, { 40.0 } };
        double[][] doubleZipped2d = Arrays.zip(doubleLeft2d, doubleRight2d, 100.0, 1000.0, (x, y) -> x * y);
        Assertions.assertArrayEquals(new double[] { 10.0, 2000.0 }, doubleZipped2d[0], 0.0001d);
        Assertions.assertArrayEquals(new double[] { 60.0, 3000.0 }, doubleZipped2d[1], 0.0001d);
        Assertions.assertArrayEquals(new double[] { 4000.0 }, doubleZipped2d[2], 0.0001d);

        double[][][] doubleLeft3d = { { { 1.0 } }, { { 2.0, 3.0 } } };
        double[][][] doubleRight3d = { { { 10.0 }, { 20.0 } }, { { 30.0 } } };
        double[][][] doubleThird3d = { { { 100.0 } }, { { 200.0, 300.0 } }, { { 400.0 } } };
        double[][][] doubleZipped3d = Arrays.zip(doubleLeft3d, doubleRight3d, doubleThird3d, 1.0, 2.0, 3.0, (x, y, z) -> x + y + z);
        Assertions.assertArrayEquals(new double[] { 111.0 }, doubleZipped3d[0][0], 0.0001d);
        Assertions.assertArrayEquals(new double[] { 24.0 }, doubleZipped3d[0][1], 0.0001d);
        Assertions.assertArrayEquals(new double[] { 232.0, 305.0 }, doubleZipped3d[1][0], 0.0001d);
        Assertions.assertArrayEquals(new double[] { 403.0 }, doubleZipped3d[2][0], 0.0001d);
    }

    @Test
    public void testPrintlnPrimitiveCubes_EmptyInput() {
        Assertions.assertEquals("[]", Arrays.printAndReturn(new char[0][][]));
        Assertions.assertEquals("[]", Arrays.printAndReturn(new byte[0][][]));
        Assertions.assertEquals("[]", Arrays.printAndReturn(new short[0][][]));
        Assertions.assertEquals("[]", Arrays.printAndReturn(new int[0][][]));
        Assertions.assertEquals("[]", Arrays.printAndReturn(new long[0][][]));
        Assertions.assertEquals("[]", Arrays.printAndReturn(new float[0][][]));
        Assertions.assertEquals("[]", Arrays.printAndReturn(new double[0][][]));
    }

    @Test
    public void test_001() throws IOException {

        File file = new File("src/main/java/com/landawn/abacus/util/Arrays.java");
        List<String> lines = IOUtil.readAllLines(file);

        Stream.of(lines).filter(line -> line.trim().startsWith("*") && line.contains("N.")).forEach(System.out::println);

        List<String> newLines = Stream.of(lines)
                .map(line -> line.trim().startsWith("*") && line.contains("N.") ? line.replace("N.", "Arrays.") : line)
                .toList();

        IOUtil.writeLines(newLines, file);

        assertEquals(lines.size(), newLines.size());
        assertTrue(newLines.stream().noneMatch(line -> line.trim().startsWith("*") && line.contains("N.")));
    }

    @Nested
    public class BooleanArrayTest {

        @Test
        public void testZip1D_TwoArrays_SameLength() throws Exception {
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true, false, true };

            boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[] { false, false, false, false }, result);
        }

        @Test
        public void testZip1D_TwoArrays_DifferentLength_FirstShorter() throws Exception {
            boolean[] a = { true, false };
            boolean[] b = { false, true, false, true };

            boolean[] result = Arrays.zip(a, b, (x, y) -> x || y);

            Assertions.assertArrayEquals(new boolean[] { true, true }, result);
        }

        @Test
        public void testZip1D_TwoArrays_DifferentLength_SecondShorter() throws Exception {
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true };

            boolean[] result = Arrays.zip(a, b, (x, y) -> x ^ y);

            Assertions.assertArrayEquals(new boolean[] { true, true }, result);
        }

        @Test
        public void testZip1D_TwoArrays_EmptyArrays() throws Exception {
            boolean[] a = {};
            boolean[] b = {};

            boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        public void testZip1D_TwoArrays_NullArrays() throws Exception {
            boolean[] a = null;
            boolean[] b = null;

            boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        public void testZip1D_TwoArrays_OneNull() throws Exception {
            boolean[] a = { true, false };
            boolean[] b = null;

            boolean[] result = Arrays.zip(a, b, (x, y) -> x || y);

            Assertions.assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        public void testZip1D_TwoArraysWithDefaults_SameLength() throws Exception {
            boolean[] a = { true, false, true };
            boolean[] b = { false, true, false };

            boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[] { false, false, false }, result);
        }

        @Test
        public void testZip1D_TwoArraysWithDefaults_FirstShorter() throws Exception {
            boolean[] a = { true, false };
            boolean[] b = { false, true, false, true };

            boolean[] result = Arrays.zip(a, b, true, false, (x, y) -> x || y);

            Assertions.assertArrayEquals(new boolean[] { true, true, true, true }, result);
        }

        @Test
        public void testZip1D_TwoArraysWithDefaults_SecondShorter() throws Exception {
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true };

            boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x ^ y);

            Assertions.assertArrayEquals(new boolean[] { true, true, false, true }, result);
        }

        @Test
        public void testZip1D_TwoArraysWithDefaults_EmptyArrays() throws Exception {
            boolean[] a = {};
            boolean[] b = {};

            boolean[] result = Arrays.zip(a, b, true, false, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        public void testZip1D_TwoArraysWithDefaults_NullArrays() throws Exception {
            boolean[] a = null;
            boolean[] b = null;

            boolean[] result = Arrays.zip(a, b, true, false, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        public void testZip1D_TwoArraysWithDefaults_OneNullOneNonEmpty() throws Exception {
            boolean[] a = null;
            boolean[] b = { true, false, true };

            boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

            Assertions.assertArrayEquals(new boolean[] { true, false, true }, result);
        }

        @Test
        public void testZip1D_ThreeArrays_SameLength() throws Exception {
            boolean[] a = { true, false, true };
            boolean[] b = { false, true, false };
            boolean[] c = { true, true, false };

            boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            Assertions.assertArrayEquals(new boolean[] { true, true, true }, result);
        }

        @Test
        public void testZip1D_ThreeArrays_DifferentLengths() throws Exception {
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true };
            boolean[] c = { true, true, false };

            boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x && y && z);

            Assertions.assertArrayEquals(new boolean[] { false, false }, result);
        }

        @Test
        public void testZip1D_ThreeArrays_EmptyArrays() throws Exception {
            boolean[] a = {};
            boolean[] b = { true };
            boolean[] c = { false, true };

            boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            Assertions.assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        public void testZip1D_ThreeArrays_NullArrays() throws Exception {
            boolean[] a = null;
            boolean[] b = null;
            boolean[] c = null;

            boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x && y && z);

            Assertions.assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        public void testZip1D_ThreeArraysWithDefaults_SameLength() throws Exception {
            boolean[] a = { true, false, true };
            boolean[] b = { false, true, false };
            boolean[] c = { true, true, false };

            boolean[] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x && y && z);

            Assertions.assertArrayEquals(new boolean[] { false, false, false }, result);
        }

        @Test
        public void testZip1D_ThreeArraysWithDefaults_DifferentLengths() throws Exception {
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true };
            boolean[] c = { true, true, false };

            boolean[] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x || y || z);

            Assertions.assertArrayEquals(new boolean[] { true, true, true, true }, result);
        }

        @Test
        public void testZip1D_ThreeArraysWithDefaults_OneEmpty() throws Exception {
            boolean[] a = {};
            boolean[] b = { true, false };
            boolean[] c = { false, true, false };

            boolean[] result = Arrays.zip(a, b, c, true, false, true, (x, y, z) -> x ^ y ^ z);

            Assertions.assertArrayEquals(new boolean[] { false, false, true }, result);
        }

        @Test
        public void testZip1D_ThreeArraysWithDefaults_AllNull() throws Exception {
            boolean[] a = null;
            boolean[] b = null;
            boolean[] c = null;

            boolean[] result = Arrays.zip(a, b, c, true, false, true, (x, y, z) -> x && y && z);

            Assertions.assertArrayEquals(new boolean[] {}, result);
        }

        // Test cases for two-dimensional boolean array zip methods

        @Test
        public void testZip2D_TwoArrays_SameStructure() throws Exception {
            boolean[][] a = { { true, false }, { false, true } };
            boolean[][] b = { { false, true }, { true, false } };

            boolean[][] result = Arrays.zip(a, b, (x, y) -> x ^ y);

            Assertions.assertArrayEquals(new boolean[][] { { true, true }, { true, true } }, result);
        }

        @Test
        public void testZip2D_TwoArrays_DifferentOuterLength() throws Exception {
            boolean[][] a = { { true, false }, { false, true }, { true, true } };
            boolean[][] b = { { false, true }, { true, false } };

            boolean[][] result = Arrays.zip(a, b, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[][] { { false, false }, { false, false } }, result);
        }

        @Test
        public void testZip2D_TwoArrays_DifferentInnerLengths() throws Exception {
            boolean[][] a = { { true, false, true }, { false, true } };
            boolean[][] b = { { false, true }, { true, false, true, false } };

            boolean[][] result = Arrays.zip(a, b, (x, y) -> x || y);

            Assertions.assertArrayEquals(new boolean[][] { { true, true }, { true, true } }, result);
        }

        @Test
        public void testZip2D_TwoArrays_EmptyArrays() throws Exception {
            boolean[][] a = {};
            boolean[][] b = {};

            boolean[][] result = Arrays.zip(a, b, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[][] {}, result);
        }

        @Test
        public void testZip2D_TwoArrays_NullArrays() throws Exception {
            boolean[][] a = null;
            boolean[][] b = null;

            boolean[][] result = Arrays.zip(a, b, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[][] {}, result);
        }

        @Test
        public void testZip2D_TwoArraysWithDefaults_SameStructure() throws Exception {
            boolean[][] a = { { true, false }, { false, true } };
            boolean[][] b = { { false, true }, { true, false } };

            boolean[][] result = Arrays.zip(a, b, true, false, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[][] { { false, false }, { false, false } }, result);
        }

        @Test
        public void testZip2D_TwoArraysWithDefaults_DifferentOuterLength() throws Exception {
            boolean[][] a = { { true, false } };
            boolean[][] b = { { false, true }, { true, false }, { true, true } };

            boolean[][] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

            Assertions.assertArrayEquals(new boolean[][] { { true, true }, { true, false }, { true, true } }, result);
        }

        @Test
        public void testZip2D_TwoArraysWithDefaults_DifferentInnerLengths() throws Exception {
            boolean[][] a = { { true, false }, { false, true, false } };
            boolean[][] b = { { false, true, false }, { true, false }, { true } };

            boolean[][] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

            Assertions.assertArrayEquals(new boolean[][] { { true, true, false }, { true, true, true }, { true } }, result);
        }

        @Test
        public void testZip2D_TwoArraysWithDefaults_NullSubArrays() throws Exception {
            boolean[][] a = { { true, false }, null };
            boolean[][] b = { null, { false, true } };

            boolean[][] result = Arrays.zip(a, b, true, false, (x, y) -> x || y);

            Assertions.assertArrayEquals(new boolean[][] { { true, false }, { true, true } }, result);
        }

        @Test
        public void testZip2D_ThreeArrays_SameStructure() throws Exception {
            boolean[][] a = { { true, false }, { false, true } };
            boolean[][] b = { { false, true }, { true, false } };
            boolean[][] c = { { true, true }, { false, false } };

            boolean[][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            Assertions.assertArrayEquals(new boolean[][] { { true, true }, { true, true } }, result);
        }

        @Test
        public void testZip2D_ThreeArrays_DifferentOuterLengths() throws Exception {
            boolean[][] a = { { true, false }, { false, true }, { true, true } };
            boolean[][] b = { { false, true } };
            boolean[][] c = { { true, true }, { false, false } };

            boolean[][] result = Arrays.zip(a, b, c, (x, y, z) -> x && y && z);

            Assertions.assertArrayEquals(new boolean[][] { { false, false } }, result);
        }

        @Test
        public void testZip2D_ThreeArrays_EmptyAndNull() throws Exception {
            boolean[][] a = {};
            boolean[][] b = null;
            boolean[][] c = { { true, false } };

            boolean[][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            Assertions.assertArrayEquals(new boolean[][] {}, result);
        }

        @Test
        public void testZip2D_ThreeArraysWithDefaults_SameStructure() throws Exception {
            boolean[][] a = { { true, false }, { false, true } };
            boolean[][] b = { { false, true }, { true, false } };
            boolean[][] c = { { true, true }, { false, false } };

            boolean[][] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x ^ y ^ z);

            Assertions.assertArrayEquals(new boolean[][] { { false, false }, { true, true } }, result);
        }

        @Test
        public void testZip2D_ThreeArraysWithDefaults_DifferentStructures() throws Exception {
            boolean[][] a = { { true } };
            boolean[][] b = { { false, true }, { true } };
            boolean[][] c = { { true, true, false } };

            boolean[][] result = Arrays.zip(a, b, c, false, false, true, (x, y, z) -> x || y || z);

            Assertions.assertArrayEquals(new boolean[][] { { true, true, false }, { true } }, result);
        }

        // Test cases for three-dimensional boolean array zip methods

        @Test
        public void testZip3D_TwoArrays_SameStructure() throws Exception {
            boolean[][][] a = { { { true, false }, { false, true } }, { { true, true }, { false, false } } };
            boolean[][][] b = { { { false, true }, { true, false } }, { { false, false }, { true, true } } };

            boolean[][][] result = Arrays.zip(a, b, (x, y) -> x ^ y);

            Assertions.assertArrayEquals(new boolean[][][] { { { true, true }, { true, true } }, { { true, true }, { true, true } } }, result);
        }

        @Test
        public void testZip3D_TwoArrays_DifferentOuterLength() throws Exception {
            boolean[][][] a = { { { true, false } }, { { false, true } }, { { true, true } } };
            boolean[][][] b = { { { false, true } }, { { true, false } } };

            boolean[][][] result = Arrays.zip(a, b, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[][][] { { { false, false } }, { { false, false } } }, result);
        }

        @Test
        public void testZip3D_TwoArrays_EmptyAndNull() throws Exception {
            boolean[][][] a = {};
            boolean[][][] b = null;

            boolean[][][] result = Arrays.zip(a, b, (x, y) -> x || y);

            Assertions.assertArrayEquals(new boolean[][][] {}, result);
        }

        @Test
        public void testZip3D_TwoArraysWithDefaults_SameStructure() throws Exception {
            boolean[][][] a = { { { true, false }, { false, true } }, { { true, true }, { false, false } } };
            boolean[][][] b = { { { false, true }, { true, false } }, { { false, false }, { true, true } } };

            boolean[][][] result = Arrays.zip(a, b, true, false, (x, y) -> x && y);

            Assertions.assertArrayEquals(new boolean[][][] { { { false, false }, { false, false } }, { { false, false }, { false, false } } }, result);
        }

        @Test
        public void testZip3D_TwoArraysWithDefaults_DifferentStructures() throws Exception {
            boolean[][][] a = { { { true, false } }, { { false } } };
            boolean[][][] b = { { { false, true }, { true, false } }, { { true, true } }, { { false, false } } };

            boolean[][][] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

            Assertions.assertArrayEquals(new boolean[][][] { { { true, true }, { true, false } }, { { true, true } }, { { false, false } } }, result);
        }

        @Test
        public void testZip3D_ThreeArrays_SameStructure() throws Exception {
            boolean[][][] a = { { { true, false } }, { { false, true } } };
            boolean[][][] b = { { { false, true } }, { { true, false } } };
            boolean[][][] c = { { { true, true } }, { { false, false } } };

            boolean[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            Assertions.assertArrayEquals(new boolean[][][] { { { true, true } }, { { true, true } } }, result);
        }

        @Test
        public void testZip3D_ThreeArrays_DifferentOuterLengths() throws Exception {
            boolean[][][] a = { { { true, false } }, { { false, true } }, { { true, true } } };
            boolean[][][] b = { { { false, true } } };
            boolean[][][] c = { { { true, true } }, { { false, false } } };

            boolean[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x && y && z);

            Assertions.assertArrayEquals(new boolean[][][] { { { false, false } } }, result);
        }

        @Test
        public void testZip3D_ThreeArrays_NullArrays() throws Exception {
            boolean[][][] a = null;
            boolean[][][] b = null;
            boolean[][][] c = null;

            boolean[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            Assertions.assertArrayEquals(new boolean[][][] {}, result);
        }

        @Test
        public void testZip3D_ThreeArraysWithDefaults_SameStructure() throws Exception {
            boolean[][][] a = { { { true, false } }, { { false, true } } };
            boolean[][][] b = { { { false, true } }, { { true, false } } };
            boolean[][][] c = { { { true, true } }, { { false, false } } };

            boolean[][][] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x ^ y ^ z);

            Assertions.assertArrayEquals(new boolean[][][] { { { false, false } }, { { true, true } } }, result);
        }

        @Test
        public void testZip3D_ThreeArraysWithDefaults_ComplexStructure() throws Exception {
            boolean[][][] a = { { { true } } };
            boolean[][][] b = { { { false, true }, { true } }, { { false } } };
            boolean[][][] c = { { { true, true, false } } };

            boolean[][][] result = Arrays.zip(a, b, c, true, false, true, (x, y, z) -> x || y || z);

            Assertions.assertArrayEquals(new boolean[][][] { { { true, true, true }, { true } }, { { true } } }, result);
        }

        // Exception handling tests

        @Test
        public void testZip1D_TwoArrays_ExceptionPropagation() {
            boolean[] a = { true, false };
            boolean[] b = { false, true };

            Throwables.BooleanBinaryOperator<RuntimeException> errorFunction = (x, y) -> {
                throw new RuntimeException("Test exception");
            };

            Assertions.assertThrows(RuntimeException.class, () -> Arrays.zip(a, b, errorFunction));
        }

        @Test
        public void testZip1D_ThreeArrays_ExceptionPropagation() {
            boolean[] a = { true };
            boolean[] b = { false };
            boolean[] c = { true };

            Throwables.BooleanTernaryOperator<RuntimeException> errorFunction = (x, y, z) -> {
                throw new RuntimeException("Test exception");
            };

            Assertions.assertThrows(RuntimeException.class, () -> Arrays.zip(a, b, c, errorFunction));
        }

        @Test
        public void testZip2D_TwoArrays_ExceptionPropagation() {
            boolean[][] a = { { true } };
            boolean[][] b = { { false } };

            Throwables.BooleanBinaryOperator<RuntimeException> errorFunction = (x, y) -> {
                throw new RuntimeException("Test exception");
            };

            Assertions.assertThrows(RuntimeException.class, () -> Arrays.zip(a, b, errorFunction));
        }

        @Test
        public void testZip3D_TwoArrays_ExceptionPropagation() {
            boolean[][][] a = { { { true } } };
            boolean[][][] b = { { { false } } };

            Throwables.BooleanBinaryOperator<RuntimeException> errorFunction = (x, y) -> {
                throw new RuntimeException("Test exception");
            };

            Assertions.assertThrows(RuntimeException.class, () -> Arrays.zip(a, b, errorFunction));
        }
    }

    @Nested
    public class CharArrayTest {

        // Tests for zip(char[], char[], CharBiFunction)

        @Test
        public void testZipTwoCharArrays() throws Exception {
            // Test normal case with equal length arrays
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y', 'Z' };
            char[] result = Arrays.zip(a, b, (x, y) -> (char) (x + y - 'A'));
            Assertions.assertArrayEquals(new char[] { 'X', 'Z', '\\' }, result);
        }

        @Test
        public void testZipTwoCharArraysWithNulls() throws Exception {
            // Test with first array null
            char[] a = null;
            char[] b = { 'X', 'Y', 'Z' };
            char[] result = Arrays.zip(a, b, (x, y) -> (char) (x + y));
            Assertions.assertArrayEquals(new char[] {}, result);

            // Test with second array null
            char[] a2 = { 'A', 'B', 'C' };
            char[] b2 = null;
            char[] result2 = Arrays.zip(a2, b2, (x, y) -> (char) (x + y));
            Assertions.assertArrayEquals(new char[] {}, result2);

            // Test with both arrays null
            char[] result3 = Arrays.zip((char[]) null, null, (x, y) -> (char) (x + y));
            Assertions.assertArrayEquals(new char[] {}, result3);
        }

        @Test
        public void testZipTwoCharArraysEmptyArrays() throws Exception {
            // Test with empty arrays
            char[] a = {};
            char[] b = {};
            char[] result = Arrays.zip(a, b, (x, y) -> (char) (x + y));
            Assertions.assertArrayEquals(new char[] {}, result);

            // Test with one empty array
            char[] a2 = { 'A', 'B' };
            char[] b2 = {};
            char[] result2 = Arrays.zip(a2, b2, (x, y) -> (char) (x + y));
            Assertions.assertArrayEquals(new char[] {}, result2);
        }

        @Test
        public void testZipTwoCharArraysException() {
            char[] a = { 'A', 'B', 'C' };
            char[] b = { 'X', 'Y', 'Z' };

            Assertions.assertThrows(Exception.class, () -> {
                Arrays.zip(a, b, (Throwables.CharBinaryOperator<Exception>) (x, y) -> {
                    throw new Exception("Test exception");
                });
            });
        }

        @Test
        public void testZipTwoCharArraysWithDefaultsEqualLength() throws Exception {
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y' };
            char[] result = Arrays.zip(a, b, '?', '!', (x, y) -> (char) (x + y - 'A'));
            // result: {'X', 'Z', '#', '$'} (using '!' for missing b elements)
            Assertions.assertArrayEquals(new char[] { 'X', 'Z', '#', '$' }, result);
        }

        @Test
        public void testZipTwoCharArraysWithDefaultsNulls() throws Exception {
            // Test with first array null
            char[] a = null;
            char[] b = { 'X', 'Y', 'Z' };
            char[] result = Arrays.zip(a, b, '?', '!', (x, y) -> y);
            Assertions.assertArrayEquals(new char[] { 'X', 'Y', 'Z' }, result);

            // Test with second array null
            char[] a2 = { 'A', 'B', 'C' };
            char[] b2 = null;
            char[] result2 = Arrays.zip(a2, b2, '?', '!', (x, y) -> x);
            Assertions.assertArrayEquals(new char[] { 'A', 'B', 'C' }, result2);

            // Test with both arrays null
            char[] result3 = Arrays.zip((char[]) null, null, '?', '!', (x, y) -> x);
            Assertions.assertArrayEquals(new char[] {}, result3);
        }

        @Test
        public void testZipTwoCharArraysWithDefaultsEmptyArrays() throws Exception {
            // Test with one empty array
            char[] a = { 'A', 'B' };
            char[] b = {};
            char[] result = Arrays.zip(a, b, '?', '!', (x, y) -> x);
            Assertions.assertArrayEquals(new char[] { 'A', 'B' }, result);

            // Test with both empty arrays
            char[] a2 = {};
            char[] b2 = {};
            char[] result2 = Arrays.zip(a2, b2, '?', '!', (x, y) -> x);
            Assertions.assertArrayEquals(new char[] {}, result2);
        }

        // Tests for zip(char[], char[], char[], CharTriFunction)

        @Test
        public void testZipThreeCharArrays() throws Exception {
            // Test normal case with equal length arrays
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y', 'Z' };
            char[] c = { '1', '3' };
            char[] result = Arrays.zip(a, b, c, (x, y, z) -> (char) (x + y + z - 'A' - '0'));
            Assertions.assertArrayEquals(new char[] { 'Y', ']' }, result);
        }

        @Test
        public void testZipThreeCharArraysDifferentLengths() throws Exception {
            // Test with arrays of different lengths
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y', 'Z' };
            char[] c = { '1', '2' };
            char[] result = Arrays.zip(a, b, c, (x, y, z) -> (char) (x + y + z - 'A' - '0'));
            Assertions.assertArrayEquals(new char[] { 'Y', '\\' }, result);
        }

        @Test
        public void testZipThreeCharArraysWithNulls() throws Exception {
            // Test with one null array
            char[] a = { 'A', 'B', 'C' };
            char[] b = null;
            char[] c = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, c, (x, y, z) -> x);
            Assertions.assertArrayEquals(new char[] {}, result);

            // Test with all null arrays
            char[] result2 = Arrays.zip((char[]) null, null, null, (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            Assertions.assertArrayEquals(new char[] {}, result2);
        }

        @Test
        public void testZipThreeCharArraysEmptyArrays() throws Exception {
            // Test with one empty array
            char[] a = { 'A', 'B' };
            char[] b = { 'X', 'Y' };
            char[] c = {};
            char[] result = Arrays.zip(a, b, c, (x, y, z) -> x);
            Assertions.assertArrayEquals(new char[] {}, result);

            // Test with all empty arrays
            char[] a2 = {};
            char[] b2 = {};
            char[] c2 = {};
            char[] result2 = Arrays.zip(a2, b2, c2, (x, y, z) -> x);
            Assertions.assertArrayEquals(new char[] {}, result2);
        }

        @Test
        public void testZipThreeCharArraysWithDefaultsEqualLength() throws Exception {
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y' };
            char[] c = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, c, '?', '!', '0', (x, y, z) -> (char) (x + y + z - 'A' - '0'));
            Assertions.assertArrayEquals(new char[] { 'Y', '\\', '&', '$' }, result);
        }

        @Test
        public void testZipThreeCharArraysWithDefaultsNulls() throws Exception {
            // Test with one null array
            char[] a = null;
            char[] b = { 'X', 'Y', 'Z' };
            char[] c = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, c, '?', '!', '0', (x, y, z) -> y);
            Assertions.assertArrayEquals(new char[] { 'X', 'Y', 'Z' }, result);

            // Test with all null arrays
            char[] result2 = Arrays.zip((char[]) null, null, null, '?', '!', '0', (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            Assertions.assertArrayEquals(new char[] {}, result2);
        }

        @Test
        public void testZipThreeCharArraysWithDefaultsEmptyArrays() throws Exception {
            // Test with mixed empty and non-empty arrays
            char[] a = { 'A', 'B' };
            char[] b = {};
            char[] c = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, c, '?', '!', '0', (x, y, z) -> x);
            Assertions.assertArrayEquals(new char[] { 'A', 'B', '?' }, result);
        }

        // Tests for zip(char[][], char[][], CharBiFunction)

        @Test
        public void testZipTwoChar2DArrays() throws Exception {
            // Test normal case
            char[][] a = { { 'A', 'B' }, { 'C', 'D', 'E' } };
            char[][] b = { { 'X', 'Y', 'Z' }, { '1', '2' } };
            char[][] result = Arrays.zip(a, b, (x, y) -> (char) (x + y - 'A'));
            Assertions.assertArrayEquals(new char[][] { { 'X', 'Z' }, { '3', '5' } }, result);
        }

        @Test
        public void testZipTwoChar2DArraysDifferentOuterLengths() throws Exception {
            // Test with first array longer
            char[][] a = { { 'A', 'B' }, { 'C', 'D' }, { 'E', 'F' } };
            char[][] b = { { 'X', 'Y' }, { '1', '2' } };
            char[][] result = Arrays.zip(a, b, (x, y) -> (char) (x + y - 'A'));
            Assertions.assertEquals(2, result.length);
            Assertions.assertArrayEquals(new char[] { 'X', 'Z' }, result[0]);
            Assertions.assertArrayEquals(new char[] { '3', '5' }, result[1]);
        }

        @Test
        public void testZipTwoChar2DArraysWithNulls() throws Exception {
            // Test with null outer array
            char[][] a = null;
            char[][] b = { { 'X', 'Y' }, { '1', '2' } };
            char[][] result = Arrays.zip(a, b, (x, y) -> y);
            Assertions.assertEquals(0, result.length);

            // Test with null inner arrays
            char[][] a2 = { { 'A', 'B' }, null };
            char[][] b2 = { null, { '1', '2' } };
            char[][] result2 = Arrays.zip(a2, b2, (x, y) -> 'Z');
            Assertions.assertEquals(2, result2.length);
            Assertions.assertArrayEquals(new char[] {}, result2[0]);
            Assertions.assertArrayEquals(new char[] {}, result2[1]);
        }

        @Test
        public void testZipTwoChar2DArraysEmptyArrays() throws Exception {
            // Test with empty outer arrays
            char[][] a = {};
            char[][] b = {};
            char[][] result = Arrays.zip(a, b, (x, y) -> x);
            Assertions.assertEquals(0, result.length);

            // Test with empty inner arrays
            char[][] a2 = { {}, { 'A', 'B' } };
            char[][] b2 = { { 'X', 'Y' }, {} };
            char[][] result2 = Arrays.zip(a2, b2, (x, y) -> x);
            Assertions.assertEquals(2, result2.length);
            Assertions.assertArrayEquals(new char[] {}, result2[0]);
            Assertions.assertArrayEquals(new char[] {}, result2[1]);
        }

        // Tests for zip(char[][], char[][], char, char, CharBiFunction)

        @Test
        public void testZipTwoChar2DArraysWithDefaults() throws Exception {
            // Test with different outer lengths
            char[][] a = { { 'A', 'B' }, { 'C', 'D', 'E' } };
            char[][] b = { { 'X', 'Y', 'Z' }, { '1', '2' }, { '3' } };
            char[][] result = Arrays.zip(a, b, '?', '!', (x, y) -> (char) (x + y - 'A'));
            // result: {{'X', 'Z', 'X'}, {'3', '5', '%'}, {'1'}}
            Assertions.assertEquals(3, result.length);
            Assertions.assertArrayEquals(new char[][] { { 'X', 'Z', 'X' }, { '3', '5', '%' }, { '1' } }, result);
        }

        @Test
        public void testZipTwoChar2DArraysWithDefaultsNulls() throws Exception {
            // Test with null outer array
            char[][] a = null;
            char[][] b = { { 'X', 'Y' }, { '1', '2' } };
            char[][] result = Arrays.zip(a, b, '?', '!', (x, y) -> y);
            Assertions.assertEquals(2, result.length);
            Assertions.assertArrayEquals(new char[] { 'X', 'Y' }, result[0]);
            Assertions.assertArrayEquals(new char[] { '1', '2' }, result[1]);

            // Test with null inner arrays
            char[][] a2 = { { 'A', 'B' }, null };
            char[][] b2 = { null, { '1', '2' } };
            char[][] result2 = Arrays.zip(a2, b2, '?', '!', (x, y) -> x);
            Assertions.assertEquals(2, result2.length);
            Assertions.assertArrayEquals(new char[] { 'A', 'B' }, result2[0]);
            Assertions.assertArrayEquals(new char[] { '?', '?' }, result2[1]);
        }

        // Tests for zip(char[][], char[][], char[][], CharTriFunction)

        @Test
        public void testZipThreeChar2DArrays() throws Exception {
            // Test normal case
            char[][] a = { { 'A', 'B' }, { 'C', 'D', 'E' } };
            char[][] b = { { 'X', 'Y', 'Z' }, { '1', '2' } };
            char[][] c = { { 'a', 'b' }, { 'c', 'd', 'e' } };
            char[][] result = Arrays.zip(a, b, c, (x, y, z) -> (char) ((x + y + z) / 3));
            Assertions.assertEquals(2, result.length);
            Assertions.assertArrayEquals(new char[][] { { 'S', 'T' }, { 'G', 'H' } }, result);
        }

        @Test
        public void testZipThreeChar2DArraysDifferentOuterLengths() throws Exception {
            // Test with arrays of different outer lengths
            char[][] a = { { 'A', 'B' }, { 'C', 'D' }, { 'E', 'F' } };
            char[][] b = { { 'X', 'Y' } };
            char[][] c = { { 'a', 'b' }, { 'c', 'd' } };
            char[][] result = Arrays.zip(a, b, c, (x, y, z) -> x);
            Assertions.assertEquals(1, result.length);
            Assertions.assertArrayEquals(new char[] { 'A', 'B' }, result[0]);
        }

        @Test
        public void testZipThreeChar2DArraysWithNulls() throws Exception {
            // Test with null arrays
            char[][] result = Arrays.zip((char[][]) null, null, null, (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            Assertions.assertEquals(0, result.length);

            // Test with one null array
            char[][] a = { { 'A', 'B' } };
            char[][] b = null;
            char[][] c = { { 'a', 'b' } };
            char[][] result2 = Arrays.zip(a, b, c, (x, y, z) -> x);
            Assertions.assertEquals(0, result2.length);
        }

        // Tests for zip(char[][], char[][], char[][], char, char, char, CharTriFunction)

        @Test
        public void testZipThreeChar2DArraysWithDefaults() throws Exception {
            // Test with different outer lengths
            char[][] a = { { 'A', 'B' }, { 'C', 'D', 'E' } };
            char[][] b = { { 'X', 'Y', 'Z' }, { '1', '2' }, { '3' } };
            char[][] c = { { 'a', 'b' } };
            char[][] result = Arrays.zip(a, b, c, '?', '!', '~', (x, y, z) -> (char) ((x + y + z) / 3));
            Assertions.assertEquals(3, result.length);
            Assertions.assertArrayEquals(new char[][] { { 'S', 'T', ']' }, { 'P', 'Q', 'L' }, { 'P' } }, result);
        }

        @Test
        public void testZipThreeChar2DArraysWithDefaultsNulls() throws Exception {
            // Test with all null arrays
            char[][] result = Arrays.zip((char[][]) null, null, null, '?', '!', '~', (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            Assertions.assertEquals(0, result.length);

            // Test with mixed null and non-null arrays
            char[][] a = { { 'A', 'B' } };
            char[][] b = null;
            char[][] c = { { 'a', 'b' }, { 'c' } };
            char[][] result2 = Arrays.zip(a, b, c, '?', '!', '~', (x, y, z) -> y);
            Assertions.assertEquals(2, result2.length);
            Assertions.assertArrayEquals(new char[] { '!', '!' }, result2[0]);
            Assertions.assertArrayEquals(new char[] { '!' }, result2[1]);
        }

        // Tests for zip(char[][][], char[][][], CharBiFunction)

        @Test
        public void testZipTwoChar3DArrays() throws Exception {
            // Test normal case
            char[][][] a = { { { 'A', 'B' }, { 'C', 'D' } }, { { 'E', 'F' }, { 'G', 'H' } } };
            char[][][] b = { { { '1', '2' }, { '3', '4' } }, { { '5', '6' }, { '7', '8' } } };
            char[][][] result = Arrays.zip(a, b, (x, y) -> (char) (x + y - 'A'));
            // result: {{{'1', '3'}, {'5', '7'}}, {{'9', ';'}, {'=', '?'}}}
            Assertions.assertEquals(2, result.length);
            Assertions.assertArrayEquals(new char[][][] { { { '1', '3' }, { '5', '7' } }, { { '9', ';' }, { '=', '?' } } }, result);
        }

        @Test
        public void testZipTwoChar3DArraysDifferentOuterLengths() throws Exception {
            // Test with first array longer
            char[][][] a = { { { 'A', 'B' } }, { { 'C', 'D' } }, { { 'E', 'F' } } };
            char[][][] b = { { { '1', '2' } }, { { '3', '4' } } };
            char[][][] result = Arrays.zip(a, b, (x, y) -> y);
            Assertions.assertEquals(2, result.length);
        }

        @Test
        public void testZipTwoChar3DArraysWithNulls() throws Exception {
            // Test with null arrays
            char[][][] result = Arrays.zip((char[][][]) null, null, (Throwables.CharBinaryOperator<Exception>) (x, y) -> x);
            Assertions.assertEquals(0, result.length);

            // Test with null sub-arrays
            char[][][] a = { { { 'A', 'B' } }, null };
            char[][][] b = { null, { { '1', '2' } } };
            char[][][] result2 = Arrays.zip(a, b, (x, y) -> 'Z');
            Assertions.assertEquals(2, result2.length);
            Assertions.assertEquals(0, result2[0].length);
            Assertions.assertEquals(0, result2[1].length);
        }

        // Tests for zip(char[][][], char[][][], char, char, CharBiFunction)

        @Test
        public void testZipTwoChar3DArraysWithDefaults() throws Exception {
            // Test with different outer lengths
            char[][][] a = { { { 'A', 'B' }, { 'C', 'D' } }, { { 'E', 'F' } } };
            char[][][] b = { { { '1', '2' }, { '3', '4' } }, { { '5', '6' }, { '7', '8' } }, { { '9' } } };
            char[][][] result = Arrays.zip(a, b, 'A', '0', (x, y) -> (char) (x + y - 'A'));
            // result: {{{'1', '3'}, {'5', '7'}}, {{'9', ';'}, {'7', '8'}}, {{'9'}}}
            Assertions.assertEquals(3, result.length);
            Assertions.assertArrayEquals(new char[][][] { { { '1', '3' }, { '5', '7' } }, { { '9', ';' }, { '7', '8' } }, { { '9' } } }, result);
        }

        @Test
        public void testZipTwoChar3DArraysWithDefaultsNulls() throws Exception {
            // Test with null outer array
            char[][][] a = null;
            char[][][] b = { { { '1', '2' } } };
            char[][][] result = Arrays.zip(a, b, '?', '!', (x, y) -> y);
            Assertions.assertEquals(1, result.length);
            Assertions.assertEquals(1, result[0].length);
            Assertions.assertArrayEquals(new char[] { '1', '2' }, result[0][0]);
        }

        // Tests for zip(char[][][], char[][][], char[][][], CharTriFunction)

        @Test
        public void testZipThreeChar3DArrays() throws Exception {
            // Test normal case
            char[][][] a = { { { 'A', 'B' } } };
            char[][][] b = { { { '1', '2' }, { '3', '4' } } };
            char[][][] c = { { { 'a', 'b' } } };
            char[][][] result = Arrays.zip(a, b, c, (x, y, z) -> (char) ((x + y + z) / 3));
            Assertions.assertEquals(1, result.length);
            Assertions.assertEquals(1, result[0].length);
            Assertions.assertArrayEquals(new char[] { 'F', 'G' }, result[0][0]);
        }

        @Test
        public void testZipThreeChar3DArraysDifferentOuterLengths() throws Exception {
            // Test with different outer lengths
            char[][][] a = { { { 'A' } }, { { 'B' } }, { { 'C' } } };
            char[][][] b = { { { '1' } } };
            char[][][] c = { { { 'a' } }, { { 'b' } } };
            char[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x);
            Assertions.assertEquals(1, result.length);
        }

        @Test
        public void testZipThreeChar3DArraysWithNulls() throws Exception {
            // Test with all null arrays
            char[][][] result = Arrays.zip((char[][][]) null, null, null, (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            Assertions.assertEquals(0, result.length);
        }

        // Tests for zip(char[][][], char[][][], char[][][], char, char, char, CharTriFunction)

        @Test
        public void testZipThreeChar3DArraysWithDefaults() throws Exception {
            char[][][] a = { { { 'A', 'B' } } };
            char[][][] b = { { { '1', '2' }, { '3', '4' } } };
            char[][][] c = { { { 'a', 'b' } } };
            char[][][] result = Arrays.zip(a, b, c, '?', '!', '~', (x, y, z) -> (char) ((x + y + z) / 3));
            Assertions.assertEquals(1, result.length);
            Assertions.assertEquals(2, result[0].length);
            Assertions.assertArrayEquals(new char[][][] { { { 'F', 'G' }, { 'P', 'P' } } }, result);
        }

        @Test
        public void testZipThreeChar3DArraysWithDefaultsAllNulls() throws Exception {
            // Test with all null arrays
            char[][][] result = Arrays.zip((char[][][]) null, null, null, '?', '!', '~', (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            Assertions.assertEquals(0, result.length);
        }
    }

    @Nested
    public class LongArrayTest {

        // Test zip(long[], long[], LongBiFunction)
        @Test
        public void testZipTwoArrays() throws Exception {
            // Test with normal arrays
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6, 7, 8 };
            long[] result = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] { 6, 8, 10, 12 }, result);
        }

        @Test
        public void testZipTwoArraysDifferentLengths() throws Exception {
            // Test with arrays of different lengths - should use shorter length
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6, 7 };
            long[] result = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] { 6, 8, 10 }, result);

            // Test with first array shorter
            long[] a2 = { 1, 2 };
            long[] b2 = { 5, 6, 7, 8 };
            long[] result2 = Arrays.zip(a2, b2, (x, y) -> x * y);
            Assertions.assertArrayEquals(new long[] { 5, 12 }, result2);
        }

        @Test
        public void testZipTwoArraysWithNull() throws Exception {
            // Test with null arrays
            long[] a = null;
            long[] b = { 5, 6, 7 };
            long[] result = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] {}, result);

            // Test with both null
            long[] result2 = Arrays.zip((long[]) null, (long[]) null, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] {}, result2);

            // Test with second null
            long[] a3 = { 1, 2, 3 };
            long[] result3 = Arrays.zip(a3, null, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] {}, result3);
        }

        @Test
        public void testZipTwoArraysEmptyArrays() throws Exception {
            // Test with empty arrays
            long[] a = {};
            long[] b = { 5, 6, 7 };
            long[] result = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] {}, result);

            // Test with both empty
            long[] result2 = Arrays.zip(new long[] {}, new long[] {}, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] {}, result2);
        }

        // Test zip(long[], long[], long, long, LongBiFunction)
        @Test
        public void testZipTwoArraysWithDefaults() throws Exception {
            // Test with arrays of different lengths using defaults
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6 };
            long[] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] { 6, 8, 13, 14 }, result);
        }

        @Test
        public void testZipTwoArraysWithDefaultsFirstShorter() throws Exception {
            // Test with first array shorter
            long[] a = { 1, 2 };
            long[] b = { 5, 6, 7, 8 };
            long[] result = Arrays.zip(a, b, 100L, 200L, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] { 6, 8, 107, 108 }, result);
        }

        @Test
        public void testZipTwoArraysWithDefaultsEqualLength() throws Exception {
            // Test with equal length arrays - defaults not used
            long[] a = { 1, 2, 3 };
            long[] b = { 5, 6, 7 };
            long[] result = Arrays.zip(a, b, 100L, 200L, (x, y) -> x * y);
            Assertions.assertArrayEquals(new long[] { 5, 12, 21 }, result);
        }

        @Test
        public void testZipTwoArraysWithDefaultsNullArrays() throws Exception {
            // Test with null arrays and defaults
            long[] result = Arrays.zip((long[]) null, (long[]) null, 10L, 20L, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] {}, result);

            // Test with first null
            long[] b = { 5, 6, 7 };
            long[] result2 = Arrays.zip(null, b, 10L, 20L, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] { 15, 16, 17 }, result2);

            // Test with second null
            long[] a = { 1, 2, 3 };
            long[] result3 = Arrays.zip(a, null, 10L, 20L, (x, y) -> x + y);
            Assertions.assertArrayEquals(new long[] { 21, 22, 23 }, result3);
        }

        // Test zip(long[], long[], long[], LongTriFunction)
        @Test
        public void testZipThreeArrays() throws Exception {
            // Test with normal arrays
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6, 7, 8 };
            long[] c = { 9, 10, 11, 12 };
            long[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new long[] { 15, 18, 21, 24 }, result);
        }

        @Test
        public void testZipThreeArraysDifferentLengths() throws Exception {
            // Test with arrays of different lengths - should use shortest length
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6, 7 };
            long[] c = { 8, 9 };
            long[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new long[] { 14, 17 }, result);
        }

        @Test
        public void testZipThreeArraysWithNull() throws Exception {
            // Test with null arrays
            long[] result = Arrays.zip((long[]) null, (long[]) null, (long[]) null, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new long[] {}, result);

            // Test with one null
            long[] a = { 1, 2, 3 };
            long[] b = { 4, 5, 6 };
            long[] result2 = Arrays.zip(a, b, null, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new long[] {}, result2);
        }

        @Test
        public void testZipThreeArraysEmptyArrays() throws Exception {
            // Test with empty arrays
            long[] a = { 1, 2, 3 };
            long[] b = {};
            long[] c = { 7, 8, 9 };
            long[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new long[] {}, result);
        }

        // Test zip(long[], long[], long[], long, long, long, LongTriFunction)
        @Test
        public void testZipThreeArraysWithDefaults() throws Exception {
            // Test with arrays of different lengths using defaults
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6 };
            long[] c = { 8, 9, 10 };
            long[] result = Arrays.zip(a, b, c, 0L, 10L, 20L, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new long[] { 14, 17, 23, 34 }, result);
        }

        @Test
        public void testZipThreeArraysWithDefaultsAllDifferent() throws Exception {
            // Test with all arrays of different lengths
            long[] a = { 1 };
            long[] b = { 5, 6 };
            long[] c = { 8, 9, 10 };
            long[] result = Arrays.zip(a, b, c, 10, 20, 30, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new long[] { 14, 25, 40 }, result);
        }

        @Test
        public void testZipThreeArraysWithDefaultsNullArrays() throws Exception {
            // Test with null arrays and defaults
            long[] result = Arrays.zip((long[]) null, (long[]) null, (long[]) null, 10L, 20L, 30L, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new long[] {}, result);

            // Test with one null
            long[] a = { 1, 2 };
            long[] result2 = Arrays.zip(a, null, null, 10L, 20L, 30L, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new long[] { 51, 52 }, result2);
        }

        // Test zip(long[][], long[][], LongBiFunction)
        @Test
        public void testZip2DArrays() throws Exception {
            // Test with normal two-dimensional arrays
            long[][] a = { { 1, 2 }, { 3, 4, 5 } };
            long[][] b = { { 5, 6, 7 }, { 8, 9 } };
            long[][] result = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertEquals(2, result.length);
            Assertions.assertArrayEquals(new long[] { 6, 8 }, result[0]);
            Assertions.assertArrayEquals(new long[] { 11, 13 }, result[1]);
        }

        @Test
        public void testZip2DArraysDifferentOuterLengths() throws Exception {
            // Test with different outer array lengths
            long[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            long[][] b = { { 7, 8 }, { 9, 10 } };
            long[][] result = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertEquals(2, result.length);
            Assertions.assertArrayEquals(new long[] { 8, 10 }, result[0]);
            Assertions.assertArrayEquals(new long[] { 12, 14 }, result[1]);
        }

        @Test
        public void testZip2DArraysWithNull() throws Exception {
            // Test with null two-dimensional arrays
            long[][] result = Arrays.zip((long[][]) null, (long[][]) null, (x, y) -> x + y);
            Assertions.assertEquals(0, result.length);

            // Test with one null
            long[][] a = { { 1, 2 }, { 3, 4 } };
            long[][] result2 = Arrays.zip(a, null, (x, y) -> x + y);
            Assertions.assertEquals(0, result2.length);
        }

        @Test
        public void testZip2DArraysWithNullSubArrays() throws Exception {
            // Test with null sub-arrays
            long[][] a = { { 1, 2 }, null, { 3, 4 } };
            long[][] b = { { 5, 6 }, { 7, 8 }, null };
            long[][] result = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertEquals(3, result.length);
            Assertions.assertArrayEquals(new long[] { 6, 8 }, result[0]);
            Assertions.assertArrayEquals(new long[] {}, result[1]);
            Assertions.assertArrayEquals(new long[] {}, result[2]);
        }

        // Test zip(long[][], long[][], long, long, LongBiFunction)
        @Test
        public void testZip2DArraysWithDefaults() throws Exception {
            // Test with two-dimensional arrays of different lengths using defaults
            long[][] a = { { 1, 2 }, { 3, 4, 5 } };
            long[][] b = { { 5, 6, 7 }, { 8, 9 }, { 10 } };
            long[][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
            Assertions.assertEquals(3, result.length);
            Assertions.assertArrayEquals(new long[] { 6, 8, 7 }, result[0]);
            Assertions.assertArrayEquals(new long[] { 11, 13, 15 }, result[1]);
            Assertions.assertArrayEquals(new long[] { 10 }, result[2]);
        }

        @Test
        public void testZip2DArraysWithDefaultsNullArrays() throws Exception {
            // Test with null arrays and defaults
            long[][] result = Arrays.zip((long[][]) null, (long[][]) null, 10L, 20L, (x, y) -> x + y);
            Assertions.assertEquals(0, result.length);

            // Test with first null
            long[][] b = { { 5, 6 }, { 7, 8 } };
            long[][] result2 = Arrays.zip(null, b, 10L, 20L, (x, y) -> x + y);
            Assertions.assertEquals(2, result2.length);
            Assertions.assertArrayEquals(new long[] { 15, 16 }, result2[0]);
            Assertions.assertArrayEquals(new long[] { 17, 18 }, result2[1]);
        }

        // Test zip(long[][], long[][], long[][], LongTriFunction)
        @Test
        public void testZipThree2DArrays() throws Exception {
            // Test with normal two-dimensional arrays
            long[][] a = { { 1, 2 }, { 3, 4, 5 } };
            long[][] b = { { 5, 6, 7 }, { 8, 9 } };
            long[][] c = { { 10, 11 }, { 12, 13, 14 } };
            long[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            Assertions.assertEquals(2, result.length);
            Assertions.assertArrayEquals(new long[] { 16, 19 }, result[0]);
            Assertions.assertArrayEquals(new long[] { 23, 26 }, result[1]);
        }

        @Test
        public void testZipThree2DArraysDifferentLengths() throws Exception {
            // Test with different outer array lengths
            long[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            long[][] b = { { 7, 8 }, { 9, 10 } };
            long[][] c = { { 11, 12 } };
            long[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            Assertions.assertEquals(1, result.length);
            Assertions.assertArrayEquals(new long[] { 19, 22 }, result[0]);
        }

        // Test zip(long[][], long[][], long[][], long, long, long, LongTriFunction)
        @Test
        public void testZipThree2DArraysWithDefaults() throws Exception {
            // Test with two-dimensional arrays of different lengths using defaults
            long[][] a = { { 1, 2 }, { 3, 4, 5 } };
            long[][] b = { { 5, 6, 7 }, { 8, 9 }, { 10 } };
            long[][] c = { { 10, 11 } };
            long[][] result = Arrays.zip(a, b, c, 0L, 10L, 20L, (x, y, z) -> x + y + z);
            Assertions.assertEquals(3, result.length);
            Assertions.assertArrayEquals(new long[] { 16, 19, 27 }, result[0]);
            Assertions.assertArrayEquals(new long[] { 31, 33, 35 }, result[1]);
            Assertions.assertArrayEquals(new long[] { 30 }, result[2]);
        }

        // Test zip(long[][][], long[][][], LongBiFunction)
        @Test
        public void testZip3DArrays() throws Exception {
            // Test with normal three-dimensional arrays
            long[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } } };
            long[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 }, { 70, 80 } } };
            long[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertEquals(2, result.length);
            Assertions.assertEquals(2, result[0].length);
            Assertions.assertArrayEquals(new long[] { 11, 22 }, result[0][0]);
            Assertions.assertArrayEquals(new long[] { 33, 44 }, result[0][1]);
            Assertions.assertArrayEquals(new long[] { 55, 66 }, result[1][0]);
            Assertions.assertArrayEquals(new long[] { 77, 88 }, result[1][1]);
        }

        @Test
        public void testZip3DArraysDifferentLengths() throws Exception {
            // Test with different outer array lengths
            long[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } }, { { 9, 10 } } };
            long[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 } } };
            long[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertEquals(2, result.length);
        }

        @Test
        public void testZip3DArraysWithNull() throws Exception {
            // Test with null three-dimensional arrays
            long[][][] result = Arrays.zip((long[][][]) null, (long[][][]) null, (x, y) -> x + y);
            Assertions.assertEquals(0, result.length);
        }

        // Test zip(long[][][], long[][][], long, long, LongBiFunction)
        @Test
        public void testZip3DArraysWithDefaults() throws Exception {
            // Test with three-dimensional arrays of different lengths using defaults
            long[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 } } };
            long[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 }, { 70, 80 } }, { { 90 } } };
            long[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
            Assertions.assertEquals(3, result.length);
            Assertions.assertEquals(2, result[0].length);
            Assertions.assertArrayEquals(new long[] { 11, 22 }, result[0][0]);
            Assertions.assertArrayEquals(new long[] { 33, 44 }, result[0][1]);
            Assertions.assertEquals(2, result[1].length);
            Assertions.assertArrayEquals(new long[] { 55, 66 }, result[1][0]);
            Assertions.assertArrayEquals(new long[] { 70, 80 }, result[1][1]);
            Assertions.assertEquals(1, result[2].length);
            Assertions.assertArrayEquals(new long[] { 90 }, result[2][0]);
        }

        // Test zip(long[][][], long[][][], long[][][], LongTriFunction)
        @Test
        public void testZipThree3DArrays() throws Exception {
            // Test with normal three-dimensional arrays
            long[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } } };
            long[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 }, { 70, 80 } } };
            long[][][] c = { { { 100, 200 }, { 300, 400 } }, { { 500, 600 }, { 700, 800 } } };
            long[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            Assertions.assertEquals(2, result.length);
            Assertions.assertEquals(2, result[0].length);
            Assertions.assertArrayEquals(new long[] { 111, 222 }, result[0][0]);
            Assertions.assertArrayEquals(new long[] { 333, 444 }, result[0][1]);
            Assertions.assertArrayEquals(new long[] { 555, 666 }, result[1][0]);
            Assertions.assertArrayEquals(new long[] { 777, 888 }, result[1][1]);
        }

        @Test
        public void testZipThree3DArraysDifferentLengths() throws Exception {
            // Test with different outer array lengths
            long[][][] a = { { { 1, 2 } }, { { 3, 4 } }, { { 5, 6 } } };
            long[][][] b = { { { 10, 20 } }, { { 30, 40 } } };
            long[][][] c = { { { 100, 200 } } };
            long[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            Assertions.assertEquals(1, result.length);
        }

        // Test zip(long[][][], long[][][], long[][][], long, long, long, LongTriFunction)
        @Test
        public void testZipThree3DArraysWithDefaults() throws Exception {
            // Test with three-dimensional arrays of different lengths using defaults
            long[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 } } };
            long[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 }, { 70, 80 } }, { { 90 } } };
            long[][][] c = { { { 100, 200 } } };
            long[][][] result = Arrays.zip(a, b, c, 0L, 10L, 20L, (x, y, z) -> x + y + z);
            Assertions.assertEquals(3, result.length);
            // First element uses all three arrays
            Assertions.assertEquals(2, result[0].length);
            Assertions.assertArrayEquals(new long[] { 111, 222 }, result[0][0]);
            // Second element uses defaults for c
            Assertions.assertEquals(2, result[1].length);
            // Third element uses defaults for a and c
            Assertions.assertEquals(1, result[2].length);
        }

        @Test
        public void testZipThree3DArraysWithDefaultsAllNull() throws Exception {
            // Test with all null arrays
            long[][][] result = Arrays.zip((long[][][]) null, (long[][][]) null, (long[][][]) null, 1L, 2L, 3L, (x, y, z) -> x + y + z);
            Assertions.assertEquals(0, result.length);
        }

        // Test exception handling
        @Test
        public void testZipWithException() {
            // Test that exceptions are properly propagated
            long[] a = { 1, 2, 3 };
            long[] b = { 4, 5, 6 };

            Assertions.assertThrows(RuntimeException.class, () -> {
                Arrays.zip(a, b, (x, y) -> {
                    if (x == 2)
                        throw new RuntimeException("Test exception");
                    return x + y;
                });
            });
        }

        // Test with different zip functions
        @Test
        public void testZipWithDifferentFunctions() throws Exception {
            long[] a = { 10, 20, 30 };
            long[] b = { 3, 4, 5 };

            // Test subtraction
            long[] result1 = Arrays.zip(a, b, (x, y) -> x - y);
            Assertions.assertArrayEquals(new long[] { 7, 16, 25 }, result1);

            // Test multiplication
            long[] result2 = Arrays.zip(a, b, (x, y) -> x * y);
            Assertions.assertArrayEquals(new long[] { 30, 80, 150 }, result2);

            // Test division
            long[] result3 = Arrays.zip(a, b, (x, y) -> x / y);
            Assertions.assertArrayEquals(new long[] { 3, 5, 6 }, result3);

            // Test max
            long[] result4 = Arrays.zip(a, b, (x, y) -> Math.max(x, y));
            Assertions.assertArrayEquals(new long[] { 10, 20, 30 }, result4);
        }
    }

    @Nested
    public class FTest {
        // Tests for map(T[], Function, Class)
        @Test
        public void testMap_NullArray() throws Exception {
            String[] input = null;
            Integer[] result = f.map(input, Integer::valueOf, Integer.class);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMap_EmptyArray() throws Exception {
            String[] input = new String[0];
            Integer[] result = f.map(input, Integer::valueOf, Integer.class);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMap_StringToInteger() throws Exception {
            String[] input = { "1", "2", "3" };
            Integer[] result = f.map(input, Integer::valueOf, Integer.class);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
        }

        @Test
        public void testMap_IntegerToString() throws Exception {
            Integer[] input = { 1, 2, 3 };
            String[] result = f.map(input, String::valueOf, String.class);
            Assertions.assertArrayEquals(new String[] { "1", "2", "3" }, result);
        }

        @Test
        public void testMap_WithException() {
            String[] input = { "1", "abc", "3" };
            Assertions.assertThrows(NumberFormatException.class, () -> {
                f.map(input, Integer::valueOf, Integer.class);
            });
        }

        // Tests for mapToBoolean
        @Test
        public void testMapToBoolean_NullArray() throws Exception {
            String[] input = null;
            boolean[] result = f.mapToBoolean(input, s -> true);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToBoolean_EmptyArray() throws Exception {
            String[] input = new String[0];
            boolean[] result = f.mapToBoolean(input, s -> true);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToBoolean_StringLength() throws Exception {
            String[] input = { "hello", "hi", "world", "a" };
            boolean[] result = f.mapToBoolean(input, s -> s.length() > 3);
            Assertions.assertArrayEquals(new boolean[] { true, false, true, false }, result);
        }

        // Tests for mapToChar
        @Test
        public void testMapToChar_NullArray() throws Exception {
            String[] input = null;
            char[] result = f.mapToChar(input, s -> 'a');
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToChar_EmptyArray() throws Exception {
            String[] input = new String[0];
            char[] result = f.mapToChar(input, s -> 'a');
            Assertions.assertNotNull(result);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToChar_FirstCharacter() throws Exception {
            String[] input = { "apple", "banana", "cherry" };
            char[] result = f.mapToChar(input, s -> s.charAt(0));
            Assertions.assertArrayEquals(new char[] { 'a', 'b', 'c' }, result);
        }

        // Tests for mapToByte
        @Test
        public void testMapToByte_NullArray() throws Exception {
            String[] input = null;
            byte[] result = f.mapToByte(input, s -> (byte) 0);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToByte_EmptyArray() throws Exception {
            String[] input = new String[0];
            byte[] result = f.mapToByte(input, s -> (byte) 0);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToByte_StringToByte() throws Exception {
            String[] input = { "1", "2", "3" };
            byte[] result = f.mapToByte(input, s -> Byte.parseByte(s));
            Assertions.assertArrayEquals(new byte[] { 1, 2, 3 }, result);
        }

        // Tests for mapToShort
        @Test
        public void testMapToShort_NullArray() throws Exception {
            String[] input = null;
            short[] result = f.mapToShort(input, s -> (short) 0);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToShort_EmptyArray() throws Exception {
            String[] input = new String[0];
            short[] result = f.mapToShort(input, s -> (short) 0);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToShort_StringToShort() throws Exception {
            String[] input = { "10", "20", "30" };
            short[] result = f.mapToShort(input, s -> Short.parseShort(s));
            Assertions.assertArrayEquals(new short[] { 10, 20, 30 }, result);
        }

        // Tests for mapToInt
        @Test
        public void testMapToInt_NullArray() throws Exception {
            String[] input = null;
            int[] result = f.mapToInt(input, s -> 0);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToInt_EmptyArray() throws Exception {
            String[] input = new String[0];
            int[] result = f.mapToInt(input, s -> 0);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToInt_StringToInt() throws Exception {
            String[] input = { "10", "20", "30" };
            int[] result = f.mapToInt(input, Integer::parseInt);
            Assertions.assertArrayEquals(new int[] { 10, 20, 30 }, result);
        }

        // Tests for mapToLong
        @Test
        public void testMapToLong_NullArray() throws Exception {
            String[] input = null;
            long[] result = f.mapToLong(input, s -> 0L);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToLong_EmptyArray() throws Exception {
            String[] input = new String[0];
            long[] result = f.mapToLong(input, s -> 0L);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToLong_StringToLong() throws Exception {
            String[] input = { "100", "200", "300" };
            long[] result = f.mapToLong(input, Long::parseLong);
            Assertions.assertArrayEquals(new long[] { 100L, 200L, 300L }, result);
        }

        // Tests for mapToFloat
        @Test
        public void testMapToFloat_NullArray() throws Exception {
            String[] input = null;
            float[] result = f.mapToFloat(input, s -> 0.0f);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToFloat_EmptyArray() throws Exception {
            String[] input = new String[0];
            float[] result = f.mapToFloat(input, s -> 0.0f);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToFloat_StringToFloat() throws Exception {
            String[] input = { "1.5", "2.5", "3.5" };
            float[] result = f.mapToFloat(input, Float::parseFloat);
            Assertions.assertArrayEquals(new float[] { 1.5f, 2.5f, 3.5f }, result);
        }

        // Tests for mapToDouble
        @Test
        public void testMapToDouble_NullArray() throws Exception {
            String[] input = null;
            double[] result = f.mapToDouble(input, s -> 0.0);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToDouble_EmptyArray() throws Exception {
            String[] input = new String[0];
            double[] result = f.mapToDouble(input, s -> 0.0);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToDouble_StringToDouble() throws Exception {
            String[] input = { "1.1", "2.2", "3.3" };
            double[] result = f.mapToDouble(input, Double::parseDouble);
            Assertions.assertArrayEquals(new double[] { 1.1, 2.2, 3.3 }, result);
        }

        // Tests for mapToObj with boolean[]
        @Test
        public void testMapToObj_BooleanArray_Null() throws Exception {
            boolean[] input = null;
            String[] result = Arrays.mapToObj(input, b -> b ? "YES" : "NO", String.class);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_BooleanArray_Empty() throws Exception {
            boolean[] input = new boolean[0];
            String[] result = Arrays.mapToObj(input, b -> b ? "YES" : "NO", String.class);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_BooleanArray() throws Exception {
            boolean[] input = { true, false, true };
            String[] result = Arrays.mapToObj(input, b -> b ? "YES" : "NO", String.class);
            Assertions.assertArrayEquals(new String[] { "YES", "NO", "YES" }, result);
        }

        // Tests for mapToObj with boolean[][]
        @Test
        public void testMapToObj_Boolean2DArray_Null() throws Exception {
            boolean[][] input = null;
            String[][] result = Arrays.mapToObj(input, b -> b ? "T" : "F", String.class);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_Boolean2DArray() throws Exception {
            boolean[][] input = { { true, false }, { false, true } };
            String[][] result = Arrays.mapToObj(input, b -> b ? "T" : "F", String.class);
            Assertions.assertArrayEquals(new String[][] { { "T", "F" }, { "F", "T" } }, result);
        }

        // Tests for mapToObj with boolean[][][]
        @Test
        public void testMapToObj_Boolean3DArray_Null() throws Exception {
            boolean[][][] input = null;
            String[][][] result = Arrays.mapToObj(input, b -> b ? "1" : "0", String.class);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_Boolean3DArray() throws Exception {
            boolean[][][] input = { { { true, false } }, { { false, true } } };
            String[][][] result = Arrays.mapToObj(input, b -> b ? "1" : "0", String.class);
            Assertions.assertArrayEquals(new String[][][] { { { "1", "0" } }, { { "0", "1" } } }, result);
        }

        // Tests for mapToObj with char[]
        @Test
        public void testMapToObj_CharArray_Null() throws Exception {
            char[] input = null;
            String[] result = Arrays.mapToObj(input, c -> String.valueOf(c), String.class);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_CharArray() throws Exception {
            char[] input = { 'a', 'b', 'c' };
            String[] result = Arrays.mapToObj(input, c -> String.valueOf(c).toUpperCase(), String.class);
            Assertions.assertArrayEquals(new String[] { "A", "B", "C" }, result);
        }

        // Tests for mapToObj with char[][]
        @Test
        public void testMapToObj_Char2DArray() throws Exception {
            char[][] input = { { 'a', 'b' }, { 'c', 'd' } };
            Integer[][] result = Arrays.mapToObj(input, c -> (int) c, Integer.class);
            Assertions.assertEquals(97, result[0][0]);
            Assertions.assertEquals(98, result[0][1]);
            Assertions.assertEquals(99, result[1][0]);
            Assertions.assertEquals(100, result[1][1]);
        }

        // Tests for mapToObj with char[][][]
        @Test
        public void testMapToObj_Char3DArray() throws Exception {
            char[][][] input = { { { 'x', 'y' } }, { { 'z', 'w' } } };
            String[][][] result = Arrays.mapToObj(input, c -> String.valueOf(c), String.class);
            Assertions.assertArrayEquals(new String[][][] { { { "x", "y" } }, { { "z", "w" } } }, result);
        }

        // Tests for mapToObj with byte[]
        @Test
        public void testMapToObj_ByteArray_Null() throws Exception {
            byte[] input = null;
            String[] result = Arrays.mapToObj(input, b -> String.valueOf(b), String.class);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_ByteArray() throws Exception {
            byte[] input = { 1, 2, 3 };
            Integer[] result = Arrays.mapToObj(input, b -> (int) b, Integer.class);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
        }

        // Tests for mapToObj with byte[][]
        @Test
        public void testMapToObj_Byte2DArray() throws Exception {
            byte[][] input = { { 1, 2 }, { 3, 4 } };
            String[][] result = Arrays.mapToObj(input, b -> "B" + b, String.class);
            Assertions.assertArrayEquals(new String[][] { { "B1", "B2" }, { "B3", "B4" } }, result);
        }

        // Tests for mapToObj with byte[][][]
        @Test
        public void testMapToObj_Byte3DArray() throws Exception {
            byte[][][] input = { { { 1, 2 } }, { { 3, 4 } } };
            Integer[][][] result = Arrays.mapToObj(input, b -> b * 10, Integer.class);
            Assertions.assertArrayEquals(new Integer[][][] { { { 10, 20 } }, { { 30, 40 } } }, result);
        }

        // Tests for mapToObj with short[]
        @Test
        public void testMapToObj_ShortArray_Null() throws Exception {
            short[] input = null;
            String[] result = Arrays.mapToObj(input, s -> String.valueOf(s), String.class);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_ShortArray() throws Exception {
            short[] input = { 10, 20, 30 };
            String[] result = Arrays.mapToObj(input, s -> "S" + s, String.class);
            Assertions.assertArrayEquals(new String[] { "S10", "S20", "S30" }, result);
        }

        // Tests for mapToObj with short[][]
        @Test
        public void testMapToObj_Short2DArray() throws Exception {
            short[][] input = { { 100, 200 }, { 300, 400 } };
            Integer[][] result = Arrays.mapToObj(input, s -> s / 100, Integer.class);
            Assertions.assertArrayEquals(new Integer[][] { { 1, 2 }, { 3, 4 } }, result);
        }

        // Tests for mapToObj with short[][][]
        @Test
        public void testMapToObj_Short3DArray() throws Exception {
            short[][][] input = { { { 1, 2 } }, { { 3, 4 } } };
            Long[][][] result = Arrays.mapToObj(input, s -> (long) s * 1000, Long.class);
            Assertions.assertArrayEquals(new Long[][][] { { { 1000L, 2000L } }, { { 3000L, 4000L } } }, result);
        }

        // Tests for mapToObj with int[]
        @Test
        public void testMapToObj_IntArray_Null() throws Exception {
            int[] input = null;
            String[] result = Arrays.mapToObj(input, i -> String.valueOf(i), String.class);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_IntArray() throws Exception {
            int[] input = { 1, 2, 3 };
            String[] result = Arrays.mapToObj(input, i -> "Number: " + i, String.class);
            Assertions.assertArrayEquals(new String[] { "Number: 1", "Number: 2", "Number: 3" }, result);
        }

        // Tests for mapToObj with int[][]
        @Test
        public void testMapToObj_Int2DArray() throws Exception {
            int[][] input = { { 1, 2 }, { 3, 4 } };
            Double[][] result = Arrays.mapToObj(input, i -> i * 0.5, Double.class);
            Assertions.assertArrayEquals(new Double[][] { { 0.5, 1.0 }, { 1.5, 2.0 } }, result);
        }

        // Tests for mapToObj with int[][][]
        @Test
        public void testMapToObj_Int3DArray() throws Exception {
            int[][][] input = { { { 10, 20 } }, { { 30, 40 } } };
            String[][][] result = Arrays.mapToObj(input, i -> Integer.toHexString(i), String.class);
            Assertions.assertArrayEquals(new String[][][] { { { "a", "14" } }, { { "1e", "28" } } }, result);
        }

        // Tests for mapToObj with long[]
        @Test
        public void testMapToObj_LongArray_Null() throws Exception {
            long[] input = null;
            String[] result = Arrays.mapToObj(input, l -> String.valueOf(l), String.class);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_LongArray() throws Exception {
            long[] input = { 1000L, 2000L, 3000L };
            String[] result = Arrays.mapToObj(input, l -> l / 1000 + "k", String.class);
            Assertions.assertArrayEquals(new String[] { "1k", "2k", "3k" }, result);
        }

        // Tests for mapToObj with long[][]
        @Test
        public void testMapToObj_Long2DArray() throws Exception {
            long[][] input = { { 100L, 200L }, { 300L, 400L } };
            Integer[][] result = Arrays.mapToObj(input, l -> (int) (l / 100), Integer.class);
            Assertions.assertArrayEquals(new Integer[][] { { 1, 2 }, { 3, 4 } }, result);
        }

        // Tests for mapToObj with long[][][]
        @Test
        public void testMapToObj_Long3DArray() throws Exception {
            long[][][] input = { { { 1L, 2L } }, { { 3L, 4L } } };
            String[][][] result = Arrays.mapToObj(input, l -> "L" + l, String.class);
            Assertions.assertArrayEquals(new String[][][] { { { "L1", "L2" } }, { { "L3", "L4" } } }, result);
        }

        // Tests for mapToObj with float[]
        @Test
        public void testMapToObj_FloatArray_Null() throws Exception {
            float[] input = null;
            String[] result = Arrays.mapToObj(input, f -> String.valueOf(f), String.class);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_FloatArray() throws Exception {
            float[] input = { 1.5f, 2.5f, 3.5f };
            Integer[] result = Arrays.mapToObj(input, f -> (int) f, Integer.class);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
        }

        // Tests for mapToObj with float[][]
        @Test
        public void testMapToObj_Float2DArray() throws Exception {
            float[][] input = { { 1.1f, 2.2f }, { 3.3f, 4.4f } };
            String[][] result = Arrays.mapToObj(input, f -> String.format("%.1f", f), String.class);
            Assertions.assertArrayEquals(new String[][] { { "1.1", "2.2" }, { "3.3", "4.4" } }, result);
        }

        // Tests for mapToObj with float[][][]
        @Test
        public void testMapToObj_Float3DArray() throws Exception {
            float[][][] input = { { { 1.0f, 2.0f } }, { { 3.0f, 4.0f } } };
            Long[][][] result = Arrays.mapToObj(input, f -> (long) f, Long.class);
            Assertions.assertArrayEquals(new Long[][][] { { { 1L, 2L } }, { { 3L, 4L } } }, result);
        }

        // Tests for mapToObj with double[]
        @Test
        public void testMapToObj_DoubleArray_Null() throws Exception {
            double[] input = null;
            String[] result = Arrays.mapToObj(input, d -> String.valueOf(d), String.class);
            Assertions.assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_DoubleArray() throws Exception {
            double[] input = { 1.5, 2.5, 3.5 };
            String[] result = Arrays.mapToObj(input, d -> String.format("%.1f", d), String.class);
            Assertions.assertArrayEquals(new String[] { "1.5", "2.5", "3.5" }, result);
        }

        // Tests for mapToObj with double[][]
        @Test
        public void testMapToObj_Double2DArray() throws Exception {
            double[][] input = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            Integer[][] result = Arrays.mapToObj(input, d -> (int) Math.round(d), Integer.class);
            Assertions.assertArrayEquals(new Integer[][] { { 2, 3 }, { 4, 5 } }, result);
        }

        // Tests for mapToObj with double[][][]
        @Test
        public void testMapToObj_Double3DArray() throws Exception {
            double[][][] input = { { { 1.1, 2.2 } }, { { 3.3, 4.4 } } };
            Integer[][][] result = Arrays.mapToObj(input, d -> (int) d, Integer.class);
            Assertions.assertArrayEquals(new Integer[][][] { { { 1, 2 } }, { { 3, 4 } } }, result);
        }

        // Edge cases and complex scenarios
        @Test
        public void testMap_WithNullElements() throws Exception {
            String[] input = { "1", null, "3" };
            String[] result = f.map(input, s -> s == null ? "NULL" : s, String.class);
            Assertions.assertArrayEquals(new String[] { "1", "NULL", "3" }, result);
        }

        @Test
        public void testMapToObj_LargeArray() throws Exception {
            int size = 10000;
            int[] input = new int[size];
            for (int i = 0; i < size; i++) {
                input[i] = i;
            }
            String[] result = Arrays.mapToObj(input, i -> String.valueOf(i), String.class);
            Assertions.assertEquals(size, result.length);
            Assertions.assertEquals("0", result[0]);
            Assertions.assertEquals("9999", result[9999]);
        }

        @Test
        public void testMap_ChainedTransformations() throws Exception {
            // Test chaining multiple transformations
            String[] input = { "1", "2", "3" };
            Integer[] intermediate = f.map(input, Integer::valueOf, Integer.class);
            String[] result = f.map(intermediate, i -> "Value: " + (i * 2), String.class);
            Assertions.assertArrayEquals(new String[] { "Value: 2", "Value: 4", "Value: 6" }, result);
        }

        // Tests for mapToLong methods
        @Test
        public void testMapToLong_IntArray() {
            // Test with normal array
            int[] input = { 1, 2, 3, 4, 5 };
            long[] result = Arrays.mapToLong(input, i -> i * 1000L);
            Assertions.assertArrayEquals(new long[] { 1000L, 2000L, 3000L, 4000L, 5000L }, result);

            // Test with empty array
            int[] emptyInput = {};
            long[] emptyResult = Arrays.mapToLong(emptyInput, i -> i * 1000L);
            Assertions.assertArrayEquals(new long[] {}, emptyResult);

            // Test with null array
            int[] nullInput = null;
            long[] nullResult = Arrays.mapToLong(nullInput, i -> i * 1000L);
            Assertions.assertEquals(0, nullResult.length);

            // Test with single element
            int[] singleInput = { 42 };
            long[] singleResult = Arrays.mapToLong(singleInput, i -> i * 100L);
            Assertions.assertArrayEquals(new long[] { 4200L }, singleResult);

            // Test with negative values
            int[] negativeInput = { -1, -2, -3 };
            long[] negativeResult = Arrays.mapToLong(negativeInput, i -> i * 1000L);
            Assertions.assertArrayEquals(new long[] { -1000L, -2000L, -3000L }, negativeResult);
        }

        @Test
        public void testMapToLong_IntArray_WithException() {
            int[] input = { 1, 2, 3 };
            Throwables.IntToLongFunction<Exception> mapper = i -> {
                if (i == 2)
                    throw new Exception("Test exception");
                return i * 1000L;
            };

            Assertions.assertThrows(Exception.class, () -> Arrays.mapToLong(input, mapper));
        }

        @Test
        public void testMapToLong_Int2DArray() {
            // Test with normal two-dimensional array
            int[][] input = { { 1, 2 }, { 3, 4 }, { 5 } };
            long[][] result = Arrays.mapToLong(input, (int i) -> i * 100L);
            Assertions.assertArrayEquals(new long[] { 100L, 200L }, result[0]);
            Assertions.assertArrayEquals(new long[] { 300L, 400L }, result[1]);
            Assertions.assertArrayEquals(new long[] { 500L }, result[2]);

            // Test with empty two-dimensional array
            int[][] emptyInput = {};
            long[][] emptyResult = Arrays.mapToLong(emptyInput, (int i) -> i * 100L);
            Assertions.assertEquals(0, emptyResult.length);

            // Test with null two-dimensional array
            int[][] nullInput = null;
            long[][] nullResult = Arrays.mapToLong(nullInput, (int i) -> i * 100L);
            Assertions.assertEquals(0, nullResult.length);

            // Test with jagged array containing empty arrays
            int[][] jaggedInput = { { 1, 2 }, {}, { 3 } };
            long[][] jaggedResult = Arrays.mapToLong(jaggedInput, (int i) -> i * 10L);
            Assertions.assertArrayEquals(new long[] { 10L, 20L }, jaggedResult[0]);
            Assertions.assertArrayEquals(new long[] {}, jaggedResult[1]);
            Assertions.assertArrayEquals(new long[] { 30L }, jaggedResult[2]);
        }

        @Test
        public void testMapToLong_Int3DArray() {
            // Test with normal three-dimensional array
            int[][][] input = { { { 1, 2 }, { 3 } }, { { 4, 5, 6 } } };
            long[][][] result = Arrays.mapToLong(input, (int i) -> i * 10L);
            Assertions.assertArrayEquals(new long[] { 10L, 20L }, result[0][0]);
            Assertions.assertArrayEquals(new long[] { 30L }, result[0][1]);
            Assertions.assertArrayEquals(new long[] { 40L, 50L, 60L }, result[1][0]);

            // Test with null three-dimensional array
            int[][][] nullInput = null;
            long[][][] nullResult = Arrays.mapToLong(nullInput, (int i) -> i * 10L);
            Assertions.assertEquals(0, nullResult.length);

            // Test with empty three-dimensional array
            int[][][] emptyInput = {};
            long[][][] emptyResult = Arrays.mapToLong(emptyInput, (int i) -> i * 10L);
            Assertions.assertEquals(0, emptyResult.length);
        }

        // Tests for mapToDouble methods with int arrays
        @Test
        public void testMapToDouble_IntArray() {
            // Test with normal array
            int[] input = { 1, 2, 3, 4 };
            double[] result = Arrays.mapToDouble(input, i -> i / 2.0);
            Assertions.assertArrayEquals(new double[] { 0.5, 1.0, 1.5, 2.0 }, result);

            // Test with empty array
            int[] emptyInput = {};
            double[] emptyResult = Arrays.mapToDouble(emptyInput, i -> i / 2.0);
            Assertions.assertArrayEquals(new double[] {}, emptyResult);

            // Test with null array
            int[] nullInput = null;
            double[] nullResult = Arrays.mapToDouble(nullInput, i -> i / 2.0);
            Assertions.assertEquals(0, nullResult.length);

            // Test with mathematical operations
            int[] mathInput = { 10, 20, 30 };
            double[] mathResult = Arrays.mapToDouble(mathInput, i -> Math.sqrt(i));
            Assertions.assertEquals(Math.sqrt(10), mathResult[0], 0.0001);
            Assertions.assertEquals(Math.sqrt(20), mathResult[1], 0.0001);
            Assertions.assertEquals(Math.sqrt(30), mathResult[2], 0.0001);
        }

        @Test
        public void testMapToDouble_Int2DArray() {
            // Test with normal two-dimensional array
            int[][] input = { { 1, 2 }, { 3, 4 } };
            double[][] result = Arrays.mapToDouble(input, (int i) -> i * 0.5);
            Assertions.assertArrayEquals(new double[] { 0.5, 1.0 }, result[0]);
            Assertions.assertArrayEquals(new double[] { 1.5, 2.0 }, result[1]);

            // Test with null two-dimensional array
            int[][] nullInput = null;
            double[][] nullResult = Arrays.mapToDouble(nullInput, (int i) -> i * 0.5);
            Assertions.assertEquals(0, nullResult.length);
        }

        @Test
        public void testMapToDouble_Int3DArray() {
            // Test with normal three-dimensional array
            int[][][] input = { { { 1, 2 } }, { { 3, 4 }, { 5, 6 } } };
            double[][][] result = Arrays.mapToDouble(input, (int i) -> i * 0.1);
            Assertions.assertArrayEquals(new double[] { 0.1, 0.2 }, result[0][0], 0.000001);
            Assertions.assertArrayEquals(new double[] { 0.3, 0.4 }, result[1][0], 0.000001);
            Assertions.assertArrayEquals(new double[] { 0.5, 0.6 }, result[1][1], 0.000001);

            // Test with null three-dimensional array
            int[][][] nullInput = null;
            double[][][] nullResult = Arrays.mapToDouble(nullInput, (int i) -> i * 0.1);
            Assertions.assertEquals(0, nullResult.length);
        }

        // Tests for mapToInt methods with long arrays
        @Test
        public void testMapToInt_LongArray() {
            // Test with normal array
            long[] input = { 1000L, 2000L, 3000L };
            int[] result = Arrays.mapToInt(input, l -> (int) (l / 1000));
            Assertions.assertArrayEquals(new int[] { 1, 2, 3 }, result);

            // Test with empty array
            long[] emptyInput = {};
            int[] emptyResult = Arrays.mapToInt(emptyInput, l -> (int) (l / 1000));
            Assertions.assertArrayEquals(new int[] {}, emptyResult);

            // Test with null array
            long[] nullInput = null;
            int[] nullResult = Arrays.mapToInt(nullInput, l -> (int) (l / 1000));
            Assertions.assertEquals(0, nullResult.length);

            // Test with overflow scenario
            long[] overflowInput = { Long.MAX_VALUE, 0L, Long.MIN_VALUE };
            int[] overflowResult = Arrays.mapToInt(overflowInput, l -> (int) (l));
            Assertions.assertEquals(-1, overflowResult[0]); // overflow
            Assertions.assertEquals(0, overflowResult[1]);
            Assertions.assertEquals(0, overflowResult[2]); // overflow
        }

        @Test
        public void testMapToInt_Long2DArray() {
            // Test with normal two-dimensional array
            long[][] input = { { 100L, 200L }, { 300L } };
            int[][] result = Arrays.mapToInt(input, (long l) -> (int) (l / 100));
            Assertions.assertArrayEquals(new int[] { 1, 2 }, result[0]);
            Assertions.assertArrayEquals(new int[] { 3 }, result[1]);

            // Test with null two-dimensional array
            long[][] nullInput = null;
            int[][] nullResult = Arrays.mapToInt(nullInput, (long l) -> (int) (l / 100));
            Assertions.assertEquals(0, nullResult.length);
        }

        @Test
        public void testMapToInt_Long3DArray() {
            // Test with normal three-dimensional array
            long[][][] input = { { { 1000L, 2000L } } };
            int[][][] result = Arrays.mapToInt(input, (long l) -> (int) (l / 1000));
            Assertions.assertArrayEquals(new int[] { 1, 2 }, result[0][0]);

            // Test with null three-dimensional array
            long[][][] nullInput = null;
            int[][][] nullResult = Arrays.mapToInt(nullInput, (long l) -> (int) (l / 1000));
            Assertions.assertEquals(0, nullResult.length);
        }

        // Tests for mapToDouble methods with long arrays
        @Test
        public void testMapToDouble_LongArray() {
            // Test with normal array
            long[] input = { 1L, 2L, 3L };
            double[] result = Arrays.mapToDouble(input, l -> l * 0.5);
            Assertions.assertArrayEquals(new double[] { 0.5, 1.0, 1.5 }, result);

            // Test with empty array
            long[] emptyInput = {};
            double[] emptyResult = Arrays.mapToDouble(emptyInput, l -> l * 0.5);
            Assertions.assertArrayEquals(new double[] {}, emptyResult);

            // Test with null array
            long[] nullInput = null;
            double[] nullResult = Arrays.mapToDouble(nullInput, l -> l * 0.5);
            Assertions.assertEquals(0, nullResult.length);
        }

        @Test
        public void testMapToDouble_Long2DArray() {
            // Test with normal two-dimensional array
            long[][] input = { { 10L, 20L }, { 30L } };
            double[][] result = Arrays.mapToDouble(input, (long l) -> l * 0.1);
            Assertions.assertArrayEquals(new double[] { 1.0, 2.0 }, result[0]);
            Assertions.assertArrayEquals(new double[] { 3.0 }, result[1]);

            // Test with null two-dimensional array
            long[][] nullInput = null;
            double[][] nullResult = Arrays.mapToDouble(nullInput, (long l) -> l * 0.1);
            Assertions.assertEquals(0, nullResult.length);
        }

        @Test
        public void testMapToDouble_Long3DArray() {
            // Test with normal three-dimensional array
            long[][][] input = { { { 100L, 200L } }, { { 300L } } };
            double[][][] result = Arrays.mapToDouble(input, (long l) -> l * 0.01d);
            Assertions.assertArrayEquals(new double[] { 1.0, 2.0 }, result[0][0]);
            Assertions.assertArrayEquals(new double[] { 3.0 }, result[1][0]);

            // Test with null three-dimensional array
            long[][][] nullInput = null;
            double[][][] nullResult = Arrays.mapToDouble(nullInput, (long l) -> l * 0.01d);
            Assertions.assertEquals(0, nullResult.length);
        }

        // Tests for mapToInt methods with double arrays
        @Test
        public void testMapToInt_DoubleArray() {
            // Test with normal array - rounding
            double[] input = { 1.7, 2.3, 3.9 };
            int[] result = Arrays.mapToInt(input, d -> (int) Math.round(d));
            Assertions.assertArrayEquals(new int[] { 2, 2, 4 }, result);

            // Test with truncation
            double[] truncInput = { 1.9, 2.1, 3.7 };
            int[] truncResult = Arrays.mapToInt(truncInput, d -> (int) d);
            Assertions.assertArrayEquals(new int[] { 1, 2, 3 }, truncResult);

            // Test with empty array
            double[] emptyInput = {};
            int[] emptyResult = Arrays.mapToInt(emptyInput, d -> (int) d);
            Assertions.assertArrayEquals(new int[] {}, emptyResult);

            // Test with null array
            double[] nullInput = null;
            int[] nullResult = Arrays.mapToInt(nullInput, d -> (int) d);
            Assertions.assertEquals(0, nullResult.length);

            // Test with negative values
            double[] negativeInput = { 1.7, 2.3, 3.9 };
            int[] negativeResult = Arrays.mapToInt(negativeInput, d -> (int) Math.round(d));
            Assertions.assertArrayEquals(new int[] { 2, 2, 4 }, negativeResult);
        }

        @Test
        public void testMapToInt_Double2DArray() {
            // Test with normal two-dimensional array
            double[][] input = { { 1.5, 2.5 }, { 3.5 } };
            int[][] result = Arrays.mapToInt(input, (double d) -> (int) Math.round(d));
            Assertions.assertArrayEquals(new int[] { 2, 3 }, result[0]);
            Assertions.assertArrayEquals(new int[] { 4 }, result[1]);

            // Test with null two-dimensional array
            double[][] nullInput = null;
            int[][] nullResult = Arrays.mapToInt(nullInput, (double d) -> (int) d);
            Assertions.assertEquals(0, nullResult.length);
        }

        @Test
        public void testMapToInt_Double3DArray() {
            // Test with normal three-dimensional array
            double[][][] input = { { { 1.1, 2.9 } }, { { 3.5 } } };
            int[][][] result = Arrays.mapToInt(input, (double d) -> (int) Math.round(d));
            Assertions.assertArrayEquals(new int[] { 1, 3 }, result[0][0]);
            Assertions.assertArrayEquals(new int[] { 4 }, result[1][0]);

            // Test with null three-dimensional array
            double[][][] nullInput = null;
            int[][][] nullResult = Arrays.mapToInt(nullInput, (double d) -> (int) d);
            Assertions.assertEquals(0, nullResult.length);
        }

        // Tests for mapToLong methods with double arrays
        @Test
        public void testMapToLong_DoubleArray() {
            // Test with normal array
            double[] input = { 1.5, 2.7, 3.2 };
            long[] result = Arrays.mapToLong(input, d -> Math.round(d));
            Assertions.assertArrayEquals(new long[] { 2L, 3L, 3L }, result);

            // Test with empty array
            double[] emptyInput = {};
            long[] emptyResult = Arrays.mapToLong(emptyInput, d -> Math.round(d));
            Assertions.assertArrayEquals(new long[] {}, emptyResult);

            // Test with null array
            double[] nullInput = null;
            long[] nullResult = Arrays.mapToLong(nullInput, d -> Math.round(d));
            Assertions.assertEquals(0, nullResult.length);

            // Test with large values
            double[] largeInput = { 1e10, 2e10, 3e10 };
            long[] largeResult = Arrays.mapToLong(largeInput, d -> (long) d);
            Assertions.assertArrayEquals(new long[] { 10000000000L, 20000000000L, 30000000000L }, largeResult);
        }

        @Test
        public void testMapToLong_Double2DArray() {
            // Test with normal two-dimensional array
            double[][] input = { { 1.1, 2.2 }, { 3.3 } };
            long[][] result = Arrays.mapToLong(input, (double d) -> Math.round(d));
            Assertions.assertArrayEquals(new long[] { 1L, 2L }, result[0]);
            Assertions.assertArrayEquals(new long[] { 3L }, result[1]);

            // Test with null two-dimensional array
            double[][] nullInput = null;
            long[][] nullResult = Arrays.mapToLong(nullInput, (double d) -> Math.round(d));
            Assertions.assertEquals(0, nullResult.length);
        }

        @Test
        public void testMapToLong_Double3DArray() {
            // Test with normal three-dimensional array
            double[][][] input = { { { 10.5, 20.5 } }, { { 30.5 } } };
            long[][][] result = Arrays.mapToLong(input, (double d) -> Math.round(d));
            Assertions.assertArrayEquals(new long[] { 11L, 21L }, result[0][0]);
            Assertions.assertArrayEquals(new long[] { 31L }, result[1][0]);

            // Test with null three-dimensional array
            double[][][] nullInput = null;
            long[][][] nullResult = Arrays.mapToLong(nullInput, (double d) -> Math.round(d));
            Assertions.assertEquals(0, nullResult.length);
        }

        // Tests for f.mapToChar methods
        @Test
        public void testMapToChar_ObjectArray() {
            // Test with String array
            String[] input = { "Hello", "World", "Test" };
            char[] result = f.mapToChar(input, s -> s.charAt(0));
            Assertions.assertArrayEquals(new char[] { 'H', 'W', 'T' }, result);

            // Test with empty array
            String[] emptyInput = {};
            char[] emptyResult = f.mapToChar(emptyInput, s -> s.charAt(0));
            Assertions.assertArrayEquals(new char[] {}, emptyResult);

            // Test with null array
            String[] nullInput = null;
            char[] nullResult = f.mapToChar(nullInput, s -> s.charAt(0));
            Assertions.assertEquals(0, nullResult.length);

            // Test with Integer array to char
            Integer[] intInput = { 65, 66, 67 }; // ASCII values for A, B, C
            char[] charResult = f.mapToChar(intInput, i -> (char) i.intValue());
            Assertions.assertArrayEquals(new char[] { 'A', 'B', 'C' }, charResult);
        }

        @Test
        public void testMapToChar_Object2DArray() {
            // Test with String two-dimensional array
            String[][] input = { { "Apple", "Banana" }, { "Cherry" } };
            char[][] result = ff.mapToChar(input, s -> s.charAt(0));
            Assertions.assertArrayEquals(new char[] { 'A', 'B' }, result[0]);
            Assertions.assertArrayEquals(new char[] { 'C' }, result[1]);

            // Test with null two-dimensional array
            String[][] nullInput = null;
            char[][] nullResult = ff.mapToChar(nullInput, s -> s.charAt(0));
            Assertions.assertEquals(0, nullResult.length);
        }

        @Test
        public void testMapToChar_Object3DArray() {
            // Test with String three-dimensional array
            String[][][] input = { { { "Dog", "Elephant" } }, { { "Fox" } } };
            char[][][] result = fff.mapToChar(input, s -> s.charAt(0));
            Assertions.assertArrayEquals(new char[] { 'D', 'E' }, result[0][0]);
            Assertions.assertArrayEquals(new char[] { 'F' }, result[1][0]);

            // Test with null three-dimensional array
            String[][][] nullInput = null;
            char[][][] nullResult = fff.mapToChar(nullInput, s -> s.charAt(0));
            Assertions.assertEquals(0, nullResult.length);
        }

        // Tests for f.mapToByte methods
        @Test
        public void testMapToByte_ObjectArray() {
            // Test with Integer array
            Integer[] input = { 10, 20, 30, 127, -128 };
            byte[] result = f.mapToByte(input, i -> i.byteValue());
            Assertions.assertArrayEquals(new byte[] { 10, 20, 30, 127, -128 }, result);

            // Test with empty array
            Integer[] emptyInput = {};
            byte[] emptyResult = f.mapToByte(emptyInput, i -> i.byteValue());
            Assertions.assertArrayEquals(new byte[] {}, emptyResult);

            // Test with null array
            Integer[] nullInput = null;
            byte[] nullResult = f.mapToByte(nullInput, i -> i.byteValue());
            Assertions.assertEquals(0, nullResult.length);

            // Test with String array containing numbers
            String[] strInput = { "1", "2", "3" };
            byte[] byteResult = f.mapToByte(strInput, s -> Byte.parseByte(s));
            Assertions.assertArrayEquals(new byte[] { 1, 2, 3 }, byteResult);
        }

        @Test
        public void testMapToByte_Object2DArray() {
            // Test with Integer two-dimensional array
            Integer[][] input = { { 10, 20 }, { 30 } };
            byte[][] result = ff.mapToByte(input, i -> i.byteValue());
            Assertions.assertArrayEquals(new byte[] { 10, 20 }, result[0]);
            Assertions.assertArrayEquals(new byte[] { 30 }, result[1]);

            // Test with null two-dimensional array
            Integer[][] nullInput = null;
            byte[][] nullResult = ff.mapToByte(nullInput, i -> i.byteValue());
            Assertions.assertEquals(0, nullResult.length);
        }

        @Test
        public void testMapToByte_Object3DArray() {
            // Test with Integer three-dimensional array
            Integer[][][] input = { { { 1, 2 } }, { { 3 } } };
            byte[][][] result = fff.mapToByte(input, i -> i.byteValue());
            Assertions.assertArrayEquals(new byte[] { 1, 2 }, result[0][0]);
            Assertions.assertArrayEquals(new byte[] { 3 }, result[1][0]);

            // Test with null three-dimensional array
            Integer[][][] nullInput = null;
            byte[][][] nullResult = fff.mapToByte(nullInput, i -> i.byteValue());
            Assertions.assertEquals(0, nullResult.length);
        }

        // Tests for f.mapToShort methods
        @Test
        public void testMapToShort_ObjectArray() {
            // Test with Integer array
            Integer[] input = { 100, 200, 300, 32767, -32768 };
            short[] result = f.mapToShort(input, i -> i.shortValue());
            Assertions.assertArrayEquals(new short[] { 100, 200, 300, 32767, -32768 }, result);

            // Test with empty array
            Integer[] emptyInput = {};
            short[] emptyResult = f.mapToShort(emptyInput, i -> i.shortValue());
            Assertions.assertArrayEquals(new short[] {}, emptyResult);

            // Test with null array
            Integer[] nullInput = null;
            short[] nullResult = f.mapToShort(nullInput, i -> i.shortValue());
            Assertions.assertEquals(0, nullResult.length);

            // Test with String array containing numbers
            String[] strInput = { "1000", "2000", "3000" };
            short[] shortResult = f.mapToShort(strInput, s -> Short.parseShort(s));
            Assertions.assertArrayEquals(new short[] { 1000, 2000, 3000 }, shortResult);
        }

        @Test
        public void testMapToShort_Object2DArray() {
            // Test with Integer two-dimensional array
            Integer[][] input = { { 1000, 2000 }, { 3000 } };
            short[][] result = ff.mapToShort(input, i -> i.shortValue());
            Assertions.assertArrayEquals(new short[] { 1000, 2000 }, result[0]);
            Assertions.assertArrayEquals(new short[] { 3000 }, result[1]);

            // Test with null two-dimensional array
            Integer[][] nullInput = null;
            short[][] nullResult = ff.mapToShort(nullInput, i -> i.shortValue());
            Assertions.assertEquals(0, nullResult.length);
        }

        @Test
        public void testMapToShort_Object3DArray() {
            // Test with Integer three-dimensional array
            Integer[][][] input = { { { 100, 200 } }, { { 300 } } };
            short[][][] result = fff.mapToShort(input, i -> i.shortValue());
            Assertions.assertArrayEquals(new short[] { 100, 200 }, result[0][0]);
            Assertions.assertArrayEquals(new short[] { 300 }, result[1][0]);

            // Test with null three-dimensional array
            Integer[][][] nullInput = null;
            short[][][] nullResult = fff.mapToShort(nullInput, i -> i.shortValue());
            Assertions.assertEquals(0, nullResult.length);
        }

        // Tests for zip methods with mathematical operations
        @Test
        public void testZip_IntArrays_MathematicalOperations() {
            int[] a = { 10, 20, 30 };
            int[] b = { 1, 2, 3 };

            // Addition
            int[] addResult = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertArrayEquals(new int[] { 11, 22, 33 }, addResult);

            // Subtraction
            int[] subResult = Arrays.zip(a, b, (x, y) -> x - y);
            Assertions.assertArrayEquals(new int[] { 9, 18, 27 }, subResult);

            // Multiplication
            int[] mulResult = Arrays.zip(a, b, (x, y) -> x * y);
            Assertions.assertArrayEquals(new int[] { 10, 40, 90 }, mulResult);

            // Division
            int[] divResult = Arrays.zip(a, b, (x, y) -> x / y);
            Assertions.assertArrayEquals(new int[] { 10, 10, 10 }, divResult);

            // Maximum
            int[] maxResult = Arrays.zip(a, b, (x, y) -> Math.max(x, y));
            Assertions.assertArrayEquals(new int[] { 10, 20, 30 }, maxResult);

            // Minimum
            int[] minResult = Arrays.zip(a, b, (x, y) -> Math.min(x, y));
            Assertions.assertArrayEquals(new int[] { 1, 2, 3 }, minResult);
        }

        @Test
        public void testZip_IntArrays_WithDefaults_MathematicalOperations() {
            int[] a = { 5, 10 };
            int[] b = { 1, 2, 3, 4 };

            // Addition with defaults
            int[] addResult = Arrays.zip(a, b, 0, 0, (x, y) -> x + y);
            Assertions.assertArrayEquals(new int[] { 6, 12, 3, 4 }, addResult);

            // Multiplication with defaults - one array shorter
            int[] mulResult = Arrays.zip(a, b, 1, 1, (x, y) -> x * y);
            Assertions.assertArrayEquals(new int[] { 5, 20, 3, 4 }, mulResult);

            // Test with different defaults
            int[] customResult = Arrays.zip(a, b, 100, 200, (x, y) -> x + y);
            Assertions.assertArrayEquals(new int[] { 6, 12, 103, 104 }, customResult);
        }

        @Test
        public void testZip_IntArrays_ThreeArrays() {
            int[] a = { 1, 2, 3 };
            int[] b = { 10, 20, 30 };
            int[] c = { 100, 200, 300 };

            // Three-way addition
            int[] addResult = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new int[] { 111, 222, 333 }, addResult);

            // Three-way multiplication
            int[] mulResult = Arrays.zip(a, b, c, (x, y, z) -> x * y * z);
            Assertions.assertArrayEquals(new int[] { 1000, 8000, 27000 }, mulResult);

            // Complex mathematical operation
            int[] complexResult = Arrays.zip(a, b, c, (x, y, z) -> (x + y) * z);
            Assertions.assertArrayEquals(new int[] { 1100, 4400, 9900 }, complexResult);
        }

        @Test
        public void testZip_IntArrays_ThreeArrays_WithDefaults() {
            int[] a = { 1, 2 };
            int[] b = { 10, 20, 30 };
            int[] c = { 100 };

            // Three arrays with different lengths and defaults
            int[] result = Arrays.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new int[] { 111, 22, 30 }, result);

            // Test with non-zero defaults
            int[] result2 = Arrays.zip(a, b, c, 5, 15, 500, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new int[] { 111, 522, 535 }, result2);
        }

        @Test
        public void testZip_Int2DArrays_MathematicalOperations() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            int[][] b = { { 10, 20 }, { 30, 40 } };

            // two-dimensional addition
            int[][] addResult = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertArrayEquals(new int[] { 11, 22 }, addResult[0]);
            Assertions.assertArrayEquals(new int[] { 33, 44 }, addResult[1]);

            // two-dimensional multiplication
            int[][] mulResult = Arrays.zip(a, b, (x, y) -> x * y);
            Assertions.assertArrayEquals(new int[] { 10, 40 }, mulResult[0]);
            Assertions.assertArrayEquals(new int[] { 90, 160 }, mulResult[1]);
        }

        @Test
        public void testZip_Int3DArrays_MathematicalOperations() {
            int[][][] a = { { { 1, 2 } }, { { 3, 4 } } };
            int[][][] b = { { { 10, 20 } }, { { 30, 40 } } };

            // three-dimensional addition
            int[][][] addResult = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertArrayEquals(new int[] { 11, 22 }, addResult[0][0]);
            Assertions.assertArrayEquals(new int[] { 33, 44 }, addResult[1][0]);

            // three-dimensional subtraction
            int[][][] subResult = Arrays.zip(a, b, (x, y) -> y - x);
            Assertions.assertArrayEquals(new int[] { 9, 18 }, subResult[0][0]);
            Assertions.assertArrayEquals(new int[] { 27, 36 }, subResult[1][0]);
        }

        @Test
        public void testZip_ErrorHandling_DivisionByZero() {
            int[] a = { 10, 20, 30 };
            int[] b = { 2, 0, 5 };

            // Test division by zero handling
            Assertions.assertThrows(ArithmeticException.class, () -> {
                Arrays.zip(a, b, (x, y) -> x / y);
            });
        }

        @Test
        public void testZip_EdgeCases_EmptyArrays() {
            int[] empty1 = {};
            int[] empty2 = {};

            int[] result = Arrays.zip(empty1, empty2, (x, y) -> x + y);
            Assertions.assertArrayEquals(new int[] {}, result);
        }

        @Test
        public void testZip_EdgeCases_NullArrays() {
            int[] a = null;
            int[] b = { 1, 2, 3 };

            int[] result = Arrays.zip(a, b, (x, y) -> x + y);
            Assertions.assertArrayEquals(new int[] {}, result);
        }

        @Test
        public void testZip_BoundaryValues_IntegerLimits() {
            int[] maxValues = { Integer.MAX_VALUE, Integer.MAX_VALUE - 1 };
            int[] ones = { 1, 1 };

            // Test overflow behavior
            int[] overflowResult = Arrays.zip(maxValues, ones, (x, y) -> x + y);
            Assertions.assertEquals(Integer.MIN_VALUE, overflowResult[0]); // Overflow wraps around
            Assertions.assertEquals(Integer.MAX_VALUE, overflowResult[1]);
        }

        // Additional error handling and edge case tests
        @Test
        public void testMapToLong_ErrorHandling_ExceptionInMapper() {
            int[] input = { 1, 2, 3 };
            Throwables.IntToLongFunction<RuntimeException> faultyMapper = i -> {
                if (i == 2) {
                    throw new RuntimeException("Test exception");
                }
                return i * 1000L;
            };

            Assertions.assertThrows(RuntimeException.class, () -> {
                Arrays.mapToLong(input, faultyMapper);
            });
        }

        @Test
        public void testMapToDouble_ErrorHandling_ExceptionInMapper() {
            long[] input = { 1L, 2L, 3L };
            Throwables.LongToDoubleFunction<IllegalArgumentException> faultyMapper = l -> {
                if (l == 2L) {
                    throw new IllegalArgumentException("Invalid value");
                }
                return l * 0.5;
            };

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                Arrays.mapToDouble(input, faultyMapper);
            });
        }

        @Test
        public void testMapToObj_ErrorHandling_ExceptionInMapper() {
            int[] input = { 1, 2, 3 };
            Throwables.IntFunction<String, Exception> faultyMapper = i -> {
                if (i == 2) {
                    throw new Exception("Conversion failed");
                }
                return String.valueOf(i);
            };

            Assertions.assertThrows(Exception.class, () -> {
                Arrays.mapToObj(input, faultyMapper, String.class);
            });
        }

        @Test
        public void testTypeConversion_BoundaryValues() {
            // Test byte boundary values
            Integer[] byteInput = { 127, -128, 0 };
            byte[] byteResult = f.mapToByte(byteInput, i -> i.byteValue());
            Assertions.assertArrayEquals(new byte[] { 127, -128, 0 }, byteResult);

            // Test short boundary values
            Integer[] shortInput = { 32767, -32768, 0 };
            short[] shortResult = f.mapToShort(shortInput, i -> i.shortValue());
            Assertions.assertArrayEquals(new short[] { 32767, -32768, 0 }, shortResult);

            // Test char boundary values
            Integer[] charInput = { 0, 65535 }; // Character range
            char[] charResult = f.mapToChar(charInput, i -> (char) i.intValue());
            Assertions.assertArrayEquals(new char[] { 0, 65535 }, charResult);
        }

        @Test
        public void testTypeConversion_Overflow() {
            // Test int to byte overflow
            Integer[] overflowInput = { 128, 256, -129 };
            byte[] overflowResult = f.mapToByte(overflowInput, i -> i.byteValue());
            // Java wraps around on overflow
            Assertions.assertArrayEquals(new byte[] { -128, 0, 127 }, overflowResult);

            // Test int to short overflow
            Integer[] shortOverflowInput = { 32768, -32769 };
            short[] shortOverflowResult = f.mapToShort(shortOverflowInput, i -> i.shortValue());
            Assertions.assertArrayEquals(new short[] { -32768, 32767 }, shortOverflowResult);
        }

        @Test
        public void testArrayOperations_LargeArrays() {
            // Test with moderately large arrays to check performance and correctness
            int size = 1000;
            int[] largeArray = new int[size];
            for (int i = 0; i < size; i++) {
                largeArray[i] = i;
            }

            // Test mapToLong with large array
            long[] longResult = Arrays.mapToLong(largeArray, i -> (long) i * i);
            Assertions.assertEquals(size, longResult.length);
            Assertions.assertEquals(0L, longResult[0]);
            Assertions.assertEquals(998001L, longResult[999]); // 999^2

            // Test mapToDouble with large array
            double[] doubleResult = Arrays.mapToDouble(largeArray, i -> Math.sqrt(i));
            Assertions.assertEquals(size, doubleResult.length);
            Assertions.assertEquals(0.0, doubleResult[0], 0.001);
            Assertions.assertEquals(Math.sqrt(999), doubleResult[999], 0.001);
        }

        @Test
        public void testMultidimensionalArrays_JaggedArrays() {
            // Test with jagged arrays - arrays with different sub-array lengths
            int[][] jaggedArray = { { 1, 2, 3 }, { 4 }, { 5, 6 } };
            long[][] jaggedLongResult = Arrays.mapToLong(jaggedArray, i -> (long) i * 10);

            Assertions.assertEquals(3, jaggedLongResult.length);
            Assertions.assertArrayEquals(new long[] { 10, 20, 30 }, jaggedLongResult[0]);
            Assertions.assertArrayEquals(new long[] { 40 }, jaggedLongResult[1]);
            Assertions.assertArrayEquals(new long[] { 50, 60 }, jaggedLongResult[2]);
        }

        @Test
        public void testZip_ComplexMathematicalOperations() {
            int[] a = { 2, 4, 6, 8 };
            int[] b = { 1, 3, 5, 7 };

            // Test modulo operation
            int[] modResult = Arrays.zip(a, b, (x, y) -> x % y);
            Assertions.assertArrayEquals(new int[] { 0, 1, 1, 1 }, modResult);

            // Test bitwise operations
            int[] bitwiseAndResult = Arrays.zip(a, b, (x, y) -> x & y);
            Assertions.assertArrayEquals(new int[] { 0, 0, 4, 0 }, bitwiseAndResult);

            int[] bitwiseOrResult = Arrays.zip(a, b, (x, y) -> x | y);
            Assertions.assertArrayEquals(new int[] { 3, 7, 7, 15 }, bitwiseOrResult);

            int[] bitwiseXorResult = Arrays.zip(a, b, (x, y) -> x ^ y);
            Assertions.assertArrayEquals(new int[] { 3, 7, 3, 15 }, bitwiseXorResult);
        }

        @Test
        public void testChainedOperations() {
            // Test chaining multiple array operations
            int[] original = { 1, 2, 3, 4, 5 };

            // Chain: int -> long -> double -> String
            long[] longChain = Arrays.mapToLong(original, i -> (long) i * i);
            double[] doubleChain = Arrays.mapToDouble(longChain, l -> l / 2.0);
            String[] stringChain = Arrays.mapToObj(doubleChain, d -> String.format("%.1f", d), String.class);

            Assertions.assertArrayEquals(new String[] { "0.5", "2.0", "4.5", "8.0", "12.5" }, stringChain);
        }

        // Tests for println methods
        @Test
        public void testPrintln_ObjectArray() {
            // Test with normal array
            Object[] arr = { "Hello", "World", 123 };
            String result = Arrays.printAndReturn(arr);
            Assertions.assertTrue(result.contains("Hello"));
            Assertions.assertTrue(result.contains("World"));
            Assertions.assertTrue(result.contains("123"));

            // Test with empty array
            Object[] emptyArr = {};
            String emptyResult = Arrays.printAndReturn(emptyArr);
            Assertions.assertEquals("[]", emptyResult.trim());

            // Test with null array
            Object[] nullArr = null;
            String nullResult = Arrays.printAndReturn(nullArr);
            Assertions.assertEquals("null", nullResult.trim());

            // Test with array containing nulls
            Object[] nullsArr = { "test", null, 42 };
            String nullsResult = Arrays.printAndReturn(nullsArr);
            Assertions.assertTrue(nullsResult.contains("test"));
            Assertions.assertTrue(nullsResult.contains("null"));
            Assertions.assertTrue(nullsResult.contains("42"));
        }

        @Test
        public void testPrintln_Object2DArray() {
            // Test with normal two-dimensional array
            Object[][] arr = { { "A", "B" }, { "C", "D" } };
            String result = Arrays.printAndReturn(arr);
            Assertions.assertNotNull(result);

            // Test with null two-dimensional array
            Object[][] nullArr = null;
            String nullResult = Arrays.printAndReturn(nullArr);
            Assertions.assertNotNull(nullResult);
        }

        @Test
        public void testPrintln_Object3DArray() {
            // Test with normal three-dimensional array
            Object[][][] arr = { { { "A", "B" } }, { { "C", "D" } } };
            String result = Arrays.printAndReturn(arr);
            Assertions.assertNotNull(result);

            // Test with null three-dimensional array
            Object[][][] nullArr = null;
            String nullResult = Arrays.printAndReturn(nullArr);
            Assertions.assertNotNull(nullResult);
        }

        // Tests for updateAll methods
        @Test
        public void testUpdateAll_BooleanArray() {
            // Test with normal array - negate all
            boolean[] arr = { true, false, true, false };
            Arrays.updateAll(arr, b -> !b);
            Assertions.assertArrayEquals(new boolean[] { false, true, false, true }, arr);

            // Test with empty array
            boolean[] emptyArr = {};
            Arrays.updateAll(emptyArr, b -> !b);
            Assertions.assertArrayEquals(new boolean[] {}, emptyArr);

            // Test with all true
            boolean[] allTrue = { true, true, true };
            Arrays.updateAll(allTrue, b -> false);
            Assertions.assertArrayEquals(new boolean[] { false, false, false }, allTrue);

            // Test with all false
            boolean[] allFalse = { false, false, false };
            Arrays.updateAll(allFalse, b -> true);
            Assertions.assertArrayEquals(new boolean[] { true, true, true }, allFalse);
        }

        @Test
        public void testUpdateAll_Boolean2DArray() {
            // Test with normal two-dimensional array
            boolean[][] arr = { { true, false }, { false, true } };
            Arrays.updateAll(arr, b -> !b);
            Assertions.assertArrayEquals(new boolean[] { false, true }, arr[0]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, arr[1]);

            // Test with empty two-dimensional array
            boolean[][] emptyArr = {};
            Arrays.updateAll(emptyArr, b -> !b);
            Assertions.assertEquals(0, emptyArr.length);

            // Test with jagged array
            boolean[][] jaggedArr = { { true }, { false, true, false } };
            Arrays.updateAll(jaggedArr, b -> !b);
            Assertions.assertArrayEquals(new boolean[] { false }, jaggedArr[0]);
            Assertions.assertArrayEquals(new boolean[] { true, false, true }, jaggedArr[1]);
        }

        @Test
        public void testUpdateAll_Boolean3DArray() {
            // Test with normal three-dimensional array
            boolean[][][] arr = { { { true, false } }, { { false, true }, { true, true } } };
            Arrays.updateAll(arr, b -> !b);
            Assertions.assertArrayEquals(new boolean[] { false, true }, arr[0][0]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, arr[1][0]);
            Assertions.assertArrayEquals(new boolean[] { false, false }, arr[1][1]);

            // Test with empty three-dimensional array
            boolean[][][] emptyArr = {};
            Arrays.updateAll(emptyArr, b -> !b);
            Assertions.assertEquals(0, emptyArr.length);
        }

        // Tests for replaceIf methods
        @Test
        public void testReplaceIf_BooleanArray() {
            // Test replacing all true values with false
            boolean[] arr = { true, false, true, false, true };
            Arrays.replaceIf(arr, b -> b, false);
            Assertions.assertArrayEquals(new boolean[] { false, false, false, false, false }, arr);

            // Test replacing all false values with true
            boolean[] arr2 = { true, false, true, false };
            Arrays.replaceIf(arr2, b -> !b, true);
            Assertions.assertArrayEquals(new boolean[] { true, true, true, true }, arr2);

            // Test with empty array
            boolean[] emptyArr = {};
            Arrays.replaceIf(emptyArr, b -> b, false);
            Assertions.assertArrayEquals(new boolean[] {}, emptyArr);

            // Test with no matches
            boolean[] noMatch = { false, false, false };
            Arrays.replaceIf(noMatch, b -> b, false);
            Assertions.assertArrayEquals(new boolean[] { false, false, false }, noMatch);
        }

        @Test
        public void testReplaceIf_Boolean2DArray() {
            // Test with normal two-dimensional array
            boolean[][] arr = { { true, false }, { true, true } };
            Arrays.replaceIf(arr, b -> b, false);
            Assertions.assertArrayEquals(new boolean[] { false, false }, arr[0]);
            Assertions.assertArrayEquals(new boolean[] { false, false }, arr[1]);

            // Test with empty two-dimensional array
            boolean[][] emptyArr = {};
            Arrays.replaceIf(emptyArr, b -> b, false);
            Assertions.assertEquals(0, emptyArr.length);
        }

        @Test
        public void testReplaceIf_Boolean3DArray() {
            // Test with normal three-dimensional array
            boolean[][][] arr = { { { true, false } }, { { false, true } } };
            Arrays.replaceIf(arr, b -> !b, true);
            Assertions.assertArrayEquals(new boolean[] { true, true }, arr[0][0]);
            Assertions.assertArrayEquals(new boolean[] { true, true }, arr[1][0]);

            // Test with empty three-dimensional array
            boolean[][][] emptyArr = {};
            Arrays.replaceIf(emptyArr, b -> b, false);
            Assertions.assertEquals(0, emptyArr.length);
        }

        // Tests for reshape methods
        @Test
        public void testReshape_BooleanArray_ToCols() {
            // Test normal reshape
            boolean[] arr = { true, false, true, false, true };
            boolean[][] reshaped = Arrays.reshape(arr, 2);
            Assertions.assertArrayEquals(new boolean[] { true, false }, reshaped[0]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, reshaped[1]);
            Assertions.assertArrayEquals(new boolean[] { true }, reshaped[2]);

            // Test perfect division
            boolean[] perfectArr = { true, false, true, false };
            boolean[][] perfectReshaped = Arrays.reshape(perfectArr, 2);
            Assertions.assertArrayEquals(new boolean[] { true, false }, perfectReshaped[0]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, perfectReshaped[1]);

            // Test with empty array
            boolean[] emptyArr = {};
            boolean[][] emptyReshaped = Arrays.reshape(emptyArr, 2);
            Assertions.assertEquals(0, emptyReshaped.length);

            // Test with single column
            boolean[] singleColArr = { true, false, true };
            boolean[][] singleColReshaped = Arrays.reshape(singleColArr, 1);
            Assertions.assertEquals(3, singleColReshaped.length);
            Assertions.assertArrayEquals(new boolean[] { true }, singleColReshaped[0]);
            Assertions.assertArrayEquals(new boolean[] { false }, singleColReshaped[1]);
            Assertions.assertArrayEquals(new boolean[] { true }, singleColReshaped[2]);

            // Test with more columns than elements
            boolean[] smallArr = { true, false };
            boolean[][] smallReshaped = Arrays.reshape(smallArr, 5);
            Assertions.assertEquals(1, smallReshaped.length);
            Assertions.assertArrayEquals(new boolean[] { true, false }, smallReshaped[0]);
        }

        @Test
        public void testReshape_BooleanArray_ToRowsCols() {
            // Test normal reshape
            boolean[] arr = { true, false, true, false, true, false };
            boolean[][][] reshaped = Arrays.reshape(arr, 2, 2);
            Assertions.assertArrayEquals(new boolean[] { true, false }, reshaped[0][0]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, reshaped[0][1]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, reshaped[1][0]);

            // Test with uneven division
            boolean[] unevenArr = { true, false, true, false, true };
            boolean[][][] unevenReshaped = Arrays.reshape(unevenArr, 2, 2);
            Assertions.assertArrayEquals(new boolean[] { true, false }, unevenReshaped[0][0]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, unevenReshaped[0][1]);
            Assertions.assertArrayEquals(new boolean[] { true }, unevenReshaped[1][0]);

            // Test with empty array
            boolean[] emptyArr = {};
            boolean[][][] emptyReshaped = Arrays.reshape(emptyArr, 2, 2);
            Assertions.assertEquals(0, emptyReshaped.length);

            // Test with single element blocks
            boolean[] singleArr = { true, false, true, false };
            boolean[][][] singleReshaped = Arrays.reshape(singleArr, 1, 1);
            Assertions.assertEquals(4, singleReshaped.length);
            Assertions.assertArrayEquals(new boolean[] { true }, singleReshaped[0][0]);
            Assertions.assertArrayEquals(new boolean[] { false }, singleReshaped[1][0]);
            Assertions.assertArrayEquals(new boolean[] { true }, singleReshaped[2][0]);
            Assertions.assertArrayEquals(new boolean[] { false }, singleReshaped[3][0]);
        }

        @Test
        public void testReshape_BooleanArray_ToRowsCols_InvalidInput() {
            boolean[] arr = { true, false };

            // Test with zero rows
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                Arrays.reshape(arr, 0, 2);
            });

            // Test with zero columns
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                Arrays.reshape(arr, 2, 0);
            });

            // Test with negative rows
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                Arrays.reshape(arr, -1, 2);
            });

            // Test with negative columns
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                Arrays.reshape(arr, 2, -1);
            });
        }

        // Tests for flatten methods
        @Test
        public void testFlatten_Boolean2DArray() {
            // Test normal flatten
            boolean[][] arr = { { true, false }, { true }, { false, true } };
            boolean[] flattened = Arrays.flatten(arr);
            Assertions.assertArrayEquals(new boolean[] { true, false, true, false, true }, flattened);

            // Test with empty two-dimensional array
            boolean[][] emptyArr = {};
            boolean[] emptyFlattened = Arrays.flatten(emptyArr);
            Assertions.assertArrayEquals(new boolean[] {}, emptyFlattened);

            // Test with null two-dimensional array
            boolean[][] nullArr = null;
            boolean[] nullFlattened = Arrays.flatten(nullArr);
            Assertions.assertArrayEquals(new boolean[] {}, nullFlattened);

            // Test with empty sub-arrays
            boolean[][] mixedArr = { { true, false }, {}, { true } };
            boolean[] mixedFlattened = Arrays.flatten(mixedArr);
            Assertions.assertArrayEquals(new boolean[] { true, false, true }, mixedFlattened);

            // Test with all empty sub-arrays
            boolean[][] allEmptyArr = { {}, {}, {} };
            boolean[] allEmptyFlattened = Arrays.flatten(allEmptyArr);
            Assertions.assertArrayEquals(new boolean[] {}, allEmptyFlattened);
        }

        @Test
        public void testFlatten_Boolean3DArray() {
            // Test normal flatten
            boolean[][][] arr = { { { true, false }, { true } }, { { false, true } } };
            boolean[] flattened = Arrays.flatten(arr);
            Assertions.assertArrayEquals(new boolean[] { true, false, true, false, true }, flattened);

            // Test with empty three-dimensional array
            boolean[][][] emptyArr = {};
            boolean[] emptyFlattened = Arrays.flatten(emptyArr);
            Assertions.assertArrayEquals(new boolean[] {}, emptyFlattened);

            // Test with null three-dimensional array
            boolean[][][] nullArr = null;
            boolean[] nullFlattened = Arrays.flatten(nullArr);
            Assertions.assertArrayEquals(new boolean[] {}, nullFlattened);

            // Test with mixed empty arrays
            boolean[][][] mixedArr = { { { true } }, { {} }, { { false, true } } };
            boolean[] mixedFlattened = Arrays.flatten(mixedArr);
            Assertions.assertArrayEquals(new boolean[] { true, false, true }, mixedFlattened);

            // Test with deeply nested empty arrays
            boolean[][][] deepEmptyArr = { { {}, {} }, { {} } };
            boolean[] deepEmptyFlattened = Arrays.flatten(deepEmptyArr);
            Assertions.assertArrayEquals(new boolean[] {}, deepEmptyFlattened);
        }

        // Tests for applyOnFlattened methods
        @Test
        public void testFlatOp_Boolean2DArray() {
            // Test sorting operation
            boolean[][] arr = { { true, false, true }, { false, true } };
            Arrays.applyOnFlattened(arr, flatArr -> {
                // Sort to have all false values first
                CommonUtil.sort(flatArr);
            });
            // After sorting, false values come first
            Assertions.assertArrayEquals(new boolean[] { false, false, true }, arr[0]);
            Assertions.assertArrayEquals(new boolean[] { true, true }, arr[1]);

            // Test with operation that sets all to true
            boolean[][] arr2 = { { true, false }, { false, true } };
            Arrays.applyOnFlattened(arr2, flatArr -> {
                for (int i = 0; i < flatArr.length; i++) {
                    flatArr[i] = true;
                }
            });
            Assertions.assertArrayEquals(new boolean[] { true, true }, arr2[0]);
            Assertions.assertArrayEquals(new boolean[] { true, true }, arr2[1]);

            // Test with empty array
            boolean[][] emptyArr = {};
            Arrays.applyOnFlattened(emptyArr, flatArr -> {
                // Should not be called
                Assertions.fail("Operation should not be called on empty array");
            });

            // Test with array containing empty sub-arrays
            boolean[][] mixedArr = { { true, false }, {}, { true } };
            Arrays.applyOnFlattened(mixedArr, flatArr -> {
                for (int i = 0; i < flatArr.length; i++) {
                    flatArr[i] = !flatArr[i];
                }
            });
            Assertions.assertArrayEquals(new boolean[] { false, true }, mixedArr[0]);
            Assertions.assertArrayEquals(new boolean[] {}, mixedArr[1]);
            Assertions.assertArrayEquals(new boolean[] { false }, mixedArr[2]);
        }

        @Test
        public void testFlatOp_Boolean3DArray() {
            // Test with normal three-dimensional array
            boolean[][][] arr = { { { true, false } }, { { false, true }, { true, true } } };
            Arrays.applyOnFlattened(arr, flatArr -> {
                // Reverse all values
                for (int i = 0; i < flatArr.length; i++) {
                    flatArr[i] = !flatArr[i];
                }
            });
            Assertions.assertArrayEquals(new boolean[] { false, true }, arr[0][0]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, arr[1][0]);
            Assertions.assertArrayEquals(new boolean[] { false, false }, arr[1][1]);

            // Test with empty three-dimensional array
            boolean[][][] emptyArr = {};
            Arrays.applyOnFlattened(emptyArr, flatArr -> {
                // Should not be called
                Assertions.fail("Operation should not be called on empty array");
            });

            // Test counting operation
            boolean[][][] countArr = { { { true, false, true } }, { { false, false } } };
            int[] trueCount = { 0 };
            Arrays.applyOnFlattened(countArr, flatArr -> {
                for (boolean b : flatArr) {
                    if (b)
                        trueCount[0]++;
                }
            });
            Assertions.assertEquals(2, trueCount[0]);
        }

        @Test
        public void testFlatOp_WithException() {
            boolean[][] arr = { { true, false }, { true } };

            Assertions.assertThrows(Exception.class, () -> {
                Arrays.applyOnFlattened(arr, flatArr -> {
                    throw new Exception("Test exception");
                });
            });
        }

        // Private helper method tests (testing indirectly through public methods)
        @Test
        public void testHelperMethods_EdgeCases() {
            // Test reshape with maximum integer values
            boolean[] largeArr = new boolean[10];
            boolean[][] largeReshaped = Arrays.reshape(largeArr, Integer.MAX_VALUE);
            Assertions.assertEquals(1, largeReshaped.length);
            Assertions.assertEquals(10, largeReshaped[0].length);

            // Test with single element
            boolean[] singleArr = { true };
            boolean[][] singleReshaped = Arrays.reshape(singleArr, 1);
            Assertions.assertEquals(1, singleReshaped.length);
            Assertions.assertArrayEquals(new boolean[] { true }, singleReshaped[0]);

            // Test flatten with large nested arrays
            boolean[][] largeNested = new boolean[100][100];
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    largeNested[i][j] = (i + j) % 2 == 0;
                }
            }
            boolean[] largeFlattened = Arrays.flatten(largeNested);
            Assertions.assertEquals(10000, largeFlattened.length);
        }
    }

    @Nested
    public class FFTest {

        @Test
        public void testUpdateAll() throws Exception {
            // Test with normal array
            String[][] array = { { "hello", "world" }, { "foo", "bar" } };
            ff.updateAll(array, str -> str.toUpperCase());
            Assertions.assertArrayEquals(new String[] { "HELLO", "WORLD" }, array[0]);
            Assertions.assertArrayEquals(new String[] { "FOO", "BAR" }, array[1]);

            // Test with null array
            String[][] nullArray = null;
            ff.updateAll(nullArray, str -> str.toUpperCase()); // Should not throw

            // Test with empty array
            String[][] emptyArray = {};
            ff.updateAll(emptyArray, str -> str.toUpperCase()); // Should not throw

            // Test with array containing null sub-arrays
            String[][] arrayWithNulls = { { "a", "b" }, null, { "c", "d" } };
            ff.updateAll(arrayWithNulls, str -> str.toUpperCase());
            Assertions.assertArrayEquals(new String[] { "A", "B" }, arrayWithNulls[0]);
            Assertions.assertNull(arrayWithNulls[1]);
            Assertions.assertArrayEquals(new String[] { "C", "D" }, arrayWithNulls[2]);

            // Test with array containing empty sub-arrays
            String[][] arrayWithEmpty = { { "a", "b" }, {}, { "c", "d" } };
            ff.updateAll(arrayWithEmpty, str -> str.toUpperCase());
            Assertions.assertArrayEquals(new String[] { "A", "B" }, arrayWithEmpty[0]);
            Assertions.assertArrayEquals(new String[] {}, arrayWithEmpty[1]);
            Assertions.assertArrayEquals(new String[] { "C", "D" }, arrayWithEmpty[2]);

            // Test with Integer array
            Integer[][] intArray = { { 1, 2 }, { 3, 4 } };
            ff.updateAll(intArray, i -> i * 2);
            Assertions.assertArrayEquals(new Integer[] { 2, 4 }, intArray[0]);
            Assertions.assertArrayEquals(new Integer[] { 6, 8 }, intArray[1]);
        }

        @Test
        public void testReplaceIf() throws Exception {
            // Test replacing null values
            Integer[][] array = { { 1, null, 3 }, { null, 5, 6 } };
            ff.replaceIf(array, val -> val == null, 0);
            Assertions.assertArrayEquals(new Integer[] { 1, 0, 3 }, array[0]);
            Assertions.assertArrayEquals(new Integer[] { 0, 5, 6 }, array[1]);

            // Test replacing based on condition
            Integer[][] array2 = { { 1, 2, 3 }, { 4, 5, 6 } };
            ff.replaceIf(array2, val -> val > 3, -1);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3 }, array2[0]);
            Assertions.assertArrayEquals(new Integer[] { -1, -1, -1 }, array2[1]);

            // Test with null array
            Integer[][] nullArray = null;
            ff.replaceIf(nullArray, val -> val == null, 0); // Should not throw

            // Test with empty array
            Integer[][] emptyArray = {};
            ff.replaceIf(emptyArray, val -> val == null, 0); // Should not throw

            // Test with array containing null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            ff.replaceIf(arrayWithNulls, val -> val > 2, 0);
            Assertions.assertArrayEquals(new Integer[] { 1, 2 }, arrayWithNulls[0]);
            Assertions.assertNull(arrayWithNulls[1]);
            Assertions.assertArrayEquals(new Integer[] { 0, 0 }, arrayWithNulls[2]);

            // Test with String array
            String[][] stringArray = { { "a", "b", null }, { "c", null, "d" } };
            ff.replaceIf(stringArray, val -> val == null, "empty");
            Assertions.assertArrayEquals(new String[] { "a", "b", "empty" }, stringArray[0]);
            Assertions.assertArrayEquals(new String[] { "c", "empty", "d" }, stringArray[1]);
        }

        @Test
        public void testReshape() {
            // Test normal reshape
            Integer[] array = { 1, 2, 3, 4, 5, 6, 7 };
            Integer[][] reshaped = ff.reshape(array, 3);
            Assertions.assertEquals(3, reshaped.length);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3 }, reshaped[0]);
            Assertions.assertArrayEquals(new Integer[] { 4, 5, 6 }, reshaped[1]);
            Assertions.assertArrayEquals(new Integer[] { 7 }, reshaped[2]);

            // Test exact division
            Integer[] array2 = { 1, 2, 3, 4, 5, 6 };
            Integer[][] reshaped2 = ff.reshape(array2, 2);
            Assertions.assertEquals(3, reshaped2.length);
            Assertions.assertArrayEquals(new Integer[] { 1, 2 }, reshaped2[0]);
            Assertions.assertArrayEquals(new Integer[] { 3, 4 }, reshaped2[1]);
            Assertions.assertArrayEquals(new Integer[] { 5, 6 }, reshaped2[2]);

            // Test single column
            Integer[] array3 = { 1, 2, 3 };
            Integer[][] reshaped3 = ff.reshape(array3, 1);
            Assertions.assertEquals(3, reshaped3.length);
            Assertions.assertArrayEquals(new Integer[] { 1 }, reshaped3[0]);
            Assertions.assertArrayEquals(new Integer[] { 2 }, reshaped3[1]);
            Assertions.assertArrayEquals(new Integer[] { 3 }, reshaped3[2]);

            // Test with columns equal to array length
            Integer[] array4 = { 1, 2, 3 };
            Integer[][] reshaped4 = ff.reshape(array4, 3);
            Assertions.assertEquals(1, reshaped4.length);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3 }, reshaped4[0]);

            // Test with empty array
            Integer[] emptyArray = {};
            Integer[][] reshapedEmpty = ff.reshape(emptyArray, 3);
            Assertions.assertEquals(0, reshapedEmpty.length);

            // Test invalid columns
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                ff.reshape(array, 0);
            });
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                ff.reshape(array, -1);
            });
        }

        @Test
        public void testFlatten() {
            // Test normal flatten
            Integer[][] array = { { 1, 2 }, { 3, 4, 5 }, { 6 } };
            Integer[] flattened = ff.flatten(array);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, flattened);

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            Integer[] flattened2 = ff.flatten(arrayWithNulls);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, flattened2);

            // Test with empty sub-arrays
            Integer[][] arrayWithEmpty = { { 1, 2 }, {}, { 3, 4 } };
            Integer[] flattened3 = ff.flatten(arrayWithEmpty);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, flattened3);

            // Test with empty array
            Integer[][] emptyArray = {};
            Integer[] flattened4 = ff.flatten(emptyArray);
            Assertions.assertArrayEquals(new Integer[] {}, flattened4);

            // Test with all null sub-arrays
            Integer[][] isAllNulls = { null, null, null };
            Integer[] flattened5 = ff.flatten(isAllNulls);
            Assertions.assertArrayEquals(new Integer[] {}, flattened5);

            // Test with String array
            String[][] stringArray = { { "a", "b" }, { "c", "d", "e" } };
            String[] flattenedStrings = ff.flatten(stringArray);
            Assertions.assertArrayEquals(new String[] { "a", "b", "c", "d", "e" }, flattenedStrings);
        }

        @Test
        public void testFlatOp() throws Exception {
            // Test sorting all elements
            Integer[][] array = { { 3, 1, 4 }, { 1, 5, 9 } };
            ff.applyOnFlattened(array, arr -> java.util.Arrays.sort(arr));
            Assertions.assertArrayEquals(new Integer[] { 1, 1, 3 }, array[0]);
            Assertions.assertArrayEquals(new Integer[] { 4, 5, 9 }, array[1]);

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 5, 3 }, null, { 1, 4 } };
            ff.applyOnFlattened(arrayWithNulls, arr -> java.util.Arrays.sort(arr));
            Assertions.assertArrayEquals(new Integer[] { 1, 3 }, arrayWithNulls[0]);
            Assertions.assertNull(arrayWithNulls[1]);
            Assertions.assertArrayEquals(new Integer[] { 4, 5 }, arrayWithNulls[2]);

            // Test with empty sub-arrays
            Integer[][] arrayWithEmpty = { { 5, 3 }, {}, { 1, 4 } };
            ff.applyOnFlattened(arrayWithEmpty, arr -> java.util.Arrays.sort(arr));
            Assertions.assertArrayEquals(new Integer[] { 1, 3 }, arrayWithEmpty[0]);
            Assertions.assertArrayEquals(new Integer[] {}, arrayWithEmpty[1]);
            Assertions.assertArrayEquals(new Integer[] { 4, 5 }, arrayWithEmpty[2]);

            // Test with empty array
            Integer[][] emptyArray = {};
            ff.applyOnFlattened(emptyArray, arr -> java.util.Arrays.sort(arr)); // Should not throw

            // Test with null array
            Integer[][] nullArray = null;
            ff.applyOnFlattened(nullArray, arr -> java.util.Arrays.sort(arr)); // Should not throw

            // Test with custom operation
            Integer[][] array2 = { { 1, 2 }, { 3, 4 } };
            ff.applyOnFlattened(array2, arr -> {
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = arr[i] * 10;
                }
            });
            Assertions.assertArrayEquals(new Integer[] { 10, 20 }, array2[0]);
            Assertions.assertArrayEquals(new Integer[] { 30, 40 }, array2[1]);
        }

        @Test
        public void testMap() throws Exception {
            // Test basic mapping
            Integer[][] array = { { 1, 2 }, { 3, 4 } };
            Integer[][] doubled = ff.map(array, x -> x * 2);
            Assertions.assertArrayEquals(new Integer[] { 2, 4 }, doubled[0]);
            Assertions.assertArrayEquals(new Integer[] { 6, 8 }, doubled[1]);

            // Test null array
            Integer[][] nullArray = null;
            Assertions.assertThrows(NullPointerException.class, () -> ff.map(nullArray, x -> x * 2));

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            Integer[][] mapped = ff.map(arrayWithNulls, x -> x + 10);
            Assertions.assertArrayEquals(new Integer[] { 11, 12 }, mapped[0]);
            Assertions.assertEquals(0, mapped[1].length);
            Assertions.assertArrayEquals(new Integer[] { 13, 14 }, mapped[2]);

            // Test String transformation
            String[][] stringArray = { { "hello", "world" }, { "foo", "bar" } };
            String[][] upperCased = ff.map(stringArray, String::toUpperCase);
            Assertions.assertArrayEquals(new String[] { "HELLO", "WORLD" }, upperCased[0]);
            Assertions.assertArrayEquals(new String[] { "FOO", "BAR" }, upperCased[1]);
        }

        @Test
        public void testMapWithTargetType() throws Exception {
            // Test type conversion
            String[][] array = { { "1", "2" }, { "3", "4" } };
            Integer[][] numbers = ff.map(array, Integer::parseInt, Integer.class);
            Assertions.assertArrayEquals(new Integer[] { 1, 2 }, numbers[0]);
            Assertions.assertArrayEquals(new Integer[] { 3, 4 }, numbers[1]);

            // Test null array
            String[][] nullArray = null;
            Assertions.assertEquals(0, ff.map(nullArray, Integer::parseInt, Integer.class).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1", "2" }, null, { "3", "4" } };
            Integer[][] mapped = ff.map(arrayWithNulls, Integer::parseInt, Integer.class);
            Assertions.assertArrayEquals(new Integer[] { 1, 2 }, mapped[0]);
            Assertions.assertEquals(0, mapped[1].length);
            Assertions.assertArrayEquals(new Integer[] { 3, 4 }, mapped[2]);

            // Test Integer to String conversion
            Integer[][] intArray = { { 10, 20 }, { 30, 40 } };
            String[][] stringArray = ff.map(intArray, Object::toString, String.class);
            Assertions.assertArrayEquals(new String[] { "10", "20" }, stringArray[0]);
            Assertions.assertArrayEquals(new String[] { "30", "40" }, stringArray[1]);
        }

        @Test
        public void testMapToBoolean() throws Exception {
            // Test even number predicate
            Integer[][] array = { { 1, 2, 3 }, { 4, 5, 6 } };
            boolean[][] evens = ff.mapToBoolean(array, x -> x % 2 == 0);
            Assertions.assertArrayEquals(new boolean[] { false, true, false }, evens[0]);
            Assertions.assertArrayEquals(new boolean[] { true, false, true }, evens[1]);

            // Test null array
            Integer[][] nullArray = null;
            Assertions.assertEquals(0, ff.mapToBoolean(nullArray, x -> x % 2 == 0).length);

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            boolean[][] mapped = ff.mapToBoolean(arrayWithNulls, x -> x > 2);
            Assertions.assertArrayEquals(new boolean[] { false, false }, mapped[0]);
            Assertions.assertEquals(0, mapped[1].length);
            Assertions.assertArrayEquals(new boolean[] { true, true }, mapped[2]);

            // Test String predicate
            String[][] stringArray = { { "hello", "hi" }, { "world", "foo" } };
            boolean[][] longStrings = ff.mapToBoolean(stringArray, s -> s.length() > 3);
            Assertions.assertArrayEquals(new boolean[] { true, false }, longStrings[0]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, longStrings[1]);
        }

        @Test
        public void testMapToChar() throws Exception {
            // Test first character extraction
            String[][] array = { { "apple", "banana" }, { "cat", "dog" } };
            char[][] firstChars = ff.mapToChar(array, s -> s.charAt(0));
            Assertions.assertArrayEquals(new char[] { 'a', 'b' }, firstChars[0]);
            Assertions.assertArrayEquals(new char[] { 'c', 'd' }, firstChars[1]);

            // Test null array
            String[][] nullArray = null;
            Assertions.assertEquals(0, ff.mapToChar(nullArray, s -> s.charAt(0)).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "abc", "def" }, null, { "ghi", "jkl" } };
            char[][] mapped = ff.mapToChar(arrayWithNulls, s -> s.charAt(1));
            Assertions.assertArrayEquals(new char[] { 'b', 'e' }, mapped[0]);
            Assertions.assertEquals(0, mapped[1].length);
            Assertions.assertArrayEquals(new char[] { 'h', 'k' }, mapped[2]);

            // Test Integer to char
            Integer[][] intArray = { { 65, 66 }, { 67, 68 } };
            char[][] chars = ff.mapToChar(intArray, i -> (char) i.intValue());
            Assertions.assertArrayEquals(new char[] { 'A', 'B' }, chars[0]);
            Assertions.assertArrayEquals(new char[] { 'C', 'D' }, chars[1]);
        }

        @Test
        public void testMapToByte() throws Exception {
            // Test Integer to byte
            Integer[][] array = { { 10, 20 }, { 30, 40 } };
            byte[][] bytes = ff.mapToByte(array, Integer::byteValue);
            Assertions.assertArrayEquals(new byte[] { 10, 20 }, bytes[0]);
            Assertions.assertArrayEquals(new byte[] { 30, 40 }, bytes[1]);

            // Test null array
            Integer[][] nullArray = null;
            Assertions.assertEquals(0, ff.mapToByte(nullArray, Integer::byteValue).length);

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            byte[][] mapped = ff.mapToByte(arrayWithNulls, Integer::byteValue);
            Assertions.assertArrayEquals(new byte[] { 1, 2 }, mapped[0]);
            Assertions.assertEquals(0, mapped[1].length);
            Assertions.assertArrayEquals(new byte[] { 3, 4 }, mapped[2]);

            // Test String to byte (parsing)
            String[][] stringArray = { { "1", "2" }, { "3", "4" } };
            byte[][] parsedBytes = ff.mapToByte(stringArray, Byte::parseByte);
            Assertions.assertArrayEquals(new byte[] { 1, 2 }, parsedBytes[0]);
            Assertions.assertArrayEquals(new byte[] { 3, 4 }, parsedBytes[1]);
        }

        @Test
        public void testMapToShort() throws Exception {
            // Test Integer to short
            Integer[][] array = { { 100, 200 }, { 300, 400 } };
            short[][] shorts = ff.mapToShort(array, Integer::shortValue);
            Assertions.assertArrayEquals(new short[] { 100, 200 }, shorts[0]);
            Assertions.assertArrayEquals(new short[] { 300, 400 }, shorts[1]);

            // Test null array
            Integer[][] nullArray = null;
            Assertions.assertEquals(0, ff.mapToShort(nullArray, Integer::shortValue).length);

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 10, 20 }, null, { 30, 40 } };
            short[][] mapped = ff.mapToShort(arrayWithNulls, Integer::shortValue);
            Assertions.assertArrayEquals(new short[] { 10, 20 }, mapped[0]);
            Assertions.assertEquals(0, mapped[1].length);
            Assertions.assertArrayEquals(new short[] { 30, 40 }, mapped[2]);

            // Test String to short
            String[][] stringArray = { { "100", "200" }, { "300", "400" } };
            short[][] parsedShorts = ff.mapToShort(stringArray, Short::parseShort);
            Assertions.assertArrayEquals(new short[] { 100, 200 }, parsedShorts[0]);
            Assertions.assertArrayEquals(new short[] { 300, 400 }, parsedShorts[1]);
        }

        @Test
        public void testMapToInt() throws Exception {
            // Test String to int
            String[][] array = { { "10", "20" }, { "30", "40" } };
            int[][] numbers = ff.mapToInt(array, Integer::parseInt);
            Assertions.assertArrayEquals(new int[] { 10, 20 }, numbers[0]);
            Assertions.assertArrayEquals(new int[] { 30, 40 }, numbers[1]);

            // Test null array
            String[][] nullArray = null;
            Assertions.assertEquals(0, ff.mapToInt(nullArray, Integer::parseInt).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1", "2" }, null, { "3", "4" } };
            int[][] mapped = ff.mapToInt(arrayWithNulls, Integer::parseInt);
            Assertions.assertArrayEquals(new int[] { 1, 2 }, mapped[0]);
            Assertions.assertEquals(0, mapped[1].length);
            Assertions.assertArrayEquals(new int[] { 3, 4 }, mapped[2]);

            // Test String length to int
            String[][] stringArray = { { "hi", "hello" }, { "world", "foo" } };
            int[][] lengths = ff.mapToInt(stringArray, String::length);
            Assertions.assertArrayEquals(new int[] { 2, 5 }, lengths[0]);
            Assertions.assertArrayEquals(new int[] { 5, 3 }, lengths[1]);
        }

        @Test
        public void testMapToLong() throws Exception {
            // Test String to long
            String[][] array = { { "1000000", "2000000" }, { "3000000", "4000000" } };
            long[][] longs = ff.mapToLong(array, Long::parseLong);
            Assertions.assertArrayEquals(new long[] { 1000000L, 2000000L }, longs[0]);
            Assertions.assertArrayEquals(new long[] { 3000000L, 4000000L }, longs[1]);

            // Test null array
            String[][] nullArray = null;
            Assertions.assertEquals(0, ff.mapToLong(nullArray, Long::parseLong).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "100", "200" }, null, { "300", "400" } };
            long[][] mapped = ff.mapToLong(arrayWithNulls, Long::parseLong);
            Assertions.assertArrayEquals(new long[] { 100L, 200L }, mapped[0]);
            Assertions.assertEquals(0, mapped[1].length);
            Assertions.assertArrayEquals(new long[] { 300L, 400L }, mapped[2]);

            // Test Integer to long
            Integer[][] intArray = { { 100, 200 }, { 300, 400 } };
            long[][] longValues = ff.mapToLong(intArray, Integer::longValue);
            Assertions.assertArrayEquals(new long[] { 100L, 200L }, longValues[0]);
            Assertions.assertArrayEquals(new long[] { 300L, 400L }, longValues[1]);
        }

        @Test
        public void testMapToFloat() throws Exception {
            // Test String to float
            String[][] array = { { "1.5", "2.5" }, { "3.5", "4.5" } };
            float[][] floats = ff.mapToFloat(array, Float::parseFloat);
            Assertions.assertArrayEquals(new float[] { 1.5f, 2.5f }, floats[0]);
            Assertions.assertArrayEquals(new float[] { 3.5f, 4.5f }, floats[1]);

            // Test null array
            String[][] nullArray = null;
            Assertions.assertEquals(0, ff.mapToFloat(nullArray, Float::parseFloat).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1.1", "2.2" }, null, { "3.3", "4.4" } };
            float[][] mapped = ff.mapToFloat(arrayWithNulls, Float::parseFloat);
            Assertions.assertArrayEquals(new float[] { 1.1f, 2.2f }, mapped[0]);
            Assertions.assertEquals(0, mapped[1].length);
            Assertions.assertArrayEquals(new float[] { 3.3f, 4.4f }, mapped[2]);

            // Test Integer to float
            Integer[][] intArray = { { 10, 20 }, { 30, 40 } };
            float[][] floatValues = ff.mapToFloat(intArray, Integer::floatValue);
            Assertions.assertArrayEquals(new float[] { 10.0f, 20.0f }, floatValues[0]);
            Assertions.assertArrayEquals(new float[] { 30.0f, 40.0f }, floatValues[1]);
        }

        @Test
        public void testMapToDouble() throws Exception {
            // Test String to double
            String[][] array = { { "1.234", "2.345" }, { "3.456", "4.567" } };
            double[][] doubles = ff.mapToDouble(array, Double::parseDouble);
            Assertions.assertArrayEquals(new double[] { 1.234, 2.345 }, doubles[0]);
            Assertions.assertArrayEquals(new double[] { 3.456, 4.567 }, doubles[1]);

            // Test null array
            String[][] nullArray = null;
            Assertions.assertEquals(0, ff.mapToDouble(nullArray, Double::parseDouble).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1.1", "2.2" }, null, { "3.3", "4.4" } };
            double[][] mapped = ff.mapToDouble(arrayWithNulls, Double::parseDouble);
            Assertions.assertArrayEquals(new double[] { 1.1, 2.2 }, mapped[0]);
            Assertions.assertEquals(0, mapped[1].length);
            Assertions.assertArrayEquals(new double[] { 3.3, 4.4 }, mapped[2]);

            // Test Integer to double
            Integer[][] intArray = { { 10, 20 }, { 30, 40 } };
            double[][] doubleValues = ff.mapToDouble(intArray, Integer::doubleValue);
            Assertions.assertArrayEquals(new double[] { 10.0, 20.0 }, doubleValues[0]);
            Assertions.assertArrayEquals(new double[] { 30.0, 40.0 }, doubleValues[1]);
        }

        @Test
        public void testZipTwoArrays() throws Exception {
            // Test basic zip
            Integer[][] a = { { 1, 2 }, { 3, 4 } };
            Integer[][] b = { { 10, 20 }, { 30, 40 } };
            Integer[][] sums = ff.zip(a, b, (x, y) -> x + y);
            Assertions.assertArrayEquals(new Integer[] { 11, 22 }, sums[0]);
            Assertions.assertArrayEquals(new Integer[] { 33, 44 }, sums[1]);

            // Test different sizes - extra elements ignored
            Integer[][] a2 = { { 1, 2, 3 }, { 4, 5 } };
            Integer[][] b2 = { { 10 }, { 20, 30 } };
            Integer[][] sums2 = ff.zip(a2, b2, (x, y) -> x + y);
            Assertions.assertArrayEquals(new Integer[] { 11 }, sums2[0]);
            Assertions.assertArrayEquals(new Integer[] { 24, 35 }, sums2[1]);

            // Test different number of rows
            Integer[][] a3 = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            Integer[][] b3 = { { 10, 20 }, { 30, 40 } };
            Integer[][] sums3 = ff.zip(a3, b3, (x, y) -> x + y);
            Assertions.assertEquals(2, sums3.length);
            Assertions.assertArrayEquals(new Integer[] { 11, 22 }, sums3[0]);
            Assertions.assertArrayEquals(new Integer[] { 33, 44 }, sums3[1]);
        }

        @Test
        public void testZipTwoArraysWithTargetType() throws Exception {
            // Test type conversion
            Integer[][] a = { { 1, 2 }, { 3, 4 } };
            String[][] b = { { "a", "b" }, { "c", "d" } };
            String[][] combined = ff.zip(a, b, (i, s) -> i + s, String.class);
            Assertions.assertArrayEquals(new String[] { "1a", "2b" }, combined[0]);
            Assertions.assertArrayEquals(new String[] { "3c", "4d" }, combined[1]);

            // Test with different sizes
            Integer[][] a2 = { { 1 }, { 2, 3 } };
            String[][] b2 = { { "X", "Y" }, { "Z" } };
            String[][] combined2 = ff.zip(a2, b2, (i, s) -> i + s, String.class);
            Assertions.assertArrayEquals(new String[] { "1X" }, combined2[0]);
            Assertions.assertArrayEquals(new String[] { "2Z" }, combined2[1]);
        }

        @Test
        public void testZipTwoArraysWithDefaults() throws Exception {
            // Test with defaults for missing elements
            Integer[][] a = { { 1, 2 }, { 3 } };
            Integer[][] b = { { 10 }, { 30, 40 } };
            Integer[][] sums = ff.zip(a, b, 0, 0, (x, y) -> x + y);
            Assertions.assertArrayEquals(new Integer[] { 11, 2 }, sums[0]);
            Assertions.assertArrayEquals(new Integer[] { 33, 40 }, sums[1]);

            // Test with different row counts
            Integer[][] a3 = { { 1 } };
            Integer[][] b3 = { { 10 }, { 20 } };
            Integer[][] result2 = ff.zip(a3, b3, 0, 0, (x, y) -> x + y);
            Assertions.assertEquals(2, result2.length);
            Assertions.assertArrayEquals(new Integer[] { 11 }, result2[0]);
            Assertions.assertArrayEquals(new Integer[] { 20 }, result2[1]);
        }

        @Test
        public void testZipTwoArraysWithDefaultsAndTargetType() throws Exception {
            // Test with type conversion and defaults
            Integer[][] a = { { 1 }, { 2, 3 } };
            String[][] b = { { "X", "Y" }, { "Z" } };
            String[][] result = ff.zip(a, b, 0, "-", (i, s) -> i + s, String.class);
            Assertions.assertArrayEquals(new String[] { "1X", "0Y" }, result[0]);
            Assertions.assertArrayEquals(new String[] { "2Z", "3-" }, result[1]);

            // Test with different row counts
            Integer[][] a2 = { { 1 } };
            String[][] b2 = { { "A" }, { "B" } };
            String[][] result2 = ff.zip(a2, b2, 0, "?", (i, s) -> i + s, String.class);
            Assertions.assertEquals(2, result2.length);
            Assertions.assertArrayEquals(new String[] { "1A" }, result2[0]);
            Assertions.assertArrayEquals(new String[] { "0B" }, result2[1]);
        }

        @Test
        public void testZipThreeArrays() throws Exception {
            // Test basic three-way zip
            Integer[][] a = { { 1, 2 }, { 3, 4 } };
            Integer[][] b = { { 10, 20 }, { 30, 40 } };
            Integer[][] c = { { 100, 200 }, { 300, 400 } };
            Integer[][] sums = ff.zip(a, b, c, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new Integer[] { 111, 222 }, sums[0]);
            Assertions.assertArrayEquals(new Integer[] { 333, 444 }, sums[1]);

            // Test with different sizes
            Integer[][] a2 = { { 1 }, { 2 } };
            Double[][] b2 = { { 1.5, 2.5 }, { 3.5 } };
            String[][] c2 = { { "A" }, { "B", "C" } };
            // Only first element of each row will be processed (minimum size)
            Integer[][] result = ff.zip(a2, b2, c2, (i, d, s) -> i);
            Assertions.assertArrayEquals(new Integer[] { 1 }, result[0]);
            Assertions.assertArrayEquals(new Integer[] { 2 }, result[1]);
        }

        @Test
        public void testZipThreeArraysWithTargetType() throws Exception {
            // Test type conversion with three arrays
            Integer[][] a = { { 1 }, { 2 } };
            Double[][] b = { { 1.5, 2.5 }, { 3.5 } };
            String[][] c = { { "A" }, { "B", "C" } };
            String[][] result = ff.zip(a, b, c, (i, d, s) -> i + ":" + d + ":" + s, String.class);
            Assertions.assertArrayEquals(new String[] { "1:1.5:A" }, result[0]);
            Assertions.assertArrayEquals(new String[] { "2:3.5:B" }, result[1]);

            // Test with different row counts
            Integer[][] a2 = { { 1, 2 } };
            Double[][] b2 = { { 1.1, 2.2 }, { 3.3 } };
            String[][] c2 = { { "X", "Y" }, { "Z" } };
            String[][] result2 = ff.zip(a2, b2, c2, (i, d, s) -> i + "-" + d + "-" + s, String.class);
            Assertions.assertEquals(1, result2.length); // Minimum row count
            Assertions.assertArrayEquals(new String[] { "1-1.1-X", "2-2.2-Y" }, result2[0]);
        }

        @Test
        public void testZipThreeArraysWithDefaults() throws Exception {
            // Test with defaults for missing elements
            Integer[][] a = { { 1 }, { 2, 3 } };
            Integer[][] b = { { 10, 20 } };
            Integer[][] c = { { 100 }, { 200, 300 } };
            Integer[][] sums = ff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
            Assertions.assertArrayEquals(new Integer[] { 111, 20 }, sums[0]);
            Assertions.assertArrayEquals(new Integer[] { 202, 303 }, sums[1]);

            // Test with different row counts
            Integer[][] a2 = { { 1 } };
            Integer[][] b2 = { { 10 }, { 20 } };
            Integer[][] c2 = { { 100 }, { 200 }, { 300 } };
            Integer[][] result = ff.zip(a2, b2, c2, 0, 0, 0, (x, y, z) -> x + y + z);
            Assertions.assertEquals(3, result.length);
            Assertions.assertArrayEquals(new Integer[] { 111 }, result[0]);
            Assertions.assertArrayEquals(new Integer[] { 220 }, result[1]);
            Assertions.assertArrayEquals(new Integer[] { 300 }, result[2]);
        }

        @Test
        public void testZipThreeArraysWithDefaultsAndTargetType() throws Exception {
            // Test with type conversion and defaults
            Integer[][] a = { { 1 } };
            String[][] b = { { "X", "Y" } };
            Double[][] c = { { 0.5 }, { 1.5, 2.5 } };
            String[][] result = ff.zip(a, b, c, 0, "-", 0.0, (i, s, d) -> i + s + d, String.class);
            Assertions.assertArrayEquals(new String[] { "1X0.5", "0Y0.0" }, result[0]);
            Assertions.assertArrayEquals(new String[] { "0-1.5", "0-2.5" }, result[1]);

            // Test with all null inputs
            Integer[][] nullA = null;
            String[][] nullB = null;
            Double[][] nullC = null;
            String[][] result2 = ff.zip(nullA, nullB, nullC, 1, "A", 1.0, (i, s, d) -> i + s + d, String.class);
            Assertions.assertEquals(0, result2.length);
        }

        @Test
        public void testTotalCountOfElements() {
            // Test normal array
            Object[][] array = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
            long total = ff.elementCount(array);
            Assertions.assertEquals(6, total);

            // Test empty array
            Object[][] emptyArray = {};
            Assertions.assertEquals(0, ff.elementCount(emptyArray));

            // Test null array
            Object[][] nullArray = null;
            Assertions.assertEquals(0, ff.elementCount(nullArray));

            // Test array with all nulls
            Object[][] isAllNulls = { null, null, null };
            Assertions.assertEquals(0, ff.elementCount(isAllNulls));

            // Test array with empty sub-arrays
            Object[][] withEmpty = { {}, { 1, 2 }, {}, { 3, 4, 5 } };
            Assertions.assertEquals(5, ff.elementCount(withEmpty));

            // Test large array
            Object[][] largeArray = new Object[1000][];
            for (int i = 0; i < 1000; i++) {
                largeArray[i] = new Object[10];
            }
            Assertions.assertEquals(10000, ff.elementCount(largeArray));
        }

        @Test
        public void testMinSubArrayLen() {
            // Test normal array
            Object[][] array = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
            int minLen = ff.minSubArrayLength(array);
            Assertions.assertEquals(0, minLen); // null counts as 0

            // Test without null
            Object[][] array2 = { { 1, 2, 3 }, { 4, 5 }, { 6 } };
            int minLen2 = ff.minSubArrayLength(array2);
            Assertions.assertEquals(1, minLen2);

            // Test empty array
            Object[][] emptyArray = {};
            Assertions.assertEquals(0, ff.minSubArrayLength(emptyArray));

            // Test null array
            Object[][] nullArray = null;
            Assertions.assertEquals(0, ff.minSubArrayLength(nullArray));

            // Test with empty sub-arrays
            Object[][] withEmpty = { {}, { 1, 2 }, { 3, 4, 5 } };
            Assertions.assertEquals(0, ff.minSubArrayLength(withEmpty));

            // Test uniform length
            Object[][] uniform = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            Assertions.assertEquals(3, ff.minSubArrayLength(uniform));
        }

        @Test
        public void testMaxSubArrayLen() {
            // Test normal array
            Object[][] array = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
            int maxLen = ff.maxSubArrayLength(array);
            Assertions.assertEquals(3, maxLen);

            // Test empty array
            Object[][] emptyArray = {};
            Assertions.assertEquals(0, ff.maxSubArrayLength(emptyArray));

            // Test null array
            Object[][] nullArray = null;
            Assertions.assertEquals(0, ff.maxSubArrayLength(nullArray));

            // Test with all nulls
            Object[][] isAllNulls = { null, null, null };
            Assertions.assertEquals(0, ff.maxSubArrayLength(isAllNulls));

            // Test with empty sub-arrays
            Object[][] withEmpty = { {}, { 1, 2 }, { 3, 4, 5, 6, 7 } };
            Assertions.assertEquals(5, ff.maxSubArrayLength(withEmpty));

            // Test uniform length
            Object[][] uniform = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            Assertions.assertEquals(2, ff.maxSubArrayLength(uniform));
        }
    }

    @Nested
    public class FFFTest {
        @Test
        public void testUpdateAll() throws Exception {
            // Test with non-empty array
            String[][][] arr = { { { "hello", "world" } }, { { "foo", "bar" } } };
            fff.updateAll(arr, str -> str.toUpperCase());
            Assertions.assertEquals("HELLO", arr[0][0][0]);
            Assertions.assertEquals("WORLD", arr[0][0][1]);
            Assertions.assertEquals("FOO", arr[1][0][0]);
            Assertions.assertEquals("BAR", arr[1][0][1]);

            // Test with empty array
            String[][][] emptyArr = new String[0][][];
            fff.updateAll(emptyArr, str -> str.toUpperCase());
            Assertions.assertEquals(0, emptyArr.length);

            // Test with null elements
            String[][][] nullArr = { null, { null, { "test" } } };
            fff.updateAll(nullArr, str -> str.toUpperCase());
            Assertions.assertNull(nullArr[0]);
            Assertions.assertNull(nullArr[1][0]);
            Assertions.assertEquals("TEST", nullArr[1][1][0]);

            // Test with Integer array
            Integer[][][] intArr = { { { 1, 2, 3 } }, { { 4, 5, 6 } } };
            fff.updateAll(intArr, n -> n * 2);
            Assertions.assertEquals(2, intArr[0][0][0]);
            Assertions.assertEquals(4, intArr[0][0][1]);
            Assertions.assertEquals(6, intArr[0][0][2]);
            Assertions.assertEquals(8, intArr[1][0][0]);
            Assertions.assertEquals(10, intArr[1][0][1]);
            Assertions.assertEquals(12, intArr[1][0][2]);
        }

        @Test
        public void testReplaceIf() throws Exception {
            // Test replacing null values
            Integer[][][] arr = { { { 1, 2, null } }, { { 3, null, 5 } } };
            fff.replaceIf(arr, val -> val == null, 0);
            Assertions.assertEquals(1, arr[0][0][0]);
            Assertions.assertEquals(2, arr[0][0][1]);
            Assertions.assertEquals(0, arr[0][0][2]);
            Assertions.assertEquals(3, arr[1][0][0]);
            Assertions.assertEquals(0, arr[1][0][1]);
            Assertions.assertEquals(5, arr[1][0][2]);

            // Test replacing based on condition
            Integer[][][] arr2 = { { { 1, 2, 3 } }, { { 4, 5, 6 } } };
            fff.replaceIf(arr2, val -> val > 3, -1);
            Assertions.assertEquals(1, arr2[0][0][0]);
            Assertions.assertEquals(2, arr2[0][0][1]);
            Assertions.assertEquals(3, arr2[0][0][2]);
            Assertions.assertEquals(-1, arr2[1][0][0]);
            Assertions.assertEquals(-1, arr2[1][0][1]);
            Assertions.assertEquals(-1, arr2[1][0][2]);

            // Test with empty array
            Integer[][][] emptyArr = new Integer[0][][];
            fff.replaceIf(emptyArr, val -> val == null, 0);
            Assertions.assertEquals(0, emptyArr.length);

            // Test with String array
            String[][][] strArr = { { { "apple", "banana" } }, { { "cat", "dog" } } };
            fff.replaceIf(strArr, str -> str.length() > 3, "***");
            Assertions.assertEquals("***", strArr[0][0][0]);
            Assertions.assertEquals("***", strArr[0][0][1]);
            Assertions.assertEquals("cat", strArr[1][0][0]);
            Assertions.assertEquals("dog", strArr[1][0][1]);
        }

        @Test
        public void testReshape() {
            // Test basic reshape
            Integer[] flat = { 1, 2, 3, 4, 5, 6, 7, 8 };
            Integer[][][] reshaped = fff.reshape(flat, 2, 2);
            Assertions.assertEquals(2, reshaped.length);
            Assertions.assertEquals(2, reshaped[0].length);
            Assertions.assertEquals(2, reshaped[0][0].length);
            Assertions.assertEquals(1, reshaped[0][0][0]);
            Assertions.assertEquals(2, reshaped[0][0][1]);
            Assertions.assertEquals(3, reshaped[0][1][0]);
            Assertions.assertEquals(4, reshaped[0][1][1]);
            Assertions.assertEquals(5, reshaped[1][0][0]);
            Assertions.assertEquals(6, reshaped[1][0][1]);
            Assertions.assertEquals(7, reshaped[1][1][0]);
            Assertions.assertEquals(8, reshaped[1][1][1]);

            // Test with uneven division
            Integer[] flat2 = { 1, 2, 3, 4, 5 };
            Integer[][][] reshaped2 = fff.reshape(flat2, 2, 2);
            Assertions.assertEquals(2, reshaped2.length);
            Assertions.assertEquals(2, reshaped2[0].length);
            Assertions.assertEquals(2, reshaped2[0][0].length);
            Assertions.assertEquals(1, reshaped2[1].length);
            Assertions.assertEquals(1, reshaped2[1][0].length);
            Assertions.assertEquals(5, reshaped2[1][0][0]);

            // Test with empty array
            Integer[] emptyFlat = new Integer[0];
            Integer[][][] emptyReshaped = fff.reshape(emptyFlat, 2, 2);
            Assertions.assertEquals(0, emptyReshaped.length);

            // Test with invalid arguments
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                fff.reshape(flat, 0, 2);
            });
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                fff.reshape(flat, 2, 0);
            });
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                fff.reshape(flat, -1, 2);
            });
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                fff.reshape(null, 2, 2);
            });
        }

        @Test
        public void testFlatten() {
            // Test basic flatten
            Integer[][][] cube = { { { 1, 2 } }, { { 3 } }, { { 4, 5, 6 } } };
            Integer[] flat = fff.flatten(cube);
            Assertions.assertEquals(6, flat.length);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, flat);

            // Test with empty sub-arrays
            Integer[][][] cube2 = { { { 1, 2 } }, {}, { { 3, 4 } } };
            Integer[] flat2 = fff.flatten(cube2);
            Assertions.assertEquals(4, flat2.length);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, flat2);

            // Test with null sub-arrays
            Integer[][][] cube3 = { { { 1, 2 } }, null, { { 3, 4 } } };
            Integer[] flat3 = fff.flatten(cube3);
            Assertions.assertEquals(4, flat3.length);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, flat3);

            // Test with empty array
            Integer[][][] emptyCube = new Integer[0][][];
            Integer[] emptyFlat = fff.flatten(emptyCube);
            Assertions.assertEquals(0, emptyFlat.length);

            // Test with all null/empty elements
            Integer[][][] nullCube = { null, { null, {} }, { {} } };
            Integer[] nullFlat = fff.flatten(nullCube);
            Assertions.assertEquals(0, nullFlat.length);
        }

        @Test
        public void testFlatOp() throws Exception {
            // Test sorting all elements
            Integer[][][] arr = { { { 5, 2 } }, { { 9, 1 } }, { { 3, 7 } } };
            fff.applyOnFlattened(arr, flat -> java.util.Arrays.sort(flat));
            Assertions.assertEquals(1, arr[0][0][0]);
            Assertions.assertEquals(2, arr[0][0][1]);
            Assertions.assertEquals(3, arr[1][0][0]);
            Assertions.assertEquals(5, arr[1][0][1]);
            Assertions.assertEquals(7, arr[2][0][0]);
            Assertions.assertEquals(9, arr[2][0][1]);

            // Test reversing elements
            Integer[][][] arr2 = { { { 1, 2 } }, { { 3, 4 } } };
            fff.applyOnFlattened(arr2, flat -> {
                for (int i = 0; i < flat.length / 2; i++) {
                    Integer temp = flat[i];
                    flat[i] = flat[flat.length - 1 - i];
                    flat[flat.length - 1 - i] = temp;
                }
            });
            Assertions.assertEquals(4, arr2[0][0][0]);
            Assertions.assertEquals(3, arr2[0][0][1]);
            Assertions.assertEquals(2, arr2[1][0][0]);
            Assertions.assertEquals(1, arr2[1][0][1]);

            // Test with empty array
            Integer[][][] emptyArr = new Integer[0][][];
            fff.applyOnFlattened(emptyArr, flat -> java.util.Arrays.sort(flat));
            Assertions.assertEquals(0, emptyArr.length);

            // Test with null and empty sub-arrays
            Integer[][][] mixedArr = { { { 1, 2 } }, null, { {} }, { { 3, 4 } } };
            fff.applyOnFlattened(mixedArr, flat -> {
                for (int i = 0; i < flat.length; i++) {
                    flat[i] = flat[i] * 10;
                }
            });
            Assertions.assertEquals(10, mixedArr[0][0][0]);
            Assertions.assertEquals(20, mixedArr[0][0][1]);
            Assertions.assertEquals(30, mixedArr[3][0][0]);
            Assertions.assertEquals(40, mixedArr[3][0][1]);
        }

        @Test
        public void testMapSameType() throws Exception {
            // Test basic mapping
            Integer[][][] numbers = { { { 1, 2 } }, { { 3, 4 } } };
            Integer[][][] doubled = fff.map(numbers, n -> n * 2);
            Assertions.assertEquals(2, doubled[0][0][0]);
            Assertions.assertEquals(4, doubled[0][0][1]);
            Assertions.assertEquals(6, doubled[1][0][0]);
            Assertions.assertEquals(8, doubled[1][0][1]);

            // Test with null input
            Assertions.assertThrows(NullPointerException.class, () -> fff.map((Integer[][][]) null, n -> n * 2));

            // Test with null sub-arrays
            Integer[][][] mixedArr = { { { 1, 2 } }, null, { { 3, 4 } } };
            Integer[][][] mappedMixed = fff.map(mixedArr, n -> n + 10);
            Assertions.assertEquals(11, mappedMixed[0][0][0]);
            Assertions.assertEquals(12, mappedMixed[0][0][1]);
            Assertions.assertEquals(0, mappedMixed[1].length);
            Assertions.assertEquals(13, mappedMixed[2][0][0]);
            Assertions.assertEquals(14, mappedMixed[2][0][1]);

            // Test with String array
            String[][][] strings = { { { "hello", "world" } }, { { "foo", "bar" } } };
            String[][][] upperStrings = fff.map(strings, String::toUpperCase);
            Assertions.assertEquals("HELLO", upperStrings[0][0][0]);
            Assertions.assertEquals("WORLD", upperStrings[0][0][1]);
            Assertions.assertEquals("FOO", upperStrings[1][0][0]);
            Assertions.assertEquals("BAR", upperStrings[1][0][1]);
        }

        @Test
        public void testMapDifferentType() throws Exception {
            // Test type conversion
            String[][][] strings = { { { "1", "2" } }, { { "3", "4" } } };
            Integer[][][] numbers = fff.map(strings, Integer::parseInt, Integer.class);
            Assertions.assertEquals(1, numbers[0][0][0]);
            Assertions.assertEquals(2, numbers[0][0][1]);
            Assertions.assertEquals(3, numbers[1][0][0]);
            Assertions.assertEquals(4, numbers[1][0][1]);

            // Test null input
            Integer[][][] nullResult = fff.map((String[][][]) null, Integer::parseInt, Integer.class);
            Assertions.assertEquals(0, nullResult.length);

            // Test Integer to String conversion
            Integer[][][] nums = { { { 100, 200 } }, { { 300, 400 } } };
            String[][][] strs = fff.map(nums, n -> "Number: " + n, String.class);
            Assertions.assertEquals("Number: 100", strs[0][0][0]);
            Assertions.assertEquals("Number: 200", strs[0][0][1]);
            Assertions.assertEquals("Number: 300", strs[1][0][0]);
            Assertions.assertEquals("Number: 400", strs[1][0][1]);

            // Test with mixed null elements
            Integer[][][] mixedNums = { { { 1, null } }, null, { { null, 2 } } };
            String[][][] mixedStrs = fff.map(mixedNums, n -> n == null ? "NULL" : n.toString(), String.class);
            Assertions.assertEquals("1", mixedStrs[0][0][0]);
            Assertions.assertEquals("NULL", mixedStrs[0][0][1]);
            Assertions.assertEquals(0, mixedStrs[1].length);
            Assertions.assertEquals("NULL", mixedStrs[2][0][0]);
            Assertions.assertEquals("2", mixedStrs[2][0][1]);
        }

        @Test
        public void testMapToBoolean() throws Exception {
            // Test even number check
            Integer[][][] numbers = { { { 1, 2, 3 } }, { { 4, 5, 6 } } };
            boolean[][][] evenMask = fff.mapToBoolean(numbers, n -> n % 2 == 0);
            Assertions.assertFalse(evenMask[0][0][0]);
            Assertions.assertTrue(evenMask[0][0][1]);
            Assertions.assertFalse(evenMask[0][0][2]);
            Assertions.assertTrue(evenMask[1][0][0]);
            Assertions.assertFalse(evenMask[1][0][1]);
            Assertions.assertTrue(evenMask[1][0][2]);

            // Test null input
            boolean[][][] nullResult = fff.mapToBoolean(null, n -> true);
            Assertions.assertEquals(0, nullResult.length);

            // Test String length check
            String[][][] strings = { { { "a", "ab", "abc" } }, { { "abcd", "abcde" } } };
            boolean[][][] lengthCheck = fff.mapToBoolean(strings, s -> s.length() > 2);
            Assertions.assertFalse(lengthCheck[0][0][0]);
            Assertions.assertFalse(lengthCheck[0][0][1]);
            Assertions.assertTrue(lengthCheck[0][0][2]);
            Assertions.assertTrue(lengthCheck[1][0][0]);
            Assertions.assertTrue(lengthCheck[1][0][1]);
        }

        @Test
        public void testMapToChar() throws Exception {
            // Test extracting first character
            String[][][] words = { { { "apple", "banana" } }, { { "cat", "dog" } } };
            char[][][] firstLetters = fff.mapToChar(words, s -> s.charAt(0));
            Assertions.assertEquals('a', firstLetters[0][0][0]);
            Assertions.assertEquals('b', firstLetters[0][0][1]);
            Assertions.assertEquals('c', firstLetters[1][0][0]);
            Assertions.assertEquals('d', firstLetters[1][0][1]);

            // Test null input
            char[][][] nullResult = fff.mapToChar(null, s -> 'x');
            Assertions.assertEquals(0, nullResult.length);

            // Test with Integer to char conversion
            Integer[][][] numbers = { { { 65, 66 } }, { { 67, 68 } } };
            char[][][] chars = fff.mapToChar(numbers, n -> (char) n.intValue());
            Assertions.assertEquals('A', chars[0][0][0]);
            Assertions.assertEquals('B', chars[0][0][1]);
            Assertions.assertEquals('C', chars[1][0][0]);
            Assertions.assertEquals('D', chars[1][0][1]);
        }

        @Test
        public void testMapToByte() throws Exception {
            // Test Integer to byte conversion
            Integer[][][] numbers = { { { 10, 20 } }, { { 30, 40 } } };
            byte[][][] bytes = fff.mapToByte(numbers, Integer::byteValue);
            Assertions.assertEquals((byte) 10, bytes[0][0][0]);
            Assertions.assertEquals((byte) 20, bytes[0][0][1]);
            Assertions.assertEquals((byte) 30, bytes[1][0][0]);
            Assertions.assertEquals((byte) 40, bytes[1][0][1]);

            // Test null input
            byte[][][] nullResult = fff.mapToByte(null, n -> (byte) 0);
            Assertions.assertEquals(0, nullResult.length);

            // Test String length to byte
            String[][][] strings = { { { "a", "ab" } }, { { "abc", "abcd" } } };
            byte[][][] lengths = fff.mapToByte(strings, s -> (byte) s.length());
            Assertions.assertEquals((byte) 1, lengths[0][0][0]);
            Assertions.assertEquals((byte) 2, lengths[0][0][1]);
            Assertions.assertEquals((byte) 3, lengths[1][0][0]);
            Assertions.assertEquals((byte) 4, lengths[1][0][1]);
        }

        @Test
        public void testMapToShort() throws Exception {
            // Test Integer to short conversion
            Integer[][][] numbers = { { { 100, 200 } }, { { 300, 400 } } };
            short[][][] shorts = fff.mapToShort(numbers, Integer::shortValue);
            Assertions.assertEquals((short) 100, shorts[0][0][0]);
            Assertions.assertEquals((short) 200, shorts[0][0][1]);
            Assertions.assertEquals((short) 300, shorts[1][0][0]);
            Assertions.assertEquals((short) 400, shorts[1][0][1]);

            // Test null input
            short[][][] nullResult = fff.mapToShort(null, n -> (short) 0);
            Assertions.assertEquals(0, nullResult.length);

            // Test with larger numbers
            Integer[][][] largeNumbers = { { { 1000, 2000 } }, { { 3000, 4000 } } };
            short[][][] largeShorts = fff.mapToShort(largeNumbers, n -> n.shortValue());
            Assertions.assertEquals((short) 1000, largeShorts[0][0][0]);
            Assertions.assertEquals((short) 2000, largeShorts[0][0][1]);
            Assertions.assertEquals((short) 3000, largeShorts[1][0][0]);
            Assertions.assertEquals((short) 4000, largeShorts[1][0][1]);
        }

        @Test
        public void testMapToInt() throws Exception {
            // Test String to int parsing
            String[][][] stringNumbers = { { { "1", "2" } }, { { "3", "4" } } };
            int[][][] integers = fff.mapToInt(stringNumbers, Integer::parseInt);
            Assertions.assertEquals(1, integers[0][0][0]);
            Assertions.assertEquals(2, integers[0][0][1]);
            Assertions.assertEquals(3, integers[1][0][0]);
            Assertions.assertEquals(4, integers[1][0][1]);

            // Test null input
            int[][][] nullResult = fff.mapToInt(null, s -> 0);
            Assertions.assertEquals(0, nullResult.length);

            // Test Double to int conversion
            Double[][][] doubles = { { { 1.5, 2.7 } }, { { 3.1, 4.9 } } };
            int[][][] ints = fff.mapToInt(doubles, Double::intValue);
            Assertions.assertEquals(1, ints[0][0][0]);
            Assertions.assertEquals(2, ints[0][0][1]);
            Assertions.assertEquals(3, ints[1][0][0]);
            Assertions.assertEquals(4, ints[1][0][1]);
        }

        @Test
        public void testMapToLong() throws Exception {
            // Test String to long parsing
            String[][][] timestamps = { { { "1000000" } }, { { "2000000" } } };
            long[][][] longs = fff.mapToLong(timestamps, Long::parseLong);
            Assertions.assertEquals(1000000L, longs[0][0][0]);
            Assertions.assertEquals(2000000L, longs[1][0][0]);

            // Test null input
            long[][][] nullResult = fff.mapToLong(null, s -> 0L);
            Assertions.assertEquals(0, nullResult.length);

            // Test Integer to long conversion
            Integer[][][] numbers = { { { 100, 200 } }, { { 300, 400 } } };
            long[][][] longNumbers = fff.mapToLong(numbers, Integer::longValue);
            Assertions.assertEquals(100L, longNumbers[0][0][0]);
            Assertions.assertEquals(200L, longNumbers[0][0][1]);
            Assertions.assertEquals(300L, longNumbers[1][0][0]);
            Assertions.assertEquals(400L, longNumbers[1][0][1]);
        }

        @Test
        public void testMapToFloat() throws Exception {
            // Test String to float parsing
            String[][][] decimals = { { { "1.5", "2.7" } }, { { "3.14", "4.2" } } };
            float[][][] floats = fff.mapToFloat(decimals, Float::parseFloat);
            Assertions.assertEquals(1.5f, floats[0][0][0], 0.001f);
            Assertions.assertEquals(2.7f, floats[0][0][1], 0.001f);
            Assertions.assertEquals(3.14f, floats[1][0][0], 0.001f);
            Assertions.assertEquals(4.2f, floats[1][0][1], 0.001f);

            // Test null input
            float[][][] nullResult = fff.mapToFloat(null, s -> 0.0f);
            Assertions.assertEquals(0, nullResult.length);

            // Test Integer to float conversion
            Integer[][][] numbers = { { { 10, 20 } }, { { 30, 40 } } };
            float[][][] floatNumbers = fff.mapToFloat(numbers, Integer::floatValue);
            Assertions.assertEquals(10.0f, floatNumbers[0][0][0], 0.001f);
            Assertions.assertEquals(20.0f, floatNumbers[0][0][1], 0.001f);
            Assertions.assertEquals(30.0f, floatNumbers[1][0][0], 0.001f);
            Assertions.assertEquals(40.0f, floatNumbers[1][0][1], 0.001f);
        }

        @Test
        public void testMapToDouble() throws Exception {
            // Test String to double parsing with scientific notation
            String[][][] scientificData = { { { "1.23e10", "4.56e-5" } }, { { "7.89e15" } } };
            double[][][] doubles = fff.mapToDouble(scientificData, Double::parseDouble);
            Assertions.assertEquals(1.23e10, doubles[0][0][0], 0.001);
            Assertions.assertEquals(4.56e-5, doubles[0][0][1], 0.00001);
            Assertions.assertEquals(7.89e15, doubles[1][0][0], 0.001);

            // Test null input
            double[][][] nullResult = fff.mapToDouble(null, s -> 0.0);
            Assertions.assertEquals(0, nullResult.length);

            // Test Integer to double conversion
            Integer[][][] numbers = { { { 100, 200 } }, { { 300, 400 } } };
            double[][][] doubleNumbers = fff.mapToDouble(numbers, Integer::doubleValue);
            Assertions.assertEquals(100.0, doubleNumbers[0][0][0], 0.001);
            Assertions.assertEquals(200.0, doubleNumbers[0][0][1], 0.001);
            Assertions.assertEquals(300.0, doubleNumbers[1][0][0], 0.001);
            Assertions.assertEquals(400.0, doubleNumbers[1][0][1], 0.001);
        }

        @Test
        public void testZipTwoArraysSameType() throws Exception {
            // Test basic zip with addition
            Integer[][][] a = { { { 1, 2 } }, { { 3, 4 } } };
            Integer[][][] b = { { { 10, 20 } }, { { 30, 40 } } };
            Integer[][][] sum = fff.zip(a, b, (x, y) -> x + y);
            Assertions.assertEquals(11, sum[0][0][0]);
            Assertions.assertEquals(22, sum[0][0][1]);
            Assertions.assertEquals(33, sum[1][0][0]);
            Assertions.assertEquals(44, sum[1][0][1]);

            // Test with different sizes (truncation)
            Integer[][][] a2 = { { { 1, 2 } }, { { 3, 4 } }, { { 5, 6 } } };
            Integer[][][] b2 = { { { 10, 20 } }, { { 30, 40 } } };
            Integer[][][] sum2 = fff.zip(a2, b2, (x, y) -> x + y);
            Assertions.assertEquals(2, sum2.length);
            Assertions.assertEquals(11, sum2[0][0][0]);
            Assertions.assertEquals(22, sum2[0][0][1]);
            Assertions.assertEquals(33, sum2[1][0][0]);
            Assertions.assertEquals(44, sum2[1][0][1]);

            // Test String concatenation
            String[][][] strA = { { { "a", "b" } }, { { "c", "d" } } };
            String[][][] strB = { { { "1", "2" } }, { { "3", "4" } } };
            String[][][] concat = fff.zip(strA, strB, (s1, s2) -> s1 + s2);
            Assertions.assertEquals("a1", concat[0][0][0]);
            Assertions.assertEquals("b2", concat[0][0][1]);
            Assertions.assertEquals("c3", concat[1][0][0]);
            Assertions.assertEquals("d4", concat[1][0][1]);
        }

        @Test
        public void testZipTwoArraysDifferentType() throws Exception {
            // Test combining different types
            Integer[][][] numbers = { { { 1, 2 } } };
            String[][][] strings = { { { "a", "b" } } };
            String[][][] combined = fff.zip(numbers, strings, (n, s) -> n + s, String.class);
            Assertions.assertEquals("1a", combined[0][0][0]);
            Assertions.assertEquals("2b", combined[0][0][1]);

            // Test with more complex transformation
            Double[][][] doubles = { { { 1.5, 2.5 } }, { { 3.5, 4.5 } } };
            Integer[][][] ints = { { { 1, 2 } }, { { 3, 4 } } };
            String[][][] result = fff.zip(doubles, ints, (d, i) -> String.format("%.1f:%d", d, i), String.class);
            Assertions.assertEquals("1.5:1", result[0][0][0]);
            Assertions.assertEquals("2.5:2", result[0][0][1]);
            Assertions.assertEquals("3.5:3", result[1][0][0]);
            Assertions.assertEquals("4.5:4", result[1][0][1]);
        }

        @Test
        public void testZipTwoArraysWithDefaults() throws Exception {
            // Test with different sizes and defaults
            Integer[][][] a = { { { 1, 2 } } };
            Integer[][][] b = { { { 10 } }, { { 20, 30 } } };
            Integer[][][] result = fff.zip(a, b, 0, 0, (x, y) -> x + y);
            Assertions.assertEquals(2, result.length);
            Assertions.assertEquals(11, result[0][0][0]);
            Assertions.assertEquals(2, result[0][0][1]);
            Assertions.assertEquals(20, result[1][0][0]);
            Assertions.assertEquals(30, result[1][0][1]);

            // Test String arrays with defaults
            String[][][] strA = { { { "a" } } };
            String[][][] strB = { { { "1", "2" } }, { { "3" } } };
            String[][][] strResult = fff.zip(strA, strB, "X", "Y", (s1, s2) -> s1 + s2);
            Assertions.assertEquals(2, strResult.length);
            Assertions.assertEquals("a1", strResult[0][0][0]);
            Assertions.assertEquals("X2", strResult[0][0][1]);
            Assertions.assertEquals("X3", strResult[1][0][0]);
        }

        @Test
        public void testZipTwoArraysWithDefaultsDifferentType() throws Exception {
            // Test type conversion with defaults
            Integer[][][] nums = { { { 1 } } };
            String[][][] strs = { { { "a", "b" } }, { { "c" } } };
            String[][][] result = fff.zip(nums, strs, 0, "x", (n, s) -> n + "-" + s, String.class);
            Assertions.assertEquals(2, result.length);
            Assertions.assertEquals("1-a", result[0][0][0]);
            Assertions.assertEquals("0-b", result[0][0][1]);
            Assertions.assertEquals("0-c", result[1][0][0]);

            // Test with all arrays needing defaults
            Integer[][][] shortArr = { { { 1 } } };
            Double[][][] longArr = { { { 1.1 } }, { { 2.2, 3.3 } }, { { 4.4 } } };
            String[][][] combined = fff.zip(shortArr, longArr, -1, 0.0, (i, d) -> String.format("%d:%.1f", i, d), String.class);
            Assertions.assertEquals(3, combined.length);
            Assertions.assertEquals("1:1.1", combined[0][0][0]);
            Assertions.assertEquals("-1:2.2", combined[1][0][0]);
            Assertions.assertEquals("-1:3.3", combined[1][0][1]);
            Assertions.assertEquals("-1:4.4", combined[2][0][0]);
        }

        @Test
        public void testZipThreeArraysSameType() throws Exception {
            // Test basic three-way zip
            Integer[][][] a = { { { 1, 2 } } };
            Integer[][][] b = { { { 10, 20 } } };
            Integer[][][] c = { { { 100, 200 } } };
            Integer[][][] sum = fff.zip(a, b, c, (x, y, z) -> x + y + z);
            Assertions.assertEquals(111, sum[0][0][0]);
            Assertions.assertEquals(222, sum[0][0][1]);

            // Test with different sizes (truncation to minimum)
            Integer[][][] a2 = { { { 1, 2 } }, { { 3, 4 } } };
            Integer[][][] b2 = { { { 10, 20 } } };
            Integer[][][] c2 = { { { 100, 200 } }, { { 300, 400 } }, { { 500, 600 } } };
            Integer[][][] sum2 = fff.zip(a2, b2, c2, (x, y, z) -> x + y + z);
            Assertions.assertEquals(1, sum2.length);
            Assertions.assertEquals(111, sum2[0][0][0]);
            Assertions.assertEquals(222, sum2[0][0][1]);
        }

        @Test
        public void testZipThreeArraysDifferentType() throws Exception {
            // Test combining three different types
            Integer[][][] nums = { { { 1 } } };
            String[][][] strs = { { { "a" } } };
            Double[][][] dbls = { { { 2.5 } } };
            String[][][] result = fff.zip(nums, strs, dbls, (n, s, d) -> n + s + d, String.class);
            Assertions.assertEquals("1a2.5", result[0][0][0]);

            // Test more complex transformation
            Integer[][][] ints = { { { 1, 2 } }, { { 3, 4 } } };
            String[][][] strings = { { { "A", "B" } }, { { "C", "D" } } };
            Boolean[][][] bools = { { { true, false } }, { { false, true } } };
            String[][][] combined = fff.zip(ints, strings, bools, (i, s, b) -> String.format("%d-%s-%s", i, s, b ? "Y" : "N"), String.class);
            Assertions.assertEquals("1-A-Y", combined[0][0][0]);
            Assertions.assertEquals("2-B-N", combined[0][0][1]);
            Assertions.assertEquals("3-C-N", combined[1][0][0]);
            Assertions.assertEquals("4-D-Y", combined[1][0][1]);
        }

        @Test
        public void testZipThreeArraysWithDefaults() throws Exception {
            // Test with different sizes and defaults
            Integer[][][] a = { { { 1 } } };
            Integer[][][] b = { { { 10, 20 } } };
            Integer[][][] c = { { {}, { 100 } } };
            Integer[][][] result = fff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
            Arrays.printAndReturn(result);
            Assertions.assertEquals(1, result.length);
            Assertions.assertEquals(11, result[0][0][0]);
            Assertions.assertEquals(20, result[0][0][1]);
            Assertions.assertEquals(100, result[0][1][0]);

            // Test with all arrays of different sizes
            Integer[][][] a2 = { { { 1, 2 } }, { { 3 } } };
            Integer[][][] b2 = { { { 10 } } };
            Integer[][][] c2 = { { { 100, 200, 300 } } };
            Integer[][][] sum = fff.zip(a2, b2, c2, -1, -10, -100, (x, y, z) -> x + y + z);
            Assertions.assertEquals(2, sum.length);
            Assertions.assertEquals(111, sum[0][0][0]);
            Assertions.assertEquals(192, sum[0][0][1]);
            Assertions.assertEquals(289, sum[0][0][2]);
            Assertions.assertEquals(-107, sum[1][0][0]);
        }

        @Test
        public void testZipThreeArraysWithDefaultsDifferentType() throws Exception {
            // Test type conversion with defaults
            Integer[][][] a = { { { 1 } } };
            String[][][] b = { { { "a", "b" } } };
            Double[][][] c = { { { 1.1 } }, { { 2.2 } } };
            String[][][] result = fff.zip(a, b, c, 0, "x", 0.0, (i, s, d) -> i + s + d, String.class);
            Assertions.assertEquals(2, result.length);
            Assertions.assertEquals("1a1.1", result[0][0][0]);
            Assertions.assertEquals("0b0.0", result[0][0][1]);
            Assertions.assertEquals("0x2.2", result[1][0][0]);

            // Test with complex default handling
            Integer[][][] ints = { { { 1 } } };
            String[][][] strs = { { { "A", "B" } }, { { "C" } } };
            Boolean[][][] bools = { { { true } }, { { false, true } }, { { false } } };
            String[][][] combined = fff.zip(ints, strs, bools, -1, "?", false, (i, s, f) -> String.format("[%d,%s,%s]", i, s, f ? "T" : "F"), String.class);
            Assertions.assertEquals(3, combined.length);
            Assertions.assertEquals("[1,A,T]", combined[0][0][0]);
            Assertions.assertEquals("[-1,B,F]", combined[0][0][1]);
            Assertions.assertEquals("[-1,C,F]", combined[1][0][0]);
            Assertions.assertEquals("[-1,?,T]", combined[1][0][1]);
            Assertions.assertEquals("[-1,?,F]", combined[2][0][0]);
        }

        @Test
        public void testTotalCountOfElements() {
            // Test with regular array
            Object[][][] array = { { { 1, 2 }, { 3 } }, null, { { 4, 5, 6 } } };
            long count = fff.elementCount(array);
            Assertions.assertEquals(6, count);

            // Test with empty array
            Object[][][] emptyArray = new Object[0][][];
            long emptyCount = fff.elementCount(emptyArray);
            Assertions.assertEquals(0, emptyCount);

            // Test with all null/empty elements
            Object[][][] nullArray = { null, { null, {} }, { {} } };
            long nullCount = fff.elementCount(nullArray);
            Assertions.assertEquals(0, nullCount);

            // Test with mixed elements
            Object[][][] mixedArray = { { { 1, 2, 3 } }, { {} }, { { 4 } }, null, { { 5, 6, 7, 8 } } };
            long mixedCount = fff.elementCount(mixedArray);
            Assertions.assertEquals(8, mixedCount);

            // Test with jagged array
            Object[][][] jaggedArray = { { { 1 }, { 2, 3 }, { 4, 5, 6 } }, { { 7, 8 } }, { { 9, 10, 11, 12 } } };
            long jaggedCount = fff.elementCount(jaggedArray);
            Assertions.assertEquals(12, jaggedCount);
        }

        @Test
        public void testPrintln() {
            // Test with normal array
            String[][][] data = { { { "a", "b" } }, { { "c", "d" }, { "e" } } };
            String output = fff.println(data);
            Assertions.assertTrue(output.contains("a"));
            Assertions.assertTrue(output.contains("b"));
            Assertions.assertTrue(output.contains("c"));
            Assertions.assertTrue(output.contains("d"));
            Assertions.assertTrue(output.contains("e"));

            // Test with null array
            String nullOutput = fff.println(null);
            Assertions.assertEquals("null", nullOutput.trim());

            // Test with empty array
            String[][][] emptyArray = new String[0][][];
            String emptyOutput = fff.println(emptyArray);
            Assertions.assertEquals("[]", emptyOutput.trim());

            // Test with null sub-arrays
            Integer[][][] mixedArray = { { { 1, 2 } }, null, { { 3, 4 } } };
            String mixedOutput = fff.println(mixedArray);
            Assertions.assertTrue(mixedOutput.contains("1"));
            Assertions.assertTrue(mixedOutput.contains("2"));
            Assertions.assertTrue(mixedOutput.contains("null"));
            Assertions.assertTrue(mixedOutput.contains("3"));
            Assertions.assertTrue(mixedOutput.contains("4"));

            // Test with empty sub-arrays
            Integer[][][] emptySubArrays = { { { 1 } }, { {} }, { { 2 } } };
            String emptySubOutput = fff.println(emptySubArrays);
            Assertions.assertTrue(emptySubOutput.contains("1"));
            Assertions.assertTrue(emptySubOutput.contains("[]"));
            Assertions.assertTrue(emptySubOutput.contains("2"));
        }

        // Edge case tests
        @Test
        public void testEdgeCasesWithNullElements() throws Exception {
            // Test updateAll with null elements in array
            Integer[][][] arr = { { { 1, null, 2 } }, { { null, 3 } } };
            fff.updateAll(arr, n -> n == null ? 0 : n * 2);
            Assertions.assertEquals(2, arr[0][0][0]);
            Assertions.assertEquals(0, arr[0][0][1]);
            Assertions.assertEquals(4, arr[0][0][2]);
            Assertions.assertEquals(0, arr[1][0][0]);
            Assertions.assertEquals(6, arr[1][0][1]);

            // Test map with function that can handle nulls
            String[][][] strArr = { { { null, "hello" } }, { { null, null } } };
            String[][][] mapped = fff.map(strArr, s -> s == null ? "NULL" : s.toUpperCase());
            Assertions.assertEquals("NULL", mapped[0][0][0]);
            Assertions.assertEquals("HELLO", mapped[0][0][1]);
            Assertions.assertEquals("NULL", mapped[1][0][0]);
            Assertions.assertEquals("NULL", mapped[1][0][1]);
        }

        @Test
        public void testLargeArrayOperations() throws Exception {
            // Test with larger arrays
            int size = 10;
            Integer[][][] largeArr = new Integer[size][][];
            for (int i = 0; i < size; i++) {
                largeArr[i] = new Integer[size][];
                for (int j = 0; j < size; j++) {
                    largeArr[i][j] = new Integer[size];
                    for (int k = 0; k < size; k++) {
                        largeArr[i][j][k] = i * 100 + j * 10 + k;
                    }
                }
            }

            // Test elementCount
            long count = fff.elementCount(largeArr);
            Assertions.assertEquals(1000, count);

            // Test flatten
            Integer[] flattened = fff.flatten(largeArr);
            Assertions.assertEquals(1000, flattened.length);
            Assertions.assertEquals(0, flattened[0]);
            Assertions.assertEquals(999, flattened[999]);

            // Test map
            Integer[][][] doubled = fff.map(largeArr, n -> n * 2);
            Assertions.assertEquals(0, doubled[0][0][0]);
            Assertions.assertEquals(1998, doubled[9][9][9]);
        }

    }

    @Nested
    /**
     * Comprehensive unit tests for the Arrays utility class.
     * This test class covers a representative sample of the extensive array manipulation methods
     * provided by the Arrays class, including reshaping, flattening, mapping, statistical operations,
     * and various transformations across different primitive types and Object arrays.
     */
    @Tag("2025")
    class Arrays2025Test extends TestBase {

        // ============================================
        // Tests for println methods
        // ============================================

        @Test
        public void testPrintln_1D_ObjectArray() {
            Object[] arr = { "Hello", "World", 123 };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
            assertTrue(result.contains("Hello"));
            assertTrue(result.contains("World"));
        }

        @Test
        public void testPrintln_2D_ObjectArray() {
            Object[][] arr = { { "A", "B" }, { "C", "D" } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_ObjectArray() {
            Object[][][] arr = { { { "A" } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_BooleanArray() {
            boolean[][][] arr = { { { true, false } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_1D_CharArray() {
            char[] arr = { 'a', 'b', 'c' };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_1D_ByteArray() {
            byte[] arr = { 1, 2, 3 };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_1D_ShortArray() {
            short[] arr = { 1, 2, 3 };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_ShortArray() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_ShortArray() {
            short[][][] arr = { { { 1, 2 } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_1D_IntArray() {
            int[] arr = { 1, 2, 3 };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_IntArray() {
            int[][] arr = { { 1, 2 }, { 3, 4 } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_IntArray() {
            int[][][] arr = { { { 1, 2 } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_1D_LongArray() {
            long[] arr = { 1L, 2L, 3L };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_LongArray() {
            long[][] arr = { { 1L, 2L }, { 3L, 4L } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_LongArray() {
            long[][][] arr = { { { 1L, 2L } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_1D_FloatArray() {
            float[] arr = { 1.0f, 2.0f, 3.0f };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_FloatArray() {
            float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_FloatArray() {
            float[][][] arr = { { { 1.0f, 2.0f } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_1D_DoubleArray() {
            double[] arr = { 1.0, 2.0, 3.0 };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_DoubleArray() {
            double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_DoubleArray() {
            double[][][] arr = { { { 1.0, 2.0 } } };
            String result = Arrays.printAndReturn(arr);
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
        // Tests for mapToObj two-dimensional arrays
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
        // Tests for mapToObj three-dimensional arrays
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
        // Tests for minSubArrayLength and maxSubArrayLength
        // ============================================

        @Test
        public void testMinSubArrayLen_Boolean2D() {
            boolean[][] a = { { true, false }, { true }, { true, false, true } };
            int result = Arrays.minSubArrayLength(a);

            assertEquals(1, result);
        }

        @Test
        public void testMaxSubArrayLen_Boolean2D() {
            boolean[][] a = { { true, false }, { true }, { true, false, true } };
            int result = Arrays.maxSubArrayLength(a);

            assertEquals(3, result);
        }

        @Test
        public void testMinSubArrayLen_Char2D() {
            char[][] a = { { 'a', 'b' }, { 'c' }, { 'd', 'e', 'f' } };
            int result = Arrays.minSubArrayLength(a);

            assertEquals(1, result);
        }

        @Test
        public void testMaxSubArrayLen_Char2D() {
            char[][] a = { { 'a', 'b' }, { 'c' }, { 'd', 'e', 'f' } };
            int result = Arrays.maxSubArrayLength(a);

            assertEquals(3, result);
        }

        @Test
        public void testMinSubArrayLen_Int2D() {
            int[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
            int result = Arrays.minSubArrayLength(a);

            assertEquals(1, result);
        }

        @Test
        public void testMaxSubArrayLen_Int2D() {
            int[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
            int result = Arrays.maxSubArrayLength(a);

            assertEquals(3, result);
        }

        // ============================================
        // Tests for applyOnFlattened methods
        // ============================================

        @Test
        public void testFlatOp_Boolean2D() {
            boolean[][] a = { { true, false }, { true, false } };
            List<Boolean> result = new ArrayList<>();

            Arrays.applyOnFlattened(a, subArray -> {
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

            Arrays.applyOnFlattened(a, subArray -> {
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

            Arrays.applyOnFlattened(a, subArray -> {
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

            Arrays.applyOnFlattened(a, subArray -> {
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

            Arrays.applyOnFlattened(a, subArray -> {
                for (int val : subArray) {
                    result.add(val);
                }
            });

            assertEquals(3, result.size());
        }

        // ============================================
        // Tests for two-dimensional array mapping
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
        // Tests for three-dimensional array mapping
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
        // Tests for two-dimensional updateAll methods
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
        // Tests for three-dimensional updateAll methods
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
        // Tests for two-dimensional replaceIf methods
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
        // Tests for three-dimensional replaceIf methods
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
            assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_NullCharArray() {
            char[] a = null;
            String[] result = Arrays.mapToObj(a, c -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_NullByteArray() {
            byte[] a = null;
            String[] result = Arrays.mapToObj(a, b -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_NullShortArray() {
            short[] a = null;
            String[] result = Arrays.mapToObj(a, s -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_NullIntArray() {
            int[] a = null;
            String[] result = Arrays.mapToObj(a, i -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_NullLongArray() {
            long[] a = null;
            String[] result = Arrays.mapToObj(a, l -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_NullFloatArray() {
            float[] a = null;
            String[] result = Arrays.mapToObj(a, f -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_NullDoubleArray() {
            double[] a = null;
            String[] result = Arrays.mapToObj(a, d -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_Null2DIntArray() {
            int[][] a = null;
            String[][] result = Arrays.mapToObj(a, i -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        public void testMapToObj_Null3DIntArray() {
            int[][][] a = null;
            String[][][] result = Arrays.mapToObj(a, i -> "X", String.class);
            assertEquals(0, result.length);
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

        // ============================================
        // Tests for elementCount methods
        // ============================================

        @Test
        public void testTotalCountOfElements_Boolean2D() {
            boolean[][] a = { { true, false }, { true }, { true, false, true } };
            long result = Arrays.elementCount(a);
            assertEquals(6, result);
        }

        @Test
        public void testTotalCountOfElements_Boolean3D() {
            boolean[][][] a = { { { true, false }, { true } }, { { false } } };
            long result = Arrays.elementCount(a);
            assertEquals(4, result);
        }

        @Test
        public void testTotalCountOfElements_Char2D() {
            char[][] a = { { 'a', 'b' }, { 'c' }, { 'd', 'e', 'f' } };
            long result = Arrays.elementCount(a);
            assertEquals(6, result);
        }

        @Test
        public void testTotalCountOfElements_Char3D() {
            char[][][] a = { { { 'a', 'b' }, { 'c' } }, { { 'd' } } };
            long result = Arrays.elementCount(a);
            assertEquals(4, result);
        }

        @Test
        public void testTotalCountOfElements_Byte2D() {
            byte[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
            long result = Arrays.elementCount(a);
            assertEquals(6, result);
        }

        @Test
        public void testTotalCountOfElements_Byte3D() {
            byte[][][] a = { { { 1, 2 }, { 3 } }, { { 4 } } };
            long result = Arrays.elementCount(a);
            assertEquals(4, result);
        }

        @Test
        public void testTotalCountOfElements_Short2D() {
            short[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
            long result = Arrays.elementCount(a);
            assertEquals(6, result);
        }

        @Test
        public void testTotalCountOfElements_Short3D() {
            short[][][] a = { { { 1, 2 }, { 3 } }, { { 4 } } };
            long result = Arrays.elementCount(a);
            assertEquals(4, result);
        }

        @Test
        public void testTotalCountOfElements_Int2D() {
            int[][] a = { { 1, 2 }, { 3 }, { 4, 5, 6 } };
            long result = Arrays.elementCount(a);
            assertEquals(6, result);
        }

        @Test
        public void testTotalCountOfElements_Int3D() {
            int[][][] a = { { { 1, 2 }, { 3 } }, { { 4 } } };
            long result = Arrays.elementCount(a);
            assertEquals(4, result);
        }

        @Test
        public void testTotalCountOfElements_Long2D() {
            long[][] a = { { 1L, 2L }, { 3L }, { 4L, 5L, 6L } };
            long result = Arrays.elementCount(a);
            assertEquals(6, result);
        }

        @Test
        public void testTotalCountOfElements_Long3D() {
            long[][][] a = { { { 1L, 2L }, { 3L } }, { { 4L } } };
            long result = Arrays.elementCount(a);
            assertEquals(4, result);
        }

        @Test
        public void testTotalCountOfElements_Float2D() {
            float[][] a = { { 1.0f, 2.0f }, { 3.0f }, { 4.0f, 5.0f, 6.0f } };
            long result = Arrays.elementCount(a);
            assertEquals(6, result);
        }

        @Test
        public void testTotalCountOfElements_Float3D() {
            float[][][] a = { { { 1.0f, 2.0f }, { 3.0f } }, { { 4.0f } } };
            long result = Arrays.elementCount(a);
            assertEquals(4, result);
        }

        @Test
        public void testTotalCountOfElements_Double2D() {
            double[][] a = { { 1.0, 2.0 }, { 3.0 }, { 4.0, 5.0, 6.0 } };
            long result = Arrays.elementCount(a);
            assertEquals(6, result);
        }

        @Test
        public void testTotalCountOfElements_Double3D() {
            double[][][] a = { { { 1.0, 2.0 }, { 3.0 } }, { { 4.0 } } };
            long result = Arrays.elementCount(a);
            assertEquals(4, result);
        }

        @Test
        public void testZip_Boolean1D_BinaryOp_WithDefaults() {
            boolean[] a = { true, false };
            boolean[] b = { false, false, true };
            boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

            assertEquals(3, result.length);
            assertTrue(result[0]);
            assertFalse(result[1]);
            assertTrue(result[2]);
        }

        @Test
        public void testZip_Boolean1D_TernaryOp_WithDefaults() {
            boolean[] a = { true };
            boolean[] b = { false, true };
            boolean[] c = { true, true };
            boolean[] result = Arrays.zip(a, b, c, false, false, true, (x, y, z) -> (x || y) && z);

            assertEquals(2, result.length);
            assertTrue(result[0]);
            assertTrue(result[1]);
        }

        // ============================================
        // Tests for zip methods - Int arrays
        // ============================================

        @Test
        public void testZip_Int1D_BinaryOp() {
            int[] a = { 1, 2, 3 };
            int[] b = { 4, 5, 6 };
            int[] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertArrayEquals(new int[] { 5, 7, 9 }, result);
        }

        @Test
        public void testZip_Int1D_BinaryOp_WithDefaults() {
            int[] a = { 1, 2 };
            int[] b = { 3, 4, 5 };
            int[] result = Arrays.zip(a, b, 0, 0, (x, y) -> x + y);

            assertArrayEquals(new int[] { 4, 6, 5 }, result);
        }

        @Test
        public void testZip_Int1D_TernaryOp() {
            int[] a = { 1, 2 };
            int[] b = { 3, 4 };
            int[] c = { 5, 6 };
            int[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertArrayEquals(new int[] { 9, 12 }, result);
        }

        @Test
        public void testZip_Int1D_TernaryOp_WithDefaults() {
            int[] a = { 1 };
            int[] b = { 2, 3 };
            int[] c = { 4, 5 };
            int[] result = Arrays.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);

            assertArrayEquals(new int[] { 7, 8 }, result);
        }

        // ============================================
        // Tests for zip methods - Long arrays
        // ============================================

        @Test
        public void testZip_Long1D_BinaryOp() {
            long[] a = { 1L, 2L, 3L };
            long[] b = { 4L, 5L, 6L };
            long[] result = Arrays.zip(a, b, (x, y) -> x * y);

            assertArrayEquals(new long[] { 4L, 10L, 18L }, result);
        }

        @Test
        public void testZip_Long1D_BinaryOp_WithDefaults() {
            long[] a = { 1L, 2L };
            long[] b = { 3L, 4L, 5L };
            long[] result = Arrays.zip(a, b, 1L, 1L, (x, y) -> x * y);

            assertArrayEquals(new long[] { 3L, 8L, 5L }, result);
        }

        // ============================================
        // Tests for zip methods - Double arrays
        // ============================================

        @Test
        public void testZip_Double1D_BinaryOp() {
            double[] a = { 1.0, 2.0, 3.0 };
            double[] b = { 4.0, 5.0, 6.0 };
            double[] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertArrayEquals(new double[] { 5.0, 7.0, 9.0 }, result, 0.0001);
        }

        @Test
        public void testZip_Double1D_BinaryOp_WithDefaults() {
            double[] a = { 1.0, 2.0 };
            double[] b = { 3.0, 4.0, 5.0 };
            double[] result = Arrays.zip(a, b, 0.0, 0.0, (x, y) -> x + y);

            assertArrayEquals(new double[] { 4.0, 6.0, 5.0 }, result, 0.0001);
        }

        // ============================================
        // Tests for zip methods - Char arrays
        // ============================================

        @Test
        public void testZip_Char1D_BinaryOp() {
            char[] a = { 'a', 'b', 'c' };
            char[] b = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, (x, y) -> x);

            assertArrayEquals(new char[] { 'a', 'b', 'c' }, result);
        }

        // ============================================
        // Tests for zip methods - Byte arrays
        // ============================================

        @Test
        public void testZip_Byte1D_BinaryOp() {
            byte[] a = { 1, 2, 3 };
            byte[] b = { 4, 5, 6 };
            byte[] result = Arrays.zip(a, b, (x, y) -> (byte) (x + y));

            assertArrayEquals(new byte[] { 5, 7, 9 }, result);
        }

        // ============================================
        // Tests for zip methods - Short arrays
        // ============================================

        @Test
        public void testZip_Short1D_BinaryOp() {
            short[] a = { 1, 2, 3 };
            short[] b = { 4, 5, 6 };
            short[] result = Arrays.zip(a, b, (x, y) -> (short) (x + y));

            assertArrayEquals(new short[] { 5, 7, 9 }, result);
        }

        // ============================================
        // Tests for zip methods - Float arrays
        // ============================================

        @Test
        public void testZip_Float1D_BinaryOp() {
            float[] a = { 1.0f, 2.0f, 3.0f };
            float[] b = { 4.0f, 5.0f, 6.0f };
            float[] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertArrayEquals(new float[] { 5.0f, 7.0f, 9.0f }, result, 0.0001f);
        }

        // ============================================
        // Tests for type conversion methods - toBoolean
        // ============================================

        @Test
        public void testToBoolean_ByteArray() {
            byte[] a = { 0, 1, 0, 1 };
            boolean[] result = Arrays.toBoolean(a);

            assertArrayEquals(new boolean[] { false, true, false, true }, result);
        }

        @Test
        public void testToBoolean_Byte2DArray() {
            byte[][] a = { { 0, 1 }, { 1, 0 } };
            boolean[][] result = Arrays.toBoolean(a);

            assertEquals(2, result.length);
            assertFalse(result[0][0]);
            assertTrue(result[0][1]);
            assertTrue(result[1][0]);
            assertFalse(result[1][1]);
        }

        @Test
        public void testToBoolean_Byte3DArray() {
            byte[][][] a = { { { 0, 1 } } };
            boolean[][][] result = Arrays.toBoolean(a);

            assertEquals(1, result.length);
            assertFalse(result[0][0][0]);
            assertTrue(result[0][0][1]);
        }

        @Test
        public void testToBoolean_IntArray() {
            int[] a = { 0, 1, 0, 1 };
            boolean[] result = Arrays.toBoolean(a);

            assertArrayEquals(new boolean[] { false, true, false, true }, result);
        }

        @Test
        public void testToBoolean_Int2DArray() {
            int[][] a = { { 0, 1 }, { 1, 0 } };
            boolean[][] result = Arrays.toBoolean(a);

            assertEquals(2, result.length);
            assertFalse(result[0][0]);
            assertTrue(result[0][1]);
        }

        @Test
        public void testToBoolean_Int3DArray() {
            int[][][] a = { { { 0, 1 } } };
            boolean[][][] result = Arrays.toBoolean(a);

            assertEquals(1, result.length);
            assertFalse(result[0][0][0]);
            assertTrue(result[0][0][1]);
        }

        // ============================================
        // Tests for type conversion methods - toChar
        // ============================================

        @Test
        public void testToChar_IntArray() {
            int[] a = { 65, 66, 67 };
            char[] result = Arrays.toChar(a);

            assertArrayEquals(new char[] { 'A', 'B', 'C' }, result);
        }

        @Test
        public void testToChar_Int2DArray() {
            int[][] a = { { 65, 66 }, { 67, 68 } };
            char[][] result = Arrays.toChar(a);

            assertEquals(2, result.length);
            assertEquals('A', result[0][0]);
            assertEquals('B', result[0][1]);
            assertEquals('C', result[1][0]);
            assertEquals('D', result[1][1]);
        }

        @Test
        public void testToChar_Int3DArray() {
            int[][][] a = { { { 65, 66 } } };
            char[][][] result = Arrays.toChar(a);

            assertEquals('A', result[0][0][0]);
            assertEquals('B', result[0][0][1]);
        }

        // ============================================
        // Tests for type conversion methods - toByte
        // ============================================

        @Test
        public void testToByte_BooleanArray() {
            boolean[] a = { true, false, true };
            byte[] result = Arrays.toByte(a);

            assertArrayEquals(new byte[] { 1, 0, 1 }, result);
        }

        @Test
        public void testToByte_Boolean2DArray() {
            boolean[][] a = { { true, false }, { false, true } };
            byte[][] result = Arrays.toByte(a);

            assertEquals(2, result.length);
            assertEquals(1, result[0][0]);
            assertEquals(0, result[0][1]);
            assertEquals(0, result[1][0]);
            assertEquals(1, result[1][1]);
        }

        @Test
        public void testToByte_Boolean3DArray() {
            boolean[][][] a = { { { true, false } } };
            byte[][][] result = Arrays.toByte(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(0, result[0][0][1]);
        }

        // ============================================
        // Tests for type conversion methods - toShort
        // ============================================

        @Test
        public void testToShort_ByteArray() {
            byte[] a = { 1, 2, 3 };
            short[] result = Arrays.toShort(a);

            assertArrayEquals(new short[] { 1, 2, 3 }, result);
        }

        @Test
        public void testToShort_Byte2DArray() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            short[][] result = Arrays.toShort(a);

            assertEquals(2, result.length);
            assertEquals(1, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(3, result[1][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        public void testToShort_Byte3DArray() {
            byte[][][] a = { { { 1, 2 } } };
            short[][][] result = Arrays.toShort(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
        }

        // ============================================
        // Tests for type conversion methods - toInt
        // ============================================

        @Test
        public void testToInt_BooleanArray() {
            boolean[] a = { true, false, true };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 1, 0, 1 }, result);
        }

        @Test
        public void testToInt_Boolean2DArray() {
            boolean[][] a = { { true, false }, { false, true } };
            int[][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0]);
            assertEquals(0, result[0][1]);
            assertEquals(0, result[1][0]);
            assertEquals(1, result[1][1]);
        }

        @Test
        public void testToInt_Boolean3DArray() {
            boolean[][][] a = { { { true, false } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(0, result[0][0][1]);
        }

        @Test
        public void testToInt_CharArray() {
            char[] a = { 'A', 'B', 'C' };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 65, 66, 67 }, result);
        }

        @Test
        public void testToInt_Char2DArray() {
            char[][] a = { { 'A', 'B' }, { 'C', 'D' } };
            int[][] result = Arrays.toInt(a);

            assertEquals(65, result[0][0]);
            assertEquals(66, result[0][1]);
            assertEquals(67, result[1][0]);
            assertEquals(68, result[1][1]);
        }

        @Test
        public void testToInt_Char3DArray() {
            char[][][] a = { { { 'A', 'B' } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(65, result[0][0][0]);
            assertEquals(66, result[0][0][1]);
        }

        @Test
        public void testToInt_ByteArray() {
            byte[] a = { 1, 2, 3 };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 1, 2, 3 }, result);
        }

        @Test
        public void testToInt_Byte2DArray() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            int[][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(3, result[1][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        public void testToInt_Byte3DArray() {
            byte[][][] a = { { { 1, 2 } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
        }

        @Test
        public void testToInt_ShortArray() {
            short[] a = { 1, 2, 3 };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 1, 2, 3 }, result);
        }

        @Test
        public void testToInt_Short2DArray() {
            short[][] a = { { 1, 2 }, { 3, 4 } };
            int[][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(3, result[1][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        public void testToInt_Short3DArray() {
            short[][][] a = { { { 1, 2 } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
        }

        @Test
        public void testToInt_FloatArray() {
            float[] a = { 1.5f, 2.5f, 3.5f };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 1, 2, 3 }, result);
        }

        @Test
        public void testToInt_Float2DArray() {
            float[][] a = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            int[][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(3, result[1][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        public void testToInt_Float3DArray() {
            float[][][] a = { { { 1.5f, 2.5f } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
        }

        @Test
        public void testToInt_DoubleArray() {
            double[] a = { 1.9, 2.9, 3.9 };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 1, 2, 3 }, result);
        }

        @Test
        public void testToInt_Double2DArray() {
            double[][] a = { { 1.9, 2.9 }, { 3.9, 4.9 } };
            int[][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(3, result[1][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        public void testToInt_Double3DArray() {
            double[][][] a = { { { 1.9, 2.9 } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
        }

        // ============================================
        // Tests for type conversion methods - toLong
        // ============================================

        @Test
        public void testToLong_ByteArray() {
            byte[] a = { 1, 2, 3 };
            long[] result = Arrays.toLong(a);

            assertArrayEquals(new long[] { 1L, 2L, 3L }, result);
        }

        @Test
        public void testToLong_Byte2DArray() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            long[][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0]);
            assertEquals(2L, result[0][1]);
            assertEquals(3L, result[1][0]);
            assertEquals(4L, result[1][1]);
        }

        @Test
        public void testToLong_Byte3DArray() {
            byte[][][] a = { { { 1, 2 } } };
            long[][][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0][0]);
            assertEquals(2L, result[0][0][1]);
        }

        @Test
        public void testToLong_ShortArray() {
            short[] a = { 10, 20, 30 };
            long[] result = Arrays.toLong(a);

            assertArrayEquals(new long[] { 10L, 20L, 30L }, result);
        }

        @Test
        public void testToLong_Short2DArray() {
            short[][] a = { { 10, 20 }, { 30, 40 } };
            long[][] result = Arrays.toLong(a);

            assertEquals(10L, result[0][0]);
            assertEquals(20L, result[0][1]);
            assertEquals(30L, result[1][0]);
            assertEquals(40L, result[1][1]);
        }

        @Test
        public void testToLong_Short3DArray() {
            short[][][] a = { { { 10, 20 } } };
            long[][][] result = Arrays.toLong(a);

            assertEquals(10L, result[0][0][0]);
            assertEquals(20L, result[0][0][1]);
        }

        @Test
        public void testToLong_IntArray() {
            int[] a = { 100, 200, 300 };
            long[] result = Arrays.toLong(a);

            assertArrayEquals(new long[] { 100L, 200L, 300L }, result);
        }

        @Test
        public void testToLong_Int2DArray() {
            int[][] a = { { 100, 200 }, { 300, 400 } };
            long[][] result = Arrays.toLong(a);

            assertEquals(100L, result[0][0]);
            assertEquals(200L, result[0][1]);
            assertEquals(300L, result[1][0]);
            assertEquals(400L, result[1][1]);
        }

        @Test
        public void testToLong_Int3DArray() {
            int[][][] a = { { { 100, 200 } } };
            long[][][] result = Arrays.toLong(a);

            assertEquals(100L, result[0][0][0]);
            assertEquals(200L, result[0][0][1]);
        }

        @Test
        public void testToLong_FloatArray() {
            float[] a = { 1.5f, 2.5f, 3.5f };
            long[] result = Arrays.toLong(a);

            assertArrayEquals(new long[] { 1L, 2L, 3L }, result);
        }

        @Test
        public void testToLong_Float2DArray() {
            float[][] a = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            long[][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0]);
            assertEquals(2L, result[0][1]);
            assertEquals(3L, result[1][0]);
            assertEquals(4L, result[1][1]);
        }

        @Test
        public void testToLong_Float3DArray() {
            float[][][] a = { { { 1.5f, 2.5f } } };
            long[][][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0][0]);
            assertEquals(2L, result[0][0][1]);
        }

        @Test
        public void testToLong_DoubleArray() {
            double[] a = { 1.9, 2.9, 3.9 };
            long[] result = Arrays.toLong(a);

            assertArrayEquals(new long[] { 1L, 2L, 3L }, result);
        }

        @Test
        public void testToLong_Double2DArray() {
            double[][] a = { { 1.9, 2.9 }, { 3.9, 4.9 } };
            long[][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0]);
            assertEquals(2L, result[0][1]);
            assertEquals(3L, result[1][0]);
            assertEquals(4L, result[1][1]);
        }

        @Test
        public void testToLong_Double3DArray() {
            double[][][] a = { { { 1.9, 2.9 } } };
            long[][][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0][0]);
            assertEquals(2L, result[0][0][1]);
        }

        // ============================================
        // Tests for type conversion methods - toFloat
        // ============================================

        @Test
        public void testToFloat_ByteArray() {
            byte[] a = { 1, 2, 3 };
            float[] result = Arrays.toFloat(a);

            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, result, 0.0001f);
        }

        @Test
        public void testToFloat_Byte2DArray() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            float[][] result = Arrays.toFloat(a);

            assertEquals(1.0f, result[0][0], 0.0001f);
            assertEquals(2.0f, result[0][1], 0.0001f);
            assertEquals(3.0f, result[1][0], 0.0001f);
            assertEquals(4.0f, result[1][1], 0.0001f);
        }

        @Test
        public void testToFloat_Byte3DArray() {
            byte[][][] a = { { { 1, 2 } } };
            float[][][] result = Arrays.toFloat(a);

            assertEquals(1.0f, result[0][0][0], 0.0001f);
            assertEquals(2.0f, result[0][0][1], 0.0001f);
        }

        @Test
        public void testToFloat_ShortArray() {
            short[] a = { 10, 20, 30 };
            float[] result = Arrays.toFloat(a);

            assertArrayEquals(new float[] { 10.0f, 20.0f, 30.0f }, result, 0.0001f);
        }

        @Test
        public void testToFloat_Short2DArray() {
            short[][] a = { { 10, 20 }, { 30, 40 } };
            float[][] result = Arrays.toFloat(a);

            assertEquals(10.0f, result[0][0], 0.0001f);
            assertEquals(20.0f, result[0][1], 0.0001f);
            assertEquals(30.0f, result[1][0], 0.0001f);
            assertEquals(40.0f, result[1][1], 0.0001f);
        }

        @Test
        public void testToFloat_Short3DArray() {
            short[][][] a = { { { 10, 20 } } };
            float[][][] result = Arrays.toFloat(a);

            assertEquals(10.0f, result[0][0][0], 0.0001f);
            assertEquals(20.0f, result[0][0][1], 0.0001f);
        }

        @Test
        public void testToFloat_IntArray() {
            int[] a = { 100, 200, 300 };
            float[] result = Arrays.toFloat(a);

            assertArrayEquals(new float[] { 100.0f, 200.0f, 300.0f }, result, 0.0001f);
        }

        @Test
        public void testToFloat_Int2DArray() {
            int[][] a = { { 100, 200 }, { 300, 400 } };
            float[][] result = Arrays.toFloat(a);

            assertEquals(100.0f, result[0][0], 0.0001f);
            assertEquals(200.0f, result[0][1], 0.0001f);
            assertEquals(300.0f, result[1][0], 0.0001f);
            assertEquals(400.0f, result[1][1], 0.0001f);
        }

        @Test
        public void testToFloat_Int3DArray() {
            int[][][] a = { { { 100, 200 } } };
            float[][][] result = Arrays.toFloat(a);

            assertEquals(100.0f, result[0][0][0], 0.0001f);
            assertEquals(200.0f, result[0][0][1], 0.0001f);
        }

        @Test
        public void testToFloat_LongArray() {
            long[] a = { 1000L, 2000L, 3000L };
            float[] result = Arrays.toFloat(a);

            assertArrayEquals(new float[] { 1000.0f, 2000.0f, 3000.0f }, result, 0.0001f);
        }

        @Test
        public void testToFloat_Long2DArray() {
            long[][] a = { { 1000L, 2000L }, { 3000L, 4000L } };
            float[][] result = Arrays.toFloat(a);

            assertEquals(1000.0f, result[0][0], 0.0001f);
            assertEquals(2000.0f, result[0][1], 0.0001f);
            assertEquals(3000.0f, result[1][0], 0.0001f);
            assertEquals(4000.0f, result[1][1], 0.0001f);
        }

        @Test
        public void testToFloat_Long3DArray() {
            long[][][] a = { { { 1000L, 2000L } } };
            float[][][] result = Arrays.toFloat(a);

            assertEquals(1000.0f, result[0][0][0], 0.0001f);
            assertEquals(2000.0f, result[0][0][1], 0.0001f);
        }

        // ============================================
        // Tests for type conversion methods - toDouble
        // ============================================

        @Test
        public void testToDouble_ByteArray() {
            byte[] a = { 1, 2, 3 };
            double[] result = Arrays.toDouble(a);

            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, result, 0.0001);
        }

        @Test
        public void testToDouble_Byte2DArray() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            double[][] result = Arrays.toDouble(a);

            assertEquals(1.0, result[0][0], 0.0001);
            assertEquals(2.0, result[0][1], 0.0001);
            assertEquals(3.0, result[1][0], 0.0001);
            assertEquals(4.0, result[1][1], 0.0001);
        }

        @Test
        public void testToDouble_Byte3DArray() {
            byte[][][] a = { { { 1, 2 } } };
            double[][][] result = Arrays.toDouble(a);

            assertEquals(1.0, result[0][0][0], 0.0001);
            assertEquals(2.0, result[0][0][1], 0.0001);
        }

        @Test
        public void testToDouble_ShortArray() {
            short[] a = { 10, 20, 30 };
            double[] result = Arrays.toDouble(a);

            assertArrayEquals(new double[] { 10.0, 20.0, 30.0 }, result, 0.0001);
        }

        @Test
        public void testToDouble_Short2DArray() {
            short[][] a = { { 10, 20 }, { 30, 40 } };
            double[][] result = Arrays.toDouble(a);

            assertEquals(10.0, result[0][0], 0.0001);
            assertEquals(20.0, result[0][1], 0.0001);
            assertEquals(30.0, result[1][0], 0.0001);
            assertEquals(40.0, result[1][1], 0.0001);
        }

        @Test
        public void testToDouble_Short3DArray() {
            short[][][] a = { { { 10, 20 } } };
            double[][][] result = Arrays.toDouble(a);

            assertEquals(10.0, result[0][0][0], 0.0001);
            assertEquals(20.0, result[0][0][1], 0.0001);
        }

        @Test
        public void testToDouble_IntArray() {
            int[] a = { 100, 200, 300 };
            double[] result = Arrays.toDouble(a);

            assertArrayEquals(new double[] { 100.0, 200.0, 300.0 }, result, 0.0001);
        }

        @Test
        public void testToDouble_Int2DArray() {
            int[][] a = { { 100, 200 }, { 300, 400 } };
            double[][] result = Arrays.toDouble(a);

            assertEquals(100.0, result[0][0], 0.0001);
            assertEquals(200.0, result[0][1], 0.0001);
            assertEquals(300.0, result[1][0], 0.0001);
            assertEquals(400.0, result[1][1], 0.0001);
        }

        @Test
        public void testToDouble_Int3DArray() {
            int[][][] a = { { { 100, 200 } } };
            double[][][] result = Arrays.toDouble(a);

            assertEquals(100.0, result[0][0][0], 0.0001);
            assertEquals(200.0, result[0][0][1], 0.0001);
        }

        @Test
        public void testToDouble_LongArray() {
            long[] a = { 1000L, 2000L, 3000L };
            double[] result = Arrays.toDouble(a);

            assertArrayEquals(new double[] { 1000.0, 2000.0, 3000.0 }, result, 0.0001);
        }

        @Test
        public void testToDouble_Long2DArray() {
            long[][] a = { { 1000L, 2000L }, { 3000L, 4000L } };
            double[][] result = Arrays.toDouble(a);

            assertEquals(1000.0, result[0][0], 0.0001);
            assertEquals(2000.0, result[0][1], 0.0001);
            assertEquals(3000.0, result[1][0], 0.0001);
            assertEquals(4000.0, result[1][1], 0.0001);
        }

        @Test
        public void testToDouble_Long3DArray() {
            long[][][] a = { { { 1000L, 2000L } } };
            double[][][] result = Arrays.toDouble(a);

            assertEquals(1000.0, result[0][0][0], 0.0001);
            assertEquals(2000.0, result[0][0][1], 0.0001);
        }

        @Test
        public void testToDouble_FloatArray() {
            float[] a = { 1.5f, 2.5f, 3.5f };
            double[] result = Arrays.toDouble(a);

            assertArrayEquals(new double[] { 1.5, 2.5, 3.5 }, result, 0.0001);
        }

        @Test
        public void testToDouble_Float2DArray() {
            float[][] a = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            double[][] result = Arrays.toDouble(a);

            assertEquals(1.5, result[0][0], 0.0001);
            assertEquals(2.5, result[0][1], 0.0001);
            assertEquals(3.5, result[1][0], 0.0001);
            assertEquals(4.5, result[1][1], 0.0001);
        }

        @Test
        public void testToDouble_Float3DArray() {
            float[][][] a = { { { 1.5f, 2.5f } } };
            double[][][] result = Arrays.toDouble(a);

            assertEquals(1.5, result[0][0][0], 0.0001);
            assertEquals(2.5, result[0][0][1], 0.0001);
        }

        // ============================================
        // Tests for zip methods - two-dimensional and three-dimensional arrays
        // ============================================

        @Test
        public void testZip_Int2D_BinaryOp() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            int[][] b = { { 5, 6 }, { 7, 8 } };
            int[][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(2, result.length);
            assertArrayEquals(new int[] { 6, 8 }, result[0]);
            assertArrayEquals(new int[] { 10, 12 }, result[1]);
        }

        @Test
        public void testZip_Int2D_BinaryOp_WithDefaults() {
            int[][] a = { { 1, 2 }, { 3 } };
            int[][] b = { { 4 }, { 5, 6 } };
            int[][] result = Arrays.zip(a, b, 0, 0, (x, y) -> x + y);

            assertEquals(2, result.length);
            assertEquals(5, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(8, result[1][0]);
            assertEquals(6, result[1][1]);
        }

        @Test
        public void testZip_Object2D_BinaryOp_WithDefaults_AllowsNullFirstArray() throws Exception {
            Integer[][] a = null;
            Integer[][] b = { { 10, 20 }, { 30 } };
            Integer[][] result = Arrays.ff.zip(a, b, 0, 0, Integer::sum);

            assertEquals(2, result.length);
            assertArrayEquals(new Integer[] { 10, 20 }, result[0]);
            assertArrayEquals(new Integer[] { 30 }, result[1]);
        }

        @Test
        public void testZip_Object2D_TernaryOp_WithDefaults_AllowsNullFirstArray() throws Exception {
            Integer[][] a = null;
            Integer[][] b = { { 10 }, { 20, 30 } };
            Integer[][] c = { { 1, 2 }, { 3 } };
            Integer[][] result = Arrays.ff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);

            assertEquals(2, result.length);
            assertArrayEquals(new Integer[] { 11, 2 }, result[0]);
            assertArrayEquals(new Integer[] { 23, 30 }, result[1]);
        }

        @Test
        public void testZip_Object2D_BinaryOp_WithDefaults_RejectsUnknownTargetType() {
            Integer[][] a = null;
            Integer[][] b = { { 10 } };

            assertThrows(IllegalArgumentException.class, () -> Arrays.ff.zip(a, b, null, 0, Integer::sum));
        }

        @Test
        public void testZip_Int2D_TernaryOp() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            int[][] b = { { 5, 6 }, { 7, 8 } };
            int[][] c = { { 9, 10 }, { 11, 12 } };
            int[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertEquals(2, result.length);
            assertArrayEquals(new int[] { 15, 18 }, result[0]);
            assertArrayEquals(new int[] { 21, 24 }, result[1]);
        }

        @Test
        public void testZip_Int2D_TernaryOp_WithDefaults() {
            int[][] a = { { 1, 2 } };
            int[][] b = { { 3, 4 }, { 5, 6 } };
            int[][] c = { { 7, 8 }, { 9, 10 } };
            int[][] result = Arrays.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);

            assertEquals(2, result.length);
            assertArrayEquals(new int[] { 11, 14 }, result[0]);
            assertArrayEquals(new int[] { 14, 16 }, result[1]);
        }

        @Test
        public void testZip_Int3D_BinaryOp() {
            int[][][] a = { { { 1, 2 }, { 3, 4 } } };
            int[][][] b = { { { 5, 6 }, { 7, 8 } } };
            int[][][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(1, result.length);
            assertEquals(2, result[0].length);
            assertArrayEquals(new int[] { 6, 8 }, result[0][0]);
            assertArrayEquals(new int[] { 10, 12 }, result[0][1]);
        }

        @Test
        public void testZip_Int3D_BinaryOp_WithDefaults() {
            int[][][] a = { { { 1, 2 } } };
            int[][][] b = { { { 3 }, { 4, 5 } } };
            int[][][] result = Arrays.zip(a, b, 0, 0, (x, y) -> x + y);

            assertEquals(1, result.length);
            assertEquals(2, result[0].length);
            assertEquals(4, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
            assertEquals(4, result[0][1][0]);
            assertEquals(5, result[0][1][1]);
        }

        @Test
        public void testZip_Int3D_TernaryOp() {
            int[][][] a = { { { 1, 2 } } };
            int[][][] b = { { { 3, 4 } } };
            int[][][] c = { { { 5, 6 } } };
            int[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertEquals(1, result.length);
            assertEquals(1, result[0].length);
            assertArrayEquals(new int[] { 9, 12 }, result[0][0]);
        }

        @Test
        public void testZip_Long2D_BinaryOp() {
            long[][] a = { { 1L, 2L }, { 3L, 4L } };
            long[][] b = { { 5L, 6L }, { 7L, 8L } };
            long[][] result = Arrays.zip(a, b, (x, y) -> x * y);

            assertEquals(2, result.length);
            assertArrayEquals(new long[] { 5L, 12L }, result[0]);
            assertArrayEquals(new long[] { 21L, 32L }, result[1]);
        }

        @Test
        public void testZip_Long2D_TernaryOp() {
            long[][] a = { { 1L, 2L } };
            long[][] b = { { 3L, 4L } };
            long[][] c = { { 5L, 6L } };
            long[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertEquals(1, result.length);
            assertArrayEquals(new long[] { 9L, 12L }, result[0]);
        }

        @Test
        public void testZip_Long3D_BinaryOp() {
            long[][][] a = { { { 1L, 2L } } };
            long[][][] b = { { { 3L, 4L } } };
            long[][][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(1, result.length);
            assertArrayEquals(new long[] { 4L, 6L }, result[0][0]);
        }

        @Test
        public void testZip_Double2D_BinaryOp() {
            double[][] a = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            double[][] b = { { 5.0, 6.0 }, { 7.0, 8.0 } };
            double[][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(2, result.length);
            assertArrayEquals(new double[] { 6.0, 8.0 }, result[0], 0.0001);
            assertArrayEquals(new double[] { 10.0, 12.0 }, result[1], 0.0001);
        }

        @Test
        public void testZip_Double2D_TernaryOp() {
            double[][] a = { { 1.0, 2.0 } };
            double[][] b = { { 3.0, 4.0 } };
            double[][] c = { { 5.0, 6.0 } };
            double[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertEquals(1, result.length);
            assertArrayEquals(new double[] { 9.0, 12.0 }, result[0], 0.0001);
        }

        @Test
        public void testZip_Double3D_BinaryOp() {
            double[][][] a = { { { 1.0, 2.0 } } };
            double[][][] b = { { { 3.0, 4.0 } } };
            double[][][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(1, result.length);
            assertArrayEquals(new double[] { 4.0, 6.0 }, result[0][0], 0.0001);
        }

        @Test
        public void testZip_Boolean2D_BinaryOp() {
            boolean[][] a = { { true, false }, { true, true } };
            boolean[][] b = { { false, false }, { true, false } };
            boolean[][] result = Arrays.zip(a, b, (x, y) -> x && y);

            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertFalse(result[0][0]);
            assertFalse(result[0][1]);
            assertTrue(result[1][0]);
            assertFalse(result[1][1]);
        }

        @Test
        public void testZip_Boolean2D_TernaryOp() {
            boolean[][] a = { { true, false } };
            boolean[][] b = { { false, true } };
            boolean[][] c = { { true, true } };
            boolean[][] result = Arrays.zip(a, b, c, (x, y, z) -> (x || y) && z);

            assertEquals(1, result.length);
            assertTrue(result[0][0]);
            assertTrue(result[0][1]);
        }

        @Test
        public void testZip_Boolean3D_BinaryOp() {
            boolean[][][] a = { { { true, false } } };
            boolean[][][] b = { { { false, true } } };
            boolean[][][] result = Arrays.zip(a, b, (x, y) -> x || y);

            assertEquals(1, result.length);
            assertTrue(result[0][0][0]);
            assertTrue(result[0][0][1]);
        }

        @Test
        public void testZip_Char2D_BinaryOp() {
            char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
            char[][] b = { { 'x', 'y' }, { 'z', 'w' } };
            char[][] result = Arrays.zip(a, b, (x, y) -> x);

            assertEquals(2, result.length);
            assertArrayEquals(new char[] { 'a', 'b' }, result[0]);
            assertArrayEquals(new char[] { 'c', 'd' }, result[1]);
        }

        @Test
        public void testZip_Char2D_TernaryOp() {
            char[][] a = { { 'a', 'b' } };
            char[][] b = { { 'c', 'd' } };
            char[][] c = { { 'e', 'f' } };
            char[][] result = Arrays.zip(a, b, c, (x, y, z) -> x);

            assertEquals(1, result.length);
            assertArrayEquals(new char[] { 'a', 'b' }, result[0]);
        }

        @Test
        public void testZip_Char3D_BinaryOp() {
            char[][][] a = { { { 'a', 'b' } } };
            char[][][] b = { { { 'c', 'd' } } };
            char[][][] result = Arrays.zip(a, b, (x, y) -> x);

            assertEquals(1, result.length);
            assertArrayEquals(new char[] { 'a', 'b' }, result[0][0]);
        }

        @Test
        public void testZip_Byte2D_BinaryOp() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            byte[][] b = { { 5, 6 }, { 7, 8 } };
            byte[][] result = Arrays.zip(a, b, (x, y) -> (byte) (x + y));

            assertEquals(2, result.length);
            assertArrayEquals(new byte[] { 6, 8 }, result[0]);
            assertArrayEquals(new byte[] { 10, 12 }, result[1]);
        }

        @Test
        public void testZip_Byte2D_TernaryOp() {
            byte[][] a = { { 1, 2 } };
            byte[][] b = { { 3, 4 } };
            byte[][] c = { { 5, 6 } };
            byte[][] result = Arrays.zip(a, b, c, (x, y, z) -> (byte) (x + y + z));

            assertEquals(1, result.length);
            assertArrayEquals(new byte[] { 9, 12 }, result[0]);
        }

        @Test
        public void testZip_Byte3D_BinaryOp() {
            byte[][][] a = { { { 1, 2 } } };
            byte[][][] b = { { { 3, 4 } } };
            byte[][][] result = Arrays.zip(a, b, (x, y) -> (byte) (x + y));

            assertEquals(1, result.length);
            assertArrayEquals(new byte[] { 4, 6 }, result[0][0]);
        }

        @Test
        public void testZip_Short2D_BinaryOp() {
            short[][] a = { { 1, 2 }, { 3, 4 } };
            short[][] b = { { 5, 6 }, { 7, 8 } };
            short[][] result = Arrays.zip(a, b, (x, y) -> (short) (x + y));

            assertEquals(2, result.length);
            assertArrayEquals(new short[] { 6, 8 }, result[0]);
            assertArrayEquals(new short[] { 10, 12 }, result[1]);
        }

        @Test
        public void testZip_Short2D_TernaryOp() {
            short[][] a = { { 1, 2 } };
            short[][] b = { { 3, 4 } };
            short[][] c = { { 5, 6 } };
            short[][] result = Arrays.zip(a, b, c, (x, y, z) -> (short) (x + y + z));

            assertEquals(1, result.length);
            assertArrayEquals(new short[] { 9, 12 }, result[0]);
        }

        @Test
        public void testZip_Short3D_BinaryOp() {
            short[][][] a = { { { 1, 2 } } };
            short[][][] b = { { { 3, 4 } } };
            short[][][] result = Arrays.zip(a, b, (x, y) -> (short) (x + y));

            assertEquals(1, result.length);
            assertArrayEquals(new short[] { 4, 6 }, result[0][0]);
        }

        @Test
        public void testZip_Float2D_BinaryOp() {
            float[][] a = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            float[][] b = { { 5.0f, 6.0f }, { 7.0f, 8.0f } };
            float[][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(2, result.length);
            assertArrayEquals(new float[] { 6.0f, 8.0f }, result[0], 0.0001f);
            assertArrayEquals(new float[] { 10.0f, 12.0f }, result[1], 0.0001f);
        }

        @Test
        public void testZip_Float2D_TernaryOp() {
            float[][] a = { { 1.0f, 2.0f } };
            float[][] b = { { 3.0f, 4.0f } };
            float[][] c = { { 5.0f, 6.0f } };
            float[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertEquals(1, result.length);
            assertArrayEquals(new float[] { 9.0f, 12.0f }, result[0], 0.0001f);
        }

        @Test
        public void testZip_Float3D_BinaryOp() {
            float[][][] a = { { { 1.0f, 2.0f } } };
            float[][][] b = { { { 3.0f, 4.0f } } };
            float[][][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(1, result.length);
            assertArrayEquals(new float[] { 4.0f, 6.0f }, result[0][0], 0.0001f);
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for the Arrays utility class (2510 test suite).
     * This test class provides extensive coverage of array manipulation methods including:
     * - Printing operations for various dimensions
     * - Mapping functions (mapToObj, mapToInt, mapToLong, mapToDouble, etc.)
     * - Array transformations (reshape, flatten, zip)
     * - In-place operations (updateAll, replaceIf)
     * - Type conversions (toBoolean, toByte, toChar, etc.)
     * - Statistical operations (elementCount, minSubArrayLength, maxSubArrayLength)
     */
    @Tag("2510")
    class Arrays2510Test extends TestBase {

        // ============================================
        // Tests for println methods - Object arrays
        // ============================================

        @Test
        public void testPrintln_1D_ObjectArray_normal() {
            Object[] arr = { "Hello", "World", 123, 45.6 };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
            assertTrue(result.contains("Hello"));
            assertTrue(result.contains("World"));
            assertTrue(result.contains("123"));
        }

        @Test
        public void testPrintln_1D_ObjectArray_null() {
            Object[] arr = null;
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
            assertTrue(result.contains("null"));
        }

        @Test
        public void testPrintln_1D_ObjectArray_empty() {
            Object[] arr = {};
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
            assertTrue(result.contains("[]"));
        }

        @Test
        public void testPrintln_2D_ObjectArray_normal() {
            Object[][] arr = { { "A", "B" }, { "C", "D" }, { "E", "F" } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_ObjectArray_null() {
            Object[][] arr = null;
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
            assertTrue(result.contains("null"));
        }

        @Test
        public void testPrintln_2D_ObjectArray_withNullSubArray() {
            Object[][] arr = { { "A", "B" }, null, { "C" } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_ObjectArray_normal() {
            Object[][][] arr = { { { "A", "B" }, { "C" } }, { { "D" } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_ObjectArray_empty() {
            Object[][][] arr = {};
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println methods - boolean arrays
        // ============================================

        @Test
        public void testPrintln_1D_BooleanArray_normal() {
            boolean[] arr = { true, false, true, false };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
            assertTrue(result.contains("true"));
            assertTrue(result.contains("false"));
        }

        @Test
        public void testPrintln_1D_BooleanArray_null() {
            boolean[] arr = null;
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_BooleanArray_normal() {
            boolean[][] arr = { { true, false }, { false, true } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_BooleanArray_normal() {
            boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println methods - char arrays
        // ============================================

        @Test
        public void testPrintln_1D_CharArray_normal() {
            char[] arr = { 'a', 'b', 'c', 'd' };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_CharArray_normal() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_CharArray_normal() {
            char[][][] arr = { { { 'a', 'b' } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println methods - byte arrays
        // ============================================

        @Test
        public void testPrintln_1D_ByteArray_normal() {
            byte[] arr = { 1, 2, 3, 4 };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_ByteArray_normal() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_ByteArray_normal() {
            byte[][][] arr = { { { 1, 2 } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println methods - short arrays
        // ============================================

        @Test
        public void testPrintln_1D_ShortArray_normal() {
            short[] arr = { 10, 20, 30 };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_ShortArray_normal() {
            short[][] arr = { { 10, 20 }, { 30, 40 } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_ShortArray_normal() {
            short[][][] arr = { { { 10, 20 } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println methods - int arrays
        // ============================================

        @Test
        public void testPrintln_1D_IntArray_normal() {
            int[] arr = { 1, 2, 3, 4, 5 };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_IntArray_normal() {
            int[][] arr = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_IntArray_normal() {
            int[][][] arr = { { { 1, 2 }, { 3 } }, { { 4 } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println methods - long arrays
        // ============================================

        @Test
        public void testPrintln_1D_LongArray_normal() {
            long[] arr = { 100L, 200L, 300L };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_LongArray_normal() {
            long[][] arr = { { 100L, 200L }, { 300L, 400L } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_LongArray_normal() {
            long[][][] arr = { { { 100L, 200L } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println methods - float arrays
        // ============================================

        @Test
        public void testPrintln_1D_FloatArray_normal() {
            float[] arr = { 1.1f, 2.2f, 3.3f };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_FloatArray_normal() {
            float[][] arr = { { 1.1f, 2.2f }, { 3.3f, 4.4f } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_FloatArray_normal() {
            float[][][] arr = { { { 1.1f, 2.2f } } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println methods - double arrays
        // ============================================

        @Test
        public void testPrintln_1D_DoubleArray_normal() {
            double[] arr = { 1.5, 2.5, 3.5 };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_2D_DoubleArray_normal() {
            double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void testPrintln_3D_DoubleArray_normal() {
            double[][][] arr = { { { 1.5, 2.5 } } };
            String result = Arrays.printAndReturn(arr);
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
            assertDoesNotThrow(() -> Arrays.updateAll(arr, b -> !b));
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
            assertDoesNotThrow(() -> Arrays.updateAll(arr, i -> i * 2));
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
            assertDoesNotThrow(() -> Arrays.updateAll(arr, d -> d * 2));
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
            assertDoesNotThrow(() -> Arrays.replaceIf(arr, b -> b, false));
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
            assertDoesNotThrow(() -> Arrays.replaceIf(arr, i -> i > 0, 0));
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
            assertDoesNotThrow(() -> Arrays.replaceIf(arr, d -> d > 0, 0.0));
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
            assertArrayEquals(CommonUtil.EMPTY_BOOLEAN_ARRAY, result);
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
            assertArrayEquals(CommonUtil.EMPTY_INT_ARRAY, result);
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
            assertArrayEquals(CommonUtil.EMPTY_DOUBLE_ARRAY, result, 0.001);
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
        // Tests for applyOnFlattened methods - boolean
        // ============================================

        @Test
        public void testFlatOp_2D_Boolean_normal() {
            boolean[][] arr = { { true, false }, { false, true } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(4, count[0]);
        }

        @Test
        public void testFlatOp_3D_Boolean_normal() {
            boolean[][][] arr = { { { true, false } }, { { true } } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        // ============================================
        // Tests for applyOnFlattened methods - char
        // ============================================

        @Test
        public void testFlatOp_2D_Char_normal() {
            char[][] arr = { { 'a', 'b' }, { 'c' } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        public void testFlatOp_3D_Char_normal() {
            char[][][] arr = { { { 'x', 'y' } }, { { 'z' } } };
            final int[] sum = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> sum[0] += subArr.length);
            assertEquals(3, sum[0]);
        }

        // ============================================
        // Tests for applyOnFlattened methods - byte
        // ============================================

        @Test
        public void testFlatOp_2D_Byte_normal() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(4, count[0]);
        }

        @Test
        public void testFlatOp_3D_Byte_normal() {
            byte[][][] arr = { { { 1, 2 } }, { { 3 } } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        // ============================================
        // Tests for applyOnFlattened methods - short
        // ============================================

        @Test
        public void testFlatOp_2D_Short_normal() {
            short[][] arr = { { 10, 20 }, { 30 } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        public void testFlatOp_3D_Short_normal() {
            short[][][] arr = { { { 100, 200 } }, { { 300 } } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        // ============================================
        // Tests for applyOnFlattened methods - int
        // ============================================

        @Test
        public void testFlatOp_2D_Int_normal() {
            int[][] arr = { { 1, 2, 3 }, { 4, 5 } };
            final int[] sum = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> {
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
            Arrays.applyOnFlattened(arr, subArr -> sum[0]++); // Should not throw exception
            assertEquals(0, sum[0]);
        }

        @Test
        public void testFlatOp_3D_Int_normal() {
            int[][][] arr = { { { 1, 2 }, { 3 } }, { { 4 } } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(4, count[0]);
        }

        // ============================================
        // Tests for applyOnFlattened methods - long
        // ============================================

        @Test
        public void testFlatOp_2D_Long_normal() {
            long[][] arr = { { 100L, 200L }, { 300L } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        public void testFlatOp_3D_Long_normal() {
            long[][][] arr = { { { 1000L, 2000L } }, { { 3000L } } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        // ============================================
        // Tests for applyOnFlattened methods - float
        // ============================================

        @Test
        public void testFlatOp_2D_Float_normal() {
            float[][] arr = { { 1.1f, 2.2f }, { 3.3f } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        @Test
        public void testFlatOp_3D_Float_normal() {
            float[][][] arr = { { { 1.0f, 2.0f } }, { { 3.0f } } };
            final int[] count = { 0 };
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
            assertEquals(3, count[0]);
        }

        // ============================================
        // Tests for applyOnFlattened methods - double
        // ============================================

        @Test
        public void testFlatOp_2D_Double_normal() {
            double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            final double[] sum = { 0.0 };
            Arrays.applyOnFlattened(arr, subArr -> {
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
            Arrays.applyOnFlattened(arr, subArr -> count[0] += subArr.length);
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
        // Tests for elementCount methods
        // ============================================

        @Test
        public void testTotalCountOfElements_2D_Boolean() {
            boolean[][] arr = { { true, false }, { true, false, true } };
            long count = Arrays.elementCount(arr);
            assertEquals(5L, count);
        }

        @Test
        public void testTotalCountOfElements_2D_Boolean_null() {
            boolean[][] arr = null;
            long count = Arrays.elementCount(arr);
            assertEquals(0L, count);
        }

        @Test
        public void testTotalCountOfElements_3D_Boolean() {
            boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
            long count = Arrays.elementCount(arr);
            assertEquals(4L, count);
        }

        @Test
        public void testTotalCountOfElements_2D_Int() {
            int[][] arr = { { 1, 2, 3 }, { 4, 5 } };
            long count = Arrays.elementCount(arr);
            assertEquals(5L, count);
        }

        @Test
        public void testTotalCountOfElements_3D_Double() {
            double[][][] arr = { { { 1.0, 2.0 }, { 3.0 } }, { { 4.0, 5.0 } } };
            long count = Arrays.elementCount(arr);
            assertEquals(5L, count);
        }

        // ============================================
        // Tests for minSubArrayLength methods
        // ============================================

        @Test
        public void testMinSubArrayLen_2D_Boolean() {
            boolean[][] arr = { { true, false, true }, { false }, { true, false } };
            int minLen = Arrays.minSubArrayLength(arr);
            assertEquals(1, minLen);
        }

        @Test
        public void testMinSubArrayLen_2D_Boolean_null() {
            boolean[][] arr = null;
            int minLen = Arrays.minSubArrayLength(arr);
            assertEquals(0, minLen);
        }

        @Test
        public void testMinSubArrayLen_2D_Int() {
            int[][] arr = { { 1, 2, 3 }, { 4, 5 }, { 6, 7, 8, 9 } };
            int minLen = Arrays.minSubArrayLength(arr);
            assertEquals(2, minLen);
        }

        @Test
        public void testMinSubArrayLen_2D_Double() {
            double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0, 5.0 }, { 6.0 } };
            int minLen = Arrays.minSubArrayLength(arr);
            assertEquals(1, minLen);
        }

        // ============================================
        // Tests for maxSubArrayLength methods
        // ============================================

        @Test
        public void testMaxSubArrayLen_2D_Boolean() {
            boolean[][] arr = { { true, false }, { true, false, true }, { false } };
            int maxLen = Arrays.maxSubArrayLength(arr);
            assertEquals(3, maxLen);
        }

        @Test
        public void testMaxSubArrayLen_2D_Boolean_null() {
            boolean[][] arr = null;
            int maxLen = Arrays.maxSubArrayLength(arr);
            assertEquals(0, maxLen);
        }

        @Test
        public void testMaxSubArrayLen_2D_Int() {
            int[][] arr = { { 1, 2 }, { 3, 4, 5, 6 }, { 7 } };
            int maxLen = Arrays.maxSubArrayLength(arr);
            assertEquals(4, maxLen);
        }

        @Test
        public void testMaxSubArrayLen_2D_Double() {
            double[][] arr = { { 1.0 }, { 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
            int maxLen = Arrays.maxSubArrayLength(arr);
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
        public void testNullHandling_typeConversion() {
            int[] arr = null;
            double[] result = Arrays.toDouble(arr);
            assertEquals(0, result.length);
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for Arrays class and its inner classes (f, ff, fff).
     * Tests cover printing methods, mapToObj methods, updateAll, replaceIf, reshape, flatten, and more.
     */
    @Tag("2511")
    class Arrays2511Test extends TestBase {

        // ============ Arrays.println Tests (1D, 2D, 3D) ============

        @Test
        public void testPrintln_1D_withNullArray() {
            String result = Arrays.printAndReturn((Object[]) null);
            assertEquals("null", result);
        }

        @Test
        public void testPrintln_1D_withEmptyArray() {
            String result = Arrays.printAndReturn(new Object[0]);
            assertEquals("[]", result);
        }

        @Test
        public void testPrintln_1D_withValidArray() {
            Object[] arr = { "Hello", 123, "World" };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
            assertTrue(result.contains("Hello"));
            assertTrue(result.contains("123"));
            assertTrue(result.contains("World"));
        }

        @Test
        public void testPrintln_2D_withNullArray() {
            String result = Arrays.printAndReturn((Object[][]) null);
            assertEquals("null", result);
        }

        @Test
        public void testPrintln_2D_withEmptyArray() {
            String result = Arrays.printAndReturn(new Object[0][]);
            assertEquals("[]", result);
        }

        @Test
        public void testPrintln_2D_withValidArray() {
            Object[][] arr = { { "A", "B" }, { "C", "D" } };
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
            assertTrue(result.contains("A"));
            assertTrue(result.contains("D"));
        }

        @Test
        public void testPrintln_3D_withNullArray() {
            String result = Arrays.printAndReturn((Object[][][]) null);
            assertEquals("null", result);
        }

        @Test
        public void testPrintln_3D_withEmptyArray() {
            String result = Arrays.printAndReturn(new Object[0][][]);
            assertEquals("[]", result);
        }

        @Test
        public void testPrintln_3D_withValidArray() {
            Object[][][] arr = { { { "A", "B" }, { "C" } }, { { "D" } } };
            String result = Arrays.printAndReturn(arr);
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
            Integer[][] result = Arrays.mapToObj(arr, b -> b * 2, Integer.class);
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
        public void testUpdateAll_boolean_3D_valid() {
            boolean[][][] arr = { { { true }, { false } } };
            Arrays.updateAll(arr, b -> !b);
            assertFalse(arr[0][0][0]);
            assertTrue(arr[0][1][0]);
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

        // ============ Arrays.applyOnFlattened Tests (boolean) ============

        @Test
        public void testFlatOp_boolean_2D_null() {
            boolean[][] arr = null;
            assertDoesNotThrow(() -> Arrays.applyOnFlattened(arr, flat -> {
                // Should not execute
                throw new RuntimeException("Should not be called");
            }));
        }

        @Test
        public void testFlatOp_boolean_2D_valid() {
            boolean[][] arr = { { true, false, true }, { false, true, false } };
            Arrays.applyOnFlattened(arr, flat -> {
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
            Arrays.applyOnFlattened(arr, flat -> {
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

        // ============ Arrays.elementCount Tests ============

        @Test
        public void testTotalCountOfElements_boolean_2D() {
            boolean[][] arr = { { true, false }, { true }, { false, true, false } };
            long count = Arrays.elementCount(arr);
            assertEquals(6L, count);
        }

        @Test
        public void testTotalCountOfElements_boolean_2D_withNulls() {
            boolean[][] arr = { { true }, null, { false, false } };
            long count = Arrays.elementCount(arr);
            assertEquals(3L, count);
        }

        @Test
        public void testMinSubArrayLen_boolean_2D_withNulls() {
            boolean[][] arr = { { true, false }, null, { true } };
            int minLen = Arrays.minSubArrayLength(arr);
            assertEquals(0, minLen);
        }

        // ============ Arrays.maxSubArrayLength Tests ============

        @Test
        public void testMaxSubArrayLen_boolean_2D() {
            boolean[][] arr = { { true, false, true }, { false }, { true, false } };
            int maxLen = Arrays.maxSubArrayLength(arr);
            assertEquals(3, maxLen);
        }

        @Test
        public void testMaxSubArrayLen_boolean_2D_withNulls() {
            boolean[][] arr = { { true }, null, { true, false, false } };
            int maxLen = Arrays.maxSubArrayLength(arr);
            assertEquals(3, maxLen);
        }

        // ============ Arrays.println Tests for primitives ============

        @Test
        public void testPrintln_boolean_1D_null() {
            String result = Arrays.printAndReturn((boolean[]) null);
            assertEquals("null", result);
        }

        @Test
        public void testPrintln_boolean_1D_empty() {
            String result = Arrays.printAndReturn(new boolean[0]);
            assertEquals("[]", result);
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
        public void testFF_applyOnFlattened_null() {
            String[][] arr = null;
            assertDoesNotThrow(() -> Arrays.ff.applyOnFlattened(arr, flat -> {
                throw new RuntimeException("Should not be called");
            }));
        }

        @Test
        public void testFF_applyOnFlattened_valid() {
            Integer[][] arr = { { 3, 1, 4 }, { 1, 5, 9 } };
            Arrays.ff.applyOnFlattened(arr, flat -> java.util.Arrays.sort(flat));
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
        public void testFF_elementCount() {
            Object[][] arr = { { "a", "b" }, { "c" }, { "d", "e", "f" } };
            long count = Arrays.ff.elementCount(arr);
            assertEquals(6L, count);
        }

        @Test
        public void testFF_minSubArrayLength() {
            Object[][] arr = { { "a", "b", "c" }, { "d" }, { "e", "f" } };
            int minLen = Arrays.ff.minSubArrayLength(arr);
            assertEquals(1, minLen);
        }

        @Test
        public void testFF_maxSubArrayLength() {
            Object[][] arr = { { "a", "b", "c" }, { "d" }, { "e", "f" } };
            int maxLen = Arrays.ff.maxSubArrayLength(arr);
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
        public void testFFF_applyOnFlattened_null() {
            String[][][] arr = null;
            assertDoesNotThrow(() -> Arrays.fff.applyOnFlattened(arr, flat -> {
                throw new RuntimeException("Should not be called");
            }));
        }

        @Test
        public void testFFF_applyOnFlattened_valid() {
            Integer[][][] arr = { { { 5, 2 } }, { { 9, 1 } }, { { 3, 7 } } };
            Arrays.fff.applyOnFlattened(arr, flat -> java.util.Arrays.sort(flat));
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
        public void testFFF_elementCount() {
            Object[][][] arr = { { { "a", "b" }, { "c" } }, { { "d", "e", "f" } } };
            long count = Arrays.fff.elementCount(arr);
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
            long[] result = Arrays.mapToLong(arr, d -> Math.round(d));
            assertArrayEquals(new long[] { 2L, 3L, 4L }, result);
        }

        @Test
        public void testMapToDouble_long_1D() {
            long[] arr = { 1L, 2L, 3L };
            double[] result = Arrays.mapToDouble(arr, l -> l * 1.5);
            assertArrayEquals(new double[] { 1.5, 3.0, 4.5 }, result, 0.001);
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for Arrays utility class.
     * This class provides extensive array manipulation methods for primitive and object arrays.
     * Tests cover println, mapToObj, mapToLong, mapToDouble, mapToInt, updateAll, replaceIf,
     * reshape, flatten, applyOnFlattened, zip, elementCount, minSubArrayLength, maxSubArrayLength methods.
     */
    @Tag("2512")
    class Arrays2512Test extends TestBase {

        // ============================================
        // Tests for println(Object[])
        // ============================================

        @Test
        public void test_println_objectArray() {
            Object[] arr = { "a", "b", "c" };
            String result = Arrays.printAndReturn(arr);

            assertNotNull(result);
            assertTrue(result.contains("a"));
            assertTrue(result.contains("b"));
            assertTrue(result.contains("c"));
        }

        @Test
        public void test_println_objectArrayNull() {
            String result = Arrays.printAndReturn((Object[]) null);
            assertNotNull(result);
        }

        @Test
        public void test_println_objectArrayEmpty() {
            Object[] arr = {};
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println(Object[][])
        // ============================================

        @Test
        public void test_println_objectArray2D() {
            Object[][] arr = { { "a", "b" }, { "c", "d" } };
            String result = Arrays.printAndReturn(arr);

            assertNotNull(result);
        }

        @Test
        public void test_println_objectArray2DNull() {
            String result = Arrays.printAndReturn((Object[][]) null);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println(Object[][][])
        // ============================================

        @Test
        public void test_println_objectArray3D() {
            Object[][][] arr = { { { "a" } } };
            String result = Arrays.printAndReturn(arr);

            assertNotNull(result);
        }

        @Test
        public void test_println_objectArray3DNull() {
            String result = Arrays.printAndReturn((Object[][][]) null);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println(boolean[])
        // ============================================

        @Test
        public void test_println_booleanArray() {
            boolean[] arr = { true, false, true };
            String result = Arrays.printAndReturn(arr);

            assertNotNull(result);
            assertTrue(result.contains("true"));
            assertTrue(result.contains("false"));
        }

        @Test
        public void test_println_booleanArrayNull() {
            String result = Arrays.printAndReturn((boolean[]) null);
            assertNotNull(result);
        }

        @Test
        public void test_println_booleanArrayEmpty() {
            boolean[] arr = {};
            String result = Arrays.printAndReturn(arr);
            assertNotNull(result);
        }

        @Test
        public void test_println_booleanArray2DNull() {
            String result = Arrays.printAndReturn((boolean[][]) null);
            assertNotNull(result);
        }

        // ============================================
        // Tests for println(boolean[][][])
        // ============================================

        @Test
        public void test_println_booleanArray3D() {
            boolean[][][] arr = { { { true } } };
            String result = Arrays.printAndReturn(arr);

            assertNotNull(result);
        }

        @Test
        public void test_println_booleanArray3DNull() {
            String result = Arrays.printAndReturn((boolean[][][]) null);
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
            assertDoesNotThrow(() -> Arrays.updateAll((boolean[]) null, b -> b));
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
            assertDoesNotThrow(() -> Arrays.updateAll((boolean[][]) null, b -> b));
        }

        // ============================================
        // Tests for replaceIf(boolean[])
        // ============================================

        @Test
        public void test_replaceIf_booleanArrayNull() {
            assertDoesNotThrow(() -> Arrays.replaceIf((boolean[]) null, b -> true, false));
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
            assertEquals(2, result.length); // 2 rows with columnCount=2 each (last row has 1 element)
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

            // With 6 elements and rows=2, columnCount=3 (2*3=6 per block), we get 1 block
            assertEquals(1, result.length);
            assertEquals(2, result[0].length); // 2 rows in the block
            assertEquals(3, result[0][0].length); // 3 columns per row
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
        // Tests for applyOnFlattened(boolean[][])
        // ============================================

        @Test
        public void test_applyOnFlattened_booleanArray2D() {
            boolean[][] arr = { { true, false }, { true, false } };
            int[] count = { 0 };

            Arrays.applyOnFlattened(arr, subArray -> count[0] += subArray.length);

            assertEquals(4, count[0]);
        }

        @Test
        public void test_applyOnFlattened_booleanArray2DNull() {
            boolean[] invoked = { false };
            assertDoesNotThrow(() -> Arrays.applyOnFlattened((boolean[][]) null, subArray -> invoked[0] = true));
            assertFalse(invoked[0], "The callback should not run for a null 2D array");
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
        // Tests for elementCount(boolean[][])
        // ============================================

        @Test
        public void test_elementCount_booleanArray2D() {
            boolean[][] arr = { { true, false }, { true, false, true } };
            long count = Arrays.elementCount(arr);

            assertEquals(5, count);
        }

        @Test
        public void test_elementCount_booleanArray2DNull() {
            long count = Arrays.elementCount((boolean[][]) null);
            assertEquals(0, count);
        }

        @Test
        public void test_elementCount_booleanArray2DEmpty() {
            boolean[][] arr = {};
            long count = Arrays.elementCount(arr);
            assertEquals(0, count);
        }

        // ============================================
        // Tests for elementCount(boolean[][][])
        // ============================================

        @Test
        public void test_elementCount_booleanArray3D() {
            boolean[][][] arr = { { { true, false }, { true } } };
            long count = Arrays.elementCount(arr);

            assertEquals(3, count);
        }

        @Test
        public void test_elementCount_booleanArray3DNull() {
            long count = Arrays.elementCount((boolean[][][]) null);
            assertEquals(0, count);
        }

        // ============================================
        // Tests for minSubArrayLength(boolean[][])
        // ============================================

        @Test
        public void test_minSubArrayLength_booleanArray2D() {
            boolean[][] arr = { { true, false, true }, { true } };
            int min = Arrays.minSubArrayLength(arr);

            assertEquals(1, min);
        }

        @Test
        public void test_minSubArrayLength_booleanArray2DNull() {
            int min = Arrays.minSubArrayLength((boolean[][]) null);
            assertEquals(0, min);
        }

        @Test
        public void test_minSubArrayLength_booleanArray2DEmpty() {
            boolean[][] arr = {};
            int min = Arrays.minSubArrayLength(arr);
            assertEquals(0, min);
        }

        // ============================================
        // Tests for maxSubArrayLength(boolean[][])
        // ============================================

        @Test
        public void test_maxSubArrayLength_booleanArray2D() {
            boolean[][] arr = { { true, false, true }, { true } };
            int max = Arrays.maxSubArrayLength(arr);

            assertEquals(3, max);
        }

        @Test
        public void test_maxSubArrayLength_booleanArray2DNull() {
            int max = Arrays.maxSubArrayLength((boolean[][]) null);
            assertEquals(0, max);
        }

        @Test
        public void test_maxSubArrayLength_booleanArray2DEmpty() {
            boolean[][] arr = {};
            int max = Arrays.maxSubArrayLength(arr);
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
        public void test_elementCount_charArray2D() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd', 'e' } };
            long count = Arrays.elementCount(arr);

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

    @Nested
    /**
     * Tests that verify the code examples in Arrays.java Javadoc comments.
     */
    class JavadocExampleArraysTest {

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
        public void testClassJavadoc_elementCount3D() {
            // Line 152-153: 2x3x4 cube => 24 total elements
            int[][][] cube = new int[2][3][4];
            long totalElements = Arrays.elementCount(cube);
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
        // boolean elementCount / minSubArrayLength / maxSubArrayLength
        // ========================

        @Test
        public void testBooleanTotalCountOfElements2D() {
            // Line 2833-2835
            boolean[][] array = { { true, false, true }, null, { false, true, false } };
            assertEquals(6, Arrays.elementCount(array));
        }

        @Test
        public void testBooleanTotalCountOfElements3D() {
            // Line 2861-2863
            boolean[][][] array = { { { true, false }, { true } }, { { false, true, false } } };
            assertEquals(6, Arrays.elementCount(array));
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
        // byte elementCount / minSubArrayLength / maxSubArrayLength
        // ========================

        @Test
        public void testByteTotalCountOfElements2D() {
            // Line 5451-5452: {{1,2,3}, null, {4,5}} => 5
            byte[][] array = { { 1, 2, 3 }, null, { 4, 5 } };
            assertEquals(5, Arrays.elementCount(array));
        }

        @Test
        public void testByteTotalCountOfElements3D() {
            // Line 5478-5479: {{{1,2},{3}},{{4,5,6}}} => 6
            byte[][][] array = { { { 1, 2 }, { 3 } }, { { 4, 5, 6 } } };
            assertEquals(6, Arrays.elementCount(array));
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
        // int elementCount / minSubArrayLength / maxSubArrayLength
        // ========================

        @Test
        public void testIntTotalCountOfElements2D() {
            // Line 8020-8022: {{1,2},{3,4,5},null,{}} => 5
            int[][] a = { { 1, 2 }, { 3, 4, 5 }, null, {} };
            assertEquals(5, Arrays.elementCount(a));
        }

        @Test
        public void testIntTotalCountOfElements3D() {
            // Line 8048-8050: {{{1},{2,3}},null,{{4,5,6}}} => 6
            int[][][] a = { { { 1 }, { 2, 3 } }, null, { { 4, 5, 6 } } };
            assertEquals(6, Arrays.elementCount(a));
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
            String result = Arrays.printAndReturn((Object[]) null);
            assertTrue(result.contains("null"));
        }

        @Test
        public void testPrintlnObjectArrayEmpty() {
            // Line 284-285: println(empty Object[]) => "[]"
            Object[] empty = new Object[0];
            String result = Arrays.printAndReturn(empty);
            assertTrue(result.contains("[]"));
        }

        @Test
        public void testPrintlnBooleanArray() {
            // Line 2965-2967: println({true,false,true}) => "[true, false, true]"
            boolean[] arr = { true, false, true };
            String result = Arrays.printAndReturn(arr);
            assertTrue(result.contains("[true, false, true]"));
        }

        @Test
        public void testPrintlnByteArray() {
            // Line 5581-5583: println({1,2,3,4,5}) => "[1, 2, 3, 4, 5]"
            byte[] array = { 1, 2, 3, 4, 5 };
            String result = Arrays.printAndReturn(array);
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

}
