package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests that verify Javadoc examples in IntMatrix, LongMatrix, FloatMatrix, DoubleMatrix, and Matrix.
 */
public class JavadocExampleMatrixGroup2Test {

    // ==================== IntMatrix ====================

    @Test
    public void testIntMatrix_repeat() {
        IntMatrix matrix = IntMatrix.repeat(2, 3, 1);
        assertEquals(2, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1, matrix.get(0, 0));
        assertEquals(1, matrix.get(1, 2));
    }

    @Test
    public void testIntMatrix_diagonals() {
        IntMatrix matrix = IntMatrix.diagonals(new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 });
        // Resulting matrix:
        //   {1, 0, 4},
        //   {0, 2, 0},
        //   {6, 0, 3}
        assertEquals(1, matrix.get(0, 0));
        assertEquals(0, matrix.get(0, 1));
        assertEquals(4, matrix.get(0, 2));
        assertEquals(0, matrix.get(1, 0));
        assertEquals(2, matrix.get(1, 1));
        assertEquals(0, matrix.get(1, 2));
        assertEquals(6, matrix.get(2, 0));
        assertEquals(0, matrix.get(2, 1));
        assertEquals(3, matrix.get(2, 2));
    }

    @Test
    public void testIntMatrix_get() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(2, matrix.get(0, 1));
    }

    @Test
    public void testIntMatrix_above() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        u.OptionalInt value = matrix.above(1, 0);
        assertEquals(1, value.get());
        u.OptionalInt empty = matrix.above(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testIntMatrix_below() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        u.OptionalInt value = matrix.below(0, 0);
        assertEquals(3, value.get());
        u.OptionalInt empty = matrix.below(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testIntMatrix_left() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        u.OptionalInt value = matrix.left(0, 1);
        assertEquals(1, value.get());
        u.OptionalInt empty = matrix.left(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testIntMatrix_right() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        u.OptionalInt value = matrix.right(0, 0);
        assertEquals(2, value.get());
        u.OptionalInt empty = matrix.right(0, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testIntMatrix_rowView() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] firstRow = matrix.rowView(0);
        assertArrayEquals(new int[] { 1, 2, 3 }, firstRow);
    }

    @Test
    public void testIntMatrix_columnCopy() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] firstColumn = matrix.columnCopy(0);
        assertArrayEquals(new int[] { 1, 4 }, firstColumn);
    }

    @Test
    public void testIntMatrix_updateRow() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        matrix.updateRow(0, x -> x * 2);
        assertArrayEquals(new int[] { 2, 4, 6 }, matrix.rowView(0));
        assertArrayEquals(new int[] { 4, 5, 6 }, matrix.rowView(1));
    }

    @Test
    public void testIntMatrix_updateColumn() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        matrix.updateColumn(0, x -> x + 10);
        assertEquals(11, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(13, matrix.get(1, 0));
        assertEquals(4, matrix.get(1, 1));
        assertEquals(15, matrix.get(2, 0));
        assertEquals(6, matrix.get(2, 1));
    }

    @Test
    public void testIntMatrix_getMainDiagonal() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] diagonal = matrix.getMainDiagonal();
        assertArrayEquals(new int[] { 1, 5, 9 }, diagonal);
    }

    @Test
    public void testIntMatrix_updateMainDiagonal() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        matrix.updateMainDiagonal(x -> x * x);
        // matrix is now {{1, 2}, {3, 16}}
        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(1, 0));
        assertEquals(16, matrix.get(1, 1));
    }

    @Test
    public void testIntMatrix_getAntiDiagonal() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] diagonal = matrix.getAntiDiagonal();
        assertArrayEquals(new int[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testIntMatrix_updateAntiDiagonal() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        matrix.updateAntiDiagonal(x -> -x);
        // matrix is now {{1, -2}, {-3, 4}}
        assertEquals(1, matrix.get(0, 0));
        assertEquals(-2, matrix.get(0, 1));
        assertEquals(-3, matrix.get(1, 0));
        assertEquals(4, matrix.get(1, 1));
    }

    @Test
    public void testIntMatrix_updateAll() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        matrix.updateAll(x -> x * 2);
        assertEquals(2, matrix.get(0, 0));
        assertEquals(4, matrix.get(0, 1));
        assertEquals(6, matrix.get(1, 0));
        assertEquals(8, matrix.get(1, 1));
    }

    @Test
    public void testIntMatrix_updateAllByIndex() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 0, 0, 0 }, { 0, 0, 0 } });
        matrix.updateAll((i, j) -> i + j);
        assertEquals(0, matrix.get(0, 0));
        assertEquals(1, matrix.get(0, 1));
        assertEquals(2, matrix.get(0, 2));
        assertEquals(1, matrix.get(1, 0));
        assertEquals(2, matrix.get(1, 1));
        assertEquals(3, matrix.get(1, 2));

        matrix.updateAll((i, j) -> i * 10 + j);
        assertEquals(0, matrix.get(0, 0));
        assertEquals(1, matrix.get(0, 1));
        assertEquals(2, matrix.get(0, 2));
        assertEquals(10, matrix.get(1, 0));
        assertEquals(11, matrix.get(1, 1));
        assertEquals(12, matrix.get(1, 2));
    }

    @Test
    public void testIntMatrix_replaceIf() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { -1, 2, -3 }, { 4, -5, 6 } });
        matrix.replaceIf(x -> x < 0, 0);
        assertEquals(0, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(0, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(0, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
    }

    @Test
    public void testIntMatrix_replaceIfByIndex() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        matrix.replaceIf((i, j) -> i == j, 0);
        assertEquals(0, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(0, 2));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(0, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
        assertEquals(7, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(0, matrix.get(2, 2));

        matrix.replaceIf((i, j) -> i == 0 || j == 0, -1);
        assertEquals(-1, matrix.get(0, 0));
        assertEquals(-1, matrix.get(0, 1));
        assertEquals(-1, matrix.get(0, 2));
        assertEquals(-1, matrix.get(1, 0));
        assertEquals(0, matrix.get(1, 1));
        assertEquals(6, matrix.get(1, 2));
        assertEquals(-1, matrix.get(2, 0));
        assertEquals(8, matrix.get(2, 1));
        assertEquals(0, matrix.get(2, 2));
    }

    @Test
    public void testIntMatrix_fill() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        matrix.fill(5);
        assertEquals(5, matrix.get(0, 0));
        assertEquals(5, matrix.get(0, 1));
        assertEquals(5, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
    }

    @Test
    public void testIntMatrix_copyFrom() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 0, 0, 0 }, { 0, 0, 0 } });
        matrix.copyFrom(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(0, matrix.get(0, 2));
        assertEquals(3, matrix.get(1, 0));
        assertEquals(4, matrix.get(1, 1));
        assertEquals(0, matrix.get(1, 2));
    }

    @Test
    public void testIntMatrix_copyFromWithOffset() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        matrix.copyFrom(1, 1, new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(0, matrix.get(0, 0));
        assertEquals(0, matrix.get(0, 1));
        assertEquals(0, matrix.get(0, 2));
        assertEquals(0, matrix.get(1, 0));
        assertEquals(1, matrix.get(1, 1));
        assertEquals(2, matrix.get(1, 2));
        assertEquals(0, matrix.get(2, 0));
        assertEquals(3, matrix.get(2, 1));
        assertEquals(4, matrix.get(2, 2));
    }

    @Test
    public void testIntMatrix_resize() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix extended = matrix.resize(3, 3);
        assertEquals(1, extended.get(0, 0));
        assertEquals(2, extended.get(0, 1));
        assertEquals(0, extended.get(0, 2));
        assertEquals(3, extended.get(1, 0));
        assertEquals(4, extended.get(1, 1));
        assertEquals(0, extended.get(1, 2));
        assertEquals(0, extended.get(2, 0));
        assertEquals(0, extended.get(2, 1));
        assertEquals(0, extended.get(2, 2));
    }

    @Test
    public void testIntMatrix_resizeWithFill() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix extended = matrix.resize(3, 4, 9);
        assertEquals(1, extended.get(0, 0));
        assertEquals(2, extended.get(0, 1));
        assertEquals(9, extended.get(0, 2));
        assertEquals(9, extended.get(0, 3));
        assertEquals(3, extended.get(1, 0));
        assertEquals(4, extended.get(1, 1));
        assertEquals(9, extended.get(2, 0));
        assertEquals(9, extended.get(2, 3));

        IntMatrix truncated = matrix.resize(1, 1, 0);
        assertEquals(1, truncated.rowCount());
        assertEquals(1, truncated.columnCount());
        assertEquals(1, truncated.get(0, 0));
    }

    @Test
    public void testIntMatrix_extend() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix extended = matrix.extend(1, 1, 1, 1);
        assertEquals(3, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(0, extended.get(0, 0));
        assertEquals(0, extended.get(0, 3));
        assertEquals(0, extended.get(1, 0));
        assertEquals(1, extended.get(1, 1));
        assertEquals(2, extended.get(1, 2));
        assertEquals(0, extended.get(1, 3));
        assertEquals(0, extended.get(2, 0));
    }

    @Test
    public void testIntMatrix_extendWithFill() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix padded = matrix.extend(1, 1, 2, 2, 9);
        // Result: [[9, 9, 9, 9, 9, 9],
        //          [9, 9, 1, 2, 9, 9],
        //          [9, 9, 9, 9, 9, 9]]
        assertEquals(3, padded.rowCount());
        assertEquals(6, padded.columnCount());
        assertEquals(9, padded.get(0, 0));
        assertEquals(9, padded.get(1, 0));
        assertEquals(9, padded.get(1, 1));
        assertEquals(1, padded.get(1, 2));
        assertEquals(2, padded.get(1, 3));
        assertEquals(9, padded.get(1, 4));
        assertEquals(9, padded.get(2, 5));
    }

    @Test
    public void testIntMatrix_flipInPlaceHorizontally() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        matrix.flipInPlaceHorizontally();
        assertArrayEquals(new int[] { 3, 2, 1 }, matrix.rowView(0));
        assertArrayEquals(new int[] { 6, 5, 4 }, matrix.rowView(1));
    }

    @Test
    public void testIntMatrix_flipInPlaceVertically() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        matrix.flipInPlaceVertically();
        assertArrayEquals(new int[] { 5, 6 }, matrix.rowView(0));
        assertArrayEquals(new int[] { 3, 4 }, matrix.rowView(1));
        assertArrayEquals(new int[] { 1, 2 }, matrix.rowView(2));
    }

    @Test
    public void testIntMatrix_repeatElements() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix repeated = matrix.repeatElements(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertArrayEquals(new int[] { 1, 1, 1, 2, 2, 2 }, repeated.rowView(0));
        assertArrayEquals(new int[] { 1, 1, 1, 2, 2, 2 }, repeated.rowView(1));
    }

    @Test
    public void testIntMatrix_repeatMatrix() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix repeated = matrix.repeatMatrix(2, 3);
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertArrayEquals(new int[] { 1, 2, 1, 2, 1, 2 }, repeated.rowView(0));
        assertArrayEquals(new int[] { 3, 4, 3, 4, 3, 4 }, repeated.rowView(1));
        assertArrayEquals(new int[] { 1, 2, 1, 2, 1, 2 }, repeated.rowView(2));
        assertArrayEquals(new int[] { 3, 4, 3, 4, 3, 4 }, repeated.rowView(3));
    }

    @Test
    public void testIntMatrix_flatten() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntList list = matrix.flatten();
        assertEquals(IntList.of(1, 2, 3, 4), list);
    }

    @Test
    public void testIntMatrix_applyOnFlattened() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 5, 3 }, { 4, 1 } });
        matrix.applyOnFlattened(arr -> java.util.Arrays.sort(arr));
        assertEquals(1, matrix.get(0, 0));
        assertEquals(3, matrix.get(0, 1));
        assertEquals(4, matrix.get(1, 0));
        assertEquals(5, matrix.get(1, 1));
    }

    @Test
    public void testIntMatrix_stackVertically() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 7, 8, 9 }, { 10, 11, 12 } });
        IntMatrix c = a.stackVertically(b);
        assertEquals(4, c.rowCount());
        assertEquals(3, c.columnCount());
        assertArrayEquals(new int[] { 1, 2, 3 }, c.rowView(0));
        assertArrayEquals(new int[] { 4, 5, 6 }, c.rowView(1));
        assertArrayEquals(new int[] { 7, 8, 9 }, c.rowView(2));
        assertArrayEquals(new int[] { 10, 11, 12 }, c.rowView(3));
    }

    @Test
    public void testIntMatrix_stackHorizontally() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 7, 8, 9 }, { 10, 11, 12 } });
        IntMatrix c = a.stackHorizontally(b);
        assertEquals(2, c.rowCount());
        assertEquals(6, c.columnCount());
        assertArrayEquals(new int[] { 1, 2, 3, 7, 8, 9 }, c.rowView(0));
        assertArrayEquals(new int[] { 4, 5, 6, 10, 11, 12 }, c.rowView(1));
    }

    @Test
    public void testIntMatrix_add() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix sum = a.add(b);
        assertEquals(6, sum.get(0, 0));
        assertEquals(8, sum.get(0, 1));
        assertEquals(10, sum.get(1, 0));
        assertEquals(12, sum.get(1, 1));
    }

    @Test
    public void testIntMatrix_subtract() {
        IntMatrix a = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix diff = a.subtract(b);
        assertEquals(4, diff.get(0, 0));
        assertEquals(4, diff.get(0, 1));
        assertEquals(4, diff.get(1, 0));
        assertEquals(4, diff.get(1, 1));
    }

    @Test
    public void testIntMatrix_multiply() {
        IntMatrix a = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix b = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix product = a.multiply(b);
        assertEquals(19, product.get(0, 0));
        assertEquals(22, product.get(0, 1));
        assertEquals(43, product.get(1, 0));
        assertEquals(50, product.get(1, 1));
    }

    @Test
    public void testIntMatrix_streamHorizontalSum() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        int sum = matrix.streamHorizontal().sum();
        assertEquals(10, sum);
        int[] array = matrix.streamHorizontal().toArray();
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, array);
    }

    @Test
    public void testIntMatrix_streamHorizontalRow() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int rowSum = matrix.streamHorizontal(1).sum();
        assertEquals(15, rowSum);
        int[] firstRow = matrix.streamHorizontal(0).toArray();
        assertArrayEquals(new int[] { 1, 2, 3 }, firstRow);
    }

    @Test
    public void testIntMatrix_streamHorizontalRange() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        int[] subset = matrix.streamHorizontal(0, 2).toArray();
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, subset);
    }

    @Test
    public void testIntMatrix_streamVertical() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        int[] colMajor = matrix.streamVertical().toArray();
        assertArrayEquals(new int[] { 1, 3, 2, 4 }, colMajor);
    }

    @Test
    public void testIntMatrix_streamVerticalColumn() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int colSum = matrix.streamVertical(0).sum();
        assertEquals(5, colSum);
        int[] secondCol = matrix.streamVertical(1).toArray();
        assertArrayEquals(new int[] { 2, 5 }, secondCol);
    }

    @Test
    public void testIntMatrix_streamVerticalRange() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] subset = matrix.streamVertical(0, 2).toArray();
        assertArrayEquals(new int[] { 1, 4, 2, 5 }, subset);
    }

    @Test
    public void testIntMatrix_streamRowSums() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] rowSums = matrix.streamRows().mapToInt(row -> row.sum()).toArray();
        assertArrayEquals(new int[] { 6, 15, 24 }, rowSums);
    }

    @Test
    public void testIntMatrix_streamColumnSums() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] colSums = matrix.streamColumns().mapToInt(col -> col.sum()).toArray();
        assertArrayEquals(new int[] { 5, 7, 9 }, colSums);
    }

    // ==================== LongMatrix ====================

    @Test
    public void testLongMatrix_repeat() {
        LongMatrix matrix = LongMatrix.repeat(2, 3, 1L);
        assertEquals(2, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1L, matrix.get(0, 0));
    }

    @Test
    public void testLongMatrix_diagonals() {
        LongMatrix matrix = LongMatrix.diagonals(new long[] { 1, 2, 3 }, new long[] { 4, 5, 6 });
        assertEquals(1L, matrix.get(0, 0));
        assertEquals(0L, matrix.get(0, 1));
        assertEquals(4L, matrix.get(0, 2));
        assertEquals(0L, matrix.get(1, 0));
        assertEquals(2L, matrix.get(1, 1));
        assertEquals(0L, matrix.get(1, 2));
        assertEquals(6L, matrix.get(2, 0));
        assertEquals(0L, matrix.get(2, 1));
        assertEquals(3L, matrix.get(2, 2));
    }

    @Test
    public void testLongMatrix_get() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertEquals(2L, matrix.get(0, 1));
    }

    @Test
    public void testLongMatrix_above() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        u.OptionalLong value = matrix.above(1, 0);
        assertEquals(1L, value.get());
        u.OptionalLong empty = matrix.above(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLongMatrix_below() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        u.OptionalLong value = matrix.below(0, 0);
        assertEquals(3L, value.get());
        u.OptionalLong empty = matrix.below(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLongMatrix_left() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        u.OptionalLong value = matrix.left(0, 1);
        assertEquals(1L, value.get());
        u.OptionalLong empty = matrix.left(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLongMatrix_right() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        u.OptionalLong value = matrix.right(0, 0);
        assertEquals(2L, value.get());
        u.OptionalLong empty = matrix.right(0, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLongMatrix_rowView() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] firstRow = matrix.rowView(0);
        assertArrayEquals(new long[] { 1L, 2L, 3L }, firstRow);
    }

    @Test
    public void testLongMatrix_columnCopy() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long[] firstColumn = matrix.columnCopy(0);
        assertArrayEquals(new long[] { 1L, 4L }, firstColumn);
    }

    @Test
    public void testLongMatrix_updateRow() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        matrix.updateRow(0, x -> x * 2);
        assertArrayEquals(new long[] { 2L, 4L, 6L }, matrix.rowView(0));
        assertArrayEquals(new long[] { 4L, 5L, 6L }, matrix.rowView(1));
    }

    @Test
    public void testLongMatrix_updateColumn() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        matrix.updateColumn(0, x -> x + 10L);
        assertEquals(11L, matrix.get(0, 0));
        assertEquals(2L, matrix.get(0, 1));
        assertEquals(13L, matrix.get(1, 0));
        assertEquals(15L, matrix.get(2, 0));
    }

    @Test
    public void testLongMatrix_getMainDiagonal() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] diagonal = matrix.getMainDiagonal();
        assertArrayEquals(new long[] { 1L, 5L, 9L }, diagonal);
    }

    @Test
    public void testLongMatrix_updateMainDiagonal() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        matrix.updateMainDiagonal(x -> x * x);
        assertEquals(1L, matrix.get(0, 0));
        assertEquals(2L, matrix.get(0, 1));
        assertEquals(3L, matrix.get(1, 0));
        assertEquals(16L, matrix.get(1, 1));
    }

    @Test
    public void testLongMatrix_getAntiDiagonal() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] diagonal = matrix.getAntiDiagonal();
        assertArrayEquals(new long[] { 3L, 5L, 7L }, diagonal);
    }

    @Test
    public void testLongMatrix_updateAntiDiagonal() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        matrix.updateAntiDiagonal(x -> -x);
        assertEquals(1L, matrix.get(0, 0));
        assertEquals(-2L, matrix.get(0, 1));
        assertEquals(-3L, matrix.get(1, 0));
        assertEquals(4L, matrix.get(1, 1));
    }

    @Test
    public void testLongMatrix_updateAll() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        matrix.updateAll(x -> x * 2);
        assertEquals(2L, matrix.get(0, 0));
        assertEquals(4L, matrix.get(0, 1));
        assertEquals(6L, matrix.get(1, 0));
        assertEquals(8L, matrix.get(1, 1));
    }

    @Test
    public void testLongMatrix_replaceIf() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { -1L, 2L, -3L }, { 4L, -5L, 6L } });
        matrix.replaceIf(x -> x < 0, 0L);
        assertEquals(0L, matrix.get(0, 0));
        assertEquals(2L, matrix.get(0, 1));
        assertEquals(0L, matrix.get(0, 2));
        assertEquals(4L, matrix.get(1, 0));
        assertEquals(0L, matrix.get(1, 1));
        assertEquals(6L, matrix.get(1, 2));
    }

    @Test
    public void testLongMatrix_fill() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        matrix.fill(5L);
        assertEquals(5L, matrix.get(0, 0));
        assertEquals(5L, matrix.get(1, 1));
    }

    @Test
    public void testLongMatrix_resize() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix extended = matrix.resize(3, 3);
        assertEquals(1L, extended.get(0, 0));
        assertEquals(2L, extended.get(0, 1));
        assertEquals(0L, extended.get(0, 2));
        assertEquals(0L, extended.get(2, 2));
    }

    @Test
    public void testLongMatrix_resizeWithFill() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix extended = matrix.resize(3, 4, 9L);
        assertEquals(1L, extended.get(0, 0));
        assertEquals(9L, extended.get(0, 2));
        assertEquals(9L, extended.get(2, 0));

        LongMatrix truncated = matrix.resize(1, 1, 0L);
        assertEquals(1, truncated.rowCount());
        assertEquals(1, truncated.columnCount());
        assertEquals(1L, truncated.get(0, 0));
    }

    @Test
    public void testLongMatrix_flipInPlaceHorizontally() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        matrix.flipInPlaceHorizontally();
        assertArrayEquals(new long[] { 3, 2, 1 }, matrix.rowView(0));
        assertArrayEquals(new long[] { 6, 5, 4 }, matrix.rowView(1));
    }

    @Test
    public void testLongMatrix_flipInPlaceVertically() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        matrix.flipInPlaceVertically();
        assertArrayEquals(new long[] { 7, 8, 9 }, matrix.rowView(0));
        assertArrayEquals(new long[] { 4, 5, 6 }, matrix.rowView(1));
        assertArrayEquals(new long[] { 1, 2, 3 }, matrix.rowView(2));
    }

    @Test
    public void testLongMatrix_repeatElements() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix repeated = matrix.repeatElements(2, 3);
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertArrayEquals(new long[] { 1, 1, 1, 2, 2, 2 }, repeated.rowView(0));
        assertArrayEquals(new long[] { 1, 1, 1, 2, 2, 2 }, repeated.rowView(1));
        assertArrayEquals(new long[] { 3, 3, 3, 4, 4, 4 }, repeated.rowView(2));
        assertArrayEquals(new long[] { 3, 3, 3, 4, 4, 4 }, repeated.rowView(3));
    }

    @Test
    public void testLongMatrix_repeatMatrix() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix tiled = matrix.repeatMatrix(2, 3);
        assertEquals(4, tiled.rowCount());
        assertEquals(6, tiled.columnCount());
        assertArrayEquals(new long[] { 1, 2, 1, 2, 1, 2 }, tiled.rowView(0));
        assertArrayEquals(new long[] { 3, 4, 3, 4, 3, 4 }, tiled.rowView(1));
    }

    @Test
    public void testLongMatrix_flatten() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongList list = matrix.flatten();
        assertEquals(LongList.of(1L, 2L, 3L, 4L), list);
    }

    @Test
    public void testLongMatrix_applyOnFlattened() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 5, 3 }, { 4, 1 } });
        matrix.applyOnFlattened(arr -> java.util.Arrays.sort(arr));
        assertEquals(1L, matrix.get(0, 0));
        assertEquals(3L, matrix.get(0, 1));
        assertEquals(4L, matrix.get(1, 0));
        assertEquals(5L, matrix.get(1, 1));
    }

    @Test
    public void testLongMatrix_stackVertically() {
        LongMatrix matrix1 = LongMatrix.of(new long[][] { { 1, 2, 3 } });
        LongMatrix matrix2 = LongMatrix.of(new long[][] { { 4, 5, 6 }, { 7, 8, 9 } });
        LongMatrix stacked = matrix1.stackVertically(matrix2);
        assertEquals(3, stacked.rowCount());
        assertArrayEquals(new long[] { 1, 2, 3 }, stacked.rowView(0));
        assertArrayEquals(new long[] { 4, 5, 6 }, stacked.rowView(1));
        assertArrayEquals(new long[] { 7, 8, 9 }, stacked.rowView(2));
    }

    @Test
    public void testLongMatrix_stackHorizontally() {
        LongMatrix matrix1 = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix matrix2 = LongMatrix.of(new long[][] { { 5 }, { 6 } });
        LongMatrix stacked = matrix1.stackHorizontally(matrix2);
        assertEquals(2, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertArrayEquals(new long[] { 1, 2, 5 }, stacked.rowView(0));
        assertArrayEquals(new long[] { 3, 4, 6 }, stacked.rowView(1));
    }

    @Test
    public void testLongMatrix_add() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5, 6 }, { 7, 8 } });
        LongMatrix sum = m1.add(m2);
        assertEquals(6L, sum.get(0, 0));
        assertEquals(8L, sum.get(0, 1));
        assertEquals(10L, sum.get(1, 0));
        assertEquals(12L, sum.get(1, 1));
    }

    @Test
    public void testLongMatrix_subtract() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 5, 6 }, { 7, 8 } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix diff = m1.subtract(m2);
        assertEquals(4L, diff.get(0, 0));
        assertEquals(4L, diff.get(0, 1));
        assertEquals(4L, diff.get(1, 0));
        assertEquals(4L, diff.get(1, 1));
    }

    @Test
    public void testLongMatrix_multiply() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5, 6 }, { 7, 8 } });
        LongMatrix product = m1.multiply(m2);
        assertEquals(19L, product.get(0, 0));
        assertEquals(22L, product.get(0, 1));
        assertEquals(43L, product.get(1, 0));
        assertEquals(50L, product.get(1, 1));
    }

    @Test
    public void testLongMatrix_streamHorizontalSum() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        long sum = matrix.streamHorizontal().sum();
        assertEquals(21L, sum);
    }

    @Test
    public void testLongMatrix_streamHorizontalRow() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        long rowSum = matrix.streamHorizontal(1).sum();
        assertEquals(15L, rowSum);
    }

    @Test
    public void testLongMatrix_streamHorizontalRange() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        long[] subset = matrix.streamHorizontal(0, 2).toArray();
        assertArrayEquals(new long[] { 1, 2, 3, 4 }, subset);
    }

    @Test
    public void testLongMatrix_zipWith() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5, 6 }, { 7, 8 } });
        LongMatrix max = m1.zipWith(m2, Math::max);
        assertEquals(5L, max.get(0, 0));
        assertEquals(6L, max.get(0, 1));
        assertEquals(7L, max.get(1, 0));
        assertEquals(8L, max.get(1, 1));
    }

    @Test
    public void testLongMatrix_zipWith3() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5, 6 }, { 7, 8 } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 9, 10 }, { 11, 12 } });
        LongMatrix average = m1.zipWith(m2, m3, (a, b, c) -> (a + b + c) / 3);
        assertEquals(5L, average.get(0, 0));
        assertEquals(6L, average.get(0, 1));
        assertEquals(7L, average.get(1, 0));
        assertEquals(8L, average.get(1, 1));
    }

    // ==================== FloatMatrix ====================

    @Test
    public void testFloatMatrix_repeat() {
        FloatMatrix matrix = FloatMatrix.repeat(2, 3, 1.0f);
        assertEquals(2, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1.0f, matrix.get(0, 0));
    }

    @Test
    public void testFloatMatrix_diagonals() {
        FloatMatrix matrix = FloatMatrix.diagonals(new float[] { 1.0f, 2.0f, 3.0f }, new float[] { 4.0f, 5.0f, 6.0f });
        assertEquals(1.0f, matrix.get(0, 0));
        assertEquals(0.0f, matrix.get(0, 1));
        assertEquals(4.0f, matrix.get(0, 2));
        assertEquals(0.0f, matrix.get(1, 0));
        assertEquals(2.0f, matrix.get(1, 1));
        assertEquals(0.0f, matrix.get(1, 2));
        assertEquals(6.0f, matrix.get(2, 0));
        assertEquals(0.0f, matrix.get(2, 1));
        assertEquals(3.0f, matrix.get(2, 2));
    }

    @Test
    public void testFloatMatrix_get() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertEquals(2.0f, matrix.get(0, 1));
    }

    @Test
    public void testFloatMatrix_above() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        u.OptionalFloat value = matrix.above(1, 0);
        assertEquals(1.0f, value.get());
        u.OptionalFloat empty = matrix.above(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testFloatMatrix_below() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        u.OptionalFloat value = matrix.below(0, 0);
        assertEquals(3.0f, value.get());
        u.OptionalFloat empty = matrix.below(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testFloatMatrix_getMainDiagonal() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        float[] diagonal = matrix.getMainDiagonal();
        assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diagonal);
    }

    @Test
    public void testFloatMatrix_getAntiDiagonal() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        float[] diagonal = matrix.getAntiDiagonal();
        assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, diagonal);
    }

    @Test
    public void testFloatMatrix_updateAll() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        matrix.updateAll(x -> x * 2);
        assertEquals(2.0f, matrix.get(0, 0));
        assertEquals(4.0f, matrix.get(0, 1));
        assertEquals(6.0f, matrix.get(1, 0));
        assertEquals(8.0f, matrix.get(1, 1));
    }

    @Test
    public void testFloatMatrix_replaceIf() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { -1.0f, 2.0f, -3.0f }, { 4.0f, -5.0f, 6.0f } });
        matrix.replaceIf(x -> x < 0, 0.0f);
        assertEquals(0.0f, matrix.get(0, 0));
        assertEquals(2.0f, matrix.get(0, 1));
        assertEquals(0.0f, matrix.get(0, 2));
        assertEquals(4.0f, matrix.get(1, 0));
        assertEquals(0.0f, matrix.get(1, 1));
        assertEquals(6.0f, matrix.get(1, 2));
    }

    @Test
    public void testFloatMatrix_flatten() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatList list = matrix.flatten();
        assertEquals(FloatList.of(1.0f, 2.0f, 3.0f, 4.0f), list);
    }

    @Test
    public void testFloatMatrix_applyOnFlattened() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 5.0f, 3.0f }, { 4.0f, 1.0f } });
        matrix.applyOnFlattened(arr -> java.util.Arrays.sort(arr));
        assertEquals(1.0f, matrix.get(0, 0));
        assertEquals(3.0f, matrix.get(0, 1));
        assertEquals(4.0f, matrix.get(1, 0));
        assertEquals(5.0f, matrix.get(1, 1));
    }

    @Test
    public void testFloatMatrix_add() {
        FloatMatrix a = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix b = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix sum = a.add(b);
        assertEquals(6.0f, sum.get(0, 0));
        assertEquals(8.0f, sum.get(0, 1));
        assertEquals(10.0f, sum.get(1, 0));
        assertEquals(12.0f, sum.get(1, 1));
    }

    @Test
    public void testFloatMatrix_subtract() {
        FloatMatrix a = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix b = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix diff = a.subtract(b);
        assertEquals(4.0f, diff.get(0, 0));
        assertEquals(4.0f, diff.get(0, 1));
        assertEquals(4.0f, diff.get(1, 0));
        assertEquals(4.0f, diff.get(1, 1));
    }

    @Test
    public void testFloatMatrix_multiply() {
        FloatMatrix a = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix b = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix product = a.multiply(b);
        assertEquals(19.0f, product.get(0, 0));
        assertEquals(22.0f, product.get(0, 1));
        assertEquals(43.0f, product.get(1, 0));
        assertEquals(50.0f, product.get(1, 1));
    }

    @Test
    public void testFloatMatrix_stackVertically() {
        FloatMatrix a = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix b = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix stacked = a.stackVertically(b);
        assertEquals(4, stacked.rowCount());
        assertEquals(1.0f, stacked.get(0, 0));
        assertEquals(5.0f, stacked.get(2, 0));
        assertEquals(8.0f, stacked.get(3, 1));
    }

    @Test
    public void testFloatMatrix_stackHorizontally() {
        FloatMatrix a = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix b = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix stacked = a.stackHorizontally(b);
        assertEquals(2, stacked.rowCount());
        assertEquals(4, stacked.columnCount());
        assertArrayEquals(new float[] { 1.0f, 2.0f, 5.0f, 6.0f }, stacked.rowView(0));
        assertArrayEquals(new float[] { 3.0f, 4.0f, 7.0f, 8.0f }, stacked.rowView(1));
    }

    @Test
    public void testFloatMatrix_repeatElements() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix repeated = matrix.repeatElements(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertArrayEquals(new float[] { 1.0f, 1.0f, 1.0f, 2.0f, 2.0f, 2.0f }, repeated.rowView(0));
    }

    @Test
    public void testFloatMatrix_repeatMatrix() {
        FloatMatrix matrix = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix repeated = matrix.repeatMatrix(2, 3);
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertArrayEquals(new float[] { 1.0f, 2.0f, 1.0f, 2.0f, 1.0f, 2.0f }, repeated.rowView(0));
    }

    @Test
    public void testFloatMatrix_zipWith() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f, 4.0f } });
        FloatMatrix result = m1.zipWith(m2, (a, b) -> a * b);
        assertEquals(3.0f, result.get(0, 0));
        assertEquals(8.0f, result.get(0, 1));
    }

    @Test
    public void testFloatMatrix_zipWith3() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 2.0f } });
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 3.0f } });
        FloatMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
        assertEquals(6.0f, result.get(0, 0));
    }

    @Test
    public void testFloatMatrix_toIntMatrix() {
        FloatMatrix floatMatrix = FloatMatrix.of(new float[][] { { 1.9f, 2.1f }, { 3.5f, 4.0f } });
        IntMatrix intMatrix = floatMatrix.toIntMatrix();
        assertEquals(1, intMatrix.get(0, 0));
        assertEquals(2, intMatrix.get(0, 1));
        assertEquals(3, intMatrix.get(1, 0));
        assertEquals(4, intMatrix.get(1, 1));
    }

    @Test
    public void testFloatMatrix_toLongMatrix() {
        FloatMatrix floatMatrix = FloatMatrix.of(new float[][] { { 1.9f, 2.1f }, { 3.5f, 4.0f } });
        LongMatrix longMatrix = floatMatrix.toLongMatrix();
        assertEquals(1L, longMatrix.get(0, 0));
        assertEquals(2L, longMatrix.get(0, 1));
        assertEquals(3L, longMatrix.get(1, 0));
        assertEquals(4L, longMatrix.get(1, 1));
    }

    // ==================== DoubleMatrix ====================

    @Test
    public void testDoubleMatrix_repeat() {
        DoubleMatrix matrix = DoubleMatrix.repeat(2, 3, 1.0);
        assertEquals(2, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1.0, matrix.get(0, 0));
    }

    @Test
    public void testDoubleMatrix_diagonals() {
        DoubleMatrix matrix = DoubleMatrix.diagonals(new double[] { 1.0, 2.0, 3.0 }, new double[] { 4.0, 5.0, 6.0 });
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(0.0, matrix.get(0, 1));
        assertEquals(4.0, matrix.get(0, 2));
        assertEquals(0.0, matrix.get(1, 0));
        assertEquals(2.0, matrix.get(1, 1));
        assertEquals(0.0, matrix.get(1, 2));
        assertEquals(6.0, matrix.get(2, 0));
        assertEquals(0.0, matrix.get(2, 1));
        assertEquals(3.0, matrix.get(2, 2));
    }

    @Test
    public void testDoubleMatrix_get() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertEquals(2.0, matrix.get(0, 1));
    }

    @Test
    public void testDoubleMatrix_above() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        u.OptionalDouble value = matrix.above(1, 0);
        assertEquals(1.0, value.get());
        u.OptionalDouble empty = matrix.above(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDoubleMatrix_below() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        u.OptionalDouble value = matrix.below(0, 0);
        assertEquals(3.0, value.get());
        u.OptionalDouble empty = matrix.below(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDoubleMatrix_left() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        u.OptionalDouble value = matrix.left(0, 1);
        assertEquals(1.0, value.get());
        u.OptionalDouble empty = matrix.left(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDoubleMatrix_right() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        u.OptionalDouble value = matrix.right(0, 0);
        assertEquals(2.0, value.get());
        u.OptionalDouble empty = matrix.right(0, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDoubleMatrix_rowView() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        double[] firstRow = matrix.rowView(0);
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, firstRow);
    }

    @Test
    public void testDoubleMatrix_columnCopy() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        double[] firstColumn = matrix.columnCopy(0);
        assertArrayEquals(new double[] { 1.0, 4.0 }, firstColumn);
    }

    @Test
    public void testDoubleMatrix_updateRow() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        matrix.updateRow(0, x -> x * 2);
        assertArrayEquals(new double[] { 2.0, 4.0, 6.0 }, matrix.rowView(0));
        assertArrayEquals(new double[] { 4.0, 5.0, 6.0 }, matrix.rowView(1));
    }

    @Test
    public void testDoubleMatrix_updateColumn() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        matrix.updateColumn(0, x -> x + 10.0);
        assertEquals(11.0, matrix.get(0, 0));
        assertEquals(2.0, matrix.get(0, 1));
        assertEquals(13.0, matrix.get(1, 0));
        assertEquals(15.0, matrix.get(2, 0));
    }

    @Test
    public void testDoubleMatrix_getMainDiagonal() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        double[] diagonal = matrix.getMainDiagonal();
        assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, diagonal);
    }

    @Test
    public void testDoubleMatrix_getAntiDiagonal() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        double[] diagonal = matrix.getAntiDiagonal();
        assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, diagonal);
    }

    @Test
    public void testDoubleMatrix_updateAll() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        matrix.updateAll(x -> x * 2);
        assertEquals(2.0, matrix.get(0, 0));
        assertEquals(4.0, matrix.get(0, 1));
        assertEquals(6.0, matrix.get(1, 0));
        assertEquals(8.0, matrix.get(1, 1));
    }

    @Test
    public void testDoubleMatrix_replaceIf() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { -1.0, 2.0, -3.0 }, { 4.0, -5.0, 6.0 } });
        matrix.replaceIf(x -> x < 0, 0.0);
        assertEquals(0.0, matrix.get(0, 0));
        assertEquals(2.0, matrix.get(0, 1));
        assertEquals(0.0, matrix.get(0, 2));
        assertEquals(4.0, matrix.get(1, 0));
        assertEquals(0.0, matrix.get(1, 1));
        assertEquals(6.0, matrix.get(1, 2));
    }

    @Test
    public void testDoubleMatrix_replaceIfByIndex() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        matrix.replaceIf((i, j) -> i == j, 0.0);
        assertEquals(0.0, matrix.get(0, 0));
        assertEquals(2.0, matrix.get(0, 1));
        assertEquals(0.0, matrix.get(1, 1));
        assertEquals(0.0, matrix.get(2, 2));

        matrix.replaceIf((i, j) -> i == 0 || j == 0, -1.0);
        assertEquals(-1.0, matrix.get(0, 0));
        assertEquals(-1.0, matrix.get(0, 1));
        assertEquals(-1.0, matrix.get(0, 2));
        assertEquals(-1.0, matrix.get(1, 0));
        assertEquals(0.0, matrix.get(1, 1));
        assertEquals(6.0, matrix.get(1, 2));
        assertEquals(-1.0, matrix.get(2, 0));
        assertEquals(8.0, matrix.get(2, 1));
        assertEquals(0.0, matrix.get(2, 2));
    }

    @Test
    public void testDoubleMatrix_flatten() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleList list = matrix.flatten();
        assertEquals(DoubleList.of(1.0, 2.0, 3.0, 4.0), list);
    }

    @Test
    public void testDoubleMatrix_applyOnFlattened() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 5.0, 3.0 }, { 4.0, 1.0 } });
        matrix.applyOnFlattened(arr -> java.util.Arrays.sort(arr));
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(3.0, matrix.get(0, 1));
        assertEquals(4.0, matrix.get(1, 0));
        assertEquals(5.0, matrix.get(1, 1));
    }

    @Test
    public void testDoubleMatrix_stackVertically() {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 5.0, 6.0 } });
        DoubleMatrix stacked = a.stackVertically(b);
        assertEquals(3, stacked.rowCount());
        assertArrayEquals(new double[] { 1.0, 2.0 }, stacked.rowView(0));
        assertArrayEquals(new double[] { 3.0, 4.0 }, stacked.rowView(1));
        assertArrayEquals(new double[] { 5.0, 6.0 }, stacked.rowView(2));
    }

    @Test
    public void testDoubleMatrix_stackHorizontally() {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 5.0 }, { 6.0 } });
        DoubleMatrix stacked = a.stackHorizontally(b);
        assertEquals(2, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertArrayEquals(new double[] { 1.0, 2.0, 5.0 }, stacked.rowView(0));
        assertArrayEquals(new double[] { 3.0, 4.0, 6.0 }, stacked.rowView(1));
    }

    @Test
    public void testDoubleMatrix_resize() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix extended = matrix.resize(3, 3);
        assertEquals(1.0, extended.get(0, 0));
        assertEquals(2.0, extended.get(0, 1));
        assertEquals(0.0, extended.get(0, 2));
        assertEquals(0.0, extended.get(2, 2));
    }

    @Test
    public void testDoubleMatrix_resizeWithFill() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix extended = matrix.resize(3, 4, 9.0);
        assertEquals(1.0, extended.get(0, 0));
        assertEquals(9.0, extended.get(0, 2));
        assertEquals(9.0, extended.get(2, 0));

        DoubleMatrix truncated = matrix.resize(1, 1, 0.0);
        assertEquals(1, truncated.rowCount());
        assertEquals(1.0, truncated.get(0, 0));
    }

    @Test
    public void testDoubleMatrix_flipInPlaceHorizontally() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
        matrix.flipInPlaceHorizontally();
        assertArrayEquals(new double[] { 3.0, 2.0, 1.0 }, matrix.rowView(0));
    }

    @Test
    public void testDoubleMatrix_flipInPlaceVertically() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0 }, { 2.0 }, { 3.0 } });
        matrix.flipInPlaceVertically();
        assertEquals(3.0, matrix.get(0, 0));
        assertEquals(2.0, matrix.get(1, 0));
        assertEquals(1.0, matrix.get(2, 0));
    }

    @Test
    public void testDoubleMatrix_flipHorizontally() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
        DoubleMatrix flipped = matrix.flipHorizontally();
        assertArrayEquals(new double[] { 3.0, 2.0, 1.0 }, flipped.rowView(0));
        // original unchanged
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, matrix.rowView(0));
    }

    @Test
    public void testDoubleMatrix_flipVertically() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0 }, { 2.0 }, { 3.0 } });
        DoubleMatrix flipped = matrix.flipVertically();
        assertEquals(3.0, flipped.get(0, 0));
        assertEquals(2.0, flipped.get(1, 0));
        assertEquals(1.0, flipped.get(2, 0));
    }

    @Test
    public void testDoubleMatrix_repeatElements() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix repeated = matrix.repeatElements(2, 3);
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertArrayEquals(new double[] { 1.0, 1.0, 1.0, 2.0, 2.0, 2.0 }, repeated.rowView(0));
        assertArrayEquals(new double[] { 3.0, 3.0, 3.0, 4.0, 4.0, 4.0 }, repeated.rowView(2));
    }

    @Test
    public void testDoubleMatrix_repeatMatrix() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix tiled = matrix.repeatMatrix(2, 3);
        assertEquals(4, tiled.rowCount());
        assertEquals(6, tiled.columnCount());
        assertArrayEquals(new double[] { 1.0, 2.0, 1.0, 2.0, 1.0, 2.0 }, tiled.rowView(0));
        assertArrayEquals(new double[] { 3.0, 4.0, 3.0, 4.0, 3.0, 4.0 }, tiled.rowView(1));
    }

    @Test
    public void testDoubleMatrix_streamHorizontal() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        double sum = matrix.streamHorizontal().sum();
        assertEquals(10.0, sum);
        double[] array = matrix.streamHorizontal().toArray();
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, array);
    }

    @Test
    public void testDoubleMatrix_streamHorizontalRow() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        double[] row1 = matrix.streamHorizontal(1).toArray();
        assertArrayEquals(new double[] { 3.0, 4.0 }, row1);
        double rowSum = matrix.streamHorizontal(1).sum();
        assertEquals(7.0, rowSum);
    }

    @Test
    public void testDoubleMatrix_streamHorizontalRange() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        double[] subset = matrix.streamHorizontal(0, 2).toArray();
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, subset);
    }

    @Test
    public void testDoubleMatrix_streamVerticalColumn() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        double[] column1 = matrix.streamVertical(1).toArray();
        assertArrayEquals(new double[] { 2.0, 4.0 }, column1);
    }

    @Test
    public void testDoubleMatrix_toIntMatrix() {
        DoubleMatrix doubleMatrix = DoubleMatrix.of(new double[][] { { 1.9, 2.1 }, { 3.5, 4.0 } });
        IntMatrix intMatrix = doubleMatrix.toIntMatrix();
        assertEquals(1, intMatrix.get(0, 0));
        assertEquals(2, intMatrix.get(0, 1));
        assertEquals(3, intMatrix.get(1, 0));
        assertEquals(4, intMatrix.get(1, 1));
    }

    @Test
    public void testDoubleMatrix_toLongMatrix() {
        DoubleMatrix doubleMatrix = DoubleMatrix.of(new double[][] { { 1.9, 2.1 }, { 3.5, 4.0 } });
        LongMatrix longMatrix = doubleMatrix.toLongMatrix();
        assertEquals(1L, longMatrix.get(0, 0));
        assertEquals(2L, longMatrix.get(0, 1));
        assertEquals(3L, longMatrix.get(1, 0));
        assertEquals(4L, longMatrix.get(1, 1));
    }

    @Test
    public void testDoubleMatrix_toFloatMatrix() {
        DoubleMatrix doubleMatrix = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.0, 4.0 } });
        FloatMatrix floatMatrix = doubleMatrix.toFloatMatrix();
        assertEquals(1.5f, floatMatrix.get(0, 0));
        assertEquals(2.5f, floatMatrix.get(0, 1));
        assertEquals(3.0f, floatMatrix.get(1, 0));
        assertEquals(4.0f, floatMatrix.get(1, 1));
    }

    // ==================== Matrix<T> ====================

    @Test
    public void testMatrix_of_get() {
        String[][] data = { { "a", "b" }, { "c", "d" } };
        Matrix<String> matrix = Matrix.of(data);
        assertEquals("c", matrix.get(1, 0));
    }

    @Test
    public void testMatrix_repeat() {
        Matrix<String> matrix = Matrix.repeat(2, 3, "a");
        assertEquals(2, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals("a", matrix.get(0, 0));
        assertEquals("a", matrix.get(1, 2));
    }

    @Test
    public void testMatrix_get_indices() {
        Matrix<String> matrix = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertEquals("C", matrix.get(1, 0));
        assertEquals("D", matrix.get(1, 1));
    }

    @Test
    public void testMatrix_above() {
        Matrix<String> matrix = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        u.Nullable<String> value = matrix.above(1, 0);
        assertEquals("A", value.get());
        u.Nullable<String> empty = matrix.above(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testMatrix_below() {
        Matrix<String> matrix = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        u.Nullable<String> value = matrix.below(0, 0);
        assertEquals("C", value.get());
        u.Nullable<String> empty = matrix.below(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testMatrix_left() {
        Matrix<String> matrix = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        u.Nullable<String> value = matrix.left(0, 1);
        assertEquals("A", value.get());
        u.Nullable<String> empty = matrix.left(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testMatrix_right() {
        Matrix<String> matrix = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        u.Nullable<String> value = matrix.right(0, 0);
        assertEquals("B", value.get());
        u.Nullable<String> empty = matrix.right(0, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testMatrix_columnCopy() {
        Matrix<String> matrix = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String[] colData = matrix.columnCopy(1);
        assertArrayEquals(new String[] { "B", "D" }, colData);
    }

    @Test
    public void testMatrix_getMainDiagonal() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Integer[] diag = m.getMainDiagonal();
        assertArrayEquals(new Integer[] { 1, 5, 9 }, diag);
    }

    @Test
    public void testMatrix_getAntiDiagonal() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Integer[] diag = m.getAntiDiagonal();
        assertArrayEquals(new Integer[] { 3, 5, 7 }, diag);
    }

    @Test
    public void testMatrix_updateAllByIndex() {
        Matrix<Integer> numMatrix = Matrix.of(new Integer[][] { { 0, 0 }, { 0, 0 } });
        numMatrix.updateAll((i, j) -> i * 10 + j);
        assertEquals(0, numMatrix.get(0, 0));
        assertEquals(1, numMatrix.get(0, 1));
        assertEquals(10, numMatrix.get(1, 0));
        assertEquals(11, numMatrix.get(1, 1));
    }

    @Test
    public void testMatrix_flatten() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<Integer> flat = matrix.flatten();
        assertEquals(java.util.Arrays.asList(1, 2, 3, 4, 5, 6), flat);
    }

    @Test
    public void testMatrix_stackVertically() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
        Matrix<Integer> stacked = m1.stackVertically(m2);
        assertEquals(4, stacked.rowCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(5, stacked.get(2, 0));
        assertEquals(8, stacked.get(3, 1));
    }

    @Test
    public void testMatrix_stackHorizontally() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5 }, { 6 } });
        Matrix<Integer> stacked = m1.stackHorizontally(m2);
        assertEquals(2, stacked.rowCount());
        assertEquals(3, stacked.columnCount());
        assertEquals(1, stacked.get(0, 0));
        assertEquals(2, stacked.get(0, 1));
        assertEquals(5, stacked.get(0, 2));
        assertEquals(3, stacked.get(1, 0));
        assertEquals(6, stacked.get(1, 2));
    }

    @Test
    public void testMatrix_zipWith() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
        Matrix<Integer> sum = m1.zipWith(m2, (a, b) -> a + b);
        assertEquals(6, sum.get(0, 0));
        assertEquals(8, sum.get(0, 1));
        assertEquals(10, sum.get(1, 0));
        assertEquals(12, sum.get(1, 1));
    }

    @Test
    public void testMatrix_zipWith3() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 9, 10 }, { 11, 12 } });
        Matrix<Integer> result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
        assertEquals(15, result.get(0, 0));
        assertEquals(18, result.get(0, 1));
        assertEquals(21, result.get(1, 0));
        assertEquals(24, result.get(1, 1));
    }

    @Test
    public void testMatrix_flipHorizontally() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Matrix<Integer> flipped = matrix.flipHorizontally();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(2, flipped.get(0, 1));
        assertEquals(1, flipped.get(0, 2));
        assertEquals(6, flipped.get(1, 0));
        assertEquals(5, flipped.get(1, 1));
        assertEquals(4, flipped.get(1, 2));
    }

    @Test
    public void testMatrix_flipVertically() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        Matrix<Integer> flipped = matrix.flipVertically();
        assertEquals(5, flipped.get(0, 0));
        assertEquals(6, flipped.get(0, 1));
        assertEquals(3, flipped.get(1, 0));
        assertEquals(4, flipped.get(1, 1));
        assertEquals(1, flipped.get(2, 0));
        assertEquals(2, flipped.get(2, 1));
    }

    @Test
    public void testMatrix_repeatElements() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> repeated = matrix.repeatElements(2, 3);
        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2));
        assertEquals(2, repeated.get(0, 3));
        assertEquals(2, repeated.get(0, 4));
        assertEquals(2, repeated.get(0, 5));
        assertEquals(3, repeated.get(2, 0));
        assertEquals(4, repeated.get(2, 3));
    }

    @Test
    public void testMatrix_repeatMatrix() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> tiled = matrix.repeatMatrix(2, 3);
        assertEquals(4, tiled.rowCount());
        assertEquals(6, tiled.columnCount());
        assertEquals(1, tiled.get(0, 0));
        assertEquals(2, tiled.get(0, 1));
        assertEquals(1, tiled.get(0, 2));
        assertEquals(2, tiled.get(0, 3));
        assertEquals(3, tiled.get(1, 0));
        assertEquals(4, tiled.get(1, 1));
    }

    @Test
    public void testMatrix_streamMainDiagonal() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Object[] diag = matrix.streamMainDiagonal().toArray();
        assertArrayEquals(new Object[] { 1, 5, 9 }, diag);
    }

    @Test
    public void testMatrix_streamAntiDiagonal() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Object[] diag = matrix.streamAntiDiagonal().toArray();
        assertArrayEquals(new Object[] { 3, 5, 7 }, diag);
    }

    @Test
    public void testMatrix_streamHorizontal() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Object[] array = matrix.streamHorizontal().toArray();
        assertArrayEquals(new Object[] { 1, 2, 3, 4 }, array);
    }

    @Test
    public void testMatrix_streamHorizontalRow() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Object[] firstRow = matrix.streamHorizontal(0).toArray();
        assertArrayEquals(new Object[] { 1, 2, 3 }, firstRow);
    }

    @Test
    public void testMatrix_streamHorizontalRange() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        Object[] subArray = matrix.streamHorizontal(0, 2).toArray();
        assertArrayEquals(new Object[] { 1, 2, 3, 4 }, subArray);
    }

    @Test
    public void testMatrix_streamVertical() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Object[] colMajor = matrix.streamVertical().toArray();
        assertArrayEquals(new Object[] { 1, 3, 2, 4 }, colMajor);
    }

    @Test
    public void testMatrix_streamVerticalColumn() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Object[] secondCol = matrix.streamVertical(1).toArray();
        assertArrayEquals(new Object[] { 2, 5, 8 }, secondCol);
    }

    @Test
    public void testMatrix_streamVerticalRange() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Object[] colMajor = matrix.streamVertical(0, 2).toArray();
        assertArrayEquals(new Object[] { 1, 4, 2, 5 }, colMajor);
    }
}
