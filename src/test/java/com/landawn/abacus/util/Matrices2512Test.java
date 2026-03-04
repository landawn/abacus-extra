package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

@Tag("2512")
public class Matrices2512Test extends TestBase {

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
    public void test_isSameShape_withThreeMatrices_allSameShape() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
        assertTrue(Matrices.isSameShape(m1, m2, m3));
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

        try {
            Matrices.runWithParallelMode(ParallelMode.FORCE_ON, () -> {
                throw new RuntimeException("Test exception");
            });
        } catch (RuntimeException e) {
            // Expected
        }

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

        Matrices.multiply(m1, m2, (i, j, k) -> {
            result.a[i][j] += m1.a[i][k] * m2.a[k][j];
        });

        assertEquals(19, result.get(0, 0)); // 1*5 + 2*7
        assertEquals(22, result.get(0, 1)); // 1*6 + 2*8
        assertEquals(43, result.get(1, 0)); // 3*5 + 4*7
        assertEquals(50, result.get(1, 1)); // 3*6 + 4*8
    }

    @Test
    public void test_multiply_withDifferentDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 } }); // 1x3
        IntMatrix m2 = IntMatrix.of(new int[][] { { 4 }, { 5 }, { 6 } }); // 3x1
        IntMatrix result = IntMatrix.of(new int[1][1]);

        Matrices.multiply(m1, m2, (i, j, k) -> {
            result.a[i][j] += m1.a[i][k] * m2.a[k][j];
        });

        assertEquals(32, result.get(0, 0)); // 1*4 + 2*5 + 3*6
    }

    @Test
    public void test_multiply_incompatibleDimensions_throwsException() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } }); // 1x2
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4, 5 } }); // 1x3 (incompatible)

        assertThrows(IllegalArgumentException.class, () -> Matrices.multiply(m1, m2, (i, j, k) -> {
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
    public void test_isSameShape_withEmptyMatrices() {
        IntMatrix m1 = IntMatrix.empty();
        IntMatrix m2 = IntMatrix.empty();

        assertTrue(Matrices.isSameShape(m1, m2));
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
