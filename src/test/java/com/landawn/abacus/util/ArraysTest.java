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

import com.landawn.abacus.util.Arrays.f;
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

        // Test cases for 2D boolean array zip methods

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

        // Test cases for 3D boolean array zip methods

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
        public void testZipTwoCharArraysDifferentLengths() throws Exception {
            // Test with first array longer
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y', 'Z' };
            char[] result = Arrays.zip(a, b, (x, y) -> (char) (x + y - 'A'));
            // result: {'X', 'Z', '\'}
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

        // Tests for zip(char[], char[], char, char, CharBiFunction)

        @Test
        public void testZipTwoCharArraysWithDefaults() throws Exception {
            // Test with first array longer
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y' };
            char[] result = Arrays.zip(a, b, '?', '!', (x, y) -> (char) (x + y - 'A'));
            Assertions.assertArrayEquals(new char[] { 'X', 'Z', '#', '$' }, result);
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

        // Tests for zip(char[], char[], char[], char, char, char, CharTriFunction)

        @Test
        public void testZipThreeCharArraysWithDefaults() throws Exception {
            // Test with arrays of different lengths
            char[] a = { 'A', 'B', 'C', 'D' };
            char[] b = { 'X', 'Y' };
            char[] c = { '1', '2', '3' };
            char[] result = Arrays.zip(a, b, c, '?', '!', '0', (x, y, z) -> (char) (x + y + z - 'A' - '0'));
            // result: {'Y', '\\', '&', '$'} (using defaults for missing elements)
            Assertions.assertArrayEquals(new char[] { 'Y', '\\', '&', '$' }, result);
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
    public class FTest {
        // Tests for map(T[], Function, Class)
        @Test
        public void testMap_NullArray() throws Exception {
            String[] input = null;
            Integer[] result = f.map(input, Integer::valueOf, Integer.class);
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(result);
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
            Assertions.assertNull(nullResult);

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
            // Test with normal 2D array
            int[][] input = { { 1, 2 }, { 3, 4 }, { 5 } };
            long[][] result = Arrays.mapToLong(input, (int i) -> i * 100L);
            Assertions.assertArrayEquals(new long[] { 100L, 200L }, result[0]);
            Assertions.assertArrayEquals(new long[] { 300L, 400L }, result[1]);
            Assertions.assertArrayEquals(new long[] { 500L }, result[2]);

            // Test with empty 2D array
            int[][] emptyInput = {};
            long[][] emptyResult = Arrays.mapToLong(emptyInput, (int i) -> i * 100L);
            Assertions.assertEquals(0, emptyResult.length);

            // Test with null 2D array
            int[][] nullInput = null;
            long[][] nullResult = Arrays.mapToLong(nullInput, (int i) -> i * 100L);
            Assertions.assertNull(nullResult);

            // Test with jagged array containing empty arrays
            int[][] jaggedInput = { { 1, 2 }, {}, { 3 } };
            long[][] jaggedResult = Arrays.mapToLong(jaggedInput, (int i) -> i * 10L);
            Assertions.assertArrayEquals(new long[] { 10L, 20L }, jaggedResult[0]);
            Assertions.assertArrayEquals(new long[] {}, jaggedResult[1]);
            Assertions.assertArrayEquals(new long[] { 30L }, jaggedResult[2]);
        }

        @Test
        public void testMapToLong_Int3DArray() {
            // Test with normal 3D array
            int[][][] input = { { { 1, 2 }, { 3 } }, { { 4, 5, 6 } } };
            long[][][] result = Arrays.mapToLong(input, (int i) -> i * 10L);
            Assertions.assertArrayEquals(new long[] { 10L, 20L }, result[0][0]);
            Assertions.assertArrayEquals(new long[] { 30L }, result[0][1]);
            Assertions.assertArrayEquals(new long[] { 40L, 50L, 60L }, result[1][0]);

            // Test with null 3D array
            int[][][] nullInput = null;
            long[][][] nullResult = Arrays.mapToLong(nullInput, (int i) -> i * 10L);
            Assertions.assertNull(nullResult);

            // Test with empty 3D array
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
            Assertions.assertNull(nullResult);

            // Test with mathematical operations
            int[] mathInput = { 10, 20, 30 };
            double[] mathResult = Arrays.mapToDouble(mathInput, i -> Math.sqrt(i));
            Assertions.assertEquals(Math.sqrt(10), mathResult[0], 0.0001);
            Assertions.assertEquals(Math.sqrt(20), mathResult[1], 0.0001);
            Assertions.assertEquals(Math.sqrt(30), mathResult[2], 0.0001);
        }

        @Test
        public void testMapToDouble_Int2DArray() {
            // Test with normal 2D array
            int[][] input = { { 1, 2 }, { 3, 4 } };
            double[][] result = Arrays.mapToDouble(input, (int i) -> i * 0.5);
            Assertions.assertArrayEquals(new double[] { 0.5, 1.0 }, result[0]);
            Assertions.assertArrayEquals(new double[] { 1.5, 2.0 }, result[1]);

            // Test with null 2D array
            int[][] nullInput = null;
            double[][] nullResult = Arrays.mapToDouble(nullInput, (int i) -> i * 0.5);
            Assertions.assertNull(nullResult);
        }

        @Test
        public void testMapToDouble_Int3DArray() {
            // Test with normal 3D array
            int[][][] input = { { { 1, 2 } }, { { 3, 4 }, { 5, 6 } } };
            double[][][] result = Arrays.mapToDouble(input, (int i) -> i * 0.1);
            Assertions.assertArrayEquals(new double[] { 0.1, 0.2 }, result[0][0], 0.000001);
            Assertions.assertArrayEquals(new double[] { 0.3, 0.4 }, result[1][0], 0.000001);
            Assertions.assertArrayEquals(new double[] { 0.5, 0.6 }, result[1][1], 0.000001);

            // Test with null 3D array
            int[][][] nullInput = null;
            double[][][] nullResult = Arrays.mapToDouble(nullInput, (int i) -> i * 0.1);
            Assertions.assertNull(nullResult);
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
            Assertions.assertNull(nullResult);

            // Test with overflow scenario
            long[] overflowInput = { Long.MAX_VALUE, 0L, Long.MIN_VALUE };
            int[] overflowResult = Arrays.mapToInt(overflowInput, l -> (int) (l));
            Assertions.assertEquals(-1, overflowResult[0]); // overflow
            Assertions.assertEquals(0, overflowResult[1]);
            Assertions.assertEquals(0, overflowResult[2]); // overflow
        }

        @Test
        public void testMapToInt_Long2DArray() {
            // Test with normal 2D array
            long[][] input = { { 100L, 200L }, { 300L } };
            int[][] result = Arrays.mapToInt(input, (long l) -> (int) (l / 100));
            Assertions.assertArrayEquals(new int[] { 1, 2 }, result[0]);
            Assertions.assertArrayEquals(new int[] { 3 }, result[1]);

            // Test with null 2D array
            long[][] nullInput = null;
            int[][] nullResult = Arrays.mapToInt(nullInput, (long l) -> (int) (l / 100));
            Assertions.assertNull(nullResult);
        }

        @Test
        public void testMapToInt_Long3DArray() {
            // Test with normal 3D array
            long[][][] input = { { { 1000L, 2000L } } };
            int[][][] result = Arrays.mapToInt(input, (long l) -> (int) (l / 1000));
            Assertions.assertArrayEquals(new int[] { 1, 2 }, result[0][0]);

            // Test with null 3D array
            long[][][] nullInput = null;
            int[][][] nullResult = Arrays.mapToInt(nullInput, (long l) -> (int) (l / 1000));
            Assertions.assertNull(nullResult);
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
            Assertions.assertNull(nullResult);
        }

        @Test
        public void testMapToDouble_Long2DArray() {
            // Test with normal 2D array
            long[][] input = { { 10L, 20L }, { 30L } };
            double[][] result = Arrays.mapToDouble(input, (long l) -> l * 0.1);
            Assertions.assertArrayEquals(new double[] { 1.0, 2.0 }, result[0]);
            Assertions.assertArrayEquals(new double[] { 3.0 }, result[1]);

            // Test with null 2D array
            long[][] nullInput = null;
            double[][] nullResult = Arrays.mapToDouble(nullInput, (long l) -> l * 0.1);
            Assertions.assertNull(nullResult);
        }

        @Test
        public void testMapToDouble_Long3DArray() {
            // Test with normal 3D array
            long[][][] input = { { { 100L, 200L } }, { { 300L } } };
            double[][][] result = Arrays.mapToDouble(input, (long l) -> l * 0.01d);
            Assertions.assertArrayEquals(new double[] { 1.0, 2.0 }, result[0][0]);
            Assertions.assertArrayEquals(new double[] { 3.0 }, result[1][0]);

            // Test with null 3D array
            long[][][] nullInput = null;
            double[][][] nullResult = Arrays.mapToDouble(nullInput, (long l) -> l * 0.01d);
            Assertions.assertNull(nullResult);
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
            Assertions.assertNull(nullResult);

            // Test with negative values
            double[] negativeInput = { 1.7, 2.3, 3.9 };
            int[] negativeResult = Arrays.mapToInt(negativeInput, d -> (int) Math.round(d));
            Assertions.assertArrayEquals(new int[] { 2, 2, 4 }, negativeResult);
        }

        @Test
        public void testMapToInt_Double2DArray() {
            // Test with normal 2D array
            double[][] input = { { 1.5, 2.5 }, { 3.5 } };
            int[][] result = Arrays.mapToInt(input, (double d) -> (int) Math.round(d));
            Assertions.assertArrayEquals(new int[] { 2, 3 }, result[0]);
            Assertions.assertArrayEquals(new int[] { 4 }, result[1]);

            // Test with null 2D array
            double[][] nullInput = null;
            int[][] nullResult = Arrays.mapToInt(nullInput, (double d) -> (int) d);
            Assertions.assertNull(nullResult);
        }

        @Test
        public void testMapToInt_Double3DArray() {
            // Test with normal 3D array
            double[][][] input = { { { 1.1, 2.9 } }, { { 3.5 } } };
            int[][][] result = Arrays.mapToInt(input, (double d) -> (int) Math.round(d));
            Assertions.assertArrayEquals(new int[] { 1, 3 }, result[0][0]);
            Assertions.assertArrayEquals(new int[] { 4 }, result[1][0]);

            // Test with null 3D array
            double[][][] nullInput = null;
            int[][][] nullResult = Arrays.mapToInt(nullInput, (double d) -> (int) d);
            Assertions.assertNull(nullResult);
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
            Assertions.assertNull(nullResult);

            // Test with large values
            double[] largeInput = { 1e10, 2e10, 3e10 };
            long[] largeResult = Arrays.mapToLong(largeInput, d -> (long) d);
            Assertions.assertArrayEquals(new long[] { 10000000000L, 20000000000L, 30000000000L }, largeResult);
        }

        @Test
        public void testMapToLong_Double2DArray() {
            // Test with normal 2D array
            double[][] input = { { 1.1, 2.2 }, { 3.3 } };
            long[][] result = Arrays.mapToLong(input, (double d) -> Math.round(d));
            Assertions.assertArrayEquals(new long[] { 1L, 2L }, result[0]);
            Assertions.assertArrayEquals(new long[] { 3L }, result[1]);

            // Test with null 2D array
            double[][] nullInput = null;
            long[][] nullResult = Arrays.mapToLong(nullInput, (double d) -> Math.round(d));
            Assertions.assertNull(nullResult);
        }

        @Test
        public void testMapToLong_Double3DArray() {
            // Test with normal 3D array
            double[][][] input = { { { 10.5, 20.5 } }, { { 30.5 } } };
            long[][][] result = Arrays.mapToLong(input, (double d) -> Math.round(d));
            Assertions.assertArrayEquals(new long[] { 11L, 21L }, result[0][0]);
            Assertions.assertArrayEquals(new long[] { 31L }, result[1][0]);

            // Test with null 3D array
            double[][][] nullInput = null;
            long[][][] nullResult = Arrays.mapToLong(nullInput, (double d) -> Math.round(d));
            Assertions.assertNull(nullResult);
        }

        // Tests for println methods
        @Test
        public void testPrintln_ObjectArray() {
            // Test with normal array
            Object[] arr = { "Hello", "World", 123 };
            String result = Arrays.println(arr);
            Assertions.assertTrue(result.contains("Hello"));
            Assertions.assertTrue(result.contains("World"));
            Assertions.assertTrue(result.contains("123"));

            // Test with empty array
            Object[] emptyArr = {};
            String emptyResult = Arrays.println(emptyArr);
            Assertions.assertEquals("[]", emptyResult.trim());

            // Test with null array
            Object[] nullArr = null;
            String nullResult = Arrays.println(nullArr);
            Assertions.assertEquals("null", nullResult.trim());

            // Test with array containing nulls
            Object[] nullsArr = { "test", null, 42 };
            String nullsResult = Arrays.println(nullsArr);
            Assertions.assertTrue(nullsResult.contains("test"));
            Assertions.assertTrue(nullsResult.contains("null"));
            Assertions.assertTrue(nullsResult.contains("42"));
        }

        @Test
        public void testPrintln_Object2DArray() {
            // Test with normal 2D array
            Object[][] arr = { { "A", "B" }, { "C", "D" } };
            String result = Arrays.println(arr);
            Assertions.assertNotNull(result);

            // Test with null 2D array
            Object[][] nullArr = null;
            String nullResult = Arrays.println(nullArr);
            Assertions.assertNotNull(nullResult);
        }

        @Test
        public void testPrintln_Object3DArray() {
            // Test with normal 3D array
            Object[][][] arr = { { { "A", "B" } }, { { "C", "D" } } };
            String result = Arrays.println(arr);
            Assertions.assertNotNull(result);

            // Test with null 3D array
            Object[][][] nullArr = null;
            String nullResult = Arrays.println(nullArr);
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
            // Test with normal 2D array
            boolean[][] arr = { { true, false }, { false, true } };
            Arrays.updateAll(arr, b -> !b);
            Assertions.assertArrayEquals(new boolean[] { false, true }, arr[0]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, arr[1]);

            // Test with empty 2D array
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
            // Test with normal 3D array
            boolean[][][] arr = { { { true, false } }, { { false, true }, { true, true } } };
            Arrays.updateAll(arr, b -> !b);
            Assertions.assertArrayEquals(new boolean[] { false, true }, arr[0][0]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, arr[1][0]);
            Assertions.assertArrayEquals(new boolean[] { false, false }, arr[1][1]);

            // Test with empty 3D array
            boolean[][][] emptyArr = {};
            Arrays.updateAll(emptyArr, b -> !b);
            Assertions.assertEquals(0, emptyArr.length);
        }

        // Tests for replaceIf methods
        @Test
        public void testReplaceIf_BooleanArray() {
            // Test replacing all true values with false
            boolean[] arr = { true, false, true, false, true };
            Arrays.replaceIf(arr, b -> b == true, false);
            Assertions.assertArrayEquals(new boolean[] { false, false, false, false, false }, arr);

            // Test replacing all false values with true
            boolean[] arr2 = { true, false, true, false };
            Arrays.replaceIf(arr2, b -> b == false, true);
            Assertions.assertArrayEquals(new boolean[] { true, true, true, true }, arr2);

            // Test with empty array
            boolean[] emptyArr = {};
            Arrays.replaceIf(emptyArr, b -> b == true, false);
            Assertions.assertArrayEquals(new boolean[] {}, emptyArr);

            // Test with no matches
            boolean[] noMatch = { false, false, false };
            Arrays.replaceIf(noMatch, b -> b == true, false);
            Assertions.assertArrayEquals(new boolean[] { false, false, false }, noMatch);
        }

        @Test
        public void testReplaceIf_Boolean2DArray() {
            // Test with normal 2D array
            boolean[][] arr = { { true, false }, { true, true } };
            Arrays.replaceIf(arr, b -> b == true, false);
            Assertions.assertArrayEquals(new boolean[] { false, false }, arr[0]);
            Assertions.assertArrayEquals(new boolean[] { false, false }, arr[1]);

            // Test with empty 2D array
            boolean[][] emptyArr = {};
            Arrays.replaceIf(emptyArr, b -> b == true, false);
            Assertions.assertEquals(0, emptyArr.length);
        }

        @Test
        public void testReplaceIf_Boolean3DArray() {
            // Test with normal 3D array
            boolean[][][] arr = { { { true, false } }, { { false, true } } };
            Arrays.replaceIf(arr, b -> b == false, true);
            Assertions.assertArrayEquals(new boolean[] { true, true }, arr[0][0]);
            Assertions.assertArrayEquals(new boolean[] { true, true }, arr[1][0]);

            // Test with empty 3D array
            boolean[][][] emptyArr = {};
            Arrays.replaceIf(emptyArr, b -> b == true, false);
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
        public void testReshape_BooleanArray_ToCols_InvalidInput() {
            boolean[] arr = { true, false };

            // Test with zero columns
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                Arrays.reshape(arr, 0);
            });

            // Test with negative columns
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                Arrays.reshape(arr, -1);
            });
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

            // Test with empty 2D array
            boolean[][] emptyArr = {};
            boolean[] emptyFlattened = Arrays.flatten(emptyArr);
            Assertions.assertArrayEquals(new boolean[] {}, emptyFlattened);

            // Test with null 2D array
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

            // Test with empty 3D array
            boolean[][][] emptyArr = {};
            boolean[] emptyFlattened = Arrays.flatten(emptyArr);
            Assertions.assertArrayEquals(new boolean[] {}, emptyFlattened);

            // Test with null 3D array
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

        // Tests for flatOp methods
        @Test
        public void testFlatOp_Boolean2DArray() {
            // Test sorting operation
            boolean[][] arr = { { true, false, true }, { false, true } };
            Arrays.flatOp(arr, flatArr -> {
                // Sort to have all false values first
                N.sort(flatArr);
            });
            // After sorting, false values come first
            Assertions.assertArrayEquals(new boolean[] { false, false, true }, arr[0]);
            Assertions.assertArrayEquals(new boolean[] { true, true }, arr[1]);

            // Test with operation that sets all to true
            boolean[][] arr2 = { { true, false }, { false, true } };
            Arrays.flatOp(arr2, flatArr -> {
                for (int i = 0; i < flatArr.length; i++) {
                    flatArr[i] = true;
                }
            });
            Assertions.assertArrayEquals(new boolean[] { true, true }, arr2[0]);
            Assertions.assertArrayEquals(new boolean[] { true, true }, arr2[1]);

            // Test with empty array
            boolean[][] emptyArr = {};
            Arrays.flatOp(emptyArr, flatArr -> {
                // Should not be called
                Assertions.fail("Operation should not be called on empty array");
            });

            // Test with array containing empty sub-arrays
            boolean[][] mixedArr = { { true, false }, {}, { true } };
            Arrays.flatOp(mixedArr, flatArr -> {
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
            // Test with normal 3D array
            boolean[][][] arr = { { { true, false } }, { { false, true }, { true, true } } };
            Arrays.flatOp(arr, flatArr -> {
                // Reverse all values
                for (int i = 0; i < flatArr.length; i++) {
                    flatArr[i] = !flatArr[i];
                }
            });
            Assertions.assertArrayEquals(new boolean[] { false, true }, arr[0][0]);
            Assertions.assertArrayEquals(new boolean[] { true, false }, arr[1][0]);
            Assertions.assertArrayEquals(new boolean[] { false, false }, arr[1][1]);

            // Test with empty 3D array
            boolean[][][] emptyArr = {};
            Arrays.flatOp(emptyArr, flatArr -> {
                // Should not be called
                Assertions.fail("Operation should not be called on empty array");
            });

            // Test counting operation
            boolean[][][] countArr = { { { true, false, true } }, { { false, false } } };
            int[] trueCount = { 0 };
            Arrays.flatOp(countArr, flatArr -> {
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
                Arrays.flatOp(arr, flatArr -> {
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
            Integer[][][] a = { { { 1 } } };
            Integer[][][] b = { { { 10, 20 } } };
            Integer[][][] c = { { {}, { 100 } } };
            Integer[][][] result = fff.zip(a, b, c, 0, 0, 0, (x, y, z) -> x + y + z);
            Arrays.println(result);
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
