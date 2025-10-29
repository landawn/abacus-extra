package com.landawn.abacus.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalByte;
import com.landawn.abacus.util.stream.ByteStream;

public class ByteMatrixTest extends TestBase {

    @Test
    public void testConstructor() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = new ByteMatrix(a);
        Assertions.assertEquals(2, matrix.rows);
        Assertions.assertEquals(2, matrix.cols);

        ByteMatrix nullMatrix = new ByteMatrix(null);
        Assertions.assertEquals(0, nullMatrix.rows);
        Assertions.assertEquals(0, nullMatrix.cols);
    }

    @Test
    public void testEmpty() {
        ByteMatrix empty = ByteMatrix.empty();
        Assertions.assertEquals(0, empty.rows);
        Assertions.assertEquals(0, empty.cols);
        Assertions.assertTrue(empty.isEmpty());
    }

    @Test
    public void testOf() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);
        Assertions.assertEquals(2, matrix.rows);
        Assertions.assertEquals(2, matrix.cols);

        ByteMatrix emptyMatrix = ByteMatrix.of();
        Assertions.assertTrue(emptyMatrix.isEmpty());

        ByteMatrix nullMatrix = ByteMatrix.of((byte[][]) null);
        Assertions.assertTrue(nullMatrix.isEmpty());
    }

    @Test
    public void testRandom() {
        ByteMatrix matrix = ByteMatrix.random(5);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(5, matrix.cols);
    }

    @Test
    public void testRepeat() {
        ByteMatrix matrix = ByteMatrix.repeat((byte) 7, 4);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(4, matrix.cols);
        for (int i = 0; i < 4; i++) {
            Assertions.assertEquals(7, matrix.get(0, i));
        }
    }

    @Test
    public void testRange() {
        ByteMatrix matrix = ByteMatrix.range((byte) 1, (byte) 5);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(4, matrix.cols);
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(0, 2));
        Assertions.assertEquals(4, matrix.get(0, 3));
    }

    @Test
    public void testRangeWithStep() {
        ByteMatrix matrix = ByteMatrix.range((byte) 0, (byte) 10, (byte) 2);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(5, matrix.cols);
        Assertions.assertEquals(0, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(4, matrix.get(0, 2));
        Assertions.assertEquals(6, matrix.get(0, 3));
        Assertions.assertEquals(8, matrix.get(0, 4));
    }

    @Test
    public void testRangeClosed() {
        ByteMatrix matrix = ByteMatrix.rangeClosed((byte) 1, (byte) 4);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(4, matrix.cols);
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(4, matrix.get(0, 3));
    }

    @Test
    public void testRangeClosedWithStep() {
        ByteMatrix matrix = ByteMatrix.rangeClosed((byte) 0, (byte) 9, (byte) 3);
        Assertions.assertEquals(1, matrix.rows);
        Assertions.assertEquals(4, matrix.cols);
        Assertions.assertEquals(0, matrix.get(0, 0));
        Assertions.assertEquals(3, matrix.get(0, 1));
        Assertions.assertEquals(4, matrix.cols);
        Assertions.assertEquals(0, matrix.get(0, 0));
        Assertions.assertEquals(3, matrix.get(0, 1));
        Assertions.assertEquals(6, matrix.get(0, 2));
        Assertions.assertEquals(9, matrix.get(0, 3));
    }

    @Test
    public void testDiagonalLU2RD() {
        byte[] diagonal = { 1, 2, 3 };
        ByteMatrix matrix = ByteMatrix.diagonalLU2RD(diagonal);
        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3, matrix.get(2, 2));
        Assertions.assertEquals(0, matrix.get(0, 1));
    }

    @Test
    public void testDiagonalRU2LD() {
        byte[] diagonal = { 1, 2, 3 };
        ByteMatrix matrix = ByteMatrix.diagonalRU2LD(diagonal);
        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals(1, matrix.get(0, 2));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3, matrix.get(2, 0));
        Assertions.assertEquals(0, matrix.get(0, 0));
    }

    @Test
    public void testDiagonal() {
        byte[] main = { 1, 2, 3 };
        byte[] anti = { 4, 5, 6 };
        ByteMatrix matrix = ByteMatrix.diagonal(main, anti);
        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3, matrix.get(2, 2));
        Assertions.assertEquals(4, matrix.get(0, 2));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(6, matrix.get(2, 0));

        ByteMatrix emptyMatrix = ByteMatrix.diagonal(null, null);
        Assertions.assertTrue(emptyMatrix.isEmpty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> ByteMatrix.diagonal(new byte[] { 1 }, new byte[] { 2, 3 }));
    }

    @Test
    public void testUnbox() {
        Byte[][] boxed = { { 1, 2 }, { 3, 4 } };
        Matrix<Byte> boxedMatrix = Matrix.of(boxed);
        ByteMatrix unboxed = ByteMatrix.unbox(boxedMatrix);
        Assertions.assertEquals(2, unboxed.rows);
        Assertions.assertEquals(2, unboxed.cols);
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

        OptionalByte up = matrix.upOf(1, 0);
        Assertions.assertTrue(up.isPresent());
        Assertions.assertEquals(1, up.get());

        OptionalByte empty = matrix.upOf(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        OptionalByte down = matrix.downOf(0, 0);
        Assertions.assertTrue(down.isPresent());
        Assertions.assertEquals(3, down.get());

        OptionalByte empty = matrix.downOf(1, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        OptionalByte left = matrix.leftOf(0, 1);
        Assertions.assertTrue(left.isPresent());
        Assertions.assertEquals(1, left.get());

        OptionalByte empty = matrix.leftOf(0, 0);
        Assertions.assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        OptionalByte right = matrix.rightOf(0, 0);
        Assertions.assertTrue(right.isPresent());
        Assertions.assertEquals(2, right.get());

        OptionalByte empty = matrix.rightOf(0, 1);
        Assertions.assertFalse(empty.isPresent());
    }


    @Test
    public void testRow() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] row0 = matrix.row(0);
        Assertions.assertArrayEquals(new byte[] { 1, 2, 3 }, row0);

        byte[] row1 = matrix.row(1);
        Assertions.assertArrayEquals(new byte[] { 4, 5, 6 }, row1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.row(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.row(2));
    }

    @Test
    public void testColumn() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] col0 = matrix.column(0);
        Assertions.assertArrayEquals(new byte[] { 1, 4 }, col0);

        byte[] col1 = matrix.column(1);
        Assertions.assertArrayEquals(new byte[] { 2, 5 }, col1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.column(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.column(3));
    }

    @Test
    public void testSetRow() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.setRow(0, new byte[] { 7, 8, 9 });
        Assertions.assertArrayEquals(new byte[] { 7, 8, 9 }, matrix.row(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setRow(0, new byte[] { 1, 2 }));
    }

    @Test
    public void testSetColumn() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.setColumn(0, new byte[] { 7, 8 });
        Assertions.assertArrayEquals(new byte[] { 7, 8 }, matrix.column(0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setColumn(0, new byte[] { 1 }));
    }

    @Test
    public void testUpdateRow() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.updateRow(0, b -> (byte) (b * 2));
        Assertions.assertArrayEquals(new byte[] { 2, 4, 6 }, matrix.row(0));
    }

    @Test
    public void testUpdateColumn() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.updateColumn(1, b -> (byte) (b + 10));
        Assertions.assertArrayEquals(new byte[] { 12, 15 }, matrix.column(1));
    }

    @Test
    public void testGetLU2RD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] diagonal = matrix.getLU2RD();
        Assertions.assertArrayEquals(new byte[] { 1, 5, 9 }, diagonal);

        ByteMatrix nonSquare = ByteMatrix.of(new byte[][] { { 1, 2 } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.setLU2RD(new byte[] { 10, 11, 12 });
        Assertions.assertEquals(10, matrix.get(0, 0));
        Assertions.assertEquals(11, matrix.get(1, 1));
        Assertions.assertEquals(12, matrix.get(2, 2));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setLU2RD(new byte[] { 1, 2 }));
    }

    @Test
    public void testUpdateLU2RD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.updateLU2RD(b -> (byte) (b * 2));
        Assertions.assertEquals(2, matrix.get(0, 0));
        Assertions.assertEquals(10, matrix.get(1, 1));
        Assertions.assertEquals(18, matrix.get(2, 2));
    }

    @Test
    public void testGetRU2LD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] diagonal = matrix.getRU2LD();
        Assertions.assertArrayEquals(new byte[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testSetRU2LD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.setRU2LD(new byte[] { 10, 11, 12 });
        Assertions.assertEquals(10, matrix.get(0, 2));
        Assertions.assertEquals(11, matrix.get(1, 1));
        Assertions.assertEquals(12, matrix.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.updateRU2LD(b -> (byte) (b + 1));
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
        matrix.fill(b);
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
        matrix.fill(1, 1, b);
        Assertions.assertEquals(0, matrix.get(0, 0)); // unchanged
        Assertions.assertEquals(1, matrix.get(1, 1));
        Assertions.assertEquals(2, matrix.get(1, 2));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.fill(-1, 0, b));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.fill(0, -1, b));
    }

    @Test
    public void testCopy() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);
        ByteMatrix copy = matrix.copy();

        Assertions.assertEquals(matrix.rows, copy.rows);
        Assertions.assertEquals(matrix.cols, copy.cols);
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

        Assertions.assertEquals(2, copy.rows);
        Assertions.assertEquals(2, copy.cols);
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

        Assertions.assertEquals(2, copy.rows);
        Assertions.assertEquals(2, copy.cols);
        Assertions.assertEquals(2, copy.get(0, 0));
        Assertions.assertEquals(3, copy.get(0, 1));
        Assertions.assertEquals(5, copy.get(1, 0));
        Assertions.assertEquals(6, copy.get(1, 1));
    }

    @Test
    public void testExtend() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix extended = matrix.extend(3, 3);
        Assertions.assertEquals(3, extended.rows);
        Assertions.assertEquals(3, extended.cols);
        Assertions.assertEquals(1, extended.get(0, 0));
        Assertions.assertEquals(4, extended.get(1, 1));
        Assertions.assertEquals(0, extended.get(2, 2));

        ByteMatrix truncated = matrix.extend(1, 1);
        Assertions.assertEquals(1, truncated.rows);
        Assertions.assertEquals(1, truncated.cols);
        Assertions.assertEquals(1, truncated.get(0, 0));
    }

    @Test
    public void testExtendWithDefaultValue() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix extended = matrix.extend(3, 3, (byte) 9);
        Assertions.assertEquals(9, extended.get(2, 2));
        Assertions.assertEquals(9, extended.get(0, 2));
        Assertions.assertEquals(9, extended.get(2, 0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 2, (byte) 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.extend(2, -1, (byte) 0));
    }

    @Test
    public void testExtendInAllDirections() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix extended = matrix.extend(1, 1, 1, 1);
        Assertions.assertEquals(4, extended.rows);
        Assertions.assertEquals(4, extended.cols);
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

        matrix.reverseH();
        Assertions.assertEquals(3, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(1, matrix.get(0, 2));
        Assertions.assertEquals(6, matrix.get(1, 0));
    }

    @Test
    public void testReverseV() {
        byte[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        matrix.reverseV();
        Assertions.assertEquals(5, matrix.get(0, 0));
        Assertions.assertEquals(6, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(1, 0));
        Assertions.assertEquals(1, matrix.get(2, 0));
    }

    @Test
    public void testFlipH() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix flipped = matrix.flipH();
        Assertions.assertEquals(3, flipped.get(0, 0));
        Assertions.assertEquals(1, flipped.get(0, 2));
        Assertions.assertEquals(1, matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testFlipV() {
        byte[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix flipped = matrix.flipV();
        Assertions.assertEquals(5, flipped.get(0, 0));
        Assertions.assertEquals(1, flipped.get(2, 0));
        Assertions.assertEquals(1, matrix.get(0, 0)); // original unchanged
    }

    @Test
    public void testRotate90() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix rotated = matrix.rotate90();
        Assertions.assertEquals(2, rotated.rows);
        Assertions.assertEquals(2, rotated.cols);
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
        Assertions.assertEquals(2, rotated.rows);
        Assertions.assertEquals(2, rotated.cols);
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
        Assertions.assertEquals(2, rotated.rows);
        Assertions.assertEquals(2, rotated.cols);
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
        Assertions.assertEquals(3, transposed.rows);
        Assertions.assertEquals(2, transposed.cols);
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
        Assertions.assertEquals(3, reshaped.rows);
        Assertions.assertEquals(2, reshaped.cols);
        Assertions.assertEquals(1, reshaped.get(0, 0));
        Assertions.assertEquals(2, reshaped.get(0, 1));
        Assertions.assertEquals(3, reshaped.get(1, 0));
        Assertions.assertEquals(4, reshaped.get(1, 1));

        ByteMatrix empty = ByteMatrix.empty().reshape(2, 3);
        Assertions.assertEquals(2, empty.rows);
        Assertions.assertEquals(3, empty.cols);
    }

    @Test
    public void testRepelem() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix repeated = matrix.repelem(2, 3);
        Assertions.assertEquals(4, repeated.rows);
        Assertions.assertEquals(6, repeated.cols);
        Assertions.assertEquals(1, repeated.get(0, 0));
        Assertions.assertEquals(1, repeated.get(0, 2));
        Assertions.assertEquals(1, repeated.get(1, 0));
        Assertions.assertEquals(2, repeated.get(0, 3));
        Assertions.assertEquals(4, repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repelem(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        ByteMatrix repeated = matrix.repmat(2, 3);
        Assertions.assertEquals(4, repeated.rows);
        Assertions.assertEquals(6, repeated.cols);
        Assertions.assertEquals(1, repeated.get(0, 0));
        Assertions.assertEquals(1, repeated.get(0, 2));
        Assertions.assertEquals(1, repeated.get(0, 4));
        Assertions.assertEquals(1, repeated.get(2, 0));
        Assertions.assertEquals(4, repeated.get(3, 5));

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repmat(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.repmat(1, 0));
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
        matrix.flatOp(row -> {
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

        ByteMatrix stacked = matrixA.vstack(matrixB);
        Assertions.assertEquals(4, stacked.rows);
        Assertions.assertEquals(2, stacked.cols);
        Assertions.assertEquals(1, stacked.get(0, 0));
        Assertions.assertEquals(5, stacked.get(2, 0));

        ByteMatrix differentCols = ByteMatrix.of(new byte[][] { { 1, 2, 3 } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.vstack(differentCols));
    }

    @Test
    public void testHstack() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        byte[][] b = { { 5, 6 }, { 7, 8 } };
        ByteMatrix matrixA = ByteMatrix.of(a);
        ByteMatrix matrixB = ByteMatrix.of(b);

        ByteMatrix stacked = matrixA.hstack(matrixB);
        Assertions.assertEquals(2, stacked.rows);
        Assertions.assertEquals(4, stacked.cols);
        Assertions.assertEquals(1, stacked.get(0, 0));
        Assertions.assertEquals(5, stacked.get(0, 2));

        ByteMatrix differentRows = ByteMatrix.of(new byte[][] { { 1 }, { 2 }, { 3 } });
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrixA.hstack(differentRows));
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
        Assertions.assertEquals(2, product.rows);
        Assertions.assertEquals(2, product.cols);
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
        Assertions.assertEquals(2, boxed.rows);
        Assertions.assertEquals(2, boxed.cols);
        Assertions.assertEquals(Byte.valueOf((byte) 1), boxed.get(0, 0));
        Assertions.assertEquals(Byte.valueOf((byte) 4), boxed.get(1, 1));
    }

    @Test
    public void testToIntMatrix() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        IntMatrix intMatrix = matrix.toIntMatrix();
        Assertions.assertEquals(2, intMatrix.rows);
        Assertions.assertEquals(2, intMatrix.cols);
        Assertions.assertEquals(1, intMatrix.get(0, 0));
        Assertions.assertEquals(4, intMatrix.get(1, 1));
    }

    @Test
    public void testToLongMatrix() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        LongMatrix longMatrix = matrix.toLongMatrix();
        Assertions.assertEquals(2, longMatrix.rows);
        Assertions.assertEquals(2, longMatrix.cols);
        Assertions.assertEquals(1L, longMatrix.get(0, 0));
        Assertions.assertEquals(4L, longMatrix.get(1, 1));
    }

    @Test
    public void testToFloatMatrix() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        FloatMatrix floatMatrix = matrix.toFloatMatrix();
        Assertions.assertEquals(2, floatMatrix.rows);
        Assertions.assertEquals(2, floatMatrix.cols);
        Assertions.assertEquals(1.0f, floatMatrix.get(0, 0));
        Assertions.assertEquals(4.0f, floatMatrix.get(1, 1));
    }

    @Test
    public void testToDoubleMatrix() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        DoubleMatrix doubleMatrix = matrix.toDoubleMatrix();
        Assertions.assertEquals(2, doubleMatrix.rows);
        Assertions.assertEquals(2, doubleMatrix.cols);
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

        byte[] diagonal = matrix.streamLU2RD().toArray();
        Assertions.assertArrayEquals(new byte[] { 1, 5, 9 }, diagonal);

        ByteMatrix empty = ByteMatrix.empty();
        Assertions.assertTrue(empty.streamLU2RD().toList().isEmpty());

        ByteMatrix nonSquare = ByteMatrix.of(new byte[][] { { 1, 2 } });
        Assertions.assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] diagonal = matrix.streamRU2LD().toArray();
        Assertions.assertArrayEquals(new byte[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testStreamH() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] all = matrix.streamH().toArray();
        Assertions.assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6 }, all);

        ByteMatrix empty = ByteMatrix.empty();
        Assertions.assertTrue(empty.streamH().toList().isEmpty());
    }

    @Test
    public void testStreamHRow() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] row1 = matrix.streamH(1).toArray();
        Assertions.assertArrayEquals(new byte[] { 4, 5, 6 }, row1);
    }

    @Test
    public void testStreamHRange() {
        byte[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] range = matrix.streamH(1, 3).toArray();
        Assertions.assertArrayEquals(new byte[] { 3, 4, 5, 6 }, range);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(-1, 2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(0, 4));
    }

    @Test
    public void testStreamV() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] all = matrix.streamV().toArray();
        Assertions.assertArrayEquals(new byte[] { 1, 4, 2, 5, 3, 6 }, all);
    }

    @Test
    public void testStreamVColumn() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] col1 = matrix.streamV(1).toArray();
        Assertions.assertArrayEquals(new byte[] { 2, 5 }, col1);
    }

    @Test
    public void testStreamVRange() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        byte[] range = matrix.streamV(1, 3).toArray();
        Assertions.assertArrayEquals(new byte[] { 2, 5, 3, 6 }, range);
    }

    @Test
    public void testStreamR() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        List<ByteStream> rows = matrix.streamR().toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new byte[] { 1, 2 }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new byte[] { 3, 4 }, rows.get(1).toArray());
    }

    @Test
    public void testStreamRRange() {
        byte[][] a = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        List<ByteStream> rows = matrix.streamR(1, 3).toList();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertArrayEquals(new byte[] { 3, 4 }, rows.get(0).toArray());
        Assertions.assertArrayEquals(new byte[] { 5, 6 }, rows.get(1).toArray());
    }

    @Test
    public void testStreamC() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        List<ByteStream> cols = matrix.streamC().toList();
        Assertions.assertEquals(3, cols.size());
        Assertions.assertArrayEquals(new byte[] { 1, 4 }, cols.get(0).toArray());
        Assertions.assertArrayEquals(new byte[] { 2, 5 }, cols.get(1).toArray());
        Assertions.assertArrayEquals(new byte[] { 3, 6 }, cols.get(2).toArray());
    }

    @Test
    public void testStreamCRange() {
        byte[][] a = { { 1, 2, 3 }, { 4, 5, 6 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        List<ByteStream> cols = matrix.streamC(1, 3).toList();
        Assertions.assertEquals(2, cols.size());
        Assertions.assertArrayEquals(new byte[] { 2, 5 }, cols.get(0).toArray());
        Assertions.assertArrayEquals(new byte[] { 3, 6 }, cols.get(1).toArray());
    }

    @Test
    public void testLength() {
        ByteMatrix matrix = ByteMatrix.of(new byte[][] { { 1, 2 }, { 3, 4 } });
        // length is protected, cannot test directly from here
        // It's tested indirectly through other operations
        Assertions.assertEquals(2, matrix.cols);
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
    public void testPrintln() {
        byte[][] a = { { 1, 2 }, { 3, 4 } };
        ByteMatrix matrix = ByteMatrix.of(a);

        // Just verify it doesn't throw
        matrix.println();
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
}