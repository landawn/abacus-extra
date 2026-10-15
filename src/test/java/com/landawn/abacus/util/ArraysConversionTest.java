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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ArraysConversionTest {

    @Nested
    class ToBooleanTests {

        @Test
        void toBoolean_1D_byte() {
            byte[] arr = { 1, 0, 1, 0 };
            boolean[] result = Arrays.toBoolean(arr);
            assertNotNull(result);
            assertEquals(4, result.length);
            assertTrue(result[0]);
            assertFalse(result[1]);
        }

        @Test
        void toBoolean_2D_byte() {
            byte[][] arr = { { 1, 0 }, { 0, 1 } };
            boolean[][] result = Arrays.toBoolean(arr);
            assertNotNull(result);
            assertEquals(2, result.length);
            assertTrue(result[0][0]);
            assertFalse(result[0][1]);
        }

        @Test
        void toBoolean_3D_int() {
            int[][][] arr = { { { 1, 0 } } };
            boolean[][][] result = Arrays.toBoolean(arr);
            assertNotNull(result);
        }

        @Test
        void toBoolean_byte2DArray() {
            byte[][] a = { { 0, 1 }, { 1, 0 } };
            boolean[][] result = Arrays.toBoolean(a);

            assertEquals(2, result.length);
            assertFalse(result[0][0]);
            assertTrue(result[0][1]);
            assertTrue(result[1][0]);
            assertFalse(result[1][1]);
        }

        @Test
        void toBoolean_byte2DArray_nullSubArrayBecomesEmpty() {
            byte[][] a = { { 1, -1 }, null, { 0 } };
            boolean[][] result = Arrays.toBoolean(a);
            assertEquals(3, result.length);
            assertArrayEquals(new boolean[] { true, false }, result[0]);
            assertNotNull(result[1]);
            assertEquals(0, result[1].length);
            assertArrayEquals(new boolean[] { false }, result[2]);
        }

        @Test
        void toBoolean_byte3DArray() {
            byte[][][] a = { { { 0, 1 } } };
            boolean[][][] result = Arrays.toBoolean(a);

            assertEquals(1, result.length);
            assertFalse(result[0][0][0]);
            assertTrue(result[0][0][1]);
        }

        @Test
        void toBoolean_byteArr_empty() {
            final boolean[] r = Arrays.toBoolean(new byte[0]);
            assertNotNull(r);
            assertEquals(0, r.length);
        }

        @Test
        void toBoolean_byteArr_normal() {
            assertArrayEquals(new boolean[] { true, false, false, true }, Arrays.toBoolean(new byte[] { 1, 0, -1, 5 }));
        }

        @Test
        void toBoolean_byteArr_null() {
            final boolean[] r = Arrays.toBoolean((byte[]) null);
            assertNotNull(r);
            assertEquals(0, r.length);
            assertEquals(boolean[].class, r.getClass());
        }

        @Test
        void toBoolean_byteArr_strictlyPositiveOnly() {
            // The documented contract is "value > 0". -1 and -127 must be false, 1 and 127 must be true.
            final boolean[] r = Arrays.toBoolean(new byte[] { -1, 0, 1, 127, -128 });
            assertArrayEquals(new boolean[] { false, false, true, true, false }, r);
        }

        @Test
        void toBoolean_byteArr2D_null() {
            final boolean[][] r = Arrays.toBoolean((byte[][]) null);
            assertNotNull(r);
            assertEquals(0, r.length);
            assertEquals(boolean[][].class, r.getClass());
        }

        @Test
        void toBoolean_byteArr2D_preservesStructureIncludingNullRow() {
            final byte[][] in = { { 1, 0 }, null, {}, { -1, 2 } };
            final boolean[][] r = Arrays.toBoolean(in);
            assertEquals(4, r.length);
            assertArrayEquals(new boolean[] { true, false }, r[0]);
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
            assertNotNull(r[2]);
            assertEquals(0, r[2].length);
            assertArrayEquals(new boolean[] { false, true }, r[3]);
        }

        @Test
        void toBoolean_byteArr3D_null() {
            final boolean[][][] r = Arrays.toBoolean((byte[][][]) null);
            assertNotNull(r);
            assertEquals(0, r.length);
            assertEquals(boolean[][][].class, r.getClass());
        }

        @Test
        void toBoolean_byteArr3D_preservesStructure() {
            final byte[][][] in = { { { 1, 0 }, { -1 } }, null, { null, { 2 } } };
            final boolean[][][] r = Arrays.toBoolean(in);
            assertEquals(3, r.length);
            assertArrayEquals(new boolean[] { true, false }, r[0][0]);
            assertArrayEquals(new boolean[] { false }, r[0][1]);
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
            assertNotNull(r[2][0]);
            assertEquals(0, r[2][0].length);
            assertArrayEquals(new boolean[] { true }, r[2][1]);
        }

        @Test
        void toBoolean_byteArray() {
            byte[] a = { 0, 1, 0, 1 };
            boolean[] result = Arrays.toBoolean(a);

            assertArrayEquals(new boolean[] { false, true, false, true }, result);
        }

        @Test
        void toBoolean_byteArray_negativeAndZeroBecomeFalse() {
            byte[] a = { 1, 0, -1, 5, Byte.MAX_VALUE, Byte.MIN_VALUE, -128 };
            boolean[] result = Arrays.toBoolean(a);
            // Doc says strictly > 0; negative bytes (including -128 / Byte.MIN_VALUE) must be false.
            assertArrayEquals(new boolean[] { true, false, false, true, true, false, false }, result);
        }

        @Test
        void toBoolean_int2DArray() {
            int[][] a = { { 0, 1 }, { 1, 0 } };
            boolean[][] result = Arrays.toBoolean(a);

            assertEquals(2, result.length);
            assertFalse(result[0][0]);
            assertTrue(result[0][1]);
        }

        @Test
        void toBoolean_int3DArray() {
            int[][][] a = { { { 0, 1 } } };
            boolean[][][] result = Arrays.toBoolean(a);

            assertEquals(1, result.length);
            assertFalse(result[0][0][0]);
            assertTrue(result[0][0][1]);
        }

        @Test
        void toBoolean_int3DArray_nullSubArrayBecomesEmpty() {
            int[][][] a = { { { 1, -1 } }, null, { { 0 } } };
            boolean[][][] result = Arrays.toBoolean(a);
            assertEquals(3, result.length);
            assertArrayEquals(new boolean[] { true, false }, result[0][0]);
            assertNotNull(result[1]);
            assertEquals(0, result[1].length);
            assertArrayEquals(new boolean[] { false }, result[2][0]);
        }

        @Test
        void toBoolean_intArr_empty() {
            assertEquals(0, Arrays.toBoolean(new int[0]).length);
        }

        @Test
        void toBoolean_intArr_normalAndNegative() {
            assertArrayEquals(new boolean[] { true, false, false, true }, Arrays.toBoolean(new int[] { 1, 0, -1, 5 }));
        }

        @Test
        void toBoolean_intArr_null() {
            final boolean[] r = Arrays.toBoolean((int[]) null);
            assertNotNull(r);
            assertEquals(0, r.length);
        }

        @Test
        void toBoolean_intArr_strictlyPositive() {
            assertArrayEquals(new boolean[] { false, false, true, true, false },
                    Arrays.toBoolean(new int[] { Integer.MIN_VALUE, 0, 1, Integer.MAX_VALUE, -1 }));
        }

        @Test
        void toBoolean_intArr2D_null() {
            assertEquals(0, Arrays.toBoolean((int[][]) null).length);
        }

        @Test
        void toBoolean_intArr2D_preservesStructure() {
            final int[][] in = { { 1, 0 }, null, { -1, 5 } };
            final boolean[][] r = Arrays.toBoolean(in);
            assertEquals(3, r.length);
            assertArrayEquals(new boolean[] { true, false }, r[0]);
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
            assertArrayEquals(new boolean[] { false, true }, r[2]);
        }

        @Test
        void toBoolean_intArr3D_null() {
            assertEquals(0, Arrays.toBoolean((int[][][]) null).length);
        }

        @Test
        void toBoolean_intArr3D_preservesStructure() {
            final int[][][] in = { { { 1, 0 }, { -1 } }, null };
            final boolean[][][] r = Arrays.toBoolean(in);
            assertEquals(2, r.length);
            assertArrayEquals(new boolean[] { true, false }, r[0][0]);
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
        }

        @Test
        void toBoolean_intArray() {
            int[] a = { 0, 1, 0, 1 };
            boolean[] result = Arrays.toBoolean(a);

            assertArrayEquals(new boolean[] { false, true, false, true }, result);
        }

        @Test
        void toBoolean_intArray_negativeAndZeroBecomeFalse() {
            int[] a = { 1, 0, -1, 5, Integer.MAX_VALUE, Integer.MIN_VALUE };
            boolean[] result = Arrays.toBoolean(a);
            // Doc says strictly > 0; Integer.MIN_VALUE > 0 must evaluate to false.
            assertArrayEquals(new boolean[] { true, false, false, true, true, false }, result);
        }
    }

    @Nested
    class ToCharTests {

        @Test
        void toChar_1D_int() {
            int[] arr = { 65, 66, 67 };
            char[] result = Arrays.toChar(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals('A', result[0]);
            assertEquals('B', result[1]);
            assertEquals('C', result[2]);
        }

        @Test
        void toChar_2D_int() {
            int[][] arr = { { 65, 66 }, { 67, 68 } };
            char[][] result = Arrays.toChar(arr);
            assertNotNull(result);
            assertEquals('A', result[0][0]);
            assertEquals('D', result[1][1]);
        }

        @Test
        void toChar_3D_int() {
            int[][][] arr = { { { 65, 66 } } };
            char[][][] result = Arrays.toChar(arr);
            assertNotNull(result);
            assertEquals('A', result[0][0][0]);
        }

        @Test
        void toChar_int_truncatesToLow16Bits() {
            // (char) cast keeps only low 16 bits; values outside 0..65535 wrap.
            int[] a = { 65, 0x10041 /* wraps to 0x0041 = 'A' */, 0xFFFF, -1 /* wraps to 0xFFFF */ };
            char[] result = Arrays.toChar(a);
            assertEquals('A', result[0]);
            assertEquals('A', result[1]);
            assertEquals('￿', result[2]);
            assertEquals('￿', result[3]);
        }

        @Test
        void toChar_int2DArray() {
            int[][] a = { { 65, 66 }, { 67, 68 } };
            char[][] result = Arrays.toChar(a);

            assertEquals(2, result.length);
            assertEquals('A', result[0][0]);
            assertEquals('B', result[0][1]);
            assertEquals('C', result[1][0]);
            assertEquals('D', result[1][1]);
        }

        @Test
        void toChar_int3DArray() {
            int[][][] a = { { { 65, 66 } } };
            char[][][] result = Arrays.toChar(a);

            assertEquals('A', result[0][0][0]);
            assertEquals('B', result[0][0][1]);
        }

        @Test
        void toChar_intArr_empty() {
            assertEquals(0, Arrays.toChar(new int[0]).length);
        }

        @Test
        void toChar_intArr_narrowingKeepsLow16Bits() {
            // (char) cast keeps only the low 16 bits; zero-extends from int -> char.
            // 0 -> 0; 65535 -> 65535; 65536 -> 0 (wraps); -1 -> 65535 (low 16 bits of 0xFFFFFFFF).
            final char[] r = Arrays.toChar(new int[] { 0, 65535, 65536, -1 });
            assertEquals((char) 0, r[0]);
            assertEquals((char) 65535, r[1]);
            assertEquals((char) 0, r[2]);
            assertEquals((char) 65535, r[3]);
            assertEquals(char[].class, r.getClass());
        }

        @Test
        void toChar_intArr_normal() {
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, Arrays.toChar(new int[] { 65, 66, 67 }));
        }

        @Test
        void toChar_intArr_null() {
            final char[] r = Arrays.toChar((int[]) null);
            assertNotNull(r);
            assertEquals(0, r.length);
            assertEquals(char[].class, r.getClass());
        }

        @Test
        void toChar_intArr2D_null() {
            assertEquals(0, Arrays.toChar((int[][]) null).length);
        }

        @Test
        void toChar_intArr2D_preservesStructure() {
            final int[][] in = { { 65, 66 }, null, { 67 } };
            final char[][] r = Arrays.toChar(in);
            assertEquals(3, r.length);
            assertArrayEquals(new char[] { 'A', 'B' }, r[0]);
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
            assertArrayEquals(new char[] { 'C' }, r[2]);
        }

        @Test
        void toChar_intArr3D_null() {
            assertEquals(0, Arrays.toChar((int[][][]) null).length);
        }

        @Test
        void toChar_intArr3D_preservesStructure() {
            final int[][][] in = { { { 65 } }, null };
            final char[][][] r = Arrays.toChar(in);
            assertEquals(2, r.length);
            assertArrayEquals(new char[] { 'A' }, r[0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toChar_intArray() {
            int[] a = { 65, 66, 67 };
            char[] result = Arrays.toChar(a);

            assertArrayEquals(new char[] { 'A', 'B', 'C' }, result);
        }
    }

    @Nested
    class ToByteTests {

        @Test
        void toByte_1D_boolean() {
            boolean[] arr = { true, false, true };
            byte[] result = Arrays.toByte(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(1, result[0]);
            assertEquals(0, result[1]);
        }

        @Test
        void toByte_2D_boolean() {
            boolean[][] arr = { { true, false }, { false, true } };
            byte[][] result = Arrays.toByte(arr);
            assertNotNull(result);
            assertEquals(1, result[0][0]);
            assertEquals(0, result[0][1]);
        }

        @Test
        void toByte_3D_boolean() {
            boolean[][][] arr = { { { true, false } } };
            byte[][][] result = Arrays.toByte(arr);
            assertNotNull(result);
            assertEquals(1, result[0][0][0]);
            assertEquals(0, result[0][0][1]);
        }

        @Test
        void toByte_boolean2DArray() {
            boolean[][] a = { { true, false }, { false, true } };
            byte[][] result = Arrays.toByte(a);

            assertEquals(2, result.length);
            assertEquals(1, result[0][0]);
            assertEquals(0, result[0][1]);
            assertEquals(0, result[1][0]);
            assertEquals(1, result[1][1]);
        }

        @Test
        void toByte_boolean3DArray() {
            boolean[][][] a = { { { true, false } } };
            byte[][][] result = Arrays.toByte(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(0, result[0][0][1]);
        }

        @Test
        void toByte_booleanArr_empty() {
            assertEquals(0, Arrays.toByte(new boolean[0]).length);
        }

        @Test
        void toByte_booleanArr_normal() {
            assertArrayEquals(new byte[] { 1, 0, 1 }, Arrays.toByte(new boolean[] { true, false, true }));
        }

        @Test
        void toByte_booleanArr_null() {
            final byte[] r = Arrays.toByte((boolean[]) null);
            assertNotNull(r);
            assertEquals(0, r.length);
            assertEquals(byte[].class, r.getClass());
        }

        @Test
        void toByte_booleanArr2D_null() {
            assertEquals(0, Arrays.toByte((boolean[][]) null).length);
        }

        @Test
        void toByte_booleanArr2D_preservesStructure() {
            final boolean[][] in = { { true, false }, null, { true } };
            final byte[][] r = Arrays.toByte(in);
            assertEquals(3, r.length);
            assertArrayEquals(new byte[] { 1, 0 }, r[0]);
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
            assertArrayEquals(new byte[] { 1 }, r[2]);
        }

        @Test
        void toByte_booleanArr3D_null() {
            assertEquals(0, Arrays.toByte((boolean[][][]) null).length);
        }

        @Test
        void toByte_booleanArr3D_preservesStructure() {
            final boolean[][][] in = { { { true, false }, { true } }, { { false } } };
            final byte[][][] r = Arrays.toByte(in);
            assertEquals(2, r.length);
            assertArrayEquals(new byte[] { 1, 0 }, r[0][0]);
            assertArrayEquals(new byte[] { 1 }, r[0][1]);
            assertArrayEquals(new byte[] { 0 }, r[1][0]);
        }

        @Test
        void toByte_booleanArray() {
            boolean[] a = { true, false, true };
            byte[] result = Arrays.toByte(a);

            assertArrayEquals(new byte[] { 1, 0, 1 }, result);
        }
    }

    @Nested
    class ToShortTests {

        @Test
        void toShort_1D_byte() {
            byte[] arr = { 1, 2, 3 };
            short[] result = Arrays.toShort(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(1, result[0]);
            assertEquals(3, result[2]);
        }

        @Test
        void toShort_2D_byte() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            short[][] result = Arrays.toShort(arr);
            assertNotNull(result);
            assertEquals(1, result[0][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        void toShort_3D_byte() {
            byte[][][] arr = { { { 10, 20 } } };
            short[][][] result = Arrays.toShort(arr);
            assertNotNull(result);
            assertEquals(10, result[0][0][0]);
        }

        @Test
        void toShort_byte_signExtended() {
            byte[] a = { -1, -128, 0, 127, 1 };
            short[] result = Arrays.toShort(a);
            // byte -> short widening sign-extends: -1 stays -1, -128 stays -128.
            assertArrayEquals(new short[] { -1, -128, 0, 127, 1 }, result);
        }

        @Test
        void toShort_byte2DArray() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            short[][] result = Arrays.toShort(a);

            assertEquals(2, result.length);
            assertEquals(1, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(3, result[1][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        void toShort_byte3DArray() {
            byte[][][] a = { { { 1, 2 } } };
            short[][][] result = Arrays.toShort(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
        }

        @Test
        void toShort_byteArr_empty() {
            assertEquals(0, Arrays.toShort(new byte[0]).length);
        }

        @Test
        void toShort_byteArr_null() {
            final short[] r = Arrays.toShort((byte[]) null);
            assertNotNull(r);
            assertEquals(0, r.length);
            assertEquals(short[].class, r.getClass());
        }

        @Test
        void toShort_byteArr_widensWithSignExtension() {
            // (byte) -1 must widen to (short) -1, not 255.
            assertArrayEquals(new short[] { 10, 20, 30, -1, -128, 127 }, Arrays.toShort(new byte[] { 10, 20, 30, -1, -128, 127 }));
        }

        @Test
        void toShort_byteArr2D_null() {
            assertEquals(0, Arrays.toShort((byte[][]) null).length);
        }

        @Test
        void toShort_byteArr2D_preservesStructure() {
            final byte[][] in = { { 1, -1 }, null, { 127, -128 } };
            final short[][] r = Arrays.toShort(in);
            assertEquals(3, r.length);
            assertArrayEquals(new short[] { 1, -1 }, r[0]);
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
            assertArrayEquals(new short[] { 127, -128 }, r[2]);
        }

        @Test
        void toShort_byteArr3D_null() {
            assertEquals(0, Arrays.toShort((byte[][][]) null).length);
        }

        @Test
        void toShort_byteArr3D_preservesStructure() {
            final byte[][][] in = { { { 1, -1 } }, null };
            final short[][][] r = Arrays.toShort(in);
            assertEquals(2, r.length);
            assertArrayEquals(new short[] { 1, -1 }, r[0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toShort_byteArray() {
            byte[] a = { 1, 2, 3 };
            short[] result = Arrays.toShort(a);

            assertArrayEquals(new short[] { 1, 2, 3 }, result);
        }
    }

    @Nested
    class ToIntTests {

        @Test
        void toInt_1D_byte() {
            byte[] arr = { 1, 2, 3, 4 };
            int[] result = Arrays.toInt(arr);
            assertNotNull(result);
            assertEquals(4, result.length);
            assertEquals(1, result[0]);
            assertEquals(4, result[3]);
        }

        @Test
        void toInt_1D_char() {
            char[] arr = { 'A', 'B', 'C' };
            int[] result = Arrays.toInt(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(65, result[0]);
        }

        @Test
        void toInt_1D_double() {
            double[] arr = { 1.5, 2.7, 3.9 };
            int[] result = Arrays.toInt(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(1, result[0]);
        }

        @Test
        void toInt_1D_float() {
            float[] arr = { 1.5f, 2.7f, 3.2f };
            int[] result = Arrays.toInt(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(1, result[0]);
        }

        @Test
        void toInt_1D_short() {
            short[] arr = { 10, 20, 30 };
            int[] result = Arrays.toInt(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(10, result[0]);
        }

        @Test
        void toInt_3D_double() {
            double[][][] arr = { { { 10.5, 20.5 } } };
            int[][][] result = Arrays.toInt(arr);
            assertNotNull(result);
            assertEquals(10, result[0][0][0]);
        }

        @Test
        void toInt_boolean2DArray() {
            boolean[][] a = { { true, false }, { false, true } };
            int[][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0]);
            assertEquals(0, result[0][1]);
            assertEquals(0, result[1][0]);
            assertEquals(1, result[1][1]);
        }

        @Test
        void toInt_boolean3DArray() {
            boolean[][][] a = { { { true, false } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(0, result[0][0][1]);
        }

        @Test
        void toInt_booleanArr_empty() {
            assertEquals(0, Arrays.toInt(new boolean[0]).length);
        }

        @Test
        void toInt_booleanArr_normal() {
            assertArrayEquals(new int[] { 1, 0, 1 }, Arrays.toInt(new boolean[] { true, false, true }));
        }

        @Test
        void toInt_booleanArr_null() {
            final int[] r = Arrays.toInt((boolean[]) null);
            assertNotNull(r);
            assertEquals(0, r.length);
            assertEquals(int[].class, r.getClass());
        }

        @Test
        void toInt_booleanArr2D_null() {
            assertEquals(0, Arrays.toInt((boolean[][]) null).length);
        }

        @Test
        void toInt_booleanArr2D_preservesStructure() {
            final boolean[][] in = { { true, false }, null };
            final int[][] r = Arrays.toInt(in);
            assertArrayEquals(new int[] { 1, 0 }, r[0]);
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
        }

        @Test
        void toInt_booleanArr3D_null() {
            assertEquals(0, Arrays.toInt((boolean[][][]) null).length);
        }

        @Test
        void toInt_booleanArr3D_preservesStructure() {
            final boolean[][][] in = { { { true } }, null };
            final int[][][] r = Arrays.toInt(in);
            assertArrayEquals(new int[] { 1 }, r[0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toInt_booleanArray() {
            boolean[] a = { true, false, true };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 1, 0, 1 }, result);
        }

        @Test
        void toInt_byte_signExtended() {
            byte[] a = { -1, -128, 0, 127 };
            int[] result = Arrays.toInt(a);
            assertArrayEquals(new int[] { -1, -128, 0, 127 }, result);
        }

        @Test
        void toInt_byte2DArray() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            int[][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(3, result[1][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        void toInt_byte3DArray() {
            byte[][][] a = { { { 1, 2 } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
        }

        @Test
        void toInt_byteArr_empty() {
            assertEquals(0, Arrays.toInt(new byte[0]).length);
        }

        @Test
        void toInt_byteArr_null() {
            assertEquals(0, Arrays.toInt((byte[]) null).length);
        }

        @Test
        void toInt_byteArr_signExtendsNegative() {
            // (byte) -1 must SIGN-extend to int -1, not 255.
            final int[] r = Arrays.toInt(new byte[] { -1, -128, 0, 1, 127 });
            assertArrayEquals(new int[] { -1, -128, 0, 1, 127 }, r);
            assertEquals(int[].class, r.getClass());
        }

        @Test
        void toInt_byteArr2D_null() {
            assertEquals(0, Arrays.toInt((byte[][]) null).length);
        }

        @Test
        void toInt_byteArr2D_preservesStructure() {
            final byte[][] in = { { -1, 1 }, null };
            final int[][] r = Arrays.toInt(in);
            assertArrayEquals(new int[] { -1, 1 }, r[0]);
            assertNotNull(r[1]);
        }

        @Test
        void toInt_byteArr3D_null() {
            assertEquals(0, Arrays.toInt((byte[][][]) null).length);
        }

        @Test
        void toInt_byteArr3D_preservesStructure() {
            final byte[][][] in = { { { -1 } }, null };
            final int[][][] r = Arrays.toInt(in);
            assertArrayEquals(new int[] { -1 }, r[0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toInt_byteArray() {
            byte[] a = { 1, 2, 3 };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 1, 2, 3 }, result);
        }

        @Test
        void toInt_char_zeroExtended() {
            // char is unsigned 16-bit; widening to int must NOT sign-extend.
            char[] a = { '\0', 'ÿ', '￿', '耀' };
            int[] result = Arrays.toInt(a);
            assertArrayEquals(new int[] { 0, 0xFF, 0xFFFF, 0x8000 }, result);
        }

        @Test
        void toInt_char2DArray() {
            char[][] a = { { 'A', 'B' }, { 'C', 'D' } };
            int[][] result = Arrays.toInt(a);

            assertEquals(65, result[0][0]);
            assertEquals(66, result[0][1]);
            assertEquals(67, result[1][0]);
            assertEquals(68, result[1][1]);
        }

        @Test
        void toInt_char3DArray() {
            char[][][] a = { { { 'A', 'B' } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(65, result[0][0][0]);
            assertEquals(66, result[0][0][1]);
        }

        @Test
        void toInt_charArr_empty() {
            assertEquals(0, Arrays.toInt(new char[0]).length);
        }

        @Test
        void toInt_charArr_null() {
            final int[] r = Arrays.toInt((char[]) null);
            assertNotNull(r);
            assertEquals(0, r.length);
        }

        @Test
        void toInt_charArr_zeroExtendsToPositive() {
            // char is unsigned; '￿' (65535) must zero-extend to 65535, NOT sign-extend to -1.
            final int[] r = Arrays.toInt(new char[] { 'A', '\0', '￿' });
            assertArrayEquals(new int[] { 65, 0, 65535 }, r);
            assertEquals(int[].class, r.getClass());
        }

        @Test
        void toInt_charArr2D_null() {
            assertEquals(0, Arrays.toInt((char[][]) null).length);
        }

        @Test
        void toInt_charArr2D_preservesStructure() {
            final char[][] in = { { 'A', 'B' }, null };
            final int[][] r = Arrays.toInt(in);
            assertArrayEquals(new int[] { 65, 66 }, r[0]);
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
        }

        @Test
        void toInt_charArr3D_null() {
            assertEquals(0, Arrays.toInt((char[][][]) null).length);
        }

        @Test
        void toInt_charArr3D_preservesStructure() {
            final char[][][] in = { { { 'A' } }, null };
            final int[][][] r = Arrays.toInt(in);
            assertArrayEquals(new int[] { 65 }, r[0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toInt_charArray() {
            char[] a = { 'A', 'B', 'C' };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 65, 66, 67 }, result);
        }

        @Test
        void toInt_double2DArray() {
            double[][] a = { { 1.9, 2.9 }, { 3.9, 4.9 } };
            int[][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(3, result[1][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        void toInt_double3DArray() {
            double[][][] a = { { { 1.9, 2.9 } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
        }

        @Test
        void toInt_doubleArr_empty() {
            assertEquals(0, Arrays.toInt(new double[0]).length);
        }

        @Test
        void toInt_doubleArr_nanAndInfinity() {
            final int[] r = Arrays.toInt(new double[] { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 1e30, -1e30 });
            assertEquals(0, r[0]);
            assertEquals(Integer.MAX_VALUE, r[1]);
            assertEquals(Integer.MIN_VALUE, r[2]);
            assertEquals(Integer.MAX_VALUE, r[3]);
            assertEquals(Integer.MIN_VALUE, r[4]);
        }

        @Test
        void toInt_doubleArr_null() {
            assertEquals(0, Arrays.toInt((double[]) null).length);
        }

        @Test
        void toInt_doubleArr_truncatesTowardZero() {
            final int[] r = Arrays.toInt(new double[] { 1.7, -1.7, 1.0, -1.0, 0.5, -0.5 });
            assertArrayEquals(new int[] { 1, -1, 1, -1, 0, 0 }, r);
        }

        @Test
        void toInt_doubleArr2D_null() {
            assertEquals(0, Arrays.toInt((double[][]) null).length);
        }

        @Test
        void toInt_doubleArr2D_preservesStructure() {
            final double[][] in = { { Double.NaN, 1.9 }, null };
            final int[][] r = Arrays.toInt(in);
            assertArrayEquals(new int[] { 0, 1 }, r[0]);
            assertNotNull(r[1]);
        }

        @Test
        void toInt_doubleArr3D_null() {
            assertEquals(0, Arrays.toInt((double[][][]) null).length);
        }

        @Test
        void toInt_doubleArr3D_preservesStructure() {
            final double[][][] in = { { { Double.NEGATIVE_INFINITY } }, null };
            final int[][][] r = Arrays.toInt(in);
            assertEquals(Integer.MIN_VALUE, r[0][0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toInt_doubleArray() {
            double[] a = { 1.9, 2.9, 3.9 };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 1, 2, 3 }, result);
        }

        @Test
        void toInt_float2DArray() {
            float[][] a = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            int[][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(3, result[1][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        void toInt_float3DArray() {
            float[][][] a = { { { 1.5f, 2.5f } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
        }

        @Test
        void toInt_floatArr_empty() {
            assertEquals(0, Arrays.toInt(new float[0]).length);
        }

        @Test
        void toInt_floatArr_nanAndInfinity() {
            // Per JLS 5.1.3: NaN -> 0, +Infinity -> Integer.MAX_VALUE, -Infinity -> Integer.MIN_VALUE.
            // Out-of-range finite values also saturate.
            final int[] r = Arrays.toInt(new float[] { Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, 1e30f, -1e30f });
            assertEquals(0, r[0]);
            assertEquals(Integer.MAX_VALUE, r[1]);
            assertEquals(Integer.MIN_VALUE, r[2]);
            assertEquals(Integer.MAX_VALUE, r[3]);
            assertEquals(Integer.MIN_VALUE, r[4]);
        }

        @Test
        void toInt_floatArr_null() {
            assertEquals(0, Arrays.toInt((float[]) null).length);
        }

        @Test
        void toInt_floatArr_truncatesTowardZero() {
            // (int) 1.7f -> 1; (int) -1.7f -> -1 (rounds TOWARD zero, not toward -infinity).
            final int[] r = Arrays.toInt(new float[] { 1.7f, -1.7f, 1.0f, -1.0f, 0.5f, -0.5f });
            assertArrayEquals(new int[] { 1, -1, 1, -1, 0, 0 }, r);
        }

        @Test
        void toInt_floatArr2D_null() {
            assertEquals(0, Arrays.toInt((float[][]) null).length);
        }

        @Test
        void toInt_floatArr2D_preservesStructure() {
            final float[][] in = { { Float.NaN, 1.9f }, null };
            final int[][] r = Arrays.toInt(in);
            assertArrayEquals(new int[] { 0, 1 }, r[0]);
            assertNotNull(r[1]);
        }

        @Test
        void toInt_floatArr3D_null() {
            assertEquals(0, Arrays.toInt((float[][][]) null).length);
        }

        @Test
        void toInt_floatArr3D_preservesStructure() {
            final float[][][] in = { { { Float.POSITIVE_INFINITY } }, null };
            final int[][][] r = Arrays.toInt(in);
            assertEquals(Integer.MAX_VALUE, r[0][0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toInt_floatArray() {
            float[] a = { 1.5f, 2.5f, 3.5f };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 1, 2, 3 }, result);
        }

        @Test
        void toInt_floatArray_naNBecomesZero() {
            // JLS narrowing: NaN -> 0, +Inf saturates to MAX, -Inf saturates to MIN.
            float[] a = { Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, 1.7f, -3.9f };
            int[] result = Arrays.toInt(a);
            assertEquals(0, result[0]);
            assertEquals(Integer.MAX_VALUE, result[1]);
            assertEquals(Integer.MIN_VALUE, result[2]);
            assertEquals(1, result[3]);
            assertEquals(-3, result[4]);
        }

        @Test
        void toInt_short2DArray() {
            short[][] a = { { 1, 2 }, { 3, 4 } };
            int[][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(3, result[1][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        void toInt_short3DArray() {
            short[][][] a = { { { 1, 2 } } };
            int[][][] result = Arrays.toInt(a);

            assertEquals(1, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
        }

        @Test
        void toInt_shortArr_empty() {
            assertEquals(0, Arrays.toInt(new short[0]).length);
        }

        @Test
        void toInt_shortArr_null() {
            assertEquals(0, Arrays.toInt((short[]) null).length);
        }

        @Test
        void toInt_shortArr_signExtendsNegative() {
            final int[] r = Arrays.toInt(new short[] { -1, Short.MIN_VALUE, Short.MAX_VALUE, 0 });
            assertArrayEquals(new int[] { -1, -32768, 32767, 0 }, r);
            assertEquals(int[].class, r.getClass());
        }

        @Test
        void toInt_shortArr2D_null() {
            assertEquals(0, Arrays.toInt((short[][]) null).length);
        }

        @Test
        void toInt_shortArr2D_preservesStructure() {
            final short[][] in = { { -1, 1 }, null };
            final int[][] r = Arrays.toInt(in);
            assertArrayEquals(new int[] { -1, 1 }, r[0]);
            assertNotNull(r[1]);
        }

        @Test
        void toInt_shortArr3D_null() {
            assertEquals(0, Arrays.toInt((short[][][]) null).length);
        }

        @Test
        void toInt_shortArr3D_preservesStructure() {
            final short[][][] in = { { { -1 } }, null };
            final int[][][] r = Arrays.toInt(in);
            assertArrayEquals(new int[] { -1 }, r[0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toInt_shortArray() {
            short[] a = { 1, 2, 3 };
            int[] result = Arrays.toInt(a);

            assertArrayEquals(new int[] { 1, 2, 3 }, result);
        }
    }

    @Nested
    class ToLongTests {

        @Test
        void toLong_1D_byte() {
            byte[] arr = { 1, 2, 3 };
            long[] result = Arrays.toLong(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(1L, result[0]);
        }

        @Test
        void toLong_1D_double() {
            double[] arr = { 1.5, 2.7, 3.9 };
            long[] result = Arrays.toLong(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(1L, result[0]);
        }

        @Test
        void toLong_1D_float() {
            float[] arr = { 1.5f, 2.7f, 3.2f };
            long[] result = Arrays.toLong(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(1L, result[0]);
        }

        @Test
        void toLong_1D_int() {
            int[] arr = { 100, 200, 300 };
            long[] result = Arrays.toLong(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(100L, result[0]);
            assertEquals(300L, result[2]);
        }

        @Test
        void toLong_1D_short() {
            short[] arr = { 10, 20, 30 };
            long[] result = Arrays.toLong(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(10L, result[0]);
        }

        @Test
        void toLong_2D_int() {
            int[][] arr = { { 10, 20 }, { 30, 40 } };
            long[][] result = Arrays.toLong(arr);
            assertNotNull(result);
            assertEquals(10L, result[0][0]);
        }

        @Test
        void toLong_3D_double() {
            double[][][] arr = { { { 100.5, 200.5 } } };
            long[][][] result = Arrays.toLong(arr);
            assertNotNull(result);
            assertEquals(100L, result[0][0][0]);
        }

        @Test
        void toLong_byte_signExtended() {
            byte[] a = { -1, -128, 127 };
            long[] result = Arrays.toLong(a);
            assertArrayEquals(new long[] { -1L, -128L, 127L }, result);
        }

        @Test
        void toLong_byte2DArray() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            long[][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0]);
            assertEquals(2L, result[0][1]);
            assertEquals(3L, result[1][0]);
            assertEquals(4L, result[1][1]);
        }

        @Test
        void toLong_byte3DArray() {
            byte[][][] a = { { { 1, 2 } } };
            long[][][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0][0]);
            assertEquals(2L, result[0][0][1]);
        }

        @Test
        void toLong_byteArr_empty() {
            assertEquals(0, Arrays.toLong(new byte[0]).length);
        }

        @Test
        void toLong_byteArr_null() {
            final long[] r = Arrays.toLong((byte[]) null);
            assertNotNull(r);
            assertEquals(0, r.length);
            assertEquals(long[].class, r.getClass());
        }

        @Test
        void toLong_byteArr_signExtends() {
            assertArrayEquals(new long[] { -1L, -128L, 0L, 127L }, Arrays.toLong(new byte[] { -1, -128, 0, 127 }));
        }

        @Test
        void toLong_byteArr2D_null() {
            assertEquals(0, Arrays.toLong((byte[][]) null).length);
        }

        @Test
        void toLong_byteArr2D_preservesStructure() {
            final byte[][] in = { { -1, 1 }, null };
            final long[][] r = Arrays.toLong(in);
            assertArrayEquals(new long[] { -1L, 1L }, r[0]);
            assertNotNull(r[1]);
        }

        @Test
        void toLong_byteArr3D_null() {
            assertEquals(0, Arrays.toLong((byte[][][]) null).length);
        }

        @Test
        void toLong_byteArr3D_preservesStructure() {
            final byte[][][] in = { { { -1 } }, null };
            final long[][][] r = Arrays.toLong(in);
            assertArrayEquals(new long[] { -1L }, r[0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toLong_byteArray() {
            byte[] a = { 1, 2, 3 };
            long[] result = Arrays.toLong(a);

            assertArrayEquals(new long[] { 1L, 2L, 3L }, result);
        }

        @Test
        void toLong_double2DArray() {
            double[][] a = { { 1.9, 2.9 }, { 3.9, 4.9 } };
            long[][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0]);
            assertEquals(2L, result[0][1]);
            assertEquals(3L, result[1][0]);
            assertEquals(4L, result[1][1]);
        }

        @Test
        void toLong_double3DArray() {
            double[][][] a = { { { 1.9, 2.9 } } };
            long[][][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0][0]);
            assertEquals(2L, result[0][0][1]);
        }

        @Test
        void toLong_doubleArr_empty() {
            assertEquals(0, Arrays.toLong(new double[0]).length);
        }

        @Test
        void toLong_doubleArr_nanAndInfinity() {
            final long[] r = Arrays.toLong(new double[] { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY });
            assertEquals(0L, r[0]);
            assertEquals(Long.MAX_VALUE, r[1]);
            assertEquals(Long.MIN_VALUE, r[2]);
        }

        @Test
        void toLong_doubleArr_null() {
            assertEquals(0, Arrays.toLong((double[]) null).length);
        }

        @Test
        void toLong_doubleArr_truncatesTowardZero() {
            assertArrayEquals(new long[] { 1L, -1L, 0L, 0L }, Arrays.toLong(new double[] { 1.7, -1.7, 0.5, -0.5 }));
        }

        @Test
        void toLong_doubleArr2D_null() {
            assertEquals(0, Arrays.toLong((double[][]) null).length);
        }

        @Test
        void toLong_doubleArr2D_preservesStructure() {
            final double[][] in = { { Double.NaN, 2.5 }, null };
            final long[][] r = Arrays.toLong(in);
            assertArrayEquals(new long[] { 0L, 2L }, r[0]);
            assertNotNull(r[1]);
        }

        @Test
        void toLong_doubleArr3D_null() {
            assertEquals(0, Arrays.toLong((double[][][]) null).length);
        }

        @Test
        void toLong_doubleArr3D_preservesStructure() {
            final double[][][] in = { { { Double.POSITIVE_INFINITY } }, null };
            final long[][][] r = Arrays.toLong(in);
            assertEquals(Long.MAX_VALUE, r[0][0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toLong_doubleArray() {
            double[] a = { 1.9, 2.9, 3.9 };
            long[] result = Arrays.toLong(a);

            assertArrayEquals(new long[] { 1L, 2L, 3L }, result);
        }

        @Test
        void toLong_doubleArray_naNBecomesZero() {
            double[] a = { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 1.9, -2.1 };
            long[] result = Arrays.toLong(a);
            assertEquals(0L, result[0]);
            assertEquals(Long.MAX_VALUE, result[1]);
            assertEquals(Long.MIN_VALUE, result[2]);
            assertEquals(1L, result[3]);
            assertEquals(-2L, result[4]);
        }

        @Test
        void toLong_float2DArray() {
            float[][] a = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            long[][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0]);
            assertEquals(2L, result[0][1]);
            assertEquals(3L, result[1][0]);
            assertEquals(4L, result[1][1]);
        }

        @Test
        void toLong_float3DArray() {
            float[][][] a = { { { 1.5f, 2.5f } } };
            long[][][] result = Arrays.toLong(a);

            assertEquals(1L, result[0][0][0]);
            assertEquals(2L, result[0][0][1]);
        }

        @Test
        void toLong_floatArr_empty() {
            assertEquals(0, Arrays.toLong(new float[0]).length);
        }

        @Test
        void toLong_floatArr_nanAndInfinity() {
            final long[] r = Arrays.toLong(new float[] { Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY });
            assertEquals(0L, r[0]);
            assertEquals(Long.MAX_VALUE, r[1]);
            assertEquals(Long.MIN_VALUE, r[2]);
        }

        @Test
        void toLong_floatArr_null() {
            assertEquals(0, Arrays.toLong((float[]) null).length);
        }

        @Test
        void toLong_floatArr_truncatesTowardZero() {
            assertArrayEquals(new long[] { 1L, -1L, 0L, 0L }, Arrays.toLong(new float[] { 1.7f, -1.7f, 0.5f, -0.5f }));
        }

        @Test
        void toLong_floatArr2D_null() {
            assertEquals(0, Arrays.toLong((float[][]) null).length);
        }

        @Test
        void toLong_floatArr2D_preservesStructure() {
            final float[][] in = { { Float.NaN, 2.5f }, null };
            final long[][] r = Arrays.toLong(in);
            assertArrayEquals(new long[] { 0L, 2L }, r[0]);
            assertNotNull(r[1]);
        }

        @Test
        void toLong_floatArr3D_null() {
            assertEquals(0, Arrays.toLong((float[][][]) null).length);
        }

        @Test
        void toLong_floatArr3D_preservesStructure() {
            final float[][][] in = { { { Float.POSITIVE_INFINITY } }, null };
            final long[][][] r = Arrays.toLong(in);
            assertEquals(Long.MAX_VALUE, r[0][0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toLong_floatArray() {
            float[] a = { 1.5f, 2.5f, 3.5f };
            long[] result = Arrays.toLong(a);

            assertArrayEquals(new long[] { 1L, 2L, 3L }, result);
        }

        @Test
        void toLong_int2DArray() {
            int[][] a = { { 100, 200 }, { 300, 400 } };
            long[][] result = Arrays.toLong(a);

            assertEquals(100L, result[0][0]);
            assertEquals(200L, result[0][1]);
            assertEquals(300L, result[1][0]);
            assertEquals(400L, result[1][1]);
        }

        @Test
        void toLong_int3DArray() {
            int[][][] a = { { { 100, 200 } } };
            long[][][] result = Arrays.toLong(a);

            assertEquals(100L, result[0][0][0]);
            assertEquals(200L, result[0][0][1]);
        }

        @Test
        void toLong_intArr_empty() {
            assertEquals(0, Arrays.toLong(new int[0]).length);
        }

        @Test
        void toLong_intArr_null() {
            assertEquals(0, Arrays.toLong((int[]) null).length);
        }

        @Test
        void toLong_intArr_widens() {
            assertArrayEquals(new long[] { -1L, Integer.MIN_VALUE, Integer.MAX_VALUE, 0L },
                    Arrays.toLong(new int[] { -1, Integer.MIN_VALUE, Integer.MAX_VALUE, 0 }));
        }

        @Test
        void toLong_intArr2D_null() {
            assertEquals(0, Arrays.toLong((int[][]) null).length);
        }

        @Test
        void toLong_intArr2D_preservesStructure() {
            final int[][] in = { { -1, Integer.MAX_VALUE }, null };
            final long[][] r = Arrays.toLong(in);
            assertArrayEquals(new long[] { -1L, Integer.MAX_VALUE }, r[0]);
            assertNotNull(r[1]);
        }

        @Test
        void toLong_intArr3D_null() {
            assertEquals(0, Arrays.toLong((int[][][]) null).length);
        }

        @Test
        void toLong_intArr3D_preservesStructure() {
            final int[][][] in = { { { -1 } }, null };
            final long[][][] r = Arrays.toLong(in);
            assertArrayEquals(new long[] { -1L }, r[0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toLong_intArray() {
            int[] a = { 100, 200, 300 };
            long[] result = Arrays.toLong(a);

            assertArrayEquals(new long[] { 100L, 200L, 300L }, result);
        }

        @Test
        void toLong_short_signExtended() {
            short[] a = { -1, Short.MIN_VALUE, Short.MAX_VALUE };
            long[] result = Arrays.toLong(a);
            assertArrayEquals(new long[] { -1L, Short.MIN_VALUE, Short.MAX_VALUE }, result);
        }

        @Test
        void toLong_short2DArray() {
            short[][] a = { { 10, 20 }, { 30, 40 } };
            long[][] result = Arrays.toLong(a);

            assertEquals(10L, result[0][0]);
            assertEquals(20L, result[0][1]);
            assertEquals(30L, result[1][0]);
            assertEquals(40L, result[1][1]);
        }

        @Test
        void toLong_short3DArray() {
            short[][][] a = { { { 10, 20 } } };
            long[][][] result = Arrays.toLong(a);

            assertEquals(10L, result[0][0][0]);
            assertEquals(20L, result[0][0][1]);
        }

        @Test
        void toLong_shortArr_empty() {
            assertEquals(0, Arrays.toLong(new short[0]).length);
        }

        @Test
        void toLong_shortArr_null() {
            assertEquals(0, Arrays.toLong((short[]) null).length);
        }

        @Test
        void toLong_shortArr_signExtends() {
            assertArrayEquals(new long[] { -1L, -32768L, 32767L, 0L }, Arrays.toLong(new short[] { -1, Short.MIN_VALUE, Short.MAX_VALUE, 0 }));
        }

        @Test
        void toLong_shortArr2D_null() {
            assertEquals(0, Arrays.toLong((short[][]) null).length);
        }

        @Test
        void toLong_shortArr2D_preservesStructure() {
            final short[][] in = { { -1, 1 }, null };
            final long[][] r = Arrays.toLong(in);
            assertArrayEquals(new long[] { -1L, 1L }, r[0]);
            assertNotNull(r[1]);
        }

        @Test
        void toLong_shortArr3D_null() {
            assertEquals(0, Arrays.toLong((short[][][]) null).length);
        }

        @Test
        void toLong_shortArr3D_preservesStructure() {
            final short[][][] in = { { { -1 } }, null };
            final long[][][] r = Arrays.toLong(in);
            assertArrayEquals(new long[] { -1L }, r[0][0]);
            assertNotNull(r[1]);
        }

        @Test
        void toLong_shortArray() {
            short[] a = { 10, 20, 30 };
            long[] result = Arrays.toLong(a);

            assertArrayEquals(new long[] { 10L, 20L, 30L }, result);
        }
    }

    @Nested
    class ToFloatTests {

        @Test
        void classJavadoc_toFloat() {
            // Line 107: int[] integers = {1, 2, 3, 4}; => toFloat => {1.0f, 2.0f, 3.0f, 4.0f}
            int[] integers = { 1, 2, 3, 4 };
            float[] floats = Arrays.toFloat(integers);
            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, floats);
        }

        @Test
        void intToFloatConversion2D() {
            // Line 13847-13849: toFloat({{1,2},{3,4}}) => {{1.0f,2.0f},{3.0f,4.0f}}
            int[][] source = { { 1, 2 }, { 3, 4 } };
            float[][] result = Arrays.toFloat(source);
            assertArrayEquals(new float[] { 1.0f, 2.0f }, result[0]);
            assertArrayEquals(new float[] { 3.0f, 4.0f }, result[1]);
        }

        @Test
        void toFloat_1D_byte() {
            byte[] arr = { 1, 2, 3 };
            float[] result = Arrays.toFloat(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(1.0f, result[0], 0.001f);
        }

        @Test
        void toFloat_1D_int() {
            int[] arr = { 100, 200, 300 };
            float[] result = Arrays.toFloat(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(100.0f, result[0], 0.001f);
        }

        @Test
        void toFloat_1D_long() {
            long[] arr = { 1000L, 2000L, 3000L };
            float[] result = Arrays.toFloat(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(1000.0f, result[0], 0.001f);
        }

        @Test
        void toFloat_1D_short() {
            short[] arr = { 10, 20, 30 };
            float[] result = Arrays.toFloat(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(10.0f, result[0], 0.001f);
        }

        @Test
        void toFloat_2D_int() {
            int[][] arr = { { 10, 20 }, { 30, 40 } };
            float[][] result = Arrays.toFloat(arr);
            assertNotNull(result);
            assertEquals(10.0f, result[0][0], 0.001f);
        }

        @Test
        void toFloat_3D_long() {
            long[][][] arr = { { { 100L, 200L } } };
            float[][][] result = Arrays.toFloat(arr);
            assertNotNull(result);
            assertEquals(100.0f, result[0][0][0], 0.001f);
        }

        @Test
        void toFloat_byte2DArray() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            float[][] result = Arrays.toFloat(a);

            assertEquals(1.0f, result[0][0], 0.0001f);
            assertEquals(2.0f, result[0][1], 0.0001f);
            assertEquals(3.0f, result[1][0], 0.0001f);
            assertEquals(4.0f, result[1][1], 0.0001f);
        }

        @Test
        void toFloat_byte3DArray() {
            byte[][][] a = { { { 1, 2 } } };
            float[][][] result = Arrays.toFloat(a);

            assertEquals(1.0f, result[0][0][0], 0.0001f);
            assertEquals(2.0f, result[0][0][1], 0.0001f);
        }

        @Test
        void toFloat_byteArr_empty() {
            assertEquals(0, Arrays.toFloat(new byte[0]).length);
        }

        @Test
        void toFloat_byteArr_null() {
            assertEquals(0, Arrays.toFloat((byte[]) null).length);
        }

        @Test
        void toFloat_byteArr_widensExact() {
            assertArrayEquals(new float[] { -1f, 0f, 127f, -128f }, Arrays.toFloat(new byte[] { -1, 0, 127, -128 }), 0f);
        }

        @Test
        void toFloat_byteArr2D_null() {
            assertEquals(0, Arrays.toFloat((byte[][]) null).length);
        }

        @Test
        void toFloat_byteArr2D_preservesStructure() {
            final byte[][] in = { { 1, -1 }, null };
            final float[][] r = Arrays.toFloat(in);
            assertArrayEquals(new float[] { 1f, -1f }, r[0], 0f);
            assertNotNull(r[1]);
        }

        @Test
        void toFloat_byteArr3D_null() {
            assertEquals(0, Arrays.toFloat((byte[][][]) null).length);
        }

        @Test
        void toFloat_byteArr3D_preservesStructure() {
            final byte[][][] in = { { { -1 } }, null };
            final float[][][] r = Arrays.toFloat(in);
            assertArrayEquals(new float[] { -1f }, r[0][0], 0f);
            assertNotNull(r[1]);
        }

        @Test
        void toFloat_byteArray() {
            byte[] a = { 1, 2, 3 };
            float[] result = Arrays.toFloat(a);

            assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, result, 0.0001f);
        }

        @Test
        void toFloat_int2DArray() {
            int[][] a = { { 100, 200 }, { 300, 400 } };
            float[][] result = Arrays.toFloat(a);

            assertEquals(100.0f, result[0][0], 0.0001f);
            assertEquals(200.0f, result[0][1], 0.0001f);
            assertEquals(300.0f, result[1][0], 0.0001f);
            assertEquals(400.0f, result[1][1], 0.0001f);
        }

        @Test
        void toFloat_int3DArray() {
            int[][][] a = { { { 100, 200 } } };
            float[][][] result = Arrays.toFloat(a);

            assertEquals(100.0f, result[0][0][0], 0.0001f);
            assertEquals(200.0f, result[0][0][1], 0.0001f);
        }

        @Test
        void toFloat_intArr_empty() {
            assertEquals(0, Arrays.toFloat(new int[0]).length);
        }

        @Test
        void toFloat_intArr_minMax() {
            final float[] r = Arrays.toFloat(new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE, 0, -1 });
            assertEquals(Integer.MIN_VALUE, r[0], 0f);
            assertEquals(Integer.MAX_VALUE, r[1], 0f);
            assertEquals(0f, r[2], 0f);
            assertEquals(-1f, r[3], 0f);
        }

        @Test
        void toFloat_intArr_null() {
            assertEquals(0, Arrays.toFloat((int[]) null).length);
        }

        @Test
        void toFloat_intArr_precisionLossPast2Pow24() {
            // 2^24 = 16777216 — exactly representable in float.
            // 16777217 cannot be represented; (float) 16777217 -> 16777216.0f
            // 16777218 IS representable. This documents the precision-loss frontier.
            final float[] r = Arrays.toFloat(new int[] { 16_777_216, 16_777_217, 16_777_218 });
            assertEquals(16777216.0f, r[0], 0f);
            assertEquals(16777216.0f, r[1], 0f); // precision loss
            assertEquals(16777218.0f, r[2], 0f);
        }

        @Test
        void toFloat_intArr2D_null() {
            assertEquals(0, Arrays.toFloat((int[][]) null).length);
        }

        @Test
        void toFloat_intArr2D_preservesStructure() {
            final int[][] in = { { -1, 1 }, null };
            final float[][] r = Arrays.toFloat(in);
            assertArrayEquals(new float[] { -1f, 1f }, r[0], 0f);
            assertNotNull(r[1]);
        }

        @Test
        void toFloat_intArr3D_null() {
            assertEquals(0, Arrays.toFloat((int[][][]) null).length);
        }

        @Test
        void toFloat_intArr3D_preservesStructure() {
            final int[][][] in = { { { -1 } }, null };
            final float[][][] r = Arrays.toFloat(in);
            assertArrayEquals(new float[] { -1f }, r[0][0], 0f);
            assertNotNull(r[1]);
        }

        @Test
        void toFloat_intArray() {
            int[] a = { 100, 200, 300 };
            float[] result = Arrays.toFloat(a);

            assertArrayEquals(new float[] { 100.0f, 200.0f, 300.0f }, result, 0.0001f);
        }

        @Test
        void toFloat_long_widening() {
            long[] a = { 0L, 1L, -1L, Long.MAX_VALUE };
            float[] result = Arrays.toFloat(a);
            assertEquals(0.0f, result[0], 0.0f);
            assertEquals(1.0f, result[1], 0.0f);
            assertEquals(-1.0f, result[2], 0.0f);
            // Long.MAX_VALUE rounds to 2^63 in float (loss of precision is documented).
            assertEquals(Long.MAX_VALUE, result[3], 0.0f);
        }

        @Test
        void toFloat_long2DArray() {
            long[][] a = { { 1000L, 2000L }, { 3000L, 4000L } };
            float[][] result = Arrays.toFloat(a);

            assertEquals(1000.0f, result[0][0], 0.0001f);
            assertEquals(2000.0f, result[0][1], 0.0001f);
            assertEquals(3000.0f, result[1][0], 0.0001f);
            assertEquals(4000.0f, result[1][1], 0.0001f);
        }

        @Test
        void toFloat_long3DArray() {
            long[][][] a = { { { 1000L, 2000L } } };
            float[][][] result = Arrays.toFloat(a);

            assertEquals(1000.0f, result[0][0][0], 0.0001f);
            assertEquals(2000.0f, result[0][0][1], 0.0001f);
        }

        @Test
        void toFloat_longArr_empty() {
            assertEquals(0, Arrays.toFloat(new long[0]).length);
        }

        @Test
        void toFloat_longArr_minMax() {
            // (float) Long.MAX_VALUE and Long.MIN_VALUE are finite (large) per JLS widening.
            final float[] r = Arrays.toFloat(new long[] { Long.MIN_VALUE, Long.MAX_VALUE });
            assertEquals(Long.MIN_VALUE, r[0], 0f);
            assertEquals(Long.MAX_VALUE, r[1], 0f);
            assertFalse(Float.isNaN(r[0]));
            assertFalse(Float.isNaN(r[1]));
        }

        @Test
        void toFloat_longArr_null() {
            assertEquals(0, Arrays.toFloat((long[]) null).length);
        }

        @Test
        void toFloat_longArr_precisionLossPast2Pow24() {
            // Same precision frontier as int[] but using long inputs.
            final float[] r = Arrays.toFloat(new long[] { 16_777_216L, 16_777_217L });
            assertEquals(16777216.0f, r[0], 0f);
            assertEquals(16777216.0f, r[1], 0f);
        }

        @Test
        void toFloat_longArr2D_null() {
            assertEquals(0, Arrays.toFloat((long[][]) null).length);
        }

        @Test
        void toFloat_longArr2D_preservesStructure() {
            final long[][] in = { { -1L, 1L }, null };
            final float[][] r = Arrays.toFloat(in);
            assertArrayEquals(new float[] { -1f, 1f }, r[0], 0f);
            assertNotNull(r[1]);
        }

        @Test
        void toFloat_longArr3D_null() {
            assertEquals(0, Arrays.toFloat((long[][][]) null).length);
        }

        @Test
        void toFloat_longArr3D_preservesStructure() {
            final long[][][] in = { { { -1L } }, null };
            final float[][][] r = Arrays.toFloat(in);
            assertArrayEquals(new float[] { -1f }, r[0][0], 0f);
            assertNotNull(r[1]);
        }

        @Test
        void toFloat_longArray() {
            long[] a = { 1000L, 2000L, 3000L };
            float[] result = Arrays.toFloat(a);

            assertArrayEquals(new float[] { 1000.0f, 2000.0f, 3000.0f }, result, 0.0001f);
        }

        @Test
        void toFloat_short2DArray() {
            short[][] a = { { 10, 20 }, { 30, 40 } };
            float[][] result = Arrays.toFloat(a);

            assertEquals(10.0f, result[0][0], 0.0001f);
            assertEquals(20.0f, result[0][1], 0.0001f);
            assertEquals(30.0f, result[1][0], 0.0001f);
            assertEquals(40.0f, result[1][1], 0.0001f);
        }

        @Test
        void toFloat_short3DArray() {
            short[][][] a = { { { 10, 20 } } };
            float[][][] result = Arrays.toFloat(a);

            assertEquals(10.0f, result[0][0][0], 0.0001f);
            assertEquals(20.0f, result[0][0][1], 0.0001f);
        }

        @Test
        void toFloat_shortArr_empty() {
            assertEquals(0, Arrays.toFloat(new short[0]).length);
        }

        @Test
        void toFloat_shortArr_null() {
            assertEquals(0, Arrays.toFloat((short[]) null).length);
        }

        @Test
        void toFloat_shortArr_widensExact() {
            assertArrayEquals(new float[] { -1f, 32767f, -32768f }, Arrays.toFloat(new short[] { -1, Short.MAX_VALUE, Short.MIN_VALUE }), 0f);
        }

        @Test
        void toFloat_shortArr2D_null() {
            assertEquals(0, Arrays.toFloat((short[][]) null).length);
        }

        @Test
        void toFloat_shortArr2D_preservesStructure() {
            final short[][] in = { { -1, 1 }, null };
            final float[][] r = Arrays.toFloat(in);
            assertArrayEquals(new float[] { -1f, 1f }, r[0], 0f);
            assertNotNull(r[1]);
        }

        @Test
        void toFloat_shortArr3D_null() {
            assertEquals(0, Arrays.toFloat((short[][][]) null).length);
        }

        @Test
        void toFloat_shortArr3D_preservesStructure() {
            final short[][][] in = { { { -1 } }, null };
            final float[][][] r = Arrays.toFloat(in);
            assertArrayEquals(new float[] { -1f }, r[0][0], 0f);
            assertNotNull(r[1]);
        }

        @Test
        void toFloat_shortArray() {
            short[] a = { 10, 20, 30 };
            float[] result = Arrays.toFloat(a);

            assertArrayEquals(new float[] { 10.0f, 20.0f, 30.0f }, result, 0.0001f);
        }
    }

    @Nested
    class ToDoubleTests {

        @Test
        void classJavadoc_toDouble() {
            // Line 105-106: int[] integers = {1, 2, 3, 4}; => toDouble => {1.0, 2.0, 3.0, 4.0}
            int[] integers = { 1, 2, 3, 4 };
            double[] doubles = Arrays.toDouble(integers);
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, doubles);
        }

        @Test
        void intToDoubleConversion2D() {
            // Line 14195-14197: toDouble({{1,2},{3,4}}) => {{1.0,2.0},{3.0,4.0}}
            int[][] source = { { 1, 2 }, { 3, 4 } };
            double[][] result = Arrays.toDouble(source);
            assertArrayEquals(new double[] { 1.0, 2.0 }, result[0]);
            assertArrayEquals(new double[] { 3.0, 4.0 }, result[1]);
        }

        @Test
        void nullHandling_typeConversion() {
            int[] arr = null;
            double[] result = Arrays.toDouble(arr);
            assertEquals(0, result.length);
        }

        @Test
        void toDouble_1D_byte() {
            byte[] arr = { 1, 2, 3 };
            double[] result = Arrays.toDouble(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(1.0, result[0], 0.001);
        }

        @Test
        void toDouble_1D_float() {
            float[] arr = { 1.5f, 2.5f, 3.5f };
            double[] result = Arrays.toDouble(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertTrue(Math.abs(1.5 - result[0]) < 0.01);
        }

        @Test
        void toDouble_1D_int() {
            int[] arr = { 100, 200, 300 };
            double[] result = Arrays.toDouble(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(100.0, result[0], 0.001);
            assertEquals(300.0, result[2], 0.001);
        }

        @Test
        void toDouble_1D_long() {
            long[] arr = { 1000L, 2000L, 3000L };
            double[] result = Arrays.toDouble(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(1000.0, result[0], 0.001);
        }

        @Test
        void toDouble_1D_short() {
            short[] arr = { 10, 20, 30 };
            double[] result = Arrays.toDouble(arr);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(10.0, result[0], 0.001);
        }

        @Test
        void toDouble_2D_int() {
            int[][] arr = { { 10, 20 }, { 30, 40 } };
            double[][] result = Arrays.toDouble(arr);
            assertNotNull(result);
            assertEquals(10.0, result[0][0], 0.001);
            assertEquals(40.0, result[1][1], 0.001);
        }

        @Test
        void toDouble_3D_float() {
            float[][][] arr = { { { 1.5f, 2.5f } } };
            double[][][] result = Arrays.toDouble(arr);
            assertNotNull(result);
            assertTrue(Math.abs(1.5 - result[0][0][0]) < 0.01);
        }

        @Test
        void toDouble_byte2DArray() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            double[][] result = Arrays.toDouble(a);

            assertEquals(1.0, result[0][0], 0.0001);
            assertEquals(2.0, result[0][1], 0.0001);
            assertEquals(3.0, result[1][0], 0.0001);
            assertEquals(4.0, result[1][1], 0.0001);
        }

        @Test
        void toDouble_byte3DArray() {
            byte[][][] a = { { { 1, 2 } } };
            double[][][] result = Arrays.toDouble(a);

            assertEquals(1.0, result[0][0][0], 0.0001);
            assertEquals(2.0, result[0][0][1], 0.0001);
        }

        @Test
        void toDouble_byteArr_empty() {
            assertEquals(0, Arrays.toDouble(new byte[0]).length);
        }

        @Test
        void toDouble_byteArr_null() {
            assertEquals(0, Arrays.toDouble((byte[]) null).length);
        }

        @Test
        void toDouble_byteArr_widensExact() {
            assertArrayEquals(new double[] { -1d, 0d, 127d, -128d }, Arrays.toDouble(new byte[] { -1, 0, 127, -128 }), 0d);
        }

        @Test
        void toDouble_byteArr2D_null() {
            assertEquals(0, Arrays.toDouble((byte[][]) null).length);
        }

        @Test
        void toDouble_byteArr2D_preservesStructure() {
            final byte[][] in = { { -1, 1 }, null };
            final double[][] r = Arrays.toDouble(in);
            assertArrayEquals(new double[] { -1d, 1d }, r[0], 0d);
            assertNotNull(r[1]);
        }

        @Test
        void toDouble_byteArr3D_null() {
            assertEquals(0, Arrays.toDouble((byte[][][]) null).length);
        }

        @Test
        void toDouble_byteArr3D_preservesStructure() {
            final byte[][][] in = { { { -1 } }, null };
            final double[][][] r = Arrays.toDouble(in);
            assertArrayEquals(new double[] { -1d }, r[0][0], 0d);
            assertNotNull(r[1]);
        }

        @Test
        void toDouble_byteArray() {
            byte[] a = { 1, 2, 3 };
            double[] result = Arrays.toDouble(a);

            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, result, 0.0001);
        }

        @Test
        void toDouble_float2DArray() {
            float[][] a = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            double[][] result = Arrays.toDouble(a);

            assertEquals(1.5, result[0][0], 0.0001);
            assertEquals(2.5, result[0][1], 0.0001);
            assertEquals(3.5, result[1][0], 0.0001);
            assertEquals(4.5, result[1][1], 0.0001);
        }

        @Test
        void toDouble_float3DArray() {
            float[][][] a = { { { 1.5f, 2.5f } } };
            double[][][] result = Arrays.toDouble(a);

            assertEquals(1.5, result[0][0][0], 0.0001);
            assertEquals(2.5, result[0][0][1], 0.0001);
        }

        @Test
        void toDouble_floatArr_empty() {
            assertEquals(0, Arrays.toDouble(new float[0]).length);
        }

        @Test
        void toDouble_floatArr_null() {
            assertEquals(0, Arrays.toDouble((float[]) null).length);
        }

        @Test
        void toDouble_floatArr_widensFiniteAndSpecials() {
            final double[] r = Arrays.toDouble(new float[] { 1.0f, -1.0f, Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY });
            assertEquals(1.0d, r[0], 0d);
            assertEquals(-1.0d, r[1], 0d);
            assertTrue(Double.isNaN(r[2]));
            assertEquals(Double.POSITIVE_INFINITY, r[3], 0d);
            assertEquals(Double.NEGATIVE_INFINITY, r[4], 0d);
        }

        @Test
        void toDouble_floatArr2D_null() {
            assertEquals(0, Arrays.toDouble((float[][]) null).length);
        }

        @Test
        void toDouble_floatArr2D_preservesStructure() {
            final float[][] in = { { 1.5f, -1.5f }, null };
            final double[][] r = Arrays.toDouble(in);
            assertEquals(1.5d, r[0][0], 0d);
            assertEquals(-1.5d, r[0][1], 0d);
            assertNotNull(r[1]);
        }

        @Test
        void toDouble_floatArr3D_null() {
            assertEquals(0, Arrays.toDouble((float[][][]) null).length);
        }

        @Test
        void toDouble_floatArr3D_preservesStructure() {
            final float[][][] in = { { { 1.5f } }, null };
            final double[][][] r = Arrays.toDouble(in);
            assertEquals(1.5d, r[0][0][0], 0d);
            assertNotNull(r[1]);
        }

        @Test
        void toDouble_floatArray() {
            float[] a = { 1.5f, 2.5f, 3.5f };
            double[] result = Arrays.toDouble(a);

            assertArrayEquals(new double[] { 1.5, 2.5, 3.5 }, result, 0.0001);
        }

        @Test
        void toDouble_int2DArray() {
            int[][] a = { { 100, 200 }, { 300, 400 } };
            double[][] result = Arrays.toDouble(a);

            assertEquals(100.0, result[0][0], 0.0001);
            assertEquals(200.0, result[0][1], 0.0001);
            assertEquals(300.0, result[1][0], 0.0001);
            assertEquals(400.0, result[1][1], 0.0001);
        }

        @Test
        void toDouble_int3DArray() {
            int[][][] a = { { { 100, 200 } } };
            double[][][] result = Arrays.toDouble(a);

            assertEquals(100.0, result[0][0][0], 0.0001);
            assertEquals(200.0, result[0][0][1], 0.0001);
        }

        @Test
        void toDouble_intArr_empty() {
            assertEquals(0, Arrays.toDouble(new int[0]).length);
        }

        @Test
        void toDouble_intArr_null() {
            assertEquals(0, Arrays.toDouble((int[]) null).length);
        }

        @Test
        void toDouble_intArr_widensExact() {
            // int widens to double exactly (double has 53-bit significand >= 32-bit int).
            assertArrayEquals(new double[] { Integer.MIN_VALUE, -1d, 0d, 1d, Integer.MAX_VALUE },
                    Arrays.toDouble(new int[] { Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE }), 0d);
        }

        @Test
        void toDouble_intArr2D_null() {
            assertEquals(0, Arrays.toDouble((int[][]) null).length);
        }

        @Test
        void toDouble_intArr2D_preservesStructure() {
            final int[][] in = { { -1, 1 }, null };
            final double[][] r = Arrays.toDouble(in);
            assertArrayEquals(new double[] { -1d, 1d }, r[0], 0d);
            assertNotNull(r[1]);
        }

        @Test
        void toDouble_intArr3D_null() {
            assertEquals(0, Arrays.toDouble((int[][][]) null).length);
        }

        @Test
        void toDouble_intArr3D_preservesStructure() {
            final int[][][] in = { { { -1 } }, null };
            final double[][][] r = Arrays.toDouble(in);
            assertArrayEquals(new double[] { -1d }, r[0][0], 0d);
            assertNotNull(r[1]);
        }

        @Test
        void toDouble_intArray() {
            int[] a = { 100, 200, 300 };
            double[] result = Arrays.toDouble(a);

            assertArrayEquals(new double[] { 100.0, 200.0, 300.0 }, result, 0.0001);
        }

        @Test
        void toDouble_long2DArray() {
            long[][] a = { { 1000L, 2000L }, { 3000L, 4000L } };
            double[][] result = Arrays.toDouble(a);

            assertEquals(1000.0, result[0][0], 0.0001);
            assertEquals(2000.0, result[0][1], 0.0001);
            assertEquals(3000.0, result[1][0], 0.0001);
            assertEquals(4000.0, result[1][1], 0.0001);
        }

        @Test
        void toDouble_long3DArray() {
            long[][][] a = { { { 1000L, 2000L } } };
            double[][][] result = Arrays.toDouble(a);

            assertEquals(1000.0, result[0][0][0], 0.0001);
            assertEquals(2000.0, result[0][0][1], 0.0001);
        }

        @Test
        void toDouble_longArr_empty() {
            assertEquals(0, Arrays.toDouble(new long[0]).length);
        }

        @Test
        void toDouble_longArr_minMax() {
            final double[] r = Arrays.toDouble(new long[] { Long.MIN_VALUE, Long.MAX_VALUE });
            assertEquals(Long.MIN_VALUE, r[0], 0d);
            assertEquals(Long.MAX_VALUE, r[1], 0d);
        }

        @Test
        void toDouble_longArr_null() {
            assertEquals(0, Arrays.toDouble((long[]) null).length);
        }

        @Test
        void toDouble_longArr_precisionLossPast2Pow53() {
            // 2^53 = 9007199254740992 is exactly representable.
            // 2^53 + 1 = 9007199254740993 cannot be represented; widens to 9007199254740992.0.
            final long pow53 = 1L << 53; // 9007199254740992
            final double[] r = Arrays.toDouble(new long[] { pow53, pow53 + 1L, pow53 + 2L });
            assertEquals(pow53, r[0], 0d);
            assertEquals(pow53, r[1], 0d); // documented precision loss
            assertEquals(pow53 + 2L, r[2], 0d);
        }

        @Test
        void toDouble_longArr2D_null() {
            assertEquals(0, Arrays.toDouble((long[][]) null).length);
        }

        @Test
        void toDouble_longArr2D_preservesStructure() {
            final long[][] in = { { -1L, 1L }, null };
            final double[][] r = Arrays.toDouble(in);
            assertArrayEquals(new double[] { -1d, 1d }, r[0], 0d);
            assertNotNull(r[1]);
        }

        @Test
        void toDouble_longArr3D_null() {
            assertEquals(0, Arrays.toDouble((long[][][]) null).length);
        }

        @Test
        void toDouble_longArr3D_preservesStructure() {
            final long[][][] in = { { { -1L } }, null };
            final double[][][] r = Arrays.toDouble(in);
            assertArrayEquals(new double[] { -1d }, r[0][0], 0d);
            assertNotNull(r[1]);
        }

        @Test
        void toDouble_longArray() {
            long[] a = { 1000L, 2000L, 3000L };
            double[] result = Arrays.toDouble(a);

            assertArrayEquals(new double[] { 1000.0, 2000.0, 3000.0 }, result, 0.0001);
        }

        @Test
        void toDouble_short2DArray() {
            short[][] a = { { 10, 20 }, { 30, 40 } };
            double[][] result = Arrays.toDouble(a);

            assertEquals(10.0, result[0][0], 0.0001);
            assertEquals(20.0, result[0][1], 0.0001);
            assertEquals(30.0, result[1][0], 0.0001);
            assertEquals(40.0, result[1][1], 0.0001);
        }

        @Test
        void toDouble_short3DArray() {
            short[][][] a = { { { 10, 20 } } };
            double[][][] result = Arrays.toDouble(a);

            assertEquals(10.0, result[0][0][0], 0.0001);
            assertEquals(20.0, result[0][0][1], 0.0001);
        }

        @Test
        void toDouble_shortArr_empty() {
            assertEquals(0, Arrays.toDouble(new short[0]).length);
        }

        @Test
        void toDouble_shortArr_null() {
            assertEquals(0, Arrays.toDouble((short[]) null).length);
        }

        @Test
        void toDouble_shortArr_widensExact() {
            assertArrayEquals(new double[] { -1d, 32767d, -32768d }, Arrays.toDouble(new short[] { -1, Short.MAX_VALUE, Short.MIN_VALUE }), 0d);
        }

        @Test
        void toDouble_shortArr2D_null() {
            assertEquals(0, Arrays.toDouble((short[][]) null).length);
        }

        @Test
        void toDouble_shortArr2D_preservesStructure() {
            final short[][] in = { { -1, 1 }, null };
            final double[][] r = Arrays.toDouble(in);
            assertArrayEquals(new double[] { -1d, 1d }, r[0], 0d);
            assertNotNull(r[1]);
        }

        @Test
        void toDouble_shortArr3D_null() {
            assertEquals(0, Arrays.toDouble((short[][][]) null).length);
        }

        @Test
        void toDouble_shortArr3D_preservesStructure() {
            final short[][][] in = { { { -1 } }, null };
            final double[][][] r = Arrays.toDouble(in);
            assertArrayEquals(new double[] { -1d }, r[0][0], 0d);
            assertNotNull(r[1]);
        }

        @Test
        void toDouble_shortArray() {
            short[] a = { 10, 20, 30 };
            double[] result = Arrays.toDouble(a);

            assertArrayEquals(new double[] { 10.0, 20.0, 30.0 }, result, 0.0001);
        }
    }

    @Nested
    class ConversionContractTests {

        @Test
        void conversion_nullInputs_returnEmptyArrays() {
            assertEquals(0, Arrays.toBoolean((byte[]) null).length);
            assertEquals(0, Arrays.toBoolean((byte[][]) null).length);
            assertEquals(0, Arrays.toBoolean((byte[][][]) null).length);
            assertEquals(0, Arrays.toBoolean((int[]) null).length);
            assertEquals(0, Arrays.toChar((int[]) null).length);
            assertEquals(0, Arrays.toByte((boolean[]) null).length);
            assertEquals(0, Arrays.toShort((byte[]) null).length);
            assertEquals(0, Arrays.toInt((boolean[]) null).length);
            assertEquals(0, Arrays.toInt((char[]) null).length);
            assertEquals(0, Arrays.toInt((byte[]) null).length);
            assertEquals(0, Arrays.toInt((short[]) null).length);
            assertEquals(0, Arrays.toInt((float[]) null).length);
            assertEquals(0, Arrays.toInt((double[]) null).length);
            assertEquals(0, Arrays.toLong((byte[]) null).length);
            assertEquals(0, Arrays.toLong((short[]) null).length);
            assertEquals(0, Arrays.toLong((int[]) null).length);
            assertEquals(0, Arrays.toLong((float[]) null).length);
            assertEquals(0, Arrays.toLong((double[]) null).length);
            assertEquals(0, Arrays.toFloat((byte[]) null).length);
            assertEquals(0, Arrays.toFloat((short[]) null).length);
            assertEquals(0, Arrays.toFloat((int[]) null).length);
            assertEquals(0, Arrays.toFloat((long[]) null).length);
            assertEquals(0, Arrays.toDouble((byte[]) null).length);
            assertEquals(0, Arrays.toDouble((short[]) null).length);
            assertEquals(0, Arrays.toDouble((int[]) null).length);
            assertEquals(0, Arrays.toDouble((long[]) null).length);
            assertEquals(0, Arrays.toDouble((float[]) null).length);
        }

        @Test
        void runtimeTypeAssertions_oneDim() {
            assertEquals(boolean[].class, Arrays.toBoolean(new byte[] { 1 }).getClass());
            assertEquals(boolean[].class, Arrays.toBoolean(new int[] { 1 }).getClass());
            assertEquals(char[].class, Arrays.toChar(new int[] { 65 }).getClass());
            assertEquals(byte[].class, Arrays.toByte(new boolean[] { true }).getClass());
            assertEquals(short[].class, Arrays.toShort(new byte[] { 1 }).getClass());
            assertEquals(int[].class, Arrays.toInt(new boolean[] { true }).getClass());
            assertEquals(int[].class, Arrays.toInt(new char[] { 'A' }).getClass());
            assertEquals(int[].class, Arrays.toInt(new byte[] { 1 }).getClass());
            assertEquals(int[].class, Arrays.toInt(new short[] { 1 }).getClass());
            assertEquals(int[].class, Arrays.toInt(new float[] { 1f }).getClass());
            assertEquals(int[].class, Arrays.toInt(new double[] { 1d }).getClass());
            assertEquals(long[].class, Arrays.toLong(new byte[] { 1 }).getClass());
            assertEquals(long[].class, Arrays.toLong(new short[] { 1 }).getClass());
            assertEquals(long[].class, Arrays.toLong(new int[] { 1 }).getClass());
            assertEquals(long[].class, Arrays.toLong(new float[] { 1f }).getClass());
            assertEquals(long[].class, Arrays.toLong(new double[] { 1d }).getClass());
            assertEquals(float[].class, Arrays.toFloat(new byte[] { 1 }).getClass());
            assertEquals(float[].class, Arrays.toFloat(new short[] { 1 }).getClass());
            assertEquals(float[].class, Arrays.toFloat(new int[] { 1 }).getClass());
            assertEquals(float[].class, Arrays.toFloat(new long[] { 1L }).getClass());
            assertEquals(double[].class, Arrays.toDouble(new byte[] { 1 }).getClass());
            assertEquals(double[].class, Arrays.toDouble(new short[] { 1 }).getClass());
            assertEquals(double[].class, Arrays.toDouble(new int[] { 1 }).getClass());
            assertEquals(double[].class, Arrays.toDouble(new long[] { 1L }).getClass());
            assertEquals(double[].class, Arrays.toDouble(new float[] { 1f }).getClass());
        }

        @Test
        void runtimeTypeAssertions_twoAndThreeDim() {
            assertEquals(int[][].class, Arrays.toInt(new byte[][] { { 1 } }).getClass());
            assertEquals(int[][][].class, Arrays.toInt(new byte[][][] { { { 1 } } }).getClass());
            assertEquals(double[][].class, Arrays.toDouble(new long[][] { { 1L } }).getClass());
            assertEquals(double[][][].class, Arrays.toDouble(new long[][][] { { { 1L } } }).getClass());
            assertEquals(char[][].class, Arrays.toChar(new int[][] { { 65 } }).getClass());
            assertEquals(char[][][].class, Arrays.toChar(new int[][][] { { { 65 } } }).getClass());
        }
    }
}
