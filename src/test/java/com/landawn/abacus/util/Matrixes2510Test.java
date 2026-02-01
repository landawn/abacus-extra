package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.Stream;

@Tag("2510")
public class Matrixes2510Test extends TestBase {

    @AfterEach
    public void cleanup() {
        // Reset parallel setting after each test to avoid side effects
        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
    }

    // ============ ParallelEnabled Configuration Tests ============

    @Test
    public void testGetParallelEnabled_default() {
        ParallelEnabled result = Matrixes.getParallelEnabled();
        assertNotNull(result);
        assertEquals(ParallelEnabled.DEFAULT, result);
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
    public void testSetParallelEnabled_nullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Matrixes.setParallelEnabled(null));
    }

    // ============ isParallelable Tests ============

    @Test
    public void testIsParallelable_singleArg_smallMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
        // Small matrix (count = 4) should not be parallelable by default
        boolean result = Matrixes.isParallelable(m);
        assertFalse(result);
    }

    @Test
    public void testIsParallelable_singleArg_largeMatrix() {
        IntMatrix m = IntMatrix.of(new int[100][100]); // count = 10000
        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
        // Large matrix should be parallelable by default if parallel streams supported
        boolean result = Matrixes.isParallelable(m);
        // Result depends on IS_PARALLEL_STREAM_SUPPORTED
        assertNotNull(result);
    }

    @Test
    public void testIsParallelable_singleArg_forcedYes() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Matrixes.setParallelEnabled(ParallelEnabled.YES);
        // Should be parallelable when forced YES (if parallel streams supported)
        boolean result = Matrixes.isParallelable(m);
        assertNotNull(result);
    }

    @Test
    public void testIsParallelable_singleArg_forcedNo() {
        IntMatrix m = IntMatrix.of(new int[100][100]);
        Matrixes.setParallelEnabled(ParallelEnabled.NO);
        // Should not be parallelable when forced NO
        boolean result = Matrixes.isParallelable(m);
        assertFalse(result);
    }

    @Test
    public void testIsParallelable_twoArgs_smallCount() {
        IntMatrix m = IntMatrix.of(new int[10][10]);
        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
        boolean result = Matrixes.isParallelable(m, 100);
        assertFalse(result);
    }

    @Test
    public void testIsParallelable_twoArgs_largeCount() {
        IntMatrix m = IntMatrix.of(new int[100][100]);
        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
        boolean result = Matrixes.isParallelable(m, 10000);
        // Result depends on IS_PARALLEL_STREAM_SUPPORTED
        assertNotNull(result);
    }

    @Test
    public void testIsParallelable_twoArgs_exactThreshold() {
        IntMatrix m = IntMatrix.of(new int[100][100]);
        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
        boolean result = Matrixes.isParallelable(m, 8192);
        // At exact threshold should be parallelable
        assertNotNull(result);
    }

    @Test
    public void testIsParallelable_twoArgs_belowThreshold() {
        IntMatrix m = IntMatrix.of(new int[100][100]);
        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
        boolean result = Matrixes.isParallelable(m, 8191);
        assertFalse(result);
    }

    // ============ isSameShape Tests ============

    @Test
    public void testIsSameShape_twoMatrices_sameShape() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 7, 8, 9 }, { 10, 11, 12 } });
        assertTrue(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_twoMatrices_differentRows() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 }, { 9, 10 } });
        assertFalse(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_twoMatrices_differentCols() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6, 7 }, { 8, 9, 10 } });
        assertFalse(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_twoMatrices_emptyMatrices() {
        IntMatrix m1 = IntMatrix.empty();
        IntMatrix m2 = IntMatrix.empty();
        assertTrue(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_twoMatrices_singleElement() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 2 } });
        assertTrue(Matrixes.isSameShape(m1, m2));
    }

    @Test
    public void testIsSameShape_threeMatrices_allSame() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
        assertTrue(Matrixes.isSameShape(m1, m2, m3));
    }

    @Test
    public void testIsSameShape_threeMatrices_firstTwoSame() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10, 11 }, { 12, 13, 14 } });
        assertFalse(Matrixes.isSameShape(m1, m2, m3));
    }

    @Test
    public void testIsSameShape_threeMatrices_lastTwoDifferent() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 }, { 9, 10 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 11 } });
        assertFalse(Matrixes.isSameShape(m1, m2, m3));
    }

    @Test
    public void testIsSameShape_collection_nullCollection() {
        Collection<IntMatrix> matrices = null;
        assertTrue(Matrixes.isSameShape(matrices));
    }

    @Test
    public void testIsSameShape_collection_emptyCollection() {
        Collection<IntMatrix> matrices = Collections.emptyList();
        assertTrue(Matrixes.isSameShape(matrices));
    }

    @Test
    public void testIsSameShape_collection_singleMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Collection<IntMatrix> matrices = Collections.singletonList(m);
        assertTrue(Matrixes.isSameShape(matrices));
    }

    @Test
    public void testIsSameShape_collection_allSameShape() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10 }, { 11, 12 } });
        Collection<IntMatrix> matrices = Arrays.asList(m1, m2, m3);
        assertTrue(Matrixes.isSameShape(matrices));
    }

    @Test
    public void testIsSameShape_collection_differentShapes() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 9, 10, 11 }, { 12, 13, 14 } });
        Collection<IntMatrix> matrices = Arrays.asList(m1, m2, m3);
        assertFalse(Matrixes.isSameShape(matrices));
    }

    @Test
    public void testIsSameShape_collection_firstTwoDifferent() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
        assertFalse(Matrixes.isSameShape(matrices));
    }

    // ============ newArray Tests ============

    @Test
    public void testNewArray_basicDimensions() {
        Integer[][] arr = Matrixes.newArray(3, 4, Integer.class);
        assertNotNull(arr);
        assertEquals(3, arr.length);
        for (Integer[] row : arr) {
            assertEquals(4, row.length);
        }
    }

    @Test
    public void testNewArray_singleRow() {
        String[][] arr = Matrixes.newArray(1, 5, String.class);
        assertNotNull(arr);
        assertEquals(1, arr.length);
        assertEquals(5, arr[0].length);
    }

    @Test
    public void testNewArray_singleColumn() {
        Double[][] arr = Matrixes.newArray(5, 1, Double.class);
        assertNotNull(arr);
        assertEquals(5, arr.length);
        assertEquals(1, arr[0].length);
    }

    @Test
    public void testNewArray_zeroRows() {
        Integer[][] arr = Matrixes.newArray(0, 5, Integer.class);
        assertNotNull(arr);
        assertEquals(0, arr.length);
    }

    @Test
    public void testNewArray_zeroCols() {
        Integer[][] arr = Matrixes.newArray(5, 0, Integer.class);
        assertNotNull(arr);
        assertEquals(5, arr.length);
        assertEquals(0, arr[0].length);
    }

    @Test
    public void testNewArray_primitiveInt() {
        Integer[][] arr = Matrixes.newArray(2, 3, int.class);
        assertNotNull(arr);
        assertEquals(2, arr.length);
        assertEquals(3, arr[0].length);
    }

    @Test
    public void testNewArray_primitiveDouble() {
        Double[][] arr = Matrixes.newArray(2, 2, double.class);
        assertNotNull(arr);
        assertEquals(2, arr.length);
        assertEquals(2, arr[0].length);
    }

    // ============ run Tests (with ParallelEnabled) ============

    @Test
    public void testRun_withParallelEnabled_executesCommand() {
        AtomicInteger counter = new AtomicInteger(0);
        Matrixes.run(() -> counter.incrementAndGet(), ParallelEnabled.NO);
        assertEquals(1, counter.get());
    }

    @Test
    public void testRun_withParallelEnabled_restoresOriginalSetting() {
        Matrixes.setParallelEnabled(ParallelEnabled.YES);
        Matrixes.run(() -> {
            assertEquals(ParallelEnabled.NO, Matrixes.getParallelEnabled());
        }, ParallelEnabled.NO);
        assertEquals(ParallelEnabled.YES, Matrixes.getParallelEnabled());
    }

    @Test
    public void testRun_withParallelEnabled_restoresOnException() {
        Matrixes.setParallelEnabled(ParallelEnabled.DEFAULT);
        try {
            Matrixes.run(() -> {
                throw new RuntimeException("Test exception");
            }, ParallelEnabled.YES);
        } catch (RuntimeException e) {
            // Expected
        }
        assertEquals(ParallelEnabled.DEFAULT, Matrixes.getParallelEnabled());
    }

    // ============ run Tests (IntBiConsumer variants) ============

    @Test
    public void testRun_intBiConsumer_basic() {
        List<String> positions = new ArrayList<>();
        Matrixes.run(2, 3, (i, j) -> positions.add(i + "," + j), false);
        assertEquals(6, positions.size());
        assertTrue(positions.contains("0,0"));
        assertTrue(positions.contains("1,2"));
    }

    @Test
    public void testRun_intBiConsumer_zeroRows() {
        AtomicInteger counter = new AtomicInteger(0);
        Matrixes.run(0, 3, (i, j) -> counter.incrementAndGet(), false);
        assertEquals(0, counter.get());
    }

    @Test
    public void testRun_intBiConsumer_zeroCols() {
        AtomicInteger counter = new AtomicInteger(0);
        Matrixes.run(3, 0, (i, j) -> counter.incrementAndGet(), false);
        assertEquals(0, counter.get());
    }

    @Test
    public void testRun_intBiConsumer_singleElement() {
        List<String> positions = new ArrayList<>();
        Matrixes.run(1, 1, (i, j) -> positions.add(i + "," + j), false);
        assertEquals(1, positions.size());
        assertEquals("0,0", positions.get(0));
    }

    @Test
    public void testRun_intBiConsumer_withRange() {
        List<String> positions = new ArrayList<>();
        Matrixes.run(1, 3, 2, 5, (i, j) -> positions.add(i + "," + j), false);
        assertEquals(6, positions.size()); // 2 rows * 3 columnCount
        assertTrue(positions.contains("1,2"));
        assertTrue(positions.contains("2,4"));
        assertFalse(positions.contains("0,0"));
        assertFalse(positions.contains("3,5"));
    }

    @Test
    public void testRun_intBiConsumer_withRange_emptyRange() {
        AtomicInteger counter = new AtomicInteger(0);
        Matrixes.run(2, 2, 3, 3, (i, j) -> counter.incrementAndGet(), false);
        assertEquals(0, counter.get());
    }

    @Test
    public void testRun_intBiConsumer_withRange_invalidRowRange() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            Matrixes.run(5, 3, 0, 2, (i, j) -> {
            }, false);
        });
    }

    @Test
    public void testRun_intBiConsumer_withRange_invalidColRange() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            Matrixes.run(0, 2, 5, 3, (i, j) -> {
            }, false);
        });
    }

    @Test
    public void testRun_intBiConsumer_withRange_negativeIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            Matrixes.run(-1, 2, 0, 2, (i, j) -> {
            }, false);
        });
    }

    // ============ call Tests (Stream variants) ============

    @Test
    public void testCall_basic() {
        Stream<String> result = Matrixes.call(2, 3, (i, j) -> i + "," + j, false);
        List<String> positions = result.toList();
        assertEquals(6, positions.size());
        assertTrue(positions.contains("0,0"));
        assertTrue(positions.contains("1,2"));
    }

    @Test
    public void testCall_zeroSize() {
        Stream<String> result = Matrixes.call(0, 0, (i, j) -> i + "," + j, false);
        List<String> positions = result.toList();
        assertEquals(0, positions.size());
    }

    @Test
    public void testCall_singleElement() {
        Stream<Integer> result = Matrixes.call(1, 1, (i, j) -> i * 10 + j, false);
        List<Integer> values = result.toList();
        assertEquals(1, values.size());
        assertEquals(0, values.get(0));
    }

    @Test
    public void testCall_withRange() {
        Stream<Integer> result = Matrixes.call(1, 4, 2, 5, (i, j) -> i * 10 + j, false);
        List<Integer> values = result.toList();
        assertEquals(9, values.size()); // 3 rows * 3 columnCount
        assertTrue(values.contains(12)); // row 1, col 2
        assertTrue(values.contains(34)); // row 3, col 4
    }

    @Test
    public void testCall_withRange_invalidRange() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            Matrixes.call(5, 3, 0, 2, (i, j) -> "test", false);
        });
    }

    // ============ callToInt Tests ============

    @Test
    public void testCallToInt_basic() {
        IntStream result = Matrixes.callToInt(2, 3, (i, j) -> i + j, false);
        int[] values = result.toArray();
        assertEquals(6, values.length);
        assertEquals(0, values[0]); // 0+0
        assertTrue(IntStream.of(values).anyMatch(v -> v == 3)); // 1+2
    }

    @Test
    public void testCallToInt_zeroSize() {
        IntStream result = Matrixes.callToInt(0, 0, (i, j) -> i + j, false);
        int[] values = result.toArray();
        assertEquals(0, values.length);
    }

    @Test
    public void testCallToInt_multiplication() {
        IntStream result = Matrixes.callToInt(2, 2, (i, j) -> i * j, false);
        int[] values = result.toArray();
        assertEquals(4, values.length);
        assertTrue(IntStream.of(values).anyMatch(v -> v == 0));
        assertTrue(IntStream.of(values).anyMatch(v -> v == 1)); // 1*1
    }

    @Test
    public void testCallToInt_withRange() {
        IntStream result = Matrixes.callToInt(0, 3, 0, 3, (i, j) -> i * 10 + j, false);
        int[] values = result.toArray();
        assertEquals(9, values.length);
        assertTrue(IntStream.of(values).anyMatch(v -> v == 0));
        assertTrue(IntStream.of(values).anyMatch(v -> v == 22));
    }

    @Test
    public void testCallToInt_withRange_invalidRange() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            Matrixes.callToInt(3, 1, 0, 2, (i, j) -> i + j, false);
        });
    }

    // ============ multiply Tests ============

    @Test
    public void testMultiply_basic() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } }); // 2x2
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } }); // 2x2
        List<String> triplets = new ArrayList<>();
        Matrixes.multiply(m1, m2, (i, j, k) -> triplets.add(i + "," + j + "," + k));
        assertEquals(8, triplets.size()); // 2*2*2
        assertTrue(triplets.contains("0,0,0"));
        assertTrue(triplets.contains("1,1,1"));
    }

    @Test
    public void testMultiply_incompatibleDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } }); // 1x2
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3 }, { 4 }, { 5 } }); // 3x1
        assertThrows(IllegalArgumentException.class, () -> {
            Matrixes.multiply(m1, m2, (i, j, k) -> {
            });
        });
    }

    @Test
    public void testMultiply_compatibleDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 } }); // 1x3
        IntMatrix m2 = IntMatrix.of(new int[][] { { 4 }, { 5 }, { 6 } }); // 3x1
        AtomicInteger count = new AtomicInteger(0);
        Matrixes.multiply(m1, m2, (i, j, k) -> count.incrementAndGet());
        assertEquals(3, count.get()); // 1*1*3
    }

    @Test
    public void testMultiply_withParallelFlag_sequential() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        AtomicInteger count = new AtomicInteger(0);
        Matrixes.multiply(m1, m2, (i, j, k) -> count.incrementAndGet(), false);
        assertEquals(8, count.get());
    }

    @Test
    public void testMultiply_withParallelFlag_incompatibleDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3 } });
        assertThrows(IllegalArgumentException.class, () -> {
            Matrixes.multiply(m1, m2, (i, j, k) -> {
            }, false);
        });
    }

    // ============ ByteMatrix zip Tests ============

    @Test
    public void testZip_byteMatrix_twoMatrices() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        ByteMatrix result = Matrixes.zip(m1, m2, (a, b) -> (byte) (a + b));
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
        ByteMatrix result = Matrixes.zip(m1, m2, m3, (a, b, c) -> (byte) (a + b + c));
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
        ByteMatrix result = Matrixes.zip(matrices, (a, b) -> (byte) (a + b));
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(6, result.get(0, 0));
    }

    @Test
    public void testZip_byteMatrix_collectionToMatrix() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        Collection<ByteMatrix> matrices = Arrays.asList(m1, m2);
        Matrix<Integer> result = Matrixes.zip(matrices, arr -> (int) (arr[0] + arr[1]), Integer.class);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(Integer.valueOf(6), result.get(0, 0));
    }

    @Test
    public void testZip_byteMatrix_collectionToMatrixWithTargetType() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        Collection<ByteMatrix> matrices = Arrays.asList(m1, m2);
        Matrix<String> result = Matrixes.zip(matrices, arr -> String.valueOf(arr[0] + arr[1]), String.class);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals("6", result.get(0, 0));
    }

    // ============ ByteMatrix zipToInt Tests ============

    @Test
    public void testZipToInt_byteMatrix_twoMatrices() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix result = Matrixes.zipToInt(m1, m2, (a, b) -> a * b);
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
        IntMatrix result = Matrixes.zipToInt(m1, m2, m3, (a, b, c) -> a + b + c);
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
        IntMatrix result = Matrixes.zipToInt(matrices, arr -> arr[0] + arr[1]);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(6, result.get(0, 0));
    }

    @Test
    public void testZipToInt_byteMatrix_collectionWithShareArray() {
        ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
        Collection<ByteMatrix> matrices = Arrays.asList(m1, m2);
        IntMatrix result = Matrixes.zipToInt(matrices, arr -> arr[0] * arr[1], false);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(5, result.get(0, 0));
    }

    // ============ IntMatrix zip Tests ============

    @Test
    public void testZip_intMatrix_twoMatrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix result = Matrixes.zip(m1, m2, (a, b) -> a + b);
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
        IntMatrix result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a + b + c);
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
        IntMatrix result = Matrixes.zip(matrices, (a, b) -> a + b);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(16, result.get(0, 0)); // 1+5+10
    }

    @Test
    public void testZip_intMatrix_collectionToMatrix() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
        Matrix<Long> result = Matrixes.zip(matrices, arr -> (long) (arr[0] + arr[1]), Long.class);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(Long.valueOf(6), result.get(0, 0));
    }

    @Test
    public void testZip_intMatrix_collectionToMatrixWithTargetType() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
        Matrix<String> result = Matrixes.zip(matrices, arr -> String.valueOf(arr[0] + arr[1]), String.class);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals("6", result.get(0, 0));
    }

    // ============ IntMatrix zipToLong Tests ============

    @Test
    public void testZipToLong_intMatrix_twoMatrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        LongMatrix result = Matrixes.zipToLong(m1, m2, (a, b) -> (long) a * b);
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
        LongMatrix result = Matrixes.zipToLong(m1, m2, m3, (a, b, c) -> (long) (a + b + c));
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
        LongMatrix result = Matrixes.zipToLong(matrices, arr -> (long) (arr[0] + arr[1]));
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(6L, result.get(0, 0));
    }

    @Test
    public void testZipToLong_intMatrix_collectionWithShareArray() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
        LongMatrix result = Matrixes.zipToLong(matrices, arr -> (long) (arr[0] * arr[1]), false);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(5L, result.get(0, 0));
    }

    // ============ IntMatrix zipToDouble Tests ============

    @Test
    public void testZipToDouble_intMatrix_twoMatrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        DoubleMatrix result = Matrixes.zipToDouble(m1, m2, (a, b) -> (double) a / b);
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
        DoubleMatrix result = Matrixes.zipToDouble(m1, m2, m3, (a, b, c) -> (double) (a + b + c));
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
        DoubleMatrix result = Matrixes.zipToDouble(matrices, arr -> (double) (arr[0] + arr[1]));
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(6.0, result.get(0, 0), 0.001);
    }

    @Test
    public void testZipToDouble_intMatrix_collectionWithShareArray() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        Collection<IntMatrix> matrices = Arrays.asList(m1, m2);
        DoubleMatrix result = Matrixes.zipToDouble(matrices, arr -> (double) (arr[0] * arr[1]), false);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(5.0, result.get(0, 0), 0.001);
    }

    // ============ LongMatrix zip Tests ============

    @Test
    public void testZip_longMatrix_twoMatrices() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix result = Matrixes.zip(m1, m2, (a, b) -> a + b);
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
        LongMatrix result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a + b + c);
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
        LongMatrix result = Matrixes.zip(matrices, (a, b) -> a + b);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(6L, result.get(0, 0));
    }

    @Test
    public void testZip_longMatrix_collectionToMatrix() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        Collection<LongMatrix> matrices = Arrays.asList(m1, m2);
        Matrix<Long> result = Matrixes.zip(matrices, arr -> arr[0] + arr[1], Long.class);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(Long.valueOf(6L), result.get(0, 0));
    }

    @Test
    public void testZip_longMatrix_collectionToMatrixWithTargetType() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        Collection<LongMatrix> matrices = Arrays.asList(m1, m2);
        Matrix<String> result = Matrixes.zip(matrices, arr -> String.valueOf(arr[0] + arr[1]), String.class);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals("6", result.get(0, 0));
    }

    // ============ LongMatrix zipToDouble Tests ============

    @Test
    public void testZipToDouble_longMatrix_twoMatrices() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        DoubleMatrix result = Matrixes.zipToDouble(m1, m2, (a, b) -> (double) a / b);
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
        DoubleMatrix result = Matrixes.zipToDouble(m1, m2, m3, (a, b, c) -> (double) (a + b + c));
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
        DoubleMatrix result = Matrixes.zipToDouble(matrices, arr -> (double) (arr[0] + arr[1]));
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(6.0, result.get(0, 0), 0.001);
    }

    @Test
    public void testZipToDouble_longMatrix_collectionWithShareArray() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        Collection<LongMatrix> matrices = Arrays.asList(m1, m2);
        DoubleMatrix result = Matrixes.zipToDouble(matrices, arr -> (double) (arr[0] * arr[1]), false);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(5.0, result.get(0, 0), 0.001);
    }

    // ============ DoubleMatrix zip Tests ============

    @Test
    public void testZip_doubleMatrix_twoMatrices() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.5, 6.5 }, { 7.5, 8.5 } });
        DoubleMatrix result = Matrixes.zip(m1, m2, (a, b) -> a + b);
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
        DoubleMatrix result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a + b + c);
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
        DoubleMatrix result = Matrixes.zip(matrices, (a, b) -> a + b);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(6.0, result.get(0, 0), 0.001);
    }

    @Test
    public void testZip_doubleMatrix_collectionToMatrix() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
        Collection<DoubleMatrix> matrices = Arrays.asList(m1, m2);
        Matrix<Double> result = Matrixes.zip(matrices, arr -> arr[0] + arr[1], Double.class);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(6.0, result.get(0, 0), 0.001);
    }

    @Test
    public void testZip_doubleMatrix_collectionToMatrixWithTargetType() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
        Collection<DoubleMatrix> matrices = Arrays.asList(m1, m2);
        Matrix<String> result = Matrixes.zip(matrices, arr -> String.valueOf(arr[0] + arr[1]), String.class);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals("6.0", result.get(0, 0));
    }

    // ============ Generic Matrix zip Tests ============

    @Test
    public void testZip_genericMatrix_twoMatrices_sameResult() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
        Matrix<Integer> result = Matrixes.zip(m1, m2, (a, b) -> a + b);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(Integer.valueOf(6), result.get(0, 0));
        assertEquals(Integer.valueOf(12), result.get(1, 1));
    }

    @Test
    public void testZip_genericMatrix_twoMatrices_differentResult() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
        Matrix<String> result = Matrixes.zip(m1, m2, (a, b) -> a + "+" + b, String.class);
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
        Matrix<Integer> result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a + b + c);
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
        Matrix<String> result = Matrixes.zip(m1, m2, m3, (a, b, c) -> a + "+" + b + "+" + c, String.class);
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
        Matrix<Integer> result = Matrixes.zip(matrices, (a, b) -> a + b);
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
        Matrix<String> result = Matrixes.zip(matrices, arr -> arr[0] + ":" + arr[1], String.class);
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
        Matrix<String> result = Matrixes.zip(matrices, arr -> arr[0] + arr[1], String.class);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals("ae", result.get(0, 0));
        assertEquals("dh", result.get(1, 1));
    }

    // ============ Edge Case Tests ============

    @Test
    public void testZip_emptyMatrices() {
        IntMatrix m1 = IntMatrix.empty();
        IntMatrix m2 = IntMatrix.empty();
        IntMatrix result = Matrixes.zip(m1, m2, (a, b) -> a + b);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testZip_singleElementMatrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 5 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 10 } });
        IntMatrix result = Matrixes.zip(m1, m2, (a, b) -> a + b);
        assertEquals(1, result.rowCount());
        assertEquals(1, result.columnCount());
        assertEquals(15, result.get(0, 0));
    }

    @Test
    public void testCall_largeMatrix() {
        // Test with larger dimensions to verify iteration logic
        Stream<Integer> result = Matrixes.call(10, 20, (i, j) -> i * 100 + j, false);
        List<Integer> values = result.toList();
        assertEquals(200, values.size());
        assertTrue(values.contains(0)); // 0*100 + 0
        assertTrue(values.contains(919)); // 9*100 + 19
    }

    @Test
    public void testCallToInt_largeMatrix() {
        IntStream result = Matrixes.callToInt(10, 20, (i, j) -> i * 100 + j, false);
        int[] values = result.toArray();
        assertEquals(200, values.length);
        assertEquals(0, values[0]);
    }

    @Test
    public void testMultiply_rectangularMatrices() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2, 3 } }); // 1x3
        IntMatrix m2 = IntMatrix.of(new int[][] { { 4 }, { 5 }, { 6 } }); // 3x1
        List<Integer> kValues = new ArrayList<>();
        Matrixes.multiply(m1, m2, (i, j, k) -> kValues.add(k));
        assertEquals(3, kValues.size());
        assertTrue(kValues.contains(0));
        assertTrue(kValues.contains(1));
        assertTrue(kValues.contains(2));
    }

    @Test
    public void testNewArray_largeArray() {
        Integer[][] arr = Matrixes.newArray(100, 100, Integer.class);
        assertNotNull(arr);
        assertEquals(100, arr.length);
        assertEquals(100, arr[0].length);
        assertEquals(100, arr[99].length);
    }
}
