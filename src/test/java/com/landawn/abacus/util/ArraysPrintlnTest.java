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
import static com.landawn.abacus.util.ArrayTestSupport.invokeArrayMethod;
import static com.landawn.abacus.util.ArrayTestSupport.nestedArray;
import static com.landawn.abacus.util.ArrayTestSupport.primitiveArray;
import static com.landawn.abacus.util.ArrayTestSupport.primitiveValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.landawn.abacus.util.Arrays.ff;
import com.landawn.abacus.util.Arrays.fff;

class ArraysPrintlnTest {

    @Nested
    class PrintlnTests {

        @ParameterizedTest(name = "{0}: null, empty, and ragged printing")
        @MethodSource("com.landawn.abacus.util.ArrayTestSupport#printableTypes")
        void println_nullEmptyAndRaggedArrays_preservesStructureAndIndentation(Class<?> elementType) throws Throwable {
            for (int dimensions = 1; dimensions <= 3; dimensions++) {
                java.lang.reflect.Method method = arrayMethod("println", elementType, dimensions, 1);
                assertEquals("null", invokeArrayMethod(method, (Object) null));
                assertEquals("[]", invokeArrayMethod(method, java.lang.reflect.Array.newInstance(elementType, new int[dimensions])));
            }
            Object row = primitiveArray(elementType, 1);
            Object grid = nestedArray(elementType, 2, null, primitiveArray(elementType), row, primitiveArray(elementType));
            String value = String.valueOf(primitiveValue(elementType, 1));
            assertEquals("[null,\n [],\n [" + value + "],\n []]", invokeArrayMethod(arrayMethod("println", elementType, 2, 1), grid));
            Object cube = nestedArray(elementType, 3, null, nestedArray(elementType, 2), grid, nestedArray(elementType, 2, row, row));
            assertEquals("[null,\n [],\n [null,\n  [],\n  [" + value + "],\n  []],\n [[" + value + "],\n  [" + value + "]]]",
                    invokeArrayMethod(arrayMethod("println", elementType, 3, 1), cube));
        }

        @Test
        void doublePrintln_3D_withNulls() {
            double[][][] arr = { { { 1.1, 2.2, 3.3 }, { 4.4, 5.5 } }, { { 6.6, 7.7 }, null, {} }, null };
            String s = Arrays.println(arr);
            assertNotNull(s);
            assertTrue(s.contains("1.1"));
            assertTrue(s.contains("4.4"));
            assertTrue(s.contains("null"));
            assertTrue(s.contains("[]"));
        }

        @Test
        void floatPrintln_specialValues() {
            // Verify NaN and Infinity render via println
            float[] a = { Float.NaN, Float.POSITIVE_INFINITY, 1.5f };
            String s = Arrays.println(a);
            assertNotNull(s);
            assertTrue(s.contains("NaN"));
            assertTrue(s.contains("Infinity"));

            String s2 = Arrays.println(new float[][] { { 1.5f, 2.5f }, null, { 3.5f } });
            assertTrue(s2.contains("1.5"));
            assertTrue(s2.contains("null"));

            String s3 = Arrays.println(new float[][][] { { { 1.5f, 2.5f } }, { { 3.5f } } });
            assertTrue(s3.contains("[[1.5, 2.5]]"));
        }

        @Test
        void longPrintln() {
            String s1 = Arrays.println(new long[] { 1L, 2L, 3L });
            assertNotNull(s1);
            assertTrue(s1.contains("1") && s1.contains("2") && s1.contains("3"));
            String s2 = Arrays.println((long[]) null);
            assertEquals("null", s2);
            String s3 = Arrays.println(new long[0]);
            assertEquals("[]", s3);
        }

        @Test
        void nestedObjectPrintingHandlesCyclicArrayElementsAndRepeatedReferences() {
            final Object[] cyclic = new Object[1];
            cyclic[0] = cyclic;
            final String rendered = "[[...]]";

            assertEquals("[[" + rendered + ", " + rendered + "]]", Arrays.println(new Object[][] { { cyclic, cyclic } }));
            assertEquals("[[[" + rendered + ", " + rendered + "]]]", Arrays.println(new Object[][][] { { { cyclic, cyclic } } }));
        }

        @Test
        void nestedObjectPrintingPreservesNonArrayToStringRepresentations() {
            final Object value = new AbstractCollection<Object>() {
                @Override
                public Iterator<Object> iterator() {
                    throw new AssertionError("Printing a non-array element must use its toString method");
                }

                @Override
                public int size() {
                    return 1;
                }

                @Override
                public String toString() {
                    return "custom value";
                }
            };

            assertEquals("[[custom value]]", Arrays.println(new Object[][] { { value } }));
            assertEquals("[[[custom value]]]", Arrays.println(new Object[][][] { { { value } } }));
        }

        @Test
        void objectArrayPrintlnHelpersArePackagePrivate() throws Exception {
            final java.lang.reflect.Method ffPrintln = ff.class.getDeclaredMethod("println", Object[][].class);
            final java.lang.reflect.Method fffPrintln = fff.class.getDeclaredMethod("println", Object[][][].class);

            assertFalse(java.lang.reflect.Modifier.isPublic(ffPrintln.getModifiers()));
            assertFalse(java.lang.reflect.Modifier.isPublic(fffPrintln.getModifiers()));
        }

        @Test
        void objectPrintingExpandsArrayPayloadsWithTheSameElementFormattingAtEveryDepth() {
            final Object value = new Object[] { new Object[] { "leaf", new boolean[] { true } }, new String[0] };
            final String rendered = "[[leaf, [true]], []]";

            assertEquals("[" + rendered + "]", Arrays.println(new Object[] { value }));
            assertEquals("[[" + rendered + "]]", Arrays.println(new Object[][] { { value } }));
            assertEquals("[[[" + rendered + "]]]", Arrays.println(new Object[][][] { { { value } } }));
        }

        @Test
        void objectPrintingPropagatesElementConversionFailures() {
            final IllegalStateException failure = new IllegalStateException("element conversion failed");
            final Object value = new Object() {
                @Override
                public String toString() {
                    throw failure;
                }
            };

            assertSame(failure, assertThrows(IllegalStateException.class, () -> Arrays.println(new Object[] { value })));
            assertSame(failure, assertThrows(IllegalStateException.class, () -> Arrays.println(new Object[][] { { value } })));
            assertSame(failure, assertThrows(IllegalStateException.class, () -> Arrays.println(new Object[][][] { { { value } } })));
        }

        @Test
        void printAndReturn_primitiveCubesWithValues() {
            String charCube = Arrays.println(new char[][][] { { { 'a', 'b' } }, { { 'c' } } });
            String byteCube = Arrays.println(new byte[][][] { { { 1, 2 } }, { { 3 } } });
            String shortCube = Arrays.println(new short[][][] { { { 4, 5 } }, { { 6 } } });
            String longCube = Arrays.println(new long[][][] { { { 7L, 8L } }, { { 9L } } });
            String floatCube = Arrays.println(new float[][][] { { { 1.5f, 2.5f } }, { { 3.5f } } });

            assertTrue(charCube.contains("[[a, b]]"));
            assertTrue(byteCube.contains("[[1, 2]]"));
            assertTrue(shortCube.contains("[[4, 5]]"));
            assertTrue(longCube.contains("[[7, 8]]"));
            assertTrue(floatCube.contains("[[1.5, 2.5]]"));
        }

        @Test
        void println() {
            // Test with normal array
            String[][][] data = { { { "a", "b" } }, { { "c", "d" }, { "e" } } };
            String output = fff.println(data);
            assertTrue(output.contains("a"));
            assertTrue(output.contains("b"));
            assertTrue(output.contains("c"));
            assertTrue(output.contains("d"));
            assertTrue(output.contains("e"));

            // Test with null array
            String nullOutput = fff.println(null);
            assertEquals("null", nullOutput.trim());

            // Test with empty array
            String[][][] emptyArray = new String[0][][];
            String emptyOutput = fff.println(emptyArray);
            assertEquals("[]", emptyOutput.trim());

            // Test with null sub-arrays
            Integer[][][] mixedArray = { { { 1, 2 } }, null, { { 3, 4 } } };
            String mixedOutput = fff.println(mixedArray);
            assertTrue(mixedOutput.contains("1"));
            assertTrue(mixedOutput.contains("2"));
            assertTrue(mixedOutput.contains("null"));
            assertTrue(mixedOutput.contains("3"));
            assertTrue(mixedOutput.contains("4"));

            // Test with empty sub-arrays
            Integer[][][] emptySubArrays = { { { 1 } }, { {} }, { { 2 } } };
            String emptySubOutput = fff.println(emptySubArrays);
            assertTrue(emptySubOutput.contains("1"));
            assertTrue(emptySubOutput.contains("[]"));
            assertTrue(emptySubOutput.contains("2"));
        }

        @Test
        void println_1D_booleanArray_normal() {
            boolean[] arr = { true, false, true, false };
            String result = Arrays.println(arr);
            assertEquals("[true, false, true, false]", result);
        }

        @Test
        void println_1D_booleanArray_null() {
            boolean[] arr = null;
            String result = Arrays.println(arr);
            assertEquals("null", result);
        }

        @Test
        void println_1D_byteArray() {
            byte[] arr = { 1, 2, 3 };
            String result = Arrays.println(arr);
            assertEquals("[1, 2, 3]", result);
        }

        @Test
        void println_1D_byteArray_normal() {
            byte[] arr = { 1, 2, 3, 4 };
            String result = Arrays.println(arr);
            assertEquals("[1, 2, 3, 4]", result);
        }

        @Test
        void println_1D_charArray() {
            char[] arr = { 'a', 'b', 'c' };
            String result = Arrays.println(arr);
            assertEquals("[a, b, c]", result);
        }

        @Test
        void println_1D_charArray_normal() {
            char[] arr = { 'a', 'b', 'c', 'd' };
            String result = Arrays.println(arr);
            assertEquals("[a, b, c, d]", result);
        }

        @Test
        void println_1D_doubleArray() {
            double[] arr = { 1.0, 2.0, 3.0 };
            String result = Arrays.println(arr);
            assertEquals("[1.0, 2.0, 3.0]", result);
        }

        @Test
        void println_1D_doubleArray_normal() {
            double[] arr = { 1.5, 2.5, 3.5 };
            String result = Arrays.println(arr);
            assertEquals("[1.5, 2.5, 3.5]", result);
        }

        @Test
        void println_1D_floatArray() {
            float[] arr = { 1.0f, 2.0f, 3.0f };
            String result = Arrays.println(arr);
            assertEquals("[1.0, 2.0, 3.0]", result);
        }

        @Test
        void println_1D_floatArray_normal() {
            float[] arr = { 1.1f, 2.2f, 3.3f };
            String result = Arrays.println(arr);
            assertEquals("[1.1, 2.2, 3.3]", result);
        }

        @Test
        void println_1D_intArray() {
            int[] arr = { 1, 2, 3 };
            String result = Arrays.println(arr);
            assertEquals("[1, 2, 3]", result);
        }

        @Test
        void println_1D_intArray_normal() {
            int[] arr = { 1, 2, 3, 4, 5 };
            String result = Arrays.println(arr);
            assertEquals("[1, 2, 3, 4, 5]", result);
        }

        @Test
        void println_1D_longArray() {
            long[] arr = { 1L, 2L, 3L };
            String result = Arrays.println(arr);
            assertEquals("[1, 2, 3]", result);
        }

        @Test
        void println_1D_longArray_normal() {
            long[] arr = { 100L, 200L, 300L };
            String result = Arrays.println(arr);
            assertEquals("[100, 200, 300]", result);
        }

        @Test
        void println_1D_objectArray() {
            Object[] arr = { "Hello", "World", 123 };
            String result = Arrays.println(arr);
            assertEquals("[Hello, World, 123]", result);
        }

        @Test
        void println_1D_objectArray_empty() {
            Object[] arr = {};
            String result = Arrays.println(arr);
            assertEquals("[]", result);
        }

        @Test
        void println_1D_objectArray_normal() {
            Object[] arr = { "Hello", "World", 123, 45.6 };
            String result = Arrays.println(arr);
            assertEquals("[Hello, World, 123, 45.6]", result);
        }

        @Test
        void println_1D_objectArray_null() {
            Object[] arr = null;
            String result = Arrays.println(arr);
            assertEquals("null", result);
        }

        @Test
        void println_1D_shortArray() {
            short[] arr = { 1, 2, 3 };
            String result = Arrays.println(arr);
            assertEquals("[1, 2, 3]", result);
        }

        @Test
        void println_1D_shortArray_normal() {
            short[] arr = { 10, 20, 30 };
            String result = Arrays.println(arr);
            assertEquals("[10, 20, 30]", result);
        }

        @Test
        void println_1D_withEmptyArray() {
            String result = Arrays.println(new Object[0]);
            assertEquals("[]", result);
        }

        @Test
        void println_1D_withNullArray() {
            String result = Arrays.println((Object[]) null);
            assertEquals("null", result);
        }

        @Test
        void println_1D_withValidArray() {
            Object[] arr = { "Hello", 123, "World" };
            String result = Arrays.println(arr);
            assertEquals("[Hello, 123, World]", result);
        }

        @Test
        void println_2D_booleanArray_normal() {
            boolean[][] arr = { { true, false }, { false, true } };
            String result = Arrays.println(arr);
            assertEquals("[[true, false],\n [false, true]]", result);
        }

        @Test
        void println_2D_byteArray_normal() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            String result = Arrays.println(arr);
            assertEquals("[[1, 2],\n [3, 4]]", result);
        }

        @Test
        void println_2D_charArray_normal() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            String result = Arrays.println(arr);
            assertEquals("[[a, b],\n [c, d]]", result);
        }

        @Test
        void println_2D_doubleArray() {
            double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            String result = Arrays.println(arr);
            assertEquals("[[1.0, 2.0],\n [3.0, 4.0]]", result);
        }

        @Test
        void println_2D_doubleArray_normal() {
            double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            String result = Arrays.println(arr);
            assertEquals("[[1.5, 2.5],\n [3.5, 4.5]]", result);
        }

        @Test
        void println_2D_floatArray() {
            float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            String result = Arrays.println(arr);
            assertEquals("[[1.0, 2.0],\n [3.0, 4.0]]", result);
        }

        @Test
        void println_2D_floatArray_normal() {
            float[][] arr = { { 1.1f, 2.2f }, { 3.3f, 4.4f } };
            String result = Arrays.println(arr);
            assertEquals("[[1.1, 2.2],\n [3.3, 4.4]]", result);
        }

        @Test
        void println_2D_intArray() {
            int[][] arr = { { 1, 2 }, { 3, 4 } };
            String result = Arrays.println(arr);
            assertEquals("[[1, 2],\n [3, 4]]", result);
        }

        @Test
        void println_2D_intArray_normal() {
            int[][] arr = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            String result = Arrays.println(arr);
            assertEquals("[[1, 2],\n [3, 4],\n [5, 6]]", result);
        }

        @Test
        void println_2D_longArray() {
            long[][] arr = { { 1L, 2L }, { 3L, 4L } };
            String result = Arrays.println(arr);
            assertEquals("[[1, 2],\n [3, 4]]", result);
        }

        @Test
        void println_2D_longArray_normal() {
            long[][] arr = { { 100L, 200L }, { 300L, 400L } };
            String result = Arrays.println(arr);
            assertEquals("[[100, 200],\n [300, 400]]", result);
        }

        @Test
        void println_2D_objectArray() {
            Object[][] arr = { { "A", "B" }, { "C", "D" } };
            String result = Arrays.println(arr);
            assertEquals("[[A, B],\n [C, D]]", result);
        }

        @Test
        void println_2D_objectArray_normal() {
            Object[][] arr = { { "A", "B" }, { "C", "D" }, { "E", "F" } };
            String result = Arrays.println(arr);
            assertEquals("[[A, B],\n [C, D],\n [E, F]]", result);
        }

        @Test
        void println_2D_objectArray_null() {
            Object[][] arr = null;
            String result = Arrays.println(arr);
            assertEquals("null", result);
        }

        @Test
        void println_2D_objectArray_withNullSubArray() {
            Object[][] arr = { { "A", "B" }, null, { "C" } };
            String result = Arrays.println(arr);
            assertEquals("[[A, B],\n null,\n [C]]", result);
        }

        @Test
        void println_2D_shortArray() {
            short[][] arr = { { 1, 2 }, { 3, 4 } };
            String result = Arrays.println(arr);
            assertEquals("[[1, 2],\n [3, 4]]", result);
        }

        @Test
        void println_2D_shortArray_normal() {
            short[][] arr = { { 10, 20 }, { 30, 40 } };
            String result = Arrays.println(arr);
            assertEquals("[[10, 20],\n [30, 40]]", result);
        }

        @Test
        void println_2D_withEmptyArray() {
            String result = Arrays.println(new Object[0][]);
            assertEquals("[]", result);
        }

        @Test
        void println_2D_withNullArray() {
            String result = Arrays.println((Object[][]) null);
            assertEquals("null", result);
        }

        @Test
        void println_3D_booleanArray() {
            boolean[][][] arr = { { { true, false } } };
            String result = Arrays.println(arr);
            assertEquals("[[[true, false]]]", result);
        }

        @Test
        void println_3D_booleanArray_normal() {
            boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
            String result = Arrays.println(arr);
            assertEquals("[[[true, false],\n  [true]],\n [[false]]]", result);
        }

        @Test
        void println_3D_byteArray_normal() {
            byte[][][] arr = { { { 1, 2 } } };
            String result = Arrays.println(arr);
            assertEquals("[[[1, 2]]]", result);
        }

        @Test
        void println_3D_charArray_normal() {
            char[][][] arr = { { { 'a', 'b' } } };
            String result = Arrays.println(arr);
            assertEquals("[[[a, b]]]", result);
        }

        @Test
        void println_3D_doubleArray() {
            double[][][] arr = { { { 1.0, 2.0 } } };
            String result = Arrays.println(arr);
            assertEquals("[[[1.0, 2.0]]]", result);
        }

        @Test
        void println_3D_doubleArray_normal() {
            double[][][] arr = { { { 1.5, 2.5 } } };
            String result = Arrays.println(arr);
            assertEquals("[[[1.5, 2.5]]]", result);
        }

        @Test
        void println_3D_floatArray() {
            float[][][] arr = { { { 1.0f, 2.0f } } };
            String result = Arrays.println(arr);
            assertEquals("[[[1.0, 2.0]]]", result);
        }

        @Test
        void println_3D_floatArray_normal() {
            float[][][] arr = { { { 1.1f, 2.2f } } };
            String result = Arrays.println(arr);
            assertEquals("[[[1.1, 2.2]]]", result);
        }

        @Test
        void println_3D_intArray() {
            int[][][] arr = { { { 1, 2 } } };
            String result = Arrays.println(arr);
            assertEquals("[[[1, 2]]]", result);
        }

        @Test
        void println_3D_intArray_normal() {
            int[][][] arr = { { { 1, 2 }, { 3 } }, { { 4 } } };
            String result = Arrays.println(arr);
            assertEquals("[[[1, 2],\n  [3]],\n [[4]]]", result);
        }

        @Test
        void println_3D_longArray() {
            long[][][] arr = { { { 1L, 2L } } };
            String result = Arrays.println(arr);
            assertEquals("[[[1, 2]]]", result);
        }

        @Test
        void println_3D_longArray_normal() {
            long[][][] arr = { { { 100L, 200L } } };
            String result = Arrays.println(arr);
            assertEquals("[[[100, 200]]]", result);
        }

        @Test
        void println_3D_objectArray() {
            Object[][][] arr = { { { "A" } } };
            String result = Arrays.println(arr);
            assertEquals("[[[A]]]", result);
        }

        @Test
        void println_3D_objectArray_empty() {
            Object[][][] arr = {};
            String result = Arrays.println(arr);
            assertEquals("[]", result);
        }

        @Test
        void println_3D_objectArray_normal() {
            Object[][][] arr = { { { "A", "B" }, { "C" } }, { { "D" } } };
            String result = Arrays.println(arr);
            assertEquals("[[[A, B],\n  [C]],\n [[D]]]", result);
        }

        @Test
        void println_3D_shortArray() {
            short[][][] arr = { { { 1, 2 } } };
            String result = Arrays.println(arr);
            assertEquals("[[[1, 2]]]", result);
        }

        @Test
        void println_3D_shortArray_normal() {
            short[][][] arr = { { { 10, 20 } } };
            String result = Arrays.println(arr);
            assertEquals("[[[10, 20]]]", result);
        }

        @Test
        void println_3D_withEmptyArray() {
            String result = Arrays.println(new Object[0][][]);
            assertEquals("[]", result);
        }

        @Test
        void println_3D_withNullArray() {
            String result = Arrays.println((Object[][][]) null);
            assertEquals("null", result);
        }

        @Test
        void println_boolean_1D_empty() {
            String result = Arrays.println(new boolean[0]);
            assertEquals("[]", result);
        }

        @Test
        void println_boolean_1D_null() {
            String result = Arrays.println((boolean[]) null);
            assertEquals("null", result);
        }

        @Test
        void println_boolean_3D_consistent() {
            final boolean[][][] a = { { { true, false } }, null };
            final String s = Arrays.println(a);
            assertTrue(s.contains("true"));
            assertTrue(s.contains("false"));
            assertTrue(s.contains("null"));
        }

        @Test
        void println_booleanArray() {
            boolean[] arr = { true, false, true };
            String result = Arrays.println(arr);

            assertEquals("[true, false, true]", result);
        }

        @Test
        void println_booleanArray2DNull() {
            String result = Arrays.println((boolean[][]) null);
            assertEquals("null", result);
        }

        @Test
        void println_booleanArray3D() {
            boolean[][][] arr = { { { true } } };
            String result = Arrays.println(arr);

            assertEquals("[[[true]]]", result);
        }

        @Test
        void println_booleanArray3DNull() {
            String result = Arrays.println((boolean[][][]) null);
            assertEquals("null", result);
        }

        @Test
        void println_booleanArrayEmpty() {
            boolean[] arr = {};
            String result = Arrays.println(arr);
            assertEquals("[]", result);
        }

        @Test
        void println_byte_2D_consistent() {
            final byte[][] a = { { 1, 2 }, null, {} };
            final String s = Arrays.println(a);
            assertTrue(s.contains("[1, 2]"));
            assertTrue(s.contains("null"));
            assertTrue(s.contains("[]"));
        }

        @Test
        void println_char_2D_consistent() {
            final char[][] a = { { 'a', 'b' }, null, {} };
            final String s = Arrays.println(a);
            assertTrue(s.contains("[a, b]"));
            assertTrue(s.contains("null"));
            assertTrue(s.contains("[]"));
        }

        @Test
        void println_double_2D_consistent() {
            final double[][] a = { { 1.5, 2.5 }, null, {} };
            final String s = Arrays.println(a);
            assertTrue(s.contains("1.5"));
            assertTrue(s.contains("null"));
            assertTrue(s.contains("[]"));
        }

        @Test
        void println_double_naN_appears() {
            final String s = Arrays.println(new double[] { Double.NaN, 1.5 });
            assertTrue(s.contains("NaN"), "Expected NaN in output: " + s);
        }

        @Test
        void println_float_2D_consistent() {
            final float[][] a = { { 1.5f, 2.5f }, null, {} };
            final String s = Arrays.println(a);
            assertTrue(s.contains("1.5"));
            assertTrue(s.contains("null"));
            assertTrue(s.contains("[]"));
        }

        @Test
        void println_float_naN_appears() {
            final String s = Arrays.println(new float[] { Float.NaN, 2.5f });
            assertTrue(s.contains("NaN"), "Expected NaN in output: " + s);
        }

        @Test
        void println_int_2D_empty_returnsBrackets() {
            assertEquals("[]", Arrays.println(new int[0][]));
        }

        @Test
        void println_int_2D_null_returnsNullLiteral() {
            assertEquals("null", Arrays.println((int[][]) null));
        }

        @Test
        void println_int_2D_nullSubArray_writesNullToken() {
            final int[][] a = { { 1, 2 }, null, {} };
            final String s = Arrays.println(a);
            assertTrue(s.contains("[1, 2]"), "Should contain row [1, 2]: " + s);
            assertTrue(s.contains("null"), "Should contain 'null' token for null sub-array: " + s);
            assertTrue(s.contains("[]"), "Should contain '[]' token for empty sub-array: " + s);
        }

        @Test
        void println_int_3D_mixedNullsAndEmpties() {
            final int[][][] a = { { { 1 }, null, {} }, null, {} };
            final String s = Arrays.println(a);
            // 3D method handles outer null/empty at index level explicitly
            assertTrue(s.contains("null"));
            assertTrue(s.contains("[]"));
            assertTrue(s.contains("[1]"));
        }

        @Test
        void println_int_3D_null_returnsNullLiteral() {
            assertEquals("null", Arrays.println((int[][][]) null));
        }

        @Test
        void println_int_empty_returnsBracketsString() {
            assertEquals("[]", Arrays.println(new int[0]));
        }

        @Test
        void println_int_null_returnsNullLiteral() {
            assertEquals("null", Arrays.println((int[]) null));
        }

        @Test
        void println_long_2D_consistent() {
            final long[][] a = { { 1L, 2L }, null, {} };
            final String s = Arrays.println(a);
            assertTrue(s.contains("[1, 2]"));
            assertTrue(s.contains("null"));
            assertTrue(s.contains("[]"));
        }

        @Test
        void println_object_1D_empty() {
            assertEquals("[]", Arrays.println(new Object[0]));
        }

        @Test
        void println_object_1D_multilineToString() {
            // An element whose toString contains a newline — does println cope?
            final Object multilineElem = new Object() {
                @Override
                public String toString() {
                    return "line1\nline2";
                }
            };
            final String s = Arrays.println(new Object[] { multilineElem, "x" });
            assertTrue(s.contains("line1"));
            assertTrue(s.contains("line2"));
            assertTrue(s.contains("x"));
        }

        @Test
        void println_object_1D_null() {
            assertEquals("null", Arrays.println((Object[]) null));
        }

        @Test
        void println_object_2D_empty() {
            assertEquals("[]", Arrays.println(new Object[0][]));
        }

        @Test
        void println_object_2D_null() {
            assertEquals("null", Arrays.println((Object[][]) null));
        }

        @Test
        void println_object_3D_null() {
            assertEquals("null", Arrays.println((Object[][][]) null));
        }

        @Test
        void println_object2DArray() {
            // Test with normal two-dimensional array
            Object[][] arr = { { "A", "B" }, { "C", "D" } };
            String result = Arrays.println(arr);
            assertEquals("[[A, B],\n [C, D]]", result);

            // Test with null two-dimensional array
            Object[][] nullArr = null;
            String nullResult = Arrays.println(nullArr);
            assertEquals("null", nullResult);
        }

        @Test
        void println_object3DArray() {
            // Test with normal three-dimensional array
            Object[][][] arr = { { { "A", "B" } }, { { "C", "D" } } };
            String result = Arrays.println(arr);
            assertEquals("[[[A, B]],\n [[C, D]]]", result);

            // Test with null three-dimensional array
            Object[][][] nullArr = null;
            String nullResult = Arrays.println(nullArr);
            assertEquals("null", nullResult);
        }

        @Test
        void println_objectArray() {
            // Test with normal array
            Object[] arr = { "Hello", "World", 123 };
            String result = Arrays.println(arr);
            assertTrue(result.contains("Hello"));
            assertTrue(result.contains("World"));
            assertTrue(result.contains("123"));

            // Test with empty array
            Object[] emptyArr = {};
            String emptyResult = Arrays.println(emptyArr);
            assertEquals("[]", emptyResult.trim());

            // Test with null array
            Object[] nullArr = null;
            String nullResult = Arrays.println(nullArr);
            assertEquals("null", nullResult.trim());

            // Test with array containing nulls
            Object[] nullsArr = { "test", null, 42 };
            String nullsResult = Arrays.println(nullsArr);
            assertTrue(nullsResult.contains("test"));
            assertTrue(nullsResult.contains("null"));
            assertTrue(nullsResult.contains("42"));
        }

        @Test
        void println_objectArray_values_a_b_c() {
            Object[] arr = { "a", "b", "c" };
            String result = Arrays.println(arr);

            assertEquals("[a, b, c]", result);
        }

        @Test
        void println_objectArray2D() {
            Object[][] arr = { { "a", "b" }, { "c", "d" } };
            String result = Arrays.println(arr);

            assertEquals("[[a, b],\n [c, d]]", result);
        }

        @Test
        void println_objectArray3D() {
            Object[][][] arr = { { { "a" } } };
            String result = Arrays.println(arr);

            assertEquals("[[[a]]]", result);
        }

        @Test
        void println_short_2D_consistent() {
            final short[][] a = { { 10, 20 }, null, {} };
            final String s = Arrays.println(a);
            assertTrue(s.contains("[10, 20]"));
            assertTrue(s.contains("null"));
            assertTrue(s.contains("[]"));
        }

        @Test
        void printlnBooleanArray() {
            // Line 2965-2967: println({true,false,true}) => "[true, false, true]"
            boolean[] arr = { true, false, true };
            String result = Arrays.println(arr);
            assertTrue(result.contains("[true, false, true]"));
        }

        @Test
        void printlnByteArray() {
            // Line 5581-5583: println({1,2,3,4,5}) => "[1, 2, 3, 4, 5]"
            byte[] array = { 1, 2, 3, 4, 5 };
            String result = Arrays.println(array);
            assertTrue(result.contains("[1, 2, 3, 4, 5]"));
        }

        @Test
        void printlnObjectArrayEmpty() {
            // Line 284-285: println(empty Object[]) => "[]"
            Object[] empty = new Object[0];
            String result = Arrays.println(empty);
            assertTrue(result.contains("[]"));
        }

        @Test
        void printlnObjectArrayNull() {
            // Line 279-280: println((Object[]) null) => "null"
            String result = Arrays.println((Object[]) null);
            assertTrue(result.contains("null"));
        }

        @Test
        void printlnPrimitiveCubes_emptyInput() {
            assertEquals("[]", Arrays.println(new char[0][][]));
            assertEquals("[]", Arrays.println(new byte[0][][]));
            assertEquals("[]", Arrays.println(new short[0][][]));
            assertEquals("[]", Arrays.println(new int[0][][]));
            assertEquals("[]", Arrays.println(new long[0][][]));
            assertEquals("[]", Arrays.println(new float[0][][]));
            assertEquals("[]", Arrays.println(new double[0][][]));
        }

        @ParameterizedTest
        @MethodSource("com.landawn.abacus.util.ArraysPrintlnTest#arrayValues")
        void threeDimensionalPrintingRendersArrayValuedElementsByContent(final Object value, final String renderedValue) {
            final Object[][][] cube = { { { value }, null, {} }, null, {} };

            assertEquals("[[[" + renderedValue + "],\n  null,\n  []],\n null,\n []]", Arrays.println(cube));
        }

        @ParameterizedTest
        @MethodSource("com.landawn.abacus.util.ArraysPrintlnTest#arrayValues")
        void twoDimensionalPrintingRendersArrayValuedElementsByContent(final Object value, final String renderedValue) {
            final Object[] row = { value, "tail", null };
            final Object[][] grid = { row, null, {} };

            assertEquals("[" + renderedValue + ", tail, null]", Arrays.println(row));
            assertEquals("[[" + renderedValue + ", tail, null],\n null,\n []]", Arrays.println(grid));
        }
    }

    static Stream<Arguments> arrayValues() {
        return Stream.of(Arguments.of(new boolean[] { true, false }, "[true, false]"), Arguments.of(new char[] { 'a', 'b' }, "[a, b]"),
                Arguments.of(new byte[] { 1, -2 }, "[1, -2]"), Arguments.of(new short[] { 3, -4 }, "[3, -4]"), Arguments.of(new int[] { 5, -6 }, "[5, -6]"),
                Arguments.of(new long[] { 7, -8 }, "[7, -8]"), Arguments.of(new float[] { 1.5f, -2.5f }, "[1.5, -2.5]"),
                Arguments.of(new double[] { 3.5d, -4.5d }, "[3.5, -4.5]"),
                Arguments.of(new Object[] { "nested", new int[] { 9, 10 }, null }, "[nested, [9, 10], null]"), Arguments.of(new Object[0], "[]"));
    }
}
