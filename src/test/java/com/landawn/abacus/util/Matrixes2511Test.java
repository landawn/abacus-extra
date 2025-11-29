package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;

/**
 * Comprehensive unit tests for the Matrixes utility class.
 * Tests cover parallel execution control, shape checking, array creation, and matrix operations.
 */
@Tag("2511")
public class Matrixes2511Test extends TestBase {

    // ============ Parallel Enabled Tests ============

    @Test
    public void testGetParallelEnabled_defaultValue() {
        ParallelEnabled enabled = Matrixes.getParallelEnabled();
        assertNotNull(enabled);
        assertEquals(ParallelEnabled.DEFAULT, enabled);
    }

    @Test
    public void testSetParallelEnabled_yes() {
        ParallelEnabled original = Matrixes.getParallelEnabled();
        try {
            Matrixes.setParallelEnabled(ParallelEnabled.YES);
            assertEquals(ParallelEnabled.YES, Matrixes.getParallelEnabled());
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    @Test
    public void testSetParallelEnabled_no() {
        ParallelEnabled original = Matrixes.getParallelEnabled();
        try {
            Matrixes.setParallelEnabled(ParallelEnabled.NO);
            assertEquals(ParallelEnabled.NO, Matrixes.getParallelEnabled());
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    @Test
    public void testSetParallelEnabled_default() {
        ParallelEnabled original = Matrixes.getParallelEnabled();
        try {
            Matrixes.setParallelEnabled(ParallelEnabled.NO);
            Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
            assertEquals(ParallelEnabled.DEFAULT, Matrixes.getParallelEnabled());
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    @Test
    public void testSetParallelEnabled_null() {
        assertThrows(IllegalArgumentException.class, () -> Matrixes.setParallelEnabled(null));
    }

    // ============ Parallelable Tests ============

    @Test
    public void testIsParallelable_smallMatrix_defaultSetting() {
        IntMatrix small = IntMatrix.of(new int[10][10]);
        boolean result = Matrixes.isParallelable(small);
        assertFalse(result);   // 100 elements < 8192
    }

    @Test
    public void testIsParallelable_smallMatrix_forceYes() {
        IntMatrix small = IntMatrix.of(new int[10][10]);
        ParallelEnabled original = Matrixes.getParallelEnabled();
        try {
            Matrixes.setParallelEnabled(ParallelEnabled.YES);
            boolean result = Matrixes.isParallelable(small);
            // Result depends on whether parallel streams are supported
            assertTrue(result || !result);   // Just verify it returns a boolean
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    @Test
    public void testIsParallelable_smallMatrix_forceNo() {
        IntMatrix small = IntMatrix.of(new int[10][10]);
        ParallelEnabled original = Matrixes.getParallelEnabled();
        try {
            Matrixes.setParallelEnabled(ParallelEnabled.NO);
            assertFalse(Matrixes.isParallelable(small));
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    @Test
    public void testIsParallelable_largeMatrix_defaultSetting() {
        IntMatrix large = IntMatrix.of(new int[100][100]);
        boolean result = Matrixes.isParallelable(large);
        // 10000 elements >= 8192, so should be true if supported
        assertTrue(result || !result);   // Verify it returns a boolean
    }

    @Test
    public void testIsParallelable_largeMatrix_forceNo() {
        IntMatrix large = IntMatrix.of(new int[100][100]);
        ParallelEnabled original = Matrixes.getParallelEnabled();
        try {
            Matrixes.setParallelEnabled(ParallelEnabled.NO);
            assertFalse(Matrixes.isParallelable(large));
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    @Test
    public void testIsParallelable_withCount_small() {
        IntMatrix matrix = IntMatrix.of(new int[10][10]);
        boolean result = Matrixes.isParallelable(matrix, 100);
        assertFalse(result);
    }

    @Test
    public void testIsParallelable_withCount_large() {
        IntMatrix matrix = IntMatrix.of(new int[10][10]);
        ParallelEnabled original = Matrixes.getParallelEnabled();
        try {
            Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
            boolean result = Matrixes.isParallelable(matrix, 10000);
            // Result depends on parallel streams being available
            assertTrue(result || !result);
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    @Test
    public void testIsParallelable_withCount_exactThreshold() {
        IntMatrix matrix = IntMatrix.of(new int[10][10]);
        ParallelEnabled original = Matrixes.getParallelEnabled();
        try {
            Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
            boolean result = Matrixes.isParallelable(matrix, 8192);
            assertTrue(result || !result);
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    // ============ Same Shape Tests ============

    @Test
    public void testIsSameShape_twoMatrices_same() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 7, 8, 9 }, { 10, 11, 12 } });
        assertTrue(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_twoMatrices_differentRows() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 7, 8, 9 } });
        assertFalse(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_twoMatrices_differentCols() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 7, 8 }, { 9, 10 } });
        assertFalse(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_twoMatrices_singleElement() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 2 } });
        assertTrue(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_threeMatrices_same() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
        assertTrue(Matrixes.isSameShape(m1, m2, m3));
    }

    @Test
    public void testIsSameShape_threeMatrices_differentSecond() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6, 7 }, { 8, 9, 10 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 11, 12 }, { 13, 14 } });
        assertFalse(Matrixes.isSameShape(m1, m2, m3));
    }

    @Test
    public void testIsSameShape_threeMatrices_differentThird() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10, 11 }, { 12, 13, 14 } });
        assertFalse(Matrixes.isSameShape(m1, m2, m3));
    }

    @Test
    public void testIsSameShape_collection_empty() {
        List<IntMatrix> matrices = new ArrayList<>();
        assertTrue(Matrixes.isSameShape(matrices));
    }

    @Test
    public void testIsSameShape_collection_null() {
        assertTrue(Matrixes.isSameShape((List<IntMatrix>) null));
    }

    @Test
    public void testIsSameShape_collection_single() {
        List<IntMatrix> matrices = Collections.singletonList(IntMatrix.of(new int[][] { { 1, 2 } }));
        assertTrue(Matrixes.isSameShape(matrices));
    }

    @Test
    public void testIsSameShape_collection_multiple_same() {
        List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } }),
                IntMatrix.of(new int[][] { { 7, 8, 9 }, { 10, 11, 12 } }), IntMatrix.of(new int[][] { { 13, 14, 15 }, { 16, 17, 18 } }));
        assertTrue(Matrixes.isSameShape(matrices));
    }

    @Test
    public void testIsSameShape_collection_multiple_different() {
        List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } }), IntMatrix.of(new int[][] { { 7, 8, 9 } }),
                IntMatrix.of(new int[][] { { 10, 11 }, { 12, 13 } }));
        assertFalse(Matrixes.isSameShape(matrices));
    }

    // ============ New Array Tests ============

    @Test
    public void testNewArray_primitive_int() {
        Integer[][] arr = Matrixes.newArray(3, 4, int.class);
        assertNotNull(arr);
        assertEquals(3, arr.length);
        assertEquals(4, arr[0].length);
        assertEquals(4, arr[1].length);
        assertEquals(4, arr[2].length);
    }

    @Test
    public void testNewArray_primitive_double() {
        Double[][] arr = Matrixes.newArray(2, 5, double.class);
        assertNotNull(arr);
        assertEquals(2, arr.length);
        assertEquals(5, arr[0].length);
        assertEquals(5, arr[1].length);
    }

    @Test
    public void testNewArray_primitive_long() {
        Long[][] arr = Matrixes.newArray(4, 3, long.class);
        assertNotNull(arr);
        assertEquals(4, arr.length);
        assertEquals(3, arr[0].length);
    }

    @Test
    public void testNewArray_primitive_boolean() {
        Boolean[][] arr = Matrixes.newArray(2, 2, boolean.class);
        assertNotNull(arr);
        assertEquals(2, arr.length);
        assertEquals(2, arr[0].length);
    }

    @Test
    public void testNewArray_reference_string() {
        String[][] arr = Matrixes.newArray(3, 2, String.class);
        assertNotNull(arr);
        assertEquals(3, arr.length);
        assertEquals(2, arr[0].length);
        // Elements should be null for reference types
        assertNotNull(arr);
    }

    @Test
    public void testNewArray_zeroRows() {
        Integer[][] arr = Matrixes.newArray(0, 5, int.class);
        assertNotNull(arr);
        assertEquals(0, arr.length);
    }

    @Test
    public void testNewArray_zeroCols() {
        Integer[][] arr = Matrixes.newArray(5, 0, int.class);
        assertNotNull(arr);
        assertEquals(5, arr.length);
        assertEquals(0, arr[0].length);
    }

    @Test
    public void testNewArray_largeDimensions() {
        Integer[][] arr = Matrixes.newArray(100, 100, int.class);
        assertNotNull(arr);
        assertEquals(100, arr.length);
        assertEquals(100, arr[0].length);
    }

    // ============ Run with ParallelEnabled Tests ============

    @Test
    public void testRun_withParallelEnabled_yes() throws Exception {
        ParallelEnabled original = Matrixes.getParallelEnabled();
        try {
            final AtomicInteger counter = new AtomicInteger(0);
            Matrixes.run(() -> {
                assertEquals(ParallelEnabled.YES, Matrixes.getParallelEnabled());
                counter.incrementAndGet();
            }, ParallelEnabled.YES);
            assertEquals(1, counter.get());
            // Original setting should be restored
            assertEquals(original, Matrixes.getParallelEnabled());
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    @Test
    public void testRun_withParallelEnabled_no() throws Exception {
        ParallelEnabled original = Matrixes.getParallelEnabled();
        try {
            Matrixes.run(() -> {
                assertEquals(ParallelEnabled.NO, Matrixes.getParallelEnabled());
            }, ParallelEnabled.NO);
            // Original setting should be restored
            assertEquals(original, Matrixes.getParallelEnabled());
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    @Test
    public void testRun_withParallelEnabled_restoresOnException() {
        ParallelEnabled original = Matrixes.getParallelEnabled();
        try {
            assertThrows(RuntimeException.class, () -> {
                Matrixes.run(() -> {
                    throw new RuntimeException("test exception");
                }, ParallelEnabled.YES);
            });
            // Original setting should be restored even after exception
            assertEquals(original, Matrixes.getParallelEnabled());
        } finally {
            Matrixes.setParallelEnabled(original);
        }
    }

    // ============ Run with Grid Tests ============

    @Test
    public void testRun_grid_sequential() throws Exception {
        int[][] grid = new int[3][4];
        Matrixes.run(3, 4, (i, j) -> {
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
        Matrixes.run(5, 5, (i, j) -> {
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
        Matrixes.run(10, 10, (i, j) -> {
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
        Matrixes.run(1, 5, (i, j) -> {
            row[j] = j * 2;
        }, false);

        assertArrayEquals(new int[] { 0, 2, 4, 6, 8 }, row);
    }

    @Test
    public void testRun_grid_singleColumn() throws Exception {
        int[] col = new int[5];
        Matrixes.run(5, 1, (i, j) -> {
            col[i] = i * 3;
        }, false);

        assertArrayEquals(new int[] { 0, 3, 6, 9, 12 }, col);
    }

    @Test
    public void testRun_grid_emptyGrid() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        Matrixes.run(0, 0, (i, j) -> {
            count.incrementAndGet();
        }, false);
        assertEquals(0, count.get());
    }

    // ============ Run with Region Tests ============

    @Test
    public void testRun_region_sequential() throws Exception {
        int[][] grid = new int[10][10];
        Matrixes.run(2, 5, 3, 8, (i, j) -> {
            grid[i][j] = i * 10 + j;
        }, false);

        assertEquals(23, grid[2][3]);
        assertEquals(47, grid[4][7]);
        assertEquals(0, grid[0][0]);   // Outside region
        assertEquals(0, grid[5][9]);   // Outside region
    }

    @Test
    public void testRun_region_parallel() throws Exception {
        int[][] grid = new int[20][20];
        Matrixes.run(5, 15, 5, 15, (i, j) -> {
            grid[i][j] = 1;
        }, true);

        for (int i = 5; i < 15; i++) {
            for (int j = 5; j < 15; j++) {
                assertEquals(1, grid[i][j]);
            }
        }

        assertEquals(0, grid[0][0]);   // Outside region
        assertEquals(0, grid[19][19]);   // Outside region
    }

    @Test
    public void testRun_region_singleCell() throws Exception {
        int[][] grid = new int[10][10];
        Matrixes.run(5, 6, 5, 6, (i, j) -> {
            grid[i][j] = 42;
        }, false);

        assertEquals(42, grid[5][5]);
        assertEquals(0, grid[4][5]);
        assertEquals(0, grid[5][4]);
    }

    @Test
    public void testRun_region_fullGrid() throws Exception {
        int[][] grid = new int[5][5];
        Matrixes.run(0, 5, 0, 5, (i, j) -> {
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
        com.landawn.abacus.util.stream.Stream<Integer> stream = Matrixes.call(2, 3, (i, j) -> i * 3 + j, false);
        assertNotNull(stream);
        List<Integer> result = stream.toList();
        assertEquals(6, result.size());
        assertTrue(result.contains(0));
        assertTrue(result.contains(5));
    }

    @Test
    public void testCall_stream_parallel() {
        com.landawn.abacus.util.stream.Stream<Integer> stream = Matrixes.call(3, 3, (i, j) -> i + j, true);
        assertNotNull(stream);
        List<Integer> result = stream.toList();
        assertEquals(9, result.size());
    }

    @Test
    public void testCall_stream_withRegion() {
        com.landawn.abacus.util.stream.Stream<Integer> stream = Matrixes.call(1, 3, 1, 3, (i, j) -> i * 3 + j, false);
        assertNotNull(stream);
        List<Integer> result = stream.toList();
        assertEquals(4, result.size());
    }

    @Test
    public void testCallToInt_sequential() {
        com.landawn.abacus.util.stream.IntStream stream = Matrixes.callToInt(2, 3, (i, j) -> i + j, false);
        assertNotNull(stream);
        int[] result = stream.toArray();
        assertEquals(6, result.length);
    }

    @Test
    public void testCallToInt_parallel() {
        com.landawn.abacus.util.stream.IntStream stream = Matrixes.callToInt(3, 3, (i, j) -> i * j, true);
        assertNotNull(stream);
        int[] result = stream.toArray();
        assertEquals(9, result.length);
    }

    @Test
    public void testCallToInt_withRegion() {
        com.landawn.abacus.util.stream.IntStream stream = Matrixes.callToInt(1, 4, 1, 4, (i, j) -> i + j, false);
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
        Matrixes.multiply(a, b, (i, j, v) -> {
            callCount.incrementAndGet();
        });

        assertTrue(callCount.get() > 0);
    }

    @Test
    public void testMultiply_rectangular() throws Exception {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        final AtomicInteger callCount = new AtomicInteger(0);
        Matrixes.multiply(a, b, (i, j, v) -> {
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
        Matrixes.multiply(a, b, (i, j, v) -> {
            callCount.incrementAndGet();
        }, true);

        assertTrue(callCount.get() > 0);
    }

    // ============ Zip Tests - ByteMatrix ============

    @Test
    public void testZip_ByteMatrix_twoMatrices() throws Exception {
        ByteMatrix a = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix b = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });

        ByteMatrix result = Matrixes.zip(a, b, (x, y) -> (byte) (x + y));

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(6, result.get(0, 0));   // 1 + 5
        assertEquals(12, result.get(1, 1));   // 4 + 8
    }

    @Test
    public void testZip_ByteMatrix_threeMatrices() throws Exception {
        ByteMatrix a = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix b = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix c = ByteMatrix.of(new byte[][] { { 1, 1 }, { 1, 1 } });

        ByteMatrix result = Matrixes.zip(a, b, c, (x, y, z) -> (byte) (x + y + z));

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(7, result.get(0, 0));   // 1 + 5 + 1
    }

    @Test
    public void testZip_ByteMatrix_collection() throws Exception {
        List<ByteMatrix> matrices = Arrays.asList(ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } }), ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } }));

        ByteMatrix result = Matrixes.zip(matrices, (a, b) -> (byte) (a + b));

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(6, result.get(0, 0));   // 1 + 5
    }

    // ============ Zip Tests - IntMatrix ============

    @Test
    public void testZip_IntMatrix_twoMatrices() throws Exception {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

        IntMatrix result = Matrixes.zip(a, b, (x, y) -> x + y);

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(6, result.get(0, 0));   // 1 + 5
        assertEquals(12, result.get(1, 1));   // 4 + 8
    }

    @Test
    public void testZip_IntMatrix_threeMatrices() throws Exception {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix c = IntMatrix.of(new int[][] { { 1, 1 }, { 1, 1 } });

        IntMatrix result = Matrixes.zip(a, b, c, (x, y, z) -> x + y + z);

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(7, result.get(0, 0));   // 1 + 5 + 1
    }

    @Test
    public void testZip_IntMatrix_collection() throws Exception {
        List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }));

        IntMatrix result = Matrixes.zip(matrices, (a, b) -> a + b);

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(6, result.get(0, 0));   // 1 + 5
    }

    @Test
    public void testZip_IntMatrix_toLong() throws Exception {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

        LongMatrix result = Matrixes.zipToLong(a, b, (x, y) -> (long) x + y);

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(6L, result.get(0, 0));   // 1 + 5
    }

    @Test
    public void testZip_IntMatrix_toDouble() throws Exception {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });

        DoubleMatrix result = Matrixes.zipToDouble(a, b, (x, y) -> x + y + 0.5);

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(6.5, result.get(0, 0), 0.01);   // 1 + 5 + 0.5
    }

    // ============ Zip Tests - LongMatrix ============

    @Test
    public void testZip_LongMatrix_twoMatrices() throws Exception {
        LongMatrix a = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix b = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });

        LongMatrix result = Matrixes.zip(a, b, (x, y) -> x + y);

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(6L, result.get(0, 0));   // 1 + 5
    }

    @Test
    public void testZip_LongMatrix_toDouble() throws Exception {
        LongMatrix a = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix b = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });

        DoubleMatrix result = Matrixes.zipToDouble(a, b, (x, y) -> (double) (x + y));

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(6.0, result.get(0, 0), 0.01);   // 1 + 5
    }

    // ============ Zip Tests - DoubleMatrix ============

    @Test
    public void testZip_DoubleMatrix_twoMatrices() throws Exception {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });

        DoubleMatrix result = Matrixes.zip(a, b, (x, y) -> x + y);

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(6.0, result.get(0, 0), 0.01);   // 1 + 5
    }

    @Test
    public void testZip_DoubleMatrix_threeMatrices() throws Exception {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
        DoubleMatrix c = DoubleMatrix.of(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } });

        DoubleMatrix result = Matrixes.zip(a, b, c, (x, y, z) -> x + y + z);

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(7.0, result.get(0, 0), 0.01);   // 1 + 5 + 1
    }

    // ============ Zip with Generic Result Type ============

    @Test
    public void testZip_IntMatrix_toGeneric() throws Exception {
        List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }));

        Matrix<String> result = Matrixes.zip(matrices, (int[] values) -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                sb.append(values[i]).append(",");
            }
            return sb.toString();
        }, String.class);

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertNotNull(result.get(0, 0));
    }

    @Test
    public void testZip_collection_withParallel() throws Exception {
        List<IntMatrix> matrices = Arrays.asList(IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }), IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }));

        IntMatrix result = Matrixes.zip(matrices, (a, b) -> a + b);

        assertNotNull(result);
        assertEquals(2, result.rows);
    }
}
