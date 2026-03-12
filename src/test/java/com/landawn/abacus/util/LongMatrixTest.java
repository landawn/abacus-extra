package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalLong;
import com.landawn.abacus.util.stream.LongStream;

class LongMatrixTest extends TestBase {

    @Test
    public void testConstructor() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = new LongMatrix(a);
        Assertions.assertEquals(2, matrix.rowCount());
        Assertions.assertEquals(2, matrix.columnCount());

        LongMatrix nullMatrix = new LongMatrix(null);
        Assertions.assertEquals(0, nullMatrix.rowCount());
        Assertions.assertEquals(0, nullMatrix.columnCount());
    }

    @Test
    public void testEmpty() {
        LongMatrix empty = LongMatrix.empty();
        Assertions.assertEquals(0, empty.rowCount());
        Assertions.assertEquals(0, empty.columnCount());
        Assertions.assertTrue(empty.isEmpty());
    }

    @Test
    public void testOf() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);
        Assertions.assertEquals(2, matrix.rowCount());
        Assertions.assertEquals(2, matrix.columnCount());

        LongMatrix emptyMatrix = LongMatrix.of();
        Assertions.assertTrue(emptyMatrix.isEmpty());

        LongMatrix nullMatrix = LongMatrix.of((long[][]) null);
        Assertions.assertTrue(nullMatrix.isEmpty());
    }

    @Test
    public void testCreate() {
        int[][] a = { { 1, 2 }, { 3, 4 } };
        LongMatrix matrix = LongMatrix.from(a);
        Assertions.assertEquals(2, matrix.rowCount());
        Assertions.assertEquals(2, matrix.columnCount());
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(4L, matrix.get(1, 1));

        LongMatrix emptyMatrix = LongMatrix.from((int[][]) null);
        Assertions.assertTrue(emptyMatrix.isEmpty());
    }

    @Test
    public void testRandom() {
        LongMatrix matrix = LongMatrix.random(5);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(5, matrix.columnCount());
    }

    @Test
    public void testRepeat() {
        LongMatrix matrix = LongMatrix.repeat(1, 10, 5L);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(10, matrix.columnCount());
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(5L, matrix.get(0, i));
        }
    }

    @Test
    public void testRange() {
        LongMatrix matrix = LongMatrix.range(0L, 5L);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(5, matrix.columnCount());
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(i, matrix.get(0, i));
        }
    }

    @Test
    public void testRangeWithStep() {
        LongMatrix matrix = LongMatrix.range(0L, 10L, 2L);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(5, matrix.columnCount());
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(i * 2L, matrix.get(0, i));
        }
    }

    @Test
    public void testRangeClosed() {
        LongMatrix matrix = LongMatrix.rangeClosed(0L, 4L);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(5, matrix.columnCount());
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(i, matrix.get(0, i));
        }
    }

    @Test
    public void testRangeClosedWithStep() {
        LongMatrix matrix = LongMatrix.rangeClosed(0L, 9L, 2L);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(5, matrix.columnCount());
        Assertions.assertEquals(0L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(0, 1));
        Assertions.assertEquals(4L, matrix.get(0, 2));
        Assertions.assertEquals(6L, matrix.get(0, 3));
        Assertions.assertEquals(8L, matrix.get(0, 4));
    }

    @Test
    public void testDiagonalLU2RD() {
        long[] diagonal = { 1L, 2L, 3L };
        LongMatrix matrix = LongMatrix.mainDiagonal(diagonal);
        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(1, 1));
        Assertions.assertEquals(3L, matrix.get(2, 2));
        Assertions.assertEquals(0L, matrix.get(0, 1));
    }

    @Test
    public void testDiagonalRU2LD() {
        long[] diagonal = { 1L, 2L, 3L };
        LongMatrix matrix = LongMatrix.antiDiagonal(diagonal);
        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals(1L, matrix.get(0, 2));
        Assertions.assertEquals(2L, matrix.get(1, 1));
        Assertions.assertEquals(3L, matrix.get(2, 0));
        Assertions.assertEquals(0L, matrix.get(0, 0));
    }

    @Test
    public void testDiagonal() {
        long[] main = { 1L, 2L, 3L };
        long[] anti = { 4L, 5L, 6L };
        LongMatrix matrix = LongMatrix.diagonals(main, anti);
        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(1, 1));
        Assertions.assertEquals(3L, matrix.get(2, 2));
        Assertions.assertEquals(4L, matrix.get(0, 2));
        Assertions.assertEquals(2L, matrix.get(1, 1));
        Assertions.assertEquals(6L, matrix.get(2, 0));

        LongMatrix emptyMatrix = LongMatrix.diagonals(null, null);
        Assertions.assertTrue(emptyMatrix.isEmpty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> LongMatrix.diagonals(new long[] { 1L }, new long[] { 4L, 5L }));
    }

    @Test
    public void testUnbox() {
        Long[][] boxed = { { 1L, 2L }, { 3L, 4L } };
        Matrix<Long> boxedMatrix = Matrix.of(boxed);
        LongMatrix unboxed = LongMatrix.unbox(boxedMatrix);
        Assertions.assertEquals(2, unboxed.rowCount());
        Assertions.assertEquals(2, unboxed.columnCount());
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

        OptionalLong up = matrix.above(1, 0);
        Assertions.assertTrue(up.isPresent());
        Assertions.assertEquals(1L, up.getAsLong());

        OptionalLong empty = matrix.above(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        OptionalLong down = matrix.below(0, 0);
        Assertions.assertTrue(down.isPresent());
        Assertions.assertEquals(3L, down.getAsLong());

        OptionalLong empty = matrix.below(1, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        OptionalLong left = matrix.left(0, 1);
        Assertions.assertTrue(left.isPresent());
        Assertions.assertEquals(1L, left.getAsLong());

        OptionalLong empty = matrix.left(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        OptionalLong right = matrix.right(0, 0);
        Assertions.assertTrue(right.isPresent());
        Assertions.assertEquals(2L, right.getAsLong());

        OptionalLong empty = matrix.right(0, 1);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testRow() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] row0 = matrix.rowView(0);
        Assertions.assertArrayEquals(new long[] { 1L, 2L, 3L }, row0);

        long[] row1 = matrix.rowView(1);
        Assertions.assertArrayEquals(new long[] { 4L, 5L, 6L }, row1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.rowView(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.rowView(2));
    }

    @Test
    public void testColumn() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] col0 = matrix.columnCopy(0);
        Assertions.assertArrayEquals(new long[] { 1L, 4L }, col0);

        long[] col1 = matrix.columnCopy(1);
        Assertions.assertArrayEquals(new long[] { 2L, 5L }, col1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(3));
    }

    @Test
    public void testSetRow() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.setRow(0, new long[] { 10L, 20L, 30L });
        Assertions.assertArrayEquals(new long[] { 10L, 20L, 30L }, matrix.rowView(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setRow(0, new long[] { 1L, 2L }));
    }

    @Test
    public void testSetColumn() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.setColumn(0, new long[] { 10L, 20L });
        Assertions.assertArrayEquals(new long[] { 10L, 20L }, matrix.columnCopy(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setColumn(0, new long[] { 1L }));
    }

    @Test
    public void testUpdateRow() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.updateRow(0, x -> x * 2);
        Assertions.assertArrayEquals(new long[] { 2L, 4L, 6L }, matrix.rowView(0));
    }

    @Test
    public void testUpdateColumn() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.updateColumn(1, x -> x + 10);
        Assertions.assertArrayEquals(new long[] { 12L, 15L }, matrix.columnCopy(1));
    }

    @Test
    public void testUpdateRowAndUpdateColumnInvalidIndex() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateRow(-1, x -> x * 2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateColumn(3, x -> x + 10));
    }

    @Test
    public void testUpdateRowAndUpdateColumnNullOperator() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateRow(0, (Throwables.LongUnaryOperator<RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateColumn(0, (Throwables.LongUnaryOperator<RuntimeException>) null));
    }

    @Test
    public void testGetLU2RD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] diagonal = matrix.getMainDiagonal();
        Assertions.assertArrayEquals(new long[] { 1L, 5L, 9L }, diagonal);

        LongMatrix nonSquare = LongMatrix.of(new long[][] { { 1L, 2L } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.getMainDiagonal());
    }

    @Test
    public void testSetLU2RD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.setMainDiagonal(new long[] { 10L, 20L, 30L });
        Assertions.assertEquals(10L, matrix.get(0, 0));
        Assertions.assertEquals(20L, matrix.get(1, 1));
        Assertions.assertEquals(30L, matrix.get(2, 2));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setMainDiagonal(new long[] { 1L, 2L }));
    }

    @Test
    public void testUpdateLU2RD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.updateMainDiagonal(x -> x * x);
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(25L, matrix.get(1, 1));
        Assertions.assertEquals(81L, matrix.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] diagonal = matrix.getAntiDiagonal();
        Assertions.assertArrayEquals(new long[] { 3L, 5L, 7L }, diagonal);
    }

    @Test
    public void testSetRU2LD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.setAntiDiagonal(new long[] { 10L, 20L, 30L });
        Assertions.assertEquals(10L, matrix.get(0, 2));
        Assertions.assertEquals(20L, matrix.get(1, 1));
        Assertions.assertEquals(30L, matrix.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.updateAntiDiagonal(x -> -x);
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
    public void testMapNullMapper() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.map((Throwables.LongUnaryOperator<RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.mapToObj((Throwables.LongFunction<String, RuntimeException>) null, String.class));
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
        matrix.copyFrom(b);
        Assertions.assertEquals(1L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(0, 1));
        Assertions.assertEquals(3L, matrix.get(1, 0));
        Assertions.assertEquals(4L, matrix.get(1, 1));
        Assertions.assertEquals(0L, matrix.get(2, 2)); // unchanged
    }

    @Test
    public void testFillWithIndices() {
        long[][] a = { { 0L, 0L, 0L }, { 0L, 0L, 0L }, { 0L, 0L, 0L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[][] b = { { 1L, 2L } };
        matrix.copyFrom(1, 1, b);
        Assertions.assertEquals(0L, matrix.get(0, 0)); // unchanged
        Assertions.assertEquals(1L, matrix.get(1, 1));
        Assertions.assertEquals(2L, matrix.get(1, 2));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.copyFrom(-1, 0, b));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.copyFrom(0, -1, b));
    }

    @Test
    public void testCopy() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);
        LongMatrix copy = matrix.copy();

        Assertions.assertEquals(matrix.rowCount(), copy.rowCount());
        Assertions.assertEquals(matrix.columnCount(), copy.columnCount());
        Assertions.assertEquals(1L, copy.get(0, 0));
        Assertions.assertEquals(4L, copy.get(1, 1));

        copy.set(0, 0, 100L);
        Assertions.assertEquals(1L, matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testCopyWithRowRange() {
        long[][] a = { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);
        LongMatrix copy = matrix.copy(1, 3);

        Assertions.assertEquals(2, copy.rowCount());
        Assertions.assertEquals(2, copy.columnCount());
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

        Assertions.assertEquals(2, copy.rowCount());
        Assertions.assertEquals(2, copy.columnCount());
        Assertions.assertEquals(4L, copy.get(0, 0));
        Assertions.assertEquals(5L, copy.get(0, 1));
        Assertions.assertEquals(7L, copy.get(1, 0));
        Assertions.assertEquals(8L, copy.get(1, 1));
    }

    @Test
    public void testExtend() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix extended = matrix.resize(3, 3);
        Assertions.assertEquals(3, extended.rowCount());
        Assertions.assertEquals(3, extended.columnCount());
        Assertions.assertEquals(1L, extended.get(0, 0));
        Assertions.assertEquals(4L, extended.get(1, 1));
        Assertions.assertEquals(0L, extended.get(2, 2));

        LongMatrix truncated = matrix.resize(1, 1);
        Assertions.assertEquals(1, truncated.rowCount());
        Assertions.assertEquals(1, truncated.columnCount());
        Assertions.assertEquals(1L, truncated.get(0, 0));
    }

    @Test
    public void testExtendWithDefaultValue() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix extended = matrix.resize(3, 3, -1L);
        Assertions.assertEquals(-1L, extended.get(2, 2));
        Assertions.assertEquals(-1L, extended.get(0, 2));
        Assertions.assertEquals(-1L, extended.get(2, 0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.resize(-1, 2, 0L));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.resize(2, -1, 0L));
    }

    @Test
    public void testExtendInAllDirections() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix extended = matrix.extend(1, 1, 1, 1);
        Assertions.assertEquals(4, extended.rowCount());
        Assertions.assertEquals(4, extended.columnCount());
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

        matrix.flipInPlaceHorizontally();
        Assertions.assertEquals(3L, matrix.get(0, 0));
        Assertions.assertEquals(2L, matrix.get(0, 1));
        Assertions.assertEquals(1L, matrix.get(0, 2));
        Assertions.assertEquals(6L, matrix.get(1, 0));
    }

    @Test
    public void testReverseV() {
        long[][] a = { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        matrix.flipInPlaceVertically();
        Assertions.assertEquals(5L, matrix.get(0, 0));
        Assertions.assertEquals(6L, matrix.get(0, 1));
        Assertions.assertEquals(3L, matrix.get(1, 0));
        Assertions.assertEquals(1L, matrix.get(2, 0));
    }

    @Test
    public void testFlipH() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix flipped = matrix.flipHorizontally();
        Assertions.assertEquals(3L, flipped.get(0, 0));
        Assertions.assertEquals(1L, flipped.get(0, 2));
        Assertions.assertEquals(1L, matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testFlipV() {
        long[][] a = { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix flipped = matrix.flipVertically();
        Assertions.assertEquals(5L, flipped.get(0, 0));
        Assertions.assertEquals(1L, flipped.get(2, 0));
        Assertions.assertEquals(1L, matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testRotate90() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix rotated = matrix.rotate90();
        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(2, rotated.columnCount());
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
        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(2, rotated.columnCount());
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
        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(2, rotated.columnCount());
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
        Assertions.assertEquals(3, transposed.rowCount());
        Assertions.assertEquals(2, transposed.columnCount());
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
        Assertions.assertEquals(3, reshaped.rowCount());
        Assertions.assertEquals(2, reshaped.columnCount());
        Assertions.assertEquals(1L, reshaped.get(0, 0));
        Assertions.assertEquals(2L, reshaped.get(0, 1));
        Assertions.assertEquals(3L, reshaped.get(1, 0));
        Assertions.assertEquals(4L, reshaped.get(1, 1));

        LongMatrix empty = LongMatrix.empty().reshape(2, 3);
        Assertions.assertEquals(2, empty.rowCount());
        Assertions.assertEquals(3, empty.columnCount());

        // Test reshape with too-small dimensions throws exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.reshape(1, 4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.reshape(2, 2));
    }

    @Test
    public void testRepelem() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix repeated = matrix.repeatElements(2, 3);
        Assertions.assertEquals(4, repeated.rowCount());
        Assertions.assertEquals(6, repeated.columnCount());
        Assertions.assertEquals(1L, repeated.get(0, 0));
        Assertions.assertEquals(1L, repeated.get(0, 2));
        Assertions.assertEquals(1L, repeated.get(1, 0));
        Assertions.assertEquals(2L, repeated.get(0, 3));
        Assertions.assertEquals(4L, repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatElements(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatElements(1, 0));
    }

    @Test
    public void testRepmat() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        LongMatrix repeated = matrix.repeatMatrix(2, 3);
        Assertions.assertEquals(4, repeated.rowCount());
        Assertions.assertEquals(6, repeated.columnCount());
        Assertions.assertEquals(1L, repeated.get(0, 0));
        Assertions.assertEquals(1L, repeated.get(0, 2));
        Assertions.assertEquals(1L, repeated.get(0, 4));
        Assertions.assertEquals(1L, repeated.get(2, 0));
        Assertions.assertEquals(4L, repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatMatrix(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatMatrix(1, 0));
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
        matrix.applyOnFlattened(row -> {
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

        LongMatrix stacked = matrixA.stackVertically(matrixB);
        Assertions.assertEquals(3, stacked.rowCount());
        Assertions.assertEquals(3, stacked.columnCount());
        Assertions.assertEquals(1L, stacked.get(0, 0));
        Assertions.assertEquals(4L, stacked.get(1, 0));
        Assertions.assertEquals(9L, stacked.get(2, 2));

        LongMatrix differentCols = LongMatrix.of(new long[][] { { 1L, 2L } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.stackVertically(differentCols));
    }

    @Test
    public void testHstack() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        long[][] b = { { 5L }, { 6L } };
        LongMatrix matrixA = LongMatrix.of(a);
        LongMatrix matrixB = LongMatrix.of(b);

        LongMatrix stacked = matrixA.stackHorizontally(matrixB);
        Assertions.assertEquals(2, stacked.rowCount());
        Assertions.assertEquals(3, stacked.columnCount());
        Assertions.assertEquals(1L, stacked.get(0, 0));
        Assertions.assertEquals(5L, stacked.get(0, 2));
        Assertions.assertEquals(6L, stacked.get(1, 2));

        LongMatrix differentRows = LongMatrix.of(new long[][] { { 1L } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.stackHorizontally(differentRows));
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
        Assertions.assertEquals(2, product.rowCount());
        Assertions.assertEquals(2, product.columnCount());
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
        Assertions.assertEquals(2, boxed.rowCount());
        Assertions.assertEquals(2, boxed.columnCount());
        Assertions.assertEquals(Long.valueOf(1L), boxed.get(0, 0));
        Assertions.assertEquals(Long.valueOf(4L), boxed.get(1, 1));
    }

    @Test
    public void testToIntMatrix() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        IntMatrix intMatrix = matrix.toIntMatrix();
        assertEquals(2, intMatrix.rowCount());
        assertEquals(2, intMatrix.columnCount());
        assertEquals(1, intMatrix.get(0, 0));
        assertEquals(4, intMatrix.get(1, 1));
    }

    @Test
    public void testToFloatMatrix() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        FloatMatrix floatMatrix = matrix.toFloatMatrix();
        Assertions.assertEquals(2, floatMatrix.rowCount());
        Assertions.assertEquals(2, floatMatrix.columnCount());
        Assertions.assertEquals(1.0f, floatMatrix.get(0, 0));
        Assertions.assertEquals(4.0f, floatMatrix.get(1, 1));
    }

    @Test
    public void testToDoubleMatrix() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        DoubleMatrix doubleMatrix = matrix.toDoubleMatrix();
        Assertions.assertEquals(2, doubleMatrix.rowCount());
        Assertions.assertEquals(2, doubleMatrix.columnCount());
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

        long[] diagonal = matrix.streamMainDiagonal().toArray();
        Assertions.assertArrayEquals(new long[] { 1L, 5L, 9L }, diagonal);

        LongMatrix empty = LongMatrix.empty();
        Assertions.assertTrue(empty.streamMainDiagonal().toList().isEmpty());

        LongMatrix nonSquare = LongMatrix.of(new long[][] { { 1L, 2L } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
    }

    @Test
    public void testStreamRU2LD() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] diagonal = matrix.streamAntiDiagonal().toArray();
        Assertions.assertArrayEquals(new long[] { 3L, 5L, 7L }, diagonal);
    }

    @Test
    public void testStreamH() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] all = matrix.streamHorizontal().toArray();
        Assertions.assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, all);

        LongMatrix empty = LongMatrix.empty();
        Assertions.assertTrue(empty.streamHorizontal().toList().isEmpty());
    }

    @Test
    public void testStreamHRow() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] row1 = matrix.streamHorizontal(1).toArray();
        Assertions.assertArrayEquals(new long[] { 4L, 5L, 6L }, row1);
    }

    @Test
    public void testStreamHRange() {
        long[][] a = { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] range = matrix.streamHorizontal(1, 3).toArray();
        Assertions.assertArrayEquals(new long[] { 3L, 4L, 5L, 6L }, range);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(-1, 2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(0, 4));
    }

    @Test
    public void testStreamV() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] all = matrix.streamVertical().toArray();
        Assertions.assertArrayEquals(new long[] { 1L, 4L, 2L, 5L, 3L, 6L }, all);
    }

    @Test
    public void testStreamVColumn() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] col1 = matrix.streamVertical(1).toArray();
        Assertions.assertArrayEquals(new long[] { 2L, 5L }, col1);
    }

    @Test
    public void testStreamVRange() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        long[] range = matrix.streamVertical(1, 3).toArray();
        Assertions.assertArrayEquals(new long[] { 2L, 5L, 3L, 6L }, range);
    }

    @Test
    public void testStreamR() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        List<LongStream> rows = matrix.streamRows().toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new long[] { 1L, 2L }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new long[] { 3L, 4L }, rows.get(1).toArray());
    }

    @Test
    public void testStreamRRange() {
        long[][] a = { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        List<LongStream> rows = matrix.streamRows(1, 3).toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new long[] { 3L, 4L }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new long[] { 5L, 6L }, rows.get(1).toArray());
    }

    @Test
    public void testStreamC() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        List<LongStream> columnCount = matrix.streamColumns().toList();
        Assertions.assertEquals(3, columnCount.size());
        Assertions.assertArrayEquals(new long[] { 1L, 4L }, columnCount.get(0).toArray());
        Assertions.assertArrayEquals(new long[] { 2L, 5L }, columnCount.get(1).toArray());
        Assertions.assertArrayEquals(new long[] { 3L, 6L }, columnCount.get(2).toArray());
    }

    @Test
    public void testStreamCRange() {
        long[][] a = { { 1L, 2L, 3L }, { 4L, 5L, 6L } };
        LongMatrix matrix = LongMatrix.of(a);

        List<LongStream> columnCount = matrix.streamColumns(1, 3).toList();
        Assertions.assertEquals(2, columnCount.size());
        Assertions.assertArrayEquals(new long[] { 2L, 5L }, columnCount.get(0).toArray());
        Assertions.assertArrayEquals(new long[] { 3L, 6L }, columnCount.get(1).toArray());
    }

    @Test
    public void testLength() {
        LongMatrix matrix = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        // length is protected, cannot test directly from here
        // It's tested indirectly through other operations
        Assertions.assertEquals(2, matrix.columnCount());
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
    public void testForEachNullAction() {
        long[][] a = { { 1L, 2 }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.forEach((Throwables.LongConsumer<RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.forEach(0, 1, 0, 2, (Throwables.LongConsumer<RuntimeException>) null));
    }

    @Test
    public void testPrintln() {
        long[][] a = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix matrix = LongMatrix.of(a);

        assertFalse(matrix.isEmpty());
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(matrix::println);
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

        Assertions.assertEquals(matrix1, matrix1); // same instance
        Assertions.assertEquals(matrix1, matrix2); // same content
        Assertions.assertNotEquals(matrix1, matrix3); // different content
        Assertions.assertNotEquals(matrix1, matrix4); // different dimensions
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

    @Nested
    class JavadocExampleMatrixGroup2Test_LongMatrix extends TestBase {
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
    }

    @Nested
    class JavadocExampleMatrixTest_LongMatrix extends TestBase {
        // ==================== LongMatrix Javadoc Examples ====================

        @Test
        public void testLongMatrixEmptyRowCount() {
            LongMatrix matrix = LongMatrix.empty();
            assertEquals(0, matrix.rowCount());
            assertEquals(0, matrix.columnCount());
        }

        @Test
        public void testLongMatrixForEach() {
            // LongMatrix.java: matrix.forEach(value -> System.out.print(value + " "));
            // Output: 1 2 3 4
            LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
            List<Long> values = new ArrayList<>();
            matrix.forEach(value -> values.add(value));
            assertEquals(4, values.size());
            assertEquals(1L, values.get(0));
            assertEquals(2L, values.get(1));
            assertEquals(3L, values.get(2));
            assertEquals(4L, values.get(3));
        }

        @Test
        public void testLongMatrixForEachWithRange() {
            // LongMatrix.java: matrix.forEach(1, 3, 1, 3, value -> System.out.print(value + " "));
            // Output: 5 6 8 9 (elements from rows 1-2, columns 1-2)
            LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Long> values = new ArrayList<>();
            matrix.forEach(1, 3, 1, 3, value -> values.add(value));
            assertEquals(4, values.size());
            assertEquals(5L, values.get(0));
            assertEquals(6L, values.get(1));
            assertEquals(8L, values.get(2));
            assertEquals(9L, values.get(3));
        }

        // ==================== LongMatrix range examples ====================

        @Test
        public void testLongMatrixRange() {
            // LongMatrix range examples similar to IntMatrix
            LongMatrix matrix = LongMatrix.range(0, 5);
            assertEquals("[[0, 1, 2, 3, 4]]", matrix.toString());
        }

        @Test
        public void testLongMatrixRangeWithStep() {
            LongMatrix matrix = LongMatrix.range(0, 10, 2);
            assertEquals("[[0, 2, 4, 6, 8]]", matrix.toString());
        }

        // ==================== LongMatrix toString example ====================

        @Test
        public void testLongMatrixToString() {
            LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertEquals("[[1, 2, 3], [4, 5, 6]]", matrix.toString());
        }
    }

    @Nested
    @Tag("2025")
    class LongMatrix2025Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            long[][] arr = { { 1L, 2L }, { 3L, 4L } };
            LongMatrix m = new LongMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(4L, m.get(1, 1));
        }

        @Test
        public void testConstructor_withNullArray() {
            LongMatrix m = new LongMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withEmptyArray() {
            LongMatrix m = new LongMatrix(new long[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withSingleElement() {
            LongMatrix m = new LongMatrix(new long[][] { { 42L } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42L, m.get(0, 0));
        }

        @Test
        public void testConstructor_withLargeValues() {
            long[][] arr = { { Long.MAX_VALUE, Long.MIN_VALUE }, { 1000000000000L, -1000000000000L } };
            LongMatrix m = new LongMatrix(arr);
            assertEquals(Long.MAX_VALUE, m.get(0, 0));
            assertEquals(Long.MIN_VALUE, m.get(0, 1));
            assertEquals(1000000000000L, m.get(1, 0));
            assertEquals(-1000000000000L, m.get(1, 1));
        }

        // ============ Factory Method Tests ============

        @Test
        public void testEmpty() {
            LongMatrix empty = LongMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());

            // Test singleton
            assertSame(LongMatrix.empty(), LongMatrix.empty());
        }

        @Test
        public void testOf_withValidArray() {
            long[][] arr = { { 1L, 2L }, { 3L, 4L } };
            LongMatrix m = LongMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void testOf_withNullArray() {
            LongMatrix m = LongMatrix.of((long[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withEmptyArray() {
            LongMatrix m = LongMatrix.of(new long[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray() {
            int[][] ints = { { 1, 2 }, { 3, 4 } };
            LongMatrix m = LongMatrix.from(ints);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(4L, m.get(1, 1));
        }

        @Test
        public void testCreateFromIntArray_withNull() {
            LongMatrix m = LongMatrix.from((int[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray_withEmpty() {
            LongMatrix m = LongMatrix.from(new int[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray_withJaggedArray() {
            int[][] jagged = { { 1, 2 }, { 3 } };
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(jagged));
        }

        @Test
        public void testCreateFromIntArray_withNullRow() {
            int[][] nullRow = { { 1, 2 }, null };
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(nullRow));
        }

        @Test
        public void testCreateFromIntArray_withNullFirstRow() {
            int[][] nullFirstRow = { null, { 1, 2 } };
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(nullFirstRow));
        }

        @Test
        public void testCreateFromIntArray_withLargeInts() {
            int[][] ints = { { Integer.MAX_VALUE, Integer.MIN_VALUE }, { 1000000, -1000000 } };
            LongMatrix m = LongMatrix.from(ints);
            assertEquals(Integer.MAX_VALUE, m.get(0, 0));
            assertEquals(Integer.MIN_VALUE, m.get(0, 1));
            assertEquals(1000000L, m.get(1, 0));
            assertEquals(-1000000L, m.get(1, 1));
        }

        @Test
        public void testRandom() {
            LongMatrix m = LongMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            // Just verify elements exist (values are random)
            for (int i = 0; i < 5; i++) {
                assertNotNull(m.get(0, i));
            }
        }

        @Test
        public void testRandom_withRowsCols() {
            LongMatrix m = LongMatrix.random(2, 3);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertNotNull(m.get(i, j));
                }
            }
        }

        @Test
        public void testRepeat() {
            LongMatrix m = LongMatrix.repeat(1, 5, 42L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(42L, m.get(0, i));
            }
        }

        @Test
        public void testRepeat_withRowsCols() {
            LongMatrix m = LongMatrix.repeat(2, 3, 42L);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(42L, m.get(i, j));
                }
            }
        }

        @Test
        public void testRepeat_withLargeValue() {
            LongMatrix m = LongMatrix.repeat(1, 3, Long.MAX_VALUE);
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 3; i++) {
                assertEquals(Long.MAX_VALUE, m.get(0, i));
            }
        }

        @Test
        public void testRange() {
            LongMatrix m = LongMatrix.range(0L, 5L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.rowView(0));
        }

        @Test
        public void testRange_withStep() {
            LongMatrix m = LongMatrix.range(0L, 10L, 2L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L }, m.rowView(0));
        }

        @Test
        public void testRange_withNegativeStep() {
            LongMatrix m = LongMatrix.range(10L, 0L, -2L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 10L, 8L, 6L, 4L, 2L }, m.rowView(0));
        }

        @Test
        public void testRange_withLargeValues() {
            LongMatrix m = LongMatrix.range(1000000000000L, 1000000000005L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 1000000000000L, 1000000000001L, 1000000000002L, 1000000000003L, 1000000000004L }, m.rowView(0));
        }

        @Test
        public void testRangeClosed() {
            LongMatrix m = LongMatrix.rangeClosed(0L, 4L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.rowView(0));
        }

        @Test
        public void testRangeClosed_withStep() {
            LongMatrix m = LongMatrix.rangeClosed(0L, 10L, 2L);
            assertEquals(1, m.rowCount());
            assertEquals(6, m.columnCount());
            assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L, 10L }, m.rowView(0));
        }

        @Test
        public void testDiagonalLU2RD() {
            LongMatrix m = LongMatrix.mainDiagonal(new long[] { 1L, 2L, 3L });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 2));
            assertEquals(0L, m.get(0, 1));
            assertEquals(0L, m.get(1, 0));
        }

        @Test
        public void testDiagonalRU2LD() {
            LongMatrix m = LongMatrix.antiDiagonal(new long[] { 1L, 2L, 3L });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 2));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 0));
            assertEquals(0L, m.get(0, 0));
            assertEquals(0L, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            LongMatrix m = LongMatrix.diagonals(new long[] { 1L, 2L, 3L }, new long[] { 4L, 5L, 6L });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 2));
            assertEquals(4L, m.get(0, 2));
            assertEquals(6L, m.get(2, 0));
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            LongMatrix m = LongMatrix.diagonals(new long[] { 1L, 2L, 3L }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            LongMatrix m = LongMatrix.diagonals(null, new long[] { 4L, 5L, 6L });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(4L, m.get(0, 2));
            assertEquals(5L, m.get(1, 1));
            assertEquals(6L, m.get(2, 0));
        }

        @Test
        public void testDiagonal_withBothNull() {
            LongMatrix m = LongMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.diagonals(new long[] { 1L, 2L }, new long[] { 3L, 4L, 5L }));
        }

        @Test
        public void testUnbox() {
            Long[][] boxed = { { 1L, 2L }, { 3L, 4L } };
            Matrix<Long> boxedMatrix = Matrix.of(boxed);
            LongMatrix unboxed = LongMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertEquals(1L, unboxed.get(0, 0));
            assertEquals(4L, unboxed.get(1, 1));
        }

        @Test
        public void testUnbox_withNullValues() {
            Long[][] boxed = { { 1L, null }, { null, 4L } };
            Matrix<Long> boxedMatrix = Matrix.of(boxed);
            LongMatrix unboxed = LongMatrix.unbox(boxedMatrix);
            assertEquals(1L, unboxed.get(0, 0));
            assertEquals(0L, unboxed.get(0, 1)); // null -> 0
            assertEquals(0L, unboxed.get(1, 0)); // null -> 0
            assertEquals(4L, unboxed.get(1, 1));
        }

        // ============ Component Type Tests ============

        @Test
        public void testComponentType() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L } });
            assertEquals(long.class, m.componentType());
        }

        // ============ Get/Set Tests ============

        @Test
        public void testGet() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            assertEquals(1L, m.get(0, 0));
            assertEquals(5L, m.get(1, 1));
            assertEquals(6L, m.get(1, 2));
        }

        @Test
        public void testGet_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
        }

        @Test
        public void testGetWithPoint() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertEquals(1L, m.get(Point.of(0, 0)));
            assertEquals(4L, m.get(Point.of(1, 1)));
            assertEquals(2L, m.get(Point.of(0, 1)));
        }

        @Test
        public void testSet() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.set(0, 0, 10L);
            assertEquals(10L, m.get(0, 0));

            m.set(1, 1, 20L);
            assertEquals(20L, m.get(1, 1));
        }

        @Test
        public void testSet_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, 0L));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, 0L));
        }

        @Test
        public void testSetWithPoint() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.set(Point.of(0, 0), 50L);
            assertEquals(50L, m.get(Point.of(0, 0)));
        }

        @Test
        public void testSet_withLargeValues() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.set(0, 0, Long.MAX_VALUE);
            m.set(1, 1, Long.MIN_VALUE);
            assertEquals(Long.MAX_VALUE, m.get(0, 0));
            assertEquals(Long.MIN_VALUE, m.get(1, 1));
        }

        // ============ Adjacent Element Tests ============

        @Test
        public void testUpOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

            OptionalLong up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1L, up.get());

            // Top row has no element above
            OptionalLong empty = m.above(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testDownOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

            OptionalLong down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3L, down.get());

            // Bottom row has no element below
            OptionalLong empty = m.below(1, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testLeftOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

            OptionalLong left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1L, left.get());

            // Leftmost column has no element to the left
            OptionalLong empty = m.left(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testRightOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

            OptionalLong right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2L, right.get());

            // Rightmost column has no element to the right
            OptionalLong empty = m.right(0, 1);
            assertFalse(empty.isPresent());
        }

        // ============ Row/Column Operations Tests ============

        @Test
        public void testRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            assertArrayEquals(new long[] { 1L, 2L, 3L }, m.rowView(0));
            assertArrayEquals(new long[] { 4L, 5L, 6L }, m.rowView(1));
        }

        @Test
        public void testRow_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(2));
        }

        @Test
        public void testColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            assertArrayEquals(new long[] { 1L, 4L }, m.columnCopy(0));
            assertArrayEquals(new long[] { 2L, 5L }, m.columnCopy(1));
            assertArrayEquals(new long[] { 3L, 6L }, m.columnCopy(2));
        }

        @Test
        public void testColumn_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.setRow(0, new long[] { 10L, 20L });
            assertArrayEquals(new long[] { 10L, 20L }, m.rowView(0));
            assertArrayEquals(new long[] { 3L, 4L }, m.rowView(1)); // unchanged
        }

        @Test
        public void testSetRow_wrongSize() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new long[] { 1L }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new long[] { 1L, 2L, 3L }));
        }

        @Test
        public void testSetColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.setColumn(0, new long[] { 10L, 20L });
            assertArrayEquals(new long[] { 10L, 20L }, m.columnCopy(0));
            assertArrayEquals(new long[] { 2L, 4L }, m.columnCopy(1)); // unchanged
        }

        @Test
        public void testSetColumn_wrongSize() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new long[] { 1L }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new long[] { 1L, 2L, 3L }));
        }

        @Test
        public void testUpdateRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateRow(0, x -> x * 2L);
            assertArrayEquals(new long[] { 2L, 4L }, m.rowView(0));
            assertArrayEquals(new long[] { 3L, 4L }, m.rowView(1)); // unchanged
        }

        @Test
        public void testUpdateColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateColumn(0, x -> x + 10L);
            assertArrayEquals(new long[] { 11L, 13L }, m.columnCopy(0));
            assertArrayEquals(new long[] { 2L, 4L }, m.columnCopy(1)); // unchanged
        }

        // ============ Diagonal Operations Tests ============

        @Test
        public void testGetLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            assertArrayEquals(new long[] { 1L, 5L, 9L }, m.getMainDiagonal());
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.setMainDiagonal(new long[] { 10L, 20L, 30L });
            assertEquals(10L, m.get(0, 0));
            assertEquals(20L, m.get(1, 1));
            assertEquals(30L, m.get(2, 2));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new long[] { 1L }));
        }

        @Test
        public void testSetLU2RD_arrayTooShort() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new long[] { 1L, 2L }));
        }

        @Test
        public void testUpdateLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.updateMainDiagonal(x -> x * 10L);
            assertEquals(10L, m.get(0, 0));
            assertEquals(50L, m.get(1, 1));
            assertEquals(90L, m.get(2, 2));
            assertEquals(2L, m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateLU2RD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.updateMainDiagonal(x -> x * 2L));
        }

        @Test
        public void testGetRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            assertArrayEquals(new long[] { 3L, 5L, 7L }, m.getAntiDiagonal());
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.setAntiDiagonal(new long[] { 10L, 20L, 30L });
            assertEquals(10L, m.get(0, 2));
            assertEquals(20L, m.get(1, 1));
            assertEquals(30L, m.get(2, 0));
        }

        @Test
        public void testSetRU2LD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.setAntiDiagonal(new long[] { 1L }));
        }

        @Test
        public void testSetRU2LD_arrayTooShort() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new long[] { 1L, 2L }));
        }

        @Test
        public void testUpdateRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.updateAntiDiagonal(x -> x * 10L);
            assertEquals(30L, m.get(0, 2));
            assertEquals(50L, m.get(1, 1));
            assertEquals(70L, m.get(2, 0));
            assertEquals(2L, m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateRU2LD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(x -> x * 2L));
        }

        // ============ Transformation Tests ============

        @Test
        public void testUpdateAll() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateAll(x -> x * 2L);
            assertEquals(2L, m.get(0, 0));
            assertEquals(4L, m.get(0, 1));
            assertEquals(6L, m.get(1, 0));
            assertEquals(8L, m.get(1, 1));
        }

        @Test
        public void testUpdateAll_withIndices() {
            LongMatrix m = LongMatrix.of(new long[][] { { 0L, 0L }, { 0L, 0L } });
            m.updateAll((i, j) -> (long) (i * 10 + j));
            assertEquals(0L, m.get(0, 0));
            assertEquals(1L, m.get(0, 1));
            assertEquals(10L, m.get(1, 0));
            assertEquals(11L, m.get(1, 1));
        }

        @Test
        public void testReplaceIf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            m.replaceIf(x -> x > 3L, 0L);
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(3L, m.get(0, 2));
            assertEquals(0L, m.get(1, 0)); // was 4
            assertEquals(0L, m.get(1, 1)); // was 5
            assertEquals(0L, m.get(1, 2)); // was 6
        }

        @Test
        public void testReplaceIf_withIndices() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.replaceIf((i, j) -> i == j, 0L); // Replace diagonal
            assertEquals(0L, m.get(0, 0));
            assertEquals(0L, m.get(1, 1));
            assertEquals(0L, m.get(2, 2));
            assertEquals(2L, m.get(0, 1)); // unchanged
        }

        @Test
        public void testMap() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix result = m.map(x -> x * 2L);
            assertEquals(2L, result.get(0, 0));
            assertEquals(4L, result.get(0, 1));
            assertEquals(6L, result.get(1, 0));
            assertEquals(8L, result.get(1, 1));

            // Original unchanged
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void testMapToInt() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            IntMatrix result = m.mapToInt(x -> (int) (x * 10));
            assertEquals(10, result.get(0, 0));
            assertEquals(40, result.get(1, 1));
        }

        @Test
        public void testMapToDouble() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            DoubleMatrix result = m.mapToDouble(x -> x * 0.1);
            assertEquals(0.1, result.get(0, 0), 0.0001);
            assertEquals(0.4, result.get(1, 1), 0.0001);
        }

        @Test
        public void testMapToObj() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            Matrix<String> result = m.mapToObj(x -> "val:" + x, String.class);
            assertEquals("val:1", result.get(0, 0));
            assertEquals("val:4", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_withValue() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.fill(99L);
            for (int i = 0; i < m.rowCount(); i++) {
                for (int j = 0; j < m.columnCount(); j++) {
                    assertEquals(99L, m.get(i, j));
                }
            }
        }

        @Test
        public void testFill_withArray() {
            LongMatrix m = LongMatrix.of(new long[][] { { 0L, 0L, 0L }, { 0L, 0L, 0L }, { 0L, 0L, 0L } });
            long[][] patch = { { 1L, 2L }, { 3L, 4L } };
            m.copyFrom(patch);
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(3L, m.get(1, 0));
            assertEquals(4L, m.get(1, 1));
            assertEquals(0L, m.get(2, 2)); // unchanged
        }

        @Test
        public void testFill_withArrayAtPosition() {
            LongMatrix m = LongMatrix.of(new long[][] { { 0L, 0L, 0L }, { 0L, 0L, 0L }, { 0L, 0L, 0L } });
            long[][] patch = { { 1L, 2L }, { 3L, 4L } };
            m.copyFrom(1, 1, patch);
            assertEquals(0L, m.get(0, 0)); // unchanged
            assertEquals(1L, m.get(1, 1));
            assertEquals(2L, m.get(1, 2));
            assertEquals(3L, m.get(2, 1));
            assertEquals(4L, m.get(2, 2));
        }

        @Test
        public void testFill_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long[][] patch = { { 1L, 2L }, { 3L, 4L } };
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals(1L, copy.get(0, 0));

            // Modify copy shouldn't affect original
            copy.set(0, 0, 99L);
            assertEquals(1L, m.get(0, 0));
            assertEquals(99L, copy.get(0, 0));
        }

        @Test
        public void testCopy_withRowRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix subset = m.copy(1, 3);
            assertEquals(2, subset.rowCount());
            assertEquals(3, subset.columnCount());
            assertEquals(4L, subset.get(0, 0));
            assertEquals(9L, subset.get(1, 2));
        }

        @Test
        public void testCopy_withRowRange_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
        }

        @Test
        public void testCopy_withFullRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix submatrix = m.copy(0, 2, 1, 3);
            assertEquals(2, submatrix.rowCount());
            assertEquals(2, submatrix.columnCount());
            assertEquals(2L, submatrix.get(0, 0));
            assertEquals(6L, submatrix.get(1, 1));
        }

        @Test
        public void testCopy_withFullRange_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_larger() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix extended = m.resize(4, 4);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1L, extended.get(0, 0));
            assertEquals(4L, extended.get(1, 1));
            assertEquals(0L, extended.get(3, 3)); // new cells are 0
        }

        @Test
        public void testExtend_smaller() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix truncated = m.resize(2, 2);
            assertEquals(2, truncated.rowCount());
            assertEquals(2, truncated.columnCount());
            assertEquals(1L, truncated.get(0, 0));
            assertEquals(5L, truncated.get(1, 1));
        }

        @Test
        public void testExtend_withDefaultValue() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix extended = m.resize(3, 3, -1L);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1L, extended.get(0, 0));
            assertEquals(-1L, extended.get(2, 2)); // new cell
        }

        @Test
        public void testExtend_withNegativeDimensions() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 3, 0L));
            assertThrows(IllegalArgumentException.class, () -> m.resize(3, -1, 0L));
        }

        @Test
        public void testExtend_directional() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix extended = m.extend(1, 1, 2, 2);
            assertEquals(5, extended.rowCount()); // 1 + 3 + 1
            assertEquals(7, extended.columnCount()); // 2 + 3 + 2

            // Original values at offset position
            assertEquals(1L, extended.get(1, 2));
            assertEquals(5L, extended.get(2, 3));

            // New cells are 0
            assertEquals(0L, extended.get(0, 0));
        }

        @Test
        public void testExtend_directionalWithDefault() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix extended = m.extend(1, 1, 1, 1, -1L);
            assertEquals(5, extended.rowCount());
            assertEquals(5, extended.columnCount());

            // Check original values
            assertEquals(1L, extended.get(1, 1));

            // Check new values
            assertEquals(-1L, extended.get(0, 0));
            assertEquals(-1L, extended.get(4, 4));
        }

        @Test
        public void testExtend_directionalWithNegativeValues() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, 0L));
        }

        // ============ Reverse/Flip Tests ============

        @Test
        public void testReverseH() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            m.flipInPlaceHorizontally();
            assertEquals(3L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(1L, m.get(0, 2));
            assertEquals(6L, m.get(1, 0));
        }

        @Test
        public void testReverseV() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            m.flipInPlaceVertically();
            assertEquals(5L, m.get(0, 0));
            assertEquals(6L, m.get(0, 1));
            assertEquals(3L, m.get(1, 0));
            assertEquals(1L, m.get(2, 0));
        }

        @Test
        public void testFlipH() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix flipped = m.flipHorizontally();
            assertEquals(3L, flipped.get(0, 0));
            assertEquals(2L, flipped.get(0, 1));
            assertEquals(1L, flipped.get(0, 2));

            // Original unchanged
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            LongMatrix flipped = m.flipVertically();
            assertEquals(5L, flipped.get(0, 0));
            assertEquals(3L, flipped.get(1, 0));
            assertEquals(1L, flipped.get(2, 0));

            // Original unchanged
            assertEquals(1L, m.get(0, 0));
        }

        // ============ Rotation Tests ============

        @Test
        public void testRotate90() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3L, rotated.get(0, 0));
            assertEquals(1L, rotated.get(0, 1));
            assertEquals(4L, rotated.get(1, 0));
            assertEquals(2L, rotated.get(1, 1));
        }

        @Test
        public void testRotate180() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix rotated = m.rotate180();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4L, rotated.get(0, 0));
            assertEquals(3L, rotated.get(0, 1));
            assertEquals(2L, rotated.get(1, 0));
            assertEquals(1L, rotated.get(1, 1));
        }

        @Test
        public void testRotate270() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(2L, rotated.get(0, 0));
            assertEquals(4L, rotated.get(0, 1));
            assertEquals(1L, rotated.get(1, 0));
            assertEquals(3L, rotated.get(1, 1));
        }

        @Test
        public void testRotate90_verticalMatrix() {
            // Test the rows > columnCount branch (3 rows, 2 columnCount)
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            LongMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(3, rotated.columnCount());
            assertEquals(5L, rotated.get(0, 0));
            assertEquals(3L, rotated.get(0, 1));
            assertEquals(1L, rotated.get(0, 2));
            assertEquals(6L, rotated.get(1, 0));
            assertEquals(4L, rotated.get(1, 1));
            assertEquals(2L, rotated.get(1, 2));
        }

        @Test
        public void testRotate270_verticalMatrix() {
            // Test the rows > columnCount branch (3 rows, 2 columnCount)
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            LongMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(3, rotated.columnCount());
            assertEquals(2L, rotated.get(0, 0));
            assertEquals(4L, rotated.get(0, 1));
            assertEquals(6L, rotated.get(0, 2));
            assertEquals(1L, rotated.get(1, 0));
            assertEquals(3L, rotated.get(1, 1));
            assertEquals(5L, rotated.get(1, 2));
        }

        // ============ Transpose Tests ============

        @Test
        public void testTranspose() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1L, transposed.get(0, 0));
            assertEquals(4L, transposed.get(0, 1));
            assertEquals(2L, transposed.get(1, 0));
            assertEquals(5L, transposed.get(1, 1));
            assertEquals(3L, transposed.get(2, 0));
            assertEquals(6L, transposed.get(2, 1));
        }

        @Test
        public void testTranspose_square() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1L, transposed.get(0, 0));
            assertEquals(3L, transposed.get(0, 1));
            assertEquals(2L, transposed.get(1, 0));
        }

        @Test
        public void testTranspose_verticalMatrix() {
            // Test the rows > columnCount branch (3 rows, 2 columnCount)
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            LongMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(3, transposed.columnCount());
            assertEquals(1L, transposed.get(0, 0));
            assertEquals(3L, transposed.get(0, 1));
            assertEquals(5L, transposed.get(0, 2));
            assertEquals(2L, transposed.get(1, 0));
            assertEquals(4L, transposed.get(1, 1));
            assertEquals(6L, transposed.get(1, 2));
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix reshaped = m.reshape(1, 9);
            assertEquals(1, reshaped.rowCount());
            assertEquals(9, reshaped.columnCount());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1L, reshaped.get(0, i));
            }
        }

        @Test
        public void testReshape_back() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix reshaped = m.reshape(1, 9);
            LongMatrix reshapedBack = reshaped.reshape(3, 3);
            assertEquals(m, reshapedBack);
        }

        @Test
        public void testReshape_empty() {
            LongMatrix empty = LongMatrix.empty();
            LongMatrix reshaped = empty.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        // ============ Repeat Tests ============

        @Test
        public void testRepelem() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix repeated = m.repeatElements(2, 3);
            assertEquals(2, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals(1L, repeated.get(0, 0));
            assertEquals(1L, repeated.get(0, 1));
            assertEquals(1L, repeated.get(0, 2));
            assertEquals(2L, repeated.get(0, 3));
            assertEquals(2L, repeated.get(0, 4));
            assertEquals(2L, repeated.get(0, 5));
            assertEquals(1L, repeated.get(1, 0)); // second row same as first
        }

        @Test
        public void testRepelem_invalidArguments() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix repeated = m.repeatMatrix(2, 3);
            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals(1L, repeated.get(0, 0));
            assertEquals(2L, repeated.get(0, 1));
            assertEquals(1L, repeated.get(0, 2)); // repeat starts
            assertEquals(2L, repeated.get(0, 3));

            assertEquals(3L, repeated.get(1, 0));
            assertEquals(4L, repeated.get(1, 1));

            // Check vertical repeat
            assertEquals(1L, repeated.get(2, 0)); // vertical repeat starts
            assertEquals(2L, repeated.get(2, 1));
        }

        @Test
        public void testRepmat_invalidArguments() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongList flat = m.flatten();
            assertEquals(9, flat.size());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1L, flat.get(i));
            }
        }

        @Test
        public void testFlatten_empty() {
            LongMatrix empty = LongMatrix.empty();
            LongList flat = empty.flatten();
            assertTrue(flat.isEmpty());
        }

        @Test
        public void testFlatOp() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            List<Long> sums = new ArrayList<>();
            m.applyOnFlattened(row -> {
                long sum = 0;
                for (long val : row) {
                    sum += val;
                }
                sums.add(sum);
            });
            assertEquals(1, sums.size());
            assertEquals(45L, sums.get(0).longValue());
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 7L, 8L, 9L }, { 10L, 11L, 12L } });
            LongMatrix stacked = m1.stackVertically(m2);

            assertEquals(4, stacked.rowCount());
            assertEquals(3, stacked.columnCount());
            assertEquals(1L, stacked.get(0, 0));
            assertEquals(7L, stacked.get(2, 0));
            assertEquals(12L, stacked.get(3, 2));
        }

        @Test
        public void testVstack_differentColumnCounts() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix stacked = m1.stackHorizontally(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertEquals(1L, stacked.get(0, 0));
            assertEquals(5L, stacked.get(0, 2));
            assertEquals(8L, stacked.get(1, 3));
        }

        @Test
        public void testHstack_differentRowCounts() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Operations Tests ============

        @Test
        public void testAdd() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix sum = m1.add(m2);

            assertEquals(6L, sum.get(0, 0));
            assertEquals(8L, sum.get(0, 1));
            assertEquals(10L, sum.get(1, 0));
            assertEquals(12L, sum.get(1, 1));
        }

        @Test
        public void testAdd_differentDimensions() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void testAdd_withLargeValues() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1000000000000L, 2000000000000L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3000000000000L, 4000000000000L } });
            LongMatrix sum = m1.add(m2);
            assertEquals(4000000000000L, sum.get(0, 0));
            assertEquals(6000000000000L, sum.get(0, 1));
        }

        @Test
        public void testSubtract() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix diff = m1.subtract(m2);

            assertEquals(4L, diff.get(0, 0));
            assertEquals(4L, diff.get(0, 1));
            assertEquals(4L, diff.get(1, 0));
            assertEquals(4L, diff.get(1, 1));
        }

        @Test
        public void testSubtract_differentDimensions() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
            assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
        }

        @Test
        public void testMultiply() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix product = m1.multiply(m2);

            assertEquals(19L, product.get(0, 0)); // 1*5 + 2*7
            assertEquals(22L, product.get(0, 1)); // 1*6 + 2*8
            assertEquals(43L, product.get(1, 0)); // 3*5 + 4*7
            assertEquals(50L, product.get(1, 1)); // 3*6 + 4*8
        }

        @Test
        public void testMultiply_incompatibleDimensions() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        @Test
        public void testMultiply_rectangularMatrices() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } }); // 1x3
            LongMatrix m2 = LongMatrix.of(new long[][] { { 4L }, { 5L }, { 6L } }); // 3x1
            LongMatrix product = m1.multiply(m2);

            assertEquals(1, product.rowCount());
            assertEquals(1, product.columnCount());
            assertEquals(32L, product.get(0, 0)); // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
        }

        @Test
        public void testMultiply_withLargeValues() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1000000L, 2000000L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3000000L }, { 4000000L } });
            LongMatrix product = m1.multiply(m2);
            assertEquals(11000000000000L, product.get(0, 0)); // 1000000*3000000 + 2000000*4000000
        }

        // ============ Conversion Tests ============

        @Test
        public void testBoxed() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            Matrix<Long> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(3, boxed.columnCount());
            assertEquals(Long.valueOf(1L), boxed.get(0, 0));
            assertEquals(Long.valueOf(6L), boxed.get(1, 2));
        }

        @Test
        public void testToFloatMatrix() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            FloatMatrix floatMatrix = m.toFloatMatrix();
            assertEquals(2, floatMatrix.rowCount());
            assertEquals(2, floatMatrix.columnCount());
            assertEquals(1.0f, floatMatrix.get(0, 0), 0.0001f);
            assertEquals(4.0f, floatMatrix.get(1, 1), 0.0001f);
        }

        @Test
        public void testToDoubleMatrix() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            DoubleMatrix doubleMatrix = m.toDoubleMatrix();
            assertEquals(2, doubleMatrix.rowCount());
            assertEquals(2, doubleMatrix.columnCount());
            assertEquals(1.0, doubleMatrix.get(0, 0), 0.0001);
            assertEquals(4.0, doubleMatrix.get(1, 1), 0.0001);
        }

        @Test
        public void testBoxed_verticalMatrix() {
            // Test the rows > columnCount branch (3 rows, 2 columnCount)
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            Matrix<Long> boxed = m.boxed();
            assertEquals(3, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(Long.valueOf(1L), boxed.get(0, 0));
            assertEquals(Long.valueOf(4L), boxed.get(1, 1));
            assertEquals(Long.valueOf(6L), boxed.get(2, 1));
        }

        @Test
        public void testBoxed_extremeValues() {
            LongMatrix m = LongMatrix.of(new long[][] { { Long.MAX_VALUE, Long.MIN_VALUE }, { 0L, -1L } });
            Matrix<Long> boxed = m.boxed();
            assertEquals(Long.valueOf(Long.MAX_VALUE), boxed.get(0, 0));
            assertEquals(Long.valueOf(Long.MIN_VALUE), boxed.get(0, 1));
            assertEquals(Long.valueOf(0L), boxed.get(1, 0));
            assertEquals(Long.valueOf(-1L), boxed.get(1, 1));
        }

        @Test
        public void testToFloatMatrix_verticalMatrix() {
            // Test the rows > columnCount branch (4 rows, 2 columnCount)
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L }, { 7L, 8L } });
            FloatMatrix floatMatrix = m.toFloatMatrix();
            assertEquals(4, floatMatrix.rowCount());
            assertEquals(2, floatMatrix.columnCount());
            assertEquals(1.0f, floatMatrix.get(0, 0), 0.0001f);
            assertEquals(8.0f, floatMatrix.get(3, 1), 0.0001f);
        }

        @Test
        public void testToFloatMatrix_extremeValues() {
            // Test conversion with extreme long values - may lose precision
            LongMatrix m = LongMatrix.of(new long[][] { { Long.MAX_VALUE, Long.MIN_VALUE }, { 1000000000000L, -1000000000000L } });
            FloatMatrix floatMatrix = m.toFloatMatrix();
            // Verify conversion happened (exact values may lose precision)
            assertNotNull(floatMatrix);
            assertEquals(2, floatMatrix.rowCount());
            assertEquals(2, floatMatrix.columnCount());
        }

        @Test
        public void testToDoubleMatrix_verticalMatrix() {
            // Test the rows > columnCount branch via create() (3 rows, 2 columnCount)
            LongMatrix m = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L }, { 50L, 60L } });
            DoubleMatrix doubleMatrix = m.toDoubleMatrix();
            assertEquals(3, doubleMatrix.rowCount());
            assertEquals(2, doubleMatrix.columnCount());
            assertEquals(10.0, doubleMatrix.get(0, 0), 0.0001);
            assertEquals(60.0, doubleMatrix.get(2, 1), 0.0001);
        }

        @Test
        public void testToDoubleMatrix_extremeValues() {
            // Test with extreme long values - check precision
            LongMatrix m = LongMatrix.of(new long[][] { { Long.MAX_VALUE, Long.MIN_VALUE }, { 9007199254740992L, -9007199254740992L } });
            DoubleMatrix doubleMatrix = m.toDoubleMatrix();
            // Double has 53 bits of precision, so very large longs may lose precision
            assertNotNull(doubleMatrix);
            assertEquals(2, doubleMatrix.rowCount());
            assertEquals(2, doubleMatrix.columnCount());
            // Verify the values are present (may not be exact due to precision)
            assertTrue(doubleMatrix.get(0, 0) > 0);
            assertTrue(doubleMatrix.get(0, 1) < 0);
        }

        @Test
        public void testUnbox_verticalMatrix() {
            // Test unbox with rows > columnCount (3 rows, 2 columnCount)
            Matrix<Long> boxedMatrix = Matrix.of(new Long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            LongMatrix m = LongMatrix.unbox(boxedMatrix);
            assertEquals(3, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(6L, m.get(2, 1));
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix result = m1.zipWith(m2, (a, b) -> a * b);

            assertEquals(5L, result.get(0, 0)); // 1*5
            assertEquals(12L, result.get(0, 1)); // 2*6
            assertEquals(21L, result.get(1, 0)); // 3*7
            assertEquals(32L, result.get(1, 1)); // 4*8
        }

        @Test
        public void testZipWith_differentShapes() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a + b));
        }

        @Test
        public void testZipWith_threeMatrices() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 9L, 10L }, { 11L, 12L } });
            LongMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);

            assertEquals(15L, result.get(0, 0)); // 1+5+9
            assertEquals(18L, result.get(0, 1)); // 2+6+10
            assertEquals(21L, result.get(1, 0)); // 3+7+11
            assertEquals(24L, result.get(1, 1)); // 4+8+12
        }

        @Test
        public void testZipWith_threeMatrices_differentShapes() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 9L, 10L, 11L } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> a + b + c));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            long[] diagonal = m.streamMainDiagonal().toArray();
            assertArrayEquals(new long[] { 1L, 5L, 9L }, diagonal);
        }

        @Test
        public void testStreamLU2RD_empty() {
            LongMatrix empty = LongMatrix.empty();
            assertEquals(0, empty.streamMainDiagonal().toArray().length);
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            LongMatrix nonSquare = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
        }

        @Test
        public void testStreamRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            long[] antiDiagonal = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new long[] { 3L, 5L, 7L }, antiDiagonal);
        }

        @Test
        public void testStreamRU2LD_empty() {
            LongMatrix empty = LongMatrix.empty();
            assertEquals(0, empty.streamAntiDiagonal().toArray().length);
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            LongMatrix nonSquare = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamAntiDiagonal());
        }

        @Test
        public void testStreamH() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] all = m.streamHorizontal().toArray();
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, all);
        }

        @Test
        public void testStreamH_empty() {
            LongMatrix empty = LongMatrix.empty();
            assertEquals(0, empty.streamHorizontal().toArray().length);
        }

        @Test
        public void testStreamH_withRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] row1 = m.streamHorizontal(1).toArray();
            assertArrayEquals(new long[] { 4L, 5L, 6L }, row1);
        }

        @Test
        public void testStreamH_withRow_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2));
        }

        @Test
        public void testStreamH_withRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            long[] rows = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new long[] { 4L, 5L, 6L, 7L, 8L, 9L }, rows);
        }

        @Test
        public void testStreamH_withRange_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2, 1));
        }

        @Test
        public void testStreamV() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] all = m.streamVertical().toArray();
            assertArrayEquals(new long[] { 1L, 4L, 2L, 5L, 3L, 6L }, all);
        }

        @Test
        public void testStreamV_empty() {
            LongMatrix empty = LongMatrix.empty();
            assertEquals(0, empty.streamVertical().toArray().length);
        }

        @Test
        public void testStreamV_withColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] col1 = m.streamVertical(1).toArray();
            assertArrayEquals(new long[] { 2L, 5L }, col1);
        }

        @Test
        public void testStreamV_withColumn_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2));
        }

        @Test
        public void testStreamV_withRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            long[] columnCount = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new long[] { 2L, 5L, 8L, 3L, 6L, 9L }, columnCount);
        }

        @Test
        public void testStreamV_withRange_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2, 1));
        }

        @Test
        public void testStreamR() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            List<long[]> rows = m.streamRows().map(LongStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new long[] { 1L, 2L, 3L }, rows.get(0));
            assertArrayEquals(new long[] { 4L, 5L, 6L }, rows.get(1));
        }

        @Test
        public void testStreamR_empty() {
            LongMatrix empty = LongMatrix.empty();
            assertEquals(0, empty.streamRows().count());
        }

        @Test
        public void testStreamR_withRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            List<long[]> rows = m.streamRows(1, 3).map(LongStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new long[] { 4L, 5L, 6L }, rows.get(0));
            assertArrayEquals(new long[] { 7L, 8L, 9L }, rows.get(1));
        }

        @Test
        public void testStreamR_withRange_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(0, 3));
        }

        @Test
        public void testStreamC() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            List<long[]> columnCount = m.streamColumns().map(LongStream::toArray).toList();
            assertEquals(3, columnCount.size());
            assertArrayEquals(new long[] { 1L, 4L }, columnCount.get(0));
            assertArrayEquals(new long[] { 2L, 5L }, columnCount.get(1));
            assertArrayEquals(new long[] { 3L, 6L }, columnCount.get(2));
        }

        @Test
        public void testStreamC_empty() {
            LongMatrix empty = LongMatrix.empty();
            assertEquals(0, empty.streamColumns().count());
        }

        @Test
        public void testStreamC_withRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            List<long[]> columnCount = m.streamColumns(1, 3).map(LongStream::toArray).toList();
            assertEquals(2, columnCount.size());
            assertArrayEquals(new long[] { 2L, 5L, 8L }, columnCount.get(0));
            assertArrayEquals(new long[] { 3L, 6L, 9L }, columnCount.get(1));
        }

        @Test
        public void testStreamC_withRange_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(0, 3));
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            List<Long> values = new ArrayList<>();
            m.forEach(v -> values.add(v));
            assertEquals(9, values.size());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1L, values.get(i).longValue());
            }
        }

        @Test
        public void testForEach_withBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            List<Long> values = new ArrayList<>();
            m.forEach(1, 3, 1, 3, v -> values.add(v));
            assertEquals(4, values.size());
            assertEquals(5L, values.get(0).longValue());
            assertEquals(6L, values.get(1).longValue());
            assertEquals(8L, values.get(2).longValue());
            assertEquals(9L, values.get(3).longValue());
        }

        @Test
        public void testForEach_withBounds_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(-1, 2, 0, 2, v -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 3, 0, 2, v -> {
            }));
        }

        // ============ Object Methods Tests ============

        @Test
        public void testPrintln() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertFalse(m.isEmpty());
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(m::println);

            LongMatrix empty = LongMatrix.empty();
            assertTrue(empty.isEmpty());
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(empty::println);
        }

        @Test
        public void testHashCode() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 1L, 2L }, { 4L, 3L } });

            assertEquals(m1.hashCode(), m2.hashCode());
            assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
        }

        @Test
        public void testEquals() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 1L, 2L }, { 4L, 3L } });
            LongMatrix m4 = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });

            assertTrue(m1.equals(m1)); // Same object
            assertTrue(m1.equals(m2)); // Same values
            assertFalse(m1.equals(m3)); // Different values
            assertFalse(m1.equals(m4)); // Different dimensions
            assertFalse(m1.equals(null));
            assertFalse(m1.equals("not a matrix"));
        }

        @Test
        public void testToString() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("1"));
            assertTrue(str.contains("2"));
            assertTrue(str.contains("3"));
            assertTrue(str.contains("4"));
        }

        // ============ Statistical Operations Tests ============

        @Test
        public void testStatisticalOperations() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });

            // Test sum
            long totalSum = m.streamHorizontal().sum();
            assertEquals(45L, totalSum); // 1+2+3+4+5+6+7+8+9 = 45

            // Test sum of specific row
            long row1Sum = m.streamHorizontal(1).sum();
            assertEquals(15L, row1Sum); // 4+5+6 = 15

            // Test sum of specific column
            long col0Sum = m.streamVertical(0).sum();
            assertEquals(12L, col0Sum); // 1+4+7 = 12

            // Test min/max
            long min = m.streamHorizontal().min().orElse(0L);
            assertEquals(1L, min);

            long max = m.streamHorizontal().max().orElse(0L);
            assertEquals(9L, max);

            // Test average
            double avg = m.streamHorizontal().average().orElse(0.0);
            assertEquals(5.0, avg, 0.0001);

            // Test diagonal operations
            long diagonalSum = m.streamMainDiagonal().sum();
            assertEquals(15L, diagonalSum); // 1+5+9 = 15

            long antiDiagonalSum = m.streamAntiDiagonal().sum();
            assertEquals(15L, antiDiagonalSum); // 3+5+7 = 15
        }

        @Test
        public void testRowColumnStatistics() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });

            // Test statistics on individual rows
            List<Long> rowSums = m.streamRows().map(row -> row.sum()).toList();
            assertEquals(3, rowSums.size());
            assertEquals(6L, rowSums.get(0).longValue()); // 1+2+3
            assertEquals(15L, rowSums.get(1).longValue()); // 4+5+6
            assertEquals(24L, rowSums.get(2).longValue()); // 7+8+9

            // Test statistics on individual columns
            List<Long> colSums = m.streamColumns().map(col -> col.sum()).toList();
            assertEquals(3, colSums.size());
            assertEquals(12L, colSums.get(0).longValue()); // 1+4+7
            assertEquals(15L, colSums.get(1).longValue()); // 2+5+8
            assertEquals(18L, colSums.get(2).longValue()); // 3+6+9
        }

        // ============ Edge Case Tests ============

        @Test
        public void testEmptyMatrixOperations() {
            LongMatrix empty = LongMatrix.empty();

            assertTrue(empty.flatten().isEmpty());
            assertEquals(0, empty.copy().rowCount);
            assertEquals(empty, empty.transpose());
            assertEquals(empty, empty.rotate90());

            LongMatrix extended = empty.resize(2, 2, 5L);
            assertEquals(2, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals(5L, extended.get(0, 0));
        }

        @Test
        public void testArithmeticEdgeCases() {
            // Test with zero matrix
            LongMatrix zeros = LongMatrix.of(new long[][] { { 0L, 0L }, { 0L, 0L } });
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

            LongMatrix addZero = m.add(zeros);
            assertEquals(m, addZero);

            LongMatrix subtractZero = m.subtract(zeros);
            assertEquals(m, subtractZero);

            // Test multiplication with zero matrix
            LongMatrix multiplyZero = m.multiply(zeros);
            assertEquals(zeros, multiplyZero);

            // Test addition commutativity
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            assertEquals(m1.add(m2), m2.add(m1));

            // Test subtraction anti-commutativity
            LongMatrix diff1 = m1.subtract(m2);
            LongMatrix diff2 = m2.subtract(m1);
            assertEquals(diff1.get(0, 0), -diff2.get(0, 0));
            assertEquals(diff1.get(1, 1), -diff2.get(1, 1));
        }

        @Test
        public void testLargeMatrixArithmetic() {
            // Test with larger matrices
            long[][] arr1 = new long[10][10];
            long[][] arr2 = new long[10][10];

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    arr1[i][j] = i * 10 + j + 1;
                    arr2[i][j] = (i * 10 + j + 1) * 2;
                }
            }

            LongMatrix large1 = LongMatrix.of(arr1);
            LongMatrix large2 = LongMatrix.of(arr2);

            // Test addition
            LongMatrix largeSum = large1.add(large2);
            assertEquals(3L, largeSum.get(0, 0)); // 1 + 2 = 3
            assertEquals(300L, largeSum.get(9, 9)); // 100 + 200 = 300

            // Test sum of all elements
            long totalSum = largeSum.streamHorizontal().sum();
            assertEquals(15150L, totalSum); // 3*(1+2+...+100) = 3*5050 = 15150
        }

        @Test
        public void testScalarOperationsWithMap() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

            // Scalar addition
            LongMatrix addScalar = m.map(x -> x + 10L);
            assertEquals(11L, addScalar.get(0, 0));
            assertEquals(14L, addScalar.get(1, 1));

            // Scalar multiplication
            LongMatrix multiplyScalar = m.map(x -> x * 3L);
            assertEquals(3L, multiplyScalar.get(0, 0));
            assertEquals(12L, multiplyScalar.get(1, 1));

            // Scalar division
            LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
            LongMatrix divideScalar = m2.map(x -> x / 10L);
            assertEquals(1L, divideScalar.get(0, 0));
            assertEquals(4L, divideScalar.get(1, 1));
        }

        @Test
        public void testElementWiseMultiplyWithZipWith() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 2L, 3L }, { 4L, 5L } });

            // Element-wise multiplication
            LongMatrix elementWiseProduct = m1.zipWith(m2, (a, b) -> a * b);
            assertEquals(2L, elementWiseProduct.get(0, 0)); // 1*2
            assertEquals(6L, elementWiseProduct.get(0, 1)); // 2*3
            assertEquals(12L, elementWiseProduct.get(1, 0)); // 3*4
            assertEquals(20L, elementWiseProduct.get(1, 1)); // 4*5

            // Element-wise division
            LongMatrix elementWiseDivision = m2.zipWith(m1, (a, b) -> a / b);
            assertEquals(2L, elementWiseDivision.get(0, 0)); // 2/1
            assertEquals(1L, elementWiseDivision.get(0, 1)); // 3/2 (integer division)
            assertEquals(1L, elementWiseDivision.get(1, 0)); // 4/3 (integer division)
            assertEquals(1L, elementWiseDivision.get(1, 1)); // 5/4 (integer division)
        }

        // ============ Long Overflow Tests ============

        @Test
        public void testLongOverflow() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { Long.MAX_VALUE, 1L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, Long.MAX_VALUE } });

            // Addition overflow
            LongMatrix sum = m1.add(m2);
            // Overflow wraps around to negative
            assertTrue(sum.get(0, 0) < 0);

            // Test max and min values
            LongMatrix extremes = LongMatrix.of(new long[][] { { Long.MAX_VALUE, Long.MIN_VALUE } });
            assertEquals(Long.MAX_VALUE, extremes.get(0, 0));
            assertEquals(Long.MIN_VALUE, extremes.get(0, 1));
        }

        @Test
        public void testNegativeValues() {
            LongMatrix m = LongMatrix.of(new long[][] { { -1L, -2L }, { -3L, -4L } });

            assertEquals(-1L, m.get(0, 0));
            assertEquals(-4L, m.get(1, 1));

            // Test operations with negative values
            LongMatrix doubled = m.map(x -> x * 2L);
            assertEquals(-2L, doubled.get(0, 0));
            assertEquals(-8L, doubled.get(1, 1));
        }

        @Test
        public void testVeryLargeValues() {
            long largeValue = 1_000_000_000_000L; // 1 trillion
            LongMatrix m = LongMatrix.of(new long[][] { { largeValue, largeValue * 2 }, { largeValue * 3, largeValue * 4 } });

            assertEquals(largeValue, m.get(0, 0));
            assertEquals(largeValue * 2, m.get(0, 1));
            assertEquals(largeValue * 3, m.get(1, 0));
            assertEquals(largeValue * 4, m.get(1, 1));

            // Test operations with large values
            LongMatrix doubled = m.map(x -> x * 2L);
            assertEquals(largeValue * 2, doubled.get(0, 0));
            assertEquals(largeValue * 8, doubled.get(1, 1));
        }

        @Test
        public void testConversionFromIntWithMaxMin() {
            int[][] ints = { { Integer.MAX_VALUE, Integer.MIN_VALUE }, { 0, -1 } };
            LongMatrix m = LongMatrix.from(ints);

            // Verify proper conversion
            assertEquals(Integer.MAX_VALUE, m.get(0, 0));
            assertEquals(Integer.MIN_VALUE, m.get(0, 1));
            assertEquals(0L, m.get(1, 0));
            assertEquals(-1L, m.get(1, 1));

            // Verify that long values are different from int values (no truncation)
            assertTrue(m.get(0, 0) < Long.MAX_VALUE);
            assertTrue(m.get(0, 1) > Long.MIN_VALUE);
        }

        @Test
        public void testLargeValueArithmetic() {
            // Test addition with large values
            long billion = 1_000_000_000L;
            LongMatrix m1 = LongMatrix.of(new long[][] { { billion * 100, billion * 200 } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { billion * 300, billion * 400 } });

            LongMatrix sum = m1.add(m2);
            assertEquals(billion * 400, sum.get(0, 0));
            assertEquals(billion * 600, sum.get(0, 1));

            // Test subtraction with large values
            LongMatrix diff = m2.subtract(m1);
            assertEquals(billion * 200, diff.get(0, 0));
            assertEquals(billion * 200, diff.get(0, 1));
        }

        @Test
        public void testRangeWithLargeStep() {
            LongMatrix m = LongMatrix.range(0L, 10000000000L, 2000000000L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 2000000000L, 4000000000L, 6000000000L, 8000000000L }, m.rowView(0));
        }
    }

    @Nested
    @Tag("2510")
    class LongMatrix2510Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            long[][] arr = { { 1L, 2L }, { 3L, 4L } };
            LongMatrix m = new LongMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(4L, m.get(1, 1));
        }

        @Test
        public void testConstructor_withNullArray() {
            LongMatrix m = new LongMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withEmptyArray() {
            LongMatrix m = new LongMatrix(new long[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withSingleElement() {
            LongMatrix m = new LongMatrix(new long[][] { { 42L } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42L, m.get(0, 0));
        }

        // ============ Factory Method Tests ============

        @Test
        public void testEmpty() {
            LongMatrix empty = LongMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());

            // Test singleton
            assertSame(LongMatrix.empty(), LongMatrix.empty());
        }

        @Test
        public void testOf_withValidArray() {
            long[][] arr = { { 1L, 2L }, { 3L, 4L } };
            LongMatrix m = LongMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void testOf_withNullArray() {
            LongMatrix m = LongMatrix.of((long[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withEmptyArray() {
            LongMatrix m = LongMatrix.of(new long[0][0]);
            assertTrue(m.isEmpty());
        }

        // ============ Create Method Tests ============

        @Test
        public void testCreate_fromIntArray() {
            int[][] ints = { { 1, 2 }, { 3, 4 } };
            LongMatrix m = LongMatrix.from(ints);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(4L, m.get(1, 1));
        }

        @Test
        public void testCreate_fromIntArray_empty() {
            LongMatrix m = LongMatrix.from(new int[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreate_fromIntArray_null() {
            LongMatrix m = LongMatrix.from((int[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreate_fromIntArray_nullFirstRow() {
            int[][] ints = { null, { 1, 2 } };
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(ints));
        }

        @Test
        public void testCreate_fromIntArray_differentRowLengths() {
            int[][] ints = { { 1, 2 }, { 3 } };
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(ints));
        }

        @Test
        public void testRandom() {
            LongMatrix m = LongMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            // Just verify elements exist (values are random)
            for (int i = 0; i < 5; i++) {
                assertNotNull(m.get(0, i));
            }
        }

        @Test
        public void testRepeat() {
            LongMatrix m = LongMatrix.repeat(1, 5, 42L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(42L, m.get(0, i));
            }
        }

        @Test
        public void testRange() {
            LongMatrix m = LongMatrix.range(0L, 5L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.rowView(0));
        }

        @Test
        public void testRange_withStep() {
            LongMatrix m = LongMatrix.range(0L, 10L, 2L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L }, m.rowView(0));
        }

        @Test
        public void testRange_withNegativeStep() {
            LongMatrix m = LongMatrix.range(10L, 0L, -2L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 10L, 8L, 6L, 4L, 2L }, m.rowView(0));
        }

        @Test
        public void testRangeClosed() {
            LongMatrix m = LongMatrix.rangeClosed(0L, 4L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.rowView(0));
        }

        @Test
        public void testRangeClosed_withStep() {
            LongMatrix m = LongMatrix.rangeClosed(0L, 10L, 2L);
            assertEquals(1, m.rowCount());
            assertEquals(6, m.columnCount());
            assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L, 10L }, m.rowView(0));
        }

        @Test
        public void testDiagonalLU2RD() {
            LongMatrix m = LongMatrix.mainDiagonal(new long[] { 1L, 2L, 3L });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 2));
            assertEquals(0L, m.get(0, 1));
            assertEquals(0L, m.get(1, 0));
        }

        @Test
        public void testDiagonalRU2LD() {
            LongMatrix m = LongMatrix.antiDiagonal(new long[] { 1L, 2L, 3L });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 2));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 0));
            assertEquals(0L, m.get(0, 0));
            assertEquals(0L, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            LongMatrix m = LongMatrix.diagonals(new long[] { 1L, 2L, 3L }, new long[] { 4L, 5L, 6L });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 2));
            assertEquals(4L, m.get(0, 2));
            assertEquals(6L, m.get(2, 0));
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            LongMatrix m = LongMatrix.diagonals(new long[] { 1L, 2L, 3L }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            LongMatrix m = LongMatrix.diagonals(null, new long[] { 4L, 5L, 6L });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(4L, m.get(0, 2));
            assertEquals(5L, m.get(1, 1));
            assertEquals(6L, m.get(2, 0));
        }

        @Test
        public void testDiagonal_withBothNull() {
            LongMatrix m = LongMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.diagonals(new long[] { 1L, 2L }, new long[] { 3L, 4L, 5L }));
        }

        @Test
        public void testUnbox() {
            Long[][] boxed = { { 1L, 2L }, { 3L, 4L } };
            Matrix<Long> boxedMatrix = Matrix.of(boxed);
            LongMatrix unboxed = LongMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertEquals(1L, unboxed.get(0, 0));
            assertEquals(4L, unboxed.get(1, 1));
        }

        @Test
        public void testUnbox_withNullValues() {
            Long[][] boxed = { { 1L, null }, { null, 4L } };
            Matrix<Long> boxedMatrix = Matrix.of(boxed);
            LongMatrix unboxed = LongMatrix.unbox(boxedMatrix);
            assertEquals(1L, unboxed.get(0, 0));
            assertEquals(0L, unboxed.get(0, 1)); // null -> 0
            assertEquals(0L, unboxed.get(1, 0)); // null -> 0
            assertEquals(4L, unboxed.get(1, 1));
        }

        // ============ Component Type Tests ============

        @Test
        public void testComponentType() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L } });
            assertEquals(long.class, m.componentType());
        }

        // ============ Get/Set Tests ============

        @Test
        public void testGet() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            assertEquals(1L, m.get(0, 0));
            assertEquals(5L, m.get(1, 1));
            assertEquals(6L, m.get(1, 2));
        }

        @Test
        public void testGet_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
        }

        @Test
        public void testGetWithPoint() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertEquals(1L, m.get(Point.of(0, 0)));
            assertEquals(4L, m.get(Point.of(1, 1)));
            assertEquals(2L, m.get(Point.of(0, 1)));
        }

        @Test
        public void testSet() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.set(0, 0, 10L);
            assertEquals(10L, m.get(0, 0));

            m.set(1, 1, 20L);
            assertEquals(20L, m.get(1, 1));
        }

        @Test
        public void testSet_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, 0L));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, 0L));
        }

        @Test
        public void testSetWithPoint() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.set(Point.of(0, 0), 50L);
            assertEquals(50L, m.get(Point.of(0, 0)));
        }

        // ============ Adjacent Element Tests ============

        @Test
        public void testUpOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

            OptionalLong up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1L, up.get());

            // Top row has no element above
            OptionalLong empty = m.above(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testDownOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

            OptionalLong down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3L, down.get());

            // Bottom row has no element below
            OptionalLong empty = m.below(1, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testLeftOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

            OptionalLong left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1L, left.get());

            // Leftmost column has no element to the left
            OptionalLong empty = m.left(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testRightOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

            OptionalLong right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2L, right.get());

            // Rightmost column has no element to the right
            OptionalLong empty = m.right(0, 1);
            assertFalse(empty.isPresent());
        }

        // ============ Row/Column Operations Tests ============

        @Test
        public void testRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            assertArrayEquals(new long[] { 1L, 2L, 3L }, m.rowView(0));
            assertArrayEquals(new long[] { 4L, 5L, 6L }, m.rowView(1));
        }

        @Test
        public void testRow_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(2));
        }

        @Test
        public void testColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            assertArrayEquals(new long[] { 1L, 4L }, m.columnCopy(0));
            assertArrayEquals(new long[] { 2L, 5L }, m.columnCopy(1));
            assertArrayEquals(new long[] { 3L, 6L }, m.columnCopy(2));
        }

        @Test
        public void testColumn_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.setRow(0, new long[] { 10L, 20L });
            assertArrayEquals(new long[] { 10L, 20L }, m.rowView(0));
            assertArrayEquals(new long[] { 3L, 4L }, m.rowView(1)); // unchanged
        }

        @Test
        public void testSetRow_wrongSize() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new long[] { 1L }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new long[] { 1L, 2L, 3L }));
        }

        @Test
        public void testSetColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.setColumn(0, new long[] { 10L, 20L });
            assertArrayEquals(new long[] { 10L, 20L }, m.columnCopy(0));
            assertArrayEquals(new long[] { 2L, 4L }, m.columnCopy(1)); // unchanged
        }

        @Test
        public void testSetColumn_wrongSize() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new long[] { 1L }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new long[] { 1L, 2L, 3L }));
        }

        @Test
        public void testUpdateRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateRow(0, x -> x * 2L);
            assertArrayEquals(new long[] { 2L, 4L }, m.rowView(0));
            assertArrayEquals(new long[] { 3L, 4L }, m.rowView(1)); // unchanged
        }

        @Test
        public void testUpdateColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateColumn(0, x -> x + 10L);
            assertArrayEquals(new long[] { 11L, 13L }, m.columnCopy(0));
            assertArrayEquals(new long[] { 2L, 4L }, m.columnCopy(1)); // unchanged
        }

        // ============ Diagonal Operations Tests ============

        @Test
        public void testGetLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            assertArrayEquals(new long[] { 1L, 5L, 9L }, m.getMainDiagonal());
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.setMainDiagonal(new long[] { 10L, 20L, 30L });
            assertEquals(10L, m.get(0, 0));
            assertEquals(20L, m.get(1, 1));
            assertEquals(30L, m.get(2, 2));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new long[] { 1L }));
        }

        @Test
        public void testSetLU2RD_arrayTooShort() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new long[] { 1L, 2L }));
        }

        @Test
        public void testUpdateLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.updateMainDiagonal(x -> x * 10L);
            assertEquals(10L, m.get(0, 0));
            assertEquals(50L, m.get(1, 1));
            assertEquals(90L, m.get(2, 2));
            assertEquals(2L, m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateLU2RD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.updateMainDiagonal(x -> x * 2L));
        }

        @Test
        public void testGetRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            assertArrayEquals(new long[] { 3L, 5L, 7L }, m.getAntiDiagonal());
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.setAntiDiagonal(new long[] { 10L, 20L, 30L });
            assertEquals(10L, m.get(0, 2));
            assertEquals(20L, m.get(1, 1));
            assertEquals(30L, m.get(2, 0));
        }

        @Test
        public void testSetRU2LD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.setAntiDiagonal(new long[] { 1L }));
        }

        @Test
        public void testSetRU2LD_arrayTooShort() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new long[] { 1L, 2L }));
        }

        @Test
        public void testUpdateRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.updateAntiDiagonal(x -> x * 10L);
            assertEquals(30L, m.get(0, 2));
            assertEquals(50L, m.get(1, 1));
            assertEquals(70L, m.get(2, 0));
            assertEquals(2L, m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateRU2LD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(x -> x * 2L));
        }

        // ============ Transformation Tests ============

        @Test
        public void testUpdateAll() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateAll(x -> x * 2L);
            assertEquals(2L, m.get(0, 0));
            assertEquals(4L, m.get(0, 1));
            assertEquals(6L, m.get(1, 0));
            assertEquals(8L, m.get(1, 1));
        }

        @Test
        public void testUpdateAll_withIndices() {
            LongMatrix m = LongMatrix.of(new long[][] { { 0L, 0L }, { 0L, 0L } });
            m.updateAll((i, j) -> (long) (i * 10 + j));
            assertEquals(0L, m.get(0, 0));
            assertEquals(1L, m.get(0, 1));
            assertEquals(10L, m.get(1, 0));
            assertEquals(11L, m.get(1, 1));
        }

        @Test
        public void testReplaceIf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            m.replaceIf(x -> x > 3L, 0L);
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(3L, m.get(0, 2));
            assertEquals(0L, m.get(1, 0)); // was 4
            assertEquals(0L, m.get(1, 1)); // was 5
            assertEquals(0L, m.get(1, 2)); // was 6
        }

        @Test
        public void testReplaceIf_withIndices() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.replaceIf((i, j) -> i == j, 0L); // Replace diagonal
            assertEquals(0L, m.get(0, 0));
            assertEquals(0L, m.get(1, 1));
            assertEquals(0L, m.get(2, 2));
            assertEquals(2L, m.get(0, 1)); // unchanged
        }

        @Test
        public void testMap() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix result = m.map(x -> x * 2L);
            assertEquals(2L, result.get(0, 0));
            assertEquals(4L, result.get(0, 1));
            assertEquals(6L, result.get(1, 0));
            assertEquals(8L, result.get(1, 1));

            // Original unchanged
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void testMapToObj() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            Matrix<String> result = m.mapToObj(x -> "val:" + x, String.class);
            assertEquals("val:1", result.get(0, 0));
            assertEquals("val:4", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_withValue() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.fill(99L);
            for (int i = 0; i < m.rowCount(); i++) {
                for (int j = 0; j < m.columnCount(); j++) {
                    assertEquals(99L, m.get(i, j));
                }
            }
        }

        @Test
        public void testFill_withArray() {
            LongMatrix m = LongMatrix.of(new long[][] { { 0L, 0L, 0L }, { 0L, 0L, 0L }, { 0L, 0L, 0L } });
            long[][] patch = { { 1L, 2L }, { 3L, 4L } };
            m.copyFrom(patch);
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(3L, m.get(1, 0));
            assertEquals(4L, m.get(1, 1));
            assertEquals(0L, m.get(2, 2)); // unchanged
        }

        @Test
        public void testFill_withArrayAtPosition() {
            LongMatrix m = LongMatrix.of(new long[][] { { 0L, 0L, 0L }, { 0L, 0L, 0L }, { 0L, 0L, 0L } });
            long[][] patch = { { 1L, 2L }, { 3L, 4L } };
            m.copyFrom(1, 1, patch);
            assertEquals(0L, m.get(0, 0)); // unchanged
            assertEquals(1L, m.get(1, 1));
            assertEquals(2L, m.get(1, 2));
            assertEquals(3L, m.get(2, 1));
            assertEquals(4L, m.get(2, 2));
        }

        @Test
        public void testFill_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long[][] patch = { { 1L, 2L }, { 3L, 4L } };
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals(1L, copy.get(0, 0));

            // Modify copy shouldn't affect original
            copy.set(0, 0, 99L);
            assertEquals(1L, m.get(0, 0));
            assertEquals(99L, copy.get(0, 0));
        }

        @Test
        public void testCopy_withRowRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix subset = m.copy(1, 3);
            assertEquals(2, subset.rowCount());
            assertEquals(3, subset.columnCount());
            assertEquals(4L, subset.get(0, 0));
            assertEquals(9L, subset.get(1, 2));
        }

        @Test
        public void testCopy_withRowRange_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
        }

        @Test
        public void testCopy_withFullRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix submatrix = m.copy(0, 2, 1, 3);
            assertEquals(2, submatrix.rowCount());
            assertEquals(2, submatrix.columnCount());
            assertEquals(2L, submatrix.get(0, 0));
            assertEquals(6L, submatrix.get(1, 1));
        }

        @Test
        public void testCopy_withFullRange_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_larger() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix extended = m.resize(4, 4);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1L, extended.get(0, 0));
            assertEquals(4L, extended.get(1, 1));
            assertEquals(0L, extended.get(3, 3)); // new cells are 0
        }

        @Test
        public void testExtend_smaller() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix truncated = m.resize(2, 2);
            assertEquals(2, truncated.rowCount());
            assertEquals(2, truncated.columnCount());
            assertEquals(1L, truncated.get(0, 0));
            assertEquals(5L, truncated.get(1, 1));
        }

        @Test
        public void testExtend_withDefaultValue() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix extended = m.resize(3, 3, -1L);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1L, extended.get(0, 0));
            assertEquals(-1L, extended.get(2, 2)); // new cell
        }

        @Test
        public void testExtend_withNegativeDimensions() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 3, 0L));
            assertThrows(IllegalArgumentException.class, () -> m.resize(3, -1, 0L));
        }

        @Test
        public void testExtend_directional() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix extended = m.extend(1, 1, 2, 2);
            assertEquals(5, extended.rowCount()); // 1 + 3 + 1
            assertEquals(7, extended.columnCount()); // 2 + 3 + 2

            // Original values at offset position
            assertEquals(1L, extended.get(1, 2));
            assertEquals(5L, extended.get(2, 3));

            // New cells are 0
            assertEquals(0L, extended.get(0, 0));
        }

        @Test
        public void testExtend_directionalWithDefault() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix extended = m.extend(1, 1, 1, 1, -1L);
            assertEquals(5, extended.rowCount());
            assertEquals(5, extended.columnCount());

            // Check original values
            assertEquals(1L, extended.get(1, 1));

            // Check new values
            assertEquals(-1L, extended.get(0, 0));
            assertEquals(-1L, extended.get(4, 4));
        }

        @Test
        public void testExtend_directionalWithNegativeValues() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, 0L));
        }

        // ============ Reverse/Flip Tests ============

        @Test
        public void testReverseH() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            m.flipInPlaceHorizontally();
            assertEquals(3L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(1L, m.get(0, 2));
            assertEquals(6L, m.get(1, 0));
        }

        @Test
        public void testReverseV() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            m.flipInPlaceVertically();
            assertEquals(5L, m.get(0, 0));
            assertEquals(6L, m.get(0, 1));
            assertEquals(3L, m.get(1, 0));
            assertEquals(1L, m.get(2, 0));
        }

        @Test
        public void testFlipH() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix flipped = m.flipHorizontally();
            assertEquals(3L, flipped.get(0, 0));
            assertEquals(2L, flipped.get(0, 1));
            assertEquals(1L, flipped.get(0, 2));

            // Original unchanged
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            LongMatrix flipped = m.flipVertically();
            assertEquals(5L, flipped.get(0, 0));
            assertEquals(3L, flipped.get(1, 0));
            assertEquals(1L, flipped.get(2, 0));

            // Original unchanged
            assertEquals(1L, m.get(0, 0));
        }

        // ============ Rotation Tests ============

        @Test
        public void testRotate90() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3L, rotated.get(0, 0));
            assertEquals(1L, rotated.get(0, 1));
            assertEquals(4L, rotated.get(1, 0));
            assertEquals(2L, rotated.get(1, 1));
        }

        @Test
        public void testRotate180() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix rotated = m.rotate180();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4L, rotated.get(0, 0));
            assertEquals(3L, rotated.get(0, 1));
            assertEquals(2L, rotated.get(1, 0));
            assertEquals(1L, rotated.get(1, 1));
        }

        @Test
        public void testRotate270() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(2L, rotated.get(0, 0));
            assertEquals(4L, rotated.get(0, 1));
            assertEquals(1L, rotated.get(1, 0));
            assertEquals(3L, rotated.get(1, 1));
        }

        // ============ Transpose Tests ============

        @Test
        public void testTranspose() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1L, transposed.get(0, 0));
            assertEquals(4L, transposed.get(0, 1));
            assertEquals(2L, transposed.get(1, 0));
            assertEquals(5L, transposed.get(1, 1));
            assertEquals(3L, transposed.get(2, 0));
            assertEquals(6L, transposed.get(2, 1));
        }

        @Test
        public void testTranspose_square() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1L, transposed.get(0, 0));
            assertEquals(3L, transposed.get(0, 1));
            assertEquals(2L, transposed.get(1, 0));
            assertEquals(4L, transposed.get(1, 1));
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix reshaped = m.reshape(1, 9);
            assertEquals(1, reshaped.rowCount());
            assertEquals(9, reshaped.columnCount());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1, reshaped.get(0, i));
            }
        }

        @Test
        public void testReshape_back() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix reshaped = m.reshape(1, 9);
            LongMatrix reshapedBack = reshaped.reshape(3, 3);
            assertEquals(m, reshapedBack);
        }

        @Test
        public void testReshape_empty() {
            LongMatrix empty = LongMatrix.empty();
            LongMatrix reshaped = empty.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        // ============ Repeat Tests ============

        @Test
        public void testRepelem() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix repeated = m.repeatElements(2, 3);
            assertEquals(2, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals(1L, repeated.get(0, 0));
            assertEquals(1L, repeated.get(0, 1));
            assertEquals(1L, repeated.get(0, 2));
            assertEquals(2L, repeated.get(0, 3));
            assertEquals(2L, repeated.get(0, 4));
            assertEquals(2L, repeated.get(0, 5));
            assertEquals(1L, repeated.get(1, 0)); // second row same as first
        }

        @Test
        public void testRepelem_invalidArguments() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix repeated = m.repeatMatrix(2, 3);
            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals(1L, repeated.get(0, 0));
            assertEquals(2L, repeated.get(0, 1));
            assertEquals(1L, repeated.get(0, 2)); // repeat starts
            assertEquals(2L, repeated.get(0, 3));

            assertEquals(3L, repeated.get(1, 0));
            assertEquals(4L, repeated.get(1, 1));
            assertEquals(3L, repeated.get(1, 2)); // repeat
            assertEquals(4L, repeated.get(1, 3));

            // Second vertical repeat
            assertEquals(1L, repeated.get(2, 0));
            assertEquals(2L, repeated.get(2, 1));
        }

        @Test
        public void testRepmat_invalidArguments() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongList flat = m.flatten();
            assertEquals(6, flat.size());
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, flat.toArray());
        }

        @Test
        public void testFlatten_empty() {
            LongMatrix m = LongMatrix.empty();
            LongList flat = m.flatten();
            assertEquals(0, flat.size());
        }

        // ============ FlatOp Tests ============

        @Test
        public void testFlatOp() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            final int[] count = { 0 };
            m.applyOnFlattened(row -> count[0] += row.length);
            assertEquals(4, count[0]);
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix stacked = m1.stackVertically(m2);
            assertEquals(4, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1L, stacked.get(0, 0));
            assertEquals(4L, stacked.get(1, 1));
            assertEquals(5L, stacked.get(2, 0));
            assertEquals(8L, stacked.get(3, 1));
        }

        @Test
        public void testVstack_differentColumnCount() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L, 5L } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertEquals(1L, stacked.get(0, 0));
            assertEquals(2L, stacked.get(0, 1));
            assertEquals(5L, stacked.get(0, 2));
            assertEquals(6L, stacked.get(0, 3));
        }

        @Test
        public void testHstack_differentRowCount() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L }, { 5L, 6L } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Operations Tests ============

        @Test
        public void testAdd() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix result = m1.add(m2);
            assertEquals(6L, result.get(0, 0));
            assertEquals(8L, result.get(0, 1));
            assertEquals(10L, result.get(1, 0));
            assertEquals(12L, result.get(1, 1));
        }

        @Test
        public void testAdd_differentDimensions() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L }, { 5L, 6L } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void testSubtract() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix result = m1.subtract(m2);
            assertEquals(4L, result.get(0, 0));
            assertEquals(4L, result.get(0, 1));
            assertEquals(4L, result.get(1, 0));
            assertEquals(4L, result.get(1, 1));
        }

        @Test
        public void testSubtract_differentDimensions() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L }, { 5L, 6L } });
            assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
        }

        @Test
        public void testMultiply() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 2L, 3L }, { 4L, 5L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix result = m1.multiply(m2);
            assertEquals(11L, result.get(0, 0));
            assertEquals(16L, result.get(0, 1));
            assertEquals(19L, result.get(1, 0));
            assertEquals(28L, result.get(1, 1));
        }

        // ============ Conversion Tests ============

        @Test
        public void testBoxed() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            Matrix<Long> boxed = m.boxed();
            assertEquals(Long.valueOf(1L), boxed.get(0, 0));
            assertEquals(Long.valueOf(4L), boxed.get(1, 1));
        }

        @Test
        public void testToFloatMatrix() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            FloatMatrix result = m.toFloatMatrix();
            assertEquals(1.0f, result.get(0, 0), 0.001f);
            assertEquals(4.0f, result.get(1, 1), 0.001f);
        }

        @Test
        public void testToDoubleMatrix() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            DoubleMatrix result = m.toDoubleMatrix();
            assertEquals(1.0, result.get(0, 0), 0.001);
            assertEquals(4.0, result.get(1, 1), 0.001);
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith_twoMatrices() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix result = m1.zipWith(m2, (a, b) -> a + b);
            assertEquals(6L, result.get(0, 0));
            assertEquals(8L, result.get(0, 1));
            assertEquals(10L, result.get(1, 0));
            assertEquals(12L, result.get(1, 1));
        }

        @Test
        public void testZipWith_twoMatrices_differentDimensions() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L }, { 5L, 6L } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a + b));
        }

        @Test
        public void testZipWith_threeMatrices() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 9L, 10L }, { 11L, 12L } });
            LongMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
            assertEquals(15L, result.get(0, 0)); // 1 + 5 + 9
            assertEquals(18L, result.get(0, 1)); // 2 + 6 + 10
            assertEquals(21L, result.get(1, 0)); // 3 + 7 + 11
            assertEquals(24L, result.get(1, 1)); // 4 + 8 + 12
        }

        @Test
        public void testZipWith_threeMatrices_differentDimensions() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> a + b + c));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            long[] diagonal = m.streamMainDiagonal().toArray();
            assertArrayEquals(new long[] { 1L, 5L, 9L }, diagonal);
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.streamMainDiagonal().toArray());
        }

        @Test
        public void testStreamRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            long[] diagonal = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new long[] { 3L, 5L, 7L }, diagonal);
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.streamAntiDiagonal().toArray());
        }

        @Test
        public void testStreamH() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] all = m.streamHorizontal().toArray();
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, all);
        }

        @Test
        public void testStreamH_withRowIndex() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] row1 = m.streamHorizontal(1).toArray();
            assertArrayEquals(new long[] { 4L, 5L, 6L }, row1);
        }

        @Test
        public void testStreamH_withRowRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            long[] rows = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new long[] { 4L, 5L, 6L, 7L, 8L, 9L }, rows);
        }

        @Test
        public void testStreamH_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1, 1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(0, 2));
        }

        @Test
        public void testStreamV() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] all = m.streamVertical().toArray();
            assertArrayEquals(new long[] { 1L, 4L, 2L, 5L, 3L, 6L }, all);
        }

        @Test
        public void testStreamV_withColumnIndex() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] col1 = m.streamVertical(1).toArray();
            assertArrayEquals(new long[] { 2L, 5L }, col1);
        }

        @Test
        public void testStreamV_withColumnRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] columnCount = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new long[] { 2L, 5L, 3L, 6L }, columnCount);
        }

        @Test
        public void testStreamV_outOfBounds() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1, 1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(0, 3));
        }

        // ============ Stream of Streams Tests ============

        @Test
        public void testStreamR() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            List<long[]> rows = m.streamRows().map(LongStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new long[] { 1L, 2L }, rows.get(0));
            assertArrayEquals(new long[] { 3L, 4L }, rows.get(1));
        }

        @Test
        public void testStreamC() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            List<long[]> columnCount = m.streamColumns().map(LongStream::toArray).toList();
            assertEquals(2, columnCount.size());
            assertArrayEquals(new long[] { 1L, 3L }, columnCount.get(0));
            assertArrayEquals(new long[] { 2L, 4L }, columnCount.get(1));
        }

        // ============ Points Stream Tests ============

        @Test
        public void testPointsH() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            List<Point> points = m.pointsHorizontal().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(0, 1), points.get(1));
            assertEquals(Point.of(1, 0), points.get(2));
            assertEquals(Point.of(1, 1), points.get(3));
        }

        @Test
        public void testPointsV() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            List<Point> points = m.pointsVertical().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 0), points.get(1));
            assertEquals(Point.of(0, 1), points.get(2));
            assertEquals(Point.of(1, 1), points.get(3));
        }

        // ============ Sum Tests ============
        // Note: sumByLong(), sumByDouble() methods don't exist in LongMatrix

        // ============ Average Tests ============
        // Note: averageByLong(), averageByDouble() methods don't exist in LongMatrix

        // ============ Empty Matrix Tests ============

        @Test
        public void testElementCount() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            assertEquals(6, m.elementCount());
        }

        @Test
        public void testElementCount_Empty() {
            LongMatrix m = LongMatrix.empty();
            assertEquals(0, m.elementCount());
        }

        @Test
        public void testIsEmpty_true() {
            LongMatrix empty = LongMatrix.empty();
            assertTrue(empty.isEmpty());
        }

        @Test
        public void testIsEmpty_false() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L } });
            assertFalse(m.isEmpty());
        }

        // ============ Equals and HashCode Tests ============

        @Test
        public void testEquals() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 5L } });

            assertEquals(m1, m2);
            assertNotEquals(m1, m3);
        }

        @Test
        public void testHashCode_equal() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });

            assertEquals(m1.hashCode(), m2.hashCode());
        }

        // ============ ToString Tests ============

        @Test
        public void testToString() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.length() > 0);
        }
    }

    @Nested
    @Tag("2511")
    class LongMatrix2511Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            long[][] arr = { { 1L, 2L }, { 3L, 4L } };
            LongMatrix m = new LongMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(4L, m.get(1, 1));
        }

        @Test
        public void testConstructor_withNullArray() {
            LongMatrix m = new LongMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withEmptyArray() {
            LongMatrix m = new LongMatrix(new long[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withSingleElement() {
            LongMatrix m = new LongMatrix(new long[][] { { 42L } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42L, m.get(0, 0));
        }

        @Test
        public void testConstructor_withLargeMatrix() {
            long[][] arr = new long[10][10];
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    arr[i][j] = i * 10L + j;
                }
            }
            LongMatrix m = new LongMatrix(arr);
            assertEquals(10, m.rowCount());
            assertEquals(10, m.columnCount());
            assertEquals(0L, m.get(0, 0));
            assertEquals(99L, m.get(9, 9));
            assertEquals(55L, m.get(5, 5));
        }

        // ============ Factory Method Tests ============

        @Test
        public void testEmpty() {
            LongMatrix empty = LongMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());

            // Test singleton
            assertSame(LongMatrix.empty(), LongMatrix.empty());
        }

        @Test
        public void testOf_withValidArray() {
            long[][] arr = { { 1L, 2L }, { 3L, 4L } };
            LongMatrix m = LongMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void testOf_withNullArray() {
            LongMatrix m = LongMatrix.of((long[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withEmptyArray() {
            LongMatrix m = LongMatrix.of(new long[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withSingleRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L, 4L, 5L } });
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        @Test
        public void testOf_withSingleColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L }, { 2L }, { 3L }, { 4L } });
            assertEquals(4, m.rowCount());
            assertEquals(1, m.columnCount());
        }

        // ============ Create Method Tests ============

        @Test
        public void testCreate_fromIntArray() {
            int[][] ints = { { 1, 2 }, { 3, 4 } };
            LongMatrix m = LongMatrix.from(ints);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(4L, m.get(1, 1));
        }

        @Test
        public void testCreate_fromIntArray_empty() {
            LongMatrix m = LongMatrix.from(new int[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreate_fromIntArray_null() {
            LongMatrix m = LongMatrix.from((int[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreate_fromIntArray_nullFirstRow() {
            int[][] ints = { null, { 1, 2 } };
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(ints));
        }

        @Test
        public void testCreate_fromIntArray_differentRowLengths() {
            int[][] ints = { { 1, 2 }, { 3 } };
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(ints));
        }

        @Test
        public void testRandom() {
            LongMatrix m = LongMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertNotNull(m.rowView(0));
        }

        @Test
        public void testRandom_zeroLength() {
            LongMatrix m = LongMatrix.random(0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRepeat() {
            LongMatrix m = LongMatrix.repeat(1, 5, 42L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(42L, m.get(0, i));
            }
        }

        @Test
        public void testRepeat_zeroLength() {
            LongMatrix m = LongMatrix.repeat(1, 0, 42L);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRange() {
            LongMatrix m = LongMatrix.range(0L, 5L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.rowView(0));
        }

        @Test
        public void testRange_withStep() {
            LongMatrix m = LongMatrix.range(0L, 10L, 2L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L }, m.rowView(0));
        }

        @Test
        public void testRange_negativeStep() {
            LongMatrix m = LongMatrix.range(10L, 0L, -2L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 10L, 8L, 6L, 4L, 2L }, m.rowView(0));
        }

        @Test
        public void testRangeClosed() {
            LongMatrix m = LongMatrix.rangeClosed(0L, 4L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.rowView(0));
        }

        @Test
        public void testRangeClosed_withStep() {
            LongMatrix m = LongMatrix.rangeClosed(0L, 10L, 2L);
            assertEquals(1, m.rowCount());
            assertEquals(6, m.columnCount());
            assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L, 10L }, m.rowView(0));
        }

        @Test
        public void testDiagonalLU2RD() {
            LongMatrix m = LongMatrix.mainDiagonal(new long[] { 1L, 2L, 3L });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 2));
            assertEquals(0L, m.get(0, 1));
            assertEquals(0L, m.get(1, 0));
        }

        @Test
        public void testDiagonalRU2LD() {
            LongMatrix m = LongMatrix.antiDiagonal(new long[] { 1L, 2L, 3L });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 2));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 0));
            assertEquals(0L, m.get(0, 0));
        }

        @Test
        public void testDiagonal_bothDiagonals() {
            LongMatrix m = LongMatrix.diagonals(new long[] { 1L, 2L, 3L }, new long[] { 4L, 5L, 6L });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 2));
            assertEquals(4L, m.get(0, 2));
            assertEquals(2L, m.get(1, 1));
            assertEquals(6L, m.get(2, 0));
        }

        @Test
        public void testDiagonal_differentLengths() {
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.diagonals(new long[] { 1L, 2L }, new long[] { 1L, 2L, 3L }));
        }

        @Test
        public void testDiagonal_emptyBoth() {
            LongMatrix m = LongMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testUnbox() {
            Matrix<Long> boxed = Matrix.of(new Long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m = LongMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(4L, m.get(1, 1));
        }

        // ============ Get/Set Methods ============

        @Test
        public void testComponentType() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L } });
            assertEquals(long.class, m.componentType());
        }

        @Test
        public void testGet() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(3L, m.get(1, 0));
            assertEquals(4L, m.get(1, 1));
        }

        @Test
        public void testGet_withPoint() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            Point p = Point.of(1, 0);
            assertEquals(3L, m.get(p));
        }

        @Test
        public void testSet() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.set(0, 0, 10L);
            assertEquals(10L, m.get(0, 0));
            m.set(1, 1, 20L);
            assertEquals(20L, m.get(1, 1));
        }

        @Test
        public void testSet_withPoint() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            Point p = Point.of(1, 0);
            m.set(p, 99L);
            assertEquals(99L, m.get(1, 0));
        }

        // ============ Directional Methods (up, down, left, right) ============

        @Test
        public void testUpOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1L, up.get());

            OptionalLong noUp = m.above(0, 0);
            assertFalse(noUp.isPresent());
        }

        @Test
        public void testDownOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3L, down.get());

            OptionalLong noDown = m.below(1, 0);
            assertFalse(noDown.isPresent());
        }

        @Test
        public void testLeftOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1L, left.get());

            OptionalLong noLeft = m.left(0, 0);
            assertFalse(noLeft.isPresent());
        }

        @Test
        public void testRightOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2L, right.get());

            OptionalLong noRight = m.right(0, 1);
            assertFalse(noRight.isPresent());
        }

        // ============ Row/Column Operations ============

        @Test
        public void testRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            assertArrayEquals(new long[] { 1L, 2L, 3L }, m.rowView(0));
            assertArrayEquals(new long[] { 4L, 5L, 6L }, m.rowView(1));
        }

        @Test
        public void testRow_invalidIndex() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(1));
        }

        @Test
        public void testColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            assertArrayEquals(new long[] { 1L, 4L }, m.columnCopy(0));
            assertArrayEquals(new long[] { 2L, 5L }, m.columnCopy(1));
            assertArrayEquals(new long[] { 3L, 6L }, m.columnCopy(2));
        }

        @Test
        public void testColumn_invalidIndex() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.setRow(0, new long[] { 10L, 20L });
            assertArrayEquals(new long[] { 10L, 20L }, m.rowView(0));
            assertEquals(3L, m.get(1, 0));
        }

        @Test
        public void testSetRow_invalidLength() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new long[] { 1L, 2L, 3L }));
        }

        @Test
        public void testSetColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.setColumn(0, new long[] { 10L, 30L });
            assertEquals(10L, m.get(0, 0));
            assertEquals(30L, m.get(1, 0));
            assertEquals(2L, m.get(0, 1));
        }

        @Test
        public void testSetColumn_invalidLength() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new long[] { 1L, 2L, 3L }));
        }

        @Test
        public void testUpdateRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateRow(0, x -> x * 2);
            assertArrayEquals(new long[] { 2L, 4L }, m.rowView(0));
            assertArrayEquals(new long[] { 3L, 4L }, m.rowView(1));
        }

        @Test
        public void testUpdateColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateColumn(0, x -> x * 10);
            assertEquals(10L, m.get(0, 0));
            assertEquals(30L, m.get(1, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(4L, m.get(1, 1));
        }

        // ============ Diagonal Operations ============

        @Test
        public void testGetLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            assertArrayEquals(new long[] { 1L, 5L, 9L }, m.getMainDiagonal());
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.setMainDiagonal(new long[] { 10L, 20L, 30L });
            assertEquals(10L, m.get(0, 0));
            assertEquals(20L, m.get(1, 1));
            assertEquals(30L, m.get(2, 2));
        }

        @Test
        public void testSetLU2RD_invalidLength() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new long[] { 1L, 2L, 3L }));
        }

        @Test
        public void testUpdateLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.updateMainDiagonal(x -> x * 10);
            assertEquals(10L, m.get(0, 0));
            assertEquals(50L, m.get(1, 1));
            assertEquals(90L, m.get(2, 2));
        }

        @Test
        public void testGetRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            assertArrayEquals(new long[] { 3L, 5L, 7L }, m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.setAntiDiagonal(new long[] { 30L, 50L, 70L });
            assertEquals(30L, m.get(0, 2));
            assertEquals(50L, m.get(1, 1));
            assertEquals(70L, m.get(2, 0));
        }

        @Test
        public void testUpdateRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.updateAntiDiagonal(x -> x + 100L);
            assertEquals(103L, m.get(0, 2));
            assertEquals(105L, m.get(1, 1));
            assertEquals(107L, m.get(2, 0));
        }

        // ============ Update Methods ============

        @Test
        public void testUpdateAll_unary() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateAll(x -> x * 2);
            assertEquals(2L, m.get(0, 0));
            assertEquals(4L, m.get(0, 1));
            assertEquals(6L, m.get(1, 0));
            assertEquals(8L, m.get(1, 1));
        }

        @Test
        public void testUpdateAll_biFunction() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateAll((i, j) -> i * 10L + j);
            assertEquals(0L, m.get(0, 0));
            assertEquals(1L, m.get(0, 1));
            assertEquals(10L, m.get(1, 0));
            assertEquals(11L, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_predicate() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            m.replaceIf(x -> x % 2 == 0, 99L);
            assertEquals(1L, m.get(0, 0));
            assertEquals(99L, m.get(0, 1));
            assertEquals(3L, m.get(0, 2));
            assertEquals(99L, m.get(1, 0));
        }

        @Test
        public void testReplaceIf_biPredicate() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.replaceIf((i, j) -> i == j, 0L);
            assertEquals(0L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(3L, m.get(1, 0));
            assertEquals(0L, m.get(1, 1));
        }

        // ============ Map Methods ============

        @Test
        public void testMap() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix result = m.map(x -> x * 10);
            assertEquals(10L, result.get(0, 0));
            assertEquals(20L, result.get(0, 1));
            assertEquals(30L, result.get(1, 0));
            assertEquals(40L, result.get(1, 1));
            // Original should be unchanged
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void testMapToDouble() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            DoubleMatrix result = m.mapToDouble(x -> x * 0.5);
            assertEquals(0.5, result.get(0, 0), 0.0001);
            assertEquals(2.0, result.get(1, 1), 0.0001);
        }

        @Test
        public void testMapToObj() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            Matrix<String> result = m.mapToObj(x -> "Value:" + x, String.class);
            assertEquals("Value:1", result.get(0, 0));
            assertEquals("Value:4", result.get(1, 1));
        }

        // ============ Fill Methods ============

        @Test
        public void testFill_value() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.fill(99L);
            assertEquals(99L, m.get(0, 0));
            assertEquals(99L, m.get(0, 1));
            assertEquals(99L, m.get(1, 0));
            assertEquals(99L, m.get(1, 1));
        }

        @Test
        public void testFill_array() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.copyFrom(new long[][] { { 10L, 20L }, { 30L, 40L } });
            assertEquals(10L, m.get(0, 0));
            assertEquals(20L, m.get(0, 1));
            assertEquals(30L, m.get(1, 0));
            assertEquals(40L, m.get(1, 1));
        }

        @Test
        public void testFill_arrayWithOffset() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.copyFrom(1, 1, new long[][] { { 99L, 88L } });
            assertEquals(1L, m.get(0, 0));
            assertEquals(99L, m.get(1, 1));
            assertEquals(88L, m.get(1, 2));
        }

        // ============ Copy Methods ============

        @Test
        public void testCopy() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix copy = m.copy();
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(1L, copy.get(0, 0));

            // Verify it's a deep copy
            copy.set(0, 0, 99L);
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void testCopy_rowRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            LongMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(3L, copy.get(0, 0));
            assertEquals(6L, copy.get(1, 1));
        }

        @Test
        public void testCopy_fullRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix copy = m.copy(1, 2, 1, 3);
            assertEquals(1, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(5L, copy.get(0, 0));
            assertEquals(6L, copy.get(0, 1));
        }

        // ============ Extend Methods ============

        @Test
        public void testExtend_newSize() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1L, extended.get(0, 0));
            assertEquals(0L, extended.get(2, 2));
        }

        @Test
        public void testExtend_withDefaultValue() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix extended = m.resize(2, 3, 99L);
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1L, extended.get(0, 0));
            assertEquals(99L, extended.get(0, 2));
            assertEquals(99L, extended.get(1, 0));
        }

        @Test
        public void testExtend_directions() {
            LongMatrix m = LongMatrix.of(new long[][] { { 5L } });
            LongMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(5L, extended.get(1, 1));
            assertEquals(0L, extended.get(0, 0));
        }

        @Test
        public void testExtend_directionsWithDefault() {
            LongMatrix m = LongMatrix.of(new long[][] { { 5L } });
            LongMatrix extended = m.extend(1, 1, 1, 1, 9L);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(5L, extended.get(1, 1));
            assertEquals(9L, extended.get(0, 0));
            assertEquals(9L, extended.get(2, 2));
        }

        // ============ Reverse and Flip Methods ============

        @Test
        public void testReverseH() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            m.flipInPlaceHorizontally();
            assertEquals(3L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(1L, m.get(0, 2));
            assertEquals(6L, m.get(1, 0));
        }

        @Test
        public void testReverseV() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            m.flipInPlaceVertically();
            assertEquals(5L, m.get(0, 0));
            assertEquals(6L, m.get(0, 1));
            assertEquals(1L, m.get(2, 0));
        }

        @Test
        public void testFlipH() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix flipped = m.flipHorizontally();
            assertEquals(3L, flipped.get(0, 0));
            assertEquals(1L, flipped.get(0, 2));
            // Original unchanged
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix flipped = m.flipVertically();
            assertEquals(3L, flipped.get(0, 0));
            assertEquals(1L, flipped.get(1, 0));
            // Original unchanged
            assertEquals(1L, m.get(0, 0));
        }

        // ============ Rotation Methods ============

        @Test
        public void testRotate90() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix rotated = m.rotate90();
            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4L, rotated.get(0, 0));
            assertEquals(1L, rotated.get(0, 1));
            assertEquals(6L, rotated.get(2, 0));
        }

        @Test
        public void testRotate180() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix rotated = m.rotate180();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4L, rotated.get(0, 0));
            assertEquals(3L, rotated.get(0, 1));
            assertEquals(2L, rotated.get(1, 0));
            assertEquals(1L, rotated.get(1, 1));
        }

        @Test
        public void testRotate270() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix rotated = m.rotate270();
            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3L, rotated.get(0, 0));
            assertEquals(6L, rotated.get(0, 1));
        }

        // ============ Transpose and Reshape Methods ============

        @Test
        public void testTranspose() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1L, transposed.get(0, 0));
            assertEquals(4L, transposed.get(0, 1));
            assertEquals(6L, transposed.get(2, 1));
        }

        @Test
        public void testReshape() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix reshaped = m.reshape(3, 2);
            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1L, reshaped.get(0, 0));
            assertEquals(2L, reshaped.get(0, 1));
            assertEquals(6L, reshaped.get(2, 1));
        }

        @Test
        public void testRepelem() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix result = m.repeatElements(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1L, result.get(0, 0));
            assertEquals(1L, result.get(0, 1));
            assertEquals(1L, result.get(1, 0));
            assertEquals(4L, result.get(3, 3));
        }

        @Test
        public void testRepmat() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix result = m.repeatMatrix(2, 2);
            assertEquals(2, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1L, result.get(0, 0));
            assertEquals(2L, result.get(0, 1));
            assertEquals(1L, result.get(0, 2));
            assertEquals(1L, result.get(1, 0));
        }

        // ============ Flatten and FlatOp Methods ============

        @Test
        public void testFlatten() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongList flattened = m.flatten();
            assertEquals(4, flattened.size());
            assertEquals(1L, flattened.get(0));
            assertEquals(2L, flattened.get(1));
            assertEquals(3L, flattened.get(2));
            assertEquals(4L, flattened.get(3));
        }

        @Test
        public void testFlatOp() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            final long[] sum = { 0L };
            m.applyOnFlattened(row -> {
                for (long val : row) {
                    sum[0] += val;
                }
            });
            assertEquals(10L, sum[0]);
        }

        // ============ Stack Methods ============

        @Test
        public void testVstack() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L } });
            LongMatrix stacked = m1.stackVertically(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1L, stacked.get(0, 0));
            assertEquals(3L, stacked.get(1, 0));
        }

        @Test
        public void testVstack_incompatibleCols() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L, 5L } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L }, { 3L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 2L }, { 4L } });
            LongMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1L, stacked.get(0, 0));
            assertEquals(2L, stacked.get(0, 1));
        }

        @Test
        public void testHstack_incompatibleRows() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 2L }, { 3L } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Methods ============

        @Test
        public void testAdd() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
            LongMatrix result = m1.add(m2);
            assertEquals(11L, result.get(0, 0));
            assertEquals(22L, result.get(0, 1));
            assertEquals(33L, result.get(1, 0));
            assertEquals(44L, result.get(1, 1));
        }

        @Test
        public void testSubtract() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix result = m1.subtract(m2);
            assertEquals(9L, result.get(0, 0));
            assertEquals(18L, result.get(0, 1));
            assertEquals(27L, result.get(1, 0));
            assertEquals(36L, result.get(1, 1));
        }

        @Test
        public void testMultiply() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
            LongMatrix result = m1.multiply(m2);
            assertEquals(70, result.get(0, 0));
            assertEquals(100, result.get(0, 1));
            assertEquals(150, result.get(1, 0));
            assertEquals(220, result.get(1, 1));
        }

        // ============ Conversion Methods ============

        @Test
        public void testBoxed() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            Matrix<Long> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(Long.valueOf(1L), boxed.get(0, 0));
            assertEquals(Long.valueOf(4L), boxed.get(1, 1));
        }

        @Test
        public void testToIntMatrix() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            IntMatrix result = m.mapToInt(l -> (int) l);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1, result.get(0, 0));
            assertEquals(4, result.get(1, 1));
        }

        // LongMatrix doesn't have mapToFloat method - removed test
        // @Test
        // public void testToFloatMatrix() {
        //     LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        //     FloatMatrix result = m.mapToFloat(l -> (float) l);
        //     assertEquals(2, result.rowCount());
        //     assertEquals(2, result.columnCount());
        //     assertEquals(1.0f, result.get(0, 0), 0.0001f);
        //     assertEquals(4.0f, result.get(1, 1), 0.0001f);
        // }

        @Test
        public void testToDoubleMatrix() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            DoubleMatrix result = m.mapToDouble(l -> (double) l);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1.0, result.get(0, 0), 0.0001);
            assertEquals(4.0, result.get(1, 1), 0.0001);
        }

        // ============ ZipWith Methods ============

        @Test
        public void testZipWith_twoMatrices() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L }, { 30L, 40L } });
            LongMatrix result = m1.zipWith(m2, (a, b) -> a + b);
            assertEquals(11L, result.get(0, 0));
            assertEquals(22L, result.get(0, 1));
            assertEquals(33L, result.get(1, 0));
            assertEquals(44L, result.get(1, 1));
        }

        @Test
        public void testZipWith_threeMatrices() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 10L, 20L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 100L, 200L } });
            LongMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
            assertEquals(111L, result.get(0, 0));
            assertEquals(222L, result.get(0, 1));
        }

        // ============ Stream Methods ============

        @Test
        public void testStreamLU2RD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            long[] diagonal = m.streamMainDiagonal().toArray();
            assertArrayEquals(new long[] { 1L, 5L, 9L }, diagonal);
        }

        @Test
        public void testStreamRU2LD() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            long[] diagonal = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new long[] { 3L, 5L, 7L }, diagonal);
        }

        @Test
        public void testStreamH() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] all = m.streamHorizontal().toArray();
            assertArrayEquals(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, all);
        }

        @Test
        public void testStreamH_singleRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] row1 = m.streamHorizontal(1).toArray();
            assertArrayEquals(new long[] { 4L, 5L, 6L }, row1);
        }

        @Test
        public void testStreamH_rowRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            long[] rows = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new long[] { 3L, 4L, 5L, 6L }, rows);
        }

        @Test
        public void testStreamV() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] all = m.streamVertical().toArray();
            assertArrayEquals(new long[] { 1L, 4L, 2L, 5L, 3L, 6L }, all);
        }

        @Test
        public void testStreamV_singleColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] col1 = m.streamVertical(1).toArray();
            assertArrayEquals(new long[] { 2L, 5L }, col1);
        }

        @Test
        public void testStreamV_columnRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long[] columnCount = m.streamVertical(0, 2).toArray();
            assertArrayEquals(new long[] { 1L, 4L, 2L, 5L }, columnCount);
        }

        // ============ Stream of Streams Methods ============

        @Test
        public void testStreamR() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            List<long[]> rows = m.streamRows().map(LongStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new long[] { 1L, 2L }, rows.get(0));
            assertArrayEquals(new long[] { 3L, 4L }, rows.get(1));
        }

        @Test
        public void testStreamC() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            List<long[]> columnCount = m.streamColumns().map(LongStream::toArray).toList();
            assertEquals(2, columnCount.size());
            assertArrayEquals(new long[] { 1L, 3L }, columnCount.get(0));
            assertArrayEquals(new long[] { 2L, 4L }, columnCount.get(1));
        }

        // ============ Inherited Methods from AbstractMatrix ============

        @Test
        public void testIsEmpty() {
            assertTrue(LongMatrix.empty().isEmpty());
            assertFalse(LongMatrix.of(new long[][] { { 1L } }).isEmpty());
        }

        @Test
        public void testHashCode() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void testEquals() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 5L } });

            assertEquals(m1, m2);
            assertNotEquals(m1, m3);
            assertNotEquals(m1, null);
            assertNotEquals(m1, "not a matrix");
        }

        @Test
        public void testToString() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("1"));
            assertTrue(str.contains("4"));
        }

        @Test
        public void testToArray() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long[][] arr = m.backingArray();
            assertEquals(2, arr.length);
            assertEquals(2, arr[0].length);
            assertArrayEquals(new long[] { 1L, 2L }, arr[0]);
            assertArrayEquals(new long[] { 3L, 4L }, arr[1]);
        }

        // ============ Edge Cases ============

        @Test
        public void testLargeDimensions() {
            long[][] data = new long[100][50];
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 50; j++) {
                    data[i][j] = i + j;
                }
            }
            LongMatrix m = LongMatrix.of(data);
            assertEquals(100, m.rowCount());
            assertEquals(50, m.columnCount());
            assertEquals(0L, m.get(0, 0));
            assertEquals(148L, m.get(99, 49));
        }

        @Test
        public void testSingleRowOperations() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L, 4L, 5L } });
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());

            LongMatrix transposed = m.transpose();
            assertEquals(5, transposed.rowCount());
            assertEquals(1, transposed.columnCount());
        }

        @Test
        public void testSingleColumnOperations() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L }, { 2L }, { 3L } });
            assertEquals(3, m.rowCount());
            assertEquals(1, m.columnCount());

            LongMatrix transposed = m.transpose();
            assertEquals(1, transposed.rowCount());
            assertEquals(3, transposed.columnCount());
        }

        @Test
        public void testChainedOperations() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix result = m.map(x -> x * 2).transpose().map(x -> x + 1);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(3L, result.get(0, 0)); // (1*2)+1
            assertEquals(7L, result.get(0, 1)); // (3*2)+1
            assertEquals(5L, result.get(1, 0)); // (2*2)+1
            assertEquals(9L, result.get(1, 1)); // (4*2)+1
        }
    }

    @Nested
    @Tag("2512")
    class LongMatrix2512Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void test_constructor_withValidArray() {
            long[][] arr = { { 1L, 2L }, { 3L, 4L } };
            LongMatrix m = new LongMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(4L, m.get(1, 1));
        }

        @Test
        public void test_constructor_withNullArray() {
            LongMatrix m = new LongMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_constructor_withEmptyArray() {
            LongMatrix m = new LongMatrix(new long[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_constructor_withSingleElement() {
            LongMatrix m = new LongMatrix(new long[][] { { 42L } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42L, m.get(0, 0));
        }

        // ============ Factory Method Tests ============

        @Test
        public void test_empty() {
            LongMatrix empty = LongMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());
            assertSame(LongMatrix.empty(), LongMatrix.empty());
        }

        @Test
        public void test_of_withValidArray() {
            long[][] arr = { { 1L, 2L }, { 3L, 4L } };
            LongMatrix m = LongMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void test_of_withNullArray() {
            LongMatrix m = LongMatrix.of((long[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_of_withEmptyArray() {
            LongMatrix m = LongMatrix.of(new long[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_of_withSingleRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L, 4L, 5L } });
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        // ============ Create Method Tests ============

        @Test
        public void test_create_fromIntArray() {
            int[][] ints = { { 1, 2 }, { 3, 4 } };
            LongMatrix m = LongMatrix.from(ints);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(4L, m.get(1, 1));
        }

        @Test
        public void test_create_fromIntArray_empty() {
            LongMatrix m = LongMatrix.from(new int[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_create_fromIntArray_null() {
            LongMatrix m = LongMatrix.from((int[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_create_fromIntArray_nullFirstRow() {
            int[][] ints = { null, { 1, 2 } };
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(ints));
        }

        @Test
        public void test_create_fromIntArray_differentRowLengths() {
            int[][] ints = { { 1, 2 }, { 3 } };
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(ints));
        }

        // ============ Random and Repeat Tests ============

        @Test
        public void test_random() {
            LongMatrix m = LongMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        @Test
        public void test_random_zeroLength() {
            LongMatrix m = LongMatrix.random(0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void test_repeat() {
            LongMatrix m = LongMatrix.repeat(1, 5, 42L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(42L, m.get(0, i));
            }
        }

        @Test
        public void test_repeat_zeroLength() {
            LongMatrix m = LongMatrix.repeat(1, 0, 42L);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        // ============ Range Tests ============

        @Test
        public void test_range() {
            LongMatrix m = LongMatrix.range(0L, 5L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.rowView(0));
        }

        @Test
        public void test_range_withStep() {
            LongMatrix m = LongMatrix.range(0L, 10L, 2L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L }, m.rowView(0));
        }

        @Test
        public void test_range_empty() {
            LongMatrix m = LongMatrix.range(5L, 5L);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void test_rangeClosed() {
            LongMatrix m = LongMatrix.rangeClosed(0L, 4L);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.rowView(0));
        }

        @Test
        public void test_rangeClosed_withStep() {
            LongMatrix m = LongMatrix.rangeClosed(0L, 10L, 2L);
            assertEquals(1, m.rowCount());
            assertEquals(6, m.columnCount());
            assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L, 10L }, m.rowView(0));
        }

        @Test
        public void test_rangeClosed_singleValue() {
            LongMatrix m = LongMatrix.rangeClosed(5L, 5L);
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(5L, m.get(0, 0));
        }

        // ============ Diagonal Tests ============

        @Test
        public void test_mainDiagonal() {
            long[] diag = { 1L, 2L, 3L };
            LongMatrix m = LongMatrix.mainDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 2));
            assertEquals(0L, m.get(0, 1));
            assertEquals(0L, m.get(1, 0));
        }

        @Test
        public void test_mainDiagonal_null() {
            LongMatrix m = LongMatrix.mainDiagonal(null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_antiDiagonal() {
            long[] diag = { 1L, 2L, 3L };
            LongMatrix m = LongMatrix.antiDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 2));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 0));
            assertEquals(0L, m.get(0, 0));
        }

        @Test
        public void test_antiDiagonal_null() {
            LongMatrix m = LongMatrix.antiDiagonal(null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_diagonal_both() {
            long[] lu2rd = { 1L, 2L, 3L };
            long[] ru2ld = { 4L, 5L, 6L };
            LongMatrix m = LongMatrix.diagonals(lu2rd, ru2ld);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(1, 1));
            assertEquals(3L, m.get(2, 2));
            assertEquals(4L, m.get(0, 2));
            assertEquals(6L, m.get(2, 0));
        }

        @Test
        public void test_diagonal_differentLengths() {
            long[] lu2rd = { 1L, 2L };
            long[] ru2ld = { 4L, 5L, 6L };
            assertThrows(IllegalArgumentException.class, () -> LongMatrix.diagonals(lu2rd, ru2ld));
        }

        @Test
        public void test_diagonal_bothNull() {
            LongMatrix m = LongMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        // ============ Unbox Test ============

        @Test
        public void test_unbox() {
            Matrix<Long> boxed = Matrix.of(new Long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m = LongMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(4L, m.get(1, 1));
        }

        @Test
        public void test_unbox_withNulls() {
            Matrix<Long> boxed = Matrix.of(new Long[][] { { 1L, null }, { null, 4L } });
            LongMatrix m = LongMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1L, m.get(0, 0));
            assertEquals(0L, m.get(0, 1));
            assertEquals(0L, m.get(1, 0));
            assertEquals(4L, m.get(1, 1));
        }

        // ============ Component Type Test ============

        @Test
        public void test_componentType() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertEquals(long.class, m.componentType());
        }

        // ============ Get and Set Tests ============

        @Test
        public void test_get_byIndices() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(3L, m.get(1, 0));
            assertEquals(4L, m.get(1, 1));
        }

        @Test
        public void test_get_byPoint() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            Point p = Point.of(0, 1);
            assertEquals(2L, m.get(p));
        }

        @Test
        public void test_set_byIndices() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.set(0, 1, 9L);
            assertEquals(9L, m.get(0, 1));
        }

        @Test
        public void test_set_byPoint() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            Point p = Point.of(1, 1);
            m.set(p, 9L);
            assertEquals(9L, m.get(p));
        }

        // ============ Directional Access Tests ============

        @Test
        public void test_upOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1L, up.get());
        }

        @Test
        public void test_upOf_firstRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong up = m.above(0, 0);
            assertFalse(up.isPresent());
        }

        @Test
        public void test_downOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3L, down.get());
        }

        @Test
        public void test_downOf_lastRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong down = m.below(1, 0);
            assertFalse(down.isPresent());
        }

        @Test
        public void test_leftOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1L, left.get());
        }

        @Test
        public void test_leftOf_firstColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong left = m.left(0, 0);
            assertFalse(left.isPresent());
        }

        @Test
        public void test_rightOf() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2L, right.get());
        }

        @Test
        public void test_rightOf_lastColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            OptionalLong right = m.right(0, 1);
            assertFalse(right.isPresent());
        }

        // ============ Row and Column Access Tests ============

        @Test
        public void test_row() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long[] row = m.rowView(0);
            assertArrayEquals(new long[] { 1L, 2L }, row);
        }

        @Test
        public void test_row_invalidIndex() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(5));
        }

        @Test
        public void test_column() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long[] col = m.columnCopy(0);
            assertArrayEquals(new long[] { 1L, 3L }, col);
        }

        @Test
        public void test_column_invalidIndex() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(5));
        }

        @Test
        public void test_setRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.setRow(0, new long[] { 9L, 8L });
            assertArrayEquals(new long[] { 9L, 8L }, m.rowView(0));
        }

        @Test
        public void test_setRow_invalidRowIndex() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(5, new long[] { 1L, 2L }));
        }

        @Test
        public void test_setRow_invalidLength() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new long[] { 1L }));
        }

        @Test
        public void test_setColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.setColumn(0, new long[] { 9L, 8L });
            assertArrayEquals(new long[] { 9L, 8L }, m.columnCopy(0));
        }

        @Test
        public void test_setColumn_invalidColumnIndex() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(5, new long[] { 1L }));
        }

        @Test
        public void test_setColumn_invalidLength() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new long[] { 1L }));
        }

        // ============ Update Row and Column Tests ============

        @Test
        public void test_updateRow() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateRow(0, x -> x * 2);
            assertArrayEquals(new long[] { 2L, 4L }, m.rowView(0));
            assertArrayEquals(new long[] { 3L, 4L }, m.rowView(1));
        }

        @Test
        public void test_updateColumn() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateColumn(0, x -> x * 2);
            assertArrayEquals(new long[] { 2L, 6L }, m.columnCopy(0));
            assertArrayEquals(new long[] { 2L, 4L }, m.columnCopy(1));
        }

        // ============ Diagonal Get/Set Tests ============

        @Test
        public void test_getMainDiagonal() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            long[] diag = m.getMainDiagonal();
            assertArrayEquals(new long[] { 1L, 5L, 9L }, diag);
        }

        @Test
        public void test_getMainDiagonal_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void test_setMainDiagonal() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.setMainDiagonal(new long[] { 9L, 8L });
            assertArrayEquals(new long[] { 9L, 8L }, m.getMainDiagonal());
        }

        @Test
        public void test_setMainDiagonal_nonSquare() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new long[] { 9L }));
        }

        @Test
        public void test_setMainDiagonal_invalidLength() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new long[] { 9L }));
        }

        @Test
        public void test_updateMainDiagonal() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateMainDiagonal(x -> x * 2);
            assertArrayEquals(new long[] { 2L, 8L }, m.getMainDiagonal());
        }

        @Test
        public void test_getAntiDiagonal() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            long[] diag = m.getAntiDiagonal();
            assertArrayEquals(new long[] { 3L, 5L, 7L }, diag);
        }

        @Test
        public void test_setAntiDiagonal() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.setAntiDiagonal(new long[] { 9L, 8L });
            assertArrayEquals(new long[] { 9L, 8L }, m.getAntiDiagonal());
        }

        @Test
        public void test_updateAntiDiagonal() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateAntiDiagonal(x -> x * 2);
            long[] diag = m.getAntiDiagonal();
            assertEquals(4L, diag[0]);
            assertEquals(6L, diag[1]);
        }

        // ============ Update All Tests ============

        @Test
        public void test_updateAll_unaryOperator() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateAll(x -> x * 2);
            assertEquals(2L, m.get(0, 0));
            assertEquals(4L, m.get(0, 1));
            assertEquals(6L, m.get(1, 0));
            assertEquals(8L, m.get(1, 1));
        }

        @Test
        public void test_updateAll_biFunction() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.updateAll((i, j) -> (i + 1) * 10L + (j + 1));
            assertEquals(11L, m.get(0, 0));
            assertEquals(12L, m.get(0, 1));
            assertEquals(21L, m.get(1, 0));
            assertEquals(22L, m.get(1, 1));
        }

        // ============ Replace If Tests ============

        @Test
        public void test_replaceIf_predicate() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.replaceIf(x -> x > 2, 99L);
            assertEquals(1L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(99L, m.get(1, 0));
            assertEquals(99L, m.get(1, 1));
        }

        @Test
        public void test_replaceIf_biPredicate() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.replaceIf((i, j) -> i == j, 99L);
            assertEquals(99L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(3L, m.get(1, 0));
            assertEquals(99L, m.get(1, 1));
        }

        // ============ Map Tests ============

        @Test
        public void test_map() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix result = m.map(x -> x * 2);
            assertEquals(2L, result.get(0, 0));
            assertEquals(4L, result.get(0, 1));
            assertEquals(6L, result.get(1, 0));
            assertEquals(8L, result.get(1, 1));
            // Original should be unchanged
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void test_mapToInt() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            IntMatrix result = m.mapToInt(x -> (int) x);
            assertEquals(1, result.get(0, 0));
            assertEquals(2, result.get(0, 1));
            assertEquals(3, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void test_mapToDouble() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            DoubleMatrix result = m.mapToDouble(x -> (double) x);
            assertEquals(1.0, result.get(0, 0), 0.0);
            assertEquals(2.0, result.get(0, 1), 0.0);
            assertEquals(3.0, result.get(1, 0), 0.0);
            assertEquals(4.0, result.get(1, 1), 0.0);
        }

        @Test
        public void test_mapToObj() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            Matrix<String> result = m.mapToObj(x -> String.valueOf(x), String.class);
            assertEquals("1", result.get(0, 0));
            assertEquals("2", result.get(0, 1));
            assertEquals("3", result.get(1, 0));
            assertEquals("4", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void test_fill_value() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.fill(9L);
            assertEquals(9L, m.get(0, 0));
            assertEquals(9L, m.get(0, 1));
            assertEquals(9L, m.get(1, 0));
            assertEquals(9L, m.get(1, 1));
        }

        @Test
        public void test_fill_array() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            m.copyFrom(new long[][] { { 9L, 8L }, { 7L, 6L } });
            assertEquals(9L, m.get(0, 0));
            assertEquals(8L, m.get(0, 1));
            assertEquals(7L, m.get(1, 0));
            assertEquals(6L, m.get(1, 1));
        }

        @Test
        public void test_fill_withOffset() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            m.copyFrom(1, 1, new long[][] { { 99L } });
            assertEquals(1L, m.get(0, 0));
            assertEquals(99L, m.get(1, 1));
            assertEquals(9L, m.get(2, 2));
        }

        @Test
        public void test_fill_withOffset_arrayLargerThanMatrix() {
            // Test that fill() gracefully handles source arrays larger than the target matrix
            // by copying only what fits (as documented in the javadoc)
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            m.copyFrom(0, 0, new long[][] { { 9L, 8L, 7L } }); // Source array has 3 elements, matrix has 2
            // Should copy only the first 2 elements that fit
            assertEquals(9L, m.get(0, 0));
            assertEquals(8L, m.get(0, 1));
        }

        // ============ Copy Tests ============

        @Test
        public void test_copy() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals(1L, copy.get(0, 0));
            copy.set(0, 0, 99L);
            assertEquals(1L, m.get(0, 0)); // Original unchanged
        }

        @Test
        public void test_copy_withRowRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            LongMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(3L, copy.get(0, 0));
            assertEquals(6L, copy.get(1, 1));
        }

        @Test
        public void test_copy_withFullRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            LongMatrix copy = m.copy(1, 3, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(5L, copy.get(0, 0));
            assertEquals(9L, copy.get(1, 1));
        }

        @Test
        public void test_copy_invalidRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 5));
        }

        // ============ Extend Tests ============

        @Test
        public void test_extend() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1L, extended.get(0, 0));
            assertEquals(0L, extended.get(2, 2));
        }

        @Test
        public void test_extend_withDefaultValue() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix extended = m.resize(2, 3, 99L);
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1L, extended.get(0, 0));
            assertEquals(99L, extended.get(1, 1));
            assertEquals(99L, extended.get(0, 2));
        }

        @Test
        public void test_extend_smaller() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix result = m.resize(1, 2);
            assertEquals(1, result.rowCount());
            assertEquals(2, result.columnCount());
        }

        @Test
        public void test_extend_directional() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1L, extended.get(1, 1));
            assertEquals(0L, extended.get(0, 0));
        }

        @Test
        public void test_extend_directional_withDefaultValue() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix extended = m.extend(1, 1, 1, 1, 99L);
            assertEquals(3, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1L, extended.get(1, 1));
            assertEquals(99L, extended.get(0, 0));
        }

        // ============ Reverse and Flip Tests ============

        @Test
        public void test_flipInPlaceHorizontally() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            m.flipInPlaceHorizontally();
            assertEquals(3L, m.get(0, 0));
            assertEquals(2L, m.get(0, 1));
            assertEquals(1L, m.get(0, 2));
        }

        @Test
        public void test_flipInPlaceVertically() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            m.flipInPlaceVertically();
            assertEquals(5L, m.get(0, 0));
            assertEquals(3L, m.get(1, 0));
            assertEquals(1L, m.get(2, 0));
        }

        @Test
        public void test_flipHorizontally() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix flipped = m.flipHorizontally();
            assertEquals(3L, flipped.get(0, 0));
            assertEquals(2L, flipped.get(0, 1));
            assertEquals(1L, flipped.get(0, 2));
            // Original unchanged
            assertEquals(1L, m.get(0, 0));
        }

        @Test
        public void test_flipVertically() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            LongMatrix flipped = m.flipVertically();
            assertEquals(5L, flipped.get(0, 0));
            assertEquals(3L, flipped.get(1, 0));
            assertEquals(1L, flipped.get(2, 0));
            // Original unchanged
            assertEquals(1L, m.get(0, 0));
        }

        // ============ Rotate Tests ============

        @Test
        public void test_rotate90() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3L, rotated.get(0, 0));
            assertEquals(1L, rotated.get(0, 1));
            assertEquals(4L, rotated.get(1, 0));
            assertEquals(2L, rotated.get(1, 1));
        }

        @Test
        public void test_rotate180() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix rotated = m.rotate180();
            assertEquals(4L, rotated.get(0, 0));
            assertEquals(3L, rotated.get(0, 1));
            assertEquals(2L, rotated.get(1, 0));
            assertEquals(1L, rotated.get(1, 1));
        }

        @Test
        public void test_rotate270() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(2L, rotated.get(0, 0));
            assertEquals(4L, rotated.get(0, 1));
            assertEquals(1L, rotated.get(1, 0));
            assertEquals(3L, rotated.get(1, 1));
        }

        // ============ Transpose Test ============

        @Test
        public void test_transpose() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1L, transposed.get(0, 0));
            assertEquals(4L, transposed.get(0, 1));
            assertEquals(2L, transposed.get(1, 0));
            assertEquals(5L, transposed.get(1, 1));
        }

        @Test
        public void test_transpose_square() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1L, transposed.get(0, 0));
            assertEquals(3L, transposed.get(0, 1));
        }

        // ============ Reshape Test ============

        @Test
        public void test_reshape() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            LongMatrix reshaped = m.reshape(3, 2);
            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1L, reshaped.get(0, 0));
            assertEquals(2L, reshaped.get(0, 1));
            assertEquals(3L, reshaped.get(1, 0));
            assertEquals(4L, reshaped.get(1, 1));
        }

        @Test
        public void test_reshape_expandWithZeroFill() {
            // Test that reshape() can expand to larger dimensions, filling new cells with zeros
            // as documented in the javadoc
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix reshaped = m.reshape(3, 3); // Expand from 2x2 (4 elements) to 3x3 (9 elements)
            assertEquals(3, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            // Verify original elements are preserved
            assertEquals(1L, reshaped.get(0, 0));
            assertEquals(2L, reshaped.get(0, 1));
            assertEquals(3L, reshaped.get(0, 2));
            assertEquals(4L, reshaped.get(1, 0));
            // Verify new cells are filled with zeros
            assertEquals(0L, reshaped.get(1, 1));
            assertEquals(0L, reshaped.get(1, 2));
            assertEquals(0L, reshaped.get(2, 0));
            assertEquals(0L, reshaped.get(2, 1));
            assertEquals(0L, reshaped.get(2, 2));
        }

        // ============ Repelem Test ============

        @Test
        public void test_repeatElements() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix result = m.repeatElements(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1L, result.get(0, 0));
            assertEquals(1L, result.get(0, 1));
            assertEquals(1L, result.get(1, 0));
            assertEquals(1L, result.get(1, 1));
            assertEquals(2L, result.get(0, 2));
        }

        @Test
        public void test_repeatElements_invalidRepeats() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
        }

        // ============ Repmat Test ============

        @Test
        public void test_repeatMatrix() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix result = m.repeatMatrix(2, 2);
            assertEquals(2, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1L, result.get(0, 0));
            assertEquals(2L, result.get(0, 1));
            assertEquals(1L, result.get(0, 2));
            assertEquals(2L, result.get(0, 3));
        }

        @Test
        public void test_repeatMatrix_invalidRepeats() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
        }

        // ============ Flatten Test ============

        @Test
        public void test_flatten() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongList flat = m.flatten();
            assertEquals(4, flat.size());
            assertEquals(1L, flat.get(0));
            assertEquals(2L, flat.get(1));
            assertEquals(3L, flat.get(2));
            assertEquals(4L, flat.get(3));
        }

        // ============ FlatOp Test ============

        @Test
        public void test_applyOnFlattened() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            AtomicInteger count = new AtomicInteger(0);
            m.applyOnFlattened(row -> count.addAndGet(row.length));
            assertEquals(4, count.get());
        }

        // ============ Vstack and Hstack Tests ============

        @Test
        public void test_stackVertically() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L } });
            LongMatrix result = m1.stackVertically(m2);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1L, result.get(0, 0));
            assertEquals(3L, result.get(1, 0));
        }

        @Test
        public void test_vstack_incompatibleCols() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void test_stackHorizontally() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L }, { 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L }, { 4L } });
            LongMatrix result = m1.stackHorizontally(m2);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1L, result.get(0, 0));
            assertEquals(3L, result.get(0, 1));
        }

        @Test
        public void test_hstack_incompatibleRows() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L }, { 4L } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Operations Tests ============

        @Test
        public void test_add() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix result = m1.add(m2);
            assertEquals(6L, result.get(0, 0));
            assertEquals(8L, result.get(0, 1));
            assertEquals(10L, result.get(1, 0));
            assertEquals(12L, result.get(1, 1));
        }

        @Test
        public void test_add_incompatibleDimensions() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void test_subtract() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix result = m1.subtract(m2);
            assertEquals(4L, result.get(0, 0));
            assertEquals(4L, result.get(0, 1));
            assertEquals(4L, result.get(1, 0));
            assertEquals(4L, result.get(1, 1));
        }

        @Test
        public void test_subtract_incompatibleDimensions() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L } });
            assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
        }

        @Test
        public void test_multiply() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 2L, 0L }, { 1L, 2L } });
            LongMatrix result = m1.multiply(m2);
            assertEquals(4L, result.get(0, 0));
            assertEquals(4L, result.get(0, 1));
            assertEquals(10L, result.get(1, 0));
            assertEquals(8L, result.get(1, 1));
        }

        @Test
        public void test_multiply_incompatibleDimensions() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        // ============ Boxed Test ============

        @Test
        public void test_boxed() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            Matrix<Long> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(1L, boxed.get(0, 0));
            assertEquals(4L, boxed.get(1, 1));
        }

        // ============ Conversion Tests ============

        @Test
        public void test_toFloatMatrix() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            FloatMatrix result = m.toFloatMatrix();
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1.0f, result.get(0, 0), 0.0f);
            assertEquals(4.0f, result.get(1, 1), 0.0f);
        }

        @Test
        public void test_toDoubleMatrix() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            DoubleMatrix result = m.toDoubleMatrix();
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1.0, result.get(0, 0), 0.0);
            assertEquals(4.0, result.get(1, 1), 0.0);
        }

        // ============ ZipWith Tests ============

        @Test
        public void test_zipWith_binary() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
            LongMatrix result = m1.zipWith(m2, (a, b) -> a * b);
            assertEquals(5L, result.get(0, 0));
            assertEquals(12L, result.get(0, 1));
            assertEquals(21L, result.get(1, 0));
            assertEquals(32L, result.get(1, 1));
        }

        @Test
        public void test_zipWith_ternary() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L } });
            LongMatrix m3 = LongMatrix.of(new long[][] { { 5L, 6L } });
            LongMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
            assertEquals(9L, result.get(0, 0));
            assertEquals(12L, result.get(0, 1));
        }

        // ============ Stream Tests ============

        @Test
        public void test_streamHorizontal() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long sum = m.streamHorizontal().sum();
            assertEquals(10L, sum);
        }

        @Test
        public void test_streamH_byRowIndex() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long sum = m.streamHorizontal(0).sum();
            assertEquals(3L, sum);
        }

        @Test
        public void test_streamH_withRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            long sum = m.streamHorizontal(1, 3).sum();
            assertEquals(18L, sum);
        }

        @Test
        public void test_streamVertical() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long sum = m.streamVertical().sum();
            assertEquals(10L, sum);
        }

        @Test
        public void test_streamV_byColumnIndex() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long sum = m.streamVertical(0).sum();
            assertEquals(4L, sum);
        }

        @Test
        public void test_streamV_withRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
            long sum = m.streamVertical(1, 3).sum();
            assertEquals(16L, sum);
        }

        @Test
        public void test_streamMainDiagonal() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long sum = m.streamMainDiagonal().sum();
            assertEquals(5L, sum);
        }

        @Test
        public void test_streamAntiDiagonal() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long sum = m.streamAntiDiagonal().sum();
            assertEquals(5L, sum);
        }

        @Test
        public void test_streamRows() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long count = m.streamRows().count();
            assertEquals(2, count);
        }

        @Test
        public void test_streamR_withRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
            long count = m.streamRows(1, 3).count();
            assertEquals(2, count);
        }

        @Test
        public void test_streamColumns() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            long count = m.streamColumns().count();
            assertEquals(2, count);
        }

        @Test
        public void test_streamC_withRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
            long count = m.streamColumns(1, 3).count();
            assertEquals(2, count);
        }

        // ============ ForEach Tests ============

        @Test
        public void test_forEach() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            AtomicInteger count = new AtomicInteger(0);
            m.forEach(x -> count.incrementAndGet());
            assertEquals(4, count.get());
        }

        @Test
        public void test_forEach_withRange() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
            AtomicInteger count = new AtomicInteger(0);
            m.forEach(1, 3, 1, 3, x -> count.incrementAndGet());
            assertEquals(4, count.get());
        }

        // ============ Utility Tests ============

        @Test
        public void test_println() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            String result = m.println();
            assertNotNull(result);
            assertTrue(result.length() > 0);
        }

        @Test
        public void test_hashCode_consistent() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void test_equals_same() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            assertEquals(m1, m2);
        }

        @Test
        public void test_equals_different() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 5L } });
            assertNotEquals(m1, m2);
        }

        @Test
        public void test_equals_null() {
            LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
            assertNotEquals(m1, null);
        }

        @Test
        public void test_toString() {
            LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.length() > 0);
        }
    }

    @Nested
    class MatrixZeroColumnRowStreamTest_LongMatrix extends TestBase {
        @Test
        public void testLongMatrixRowsForZeroColumnMatrix() {
            final LongMatrix matrix = LongMatrix.of(new long[][] { {}, {}, {} });
            final List<long[]> rows = matrix.streamRows().map(LongStream::toArray).toList();

            assertEquals(3, rows.size());
            assertArrayEquals(new long[0], rows.get(0));
            assertArrayEquals(new long[0], rows.get(1));
            assertArrayEquals(new long[0], rows.get(2));
        }
    }

    @Nested
    class PrimitiveMatrixUpdateAllNullValidationTest_LongMatrix extends TestBase {
        @Test
        public void testLongMatrixUpdateAllNullOperator() {
            LongMatrix matrix = LongMatrix.of(new long[][] { { 1, 2 }, { 3, 4 } });
            LongMatrix emptyLike = LongMatrix.of(new long[][] { {}, {} });
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.LongUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Long, RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.LongUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.LongUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.LongPredicate<RuntimeException>) null, 0L));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0L));

            assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.LongUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.LongUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.LongPredicate<RuntimeException>) null, 0L));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0L));
        }

        @Test
        public void testLongMatrixBinaryOpsRejectNullMatrix() {
            LongMatrix matrix = LongMatrix.of(new long[][] { { 1L } });
            assertThrows(IllegalArgumentException.class, () -> matrix.stackVertically(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.stackHorizontally(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.add(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.subtract(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.multiply(null));
        }
    }

}
