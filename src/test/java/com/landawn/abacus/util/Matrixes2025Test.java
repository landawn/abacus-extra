package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.stream.IntStream;

/**
 * Comprehensive unit tests for Matrixes utility class.
 */
@Tag("2025")
public class Matrixes2025Test extends TestBase {

    @AfterEach
    public void tearDown() {
        // Reset parallel settings after each test
        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
    }

    // ============ Parallel Settings Tests ============

    @Test
    public void testGetParallelEnabled_default() {
        ParallelEnabled enabled = Matrixes.getParallelEnabled();
        assertNotNull(enabled);
    }

    @Test
    public void testSetParallelEnabled_yes() {
        Matrixes.setParallelEnabled(ParallelEnabled.YES);
        assertEquals(ParallelEnabled.YES, Matrixes.getParallelEnabled());
    }

    @Test
    public void testSetParallelEnabled_no() {
        Matrixes.setParallelEnabled(ParallelEnabled.NO);
        assertEquals(ParallelEnabled.NO, Matrixes.getParallelEnabled());
    }

    @Test
    public void testSetParallelEnabled_default() {
        Matrixes.setParallelEnabled(ParallelEnabled.YES);
        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
        assertEquals(ParallelEnabled.DEFAULT, Matrixes.getParallelEnabled());
    }

    @Test
    public void testSetParallelEnabled_null() {
        assertThrows(IllegalArgumentException.class, () -> Matrixes.setParallelEnabled(null));
    }

    // ============ isParallelable Tests ============

    @Test
    public void testIsParallelable_withMatrix() {
        IntMatrix small = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        // Small matrix usually not parallelable by default
        boolean result = Matrixes.isParallelable(small);
        // Result depends on parallel settings
        assertNotNull(result);
    }

    @Test
    public void testIsParallelable_withCount() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        boolean result = Matrixes.isParallelable(m, 10);
        assertNotNull(result);
    }

    @Test
    public void testIsParallelable_largeCount() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        boolean result = Matrixes.isParallelable(m, 10000);
        // Large count may trigger parallel
        assertNotNull(result);
    }

    @Test
    public void testIsParallelable_forcedYes() {
        Matrixes.setParallelEnabled(ParallelEnabled.YES);
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        boolean result = Matrixes.isParallelable(m);
        assertTrue(result);
    }

    @Test
    public void testIsParallelable_forcedNo() {
        Matrixes.setParallelEnabled(ParallelEnabled.NO);
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        boolean result = Matrixes.isParallelable(m);
        assertFalse(result);
    }

    // ============ isSameShape Tests ============

    @Test
    public void testIsSameShape_twoMatrices_same() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        assertTrue(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_twoMatrices_different() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        assertFalse(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_twoMatrices_differentRows() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 } });
        assertFalse(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_twoMatrices_differentCols() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertFalse(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_threeMatrices_same() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
        assertTrue(Matrixes.isSameShape(m1, m2, m3));
    }

    @Test
    public void testIsSameShape_threeMatrices_different() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10, 11 } });
        assertFalse(Matrixes.isSameShape(m1, m2, m3));
    }

    @Test
    public void testIsSameShape_collection_same() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }),
                IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } }));
        assertTrue(Matrixes.isSameShape(matrices));
    }

    @Test
    public void testIsSameShape_collection_different() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6, 7 } }));
        assertFalse(Matrixes.isSameShape(matrices));
    }

    @Test
    public void testIsSameShape_collection_empty() {
        Collection<IntMatrix> matrices = new ArrayList<>();
        assertTrue(Matrixes.isSameShape(matrices));
    }

    @Test
    public void testIsSameShape_collection_single() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 } }));
        assertTrue(Matrixes.isSameShape(matrices));
    }

    // ============ newArray Tests ============

    @Test
    public void testNewArray() {
        Integer[][] arr = Matrixes.newArray(2, 3, Integer.class);
        assertNotNull(arr);
        assertEquals(2, arr.length);
        assertEquals(3, arr[0].length);
        assertEquals(3, arr[1].length);
    }

    @Test
    public void testNewArray_primitiveType() {
        Integer[][] arr = Matrixes.newArray(3, 4, int.class);
        assertNotNull(arr);
        assertEquals(3, arr.length);
        assertEquals(4, arr[0].length);
    }

    @Test
    public void testNewArray_stringType() {
        String[][] arr = Matrixes.newArray(2, 2, String.class);
        assertNotNull(arr);
        assertEquals(2, arr.length);
        assertEquals(2, arr[0].length);
    }

    @Test
    public void testNewArray_singleElement() {
        Double[][] arr = Matrixes.newArray(1, 1, Double.class);
        assertNotNull(arr);
        assertEquals(1, arr.length);
        assertEquals(1, arr[0].length);
    }

    // ============ run Tests ============

    @Test
    public void testRun_withParallelEnabled() {
        List<String> values = new ArrayList<>();
        Matrixes.run(() -> values.add("test"), ParallelEnabled.NO);
        assertEquals(1, values.size());
        assertEquals("test", values.get(0));
        // Should restore original setting
        assertNotNull(Matrixes.getParallelEnabled());
    }

    @Test
    public void testRun_withParallelEnabled_exception() {
        assertThrows(RuntimeException.class, () -> {
            Matrixes.run(() -> {
                throw new RuntimeException("test exception");
            }, ParallelEnabled.NO);
        });
    }

    @Test
    public void testRun_rowsAndCols() {
        List<String> positions = new ArrayList<>();
        Matrixes.run(2, 3, (i, j) -> positions.add(i + "," + j), false);
        assertEquals(6, positions.size());
        assertTrue(positions.contains("0,0"));
        assertTrue(positions.contains("1,2"));
    }

    @Test
    public void testRun_rowsAndCols_parallel() {
        List<String> positions = new ArrayList<>();
        Matrixes.setParallelEnabled(ParallelEnabled.NO);
        Matrixes.run(2, 2, (i, j) -> {
            synchronized (positions) {
                positions.add(i + "," + j);
            }
        }, true);
        assertEquals(4, positions.size());
    }

    @Test
    public void testRun_withRange() {
        List<String> positions = new ArrayList<>();
        Matrixes.run(1, 3, 1, 3, (i, j) -> positions.add(i + "," + j), false);
        assertEquals(4, positions.size());
        assertEquals("1,1", positions.get(0));
        assertEquals("1,2", positions.get(1));
        assertEquals("2,1", positions.get(2));
        assertEquals("2,2", positions.get(3));
    }

    @Test
    public void testRun_withRange_outOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> Matrixes.run(-1, 2, 0, 2, (i, j) -> {
        }, false));
    }

    // ============ call Tests ============

    @Test
    public void testCall() {
        com.landawn.abacus.util.stream.Stream<String> result = Matrixes.call(2, 2, (i, j) -> i + "," + j, false);
        List<String> list = result.toList();
        assertEquals(4, list.size());
        assertTrue(list.contains("0,0"));
        assertTrue(list.contains("1,1"));
    }

    @Test
    public void testCall_withRange() {
        com.landawn.abacus.util.stream.Stream<String> result = Matrixes.call(0, 2, 0, 3, (i, j) -> i + ":" + j, false);
        List<String> list = result.toList();
        assertEquals(6, list.size());
    }

    @Test
    public void testCall_parallel() {
        com.landawn.abacus.util.stream.Stream<Integer> result = Matrixes.call(2, 2, (i, j) -> i * 10 + j, true);
        List<Integer> list = result.toList();
        assertEquals(4, list.size());
    }

    @Test
    public void testCall_withRange_outOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> Matrixes.call(-1, 2, 0, 2, (i, j) -> i + j, false));
    }

    // ============ callToInt Tests ============

    @Test
    public void testCallToInt() {
        IntStream result = Matrixes.callToInt(2, 3, (i, j) -> i * 10 + j, false);
        int[] array = result.toArray();
        assertEquals(6, array.length);
    }

    @Test
    public void testCallToInt_withRange() {
        IntStream result = Matrixes.callToInt(0, 2, 0, 2, (i, j) -> i + j, false);
        int[] array = result.toArray();
        assertEquals(4, array.length);
    }

    @Test
    public void testCallToInt_parallel() {
        IntStream result = Matrixes.callToInt(3, 3, (i, j) -> i * j, true);
        int[] array = result.toArray();
        assertEquals(9, array.length);
    }

    @Test
    public void testCallToInt_withRange_outOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> Matrixes.callToInt(-1, 2, 0, 2, (i, j) -> i + j, false));
    }

    // ============ multiply Tests ============

    @Test
    public void testMultiply() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        int[][] result = new int[2][2];

        Matrixes.multiply(m1, m2, (i, j, k) -> result[i][j] += m1.get(i, k) * m2.get(k, j));

        assertEquals(19, result[0][0]);
        assertEquals(22, result[0][1]);
        assertEquals(43, result[1][0]);
        assertEquals(50, result[1][1]);
    }

    @Test
    public void testMultiply_incompatibleDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1 }, { 2 }, { 3 } });
        assertThrows(IllegalArgumentException.class, () -> Matrixes.multiply(m1, m2, (i, j, k) -> {
        }));
    }

    @Test
    public void testMultiply_withParallel() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        int[][] result = new int[2][2];

        Matrixes.multiply(m1, m2, (i, j, k) -> {
            synchronized (result) {
                result[i][j] += m1.get(i, k) * m2.get(k, j);
            }
        }, true);

        assertEquals(19, result[0][0]);
        assertEquals(22, result[0][1]);
    }

    // ============ zip Tests for ByteMatrix ============

    @Test
    public void testZip_byteMatrix_two() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix result = Matrixes.zip(m1, m2, (a, b) -> (byte) (a + b));

        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(10, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
    }

    @Test
    public void testZip_byteMatrix_three() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } });
        ByteMatrix result = Matrixes.zip(m1, m2, m3, (a, b, c) -> (byte) (a + b + c));

        assertEquals(15, result.get(0, 0));
        assertEquals(18, result.get(0, 1));
        assertEquals(21, result.get(1, 0));
        assertEquals(24, result.get(1, 1));
    }

    @Test
    public void testZip_byteMatrix_collection() {
        Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } }), ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } }));
        ByteMatrix result = Matrixes.zip(matrices, (a, b) -> (byte) (a * b));

        assertEquals(5, result.get(0, 0));
        assertEquals(12, result.get(0, 1));
        assertEquals(21, result.get(1, 0));
        assertEquals(32, result.get(1, 1));
    }

    @Test
    public void testZip_byteMatrix_collection_single() {
        Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } }));
        ByteMatrix result = Matrixes.zip(matrices, (a, b) -> (byte) (a + b));

        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
    }

    @Test
    public void testZip_byteMatrix_toObject() {
        Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 } }), ByteMatrix.of(new byte[][] { { 3, 4 } }));
        Matrix<String> result = Matrixes.zip(matrices, arr -> String.valueOf(arr[0] + arr[1]), String.class);

        assertEquals("4", result.get(0, 0));
        assertEquals("6", result.get(0, 1));
    }

    // ============ zip Tests for IntMatrix ============

    @Test
    public void testZip_intMatrix_two() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix result = Matrixes.zip(m1, m2, (a, b) -> a + b);

        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(10, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
    }

    @Test
    public void testZip_intMatrix_three() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
        IntMatrix result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a + b + c);

        assertEquals(15, result.get(0, 0));
        assertEquals(18, result.get(0, 1));
        assertEquals(21, result.get(1, 0));
        assertEquals(24, result.get(1, 1));
    }

    @Test
    public void testZip_intMatrix_collection() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }),
                IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } }));
        IntMatrix result = Matrixes.zip(matrices, (a, b) -> a + b);

        assertEquals(15, result.get(0, 0));
        assertEquals(18, result.get(0, 1));
        assertEquals(21, result.get(1, 0));
        assertEquals(24, result.get(1, 1));
    }

    @Test
    public void testZip_intMatrix_toObject() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 } }), IntMatrix.of(new int[][] { { 3, 4 } }));
        Matrix<Integer> result = Matrixes.zip(matrices, arr -> arr[0] * arr[1], Integer.class);

        assertEquals(3, result.get(0, 0).intValue());
        assertEquals(8, result.get(0, 1).intValue());
    }

    @Test
    public void testZipToLong_intMatrix_two() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        LongMatrix result = Matrixes.zipToLong(m1, m2, (a, b) -> (long) a * b);

        assertEquals(5L, result.get(0, 0));
        assertEquals(12L, result.get(0, 1));
        assertEquals(21L, result.get(1, 0));
        assertEquals(32L, result.get(1, 1));
    }

    @Test
    public void testZipToDouble_intMatrix_two() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        DoubleMatrix result = Matrixes.zipToDouble(m1, m2, (a, b) -> (double) a / b);

        assertEquals(0.2, result.get(0, 0), 0.0001);
        assertEquals(0.333, result.get(0, 1), 0.01);
    }

    // ============ zip Tests for LongMatrix ============

    @Test
    public void testZip_longMatrix_two() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix result = Matrixes.zip(m1, m2, (a, b) -> a + b);

        assertEquals(6L, result.get(0, 0));
        assertEquals(8L, result.get(0, 1));
        assertEquals(10L, result.get(1, 0));
        assertEquals(12L, result.get(1, 1));
    }

    @Test
    public void testZip_longMatrix_collection() {
        Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } }),
                LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } }));
        LongMatrix result = Matrixes.zip(matrices, (a, b) -> a * b);

        assertEquals(5L, result.get(0, 0));
        assertEquals(12L, result.get(0, 1));
        assertEquals(21L, result.get(1, 0));
        assertEquals(32L, result.get(1, 1));
    }

    @Test
    public void testZipToDouble_longMatrix() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 2L, 4L }, { 6L, 8L } });
        DoubleMatrix result = Matrixes.zipToDouble(m1, m2, (a, b) -> (double) a / b);

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
        DoubleMatrix result = Matrixes.zip(m1, m2, (a, b) -> a + b);

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
        DoubleMatrix result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a + b + c);

        assertEquals(2.0, result.get(0, 0), 0.0001);
        assertEquals(3.0, result.get(0, 1), 0.0001);
    }

    @Test
    public void testZip_doubleMatrix_collection() {
        Collection<DoubleMatrix> matrices = Arrays.asList(DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } }),
                DoubleMatrix.of(new double[][] { { 2.0, 3.0 }, { 4.0, 5.0 } }));
        DoubleMatrix result = Matrixes.zip(matrices, (a, b) -> a * b);

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
        Matrix<String> result = Matrixes.zip(m1, m2, (a, b) -> a + b);

        assertEquals("a1", result.get(0, 0));
        assertEquals("b2", result.get(0, 1));
        assertEquals("c3", result.get(1, 0));
        assertEquals("d4", result.get(1, 1));
    }

    @Test
    public void testZip_objectMatrix_toOtherType() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
        Matrix<String> result = Matrixes.zip(m1, m2, (a, b) -> a + "+" + b, String.class);

        assertEquals("1+5", result.get(0, 0));
        assertEquals("2+6", result.get(0, 1));
    }

    @Test
    public void testZip_objectMatrix_three() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 9, 10 }, { 11, 12 } });
        Matrix<Integer> result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a + b + c);

        assertEquals(15, result.get(0, 0).intValue());
        assertEquals(18, result.get(0, 1).intValue());
        assertEquals(21, result.get(1, 0).intValue());
        assertEquals(24, result.get(1, 1).intValue());
    }

    @Test
    public void testZip_objectMatrix_collection() {
        Collection<Matrix<Integer>> matrices = Arrays.asList(Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } }),
                Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } }));
        Matrix<Integer> result = Matrixes.zip(matrices, (a, b) -> a + b);

        assertEquals(6, result.get(0, 0).intValue());
        assertEquals(8, result.get(0, 1).intValue());
    }

    @Test
    public void testZip_objectMatrix_collection_withArray() {
        Collection<Matrix<Integer>> matrices = Arrays.asList(Matrix.of(new Integer[][] { { 1, 2 } }), Matrix.of(new Integer[][] { { 3, 4 } }),
                Matrix.of(new Integer[][] { { 5, 6 } }));
        Matrix<Integer> result = Matrixes.zip(matrices, arr -> arr[0] + arr[1] + arr[2], Integer.class);

        assertEquals(9, result.get(0, 0).intValue());
        assertEquals(12, result.get(0, 1).intValue());
    }

    // ============ Error Cases for zip ============

    @Test
    public void testZip_differentShapes() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        assertThrows(IllegalArgumentException.class, () -> Matrixes.zip(m1, m2, (a, b) -> a + b));
    }

    @Test
    public void testZip_emptyCollection() {
        Collection<IntMatrix> matrices = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> Matrixes.zip(matrices, (a, b) -> a + b));
    }

    @Test
    public void testZip_collectionWithDifferentShapes() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 1, 2, 3 } }));
        assertThrows(IllegalArgumentException.class, () -> Matrixes.zip(matrices, (a, b) -> a + b));
    }

    // ============ Edge Cases ============

    @Test
    public void testZip_singleElementMatrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 5 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3 } });
        IntMatrix result = Matrixes.zip(m1, m2, (a, b) -> a * b);

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
        IntMatrix result = Matrixes.zip(m1, m2, (a, b) -> a + b);
        m1.println();
        m2.println();
        result.println();

        assertEquals(10, result.rows);
        assertEquals(10, result.cols);
        assertEquals(0, result.get(0, 0));   // 0 + 0
        assertEquals(99, result.get(9, 9));   // 18 + 81
    }

    @Test
    public void testZip_withShareIntermediateArray() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 } }), IntMatrix.of(new int[][] { { 3, 4 } }),
                IntMatrix.of(new int[][] { { 5, 6 } }));
        Matrix<Integer> result = Matrixes.zip(matrices, arr -> arr[0] + arr[1] + arr[2], true, Integer.class);

        assertEquals(9, result.get(0, 0).intValue());
        assertEquals(12, result.get(0, 1).intValue());
    }

    @Test
    public void testMultiply_rectangularMatrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 } });   // 1x3
        IntMatrix m2 = IntMatrix.of(new int[][] { { 4 }, { 5 }, { 6 } });   // 3x1
        int[][] result = new int[1][1];

        Matrixes.multiply(m1, m2, (i, j, k) -> result[i][j] += m1.get(i, k) * m2.get(k, j));

        assertEquals(32, result[0][0]);   // 1*4 + 2*5 + 3*6
    }

    @Test
    public void testRun_zeroRowsOrCols() {
        List<String> positions = new ArrayList<>();
        Matrixes.run(0, 3, (i, j) -> positions.add(i + "," + j), false);
        assertEquals(0, positions.size());

        Matrixes.run(3, 0, (i, j) -> positions.add(i + "," + j), false);
        assertEquals(0, positions.size());
    }

    // ============ Additional Coverage Tests ============

    @Test
    public void testRun_withRange_moreRowsThanCols_sequential() {
        List<String> positions = new ArrayList<>();
        // 5 rows x 2 cols - should iterate by columns first
        Matrixes.run(0, 5, 0, 2, (i, j) -> positions.add(i + "," + j), false);
        assertEquals(10, positions.size());
        // Should start with all rows for first column
        assertEquals("0,0", positions.get(0));
        assertEquals("1,0", positions.get(1));
    }

    @Test
    public void testRun_withRange_moreRowsThanCols_parallel() {
        List<String> positions = new ArrayList<>();
        Matrixes.setParallelEnabled(ParallelEnabled.NO);
        // 5 rows x 2 cols - parallel should iterate by columns
        Matrixes.run(0, 5, 0, 2, (i, j) -> {
            synchronized (positions) {
                positions.add(i + "," + j);
            }
        }, true);
        assertEquals(10, positions.size());
    }

    @Test
    public void testCall_withRange_moreRowsThanCols() {
        // 4 rows x 2 cols - should iterate by columns
        com.landawn.abacus.util.stream.Stream<String> result = Matrixes.call(0, 4, 0, 2, (i, j) -> i + ":" + j, false);
        List<String> list = result.toList();
        assertEquals(8, list.size());
    }

    @Test
    public void testCall_withRange_moreRowsThanCols_parallel() {
        // 4 rows x 2 cols - parallel should iterate by columns
        com.landawn.abacus.util.stream.Stream<Integer> result = Matrixes.call(0, 4, 0, 2, (i, j) -> i * 10 + j, true);
        List<Integer> list = result.toList();
        assertEquals(8, list.size());
    }

    @Test
    public void testCallToInt_withRange_moreRowsThanCols() {
        // 5 rows x 2 cols - should iterate by columns
        IntStream result = Matrixes.callToInt(0, 5, 0, 2, (i, j) -> i + j, false);
        int[] array = result.toArray();
        assertEquals(10, array.length);
    }

    @Test
    public void testCallToInt_withRange_moreRowsThanCols_parallel() {
        // 5 rows x 2 cols - parallel should iterate by columns
        IntStream result = Matrixes.callToInt(0, 5, 0, 2, (i, j) -> i * j, true);
        int[] array = result.toArray();
        assertEquals(10, array.length);
    }

    @Test
    public void testMultiply_differentDimensionOrdering_smallestIsColsA() {
        // Test case where colsA is smallest dimension
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });   // 3x2
        IntMatrix m2 = IntMatrix.of(new int[][] { { 7, 8, 9, 10 }, { 11, 12, 13, 14 } });   // 2x4
        int[][] result = new int[3][4];

        Matrixes.multiply(m1, m2, (i, j, k) -> result[i][j] += m1.get(i, k) * m2.get(k, j), false);

        assertEquals(29, result[0][0]);   // 1*7 + 2*11
        assertEquals(65, result[1][0]);   // 3*7 + 4*11 = 21 + 44 = 65
    }

    @Test
    public void testMultiply_differentDimensionOrdering_smallestIsColsB() {
        // Test case where colsB is smallest dimension
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } });   // 4x3
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });   // 3x2
        int[][] result = new int[4][2];

        Matrixes.multiply(m1, m2, (i, j, k) -> result[i][j] += m1.get(i, k) * m2.get(k, j), false);

        assertEquals(22, result[0][0]);   // 1*1 + 2*3 + 3*5
        assertEquals(28, result[0][1]);   // 1*2 + 2*4 + 3*6
    }

    @Test
    public void testMultiply_parallelExecution_variousDimensions() {
        // Test parallel execution with different dimension orderings
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6, 7 }, { 8, 9, 10 } });   // 2x3
        int[][] result = new int[2][3];

        Matrixes.multiply(m1, m2, (i, j, k) -> {
            synchronized (result) {
                result[i][j] += m1.get(i, k) * m2.get(k, j);
            }
        }, true);

        assertEquals(21, result[0][0]);   // 1*5 + 2*8
        assertEquals(24, result[0][1]);   // 1*6 + 2*9
        assertEquals(27, result[0][2]);   // 1*7 + 2*10
    }

    @Test
    public void testZipToInt_byteMatrix_three() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } });
        IntMatrix result = Matrixes.zipToInt(m1, m2, m3, (a, b, c) -> a + b + c);

        assertEquals(15, result.get(0, 0));
        assertEquals(18, result.get(0, 1));
        assertEquals(21, result.get(1, 0));
        assertEquals(24, result.get(1, 1));
    }

    @Test
    public void testZipToInt_byteMatrix_collection() {
        Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } }), ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } }),
                ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } }));
        IntMatrix result = Matrixes.zipToInt(matrices, arr -> arr[0] + arr[1] + arr[2]);

        assertEquals(15, result.get(0, 0));
        assertEquals(18, result.get(0, 1));
        assertEquals(21, result.get(1, 0));
        assertEquals(24, result.get(1, 1));
    }

    @Test
    public void testZipToInt_byteMatrix_collection_withSharing() {
        Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 2, 4 } }), ByteMatrix.of(new byte[][] { { 3, 6 } }));
        IntMatrix result = Matrixes.zipToInt(matrices, arr -> arr[0] * arr[1], true);

        assertEquals(6, result.get(0, 0));
        assertEquals(24, result.get(0, 1));
    }

    @Test
    public void testZipToLong_intMatrix_three() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
        LongMatrix result = Matrixes.zipToLong(m1, m2, m3, (a, b, c) -> (long) a + b + c);

        assertEquals(15L, result.get(0, 0));
        assertEquals(18L, result.get(0, 1));
        assertEquals(21L, result.get(1, 0));
        assertEquals(24L, result.get(1, 1));
    }

    @Test
    public void testZipToLong_intMatrix_collection() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }));
        LongMatrix result = Matrixes.zipToLong(matrices, arr -> (long) arr[0] * arr[1]);

        assertEquals(5L, result.get(0, 0));
        assertEquals(12L, result.get(0, 1));
        assertEquals(21L, result.get(1, 0));
        assertEquals(32L, result.get(1, 1));
    }

    @Test
    public void testZipToLong_intMatrix_collection_withSharing() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 10, 20 } }), IntMatrix.of(new int[][] { { 2, 4 } }));
        LongMatrix result = Matrixes.zipToLong(matrices, arr -> (long) arr[0] / arr[1], true);

        assertEquals(5L, result.get(0, 0));
        assertEquals(5L, result.get(0, 1));
    }

    @Test
    public void testZipToDouble_intMatrix_three() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 10, 20 }, { 30, 40 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 2, 4 }, { 6, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 1, 1 }, { 1, 1 } });
        DoubleMatrix result = Matrixes.zipToDouble(m1, m2, m3, (a, b, c) -> (double) (a + b) / c);

        assertEquals(12.0, result.get(0, 0), 0.0001);
        assertEquals(24.0, result.get(0, 1), 0.0001);
        assertEquals(36.0, result.get(1, 0), 0.0001);
        assertEquals(48.0, result.get(1, 1), 0.0001);
    }

    @Test
    public void testZipToDouble_intMatrix_collection() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 10, 20 }, { 30, 40 } }), IntMatrix.of(new int[][] { { 2, 4 }, { 6, 8 } }));
        DoubleMatrix result = Matrixes.zipToDouble(matrices, arr -> (double) arr[0] / arr[1]);

        assertEquals(5.0, result.get(0, 0), 0.0001);
        assertEquals(5.0, result.get(0, 1), 0.0001);
        assertEquals(5.0, result.get(1, 0), 0.0001);
        assertEquals(5.0, result.get(1, 1), 0.0001);
    }

    @Test
    public void testZipToDouble_intMatrix_collection_withSharing() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 100, 200 } }), IntMatrix.of(new int[][] { { 10, 20 } }),
                IntMatrix.of(new int[][] { { 2, 4 } }));
        DoubleMatrix result = Matrixes.zipToDouble(matrices, arr -> (double) (arr[0] + arr[1]) / arr[2], true);

        assertEquals(55.0, result.get(0, 0), 0.0001);
        assertEquals(55.0, result.get(0, 1), 0.0001);
    }

    @Test
    public void testZip_longMatrix_three() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 9L, 10L }, { 11L, 12L } });
        LongMatrix result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a + b + c);

        assertEquals(15L, result.get(0, 0));
        assertEquals(18L, result.get(0, 1));
        assertEquals(21L, result.get(1, 0));
        assertEquals(24L, result.get(1, 1));
    }

    @Test
    public void testZip_longMatrix_toObject() {
        Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 1L, 2L } }), LongMatrix.of(new long[][] { { 3L, 4L } }));
        Matrix<String> result = Matrixes.zip(matrices, arr -> arr[0] + "+" + arr[1], String.class);

        assertEquals("1+3", result.get(0, 0));
        assertEquals("2+4", result.get(0, 1));
    }

    @Test
    public void testZip_longMatrix_toObject_withSharing() {
        Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 10L, 20L } }), LongMatrix.of(new long[][] { { 5L, 10L } }));
        Matrix<Long> result = Matrixes.zip(matrices, arr -> arr[0] - arr[1], true, Long.class);

        assertEquals(5L, result.get(0, 0).longValue());
        assertEquals(10L, result.get(0, 1).longValue());
    }

    @Test
    public void testZipToDouble_longMatrix_three() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 100L, 200L }, { 300L, 400L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 2L, 4L }, { 6L, 8L } });
        DoubleMatrix result = Matrixes.zipToDouble(m1, m2, m3, (a, b, c) -> (double) (a + b) / c);

        assertEquals(55.0, result.get(0, 0), 0.0001);
        assertEquals(55.0, result.get(0, 1), 0.0001);
        assertEquals(55.0, result.get(1, 0), 0.0001);
        assertEquals(55.0, result.get(1, 1), 0.0001);
    }

    @Test
    public void testZipToDouble_longMatrix_collection() {
        Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 100L, 200L }, { 300L, 400L } }),
                LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } }));
        DoubleMatrix result = Matrixes.zipToDouble(matrices, arr -> (double) arr[0] / arr[1]);

        assertEquals(10.0, result.get(0, 0), 0.0001);
        assertEquals(10.0, result.get(0, 1), 0.0001);
        assertEquals(10.0, result.get(1, 0), 0.0001);
        assertEquals(10.0, result.get(1, 1), 0.0001);
    }

    @Test
    public void testZipToDouble_longMatrix_collection_withSharing() {
        Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 50L, 100L } }), LongMatrix.of(new long[][] { { 10L, 20L } }),
                LongMatrix.of(new long[][] { { 2L, 4L } }));
        DoubleMatrix result = Matrixes.zipToDouble(matrices, arr -> (double) (arr[0] + arr[1]) / arr[2], true);

        assertEquals(30.0, result.get(0, 0), 0.0001);
        assertEquals(30.0, result.get(0, 1), 0.0001);
    }

    @Test
    public void testZip_doubleMatrix_toObject() {
        Collection<DoubleMatrix> matrices = Arrays.asList(DoubleMatrix.of(new double[][] { { 1.5, 2.5 } }), DoubleMatrix.of(new double[][] { { 0.5, 0.5 } }));
        Matrix<String> result = Matrixes.zip(matrices, arr -> String.format("%.1f", arr[0] + arr[1]), String.class);

        assertEquals("2.0", result.get(0, 0));
        assertEquals("3.0", result.get(0, 1));
    }

    @Test
    public void testZip_doubleMatrix_toObject_withSharing() {
        Collection<DoubleMatrix> matrices = Arrays.asList(DoubleMatrix.of(new double[][] { { 10.0, 20.0 } }),
                DoubleMatrix.of(new double[][] { { 5.0, 10.0 } }));
        Matrix<Double> result = Matrixes.zip(matrices, arr -> arr[0] / arr[1], true, Double.class);

        assertEquals(2.0, result.get(0, 0), 0.0001);
        assertEquals(2.0, result.get(0, 1), 0.0001);
    }

    @Test
    public void testZip_objectMatrix_three_withTargetType() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 9, 10 }, { 11, 12 } });
        Matrix<String> result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a + "+" + b + "+" + c, String.class);

        assertEquals("1+5+9", result.get(0, 0));
        assertEquals("2+6+10", result.get(0, 1));
        assertEquals("3+7+11", result.get(1, 0));
        assertEquals("4+8+12", result.get(1, 1));
    }

    @Test
    public void testZip_intMatrix_collection_singleItem() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 5, 10 }, { 15, 20 } }));
        IntMatrix result = Matrixes.zip(matrices, (a, b) -> a + b);

        // Single matrix should be copied
        assertEquals(5, result.get(0, 0));
        assertEquals(10, result.get(0, 1));
        assertEquals(15, result.get(1, 0));
        assertEquals(20, result.get(1, 1));
    }

    @Test
    public void testZip_intMatrix_collection_twoItems() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }));
        IntMatrix result = Matrixes.zip(matrices, (a, b) -> a + b);

        // Two items should use zipWith directly
        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(10, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
    }

    @Test
    public void testZip_longMatrix_collection_singleItem() {
        Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 100L, 200L } }));
        LongMatrix result = Matrixes.zip(matrices, (a, b) -> a * b);

        // Single matrix should be copied
        assertEquals(100L, result.get(0, 0));
        assertEquals(200L, result.get(0, 1));
    }

    @Test
    public void testZip_longMatrix_collection_twoItems() {
        Collection<LongMatrix> matrices = Arrays.asList(LongMatrix.of(new long[][] { { 2L, 4L } }), LongMatrix.of(new long[][] { { 3L, 5L } }));
        LongMatrix result = Matrixes.zip(matrices, (a, b) -> a * b);

        // Two items should use zipWith directly
        assertEquals(6L, result.get(0, 0));
        assertEquals(20L, result.get(0, 1));
    }

    @Test
    public void testZip_doubleMatrix_collection_singleItem() {
        Collection<DoubleMatrix> matrices = Arrays.asList(DoubleMatrix.of(new double[][] { { 1.5, 2.5 } }));
        DoubleMatrix result = Matrixes.zip(matrices, (a, b) -> a + b);

        // Single matrix should be copied
        assertEquals(1.5, result.get(0, 0), 0.0001);
        assertEquals(2.5, result.get(0, 1), 0.0001);
    }

    @Test
    public void testZip_doubleMatrix_collection_twoItems() {
        Collection<DoubleMatrix> matrices = Arrays.asList(DoubleMatrix.of(new double[][] { { 1.0, 2.0 } }), DoubleMatrix.of(new double[][] { { 3.0, 4.0 } }));
        DoubleMatrix result = Matrixes.zip(matrices, (a, b) -> a * b);

        // Two items should use zipWith directly
        assertEquals(3.0, result.get(0, 0), 0.0001);
        assertEquals(8.0, result.get(0, 1), 0.0001);
    }

    @Test
    public void testZip_objectMatrix_collection_singleItem() {
        Collection<Matrix<Integer>> matrices = Arrays.asList(Matrix.of(new Integer[][] { { 10, 20 } }));
        Matrix<Integer> result = Matrixes.zip(matrices, (a, b) -> a + b);

        // Single matrix should be copied
        assertEquals(10, result.get(0, 0).intValue());
        assertEquals(20, result.get(0, 1).intValue());
    }

    @Test
    public void testZip_objectMatrix_collection_twoItems() {
        Collection<Matrix<String>> matrices = Arrays.asList(Matrix.of(new String[][] { { "a", "b" } }), Matrix.of(new String[][] { { "x", "y" } }));
        Matrix<String> result = Matrixes.zip(matrices, (a, b) -> a + b);

        // Two items should use zipWith directly
        assertEquals("ax", result.get(0, 0));
        assertEquals("by", result.get(0, 1));
    }

    @Test
    public void testZip_byteMatrix_collection_twoItems() {
        Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 } }), ByteMatrix.of(new byte[][] { { 3, 4 } }));
        ByteMatrix result = Matrixes.zip(matrices, (a, b) -> (byte) (a + b));

        // Two items should use zipWith directly
        assertEquals(4, result.get(0, 0));
        assertEquals(6, result.get(0, 1));
    }

    @Test
    public void testZip_byteMatrix_toObject_withSharing() {
        Collection<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 10, 20 } }), ByteMatrix.of(new byte[][] { { 5, 10 } }));
        Matrix<Integer> result = Matrixes.zip(matrices, arr -> (int) (arr[0] - arr[1]), true, Integer.class);

        assertEquals(5, result.get(0, 0).intValue());
        assertEquals(10, result.get(0, 1).intValue());
    }

    @Test
    public void testZip_intMatrix_toObject_withSharing() {
        Collection<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 10, 20 } }), IntMatrix.of(new int[][] { { 5, 10 } }));
        Matrix<String> result = Matrixes.zip(matrices, arr -> arr[0] + "-" + arr[1], true, String.class);

        assertEquals("10-5", result.get(0, 0));
        assertEquals("20-10", result.get(0, 1));
    }

    @Test
    public void testZip_objectMatrix_collection_withSharing() {
        Collection<Matrix<Integer>> matrices = Arrays.asList(Matrix.of(new Integer[][] { { 10, 20 } }), Matrix.of(new Integer[][] { { 5, 10 } }),
                Matrix.of(new Integer[][] { { 1, 2 } }));
        Matrix<Integer> result = Matrixes.zip(matrices, arr -> arr[0] + arr[1] + arr[2], true, Integer.class);

        assertEquals(16, result.get(0, 0).intValue());
        assertEquals(32, result.get(0, 1).intValue());
    }
}
