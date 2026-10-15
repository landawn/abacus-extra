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

import static com.landawn.abacus.util.ArrayTestSupport.ITERATIONS;
import static com.landawn.abacus.util.ArrayTestSupport.arrayMethod;
import static com.landawn.abacus.util.ArrayTestSupport.arrayType;
import static com.landawn.abacus.util.ArrayTestSupport.callback;
import static com.landawn.abacus.util.ArrayTestSupport.invokeArrayMethod;
import static com.landawn.abacus.util.ArrayTestSupport.nestedArray;
import static com.landawn.abacus.util.ArrayTestSupport.primitiveArray;
import static com.landawn.abacus.util.ArrayTestSupport.primitiveValue;
import static com.landawn.abacus.util.ArrayTestSupport.randomIntArray;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.landawn.abacus.util.Arrays.ff;
import com.landawn.abacus.util.Arrays.fff;

class ArraysZipTest {

    @Nested
    class ZipTests {

        @Test
        void objectZipGrid_shorterRightInput_suppliesDefaultsForRemainingRows() {
            String[][] left = { { "a" }, { "b", "c" } };
            String[][] right = { { "x" } };
            String[][] result = ff.zip(left, right, "missing-left", "missing-right", (a, b) -> a + ":" + b, String.class);
            assertArrayEquals(new String[][] { { "a:x" }, { "b:missing-right", "c:missing-right" } }, result);
        }

        @Test
        void objectZipCube_shorterRightInput_suppliesDefaultsForRemainingPlanes() {
            String[][][] left = { { { "a" } }, { { "b" }, { "c", "d" } } };
            String[][][] right = { { { "x" } } };
            String[][][] result = fff.zip(left, right, "missing-left", "missing-right", (a, b) -> a + ":" + b, String.class);
            assertArrayEquals(new String[][][] { { { "a:x" } }, { { "b:missing-right" }, { "c:missing-right", "d:missing-right" } } }, result);
        }

        @ParameterizedTest(name = "{0}, dimension {1}, {2} operands, defaults={3}")
        @MethodSource("com.landawn.abacus.util.ArraysZipTest#raggedZipCases")
        void zip_raggedInputs_preservesShapeCallbackOrderAndDefaults(Class<?> elementType, int dimensions, int operands, boolean padded) throws Throwable {
            int parameterCount = operands * (padded ? 2 : 1) + 1;
            java.lang.reflect.Method method = arrayMethod("zip", elementType, dimensions, parameterCount);
            Object[] fixtures = zipFixtures(elementType, dimensions);
            Object[] defaults = new Object[operands];
            for (int i = 0; i < operands; i++) {
                defaults[i] = primitiveValue(elementType, 9 + i);
            }
            Object marker = primitiveValue(elementType, 42);
            int combinations = operands == 2 ? fixtures.length * fixtures.length : fixtures.length * fixtures.length * fixtures.length;
            for (int scenario = 0; scenario < combinations; scenario++) {
                Object[] inputs = new Object[operands];
                int choices = scenario;
                for (int i = 0; i < operands; i++) {
                    inputs[i] = fixtures[choices % fixtures.length];
                    choices /= fixtures.length;
                }
                List<List<Object>> expectedCalls = new java.util.ArrayList<>();
                Object expected = expectedZip(elementType, dimensions, inputs, defaults, padded, marker, expectedCalls);
                String inputsBefore = java.util.Arrays.deepToString(inputs);
                List<List<Object>> actualCalls = new java.util.ArrayList<>();
                Object function = callback(method.getParameterTypes()[parameterCount - 1], values -> {
                    actualCalls.add(java.util.Arrays.asList(values.clone()));
                    return marker;
                });
                Object[] arguments = new Object[parameterCount];
                System.arraycopy(inputs, 0, arguments, 0, operands);
                if (padded) {
                    System.arraycopy(defaults, 0, arguments, operands, operands);
                }
                arguments[parameterCount - 1] = function;
                Object actual = invokeArrayMethod(method, arguments);
                assertArrayEquals(new Object[] { expected }, new Object[] { actual }, "scenario " + scenario);
                assertEquals(expectedCalls, actualCalls, "callback order and values for scenario " + scenario);
                assertEquals(inputsBefore, java.util.Arrays.deepToString(inputs), "zip must preserve input arrays");
            }
        }

        @Test
        void booleanZipBinary() {
            // zip({true,false,true,false}, {false,true,false}, (x,y) -> x&&y) => {false,false,false}
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true, false };
            boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);
            assertArrayEquals(new boolean[] { false, false, false }, result);
        }

        @Test
        void booleanZipBinaryWithDefaults() {
            // The shorter right input supplies true for missing values, so every OR result is true.
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true };
            boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);
            assertArrayEquals(new boolean[] { true, true, true, true }, result);
        }

        @Test
        void booleanZipTernary() {
            // zip({true,false,true,false}, {false,true,false}, {true,true}, (x,y,z)->x||y||z) => {true,true}
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true, false };
            boolean[] c = { true, true };
            boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);
            assertArrayEquals(new boolean[] { true, true }, result);
        }

        @Test
        void byteZipBinary() {
            // zip({1,2,3,4}, {5,6,7}, (x,y) -> (byte)(x+y)) => {6,8,10}
            byte[] a = { 1, 2, 3, 4 };
            byte[] b = { 5, 6, 7 };
            byte[] result = Arrays.zip(a, b, (x, y) -> (byte) (x + y));
            assertArrayEquals(new byte[] { 6, 8, 10 }, result);
        }

        @Test
        void byteZipBinaryWithDefaults() {
            // zip({1,2,3,4}, {5,6}, 0, 10, (x,y) -> (byte)(x+y)) => {6,8,13,14}
            byte[] a = { 1, 2, 3, 4 };
            byte[] b = { 5, 6 };
            byte[] result = Arrays.zip(a, b, (byte) 0, (byte) 10, (x, y) -> (byte) (x + y));
            assertArrayEquals(new byte[] { 6, 8, 13, 14 }, result);
        }

        @Test
        void byteZipTernary() {
            // zip({1,2,3,4}, {5,6,7}, {8,9}, (x,y,z) -> (byte)(x+y+z)) => {14,17}
            byte[] a = { 1, 2, 3, 4 };
            byte[] b = { 5, 6, 7 };
            byte[] c = { 8, 9 };
            byte[] result = Arrays.zip(a, b, c, (x, y, z) -> (byte) (x + y + z));
            assertArrayEquals(new byte[] { 14, 17 }, result);
        }

        @Test
        void byteZipTernaryWithDefaults() {
            // zip({1,2,3,4}, {5,6}, {8,9,10}, 0, 10, 20, ...) => {14,17,23,34}
            byte[] a = { 1, 2, 3, 4 };
            byte[] b = { 5, 6 };
            byte[] c = { 8, 9, 10 };
            byte[] result = Arrays.zip(a, b, c, (byte) 0, (byte) 10, (byte) 20, (x, y, z) -> (byte) (x + y + z));
            assertArrayEquals(new byte[] { 14, 17, 23, 34 }, result);
        }

        @Test
        void classJavadoc_zipIntBinary() {
            // zip({1,2,3}, {4,5,6}, (x,y) -> x + y*2) => {9,12,15}
            int[] a = { 1, 2, 3 };
            int[] b = { 4, 5, 6 };
            int[] zipped = Arrays.zip(a, b, (x, y) -> x + y * 2);
            assertArrayEquals(new int[] { 9, 12, 15 }, zipped);
        }

        @Test
        void defaultedInferredZipTranslatesIncompatibleResultsAndRetainsTheCause() {
            final Number[][] grid = new Integer[][] { { 1 } };
            final Number[][][] cube = new Integer[][][] { (Integer[][]) grid };
            final List<Executable> operations = List.of(//
                    () -> Arrays.ff.zip(grid, null, 0, 0, (left, right) -> 1.5d),
                    () -> Arrays.ff.zip(grid, null, null, 0, 0, 0, (first, second, third) -> 1.5d),
                    () -> Arrays.fff.zip(cube, null, 0, 0, (left, right) -> 1.5d),
                    () -> Arrays.fff.zip(cube, null, null, 0, 0, 0, (first, second, third) -> 1.5d));

            for (final Executable operation : operations) {
                final IllegalArgumentException failure = assertThrows(IllegalArgumentException.class, operation);
                assertInstanceOf(ArrayStoreException.class, failure.getCause());
            }
        }

        @Test
        void defaultedZipInfersArrayValuedDefaultsWithoutLosingADimension() {
            final String[] defaultValue = { "default" };
            final Integer[][] other2D = { { 1, 2 } };
            final Integer[][][] other3D = { other2D };
            final String[][][] binary2D = Arrays.ff.<String[], Integer, RuntimeException> zip(null, other2D, defaultValue, 0, (left, right) -> left);
            final String[][][] ternary2D = Arrays.ff.<String[], Integer, Integer, RuntimeException> zip(null, other2D, other2D, defaultValue, 0, 0,
                    (first, second, third) -> first);
            final String[][][][] binary3D = Arrays.fff.<String[], Integer, RuntimeException> zip(null, other3D, defaultValue, 0, (left, right) -> left);
            final String[][][][] ternary3D = Arrays.fff.<String[], Integer, Integer, RuntimeException> zip(null, other3D, other3D, defaultValue, 0, 0,
                    (first, second, third) -> first);

            assertEquals(String[][][].class, binary2D.getClass());
            assertEquals(String[][][].class, ternary2D.getClass());
            assertEquals(String[][][][].class, binary3D.getClass());
            assertEquals(String[][][][].class, ternary3D.getClass());
            assertSame(defaultValue, binary2D[0][0]);
            assertSame(defaultValue, binary2D[0][1]);
            assertSame(defaultValue, ternary2D[0][0]);
            assertSame(defaultValue, ternary2D[0][1]);
            assertSame(defaultValue, binary3D[0][0][0]);
            assertSame(defaultValue, binary3D[0][0][1]);
            assertSame(defaultValue, ternary3D[0][0][0]);
            assertSame(defaultValue, ternary3D[0][0][1]);
        }

        @Test
        void defaultedZipValidatesNullCallbackWithEmptyTypedInputs() {
            final Number[][] first2D = new Number[0][];
            final Integer[][] other2D = new Integer[0][];
            final Number[][][] first3D = new Number[0][][];
            final Integer[][][] other3D = new Integer[0][][];
            final List<Executable> operations = List.of(//
                    () -> Arrays.ff.<Number, Integer, RuntimeException> zip(first2D, other2D, null, 0, null),
                    () -> Arrays.ff.<Number, Integer, Integer, RuntimeException> zip(first2D, other2D, other2D, null, 0, 0, null),
                    () -> Arrays.fff.<Number, Integer, RuntimeException> zip(first3D, other3D, null, 0, null),
                    () -> Arrays.fff.<Number, Integer, Integer, RuntimeException> zip(first3D, other3D, other3D, null, 0, 0, null));

            for (final Executable operation : operations) {
                final IllegalArgumentException failure = assertThrows(IllegalArgumentException.class, operation);
                assertEquals("'zipFunction' cannot be null", failure.getMessage());
            }
        }

        @Test
        void defaultedZipValidatesTheCallbackBeforeAllocatingItsResult() {
            final Object defaultValue = arrayWithDimensions(255);
            final List<Executable> operations = List.of(//
                    () -> Arrays.ff.<Object, Object, RuntimeException> zip(null, null, defaultValue, null, null),
                    () -> Arrays.ff.<Object, Object, Object, RuntimeException> zip(null, null, null, defaultValue, null, null, null),
                    () -> Arrays.fff.<Object, Object, RuntimeException> zip(null, null, defaultValue, null, null),
                    () -> Arrays.fff.<Object, Object, Object, RuntimeException> zip(null, null, null, defaultValue, null, null, null));

            for (final Executable operation : operations) {
                final IllegalArgumentException failure = assertThrows(IllegalArgumentException.class, operation);
                assertEquals("'zipFunction' cannot be null", failure.getMessage());
            }
        }

        @Test
        void defaultedZipValidatesTypeInferenceInputsBeforeTheCallback() {
            final List<Executable> operations = List.of(//
                    () -> Arrays.ff.<Object, Object, RuntimeException> zip(null, null, null, null, null),
                    () -> Arrays.ff.<Object, Object, Object, RuntimeException> zip(null, null, null, null, null, null, null),
                    () -> Arrays.fff.<Object, Object, RuntimeException> zip(null, null, null, null, null),
                    () -> Arrays.fff.<Object, Object, Object, RuntimeException> zip(null, null, null, null, null, null, null));

            for (final Executable operation : operations) {
                final IllegalArgumentException failure = assertThrows(IllegalArgumentException.class, operation);
                assertEquals("Unable to infer target element type: both 'a' and 'defaultValueA' are null. Use the overload with targetElementType.",
                        failure.getMessage());
            }
        }

        @Test
        void doubleZip1D_defaultValues() throws Exception {
            double[] a = { 1, 2, 3, 4 };
            double[] b = { 5, 6 };
            double[] result = Arrays.zip(a, b, 0d, 10d, (x, y) -> x + y);
            assertArrayEquals(new double[] { 6, 8, 13, 14 }, result, 0.0);
        }

        @Test
        void doubleZip3D_binaryWithDefaultValues_firstShorter() throws Exception {
            double[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 } } };
            double[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 }, { 70, 80 } }, { { 90 } } };
            double[][][] result = Arrays.zip(a, b, 0d, 10d, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertArrayEquals(new double[] { 11, 22 }, result[0][0], 0.0);
            assertArrayEquals(new double[] { 33, 44 }, result[0][1], 0.0);
            assertArrayEquals(new double[] { 55, 66 }, result[1][0], 0.0);
            assertArrayEquals(new double[] { 70, 80 }, result[1][1], 0.0);
            assertArrayEquals(new double[] { 90 }, result[2][0], 0.0);
        }

        @Test
        void doubleZip3D_ternaryWithDefaultValues() throws Exception {
            double[][][] a = { { { 1, 2 } } };
            double[][][] b = { { { 11, 12 }, { 13, 14 } } };
            double[][][] c = { { { 21, 22 } } };
            double[][][] result = Arrays.zip(a, b, c, 0d, 10d, 20d, (x, y, z) -> x + y + z);
            assertEquals(1, result.length);
            assertEquals(2, result[0].length);
            assertArrayEquals(new double[] { 33, 36 }, result[0][0], 0.0);
            assertArrayEquals(new double[] { 33, 34 }, result[0][1], 0.0);
        }

        @Test
        void fF_zip_threeArrays() {
            Integer[][] a = { { 1, 2 } };
            Integer[][] b = { { 10, 20 } };
            Integer[][] c = { { 100, 200 } };
            Integer[][] result = Arrays.ff.zip(a, b, c, (x, y, z) -> x + y + z);
            assertArrayEquals(new Integer[] { 111, 222 }, result[0]);
        }

        @Test
        void fF_zip_truncatesToMin() throws Exception {
            Integer[][] a = { { 1, 2, 3 }, { 4, 5 } };
            Integer[][] b = { { 10, 20 }, { 30, 40, 50 }, { 60 } };
            Integer[][] sums = ff.zip(a, b, (x, y) -> x + y);
            assertEquals(2, sums.length);
            assertArrayEquals(new Integer[] { 11, 22 }, sums[0]);
            assertArrayEquals(new Integer[] { 34, 45 }, sums[1]);
        }

        @Test
        void fF_zip_twoArrays_differentTypes() {
            Integer[][] a = { { 1, 2 } };
            String[][] b = { { "A", "B" } };
            String[][] result = Arrays.ff.zip(a, b, (x, y) -> x + y, String.class);
            assertArrayEquals(new String[] { "1A", "2B" }, result[0]);
        }

        @Test
        void fF_zip_twoArrays_sameLength() {
            Integer[][] a = { { 1, 2 }, { 3, 4 } };
            Integer[][] b = { { 10, 20 }, { 30, 40 } };
            Integer[][] result = Arrays.ff.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new Integer[] { 11, 22 }, result[0]);
            assertArrayEquals(new Integer[] { 33, 44 }, result[1]);
        }

        @Test
        void fF_zip_twoArrays_withDefaults() {
            Integer[][] a = { { 1, 2 } };
            Integer[][] b = { { 10 } };
            Integer[][] result = Arrays.ff.zip(a, b, 0, 0, (x, y) -> x + y);
            assertArrayEquals(new Integer[] { 11, 2 }, result[0]);
        }

        @Test
        void fF_zip3_truncatesToMin() throws Exception {
            Integer[][] a = { { 1, 2 }, { 3, 4 } };
            Integer[][] b = { { 10, 20 }, { 30, 40 }, { 50 } };
            Integer[][] c = { { 100, 200 }, { 300, 400 } };
            Integer[][] sums = ff.zip(a, b, c, (x, y, z) -> x + y + z);
            assertEquals(2, sums.length);
            assertArrayEquals(new Integer[] { 111, 222 }, sums[0]);
            assertArrayEquals(new Integer[] { 333, 444 }, sums[1]);
        }

        @Test
        void fF_zip3WithDefaults_extendsToMax() throws Exception {
            Integer[][] a = { { 1 }, { 2, 3 } };
            Integer[][] b = { { 10, 20 } };
            Integer[][] c = { { 100 }, { 200, 300 }, { 400 } };
            Integer[][] sums = ff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
            assertEquals(3, sums.length);
            assertArrayEquals(new Integer[] { 111, 20 }, sums[0]);
            assertArrayEquals(new Integer[] { 202, 303 }, sums[1]);
            assertArrayEquals(new Integer[] { 400 }, sums[2]);
        }

        @Test
        void ff_zipWithDefaults_aNotNull_inferredFromArrayComponentType() {
            final Integer[][] a = { { 1, 2 } };
            final Integer[][] b = { { 10, 20, 30 } };
            // a.getClass().getComponentType().getComponentType() = Integer.class
            // initial target = Integer; both arrays have Integers; bi function returns Integer.
            final Integer[][] out = ff.zip(a, b, 0, 0, (x, y) -> x + y);
            assertEquals(Integer[][].class, out.getClass());
            assertArrayEquals(new Integer[] { 11, 22, 30 }, out[0]);
        }

        @Test
        void ff_zipWithDefaults_aNull_fallsBackToDefaultsClass() {
            final Integer[][] b = { { 10, 20 } };
            // a == null -> use defaultValueA.getClass() = Integer.class
            final Integer[][] out = ff.zip((Integer[][]) null, b, 7, 0, (x, y) -> x + y);
            assertEquals(Integer[][].class, out.getClass());
            assertArrayEquals(new Integer[] { 17, 27 }, out[0]);
        }

        @Test
        void fF_zipWithDefaults_aShorter() {
            Integer[][] aa = { { 1 } };
            Integer[][] bb = { { 10, 20 }, { 30, 40 } };
            Integer[][] result = ff.zip(aa, bb, 5, 0, (x, y) -> x + y);
            assertEquals(2, result.length);
            // Row 0: pair (1, 10), then aa[0] short -> (defaultA=5, 20) -> 25
            assertArrayEquals(new Integer[] { 11, 25 }, result[0]);
            // Row 1: aa[1] doesn't exist -> use defaultA=5 for all elements
            assertArrayEquals(new Integer[] { 35, 45 }, result[1]);
        }

        @Test
        void ff_zipWithDefaults_bothNullThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> ff.zip((Integer[][]) null, (Integer[][]) null, (Integer) null, (Integer) null, (x, y) -> (x == null ? 0 : x) + (y == null ? 0 : y)));
        }

        @Test
        void fF_zipWithDefaults_extendsToMax() throws Exception {
            Integer[][] a = { { 1, 2 }, { 3 } };
            Integer[][] b = { { 10 }, { 30, 40 }, { 100 } };
            Integer[][] sums = ff.zip(a, b, 0, 0, (x, y) -> x + y);
            assertEquals(3, sums.length);
            assertArrayEquals(new Integer[] { 11, 2 }, sums[0]);
            assertArrayEquals(new Integer[] { 33, 40 }, sums[1]);
            assertArrayEquals(new Integer[] { 100 }, sums[2]);
        }

        @Test
        void fF_zipWithDefaults_unequalRowCount_usesDefaults() {
            Integer[][] aa = { { 1, 2 } };
            Integer[][] bb = { { 10 }, { 20, 30 } };
            Integer[][] result = ff.zip(aa, bb, 0, 0, (x, y) -> x + y);
            assertEquals(2, result.length);
            // Row 0: pair (1,10), (2, defaultB=0) -> (11, 2)
            assertArrayEquals(new Integer[] { 11, 2 }, result[0]);
            // Row 1: aa is shorter -> use defaultA=0; pair (0,20), (0,30)
            assertArrayEquals(new Integer[] { 20, 30 }, result[1]);
        }

        @Test
        void fFF_zip_threeArrays() {
            Integer[][][] a = { { { 1, 2 } } };
            Integer[][][] b = { { { 10, 20 } } };
            Integer[][][] c = { { { 100, 200 } } };
            Integer[][][] result = Arrays.fff.zip(a, b, c, (x, y, z) -> x + y + z);
            assertArrayEquals(new Integer[] { 111, 222 }, result[0][0]);
        }

        @Test
        void fFF_zip_twoArrays() {
            Integer[][][] a = { { { 1, 2 } } };
            Integer[][][] b = { { { 10, 20 } } };
            Integer[][][] result = Arrays.fff.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new Integer[] { 11, 22 }, result[0][0]);
        }

        @Test
        void fFF_zip_twoArrays_differentTypes() {
            Integer[][][] a = { { { 1, 2 } } };
            String[][][] b = { { { "A", "B" } } };
            String[][][] result = Arrays.fff.zip(a, b, (x, y) -> x + y, String.class);
            assertArrayEquals(new String[] { "1A", "2B" }, result[0][0]);
        }

        @Test
        void fFF_zip_twoArrays_withDefaults() {
            Integer[][][] a = { { { 1, 2 } } };
            Integer[][][] b = { { { 10 } } };
            Integer[][][] result = Arrays.fff.zip(a, b, 0, 0, (x, y) -> x + y);
            assertArrayEquals(new Integer[] { 11, 2 }, result[0][0]);
        }

        @Test
        void fFF_zip2_truncatesToMin() throws Exception {
            Integer[][][] a = { { { 1, 2 } }, { { 3, 4 } } };
            Integer[][][] b = { { { 10, 20 } }, { { 30, 40 } }, { { 50, 60 } } };
            Integer[][][] sums = fff.zip(a, b, (x, y) -> x + y);
            assertEquals(2, sums.length);
            assertArrayEquals(new Integer[] { 11, 22 }, sums[0][0]);
            assertArrayEquals(new Integer[] { 33, 44 }, sums[1][0]);
        }

        @Test
        void fFF_zip2WithDefaults_extendsToMax() throws Exception {
            Integer[][][] a = { { { 1, 2 } } };
            Integer[][][] b = { { { 10 } }, { { 20, 30 } } };
            Integer[][][] result = fff.zip(a, b, 0, 0, (x, y) -> x + y);
            assertEquals(2, result.length);
            assertArrayEquals(new Integer[] { 11, 2 }, result[0][0]);
            assertArrayEquals(new Integer[] { 20, 30 }, result[1][0]);
        }

        @Test
        void fFF_zip3_truncatesToMin() throws Exception {
            Integer[][][] a = { { { 1, 2 } }, { { 3, 4 } } };
            Integer[][][] b = { { { 10, 20 } }, { { 30, 40 } } };
            Integer[][][] c = { { { 100, 200 } }, { { 300, 400 } }, { { 500, 600 } } };
            Integer[][][] sums = fff.zip(a, b, c, (x, y, z) -> x + y + z);
            assertEquals(2, sums.length);
            assertArrayEquals(new Integer[] { 111, 222 }, sums[0][0]);
            assertArrayEquals(new Integer[] { 333, 444 }, sums[1][0]);
        }

        @Test
        void fFF_zip3WithDefaults_extendsToMax() throws Exception {
            Integer[][][] a = { { { 1 } } };
            Integer[][][] b = { { { 10, 20 } } };
            Integer[][][] c = { { {} }, { { 100 } } };
            Integer[][][] result = fff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
            assertEquals(2, result.length);
            assertArrayEquals(new Integer[] { 11, 20 }, result[0][0]);
            assertArrayEquals(new Integer[] { 100 }, result[1][0]);
        }

        @Test
        void fFF_zipThreeArraysWithDefaults() {
            Integer[][][] aa = { { { 1 } } };
            Integer[][][] bb = { { { 10, 20 } } };
            Integer[][][] cc = { { {} }, { { 100 } } };
            Integer[][][] result = fff.zip(aa, bb, cc, 0, 0, 0, (x, y, z) -> x + y + z);
            // Result should have outer length 2 (max of 1, 1, 2)
            assertEquals(2, result.length);
            // i=0: aa has 1, bb has [10,20], cc[0] = [[]] -> the inner row is empty
            //      so column counts: aa[0][0]=[1], bb[0][0]=[10,20], cc[0][0]=[]
            //      max=2, so zip across max 2 cols: (1,10,defaultC=0)=11, (defaultA=0,20,defaultC=0)=20
            assertArrayEquals(new Integer[] { 11, 20 }, result[0][0]);
            // i=1: aa missing, bb missing, cc[1]=[[100]]; defaults for missing
            assertArrayEquals(new Integer[] { 100 }, result[1][0]);
        }

        @Test
        void fff_zipWithDefaults_aNull_fallsBack() {
            final Integer[][][] b = { { { 10, 20 } } };
            final Integer[][][] out = fff.zip((Integer[][][]) null, b, 5, 0, (x, y) -> x + y);
            assertEquals(Integer[][][].class, out.getClass());
            assertArrayEquals(new Integer[] { 15, 25 }, out[0][0]);
        }

        @Test
        void fff_zipWithDefaults_bothNullThrows() {
            assertThrows(IllegalArgumentException.class, () -> fff.zip((Integer[][][]) null, (Integer[][][]) null, (Integer) null, (Integer) null,
                    (x, y) -> (x == null ? 0 : x) + (y == null ? 0 : y)));
        }

        @Test
        void fFF_zipWithDefaults_unequalLengths() {
            Integer[][][] aa = { { { 1, 2 } } };
            Integer[][][] bb = { { { 10 } }, { { 20, 30 } } };
            Integer[][][] result = fff.zip(aa, bb, 0, 0, (x, y) -> x + y);
            assertEquals(2, result.length);
            // Top-level i=0: zip 2D rows
            assertArrayEquals(new Integer[] { 11, 2 }, result[0][0]);
            // Top-level i=1: aa is too short -> defaultA used everywhere
            assertArrayEquals(new Integer[] { 20, 30 }, result[1][0]);
        }

        @Test
        void fffTernaryZipInferredTypeRejectsRuntimeComponentWidening() throws Exception {
            Number[][][] left = new Integer[][][] { { { 1, 2 } } };
            Number[][][] second = new Number[][][] { { { 10L, 20L } } };
            Number[][][] third = new Number[][][] { { { 100L, 200L } } };

            assertThrows(IllegalArgumentException.class, () -> fff.zip(left, second, third, (x, y, z) -> z));

            Number[][][] out = fff.zip(left, second, third, (x, y, z) -> z, Number.class);

            assertEquals(100L, out[0][0][0]);
            assertEquals(200L, out[0][0][1]);
            assertEquals(Number[][][].class, out.getClass());
        }

        @Test
        void fffTernaryZipWithDefaultsInferredTypeRejectsRuntimeComponentWidening() throws Exception {
            Number[][][] left = new Integer[][][] { { { 1 } } };
            Number[][][] second = new Number[][][] { { { 10L } }, { { 30L } } };
            Number[][][] third = new Number[][][] { { { 100L } } };

            assertThrows(IllegalArgumentException.class, () -> fff.zip(left, second, third, 0, 0L, 0L, (x, y, z) -> y));

            Number[][][] out = fff.zip(left, second, third, 0, 0L, 0L, (x, y, z) -> y, Number.class);

            assertEquals(10L, out[0][0][0]);
            assertEquals(30L, out[1][0][0]);
            assertEquals(Number[][][].class, out.getClass());
        }

        @Test
        void fffZipInferredTypeRejectsAmbiguousInterfaceTarget() throws Exception {
            java.io.Serializable[][][] left = new Integer[][][] { { { 1 } } };
            String[][][] right = { { { "x" } } };

            assertThrows(IllegalArgumentException.class, () -> fff.zip(left, right, (x, y) -> y));

            java.io.Serializable[][][] out = fff.zip(left, right, (x, y) -> y, java.io.Serializable.class);

            assertEquals("x", out[0][0][0]);
            assertEquals(java.io.Serializable[][][].class, out.getClass());
        }

        @Test
        void fffZipTypeInferenceRejectsNullLeftArray() {
            Integer[][][] right = { { { 1, 2 } } };
            Integer[][][] third = { { { 3, 4 } } };

            assertThrows(IllegalArgumentException.class, () -> fff.zip((Integer[][][]) null, right, Integer::sum));
            assertThrows(IllegalArgumentException.class, () -> fff.zip((Integer[][][]) null, right, third, (a, b, c) -> a + b + c));
        }

        @Test
        void fffZipWithDefaults_resultElementTypeComesFromAOrElseFromDefaultValueA() throws Exception {
            final Number[][][] b = { { { 10L, 20L } } };

            final Number[][][] fromA = fff.zip(new Number[][][] { { { 1 } } }, b, 0, 0L, (x, y) -> y);
            assertEquals(Number[][][].class, fromA.getClass());
            assertEquals(20L, fromA[0][0][1]);

            final Number[][][] fromDefault = fff.zip((Number[][][]) null, b, (Number) 0, (Number) 0L, (x, y) -> x);
            assertEquals(Integer[][][].class, fromDefault.getClass());

            final Number[][][] fromDefault3 = fff.zip((Number[][][]) null, b, b, (Number) 0, (Number) 0L, (Number) 0L, (x, y, z) -> x);
            assertEquals(Integer[][][].class, fromDefault3.getClass());

            final Number[][][] fromA3 = fff.zip(new Number[][][] { { { 1 } } }, b, b, 0, 0L, 0L, (x, y, z) -> z);
            assertEquals(Number[][][].class, fromA3.getClass());
        }

        @Test
        void fffZipWithDefaultsInferredTypeRejectsRuntimeComponentWidening() throws Exception {
            Number[][][] left = new Integer[][][] { { { 1 } } };
            Number[][][] right = new Number[][][] { { { 10L, 20L } } };

            assertThrows(IllegalArgumentException.class, () -> fff.zip(left, right, 0, 0L, (x, y) -> y));

            Number[][][] out = fff.zip(left, right, 0, 0L, (x, y) -> y, Number.class);

            assertEquals(10L, out[0][0][0]);
            assertEquals(20L, out[0][0][1]);
            assertEquals(Number[][][].class, out.getClass());
        }

        @Test
        void fffZipWithDefaultsRejectsMissingTypeHints() {
            Integer[][][] right = { { { 1 } } };

            assertThrows(IllegalArgumentException.class, () -> fff.zip(null, right, null, 0, Integer::sum));
        }

        @Test
        void fffZipWithDefaultsSupportsNullLeftInput() throws Exception {
            Integer[][][] right = { { { 1, 2 } } };

            Integer[][][] result = fff.zip(null, right, 0, 0, Integer::sum);

            assertArrayEquals(new Integer[][][] { { { 1, 2 } } }, result);
        }

        @Test
        void ffTernaryZipInferredTypeRejectsRuntimeComponentWidening() throws Exception {
            Number[][] left = new Integer[][] { { 1, 2 } };
            Number[][] second = new Number[][] { { 10L, 20L } };
            Number[][] third = new Number[][] { { 100L, 200L } };

            assertThrows(IllegalArgumentException.class, () -> ff.zip(left, second, third, (x, y, z) -> z));

            Number[][] out = ff.zip(left, second, third, (x, y, z) -> z, Number.class);

            assertEquals(100L, out[0][0]);
            assertEquals(200L, out[0][1]);
            assertEquals(Number[][].class, out.getClass());
        }

        @Test
        void ffTernaryZipWithDefaultsInferredTypeRejectsRuntimeComponentWidening() throws Exception {
            Number[][] left = new Integer[][] { { 1 } };
            Number[][] second = new Number[][] { { 10L }, { 30L } };
            Number[][] third = new Number[][] { { 100L } };

            assertThrows(IllegalArgumentException.class, () -> ff.zip(left, second, third, 0, 0L, 0L, (x, y, z) -> y));

            Number[][] out = ff.zip(left, second, third, 0, 0L, 0L, (x, y, z) -> y, Number.class);

            assertEquals(10L, out[0][0]);
            assertEquals(30L, out[1][0]);
            assertEquals(Number[][].class, out.getClass());
        }

        @Test
        void ffZipInferredTypeRejectsRuntimeComponentWidening() throws Exception {
            Number[][] left = new Integer[][] { { 1, 2 } };
            Number[][] right = new Number[][] { { 10L, 20L } };

            assertThrows(IllegalArgumentException.class, () -> ff.zip(left, right, (x, y) -> y));

            Number[][] out = ff.zip(left, right, (x, y) -> y, Number.class);

            assertEquals(10L, out[0][0]);
            assertEquals(20L, out[0][1]);
            assertEquals(Number[][].class, out.getClass());
        }

        @Test
        void ffZipTypeInferenceRejectsNullLeftArray() {
            Integer[][] right = { { 1, 2 } };
            Integer[][] third = { { 3, 4 } };

            assertThrows(IllegalArgumentException.class, () -> ff.zip((Integer[][]) null, right, Integer::sum));
            assertThrows(IllegalArgumentException.class, () -> ff.zip((Integer[][]) null, right, third, (a, b, c) -> a + b + c));
        }

        @Test
        void ffZipWithDefaults_resultElementTypeComesFromAOrElseFromDefaultValueA() throws Exception {
            final Number[][] b = { { 10L, 20L } };

            final Number[][] fromA = ff.zip(new Number[][] { { 1 } }, b, 0, 0L, (x, y) -> y);
            assertEquals(Number[][].class, fromA.getClass());
            assertEquals(20L, fromA[0][1]);

            final Number[][] fromDefault = ff.zip((Number[][]) null, b, (Number) 0, (Number) 0L, (x, y) -> x);
            assertEquals(Integer[][].class, fromDefault.getClass());

            final Number[][] fromDefault3 = ff.zip((Number[][]) null, b, b, (Number) 0, (Number) 0L, (Number) 0L, (x, y, z) -> x);
            assertEquals(Integer[][].class, fromDefault3.getClass());

            final Number[][] fromA3 = ff.zip(new Number[][] { { 1 } }, b, b, 0, 0L, 0L, (x, y, z) -> z);
            assertEquals(Number[][].class, fromA3.getClass());
        }

        @Test
        void ffZipWithDefaultsInferredTypeRejectsRuntimeComponentWidening() throws Exception {
            Number[][] right = new Number[][] { { 10L, 20L } };

            assertThrows(IllegalArgumentException.class, () -> ff.zip((Number[][]) null, right, (Number) 0, (Number) 0L, (x, y) -> y));

            Number[][] out = ff.zip((Number[][]) null, right, (Number) 0, (Number) 0L, (x, y) -> y, Number.class);

            assertEquals(10L, out[0][0]);
            assertEquals(20L, out[0][1]);
            assertEquals(Number[][].class, out.getClass());
        }

        @Test
        void floatZip1D_defaultValues() throws Exception {
            // Doc: zip({1,2,3,4}, {5,6}, 0, 10, +) => {6,8,13,14}
            float[] a = { 1, 2, 3, 4 };
            float[] b = { 5, 6 };
            float[] result = Arrays.zip(a, b, 0f, 10f, (x, y) -> x + y);
            assertArrayEquals(new float[] { 6, 8, 13, 14 }, result, 0.0f);
        }

        @Test
        void floatZip3D_binaryWithDefaultValues_firstShorter() throws Exception {
            float[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 } } };
            float[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 }, { 70, 80 } }, { { 90 } } };
            float[][][] result = Arrays.zip(a, b, 0f, 10f, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertArrayEquals(new float[] { 11, 22 }, result[0][0], 0.0f);
            assertArrayEquals(new float[] { 33, 44 }, result[0][1], 0.0f);
            assertArrayEquals(new float[] { 55, 66 }, result[1][0], 0.0f);
            assertArrayEquals(new float[] { 70, 80 }, result[1][1], 0.0f);
            assertArrayEquals(new float[] { 90 }, result[2][0], 0.0f);
        }

        @Test
        void floatZip3D_ternaryWithDefaultValues() throws Exception {
            float[][][] a = { { { 1, 2 } } };
            float[][][] b = { { { 11, 12 }, { 13, 14 } } };
            float[][][] c = { { { 21, 22 } } };
            float[][][] result = Arrays.zip(a, b, c, 0f, 10f, 20f, (x, y, z) -> x + y + z);
            assertEquals(1, result.length);
            assertEquals(2, result[0].length);
            assertArrayEquals(new float[] { 33, 36 }, result[0][0], 0.0f);
            assertArrayEquals(new float[] { 33, 34 }, result[0][1], 0.0f);
        }

        @Test
        void intZipBinary() {
            // zip({1,2,3,4}, {5,6,7}, (x,y) -> x+y) => {6,8,10}
            int[] a = { 1, 2, 3, 4 };
            int[] b = { 5, 6, 7 };
            int[] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new int[] { 6, 8, 10 }, result);
        }

        @Test
        void intZipBinaryWithDefaults() {
            // zip({1,2,3,4}, {5,6}, 0, 10, (x,y) -> x+y) => {6,8,13,14}
            int[] a = { 1, 2, 3, 4 };
            int[] b = { 5, 6 };
            int[] result = Arrays.zip(a, b, 0, 10, (x, y) -> x + y);
            assertArrayEquals(new int[] { 6, 8, 13, 14 }, result);
        }

        @Test
        void longZip2D_ternaryWithDefaultValues() throws Exception {
            // Doc example at long Arrays.zip(long[][],long[][],long[][],long,long,long,...)
            long[][] a = { { 1, 2 }, { 3, 4, 5 } };
            long[][] b = { { 5, 6, 7 }, { 8, 9 }, { 10 } };
            long[][] c = { { 10, 11 } };
            long[][] result = Arrays.zip(a, b, c, 0L, 10L, 20L, (x, y, z) -> x + y + z);
            assertEquals(3, result.length);
            assertArrayEquals(new long[] { 16, 19, 27 }, result[0]);
            assertArrayEquals(new long[] { 31, 33, 35 }, result[1]);
            assertArrayEquals(new long[] { 30 }, result[2]);
        }

        @Test
        void longZip3D_binaryWithDefaultValues_firstShorter() throws Exception {
            // Doc example: a shorter than b at outer level, ensures defaultValueA used for missing 2D blocks
            long[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 } } };
            long[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 }, { 70, 80 } }, { { 90 } } };
            long[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertArrayEquals(new long[] { 11, 22 }, result[0][0]);
            assertArrayEquals(new long[] { 33, 44 }, result[0][1]);
            assertArrayEquals(new long[] { 55, 66 }, result[1][0]);
            assertArrayEquals(new long[] { 70, 80 }, result[1][1]);
            assertArrayEquals(new long[] { 90 }, result[2][0]);
        }

        @Test
        void longZip3D_ternaryWithDefaultValues() throws Exception {
            // Doc example at long Arrays.zip(long[][][],long[][][],long[][][],long,long,long,...)
            long[][][] a = { { { 1, 2 } } };
            long[][][] b = { { { 11, 12 }, { 13, 14 } } };
            long[][][] c = { { { 21, 22 } } };
            long[][][] result = Arrays.zip(a, b, c, 0L, 10L, 20L, (x, y, z) -> x + y + z);
            assertEquals(1, result.length);
            assertEquals(2, result[0].length);
            assertArrayEquals(new long[] { 33, 36 }, result[0][0]);
            assertArrayEquals(new long[] { 33, 34 }, result[0][1]);
        }

        @Test
        void property_zip1D_lengthAndElementwise_noMutation() {
            final java.util.Random r = new java.util.Random(42L);

            for (int it = 0; it < ITERATIONS; it++) {
                final int lenA = r.nextInt(50);
                final int lenB = r.nextInt(50);
                final int[] a = randomIntArray(r, lenA);
                final int[] b = randomIntArray(r, lenB);
                final int[] snapA = a.clone();
                final int[] snapB = b.clone();

                final int[] zipped = Arrays.zip(a, b, (x, y) -> x + y);

                assertArrayEquals(snapA, a, "zip must not mutate a");
                assertArrayEquals(snapB, b, "zip must not mutate b");
                assertEquals(Math.min(lenA, lenB), zipped.length, "zip length is min of inputs");
                for (int i = 0; i < zipped.length; i++) {
                    assertEquals(a[i] + b[i], zipped[i], "zip element-wise at i=" + i);
                }
            }
        }

        @Test
        void property_zip2D_elementwise() {
            final int[][] a = { { 1, 2, 3 }, { 4 }, { 5, 6 } };
            final int[][] b = { { 10, 20 }, { 30, 40 }, { 50, 60, 70 } };

            final int[][] z = Arrays.zip(a, b, (x, y) -> x * y);

            assertEquals(3, z.length);
            assertArrayEquals(new int[] { 10, 40 }, z[0]); // min(3,2)=2 cols
            assertArrayEquals(new int[] { 120 }, z[1]); // min(1,2)=1 col
            assertArrayEquals(new int[] { 250, 360 }, z[2]); // min(2,3)=2 cols
        }

        @Test
        void zip() {
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
                Arrays.println(c);
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
        void zip_1D_boolean_threeArrays() {
            boolean[] a = { true, false };
            boolean[] b = { false, true };
            boolean[] c = { true, true };
            boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> (x || y) && z);
            assertEquals(2, result.length);
            assertTrue(result[0]);
            assertTrue(result[1]);
        }

        @Test
        void zip_1D_boolean_threeArrays_withDefaults() {
            boolean[] a = { true, false };
            boolean[] b = { false, true, true };
            boolean[] c = { true };
            boolean[] result = Arrays.zip(a, b, c, false, false, true, (x, y, z) -> x || y || z);
            assertEquals(3, result.length);
            assertTrue(result[0]);
            assertTrue(result[1]);
            assertTrue(result[2]);
        }

        @Test
        void zip_1D_boolean_twoArrays() {
            boolean[] a = { true, false, true };
            boolean[] b = { false, false, true };
            boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);
            assertEquals(3, result.length);
            assertFalse(result[0]);
            assertFalse(result[1]);
            assertTrue(result[2]);
        }

        @Test
        void zip_1D_boolean_twoArrays_withDefaults() {
            boolean[] a = { true, false };
            boolean[] b = { false, false, true };
            boolean[] result = Arrays.zip(a, b, true, false, (x, y) -> x || y);
            assertEquals(3, result.length);
            assertTrue(result[0]);
            assertFalse(result[1]);
            assertTrue(result[2]);
        }

        @Test
        void zip_1D_bothNull_returnsEmpty_boolean() {
            final boolean[] r = Arrays.zip((boolean[]) null, (boolean[]) null, (x, y) -> x && y);
            assertArrayEquals(new boolean[0], r);
        }

        @Test
        void zip_1D_bothNull_returnsEmpty_int() {
            final int[] r = Arrays.zip((int[]) null, (int[]) null, (x, y) -> x + y);
            assertArrayEquals(new int[0], r);
        }

        @Test
        void zip_1D_byte_threeArrays() {
            byte[] a = { 1, 2 };
            byte[] b = { 3, 4 };
            byte[] c = { 5, 6 };
            byte[] result = Arrays.zip(a, b, c, (x, y, z) -> (byte) (x + y + z));
            assertEquals(2, result.length);
            assertEquals(9, result[0]);
            assertEquals(12, result[1]);
        }

        @Test
        void zip_1D_byte_twoArrays() {
            byte[] a = { 1, 2, 3 };
            byte[] b = { 4, 5, 6 };
            byte[] result = Arrays.zip(a, b, (x, y) -> (byte) (x + y));
            assertEquals(3, result.length);
            assertEquals(5, result[0]);
            assertEquals(9, result[2]);
        }

        @Test
        void zip_1D_byte_twoArrays_withDefaults() {
            byte[] a = { 1, 2 };
            byte[] b = { 3, 4, 5 };
            byte[] result = Arrays.zip(a, b, (byte) 0, (byte) 0, (x, y) -> (byte) (x + y));
            assertEquals(3, result.length);
            assertEquals(4, result[0]);
            assertEquals(5, result[2]);
        }

        @Test
        void zip_1D_char_threeArrays() {
            char[] a = { 'a', 'b' };
            char[] b = { 'c', 'd' };
            char[] c = { 'e', 'f' };
            char[] result = Arrays.zip(a, b, c, (x, y, z) -> x);
            assertEquals(2, result.length);
            assertEquals('a', result[0]);
        }

        @Test
        void zip_1D_char_twoArrays() {
            char[] a = { 'a', 'b', 'c' };
            char[] b = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, (x, y) -> x);
            assertEquals(3, result.length);
            assertEquals('a', result[0]);
            assertEquals('c', result[2]);
        }

        @Test
        void zip_1D_char_twoArrays_withDefaults() {
            char[] a = { 'a', 'b' };
            char[] b = { 'x', 'y', 'z' };
            char[] result = Arrays.zip(a, b, 'A', 'X', (x, y) -> x);
            assertEquals(3, result.length);
            assertEquals('a', result[0]);
            assertEquals('A', result[2]);
        }

        @Test
        void zip_1D_default_aIsShorter_double() {
            final double[] r = Arrays.zip(new double[0], new double[] { 1.0, 2.0 }, 10.0, 20.0, (x, y) -> x + y);
            assertArrayEquals(new double[] { 11.0, 12.0 }, r);
        }

        @Test
        void zip_1D_default_aIsShorter_int() {
            // a empty, b = {1,2,3}, default a=100, b=200 -> result uses 100 for missing a
            final int[] r = Arrays.zip(new int[0], new int[] { 1, 2, 3 }, 100, 200, (x, y) -> x + y);
            assertArrayEquals(new int[] { 101, 102, 103 }, r);
        }

        @Test
        void zip_1D_default_bIsShorter_int() {
            // a = {1}, b empty, default a=100, b=200 -> use 200 for missing b
            final int[] r = Arrays.zip(new int[] { 1 }, new int[0], 100, 200, (x, y) -> x + y);
            assertArrayEquals(new int[] { 201 }, r);
        }

        @Test
        void zip_1D_default_bIsShorter_long() {
            final long[] r = Arrays.zip(new long[] { 1L }, new long[0], 10L, 20L, (x, y) -> x + y);
            assertArrayEquals(new long[] { 21L }, r);
        }

        @Test
        void zip_1D_default_bothEmpty_returnsEmpty_int() {
            final int[] r = Arrays.zip(new int[0], new int[0], 100, 200, (x, y) -> x + y);
            assertArrayEquals(new int[0], r);
        }

        @Test
        void zip_1D_default_equalLength_int() {
            // Equal lengths: defaults not applied
            final int[] r = Arrays.zip(new int[] { 1, 2 }, new int[] { 3, 4 }, 100, 200, (x, y) -> x + y);
            assertArrayEquals(new int[] { 4, 6 }, r);
        }

        @Test
        void zip_1D_double_threeArrays() {
            double[] a = { 1.0, 2.0, 3.0 };
            double[] b = { 4.0, 5.0, 6.0 };
            double[] c = { 7.0, 8.0, 9.0 };
            double[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertEquals(3, result.length);
            assertEquals(12.0, result[0], 0.001);
            assertEquals(18.0, result[2], 0.001);
        }

        @Test
        void zip_1D_double_threeArrays_withDefaults() {
            double[] a = { 1.0, 2.0 };
            double[] b = { 3.0, 4.0, 5.0 };
            double[] c = { 6.0 };
            double[] result = Arrays.zip(a, b, c, 0.0, 0.0, 0.0, (x, y, z) -> x + y + z);
            assertEquals(3, result.length);
            assertEquals(10.0, result[0], 0.001);
            assertEquals(5.0, result[2], 0.001);
        }

        @Test
        void zip_1D_double_twoArrays() {
            double[] a = { 1.5, 2.5, 3.5 };
            double[] b = { 0.5, 1.5, 2.5 };
            double[] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertEquals(2.0, result[0], 0.001);
            assertEquals(6.0, result[2], 0.001);
        }

        @Test
        void zip_1D_double_twoArrays_withDefaults() {
            double[] a = { 1.0, 2.0 };
            double[] b = { 3.0, 4.0, 5.0 };
            double[] result = Arrays.zip(a, b, 0.0, 0.0, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertEquals(4.0, result[0], 0.001);
            assertEquals(5.0, result[2], 0.001);
        }

        @Test
        void zip_1D_float_threeArrays() {
            float[] a = { 1.0f, 2.0f };
            float[] b = { 3.0f, 4.0f };
            float[] c = { 5.0f, 6.0f };
            float[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertEquals(2, result.length);
            assertEquals(9.0f, result[0], 0.001f);
            assertEquals(12.0f, result[1], 0.001f);
        }

        @Test
        void zip_1D_float_twoArrays() {
            float[] a = { 1.5f, 2.5f, 3.5f };
            float[] b = { 0.5f, 1.5f, 2.5f };
            float[] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertEquals(2.0f, result[0], 0.001f);
            assertEquals(6.0f, result[2], 0.001f);
        }

        @Test
        void zip_1D_float_twoArrays_withDefaults() {
            float[] a = { 1.0f, 2.0f };
            float[] b = { 3.0f, 4.0f, 5.0f };
            float[] result = Arrays.zip(a, b, 0.0f, 0.0f, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertEquals(4.0f, result[0], 0.001f);
            assertEquals(5.0f, result[2], 0.001f);
        }

        @Test
        void zip_1D_int_threeArrays() {
            int[] a = { 1, 2, 3 };
            int[] b = { 4, 5, 6 };
            int[] c = { 7, 8, 9 };
            int[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertEquals(3, result.length);
            assertEquals(12, result[0]);
            assertEquals(18, result[2]);
        }

        @Test
        void zip_1D_int_threeArrays_withDefaults() {
            int[] a = { 1, 2 };
            int[] b = { 3, 4, 5 };
            int[] c = { 6 };
            int[] result = Arrays.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
            assertEquals(3, result.length);
            assertEquals(10, result[0]);
            assertEquals(5, result[2]);
        }

        @Test
        void zip_1D_int_twoArrays() {
            int[] a = { 1, 2, 3 };
            int[] b = { 4, 5, 6 };
            int[] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertEquals(5, result[0]);
            assertEquals(9, result[2]);
        }

        @Test
        void zip_1D_int_twoArrays_withDefaults() {
            int[] a = { 1, 2 };
            int[] b = { 3, 4, 5 };
            int[] result = Arrays.zip(a, b, 0, 0, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertEquals(4, result[0]);
            assertEquals(5, result[2]);
        }

        @Test
        void zip_1D_long_threeArrays() {
            long[] a = { 1L, 2L };
            long[] b = { 3L, 4L };
            long[] c = { 5L, 6L };
            long[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertEquals(2, result.length);
            assertEquals(9L, result[0]);
            assertEquals(12L, result[1]);
        }

        @Test
        void zip_1D_long_twoArrays() {
            long[] a = { 100L, 200L, 300L };
            long[] b = { 10L, 20L, 30L };
            long[] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertEquals(110L, result[0]);
            assertEquals(330L, result[2]);
        }

        @Test
        void zip_1D_long_twoArrays_withDefaults() {
            long[] a = { 100L, 200L };
            long[] b = { 10L, 20L, 30L };
            long[] result = Arrays.zip(a, b, 0L, 0L, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertEquals(110L, result[0]);
            assertEquals(30L, result[2]);
        }

        @Test
        void zip_1D_oneEmpty_returnsEmpty_int() {
            final int[] r = Arrays.zip(new int[0], new int[] { 1, 2, 3 }, (x, y) -> x + y);
            assertArrayEquals(new int[0], r);
        }

        @Test
        void zip_1D_short_threeArrays() {
            short[] a = { 10, 20 };
            short[] b = { 5, 10 };
            short[] c = { 1, 2 };
            short[] result = Arrays.zip(a, b, c, (x, y, z) -> (short) (x + y + z));
            assertEquals(2, result.length);
            assertEquals(16, result[0]);
        }

        @Test
        void zip_1D_short_twoArrays() {
            short[] a = { 10, 20, 30 };
            short[] b = { 5, 10, 15 };
            short[] result = Arrays.zip(a, b, (x, y) -> (short) (x + y));
            assertEquals(3, result.length);
            assertEquals(15, result[0]);
            assertEquals(45, result[2]);
        }

        @Test
        void zip_1D_short_twoArrays_withDefaults() {
            short[] a = { 10, 20 };
            short[] b = { 5, 10, 15 };
            short[] result = Arrays.zip(a, b, (short) 0, (short) 0, (x, y) -> (short) (x + y));
            assertEquals(3, result.length);
            assertEquals(15, result[0]);
            assertEquals(15, result[2]);
        }

        @Test
        void zip_1D_shorterFirst_returnsShorterLen_int() {
            final int[] r = Arrays.zip(new int[] { 1, 2 }, new int[] { 3, 4, 5 }, (x, y) -> x + y);
            assertArrayEquals(new int[] { 4, 6 }, r);
        }

        @Test
        void zip_1D_ternary_default_allEmpty_int() {
            final int[] r = Arrays.zip(new int[0], new int[0], new int[0], 1, 2, 3, (x, y, z) -> x + y + z);
            assertArrayEquals(new int[0], r);
        }

        @Test
        void zip_1D_ternary_default_int() {
            // a={1,2,3,4}, b={5,6}, c={8,9,10}; defaults 0,10,20
            final int[] r = Arrays.zip(new int[] { 1, 2, 3, 4 }, new int[] { 5, 6 }, new int[] { 8, 9, 10 }, 0, 10, 20, (x, y, z) -> x + y + z);
            // i=0: 1+5+8=14
            // i=1: 2+6+9=17
            // i=2: 3+10+10=23 (b missing, use 10)
            // i=3: 4+10+20=34 (b and c missing)
            assertArrayEquals(new int[] { 14, 17, 23, 34 }, r);
        }

        @Test
        void zip_2D_default_lenA_greater_than_lenB_boolean() {
            final boolean[][] a = { { true, false }, { false, true } };
            final boolean[][] b = { { true } };
            final boolean[][] r = Arrays.zip(a, b, false, true, (x, y) -> x || y);
            assertEquals(2, r.length);
            // row 0: lenA=2,lenB=1 -> {true||true, false||true} = {true, true}
            assertArrayEquals(new boolean[] { true, true }, r[0]);
            // row 1 default: zip(a[1]={false,true}, null, false, true) -> {false||true, true||true}
            assertArrayEquals(new boolean[] { true, true }, r[1]);
        }

        @Test
        void zip_2D_default_lenA_greater_than_lenB_int() {
            // lenA > lenB -> 'else if (lenB < maxLen)' branch
            final int[][] a = { { 1, 2, 3 }, { 4, 5 } };
            final int[][] b = { { 10, 20 } };
            final int[][] r = Arrays.zip(a, b, 100, 200, (x, y) -> x + y);
            assertEquals(2, r.length);
            // row 0: lenA=3,lenB=2 -> {11,22,203}
            assertArrayEquals(new int[] { 11, 22, 203 }, r[0]);
            // row 1 from default: zip(a[1]={4,5}, null, 100, 200, ...) -> {204, 205}
            assertArrayEquals(new int[] { 204, 205 }, r[1]);
        }

        @Test
        void zip_2D_default_lenA_less_than_lenB_boolean() {
            final boolean[][] a = { { true } };
            final boolean[][] b = { { false, true }, { false, true, false } };
            final boolean[][] r = Arrays.zip(a, b, false, true, (x, y) -> x || y);
            assertEquals(2, r.length);
            // row 0: lenA=1, lenB=2, max=2 -> {true||false, false||true} = {true, true}
            assertArrayEquals(new boolean[] { true, true }, r[0]);
            // row 1 default: zip(null, b[1], false, true) -> {false||false, false||true, false||false} = {false,true,false}
            assertArrayEquals(new boolean[] { false, true, false }, r[1]);
        }

        @Test
        void zip_2D_default_lenA_less_than_lenB_int() {
            // lenA < lenB -> 'if (lenA < maxLen)' branch
            final int[][] a = { { 1, 2 } };
            final int[][] b = { { 3, 4, 5 }, { 8, 9 } };
            final int[][] r = Arrays.zip(a, b, 100, 200, (x, y) -> x + y);
            assertEquals(2, r.length);
            // row 0 inner: lenA=2,lenB=3, max=3 -> {4, 6, 105}
            assertArrayEquals(new int[] { 4, 6, 105 }, r[0]);
            // row 1 from default: zip(null, b[1], 100, 200, ...) -> {108, 109}
            assertArrayEquals(new int[] { 108, 109 }, r[1]);
        }

        @Test
        void zip_2D_default_lenAEqualsLenB_int() {
            // lenA == lenB == maxLen -> neither branch taken
            final int[][] a = { { 1, 2 }, { 3, 4 } };
            final int[][] b = { { 10, 20 }, { 30, 40 } };
            final int[][] r = Arrays.zip(a, b, 100, 200, (x, y) -> x + y);
            assertEquals(2, r.length);
            assertArrayEquals(new int[] { 11, 22 }, r[0]);
            assertArrayEquals(new int[] { 33, 44 }, r[1]);
        }

        @Test
        void zip_2D_double_twoArrays() {
            double[][] a = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            double[][] b = { { 0.5, 1.5 }, { 2.5, 3.5 } };
            double[][] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(2, result.length);
            assertEquals(2.0, result[0][0], 0.001);
            assertEquals(8.0, result[1][1], 0.001);
        }

        @Test
        void zip_2D_int_twoArrays() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            int[][] b = { { 5, 6 }, { 7, 8 } };
            int[][] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(2, result.length);
            assertEquals(6, result[0][0]);
            assertEquals(12, result[1][1]);
        }

        @Test
        void zip_2D_outerShorter_innerEqualLen_int() {
            final int[][] a = { { 1, 2 } };
            final int[][] b = { { 3, 4 }, { 5, 6 } };
            final int[][] r = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(1, r.length);
            assertArrayEquals(new int[] { 4, 6 }, r[0]);
        }

        @Test
        void zip_2D_ternary_default_assortedLengths_int() {
            // Different outer lengths AND different inner lengths for the 3 rows.
            // The inner zip uses each row's own max-length, NOT a global outer-aligned length.
            final int[][] a = { { 1, 2 } };
            final int[][] b = { { 10, 20 }, { 30 } };
            final int[][] c = { { 100, 101, 102 }, { 200, 300, 400 }, { 500 } };
            final int[][] r = Arrays.zip(a, b, c, 1000, 2000, 3000, (x, y, z) -> x + y + z);
            // outer max = 3
            assertEquals(3, r.length);
            // row 0: a={1,2}, b={10,20}, c={100,101,102}, inner maxLen=3
            //   i=0: 1+10+100 = 111
            //   i=1: 2+20+101 = 123
            //   i=2: 1000+2000+102 = 3102 (a and b missing)
            assertArrayEquals(new int[] { 111, 123, 3102 }, r[0]);
            // row 1: a=null (outer missing), b={30}, c={200,300,400}, inner maxLen=3
            //   i=0: 1000+30+200 = 1230
            //   i=1: 1000+2000+300 = 3300
            //   i=2: 1000+2000+400 = 3400
            assertArrayEquals(new int[] { 1230, 3300, 3400 }, r[1]);
            // row 2: a=null, b=null, c={500}, inner maxLen=1
            //   i=0: 1000+2000+500 = 3500
            assertArrayEquals(new int[] { 3500 }, r[2]);
        }

        @Test
        void zip_3D_default_lenA_greater_than_lenB_int() {
            final int[][][] a = { { { 1, 2 } }, { { 5, 6 } } };
            final int[][][] b = { { { 3, 4 } } };
            final int[][][] r = Arrays.zip(a, b, 100, 200, (x, y) -> x + y);
            assertEquals(2, r.length);
            assertArrayEquals(new int[] { 4, 6 }, r[0][0]);
            // r[1] from zip(a[1], null, ...): inner zip({5,6}, null, 100, 200) -> {205, 206}
            assertArrayEquals(new int[] { 205, 206 }, r[1][0]);
        }

        @Test
        void zip_3D_default_lenA_less_than_lenB_int() {
            final int[][][] a = { { { 1, 2 } } };
            final int[][][] b = { { { 3, 4 } }, { { 5, 6 } } };
            final int[][][] r = Arrays.zip(a, b, 100, 200, (x, y) -> x + y);
            assertEquals(2, r.length);
            assertArrayEquals(new int[] { 4, 6 }, r[0][0]);
            // r[1] comes from zip(null, b[1], ...) -> 2D: lenA=0,lenB=1 -> row 0: zip(null, {5,6}, 100, 200) -> {105, 106}
            assertArrayEquals(new int[] { 105, 106 }, r[1][0]);
        }

        @Test
        void zip_3D_double_twoArrays() {
            double[][][] a = { { { 1.5, 2.5 } } };
            double[][][] b = { { { 0.5, 1.5 } } };
            double[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(1, result.length);
            assertEquals(2.0, result[0][0][0], 0.001);
            assertEquals(4.0, result[0][0][1], 0.001);
        }

        @Test
        void zip_3D_int_twoArrays() {
            int[][][] a = { { { 1, 2 } } };
            int[][][] b = { { { 3, 4 } } };
            int[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(1, result.length);
            assertEquals(4, result[0][0][0]);
            assertEquals(6, result[0][0][1]);
        }

        @Test
        void zip_3D_ternary_default_assortedLengths_int() {
            final int[][][] a = { { { 1 } } };
            final int[][][] b = { { { 2 } }, { { 3 } } };
            final int[][][] c = { { { 4 } }, { { 5 } }, { { 6 } } };
            final int[][][] r = Arrays.zip(a, b, c, 10, 20, 30, (x, y, z) -> x + y + z);
            assertEquals(3, r.length);
            // r[0]: a={{1}}, b={{2}}, c={{4}} -> 1+2+4 = 7
            assertArrayEquals(new int[] { 7 }, r[0][0]);
            // r[1]: a=null, b={{3}}, c={{5}} -> 10+3+5 = 18
            assertArrayEquals(new int[] { 18 }, r[1][0]);
            // r[2]: a=null, b=null, c={{6}} -> 10+20+6 = 36
            assertArrayEquals(new int[] { 36 }, r[2][0]);
        }

        @Test
        void zip_boolean_1D_differentLength() {
            boolean[] a = { true, false };
            boolean[] b = { false, false, true };
            boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);
            assertArrayEquals(new boolean[] { true, false, true }, result);
        }

        @Test
        void zip_boolean_1D_threeArrays() {
            boolean[] a = { true, false };
            boolean[] b = { false, true };
            boolean[] c = { true, true };
            boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> (x || y) && z);
            assertArrayEquals(new boolean[] { true, true }, result);
        }

        @Test
        void zip_boolean_2D_sameLength() {
            boolean[][] a = { { true, false }, { true, true } };
            boolean[][] b = { { false, false }, { true, false } };
            boolean[][] result = Arrays.zip(a, b, (x, y) -> x && y);
            assertEquals(2, result.length);
            assertArrayEquals(new boolean[] { false, false }, result[0]);
            assertArrayEquals(new boolean[] { true, false }, result[1]);
        }

        @Test
        void zip_boolean_3D_valid() {
            boolean[][][] a = { { { true } } };
            boolean[][][] b = { { { false } } };
            boolean[][][] result = Arrays.zip(a, b, (x, y) -> x || y);
            assertTrue(result[0][0][0]);
        }

        @Test
        void zip_boolean1D_binaryOp_withDefaults() {
            boolean[] a = { true, false };
            boolean[] b = { false, false, true };
            boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

            assertEquals(3, result.length);
            assertTrue(result[0]);
            assertFalse(result[1]);
            assertTrue(result[2]);
        }

        @Test
        void zip_boolean1D_ternaryOp_withDefaults() {
            boolean[] a = { true };
            boolean[] b = { false, true };
            boolean[] c = { true, true };
            boolean[] result = Arrays.zip(a, b, c, false, false, true, (x, y, z) -> (x || y) && z);

            assertEquals(2, result.length);
            assertTrue(result[0]);
            assertTrue(result[1]);
        }

        @Test
        void zip_boolean2D_binaryOp() {
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
        void zip_boolean2D_ternaryOp() {
            boolean[][] a = { { true, false } };
            boolean[][] b = { { false, true } };
            boolean[][] c = { { true, true } };
            boolean[][] result = Arrays.zip(a, b, c, (x, y, z) -> (x || y) && z);

            assertEquals(1, result.length);
            assertTrue(result[0][0]);
            assertTrue(result[0][1]);
        }

        @Test
        void zip_boolean2DWithDefaults_onlyTheMissingSideUsesItsDefault() {
            final boolean[][] a = { { true, false } };
            final boolean[][] b = { { false }, { true, false } };

            // row 1: a has no sub-array -> a's default fills every position, b's actual values are kept
            final boolean[][] keepB = Arrays.zip(a, b, true, true, (x, y) -> y);
            assertArrayEquals(new boolean[] { false, true }, keepB[0]);
            assertArrayEquals(new boolean[] { true, false }, keepB[1]);

            final boolean[][] keepA = Arrays.zip(a, b, true, false, (x, y) -> x);
            assertArrayEquals(new boolean[] { true, false }, keepA[0]);
            assertArrayEquals(new boolean[] { true, true }, keepA[1]);

            // a null sub-array behaves like a missing one
            final boolean[][] nullRow = Arrays.zip(new boolean[][] { null }, new boolean[][] { { true, false } }, true, true, (x, y) -> y);
            assertArrayEquals(new boolean[] { true, false }, nullRow[0]);
        }

        @Test
        void zip_boolean3D_binaryOp() {
            boolean[][][] a = { { { true, false } } };
            boolean[][][] b = { { { false, true } } };
            boolean[][][] result = Arrays.zip(a, b, (x, y) -> x || y);

            assertEquals(1, result.length);
            assertTrue(result[0][0][0]);
            assertTrue(result[0][0][1]);
        }

        @Test
        void zip_booleanArrays() {
            boolean[] a = { true, false, true };
            boolean[] b = { false, false, true };
            boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

            assertArrayEquals(new boolean[] { false, false, true }, result);
        }

        @Test
        void zip_booleanArraysEmpty() {
            boolean[] a = {};
            boolean[] b = {};
            boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

            assertEquals(0, result.length);
        }

        @Test
        void zip_booleanArraysNull() {
            boolean[] a = null;
            boolean[] b = { true, false };
            boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

            assertEquals(0, result.length);
        }

        @Test
        void zip_booleanArraysWithDefaults() {
            boolean[] a = { true, false };
            boolean[] b = { false };
            boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

            assertEquals(2, result.length);
            assertTrue(result[0]);
            assertTrue(result[1]);
        }

        @Test
        void zip_boundaryValues_integerLimits() {
            int[] maxValues = { Integer.MAX_VALUE, Integer.MAX_VALUE - 1 };
            int[] ones = { 1, 1 };

            // Test overflow behavior
            int[] overflowResult = Arrays.zip(maxValues, ones, (x, y) -> x + y);
            assertEquals(Integer.MIN_VALUE, overflowResult[0]); // Overflow wraps around
            assertEquals(Integer.MAX_VALUE, overflowResult[1]);
        }

        @Test
        void zip_byte1D_binaryOp() {
            byte[] a = { 1, 2, 3 };
            byte[] b = { 4, 5, 6 };
            byte[] result = Arrays.zip(a, b, (x, y) -> (byte) (x + y));

            assertArrayEquals(new byte[] { 5, 7, 9 }, result);
        }

        @Test
        void zip_byte2D_binaryOp() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            byte[][] b = { { 5, 6 }, { 7, 8 } };
            byte[][] result = Arrays.zip(a, b, (x, y) -> (byte) (x + y));

            assertEquals(2, result.length);
            assertArrayEquals(new byte[] { 6, 8 }, result[0]);
            assertArrayEquals(new byte[] { 10, 12 }, result[1]);
        }

        @Test
        void zip_byte2D_ternaryOp() {
            byte[][] a = { { 1, 2 } };
            byte[][] b = { { 3, 4 } };
            byte[][] c = { { 5, 6 } };
            byte[][] result = Arrays.zip(a, b, c, (x, y, z) -> (byte) (x + y + z));

            assertEquals(1, result.length);
            assertArrayEquals(new byte[] { 9, 12 }, result[0]);
        }

        @Test
        void zip_byte3D_binaryOp() {
            byte[][][] a = { { { 1, 2 } } };
            byte[][][] b = { { { 3, 4 } } };
            byte[][][] result = Arrays.zip(a, b, (x, y) -> (byte) (x + y));

            assertEquals(1, result.length);
            assertArrayEquals(new byte[] { 4, 6 }, result[0][0]);
        }

        @Test
        void zip_char_1D_differentLength() {
            char[] a = { 'a', 'b' };
            char[] b = { 'x', 'y', 'z' };
            char[] result = Arrays.zip(a, b, '-', '+', (x, y) -> x);
            assertArrayEquals(new char[] { 'a', 'b', '-' }, result);
        }

        @Test
        void zip_char_1D_sameLength() {
            char[] a = { 'a', 'b', 'c' };
            char[] b = { 'x', 'y', 'z' };
            char[] result = Arrays.zip(a, b, (x, y) -> x);
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, result);
        }

        @Test
        void zip_char_2D_valid() {
            char[][] a = { { 'a', 'b' } };
            char[][] b = { { 'x', 'y' } };
            char[][] result = Arrays.zip(a, b, (x, y) -> y);
            assertArrayEquals(new char[] { 'x', 'y' }, result[0]);
        }

        @Test
        void zip_char1D_binaryOp() {
            char[] a = { 'a', 'b', 'c' };
            char[] b = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, (x, y) -> x);

            assertArrayEquals(new char[] { 'a', 'b', 'c' }, result);
        }

        @Test
        void zip_char2D_binaryOp() {
            char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
            char[][] b = { { 'x', 'y' }, { 'z', 'w' } };
            char[][] result = Arrays.zip(a, b, (x, y) -> x);

            assertEquals(2, result.length);
            assertArrayEquals(new char[] { 'a', 'b' }, result[0]);
            assertArrayEquals(new char[] { 'c', 'd' }, result[1]);
        }

        @Test
        void zip_char2D_ternaryOp() {
            char[][] a = { { 'a', 'b' } };
            char[][] b = { { 'c', 'd' } };
            char[][] c = { { 'e', 'f' } };
            char[][] result = Arrays.zip(a, b, c, (x, y, z) -> x);

            assertEquals(1, result.length);
            assertArrayEquals(new char[] { 'a', 'b' }, result[0]);
        }

        @Test
        void zip_char3D_binaryOp() {
            char[][][] a = { { { 'a', 'b' } } };
            char[][][] b = { { { 'c', 'd' } } };
            char[][][] result = Arrays.zip(a, b, (x, y) -> x);

            assertEquals(1, result.length);
            assertArrayEquals(new char[] { 'a', 'b' }, result[0][0]);
        }

        @Test
        void zip_charArrays() {
            char[] a = { 'a', 'b', 'c' };
            char[] b = { 'x', 'y', 'z' };
            char[] result = Arrays.zip(a, b, (x, y) -> Character.isLowerCase(x) ? x : y);

            assertArrayEquals(new char[] { 'a', 'b', 'c' }, result);
        }

        @Test
        void zip_complexMathematicalOperations() {
            int[] a = { 2, 4, 6, 8 };
            int[] b = { 1, 3, 5, 7 };

            // Test modulo operation
            int[] modResult = Arrays.zip(a, b, (x, y) -> x % y);
            assertArrayEquals(new int[] { 0, 1, 1, 1 }, modResult);

            // Test bitwise operations
            int[] bitwiseAndResult = Arrays.zip(a, b, (x, y) -> x & y);
            assertArrayEquals(new int[] { 0, 0, 4, 0 }, bitwiseAndResult);

            int[] bitwiseOrResult = Arrays.zip(a, b, (x, y) -> x | y);
            assertArrayEquals(new int[] { 3, 7, 7, 15 }, bitwiseOrResult);

            int[] bitwiseXorResult = Arrays.zip(a, b, (x, y) -> x ^ y);
            assertArrayEquals(new int[] { 3, 7, 3, 15 }, bitwiseXorResult);
        }

        @Test
        void zip_double1D_binaryOp() {
            double[] a = { 1.0, 2.0, 3.0 };
            double[] b = { 4.0, 5.0, 6.0 };
            double[] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertArrayEquals(new double[] { 5.0, 7.0, 9.0 }, result, 0.0001);
        }

        @Test
        void zip_double1D_binaryOp_withDefaults() {
            double[] a = { 1.0, 2.0 };
            double[] b = { 3.0, 4.0, 5.0 };
            double[] result = Arrays.zip(a, b, 0.0, 0.0, (x, y) -> x + y);

            assertArrayEquals(new double[] { 4.0, 6.0, 5.0 }, result, 0.0001);
        }

        @Test
        void zip_double2D_binaryOp() {
            double[][] a = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            double[][] b = { { 5.0, 6.0 }, { 7.0, 8.0 } };
            double[][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(2, result.length);
            assertArrayEquals(new double[] { 6.0, 8.0 }, result[0], 0.0001);
            assertArrayEquals(new double[] { 10.0, 12.0 }, result[1], 0.0001);
        }

        @Test
        void zip_double2D_ternaryOp() {
            double[][] a = { { 1.0, 2.0 } };
            double[][] b = { { 3.0, 4.0 } };
            double[][] c = { { 5.0, 6.0 } };
            double[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertEquals(1, result.length);
            assertArrayEquals(new double[] { 9.0, 12.0 }, result[0], 0.0001);
        }

        @Test
        void zip_double3D_binaryOp() {
            double[][][] a = { { { 1.0, 2.0 } } };
            double[][][] b = { { { 3.0, 4.0 } } };
            double[][][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(1, result.length);
            assertArrayEquals(new double[] { 4.0, 6.0 }, result[0][0], 0.0001);
        }

        @Test
        void zip_edgeCases_emptyArrays() {
            int[] empty1 = {};
            int[] empty2 = {};

            int[] result = Arrays.zip(empty1, empty2, (x, y) -> x + y);
            assertArrayEquals(new int[] {}, result);
        }

        @Test
        void zip_edgeCases_nullArrays() {
            int[] a = null;
            int[] b = { 1, 2, 3 };

            int[] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new int[] {}, result);
        }

        @Test
        void zip_errorHandling_divisionByZero() {
            int[] a = { 10, 20, 30 };
            int[] b = { 2, 0, 5 };

            // Test division by zero handling
            assertThrows(ArithmeticException.class, () -> {
                Arrays.zip(a, b, (x, y) -> x / y);
            });
        }

        @Test
        void zip_float1D_binaryOp() {
            float[] a = { 1.0f, 2.0f, 3.0f };
            float[] b = { 4.0f, 5.0f, 6.0f };
            float[] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertArrayEquals(new float[] { 5.0f, 7.0f, 9.0f }, result, 0.0001f);
        }

        @Test
        void zip_float2D_binaryOp() {
            float[][] a = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            float[][] b = { { 5.0f, 6.0f }, { 7.0f, 8.0f } };
            float[][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(2, result.length);
            assertArrayEquals(new float[] { 6.0f, 8.0f }, result[0], 0.0001f);
            assertArrayEquals(new float[] { 10.0f, 12.0f }, result[1], 0.0001f);
        }

        @Test
        void zip_float2D_ternaryOp() {
            float[][] a = { { 1.0f, 2.0f } };
            float[][] b = { { 3.0f, 4.0f } };
            float[][] c = { { 5.0f, 6.0f } };
            float[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertEquals(1, result.length);
            assertArrayEquals(new float[] { 9.0f, 12.0f }, result[0], 0.0001f);
        }

        @Test
        void zip_float3D_binaryOp() {
            float[][][] a = { { { 1.0f, 2.0f } } };
            float[][][] b = { { { 3.0f, 4.0f } } };
            float[][][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(1, result.length);
            assertArrayEquals(new float[] { 4.0f, 6.0f }, result[0][0], 0.0001f);
        }

        @Test
        void zip_int1D_binaryOp() {
            int[] a = { 1, 2, 3 };
            int[] b = { 4, 5, 6 };
            int[] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertArrayEquals(new int[] { 5, 7, 9 }, result);
        }

        @Test
        void zip_int1D_binaryOp_withDefaults() {
            int[] a = { 1, 2 };
            int[] b = { 3, 4, 5 };
            int[] result = Arrays.zip(a, b, 0, 0, (x, y) -> x + y);

            assertArrayEquals(new int[] { 4, 6, 5 }, result);
        }

        @Test
        void zip_int1D_ternaryOp() {
            int[] a = { 1, 2 };
            int[] b = { 3, 4 };
            int[] c = { 5, 6 };
            int[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertArrayEquals(new int[] { 9, 12 }, result);
        }

        @Test
        void zip_int1D_ternaryOp_withDefaults() {
            int[] a = { 1 };
            int[] b = { 2, 3 };
            int[] c = { 4, 5 };
            int[] result = Arrays.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);

            assertArrayEquals(new int[] { 7, 8 }, result);
        }

        @Test
        void zip_int2D_binaryOp() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            int[][] b = { { 5, 6 }, { 7, 8 } };
            int[][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(2, result.length);
            assertArrayEquals(new int[] { 6, 8 }, result[0]);
            assertArrayEquals(new int[] { 10, 12 }, result[1]);
        }

        @Test
        void zip_int2D_binaryOp_withDefaults() {
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
        void zip_int2D_ternaryOp() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            int[][] b = { { 5, 6 }, { 7, 8 } };
            int[][] c = { { 9, 10 }, { 11, 12 } };
            int[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertEquals(2, result.length);
            assertArrayEquals(new int[] { 15, 18 }, result[0]);
            assertArrayEquals(new int[] { 21, 24 }, result[1]);
        }

        @Test
        void zip_int2D_ternaryOp_withDefaults() {
            int[][] a = { { 1, 2 } };
            int[][] b = { { 3, 4 }, { 5, 6 } };
            int[][] c = { { 7, 8 }, { 9, 10 } };
            int[][] result = Arrays.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);

            assertEquals(2, result.length);
            assertArrayEquals(new int[] { 11, 14 }, result[0]);
            assertArrayEquals(new int[] { 14, 16 }, result[1]);
        }

        @Test
        void zip_int2DArrays_mathematicalOperations() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            int[][] b = { { 10, 20 }, { 30, 40 } };

            // two-dimensional addition
            int[][] addResult = Arrays.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new int[] { 11, 22 }, addResult[0]);
            assertArrayEquals(new int[] { 33, 44 }, addResult[1]);

            // two-dimensional multiplication
            int[][] mulResult = Arrays.zip(a, b, (x, y) -> x * y);
            assertArrayEquals(new int[] { 10, 40 }, mulResult[0]);
            assertArrayEquals(new int[] { 90, 160 }, mulResult[1]);
        }

        @Test
        void zip_int3D_binaryOp() {
            int[][][] a = { { { 1, 2 }, { 3, 4 } } };
            int[][][] b = { { { 5, 6 }, { 7, 8 } } };
            int[][][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(1, result.length);
            assertEquals(2, result[0].length);
            assertArrayEquals(new int[] { 6, 8 }, result[0][0]);
            assertArrayEquals(new int[] { 10, 12 }, result[0][1]);
        }

        @Test
        void zip_int3D_binaryOp_withDefaults() {
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
        void zip_int3D_ternaryOp() {
            int[][][] a = { { { 1, 2 } } };
            int[][][] b = { { { 3, 4 } } };
            int[][][] c = { { { 5, 6 } } };
            int[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertEquals(1, result.length);
            assertEquals(1, result[0].length);
            assertArrayEquals(new int[] { 9, 12 }, result[0][0]);
        }

        @Test
        void zip_int3DArrays_mathematicalOperations() {
            int[][][] a = { { { 1, 2 } }, { { 3, 4 } } };
            int[][][] b = { { { 10, 20 } }, { { 30, 40 } } };

            // three-dimensional addition
            int[][][] addResult = Arrays.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new int[] { 11, 22 }, addResult[0][0]);
            assertArrayEquals(new int[] { 33, 44 }, addResult[1][0]);

            // three-dimensional subtraction
            int[][][] subResult = Arrays.zip(a, b, (x, y) -> y - x);
            assertArrayEquals(new int[] { 9, 18 }, subResult[0][0]);
            assertArrayEquals(new int[] { 27, 36 }, subResult[1][0]);
        }

        @Test
        void zip_intArrays_mathematicalOperations() {
            int[] a = { 10, 20, 30 };
            int[] b = { 1, 2, 3 };

            // Addition
            int[] addResult = Arrays.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new int[] { 11, 22, 33 }, addResult);

            // Subtraction
            int[] subResult = Arrays.zip(a, b, (x, y) -> x - y);
            assertArrayEquals(new int[] { 9, 18, 27 }, subResult);

            // Multiplication
            int[] mulResult = Arrays.zip(a, b, (x, y) -> x * y);
            assertArrayEquals(new int[] { 10, 40, 90 }, mulResult);

            // Division
            int[] divResult = Arrays.zip(a, b, (x, y) -> x / y);
            assertArrayEquals(new int[] { 10, 10, 10 }, divResult);

            // Maximum
            int[] maxResult = Arrays.zip(a, b, (x, y) -> Math.max(x, y));
            assertArrayEquals(new int[] { 10, 20, 30 }, maxResult);

            // Minimum
            int[] minResult = Arrays.zip(a, b, (x, y) -> Math.min(x, y));
            assertArrayEquals(new int[] { 1, 2, 3 }, minResult);
        }

        @Test
        void zip_intArrays_threeArrays() {
            int[] a = { 1, 2, 3 };
            int[] b = { 10, 20, 30 };
            int[] c = { 100, 200, 300 };

            // Three-way addition
            int[] addResult = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertArrayEquals(new int[] { 111, 222, 333 }, addResult);

            // Three-way multiplication
            int[] mulResult = Arrays.zip(a, b, c, (x, y, z) -> x * y * z);
            assertArrayEquals(new int[] { 1000, 8000, 27000 }, mulResult);

            // Complex mathematical operation
            int[] complexResult = Arrays.zip(a, b, c, (x, y, z) -> (x + y) * z);
            assertArrayEquals(new int[] { 1100, 4400, 9900 }, complexResult);
        }

        @Test
        void zip_intArrays_threeArrays_withDefaults() {
            int[] a = { 1, 2 };
            int[] b = { 10, 20, 30 };
            int[] c = { 100 };

            // Three arrays with different lengths and defaults
            int[] result = Arrays.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
            assertArrayEquals(new int[] { 111, 22, 30 }, result);

            // Test with non-zero defaults
            int[] result2 = Arrays.zip(a, b, c, 5, 15, 500, (x, y, z) -> x + y + z);
            assertArrayEquals(new int[] { 111, 522, 535 }, result2);
        }

        @Test
        void zip_intArrays_withDefaults_mathematicalOperations() {
            int[] a = { 5, 10 };
            int[] b = { 1, 2, 3, 4 };

            // Addition with defaults
            int[] addResult = Arrays.zip(a, b, 0, 0, (x, y) -> x + y);
            assertArrayEquals(new int[] { 6, 12, 3, 4 }, addResult);

            // Multiplication with defaults - one array shorter
            int[] mulResult = Arrays.zip(a, b, 1, 1, (x, y) -> x * y);
            assertArrayEquals(new int[] { 5, 20, 3, 4 }, mulResult);

            // Test with different defaults
            int[] customResult = Arrays.zip(a, b, 100, 200, (x, y) -> x + y);
            assertArrayEquals(new int[] { 6, 12, 103, 104 }, customResult);
        }

        @Test
        void zip_long1D_binaryOp() {
            long[] a = { 1L, 2L, 3L };
            long[] b = { 4L, 5L, 6L };
            long[] result = Arrays.zip(a, b, (x, y) -> x * y);

            assertArrayEquals(new long[] { 4L, 10L, 18L }, result);
        }

        @Test
        void zip_long1D_binaryOp_withDefaults() {
            long[] a = { 1L, 2L };
            long[] b = { 3L, 4L, 5L };
            long[] result = Arrays.zip(a, b, 1L, 1L, (x, y) -> x * y);

            assertArrayEquals(new long[] { 3L, 8L, 5L }, result);
        }

        @Test
        void zip_long2D_binaryOp() {
            long[][] a = { { 1L, 2L }, { 3L, 4L } };
            long[][] b = { { 5L, 6L }, { 7L, 8L } };
            long[][] result = Arrays.zip(a, b, (x, y) -> x * y);

            assertEquals(2, result.length);
            assertArrayEquals(new long[] { 5L, 12L }, result[0]);
            assertArrayEquals(new long[] { 21L, 32L }, result[1]);
        }

        @Test
        void zip_long2D_ternaryOp() {
            long[][] a = { { 1L, 2L } };
            long[][] b = { { 3L, 4L } };
            long[][] c = { { 5L, 6L } };
            long[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);

            assertEquals(1, result.length);
            assertArrayEquals(new long[] { 9L, 12L }, result[0]);
        }

        @Test
        void zip_long3D_binaryOp() {
            long[][][] a = { { { 1L, 2L } } };
            long[][][] b = { { { 3L, 4L } } };
            long[][][] result = Arrays.zip(a, b, (x, y) -> x + y);

            assertEquals(1, result.length);
            assertArrayEquals(new long[] { 4L, 6L }, result[0][0]);
        }

        @Test
        void zip_object2D_binaryOp_withDefaults_allowsNullFirstArray() throws Exception {
            Integer[][] a = null;
            Integer[][] b = { { 10, 20 }, { 30 } };
            Integer[][] result = Arrays.ff.zip(a, b, 0, 0, Integer::sum);

            assertEquals(2, result.length);
            assertArrayEquals(new Integer[] { 10, 20 }, result[0]);
            assertArrayEquals(new Integer[] { 30 }, result[1]);
        }

        @Test
        void zip_object2D_binaryOp_withDefaults_rejectsUnknownTargetType() {
            Integer[][] a = null;
            Integer[][] b = { { 10 } };

            assertThrows(IllegalArgumentException.class, () -> Arrays.ff.zip(a, b, null, 0, Integer::sum));
        }

        @Test
        void zip_object2D_ternaryOp_withDefaults_allowsNullFirstArray() throws Exception {
            Integer[][] a = null;
            Integer[][] b = { { 10 }, { 20, 30 } };
            Integer[][] c = { { 1, 2 }, { 3 } };
            Integer[][] result = Arrays.ff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);

            assertEquals(2, result.length);
            assertArrayEquals(new Integer[] { 11, 2 }, result[0]);
            assertArrayEquals(new Integer[] { 23, 30 }, result[1]);
        }

        @Test
        void zip_short1D_binaryOp() {
            short[] a = { 1, 2, 3 };
            short[] b = { 4, 5, 6 };
            short[] result = Arrays.zip(a, b, (x, y) -> (short) (x + y));

            assertArrayEquals(new short[] { 5, 7, 9 }, result);
        }

        @Test
        void zip_short2D_binaryOp() {
            short[][] a = { { 1, 2 }, { 3, 4 } };
            short[][] b = { { 5, 6 }, { 7, 8 } };
            short[][] result = Arrays.zip(a, b, (x, y) -> (short) (x + y));

            assertEquals(2, result.length);
            assertArrayEquals(new short[] { 6, 8 }, result[0]);
            assertArrayEquals(new short[] { 10, 12 }, result[1]);
        }

        @Test
        void zip_short2D_ternaryOp() {
            short[][] a = { { 1, 2 } };
            short[][] b = { { 3, 4 } };
            short[][] c = { { 5, 6 } };
            short[][] result = Arrays.zip(a, b, c, (x, y, z) -> (short) (x + y + z));

            assertEquals(1, result.length);
            assertArrayEquals(new short[] { 9, 12 }, result[0]);
        }

        @Test
        void zip_short3D_binaryOp() {
            short[][][] a = { { { 1, 2 } } };
            short[][][] b = { { { 3, 4 } } };
            short[][][] result = Arrays.zip(a, b, (x, y) -> (short) (x + y));

            assertEquals(1, result.length);
            assertArrayEquals(new short[] { 4, 6 }, result[0][0]);
        }

        @Test
        void zip1D_threeArrays_differentLengths() throws Exception {
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true };
            boolean[] c = { true, true, false };

            boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x && y && z);

            assertArrayEquals(new boolean[] { false, false }, result);
        }

        @Test
        void zip1D_threeArrays_emptyArrays() throws Exception {
            boolean[] a = {};
            boolean[] b = { true };
            boolean[] c = { false, true };

            boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        void zip1D_threeArrays_exceptionPropagation() {
            boolean[] a = { true };
            boolean[] b = { false };
            boolean[] c = { true };

            Throwables.BooleanTernaryOperator<RuntimeException> errorFunction = (x, y, z) -> {
                throw new RuntimeException("Test exception");
            };

            assertThrows(RuntimeException.class, () -> Arrays.zip(a, b, c, errorFunction));
        }

        @Test
        void zip1D_threeArrays_nullArrays() throws Exception {
            boolean[] a = null;
            boolean[] b = null;
            boolean[] c = null;

            boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x && y && z);

            assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        void zip1D_threeArrays_sameLength() throws Exception {
            boolean[] a = { true, false, true };
            boolean[] b = { false, true, false };
            boolean[] c = { true, true, false };

            boolean[] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            assertArrayEquals(new boolean[] { true, true, true }, result);
        }

        @Test
        void zip1D_threeArraysWithDefaults_allNull() throws Exception {
            boolean[] a = null;
            boolean[] b = null;
            boolean[] c = null;

            boolean[] result = Arrays.zip(a, b, c, true, false, true, (x, y, z) -> x && y && z);

            assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        void zip1D_threeArraysWithDefaults_differentLengths() throws Exception {
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true };
            boolean[] c = { true, true, false };

            boolean[] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x || y || z);

            assertArrayEquals(new boolean[] { true, true, true, true }, result);
        }

        @Test
        void zip1D_threeArraysWithDefaults_oneEmpty() throws Exception {
            boolean[] a = {};
            boolean[] b = { true, false };
            boolean[] c = { false, true, false };

            boolean[] result = Arrays.zip(a, b, c, true, false, true, (x, y, z) -> x ^ y ^ z);

            assertArrayEquals(new boolean[] { false, false, true }, result);
        }

        @Test
        void zip1D_threeArraysWithDefaults_sameLength() throws Exception {
            boolean[] a = { true, false, true };
            boolean[] b = { false, true, false };
            boolean[] c = { true, true, false };

            boolean[] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x && y && z);

            assertArrayEquals(new boolean[] { false, false, false }, result);
        }

        @Test
        void zip1D_twoArrays_differentLength_firstShorter() throws Exception {
            boolean[] a = { true, false };
            boolean[] b = { false, true, false, true };

            boolean[] result = Arrays.zip(a, b, (x, y) -> x || y);

            assertArrayEquals(new boolean[] { true, true }, result);
        }

        @Test
        void zip1D_twoArrays_differentLength_secondShorter() throws Exception {
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true };

            boolean[] result = Arrays.zip(a, b, (x, y) -> x ^ y);

            assertArrayEquals(new boolean[] { true, true }, result);
        }

        @Test
        void zip1D_twoArrays_emptyArrays() throws Exception {
            boolean[] a = {};
            boolean[] b = {};

            boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

            assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        void zip1D_twoArrays_exceptionPropagation() {
            boolean[] a = { true, false };
            boolean[] b = { false, true };

            Throwables.BooleanBinaryOperator<RuntimeException> errorFunction = (x, y) -> {
                throw new RuntimeException("Test exception");
            };

            assertThrows(RuntimeException.class, () -> Arrays.zip(a, b, errorFunction));
        }

        @Test
        void zip1D_twoArrays_nullArrays() throws Exception {
            boolean[] a = null;
            boolean[] b = null;

            boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

            assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        void zip1D_twoArrays_oneNull() throws Exception {
            boolean[] a = { true, false };
            boolean[] b = null;

            boolean[] result = Arrays.zip(a, b, (x, y) -> x || y);

            assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        void zip1D_twoArrays_sameLength() throws Exception {
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true, false, true };

            boolean[] result = Arrays.zip(a, b, (x, y) -> x && y);

            assertArrayEquals(new boolean[] { false, false, false, false }, result);
        }

        @Test
        void zip1D_twoArraysWithDefaults_emptyArrays() throws Exception {
            boolean[] a = {};
            boolean[] b = {};

            boolean[] result = Arrays.zip(a, b, true, false, (x, y) -> x && y);

            assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        void zip1D_twoArraysWithDefaults_firstShorter() throws Exception {
            boolean[] a = { true, false };
            boolean[] b = { false, true, false, true };

            boolean[] result = Arrays.zip(a, b, true, false, (x, y) -> x || y);

            assertArrayEquals(new boolean[] { true, true, true, true }, result);
        }

        @Test
        void zip1D_twoArraysWithDefaults_nullArrays() throws Exception {
            boolean[] a = null;
            boolean[] b = null;

            boolean[] result = Arrays.zip(a, b, true, false, (x, y) -> x && y);

            assertArrayEquals(new boolean[] {}, result);
        }

        @Test
        void zip1D_twoArraysWithDefaults_oneNullOneNonEmpty() throws Exception {
            boolean[] a = null;
            boolean[] b = { true, false, true };

            boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

            assertArrayEquals(new boolean[] { true, false, true }, result);
        }

        @Test
        void zip1D_twoArraysWithDefaults_sameLength() throws Exception {
            boolean[] a = { true, false, true };
            boolean[] b = { false, true, false };

            boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x && y);

            assertArrayEquals(new boolean[] { false, false, false }, result);
        }

        @Test
        void zip1D_twoArraysWithDefaults_secondShorter() throws Exception {
            boolean[] a = { true, false, true, false };
            boolean[] b = { false, true };

            boolean[] result = Arrays.zip(a, b, false, true, (x, y) -> x ^ y);

            assertArrayEquals(new boolean[] { true, true, false, true }, result);
        }

        @Test
        void zip2D_threeArrays_differentOuterLengths() throws Exception {
            boolean[][] a = { { true, false }, { false, true }, { true, true } };
            boolean[][] b = { { false, true } };
            boolean[][] c = { { true, true }, { false, false } };

            boolean[][] result = Arrays.zip(a, b, c, (x, y, z) -> x && y && z);

            assertArrayEquals(new boolean[][] { { false, false } }, result);
        }

        @Test
        void zip2D_threeArrays_emptyAndNull() throws Exception {
            boolean[][] a = {};
            boolean[][] b = null;
            boolean[][] c = { { true, false } };

            boolean[][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            assertArrayEquals(new boolean[][] {}, result);
        }

        @Test
        void zip2D_threeArrays_sameStructure() throws Exception {
            boolean[][] a = { { true, false }, { false, true } };
            boolean[][] b = { { false, true }, { true, false } };
            boolean[][] c = { { true, true }, { false, false } };

            boolean[][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            assertArrayEquals(new boolean[][] { { true, true }, { true, true } }, result);
        }

        @Test
        void zip2D_threeArraysWithDefaults_differentStructures() throws Exception {
            boolean[][] a = { { true } };
            boolean[][] b = { { false, true }, { true } };
            boolean[][] c = { { true, true, false } };

            boolean[][] result = Arrays.zip(a, b, c, false, false, true, (x, y, z) -> x || y || z);

            assertArrayEquals(new boolean[][] { { true, true, false }, { true } }, result);
        }

        @Test
        void zip2D_threeArraysWithDefaults_sameStructure() throws Exception {
            boolean[][] a = { { true, false }, { false, true } };
            boolean[][] b = { { false, true }, { true, false } };
            boolean[][] c = { { true, true }, { false, false } };

            boolean[][] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x ^ y ^ z);

            assertArrayEquals(new boolean[][] { { false, false }, { true, true } }, result);
        }

        @Test
        void zip2D_twoArrays_differentInnerLengths() throws Exception {
            boolean[][] a = { { true, false, true }, { false, true } };
            boolean[][] b = { { false, true }, { true, false, true, false } };

            boolean[][] result = Arrays.zip(a, b, (x, y) -> x || y);

            assertArrayEquals(new boolean[][] { { true, true }, { true, true } }, result);
        }

        @Test
        void zip2D_twoArrays_differentOuterLength() throws Exception {
            boolean[][] a = { { true, false }, { false, true }, { true, true } };
            boolean[][] b = { { false, true }, { true, false } };

            boolean[][] result = Arrays.zip(a, b, (x, y) -> x && y);

            assertArrayEquals(new boolean[][] { { false, false }, { false, false } }, result);
        }

        @Test
        void zip2D_twoArrays_emptyArrays() throws Exception {
            boolean[][] a = {};
            boolean[][] b = {};

            boolean[][] result = Arrays.zip(a, b, (x, y) -> x && y);

            assertArrayEquals(new boolean[][] {}, result);
        }

        @Test
        void zip2D_twoArrays_exceptionPropagation() {
            boolean[][] a = { { true } };
            boolean[][] b = { { false } };

            Throwables.BooleanBinaryOperator<RuntimeException> errorFunction = (x, y) -> {
                throw new RuntimeException("Test exception");
            };

            assertThrows(RuntimeException.class, () -> Arrays.zip(a, b, errorFunction));
        }

        @Test
        void zip2D_twoArrays_nullArrays() throws Exception {
            boolean[][] a = null;
            boolean[][] b = null;

            boolean[][] result = Arrays.zip(a, b, (x, y) -> x && y);

            assertArrayEquals(new boolean[][] {}, result);
        }

        @Test
        void zip2D_twoArrays_sameStructure() throws Exception {
            boolean[][] a = { { true, false }, { false, true } };
            boolean[][] b = { { false, true }, { true, false } };

            boolean[][] result = Arrays.zip(a, b, (x, y) -> x ^ y);

            assertArrayEquals(new boolean[][] { { true, true }, { true, true } }, result);
        }

        @Test
        void zip2D_twoArraysWithDefaults_differentInnerLengths() throws Exception {
            boolean[][] a = { { true, false }, { false, true, false } };
            boolean[][] b = { { false, true, false }, { true, false }, { true } };

            boolean[][] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

            assertArrayEquals(new boolean[][] { { true, true, false }, { true, true, true }, { true } }, result);
        }

        @Test
        void zip2D_twoArraysWithDefaults_differentOuterLength() throws Exception {
            boolean[][] a = { { true, false } };
            boolean[][] b = { { false, true }, { true, false }, { true, true } };

            boolean[][] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

            assertArrayEquals(new boolean[][] { { true, true }, { true, false }, { true, true } }, result);
        }

        @Test
        void zip2D_twoArraysWithDefaults_nullSubArrays() throws Exception {
            boolean[][] a = { { true, false }, null };
            boolean[][] b = { null, { false, true } };

            boolean[][] result = Arrays.zip(a, b, true, false, (x, y) -> x || y);

            assertArrayEquals(new boolean[][] { { true, false }, { true, true } }, result);
        }

        @Test
        void zip2D_twoArraysWithDefaults_sameStructure() throws Exception {
            boolean[][] a = { { true, false }, { false, true } };
            boolean[][] b = { { false, true }, { true, false } };

            boolean[][] result = Arrays.zip(a, b, true, false, (x, y) -> x && y);

            assertArrayEquals(new boolean[][] { { false, false }, { false, false } }, result);
        }

        @Test
        void zip2DArrays() throws Exception {
            // Test with normal two-dimensional arrays
            long[][] a = { { 1, 2 }, { 3, 4, 5 } };
            long[][] b = { { 5, 6, 7 }, { 8, 9 } };
            long[][] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(2, result.length);
            assertArrayEquals(new long[] { 6, 8 }, result[0]);
            assertArrayEquals(new long[] { 11, 13 }, result[1]);
        }

        @Test
        void zip2DArraysDifferentOuterLengths() throws Exception {
            // Test with different outer array lengths
            long[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            long[][] b = { { 7, 8 }, { 9, 10 } };
            long[][] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(2, result.length);
            assertArrayEquals(new long[] { 8, 10 }, result[0]);
            assertArrayEquals(new long[] { 12, 14 }, result[1]);
        }

        @Test
        void zip2DArraysWithDefaults() throws Exception {
            // Test with two-dimensional arrays of different lengths using defaults
            long[][] a = { { 1, 2 }, { 3, 4, 5 } };
            long[][] b = { { 5, 6, 7 }, { 8, 9 }, { 10 } };
            long[][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertArrayEquals(new long[] { 6, 8, 7 }, result[0]);
            assertArrayEquals(new long[] { 11, 13, 15 }, result[1]);
            assertArrayEquals(new long[] { 10 }, result[2]);
        }

        @Test
        void zip2DArraysWithDefaultsNullArrays() throws Exception {
            // Test with null arrays and defaults
            long[][] result = Arrays.zip((long[][]) null, (long[][]) null, 10L, 20L, (x, y) -> x + y);
            assertEquals(0, result.length);

            // Test with first null
            long[][] b = { { 5, 6 }, { 7, 8 } };
            long[][] result2 = Arrays.zip(null, b, 10L, 20L, (x, y) -> x + y);
            assertEquals(2, result2.length);
            assertArrayEquals(new long[] { 15, 16 }, result2[0]);
            assertArrayEquals(new long[] { 17, 18 }, result2[1]);
        }

        @Test
        void zip2DArraysWithNull() throws Exception {
            // Test with null two-dimensional arrays
            long[][] result = Arrays.zip((long[][]) null, (long[][]) null, (x, y) -> x + y);
            assertEquals(0, result.length);

            // Test with one null
            long[][] a = { { 1, 2 }, { 3, 4 } };
            long[][] result2 = Arrays.zip(a, null, (x, y) -> x + y);
            assertEquals(0, result2.length);
        }

        @Test
        void zip2DArraysWithNullSubArrays() throws Exception {
            // Test with null sub-arrays
            long[][] a = { { 1, 2 }, null, { 3, 4 } };
            long[][] b = { { 5, 6 }, { 7, 8 }, null };
            long[][] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertArrayEquals(new long[] { 6, 8 }, result[0]);
            assertArrayEquals(new long[] {}, result[1]);
            assertArrayEquals(new long[] {}, result[2]);
        }

        @Test
        void zip3D_threeArrays_differentOuterLengths() throws Exception {
            boolean[][][] a = { { { true, false } }, { { false, true } }, { { true, true } } };
            boolean[][][] b = { { { false, true } } };
            boolean[][][] c = { { { true, true } }, { { false, false } } };

            boolean[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x && y && z);

            assertArrayEquals(new boolean[][][] { { { false, false } } }, result);
        }

        @Test
        void zip3D_threeArrays_nullArrays() throws Exception {
            boolean[][][] a = null;
            boolean[][][] b = null;
            boolean[][][] c = null;

            boolean[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            assertArrayEquals(new boolean[][][] {}, result);
        }

        @Test
        void zip3D_threeArrays_sameStructure() throws Exception {
            boolean[][][] a = { { { true, false } }, { { false, true } } };
            boolean[][][] b = { { { false, true } }, { { true, false } } };
            boolean[][][] c = { { { true, true } }, { { false, false } } };

            boolean[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x || y || z);

            assertArrayEquals(new boolean[][][] { { { true, true } }, { { true, true } } }, result);
        }

        @Test
        void zip3D_threeArraysWithDefaults_complexStructure() throws Exception {
            boolean[][][] a = { { { true } } };
            boolean[][][] b = { { { false, true }, { true } }, { { false } } };
            boolean[][][] c = { { { true, true, false } } };

            boolean[][][] result = Arrays.zip(a, b, c, true, false, true, (x, y, z) -> x || y || z);

            assertArrayEquals(new boolean[][][] { { { true, true, true }, { true } }, { { true } } }, result);
        }

        @Test
        void zip3D_threeArraysWithDefaults_sameStructure() throws Exception {
            boolean[][][] a = { { { true, false } }, { { false, true } } };
            boolean[][][] b = { { { false, true } }, { { true, false } } };
            boolean[][][] c = { { { true, true } }, { { false, false } } };

            boolean[][][] result = Arrays.zip(a, b, c, false, true, false, (x, y, z) -> x ^ y ^ z);

            assertArrayEquals(new boolean[][][] { { { false, false } }, { { true, true } } }, result);
        }

        @Test
        void zip3D_twoArrays_differentOuterLength() throws Exception {
            boolean[][][] a = { { { true, false } }, { { false, true } }, { { true, true } } };
            boolean[][][] b = { { { false, true } }, { { true, false } } };

            boolean[][][] result = Arrays.zip(a, b, (x, y) -> x && y);

            assertArrayEquals(new boolean[][][] { { { false, false } }, { { false, false } } }, result);
        }

        @Test
        void zip3D_twoArrays_emptyAndNull() throws Exception {
            boolean[][][] a = {};
            boolean[][][] b = null;

            boolean[][][] result = Arrays.zip(a, b, (x, y) -> x || y);

            assertArrayEquals(new boolean[][][] {}, result);
        }

        @Test
        void zip3D_twoArrays_exceptionPropagation() {
            boolean[][][] a = { { { true } } };
            boolean[][][] b = { { { false } } };

            Throwables.BooleanBinaryOperator<RuntimeException> errorFunction = (x, y) -> {
                throw new RuntimeException("Test exception");
            };

            assertThrows(RuntimeException.class, () -> Arrays.zip(a, b, errorFunction));
        }

        @Test
        void zip3D_twoArrays_sameStructure() throws Exception {
            boolean[][][] a = { { { true, false }, { false, true } }, { { true, true }, { false, false } } };
            boolean[][][] b = { { { false, true }, { true, false } }, { { false, false }, { true, true } } };

            boolean[][][] result = Arrays.zip(a, b, (x, y) -> x ^ y);

            assertArrayEquals(new boolean[][][] { { { true, true }, { true, true } }, { { true, true }, { true, true } } }, result);
        }

        @Test
        void zip3D_twoArraysWithDefaults_differentStructures() throws Exception {
            boolean[][][] a = { { { true, false } }, { { false } } };
            boolean[][][] b = { { { false, true }, { true, false } }, { { true, true } }, { { false, false } } };

            boolean[][][] result = Arrays.zip(a, b, false, true, (x, y) -> x || y);

            assertArrayEquals(new boolean[][][] { { { true, true }, { true, false } }, { { true, true } }, { { false, false } } }, result);
        }

        @Test
        void zip3D_twoArraysWithDefaults_sameStructure() throws Exception {
            boolean[][][] a = { { { true, false }, { false, true } }, { { true, true }, { false, false } } };
            boolean[][][] b = { { { false, true }, { true, false } }, { { false, false }, { true, true } } };

            boolean[][][] result = Arrays.zip(a, b, true, false, (x, y) -> x && y);

            assertArrayEquals(new boolean[][][] { { { false, false }, { false, false } }, { { false, false }, { false, false } } }, result);
        }

        @Test
        void zip3DArrays() throws Exception {
            // Test with normal three-dimensional arrays
            long[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } } };
            long[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 }, { 70, 80 } } };
            long[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertArrayEquals(new long[] { 11, 22 }, result[0][0]);
            assertArrayEquals(new long[] { 33, 44 }, result[0][1]);
            assertArrayEquals(new long[] { 55, 66 }, result[1][0]);
            assertArrayEquals(new long[] { 77, 88 }, result[1][1]);
        }

        @Test
        void zip3DArraysDifferentLengths() throws Exception {
            // Test with different outer array lengths
            long[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } }, { { 9, 10 } } };
            long[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 } } };
            long[][][] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertEquals(2, result.length);
        }

        @Test
        void zip3DArraysWithDefaults() throws Exception {
            // Test with three-dimensional arrays of different lengths using defaults
            long[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 } } };
            long[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 }, { 70, 80 } }, { { 90 } } };
            long[][][] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
            assertEquals(3, result.length);
            assertEquals(2, result[0].length);
            assertArrayEquals(new long[] { 11, 22 }, result[0][0]);
            assertArrayEquals(new long[] { 33, 44 }, result[0][1]);
            assertEquals(2, result[1].length);
            assertArrayEquals(new long[] { 55, 66 }, result[1][0]);
            assertArrayEquals(new long[] { 70, 80 }, result[1][1]);
            assertEquals(1, result[2].length);
            assertArrayEquals(new long[] { 90 }, result[2][0]);
        }

        @Test
        void zip3DArraysWithNull() throws Exception {
            // Test with null three-dimensional arrays
            long[][][] result = Arrays.zip((long[][][]) null, (long[][][]) null, (x, y) -> x + y);
            assertEquals(0, result.length);
        }

        @Test
        void zipByteArrays_ternaryWithDefaultValuesForTrailingRows() throws Exception {
            byte[][] first2d = { { 1 }, { 2, 3 } };
            byte[][] second2d = { { 10, 20 } };
            byte[][] third2d = { { 100 }, { 30, 40 }, { 50 } };

            byte[][] zipped2d = Arrays.zip(first2d, second2d, third2d, (byte) 5, (byte) 6, (byte) 7, (x, y, z) -> (byte) (x + y + z));

            assertArrayEquals(new byte[] { 111, 32 }, zipped2d[0]);
            assertArrayEquals(new byte[] { 38, 49 }, zipped2d[1]);
            assertArrayEquals(new byte[] { 61 }, zipped2d[2]);

            byte[][][] first3d = { { { 1 } }, { { 2, 3 } } };
            byte[][][] second3d = { { { 10, 20 } } };
            byte[][][] third3d = { { { 100 } }, { { 30, 40 } }, { { 50 } } };

            byte[][][] zipped3d = Arrays.zip(first3d, second3d, third3d, (byte) 5, (byte) 6, (byte) 7, (x, y, z) -> (byte) (x + y + z));

            assertArrayEquals(new byte[] { 111, 32 }, zipped3d[0][0]);
            assertArrayEquals(new byte[] { 38, 49 }, zipped3d[1][0]);
            assertArrayEquals(new byte[] { 61 }, zipped3d[2][0]);
        }

        @Test
        void zipFloat3D_allNullInputs() throws Exception {
            float[][][] r = Arrays.zip((float[][][]) null, (float[][][]) null, (float[][][]) null, (x, y, z) -> x + y + z);
            assertEquals(0, r.length);
            float[][][] r2 = Arrays.zip((float[][][]) null, (float[][][]) null, (float[][][]) null, 0f, 0f, 0f, (x, y, z) -> x + y + z);
            assertEquals(0, r2.length);
        }

        @Test
        void zipLong3D_bothEmpty() throws Exception {
            long[][][] r = Arrays.zip((long[][][]) null, (long[][][]) null, (x, y) -> x + y);
            assertEquals(0, r.length);
            long[][][] r2 = Arrays.zip((long[][][]) null, (long[][][]) null, 0L, 0L, (x, y) -> x + y);
            assertEquals(0, r2.length);
        }

        @Test
        void zipShortArrays_ternary3DWithDefaultValuesForTrailingRows() throws Exception {
            short[][][] first3d = { { { 1 } }, { { 2, 3 } } };
            short[][][] second3d = { { { 10, 20 } } };
            short[][][] third3d = { { { 100 } }, { { 30, 40 } }, { { 50 } } };

            short[][][] zipped3d = Arrays.zip(first3d, second3d, third3d, (short) 5, (short) 6, (short) 7, (x, y, z) -> (short) (x + y + z));

            assertArrayEquals(new short[] { 111, 32 }, zipped3d[0][0]);
            assertArrayEquals(new short[] { 38, 49 }, zipped3d[1][0]);
            assertArrayEquals(new short[] { 61 }, zipped3d[2][0]);
        }

        @Test
        void zipThree2DArrays() throws Exception {
            // Test with normal two-dimensional arrays
            long[][] a = { { 1, 2 }, { 3, 4, 5 } };
            long[][] b = { { 5, 6, 7 }, { 8, 9 } };
            long[][] c = { { 10, 11 }, { 12, 13, 14 } };
            long[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertEquals(2, result.length);
            assertArrayEquals(new long[] { 16, 19 }, result[0]);
            assertArrayEquals(new long[] { 23, 26 }, result[1]);
        }

        @Test
        void zipThree2DArraysDifferentLengths() throws Exception {
            // Test with different outer array lengths
            long[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            long[][] b = { { 7, 8 }, { 9, 10 } };
            long[][] c = { { 11, 12 } };
            long[][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertEquals(1, result.length);
            assertArrayEquals(new long[] { 19, 22 }, result[0]);
        }

        @Test
        void zipThree3DArrays() throws Exception {
            // Test with normal three-dimensional arrays
            long[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } } };
            long[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 }, { 70, 80 } } };
            long[][][] c = { { { 100, 200 }, { 300, 400 } }, { { 500, 600 }, { 700, 800 } } };
            long[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertArrayEquals(new long[] { 111, 222 }, result[0][0]);
            assertArrayEquals(new long[] { 333, 444 }, result[0][1]);
            assertArrayEquals(new long[] { 555, 666 }, result[1][0]);
            assertArrayEquals(new long[] { 777, 888 }, result[1][1]);
        }

        @Test
        void zipThree3DArraysDifferentLengths() throws Exception {
            // Test with different outer array lengths
            long[][][] a = { { { 1, 2 } }, { { 3, 4 } }, { { 5, 6 } } };
            long[][][] b = { { { 10, 20 } }, { { 30, 40 } } };
            long[][][] c = { { { 100, 200 } } };
            long[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertEquals(1, result.length);
        }

        @Test
        void zipThree3DArraysWithDefaults() throws Exception {
            // Test with three-dimensional arrays of different lengths using defaults
            long[][][] a = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 } } };
            long[][][] b = { { { 10, 20 }, { 30, 40 } }, { { 50, 60 }, { 70, 80 } }, { { 90 } } };
            long[][][] c = { { { 100, 200 } } };
            long[][][] result = Arrays.zip(a, b, c, 0L, 10L, 20L, (x, y, z) -> x + y + z);
            assertEquals(3, result.length);
            // First element uses all three arrays
            assertEquals(2, result[0].length);
            assertArrayEquals(new long[] { 111, 222 }, result[0][0]);
            // Second element uses defaults for c
            assertEquals(2, result[1].length);
            // Third element uses defaults for a and c
            assertEquals(1, result[2].length);
        }

        @Test
        void zipThree3DArraysWithDefaultsAllNull() throws Exception {
            // Test with all null arrays
            long[][][] result = Arrays.zip((long[][][]) null, (long[][][]) null, (long[][][]) null, 1L, 2L, 3L, (x, y, z) -> x + y + z);
            assertEquals(0, result.length);
        }

        @Test
        void zipThreeArrays() throws Exception {
            // Test with normal arrays
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6, 7, 8 };
            long[] c = { 9, 10, 11, 12 };
            long[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertArrayEquals(new long[] { 15, 18, 21, 24 }, result);
        }

        @Test
        void zipThreeArrays_values_1_2_3_4() throws Exception {
            // Test basic three-way zip
            Integer[][] a = { { 1, 2 }, { 3, 4 } };
            Integer[][] b = { { 10, 20 }, { 30, 40 } };
            Integer[][] c = { { 100, 200 }, { 300, 400 } };
            Integer[][] sums = ff.zip(a, b, c, (x, y, z) -> x + y + z);
            assertArrayEquals(new Integer[] { 111, 222 }, sums[0]);
            assertArrayEquals(new Integer[] { 333, 444 }, sums[1]);

            // Test with different sizes
            Integer[][] a2 = { { 1 }, { 2 } };
            Double[][] b2 = { { 1.5, 2.5 }, { 3.5 } };
            String[][] c2 = { { "A" }, { "B", "C" } };
            // Only first element of each row will be processed (minimum size)
            Integer[][] result = ff.zip(a2, b2, c2, (i, d, s) -> i);
            assertArrayEquals(new Integer[] { 1 }, result[0]);
            assertArrayEquals(new Integer[] { 2 }, result[1]);
        }

        @Test
        void zipThreeArraysDifferentLengths() throws Exception {
            // Test with arrays of different lengths - should use shortest length
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6, 7 };
            long[] c = { 8, 9 };
            long[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertArrayEquals(new long[] { 14, 17 }, result);
        }

        @Test
        void zipThreeArraysDifferentType() throws Exception {
            // Test combining three different types
            Integer[][][] nums = { { { 1 } } };
            String[][][] strs = { { { "a" } } };
            Double[][][] dbls = { { { 2.5 } } };
            String[][][] result = fff.zip(nums, strs, dbls, (n, s, d) -> n + s + d, String.class);
            assertEquals("1a2.5", result[0][0][0]);

            // Test more complex transformation
            Integer[][][] ints = { { { 1, 2 } }, { { 3, 4 } } };
            String[][][] strings = { { { "A", "B" } }, { { "C", "D" } } };
            Boolean[][][] bools = { { { true, false } }, { { false, true } } };
            String[][][] combined = fff.zip(ints, strings, bools, (i, s, b) -> String.format(java.util.Locale.ROOT, "%d-%s-%s", i, s, b ? "Y" : "N"),
                    String.class);
            assertEquals("1-A-Y", combined[0][0][0]);
            assertEquals("2-B-N", combined[0][0][1]);
            assertEquals("3-C-N", combined[1][0][0]);
            assertEquals("4-D-Y", combined[1][0][1]);
        }

        @Test
        void zipThreeArraysEmptyArrays() throws Exception {
            // Test with empty arrays
            long[] a = { 1, 2, 3 };
            long[] b = {};
            long[] c = { 7, 8, 9 };
            long[] result = Arrays.zip(a, b, c, (x, y, z) -> x + y + z);
            assertArrayEquals(new long[] {}, result);
        }

        @Test
        void zipThreeArraysSameType() throws Exception {
            // Test basic three-way zip
            Integer[][][] a = { { { 1, 2 } } };
            Integer[][][] b = { { { 10, 20 } } };
            Integer[][][] c = { { { 100, 200 } } };
            Integer[][][] sum = fff.zip(a, b, c, (x, y, z) -> x + y + z);
            assertEquals(111, sum[0][0][0]);
            assertEquals(222, sum[0][0][1]);

            // Test with different sizes (truncation to minimum)
            Integer[][][] a2 = { { { 1, 2 } }, { { 3, 4 } } };
            Integer[][][] b2 = { { { 10, 20 } } };
            Integer[][][] c2 = { { { 100, 200 } }, { { 300, 400 } }, { { 500, 600 } } };
            Integer[][][] sum2 = fff.zip(a2, b2, c2, (x, y, z) -> x + y + z);
            assertEquals(1, sum2.length);
            assertEquals(111, sum2[0][0][0]);
            assertEquals(222, sum2[0][0][1]);
        }

        @Test
        void zipThreeArraysWithDefaults() throws Exception {
            // Test with arrays of different lengths using defaults
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6 };
            long[] c = { 8, 9, 10 };
            long[] result = Arrays.zip(a, b, c, 0L, 10L, 20L, (x, y, z) -> x + y + z);
            assertArrayEquals(new long[] { 14, 17, 23, 34 }, result);
        }

        @Test
        void zipThreeArraysWithDefaults_values_1() throws Exception {
            // Test with different sizes and defaults
            Integer[][][] a = { { { 1 } } };
            Integer[][][] b = { { { 10, 20 } } };
            Integer[][][] c = { { {}, { 100 } } };
            Integer[][][] result = fff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
            Arrays.println(result);
            assertEquals(1, result.length);
            assertEquals(11, result[0][0][0]);
            assertEquals(20, result[0][0][1]);
            assertEquals(100, result[0][1][0]);

            // Test with all arrays of different sizes
            Integer[][][] a2 = { { { 1, 2 } }, { { 3 } } };
            Integer[][][] b2 = { { { 10 } } };
            Integer[][][] c2 = { { { 100, 200, 300 } } };
            Integer[][][] sum = fff.zip(a2, b2, c2, -1, -10, -100, (x, y, z) -> x + y + z);
            assertEquals(2, sum.length);
            assertEquals(111, sum[0][0][0]);
            assertEquals(192, sum[0][0][1]);
            assertEquals(289, sum[0][0][2]);
            assertEquals(-107, sum[1][0][0]);
        }

        @Test
        void zipThreeArraysWithDefaults_values_1_2_3() throws Exception {
            // Test with defaults for missing elements
            Integer[][] a = { { 1 }, { 2, 3 } };
            Integer[][] b = { { 10, 20 } };
            Integer[][] c = { { 100 }, { 200, 300 } };
            Integer[][] sums = ff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
            assertArrayEquals(new Integer[] { 111, 20 }, sums[0]);
            assertArrayEquals(new Integer[] { 202, 303 }, sums[1]);

            // Test with different row counts
            Integer[][] a2 = { { 1 } };
            Integer[][] b2 = { { 10 }, { 20 } };
            Integer[][] c2 = { { 100 }, { 200 }, { 300 } };
            Integer[][] result = ff.zip(a2, b2, c2, 0, 0, 0, (x, y, z) -> x + y + z);
            assertEquals(3, result.length);
            assertArrayEquals(new Integer[] { 111 }, result[0]);
            assertArrayEquals(new Integer[] { 220 }, result[1]);
            assertArrayEquals(new Integer[] { 300 }, result[2]);
        }

        @Test
        void zipThreeArraysWithDefaultsAllDifferent() throws Exception {
            // Test with all arrays of different lengths
            long[] a = { 1 };
            long[] b = { 5, 6 };
            long[] c = { 8, 9, 10 };
            long[] result = Arrays.zip(a, b, c, 10, 20, 30, (x, y, z) -> x + y + z);
            assertArrayEquals(new long[] { 14, 25, 40 }, result);
        }

        @Test
        void zipThreeArraysWithDefaultsAndTargetType() throws Exception {
            // Test with type conversion and defaults
            Integer[][] a = { { 1 } };
            String[][] b = { { "X", "Y" } };
            Double[][] c = { { 0.5 }, { 1.5, 2.5 } };
            String[][] result = ff.zip(a, b, c, 0, "-", 0.0, (i, s, d) -> i + s + d, String.class);
            assertArrayEquals(new String[] { "1X0.5", "0Y0.0" }, result[0]);
            assertArrayEquals(new String[] { "0-1.5", "0-2.5" }, result[1]);

            // Test with all null inputs
            Integer[][] nullA = null;
            String[][] nullB = null;
            Double[][] nullC = null;
            String[][] result2 = ff.zip(nullA, nullB, nullC, 1, "A", 1.0, (i, s, d) -> i + s + d, String.class);
            assertEquals(0, result2.length);
        }

        @Test
        void zipThreeArraysWithDefaultsDifferentType() throws Exception {
            // Test type conversion with defaults
            Integer[][][] a = { { { 1 } } };
            String[][][] b = { { { "a", "b" } } };
            Double[][][] c = { { { 1.1 } }, { { 2.2 } } };
            String[][][] result = fff.zip(a, b, c, 0, "x", 0.0, (i, s, d) -> i + s + d, String.class);
            assertEquals(2, result.length);
            assertEquals("1a1.1", result[0][0][0]);
            assertEquals("0b0.0", result[0][0][1]);
            assertEquals("0x2.2", result[1][0][0]);

            // Test with complex default handling
            Integer[][][] ints = { { { 1 } } };
            String[][][] strs = { { { "A", "B" } }, { { "C" } } };
            Boolean[][][] bools = { { { true } }, { { false, true } }, { { false } } };
            String[][][] combined = fff.zip(ints, strs, bools, -1, "?", false,
                    (i, s, f) -> String.format(java.util.Locale.ROOT, "[%d,%s,%s]", i, s, f ? "T" : "F"), String.class);
            assertEquals(3, combined.length);
            assertEquals("[1,A,T]", combined[0][0][0]);
            assertEquals("[-1,B,F]", combined[0][0][1]);
            assertEquals("[-1,C,F]", combined[1][0][0]);
            assertEquals("[-1,?,T]", combined[1][0][1]);
            assertEquals("[-1,?,F]", combined[2][0][0]);
        }

        @Test
        void zipThreeArraysWithDefaultsNullArrays() throws Exception {
            // Test with null arrays and defaults
            long[] result = Arrays.zip((long[]) null, (long[]) null, (long[]) null, 10L, 20L, 30L, (x, y, z) -> x + y + z);
            assertArrayEquals(new long[] {}, result);

            // Test with one null
            long[] a = { 1, 2 };
            long[] result2 = Arrays.zip(a, null, null, 10L, 20L, 30L, (x, y, z) -> x + y + z);
            assertArrayEquals(new long[] { 51, 52 }, result2);
        }

        @Test
        void zipThreeArraysWithNull() throws Exception {
            // Test with null arrays
            long[] result = Arrays.zip((long[]) null, (long[]) null, (long[]) null, (x, y, z) -> x + y + z);
            assertArrayEquals(new long[] {}, result);

            // Test with one null
            long[] a = { 1, 2, 3 };
            long[] b = { 4, 5, 6 };
            long[] result2 = Arrays.zip(a, b, null, (x, y, z) -> x + y + z);
            assertArrayEquals(new long[] {}, result2);
        }

        @Test
        void zipThreeArraysWithTargetType() throws Exception {
            // Test type conversion with three arrays
            Integer[][] a = { { 1 }, { 2 } };
            Double[][] b = { { 1.5, 2.5 }, { 3.5 } };
            String[][] c = { { "A" }, { "B", "C" } };
            String[][] result = ff.zip(a, b, c, (i, d, s) -> i + ":" + d + ":" + s, String.class);
            assertArrayEquals(new String[] { "1:1.5:A" }, result[0]);
            assertArrayEquals(new String[] { "2:3.5:B" }, result[1]);

            // Test with different row counts
            Integer[][] a2 = { { 1, 2 } };
            Double[][] b2 = { { 1.1, 2.2 }, { 3.3 } };
            String[][] c2 = { { "X", "Y" }, { "Z" } };
            String[][] result2 = ff.zip(a2, b2, c2, (i, d, s) -> i + "-" + d + "-" + s, String.class);
            assertEquals(1, result2.length); // Minimum row count
            assertArrayEquals(new String[] { "1-1.1-X", "2-2.2-Y" }, result2[0]);
        }

        @Test
        void zipThreeChar2DArrays() throws Exception {
            // Test normal case
            char[][] a = { { 'A', 'B' }, { 'C', 'D', 'E' } };
            char[][] b = { { 'X', 'Y', 'Z' }, { '1', '2' } };
            char[][] c = { { 'a', 'b' }, { 'c', 'd', 'e' } };
            char[][] result = Arrays.zip(a, b, c, (x, y, z) -> (char) ((x + y + z) / 3));
            assertEquals(2, result.length);
            assertArrayEquals(new char[][] { { 'S', 'T' }, { 'G', 'H' } }, result);
        }

        @Test
        void zipThreeChar2DArraysDifferentOuterLengths() throws Exception {
            // Test with arrays of different outer lengths
            char[][] a = { { 'A', 'B' }, { 'C', 'D' }, { 'E', 'F' } };
            char[][] b = { { 'X', 'Y' } };
            char[][] c = { { 'a', 'b' }, { 'c', 'd' } };
            char[][] result = Arrays.zip(a, b, c, (x, y, z) -> x);
            assertEquals(1, result.length);
            assertArrayEquals(new char[] { 'A', 'B' }, result[0]);
        }

        @Test
        void zipThreeChar2DArraysWithDefaults() throws Exception {
            // Test with different outer lengths
            char[][] a = { { 'A', 'B' }, { 'C', 'D', 'E' } };
            char[][] b = { { 'X', 'Y', 'Z' }, { '1', '2' }, { '3' } };
            char[][] c = { { 'a', 'b' } };
            char[][] result = Arrays.zip(a, b, c, '?', '!', '~', (x, y, z) -> (char) ((x + y + z) / 3));
            assertEquals(3, result.length);
            assertArrayEquals(new char[][] { { 'S', 'T', ']' }, { 'P', 'Q', 'L' }, { 'P' } }, result);
        }

        @Test
        void zipThreeChar2DArraysWithDefaultsNulls() throws Exception {
            // Test with all null arrays
            char[][] result = Arrays.zip((char[][]) null, null, null, '?', '!', '~', (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            assertEquals(0, result.length);

            // Test with mixed null and non-null arrays
            char[][] a = { { 'A', 'B' } };
            char[][] b = null;
            char[][] c = { { 'a', 'b' }, { 'c' } };
            char[][] result2 = Arrays.zip(a, b, c, '?', '!', '~', (x, y, z) -> y);
            assertEquals(2, result2.length);
            assertArrayEquals(new char[] { '!', '!' }, result2[0]);
            assertArrayEquals(new char[] { '!' }, result2[1]);
        }

        @Test
        void zipThreeChar2DArraysWithNulls() throws Exception {
            // Test with null arrays
            char[][] result = Arrays.zip((char[][]) null, null, null, (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            assertEquals(0, result.length);

            // Test with one null array
            char[][] a = { { 'A', 'B' } };
            char[][] b = null;
            char[][] c = { { 'a', 'b' } };
            char[][] result2 = Arrays.zip(a, b, c, (x, y, z) -> x);
            assertEquals(0, result2.length);
        }

        @Test
        void zipThreeChar3DArrays() throws Exception {
            // Test normal case
            char[][][] a = { { { 'A', 'B' } } };
            char[][][] b = { { { '1', '2' }, { '3', '4' } } };
            char[][][] c = { { { 'a', 'b' } } };
            char[][][] result = Arrays.zip(a, b, c, (x, y, z) -> (char) ((x + y + z) / 3));
            assertEquals(1, result.length);
            assertEquals(1, result[0].length);
            assertArrayEquals(new char[] { 'F', 'G' }, result[0][0]);
        }

        @Test
        void zipThreeChar3DArraysDifferentOuterLengths() throws Exception {
            // Test with different outer lengths
            char[][][] a = { { { 'A' } }, { { 'B' } }, { { 'C' } } };
            char[][][] b = { { { '1' } } };
            char[][][] c = { { { 'a' } }, { { 'b' } } };
            char[][][] result = Arrays.zip(a, b, c, (x, y, z) -> x);
            assertEquals(1, result.length);
        }

        @Test
        void zipThreeChar3DArraysWithDefaults() throws Exception {
            char[][][] a = { { { 'A', 'B' } } };
            char[][][] b = { { { '1', '2' }, { '3', '4' } } };
            char[][][] c = { { { 'a', 'b' } } };
            char[][][] result = Arrays.zip(a, b, c, '?', '!', '~', (x, y, z) -> (char) ((x + y + z) / 3));
            assertEquals(1, result.length);
            assertEquals(2, result[0].length);
            assertArrayEquals(new char[][][] { { { 'F', 'G' }, { 'P', 'P' } } }, result);
        }

        @Test
        void zipThreeChar3DArraysWithDefaultsAllNulls() throws Exception {
            // Test with all null arrays
            char[][][] result = Arrays.zip((char[][][]) null, null, null, '?', '!', '~', (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            assertEquals(0, result.length);
        }

        @Test
        void zipThreeChar3DArraysWithNulls() throws Exception {
            // Test with all null arrays
            char[][][] result = Arrays.zip((char[][][]) null, null, null, (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            assertEquals(0, result.length);
        }

        @Test
        void zipThreeCharArrays() throws Exception {
            // Test normal case with equal length arrays
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y', 'Z' };
            char[] c = { '1', '3' };
            char[] result = Arrays.zip(a, b, c, (x, y, z) -> (char) (x + y + z - 'A' - '0'));
            assertArrayEquals(new char[] { 'Y', ']' }, result);
        }

        @Test
        void zipThreeCharArraysDifferentLengths() throws Exception {
            // Test with arrays of different lengths
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y', 'Z' };
            char[] c = { '1', '2' };
            char[] result = Arrays.zip(a, b, c, (x, y, z) -> (char) (x + y + z - 'A' - '0'));
            assertArrayEquals(new char[] { 'Y', '\\' }, result);
        }

        @Test
        void zipThreeCharArraysEmptyArrays() throws Exception {
            // Test with one empty array
            char[] a = { 'A', 'B' };
            char[] b = { 'X', 'Y' };
            char[] c = {};
            char[] result = Arrays.zip(a, b, c, (x, y, z) -> x);
            assertArrayEquals(new char[] {}, result);

            // Test with all empty arrays
            char[] a2 = {};
            char[] b2 = {};
            char[] c2 = {};
            char[] result2 = Arrays.zip(a2, b2, c2, (x, y, z) -> x);
            assertArrayEquals(new char[] {}, result2);
        }

        @Test
        void zipThreeCharArraysWithDefaultsEmptyArrays() throws Exception {
            // Test with mixed empty and non-empty arrays
            char[] a = { 'A', 'B' };
            char[] b = {};
            char[] c = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, c, '?', '!', '0', (x, y, z) -> x);
            assertArrayEquals(new char[] { 'A', 'B', '?' }, result);
        }

        @Test
        void zipThreeCharArraysWithDefaultsEqualLength() throws Exception {
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y' };
            char[] c = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, c, '?', '!', '0', (x, y, z) -> (char) (x + y + z - 'A' - '0'));
            assertArrayEquals(new char[] { 'Y', '\\', '&', '$' }, result);
        }

        @Test
        void zipThreeCharArraysWithDefaultsNulls() throws Exception {
            // Test with one null array
            char[] a = null;
            char[] b = { 'X', 'Y', 'Z' };
            char[] c = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, c, '?', '!', '0', (x, y, z) -> y);
            assertArrayEquals(new char[] { 'X', 'Y', 'Z' }, result);

            // Test with all null arrays
            char[] result2 = Arrays.zip((char[]) null, null, null, '?', '!', '0', (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            assertArrayEquals(new char[] {}, result2);
        }

        @Test
        void zipThreeCharArraysWithNulls() throws Exception {
            // Test with one null array
            char[] a = { 'A', 'B', 'C' };
            char[] b = null;
            char[] c = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, c, (x, y, z) -> x);
            assertArrayEquals(new char[] {}, result);

            // Test with all null arrays
            char[] result2 = Arrays.zip((char[]) null, null, null, (Throwables.CharTernaryOperator<Exception>) (x, y, z) -> x);
            assertArrayEquals(new char[] {}, result2);
        }

        @Test
        void zipTwoArrays() throws Exception {
            // Test with normal arrays
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6, 7, 8 };
            long[] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new long[] { 6, 8, 10, 12 }, result);
        }

        @Test
        void zipTwoArrays_values_1_2_3_4() throws Exception {
            // Test basic zip
            Integer[][] a = { { 1, 2 }, { 3, 4 } };
            Integer[][] b = { { 10, 20 }, { 30, 40 } };
            Integer[][] sums = ff.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new Integer[] { 11, 22 }, sums[0]);
            assertArrayEquals(new Integer[] { 33, 44 }, sums[1]);

            // Test different sizes - extra elements ignored
            Integer[][] a2 = { { 1, 2, 3 }, { 4, 5 } };
            Integer[][] b2 = { { 10 }, { 20, 30 } };
            Integer[][] sums2 = ff.zip(a2, b2, (x, y) -> x + y);
            assertArrayEquals(new Integer[] { 11 }, sums2[0]);
            assertArrayEquals(new Integer[] { 24, 35 }, sums2[1]);

            // Test different number of rows
            Integer[][] a3 = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            Integer[][] b3 = { { 10, 20 }, { 30, 40 } };
            Integer[][] sums3 = ff.zip(a3, b3, (x, y) -> x + y);
            assertEquals(2, sums3.length);
            assertArrayEquals(new Integer[] { 11, 22 }, sums3[0]);
            assertArrayEquals(new Integer[] { 33, 44 }, sums3[1]);
        }

        @Test
        void zipTwoArraysDifferentLengths() throws Exception {
            // Test with arrays of different lengths - should use shorter length
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6, 7 };
            long[] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new long[] { 6, 8, 10 }, result);

            // Test with first array shorter
            long[] a2 = { 1, 2 };
            long[] b2 = { 5, 6, 7, 8 };
            long[] result2 = Arrays.zip(a2, b2, (x, y) -> x * y);
            assertArrayEquals(new long[] { 5, 12 }, result2);
        }

        @Test
        void zipTwoArraysDifferentType() throws Exception {
            // Test combining different types
            Integer[][][] numbers = { { { 1, 2 } } };
            String[][][] strings = { { { "a", "b" } } };
            String[][][] combined = fff.zip(numbers, strings, (n, s) -> n + s, String.class);
            assertEquals("1a", combined[0][0][0]);
            assertEquals("2b", combined[0][0][1]);

            // Test with more complex transformation
            Double[][][] doubles = { { { 1.5, 2.5 } }, { { 3.5, 4.5 } } };
            Integer[][][] ints = { { { 1, 2 } }, { { 3, 4 } } };
            String[][][] result = fff.zip(doubles, ints, (d, i) -> String.format(java.util.Locale.ROOT, "%.1f:%d", d, i), String.class);
            assertEquals("1.5:1", result[0][0][0]);
            assertEquals("2.5:2", result[0][0][1]);
            assertEquals("3.5:3", result[1][0][0]);
            assertEquals("4.5:4", result[1][0][1]);
        }

        @Test
        void zipTwoArraysEmptyArrays() throws Exception {
            // Test with empty arrays
            long[] a = {};
            long[] b = { 5, 6, 7 };
            long[] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new long[] {}, result);

            // Test with both empty
            long[] result2 = Arrays.zip(new long[] {}, new long[] {}, (x, y) -> x + y);
            assertArrayEquals(new long[] {}, result2);
        }

        @Test
        void zipTwoArraysSameType() throws Exception {
            // Test basic zip with addition
            Integer[][][] a = { { { 1, 2 } }, { { 3, 4 } } };
            Integer[][][] b = { { { 10, 20 } }, { { 30, 40 } } };
            Integer[][][] sum = fff.zip(a, b, (x, y) -> x + y);
            assertEquals(11, sum[0][0][0]);
            assertEquals(22, sum[0][0][1]);
            assertEquals(33, sum[1][0][0]);
            assertEquals(44, sum[1][0][1]);

            // Test with different sizes (truncation)
            Integer[][][] a2 = { { { 1, 2 } }, { { 3, 4 } }, { { 5, 6 } } };
            Integer[][][] b2 = { { { 10, 20 } }, { { 30, 40 } } };
            Integer[][][] sum2 = fff.zip(a2, b2, (x, y) -> x + y);
            assertEquals(2, sum2.length);
            assertEquals(11, sum2[0][0][0]);
            assertEquals(22, sum2[0][0][1]);
            assertEquals(33, sum2[1][0][0]);
            assertEquals(44, sum2[1][0][1]);

            // Test String concatenation
            String[][][] strA = { { { "a", "b" } }, { { "c", "d" } } };
            String[][][] strB = { { { "1", "2" } }, { { "3", "4" } } };
            String[][][] concat = fff.zip(strA, strB, (s1, s2) -> s1 + s2);
            assertEquals("a1", concat[0][0][0]);
            assertEquals("b2", concat[0][0][1]);
            assertEquals("c3", concat[1][0][0]);
            assertEquals("d4", concat[1][0][1]);
        }

        @Test
        void zipTwoArraysWithDefaults() throws Exception {
            // Test with arrays of different lengths using defaults
            long[] a = { 1, 2, 3, 4 };
            long[] b = { 5, 6 };
            long[] result = Arrays.zip(a, b, 0L, 10L, (x, y) -> x + y);
            assertArrayEquals(new long[] { 6, 8, 13, 14 }, result);
        }

        @Test
        void zipTwoArraysWithDefaults_values_1_2() throws Exception {
            // Test with different sizes and defaults
            Integer[][][] a = { { { 1, 2 } } };
            Integer[][][] b = { { { 10 } }, { { 20, 30 } } };
            Integer[][][] result = fff.zip(a, b, 0, 0, (x, y) -> x + y);
            assertEquals(2, result.length);
            assertEquals(11, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
            assertEquals(20, result[1][0][0]);
            assertEquals(30, result[1][0][1]);

            // Test String arrays with defaults
            String[][][] strA = { { { "a" } } };
            String[][][] strB = { { { "1", "2" } }, { { "3" } } };
            String[][][] strResult = fff.zip(strA, strB, "X", "Y", (s1, s2) -> s1 + s2);
            assertEquals(2, strResult.length);
            assertEquals("a1", strResult[0][0][0]);
            assertEquals("X2", strResult[0][0][1]);
            assertEquals("X3", strResult[1][0][0]);
        }

        @Test
        void zipTwoArraysWithDefaults_values_1_2_3() throws Exception {
            // Test with defaults for missing elements
            Integer[][] a = { { 1, 2 }, { 3 } };
            Integer[][] b = { { 10 }, { 30, 40 } };
            Integer[][] sums = ff.zip(a, b, 0, 0, (x, y) -> x + y);
            assertArrayEquals(new Integer[] { 11, 2 }, sums[0]);
            assertArrayEquals(new Integer[] { 33, 40 }, sums[1]);

            // Test with different row counts
            Integer[][] a3 = { { 1 } };
            Integer[][] b3 = { { 10 }, { 20 } };
            Integer[][] result2 = ff.zip(a3, b3, 0, 0, (x, y) -> x + y);
            assertEquals(2, result2.length);
            assertArrayEquals(new Integer[] { 11 }, result2[0]);
            assertArrayEquals(new Integer[] { 20 }, result2[1]);
        }

        @Test
        void zipTwoArraysWithDefaultsAndTargetType() throws Exception {
            // Test with type conversion and defaults
            Integer[][] a = { { 1 }, { 2, 3 } };
            String[][] b = { { "X", "Y" }, { "Z" } };
            String[][] result = ff.zip(a, b, 0, "-", (i, s) -> i + s, String.class);
            assertArrayEquals(new String[] { "1X", "0Y" }, result[0]);
            assertArrayEquals(new String[] { "2Z", "3-" }, result[1]);

            // Test with different row counts
            Integer[][] a2 = { { 1 } };
            String[][] b2 = { { "A" }, { "B" } };
            String[][] result2 = ff.zip(a2, b2, 0, "?", (i, s) -> i + s, String.class);
            assertEquals(2, result2.length);
            assertArrayEquals(new String[] { "1A" }, result2[0]);
            assertArrayEquals(new String[] { "0B" }, result2[1]);
        }

        @Test
        void zipTwoArraysWithDefaultsDifferentType() throws Exception {
            // Test type conversion with defaults
            Integer[][][] nums = { { { 1 } } };
            String[][][] strs = { { { "a", "b" } }, { { "c" } } };
            String[][][] result = fff.zip(nums, strs, 0, "x", (n, s) -> n + "-" + s, String.class);
            assertEquals(2, result.length);
            assertEquals("1-a", result[0][0][0]);
            assertEquals("0-b", result[0][0][1]);
            assertEquals("0-c", result[1][0][0]);

            // Test with all arrays needing defaults
            Integer[][][] shortArr = { { { 1 } } };
            Double[][][] longArr = { { { 1.1 } }, { { 2.2, 3.3 } }, { { 4.4 } } };
            String[][][] combined = fff.zip(shortArr, longArr, -1, 0.0, (i, d) -> String.format(java.util.Locale.ROOT, "%d:%.1f", i, d), String.class);
            assertEquals(3, combined.length);
            assertEquals("1:1.1", combined[0][0][0]);
            assertEquals("-1:2.2", combined[1][0][0]);
            assertEquals("-1:3.3", combined[1][0][1]);
            assertEquals("-1:4.4", combined[2][0][0]);
        }

        @Test
        void zipTwoArraysWithDefaultsEqualLength() throws Exception {
            // Test with equal length arrays - defaults not used
            long[] a = { 1, 2, 3 };
            long[] b = { 5, 6, 7 };
            long[] result = Arrays.zip(a, b, 100L, 200L, (x, y) -> x * y);
            assertArrayEquals(new long[] { 5, 12, 21 }, result);
        }

        @Test
        void zipTwoArraysWithDefaultsFirstShorter() throws Exception {
            // Test with first array shorter
            long[] a = { 1, 2 };
            long[] b = { 5, 6, 7, 8 };
            long[] result = Arrays.zip(a, b, 100L, 200L, (x, y) -> x + y);
            assertArrayEquals(new long[] { 6, 8, 107, 108 }, result);
        }

        @Test
        void zipTwoArraysWithDefaultsNullArrays() throws Exception {
            // Test with null arrays and defaults
            long[] result = Arrays.zip((long[]) null, (long[]) null, 10L, 20L, (x, y) -> x + y);
            assertArrayEquals(new long[] {}, result);

            // Test with first null
            long[] b = { 5, 6, 7 };
            long[] result2 = Arrays.zip(null, b, 10L, 20L, (x, y) -> x + y);
            assertArrayEquals(new long[] { 15, 16, 17 }, result2);

            // Test with second null
            long[] a = { 1, 2, 3 };
            long[] result3 = Arrays.zip(a, null, 10L, 20L, (x, y) -> x + y);
            assertArrayEquals(new long[] { 21, 22, 23 }, result3);
        }

        @Test
        void zipTwoArraysWithNull() throws Exception {
            // Test with null arrays
            long[] a = null;
            long[] b = { 5, 6, 7 };
            long[] result = Arrays.zip(a, b, (x, y) -> x + y);
            assertArrayEquals(new long[] {}, result);

            // Test with both null
            long[] result2 = Arrays.zip((long[]) null, (long[]) null, (x, y) -> x + y);
            assertArrayEquals(new long[] {}, result2);

            // Test with second null
            long[] a3 = { 1, 2, 3 };
            long[] result3 = Arrays.zip(a3, null, (x, y) -> x + y);
            assertArrayEquals(new long[] {}, result3);
        }

        @Test
        void zipTwoArraysWithTargetType() throws Exception {
            // Test type conversion
            Integer[][] a = { { 1, 2 }, { 3, 4 } };
            String[][] b = { { "a", "b" }, { "c", "d" } };
            String[][] combined = ff.zip(a, b, (i, s) -> i + s, String.class);
            assertArrayEquals(new String[] { "1a", "2b" }, combined[0]);
            assertArrayEquals(new String[] { "3c", "4d" }, combined[1]);

            // Test with different sizes
            Integer[][] a2 = { { 1 }, { 2, 3 } };
            String[][] b2 = { { "X", "Y" }, { "Z" } };
            String[][] combined2 = ff.zip(a2, b2, (i, s) -> i + s, String.class);
            assertArrayEquals(new String[] { "1X" }, combined2[0]);
            assertArrayEquals(new String[] { "2Z" }, combined2[1]);
        }

        @Test
        void zipTwoChar2DArrays() throws Exception {
            // Test normal case
            char[][] a = { { 'A', 'B' }, { 'C', 'D', 'E' } };
            char[][] b = { { 'X', 'Y', 'Z' }, { '1', '2' } };
            char[][] result = Arrays.zip(a, b, (x, y) -> (char) (x + y - 'A'));
            assertArrayEquals(new char[][] { { 'X', 'Z' }, { '3', '5' } }, result);
        }

        @Test
        void zipTwoChar2DArraysDifferentOuterLengths() throws Exception {
            // Test with first array longer
            char[][] a = { { 'A', 'B' }, { 'C', 'D' }, { 'E', 'F' } };
            char[][] b = { { 'X', 'Y' }, { '1', '2' } };
            char[][] result = Arrays.zip(a, b, (x, y) -> (char) (x + y - 'A'));
            assertEquals(2, result.length);
            assertArrayEquals(new char[] { 'X', 'Z' }, result[0]);
            assertArrayEquals(new char[] { '3', '5' }, result[1]);
        }

        @Test
        void zipTwoChar2DArraysEmptyArrays() throws Exception {
            // Test with empty outer arrays
            char[][] a = {};
            char[][] b = {};
            char[][] result = Arrays.zip(a, b, (x, y) -> x);
            assertEquals(0, result.length);

            // Test with empty inner arrays
            char[][] a2 = { {}, { 'A', 'B' } };
            char[][] b2 = { { 'X', 'Y' }, {} };
            char[][] result2 = Arrays.zip(a2, b2, (x, y) -> x);
            assertEquals(2, result2.length);
            assertArrayEquals(new char[] {}, result2[0]);
            assertArrayEquals(new char[] {}, result2[1]);
        }

        @Test
        void zipTwoChar2DArraysWithDefaults() throws Exception {
            // Test with different outer lengths
            char[][] a = { { 'A', 'B' }, { 'C', 'D', 'E' } };
            char[][] b = { { 'X', 'Y', 'Z' }, { '1', '2' }, { '3' } };
            char[][] result = Arrays.zip(a, b, '?', '!', (x, y) -> (char) (x + y - 'A'));
            // result: {{'X', 'Z', 'X'}, {'3', '5', '%'}, {'1'}}
            assertEquals(3, result.length);
            assertArrayEquals(new char[][] { { 'X', 'Z', 'X' }, { '3', '5', '%' }, { '1' } }, result);
        }

        @Test
        void zipTwoChar2DArraysWithDefaultsNulls() throws Exception {
            // Test with null outer array
            char[][] a = null;
            char[][] b = { { 'X', 'Y' }, { '1', '2' } };
            char[][] result = Arrays.zip(a, b, '?', '!', (x, y) -> y);
            assertEquals(2, result.length);
            assertArrayEquals(new char[] { 'X', 'Y' }, result[0]);
            assertArrayEquals(new char[] { '1', '2' }, result[1]);

            // Test with null inner arrays
            char[][] a2 = { { 'A', 'B' }, null };
            char[][] b2 = { null, { '1', '2' } };
            char[][] result2 = Arrays.zip(a2, b2, '?', '!', (x, y) -> x);
            assertEquals(2, result2.length);
            assertArrayEquals(new char[] { 'A', 'B' }, result2[0]);
            assertArrayEquals(new char[] { '?', '?' }, result2[1]);
        }

        @Test
        void zipTwoChar2DArraysWithNulls() throws Exception {
            // Test with null outer array
            char[][] a = null;
            char[][] b = { { 'X', 'Y' }, { '1', '2' } };
            char[][] result = Arrays.zip(a, b, (x, y) -> y);
            assertEquals(0, result.length);

            // Test with null inner arrays
            char[][] a2 = { { 'A', 'B' }, null };
            char[][] b2 = { null, { '1', '2' } };
            char[][] result2 = Arrays.zip(a2, b2, (x, y) -> 'Z');
            assertEquals(2, result2.length);
            assertArrayEquals(new char[] {}, result2[0]);
            assertArrayEquals(new char[] {}, result2[1]);
        }

        @Test
        void zipTwoChar3DArrays() throws Exception {
            // Test normal case
            char[][][] a = { { { 'A', 'B' }, { 'C', 'D' } }, { { 'E', 'F' }, { 'G', 'H' } } };
            char[][][] b = { { { '1', '2' }, { '3', '4' } }, { { '5', '6' }, { '7', '8' } } };
            char[][][] result = Arrays.zip(a, b, (x, y) -> (char) (x + y - 'A'));
            // result: {{{'1', '3'}, {'5', '7'}}, {{'9', ';'}, {'=', '?'}}}
            assertEquals(2, result.length);
            assertArrayEquals(new char[][][] { { { '1', '3' }, { '5', '7' } }, { { '9', ';' }, { '=', '?' } } }, result);
        }

        @Test
        void zipTwoChar3DArraysDifferentOuterLengths() throws Exception {
            // Test with first array longer
            char[][][] a = { { { 'A', 'B' } }, { { 'C', 'D' } }, { { 'E', 'F' } } };
            char[][][] b = { { { '1', '2' } }, { { '3', '4' } } };
            char[][][] result = Arrays.zip(a, b, (x, y) -> y);
            assertEquals(2, result.length);
        }

        @Test
        void zipTwoChar3DArraysWithDefaults() throws Exception {
            // Test with different outer lengths
            char[][][] a = { { { 'A', 'B' }, { 'C', 'D' } }, { { 'E', 'F' } } };
            char[][][] b = { { { '1', '2' }, { '3', '4' } }, { { '5', '6' }, { '7', '8' } }, { { '9' } } };
            char[][][] result = Arrays.zip(a, b, 'A', '0', (x, y) -> (char) (x + y - 'A'));
            // result: {{{'1', '3'}, {'5', '7'}}, {{'9', ';'}, {'7', '8'}}, {{'9'}}}
            assertEquals(3, result.length);
            assertArrayEquals(new char[][][] { { { '1', '3' }, { '5', '7' } }, { { '9', ';' }, { '7', '8' } }, { { '9' } } }, result);
        }

        @Test
        void zipTwoChar3DArraysWithDefaultsNulls() throws Exception {
            // Test with null outer array
            char[][][] a = null;
            char[][][] b = { { { '1', '2' } } };
            char[][][] result = Arrays.zip(a, b, '?', '!', (x, y) -> y);
            assertEquals(1, result.length);
            assertEquals(1, result[0].length);
            assertArrayEquals(new char[] { '1', '2' }, result[0][0]);
        }

        @Test
        void zipTwoChar3DArraysWithNulls() throws Exception {
            // Test with null arrays
            char[][][] result = Arrays.zip((char[][][]) null, null, (Throwables.CharBinaryOperator<Exception>) (x, y) -> x);
            assertEquals(0, result.length);

            // Test with null sub-arrays
            char[][][] a = { { { 'A', 'B' } }, null };
            char[][][] b = { null, { { '1', '2' } } };
            char[][][] result2 = Arrays.zip(a, b, (x, y) -> 'Z');
            assertEquals(2, result2.length);
            assertEquals(0, result2[0].length);
            assertEquals(0, result2[1].length);
        }

        @Test
        void zipTwoCharArrays() throws Exception {
            // Test normal case with equal length arrays
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y', 'Z' };
            char[] result = Arrays.zip(a, b, (x, y) -> (char) (x + y - 'A'));
            assertArrayEquals(new char[] { 'X', 'Z', '\\' }, result);
        }

        @Test
        void zipTwoCharArraysEmptyArrays() throws Exception {
            // Test with empty arrays
            char[] a = {};
            char[] b = {};
            char[] result = Arrays.zip(a, b, (x, y) -> (char) (x + y));
            assertArrayEquals(new char[] {}, result);

            // Test with one empty array
            char[] a2 = { 'A', 'B' };
            char[] b2 = {};
            char[] result2 = Arrays.zip(a2, b2, (x, y) -> (char) (x + y));
            assertArrayEquals(new char[] {}, result2);
        }

        @Test
        void zipTwoCharArraysException() {
            char[] a = { 'A', 'B', 'C' };
            char[] b = { 'X', 'Y', 'Z' };

            assertThrows(Exception.class, () -> {
                Arrays.zip(a, b, (Throwables.CharBinaryOperator<Exception>) (x, y) -> {
                    throw new Exception("Test exception");
                });
            });
        }

        @Test
        void zipTwoCharArraysWithDefaultsEmptyArrays() throws Exception {
            // Test with one empty array
            char[] a = { 'A', 'B' };
            char[] b = {};
            char[] result = Arrays.zip(a, b, '?', '!', (x, y) -> x);
            assertArrayEquals(new char[] { 'A', 'B' }, result);

            // Test with both empty arrays
            char[] a2 = {};
            char[] b2 = {};
            char[] result2 = Arrays.zip(a2, b2, '?', '!', (x, y) -> x);
            assertArrayEquals(new char[] {}, result2);
        }

        @Test
        void zipTwoCharArraysWithDefaultsEqualLength() throws Exception {
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y' };
            char[] result = Arrays.zip(a, b, '?', '!', (x, y) -> (char) (x + y - 'A'));
            // result: {'X', 'Z', '#', '$'} (using '!' for missing b elements)
            assertArrayEquals(new char[] { 'X', 'Z', '#', '$' }, result);
        }

        @Test
        void zipTwoCharArraysWithDefaultsNulls() throws Exception {
            // Test with first array null
            char[] a = null;
            char[] b = { 'X', 'Y', 'Z' };
            char[] result = Arrays.zip(a, b, '?', '!', (x, y) -> y);
            assertArrayEquals(new char[] { 'X', 'Y', 'Z' }, result);

            // Test with second array null
            char[] a2 = { 'A', 'B', 'C' };
            char[] b2 = null;
            char[] result2 = Arrays.zip(a2, b2, '?', '!', (x, y) -> x);
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, result2);

            // Test with both arrays null
            char[] result3 = Arrays.zip((char[]) null, null, '?', '!', (x, y) -> x);
            assertArrayEquals(new char[] {}, result3);
        }

        @Test
        void zipTwoCharArraysWithNulls() throws Exception {
            // Test with first array null
            char[] a = null;
            char[] b = { 'X', 'Y', 'Z' };
            char[] result = Arrays.zip(a, b, (x, y) -> (char) (x + y));
            assertArrayEquals(new char[] {}, result);

            // Test with second array null
            char[] a2 = { 'A', 'B', 'C' };
            char[] b2 = null;
            char[] result2 = Arrays.zip(a2, b2, (x, y) -> (char) (x + y));
            assertArrayEquals(new char[] {}, result2);

            // Test with both arrays null
            char[] result3 = Arrays.zip((char[]) null, null, (x, y) -> (char) (x + y));
            assertArrayEquals(new char[] {}, result3);
        }

        @Test
        void zipWithDefaultValuesForTrailingBlocks_int3DArrays() throws Exception {
            int[][][] first = { { { 1 }, { 2, 3 } } };
            int[][][] second = { { { 10, 20 } } };
            int[][][] third = { { { 100 } }, { { 200, 300 } }, { { 400 } } };

            int[][][] zipped = Arrays.zip(first, second, third, 1000, 2000, 3000, (x, y, z) -> x + y + z);

            assertArrayEquals(new int[] { 111, 4020 }, zipped[0][0]);
            assertArrayEquals(new int[] { 5002, 5003 }, zipped[0][1]);
            assertArrayEquals(new int[] { 3200, 3300 }, zipped[1][0]);
            assertArrayEquals(new int[] { 3400 }, zipped[2][0]);
        }

        @Test
        void zipWithDefaultValuesForTrailingRows_byteArrays() throws Exception {
            byte[][] left2d = { { 1, 2 }, { 3 } };
            byte[][] right2d = { { 10 }, { 20, 30 }, { 40, 50 } };

            byte[][] zipped2d = Arrays.zip(left2d, right2d, (byte) 100, (byte) 0, (x, y) -> (byte) (x + y));

            assertArrayEquals(new byte[] { 11, 2 }, zipped2d[0]);
            assertArrayEquals(new byte[] { 23, -126 }, zipped2d[1]);
            assertArrayEquals(new byte[] { -116, -106 }, zipped2d[2]);

            byte[][][] left3d = { { { 1 }, { 2 } } };
            byte[][][] right3d = { { { 10, 20 } }, { { 30 } } };

            byte[][][] zipped3d = Arrays.zip(left3d, right3d, (byte) 100, (byte) 0, (x, y) -> (byte) (x + y));

            assertArrayEquals(new byte[] { 11, 120 }, zipped3d[0][0]);
            assertArrayEquals(new byte[] { 2 }, zipped3d[0][1]);
            assertArrayEquals(new byte[] { -126 }, zipped3d[1][0]);
        }

        @Test
        void zipWithDefaultValuesForTrailingRows_floatAndDoubleArrays() throws Exception {
            float[][] leftFloat2d = { { 1f, 2f }, { 3f } };
            float[][] rightFloat2d = { { 10f }, { 20f, 30f }, { 40f } };

            float[][] zippedFloat2d = Arrays.zip(leftFloat2d, rightFloat2d, 100f, 0f, (x, y) -> x + y);

            assertArrayEquals(new float[] { 11f, 2f }, zippedFloat2d[0], 0.0001f);
            assertArrayEquals(new float[] { 23f, 130f }, zippedFloat2d[1], 0.0001f);
            assertArrayEquals(new float[] { 140f }, zippedFloat2d[2], 0.0001f);

            float[][][] leftFloat3d = { { { 1f }, { 2f } } };
            float[][][] rightFloat3d = { { { 10f, 20f } }, { { 30f } } };

            float[][][] zippedFloat3d = Arrays.zip(leftFloat3d, rightFloat3d, 100f, 0f, (x, y) -> x + y);

            assertArrayEquals(new float[] { 11f, 120f }, zippedFloat3d[0][0], 0.0001f);
            assertArrayEquals(new float[] { 2f }, zippedFloat3d[0][1], 0.0001f);
            assertArrayEquals(new float[] { 130f }, zippedFloat3d[1][0], 0.0001f);

            double[][] firstDouble2d = { { 1d }, { 2d, 3d } };
            double[][] secondDouble2d = { { 10d, 20d } };
            double[][] thirdDouble2d = { { 100d }, { 200d, 300d }, { 400d } };

            double[][] zippedDoubleTernary2d = Arrays.zip(firstDouble2d, secondDouble2d, thirdDouble2d, 1000d, 2000d, 3000d, (x, y, z) -> x + y + z);

            assertArrayEquals(new double[] { 111d, 4020d }, zippedDoubleTernary2d[0], 0.0001d);
            assertArrayEquals(new double[] { 2202d, 2303d }, zippedDoubleTernary2d[1], 0.0001d);
            assertArrayEquals(new double[] { 3400d }, zippedDoubleTernary2d[2], 0.0001d);

            double[][][] leftDouble3d = { { { 1d }, { 2d } } };
            double[][][] rightDouble3d = { { { 10d, 20d } }, { { 30d } } };

            double[][][] zippedDouble3d = Arrays.zip(leftDouble3d, rightDouble3d, 100d, 0d, (x, y) -> x + y);

            assertArrayEquals(new double[] { 11d, 120d }, zippedDouble3d[0][0], 0.0001d);
            assertArrayEquals(new double[] { 2d }, zippedDouble3d[0][1], 0.0001d);
            assertArrayEquals(new double[] { 130d }, zippedDouble3d[1][0], 0.0001d);
        }

        @Test
        void zipWithDefaultValuesForTrailingRows_shortArrays() throws Exception {
            short[][] left2d = { { 1, 2 }, { 3 } };
            short[][] right2d = { { 10 }, { 20, 30 }, { 40, 50 } };

            short[][] zipped2d = Arrays.zip(left2d, right2d, (short) 100, (short) 0, (x, y) -> (short) (x + y));

            assertArrayEquals(new short[] { 11, 2 }, zipped2d[0]);
            assertArrayEquals(new short[] { 23, 130 }, zipped2d[1]);
            assertArrayEquals(new short[] { 140, 150 }, zipped2d[2]);

            short[][][] left3d = { { { 1 }, { 2 } } };
            short[][][] right3d = { { { 10, 20 } }, { { 30 } } };

            short[][][] zipped3d = Arrays.zip(left3d, right3d, (short) 100, (short) 0, (x, y) -> (short) (x + y));

            assertArrayEquals(new short[] { 11, 120 }, zipped3d[0][0]);
            assertArrayEquals(new short[] { 2 }, zipped3d[0][1]);
            assertArrayEquals(new short[] { 130 }, zipped3d[1][0]);

            short[][] first2d = { { 1 }, { 2, 3 } };
            short[][] second2d = { { 10, 20 } };
            short[][] third2d = { { 100 }, { 200, 300 }, { 400 } };

            short[][] zippedTernary2d = Arrays.zip(first2d, second2d, third2d, (short) 1000, (short) 2000, (short) 3000, (x, y, z) -> (short) (x + y + z));

            assertArrayEquals(new short[] { 111, 4020 }, zippedTernary2d[0]);
            assertArrayEquals(new short[] { 2202, 2303 }, zippedTernary2d[1]);
            assertArrayEquals(new short[] { 3400 }, zippedTernary2d[2]);
        }

        @Test
        void zipWithDefaultValuesForTrailingRows_shortFloatAndDoubleArrays() throws Exception {
            short[][] shortLeft2d = { { 1, 2 }, { 3 } };
            short[][] shortRight2d = { { 10 }, { 20, 30 }, { 40 } };
            short[][] shortZipped2d = Arrays.zip(shortLeft2d, shortRight2d, (short) 100, (short) 1000, (x, y) -> (short) (x - y));
            assertArrayEquals(new short[] { -9, -998 }, shortZipped2d[0]);
            assertArrayEquals(new short[] { -17, 70 }, shortZipped2d[1]);
            assertArrayEquals(new short[] { 60 }, shortZipped2d[2]);

            short[][][] shortLeft3d = { { { 1, 2 } }, { { 3 } } };
            short[][][] shortRight3d = { { { 10 }, { 20 } }, { { 30, 40 } }, { { 50 } } };
            short[][][] shortZipped3d = Arrays.zip(shortLeft3d, shortRight3d, (short) 100, (short) 1000, (x, y) -> (short) (x + y));
            assertArrayEquals(new short[] { 11, 1002 }, shortZipped3d[0][0]);
            assertArrayEquals(new short[] { 120 }, shortZipped3d[0][1]);
            assertArrayEquals(new short[] { 33, 140 }, shortZipped3d[1][0]);
            assertArrayEquals(new short[] { 150 }, shortZipped3d[2][0]);

            float[][] floatLeft2d = { { 1.5f }, { 2.5f, 3.5f } };
            float[][] floatRight2d = { { 10.0f, 20.0f }, { 30.0f } };
            float[][] floatThird2d = { { 100.0f }, { 200.0f, 300.0f }, { 400.0f } };
            float[][] floatZipped2d = Arrays.zip(floatLeft2d, floatRight2d, floatThird2d, 1.0f, 2.0f, 3.0f, (x, y, z) -> x + y + z);
            assertArrayEquals(new float[] { 111.5f, 24.0f }, floatZipped2d[0], 0.0001f);
            assertArrayEquals(new float[] { 232.5f, 305.5f }, floatZipped2d[1], 0.0001f);
            assertArrayEquals(new float[] { 403.0f }, floatZipped2d[2], 0.0001f);

            float[][][] floatLeft3d = { { { 1.0f } }, { { 2.0f } } };
            float[][][] floatRight3d = { { { 10.0f }, { 20.0f } }, { { 30.0f } } };
            float[][][] floatThird3d = { { { 100.0f } }, { { 200.0f, 300.0f } }, { { 400.0f } } };
            float[][][] floatZipped3d = Arrays.zip(floatLeft3d, floatRight3d, floatThird3d, 1.0f, 2.0f, 3.0f, (x, y, z) -> x + y + z);
            assertArrayEquals(new float[] { 111.0f }, floatZipped3d[0][0], 0.0001f);
            assertArrayEquals(new float[] { 24.0f }, floatZipped3d[0][1], 0.0001f);
            assertArrayEquals(new float[] { 232.0f, 303.0f }, floatZipped3d[1][0], 0.0001f);
            assertArrayEquals(new float[] { 403.0f }, floatZipped3d[2][0], 0.0001f);

            double[][] doubleLeft2d = { { 1.0, 2.0 }, { 3.0 } };
            double[][] doubleRight2d = { { 10.0 }, { 20.0, 30.0 }, { 40.0 } };
            double[][] doubleZipped2d = Arrays.zip(doubleLeft2d, doubleRight2d, 100.0, 1000.0, (x, y) -> x * y);
            assertArrayEquals(new double[] { 10.0, 2000.0 }, doubleZipped2d[0], 0.0001d);
            assertArrayEquals(new double[] { 60.0, 3000.0 }, doubleZipped2d[1], 0.0001d);
            assertArrayEquals(new double[] { 4000.0 }, doubleZipped2d[2], 0.0001d);

            double[][][] doubleLeft3d = { { { 1.0 } }, { { 2.0, 3.0 } } };
            double[][][] doubleRight3d = { { { 10.0 }, { 20.0 } }, { { 30.0 } } };
            double[][][] doubleThird3d = { { { 100.0 } }, { { 200.0, 300.0 } }, { { 400.0 } } };
            double[][][] doubleZipped3d = Arrays.zip(doubleLeft3d, doubleRight3d, doubleThird3d, 1.0, 2.0, 3.0, (x, y, z) -> x + y + z);
            assertArrayEquals(new double[] { 111.0 }, doubleZipped3d[0][0], 0.0001d);
            assertArrayEquals(new double[] { 24.0 }, doubleZipped3d[0][1], 0.0001d);
            assertArrayEquals(new double[] { 232.0, 305.0 }, doubleZipped3d[1][0], 0.0001d);
            assertArrayEquals(new double[] { 403.0 }, doubleZipped3d[2][0], 0.0001d);
        }

        @Test
        void zipWithDifferentFunctions() throws Exception {
            long[] a = { 10, 20, 30 };
            long[] b = { 3, 4, 5 };

            // Test subtraction
            long[] result1 = Arrays.zip(a, b, (x, y) -> x - y);
            assertArrayEquals(new long[] { 7, 16, 25 }, result1);

            // Test multiplication
            long[] result2 = Arrays.zip(a, b, (x, y) -> x * y);
            assertArrayEquals(new long[] { 30, 80, 150 }, result2);

            // Test division
            long[] result3 = Arrays.zip(a, b, (x, y) -> x / y);
            assertArrayEquals(new long[] { 3, 5, 6 }, result3);

            // Test max
            long[] result4 = Arrays.zip(a, b, (x, y) -> Math.max(x, y));
            assertArrayEquals(new long[] { 10, 20, 30 }, result4);
        }

        @Test
        void zipWithException() {
            // Test that exceptions are properly propagated
            long[] a = { 1, 2, 3 };
            long[] b = { 4, 5, 6 };

            assertThrows(RuntimeException.class, () -> {
                Arrays.zip(a, b, (x, y) -> {
                    if (x == 2) {
                        throw new RuntimeException("Test exception");
                    }
                    return x + y;
                });
            });
        }
    }

    private static Object[] arrayWithDimensions(final int dimensions) {
        return (Object[]) java.lang.reflect.Array.newInstance(Object.class, new int[dimensions]);
    }

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> raggedZipCases() {
        return ArrayTestSupport.primitiveTypes()
                .flatMap(type -> java.util.stream.IntStream.rangeClosed(1, 3)
                        .boxed()
                        .flatMap(dimensions -> java.util.stream.Stream.of(2, 3)
                                .flatMap(operands -> java.util.stream.Stream.of(false, true)
                                        .map(padded -> org.junit.jupiter.params.provider.Arguments.of(type, dimensions, operands, padded)))));
    }

    private static Object[] zipFixtures(Class<?> type, int dimensions) {
        Object single = primitiveArray(type, 1);
        Object ragged = primitiveArray(type, 2, 3);
        for (int depth = 2; depth <= dimensions; depth++) {
            single = nestedArray(type, depth, single);
            Object empty = java.lang.reflect.Array.newInstance(type, new int[depth - 1]);
            ragged = nestedArray(type, depth, null, empty, ragged, singleChild(type, depth - 1));
        }
        return new Object[] { null, java.lang.reflect.Array.newInstance(type, new int[dimensions]), single, ragged };
    }

    private static Object singleChild(Class<?> type, int dimensions) {
        Object result = primitiveArray(type, 4);
        for (int depth = 2; depth <= dimensions; depth++) {
            result = nestedArray(type, depth, result);
        }
        return result;
    }

    // Declarative shape oracle: each dimension uses the shortest input without
    // defaults and the longest input with defaults, independently of the optimized
    // typed overloads. Record every leaf argument tuple as well as the result shape.
    private static Object expectedZip(Class<?> type, int dimensions, Object[] inputs, Object[] defaults, boolean padded, Object marker,
            List<List<Object>> calls) {
        int[] lengths = java.util.Arrays.stream(inputs).mapToInt(input -> input == null ? 0 : java.lang.reflect.Array.getLength(input)).toArray();
        int length = padded ? java.util.Arrays.stream(lengths).max().orElseThrow() : java.util.Arrays.stream(lengths).min().orElseThrow();
        Class<?> componentType = dimensions == 1 ? type : arrayType(type, dimensions - 1);
        Object result = java.lang.reflect.Array.newInstance(componentType, length);
        for (int position = 0; position < length; position++) {
            Object[] values = new Object[inputs.length];
            for (int operand = 0; operand < inputs.length; operand++) {
                values[operand] = position < lengths[operand] ? java.lang.reflect.Array.get(inputs[operand], position)
                        : dimensions == 1 ? defaults[operand] : null;
            }
            if (dimensions == 1) {
                calls.add(java.util.Arrays.asList(values));
                java.lang.reflect.Array.set(result, position, marker);
            } else {
                java.lang.reflect.Array.set(result, position, expectedZip(type, dimensions - 1, values, defaults, padded, marker, calls));
            }
        }
        return result;
    }
}
