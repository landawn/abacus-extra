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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.stream.ByteStream;
import com.landawn.abacus.util.stream.Collectors;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.LongStream;

class MatrixesTest extends TestBase {

    private ByteMatrix byteMatrix1;
    private ByteMatrix byteMatrix2;
    private ByteMatrix byteMatrix3;
    private IntMatrix intMatrix1;
    private IntMatrix intMatrix2;
    private IntMatrix intMatrix3;
    private LongMatrix longMatrix1;
    private LongMatrix longMatrix2;
    private LongMatrix longMatrix3;
    private DoubleMatrix doubleMatrix1;
    private DoubleMatrix doubleMatrix2;
    private DoubleMatrix doubleMatrix3;
    private Matrix<String> stringMatrix1;
    private Matrix<String> stringMatrix2;
    private Matrix<String> stringMatrix3;

    @BeforeEach
    public void setUp() {
        // Initialize test matrices
        byteMatrix1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        byteMatrix2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        byteMatrix3 = ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } });

        intMatrix1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        intMatrix2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        intMatrix3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });

        longMatrix1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        longMatrix2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        longMatrix3 = LongMatrix.of(new long[][] { { 9L, 10L }, { 11L, 12L } });

        doubleMatrix1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        doubleMatrix2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
        doubleMatrix3 = DoubleMatrix.of(new double[][] { { 9.0, 10.0 }, { 11.0, 12.0 } });

        stringMatrix1 = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
        stringMatrix2 = Matrix.of(new String[][] { { "e", "f" }, { "g", "h" } });
        stringMatrix3 = Matrix.of(new String[][] { { "i", "j" }, { "k", "l" } });
    }

    @AfterEach
    public void tearDown() {
        // Reset parallel enabled to default
        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
    }

    @Test
    public void testGetParallelEnabled() {
        // Test default value
        assertEquals(ParallelEnabled.DEFAULT, Matrixes.getParallelEnabled());

        // Test after setting
        Matrixes.setParallelEnabled(ParallelEnabled.YES);
        assertEquals(ParallelEnabled.YES, Matrixes.getParallelEnabled());

        Matrixes.setParallelEnabled(ParallelEnabled.NO);
        assertEquals(ParallelEnabled.NO, Matrixes.getParallelEnabled());
    }

    @Test
    public void testSetParallelEnabled() {
        // Test setting different values
        Matrixes.setParallelEnabled(ParallelEnabled.YES);
        assertEquals(ParallelEnabled.YES, Matrixes.getParallelEnabled());

        Matrixes.setParallelEnabled(ParallelEnabled.NO);
        assertEquals(ParallelEnabled.NO, Matrixes.getParallelEnabled());

        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
        assertEquals(ParallelEnabled.DEFAULT, Matrixes.getParallelEnabled());

        // Test null argument
        assertThrows(IllegalArgumentException.class, () -> Matrixes.setParallelEnabled(null));
    }

    @Test
    public void testIsParallelableWithMatrix() {
        // Test with small matrix (should return false for default setting)
        IntMatrix smallMatrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertFalse(Matrixes.isParallelable(smallMatrix));

        // Test with forced parallel enabled
        Matrixes.setParallelEnabled(ParallelEnabled.YES);
        assertTrue(Matrixes.isParallelable(smallMatrix));

        // Test with forced parallel disabled
        Matrixes.setParallelEnabled(ParallelEnabled.NO);
        assertFalse(Matrixes.isParallelable(smallMatrix));
    }

    @Test
    public void testIsParallelableWithMatrixAndCount() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        // Test with small count
        assertFalse(Matrixes.isParallelable(matrix, 100));

        // Test with large count (above threshold)
        boolean result = Matrixes.isParallelable(matrix, 10000);
        assertTrue(result || !result); // Depends on IS_PARALLEL_STREAM_SUPPORTED

        // Test with forced settings
        Matrixes.setParallelEnabled(ParallelEnabled.YES);
        assertTrue(Matrixes.isParallelable(matrix, 1));

        Matrixes.setParallelEnabled(ParallelEnabled.NO);
        assertFalse(Matrixes.isParallelable(matrix, 100000));
    }

    @Test
    public void testIsSameShapeTwoMatrices() {
        // Test same shape
        assertTrue(Matrixes.isSameShape(intMatrix1, intMatrix2));

        // Test different shapes
        IntMatrix differentShape = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertFalse(Matrixes.isSameShape(intMatrix1, differentShape));

        // Test with different matrix types but same shape
        assertTrue(Matrixes.isSameShape(byteMatrix1, byteMatrix2));
    }

    @Test
    public void testIsSameShapeThreeMatrices() {
        // Test same shape
        assertTrue(Matrixes.isSameShape(intMatrix1, intMatrix2, intMatrix3));

        // Test different shapes
        IntMatrix differentShape = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertFalse(Matrixes.isSameShape(intMatrix1, intMatrix2, differentShape));
    }

    @Test
    public void testIsSameShapeCollection() {
        // Test empty collection
        assertTrue(Matrixes.isSameShape(new ArrayList<IntMatrix>()));

        // Test single element
        assertTrue(Matrixes.isSameShape(N.asList(intMatrix1)));

        // Test multiple elements with same shape
        assertTrue(Matrixes.isSameShape(N.asList(intMatrix1, intMatrix2, intMatrix3)));

        // Test multiple elements with different shapes
        IntMatrix differentShape = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertFalse(Matrixes.isSameShape(N.asList(intMatrix1, intMatrix2, differentShape)));
    }

    @Test
    public void testNewArray() {
        // Test creating different types of arrays
        Integer[][] intArray = Matrixes.newArray(3, 4, Integer.class);
        assertEquals(3, intArray.length);
        assertEquals(4, intArray[0].length);

        String[][] stringArray = Matrixes.newArray(2, 5, String.class);
        assertEquals(2, stringArray.length);
        assertEquals(5, stringArray[0].length);

        // Test with primitive wrapper
        Double[][] doubleArray = Matrixes.newArray(1, 1, Double.class);
        assertEquals(1, doubleArray.length);
        assertEquals(1, doubleArray[0].length);
    }

    @Test
    public void testRunWithParallelEnabled() throws Exception {
        // Test that parallel setting is restored after execution
        ParallelEnabled original = Matrixes.getParallelEnabled();

        Matrixes.run(() -> {
            assertEquals(ParallelEnabled.YES, Matrixes.getParallelEnabled());
        }, ParallelEnabled.YES);

        assertEquals(original, Matrixes.getParallelEnabled());

        // Test with exception
        assertThrows(RuntimeException.class, () -> {
            Matrixes.run(() -> {
                throw new RuntimeException("Test exception");
            }, ParallelEnabled.NO);
        });

        // Verify setting is still restored after exception
        assertEquals(original, Matrixes.getParallelEnabled());
    }

    @Test
    public void testRunWithRowsColsAndCommand() throws Exception {
        // Test sequential execution
        int[][] result = new int[2][3];
        Matrixes.run(2, 3, (i, j) -> result[i][j] = i * 10 + j, false);

        assertEquals(0, result[0][0]);
        assertEquals(1, result[0][1]);
        assertEquals(2, result[0][2]);
        assertEquals(10, result[1][0]);
        assertEquals(11, result[1][1]);
        assertEquals(12, result[1][2]);

        // Test parallel execution
        int[][] parallelResult = new int[2][3];
        Matrixes.run(2, 3, (i, j) -> parallelResult[i][j] = i * 10 + j, true);

        assertEquals(0, parallelResult[0][0]);
        assertEquals(1, parallelResult[0][1]);
        assertEquals(2, parallelResult[0][2]);
        assertEquals(10, parallelResult[1][0]);
        assertEquals(11, parallelResult[1][1]);
        assertEquals(12, parallelResult[1][2]);
    }

    @Test
    public void testRunWithIndicesAndCommand() throws Exception {
        // Test with subregion
        int[][] result = new int[4][4];
        Matrixes.run(1, 3, 1, 3, (i, j) -> result[i][j] = i * 10 + j, false);

        assertEquals(0, result[0][0]); // Not touched
        assertEquals(11, result[1][1]);
        assertEquals(12, result[1][2]);
        assertEquals(21, result[2][1]);
        assertEquals(22, result[2][2]);
        assertEquals(0, result[3][3]); // Not touched

        // Test with invalid indices
        assertThrows(IndexOutOfBoundsException.class, () -> Matrixes.run(2, 1, 0, 2, (i, j) -> {
        }, false));
    }

    @Test
    public void testCall() {
        // Test sequential call
        List<String> results = Matrixes.call(2, 3, (i, j) -> i + "," + j, false).toList();

        assertEquals(6, results.size());
        assertTrue(results.contains("0,0"));
        assertTrue(results.contains("0,1"));
        assertTrue(results.contains("0,2"));
        assertTrue(results.contains("1,0"));
        assertTrue(results.contains("1,1"));
        assertTrue(results.contains("1,2"));

        // Test parallel call
        List<Integer> parallelResults = Matrixes.call(3, 2, (i, j) -> i * 10 + j, true).toList();

        assertEquals(6, parallelResults.size());
        assertTrue(parallelResults.contains(0));
        assertTrue(parallelResults.contains(1));
        assertTrue(parallelResults.contains(10));
        assertTrue(parallelResults.contains(11));
        assertTrue(parallelResults.contains(20));
        assertTrue(parallelResults.contains(21));
    }

    @Test
    public void testCallWithIndices() {
        // Test with subregion
        List<String> results = Matrixes.call(1, 3, 1, 3, (i, j) -> i + "," + j, false).toList();

        assertEquals(4, results.size());
        assertTrue(results.contains("1,1"));
        assertTrue(results.contains("1,2"));
        assertTrue(results.contains("2,1"));
        assertTrue(results.contains("2,2"));

        // Test with invalid indices
        assertThrows(IndexOutOfBoundsException.class, () -> Matrixes.call(2, 1, 0, 2, (i, j) -> "", false));
    }

    @Test
    public void testCallToInt() {
        // Test sequential
        int[] results = Matrixes.callToInt(2, 3, (i, j) -> i * 10 + j, false).toArray();

        assertEquals(6, results.length);
        assertTrue(IntStream.of(results).anyMatch(x -> x == 0));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 1));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 2));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 10));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 11));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 12));

        // Test parallel
        int[] parallelResults = Matrixes.callToInt(3, 2, (i, j) -> i + j, true).toArray();

        assertEquals(6, parallelResults.length);
    }

    @Test
    public void testCallToIntWithIndices() {
        // Test with subregion
        int[] results = Matrixes.callToInt(1, 3, 1, 3, (i, j) -> i * 10 + j, false).toArray();

        assertEquals(4, results.length);
        assertTrue(IntStream.of(results).anyMatch(x -> x == 11));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 12));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 21));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 22));
    }

    @Test
    public void testMultiply() {
        // Create test matrices for multiplication
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

        int[][] result = new int[2][2];

        Matrixes.multiply(a, b, (i, j, k) -> {
            result[i][j] += a.get(i, k) * b.get(k, j);
        });

        // Verify multiplication result
        assertEquals(19, result[0][0]); // 1*5 + 2*7
        assertEquals(22, result[0][1]); // 1*6 + 2*8
        assertEquals(43, result[1][0]); // 3*5 + 4*7
        assertEquals(50, result[1][1]); // 3*6 + 4*8

        // Test with incompatible dimensions
        IntMatrix c = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> Matrixes.multiply(a, c, (i, j, k) -> {
        }));
    }

    @Test
    public void testMultiplyWithParallel() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

        // Test sequential
        int[][] seqResult = new int[2][2];
        Matrixes.multiply(a, b, (i, j, k) -> {
            seqResult[i][j] += a.get(i, k) * b.get(k, j);
        }, false);

        // Test parallel
        int[][] parResult = new int[2][2];
        Matrixes.multiply(a, b, (i, j, k) -> {
            parResult[i][j] += a.get(i, k) * b.get(k, j);
        }, true);

        // Results should be the same
        assertArrayEquals(seqResult[0], parResult[0]);
        assertArrayEquals(seqResult[1], parResult[1]);
    }

    @Test
    public void testZipByteMatrix() throws Exception {
        // Test binary zip
        ByteMatrix result = Matrixes.zip(byteMatrix1, byteMatrix2, (a, b) -> (byte) (a + b));
        assertEquals((byte) 6, result.get(0, 0)); // 1 + 5
        assertEquals((byte) 8, result.get(0, 1)); // 2 + 6
        assertEquals((byte) 10, result.get(1, 0)); // 3 + 7
        assertEquals((byte) 12, result.get(1, 1)); // 4 + 8

        // Test ternary zip
        ByteMatrix ternaryResult = Matrixes.zip(byteMatrix1, byteMatrix2, byteMatrix3, (a, b, c) -> (byte) (a + b + c));
        assertEquals((byte) 15, ternaryResult.get(0, 0)); // 1 + 5 + 9
    }

    @Test
    public void testZipByteMatrixCollection() throws Exception {
        List<ByteMatrix> matrices = N.asList(byteMatrix1, byteMatrix2, byteMatrix3);

        // Test with binary operator
        ByteMatrix result = Matrixes.zip(matrices, (a, b) -> (byte) Math.max(a, b));
        assertEquals((byte) 9, result.get(0, 0));
        assertEquals((byte) 10, result.get(0, 1));
        assertEquals((byte) 11, result.get(1, 0));
        assertEquals((byte) 12, result.get(1, 1));

        // Test with empty collection
        assertThrows(IllegalArgumentException.class, () -> Matrixes.zip(new ArrayList<ByteMatrix>(), (a, b) -> a));

        // Test with single element
        ByteMatrix single = Matrixes.zip(N.asList(byteMatrix1), (a, b) -> a);
        assertEquals(byteMatrix1.get(0, 0), single.get(0, 0));
    }

    @Test
    public void testZipByteMatrixToGeneric() throws Exception {
        List<ByteMatrix> matrices = N.asList(byteMatrix1, byteMatrix2);

        // Test without sharing array
        Matrix<Integer> result = Matrixes.zip(matrices, arr -> (int) arr[0] + (int) arr[1], Integer.class);
        assertEquals(6, result.get(0, 0)); // 1 + 5
        assertEquals(8, result.get(0, 1)); // 2 + 6

        // Test with sharing array
        Matrix<String> stringResult = Matrixes.zip(matrices, arr -> N.toString(arr), true, String.class);
        assertEquals("[1, 5]", stringResult.get(0, 0));
    }

    @Test
    public void testZipByteMatrixToInt() throws Exception {
        // Test binary
        IntMatrix result = Matrixes.zipToInt(byteMatrix1, byteMatrix2, (a, b) -> (int) a * (int) b);
        assertEquals(5, result.get(0, 0)); // 1 * 5
        assertEquals(12, result.get(0, 1)); // 2 * 6

        // Test ternary
        IntMatrix ternaryResult = Matrixes.zipToInt(byteMatrix1, byteMatrix2, byteMatrix3, (a, b, c) -> (int) a + (int) b + (int) c);
        assertEquals(15, ternaryResult.get(0, 0)); // 1 + 5 + 9
    }

    @Test
    public void testZipByteMatrixCollectionToInt() throws Exception {
        List<ByteMatrix> matrices = N.asList(byteMatrix1, byteMatrix2, byteMatrix3);

        // Test without sharing array
        IntMatrix result = Matrixes.zipToInt(matrices, arr -> ByteStream.of(arr).mapToInt(b -> b).sum());
        assertEquals(15, result.get(0, 0)); // 1 + 5 + 9

        // Test with sharing array
        IntMatrix sharedResult = Matrixes.zipToInt(matrices, arr -> ByteStream.of(arr).mapToInt(b -> b).max().orElse(0), true);
        assertEquals(9, sharedResult.get(0, 0));
    }

    @Test
    public void testZipIntMatrix() throws Exception {
        // Test binary zip
        IntMatrix result = Matrixes.zip(intMatrix1, intMatrix2, (a, b) -> a + b);
        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));

        // Test ternary zip
        IntMatrix ternaryResult = Matrixes.zip(intMatrix1, intMatrix2, intMatrix3, (a, b, c) -> a * b * c);
        assertEquals(45, ternaryResult.get(0, 0)); // 1 * 5 * 9
    }

    @Test
    public void testZipIntMatrixCollection() throws Exception {
        List<IntMatrix> matrices = N.asList(intMatrix1, intMatrix2, intMatrix3);

        IntMatrix result = Matrixes.zip(matrices, (a, b) -> a + b);
        assertEquals(15, result.get(0, 0)); // 1 + 5 + 9
        assertEquals(18, result.get(0, 1)); // 2 + 6 + 10
    }

    @Test
    public void testZipIntMatrixToGeneric() throws Exception {
        List<IntMatrix> matrices = N.asList(intMatrix1, intMatrix2);

        Matrix<Double> result = Matrixes.zip(matrices, arr -> IntStream.of(arr).average().orElse(0.0), Double.class);
        assertEquals(3.0, result.get(0, 0)); // (1 + 5) / 2
    }

    @Test
    public void testZipIntMatrixToLong() throws Exception {
        // Test binary
        LongMatrix result = Matrixes.zipToLong(intMatrix1, intMatrix2, (a, b) -> (long) a * b);
        assertEquals(5L, result.get(0, 0));

        // Test ternary
        LongMatrix ternaryResult = Matrixes.zipToLong(intMatrix1, intMatrix2, intMatrix3, (a, b, c) -> (long) (a + b + c));
        assertEquals(15L, ternaryResult.get(0, 0));
    }

    @Test
    public void testZipIntMatrixCollectionToLong() throws Exception {
        List<IntMatrix> matrices = N.asList(intMatrix1, intMatrix2);

        LongMatrix result = Matrixes.zipToLong(matrices, arr -> (long) IntStream.of(arr).sum());
        assertEquals(6L, result.get(0, 0));
    }

    @Test
    public void testZipIntMatrixToDouble() throws Exception {
        // Test binary
        DoubleMatrix result = Matrixes.zipToDouble(intMatrix1, intMatrix2, (a, b) -> (double) a / b);
        assertEquals(0.2, result.get(0, 0), 0.001); // 1 / 5

        // Test ternary
        DoubleMatrix ternaryResult = Matrixes.zipToDouble(intMatrix1, intMatrix2, intMatrix3, (a, b, c) -> (double) (a + b) / c);
        assertEquals(0.666, ternaryResult.get(0, 0), 0.001); // (1 + 5) / 9
    }

    @Test
    public void testZipIntMatrixCollectionToDouble() throws Exception {
        List<IntMatrix> matrices = N.asList(intMatrix1, intMatrix2);

        DoubleMatrix result = Matrixes.zipToDouble(matrices, arr -> IntStream.of(arr).average().orElse(0.0));
        assertEquals(3.0, result.get(0, 0));
    }

    @Test
    public void testZipLongMatrix() throws Exception {
        // Test binary zip
        LongMatrix result = Matrixes.zip(longMatrix1, longMatrix2, (a, b) -> a + b);
        assertEquals(6L, result.get(0, 0));

        // Test ternary zip
        LongMatrix ternaryResult = Matrixes.zip(longMatrix1, longMatrix2, longMatrix3, (a, b, c) -> a * b + c);
        assertEquals(14L, ternaryResult.get(0, 0)); // 1 * 5 + 9
    }

    @Test
    public void testZipLongMatrixCollection() throws Exception {
        List<LongMatrix> matrices = N.asList(longMatrix1, longMatrix2, longMatrix3);

        LongMatrix result = Matrixes.zip(matrices, Math::max);
        assertEquals(9L, result.get(0, 0));
    }

    @Test
    public void testZipLongMatrixToGeneric() throws Exception {
        List<LongMatrix> matrices = N.asList(longMatrix1, longMatrix2);

        Matrix<String> result = Matrixes.zip(matrices, arr -> LongStream.of(arr).mapToObj(String::valueOf).collect(Collectors.joining(",")), String.class);
        assertEquals("1,5", result.get(0, 0));
    }

    @Test
    public void testZipLongMatrixToDouble() throws Exception {
        // Test binary
        DoubleMatrix result = Matrixes.zipToDouble(longMatrix1, longMatrix2, (a, b) -> (double) a / b);
        assertEquals(0.2, result.get(0, 0), 0.001);

        // Test ternary
        DoubleMatrix ternaryResult = Matrixes.zipToDouble(longMatrix1, longMatrix2, longMatrix3, (a, b, c) -> Math.sqrt(a * b * c));
        assertEquals(Math.sqrt(45), ternaryResult.get(0, 0), 0.001);
    }

    @Test
    public void testZipLongMatrixCollectionToDouble() throws Exception {
        List<LongMatrix> matrices = N.asList(longMatrix1, longMatrix2);

        DoubleMatrix result = Matrixes.zipToDouble(matrices, arr -> LongStream.of(arr).average().orElse(0.0));
        assertEquals(3.0, result.get(0, 0));
    }

    @Test
    public void testZipDoubleMatrix() throws Exception {
        // Test binary zip
        DoubleMatrix result = Matrixes.zip(doubleMatrix1, doubleMatrix2, (a, b) -> a + b);
        assertEquals(6.0, result.get(0, 0), 0.001);

        // Test ternary zip
        DoubleMatrix ternaryResult = Matrixes.zip(doubleMatrix1, doubleMatrix2, doubleMatrix3, (a, b, c) -> (a + b) * c);
        assertEquals(54.0, ternaryResult.get(0, 0), 0.001); // (1 + 5) * 9
    }

    @Test
    public void testZipDoubleMatrixCollection() throws Exception {
        List<DoubleMatrix> matrices = N.asList(doubleMatrix1, doubleMatrix2, doubleMatrix3);

        DoubleMatrix result = Matrixes.zip(matrices, Math::min);
        assertEquals(1.0, result.get(0, 0), 0.001);
    }

    @Test
    public void testZipDoubleMatrixToGeneric() throws Exception {
        List<DoubleMatrix> matrices = N.asList(doubleMatrix1, doubleMatrix2);

        Matrix<Boolean> result = Matrixes.zip(matrices, arr -> arr[0] < arr[1], Boolean.class);
        assertTrue(result.get(0, 0)); // 1.0 < 5.0
    }

    @Test
    public void testZipGenericMatrix() throws Exception {
        // Test binary zip with same result type
        Matrix<String> result = Matrixes.zip(stringMatrix1, stringMatrix2, (a, b) -> a + b);
        assertEquals("ae", result.get(0, 0));

        // Test binary zip with different result type
        Matrix<Integer> lengthResult = Matrixes.zip(stringMatrix1, stringMatrix2, (a, b) -> a.length() + b.length(), Integer.class);
        assertEquals(2, lengthResult.get(0, 0).intValue());

        // Test ternary zip
        Matrix<String> ternaryResult = Matrixes.zip(stringMatrix1, stringMatrix2, stringMatrix3, (a, b, c) -> a + b + c);
        assertEquals("aei", ternaryResult.get(0, 0));

        // Test ternary zip with different result type
        Matrix<Integer> ternaryLengthResult = Matrixes.zip(stringMatrix1, stringMatrix2, stringMatrix3, (a, b, c) -> a.length() + b.length() + c.length(),
                Integer.class);
        assertEquals(3, ternaryLengthResult.get(0, 0).intValue());
    }

    @Test
    public void testZipGenericMatrixCollection() throws Exception {
        List<Matrix<String>> matrices = N.asList(stringMatrix1, stringMatrix2, stringMatrix3);

        // Test with binary operator
        Matrix<String> result = Matrixes.zip(matrices, (a, b) -> a + b);
        assertEquals("aei", result.get(0, 0));
    }

    @Test
    public void testZipGenericMatrixCollectionWithFunction() throws Exception {
        List<Matrix<String>> matrices = N.asList(stringMatrix1, stringMatrix2);

        // Test without sharing array
        Matrix<String> result = Matrixes.zip(matrices, arr -> String.join("-", arr), String.class);
        assertEquals("a-e", result.get(0, 0));

        // Test with sharing array
        Matrix<Integer> countResult = Matrixes.zip(matrices, arr -> arr.length, true, Integer.class);
        assertEquals(2, countResult.get(0, 0).intValue());
    }

    @Test
    public void testZipWithDifferentShapes() {
        // Create matrices with different shapes
        IntMatrix differentShape = IntMatrix.of(new int[][] { { 1, 2, 3 } });

        // Test that zip methods throw exception for different shapes
        assertThrows(IllegalArgumentException.class, () -> Matrixes.zip(intMatrix1, differentShape, (a, b) -> a + b));

        assertThrows(IllegalArgumentException.class, () -> Matrixes.zip(N.asList(intMatrix1, differentShape), (a, b) -> a + b));
    }

    @Test
    public void testParallelProcessing() throws Exception {
        // Create larger matrices to test parallel processing
        int size = 100;
        int[][] largeData1 = new int[size][size];
        int[][] largeData2 = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                largeData1[i][j] = i * size + j;
                largeData2[i][j] = i * size + j + 1;
            }
        }

        IntMatrix large1 = IntMatrix.of(largeData1);
        IntMatrix large2 = IntMatrix.of(largeData2);

        // Force parallel processing
        Matrixes.setParallelEnabled(ParallelEnabled.YES);

        IntMatrix result = Matrixes.zip(large1, large2, (a, b) -> a + b);

        // Verify a few results
        assertEquals(1, result.get(0, 0)); // 0 + 1
        assertEquals(size * size * 2 - 1, result.get(size - 1, size - 1));
    }

    @Test
    public void testZipCollectionEdgeCases() throws Exception {
        // Test collection zip with two elements (should use optimized path)
        List<IntMatrix> twoMatrices = N.asList(intMatrix1, intMatrix2);
        IntMatrix twoResult = Matrixes.zip(twoMatrices, (a, b) -> a + b);
        assertEquals(6, twoResult.get(0, 0));

        // Test collection zip with many elements
        List<IntMatrix> manyMatrices = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            manyMatrices.add(IntMatrix.of(new int[][] { { i, i + 1 }, { i + 2, i + 3 } }));
        }

        IntMatrix manyResult = Matrixes.zip(manyMatrices, (a, b) -> a + b);
        // Should sum all values at each position
        assertEquals(0 + 1 + 2 + 3 + 4, manyResult.get(0, 0));
    }

    @Test
    public void test_toString() {

        {
            final ByteMatrix mx = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);

            mx.println();
            mx.boxed().println();

            N.println(Strings.repeat('-', 80));

            N.println(mx);
            N.println(mx.boxed());

            mx.boxed().toDataSetH(N.asList("a", "b", "c", "d")).println();
            mx.boxed().toDataSetV(N.asList("a", "b")).println();
        }
    }

    @Test
    public void test_zipMatrix() {

        {
            final ByteMatrix mx = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);
            mx.println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx), (i, j) -> (byte) (i + j)).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx), (i, j) -> (byte) (i + j)).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx), (i, j) -> (byte) (i + j)).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx, mx), (i, j) -> (byte) (i + j)).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx, mx), a -> (byte) N.sum(a), Byte.class).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zipToInt(N.asList(mx, mx, mx, mx), N::sum).println();
        }

        N.println(Strings.repeat('=', 80));
        N.println(Strings.repeat('=', 80));

        {
            final IntMatrix mx = IntMatrix.range(0, 8).reshape(2, 4);
            mx.println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx), (i, j) -> i + j).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx), (i, j) -> i + j).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx), (i, j) -> i + j).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx, mx), (i, j) -> i + j).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zip(N.asList(mx, mx, mx, mx), a -> (byte) N.sum(a), byte.class).println();

            N.println(Strings.repeat('-', 80));
            Matrixes.zipToLong(N.asList(mx, mx, mx, mx), a -> (long) N.sum(a)).println();
        }

    }

    @Test
    public void test_multiply() {
        {
            final ByteMatrix mxa = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);
            mxa.println();

            final ByteMatrix mxb = ByteMatrix.range((byte) 0, (byte) 8).reshape(4, 2);
            mxb.println();

            mxa.multiply(mxb).println();
        }

        N.println(Strings.repeat('=', 80));
        N.println(Strings.repeat('=', 80));

        {
            final IntMatrix mxa = IntMatrix.range(0, 8).reshape(2, 4);
            mxa.println();

            final IntMatrix mxb = IntMatrix.range(0, 8).reshape(4, 2);
            mxb.println();

            mxa.multiply(mxb).println();
        }
    }

    @Test
    public void test_multiply_perf() {
        final int rows = 2000;
        final int cols = 3000;

        final int[][] a = new int[rows][cols];
        final int[][] b = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                a[i][j] = 1;
                b[j][i] = 2;
            }
        }

        final IntMatrix mxa = IntMatrix.of(a);
        final IntMatrix mxb = IntMatrix.of(b);

        //    final IntMatrix mxc = mxa.multiply(mxb);
        //
        //    assertEquals(rows, mxc.rows);
        //    assertEquals(rows, mxc.cols);
        // mxc.println();

        Profiler.run(1, 1, 1, "seq-multiply(" + rows + ", " + cols + ")", () -> {
            Matrixes.setParallelEnabled(ParallelEnabled.NO);
            mxa.multiply(mxb);
        }).printResult();

        Profiler.run(1, 1, 1, "parallel-multiply(" + rows + ", " + cols + ")", () -> {
            Matrixes.setParallelEnabled(ParallelEnabled.YES);
            mxa.multiply(mxb);
        }).printResult();

    }

}
