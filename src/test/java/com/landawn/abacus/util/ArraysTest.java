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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.util.Arrays.ff;
import com.landawn.abacus.util.Arrays.fff;
import com.landawn.abacus.util.stream.Stream;

class ArraysTest {

    @Test
    public void test_minSubArrayLen() {
        final String[][] a = { { "a", "b" }, { "c", "d", "d" } };
        final String[][][] b = { { { "a", "b" } }, { { "1", "2" }, { "3", "4" } } };
        final int[][][] c = Arrays.reshape(Array.rangeClosed(1, 9), 2, 3);

        assertEquals(2, ff.minSubArrayLen(a));
        assertEquals(3, ff.maxSubArrayLen(a));
        assertEquals(1, ff.minSubArrayLen(b));
        assertEquals(2, ff.maxSubArrayLen(b));
        assertEquals(1, ff.minSubArrayLen(c));
        assertEquals(2, ff.maxSubArrayLen(c));

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
        }
        {
            final int[][][] a = Arrays.reshape(Array.rangeClosed(1, 9), 2, 3);
            final int[][][] b = Arrays.reshape(Array.repeat(0, 9), 3, 2);
            final int[][][] c = Arrays.zip(a, b, -1, -1, (p1, p2) -> p1 + p2);
            Arrays.println(c);
        }

        {

            Integer[][][] a = { { { 1 } } };
            Integer[][][] b = { { { 10, 20 } } };
            Integer[][][] c = { { {}, { 100 } } };
            Integer[][][] result = fff.zip(a, b, c, -1, -1, -1, (x, y, z) -> x + y + z);

            fff.println(result);
            // result is {{{10, 18}}, {{-3, 98}}}
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
        }
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
            // Test with normal 2D arrays
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
            // Test with null 2D arrays
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
            // Test with 2D arrays of different lengths using defaults
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
            // Test with normal 2D arrays
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
            // Test with 2D arrays of different lengths using defaults
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
            // Test with normal 3D arrays
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
            // Test with null 3D arrays
            long[][][] result = Arrays.zip((long[][][]) null, (long[][][]) null, (x, y) -> x + y);
            Assertions.assertEquals(0, result.length);
        }

        // Test zip(long[][][], long[][][], long, long, LongBiFunction)
        @Test
        public void testZip3DArraysWithDefaults() throws Exception {
            // Test with 3D arrays of different lengths using defaults
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
            // Test with normal 3D arrays
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
            // Test with 3D arrays of different lengths using defaults
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
            Integer[][] allNulls = { null, null, null };
            Integer[] flattened5 = ff.flatten(allNulls);
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
            ff.flatOp(array, arr -> java.util.Arrays.sort(arr));
            Assertions.assertArrayEquals(new Integer[] { 1, 1, 3 }, array[0]);
            Assertions.assertArrayEquals(new Integer[] { 4, 5, 9 }, array[1]);

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 5, 3 }, null, { 1, 4 } };
            ff.flatOp(arrayWithNulls, arr -> java.util.Arrays.sort(arr));
            Assertions.assertArrayEquals(new Integer[] { 1, 3 }, arrayWithNulls[0]);
            Assertions.assertNull(arrayWithNulls[1]);
            Assertions.assertArrayEquals(new Integer[] { 4, 5 }, arrayWithNulls[2]);

            // Test with empty sub-arrays
            Integer[][] arrayWithEmpty = { { 5, 3 }, {}, { 1, 4 } };
            ff.flatOp(arrayWithEmpty, arr -> java.util.Arrays.sort(arr));
            Assertions.assertArrayEquals(new Integer[] { 1, 3 }, arrayWithEmpty[0]);
            Assertions.assertArrayEquals(new Integer[] {}, arrayWithEmpty[1]);
            Assertions.assertArrayEquals(new Integer[] { 4, 5 }, arrayWithEmpty[2]);

            // Test with empty array
            Integer[][] emptyArray = {};
            ff.flatOp(emptyArray, arr -> java.util.Arrays.sort(arr)); // Should not throw

            // Test with null array
            Integer[][] nullArray = null;
            ff.flatOp(nullArray, arr -> java.util.Arrays.sort(arr)); // Should not throw

            // Test with custom operation
            Integer[][] array2 = { { 1, 2 }, { 3, 4 } };
            ff.flatOp(array2, arr -> {
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
            Assertions.assertNull(ff.map(nullArray, x -> x * 2));

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            Integer[][] mapped = ff.map(arrayWithNulls, x -> x + 10);
            Assertions.assertArrayEquals(new Integer[] { 11, 12 }, mapped[0]);
            Assertions.assertNull(mapped[1]);
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
            Assertions.assertNull(ff.map(nullArray, Integer::parseInt, Integer.class));

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1", "2" }, null, { "3", "4" } };
            Integer[][] mapped = ff.map(arrayWithNulls, Integer::parseInt, Integer.class);
            Assertions.assertArrayEquals(new Integer[] { 1, 2 }, mapped[0]);
            Assertions.assertNull(mapped[1]);
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
            Assertions.assertNull(ff.mapToBoolean(nullArray, x -> x % 2 == 0));

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            boolean[][] mapped = ff.mapToBoolean(arrayWithNulls, x -> x > 2);
            Assertions.assertArrayEquals(new boolean[] { false, false }, mapped[0]);
            Assertions.assertNull(mapped[1]);
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
            Assertions.assertNull(ff.mapToChar(nullArray, s -> s.charAt(0)));

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "abc", "def" }, null, { "ghi", "jkl" } };
            char[][] mapped = ff.mapToChar(arrayWithNulls, s -> s.charAt(1));
            Assertions.assertArrayEquals(new char[] { 'b', 'e' }, mapped[0]);
            Assertions.assertNull(mapped[1]);
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
            Assertions.assertNull(ff.mapToByte(nullArray, Integer::byteValue));

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 1, 2 }, null, { 3, 4 } };
            byte[][] mapped = ff.mapToByte(arrayWithNulls, Integer::byteValue);
            Assertions.assertArrayEquals(new byte[] { 1, 2 }, mapped[0]);
            Assertions.assertNull(mapped[1]);
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
            Assertions.assertNull(ff.mapToShort(nullArray, Integer::shortValue));

            // Test with null sub-arrays
            Integer[][] arrayWithNulls = { { 10, 20 }, null, { 30, 40 } };
            short[][] mapped = ff.mapToShort(arrayWithNulls, Integer::shortValue);
            Assertions.assertArrayEquals(new short[] { 10, 20 }, mapped[0]);
            Assertions.assertNull(mapped[1]);
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
            Assertions.assertNull(ff.mapToInt(nullArray, Integer::parseInt));

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1", "2" }, null, { "3", "4" } };
            int[][] mapped = ff.mapToInt(arrayWithNulls, Integer::parseInt);
            Assertions.assertArrayEquals(new int[] { 1, 2 }, mapped[0]);
            Assertions.assertNull(mapped[1]);
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
            Assertions.assertNull(ff.mapToLong(nullArray, Long::parseLong));

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "100", "200" }, null, { "300", "400" } };
            long[][] mapped = ff.mapToLong(arrayWithNulls, Long::parseLong);
            Assertions.assertArrayEquals(new long[] { 100L, 200L }, mapped[0]);
            Assertions.assertNull(mapped[1]);
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
            Assertions.assertNull(ff.mapToFloat(nullArray, Float::parseFloat));

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1.1", "2.2" }, null, { "3.3", "4.4" } };
            float[][] mapped = ff.mapToFloat(arrayWithNulls, Float::parseFloat);
            Assertions.assertArrayEquals(new float[] { 1.1f, 2.2f }, mapped[0]);
            Assertions.assertNull(mapped[1]);
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
            Assertions.assertNull(ff.mapToDouble(nullArray, Double::parseDouble));

            // Test with null sub-arrays
            String[][] arrayWithNulls = { { "1.1", "2.2" }, null, { "3.3", "4.4" } };
            double[][] mapped = ff.mapToDouble(arrayWithNulls, Double::parseDouble);
            Assertions.assertArrayEquals(new double[] { 1.1, 2.2 }, mapped[0]);
            Assertions.assertNull(mapped[1]);
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
            long total = ff.totalCountOfElements(array);
            Assertions.assertEquals(6, total);

            // Test empty array
            Object[][] emptyArray = {};
            Assertions.assertEquals(0, ff.totalCountOfElements(emptyArray));

            // Test null array
            Object[][] nullArray = null;
            Assertions.assertEquals(0, ff.totalCountOfElements(nullArray));

            // Test array with all nulls
            Object[][] allNulls = { null, null, null };
            Assertions.assertEquals(0, ff.totalCountOfElements(allNulls));

            // Test array with empty sub-arrays
            Object[][] withEmpty = { {}, { 1, 2 }, {}, { 3, 4, 5 } };
            Assertions.assertEquals(5, ff.totalCountOfElements(withEmpty));

            // Test large array
            Object[][] largeArray = new Object[1000][];
            for (int i = 0; i < 1000; i++) {
                largeArray[i] = new Object[10];
            }
            Assertions.assertEquals(10000, ff.totalCountOfElements(largeArray));
        }

        @Test
        public void testMinSubArrayLen() {
            // Test normal array
            Object[][] array = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
            int minLen = ff.minSubArrayLen(array);
            Assertions.assertEquals(0, minLen); // null counts as 0

            // Test without null
            Object[][] array2 = { { 1, 2, 3 }, { 4, 5 }, { 6 } };
            int minLen2 = ff.minSubArrayLen(array2);
            Assertions.assertEquals(1, minLen2);

            // Test empty array
            Object[][] emptyArray = {};
            Assertions.assertEquals(0, ff.minSubArrayLen(emptyArray));

            // Test null array
            Object[][] nullArray = null;
            Assertions.assertEquals(0, ff.minSubArrayLen(nullArray));

            // Test with empty sub-arrays
            Object[][] withEmpty = { {}, { 1, 2 }, { 3, 4, 5 } };
            Assertions.assertEquals(0, ff.minSubArrayLen(withEmpty));

            // Test uniform length
            Object[][] uniform = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            Assertions.assertEquals(3, ff.minSubArrayLen(uniform));
        }

        @Test
        public void testMaxSubArrayLen() {
            // Test normal array
            Object[][] array = { { 1, 2, 3 }, { 4, 5 }, null, { 6 } };
            int maxLen = ff.maxSubArrayLen(array);
            Assertions.assertEquals(3, maxLen);

            // Test empty array
            Object[][] emptyArray = {};
            Assertions.assertEquals(0, ff.maxSubArrayLen(emptyArray));

            // Test null array
            Object[][] nullArray = null;
            Assertions.assertEquals(0, ff.maxSubArrayLen(nullArray));

            // Test with all nulls
            Object[][] allNulls = { null, null, null };
            Assertions.assertEquals(0, ff.maxSubArrayLen(allNulls));

            // Test with empty sub-arrays
            Object[][] withEmpty = { {}, { 1, 2 }, { 3, 4, 5, 6, 7 } };
            Assertions.assertEquals(5, ff.maxSubArrayLen(withEmpty));

            // Test uniform length
            Object[][] uniform = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
            Assertions.assertEquals(2, ff.maxSubArrayLen(uniform));
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
            Assertions.assertThrows(NullPointerException.class, () -> {
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
            fff.flatOp(arr, flat -> java.util.Arrays.sort(flat));
            Assertions.assertEquals(1, arr[0][0][0]);
            Assertions.assertEquals(2, arr[0][0][1]);
            Assertions.assertEquals(3, arr[1][0][0]);
            Assertions.assertEquals(5, arr[1][0][1]);
            Assertions.assertEquals(7, arr[2][0][0]);
            Assertions.assertEquals(9, arr[2][0][1]);

            // Test reversing elements
            Integer[][][] arr2 = { { { 1, 2 } }, { { 3, 4 } } };
            fff.flatOp(arr2, flat -> {
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
            fff.flatOp(emptyArr, flat -> java.util.Arrays.sort(flat));
            Assertions.assertEquals(0, emptyArr.length);

            // Test with null and empty sub-arrays
            Integer[][][] mixedArr = { { { 1, 2 } }, null, { {} }, { { 3, 4 } } };
            fff.flatOp(mixedArr, flat -> {
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
            Integer[][][] nullResult = fff.map(null, n -> n * 2);
            Assertions.assertNull(nullResult);

            // Test with null sub-arrays
            Integer[][][] mixedArr = { { { 1, 2 } }, null, { { 3, 4 } } };
            Integer[][][] mappedMixed = fff.map(mixedArr, n -> n + 10);
            Assertions.assertEquals(11, mappedMixed[0][0][0]);
            Assertions.assertEquals(12, mappedMixed[0][0][1]);
            Assertions.assertNull(mappedMixed[1]);
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
            Assertions.assertNull(nullResult);

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
            Assertions.assertNull(mixedStrs[1]);
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
            Assertions.assertNull(nullResult);

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
            Assertions.assertNull(nullResult);

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
            Assertions.assertNull(nullResult);

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
            Assertions.assertNull(nullResult);

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
            Assertions.assertNull(nullResult);

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
            Assertions.assertNull(nullResult);

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
            Assertions.assertNull(nullResult);

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
            Assertions.assertNull(nullResult);

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
            Integer[][][] a = {{{1}}};
            Integer[][][] b = {{{10, 20}}};
            Integer[][][] c = { { {}, { 100 } } };
            Integer[][][] result = fff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
            Arrays.println(result);
            Assertions.assertEquals(1, result.length);
            Assertions.assertEquals(11, result[0][0][0]);
            Assertions.assertEquals(20, result[0][0][1]);
            Assertions.assertEquals(100, result[0][1][0]);

            // Test with all arrays of different sizes
            Integer[][][] a2 = {{{1, 2}}, {{3}}};
            Integer[][][] b2 = {{{10}}};
            Integer[][][] c2 = {{{100, 200, 300}}};
            Integer[][][] sum = fff.zip(a2, b2, c2, -1, -10, -100, 
                (x, y, z) -> x + y + z);
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
            String[][][] combined = fff.zip(ints, strs, bools, -1, "?", false, (i, s, f) -> String.format("[%d,%s,%s]", i, s, f ? "T" : "F"),
                    String.class);
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
            long count = fff.totalCountOfElements(array);
            Assertions.assertEquals(6, count);

            // Test with empty array
            Object[][][] emptyArray = new Object[0][][];
            long emptyCount = fff.totalCountOfElements(emptyArray);
            Assertions.assertEquals(0, emptyCount);

            // Test with all null/empty elements
            Object[][][] nullArray = { null, { null, {} }, { {} } };
            long nullCount = fff.totalCountOfElements(nullArray);
            Assertions.assertEquals(0, nullCount);

            // Test with mixed elements
            Object[][][] mixedArray = { { { 1, 2, 3 } }, { {} }, { { 4 } }, null, { { 5, 6, 7, 8 } } };
            long mixedCount = fff.totalCountOfElements(mixedArray);
            Assertions.assertEquals(8, mixedCount);

            // Test with jagged array
            Object[][][] jaggedArray = { { { 1 }, { 2, 3 }, { 4, 5, 6 } }, { { 7, 8 } }, { { 9, 10, 11, 12 } } };
            long jaggedCount = fff.totalCountOfElements(jaggedArray);
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

            // Test totalCountOfElements
            long count = fff.totalCountOfElements(largeArr);
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

}
