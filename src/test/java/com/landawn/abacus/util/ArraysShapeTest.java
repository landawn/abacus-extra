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

import static com.landawn.abacus.util.ArrayTestSupport.ITERATIONS;
import static com.landawn.abacus.util.ArrayTestSupport.arrayMethod;
import static com.landawn.abacus.util.ArrayTestSupport.arrayType;
import static com.landawn.abacus.util.ArrayTestSupport.invokeArrayMethod;
import static com.landawn.abacus.util.ArrayTestSupport.nestedArray;
import static com.landawn.abacus.util.ArrayTestSupport.newArray;
import static com.landawn.abacus.util.ArrayTestSupport.primitiveArray;
import static com.landawn.abacus.util.ArrayTestSupport.randomIntArray;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.landawn.abacus.util.ArrayTestSupport.Value;
import com.landawn.abacus.util.Arrays.f;
import com.landawn.abacus.util.Arrays.ff;
import com.landawn.abacus.util.Arrays.fff;

class ArraysShapeTest {

    @Nested
    class ReshapeTests {

        @ParameterizedTest(name = "{0}: empty reshape")
        @MethodSource("com.landawn.abacus.util.ArrayTestSupport#primitiveTypes")
        void reshape_nullOrEmptyVector_returnsEmptyArrayOfRequestedDimension(Class<?> elementType) throws Throwable {
            for (Object input : new Object[] { null, primitiveArray(elementType) }) {
                Object grid = invokeArrayMethod(arrayMethod("reshape", elementType, 1, 2), input, 2);
                Object cube = invokeArrayMethod(arrayMethod("reshape", elementType, 1, 3), input, 2, 3);
                assertEquals(arrayType(elementType, 2), grid.getClass());
                assertEquals(arrayType(elementType, 3), cube.getClass());
                assertEquals(0, java.lang.reflect.Array.getLength(grid));
                assertEquals(0, java.lang.reflect.Array.getLength(cube));
            }
        }

        @Test
        void booleanReshape2D() {
            // Line 2057-2060: reshape({true,false,true,false,true}, 2)
            boolean[] arr = { true, false, true, false, true };
            boolean[][] reshaped = Arrays.reshape(arr, 2);
            assertEquals(3, reshaped.length);
            assertArrayEquals(new boolean[] { true, false }, reshaped[0]);
            assertArrayEquals(new boolean[] { true, false }, reshaped[1]);
            assertArrayEquals(new boolean[] { true }, reshaped[2]);
        }

        @Test
        void booleanReshape3D() {
            // Line 2091-2094: reshape({true,false,true,false,true,false}, 2, 2)
            boolean[] arr = { true, false, true, false, true, false };
            boolean[][][] reshaped = Arrays.reshape(arr, 2, 2);
            assertEquals(2, reshaped.length);
            assertArrayEquals(new boolean[] { true, false }, reshaped[0][0]);
            assertArrayEquals(new boolean[] { true, false }, reshaped[0][1]);
            assertArrayEquals(new boolean[] { true, false }, reshaped[1][0]);
        }

        @Test
        void byteReshape2D() {
            // Line 4885-4888
            byte[] arr = { 1, 2, 3, 4, 5 };
            byte[][] reshaped = Arrays.reshape(arr, 2);
            assertArrayEquals(new byte[] { 1, 2 }, reshaped[0]);
            assertArrayEquals(new byte[] { 3, 4 }, reshaped[1]);
            assertArrayEquals(new byte[] { 5 }, reshaped[2]);
        }

        @Test
        void byteReshape3D() {
            // Line 4920-4922: reshape({1,2,3,4,5,6}, 2, 2) => {{{1,2},{3,4}},{{5,6}}}
            byte[] arr = { 1, 2, 3, 4, 5, 6 };
            byte[][][] reshaped = Arrays.reshape(arr, 2, 2);
            assertEquals(2, reshaped.length);
            assertArrayEquals(new byte[] { 1, 2 }, reshaped[0][0]);
            assertArrayEquals(new byte[] { 3, 4 }, reshaped[0][1]);
            assertArrayEquals(new byte[] { 5, 6 }, reshaped[1][0]);
        }

        @Test
        void classJavadoc_reshapeInt2D() {
            // Line 114-115: reshape({1,2,3,4,5,6}, 2) => {{1,2},{3,4},{5,6}}
            int[] linear = { 1, 2, 3, 4, 5, 6 };
            int[][] reshaped2D = Arrays.reshape(linear, 2);
            assertArrayEquals(new int[] { 1, 2 }, reshaped2D[0]);
            assertArrayEquals(new int[] { 3, 4 }, reshaped2D[1]);
            assertArrayEquals(new int[] { 5, 6 }, reshaped2D[2]);
        }

        @Test
        void classJavadoc_reshapeInt3D() {
            // Line 118-119: reshape({1,2,3,4,5,6,7,8}, 2, 2) => {{{1,2},{3,4}},{{5,6},{7,8}}}
            int[] arr = { 1, 2, 3, 4, 5, 6, 7, 8 };
            int[][][] reshaped3D = Arrays.reshape(arr, 2, 2);
            assertArrayEquals(new int[] { 1, 2 }, reshaped3D[0][0]);
            assertArrayEquals(new int[] { 3, 4 }, reshaped3D[0][1]);
            assertArrayEquals(new int[] { 5, 6 }, reshaped3D[1][0]);
            assertArrayEquals(new int[] { 7, 8 }, reshaped3D[1][1]);
        }

        @Test
        void doubleReshape3D() {
            double[] arr = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
            double[][][] cube = Arrays.reshape(arr, 2, 3);
            assertEquals(2, cube.length);
            assertArrayEquals(new double[] { 1, 2, 3 }, cube[0][0], 0.0);
            assertArrayEquals(new double[] { 4, 5, 6 }, cube[0][1], 0.0);
            assertArrayEquals(new double[] { 7, 8, 9 }, cube[1][0], 0.0);
            assertArrayEquals(new double[] { 10 }, cube[1][1], 0.0);
        }

        @Test
        void fF_reshape_emptyArray() {
            Integer[] empty = new Integer[0];
            Integer[][] result = Arrays.ff.reshape(empty, 1);
            assertEquals(0, result.length);
        }

        @Test
        void fF_reshape_invalidCols() {
            Integer[] arr = { 1, 2, 3 };
            assertThrows(IllegalArgumentException.class, () -> Arrays.ff.reshape(arr, 0));
            assertThrows(IllegalArgumentException.class, () -> Arrays.ff.reshape(arr, -1));
        }

        @Test
        void fF_reshape_jaggedLastRow() {
            Integer[] arr = { 1, 2, 3, 4, 5, 6, 7 };
            Integer[][] reshaped = ff.reshape(arr, 3);
            assertEquals(3, reshaped.length);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, reshaped[0]);
            assertArrayEquals(new Integer[] { 4, 5, 6 }, reshaped[1]);
            assertArrayEquals(new Integer[] { 7 }, reshaped[2]);
        }

        @Test
        void ff_reshape_keepsTheInputRuntimeComponentType() {
            final Number[] src = new Integer[] { 1, 2, 3 };
            final Number[][] resh = Arrays.ff.reshape(src, 2);

            assertEquals(Integer.class, resh.getClass().getComponentType().getComponentType());
            assertThrows(ArrayStoreException.class, () -> resh[0][0] = Double.valueOf(1.5));
        }

        @Test
        void fF_reshape_unevenDivision() {
            Integer[] arr = { 1, 2, 3, 4, 5, 6, 7 };
            Integer[][] result = Arrays.ff.reshape(arr, 3);
            assertEquals(3, result.length);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, result[0]);
            assertArrayEquals(new Integer[] { 4, 5, 6 }, result[1]);
            assertArrayEquals(new Integer[] { 7 }, result[2]);
        }

        @Test
        void fF_reshape_validEvenDivision() {
            Integer[] arr = { 1, 2, 3, 4, 5, 6 };
            Integer[][] result = Arrays.ff.reshape(arr, 3);
            assertEquals(2, result.length);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, result[0]);
            assertArrayEquals(new Integer[] { 4, 5, 6 }, result[1]);
        }

        @Test
        void fFF_reshape_3x2() {
            Integer[] flat = { 1, 2, 3, 4, 5, 6, 7, 8 };
            Integer[][][] reshaped = fff.reshape(flat, 2, 2);
            assertEquals(2, reshaped.length);
            assertArrayEquals(new Integer[] { 1, 2 }, reshaped[0][0]);
            assertArrayEquals(new Integer[] { 3, 4 }, reshaped[0][1]);
            assertArrayEquals(new Integer[] { 5, 6 }, reshaped[1][0]);
            assertArrayEquals(new Integer[] { 7, 8 }, reshaped[1][1]);
        }

        @Test
        void fFF_reshape_emptyArray() {
            Integer[] empty = new Integer[0];
            Integer[][][] result = Arrays.fff.reshape(empty, 1, 1);
            assertEquals(0, result.length);
        }

        @Test
        void fFF_reshape_invalidDimensions() {
            Integer[] arr = { 1, 2, 3 };
            assertThrows(IllegalArgumentException.class, () -> Arrays.fff.reshape(arr, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> Arrays.fff.reshape(arr, 1, 0));
            assertThrows(IllegalArgumentException.class, () -> Arrays.fff.reshape(arr, -1, 1));
            assertThrows(IllegalArgumentException.class, () -> Arrays.fff.reshape(arr, 1, -1));
        }

        @Test
        void fFF_reshape_unevenDivision() {
            Integer[] arr = { 1, 2, 3, 4, 5 };
            Integer[][][] result = Arrays.fff.reshape(arr, 2, 2);
            assertEquals(2, result.length);
            assertArrayEquals(new Integer[] { 1, 2 }, result[0][0]);
            assertArrayEquals(new Integer[] { 3, 4 }, result[0][1]);
            assertArrayEquals(new Integer[] { 5 }, result[1][0]);
        }

        @Test
        void fFF_reshape_valid() {
            Integer[] arr = { 1, 2, 3, 4, 5, 6, 7, 8 };
            Integer[][][] result = Arrays.fff.reshape(arr, 2, 2);
            assertEquals(2, result.length);
            assertArrayEquals(new Integer[] { 1, 2 }, result[0][0]);
            assertArrayEquals(new Integer[] { 3, 4 }, result[0][1]);
            assertArrayEquals(new Integer[] { 5, 6 }, result[1][0]);
            assertArrayEquals(new Integer[] { 7, 8 }, result[1][1]);
        }

        @Test
        void fffReshapeRejectsNullInput() {
            assertThrows(IllegalArgumentException.class, () -> fff.reshape((String[]) null, 2, 2));
        }

        @Test
        void ffReshapeRejectsNullInput() {
            assertThrows(IllegalArgumentException.class, () -> ff.reshape((String[]) null, 2));
        }

        @Test
        void floatReshape2D_and_3D() {
            float[] arr = { 1, 2, 3, 4, 5, 6, 7 };
            float[][] grid = Arrays.reshape(arr, 3);
            assertArrayEquals(new float[] { 1, 2, 3 }, grid[0], 0.0f);
            assertArrayEquals(new float[] { 4, 5, 6 }, grid[1], 0.0f);
            assertArrayEquals(new float[] { 7 }, grid[2], 0.0f);

            float[][][] cube = Arrays.reshape(arr, 2, 2);
            assertEquals(2, cube.length);
            assertArrayEquals(new float[] { 1, 2 }, cube[0][0], 0.0f);
            assertArrayEquals(new float[] { 3, 4 }, cube[0][1], 0.0f);
            assertArrayEquals(new float[] { 5, 6 }, cube[1][0], 0.0f);
            assertArrayEquals(new float[] { 7 }, cube[1][1], 0.0f);
        }

        @Test
        void integration_reshapeAndFlatten() {
            boolean[] original = { true, false, true, false, true, false };
            boolean[][] reshaped = Arrays.reshape(original, 3);
            boolean[] flattened = Arrays.flatten(reshaped);

            assertArrayEquals(original, flattened);
        }

        @Test
        void intReshape2D() {
            // Line 7278-7281: reshape({1,2,3,4,5,6,7}, 3) => {{1,2,3},{4,5,6},{7}}
            int[] arr = { 1, 2, 3, 4, 5, 6, 7 };
            int[][] result = Arrays.reshape(arr, 3);
            assertArrayEquals(new int[] { 1, 2, 3 }, result[0]);
            assertArrayEquals(new int[] { 4, 5, 6 }, result[1]);
            assertArrayEquals(new int[] { 7 }, result[2]);
        }

        @Test
        void intReshape3D() {
            // Line 7316-7318: reshape({1,2,3,4,5,6,7,8}, 2, 2) => {{{1,2},{3,4}},{{5,6},{7,8}}}
            int[] arr = { 1, 2, 3, 4, 5, 6, 7, 8 };
            int[][][] result = Arrays.reshape(arr, 2, 2);
            assertArrayEquals(new int[] { 1, 2 }, result[0][0]);
            assertArrayEquals(new int[] { 3, 4 }, result[0][1]);
            assertArrayEquals(new int[] { 5, 6 }, result[1][0]);
            assertArrayEquals(new int[] { 7, 8 }, result[1][1]);
        }

        @Test
        void nullHandling_reshape() {
            int[] arr = null;
            int[][] result = Arrays.reshape(arr, 2);
            assertEquals(0, result.length);
        }

        @Test
        void reshape() {
            {
                final int[] a = Array.rangeClosed(1, 9);
                final int[][] b = Arrays.reshape(a, 2);
                final int[][][] c = Arrays.reshape(a, 2, 2);
                final int[][][] d = Arrays.reshape(a, 3, 2);
                final int[][][] e = Arrays.reshape(a, 2, 3);
                final int[][][] f = Arrays.reshape(a, 3, 3);
                Arrays.println(a);
                N.println(Strings.repeat('-', 80));
                Arrays.println(b);
                N.println(Strings.repeat('-', 80));
                Arrays.println(c);
                N.println(Strings.repeat('-', 80));
                Arrays.println(d);
                N.println(Strings.repeat('-', 80));
                Arrays.println(e);
                N.println(Strings.repeat('-', 80));
                Arrays.println(f);

                assertArrayEquals(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 }, { 7, 8 }, { 9 } }, b);
                assertArrayEquals(new int[][][] { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } }, { { 9 } } }, c);
                assertArrayEquals(new int[][][] { { { 1, 2 }, { 3, 4 }, { 5, 6 } }, { { 7, 8 }, { 9 } } }, d);
                assertArrayEquals(new int[][][] { { { 1, 2, 3 }, { 4, 5, 6 } }, { { 7, 8, 9 } } }, e);
                assertArrayEquals(new int[][][] { { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } } }, f);
            }
        }

        @Test
        void reshape_1Dto2D_boolean_invalidSize() {
            boolean[] arr = { true, false, true, false, true };
            boolean[][] result = Arrays.reshape(arr, 3);
            assertEquals(2, result.length);
            assertEquals(3, result[0].length);
            assertEquals(2, result[1].length);
        }

        @Test
        void reshape_1Dto2D_boolean_normal() {
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
        void reshape_1Dto2D_byte_normal() {
            byte[] arr = { 1, 2, 3, 4, 5, 6 };
            byte[][] result = Arrays.reshape(arr, 2);
            assertEquals(3, result.length);
            assertEquals(2, result[0].length);
            assertEquals(1, result[0][0]);
            assertEquals(5, result[2][0]);
        }

        @Test
        void reshape_1Dto2D_char_normal() {
            char[] arr = { 'a', 'b', 'c', 'd', 'e', 'f' };
            char[][] result = Arrays.reshape(arr, 3);
            assertEquals(2, result.length);
            assertEquals(3, result[0].length);
            assertEquals('a', result[0][0]);
            assertEquals('d', result[1][0]);
        }

        @Test
        void reshape_1Dto2D_double_normal() {
            double[] arr = { 1.5, 2.5, 3.5, 4.5 };
            double[][] result = Arrays.reshape(arr, 2);
            assertEquals(2, result.length);
            assertEquals(1.5, result[0][0], 0.001);
            assertEquals(3.5, result[1][0], 0.001);
        }

        @Test
        void reshape_1Dto2D_float_normal() {
            float[] arr = { 1.1f, 2.2f, 3.3f, 4.4f };
            float[][] result = Arrays.reshape(arr, 2);
            assertEquals(2, result.length);
            assertEquals(1.1f, result[0][0], 0.001f);
            assertEquals(3.3f, result[1][0], 0.001f);
        }

        @Test
        void reshape_1Dto2D_int_normal() {
            int[] arr = { 1, 2, 3, 4, 5, 6 };
            int[][] result = Arrays.reshape(arr, 3);
            assertEquals(2, result.length);
            assertEquals(3, result[0].length);
            assertEquals(1, result[0][0]);
            assertEquals(4, result[1][0]);
            assertEquals(6, result[1][2]);
        }

        @Test
        void reshape_1Dto2D_int_singleRow() {
            int[] arr = { 1, 2, 3 };
            int[][] result = Arrays.reshape(arr, 3);
            assertEquals(1, result.length);
            assertEquals(3, result[0].length);
        }

        @Test
        void reshape_1Dto2D_long_normal() {
            long[] arr = { 100L, 200L, 300L, 400L };
            long[][] result = Arrays.reshape(arr, 2);
            assertEquals(2, result.length);
            assertEquals(100L, result[0][0]);
            assertEquals(300L, result[1][0]);
        }

        @Test
        void reshape_1Dto2D_short_normal() {
            short[] arr = { 10, 20, 30, 40, 50, 60 };
            short[][] result = Arrays.reshape(arr, 3);
            assertEquals(2, result.length);
            assertEquals(10, result[0][0]);
            assertEquals(40, result[1][0]);
        }

        @Test
        void reshape_1Dto3D_boolean_normal() {
            boolean[] arr = { true, false, true, false, true, false };
            boolean[][][] result = Arrays.reshape(arr, 2, 3);
            assertEquals(1, result.length);
            assertEquals(2, result[0].length);
            assertEquals(3, result[0][0].length);
            assertTrue(result[0][0][0]);
            assertFalse(result[0][0][1]);
        }

        @Test
        void reshape_1Dto3D_byte_normal() {
            byte[] arr = { 1, 2, 3, 4, 5, 6, 7, 8 };
            byte[][][] result = Arrays.reshape(arr, 2, 2);
            assertEquals(2, result.length);
            assertEquals(1, result[0][0][0]);
            assertEquals(5, result[1][0][0]);
        }

        @Test
        void reshape_1Dto3D_char_normal() {
            char[] arr = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
            char[][][] result = Arrays.reshape(arr, 2, 2);
            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals('a', result[0][0][0]);
            assertEquals('e', result[1][0][0]);
        }

        @Test
        void reshape_1Dto3D_double_normal() {
            double[] arr = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
            double[][][] result = Arrays.reshape(arr, 2, 3);
            assertEquals(1, result.length);
            assertEquals(1.0, result[0][0][0], 0.001);
        }

        @Test
        void reshape_1Dto3D_float_normal() {
            float[] arr = { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f };
            float[][][] result = Arrays.reshape(arr, 2, 3);
            assertEquals(1, result.length);
            assertEquals(1.0f, result[0][0][0], 0.001f);
        }

        @Test
        void reshape_1Dto3D_int_normal() {
            int[] arr = { 1, 2, 3, 4, 5, 6, 7, 8 };
            int[][][] result = Arrays.reshape(arr, 2, 2);
            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals(1, result[0][0][0]);
            assertEquals(5, result[1][0][0]);
        }

        @Test
        void reshape_1Dto3D_long_normal() {
            long[] arr = { 1L, 2L, 3L, 4L, 5L, 6L };
            long[][][] result = Arrays.reshape(arr, 2, 3);
            assertEquals(1, result.length);
            assertEquals(1L, result[0][0][0]);
        }

        @Test
        void reshape_1Dto3D_short_normal() {
            short[] arr = { 10, 20, 30, 40, 50, 60 };
            short[][][] result = Arrays.reshape(arr, 2, 3);
            assertEquals(1, result.length);
            assertEquals(10, result[0][0][0]);
        }

        @Test
        void reshape_2D_empty_returnsEmpty2D() {
            // int
            final int[][] ri = Arrays.reshape(new int[0], 5);
            assertEquals(0, ri.length);
            // boolean
            final boolean[][] rb = Arrays.reshape(new boolean[0], 3);
            assertEquals(0, rb.length);
            // double
            final double[][] rd = Arrays.reshape(new double[0], 7);
            assertEquals(0, rd.length);
        }

        @Test
        void reshape_2D_negativeColumns_throws() {
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(new int[] { 1, 2 }, -1));
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(new double[] { 1.0 }, -5));
        }

        @Test
        void reshape_2D_nullSource_returnsEmpty2D() {
            // primitives accept null per javadoc
            assertEquals(0, Arrays.reshape((int[]) null, 3).length);
            assertEquals(0, Arrays.reshape((boolean[]) null, 3).length);
            assertEquals(0, Arrays.reshape((long[]) null, 3).length);
            assertEquals(0, Arrays.reshape((char[]) null, 3).length);
        }

        @Test
        void reshape_2D_partialLastRow_byte() {
            final byte[] arr = { 1, 2, 3, 4, 5 };
            final byte[][] r = Arrays.reshape(arr, 2);
            assertEquals(3, r.length);
            assertArrayEquals(new byte[] { 1, 2 }, r[0]);
            assertArrayEquals(new byte[] { 3, 4 }, r[1]);
            assertArrayEquals(new byte[] { 5 }, r[2]);
        }

        @Test
        void reshape_2D_partialLastRow_int() {
            final int[] arr = { 1, 2, 3, 4, 5 };
            final int[][] r = Arrays.reshape(arr, 2);
            assertEquals(3, r.length);
            assertArrayEquals(new int[] { 1, 2 }, r[0]);
            assertArrayEquals(new int[] { 3, 4 }, r[1]);
            assertArrayEquals(new int[] { 5 }, r[2]);
        }

        @Test
        void reshape_2D_singleElementWithBiggerCols() {
            final int[][] r = Arrays.reshape(new int[] { 42 }, 5);
            assertEquals(1, r.length);
            assertArrayEquals(new int[] { 42 }, r[0]);
        }

        @Test
        void reshape_2D_zeroColumns_throws() {
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(new int[] { 1, 2 }, 0));
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(new boolean[] { true }, 0));
        }

        @Test
        void reshape_3D_dimensionProductMayExceedIntegerMaxValue() {
            final int rows = 50_000;
            final int columns = 50_000;

            final boolean[][][] booleans = Arrays.reshape(new boolean[] { true }, rows, columns);
            assertEquals(1, booleans.length);
            assertEquals(1, booleans[0].length);
            assertArrayEquals(new boolean[] { true }, booleans[0][0]);

            assertArrayEquals(new char[] { 'a' }, Arrays.reshape(new char[] { 'a' }, rows, columns)[0][0]);
            assertArrayEquals(new byte[] { 1 }, Arrays.reshape(new byte[] { 1 }, rows, columns)[0][0]);
            assertArrayEquals(new short[] { 2 }, Arrays.reshape(new short[] { 2 }, rows, columns)[0][0]);
            assertArrayEquals(new int[] { 3 }, Arrays.reshape(new int[] { 3 }, rows, columns)[0][0]);
            assertArrayEquals(new long[] { 4L }, Arrays.reshape(new long[] { 4L }, rows, columns)[0][0]);
            assertArrayEquals(new float[] { 5F }, Arrays.reshape(new float[] { 5F }, rows, columns)[0][0]);
            assertArrayEquals(new double[] { 6D }, Arrays.reshape(new double[] { 6D }, rows, columns)[0][0]);

            final String[][][] objects = fff.reshape(new String[] { "value" }, rows, columns);
            assertEquals(String[][][].class, objects.getClass());
            assertArrayEquals(new String[] { "value" }, objects[0][0]);
        }

        @Test
        void reshape_3D_empty_returnsEmpty3D() {
            assertEquals(0, Arrays.reshape(new int[0], 2, 2).length);
            assertEquals(0, Arrays.reshape(new short[0], 3, 4).length);
            assertEquals(0, Arrays.reshape((float[]) null, 2, 2).length);
        }

        @Test
        void reshape_3D_evenBlocks_int() {
            final int[] arr = { 1, 2, 3, 4, 5, 6 };
            final int[][][] r = Arrays.reshape(arr, 2, 2);
            // block-size = 4; len=6 -> 2 blocks. Block 0 has 2 rows, block 1 has 1 row of 2.
            assertEquals(2, r.length);
            assertEquals(2, r[0].length);
            assertArrayEquals(new int[] { 1, 2 }, r[0][0]);
            assertArrayEquals(new int[] { 3, 4 }, r[0][1]);
            assertEquals(1, r[1].length);
            assertArrayEquals(new int[] { 5, 6 }, r[1][0]);
        }

        @Test
        void reshape_3D_partialLastBlockPartialLastRow_int() {
            final int[] arr = { 1, 2, 3, 4, 5, 6, 7 };
            final int[][][] r = Arrays.reshape(arr, 2, 2);
            // block 0: {{1,2},{3,4}}; block 1 has 2 rows: {{5,6},{7}}
            assertEquals(2, r.length);
            assertEquals(2, r[0].length);
            assertArrayEquals(new int[] { 1, 2 }, r[0][0]);
            assertArrayEquals(new int[] { 3, 4 }, r[0][1]);
            assertEquals(2, r[1].length);
            assertArrayEquals(new int[] { 5, 6 }, r[1][0]);
            assertArrayEquals(new int[] { 7 }, r[1][1]);
        }

        @Test
        void reshape_3D_partialLastBlockPartialLastRow_long() {
            final long[] arr = { 1, 2, 3, 4, 5, 6, 7 };
            final long[][][] r = Arrays.reshape(arr, 2, 2);
            assertEquals(2, r.length);
            assertEquals(2, r[0].length);
            assertArrayEquals(new long[] { 1, 2 }, r[0][0]);
            assertArrayEquals(new long[] { 3, 4 }, r[0][1]);
            assertEquals(2, r[1].length);
            assertArrayEquals(new long[] { 5, 6 }, r[1][0]);
            assertArrayEquals(new long[] { 7 }, r[1][1]);
        }

        @Test
        void reshape_3D_zeroOrNegative_throws() {
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(new int[] { 1 }, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(new int[] { 1 }, 1, 0));
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(new int[] { 1 }, -1, 1));
        }

        @Test
        void reshape_boolean_2D() {
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
        void reshape_boolean_2D_invalidCols() {
            boolean[] a = { true, false, true, false, true };
            boolean[][] b = Arrays.reshape(a, 3);

            assertArrayEquals(new boolean[][] { { true, false, true }, { false, true } }, b);

        }

        @Test
        void reshape_boolean_2D_invalidCols_values_true_false() {
            boolean[] arr = { true, false };
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, 0));
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, -1));
        }

        @Test
        void reshape_boolean_2D_unevenDivision() {
            boolean[] arr = { true, false, true, false, true };
            boolean[][] result = Arrays.reshape(arr, 2);
            assertEquals(3, result.length);
            assertArrayEquals(new boolean[] { true, false }, result[0]);
            assertArrayEquals(new boolean[] { true, false }, result[1]);
            assertArrayEquals(new boolean[] { true }, result[2]);
        }

        @Test
        void reshape_boolean_2D_validEvenDivision() {
            boolean[] arr = { true, false, true, false, true, false };
            boolean[][] result = Arrays.reshape(arr, 2);
            assertEquals(3, result.length);
            assertArrayEquals(new boolean[] { true, false }, result[0]);
            assertArrayEquals(new boolean[] { true, false }, result[1]);
            assertArrayEquals(new boolean[] { true, false }, result[2]);
        }

        @Test
        void reshape_boolean_3D() {
            boolean[] a = new boolean[12];
            for (int i = 0; i < 12; i++) {
                a[i] = (i % 2 == 0);
            }
            boolean[][][] result = Arrays.reshape(a, 2, 3);

            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals(3, result[0][0].length);
        }

        @Test
        void reshape_boolean_3D_invalidDimensions() {
            boolean[] arr = { true };
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, 0, 1));
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, 1, 0));
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, -1, 1));
        }

        @Test
        void reshape_boolean_3D_valid() {
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
        void reshape_booleanArray() {
            boolean[] arr = { true, false, true, false };
            boolean[][] result = Arrays.reshape(arr, 2);

            assertEquals(2, result.length);
            assertArrayEquals(new boolean[] { true, false }, result[0]);
            assertArrayEquals(new boolean[] { true, false }, result[1]);
        }

        @Test
        void reshape_booleanArray_toCols() {
            // Test normal reshape
            boolean[] arr = { true, false, true, false, true };
            boolean[][] reshaped = Arrays.reshape(arr, 2);
            assertArrayEquals(new boolean[] { true, false }, reshaped[0]);
            assertArrayEquals(new boolean[] { true, false }, reshaped[1]);
            assertArrayEquals(new boolean[] { true }, reshaped[2]);

            // Test perfect division
            boolean[] perfectArr = { true, false, true, false };
            boolean[][] perfectReshaped = Arrays.reshape(perfectArr, 2);
            assertArrayEquals(new boolean[] { true, false }, perfectReshaped[0]);
            assertArrayEquals(new boolean[] { true, false }, perfectReshaped[1]);

            // Test with empty array
            boolean[] emptyArr = {};
            boolean[][] emptyReshaped = Arrays.reshape(emptyArr, 2);
            assertEquals(0, emptyReshaped.length);

            // Test with single column
            boolean[] singleColArr = { true, false, true };
            boolean[][] singleColReshaped = Arrays.reshape(singleColArr, 1);
            assertEquals(3, singleColReshaped.length);
            assertArrayEquals(new boolean[] { true }, singleColReshaped[0]);
            assertArrayEquals(new boolean[] { false }, singleColReshaped[1]);
            assertArrayEquals(new boolean[] { true }, singleColReshaped[2]);

            // Test with more columns than elements
            boolean[] smallArr = { true, false };
            boolean[][] smallReshaped = Arrays.reshape(smallArr, 5);
            assertEquals(1, smallReshaped.length);
            assertArrayEquals(new boolean[] { true, false }, smallReshaped[0]);
        }

        @Test
        void reshape_booleanArray_toRowsCols() {
            // Test normal reshape
            boolean[] arr = { true, false, true, false, true, false };
            boolean[][][] reshaped = Arrays.reshape(arr, 2, 2);
            assertArrayEquals(new boolean[] { true, false }, reshaped[0][0]);
            assertArrayEquals(new boolean[] { true, false }, reshaped[0][1]);
            assertArrayEquals(new boolean[] { true, false }, reshaped[1][0]);

            // Test with uneven division
            boolean[] unevenArr = { true, false, true, false, true };
            boolean[][][] unevenReshaped = Arrays.reshape(unevenArr, 2, 2);
            assertArrayEquals(new boolean[] { true, false }, unevenReshaped[0][0]);
            assertArrayEquals(new boolean[] { true, false }, unevenReshaped[0][1]);
            assertArrayEquals(new boolean[] { true }, unevenReshaped[1][0]);

            // Test with empty array
            boolean[] emptyArr = {};
            boolean[][][] emptyReshaped = Arrays.reshape(emptyArr, 2, 2);
            assertEquals(0, emptyReshaped.length);

            // Test with single element blocks
            boolean[] singleArr = { true, false, true, false };
            boolean[][][] singleReshaped = Arrays.reshape(singleArr, 1, 1);
            assertEquals(4, singleReshaped.length);
            assertArrayEquals(new boolean[] { true }, singleReshaped[0][0]);
            assertArrayEquals(new boolean[] { false }, singleReshaped[1][0]);
            assertArrayEquals(new boolean[] { true }, singleReshaped[2][0]);
            assertArrayEquals(new boolean[] { false }, singleReshaped[3][0]);
        }

        @Test
        void reshape_booleanArray_toRowsCols_invalidInput() {
            boolean[] arr = { true, false };

            // Test with zero rows
            assertThrows(IllegalArgumentException.class, () -> {
                Arrays.reshape(arr, 0, 2);
            });

            // Test with zero columns
            assertThrows(IllegalArgumentException.class, () -> {
                Arrays.reshape(arr, 2, 0);
            });

            // Test with negative rows
            assertThrows(IllegalArgumentException.class, () -> {
                Arrays.reshape(arr, -1, 2);
            });

            // Test with negative columns
            assertThrows(IllegalArgumentException.class, () -> {
                Arrays.reshape(arr, 2, -1);
            });
        }

        @Test
        void reshape_booleanArray3D() {
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
        void reshape_booleanArray3DBlockCapacityExceedsIntRange() {
            // 46341 * 46341 overflows int. The block capacity is computed with long arithmetic,
            // so this is accepted and yields a single partial block rather than throwing.
            boolean[] arr = { true, false, true };
            boolean[][][] result = Arrays.reshape(arr, 46341, 46341);

            assertEquals(1, result.length);
            assertEquals(1, result[0].length);
            assertArrayEquals(new boolean[] { true, false, true }, result[0][0]);

            // Non-positive dimensions are still rejected.
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, 0, 2));
            assertThrows(IllegalArgumentException.class, () -> Arrays.reshape(arr, 2, 0));
        }

        @Test
        void reshape_booleanArray3DNull() {
            boolean[][][] result = Arrays.reshape((boolean[]) null, 2, 2);
            assertEquals(0, result.length);
        }

        @Test
        void reshape_booleanArrayInvalidCols() {
            boolean[] arr = { true, false, true };
            // The reshape method allows non-evenly divisible arrays using CEILING rounding
            boolean[][] result = Arrays.reshape(arr, 2);
            assertEquals(2, result.length); // 2 rows with columnCount=2 each (last row has 1 element)
            assertArrayEquals(new boolean[] { true, false }, result[0]);
            assertArrayEquals(new boolean[] { true }, result[1]);
        }

        @Test
        void reshape_booleanArrayNull() {
            boolean[][] result = Arrays.reshape((boolean[]) null, 2);
            assertEquals(0, result.length);
        }

        @Test
        void reshape_byte_2D() {
            byte[] a = { 1, 2, 3, 4, 5, 6 };
            byte[][] result = Arrays.reshape(a, 3);

            assertEquals(2, result.length);
            assertEquals(3, result[0].length);
            assertEquals(1, result[0][0]);
            assertEquals(4, result[1][0]);
        }

        @Test
        void reshape_byte_3D() {
            byte[] a = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
            byte[][][] result = Arrays.reshape(a, 2, 3);

            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals(3, result[0][0].length);
        }

        @Test
        void reshape_char_2D() {
            char[] a = { 'a', 'b', 'c', 'd', 'e', 'f' };
            char[][] result = Arrays.reshape(a, 3);

            assertEquals(2, result.length);
            assertEquals(3, result[0].length);
            assertEquals('a', result[0][0]);
            assertEquals('d', result[1][0]);
        }

        @Test
        void reshape_char_2D_valid() {
            char[] arr = { 'a', 'b', 'c', 'd', 'e', 'f' };
            char[][] result = Arrays.reshape(arr, 3);
            assertEquals(2, result.length);
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, result[0]);
            assertArrayEquals(new char[] { 'd', 'e', 'f' }, result[1]);
        }

        @Test
        void reshape_char_3D() {
            char[] a = "abcdefghijkl".toCharArray();
            char[][][] result = Arrays.reshape(a, 2, 3);

            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals(3, result[0][0].length);
            assertEquals('a', result[0][0][0]);
        }

        @Test
        void reshape_char_3D_valid() {
            char[] arr = { 'a', 'b', 'c', 'd' };
            char[][][] result = Arrays.reshape(arr, 1, 2);
            assertEquals(2, result.length);
            assertArrayEquals(new char[] { 'a', 'b' }, result[0][0]);
            assertArrayEquals(new char[] { 'c', 'd' }, result[1][0]);
        }

        @Test
        void reshape_charArray() {
            char[] arr = { 'a', 'b', 'c', 'd' };
            char[][] result = Arrays.reshape(arr, 2);

            assertEquals(2, result.length);
            assertArrayEquals(new char[] { 'a', 'b' }, result[0]);
            assertArrayEquals(new char[] { 'c', 'd' }, result[1]);
        }

        @Test
        void reshape_double_2D() {
            double[] a = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
            double[][] result = Arrays.reshape(a, 3);

            assertEquals(2, result.length);
            assertEquals(3, result[0].length);
            assertEquals(1.0, result[0][0], 0.0001);
            assertEquals(4.0, result[1][0], 0.0001);
        }

        @Test
        void reshape_double_3D() {
            double[] a = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0 };
            double[][][] result = Arrays.reshape(a, 2, 3);

            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals(3, result[0][0].length);
        }

        @Test
        void reshape_emptyArray() {
            int[] a = {};
            int[][] b = Arrays.reshape(a, 1);
            assertEquals(0, b.length);

        }

        @Test
        void reshape_float_2D() {
            float[] a = { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f };
            float[][] result = Arrays.reshape(a, 3);

            assertEquals(2, result.length);
            assertEquals(3, result[0].length);
            assertEquals(1.0f, result[0][0], 0.0001f);
            assertEquals(4.0f, result[1][0], 0.0001f);
        }

        @Test
        void reshape_float_3D() {
            float[] a = { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f };
            float[][][] result = Arrays.reshape(a, 2, 3);

            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals(3, result[0][0].length);
        }

        @Test
        void reshape_int_2D() {
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
        void reshape_int_2D_singleRow() {
            int[] a = { 1, 2, 3 };
            int[][] result = Arrays.reshape(a, 3);

            assertEquals(1, result.length);
            assertEquals(3, result[0].length);
            assertArrayEquals(new int[] { 1, 2, 3 }, result[0]);
        }

        @Test
        void reshape_int_3D() {
            int[] a = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
            int[][][] result = Arrays.reshape(a, 2, 3);

            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals(3, result[0][0].length);
            assertEquals(1, result[0][0][0]);
            assertEquals(12, result[1][1][2]);
        }

        @Test
        void reshape_long_2D() {
            long[] a = { 1L, 2L, 3L, 4L, 5L, 6L };
            long[][] result = Arrays.reshape(a, 3);

            assertEquals(2, result.length);
            assertEquals(3, result[0].length);
            assertEquals(1L, result[0][0]);
            assertEquals(4L, result[1][0]);
        }

        @Test
        void reshape_long_3D() {
            long[] a = { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L };
            long[][][] result = Arrays.reshape(a, 2, 3);

            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals(3, result[0][0].length);
        }

        @Test
        void reshape_short_2D() {
            short[] a = { 1, 2, 3, 4, 5, 6 };
            short[][] result = Arrays.reshape(a, 3);

            assertEquals(2, result.length);
            assertEquals(3, result[0].length);
            assertEquals(1, result[0][0]);
            assertEquals(4, result[1][0]);
        }

        @Test
        void reshape_short_3D() {
            short[] a = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
            short[][][] result = Arrays.reshape(a, 2, 3);

            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals(3, result[0][0].length);
        }

        @Test
        void reshape_singleElement_2D() {
            boolean[] arr = { true };
            boolean[][] result = Arrays.reshape(arr, 1);
            assertEquals(1, result.length);
            assertArrayEquals(new boolean[] { true }, result[0]);
        }

        @Test
        void reshape_singleElement_3D() {
            boolean[] arr = { false };
            boolean[][][] result = Arrays.reshape(arr, 1, 1);
            assertEquals(1, result.length);
            assertEquals(1, result[0].length);
            assertArrayEquals(new boolean[] { false }, result[0][0]);
        }

        @Test
        void reshape_values_1_2_3_4_5_6_7() {
            // Test normal reshape
            Integer[] array = { 1, 2, 3, 4, 5, 6, 7 };
            Integer[][] reshaped = ff.reshape(array, 3);
            assertEquals(3, reshaped.length);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, reshaped[0]);
            assertArrayEquals(new Integer[] { 4, 5, 6 }, reshaped[1]);
            assertArrayEquals(new Integer[] { 7 }, reshaped[2]);

            // Test exact division
            Integer[] array2 = { 1, 2, 3, 4, 5, 6 };
            Integer[][] reshaped2 = ff.reshape(array2, 2);
            assertEquals(3, reshaped2.length);
            assertArrayEquals(new Integer[] { 1, 2 }, reshaped2[0]);
            assertArrayEquals(new Integer[] { 3, 4 }, reshaped2[1]);
            assertArrayEquals(new Integer[] { 5, 6 }, reshaped2[2]);

            // Test single column
            Integer[] array3 = { 1, 2, 3 };
            Integer[][] reshaped3 = ff.reshape(array3, 1);
            assertEquals(3, reshaped3.length);
            assertArrayEquals(new Integer[] { 1 }, reshaped3[0]);
            assertArrayEquals(new Integer[] { 2 }, reshaped3[1]);
            assertArrayEquals(new Integer[] { 3 }, reshaped3[2]);

            // Test with columns equal to array length
            Integer[] array4 = { 1, 2, 3 };
            Integer[][] reshaped4 = ff.reshape(array4, 3);
            assertEquals(1, reshaped4.length);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, reshaped4[0]);

            // Test with empty array
            Integer[] emptyArray = {};
            Integer[][] reshapedEmpty = ff.reshape(emptyArray, 3);
            assertEquals(0, reshapedEmpty.length);

            // Test invalid columns
            assertThrows(IllegalArgumentException.class, () -> {
                ff.reshape(array, 0);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                ff.reshape(array, -1);
            });
        }

        @Test
        void reshape_values_1_2_3_4_5_6_7_8() {
            // Test basic reshape
            Integer[] flat = { 1, 2, 3, 4, 5, 6, 7, 8 };
            Integer[][][] reshaped = fff.reshape(flat, 2, 2);
            assertEquals(2, reshaped.length);
            assertEquals(2, reshaped[0].length);
            assertEquals(2, reshaped[0][0].length);
            assertEquals(1, reshaped[0][0][0]);
            assertEquals(2, reshaped[0][0][1]);
            assertEquals(3, reshaped[0][1][0]);
            assertEquals(4, reshaped[0][1][1]);
            assertEquals(5, reshaped[1][0][0]);
            assertEquals(6, reshaped[1][0][1]);
            assertEquals(7, reshaped[1][1][0]);
            assertEquals(8, reshaped[1][1][1]);

            // Test with uneven division
            Integer[] flat2 = { 1, 2, 3, 4, 5 };
            Integer[][][] reshaped2 = fff.reshape(flat2, 2, 2);
            assertEquals(2, reshaped2.length);
            assertEquals(2, reshaped2[0].length);
            assertEquals(2, reshaped2[0][0].length);
            assertEquals(1, reshaped2[1].length);
            assertEquals(1, reshaped2[1][0].length);
            assertEquals(5, reshaped2[1][0][0]);

            // Test with empty array
            Integer[] emptyFlat = new Integer[0];
            Integer[][][] emptyReshaped = fff.reshape(emptyFlat, 2, 2);
            assertEquals(0, emptyReshaped.length);

            // Test with invalid arguments
            assertThrows(IllegalArgumentException.class, () -> {
                fff.reshape(flat, 0, 2);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                fff.reshape(flat, 2, 0);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                fff.reshape(flat, -1, 2);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                fff.reshape(null, 2, 2);
            });
        }

        @Test
        void shortReshape2D() {
            // Line 5993: reshape({1,2,3,4,5}, 2) => {{1,2},{3,4},{5}}
            short[] array = { 1, 2, 3, 4, 5 };
            short[][] reshaped = Arrays.reshape(array, 2);
            assertArrayEquals(new short[] { 1, 2 }, reshaped[0]);
            assertArrayEquals(new short[] { 3, 4 }, reshaped[1]);
            assertArrayEquals(new short[] { 5 }, reshaped[2]);
        }
    }

    @Nested
    class FlattenTests {

        @ParameterizedTest(name = "{0}: no elements in nonempty cube")
        @MethodSource("com.landawn.abacus.util.ArrayTestSupport#primitiveTypes")
        void flatten_nullEmptyAndMissingBranches_returnsEmptyVector(Class<?> elementType) throws Throwable {
            Object rows = nestedArray(elementType, 2, null, primitiveArray(elementType));
            Object cube = nestedArray(elementType, 3, null, nestedArray(elementType, 2), rows);
            for (Object input : new Object[] { null, nestedArray(elementType, 3), cube }) {
                Object flattened = invokeArrayMethod(arrayMethod("flatten", elementType, 3, 1), input);
                assertEquals(arrayType(elementType, 1), flattened.getClass());
                assertEquals(0, java.lang.reflect.Array.getLength(flattened));
            }
        }

        @Test
        void aWideInputKeepsTheWideComponentType() {
            final Object[][] wide = new Object[][] { { 1 }, { 2 } };
            final Object[] flat = Arrays.ff.flatten(wide);

            assertEquals(Object.class, flat.getClass().getComponentType());
            assertDoesNotThrow(() -> flat[0] = Double.valueOf(1.5));
        }

        @Test
        void booleanFlatten2D() {
            // Line 2131-2134: flatten({{true,false},{true},{false,true}}) => {true,false,true,false,true}
            boolean[][] arr = { { true, false }, { true }, { false, true } };
            boolean[] flattened = Arrays.flatten(arr);
            assertArrayEquals(new boolean[] { true, false, true, false, true }, flattened);
        }

        @Test
        void booleanFlatten3D() {
            // Line 2170-2173: flatten({{{true,false},{true}},{{false,true}}}) => {true,false,true,false,true}
            boolean[][][] cube = { { { true, false }, { true } }, { { false, true } } };
            boolean[] flattened = Arrays.flatten(cube);
            assertArrayEquals(new boolean[] { true, false, true, false, true }, flattened);
        }

        @Test
        void byteApplyOnFlattened() {
            // Line 4807-4809: mutateViaFlatArray({{3,1},{4,2}}, t -> sort(t)) => {{1,2},{3,4}}
            byte[][] arr = { { 3, 1 }, { 4, 2 } };
            Arrays.mutateViaFlatArray(arr, t -> java.util.Arrays.sort(t));
            assertArrayEquals(new byte[] { 1, 2 }, arr[0]);
            assertArrayEquals(new byte[] { 3, 4 }, arr[1]);
        }

        @Test
        void byteFlatten2D() {
            // Line 4722-4725
            byte[][] arr = { { 1, 2 }, { 3 }, { 4, 5 } };
            byte[] flattened = Arrays.flatten(arr);
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, flattened);
        }

        @Test
        void classJavadoc_flattenIntGrid() {
            // Line 101-102: int[][] grid = {{1, 2, 3}, {4, 5, 6}}; => flatten => {1, 2, 3, 4, 5, 6}
            int[][] grid = { { 1, 2, 3 }, { 4, 5, 6 } };
            int[] flattened = Arrays.flatten(grid);
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, flattened);
        }

        @Test
        void doubleApplyOnFlattened_3D() {
            double[][][] cube = { { { 9.0, 2.0 } }, { { 5.0 }, { 1.0 } } };
            Arrays.mutateViaFlatArray(cube, arr -> java.util.Arrays.sort(arr));
            assertArrayEquals(new double[] { 1.0, 2.0 }, cube[0][0], 0.0);
            assertArrayEquals(new double[] { 5.0 }, cube[1][0], 0.0);
            assertArrayEquals(new double[] { 9.0 }, cube[1][1], 0.0);
        }

        @Test
        void doubleFlatten_3D_withNullsAndEmpties() {
            double[][][] cube = { { { 1.0 }, { 2.0, 3.0 } }, null, { { 4.0 } }, { null, {}, { 5.0 } } };
            double[] flat = Arrays.flatten(cube);
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }, flat, 0.0);
        }

        @Test
        void ff_flatten_2D_mixedNullsAndEmpties() {
            final Integer[][] a = new Integer[][] { { 1, 2 }, null, {}, { 3 } };
            final Integer[] r = ff.flatten(a);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, r);
        }

        @Test
        void fF_flatten_jaggedWithNullAndEmpty() {
            Integer[][] arr = { { 1, 2 }, null, {}, { 3, 4, 5 } };
            Integer[] result = ff.flatten(arr);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5 }, result);
        }

        @Test
        void ff_flatten_keepsTheInputRuntimeComponentType() {
            final Number[][] grid = new Integer[][] { { 1 }, { 2 } };
            final Number[] flat = Arrays.ff.flatten(grid);

            assertEquals(Integer.class, flat.getClass().getComponentType(), "narrower than the declared Number[]");
            assertThrows(ArrayStoreException.class, () -> flat[0] = Double.valueOf(1.5));
        }

        @Test
        void fF_flatten_nullArray() {
            Integer[][] arr = null;
            assertThrows(IllegalArgumentException.class, () -> Arrays.ff.flatten(arr));
        }

        @Test
        void fF_flatten_preservesComponentType() {
            Integer[][] arr = { { 1, 2 }, { 3 } };
            Integer[] flat = ff.flatten(arr);
            // Component type must be Integer, not Object
            assertEquals(Integer.class, flat.getClass().getComponentType());
            assertArrayEquals(new Integer[] { 1, 2, 3 }, flat);
        }

        @Test
        void fF_flatten_skipsNullAndEmptySubArrays() {
            Integer[][] arr = { { 1, 2 }, null, { 3, 4, 5 }, {}, { 6 } };
            Integer[] flat = ff.flatten(arr);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, flat);
        }

        @Test
        void fF_flatten_valid() {
            Integer[][] arr = { { 1, 2 }, { 3, 4, 5 }, { 6 } };
            Integer[] result = Arrays.ff.flatten(arr);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, result);
        }

        @Test
        void fF_flatten_withNullSubArrays() {
            Integer[][] arr = { { 1, 2 }, null, { 3 } };
            Integer[] result = Arrays.ff.flatten(arr);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
        }

        @Test
        void fFF_flatten_jaggedWithNullAndEmpty() {
            Integer[][][] arr = { { { 1, 2 }, null, { 3 } }, null, { {}, { 4, 5 } } };
            Integer[] result = fff.flatten(arr);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5 }, result);
        }

        @Test
        void fFF_flatten_nullArray() {
            Integer[][][] arr = null;
            assertThrows(IllegalArgumentException.class, () -> Arrays.fff.flatten(arr));
        }

        @Test
        void fFF_flatten_skipsNullsAtAllLevels() {
            Integer[][][] arr = { { { 1, 2 }, null, { 3 } }, null, { { 4, 5, 6 } } };
            Integer[] flat = fff.flatten(arr);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, flat);
            assertEquals(Integer.class, flat.getClass().getComponentType());
        }

        @Test
        void fFF_flatten_valid() {
            Integer[][][] arr = { { { 1, 2 } }, { { 3 } }, { { 4, 5, 6 } } };
            Integer[] result = Arrays.fff.flatten(arr);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, result);
        }

        @Test
        void fFF_flatten_withNullSubArrays() {
            Integer[][][] arr = { { { 1, 2 }, null }, null, { { 3 } } };
            Integer[] result = Arrays.fff.flatten(arr);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
        }

        @Test
        void fff_flattenAndReshape_keepTheInputRuntimeComponentType() {
            final Number[][][] cube = new Integer[][][] { { { 1 } } };
            assertEquals(Integer.class, Arrays.fff.flatten(cube).getClass().getComponentType());

            final Number[] src = new Integer[] { 1, 2, 3 };
            final Number[][][] resh = Arrays.fff.reshape(src, 2, 2);
            assertEquals(Integer.class, resh.getClass().getComponentType().getComponentType().getComponentType());
        }

        @Test
        void flatten() {
            // Test normal flatten
            Integer[][] array = { { 1, 2 }, { 3, 4, 5 }, { 6 } };
            Integer[] flattened = ff.flatten(array);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, flattened);

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            Integer[] flattened2 = ff.flatten(arrayWithNulls);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, flattened2);

            // Test with empty sub-arrays
            Integer[][] arrayWithEmpty = { { 1, 2 }, {}, { 3, 4 } };
            Integer[] flattened3 = ff.flatten(arrayWithEmpty);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, flattened3);

            // Test with empty array
            Integer[][] emptyArray = {};
            Integer[] flattened4 = ff.flatten(emptyArray);
            assertArrayEquals(new Integer[] {}, flattened4);

            // Test with all null sub-arrays
            Integer[][] isAllNulls = { null, null, null };
            Integer[] flattened5 = ff.flatten(isAllNulls);
            assertArrayEquals(new Integer[] {}, flattened5);

            // Test with String array
            String[][] stringArray = { { "a", "b" }, { "c", "d", "e" } };
            String[] flattenedStrings = ff.flatten(stringArray);
            assertArrayEquals(new String[] { "a", "b", "c", "d", "e" }, flattenedStrings);
        }

        @Test
        void flatten_2D_allNullRows_int() {
            // Note: int[][] cannot literally hold "null, null" as rows are int[] which is an object;
            // but two null int[] rows are perfectly legal.
            final int[][] a = new int[][] { null, null };
            assertArrayEquals(new int[0], Arrays.flatten(a));
        }

        @Test
        void flatten_2D_boolean_normal() {
            boolean[][] arr = { { true, false }, { true, false } };
            boolean[] result = Arrays.flatten(arr);
            assertEquals(4, result.length);
            assertTrue(result[0]);
            assertFalse(result[1]);
            assertTrue(result[2]);
            assertFalse(result[3]);
        }

        @Test
        void flatten_2D_boolean_null() {
            boolean[][] arr = null;
            boolean[] result = Arrays.flatten(arr);
            assertArrayEquals(CommonUtil.EMPTY_BOOLEAN_ARRAY, result);
        }

        @Test
        void flatten_2D_byte_normal() {
            byte[][] arr = { { 1, 2, 3 }, { 4, 5 } };
            byte[] result = Arrays.flatten(arr);
            assertEquals(5, result.length);
            assertEquals(1, result[0]);
            assertEquals(5, result[4]);
        }

        @Test
        void flatten_2D_char_normal() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            char[] result = Arrays.flatten(arr);
            assertEquals(4, result.length);
            assertEquals('a', result[0]);
            assertEquals('d', result[3]);
        }

        @Test
        void flatten_2D_double_normal() {
            double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            double[] result = Arrays.flatten(arr);
            assertEquals(4, result.length);
            assertEquals(1.5, result[0], 0.001);
            assertEquals(4.5, result[3], 0.001);
        }

        @Test
        void flatten_2D_double_null() {
            double[][] arr = null;
            double[] result = Arrays.flatten(arr);
            assertArrayEquals(CommonUtil.EMPTY_DOUBLE_ARRAY, result, 0.001);
        }

        @Test
        void flatten_2D_emptyOuter_int() {
            assertArrayEquals(new int[0], Arrays.flatten(new int[0][]));
            assertArrayEquals(new int[0], Arrays.flatten((int[][]) null));
        }

        @Test
        void flatten_2D_float_normal() {
            float[][] arr = { { 1.1f, 2.2f }, { 3.3f, 4.4f } };
            float[] result = Arrays.flatten(arr);
            assertEquals(4, result.length);
            assertEquals(1.1f, result[0], 0.001f);
            assertEquals(4.4f, result[3], 0.001f);
        }

        @Test
        void flatten_2D_int_jaggedArray() {
            int[][] arr = { { 1, 2 }, { 3, 4, 5 }, { 6 } };
            int[] result = Arrays.flatten(arr);
            assertEquals(6, result.length);
            assertEquals(1, result[0]);
            assertEquals(6, result[5]);
        }

        @Test
        void flatten_2D_int_normal() {
            int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            int[] result = Arrays.flatten(arr);
            assertEquals(6, result.length);
            assertEquals(1, result[0]);
            assertEquals(6, result[5]);
        }

        @Test
        void flatten_2D_int_null() {
            int[][] arr = null;
            int[] result = Arrays.flatten(arr);
            assertArrayEquals(CommonUtil.EMPTY_INT_ARRAY, result);
        }

        @Test
        void flatten_2D_long_normal() {
            long[][] arr = { { 100L, 200L }, { 300L, 400L } };
            long[] result = Arrays.flatten(arr);
            assertEquals(4, result.length);
            assertEquals(100L, result[0]);
            assertEquals(400L, result[3]);
        }

        @Test
        void flatten_2D_mixedNullEmptyAndPopulated_boolean() {
            final boolean[][] a = new boolean[][] { { true, false }, null, {}, { true } };
            assertArrayEquals(new boolean[] { true, false, true }, Arrays.flatten(a));
        }

        @Test
        void flatten_2D_mixedNullEmptyAndPopulated_double() {
            final double[][] a = new double[][] { { 1.5, 2.5 }, null, {}, { 3.5 } };
            assertArrayEquals(new double[] { 1.5, 2.5, 3.5 }, Arrays.flatten(a));
        }

        @Test
        void flatten_2D_mixedNullEmptyAndPopulated_int() {
            final int[][] a = new int[][] { { 1, 2 }, null, {}, { 3 } };
            assertArrayEquals(new int[] { 1, 2, 3 }, Arrays.flatten(a));
        }

        @Test
        void flatten_2D_short_normal() {
            short[][] arr = { { 10, 20 }, { 30, 40 } };
            short[] result = Arrays.flatten(arr);
            assertEquals(4, result.length);
            assertEquals(10, result[0]);
            assertEquals(40, result[3]);
        }

        @Test
        void flatten_3D_boolean_normal() {
            boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
            boolean[] result = Arrays.flatten(arr);
            assertEquals(4, result.length);
            assertTrue(result[0]);
            assertFalse(result[1]);
            assertTrue(result[2]);
            assertFalse(result[3]);
        }

        @Test
        void flatten_3D_byte_normal() {
            byte[][][] arr = { { { 1, 2 } }, { { 3 } } };
            byte[] result = Arrays.flatten(arr);
            assertEquals(3, result.length);
            assertEquals(1, result[0]);
            assertEquals(3, result[2]);
        }

        @Test
        void flatten_3D_char_normal() {
            char[][][] arr = { { { 'x', 'y' } }, { { 'z' } } };
            char[] result = Arrays.flatten(arr);
            assertEquals(3, result.length);
            assertEquals('x', result[0]);
            assertEquals('z', result[2]);
        }

        @Test
        void flatten_3D_double_normal() {
            double[][][] arr = { { { 10.5, 20.5 } }, { { 30.5 } } };
            double[] result = Arrays.flatten(arr);
            assertEquals(3, result.length);
            assertEquals(10.5, result[0], 0.001);
            assertEquals(30.5, result[2], 0.001);
        }

        @Test
        void flatten_3D_empty_double() {
            assertArrayEquals(new double[0], Arrays.flatten(new double[0][][]));
            assertArrayEquals(new double[0], Arrays.flatten((double[][][]) null));
        }

        @Test
        void flatten_3D_float_normal() {
            float[][][] arr = { { { 1.0f, 2.0f } }, { { 3.0f } } };
            float[] result = Arrays.flatten(arr);
            assertEquals(3, result.length);
            assertEquals(1.0f, result[0], 0.001f);
            assertEquals(3.0f, result[2], 0.001f);
        }

        @Test
        void flatten_3D_int_normal() {
            int[][][] arr = { { { 1, 2 }, { 3 } }, { { 4, 5 } } };
            int[] result = Arrays.flatten(arr);
            assertEquals(5, result.length);
            assertEquals(1, result[0]);
            assertEquals(5, result[4]);
        }

        @Test
        void flatten_3D_long_normal() {
            long[][][] arr = { { { 1000L, 2000L } }, { { 3000L } } };
            long[] result = Arrays.flatten(arr);
            assertEquals(3, result.length);
            assertEquals(1000L, result[0]);
            assertEquals(3000L, result[2]);
        }

        @Test
        void flatten_3D_mixedNullsAndEmpties_int() {
            final int[][][] a = new int[][][] { { null, { 1 } }, { { 2 }, {} }, null };
            assertArrayEquals(new int[] { 1, 2 }, Arrays.flatten(a));
        }

        @Test
        void flatten_3D_mixedNullsAndEmpties_long() {
            final long[][][] a = new long[][][] { { null, { 1L } }, { { 2L }, {} }, null };
            assertArrayEquals(new long[] { 1L, 2L }, Arrays.flatten(a));
        }

        @Test
        void flatten_3D_short_normal() {
            short[][][] arr = { { { 100, 200 } }, { { 300 } } };
            short[] result = Arrays.flatten(arr);
            assertEquals(3, result.length);
            assertEquals(100, result[0]);
            assertEquals(300, result[2]);
        }

        @Test
        void flatten_boolean_2D() {
            boolean[][] a = { { true, false }, { true, false } };
            boolean[] result = Arrays.flatten(a);

            assertEquals(4, result.length);
            assertTrue(result[0]);
            assertFalse(result[1]);
            assertTrue(result[2]);
            assertFalse(result[3]);
        }

        @Test
        void flatten_boolean_2D_valid() {
            boolean[][] arr = { { true, false }, { true }, { false, true, false } };
            boolean[] result = Arrays.flatten(arr);
            assertArrayEquals(new boolean[] { true, false, true, false, true, false }, result);
        }

        @Test
        void flatten_boolean_2D_withNullSubArrays() {
            boolean[][] arr = { { true }, null, { false } };
            boolean[] result = Arrays.flatten(arr);
            assertArrayEquals(new boolean[] { true, false }, result);
        }

        @Test
        void flatten_boolean_3D() {
            boolean[][][] a = { { { true, false }, { true } }, { { false } } };
            boolean[] result = Arrays.flatten(a);

            assertEquals(4, result.length);
            assertTrue(result[0]);
            assertFalse(result[1]);
            assertTrue(result[2]);
            assertFalse(result[3]);
        }

        @Test
        void flatten_boolean_3D_valid() {
            boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
            boolean[] result = Arrays.flatten(arr);
            assertArrayEquals(new boolean[] { true, false, true, false }, result);
        }

        @Test
        void flatten_boolean2DArray() {
            // Test normal flatten
            boolean[][] arr = { { true, false }, { true }, { false, true } };
            boolean[] flattened = Arrays.flatten(arr);
            assertArrayEquals(new boolean[] { true, false, true, false, true }, flattened);

            // Test with empty two-dimensional array
            boolean[][] emptyArr = {};
            boolean[] emptyFlattened = Arrays.flatten(emptyArr);
            assertArrayEquals(new boolean[] {}, emptyFlattened);

            // Test with null two-dimensional array
            boolean[][] nullArr = null;
            boolean[] nullFlattened = Arrays.flatten(nullArr);
            assertArrayEquals(new boolean[] {}, nullFlattened);

            // Test with empty sub-arrays
            boolean[][] mixedArr = { { true, false }, {}, { true } };
            boolean[] mixedFlattened = Arrays.flatten(mixedArr);
            assertArrayEquals(new boolean[] { true, false, true }, mixedFlattened);

            // Test with all empty sub-arrays
            boolean[][] allEmptyArr = { {}, {}, {} };
            boolean[] allEmptyFlattened = Arrays.flatten(allEmptyArr);
            assertArrayEquals(new boolean[] {}, allEmptyFlattened);
        }

        @Test
        void flatten_boolean3DArray() {
            // Test normal flatten
            boolean[][][] arr = { { { true, false }, { true } }, { { false, true } } };
            boolean[] flattened = Arrays.flatten(arr);
            assertArrayEquals(new boolean[] { true, false, true, false, true }, flattened);

            // Test with empty three-dimensional array
            boolean[][][] emptyArr = {};
            boolean[] emptyFlattened = Arrays.flatten(emptyArr);
            assertArrayEquals(new boolean[] {}, emptyFlattened);

            // Test with null three-dimensional array
            boolean[][][] nullArr = null;
            boolean[] nullFlattened = Arrays.flatten(nullArr);
            assertArrayEquals(new boolean[] {}, nullFlattened);

            // Test with mixed empty arrays
            boolean[][][] mixedArr = { { { true } }, { {} }, { { false, true } } };
            boolean[] mixedFlattened = Arrays.flatten(mixedArr);
            assertArrayEquals(new boolean[] { true, false, true }, mixedFlattened);

            // Test with deeply nested empty arrays
            boolean[][][] deepEmptyArr = { { {}, {} }, { {} } };
            boolean[] deepEmptyFlattened = Arrays.flatten(deepEmptyArr);
            assertArrayEquals(new boolean[] {}, deepEmptyFlattened);
        }

        @Test
        void flatten_booleanArray2D() {
            boolean[][] arr = { { true, false }, { true, false } };
            boolean[] result = Arrays.flatten(arr);

            assertArrayEquals(new boolean[] { true, false, true, false }, result);
        }

        @Test
        void flatten_booleanArray2DEmpty() {
            boolean[][] arr = {};
            boolean[] result = Arrays.flatten(arr);
            assertEquals(0, result.length);
        }

        @Test
        void flatten_booleanArray2DNull() {
            boolean[] result = Arrays.flatten((boolean[][]) null);
            assertEquals(0, result.length);
        }

        @Test
        void flatten_booleanArray3D() {
            boolean[][][] arr = { { { true, false } } };
            boolean[] result = Arrays.flatten(arr);

            assertArrayEquals(new boolean[] { true, false }, result);
        }

        @Test
        void flatten_booleanArray3DNull() {
            boolean[] result = Arrays.flatten((boolean[][][]) null);
            assertEquals(0, result.length);
        }

        @Test
        void flatten_byte_2D() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            byte[] result = Arrays.flatten(a);

            assertArrayEquals(new byte[] { 1, 2, 3, 4 }, result);
        }

        @Test
        void flatten_byte_3D() {
            byte[][][] a = { { { 1, 2 }, { 3 } }, { { 4 } } };
            byte[] result = Arrays.flatten(a);

            assertArrayEquals(new byte[] { 1, 2, 3, 4 }, result);
        }

        @Test
        void flatten_char_2D() {
            char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
            char[] result = Arrays.flatten(a);

            assertArrayEquals("abcd".toCharArray(), result);
        }

        @Test
        void flatten_char_2D_valid() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd', 'e' } };
            char[] result = Arrays.flatten(arr);
            assertArrayEquals(new char[] { 'a', 'b', 'c', 'd', 'e' }, result);
        }

        @Test
        void flatten_char_3D() {
            char[][][] a = { { { 'a', 'b' }, { 'c' } }, { { 'd' } } };
            char[] result = Arrays.flatten(a);

            assertArrayEquals("abcd".toCharArray(), result);
        }

        @Test
        void flatten_char_3D_valid() {
            char[][][] arr = { { { 'x' }, { 'y', 'z' } } };
            char[] result = Arrays.flatten(arr);
            assertArrayEquals(new char[] { 'x', 'y', 'z' }, result);
        }

        @Test
        void flatten_charArray2D() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            char[] result = Arrays.flatten(arr);

            assertArrayEquals(new char[] { 'a', 'b', 'c', 'd' }, result);
        }

        @Test
        void flatten_double_2D() {
            double[][] a = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            double[] result = Arrays.flatten(a);

            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, result, 0.0001);
        }

        @Test
        void flatten_double_3D() {
            double[][][] a = { { { 1.0, 2.0 }, { 3.0 } }, { { 4.0 } } };
            double[] result = Arrays.flatten(a);

            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, result, 0.0001);
        }

        @Test
        void flatten_emptyArray() {
            int[][] a = {};
            int[] result = Arrays.flatten(a);

            assertEquals(0, result.length);
        }

        @Test
        void flatten_float_2D() {
            float[][] a = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            float[] result = Arrays.flatten(a);

            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, result, 0.0001f);
        }

        @Test
        void flatten_float_3D() {
            float[][][] a = { { { 1.0f, 2.0f }, { 3.0f } }, { { 4.0f } } };
            float[] result = Arrays.flatten(a);

            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, result, 0.0001f);
        }

        @Test
        void flatten_int_2D() {
            int[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
            int[] result = Arrays.flatten(a);

            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, result);
        }

        @Test
        void flatten_int_2D_jaggedArray() {
            int[][] a = { { 1, 2 }, { 3, 4, 5 }, { 6 } };
            int[] result = Arrays.flatten(a);

            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, result);
        }

        @Test
        void flatten_int_3D() {
            int[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } } };
            int[] result = Arrays.flatten(a);

            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, result);
        }

        @Test
        void flatten_long_2D() {
            long[][] a = { { 1L, 2L }, { 3L, 4L } };
            long[] result = Arrays.flatten(a);

            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L }, result);
        }

        @Test
        void flatten_long_3D() {
            long[][][] a = { { { 1L, 2L }, { 3L } }, { { 4L } } };
            long[] result = Arrays.flatten(a);

            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L }, result);
        }

        @Test
        void flatten_primitiveReturnsSharedEmptyOnlyForNullOrEmptyInput() {
            assertSame(Arrays.flatten((boolean[][]) null), Arrays.flatten(new boolean[0][]));
            assertSame(Arrays.flatten((char[][]) null), Arrays.flatten(new char[0][]));
            assertSame(Arrays.flatten((byte[][]) null), Arrays.flatten(new byte[0][]));
            assertSame(Arrays.flatten((short[][]) null), Arrays.flatten(new short[0][]));
            assertSame(Arrays.flatten((int[][]) null), Arrays.flatten(new int[0][]));
            assertSame(Arrays.flatten((long[][]) null), Arrays.flatten(new long[0][]));
            assertSame(Arrays.flatten((float[][]) null), Arrays.flatten(new float[0][]));
            assertSame(Arrays.flatten((double[][]) null), Arrays.flatten(new double[0][]));
            assertSame(Arrays.flatten((int[][][]) null), Arrays.flatten(new int[0][][]));

            // a non-empty outer array always yields a newly allocated result, even when it has no elements
            assertEquals(0, Arrays.flatten(new int[][] { null, {} }).length);
            assertTrue(Arrays.flatten(new int[][] { null, {} }) != Arrays.flatten((int[][]) null));
            assertTrue(Arrays.flatten(new int[][][] { null, { null } }) != Arrays.flatten((int[][][]) null));
            final int[][] rows = { { 1, 2 }, { 3 } };
            assertTrue(Arrays.flatten(rows) != Arrays.flatten(rows));
        }

        @Test
        void flatten_short_2D() {
            short[][] a = { { 1, 2 }, { 3, 4 } };
            short[] result = Arrays.flatten(a);

            assertArrayEquals(new short[] { 1, 2, 3, 4 }, result);
        }

        @Test
        void flatten_short_3D() {
            short[][][] a = { { { 1, 2 }, { 3 } }, { { 4 } } };
            short[] result = Arrays.flatten(a);

            assertArrayEquals(new short[] { 1, 2, 3, 4 }, result);
        }

        @Test
        void flatten_singleElement_2D() {
            boolean[][] arr = { { true } };
            boolean[] result = Arrays.flatten(arr);
            assertArrayEquals(new boolean[] { true }, result);
        }

        @Test
        void flatten_singleElement_3D() {
            boolean[][][] arr = { { { false } } };
            boolean[] result = Arrays.flatten(arr);
            assertArrayEquals(new boolean[] { false }, result);
        }

        @Test
        void flatten_values_1_2_3_4_5_6() {
            // Test basic flatten
            Integer[][][] cube = { { { 1, 2 } }, { { 3 } }, { { 4, 5, 6 } } };
            Integer[] flat = fff.flatten(cube);
            assertEquals(6, flat.length);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6 }, flat);

            // Test with empty sub-arrays
            Integer[][][] cube2 = { { { 1, 2 } }, {}, { { 3, 4 } } };
            Integer[] flat2 = fff.flatten(cube2);
            assertEquals(4, flat2.length);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, flat2);

            // Test with null sub-arrays
            Integer[][][] cube3 = { { { 1, 2 } }, null, { { 3, 4 } } };
            Integer[] flat3 = fff.flatten(cube3);
            assertEquals(4, flat3.length);
            assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, flat3);

            // Test with empty array
            Integer[][][] emptyCube = new Integer[0][][];
            Integer[] emptyFlat = fff.flatten(emptyCube);
            assertEquals(0, emptyFlat.length);

            // Test with all null/empty elements
            Integer[][][] nullCube = { null, { null, {} }, { {} } };
            Integer[] nullFlat = fff.flatten(nullCube);
            assertEquals(0, nullFlat.length);
        }

        @Test
        void flattenSkipsNullAndEmptyNestedCharArrays() {
            char[][] emptyRows = { null, new char[0], { 'a', 'b' }, { 'c' } };
            char[][][] nested = { null, new char[0][], { null, new char[0], { 'x', 'y' } }, { { 'z' }, null } };

            assertArrayEquals(new char[] { 'a', 'b', 'c' }, Arrays.flatten(emptyRows));
            assertArrayEquals(new char[] { 'x', 'y', 'z' }, Arrays.flatten(nested));
        }

        @Test
        void floatApplyOnFlattened_sort() {
            float[][] grid = { { 4.0f, 1.0f }, { 3.0f, 2.0f } };
            Arrays.mutateViaFlatArray(grid, t -> java.util.Arrays.sort(t));
            assertArrayEquals(new float[] { 1.0f, 2.0f }, grid[0], 0.0f);
            assertArrayEquals(new float[] { 3.0f, 4.0f }, grid[1], 0.0f);
        }

        @Test
        void floatFlatten_preservesNaN() {
            float[][] grid = { { 1.0f, Float.NaN }, { 3.0f, 4.0f } };
            float[] flat = Arrays.flatten(grid);
            assertEquals(4, flat.length);
            assertEquals(1.0f, flat[0], 0.0f);
            assertTrue(Float.isNaN(flat[1]));
            assertEquals(3.0f, flat[2], 0.0f);
            assertEquals(4.0f, flat[3], 0.0f);
        }

        @Test
        void genericEmptyFlattenPreservesRuntimeTypesAndFreshCustomArrays() {
            final Value[][] matrix = { null, new Value[0] };
            final Value[][][] cube = { null, new Value[0][], matrix };
            final Value[] flatMatrix = Arrays.ff.flatten(matrix);
            final Value[] flatCube = Arrays.fff.flatten(cube);
            assertEquals(Value[].class, flatMatrix.getClass());
            assertEquals(Value[].class, flatCube.getClass());
            assertEquals(0, flatMatrix.length);
            assertEquals(0, flatCube.length);
            assertNotSame(matrix[1], flatMatrix);
            assertNotSame(flatMatrix, Arrays.ff.flatten(matrix));
            assertNotSame(flatCube, Arrays.fff.flatten(cube));
            assertEquals(String[].class, Arrays.ff.flatten(new String[][] { null, {} }).getClass());
            assertEquals(String[].class, Arrays.fff.flatten(new String[][][] { null, { null, {} } }).getClass());
            assertThrows(IllegalArgumentException.class, () -> Arrays.ff.flatten((Object[][]) null));
            assertThrows(IllegalArgumentException.class, () -> Arrays.fff.flatten((Object[][][]) null));
        }

        @Test
        void genericFlattenPreservesArrayValuedElementTypes() {
            final int[][][] matrix = { null, new int[0][] };
            final int[][][][] cube = { null, matrix };
            final int[][] flatMatrix = Arrays.ff.flatten(matrix);
            final int[][] flatCube = Arrays.fff.flatten(cube);
            assertEquals(int[][].class, flatMatrix.getClass());
            assertEquals(int[][].class, flatCube.getClass());
            assertEquals(0, flatMatrix.length);
            assertEquals(0, flatCube.length);
            assertNotSame(flatMatrix, Arrays.ff.flatten(matrix));
            assertNotSame(flatCube, Arrays.fff.flatten(cube));

            final int[] element = { 7 };
            matrix[1] = new int[][] { element };
            assertSame(element, Arrays.ff.flatten(matrix)[0]);
            assertSame(element, Arrays.fff.flatten(cube)[0]);
        }

        @Test
        void intFlatten2D() {
            // Line 7358-7361: flatten({{1,2,3},{4,5},{6,7,8}}) => {1,2,3,4,5,6,7,8}
            int[][] arr = { { 1, 2, 3 }, { 4, 5 }, { 6, 7, 8 } };
            int[] result = Arrays.flatten(arr);
            assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, result);
        }

        @Test
        void nullHandling_flatten() {
            int[][] arr = null;
            int[] result = Arrays.flatten(arr);
            assertEquals(0, result.length);
        }

        @ParameterizedTest
        @MethodSource("com.landawn.abacus.util.ArrayTestSupport#primitiveTypes")
        void primitiveFlattenPreservesEmptyResultIdentityAndSparseShapes(final Class<?> type) throws Exception {
            final Object emptyRow = newArray(type, 0);
            final Object emptyMatrix = newArray(emptyRow.getClass(), 3);
            java.lang.reflect.Array.set(emptyMatrix, 1, emptyRow);
            final Object emptyCube = newArray(emptyMatrix.getClass(), 4);
            java.lang.reflect.Array.set(emptyCube, 1, newArray(emptyRow.getClass(), 0));
            java.lang.reflect.Array.set(emptyCube, 2, emptyMatrix);

            for (final Object input : new Object[] { emptyMatrix, emptyCube }) {
                final Method flatten = Arrays.class.getMethod("flatten", input.getClass());
                final Object result = flatten.invoke(null, input);
                assertEquals(emptyRow.getClass(), result.getClass());
                assertEquals(0, java.lang.reflect.Array.getLength(result));
                assertNotSame(emptyRow, result);
                assertNotSame(result, flatten.invoke(null, input));
            }

            final Object row = newArray(type, 1);
            java.lang.reflect.Array.set(emptyMatrix, 2, row);

            for (final Object input : new Object[] { emptyMatrix, emptyCube }) {
                final Object result = Arrays.class.getMethod("flatten", input.getClass()).invoke(null, input);
                assertEquals(row.getClass(), result.getClass());
                assertEquals(1, java.lang.reflect.Array.getLength(result));
                assertEquals(java.lang.reflect.Array.get(row, 0), java.lang.reflect.Array.get(result, 0));
                assertNotSame(row, result);
            }
        }

        @Test
        void property_flattenReshape2D_roundTrips() {
            final java.util.Random r = new java.util.Random(20260516L);

            for (int it = 0; it < ITERATIONS; it++) {
                final int len = r.nextInt(200); // include 0 (empty)
                final int cols = 1 + r.nextInt(20);
                final int[] a = randomIntArray(r, len);
                final int[] snapshot = a.clone();

                final int[][] reshaped = Arrays.reshape(a, cols);
                assertArrayEquals(snapshot, a, "reshape must not mutate its input");
                assertArrayEquals(a, Arrays.flatten(reshaped), "flatten(reshape(a, cols)) must equal a for len=" + len + ", cols=" + cols);

                if (len > 0) {
                    final int fullRows = len / cols;
                    final int rem = len % cols;
                    final int expectedRows = fullRows + (rem == 0 ? 0 : 1);
                    assertEquals(expectedRows, reshaped.length, "row count");
                    for (int i = 0; i < fullRows; i++) {
                        assertEquals(cols, reshaped[i].length, "full rows must have exactly cols elements");
                    }
                    if (rem != 0) {
                        assertEquals(rem, reshaped[reshaped.length - 1].length, "last (partial) row length");
                    }
                }
            }
        }

        @Test
        void property_flattenReshape3D_roundTrips() {
            final java.util.Random r = new java.util.Random(987654321L);

            for (int it = 0; it < ITERATIONS; it++) {
                final int len = r.nextInt(200);
                final int rows = 1 + r.nextInt(8);
                final int cols = 1 + r.nextInt(8);
                final int[] a = randomIntArray(r, len);
                final int[] snapshot = a.clone();

                final int[][][] reshaped = Arrays.reshape(a, rows, cols);
                assertArrayEquals(snapshot, a, "3D reshape must not mutate its input");
                assertArrayEquals(a, Arrays.flatten(reshaped),
                        "flatten(reshape(a, rows, cols)) must equal a for len=" + len + ", rows=" + rows + ", cols=" + cols);

                // Total element count across all blocks/rows must equal len.
                long total = 0;
                for (final int[][] block : reshaped) {
                    for (final int[] row : block) {
                        total += row.length;
                    }
                }
                assertEquals(len, total, "reshaped element count");
            }
        }

        @Test
        void property_flattenReturnsIndependentCopy() {
            final int[][] a = { { 1, 2, 3 }, { 4, 5 }, { 6 } };
            final int[] flat = Arrays.flatten(a);
            java.util.Arrays.fill(flat, 999);
            // Mutating the flattened result must not affect the source structure.
            assertArrayEquals(new int[] { 1, 2, 3 }, a[0]);
            assertArrayEquals(new int[] { 4, 5 }, a[1]);
            assertArrayEquals(new int[] { 6 }, a[2]);
        }

        @Test
        void shortFlatten2D() {
            // Line 6066: flatten({{1,2,3},{4,5}}) => {1,2,3,4,5}
            short[][] array = { { 1, 2, 3 }, { 4, 5 } };
            short[] flat = Arrays.flatten(array);
            assertArrayEquals(new short[] { 1, 2, 3, 4, 5 }, flat);
        }
    }

    @Nested
    class ShapeContractTests {

        @Test
        void helperMethods_edgeCases() {
            // Test reshape with maximum integer values
            boolean[] largeArr = new boolean[10];
            boolean[][] largeReshaped = Arrays.reshape(largeArr, Integer.MAX_VALUE);
            assertEquals(1, largeReshaped.length);
            assertEquals(10, largeReshaped[0].length);

            // Test with single element
            boolean[] singleArr = { true };
            boolean[][] singleReshaped = Arrays.reshape(singleArr, 1);
            assertEquals(1, singleReshaped.length);
            assertArrayEquals(new boolean[] { true }, singleReshaped[0]);

            // Test flatten with large nested arrays
            boolean[][] largeNested = new boolean[100][100];
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    largeNested[i][j] = (i + j) % 2 == 0;
                }
            }
            boolean[] largeFlattened = Arrays.flatten(largeNested);
            assertEquals(10000, largeFlattened.length);
        }
    }
}
