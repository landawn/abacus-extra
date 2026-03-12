package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.Nullable;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.ObjIteratorEx;
import com.landawn.abacus.util.stream.Stream;

class MatrixTest extends TestBase {

    @Test
    public void testConstructor() {
        Integer[][] data = { { 1, 2, 3 }, { 4, 5, 6 } };
        Matrix<Integer> matrix = new Matrix<>(data);

        Assertions.assertEquals(2, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals(6, matrix.elementCount());
    }

    @Test
    public void testConstructorWithEmptyArray() {
        Integer[][] data = new Integer[0][0];
        Matrix<Integer> matrix = new Matrix<>(data);

        Assertions.assertEquals(0, matrix.rowCount());
        Assertions.assertEquals(0, matrix.columnCount());
        Assertions.assertEquals(0, matrix.elementCount());
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

        Assertions.assertEquals(2, matrix.rowCount());
        Assertions.assertEquals(2, matrix.columnCount());
        Assertions.assertEquals(1, matrix.get(0, 0));
    }

    @Test
    public void testOfVarargs() {
        Matrix<String> matrix = Matrix.of(new String[] { "a", "b" }, new String[] { "c", "d" });

        Assertions.assertEquals(2, matrix.rowCount());
        Assertions.assertEquals(2, matrix.columnCount());
        Assertions.assertEquals("a", matrix.get(0, 0));
    }

    @Test
    public void testDiagonalLU2RD() {
        Integer[] diagonal = { 1, 2, 3 };
        Matrix<Integer> matrix = Matrix.mainDiagonal(diagonal);

        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3, matrix.get(2, 2));
        Assertions.assertNull(matrix.get(0, 1));
        Assertions.assertNull(matrix.get(1, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        Integer[] diagonal = { 1, 2, 3 };
        Matrix<Integer> matrix = Matrix.antiDiagonal(diagonal);

        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
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
        Matrix<Integer> matrix = Matrix.diagonals(mainDiag, antiDiag);

        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(3, matrix.columnCount());
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3, matrix.get(2, 2));
        Assertions.assertEquals(7, matrix.get(0, 2));
        Assertions.assertEquals(9, matrix.get(2, 0));
    }

    @Test
    public void testDiagonalWithEmptyMainDiagonalAndDifferentRuntimeType() {
        Number[] mainDiag = new Integer[0];
        Number[] antiDiag = new Double[] { 1.5, 2.5 };

        Matrix<Number> matrix = Matrix.diagonals(mainDiag, antiDiag);

        Assertions.assertEquals(2, matrix.rowCount());
        Assertions.assertEquals(2, matrix.columnCount());
        Assertions.assertEquals(1.5d, matrix.get(0, 1).doubleValue());
        Assertions.assertEquals(2.5d, matrix.get(1, 0).doubleValue());
    }

    @Test
    public void testDiagonalWithIncompatibleRuntimeTypes() {
        Number[] mainDiag = new Integer[] { 1, 2 };
        Number[] antiDiag = new Double[] { 3.0, 4.0 };

        Matrix<Number> matrix = Matrix.diagonals(mainDiag, antiDiag);
        Assertions.assertEquals(Number.class, matrix.componentType());
        Assertions.assertEquals(1, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(1, 1));
        Assertions.assertEquals(3.0d, matrix.get(0, 1).doubleValue());
        Assertions.assertEquals(4.0d, matrix.get(1, 0).doubleValue());
    }

    @Test
    public void testDiagonalWithDifferentLengths() {
        Integer[] mainDiag = { 1, 2, 3 };
        Integer[] antiDiag = { 7, 8 };

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Matrix.diagonals(mainDiag, antiDiag);
        });
    }

    @Test
    public void testComponentType() {
        Matrix<String> matrix = Matrix.of(new String[][] { { "a", "b" } });
        Assertions.assertEquals(String.class, matrix.componentType());

        Matrix<Integer> intMatrix = Matrix.of(new Integer[][] { { 1, 2 } });
        Assertions.assertEquals(Integer.class, intMatrix.componentType());

        Matrix<String> repeated = Matrix.repeat(1, 2, "x");
        Assertions.assertEquals(String.class, repeated.componentType());
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

        Assertions.assertEquals(1, matrix.above(1, 0).orElse(null));
        Assertions.assertFalse(matrix.above(0, 0).isPresent());
    }

    @Test
    public void testDownOf() {
        Integer[][] data = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Assertions.assertEquals(3, matrix.below(0, 0).orElse(null));
        Assertions.assertFalse(matrix.below(1, 0).isPresent());
    }

    @Test
    public void testLeftOf() {
        Integer[][] data = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Assertions.assertEquals(1, matrix.left(0, 1).orElse(null));
        Assertions.assertFalse(matrix.left(0, 0).isPresent());
    }

    @Test
    public void testRightOf() {
        Integer[][] data = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Assertions.assertEquals(2, matrix.right(0, 0).orElse(null));
        Assertions.assertFalse(matrix.right(0, 1).isPresent());
    }

    @Test
    public void testRow() {
        Integer[][] data = { { 1, 2, 3 }, { 4, 5, 6 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Integer[] row = matrix.rowView(0);
        Assertions.assertArrayEquals(new Integer[] { 1, 2, 3 }, row);

        row[0] = 10; // This modifies the matrix
        Assertions.assertEquals(10, matrix.get(0, 0));
    }

    @Test
    public void testRowOnObjectMatrixDoesNotNarrowInternalStorage() {
        Matrix<Object> matrix = Matrix.of(new Object[][] { { "a" } });
        Object[] row = matrix.rowView(0);

        Assertions.assertEquals(Object.class, row.getClass().getComponentType());

        matrix.set(0, 0, 1);
        Assertions.assertEquals(1, matrix.get(0, 0));
    }

    @Test
    public void testRowInvalidIndex() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.rowView(-1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.rowView(2);
        });
    }

    @Test
    public void testColumn() {
        Integer[][] data = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        Matrix<Integer> matrix = Matrix.of(data);

        Integer[] column = matrix.columnCopy(1);
        Assertions.assertArrayEquals(new Integer[] { 2, 4, 6 }, column);

        column[0] = 10; // This does not modify the matrix
        Assertions.assertEquals(2, matrix.get(0, 1));
    }

    @Test
    public void testColumnInvalidIndex() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.columnCopy(-1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.columnCopy(2);
        });
    }

    @Test
    public void testSetRow() {
        Integer[][] data = { { 1, 2, 3 }, { 4, 5, 6 } };
        Matrix<Integer> matrix = Matrix.of(data);

        matrix.setRow(0, new Integer[] { 7, 8, 9 });
        Assertions.assertArrayEquals(new Integer[] { 7, 8, 9 }, matrix.rowView(0));
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
        Assertions.assertArrayEquals(new Integer[] { 7, 8, 9 }, matrix.columnCopy(1));
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
        Assertions.assertArrayEquals(new Integer[] { 2, 4, 6 }, matrix.rowView(0));
        Assertions.assertArrayEquals(new Integer[] { 4, 5, 6 }, matrix.rowView(1));
    }

    @Test
    public void testUpdateRowInvalidIndex() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateRow(-1, x -> x * 2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateRow(2, x -> x * 2));
    }

    @Test
    public void testUpdateRowNullOperator() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateRow(0, (Throwables.UnaryOperator<Integer, RuntimeException>) null));
    }

    @Test
    public void testUpdateColumn() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        matrix.updateColumn(1, x -> x + 10);
        Assertions.assertArrayEquals(new Integer[] { 12, 14, 16 }, matrix.columnCopy(1));
        Assertions.assertArrayEquals(new Integer[] { 1, 3, 5 }, matrix.columnCopy(0));
    }

    @Test
    public void testUpdateColumnInvalidIndex() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateColumn(-1, x -> x + 10));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateColumn(2, x -> x + 10));
    }

    @Test
    public void testUpdateColumnNullOperator() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateColumn(0, (Throwables.UnaryOperator<Integer, RuntimeException>) null));
    }

    @Test
    public void testGetLU2RD() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        Integer[] diagonal = matrix.getMainDiagonal();
        Assertions.assertArrayEquals(new Integer[] { 1, 5, 9 }, diagonal);
    }

    @Test
    public void testGetLU2RDNonSquare() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Assertions.assertThrows(IllegalStateException.class, () -> {
            matrix.getMainDiagonal();
        });
    }

    @Test
    public void testSetLU2RD() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        matrix.setMainDiagonal(new Integer[] { 10, 20, 30 });
        Assertions.assertEquals(10, matrix.get(0, 0));
        Assertions.assertEquals(20, matrix.get(1, 1));
        Assertions.assertEquals(30, matrix.get(2, 2));
    }

    @Test
    public void testSetLU2RDShortArray() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.setMainDiagonal(new Integer[] { 10 });
        });
    }

    @Test
    public void testUpdateLU2RD() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        matrix.updateMainDiagonal(x -> x * 10);
        Assertions.assertEquals(10, matrix.get(0, 0));
        Assertions.assertEquals(50, matrix.get(1, 1));
        Assertions.assertEquals(90, matrix.get(2, 2));
        Assertions.assertEquals(2, matrix.get(0, 1)); // unchanged
    }

    @Test
    public void testGetRU2LD() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        Integer[] diagonal = matrix.getAntiDiagonal();
        Assertions.assertArrayEquals(new Integer[] { 3, 5, 7 }, diagonal);
    }

    @Test
    public void testSetRU2LD() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        matrix.setAntiDiagonal(new Integer[] { 10, 20, 30 });
        Assertions.assertEquals(10, matrix.get(0, 2));
        Assertions.assertEquals(20, matrix.get(1, 1));
        Assertions.assertEquals(30, matrix.get(2, 0));
    }

    @Test
    public void testUpdateRU2LD() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        matrix.updateAntiDiagonal(x -> x * -1);
        Assertions.assertEquals(-3, matrix.get(0, 2));
        Assertions.assertEquals(-5, matrix.get(1, 1));
        Assertions.assertEquals(-7, matrix.get(2, 0));
        Assertions.assertEquals(1, matrix.get(0, 0)); // unchanged
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
    public void testUpdateAllNullOperator() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> emptyLike = Matrix.of(new Integer[][] { {}, {} });

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.UnaryOperator<Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> emptyLike.updateAll((Throwables.UnaryOperator<Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> emptyLike.updateAll((Throwables.IntBiFunction<Integer, RuntimeException>) null));
    }

    @Test
    public void testDiagonalAndReplaceIfNullCallbacks() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> emptyLike = Matrix.of(new Integer[][] { {}, {} });

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.UnaryOperator<Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.UnaryOperator<Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.Predicate<Integer, RuntimeException>) null, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0));

        Assertions.assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.UnaryOperator<Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.UnaryOperator<Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.Predicate<Integer, RuntimeException>) null, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0));
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
    public void testMapNullMapper() throws Exception {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.map((Throwables.UnaryOperator<Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.mapToBoolean((Throwables.ToBooleanFunction<Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.map((Throwables.Function) null, String.class));
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
        matrix.copyFrom(patch);
        Assertions.assertEquals(7, matrix.get(0, 0));
        Assertions.assertEquals(8, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(0, 2)); // unchanged
        Assertions.assertEquals(4, matrix.get(1, 0)); // unchanged
    }

    @Test
    public void testFillWithPosition() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        Integer[][] patch = { { 10, 11 }, { 12, 13 } };
        matrix.copyFrom(1, 1, patch);
        Assertions.assertEquals(1, matrix.get(0, 0)); // unchanged
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
        Assertions.assertEquals(1, matrix.get(0, 0)); // original unchanged
        Assertions.assertEquals(10, copy.get(0, 0));
    }

    @Test
    public void testCopyRows() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        Matrix<Integer> copy = matrix.copy(0, 2);
        Assertions.assertEquals(2, copy.rowCount());
        Assertions.assertEquals(2, copy.columnCount());
        Assertions.assertEquals(1, copy.get(0, 0));
        Assertions.assertEquals(4, copy.get(1, 1));
    }

    @Test
    public void testCopyRegion() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        Matrix<Integer> copy = matrix.copy(1, 3, 1, 3);
        Assertions.assertEquals(2, copy.rowCount());
        Assertions.assertEquals(2, copy.columnCount());
        Assertions.assertEquals(5, copy.get(0, 0));
        Assertions.assertEquals(6, copy.get(0, 1));
        Assertions.assertEquals(8, copy.get(1, 0));
        Assertions.assertEquals(9, copy.get(1, 1));
    }

    @Test
    public void testExtend() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<Integer> extended = matrix.resize(3, 3);
        Assertions.assertEquals(3, extended.rowCount());
        Assertions.assertEquals(3, extended.columnCount());
        Assertions.assertEquals(1, extended.get(0, 0));
        Assertions.assertEquals(4, extended.get(1, 1));
        Assertions.assertNull(extended.get(2, 2));
    }

    @Test
    public void testExtendWithDefault() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<Integer> extended = matrix.resize(3, 3, 0);
        Assertions.assertEquals(3, extended.rowCount());
        Assertions.assertEquals(3, extended.columnCount());
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
        Assertions.assertEquals(4, extended.rowCount());
        Assertions.assertEquals(4, extended.columnCount());
        Assertions.assertEquals(0, extended.get(0, 0));
        Assertions.assertEquals(1, extended.get(1, 1));
        Assertions.assertEquals(4, extended.get(2, 2));
        Assertions.assertEquals(0, extended.get(3, 3));
    }

    @Test
    public void testReverseH() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        matrix.flipInPlaceHorizontally();
        Assertions.assertEquals(3, matrix.get(0, 0));
        Assertions.assertEquals(2, matrix.get(0, 1));
        Assertions.assertEquals(1, matrix.get(0, 2));
        Assertions.assertEquals(6, matrix.get(1, 0));
    }

    @Test
    public void testReverseV() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        matrix.flipInPlaceVertically();
        Assertions.assertEquals(5, matrix.get(0, 0));
        Assertions.assertEquals(6, matrix.get(0, 1));
        Assertions.assertEquals(3, matrix.get(1, 0));
        Assertions.assertEquals(1, matrix.get(2, 0));
    }

    @Test
    public void testFlipH() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Matrix<Integer> flipped = matrix.flipHorizontally();
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

        Matrix<Integer> flipped = matrix.flipVertically();
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
        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(3, rotated.columnCount());
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
        Assertions.assertEquals(2, rotated.rowCount());
        Assertions.assertEquals(3, rotated.columnCount());
        Assertions.assertEquals(2, rotated.get(0, 0));
        Assertions.assertEquals(4, rotated.get(0, 1));
        Assertions.assertEquals(6, rotated.get(0, 2));
        Assertions.assertEquals(1, rotated.get(1, 0));
    }

    @Test
    public void testTranspose() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Matrix<Integer> transposed = matrix.transpose();
        Assertions.assertEquals(3, transposed.rowCount());
        Assertions.assertEquals(2, transposed.columnCount());
        Assertions.assertEquals(1, transposed.get(0, 0));
        Assertions.assertEquals(4, transposed.get(0, 1));
        Assertions.assertEquals(2, transposed.get(1, 0));
        Assertions.assertEquals(5, transposed.get(1, 1));
    }

    @Test
    public void testReshape() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        Matrix<Integer> reshaped = matrix.reshape(3, 2);
        Assertions.assertEquals(3, reshaped.rowCount());
        Assertions.assertEquals(2, reshaped.columnCount());
        Assertions.assertEquals(1, reshaped.get(0, 0));
        Assertions.assertEquals(2, reshaped.get(0, 1));
        Assertions.assertEquals(3, reshaped.get(1, 0));
        Assertions.assertEquals(4, reshaped.get(1, 1));

        // Test reshape with too-small dimensions throws exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.reshape(1, 4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.reshape(2, 2));
    }

    @Test
    public void testRepelem() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<Integer> repeated = matrix.repeatElements(2, 3);
        Assertions.assertEquals(4, repeated.rowCount());
        Assertions.assertEquals(6, repeated.columnCount());
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

        Matrix<Integer> tiled = matrix.repeatMatrix(2, 3);
        Assertions.assertEquals(4, tiled.rowCount());
        Assertions.assertEquals(6, tiled.columnCount());
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

        matrix.applyOnFlattened(arrays -> {
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

        Matrix<Integer> stacked = m1.stackVertically(m2);
        Assertions.assertEquals(4, stacked.rowCount());
        Assertions.assertEquals(2, stacked.columnCount());
        Assertions.assertEquals(1, stacked.get(0, 0));
        Assertions.assertEquals(5, stacked.get(2, 0));
        Assertions.assertEquals(8, stacked.get(3, 1));
    }

    @Test
    public void testVstackDifferentCols() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 3, 4, 5 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            m1.stackVertically(m2);
        });
    }

    @Test
    public void testHstack() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5 }, { 6 } });

        Matrix<Integer> stacked = m1.stackHorizontally(m2);
        Assertions.assertEquals(2, stacked.rowCount());
        Assertions.assertEquals(3, stacked.columnCount());
        Assertions.assertEquals(1, stacked.get(0, 0));
        Assertions.assertEquals(5, stacked.get(0, 2));
        Assertions.assertEquals(6, stacked.get(1, 2));
    }

    @Test
    public void testHstackDifferentRows() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 3 }, { 4 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            m1.stackHorizontally(m2);
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
    public void testZipWithNullArguments() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 5, 6 }, { 7, 8 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 9, 10 }, { 11, 12 } });

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> m1.zipWith(m2, (Throwables.BiFunction<Integer, Integer, Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> m1.zipWith(m2, (Throwables.BiFunction<Integer, Integer, String, RuntimeException>) null, String.class));
        Assertions.assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a + b, (Class<Integer>) null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> m1.zipWith(m2, m3, (Throwables.TriFunction<Integer, Integer, Integer, Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> m1.zipWith(m2, m3, (Throwables.TriFunction<Integer, Integer, Integer, String, RuntimeException>) null, String.class));
        Assertions.assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> a + b + c, (Class<Integer>) null));
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

        List<Integer> diagonal = matrix.streamMainDiagonal().toList();
        Assertions.assertEquals(Arrays.asList(1, 5, 9), diagonal);
    }

    @Test
    public void testStreamLU2RDEmpty() {
        Matrix<Integer> matrix = Matrix.of(new Integer[0][0]);

        List<Integer> diagonal = matrix.streamMainDiagonal().toList();
        Assertions.assertTrue(diagonal.isEmpty());
    }

    @Test
    public void testStreamRU2LD() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        List<Integer> diagonal = matrix.streamAntiDiagonal().toList();
        Assertions.assertEquals(Arrays.asList(3, 5, 7), diagonal);
    }

    @Test
    public void testStreamH() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        List<Integer> elements = matrix.streamHorizontal().toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), elements);
    }

    @Test
    public void testStreamHRow() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        List<Integer> row1 = matrix.streamHorizontal(1).toList();
        Assertions.assertEquals(Arrays.asList(4, 5, 6), row1);
    }

    @Test
    public void testStreamHRange() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        List<Integer> rows = matrix.streamHorizontal(1, 3).toList();
        Assertions.assertEquals(Arrays.asList(3, 4, 5, 6), rows);
    }

    @Test
    public void testStreamV() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        List<Integer> elements = matrix.streamVertical().toList();
        Assertions.assertEquals(Arrays.asList(1, 3, 2, 4), elements);
    }

    @Test
    public void testStreamVColumn() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });

        List<Integer> col1 = matrix.streamVertical(1).toList();
        Assertions.assertEquals(Arrays.asList(2, 5, 8), col1);
    }

    @Test
    public void testStreamVRange() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        List<Integer> columnCount = matrix.streamVertical(1, 3).toList();
        Assertions.assertEquals(Arrays.asList(2, 5, 3, 6), columnCount);
    }

    @Test
    public void testStreamR() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        List<List<Integer>> rows = matrix.streamRows().map(stream -> stream.toList()).toList();

        Assertions.assertEquals(3, rows.size());
        Assertions.assertEquals(Arrays.asList(1, 2), rows.get(0));
        Assertions.assertEquals(Arrays.asList(3, 4), rows.get(1));
        Assertions.assertEquals(Arrays.asList(5, 6), rows.get(2));
    }

    @Test
    public void testStreamRWithZeroColumnRows() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { {}, {}, {} });

        Assertions.assertEquals(3, matrix.rowCount());
        Assertions.assertEquals(0, matrix.columnCount());
        Assertions.assertTrue(matrix.isEmpty());

        List<List<Integer>> rows = matrix.streamRows().map(stream -> stream.toList()).toList();

        Assertions.assertEquals(3, rows.size());
        Assertions.assertTrue(rows.get(0).isEmpty());
        Assertions.assertTrue(rows.get(1).isEmpty());
        Assertions.assertTrue(rows.get(2).isEmpty());
    }

    @Test
    public void testStreamRRange() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });

        List<List<Integer>> rows = matrix.streamRows(1, 3).map(stream -> stream.toList()).toList();

        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(Arrays.asList(3, 4), rows.get(0));
        Assertions.assertEquals(Arrays.asList(5, 6), rows.get(1));
    }

    @Test
    public void testStreamC() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        List<List<Integer>> columnCount = matrix.streamColumns().map(stream -> stream.toList()).toList();

        Assertions.assertEquals(3, columnCount.size());
        Assertions.assertEquals(Arrays.asList(1, 4), columnCount.get(0));
        Assertions.assertEquals(Arrays.asList(2, 5), columnCount.get(1));
        Assertions.assertEquals(Arrays.asList(3, 6), columnCount.get(2));
    }

    @Test
    public void testStreamCRange() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });

        List<List<Integer>> columnCount = matrix.streamColumns(1, 3).map(stream -> stream.toList()).toList();

        Assertions.assertEquals(2, columnCount.size());
        Assertions.assertEquals(Arrays.asList(2, 5), columnCount.get(0));
        Assertions.assertEquals(Arrays.asList(3, 6), columnCount.get(1));
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
    public void testForEachNullAction() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.forEach((Throwables.Consumer<Integer, RuntimeException>) null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.forEach(0, 2, 0, 2, (Throwables.Consumer<Integer, RuntimeException>) null));
    }

    @Test
    public void testToDatasetH() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<String> columnNames = Arrays.asList("A", "B", "C");

        Dataset dataset = matrix.toRowDataset(columnNames);

        Assertions.assertEquals(2, dataset.size());
        Assertions.assertEquals(3, dataset.columnCount());
        Assertions.assertEquals(Arrays.asList(1, 4), dataset.getColumn("A"));
    }

    @Test
    public void testToDatasetHWrongColumnCount() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 } });
        List<String> columnNames = Arrays.asList("A", "B");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.toRowDataset(columnNames);
        });
    }

    @Test
    public void testToDatasetHNullColumnNames() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.toRowDataset(null));
    }

    @Test
    public void testToDatasetV() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<String> columnNames = Arrays.asList("Row1", "Row2");

        Dataset dataset = matrix.toColumnDataset(columnNames);

        Assertions.assertEquals(3, dataset.size());
        Assertions.assertEquals(2, dataset.columnCount());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), dataset.getColumn("Row1"));
    }

    @Test
    public void testToDatasetVWrongColumnCount() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 } });
        List<String> columnNames = Arrays.asList("Row1", "Row2");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            matrix.toColumnDataset(columnNames);
        });
    }

    @Test
    public void testToDatasetVNullColumnNames() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.toColumnDataset(null));
    }

    @Test
    public void testPrintln() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        assertFalse(matrix.isEmpty());
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(matrix::println);
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

        Assertions.assertEquals(m1, m1); // same instance
        Assertions.assertEquals(m1, m2); // equal content
        Assertions.assertNotEquals(m1, m3); // different content
        Assertions.assertNotEquals(m1, m4); // different dimensions
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
        var stream = matrix.streamHorizontal();
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
        var stream = matrix.streamHorizontal(0, 2);
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
    public void testRejectUnrepresentableZeroRowNonZeroColumnShape() {
        Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        // Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.copy(0, 0));
        // Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.copy(0, 0, 0, 1));
        // Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.extend(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.reshape(0, 1));
    }

    @Test
    public void testRepeatSupportsWiderGenericType() {
        Matrix<Number> matrix = Matrix.repeat(1, 1, 1);

        matrix.set(0, 0, 2.5d);
        Assertions.assertEquals(2.5d, matrix.get(0, 0).doubleValue(), 0.000001d);
    }

    @Test
    public void testRepeatSupportsWiderGenericTypeAfterRowView() {
        Matrix<Number> matrix = Matrix.repeat(1, 1, 1);

        Number[] row = matrix.rowView(0);
        Assertions.assertEquals(Integer.class, row.getClass().getComponentType());

        matrix.set(0, 0, 2.5d);
        Assertions.assertEquals(2.5d, matrix.get(0, 0).doubleValue(), 0.000001d);
    }

    @Test
    public void testDiagonalSupportsWiderGenericTypeAfterRowView() {
        Number[] mainDiag = new Integer[] { 1 };
        Matrix<Number> matrix = Matrix.mainDiagonal(mainDiag);

        Number[] row = matrix.rowView(0);
        Assertions.assertEquals(Integer.class, row.getClass().getComponentType());

        matrix.set(0, 0, 2.5d);
        Assertions.assertEquals(2.5d, matrix.get(0, 0).doubleValue(), 0.000001d);
    }

    @Test
    public void testTransformsStillWorkAfterWideningElementTypeAtRuntime() {
        Matrix<Number> matrix = Matrix.repeat(1, 2, 1);
        matrix.rowView(0);
        matrix.set(0, 1, 2.5d);

        Matrix<Number> transposed = matrix.transpose();
        Assertions.assertEquals(2.5d, transposed.get(1, 0).doubleValue(), 0.000001d);

        Matrix<Number> reshaped = matrix.reshape(2, 1);
        Assertions.assertEquals(2.5d, reshaped.get(1, 0).doubleValue(), 0.000001d);
    }

    @Test
    public void testSetRowAndSetColumnWidenStorageWhenNeeded() {
        Matrix<Number> matrix = Matrix.repeat(2, 2, 1);
        matrix.rowView(0);
        matrix.rowView(1);

        matrix.setRow(0, new Number[] { 1.5d, 2.5d });
        matrix.setColumn(1, new Number[] { 3.5d, 4.5d });

        Assertions.assertEquals(1.5d, matrix.get(0, 0).doubleValue(), 0.000001d);
        Assertions.assertEquals(3.5d, matrix.get(0, 1).doubleValue(), 0.000001d);
        Assertions.assertEquals(4.5d, matrix.get(1, 1).doubleValue(), 0.000001d);
    }

    @Test
    public void testFillWidenStorageWhenNeeded() {
        Matrix<Number> matrix = Matrix.repeat(1, 2, 1);
        matrix.rowView(0);

        matrix.fill(2.5d);
        Assertions.assertEquals(2.5d, matrix.get(0, 0).doubleValue(), 0.000001d);
        Assertions.assertEquals(2.5d, matrix.get(0, 1).doubleValue(), 0.000001d);

        matrix.copyFrom(new Number[][] { { 3.5d, 4.5d } });
        Assertions.assertEquals(3.5d, matrix.get(0, 0).doubleValue(), 0.000001d);
        Assertions.assertEquals(4.5d, matrix.get(0, 1).doubleValue(), 0.000001d);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testIncompatibleWriteOnNarrowBackingArrayThrowsIllegalArgumentException() {
        Matrix<Integer> intMatrix = Matrix.of(new Integer[][] { { 1 } });
        Matrix<Number> numberView = (Matrix<Number>) (Matrix<?>) intMatrix;

        Assertions.assertThrows(IllegalArgumentException.class, () -> numberView.set(0, 0, 2.5d));
    }

    @Test
    public void testSetRowColumnAndAntiDiagonalRejectNullArguments() {
        Matrix<String> matrix = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });

        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setRow(0, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setColumn(0, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> matrix.setAntiDiagonal(null));
    }

    @Test
    public void testCopyAndTransformsPreserveElementTypeForRepeatMatrices() {
        Matrix<String> matrix = Matrix.repeat(1, 2, "x");

        Matrix<String> copied = matrix.copy();
        Assertions.assertEquals(String.class, copied.componentType());
        Assertions.assertEquals(String.class, copied.rowView(0).getClass().getComponentType());

        Matrix<String> transposed = matrix.transpose();
        Assertions.assertEquals(String.class, transposed.componentType());
        Assertions.assertEquals(String.class, transposed.rowView(0).getClass().getComponentType());

        Matrix<String> reshaped = matrix.reshape(2, 1);
        Assertions.assertEquals(String.class, reshaped.componentType());
        Assertions.assertEquals(String.class, reshaped.rowView(0).getClass().getComponentType());

        Matrix<String> repeated = matrix.repeatElements(2, 2);
        Assertions.assertEquals(String.class, repeated.componentType());
        Assertions.assertEquals(String.class, repeated.rowView(0).getClass().getComponentType());
    }

    @Test
    public void testStackOperationsPreserveElementTypeForRepeatMatrices() {
        Matrix<Integer> top = Matrix.repeat(1, 2, 1);
        Matrix<Integer> bottom = Matrix.repeat(1, 2, 2);

        Matrix<Integer> vstacked = top.stackVertically(bottom);
        Assertions.assertEquals(Integer.class, vstacked.componentType());
        Assertions.assertEquals(Integer.class, vstacked.rowView(0).getClass().getComponentType());
        vstacked.set(0, 0, 3);
        Assertions.assertEquals(3, vstacked.get(0, 0));

        Matrix<Integer> hstacked = top.stackHorizontally(bottom);
        Assertions.assertEquals(Integer.class, hstacked.componentType());
        Assertions.assertEquals(Integer.class, hstacked.rowView(0).getClass().getComponentType());
        hstacked.set(0, 3, 4);
        Assertions.assertEquals(4, hstacked.get(0, 3));
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
        Assertions.assertEquals(size, matrix.rowCount());
        Assertions.assertEquals(size, matrix.columnCount());
        Assertions.assertEquals(size * size, matrix.elementCount());
        Assertions.assertEquals(5050, matrix.get(50, 50));
    }

    @Nested
    class JavadocExampleMatrixGroup2Test_Matrix extends TestBase {
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

    @Nested
    class JavadocExampleMatrixTest_Matrix extends TestBase {
        // ==================== Matrix (generic) Javadoc Examples ====================

        @Test
        public void testMatrixOfGet() {
            Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            assertEquals(2, matrix.rowCount());
            assertEquals(3, matrix.columnCount());
            assertEquals(Integer.valueOf(1), matrix.get(0, 0));
            assertEquals(Integer.valueOf(6), matrix.get(1, 2));
        }

        @Test
        public void testMatrixEmptyRowCount() {
            Matrix<String> matrix = new Matrix<>(new String[0][0]);
            assertEquals(0, matrix.rowCount());
            assertEquals(0, matrix.columnCount());
        }
    }

    @Nested
    @Tag("2025")
    class Matrix2025Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            String[][] arr = { { "A", "B" }, { "C", "D" } };
            Matrix<String> m = new Matrix<>(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals("A", m.get(0, 0));
            assertEquals("D", m.get(1, 1));
        }

        @Test
        public void testConstructor_withNullArray() {
            assertThrows(IllegalArgumentException.class, () -> new Matrix<>(null));
        }

        @Test
        public void testConstructor_withEmptyArray() {
            Matrix<String> m = new Matrix<>(new String[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withSingleElement() {
            Matrix<String> m = new Matrix<>(new String[][] { { "X" } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals("X", m.get(0, 0));
        }

        @Test
        public void testConstructor_withNullElements() {
            String[][] arr = { { "A", null }, { null, "D" } };
            Matrix<String> m = new Matrix<>(arr);
            assertEquals("A", m.get(0, 0));
            assertNull(m.get(0, 1));
            assertNull(m.get(1, 0));
            assertEquals("D", m.get(1, 1));
        }

        // ============ Factory Method Tests ============

        @Test
        public void testOf_withValidArray() {
            String[][] arr = { { "A", "B" }, { "C", "D" } };
            Matrix<String> m = Matrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals("A", m.get(0, 0));
        }

        @Test
        public void testOf_withNullArray() {
            Matrix<String> m = Matrix.of(new String[][] {});
            assertTrue(m.isEmpty());

            assertThrows(IllegalArgumentException.class, () -> Matrix.of((String[][]) null));
        }

        @Test
        public void testOf_withEmptyArray() {
            Matrix<String> m = Matrix.of(new String[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withVarargs() {
            Matrix<Integer> m = Matrix.of(new Integer[] { 1, 2, 3 }, new Integer[] { 4, 5, 6 });
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(Integer.valueOf(1), m.get(0, 0));
            assertEquals(Integer.valueOf(6), m.get(1, 2));
        }

        @Test
        public void testRepeat() {
            Matrix<String> m = Matrix.repeat(2, 3, "a");
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals("a", m.get(i, j));
                }
            }
        }

        @Test
        public void testRepeat_withNegativeDimensions() {
            assertThrows(IllegalArgumentException.class, () -> Matrix.repeat(-1, 2, "a"));
            assertThrows(IllegalArgumentException.class, () -> Matrix.repeat(1, -2, "a"));
        }

        @Test
        public void testDiagonalLU2RD() {
            Matrix<Integer> m = Matrix.mainDiagonal(new Integer[] { 1, 2, 3 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(Integer.valueOf(1), m.get(0, 0));
            assertEquals(Integer.valueOf(2), m.get(1, 1));
            assertEquals(Integer.valueOf(3), m.get(2, 2));
            assertNull(m.get(0, 1));
            assertNull(m.get(1, 0));
        }

        @Test
        public void testDiagonalRU2LD() {
            Matrix<Integer> m = Matrix.antiDiagonal(new Integer[] { 1, 2, 3 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(Integer.valueOf(1), m.get(0, 2));
            assertEquals(Integer.valueOf(2), m.get(1, 1));
            assertEquals(Integer.valueOf(3), m.get(2, 0));
            assertNull(m.get(0, 0));
            assertNull(m.get(2, 2));
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            Matrix<String> m = Matrix.diagonals(new String[] { "A", "B", "C" }, new String[] { "X", "Y", "Z" });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(1, 1));
            assertEquals("C", m.get(2, 2));
            assertEquals("X", m.get(0, 2));
            assertEquals("Z", m.get(2, 0));
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            Matrix<String> m = Matrix.diagonals(new String[] { "A", "B", "C" }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(1, 1));
            assertEquals("C", m.get(2, 2));
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            Matrix<String> m = Matrix.diagonals(null, new String[] { "X", "Y", "Z" });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals("X", m.get(0, 2));
            assertEquals("Y", m.get(1, 1));
            assertEquals("Z", m.get(2, 0));
        }

        @Test
        public void testDiagonal_withBothNull() {
            assertTrue(Matrix.diagonals(new String[] {}, new String[] {}).isEmpty());
            assertTrue(Matrix.diagonals(new String[] {}, null).isEmpty());
            assertTrue(Matrix.diagonals(null, new String[] {}).isEmpty());

            assertThrows(IllegalArgumentException.class, () -> Matrix.diagonals(null, null));
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> Matrix.diagonals(new String[] { "A", "B" }, new String[] { "X", "Y", "Z" }));
        }

        // ============ Component Type Tests ============

        @Test
        public void testComponentType() {
            Matrix<String> m = Matrix.of(new String[][] { { "A" } });
            assertEquals(String.class, m.componentType());
        }

        @Test
        public void testComponentType_withInteger() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1 } });
            assertEquals(Integer.class, m.componentType());
        }

        // ============ Get/Set Tests ============

        @Test
        public void testGet() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            assertEquals("A", m.get(0, 0));
            assertEquals("E", m.get(1, 1));
            assertEquals("F", m.get(1, 2));
        }

        @Test
        public void testGet_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
        }

        @Test
        public void testGetWithPoint() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertEquals("A", m.get(Point.of(0, 0)));
            assertEquals("D", m.get(Point.of(1, 1)));
            assertEquals("B", m.get(Point.of(0, 1)));
        }

        @Test
        public void testSet() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.set(0, 0, "X");
            assertEquals("X", m.get(0, 0));

            m.set(1, 1, "Y");
            assertEquals("Y", m.get(1, 1));
        }

        @Test
        public void testSet_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, "X"));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, "X"));
        }

        @Test
        public void testSetWithPoint() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.set(Point.of(0, 0), "Z");
            assertEquals("Z", m.get(Point.of(0, 0)));
        }

        @Test
        public void testSet_withNull() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.set(0, 0, null);
            assertNull(m.get(0, 0));
        }

        // ============ Adjacent Element Tests ============

        @Test
        public void testUpOf() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });

            Nullable<String> up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals("A", up.get());

            // Top row has no element above
            Nullable<String> empty = m.above(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testDownOf() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });

            Nullable<String> down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals("C", down.get());

            // Bottom row has no element below
            Nullable<String> empty = m.below(1, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testLeftOf() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });

            Nullable<String> left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals("A", left.get());

            // Leftmost column has no element to the left
            Nullable<String> empty = m.left(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testRightOf() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });

            Nullable<String> right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals("B", right.get());

            // Rightmost column has no element to the right
            Nullable<String> empty = m.right(0, 1);
            assertFalse(empty.isPresent());
        }

        // ============ Row/Column Operations Tests ============

        @Test
        public void testRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            assertArrayEquals(new String[] { "A", "B", "C" }, m.rowView(0));
            assertArrayEquals(new String[] { "D", "E", "F" }, m.rowView(1));
        }

        @Test
        public void testRow_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(2));
        }

        @Test
        public void testColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            assertArrayEquals(new String[] { "A", "D" }, m.columnCopy(0));
            assertArrayEquals(new String[] { "B", "E" }, m.columnCopy(1));
            assertArrayEquals(new String[] { "C", "F" }, m.columnCopy(2));
        }

        @Test
        public void testColumn_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.setRow(0, new String[] { "X", "Y" });
            assertArrayEquals(new String[] { "X", "Y" }, m.rowView(0));
            assertArrayEquals(new String[] { "C", "D" }, m.rowView(1)); // unchanged
        }

        @Test
        public void testSetRow_wrongSize() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "X" }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "X", "Y", "Z" }));
        }

        @Test
        public void testSetColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.setColumn(0, new String[] { "X", "Y" });
            assertArrayEquals(new String[] { "X", "Y" }, m.columnCopy(0));
            assertArrayEquals(new String[] { "B", "D" }, m.columnCopy(1)); // unchanged
        }

        @Test
        public void testSetColumn_wrongSize() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "X" }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "X", "Y", "Z" }));
        }

        @Test
        public void testUpdateRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.updateRow(0, x -> x + "1");
            assertArrayEquals(new String[] { "A1", "B1" }, m.rowView(0));
            assertArrayEquals(new String[] { "C", "D" }, m.rowView(1)); // unchanged
        }

        @Test
        public void testUpdateColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.updateColumn(0, x -> x + "2");
            assertArrayEquals(new String[] { "A2", "C2" }, m.columnCopy(0));
            assertArrayEquals(new String[] { "B", "D" }, m.columnCopy(1)); // unchanged
        }

        // ============ Diagonal Operations Tests ============

        @Test
        public void testGetLU2RD() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            assertArrayEquals(new String[] { "A", "E", "I" }, m.getMainDiagonal());
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            m.setMainDiagonal(new String[] { "X", "Y", "Z" });
            assertEquals("X", m.get(0, 0));
            assertEquals("Y", m.get(1, 1));
            assertEquals("Z", m.get(2, 2));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new String[] { "X" }));
        }

        @Test
        public void testSetLU2RD_arrayTooShort() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new String[] { "X", "Y" }));
        }

        @Test
        public void testUpdateLU2RD() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            m.updateMainDiagonal(x -> x + "1");
            assertEquals("A1", m.get(0, 0));
            assertEquals("E1", m.get(1, 1));
            assertEquals("I1", m.get(2, 2));
            assertEquals("B", m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateLU2RD_nonSquare() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalStateException.class, () -> m.updateMainDiagonal(x -> x + "1"));
        }

        @Test
        public void testGetRU2LD() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            assertArrayEquals(new String[] { "C", "E", "G" }, m.getAntiDiagonal());
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            m.setAntiDiagonal(new String[] { "X", "Y", "Z" });
            assertEquals("X", m.get(0, 2));
            assertEquals("Y", m.get(1, 1));
            assertEquals("Z", m.get(2, 0));
        }

        @Test
        public void testSetRU2LD_nonSquare() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalStateException.class, () -> m.setAntiDiagonal(new String[] { "X" }));
        }

        @Test
        public void testSetRU2LD_arrayTooShort() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new String[] { "X", "Y" }));
        }

        @Test
        public void testUpdateRU2LD() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            m.updateAntiDiagonal(x -> x + "2");
            assertEquals("C2", m.get(0, 2));
            assertEquals("E2", m.get(1, 1));
            assertEquals("G2", m.get(2, 0));
            assertEquals("B", m.get(0, 1)); // unchanged
        }

        @Test
        public void testUpdateRU2LD_nonSquare() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(x -> x + "2"));
        }

        // ============ Transformation Tests ============

        @Test
        public void testUpdateAll() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.updateAll(x -> x + "1");
            assertEquals("A1", m.get(0, 0));
            assertEquals("B1", m.get(0, 1));
            assertEquals("C1", m.get(1, 0));
            assertEquals("D1", m.get(1, 1));
        }

        @Test
        public void testUpdateAll_withIndices() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 0, 0 }, { 0, 0 } });
            m.updateAll((i, j) -> i * 10 + j);
            assertEquals(Integer.valueOf(0), m.get(0, 0));
            assertEquals(Integer.valueOf(1), m.get(0, 1));
            assertEquals(Integer.valueOf(10), m.get(1, 0));
            assertEquals(Integer.valueOf(11), m.get(1, 1));
        }

        @Test
        public void testReplaceIf() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "A" }, { "C", "A", "D" } });
            m.replaceIf(x -> "A".equals(x), "X");
            assertEquals("X", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("X", m.get(0, 2));
            assertEquals("C", m.get(1, 0));
            assertEquals("X", m.get(1, 1));
            assertEquals("D", m.get(1, 2));
        }

        @Test
        public void testReplaceIf_withIndices() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            m.replaceIf((i, j) -> i == j, "X"); // Replace diagonal
            assertEquals("X", m.get(0, 0));
            assertEquals("X", m.get(1, 1));
            assertEquals("X", m.get(2, 2));
            assertEquals("B", m.get(0, 1)); // unchanged
        }

        @Test
        public void testMap() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> result = m.map(x -> x + "1");
            assertEquals("A1", result.get(0, 0));
            assertEquals("B1", result.get(0, 1));
            assertEquals("C1", result.get(1, 0));
            assertEquals("D1", result.get(1, 1));

            // Original unchanged
            assertEquals("A", m.get(0, 0));
        }

        @Test
        public void testMap_withTypeChange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<Integer> result = m.map(x -> x.charAt(0) - 'A', Integer.class);
            assertEquals(Integer.valueOf(0), result.get(0, 0));
            assertEquals(Integer.valueOf(1), result.get(0, 1));
            assertEquals(Integer.valueOf(2), result.get(1, 0));
            assertEquals(Integer.valueOf(3), result.get(1, 1));
        }

        @Test
        public void testMapToBoolean() {
            Matrix<String> m = Matrix.of(new String[][] { { "true", "false" }, { "false", "true" } });
            BooleanMatrix result = m.mapToBoolean(x -> Boolean.parseBoolean(x));
            assertTrue(result.get(0, 0));
            assertFalse(result.get(0, 1));
            assertFalse(result.get(1, 0));
            assertTrue(result.get(1, 1));
        }

        @Test
        public void testMapToByte() {
            Matrix<String> m = Matrix.of(new String[][] { { "1", "2" }, { "3", "4" } });
            ByteMatrix result = m.mapToByte(x -> Byte.parseByte(x));
            assertEquals((byte) 1, result.get(0, 0));
            assertEquals((byte) 2, result.get(0, 1));
            assertEquals((byte) 3, result.get(1, 0));
            assertEquals((byte) 4, result.get(1, 1));
        }

        @Test
        public void testMapToChar() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            CharMatrix result = m.mapToChar(x -> x.charAt(0));
            assertEquals('A', result.get(0, 0));
            assertEquals('B', result.get(0, 1));
            assertEquals('C', result.get(1, 0));
            assertEquals('D', result.get(1, 1));
        }

        @Test
        public void testMapToShort() {
            Matrix<String> m = Matrix.of(new String[][] { { "10", "20" }, { "30", "40" } });
            ShortMatrix result = m.mapToShort(x -> Short.parseShort(x));
            assertEquals((short) 10, result.get(0, 0));
            assertEquals((short) 20, result.get(0, 1));
            assertEquals((short) 30, result.get(1, 0));
            assertEquals((short) 40, result.get(1, 1));
        }

        @Test
        public void testMapToInt() {
            Matrix<String> m = Matrix.of(new String[][] { { "1", "2" }, { "3", "4" } });
            IntMatrix result = m.mapToInt(x -> Integer.parseInt(x));
            assertEquals(1, result.get(0, 0));
            assertEquals(2, result.get(0, 1));
            assertEquals(3, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void testMapToLong() {
            Matrix<String> m = Matrix.of(new String[][] { { "100", "200" }, { "300", "400" } });
            LongMatrix result = m.mapToLong(x -> Long.parseLong(x));
            assertEquals(100L, result.get(0, 0));
            assertEquals(200L, result.get(0, 1));
            assertEquals(300L, result.get(1, 0));
            assertEquals(400L, result.get(1, 1));
        }

        @Test
        public void testMapToFloat() {
            Matrix<String> m = Matrix.of(new String[][] { { "1.5", "2.5" }, { "3.5", "4.5" } });
            FloatMatrix result = m.mapToFloat(x -> Float.parseFloat(x));
            assertEquals(1.5f, result.get(0, 0), 0.001f);
            assertEquals(2.5f, result.get(0, 1), 0.001f);
            assertEquals(3.5f, result.get(1, 0), 0.001f);
            assertEquals(4.5f, result.get(1, 1), 0.001f);
        }

        @Test
        public void testMapToDouble() {
            Matrix<String> m = Matrix.of(new String[][] { { "1.5", "2.5" }, { "3.5", "4.5" } });
            DoubleMatrix result = m.mapToDouble(x -> Double.parseDouble(x));
            assertEquals(1.5, result.get(0, 0), 0.001);
            assertEquals(2.5, result.get(0, 1), 0.001);
            assertEquals(3.5, result.get(1, 0), 0.001);
            assertEquals(4.5, result.get(1, 1), 0.001);
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_withValue() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.fill("X");
            for (int i = 0; i < m.rowCount(); i++) {
                for (int j = 0; j < m.columnCount(); j++) {
                    assertEquals("X", m.get(i, j));
                }
            }
        }

        @Test
        public void testFill_withArray() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "A", "A" }, { "A", "A", "A" }, { "A", "A", "A" } });
            String[][] patch = { { "X", "Y" }, { "Z", "W" } };
            m.copyFrom(patch);
            assertEquals("X", m.get(0, 0));
            assertEquals("Y", m.get(0, 1));
            assertEquals("Z", m.get(1, 0));
            assertEquals("W", m.get(1, 1));
            assertEquals("A", m.get(2, 2)); // unchanged
        }

        @Test
        public void testFill_withArrayAtPosition() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "A", "A" }, { "A", "A", "A" }, { "A", "A", "A" } });
            String[][] patch = { { "X", "Y" }, { "Z", "W" } };
            m.copyFrom(1, 1, patch);
            assertEquals("A", m.get(0, 0)); // unchanged
            assertEquals("X", m.get(1, 1));
            assertEquals("Y", m.get(1, 2));
            assertEquals("Z", m.get(2, 1));
            assertEquals("W", m.get(2, 2));
        }

        @Test
        public void testFill_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[][] patch = { { "X", "Y" }, { "Z", "W" } };
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals("A", copy.get(0, 0));

            // Modify copy shouldn't affect original
            copy.set(0, 0, "X");
            assertEquals("A", m.get(0, 0));
            assertEquals("X", copy.get(0, 0));
        }

        @Test
        public void testCopy_withRowRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            Matrix<String> subset = m.copy(1, 3);
            assertEquals(2, subset.rowCount());
            assertEquals(3, subset.columnCount());
            assertEquals("D", subset.get(0, 0));
            assertEquals("I", subset.get(1, 2));
        }

        @Test
        public void testCopy_withRowRange_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
        }

        @Test
        public void testCopy_withFullRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            Matrix<String> submatrix = m.copy(0, 2, 1, 3);
            assertEquals(2, submatrix.rowCount());
            assertEquals(2, submatrix.columnCount());
            assertEquals("B", submatrix.get(0, 0));
            assertEquals("F", submatrix.get(1, 1));
        }

        @Test
        public void testCopy_withFullRange_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_larger() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> extended = m.resize(4, 4);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals("A", extended.get(0, 0));
            assertEquals("D", extended.get(1, 1));
            assertNull(extended.get(3, 3)); // new cells are null
        }

        @Test
        public void testExtend_smaller() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            Matrix<String> truncated = m.resize(2, 2);
            assertEquals(2, truncated.rowCount());
            assertEquals(2, truncated.columnCount());
            assertEquals("A", truncated.get(0, 0));
            assertEquals("E", truncated.get(1, 1));
        }

        @Test
        public void testExtend_withDefaultValue() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> extended = m.resize(3, 3, "X");
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals("A", extended.get(0, 0));
            assertEquals("X", extended.get(2, 2)); // new cell
        }

        @Test
        public void testExtend_withNegativeDimensions() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 3, "X"));
            assertThrows(IllegalArgumentException.class, () -> m.resize(3, -1, "X"));
        }

        @Test
        public void testExtend_directional() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            m.println();
            Matrix<String> extended = m.extend(1, 1, 2, 2);
            assertEquals(5, extended.rowCount()); // 1 + 3 + 1
            assertEquals(7, extended.columnCount()); // 2 + 3 + 2

            extended.println();

            // Original values at offset position
            assertEquals("A", extended.get(1, 2));
            assertEquals("F", extended.get(2, 4));

            // New cells are null
            assertNull(extended.get(0, 0));
        }

        @Test
        public void testExtend_directionalWithDefault() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            Matrix<String> extended = m.extend(1, 1, 1, 1, "X");
            assertEquals(5, extended.rowCount());
            assertEquals(5, extended.columnCount());

            // Check original values
            assertEquals("A", extended.get(1, 1));

            // Check new values
            assertEquals("X", extended.get(0, 0));
            assertEquals("X", extended.get(4, 4));
        }

        @Test
        public void testExtend_directionalWithNegativeValues() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, "X"));
        }

        // ============ Reverse/Flip Tests ============

        @Test
        public void testReverseH() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            m.flipInPlaceHorizontally();
            assertEquals("C", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("A", m.get(0, 2));
            assertEquals("F", m.get(1, 0));
        }

        @Test
        public void testReverseV() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            m.flipInPlaceVertically();
            assertEquals("E", m.get(0, 0));
            assertEquals("F", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
            assertEquals("A", m.get(2, 0));
        }

        @Test
        public void testFlipH() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> flipped = m.flipHorizontally();
            assertEquals("C", flipped.get(0, 0));
            assertEquals("B", flipped.get(0, 1));
            assertEquals("A", flipped.get(0, 2));

            // Original unchanged
            assertEquals("A", m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            Matrix<String> flipped = m.flipVertically();
            assertEquals("E", flipped.get(0, 0));
            assertEquals("C", flipped.get(1, 0));
            assertEquals("A", flipped.get(2, 0));

            // Original unchanged
            assertEquals("A", m.get(0, 0));
        }

        // ============ Rotation Tests ============

        @Test
        public void testRotate90() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals("C", rotated.get(0, 0));
            assertEquals("A", rotated.get(0, 1));
            assertEquals("D", rotated.get(1, 0));
            assertEquals("B", rotated.get(1, 1));
        }

        @Test
        public void testRotate180() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> rotated = m.rotate180();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals("D", rotated.get(0, 0));
            assertEquals("C", rotated.get(0, 1));
            assertEquals("B", rotated.get(1, 0));
            assertEquals("A", rotated.get(1, 1));
        }

        @Test
        public void testRotate270() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals("B", rotated.get(0, 0));
            assertEquals("D", rotated.get(0, 1));
            assertEquals("A", rotated.get(1, 0));
            assertEquals("C", rotated.get(1, 1));
        }

        // ============ Transpose Tests ============

        @Test
        public void testTranspose() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals("A", transposed.get(0, 0));
            assertEquals("D", transposed.get(0, 1));
            assertEquals("B", transposed.get(1, 0));
            assertEquals("E", transposed.get(1, 1));
            assertEquals("C", transposed.get(2, 0));
            assertEquals("F", transposed.get(2, 1));
        }

        @Test
        public void testTranspose_square() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals("A", transposed.get(0, 0));
            assertEquals("C", transposed.get(0, 1));
            assertEquals("B", transposed.get(1, 0));
            assertEquals("D", transposed.get(1, 1));
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            Matrix<String> reshaped = m.reshape(1, 9);
            assertEquals(1, reshaped.rowCount());
            assertEquals(9, reshaped.columnCount());
            String[] expected = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };
            for (int i = 0; i < 9; i++) {
                assertEquals(expected[i], reshaped.get(0, i));
            }
        }

        @Test
        public void testReshape_back() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            Matrix<String> reshaped = m.reshape(1, 9);
            Matrix<String> reshapedBack = reshaped.reshape(3, 3);
            assertEquals(m, reshapedBack);
        }

        @Test
        public void testReshape_empty() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            Matrix<String> reshaped = empty.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        // ============ Repeat Tests ============

        @Test
        public void testRepelem() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            Matrix<String> repeated = m.repeatElements(2, 3);
            assertEquals(2, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals("A", repeated.get(0, 0));
            assertEquals("A", repeated.get(0, 1));
            assertEquals("A", repeated.get(0, 2));
            assertEquals("B", repeated.get(0, 3));
            assertEquals("B", repeated.get(0, 4));
            assertEquals("B", repeated.get(0, 5));
            assertEquals("A", repeated.get(1, 0)); // second row same as first
        }

        @Test
        public void testRepelem_invalidArguments() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> repeated = m.repeatMatrix(2, 3);
            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals("A", repeated.get(0, 0));
            assertEquals("B", repeated.get(0, 1));
            assertEquals("A", repeated.get(0, 2)); // repeat starts
            assertEquals("B", repeated.get(0, 3));

            assertEquals("C", repeated.get(1, 0));
            assertEquals("D", repeated.get(1, 1));

            // Check vertical repeat
            assertEquals("A", repeated.get(2, 0)); // vertical repeat starts
            assertEquals("B", repeated.get(2, 1));
        }

        @Test
        public void testRepmat_invalidArguments() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            List<String> flat = m.flatten();
            assertEquals(9, flat.size());
            List<String> expected = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I");
            assertEquals(expected, flat);
        }

        @Test
        public void testFlatten_empty() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            List<String> flat = empty.flatten();
            assertTrue(flat.isEmpty());
        }

        @Test
        public void testFlatOp() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            List<Integer> rowLengths = new ArrayList<>();
            m.applyOnFlattened(row -> {
                rowLengths.add(row.length);
            });
            assertEquals(1, rowLengths.size());
            assertEquals(9, rowLengths.get(0).intValue());
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "G", "H", "I" }, { "J", "K", "L" } });
            Matrix<String> stacked = m1.stackVertically(m2);

            assertEquals(4, stacked.rowCount());
            assertEquals(3, stacked.columnCount());
            assertEquals("A", stacked.get(0, 0));
            assertEquals("G", stacked.get(2, 0));
            assertEquals("L", stacked.get(3, 2));
        }

        @Test
        public void testVstack_differentColumnCounts() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "C", "D", "E" } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "E", "F" }, { "G", "H" } });
            Matrix<String> stacked = m1.stackHorizontally(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertEquals("A", stacked.get(0, 0));
            assertEquals("E", stacked.get(0, 2));
            assertEquals("H", stacked.get(1, 3));
        }

        @Test
        public void testHstack_differentRowCounts() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "E", "F" } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
            Matrix<Integer> result = m1.zipWith(m2, (a, b) -> a + b);

            assertEquals(Integer.valueOf(11), result.get(0, 0));
            assertEquals(Integer.valueOf(22), result.get(0, 1));
            assertEquals(Integer.valueOf(33), result.get(1, 0));
            assertEquals(Integer.valueOf(44), result.get(1, 1));
        }

        @Test
        public void testZipWith_withTypeChange() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
            Matrix<String> result = m1.zipWith(m2, (a, b) -> a + "+" + b, String.class);

            assertEquals("1+10", result.get(0, 0));
            assertEquals("2+20", result.get(0, 1));
            assertEquals("3+30", result.get(1, 0));
            assertEquals("4+40", result.get(1, 1));
        }

        @Test
        public void testZipWith_differentShapes() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20, 30 } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a + b));
        }

        @Test
        public void testZipWith_threeMatrices() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200 }, { 300, 400 } });
            Matrix<Integer> result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);

            assertEquals(Integer.valueOf(111), result.get(0, 0));
            assertEquals(Integer.valueOf(222), result.get(0, 1));
            assertEquals(Integer.valueOf(333), result.get(1, 0));
            assertEquals(Integer.valueOf(444), result.get(1, 1));
        }

        @Test
        public void testZipWith_threeMatrices_withTypeChange() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200 }, { 300, 400 } });
            Matrix<String> result = m1.zipWith(m2, m3, (a, b, c) -> a + "+" + b + "+" + c, String.class);

            assertEquals("1+10+100", result.get(0, 0));
            assertEquals("2+20+200", result.get(0, 1));
            assertEquals("3+30+300", result.get(1, 0));
            assertEquals("4+40+400", result.get(1, 1));
        }

        @Test
        public void testZipWith_threeMatrices_differentShapes() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200, 300 } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> a + b + c));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            List<String> diagonal = m.streamMainDiagonal().toList();
            assertEquals(3, diagonal.size());
            assertEquals("A", diagonal.get(0));
            assertEquals("E", diagonal.get(1));
            assertEquals("I", diagonal.get(2));
        }

        @Test
        public void testStreamLU2RD_empty() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            assertEquals(0, empty.streamMainDiagonal().count());
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            Matrix<String> nonSquare = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
        }

        @Test
        public void testStreamRU2LD() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            List<String> antiDiagonal = m.streamAntiDiagonal().toList();
            assertEquals(3, antiDiagonal.size());
            assertEquals("C", antiDiagonal.get(0));
            assertEquals("E", antiDiagonal.get(1));
            assertEquals("G", antiDiagonal.get(2));
        }

        @Test
        public void testStreamRU2LD_empty() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            assertEquals(0, empty.streamAntiDiagonal().count());
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            Matrix<String> nonSquare = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamAntiDiagonal());
        }

        @Test
        public void testStreamH() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<String> all = m.streamHorizontal().toList();
            assertEquals(6, all.size());
            assertEquals("A", all.get(0));
            assertEquals("B", all.get(1));
            assertEquals("C", all.get(2));
            assertEquals("D", all.get(3));
        }

        @Test
        public void testStreamH_empty() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            assertEquals(0, empty.streamHorizontal().count());
        }

        @Test
        public void testStreamH_withRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<String> row1 = m.streamHorizontal(1).toList();
            assertEquals(3, row1.size());
            assertEquals("D", row1.get(0));
            assertEquals("E", row1.get(1));
            assertEquals("F", row1.get(2));
        }

        @Test
        public void testStreamH_withRow_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2));
        }

        @Test
        public void testStreamH_withRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            List<String> rows = m.streamHorizontal(1, 3).toList();
            assertEquals(6, rows.size());
        }

        @Test
        public void testStreamH_withRange_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2, 1));
        }

        @Test
        public void testStreamV() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<String> all = m.streamVertical().toList();
            assertEquals(6, all.size());
            assertEquals("A", all.get(0));
            assertEquals("D", all.get(1));
            assertEquals("B", all.get(2));
        }

        @Test
        public void testStreamV_empty() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            assertEquals(0, empty.streamVertical().count());
        }

        @Test
        public void testStreamV_withColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<String> col1 = m.streamVertical(1).toList();
            assertEquals(2, col1.size());
            assertEquals("B", col1.get(0));
            assertEquals("E", col1.get(1));
        }

        @Test
        public void testStreamV_withColumn_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2));
        }

        @Test
        public void testStreamV_withRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            List<String> columnCount = m.streamVertical(1, 3).toList();
            assertEquals(6, columnCount.size());
        }

        @Test
        public void testStreamV_withRange_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2, 1));
        }

        @Test
        public void testStreamR() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<Stream<String>> rows = m.streamRows().toList();
            assertEquals(2, rows.size());
            List<String> row0 = rows.get(0).toList();
            assertEquals(3, row0.size());
            assertEquals("A", row0.get(0));
        }

        @Test
        public void testStreamR_empty() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            assertEquals(0, empty.streamRows().count());
        }

        @Test
        public void testStreamR_withRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            List<Stream<String>> rows = m.streamRows(1, 3).toList();
            assertEquals(2, rows.size());
        }

        @Test
        public void testStreamR_withRange_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(0, 3));
        }

        @Test
        public void testStreamC() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<Stream<String>> columnCount = m.streamColumns().toList();
            assertEquals(3, columnCount.size());
        }

        @Test
        public void testStreamC_empty() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            assertEquals(0, empty.streamColumns().count());
        }

        @Test
        public void testStreamC_withRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            List<Stream<String>> columnCount = m.streamColumns(1, 3).toList();
            assertEquals(2, columnCount.size());
        }

        @Test
        public void testStreamC_withRange_outOfBounds() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(0, 3));
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<String> values = new ArrayList<>();
            m.forEach(v -> values.add(v));
            assertEquals(4, values.size());
            assertTrue(values.contains("A"));
            assertTrue(values.contains("D"));
        }

        @Test
        public void testForEach_withRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            List<String> values = new ArrayList<>();
            m.forEach(1, 3, 1, 3, v -> values.add(v));
            assertEquals(4, values.size());
            assertTrue(values.contains("E"));
            assertTrue(values.contains("I"));
            assertFalse(values.contains("A"));
        }

        // ============ Object Methods Tests ============

        @Test
        public void testHashCode() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m3 = Matrix.of(new String[][] { { "A", "B" }, { "X", "D" } });

            assertEquals(m1.hashCode(), m2.hashCode());
            assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
        }

        @Test
        public void testEquals() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m3 = Matrix.of(new String[][] { { "A", "B" }, { "X", "D" } });
            Matrix<String> m4 = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });

            assertTrue(m1.equals(m1)); // Same object
            assertTrue(m1.equals(m2)); // Same values
            assertFalse(m1.equals(m3)); // Different values
            assertFalse(m1.equals(m4)); // Different dimensions
            assertFalse(m1.equals(null));
            assertFalse(m1.equals("not a matrix"));
        }

        @Test
        public void testToString() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("A") || str.contains("["));
        }

        // ============ Edge Case Tests ============

        @Test
        public void testNullHandling() {
            Matrix<String> m = Matrix.of(new String[][] { { null, "B" }, { "C", null } });

            assertNull(m.get(0, 0));
            assertEquals("B", m.get(0, 1));

            // Test operations with nulls
            m.updateAll(x -> x == null ? "NULL" : x);
            assertEquals("NULL", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
        }

        @Test
        public void testEmptyMatrixOperations() {
            Matrix<String> empty = Matrix.of(new String[0][0]);

            assertTrue(empty.flatten().isEmpty());
            assertEquals(0, empty.copy().rowCount);
            assertEquals(empty, empty.transpose());
            assertEquals(empty, empty.rotate90());

            Matrix<String> extended = empty.resize(2, 2, "X");
            assertEquals(2, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals("X", extended.get(0, 0));
        }

        @Test
        public void testSingleElementMatrix() {
            Matrix<String> m = Matrix.of(new String[][] { { "X" } });

            assertEquals("X", m.get(0, 0));
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());

            Matrix<String> transposed = m.transpose();
            assertEquals("X", transposed.get(0, 0));
        }

        @Test
        public void testLargeMatrix() {
            Integer[][] data = new Integer[100][100];
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    data[i][j] = i * 100 + j;
                }
            }
            Matrix<Integer> m = Matrix.of(data);

            assertEquals(100, m.rowCount());
            assertEquals(100, m.columnCount());
            assertEquals(Integer.valueOf(9999), m.get(99, 99));
        }

        @Test
        public void testGenericTypeWithCustomObject() {
            class CustomObject {
                String value;

                CustomObject(String value) {
                    this.value = value;
                }

                @Override
                public String toString() {
                    return value;
                }
            }

            CustomObject[][] data = { { new CustomObject("A"), new CustomObject("B") }, { new CustomObject("C"), new CustomObject("D") } };
            Matrix<CustomObject> m = Matrix.of(data);

            assertEquals("A", m.get(0, 0).value);
            assertEquals("D", m.get(1, 1).value);
        }

        @Test
        public void testGenericTypeWithList() {
            List<String> list1 = Arrays.asList("a", "b");
            List<String> list2 = Arrays.asList("c", "d");
            List<String> list3 = Arrays.asList("e", "f");
            List<String> list4 = Arrays.asList("g", "h");

            @SuppressWarnings("unchecked")
            Matrix<List<String>> m = Matrix.of(new List[] { list1, list2 }, new List[] { list3, list4 });

            assertEquals(list1, m.get(0, 0));
            assertEquals(list4, m.get(1, 1));
        }

        @Test
        public void testImmutabilityOfDimensions() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            int originalRows = m.rowCount();
            int originalCols = m.columnCount();

            // Operations that return new matrices
            m.transpose();
            m.rotate90();
            m.resize(5, 5);

            // Original dimensions should not change
            assertEquals(originalRows, m.rowCount());
            assertEquals(originalCols, m.columnCount());
        }

        @Test
        public void testChainedOperations() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

            Matrix<Integer> result = m.map(x -> x * 2).map(x -> x + 1).transpose().rotate90();

            assertNotNull(result);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
        }

        // ============ Additional Coverage Tests ============

        @Test
        public void testToDatasetH() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<String> columnNames = Arrays.asList("A", "B", "C");
            Dataset dataset = m.toRowDataset(columnNames);

            assertNotNull(dataset);
            assertEquals(3, dataset.columnNames().size());
            assertEquals(2, dataset.size());
            assertEquals(Integer.valueOf(1), dataset.getColumn("A").get(0));
            assertEquals(Integer.valueOf(6), dataset.getColumn("C").get(1));
        }

        @Test
        public void testToDatasetH_wrongColumnCount() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<String> columnNames = Arrays.asList("A", "B");
            assertThrows(IllegalArgumentException.class, () -> m.toRowDataset(columnNames));
        }

        @Test
        public void testToDatasetV() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<String> columnNames = Arrays.asList("Row1", "Row2");
            Dataset dataset = m.toColumnDataset(columnNames);

            assertNotNull(dataset);
            assertEquals(2, dataset.columnNames().size());
            assertEquals(3, dataset.size());
            assertEquals(Integer.valueOf(1), dataset.getColumn("Row1").get(0));
            assertEquals(Integer.valueOf(6), dataset.getColumn("Row2").get(2));
        }

        @Test
        public void testToDatasetV_wrongColumnCount() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<String> columnNames = Arrays.asList("A", "B", "C");
            assertThrows(IllegalArgumentException.class, () -> m.toColumnDataset(columnNames));
        }

        @Test
        public void testExtend_allZeros() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> result = m.extend(0, 0, 0, 0);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("A", result.get(0, 0));
            assertEquals("D", result.get(1, 1));
        }

        @Test
        public void testExtend_withOnlyUp() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> extended = m.extend(2, 0, 0, 0, "X");
            assertEquals(4, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals("X", extended.get(0, 0));
            assertEquals("X", extended.get(1, 0));
            assertEquals("A", extended.get(2, 0));
        }

        @Test
        public void testExtend_withOnlyLeft() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> extended = m.extend(0, 0, 2, 0, "X");
            assertEquals(2, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals("X", extended.get(0, 0));
            assertEquals("X", extended.get(0, 1));
            assertEquals("A", extended.get(0, 2));
        }

        @Test
        public void testExtend_smaller_nonSquare() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> result = m.resize(1, 2);
            assertEquals(1, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("A", result.get(0, 0));
            assertEquals("B", result.get(0, 1));
        }

        @Test
        public void testExtend_largerRows_smallerCols() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> result = m.resize(3, 1, "X");
            assertEquals(3, result.rowCount());
            assertEquals(1, result.columnCount());
            assertEquals("A", result.get(0, 0));
            assertEquals("C", result.get(1, 0));
            assertEquals("X", result.get(2, 0));
        }

        @Test
        public void testReverseH_singleColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A" }, { "B" }, { "C" } });
            m.flipInPlaceHorizontally();
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(1, 0));
            assertEquals("C", m.get(2, 0));
        }

        @Test
        public void testReverseV_singleRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
            m.flipInPlaceVertically();
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("C", m.get(0, 2));
        }

        @Test
        public void testFill_withNullValue() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.fill((String) null);
            assertNull(m.get(0, 0));
            assertNull(m.get(1, 1));
        }

        @Test
        public void testFill_withArray_emptySource() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[][] emptySource = {};
            m.copyFrom(emptySource);
            assertEquals("A", m.get(0, 0));
            assertEquals("D", m.get(1, 1));
        }

        @Test
        public void testFill_withArray_largerSource() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[][] largeSource = { { "X", "Y", "Z" }, { "W", "V", "U" }, { "T", "S", "R" } };
            m.copyFrom(largeSource);
            assertEquals("X", m.get(0, 0));
            assertEquals("Y", m.get(0, 1));
            assertEquals("W", m.get(1, 0));
            assertEquals("V", m.get(1, 1));
        }

        @Test
        public void testFill_atBoundary() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "A", "A" }, { "A", "A", "A" }, { "A", "A", "A" } });
            String[][] patch = { { "X" } };
            m.copyFrom(2, 2, patch);
            assertEquals("A", m.get(0, 0));
            assertEquals("X", m.get(2, 2));
        }

        @Test
        public void testFill_atEdgePosition() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[][] patch = { { "X", "Y" } };
            m.copyFrom(1, 0, patch);
            assertEquals("A", m.get(0, 0));
            assertEquals("X", m.get(1, 0));
            assertEquals("Y", m.get(1, 1));
        }

        @Test
        public void testReshape_largerSize() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> reshaped = m.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals("A", reshaped.get(0, 0));
            assertEquals("D", reshaped.get(1, 0));
            assertNull(reshaped.get(1, 2));
        }

        @Test
        public void testReshape_singleRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C", "D" } });
            Matrix<String> reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals("A", reshaped.get(0, 0));
            assertEquals("B", reshaped.get(0, 1));
            assertEquals("C", reshaped.get(1, 0));
            assertEquals("D", reshaped.get(1, 1));
        }

        @Test
        public void testUpdateRow_withNullFunction() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.updateRow(0, x -> null);
            assertNull(m.get(0, 0));
            assertNull(m.get(0, 1));
            assertEquals("C", m.get(1, 0));
        }

        @Test
        public void testUpdateColumn_withNullFunction() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.updateColumn(0, x -> null);
            assertNull(m.get(0, 0));
            assertNull(m.get(1, 0));
            assertEquals("B", m.get(0, 1));
        }

        @Test
        public void testLength_withNullArray() {
            Matrix<String> m = Matrix.of(new String[][] { { "A" } });
            assertEquals(0, m.length(null));
        }

        @Test
        public void testAdjacentPoints_withPoint() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            Point p = Point.of(1, 1);
            assertEquals("E", m.get(p));

            // Test setting with point
            m.set(p, "X");
            assertEquals("X", m.get(1, 1));
        }

        @Test
        public void testRotate90_nonSquare_rowsLessThanCols() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
            Matrix<String> rotated = m.rotate90();
            assertEquals(3, rotated.rowCount());
            assertEquals(1, rotated.columnCount());
            assertEquals("A", rotated.get(0, 0));
            assertEquals("B", rotated.get(1, 0));
            assertEquals("C", rotated.get(2, 0));
        }

        @Test
        public void testRotate270_nonSquare_rowsLessThanCols() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
            Matrix<String> rotated = m.rotate270();
            assertEquals(3, rotated.rowCount());
            assertEquals(1, rotated.columnCount());
            assertEquals("C", rotated.get(0, 0));
            assertEquals("B", rotated.get(1, 0));
            assertEquals("A", rotated.get(2, 0));
        }

        @Test
        public void testTranspose_nonSquare_rowsLessThanCols() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
            Matrix<String> transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(1, transposed.columnCount());
            assertEquals("A", transposed.get(0, 0));
            assertEquals("B", transposed.get(1, 0));
            assertEquals("C", transposed.get(2, 0));
        }

        @Test
        public void testMap_withIndices() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.updateAll((i, j) -> m.get(i, j) + i + j);
            assertEquals("A00", m.get(0, 0));
            assertEquals("B01", m.get(0, 1));
            assertEquals("C10", m.get(1, 0));
            assertEquals("D11", m.get(1, 1));
        }

        @Test
        public void testReplaceIf_noneMatch() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.replaceIf(x -> "Z".equals(x), "X");
            assertEquals("A", m.get(0, 0));
            assertEquals("D", m.get(1, 1));
        }

        @Test
        public void testReplaceIf_withIndices_noneMatch() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.replaceIf((i, j) -> i > 10, "X");
            assertEquals("A", m.get(0, 0));
            assertEquals("D", m.get(1, 1));
        }

        @Test
        public void testStreamH_singleRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
            List<String> result = m.streamHorizontal(0).toList();
            assertEquals(3, result.size());
            assertEquals("A", result.get(0));
            assertEquals("C", result.get(2));
        }

        @Test
        public void testStreamV_singleColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A" }, { "B" }, { "C" } });
            List<String> result = m.streamVertical(0).toList();
            assertEquals(3, result.size());
            assertEquals("A", result.get(0));
            assertEquals("C", result.get(2));
        }

        @Test
        public void testForEach_fullMatrix() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            List<Integer> collected = new ArrayList<>();
            m.forEach(0, 2, 0, 2, (Throwables.Consumer<Integer, RuntimeException>) collected::add);
            assertEquals(4, collected.size());
            assertTrue(collected.contains(1));
            assertTrue(collected.contains(4));
        }

        @Test
        public void testForEach_emptyRange() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            List<Integer> collected = new ArrayList<>();
            m.forEach(0, 0, 0, 0, (Throwables.Consumer<Integer, RuntimeException>) collected::add);
            assertEquals(0, collected.size());
        }

        @Test
        public void testVstack_withSameMatrix() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            Matrix<String> result = m.stackVertically(m);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("A", result.get(0, 0));
            assertEquals("A", result.get(1, 0));
        }

        @Test
        public void testHstack_withSameMatrix() {
            Matrix<String> m = Matrix.of(new String[][] { { "A" }, { "B" } });
            Matrix<String> result = m.stackHorizontally(m);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals("A", result.get(0, 0));
            assertEquals("A", result.get(0, 1));
        }

        @Test
        public void testCopy_singleRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
            Matrix<String> copy = m.copy(0, 1);
            assertEquals(1, copy.rowCount());
            assertEquals(3, copy.columnCount());
            assertEquals("A", copy.get(0, 0));
        }

        @Test
        public void testCopy_singleElement() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> copy = m.copy(0, 1, 0, 1);
            assertEquals(1, copy.rowCount());
            assertEquals(1, copy.columnCount());
            assertEquals("A", copy.get(0, 0));
        }

        @Test
        public void testFlatOp_emptyMatrix() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            List<Integer> lengths = new ArrayList<>();
            empty.applyOnFlattened(row -> lengths.add(row.length));
            assertEquals(0, lengths.size());
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for Matrix class covering all public methods.
     */
    @Tag("2510")
    class Matrix2510Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            String[][] arr = { { "A", "B" }, { "C", "D" } };
            Matrix<String> m = new Matrix<>(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals("A", m.get(0, 0));
            assertEquals("D", m.get(1, 1));
        }

        @Test
        public void testConstructor_withEmptyArray() {
            Matrix<String> m = new Matrix<>(new String[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withSingleElement() {
            Matrix<Integer> m = new Matrix<>(new Integer[][] { { 42 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42, m.get(0, 0));
        }

        @Test
        public void testConstructor_withNullElements() {
            Matrix<String> m = new Matrix<>(new String[][] { { null, "B" }, { "C", null } });
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertNull(m.get(0, 0));
            assertEquals("B", m.get(0, 1));
        }

        // ============ Factory Method Tests - of() ============

        @Test
        public void testOf_withValidArray() {
            String[][] arr = { { "A", "B" }, { "C", "D" } };
            Matrix<String> m = Matrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals("A", m.get(0, 0));
        }

        @Test
        public void testOf_withEmptyArray() {
            Matrix<String> m = Matrix.of(new String[0][0]);
            assertTrue(m.isEmpty());
        }

        // ============ Factory Method Tests - repeat() ============

        // ============ Factory Method Tests - diagonal ============

        @Test
        public void testDiagonalLU2RD() {
            Integer[] diag = { 1, 2, 3 };
            Matrix<Integer> m = Matrix.mainDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
            assertNull(m.get(0, 1));
            assertNull(m.get(1, 0));
        }

        @Test
        public void testDiagonalLU2RD_emptyArray() {
            Matrix<String> m = Matrix.mainDiagonal(new String[0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testDiagonalRU2LD() {
            Integer[] diag = { 1, 2, 3 };
            Matrix<Integer> m = Matrix.antiDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 2));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 0));
            assertNull(m.get(0, 0));
            assertNull(m.get(2, 2));
        }

        @Test
        public void testDiagonalRU2LD_emptyArray() {
            Matrix<String> m = Matrix.antiDiagonal(new String[0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testDiagonal_bothDiagonals() {
            Integer[] lu2rd = { 1, 2, 3 };
            Integer[] ru2ld = { 7, 8, 9 };
            Matrix<Integer> m = Matrix.diagonals(lu2rd, ru2ld);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            // LU2RD diagonal
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
            // RU2LD diagonal
            assertEquals(7, m.get(0, 2));
            assertEquals(9, m.get(2, 0));
            // LU2RD takes precedence at center
            assertEquals(2, m.get(1, 1));
        }

        @Test
        public void testDiagonal_onlyLU2RD() {
            Integer[] lu2rd = { 1, 2 };
            Matrix<Integer> m = Matrix.diagonals(lu2rd, null);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
        }

        @Test
        public void testDiagonal_onlyRU2LD() {
            Integer[] ru2ld = { 1, 2 };
            Matrix<Integer> m = Matrix.diagonals(null, ru2ld);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1, m.get(0, 1));
            assertEquals(2, m.get(1, 0));
        }

        @Test
        public void testDiagonal_bothNull() {
            assertThrows(IllegalArgumentException.class, () -> Matrix.diagonals(null, null));
        }

        @Test
        public void testDiagonal_differentLengths() {
            Integer[] lu2rd = { 1, 2, 3 };
            Integer[] ru2ld = { 7, 8 };
            assertThrows(IllegalArgumentException.class, () -> Matrix.diagonals(lu2rd, ru2ld));
        }

        // ============ Basic Access Methods ============

        @Test
        public void testComponentType() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" } });
            assertEquals(String.class, m.componentType());
        }

        @Test
        public void testComponentType_integer() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 } });
            assertEquals(Integer.class, m.componentType());
        }

        @Test
        public void testGet_validIndices() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
            assertEquals("D", m.get(1, 1));
        }

        @Test
        public void testGet_withPoint() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Point p = Point.of(1, 0);
            assertEquals("C", m.get(p));
        }

        @Test
        public void testSet_validIndices() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.set(0, 0, "X");
            m.set(1, 1, "Y");
            assertEquals("X", m.get(0, 0));
            assertEquals("Y", m.get(1, 1));
        }

        @Test
        public void testSet_withPoint() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Point p = Point.of(1, 0);
            m.set(p, "Z");
            assertEquals("Z", m.get(1, 0));
        }

        @Test
        public void testSet_nullValue() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.set(0, 0, null);
            assertNull(m.get(0, 0));
        }

        // ============ Adjacent Element Methods ============

        @Test
        public void testUpOf_validPosition() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals("A", up.get());
        }

        @Test
        public void testUpOf_topEdge() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> up = m.above(0, 0);
            assertFalse(up.isPresent());
        }

        @Test
        public void testDownOf_validPosition() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals("C", down.get());
        }

        @Test
        public void testDownOf_bottomEdge() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> down = m.below(1, 0);
            assertFalse(down.isPresent());
        }

        @Test
        public void testLeftOf_validPosition() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals("A", left.get());
        }

        @Test
        public void testLeftOf_leftEdge() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> left = m.left(0, 0);
            assertFalse(left.isPresent());
        }

        @Test
        public void testRightOf_validPosition() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals("B", right.get());
        }

        @Test
        public void testRightOf_rightEdge() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> right = m.right(0, 1);
            assertFalse(right.isPresent());
        }

        // ============ Row and Column Access ============

        @Test
        public void testRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[] row = m.rowView(0);
            assertArrayEquals(new String[] { "A", "B" }, row);
        }

        @Test
        public void testRow_lastRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[] row = m.rowView(1);
            assertArrayEquals(new String[] { "C", "D" }, row);
        }

        @Test
        public void testRow_invalidIndex() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(1));
        }

        @Test
        public void testColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[] col = m.columnCopy(0);
            assertArrayEquals(new String[] { "A", "C" }, col);
        }

        @Test
        public void testColumn_lastColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[] col = m.columnCopy(1);
            assertArrayEquals(new String[] { "B", "D" }, col);
        }

        @Test
        public void testColumn_invalidIndex() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.setRow(0, new String[] { "X", "Y" });
            assertArrayEquals(new String[] { "X", "Y" }, m.rowView(0));
            assertArrayEquals(new String[] { "C", "D" }, m.rowView(1));
        }

        @Test
        public void testSetRow_wrongLength() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "X" }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "X", "Y", "Z" }));
        }

        @Test
        public void testSetColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.setColumn(0, new String[] { "X", "Y" });
            assertArrayEquals(new String[] { "X", "Y" }, m.columnCopy(0));
            assertArrayEquals(new String[] { "B", "D" }, m.columnCopy(1));
        }

        @Test
        public void testSetColumn_wrongLength() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "X" }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "X", "Y", "Z" }));
        }

        @Test
        public void testUpdateRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            m.updateRow(0, String::toUpperCase);
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("c", m.get(1, 0));
        }

        @Test
        public void testUpdateColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            m.updateColumn(0, String::toUpperCase);
            assertEquals("A", m.get(0, 0));
            assertEquals("b", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
        }

        // ============ Diagonal Methods ============

        @Test
        public void testGetLU2RD() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            Integer[] diag = m.getMainDiagonal();
            assertArrayEquals(new Integer[] { 1, 5, 9 }, diag);
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.setMainDiagonal(new Integer[] { 10, 20, 30 });
            assertEquals(10, m.get(0, 0));
            assertEquals(20, m.get(1, 1));
            assertEquals(30, m.get(2, 2));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new Integer[] { 10, 20 }));
        }

        @Test
        public void testSetLU2RD_wrongLength() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new Integer[] { 10 }));
        }

        @Test
        public void testUpdateLU2RD() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateMainDiagonal(x -> x * 2);
            assertEquals(2, m.get(0, 0));
            assertEquals(10, m.get(1, 1));
            assertEquals(18, m.get(2, 2));
            assertEquals(2, m.get(0, 1)); // Non-diagonal unchanged
        }

        @Test
        public void testUpdateLU2RD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.updateMainDiagonal(x -> x * 2));
        }

        @Test
        public void testGetRU2LD() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            Integer[] diag = m.getAntiDiagonal();
            assertArrayEquals(new Integer[] { 3, 5, 7 }, diag);
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.setAntiDiagonal(new Integer[] { 10, 20, 30 });
            assertEquals(10, m.get(0, 2));
            assertEquals(20, m.get(1, 1));
            assertEquals(30, m.get(2, 0));
        }

        @Test
        public void testSetRU2LD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.setAntiDiagonal(new Integer[] { 10, 20 }));
        }

        @Test
        public void testSetRU2LD_wrongLength() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new Integer[] { 10 }));
        }

        @Test
        public void testUpdateRU2LD() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateAntiDiagonal(x -> x * 2);
            assertEquals(6, m.get(0, 2));
            assertEquals(10, m.get(1, 1));
            assertEquals(14, m.get(2, 0));
            assertEquals(2, m.get(0, 1)); // Non-diagonal unchanged
        }

        @Test
        public void testUpdateRU2LD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(x -> x * 2));
        }

        // ============ Update and Replace Methods ============

        @Test
        public void testUpdateAll_unaryOperator() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            m.updateAll(x -> x.toUpperCase());
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
            assertEquals("D", m.get(1, 1));
        }

        @Test
        public void testUpdateAll_unaryOperator_withNull() {
            Matrix<String> m = Matrix.of(new String[][] { { null, "b" }, { "c", null } });
            m.updateAll(x -> x == null ? "NULL" : x.toUpperCase());
            assertEquals("NULL", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
            assertEquals("NULL", m.get(1, 1));
        }

        @Test
        public void testUpdateAll_biFunction() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 0, 1 }, { 2, 3 } });
            m.updateAll((i, j) -> i * 10 + j);
            assertEquals(0, m.get(0, 0));
            assertEquals(1, m.get(0, 1));
            assertEquals(10, m.get(1, 0));
            assertEquals(11, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_valuePredicate() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, -2, 3 }, { -4, 5, -6 } });
            m.replaceIf(x -> x < 0, 0);
            assertEquals(1, m.get(0, 0));
            assertEquals(0, m.get(0, 1));
            assertEquals(3, m.get(0, 2));
            assertEquals(0, m.get(1, 0));
            assertEquals(5, m.get(1, 1));
            assertEquals(0, m.get(1, 2));
        }

        @Test
        public void testReplaceIf_valuePredicate_nulls() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", null, "c" }, { null, "e", null } });
            m.replaceIf(x -> x == null, "EMPTY");
            assertEquals("a", m.get(0, 0));
            assertEquals("EMPTY", m.get(0, 1));
            assertEquals("c", m.get(0, 2));
            assertEquals("EMPTY", m.get(1, 0));
        }

        @Test
        public void testReplaceIf_positionPredicate() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.replaceIf((i, j) -> i == j, 0); // Replace diagonal
            assertEquals(0, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(0, m.get(1, 1));
            assertEquals(0, m.get(2, 2));
        }

        @Test
        public void testReplaceIf_positionPredicate_upperTriangle() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.replaceIf((i, j) -> i < j, 0);
            assertEquals(1, m.get(0, 0));
            assertEquals(0, m.get(0, 1));
            assertEquals(0, m.get(0, 2));
            assertEquals(4, m.get(1, 0));
            assertEquals(5, m.get(1, 1));
            assertEquals(0, m.get(1, 2));
        }

        // ============ Map Methods ============

        @Test
        public void testMap_sameType() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            Matrix<String> mapped = m.map(String::toUpperCase);
            assertEquals("A", mapped.get(0, 0));
            assertEquals("B", mapped.get(0, 1));
            assertEquals("C", mapped.get(1, 0));
            assertEquals("D", mapped.get(1, 1));
        }

        @Test
        public void testMap_differentType() {
            Matrix<String> m = Matrix.of(new String[][] { { "ab", "abc" }, { "a", "abcd" } });
            Matrix<Integer> lengths = m.map(String::length, Integer.class);
            assertEquals(2, lengths.get(0, 0));
            assertEquals(3, lengths.get(0, 1));
            assertEquals(1, lengths.get(1, 0));
            assertEquals(4, lengths.get(1, 1));
        }

        @Test
        public void testMapToBoolean() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { -1, 2 }, { 3, -4 } });
            BooleanMatrix result = m.mapToBoolean(x -> x > 0);
            assertFalse(result.get(0, 0));
            assertTrue(result.get(0, 1));
            assertTrue(result.get(1, 0));
            assertFalse(result.get(1, 1));
        }

        @Test
        public void testMapToByte() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix result = m.mapToByte(Integer::byteValue);
            assertEquals(1, result.get(0, 0));
            assertEquals(2, result.get(0, 1));
            assertEquals(3, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void testMapToChar() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 65, 66 }, { 67, 68 } });
            CharMatrix result = m.mapToChar(x -> (char) x.intValue());
            assertEquals('A', result.get(0, 0));
            assertEquals('B', result.get(0, 1));
            assertEquals('C', result.get(1, 0));
            assertEquals('D', result.get(1, 1));
        }

        @Test
        public void testMapToShort() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix result = m.mapToShort(Integer::shortValue);
            assertEquals(1, result.get(0, 0));
            assertEquals(2, result.get(0, 1));
            assertEquals(3, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void testMapToInt() {
            Matrix<String> m = Matrix.of(new String[][] { { "ab", "abc" }, { "a", "abcd" } });
            IntMatrix result = m.mapToInt(String::length);
            assertEquals(2, result.get(0, 0));
            assertEquals(3, result.get(0, 1));
            assertEquals(1, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void testMapToLong() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            LongMatrix result = m.mapToLong(Integer::longValue);
            assertEquals(1L, result.get(0, 0));
            assertEquals(2L, result.get(0, 1));
            assertEquals(3L, result.get(1, 0));
            assertEquals(4L, result.get(1, 1));
        }

        @Test
        public void testMapToFloat() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            FloatMatrix result = m.mapToFloat(Integer::floatValue);
            assertEquals(1.0f, result.get(0, 0), 0.001f);
            assertEquals(2.0f, result.get(0, 1), 0.001f);
            assertEquals(3.0f, result.get(1, 0), 0.001f);
            assertEquals(4.0f, result.get(1, 1), 0.001f);
        }

        @Test
        public void testMapToDouble() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            DoubleMatrix result = m.mapToDouble(Integer::doubleValue);
            assertEquals(1.0, result.get(0, 0), 0.001);
            assertEquals(2.0, result.get(0, 1), 0.001);
            assertEquals(3.0, result.get(1, 0), 0.001);
            assertEquals(4.0, result.get(1, 1), 0.001);
        }

        // ============ Fill Methods ============

        @Test
        public void testFill_singleValue() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.fill("X");
            assertEquals("X", m.get(0, 0));
            assertEquals("X", m.get(0, 1));
            assertEquals("X", m.get(1, 0));
            assertEquals("X", m.get(1, 1));
        }

        @Test
        public void testFill_singleValue_null() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.fill((String) null);
            assertNull(m.get(0, 0));
            assertNull(m.get(0, 1));
            assertNull(m.get(1, 0));
            assertNull(m.get(1, 1));
        }

        @Test
        public void testFill_array() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[][] newData = { { "X", "Y" }, { "Z", "W" } };
            m.copyFrom(newData);
            assertEquals("X", m.get(0, 0));
            assertEquals("Y", m.get(0, 1));
            assertEquals("Z", m.get(1, 0));
            assertEquals("W", m.get(1, 1));
        }

        @Test
        public void testFill_array_partial() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            String[][] patch = { { "X", "Y" } };
            m.copyFrom(patch);
            assertEquals("X", m.get(0, 0));
            assertEquals("Y", m.get(0, 1));
            assertEquals("C", m.get(0, 2)); // Unchanged
            assertEquals("D", m.get(1, 0)); // Unchanged
        }

        @Test
        public void testFill_array_withOffset() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            String[][] patch = { { "X", "Y" } };
            m.copyFrom(1, 1, patch);
            assertEquals("A", m.get(0, 0));
            assertEquals("X", m.get(1, 1));
            assertEquals("Y", m.get(1, 2));
            assertEquals("G", m.get(2, 0)); // Unchanged
        }

        @Test
        public void testFill_array_withOffset_invalidIndices() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            String[][] patch = { { "X" } };
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(0, -1, patch));
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(2, 0, patch));
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(0, 3, patch));
        }

        // ============ Copy Methods ============

        @Test
        public void testCopy() {
            Matrix<String> original = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> copy = original.copy();

            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals("A", copy.get(0, 0));
            assertEquals("D", copy.get(1, 1));

            // Verify independence
            copy.set(0, 0, "X");
            assertEquals("A", original.get(0, 0));
            assertEquals("X", copy.get(0, 0));
        }

        @Test
        public void testCopy_emptyMatrix() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            Matrix<String> copy = empty.copy();
            assertTrue(copy.isEmpty());
        }

        @Test
        public void testCopy_rowRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            Matrix<String> subset = m.copy(1, 3);

            assertEquals(2, subset.rowCount());
            assertEquals(2, subset.columnCount());
            assertEquals("C", subset.get(0, 0));
            assertEquals("F", subset.get(1, 1));
        }

        @Test
        public void testCopy_rowRange_singleRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            Matrix<String> subset = m.copy(1, 2);

            assertEquals(1, subset.rowCount());
            assertEquals(2, subset.columnCount());
            assertEquals("C", subset.get(0, 0));
        }

        @Test
        public void testCopy_fullRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            Matrix<String> submatrix = m.copy(0, 2, 1, 3);

            assertEquals(2, submatrix.rowCount());
            assertEquals(2, submatrix.columnCount());
            assertEquals("B", submatrix.get(0, 0));
            assertEquals("C", submatrix.get(0, 1));
            assertEquals("E", submatrix.get(1, 0));
            assertEquals("F", submatrix.get(1, 1));
        }

        @Test
        public void testCopy_fullRange_singleElement() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> single = m.copy(1, 2, 1, 2);

            assertEquals(1, single.rowCount());
            assertEquals(1, single.columnCount());
            assertEquals("D", single.get(0, 0));
        }

        // ============ Extend Methods ============

        @Test
        public void testExtend_larger() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> extended = m.resize(3, 3);

            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(4, extended.get(1, 1));
            assertNull(extended.get(2, 2));
            assertNull(extended.get(0, 2));
        }

        @Test
        public void testExtend_larger_withDefault() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> extended = m.resize(3, 3, 0);

            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(4, extended.get(1, 1));
            assertEquals(0, extended.get(2, 2));
            assertEquals(0, extended.get(0, 2));
        }

        @Test
        public void testExtend_smaller() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            Matrix<Integer> truncated = m.resize(2, 2);

            assertEquals(2, truncated.rowCount());
            assertEquals(2, truncated.columnCount());
            assertEquals(1, truncated.get(0, 0));
            assertEquals(5, truncated.get(1, 1));
        }

        @Test
        public void testExtend_directionBased() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> extended = m.extend(1, 1, 1, 1);

            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertNull(extended.get(0, 0)); // New top row
            assertEquals(1, extended.get(1, 1)); // Original top-left
            assertEquals(4, extended.get(2, 2)); // Original bottom-right
        }

        @Test
        public void testExtend_directionBased_withDefault() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> extended = m.extend(1, 1, 1, 1, 99);

            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(99, extended.get(0, 0)); // New top row
            assertEquals(1, extended.get(1, 1)); // Original top-left
            assertEquals(4, extended.get(2, 2)); // Original bottom-right
        }

        // ============ Transformation Methods ============

        @Test
        public void testReverseH() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            m.flipInPlaceHorizontally();
            assertEquals("C", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("A", m.get(0, 2));
            assertEquals("F", m.get(1, 0));
        }

        @Test
        public void testReverseV() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            m.flipInPlaceVertically();
            assertEquals("E", m.get(0, 0));
            assertEquals("F", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
            assertEquals("A", m.get(2, 0));
        }

        @Test
        public void testFlipH() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> flipped = m.flipHorizontally();

            assertEquals("C", flipped.get(0, 0));
            assertEquals("B", flipped.get(0, 1));
            assertEquals("A", flipped.get(0, 2));
            assertEquals("F", flipped.get(1, 0));
            // Original unchanged
            assertEquals("A", m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            Matrix<String> flipped = m.flipVertically();

            assertEquals("E", flipped.get(0, 0));
            assertEquals("F", flipped.get(0, 1));
            assertEquals("C", flipped.get(1, 0));
            assertEquals("A", flipped.get(2, 0));
            // Original unchanged
            assertEquals("A", m.get(0, 0));
        }

        @Test
        public void testRotate90() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> rotated = m.rotate90();

            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals("D", rotated.get(0, 0));
            assertEquals("A", rotated.get(0, 1));
            assertEquals("F", rotated.get(2, 0));
            assertEquals("C", rotated.get(2, 1));
        }

        @Test
        public void testRotate180() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> rotated = m.rotate180();

            assertEquals(2, rotated.rowCount());
            assertEquals(3, rotated.columnCount());
            assertEquals("F", rotated.get(0, 0));
            assertEquals("E", rotated.get(0, 1));
            assertEquals("D", rotated.get(0, 2));
            assertEquals("C", rotated.get(1, 0));
        }

        @Test
        public void testRotate270() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> rotated = m.rotate270();

            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals("C", rotated.get(0, 0));
            assertEquals("F", rotated.get(0, 1));
            assertEquals("A", rotated.get(2, 0));
            assertEquals("D", rotated.get(2, 1));
        }

        @Test
        public void testTranspose() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> transposed = m.transpose();

            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals("A", transposed.get(0, 0));
            assertEquals("D", transposed.get(0, 1));
            assertEquals("B", transposed.get(1, 0));
            assertEquals("E", transposed.get(1, 1));
            assertEquals("C", transposed.get(2, 0));
            assertEquals("F", transposed.get(2, 1));
        }

        @Test
        public void testTranspose_squareMatrix() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> transposed = m.transpose();

            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals("A", transposed.get(0, 0));
            assertEquals("C", transposed.get(0, 1));
            assertEquals("B", transposed.get(1, 0));
            assertEquals("D", transposed.get(1, 1));
        }

        // ============ Reshape and Repeat Methods ============

        @Test
        public void testReshape_singleParam() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            Matrix<Integer> reshaped = m.reshape(2);

            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
            assertEquals(3, reshaped.get(1, 0));
            assertEquals(6, reshaped.get(2, 1));
        }

        @Test
        public void testReshape_twoParams() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            Matrix<Integer> reshaped = m.reshape(3, 2);

            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
            assertEquals(3, reshaped.get(1, 0));
            assertEquals(6, reshaped.get(2, 1));
        }

        @Test
        public void testReshape_twoParams_needsPadding() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            Matrix<Integer> reshaped = m.reshape(2, 4);

            assertEquals(2, reshaped.rowCount());
            assertEquals(4, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(4, reshaped.get(0, 3));
            assertEquals(5, reshaped.get(1, 0));
            assertEquals(6, reshaped.get(1, 1));
            assertNull(reshaped.get(1, 2)); // Padding
        }

        @Test
        public void testRepelem() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> repeated = m.repeatElements(2, 2);

            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals("A", repeated.get(0, 0));
            assertEquals("A", repeated.get(0, 1));
            assertEquals("A", repeated.get(1, 0));
            assertEquals("A", repeated.get(1, 1));
            assertEquals("D", repeated.get(2, 2));
            assertEquals("D", repeated.get(3, 3));
        }

        @Test
        public void testRepelem_invalidArgs() {
            Matrix<String> m = Matrix.of(new String[][] { { "A" } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> repeated = m.repeatMatrix(2, 2);

            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals("A", repeated.get(0, 0));
            assertEquals("B", repeated.get(0, 1));
            assertEquals("A", repeated.get(0, 2)); // Tiled
            assertEquals("A", repeated.get(2, 0)); // Tiled
            assertEquals("D", repeated.get(3, 3));
        }

        @Test
        public void testRepmat_invalidArgs() {
            Matrix<String> m = Matrix.of(new String[][] { { "A" } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        // ============ Flatten and Stream Methods ============

        @Test
        public void testFlatten() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<String> flat = m.flatten();

            assertEquals(4, flat.size());
            assertEquals("A", flat.get(0));
            assertEquals("B", flat.get(1));
            assertEquals("C", flat.get(2));
            assertEquals("D", flat.get(3));
        }

        @Test
        public void testFlatten_emptyMatrix() {
            Matrix<String> m = Matrix.of(new String[0][0]);
            List<String> flat = m.flatten();
            assertTrue(flat.isEmpty());
        }

        @Test
        public void testFlatOp() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 3, 1 }, { 4, 2 } });
            List<Integer> captured = new java.util.ArrayList<>();
            m.applyOnFlattened(arr -> {
                for (Integer val : arr) {
                    captured.add(val);
                }
            });
            assertEquals(4, captured.size());
        }

        // ============ Stack and Zip Methods ============

        @Test
        public void testVstack() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "C", "D" } });
            Matrix<String> stacked = m1.stackVertically(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals("A", stacked.get(0, 0));
            assertEquals("B", stacked.get(0, 1));
            assertEquals("C", stacked.get(1, 0));
            assertEquals("D", stacked.get(1, 1));
        }

        @Test
        public void testVstack_differentColCount() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "C" } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A" }, { "B" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "C" }, { "D" } });
            Matrix<String> stacked = m1.stackHorizontally(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals("A", stacked.get(0, 0));
            assertEquals("C", stacked.get(0, 1));
            assertEquals("B", stacked.get(1, 0));
            assertEquals("D", stacked.get(1, 1));
        }

        @Test
        public void testHstack_differentRowCount() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A" }, { "B" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "C" } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        @Test
        public void testZipWith_twoMatrices_sameType() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
            Matrix<Integer> result = m1.zipWith(m2, (a, b) -> a + b);

            assertEquals(11, result.get(0, 0));
            assertEquals(22, result.get(0, 1));
            assertEquals(33, result.get(1, 0));
            assertEquals(44, result.get(1, 1));
        }

        @Test
        public void testZipWith_twoMatrices_differentType() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> result = m1.zipWith(m2, (a, b) -> a + b, String.class);

            assertEquals("1A", result.get(0, 0));
            assertEquals("2B", result.get(0, 1));
            assertEquals("3C", result.get(1, 0));
            assertEquals("4D", result.get(1, 1));
        }

        @Test
        public void testZipWith_threeMatrices_sameType() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200 } });
            Matrix<Integer> result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);

            assertEquals(111, result.get(0, 0));
            assertEquals(222, result.get(0, 1));
        }

        @Test
        public void testZipWith_threeMatrices_differentType() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200 } });
            Matrix<String> result = m1.zipWith(m2, m3, (a, b, c) -> "" + a + b + c, String.class);

            assertEquals("110100", result.get(0, 0));
            assertEquals("220200", result.get(0, 1));
        }

        // ============ Stream Methods ============

        @Test
        public void testStreamLU2RD() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Integer> diagonal = m.streamMainDiagonal().toList();

            assertEquals(3, diagonal.size());
            assertEquals(1, diagonal.get(0));
            assertEquals(5, diagonal.get(1));
            assertEquals(9, diagonal.get(2));
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.streamMainDiagonal().toList());
        }

        @Test
        public void testStreamRU2LD() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Integer> diagonal = m.streamAntiDiagonal().toList();

            assertEquals(3, diagonal.size());
            assertEquals(3, diagonal.get(0));
            assertEquals(5, diagonal.get(1));
            assertEquals(7, diagonal.get(2));
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.streamAntiDiagonal().toList());
        }

        @Test
        public void testStreamH() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<String> elements = m.streamHorizontal().toList();

            assertEquals(4, elements.size());
            assertEquals("A", elements.get(0));
            assertEquals("B", elements.get(1));
            assertEquals("C", elements.get(2));
            assertEquals("D", elements.get(3));
        }

        @Test
        public void testStreamH_singleRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<String> row = m.streamHorizontal(1).toList();

            assertEquals(3, row.size());
            assertEquals("D", row.get(0));
            assertEquals("E", row.get(1));
            assertEquals("F", row.get(2));
        }

        @Test
        public void testStreamH_rowRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            List<String> elements = m.streamHorizontal(1, 3).toList();

            assertEquals(4, elements.size());
            assertEquals("C", elements.get(0));
            assertEquals("D", elements.get(1));
            assertEquals("E", elements.get(2));
            assertEquals("F", elements.get(3));
        }

        @Test
        public void testStreamV() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<String> elements = m.streamVertical().toList();

            assertEquals(4, elements.size());
            assertEquals("A", elements.get(0));
            assertEquals("C", elements.get(1));
            assertEquals("B", elements.get(2));
            assertEquals("D", elements.get(3));
        }

        @Test
        public void testStreamV_singleColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<String> col = m.streamVertical(1).toList();

            assertEquals(2, col.size());
            assertEquals("B", col.get(0));
            assertEquals("E", col.get(1));
        }

        @Test
        public void testStreamV_columnRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<String> elements = m.streamVertical(1, 3).toList();

            assertEquals(4, elements.size());
            assertEquals("B", elements.get(0));
            assertEquals("E", elements.get(1));
            assertEquals("C", elements.get(2));
            assertEquals("F", elements.get(3));
        }

        @Test
        public void testStreamR() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<List<String>> rows = m.streamRows().map(Stream::toList).toList();

            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).size());
            assertEquals("A", rows.get(0).get(0));
            assertEquals("B", rows.get(0).get(1));
            assertEquals("C", rows.get(1).get(0));
            assertEquals("D", rows.get(1).get(1));
        }

        @Test
        public void testStreamR_rowRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            List<List<String>> rows = m.streamRows(1, 3).map(Stream::toList).toList();

            assertEquals(2, rows.size());
            assertEquals("C", rows.get(0).get(0));
            assertEquals("E", rows.get(1).get(0));
        }

        @Test
        public void testStreamC() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<List<String>> columnCount = m.streamColumns().map(Stream::toList).toList();

            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).size());
            assertEquals("A", columnCount.get(0).get(0));
            assertEquals("C", columnCount.get(0).get(1));
            assertEquals("B", columnCount.get(1).get(0));
            assertEquals("D", columnCount.get(1).get(1));
        }

        @Test
        public void testStreamC_columnRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<List<String>> columnCount = m.streamColumns(1, 3).map(Stream::toList).toList();

            assertEquals(2, columnCount.size());
            assertEquals("B", columnCount.get(0).get(0));
            assertEquals("C", columnCount.get(1).get(0));
        }

        // ============ Println and toString ============

        @Test
        public void testPrintln() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String output = m.println();
            assertNotNull(output);
            assertTrue(output.contains("A"));
            assertTrue(output.contains("D"));
        }

        @Test
        public void testToString() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("A"));
            assertTrue(str.contains("D"));
        }

        // ============ Hashcode and Equals ============

        @Test
        public void testHashCode() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void testEquals_same() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertEquals(m1, m2);
        }

        @Test
        public void testEquals_different() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "X" } });
            assertNotSame(m1, m2);
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for Matrix class covering all public methods.
     * Tests with various object types including String, Integer, Double, and BigDecimal.
     */
    @Tag("2511")
    class Matrix2511Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidStringArray() {
            String[][] arr = { { "A", "B" }, { "C", "D" } };
            Matrix<String> m = new Matrix<>(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals("A", m.get(0, 0));
            assertEquals("D", m.get(1, 1));
        }

        @Test
        public void testConstructor_withValidIntegerArray() {
            Integer[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            Matrix<Integer> m = new Matrix<>(arr);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(6, m.get(1, 2));
        }

        @Test
        public void testConstructor_withEmptyArray() {
            Matrix<String> m = new Matrix<>(new String[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withSingleElement() {
            Matrix<Integer> m = new Matrix<>(new Integer[][] { { 42 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42, m.get(0, 0));
        }

        @Test
        public void testConstructor_withNullElements() {
            Matrix<String> m = new Matrix<>(new String[][] { { null, "B" }, { "C", null } });
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertNull(m.get(0, 0));
            assertEquals("B", m.get(0, 1));
        }

        @Test
        public void testConstructor_withBigDecimal() {
            BigDecimal[][] arr = { { BigDecimal.ONE, BigDecimal.TEN }, { BigDecimal.ZERO, new BigDecimal("3.14") } };
            Matrix<BigDecimal> m = new Matrix<>(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(BigDecimal.ONE, m.get(0, 0));
            assertEquals(new BigDecimal("3.14"), m.get(1, 1));
        }

        // ============ Factory Method Tests - of() ============

        @Test
        public void testOf_withValidStringArray() {
            String[][] arr = { { "A", "B" }, { "C", "D" } };
            Matrix<String> m = Matrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals("A", m.get(0, 0));
        }

        @Test
        public void testOf_withValidDoubleArray() {
            Double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            Matrix<Double> m = Matrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.5, m.get(0, 0));
            assertEquals(4.5, m.get(1, 1));
        }

        @Test
        public void testOf_withEmptyArray() {
            Matrix<String> m = Matrix.of(new String[0][0]);
            assertTrue(m.isEmpty());
        }

        // ============ Factory Method Tests - repeat() ============

        // ============ Factory Method Tests - diagonal ============

        @Test
        public void testDiagonalLU2RD_integers() {
            Integer[] diag = { 1, 2, 3 };
            Matrix<Integer> m = Matrix.mainDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
            assertNull(m.get(0, 1));
            assertNull(m.get(1, 0));
        }

        @Test
        public void testDiagonalLU2RD_strings() {
            String[] diag = { "A", "B", "C" };
            Matrix<String> m = Matrix.mainDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(1, 1));
            assertEquals("C", m.get(2, 2));
            assertNull(m.get(0, 1));
        }

        @Test
        public void testDiagonalLU2RD_emptyArray() {
            Matrix<String> m = Matrix.mainDiagonal(new String[0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testDiagonalRU2LD_integers() {
            Integer[] diag = { 1, 2, 3 };
            Matrix<Integer> m = Matrix.antiDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 2));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 0));
            assertNull(m.get(0, 0));
            assertNull(m.get(2, 2));
        }

        @Test
        public void testDiagonalRU2LD_emptyArray() {
            Matrix<String> m = Matrix.antiDiagonal(new String[0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testDiagonal_bothDiagonals_integers() {
            Integer[] lu2rd = { 1, 2, 3 };
            Integer[] ru2ld = { 7, 8, 9 };
            Matrix<Integer> m = Matrix.diagonals(lu2rd, ru2ld);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 2));
            assertEquals(7, m.get(0, 2));
            assertEquals(9, m.get(2, 0));
        }

        @Test
        public void testDiagonal_bothDiagonals_strings() {
            String[] lu2rd = { "A", "B" };
            String[] ru2ld = { "X", "Y" };
            Matrix<String> m = Matrix.diagonals(lu2rd, ru2ld);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(1, 1));
            assertEquals("X", m.get(0, 1));
            assertEquals("Y", m.get(1, 0));
        }

        @Test
        public void testDiagonal_onlyLU2RD() {
            Integer[] lu2rd = { 1, 2 };
            Matrix<Integer> m = Matrix.diagonals(lu2rd, null);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1));
        }

        @Test
        public void testDiagonal_onlyRU2LD() {
            Integer[] ru2ld = { 1, 2 };
            Matrix<Integer> m = Matrix.diagonals(null, ru2ld);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1, m.get(0, 1));
            assertEquals(2, m.get(1, 0));
        }

        @Test
        public void testDiagonal_bothNull() {
            assertThrows(IllegalArgumentException.class, () -> Matrix.diagonals(null, null));
        }

        @Test
        public void testDiagonal_differentLengths() {
            Integer[] lu2rd = { 1, 2, 3 };
            Integer[] ru2ld = { 7, 8 };
            assertThrows(IllegalArgumentException.class, () -> Matrix.diagonals(lu2rd, ru2ld));
        }

        // ============ Basic Access Methods ============

        @Test
        public void testComponentType_string() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" } });
            assertEquals(String.class, m.componentType());
        }

        @Test
        public void testComponentType_integer() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 } });
            assertEquals(Integer.class, m.componentType());
        }

        @Test
        public void testComponentType_bigDecimal() {
            Matrix<BigDecimal> m = Matrix.of(new BigDecimal[][] { { BigDecimal.ONE, BigDecimal.TEN } });
            assertEquals(BigDecimal.class, m.componentType());
        }

        @Test
        public void testGet_validIndices() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
            assertEquals("D", m.get(1, 1));
        }

        @Test
        public void testGet_withPoint() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Point p = Point.of(1, 0);
            assertEquals("C", m.get(p));
        }

        @Test
        public void testGet_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
            assertEquals(10, m.get(0, 0));
            assertEquals(40, m.get(1, 1));
        }

        @Test
        public void testSet_validIndices() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.set(0, 0, "X");
            m.set(1, 1, "Y");
            assertEquals("X", m.get(0, 0));
            assertEquals("Y", m.get(1, 1));
        }

        @Test
        public void testSet_withPoint() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Point p = Point.of(1, 0);
            m.set(p, "Z");
            assertEquals("Z", m.get(1, 0));
        }

        @Test
        public void testSet_nullValue() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.set(0, 0, null);
            assertNull(m.get(0, 0));
        }

        @Test
        public void testSet_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            m.set(0, 1, 99);
            assertEquals(99, m.get(0, 1));
        }

        // ============ Adjacent Element Methods ============

        @Test
        public void testUpOf_validPosition() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals("A", up.get());
        }

        @Test
        public void testUpOf_topEdge() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> up = m.above(0, 0);
            assertFalse(up.isPresent());
        }

        @Test
        public void testDownOf_validPosition() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals("C", down.get());
        }

        @Test
        public void testDownOf_bottomEdge() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> down = m.below(1, 0);
            assertFalse(down.isPresent());
        }

        @Test
        public void testLeftOf_validPosition() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals("A", left.get());
        }

        @Test
        public void testLeftOf_leftEdge() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> left = m.left(0, 0);
            assertFalse(left.isPresent());
        }

        @Test
        public void testRightOf_validPosition() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals("B", right.get());
        }

        @Test
        public void testRightOf_rightEdge() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Nullable<String> right = m.right(0, 1);
            assertFalse(right.isPresent());
        }

        @Test
        public void testAdjacentMethods_withIntegers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            assertEquals(5, m.above(2, 1).get());
            assertEquals(5, m.below(0, 1).get());
            assertEquals(5, m.left(1, 2).get());
            assertEquals(5, m.right(1, 0).get());
        }

        // ============ Row and Column Access ============

        @Test
        public void testRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[] row = m.rowView(0);
            assertArrayEquals(new String[] { "A", "B" }, row);
        }

        @Test
        public void testRow_lastRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[] row = m.rowView(1);
            assertArrayEquals(new String[] { "C", "D" }, row);
        }

        @Test
        public void testRow_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            Integer[] row = m.rowView(1);
            assertArrayEquals(new Integer[] { 4, 5, 6 }, row);
        }

        @Test
        public void testRow_invalidIndex() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(1));
        }

        @Test
        public void testColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[] col = m.columnCopy(0);
            assertArrayEquals(new String[] { "A", "C" }, col);
        }

        @Test
        public void testColumn_lastColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[] col = m.columnCopy(1);
            assertArrayEquals(new String[] { "B", "D" }, col);
        }

        @Test
        public void testColumn_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            Integer[] col = m.columnCopy(1);
            assertArrayEquals(new Integer[] { 2, 4, 6 }, col);
        }

        @Test
        public void testColumn_invalidIndex() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.setRow(0, new String[] { "X", "Y" });
            assertArrayEquals(new String[] { "X", "Y" }, m.rowView(0));
            assertArrayEquals(new String[] { "C", "D" }, m.rowView(1));
        }

        @Test
        public void testSetRow_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            m.setRow(1, new Integer[] { 10, 20 });
            assertArrayEquals(new Integer[] { 10, 20 }, m.rowView(1));
        }

        @Test
        public void testSetRow_wrongLength() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "X" }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "X", "Y", "Z" }));
        }

        @Test
        public void testSetColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.setColumn(0, new String[] { "X", "Y" });
            assertArrayEquals(new String[] { "X", "Y" }, m.columnCopy(0));
            assertArrayEquals(new String[] { "B", "D" }, m.columnCopy(1));
        }

        @Test
        public void testSetColumn_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            m.setColumn(0, new Integer[] { 100, 200 });
            assertArrayEquals(new Integer[] { 100, 200 }, m.columnCopy(0));
        }

        @Test
        public void testSetColumn_wrongLength() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "X" }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "X", "Y", "Z" }));
        }

        @Test
        public void testUpdateRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            m.updateRow(0, String::toUpperCase);
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("c", m.get(1, 0));
        }

        @Test
        public void testUpdateRow_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            m.updateRow(1, x -> x * 10);
            assertEquals(1, m.get(0, 0));
            assertEquals(30, m.get(1, 0));
            assertEquals(40, m.get(1, 1));
        }

        @Test
        public void testUpdateColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            m.updateColumn(0, String::toUpperCase);
            assertEquals("A", m.get(0, 0));
            assertEquals("b", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
        }

        @Test
        public void testUpdateColumn_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            m.updateColumn(1, x -> x + 100);
            assertEquals(1, m.get(0, 0));
            assertEquals(102, m.get(0, 1));
            assertEquals(104, m.get(1, 1));
        }

        // ============ Diagonal Methods ============

        @Test
        public void testGetLU2RD_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            Integer[] diag = m.getMainDiagonal();
            assertArrayEquals(new Integer[] { 1, 5, 9 }, diag);
        }

        @Test
        public void testGetLU2RD_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[] diag = m.getMainDiagonal();
            assertArrayEquals(new String[] { "A", "D" }, diag);
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.setMainDiagonal(new Integer[] { 10, 20, 30 });
            assertEquals(10, m.get(0, 0));
            assertEquals(20, m.get(1, 1));
            assertEquals(30, m.get(2, 2));
        }

        @Test
        public void testSetLU2RD_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.setMainDiagonal(new String[] { "X", "Y" });
            assertEquals("X", m.get(0, 0));
            assertEquals("Y", m.get(1, 1));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new Integer[] { 10, 20 }));
        }

        @Test
        public void testSetLU2RD_wrongLength() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new Integer[] { 10 }));
        }

        @Test
        public void testUpdateLU2RD_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateMainDiagonal(x -> x * 2);
            assertEquals(2, m.get(0, 0));
            assertEquals(10, m.get(1, 1));
            assertEquals(18, m.get(2, 2));
            assertEquals(2, m.get(0, 1)); // Non-diagonal unchanged
        }

        @Test
        public void testUpdateLU2RD_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            m.updateMainDiagonal(String::toUpperCase);
            assertEquals("A", m.get(0, 0));
            assertEquals("D", m.get(1, 1));
            assertEquals("b", m.get(0, 1)); // Non-diagonal unchanged
        }

        @Test
        public void testUpdateLU2RD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.updateMainDiagonal(x -> x * 2));
        }

        @Test
        public void testGetRU2LD_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            Integer[] diag = m.getAntiDiagonal();
            assertArrayEquals(new Integer[] { 3, 5, 7 }, diag);
        }

        @Test
        public void testGetRU2LD_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[] diag = m.getAntiDiagonal();
            assertArrayEquals(new String[] { "B", "C" }, diag);
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.setAntiDiagonal(new Integer[] { 10, 20, 30 });
            assertEquals(10, m.get(0, 2));
            assertEquals(20, m.get(1, 1));
            assertEquals(30, m.get(2, 0));
        }

        @Test
        public void testSetRU2LD_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.setAntiDiagonal(new String[] { "X", "Y" });
            assertEquals("X", m.get(0, 1));
            assertEquals("Y", m.get(1, 0));
        }

        @Test
        public void testSetRU2LD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.setAntiDiagonal(new Integer[] { 10, 20 }));
        }

        @Test
        public void testSetRU2LD_wrongLength() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new Integer[] { 10 }));
        }

        @Test
        public void testUpdateRU2LD_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.updateAntiDiagonal(x -> x * 2);
            assertEquals(6, m.get(0, 2));
            assertEquals(10, m.get(1, 1));
            assertEquals(14, m.get(2, 0));
            assertEquals(2, m.get(0, 1)); // Non-diagonal unchanged
        }

        @Test
        public void testUpdateRU2LD_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            m.updateAntiDiagonal(String::toUpperCase);
            assertEquals("B", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
            assertEquals("a", m.get(0, 0)); // Non-diagonal unchanged
        }

        @Test
        public void testUpdateRU2LD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(x -> x * 2));
        }

        // ============ Update and Replace Methods ============

        @Test
        public void testUpdateAll_unaryOperator_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            m.updateAll(x -> x.toUpperCase());
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
            assertEquals("D", m.get(1, 1));
        }

        @Test
        public void testUpdateAll_unaryOperator_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            m.updateAll(x -> x * 10);
            assertEquals(10, m.get(0, 0));
            assertEquals(20, m.get(0, 1));
            assertEquals(30, m.get(1, 0));
            assertEquals(40, m.get(1, 1));
        }

        @Test
        public void testUpdateAll_unaryOperator_withNull() {
            Matrix<String> m = Matrix.of(new String[][] { { null, "b" }, { "c", null } });
            m.updateAll(x -> x == null ? "NULL" : x.toUpperCase());
            assertEquals("NULL", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
            assertEquals("NULL", m.get(1, 1));
        }

        @Test
        public void testUpdateAll_biFunction_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 0, 1 }, { 2, 3 } });
            m.updateAll((i, j) -> i * 10 + j);
            assertEquals(0, m.get(0, 0));
            assertEquals(1, m.get(0, 1));
            assertEquals(10, m.get(1, 0));
            assertEquals(11, m.get(1, 1));
        }

        @Test
        public void testUpdateAll_biFunction_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            m.updateAll((i, j) -> "" + i + j);
            assertEquals("00", m.get(0, 0));
            assertEquals("01", m.get(0, 1));
            assertEquals("10", m.get(1, 0));
            assertEquals("11", m.get(1, 1));
        }

        @Test
        public void testReplaceIf_valuePredicate_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, -2, 3 }, { -4, 5, -6 } });
            m.replaceIf(x -> x < 0, 0);
            assertEquals(1, m.get(0, 0));
            assertEquals(0, m.get(0, 1));
            assertEquals(3, m.get(0, 2));
            assertEquals(0, m.get(1, 0));
            assertEquals(5, m.get(1, 1));
            assertEquals(0, m.get(1, 2));
        }

        @Test
        public void testReplaceIf_valuePredicate_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "short", "verylongstring" }, { "ok", "toolong" } });
            m.replaceIf(x -> x.length() > 5, "TOO_LONG");
            assertEquals("short", m.get(0, 0));
            assertEquals("TOO_LONG", m.get(0, 1));
            assertEquals("ok", m.get(1, 0));
            assertEquals("TOO_LONG", m.get(1, 1));
        }

        @Test
        public void testReplaceIf_valuePredicate_nulls() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", null, "c" }, { null, "e", null } });
            m.replaceIf(x -> x == null, "EMPTY");
            assertEquals("a", m.get(0, 0));
            assertEquals("EMPTY", m.get(0, 1));
            assertEquals("c", m.get(0, 2));
            assertEquals("EMPTY", m.get(1, 0));
        }

        @Test
        public void testReplaceIf_positionPredicate_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            m.replaceIf((i, j) -> i == j, 0); // Replace diagonal
            assertEquals(0, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(0, m.get(1, 1));
            assertEquals(0, m.get(2, 2));
        }

        @Test
        public void testReplaceIf_positionPredicate_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            m.replaceIf((i, j) -> i < j, "X"); // Replace upper triangle
            assertEquals("A", m.get(0, 0));
            assertEquals("X", m.get(0, 1));
            assertEquals("X", m.get(0, 2));
            assertEquals("D", m.get(1, 0));
            assertEquals("E", m.get(1, 1));
            assertEquals("X", m.get(1, 2));
        }

        // ============ Map Methods ============

        @Test
        public void testMap_sameType_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            Matrix<String> mapped = m.map(String::toUpperCase);
            assertEquals("A", mapped.get(0, 0));
            assertEquals("B", mapped.get(0, 1));
            assertEquals("C", mapped.get(1, 0));
            assertEquals("D", mapped.get(1, 1));
        }

        @Test
        public void testMap_sameType_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> mapped = m.map(x -> x * 2);
            assertEquals(2, mapped.get(0, 0));
            assertEquals(4, mapped.get(0, 1));
            assertEquals(6, mapped.get(1, 0));
            assertEquals(8, mapped.get(1, 1));
        }

        @Test
        public void testMap_differentType_stringToInteger() {
            Matrix<String> m = Matrix.of(new String[][] { { "ab", "abc" }, { "a", "abcd" } });
            Matrix<Integer> lengths = m.map(String::length, Integer.class);
            assertEquals(2, lengths.get(0, 0));
            assertEquals(3, lengths.get(0, 1));
            assertEquals(1, lengths.get(1, 0));
            assertEquals(4, lengths.get(1, 1));
        }

        @Test
        public void testMap_differentType_integerToString() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 22 }, { 333, 4444 } });
            Matrix<String> strings = m.map(String::valueOf, String.class);
            assertEquals("1", strings.get(0, 0));
            assertEquals("22", strings.get(0, 1));
            assertEquals("333", strings.get(1, 0));
            assertEquals("4444", strings.get(1, 1));
        }

        @Test
        public void testMap_differentType_integerToDouble() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Double> doubles = m.map(x -> x * 1.5, Double.class);
            assertEquals(1.5, doubles.get(0, 0));
            assertEquals(3.0, doubles.get(0, 1));
            assertEquals(4.5, doubles.get(1, 0));
            assertEquals(6.0, doubles.get(1, 1));
        }

        @Test
        public void testMapToBoolean_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { -1, 2 }, { 3, -4 } });
            BooleanMatrix result = m.mapToBoolean(x -> x > 0);
            assertFalse(result.get(0, 0));
            assertTrue(result.get(0, 1));
            assertTrue(result.get(1, 0));
            assertFalse(result.get(1, 1));
        }

        @Test
        public void testMapToBoolean_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "hello", "hi" }, { "greetings", "hey" } });
            BooleanMatrix result = m.mapToBoolean(x -> x.length() > 3);
            assertTrue(result.get(0, 0));
            assertFalse(result.get(0, 1));
            assertTrue(result.get(1, 0));
            assertFalse(result.get(1, 1));
        }

        @Test
        public void testMapToByte() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            ByteMatrix result = m.mapToByte(Integer::byteValue);
            assertEquals(1, result.get(0, 0));
            assertEquals(2, result.get(0, 1));
            assertEquals(3, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void testMapToChar() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 65, 66 }, { 67, 68 } });
            CharMatrix result = m.mapToChar(x -> (char) x.intValue());
            assertEquals('A', result.get(0, 0));
            assertEquals('B', result.get(0, 1));
            assertEquals('C', result.get(1, 0));
            assertEquals('D', result.get(1, 1));
        }

        @Test
        public void testMapToShort() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            ShortMatrix result = m.mapToShort(Integer::shortValue);
            assertEquals(1, result.get(0, 0));
            assertEquals(2, result.get(0, 1));
            assertEquals(3, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void testMapToInt_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "ab", "abc" }, { "a", "abcd" } });
            IntMatrix result = m.mapToInt(String::length);
            assertEquals(2, result.get(0, 0));
            assertEquals(3, result.get(0, 1));
            assertEquals(1, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void testMapToInt_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            IntMatrix result = m.mapToInt(x -> x * 10);
            assertEquals(10, result.get(0, 0));
            assertEquals(20, result.get(0, 1));
            assertEquals(30, result.get(1, 0));
            assertEquals(40, result.get(1, 1));
        }

        @Test
        public void testMapToLong() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            LongMatrix result = m.mapToLong(Integer::longValue);
            assertEquals(1L, result.get(0, 0));
            assertEquals(2L, result.get(0, 1));
            assertEquals(3L, result.get(1, 0));
            assertEquals(4L, result.get(1, 1));
        }

        @Test
        public void testMapToFloat() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            FloatMatrix result = m.mapToFloat(Integer::floatValue);
            assertEquals(1.0f, result.get(0, 0), 0.001f);
            assertEquals(2.0f, result.get(0, 1), 0.001f);
            assertEquals(3.0f, result.get(1, 0), 0.001f);
            assertEquals(4.0f, result.get(1, 1), 0.001f);
        }

        @Test
        public void testMapToDouble_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            DoubleMatrix result = m.mapToDouble(Integer::doubleValue);
            assertEquals(1.0, result.get(0, 0), 0.001);
            assertEquals(2.0, result.get(0, 1), 0.001);
            assertEquals(3.0, result.get(1, 0), 0.001);
            assertEquals(4.0, result.get(1, 1), 0.001);
        }

        @Test
        public void testMapToDouble_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "1.5", "2.5" }, { "3.5", "4.5" } });
            DoubleMatrix result = m.mapToDouble(Double::parseDouble);
            assertEquals(1.5, result.get(0, 0), 0.001);
            assertEquals(2.5, result.get(0, 1), 0.001);
            assertEquals(3.5, result.get(1, 0), 0.001);
            assertEquals(4.5, result.get(1, 1), 0.001);
        }

        // ============ Fill Methods ============

        @Test
        public void testFill_singleValue_string() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.fill("X");
            assertEquals("X", m.get(0, 0));
            assertEquals("X", m.get(0, 1));
            assertEquals("X", m.get(1, 0));
            assertEquals("X", m.get(1, 1));
        }

        @Test
        public void testFill_singleValue_integer() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            m.fill(99);
            assertEquals(99, m.get(0, 0));
            assertEquals(99, m.get(0, 1));
            assertEquals(99, m.get(1, 0));
            assertEquals(99, m.get(1, 1));
        }

        @Test
        public void testFill_singleValue_null() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            m.fill((String) null);
            assertNull(m.get(0, 0));
            assertNull(m.get(0, 1));
            assertNull(m.get(1, 0));
            assertNull(m.get(1, 1));
        }

        @Test
        public void testFill_array_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[][] newData = { { "X", "Y" }, { "Z", "W" } };
            m.copyFrom(newData);
            assertEquals("X", m.get(0, 0));
            assertEquals("Y", m.get(0, 1));
            assertEquals("Z", m.get(1, 0));
            assertEquals("W", m.get(1, 1));
        }

        @Test
        public void testFill_array_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Integer[][] newData = { { 10, 20 }, { 30, 40 } };
            m.copyFrom(newData);
            assertEquals(10, m.get(0, 0));
            assertEquals(20, m.get(0, 1));
            assertEquals(30, m.get(1, 0));
            assertEquals(40, m.get(1, 1));
        }

        @Test
        public void testFill_array_partial() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            String[][] patch = { { "X", "Y" } };
            m.copyFrom(patch);
            assertEquals("X", m.get(0, 0));
            assertEquals("Y", m.get(0, 1));
            assertEquals("C", m.get(0, 2)); // Unchanged
            assertEquals("D", m.get(1, 0)); // Unchanged
        }

        @Test
        public void testFill_array_withOffset() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            String[][] patch = { { "X", "Y" } };
            m.copyFrom(1, 1, patch);
            assertEquals("A", m.get(0, 0));
            assertEquals("X", m.get(1, 1));
            assertEquals("Y", m.get(1, 2));
            assertEquals("G", m.get(2, 0)); // Unchanged
        }

        @Test
        public void testFill_array_withOffset_invalidIndices() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
            String[][] patch = { { "X" } };
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(0, -1, patch));
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(2, 0, patch));
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(0, 3, patch));
        }

        // ============ Copy Methods ============

        @Test
        public void testCopy_strings() {
            Matrix<String> original = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> copy = original.copy();

            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals("A", copy.get(0, 0));
            assertEquals("D", copy.get(1, 1));

            copy.set(0, 0, "X");
            assertEquals("A", original.get(0, 0));
            assertEquals("X", copy.get(0, 0));
        }

        @Test
        public void testCopy_integers() {
            Matrix<Integer> original = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> copy = original.copy();

            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(1, copy.get(0, 0));
            assertEquals(4, copy.get(1, 1));

            copy.set(0, 0, 99);
            assertEquals(1, original.get(0, 0));
            assertEquals(99, copy.get(0, 0));
        }

        @Test
        public void testCopy_emptyMatrix() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            Matrix<String> copy = empty.copy();
            assertTrue(copy.isEmpty());
        }

        @Test
        public void testCopy_rowRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            Matrix<String> subset = m.copy(1, 3);

            assertEquals(2, subset.rowCount());
            assertEquals(2, subset.columnCount());
            assertEquals("C", subset.get(0, 0));
            assertEquals("F", subset.get(1, 1));
        }

        @Test
        public void testCopy_rowRange_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            Matrix<Integer> subset = m.copy(0, 2);

            assertEquals(2, subset.rowCount());
            assertEquals(2, subset.columnCount());
            assertEquals(1, subset.get(0, 0));
            assertEquals(4, subset.get(1, 1));
        }

        @Test
        public void testCopy_rowRange_singleRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            Matrix<String> subset = m.copy(1, 2);

            assertEquals(1, subset.rowCount());
            assertEquals(2, subset.columnCount());
            assertEquals("C", subset.get(0, 0));
        }

        @Test
        public void testCopy_fullRange_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
            Matrix<String> submatrix = m.copy(0, 2, 1, 3);

            assertEquals(2, submatrix.rowCount());
            assertEquals(2, submatrix.columnCount());
            assertEquals("B", submatrix.get(0, 0));
            assertEquals("C", submatrix.get(0, 1));
            assertEquals("E", submatrix.get(1, 0));
            assertEquals("F", submatrix.get(1, 1));
        }

        @Test
        public void testCopy_fullRange_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            Matrix<Integer> submatrix = m.copy(1, 3, 1, 3);

            assertEquals(2, submatrix.rowCount());
            assertEquals(2, submatrix.columnCount());
            assertEquals(5, submatrix.get(0, 0));
            assertEquals(6, submatrix.get(0, 1));
            assertEquals(8, submatrix.get(1, 0));
            assertEquals(9, submatrix.get(1, 1));
        }

        @Test
        public void testCopy_fullRange_singleElement() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> single = m.copy(1, 2, 1, 2);

            assertEquals(1, single.rowCount());
            assertEquals(1, single.columnCount());
            assertEquals("D", single.get(0, 0));
        }

        // ============ Extend Methods ============

        @Test
        public void testExtend_larger_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> extended = m.resize(3, 3);

            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(4, extended.get(1, 1));
            assertNull(extended.get(2, 2));
            assertNull(extended.get(0, 2));
        }

        @Test
        public void testExtend_larger_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> extended = m.resize(3, 3);

            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals("A", extended.get(0, 0));
            assertEquals("D", extended.get(1, 1));
            assertNull(extended.get(2, 2));
        }

        @Test
        public void testExtend_larger_withDefault_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> extended = m.resize(3, 3, 0);

            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1, extended.get(0, 0));
            assertEquals(4, extended.get(1, 1));
            assertEquals(0, extended.get(2, 2));
            assertEquals(0, extended.get(0, 2));
        }

        @Test
        public void testExtend_larger_withDefault_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> extended = m.resize(3, 3, "X");

            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals("A", extended.get(0, 0));
            assertEquals("D", extended.get(1, 1));
            assertEquals("X", extended.get(2, 2));
            assertEquals("X", extended.get(0, 2));
        }

        @Test
        public void testExtend_smaller() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            Matrix<Integer> truncated = m.resize(2, 2);

            assertEquals(2, truncated.rowCount());
            assertEquals(2, truncated.columnCount());
            assertEquals(1, truncated.get(0, 0));
            assertEquals(5, truncated.get(1, 1));
        }

        @Test
        public void testExtend_directionBased_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> extended = m.extend(1, 1, 1, 1);

            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertNull(extended.get(0, 0)); // New top row
            assertEquals(1, extended.get(1, 1)); // Original top-left
            assertEquals(4, extended.get(2, 2)); // Original bottom-right
        }

        @Test
        public void testExtend_directionBased_withDefault() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> extended = m.extend(1, 1, 1, 1, 99);

            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(99, extended.get(0, 0)); // New top row
            assertEquals(1, extended.get(1, 1)); // Original top-left
            assertEquals(4, extended.get(2, 2)); // Original bottom-right
        }

        @Test
        public void testExtend_directionBased_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A" } });
            Matrix<String> extended = m.extend(1, 1, 1, 1, "X");

            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals("X", extended.get(0, 0));
            assertEquals("A", extended.get(1, 1));
            assertEquals("X", extended.get(2, 2));
        }

        // ============ Transformation Methods ============

        @Test
        public void testReverseH_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            m.flipInPlaceHorizontally();
            assertEquals("C", m.get(0, 0));
            assertEquals("B", m.get(0, 1));
            assertEquals("A", m.get(0, 2));
            assertEquals("F", m.get(1, 0));
        }

        @Test
        public void testReverseH_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            m.flipInPlaceHorizontally();
            assertEquals(2, m.get(0, 0));
            assertEquals(1, m.get(0, 1));
            assertEquals(4, m.get(1, 0));
            assertEquals(3, m.get(1, 1));
        }

        @Test
        public void testReverseV_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            m.flipInPlaceVertically();
            assertEquals("E", m.get(0, 0));
            assertEquals("F", m.get(0, 1));
            assertEquals("C", m.get(1, 0));
            assertEquals("A", m.get(2, 0));
        }

        @Test
        public void testReverseV_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            m.flipInPlaceVertically();
            assertEquals(3, m.get(0, 0));
            assertEquals(4, m.get(0, 1));
            assertEquals(1, m.get(1, 0));
            assertEquals(2, m.get(1, 1));
        }

        @Test
        public void testFlipH_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> flipped = m.flipHorizontally();

            assertEquals("C", flipped.get(0, 0));
            assertEquals("B", flipped.get(0, 1));
            assertEquals("A", flipped.get(0, 2));
            assertEquals("F", flipped.get(1, 0));
            assertEquals("A", m.get(0, 0)); // Original unchanged
        }

        @Test
        public void testFlipH_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> flipped = m.flipHorizontally();

            assertEquals(2, flipped.get(0, 0));
            assertEquals(1, flipped.get(0, 1));
            assertEquals(1, m.get(0, 0)); // Original unchanged
        }

        @Test
        public void testFlipV_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            Matrix<String> flipped = m.flipVertically();

            assertEquals("E", flipped.get(0, 0));
            assertEquals("F", flipped.get(0, 1));
            assertEquals("C", flipped.get(1, 0));
            assertEquals("A", flipped.get(2, 0));
            assertEquals("A", m.get(0, 0)); // Original unchanged
        }

        @Test
        public void testFlipV_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> flipped = m.flipVertically();

            assertEquals(3, flipped.get(0, 0));
            assertEquals(1, m.get(0, 0)); // Original unchanged
        }

        @Test
        public void testRotate90_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> rotated = m.rotate90();

            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals("D", rotated.get(0, 0));
            assertEquals("A", rotated.get(0, 1));
            assertEquals("F", rotated.get(2, 0));
            assertEquals("C", rotated.get(2, 1));
        }

        @Test
        public void testRotate90_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> rotated = m.rotate90();

            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3, rotated.get(0, 0));
            assertEquals(1, rotated.get(0, 1));
            assertEquals(4, rotated.get(1, 0));
            assertEquals(2, rotated.get(1, 1));
        }

        @Test
        public void testRotate180_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> rotated = m.rotate180();

            assertEquals(2, rotated.rowCount());
            assertEquals(3, rotated.columnCount());
            assertEquals("F", rotated.get(0, 0));
            assertEquals("E", rotated.get(0, 1));
            assertEquals("D", rotated.get(0, 2));
            assertEquals("C", rotated.get(1, 0));
        }

        @Test
        public void testRotate180_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> rotated = m.rotate180();

            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4, rotated.get(0, 0));
            assertEquals(3, rotated.get(0, 1));
            assertEquals(2, rotated.get(1, 0));
            assertEquals(1, rotated.get(1, 1));
        }

        @Test
        public void testRotate270_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> rotated = m.rotate270();

            assertEquals(3, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals("C", rotated.get(0, 0));
            assertEquals("F", rotated.get(0, 1));
            assertEquals("A", rotated.get(2, 0));
            assertEquals("D", rotated.get(2, 1));
        }

        @Test
        public void testRotate270_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> rotated = m.rotate270();

            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(2, rotated.get(0, 0));
            assertEquals(4, rotated.get(0, 1));
            assertEquals(1, rotated.get(1, 0));
            assertEquals(3, rotated.get(1, 1));
        }

        @Test
        public void testTranspose_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> transposed = m.transpose();

            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals("A", transposed.get(0, 0));
            assertEquals("D", transposed.get(0, 1));
            assertEquals("B", transposed.get(1, 0));
            assertEquals("E", transposed.get(1, 1));
            assertEquals("C", transposed.get(2, 0));
            assertEquals("F", transposed.get(2, 1));
        }

        @Test
        public void testTranspose_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            Matrix<Integer> transposed = m.transpose();

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
        public void testTranspose_squareMatrix() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> transposed = m.transpose();

            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals("A", transposed.get(0, 0));
            assertEquals("C", transposed.get(0, 1));
            assertEquals("B", transposed.get(1, 0));
            assertEquals("D", transposed.get(1, 1));
        }

        // ============ Reshape and Repeat Methods ============

        @Test
        public void testReshape_singleParam_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            Matrix<Integer> reshaped = m.reshape(2);

            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
            assertEquals(3, reshaped.get(1, 0));
            assertEquals(6, reshaped.get(2, 1));
        }

        @Test
        public void testReshape_singleParam_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> reshaped = m.reshape(3);

            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals("A", reshaped.get(0, 0));
            assertEquals("C", reshaped.get(0, 2));
            assertEquals("F", reshaped.get(1, 2));
        }

        @Test
        public void testReshape_twoParams_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            Matrix<Integer> reshaped = m.reshape(3, 2);

            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(2, reshaped.get(0, 1));
            assertEquals(3, reshaped.get(1, 0));
            assertEquals(6, reshaped.get(2, 1));
        }

        @Test
        public void testReshape_twoParams_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Matrix<String> reshaped = m.reshape(2, 3);

            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals("A", reshaped.get(0, 0));
            assertEquals("F", reshaped.get(1, 2));
        }

        @Test
        public void testReshape_twoParams_needsPadding() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            Matrix<Integer> reshaped = m.reshape(2, 4);

            assertEquals(2, reshaped.rowCount());
            assertEquals(4, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(4, reshaped.get(0, 3));
            assertEquals(5, reshaped.get(1, 0));
            assertEquals(6, reshaped.get(1, 1));
            assertNull(reshaped.get(1, 2)); // Padding
        }

        @Test
        public void testRepelem_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> repeated = m.repeatElements(2, 2);

            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals("A", repeated.get(0, 0));
            assertEquals("A", repeated.get(0, 1));
            assertEquals("A", repeated.get(1, 0));
            assertEquals("A", repeated.get(1, 1));
            assertEquals("D", repeated.get(2, 2));
            assertEquals("D", repeated.get(3, 3));
        }

        @Test
        public void testRepelem_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> repeated = m.repeatElements(2, 3);

            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(1, repeated.get(0, 1));
            assertEquals(1, repeated.get(1, 2));
            assertEquals(4, repeated.get(3, 5));
        }

        @Test
        public void testRepelem_invalidArgs() {
            Matrix<String> m = Matrix.of(new String[][] { { "A" } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> repeated = m.repeatMatrix(2, 2);

            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals("A", repeated.get(0, 0));
            assertEquals("B", repeated.get(0, 1));
            assertEquals("A", repeated.get(0, 2)); // Tiled
            assertEquals("A", repeated.get(2, 0)); // Tiled
            assertEquals("D", repeated.get(3, 3));
        }

        @Test
        public void testRepmat_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> repeated = m.repeatMatrix(3, 2);

            assertEquals(6, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals(1, repeated.get(0, 0));
            assertEquals(2, repeated.get(0, 1));
            assertEquals(1, repeated.get(0, 2)); // Tiled horizontally
            assertEquals(1, repeated.get(2, 0)); // Tiled vertically
            assertEquals(4, repeated.get(5, 3));
        }

        @Test
        public void testRepmat_invalidArgs() {
            Matrix<String> m = Matrix.of(new String[][] { { "A" } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        // ============ Flatten and Stream Methods ============

        @Test
        public void testFlatten_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<String> flat = m.flatten();

            assertEquals(4, flat.size());
            assertEquals("A", flat.get(0));
            assertEquals("B", flat.get(1));
            assertEquals("C", flat.get(2));
            assertEquals("D", flat.get(3));
        }

        @Test
        public void testFlatten_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            List<Integer> flat = m.flatten();

            assertEquals(4, flat.size());
            assertEquals(1, flat.get(0));
            assertEquals(2, flat.get(1));
            assertEquals(3, flat.get(2));
            assertEquals(4, flat.get(3));
        }

        @Test
        public void testFlatten_emptyMatrix() {
            Matrix<String> m = Matrix.of(new String[0][0]);
            List<String> flat = m.flatten();
            assertTrue(flat.isEmpty());
        }

        @Test
        public void testFlatOp_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<String> captured = new java.util.ArrayList<>();
            m.applyOnFlattened(arr -> {
                for (String val : arr) {
                    captured.add(val);
                }
            });
            assertEquals(4, captured.size());
            assertEquals("A", captured.get(0));
        }

        @Test
        public void testFlatOp_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 3, 1 }, { 4, 2 } });
            List<Integer> captured = new java.util.ArrayList<>();
            m.applyOnFlattened(arr -> {
                for (Integer val : arr) {
                    captured.add(val);
                }
            });
            assertEquals(4, captured.size());
        }

        // ============ Stack and Zip Methods ============

        @Test
        public void testVstack_strings() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "C", "D" } });
            Matrix<String> stacked = m1.stackVertically(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals("A", stacked.get(0, 0));
            assertEquals("B", stacked.get(0, 1));
            assertEquals("C", stacked.get(1, 0));
            assertEquals("D", stacked.get(1, 1));
        }

        @Test
        public void testVstack_integers() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2, 3 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 4, 5, 6 } });
            Matrix<Integer> stacked = m1.stackVertically(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(3, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(6, stacked.get(1, 2));
        }

        @Test
        public void testVstack_differentColCount() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "C" } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack_strings() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A" }, { "B" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "C" }, { "D" } });
            Matrix<String> stacked = m1.stackHorizontally(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals("A", stacked.get(0, 0));
            assertEquals("C", stacked.get(0, 1));
            assertEquals("B", stacked.get(1, 0));
            assertEquals("D", stacked.get(1, 1));
        }

        @Test
        public void testHstack_integers() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1 }, { 2 }, { 3 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 4 }, { 5 }, { 6 } });
            Matrix<Integer> stacked = m1.stackHorizontally(m2);

            assertEquals(3, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1, stacked.get(0, 0));
            assertEquals(6, stacked.get(2, 1));
        }

        @Test
        public void testHstack_differentRowCount() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A" }, { "B" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "C" } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        @Test
        public void testZipWith_twoMatrices_sameType_integers() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
            Matrix<Integer> result = m1.zipWith(m2, (a, b) -> a + b);

            assertEquals(11, result.get(0, 0));
            assertEquals(22, result.get(0, 1));
            assertEquals(33, result.get(1, 0));
            assertEquals(44, result.get(1, 1));
        }

        @Test
        public void testZipWith_twoMatrices_sameType_strings() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "1", "2" }, { "3", "4" } });
            Matrix<String> result = m1.zipWith(m2, (a, b) -> a + b);

            assertEquals("A1", result.get(0, 0));
            assertEquals("B2", result.get(0, 1));
            assertEquals("C3", result.get(1, 0));
            assertEquals("D4", result.get(1, 1));
        }

        @Test
        public void testZipWith_twoMatrices_differentType() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> result = m1.zipWith(m2, (a, b) -> a + b, String.class);

            assertEquals("1A", result.get(0, 0));
            assertEquals("2B", result.get(0, 1));
            assertEquals("3C", result.get(1, 0));
            assertEquals("4D", result.get(1, 1));
        }

        @Test
        public void testZipWith_threeMatrices_sameType_integers() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200 } });
            Matrix<Integer> result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);

            assertEquals(111, result.get(0, 0));
            assertEquals(222, result.get(0, 1));
        }

        @Test
        public void testZipWith_threeMatrices_differentType() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 } });
            Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200 } });
            Matrix<String> result = m1.zipWith(m2, m3, (a, b, c) -> "" + a + b + c, String.class);

            assertEquals("110100", result.get(0, 0));
            assertEquals("220200", result.get(0, 1));
        }

        // ============ Stream Methods ============

        @Test
        public void testStreamLU2RD_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Integer> diagonal = m.streamMainDiagonal().toList();

            assertEquals(3, diagonal.size());
            assertEquals(1, diagonal.get(0));
            assertEquals(5, diagonal.get(1));
            assertEquals(9, diagonal.get(2));
        }

        @Test
        public void testStreamLU2RD_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<String> diagonal = m.streamMainDiagonal().toList();

            assertEquals(2, diagonal.size());
            assertEquals("A", diagonal.get(0));
            assertEquals("D", diagonal.get(1));
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.streamMainDiagonal().toList());
        }

        @Test
        public void testStreamRU2LD_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
            List<Integer> diagonal = m.streamAntiDiagonal().toList();

            assertEquals(3, diagonal.size());
            assertEquals(3, diagonal.get(0));
            assertEquals(5, diagonal.get(1));
            assertEquals(7, diagonal.get(2));
        }

        @Test
        public void testStreamRU2LD_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<String> diagonal = m.streamAntiDiagonal().toList();

            assertEquals(2, diagonal.size());
            assertEquals("B", diagonal.get(0));
            assertEquals("C", diagonal.get(1));
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
            assertThrows(IllegalStateException.class, () -> m.streamAntiDiagonal().toList());
        }

        @Test
        public void testStreamH_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<String> elements = m.streamHorizontal().toList();

            assertEquals(4, elements.size());
            assertEquals("A", elements.get(0));
            assertEquals("B", elements.get(1));
            assertEquals("C", elements.get(2));
            assertEquals("D", elements.get(3));
        }

        @Test
        public void testStreamH_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<Integer> elements = m.streamHorizontal().toList();

            assertEquals(6, elements.size());
            assertEquals(1, elements.get(0));
            assertEquals(6, elements.get(5));
        }

        @Test
        public void testStreamH_singleRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<String> row = m.streamHorizontal(1).toList();

            assertEquals(3, row.size());
            assertEquals("D", row.get(0));
            assertEquals("E", row.get(1));
            assertEquals("F", row.get(2));
        }

        @Test
        public void testStreamH_rowRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            List<String> elements = m.streamHorizontal(1, 3).toList();

            assertEquals(4, elements.size());
            assertEquals("C", elements.get(0));
            assertEquals("D", elements.get(1));
            assertEquals("E", elements.get(2));
            assertEquals("F", elements.get(3));
        }

        @Test
        public void testStreamV_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<String> elements = m.streamVertical().toList();

            assertEquals(4, elements.size());
            assertEquals("A", elements.get(0));
            assertEquals("C", elements.get(1));
            assertEquals("B", elements.get(2));
            assertEquals("D", elements.get(3));
        }

        @Test
        public void testStreamV_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<Integer> elements = m.streamVertical().toList();

            assertEquals(6, elements.size());
            assertEquals(1, elements.get(0));
            assertEquals(4, elements.get(1));
            assertEquals(2, elements.get(2));
        }

        @Test
        public void testStreamV_singleColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<String> col = m.streamVertical(1).toList();

            assertEquals(2, col.size());
            assertEquals("B", col.get(0));
            assertEquals("E", col.get(1));
        }

        @Test
        public void testStreamV_columnRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<String> elements = m.streamVertical(1, 3).toList();

            assertEquals(4, elements.size());
            assertEquals("B", elements.get(0));
            assertEquals("E", elements.get(1));
            assertEquals("C", elements.get(2));
            assertEquals("F", elements.get(3));
        }

        @Test
        public void testStreamR_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<List<String>> rows = m.streamRows().map(Stream::toList).toList();

            assertEquals(2, rows.size());
            assertEquals(2, rows.get(0).size());
            assertEquals("A", rows.get(0).get(0));
            assertEquals("B", rows.get(0).get(1));
            assertEquals("C", rows.get(1).get(0));
            assertEquals("D", rows.get(1).get(1));
        }

        @Test
        public void testStreamR_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<List<Integer>> rows = m.streamRows().map(Stream::toList).toList();

            assertEquals(2, rows.size());
            assertEquals(3, rows.get(0).size());
            assertEquals(1, rows.get(0).get(0));
            assertEquals(6, rows.get(1).get(2));
        }

        @Test
        public void testStreamR_rowRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            List<List<String>> rows = m.streamRows(1, 3).map(Stream::toList).toList();

            assertEquals(2, rows.size());
            assertEquals("C", rows.get(0).get(0));
            assertEquals("E", rows.get(1).get(0));
        }

        @Test
        public void testStreamC_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            List<List<String>> columnCount = m.streamColumns().map(Stream::toList).toList();

            assertEquals(2, columnCount.size());
            assertEquals(2, columnCount.get(0).size());
            assertEquals("A", columnCount.get(0).get(0));
            assertEquals("C", columnCount.get(0).get(1));
            assertEquals("B", columnCount.get(1).get(0));
            assertEquals("D", columnCount.get(1).get(1));
        }

        @Test
        public void testStreamC_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
            List<List<Integer>> columnCount = m.streamColumns().map(Stream::toList).toList();

            assertEquals(3, columnCount.size());
            assertEquals(2, columnCount.get(0).size());
            assertEquals(1, columnCount.get(0).get(0));
            assertEquals(6, columnCount.get(2).get(1));
        }

        @Test
        public void testStreamC_columnRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            List<List<String>> columnCount = m.streamColumns(1, 3).map(Stream::toList).toList();

            assertEquals(2, columnCount.size());
            assertEquals("B", columnCount.get(0).get(0));
            assertEquals("C", columnCount.get(1).get(0));
        }

        // ============ Println and toString ============

        @Test
        public void testPrintln_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String output = m.println();
            assertNotNull(output);
            assertTrue(output.contains("A"));
            assertTrue(output.contains("D"));
        }

        @Test
        public void testPrintln_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            String output = m.println();
            assertNotNull(output);
            assertTrue(output.contains("1"));
            assertTrue(output.contains("4"));
        }

        @Test
        public void testToString_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("A"));
            assertTrue(str.contains("D"));
        }

        @Test
        public void testToString_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("1"));
            assertTrue(str.contains("4"));
        }

        // ============ Hashcode and Equals ============

        @Test
        public void testHashCode_strings() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void testHashCode_integers() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void testEquals_same_strings() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            assertEquals(m1, m2);
        }

        @Test
        public void testEquals_same_integers() {
            Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            assertEquals(m1, m2);
        }

        @Test
        public void testEquals_different() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "X" } });
            assertNotSame(m1, m2);
        }

        // ============ Inherited Methods Tests ============

        @Test
        public void testIsEmpty() {
            Matrix<String> empty = Matrix.of(new String[0][0]);
            assertTrue(empty.isEmpty());
            Matrix<String> notEmpty = Matrix.of(new String[][] { { "A" } });
            assertFalse(notEmpty.isEmpty());
        }

        @Test
        public void testIsSameShape() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "X", "Y" }, { "Z", "W" } });
            Matrix<String> m3 = Matrix.of(new String[][] { { "A", "B", "C" } });
            assertTrue(m1.isSameShape(m2));
            assertFalse(m1.isSameShape(m3));
        }

        @Test
        public void testArray_strings() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            String[][] array = m.backingArray();
            assertArrayEquals(new String[] { "A", "B" }, array[0]);
            assertArrayEquals(new String[] { "C", "D" }, array[1]);
        }

        @Test
        public void testArray_integers() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            Integer[][] array = m.backingArray();
            assertArrayEquals(new Integer[] { 1, 2 }, array[0]);
            assertArrayEquals(new Integer[] { 3, 4 }, array[1]);
        }

        @Test
        public void testPointsLU2RD() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Stream<Point> points = m.pointsMainDiagonal();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsRU2LD() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Stream<Point> points = m.pointsAntiDiagonal();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsH() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Stream<Point> points = m.pointsHorizontal();
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsH_withRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Stream<Point> points = m.pointsHorizontal(1);
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsH_withRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
            Stream<Point> points = m.pointsHorizontal(1, 3);
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsV() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Stream<Point> points = m.pointsVertical();
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsV_withColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Stream<Point> points = m.pointsVertical(1);
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsV_withRange() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
            Stream<Point> points = m.pointsVertical(1, 3);
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsR() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Stream<Stream<Point>> points = m.pointsRows();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsC() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            Stream<Stream<Point>> points = m.pointsColumns();
            assertEquals(2, points.count());
        }

        @Test
        public void testForEach_biConsumer() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            final int[] count = { 0 };
            m.forEachIndex((i, j) -> count[0]++);
            assertEquals(4, count[0]);
        }

        @Test
        public void testForEach_biObjConsumer() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            final int[] count = { 0 };
            m.forEachIndex((i, j, matrix) -> count[0]++);
            assertEquals(4, count[0]);
        }

        @Test
        public void testAccept() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            final boolean[] called = { false };
            m.accept(matrix -> called[0] = true);
            assertTrue(called[0]);
        }

        @Test
        public void testApply() {
            Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
            int result = m.apply(matrix -> matrix.rowCount() * matrix.columnCount());
            assertEquals(4, result);
        }
    }

    @Nested
    @Tag("2512")
    class Matrix2512Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void test_constructor_withValidArray() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals("a", m.get(0, 0));
            assertEquals("d", m.get(1, 1));
        }

        @Test
        public void test_constructor_withNullArray_throwsException() {
            assertThrows(IllegalArgumentException.class, () -> new Matrix<>((String[][]) null));
        }

        @Test
        public void test_constructor_withEmptyArray() {
            Matrix<String> m = new Matrix<>(new String[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_constructor_withSingleElement() {
            Matrix<Integer> m = new Matrix<>(new Integer[][] { { 42 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42, m.get(0, 0));
        }

        // ============ Factory Method Tests ============

        @Test
        public void test_of_withValidArray() {
            String[][] arr = { { "x", "y", "z" }, { "a", "b", "c" } };
            Matrix<String> m = Matrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals("x", m.get(0, 0));
            assertEquals("c", m.get(1, 2));
        }

        @Test
        public void test_of_withSingleRow() {
            Matrix<String> m = Matrix.of(new String[][] { { "one", "two", "three" } });
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
        }

        @Test
        public void test_of_withSingleColumn() {
            Matrix<String> m = Matrix.of(new String[][] { { "a" }, { "b" }, { "c" } });
            assertEquals(3, m.rowCount());
            assertEquals(1, m.columnCount());
        }

        @Test
        public void test_mainDiagonal_createsMainDiagonal() {
            String[] diag = { "A", "B", "C" };
            Matrix<String> m = Matrix.mainDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals("A", m.get(0, 0));
            assertEquals("B", m.get(1, 1));
            assertEquals("C", m.get(2, 2));
            assertNull(m.get(0, 1));
            assertNull(m.get(1, 0));
        }

        @Test
        public void test_antiDiagonal_createsAntiDiagonal() {
            Integer[] diag = { 1, 2, 3 };
            Matrix<Integer> m = Matrix.antiDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 2));
            assertEquals(2, m.get(1, 1));
            assertEquals(3, m.get(2, 0));
            assertNull(m.get(0, 0));
        }

        @Test
        public void test_diagonal_withBothDiagonals() {
            Integer[] lu = { 1, 2, 3 };
            Integer[] ru = { 7, 8, 9 };
            Matrix<Integer> m = Matrix.diagonals(lu, ru);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(1, 1)); // Main diagonal takes precedence
            assertEquals(3, m.get(2, 2));
            assertEquals(7, m.get(0, 2));
            assertEquals(9, m.get(2, 0));
        }

        @Test
        public void test_diagonal_withDifferentLengths_throwsException() {
            Integer[] lu = { 1, 2 };
            Integer[] ru = { 3, 4, 5 };
            assertThrows(IllegalArgumentException.class, () -> Matrix.diagonals(lu, ru));
        }

        @Test
        public void test_diagonal_withBothNull_throwsException() {
            assertThrows(IllegalArgumentException.class, () -> Matrix.diagonals(null, null));
        }

        // ============ Component Type Test ============

        @Test
        public void test_componentType_returnsElementClass() {
            Matrix<String> m = Matrix.of(new String[][] { { "a" } });
            assertEquals(String.class, m.componentType());

            Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 1 } });
            assertEquals(Integer.class, m2.componentType());
        }

        // ============ Get/Set Tests ============

        @Test
        public void test_get_withIndices() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            assertEquals("a", m.get(0, 0));
            assertEquals("e", m.get(1, 1));
            assertEquals("f", m.get(1, 2));
        }

        @Test
        public void test_get_withPoint() {
            String[][] arr = { { "x", "y" }, { "z", "w" } };
            Matrix<String> m = new Matrix<>(arr);
            Point p = Point.of(1, 0);
            assertEquals("z", m.get(p));
        }

        @Test
        public void test_get_canReturnNull() {
            String[][] arr = { { "a", null }, { null, "d" } };
            Matrix<String> m = new Matrix<>(arr);
            assertNull(m.get(0, 1));
            assertNull(m.get(1, 0));
        }

        @Test
        public void test_set_withIndices() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            m.set(0, 1, "NEW");
            assertEquals("NEW", m.get(0, 1));
        }

        @Test
        public void test_set_withPoint() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Point p = Point.of(1, 1);
            m.set(p, "UPDATED");
            assertEquals("UPDATED", m.get(p));
        }

        @Test
        public void test_set_withNull() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            m.set(0, 0, null);
            assertNull(m.get(0, 0));
        }

        // ============ Directional Navigation Tests ============

        @Test
        public void test_upOf_withElementAbove() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Nullable<String> result = m.above(1, 0);
            assertTrue(result.isPresent());
            assertEquals("a", result.get());
        }

        @Test
        public void test_upOf_atTopRow_returnsEmpty() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Nullable<String> result = m.above(0, 0);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_downOf_withElementBelow() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Nullable<String> result = m.below(0, 1);
            assertTrue(result.isPresent());
            assertEquals("d", result.get());
        }

        @Test
        public void test_downOf_atBottomRow_returnsEmpty() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Nullable<String> result = m.below(1, 1);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_leftOf_withElementToLeft() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Nullable<String> result = m.left(0, 1);
            assertTrue(result.isPresent());
            assertEquals("a", result.get());
        }

        @Test
        public void test_leftOf_atLeftColumn_returnsEmpty() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Nullable<String> result = m.left(1, 0);
            assertFalse(result.isPresent());
        }

        @Test
        public void test_rightOf_withElementToRight() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Nullable<String> result = m.right(1, 0);
            assertTrue(result.isPresent());
            assertEquals("d", result.get());
        }

        @Test
        public void test_rightOf_atRightColumn_returnsEmpty() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Nullable<String> result = m.right(0, 1);
            assertFalse(result.isPresent());
        }

        // ============ Row/Column Access Tests ============

        @Test
        public void test_row_returnsCorrectRow() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            String[] row = m.rowView(1);
            assertArrayEquals(new String[] { "d", "e", "f" }, row);
        }

        @Test
        public void test_row_outOfBounds_throwsException() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(5));
        }

        @Test
        public void test_column_returnsCorrectColumn() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            String[] col = m.columnCopy(1);
            assertArrayEquals(new String[] { "b", "e" }, col);
        }

        @Test
        public void test_column_outOfBounds_throwsException() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(5));
        }

        @Test
        public void test_setRow_updatesRow() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            m.setRow(0, new String[] { "X", "Y" });
            assertEquals("X", m.get(0, 0));
            assertEquals("Y", m.get(0, 1));
        }

        @Test
        public void test_setRow_wrongLength_throwsException() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "x", "y", "z" }));
        }

        @Test
        public void test_setColumn_updatesColumn() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            m.setColumn(1, new String[] { "X", "Y" });
            assertEquals("X", m.get(0, 1));
            assertEquals("Y", m.get(1, 1));
        }

        @Test
        public void test_setColumn_wrongLength_throwsException() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "x", "y", "z" }));
        }

        @Test
        public void test_updateRow_appliesFunction() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            m.updateRow(0, x -> x * 10);
            assertEquals(10, m.get(0, 0));
            assertEquals(20, m.get(0, 1));
            assertEquals(3, m.get(1, 0));
        }

        @Test
        public void test_updateColumn_appliesFunction() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            m.updateColumn(0, String::toUpperCase);
            assertEquals("A", m.get(0, 0));
            assertEquals("C", m.get(1, 0));
            assertEquals("b", m.get(0, 1));
        }

        // ============ Diagonal Tests ============

        @Test
        public void test_getMainDiagonal_returnsMainDiagonal() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } };
            Matrix<String> m = new Matrix<>(arr);
            String[] diag = m.getMainDiagonal();
            assertArrayEquals(new String[] { "a", "e", "i" }, diag);
        }

        @Test
        public void test_getMainDiagonal_nonSquare_throwsException() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b", "c" }, { "d", "e", "f" } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void test_setMainDiagonal_setsMainDiagonal() {
            Integer[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            Matrix<Integer> m = new Matrix<>(arr);
            m.setMainDiagonal(new Integer[] { 11, 22, 33 });
            assertEquals(11, m.get(0, 0));
            assertEquals(22, m.get(1, 1));
            assertEquals(33, m.get(2, 2));
        }

        @Test
        public void test_setMainDiagonal_wrongLength_throwsException() {
            Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new Integer[] { 1, 2, 3 }));
        }

        @Test
        public void test_updateMainDiagonal_appliesFunction() {
            Integer[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            Matrix<Integer> m = new Matrix<>(arr);
            m.updateMainDiagonal(x -> x * 100);
            assertEquals(100, m.get(0, 0));
            assertEquals(500, m.get(1, 1));
            assertEquals(900, m.get(2, 2));
        }

        @Test
        public void test_getAntiDiagonal_returnsAntiDiagonal() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } };
            Matrix<String> m = new Matrix<>(arr);
            String[] diag = m.getAntiDiagonal();
            assertArrayEquals(new String[] { "c", "e", "g" }, diag);
        }

        @Test
        public void test_setAntiDiagonal_setsAntiDiagonal() {
            Integer[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            Matrix<Integer> m = new Matrix<>(arr);
            m.setAntiDiagonal(new Integer[] { 11, 22, 33 });
            assertEquals(11, m.get(0, 2));
            assertEquals(22, m.get(1, 1));
            assertEquals(33, m.get(2, 0));
        }

        @Test
        public void test_updateAntiDiagonal_appliesFunction() {
            Integer[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            Matrix<Integer> m = new Matrix<>(arr);
            m.updateAntiDiagonal(x -> x + 1000);
            assertEquals(1003, m.get(0, 2));
            assertEquals(1005, m.get(1, 1));
            assertEquals(1007, m.get(2, 0));
        }

        // ============ Update/Replace Tests ============

        @Test
        public void test_updateAll_withUnaryOperator() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            m.updateAll(x -> x * 2);
            assertEquals(2, m.get(0, 0));
            assertEquals(4, m.get(0, 1));
            assertEquals(6, m.get(1, 0));
            assertEquals(8, m.get(1, 1));
        }

        @Test
        public void test_updateAll_withBiFunction() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            m.updateAll((i, j) -> i + ":" + j);
            assertEquals("0:0", m.get(0, 0));
            assertEquals("0:1", m.get(0, 1));
            assertEquals("1:0", m.get(1, 0));
            assertEquals("1:1", m.get(1, 1));
        }

        @Test
        public void test_replaceIf_withPredicate() {
            Integer[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
            Matrix<Integer> m = new Matrix<>(arr);
            m.replaceIf(x -> x > 3, 99);
            assertEquals(1, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(99, m.get(1, 0));
            assertEquals(99, m.get(1, 2));
        }

        @Test
        public void test_replaceIf_withBiPredicate() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            m.replaceIf((i, j) -> i == j, 0);
            assertEquals(0, m.get(0, 0));
            assertEquals(2, m.get(0, 1));
            assertEquals(0, m.get(1, 1));
        }

        // ============ Map Tests ============

        @Test
        public void test_map_createsNewMatrix() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            Matrix<Integer> result = m.map(x -> x * 3);
            assertEquals(3, result.get(0, 0));
            assertEquals(6, result.get(0, 1));
            assertEquals(9, result.get(1, 0));
            assertEquals(12, result.get(1, 1));
            assertEquals(1, m.get(0, 0)); // original unchanged
        }

        @Test
        public void test_map_withTypeConversion() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            Matrix<String> result = m.map(x -> "val" + x, String.class);
            assertEquals("val1", result.get(0, 0));
            assertEquals("val4", result.get(1, 1));
        }

        @Test
        public void test_mapToBoolean_createsBooleanMatrix() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            BooleanMatrix result = m.mapToBoolean(x -> x > 2);
            assertFalse(result.get(0, 0));
            assertFalse(result.get(0, 1));
            assertTrue(result.get(1, 0));
            assertTrue(result.get(1, 1));
        }

        @Test
        public void test_mapToByte_createsByteMatrix() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            ByteMatrix result = m.mapToByte(x -> (byte) x.intValue());
            assertEquals(1, result.get(0, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void test_mapToChar_createsCharMatrix() {
            String[][] arr = { { "A", "B" }, { "C", "D" } };
            Matrix<String> m = new Matrix<>(arr);
            CharMatrix result = m.mapToChar(x -> x.charAt(0));
            assertEquals('A', result.get(0, 0));
            assertEquals('D', result.get(1, 1));
        }

        @Test
        public void test_mapToShort_createsShortMatrix() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            ShortMatrix result = m.mapToShort(x -> (short) x.intValue());
            assertEquals(1, result.get(0, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void test_mapToInt_createsIntMatrix() {
            String[][] arr = { { "1", "2" }, { "3", "4" } };
            Matrix<String> m = new Matrix<>(arr);
            IntMatrix result = m.mapToInt(Integer::parseInt);
            assertEquals(1, result.get(0, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void test_mapToLong_createsLongMatrix() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            LongMatrix result = m.mapToLong(x -> x.longValue());
            assertEquals(1L, result.get(0, 0));
            assertEquals(4L, result.get(1, 1));
        }

        @Test
        public void test_mapToFloat_createsFloatMatrix() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            FloatMatrix result = m.mapToFloat(x -> x.floatValue());
            assertEquals(1.0f, result.get(0, 0));
            assertEquals(4.0f, result.get(1, 1));
        }

        @Test
        public void test_mapToDouble_createsDoubleMatrix() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            DoubleMatrix result = m.mapToDouble(x -> x.doubleValue());
            assertEquals(1.0, result.get(0, 0));
            assertEquals(4.0, result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void test_fill_withValue() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            m.fill("X");
            assertEquals("X", m.get(0, 0));
            assertEquals("X", m.get(0, 1));
            assertEquals("X", m.get(1, 0));
            assertEquals("X", m.get(1, 1));
        }

        @Test
        public void test_fill_withArray() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            String[][] b = { { "X", "Y" }, { "Z", "W" } };
            m.copyFrom(b);
            assertEquals("X", m.get(0, 0));
            assertEquals("W", m.get(1, 1));
        }

        @Test
        public void test_fill_withArrayAndOffset() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } };
            Matrix<String> m = new Matrix<>(arr);
            String[][] b = { { "X", "Y" } };
            m.copyFrom(1, 1, b);
            assertEquals("a", m.get(0, 0));
            assertEquals("X", m.get(1, 1));
            assertEquals("Y", m.get(1, 2));
        }

        // ============ Copy Tests ============

        @Test
        public void test_copy_createsIndependentCopy() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals("a", copy.get(0, 0));

            copy.set(0, 0, "CHANGED");
            assertEquals("a", m.get(0, 0)); // original unchanged
        }

        @Test
        public void test_copy_withRowRange() {
            String[][] arr = { { "a", "b" }, { "c", "d" }, { "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals("c", copy.get(0, 0));
            assertEquals("f", copy.get(1, 1));
        }

        @Test
        public void test_copy_withFullRange() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> copy = m.copy(0, 2, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals("b", copy.get(0, 0));
            assertEquals("f", copy.get(1, 1));
        }

        // ============ Extend Tests ============

        @Test
        public void test_extend_increasesSize() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals("a", extended.get(0, 0));
            assertNull(extended.get(2, 2));
        }

        @Test
        public void test_extend_withDefaultValue() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> extended = m.resize(3, 3, "X");
            assertEquals("X", extended.get(2, 2));
        }

        @Test
        public void test_extend_withDirections() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> extended = m.extend(1, 1, 1, 1);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals("a", extended.get(1, 1));
        }

        @Test
        public void test_extend_withDirectionsAndDefault() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> extended = m.extend(1, 1, 1, 1, "Y");
            assertEquals("Y", extended.get(0, 0));
            assertEquals("a", extended.get(1, 1));
        }

        // ============ Reverse/Flip Tests ============

        @Test
        public void test_reverseH_reversesHorizontally() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            m.flipInPlaceHorizontally();
            assertEquals("c", m.get(0, 0));
            assertEquals("a", m.get(0, 2));
            assertEquals("f", m.get(1, 0));
        }

        @Test
        public void test_reverseV_reversesVertically() {
            String[][] arr = { { "a", "b" }, { "c", "d" }, { "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            m.flipInPlaceVertically();
            assertEquals("e", m.get(0, 0));
            assertEquals("f", m.get(0, 1));
            assertEquals("a", m.get(2, 0));
        }

        @Test
        public void test_flipH_createsNewHorizontallyFlipped() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> flipped = m.flipHorizontally();
            assertEquals("c", flipped.get(0, 0));
            assertEquals("a", m.get(0, 0)); // original unchanged
        }

        @Test
        public void test_flipV_createsNewVerticallyFlipped() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> flipped = m.flipVertically();
            assertEquals("c", flipped.get(0, 0));
            assertEquals("a", m.get(0, 0)); // original unchanged
        }

        // ============ Rotation Tests ============

        @Test
        public void test_rotate90_rotatesClockwise() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals("c", rotated.get(0, 0));
            assertEquals("a", rotated.get(0, 1));
            assertEquals("d", rotated.get(1, 0));
            assertEquals("b", rotated.get(1, 1));
        }

        @Test
        public void test_rotate180_rotates180Degrees() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> rotated = m.rotate180();
            assertEquals("d", rotated.get(0, 0));
            assertEquals("c", rotated.get(0, 1));
            assertEquals("b", rotated.get(1, 0));
            assertEquals("a", rotated.get(1, 1));
        }

        @Test
        public void test_rotate270_rotatesCounterClockwise() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> rotated = m.rotate270();
            assertEquals("b", rotated.get(0, 0));
            assertEquals("d", rotated.get(0, 1));
            assertEquals("a", rotated.get(1, 0));
            assertEquals("c", rotated.get(1, 1));
        }

        // ============ Transpose Test ============

        @Test
        public void test_transpose_swapsRowsAndColumns() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals("a", transposed.get(0, 0));
            assertEquals("d", transposed.get(0, 1));
            assertEquals("f", transposed.get(2, 1));
        }

        // ============ Reshape Test ============

        @Test
        public void test_reshape_changesShape() {
            Integer[][] arr = { { 1, 2, 3, 4, 5, 6 } };
            Matrix<Integer> m = new Matrix<>(arr);
            Matrix<Integer> reshaped = m.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals(1, reshaped.get(0, 0));
            assertEquals(6, reshaped.get(1, 2));
        }

        @Test
        public void test_reshape_incompatibleSize_fillsWithNull() {
            Matrix<String> m = Matrix.of(new String[][] { { "a", "b", "c" } });
            Matrix<String> reshaped = m.reshape(2, 2);
            assertEquals(2, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals("a", reshaped.get(0, 0));
            assertEquals("b", reshaped.get(0, 1));
            assertEquals("c", reshaped.get(1, 0));
            assertNull(reshaped.get(1, 1)); // Not enough elements, should be null
        }

        // ============ Repelem Test ============

        @Test
        public void test_repeatElements_repeatsElements() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> repeated = m.repeatElements(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals("a", repeated.get(0, 0));
            assertEquals("a", repeated.get(1, 1));
            assertEquals("d", repeated.get(2, 2));
        }

        // ============ Repmat Test ============

        @Test
        public void test_repeatMatrix_repeatsMatrix() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Matrix<String> repeated = m.repeatMatrix(2, 2);
            assertEquals(4, repeated.rowCount());
            assertEquals(4, repeated.columnCount());
            assertEquals("a", repeated.get(0, 0));
            assertEquals("a", repeated.get(2, 2));
            assertEquals("d", repeated.get(3, 3));
        }

        // ============ Flatten Tests ============

        @Test
        public void test_flatten_returnsAllElements() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            List<String> flattened = m.flatten();
            assertEquals(6, flattened.size());
            assertEquals("a", flattened.get(0));
            assertEquals("f", flattened.get(5));
        }

        @Test
        public void test_applyOnFlattened_appliesOperationToEachRow() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            final int[] sum = { 0 };
            m.applyOnFlattened(row -> {
                for (Integer val : row) {
                    sum[0] += val;
                }
            });
            assertEquals(10, sum[0]);
        }

        // ============ Stack Tests ============

        @Test
        public void test_vstack_stacksVertically() {
            String[][] arr1 = { { "a", "b" } };
            String[][] arr2 = { { "c", "d" } };
            Matrix<String> m1 = new Matrix<>(arr1);
            Matrix<String> m2 = new Matrix<>(arr2);
            Matrix<String> stacked = m1.stackVertically(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals("a", stacked.get(0, 0));
            assertEquals("c", stacked.get(1, 0));
        }

        @Test
        public void test_vstack_incompatibleColumns_throwsException() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "a", "b" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "c", "d", "e" } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void test_hstack_stacksHorizontally() {
            String[][] arr1 = { { "a" }, { "b" } };
            String[][] arr2 = { { "c" }, { "d" } };
            Matrix<String> m1 = new Matrix<>(arr1);
            Matrix<String> m2 = new Matrix<>(arr2);
            Matrix<String> stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals("a", stacked.get(0, 0));
            assertEquals("c", stacked.get(0, 1));
        }

        @Test
        public void test_hstack_incompatibleRows_throwsException() {
            Matrix<String> m1 = Matrix.of(new String[][] { { "a" } });
            Matrix<String> m2 = Matrix.of(new String[][] { { "b" }, { "c" } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Zip Tests ============

        @Test
        public void test_zipWith_withTwoMatrices() {
            Integer[][] arr1 = { { 1, 2 }, { 3, 4 } };
            Integer[][] arr2 = { { 5, 6 }, { 7, 8 } };
            Matrix<Integer> m1 = new Matrix<>(arr1);
            Matrix<Integer> m2 = new Matrix<>(arr2);
            Matrix<Integer> result = m1.zipWith(m2, (a, b) -> a + b);
            assertEquals(6, result.get(0, 0));
            assertEquals(12, result.get(1, 1));
        }

        // ============ Stream Tests ============

        @Test
        public void test_streamMainDiagonal_streamsDiagonal() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } };
            Matrix<String> m = new Matrix<>(arr);
            List<String> result = m.streamMainDiagonal().toList();
            assertEquals(Arrays.asList("a", "e", "i"), result);
        }

        @Test
        public void test_streamAntiDiagonal_streamsAntiDiagonal() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } };
            Matrix<String> m = new Matrix<>(arr);
            List<String> result = m.streamAntiDiagonal().toList();
            assertEquals(Arrays.asList("c", "e", "g"), result);
        }

        @Test
        public void test_streamH_streamsAllElements() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            List<String> result = m.streamHorizontal().toList();
            assertEquals(Arrays.asList("a", "b", "c", "d"), result);
        }

        @Test
        public void test_streamH_withRowIndex() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            List<String> result = m.streamHorizontal(1).toList();
            assertEquals(Arrays.asList("d", "e", "f"), result);
        }

        @Test
        public void test_streamH_withRowRange() {
            String[][] arr = { { "a", "b" }, { "c", "d" }, { "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            List<String> result = m.streamHorizontal(1, 3).toList();
            assertEquals(Arrays.asList("c", "d", "e", "f"), result);
        }

        @Test
        public void test_streamV_streamsAllElementsVertically() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            List<String> result = m.streamVertical().toList();
            assertEquals(Arrays.asList("a", "c", "b", "d"), result);
        }

        @Test
        public void test_streamV_withColumnIndex() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            List<String> result = m.streamVertical(1).toList();
            assertEquals(Arrays.asList("b", "e"), result);
        }

        @Test
        public void test_streamV_withColumnRange() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            List<String> result = m.streamVertical(1, 3).toList();
            assertEquals(Arrays.asList("b", "e", "c", "f"), result);
        }

        @Test
        public void test_streamR_streamsRows() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Stream<Stream<String>> streams = m.streamRows();
            assertEquals(2, streams.count());
        }

        @Test
        public void test_streamR_withRange() {
            String[][] arr = { { "a", "b" }, { "c", "d" }, { "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            Stream<Stream<String>> streams = m.streamRows(1, 3);
            assertEquals(2, streams.count());
        }

        @Test
        public void test_streamC_streamsColumns() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Stream<Stream<String>> streams = m.streamColumns();
            assertEquals(2, streams.count());
        }

        @Test
        public void test_streamC_withRange() {
            String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
            Matrix<String> m = new Matrix<>(arr);
            Stream<Stream<String>> streams = m.streamColumns(1, 3);
            assertEquals(2, streams.count());
        }

        // ============ ForEach Test ============

        @Test
        public void test_forEach_iteratesAllElements() {
            Integer[][] arr = { { 1, 2 }, { 3, 4 } };
            Matrix<Integer> m = new Matrix<>(arr);
            final int[] sum = { 0 };
            m.forEach(val -> sum[0] += val);
            assertEquals(10, sum[0]);
        }

        // ============ Dataset Conversion Tests ============

        @Test
        public void test_toDatasetH_convertsToDataset() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Dataset ds = m.toRowDataset(Arrays.asList("col1", "col2"));
            assertNotNull(ds);
            assertEquals(2, ds.size());
        }

        @Test
        public void test_toDatasetV_convertsToDataset() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            Dataset ds = m.toColumnDataset(Arrays.asList("col1", "col2"));
            assertNotNull(ds);
            assertEquals(2, ds.size());
        }

        // ============ Utility Tests ============

        @Test
        public void test_println_returnsString() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            String result = m.println();
            assertNotNull(result);
            assertTrue(result.length() > 0);
        }

        @Test
        public void test_hashCode_consistentWithEquals() {
            String[][] arr1 = { { "a", "b" }, { "c", "d" } };
            String[][] arr2 = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m1 = new Matrix<>(arr1);
            Matrix<String> m2 = new Matrix<>(arr2);
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void test_equals_sameContent() {
            String[][] arr1 = { { "a", "b" }, { "c", "d" } };
            String[][] arr2 = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m1 = new Matrix<>(arr1);
            Matrix<String> m2 = new Matrix<>(arr2);
            assertEquals(m1, m2);
        }

        @Test
        public void test_equals_differentContent() {
            String[][] arr1 = { { "a", "b" }, { "c", "d" } };
            String[][] arr2 = { { "a", "b" }, { "c", "X" } };
            Matrix<String> m1 = new Matrix<>(arr1);
            Matrix<String> m2 = new Matrix<>(arr2);
            assertNotEquals(m1, m2);
        }

        @Test
        public void test_equals_differentDimensions() {
            String[][] arr1 = { { "a", "b" }, { "c", "d" } };
            String[][] arr2 = { { "a", "b", "c" } };
            Matrix<String> m1 = new Matrix<>(arr1);
            Matrix<String> m2 = new Matrix<>(arr2);
            assertNotEquals(m1, m2);
        }

        @Test
        public void test_toString_returnsStringRepresentation() {
            String[][] arr = { { "a", "b" }, { "c", "d" } };
            Matrix<String> m = new Matrix<>(arr);
            String result = m.toString();
            assertNotNull(result);
            assertTrue(result.contains("a"));
            assertTrue(result.contains("d"));
        }
    }

    // Placed here because this parity test treats Matrix as the generic reference surface across matrix implementations.
    @Nested
    class MatrixParityFuzzTest extends TestBase {

        @Test
        public void testIntAndGenericMatrixParity() {
            final Random random = new Random(123456789L);

            for (int n = 0; n < 400; n++) {
                final int rows = random.nextInt(5);
                final int cols = rows == 0 ? 0 : random.nextInt(5);
                final int[][] source = new int[rows][cols];
                final Integer[][] boxedSource = new Integer[rows][cols];

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        final int v = random.nextInt(101) - 50;
                        source[i][j] = v;
                        boxedSource[i][j] = v;
                    }
                }

                final IntMatrix intMatrix = IntMatrix.of(source);
                final Matrix<Integer> genericMatrix = Matrix.of(boxedSource);

                assertEquals(intMatrix.rowCount(), genericMatrix.rowCount());
                assertEquals(intMatrix.columnCount(), genericMatrix.columnCount());
                assertEquals(intMatrix.elementCount(), genericMatrix.elementCount());
                assertEquals(intMatrix.isEmpty(), genericMatrix.isEmpty());

                assertArrayEquals(intMatrix.streamHorizontal().toArray(), toIntArray(genericMatrix.streamHorizontal().toList()));
                assertArrayEquals(intMatrix.streamVertical().toArray(), toIntArray(genericMatrix.streamVertical().toList()));

                final List<int[]> intRows = intMatrix.streamRows().map(IntStream::toArray).toList();
                final List<int[]> genericRows = genericMatrix.streamRows().map(row -> toIntArray(row.toList())).toList();
                assertEquals(intRows.size(), genericRows.size());
                for (int i = 0; i < intRows.size(); i++) {
                    assertArrayEquals(intRows.get(i), genericRows.get(i));
                }

                final List<int[]> intCols = intMatrix.streamColumns().map(IntStream::toArray).toList();
                final List<int[]> genericCols = genericMatrix.streamColumns().map(col -> toIntArray(col.toList())).toList();
                assertEquals(intCols.size(), genericCols.size());
                for (int i = 0; i < intCols.size(); i++) {
                    assertArrayEquals(intCols.get(i), genericCols.get(i));
                }

                assertTransformParity(intMatrix::transpose, genericMatrix::transpose);
                assertTransformParity(intMatrix::rotate90, genericMatrix::rotate90);
                assertMatrixEquals(intMatrix.rotate180(), genericMatrix.rotate180());
                assertTransformParity(intMatrix::rotate270, genericMatrix::rotate270);
                assertMatrixEquals(intMatrix.flipHorizontally(), genericMatrix.flipHorizontally());
                assertMatrixEquals(intMatrix.flipVertically(), genericMatrix.flipVertically());

                final int rowRepeats = random.nextInt(3) + 1;
                final int columnRepeats = random.nextInt(3) + 1;
                assertMatrixEquals(intMatrix.repeatElements(rowRepeats, columnRepeats), genericMatrix.repeatElements(rowRepeats, columnRepeats));
                assertMatrixEquals(intMatrix.repeatMatrix(rowRepeats, columnRepeats), genericMatrix.repeatMatrix(rowRepeats, columnRepeats));

                final int elements = (int) intMatrix.elementCount();
                int newRows = 0;
                int newCols = 0;

                if (elements == 0) {
                    newRows = random.nextInt(5);
                    newCols = newRows == 0 ? 0 : random.nextInt(5);
                } else {
                    int divisor;
                    do {
                        divisor = random.nextInt(elements) + 1;
                    } while (elements % divisor != 0);

                    newRows = divisor;
                    newCols = elements / divisor;
                }

                final IntMatrix reshapedInt = intMatrix.reshape(newRows, newCols);
                final Matrix<Integer> reshapedGeneric = genericMatrix.reshape(newRows, newCols);

                if (elements == 0) {
                    assertEquals(reshapedInt.rowCount(), reshapedGeneric.rowCount());
                    assertEquals(reshapedInt.columnCount(), reshapedGeneric.columnCount());
                } else {
                    assertMatrixEquals(reshapedInt, reshapedGeneric);
                }
            }
        }

        private static int[] toIntArray(final List<Integer> list) {
            final int[] a = new int[list.size()];

            for (int i = 0; i < list.size(); i++) {
                a[i] = list.get(i);
            }

            return a;
        }

        private static void assertTransformParity(final java.util.function.Supplier<IntMatrix> intTransform,
                final java.util.function.Supplier<Matrix<Integer>> genericTransform) {
            try {
                final IntMatrix intResult = intTransform.get();
                final Matrix<Integer> genericResult = genericTransform.get();
                assertMatrixEquals(intResult, genericResult);
            } catch (final IllegalArgumentException e) {
                try {
                    genericTransform.get();
                } catch (final IllegalArgumentException expected) {
                    return;
                }

                throw e;
            }
        }

        private static void assertMatrixEquals(final IntMatrix intMatrix, final Matrix<Integer> genericMatrix) {
            assertEquals(intMatrix.rowCount(), genericMatrix.rowCount());
            assertEquals(intMatrix.columnCount(), genericMatrix.columnCount());

            for (int i = 0; i < intMatrix.rowCount(); i++) {
                for (int j = 0; j < intMatrix.columnCount(); j++) {
                    assertEquals(intMatrix.get(i, j), genericMatrix.get(i, j).intValue());
                }
            }
        }
    }

    @Nested
    class MatrixZeroColumnRowStreamTest_Matrix extends TestBase {
        @Test
        public void testGenericMatrixRowsForZeroColumnMatrix() {
            final Matrix<Integer> matrix = Matrix.of(new Integer[][] { {}, {}, {} });
            final List<List<Integer>> rows = matrix.streamRows().map(row -> row.toList()).toList();

            assertEquals(3, rows.size());
            assertTrue(rows.get(0).isEmpty());
            assertTrue(rows.get(1).isEmpty());
            assertTrue(rows.get(2).isEmpty());
        }
    }

    @Nested
    class PrimitiveMatrixUpdateAllNullValidationTest_Matrix extends TestBase {
        @Test
        public void testObjectMatrixStackRejectsNullMatrix() {
            Matrix<Integer> matrix = Matrix.of(new Integer[][] { { 1 } });
            assertThrows(IllegalArgumentException.class, () -> matrix.stackVertically(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.stackHorizontally(null));
        }
    }

}
