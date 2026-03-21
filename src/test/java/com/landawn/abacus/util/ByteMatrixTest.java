package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalByte;
import com.landawn.abacus.util.stream.ByteIteratorEx;
import com.landawn.abacus.util.stream.ByteStream;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

class ByteMatrixTest extends TestBase {

    @Test
    public void testConstructor() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = new ByteMatrix(a);
        Assertions.assertEquals(2, matrix.rowCount());
        Assertions.assertEquals(2, matrix.columnCount());

        ByteMatrix nullMatrix = new ByteMatrix(null);
        Assertions.assertEquals(0, nullMatrix.rowCount());
        Assertions.assertEquals(0, nullMatrix.columnCount());
    }

    @Test
    public void testEmpty() {
        ByteMatrix empty = ByteMatrix.empty();
        Assertions.assertEquals(0, empty.rowCount());
        Assertions.assertEquals(0, empty.columnCount());
        Assertions.assertTrue(empty.isEmpty());
    }

    @Test
    public void testOf() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);
        Assertions.assertEquals(2, matrix.rowCount());
        Assertions.assertEquals(2, matrix.columnCount());

        ByteMatrix emptyMatrix = ByteMatrix.of();
        Assertions.assertTrue(emptyMatrix.isEmpty());

        ByteMatrix nullMatrix = ByteMatrix.of((byte[][]) null);
        Assertions.assertTrue(nullMatrix.isEmpty());
    }

    @Test
    public void testRandom() {
        ByteMatrix matrix = ByteMatrix.random(5);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(5, matrix.columnCount());
    }

    @Test
    public void testRepeat() {
        ByteMatrix matrix = ByteMatrix.repeat(1, 4, (byte) 7);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(4, matrix.columnCount());
        for (int i = 0; i < 4; i++) {
            Assertions.assertEquals(7, matrix.get(0, i));
        }
    }

    @Test
    public void testRange() {
        ByteMatrix matrix = ByteMatrix.range((byte) 1, (byte) 5);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(4, matrix.columnCount());
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(0, 2));
        Assertions.assertEquals(4, matrix.get(0, 3));
    }

    @Test
    public void testRangeWithStep() {
        ByteMatrix matrix = ByteMatrix.range((byte) 0, (byte) 10, (byte) 2);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(5, matrix.columnCount());
        Assertions.assertEquals(0, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(4, matrix.get(0, 2));
        Assertions.assertEquals(6, matrix.get(0, 3));
        Assertions.assertEquals(8, matrix.get(0, 4));
    }

    @Test
    public void testRangeClosed() {
        ByteMatrix matrix = ByteMatrix.rangeClosed((byte) 1, (byte) 4);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(4, matrix.columnCount());
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(4, matrix.get(0, 3));
    }

    @Test
    public void testRangeClosedWithStep() {
        ByteMatrix matrix = ByteMatrix.rangeClosed((byte) 0, (byte) 9, (byte) 3);
        Assertions.assertEquals(1, matrix.rowCount());
        Assertions.assertEquals(4, matrix.columnCount());
        Assertions.assertEquals(0, matrix.get(0, 0));
        Assertions.assertEquals(3, matrix.get(0, 1));
        Assertions.assertEquals(4, matrix.columnCount());
        Assertions.assertEquals(0, matrix.get(0, 0));
        Assertions.assertEquals(3, matrix.get(0, 1));
        Assertions.assertEquals(6, matrix.get(0, 2));
        Assertions.assertEquals(9, matrix.get(0, 3));
    }

    @Test
    public void testDiagonalLU2RD() {
        byte[] diagonal = { 1, 2, 3 };
        ByteMatrix matrix = ByteMatrix.mainDiagonal(diagonal);
        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3, matrix.get(2, 2));
        Assertions.assertEquals(0, matrix.get(0, 1));
    }

    @Test
    public void testDiagonalRU2LD() {
        byte[] diagonal = { 1, 2, 3 };
        ByteMatrix matrix = ByteMatrix.antiDiagonal(diagonal);
        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals(1, matrix.get(0, 2));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3, matrix.get(2, 0));
        Assertions.assertEquals(0, matrix.get(0, 0));
    }

    @Test
    public void testDiagonal() {
        byte[] main = { 1, 2, 3 };
        byte[] anti = { 4, 5, 6 };
        ByteMatrix matrix = ByteMatrix.diagonals(main, anti);
        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3, matrix.get(2, 2));
        Assertions.assertEquals(4, matrix.get(0, 2));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(6, matrix.get(2, 0));

        ByteMatrix emptyMatrix = ByteMatrix.diagonals(null, null);
        Assertions.assertTrue(emptyMatrix.isEmpty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> ByteMatrix.diagonals(new byte[] { 1 }, new byte[] { 2, 3 }));
    }

    @Test
    public void testUnbox() {
        Byte[][] boxed = { { 1, 2 }, { 3, 4 } };
        Matrix<Byte> boxedMatrix = Matrix.of(boxed);
        ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
        Assertions.assertEquals(2, unboxed.rowCount());
        Assertions.assertEquals(2, unboxed.columnCount());
        Assertions.assertEquals(1, unboxed.get(0, 0));
        Assertions.assertEquals(4, unboxed.get(1, 1));
    }

    @Test
    public void testComponentType() {
        ByteMatrix matrix = ByteMatrix.empty();
        Assertions.assertEquals(byte.class, matrix.componentType());
    }

    @Test
    public void testGet() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(1, 0));
        Assertions.assertEquals(4, matrix.get(1, 1));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(2, 0));
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(0, 2));
    }

    @Test
    public void testGetWithPoint() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);
        Assertions.assertEquals(1, matrix.get(Point.of(0, 0)));
        Assertions.assertEquals(4, matrix.get(Point.of(1, 1)));
    }

    @Test
    public void testSet() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);
        matrix.set(0, 0, (byte) 5);
        Assertions.assertEquals(5, matrix.get(0, 0));

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.set(2, 0, (byte) 5));
    }

    @Test
    public void testSetWithPoint() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);
        matrix.set(Point.of(1, 1), (byte) 5);
        Assertions.assertEquals(5, matrix.get(1, 1));
    }

    @Test
    public void testUpOf() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        OptionalByte up = matrix.above(1, 0);
        Assertions.assertTrue(up.isPresent());
        Assertions.assertEquals(1, up.get());

        OptionalByte empty = matrix.above(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        OptionalByte down = matrix.below(0, 0);
        Assertions.assertTrue(down.isPresent());
        Assertions.assertEquals(3, down.get());

        OptionalByte empty = matrix.below(1, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        OptionalByte left = matrix.left(0, 1);
        Assertions.assertTrue(left.isPresent());
        Assertions.assertEquals(1, left.get());

        OptionalByte empty = matrix.left(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        OptionalByte right = matrix.right(0, 0);
        Assertions.assertTrue(right.isPresent());
        Assertions.assertEquals(2, right.get());

        OptionalByte empty = matrix.right(0, 1);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testRow() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] row0 = matrix.rowView(0);
        Assertions.assertArrayEquals(new byte[] { 1, 2, 3 }, row0);

        byte[] row1 = matrix.rowView(1);
        Assertions.assertArrayEquals(new byte[] { 4, 5, 6 }, row1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.rowView(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.rowView(2));
    }

    // Verify rowCopy returns a defensive row snapshot and enforces row bounds.
    @Test
    public void testRowCopy() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] rowCopy = matrix.rowCopy(0);
        Assertions.assertArrayEquals(new byte[] { 1, 2, 3 }, rowCopy);

        rowCopy[0] = 9;
        Assertions.assertArrayEquals(new byte[] { 1, 2, 3 }, matrix.rowView(0));
    }

    @Test
    public void testRowCopy_InvalidIndex() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.rowCopy(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.rowCopy(2));
    }

    @Test
    public void testColumn() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] col0 = matrix.columnCopy(0);
        Assertions.assertArrayEquals(new byte[] { 1, 4 }, col0);

        byte[] col1 = matrix.columnCopy(1);
        Assertions.assertArrayEquals(new byte[] { 2, 5 }, col1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(3));
    }

    @Test
    public void testSetRow() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.setRow(0, new byte[] { 7, 8, 9 });
        Assertions.assertArrayEquals(new byte[] { 7, 8, 9 }, matrix.rowView(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setRow(0, new byte[] { 1, 2 }));
    }

    @Test
    public void testSetColumn() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.setColumn(0, new byte[] { 7, 8 });
        Assertions.assertArrayEquals(new byte[] { 7, 8 }, matrix.columnCopy(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setColumn(0, new byte[] { 1 }));
    }

    @Test
    public void testUpdateRow() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.updateRow(0, b -> (byte) (b * 2));
        Assertions.assertArrayEquals(new byte[] { 2, 4, 6 }, matrix.rowView(0));
    }

    @Test
    public void testUpdateColumn() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.updateColumn(1, b -> (byte) (b + 10));
        Assertions.assertArrayEquals(new byte[] { 12, 15 }, matrix.columnCopy(1));
    }

    @Test
    public void testUpdateRowAndUpdateColumnInvalidIndex() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateRow(-1, b -> (byte) (b * 2)));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateColumn(3, b -> (byte) (b + 10)));
    }

    @Test
    public void testUpdateRowAndUpdateColumnNullOperator() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateRow(0, (Throwables.ByteUnaryOperator<RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateColumn(0, (Throwables.ByteUnaryOperator<RuntimeException>) null));
    }

    @Test
    public void testGetLU2RD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] diagonal = matrix.getMainDiagonal();
        Assertions.assertArrayEquals(new byte[] { 1, 5, 9 }, diagonal);

        ByteMatrix nonSquare = ByteMatrix.of(new byte[][] { { 1, 2 } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.getMainDiagonal());
    }

    @Test
    public void testSetLU2RD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.setMainDiagonal(new byte[] { 10, 11, 12 });
        Assertions.assertEquals(10, matrix.get(0, 0));
        Assertions.assertEquals(11, matrix.get(1, 1));
        Assertions.assertEquals(12, matrix.get(2, 2));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setMainDiagonal(new byte[] { 1, 2 }));
    }

    @Test
    public void testUpdateLU2RD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.updateMainDiagonal(b -> (byte) (b * 2));
        Assertions.assertEquals(2, matrix.get(0, 0));
        Assertions.assertEquals(10, matrix.get(1, 1));
        Assertions.assertEquals(18, matrix.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] diagonal = matrix.getAntiDiagonal();
        Assertions.assertArrayEquals(new byte[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testSetRU2LD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.setAntiDiagonal(new byte[] { 10, 11, 12 });
        Assertions.assertEquals(10, matrix.get(0, 2));
        Assertions.assertEquals(11, matrix.get(1, 1));
        Assertions.assertEquals(12, matrix.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.updateAntiDiagonal(b -> (byte) (b + 1));
        Assertions.assertEquals(4, matrix.get(0, 2));
        Assertions.assertEquals(6, matrix.get(1, 1));
        Assertions.assertEquals(8, matrix.get(2, 0));
    }

    @Test
    public void testUpdateAll() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.updateAll(b -> (byte) (b * 2));
        Assertions.assertEquals(2, matrix.get(0, 0));
        Assertions.assertEquals(4, matrix.get(0, 1));
        Assertions.assertEquals(6, matrix.get(1, 0));
        Assertions.assertEquals(8, matrix.get(1, 1));
    }

    @Test
    public void testUpdateAllWithIndices() {
        byte[][] a = { { 0, 0 }, { 0, 0 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.updateAll((i, j) -> (byte) (i + j));
        Assertions.assertEquals(0, matrix.get(0, 0));
        Assertions.assertEquals(1, matrix.get(0, 1));
        Assertions.assertEquals(1, matrix.get(1, 0));
        Assertions.assertEquals(2, matrix.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.replaceIf(b -> b % 2 == 0, (byte) 0);
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(0, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(0, 2));
        Assertions.assertEquals(0, matrix.get(1, 0));
    }

    @Test
    public void testReplaceIfWithIndices() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.replaceIf((i, j) -> i == j, (byte) 0);
        Assertions.assertEquals(0, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(1, 0));
        Assertions.assertEquals(0, matrix.get(1, 1));
    }

    @Test
    public void testMap() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix doubled = matrix.map(b -> (byte) (b * 2));
        Assertions.assertEquals(2, doubled.get(0, 0));
        Assertions.assertEquals(4, doubled.get(0, 1));
        Assertions.assertEquals(6, doubled.get(1, 0));
        Assertions.assertEquals(8, doubled.get(1, 1));
    }

    @Test
    public void testMapNullMapper() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.map((Throwables.ByteUnaryOperator<RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.mapToObj((Throwables.ByteFunction<String, RuntimeException>) null, String.class));
    }

    @Test
    public void testMapToObj() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        Matrix<String> stringMatrix = matrix.mapToObj(b -> "Value: " + b, String.class);
        Assertions.assertEquals("Value: 1", stringMatrix.get(0, 0));
        Assertions.assertEquals("Value: 4", stringMatrix.get(1, 1));
    }

    @Test
    public void testFill() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.fill((byte) 5);
        Assertions.assertEquals(5, matrix.get(0, 0));
        Assertions.assertEquals(5, matrix.get(0, 1));
        Assertions.assertEquals(5, matrix.get(1, 0));
        Assertions.assertEquals(5, matrix.get(1, 1));
    }

    @Test
    public void testFillWithArray() {
        byte[][] a = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[][] b = { { 1, 2 }, { 3, 4 } };
        matrix.copyFrom(b);
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(1, 0));
        Assertions.assertEquals(4, matrix.get(1, 1));
        Assertions.assertEquals(0, matrix.get(2, 2)); // unchanged
    }

    @Test
    public void testFillWithIndices() {
        byte[][] a = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[][] b = { { 1, 2 } };
        matrix.copyFrom(1, 1, b);
        Assertions.assertEquals(0, matrix.get(0, 0)); // unchanged
        Assertions.assertEquals(1, matrix.get(1, 1));
        Assertions.assertEquals(2, matrix.get(1, 2));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.copyFrom(-1, 0, b));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.copyFrom(0, -1, b));
    }

    @Test
    public void testCopy() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);
        ByteMatrix copy = matrix.copy();

        Assertions.assertEquals(matrix.rowCount(), copy.rowCount());
        Assertions.assertEquals(matrix.columnCount(), copy.columnCount());
        Assertions.assertEquals(1, copy.get(0, 0));
        Assertions.assertEquals(4, copy.get(1, 1));

        copy.set(0, 0, (byte) 9);
        Assertions.assertEquals(1, matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testCopyWithRowRange() {
        byte[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);
        ByteMatrix copy = matrix.copy(0, 2);

        Assertions.assertEquals(2, copy.rowCount());
        Assertions.assertEquals(2, copy.columnCount());
        Assertions.assertEquals(1, copy.get(0, 0));
        Assertions.assertEquals(4, copy.get(1, 1));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(-1, 2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(0, 4));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(2, 1));
    }

    @Test
    public void testCopyWithFullRange() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);
        ByteMatrix copy = matrix.copy(0, 2, 1, 3);

        Assertions.assertEquals(2, copy.rowCount());
        Assertions.assertEquals(2, copy.columnCount());
        Assertions.assertEquals(2, copy.get(0, 0));
        Assertions.assertEquals(3, copy.get(0, 1));
        Assertions.assertEquals(5, copy.get(1, 0));
        Assertions.assertEquals(6, copy.get(1, 1));
    }

    @Test
    public void testExtend() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix extended = matrix.resize(3, 3);
        Assertions.assertEquals(3, extended.rowCount());
        Assertions.assertEquals(3, extended.columnCount());
        Assertions.assertEquals(1, extended.get(0, 0));
        Assertions.assertEquals(4, extended.get(1, 1));
        Assertions.assertEquals(0, extended.get(2, 2));

        ByteMatrix truncated = matrix.resize(1, 1);
        Assertions.assertEquals(1, truncated.rowCount());
        Assertions.assertEquals(1, truncated.columnCount());
        Assertions.assertEquals(1, truncated.get(0, 0));
    }

    @Test
    public void testExtendWithDefaultValue() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix extended = matrix.resize(3, 3, (byte) 9);
        Assertions.assertEquals(9, extended.get(2, 2));
        Assertions.assertEquals(9, extended.get(0, 2));
        Assertions.assertEquals(9, extended.get(2, 0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.resize(-1, 2, (byte) 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.resize(2, -1, (byte) 0));
    }

    @Test
    public void testExtendInAllDirections() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix extended = matrix.extend(1, 1, 1, 1);
        Assertions.assertEquals(4, extended.rowCount());
        Assertions.assertEquals(4, extended.columnCount());
        Assertions.assertEquals(1, extended.get(1, 1));
        Assertions.assertEquals(4, extended.get(2, 2));
        Assertions.assertEquals(0, extended.get(0, 0));
    }

    @Test
    public void testExtendInAllDirectionsWithDefaultValue() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix extended = matrix.extend(1, 1, 1, 1, (byte) 9);
        Assertions.assertEquals(9, extended.get(0, 0));
        Assertions.assertEquals(9, extended.get(0, 3));
        Assertions.assertEquals(9, extended.get(3, 0));
        Assertions.assertEquals(9, extended.get(3, 3));
        Assertions.assertEquals(1, extended.get(1, 1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 0, 0, 0, (byte) 0));
    }

    @Test
    public void testReverseH() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.flipInPlaceHorizontally();
        Assertions.assertEquals(3, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(1, matrix.get(0, 2));
        Assertions.assertEquals(6, matrix.get(1, 0));
    }

    @Test
    public void testReverseV() {
        byte[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.flipInPlaceVertically();
        Assertions.assertEquals(5, matrix.get(0, 0));
        Assertions.assertEquals(6, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(1, 0));
        Assertions.assertEquals(1, matrix.get(2, 0));
    }

    @Test
    public void testFlipH() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix flipped = matrix.flipHorizontally();
        Assertions.assertEquals(3, flipped.get(0, 0));
        Assertions.assertEquals(1, flipped.get(0, 2));
        Assertions.assertEquals(1, matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testFlipV() {
        byte[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix flipped = matrix.flipVertically();
        Assertions.assertEquals(5, flipped.get(0, 0));
        Assertions.assertEquals(1, flipped.get(2, 0));
        Assertions.assertEquals(1, matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testRotate90() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix rotated = matrix.rotate90();
        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(2, rotated.columnCount());
        Assertions.assertEquals(3, rotated.get(0, 0));
        Assertions.assertEquals(1, rotated.get(0, 1));
        Assertions.assertEquals(4, rotated.get(1, 0));
        Assertions.assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix rotated = matrix.rotate180();
        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(2, rotated.columnCount());
        Assertions.assertEquals(4, rotated.get(0, 0));
        Assertions.assertEquals(3, rotated.get(0, 1));
        Assertions.assertEquals(2, rotated.get(1, 0));
        Assertions.assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix rotated = matrix.rotate270();
        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(2, rotated.columnCount());
        Assertions.assertEquals(2, rotated.get(0, 0));
        Assertions.assertEquals(4, rotated.get(0, 1));
        Assertions.assertEquals(1, rotated.get(1, 0));
        Assertions.assertEquals(3, rotated.get(1, 1));
    }

    @Test
    public void testTranspose() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix transposed = matrix.transpose();
        Assertions.assertEquals(3, transposed.rowCount());
        Assertions.assertEquals(2, transposed.columnCount());
        Assertions.assertEquals(1, transposed.get(0, 0));
        Assertions.assertEquals(4, transposed.get(0, 1));
        Assertions.assertEquals(2, transposed.get(1, 0));
        Assertions.assertEquals(5, transposed.get(1, 1));
    }

    @Test
    public void testReshape() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix reshaped = matrix.reshape(3, 2);
        Assertions.assertEquals(3, reshaped.rowCount());
        Assertions.assertEquals(2, reshaped.columnCount());
        Assertions.assertEquals(1, reshaped.get(0, 0));
        Assertions.assertEquals(2, reshaped.get(0, 1));
        Assertions.assertEquals(3, reshaped.get(1, 0));
        Assertions.assertEquals(4, reshaped.get(1, 1));

        ByteMatrix empty = ByteMatrix.empty().reshape(2, 3);
        Assertions.assertEquals(2, empty.rowCount());
        Assertions.assertEquals(3, empty.columnCount());

        // Test reshape with too-small dimensions throws exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.reshape(1, 4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.reshape(2, 2));
    }

    @Test
    public void testRepelem() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix repeated = matrix.repeatElements(2, 3);
        Assertions.assertEquals(4, repeated.rowCount());
        Assertions.assertEquals(6, repeated.columnCount());
        Assertions.assertEquals(1, repeated.get(0, 0));
        Assertions.assertEquals(1, repeated.get(0, 2));
        Assertions.assertEquals(1, repeated.get(1, 0));
        Assertions.assertEquals(2, repeated.get(0, 3));
        Assertions.assertEquals(4, repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatElements(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatElements(1, 0));
    }

    @Test
    public void testRepmat() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix repeated = matrix.repeatMatrix(2, 3);
        Assertions.assertEquals(4, repeated.rowCount());
        Assertions.assertEquals(6, repeated.columnCount());
        Assertions.assertEquals(1, repeated.get(0, 0));
        Assertions.assertEquals(1, repeated.get(0, 2));
        Assertions.assertEquals(1, repeated.get(0, 4));
        Assertions.assertEquals(1, repeated.get(2, 0));
        Assertions.assertEquals(4, repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatMatrix(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repeatMatrix(1, 0));
    }

    @Test
    public void testFlatten() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteList flat = matrix.flatten();
        Assertions.assertEquals(6, flat.size());
        Assertions.assertEquals(1, flat.get(0));
        Assertions.assertEquals(2, flat.get(1));
        Assertions.assertEquals(3, flat.get(2));
        Assertions.assertEquals(4, flat.get(3));
        Assertions.assertEquals(5, flat.get(4));
        Assertions.assertEquals(6, flat.get(5));
    }

    @Test
    public void testFlatOp() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        List<Byte> collected = new ArrayList<>();
        matrix.applyOnFlattened(row -> {
            for (byte b : row) {
                collected.add(b);
            }
        });

        Assertions.assertEquals(4, collected.size());
        Assertions.assertTrue(collected.contains((byte) 1));
        Assertions.assertTrue(collected.contains((byte) 4));
    }

    @Test
    public void testVstack() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        byte[][] b = { { 5, 6 }, { 7, 8 } };
        ByteMatrix matrixA = ByteMatrix.of(a);
        ByteMatrix matrixB = ByteMatrix.of(b);

        ByteMatrix stacked = matrixA.stackVertically(matrixB);
        Assertions.assertEquals(4, stacked.rowCount());
        Assertions.assertEquals(2, stacked.columnCount());
        Assertions.assertEquals(1, stacked.get(0, 0));
        Assertions.assertEquals(5, stacked.get(2, 0));

        ByteMatrix differentCols = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.stackVertically(differentCols));
    }

    @Test
    public void testHstack() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        byte[][] b = { { 5, 6 }, { 7, 8 } };
        ByteMatrix matrixA = ByteMatrix.of(a);
        ByteMatrix matrixB = ByteMatrix.of(b);

        ByteMatrix stacked = matrixA.stackHorizontally(matrixB);
        Assertions.assertEquals(2, stacked.rowCount());
        Assertions.assertEquals(4, stacked.columnCount());
        Assertions.assertEquals(1, stacked.get(0, 0));
        Assertions.assertEquals(5, stacked.get(0, 2));

        ByteMatrix differentRows = ByteMatrix.of(new byte[][] { { 1 }, { 2 }, { 3 } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.stackHorizontally(differentRows));
    }

    @Test
    public void testAdd() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        byte[][] b = { { 5, 6 }, { 7, 8 } };
        ByteMatrix matrixA = ByteMatrix.of(a);
        ByteMatrix matrixB = ByteMatrix.of(b);

        ByteMatrix sum = matrixA.add(matrixB);
        Assertions.assertEquals(6, sum.get(0, 0));
        Assertions.assertEquals(8, sum.get(0, 1));
        Assertions.assertEquals(10, sum.get(1, 0));
        Assertions.assertEquals(12, sum.get(1, 1));

        ByteMatrix differentShape = ByteMatrix.of(new byte[][] { { 1 } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.add(differentShape));
    }

    @Test
    public void testSubtract() {
        byte[][] a = { { 5, 6 }, { 7, 8 } };
        byte[][] b = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrixA = ByteMatrix.of(a);
        ByteMatrix matrixB = ByteMatrix.of(b);

        ByteMatrix diff = matrixA.subtract(matrixB);
        Assertions.assertEquals(4, diff.get(0, 0));
        Assertions.assertEquals(4, diff.get(0, 1));
        Assertions.assertEquals(4, diff.get(1, 0));
        Assertions.assertEquals(4, diff.get(1, 1));

        ByteMatrix differentShape = ByteMatrix.of(new byte[][] { { 1 } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.subtract(differentShape));
    }

    @Test
    public void testMultiply() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        byte[][] b = { { 5, 6 }, { 7, 8 } };
        ByteMatrix matrixA = ByteMatrix.of(a);
        ByteMatrix matrixB = ByteMatrix.of(b);

        ByteMatrix product = matrixA.multiply(matrixB);
        Assertions.assertEquals(2, product.rowCount());
        Assertions.assertEquals(2, product.columnCount());
        Assertions.assertEquals(19, product.get(0, 0));
        Assertions.assertEquals(22, product.get(0, 1));
        Assertions.assertEquals(43, product.get(1, 0));
        Assertions.assertEquals(50, product.get(1, 1));

        ByteMatrix incompatible = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.multiply(incompatible));
    }

    @Test
    public void testBoxed() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        Matrix<Byte> boxed = matrix.boxed();
        Assertions.assertEquals(2, boxed.rowCount());
        Assertions.assertEquals(2, boxed.columnCount());
        Assertions.assertEquals(Byte.valueOf((byte) 1), boxed.get(0, 0));
        Assertions.assertEquals(Byte.valueOf((byte) 4), boxed.get(1, 1));
    }

    @Test
    public void testToIntMatrix() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        IntMatrix intMatrix = matrix.toIntMatrix();
        Assertions.assertEquals(2, intMatrix.rowCount());
        Assertions.assertEquals(2, intMatrix.columnCount());
        Assertions.assertEquals(1, intMatrix.get(0, 0));
        Assertions.assertEquals(4, intMatrix.get(1, 1));
    }

    @Test
    public void testToLongMatrix() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        LongMatrix longMatrix = matrix.toLongMatrix();
        Assertions.assertEquals(2, longMatrix.rowCount());
        Assertions.assertEquals(2, longMatrix.columnCount());
        Assertions.assertEquals(1L, longMatrix.get(0, 0));
        Assertions.assertEquals(4L, longMatrix.get(1, 1));
    }

    @Test
    public void testToFloatMatrix() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        FloatMatrix floatMatrix = matrix.toFloatMatrix();
        Assertions.assertEquals(2, floatMatrix.rowCount());
        Assertions.assertEquals(2, floatMatrix.columnCount());
        Assertions.assertEquals(1.0f, floatMatrix.get(0, 0));
        Assertions.assertEquals(4.0f, floatMatrix.get(1, 1));
    }

    @Test
    public void testToDoubleMatrix() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        DoubleMatrix doubleMatrix = matrix.toDoubleMatrix();
        Assertions.assertEquals(2, doubleMatrix.rowCount());
        Assertions.assertEquals(2, doubleMatrix.columnCount());
        Assertions.assertEquals(1.0, doubleMatrix.get(0, 0));
        Assertions.assertEquals(4.0, doubleMatrix.get(1, 1));
    }

    @Test
    public void testZipWith() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        byte[][] b = { { 5, 6 }, { 7, 8 } };
        ByteMatrix matrixA = ByteMatrix.of(a);
        ByteMatrix matrixB = ByteMatrix.of(b);

        ByteMatrix result = matrixA.zipWith(matrixB, (x, y) -> (byte) (x * y));
        Assertions.assertEquals(5, result.get(0, 0));
        Assertions.assertEquals(12, result.get(0, 1));
        Assertions.assertEquals(21, result.get(1, 0));
        Assertions.assertEquals(32, result.get(1, 1));

        ByteMatrix differentShape = ByteMatrix.of(new byte[][] { { 1 } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.zipWith(differentShape, (x, y) -> x));
    }

    @Test
    public void testZipWith3() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        byte[][] b = { { 5, 6 }, { 7, 8 } };
        byte[][] c = { { 9, 10 }, { 11, 12 } };
        ByteMatrix matrixA = ByteMatrix.of(a);
        ByteMatrix matrixB = ByteMatrix.of(b);
        ByteMatrix matrixC = ByteMatrix.of(c);

        ByteMatrix result = matrixA.zipWith(matrixB, matrixC, (x, y, z) -> (byte) (x + y + z));
        Assertions.assertEquals(15, result.get(0, 0));
        Assertions.assertEquals(18, result.get(0, 1));
        Assertions.assertEquals(21, result.get(1, 0));
        Assertions.assertEquals(24, result.get(1, 1));
    }

    @Test
    public void testStreamLU2RD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] diagonal = matrix.streamMainDiagonal().toArray();
        Assertions.assertArrayEquals(new byte[] { 1, 5, 9 }, diagonal);

        ByteMatrix empty = ByteMatrix.empty();
        Assertions.assertTrue(empty.streamMainDiagonal().toList().isEmpty());

        ByteMatrix nonSquare = ByteMatrix.of(new byte[][] { { 1, 2 } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
    }

    @Test
    public void testStreamRU2LD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] diagonal = matrix.streamAntiDiagonal().toArray();
        Assertions.assertArrayEquals(new byte[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testStreamH() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] all = matrix.streamHorizontal().toArray();
        Assertions.assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6 }, all);

        ByteMatrix empty = ByteMatrix.empty();
        Assertions.assertTrue(empty.streamHorizontal().toList().isEmpty());
    }

    @Test
    public void testStreamHRow() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] row1 = matrix.streamHorizontal(1).toArray();
        Assertions.assertArrayEquals(new byte[] { 4, 5, 6 }, row1);
    }

    @Test
    public void testStreamHRange() {
        byte[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] range = matrix.streamHorizontal(1, 3).toArray();
        Assertions.assertArrayEquals(new byte[] { 3, 4, 5, 6 }, range);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(-1, 2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(0, 4));
    }

    @Test
    public void testStreamV() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] all = matrix.streamVertical().toArray();
        Assertions.assertArrayEquals(new byte[] { 1, 4, 2, 5, 3, 6 }, all);
    }

    @Test
    public void testStreamVColumn() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] col1 = matrix.streamVertical(1).toArray();
        Assertions.assertArrayEquals(new byte[] { 2, 5 }, col1);
    }

    @Test
    public void testStreamVRange() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] range = matrix.streamVertical(1, 3).toArray();
        Assertions.assertArrayEquals(new byte[] { 2, 5, 3, 6 }, range);
    }

    @Test
    public void testStreamR() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        List<ByteStream> rows = matrix.streamRows().toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new byte[] { 1, 2 }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new byte[] { 3, 4 }, rows.get(1).toArray());
    }

    @Test
    public void testStreamRRange() {
        byte[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        List<ByteStream> rows = matrix.streamRows(1, 3).toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new byte[] { 3, 4 }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new byte[] { 5, 6 }, rows.get(1).toArray());
    }

    @Test
    public void testStreamC() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        List<ByteStream> columnCount = matrix.streamColumns().toList();
        Assertions.assertEquals(3, columnCount.size());
        Assertions.assertArrayEquals(new byte[] { 1, 4 }, columnCount.get(0).toArray());
        Assertions.assertArrayEquals(new byte[] { 2, 5 }, columnCount.get(1).toArray());
        Assertions.assertArrayEquals(new byte[] { 3, 6 }, columnCount.get(2).toArray());
    }

    @Test
    public void testStreamCRange() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        List<ByteStream> columnCount = matrix.streamColumns(1, 3).toList();
        Assertions.assertEquals(2, columnCount.size());
        Assertions.assertArrayEquals(new byte[] { 2, 5 }, columnCount.get(0).toArray());
        Assertions.assertArrayEquals(new byte[] { 3, 6 }, columnCount.get(1).toArray());
    }

    @Test
    public void testLength() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        // length is protected, cannot test directly from here
        // It's tested indirectly through other operations
        Assertions.assertEquals(2, matrix.columnCount());
    }

    @Test
    public void testForEach() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        List<Byte> collected = new ArrayList<>();
        matrix.forEach(b -> collected.add(b));

        Assertions.assertEquals(4, collected.size());
        Assertions.assertTrue(collected.contains((byte) 1));
        Assertions.assertTrue(collected.contains((byte) 2));
        Assertions.assertTrue(collected.contains((byte) 3));
        Assertions.assertTrue(collected.contains((byte) 4));
    }

    @Test
    public void testForEachWithRange() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        List<Byte> collected = new ArrayList<>();
        matrix.forEach(1, 3, 1, 3, b -> collected.add(b));

        Assertions.assertEquals(4, collected.size());
        Assertions.assertTrue(collected.contains((byte) 5));
        Assertions.assertTrue(collected.contains((byte) 6));
        Assertions.assertTrue(collected.contains((byte) 8));
        Assertions.assertTrue(collected.contains((byte) 9));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.forEach(0, 4, 0, 3, b -> {
        }));
    }

    @Test
    public void testForEachWithForcedParallelMode_EdgeCase() throws Exception {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<Byte> allValues = new ArrayList<>();
        List<Byte> regionValues = new ArrayList<>();

        Matrices.runWithParallelMode(ParallelMode.FORCE_ON, () -> matrix.forEach(e -> {
            synchronized (allValues) {
                allValues.add(e);
            }
        }));

        Matrices.runWithParallelMode(ParallelMode.FORCE_ON, () -> matrix.forEach(0, 2, 1, 3, e -> {
            synchronized (regionValues) {
                regionValues.add(e);
            }
        }));

        Assertions.assertEquals(6, allValues.size());
        Assertions.assertTrue(allValues.containsAll(List.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6)));
        Assertions.assertEquals(4, regionValues.size());
        Assertions.assertTrue(regionValues.containsAll(List.of((byte) 2, (byte) 3, (byte) 5, (byte) 6)));
    }

    @Test
    public void testForEachNullAction() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.forEach((Throwables.ByteConsumer<RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.forEach(0, 1, 0, 2, (Throwables.ByteConsumer<RuntimeException>) null));
    }

    @Test
    public void testPrintln() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        assertFalse(matrix.isEmpty());
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(matrix::println);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testStreamRowsAndColumnsIteratorAdvanceAndExhaustion_EdgeCase() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        var rowIterator = matrix.streamRows(0, 2).iterator();

        Assertions.assertTrue(rowIterator instanceof ObjIteratorEx);

        ObjIteratorEx<ByteStream> rowEx = (ObjIteratorEx<ByteStream>) rowIterator;
        rowEx.advance(0);
        Assertions.assertEquals(2L, rowEx.count());
        Assertions.assertArrayEquals(new byte[] { 1, 2 }, rowEx.next().toArray());
        rowEx.advance(10);
        Assertions.assertEquals(0L, rowEx.count());
        Assertions.assertThrows(NoSuchElementException.class, rowEx::next);

        var columnIterator = matrix.streamColumns(0, 1).iterator();

        Assertions.assertTrue(columnIterator instanceof ObjIteratorEx);

        ObjIteratorEx<ByteStream> columnEx = (ObjIteratorEx<ByteStream>) columnIterator;
        ByteStream firstColumn = columnEx.next();
        var valueIterator = firstColumn.iterator();

        Assertions.assertTrue(valueIterator instanceof ByteIteratorEx);

        ByteIteratorEx byteEx = (ByteIteratorEx) valueIterator;
        byteEx.advance(0);
        Assertions.assertEquals(2L, byteEx.count());
        Assertions.assertEquals((byte) 1, byteEx.nextByte());
        byteEx.advance(10);
        Assertions.assertEquals(0L, byteEx.count());
        Assertions.assertThrows(NoSuchElementException.class, byteEx::nextByte);
    }

    @Test
    public void testPrintlnEmptyMatrix_EdgeCase() {
        Assertions.assertEquals("[]", ByteMatrix.empty().println());
    }

    @Test
    public void testHashCode() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix1 = ByteMatrix.of(a);
        ByteMatrix matrix2 = ByteMatrix.of(a.clone());

        // Same content should have same hash code
        Assertions.assertEquals(matrix1.hashCode(), matrix2.hashCode());
    }

    @Test
    public void testEquals() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix1 = ByteMatrix.of(a);
        ByteMatrix matrix2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix matrix3 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 5 } });
        ByteMatrix matrix4 = ByteMatrix.of(new byte[][] { { 1, 2 } });

        Assertions.assertEquals(matrix1, matrix1); // same instance
        Assertions.assertEquals(matrix1, matrix2); // same content
        Assertions.assertNotEquals(matrix1, matrix3); // different content
        Assertions.assertNotEquals(matrix1, matrix4); // different dimensions
        Assertions.assertNotEquals(matrix1, null);
        Assertions.assertNotEquals(matrix1, "not a matrix");
    }

    @Test
    public void testToString() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        String str = matrix.toString();
        Assertions.assertNotNull(str);
        Assertions.assertTrue(str.contains("1"));
        Assertions.assertTrue(str.contains("4"));
    }

    @Nested
    @Tag("2025")
    class ByteMatrix2025Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            ByteMatrix m = new ByteMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(4, m.get(1, 1));
        }

        @Test
        public void testConstructor_withNullArray() {
            ByteMatrix m = new ByteMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withEmptyArray() {
            ByteMatrix m = new ByteMatrix(new byte[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withSingleElement() {
            ByteMatrix m = new ByteMatrix(new byte[][] { { 42 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42, m.get(0, 0));
        }

        // ============ Factory Method Tests ============

        @Test
        public void testEmpty() {
            ByteMatrix empty = ByteMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());

            // Test singleton
            assertSame(ByteMatrix.empty(), ByteMatrix.empty());
        }

        @Test
        public void testOf_withValidArray() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            ByteMatrix m = ByteMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1, m.get(0, 0));
        }

        @Test
        public void testOf_withNullArray() {
            ByteMatrix m = ByteMatrix.of((byte[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withEmptyArray() {
            ByteMatrix m = ByteMatrix.of(new byte[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testRandom() {
            ByteMatrix m = ByteMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            // Just verify elements exist (values are random)
            for (int i = 0; i < 5; i++) {
                assertNotNull(m.get(0, i));
            }
        }

        @Test
        public void testRandom_withRowsCols() {
            ByteMatrix m = ByteMatrix.random(2, 3);
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
            ByteMatrix m = ByteMatrix.repeat(1, 5, (byte) 42);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(42, m.get(0, i));
            }
        }

        @Test
        public void testRepeat_withRowsCols() {
            ByteMatrix m = ByteMatrix.repeat(2, 3, (byte) 42);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(42, m.get(i, j));
                }
            }
        }

        @Test
        public void testRange() {
            ByteMatrix m = ByteMatrix.range((byte) 0, (byte) 5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, m.rowView(0));
        }

        @Test
        public void testRange_withStep() {
            ByteMatrix m = ByteMatrix.range((byte) 0, (byte) 10, (byte) 2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new byte[] { 0, 2, 4, 6, 8 }, m.rowView(0));
        }

        @Test
        public void testRange_withNegativeStep() {
            ByteMatrix m = ByteMatrix.range((byte) 10, (byte) 0, (byte) -2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new byte[] { 10, 8, 6, 4, 2 }, m.rowView(0));
        }

        @Test
        public void testRangeClosed() {
            ByteMatrix m = ByteMatrix.rangeClosed((byte) 0, (byte) 4);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, m.rowView(0));
        }

        @Test
        public void testRangeClosed_withStep() {
            ByteMatrix m = ByteMatrix.rangeClosed((byte) 0, (byte) 10, (byte) 2);
            assertEquals(1, m.rowCount());
            assertEquals(6, m.columnCount());
            assertArrayEquals(new byte[] { 0, 2, 4, 6, 8, 10 }, m.rowView(0));
        }

        @Test
        public void testDiagonalLU2RD() {
            ByteMatrix m = ByteMatrix.mainDiagonal(new byte[] { 1, 2, 3 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
            assertEquals(0, m.get(0, 1));
            assertEquals(0, m.get(1, 0));
        }

        @Test
        public void testDiagonalRU2LD() {
            ByteMatrix m = ByteMatrix.antiDiagonal(new byte[] { 1, 2, 3 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 2));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 0));
            assertEquals(0, m.get(0, 0));
            assertEquals(0, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            ByteMatrix m = ByteMatrix.diagonals(new byte[] { 1, 2, 3 }, new byte[] { 4, 5, 6 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
            assertEquals(4, m.get(0, 2));
            assertEquals(6, m.get(2, 0));
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            ByteMatrix m = ByteMatrix.diagonals(new byte[] { 1, 2, 3 }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            ByteMatrix m = ByteMatrix.diagonals(null, new byte[] { 4, 5, 6 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(4, m.get(0, 2));
            assertEquals(5, m.get(1, 1));
            assertEquals(6, m.get(2, 0));
        }

        @Test
        public void testDiagonal_withBothNull() {
            ByteMatrix m = ByteMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> ByteMatrix.diagonals(new byte[] { 1, 2 }, new byte[] { 3, 4, 5 }));
        }

        @Test
        public void testUnbox() {
            Byte[][] boxed = { { 1, 2 }, { 3, 4 } };
            Matrix<Byte> boxedMatrix = Matrix.of(boxed);
            ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertEquals(1, unboxed.get(0, 0));
            assertEquals(4, unboxed.get(1, 1));
        }

        @Test
        public void testUnbox_withNullValues() {
            Byte[][] boxed = { { 1, null }, { null, 4 } };
            Matrix<Byte> boxedMatrix = Matrix.of(boxed);
            ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
            assertEquals(1, unboxed.get(0, 0));
            assertEquals(0, unboxed.get(0, 1)); // null -> 0
            assertEquals(0, unboxed.get(1, 0)); // null -> 0
            assertEquals(4, unboxed.get(1, 1));
        }

        // ============ Component Type Tests ============

        @Test
        public void testComponentType() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
            assertEquals(byte.class, m.componentType());
        }

        // ============ Get/Set Tests ============

        @Test
        public void testGet() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertEquals(1, m.get(0, 0));
            assertEquals(5, m.get(1, 1));
            assertEquals(6, m.get(1, 2));
        }

        @Test
        public void testGet_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
        }

        @Test
        public void testGetWithPoint() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(1, m.get(Point.of(0, 0)));
            assertEquals(4, m.get(Point.of(1, 1)));
            assertEquals(2, m.get(Point.of(0, 1)));
        }

        @Test
        public void testSet() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.set(0, 0, (byte) 10);
            assertEquals(10, m.get(0, 0));

            m.set(1, 1, (byte) 20);
            assertEquals(20, m.get(1, 1));
        }

        @Test
        public void testSet_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, (byte) 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, (byte) 0));
        }

        @Test
        public void testSetWithPoint() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.set(Point.of(0, 0), (byte) 50);
            assertEquals(50, m.get(Point.of(0, 0)));
        }

        // ============ Adjacent Element Tests ============

        @Test
        public void testUpOf() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });

            OptionalByte up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1, up.get());

            // Top row has no element above
            OptionalByte empty = m.above(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testDownOf() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });

            OptionalByte down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3, down.get());

            // Bottom row has no element below
            OptionalByte empty = m.below(1, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testLeftOf() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });

            OptionalByte left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1, left.get());

            // Leftmost column has no element to the left
            OptionalByte empty = m.left(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testRightOf() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });

            OptionalByte right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2, right.get());

            // Rightmost column has no element to the right
            OptionalByte empty = m.right(0, 1);
            assertFalse(empty.isPresent());
        }

        // ============ Row/Column Operations Tests ============

        @Test
        public void testRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertArrayEquals(new byte[] { 1, 2, 3 }, m.rowView(0));
            assertArrayEquals(new byte[] { 4, 5, 6 }, m.rowView(1));
        }

        @Test
        public void testRow_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(2));
        }

        @Test
        public void testColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertArrayEquals(new byte[] { 1, 4 }, m.columnCopy(0));
            assertArrayEquals(new byte[] { 2, 5 }, m.columnCopy(1));
            assertArrayEquals(new byte[] { 3, 6 }, m.columnCopy(2));
        }

        @Test
        public void testColumn_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.setRow(0, new byte[] { 10, 20 });
            assertArrayEquals(new byte[] { 10, 20 }, m.rowView(0));
            assertArrayEquals(new byte[] { 3, 4 }, m.rowView(1)); // unchanged
        }

        @Test
        public void testSetRow_wrongSize() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new byte[] { 1 }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new byte[] { 1, 2, 3 }));
        }

        @Test
        public void testSetColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.setColumn(0, new byte[] { 10, 20 });
            assertArrayEquals(new byte[] { 10, 20 }, m.columnCopy(0));
            assertArrayEquals(new byte[] { 2, 4 }, m.columnCopy(1)); // unchanged
        }

        @Test
        public void testSetColumn_wrongSize() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new byte[] { 1 }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new byte[] { 1, 2, 3 }));
        }

        @Test
        public void testUpdateRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.updateRow(0, x -> (byte) (x * 2));
            assertArrayEquals(new byte[] { 2, 4 }, m.rowView(0));
            assertArrayEquals(new byte[] { 3, 4 }, m.rowView(1)); // unchanged
        }

        @Test
        public void testUpdateColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.updateColumn(0, x -> (byte) (x + 10));
            assertArrayEquals(new byte[] { 11, 13 }, m.columnCopy(0));
            assertArrayEquals(new byte[] { 2, 4 }, m.columnCopy(1)); // unchanged
        }

        // ============ Diagonal Operations Tests ============

        @Test
        public void testGetLU2RD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            assertArrayEquals(new byte[] { 1, 5, 9 }, m.getMainDiagonal());
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new byte[] { 1 }));
        }

        @Test
        public void testSetLU2RD_arrayTooShort() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new byte[] { 1, 2 }));
        }

        @Test
        public void testUpdateLU2RD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateMainDiagonal(x -> (byte) (x * 10));
            assertEquals(10, m.get(0, 0));
            assertEquals(50, m.get(1, 1));
            assertEquals(90, m.get(2, 2));
            assertEquals(2, m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateLU2RD_nonSquare() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.updateMainDiagonal(x -> (byte) (x * 2)));
        }

        @Test
        public void testGetRU2LD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            assertArrayEquals(new byte[] { 3, 5, 7 }, m.getAntiDiagonal());
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD_nonSquare() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.setAntiDiagonal(new byte[] { 1 }));
        }

        @Test
        public void testSetRU2LD_arrayTooShort() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new byte[] { 1, 2 }));
        }

        @Test
        public void testUpdateRU2LD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateAntiDiagonal(x -> (byte) (x * 10));
            assertEquals(30, m.get(0, 2));
            assertEquals(50, m.get(1, 1));
            assertEquals(70, m.get(2, 0));
            assertEquals(2, m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateRU2LD_nonSquare() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(x -> (byte) (x * 2)));
        }

        // ============ Transformation Tests ============

        @Test
        public void testUpdateAll() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.updateAll(x -> (byte) (x * 2));
            assertEquals(2, m.get(0, 0));
            assertEquals(4, m.get(0, 1));
            assertEquals(6, m.get(1, 0));
            assertEquals(8, m.get(1, 1));
        }

        @Test
        public void testUpdateAll_withIndices() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
            m.updateAll((i, j) -> (byte) (i * 10 + j));
            assertEquals(0, m.get(0, 0));
            assertEquals(1, m.get(0, 1));
            assertEquals(10, m.get(1, 0));
            assertEquals(11, m.get(1, 1));
        }

        @Test
        public void testReplaceIf() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            m.replaceIf(x -> x > 3, (byte) 0);
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(0, 2));
            assertEquals(0, m.get(1, 0)); // was 4
            assertEquals(0, m.get(1, 1)); // was 5
            assertEquals(0, m.get(1, 2)); // was 6
        }

        @Test
        public void testReplaceIf_withIndices() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.replaceIf((i, j) -> i == j, (byte) 0); // Replace diagonal
            assertEquals(0, m.get(0, 0));
            assertEquals(0, m.get(1, 1));
            assertEquals(0, m.get(2, 2));
            assertEquals(2, m.get(0, 1)); // unchanged
        }

        @Test
        public void testMap() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix result = m.map(x -> (byte) (x * 2));
            assertEquals(2, result.get(0, 0));
            assertEquals(4, result.get(0, 1));
            assertEquals(6, result.get(1, 0));
            assertEquals(8, result.get(1, 1));

            // Original unchanged
            assertEquals(1, m.get(0, 0));
        }

        @Test
        public void testMapToObj() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            Matrix<String> result = m.mapToObj(x -> "val:" + x, String.class);
            assertEquals("val:1", result.get(0, 0));
            assertEquals("val:4", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_withValue() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.fill((byte) 99);
            for (int i = 0; i < m.rowCount(); i++) {
                for (int j = 0; j < m.columnCount(); j++) {
                    assertEquals(99, m.get(i, j));
                }
            }
        }

        @Test
        public void testFill_withArray() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
            byte[][] patch = { { 1, 2 }, { 3, 4 } };
            m.copyFrom(patch);
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(4, m.get(1, 1));
            assertEquals(0, m.get(2, 2)); // unchanged
        }

        @Test
        public void testFill_withArrayAtPosition() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
            byte[][] patch = { { 1, 2 }, { 3, 4 } };
            m.copyFrom(1, 1, patch);
            assertEquals(0, m.get(0, 0)); // unchanged
            assertEquals(1, m.get(1, 1));
            assertEquals(2, m.get(1, 2));
            assertEquals(3, m.get(2, 1));
            assertEquals(4, m.get(2, 2));
        }

        @Test
        public void testFill_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            byte[][] patch = { { 1, 2 }, { 3, 4 } };
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals(1, copy.get(0, 0));

            // Modify copy shouldn't affect original
            copy.set(0, 0, (byte) 99);
            assertEquals(1, m.get(0, 0));
            assertEquals(99, copy.get(0, 0));
        }

        @Test
        public void testCopy_withRowRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ByteMatrix subset = m.copy(1, 3);
            assertEquals(2, subset.rowCount());
            assertEquals(3, subset.columnCount());
            assertEquals(4, subset.get(0, 0));
            assertEquals(9, subset.get(1, 2));
        }

        @Test
        public void testCopy_withRowRange_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
        }

        @Test
        public void testCopy_withFullRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ByteMatrix submatrix = m.copy(0, 2, 1, 3);
            assertEquals(2, submatrix.rowCount());
            assertEquals(2, submatrix.columnCount());
            assertEquals(2, submatrix.get(0, 0));
            assertEquals(6, submatrix.get(1, 1));
        }

        @Test
        public void testCopy_withFullRange_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_larger() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix extended = m.resize(4, 4);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(4, extended.get(1, 1));
            assertEquals(0, extended.get(3, 3)); // new cells are 0
        }

        @Test
        public void testExtend_smaller() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ByteMatrix truncated = m.resize(2, 2);
            assertEquals(2, truncated.rowCount());
            assertEquals(2, truncated.columnCount());
            assertEquals(1, truncated.get(0, 0));
            assertEquals(5, truncated.get(1, 1));
        }

        @Test
        public void testExtend_withDefaultValue() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix extended = m.resize(3, 3, (byte) -1);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(-1, extended.get(2, 2)); // new cell
        }

        @Test
        public void testExtend_withNegativeDimensions() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 3, (byte) 0));
            assertThrows(IllegalArgumentException.class, () -> m.resize(3, -1, (byte) 0));
        }

        @Test
        public void testExtend_directional() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ByteMatrix extended = m.extend(1, 1, 2, 2);
            assertEquals(5, extended.rowCount()); // 1 + 3 + 1
            assertEquals(7, extended.columnCount()); // 2 + 3 + 2

            // Original values at offset position
            assertEquals(1, extended.get(1, 2));
            assertEquals(5, extended.get(2, 3));

            // New cells are 0
            assertEquals(0, extended.get(0, 0));
        }

        @Test
        public void testExtend_directionalWithDefault() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ByteMatrix extended = m.extend(1, 1, 1, 1, (byte) -1);
            assertEquals(5, extended.rowCount());
            assertEquals(5, extended.columnCount());

            // Check original values
            assertEquals(1, extended.get(1, 1));

            // Check new values
            assertEquals(-1, extended.get(0, 0));
            assertEquals(-1, extended.get(4, 4));
        }

        @Test
        public void testExtend_directionalWithNegativeValues() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, (byte) 0));
        }

        // ============ Reverse/Flip Tests ============

        @Test
        public void testReverseH() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            m.flipInPlaceHorizontally();
            assertEquals(3, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(1, m.get(0, 2));
            assertEquals(6, m.get(1, 0));
        }

        @Test
        public void testReverseV() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            m.flipInPlaceVertically();
            assertEquals(5, m.get(0, 0));
            assertEquals(6, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(1, m.get(2, 0));
        }

        @Test
        public void testFlipH() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ByteMatrix flipped = m.flipHorizontally();
            assertEquals(3, flipped.get(0, 0));
            assertEquals(2, flipped.get(0, 1));
            assertEquals(1, flipped.get(0, 2));

            // Original unchanged
            assertEquals(1, m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            ByteMatrix flipped = m.flipVertically();
            assertEquals(5, flipped.get(0, 0));
            assertEquals(3, flipped.get(1, 0));
            assertEquals(1, flipped.get(2, 0));

            // Original unchanged
            assertEquals(1, m.get(0, 0));
        }

        // ============ Rotation Tests ============

        @Test
        public void testRotate90() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3, rotated.get(0, 0));
            assertEquals(1, rotated.get(0, 1));
            assertEquals(4, rotated.get(1, 0));
            assertEquals(2, rotated.get(1, 1));
        }

        @Test
        public void testRotate180() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix rotated = m.rotate180();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4, rotated.get(0, 0));
            assertEquals(3, rotated.get(0, 1));
            assertEquals(2, rotated.get(1, 0));
            assertEquals(1, rotated.get(1, 1));
        }

        @Test
        public void testRotate270() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(2, rotated.get(0, 0));
            assertEquals(4, rotated.get(0, 1));
            assertEquals(1, rotated.get(1, 0));
            assertEquals(3, rotated.get(1, 1));
        }

        // ============ Transpose Tests ============

        @Test
        public void testTranspose() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ByteMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(4, transposed.get(0, 1));
            assertEquals(2, transposed.get(1, 0));
            assertEquals(5, transposed.get(1, 1));
            assertEquals(3, transposed.get(2, 0));
            assertEquals(6, transposed.get(2, 1));
        }

        @Test
        public void testTranspose_square() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(3, transposed.get(0, 1));
            assertEquals(2, transposed.get(1, 0));
            assertEquals(4, transposed.get(1, 1));
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ByteMatrix reshaped = m.reshape(1, 9);
            assertEquals(1, reshaped.rowCount());
            assertEquals(9, reshaped.columnCount());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1, reshaped.get(0, i));
            }
        }

        @Test
        public void testReshape_back() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ByteMatrix reshaped = m.reshape(1, 9);
            ByteMatrix reshapedBack = reshaped.reshape(3, 3);
            assertEquals(m, reshapedBack);
        }

        @Test
        public void testReshape_empty() {
            ByteMatrix empty = ByteMatrix.empty();
            ByteMatrix reshaped = empty.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        // ============ Repeat Tests ============

        @Test
        public void testRepelem() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix repeated = m.repeatElements(2, 3);
            assertEquals(2, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals(1, repeated.get(0, 0));
            assertEquals(1, repeated.get(0, 1));
            assertEquals(1, repeated.get(0, 2));
            assertEquals(2, repeated.get(0, 3));
            assertEquals(2, repeated.get(0, 4));
            assertEquals(2, repeated.get(0, 5));
            assertEquals(1, repeated.get(1, 0)); // second row same as first
        }

        @Test
        public void testRepelem_invalidArguments() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix repeated = m.repeatMatrix(2, 3);
            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals(1, repeated.get(0, 0));
            assertEquals(2, repeated.get(0, 1));
            assertEquals(1, repeated.get(0, 2)); // repeat starts
            assertEquals(2, repeated.get(0, 3));

            assertEquals(3, repeated.get(1, 0));
            assertEquals(4, repeated.get(1, 1));

            // Check vertical repeat
            assertEquals(1, repeated.get(2, 0)); // vertical repeat starts
            assertEquals(2, repeated.get(2, 1));
        }

        @Test
        public void testRepmat_invalidArguments() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ByteList flat = m.flatten();
            assertEquals(9, flat.size());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1, flat.get(i));
            }
        }

        @Test
        public void testFlatten_empty() {
            ByteMatrix empty = ByteMatrix.empty();
            ByteList flat = empty.flatten();
            assertTrue(flat.isEmpty());
        }

        @Test
        public void testFlatOp() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Integer> sums = new ArrayList<>();
            m.applyOnFlattened(row -> {
                int sum = 0;
                for (byte val : row) {
                    sum += val;
                }
                sums.add(sum);
            });
            assertEquals(1, sums.size());
            assertEquals(45, sums.get(0).intValue());
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 7, 8, 9 }, { 10, 11, 12 } });
            ByteMatrix stacked = m1.stackVertically(m2);

            assertEquals(4, stacked.rowCount());
            assertEquals(3, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(7, stacked.get(2, 0));
            assertEquals(12, stacked.get(3, 2));
        }

        @Test
        public void testVstack_differentColumnCounts() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix stacked = m1.stackHorizontally(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(5, stacked.get(0, 2));
            assertEquals(8, stacked.get(1, 3));
        }

        @Test
        public void testHstack_differentRowCounts() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Operations Tests ============

        @Test
        public void testAdd() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix sum = m1.add(m2);

            assertEquals(6, sum.get(0, 0));
            assertEquals(8, sum.get(0, 1));
            assertEquals(10, sum.get(1, 0));
            assertEquals(12, sum.get(1, 1));
        }

        @Test
        public void testAdd_differentDimensions() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void testSubtract() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix diff = m1.subtract(m2);

            assertEquals(4, diff.get(0, 0));
            assertEquals(4, diff.get(0, 1));
            assertEquals(4, diff.get(1, 0));
            assertEquals(4, diff.get(1, 1));
        }

        @Test
        public void testSubtract_differentDimensions() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
        }

        @Test
        public void testMultiply() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix product = m1.multiply(m2);

            assertEquals(19, product.get(0, 0)); // 1*5 + 2*7
            assertEquals(22, product.get(0, 1)); // 1*6 + 2*8
            assertEquals(43, product.get(1, 0)); // 3*5 + 4*7
            assertEquals(50, product.get(1, 1)); // 3*6 + 4*8
        }

        @Test
        public void testMultiply_incompatibleDimensions() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        @Test
        public void testMultiply_rectangularMatrices() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } }); // 1x3
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 4 }, { 5 }, { 6 } }); // 3x1
            ByteMatrix product = m1.multiply(m2);

            assertEquals(1, product.rowCount());
            assertEquals(1, product.columnCount());
            assertEquals(32, product.get(0, 0)); // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
        }

        // ============ Conversion Tests ============

        @Test
        public void testBoxed() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            Matrix<Byte> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(3, boxed.columnCount());
            assertEquals(Byte.valueOf((byte) 1), boxed.get(0, 0));
            assertEquals(Byte.valueOf((byte) 6), boxed.get(1, 2));
        }

        @Test
        public void testToIntMatrix() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix intMatrix = m.toIntMatrix();
            assertEquals(2, intMatrix.rowCount());
            assertEquals(2, intMatrix.columnCount());
            assertEquals(1, intMatrix.get(0, 0));
            assertEquals(4, intMatrix.get(1, 1));
        }

        @Test
        public void testToLongMatrix() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            LongMatrix longMatrix = m.toLongMatrix();
            assertEquals(2, longMatrix.rowCount());
            assertEquals(2, longMatrix.columnCount());
            assertEquals(1L, longMatrix.get(0, 0));
            assertEquals(4L, longMatrix.get(1, 1));
        }

        @Test
        public void testToFloatMatrix() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            FloatMatrix floatMatrix = m.toFloatMatrix();
            assertEquals(2, floatMatrix.rowCount());
            assertEquals(2, floatMatrix.columnCount());
            assertEquals(1.0f, floatMatrix.get(0, 0), 0.0001f);
            assertEquals(4.0f, floatMatrix.get(1, 1), 0.0001f);
        }

        @Test
        public void testToDoubleMatrix() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            DoubleMatrix doubleMatrix = m.toDoubleMatrix();
            assertEquals(2, doubleMatrix.rowCount());
            assertEquals(2, doubleMatrix.columnCount());
            assertEquals(1.0, doubleMatrix.get(0, 0), 0.0001);
            assertEquals(4.0, doubleMatrix.get(1, 1), 0.0001);
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix result = m1.zipWith(m2, (a, b) -> (byte) (a * b));

            assertEquals(5, result.get(0, 0)); // 1*5
            assertEquals(12, result.get(0, 1)); // 2*6
            assertEquals(21, result.get(1, 0)); // 3*7
            assertEquals(32, result.get(1, 1)); // 4*8
        }

        @Test
        public void testZipWith_differentShapes() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> (byte) (a + b)));
        }

        @Test
        public void testZipWith_threeMatrices() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } });
            ByteMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (byte) (a + b + c));

            assertEquals(15, result.get(0, 0)); // 1+5+9
            assertEquals(18, result.get(0, 1)); // 2+6+10
            assertEquals(21, result.get(1, 0)); // 3+7+11
            assertEquals(24, result.get(1, 1)); // 4+8+12
        }

        @Test
        public void testZipWith_threeMatrices_differentShapes() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 9, 10, 11 } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> (byte) (a + b + c)));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            byte[] diagonal = m.streamMainDiagonal().toArray();
            assertArrayEquals(new byte[] { 1, 5, 9 }, diagonal);
        }

        @Test
        public void testStreamLU2RD_empty() {
            ByteMatrix empty = ByteMatrix.empty();
            assertEquals(0, empty.streamMainDiagonal().toArray().length);
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            ByteMatrix nonSquare = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
        }

        @Test
        public void testStreamRU2LD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            byte[] antiDiagonal = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new byte[] { 3, 5, 7 }, antiDiagonal);
        }

        @Test
        public void testStreamRU2LD_empty() {
            ByteMatrix empty = ByteMatrix.empty();
            assertEquals(0, empty.streamAntiDiagonal().toArray().length);
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            ByteMatrix nonSquare = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamAntiDiagonal());
        }

        @Test
        public void testStreamH() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            byte[] all = m.streamHorizontal().toArray();
            assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6 }, all);
        }

        @Test
        public void testStreamH_empty() {
            ByteMatrix empty = ByteMatrix.empty();
            assertEquals(0, empty.streamHorizontal().toArray().length);
        }

        @Test
        public void testStreamH_withRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            byte[] row1 = m.streamHorizontal(1).toArray();
            assertArrayEquals(new byte[] { 4, 5, 6 }, row1);
        }

        @Test
        public void testStreamH_withRow_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2));
        }

        @Test
        public void testStreamH_withRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            byte[] rows = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new byte[] { 4, 5, 6, 7, 8, 9 }, rows);
        }

        @Test
        public void testStreamH_withRange_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2, 1));
        }

        @Test
        public void testStreamV() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            byte[] all = m.streamVertical().toArray();
            assertArrayEquals(new byte[] { 1, 4, 2, 5, 3, 6 }, all);
        }

        @Test
        public void testStreamV_empty() {
            ByteMatrix empty = ByteMatrix.empty();
            assertEquals(0, empty.streamVertical().toArray().length);
        }

        @Test
        public void testStreamV_withColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            byte[] col1 = m.streamVertical(1).toArray();
            assertArrayEquals(new byte[] { 2, 5 }, col1);
        }

        @Test
        public void testStreamV_withColumn_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2));
        }

        @Test
        public void testStreamV_withRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            byte[] columnCount = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new byte[] { 2, 5, 8, 3, 6, 9 }, columnCount);
        }

        @Test
        public void testStreamV_withRange_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2, 1));
        }

        @Test
        public void testStreamR() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<byte[]> rows = m.streamRows().map(ByteStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new byte[] { 1, 2, 3 }, rows.get(0));
            assertArrayEquals(new byte[] { 4, 5, 6 }, rows.get(1));
        }

        @Test
        public void testStreamR_empty() {
            ByteMatrix empty = ByteMatrix.empty();
            assertEquals(0, empty.streamRows().count());
        }

        @Test
        public void testStreamR_withRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<byte[]> rows = m.streamRows(1, 3).map(ByteStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new byte[] { 4, 5, 6 }, rows.get(0));
            assertArrayEquals(new byte[] { 7, 8, 9 }, rows.get(1));
        }

        @Test
        public void testStreamR_withRange_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(0, 3));
        }

        @Test
        public void testStreamC() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<byte[]> columnCount = m.streamColumns().map(ByteStream::toArray).toList();
            assertEquals(3, columnCount.size());
            assertArrayEquals(new byte[] { 1, 4 }, columnCount.get(0));
            assertArrayEquals(new byte[] { 2, 5 }, columnCount.get(1));
            assertArrayEquals(new byte[] { 3, 6 }, columnCount.get(2));
        }

        @Test
        public void testStreamC_empty() {
            ByteMatrix empty = ByteMatrix.empty();
            assertEquals(0, empty.streamColumns().count());
        }

        @Test
        public void testStreamC_withRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<byte[]> columnCount = m.streamColumns(1, 3).map(ByteStream::toArray).toList();
            assertEquals(2, columnCount.size());
            assertArrayEquals(new byte[] { 2, 5, 8 }, columnCount.get(0));
            assertArrayEquals(new byte[] { 3, 6, 9 }, columnCount.get(1));
        }

        @Test
        public void testStreamC_withRange_outOfBounds() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(0, 3));
        }

        // ============ Object Methods Tests ============

        @Test
        public void testHashCode() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 4, 3 } });

            assertEquals(m1.hashCode(), m2.hashCode());
            assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
        }

        @Test
        public void testEquals() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 4, 3 } });
            ByteMatrix m4 = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });

            assertTrue(m1.equals(m1)); // Same object
            assertTrue(m1.equals(m2)); // Same values
            assertFalse(m1.equals(m3)); // Different values
            assertFalse(m1.equals(m4)); // Different dimensions
            assertFalse(m1.equals(null));
            assertFalse(m1.equals("not a matrix"));
        }

        @Test
        public void testToString() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("1"));
            assertTrue(str.contains("2"));
            assertTrue(str.contains("3"));
            assertTrue(str.contains("4"));
        }

        // ============ Edge Case Tests ============

        @Test
        public void testByteMinMaxValues() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { Byte.MAX_VALUE, Byte.MIN_VALUE } });
            assertEquals(Byte.MAX_VALUE, m.get(0, 0));
            assertEquals(Byte.MIN_VALUE, m.get(0, 1));
        }

        @Test
        public void testByteOverflow() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { Byte.MAX_VALUE, 1 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, Byte.MAX_VALUE } });

            // Addition overflow wraps around
            ByteMatrix sum = m1.add(m2);
            assertTrue(sum.get(0, 0) < 0); // Overflow
        }

        @Test
        public void testNegativeBytes() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { -1, -2 }, { -3, -4 } });

            assertEquals(-1, m.get(0, 0));
            assertEquals(-4, m.get(1, 1));

            ByteMatrix doubled = m.map(x -> (byte) (x * 2));
            assertEquals(-2, doubled.get(0, 0));
            assertEquals(-8, doubled.get(1, 1));
        }

        @Test
        public void testEmptyMatrixOperations() {
            ByteMatrix empty = ByteMatrix.empty();

            assertTrue(empty.flatten().isEmpty());
            assertEquals(0, empty.copy().rowCount);
            assertEquals(empty, empty.transpose());
            assertEquals(empty, empty.rotate90());

            ByteMatrix extended = empty.resize(2, 2, (byte) 5);
            assertEquals(2, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals(5, extended.get(0, 0));
        }

        // ============ High-Impact Tests for 95% Coverage ============

        @Test
        public void testRotateTransposeAndConvertTallMatrix() {
            // Create a tall matrix (rows > columnCount) - 5 rows × 3 columnCount
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 }, { 13, 14, 15 } });

            // Test rotate90() with tall matrix
            ByteMatrix rotated90 = m.rotate90();
            assertEquals(3, rotated90.rowCount());
            assertEquals(5, rotated90.columnCount());
            assertEquals(13, rotated90.get(0, 0));
            assertEquals(1, rotated90.get(0, 4));

            // Test rotate270() with tall matrix
            ByteMatrix rotated270 = m.rotate270();
            assertEquals(3, rotated270.rowCount());
            assertEquals(5, rotated270.columnCount());
            assertEquals(3, rotated270.get(0, 0));

            // Test transpose() with tall matrix
            ByteMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(5, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(15, transposed.get(2, 4));

            // Test boxed() with tall matrix
            Matrix<Byte> boxed = m.boxed();
            assertEquals(5, boxed.rowCount());
            assertEquals(3, boxed.columnCount());
            assertEquals(Byte.valueOf((byte) 1), boxed.get(0, 0));

            // Test toLongMatrix() with tall matrix
            LongMatrix longMat = m.toLongMatrix();
            assertEquals(5, longMat.rowCount());
            assertEquals(3, longMat.columnCount());
            assertEquals(1L, longMat.get(0, 0));
            assertEquals(15L, longMat.get(4, 2));

            // Test toFloatMatrix() with tall matrix
            FloatMatrix floatMat = m.toFloatMatrix();
            assertEquals(5, floatMat.rowCount());
            assertEquals(3, floatMat.columnCount());
            assertEquals(1.0f, floatMat.get(0, 0), 0.001f);

            // Test toDoubleMatrix() with tall matrix
            DoubleMatrix doubleMat = m.toDoubleMatrix();
            assertEquals(5, doubleMat.rowCount());
            assertEquals(3, doubleMat.columnCount());
            assertEquals(1.0, doubleMat.get(0, 0), 0.001);
        }

        @Test
        public void testRepelemOverflow() {
            int largeSize = 50000;
            ByteMatrix m = ByteMatrix.of(new byte[largeSize][2]);

            IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> m.repeatElements(50000, 1));
            assertTrue(ex1.getMessage().contains("row count overflow"));

            ByteMatrix m2 = ByteMatrix.of(new byte[2][largeSize]);
            IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> m2.repeatElements(1, 50000));
            assertTrue(ex2.getMessage().contains("column count overflow"));
        }

        @Test
        public void testRepmatOverflow() {
            int largeSize = 50000;
            ByteMatrix m = ByteMatrix.of(new byte[largeSize][2]);

            IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(50000, 1));
            assertTrue(ex1.getMessage().contains("row count overflow"));

            ByteMatrix m2 = ByteMatrix.of(new byte[2][largeSize]);
            IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> m2.repeatMatrix(1, 50000));
            assertTrue(ex2.getMessage().contains("column count overflow"));
        }
    }

    @Nested
    @Tag("2510")
    class ByteMatrix2510Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            ByteMatrix m = new ByteMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(4, m.get(1, 1));
        }

        @Test
        public void testConstructor_withNonSquareMatrix() {
            byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ByteMatrix m = new ByteMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
        }

        @Test
        public void testConstructor_withNegativeValues() {
            byte[][] arr = { { -1, -2 }, { -3, -4 } };
            ByteMatrix m = new ByteMatrix(arr);
            assertEquals(-1, m.get(0, 0));
            assertEquals(-4, m.get(1, 1));
        }

        @Test
        public void testRandom_withZeroLength() {
            ByteMatrix m = ByteMatrix.random(0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRepeat_withZero() {
            ByteMatrix m = ByteMatrix.repeat(1, 3, (byte) 0);
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 3; i++) {
                assertEquals(0, m.get(0, i));
            }
        }

        @Test
        public void testRange() {
            ByteMatrix m = ByteMatrix.range((byte) 1, (byte) 5);
            assertEquals(1, m.rowCount());
            assertEquals(4, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(0, 2));
            assertEquals(4, m.get(0, 3));
        }

        @Test
        public void testRange_withStep() {
            ByteMatrix m = ByteMatrix.range((byte) 0, (byte) 10, (byte) 2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals(0, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(4, m.get(0, 2));
            assertEquals(6, m.get(0, 3));
            assertEquals(8, m.get(0, 4));
        }

        @Test
        public void testRange_emptyRange() {
            ByteMatrix m = ByteMatrix.range((byte) 5, (byte) 5);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRangeClosed() {
            ByteMatrix m = ByteMatrix.rangeClosed((byte) 1, (byte) 4);
            assertEquals(1, m.rowCount());
            assertEquals(4, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(4, m.get(0, 3));
        }

        @Test
        public void testRangeClosed_withStep() {
            ByteMatrix m = ByteMatrix.rangeClosed((byte) 0, (byte) 9, (byte) 3);
            assertEquals(1, m.rowCount());
            assertEquals(4, m.columnCount());
            assertEquals(0, m.get(0, 0));
            assertEquals(3, m.get(0, 1));
            assertEquals(6, m.get(0, 2));
            assertEquals(9, m.get(0, 3));
        }

        @Test
        public void testRangeClosed_singleElement() {
            ByteMatrix m = ByteMatrix.rangeClosed((byte) 5, (byte) 5);
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(5, m.get(0, 0));
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> ByteMatrix.diagonals(new byte[] { 1, 2 }, new byte[] { 1, 2, 3 }));
        }

        @Test
        public void testUnbox() {
            Byte[][] boxed = { { 1, 2 }, { 3, 4 } };
            Matrix<Byte> boxedMatrix = Matrix.of(boxed);
            ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertEquals(1, unboxed.get(0, 0));
            assertEquals(2, unboxed.get(0, 1));
        }

        @Test
        public void testUnbox_withNullElements() {
            Byte[][] boxed = { { 1, null }, { null, 4 } };
            Matrix<Byte> boxedMatrix = Matrix.of(boxed);
            ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(1, unboxed.get(0, 0));
            assertEquals(0, unboxed.get(0, 1));
            assertEquals(0, unboxed.get(1, 0));
        }
        // ============ Get/Set Tests ============

        @Test
        public void testGet_byIndices() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            ByteMatrix m = ByteMatrix.of(arr);
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(4, m.get(1, 1));
        }

        @Test
        public void testGet_byPoint() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            ByteMatrix m = ByteMatrix.of(arr);
            assertEquals(1, m.get(Point.of(0, 0)));
            assertEquals(2, m.get(Point.of(0, 1)));
            assertEquals(3, m.get(Point.of(1, 0)));
            assertEquals(4, m.get(Point.of(1, 1)));
        }

        @Test
        public void testSet_byIndices() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
            m.set(0, 0, (byte) 1);
            m.set(1, 1, (byte) 4);
            assertEquals(1, m.get(0, 0));
            assertEquals(4, m.get(1, 1));
            assertEquals(0, m.get(0, 1));
            assertEquals(0, m.get(1, 0));
        }

        @Test
        public void testSet_byPoint() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
            m.set(Point.of(0, 0), (byte) 1);
            m.set(Point.of(1, 1), (byte) 4);
            assertEquals(1, m.get(Point.of(0, 0)));
            assertEquals(4, m.get(Point.of(1, 1)));
            assertEquals(0, m.get(Point.of(0, 1)));
        }

        @Test
        public void testDownOf_atBottomEdge() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            OptionalByte down = m.below(1, 0);
            assertFalse(down.isPresent());
        }

        @Test
        public void testLeftOf_atLeftEdge() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            OptionalByte left = m.left(0, 0);
            assertFalse(left.isPresent());
        }

        @Test
        public void testRightOf_atRightEdge() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            OptionalByte right = m.right(0, 1);
            assertFalse(right.isPresent());
        }

        // ============ Row/Column Access Tests ============

        @Test
        public void testRow() {
            byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ByteMatrix m = ByteMatrix.of(arr);
            byte[] row0 = m.rowView(0);
            assertArrayEquals(new byte[] { 1, 2, 3 }, row0);
            byte[] row1 = m.rowView(1);
            assertArrayEquals(new byte[] { 4, 5, 6 }, row1);
        }

        @Test
        public void testRow_invalidIndex() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(1));
        }

        @Test
        public void testColumn() {
            byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            ByteMatrix m = ByteMatrix.of(arr);
            byte[] col0 = m.columnCopy(0);
            assertArrayEquals(new byte[] { 1, 4 }, col0);
            byte[] col1 = m.columnCopy(1);
            assertArrayEquals(new byte[] { 2, 5 }, col1);
        }

        @Test
        public void testColumn_invalidIndex() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 } });
            m.setRow(0, new byte[] { 1, 2, 3 });
            assertArrayEquals(new byte[] { 1, 2, 3 }, m.rowView(0));
            assertArrayEquals(new byte[] { 0, 0, 0 }, m.rowView(1));
        }

        @Test
        public void testSetRow_invalidLength() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new byte[] { 1, 2 }));
        }

        @Test
        public void testSetColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 }, { 0, 0 } });
            m.setColumn(0, new byte[] { 1, 2, 3 });
            assertArrayEquals(new byte[] { 1, 2, 3 }, m.columnCopy(0));
            assertArrayEquals(new byte[] { 0, 0, 0 }, m.columnCopy(1));
        }

        @Test
        public void testSetColumn_invalidLength() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new byte[] { 1 }));
        }

        @Test
        public void testUpdateRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            m.updateRow(0, val -> (byte) (val * 2));
            assertArrayEquals(new byte[] { 2, 4, 6 }, m.rowView(0));
            assertArrayEquals(new byte[] { 4, 5, 6 }, m.rowView(1));
        }

        @Test
        public void testUpdateColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            m.updateColumn(0, val -> (byte) (val + 10));
            assertArrayEquals(new byte[] { 11, 13, 15 }, m.columnCopy(0));
            assertArrayEquals(new byte[] { 2, 4, 6 }, m.columnCopy(1));
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
            m.setMainDiagonal(new byte[] { 1, 2, 3 });
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
            assertEquals(0, m.get(0, 1));
        }

        @Test
        public void testSetLU2RD_invalidLength() {
            ByteMatrix m = ByteMatrix.of(new byte[3][3]);
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new byte[] { 1, 2 }));
        }

        @Test
        public void testUpdateLU2RD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 0, 0 }, { 0, 2, 0 }, { 0, 0, 3 } });
            m.updateMainDiagonal(val -> (byte) (val * 10));
            assertEquals(10, m.get(0, 0));
            assertEquals(20, m.get(1, 1));
            assertEquals(30, m.get(2, 2));
        }

        @Test
        public void testSetRU2LD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
            m.setAntiDiagonal(new byte[] { 1, 2, 3 });
            assertEquals(1, m.get(0, 2));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 0));
            assertEquals(0, m.get(0, 0));
        }

        @Test
        public void testUpdateRU2LD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 1 }, { 0, 2, 0 }, { 3, 0, 0 } });
            m.updateAntiDiagonal(val -> (byte) (val * 5));
            assertEquals(5, m.get(0, 2));
            assertEquals(10, m.get(1, 1));
            assertEquals(15, m.get(2, 0));
        }

        @Test
        public void testUpdateAll_biFunction() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.updateAll((i, j) -> (byte) (i + j));
            assertEquals(0, m.get(0, 0));
            assertEquals(1, m.get(0, 1));
            assertEquals(1, m.get(1, 0));
            assertEquals(2, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_predicate() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.replaceIf(val -> val > 2, (byte) 0);
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(0, m.get(1, 0));
            assertEquals(0, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_biPredicate() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.replaceIf((i, j) -> i == j, (byte) 0);
            assertEquals(0, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(0, m.get(1, 1));
        }

        // ============ Map Tests ============

        @Test
        public void testMap() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix mapped = m.map(val -> (byte) (val * 10));
            assertEquals(10, mapped.get(0, 0));
            assertEquals(20, mapped.get(0, 1));
            assertEquals(30, mapped.get(1, 0));
            assertEquals(40, mapped.get(1, 1));
            assertEquals(1, m.get(0, 0));
        }

        @Test
        public void testMapToObj() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            Matrix<String> mapped = m.mapToObj(val -> "V" + val, String.class);
            assertEquals("V1", mapped.get(0, 0));
            assertEquals("V2", mapped.get(0, 1));
            assertEquals("V3", mapped.get(1, 0));
            assertEquals("V4", mapped.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_singleValue() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
            m.fill((byte) 5);
            assertEquals(5, m.get(0, 0));
            assertEquals(5, m.get(0, 1));
            assertEquals(5, m.get(1, 0));
            assertEquals(5, m.get(1, 1));
        }

        @Test
        public void testFill_withArray() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
            m.copyFrom(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(1, m.get(0, 0));
            assertEquals(4, m.get(1, 1));
        }

        @Test
        public void testFill_withOffset() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
            m.copyFrom(1, 1, new byte[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(0, m.get(0, 0));
            assertEquals(1, m.get(1, 1));
            assertEquals(4, m.get(2, 2));
        }

        @Test
        public void testFill_withOffset_invalidPosition() {
            ByteMatrix m = ByteMatrix.of(new byte[2][2]);
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(3, 3, new byte[][] { { 1, 2 }, { 3, 4 } }));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix copy = m.copy();
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(1, copy.get(0, 0));
            assertEquals(2, copy.get(0, 1));
            copy.set(0, 0, (byte) 99);
            assertEquals(1, m.get(0, 0));
        }

        @Test
        public void testCopy_withRowRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            ByteMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(3, copy.get(0, 0));
            assertEquals(4, copy.get(0, 1));
        }

        @Test
        public void testCopy_withRowRange_invalidRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2));
        }

        @Test
        public void testCopy_withFullRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ByteMatrix copy = m.copy(1, 3, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(5, copy.get(0, 0));
            assertEquals(6, copy.get(0, 1));
            assertEquals(8, copy.get(1, 0));
            assertEquals(9, copy.get(1, 1));
        }

        @Test
        public void testCopy_withFullRange_invalidRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 1, -1, 1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 1, 0, 3));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_withDefaultValue() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix extended = m.resize(2, 3);
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(2, extended.get(0, 1));
            assertEquals(0, extended.get(0, 2));
            assertEquals(0, extended.get(1, 0));
        }

        @Test
        public void testExtend_withCustomDefaultValue() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix extended = m.resize(2, 3, (byte) 9);
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(2, extended.get(0, 1));
            assertEquals(9, extended.get(0, 2));
            assertEquals(9, extended.get(1, 0));
        }

        @Test
        public void testExtend_withDirections() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 5 } });
            ByteMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(5, extended.get(1, 1));
            assertEquals(0, extended.get(0, 0));
        }

        @Test
        public void testExtend_withDirectionsAndValue() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 5 } });
            ByteMatrix extended = m.extend(1, 1, 1, 1, (byte) 7);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(5, extended.get(1, 1));
            assertEquals(7, extended.get(0, 0));
        }
        // ============ Transformation Tests ============

        @Test
        public void testReverseH() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            m.flipInPlaceHorizontally();
            assertEquals(3, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(1, m.get(0, 2));
        }

        @Test
        public void testReverseV() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.flipInPlaceVertically();
            assertEquals(3, m.get(0, 0));
            assertEquals(4, m.get(0, 1));
            assertEquals(1, m.get(1, 0));
            assertEquals(2, m.get(1, 1));
        }

        @Test
        public void testFlipV() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix flipped = m.flipVertically();
            assertEquals(3, flipped.get(0, 0));
            assertEquals(4, flipped.get(0, 1));
            assertEquals(1, flipped.get(1, 0));
            assertEquals(2, flipped.get(1, 1));
            assertEquals(1, m.get(0, 0));
        }
        // ============ Reshape Tests ============

        @Test
        public void testReshape_oneArg() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3, 4 } });
            ByteMatrix reshaped = m.reshape(2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
            assertEquals(3, reshaped.get(1, 0));
            assertEquals(4, reshaped.get(1, 1));
        }

        @Test
        public void testReshape_twoArgs() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3, 4 } });
            ByteMatrix reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
        }

        @Test
        public void testReshape_invalidSize() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
            ByteMatrix reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
        }

        // ============ Repelem/Repmat Tests ============

        @Test
        public void testRepelem() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix result = m.repeatElements(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1, result.get(0, 0));
            assertEquals(1, result.get(0, 1));
            assertEquals(1, result.get(1, 0));
            assertEquals(1, result.get(1, 1));
            assertEquals(2, result.get(0, 2));
        }

        @Test
        public void testRepelem_invalidRepeats() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix result = m.repeatMatrix(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1, result.get(0, 0));
            assertEquals(2, result.get(0, 1));
            assertEquals(1, result.get(2, 0));
            assertEquals(2, result.get(2, 1));
        }

        @Test
        public void testRepmat_invalidRepeats() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        @Test
        public void testFlatOp() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            AtomicInteger count = new AtomicInteger(0);
            m.applyOnFlattened(row -> count.addAndGet(row.length));
            assertEquals(4, count.get());
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 } });
            ByteMatrix stacked = m1.stackVertically(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(2, stacked.get(0, 1));
            assertEquals(3, stacked.get(1, 0));
            assertEquals(4, stacked.get(1, 1));
        }

        @Test
        public void testHstack() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1 }, { 3 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 2 }, { 4 } });
            ByteMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(2, stacked.get(0, 1));
            assertEquals(3, stacked.get(1, 0));
            assertEquals(4, stacked.get(1, 1));
        }

        @Test
        public void testHstack_invalidRows() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 2 }, { 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Boxed Test ============

        @Test
        public void testBoxed() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Byte> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(Byte.valueOf((byte) 1), boxed.get(0, 0));
            assertEquals(Byte.valueOf((byte) 2), boxed.get(0, 1));
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith_two() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix result = m1.zipWith(m2, (a, b) -> (byte) (a + b));
            assertEquals(6, result.get(0, 0));
            assertEquals(8, result.get(0, 1));
            assertEquals(10, result.get(1, 0));
            assertEquals(12, result.get(1, 1));
        }

        @Test
        public void testZipWith_two_invalidShape() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> (byte) (a + b)));
        }

        @Test
        public void testZipWith_three() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 5, 6 } });
            ByteMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (byte) (a + b + c));
            assertEquals(9, result.get(0, 0));
            assertEquals(12, result.get(0, 1));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Byte> diag = m.streamMainDiagonal().boxed().toList();
            assertEquals(3, diag.size());
            assertEquals((byte) 1, diag.get(0));
            assertEquals((byte) 5, diag.get(1));
            assertEquals((byte) 9, diag.get(2));
        }

        @Test
        public void testStreamRU2LD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Byte> diag = m.streamAntiDiagonal().boxed().toList();
            assertEquals(3, diag.size());
            assertEquals((byte) 3, diag.get(0));
            assertEquals((byte) 5, diag.get(1));
            assertEquals((byte) 7, diag.get(2));
        }

        @Test
        public void testStreamH() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Byte> all = m.streamHorizontal().boxed().toList();
            assertEquals(4, all.size());
            assertEquals((byte) 1, all.get(0));
            assertEquals((byte) 2, all.get(1));
        }

        @Test
        public void testStreamH_singleRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<Byte> row = m.streamHorizontal(0).boxed().toList();
            assertEquals(3, row.size());
            assertEquals((byte) 1, row.get(0));
            assertEquals((byte) 2, row.get(1));
            assertEquals((byte) 3, row.get(2));
        }

        @Test
        public void testStreamH_rowRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            List<Byte> rows = m.streamHorizontal(1, 3).boxed().toList();
            assertEquals(4, rows.size());
            assertEquals((byte) 3, rows.get(0));
            assertEquals((byte) 4, rows.get(1));
        }

        @Test
        public void testStreamV() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Byte> all = m.streamVertical().boxed().toList();
            assertEquals(4, all.size());
            assertEquals((byte) 1, all.get(0));
            assertEquals((byte) 3, all.get(1));
        }

        @Test
        public void testStreamV_singleColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            List<Byte> col = m.streamVertical(0).boxed().toList();
            assertEquals(3, col.size());
            assertEquals((byte) 1, col.get(0));
            assertEquals((byte) 3, col.get(1));
            assertEquals((byte) 5, col.get(2));
        }

        @Test
        public void testStreamV_columnRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<Byte> columnCount = m.streamVertical(1, 3).boxed().toList();
            assertEquals(4, columnCount.size());
            assertEquals((byte) 2, columnCount.get(0));
            assertEquals((byte) 5, columnCount.get(1));
        }

        @Test
        public void testStreamR() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<ByteStream> rows = m.streamRows().toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).count());
        }

        @Test
        public void testStreamR_rowRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            List<ByteStream> rows = m.streamRows(1, 3).toList();
            assertEquals(2, rows.size());
        }

        @Test
        public void testStreamC() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<ByteStream> columnCount = m.streamColumns().toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).count());
        }

        @Test
        public void testStreamC_columnRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<ByteStream> columnCount = m.streamColumns(1, 3).toList();
            assertEquals(2, columnCount.size());
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach_valueConsumer() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Byte> values = new ArrayList<>();
            m.forEach(b -> values.add(b));
            assertEquals(4, values.size());
            assertEquals((byte) 1, values.get(0));
            assertEquals((byte) 2, values.get(1));
        }

        @Test
        public void testForEach_withRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Byte> values = new ArrayList<>();
            m.forEach(1, 3, 1, 3, b -> values.add(b));
            assertEquals(4, values.size());
            assertEquals((byte) 5, values.get(0));
            assertEquals((byte) 6, values.get(1));
        }

        // ============ Points Tests (Inherited) ============

        @Test
        public void testPointsLU2RD() {
            ByteMatrix m = ByteMatrix.of(new byte[3][3]);
            List<Point> points = m.pointsMainDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 2), points.get(2));
        }

        @Test
        public void testPointsRU2LD() {
            ByteMatrix m = ByteMatrix.of(new byte[3][3]);
            List<Point> points = m.pointsAntiDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 2), points.get(0));
            assertEquals(Point.of(1, 1), points.get(1));
            assertEquals(Point.of(2, 0), points.get(2));
        }

        @Test
        public void testPointsH() {
            ByteMatrix m = ByteMatrix.of(new byte[2][2]);
            List<Point> points = m.pointsHorizontal().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(0, 1), points.get(1));
            assertEquals(Point.of(1, 0), points.get(2));
        }

        @Test
        public void testPointsH_singleRow() {
            ByteMatrix m = ByteMatrix.of(new byte[2][3]);
            List<Point> points = m.pointsHorizontal(0).toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(0, 2), points.get(2));
        }

        @Test
        public void testPointsV() {
            ByteMatrix m = ByteMatrix.of(new byte[2][2]);
            List<Point> points = m.pointsVertical().toList();
            assertEquals(4, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(1, 0), points.get(1));
        }

        @Test
        public void testPointsV_singleColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[3][2]);
            List<Point> points = m.pointsVertical(0).toList();
            assertEquals(3, points.size());
            assertEquals(Point.of(0, 0), points.get(0));
            assertEquals(Point.of(2, 0), points.get(2));
        }

        @Test
        public void testPointsR() {
            ByteMatrix m = ByteMatrix.of(new byte[2][2]);
            List<Stream<Point>> rows = m.pointsRows().toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).count());
        }

        @Test
        public void testPointsC() {
            ByteMatrix m = ByteMatrix.of(new byte[2][2]);
            List<Stream<Point>> columnCount = m.pointsColumns().toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).count());
        }

        // ============ Utility Tests (Inherited) ============

        @Test
        public void testElementCount() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertEquals(6, m.elementCount());
        }

        @Test
        public void testElementCount_Empty() {
            ByteMatrix m = ByteMatrix.empty();
            assertEquals(0, m.elementCount());
        }

        @Test
        public void testIsEmpty() {
            ByteMatrix empty = ByteMatrix.empty();
            assertTrue(empty.isEmpty());
            ByteMatrix notEmpty = ByteMatrix.of(new byte[][] { { 1 } });
            assertFalse(notEmpty.isEmpty());
        }

        @Test
        public void testArray() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            ByteMatrix m = ByteMatrix.of(arr);
            byte[][] result = m.backingArray();
            assertSame(arr, result);
        }

        @Test
        public void testIsSameShape() {
            ByteMatrix m1 = ByteMatrix.of(new byte[2][3]);
            ByteMatrix m2 = ByteMatrix.of(new byte[2][3]);
            ByteMatrix m3 = ByteMatrix.of(new byte[3][2]);
            assertTrue(m1.isSameShape(m2));
            assertFalse(m1.isSameShape(m3));
        }

        @Test
        public void testAccept() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            AtomicInteger counter = new AtomicInteger(0);
            m.accept(matrix -> counter.set(matrix.rowCount() * matrix.columnCount()));
            assertEquals(2, counter.get());
        }

        @Test
        public void testApply() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            int result = m.apply(matrix -> matrix.rowCount() * matrix.columnCount());
            assertEquals(2, result);
        }

        // ============ Println Test ============

        @Test
        public void testPrintln() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            String result = m.println();
            assertNotNull(result);
            assertTrue(result.contains("1"));
            assertTrue(result.contains("4"));
        }

        // ============ Equals/HashCode Tests ============

        @Test
        public void testHashCode_equal() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void testEquals_same() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertEquals(m, m);
        }

        @Test
        public void testEquals_equal() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(m1, m2);
        }

        @Test
        public void testEquals_notEqual() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 } });
            assertNotEquals(m1, m2);
        }

        @Test
        public void testEquals_null() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
            assertNotEquals(m, null);
        }

        @Test
        public void testEquals_differentType() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
            assertNotEquals(m, "string");
        }

        // ============ ToString Test ============

        @Test
        public void testToString() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("1"));
            assertTrue(str.contains("4"));
        }
    }

    @Nested
    @Tag("2511")
    class ByteMatrix2511Test extends TestBase {
        @Test
        public void testConstructor_largeMatrix() {
            byte[][] arr = new byte[100][100];
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    arr[i][j] = (byte) ((i + j) % 128);
                }
            }
            ByteMatrix m = new ByteMatrix(arr);
            assertEquals(100, m.rowCount());
            assertEquals(100, m.columnCount());
            assertEquals(0, m.get(0, 0));
            assertEquals(1, m.get(0, 1));
        }

        @Test
        public void testOf_withSingleRow() {
            ByteMatrix m = ByteMatrix.of(new byte[] { 1, 2, 3 });
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(0, 2));
        }

        @Test
        public void testRandom_withLargeLength() {
            ByteMatrix m = ByteMatrix.random(1000);
            assertEquals(1, m.rowCount());
            assertEquals(1000, m.columnCount());
        }

        @Test
        public void testRepeat_withNegative() {
            ByteMatrix m = ByteMatrix.repeat(1, 3, (byte) -10);
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 3; i++) {
                assertEquals(-10, m.get(0, i));
            }
        }

        @Test
        public void testRange_negativeValues() {
            ByteMatrix m = ByteMatrix.range((byte) -5, (byte) 0);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals(-5, m.get(0, 0));
            assertEquals(-1, m.get(0, 4));
        }

        @Test
        public void testDiagonalLU2RD_singleElement() {
            ByteMatrix m = ByteMatrix.mainDiagonal(new byte[] { 42 });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42, m.get(0, 0));
        }

        @Test
        public void testDiagonalRU2LD_empty() {
            ByteMatrix m = ByteMatrix.antiDiagonal(new byte[0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withBothDiagonalsOverlapping() {
            ByteMatrix m = ByteMatrix.diagonals(new byte[] { 1, 2, 3 }, new byte[] { 4, 5, 6 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            // Center element should be from main diagonal (set second)
            assertEquals(2, m.get(1, 1));
        }

        @Test
        public void testUnbox_isAllNulls() {
            Byte[][] boxed = { { null, null }, { null, null } };
            Matrix<Byte> boxedMatrix = Matrix.of(boxed);
            ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertEquals(0, unboxed.get(0, 0));
            assertEquals(0, unboxed.get(1, 1));
        }

        @Test
        public void testComponentType_emptyMatrix() {
            ByteMatrix m = ByteMatrix.empty();
            assertEquals(byte.class, m.componentType());
        }

        @Test
        public void testGet_allPositions() {
            byte[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            ByteMatrix m = ByteMatrix.of(arr);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(arr[i][j], m.get(i, j));
                }
            }
        }

        @Test
        public void testSet_overwriteValue() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            m.set(0, 0, (byte) 10);
            assertEquals(10, m.get(0, 0));
            m.set(0, 0, (byte) 20);
            assertEquals(20, m.get(0, 0));
        }

        @Test
        public void testSet_negativeValues() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 } });
            m.set(0, 0, (byte) -5);
            assertEquals(-5, m.get(0, 0));
        }

        @Test
        public void testUpOf_multipleRows() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            OptionalByte up = m.above(2, 1);
            assertTrue(up.isPresent());
            assertEquals(4, up.get());
        }

        @Test
        public void testDownOf_multipleRows() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            OptionalByte down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3, down.get());
        }

        @Test
        public void testLeftOf_multipleColumns() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
            OptionalByte left = m.left(0, 2);
            assertTrue(left.isPresent());
            assertEquals(2, left.get());
        }

        @Test
        public void testRightOf_multipleColumns() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
            OptionalByte right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2, right.get());
        }

        @Test
        public void testRow_singleRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
            assertArrayEquals(new byte[] { 1, 2, 3 }, m.rowView(0));
        }

        @Test
        public void testColumn_singleColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 }, { 2 }, { 3 } });
            assertArrayEquals(new byte[] { 1, 2, 3 }, m.columnCopy(0));
        }

        @Test
        public void testSetRow_invalidIndex() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(-1, new byte[] { 1, 2 }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(1, new byte[] { 1, 2 }));
        }

        @Test
        public void testSetColumn_invalidIndex() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(-1, new byte[] { 1, 2 }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(2, new byte[] { 1, 2 }));
        }

        @Test
        public void testUpdateRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 0, 0, 0 } });
            m.updateRow(0, val -> (byte) (val * 2));
            assertArrayEquals(new byte[] { 2, 4, 6 }, m.rowView(0));
            assertArrayEquals(new byte[] { 0, 0, 0 }, m.rowView(1));
        }

        @Test
        public void testUpdateRow_multipleRows() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            m.updateRow(1, val -> (byte) (val + 10));
            assertArrayEquals(new byte[] { 1, 2 }, m.rowView(0));
            assertArrayEquals(new byte[] { 13, 14 }, m.rowView(1));
            assertArrayEquals(new byte[] { 5, 6 }, m.rowView(2));
        }

        @Test
        public void testUpdateColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 0 }, { 2, 0 }, { 3, 0 } });
            m.updateColumn(0, val -> (byte) (val * 2));
            assertArrayEquals(new byte[] { 2, 4, 6 }, m.columnCopy(0));
            assertArrayEquals(new byte[] { 0, 0, 0 }, m.columnCopy(1));
        }

        @Test
        public void testUpdateColumn_multipleColumns() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            m.updateColumn(2, val -> (byte) (val + 10));
            assertArrayEquals(new byte[] { 1, 2, 13 }, m.rowView(0));
            assertArrayEquals(new byte[] { 4, 5, 16 }, m.rowView(1));
        }

        @Test
        public void testGetLU2RD_singleElement() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 42 } });
            assertArrayEquals(new byte[] { 42 }, m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            ByteMatrix m = ByteMatrix.of(new byte[2][3]);
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new byte[] { 1, 2 }));
        }

        @Test
        public void testUpdateLU2RD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 0, 0 }, { 0, 2, 0 }, { 0, 0, 3 } });
            m.updateMainDiagonal(val -> (byte) (val * 2));
            assertEquals(2, m.get(0, 0));
            assertEquals(4, m.get(1, 1));
            assertEquals(6, m.get(2, 2));
        }

        @Test
        public void testUpdateLU2RD_allValues() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } });
            m.updateMainDiagonal(val -> (byte) 9);
            assertEquals(9, m.get(0, 0));
            assertEquals(9, m.get(1, 1));
            assertEquals(9, m.get(2, 2));
        }

        @Test
        public void testGetRU2LD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 1 }, { 0, 2, 0 }, { 3, 0, 0 } });
            byte[] diag = m.getAntiDiagonal();
            assertArrayEquals(new byte[] { 1, 2, 3 }, diag);
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD_invalidLength() {
            ByteMatrix m = ByteMatrix.of(new byte[3][3]);
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new byte[] { 1, 2 }));
        }

        @Test
        public void testUpdateRU2LD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 1 }, { 0, 2, 0 }, { 3, 0, 0 } });
            m.updateAntiDiagonal(val -> (byte) (val * 3));
            assertEquals(3, m.get(0, 2));
            assertEquals(6, m.get(1, 1));
            assertEquals(9, m.get(2, 0));
        }

        @Test
        public void testUpdateRU2LD_rectangular() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 1 }, { 2, 0, 0 } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(val -> (byte) (val + 10)));
        }

        @Test
        public void testUpdateAll_withConstant() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.updateAll(val -> (byte) 10);
            assertEquals(10, m.get(0, 0));
            assertEquals(10, m.get(0, 1));
            assertEquals(10, m.get(1, 0));
            assertEquals(10, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_noMatches() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.replaceIf(val -> val > 10, (byte) 0);
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(4, m.get(1, 1));
        }

        // ============ Map Tests ============

        @Test
        public void testMap() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix mapped = m.map(val -> (byte) (val * 2));
            assertEquals(2, mapped.get(0, 0));
            assertEquals(4, mapped.get(0, 1));
            assertEquals(6, mapped.get(1, 0));
            assertEquals(8, mapped.get(1, 1));
            assertEquals(1, m.get(0, 0));
        }

        @Test
        public void testMap_identity() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix mapped = m.map(val -> val);
            assertEquals(1, mapped.get(0, 0));
            assertEquals(2, mapped.get(0, 1));
            assertNotSame(m, mapped);
        }

        @Test
        public void testMapToObj() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            Matrix<String> mapped = m.mapToObj(val -> String.valueOf(val), String.class);
            assertEquals("1", mapped.get(0, 0));
            assertEquals("2", mapped.get(0, 1));
            assertEquals("3", mapped.get(1, 0));
            assertEquals("4", mapped.get(1, 1));
        }

        @Test
        public void testMapToObj_withComplexType() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> mapped = m.mapToObj(val -> val * 10, Integer.class);
            assertEquals(10, mapped.get(0, 0));
            assertEquals(20, mapped.get(0, 1));
            assertEquals(30, mapped.get(1, 0));
            assertEquals(40, mapped.get(1, 1));
        }

        @Test
        public void testFill_withPartialArray() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
            m.copyFrom(0, 0, new byte[][] { { 1, 2 } });
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(0, m.get(0, 2));
            assertEquals(0, m.get(1, 0));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix copy = m.copy();
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(1, copy.get(0, 0));
            assertEquals(2, copy.get(0, 1));
            copy.set(0, 0, (byte) 10);
            assertEquals(1, m.get(0, 0));
        }

        @Test
        public void testCopy_emptyMatrix() {
            ByteMatrix m = ByteMatrix.empty();
            ByteMatrix copy = m.copy();
            assertTrue(copy.isEmpty());
        }

        @Test
        public void testCopy_withRowRange_singleRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            ByteMatrix copy = m.copy(1, 2);
            assertEquals(1, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(3, copy.get(0, 0));
            assertEquals(4, copy.get(0, 1));
        }

        @Test
        public void testCopy_withFullRange_singleCell() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ByteMatrix copy = m.copy(0, 1, 1, 2);
            assertEquals(1, copy.rowCount());
            assertEquals(1, copy.columnCount());
            assertEquals(2, copy.get(0, 0));
        }

        @Test
        public void testExtend_invalidSize() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });

            ByteMatrix extended = m.resize(1, 1);
            assertEquals(1, extended.rowCount());
            assertEquals(1, extended.columnCount());
        }

        @Test
        public void testExtend_noExtension() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix extended = m.resize(1, 2);
            assertEquals(1, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(2, extended.get(0, 1));
        }

        // ============ Transformation Tests ============

        @Test
        public void testReverseH() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            m.flipInPlaceHorizontally();
            assertArrayEquals(new byte[] { 3, 2, 1 }, m.rowView(0));
            assertArrayEquals(new byte[] { 6, 5, 4 }, m.rowView(1));
        }

        @Test
        public void testReverseH_singleRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
            m.flipInPlaceHorizontally();
            assertArrayEquals(new byte[] { 3, 2, 1 }, m.rowView(0));
        }

        @Test
        public void testReverseV() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            m.flipInPlaceVertically();
            assertArrayEquals(new byte[] { 5, 3, 1 }, m.columnCopy(0));
            assertArrayEquals(new byte[] { 6, 4, 2 }, m.columnCopy(1));
        }

        @Test
        public void testReverseV_singleColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 }, { 2 }, { 3 } });
            m.flipInPlaceVertically();
            assertArrayEquals(new byte[] { 3, 2, 1 }, m.columnCopy(0));
        }

        @Test
        public void testFlipH() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ByteMatrix flipped = m.flipHorizontally();
            assertEquals(2, flipped.rowCount());
            assertEquals(3, flipped.columnCount());
            assertArrayEquals(new byte[] { 3, 2, 1 }, flipped.rowView(0));
            assertArrayEquals(new byte[] { 6, 5, 4 }, flipped.rowView(1));
            assertNotSame(m, flipped);
        }

        @Test
        public void testFlipV() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            ByteMatrix flipped = m.flipVertically();
            assertEquals(3, flipped.rowCount());
            assertEquals(2, flipped.columnCount());
            assertArrayEquals(new byte[] { 5, 3, 1 }, flipped.columnCopy(0));
            assertArrayEquals(new byte[] { 6, 4, 2 }, flipped.columnCopy(1));
            assertNotSame(m, flipped);
        }

        @Test
        public void testRotate90_nonSquare() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ByteMatrix rotated = m.rotate90();
            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
        }

        @Test
        public void testTranspose_squareMatrix() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(3, transposed.get(0, 1));
        }

        @Test
        public void testReshape() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3, 4 } });
            ByteMatrix reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
            assertEquals(3, reshaped.get(1, 0));
            assertEquals(4, reshaped.get(1, 1));
        }

        @Test
        public void testReshape_toSingleRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix reshaped = m.reshape(1, 4);
            assertEquals(1, reshaped.rowCount());
            assertEquals(4, reshaped.columnCount());
        }

        @Test
        public void testRepelem() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix repeated = m.repeatElements(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(1, repeated.get(0, 1));
            assertEquals(1, repeated.get(1, 0));
            assertEquals(1, repeated.get(1, 1));
            assertEquals(2, repeated.get(0, 2));
        }

        @Test
        public void testRepelem_singleRepeat() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix repeated = m.repeatElements(1, 1);
            assertEquals(1, repeated.rowCount());
            assertEquals(2, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(2, repeated.get(0, 1));
        }

        @Test
        public void testRepmat() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix repeated = m.repeatMatrix(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(2, repeated.get(0, 1));
            assertEquals(4, repeated.get(3, 3));
        }

        @Test
        public void testRepmat_singleRepeat() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix repeated = m.repeatMatrix(1, 1);
            assertEquals(1, repeated.rowCount());
            assertEquals(2, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
        }

        @Test
        public void testFlatten() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteList flattened = m.flatten();
            assertEquals(4, flattened.size());
            assertEquals(1, flattened.get(0));
            assertEquals(2, flattened.get(1));
            assertEquals(3, flattened.get(2));
            assertEquals(4, flattened.get(3));
        }

        @Test
        public void testFlatten_emptyMatrix() {
            ByteMatrix m = ByteMatrix.empty();
            ByteList flattened = m.flatten();
            assertEquals(0, flattened.size());
        }

        @Test
        public void testVstack_incompatibleColumns() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack_incompatibleRows() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1 }, { 3 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 2 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Boxed Tests ============

        @Test
        public void testBoxed() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Byte> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(Byte.valueOf((byte) 1), boxed.get(0, 0));
            assertEquals(Byte.valueOf((byte) 2), boxed.get(0, 1));
            assertEquals(Byte.valueOf((byte) 3), boxed.get(1, 0));
            assertEquals(Byte.valueOf((byte) 4), boxed.get(1, 1));
        }

        @Test
        public void testBoxed_emptyMatrix() {
            ByteMatrix m = ByteMatrix.empty();
            Matrix<Byte> boxed = m.boxed();
            assertTrue(boxed.isEmpty());
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Byte> diagonal = m.streamMainDiagonal().boxed().toList();
            assertEquals(3, diagonal.size());
            assertEquals(1, diagonal.get(0).byteValue());
            assertEquals(5, diagonal.get(1).byteValue());
            assertEquals(9, diagonal.get(2).byteValue());
        }

        @Test
        public void testStreamRU2LD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Byte> diagonal = m.streamAntiDiagonal().boxed().toList();
            assertEquals(3, diagonal.size());
            assertEquals(3, diagonal.get(0).byteValue());
            assertEquals(5, diagonal.get(1).byteValue());
            assertEquals(7, diagonal.get(2).byteValue());
        }

        @Test
        public void testStreamH() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Byte> elements = m.streamHorizontal().boxed().toList();
            assertEquals(4, elements.size());
            assertEquals(1, elements.get(0).byteValue());
            assertEquals(2, elements.get(1).byteValue());
        }

        @Test
        public void testStreamH_withRowIndex() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Byte> row = m.streamHorizontal(1).boxed().toList();
            assertEquals(2, row.size());
            assertEquals(3, row.get(0).byteValue());
            assertEquals(4, row.get(1).byteValue());
        }

        @Test
        public void testStreamH_withRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            List<Byte> elements = m.streamHorizontal(1, 3).boxed().toList();
            assertEquals(4, elements.size());
            assertEquals(3, elements.get(0).byteValue());
            assertEquals(4, elements.get(1).byteValue());
        }

        @Test
        public void testStreamV() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Byte> elements = m.streamVertical().boxed().toList();
            assertEquals(4, elements.size());
            assertEquals(1, elements.get(0).byteValue());
            assertEquals(3, elements.get(1).byteValue());
        }

        @Test
        public void testStreamV_withColumnIndex() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Byte> col = m.streamVertical(1).boxed().toList();
            assertEquals(2, col.size());
            assertEquals(2, col.get(0).byteValue());
            assertEquals(4, col.get(1).byteValue());
        }

        @Test
        public void testStreamV_withRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<Byte> elements = m.streamVertical(1, 3).boxed().toList();
            assertEquals(4, elements.size());
            assertEquals(2, elements.get(0).byteValue());
            assertEquals(5, elements.get(1).byteValue());
        }

        @Test
        public void testStreamR() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<ByteStream> rows = m.streamRows().toList();
            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).toArray().length);
        }

        @Test
        public void testStreamC() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<ByteStream> columnCount = m.streamColumns().toList();
            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).toArray().length);
        }
        // ============ Point Stream Tests ============

        @Test
        public void testPointsH() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.pointsHorizontal().toList();
            assertEquals(4, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(0, points.get(0).columnIndex());
        }

        @Test
        public void testPointsV() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.pointsVertical().toList();
            assertEquals(4, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(0, points.get(0).columnIndex());
        }

        @Test
        public void testPointsLU2RD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = m.pointsMainDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(0, points.get(0).columnIndex());
        }

        @Test
        public void testPointsRU2LD() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = m.pointsAntiDiagonal().toList();
            assertEquals(3, points.size());
            assertEquals(0, points.get(0).rowIndex());
            assertEquals(2, points.get(0).columnIndex());
        }

        // ============ Adjacent Points Tests ============

        @Test
        public void testAdjacent4Points() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = m.adjacent4Points(1, 1).toList();
            assertEquals(4, points.size());
        }

        @Test
        public void testAdjacent4Points_corner() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.adjacent4Points(0, 0).toList();
            assertEquals(2, points.size());
        }

        @Test
        public void testAdjacent8Points() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Point> points = m.adjacent8Points(1, 1).toList();
            assertEquals(8, points.size());
        }

        @Test
        public void testAdjacent8Points_corner() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Point> points = m.adjacent8Points(0, 0).toList();
            assertEquals(3, points.size());
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            AtomicInteger sum = new AtomicInteger(0);
            m.forEach(val -> sum.addAndGet(val));
            assertEquals(10, sum.get());
        }

        // ============ Utility Tests ============

        @Test
        public void testIsEmpty() {
            assertTrue(ByteMatrix.empty().isEmpty());
            assertFalse(ByteMatrix.of(new byte[][] { { 1 } }).isEmpty());
        }

        @Test
        public void testIsSameShape() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
            assertTrue(m1.isSameShape(m2));
            assertFalse(m1.isSameShape(m3));
        }

        @Test
        public void testArray() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            ByteMatrix m = ByteMatrix.of(arr);
            byte[][] result = m.backingArray();
            assertArrayEquals(arr, result);
        }

        @Test
        public void testEquals() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            assertEquals(m1, m2);
            assertNotEquals(m1, m3);
            assertNotEquals(m1, null);
            assertNotEquals(m1, "not a matrix");
        }

        @Test
        public void testEquals_emptyMatrices() {
            ByteMatrix m1 = ByteMatrix.empty();
            ByteMatrix m2 = ByteMatrix.empty();
            assertEquals(m1, m2);
        }

        @Test
        public void testPrintln() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            String result = m.println();
            assertNotNull(result);
            assertTrue(result.contains("1"));
        }
    }

    @Nested
    @Tag("2512")
    class ByteMatrix2512Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void test_constructor_validArray() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            ByteMatrix m = new ByteMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
        }

        @Test
        public void test_constructor_emptyArray() {
            ByteMatrix m = new ByteMatrix(new byte[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_constructor_singleElement() {
            ByteMatrix m = new ByteMatrix(new byte[][] { { 5 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(5, m.get(0, 0));
        }
        // ============ Factory Method Tests ============

        @Test
        public void test_empty() {
            ByteMatrix empty = ByteMatrix.empty();
            assertTrue(empty.isEmpty());
            assertSame(ByteMatrix.empty(), ByteMatrix.empty());
        }

        @Test
        public void test_of_validArray() {
            byte[][] arr = { { 1, 2 }, { 3, 4 } };
            ByteMatrix m = ByteMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
        }

        @Test
        public void test_of_singleRow() {
            ByteMatrix m = ByteMatrix.of(new byte[] { 1, 2, 3 });
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
        }

        @Test
        public void test_random() {
            ByteMatrix m = ByteMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        @Test
        public void test_repeat() {
            ByteMatrix m = ByteMatrix.repeat(1, 5, (byte) 7);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(7, m.get(0, i));
            }
        }

        @Test
        public void test_range() {
            ByteMatrix m = ByteMatrix.range((byte) 0, (byte) 5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals(0, m.get(0, 0));
            assertEquals(4, m.get(0, 4));
        }

        @Test
        public void test_range_withBy() {
            ByteMatrix m = ByteMatrix.range((byte) 0, (byte) 10, (byte) 2);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            assertEquals(0, m.get(0, 0));
            assertEquals(8, m.get(0, 4));
        }

        @Test
        public void test_rangeClosed() {
            ByteMatrix m = ByteMatrix.rangeClosed((byte) 0, (byte) 5);
            assertEquals(1, m.rowCount());
            assertEquals(6, m.columnCount());
            assertEquals(0, m.get(0, 0));
            assertEquals(5, m.get(0, 5));
        }

        @Test
        public void test_rangeClosed_withBy() {
            ByteMatrix m = ByteMatrix.rangeClosed((byte) 0, (byte) 10, (byte) 2);
            assertEquals(1, m.rowCount());
            assertEquals(6, m.columnCount());
            assertEquals(0, m.get(0, 0));
            assertEquals(10, m.get(0, 5));
        }

        @Test
        public void test_mainDiagonal() {
            ByteMatrix m = ByteMatrix.mainDiagonal(new byte[] { 1, 2, 3 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
            assertEquals(0, m.get(0, 1));
        }

        @Test
        public void test_mainDiagonal_empty() {
            ByteMatrix m = ByteMatrix.mainDiagonal(new byte[0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_antiDiagonal() {
            ByteMatrix m = ByteMatrix.antiDiagonal(new byte[] { 1, 2, 3 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 2));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 0));
        }

        @Test
        public void test_diagonal_both() {
            ByteMatrix m = ByteMatrix.diagonals(new byte[] { 1, 2, 3 }, new byte[] { 4, 5, 6 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(4, m.get(0, 2));
        }

        @Test
        public void test_unbox() {
            Matrix<Byte> boxed = Matrix.of(new Byte[][] { { 1, 2 }, { null, 4 } });
            ByteMatrix primitive = ByteMatrix.unbox(boxed);
            assertEquals(1, primitive.get(0, 0));
            assertEquals(2, primitive.get(0, 1));
            assertEquals(0, primitive.get(1, 0)); // null becomes 0
            assertEquals(4, primitive.get(1, 1));
        }

        // ============ Element Access Tests ============

        @Test
        public void test_get_byIndices() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(4, m.get(1, 1));
        }

        @Test
        public void test_get_byPoint() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(1, m.get(Point.of(0, 0)));
            assertEquals(4, m.get(Point.of(1, 1)));
        }

        @Test
        public void test_set_byIndices() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.set(0, 1, (byte) 10);
            assertEquals(10, m.get(0, 1));
        }

        @Test
        public void test_set_byPoint() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.set(Point.of(1, 0), (byte) 20);
            assertEquals(20, m.get(1, 0));
        }

        // ============ Neighbor Access Tests ============

        @Test
        public void test_upOf_exists() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            OptionalByte up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1, up.get());
        }

        @Test
        public void test_upOf_notExists() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            OptionalByte up = m.above(0, 0);
            assertFalse(up.isPresent());
        }

        @Test
        public void test_downOf_exists() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            OptionalByte down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3, down.get());
        }

        @Test
        public void test_leftOf_exists() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            OptionalByte left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1, left.get());
        }

        @Test
        public void test_rightOf_exists() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            OptionalByte right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2, right.get());
        }
        // ============ Row/Column Access Tests ============

        @Test
        public void test_row() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            byte[] row = m.rowView(0);
            assertArrayEquals(new byte[] { 1, 2, 3 }, row);
        }

        @Test
        public void test_row_invalidIndex() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(5));
        }

        @Test
        public void test_column() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            byte[] col = m.columnCopy(0);
            assertArrayEquals(new byte[] { 1, 4 }, col);
        }

        @Test
        public void test_column_invalidIndex() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(5));
        }

        @Test
        public void test_setRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.setRow(0, new byte[] { 10, 20 });
            assertEquals(10, m.get(0, 0));
            assertEquals(20, m.get(0, 1));
        }

        @Test
        public void test_setRow_invalidLength() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new byte[] { 1 }));
        }

        @Test
        public void test_setColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.setColumn(0, new byte[] { 10, 20 });
            assertEquals(10, m.get(0, 0));
            assertEquals(20, m.get(1, 0));
        }

        @Test
        public void test_setColumn_invalidLength() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new byte[] { 1 }));
        }

        @Test
        public void test_updateRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.updateRow(0, val -> (byte) (val * 2));
            assertEquals(2, m.get(0, 0));
            assertEquals(4, m.get(0, 1));
        }

        @Test
        public void test_updateColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.updateColumn(0, val -> (byte) (val * 2));
            assertEquals(2, m.get(0, 0));
            assertEquals(6, m.get(1, 0));
        }

        // ============ Diagonal Access Tests ============

        @Test
        public void test_getMainDiagonal() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            byte[] diag = m.getMainDiagonal();
            assertArrayEquals(new byte[] { 1, 5, 9 }, diag);
        }

        @Test
        public void test_getMainDiagonal_nonSquare() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void test_setMainDiagonal() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.setMainDiagonal(new byte[] { 10, 20, 30 });
            assertEquals(10, m.get(0, 0));
            assertEquals(20, m.get(1, 1));
            assertEquals(30, m.get(2, 2));
        }

        @Test
        public void test_setMainDiagonal_invalidLength() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new byte[] { 1 }));
        }

        @Test
        public void test_updateMainDiagonal() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateMainDiagonal(val -> (byte) (val * 2));
            assertEquals(2, m.get(0, 0));
            assertEquals(10, m.get(1, 1));
            assertEquals(18, m.get(2, 2));
        }

        @Test
        public void test_getAntiDiagonal() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            byte[] diag = m.getAntiDiagonal();
            assertArrayEquals(new byte[] { 3, 5, 7 }, diag);
        }

        @Test
        public void test_setAntiDiagonal() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.setAntiDiagonal(new byte[] { 10, 20, 30 });
            assertEquals(10, m.get(0, 2));
            assertEquals(20, m.get(1, 1));
            assertEquals(30, m.get(2, 0));
        }

        @Test
        public void test_updateAntiDiagonal() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateAntiDiagonal(val -> (byte) (val * 2));
            assertEquals(6, m.get(0, 2));
            assertEquals(10, m.get(1, 1));
            assertEquals(14, m.get(2, 0));
        }

        // ============ Update Tests ============

        @Test
        public void test_updateAll_unaryOperator() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.updateAll(val -> (byte) (val * 2));
            assertEquals(2, m.get(0, 0));
            assertEquals(4, m.get(0, 1));
            assertEquals(6, m.get(1, 0));
            assertEquals(8, m.get(1, 1));
        }

        @Test
        public void test_updateAll_biFunction() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
            m.updateAll((i, j) -> (byte) (i + j));
            assertEquals(0, m.get(0, 0));
            assertEquals(1, m.get(0, 1));
            assertEquals(1, m.get(1, 0));
            assertEquals(2, m.get(1, 1));
        }

        @Test
        public void test_replaceIf_predicate() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.replaceIf(val -> val < 3, (byte) 0);
            assertEquals(0, m.get(0, 0));
            assertEquals(0, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(4, m.get(1, 1));
        }

        @Test
        public void test_replaceIf_biPredicate() {
            ByteMatrix m = ByteMatrix.of(new byte[3][3]);
            m.replaceIf((i, j) -> i == j, (byte) 1);
            assertEquals(1, m.get(0, 0));
            assertEquals(0, m.get(0, 1));
            assertEquals(1, m.get(1, 1));
        }

        // ============ Map Tests ============

        @Test
        public void test_map() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix doubled = m.map(val -> (byte) (val * 2));
            assertEquals(2, doubled.get(0, 0));
            assertEquals(4, doubled.get(0, 1));
            // Original unchanged
            assertEquals(1, m.get(0, 0));
        }

        @Test
        public void test_mapToObj() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            Matrix<String> stringMatrix = m.mapToObj(val -> "B" + val, String.class);
            assertEquals("B1", stringMatrix.get(0, 0));
            assertEquals("B2", stringMatrix.get(0, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void test_fill_singleValue() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.fill((byte) 0);
            assertEquals(0, m.get(0, 0));
            assertEquals(0, m.get(0, 1));
            assertEquals(0, m.get(1, 0));
            assertEquals(0, m.get(1, 1));
        }

        @Test
        public void test_fill_array() {
            ByteMatrix m = ByteMatrix.of(new byte[3][3]);
            m.copyFrom(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
            assertEquals(4, m.get(1, 1));
            assertEquals(0, m.get(2, 2));
        }

        @Test
        public void test_fill_arrayWithPosition() {
            ByteMatrix m = ByteMatrix.of(new byte[4][4]);
            m.copyFrom(1, 1, new byte[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(0, m.get(0, 0));
            assertEquals(1, m.get(1, 1));
            assertEquals(2, m.get(1, 2));
            assertEquals(3, m.get(2, 1));
            assertEquals(4, m.get(2, 2));
        }

        // ============ Copy Tests ============

        @Test
        public void test_copy() {
            ByteMatrix original = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix copy = original.copy();
            assertEquals(original.rowCount(), copy.rowCount());
            assertEquals(original.columnCount(), copy.columnCount());
            assertEquals(1, copy.get(0, 0));
            // Modify copy
            copy.set(0, 0, (byte) 99);
            assertEquals(1, original.get(0, 0)); // Original unchanged
        }

        @Test
        public void test_copy_fullRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            ByteMatrix copy = m.copy(0, 2, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(2, copy.get(0, 0)); // From (0,1)
            assertEquals(3, copy.get(0, 1)); // From (0,2)
        }

        // ============ Transformation Tests ============

        @Test
        public void test_rotate90() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3, rotated.get(0, 0));
            assertEquals(1, rotated.get(0, 1));
        }

        @Test
        public void test_rotate180() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix rotated = m.rotate180();
            assertEquals(4, rotated.get(0, 0));
            assertEquals(3, rotated.get(0, 1));
            assertEquals(2, rotated.get(1, 0));
            assertEquals(1, rotated.get(1, 1));
        }

        @Test
        public void test_rotate270() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
        }

        @Test
        public void test_transpose() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ByteMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1, transposed.get(0, 0));
            assertEquals(4, transposed.get(0, 1));
            assertEquals(2, transposed.get(1, 0));
            assertEquals(5, transposed.get(1, 1));
        }

        @Test
        public void test_flipHorizontally() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix flipped = m.flipHorizontally();
            assertEquals(2, flipped.get(0, 0));
            assertEquals(1, flipped.get(0, 1));
        }

        @Test
        public void test_flipVertically() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix flipped = m.flipVertically();
            assertEquals(3, flipped.get(0, 0));
            assertEquals(4, flipped.get(0, 1));
        }

        @Test
        public void test_flipInPlaceHorizontally() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.flipInPlaceHorizontally();
            assertEquals(2, m.get(0, 0));
            assertEquals(1, m.get(0, 1));
        }

        @Test
        public void test_flipInPlaceVertically() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.flipInPlaceVertically();
            assertEquals(3, m.get(0, 0));
            assertEquals(4, m.get(0, 1));
        }

        // ============ Reshape Tests ============

        @Test
        public void test_reshape() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            ByteMatrix reshaped = m.reshape(3, 2);
            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
        }

        // ============ Repeat Tests ============

        @Test
        public void test_repeatElements() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix repeated = m.repeatElements(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(1, repeated.get(0, 1));
            assertEquals(1, repeated.get(1, 0));
            assertEquals(1, repeated.get(1, 1));
        }

        @Test
        public void test_repeatMatrix() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix tiled = m.repeatMatrix(2, 2);
            assertEquals(4, tiled.rowCount());
            assertEquals(4, tiled.columnCount());
            assertEquals(1, tiled.get(0, 0));
            assertEquals(2, tiled.get(0, 1));
            assertEquals(1, tiled.get(2, 2));
        }

        // ============ Flatten Tests ============

        @Test
        public void test_flatten() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteList flat = m.flatten();
            assertEquals(4, flat.size());
            assertEquals(1, flat.get(0));
            assertEquals(2, flat.get(1));
            assertEquals(3, flat.get(2));
            assertEquals(4, flat.get(3));
        }

        @Test
        public void test_applyOnFlattened() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            m.applyOnFlattened(arr -> {
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = (byte) (arr[i] * 2);
                }
            });
            assertEquals(2, m.get(0, 0));
            assertEquals(4, m.get(0, 1));
        }

        // ============ Stack Tests ============

        @Test
        public void test_stackVertically() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 } });
            ByteMatrix stacked = m1.stackVertically(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(3, stacked.get(1, 0));
        }

        @Test
        public void test_vstack_differentCols() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4, 5 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void test_stackHorizontally() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1 }, { 3 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 2 }, { 4 } });
            ByteMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(2, stacked.get(0, 1));
        }

        @Test
        public void test_hstack_differentRows() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Tests ============

        @Test
        public void test_add() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix result = m1.add(m2);
            assertEquals(6, result.get(0, 0));
            assertEquals(8, result.get(0, 1));
            assertEquals(10, result.get(1, 0));
            assertEquals(12, result.get(1, 1));
        }

        @Test
        public void test_subtract() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix result = m1.subtract(m2);
            assertEquals(4, result.get(0, 0));
            assertEquals(4, result.get(0, 1));
            assertEquals(4, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void test_multiply() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 2, 3 }, { 4, 5 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix result = m1.multiply(m2);
            assertEquals(11, result.get(0, 0)); // 2*1 + 3*3 = 11
            assertEquals(16, result.get(0, 1)); // 2*2 + 3*4 = 16
            assertEquals(19, result.get(1, 0)); // 4*1 + 5*3 = 19
            assertEquals(28, result.get(1, 1)); // 4*2 + 5*4 = 28
        }

        // ============ Conversion Tests ============

        @Test
        public void test_boxed() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Byte> boxed = m.boxed();
            assertEquals((byte) 1, boxed.get(0, 0));
            assertEquals((byte) 2, boxed.get(0, 1));
        }

        @Test
        public void test_toIntMatrix() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix intMatrix = m.toIntMatrix();
            assertEquals(1, intMatrix.get(0, 0));
            assertEquals(2, intMatrix.get(0, 1));
        }

        @Test
        public void test_toLongMatrix() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            LongMatrix longMatrix = m.toLongMatrix();
            assertEquals(1L, longMatrix.get(0, 0));
            assertEquals(2L, longMatrix.get(0, 1));
        }

        @Test
        public void test_toFloatMatrix() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            FloatMatrix floatMatrix = m.toFloatMatrix();
            assertEquals(1.0f, floatMatrix.get(0, 0));
            assertEquals(2.0f, floatMatrix.get(0, 1));
        }

        @Test
        public void test_toDoubleMatrix() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            DoubleMatrix doubleMatrix = m.toDoubleMatrix();
            assertEquals(1.0, doubleMatrix.get(0, 0));
            assertEquals(2.0, doubleMatrix.get(0, 1));
        }

        // ============ ZipWith Tests ============

        @Test
        public void test_zipWith_twoMatrices() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix result = m1.zipWith(m2, (a, b) -> (byte) (a + b));
            assertEquals(6, result.get(0, 0));
            assertEquals(8, result.get(0, 1));
        }

        @Test
        public void test_zipWith_threeMatrices() {
            ByteMatrix m1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix m2 = ByteMatrix.of(new byte[][] { { 1, 1 }, { 1, 1 } });
            ByteMatrix m3 = ByteMatrix.of(new byte[][] { { 2, 2 }, { 2, 2 } });
            ByteMatrix result = m1.zipWith(m2, m3, (a, b, c) -> (byte) (a + b + c));
            assertEquals(4, result.get(0, 0));
            assertEquals(5, result.get(0, 1));
        }

        // ============ Stream Tests ============

        @Test
        public void test_streamMainDiagonal() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            byte[] diag = m.streamMainDiagonal().toArray();
            assertArrayEquals(new byte[] { 1, 5, 9 }, diag);
        }

        @Test
        public void test_streamAntiDiagonal() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            byte[] diag = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new byte[] { 3, 5, 7 }, diag);
        }

        @Test
        public void test_streamHorizontal() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            byte[] elements = m.streamHorizontal().toArray();
            assertArrayEquals(new byte[] { 1, 2, 3, 4 }, elements);
        }

        @Test
        public void test_streamH_singleRow() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            byte[] row = m.streamHorizontal(1).toArray();
            assertArrayEquals(new byte[] { 3, 4 }, row);
        }

        @Test
        public void test_streamH_rowRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            byte[] elements = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new byte[] { 3, 4, 5, 6 }, elements);
        }

        @Test
        public void test_streamVertical() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            byte[] elements = m.streamVertical().toArray();
            assertArrayEquals(new byte[] { 1, 3, 2, 4 }, elements);
        }

        @Test
        public void test_streamV_singleColumn() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            byte[] col = m.streamVertical(0).toArray();
            assertArrayEquals(new byte[] { 1, 3 }, col);
        }

        @Test
        public void test_streamV_columnRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            byte[] elements = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new byte[] { 2, 5, 3, 6 }, elements);
        }

        @Test
        public void test_streamRows() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<byte[]> rows = m.streamRows().map(ByteStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new byte[] { 1, 2 }, rows.get(0));
        }

        @Test
        public void test_streamR_rowRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            List<byte[]> rows = m.streamRows(1, 3).map(ByteStream::toArray).toList();
            assertEquals(2, rows.size());
        }

        @Test
        public void test_streamColumns() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<byte[]> columnCount = m.streamColumns().map(ByteStream::toArray).toList();
            assertEquals(2, columnCount.size());
            assertArrayEquals(new byte[] { 1, 3 }, columnCount.get(0));
        }

        @Test
        public void test_streamC_columnRange() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<byte[]> columnCount = m.streamColumns(0, 2).map(ByteStream::toArray).toList();
            assertEquals(2, columnCount.size());
        }

        // ============ Extend Tests ============

        @Test
        public void test_extend_twoParams() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(0, extended.get(2, 2));
        }

        @Test
        public void test_extend_twoParamsWithDefault() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix extended = m.resize(3, 3, (byte) 9);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(9, extended.get(2, 2));
        }

        @Test
        public void test_extend_fourParams() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1, extended.get(1, 1));
        }

        @Test
        public void test_extend_fourParamsWithDefault() {
            ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix extended = m.extend(1, 1, 1, 1, (byte) 9);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(9, extended.get(0, 0));
            assertEquals(9, extended.get(3, 3));
        }
    }

    @Nested
    class JavadocExampleMatrixGroup1Test_ByteMatrix extends TestBase {
        // ==================== ByteMatrix ====================

        @Test
        public void testByteMatrix_repeat() {
            ByteMatrix matrix = ByteMatrix.repeat(2, 3, (byte) 1);
            // Result: [[1, 1, 1], [1, 1, 1]]
            assertEquals(2, matrix.rowCount());
            assertEquals(3, matrix.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals((byte) 1, matrix.get(i, j));
                }
            }
        }

        @Test
        public void testByteMatrix_rowView() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            byte[] firstRow = matrix.rowView(0);
            assertArrayEquals(new byte[] { 1, 2, 3 }, firstRow);
        }

        @Test
        public void testByteMatrix_columnCopy() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            byte[] firstColumn = matrix.columnCopy(0);
            assertArrayEquals(new byte[] { 1, 4 }, firstColumn);
        }

        @Test
        public void testByteMatrix_getMainDiagonal() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            byte[] diagonal = matrix.getMainDiagonal();
            assertArrayEquals(new byte[] { 1, 5, 9 }, diagonal);
        }

        @Test
        public void testByteMatrix_getAntiDiagonal() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            byte[] diagonal = matrix.getAntiDiagonal();
            assertArrayEquals(new byte[] { 3, 5, 7 }, diagonal);
        }

        @Test
        public void testByteMatrix_resize_default() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix extended = matrix.resize(3, 3);
            // Result: [[1, 2, 0], [3, 4, 0], [0, 0, 0]]
            assertEquals((byte) 1, extended.get(0, 0));
            assertEquals((byte) 2, extended.get(0, 1));
            assertEquals((byte) 0, extended.get(0, 2));
            assertEquals((byte) 3, extended.get(1, 0));
            assertEquals((byte) 4, extended.get(1, 1));
            assertEquals((byte) 0, extended.get(1, 2));
            assertEquals((byte) 0, extended.get(2, 0));
            assertEquals((byte) 0, extended.get(2, 1));
            assertEquals((byte) 0, extended.get(2, 2));
        }

        @Test
        public void testByteMatrix_resize_withFill() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix extended = matrix.resize(3, 4, (byte) 9);
            // Result: [[1, 2, 9, 9], [3, 4, 9, 9], [9, 9, 9, 9]]
            assertEquals((byte) 1, extended.get(0, 0));
            assertEquals((byte) 2, extended.get(0, 1));
            assertEquals((byte) 9, extended.get(0, 2));
            assertEquals((byte) 9, extended.get(0, 3));
            assertEquals((byte) 3, extended.get(1, 0));
            assertEquals((byte) 4, extended.get(1, 1));
            assertEquals((byte) 9, extended.get(1, 2));
            assertEquals((byte) 9, extended.get(1, 3));
            for (int j = 0; j < 4; j++) {
                assertEquals((byte) 9, extended.get(2, j));
            }

            // Truncate
            ByteMatrix truncated = matrix.resize(1, 1, (byte) 0);
            assertEquals((byte) 1, truncated.get(0, 0));
        }

        @Test
        public void testByteMatrix_extend_default() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix extended = matrix.extend(1, 1, 1, 1);
            // Result: [[0, 0, 0, 0], [0, 1, 2, 0], [0, 0, 0, 0]]
            assertEquals(3, extended.rowCount());
            assertEquals(4, extended.columnCount());
            for (int j = 0; j < 4; j++) {
                assertEquals((byte) 0, extended.get(0, j));
                assertEquals((byte) 0, extended.get(2, j));
            }
            assertEquals((byte) 0, extended.get(1, 0));
            assertEquals((byte) 1, extended.get(1, 1));
            assertEquals((byte) 2, extended.get(1, 2));
            assertEquals((byte) 0, extended.get(1, 3));
        }

        @Test
        public void testByteMatrix_extend_withFill() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 } });
            ByteMatrix padded = matrix.extend(1, 1, 2, 2, (byte) 9);
            // Result: [[9, 9, 9, 9, 9, 9], [9, 9, 1, 2, 9, 9], [9, 9, 9, 9, 9, 9]]
            assertEquals(3, padded.rowCount());
            assertEquals(6, padded.columnCount());
            assertEquals((byte) 9, padded.get(0, 0));
            assertEquals((byte) 9, padded.get(1, 0));
            assertEquals((byte) 9, padded.get(1, 1));
            assertEquals((byte) 1, padded.get(1, 2));
            assertEquals((byte) 2, padded.get(1, 3));
            assertEquals((byte) 9, padded.get(1, 4));
            assertEquals((byte) 9, padded.get(1, 5));
        }

        @Test
        public void testByteMatrix_flipInPlaceHorizontally() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            matrix.flipInPlaceHorizontally();
            // matrix is now: [[3, 2, 1], [6, 5, 4]]
            assertArrayEquals(new byte[] { 3, 2, 1 }, matrix.rowView(0));
            assertArrayEquals(new byte[] { 6, 5, 4 }, matrix.rowView(1));
        }

        @Test
        public void testByteMatrix_flipInPlaceVertically() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            matrix.flipInPlaceVertically();
            // matrix is now: [[5, 6], [3, 4], [1, 2]]
            assertArrayEquals(new byte[] { 5, 6 }, matrix.rowView(0));
            assertArrayEquals(new byte[] { 3, 4 }, matrix.rowView(1));
            assertArrayEquals(new byte[] { 1, 2 }, matrix.rowView(2));
        }

        @Test
        public void testByteMatrix_flatten() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteList list = matrix.flatten();
            // Returns ByteList containing [1, 2, 3, 4]
            assertEquals(4, list.size());
            assertEquals((byte) 1, list.get(0));
            assertEquals((byte) 2, list.get(1));
            assertEquals((byte) 3, list.get(2));
            assertEquals((byte) 4, list.get(3));
        }

        @Test
        public void testByteMatrix_applyOnFlattened() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 5, 3 }, { 4, 1 } });
            matrix.applyOnFlattened(arr -> java.util.Arrays.sort(arr));
            // matrix is now [[1, 3], [4, 5]]
            assertEquals((byte) 1, matrix.get(0, 0));
            assertEquals((byte) 3, matrix.get(0, 1));
            assertEquals((byte) 4, matrix.get(1, 0));
            assertEquals((byte) 5, matrix.get(1, 1));
        }

        @Test
        public void testByteMatrix_toString() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertEquals("[[1, 2], [3, 4]]", matrix.toString());

            ByteMatrix empty = ByteMatrix.empty();
            assertEquals("[]", empty.toString());
        }
    }

    @Nested
    class JavadocExampleMatrixTest_ByteMatrix extends TestBase {
        // ==================== ByteMatrix Javadoc Examples ====================

        @Test
        public void testByteMatrixEmptyRowCount() {
            ByteMatrix matrix = ByteMatrix.empty();
            assertEquals(0, matrix.rowCount());
        }

        @Test
        public void testByteMatrixOfGet() {
            // ByteMatrix.java: ByteMatrix matrix = ByteMatrix.of(new byte[][] {{1, 2, 3}, {4, 5, 6}});
            // matrix.get(1, 2) returns 6
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertEquals((byte) 6, matrix.get(1, 2));
        }

        @Test
        public void testByteMatrixRange() {
            // ByteMatrix.java: ByteMatrix range = ByteMatrix.range((byte)1, (byte)5);
            // Creates matrix: [[1, 2, 3, 4]]
            ByteMatrix range = ByteMatrix.range((byte) 1, (byte) 5);
            assertEquals("[[1, 2, 3, 4]]", range.toString());
        }

        @Test
        public void testByteMatrixRangeWithStep() {
            // ByteMatrix.java: ByteMatrix range = ByteMatrix.range((byte)0, (byte)10, (byte)2);    // Creates [[0, 2, 4, 6, 8]]
            ByteMatrix range = ByteMatrix.range((byte) 0, (byte) 10, (byte) 2);
            assertEquals("[[0, 2, 4, 6, 8]]", range.toString());
        }

        @Test
        public void testByteMatrixRangeDescending() {
            // ByteMatrix.java: ByteMatrix desc = ByteMatrix.range((byte)10, (byte)0, (byte)-2);    // Creates [[10, 8, 6, 4, 2]]
            ByteMatrix desc = ByteMatrix.range((byte) 10, (byte) 0, (byte) -2);
            assertEquals("[[10, 8, 6, 4, 2]]", desc.toString());
        }

        @Test
        public void testByteMatrixRangeClosed() {
            // ByteMatrix.java: ByteMatrix range = ByteMatrix.rangeClosed((byte)1, (byte)4);
            // Creates matrix: [[1, 2, 3, 4]]
            ByteMatrix range = ByteMatrix.rangeClosed((byte) 1, (byte) 4);
            assertEquals("[[1, 2, 3, 4]]", range.toString());
        }

        @Test
        public void testByteMatrixRangeClosedWithStep() {
            // ByteMatrix.java: ByteMatrix range = ByteMatrix.rangeClosed((byte)0, (byte)8, (byte)2);     // Creates [[0, 2, 4, 6, 8]]
            ByteMatrix range = ByteMatrix.rangeClosed((byte) 0, (byte) 8, (byte) 2);
            assertEquals("[[0, 2, 4, 6, 8]]", range.toString());
        }

        @Test
        public void testByteMatrixRangeClosedPartial() {
            // ByteMatrix.java: ByteMatrix partial = ByteMatrix.rangeClosed((byte)0, (byte)9, (byte)2);   // Creates [[0, 2, 4, 6, 8]] (9 not reachable)
            ByteMatrix partial = ByteMatrix.rangeClosed((byte) 0, (byte) 9, (byte) 2);
            assertEquals("[[0, 2, 4, 6, 8]]", partial.toString());
        }

        @Test
        public void testByteMatrixRangeClosedDescending() {
            // ByteMatrix.java: ByteMatrix desc = ByteMatrix.rangeClosed((byte)10, (byte)0, (byte)-2);    // Creates [[10, 8, 6, 4, 2, 0]]
            ByteMatrix desc = ByteMatrix.rangeClosed((byte) 10, (byte) 0, (byte) -2);
            assertEquals("[[10, 8, 6, 4, 2, 0]]", desc.toString());
        }

        @Test
        public void testByteMatrixZipWith2() {
            // ByteMatrix.java: ByteMatrix result = matrix1.zipWith(matrix2, (a, b) -> (byte)(a * b));
            // result is: [[5, 12], [21, 32]]
            ByteMatrix matrix1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix matrix2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix result = matrix1.zipWith(matrix2, (a, b) -> (byte) (a * b));
            assertEquals("[[5, 12], [21, 32]]", result.toString());
        }

        @Test
        public void testByteMatrixZipWith3() {
            // ByteMatrix.java: result is: [[15, 18], [21, 24]]
            ByteMatrix matrix1 = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix matrix2 = ByteMatrix.of(new byte[][] { { 5, 6 }, { 7, 8 } });
            ByteMatrix matrix3 = ByteMatrix.of(new byte[][] { { 9, 10 }, { 11, 12 } });
            ByteMatrix result = matrix1.zipWith(matrix2, matrix3, (a, b, c) -> (byte) (a + b + c));
            assertEquals("[[15, 18], [21, 24]]", result.toString());
        }

        @Test
        public void testByteMatrixToString() {
            // ByteMatrix.java: System.out.println(matrix.toString());   // Output: [[1, 2], [3, 4]]
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            assertEquals("[[1, 2], [3, 4]]", matrix.toString());
        }

        @Test
        public void testByteMatrixEmptyToString() {
            // ByteMatrix.java: System.out.println(empty.toString());   // Output: []
            ByteMatrix empty = ByteMatrix.empty();
            assertEquals("[]", empty.toString());
        }

        @Test
        public void testByteMatrixMainDiagonal() {
            // ByteMatrix.java: ByteMatrix matrix = ByteMatrix.mainDiagonal(new byte[] {1, 2, 3});
            // Creates 3x3 matrix with diagonal [1, 2, 3] and zeros elsewhere
            ByteMatrix matrix = ByteMatrix.mainDiagonal(new byte[] { 1, 2, 3 });
            assertEquals(3, matrix.rowCount());
            assertEquals(3, matrix.columnCount());
            assertEquals((byte) 1, matrix.get(0, 0));
            assertEquals((byte) 2, matrix.get(1, 1));
            assertEquals((byte) 3, matrix.get(2, 2));
            assertEquals((byte) 0, matrix.get(0, 1));
            assertEquals((byte) 0, matrix.get(1, 0));
        }

        @Test
        public void testByteMatrixAntiDiagonal() {
            // ByteMatrix.java: ByteMatrix matrix = ByteMatrix.antiDiagonal(new byte[] {1, 2, 3});
            // Creates 3x3 matrix with anti-diagonal [1, 2, 3] and zeros elsewhere
            ByteMatrix matrix = ByteMatrix.antiDiagonal(new byte[] { 1, 2, 3 });
            assertEquals(3, matrix.rowCount());
            assertEquals(3, matrix.columnCount());
            assertEquals((byte) 1, matrix.get(0, 2));
            assertEquals((byte) 2, matrix.get(1, 1));
            assertEquals((byte) 3, matrix.get(2, 0));
            assertEquals((byte) 0, matrix.get(0, 0));
        }

        @Test
        public void testByteMatrixForEach() {
            // ByteMatrix.java: matrix.forEach(value -> System.out.print(value + " "));
            // Output: 1 2 3 4
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            List<Byte> values = new ArrayList<>();
            matrix.forEach(value -> values.add(value));
            assertEquals(4, values.size());
            assertEquals((byte) 1, values.get(0));
            assertEquals((byte) 2, values.get(1));
            assertEquals((byte) 3, values.get(2));
            assertEquals((byte) 4, values.get(3));
        }

        @Test
        public void testByteMatrixForEachWithRange() {
            // ByteMatrix.java: matrix.forEach(1, 3, 1, 3, value -> System.out.print(value + " "));
            // Output: 5 6 8 9  (processes elements in rows 1-2, columns 1-2)
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Byte> values = new ArrayList<>();
            matrix.forEach(1, 3, 1, 3, value -> values.add(value));
            assertEquals(4, values.size());
            assertEquals((byte) 5, values.get(0));
            assertEquals((byte) 6, values.get(1));
            assertEquals((byte) 8, values.get(2));
            assertEquals((byte) 9, values.get(3));
        }
    }

    @Nested
    class MatrixZeroColumnRowStreamTest_ByteMatrix extends TestBase {
        @Test
        public void testByteMatrixRowsForZeroColumnMatrix() {
            final ByteMatrix matrix = ByteMatrix.of(new byte[][] { {}, {}, {} });
            final List<byte[]> rows = matrix.streamRows().map(ByteStream::toArray).toList();

            assertEquals(3, rows.size());
            assertArrayEquals(new byte[0], rows.get(0));
            assertArrayEquals(new byte[0], rows.get(1));
            assertArrayEquals(new byte[0], rows.get(2));
        }
    }

    @Nested
    class PrimitiveMatrixUpdateAllNullValidationTest_ByteMatrix extends TestBase {
        @Test
        public void testByteMatrixUpdateAllNullOperator() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix emptyLike = ByteMatrix.of(new byte[][] { {}, {} });
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.ByteUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Byte, RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.ByteUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.ByteUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.BytePredicate<RuntimeException>) null, (byte) 0));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, (byte) 0));

            assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.ByteUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.ByteUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.BytePredicate<RuntimeException>) null, (byte) 0));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, (byte) 0));
        }

        @Test
        public void testByteMatrixStackAndMultiplyRejectNullMatrix() {
            ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 } });
            assertThrows(IllegalArgumentException.class, () -> matrix.stackVertically(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.stackHorizontally(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.add(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.subtract(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.multiply(null));
        }
    }

    // === Missing coverage: resize, copyFrom, flipInPlace ===

    @Test
    public void testResize_expand() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix resized = m.resize(3, 3);
        assertEquals(3, resized.rowCount());
        assertEquals(3, resized.columnCount());
        assertEquals(1, resized.get(0, 0));
        assertEquals(2, resized.get(0, 1));
        assertEquals(0, resized.get(0, 2));
        assertEquals(3, resized.get(1, 0));
        assertEquals(0, resized.get(2, 0));
        assertEquals(0, resized.get(2, 2));
    }

    @Test
    public void testResize_shrink() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        ByteMatrix resized = m.resize(2, 2);
        assertEquals(2, resized.rowCount());
        assertEquals(2, resized.columnCount());
        assertEquals(1, resized.get(0, 0));
        assertEquals(2, resized.get(0, 1));
        assertEquals(4, resized.get(1, 0));
        assertEquals(5, resized.get(1, 1));
    }

    @Test
    public void testResize_withDefaultValue() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
        ByteMatrix resized = m.resize(2, 3, (byte) 99);
        assertEquals(2, resized.rowCount());
        assertEquals(3, resized.columnCount());
        assertEquals(1, resized.get(0, 0));
        assertEquals(99, resized.get(0, 1));
        assertEquals(99, resized.get(0, 2));
        assertEquals(99, resized.get(1, 0));
        assertEquals(99, resized.get(1, 1));
        assertEquals(99, resized.get(1, 2));
    }

    @Test
    public void testResize_toEmpty() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix resized = m.resize(0, 0);
        assertEquals(0, resized.rowCount());
        assertTrue(resized.isEmpty());
    }

    @Test
    public void testResize_negativeThrows() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
        assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> m.resize(1, -1));
    }

    @Test
    public void testCopyFrom_fullOverwrite() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0 }, { 0, 0 } });
        m.copyFrom(new byte[][] { { 10, 20 }, { 30, 40 } });
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(0, 1));
        assertEquals(30, m.get(1, 0));
        assertEquals(40, m.get(1, 1));
    }

    @Test
    public void testCopyFrom_partialOverwrite() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        m.copyFrom(new byte[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(0, m.get(0, 2));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
        assertEquals(0, m.get(2, 0));
    }

    @Test
    public void testCopyFrom_withOffset() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        m.copyFrom(1, 1, new byte[][] { { 5, 6 }, { 7, 8 } });
        assertEquals(0, m.get(0, 0));
        assertEquals(0, m.get(1, 0));
        assertEquals(5, m.get(1, 1));
        assertEquals(6, m.get(1, 2));
        assertEquals(7, m.get(2, 1));
        assertEquals(8, m.get(2, 2));
    }

    @Test
    public void testCopyFrom_emptySource() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        m.copyFrom(new byte[0][0]);
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void testCopyFrom_negativeIndexThrows() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 } });
        assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, new byte[][] { { 1 } }));
        assertThrows(IllegalArgumentException.class, () -> m.copyFrom(0, -1, new byte[][] { { 1 } }));
    }

    @Test
    public void testFlipInPlaceHorizontally() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.flipInPlaceHorizontally();
        assertEquals(3, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(1, m.get(0, 2));
        assertEquals(6, m.get(1, 0));
        assertEquals(5, m.get(1, 1));
        assertEquals(4, m.get(1, 2));
    }

    @Test
    public void testFlipInPlaceHorizontally_singleColumn() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1 }, { 2 } });
        m.flipInPlaceHorizontally();
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 0));
    }

    @Test
    public void testFlipInPlaceVertically() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        m.flipInPlaceVertically();
        assertEquals(5, m.get(0, 0));
        assertEquals(6, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
        assertEquals(1, m.get(2, 0));
        assertEquals(2, m.get(2, 1));
    }

    @Test
    public void testFlipInPlaceVertically_singleRow() {
        ByteMatrix m = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        m.flipInPlaceVertically();
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(0, 2));
    }

    @Test
    public void testStreamHorizontalIteratorAdvanceAndExhaustion_SingleValueRemaining() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        var iterator = matrix.streamHorizontal(0, 2).iterator();

        assertTrue(iterator instanceof com.landawn.abacus.util.stream.ByteIteratorEx);

        com.landawn.abacus.util.stream.ByteIteratorEx ex = (com.landawn.abacus.util.stream.ByteIteratorEx) iterator;
        ex.advance(3);
        assertEquals(1L, ex.count());
        assertEquals((byte) 4, ex.nextByte());
        ex.advance(10);
        assertEquals(0L, ex.count());
        assertThrows(java.util.NoSuchElementException.class, ex::nextByte);
    }

}
