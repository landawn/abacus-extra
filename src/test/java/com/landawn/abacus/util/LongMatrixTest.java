package com.landawn.abacus.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalLong;
import com.landawn.abacus.util.stream.LongStream;

public class LongMatrixTest extends TestBase {

    @Test
    public void testConstructor() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = new LongMatrix(a);
        Assertions.assertEquals(2, matrix.rows);
        Assertions.assertEquals(2, matrix.cols);

        LongMatrix nullMatrix = new LongMatrix(null);
        Assertions.assertEquals(0, nullMatrix.rows);
        Assertions.assertEquals(0, nullMatrix.cols);
    }

    @Test
    public void testEmpty() {
        LongMatrix empty = LongMatrix.empty();
        Assertions.assertEquals(0, empty.rows);
        Assertions.assertEquals(0, empty.cols);
        Assertions.assertTrue(empty.isEmpty());
    }

    @Test
    public void testOf() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);
        Assertions.assertEquals(2, matrix.rows);
        Assertions.assertEquals(2, matrix.cols);

        LongMatrix emptyMatrix = LongMatrix.of();
        Assertions.assertTrue(emptyMatrix.isEmpty());

        LongMatrix nullMatrix = LongMatrix.of((long[][]) null);
        Assertions.assertTrue(nullMatrix.isEmpty());
    }

    @Test
    public void testCreate() {
        int[][] a = { { 1, 2 }, { 3, 4 } };
        LongMatrix matrix = LongMatrix.create(a);
        Assertions.assertEquals(2, matrix.rows);
        Assertions.assertEquals(2, matrix.cols);
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(4L, matrix.get(1, 1));

        LongMatrix emptyMatrix = LongMatrix.create((int[][]) null);
        Assertions.assertTrue(emptyMatrix.isEmpty());
    }

    @Test
    public void testRandom() {
        LongMatrix matrix = LongMatrix.random(5);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(5, matrix.cols);
    }

    @Test
    public void testRepeat() {
        LongMatrix matrix = LongMatrix.repeat(5L, 10);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(10, matrix.cols);
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(5L, matrix.get(0, i));
        }
    }

    @Test
    public void testRange() {
        LongMatrix matrix = LongMatrix.range(0L, 5L);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(5, matrix.cols);
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(i, matrix.get(0, i));
        }
    }

    @Test
    public void testRangeWithStep() {
        LongMatrix matrix = LongMatrix.range(0L, 10L, 2L);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(5, matrix.cols);
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(i * 2L, matrix.get(0, i));
        }
    }

    @Test
    public void testRangeClosed() {
        LongMatrix matrix = LongMatrix.rangeClosed(0L, 4L);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(5, matrix.cols);
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(i, matrix.get(0, i));
        }
    }

    @Test
    public void testRangeClosedWithStep() {
        LongMatrix matrix = LongMatrix.rangeClosed(0L, 9L, 2L);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(5, matrix.cols);
        Assertions.assertEquals(0L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(0, 1));
        Assertions.assertEquals(4L, matrix.get(0, 2));
        Assertions.assertEquals(6L, matrix.get(0, 3));
        Assertions.assertEquals(8L, matrix.get(0, 4));
    }

    @Test
    public void testDiagonalLU2RD() {
        long[] diagonal = { 1L, 2L, 3L };
        LongMatrix matrix = LongMatrix.diagonalLU2RD(diagonal);
        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(1, 1));
        Assertions.assertEquals(3L, matrix.get(2, 2));
        Assertions.assertEquals(0L, matrix.get(0, 1));
    }

    @Test
    public void testDiagonalRU2LD() {
        long[] diagonal = { 1L, 2L, 3L };
        LongMatrix matrix = LongMatrix.diagonalRU2LD(diagonal);
        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals(1L, matrix.get(0, 2));
        Assertions.assertEquals(2L, matrix.get(1, 1));
        Assertions.assertEquals(3L, matrix.get(2, 0));
        Assertions.assertEquals(0L, matrix.get(0, 0));
    }

    @Test
    public void testDiagonal() {
        long[] main = { 1L, 2L, 3L };
        long[] anti = { 4L, 5L, 6L };
        LongMatrix matrix = LongMatrix.diagonal(main, anti);
        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(1, 1));
        Assertions.assertEquals(3L, matrix.get(2, 2));
        Assertions.assertEquals(4L, matrix.get(0, 2));
        Assertions.assertEquals(2L, matrix.get(1, 1));
        Assertions.assertEquals(6L, matrix.get(2, 0));

        LongMatrix emptyMatrix = LongMatrix.diagonal(null, null);
        Assertions.assertTrue(emptyMatrix.isEmpty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> LongMatrix.diagonal(new long[] { 1L }, new long[] { 4L, 5L }));
    }

    @Test
    public void testUnbox() {
        Long[][] boxed = { { 1L, 2L }, { 3L, 4L } };
        Matrix<Long> boxedMatrix = Matrix.of(boxed);
        LongMatrix unboxed = LongMatrix.unbox(boxedMatrix);
        Assertions.assertEquals(2, unboxed.rows);
        Assertions.assertEquals(2, unboxed.cols);
        Assertions.assertEquals(1L, unboxed.get(0, 0));
        Assertions.assertEquals(4L, unboxed.get(1, 1));
    }

    @Test
    public void testComponentType() {
        LongMatrix matrix = LongMatrix.empty();
        Assertions.assertEquals(long.class, matrix.componentType());
    }

    @Test
    public void testGet() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(0, 1));
        Assertions.assertEquals(3L, matrix.get(1, 0));
        Assertions.assertEquals(4L, matrix.get(1, 1));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(2, 0));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(0, 2));
    }

    @Test
    public void testGetWithPoint() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);
        Assertions.assertEquals(1L, matrix.get(Point.of(0, 0)));
        Assertions.assertEquals(4L, matrix.get(Point.of(1, 1)));
    }

    @Test
    public void testSet() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);
        matrix.set(0, 0, 10L);
        Assertions.assertEquals(10L, matrix.get(0, 0));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.set(2, 0, 5L));
    }

    @Test
    public void testSetWithPoint() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);
        matrix.set(Point.of(1, 1), 10L);
        Assertions.assertEquals(10L, matrix.get(1, 1));
    }

    @Test
    public void testUpOf() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        OptionalLong up = matrix.upOf(1, 0);
        Assertions.assertTrue(up.isPresent());
        Assertions.assertEquals(1L, up.getAsLong());

        OptionalLong empty = matrix.upOf(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        OptionalLong down = matrix.downOf(0, 0);
        Assertions.assertTrue(down.isPresent());
        Assertions.assertEquals(3L, down.getAsLong());

        OptionalLong empty = matrix.downOf(1, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        OptionalLong left = matrix.leftOf(0, 1);
        Assertions.assertTrue(left.isPresent());
        Assertions.assertEquals(1L, left.getAsLong());

        OptionalLong empty = matrix.leftOf(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        OptionalLong right = matrix.rightOf(0, 0);
        Assertions.assertTrue(right.isPresent());
        Assertions.assertEquals(2L, right.getAsLong());

        OptionalLong empty = matrix.rightOf(0, 1);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testRow() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] row0 = matrix.row(0);
        Assertions.assertArrayEquals(new long[] { 1L, 2L, 3L }, row0);

        long[] row1 = matrix.row(1);
        Assertions.assertArrayEquals(new long[] { 4L, 5L, 6L }, row1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.row(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.row(2));
    }

    @Test
    public void testColumn() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] col0 = matrix.column(0);
        Assertions.assertArrayEquals(new long[] { 1L, 4L }, col0);

        long[] col1 = matrix.column(1);
        Assertions.assertArrayEquals(new long[] { 2L, 5L }, col1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.column(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.column(3));
    }

    @Test
    public void testSetRow() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.setRow(0, new long[] { 10L, 20L, 30L });
        Assertions.assertArrayEquals(new long[] { 10L, 20L, 30L }, matrix.row(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setRow(0, new long[] { 1L, 2L }));
    }

    @Test
    public void testSetColumn() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.setColumn(0, new long[] { 10L, 20L });
        Assertions.assertArrayEquals(new long[] { 10L, 20L }, matrix.column(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setColumn(0, new long[] { 1L }));
    }

    @Test
    public void testUpdateRow() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.updateRow(0, x -> x * 2);
        Assertions.assertArrayEquals(new long[] { 2L, 4L, 6L }, matrix.row(0));
    }

    @Test
    public void testUpdateColumn() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.updateColumn(1, x -> x + 10);
        Assertions.assertArrayEquals(new long[] { 12L, 15L }, matrix.column(1));
    }

    @Test
    public void testGetLU2RD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] diagonal = matrix.getLU2RD();
        Assertions.assertArrayEquals(new long[] { 1L, 5L, 9L }, diagonal);

        LongMatrix nonSquare = LongMatrix.of(new long[][] { { 1L, 2L } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.setLU2RD(new long[] { 10L, 20L, 30L });
        Assertions.assertEquals(10L, matrix.get(0, 0));
        Assertions.assertEquals(20L, matrix.get(1, 1));
        Assertions.assertEquals(30L, matrix.get(2, 2));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setLU2RD(new long[] { 1L, 2L }));
    }

    @Test
    public void testUpdateLU2RD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.updateLU2RD(x -> x * x);
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(25L, matrix.get(1, 1));
        Assertions.assertEquals(81L, matrix.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] diagonal = matrix.getRU2LD();
        Assertions.assertArrayEquals(new long[] { 3L, 5L, 7L }, diagonal);
    }

    @Test
    public void testSetRU2LD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.setRU2LD(new long[] { 10L, 20L, 30L });
        Assertions.assertEquals(10L, matrix.get(0, 2));
        Assertions.assertEquals(20L, matrix.get(1, 1));
        Assertions.assertEquals(30L, matrix.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.updateRU2LD(x -> -x);
        Assertions.assertEquals(-3L, matrix.get(0, 2));
        Assertions.assertEquals(-5L, matrix.get(1, 1));
        Assertions.assertEquals(-7L, matrix.get(2, 0));
    }

    @Test
    public void testUpdateAll() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.updateAll(x -> x * 2);
        Assertions.assertEquals(2L, matrix.get(0, 0));
        Assertions.assertEquals(4L, matrix.get(0, 1));
        Assertions.assertEquals(6L, matrix.get(1, 0));
        Assertions.assertEquals(8L, matrix.get(1, 1));
    }

    @Test
    public void testUpdateAllWithIndices() {
        long[][] a = { { 0L, 0L }, { 0L, 0L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.updateAll((i, j) -> (long) (i + j));
        Assertions.assertEquals(0L, matrix.get(0, 0));
        Assertions.assertEquals(1L, matrix.get(0, 1));
        Assertions.assertEquals(1L, matrix.get(1, 0));
        Assertions.assertEquals(2L, matrix.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.replaceIf(x -> x < 3, 0L);
        Assertions.assertEquals(0L, matrix.get(0, 0));
        Assertions.assertEquals(0L, matrix.get(0, 1));
        Assertions.assertEquals(3L, matrix.get(1, 0));
        Assertions.assertEquals(4L, matrix.get(1, 1));
    }

    @Test
    public void testReplaceIfWithIndices() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.replaceIf((i, j) -> i == j, 1L);
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(0, 1));
        Assertions.assertEquals(3L, matrix.get(1, 0));
        Assertions.assertEquals(1L, matrix.get(1, 1));
    }

    @Test
    public void testMap() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix squared = matrix.map(x -> x * x);
        Assertions.assertEquals(1L, squared.get(0, 0));
        Assertions.assertEquals(4L, squared.get(0, 1));
        Assertions.assertEquals(9L, squared.get(1, 0));
        Assertions.assertEquals(16L, squared.get(1, 1));
    }

    @Test
    public void testMapToInt() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        IntMatrix intMatrix = matrix.mapToInt(x -> (int) (x % 100));
        Assertions.assertEquals(1, intMatrix.get(0, 0));
        Assertions.assertEquals(4, intMatrix.get(1, 1));
    }

    @Test
    public void testMapToDouble() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        DoubleMatrix doubleMatrix = matrix.mapToDouble(x -> Math.sqrt(x));
        Assertions.assertEquals(1.0, doubleMatrix.get(0, 0));
        Assertions.assertEquals(2.0, doubleMatrix.get(1, 1));
    }

    @Test
    public void testMapToObj() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        Matrix<String> stringMatrix = matrix.mapToObj(Long::toString, String.class);
        Assertions.assertEquals("1", stringMatrix.get(0, 0));
        Assertions.assertEquals("4", stringMatrix.get(1, 1));
    }

    @Test
    public void testFill() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.fill(0L);
        Assertions.assertEquals(0L, matrix.get(0, 0));
        Assertions.assertEquals(0L, matrix.get(0, 1));
        Assertions.assertEquals(0L, matrix.get(1, 0));
        Assertions.assertEquals(0L, matrix.get(1, 1));
    }

    @Test
    public void testFillWithArray() {
        long[][] a = { { 0L, 0L, 0L }, { 0L, 0L, 0L }, { 0L, 0L, 0L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[][] b = { { 1L, 2L }, { 3L, 4L } };
        matrix.fill(b);
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(0, 1));
        Assertions.assertEquals(3L, matrix.get(1, 0));
        Assertions.assertEquals(4L, matrix.get(1, 1));
        Assertions.assertEquals(0L, matrix.get(2, 2));   // unchanged
    }

    @Test
    public void testFillWithIndices() {
        long[][] a = { { 0L, 0L, 0L }, { 0L, 0L, 0L }, { 0L, 0L, 0L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[][] b = { { 1L, 2L } };
        matrix.fill(1, 1, b);
        Assertions.assertEquals(0L, matrix.get(0, 0));   // unchanged
        Assertions.assertEquals(1L, matrix.get(1, 1));
        Assertions.assertEquals(2L, matrix.get(1, 2));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.fill(-1, 0, b));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.fill(0, -1, b));
    }

    @Test
    public void testCopy() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);
        LongMatrix copy = matrix.copy();

        Assertions.assertEquals(matrix.rows, copy.rows);
        Assertions.assertEquals(matrix.cols, copy.cols);
        Assertions.assertEquals(1L, copy.get(0, 0));
        Assertions.assertEquals(4L, copy.get(1, 1));

        copy.set(0, 0, 100L);
        Assertions.assertEquals(1L, matrix.get(0, 0));   // original unchanged
    }

    @Test
    public void testCopyWithRowRange() {
        long[][] a = { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);
        LongMatrix copy = matrix.copy(1, 3);

        Assertions.assertEquals(2, copy.rows);
        Assertions.assertEquals(2, copy.cols);
        Assertions.assertEquals(3L, copy.get(0, 0));
        Assertions.assertEquals(6L, copy.get(1, 1));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(-1, 2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 4));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(2, 1));
    }

    @Test
    public void testCopyWithFullRange() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);
        LongMatrix copy = matrix.copy(1, 3, 0, 2);

        Assertions.assertEquals(2, copy.rows);
        Assertions.assertEquals(2, copy.cols);
        Assertions.assertEquals(4L, copy.get(0, 0));
        Assertions.assertEquals(5L, copy.get(0, 1));
        Assertions.assertEquals(7L, copy.get(1, 0));
        Assertions.assertEquals(8L, copy.get(1, 1));
    }

    @Test
    public void testExtend() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix extended = matrix.extend(3, 3);
        Assertions.assertEquals(3, extended.rows);
        Assertions.assertEquals(3, extended.cols);
        Assertions.assertEquals(1L, extended.get(0, 0));
        Assertions.assertEquals(4L, extended.get(1, 1));
        Assertions.assertEquals(0L, extended.get(2, 2));

        LongMatrix truncated = matrix.extend(1, 1);
        Assertions.assertEquals(1, truncated.rows);
        Assertions.assertEquals(1, truncated.cols);
        Assertions.assertEquals(1L, truncated.get(0, 0));
    }

    @Test
    public void testExtendWithDefaultValue() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix extended = matrix.extend(3, 3, -1L);
        Assertions.assertEquals(-1L, extended.get(2, 2));
        Assertions.assertEquals(-1L, extended.get(0, 2));
        Assertions.assertEquals(-1L, extended.get(2, 0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 2, 0L));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.extend(2, -1, 0L));
    }

    @Test
    public void testExtendInAllDirections() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix extended = matrix.extend(1, 1, 1, 1);
        Assertions.assertEquals(4, extended.rows);
        Assertions.assertEquals(4, extended.cols);
        Assertions.assertEquals(1L, extended.get(1, 1));
        Assertions.assertEquals(4L, extended.get(2, 2));
        Assertions.assertEquals(0L, extended.get(0, 0));
    }

    @Test
    public void testExtendInAllDirectionsWithDefaultValue() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix extended = matrix.extend(1, 1, 1, 1, 99L);
        Assertions.assertEquals(99L, extended.get(0, 0));
        Assertions.assertEquals(99L, extended.get(0, 3));
        Assertions.assertEquals(99L, extended.get(3, 0));
        Assertions.assertEquals(99L, extended.get(3, 3));
        Assertions.assertEquals(1L, extended.get(1, 1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 0, 0, 0, 0L));
    }

    @Test
    public void testReverseH() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.reverseH();
        Assertions.assertEquals(3L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(0, 1));
        Assertions.assertEquals(1L, matrix.get(0, 2));
        Assertions.assertEquals(6L, matrix.get(1, 0));
    }

    @Test
    public void testReverseV() {
        long[][] a = { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.reverseV();
        Assertions.assertEquals(5L, matrix.get(0, 0));
        Assertions.assertEquals(6L, matrix.get(0, 1));
        Assertions.assertEquals(3L, matrix.get(1, 0));
        Assertions.assertEquals(1L, matrix.get(2, 0));
    }

    @Test
    public void testFlipH() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix flipped = matrix.flipH();
        Assertions.assertEquals(3L, flipped.get(0, 0));
        Assertions.assertEquals(1L, flipped.get(0, 2));
        Assertions.assertEquals(1L, matrix.get(0, 0));   // original unchanged
    }

    @Test
    public void testFlipV() {
        long[][] a = { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix flipped = matrix.flipV();
        Assertions.assertEquals(5L, flipped.get(0, 0));
        Assertions.assertEquals(1L, flipped.get(2, 0));
        Assertions.assertEquals(1L, matrix.get(0, 0));   // original unchanged
    }

    @Test
    public void testRotate90() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix rotated = matrix.rotate90();
        Assertions.assertEquals(2, rotated.rows);
        Assertions.assertEquals(2, rotated.cols);
        Assertions.assertEquals(3L, rotated.get(0, 0));
        Assertions.assertEquals(1L, rotated.get(0, 1));
        Assertions.assertEquals(4L, rotated.get(1, 0));
        Assertions.assertEquals(2L, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix rotated = matrix.rotate180();
        Assertions.assertEquals(2, rotated.rows);
        Assertions.assertEquals(2, rotated.cols);
        Assertions.assertEquals(4L, rotated.get(0, 0));
        Assertions.assertEquals(3L, rotated.get(0, 1));
        Assertions.assertEquals(2L, rotated.get(1, 0));
        Assertions.assertEquals(1L, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix rotated = matrix.rotate270();
        Assertions.assertEquals(2, rotated.rows);
        Assertions.assertEquals(2, rotated.cols);
        Assertions.assertEquals(2L, rotated.get(0, 0));
        Assertions.assertEquals(4L, rotated.get(0, 1));
        Assertions.assertEquals(1L, rotated.get(1, 0));
        Assertions.assertEquals(3L, rotated.get(1, 1));
    }

    @Test
    public void testTranspose() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix transposed = matrix.transpose();
        Assertions.assertEquals(3, transposed.rows);
        Assertions.assertEquals(2, transposed.cols);
        Assertions.assertEquals(1L, transposed.get(0, 0));
        Assertions.assertEquals(4L, transposed.get(0, 1));
        Assertions.assertEquals(2L, transposed.get(1, 0));
        Assertions.assertEquals(5L, transposed.get(1, 1));
    }

    @Test
    public void testReshape() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix reshaped = matrix.reshape(3, 2);
        Assertions.assertEquals(3, reshaped.rows);
        Assertions.assertEquals(2, reshaped.cols);
        Assertions.assertEquals(1L, reshaped.get(0, 0));
        Assertions.assertEquals(2L, reshaped.get(0, 1));
        Assertions.assertEquals(3L, reshaped.get(1, 0));
        Assertions.assertEquals(4L, reshaped.get(1, 1));

        LongMatrix empty = LongMatrix.empty().reshape(2, 3);
        Assertions.assertEquals(2, empty.rows);
        Assertions.assertEquals(3, empty.cols);
    }

    @Test
    public void testRepelem() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix repeated = matrix.repelem(2, 3);
        Assertions.assertEquals(4, repeated.rows);
        Assertions.assertEquals(6, repeated.cols);
        Assertions.assertEquals(1L, repeated.get(0, 0));
        Assertions.assertEquals(1L, repeated.get(0, 2));
        Assertions.assertEquals(1L, repeated.get(1, 0));
        Assertions.assertEquals(2L, repeated.get(0, 3));
        Assertions.assertEquals(4L, repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repelem(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix repeated = matrix.repmat(2, 3);
        Assertions.assertEquals(4, repeated.rows);
        Assertions.assertEquals(6, repeated.cols);
        Assertions.assertEquals(1L, repeated.get(0, 0));
        Assertions.assertEquals(1L, repeated.get(0, 2));
        Assertions.assertEquals(1L, repeated.get(0, 4));
        Assertions.assertEquals(1L, repeated.get(2, 0));
        Assertions.assertEquals(4L, repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repmat(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repmat(1, 0));
    }

    @Test
    public void testFlatten() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongList flat = matrix.flatten();
        Assertions.assertEquals(6, flat.size());
        Assertions.assertEquals(1L, flat.get(0));
        Assertions.assertEquals(2L, flat.get(1));
        Assertions.assertEquals(3L, flat.get(2));
        Assertions.assertEquals(4L, flat.get(3));
        Assertions.assertEquals(5L, flat.get(4));
        Assertions.assertEquals(6L, flat.get(5));
    }

    @Test
    public void testFlatOp() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        List<Long> collected = new ArrayList<>();
        matrix.flatOp(row -> {
            for (long val : row) {
                collected.add(val);
            }
        });

        Assertions.assertEquals(4, collected.size());
        Assertions.assertTrue(collected.contains(1L));
        Assertions.assertTrue(collected.contains(4L));
    }

    @Test
    public void testVstack() {
        long[][] a = { { 1L, 2L, 3L } };
        long[][] b = { { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrixA = LongMatrix.of(a);
        LongMatrix matrixB = LongMatrix.of(b);

        LongMatrix stacked = matrixA.vstack(matrixB);
        Assertions.assertEquals(3, stacked.rows);
        Assertions.assertEquals(3, stacked.cols);
        Assertions.assertEquals(1L, stacked.get(0, 0));
        Assertions.assertEquals(4L, stacked.get(1, 0));
        Assertions.assertEquals(9L, stacked.get(2, 2));

        LongMatrix differentCols = LongMatrix.of(new long[][] { { 1L, 2L } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.vstack(differentCols));
    }

    @Test
    public void testHstack() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        long[][] b = { { 5L }, { 6L } };
        LongMatrix matrixA = LongMatrix.of(a);
        LongMatrix matrixB = LongMatrix.of(b);

        LongMatrix stacked = matrixA.hstack(matrixB);
        Assertions.assertEquals(2, stacked.rows);
        Assertions.assertEquals(3, stacked.cols);
        Assertions.assertEquals(1L, stacked.get(0, 0));
        Assertions.assertEquals(5L, stacked.get(0, 2));
        Assertions.assertEquals(6L, stacked.get(1, 2));

        LongMatrix differentRows = LongMatrix.of(new long[][] { { 1L } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.hstack(differentRows));
    }

    @Test
    public void testAdd() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        long[][] b = { { 5L, 6L }, { 7L, 8L } };
        LongMatrix matrixA = LongMatrix.of(a);
        LongMatrix matrixB = LongMatrix.of(b);

        LongMatrix sum = matrixA.add(matrixB);
        Assertions.assertEquals(6L, sum.get(0, 0));
        Assertions.assertEquals(8L, sum.get(0, 1));
        Assertions.assertEquals(10L, sum.get(1, 0));
        Assertions.assertEquals(12L, sum.get(1, 1));

        LongMatrix differentShape = LongMatrix.of(new long[][] { { 1L } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.add(differentShape));
    }

    @Test
    public void testSubtract() {
        long[][] a = { { 5L, 6L }, { 7L, 8L } };
        long[][] b = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrixA = LongMatrix.of(a);
        LongMatrix matrixB = LongMatrix.of(b);

        LongMatrix diff = matrixA.subtract(matrixB);
        Assertions.assertEquals(4L, diff.get(0, 0));
        Assertions.assertEquals(4L, diff.get(0, 1));
        Assertions.assertEquals(4L, diff.get(1, 0));
        Assertions.assertEquals(4L, diff.get(1, 1));

        LongMatrix differentShape = LongMatrix.of(new long[][] { { 1L } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.subtract(differentShape));
    }

    @Test
    public void testMultiply() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        long[][] b = { { 5L, 6L }, { 7L, 8L } };
        LongMatrix matrixA = LongMatrix.of(a);
        LongMatrix matrixB = LongMatrix.of(b);

        LongMatrix product = matrixA.multiply(matrixB);
        Assertions.assertEquals(2, product.rows);
        Assertions.assertEquals(2, product.cols);
        Assertions.assertEquals(19L, product.get(0, 0));
        Assertions.assertEquals(22L, product.get(0, 1));
        Assertions.assertEquals(43L, product.get(1, 0));
        Assertions.assertEquals(50L, product.get(1, 1));

        LongMatrix incompatible = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.multiply(incompatible));
    }

    @Test
    public void testBoxed() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        Matrix<Long> boxed = matrix.boxed();
        Assertions.assertEquals(2, boxed.rows);
        Assertions.assertEquals(2, boxed.cols);
        Assertions.assertEquals(Long.valueOf(1L), boxed.get(0, 0));
        Assertions.assertEquals(Long.valueOf(4L), boxed.get(1, 1));
    }

    @Test
    public void testToFloatMatrix() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        FloatMatrix floatMatrix = matrix.toFloatMatrix();
        Assertions.assertEquals(2, floatMatrix.rows);
        Assertions.assertEquals(2, floatMatrix.cols);
        Assertions.assertEquals(1.0f, floatMatrix.get(0, 0));
        Assertions.assertEquals(4.0f, floatMatrix.get(1, 1));
    }

    @Test
    public void testToDoubleMatrix() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        DoubleMatrix doubleMatrix = matrix.toDoubleMatrix();
        Assertions.assertEquals(2, doubleMatrix.rows);
        Assertions.assertEquals(2, doubleMatrix.cols);
        Assertions.assertEquals(1.0, doubleMatrix.get(0, 0));
        Assertions.assertEquals(4.0, doubleMatrix.get(1, 1));
    }

    @Test
    public void testZipWith() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        long[][] b = { { 5L, 6L }, { 7L, 8L } };
        LongMatrix matrixA = LongMatrix.of(a);
        LongMatrix matrixB = LongMatrix.of(b);

        LongMatrix result = matrixA.zipWith(matrixB, Math::max);
        Assertions.assertEquals(5L, result.get(0, 0));
        Assertions.assertEquals(6L, result.get(0, 1));
        Assertions.assertEquals(7L, result.get(1, 0));
        Assertions.assertEquals(8L, result.get(1, 1));

        LongMatrix differentShape = LongMatrix.of(new long[][] { { 1L } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.zipWith(differentShape, (x, y) -> x));
    }

    @Test
    public void testZipWith3() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        long[][] b = { { 5L, 6L }, { 7L, 8L } };
        long[][] c = { { 9L, 10L }, { 11L, 12L } };
        LongMatrix matrixA = LongMatrix.of(a);
        LongMatrix matrixB = LongMatrix.of(b);
        LongMatrix matrixC = LongMatrix.of(c);

        LongMatrix result = matrixA.zipWith(matrixB, matrixC, (x, y, z) -> (x + y + z) / 3);
        Assertions.assertEquals(5L, result.get(0, 0));
        Assertions.assertEquals(6L, result.get(0, 1));
        Assertions.assertEquals(7L, result.get(1, 0));
        Assertions.assertEquals(8L, result.get(1, 1));
    }

    @Test
    public void testStreamLU2RD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] diagonal = matrix.streamLU2RD().toArray();
        Assertions.assertArrayEquals(new long[] { 1L, 5L, 9L }, diagonal);

        LongMatrix empty = LongMatrix.empty();
        Assertions.assertTrue(empty.streamLU2RD().toList().isEmpty());

        LongMatrix nonSquare = LongMatrix.of(new long[][] { { 1L, 2L } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] diagonal = matrix.streamRU2LD().toArray();
        Assertions.assertArrayEquals(new long[] { 3L, 5L, 7L }, diagonal);
    }

    @Test
    public void testStreamH() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] all = matrix.streamH().toArray();
        Assertions.assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, all);

        LongMatrix empty = LongMatrix.empty();
        Assertions.assertTrue(empty.streamH().toList().isEmpty());
    }

    @Test
    public void testStreamHRow() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] row1 = matrix.streamH(1).toArray();
        Assertions.assertArrayEquals(new long[] { 4L, 5L, 6L }, row1);
    }

    @Test
    public void testStreamHRange() {
        long[][] a = { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] range = matrix.streamH(1, 3).toArray();
        Assertions.assertArrayEquals(new long[] { 3L, 4L, 5L, 6L }, range);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(-1, 2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(0, 4));
    }

    @Test
    public void testStreamV() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] all = matrix.streamV().toArray();
        Assertions.assertArrayEquals(new long[] { 1L, 4L, 2L, 5L, 3L, 6L }, all);
    }

    @Test
    public void testStreamVColumn() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] col1 = matrix.streamV(1).toArray();
        Assertions.assertArrayEquals(new long[] { 2L, 5L }, col1);
    }

    @Test
    public void testStreamVRange() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] range = matrix.streamV(1, 3).toArray();
        Assertions.assertArrayEquals(new long[] { 2L, 5L, 3L, 6L }, range);
    }

    @Test
    public void testStreamR() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        List<LongStream> rows = matrix.streamR().toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new long[] { 1L, 2L }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new long[] { 3L, 4L }, rows.get(1).toArray());
    }

    @Test
    public void testStreamRRange() {
        long[][] a = { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        List<LongStream> rows = matrix.streamR(1, 3).toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new long[] { 3L, 4L }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new long[] { 5L, 6L }, rows.get(1).toArray());
    }

    @Test
    public void testStreamC() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        List<LongStream> cols = matrix.streamC().toList();
        Assertions.assertEquals(3, cols.size());
        Assertions.assertArrayEquals(new long[] { 1L, 4L }, cols.get(0).toArray());
        Assertions.assertArrayEquals(new long[] { 2L, 5L }, cols.get(1).toArray());
        Assertions.assertArrayEquals(new long[] { 3L, 6L }, cols.get(2).toArray());
    }

    @Test
    public void testStreamCRange() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        List<LongStream> cols = matrix.streamC(1, 3).toList();
        Assertions.assertEquals(2, cols.size());
        Assertions.assertArrayEquals(new long[] { 2L, 5L }, cols.get(0).toArray());
        Assertions.assertArrayEquals(new long[] { 3L, 6L }, cols.get(1).toArray());
    }

    @Test
    public void testLength() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        // length is protected, cannot test directly from here
        // It's tested indirectly through other operations
        Assertions.assertEquals(2, matrix.cols);
    }

    @Test
    public void testForEach() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        List<Long> collected = new ArrayList<>();
        matrix.forEach(val -> collected.add(val));

        Assertions.assertEquals(4, collected.size());
        Assertions.assertTrue(collected.contains(1L));
        Assertions.assertTrue(collected.contains(2L));
        Assertions.assertTrue(collected.contains(3L));
        Assertions.assertTrue(collected.contains(4L));
    }

    @Test
    public void testForEachWithRange() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        List<Long> collected = new ArrayList<>();
        matrix.forEach(1, 3, 1, 3, val -> collected.add(val));

        Assertions.assertEquals(4, collected.size());
        Assertions.assertTrue(collected.contains(5L));
        Assertions.assertTrue(collected.contains(6L));
        Assertions.assertTrue(collected.contains(8L));
        Assertions.assertTrue(collected.contains(9L));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.forEach(0, 4, 0, 3, val -> {
        }));
    }

    @Test
    public void testPrintln() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        // Just verify it doesn't throw
        matrix.println();
    }

    @Test
    public void testHashCode() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix1 = LongMatrix.of(a);
        LongMatrix matrix2 = LongMatrix.of(a.clone());

        // Same content should have same hash code
        Assertions.assertEquals(matrix1.hashCode(), matrix2.hashCode());
    }

    @Test
    public void testEquals() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix1 = LongMatrix.of(a);
        LongMatrix matrix2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix matrix3 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 5L } });
        LongMatrix matrix4 = LongMatrix.of(new long[][] { { 1L, 2L } });

        Assertions.assertEquals(matrix1, matrix1);   // same instance
        Assertions.assertEquals(matrix1, matrix2);   // same content
        Assertions.assertNotEquals(matrix1, matrix3);   // different content
        Assertions.assertNotEquals(matrix1, matrix4);   // different dimensions
        Assertions.assertNotEquals(matrix1, null);
        Assertions.assertNotEquals(matrix1, "not a matrix");
    }

    @Test
    public void testToString() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        String str = matrix.toString();
        Assertions.assertNotNull(str);
        Assertions.assertTrue(str.contains("1"));
        Assertions.assertTrue(str.contains("4"));
    }
}