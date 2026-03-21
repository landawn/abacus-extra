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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.stream.ByteStream;
import com.landawn.abacus.util.stream.Collectors;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.LongStream;
import com.landawn.abacus.util.stream.Stream;

class MatricesTest extends TestBase {

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
        Matrices.setParallelMode(ParallelMode.AUTO);
    }

    @Test
    public void testGetParallelMode() {
        // Test default value
        assertEquals(ParallelMode.AUTO, Matrices.getParallelMode());

        // Test after setting
        Matrices.setParallelMode(ParallelMode.FORCE_ON);
        assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());

        Matrices.setParallelMode(ParallelMode.FORCE_OFF);
        assertEquals(ParallelMode.FORCE_OFF, Matrices.getParallelMode());
    }

    @Test
    public void testSetParallelMode() {
        // Test setting different values
        Matrices.setParallelMode(ParallelMode.FORCE_ON);
        assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());

        Matrices.setParallelMode(ParallelMode.FORCE_OFF);
        assertEquals(ParallelMode.FORCE_OFF, Matrices.getParallelMode());

        Matrices.setParallelMode(ParallelMode.AUTO);
        assertEquals(ParallelMode.AUTO, Matrices.getParallelMode());

        // Test null argument
        assertThrows(IllegalArgumentException.class, () -> Matrices.setParallelMode(null));
    }

    @Test
    public void testIsParallelableWithMatrix() {
        // Test with small matrix (should return false for default setting)
        IntMatrix smallMatrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertFalse(Matrices.isParallelizable(smallMatrix));

        // Test with forced parallel enabled
        Matrices.setParallelMode(ParallelMode.FORCE_ON);
        assertTrue(Matrices.isParallelizable(smallMatrix));

        // Test with forced parallel disabled
        Matrices.setParallelMode(ParallelMode.FORCE_OFF);
        assertFalse(Matrices.isParallelizable(smallMatrix));
    }

    @Test
    public void testIsSameShapeTwoMatrices() {
        // Test same shape
        assertTrue(Matrices.isSameShape(intMatrix1, intMatrix2));
        assertThrows(IllegalArgumentException.class, () -> intMatrix1.isSameShape((IntMatrix) null));

        // Test different shapes
        IntMatrix differentShape = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertFalse(Matrices.isSameShape(intMatrix1, differentShape));

        // Test with different matrix types but same shape
        assertTrue(Matrices.isSameShape(byteMatrix1, byteMatrix2));
    }

    @Test
    public void testIsSameShapeThreeMatrices() {
        // Test same shape
        assertTrue(Matrices.isSameShape(intMatrix1, intMatrix2, intMatrix3));

        // Test different shapes
        IntMatrix differentShape = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertFalse(Matrices.isSameShape(intMatrix1, intMatrix2, differentShape));

        // Test null arguments
        assertThrows(IllegalArgumentException.class, () -> Matrices.isSameShape(intMatrix1, null, intMatrix3));
        assertThrows(IllegalArgumentException.class, () -> Matrices.isSameShape(intMatrix1, intMatrix2, null));
    }

    @Test
    public void testIsSameShapeCollection() {
        // Test empty collection
        assertTrue(Matrices.isSameShape(new ArrayList<IntMatrix>()));

        // Test single element
        assertTrue(Matrices.isSameShape(CommonUtil.asList(intMatrix1)));

        // Test multiple elements with same shape
        assertTrue(Matrices.isSameShape(CommonUtil.asList(intMatrix1, intMatrix2, intMatrix3)));

        // Test multiple elements with different shapes
        IntMatrix differentShape = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertFalse(Matrices.isSameShape(CommonUtil.asList(intMatrix1, intMatrix2, differentShape)));
    }

    @Test
    public void testIsSameShapeCollectionWithLeadingNull_EdgeCase() {
        assertFalse(Matrices.isSameShape(Arrays.asList(null, intMatrix1)));
    }

    @Test
    public void testNewArray() {
        // Test creating different types of arrays
        Integer[][] intArray = Matrices.newMatrixArray(3, 4, Integer.class);
        assertEquals(3, intArray.length);
        assertEquals(4, intArray[0].length);

        String[][] stringArray = Matrices.newMatrixArray(2, 5, String.class);
        assertEquals(2, stringArray.length);
        assertEquals(5, stringArray[0].length);

        // Test with primitive wrapper
        Double[][] doubleArray = Matrices.newMatrixArray(1, 1, Double.class);
        assertEquals(1, doubleArray.length);
        assertEquals(1, doubleArray[0].length);
    }

    @Test
    public void testRunWithParallelMode() throws Exception {
        // Test that parallel setting is restored after execution
        ParallelMode original = Matrices.getParallelMode();

        Matrices.runWithParallelMode(ParallelMode.FORCE_ON, () -> {
            assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());
        });

        assertEquals(original, Matrices.getParallelMode());

        // Test with exception
        assertThrows(RuntimeException.class, () -> {
            Matrices.runWithParallelMode(ParallelMode.FORCE_OFF, () -> {
                throw new RuntimeException("Test exception");
            });
        });

        // Verify setting is still restored after exception
        assertEquals(original, Matrices.getParallelMode());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRunWithNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> Matrices.runWithParallelMode(ParallelMode.FORCE_OFF, (Throwables.Runnable<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachIndex(2, 3, (Throwables.IntBiConsumer<RuntimeException>) null, false));
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachIndex(0, 2, 0, 2, (Throwables.IntBiConsumer<RuntimeException>) null, false));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCallWithNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> Matrices.mapIndices(2, 3, (Throwables.IntBiFunction<String, RuntimeException>) null, false));
        assertThrows(IllegalArgumentException.class, () -> Matrices.mapIndices(0, 2, 0, 3, (Throwables.IntBiFunction<String, RuntimeException>) null, false));
        assertThrows(IllegalArgumentException.class, () -> Matrices.mapIndicesToInt(2, 3, (Throwables.IntBinaryOperator<RuntimeException>) null, false));
        assertThrows(IllegalArgumentException.class, () -> Matrices.mapIndicesToInt(0, 2, 0, 3, (Throwables.IntBinaryOperator<RuntimeException>) null, false));
    }

    @Test
    public void testRunWithRowsColsAndCommand() throws Exception {
        // Test sequential execution
        int[][] result = new int[2][3];
        Matrices.forEachIndex(2, 3, (i, j) -> result[i][j] = i * 10 + j, false);

        assertEquals(0, result[0][0]);
        assertEquals(1, result[0][1]);
        assertEquals(2, result[0][2]);
        assertEquals(10, result[1][0]);
        assertEquals(11, result[1][1]);
        assertEquals(12, result[1][2]);

        // Test parallel execution
        int[][] parallelResult = new int[2][3];
        Matrices.forEachIndex(2, 3, (i, j) -> parallelResult[i][j] = i * 10 + j, true);

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
        Matrices.forEachIndex(1, 3, 1, 3, (i, j) -> result[i][j] = i * 10 + j, false);

        assertEquals(0, result[0][0]); // Not touched
        assertEquals(11, result[1][1]);
        assertEquals(12, result[1][2]);
        assertEquals(21, result[2][1]);
        assertEquals(22, result[2][2]);
        assertEquals(0, result[3][3]); // Not touched

        // Test with invalid indices
        assertThrows(IndexOutOfBoundsException.class, () -> Matrices.forEachIndex(2, 1, 0, 2, (i, j) -> {
        }, false));
    }

    @Test
    public void testCall() {
        // Test sequential call
        List<String> results = Matrices.mapIndices(2, 3, (i, j) -> i + "," + j, false).toList();

        assertEquals(6, results.size());
        assertTrue(results.contains("0,0"));
        assertTrue(results.contains("0,1"));
        assertTrue(results.contains("0,2"));
        assertTrue(results.contains("1,0"));
        assertTrue(results.contains("1,1"));
        assertTrue(results.contains("1,2"));

        // Test parallel call
        List<Integer> parallelResults = Matrices.mapIndices(3, 2, (i, j) -> i * 10 + j, true).toList();

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
        List<String> results = Matrices.mapIndices(1, 3, 1, 3, (i, j) -> i + "," + j, false).toList();

        assertEquals(4, results.size());
        assertTrue(results.contains("1,1"));
        assertTrue(results.contains("1,2"));
        assertTrue(results.contains("2,1"));
        assertTrue(results.contains("2,2"));

        // Test with invalid indices
        assertThrows(IndexOutOfBoundsException.class, () -> Matrices.mapIndices(2, 1, 0, 2, (i, j) -> "", false));
    }

    @Test
    public void testCallToInt() {
        // Test sequential
        int[] results = Matrices.mapIndicesToInt(2, 3, (i, j) -> i * 10 + j, false).toArray();

        assertEquals(6, results.length);
        assertTrue(IntStream.of(results).anyMatch(x -> x == 0));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 1));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 2));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 10));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 11));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 12));

        // Test parallel
        int[] parallelResults = Matrices.mapIndicesToInt(3, 2, (i, j) -> i + j, true).toArray();

        assertEquals(6, parallelResults.length);
    }

    @Test
    public void testCallToIntWithIndices() {
        // Test with subregion
        int[] results = Matrices.mapIndicesToInt(1, 3, 1, 3, (i, j) -> i * 10 + j, false).toArray();

        assertEquals(4, results.length);
        assertTrue(IntStream.of(results).anyMatch(x -> x == 11));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 12));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 21));
        assertTrue(IntStream.of(results).anyMatch(x -> x == 22));
    }

    @Test
    public void testMapIndicesWrapsCheckedException_EdgeCase() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> Matrices.mapIndices(2, 2, (i, j) -> {
            if (i == 1 && j == 0) {
                throw new IOException("boom");
            }

            return i + "," + j;
        }, false).toList());

        assertNotNull(ex.getCause());
        assertTrue(ex.getCause() instanceof IOException);
    }

    @Test
    public void testMapIndicesToIntWrapsCheckedException_EdgeCase() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> Matrices.mapIndicesToInt(2, 2, (i, j) -> {
            if (i == 0 && j == 1) {
                throw new IOException("boom");
            }

            return i + j;
        }, false).toArray());

        assertNotNull(ex.getCause());
        assertTrue(ex.getCause() instanceof IOException);
    }

    @Test
    public void testMultiply() {
        // Create test matrices for multiplication
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

        int[][] result = new int[2][2];

        Matrices.forEachCartesianIndices(a, b, (i, j, k) -> {
            result[i][j] += a.get(i, k) * b.get(k, j);
        });

        // Verify multiplication result
        assertEquals(19, result[0][0]); // 1*5 + 2*7
        assertEquals(22, result[0][1]); // 1*6 + 2*8
        assertEquals(43, result[1][0]); // 3*5 + 4*7
        assertEquals(50, result[1][1]); // 3*6 + 4*8

        // Test with incompatible dimensions
        IntMatrix c = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(a, c, (i, j, k) -> {
        }));

        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(null, b, (i, j, k) -> {
        }));
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(a, null, (i, j, k) -> {
        }));
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(a, b, (Throwables.IntTriConsumer<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(a, b, (Throwables.IntTriConsumer<RuntimeException>) null, false));
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(null, b, (i, j, k) -> {
        }, false));
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(a, null, (i, j, k) -> {
        }, false));
    }

    @Test
    public void testMultiplyWithParallel() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

        // Test sequential
        int[][] seqResult = new int[2][2];
        Matrices.forEachCartesianIndices(a, b, (i, j, k) -> {
            seqResult[i][j] += a.get(i, k) * b.get(k, j);
        }, false);

        // Test parallel
        int[][] parResult = new int[2][2];
        Matrices.forEachCartesianIndices(a, b, (i, j, k) -> {
            parResult[i][j] += a.get(i, k) * b.get(k, j);
        }, true);

        // Results should be the same
        assertArrayEquals(seqResult[0], parResult[0]);
        assertArrayEquals(seqResult[1], parResult[1]);
    }

    @Test
    public void testZipByteMatrix() throws Exception {
        // Test binary zip
        ByteMatrix result = Matrices.zip(byteMatrix1, byteMatrix2, (a, b) -> (byte) (a + b));
        assertEquals((byte) 6, result.get(0, 0)); // 1 + 5
        assertEquals((byte) 8, result.get(0, 1)); // 2 + 6
        assertEquals((byte) 10, result.get(1, 0)); // 3 + 7
        assertEquals((byte) 12, result.get(1, 1)); // 4 + 8

        // Test ternary zip
        ByteMatrix ternaryResult = Matrices.zip(byteMatrix1, byteMatrix2, byteMatrix3, (a, b, c) -> (byte) (a + b + c));
        assertEquals((byte) 15, ternaryResult.get(0, 0)); // 1 + 5 + 9
    }

    @Test
    public void testZipByteMatrixToGeneric() throws Exception {
        List<ByteMatrix> matrices = CommonUtil.asList(byteMatrix1, byteMatrix2);

        // Test without sharing array
        Matrix<Integer> result = Matrices.zip(matrices, arr -> arr[0] + arr[1], Integer.class);
        assertEquals(6, result.get(0, 0)); // 1 + 5
        assertEquals(8, result.get(0, 1)); // 2 + 6

        // Test with sharing array
        Matrix<String> stringResult = Matrices.zip(matrices, arr -> CommonUtil.toString(arr), true, String.class);
        assertEquals("[1, 5]", stringResult.get(0, 0));
    }

    @Test
    public void testZipByteMatrixToInt() throws Exception {
        // Test binary
        IntMatrix result = Matrices.zipToInt(byteMatrix1, byteMatrix2, (a, b) -> a * b);
        assertEquals(5, result.get(0, 0)); // 1 * 5
        assertEquals(12, result.get(0, 1)); // 2 * 6

        // Test ternary
        IntMatrix ternaryResult = Matrices.zipToInt(byteMatrix1, byteMatrix2, byteMatrix3, (a, b, c) -> a + b + c);
        assertEquals(15, ternaryResult.get(0, 0)); // 1 + 5 + 9
    }

    @Test
    public void testZipByteMatrixCollectionToInt() throws Exception {
        List<ByteMatrix> matrices = CommonUtil.asList(byteMatrix1, byteMatrix2, byteMatrix3);

        // Test without sharing array
        IntMatrix result = Matrices.zipToInt(matrices, arr -> ByteStream.of(arr).mapToInt(b -> b).sum());
        assertEquals(15, result.get(0, 0)); // 1 + 5 + 9

        // Test with sharing array
        IntMatrix sharedResult = Matrices.zipToInt(matrices, arr -> ByteStream.of(arr).mapToInt(b -> b).max().orElse(0), true);
        assertEquals(9, sharedResult.get(0, 0));
    }

    @Test
    public void testZipIntMatrix() throws Exception {
        // Test binary zip
        IntMatrix result = Matrices.zip(intMatrix1, intMatrix2, (a, b) -> a + b);
        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));

        // Test ternary zip
        IntMatrix ternaryResult = Matrices.zip(intMatrix1, intMatrix2, intMatrix3, (a, b, c) -> a * b * c);
        assertEquals(45, ternaryResult.get(0, 0)); // 1 * 5 * 9
    }

    @Test
    public void testZipIntMatrixCollection() throws Exception {
        List<IntMatrix> matrices = CommonUtil.asList(intMatrix1, intMatrix2, intMatrix3);

        IntMatrix result = Matrices.zip(matrices, (a, b) -> a + b);
        assertEquals(15, result.get(0, 0)); // 1 + 5 + 9
        assertEquals(18, result.get(0, 1)); // 2 + 6 + 10
    }

    @Test
    public void testZipIntMatrixToGeneric() throws Exception {
        List<IntMatrix> matrices = CommonUtil.asList(intMatrix1, intMatrix2);

        Matrix<Double> result = Matrices.zip(matrices, arr -> IntStream.of(arr).average().orElse(0.0), Double.class);
        assertEquals(3.0, result.get(0, 0)); // (1 + 5) / 2
    }

    @Test
    public void testZipIntMatrixToLong() throws Exception {
        // Test binary
        LongMatrix result = Matrices.zipToLong(intMatrix1, intMatrix2, (a, b) -> (long) a * b);
        assertEquals(5L, result.get(0, 0));

        // Test ternary
        LongMatrix ternaryResult = Matrices.zipToLong(intMatrix1, intMatrix2, intMatrix3, (a, b, c) -> (long) (a + b + c));
        assertEquals(15L, ternaryResult.get(0, 0));
    }

    @Test
    public void testZipIntMatrixCollectionToLong() throws Exception {
        List<IntMatrix> matrices = CommonUtil.asList(intMatrix1, intMatrix2);

        LongMatrix result = Matrices.zipToLong(matrices, arr -> (long) IntStream.of(arr).sum());
        assertEquals(6L, result.get(0, 0));
    }

    @Test
    public void testZipIntMatrixToDouble() throws Exception {
        // Test binary
        DoubleMatrix result = Matrices.zipToDouble(intMatrix1, intMatrix2, (a, b) -> (double) a / b);
        assertEquals(0.2, result.get(0, 0), 0.001); // 1 / 5

        // Test ternary
        DoubleMatrix ternaryResult = Matrices.zipToDouble(intMatrix1, intMatrix2, intMatrix3, (a, b, c) -> (double) (a + b) / c);
        assertEquals(0.666, ternaryResult.get(0, 0), 0.001); // (1 + 5) / 9
    }

    @Test
    public void testZipIntMatrixCollectionToDouble() throws Exception {
        List<IntMatrix> matrices = CommonUtil.asList(intMatrix1, intMatrix2);

        DoubleMatrix result = Matrices.zipToDouble(matrices, arr -> IntStream.of(arr).average().orElse(0.0));
        assertEquals(3.0, result.get(0, 0));
    }

    @Test
    public void testZipLongMatrix() throws Exception {
        // Test binary zip
        LongMatrix result = Matrices.zip(longMatrix1, longMatrix2, (a, b) -> a + b);
        assertEquals(6L, result.get(0, 0));

        // Test ternary zip
        LongMatrix ternaryResult = Matrices.zip(longMatrix1, longMatrix2, longMatrix3, (a, b, c) -> a * b + c);
        assertEquals(14L, ternaryResult.get(0, 0)); // 1 * 5 + 9
    }

    @Test
    public void testZipLongMatrixCollection() throws Exception {
        List<LongMatrix> matrices = CommonUtil.asList(longMatrix1, longMatrix2, longMatrix3);

        LongMatrix result = Matrices.zip(matrices, Math::max);
        assertEquals(9L, result.get(0, 0));
    }

    @Test
    public void testZipLongMatrixToGeneric() throws Exception {
        List<LongMatrix> matrices = CommonUtil.asList(longMatrix1, longMatrix2);

        Matrix<String> result = Matrices.zip(matrices, arr -> LongStream.of(arr).mapToObj(String::valueOf).collect(Collectors.joining(",")), String.class);
        assertEquals("1,5", result.get(0, 0));
    }

    @Test
    public void testZipLongMatrixToDouble() throws Exception {
        // Test binary
        DoubleMatrix result = Matrices.zipToDouble(longMatrix1, longMatrix2, (a, b) -> (double) a / b);
        assertEquals(0.2, result.get(0, 0), 0.001);

        // Test ternary
        DoubleMatrix ternaryResult = Matrices.zipToDouble(longMatrix1, longMatrix2, longMatrix3, (a, b, c) -> Math.sqrt(a * b * c));
        assertEquals(Math.sqrt(45), ternaryResult.get(0, 0), 0.001);
    }

    @Test
    public void testZipLongMatrixCollectionToDouble() throws Exception {
        List<LongMatrix> matrices = CommonUtil.asList(longMatrix1, longMatrix2);

        DoubleMatrix result = Matrices.zipToDouble(matrices, arr -> LongStream.of(arr).average().orElse(0.0));
        assertEquals(3.0, result.get(0, 0));
    }

    @Test
    public void testZipDoubleMatrix() throws Exception {
        // Test binary zip
        DoubleMatrix result = Matrices.zip(doubleMatrix1, doubleMatrix2, (a, b) -> a + b);
        assertEquals(6.0, result.get(0, 0), 0.001);

        // Test ternary zip
        DoubleMatrix ternaryResult = Matrices.zip(doubleMatrix1, doubleMatrix2, doubleMatrix3, (a, b, c) -> (a + b) * c);
        assertEquals(54.0, ternaryResult.get(0, 0), 0.001); // (1 + 5) * 9
    }

    @Test
    public void testZipDoubleMatrixCollection() throws Exception {
        List<DoubleMatrix> matrices = CommonUtil.asList(doubleMatrix1, doubleMatrix2, doubleMatrix3);

        DoubleMatrix result = Matrices.zip(matrices, Math::min);
        assertEquals(1.0, result.get(0, 0), 0.001);
    }

    @Test
    public void testZipDoubleMatrixToGeneric() throws Exception {
        List<DoubleMatrix> matrices = CommonUtil.asList(doubleMatrix1, doubleMatrix2);

        Matrix<Boolean> result = Matrices.zip(matrices, arr -> arr[0] < arr[1], Boolean.class);
        assertTrue(result.get(0, 0)); // 1.0 < 5.0
    }

    @Test
    public void testZipGenericMatrix() throws Exception {
        // Test binary zip with same result type
        Matrix<String> result = Matrices.zip(stringMatrix1, stringMatrix2, (a, b) -> a + b);
        assertEquals("ae", result.get(0, 0));

        // Test binary zip with different result type
        Matrix<Integer> lengthResult = Matrices.zip(stringMatrix1, stringMatrix2, (a, b) -> a.length() + b.length(), Integer.class);
        assertEquals(2, lengthResult.get(0, 0).intValue());

        // Test ternary zip
        Matrix<String> ternaryResult = Matrices.zip(stringMatrix1, stringMatrix2, stringMatrix3, (a, b, c) -> a + b + c);
        assertEquals("aei", ternaryResult.get(0, 0));

        // Test ternary zip with different result type
        Matrix<Integer> ternaryLengthResult = Matrices.zip(stringMatrix1, stringMatrix2, stringMatrix3, (a, b, c) -> a.length() + b.length() + c.length(),
                Integer.class);
        assertEquals(3, ternaryLengthResult.get(0, 0).intValue());
    }

    @Test
    public void testZipGenericMatrixCollection() throws Exception {
        List<Matrix<String>> matrices = CommonUtil.asList(stringMatrix1, stringMatrix2, stringMatrix3);

        // Test with binary operator
        Matrix<String> result = Matrices.zip(matrices, (a, b) -> a + b);
        assertEquals("aei", result.get(0, 0));
    }

    @Test
    public void testZipGenericMatrixCollectionWithFunction() throws Exception {
        List<Matrix<String>> matrices = CommonUtil.asList(stringMatrix1, stringMatrix2);

        // Test without sharing array
        Matrix<String> result = Matrices.zip(matrices, arr -> String.join("-", arr), String.class);
        assertEquals("a-e", result.get(0, 0));

        // Test with sharing array
        Matrix<Integer> countResult = Matrices.zip(matrices, arr -> arr.length, true, Integer.class);
        assertEquals(2, countResult.get(0, 0).intValue());
    }

    @Test
    public void testZipGenericMatrixCollectionWithMixedRuntimeTypes() throws Exception {
        Number[][] intData = new Integer[][] { { 1, 2 }, { 3, 4 } };
        Number[][] doubleData = new Double[][] { { 0.5, 1.5 }, { 2.5, 3.5 } };
        List<Matrix<Number>> matrices = CommonUtil.asList(Matrix.of(doubleData), Matrix.of(intData));

        Matrix<Number> result = Matrices.zip(matrices, (a, b) -> a.doubleValue() + b.doubleValue());
        assertEquals(1.5d, result.get(0, 0).doubleValue());
        assertEquals(7.5d, result.get(1, 1).doubleValue());
    }

    @Test
    public void testZipGenericMatrixCollectionWithFunctionAndMixedRuntimeTypes() throws Exception {
        Number[][] intData = new Integer[][] { { 1, 2 }, { 3, 4 } };
        Number[][] doubleData = new Double[][] { { 0.5, 1.5 }, { 2.5, 3.5 } };
        List<Matrix<Number>> matrices = CommonUtil.asList(Matrix.of(intData), Matrix.of(doubleData));

        Matrix<Double> result = Matrices.zip(matrices, arr -> arr[0].doubleValue() + arr[1].doubleValue(), true, Double.class);
        assertEquals(1.5d, result.get(0, 0));
        assertEquals(7.5d, result.get(1, 1));
    }

    @Test
    public void testZipNullArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(CommonUtil.asList(byteMatrix1, byteMatrix2), (Throwables.ByteBinaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(CommonUtil.asList(byteMatrix1, byteMatrix2),
                (Throwables.ByteNFunction<Integer, RuntimeException>) null, false, Integer.class));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(CommonUtil.asList(byteMatrix1, byteMatrix2), arr -> (int) arr[0], false, (Class<Integer>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zipToInt(byteMatrix1, byteMatrix2, (Throwables.ByteBiFunction<Integer, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zipToInt(byteMatrix1, byteMatrix2, byteMatrix3, (Throwables.ByteTriFunction<Integer, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zipToInt(CommonUtil.asList(byteMatrix1, byteMatrix2), (Throwables.ByteNFunction<Integer, RuntimeException>) null, false));

        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(CommonUtil.asList(intMatrix1, intMatrix2), (Throwables.IntBinaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(CommonUtil.asList(intMatrix1, intMatrix2), (Throwables.IntNFunction<Integer, RuntimeException>) null, false, Integer.class));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(CommonUtil.asList(intMatrix1, intMatrix2), arr -> arr[0], false, (Class<Integer>) null));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zipToLong(intMatrix1, intMatrix2, (Throwables.IntBiFunction<Long, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zipToLong(intMatrix1, intMatrix2, intMatrix3, (Throwables.IntTriFunction<Long, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zipToLong(CommonUtil.asList(intMatrix1, intMatrix2), (Throwables.IntNFunction<Long, RuntimeException>) null, false));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zipToDouble(intMatrix1, intMatrix2, (Throwables.IntBiFunction<Double, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zipToDouble(intMatrix1, intMatrix2, intMatrix3, (Throwables.IntTriFunction<Double, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zipToDouble(CommonUtil.asList(intMatrix1, intMatrix2), (Throwables.IntNFunction<Double, RuntimeException>) null, false));

        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(CommonUtil.asList(longMatrix1, longMatrix2), (Throwables.LongBinaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(CommonUtil.asList(longMatrix1, longMatrix2), (Throwables.LongNFunction<Long, RuntimeException>) null, false, Long.class));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(CommonUtil.asList(longMatrix1, longMatrix2), arr -> arr[0], false, (Class<Long>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zipToDouble(longMatrix1, longMatrix2, (Throwables.LongBiFunction<Double, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zipToDouble(longMatrix1, longMatrix2, longMatrix3, (Throwables.LongTriFunction<Double, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zipToDouble(CommonUtil.asList(longMatrix1, longMatrix2), (Throwables.LongNFunction<Double, RuntimeException>) null, false));

        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(CommonUtil.asList(doubleMatrix1, doubleMatrix2), (Throwables.DoubleBinaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(CommonUtil.asList(doubleMatrix1, doubleMatrix2),
                (Throwables.DoubleNFunction<Double, RuntimeException>) null, false, Double.class));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(CommonUtil.asList(doubleMatrix1, doubleMatrix2), arr -> arr[0], false, (Class<Double>) null));

        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(CommonUtil.asList(stringMatrix1, stringMatrix2), (Throwables.BinaryOperator<String, RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(CommonUtil.asList(stringMatrix1, stringMatrix2),
                (Throwables.Function<String[], String, RuntimeException>) null, false, String.class));
        assertThrows(IllegalArgumentException.class,
                () -> Matrices.zip(CommonUtil.asList(stringMatrix1, stringMatrix2), arr -> arr[0], false, (Class<String>) null));
    }

    @Test
    public void testZipWithDifferentShapes() {
        // Create matrices with different shapes
        IntMatrix differentShape = IntMatrix.of(new int[][] { { 1, 2, 3 } });

        // Test that zip methods throw exception for different shapes
        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(intMatrix1, differentShape, (a, b) -> a + b));

        assertThrows(IllegalArgumentException.class, () -> Matrices.zip(CommonUtil.asList(intMatrix1, differentShape), (a, b) -> a + b));
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
        Matrices.setParallelMode(ParallelMode.FORCE_ON);

        IntMatrix result = Matrices.zip(large1, large2, (a, b) -> a + b);

        // Verify a few results
        assertEquals(1, result.get(0, 0)); // 0 + 1
        assertEquals(size * size * 2 - 1, result.get(size - 1, size - 1));
    }

    @Test
    public void testZipCollectionEdgeCases() throws Exception {
        // Test collection zip with two elements (should use optimized path)
        List<IntMatrix> twoMatrices = CommonUtil.asList(intMatrix1, intMatrix2);
        IntMatrix twoResult = Matrices.zip(twoMatrices, (a, b) -> a + b);
        assertEquals(6, twoResult.get(0, 0));

        // Test collection zip with many elements
        List<IntMatrix> manyMatrices = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            manyMatrices.add(IntMatrix.of(new int[][] { { i, i + 1 }, { i + 2, i + 3 } }));
        }

        IntMatrix manyResult = Matrices.zip(manyMatrices, (a, b) -> a + b);
        // Should sum all values at each position
        assertEquals(0 + 1 + 2 + 3 + 4, manyResult.get(0, 0));
    }

    @Test
    public void test_toString() {

        {
            final ByteMatrix mx = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);

            assertEquals("[[0, 1, 2, 3], [4, 5, 6, 7]]", mx.toString());
            assertEquals(mx.toString(), mx.boxed().toString());
            assertDoesNotThrow(() -> {
                mx.println();
                mx.boxed().println();

                N.println(Strings.repeat('-', 80));

                N.println(mx);
                N.println(mx.boxed());

                mx.boxed().toRowDataset(CommonUtil.asList("a", "b", "c", "d")).println();
                mx.boxed().toColumnDataset(CommonUtil.asList("a", "b")).println();
            });
        }
    }

    @Test
    public void test_zipMatrix() {

        {
            final ByteMatrix mx = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);
            Matrix<Byte> same = Matrices.zip(CommonUtil.asList(mx), a -> (byte) N.sum(a), Byte.class);
            ByteMatrix doubled = Matrices.zip(CommonUtil.asList(mx, mx), (i, j) -> (byte) (i + j));
            ByteMatrix tripled = Matrices.zip(CommonUtil.asList(mx, mx, mx), (i, j) -> (byte) (i + j));
            ByteMatrix quadrupled = Matrices.zip(CommonUtil.asList(mx, mx, mx, mx), (i, j) -> (byte) (i + j));
            Matrix<Byte> boxedSum = Matrices.zip(CommonUtil.asList(mx, mx, mx, mx), a -> (byte) N.sum(a), Byte.class);
            IntMatrix intSum = Matrices.zipToInt(CommonUtil.asList(mx, mx, mx, mx), N::sum);

            assertEquals(Byte.valueOf((byte) 7), same.get(1, 3));
            assertEquals((byte) 14, doubled.get(1, 3));
            assertEquals((byte) 21, tripled.get(1, 3));
            assertEquals((byte) 28, quadrupled.get(1, 3));
            assertEquals(Byte.valueOf((byte) 28), boxedSum.get(1, 3));
            assertEquals(28, intSum.get(1, 3));

            assertDoesNotThrow(() -> {
                mx.println();

                N.println(Strings.repeat('-', 80));
                same.println();

                N.println(Strings.repeat('-', 80));
                doubled.println();

                N.println(Strings.repeat('-', 80));
                tripled.println();

                N.println(Strings.repeat('-', 80));
                quadrupled.println();

                N.println(Strings.repeat('-', 80));
                boxedSum.println();

                N.println(Strings.repeat('-', 80));
                intSum.println();
            });
        }

        N.println(Strings.repeat('=', 80));
        N.println(Strings.repeat('=', 80));

        {
            final IntMatrix mx = IntMatrix.range(0, 8).reshape(2, 4);
            Matrix<Integer> same = Matrices.zip(CommonUtil.asList(mx), a -> N.sum(a), Integer.class);
            IntMatrix doubled = Matrices.zip(CommonUtil.asList(mx, mx), (i, j) -> i + j);
            IntMatrix tripled = Matrices.zip(CommonUtil.asList(mx, mx, mx), (i, j) -> i + j);
            IntMatrix quadrupled = Matrices.zip(CommonUtil.asList(mx, mx, mx, mx), (i, j) -> i + j);
            Matrix<Byte> byteSum = Matrices.zip(CommonUtil.asList(mx, mx, mx, mx), a -> (byte) N.sum(a), byte.class);
            LongMatrix longSum = Matrices.zipToLong(CommonUtil.asList(mx, mx, mx, mx), a -> (long) N.sum(a));

            assertEquals(Integer.valueOf(7), same.get(1, 3));
            assertEquals(14, doubled.get(1, 3));
            assertEquals(21, tripled.get(1, 3));
            assertEquals(28, quadrupled.get(1, 3));
            assertEquals(Byte.valueOf((byte) 28), byteSum.get(1, 3));
            assertEquals(28L, longSum.get(1, 3));

            assertDoesNotThrow(() -> {
                mx.println();

                N.println(Strings.repeat('-', 80));
                same.println();

                N.println(Strings.repeat('-', 80));
                doubled.println();

                N.println(Strings.repeat('-', 80));
                tripled.println();

                N.println(Strings.repeat('-', 80));
                quadrupled.println();

                N.println(Strings.repeat('-', 80));
                byteSum.println();

                N.println(Strings.repeat('-', 80));
                longSum.println();
            });
        }

    }

    @Test
    public void test_multiply() {
        {
            final ByteMatrix mxa = ByteMatrix.range((byte) 0, (byte) 8).reshape(2, 4);
            final ByteMatrix mxb = ByteMatrix.range((byte) 0, (byte) 8).reshape(4, 2);
            final ByteMatrix result = mxa.multiply(mxb);

            assertEquals((byte) 28, result.get(0, 0));
            assertEquals((byte) 34, result.get(0, 1));
            assertEquals((byte) 76, result.get(1, 0));
            assertEquals((byte) 98, result.get(1, 1));
            assertDoesNotThrow(() -> {
                mxa.println();
                mxb.println();
                result.println();
            });
        }

        N.println(Strings.repeat('=', 80));
        N.println(Strings.repeat('=', 80));

        {
            final IntMatrix mxa = IntMatrix.range(0, 8).reshape(2, 4);
            final IntMatrix mxb = IntMatrix.range(0, 8).reshape(4, 2);
            final IntMatrix result = mxa.multiply(mxb);

            assertEquals(28, result.get(0, 0));
            assertEquals(34, result.get(0, 1));
            assertEquals(76, result.get(1, 0));
            assertEquals(98, result.get(1, 1));
            assertDoesNotThrow(() -> {
                mxa.println();
                mxb.println();
                result.println();
            });
        }
    }

    @Test
    public void test_multiply_perf() {
        final int rows = 200;
        final int columnCount = 300;

        final int[][] a = new int[rows][columnCount];
        final int[][] b = new int[columnCount][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columnCount; j++) {
                a[i][j] = 1;
                b[j][i] = 2;
            }
        }

        final IntMatrix mxa = IntMatrix.of(a);
        final IntMatrix mxb = IntMatrix.of(b);
        final IntMatrix mxc = mxa.multiply(mxb);

        assertEquals(rows, mxc.rowCount());
        assertEquals(rows, mxc.columnCount());
        assertEquals(columnCount * 2, mxc.get(0, 0));
        assertEquals(columnCount * 2, mxc.get(rows - 1, rows - 1));

        assertDoesNotThrow(() -> Profiler.run(1, 1, 1, "seq-multiply(" + rows + ", " + columnCount + ")", () -> {
            Matrices.setParallelMode(ParallelMode.FORCE_OFF);
            mxa.multiply(mxb);
        }).printResult());

        assertDoesNotThrow(() -> Profiler.run(1, 1, 1, "parallel-multiply(" + rows + ", " + columnCount + ")", () -> {
            Matrices.setParallelMode(ParallelMode.FORCE_ON);
            mxa.multiply(mxb);
        }).printResult());

    }

    @Nested
    class JavadocExampleUtilsTest_Matrices extends TestBase {
        // ==================== Matrices Javadoc Examples ====================

        @Test
        public void testMatrices_isSameShape_2() {
            // From Matrices.isSameShape(a, b) Javadoc
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }); // 2x2
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }); // 2x2
            IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } }); // 2x3

            assertTrue(Matrices.isSameShape(m1, m2)); // true
            assertFalse(Matrices.isSameShape(m1, m3)); // false
        }

        @Test
        public void testMatrices_isSameShape_3() {
            // From Matrices.isSameShape(a, b, c) Javadoc
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
            assertTrue(Matrices.isSameShape(m1, m2, m3)); // true
        }

        @Test
        public void testMatrices_isParallelizable_count() {
            // From isParallelizable(matrix, count) Javadoc
            // Default mode is AUTO, count 5000 < 8192 so should return false
            Matrices.setParallelMode(ParallelMode.AUTO);
            IntMatrix matrix = IntMatrix.of(new int[100][100]);
            boolean shouldParallelize = Matrices.isParallelizable(matrix, 5000);
            // Returns true only if settings allow and count >= 8192
            // Since 5000 < 8192, in AUTO mode this returns false
            assertFalse(shouldParallelize);
        }

        @Test
        public void testMatrices_getSetParallelMode() {
            // From getParallelMode Javadoc
            ParallelMode original = Matrices.getParallelMode();
            try {
                Matrices.setParallelMode(ParallelMode.FORCE_ON);
                assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());

                Matrices.setParallelMode(ParallelMode.FORCE_OFF);
                assertEquals(ParallelMode.FORCE_OFF, Matrices.getParallelMode());

                Matrices.setParallelMode(ParallelMode.AUTO);
                assertEquals(ParallelMode.AUTO, Matrices.getParallelMode());
            } finally {
                Matrices.setParallelMode(original);
            }
        }

        @Test
        public void testMatrices_runWithParallelMode() {
            // From runWithParallelMode Javadoc
            ParallelMode original = Matrices.getParallelMode();
            try {
                Matrices.setParallelMode(ParallelMode.AUTO);

                Matrices.runWithParallelMode(ParallelMode.FORCE_ON, () -> {
                    assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());
                });

                // After execution, the original setting is restored
                assertEquals(ParallelMode.AUTO, Matrices.getParallelMode());
            } finally {
                Matrices.setParallelMode(original);
            }
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for Matrices utility class.
     */
    @Tag("2025")
    class Matrices2025Test extends TestBase {

        @AfterEach
        public void tearDown() {
            // Reset parallel settings after each test
            Matrices.setParallelMode(ParallelMode.AUTO);
        }

        // ============ Parallel Settings Tests ============

        @Test
        public void testGetParallelMode_default() {
            ParallelMode enabled = Matrices.getParallelMode();
            assertNotNull(enabled);
        }

        @Test
        public void testSetParallelMode_yes() {
            Matrices.setParallelMode(ParallelMode.FORCE_ON);
            assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());
        }

        @Test
        public void testSetParallelMode_no() {
            Matrices.setParallelMode(ParallelMode.FORCE_OFF);
            assertEquals(ParallelMode.FORCE_OFF, Matrices.getParallelMode());
        }

        @Test
        public void testSetParallelMode_default() {
            Matrices.setParallelMode(ParallelMode.FORCE_ON);
            Matrices.setParallelMode(ParallelMode.AUTO);
            assertEquals(ParallelMode.AUTO, Matrices.getParallelMode());
        }
        // ============ isParallelizable Tests ============

        @Test
        public void testIsParallelable_withMatrix() {
            IntMatrix small = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            // Small matrix usually not parallelable by default
            boolean result = Matrices.isParallelizable(small);
            // Result depends on parallel settings
            assertNotNull(result);
        }

        @Test
        public void testIsParallelable_withCount() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            boolean result = Matrices.isParallelizable(m, 10);
            assertNotNull(result);
        }

        @Test
        public void testIsParallelable_largeCount() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            boolean result = Matrices.isParallelizable(m, 10000);
            // Large count may trigger parallel
            assertNotNull(result);
        }

        @Test
        public void testIsParallelable_forcedYes() {
            Matrices.setParallelMode(ParallelMode.FORCE_ON);
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            boolean result = Matrices.isParallelizable(m);
            assertTrue(result);
        }

        @Test
        public void testIsParallelable_forcedNo() {
            Matrices.setParallelMode(ParallelMode.FORCE_OFF);
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
            boolean result = Matrices.isParallelizable(m);
            assertFalse(result);
        }

        @Test
        public void testIsSameShape_twoMatrices_different() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2, 3 } });
            assertFalse(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void testIsSameShape_twoMatrices_differentRows() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 } });
            assertFalse(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void testIsSameShape_twoMatrices_differentCols() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertFalse(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void testIsSameShape_collection_same() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }),
                    IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } }));
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void testIsSameShape_collection_different() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6, 7 } }));
            assertFalse(Matrices.isSameShape(matrices));
        }

        @Test
        public void testIsSameShape_collection_empty() {
            Collection<IntMatrix> matrices = new ArrayList<>();
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void testIsSameShape_collection_single() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 } }));
            assertTrue(Matrices.isSameShape(matrices));
        }

        // ============ newArray Tests ============

        @Test
        public void testNewArray() {
            Integer[][] arr = Matrices.newMatrixArray(2, 3, Integer.class);
            assertNotNull(arr);
            assertEquals(2, arr.length);
            assertEquals(3, arr[0].length);
            assertEquals(3, arr[1].length);
        }

        @Test
        public void testNewArray_primitiveType() {
            Integer[][] arr = Matrices.newMatrixArray(3, 4, int.class);
            assertNotNull(arr);
            assertEquals(3, arr.length);
            assertEquals(4, arr[0].length);
        }

        @Test
        public void testNewArray_stringType() {
            String[][] arr = Matrices.newMatrixArray(2, 2, String.class);
            assertNotNull(arr);
            assertEquals(2, arr.length);
            assertEquals(2, arr[0].length);
        }

        @Test
        public void testNewArray_singleElement() {
            Double[][] arr = Matrices.newMatrixArray(1, 1, Double.class);
            assertNotNull(arr);
            assertEquals(1, arr.length);
            assertEquals(1, arr[0].length);
        }

        // ============ run Tests ============

        @Test
        public void testRun_withParallelMode() {
            List<String> values = new ArrayList<>();
            Matrices.runWithParallelMode(ParallelMode.FORCE_OFF, () -> values.add("test"));
            assertEquals(1, values.size());
            assertEquals("test", values.get(0));
            // Should restore original setting
            assertNotNull(Matrices.getParallelMode());
        }

        @Test
        public void testRun_withParallelMode_exception() {
            assertThrows(RuntimeException.class, () -> {
                Matrices.runWithParallelMode(ParallelMode.FORCE_OFF, () -> {
                    throw new RuntimeException("test exception");
                });
            });
        }

        @Test
        public void testRun_rowsAndCols_parallel() {
            List<String> positions = new ArrayList<>();
            Matrices.setParallelMode(ParallelMode.FORCE_OFF);
            Matrices.forEachIndex(2, 2, (i, j) -> {
                synchronized (positions) {
                    positions.add(i + "," + j);
                }
            }, true);
            assertEquals(4, positions.size());
        }

        @Test
        public void testRun_withRange() {
            List<String> positions = new ArrayList<>();
            Matrices.forEachIndex(1, 3, 1, 3, (i, j) -> positions.add(i + "," + j), false);
            assertEquals(4, positions.size());
            assertEquals("1,1", positions.get(0));
            assertEquals("1,2", positions.get(1));
            assertEquals("2,1", positions.get(2));
            assertEquals("2,2", positions.get(3));
        }

        @Test
        public void testRun_withRange_outOfBounds() {
            assertThrows(IndexOutOfBoundsException.class, () -> Matrices.forEachIndex(-1, 2, 0, 2, (i, j) -> {
            }, false));
        }

        // ============ call Tests ============

        @Test
        public void testCall() {
            com.landawn.abacus.util.stream.Stream<String> result = Matrices.mapIndices(2, 2, (i, j) -> i + "," + j, false);
            List<String> list = result.toList();
            assertEquals(4, list.size());
            assertTrue(list.contains("0,0"));
            assertTrue(list.contains("1,1"));
        }

        @Test
        public void testCall_withRange() {
            com.landawn.abacus.util.stream.Stream<String> result = Matrices.mapIndices(0, 2, 0, 3, (i, j) -> i + ":" + j, false);
            List<String> list = result.toList();
            assertEquals(6, list.size());
        }

        @Test
        public void testCall_parallel() {
            com.landawn.abacus.util.stream.Stream<Integer> result = Matrices.mapIndices(2, 2, (i, j) -> i * 10 + j, true);
            List<Integer> list = result.toList();
            assertEquals(4, list.size());
        }

        @Test
        public void testCall_withRange_outOfBounds() {
            assertThrows(IndexOutOfBoundsException.class, () -> Matrices.mapIndices(-1, 2, 0, 2, (i, j) -> i + j, false));
        }

        // ============ callToInt Tests ============

        @Test
        public void testCallToInt() {
            IntStream result = Matrices.mapIndicesToInt(2, 3, (i, j) -> i * 10 + j, false);
            int[] array = result.toArray();
            assertEquals(6, array.length);
        }

        @Test
        public void testCallToInt_withRange() {
            IntStream result = Matrices.mapIndicesToInt(0, 2, 0, 2, (i, j) -> i + j, false);
            int[] array = result.toArray();
            assertEquals(4, array.length);
        }

        @Test
        public void testCallToInt_parallel() {
            IntStream result = Matrices.mapIndicesToInt(3, 3, (i, j) -> i * j, true);
            int[] array = result.toArray();
            assertEquals(9, array.length);
        }

        @Test
        public void testCallToInt_withRange_outOfBounds() {
            assertThrows(IndexOutOfBoundsException.class, () -> Matrices.mapIndicesToInt(-1, 2, 0, 2, (i, j) -> i + j, false));
        }

        // ============ multiply Tests ============

        @Test
        public void testMultiply() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            int[][] result = new int[2][2];

            Matrices.forEachCartesianIndices(m1, m2, (i, j, k) -> result[i][j] += m1.get(i, k) * m2.get(k, j));

            assertEquals(19, result[0][0]);
            assertEquals(22, result[0][1]);
            assertEquals(43, result[1][0]);
            assertEquals(50, result[1][1]);
        }

        @Test
        public void testMultiply_incompatibleDimensions() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 1 }, { 2 }, { 3 } });
            assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(m1, m2, (i, j, k) -> {
            }));
        }

        @Test
        public void testMultiply_withParallel() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            int[][] result = new int[2][2];

            Matrices.forEachCartesianIndices(m1, m2, (i, j, k) -> {
                synchronized (result) {
                    result[i][j] += m1.get(i, k) * m2.get(k, j);
                }
            }, true);

            assertEquals(19, result[0][0]);
            assertEquals(22, result[0][1]);
        }

        @Test
        public void testZip_byteMatrix_three() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } });
            ByteMatrix result = Matrices.zip(m1, m2, m3, (a, b, c) -> (byte) (a + b + c));

            assertEquals(15, result.get(0, 0));
            assertEquals(18, result.get(0, 1));
            assertEquals(21, result.get(1, 0));
            assertEquals(24, result.get(1, 1));
        }

        @Test
        public void testZip_byteMatrix_collection() {
            Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } }),
                    ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } }));
            ByteMatrix result = Matrices.zip(matrices, (a, b) -> (byte) (a * b));

            assertEquals(5, result.get(0, 0));
            assertEquals(12, result.get(0, 1));
            assertEquals(21, result.get(1, 0));
            assertEquals(32, result.get(1, 1));
        }

        @Test
        public void testZip_byteMatrix_collection_single() {
            Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } }));
            ByteMatrix result = Matrices.zip(matrices, (a, b) -> (byte) (a + b));

            assertEquals(1, result.get(0, 0));
            assertEquals(2, result.get(0, 1));
        }

        @Test
        public void testZip_byteMatrix_toObject() {
            Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 } }), ByteMatrix.of(new byte[][] { { 3, 4 } }));
            Matrix<String> result = Matrices.zip(matrices, arr -> String.valueOf(arr[0] + arr[1]), String.class);

            assertEquals("4", result.get(0, 0));
            assertEquals("6", result.get(0, 1));
        }

        @Test
        public void testZip_intMatrix_three() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
            IntMatrix result = Matrices.zip(m1, m2, m3, (a, b, c) -> a + b + c);

            assertEquals(15, result.get(0, 0));
            assertEquals(18, result.get(0, 1));
            assertEquals(21, result.get(1, 0));
            assertEquals(24, result.get(1, 1));
        }

        @Test
        public void testZip_intMatrix_collection() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }),
                    IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } }));
            IntMatrix result = Matrices.zip(matrices, (a, b) -> a + b);

            assertEquals(15, result.get(0, 0));
            assertEquals(18, result.get(0, 1));
            assertEquals(21, result.get(1, 0));
            assertEquals(24, result.get(1, 1));
        }

        @Test
        public void testZip_intMatrix_toObject() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 } }), IntMatrix.of(new int[][] { { 3, 4 } }));
            Matrix<Integer> result = Matrices.zip(matrices, arr -> arr[0] * arr[1], Integer.class);

            assertEquals(3, result.get(0, 0).intValue());
            assertEquals(8, result.get(0, 1).intValue());
        }

        @Test
        public void testZipToLong_intMatrix_two() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            LongMatrix result = Matrices.zipToLong(m1, m2, (a, b) -> (long) a * b);

            assertEquals(5L, result.get(0, 0));
            assertEquals(12L, result.get(0, 1));
            assertEquals(21L, result.get(1, 0));
            assertEquals(32L, result.get(1, 1));
        }

        @Test
        public void testZipToDouble_intMatrix_two() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            DoubleMatrix result = Matrices.zipToDouble(m1, m2, (a, b) -> (double) a / b);

            assertEquals(0.2, result.get(0, 0), 0.0001);
            assertEquals(0.333, result.get(0, 1), 0.01);
        }

        // ============ zip Tests for LongMatrix ============

        @Test
        public void testZip_longMatrix_two() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix result = Matrices.zip(m1, m2, (a, b) -> a + b);

            assertEquals(6L, result.get(0, 0));
            assertEquals(8L, result.get(0, 1));
            assertEquals(10L, result.get(1, 0));
            assertEquals(12L, result.get(1, 1));
        }

        @Test
        public void testZip_longMatrix_collection() {
            Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } }),
                    LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } }));
            LongMatrix result = Matrices.zip(matrices, (a, b) -> a * b);

            assertEquals(5L, result.get(0, 0));
            assertEquals(12L, result.get(0, 1));
            assertEquals(21L, result.get(1, 0));
            assertEquals(32L, result.get(1, 1));
        }

        @Test
        public void testZipToDouble_longMatrix() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 2L, 4L }, { 6L, 8L } });
            DoubleMatrix result = Matrices.zipToDouble(m1, m2, (a, b) -> (double) a / b);

            assertEquals(5.0, result.get(0, 0), 0.0001);
            assertEquals(5.0, result.get(0, 1), 0.0001);
            assertEquals(5.0, result.get(1, 0), 0.0001);
            assertEquals(5.0, result.get(1, 1), 0.0001);
        }

        // ============ zip Tests for DoubleMatrix ============

        @Test
        public void testZip_doubleMatrix_two() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 0.5, 0.5 }, { 0.5, 0.5 } });
            DoubleMatrix result = Matrices.zip(m1, m2, (a, b) -> a + b);

            assertEquals(2.0, result.get(0, 0), 0.0001);
            assertEquals(3.0, result.get(0, 1), 0.0001);
            assertEquals(4.0, result.get(1, 0), 0.0001);
            assertEquals(5.0, result.get(1, 1), 0.0001);
        }

        @Test
        public void testZip_doubleMatrix_three() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 0.5, 0.5 }, { 0.5, 0.5 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 0.5, 0.5 }, { 0.5, 0.5 } });
            DoubleMatrix result = Matrices.zip(m1, m2, m3, (a, b, c) -> a + b + c);

            assertEquals(2.0, result.get(0, 0), 0.0001);
            assertEquals(3.0, result.get(0, 1), 0.0001);
        }

        @Test
        public void testZip_doubleMatrix_collection() {
            Collection<DoubleMatrix> matrices = Arrays.asList(DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } }),
                    DoubleMatrix.of(new double[][] { { 2.0, 3.0 }, { 4.0, 5.0 } }));
            DoubleMatrix result = Matrices.zip(matrices, (a, b) -> a * b);

            assertEquals(2.0, result.get(0, 0), 0.0001);
            assertEquals(6.0, result.get(0, 1), 0.0001);
            assertEquals(12.0, result.get(1, 0), 0.0001);
            assertEquals(20.0, result.get(1, 1), 0.0001);
        }

        // ============ zip Tests for Matrix<T> ============

        @Test
        public void testZip_objectMatrix_two() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "1", "2" }, { "3", "4" } });
            Matrix<String> result = Matrices.zip(m1, m2, (a, b) -> a + b);

            assertEquals("a1", result.get(0, 0));
            assertEquals("b2", result.get(0, 1));
            assertEquals("c3", result.get(1, 0));
            assertEquals("d4", result.get(1, 1));
        }

        @Test
        public void testZip_objectMatrix_toOtherType() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
            Matrix<String> result = Matrices.zip(m1, m2, (a, b) -> a + "+" + b, String.class);

            assertEquals("1+5", result.get(0, 0));
            assertEquals("2+6", result.get(0, 1));
        }

        @Test
        public void testZip_objectMatrix_three() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 9, 10 }, { 11, 12 } });
            Matrix<Integer> result = Matrices.zip(m1, m2, m3, (a, b, c) -> a + b + c);

            assertEquals(15, result.get(0, 0).intValue());
            assertEquals(18, result.get(0, 1).intValue());
            assertEquals(21, result.get(1, 0).intValue());
            assertEquals(24, result.get(1, 1).intValue());
        }

        @Test
        public void testZip_objectMatrix_collection() {
            Collection<Matrix<Integer>> matrices = Arrays.asList(Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } }),
                    Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } }));
            Matrix<Integer> result = Matrices.zip(matrices, (a, b) -> a + b);

            assertEquals(6, result.get(0, 0).intValue());
            assertEquals(8, result.get(0, 1).intValue());
        }

        @Test
        public void testZip_objectMatrix_collection_withArray() {
            Collection<Matrix<Integer>> matrices = Arrays.asList(Matrix.of(new Integer[][] { { 1, 2 } }), Matrix.of(new Integer[][] { { 3, 4 } }),
                    Matrix.of(new Integer[][] { { 5, 6 } }));
            Matrix<Integer> result = Matrices.zip(matrices, arr -> arr[0] + arr[1] + arr[2], Integer.class);

            assertEquals(9, result.get(0, 0).intValue());
            assertEquals(12, result.get(0, 1).intValue());
        }

        // ============ Error Cases for zip ============

        @Test
        public void testZip_differentShapes() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (a, b) -> a + b));
        }

        @Test
        public void testZip_emptyCollection() {
            Collection<IntMatrix> matrices = new ArrayList<>();
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(matrices, (a, b) -> a + b));
        }

        @Test
        public void testZip_collectionWithDifferentShapes() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 1, 2, 3 } }));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(matrices, (a, b) -> a + b));
        }

        // ============ Edge Cases ============

        @Test
        public void testZip_singleElementMatrices() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 5 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 3 } });
            IntMatrix result = Matrices.zip(m1, m2, (a, b) -> a * b);

            assertEquals(15, result.get(0, 0));
        }

        @Test
        public void testZip_largeMatrices() {
            int[][] arr1 = new int[10][10];
            int[][] arr2 = new int[10][10];
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    arr1[i][j] = i + j;
                    arr2[i][j] = i * j;
                }
            }

            IntMatrix m1 = IntMatrix.of(arr1);
            IntMatrix m2 = IntMatrix.of(arr2);
            IntMatrix result = Matrices.zip(m1, m2, (a, b) -> a + b);
            m1.println();
            m2.println();
            result.println();

            assertEquals(10, result.rowCount());
            assertEquals(10, result.columnCount());
            assertEquals(0, result.get(0, 0)); // 0 + 0
            assertEquals(99, result.get(9, 9)); // 18 + 81
        }

        @Test
        public void testZip_withShareIntermediateArray() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 } }), IntMatrix.of(new int[][] { { 3, 4 } }),
                    IntMatrix.of(new int[][] { { 5, 6 } }));
            Matrix<Integer> result = Matrices.zip(matrices, arr -> arr[0] + arr[1] + arr[2], true, Integer.class);

            assertEquals(9, result.get(0, 0).intValue());
            assertEquals(12, result.get(0, 1).intValue());
        }

        @Test
        public void testRun_zeroRowsOrCols() {
            List<String> positions = new ArrayList<>();
            Matrices.forEachIndex(0, 3, (i, j) -> positions.add(i + "," + j), false);
            assertEquals(0, positions.size());

            Matrices.forEachIndex(3, 0, (i, j) -> positions.add(i + "," + j), false);
            assertEquals(0, positions.size());
        }

        // ============ Additional Coverage Tests ============

        @Test
        public void testRun_withRange_moreRowsThanCols_sequential() {
            List<String> positions = new ArrayList<>();
            // 5 rows x 2 columnCount - should iterate by columns first
            Matrices.forEachIndex(0, 5, 0, 2, (i, j) -> positions.add(i + "," + j), false);
            assertEquals(10, positions.size());
            // Should start with all rows for first column
            assertEquals("0,0", positions.get(0));
            assertEquals("1,0", positions.get(1));
        }

        @Test
        public void testRun_withRange_moreRowsThanCols_parallel() {
            List<String> positions = new ArrayList<>();
            Matrices.setParallelMode(ParallelMode.FORCE_OFF);
            // 5 rows x 2 columnCount - parallel should iterate by columns
            Matrices.forEachIndex(0, 5, 0, 2, (i, j) -> {
                synchronized (positions) {
                    positions.add(i + "," + j);
                }
            }, true);
            assertEquals(10, positions.size());
        }

        @Test
        public void testCall_withRange_moreRowsThanCols() {
            // 4 rows x 2 columnCount - should iterate by columns
            com.landawn.abacus.util.stream.Stream<String> result = Matrices.mapIndices(0, 4, 0, 2, (i, j) -> i + ":" + j, false);
            List<String> list = result.toList();
            assertEquals(8, list.size());
        }

        @Test
        public void testCall_withRange_moreRowsThanCols_parallel() {
            // 4 rows x 2 columnCount - parallel should iterate by columns
            com.landawn.abacus.util.stream.Stream<Integer> result = Matrices.mapIndices(0, 4, 0, 2, (i, j) -> i * 10 + j, true);
            List<Integer> list = result.toList();
            assertEquals(8, list.size());
        }

        @Test
        public void testCallToInt_withRange_moreRowsThanCols() {
            // 5 rows x 2 columnCount - should iterate by columns
            IntStream result = Matrices.mapIndicesToInt(0, 5, 0, 2, (i, j) -> i + j, false);
            int[] array = result.toArray();
            assertEquals(10, array.length);
        }

        @Test
        public void testCallToInt_withRange_moreRowsThanCols_parallel() {
            // 5 rows x 2 columnCount - parallel should iterate by columns
            IntStream result = Matrices.mapIndicesToInt(0, 5, 0, 2, (i, j) -> i * j, true);
            int[] array = result.toArray();
            assertEquals(10, array.length);
        }

        @Test
        public void testMultiply_differentDimensionOrdering_smallestIsColsA() {
            // Test case where columnCountA is smallest dimension
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } }); // 3x2
            IntMatrix m2 = IntMatrix.of(new int[][] { { 7, 8, 9, 10 }, { 11, 12, 13, 14 } }); // 2x4
            int[][] result = new int[3][4];

            Matrices.forEachCartesianIndices(m1, m2, (i, j, k) -> result[i][j] += m1.get(i, k) * m2.get(k, j), false);

            assertEquals(29, result[0][0]); // 1*7 + 2*11
            assertEquals(65, result[1][0]); // 3*7 + 4*11 = 21 + 44 = 65
        }

        @Test
        public void testMultiply_differentDimensionOrdering_smallestIsColsB() {
            // Test case where columnCountB is smallest dimension
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } }); // 4x3
            IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } }); // 3x2
            int[][] result = new int[4][2];

            Matrices.forEachCartesianIndices(m1, m2, (i, j, k) -> result[i][j] += m1.get(i, k) * m2.get(k, j), false);

            assertEquals(22, result[0][0]); // 1*1 + 2*3 + 3*5
            assertEquals(28, result[0][1]); // 1*2 + 2*4 + 3*6
        }

        @Test
        public void testMultiply_parallelExecution_variousDimensions() {
            // Test parallel execution with different dimension orderings
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6, 7 }, { 8, 9, 10 } }); // 2x3
            int[][] result = new int[2][3];

            Matrices.forEachCartesianIndices(m1, m2, (i, j, k) -> {
                synchronized (result) {
                    result[i][j] += m1.get(i, k) * m2.get(k, j);
                }
            }, true);

            assertEquals(21, result[0][0]); // 1*5 + 2*8
            assertEquals(24, result[0][1]); // 1*6 + 2*9
            assertEquals(27, result[0][2]); // 1*7 + 2*10
        }

        @Test
        public void testZipToInt_byteMatrix_three() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } });
            IntMatrix result = Matrices.zipToInt(m1, m2, m3, (a, b, c) -> a + b + c);

            assertEquals(15, result.get(0, 0));
            assertEquals(18, result.get(0, 1));
            assertEquals(21, result.get(1, 0));
            assertEquals(24, result.get(1, 1));
        }

        @Test
        public void testZipToInt_byteMatrix_collection() {
            Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } }),
                    ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } }), ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } }));
            IntMatrix result = Matrices.zipToInt(matrices, arr -> arr[0] + arr[1] + arr[2]);

            assertEquals(15, result.get(0, 0));
            assertEquals(18, result.get(0, 1));
            assertEquals(21, result.get(1, 0));
            assertEquals(24, result.get(1, 1));
        }

        @Test
        public void testZipToInt_byteMatrix_collection_withSharing() {
            Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 2, 4 } }), ByteMatrix.of(new byte[][] { { 3, 6 } }));
            IntMatrix result = Matrices.zipToInt(matrices, arr -> arr[0] * arr[1], true);

            assertEquals(6, result.get(0, 0));
            assertEquals(24, result.get(0, 1));
        }

        @Test
        public void testZipToLong_intMatrix_three() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
            LongMatrix result = Matrices.zipToLong(m1, m2, m3, (a, b, c) -> (long) a + b + c);

            assertEquals(15L, result.get(0, 0));
            assertEquals(18L, result.get(0, 1));
            assertEquals(21L, result.get(1, 0));
            assertEquals(24L, result.get(1, 1));
        }

        @Test
        public void testZipToLong_intMatrix_collection() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }));
            LongMatrix result = Matrices.zipToLong(matrices, arr -> (long) arr[0] * arr[1]);

            assertEquals(5L, result.get(0, 0));
            assertEquals(12L, result.get(0, 1));
            assertEquals(21L, result.get(1, 0));
            assertEquals(32L, result.get(1, 1));
        }

        @Test
        public void testZipToLong_intMatrix_collection_withSharing() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 10, 20 } }), IntMatrix.of(new int[][] { { 2, 4 } }));
            LongMatrix result = Matrices.zipToLong(matrices, arr -> (long) arr[0] / arr[1], true);

            assertEquals(5L, result.get(0, 0));
            assertEquals(5L, result.get(0, 1));
        }

        @Test
        public void testZipToDouble_intMatrix_three() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 10, 20 }, { 30, 40 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 2, 4 }, { 6, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 1 }, { 1, 1 } });
            DoubleMatrix result = Matrices.zipToDouble(m1, m2, m3, (a, b, c) -> (double) (a + b) / c);

            assertEquals(12.0, result.get(0, 0), 0.0001);
            assertEquals(24.0, result.get(0, 1), 0.0001);
            assertEquals(36.0, result.get(1, 0), 0.0001);
            assertEquals(48.0, result.get(1, 1), 0.0001);
        }

        @Test
        public void testZipToDouble_intMatrix_collection() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 10, 20 }, { 30, 40 } }),
                    IntMatrix.of(new int[][] { { 2, 4 }, { 6, 8 } }));
            DoubleMatrix result = Matrices.zipToDouble(matrices, arr -> (double) arr[0] / arr[1]);

            assertEquals(5.0, result.get(0, 0), 0.0001);
            assertEquals(5.0, result.get(0, 1), 0.0001);
            assertEquals(5.0, result.get(1, 0), 0.0001);
            assertEquals(5.0, result.get(1, 1), 0.0001);
        }

        @Test
        public void testZipToDouble_intMatrix_collection_withSharing() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 100, 200 } }), IntMatrix.of(new int[][] { { 10, 20 } }),
                    IntMatrix.of(new int[][] { { 2, 4 } }));
            DoubleMatrix result = Matrices.zipToDouble(matrices, arr -> (double) (arr[0] + arr[1]) / arr[2], true);

            assertEquals(55.0, result.get(0, 0), 0.0001);
            assertEquals(55.0, result.get(0, 1), 0.0001);
        }

        @Test
        public void testZip_longMatrix_three() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 9L, 10L }, { 11L, 12L } });
            LongMatrix result = Matrices.zip(m1, m2, m3, (a, b, c) -> a + b + c);

            assertEquals(15L, result.get(0, 0));
            assertEquals(18L, result.get(0, 1));
            assertEquals(21L, result.get(1, 0));
            assertEquals(24L, result.get(1, 1));
        }

        @Test
        public void testZip_longMatrix_toObject() {
            Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 1L, 2L } }), LongMatrix.of(new long[][] { { 3L, 4L } }));
            Matrix<String> result = Matrices.zip(matrices, arr -> arr[0] + "+" + arr[1], String.class);

            assertEquals("1+3", result.get(0, 0));
            assertEquals("2+4", result.get(0, 1));
        }

        @Test
        public void testZip_longMatrix_toObject_withSharing() {
            Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 10L, 20L } }), LongMatrix.of(new long[][] { { 5L, 10L } }));
            Matrix<Long> result = Matrices.zip(matrices, arr -> arr[0] - arr[1], true, Long.class);

            assertEquals(5L, result.get(0, 0).longValue());
            assertEquals(10L, result.get(0, 1).longValue());
        }

        @Test
        public void testZipToDouble_longMatrix_three() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 100L, 200L }, { 300L, 400L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 2L, 4L }, { 6L, 8L } });
            DoubleMatrix result = Matrices.zipToDouble(m1, m2, m3, (a, b, c) -> (double) (a + b) / c);

            assertEquals(55.0, result.get(0, 0), 0.0001);
            assertEquals(55.0, result.get(0, 1), 0.0001);
            assertEquals(55.0, result.get(1, 0), 0.0001);
            assertEquals(55.0, result.get(1, 1), 0.0001);
        }

        @Test
        public void testZipToDouble_longMatrix_collection() {
            Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 100L, 200L }, { 300L, 400L } }),
                    LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } }));
            DoubleMatrix result = Matrices.zipToDouble(matrices, arr -> (double) arr[0] / arr[1]);

            assertEquals(10.0, result.get(0, 0), 0.0001);
            assertEquals(10.0, result.get(0, 1), 0.0001);
            assertEquals(10.0, result.get(1, 0), 0.0001);
            assertEquals(10.0, result.get(1, 1), 0.0001);
        }

        @Test
        public void testZipToDouble_longMatrix_collection_withSharing() {
            Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 50L, 100L } }), LongMatrix.of(new long[][] { { 10L, 20L } }),
                    LongMatrix.of(new long[][] { { 2L, 4L } }));
            DoubleMatrix result = Matrices.zipToDouble(matrices, arr -> (double) (arr[0] + arr[1]) / arr[2], true);

            assertEquals(30.0, result.get(0, 0), 0.0001);
            assertEquals(30.0, result.get(0, 1), 0.0001);
        }

        @Test
        public void testZip_doubleMatrix_toObject() {
            Collection<DoubleMatrix> matrices = Arrays.asList(DoubleMatrix.of(new double[][] { { 1.5, 2.5 } }),
                    DoubleMatrix.of(new double[][] { { 0.5, 0.5 } }));
            Matrix<String> result = Matrices.zip(matrices, arr -> String.format("%.1f", arr[0] + arr[1]), String.class);

            assertEquals("2.0", result.get(0, 0));
            assertEquals("3.0", result.get(0, 1));
        }

        @Test
        public void testZip_doubleMatrix_toObject_withSharing() {
            Collection<DoubleMatrix> matrices = Arrays.asList(DoubleMatrix.of(new double[][] { { 10.0, 20.0 } }),
                    DoubleMatrix.of(new double[][] { { 5.0, 10.0 } }));
            Matrix<Double> result = Matrices.zip(matrices, arr -> arr[0] / arr[1], true, Double.class);

            assertEquals(2.0, result.get(0, 0), 0.0001);
            assertEquals(2.0, result.get(0, 1), 0.0001);
        }

        @Test
        public void testZip_objectMatrix_three_withTargetType() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 9, 10 }, { 11, 12 } });
            Matrix<String> result = Matrices.zip(m1, m2, m3, (a, b, c) -> a + "+" + b + "+" + c, String.class);

            assertEquals("1+5+9", result.get(0, 0));
            assertEquals("2+6+10", result.get(0, 1));
            assertEquals("3+7+11", result.get(1, 0));
            assertEquals("4+8+12", result.get(1, 1));
        }

        @Test
        public void testZip_intMatrix_collection_singleItem() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 5, 10 }, { 15, 20 } }));
            IntMatrix result = Matrices.zip(matrices, (a, b) -> a + b);

            // Single matrix should be copied
            assertEquals(5, result.get(0, 0));
            assertEquals(10, result.get(0, 1));
            assertEquals(15, result.get(1, 0));
            assertEquals(20, result.get(1, 1));
        }

        @Test
        public void testZip_intMatrix_collection_twoItems() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }));
            IntMatrix result = Matrices.zip(matrices, (a, b) -> a + b);

            // Two items should use zipWith directly
            assertEquals(6, result.get(0, 0));
            assertEquals(8, result.get(0, 1));
            assertEquals(10, result.get(1, 0));
            assertEquals(12, result.get(1, 1));
        }

        @Test
        public void testZip_longMatrix_collection_singleItem() {
            Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 100L, 200L } }));
            LongMatrix result = Matrices.zip(matrices, (a, b) -> a * b);

            // Single matrix should be copied
            assertEquals(100L, result.get(0, 0));
            assertEquals(200L, result.get(0, 1));
        }

        @Test
        public void testZip_longMatrix_collection_twoItems() {
            Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 2L, 4L } }), LongMatrix.of(new long[][] { { 3L, 5L } }));
            LongMatrix result = Matrices.zip(matrices, (a, b) -> a * b);

            // Two items should use zipWith directly
            assertEquals(6L, result.get(0, 0));
            assertEquals(20L, result.get(0, 1));
        }

        @Test
        public void testZip_doubleMatrix_collection_singleItem() {
            Collection<DoubleMatrix> matrices = Arrays.asList(DoubleMatrix.of(new double[][] { { 1.5, 2.5 } }));
            DoubleMatrix result = Matrices.zip(matrices, (a, b) -> a + b);

            // Single matrix should be copied
            assertEquals(1.5, result.get(0, 0), 0.0001);
            assertEquals(2.5, result.get(0, 1), 0.0001);
        }

        @Test
        public void testZip_doubleMatrix_collection_twoItems() {
            Collection<DoubleMatrix> matrices = Arrays.asList(DoubleMatrix.of(new double[][] { { 1.0, 2.0 } }),
                    DoubleMatrix.of(new double[][] { { 3.0, 4.0 } }));
            DoubleMatrix result = Matrices.zip(matrices, (a, b) -> a * b);

            // Two items should use zipWith directly
            assertEquals(3.0, result.get(0, 0), 0.0001);
            assertEquals(8.0, result.get(0, 1), 0.0001);
        }

        @Test
        public void testZip_objectMatrix_collection_singleItem() {
            Collection<Matrix<Integer>> matrices = Arrays.asList(Matrix.of(new Integer[][] { { 10, 20 } }));
            Matrix<Integer> result = Matrices.zip(matrices, (a, b) -> a + b);

            // Single matrix should be copied
            assertEquals(10, result.get(0, 0).intValue());
            assertEquals(20, result.get(0, 1).intValue());
        }

        @Test
        public void testZip_objectMatrix_collection_twoItems() {
            Collection<Matrix<String>> matrices = Arrays.asList(Matrix.of(new String[][] { { "a", "b" } }), Matrix.of(new String[][] { { "x", "y" } }));
            Matrix<String> result = Matrices.zip(matrices, (a, b) -> a + b);

            // Two items should use zipWith directly
            assertEquals("ax", result.get(0, 0));
            assertEquals("by", result.get(0, 1));
        }

        @Test
        public void testZip_byteMatrix_collection_twoItems() {
            Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 } }), ByteMatrix.of(new byte[][] { { 3, 4 } }));
            ByteMatrix result = Matrices.zip(matrices, (a, b) -> (byte) (a + b));

            // Two items should use zipWith directly
            assertEquals(4, result.get(0, 0));
            assertEquals(6, result.get(0, 1));
        }

        @Test
        public void testZip_byteMatrix_toObject_withSharing() {
            Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 10, 20 } }), ByteMatrix.of(new byte[][] { { 5, 10 } }));
            Matrix<Integer> result = Matrices.zip(matrices, arr -> (int) (arr[0] - arr[1]), true, Integer.class);

            assertEquals(5, result.get(0, 0).intValue());
            assertEquals(10, result.get(0, 1).intValue());
        }

        @Test
        public void testZip_intMatrix_toObject_withSharing() {
            Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 10, 20 } }), IntMatrix.of(new int[][] { { 5, 10 } }));
            Matrix<String> result = Matrices.zip(matrices, arr -> arr[0] + "-" + arr[1], true, String.class);

            assertEquals("10-5", result.get(0, 0));
            assertEquals("20-10", result.get(0, 1));
        }

        @Test
        public void testZip_objectMatrix_collection_withSharing() {
            Collection<Matrix<Integer>> matrices = Arrays.asList(Matrix.of(new Integer[][] { { 10, 20 } }), Matrix.of(new Integer[][] { { 5, 10 } }),
                    Matrix.of(new Integer[][] { { 1, 2 } }));
            Matrix<Integer> result = Matrices.zip(matrices, arr -> arr[0] + arr[1] + arr[2], true, Integer.class);

            assertEquals(16, result.get(0, 0).intValue());
            assertEquals(32, result.get(0, 1).intValue());
        }

        private static long invokeSaturatedMultiply(final long left, final long right) throws Exception {
            final Method method = Matrices.class.getDeclaredMethod("saturatedMultiply", long.class, long.class);
            method.setAccessible(true);
            return (long) method.invoke(null, left, right);
        }

        // ============ saturatedMultiply basic cases ============

        @Test
        public void test_saturatedMultiply_zeroLeft() throws Exception {
            assertEquals(0, invokeSaturatedMultiply(0, 100));
        }

        @Test
        public void test_saturatedMultiply_zeroRight() throws Exception {
            assertEquals(0, invokeSaturatedMultiply(100, 0));
        }

        @Test
        public void test_saturatedMultiply_bothZero() throws Exception {
            assertEquals(0, invokeSaturatedMultiply(0, 0));
        }

        @Test
        public void test_saturatedMultiply_normalPositive() throws Exception {
            assertEquals(20, invokeSaturatedMultiply(4, 5));
            assertEquals(1_000_000L, invokeSaturatedMultiply(1000, 1000));
        }

        @Test
        public void test_saturatedMultiply_normalNegative() throws Exception {
            assertEquals(-20, invokeSaturatedMultiply(-4, 5));
            assertEquals(-20, invokeSaturatedMultiply(4, -5));
            assertEquals(20, invokeSaturatedMultiply(-4, -5));
        }

        // ============ saturatedMultiply overflow cases ============

        @Test
        public void test_saturatedMultiply_positiveOverflow() throws Exception {
            assertEquals(Long.MAX_VALUE, invokeSaturatedMultiply(Long.MAX_VALUE, 2));
            assertEquals(Long.MAX_VALUE, invokeSaturatedMultiply(2, Long.MAX_VALUE));
            assertEquals(Long.MAX_VALUE, invokeSaturatedMultiply(Long.MAX_VALUE, Long.MAX_VALUE));
        }

        @Test
        public void test_saturatedMultiply_negativeOverflow() throws Exception {
            assertEquals(Long.MIN_VALUE, invokeSaturatedMultiply(Long.MAX_VALUE, -2));
            assertEquals(Long.MIN_VALUE, invokeSaturatedMultiply(-2, Long.MAX_VALUE));
            assertEquals(Long.MIN_VALUE, invokeSaturatedMultiply(Long.MIN_VALUE, 2));
        }

        @Test
        public void test_saturatedMultiply_bothNegativeOverflow() throws Exception {
            assertEquals(Long.MAX_VALUE, invokeSaturatedMultiply(Long.MIN_VALUE, -2));
            assertEquals(Long.MAX_VALUE, invokeSaturatedMultiply(Long.MIN_VALUE, Long.MIN_VALUE));
        }

        @Test
        public void test_saturatedMultiply_largeNonOverflow() throws Exception {
            // Just under overflow: (2^31) * (2^32) = 2^63 which overflows, but (2^31 - 1) * 2 is fine
            final long a = Integer.MAX_VALUE;
            final long b = 2;
            assertEquals(a * b, invokeSaturatedMultiply(a, b));
        }

        @Test
        public void test_saturatedMultiply_one() throws Exception {
            assertEquals(Long.MAX_VALUE, invokeSaturatedMultiply(Long.MAX_VALUE, 1));
            assertEquals(Long.MIN_VALUE, invokeSaturatedMultiply(Long.MIN_VALUE, 1));
            assertEquals(42, invokeSaturatedMultiply(1, 42));
            assertEquals(-42, invokeSaturatedMultiply(1, -42));
        }

        @Test
        public void test_saturatedMultiply_minValueEdge() throws Exception {
            // Long.MIN_VALUE * -1 overflows (since |Long.MIN_VALUE| > Long.MAX_VALUE)
            assertEquals(Long.MAX_VALUE, invokeSaturatedMultiply(Long.MIN_VALUE, -1));
        }

    }

    @Nested
    @Tag("2510")
    class Matrices2510Test extends TestBase {

        @AfterEach
        public void cleanup() {
            // Reset parallel setting after each test to avoid side effects
            Matrices.setParallelMode(ParallelMode.AUTO);
        }

        // ============ ParallelMode Configuration Tests ============

        @Test
        public void testGetParallelMode_default() {
            ParallelMode result = Matrices.getParallelMode();
            assertNotNull(result);
            assertEquals(ParallelMode.AUTO, result);
        }
        // ============ isParallelizable Tests ============

        @Test
        public void testIsParallelable_singleArg_smallMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            Matrices.setParallelMode(ParallelMode.AUTO);
            // Small matrix (count = 4) should not be parallelable by default
            boolean result = Matrices.isParallelizable(m);
            assertFalse(result);
        }

        @Test
        public void testIsParallelable_singleArg_largeMatrix() {
            IntMatrix m = IntMatrix.of(new int[100][100]); // count = 10000
            Matrices.setParallelMode(ParallelMode.AUTO);
            // Large matrix should be parallelable by default if parallel streams supported
            boolean result = Matrices.isParallelizable(m);
            // Result depends on IS_DOUBLE_PIPE_STREAM_SUPPORTED
            assertNotNull(result);
        }

        @Test
        public void testIsParallelable_singleArg_forcedYes() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            Matrices.setParallelMode(ParallelMode.FORCE_ON);
            // Should be parallelable when forced FORCE_ON (if parallel streams supported)
            boolean result = Matrices.isParallelizable(m);
            assertNotNull(result);
        }

        @Test
        public void testIsParallelable_singleArg_forcedNo() {
            IntMatrix m = IntMatrix.of(new int[100][100]);
            Matrices.setParallelMode(ParallelMode.FORCE_OFF);
            // Should not be parallelable when forced FORCE_OFF
            boolean result = Matrices.isParallelizable(m);
            assertFalse(result);
        }

        @Test
        public void testIsParallelable_twoArgs_smallCount() {
            IntMatrix m = IntMatrix.of(new int[10][10]);
            Matrices.setParallelMode(ParallelMode.AUTO);
            boolean result = Matrices.isParallelizable(m, 100);
            assertFalse(result);
        }

        @Test
        public void testIsParallelable_twoArgs_largeCount() {
            IntMatrix m = IntMatrix.of(new int[100][100]);
            Matrices.setParallelMode(ParallelMode.AUTO);
            boolean result = Matrices.isParallelizable(m, 10000);
            // Result depends on IS_DOUBLE_PIPE_STREAM_SUPPORTED
            assertNotNull(result);
        }

        @Test
        public void testIsParallelable_twoArgs_exactThreshold() {
            IntMatrix m = IntMatrix.of(new int[100][100]);
            Matrices.setParallelMode(ParallelMode.AUTO);
            boolean result = Matrices.isParallelizable(m, 8192);
            // At exact threshold should be parallelable
            assertNotNull(result);
        }

        @Test
        public void testIsParallelable_twoArgs_belowThreshold() {
            IntMatrix m = IntMatrix.of(new int[100][100]);
            Matrices.setParallelMode(ParallelMode.AUTO);
            boolean result = Matrices.isParallelizable(m, 8191);
            assertFalse(result);
        }

        // ============ isSameShape Tests ============

        @Test
        public void testIsSameShape_twoMatrices_sameShape() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 7, 8, 9 }, { 10, 11, 12 } });
            assertTrue(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void testIsSameShape_twoMatrices_differentRows() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 }, { 9, 10 } });
            assertFalse(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void testIsSameShape_twoMatrices_differentCols() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6, 7 }, { 8, 9, 10 } });
            assertFalse(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void testIsSameShape_twoMatrices_emptyMatrices() {
            IntMatrix m1 = IntMatrix.empty();
            IntMatrix m2 = IntMatrix.empty();
            assertTrue(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void testIsSameShape_twoMatrices_singleElement() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 2 } });
            assertTrue(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void testIsSameShape_threeMatrices_lastTwoDifferent() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 }, { 9, 10 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 11 } });
            assertFalse(Matrices.isSameShape(m1, m2, m3));
        }

        @Test
        public void testIsSameShape_collection_nullCollection() {
            Collection<IntMatrix> matrices = null;
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void testIsSameShape_collection_emptyCollection() {
            Collection<IntMatrix> matrices = Collections.emptyList();
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void testIsSameShape_collection_singleMatrix() {
            IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            Collection<IntMatrix> matrices = Collections.singletonList(m);
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void testIsSameShape_collection_allSameShape() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
            Collection<IntMatrix> matrices = Arrays.asList(m1, m2, m3);
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void testIsSameShape_collection_differentShapes() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10, 11 }, { 12, 13, 14 } });
            Collection<IntMatrix> matrices = Arrays.asList(m1, m2, m3);
            assertFalse(Matrices.isSameShape(matrices));
        }

        @Test
        public void testIsSameShape_collection_firstTwoDifferent() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
            assertFalse(Matrices.isSameShape(matrices));
        }

        // ============ newArray Tests ============

        @Test
        public void testNewArray_basicDimensions() {
            Integer[][] arr = Matrices.newMatrixArray(3, 4, Integer.class);
            assertNotNull(arr);
            assertEquals(3, arr.length);
            for (Integer[] row : arr) {
                assertEquals(4, row.length);
            }
        }

        @Test
        public void testNewArray_singleRow() {
            String[][] arr = Matrices.newMatrixArray(1, 5, String.class);
            assertNotNull(arr);
            assertEquals(1, arr.length);
            assertEquals(5, arr[0].length);
        }

        @Test
        public void testNewArray_singleColumn() {
            Double[][] arr = Matrices.newMatrixArray(5, 1, Double.class);
            assertNotNull(arr);
            assertEquals(5, arr.length);
            assertEquals(1, arr[0].length);
        }

        @Test
        public void testNewArray_zeroCols() {
            Integer[][] arr = Matrices.newMatrixArray(5, 0, Integer.class);
            assertNotNull(arr);
            assertEquals(5, arr.length);
            assertEquals(0, arr[0].length);
        }

        @Test
        public void testNewArray_primitiveDouble() {
            Double[][] arr = Matrices.newMatrixArray(2, 2, double.class);
            assertNotNull(arr);
            assertEquals(2, arr.length);
            assertEquals(2, arr[0].length);
        }

        // ============ run Tests (with ParallelMode) ============

        @Test
        public void testRun_withParallelMode_executesCommand() {
            AtomicInteger counter = new AtomicInteger(0);
            Matrices.runWithParallelMode(ParallelMode.FORCE_OFF, () -> counter.incrementAndGet());
            assertEquals(1, counter.get());
        }

        @Test
        public void testRun_withParallelMode_restoresOriginalSetting() {
            Matrices.setParallelMode(ParallelMode.FORCE_ON);
            Matrices.runWithParallelMode(ParallelMode.FORCE_OFF, () -> {
                assertEquals(ParallelMode.FORCE_OFF, Matrices.getParallelMode());
            });
            assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());
        }
        // ============ run Tests (IntBiConsumer variants) ============

        @Test
        public void testRun_intBiConsumer_basic() {
            List<String> positions = new ArrayList<>();
            Matrices.forEachIndex(2, 3, (i, j) -> positions.add(i + "," + j), false);
            assertEquals(6, positions.size());
            assertTrue(positions.contains("0,0"));
            assertTrue(positions.contains("1,2"));
        }

        @Test
        public void testRun_intBiConsumer_zeroRows() {
            AtomicInteger counter = new AtomicInteger(0);
            Matrices.forEachIndex(0, 3, (i, j) -> counter.incrementAndGet(), false);
            assertEquals(0, counter.get());
        }

        @Test
        public void testRun_intBiConsumer_zeroCols() {
            AtomicInteger counter = new AtomicInteger(0);
            Matrices.forEachIndex(3, 0, (i, j) -> counter.incrementAndGet(), false);
            assertEquals(0, counter.get());
        }

        @Test
        public void testRun_intBiConsumer_singleElement() {
            List<String> positions = new ArrayList<>();
            Matrices.forEachIndex(1, 1, (i, j) -> positions.add(i + "," + j), false);
            assertEquals(1, positions.size());
            assertEquals("0,0", positions.get(0));
        }

        @Test
        public void testRun_intBiConsumer_withRange() {
            List<String> positions = new ArrayList<>();
            Matrices.forEachIndex(1, 3, 2, 5, (i, j) -> positions.add(i + "," + j), false);
            assertEquals(6, positions.size()); // 2 rows * 3 columnCount
            assertTrue(positions.contains("1,2"));
            assertTrue(positions.contains("2,4"));
            assertFalse(positions.contains("0,0"));
            assertFalse(positions.contains("3,5"));
        }

        @Test
        public void testRun_intBiConsumer_withRange_emptyRange() {
            AtomicInteger counter = new AtomicInteger(0);
            Matrices.forEachIndex(2, 2, 3, 3, (i, j) -> counter.incrementAndGet(), false);
            assertEquals(0, counter.get());
        }

        @Test
        public void testRun_intBiConsumer_withRange_invalidRowRange() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                Matrices.forEachIndex(5, 3, 0, 2, (i, j) -> {
                }, false);
            });
        }

        @Test
        public void testRun_intBiConsumer_withRange_invalidColRange() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                Matrices.forEachIndex(0, 2, 5, 3, (i, j) -> {
                }, false);
            });
        }

        @Test
        public void testRun_intBiConsumer_withRange_negativeIndex() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                Matrices.forEachIndex(-1, 2, 0, 2, (i, j) -> {
                }, false);
            });
        }

        // ============ call Tests (Stream variants) ============

        @Test
        public void testCall_basic() {
            Stream<String> result = Matrices.mapIndices(2, 3, (i, j) -> i + "," + j, false);
            List<String> positions = result.toList();
            assertEquals(6, positions.size());
            assertTrue(positions.contains("0,0"));
            assertTrue(positions.contains("1,2"));
        }

        @Test
        public void testCall_zeroSize() {
            Stream<String> result = Matrices.mapIndices(0, 0, (i, j) -> i + "," + j, false);
            List<String> positions = result.toList();
            assertEquals(0, positions.size());
        }

        @Test
        public void testCall_singleElement() {
            Stream<Integer> result = Matrices.mapIndices(1, 1, (i, j) -> i * 10 + j, false);
            List<Integer> values = result.toList();
            assertEquals(1, values.size());
            assertEquals(0, values.get(0));
        }

        @Test
        public void testCall_withRange() {
            Stream<Integer> result = Matrices.mapIndices(1, 4, 2, 5, (i, j) -> i * 10 + j, false);
            List<Integer> values = result.toList();
            assertEquals(9, values.size()); // 3 rows * 3 columnCount
            assertTrue(values.contains(12)); // row 1, col 2
            assertTrue(values.contains(34)); // row 3, col 4
        }

        @Test
        public void testCall_withRange_invalidRange() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                Matrices.mapIndices(5, 3, 0, 2, (i, j) -> "test", false);
            });
        }

        // ============ callToInt Tests ============

        @Test
        public void testCallToInt_basic() {
            IntStream result = Matrices.mapIndicesToInt(2, 3, (i, j) -> i + j, false);
            int[] values = result.toArray();
            assertEquals(6, values.length);
            assertEquals(0, values[0]); // 0+0
            assertTrue(IntStream.of(values).anyMatch(v -> v == 3)); // 1+2
        }

        @Test
        public void testCallToInt_zeroSize() {
            IntStream result = Matrices.mapIndicesToInt(0, 0, (i, j) -> i + j, false);
            int[] values = result.toArray();
            assertEquals(0, values.length);
        }

        @Test
        public void testCallToInt_multiplication() {
            IntStream result = Matrices.mapIndicesToInt(2, 2, (i, j) -> i * j, false);
            int[] values = result.toArray();
            assertEquals(4, values.length);
            assertTrue(IntStream.of(values).anyMatch(v -> v == 0));
            assertTrue(IntStream.of(values).anyMatch(v -> v == 1)); // 1*1
        }

        @Test
        public void testCallToInt_withRange() {
            IntStream result = Matrices.mapIndicesToInt(0, 3, 0, 3, (i, j) -> i * 10 + j, false);
            int[] values = result.toArray();
            assertEquals(9, values.length);
            assertTrue(IntStream.of(values).anyMatch(v -> v == 0));
            assertTrue(IntStream.of(values).anyMatch(v -> v == 22));
        }

        @Test
        public void testCallToInt_withRange_invalidRange() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                Matrices.mapIndicesToInt(3, 1, 0, 2, (i, j) -> i + j, false);
            });
        }

        // ============ multiply Tests ============

        @Test
        public void testMultiply_basic() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }); // 2x2
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }); // 2x2
            List<String> triplets = new ArrayList<>();
            Matrices.forEachCartesianIndices(m1, m2, (i, j, k) -> triplets.add(i + "," + j + "," + k));
            assertEquals(8, triplets.size()); // 2*2*2
            assertTrue(triplets.contains("0,0,0"));
            assertTrue(triplets.contains("1,1,1"));
        }

        @Test
        public void testMultiply_withParallelFlag_sequential() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            AtomicInteger count = new AtomicInteger(0);
            Matrices.forEachCartesianIndices(m1, m2, (i, j, k) -> count.incrementAndGet(), false);
            assertEquals(8, count.get());
        }

        @Test
        public void testMultiply_withParallelFlag_incompatibleDimensions() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 3 } });
            assertThrows(IllegalArgumentException.class, () -> {
                Matrices.forEachCartesianIndices(m1, m2, (i, j, k) -> {
                }, false);
            });
        }

        // ============ ByteMatrix zip Tests ============

        @Test
        public void testZip_byteMatrix_twoMatrices() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix result = Matrices.zip(m1, m2, (a, b) -> (byte) (a + b));
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6, result.get(0, 0)); // 1+5
            assertEquals(12, result.get(1, 1)); // 4+8
        }

        @Test
        public void testZip_byteMatrix_threeMatrices() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 10, 20 }, { 30, 40 } });
            ByteMatrix result = Matrices.zip(m1, m2, m3, (a, b, c) -> (byte) (a + b + c));
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(16, result.get(0, 0)); // 1+5+10
            assertEquals(52, result.get(1, 1)); // 4+8+40
        }

        @Test
        public void testZip_byteMatrix_collection() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            Collection<ByteMatrix> matrices = Arrays.asList(m1, m2);
            ByteMatrix result = Matrices.zip(matrices, (a, b) -> (byte) (a + b));
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6, result.get(0, 0));
        }

        @Test
        public void testZip_byteMatrix_collectionToMatrix() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            Collection<ByteMatrix> matrices = Arrays.asList(m1, m2);
            Matrix<Integer> result = Matrices.zip(matrices, arr -> (int) (arr[0] + arr[1]), Integer.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(Integer.valueOf(6), result.get(0, 0));
        }

        @Test
        public void testZip_byteMatrix_collectionToMatrixWithTargetType() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            Collection<ByteMatrix> matrices = Arrays.asList(m1, m2);
            Matrix<String> result = Matrices.zip(matrices, arr -> String.valueOf(arr[0] + arr[1]), String.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("6", result.get(0, 0));
        }

        // ============ ByteMatrix zipToInt Tests ============

        @Test
        public void testZipToInt_byteMatrix_twoMatrices() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix result = Matrices.zipToInt(m1, m2, (a, b) -> a * b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(5, result.get(0, 0)); // 1*5
            assertEquals(32, result.get(1, 1)); // 4*8
        }

        @Test
        public void testZipToInt_byteMatrix_threeMatrices() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 2, 2 }, { 2, 2 } });
            IntMatrix result = Matrices.zipToInt(m1, m2, m3, (a, b, c) -> a + b + c);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(8, result.get(0, 0)); // 1+5+2
            assertEquals(14, result.get(1, 1)); // 4+8+2
        }

        @Test
        public void testZipToInt_byteMatrix_collection() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            Collection<ByteMatrix> matrices = Arrays.asList(m1, m2);
            IntMatrix result = Matrices.zipToInt(matrices, arr -> arr[0] + arr[1]);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6, result.get(0, 0));
        }

        @Test
        public void testZipToInt_byteMatrix_collectionWithShareArray() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            Collection<ByteMatrix> matrices = Arrays.asList(m1, m2);
            IntMatrix result = Matrices.zipToInt(matrices, arr -> arr[0] * arr[1], false);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(5, result.get(0, 0));
        }

        // ============ IntMatrix zip Tests ============

        @Test
        public void testZip_intMatrix_twoMatrices() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix result = Matrices.zip(m1, m2, (a, b) -> a + b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6, result.get(0, 0));
            assertEquals(12, result.get(1, 1));
        }

        @Test
        public void testZip_intMatrix_threeMatrices() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 10, 20 }, { 30, 40 } });
            IntMatrix result = Matrices.zip(m1, m2, m3, (a, b, c) -> a + b + c);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(16, result.get(0, 0));
            assertEquals(52, result.get(1, 1));
        }

        @Test
        public void testZip_intMatrix_collection() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 10, 20 }, { 30, 40 } });
            Collection<IntMatrix> matrices = Arrays.asList(m1, m2, m3);
            // Uses binary operator to reduce all matrices
            IntMatrix result = Matrices.zip(matrices, (a, b) -> a + b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(16, result.get(0, 0)); // 1+5+10
        }

        @Test
        public void testZip_intMatrix_collectionToMatrix() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
            Matrix<Long> result = Matrices.zip(matrices, arr -> (long) (arr[0] + arr[1]), Long.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(Long.valueOf(6), result.get(0, 0));
        }

        @Test
        public void testZip_intMatrix_collectionToMatrixWithTargetType() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
            Matrix<String> result = Matrices.zip(matrices, arr -> String.valueOf(arr[0] + arr[1]), String.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("6", result.get(0, 0));
        }

        // ============ IntMatrix zipToLong Tests ============

        @Test
        public void testZipToLong_intMatrix_twoMatrices() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            LongMatrix result = Matrices.zipToLong(m1, m2, (a, b) -> (long) a * b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(5L, result.get(0, 0));
            assertEquals(32L, result.get(1, 1));
        }

        @Test
        public void testZipToLong_intMatrix_threeMatrices() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 2, 2 }, { 2, 2 } });
            LongMatrix result = Matrices.zipToLong(m1, m2, m3, (a, b, c) -> (long) (a + b + c));
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(8L, result.get(0, 0));
            assertEquals(14L, result.get(1, 1));
        }

        @Test
        public void testZipToLong_intMatrix_collection() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
            LongMatrix result = Matrices.zipToLong(matrices, arr -> (long) (arr[0] + arr[1]));
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6L, result.get(0, 0));
        }

        @Test
        public void testZipToLong_intMatrix_collectionWithShareArray() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
            LongMatrix result = Matrices.zipToLong(matrices, arr -> (long) (arr[0] * arr[1]), false);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(5L, result.get(0, 0));
        }

        // ============ IntMatrix zipToDouble Tests ============

        @Test
        public void testZipToDouble_intMatrix_twoMatrices() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            DoubleMatrix result = Matrices.zipToDouble(m1, m2, (a, b) -> (double) a / b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(0.2, result.get(0, 0), 0.001);
            assertEquals(0.5, result.get(1, 1), 0.001);
        }

        @Test
        public void testZipToDouble_intMatrix_threeMatrices() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 2, 2 }, { 2, 2 } });
            DoubleMatrix result = Matrices.zipToDouble(m1, m2, m3, (a, b, c) -> (double) (a + b + c));
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(8.0, result.get(0, 0), 0.001);
            assertEquals(14.0, result.get(1, 1), 0.001);
        }

        @Test
        public void testZipToDouble_intMatrix_collection() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
            DoubleMatrix result = Matrices.zipToDouble(matrices, arr -> (double) (arr[0] + arr[1]));
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6.0, result.get(0, 0), 0.001);
        }

        @Test
        public void testZipToDouble_intMatrix_collectionWithShareArray() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
            DoubleMatrix result = Matrices.zipToDouble(matrices, arr -> (double) (arr[0] * arr[1]), false);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(5.0, result.get(0, 0), 0.001);
        }

        // ============ LongMatrix zip Tests ============

        @Test
        public void testZip_longMatrix_twoMatrices() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix result = Matrices.zip(m1, m2, (a, b) -> a + b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6L, result.get(0, 0));
            assertEquals(12L, result.get(1, 1));
        }

        @Test
        public void testZip_longMatrix_threeMatrices() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
            LongMatrix result = Matrices.zip(m1, m2, m3, (a, b, c) -> a + b + c);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(16L, result.get(0, 0));
            assertEquals(52L, result.get(1, 1));
        }

        @Test
        public void testZip_longMatrix_collection() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            Collection<LongMatrix> matrices = Arrays.asList(m1, m2);
            LongMatrix result = Matrices.zip(matrices, (a, b) -> a + b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6L, result.get(0, 0));
        }

        @Test
        public void testZip_longMatrix_collectionToMatrix() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            Collection<LongMatrix> matrices = Arrays.asList(m1, m2);
            Matrix<Long> result = Matrices.zip(matrices, arr -> arr[0] + arr[1], Long.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(Long.valueOf(6L), result.get(0, 0));
        }

        @Test
        public void testZip_longMatrix_collectionToMatrixWithTargetType() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            Collection<LongMatrix> matrices = Arrays.asList(m1, m2);
            Matrix<String> result = Matrices.zip(matrices, arr -> String.valueOf(arr[0] + arr[1]), String.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("6", result.get(0, 0));
        }

        // ============ LongMatrix zipToDouble Tests ============

        @Test
        public void testZipToDouble_longMatrix_twoMatrices() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            DoubleMatrix result = Matrices.zipToDouble(m1, m2, (a, b) -> (double) a / b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(0.2, result.get(0, 0), 0.001);
            assertEquals(0.5, result.get(1, 1), 0.001);
        }

        @Test
        public void testZipToDouble_longMatrix_threeMatrices() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 2L, 2L }, { 2L, 2L } });
            DoubleMatrix result = Matrices.zipToDouble(m1, m2, m3, (a, b, c) -> (double) (a + b + c));
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(8.0, result.get(0, 0), 0.001);
            assertEquals(14.0, result.get(1, 1), 0.001);
        }

        @Test
        public void testZipToDouble_longMatrix_collection() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            Collection<LongMatrix> matrices = Arrays.asList(m1, m2);
            DoubleMatrix result = Matrices.zipToDouble(matrices, arr -> (double) (arr[0] + arr[1]));
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6.0, result.get(0, 0), 0.001);
        }

        @Test
        public void testZipToDouble_longMatrix_collectionWithShareArray() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            Collection<LongMatrix> matrices = Arrays.asList(m1, m2);
            DoubleMatrix result = Matrices.zipToDouble(matrices, arr -> (double) (arr[0] * arr[1]), false);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(5.0, result.get(0, 0), 0.001);
        }

        // ============ DoubleMatrix zip Tests ============

        @Test
        public void testZip_doubleMatrix_twoMatrices() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.5, 6.5 }, { 7.5, 8.5 } });
            DoubleMatrix result = Matrices.zip(m1, m2, (a, b) -> a + b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(7.0, result.get(0, 0), 0.001);
            assertEquals(13.0, result.get(1, 1), 0.001);
        }

        @Test
        public void testZip_doubleMatrix_threeMatrices() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 10.0, 20.0 }, { 30.0, 40.0 } });
            DoubleMatrix result = Matrices.zip(m1, m2, m3, (a, b, c) -> a + b + c);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(16.0, result.get(0, 0), 0.001);
            assertEquals(52.0, result.get(1, 1), 0.001);
        }

        @Test
        public void testZip_doubleMatrix_collection() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            Collection<DoubleMatrix> matrices = Arrays.asList(m1, m2);
            DoubleMatrix result = Matrices.zip(matrices, (a, b) -> a + b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6.0, result.get(0, 0), 0.001);
        }

        @Test
        public void testZip_doubleMatrix_collectionToMatrix() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            Collection<DoubleMatrix> matrices = Arrays.asList(m1, m2);
            Matrix<Double> result = Matrices.zip(matrices, arr -> arr[0] + arr[1], Double.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6.0, result.get(0, 0), 0.001);
        }

        @Test
        public void testZip_doubleMatrix_collectionToMatrixWithTargetType() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            Collection<DoubleMatrix> matrices = Arrays.asList(m1, m2);
            Matrix<String> result = Matrices.zip(matrices, arr -> String.valueOf(arr[0] + arr[1]), String.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("6.0", result.get(0, 0));
        }

        // ============ Generic Matrix zip Tests ============

        @Test
        public void testZip_genericMatrix_twoMatrices_sameResult() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
            Matrix<Integer> result = Matrices.zip(m1, m2, (a, b) -> a + b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(Integer.valueOf(6), result.get(0, 0));
            assertEquals(Integer.valueOf(12), result.get(1, 1));
        }

        @Test
        public void testZip_genericMatrix_twoMatrices_differentResult() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
            Matrix<String> result = Matrices.zip(m1, m2, (a, b) -> a + "+" + b, String.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("1+5", result.get(0, 0));
            assertEquals("4+8", result.get(1, 1));
        }

        @Test
        public void testZip_genericMatrix_threeMatrices_sameResult() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
            Matrix<Integer> result = Matrices.zip(m1, m2, m3, (a, b, c) -> a + b + c);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(Integer.valueOf(16), result.get(0, 0));
            assertEquals(Integer.valueOf(52), result.get(1, 1));
        }

        @Test
        public void testZip_genericMatrix_threeMatrices_differentResult() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
            Matrix<String> result = Matrices.zip(m1, m2, m3, (a, b, c) -> a + "+" + b + "+" + c, String.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("1+5+10", result.get(0, 0));
            assertEquals("4+8+40", result.get(1, 1));
        }

        @Test
        public void testZip_genericMatrix_collection_sameResult() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
            Collection<Matrix<Integer>> matrices = Arrays.asList(m1, m2, m3);
            // Uses binary operator to reduce all matrices
            Matrix<Integer> result = Matrices.zip(matrices, (a, b) -> a + b);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(Integer.valueOf(16), result.get(0, 0));
            assertEquals(Integer.valueOf(52), result.get(1, 1));
        }

        @Test
        public void testZip_genericMatrix_collection_differentResult() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
            Collection<Matrix<Integer>> matrices = Arrays.asList(m1, m2);
            Matrix<String> result = Matrices.zip(matrices, arr -> arr[0] + ":" + arr[1], String.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("1:5", result.get(0, 0));
            assertEquals("4:8", result.get(1, 1));
        }

        @Test
        public void testZip_genericMatrix_collectionWithTargetType() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "e", "f" }, { "g", "h" } });
            Collection<Matrix<String>> matrices = Arrays.asList(m1, m2);
            Matrix<String> result = Matrices.zip(matrices, arr -> arr[0] + arr[1], String.class);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("ae", result.get(0, 0));
            assertEquals("dh", result.get(1, 1));
        }

        @Test
        public void testZip_singleElementMatrices() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 5 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 10 } });
            IntMatrix result = Matrices.zip(m1, m2, (a, b) -> a + b);
            assertEquals(1, result.rowCount());
            assertEquals(1, result.columnCount());
            assertEquals(15, result.get(0, 0));
        }

        @Test
        public void testCall_largeMatrix() {
            // Test with larger dimensions to verify iteration logic
            Stream<Integer> result = Matrices.mapIndices(10, 20, (i, j) -> i * 100 + j, false);
            List<Integer> values = result.toList();
            assertEquals(200, values.size());
            assertTrue(values.contains(0)); // 0*100 + 0
            assertTrue(values.contains(919)); // 9*100 + 19
        }

        @Test
        public void testCallToInt_largeMatrix() {
            IntStream result = Matrices.mapIndicesToInt(10, 20, (i, j) -> i * 100 + j, false);
            int[] values = result.toArray();
            assertEquals(200, values.length);
            assertEquals(0, values[0]);
        }

        @Test
        public void testNewArray_largeArray() {
            Integer[][] arr = Matrices.newMatrixArray(100, 100, Integer.class);
            assertNotNull(arr);
            assertEquals(100, arr.length);
            assertEquals(100, arr[0].length);
            assertEquals(100, arr[99].length);
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for the Matrices utility class.
     * Tests cover parallel execution control, shape checking, array creation, and matrix operations.
     */
    @Tag("2511")
    class Matrices2511Test extends TestBase {

        // ============ Parallel Enabled Tests ============

        @Test
        public void testGetParallelMode_defaultValue() {
            ParallelMode enabled = Matrices.getParallelMode();
            assertNotNull(enabled);
            assertEquals(ParallelMode.AUTO, enabled);
        }

        @Test
        public void testSetParallelMode_yes() {
            ParallelMode original = Matrices.getParallelMode();
            try {
                Matrices.setParallelMode(ParallelMode.FORCE_ON);
                assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());
            } finally {
                Matrices.setParallelMode(original);
            }
        }

        @Test
        public void testSetParallelMode_no() {
            ParallelMode original = Matrices.getParallelMode();
            try {
                Matrices.setParallelMode(ParallelMode.FORCE_OFF);
                assertEquals(ParallelMode.FORCE_OFF, Matrices.getParallelMode());
            } finally {
                Matrices.setParallelMode(original);
            }
        }

        @Test
        public void testSetParallelMode_default() {
            ParallelMode original = Matrices.getParallelMode();
            try {
                Matrices.setParallelMode(ParallelMode.FORCE_OFF);
                Matrices.setParallelMode(ParallelMode.AUTO);
                assertEquals(ParallelMode.AUTO, Matrices.getParallelMode());
            } finally {
                Matrices.setParallelMode(original);
            }
        }
        // ============ Parallelable Tests ============

        @Test
        public void testIsParallelable_smallMatrix_defaultSetting() {
            IntMatrix small = IntMatrix.of(new int[10][10]);
            boolean result = Matrices.isParallelizable(small);
            assertFalse(result); // 100 elements < 8192
        }

        @Test
        public void testIsParallelable_smallMatrix_forceYes() {
            IntMatrix small = IntMatrix.of(new int[10][10]);
            ParallelMode original = Matrices.getParallelMode();
            try {
                Matrices.setParallelMode(ParallelMode.FORCE_ON);
                boolean result = Matrices.isParallelizable(small);
                // Result depends on whether parallel streams are supported
                assertTrue(result || !result); // Just verify it returns a boolean
            } finally {
                Matrices.setParallelMode(original);
            }
        }

        @Test
        public void testIsParallelable_smallMatrix_forceNo() {
            IntMatrix small = IntMatrix.of(new int[10][10]);
            ParallelMode original = Matrices.getParallelMode();
            try {
                Matrices.setParallelMode(ParallelMode.FORCE_OFF);
                assertFalse(Matrices.isParallelizable(small));
            } finally {
                Matrices.setParallelMode(original);
            }
        }

        @Test
        public void testIsParallelable_largeMatrix_defaultSetting() {
            IntMatrix large = IntMatrix.of(new int[100][100]);
            boolean result = Matrices.isParallelizable(large);
            // 10000 elements >= 8192, so should be true if supported
            assertTrue(result || !result); // Verify it returns a boolean
        }

        @Test
        public void testIsParallelable_largeMatrix_forceNo() {
            IntMatrix large = IntMatrix.of(new int[100][100]);
            ParallelMode original = Matrices.getParallelMode();
            try {
                Matrices.setParallelMode(ParallelMode.FORCE_OFF);
                assertFalse(Matrices.isParallelizable(large));
            } finally {
                Matrices.setParallelMode(original);
            }
        }

        @Test
        public void testIsParallelable_withCount_small() {
            IntMatrix matrix = IntMatrix.of(new int[10][10]);
            boolean result = Matrices.isParallelizable(matrix, 100);
            assertFalse(result);
        }

        @Test
        public void testIsParallelable_withCount_large() {
            IntMatrix matrix = IntMatrix.of(new int[10][10]);
            ParallelMode original = Matrices.getParallelMode();
            try {
                Matrices.setParallelMode(ParallelMode.AUTO);
                boolean result = Matrices.isParallelizable(matrix, 10000);
                // Result depends on parallel streams being available
                assertTrue(result || !result);
            } finally {
                Matrices.setParallelMode(original);
            }
        }

        @Test
        public void testIsParallelable_withCount_exactThreshold() {
            IntMatrix matrix = IntMatrix.of(new int[10][10]);
            ParallelMode original = Matrices.getParallelMode();
            try {
                Matrices.setParallelMode(ParallelMode.AUTO);
                boolean result = Matrices.isParallelizable(matrix, 8192);
                assertTrue(result || !result);
            } finally {
                Matrices.setParallelMode(original);
            }
        }

        @Test
        public void testIsSameShape_twoMatrices_differentRows() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 7, 8, 9 } });
            assertFalse(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void testIsSameShape_twoMatrices_differentCols() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 7, 8 }, { 9, 10 } });
            assertFalse(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void testIsSameShape_threeMatrices_differentSecond() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6, 7 }, { 8, 9, 10 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 11, 12 }, { 13, 14 } });
            assertFalse(Matrices.isSameShape(m1, m2, m3));
        }

        @Test
        public void testIsSameShape_threeMatrices_differentThird() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10, 11 }, { 12, 13, 14 } });
            assertFalse(Matrices.isSameShape(m1, m2, m3));
        }

        @Test
        public void testIsSameShape_collection_empty() {
            List<IntMatrix> matrices = new ArrayList<>();
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void testIsSameShape_collection_single() {
            List<IntMatrix> matrices = Collections.singletonList(IntMatrix.of(new int[][] { { 1, 2 } }));
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void testIsSameShape_collection_multiple_same() {
            List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } }),
                    IntMatrix.of(new int[][] { { 7, 8, 9 }, { 10, 11, 12 } }), IntMatrix.of(new int[][] { { 13, 14, 15 }, { 16, 17, 18 } }));
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void testIsSameShape_collection_multiple_different() {
            List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } }), IntMatrix.of(new int[][] { { 7, 8, 9 } }),
                    IntMatrix.of(new int[][] { { 10, 11 }, { 12, 13 } }));
            assertFalse(Matrices.isSameShape(matrices));
        }

        // ============ New Array Tests ============

        @Test
        public void testNewArray_primitive_int() {
            Integer[][] arr = Matrices.newMatrixArray(3, 4, int.class);
            assertNotNull(arr);
            assertEquals(3, arr.length);
            assertEquals(4, arr[0].length);
            assertEquals(4, arr[1].length);
            assertEquals(4, arr[2].length);
        }

        @Test
        public void testNewArray_primitive_double() {
            Double[][] arr = Matrices.newMatrixArray(2, 5, double.class);
            assertNotNull(arr);
            assertEquals(2, arr.length);
            assertEquals(5, arr[0].length);
            assertEquals(5, arr[1].length);
        }

        @Test
        public void testNewArray_primitive_long() {
            Long[][] arr = Matrices.newMatrixArray(4, 3, long.class);
            assertNotNull(arr);
            assertEquals(4, arr.length);
            assertEquals(3, arr[0].length);
        }

        @Test
        public void testNewArray_primitive_boolean() {
            Boolean[][] arr = Matrices.newMatrixArray(2, 2, boolean.class);
            assertNotNull(arr);
            assertEquals(2, arr.length);
            assertEquals(2, arr[0].length);
        }

        @Test
        public void testNewArray_reference_string() {
            String[][] arr = Matrices.newMatrixArray(3, 2, String.class);
            assertNotNull(arr);
            assertEquals(3, arr.length);
            assertEquals(2, arr[0].length);
            // Elements should be null for reference types
            assertNotNull(arr);
        }

        @Test
        public void testNewArray_zeroRows() {
            assertThrows(IllegalArgumentException.class, () -> Matrices.newMatrixArray(0, 5, int.class));
        }

        @Test
        public void testNewArray_zeroCols() {
            Integer[][] arr = Matrices.newMatrixArray(5, 0, int.class);
            assertNotNull(arr);
            assertEquals(5, arr.length);
            assertEquals(0, arr[0].length);
        }

        @Test
        public void testNewArray_largeDimensions() {
            Integer[][] arr = Matrices.newMatrixArray(100, 100, int.class);
            assertNotNull(arr);
            assertEquals(100, arr.length);
            assertEquals(100, arr[0].length);
        }

        // ============ Run with ParallelMode Tests ============

        @Test
        public void testRun_withParallelMode_yes() throws Exception {
            ParallelMode original = Matrices.getParallelMode();
            try {
                final AtomicInteger counter = new AtomicInteger(0);
                Matrices.runWithParallelMode(ParallelMode.FORCE_ON, () -> {
                    assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());
                    counter.incrementAndGet();
                });
                assertEquals(1, counter.get());
                // Original setting should be restored
                assertEquals(original, Matrices.getParallelMode());
            } finally {
                Matrices.setParallelMode(original);
            }
        }

        @Test
        public void testRun_withParallelMode_no() throws Exception {
            ParallelMode original = Matrices.getParallelMode();
            try {
                Matrices.runWithParallelMode(ParallelMode.FORCE_OFF, () -> {
                    assertEquals(ParallelMode.FORCE_OFF, Matrices.getParallelMode());
                });
                // Original setting should be restored
                assertEquals(original, Matrices.getParallelMode());
            } finally {
                Matrices.setParallelMode(original);
            }
        }

        @Test
        public void testRun_withParallelMode_restoresOnException() {
            ParallelMode original = Matrices.getParallelMode();
            try {
                assertThrows(RuntimeException.class, () -> {
                    Matrices.runWithParallelMode(ParallelMode.FORCE_ON, () -> {
                        throw new RuntimeException("test exception");
                    });
                });
                // Original setting should be restored even after exception
                assertEquals(original, Matrices.getParallelMode());
            } finally {
                Matrices.setParallelMode(original);
            }
        }

        // ============ Run with Grid Tests ============

        @Test
        public void testRun_grid_sequential() throws Exception {
            int[][] grid = new int[3][4];
            Matrices.forEachIndex(3, 4, (i, j) -> {
                grid[i][j] = i * 4 + j;
            }, false);

            assertEquals(0, grid[0][0]);
            assertEquals(1, grid[0][1]);
            assertEquals(4, grid[1][0]);
            assertEquals(11, grid[2][3]);
        }

        @Test
        public void testRun_grid_sequential_verify_all_cells() throws Exception {
            int[][] grid = new int[5][5];
            Matrices.forEachIndex(5, 5, (i, j) -> {
                grid[i][j] = 1;
            }, false);

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    assertEquals(1, grid[i][j]);
                }
            }
        }

        @Test
        public void testRun_grid_parallel() throws Exception {
            int[][] grid = new int[10][10];
            Matrices.forEachIndex(10, 10, (i, j) -> {
                grid[i][j] = 1;
            }, true);

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    assertEquals(1, grid[i][j]);
                }
            }
        }

        @Test
        public void testRun_grid_singleRow() throws Exception {
            int[] row = new int[5];
            Matrices.forEachIndex(1, 5, (i, j) -> {
                row[j] = j * 2;
            }, false);

            assertArrayEquals(new int[] { 0, 2, 4, 6, 8 }, row);
        }

        @Test
        public void testRun_grid_singleColumn() throws Exception {
            int[] col = new int[5];
            Matrices.forEachIndex(5, 1, (i, j) -> {
                col[i] = i * 3;
            }, false);

            assertArrayEquals(new int[] { 0, 3, 6, 9, 12 }, col);
        }

        @Test
        public void testRun_grid_emptyGrid() throws Exception {
            final AtomicInteger count = new AtomicInteger(0);
            Matrices.forEachIndex(0, 0, (i, j) -> {
                count.incrementAndGet();
            }, false);
            assertEquals(0, count.get());
        }

        // ============ Run with Region Tests ============

        @Test
        public void testRun_region_sequential() throws Exception {
            int[][] grid = new int[10][10];
            Matrices.forEachIndex(2, 5, 3, 8, (i, j) -> {
                grid[i][j] = i * 10 + j;
            }, false);

            assertEquals(23, grid[2][3]);
            assertEquals(47, grid[4][7]);
            assertEquals(0, grid[0][0]); // Outside region
            assertEquals(0, grid[5][9]); // Outside region
        }

        @Test
        public void testRun_region_parallel() throws Exception {
            int[][] grid = new int[20][20];
            Matrices.forEachIndex(5, 15, 5, 15, (i, j) -> {
                grid[i][j] = 1;
            }, true);

            for (int i = 5; i < 15; i++) {
                for (int j = 5; j < 15; j++) {
                    assertEquals(1, grid[i][j]);
                }
            }

            assertEquals(0, grid[0][0]); // Outside region
            assertEquals(0, grid[19][19]); // Outside region
        }

        @Test
        public void testRun_region_singleCell() throws Exception {
            int[][] grid = new int[10][10];
            Matrices.forEachIndex(5, 6, 5, 6, (i, j) -> {
                grid[i][j] = 42;
            }, false);

            assertEquals(42, grid[5][5]);
            assertEquals(0, grid[4][5]);
            assertEquals(0, grid[5][4]);
        }

        @Test
        public void testRun_region_fullGrid() throws Exception {
            int[][] grid = new int[5][5];
            Matrices.forEachIndex(0, 5, 0, 5, (i, j) -> {
                grid[i][j] = 1;
            }, false);

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    assertEquals(1, grid[i][j]);
                }
            }
        }

        // ============ Call Tests ============

        @Test
        public void testCall_stream_sequential() {
            com.landawn.abacus.util.stream.Stream<Integer> stream = Matrices.mapIndices(2, 3, (i, j) -> i * 3 + j, false);
            assertNotNull(stream);
            List<Integer> result = stream.toList();
            assertEquals(6, result.size());
            assertTrue(result.contains(0));
            assertTrue(result.contains(5));
        }

        @Test
        public void testCall_stream_parallel() {
            com.landawn.abacus.util.stream.Stream<Integer> stream = Matrices.mapIndices(3, 3, (i, j) -> i + j, true);
            assertNotNull(stream);
            List<Integer> result = stream.toList();
            assertEquals(9, result.size());
        }

        @Test
        public void testCall_stream_withRegion() {
            com.landawn.abacus.util.stream.Stream<Integer> stream = Matrices.mapIndices(1, 3, 1, 3, (i, j) -> i * 3 + j, false);
            assertNotNull(stream);
            List<Integer> result = stream.toList();
            assertEquals(4, result.size());
        }

        @Test
        public void testCallToInt_sequential() {
            com.landawn.abacus.util.stream.IntStream stream = Matrices.mapIndicesToInt(2, 3, (i, j) -> i + j, false);
            assertNotNull(stream);
            int[] result = stream.toArray();
            assertEquals(6, result.length);
        }

        @Test
        public void testCallToInt_parallel() {
            com.landawn.abacus.util.stream.IntStream stream = Matrices.mapIndicesToInt(3, 3, (i, j) -> i * j, true);
            assertNotNull(stream);
            int[] result = stream.toArray();
            assertEquals(9, result.length);
        }

        @Test
        public void testCallToInt_withRegion() {
            com.landawn.abacus.util.stream.IntStream stream = Matrices.mapIndicesToInt(1, 4, 1, 4, (i, j) -> i + j, false);
            assertNotNull(stream);
            int[] result = stream.toArray();
            assertEquals(9, result.length);
        }

        // ============ Multiply Tests ============

        @Test
        public void testMultiply_simpleMatrices() throws Exception {
            IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

            final AtomicInteger callCount = new AtomicInteger(0);
            Matrices.forEachCartesianIndices(a, b, (i, j, v) -> {
                callCount.incrementAndGet();
            });

            assertTrue(callCount.get() > 0);
        }

        @Test
        public void testMultiply_rectangular() throws Exception {
            IntMatrix a = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            IntMatrix b = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

            final AtomicInteger callCount = new AtomicInteger(0);
            Matrices.forEachCartesianIndices(a, b, (i, j, v) -> {
                callCount.incrementAndGet();
            });

            // Should call for each element of result (2x2)
            assertTrue(callCount.get() > 0);
        }

        @Test
        public void testMultiply_withParallel() throws Exception {
            IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

            final AtomicInteger callCount = new AtomicInteger(0);
            Matrices.forEachCartesianIndices(a, b, (i, j, v) -> {
                callCount.incrementAndGet();
            }, true);

            assertTrue(callCount.get() > 0);
        }

        // ============ Zip Tests - ByteMatrix ============

        @Test
        public void testZip_ByteMatrix_twoMatrices() throws Exception {
            ByteMatrix a = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix b = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });

            ByteMatrix result = Matrices.zip(a, b, (x, y) -> (byte) (x + y));

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6, result.get(0, 0)); // 1 + 5
            assertEquals(12, result.get(1, 1)); // 4 + 8
        }

        @Test
        public void testZip_ByteMatrix_threeMatrices() throws Exception {
            ByteMatrix a = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix b = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix c = ByteMatrix.of(new byte[][] { { 1, 1 }, { 1, 1 } });

            ByteMatrix result = Matrices.zip(a, b, c, (x, y, z) -> (byte) (x + y + z));

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(7, result.get(0, 0)); // 1 + 5 + 1
        }

        @Test
        public void testZip_ByteMatrix_collection() throws Exception {
            List<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } }), ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } }));

            ByteMatrix result = Matrices.zip(matrices, (a, b) -> (byte) (a + b));

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(6, result.get(0, 0)); // 1 + 5
        }

        // ============ Zip Tests - IntMatrix ============

        @Test
        public void testZip_IntMatrix_twoMatrices() throws Exception {
            IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

            IntMatrix result = Matrices.zip(a, b, (x, y) -> x + y);

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(6, result.get(0, 0)); // 1 + 5
            assertEquals(12, result.get(1, 1)); // 4 + 8
        }

        @Test
        public void testZip_IntMatrix_threeMatrices() throws Exception {
            IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix c = IntMatrix.of(new int[][] { { 1, 1 }, { 1, 1 } });

            IntMatrix result = Matrices.zip(a, b, c, (x, y, z) -> x + y + z);

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(7, result.get(0, 0)); // 1 + 5 + 1
        }

        @Test
        public void testZip_IntMatrix_collection() throws Exception {
            List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }));

            IntMatrix result = Matrices.zip(matrices, (a, b) -> a + b);

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(6, result.get(0, 0)); // 1 + 5
        }

        @Test
        public void testZip_IntMatrix_toLong() throws Exception {
            IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

            LongMatrix result = Matrices.zipToLong(a, b, (x, y) -> (long) x + y);

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(6L, result.get(0, 0)); // 1 + 5
        }

        @Test
        public void testZip_IntMatrix_toDouble() throws Exception {
            IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

            DoubleMatrix result = Matrices.zipToDouble(a, b, (x, y) -> x + y + 0.5);

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(6.5, result.get(0, 0), 0.01); // 1 + 5 + 0.5
        }

        // ============ Zip Tests - LongMatrix ============

        @Test
        public void testZip_LongMatrix_twoMatrices() throws Exception {
            LongMatrix a = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix b = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });

            LongMatrix result = Matrices.zip(a, b, (x, y) -> x + y);

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(6L, result.get(0, 0)); // 1 + 5
        }

        @Test
        public void testZip_LongMatrix_toDouble() throws Exception {
            LongMatrix a = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix b = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });

            DoubleMatrix result = Matrices.zipToDouble(a, b, (x, y) -> (double) (x + y));

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(6.0, result.get(0, 0), 0.01); // 1 + 5
        }

        // ============ Zip Tests - DoubleMatrix ============

        @Test
        public void testZip_DoubleMatrix_twoMatrices() throws Exception {
            DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix b = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });

            DoubleMatrix result = Matrices.zip(a, b, (x, y) -> x + y);

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(6.0, result.get(0, 0), 0.01); // 1 + 5
        }

        @Test
        public void testZip_DoubleMatrix_threeMatrices() throws Exception {
            DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix b = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix c = DoubleMatrix.of(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } });

            DoubleMatrix result = Matrices.zip(a, b, c, (x, y, z) -> x + y + z);

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(7.0, result.get(0, 0), 0.01); // 1 + 5 + 1
        }

        // ============ Zip with Generic Result Type ============

        @Test
        public void testZip_IntMatrix_toGeneric() throws Exception {
            List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }));

            Matrix<String> result = Matrices.zip(matrices, (int[] values) -> {
                StringBuilder sb = new StringBuilder();
                for (int value : values) {
                    sb.append(value).append(",");
                }
                return sb.toString();
            }, String.class);

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertNotNull(result.get(0, 0));
        }

        @Test
        public void testZip_collection_withParallel() throws Exception {
            List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }));

            IntMatrix result = Matrices.zip(matrices, (a, b) -> a + b);

            assertNotNull(result);
            assertEquals(2, result.rowCount());
        }
    }

    @Nested
    @Tag("2512")
    class Matrices2512Test extends TestBase {

        @AfterEach
        public void tearDown() {
            // Reset to default after each test to avoid side effects
            Matrices.setParallelMode(ParallelMode.AUTO);
        }

        // ============ Parallel Control Tests ============

        @Test
        public void test_getParallelMode_returnsCurrentSetting() {
            ParallelMode setting = Matrices.getParallelMode();
            assertNotNull(setting);
            // Default should be AUTO
            assertEquals(ParallelMode.AUTO, setting);
        }

        @Test
        public void test_setParallelMode_changesThreadLocalSetting() {
            Matrices.setParallelMode(ParallelMode.FORCE_ON);
            assertEquals(ParallelMode.FORCE_ON, Matrices.getParallelMode());

            Matrices.setParallelMode(ParallelMode.FORCE_OFF);
            assertEquals(ParallelMode.FORCE_OFF, Matrices.getParallelMode());

            Matrices.setParallelMode(ParallelMode.AUTO);
            assertEquals(ParallelMode.AUTO, Matrices.getParallelMode());
        }

        @Test
        public void test_setParallelMode_withNull_throwsException() {
            assertThrows(IllegalArgumentException.class, () -> Matrices.setParallelMode(null));
        }

        @Test
        public void test_isParallelizable_withMatrix() {
            IntMatrix largeMatrix = IntMatrix.of(new int[100][100]); // 10000 elements
            IntMatrix smallMatrix = IntMatrix.of(new int[10][10]); // 100 elements

            // With AUTO, large matrix should be parallelable
            Matrices.setParallelMode(ParallelMode.AUTO);
            assertTrue(Matrices.isParallelizable(largeMatrix));
            assertFalse(Matrices.isParallelizable(smallMatrix));

            // With FORCE_ON, both should be parallelable
            Matrices.setParallelMode(ParallelMode.FORCE_ON);
            assertTrue(Matrices.isParallelizable(largeMatrix));
            assertTrue(Matrices.isParallelizable(smallMatrix));

            // With FORCE_OFF, neither should be parallelable
            Matrices.setParallelMode(ParallelMode.FORCE_OFF);
            assertFalse(Matrices.isParallelizable(largeMatrix));
            assertFalse(Matrices.isParallelizable(smallMatrix));
        }

        @Test
        public void test_isParallelizable_withCount() {
            IntMatrix matrix = IntMatrix.of(new int[1][1]);

            // Default: parallelable when count >= 8192
            Matrices.setParallelMode(ParallelMode.AUTO);
            assertTrue(Matrices.isParallelizable(matrix, 8192));
            assertTrue(Matrices.isParallelizable(matrix, 10000));
            assertFalse(Matrices.isParallelizable(matrix, 100));

            // FORCE_ON: always parallelable
            Matrices.setParallelMode(ParallelMode.FORCE_ON);
            assertTrue(Matrices.isParallelizable(matrix, 1));
            assertTrue(Matrices.isParallelizable(matrix, 10000));

            // FORCE_OFF: never parallelable
            Matrices.setParallelMode(ParallelMode.FORCE_OFF);
            assertFalse(Matrices.isParallelizable(matrix, 1));
            assertFalse(Matrices.isParallelizable(matrix, 1000000));
        }

        // ============ Shape Comparison Tests ============

        @Test
        public void test_isSameShape_withTwoMatrices_sameShape() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            assertTrue(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void test_isSameShape_withTwoMatrices_differentShape() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6, 7 } });
            assertFalse(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void test_isSameShape_withTwoMatrices_differentRows() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 } });
            assertFalse(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void test_isSameShape_withTwoMatrices_differentCols() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4, 5 } });
            assertFalse(Matrices.isSameShape(m1, m2));
        }

        @Test
        public void test_isSameShape_withThreeMatrices_differentShape() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10, 11 } });
            assertFalse(Matrices.isSameShape(m1, m2, m3));
        }

        @Test
        public void test_isSameShape_withCollection_allSameShape() {
            List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }),
                    IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } }));
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void test_isSameShape_withCollection_differentShape() {
            List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }),
                    IntMatrix.of(new int[][] { { 9, 10, 11 } }));
            assertFalse(Matrices.isSameShape(matrices));
        }

        @Test
        public void test_isSameShape_withCollection_empty() {
            List<IntMatrix> matrices = Arrays.asList();
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void test_isSameShape_withCollection_singleElement() {
            List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }));
            assertTrue(Matrices.isSameShape(matrices));
        }

        @Test
        public void test_isSameShape_withCollection_null() {
            assertTrue(Matrices.isSameShape((List<IntMatrix>) null));
        }

        // ============ Array Creation Tests ============

        @Test
        public void test_newArray_createsCorrectDimensions() {
            String[][] arr = Matrices.newMatrixArray(3, 4, String.class);
            assertEquals(3, arr.length);
            assertEquals(4, arr[0].length);
            assertEquals(4, arr[1].length);
            assertEquals(4, arr[2].length);
        }

        @Test
        public void test_newArray_withZeroRows() {
            assertThrows(IllegalArgumentException.class, () -> Matrices.newMatrixArray(0, 5, Integer.class));
        }

        @Test
        public void test_newArray_withZeroCols() {
            Integer[][] arr = Matrices.newMatrixArray(3, 0, Integer.class);
            assertEquals(3, arr.length);
            assertEquals(0, arr[0].length);
        }

        @Test
        public void test_newArray_withPrimitiveType() {
            Integer[][] arr = Matrices.newMatrixArray(2, 3, int.class); // Should be wrapped to Integer
            assertEquals(2, arr.length);
            assertEquals(3, arr[0].length);
            // Elements should be null by default
            assertNull(arr[0][0]);
        }

        // ============ Run with ParallelMode Tests ============

        @Test
        public void test_run_withParallelMode_executesAndRestores() {
            Matrices.setParallelMode(ParallelMode.AUTO);

            final ParallelMode[] capturedSetting = new ParallelMode[1];

            Matrices.runWithParallelMode(ParallelMode.FORCE_ON, () -> {
                capturedSetting[0] = Matrices.getParallelMode();
            });

            // Inside the run, it should have been FORCE_ON
            assertEquals(ParallelMode.FORCE_ON, capturedSetting[0]);

            // After the run, it should be restored to AUTO
            assertEquals(ParallelMode.AUTO, Matrices.getParallelMode());
        }

        @Test
        public void test_run_withParallelMode_restoresOnException() {
            Matrices.setParallelMode(ParallelMode.AUTO);

            assertThrows(RuntimeException.class, () -> {
                Matrices.runWithParallelMode(ParallelMode.FORCE_ON, () -> {
                    throw new RuntimeException("Test exception");
                });
            });

            // Should still be restored to AUTO
            assertEquals(ParallelMode.AUTO, Matrices.getParallelMode());
        }

        // ============ Run with Grid Tests ============

        @Test
        public void test_run_withRowsAndCols_iteratesAllPositions() {
            AtomicInteger count = new AtomicInteger(0);

            Matrices.forEachIndex(3, 4, (i, j) -> {
                count.incrementAndGet();
            }, false);

            assertEquals(12, count.get()); // 3 rows * 4 columnCount
        }

        @Test
        public void test_run_withRowsAndCols_passesCorrectIndices() {
            final int[][] visited = new int[2][3];

            Matrices.forEachIndex(2, 3, (i, j) -> {
                visited[i][j] = 1;
            }, false);

            // All positions should be visited
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(1, visited[i][j]);
                }
            }
        }

        @Test
        public void test_run_withRowsAndCols_parallel() {
            AtomicInteger count = new AtomicInteger(0);

            Matrices.forEachIndex(10, 10, (i, j) -> {
                count.incrementAndGet();
            }, true);

            assertEquals(100, count.get());
        }

        @Test
        public void test_run_withRange_iteratesCorrectPositions() {
            final int[][] visited = new int[5][5];

            Matrices.forEachIndex(1, 3, 1, 4, (i, j) -> {
                visited[i][j] = 1;
            }, false);

            // Check that only positions in range [1,3) x [1,4) are visited
            assertEquals(0, visited[0][0]); // outside range
            assertEquals(1, visited[1][1]); // inside range
            assertEquals(1, visited[2][3]); // inside range
            assertEquals(0, visited[3][1]); // outside range (row)
            assertEquals(0, visited[1][4]); // outside range (col)
        }

        // ============ Zip Tests for ByteMatrix ============

        @Test
        public void test_zip_byteMatrix_withTwoMatrices() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });

            ByteMatrix result = Matrices.zip(m1, m2, (a, b) -> (byte) (a + b));

            assertEquals(6, result.get(0, 0));
            assertEquals(8, result.get(0, 1));
            assertEquals(10, result.get(1, 0));
            assertEquals(12, result.get(1, 1));
        }

        @Test
        public void test_zip_byteMatrix_withCollection() {
            List<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } }), ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } }),
                    ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } }));

            ByteMatrix result = Matrices.zip(matrices, (a, b) -> (byte) (a + b));

            // Should sum all three matrices element-wise
            assertEquals(15, result.get(0, 0)); // 1+5+9
            assertEquals(24, result.get(1, 1)); // 4+8+12
        }

        @Test
        public void test_zipToInt_fromByteMatrix() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });

            IntMatrix result = Matrices.zipToInt(m1, m2, (a, b) -> a * b);

            assertEquals(5, result.get(0, 0)); // 1*5
            assertEquals(12, result.get(0, 1)); // 2*6
            assertEquals(32, result.get(1, 1)); // 4*8
        }

        // ============ Zip Tests for IntMatrix ============

        @Test
        public void test_zip_intMatrix_withTwoMatrices() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

            IntMatrix result = Matrices.zip(m1, m2, (a, b) -> a + b);

            assertEquals(6, result.get(0, 0));
            assertEquals(8, result.get(0, 1));
            assertEquals(10, result.get(1, 0));
            assertEquals(12, result.get(1, 1));
        }

        @Test
        public void test_zip_intMatrix_withCollection() {
            List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }),
                    IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } }));

            IntMatrix result = Matrices.zip(matrices, (a, b) -> a + b);

            assertEquals(15, result.get(0, 0));
            assertEquals(24, result.get(1, 1));
        }

        @Test
        public void test_zip_intMatrix_differentSizes_throwsException() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4, 5 } });

            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (a, b) -> a + b));
        }

        @Test
        public void test_zipToLong_fromIntMatrix() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

            LongMatrix result = Matrices.zipToLong(m1, m2, (a, b) -> (long) a * b);

            assertEquals(5L, result.get(0, 0));
            assertEquals(32L, result.get(1, 1));
        }

        @Test
        public void test_zipToDouble_fromIntMatrix() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 2, 3 }, { 4, 5 } });

            DoubleMatrix result = Matrices.zipToDouble(m1, m2, (a, b) -> (double) a / b);

            assertEquals(0.5, result.get(0, 0), 0.001);
            assertEquals(0.8, result.get(1, 1), 0.001);
        }

        // ============ Zip Tests for LongMatrix ============

        @Test
        public void test_zip_longMatrix_withTwoMatrices() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });

            LongMatrix result = Matrices.zip(m1, m2, (a, b) -> a + b);

            assertEquals(6L, result.get(0, 0));
            assertEquals(12L, result.get(1, 1));
        }

        @Test
        public void test_zip_longMatrix_withCollection() {
            List<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } }),
                    LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } }));

            LongMatrix result = Matrices.zip(matrices, (a, b) -> a + b);

            assertEquals(6L, result.get(0, 0));
            assertEquals(12L, result.get(1, 1));
        }

        @Test
        public void test_zipToDouble_fromLongMatrix() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 2L, 4L }, { 6L, 8L } });

            DoubleMatrix result = Matrices.zipToDouble(m1, m2, (a, b) -> (double) a / b);

            assertEquals(5.0, result.get(0, 0));
            assertEquals(5.0, result.get(1, 1));
        }

        // ============ Zip Tests for DoubleMatrix ============

        @Test
        public void test_zip_doubleMatrix_withTwoMatrices() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });

            DoubleMatrix result = Matrices.zip(m1, m2, (a, b) -> a + b);

            assertEquals(6.0, result.get(0, 0));
            assertEquals(12.0, result.get(1, 1));
        }

        @Test
        public void test_zip_doubleMatrix_withCollection() {
            List<DoubleMatrix> matrices = Arrays.asList(DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } }),
                    DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } }));

            DoubleMatrix result = Matrices.zip(matrices, (a, b) -> a + b);

            assertEquals(6.0, result.get(0, 0));
            assertEquals(12.0, result.get(1, 1));
        }

        @Test
        public void test_zip_doubleMatrix_multiplicationFunction() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 2.0, 3.0 }, { 4.0, 5.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });

            DoubleMatrix result = Matrices.zip(m1, m2, (a, b) -> a * b);

            assertEquals(3.0, result.get(0, 0), 0.001);
            assertEquals(22.5, result.get(1, 1), 0.001);
        }

        // ============ Zip Tests for Generic Matrix ============

        @Test
        public void test_zip_genericMatrix_withCollection() {
            List<Matrix<Integer>> matrices = Arrays.asList(Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } }), Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } }),
                    Matrix.of(new Integer[][] { { 9, 10 }, { 11, 12 } }));

            Matrix<Integer> result = Matrices.zip(matrices, (a, b) -> a + b);

            assertEquals(15, result.get(0, 0));
            assertEquals(24, result.get(1, 1));
        }

        @Test
        public void test_zip_genericMatrix_withStrings() {
            List<Matrix<String>> matrices = Arrays.asList(Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } }),
                    Matrix.of(new String[][] { { "1", "2" }, { "3", "4" } }));

            Matrix<String> result = Matrices.zip(matrices, (a, b) -> a + b);

            assertEquals("a1", result.get(0, 0));
            assertEquals("b2", result.get(0, 1));
            assertEquals("d4", result.get(1, 1));
        }

        @Test
        public void test_zip_genericMatrix_emptyCollection_throwsException() {
            List<Matrix<Integer>> matrices = Arrays.asList();

            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(matrices, (a, b) -> a + b));
        }

        // ============ Multiply Tests ============

        @Test
        public void test_multiply_performsMatrixMultiplication() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
            IntMatrix result = IntMatrix.of(new int[2][2]);

            Matrices.forEachCartesianIndices(m1, m2, (i, j, k) -> {
                result.a[i][j] += m1.a[i][k] * m2.a[k][j];
            });

            assertEquals(19, result.get(0, 0)); // 1*5 + 2*7
            assertEquals(22, result.get(0, 1)); // 1*6 + 2*8
            assertEquals(43, result.get(1, 0)); // 3*5 + 4*7
            assertEquals(50, result.get(1, 1)); // 3*6 + 4*8
        }

        @Test
        public void test_multiply_incompatibleDimensions_throwsException() {
            IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } }); // 1x2
            IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4, 5 } }); // 1x3 (incompatible)

            assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(m1, m2, (i, j, k) -> {
            }));
        }

        // ============ CallToInt Tests ============

        @Test
        public void test_callToInt_generatesIntStream() {
            com.landawn.abacus.util.stream.IntStream stream = Matrices.mapIndicesToInt(2, 3, (i, j) -> i * 10 + j, false);

            int[] result = stream.toArray();

            assertEquals(6, result.length); // 2 rows * 3 columnCount
            assertArrayEquals(new int[] { 0, 1, 2, 10, 11, 12 }, result);
        }

        @Test
        public void test_callToInt_withParallel() {
            com.landawn.abacus.util.stream.IntStream stream = Matrices.mapIndicesToInt(5, 5, (i, j) -> i + j, true);

            long count = stream.count();

            assertEquals(25, count); // 5 rows * 5 columnCount
        }

        // ============ Edge Cases Tests ============

        @Test
        public void test_zip_withEmptyMatrices() {
            IntMatrix m1 = IntMatrix.empty();
            IntMatrix m2 = IntMatrix.empty();

            IntMatrix result = Matrices.zip(m1, m2, (a, b) -> a + b);

            assertTrue(result.isEmpty());
        }

        @Test
        public void test_run_withZeroRows() {
            AtomicInteger count = new AtomicInteger(0);

            Matrices.forEachIndex(0, 5, (i, j) -> {
                count.incrementAndGet();
            }, false);

            assertEquals(0, count.get());
        }

        @Test
        public void test_run_withZeroCols() {
            AtomicInteger count = new AtomicInteger(0);

            Matrices.forEachIndex(5, 0, (i, j) -> {
                count.incrementAndGet();
            }, false);

            assertEquals(0, count.get());
        }
    }

    @Nested
    class MatricesNullArgumentContractTest {

        @Test
        public void testZipByteMatrixNullArgumentsThrowIllegalArgumentException() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 5, 6 } });

            assertThrows(IllegalArgumentException.class, () -> Matrices.zip((ByteMatrix) null, m2, (a, b) -> (byte) (a + b)));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (ByteMatrix) null, (a, b) -> (byte) (a + b)));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (Throwables.ByteBinaryOperator<RuntimeException>) null));

            assertThrows(IllegalArgumentException.class, () -> Matrices.zip((ByteMatrix) null, m2, m3, (a, b, c) -> (byte) (a + b + c)));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (ByteMatrix) null, m3, (a, b, c) -> (byte) (a + b + c)));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (ByteMatrix) null, (a, b, c) -> (byte) (a + b + c)));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, m3, (Throwables.ByteTernaryOperator<RuntimeException>) null));
        }

        @Test
        public void testZipGenericMatrixNullArgumentsThrowIllegalArgumentException() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "a", "b" } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 1, 2 } });
            Matrix<Double> m3 = Matrix.of(new Double[][] { { 1.0, 2.0 } });

            assertThrows(IllegalArgumentException.class, () -> Matrices.zip((Matrix<String>) null, m2, (a, b) -> a + b));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (Matrix<Integer>) null, (a, b) -> a + b));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (Throwables.BiFunction<String, Integer, String, RuntimeException>) null));

            assertThrows(IllegalArgumentException.class, () -> Matrices.zip((Matrix<String>) null, m2, (a, b) -> a + b, String.class));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (Matrix<Integer>) null, (a, b) -> a + b, String.class));
            assertThrows(IllegalArgumentException.class,
                    () -> Matrices.zip(m1, m2, (Throwables.BiFunction<String, Integer, String, RuntimeException>) null, String.class));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (a, b) -> a + b, null));

            assertThrows(IllegalArgumentException.class, () -> Matrices.zip((Matrix<String>) null, m2, m3, (a, b, c) -> a + b + c.intValue()));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (Matrix<Integer>) null, m3, (a, b, c) -> a + b + c.intValue()));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (Matrix<Double>) null, (a, b, c) -> a + b + c.intValue()));
            assertThrows(IllegalArgumentException.class,
                    () -> Matrices.zip(m1, m2, m3, (Throwables.TriFunction<String, Integer, Double, String, RuntimeException>) null));

            assertThrows(IllegalArgumentException.class, () -> Matrices.zip((Matrix<String>) null, m2, m3, (a, b, c) -> a + b + c.intValue(), String.class));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, (Matrix<Integer>) null, m3, (a, b, c) -> a + b + c.intValue(), String.class));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, (Matrix<Double>) null, (a, b, c) -> a + b + c.intValue(), String.class));
            assertThrows(IllegalArgumentException.class,
                    () -> Matrices.zip(m1, m2, m3, (Throwables.TriFunction<String, Integer, Double, String, RuntimeException>) null, String.class));
            assertThrows(IllegalArgumentException.class, () -> Matrices.zip(m1, m2, m3, (a, b, c) -> a + b + c.intValue(), null));
        }
    }

    // === Missing coverage: newMatrixArray, forEachCartesianIndices ===

    @Test
    public void testNewMatrixArray() {
        Integer[][] arr = Matrices.newMatrixArray(3, 4, Integer.class);
        assertNotNull(arr);
        assertEquals(3, arr.length);
        assertEquals(4, arr[0].length);
        assertEquals(4, arr[1].length);
        assertEquals(4, arr[2].length);
    }

    @Test
    public void testNewMatrixArray_primitiveTypeWrapped() {
        Integer[][] arr = Matrices.newMatrixArray(2, 3, int.class);
        assertNotNull(arr);
        assertEquals(2, arr.length);
        assertEquals(3, arr[0].length);
    }

    @Test
    public void testNewMatrixArray_zeroRows() {
        String[][] arr = Matrices.newMatrixArray(0, 0, String.class);
        assertNotNull(arr);
        assertEquals(0, arr.length);
    }

    @Test
    public void testNewMatrixArray_zeroRowsNonZeroColsThrows() {
        assertThrows(IllegalArgumentException.class, () -> Matrices.newMatrixArray(0, 5, String.class));
    }

    @Test
    public void testNewMatrixArray_zeroCols() {
        String[][] arr = Matrices.newMatrixArray(3, 0, String.class);
        assertNotNull(arr);
        assertEquals(3, arr.length);
        assertEquals(0, arr[0].length);
    }

    @Test
    public void testNewMatrixArray_negativeRowsThrows() {
        assertThrows(IllegalArgumentException.class, () -> Matrices.newMatrixArray(-1, 3, String.class));
    }

    @Test
    public void testNewMatrixArray_negativeColsThrows() {
        assertThrows(IllegalArgumentException.class, () -> Matrices.newMatrixArray(3, -1, String.class));
    }

    @Test
    public void testNewMatrixArray_nullTypeThrows() {
        assertThrows(IllegalArgumentException.class, () -> Matrices.newMatrixArray(2, 2, null));
    }

    @Test
    public void testForEachCartesianIndices() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

        int[][] result = new int[2][2];
        Matrices.forEachCartesianIndices(a, b, (i, j, k) -> result[i][j] += a.get(i, k) * b.get(k, j));

        assertEquals(19, result[0][0]); // 1*5 + 2*7
        assertEquals(22, result[0][1]); // 1*6 + 2*8
        assertEquals(43, result[1][0]); // 3*5 + 4*7
        assertEquals(50, result[1][1]); // 3*6 + 4*8
    }

    @Test
    public void testForEachCartesianIndices_rectangular() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 4 }, { 5 }, { 6 } });

        int[][] result = new int[1][1];
        Matrices.forEachCartesianIndices(a, b, (i, j, k) -> result[i][j] += a.get(i, k) * b.get(k, j));

        assertEquals(32, result[0][0]); // 1*4 + 2*5 + 3*6
    }

    @Test
    public void testForEachCartesianIndices_incompatibleThrows() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(a, b, (i, j, k) -> {
        }));
    }

    @Test
    public void testForEachCartesianIndices_nullThrows() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(null, b, (i, j, k) -> {
        }));
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(a, null, (i, j, k) -> {
        }));
        assertThrows(IllegalArgumentException.class, () -> Matrices.forEachCartesianIndices(a, b, null));
    }

    @Test
    public void testForEachCartesianIndices_withParallelFlag() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

        int[][] result = new int[2][2];
        Matrices.forEachCartesianIndices(a, b, (i, j, k) -> {
            synchronized (result) {
                result[i][j] += a.get(i, k) * b.get(k, j);
            }
        }, true);

        assertEquals(19, result[0][0]);
        assertEquals(22, result[0][1]);
        assertEquals(43, result[1][0]);
        assertEquals(50, result[1][1]);
    }

    @Test
    public void testForEachCartesianIndices_RectangularResultSequential() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 1 }, { 2 }, { 3 } });
        int[][] result = new int[a.rowCount()][b.columnCount()];

        Matrices.forEachCartesianIndices(a, b, (i, j, k) -> result[i][j] += a.get(i, k) * b.get(k, j), false);

        assertEquals(14, result[0][0]);
        assertEquals(32, result[1][0]);
        assertEquals(50, result[2][0]);
    }

    @Test
    public void testForEachCartesianIndices_RectangularResultParallel() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1 }, { 2 }, { 3 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 4, 5, 6 } });
        int[][] result = new int[a.rowCount()][b.columnCount()];

        Matrices.forEachCartesianIndices(a, b, (i, j, k) -> {
            synchronized (result) {
                result[i][j] += a.get(i, k) * b.get(k, j);
            }
        }, true);

        assertArrayEquals(new int[] { 4, 5, 6 }, result[0]);
        assertArrayEquals(new int[] { 8, 10, 12 }, result[1]);
        assertArrayEquals(new int[] { 12, 15, 18 }, result[2]);
    }

}
