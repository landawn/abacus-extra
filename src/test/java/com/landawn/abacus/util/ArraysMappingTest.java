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

import static com.landawn.abacus.util.ArrayTestSupport.arrayMethod;
import static com.landawn.abacus.util.ArrayTestSupport.arrayType;
import static com.landawn.abacus.util.ArrayTestSupport.callback;
import static com.landawn.abacus.util.ArrayTestSupport.invokeArrayMethod;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.landawn.abacus.util.Arrays.f;
import com.landawn.abacus.util.Arrays.ff;
import com.landawn.abacus.util.Arrays.fff;

class ArraysMappingTest {

    @Nested
    class MapTests {

        @Test
        void f_map_null() {
            String[] arr = null;
            Integer[] result = Arrays.f.map(arr, Integer::valueOf, Integer.class);
            assertEquals(0, result.length);
        }

        @Test
        void f_map_valid() {
            String[] arr = { "1", "2", "3" };
            Integer[] result = Arrays.f.map(arr, Integer::valueOf, Integer.class);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
        }

        @Test
        void f_mapPrimitiveTypes() throws Exception {
            String[] strings = { "1", "2", "3" };
            Integer[] ints = f.map(strings, Integer::valueOf, Integer.class);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, ints);

            assertArrayEquals(new boolean[] { true, false, true }, f.mapToBoolean(new String[] { "hello", "hi", "world" }, s -> s.length() > 3));
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, f.mapToChar(new String[] { "apple", "banana", "cherry" }, s -> s.charAt(0)));
            assertArrayEquals(new byte[] { 10, 20, 30 }, f.mapToByte(new String[] { "10", "20", "30" }, Byte::parseByte));
            assertArrayEquals(new short[] { 100, 200, 300 }, f.mapToShort(new String[] { "100", "200", "300" }, Short::parseShort));
            assertArrayEquals(new int[] { 10, 20, 30 }, f.mapToInt(new String[] { "10", "20", "30" }, Integer::parseInt));
            assertArrayEquals(new long[] { 1000L, 2000L, 3000L }, f.mapToLong(new String[] { "1000", "2000", "3000" }, Long::parseLong));
            assertArrayEquals(new float[] { 1.5f, 2.5f, 3.5f }, f.mapToFloat(new String[] { "1.5", "2.5", "3.5" }, Float::parseFloat), 0.0f);
            assertArrayEquals(new double[] { 1.5, 2.5, 3.5 }, f.mapToDouble(new String[] { "1.5", "2.5", "3.5" }, Double::parseDouble), 0.0);
        }

        @Test
        void ff_map_allIntegers_resultIsIntegerArray() {
            final Number[][] in = { { 1, 2 }, { 3, 4 } };
            final Number[][] out = ff.map(in, n -> n);
            assertEquals(Number[][].class, out.getClass());
            // target = Number (runtime component type of the input array); no narrowing happens.
        }

        @Test
        void ff_map_emptyInnerRows_preservesStructure() {
            final Integer[][] in = { {}, {}, {} };
            final Integer[][] out = ff.map(in, n -> n + 1);
            assertEquals(Integer[][].class, out.getClass());
            assertEquals(3, out.length);
            for (int i = 0; i < 3; i++) {
                assertEquals(0, out[i].length);
            }
        }

        @Test
        void ff_map_emptyInput_returnsEmptyOfDeclaredType() {
            final Integer[][] in = new Integer[0][];
            final Integer[][] out = ff.map(in, n -> n + 1);
            assertEquals(Integer[][].class, out.getClass());
            assertEquals(0, out.length);
        }

        @Test
        void fF_map_function() {
            String[][] arr = { { "1", "2" }, { "3", "4" } };
            Integer[][] result = Arrays.ff.map(arr, Integer::valueOf, Integer.class);
            assertEquals(1, result[0][0]);
            assertEquals(2, result[0][1]);
            assertEquals(3, result[1][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        void ff_map_mixedNumberSubtypes_resultArrayCanHoldAllValues() {
            // Object[][] declared, mapper returns Integer for first cell, Long for second.
            // The element type is inferred as Object from the runtime component type,
            // so the result array correctly holds both Integer and Long.
            final Object[][] in = { { "a", "b" } };
            final Object[][] out = ff.map(in, s -> ((String) s).length() % 2 == 0 ? (Object) 1L : (Object) 1);
            assertEquals(Object[][].class, out.getClass());
            assertNotNull(out[0][0]);
            assertNotNull(out[0][1]);
        }

        @Test
        void ff_map_nullsInResult_doNotWidenTarget() {
            // Number[][] in, mapper returns nulls for some, Integer for others.
            // target = Number (runtime component type); nulls and Integers both store fine.
            final Number[][] in = { { 1, 2 }, { 3, 4 } };
            final Number[][] out = ff.map(in, n -> n.intValue() % 2 == 0 ? null : n);
            assertEquals(Number[][].class, out.getClass());
            assertNull(out[0][1]); // value 2 -> null
            assertEquals(1, out[0][0]);
        }

        @Test
        void fF_map_preservesShape() throws Exception {
            Integer[][] arr = { { 1, 2 }, { 3, 4, 5 } };
            Integer[][] doubled = ff.map(arr, x -> x * 2);
            assertArrayEquals(new Integer[] { 2, 4 }, doubled[0]);
            assertArrayEquals(new Integer[] { 6, 8, 10 }, doubled[1]);
        }

        @Test
        void fF_map_unaryOperator() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            String[][] result = Arrays.ff.map(arr, String::toUpperCase);
            assertEquals("A", result[0][0]);
            assertEquals("B", result[0][1]);
            assertEquals("C", result[1][0]);
            assertEquals("D", result[1][1]);
        }

        @Test
        void fFF_map_function() {
            String[][][] arr = { { { "1", "2" } }, { { "3", "4" } } };
            Integer[][][] result = Arrays.fff.map(arr, Integer::valueOf, Integer.class);
            assertEquals(1, result[0][0][0]);
            assertEquals(2, result[0][0][1]);
            assertEquals(3, result[1][0][0]);
            assertEquals(4, result[1][0][1]);
        }

        @Test
        void fff_map_preservesDeclaredType() {
            final Integer[][][] in = { { { 1, 2 }, { 3 } }, { { 4 } } };
            final Integer[][][] out = fff.map(in, n -> n + 1);
            assertEquals(Integer[][][].class, out.getClass());
            assertArrayEquals(new Integer[] { 2, 3 }, out[0][0]);
            assertArrayEquals(new Integer[] { 4 }, out[0][1]);
            assertArrayEquals(new Integer[] { 5 }, out[1][0]);
        }

        @Test
        void fFF_map_unaryOperator() {
            String[][][] arr = { { { "a", "b" } }, { { "c", "d" } } };
            String[][][] result = Arrays.fff.map(arr, String::toUpperCase);
            assertEquals("A", result[0][0][0]);
            assertEquals("B", result[0][0][1]);
            assertEquals("C", result[1][0][0]);
            assertEquals("D", result[1][0][1]);
        }

        @Test
        void fffMapInferredTypeRejectsRuntimeComponentWidening() throws Exception {
            Number[][][] input = new Integer[][][] { { { 1, 2 } } };

            assertThrows(IllegalArgumentException.class, () -> fff.map(input, n -> n.longValue() + 1L));

            Number[][][] out = fff.map(input, n -> n.longValue() + 1L, Number.class);

            assertEquals(2L, out[0][0][0]);
            assertEquals(3L, out[0][0][1]);
            assertEquals(Number[][][].class, out.getClass());
        }

        @Test
        void ffMapInferredTypeRejectsAmbiguousInterfaceTarget() throws Exception {
            java.io.Serializable[][] input = new Integer[][] { { 1 } };

            assertThrows(IllegalArgumentException.class, () -> ff.map(input, x -> "s"));

            java.io.Serializable[][] out = ff.map(input, x -> "s", java.io.Serializable.class);

            assertEquals("s", out[0][0]);
            assertEquals(java.io.Serializable[][].class, out.getClass());
        }

        @Test
        void ffMapInferredTypeRejectsRuntimeComponentWidening() throws Exception {
            Number[][] input = new Integer[][] { { 1, 2 } };

            assertThrows(IllegalArgumentException.class, () -> ff.map(input, n -> n.longValue() + 1L));

            Number[][] out = ff.map(input, n -> n.longValue() + 1L, Number.class);

            assertEquals(2L, out[0][0]);
            assertEquals(3L, out[0][1]);
            assertEquals(Number[][].class, out.getClass());
        }

        @Test
        void inferredMap_resultArrayAcceptsAllElements() {
            // Use a Number[][] declared array but stuff Integers and Longs.
            final Number[][] in = { { 1, 2L }, { 3.0, 4f } };
            final Number[][] out = ff.map(in, n -> n);
            assertEquals(Number[][].class, out.getClass());
            assertEquals(Integer.valueOf(1), out[0][0]);
            assertEquals(Long.valueOf(2L), out[0][1]);
            assertEquals(Double.valueOf(3.0), out[1][0]);
            assertEquals(Float.valueOf(4f), out[1][1]);
        }

        @Test
        void integration_mapAndUpdate() {
            int[] arr = { 1, 2, 3 };
            long[] mapped = Arrays.mapToLong(arr, i -> i * 1000L);

            assertArrayEquals(new long[] { 1000L, 2000L, 3000L }, mapped);
        }

        @Test
        void map_chainedTransformations() throws Exception {
            // Test chaining multiple transformations
            String[] input = { "1", "2", "3" };
            Integer[] intermediate = f.map(input, Integer::valueOf, Integer.class);
            String[] result = f.map(intermediate, i -> "Value: " + (i * 2), String.class);
            assertArrayEquals(new String[] { "Value: 2", "Value: 4", "Value: 6" }, result);
        }

        @Test
        void map_emptyArray() throws Exception {
            String[] input = new String[0];
            Integer[] result = f.map(input, Integer::valueOf, Integer.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void map_integerToString() throws Exception {
            Integer[] input = { 1, 2, 3 };
            String[] result = f.map(input, String::valueOf, String.class);
            assertArrayEquals(new String[] { "1", "2", "3" }, result);
        }

        @Test
        void map_nullArray() throws Exception {
            String[] input = null;
            Integer[] result = f.map(input, Integer::valueOf, Integer.class);
            assertEquals(0, result.length);
        }

        @Test
        void map_returnsMappedValue() throws Exception {
            // Test basic mapping
            Integer[][] array = { { 1, 2 }, { 3, 4 } };
            Integer[][] doubled = ff.map(array, x -> x * 2);
            assertArrayEquals(new Integer[] { 2, 4 }, doubled[0]);
            assertArrayEquals(new Integer[] { 6, 8 }, doubled[1]);

            // Test null array
            Integer[][] nullArray = null;
            assertThrows(IllegalArgumentException.class, () -> ff.map(nullArray, x -> x * 2));

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            Integer[][] mapped = ff.map(arrayWithNulls, x -> x + 10);
            assertArrayEquals(new Integer[] { 11, 12 }, mapped[0]);
            assertEquals(0, mapped[1].length);
            assertArrayEquals(new Integer[] { 13, 14 }, mapped[2]);

            // Test String transformation
            String[][] stringArray = { { "hello", "world" }, { "foo", "bar" } };
            String[][] upperCased = ff.map(stringArray, String::toUpperCase);
            assertArrayEquals(new String[] { "HELLO", "WORLD" }, upperCased[0]);
            assertArrayEquals(new String[] { "FOO", "BAR" }, upperCased[1]);
        }

        @Test
        void map_stringToInteger() throws Exception {
            String[] input = { "1", "2", "3" };
            Integer[] result = f.map(input, Integer::valueOf, Integer.class);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
        }

        @Test
        void map_withException() {
            String[] input = { "1", "abc", "3" };
            assertThrows(NumberFormatException.class, () -> {
                f.map(input, Integer::valueOf, Integer.class);
            });
        }

        @Test
        void map_withNullElements() throws Exception {
            String[] input = { "1", null, "3" };
            String[] result = f.map(input, s -> s == null ? "NULL" : s, String.class);
            assertArrayEquals(new String[] { "1", "NULL", "3" }, result);
        }

        @Test
        void mapDifferentType() throws Exception {
            // Test type conversion
            String[][][] strings = { { { "1", "2" } }, { { "3", "4" } } };
            Integer[][][] numbers = fff.map(strings, Integer::parseInt, Integer.class);
            assertEquals(1, numbers[0][0][0]);
            assertEquals(2, numbers[0][0][1]);
            assertEquals(3, numbers[1][0][0]);
            assertEquals(4, numbers[1][0][1]);

            // Test null input
            Integer[][][] nullResult = fff.map((String[][][]) null, Integer::parseInt, Integer.class);
            assertEquals(0, nullResult.length);

            // Test Integer to String conversion
            Integer[][][] nums = { { { 100, 200 } }, { { 300, 400 } } };
            String[][][] strs = fff.map(nums, n -> "Number: " + n, String.class);
            assertEquals("Number: 100", strs[0][0][0]);
            assertEquals("Number: 200", strs[0][0][1]);
            assertEquals("Number: 300", strs[1][0][0]);
            assertEquals("Number: 400", strs[1][0][1]);

            // Test with mixed null elements
            Integer[][][] mixedNums = { { { 1, null } }, null, { { null, 2 } } };
            String[][][] mixedStrs = fff.map(mixedNums, n -> n == null ? "NULL" : n.toString(), String.class);
            assertEquals("1", mixedStrs[0][0][0]);
            assertEquals("NULL", mixedStrs[0][0][1]);
            assertEquals(0, mixedStrs[1].length);
            assertEquals("NULL", mixedStrs[2][0][0]);
            assertEquals("2", mixedStrs[2][0][1]);
        }

        @Test
        void mapSameType() throws Exception {
            // Test basic mapping
            Integer[][][] numbers = { { { 1, 2 } }, { { 3, 4 } } };
            Integer[][][] doubled = fff.map(numbers, n -> n * 2);
            assertEquals(2, doubled[0][0][0]);
            assertEquals(4, doubled[0][0][1]);
            assertEquals(6, doubled[1][0][0]);
            assertEquals(8, doubled[1][0][1]);

            // Test with null input
            assertThrows(IllegalArgumentException.class, () -> fff.map((Integer[][][]) null, n -> n * 2));

            // Test with null sub-arrays
            Integer[][][] mixedArr = { { { 1, 2 } }, null, { { 3, 4 } } };
            Integer[][][] mappedMixed = fff.map(mixedArr, n -> n + 10);
            assertEquals(11, mappedMixed[0][0][0]);
            assertEquals(12, mappedMixed[0][0][1]);
            assertEquals(0, mappedMixed[1].length);
            assertEquals(13, mappedMixed[2][0][0]);
            assertEquals(14, mappedMixed[2][0][1]);

            // Test with String array
            String[][][] strings = { { { "hello", "world" } }, { { "foo", "bar" } } };
            String[][][] upperStrings = fff.map(strings, String::toUpperCase);
            assertEquals("HELLO", upperStrings[0][0][0]);
            assertEquals("WORLD", upperStrings[0][0][1]);
            assertEquals("FOO", upperStrings[1][0][0]);
            assertEquals("BAR", upperStrings[1][0][1]);
        }

        @Test
        void mapWithTargetType() throws Exception {
            // Test type conversion
            String[][] array = { { "1", "2" }, { "3", "4" } };
            Integer[][] numbers = ff.map(array, Integer::parseInt, Integer.class);
            assertArrayEquals(new Integer[] { 1, 2 }, numbers[0]);
            assertArrayEquals(new Integer[] { 3, 4 }, numbers[1]);

            // Test null array
            String[][] nullArray = null;
            assertEquals(0, ff.map(nullArray, Integer::parseInt, Integer.class).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1", "2" }, null, { "3", "4" } };
            Integer[][] mapped = ff.map(arrayWithNulls, Integer::parseInt, Integer.class);
            assertArrayEquals(new Integer[] { 1, 2 }, mapped[0]);
            assertEquals(0, mapped[1].length);
            assertArrayEquals(new Integer[] { 3, 4 }, mapped[2]);

            // Test Integer to String conversion
            Integer[][] intArray = { { 10, 20 }, { 30, 40 } };
            String[][] stringArray = ff.map(intArray, Object::toString, String.class);
            assertArrayEquals(new String[] { "10", "20" }, stringArray[0]);
            assertArrayEquals(new String[] { "30", "40" }, stringArray[1]);
        }

        @Test
        void objectMappingAllocatesRowsButRetainsReturnedElementReferences() {
            final StringBuilder value = new StringBuilder("original");
            final StringBuilder[] row = { value };
            final StringBuilder[][] grid = { row, row };
            final StringBuilder[][] inferred = Arrays.ff.map(grid, element -> element.append('!'));

            assertNotSame(grid, inferred);
            assertNotSame(row, inferred[0]);
            assertNotSame(inferred[0], inferred[1]);
            assertSame(value, inferred[0][0]);
            assertSame(value, inferred[1][0]);
            assertEquals("original!!", grid[0][0].toString());

            final StringBuilder[][][] cube = { grid };
            final StringBuilder[][][] explicit = Arrays.fff.map(cube, element -> element, StringBuilder.class);

            assertNotSame(cube, explicit);
            assertNotSame(grid, explicit[0]);
            assertNotSame(row, explicit[0][0]);
            assertNotSame(explicit[0][0], explicit[0][1]);
            assertSame(value, explicit[0][0][0]);
            assertSame(value, explicit[0][1][0]);
        }
    }

    @Nested
    class MapToObjTests {

        @ParameterizedTest(name = "reject primitive target {0}")
        @ValueSource(classes = { boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class })
        void mapToObj_primitiveTargetType_isRejectedBeforeCallingMapper(Class<?> targetType) {
            java.lang.reflect.Method method = arrayMethod("mapToObj", int.class, 1, 3);
            int[] calls = { 0 };
            Object mapper = callback(method.getParameterTypes()[1], values -> {
                calls[0]++;
                return values[0];
            });
            IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                    () -> invokeArrayMethod(method, new int[] { 1 }, mapper, targetType));
            assertTrue(failure.getMessage().contains("reference type"));
            assertEquals(0, calls[0]);
        }

        @ParameterizedTest(name = "{0}, dimension {1}: empty mapping")
        @MethodSource("com.landawn.abacus.util.ArrayTestSupport#primitiveArrayCases")
        void mapToObj_nullOrEmptyOuterArray_preservesResultTypeWithoutCallingMapper(Class<?> elementType, int dimensions) throws Throwable {
            java.lang.reflect.Method method = arrayMethod("mapToObj", elementType, dimensions, 3);
            int[] calls = { 0 };
            Object mapper = callback(method.getParameterTypes()[1], values -> {
                calls[0]++;
                return "unexpected";
            });
            for (Object input : new Object[] { null, java.lang.reflect.Array.newInstance(elementType, new int[dimensions]) }) {
                Object result = invokeArrayMethod(method, input, mapper, String.class);
                assertEquals(arrayType(String.class, dimensions), result.getClass());
                assertEquals(0, java.lang.reflect.Array.getLength(result));
            }
            assertEquals(0, calls[0]);
        }

        @Test
        void edgeCase_largeArray() {
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
        void mapToObj_1D_boolean_empty() {
            boolean[] arr = {};
            String[] result = Arrays.mapToObj(arr, b -> b ? "YES" : "NO", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_1D_boolean_normal() {
            boolean[] arr = { true, false, true };
            String[] result = Arrays.mapToObj(arr, b -> b ? "YES" : "NO", String.class);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals("YES", result[0]);
            assertEquals("NO", result[1]);
            assertEquals("YES", result[2]);
        }

        @Test
        void mapToObj_1D_byte_normal() {
            byte[] arr = { 1, 2, 3, 4 };
            String[] result = Arrays.mapToObj(arr, b -> String.format(java.util.Locale.ROOT, "%02X", b), String.class);
            assertNotNull(result);
            assertEquals(4, result.length);
            assertEquals("01", result[0]);
            assertEquals("02", result[1]);
        }

        @Test
        void mapToObj_1D_byte_null() {
            byte[] arr = null;
            String[] result = Arrays.mapToObj(arr, b -> String.valueOf(b), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_1D_char_normal() {
            char[] arr = { 'a', 'b', 'c' };
            String[] result = Arrays.mapToObj(arr, c -> String.valueOf(c).toUpperCase(), String.class);
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals("A", result[0]);
            assertEquals("B", result[1]);
            assertEquals("C", result[2]);
        }

        @Test
        void mapToObj_1D_char_null() {
            char[] arr = null;
            String[] result = Arrays.mapToObj(arr, c -> String.valueOf(c), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_1D_double_normal() {
            double[] arr = { 1.5, 2.5, 3.5 };
            String[] result = Arrays.mapToObj(arr, d -> String.format(java.util.Locale.ROOT, "%.2f", d), String.class);
            assertNotNull(result);
            assertEquals("1.50", result[0]);
            assertEquals("3.50", result[2]);
        }

        @Test
        void mapToObj_1D_double_null() {
            double[] arr = null;
            String[] result = Arrays.mapToObj(arr, d -> String.valueOf(d), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_1D_float_normal() {
            float[] arr = { 1.1f, 2.2f, 3.3f };
            String[] result = Arrays.mapToObj(arr, f -> String.format(java.util.Locale.ROOT, "%.1f", f), String.class);
            assertNotNull(result);
            assertEquals("1.1", result[0]);
            assertEquals("3.3", result[2]);
        }

        @Test
        void mapToObj_1D_float_null() {
            float[] arr = null;
            String[] result = Arrays.mapToObj(arr, f -> String.valueOf(f), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_1D_int_normal() {
            int[] arr = { 1, 2, 3, 4, 5 };
            String[] result = Arrays.mapToObj(arr, i -> "num:" + i, String.class);
            assertNotNull(result);
            assertEquals(5, result.length);
            assertEquals("num:1", result[0]);
            assertEquals("num:5", result[4]);
        }

        @Test
        void mapToObj_1D_int_null() {
            int[] arr = null;
            String[] result = Arrays.mapToObj(arr, i -> String.valueOf(i), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_1D_long_normal() {
            long[] arr = { 100L, 200L, 300L };
            String[] result = Arrays.mapToObj(arr, l -> "L" + l, String.class);
            assertNotNull(result);
            assertEquals("L100", result[0]);
            assertEquals("L300", result[2]);
        }

        @Test
        void mapToObj_1D_long_null() {
            long[] arr = null;
            String[] result = Arrays.mapToObj(arr, l -> String.valueOf(l), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_1D_short_normal() {
            short[] arr = { 10, 20, 30 };
            String[] result = Arrays.mapToObj(arr, s -> "S" + s, String.class);
            assertNotNull(result);
            assertEquals("S10", result[0]);
            assertEquals("S20", result[1]);
        }

        @Test
        void mapToObj_2D_boolean_normal() {
            boolean[][] arr = { { true, false }, { false, true } };
            String[][] result = Arrays.mapToObj(arr, b -> b ? "1" : "0", String.class);
            assertNotNull(result);
            assertEquals(2, result.length);
            assertEquals("1", result[0][0]);
            assertEquals("0", result[0][1]);
        }

        @Test
        void mapToObj_2D_byte_normal() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            Integer[][] result = Arrays.mapToObj(arr, b -> b * 10, Integer.class);
            assertNotNull(result);
            assertEquals(10, result[0][0]);
            assertEquals(20, result[0][1]);
        }

        @Test
        void mapToObj_2D_char_normal() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            Integer[][] result = Arrays.mapToObj(arr, c -> (int) c, Integer.class);
            assertNotNull(result);
            assertEquals(97, result[0][0]);
            assertEquals(98, result[0][1]);
        }

        @Test
        void mapToObj_2D_double_normal() {
            double[][] arr = { { 10.5, 20.5 }, { 30.5, 40.5 } };
            Long[][] result = Arrays.mapToObj(arr, d -> Math.round(d), Long.class);
            assertNotNull(result);
            assertEquals(11L, result[0][0]);
            assertEquals(41L, result[1][1]);
        }

        @Test
        void mapToObj_2D_float_normal() {
            float[][] arr = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            Integer[][] result = Arrays.mapToObj(arr, f -> Math.round(f), Integer.class);
            assertNotNull(result);
            assertEquals(2, result[0][0]);
            assertEquals(5, result[1][1]);
        }

        @Test
        void mapToObj_2D_int_normal() {
            int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            String[][] result = Arrays.mapToObj(arr, i -> Integer.toHexString(i), String.class);
            assertNotNull(result);
            assertEquals("1", result[0][0]);
            assertEquals("6", result[1][2]);
        }

        @Test
        void mapToObj_2D_long_normal() {
            long[][] arr = { { 1000L, 2000L }, { 3000L, 4000L } };
            Integer[][] result = Arrays.mapToObj(arr, l -> (int) (l / 1000), Integer.class);
            assertNotNull(result);
            assertEquals(1, result[0][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        void mapToObj_2D_short_normal() {
            short[][] arr = { { 100, 200 }, { 300, 400 } };
            Integer[][] result = Arrays.mapToObj(arr, s -> s / 10, Integer.class);
            assertNotNull(result);
            assertEquals(10, result[0][0]);
        }

        @Test
        void mapToObj_3D_boolean_normal() {
            boolean[][][] arr = { { { true, false } } };
            Integer[][][] result = Arrays.mapToObj(arr, b -> b ? 1 : 0, Integer.class);
            assertNotNull(result);
            assertEquals(1, result[0][0][0]);
            assertEquals(0, result[0][0][1]);
        }

        @Test
        void mapToObj_3D_byte_normal() {
            byte[][][] arr = { { { 5, 6 } } };
            String[][][] result = Arrays.mapToObj(arr, b -> "byte:" + b, String.class);
            assertNotNull(result);
            assertEquals("byte:5", result[0][0][0]);
        }

        @Test
        void mapToObj_3D_char_normal() {
            char[][][] arr = { { { 'x', 'y' } } };
            String[][][] result = Arrays.mapToObj(arr, c -> c + "!", String.class);
            assertNotNull(result);
            assertEquals("x!", result[0][0][0]);
        }

        @Test
        void mapToObj_3D_double_normal() {
            double[][][] arr = { { { 100.25, 200.75 } } };
            String[][][] result = Arrays.mapToObj(arr, d -> "$" + d, String.class);
            assertNotNull(result);
            assertEquals("$100.25", result[0][0][0]);
        }

        @Test
        void mapToObj_3D_float_normal() {
            float[][][] arr = { { { 0.5f, 0.75f } } };
            Double[][][] result = Arrays.mapToObj(arr, f -> (double) f * 2, Double.class);
            assertNotNull(result);
            assertEquals(1.0, result[0][0][0], 0.01);
        }

        @Test
        void mapToObj_3D_int_normal() {
            int[][][] arr = { { { 10, 20 }, { 30 } } };
            Double[][][] result = Arrays.mapToObj(arr, i -> i * 0.5, Double.class);
            assertNotNull(result);
            assertEquals(5.0, result[0][0][0]);
        }

        @Test
        void mapToObj_3D_long_normal() {
            long[][][] arr = { { { 500L, 600L } } };
            String[][][] result = Arrays.mapToObj(arr, l -> l + "ms", String.class);
            assertNotNull(result);
            assertEquals("500ms", result[0][0][0]);
        }

        @Test
        void mapToObj_3D_short_normal() {
            short[][][] arr = { { { 50, 60 } } };
            Long[][][] result = Arrays.mapToObj(arr, s -> (long) s * 2, Long.class);
            assertNotNull(result);
            assertEquals(100L, result[0][0][0]);
        }

        @Test
        void mapToObj_boolean_1D_empty() {
            boolean[] arr = new boolean[0];
            String[] result = Arrays.mapToObj(arr, b -> b ? "YES" : "NO", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_boolean_1D_null() {
            boolean[] arr = null;
            String[] result = Arrays.mapToObj(arr, b -> b ? "YES" : "NO", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_boolean_1D_valid() {
            boolean[] arr = { true, false, true };
            String[] result = Arrays.mapToObj(arr, b -> b ? "YES" : "NO", String.class);
            assertArrayEquals(new String[] { "YES", "NO", "YES" }, result);
        }

        @Test
        void mapToObj_boolean_2D_null() {
            boolean[][] arr = null;
            String[][] result = Arrays.mapToObj(arr, b -> b ? "T" : "F", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_boolean_3D_null() {
            boolean[][][] arr = null;
            Integer[][][] result = Arrays.mapToObj(arr, b -> b ? 1 : 0, Integer.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_boolean_3D_runtimeType() {
            final boolean[][][] a = { { { true, false } }, { { false } } };
            final Integer[][][] r = Arrays.mapToObj(a, b -> b ? 1 : 0, Integer.class);
            assertEquals(Integer[][][].class, r.getClass());
            assertEquals(2, r.length);
            assertArrayEquals(new Integer[] { 1, 0 }, r[0][0]);
            assertArrayEquals(new Integer[] { 0 }, r[1][0]);
        }

        @Test
        void mapToObj_boolean_3D_valid() {
            boolean[][][] arr = { { { true, false }, { true } }, { { false } } };
            Integer[][][] result = Arrays.mapToObj(arr, b -> b ? 1 : 0, Integer.class);
            assertEquals(2, result.length);
            assertEquals(1, result[0][0][0]);
            assertEquals(0, result[0][0][1]);
            assertEquals(1, result[0][1][0]);
            assertEquals(0, result[1][0][0]);
        }

        @Test
        void mapToObj_boolean_runtimeType() {
            final String[] r = Arrays.mapToObj(new boolean[] { true, false, true }, b -> b ? "Y" : "N", String.class);
            assertEquals(String[].class, r.getClass());
            assertArrayEquals(new String[] { "Y", "N", "Y" }, r);
        }

        @Test
        void mapToObj_boolean2D() {
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
        void mapToObj_boolean2DArray() throws Exception {
            boolean[][] input = { { true, false }, { false, true } };
            String[][] result = Arrays.mapToObj(input, b -> b ? "T" : "F", String.class);
            assertArrayEquals(new String[][] { { "T", "F" }, { "F", "T" } }, result);
        }

        @Test
        void mapToObj_boolean2DArray_null() throws Exception {
            boolean[][] input = null;
            String[][] result = Arrays.mapToObj(input, b -> b ? "T" : "F", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_boolean3D() {
            boolean[][][] a = { { { true, false }, { true } } };
            String[][][] result = Arrays.mapToObj(a, b -> b ? "T" : "F", String.class);

            assertEquals(1, result.length);
            assertEquals(2, result[0].length);
            assertEquals("T", result[0][0][0]);
            assertEquals("F", result[0][0][1]);
            assertEquals("T", result[0][1][0]);
        }

        @Test
        void mapToObj_boolean3DArray() throws Exception {
            boolean[][][] input = { { { true, false } }, { { false, true } } };
            String[][][] result = Arrays.mapToObj(input, b -> b ? "1" : "0", String.class);
            assertArrayEquals(new String[][][] { { { "1", "0" } }, { { "0", "1" } } }, result);
        }

        @Test
        void mapToObj_boolean3DArray_null() throws Exception {
            boolean[][][] input = null;
            String[][][] result = Arrays.mapToObj(input, b -> b ? "1" : "0", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_booleanArray() throws Exception {
            boolean[] input = { true, false, true };
            String[] result = Arrays.mapToObj(input, b -> b ? "YES" : "NO", String.class);
            assertArrayEquals(new String[] { "YES", "NO", "YES" }, result);
        }

        @Test
        void mapToObj_booleanArray_empty() throws Exception {
            boolean[] input = new boolean[0];
            String[] result = Arrays.mapToObj(input, b -> b ? "YES" : "NO", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_booleanArray_null() throws Exception {
            boolean[] input = null;
            String[] result = Arrays.mapToObj(input, b -> b ? "YES" : "NO", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_booleanArray_values_true_false_true() {
            boolean[] a = { true, false, true };
            String[] result = Arrays.mapToObj(a, b -> b ? "T" : "F", String.class);

            assertArrayEquals(new String[] { "T", "F", "T" }, result);
        }

        @Test
        void mapToObj_booleanArray2D() {
            boolean[][] arr = { { true, false }, { false, true } };
            String[][] result = Arrays.mapToObj(arr, b -> b ? "T" : "F", String.class);

            assertEquals(2, result.length);
            assertArrayEquals(new String[] { "T", "F" }, result[0]);
            assertArrayEquals(new String[] { "F", "T" }, result[1]);
        }

        @Test
        void mapToObj_booleanArray2DNull() {
            String[][] result = Arrays.mapToObj((boolean[][]) null, b -> "test", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_booleanArray3D() {
            boolean[][][] arr = { { { true, false } } };
            String[][][] result = Arrays.mapToObj(arr, b -> b ? "T" : "F", String.class);

            assertEquals(1, result.length);
            assertEquals(1, result[0].length);
            assertArrayEquals(new String[] { "T", "F" }, result[0][0]);
        }

        @Test
        void mapToObj_booleanArray3DNull() {
            String[][][] result = Arrays.mapToObj((boolean[][][]) null, b -> "test", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_booleanArrayEmpty() {
            boolean[] arr = {};
            String[] result = Arrays.mapToObj(arr, b -> "test", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_booleanArrayNull() {
            String[] result = Arrays.mapToObj((boolean[]) null, b -> "test", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_booleanToString() {
            // mapToObj({true,false,true}, b -> b?"YES":"NO", String.class) => ["YES","NO","YES"]
            boolean[] flags = { true, false, true };
            String[] strings = Arrays.mapToObj(flags, b -> b ? "YES" : "NO", String.class);
            assertArrayEquals(new String[] { "YES", "NO", "YES" }, strings);
        }

        @Test
        void mapToObj_byte_1D_null() {
            byte[] arr = null;
            String[] result = Arrays.mapToObj(arr, b -> String.format(java.util.Locale.ROOT, "%02X", b), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_byte_1D_valid() {
            byte[] arr = { 1, 2, 15, 16 };
            String[] result = Arrays.mapToObj(arr, b -> String.format(java.util.Locale.ROOT, "%02X", b), String.class);
            assertEquals("01", result[0]);
            assertEquals("02", result[1]);
            assertEquals("0F", result[2]);
            assertEquals("10", result[3]);
        }

        @Test
        void mapToObj_byte_2D_valid() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            Integer[][] result = Arrays.mapToObj(arr, b -> b * 2, Integer.class);
            assertEquals(2, result[0][0]);
            assertEquals(4, result[0][1]);
            assertEquals(6, result[1][0]);
            assertEquals(8, result[1][1]);
        }

        @Test
        void mapToObj_byte_3D_valid() {
            byte[][][] arr = { { { 1, 2 } } };
            String[][][] result = Arrays.mapToObj(arr, b -> "byte:" + b, String.class);
            assertEquals("byte:1", result[0][0][0]);
            assertEquals("byte:2", result[0][0][1]);
        }

        @Test
        void mapToObj_byte_runtimeType() {
            final String[] r = Arrays.mapToObj(new byte[] { 1, 2 }, b -> "b" + b, String.class);
            assertEquals(String[].class, r.getClass());
            assertArrayEquals(new String[] { "b1", "b2" }, r);
        }

        @Test
        void mapToObj_byte2D() {
            byte[][] a = { { 1, 2 }, { 3, 4 } };
            String[][] result = Arrays.mapToObj(a, b -> "B" + b, String.class);

            assertEquals(2, result.length);
            assertEquals("B1", result[0][0]);
            assertEquals("B2", result[0][1]);
            assertEquals("B3", result[1][0]);
            assertEquals("B4", result[1][1]);
        }

        @Test
        void mapToObj_byte2DArray() throws Exception {
            byte[][] input = { { 1, 2 }, { 3, 4 } };
            String[][] result = Arrays.mapToObj(input, b -> "B" + b, String.class);
            assertArrayEquals(new String[][] { { "B1", "B2" }, { "B3", "B4" } }, result);
        }

        @Test
        void mapToObj_byte3D() {
            byte[][][] a = { { { 1, 2 }, { 3 } } };
            String[][][] result = Arrays.mapToObj(a, b -> "Byte" + b, String.class);

            assertEquals(1, result.length);
            assertEquals("Byte1", result[0][0][0]);
            assertEquals("Byte2", result[0][0][1]);
            assertEquals("Byte3", result[0][1][0]);
        }

        @Test
        void mapToObj_byte3DArray() throws Exception {
            byte[][][] input = { { { 1, 2 } }, { { 3, 4 } } };
            Integer[][][] result = Arrays.mapToObj(input, b -> b * 10, Integer.class);
            assertArrayEquals(new Integer[][][] { { { 10, 20 } }, { { 30, 40 } } }, result);
        }

        @Test
        void mapToObj_byteArray() throws Exception {
            byte[] input = { 1, 2, 3 };
            Integer[] result = Arrays.mapToObj(input, b -> (int) b, Integer.class);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
        }

        @Test
        void mapToObj_byteArray_null() throws Exception {
            byte[] input = null;
            String[] result = Arrays.mapToObj(input, b -> String.valueOf(b), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_byteArray_values_1_2_3() {
            byte[] a = { 1, 2, 3 };
            String[] result = Arrays.mapToObj(a, b -> "Byte:" + b, String.class);

            assertArrayEquals(new String[] { "Byte:1", "Byte:2", "Byte:3" }, result);
        }

        @Test
        void mapToObj_byteArrayNull() {
            String[] result = Arrays.mapToObj((byte[]) null, b -> "test", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_byteToHex() {
            // mapToObj({1,2,3,4}, b -> format("%02X", b), String.class) => ["01","02","03","04"]
            byte[] bytes = { 1, 2, 3, 4 };
            String[] hex = Arrays.mapToObj(bytes, b -> String.format(java.util.Locale.ROOT, "%02X", b), String.class);
            assertArrayEquals(new String[] { "01", "02", "03", "04" }, hex);
        }

        @Test
        void mapToObj_char_1D_null() {
            char[] arr = null;
            String[] result = Arrays.mapToObj(arr, c -> String.valueOf(c).toUpperCase(), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_char_2D_valid() {
            char[][] arr = { { 'a', 'b' }, { 'c', 'd' } };
            Integer[][] result = Arrays.mapToObj(arr, c -> (int) c, Integer.class);
            assertEquals(2, result.length);
            assertEquals(97, result[0][0]);
            assertEquals(100, result[1][1]);
        }

        @Test
        void mapToObj_char_3D_valid() {
            char[][][] arr = { { { 'x', 'y' } } };
            String[][][] result = Arrays.mapToObj(arr, c -> String.valueOf(c), String.class);
            assertEquals(1, result.length);
            assertEquals("x", result[0][0][0]);
            assertEquals("y", result[0][0][1]);
        }

        @Test
        void mapToObj_char_runtimeType() {
            final Integer[] r = Arrays.mapToObj(new char[] { 'a', 'b' }, c -> (int) c, Integer.class);
            assertArrayEquals(new Integer[] { 97, 98 }, r);
            assertEquals(Integer[].class, r.getClass());
        }

        @Test
        void mapToObj_char2D() {
            char[][] a = { { 'a', 'b' }, { 'c', 'd' } };
            String[][] result = Arrays.mapToObj(a, c -> String.valueOf(c).toUpperCase(), String.class);

            assertEquals(2, result.length);
            assertEquals("A", result[0][0]);
            assertEquals("B", result[0][1]);
            assertEquals("C", result[1][0]);
            assertEquals("D", result[1][1]);
        }

        @Test
        void mapToObj_char2DArray() throws Exception {
            char[][] input = { { 'a', 'b' }, { 'c', 'd' } };
            Integer[][] result = Arrays.mapToObj(input, c -> (int) c, Integer.class);
            assertEquals(97, result[0][0]);
            assertEquals(98, result[0][1]);
            assertEquals(99, result[1][0]);
            assertEquals(100, result[1][1]);
        }

        @Test
        void mapToObj_char3D() {
            char[][][] a = { { { 'a', 'b' }, { 'c' } } };
            String[][][] result = Arrays.mapToObj(a, c -> String.valueOf(c).toUpperCase(), String.class);

            assertEquals(1, result.length);
            assertEquals("A", result[0][0][0]);
            assertEquals("B", result[0][0][1]);
            assertEquals("C", result[0][1][0]);
        }

        @Test
        void mapToObj_char3DArray() throws Exception {
            char[][][] input = { { { 'x', 'y' } }, { { 'z', 'w' } } };
            String[][][] result = Arrays.mapToObj(input, c -> String.valueOf(c), String.class);
            assertArrayEquals(new String[][][] { { { "x", "y" } }, { { "z", "w" } } }, result);
        }

        @Test
        void mapToObj_charArray() throws Exception {
            char[] input = { 'a', 'b', 'c' };
            String[] result = Arrays.mapToObj(input, c -> String.valueOf(c).toUpperCase(), String.class);
            assertArrayEquals(new String[] { "A", "B", "C" }, result);
        }

        @Test
        void mapToObj_charArray_null() throws Exception {
            char[] input = null;
            String[] result = Arrays.mapToObj(input, c -> String.valueOf(c), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_charArrayNull() {
            String[] result = Arrays.mapToObj((char[]) null, c -> "test", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_charToInt3D() {
            // char[][][] cube => Integer[][][] with ASCII values
            char[][][] cube = { { { 'a', 'b' }, { 'c', 'd' } }, { { 'e', 'f' }, { 'g', 'h' } } };
            Integer[][][] result = Arrays.mapToObj(cube, c -> (int) c, Integer.class);
            assertEquals(97, result[0][0][0]); // 'a'
            assertEquals(98, result[0][0][1]); // 'b'
            assertEquals(99, result[0][1][0]); // 'c'
            assertEquals(104, result[1][1][1]); // 'h'
        }

        @Test
        void mapToObj_charToString() {
            // mapToObj({'a','b','c'}, ...) => ["A","B","C"]
            char[] chars = { 'a', 'b', 'c' };
            String[] strings = Arrays.mapToObj(chars, c -> String.valueOf(c).toUpperCase(), String.class);
            assertArrayEquals(new String[] { "A", "B", "C" }, strings);
        }

        @Test
        void mapToObj_double_1D_null() {
            double[] arr = null;
            String[] result = Arrays.mapToObj(arr, d -> String.format(java.util.Locale.ROOT, "%.2f", d), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_double_1D_valid() {
            double[] arr = { 1.5, 2.7, 3.9 };
            String[] result = Arrays.mapToObj(arr, d -> String.format(java.util.Locale.ROOT, "%.1f", d), String.class);
            assertArrayEquals(new String[] { "1.5", "2.7", "3.9" }, result);
        }

        @Test
        void mapToObj_double_2D_valid() {
            double[][] arr = { { 1.1, 2.2 }, { 3.3, 4.4 } };
            Integer[][] result = Arrays.mapToObj(arr, d -> (int) Math.round(d), Integer.class);
            assertEquals(1, result[0][0]);
            assertEquals(4, result[1][1]);
        }

        @Test
        void mapToObj_double_3D_valid() {
            double[][][] arr = { { { 1.5, 2.5 } } };
            String[][][] result = Arrays.mapToObj(arr, d -> "D" + d, String.class);
            assertEquals("D1.5", result[0][0][0]);
            assertEquals("D2.5", result[0][0][1]);
        }

        @Test
        void mapToObj_double_runtimeType() {
            final String[] r = Arrays.mapToObj(new double[] { 1.5, 2.5 }, d -> "" + d, String.class);
            assertArrayEquals(new String[] { "1.5", "2.5" }, r);
            assertEquals(String[].class, r.getClass());
        }

        @Test
        void mapToObj_double2D() {
            double[][] a = { { 1.1, 2.2 }, { 3.3, 4.4 } };
            String[][] result = Arrays.mapToObj(a, d -> String.format(java.util.Locale.ROOT, "%.1f", d), String.class);

            assertEquals(2, result.length);
            assertEquals("1.1", result[0][0]);
            assertEquals("2.2", result[0][1]);
            assertEquals("3.3", result[1][0]);
            assertEquals("4.4", result[1][1]);
        }

        @Test
        void mapToObj_double2DArray() throws Exception {
            double[][] input = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            Integer[][] result = Arrays.mapToObj(input, d -> (int) Math.round(d), Integer.class);
            assertArrayEquals(new Integer[][] { { 2, 3 }, { 4, 5 } }, result);
        }

        @Test
        void mapToObj_double3D() {
            double[][][] a = { { { 1.1, 2.2 }, { 3.3 } } };
            String[][][] result = Arrays.mapToObj(a, d -> String.format(java.util.Locale.ROOT, "%.1f", d), String.class);

            assertEquals(1, result.length);
            assertEquals("1.1", result[0][0][0]);
            assertEquals("2.2", result[0][0][1]);
            assertEquals("3.3", result[0][1][0]);
        }

        @Test
        void mapToObj_double3DArray() throws Exception {
            double[][][] input = { { { 1.1, 2.2 } }, { { 3.3, 4.4 } } };
            Integer[][][] result = Arrays.mapToObj(input, d -> (int) d, Integer.class);
            assertArrayEquals(new Integer[][][] { { { 1, 2 } }, { { 3, 4 } } }, result);
        }

        @Test
        void mapToObj_doubleArray() throws Exception {
            double[] input = { 1.5, 2.5, 3.5 };
            String[] result = Arrays.mapToObj(input, d -> String.format(java.util.Locale.ROOT, "%.1f", d), String.class);
            assertArrayEquals(new String[] { "1.5", "2.5", "3.5" }, result);
        }

        @Test
        void mapToObj_doubleArray_null() throws Exception {
            double[] input = null;
            String[] result = Arrays.mapToObj(input, d -> String.valueOf(d), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_doubleArrayNull() {
            String[] result = Arrays.mapToObj((double[]) null, d -> "test", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_emptyArray() {
            int[] a = {};
            String[] result = Arrays.mapToObj(a, i -> "X", String.class);

            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_emptyArrays() {
            boolean[] empty = new boolean[0];
            String[] result = Arrays.mapToObj(empty, b -> "X", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_errorHandling_exceptionInMapper() {
            int[] input = { 1, 2, 3 };
            Throwables.IntFunction<String, Exception> faultyMapper = i -> {
                if (i == 2) {
                    throw new Exception("Conversion failed");
                }
                return String.valueOf(i);
            };

            assertThrows(Exception.class, () -> {
                Arrays.mapToObj(input, faultyMapper, String.class);
            });
        }

        @Test
        void mapToObj_float_runtimeType() {
            final String[] r = Arrays.mapToObj(new float[] { 1.5f, 2.5f }, f -> "f" + f, String.class);
            assertEquals(String[].class, r.getClass());
            assertArrayEquals(new String[] { "f1.5", "f2.5" }, r);
        }

        @Test
        void mapToObj_float2D() {
            float[][] a = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            String[][] result = Arrays.mapToObj(a, f -> String.format(java.util.Locale.ROOT, "%.1f", f), String.class);

            assertEquals(2, result.length);
            assertEquals("1.5", result[0][0]);
            assertEquals("2.5", result[0][1]);
            assertEquals("3.5", result[1][0]);
            assertEquals("4.5", result[1][1]);
        }

        @Test
        void mapToObj_float2DArray() throws Exception {
            float[][] input = { { 1.1f, 2.2f }, { 3.3f, 4.4f } };
            String[][] result = Arrays.mapToObj(input, f -> String.format(java.util.Locale.ROOT, "%.1f", f), String.class);
            assertArrayEquals(new String[][] { { "1.1", "2.2" }, { "3.3", "4.4" } }, result);
        }

        @Test
        void mapToObj_float3D() {
            float[][][] a = { { { 1.5f, 2.5f }, { 3.5f } } };
            String[][][] result = Arrays.mapToObj(a, f -> String.format(java.util.Locale.ROOT, "%.1f", f), String.class);

            assertEquals(1, result.length);
            assertEquals("1.5", result[0][0][0]);
            assertEquals("2.5", result[0][0][1]);
            assertEquals("3.5", result[0][1][0]);
        }

        @Test
        void mapToObj_float3DArray() throws Exception {
            float[][][] input = { { { 1.0f, 2.0f } }, { { 3.0f, 4.0f } } };
            Long[][][] result = Arrays.mapToObj(input, f -> (long) f, Long.class);
            assertArrayEquals(new Long[][][] { { { 1L, 2L } }, { { 3L, 4L } } }, result);
        }

        @Test
        void mapToObj_floatArray() throws Exception {
            float[] input = { 1.5f, 2.5f, 3.5f };
            Integer[] result = Arrays.mapToObj(input, f -> (int) f, Integer.class);
            assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
        }

        @Test
        void mapToObj_floatArray_null() throws Exception {
            float[] input = null;
            String[] result = Arrays.mapToObj(input, f -> String.valueOf(f), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_floatArray_values_1_1F_2_2F_3_3F() {
            float[] arr = { 1.1f, 2.2f, 3.3f };
            String[] result = Arrays.mapToObj(arr, f -> String.format(java.util.Locale.ROOT, "%.1f", f), String.class);

            assertArrayEquals(new String[] { "1.1", "2.2", "3.3" }, result);
        }

        @Test
        void mapToObj_floatArray_values_1_5F_2_5F_3_5F() {
            float[] a = { 1.5f, 2.5f, 3.5f };
            String[] result = Arrays.mapToObj(a, f -> String.format(java.util.Locale.ROOT, "%.1f", f), String.class);

            assertArrayEquals(new String[] { "1.5", "2.5", "3.5" }, result);
        }

        @Test
        void mapToObj_floatArrayNull() {
            String[] result = Arrays.mapToObj((float[]) null, f -> "test", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_floatToString() {
            // mapToObj({1.5f,2.5f,3.5f}, f -> String.valueOf(f), String.class) => ["1.5","2.5","3.5"]
            float[] floats = { 1.5f, 2.5f, 3.5f };
            String[] result = Arrays.mapToObj(floats, f -> String.valueOf(f), String.class);
            assertArrayEquals(new String[] { "1.5", "2.5", "3.5" }, result);
        }

        @Test
        void mapToObj_int_1D_null() {
            int[] arr = null;
            String[] result = Arrays.mapToObj(arr, i -> "num:" + i, String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_int_1D_valid() {
            int[] arr = { 1, 2, 3 };
            String[] result = Arrays.mapToObj(arr, i -> "num:" + i, String.class);
            assertArrayEquals(new String[] { "num:1", "num:2", "num:3" }, result);
        }

        @Test
        void mapToObj_int_2D_emptySubArrayPreserved() {
            final int[][] a = { { 1 }, {}, { 2 } };
            final String[][] r = Arrays.mapToObj(a, i -> "v" + i, String.class);
            assertEquals(3, r.length);
            assertArrayEquals(new String[] { "v1" }, r[0]);
            assertEquals(0, r[1].length);
            assertArrayEquals(new String[] { "v2" }, r[2]);
        }

        @Test
        void mapToObj_int_2D_nullInput_emptyArrayOfCorrectType() {
            final String[][] r = Arrays.mapToObj((int[][]) null, i -> "v" + i, String.class);
            assertNotNull(r);
            assertEquals(0, r.length);
            assertEquals(String[][].class, r.getClass());
        }

        @Test
        void mapToObj_int_2D_nullSubArray_becomesEmpty() {
            // The 2D method recurses to 1D method, where N.isEmpty(null) == true → empty array.
            // So a null sub-array becomes empty (not preserved as null).
            final int[][] a = { { 1, 2 }, null, { 3 } };
            final String[][] r = Arrays.mapToObj(a, i -> "v" + i, String.class);
            assertEquals(3, r.length);
            assertArrayEquals(new String[] { "v1", "v2" }, r[0]);
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
            assertEquals(String[].class, r[1].getClass());
            assertArrayEquals(new String[] { "v3" }, r[2]);
        }

        @Test
        void mapToObj_int_2D_runtimeTypeAndStructure() {
            final int[][] a = { { 1, 2 }, { 3 } };
            final String[][] r = Arrays.mapToObj(a, i -> "v" + i, String.class);
            assertEquals(String[][].class, r.getClass());
            assertEquals(2, r.length);
            assertArrayEquals(new String[] { "v1", "v2" }, r[0]);
            assertArrayEquals(new String[] { "v3" }, r[1]);
            assertEquals(String[].class, r[0].getClass());
        }

        @Test
        void mapToObj_int_2D_valid() {
            int[][] arr = { { 10, 20 }, { 30, 40 } };
            String[][] result = Arrays.mapToObj(arr, i -> String.valueOf(i), String.class);
            assertEquals("10", result[0][0]);
            assertEquals("40", result[1][1]);
        }

        @Test
        void mapToObj_int_3D_nullAtMultipleLevels() {
            final int[][][] a = { { { 1, 2 }, null }, null, { { 3 } } };
            final String[][][] r = Arrays.mapToObj(a, i -> "v" + i, String.class);
            assertEquals(String[][][].class, r.getClass());
            assertEquals(3, r.length);
            // outer null → becomes empty String[][] (2D method recurses to 2D, which returns empty[0][])
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
            // inner null becomes empty String[]
            assertNotNull(r[0][1]);
            assertEquals(0, r[0][1].length);
            assertArrayEquals(new String[] { "v1", "v2" }, r[0][0]);
            assertArrayEquals(new String[] { "v3" }, r[2][0]);
        }

        @Test
        void mapToObj_int_3D_valid() {
            int[][][] arr = { { { 1, 2 }, { 3 } } };
            Double[][][] result = Arrays.mapToObj(arr, i -> i * 1.5, Double.class);
            assertEquals(1.5, result[0][0][0], 0.001);
            assertEquals(3.0, result[0][0][1], 0.001);
            assertEquals(4.5, result[0][1][0], 0.001);
        }

        @Test
        void mapToObj_int_basic_runtimeType() {
            final String[] r = Arrays.mapToObj(new int[] { 1, 2, 3 }, i -> "v" + i, String.class);
            assertArrayEquals(new String[] { "v1", "v2", "v3" }, r);
            // RUNTIME TYPE: must be String[] not Object[]
            assertEquals(String[].class, r.getClass());
        }

        @Test
        void mapToObj_int_emptyInput_returnsEmptyArrayOfTargetType() {
            final String[] r = Arrays.mapToObj(new int[0], i -> "x", String.class);
            assertNotNull(r);
            assertEquals(0, r.length);
            assertEquals(String[].class, r.getClass());
        }

        @Test
        void mapToObj_int_mapperReturnsNull_isPreserved() {
            final String[] r = Arrays.mapToObj(new int[] { 1, 2, 3 }, i -> i == 2 ? null : "v" + i, String.class);
            assertArrayEquals(new String[] { "v1", null, "v3" }, r);
        }

        @Test
        void mapToObj_int_mapperThrows_propagates() {
            final RuntimeException ex = assertThrows(RuntimeException.class, () -> Arrays.mapToObj(new int[] { 1, 2, 3 }, i -> {
                if (i == 2) {
                    throw new RuntimeException("boom");
                }
                return "v" + i;
            }, String.class));
            assertEquals("boom", ex.getMessage());
        }

        @Test
        void mapToObj_int_nullInput_returnsEmptyArrayOfTargetType() {
            final Integer[] r = Arrays.mapToObj((int[]) null, i -> i, Integer.class);
            assertNotNull(r);
            assertEquals(0, r.length);
            assertEquals(Integer[].class, r.getClass());
        }

        @Test
        void mapToObj_int2D() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            String[][] result = Arrays.mapToObj(a, i -> "V" + i, String.class);

            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals("V1", result[0][0]);
            assertEquals("V4", result[1][1]);
        }

        @Test
        void mapToObj_int2DArray() throws Exception {
            int[][] input = { { 1, 2 }, { 3, 4 } };
            Double[][] result = Arrays.mapToObj(input, i -> i * 0.5, Double.class);
            assertArrayEquals(new Double[][] { { 0.5, 1.0 }, { 1.5, 2.0 } }, result);
        }

        @Test
        void mapToObj_int3D() {
            int[][][] a = { { { 1, 2 } } };
            String[][][] result = Arrays.mapToObj(a, i -> "X" + i, String.class);

            assertEquals(1, result.length);
            assertEquals(1, result[0].length);
            assertEquals("X1", result[0][0][0]);
        }

        @Test
        void mapToObj_int3DArray() throws Exception {
            int[][][] input = { { { 10, 20 } }, { { 30, 40 } } };
            String[][][] result = Arrays.mapToObj(input, i -> Integer.toHexString(i), String.class);
            assertArrayEquals(new String[][][] { { { "a", "14" } }, { { "1e", "28" } } }, result);
        }

        @Test
        void mapToObj_intArray() throws Exception {
            int[] input = { 1, 2, 3 };
            String[] result = Arrays.mapToObj(input, i -> "Number: " + i, String.class);
            assertArrayEquals(new String[] { "Number: 1", "Number: 2", "Number: 3" }, result);
        }

        @Test
        void mapToObj_intArray_null() throws Exception {
            int[] input = null;
            String[] result = Arrays.mapToObj(input, i -> String.valueOf(i), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_intArray_values_1_2_3() {
            int[] arr = { 1, 2, 3 };
            String[] result = Arrays.mapToObj(arr, i -> "Int:" + i, String.class);

            assertArrayEquals(new String[] { "Int:1", "Int:2", "Int:3" }, result);
        }

        @Test
        void mapToObj_intArray_values_1_2_3_4_5() {
            int[] a = { 1, 2, 3, 4, 5 };
            String[] result = Arrays.mapToObj(a, i -> "Val:" + i, String.class);

            assertEquals(5, result.length);
            assertEquals("Val:1", result[0]);
            assertEquals("Val:5", result[4]);
        }

        @Test
        void mapToObj_intArrayNull() {
            String[] result = Arrays.mapToObj((int[]) null, i -> "test", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_intToString() {
            // mapToObj({1,2,3}, i -> "Number: " + i, String.class) => ["Number: 1",...]
            int[] ints = { 1, 2, 3 };
            String[] result = Arrays.mapToObj(ints, i -> "Number: " + i, String.class);
            assertArrayEquals(new String[] { "Number: 1", "Number: 2", "Number: 3" }, result);
        }

        @Test
        void mapToObj_largeArray() throws Exception {
            int size = 10000;
            int[] input = new int[size];
            for (int i = 0; i < size; i++) {
                input[i] = i;
            }
            String[] result = Arrays.mapToObj(input, i -> String.valueOf(i), String.class);
            assertEquals(size, result.length);
            assertEquals("0", result[0]);
            assertEquals("9999", result[9999]);
        }

        @Test
        void mapToObj_long_1D_null() {
            long[] arr = null;
            String[] result = Arrays.mapToObj(arr, l -> "L" + l, String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_long_1D_valid() {
            long[] arr = { 100L, 200L, 300L };
            String[] result = Arrays.mapToObj(arr, l -> "L" + l, String.class);
            assertArrayEquals(new String[] { "L100", "L200", "L300" }, result);
        }

        @Test
        void mapToObj_long_2D_valid() {
            long[][] arr = { { 1L, 2L }, { 3L, 4L } };
            Integer[][] result = Arrays.mapToObj(arr, l -> (int) (l * 10), Integer.class);
            assertEquals(10, result[0][0]);
            assertEquals(40, result[1][1]);
        }

        @Test
        void mapToObj_long_3D_valid() {
            long[][][] arr = { { { 5L } } };
            String[][][] result = Arrays.mapToObj(arr, l -> "long:" + l, String.class);
            assertEquals("long:5", result[0][0][0]);
        }

        @Test
        void mapToObj_long_runtimeType() {
            final Long[] r = Arrays.mapToObj(new long[] { 100L, 200L }, l -> l + 1, Long.class);
            assertArrayEquals(new Long[] { 101L, 201L }, r);
            assertEquals(Long[].class, r.getClass());
        }

        @Test
        void mapToObj_long2D() {
            long[][] a = { { 100L, 200L }, { 300L, 400L } };
            String[][] result = Arrays.mapToObj(a, l -> "L" + l, String.class);

            assertEquals(2, result.length);
            assertEquals("L100", result[0][0]);
            assertEquals("L200", result[0][1]);
            assertEquals("L300", result[1][0]);
            assertEquals("L400", result[1][1]);
        }

        @Test
        void mapToObj_long2DArray() throws Exception {
            long[][] input = { { 100L, 200L }, { 300L, 400L } };
            Integer[][] result = Arrays.mapToObj(input, l -> (int) (l / 100), Integer.class);
            assertArrayEquals(new Integer[][] { { 1, 2 }, { 3, 4 } }, result);
        }

        @Test
        void mapToObj_long3D() {
            long[][][] a = { { { 100L, 200L }, { 300L } } };
            String[][][] result = Arrays.mapToObj(a, l -> "Long" + l, String.class);

            assertEquals(1, result.length);
            assertEquals("Long100", result[0][0][0]);
            assertEquals("Long200", result[0][0][1]);
            assertEquals("Long300", result[0][1][0]);
        }

        @Test
        void mapToObj_long3DArray() throws Exception {
            long[][][] input = { { { 1L, 2L } }, { { 3L, 4L } } };
            String[][][] result = Arrays.mapToObj(input, l -> "L" + l, String.class);
            assertArrayEquals(new String[][][] { { { "L1", "L2" } }, { { "L3", "L4" } } }, result);
        }

        @Test
        void mapToObj_longArray() throws Exception {
            long[] input = { 1000L, 2000L, 3000L };
            String[] result = Arrays.mapToObj(input, l -> l / 1000 + "k", String.class);
            assertArrayEquals(new String[] { "1k", "2k", "3k" }, result);
        }

        @Test
        void mapToObj_longArray_null() throws Exception {
            long[] input = null;
            String[] result = Arrays.mapToObj(input, l -> String.valueOf(l), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_longArray_values_1L_2L_3L() {
            long[] a = { 1L, 2L, 3L };
            String[] result = Arrays.mapToObj(a, l -> "L" + l, String.class);

            assertEquals(3, result.length);
            assertEquals("L1", result[0]);
            assertEquals("L3", result[2]);
        }

        @Test
        void mapToObj_longArray_values_100L_200L_300L() {
            long[] arr = { 100L, 200L, 300L };
            String[] result = Arrays.mapToObj(arr, l -> "Long:" + l, String.class);

            assertArrayEquals(new String[] { "Long:100", "Long:200", "Long:300" }, result);
        }

        @Test
        void mapToObj_longArrayNull() {
            String[] result = Arrays.mapToObj((long[]) null, l -> "test", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_longToString() {
            // mapToObj({1000000L,...}, l -> l/1000000 + "M", String.class) => ["1M","2M","3M"]
            long[] longs = { 1000000L, 2000000L, 3000000L };
            String[] result = Arrays.mapToObj(longs, l -> l / 1000000 + "M", String.class);
            assertArrayEquals(new String[] { "1M", "2M", "3M" }, result);
        }

        @Test
        void mapToObj_null2DIntArray() {
            int[][] a = null;
            String[][] result = Arrays.mapToObj(a, i -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_null3DIntArray() {
            int[][][] a = null;
            String[][][] result = Arrays.mapToObj(a, i -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_nullBooleanArray() {
            boolean[] a = null;
            String[] result = Arrays.mapToObj(a, b -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_nullByteArray() {
            byte[] a = null;
            String[] result = Arrays.mapToObj(a, b -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_nullCharArray() {
            char[] a = null;
            String[] result = Arrays.mapToObj(a, c -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_nullDoubleArray() {
            double[] a = null;
            String[] result = Arrays.mapToObj(a, d -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_nullFloatArray() {
            float[] a = null;
            String[] result = Arrays.mapToObj(a, f -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_nullIntArray() {
            int[] a = null;
            String[] result = Arrays.mapToObj(a, i -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_nullLongArray() {
            long[] a = null;
            String[] result = Arrays.mapToObj(a, l -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_nullShortArray() {
            short[] a = null;
            String[] result = Arrays.mapToObj(a, s -> "X", String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_short_runtimeType() {
            final String[] r = Arrays.mapToObj(new short[] { 10, 20 }, s -> "s" + s, String.class);
            assertEquals(String[].class, r.getClass());
            assertArrayEquals(new String[] { "s10", "s20" }, r);
        }

        @Test
        void mapToObj_short2D() {
            short[][] a = { { 10, 20 }, { 30, 40 } };
            String[][] result = Arrays.mapToObj(a, s -> "Short:" + s, String.class);

            assertEquals(2, result.length);
            assertEquals("Short:10", result[0][0]);
            assertEquals("Short:20", result[0][1]);
            assertEquals("Short:30", result[1][0]);
            assertEquals("Short:40", result[1][1]);
        }

        @Test
        void mapToObj_short2DArray() throws Exception {
            short[][] input = { { 100, 200 }, { 300, 400 } };
            Integer[][] result = Arrays.mapToObj(input, s -> s / 100, Integer.class);
            assertArrayEquals(new Integer[][] { { 1, 2 }, { 3, 4 } }, result);
        }

        @Test
        void mapToObj_short3D() {
            short[][][] a = { { { 10, 20 }, { 30 } } };
            String[][][] result = Arrays.mapToObj(a, s -> "S" + s, String.class);

            assertEquals(1, result.length);
            assertEquals("S10", result[0][0][0]);
            assertEquals("S20", result[0][0][1]);
            assertEquals("S30", result[0][1][0]);
        }

        @Test
        void mapToObj_short3DArray() throws Exception {
            short[][][] input = { { { 1, 2 } }, { { 3, 4 } } };
            Long[][][] result = Arrays.mapToObj(input, s -> (long) s * 1000, Long.class);
            assertArrayEquals(new Long[][][] { { { 1000L, 2000L } }, { { 3000L, 4000L } } }, result);
        }

        @Test
        void mapToObj_shortArray() throws Exception {
            short[] input = { 10, 20, 30 };
            String[] result = Arrays.mapToObj(input, s -> "S" + s, String.class);
            assertArrayEquals(new String[] { "S10", "S20", "S30" }, result);
        }

        @Test
        void mapToObj_shortArray_null() throws Exception {
            short[] input = null;
            String[] result = Arrays.mapToObj(input, s -> String.valueOf(s), String.class);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObj_shortArray_prefixesEachValue() {
            short[] arr = { 10, 20, 30 };
            String[] result = Arrays.mapToObj(arr, s -> "Short:" + s, String.class);

            assertArrayEquals(new String[] { "Short:10", "Short:20", "Short:30" }, result);
        }

        @Test
        void mapToObj_shortArrayNull() {
            String[] result = Arrays.mapToObj((short[]) null, s -> "test", String.class);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToObjNestedEmptyInputs_charAndByte() throws Exception {
            String[][] mappedChars2d = Arrays.mapToObj((char[][]) null, c -> String.valueOf(c), String.class);
            Integer[][][] mappedChars3d = Arrays.mapToObj(new char[0][][], c -> (int) c, Integer.class);
            String[][] mappedBytes2d = Arrays.mapToObj(new byte[0][], b -> "B" + b, String.class);
            Integer[][][] mappedBytes3d = Arrays.mapToObj((byte[][][]) null, b -> (int) b, Integer.class);

            assertEquals(0, mappedChars2d.length);
            assertEquals(String[].class, mappedChars2d.getClass().getComponentType());
            assertEquals(0, mappedChars3d.length);
            assertEquals(Integer[][].class, mappedChars3d.getClass().getComponentType());
            assertEquals(0, mappedBytes2d.length);
            assertEquals(String[].class, mappedBytes2d.getClass().getComponentType());
            assertEquals(0, mappedBytes3d.length);
            assertEquals(Integer[][].class, mappedBytes3d.getClass().getComponentType());
        }
    }

    @Nested
    class MapToBooleanTests {

        @Test
        void f_mapToBoolean_null() {
            String[] arr = null;
            boolean[] result = Arrays.f.mapToBoolean(arr, s -> s.length() > 3);
            assertEquals(0, result.length);
        }

        @Test
        void f_mapToBoolean_nullArray_returnsEmpty() {
            boolean[] result = f.mapToBoolean((String[]) null, s -> true);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void f_mapToBoolean_valid() {
            String[] arr = { "hello", "hi", "world", "go" };
            boolean[] result = Arrays.f.mapToBoolean(arr, s -> s.length() > 3);
            assertArrayEquals(new boolean[] { true, false, true, false }, result);
        }

        @Test
        void fF_mapToBoolean() {
            String[][] arr = { { "hello", "hi" }, { "world", "go" } };
            boolean[][] result = Arrays.ff.mapToBoolean(arr, s -> s.length() > 3);
            assertArrayEquals(new boolean[] { true, false }, result[0]);
            assertArrayEquals(new boolean[] { true, false }, result[1]);
        }

        @Test
        void fFF_mapToBoolean() {
            String[][][] arr = { { { "hello", "hi" } }, { { "world", "go" } } };
            boolean[][][] result = Arrays.fff.mapToBoolean(arr, s -> s.length() > 3);
            assertArrayEquals(new boolean[] { true, false }, result[0][0]);
            assertArrayEquals(new boolean[] { true, false }, result[1][0]);
        }

        @Test
        void fFF_mapToBoolean_nullInnerSubArrayBecomesEmpty() {
            Integer[][][] arr = { { { 1, 2 } }, null, { { 3 } } };
            boolean[][][] result = fff.mapToBoolean(arr, x -> x > 1);
            assertEquals(3, result.length);
            assertArrayEquals(new boolean[] { false, true }, result[0][0]);
            assertNotNull(result[1]);
            assertEquals(0, result[1].length);
            assertArrayEquals(new boolean[] { true }, result[2][0]);
        }

        @Test
        void mapToBoolean() throws Exception {
            // Test even number predicate
            Integer[][] array = { { 1, 2, 3 }, { 4, 5, 6 } };
            boolean[][] evens = ff.mapToBoolean(array, x -> x % 2 == 0);
            assertArrayEquals(new boolean[] { false, true, false }, evens[0]);
            assertArrayEquals(new boolean[] { true, false, true }, evens[1]);

            // Test null array
            Integer[][] nullArray = null;
            assertEquals(0, ff.mapToBoolean(nullArray, x -> x % 2 == 0).length);

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            boolean[][] mapped = ff.mapToBoolean(arrayWithNulls, x -> x > 2);
            assertArrayEquals(new boolean[] { false, false }, mapped[0]);
            assertEquals(0, mapped[1].length);
            assertArrayEquals(new boolean[] { true, true }, mapped[2]);

            // Test String predicate
            String[][] stringArray = { { "hello", "hi" }, { "world", "foo" } };
            boolean[][] longStrings = ff.mapToBoolean(stringArray, s -> s.length() > 3);
            assertArrayEquals(new boolean[] { true, false }, longStrings[0]);
            assertArrayEquals(new boolean[] { true, false }, longStrings[1]);
        }

        @Test
        void mapToBoolean_emptyArray() throws Exception {
            String[] input = new String[0];
            boolean[] result = f.mapToBoolean(input, s -> true);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToBoolean_nullArray() throws Exception {
            String[] input = null;
            boolean[] result = f.mapToBoolean(input, s -> true);
            assertEquals(0, result.length);
        }

        @Test
        void mapToBoolean_stringLength() throws Exception {
            String[] input = { "hello", "hi", "world", "a" };
            boolean[] result = f.mapToBoolean(input, s -> s.length() > 3);
            assertArrayEquals(new boolean[] { true, false, true, false }, result);
        }

        @Test
        void mapToBoolean_values_1_2_3_4_5_6() throws Exception {
            // Test even number check
            Integer[][][] numbers = { { { 1, 2, 3 } }, { { 4, 5, 6 } } };
            boolean[][][] evenMask = fff.mapToBoolean(numbers, n -> n % 2 == 0);
            assertFalse(evenMask[0][0][0]);
            assertTrue(evenMask[0][0][1]);
            assertFalse(evenMask[0][0][2]);
            assertTrue(evenMask[1][0][0]);
            assertFalse(evenMask[1][0][1]);
            assertTrue(evenMask[1][0][2]);

            // Test null input
            boolean[][][] nullResult = fff.mapToBoolean(null, n -> true);
            assertEquals(0, nullResult.length);

            // Test String length check
            String[][][] strings = { { { "a", "ab", "abc" } }, { { "abcd", "abcde" } } };
            boolean[][][] lengthCheck = fff.mapToBoolean(strings, s -> s.length() > 2);
            assertFalse(lengthCheck[0][0][0]);
            assertFalse(lengthCheck[0][0][1]);
            assertTrue(lengthCheck[0][0][2]);
            assertTrue(lengthCheck[1][0][0]);
            assertTrue(lengthCheck[1][0][1]);
        }
    }

    @Nested
    class MapToCharTests {

        @Test
        void f_mapToChar_null() {
            String[] arr = null;
            char[] result = Arrays.f.mapToChar(arr, s -> s.charAt(0));
            assertEquals(0, result.length);
        }

        @Test
        void f_mapToChar_valid() {
            String[] arr = { "apple", "banana", "cherry" };
            char[] result = Arrays.f.mapToChar(arr, s -> s.charAt(0));
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, result);
        }

        @Test
        void fF_mapToChar() {
            String[][] arr = { { "apple", "banana" }, { "cherry", "date" } };
            char[][] result = Arrays.ff.mapToChar(arr, s -> s.charAt(0));
            assertArrayEquals(new char[] { 'a', 'b' }, result[0]);
            assertArrayEquals(new char[] { 'c', 'd' }, result[1]);
        }

        @Test
        void fFF_mapToChar() {
            String[][][] arr = { { { "apple", "banana" } } };
            char[][][] result = Arrays.fff.mapToChar(arr, s -> s.charAt(0));
            assertArrayEquals(new char[] { 'a', 'b' }, result[0][0]);
        }

        @Test
        void mapToChar() throws Exception {
            // Test first character extraction
            String[][] array = { { "apple", "banana" }, { "cat", "dog" } };
            char[][] firstChars = ff.mapToChar(array, s -> s.charAt(0));
            assertArrayEquals(new char[] { 'a', 'b' }, firstChars[0]);
            assertArrayEquals(new char[] { 'c', 'd' }, firstChars[1]);

            // Test null array
            String[][] nullArray = null;
            assertEquals(0, ff.mapToChar(nullArray, s -> s.charAt(0)).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "abc", "def" }, null, { "ghi", "jkl" } };
            char[][] mapped = ff.mapToChar(arrayWithNulls, s -> s.charAt(1));
            assertArrayEquals(new char[] { 'b', 'e' }, mapped[0]);
            assertEquals(0, mapped[1].length);
            assertArrayEquals(new char[] { 'h', 'k' }, mapped[2]);

            // Test Integer to char
            Integer[][] intArray = { { 65, 66 }, { 67, 68 } };
            char[][] chars = ff.mapToChar(intArray, i -> (char) i.intValue());
            assertArrayEquals(new char[] { 'A', 'B' }, chars[0]);
            assertArrayEquals(new char[] { 'C', 'D' }, chars[1]);
        }

        @Test
        void mapToChar_emptyArray() throws Exception {
            String[] input = new String[0];
            char[] result = f.mapToChar(input, s -> 'a');
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToChar_firstCharacter() throws Exception {
            String[] input = { "apple", "banana", "cherry" };
            char[] result = f.mapToChar(input, s -> s.charAt(0));
            assertArrayEquals(new char[] { 'a', 'b', 'c' }, result);
        }

        @Test
        void mapToChar_nullArray() throws Exception {
            String[] input = null;
            char[] result = f.mapToChar(input, s -> 'a');
            assertEquals(0, result.length);
        }

        @Test
        void mapToChar_object2DArray() {
            // Test with String two-dimensional array
            String[][] input = { { "Apple", "Banana" }, { "Cherry" } };
            char[][] result = ff.mapToChar(input, s -> s.charAt(0));
            assertArrayEquals(new char[] { 'A', 'B' }, result[0]);
            assertArrayEquals(new char[] { 'C' }, result[1]);

            // Test with null two-dimensional array
            String[][] nullInput = null;
            char[][] nullResult = ff.mapToChar(nullInput, s -> s.charAt(0));
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToChar_object3DArray() {
            // Test with String three-dimensional array
            String[][][] input = { { { "Dog", "Elephant" } }, { { "Fox" } } };
            char[][][] result = fff.mapToChar(input, s -> s.charAt(0));
            assertArrayEquals(new char[] { 'D', 'E' }, result[0][0]);
            assertArrayEquals(new char[] { 'F' }, result[1][0]);

            // Test with null three-dimensional array
            String[][][] nullInput = null;
            char[][][] nullResult = fff.mapToChar(nullInput, s -> s.charAt(0));
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToChar_objectArray() {
            // Test with String array
            String[] input = { "Hello", "World", "Test" };
            char[] result = f.mapToChar(input, s -> s.charAt(0));
            assertArrayEquals(new char[] { 'H', 'W', 'T' }, result);

            // Test with empty array
            String[] emptyInput = {};
            char[] emptyResult = f.mapToChar(emptyInput, s -> s.charAt(0));
            assertArrayEquals(new char[] {}, emptyResult);

            // Test with null array
            String[] nullInput = null;
            char[] nullResult = f.mapToChar(nullInput, s -> s.charAt(0));
            assertEquals(0, nullResult.length);

            // Test with Integer array to char
            Integer[] intInput = { 65, 66, 67 }; // ASCII values for A, B, C
            char[] charResult = f.mapToChar(intInput, i -> (char) i.intValue());
            assertArrayEquals(new char[] { 'A', 'B', 'C' }, charResult);
        }

        @Test
        void mapToChar_values_apple_banana_cat_dog() throws Exception {
            // Test extracting first character
            String[][][] words = { { { "apple", "banana" } }, { { "cat", "dog" } } };
            char[][][] firstLetters = fff.mapToChar(words, s -> s.charAt(0));
            assertEquals('a', firstLetters[0][0][0]);
            assertEquals('b', firstLetters[0][0][1]);
            assertEquals('c', firstLetters[1][0][0]);
            assertEquals('d', firstLetters[1][0][1]);

            // Test null input
            char[][][] nullResult = fff.mapToChar(null, s -> 'x');
            assertEquals(0, nullResult.length);

            // Test with Integer to char conversion
            Integer[][][] numbers = { { { 65, 66 } }, { { 67, 68 } } };
            char[][][] chars = fff.mapToChar(numbers, n -> (char) n.intValue());
            assertEquals('A', chars[0][0][0]);
            assertEquals('B', chars[0][0][1]);
            assertEquals('C', chars[1][0][0]);
            assertEquals('D', chars[1][0][1]);
        }
    }

    @Nested
    class MapToByteTests {

        @Test
        void f_mapToByte_null() {
            String[] arr = null;
            byte[] result = Arrays.f.mapToByte(arr, s -> (byte) s.length());
            assertEquals(0, result.length);
        }

        @Test
        void f_mapToByte_valid() {
            String[] arr = { "a", "ab", "abc" };
            byte[] result = Arrays.f.mapToByte(arr, s -> (byte) s.length());
            assertArrayEquals(new byte[] { 1, 2, 3 }, result);
        }

        @Test
        void fF_mapToByte() {
            String[][] arr = { { "a", "ab" }, { "abc", "abcd" } };
            byte[][] result = Arrays.ff.mapToByte(arr, s -> (byte) s.length());
            assertArrayEquals(new byte[] { 1, 2 }, result[0]);
            assertArrayEquals(new byte[] { 3, 4 }, result[1]);
        }

        @Test
        void fFF_mapToByte() {
            String[][][] arr = { { { "a", "ab" } } };
            byte[][][] result = Arrays.fff.mapToByte(arr, s -> (byte) s.length());
            assertArrayEquals(new byte[] { 1, 2 }, result[0][0]);
        }

        @Test
        void mapToByte() throws Exception {
            // Test Integer to byte
            Integer[][] array = { { 10, 20 }, { 30, 40 } };
            byte[][] bytes = ff.mapToByte(array, Integer::byteValue);
            assertArrayEquals(new byte[] { 10, 20 }, bytes[0]);
            assertArrayEquals(new byte[] { 30, 40 }, bytes[1]);

            // Test null array
            Integer[][] nullArray = null;
            assertEquals(0, ff.mapToByte(nullArray, Integer::byteValue).length);

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            byte[][] mapped = ff.mapToByte(arrayWithNulls, Integer::byteValue);
            assertArrayEquals(new byte[] { 1, 2 }, mapped[0]);
            assertEquals(0, mapped[1].length);
            assertArrayEquals(new byte[] { 3, 4 }, mapped[2]);

            // Test String to byte (parsing)
            String[][] stringArray = { { "1", "2" }, { "3", "4" } };
            byte[][] parsedBytes = ff.mapToByte(stringArray, Byte::parseByte);
            assertArrayEquals(new byte[] { 1, 2 }, parsedBytes[0]);
            assertArrayEquals(new byte[] { 3, 4 }, parsedBytes[1]);
        }

        @Test
        void mapToByte_emptyArray() throws Exception {
            String[] input = new String[0];
            byte[] result = f.mapToByte(input, s -> (byte) 0);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToByte_nullArray() throws Exception {
            String[] input = null;
            byte[] result = f.mapToByte(input, s -> (byte) 0);
            assertEquals(0, result.length);
        }

        @Test
        void mapToByte_object2DArray() {
            // Test with Integer two-dimensional array
            Integer[][] input = { { 10, 20 }, { 30 } };
            byte[][] result = ff.mapToByte(input, i -> i.byteValue());
            assertArrayEquals(new byte[] { 10, 20 }, result[0]);
            assertArrayEquals(new byte[] { 30 }, result[1]);

            // Test with null two-dimensional array
            Integer[][] nullInput = null;
            byte[][] nullResult = ff.mapToByte(nullInput, i -> i.byteValue());
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToByte_object3DArray() {
            // Test with Integer three-dimensional array
            Integer[][][] input = { { { 1, 2 } }, { { 3 } } };
            byte[][][] result = fff.mapToByte(input, i -> i.byteValue());
            assertArrayEquals(new byte[] { 1, 2 }, result[0][0]);
            assertArrayEquals(new byte[] { 3 }, result[1][0]);

            // Test with null three-dimensional array
            Integer[][][] nullInput = null;
            byte[][][] nullResult = fff.mapToByte(nullInput, i -> i.byteValue());
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToByte_objectArray() {
            // Test with Integer array
            Integer[] input = { 10, 20, 30, 127, -128 };
            byte[] result = f.mapToByte(input, i -> i.byteValue());
            assertArrayEquals(new byte[] { 10, 20, 30, 127, -128 }, result);

            // Test with empty array
            Integer[] emptyInput = {};
            byte[] emptyResult = f.mapToByte(emptyInput, i -> i.byteValue());
            assertArrayEquals(new byte[] {}, emptyResult);

            // Test with null array
            Integer[] nullInput = null;
            byte[] nullResult = f.mapToByte(nullInput, i -> i.byteValue());
            assertEquals(0, nullResult.length);

            // Test with String array containing numbers
            String[] strInput = { "1", "2", "3" };
            byte[] byteResult = f.mapToByte(strInput, s -> Byte.parseByte(s));
            assertArrayEquals(new byte[] { 1, 2, 3 }, byteResult);
        }

        @Test
        void mapToByte_stringToByte() throws Exception {
            String[] input = { "1", "2", "3" };
            byte[] result = f.mapToByte(input, s -> Byte.parseByte(s));
            assertArrayEquals(new byte[] { 1, 2, 3 }, result);
        }

        @Test
        void mapToByte_values_10_20_30_40() throws Exception {
            // Test Integer to byte conversion
            Integer[][][] numbers = { { { 10, 20 } }, { { 30, 40 } } };
            byte[][][] bytes = fff.mapToByte(numbers, Integer::byteValue);
            assertEquals((byte) 10, bytes[0][0][0]);
            assertEquals((byte) 20, bytes[0][0][1]);
            assertEquals((byte) 30, bytes[1][0][0]);
            assertEquals((byte) 40, bytes[1][0][1]);

            // Test null input
            byte[][][] nullResult = fff.mapToByte(null, n -> (byte) 0);
            assertEquals(0, nullResult.length);

            // Test String length to byte
            String[][][] strings = { { { "a", "ab" } }, { { "abc", "abcd" } } };
            byte[][][] lengths = fff.mapToByte(strings, s -> (byte) s.length());
            assertEquals((byte) 1, lengths[0][0][0]);
            assertEquals((byte) 2, lengths[0][0][1]);
            assertEquals((byte) 3, lengths[1][0][0]);
            assertEquals((byte) 4, lengths[1][0][1]);
        }
    }

    @Nested
    class MapToShortTests {

        @Test
        void f_mapToShort_null() {
            String[] arr = null;
            short[] result = Arrays.f.mapToShort(arr, s -> (short) s.length());
            assertEquals(0, result.length);
        }

        @Test
        void f_mapToShort_valid() {
            String[] arr = { "x", "xy", "xyz" };
            short[] result = Arrays.f.mapToShort(arr, s -> (short) s.length());
            assertArrayEquals(new short[] { 1, 2, 3 }, result);
        }

        @Test
        void fF_mapToShort() {
            String[][] arr = { { "x", "xy" } };
            short[][] result = Arrays.ff.mapToShort(arr, s -> (short) s.length());
            assertArrayEquals(new short[] { 1, 2 }, result[0]);
        }

        @Test
        void fFF_mapToShort() {
            String[][][] arr = { { { "x", "xy" } } };
            short[][][] result = Arrays.fff.mapToShort(arr, s -> (short) s.length());
            assertArrayEquals(new short[] { 1, 2 }, result[0][0]);
        }

        @Test
        void mapToShort() throws Exception {
            // Test Integer to short
            Integer[][] array = { { 100, 200 }, { 300, 400 } };
            short[][] shorts = ff.mapToShort(array, Integer::shortValue);
            assertArrayEquals(new short[] { 100, 200 }, shorts[0]);
            assertArrayEquals(new short[] { 300, 400 }, shorts[1]);

            // Test null array
            Integer[][] nullArray = null;
            assertEquals(0, ff.mapToShort(nullArray, Integer::shortValue).length);

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 10, 20 }, null, { 30, 40 } };
            short[][] mapped = ff.mapToShort(arrayWithNulls, Integer::shortValue);
            assertArrayEquals(new short[] { 10, 20 }, mapped[0]);
            assertEquals(0, mapped[1].length);
            assertArrayEquals(new short[] { 30, 40 }, mapped[2]);

            // Test String to short
            String[][] stringArray = { { "100", "200" }, { "300", "400" } };
            short[][] parsedShorts = ff.mapToShort(stringArray, Short::parseShort);
            assertArrayEquals(new short[] { 100, 200 }, parsedShorts[0]);
            assertArrayEquals(new short[] { 300, 400 }, parsedShorts[1]);
        }

        @Test
        void mapToShort_emptyArray() throws Exception {
            String[] input = new String[0];
            short[] result = f.mapToShort(input, s -> (short) 0);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToShort_nullArray() throws Exception {
            String[] input = null;
            short[] result = f.mapToShort(input, s -> (short) 0);
            assertEquals(0, result.length);
        }

        @Test
        void mapToShort_object2DArray() {
            // Test with Integer two-dimensional array
            Integer[][] input = { { 1000, 2000 }, { 3000 } };
            short[][] result = ff.mapToShort(input, i -> i.shortValue());
            assertArrayEquals(new short[] { 1000, 2000 }, result[0]);
            assertArrayEquals(new short[] { 3000 }, result[1]);

            // Test with null two-dimensional array
            Integer[][] nullInput = null;
            short[][] nullResult = ff.mapToShort(nullInput, i -> i.shortValue());
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToShort_object3DArray() {
            // Test with Integer three-dimensional array
            Integer[][][] input = { { { 100, 200 } }, { { 300 } } };
            short[][][] result = fff.mapToShort(input, i -> i.shortValue());
            assertArrayEquals(new short[] { 100, 200 }, result[0][0]);
            assertArrayEquals(new short[] { 300 }, result[1][0]);

            // Test with null three-dimensional array
            Integer[][][] nullInput = null;
            short[][][] nullResult = fff.mapToShort(nullInput, i -> i.shortValue());
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToShort_objectArray() {
            // Test with Integer array
            Integer[] input = { 100, 200, 300, 32767, -32768 };
            short[] result = f.mapToShort(input, i -> i.shortValue());
            assertArrayEquals(new short[] { 100, 200, 300, 32767, -32768 }, result);

            // Test with empty array
            Integer[] emptyInput = {};
            short[] emptyResult = f.mapToShort(emptyInput, i -> i.shortValue());
            assertArrayEquals(new short[] {}, emptyResult);

            // Test with null array
            Integer[] nullInput = null;
            short[] nullResult = f.mapToShort(nullInput, i -> i.shortValue());
            assertEquals(0, nullResult.length);

            // Test with String array containing numbers
            String[] strInput = { "1000", "2000", "3000" };
            short[] shortResult = f.mapToShort(strInput, s -> Short.parseShort(s));
            assertArrayEquals(new short[] { 1000, 2000, 3000 }, shortResult);
        }

        @Test
        void mapToShort_stringToShort() throws Exception {
            String[] input = { "10", "20", "30" };
            short[] result = f.mapToShort(input, s -> Short.parseShort(s));
            assertArrayEquals(new short[] { 10, 20, 30 }, result);
        }

        @Test
        void mapToShort_values_100_200_300_400() throws Exception {
            // Test Integer to short conversion
            Integer[][][] numbers = { { { 100, 200 } }, { { 300, 400 } } };
            short[][][] shorts = fff.mapToShort(numbers, Integer::shortValue);
            assertEquals((short) 100, shorts[0][0][0]);
            assertEquals((short) 200, shorts[0][0][1]);
            assertEquals((short) 300, shorts[1][0][0]);
            assertEquals((short) 400, shorts[1][0][1]);

            // Test null input
            short[][][] nullResult = fff.mapToShort(null, n -> (short) 0);
            assertEquals(0, nullResult.length);

            // Test with larger numbers
            Integer[][][] largeNumbers = { { { 1000, 2000 } }, { { 3000, 4000 } } };
            short[][][] largeShorts = fff.mapToShort(largeNumbers, n -> n.shortValue());
            assertEquals((short) 1000, largeShorts[0][0][0]);
            assertEquals((short) 2000, largeShorts[0][0][1]);
            assertEquals((short) 3000, largeShorts[1][0][0]);
            assertEquals((short) 4000, largeShorts[1][0][1]);
        }
    }

    @Nested
    class MapToIntTests {

        @Test
        void f_mapToInt_appliesPerElement() {
            String[] arr = { "1", "10", "100" };
            int[] result = f.mapToInt(arr, Integer::parseInt);
            assertArrayEquals(new int[] { 1, 10, 100 }, result);
        }

        @Test
        void f_mapToInt_null() {
            String[] arr = null;
            int[] result = Arrays.f.mapToInt(arr, String::length);
            assertEquals(0, result.length);
        }

        @Test
        void f_mapToInt_valid() {
            String[] arr = { "a", "ab", "abc", "abcd" };
            int[] result = Arrays.f.mapToInt(arr, String::length);
            assertArrayEquals(new int[] { 1, 2, 3, 4 }, result);
        }

        @Test
        void fF_mapToInt() {
            String[][] arr = { { "a", "ab", "abc" } };
            int[][] result = Arrays.ff.mapToInt(arr, String::length);
            assertArrayEquals(new int[] { 1, 2, 3 }, result[0]);
        }

        @Test
        void fF_mapToInt_nullInnerSubArrayBecomesEmpty() {
            Integer[][] arr = { { 1, 2 }, null, { 3 } };
            int[][] result = ff.mapToInt(arr, x -> x * 2);
            assertEquals(3, result.length);
            assertArrayEquals(new int[] { 2, 4 }, result[0]);
            assertNotNull(result[1]);
            assertEquals(0, result[1].length);
            assertArrayEquals(new int[] { 6 }, result[2]);
        }

        @Test
        void fFF_mapToInt() {
            String[][][] arr = { { { "a", "ab", "abc" } } };
            int[][][] result = Arrays.fff.mapToInt(arr, String::length);
            assertArrayEquals(new int[] { 1, 2, 3 }, result[0][0]);
        }

        @Test
        void fFF_mapToInt_acrossPrimitiveVariants() throws Exception {
            String[][][] strs = { { { "1", "2" } }, { { "3", "4" } } };
            int[][][] ints = fff.mapToInt(strs, Integer::parseInt);
            assertArrayEquals(new int[] { 1, 2 }, ints[0][0]);
            assertArrayEquals(new int[] { 3, 4 }, ints[1][0]);

            long[][][] longs = fff.mapToLong(strs, Long::parseLong);
            assertArrayEquals(new long[] { 1L, 2L }, longs[0][0]);

            boolean[][][] bools = fff.mapToBoolean(strs, s -> Integer.parseInt(s) > 2);
            assertArrayEquals(new boolean[] { false, false }, bools[0][0]);
            assertArrayEquals(new boolean[] { true, true }, bools[1][0]);
        }

        @Test
        void mapToInt() throws Exception {
            // Test String to int
            String[][] array = { { "10", "20" }, { "30", "40" } };
            int[][] numbers = ff.mapToInt(array, Integer::parseInt);
            assertArrayEquals(new int[] { 10, 20 }, numbers[0]);
            assertArrayEquals(new int[] { 30, 40 }, numbers[1]);

            // Test null array
            String[][] nullArray = null;
            assertEquals(0, ff.mapToInt(nullArray, Integer::parseInt).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1", "2" }, null, { "3", "4" } };
            int[][] mapped = ff.mapToInt(arrayWithNulls, Integer::parseInt);
            assertArrayEquals(new int[] { 1, 2 }, mapped[0]);
            assertEquals(0, mapped[1].length);
            assertArrayEquals(new int[] { 3, 4 }, mapped[2]);

            // Test String length to int
            String[][] stringArray = { { "hi", "hello" }, { "world", "foo" } };
            int[][] lengths = ff.mapToInt(stringArray, String::length);
            assertArrayEquals(new int[] { 2, 5 }, lengths[0]);
            assertArrayEquals(new int[] { 5, 3 }, lengths[1]);
        }

        @Test
        void mapToInt_1D_double_normal() {
            double[] arr = { 1.5, 2.7, 3.2, 4.9 };
            int[] result = Arrays.mapToInt(arr, d -> (int) Math.round(d));
            assertNotNull(result);
            assertEquals(2, result[0]);
            assertEquals(5, result[3]);
        }

        @Test
        void mapToInt_1D_double_null() {
            double[] arr = null;
            int[] result = Arrays.mapToInt(arr, d -> (int) d);
            assertEquals(0, result.length);
        }

        @Test
        void mapToInt_1D_long_normal() {
            long[] arr = { 100L, 200L, 300L };
            int[] result = Arrays.mapToInt(arr, l -> (int) (l / 10));
            assertNotNull(result);
            assertEquals(3, result.length);
            assertEquals(10, result[0]);
            assertEquals(30, result[2]);
        }

        @Test
        void mapToInt_1D_long_null() {
            long[] arr = null;
            int[] result = Arrays.mapToInt(arr, l -> (int) l);
            assertEquals(0, result.length);
        }

        @Test
        void mapToInt_2D_double_normal() {
            double[][] arr = { { 10.5, 20.5 }, { 30.5, 40.5 } };
            int[][] result = Arrays.mapToInt(arr, d -> (int) d);
            assertNotNull(result);
            assertEquals(10, result[0][0]);
            assertEquals(40, result[1][1]);
        }

        @Test
        void mapToInt_2D_long_normal() {
            long[][] arr = { { 1000L, 2000L }, { 3000L, 4000L } };
            int[][] result = Arrays.mapToInt(arr, l -> (int) (l / 100));
            assertNotNull(result);
            assertEquals(10, result[0][0]);
            assertEquals(40, result[1][1]);
        }

        @Test
        void mapToInt_3D_double_normal() {
            double[][][] arr = { { { 100.8, 200.2 } } };
            int[][][] result = Arrays.mapToInt(arr, d -> (int) Math.ceil(d));
            assertNotNull(result);
            assertEquals(101, result[0][0][0]);
        }

        @Test
        void mapToInt_3D_long_normal() {
            long[][][] arr = { { { 500L, 600L } } };
            int[][][] result = Arrays.mapToInt(arr, l -> (int) l - 100);
            assertNotNull(result);
            assertEquals(400, result[0][0][0]);
        }

        @Test
        void mapToInt_double_1D() {
            double[] arr = { 1.5, 2.7, 3.9 };
            int[] result = Arrays.mapToInt(arr, d -> (int) Math.round(d));
            assertArrayEquals(new int[] { 2, 3, 4 }, result);
        }

        @Test
        void mapToInt_double_2D_preservesShape() {
            final double[][] a = { { 1.1, 2.2 }, { 3.3 } };
            final int[][] r = Arrays.mapToInt(a, d -> (int) (d * 10));
            assertEquals(2, r.length);
            assertArrayEquals(new int[] { 11, 22 }, r[0]);
            assertArrayEquals(new int[] { 33 }, r[1]);
        }

        @Test
        void mapToInt_double_truncation() {
            final int[] r = Arrays.mapToInt(new double[] { 1.7, -2.3, 3.999 }, d -> (int) d);
            assertArrayEquals(new int[] { 1, -2, 3 }, r);
        }

        @Test
        void mapToInt_double2DArray() {
            // Test with normal two-dimensional array
            double[][] input = { { 1.5, 2.5 }, { 3.5 } };
            int[][] result = Arrays.mapToInt(input, (double d) -> (int) Math.round(d));
            assertArrayEquals(new int[] { 2, 3 }, result[0]);
            assertArrayEquals(new int[] { 4 }, result[1]);

            // Test with null two-dimensional array
            double[][] nullInput = null;
            int[][] nullResult = Arrays.mapToInt(nullInput, (double d) -> (int) d);
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToInt_double3DArray() {
            // Test with normal three-dimensional array
            double[][][] input = { { { 1.1, 2.9 } }, { { 3.5 } } };
            int[][][] result = Arrays.mapToInt(input, (double d) -> (int) Math.round(d));
            assertArrayEquals(new int[] { 1, 3 }, result[0][0]);
            assertArrayEquals(new int[] { 4 }, result[1][0]);

            // Test with null three-dimensional array
            double[][][] nullInput = null;
            int[][][] nullResult = Arrays.mapToInt(nullInput, (double d) -> (int) d);
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToInt_doubleArray() {
            // Test with normal array - rounding
            double[] input = { 1.7, 2.3, 3.9 };
            int[] result = Arrays.mapToInt(input, d -> (int) Math.round(d));
            assertArrayEquals(new int[] { 2, 2, 4 }, result);

            // Test with truncation
            double[] truncInput = { 1.9, 2.1, 3.7 };
            int[] truncResult = Arrays.mapToInt(truncInput, d -> (int) d);
            assertArrayEquals(new int[] { 1, 2, 3 }, truncResult);

            // Test with empty array
            double[] emptyInput = {};
            int[] emptyResult = Arrays.mapToInt(emptyInput, d -> (int) d);
            assertArrayEquals(new int[] {}, emptyResult);

            // Test with null array
            double[] nullInput = null;
            int[] nullResult = Arrays.mapToInt(nullInput, d -> (int) d);
            assertEquals(0, nullResult.length);

            // Test with negative values
            double[] negativeInput = { 1.7, 2.3, 3.9 };
            int[] negativeResult = Arrays.mapToInt(negativeInput, d -> (int) Math.round(d));
            assertArrayEquals(new int[] { 2, 2, 4 }, negativeResult);
        }

        @Test
        void mapToInt_doubleArray_values_1_9_2_9_3_9() {
            double[] a = { 1.9, 2.9, 3.9 };
            int[] result = Arrays.mapToInt(a, d -> (int) d);

            assertArrayEquals(new int[] { 1, 2, 3 }, result);
        }

        @Test
        void mapToInt_doubleArrayNull() {
            int[] result = Arrays.mapToInt((double[]) null, d -> 0);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToInt_doubleToInt() {
            // mapToInt({1.7,2.3,3.9}, d -> (int)Math.round(d)) => {2,2,4}
            double[] doubles = { 1.7, 2.3, 3.9 };
            int[] ints = Arrays.mapToInt(doubles, d -> (int) Math.round(d));
            assertArrayEquals(new int[] { 2, 2, 4 }, ints);
        }

        @Test
        void mapToInt_doubleToInt2D() {
            // mapToInt({{1.5,2.7},{3.2,4.9}}, d -> (int)Math.round(d)) => {{2,3},{3,5}}
            double[][] array2D = { { 1.5, 2.7 }, { 3.2, 4.9 } };
            int[][] result = Arrays.mapToInt(array2D, d -> (int) Math.round(d));
            assertArrayEquals(new int[] { 2, 3 }, result[0]);
            assertArrayEquals(new int[] { 3, 5 }, result[1]);
        }

        @Test
        void mapToInt_doubleToInt3D() {
            // mapToInt(cube, d -> (int)Math.ceil(d)) => {{{2,3},{4,5}},{{6,7},{8,9}}}
            double[][][] cube = { { { 1.5, 2.7 }, { 3.2, 4.9 } }, { { 5.1, 6.8 }, { 7.3, 8.6 } } };
            int[][][] result = Arrays.mapToInt(cube, d -> (int) Math.ceil(d));
            assertArrayEquals(new int[] { 2, 3 }, result[0][0]);
            assertArrayEquals(new int[] { 4, 5 }, result[0][1]);
            assertArrayEquals(new int[] { 6, 7 }, result[1][0]);
            assertArrayEquals(new int[] { 8, 9 }, result[1][1]);
        }

        @Test
        void mapToInt_emptyArray() throws Exception {
            String[] input = new String[0];
            int[] result = f.mapToInt(input, s -> 0);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToInt_long_1D() {
            long[] arr = { 1L, 2L, 3L };
            int[] result = Arrays.mapToInt(arr, l -> (int) (l * 2));
            assertArrayEquals(new int[] { 2, 4, 6 }, result);
        }

        @Test
        void mapToInt_long_2D_nullSubArrayBecomesEmpty() {
            final long[][] a = { { 1L, 2L }, null, { 3L } };
            final int[][] r = Arrays.mapToInt(a, l -> (int) l);
            assertEquals(3, r.length);
            assertArrayEquals(new int[] { 1, 2 }, r[0]);
            assertNotNull(r[1]);
            assertEquals(0, r[1].length);
            assertArrayEquals(new int[] { 3 }, r[2]);
        }

        @Test
        void mapToInt_long_3D_preservesShape() {
            final long[][][] a = { { { 1L, 2L } }, { { 3L } } };
            final int[][][] r = Arrays.mapToInt(a, l -> (int) (l * 10));
            assertEquals(2, r.length);
            assertArrayEquals(new int[] { 10, 20 }, r[0][0]);
            assertArrayEquals(new int[] { 30 }, r[1][0]);
        }

        @Test
        void mapToInt_long_extremeValues() {
            final int[] r = Arrays.mapToInt(new long[] { 1L, 2L, Long.MAX_VALUE }, l -> (int) (l & 0xFFFF));
            assertArrayEquals(new int[] { 1, 2, 0xFFFF }, r);
        }

        @Test
        void mapToInt_long_nullInput_emptyIntArray() {
            final int[] r = Arrays.mapToInt((long[]) null, l -> (int) l);
            assertNotNull(r);
            assertEquals(0, r.length);
        }

        @Test
        void mapToInt_long2DArray() {
            // Test with normal two-dimensional array
            long[][] input = { { 100L, 200L }, { 300L } };
            int[][] result = Arrays.mapToInt(input, (long l) -> (int) (l / 100));
            assertArrayEquals(new int[] { 1, 2 }, result[0]);
            assertArrayEquals(new int[] { 3 }, result[1]);

            // Test with null two-dimensional array
            long[][] nullInput = null;
            int[][] nullResult = Arrays.mapToInt(nullInput, (long l) -> (int) (l / 100));
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToInt_long3DArray() {
            // Test with normal three-dimensional array
            long[][][] input = { { { 1000L, 2000L } } };
            int[][][] result = Arrays.mapToInt(input, (long l) -> (int) (l / 1000));
            assertArrayEquals(new int[] { 1, 2 }, result[0][0]);

            // Test with null three-dimensional array
            long[][][] nullInput = null;
            int[][][] nullResult = Arrays.mapToInt(nullInput, (long l) -> (int) (l / 1000));
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToInt_longArray() {
            // Test with normal array
            long[] input = { 1000L, 2000L, 3000L };
            int[] result = Arrays.mapToInt(input, l -> (int) (l / 1000));
            assertArrayEquals(new int[] { 1, 2, 3 }, result);

            // Test with empty array
            long[] emptyInput = {};
            int[] emptyResult = Arrays.mapToInt(emptyInput, l -> (int) (l / 1000));
            assertArrayEquals(new int[] {}, emptyResult);

            // Test with null array
            long[] nullInput = null;
            int[] nullResult = Arrays.mapToInt(nullInput, l -> (int) (l / 1000));
            assertEquals(0, nullResult.length);

            // Test with overflow scenario
            long[] overflowInput = { Long.MAX_VALUE, 0L, Long.MIN_VALUE };
            int[] overflowResult = Arrays.mapToInt(overflowInput, l -> (int) (l));
            assertEquals(-1, overflowResult[0]); // overflow
            assertEquals(0, overflowResult[1]);
            assertEquals(0, overflowResult[2]); // overflow
        }

        @Test
        void mapToInt_longArray_values_100L_200L_300L() {
            long[] a = { 100L, 200L, 300L };
            int[] result = Arrays.mapToInt(a, l -> (int) (l / 10));

            assertArrayEquals(new int[] { 10, 20, 30 }, result);
        }

        @Test
        void mapToInt_longArray_values_1000L_2000L_3000L() {
            long[] arr = { 1000L, 2000L, 3000L };
            int[] result = Arrays.mapToInt(arr, l -> (int) (l / 1000));

            assertArrayEquals(new int[] { 1, 2, 3 }, result);
        }

        @Test
        void mapToInt_longArrayNull() {
            int[] result = Arrays.mapToInt((long[]) null, l -> 0);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToInt_longToInt() {
            // mapToInt({1000L,2000L,3000L}, l -> (int)(l/1000)) => {1,2,3}
            long[] longs = { 1000L, 2000L, 3000L };
            int[] ints = Arrays.mapToInt(longs, l -> (int) (l / 1000));
            assertArrayEquals(new int[] { 1, 2, 3 }, ints);
        }

        @Test
        void mapToInt_nullArray() throws Exception {
            String[] input = null;
            int[] result = f.mapToInt(input, s -> 0);
            assertEquals(0, result.length);
        }

        @Test
        void mapToInt_stringToInt() throws Exception {
            String[] input = { "10", "20", "30" };
            int[] result = f.mapToInt(input, Integer::parseInt);
            assertArrayEquals(new int[] { 10, 20, 30 }, result);
        }

        @Test
        void mapToInt_values_1_2_3_4() throws Exception {
            // Test String to int parsing
            String[][][] stringNumbers = { { { "1", "2" } }, { { "3", "4" } } };
            int[][][] integers = fff.mapToInt(stringNumbers, Integer::parseInt);
            assertEquals(1, integers[0][0][0]);
            assertEquals(2, integers[0][0][1]);
            assertEquals(3, integers[1][0][0]);
            assertEquals(4, integers[1][0][1]);

            // Test null input
            int[][][] nullResult = fff.mapToInt(null, s -> 0);
            assertEquals(0, nullResult.length);

            // Test Double to int conversion
            Double[][][] doubles = { { { 1.5, 2.7 } }, { { 3.1, 4.9 } } };
            int[][][] ints = fff.mapToInt(doubles, Double::intValue);
            assertEquals(1, ints[0][0][0]);
            assertEquals(2, ints[0][0][1]);
            assertEquals(3, ints[1][0][0]);
            assertEquals(4, ints[1][0][1]);
        }
    }

    @Nested
    class MapToLongTests {

        @Test
        void f_mapToLong_null() {
            String[] arr = null;
            long[] result = Arrays.f.mapToLong(arr, s -> (long) s.length());
            assertEquals(0, result.length);
        }

        @Test
        void f_mapToLong_valid() {
            String[] arr = { "abc", "defg" };
            long[] result = Arrays.f.mapToLong(arr, s -> (long) s.length());
            assertArrayEquals(new long[] { 3L, 4L }, result);
        }

        @Test
        void fF_mapToLong() {
            String[][] arr = { { "abc", "defg" } };
            long[][] result = Arrays.ff.mapToLong(arr, s -> (long) s.length());
            assertArrayEquals(new long[] { 3L, 4L }, result[0]);
        }

        @Test
        void fFF_mapToLong() {
            String[][][] arr = { { { "abc", "defg" } } };
            long[][][] result = Arrays.fff.mapToLong(arr, s -> (long) s.length());
            assertArrayEquals(new long[] { 3L, 4L }, result[0][0]);
        }

        @Test
        void mapToLong() throws Exception {
            // Test String to long
            String[][] array = { { "1000000", "2000000" }, { "3000000", "4000000" } };
            long[][] longs = ff.mapToLong(array, Long::parseLong);
            assertArrayEquals(new long[] { 1000000L, 2000000L }, longs[0]);
            assertArrayEquals(new long[] { 3000000L, 4000000L }, longs[1]);

            // Test null array
            String[][] nullArray = null;
            assertEquals(0, ff.mapToLong(nullArray, Long::parseLong).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "100", "200" }, null, { "300", "400" } };
            long[][] mapped = ff.mapToLong(arrayWithNulls, Long::parseLong);
            assertArrayEquals(new long[] { 100L, 200L }, mapped[0]);
            assertEquals(0, mapped[1].length);
            assertArrayEquals(new long[] { 300L, 400L }, mapped[2]);

            // Test Integer to long
            Integer[][] intArray = { { 100, 200 }, { 300, 400 } };
            long[][] longValues = ff.mapToLong(intArray, Integer::longValue);
            assertArrayEquals(new long[] { 100L, 200L }, longValues[0]);
            assertArrayEquals(new long[] { 300L, 400L }, longValues[1]);
        }

        @Test
        void mapToLong_1D_double_normal() {
            double[] arr = { 1.5, 2.7, 3.9 };
            long[] result = Arrays.mapToLong(arr, d -> Math.round(d * 100));
            assertNotNull(result);
            assertEquals(150L, result[0]);
            assertEquals(390L, result[2]);
        }

        @Test
        void mapToLong_1D_double_null() {
            double[] arr = null;
            long[] result = Arrays.mapToLong(arr, d -> (long) d);
            assertEquals(0, result.length);
        }

        @Test
        void mapToLong_1D_int_normal() {
            int[] arr = { 1, 2, 3, 4, 5 };
            long[] result = Arrays.mapToLong(arr, i -> (long) i * 100);
            assertNotNull(result);
            assertEquals(5, result.length);
            assertEquals(100L, result[0]);
            assertEquals(500L, result[4]);
        }

        @Test
        void mapToLong_1D_int_null() {
            int[] arr = null;
            long[] result = Arrays.mapToLong(arr, i -> (long) i);
            assertEquals(0, result.length);
        }

        @Test
        void mapToLong_2D_double_normal() {
            double[][] arr = { { 10.5, 20.5 }, { 30.5, 40.5 } };
            long[][] result = Arrays.mapToLong(arr, d -> Math.round(d));
            assertNotNull(result);
            assertEquals(11L, result[0][0]);
            assertEquals(41L, result[1][1]);
        }

        @Test
        void mapToLong_2D_int_normal() {
            int[][] arr = { { 1, 2 }, { 3, 4 } };
            long[][] result = Arrays.mapToLong(arr, i -> (long) i * 1000);
            assertNotNull(result);
            assertEquals(1000L, result[0][0]);
            assertEquals(4000L, result[1][1]);
        }

        @Test
        void mapToLong_3D_double_normal() {
            double[][][] arr = { { { 99.9, 199.9 } } };
            long[][][] result = Arrays.mapToLong(arr, d -> (long) Math.floor(d));
            assertNotNull(result);
            assertEquals(99L, result[0][0][0]);
        }

        @Test
        void mapToLong_3D_int_normal() {
            int[][][] arr = { { { 10, 20 } } };
            long[][][] result = Arrays.mapToLong(arr, i -> (long) i + 5);
            assertNotNull(result);
            assertEquals(15L, result[0][0][0]);
        }

        @Test
        void mapToLong_double_1D() {
            double[] arr = { 1.5, 2.7, 3.9 };
            long[] result = Arrays.mapToLong(arr, d -> Math.round(d));
            assertArrayEquals(new long[] { 2L, 3L, 4L }, result);
        }

        @Test
        void mapToLong_double_3D() {
            final double[][][] a = { { { 1.5, 2.5 } }, { { 3.5 } } };
            final long[][][] r = Arrays.mapToLong(a, d -> (long) d);
            assertEquals(2, r.length);
            assertArrayEquals(new long[] { 1L, 2L }, r[0][0]);
            assertArrayEquals(new long[] { 3L }, r[1][0]);
        }

        @Test
        void mapToLong_double_truncation() {
            final long[] r = Arrays.mapToLong(new double[] { 1.7, -2.3, 1e10 }, d -> (long) d);
            assertArrayEquals(new long[] { 1L, -2L, 10_000_000_000L }, r);
        }

        @Test
        void mapToLong_double2DArray() {
            // Test with normal two-dimensional array
            double[][] input = { { 1.1, 2.2 }, { 3.3 } };
            long[][] result = Arrays.mapToLong(input, (double d) -> Math.round(d));
            assertArrayEquals(new long[] { 1L, 2L }, result[0]);
            assertArrayEquals(new long[] { 3L }, result[1]);

            // Test with null two-dimensional array
            double[][] nullInput = null;
            long[][] nullResult = Arrays.mapToLong(nullInput, (double d) -> Math.round(d));
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToLong_double3DArray() {
            // Test with normal three-dimensional array
            double[][][] input = { { { 10.5, 20.5 } }, { { 30.5 } } };
            long[][][] result = Arrays.mapToLong(input, (double d) -> Math.round(d));
            assertArrayEquals(new long[] { 11L, 21L }, result[0][0]);
            assertArrayEquals(new long[] { 31L }, result[1][0]);

            // Test with null three-dimensional array
            double[][][] nullInput = null;
            long[][][] nullResult = Arrays.mapToLong(nullInput, (double d) -> Math.round(d));
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToLong_doubleArray() {
            // Test with normal array
            double[] input = { 1.5, 2.7, 3.2 };
            long[] result = Arrays.mapToLong(input, d -> Math.round(d));
            assertArrayEquals(new long[] { 2L, 3L, 3L }, result);

            // Test with empty array
            double[] emptyInput = {};
            long[] emptyResult = Arrays.mapToLong(emptyInput, d -> Math.round(d));
            assertArrayEquals(new long[] {}, emptyResult);

            // Test with null array
            double[] nullInput = null;
            long[] nullResult = Arrays.mapToLong(nullInput, d -> Math.round(d));
            assertEquals(0, nullResult.length);

            // Test with large values
            double[] largeInput = { 1e10, 2e10, 3e10 };
            long[] largeResult = Arrays.mapToLong(largeInput, d -> (long) d);
            assertArrayEquals(new long[] { 10000000000L, 20000000000L, 30000000000L }, largeResult);
        }

        @Test
        void mapToLong_doubleArray_values_1_5_2_5_3_5() {
            double[] a = { 1.5, 2.5, 3.5 };
            long[] result = Arrays.mapToLong(a, d -> (long) (d * 10));

            assertArrayEquals(new long[] { 15L, 25L, 35L }, result);
        }

        @Test
        void mapToLong_doubleToLong() {
            // mapToLong({10.50,20.75,30.25}, d -> (long)(d*100)) => {1050,2075,3025}
            double[] prices = { 10.50, 20.75, 30.25 };
            long[] cents = Arrays.mapToLong(prices, d -> (long) (d * 100));
            assertArrayEquals(new long[] { 1050, 2075, 3025 }, cents);
        }

        @Test
        void mapToLong_doubleToLong2D() {
            // mapToLong({{10.50,20.75},{30.25,40.50}}, d -> (long)(d*100))
            double[][] prices = { { 10.50, 20.75 }, { 30.25, 40.50 } };
            long[][] cents = Arrays.mapToLong(prices, d -> (long) (d * 100));
            assertArrayEquals(new long[] { 1050, 2075 }, cents[0]);
            assertArrayEquals(new long[] { 3025, 4050 }, cents[1]);
        }

        @Test
        void mapToLong_doubleToLong3D() {
            // mapToLong(cube, d -> (long)(d*1000))
            double[][][] cube = { { { 1.5, 2.7 }, { 3.2, 4.9 } }, { { 5.1, 6.8 }, { 7.3, 8.6 } } };
            long[][][] result = Arrays.mapToLong(cube, d -> (long) (d * 1000));
            assertArrayEquals(new long[] { 1500, 2700 }, result[0][0]);
            assertArrayEquals(new long[] { 3200, 4900 }, result[0][1]);
            assertArrayEquals(new long[] { 5100, 6800 }, result[1][0]);
            assertArrayEquals(new long[] { 7300, 8600 }, result[1][1]);
        }

        @Test
        void mapToLong_emptyArray() throws Exception {
            String[] input = new String[0];
            long[] result = f.mapToLong(input, s -> 0L);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToLong_errorHandling_exceptionInMapper() {
            int[] input = { 1, 2, 3 };
            Throwables.IntToLongFunction<RuntimeException> faultyMapper = i -> {
                if (i == 2) {
                    throw new RuntimeException("Test exception");
                }
                return i * 1000L;
            };

            assertThrows(RuntimeException.class, () -> {
                Arrays.mapToLong(input, faultyMapper);
            });
        }

        @Test
        void mapToLong_int_1D_null() {
            int[] arr = null;
            long[] result = Arrays.mapToLong(arr, i -> i * 2L);
            assertEquals(0, result.length);
        }

        @Test
        void mapToLong_int_1D_valid() {
            int[] arr = { 1, 2, 3 };
            long[] result = Arrays.mapToLong(arr, i -> i * 10L);
            assertArrayEquals(new long[] { 10L, 20L, 30L }, result);
        }

        @Test
        void mapToLong_int_2D_valid() {
            int[][] arr = { { 1, 2 }, { 3, 4 } };
            long[][] result = Arrays.mapToLong(arr, i -> i * 100L);
            assertEquals(2, result.length);
            assertArrayEquals(new long[] { 100L, 200L }, result[0]);
            assertArrayEquals(new long[] { 300L, 400L }, result[1]);
        }

        @Test
        void mapToLong_int_2D_widening() {
            final int[][] a = { { 1, 2 }, { 3 } };
            final long[][] r = Arrays.mapToLong(a, i -> i * 1_000_000_000L);
            assertEquals(2, r.length);
            assertArrayEquals(new long[] { 1_000_000_000L, 2_000_000_000L }, r[0]);
            assertArrayEquals(new long[] { 3_000_000_000L }, r[1]);
        }

        @Test
        void mapToLong_int_3D_valid() {
            int[][][] arr = { { { 1, 2 } } };
            long[][][] result = Arrays.mapToLong(arr, i -> i * 5L);
            assertEquals(5L, result[0][0][0]);
            assertEquals(10L, result[0][0][1]);
        }

        @Test
        void mapToLong_int_nullInput_empty() {
            final long[] r = Arrays.mapToLong((int[]) null, i -> (long) i);
            assertNotNull(r);
            assertEquals(0, r.length);
        }

        @Test
        void mapToLong_int_widening() {
            final long[] r = Arrays.mapToLong(new int[] { 1, 2, 3 }, i -> i * 1_000_000_000L);
            assertArrayEquals(new long[] { 1_000_000_000L, 2_000_000_000L, 3_000_000_000L }, r);
        }

        @Test
        void mapToLong_int2D() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            long[][] result = Arrays.mapToLong(a, i -> (long) i * 10);

            assertEquals(2, result.length);
            assertEquals(10L, result[0][0]);
            assertEquals(40L, result[1][1]);
        }

        @Test
        void mapToLong_int2DArray() {
            // Test with normal two-dimensional array
            int[][] input = { { 1, 2 }, { 3, 4 }, { 5 } };
            long[][] result = Arrays.mapToLong(input, (int i) -> i * 100L);
            assertArrayEquals(new long[] { 100L, 200L }, result[0]);
            assertArrayEquals(new long[] { 300L, 400L }, result[1]);
            assertArrayEquals(new long[] { 500L }, result[2]);

            // Test with empty two-dimensional array
            int[][] emptyInput = {};
            long[][] emptyResult = Arrays.mapToLong(emptyInput, (int i) -> i * 100L);
            assertEquals(0, emptyResult.length);

            // Test with null two-dimensional array
            int[][] nullInput = null;
            long[][] nullResult = Arrays.mapToLong(nullInput, (int i) -> i * 100L);
            assertEquals(0, nullResult.length);

            // Test with jagged array containing empty arrays
            int[][] jaggedInput = { { 1, 2 }, {}, { 3 } };
            long[][] jaggedResult = Arrays.mapToLong(jaggedInput, (int i) -> i * 10L);
            assertArrayEquals(new long[] { 10L, 20L }, jaggedResult[0]);
            assertArrayEquals(new long[] {}, jaggedResult[1]);
            assertArrayEquals(new long[] { 30L }, jaggedResult[2]);
        }

        @Test
        void mapToLong_int3D() {
            int[][][] a = { { { 1, 2 } } };
            long[][][] result = Arrays.mapToLong(a, i -> (long) i * 100);

            assertEquals(100L, result[0][0][0]);
        }

        @Test
        void mapToLong_int3DArray() {
            // Test with normal three-dimensional array
            int[][][] input = { { { 1, 2 }, { 3 } }, { { 4, 5, 6 } } };
            long[][][] result = Arrays.mapToLong(input, (int i) -> i * 10L);
            assertArrayEquals(new long[] { 10L, 20L }, result[0][0]);
            assertArrayEquals(new long[] { 30L }, result[0][1]);
            assertArrayEquals(new long[] { 40L, 50L, 60L }, result[1][0]);

            // Test with null three-dimensional array
            int[][][] nullInput = null;
            long[][][] nullResult = Arrays.mapToLong(nullInput, (int i) -> i * 10L);
            assertEquals(0, nullResult.length);

            // Test with empty three-dimensional array
            int[][][] emptyInput = {};
            long[][][] emptyResult = Arrays.mapToLong(emptyInput, (int i) -> i * 10L);
            assertEquals(0, emptyResult.length);
        }

        @Test
        void mapToLong_intArray() {
            // Test with normal array
            int[] input = { 1, 2, 3, 4, 5 };
            long[] result = Arrays.mapToLong(input, i -> i * 1000L);
            assertArrayEquals(new long[] { 1000L, 2000L, 3000L, 4000L, 5000L }, result);

            // Test with empty array
            int[] emptyInput = {};
            long[] emptyResult = Arrays.mapToLong(emptyInput, i -> i * 1000L);
            assertArrayEquals(new long[] {}, emptyResult);

            // Test with null array
            int[] nullInput = null;
            long[] nullResult = Arrays.mapToLong(nullInput, i -> i * 1000L);
            assertEquals(0, nullResult.length);

            // Test with single element
            int[] singleInput = { 42 };
            long[] singleResult = Arrays.mapToLong(singleInput, i -> i * 100L);
            assertArrayEquals(new long[] { 4200L }, singleResult);

            // Test with negative values
            int[] negativeInput = { -1, -2, -3 };
            long[] negativeResult = Arrays.mapToLong(negativeInput, i -> i * 1000L);
            assertArrayEquals(new long[] { -1000L, -2000L, -3000L }, negativeResult);
        }

        @Test
        void mapToLong_intArray_values_1_2_3() {
            int[] arr = { 1, 2, 3 };
            long[] result = Arrays.mapToLong(arr, i -> i * 1000L);

            assertArrayEquals(new long[] { 1000L, 2000L, 3000L }, result);
        }

        @Test
        void mapToLong_intArray_values_1_2_3_4_5() {
            int[] a = { 1, 2, 3, 4, 5 };
            long[] result = Arrays.mapToLong(a, i -> (long) i * 2);

            assertArrayEquals(new long[] { 2L, 4L, 6L, 8L, 10L }, result);
        }

        @Test
        void mapToLong_intArray_withException() {
            int[] input = { 1, 2, 3 };
            Throwables.IntToLongFunction<Exception> mapper = i -> {
                if (i == 2) {
                    throw new Exception("Test exception");
                }
                return i * 1000L;
            };

            assertThrows(Exception.class, () -> Arrays.mapToLong(input, mapper));
        }

        @Test
        void mapToLong_intArray2D() {
            int[][] arr = { { 1, 2 }, { 3, 4 } };
            long[][] result = Arrays.mapToLong(arr, i -> i * 1000L);

            assertEquals(2, result.length);
            assertArrayEquals(new long[] { 1000L, 2000L }, result[0]);
            assertArrayEquals(new long[] { 3000L, 4000L }, result[1]);
        }

        @Test
        void mapToLong_intArray2DNull() {
            long[][] result = Arrays.mapToLong((int[][]) null, i -> 0L);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToLong_intArrayEmpty() {
            int[] arr = {};
            long[] result = Arrays.mapToLong(arr, i -> i * 1000L);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToLong_intArrayNull() {
            long[] result = Arrays.mapToLong((int[]) null, i -> 0L);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToLong_intToLong() {
            // mapToLong({1,2,3}, i -> i * 1000000000L) => {1000000000L,...}
            int[] ints = { 1, 2, 3 };
            long[] longs = Arrays.mapToLong(ints, i -> i * 1000000000L);
            assertArrayEquals(new long[] { 1000000000L, 2000000000L, 3000000000L }, longs);
        }

        @Test
        void mapToLong_intToLong3D() {
            // mapToLong(cube, i -> (long)i*i) => {{{1L,4L},{9L,16L}},{{25L,36L},{49L,64L}}}
            int[][][] cube = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } } };
            long[][][] result = Arrays.mapToLong(cube, i -> (long) i * i);
            assertArrayEquals(new long[] { 1L, 4L }, result[0][0]);
            assertArrayEquals(new long[] { 9L, 16L }, result[0][1]);
            assertArrayEquals(new long[] { 25L, 36L }, result[1][0]);
            assertArrayEquals(new long[] { 49L, 64L }, result[1][1]);
        }

        @Test
        void mapToLong_nullArray() throws Exception {
            String[] input = null;
            long[] result = f.mapToLong(input, s -> 0L);
            assertEquals(0, result.length);
        }

        @Test
        void mapToLong_stringToLong() throws Exception {
            String[] input = { "100", "200", "300" };
            long[] result = f.mapToLong(input, Long::parseLong);
            assertArrayEquals(new long[] { 100L, 200L, 300L }, result);
        }

        @Test
        void mapToLong_values_1000000_2000000() throws Exception {
            // Test String to long parsing
            String[][][] timestamps = { { { "1000000" } }, { { "2000000" } } };
            long[][][] longs = fff.mapToLong(timestamps, Long::parseLong);
            assertEquals(1000000L, longs[0][0][0]);
            assertEquals(2000000L, longs[1][0][0]);

            // Test null input
            long[][][] nullResult = fff.mapToLong(null, s -> 0L);
            assertEquals(0, nullResult.length);

            // Test Integer to long conversion
            Integer[][][] numbers = { { { 100, 200 } }, { { 300, 400 } } };
            long[][][] longNumbers = fff.mapToLong(numbers, Integer::longValue);
            assertEquals(100L, longNumbers[0][0][0]);
            assertEquals(200L, longNumbers[0][0][1]);
            assertEquals(300L, longNumbers[1][0][0]);
            assertEquals(400L, longNumbers[1][0][1]);
        }

        @Test
        void multidimensionalArrays_jaggedArrays() {
            // Test with jagged arrays - arrays with different sub-array lengths
            int[][] jaggedArray = { { 1, 2, 3 }, { 4 }, { 5, 6 } };
            long[][] jaggedLongResult = Arrays.mapToLong(jaggedArray, i -> (long) i * 10);

            assertEquals(3, jaggedLongResult.length);
            assertArrayEquals(new long[] { 10, 20, 30 }, jaggedLongResult[0]);
            assertArrayEquals(new long[] { 40 }, jaggedLongResult[1]);
            assertArrayEquals(new long[] { 50, 60 }, jaggedLongResult[2]);
        }
    }

    @Nested
    class MapToFloatTests {

        @Test
        void f_mapToFloat_null() {
            String[] arr = null;
            float[] result = Arrays.f.mapToFloat(arr, s -> (float) s.length());
            assertEquals(0, result.length);
        }

        @Test
        void f_mapToFloat_valid() {
            String[] arr = { "ab", "abc" };
            float[] result = Arrays.f.mapToFloat(arr, s -> (float) s.length());
            assertArrayEquals(new float[] { 2.0f, 3.0f }, result, 0.001f);
        }

        @Test
        void fF_mapToFloat() {
            String[][] arr = { { "ab", "abc" } };
            float[][] result = Arrays.ff.mapToFloat(arr, s -> (float) s.length());
            assertArrayEquals(new float[] { 2.0f, 3.0f }, result[0], 0.001f);
        }

        @Test
        void fFF_mapToFloat() {
            String[][][] arr = { { { "ab", "abc" } } };
            float[][][] result = Arrays.fff.mapToFloat(arr, s -> (float) s.length());
            assertArrayEquals(new float[] { 2.0f, 3.0f }, result[0][0], 0.001f);
        }

        @Test
        void mapToFloat() throws Exception {
            // Test String to float
            String[][] array = { { "1.5", "2.5" }, { "3.5", "4.5" } };
            float[][] floats = ff.mapToFloat(array, Float::parseFloat);
            assertArrayEquals(new float[] { 1.5f, 2.5f }, floats[0]);
            assertArrayEquals(new float[] { 3.5f, 4.5f }, floats[1]);

            // Test null array
            String[][] nullArray = null;
            assertEquals(0, ff.mapToFloat(nullArray, Float::parseFloat).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1.1", "2.2" }, null, { "3.3", "4.4" } };
            float[][] mapped = ff.mapToFloat(arrayWithNulls, Float::parseFloat);
            assertArrayEquals(new float[] { 1.1f, 2.2f }, mapped[0]);
            assertEquals(0, mapped[1].length);
            assertArrayEquals(new float[] { 3.3f, 4.4f }, mapped[2]);

            // Test Integer to float
            Integer[][] intArray = { { 10, 20 }, { 30, 40 } };
            float[][] floatValues = ff.mapToFloat(intArray, Integer::floatValue);
            assertArrayEquals(new float[] { 10.0f, 20.0f }, floatValues[0]);
            assertArrayEquals(new float[] { 30.0f, 40.0f }, floatValues[1]);
        }

        @Test
        void mapToFloat_emptyArray() throws Exception {
            String[] input = new String[0];
            float[] result = f.mapToFloat(input, s -> 0.0f);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToFloat_nullArray() throws Exception {
            String[] input = null;
            float[] result = f.mapToFloat(input, s -> 0.0f);
            assertEquals(0, result.length);
        }

        @Test
        void mapToFloat_stringToFloat() throws Exception {
            String[] input = { "1.5", "2.5", "3.5" };
            float[] result = f.mapToFloat(input, Float::parseFloat);
            assertArrayEquals(new float[] { 1.5f, 2.5f, 3.5f }, result);
        }

        @Test
        void mapToFloat_values_1_5_2_7_3_14_4_2() throws Exception {
            // Test String to float parsing
            String[][][] decimals = { { { "1.5", "2.7" } }, { { "3.14", "4.2" } } };
            float[][][] floats = fff.mapToFloat(decimals, Float::parseFloat);
            assertEquals(1.5f, floats[0][0][0], 0.001f);
            assertEquals(2.7f, floats[0][0][1], 0.001f);
            assertEquals(3.14f, floats[1][0][0], 0.001f);
            assertEquals(4.2f, floats[1][0][1], 0.001f);

            // Test null input
            float[][][] nullResult = fff.mapToFloat(null, s -> 0.0f);
            assertEquals(0, nullResult.length);

            // Test Integer to float conversion
            Integer[][][] numbers = { { { 10, 20 } }, { { 30, 40 } } };
            float[][][] floatNumbers = fff.mapToFloat(numbers, Integer::floatValue);
            assertEquals(10.0f, floatNumbers[0][0][0], 0.001f);
            assertEquals(20.0f, floatNumbers[0][0][1], 0.001f);
            assertEquals(30.0f, floatNumbers[1][0][0], 0.001f);
            assertEquals(40.0f, floatNumbers[1][0][1], 0.001f);
        }
    }

    @Nested
    class MapToDoubleTests {

        @Test
        void f_mapToDouble_null() {
            String[] arr = null;
            double[] result = Arrays.f.mapToDouble(arr, s -> (double) s.length());
            assertEquals(0, result.length);
        }

        @Test
        void f_mapToDouble_valid() {
            String[] arr = { "x", "xyz" };
            double[] result = Arrays.f.mapToDouble(arr, s -> (double) s.length());
            assertArrayEquals(new double[] { 1.0, 3.0 }, result, 0.001);
        }

        @Test
        void fF_mapToDouble() {
            String[][] arr = { { "x", "xyz" } };
            double[][] result = Arrays.ff.mapToDouble(arr, s -> (double) s.length());
            assertArrayEquals(new double[] { 1.0, 3.0 }, result[0], 0.001);
        }

        @Test
        void fFF_mapToDouble() {
            String[][][] arr = { { { "x", "xyz" } } };
            double[][][] result = Arrays.fff.mapToDouble(arr, s -> (double) s.length());
            assertArrayEquals(new double[] { 1.0, 3.0 }, result[0][0], 0.001);
        }

        @Test
        void mapToDouble() throws Exception {
            // Test String to double
            String[][] array = { { "1.234", "2.345" }, { "3.456", "4.567" } };
            double[][] doubles = ff.mapToDouble(array, Double::parseDouble);
            assertArrayEquals(new double[] { 1.234, 2.345 }, doubles[0]);
            assertArrayEquals(new double[] { 3.456, 4.567 }, doubles[1]);

            // Test null array
            String[][] nullArray = null;
            assertEquals(0, ff.mapToDouble(nullArray, Double::parseDouble).length);

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1.1", "2.2" }, null, { "3.3", "4.4" } };
            double[][] mapped = ff.mapToDouble(arrayWithNulls, Double::parseDouble);
            assertArrayEquals(new double[] { 1.1, 2.2 }, mapped[0]);
            assertEquals(0, mapped[1].length);
            assertArrayEquals(new double[] { 3.3, 4.4 }, mapped[2]);

            // Test Integer to double
            Integer[][] intArray = { { 10, 20 }, { 30, 40 } };
            double[][] doubleValues = ff.mapToDouble(intArray, Integer::doubleValue);
            assertArrayEquals(new double[] { 10.0, 20.0 }, doubleValues[0]);
            assertArrayEquals(new double[] { 30.0, 40.0 }, doubleValues[1]);
        }

        @Test
        void mapToDouble_1D_int_normal() {
            int[] arr = { 1, 2, 3, 4 };
            double[] result = Arrays.mapToDouble(arr, i -> i * 0.5);
            assertNotNull(result);
            assertEquals(4, result.length);
            assertEquals(0.5, result[0], 0.001);
            assertEquals(2.0, result[3], 0.001);
        }

        @Test
        void mapToDouble_1D_int_null() {
            int[] arr = null;
            double[] result = Arrays.mapToDouble(arr, i -> (double) i);
            assertEquals(0, result.length);
        }

        @Test
        void mapToDouble_1D_long_normal() {
            long[] arr = { 1000L, 2000L, 3000L };
            double[] result = Arrays.mapToDouble(arr, l -> l / 100.0);
            assertNotNull(result);
            assertEquals(10.0, result[0], 0.001);
            assertEquals(30.0, result[2], 0.001);
        }

        @Test
        void mapToDouble_2D_int_normal() {
            int[][] arr = { { 10, 20 }, { 30, 40 } };
            double[][] result = Arrays.mapToDouble(arr, i -> i / 10.0);
            assertNotNull(result);
            assertEquals(1.0, result[0][0], 0.001);
            assertEquals(4.0, result[1][1], 0.001);
        }

        @Test
        void mapToDouble_2D_long_normal() {
            long[][] arr = { { 100L, 200L }, { 300L, 400L } };
            double[][] result = Arrays.mapToDouble(arr, l -> l * 1.5);
            assertNotNull(result);
            assertEquals(150.0, result[0][0], 0.001);
        }

        @Test
        void mapToDouble_3D_int_normal() {
            int[][][] arr = { { { 100, 200 } } };
            double[][][] result = Arrays.mapToDouble(arr, i -> i * 1.5);
            assertNotNull(result);
            assertEquals(150.0, result[0][0][0], 0.001);
        }

        @Test
        void mapToDouble_3D_long_normal() {
            long[][][] arr = { { { 50L, 75L } } };
            double[][][] result = Arrays.mapToDouble(arr, l -> l + 0.5);
            assertNotNull(result);
            assertEquals(50.5, result[0][0][0], 0.001);
        }

        @Test
        void mapToDouble_emptyArray() throws Exception {
            String[] input = new String[0];
            double[] result = f.mapToDouble(input, s -> 0.0);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToDouble_errorHandling_exceptionInMapper() {
            long[] input = { 1L, 2L, 3L };
            Throwables.LongToDoubleFunction<IllegalArgumentException> faultyMapper = l -> {
                if (l == 2L) {
                    throw new IllegalArgumentException("Invalid value");
                }
                return l * 0.5;
            };

            assertThrows(IllegalArgumentException.class, () -> {
                Arrays.mapToDouble(input, faultyMapper);
            });
        }

        @Test
        void mapToDouble_int_1D_null() {
            int[] arr = null;
            double[] result = Arrays.mapToDouble(arr, i -> i * 1.5);
            assertEquals(0, result.length);
        }

        @Test
        void mapToDouble_int_1D_valid() {
            int[] arr = { 1, 2, 3 };
            double[] result = Arrays.mapToDouble(arr, i -> i * 1.5);
            assertArrayEquals(new double[] { 1.5, 3.0, 4.5 }, result, 0.001);
        }

        @Test
        void mapToDouble_int_2D_valid() {
            int[][] arr = { { 2, 4 }, { 6, 8 } };
            double[][] result = Arrays.mapToDouble(arr, i -> i / 2.0);
            assertEquals(1.0, result[0][0], 0.001);
            assertEquals(4.0, result[1][1], 0.001);
        }

        @Test
        void mapToDouble_int_3D_valid() {
            int[][][] arr = { { { 10 } } };
            double[][][] result = Arrays.mapToDouble(arr, i -> i * 0.1);
            assertEquals(1.0, result[0][0][0], 0.001);
        }

        @Test
        void mapToDouble_int_fractional() {
            final double[] r = Arrays.mapToDouble(new int[] { 1, 2, 3 }, i -> i / 2.0);
            assertArrayEquals(new double[] { 0.5, 1.0, 1.5 }, r, 0.0);
        }

        @Test
        void mapToDouble_int_mapperThrows_propagates() {
            assertThrows(RuntimeException.class, () -> Arrays.mapToDouble(new int[] { 1, 2 }, i -> {
                throw new RuntimeException("boom");
            }));
        }

        @Test
        void mapToDouble_int_nullInput_empty() {
            final double[] r = Arrays.mapToDouble((int[]) null, i -> (double) i);
            assertNotNull(r);
            assertEquals(0, r.length);
        }

        @Test
        void mapToDouble_int2D() {
            int[][] a = { { 1, 2 }, { 3, 4 } };
            double[][] result = Arrays.mapToDouble(a, i -> i * 0.5);

            assertEquals(2, result.length);
            assertEquals(0.5, result[0][0], 0.0001);
            assertEquals(2.0, result[1][1], 0.0001);
        }

        @Test
        void mapToDouble_int2DArray() {
            // Test with normal two-dimensional array
            int[][] input = { { 1, 2 }, { 3, 4 } };
            double[][] result = Arrays.mapToDouble(input, (int i) -> i * 0.5);
            assertArrayEquals(new double[] { 0.5, 1.0 }, result[0]);
            assertArrayEquals(new double[] { 1.5, 2.0 }, result[1]);

            // Test with null two-dimensional array
            int[][] nullInput = null;
            double[][] nullResult = Arrays.mapToDouble(nullInput, (int i) -> i * 0.5);
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToDouble_int3D() {
            int[][][] a = { { { 10, 20 } } };
            double[][][] result = Arrays.mapToDouble(a, i -> i * 0.1);

            assertEquals(1.0, result[0][0][0], 0.0001);
            assertEquals(2.0, result[0][0][1], 0.0001);
        }

        @Test
        void mapToDouble_int3DArray() {
            // Test with normal three-dimensional array
            int[][][] input = { { { 1, 2 } }, { { 3, 4 }, { 5, 6 } } };
            double[][][] result = Arrays.mapToDouble(input, (int i) -> i * 0.1);
            assertArrayEquals(new double[] { 0.1, 0.2 }, result[0][0], 0.000001);
            assertArrayEquals(new double[] { 0.3, 0.4 }, result[1][0], 0.000001);
            assertArrayEquals(new double[] { 0.5, 0.6 }, result[1][1], 0.000001);

            // Test with null three-dimensional array
            int[][][] nullInput = null;
            double[][][] nullResult = Arrays.mapToDouble(nullInput, (int i) -> i * 0.1);
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToDouble_intArray() {
            // Test with normal array
            int[] input = { 1, 2, 3, 4 };
            double[] result = Arrays.mapToDouble(input, i -> i / 2.0);
            assertArrayEquals(new double[] { 0.5, 1.0, 1.5, 2.0 }, result);

            // Test with empty array
            int[] emptyInput = {};
            double[] emptyResult = Arrays.mapToDouble(emptyInput, i -> i / 2.0);
            assertArrayEquals(new double[] {}, emptyResult);

            // Test with null array
            int[] nullInput = null;
            double[] nullResult = Arrays.mapToDouble(nullInput, i -> i / 2.0);
            assertEquals(0, nullResult.length);

            // Test with mathematical operations
            int[] mathInput = { 10, 20, 30 };
            double[] mathResult = Arrays.mapToDouble(mathInput, i -> Math.sqrt(i));
            assertEquals(Math.sqrt(10), mathResult[0], 0.0001);
            assertEquals(Math.sqrt(20), mathResult[1], 0.0001);
            assertEquals(Math.sqrt(30), mathResult[2], 0.0001);
        }

        @Test
        void mapToDouble_intArray_preservesExactProducts() {
            int[] arr = { 1, 2, 3 };
            double[] result = Arrays.mapToDouble(arr, i -> i * 1.5);

            assertArrayEquals(new double[] { 1.5, 3.0, 4.5 }, result);
        }

        @Test
        void mapToDouble_intArrayNull() {
            double[] result = Arrays.mapToDouble((int[]) null, i -> 0.0);
            assertNotNull(result);
            assertEquals(0, result.length);
        }

        @Test
        void mapToDouble_intToDouble() {
            // mapToDouble({1,2,3}, i -> i/2.0) => {0.5, 1.0, 1.5}
            int[] ints = { 1, 2, 3 };
            double[] doubles = Arrays.mapToDouble(ints, i -> i / 2.0);
            assertArrayEquals(new double[] { 0.5, 1.0, 1.5 }, doubles);
        }

        @Test
        void mapToDouble_long_1D() {
            long[] arr = { 1L, 2L, 3L };
            double[] result = Arrays.mapToDouble(arr, l -> l * 1.5);
            assertArrayEquals(new double[] { 1.5, 3.0, 4.5 }, result, 0.001);
        }

        @Test
        void mapToDouble_long_2D() {
            final long[][] a = { { 100L, 200L }, { 300L } };
            final double[][] r = Arrays.mapToDouble(a, l -> l / 100.0);
            assertEquals(2, r.length);
            assertArrayEquals(new double[] { 1.0, 2.0 }, r[0], 0.0);
            assertArrayEquals(new double[] { 3.0 }, r[1], 0.0);
        }

        @Test
        void mapToDouble_long_widening() {
            final double[] r = Arrays.mapToDouble(new long[] { 100L, 200L }, l -> l / 100.0);
            assertArrayEquals(new double[] { 1.0, 2.0 }, r, 0.0);
        }

        @Test
        void mapToDouble_long2DArray() {
            // Test with normal two-dimensional array
            long[][] input = { { 10L, 20L }, { 30L } };
            double[][] result = Arrays.mapToDouble(input, (long l) -> l * 0.1);
            assertArrayEquals(new double[] { 1.0, 2.0 }, result[0]);
            assertArrayEquals(new double[] { 3.0 }, result[1]);

            // Test with null two-dimensional array
            long[][] nullInput = null;
            double[][] nullResult = Arrays.mapToDouble(nullInput, (long l) -> l * 0.1);
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToDouble_long3DArray() {
            // Test with normal three-dimensional array
            long[][][] input = { { { 100L, 200L } }, { { 300L } } };
            double[][][] result = Arrays.mapToDouble(input, (long l) -> l * 0.01d);
            assertArrayEquals(new double[] { 1.0, 2.0 }, result[0][0]);
            assertArrayEquals(new double[] { 3.0 }, result[1][0]);

            // Test with null three-dimensional array
            long[][][] nullInput = null;
            double[][][] nullResult = Arrays.mapToDouble(nullInput, (long l) -> l * 0.01d);
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToDouble_longArray() {
            // Test with normal array
            long[] input = { 1L, 2L, 3L };
            double[] result = Arrays.mapToDouble(input, l -> l * 0.5);
            assertArrayEquals(new double[] { 0.5, 1.0, 1.5 }, result);

            // Test with empty array
            long[] emptyInput = {};
            double[] emptyResult = Arrays.mapToDouble(emptyInput, l -> l * 0.5);
            assertArrayEquals(new double[] {}, emptyResult);

            // Test with null array
            long[] nullInput = null;
            double[] nullResult = Arrays.mapToDouble(nullInput, l -> l * 0.5);
            assertEquals(0, nullResult.length);
        }

        @Test
        void mapToDouble_longArray_values_10L_20L_30L() {
            long[] a = { 10L, 20L, 30L };
            double[] result = Arrays.mapToDouble(a, l -> l * 0.5);

            assertArrayEquals(new double[] { 5.0, 10.0, 15.0 }, result, 0.0001);
        }

        @Test
        void mapToDouble_longToDouble() {
            // mapToDouble({100L,200L,300L}, l -> l/100.0) => {1.0,2.0,3.0}
            long[] values = { 100L, 200L, 300L };
            double[] result = Arrays.mapToDouble(values, l -> l / 100.0);
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, result);
        }

        @Test
        void mapToDouble_nullArray() throws Exception {
            String[] input = null;
            double[] result = f.mapToDouble(input, s -> 0.0);
            assertEquals(0, result.length);
        }

        @Test
        void mapToDouble_stringToDouble() throws Exception {
            String[] input = { "1.1", "2.2", "3.3" };
            double[] result = f.mapToDouble(input, Double::parseDouble);
            assertArrayEquals(new double[] { 1.1, 2.2, 3.3 }, result);
        }

        @Test
        void mapToDouble_values_1_23e10_4_56eminus5_7_89e15() throws Exception {
            // Test String to double parsing with scientific notation
            String[][][] scientificData = { { { "1.23e10", "4.56e-5" } }, { { "7.89e15" } } };
            double[][][] doubles = fff.mapToDouble(scientificData, Double::parseDouble);
            assertEquals(1.23e10, doubles[0][0][0], 0.001);
            assertEquals(4.56e-5, doubles[0][0][1], 0.00001);
            assertEquals(7.89e15, doubles[1][0][0], 0.001);

            // Test null input
            double[][][] nullResult = fff.mapToDouble(null, s -> 0.0);
            assertEquals(0, nullResult.length);

            // Test Integer to double conversion
            Integer[][][] numbers = { { { 100, 200 } }, { { 300, 400 } } };
            double[][][] doubleNumbers = fff.mapToDouble(numbers, Integer::doubleValue);
            assertEquals(100.0, doubleNumbers[0][0][0], 0.001);
            assertEquals(200.0, doubleNumbers[0][0][1], 0.001);
            assertEquals(300.0, doubleNumbers[1][0][0], 0.001);
            assertEquals(400.0, doubleNumbers[1][0][1], 0.001);
        }
    }

    @Nested
    class MappingContractTests {

        @Test
        void arrayOperations_largeArrays() {
            // Test with moderately large arrays to check performance and correctness
            int size = 1000;
            int[] largeArray = new int[size];
            for (int i = 0; i < size; i++) {
                largeArray[i] = i;
            }

            // Test mapToLong with large array
            long[] longResult = Arrays.mapToLong(largeArray, i -> (long) i * i);
            assertEquals(size, longResult.length);
            assertEquals(0L, longResult[0]);
            assertEquals(998001L, longResult[999]); // 999^2

            // Test mapToDouble with large array
            double[] doubleResult = Arrays.mapToDouble(largeArray, i -> Math.sqrt(i));
            assertEquals(size, doubleResult.length);
            assertEquals(0.0, doubleResult[0], 0.001);
            assertEquals(Math.sqrt(999), doubleResult[999], 0.001);
        }

        @Test
        void chainedOperations() {
            // Test chaining multiple array operations
            int[] original = { 1, 2, 3, 4, 5 };

            // Chain: int -> long -> double -> String
            long[] longChain = Arrays.mapToLong(original, i -> (long) i * i);
            double[] doubleChain = Arrays.mapToDouble(longChain, l -> l / 2.0);
            String[] stringChain = Arrays.mapToObj(doubleChain, d -> String.format(java.util.Locale.ROOT, "%.1f", d), String.class);

            assertArrayEquals(new String[] { "0.5", "2.0", "4.5", "8.0", "12.5" }, stringChain);
        }

        @Test
        void typeConversion_boundaryValues() {
            // Test byte boundary values
            Integer[] byteInput = { 127, -128, 0 };
            byte[] byteResult = f.mapToByte(byteInput, i -> i.byteValue());
            assertArrayEquals(new byte[] { 127, -128, 0 }, byteResult);

            // Test short boundary values
            Integer[] shortInput = { 32767, -32768, 0 };
            short[] shortResult = f.mapToShort(shortInput, i -> i.shortValue());
            assertArrayEquals(new short[] { 32767, -32768, 0 }, shortResult);

            // Test char boundary values
            Integer[] charInput = { 0, 65535 }; // Character range
            char[] charResult = f.mapToChar(charInput, i -> (char) i.intValue());
            assertArrayEquals(new char[] { 0, 65535 }, charResult);
        }

        @Test
        void typeConversion_overflow() {
            // Test int to byte overflow
            Integer[] overflowInput = { 128, 256, -129 };
            byte[] overflowResult = f.mapToByte(overflowInput, i -> i.byteValue());
            // Java wraps around on overflow
            assertArrayEquals(new byte[] { -128, 0, 127 }, overflowResult);

            // Test int to short overflow
            Integer[] shortOverflowInput = { 32768, -32769 };
            short[] shortOverflowResult = f.mapToShort(shortOverflowInput, i -> i.shortValue());
            assertArrayEquals(new short[] { -32768, 32767 }, shortOverflowResult);
        }
    }
}
