package com.landawn.abacus.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.stream.ObjIteratorEx;

public class MatrixTest extends TestBase {

    @Test
    public void testConstructor() {
        Integer[][] data = { { 1, 2, 3 }, { 4, 5, 6 } };
        Matrix<Integer> matrix = new Matrix<>(data);

        Assertions.assertEquals(2, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals(6, matrix.count);
    }

    @Test
    public void testConstructorWithEmptyArray() {
        Integer[][] data = new Integer[0][0];
        Matrix<Integer> matrix = new Matrix<>(data);

        Assertions.assertEquals(0, matrix.rows);
        Assertions.assertEquals(0, matrix.cols);
        Assertions.assertEquals(0, matrix.count);
    }

    @Test
    public void testConstructorWithNonRectangularArray() {
        Integer[][] data = { { 1, 2 }, { 3, 4, 5 } };

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Matrix<>(data);
        });
    }

    @Test
    public void testOf() {
        Integer[][] data = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Assertions.assertEquals(2, matrix.rows);
        Assertions.assertEquals(2, matrix.cols);
        Assertions.assertEquals(1, matrix.get(0, 0));
    }

    @Test
    public void testOfVarargs() {
        Matrix<String> matrix = Matrix.of(new String[] { "a", "b" }, new String[] { "c", "d" });

        Assertions.assertEquals(2, matrix.rows);
        Assertions.assertEquals(2, matrix.cols);
        Assertions.assertEquals("a", matrix.get(0, 0));
    }


    @Test
    public void testDiagonalLU2RD() {
        Integer[] diagonal = { 1, 2, 3 };
        Matrix<Integer> matrix = Matrix.diagonalLU2RD(diagonal);

        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3, matrix.get(2, 2));
        Assertions.assertNull(matrix.get(0, 1));
        Assertions.assertNull(matrix.get(1, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        Integer[] diagonal = { 1, 2, 3 };
        Matrix<Integer> matrix = Matrix.diagonalRU2LD(diagonal);

        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals(1, matrix.get(0, 2));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3, matrix.get(2, 0));
        Assertions.assertNull(matrix.get(0, 0));
        Assertions.assertNull(matrix.get(2, 2));
    }

    @Test
    public void testDiagonalBoth() {
        Integer[] mainDiag = { 1, 2, 3 };
        Integer[] antiDiag = { 7, 8, 9 };
        Matrix<Integer> matrix = Matrix.diagonal(mainDiag, antiDiag);

        Assertions.assertEquals(3, matrix.rows);
        Assertions.assertEquals(3, matrix.cols);
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3, matrix.get(2, 2));
        Assertions.assertEquals(7, matrix.get(0, 2));
        Assertions.assertEquals(9, matrix.get(2, 0));
    }

    @Test
    public void testDiagonalWithDifferentLengths() {
        Integer[] mainDiag = { 1, 2, 3 };
        Integer[] antiDiag = { 7, 8 };

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Matrix.diagonal(mainDiag, antiDiag);
        });
    }

    @Test
    public void testComponentType() {
        Matrix<String> matrix = Matrix.of(new String[][] { { "a", "b" } });
        Assertions.assertEquals(String.class, matrix.componentType());

        Matrix<Integer> intMatrix = Matrix.of(new Integer[][] { { 1, 2 } });
        Assertions.assertEquals(Integer.class, intMatrix.componentType());
    }

    @Test
    public void testGet() {
        Integer[][] data = { { 1, 2, 3 }, { 4, 5, 6 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(5, matrix.get(1, 1));
        Assertions.assertEquals(6, matrix.get(1, 2));
    }

    @Test
    public void testGetOutOfBounds() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 } });

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            matrix.get(2, 0);
        });
    }

    @Test
    public void testGetWithPoint() {
        Integer[][] data = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Sheet.Point point = Sheet.Point.of(1, 1);
        Assertions.assertEquals(4, matrix.get(point));
    }

    @Test
    public void testSet() {
        Integer[][] data = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> matrix = Matrix.of(data);

        matrix.set(0, 1, 10);
        Assertions.assertEquals(10, matrix.get(0, 1));
    }

    @Test
    public void testSetWithPoint() {
        Integer[][] data = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Sheet.Point point = Sheet.Point.of(1, 0);
        matrix.set(point, 20);
        Assertions.assertEquals(20, matrix.get(1, 0));
    }

    @Test
    public void testUpOf() {
        Integer[][] data = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Assertions.assertEquals(1, matrix.upOf(1, 0).orElse(null));
        Assertions.assertFalse(matrix.upOf(0, 0).isPresent());
    }

    @Test
    public void testDownOf() {
        Integer[][] data = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Assertions.assertEquals(3, matrix.downOf(0, 0).orElse(null));
        Assertions.assertFalse(matrix.downOf(1, 0).isPresent());
    }

    @Test
    public void testLeftOf() {
        Integer[][] data = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Assertions.assertEquals(1, matrix.leftOf(0, 1).orElse(null));
        Assertions.assertFalse(matrix.leftOf(0, 0).isPresent());
    }

    @Test
    public void testRightOf() {
        Integer[][] data = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Assertions.assertEquals(2, matrix.rightOf(0, 0).orElse(null));
        Assertions.assertFalse(matrix.rightOf(0, 1).isPresent());
    }

    @Test
    public void testRow() {
        Integer[][] data = { { 1, 2, 3 }, { 4, 5, 6 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Integer[] row = matrix.row(0);
        Assertions.assertArrayEquals(new Integer[] { 1, 2, 3 }, row);

        row[0] = 10; // This modifies the matrix
        Assertions.assertEquals(10, matrix.get(0, 0));
    }

    @Test
    public void testRowInvalidIndex() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.row(-1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.row(2);
        });
    }

    @Test
    public void testColumn() {
        Integer[][] data = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Integer[] column = matrix.column(1);
        Assertions.assertArrayEquals(new Integer[] { 2, 4, 6 }, column);

        column[0] = 10; // This does not modify the matrix
        Assertions.assertEquals(2, matrix.get(0, 1));
    }

    @Test
    public void testColumnInvalidIndex() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.column(-1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.column(2);
        });
    }

    @Test
    public void testSetRow() {
        Integer[][] data = { { 1, 2, 3 }, { 4, 5, 6 } };
        Matrix<Integer> matrix = Matrix.of(data);

        matrix.setRow(0, new Integer[] { 7, 8, 9 });
        Assertions.assertArrayEquals(new Integer[] { 7, 8, 9 }, matrix.row(0));
    }

    @Test
    public void testSetRowWrongSize() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.setRow(0, new Integer[] { 7, 8 });
        });
    }

    @Test
    public void testSetColumn() {
        Integer[][] data = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        Matrix<Integer> matrix = Matrix.of(data);

        matrix.setColumn(1, new Integer[] { 7, 8, 9 });
        Assertions.assertArrayEquals(new Integer[] { 7, 8, 9 }, matrix.column(1));
    }

    @Test
    public void testSetColumnWrongSize() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.setColumn(0, new Integer[] { 7, 8 });
        });
    }

    @Test
    public void testUpdateRow() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        matrix.updateRow(0, x -> x * 2);
        Assertions.assertArrayEquals(new Integer[] { 2, 4, 6 }, matrix.row(0));
        Assertions.assertArrayEquals(new Integer[] { 4, 5, 6 }, matrix.row(1));
    }

    @Test
    public void testUpdateColumn() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        matrix.updateColumn(1, x -> x + 10);
        Assertions.assertArrayEquals(new Integer[] { 12, 14, 16 }, matrix.column(1));
        Assertions.assertArrayEquals(new Integer[] { 1, 3, 5 }, matrix.column(0));
    }

    @Test
    public void testGetLU2RD() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        Integer[] diagonal = matrix.getLU2RD();
        Assertions.assertArrayEquals(new Integer[] { 1, 5, 9 }, diagonal);
    }

    @Test
    public void testGetLU2RDNonSquare() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Assertions.assertThrows(IllegalStateException.class, () -> {
            matrix.getLU2RD();
        });
    }

    @Test
    public void testSetLU2RD() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        matrix.setLU2RD(new Integer[] { 10, 20, 30 });
        Assertions.assertEquals(10, matrix.get(0, 0));
        Assertions.assertEquals(20, matrix.get(1, 1));
        Assertions.assertEquals(30, matrix.get(2, 2));
    }

    @Test
    public void testSetLU2RDShortArray() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.setLU2RD(new Integer[] { 10 });
        });
    }

    @Test
    public void testUpdateLU2RD() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        matrix.updateLU2RD(x -> x * 10);
        Assertions.assertEquals(10, matrix.get(0, 0));
        Assertions.assertEquals(50, matrix.get(1, 1));
        Assertions.assertEquals(90, matrix.get(2, 2));
        Assertions.assertEquals(2, matrix.get(0, 1));   // unchanged
    }

    @Test
    public void testGetRU2LD() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        Integer[] diagonal = matrix.getRU2LD();
        Assertions.assertArrayEquals(new Integer[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testSetRU2LD() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        matrix.setRU2LD(new Integer[] { 10, 20, 30 });
        Assertions.assertEquals(10, matrix.get(0, 2));
        Assertions.assertEquals(20, matrix.get(1, 1));
        Assertions.assertEquals(30, matrix.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        matrix.updateRU2LD(x -> x * -1);
        Assertions.assertEquals(-3, matrix.get(0, 2));
        Assertions.assertEquals(-5, matrix.get(1, 1));
        Assertions.assertEquals(-7, matrix.get(2, 0));
        Assertions.assertEquals(1, matrix.get(0, 0));   // unchanged
    }

    @Test
    public void testUpdateAll() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        matrix.updateAll(x -> x * x);
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(4, matrix.get(0, 1));
        Assertions.assertEquals(9, matrix.get(1, 0));
        Assertions.assertEquals(16, matrix.get(1, 1));
    }

    @Test
    public void testUpdateAllWithIndices() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        matrix.updateAll((i, j) -> i + j);
        Assertions.assertEquals(0, matrix.get(0, 0));
        Assertions.assertEquals(1, matrix.get(0, 1));
        Assertions.assertEquals(1, matrix.get(1, 0));
        Assertions.assertEquals(2, matrix.get(1, 1));
    }

    @Test
    public void testReplaceIf() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        matrix.replaceIf(x -> x % 2 == 0, 0);
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(0, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(0, 2));
        Assertions.assertEquals(0, matrix.get(1, 0));
    }

    @Test
    public void testReplaceIfWithIndices() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        matrix.replaceIf((i, j) -> i == j, 0);
        Assertions.assertEquals(0, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(1, 0));
        Assertions.assertEquals(0, matrix.get(1, 1));
    }

    @Test
    public void testMap() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<Integer> result = matrix.map(x -> x * 2);
        Assertions.assertEquals(2, result.get(0, 0));
        Assertions.assertEquals(4, result.get(0, 1));
        Assertions.assertEquals(6, result.get(1, 0));
        Assertions.assertEquals(8, result.get(1, 1));
    }

    @Test
    public void testMapToDifferentType() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<String> result = matrix.map(Object::toString, String.class);
        Assertions.assertEquals("1", result.get(0, 0));
        Assertions.assertEquals("2", result.get(0, 1));
        Assertions.assertEquals("3", result.get(1, 0));
        Assertions.assertEquals("4", result.get(1, 1));
    }

    @Test
    public void testMapToBoolean() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        BooleanMatrix result = matrix.mapToBoolean(x -> x % 2 == 0);
        Assertions.assertFalse(result.get(0, 0));
        Assertions.assertTrue(result.get(0, 1));
        Assertions.assertFalse(result.get(1, 0));
        Assertions.assertTrue(result.get(1, 1));
    }

    @Test
    public void testMapToByte() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        ByteMatrix result = matrix.mapToByte(Integer::byteValue);
        Assertions.assertEquals((byte) 1, result.get(0, 0));
        Assertions.assertEquals((byte) 2, result.get(0, 1));
        Assertions.assertEquals((byte) 3, result.get(1, 0));
        Assertions.assertEquals((byte) 4, result.get(1, 1));
    }

    @Test
    public void testMapToChar() throws Exception {
        Matrix<String> matrix = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });

        CharMatrix result = matrix.mapToChar(s -> s.charAt(0));
        Assertions.assertEquals('a', result.get(0, 0));
        Assertions.assertEquals('b', result.get(0, 1));
        Assertions.assertEquals('c', result.get(1, 0));
        Assertions.assertEquals('d', result.get(1, 1));
    }

    @Test
    public void testMapToShort() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        ShortMatrix result = matrix.mapToShort(Integer::shortValue);
        Assertions.assertEquals((short) 1, result.get(0, 0));
        Assertions.assertEquals((short) 2, result.get(0, 1));
        Assertions.assertEquals((short) 3, result.get(1, 0));
        Assertions.assertEquals((short) 4, result.get(1, 1));
    }

    @Test
    public void testMapToInt() throws Exception {
        Matrix<String> matrix = Matrix.of(new String[][] { { "1", "12" }, { "123", "1234" } });

        IntMatrix result = matrix.mapToInt(String::length);
        Assertions.assertEquals(1, result.get(0, 0));
        Assertions.assertEquals(2, result.get(0, 1));
        Assertions.assertEquals(3, result.get(1, 0));
        Assertions.assertEquals(4, result.get(1, 1));
    }

    @Test
    public void testMapToLong() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        LongMatrix result = matrix.mapToLong(Integer::longValue);
        Assertions.assertEquals(1L, result.get(0, 0));
        Assertions.assertEquals(2L, result.get(0, 1));
        Assertions.assertEquals(3L, result.get(1, 0));
        Assertions.assertEquals(4L, result.get(1, 1));
    }

    @Test
    public void testMapToFloat() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        FloatMatrix result = matrix.mapToFloat(Integer::floatValue);
        Assertions.assertEquals(1.0f, result.get(0, 0));
        Assertions.assertEquals(2.0f, result.get(0, 1));
        Assertions.assertEquals(3.0f, result.get(1, 0));
        Assertions.assertEquals(4.0f, result.get(1, 1));
    }

    @Test
    public void testMapToDouble() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        DoubleMatrix result = matrix.mapToDouble(Integer::doubleValue);
        Assertions.assertEquals(1.0, result.get(0, 0));
        Assertions.assertEquals(2.0, result.get(0, 1));
        Assertions.assertEquals(3.0, result.get(1, 0));
        Assertions.assertEquals(4.0, result.get(1, 1));
    }

    @Test
    public void testFill() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        matrix.fill(0);
        Assertions.assertEquals(0, matrix.get(0, 0));
        Assertions.assertEquals(0, matrix.get(0, 1));
        Assertions.assertEquals(0, matrix.get(1, 0));
        Assertions.assertEquals(0, matrix.get(1, 1));
    }

    @Test
    public void testFillWithArray() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Integer[][] patch = { { 7, 8 } };
        matrix.fill(patch);
        Assertions.assertEquals(7, matrix.get(0, 0));
        Assertions.assertEquals(8, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(0, 2));   // unchanged
        Assertions.assertEquals(4, matrix.get(1, 0));   // unchanged
    }

    @Test
    public void testFillWithPosition() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        Integer[][] patch = { { 10, 11 }, { 12, 13 } };
        matrix.fill(1, 1, patch);
        Assertions.assertEquals(1, matrix.get(0, 0));   // unchanged
        Assertions.assertEquals(10, matrix.get(1, 1));
        Assertions.assertEquals(11, matrix.get(1, 2));
        Assertions.assertEquals(12, matrix.get(2, 1));
        Assertions.assertEquals(13, matrix.get(2, 2));
    }

    @Test
    public void testCopy() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<Integer> copy = matrix.copy();
        Assertions.assertEquals(matrix.get(0, 0), copy.get(0, 0));
        Assertions.assertEquals(matrix.get(1, 1), copy.get(1, 1));

        copy.set(0, 0, 10);
        Assertions.assertEquals(1, matrix.get(0, 0));   // original unchanged
        Assertions.assertEquals(10, copy.get(0, 0));
    }

    @Test
    public void testCopyRows() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        Matrix<Integer> copy = matrix.copy(0, 2);
        Assertions.assertEquals(2, copy.rows);
        Assertions.assertEquals(2, copy.cols);
        Assertions.assertEquals(1, copy.get(0, 0));
        Assertions.assertEquals(4, copy.get(1, 1));
    }

    @Test
    public void testCopyRegion() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        Matrix<Integer> copy = matrix.copy(1, 3, 1, 3);
        Assertions.assertEquals(2, copy.rows);
        Assertions.assertEquals(2, copy.cols);
        Assertions.assertEquals(5, copy.get(0, 0));
        Assertions.assertEquals(6, copy.get(0, 1));
        Assertions.assertEquals(8, copy.get(1, 0));
        Assertions.assertEquals(9, copy.get(1, 1));
    }

    @Test
    public void testExtend() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<Integer> extended = matrix.extend(3, 3);
        Assertions.assertEquals(3, extended.rows);
        Assertions.assertEquals(3, extended.cols);
        Assertions.assertEquals(1, extended.get(0, 0));
        Assertions.assertEquals(4, extended.get(1, 1));
        Assertions.assertNull(extended.get(2, 2));
    }

    @Test
    public void testExtendWithDefault() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<Integer> extended = matrix.extend(3, 3, 0);
        Assertions.assertEquals(3, extended.rows);
        Assertions.assertEquals(3, extended.cols);
        Assertions.assertEquals(1, extended.get(0, 0));
        Assertions.assertEquals(4, extended.get(1, 1));
        Assertions.assertEquals(0, extended.get(2, 2));
        Assertions.assertEquals(0, extended.get(0, 2));
        Assertions.assertEquals(0, extended.get(2, 0));
    }

    @Test
    public void testExtendDirectional() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<Integer> extended = matrix.extend(1, 1, 1, 1, 0);
        Assertions.assertEquals(4, extended.rows);
        Assertions.assertEquals(4, extended.cols);
        Assertions.assertEquals(0, extended.get(0, 0));
        Assertions.assertEquals(1, extended.get(1, 1));
        Assertions.assertEquals(4, extended.get(2, 2));
        Assertions.assertEquals(0, extended.get(3, 3));
    }

    @Test
    public void testReverseH() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        matrix.reverseH();
        Assertions.assertEquals(3, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(1, matrix.get(0, 2));
        Assertions.assertEquals(6, matrix.get(1, 0));
    }

    @Test
    public void testReverseV() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        matrix.reverseV();
        Assertions.assertEquals(5, matrix.get(0, 0));
        Assertions.assertEquals(6, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(1, 0));
        Assertions.assertEquals(1, matrix.get(2, 0));
    }

    @Test
    public void testFlipH() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Matrix<Integer> flipped = matrix.flipH();
        Assertions.assertEquals(3, flipped.get(0, 0));
        Assertions.assertEquals(2, flipped.get(0, 1));
        Assertions.assertEquals(1, flipped.get(0, 2));
        Assertions.assertEquals(6, flipped.get(1, 0));

        // Original unchanged
        Assertions.assertEquals(1, matrix.get(0, 0));
    }

    @Test
    public void testFlipV() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        Matrix<Integer> flipped = matrix.flipV();
        Assertions.assertEquals(5, flipped.get(0, 0));
        Assertions.assertEquals(6, flipped.get(0, 1));
        Assertions.assertEquals(3, flipped.get(1, 0));
        Assertions.assertEquals(1, flipped.get(2, 0));

        // Original unchanged
        Assertions.assertEquals(1, matrix.get(0, 0));
    }

    @Test
    public void testRotate90() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        Matrix<Integer> rotated = matrix.rotate90();
        Assertions.assertEquals(2, rotated.rows);
        Assertions.assertEquals(3, rotated.cols);
        Assertions.assertEquals(5, rotated.get(0, 0));
        Assertions.assertEquals(3, rotated.get(0, 1));
        Assertions.assertEquals(1, rotated.get(0, 2));
        Assertions.assertEquals(6, rotated.get(1, 0));
    }

    @Test
    public void testRotate180() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Matrix<Integer> rotated = matrix.rotate180();
        Assertions.assertEquals(6, rotated.get(0, 0));
        Assertions.assertEquals(5, rotated.get(0, 1));
        Assertions.assertEquals(4, rotated.get(0, 2));
        Assertions.assertEquals(3, rotated.get(1, 0));
        Assertions.assertEquals(2, rotated.get(1, 1));
        Assertions.assertEquals(1, rotated.get(1, 2));
    }

    @Test
    public void testRotate270() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        Matrix<Integer> rotated = matrix.rotate270();
        Assertions.assertEquals(2, rotated.rows);
        Assertions.assertEquals(3, rotated.cols);
        Assertions.assertEquals(2, rotated.get(0, 0));
        Assertions.assertEquals(4, rotated.get(0, 1));
        Assertions.assertEquals(6, rotated.get(0, 2));
        Assertions.assertEquals(1, rotated.get(1, 0));
    }

    @Test
    public void testTranspose() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Matrix<Integer> transposed = matrix.transpose();
        Assertions.assertEquals(3, transposed.rows);
        Assertions.assertEquals(2, transposed.cols);
        Assertions.assertEquals(1, transposed.get(0, 0));
        Assertions.assertEquals(4, transposed.get(0, 1));
        Assertions.assertEquals(2, transposed.get(1, 0));
        Assertions.assertEquals(5, transposed.get(1, 1));
    }

    @Test
    public void testReshape() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Matrix<Integer> reshaped = matrix.reshape(3, 2);
        Assertions.assertEquals(3, reshaped.rows);
        Assertions.assertEquals(2, reshaped.cols);
        Assertions.assertEquals(1, reshaped.get(0, 0));
        Assertions.assertEquals(2, reshaped.get(0, 1));
        Assertions.assertEquals(3, reshaped.get(1, 0));
        Assertions.assertEquals(4, reshaped.get(1, 1));
    }

    @Test
    public void testRepelem() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<Integer> repeated = matrix.repelem(2, 3);
        Assertions.assertEquals(4, repeated.rows);
        Assertions.assertEquals(6, repeated.cols);
        Assertions.assertEquals(1, repeated.get(0, 0));
        Assertions.assertEquals(1, repeated.get(0, 2));
        Assertions.assertEquals(1, repeated.get(1, 0));
        Assertions.assertEquals(2, repeated.get(0, 3));
        Assertions.assertEquals(3, repeated.get(2, 0));
        Assertions.assertEquals(4, repeated.get(3, 5));
    }

    @Test
    public void testRepmat() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<Integer> tiled = matrix.repmat(2, 3);
        Assertions.assertEquals(4, tiled.rows);
        Assertions.assertEquals(6, tiled.cols);
        Assertions.assertEquals(1, tiled.get(0, 0));
        Assertions.assertEquals(2, tiled.get(0, 1));
        Assertions.assertEquals(1, tiled.get(0, 2));
        Assertions.assertEquals(1, tiled.get(2, 0));
        Assertions.assertEquals(4, tiled.get(3, 5));
    }

    @Test
    public void testFlatten() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        List<Integer> flat = matrix.flatten();
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), flat);
    }

    @Test
    public void testFlatOp() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 3, 1, 4 }, { 1, 5, 9 } });

        matrix.flatOp(arrays -> {
            Arrays.sort(arrays);
        });

        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(1, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(0, 2));
    }

    @Test
    public void testVstack() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });

        Matrix<Integer> stacked = m1.vstack(m2);
        Assertions.assertEquals(4, stacked.rows);
        Assertions.assertEquals(2, stacked.cols);
        Assertions.assertEquals(1, stacked.get(0, 0));
        Assertions.assertEquals(5, stacked.get(2, 0));
        Assertions.assertEquals(8, stacked.get(3, 1));
    }

    @Test
    public void testVstackDifferentCols() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 3, 4, 5 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            m1.vstack(m2);
        });
    }

    @Test
    public void testHstack() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5 }, { 6 } });

        Matrix<Integer> stacked = m1.hstack(m2);
        Assertions.assertEquals(2, stacked.rows);
        Assertions.assertEquals(3, stacked.cols);
        Assertions.assertEquals(1, stacked.get(0, 0));
        Assertions.assertEquals(5, stacked.get(0, 2));
        Assertions.assertEquals(6, stacked.get(1, 2));
    }

    @Test
    public void testHstackDifferentRows() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 3 }, { 4 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            m1.hstack(m2);
        });
    }

    @Test
    public void testZipWith() throws Exception {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });

        Matrix<Integer> sum = m1.zipWith(m2, (a, b) -> a + b);
        Assertions.assertEquals(6, sum.get(0, 0));
        Assertions.assertEquals(8, sum.get(0, 1));
        Assertions.assertEquals(10, sum.get(1, 0));
        Assertions.assertEquals(12, sum.get(1, 1));
    }

    @Test
    public void testZipWithDifferentType() throws Exception {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Double> m2 = Matrix.of(new Double[][] { { 0.5, 1.0 }, { 1.5, 2.0 } });

        Matrix<String> result = m1.zipWith(m2, (a, b) -> a + ":" + b, String.class);
        Assertions.assertEquals("1:0.5", result.get(0, 0));
        Assertions.assertEquals("2:1.0", result.get(0, 1));
        Assertions.assertEquals("3:1.5", result.get(1, 0));
        Assertions.assertEquals("4:2.0", result.get(1, 1));
    }

    @Test
    public void testZipWithDifferentShape() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 3, 4, 5 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            m1.zipWith(m2, (a, b) -> a + b);
        });
    }

    @Test
    public void testZipWith3Matrices() throws Exception {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 9, 10 }, { 11, 12 } });

        Matrix<Integer> result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
        Assertions.assertEquals(15, result.get(0, 0));
        Assertions.assertEquals(18, result.get(0, 1));
        Assertions.assertEquals(21, result.get(1, 0));
        Assertions.assertEquals(24, result.get(1, 1));
    }

    @Test
    public void testStreamLU2RD() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        List<Integer> diagonal = matrix.streamLU2RD().toList();
        Assertions.assertEquals(Arrays.asList(1, 5, 9), diagonal);
    }

    @Test
    public void testStreamLU2RDEmpty() {
        Matrix<Integer> matrix = Matrix.of(new Integer[0][0]);

        List<Integer> diagonal = matrix.streamLU2RD().toList();
        Assertions.assertTrue(diagonal.isEmpty());
    }

    @Test
    public void testStreamRU2LD() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        List<Integer> diagonal = matrix.streamRU2LD().toList();
        Assertions.assertEquals(Arrays.asList(3, 5, 7), diagonal);
    }

    @Test
    public void testStreamH() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        List<Integer> elements = matrix.streamH().toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), elements);
    }

    @Test
    public void testStreamHRow() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        List<Integer> row1 = matrix.streamH(1).toList();
        Assertions.assertEquals(Arrays.asList(4, 5, 6), row1);
    }

    @Test
    public void testStreamHRange() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        List<Integer> rows = matrix.streamH(1, 3).toList();
        Assertions.assertEquals(Arrays.asList(3, 4, 5, 6), rows);
    }

    @Test
    public void testStreamV() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        List<Integer> elements = matrix.streamV().toList();
        Assertions.assertEquals(Arrays.asList(1, 3, 2, 4), elements);
    }

    @Test
    public void testStreamVColumn() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        List<Integer> col1 = matrix.streamV(1).toList();
        Assertions.assertEquals(Arrays.asList(2, 5, 8), col1);
    }

    @Test
    public void testStreamVRange() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        List<Integer> cols = matrix.streamV(1, 3).toList();
        Assertions.assertEquals(Arrays.asList(2, 5, 3, 6), cols);
    }

    @Test
    public void testStreamR() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        List<List<Integer>> rows = matrix.streamR().map(stream -> stream.toList()).toList();

        Assertions.assertEquals(3, rows.size());
        Assertions.assertEquals(Arrays.asList(1, 2), rows.get(0));
        Assertions.assertEquals(Arrays.asList(3, 4), rows.get(1));
        Assertions.assertEquals(Arrays.asList(5, 6), rows.get(2));
    }

    @Test
    public void testStreamRRange() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        List<List<Integer>> rows = matrix.streamR(1, 3).map(stream -> stream.toList()).toList();

        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(Arrays.asList(3, 4), rows.get(0));
        Assertions.assertEquals(Arrays.asList(5, 6), rows.get(1));
    }

    @Test
    public void testStreamC() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        List<List<Integer>> cols = matrix.streamC().map(stream -> stream.toList()).toList();

        Assertions.assertEquals(3, cols.size());
        Assertions.assertEquals(Arrays.asList(1, 4), cols.get(0));
        Assertions.assertEquals(Arrays.asList(2, 5), cols.get(1));
        Assertions.assertEquals(Arrays.asList(3, 6), cols.get(2));
    }

    @Test
    public void testStreamCRange() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        List<List<Integer>> cols = matrix.streamC(1, 3).map(stream -> stream.toList()).toList();

        Assertions.assertEquals(2, cols.size());
        Assertions.assertEquals(Arrays.asList(2, 5), cols.get(0));
        Assertions.assertEquals(Arrays.asList(3, 6), cols.get(1));
    }

    @Test
    public void testLength() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 } });
        Assertions.assertEquals(3, matrix.length(matrix.a[0]));
        Assertions.assertEquals(0, matrix.length(null));
    }

    @Test
    public void testForEach() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        List<Integer> values = new ArrayList<>();

        matrix.forEach(it -> values.add(it));

        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), values);
    }

    @Test
    public void testForEachRegion() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Integer> values = new ArrayList<>();

        matrix.forEach(1, 3, 1, 3, it -> values.add(it));

        Assertions.assertEquals(Arrays.asList(5, 6, 8, 9), values);
    }

    @Test
    public void testToDatasetH() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<String> columnNames = Arrays.asList("A", "B", "C");

        Dataset dataset = matrix.toDatasetH(columnNames);

        Assertions.assertEquals(2, dataset.size());
        Assertions.assertEquals(3, dataset.columnCount());
        Assertions.assertEquals(Arrays.asList(1, 4), dataset.getColumn("A"));
    }

    @Test
    public void testToDatasetHWrongColumnCount() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 } });
        List<String> columnNames = Arrays.asList("A", "B");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.toDatasetH(columnNames);
        });
    }

    @Test
    public void testToDatasetV() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<String> columnNames = Arrays.asList("Row1", "Row2");

        Dataset dataset = matrix.toDatasetV(columnNames);

        Assertions.assertEquals(3, dataset.size());
        Assertions.assertEquals(2, dataset.columnCount());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), dataset.getColumn("Row1"));
    }

    @Test
    public void testToDatasetVWrongColumnCount() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 } });
        List<String> columnNames = Arrays.asList("Row1", "Row2");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.toDatasetV(columnNames);
        });
    }

    @Test
    public void testPrintln() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        // Just verify it doesn't throw an exception
        matrix.println();
    }

    @Test
    public void testHashCode() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 5 } });

        Assertions.assertEquals(m1.hashCode(), m2.hashCode());
        Assertions.assertNotEquals(m1.hashCode(), m3.hashCode());
    }

    @Test
    public void testEquals() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 5 } });
        Matrix<Integer> m4 = Matrix.of(new Integer[][] { { 1, 2 } });

        Assertions.assertEquals(m1, m1);   // same instance
        Assertions.assertEquals(m1, m2);   // equal content
        Assertions.assertNotEquals(m1, m3);   // different content
        Assertions.assertNotEquals(m1, m4);   // different dimensions
        Assertions.assertNotEquals(m1, null);
        Assertions.assertNotEquals(m1, "not a matrix");
    }

    @Test
    public void testToString() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        String str = matrix.toString();

        Assertions.assertTrue(str.contains("1"));
        Assertions.assertTrue(str.contains("2"));
        Assertions.assertTrue(str.contains("3"));
        Assertions.assertTrue(str.contains("4"));
    }

    @Test
    public void testStreamAdvance() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        var stream = matrix.streamH();
        var iterator = stream.iterator();

        if (iterator instanceof ObjIteratorEx) {
            ObjIteratorEx<Integer> ex = (ObjIteratorEx<Integer>) iterator;
            ex.advance(2);
            Assertions.assertEquals(3, ex.next());
            Assertions.assertEquals(3, ex.count());
        }
    }

    @Test
    public void testStreamToArray() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        var stream = matrix.streamH(0, 2);
        var iterator = stream.iterator();

        if (iterator instanceof ObjIteratorEx) {
            ObjIteratorEx<Integer> ex = (ObjIteratorEx<Integer>) iterator;
            Integer[] array = ex.toArray(new Integer[0]);
            Assertions.assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, array);
        }
    }

    @Test
    public void testNullHandling() {
        Matrix<String> matrix = Matrix.of(new String[][] { { null, "a" }, { "b", null } });

        Assertions.assertNull(matrix.get(0, 0));
        Assertions.assertEquals("a", matrix.get(0, 1));
        Assertions.assertEquals("b", matrix.get(1, 0));
        Assertions.assertNull(matrix.get(1, 1));

        matrix.set(0, 0, "x");
        Assertions.assertEquals("x", matrix.get(0, 0));
    }

    @Test
    public void testLargeMatrix() {
        int size = 100;
        Integer[][] data = new Integer[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = i * size + j;
            }
        }

        Matrix<Integer> matrix = Matrix.of(data);
        Assertions.assertEquals(size, matrix.rows);
        Assertions.assertEquals(size, matrix.cols);
        Assertions.assertEquals(size * size, matrix.count);
        Assertions.assertEquals(5050, matrix.get(50, 50));
    }
}