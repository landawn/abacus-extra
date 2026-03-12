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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalDouble;
import com.landawn.abacus.util.stream.DoubleStream;
import com.landawn.abacus.util.stream.Stream;

class DoubleMatrixTest extends TestBase {

    @Test
    public void testConstructor() {
        // Test with null array
        DoubleMatrix matrix1 = new DoubleMatrix(null);
        assertEquals(0, matrix1.rowCount());
        assertEquals(0, matrix1.columnCount());

        // Test with valid array
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix2 = new DoubleMatrix(arr);
        assertEquals(2, matrix2.rowCount());
        assertEquals(2, matrix2.columnCount());
    }

    @Test
    public void testEmpty() {
        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testOf() {
        // Test with null/empty
        DoubleMatrix empty = DoubleMatrix.of((double[][]) null);
        assertTrue(empty.isEmpty());

        // Test with valid array
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);
        assertEquals(2, matrix.rowCount());
        assertEquals(2, matrix.columnCount());
    }

    @Test
    public void testCreateFromInt() {
        int[][] intArr = { { 1, 2 }, { 3, 4 } };
        DoubleMatrix matrix = DoubleMatrix.from(intArr);
        assertEquals(2, matrix.rowCount());
        assertEquals(2, matrix.columnCount());
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(4.0, matrix.get(1, 1));

        // Test empty
        assertTrue(DoubleMatrix.from((int[][]) null).isEmpty());
    }

    @Test
    public void testCreateFromLong() {
        long[][] longArr = { { 1L, 2L }, { 3L, 4L } };
        DoubleMatrix matrix = DoubleMatrix.from(longArr);
        assertEquals(2, matrix.rowCount());
        assertEquals(2, matrix.columnCount());
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(4.0, matrix.get(1, 1));
    }

    @Test
    public void testCreateFromFloat() {
        float[][] floatArr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        DoubleMatrix matrix = DoubleMatrix.from(floatArr);
        assertEquals(2, matrix.rowCount());
        assertEquals(2, matrix.columnCount());
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(4.0, matrix.get(1, 1));
    }

    @Test
    public void testRandom() {
        DoubleMatrix matrix = DoubleMatrix.random(5);
        assertEquals(1, matrix.rowCount());
        assertEquals(5, matrix.columnCount());
    }

    @Test
    public void testRepeat() {
        DoubleMatrix matrix = DoubleMatrix.repeat(1, 5, 3.14);
        assertEquals(1, matrix.rowCount());
        assertEquals(5, matrix.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(3.14, matrix.get(0, i));
        }
    }

    @Test
    public void testDiagonalLU2RD() {
        double[] diagonal = { 1.0, 2.0, 3.0 };
        DoubleMatrix matrix = DoubleMatrix.mainDiagonal(diagonal);
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(2.0, matrix.get(1, 1));
        assertEquals(3.0, matrix.get(2, 2));
        assertEquals(0.0, matrix.get(0, 1));
    }

    @Test
    public void testDiagonalRU2LD() {
        double[] diagonal = { 1.0, 2.0, 3.0 };
        DoubleMatrix matrix = DoubleMatrix.antiDiagonal(diagonal);
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1.0, matrix.get(0, 2));
        assertEquals(2.0, matrix.get(1, 1));
        assertEquals(3.0, matrix.get(2, 0));
    }

    @Test
    public void testDiagonal() {
        double[] mainDiag = { 1.0, 2.0, 3.0 };
        double[] antiDiag = { 7.0, 8.0, 9.0 };
        DoubleMatrix matrix = DoubleMatrix.diagonals(mainDiag, antiDiag);
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(7.0, matrix.get(0, 2));

        // Test with different lengths
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.diagonals(new double[] { 1.0 }, new double[] { 2.0, 3.0 }));
    }

    @Test
    public void testUnbox() {
        Double[][] boxed = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        Matrix<Double> boxedMatrix = Matrix.of(boxed);
        DoubleMatrix unboxed = DoubleMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rowCount());
        assertEquals(2, unboxed.columnCount());
        assertEquals(1.0, unboxed.get(0, 0));
        assertEquals(4.0, unboxed.get(1, 1));
    }

    @Test
    public void testComponentType() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0 } });
        assertEquals(double.class, matrix.componentType());
    }

    @Test
    public void testGet() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(2.0, matrix.get(0, 1));
        assertEquals(3.0, matrix.get(1, 0));
        assertEquals(4.0, matrix.get(1, 1));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(2, 0));
    }

    @Test
    public void testGetWithPoint() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);
        assertEquals(1.0, matrix.get(Point.of(0, 0)));
        assertEquals(4.0, matrix.get(Point.of(1, 1)));
    }

    @Test
    public void testSet() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);
        matrix.set(0, 0, 5.0);
        assertEquals(5.0, matrix.get(0, 0));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.set(2, 0, 5.0));
    }

    @Test
    public void testSetWithPoint() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);
        matrix.set(Point.of(0, 0), 5.0);
        assertEquals(5.0, matrix.get(Point.of(0, 0)));
    }

    @Test
    public void testUpOf() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertTrue(matrix.above(0, 0).isEmpty());
        assertEquals(1.0, matrix.above(1, 0).orElse(0.0));
    }

    @Test
    public void testDownOf() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertEquals(3.0, matrix.below(0, 0).orElse(0.0));
        assertTrue(matrix.below(1, 0).isEmpty());
    }

    @Test
    public void testLeftOf() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertTrue(matrix.left(0, 0).isEmpty());
        assertEquals(1.0, matrix.left(0, 1).orElse(0.0));
    }

    @Test
    public void testRightOf() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertEquals(2.0, matrix.right(0, 0).orElse(0.0));
        assertTrue(matrix.right(0, 1).isEmpty());
    }

    @Test
    public void testRow() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] row = matrix.rowView(0);
        assertEquals(2, row.length);
        assertEquals(1.0, row[0]);
        assertEquals(2.0, row[1]);

        assertThrows(IllegalArgumentException.class, () -> matrix.rowView(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.rowView(2));
    }

    @Test
    public void testColumn() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] col = matrix.columnCopy(0);
        assertEquals(2, col.length);
        assertEquals(1.0, col[0]);
        assertEquals(3.0, col[1]);

        assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.columnCopy(2));
    }

    @Test
    public void testSetRow() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.setRow(0, new double[] { 5.0, 6.0 });
        assertEquals(5.0, matrix.get(0, 0));
        assertEquals(6.0, matrix.get(0, 1));

        assertThrows(IllegalArgumentException.class, () -> matrix.setRow(0, new double[] { 1.0 }));
    }

    @Test
    public void testSetColumn() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.setColumn(0, new double[] { 5.0, 6.0 });
        assertEquals(5.0, matrix.get(0, 0));
        assertEquals(6.0, matrix.get(1, 0));

        assertThrows(IllegalArgumentException.class, () -> matrix.setColumn(0, new double[] { 1.0 }));
    }

    @Test
    public void testUpdateRow() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.updateRow(0, d -> d * 2);
        assertEquals(2.0, matrix.get(0, 0));
        assertEquals(4.0, matrix.get(0, 1));
    }

    @Test
    public void testUpdateColumn() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.updateColumn(0, d -> d * 2);
        assertEquals(2.0, matrix.get(0, 0));
        assertEquals(6.0, matrix.get(1, 0));
    }

    @Test
    public void testUpdateRowAndUpdateColumnInvalidIndex() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateRow(-1, d -> d * 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.updateColumn(2, d -> d * 2));
    }

    @Test
    public void testUpdateRowAndUpdateColumnNullOperator() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertThrows(IllegalArgumentException.class, () -> matrix.updateRow(0, (Throwables.DoubleUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.updateColumn(0, (Throwables.DoubleUnaryOperator<RuntimeException>) null));
    }

    @Test
    public void testGetLU2RD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] diag = matrix.getMainDiagonal();
        assertEquals(2, diag.length);
        assertEquals(1.0, diag[0]);
        assertEquals(4.0, diag[1]);

        // Test non-square matrix
        DoubleMatrix nonSquare = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getMainDiagonal());
    }

    @Test
    public void testSetLU2RD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.setMainDiagonal(new double[] { 5.0, 6.0 });
        assertEquals(5.0, matrix.get(0, 0));
        assertEquals(6.0, matrix.get(1, 1));

        assertThrows(IllegalArgumentException.class, () -> matrix.setMainDiagonal(new double[] { 1.0 }));
    }

    @Test
    public void testUpdateLU2RD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.updateMainDiagonal(d -> d * 2);
        assertEquals(2.0, matrix.get(0, 0));
        assertEquals(8.0, matrix.get(1, 1));
    }

    @Test
    public void testGetRU2LD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] diag = matrix.getAntiDiagonal();
        assertEquals(2, diag.length);
        assertEquals(2.0, diag[0]);
        assertEquals(3.0, diag[1]);
    }

    @Test
    public void testSetRU2LD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.setAntiDiagonal(new double[] { 5.0, 6.0 });
        assertEquals(5.0, matrix.get(0, 1));
        assertEquals(6.0, matrix.get(1, 0));

        assertThrows(IllegalArgumentException.class, () -> matrix.setAntiDiagonal(new double[] { 1.0 }));
    }

    @Test
    public void testUpdateRU2LD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.updateAntiDiagonal(d -> d * 2);
        assertEquals(4.0, matrix.get(0, 1));
        assertEquals(6.0, matrix.get(1, 0));
    }

    @Test
    public void testUpdateAll() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.updateAll(d -> d * 2);
        assertEquals(2.0, matrix.get(0, 0));
        assertEquals(4.0, matrix.get(0, 1));
        assertEquals(6.0, matrix.get(1, 0));
        assertEquals(8.0, matrix.get(1, 1));
    }

    @Test
    public void testUpdateAllWithIndices() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.updateAll((i, j) -> (double) (i * 10 + j));
        assertEquals(0.0, matrix.get(0, 0));
        assertEquals(1.0, matrix.get(0, 1));
        assertEquals(10.0, matrix.get(1, 0));
        assertEquals(11.0, matrix.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.replaceIf(d -> d < 3.0, 0.0);
        assertEquals(0.0, matrix.get(0, 0));
        assertEquals(0.0, matrix.get(0, 1));
        assertEquals(3.0, matrix.get(1, 0));
        assertEquals(4.0, matrix.get(1, 1));
    }

    @Test
    public void testReplaceIfWithIndices() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.replaceIf((i, j) -> i == j, 0.0);
        assertEquals(0.0, matrix.get(0, 0));
        assertEquals(2.0, matrix.get(0, 1));
        assertEquals(3.0, matrix.get(1, 0));
        assertEquals(0.0, matrix.get(1, 1));
    }

    @Test
    public void testMap() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix mapped = matrix.map(d -> d * 2);
        assertEquals(2.0, mapped.get(0, 0));
        assertEquals(4.0, mapped.get(0, 1));
        assertEquals(6.0, mapped.get(1, 0));
        assertEquals(8.0, mapped.get(1, 1));
    }

    @Test
    public void testMapNullMapper() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertThrows(IllegalArgumentException.class, () -> matrix.map((Throwables.DoubleUnaryOperator<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.mapToObj((Throwables.DoubleFunction<String, RuntimeException>) null, String.class));
    }

    @Test
    public void testMapToInt() {
        double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        IntMatrix mapped = matrix.mapToInt(d -> (int) Math.round(d));
        assertEquals(2, mapped.get(0, 0));
        assertEquals(3, mapped.get(0, 1));
        assertEquals(4, mapped.get(1, 0));
        assertEquals(5, mapped.get(1, 1));
    }

    @Test
    public void testMapToLong() {
        double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        LongMatrix mapped = matrix.mapToLong(d -> Math.round(d));
        assertEquals(2L, mapped.get(0, 0));
        assertEquals(3L, mapped.get(0, 1));
        assertEquals(4L, mapped.get(1, 0));
        assertEquals(5L, mapped.get(1, 1));
    }

    @Test
    public void testMapToObj() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        Matrix<String> mapped = matrix.mapToObj(d -> String.format("%.1f", d), String.class);
        assertEquals("1.0", mapped.get(0, 0));
        assertEquals("2.0", mapped.get(0, 1));
        assertEquals("3.0", mapped.get(1, 0));
        assertEquals("4.0", mapped.get(1, 1));
    }

    @Test
    public void testFill() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.fill(5.0);
        assertEquals(5.0, matrix.get(0, 0));
        assertEquals(5.0, matrix.get(0, 1));
        assertEquals(5.0, matrix.get(1, 0));
        assertEquals(5.0, matrix.get(1, 1));
    }

    @Test
    public void testFillWithArray() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[][] fillArr = { { 5.0, 6.0 }, { 7.0, 8.0 } };
        matrix.copyFrom(fillArr);
        assertEquals(5.0, matrix.get(0, 0));
        assertEquals(6.0, matrix.get(0, 1));
        assertEquals(7.0, matrix.get(1, 0));
        assertEquals(8.0, matrix.get(1, 1));
    }

    @Test
    public void testFillWithIndices() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);
        double[][] fillArr = { { 7.0, 8.0 } };
        matrix.copyFrom(0, 1, fillArr);
        assertEquals(7.0, matrix.get(0, 1));
        assertEquals(8.0, matrix.get(0, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.copyFrom(-1, 0, fillArr));
    }

    @Test
    public void testCopy() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix copy = matrix.copy();
        assertEquals(matrix.rowCount(), copy.rowCount());
        assertEquals(matrix.columnCount(), copy.columnCount());
        assertEquals(1.0, copy.get(0, 0));

        // Ensure it's a deep copy
        copy.set(0, 0, 5.0);
        assertEquals(1.0, matrix.get(0, 0));
    }

    @Test
    public void testCopyWithRowRange() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix copy = matrix.copy(0, 2);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(1.0, copy.get(0, 0));
        assertEquals(3.0, copy.get(1, 0));

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(-1, 2));
    }

    @Test
    public void testCopyWithFullRange() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix copy = matrix.copy(0, 2, 1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(2.0, copy.get(0, 0));
        assertEquals(5.0, copy.get(1, 0));
    }

    @Test
    public void testExtend() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix extended = matrix.resize(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(0.0, extended.get(2, 2));
    }

    @Test
    public void testExtendWithDefault() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix extended = matrix.resize(3, 3, -1.0);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(-1.0, extended.get(2, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.resize(-1, 3, -1.0));
    }

    @Test
    public void testExtendWithDirections() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix extended = matrix.extend(1, 1, 1, 1);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(0.0, extended.get(0, 0));
    }

    @Test
    public void testExtendWithDirectionsAndDefault() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix extended = matrix.extend(1, 1, 1, 1, -1.0);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(-1.0, extended.get(0, 0));

        assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 1, 1, 1, -1.0));
    }

    @Test
    public void testReverseH() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.flipInPlaceHorizontally();
        assertEquals(2.0, matrix.get(0, 0));
        assertEquals(1.0, matrix.get(0, 1));
    }

    @Test
    public void testReverseV() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.flipInPlaceVertically();
        assertEquals(3.0, matrix.get(0, 0));
        assertEquals(1.0, matrix.get(1, 0));
    }

    @Test
    public void testFlipH() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix flipped = matrix.flipHorizontally();
        assertEquals(2.0, flipped.get(0, 0));
        assertEquals(1.0, flipped.get(0, 1));

        // Original unchanged
        assertEquals(1.0, matrix.get(0, 0));
    }

    @Test
    public void testFlipV() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix flipped = matrix.flipVertically();
        assertEquals(3.0, flipped.get(0, 0));
        assertEquals(1.0, flipped.get(1, 0));

        // Original unchanged
        assertEquals(1.0, matrix.get(0, 0));
    }

    @Test
    public void testRotate90() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix rotated = matrix.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3.0, rotated.get(0, 0));
        assertEquals(1.0, rotated.get(0, 1));
    }

    @Test
    public void testRotate180() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix rotated = matrix.rotate180();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(4.0, rotated.get(0, 0));
        assertEquals(3.0, rotated.get(0, 1));
    }

    @Test
    public void testRotate270() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix rotated = matrix.rotate270();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(2.0, rotated.get(0, 0));
        assertEquals(4.0, rotated.get(0, 1));
    }

    @Test
    public void testTranspose() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix transposed = matrix.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1.0, transposed.get(0, 0));
        assertEquals(4.0, transposed.get(0, 1));
    }

    @Test
    public void testReshape() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix reshaped = matrix.reshape(1, 4);
        assertEquals(1, reshaped.rowCount());
        assertEquals(4, reshaped.columnCount());
        assertEquals(1.0, reshaped.get(0, 0));
        assertEquals(2.0, reshaped.get(0, 1));
        assertEquals(3.0, reshaped.get(0, 2));
        assertEquals(4.0, reshaped.get(0, 3));

        // Test reshape with too-small dimensions throws exception
        assertThrows(IllegalArgumentException.class, () -> matrix.reshape(1, 3));
        assertThrows(IllegalArgumentException.class, () -> matrix.reshape(1, 1));
    }

    @Test
    public void testRepelem() {
        double[][] arr = { { 1.0, 2.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix repeated = matrix.repeatElements(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertEquals(1.0, repeated.get(0, 0));
        assertEquals(1.0, repeated.get(0, 1));
        assertEquals(1.0, repeated.get(0, 2));
        assertEquals(2.0, repeated.get(0, 3));

        assertThrows(IllegalArgumentException.class, () -> matrix.repeatElements(0, 1));
    }

    @Test
    public void testRepmat() {
        double[][] arr = { { 1.0, 2.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix tiled = matrix.repeatMatrix(2, 3);
        assertEquals(2, tiled.rowCount());
        assertEquals(6, tiled.columnCount());
        assertEquals(1.0, tiled.get(0, 0));
        assertEquals(2.0, tiled.get(0, 1));
        assertEquals(1.0, tiled.get(0, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.repeatMatrix(0, 1));
    }

    @Test
    public void testFlatten() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleList flat = matrix.flatten();
        assertEquals(4, flat.size());
        assertEquals(1.0, flat.get(0));
        assertEquals(2.0, flat.get(1));
        assertEquals(3.0, flat.get(2));
        assertEquals(4.0, flat.get(3));
    }

    @Test
    public void testFlatOp() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        int[] count = { 0 };
        matrix.applyOnFlattened(array -> count[0] += array.length);
        assertEquals(4, count[0]);
    }

    @Test
    public void testVstack() {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 3.0, 4.0 } });

        DoubleMatrix stacked = a.stackVertically(b);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1.0, stacked.get(0, 0));
        assertEquals(3.0, stacked.get(1, 0));

        DoubleMatrix c = DoubleMatrix.of(new double[][] { { 5.0 } });
        assertThrows(IllegalArgumentException.class, () -> a.stackVertically(c));
    }

    @Test
    public void testHstack() {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0 }, { 3.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 2.0 }, { 4.0 } });

        DoubleMatrix stacked = a.stackHorizontally(b);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1.0, stacked.get(0, 0));
        assertEquals(2.0, stacked.get(0, 1));

        DoubleMatrix c = DoubleMatrix.of(new double[][] { { 5.0 } });
        assertThrows(IllegalArgumentException.class, () -> a.stackHorizontally(c));
    }

    @Test
    public void testAdd() {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });

        DoubleMatrix sum = a.add(b);
        assertEquals(6.0, sum.get(0, 0));
        assertEquals(8.0, sum.get(0, 1));
        assertEquals(10.0, sum.get(1, 0));
        assertEquals(12.0, sum.get(1, 1));

        DoubleMatrix c = DoubleMatrix.of(new double[][] { { 1.0 } });
        assertThrows(IllegalArgumentException.class, () -> a.add(c));
    }

    @Test
    public void testSubtract() {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });

        DoubleMatrix diff = a.subtract(b);
        assertEquals(4.0, diff.get(0, 0));
        assertEquals(4.0, diff.get(0, 1));
        assertEquals(4.0, diff.get(1, 0));
        assertEquals(4.0, diff.get(1, 1));

        DoubleMatrix c = DoubleMatrix.of(new double[][] { { 1.0 } });
        assertThrows(IllegalArgumentException.class, () -> a.subtract(c));
    }

    @Test
    public void testMultiply() {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });

        DoubleMatrix product = a.multiply(b);
        assertEquals(19.0, product.get(0, 0));
        assertEquals(22.0, product.get(0, 1));
        assertEquals(43.0, product.get(1, 0));
        assertEquals(50.0, product.get(1, 1));

        DoubleMatrix c = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
        assertThrows(IllegalArgumentException.class, () -> a.multiply(c));
    }

    @Test
    public void testBoxed() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        Matrix<Double> boxed = matrix.boxed();
        assertEquals(2, boxed.rowCount());
        assertEquals(2, boxed.columnCount());
        assertEquals(Double.valueOf(1.0), boxed.get(0, 0));
        assertEquals(Double.valueOf(4.0), boxed.get(1, 1));
    }

    @Test
    public void testZipWith() {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });

        DoubleMatrix max = a.zipWith(b, Math::max);
        assertEquals(5.0, max.get(0, 0));
        assertEquals(6.0, max.get(0, 1));
        assertEquals(7.0, max.get(1, 0));
        assertEquals(8.0, max.get(1, 1));

        DoubleMatrix c = DoubleMatrix.of(new double[][] { { 1.0 } });
        assertThrows(IllegalArgumentException.class, () -> a.zipWith(c, Math::max));
    }

    @Test
    public void testZipWithThree() {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 3.0, 4.0 } });
        DoubleMatrix c = DoubleMatrix.of(new double[][] { { 5.0, 6.0 } });

        DoubleMatrix result = a.zipWith(b, c, (x, y, z) -> x + y * z);
        assertEquals(16.0, result.get(0, 0));
        assertEquals(26.0, result.get(0, 1));

        DoubleMatrix d = DoubleMatrix.of(new double[][] { { 1.0 } });
        assertThrows(IllegalArgumentException.class, () -> a.zipWith(b, d, (x, y, z) -> x + y + z));
    }

    @Test
    public void testStreamH() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] elements = matrix.streamHorizontal().toArray();
        assertEquals(4, elements.length);
        assertEquals(1.0, elements[0]);
        assertEquals(2.0, elements[1]);
        assertEquals(3.0, elements[2]);
        assertEquals(4.0, elements[3]);
    }

    @Test
    public void testStreamLU2RD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] diagonal = matrix.streamMainDiagonal().toArray();
        assertEquals(2, diagonal.length);
        assertEquals(1.0, diagonal[0]);
        assertEquals(4.0, diagonal[1]);

        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.streamMainDiagonal().toArray().length);
    }

    @Test
    public void testStreamRU2LD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] diagonal = matrix.streamAntiDiagonal().toArray();
        assertEquals(2, diagonal.length);
        assertEquals(2.0, diagonal[0]);
        assertEquals(3.0, diagonal[1]);
    }

    @Test
    public void testStreamHWithRow() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] row = matrix.streamHorizontal(1).toArray();
        assertEquals(2, row.length);
        assertEquals(3.0, row[0]);
        assertEquals(4.0, row[1]);
    }

    @Test
    public void testStreamHWithRange() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] elements = matrix.streamHorizontal(1, 3).toArray();
        assertEquals(4, elements.length);
        assertEquals(3.0, elements[0]);
        assertEquals(4.0, elements[1]);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamHorizontal(-1, 2));
    }

    @Test
    public void testStreamV() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] elements = matrix.streamVertical().toArray();
        assertEquals(4, elements.length);
        assertEquals(1.0, elements[0]);
        assertEquals(3.0, elements[1]);
        assertEquals(2.0, elements[2]);
        assertEquals(4.0, elements[3]);
    }

    @Test
    public void testStreamVWithColumn() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] col = matrix.streamVertical(1).toArray();
        assertEquals(2, col.length);
        assertEquals(2.0, col[0]);
        assertEquals(4.0, col[1]);
    }

    @Test
    public void testStreamVWithRange() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] elements = matrix.streamVertical(1, 3).toArray();
        assertEquals(4, elements.length);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamVertical(-1, 2));
    }

    @Test
    public void testStreamR() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        List<double[]> rows = matrix.streamRows().map(stream -> stream.toArray()).toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).length);
        assertEquals(1.0, rows.get(0)[0]);
        assertEquals(2.0, rows.get(0)[1]);
    }

    @Test
    public void testStreamRWithRange() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        List<double[]> rows = matrix.streamRows(1, 3).map(stream -> stream.toArray()).toList();
        assertEquals(2, rows.size());

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamRows(-1, 2));
    }

    @Test
    public void testStreamC() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        List<double[]> columnCount = matrix.streamColumns().map(stream -> stream.toArray()).toList();
        assertEquals(2, columnCount.size());
        assertEquals(2, columnCount.get(0).length);
        assertEquals(1.0, columnCount.get(0)[0]);
        assertEquals(3.0, columnCount.get(0)[1]);
    }

    @Test
    public void testStreamCWithRange() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        List<double[]> columnCount = matrix.streamColumns(1, 3).map(stream -> stream.toArray()).toList();
        assertEquals(2, columnCount.size());

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamColumns(-1, 2));
    }

    @Test
    public void testForEach() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] sum = { 0.0 };
        matrix.forEach(val -> sum[0] += val);
        assertEquals(10.0, sum[0]);
    }

    @Test
    public void testForEachWithRange() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] sum = { 0.0 };
        matrix.forEach(0, 2, 1, 3, val -> sum[0] += val);
        assertEquals(16.0, sum[0]); // 2 + 3 + 5 + 6

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.forEach(-1, 2, 0, 2, val -> {
        }));
    }

    @Test
    public void testForEachNullAction() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertThrows(IllegalArgumentException.class, () -> matrix.forEach((Throwables.DoubleConsumer<RuntimeException>) null));
        assertThrows(IllegalArgumentException.class, () -> matrix.forEach(0, 1, 0, 2, (Throwables.DoubleConsumer<RuntimeException>) null));
    }

    @Test
    public void testPrintln() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertFalse(matrix.isEmpty());
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(matrix::println);
    }

    @Test
    public void testHashCode() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix1 = DoubleMatrix.of(arr);
        DoubleMatrix matrix2 = DoubleMatrix.of(arr);

        assertEquals(matrix1.hashCode(), matrix2.hashCode());
    }

    @Test
    public void testEquals() {
        double[][] arr1 = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        double[][] arr2 = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        double[][] arr3 = { { 1.0, 2.0 }, { 3.0, 5.0 } };

        DoubleMatrix matrix1 = DoubleMatrix.of(arr1);
        DoubleMatrix matrix2 = DoubleMatrix.of(arr2);
        DoubleMatrix matrix3 = DoubleMatrix.of(arr3);

        assertTrue(matrix1.equals(matrix1));
        assertTrue(matrix1.equals(matrix2));
        assertFalse(matrix1.equals(matrix3));
        assertFalse(matrix1.equals(null));
        assertFalse(matrix1.equals("not a matrix"));
    }

    @Test
    public void testToString() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        String str = matrix.toString();
        assertNotNull(str);
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("4.0"));
    }

    @Test
    public void testStatisticalOperations() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        // Test sum operation on streams
        double totalSum = matrix.streamHorizontal().sum();
        assertEquals(45.0, totalSum, 0.0001); // 1+2+3+4+5+6+7+8+9 = 45

        // Test sum of specific row
        double row1Sum = matrix.streamHorizontal(1).sum();
        assertEquals(15.0, row1Sum, 0.0001); // 4+5+6 = 15

        // Test sum of specific column
        double col0Sum = matrix.streamVertical(0).sum();
        assertEquals(12.0, col0Sum, 0.0001); // 1+4+7 = 12

        // Test min/max on streams
        double min = matrix.streamHorizontal().min().orElse(0.0);
        assertEquals(1.0, min, 0.0001);

        double max = matrix.streamHorizontal().max().orElse(0.0);
        assertEquals(9.0, max, 0.0001);

        // Test average
        double avg = matrix.streamHorizontal().average().orElse(0.0);
        assertEquals(5.0, avg, 0.0001);

        // Test statistical operations on diagonal
        double diagonalSum = matrix.streamMainDiagonal().sum();
        assertEquals(15.0, diagonalSum, 0.0001); // 1+5+9 = 15

        double antiDiagonalSum = matrix.streamAntiDiagonal().sum();
        assertEquals(15.0, antiDiagonalSum, 0.0001); // 3+5+7 = 15
    }

    @Test
    public void testRowColumnStatistics() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        // Test statistics on individual rows
        List<Double> rowSums = matrix.streamRows().map(row -> row.sum()).toList();
        assertEquals(3, rowSums.size());
        assertEquals(6.0, rowSums.get(0).doubleValue(), 0.0001); // 1+2+3
        assertEquals(15.0, rowSums.get(1).doubleValue(), 0.0001); // 4+5+6
        assertEquals(24.0, rowSums.get(2).doubleValue(), 0.0001); // 7+8+9

        // Test statistics on individual columns
        List<Double> colSums = matrix.streamColumns().map(col -> col.sum()).toList();
        assertEquals(3, colSums.size());
        assertEquals(12.0, colSums.get(0).doubleValue(), 0.0001); // 1+4+7
        assertEquals(15.0, colSums.get(1).doubleValue(), 0.0001); // 2+5+8
        assertEquals(18.0, colSums.get(2).doubleValue(), 0.0001); // 3+6+9

        // Test min/max per row
        List<Double> rowMins = matrix.streamRows().map(row -> row.min().orElse(0.0)).toList();
        assertEquals(1.0, rowMins.get(0).doubleValue(), 0.0001);
        assertEquals(4.0, rowMins.get(1).doubleValue(), 0.0001);
        assertEquals(7.0, rowMins.get(2).doubleValue(), 0.0001);

        List<Double> rowMaxs = matrix.streamRows().map(row -> row.max().orElse(0.0)).toList();
        assertEquals(3.0, rowMaxs.get(0).doubleValue(), 0.0001);
        assertEquals(6.0, rowMaxs.get(1).doubleValue(), 0.0001);
        assertEquals(9.0, rowMaxs.get(2).doubleValue(), 0.0001);
    }

    @Test
    public void testElementWiseOperationsWithZipWith() {
        // Test element-wise multiplication using zipWith
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 2.0, 3.0 }, { 4.0, 5.0 } });

        // Element-wise multiplication
        DoubleMatrix elementWiseProduct = m1.zipWith(m2, (a, b) -> a * b);
        assertEquals(2.0, elementWiseProduct.get(0, 0), 0.0001); // 1.0*2.0
        assertEquals(6.0, elementWiseProduct.get(0, 1), 0.0001); // 2.0*3.0
        assertEquals(12.0, elementWiseProduct.get(1, 0), 0.0001); // 3.0*4.0
        assertEquals(20.0, elementWiseProduct.get(1, 1), 0.0001); // 4.0*5.0

        // Element-wise division
        DoubleMatrix elementWiseDivision = m2.zipWith(m1, (a, b) -> a / b);
        assertEquals(2.0, elementWiseDivision.get(0, 0), 0.0001); // 2.0/1.0
        assertEquals(1.5, elementWiseDivision.get(0, 1), 0.0001); // 3.0/2.0
        assertEquals(1.333333, elementWiseDivision.get(1, 0), 0.0001); // 4.0/3.0
        assertEquals(1.25, elementWiseDivision.get(1, 1), 0.0001); // 5.0/4.0
    }

    @Test
    public void testScalarOperationsWithMap() {
        // Test scalar addition using map
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });

        DoubleMatrix addScalar = m.map(x -> x + 10.5);
        assertEquals(11.5, addScalar.get(0, 0), 0.0001);
        assertEquals(12.5, addScalar.get(0, 1), 0.0001);
        assertEquals(13.5, addScalar.get(1, 0), 0.0001);
        assertEquals(14.5, addScalar.get(1, 1), 0.0001);

        // Test scalar subtraction
        DoubleMatrix subtractScalar = m.map(x -> x - 0.5);
        assertEquals(0.5, subtractScalar.get(0, 0), 0.0001);
        assertEquals(1.5, subtractScalar.get(0, 1), 0.0001);
        assertEquals(2.5, subtractScalar.get(1, 0), 0.0001);
        assertEquals(3.5, subtractScalar.get(1, 1), 0.0001);

        // Test scalar multiplication
        DoubleMatrix multiplyScalar = m.map(x -> x * 2.5);
        assertEquals(2.5, multiplyScalar.get(0, 0), 0.0001);
        assertEquals(5.0, multiplyScalar.get(0, 1), 0.0001);
        assertEquals(7.5, multiplyScalar.get(1, 0), 0.0001);
        assertEquals(10.0, multiplyScalar.get(1, 1), 0.0001);

        // Test scalar division
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 10.0, 20.0 }, { 30.0, 40.0 } });
        DoubleMatrix divideScalar = m2.map(x -> x / 10.0);
        assertEquals(1.0, divideScalar.get(0, 0), 0.0001);
        assertEquals(2.0, divideScalar.get(0, 1), 0.0001);
        assertEquals(3.0, divideScalar.get(1, 0), 0.0001);
        assertEquals(4.0, divideScalar.get(1, 1), 0.0001);
    }

    @Test
    public void testFloatingPointPrecision() {
        // Test operations that may introduce floating point precision issues
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 0.1, 0.2 }, { 0.3, 0.7 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 0.1, 0.1 }, { 0.2, 0.3 } });

        // Addition with potential precision issues
        DoubleMatrix sum = m1.add(m2);
        assertEquals(0.2, sum.get(0, 0), 0.0001);
        assertEquals(0.3, sum.get(0, 1), 0.0001);
        assertEquals(0.5, sum.get(1, 0), 0.0001);
        assertEquals(1.0, sum.get(1, 1), 0.0001);

        // Subtraction with potential precision issues
        DoubleMatrix diff = m1.subtract(m2);
        assertEquals(0.0, diff.get(0, 0), 0.0001);
        assertEquals(0.1, diff.get(0, 1), 0.0001);
        assertEquals(0.1, diff.get(1, 0), 0.0001);
        assertEquals(0.4, diff.get(1, 1), 0.0001);

        // Test operations that often reveal precision issues
        DoubleMatrix precisionTest = DoubleMatrix.of(new double[][] { { 1.0 / 3.0, 2.0 / 3.0 } });
        DoubleMatrix multiplyByThree = precisionTest.map(x -> x * 3.0);
        assertEquals(1.0, multiplyByThree.get(0, 0), 0.0001);
        assertEquals(2.0, multiplyByThree.get(0, 1), 0.0001);
    }

    @Test
    public void testSpecialFloatingPointValues() {
        // Test with NaN values
        DoubleMatrix nanMatrix = DoubleMatrix.of(new double[][] { { Double.NaN, 1.0 }, { 2.0, Double.NaN } });

        // NaN should remain NaN in operations
        DoubleMatrix nanPlusOne = nanMatrix.map(x -> x + 1.0);
        assertTrue(Double.isNaN(nanPlusOne.get(0, 0)));
        assertEquals(2.0, nanPlusOne.get(0, 1), 0.0001);
        assertEquals(3.0, nanPlusOne.get(1, 0), 0.0001);
        assertTrue(Double.isNaN(nanPlusOne.get(1, 1)));

        // Test with infinity values
        DoubleMatrix infMatrix = DoubleMatrix.of(new double[][] { { Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY }, { 1.0, 2.0 } });

        DoubleMatrix infPlusOne = infMatrix.map(x -> x + 1.0);
        assertEquals(Double.POSITIVE_INFINITY, infPlusOne.get(0, 0), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, infPlusOne.get(0, 1), 0.0);
        assertEquals(2.0, infPlusOne.get(1, 0), 0.0001);
        assertEquals(3.0, infPlusOne.get(1, 1), 0.0001);

        // Test infinity in arithmetic operations
        DoubleMatrix finite = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        DoubleMatrix inf = DoubleMatrix.of(new double[][] { { Double.POSITIVE_INFINITY, 3.0 } });

        DoubleMatrix infSum = finite.add(inf);
        assertEquals(Double.POSITIVE_INFINITY, infSum.get(0, 0), 0.0);
        assertEquals(5.0, infSum.get(0, 1), 0.0001);
    }

    @Test
    public void testArithmeticEdgeCases() {
        // Test with zero matrix
        DoubleMatrix zeros = DoubleMatrix.of(new double[][] { { 0.0, 0.0 }, { 0.0, 0.0 } });
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });

        DoubleMatrix addZero = m.add(zeros);
        assertEquals(m.get(0, 0), addZero.get(0, 0), 0.0001);
        assertEquals(m.get(1, 1), addZero.get(1, 1), 0.0001);

        DoubleMatrix subtractZero = m.subtract(zeros);
        assertEquals(m.get(0, 0), subtractZero.get(0, 0), 0.0001);
        assertEquals(m.get(1, 1), subtractZero.get(1, 1), 0.0001);

        // Test multiplication with zero matrix
        DoubleMatrix multiplyZero = m.multiply(zeros);
        assertEquals(0.0, multiplyZero.get(0, 0), 0.0001);
        assertEquals(0.0, multiplyZero.get(1, 1), 0.0001);

        // Test addition commutativity
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.5, 6.5 }, { 7.5, 8.5 } });
        DoubleMatrix sum1 = m1.add(m2);
        DoubleMatrix sum2 = m2.add(m1);

        assertEquals(sum1.get(0, 0), sum2.get(0, 0), 0.0001);
        assertEquals(sum1.get(1, 1), sum2.get(1, 1), 0.0001);

        // Test subtraction anti-commutativity
        DoubleMatrix diff1 = m1.subtract(m2);
        DoubleMatrix diff2 = m2.subtract(m1);
        assertEquals(diff1.get(0, 0), -diff2.get(0, 0), 0.0001);
        assertEquals(diff1.get(1, 1), -diff2.get(1, 1), 0.0001);
    }

    @Test
    public void testLargeMatrixArithmetic() {
        // Test with larger matrices to ensure performance and precision
        double[][] arr1 = new double[10][10];
        double[][] arr2 = new double[10][10];

        // Fill with test data
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                arr1[i][j] = (i * 10 + j + 1) * 0.1;
                arr2[i][j] = (i * 10 + j + 1) * 0.2;
            }
        }

        DoubleMatrix large1 = DoubleMatrix.of(arr1);
        DoubleMatrix large2 = DoubleMatrix.of(arr2);

        // Test addition
        DoubleMatrix largeSum = large1.add(large2);
        assertEquals(0.3, largeSum.get(0, 0), 0.0001); // 0.1 + 0.2 = 0.3
        assertEquals(30.0, largeSum.get(9, 9), 0.0001); // 10.0 + 20.0 = 30.0

        // Test that sum of all elements is correct
        double totalSum = largeSum.streamHorizontal().sum();
        double expected = 3 * (1 + 2 + 3 + /* ... */ +100) * 0.1; // 3 * 5050 * 0.1 = 1515.0
        assertEquals(1515.0, totalSum, 0.1);
    }

    @Test
    public void testDivisionByZero() {
        // Test division by zero behavior
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix zeros = DoubleMatrix.of(new double[][] { { 0.0, 0.0 }, { 0.0, 0.0 } });

        // Element-wise division by zero should produce infinity or NaN
        DoubleMatrix divByZero = m1.zipWith(zeros, (a, b) -> a / b);
        assertEquals(Double.POSITIVE_INFINITY, divByZero.get(0, 0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, divByZero.get(0, 1), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, divByZero.get(1, 0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, divByZero.get(1, 1), 0.0);

        // Zero divided by zero should be NaN
        DoubleMatrix zeroByZero = zeros.zipWith(zeros, (a, b) -> a / b);
        assertTrue(Double.isNaN(zeroByZero.get(0, 0)));
        assertTrue(Double.isNaN(zeroByZero.get(1, 1)));
    }

    @Test
    public void testMathematicalConstants() {
        // Test operations with mathematical constants
        DoubleMatrix constants = DoubleMatrix.of(new double[][] { { Math.PI, Math.E }, { Math.sqrt(2), Math.log(10) } });

        // Test trigonometric operations
        DoubleMatrix sinConstants = constants.map(Math::sin);
        assertEquals(Math.sin(Math.PI), sinConstants.get(0, 0), 0.0001);
        assertEquals(Math.sin(Math.E), sinConstants.get(0, 1), 0.0001);

        // Test logarithmic operations
        DoubleMatrix logConstants = constants.map(Math::log);
        assertEquals(Math.log(Math.PI), logConstants.get(0, 0), 0.0001);
        assertEquals(1.0, logConstants.get(0, 1), 0.0001); // log(e) = 1

        // Test power operations
        DoubleMatrix squared = constants.map(x -> x * x);
        assertEquals(Math.PI * Math.PI, squared.get(0, 0), 0.0001);
        assertEquals(Math.E * Math.E, squared.get(0, 1), 0.0001);
    }

    @Nested
    @Tag("2025")
    class DoubleMatrix2025Test extends TestBase {

        private static final double DELTA = 0.0001;

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            DoubleMatrix m = new DoubleMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.5, m.get(0, 0), DELTA);
            assertEquals(4.5, m.get(1, 1), DELTA);
        }

        @Test
        public void testConstructor_withNullArray() {
            DoubleMatrix m = new DoubleMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withEmptyArray() {
            DoubleMatrix m = new DoubleMatrix(new double[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withSingleElement() {
            DoubleMatrix m = new DoubleMatrix(new double[][] { { 42.75 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42.75, m.get(0, 0), DELTA);
        }

        // ============ Factory Method Tests ============

        @Test
        public void testEmpty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());

            // Test singleton
            assertSame(DoubleMatrix.empty(), DoubleMatrix.empty());
        }

        @Test
        public void testOf_withValidArray() {
            double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            DoubleMatrix m = DoubleMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.5, m.get(0, 0), DELTA);
        }

        @Test
        public void testOf_withNullArray() {
            DoubleMatrix m = DoubleMatrix.of((double[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withEmptyArray() {
            DoubleMatrix m = DoubleMatrix.of(new double[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray() {
            int[][] ints = { { 1, 2 }, { 3, 4 } };
            DoubleMatrix m = DoubleMatrix.from(ints);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0), DELTA);
            assertEquals(2.0, m.get(0, 1), DELTA);
            assertEquals(3.0, m.get(1, 0), DELTA);
            assertEquals(4.0, m.get(1, 1), DELTA);
        }

        @Test
        public void testCreateFromIntArray_withNull() {
            DoubleMatrix m = DoubleMatrix.from((int[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray_withEmpty() {
            DoubleMatrix m = DoubleMatrix.from(new int[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray_withJaggedArray() {
            int[][] jagged = { { 1, 2 }, { 3 } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(jagged));
        }

        @Test
        public void testCreateFromIntArray_withNullRow() {
            int[][] nullRow = { { 1, 2 }, null };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(nullRow));
        }

        @Test
        public void testCreateFromIntArray_withNullFirstRow() {
            int[][] nullFirstRow = { null, { 1, 2 } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(nullFirstRow));
        }

        @Test
        public void testCreateFromLongArray() {
            long[][] longs = { { 1L, 2L }, { 3L, 4L } };
            DoubleMatrix m = DoubleMatrix.from(longs);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0), DELTA);
            assertEquals(4.0, m.get(1, 1), DELTA);
        }

        @Test
        public void testCreateFromLongArray_withNull() {
            DoubleMatrix m = DoubleMatrix.from((long[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromLongArray_withJaggedArray() {
            long[][] jagged = { { 1L, 2L }, { 3L } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(jagged));
        }

        @Test
        public void testCreateFromLongArray_withNullRow() {
            long[][] nullRow = { { 1L, 2L }, null };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(nullRow));
        }

        @Test
        public void testCreateFromFloatArray() {
            float[][] floats = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
            DoubleMatrix m = DoubleMatrix.from(floats);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.5, m.get(0, 0), DELTA);
            assertEquals(4.5, m.get(1, 1), DELTA);
        }

        @Test
        public void testCreateFromFloatArray_withNull() {
            DoubleMatrix m = DoubleMatrix.from((float[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromFloatArray_withJaggedArray() {
            float[][] jagged = { { 1.5f, 2.5f }, { 3.5f } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(jagged));
        }

        @Test
        public void testCreateFromFloatArray_withNullRow() {
            float[][] nullRow = { { 1.5f, 2.5f }, null };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(nullRow));
        }

        @Test
        public void testRandom() {
            DoubleMatrix m = DoubleMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            // Just verify elements exist (values are random between 0.0 and 1.0)
            for (int i = 0; i < 5; i++) {
                double val = m.get(0, i);
                assertTrue(val >= 0.0 && val < 1.0);
            }
        }

        @Test
        public void testRandom_withRowsCols() {
            DoubleMatrix m = DoubleMatrix.random(2, 3);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    double val = m.get(i, j);
                    assertTrue(val >= 0.0 && val < 1.0);
                }
            }
        }

        @Test
        public void testRepeat() {
            DoubleMatrix m = DoubleMatrix.repeat(1, 5, 3.14);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(3.14, m.get(0, i), DELTA);
            }
        }

        @Test
        public void testRepeat_withRowsCols() {
            DoubleMatrix m = DoubleMatrix.repeat(2, 3, 3.14);
            assertEquals(2, m.rowCount());
            assertEquals(3, m.columnCount());
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    assertEquals(3.14, m.get(i, j), DELTA);
                }
            }
        }

        @Test
        public void testDiagonalLU2RD() {
            DoubleMatrix m = DoubleMatrix.mainDiagonal(new double[] { 1.5, 2.5, 3.5 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.5, m.get(0, 0), DELTA);
            assertEquals(2.5, m.get(1, 1), DELTA);
            assertEquals(3.5, m.get(2, 2), DELTA);
            assertEquals(0.0, m.get(0, 1), DELTA);
            assertEquals(0.0, m.get(1, 0), DELTA);
        }

        @Test
        public void testDiagonalRU2LD() {
            DoubleMatrix m = DoubleMatrix.antiDiagonal(new double[] { 1.5, 2.5, 3.5 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.5, m.get(0, 2), DELTA);
            assertEquals(2.5, m.get(1, 1), DELTA);
            assertEquals(3.5, m.get(2, 0), DELTA);
            assertEquals(0.0, m.get(0, 0), DELTA);
            assertEquals(0.0, m.get(2, 2), DELTA);
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            DoubleMatrix m = DoubleMatrix.diagonals(new double[] { 1.0, 2.0, 3.0 }, new double[] { 4.0, 5.0, 6.0 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 0), DELTA);
            assertEquals(2.0, m.get(1, 1), DELTA);
            assertEquals(3.0, m.get(2, 2), DELTA);
            assertEquals(4.0, m.get(0, 2), DELTA);
            assertEquals(6.0, m.get(2, 0), DELTA);
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            DoubleMatrix m = DoubleMatrix.diagonals(new double[] { 1.5, 2.5, 3.5 }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.5, m.get(0, 0), DELTA);
            assertEquals(2.5, m.get(1, 1), DELTA);
            assertEquals(3.5, m.get(2, 2), DELTA);
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            DoubleMatrix m = DoubleMatrix.diagonals(null, new double[] { 4.5, 5.5, 6.5 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(4.5, m.get(0, 2), DELTA);
            assertEquals(5.5, m.get(1, 1), DELTA);
            assertEquals(6.5, m.get(2, 0), DELTA);
        }

        @Test
        public void testDiagonal_withBothNull() {
            DoubleMatrix m = DoubleMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.diagonals(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0, 5.0 }));
        }

        @Test
        public void testUnbox() {
            Double[][] boxed = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            Matrix<Double> boxedMatrix = Matrix.of(boxed);
            DoubleMatrix unboxed = DoubleMatrix.unbox(boxedMatrix);
            assertEquals(2, unboxed.rowCount());
            assertEquals(2, unboxed.columnCount());
            assertEquals(1.5, unboxed.get(0, 0), DELTA);
            assertEquals(4.5, unboxed.get(1, 1), DELTA);
        }

        @Test
        public void testUnbox_withNullValues() {
            Double[][] boxed = { { 1.5, null }, { null, 4.5 } };
            Matrix<Double> boxedMatrix = Matrix.of(boxed);
            DoubleMatrix unboxed = DoubleMatrix.unbox(boxedMatrix);
            assertEquals(1.5, unboxed.get(0, 0), DELTA);
            assertEquals(0.0, unboxed.get(0, 1), DELTA); // null -> 0.0
            assertEquals(0.0, unboxed.get(1, 0), DELTA); // null -> 0.0
            assertEquals(4.5, unboxed.get(1, 1), DELTA);
        }

        // ============ Component Type Tests ============

        @Test
        public void testComponentType() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0 } });
            assertEquals(double.class, m.componentType());
        }

        // ============ Get/Set Tests ============

        @Test
        public void testGet() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5, 3.5 }, { 4.5, 5.5, 6.5 } });
            assertEquals(1.5, m.get(0, 0), DELTA);
            assertEquals(5.5, m.get(1, 1), DELTA);
            assertEquals(6.5, m.get(1, 2), DELTA);
        }

        @Test
        public void testGet_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
        }

        @Test
        public void testGetWithPoint() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            assertEquals(1.5, m.get(Point.of(0, 0)), DELTA);
            assertEquals(4.5, m.get(Point.of(1, 1)), DELTA);
            assertEquals(2.5, m.get(Point.of(0, 1)), DELTA);
        }

        @Test
        public void testSet() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.set(0, 0, 10.5);
            assertEquals(10.5, m.get(0, 0), DELTA);

            m.set(1, 1, 20.5);
            assertEquals(20.5, m.get(1, 1), DELTA);
        }

        @Test
        public void testSet_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, 0.0));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, 0.0));
        }

        @Test
        public void testSetWithPoint() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.set(Point.of(0, 0), 50.5);
            assertEquals(50.5, m.get(Point.of(0, 0)), DELTA);
        }

        // ============ Adjacent Element Tests ============

        @Test
        public void testUpOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });

            OptionalDouble up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1.5, up.get(), DELTA);

            // Top row has no element above
            OptionalDouble empty = m.above(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testDownOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });

            OptionalDouble down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3.5, down.get(), DELTA);

            // Bottom row has no element below
            OptionalDouble empty = m.below(1, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testLeftOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });

            OptionalDouble left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1.5, left.get(), DELTA);

            // Leftmost column has no element to the left
            OptionalDouble empty = m.left(0, 0);
            assertFalse(empty.isPresent());
        }

        @Test
        public void testRightOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });

            OptionalDouble right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2.5, right.get(), DELTA);

            // Rightmost column has no element to the right
            OptionalDouble empty = m.right(0, 1);
            assertFalse(empty.isPresent());
        }

        // ============ Row/Column Operations Tests ============

        @Test
        public void testRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5, 3.5 }, { 4.5, 5.5, 6.5 } });
            assertArrayEquals(new double[] { 1.5, 2.5, 3.5 }, m.rowView(0), DELTA);
            assertArrayEquals(new double[] { 4.5, 5.5, 6.5 }, m.rowView(1), DELTA);
        }

        @Test
        public void testRow_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(2));
        }

        @Test
        public void testColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5, 3.5 }, { 4.5, 5.5, 6.5 } });
            assertArrayEquals(new double[] { 1.5, 4.5 }, m.columnCopy(0), DELTA);
            assertArrayEquals(new double[] { 2.5, 5.5 }, m.columnCopy(1), DELTA);
            assertArrayEquals(new double[] { 3.5, 6.5 }, m.columnCopy(2), DELTA);
        }

        @Test
        public void testColumn_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setRow(0, new double[] { 10.5, 20.5 });
            assertArrayEquals(new double[] { 10.5, 20.5 }, m.rowView(0), DELTA);
            assertArrayEquals(new double[] { 3.0, 4.0 }, m.rowView(1), DELTA); // unchanged
        }

        @Test
        public void testSetRow_wrongSize() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new double[] { 1.0 }));
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new double[] { 1.0, 2.0, 3.0 }));
        }

        @Test
        public void testSetColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setColumn(0, new double[] { 10.5, 20.5 });
            assertArrayEquals(new double[] { 10.5, 20.5 }, m.columnCopy(0), DELTA);
            assertArrayEquals(new double[] { 2.0, 4.0 }, m.columnCopy(1), DELTA); // unchanged
        }

        @Test
        public void testSetColumn_wrongSize() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new double[] { 1.0 }));
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new double[] { 1.0, 2.0, 3.0 }));
        }

        @Test
        public void testUpdateRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateRow(0, x -> x * 2.0);
            assertArrayEquals(new double[] { 2.0, 4.0 }, m.rowView(0), DELTA);
            assertArrayEquals(new double[] { 3.0, 4.0 }, m.rowView(1), DELTA); // unchanged
        }

        @Test
        public void testUpdateColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateColumn(0, x -> x + 10.5);
            assertArrayEquals(new double[] { 11.5, 13.5 }, m.columnCopy(0), DELTA);
            assertArrayEquals(new double[] { 2.0, 4.0 }, m.columnCopy(1), DELTA); // unchanged
        }

        // ============ Diagonal Operations Tests ============

        @Test
        public void testGetLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, m.getMainDiagonal(), DELTA);
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            m.setMainDiagonal(new double[] { 10.5, 20.5, 30.5 });
            assertEquals(10.5, m.get(0, 0), DELTA);
            assertEquals(20.5, m.get(1, 1), DELTA);
            assertEquals(30.5, m.get(2, 2), DELTA);
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new double[] { 1.0 }));
        }

        @Test
        public void testSetLU2RD_arrayTooShort() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new double[] { 1.0, 2.0 }));
        }

        @Test
        public void testUpdateLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            m.updateMainDiagonal(x -> x * 10.0);
            assertEquals(10.0, m.get(0, 0), DELTA);
            assertEquals(50.0, m.get(1, 1), DELTA);
            assertEquals(90.0, m.get(2, 2), DELTA);
            assertEquals(2.0, m.get(0, 1), DELTA); // unchanged
        }

        @Test
        public void testUpdateLU2RD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalStateException.class, () -> m.updateMainDiagonal(x -> x * 2.0));
        }

        @Test
        public void testGetRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, m.getAntiDiagonal(), DELTA);
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            m.setAntiDiagonal(new double[] { 10.5, 20.5, 30.5 });
            assertEquals(10.5, m.get(0, 2), DELTA);
            assertEquals(20.5, m.get(1, 1), DELTA);
            assertEquals(30.5, m.get(2, 0), DELTA);
        }

        @Test
        public void testSetRU2LD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalStateException.class, () -> m.setAntiDiagonal(new double[] { 1.0 }));
        }

        @Test
        public void testSetRU2LD_arrayTooShort() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setAntiDiagonal(new double[] { 1.0, 2.0 }));
        }

        @Test
        public void testUpdateRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            m.updateAntiDiagonal(x -> x * 10.0);
            assertEquals(30.0, m.get(0, 2), DELTA);
            assertEquals(50.0, m.get(1, 1), DELTA);
            assertEquals(70.0, m.get(2, 0), DELTA);
            assertEquals(2.0, m.get(0, 1), DELTA); // unchanged
        }

        @Test
        public void testUpdateRU2LD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalStateException.class, () -> m.updateAntiDiagonal(x -> x * 2.0));
        }

        // ============ Transformation Tests ============

        @Test
        public void testUpdateAll() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateAll(x -> x * 2.0);
            assertEquals(2.0, m.get(0, 0), DELTA);
            assertEquals(4.0, m.get(0, 1), DELTA);
            assertEquals(6.0, m.get(1, 0), DELTA);
            assertEquals(8.0, m.get(1, 1), DELTA);
        }

        @Test
        public void testUpdateAll_withIndices() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 0.0, 0.0 }, { 0.0, 0.0 } });
            m.updateAll((i, j) -> i * 10.0 + j);
            assertEquals(0.0, m.get(0, 0), DELTA);
            assertEquals(1.0, m.get(0, 1), DELTA);
            assertEquals(10.0, m.get(1, 0), DELTA);
            assertEquals(11.0, m.get(1, 1), DELTA);
        }

        @Test
        public void testReplaceIf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            m.replaceIf(x -> x > 3.0, 0.0);
            assertEquals(1.0, m.get(0, 0), DELTA);
            assertEquals(2.0, m.get(0, 1), DELTA);
            assertEquals(3.0, m.get(0, 2), DELTA);
            assertEquals(0.0, m.get(1, 0), DELTA); // was 4.0
            assertEquals(0.0, m.get(1, 1), DELTA); // was 5.0
            assertEquals(0.0, m.get(1, 2), DELTA); // was 6.0
        }

        @Test
        public void testReplaceIf_withIndices() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            m.replaceIf((i, j) -> i == j, 0.0); // Replace diagonal
            assertEquals(0.0, m.get(0, 0), DELTA);
            assertEquals(0.0, m.get(1, 1), DELTA);
            assertEquals(0.0, m.get(2, 2), DELTA);
            assertEquals(2.0, m.get(0, 1), DELTA); // unchanged
        }

        @Test
        public void testMap() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m.map(x -> x * 2.0);
            assertEquals(2.0, result.get(0, 0), DELTA);
            assertEquals(4.0, result.get(0, 1), DELTA);
            assertEquals(6.0, result.get(1, 0), DELTA);
            assertEquals(8.0, result.get(1, 1), DELTA);

            // Original unchanged
            assertEquals(1.0, m.get(0, 0), DELTA);
        }

        @Test
        public void testMapToInt() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            IntMatrix result = m.mapToInt(x -> (int) x);
            assertEquals(1, result.get(0, 0));
            assertEquals(2, result.get(0, 1));
            assertEquals(3, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void testMapToLong() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            LongMatrix result = m.mapToLong(x -> (long) (x * 1000));
            assertEquals(1500L, result.get(0, 0));
            assertEquals(4500L, result.get(1, 1));
        }

        @Test
        public void testMapToObj() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            Matrix<String> result = m.mapToObj(x -> "val:" + x, String.class);
            assertEquals("val:1.5", result.get(0, 0));
            assertEquals("val:4.5", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_withValue() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.fill(99.5);
            for (int i = 0; i < m.rowCount(); i++) {
                for (int j = 0; j < m.columnCount(); j++) {
                    assertEquals(99.5, m.get(i, j), DELTA);
                }
            }
        }

        @Test
        public void testFill_withArray() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } });
            double[][] patch = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            m.copyFrom(patch);
            assertEquals(1.5, m.get(0, 0), DELTA);
            assertEquals(2.5, m.get(0, 1), DELTA);
            assertEquals(3.5, m.get(1, 0), DELTA);
            assertEquals(4.5, m.get(1, 1), DELTA);
            assertEquals(0.0, m.get(2, 2), DELTA); // unchanged
        }

        @Test
        public void testFill_withArrayAtPosition() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } });
            double[][] patch = { { 1.5, 2.5 }, { 3.5, 4.5 } };
            m.copyFrom(1, 1, patch);
            assertEquals(0.0, m.get(0, 0), DELTA); // unchanged
            assertEquals(1.5, m.get(1, 1), DELTA);
            assertEquals(2.5, m.get(1, 2), DELTA);
            assertEquals(3.5, m.get(2, 1), DELTA);
            assertEquals(4.5, m.get(2, 2), DELTA);
        }

        @Test
        public void testFill_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            double[][] patch = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            assertThrows(IllegalArgumentException.class, () -> m.copyFrom(-1, 0, patch));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals(1.0, copy.get(0, 0), DELTA);

            // Modify copy shouldn't affect original
            copy.set(0, 0, 99.5);
            assertEquals(1.0, m.get(0, 0), DELTA);
            assertEquals(99.5, copy.get(0, 0), DELTA);
        }

        @Test
        public void testCopy_withRowRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleMatrix subset = m.copy(1, 3);
            assertEquals(2, subset.rowCount());
            assertEquals(3, subset.columnCount());
            assertEquals(4.0, subset.get(0, 0), DELTA);
            assertEquals(9.0, subset.get(1, 2), DELTA);
        }

        @Test
        public void testCopy_withRowRange_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
        }

        @Test
        public void testCopy_withFullRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleMatrix submatrix = m.copy(0, 2, 1, 3);
            assertEquals(2, submatrix.rowCount());
            assertEquals(2, submatrix.columnCount());
            assertEquals(2.0, submatrix.get(0, 0), DELTA);
            assertEquals(6.0, submatrix.get(1, 1), DELTA);
        }

        @Test
        public void testCopy_withFullRange_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_larger() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix extended = m.resize(4, 4);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1.0, extended.get(0, 0), DELTA);
            assertEquals(4.0, extended.get(1, 1), DELTA);
            assertEquals(0.0, extended.get(3, 3), DELTA); // new cells are 0.0
        }

        @Test
        public void testExtend_smaller() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleMatrix truncated = m.resize(2, 2);
            assertEquals(2, truncated.rowCount());
            assertEquals(2, truncated.columnCount());
            assertEquals(1.0, truncated.get(0, 0), DELTA);
            assertEquals(5.0, truncated.get(1, 1), DELTA);
        }

        @Test
        public void testExtend_withDefaultValue() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix extended = m.resize(3, 3, -1.5);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1.0, extended.get(0, 0), DELTA);
            assertEquals(-1.5, extended.get(2, 2), DELTA); // new cell
        }

        @Test
        public void testExtend_withNegativeDimensions() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.resize(-1, 3, 0.0));
            assertThrows(IllegalArgumentException.class, () -> m.resize(3, -1, 0.0));
        }

        @Test
        public void testExtend_directional() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleMatrix extended = m.extend(1, 1, 2, 2);
            assertEquals(5, extended.rowCount()); // 1 + 3 + 1
            assertEquals(7, extended.columnCount()); // 2 + 3 + 2

            // Original values at offset position
            assertEquals(1.0, extended.get(1, 2), DELTA);
            assertEquals(5.0, extended.get(2, 3), DELTA);

            // New cells are 0.0
            assertEquals(0.0, extended.get(0, 0), DELTA);
        }

        @Test
        public void testExtend_directionalWithDefault() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleMatrix extended = m.extend(1, 1, 1, 1, -1.5);
            assertEquals(5, extended.rowCount());
            assertEquals(5, extended.columnCount());

            // Check original values
            assertEquals(1.0, extended.get(1, 1), DELTA);

            // Check new values
            assertEquals(-1.5, extended.get(0, 0), DELTA);
            assertEquals(-1.5, extended.get(4, 4), DELTA);
        }

        @Test
        public void testExtend_directionalWithNegativeValues() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, 0.0));
        }

        // ============ Reverse/Flip Tests ============

        @Test
        public void testReverseH() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            m.flipInPlaceHorizontally();
            assertEquals(3.0, m.get(0, 0), DELTA);
            assertEquals(2.0, m.get(0, 1), DELTA);
            assertEquals(1.0, m.get(0, 2), DELTA);
            assertEquals(6.0, m.get(1, 0), DELTA);
        }

        @Test
        public void testReverseV() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            m.flipInPlaceVertically();
            assertEquals(5.0, m.get(0, 0), DELTA);
            assertEquals(6.0, m.get(0, 1), DELTA);
            assertEquals(3.0, m.get(1, 0), DELTA);
            assertEquals(1.0, m.get(2, 0), DELTA);
        }

        @Test
        public void testFlipH() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleMatrix flipped = m.flipHorizontally();
            assertEquals(3.0, flipped.get(0, 0), DELTA);
            assertEquals(2.0, flipped.get(0, 1), DELTA);
            assertEquals(1.0, flipped.get(0, 2), DELTA);

            // Original unchanged
            assertEquals(1.0, m.get(0, 0), DELTA);
        }

        @Test
        public void testFlipV() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            DoubleMatrix flipped = m.flipVertically();
            assertEquals(5.0, flipped.get(0, 0), DELTA);
            assertEquals(3.0, flipped.get(1, 0), DELTA);
            assertEquals(1.0, flipped.get(2, 0), DELTA);

            // Original unchanged
            assertEquals(1.0, m.get(0, 0), DELTA);
        }

        // ============ Rotation Tests ============

        @Test
        public void testRotate90() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3.0, rotated.get(0, 0), DELTA);
            assertEquals(1.0, rotated.get(0, 1), DELTA);
            assertEquals(4.0, rotated.get(1, 0), DELTA);
            assertEquals(2.0, rotated.get(1, 1), DELTA);
        }

        @Test
        public void testRotate180() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate180();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(4.0, rotated.get(0, 0), DELTA);
            assertEquals(3.0, rotated.get(0, 1), DELTA);
            assertEquals(2.0, rotated.get(1, 0), DELTA);
            assertEquals(1.0, rotated.get(1, 1), DELTA);
        }

        @Test
        public void testRotate270() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(2.0, rotated.get(0, 0), DELTA);
            assertEquals(4.0, rotated.get(0, 1), DELTA);
            assertEquals(1.0, rotated.get(1, 0), DELTA);
            assertEquals(3.0, rotated.get(1, 1), DELTA);
        }

        // ============ Transpose Tests ============

        @Test
        public void testTranspose() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1.0, transposed.get(0, 0), DELTA);
            assertEquals(4.0, transposed.get(0, 1), DELTA);
            assertEquals(2.0, transposed.get(1, 0), DELTA);
            assertEquals(5.0, transposed.get(1, 1), DELTA);
            assertEquals(3.0, transposed.get(2, 0), DELTA);
            assertEquals(6.0, transposed.get(2, 1), DELTA);
        }

        @Test
        public void testTranspose_square() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1.0, transposed.get(0, 0), DELTA);
            assertEquals(3.0, transposed.get(0, 1), DELTA);
            assertEquals(2.0, transposed.get(1, 0), DELTA);
            assertEquals(4.0, transposed.get(1, 1), DELTA);
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleMatrix reshaped = m.reshape(1, 9);
            assertEquals(1, reshaped.rowCount());
            assertEquals(9, reshaped.columnCount());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1.0, reshaped.get(0, i), DELTA);
            }
        }

        @Test
        public void testReshape_back() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleMatrix reshaped = m.reshape(1, 9);
            DoubleMatrix reshapedBack = reshaped.reshape(3, 3);
            assertEquals(m, reshapedBack);
        }

        @Test
        public void testReshape_empty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            DoubleMatrix reshaped = empty.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        // ============ Repeat Tests ============

        @Test
        public void testRepelem() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 } });
            DoubleMatrix repeated = m.repeatElements(2, 3);
            assertEquals(2, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals(1.5, repeated.get(0, 0), DELTA);
            assertEquals(1.5, repeated.get(0, 1), DELTA);
            assertEquals(1.5, repeated.get(0, 2), DELTA);
            assertEquals(2.5, repeated.get(0, 3), DELTA);
            assertEquals(2.5, repeated.get(0, 4), DELTA);
            assertEquals(2.5, repeated.get(0, 5), DELTA);
            assertEquals(1.5, repeated.get(1, 0), DELTA); // second row same as first
        }

        @Test
        public void testRepelem_invalidArguments() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(1, 0));
        }

        @Test
        public void testRepmat() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            DoubleMatrix repeated = m.repeatMatrix(2, 3);
            assertEquals(4, repeated.rowCount());
            assertEquals(6, repeated.columnCount());

            // Check pattern
            assertEquals(1.5, repeated.get(0, 0), DELTA);
            assertEquals(2.5, repeated.get(0, 1), DELTA);
            assertEquals(1.5, repeated.get(0, 2), DELTA); // repeat starts
            assertEquals(2.5, repeated.get(0, 3), DELTA);

            assertEquals(3.5, repeated.get(1, 0), DELTA);
            assertEquals(4.5, repeated.get(1, 1), DELTA);

            // Check vertical repeat
            assertEquals(1.5, repeated.get(2, 0), DELTA); // vertical repeat starts
            assertEquals(2.5, repeated.get(2, 1), DELTA);
        }

        @Test
        public void testRepmat_invalidArguments() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(1, 0));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleList flat = m.flatten();
            assertEquals(9, flat.size());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1.0, flat.get(i), DELTA);
            }
        }

        @Test
        public void testFlatten_empty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            DoubleList flat = empty.flatten();
            assertTrue(flat.isEmpty());
        }

        @Test
        public void testFlatOp() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            List<Double> sums = new ArrayList<>();
            m.applyOnFlattened(row -> {
                double sum = 0.0;
                for (double val : row) {
                    sum += val;
                }
                sums.add(sum);
            });
            assertEquals(1, sums.size());
            assertEquals(45.0, sums.get(0), DELTA);
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 7.0, 8.0, 9.0 }, { 10.0, 11.0, 12.0 } });
            DoubleMatrix stacked = m1.stackVertically(m2);

            assertEquals(4, stacked.rowCount());
            assertEquals(3, stacked.columnCount());
            assertEquals(1.0, stacked.get(0, 0), DELTA);
            assertEquals(7.0, stacked.get(2, 0), DELTA);
            assertEquals(12.0, stacked.get(3, 2), DELTA);
        }

        @Test
        public void testVstack_differentColumnCounts() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix stacked = m1.stackHorizontally(m2);

            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertEquals(1.0, stacked.get(0, 0), DELTA);
            assertEquals(5.0, stacked.get(0, 2), DELTA);
            assertEquals(8.0, stacked.get(1, 3), DELTA);
        }

        @Test
        public void testHstack_differentRowCounts() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Operations Tests ============

        @Test
        public void testAdd() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix sum = m1.add(m2);

            assertEquals(6.5, sum.get(0, 0), DELTA);
            assertEquals(8.5, sum.get(0, 1), DELTA);
            assertEquals(10.5, sum.get(1, 0), DELTA);
            assertEquals(12.5, sum.get(1, 1), DELTA);
        }

        @Test
        public void testAdd_differentDimensions() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void testSubtract() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 5.5, 6.5 }, { 7.5, 8.5 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix diff = m1.subtract(m2);

            assertEquals(4.5, diff.get(0, 0), DELTA);
            assertEquals(4.5, diff.get(0, 1), DELTA);
            assertEquals(4.5, diff.get(1, 0), DELTA);
            assertEquals(4.5, diff.get(1, 1), DELTA);
        }

        @Test
        public void testSubtract_differentDimensions() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
        }

        @Test
        public void testMultiply() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix product = m1.multiply(m2);

            assertEquals(19.0, product.get(0, 0), DELTA); // 1*5 + 2*7
            assertEquals(22.0, product.get(0, 1), DELTA); // 1*6 + 2*8
            assertEquals(43.0, product.get(1, 0), DELTA); // 3*5 + 4*7
            assertEquals(50.0, product.get(1, 1), DELTA); // 3*6 + 4*8
        }

        @Test
        public void testMultiply_incompatibleDimensions() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        @Test
        public void testMultiply_rectangularMatrices() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } }); // 1x3
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 4.0 }, { 5.0 }, { 6.0 } }); // 3x1
            DoubleMatrix product = m1.multiply(m2);

            assertEquals(1, product.rowCount());
            assertEquals(1, product.columnCount());
            assertEquals(32.0, product.get(0, 0), DELTA); // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
        }

        // ============ Conversion Tests ============

        @Test
        public void testBoxed() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5, 3.5 }, { 4.5, 5.5, 6.5 } });
            Matrix<Double> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(3, boxed.columnCount());
            assertEquals(Double.valueOf(1.5), boxed.get(0, 0));
            assertEquals(Double.valueOf(6.5), boxed.get(1, 2));
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix result = m1.zipWith(m2, (a, b) -> a * b);

            assertEquals(5.0, result.get(0, 0), DELTA); // 1*5
            assertEquals(12.0, result.get(0, 1), DELTA); // 2*6
            assertEquals(21.0, result.get(1, 0), DELTA); // 3*7
            assertEquals(32.0, result.get(1, 1), DELTA); // 4*8
        }

        @Test
        public void testZipWith_differentShapes() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a + b));
        }

        @Test
        public void testZipWith_threeMatrices() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 9.0, 10.0 }, { 11.0, 12.0 } });
            DoubleMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);

            assertEquals(15.0, result.get(0, 0), DELTA); // 1+5+9
            assertEquals(18.0, result.get(0, 1), DELTA); // 2+6+10
            assertEquals(21.0, result.get(1, 0), DELTA); // 3+7+11
            assertEquals(24.0, result.get(1, 1), DELTA); // 4+8+12
        }

        @Test
        public void testZipWith_threeMatrices_differentShapes() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 9.0, 10.0, 11.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> a + b + c));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            double[] diagonal = m.streamMainDiagonal().toArray();
            assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, diagonal, DELTA);
        }

        @Test
        public void testStreamLU2RD_empty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertEquals(0, empty.streamMainDiagonal().toArray().length);
        }

        @Test
        public void testStreamLU2RD_nonSquare() {
            DoubleMatrix nonSquare = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamMainDiagonal());
        }

        @Test
        public void testStreamRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            double[] antiDiagonal = m.streamAntiDiagonal().toArray();
            assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, antiDiagonal, DELTA);
        }

        @Test
        public void testStreamRU2LD_empty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertEquals(0, empty.streamAntiDiagonal().toArray().length);
        }

        @Test
        public void testStreamRU2LD_nonSquare() {
            DoubleMatrix nonSquare = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalStateException.class, () -> nonSquare.streamAntiDiagonal());
        }

        @Test
        public void testStreamH() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            double[] all = m.streamHorizontal().toArray();
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }, all, DELTA);
        }

        @Test
        public void testStreamH_empty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertEquals(0, empty.streamHorizontal().toArray().length);
        }

        @Test
        public void testStreamH_withRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            double[] row1 = m.streamHorizontal(1).toArray();
            assertArrayEquals(new double[] { 4.0, 5.0, 6.0 }, row1, DELTA);
        }

        @Test
        public void testStreamH_withRow_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2));
        }

        @Test
        public void testStreamH_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            double[] rows = m.streamHorizontal(1, 3).toArray();
            assertArrayEquals(new double[] { 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 }, rows, DELTA);
        }

        @Test
        public void testStreamH_withRange_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamHorizontal(2, 1));
        }

        @Test
        public void testStreamV() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            double[] all = m.streamVertical().toArray();
            assertArrayEquals(new double[] { 1.0, 4.0, 2.0, 5.0, 3.0, 6.0 }, all, DELTA);
        }

        @Test
        public void testStreamV_empty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertEquals(0, empty.streamVertical().toArray().length);
        }

        @Test
        public void testStreamV_withColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            double[] col1 = m.streamVertical(1).toArray();
            assertArrayEquals(new double[] { 2.0, 5.0 }, col1, DELTA);
        }

        @Test
        public void testStreamV_withColumn_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2));
        }

        @Test
        public void testStreamV_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            double[] columnCount = m.streamVertical(1, 3).toArray();
            assertArrayEquals(new double[] { 2.0, 5.0, 8.0, 3.0, 6.0, 9.0 }, columnCount, DELTA);
        }

        @Test
        public void testStreamV_withRange_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(0, 3));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamVertical(2, 1));
        }

        @Test
        public void testStreamR() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            List<double[]> rows = m.streamRows().map(DoubleStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, rows.get(0), DELTA);
            assertArrayEquals(new double[] { 4.0, 5.0, 6.0 }, rows.get(1), DELTA);
        }

        @Test
        public void testStreamR_empty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertEquals(0, empty.streamRows().count());
        }

        @Test
        public void testStreamR_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            List<double[]> rows = m.streamRows(1, 3).map(DoubleStream::toArray).toList();
            assertEquals(2, rows.size());
            assertArrayEquals(new double[] { 4.0, 5.0, 6.0 }, rows.get(0), DELTA);
            assertArrayEquals(new double[] { 7.0, 8.0, 9.0 }, rows.get(1), DELTA);
        }

        @Test
        public void testStreamR_withRange_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamRows(0, 3));
        }

        @Test
        public void testStreamC() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            List<double[]> columnCount = m.streamColumns().map(DoubleStream::toArray).toList();
            assertEquals(3, columnCount.size());
            assertArrayEquals(new double[] { 1.0, 4.0 }, columnCount.get(0), DELTA);
            assertArrayEquals(new double[] { 2.0, 5.0 }, columnCount.get(1), DELTA);
            assertArrayEquals(new double[] { 3.0, 6.0 }, columnCount.get(2), DELTA);
        }

        @Test
        public void testStreamC_empty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertEquals(0, empty.streamColumns().count());
        }

        @Test
        public void testStreamC_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            List<double[]> columnCount = m.streamColumns(1, 3).map(DoubleStream::toArray).toList();
            assertEquals(2, columnCount.size());
            assertArrayEquals(new double[] { 2.0, 5.0, 8.0 }, columnCount.get(0), DELTA);
            assertArrayEquals(new double[] { 3.0, 6.0, 9.0 }, columnCount.get(1), DELTA);
        }

        @Test
        public void testStreamC_withRange_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> m.streamColumns(0, 3));
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            List<Double> values = new ArrayList<>();
            m.forEach(v -> values.add(v));
            assertEquals(9, values.size());
            for (int i = 0; i < 9; i++) {
                assertEquals(i + 1.0, values.get(i), DELTA);
            }
        }

        @Test
        public void testForEach_withBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            List<Double> values = new ArrayList<>();
            m.forEach(1, 3, 1, 3, v -> values.add(v));
            assertEquals(4, values.size());
            assertEquals(5.0, values.get(0), DELTA);
            assertEquals(6.0, values.get(1), DELTA);
            assertEquals(8.0, values.get(2), DELTA);
            assertEquals(9.0, values.get(3), DELTA);
        }

        @Test
        public void testForEach_withBounds_outOfBounds() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(-1, 2, 0, 2, v -> {
            }));
            assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 3, 0, 2, v -> {
            }));
        }

        // ============ Object Methods Tests ============

        @Test
        public void testPrintln() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertFalse(m.isEmpty());
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(m::println);

            DoubleMatrix empty = DoubleMatrix.empty();
            assertTrue(empty.isEmpty());
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(empty::println);
        }

        @Test
        public void testHashCode() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 4.0, 3.0 } });

            assertEquals(m1.hashCode(), m2.hashCode());
            assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
        }

        @Test
        public void testEquals() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 4.0, 3.0 } });
            DoubleMatrix m4 = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });

            assertTrue(m1.equals(m1)); // Same object
            assertTrue(m1.equals(m2)); // Same values
            assertFalse(m1.equals(m3)); // Different values
            assertFalse(m1.equals(m4)); // Different dimensions
            assertFalse(m1.equals(null));
            assertFalse(m1.equals("not a matrix"));
        }

        @Test
        public void testToString() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
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
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });

            // Test sum
            double totalSum = m.streamHorizontal().sum();
            assertEquals(45.0, totalSum, DELTA); // 1+2+3+4+5+6+7+8+9 = 45

            // Test sum of specific row
            double row1Sum = m.streamHorizontal(1).sum();
            assertEquals(15.0, row1Sum, DELTA); // 4+5+6 = 15

            // Test sum of specific column
            double col0Sum = m.streamVertical(0).sum();
            assertEquals(12.0, col0Sum, DELTA); // 1+4+7 = 12

            // Test min/max
            double min = m.streamHorizontal().min().orElse(0.0);
            assertEquals(1.0, min, DELTA);

            double max = m.streamHorizontal().max().orElse(0.0);
            assertEquals(9.0, max, DELTA);

            // Test average
            double avg = m.streamHorizontal().average().orElse(0.0);
            assertEquals(5.0, avg, DELTA);

            // Test diagonal operations
            double diagonalSum = m.streamMainDiagonal().sum();
            assertEquals(15.0, diagonalSum, DELTA); // 1+5+9 = 15

            double antiDiagonalSum = m.streamAntiDiagonal().sum();
            assertEquals(15.0, antiDiagonalSum, DELTA); // 3+5+7 = 15
        }

        @Test
        public void testRowColumnStatistics() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });

            // Test statistics on individual rows
            List<Double> rowSums = m.streamRows().map(row -> row.sum()).toList();
            assertEquals(3, rowSums.size());
            assertEquals(6.0, rowSums.get(0), DELTA); // 1+2+3
            assertEquals(15.0, rowSums.get(1), DELTA); // 4+5+6
            assertEquals(24.0, rowSums.get(2), DELTA); // 7+8+9

            // Test statistics on individual columns
            List<Double> colSums = m.streamColumns().map(col -> col.sum()).toList();
            assertEquals(3, colSums.size());
            assertEquals(12.0, colSums.get(0), DELTA); // 1+4+7
            assertEquals(15.0, colSums.get(1), DELTA); // 2+5+8
            assertEquals(18.0, colSums.get(2), DELTA); // 3+6+9
        }

        // ============ Edge Case Tests ============

        @Test
        public void testEmptyMatrixOperations() {
            DoubleMatrix empty = DoubleMatrix.empty();

            assertTrue(empty.flatten().isEmpty());
            assertEquals(0, empty.copy().rowCount);
            assertEquals(empty, empty.transpose());
            assertEquals(empty, empty.rotate90());

            DoubleMatrix extended = empty.resize(2, 2, 5.5);
            assertEquals(2, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals(5.5, extended.get(0, 0), DELTA);
        }

        @Test
        public void testArithmeticEdgeCases() {
            // Test with zero matrix
            DoubleMatrix zeros = DoubleMatrix.of(new double[][] { { 0.0, 0.0 }, { 0.0, 0.0 } });
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });

            DoubleMatrix addZero = m.add(zeros);
            assertEquals(m, addZero);

            DoubleMatrix subtractZero = m.subtract(zeros);
            assertEquals(m, subtractZero);

            // Test multiplication with zero matrix
            DoubleMatrix multiplyZero = m.multiply(zeros);
            assertEquals(zeros, multiplyZero);

            // Test addition commutativity
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            assertEquals(m1.add(m2), m2.add(m1));

            // Test subtraction anti-commutativity
            DoubleMatrix diff1 = m1.subtract(m2);
            DoubleMatrix diff2 = m2.subtract(m1);
            assertEquals(diff1.get(0, 0), -diff2.get(0, 0), DELTA);
            assertEquals(diff1.get(1, 1), -diff2.get(1, 1), DELTA);
        }

        @Test
        public void testLargeMatrixArithmetic() {
            // Test with larger matrices
            double[][] arr1 = new double[10][10];
            double[][] arr2 = new double[10][10];

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    arr1[i][j] = i * 10.0 + j + 1.0;
                    arr2[i][j] = (i * 10.0 + j + 1.0) * 2.0;
                }
            }

            DoubleMatrix large1 = DoubleMatrix.of(arr1);
            DoubleMatrix large2 = DoubleMatrix.of(arr2);

            // Test addition
            DoubleMatrix largeSum = large1.add(large2);
            assertEquals(3.0, largeSum.get(0, 0), DELTA); // 1 + 2 = 3
            assertEquals(300.0, largeSum.get(9, 9), DELTA); // 100 + 200 = 300

            // Test sum of all elements
            double totalSum = largeSum.streamHorizontal().sum();
            assertEquals(15150.0, totalSum, DELTA); // 3*(1+2+...+100) = 3*5050 = 15150
        }

        @Test
        public void testScalarOperationsWithMap() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });

            // Scalar addition
            DoubleMatrix addScalar = m.map(x -> x + 10.0);
            assertEquals(11.0, addScalar.get(0, 0), DELTA);
            assertEquals(14.0, addScalar.get(1, 1), DELTA);

            // Scalar multiplication
            DoubleMatrix multiplyScalar = m.map(x -> x * 3.0);
            assertEquals(3.0, multiplyScalar.get(0, 0), DELTA);
            assertEquals(12.0, multiplyScalar.get(1, 1), DELTA);

            // Scalar division
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 10.0, 20.0 }, { 30.0, 40.0 } });
            DoubleMatrix divideScalar = m2.map(x -> x / 10.0);
            assertEquals(1.0, divideScalar.get(0, 0), DELTA);
            assertEquals(4.0, divideScalar.get(1, 1), DELTA);
        }

        @Test
        public void testElementWiseMultiplyWithZipWith() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 2.0, 3.0 }, { 4.0, 5.0 } });

            // Element-wise multiplication
            DoubleMatrix elementWiseProduct = m1.zipWith(m2, (a, b) -> a * b);
            assertEquals(2.0, elementWiseProduct.get(0, 0), DELTA); // 1*2
            assertEquals(6.0, elementWiseProduct.get(0, 1), DELTA); // 2*3
            assertEquals(12.0, elementWiseProduct.get(1, 0), DELTA); // 3*4
            assertEquals(20.0, elementWiseProduct.get(1, 1), DELTA); // 4*5

            // Element-wise division
            DoubleMatrix elementWiseDivision = m2.zipWith(m1, (a, b) -> a / b);
            assertEquals(2.0, elementWiseDivision.get(0, 0), DELTA); // 2/1
            assertEquals(1.5, elementWiseDivision.get(0, 1), DELTA); // 3/2
            assertEquals(1.3333, elementWiseDivision.get(1, 0), 0.001); // 4/3
            assertEquals(1.25, elementWiseDivision.get(1, 1), DELTA); // 5/4
        }

        // ============ Special Double Value Tests ============

        @Test
        public void testSpecialDoubleValues_NaN() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { Double.NaN, 2.0 }, { 3.0, Double.NaN } });

            assertTrue(Double.isNaN(m.get(0, 0)));
            assertEquals(2.0, m.get(0, 1), DELTA);
            assertTrue(Double.isNaN(m.get(1, 1)));

            // Test operations with NaN
            DoubleMatrix doubled = m.map(x -> x * 2.0);
            assertTrue(Double.isNaN(doubled.get(0, 0))); // NaN * 2 = NaN
            assertEquals(4.0, doubled.get(0, 1), DELTA);
        }

        @Test
        public void testSpecialDoubleValues_Infinity() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY }, { 3.0, 4.0 } });

            assertTrue(Double.isInfinite(m.get(0, 0)));
            assertEquals(Double.POSITIVE_INFINITY, m.get(0, 0), DELTA);
            assertTrue(Double.isInfinite(m.get(0, 1)));
            assertEquals(Double.NEGATIVE_INFINITY, m.get(0, 1), DELTA);

            // Test operations with infinity
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } });
            DoubleMatrix sum = m.add(m2);
            assertEquals(Double.POSITIVE_INFINITY, sum.get(0, 0), DELTA);
            assertEquals(Double.NEGATIVE_INFINITY, sum.get(0, 1), DELTA);
        }

        @Test
        public void testSpecialDoubleValues_NegativeZero() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { -0.0, 0.0 }, { 1.0, -1.0 } });

            assertEquals(-0.0, m.get(0, 0), DELTA);
            assertEquals(0.0, m.get(0, 1), DELTA);

            // Test that -0.0 and 0.0 are equal in value
            assertTrue(m.get(0, 0) == m.get(0, 1));
        }

        @Test
        public void testSpecialDoubleValues_VerySmallAndLarge() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { Double.MAX_VALUE, Double.MIN_VALUE }, { Double.MIN_NORMAL, 1e-308 } });

            assertEquals(Double.MAX_VALUE, m.get(0, 0), DELTA);
            assertEquals(Double.MIN_VALUE, m.get(0, 1), DELTA);
            assertEquals(Double.MIN_NORMAL, m.get(1, 0), DELTA);

            // Test operations with very large values
            DoubleMatrix doubled = m.map(x -> x * 2.0);
            assertEquals(Double.POSITIVE_INFINITY, doubled.get(0, 0), DELTA); // Overflow to infinity
        }

        @Test
        public void testPrecisionWithSmallValues() {
            // Test precision with small decimal values
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 0.1 + 0.2, 0.3 }, { 1.0 / 3.0, 0.333333 } });

            // Due to floating point precision, 0.1 + 0.2 != 0.3 exactly
            assertEquals(0.3, m.get(0, 0), DELTA);
            assertEquals(0.3, m.get(0, 0), 1e-10); // But close enough with tolerance

            assertEquals(0.3, m.get(0, 1), DELTA);
        }

        @Test
        public void testConversionAccuracy_FromInt() {
            // Test that large int values convert accurately to double
            int[][] ints = { { Integer.MAX_VALUE, Integer.MIN_VALUE }, { 1000000, -1000000 } };
            DoubleMatrix m = DoubleMatrix.from(ints);

            assertEquals(Integer.MAX_VALUE, m.get(0, 0), DELTA);
            assertEquals(Integer.MIN_VALUE, m.get(0, 1), DELTA);
            assertEquals(1000000.0, m.get(1, 0), DELTA);
            assertEquals(-1000000.0, m.get(1, 1), DELTA);
        }

        @Test
        public void testConversionAccuracy_FromLong() {
            // Test that long values convert to double (may lose precision for very large longs)
            long[][] longs = { { Long.MAX_VALUE, Long.MIN_VALUE }, { 1000000000000L, -1000000000000L } };
            DoubleMatrix m = DoubleMatrix.from(longs);

            // Note: Very large long values may lose precision when converted to double
            assertEquals(Long.MAX_VALUE, m.get(0, 0), 1.0); // Large tolerance for precision loss
            assertEquals(Long.MIN_VALUE, m.get(0, 1), 1.0);
            assertEquals(1000000000000.0, m.get(1, 0), DELTA);
            assertEquals(-1000000000000.0, m.get(1, 1), DELTA);
        }

        @Test
        public void testConversionAccuracy_FromFloat() {
            // Test that float values convert accurately to double
            float[][] floats = { { 1.5f, 2.5f }, { Float.MAX_VALUE, Float.MIN_VALUE } };
            DoubleMatrix m = DoubleMatrix.from(floats);

            assertEquals(1.5, m.get(0, 0), DELTA);
            assertEquals(2.5, m.get(0, 1), DELTA);
            assertEquals(Float.MAX_VALUE, m.get(1, 0), 1e30); // Large tolerance for float max
            assertEquals(Float.MIN_VALUE, m.get(1, 1), 1e-40);
        }

        // ============ Additional Coverage Tests for Branches ============

        @Test
        public void testBoxed_moreRowsThanCols() {
            // Test the else branch (rows > columnCount) in boxed() method
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 }, { 5.5, 6.5 }, { 7.5, 8.5 } }); // 4 rows, 2 columnCount
            Matrix<Double> boxed = m.boxed();
            assertEquals(4, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(Double.valueOf(1.5), boxed.get(0, 0));
            assertEquals(Double.valueOf(8.5), boxed.get(3, 1));
            assertEquals(Double.valueOf(5.5), boxed.get(2, 0));
        }

        @Test
        public void testRotate90_moreRowsThanCols() {
            // Test the else branch (rows > columnCount) in rotate90() method
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } }); // 3 rows, 2 columnCount
            DoubleMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(3, rotated.columnCount());
            // After 90-degree clockwise rotation:
            // Original:     Rotated:
            // 1 2           5 3 1
            // 3 4    =>     6 4 2
            // 5 6
            assertEquals(5.0, rotated.get(0, 0), DELTA);
            assertEquals(3.0, rotated.get(0, 1), DELTA);
            assertEquals(1.0, rotated.get(0, 2), DELTA);
            assertEquals(6.0, rotated.get(1, 0), DELTA);
            assertEquals(4.0, rotated.get(1, 1), DELTA);
            assertEquals(2.0, rotated.get(1, 2), DELTA);
        }

        @Test
        public void testRotate270_moreRowsThanCols() {
            // Test the else branch (rows > columnCount) in rotate270() method
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } }); // 3 rows, 2 columnCount
            DoubleMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(3, rotated.columnCount());
            // After 270-degree clockwise rotation (or 90 counter-clockwise):
            assertEquals(2.0, rotated.get(0, 0), DELTA);
            assertEquals(4.0, rotated.get(0, 1), DELTA);
            assertEquals(6.0, rotated.get(0, 2), DELTA);
        }

        @Test
        public void testTranspose_moreRowsThanCols() {
            // Test the else branch (rows > columnCount) in transpose() method
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } }); // 3 rows, 2 columnCount
            DoubleMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(3, transposed.columnCount());
            assertEquals(1.0, transposed.get(0, 0), DELTA);
            assertEquals(3.0, transposed.get(0, 1), DELTA);
            assertEquals(5.0, transposed.get(0, 2), DELTA);
            assertEquals(2.0, transposed.get(1, 0), DELTA);
            assertEquals(4.0, transposed.get(1, 1), DELTA);
            assertEquals(6.0, transposed.get(1, 2), DELTA);
        }

        @Test
        public void testNaNHandling() {
            // Test operations with NaN values
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, Double.NaN }, { Double.NaN, 4.0 } });

            // Test get
            assertEquals(1.0, m.get(0, 0), DELTA);
            assertTrue(Double.isNaN(m.get(0, 1)));
            assertTrue(Double.isNaN(m.get(1, 0)));
            assertEquals(4.0, m.get(1, 1), DELTA);

            // Test map with NaN
            DoubleMatrix mapped = m.map(x -> Double.isNaN(x) ? 0.0 : x * 2);
            assertEquals(2.0, mapped.get(0, 0), DELTA);
            assertEquals(0.0, mapped.get(0, 1), DELTA); // NaN replaced with 0
            assertEquals(0.0, mapped.get(1, 0), DELTA); // NaN replaced with 0
            assertEquals(8.0, mapped.get(1, 1), DELTA);

            // Test replaceIf with NaN
            DoubleMatrix replaced = DoubleMatrix.of(new double[][] { { 1.0, Double.NaN }, { Double.NaN, 4.0 } });
            replaced.replaceIf(Double::isNaN, -1.0);
            assertEquals(1.0, replaced.get(0, 0), DELTA);
            assertEquals(-1.0, replaced.get(0, 1), DELTA);
            assertEquals(-1.0, replaced.get(1, 0), DELTA);
            assertEquals(4.0, replaced.get(1, 1), DELTA);
        }

        @Test
        public void testInfinityHandling() {
            // Test operations with Infinity values
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY }, { 1.0, -1.0 } });

            assertEquals(Double.POSITIVE_INFINITY, m.get(0, 0), DELTA);
            assertEquals(Double.NEGATIVE_INFINITY, m.get(0, 1), DELTA);

            // Test addition with infinity
            DoubleMatrix result = m.add(DoubleMatrix.of(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } }));
            assertEquals(Double.POSITIVE_INFINITY, result.get(0, 0), DELTA);
            assertEquals(Double.NEGATIVE_INFINITY, result.get(0, 1), DELTA);

            // Test replaceIf with infinity
            m.replaceIf(Double::isInfinite, 999.0);
            assertEquals(999.0, m.get(0, 0), DELTA);
            assertEquals(999.0, m.get(0, 1), DELTA);
            assertEquals(1.0, m.get(1, 0), DELTA);
            assertEquals(-1.0, m.get(1, 1), DELTA);
        }

        @Test
        public void testForEach_largeMatrix() {
            // Test forEach with a large matrix to potentially trigger parallel execution path
            // Create a 100x100 matrix (10,000 elements should exceed parallel threshold)
            double[][] data = new double[100][100];
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    data[i][j] = i * 100 + j;
                }
            }
            DoubleMatrix m = DoubleMatrix.of(data);

            List<Double> values = new ArrayList<>();
            m.forEach(v -> {
                synchronized (values) {
                    values.add(v);
                }
            });

            assertEquals(10000, values.size());
            // Verify sum to ensure all elements were processed
            double sum = values.stream().mapToDouble(Double::doubleValue).sum();
            assertEquals(49995000.0, sum, 1.0); // Sum of 0 to 9999
        }

        @Test
        public void testExtend_smallerWithZeroDefault() {
            // Test extend where newRows < rows and newColumnCount < columnCount with default value 0
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleMatrix extended = m.resize(1, 2, 0.0);
            assertEquals(1, extended.rowCount());
            assertEquals(2, extended.columnCount());
            assertEquals(1.0, extended.get(0, 0), DELTA);
            assertEquals(2.0, extended.get(0, 1), DELTA);
        }

        @Test
        public void testMapToObj_withNullElements() {
            // Test mapToObj to ensure proper object type conversion
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
            Matrix<String> stringMatrix = m.mapToObj(d -> String.format("%.1f", d), String.class);

            assertEquals(2, stringMatrix.rowCount());
            assertEquals(2, stringMatrix.columnCount());
            assertEquals("1.5", stringMatrix.get(0, 0));
            assertEquals("4.5", stringMatrix.get(1, 1));
        }
    }

    @Nested
    @Tag("2510")
    class DoubleMatrix2510Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            DoubleMatrix m = new DoubleMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testConstructor_withNullArray() {
            DoubleMatrix m = new DoubleMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withEmptyArray() {
            DoubleMatrix m = new DoubleMatrix(new double[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withSingleElement() {
            DoubleMatrix m = new DoubleMatrix(new double[][] { { 42.5 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42.5, m.get(0, 0));
        }

        // ============ Factory Method Tests ============

        @Test
        public void testEmpty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());
            assertSame(DoubleMatrix.empty(), DoubleMatrix.empty());
        }

        @Test
        public void testOf_withValidArray() {
            double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            DoubleMatrix m = DoubleMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
        }

        @Test
        public void testOf_withNullArray() {
            DoubleMatrix m = DoubleMatrix.of((double[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withEmptyArray() {
            DoubleMatrix m = DoubleMatrix.of(new double[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray() {
            int[][] ints = { { 1, 2 }, { 3, 4 } };
            DoubleMatrix m = DoubleMatrix.from(ints);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testCreateFromIntArray_withNull() {
            DoubleMatrix m = DoubleMatrix.from((int[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray_withEmpty() {
            DoubleMatrix m = DoubleMatrix.from(new int[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray_withJaggedArray() {
            int[][] jagged = { { 1, 2 }, { 3 } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(jagged));
        }

        @Test
        public void testCreateFromIntArray_withNullRow() {
            int[][] nullRow = { { 1, 2 }, null };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(nullRow));
        }

        @Test
        public void testCreateFromIntArray_withNullFirstRow() {
            int[][] nullFirstRow = { null, { 1, 2 } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(nullFirstRow));
        }

        @Test
        public void testCreateFromLongArray() {
            long[][] longs = { { 1L, 2L }, { 3L, 4L } };
            DoubleMatrix m = DoubleMatrix.from(longs);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testCreateFromLongArray_withNull() {
            DoubleMatrix m = DoubleMatrix.from((long[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromLongArray_withJaggedArray() {
            long[][] jagged = { { 1L, 2L }, { 3L } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(jagged));
        }

        @Test
        public void testCreateFromLongArray_withNullRow() {
            long[][] nullRow = { { 1L, 2L }, null };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(nullRow));
        }

        @Test
        public void testCreateFromFloatArray() {
            float[][] floats = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            DoubleMatrix m = DoubleMatrix.from(floats);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0), 0.001);
            assertEquals(4.0, m.get(1, 1), 0.001);
        }

        @Test
        public void testCreateFromFloatArray_withNull() {
            DoubleMatrix m = DoubleMatrix.from((float[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromFloatArray_withJaggedArray() {
            float[][] jagged = { { 1.0f, 2.0f }, { 3.0f } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(jagged));
        }

        @Test
        public void testCreateFromFloatArray_withNullRow() {
            float[][] nullRow = { { 1.0f, 2.0f }, null };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(nullRow));
        }

        @Test
        public void testRandom() {
            DoubleMatrix m = DoubleMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertNotNull(m.get(0, i));
            }
        }

        @Test
        public void testRandom_withZeroLength() {
            DoubleMatrix m = DoubleMatrix.random(0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRepeat() {
            DoubleMatrix m = DoubleMatrix.repeat(1, 5, 3.14);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(3.14, m.get(0, i));
            }
        }

        @Test
        public void testRepeat_withZeroLength() {
            DoubleMatrix m = DoubleMatrix.repeat(1, 0, 1.0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testDiagonalLU2RD() {
            DoubleMatrix m = DoubleMatrix.mainDiagonal(new double[] { 1.0, 2.0, 3.0 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(2.0, m.get(1, 1));
            assertEquals(3.0, m.get(2, 2));
            assertEquals(0.0, m.get(0, 1));
            assertEquals(0.0, m.get(1, 0));
        }

        @Test
        public void testDiagonalLU2RD_withNull() {
            DoubleMatrix m = DoubleMatrix.mainDiagonal(null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonalRU2LD() {
            DoubleMatrix m = DoubleMatrix.antiDiagonal(new double[] { 1.0, 2.0, 3.0 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 2));
            assertEquals(2.0, m.get(1, 1));
            assertEquals(3.0, m.get(2, 0));
            assertEquals(0.0, m.get(0, 0));
            assertEquals(0.0, m.get(2, 2));
        }

        @Test
        public void testDiagonalRU2LD_withNull() {
            DoubleMatrix m = DoubleMatrix.antiDiagonal(null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            DoubleMatrix m = DoubleMatrix.diagonals(new double[] { 1.0, 4.0 }, new double[] { 2.0, 3.0 });
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(2.0, m.get(0, 1));
            assertEquals(3.0, m.get(1, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            DoubleMatrix m = DoubleMatrix.diagonals(new double[] { 1.0, 2.0, 3.0 }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(2.0, m.get(1, 1));
            assertEquals(3.0, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            DoubleMatrix m = DoubleMatrix.diagonals(null, new double[] { 1.0, 2.0, 3.0 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 2));
            assertEquals(2.0, m.get(1, 1));
            assertEquals(3.0, m.get(2, 0));
        }

        @Test
        public void testDiagonal_withBothEmpty() {
            DoubleMatrix m = DoubleMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.diagonals(new double[] { 1.0, 2.0 }, new double[] { 1.0, 2.0, 3.0 }));
        }

        @Test
        public void testUnbox() {
            Matrix<Double> boxed = Matrix.of(new Double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m = DoubleMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testUnbox_withNullValues() {
            Matrix<Double> boxed = Matrix.of(new Double[][] { { 1.0, null }, { null, 4.0 } });
            DoubleMatrix m = DoubleMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(0.0, m.get(0, 1));
            assertEquals(0.0, m.get(1, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        // ============ Element Access Tests ============

        @Test
        public void testComponentType() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0 } });
            assertEquals(double.class, m.componentType());
        }

        @Test
        public void testGet() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertEquals(1.0, m.get(0, 0));
            assertEquals(2.0, m.get(0, 1));
            assertEquals(3.0, m.get(1, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testGet_withPoint() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertEquals(2.0, m.get(Point.of(0, 1)));
            assertEquals(3.0, m.get(Point.of(1, 0)));
        }

        @Test
        public void testSet() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.set(0, 1, 9.0);
            assertEquals(9.0, m.get(0, 1));
        }

        @Test
        public void testSet_withPoint() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.set(Point.of(0, 1), 9.0);
            assertEquals(9.0, m.get(0, 1));
        }

        // ============ Neighbor Tests ============

        @Test
        public void testUpOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1.0, up.get());
        }

        @Test
        public void testUpOf_firstRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble up = m.above(0, 0);
            assertFalse(up.isPresent());
        }

        @Test
        public void testDownOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3.0, down.get());
        }

        @Test
        public void testDownOf_lastRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble down = m.below(1, 0);
            assertFalse(down.isPresent());
        }

        @Test
        public void testLeftOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1.0, left.get());
        }

        @Test
        public void testLeftOf_firstColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble left = m.left(0, 0);
            assertFalse(left.isPresent());
        }

        @Test
        public void testRightOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2.0, right.get());
        }

        @Test
        public void testRightOf_lastColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble right = m.right(0, 1);
            assertFalse(right.isPresent());
        }

        // ============ Row/Column Access Tests ============

        @Test
        public void testRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            double[] row = m.rowView(0);
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, row);
        }

        @Test
        public void testRow_invalidIndex() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(2));
        }

        @Test
        public void testColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            double[] col = m.columnCopy(0);
            assertArrayEquals(new double[] { 1.0, 4.0 }, col);
        }

        @Test
        public void testColumn_invalidIndex() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setRow(0, new double[] { 7.0, 8.0 });
            assertEquals(7.0, m.get(0, 0));
            assertEquals(8.0, m.get(0, 1));
        }

        @Test
        public void testSetRow_wrongLength() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new double[] { 7.0 }));
        }

        @Test
        public void testSetColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setColumn(0, new double[] { 7.0, 8.0 });
            assertEquals(7.0, m.get(0, 0));
            assertEquals(8.0, m.get(1, 0));
        }

        @Test
        public void testSetColumn_wrongLength() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new double[] { 7.0 }));
        }

        @Test
        public void testUpdateRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateRow(0, x -> x * 2);
            assertEquals(2.0, m.get(0, 0));
            assertEquals(4.0, m.get(0, 1));
        }

        @Test
        public void testUpdateColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateColumn(0, x -> x + 10);
            assertEquals(11.0, m.get(0, 0));
            assertEquals(13.0, m.get(1, 0));
        }

        // ============ Diagonal Tests ============

        @Test
        public void testGetLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            double[] diagonal = m.getMainDiagonal();
            assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, diagonal);
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setMainDiagonal(new double[] { 9.0, 8.0 });
            assertEquals(9.0, m.get(0, 0));
            assertEquals(8.0, m.get(1, 1));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new double[] { 9.0, 8.0 }));
        }

        @Test
        public void testSetLU2RD_tooShort() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new double[] { 9.0 }));
        }

        @Test
        public void testUpdateLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateMainDiagonal(x -> x * 2);
            assertEquals(2.0, m.get(0, 0));
            assertEquals(8.0, m.get(1, 1));
        }

        @Test
        public void testGetRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            double[] diagonal = m.getAntiDiagonal();
            assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, diagonal);
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setAntiDiagonal(new double[] { 9.0, 8.0 });
            assertEquals(9.0, m.get(0, 1));
            assertEquals(8.0, m.get(1, 0));
        }

        @Test
        public void testUpdateRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateAntiDiagonal(x -> x + 10);
            assertEquals(12.0, m.get(0, 1));
            assertEquals(13.0, m.get(1, 0));
        }

        // ============ Update/Transform Tests ============

        @Test
        public void testUpdateAll_unaryOperator() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateAll(x -> x * 2);
            assertEquals(2.0, m.get(0, 0));
            assertEquals(4.0, m.get(0, 1));
            assertEquals(6.0, m.get(1, 0));
            assertEquals(8.0, m.get(1, 1));
        }

        @Test
        public void testUpdateAll_biFunction() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateAll((i, j) -> (double) (i + j));
            assertEquals(0.0, m.get(0, 0));
            assertEquals(1.0, m.get(0, 1));
            assertEquals(1.0, m.get(1, 0));
            assertEquals(2.0, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_predicate() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.replaceIf(x -> x > 2, 0.0);
            assertEquals(1.0, m.get(0, 0));
            assertEquals(2.0, m.get(0, 1));
            assertEquals(0.0, m.get(1, 0));
            assertEquals(0.0, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_biPredicate() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.replaceIf((i, j) -> i == j, 0.0);
            assertEquals(0.0, m.get(0, 0));
            assertEquals(2.0, m.get(0, 1));
            assertEquals(3.0, m.get(1, 0));
            assertEquals(0.0, m.get(1, 1));
        }

        @Test
        public void testMap() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m.map(x -> x * 2);
            assertEquals(2.0, result.get(0, 0));
            assertEquals(4.0, result.get(0, 1));
            assertEquals(6.0, result.get(1, 0));
            assertEquals(8.0, result.get(1, 1));
            assertEquals(1.0, m.get(0, 0));
        }

        @Test
        public void testMapToInt() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            IntMatrix result = m.mapToInt(x -> (int) x);
            assertEquals(1, result.get(0, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void testMapToLong() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            LongMatrix result = m.mapToLong(x -> (long) x);
            assertEquals(1L, result.get(0, 0));
            assertEquals(4L, result.get(1, 1));
        }

        @Test
        public void testMapToObj() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Matrix<String> result = m.mapToObj(x -> String.valueOf(x), String.class);
            assertEquals("1.0", result.get(0, 0));
            assertEquals("4.0", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_singleValue() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.fill(9.0);
            assertEquals(9.0, m.get(0, 0));
            assertEquals(9.0, m.get(0, 1));
            assertEquals(9.0, m.get(1, 0));
            assertEquals(9.0, m.get(1, 1));
        }

        @Test
        public void testFill_array() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.copyFrom(new double[][] { { 7.0, 8.0 }, { 9.0, 10.0 } });
            assertEquals(7.0, m.get(0, 0));
            assertEquals(8.0, m.get(0, 1));
            assertEquals(9.0, m.get(1, 0));
            assertEquals(10.0, m.get(1, 1));
        }

        @Test
        public void testFill_withPosition() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            m.copyFrom(1, 1, new double[][] { { 11.0, 12.0 }, { 13.0, 14.0 } });
            assertEquals(1.0, m.get(0, 0));
            assertEquals(11.0, m.get(1, 1));
            assertEquals(12.0, m.get(1, 2));
            assertEquals(13.0, m.get(2, 1));
            assertEquals(14.0, m.get(2, 2));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix copy = m.copy();
            assertEquals(1.0, copy.get(0, 0));
            assertEquals(4.0, copy.get(1, 1));
            copy.set(0, 0, 99.0);
            assertEquals(1.0, m.get(0, 0));
        }

        @Test
        public void testCopy_rows() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            DoubleMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(3.0, copy.get(0, 0));
            assertEquals(6.0, copy.get(1, 1));
        }

        @Test
        public void testCopy_subMatrix() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleMatrix copy = m.copy(1, 3, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(5.0, copy.get(0, 0));
            assertEquals(9.0, copy.get(1, 1));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_simple() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1.0, extended.get(0, 0));
            assertEquals(4.0, extended.get(1, 1));
            assertEquals(0.0, extended.get(2, 2));
        }

        @Test
        public void testExtend_withDefaultValue() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix extended = m.resize(3, 3, 9.0);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(9.0, extended.get(2, 2));
        }

        @Test
        public void testExtend_directions() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 5.0 } });
            DoubleMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(5.0, extended.get(1, 1));
            assertEquals(0.0, extended.get(0, 0));
        }

        @Test
        public void testExtend_directionsWithDefault() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 5.0 } });
            DoubleMatrix extended = m.extend(1, 1, 1, 1, 9.0);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(5.0, extended.get(1, 1));
            assertEquals(9.0, extended.get(0, 0));
        }

        // ============ Flip/Reverse Tests ============

        @Test
        public void testReverseH() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.flipInPlaceHorizontally();
            assertEquals(2.0, m.get(0, 0));
            assertEquals(1.0, m.get(0, 1));
            assertEquals(4.0, m.get(1, 0));
            assertEquals(3.0, m.get(1, 1));
        }

        @Test
        public void testReverseV() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.flipInPlaceVertically();
            assertEquals(3.0, m.get(0, 0));
            assertEquals(4.0, m.get(0, 1));
            assertEquals(1.0, m.get(1, 0));
            assertEquals(2.0, m.get(1, 1));
        }

        @Test
        public void testFlipH() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix flipped = m.flipHorizontally();
            assertEquals(2.0, flipped.get(0, 0));
            assertEquals(1.0, flipped.get(0, 1));
            assertEquals(1.0, m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix flipped = m.flipVertically();
            assertEquals(3.0, flipped.get(0, 0));
            assertEquals(4.0, flipped.get(0, 1));
            assertEquals(1.0, m.get(0, 0));
        }

        // ============ Rotation Tests ============

        @Test
        public void testRotate90() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3.0, rotated.get(0, 0));
            assertEquals(1.0, rotated.get(0, 1));
            assertEquals(4.0, rotated.get(1, 0));
            assertEquals(2.0, rotated.get(1, 1));
        }

        @Test
        public void testRotate180() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate180();
            assertEquals(4.0, rotated.get(0, 0));
            assertEquals(3.0, rotated.get(0, 1));
            assertEquals(2.0, rotated.get(1, 0));
            assertEquals(1.0, rotated.get(1, 1));
        }

        @Test
        public void testRotate270() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate270();
            assertEquals(2.0, rotated.get(0, 0));
            assertEquals(4.0, rotated.get(0, 1));
            assertEquals(1.0, rotated.get(1, 0));
            assertEquals(3.0, rotated.get(1, 1));
        }

        // ============ Transpose Tests ============

        @Test
        public void testTranspose() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1.0, transposed.get(0, 0));
            assertEquals(4.0, transposed.get(0, 1));
            assertEquals(3.0, transposed.get(2, 0));
            assertEquals(6.0, transposed.get(2, 1));
        }

        @Test
        public void testTranspose_square() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix transposed = m.transpose();
            assertEquals(1.0, transposed.get(0, 0));
            assertEquals(3.0, transposed.get(0, 1));
            assertEquals(2.0, transposed.get(1, 0));
            assertEquals(4.0, transposed.get(1, 1));
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 } });
            DoubleMatrix reshaped = m.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals(1.0, reshaped.get(0, 0));
            assertEquals(4.0, reshaped.get(1, 0));
        }

        @Test
        public void testReshape_invalidSize() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix reshaped = m.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals(1.0, reshaped.get(0, 0));
            assertEquals(4.0, reshaped.get(1, 0));

        }

        // ============ Repelem Tests ============

        @Test
        public void testRepelem() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m.repeatElements(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1.0, result.get(0, 0));
            assertEquals(1.0, result.get(0, 1));
            assertEquals(1.0, result.get(1, 0));
            assertEquals(1.0, result.get(1, 1));
            assertEquals(4.0, result.get(2, 2));
            assertEquals(4.0, result.get(3, 3));
        }

        // ============ Repmat Tests ============

        @Test
        public void testRepmat() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m.repeatMatrix(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1.0, result.get(0, 0));
            assertEquals(2.0, result.get(0, 1));
            assertEquals(1.0, result.get(0, 2));
            assertEquals(2.0, result.get(0, 3));
            assertEquals(1.0, result.get(2, 0));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleList flat = m.flatten();
            assertEquals(4, flat.size());
            assertEquals(1.0, flat.get(0));
            assertEquals(2.0, flat.get(1));
            assertEquals(3.0, flat.get(2));
            assertEquals(4.0, flat.get(3));
        }

        @Test
        public void testFlatOp() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            final double[] sum = { 0.0 };
            m.applyOnFlattened(row -> {
                for (double val : row) {
                    sum[0] += val;
                }
            });
            assertEquals(10.0, sum[0]);
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix stacked = m1.stackVertically(m2);
            assertEquals(4, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1.0, stacked.get(0, 0));
            assertEquals(5.0, stacked.get(2, 0));
        }

        @Test
        public void testVstack_incompatibleColumns() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertEquals(1.0, stacked.get(0, 0));
            assertEquals(5.0, stacked.get(0, 2));
        }

        @Test
        public void testHstack_incompatibleRows() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Tests ============

        @Test
        public void testAdd() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix result = m1.add(m2);
            assertEquals(6.0, result.get(0, 0));
            assertEquals(8.0, result.get(0, 1));
            assertEquals(10.0, result.get(1, 0));
            assertEquals(12.0, result.get(1, 1));
        }

        @Test
        public void testAdd_incompatibleSize() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void testSubtract() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m1.subtract(m2);
            assertEquals(4.0, result.get(0, 0));
            assertEquals(4.0, result.get(0, 1));
            assertEquals(4.0, result.get(1, 0));
            assertEquals(4.0, result.get(1, 1));
        }

        @Test
        public void testMultiply() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 2.0, 0.0 }, { 1.0, 2.0 } });
            DoubleMatrix result = m1.multiply(m2);
            assertEquals(4.0, result.get(0, 0));
            assertEquals(4.0, result.get(0, 1));
            assertEquals(10.0, result.get(1, 0));
            assertEquals(8.0, result.get(1, 1));
        }

        @Test
        public void testMultiply_incompatibleSize() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        // ============ Conversion Tests ============

        @Test
        public void testBoxed() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Matrix<Double> boxed = m.boxed();
            assertEquals(Double.valueOf(1.0), boxed.get(0, 0));
            assertEquals(Double.valueOf(4.0), boxed.get(1, 1));
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith_binary() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix result = m1.zipWith(m2, (a, b) -> a + b);
            assertEquals(6.0, result.get(0, 0));
            assertEquals(12.0, result.get(1, 1));
        }

        @Test
        public void testZipWith_ternary() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } });
            DoubleMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
            assertEquals(7.0, result.get(0, 0));
            assertEquals(13.0, result.get(1, 1));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleStream stream = m.streamMainDiagonal();
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, result);
        }

        @Test
        public void testStreamRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleStream stream = m.streamAntiDiagonal();
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, result);
        }

        @Test
        public void testStreamH() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleStream stream = m.streamHorizontal();
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, result);
        }

        @Test
        public void testStreamH_withRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleStream stream = m.streamHorizontal(1);
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 3.0, 4.0 }, result);
        }

        @Test
        public void testStreamH_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            DoubleStream stream = m.streamHorizontal(1, 3);
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 3.0, 4.0, 5.0, 6.0 }, result);
        }

        @Test
        public void testStreamV() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleStream stream = m.streamVertical();
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 1.0, 3.0, 2.0, 4.0 }, result);
        }

        @Test
        public void testStreamV_withColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleStream stream = m.streamVertical(0);
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 1.0, 3.0 }, result);
        }

        @Test
        public void testStreamV_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleStream stream = m.streamVertical(1, 3);
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 2.0, 5.0, 3.0, 6.0 }, result);
        }

        @Test
        public void testStreamR() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<DoubleStream> stream = m.streamRows();
            assertEquals(2, stream.count());
        }

        @Test
        public void testStreamR_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            Stream<DoubleStream> stream = m.streamRows(1, 3);
            assertEquals(2, stream.count());
        }

        @Test
        public void testStreamC() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<DoubleStream> stream = m.streamColumns();
            assertEquals(2, stream.count());
        }

        @Test
        public void testStreamC_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            Stream<DoubleStream> stream = m.streamColumns(1, 3);
            assertEquals(2, stream.count());
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            final double[] sum = { 0.0 };
            m.forEach(val -> sum[0] += val);
            assertEquals(10.0, sum[0]);
        }

        @Test
        public void testForEach_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            final double[] sum = { 0.0 };
            m.forEach(1, 3, 1, 3, val -> sum[0] += val);
            assertEquals(28.0, sum[0]);
        }

        // ============ Inherited Methods Tests ============

        @Test
        public void testIsEmpty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertTrue(empty.isEmpty());
            DoubleMatrix notEmpty = DoubleMatrix.of(new double[][] { { 1.0 } });
            assertFalse(notEmpty.isEmpty());
        }

        @Test
        public void testIsSameShape() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
            assertTrue(m1.isSameShape(m2));
            assertFalse(m1.isSameShape(m3));
        }

        @Test
        public void testArray() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            double[][] array = m.backingArray();
            assertArrayEquals(new double[] { 1.0, 2.0 }, array[0]);
            assertArrayEquals(new double[] { 3.0, 4.0 }, array[1]);
        }

        @Test
        public void testReshape_singleParam() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 } });
            DoubleMatrix reshaped = m.reshape(3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        @Test
        public void testPointsLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsMainDiagonal();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsAntiDiagonal();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsH() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsHorizontal();
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsH_withRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsHorizontal(1);
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsH_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            Stream<Point> points = m.pointsHorizontal(1, 3);
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsV() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsVertical();
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsV_withColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsVertical(1);
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsV_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            Stream<Point> points = m.pointsVertical(1, 3);
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsR() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Stream<Point>> points = m.pointsRows();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsC() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Stream<Point>> points = m.pointsColumns();
            assertEquals(2, points.count());
        }

        @Test
        public void testForEach_biConsumer() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            final int[] count = { 0 };
            m.forEachIndex((i, j) -> count[0]++);
            assertEquals(4, count[0]);
        }

        @Test
        public void testForEach_biObjConsumer() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            final int[] count = { 0 };
            m.forEachIndex((i, j, matrix) -> count[0]++);
            assertEquals(4, count[0]);
        }

        @Test
        public void testAccept() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            final boolean[] called = { false };
            m.accept(matrix -> called[0] = true);
            assertTrue(called[0]);
        }

        @Test
        public void testApply() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            int result = m.apply(matrix -> matrix.rowCount() * matrix.columnCount());
            assertEquals(4, result);
        }

        // ============ Equality Tests ============

        @Test
        public void testHashCode() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void testEquals() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 5.0 } });
            assertTrue(m1.equals(m2));
            assertFalse(m1.equals(m3));
            assertTrue(m1.equals(m1));
            assertFalse(m1.equals(null));
            assertFalse(m1.equals("not a matrix"));
        }

        @Test
        public void testToString() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("4.0"));
        }

        @Test
        public void testPrintln() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            String result = m.println();
            assertNotNull(result);
        }

        @Test
        public void testPrintln_empty() {
            DoubleMatrix m = DoubleMatrix.empty();
            String result = m.println();
            assertNotNull(result);
        }
    }

    @Nested
    /**
     * Comprehensive unit tests for DoubleMatrix class covering all public methods.
     */
    @Tag("2511")
    class DoubleMatrix2511Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void testConstructor_withValidArray() {
            double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            DoubleMatrix m = new DoubleMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testConstructor_withNullArray() {
            DoubleMatrix m = new DoubleMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withEmptyArray() {
            DoubleMatrix m = new DoubleMatrix(new double[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void testConstructor_withSingleElement() {
            DoubleMatrix m = new DoubleMatrix(new double[][] { { 42.5 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42.5, m.get(0, 0));
        }

        @Test
        public void testConstructor_withLargerMatrix() {
            double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
            DoubleMatrix m = new DoubleMatrix(arr);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(5.0, m.get(1, 1));
        }

        // ============ Factory Method Tests ============

        @Test
        public void testEmpty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());
            assertSame(DoubleMatrix.empty(), DoubleMatrix.empty());
        }

        @Test
        public void testOf_withValidArray() {
            double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            DoubleMatrix m = DoubleMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
        }

        @Test
        public void testOf_withNullArray() {
            DoubleMatrix m = DoubleMatrix.of((double[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withEmptyArray() {
            DoubleMatrix m = DoubleMatrix.of(new double[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testOf_withEmptyRows() {
            DoubleMatrix m = DoubleMatrix.of(new double[3][0]);
            assertEquals(3, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testCreateFromIntArray() {
            int[][] ints = { { 1, 2 }, { 3, 4 } };
            DoubleMatrix m = DoubleMatrix.from(ints);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testCreateFromIntArray_withNull() {
            DoubleMatrix m = DoubleMatrix.from((int[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray_withEmpty() {
            DoubleMatrix m = DoubleMatrix.from(new int[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromIntArray_withJaggedArray() {
            int[][] jagged = { { 1, 2 }, { 3 } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(jagged));
        }

        @Test
        public void testCreateFromIntArray_withNullRow() {
            int[][] nullRow = { { 1, 2 }, null };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(nullRow));
        }

        @Test
        public void testCreateFromIntArray_withNullFirstRow() {
            int[][] nullFirstRow = { null, { 1, 2 } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(nullFirstRow));
        }

        @Test
        public void testCreateFromLongArray() {
            long[][] longs = { { 1L, 2L }, { 3L, 4L } };
            DoubleMatrix m = DoubleMatrix.from(longs);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testCreateFromLongArray_withNull() {
            DoubleMatrix m = DoubleMatrix.from((long[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromLongArray_withJaggedArray() {
            long[][] jagged = { { 1L, 2L }, { 3L } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(jagged));
        }

        @Test
        public void testCreateFromFloatArray() {
            float[][] floats = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            DoubleMatrix m = DoubleMatrix.from(floats);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0), 0.001);
            assertEquals(4.0, m.get(1, 1), 0.001);
        }

        @Test
        public void testCreateFromFloatArray_withNull() {
            DoubleMatrix m = DoubleMatrix.from((float[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testCreateFromFloatArray_withJaggedArray() {
            float[][] jagged = { { 1.0f, 2.0f }, { 3.0f } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(jagged));
        }

        @Test
        public void testRandom() {
            DoubleMatrix m = DoubleMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertNotNull(m.get(0, i));
            }
        }

        @Test
        public void testRandom_withZeroLength() {
            DoubleMatrix m = DoubleMatrix.random(0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRandom_withLargeLength() {
            DoubleMatrix m = DoubleMatrix.random(100);
            assertEquals(1, m.rowCount());
            assertEquals(100, m.columnCount());
        }

        @Test
        public void testRepeat() {
            DoubleMatrix m = DoubleMatrix.repeat(1, 5, 3.14);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(3.14, m.get(0, i));
            }
        }

        @Test
        public void testRepeat_withZeroLength() {
            DoubleMatrix m = DoubleMatrix.repeat(1, 0, 1.0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void testRepeat_withNegativeValue() {
            DoubleMatrix m = DoubleMatrix.repeat(1, 3, -5.5);
            assertEquals(1, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(-5.5, m.get(0, 0));
        }

        @Test
        public void testDiagonalLU2RD() {
            DoubleMatrix m = DoubleMatrix.mainDiagonal(new double[] { 1.0, 2.0, 3.0 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(2.0, m.get(1, 1));
            assertEquals(3.0, m.get(2, 2));
            assertEquals(0.0, m.get(0, 1));
            assertEquals(0.0, m.get(1, 0));
        }

        @Test
        public void testDiagonalLU2RD_withNull() {
            DoubleMatrix m = DoubleMatrix.mainDiagonal(null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonalLU2RD_withEmptyArray() {
            DoubleMatrix m = DoubleMatrix.mainDiagonal(new double[0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonalRU2LD() {
            DoubleMatrix m = DoubleMatrix.antiDiagonal(new double[] { 1.0, 2.0, 3.0 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 2));
            assertEquals(2.0, m.get(1, 1));
            assertEquals(3.0, m.get(2, 0));
            assertEquals(0.0, m.get(0, 0));
            assertEquals(0.0, m.get(2, 2));
        }

        @Test
        public void testDiagonalRU2LD_withNull() {
            DoubleMatrix m = DoubleMatrix.antiDiagonal(null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withBothDiagonals() {
            DoubleMatrix m = DoubleMatrix.diagonals(new double[] { 1.0, 4.0 }, new double[] { 2.0, 3.0 });
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(2.0, m.get(0, 1));
            assertEquals(3.0, m.get(1, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testDiagonal_withOnlyMainDiagonal() {
            DoubleMatrix m = DoubleMatrix.diagonals(new double[] { 1.0, 2.0, 3.0 }, null);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(2.0, m.get(1, 1));
            assertEquals(3.0, m.get(2, 2));
        }

        @Test
        public void testDiagonal_withOnlyAntiDiagonal() {
            DoubleMatrix m = DoubleMatrix.diagonals(null, new double[] { 1.0, 2.0, 3.0 });
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 2));
            assertEquals(2.0, m.get(1, 1));
            assertEquals(3.0, m.get(2, 0));
        }

        @Test
        public void testDiagonal_withBothEmpty() {
            DoubleMatrix m = DoubleMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void testDiagonal_withDifferentLengths() {
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.diagonals(new double[] { 1.0, 2.0 }, new double[] { 1.0, 2.0, 3.0 }));
        }

        @Test
        public void testDiagonal_singleElement() {
            DoubleMatrix m = DoubleMatrix.diagonals(new double[] { 5.0 }, new double[] { 7.0 });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(5.0, m.get(0, 0)); // Main diagonal takes precedence
        }

        @Test
        public void testUnbox() {
            Matrix<Double> boxed = Matrix.of(new Double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m = DoubleMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testUnbox_withNullValues() {
            Matrix<Double> boxed = Matrix.of(new Double[][] { { 1.0, null }, { null, 4.0 } });
            DoubleMatrix m = DoubleMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0));
            assertEquals(0.0, m.get(0, 1));
            assertEquals(0.0, m.get(1, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        // ============ Element Access Tests ============

        @Test
        public void testComponentType() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0 } });
            assertEquals(double.class, m.componentType());
        }

        @Test
        public void testGet() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertEquals(1.0, m.get(0, 0));
            assertEquals(2.0, m.get(0, 1));
            assertEquals(3.0, m.get(1, 0));
            assertEquals(4.0, m.get(1, 1));
        }

        @Test
        public void testGet_withPoint() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertEquals(2.0, m.get(Point.of(0, 1)));
            assertEquals(3.0, m.get(Point.of(1, 0)));
        }

        @Test
        public void testSet() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.set(0, 1, 9.0);
            assertEquals(9.0, m.get(0, 1));
        }

        @Test
        public void testSet_withPoint() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.set(Point.of(0, 1), 9.0);
            assertEquals(9.0, m.get(0, 1));
        }

        @Test
        public void testSet_negativeValue() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.set(0, 0, -99.9f);
            assertEquals(-99.9, m.get(0, 0), 0.0001);
        }

        // ============ Neighbor Tests ============

        @Test
        public void testUpOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1.0, up.get());
        }

        @Test
        public void testUpOf_firstRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble up = m.above(0, 0);
            assertFalse(up.isPresent());
        }

        @Test
        public void testDownOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3.0, down.get());
        }

        @Test
        public void testDownOf_lastRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble down = m.below(1, 0);
            assertFalse(down.isPresent());
        }

        @Test
        public void testLeftOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1.0, left.get());
        }

        @Test
        public void testLeftOf_firstColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble left = m.left(0, 0);
            assertFalse(left.isPresent());
        }

        @Test
        public void testRightOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2.0, right.get());
        }

        @Test
        public void testRightOf_lastColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble right = m.right(0, 1);
            assertFalse(right.isPresent());
        }

        // ============ Row/Column Access Tests ============

        @Test
        public void testRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            double[] row = m.rowView(0);
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, row);
        }

        @Test
        public void testRow_invalidIndex() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(-1));
            assertThrows(IllegalArgumentException.class, () -> m.rowView(2));
        }

        @Test
        public void testColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            double[] col = m.columnCopy(0);
            assertArrayEquals(new double[] { 1.0, 4.0 }, col);
        }

        @Test
        public void testColumn_invalidIndex() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(-1));
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(2));
        }

        @Test
        public void testSetRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setRow(0, new double[] { 7.0, 8.0 });
            assertEquals(7.0, m.get(0, 0));
            assertEquals(8.0, m.get(0, 1));
        }

        @Test
        public void testSetRow_wrongLength() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new double[] { 7.0 }));
        }

        @Test
        public void testSetColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setColumn(0, new double[] { 7.0, 8.0 });
            assertEquals(7.0, m.get(0, 0));
            assertEquals(8.0, m.get(1, 0));
        }

        @Test
        public void testSetColumn_wrongLength() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new double[] { 7.0 }));
        }

        @Test
        public void testUpdateRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateRow(0, x -> x * 2);
            assertEquals(2.0, m.get(0, 0));
            assertEquals(4.0, m.get(0, 1));
        }

        @Test
        public void testUpdateColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateColumn(0, x -> x + 10);
            assertEquals(11.0, m.get(0, 0));
            assertEquals(13.0, m.get(1, 0));
        }

        // ============ Diagonal Tests ============

        @Test
        public void testGetLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            double[] diagonal = m.getMainDiagonal();
            assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, diagonal);
        }

        @Test
        public void testGetLU2RD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void testSetLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setMainDiagonal(new double[] { 9.0, 8.0 });
            assertEquals(9.0, m.get(0, 0));
            assertEquals(8.0, m.get(1, 1));
        }

        @Test
        public void testSetLU2RD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new double[] { 9.0, 8.0 }));
        }

        @Test
        public void testSetLU2RD_tooShort() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new double[] { 9.0 }));
        }

        @Test
        public void testUpdateLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateMainDiagonal(x -> x * 2);
            assertEquals(2.0, m.get(0, 0));
            assertEquals(8.0, m.get(1, 1));
        }

        @Test
        public void testGetRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            double[] diagonal = m.getAntiDiagonal();
            assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, diagonal);
        }

        @Test
        public void testGetRU2LD_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            assertThrows(IllegalStateException.class, () -> m.getAntiDiagonal());
        }

        @Test
        public void testSetRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setAntiDiagonal(new double[] { 9.0, 8.0 });
            assertEquals(9.0, m.get(0, 1));
            assertEquals(8.0, m.get(1, 0));
        }

        @Test
        public void testUpdateRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateAntiDiagonal(x -> x + 10);
            assertEquals(12.0, m.get(0, 1));
            assertEquals(13.0, m.get(1, 0));
        }

        // ============ Update/Transform Tests ============

        @Test
        public void testUpdateAll_unaryOperator() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateAll(x -> x * 2);
            assertEquals(2.0, m.get(0, 0));
            assertEquals(4.0, m.get(0, 1));
            assertEquals(6.0, m.get(1, 0));
            assertEquals(8.0, m.get(1, 1));
        }

        @Test
        public void testUpdateAll_biFunction() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateAll((i, j) -> (double) (i + j));
            assertEquals(0.0, m.get(0, 0));
            assertEquals(1.0, m.get(0, 1));
            assertEquals(1.0, m.get(1, 0));
            assertEquals(2.0, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_predicate() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.replaceIf(x -> x > 2, 0.0);
            assertEquals(1.0, m.get(0, 0));
            assertEquals(2.0, m.get(0, 1));
            assertEquals(0.0, m.get(1, 0));
            assertEquals(0.0, m.get(1, 1));
        }

        @Test
        public void testReplaceIf_biPredicate() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.replaceIf((i, j) -> i == j, 0.0);
            assertEquals(0.0, m.get(0, 0));
            assertEquals(2.0, m.get(0, 1));
            assertEquals(3.0, m.get(1, 0));
            assertEquals(0.0, m.get(1, 1));
        }

        @Test
        public void testMap() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m.map(x -> x * 2);
            assertEquals(2.0, result.get(0, 0));
            assertEquals(4.0, result.get(0, 1));
            assertEquals(6.0, result.get(1, 0));
            assertEquals(8.0, result.get(1, 1));
            assertEquals(1.0, m.get(0, 0));
        }

        @Test
        public void testMapToObj() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Matrix<String> result = m.mapToObj(x -> String.valueOf(x), String.class);
            assertEquals("1.0", result.get(0, 0));
            assertEquals("4.0", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void testFill_singleValue() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.fill(9.0);
            assertEquals(9.0, m.get(0, 0));
            assertEquals(9.0, m.get(0, 1));
            assertEquals(9.0, m.get(1, 0));
            assertEquals(9.0, m.get(1, 1));
        }

        @Test
        public void testFill_array() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.copyFrom(new double[][] { { 7.0, 8.0 }, { 9.0, 10.0 } });
            assertEquals(7.0, m.get(0, 0));
            assertEquals(8.0, m.get(0, 1));
            assertEquals(9.0, m.get(1, 0));
            assertEquals(10.0, m.get(1, 1));
        }

        @Test
        public void testFill_withPosition() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            m.copyFrom(1, 1, new double[][] { { 11.0, 12.0 }, { 13.0, 14.0 } });
            assertEquals(1.0, m.get(0, 0));
            assertEquals(11.0, m.get(1, 1));
            assertEquals(12.0, m.get(1, 2));
            assertEquals(13.0, m.get(2, 1));
            assertEquals(14.0, m.get(2, 2));
        }

        // ============ Copy Tests ============

        @Test
        public void testCopy() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix copy = m.copy();
            assertEquals(1.0, copy.get(0, 0));
            assertEquals(4.0, copy.get(1, 1));
            copy.set(0, 0, 99.0);
            assertEquals(1.0, m.get(0, 0));
        }

        @Test
        public void testCopy_rows() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            DoubleMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(3.0, copy.get(0, 0));
            assertEquals(6.0, copy.get(1, 1));
        }

        @Test
        public void testCopy_subMatrix() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleMatrix copy = m.copy(1, 3, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(5.0, copy.get(0, 0));
            assertEquals(9.0, copy.get(1, 1));
        }

        // ============ Extend Tests ============

        @Test
        public void testExtend_simple() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1.0, extended.get(0, 0));
            assertEquals(4.0, extended.get(1, 1));
            assertEquals(0.0, extended.get(2, 2));
        }

        @Test
        public void testExtend_withDefaultValue() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix extended = m.resize(3, 3, 9.0);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(9.0, extended.get(2, 2));
        }

        @Test
        public void testExtend_directions() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 5.0 } });
            DoubleMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(5.0, extended.get(1, 1));
            assertEquals(0.0, extended.get(0, 0));
        }

        @Test
        public void testExtend_directionsWithDefault() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 5.0 } });
            DoubleMatrix extended = m.extend(1, 1, 1, 1, 9.0);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(5.0, extended.get(1, 1));
            assertEquals(9.0, extended.get(0, 0));
        }

        // ============ Flip/Reverse Tests ============

        @Test
        public void testReverseH() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.flipInPlaceHorizontally();
            assertEquals(2.0, m.get(0, 0));
            assertEquals(1.0, m.get(0, 1));
            assertEquals(4.0, m.get(1, 0));
            assertEquals(3.0, m.get(1, 1));
        }

        @Test
        public void testReverseV() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.flipInPlaceVertically();
            assertEquals(3.0, m.get(0, 0));
            assertEquals(4.0, m.get(0, 1));
            assertEquals(1.0, m.get(1, 0));
            assertEquals(2.0, m.get(1, 1));
        }

        @Test
        public void testFlipH() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix flipped = m.flipHorizontally();
            assertEquals(2.0, flipped.get(0, 0));
            assertEquals(1.0, flipped.get(0, 1));
            assertEquals(1.0, m.get(0, 0));
        }

        @Test
        public void testFlipV() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix flipped = m.flipVertically();
            assertEquals(3.0, flipped.get(0, 0));
            assertEquals(4.0, flipped.get(0, 1));
            assertEquals(1.0, m.get(0, 0));
        }

        // ============ Rotation Tests ============

        @Test
        public void testRotate90() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3.0, rotated.get(0, 0));
            assertEquals(1.0, rotated.get(0, 1));
            assertEquals(4.0, rotated.get(1, 0));
            assertEquals(2.0, rotated.get(1, 1));
        }

        @Test
        public void testRotate180() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate180();
            assertEquals(4.0, rotated.get(0, 0));
            assertEquals(3.0, rotated.get(0, 1));
            assertEquals(2.0, rotated.get(1, 0));
            assertEquals(1.0, rotated.get(1, 1));
        }

        @Test
        public void testRotate270() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate270();
            assertEquals(2.0, rotated.get(0, 0));
            assertEquals(4.0, rotated.get(0, 1));
            assertEquals(1.0, rotated.get(1, 0));
            assertEquals(3.0, rotated.get(1, 1));
        }

        // ============ Transpose Tests ============

        @Test
        public void testTranspose() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1.0, transposed.get(0, 0));
            assertEquals(4.0, transposed.get(0, 1));
            assertEquals(3.0, transposed.get(2, 0));
            assertEquals(6.0, transposed.get(2, 1));
        }

        @Test
        public void testTranspose_square() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix transposed = m.transpose();
            assertEquals(1.0, transposed.get(0, 0));
            assertEquals(3.0, transposed.get(0, 1));
            assertEquals(2.0, transposed.get(1, 0));
            assertEquals(4.0, transposed.get(1, 1));
        }

        // ============ Reshape Tests ============

        @Test
        public void testReshape() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 } });
            DoubleMatrix reshaped = m.reshape(2, 3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals(1.0, reshaped.get(0, 0));
            assertEquals(4.0, reshaped.get(1, 0));
        }

        // ============ Repelem Tests ============

        @Test
        public void testRepelem() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m.repeatElements(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1.0, result.get(0, 0));
            assertEquals(1.0, result.get(0, 1));
            assertEquals(1.0, result.get(1, 0));
            assertEquals(1.0, result.get(1, 1));
            assertEquals(4.0, result.get(2, 2));
            assertEquals(4.0, result.get(3, 3));
        }

        // ============ Repmat Tests ============

        @Test
        public void testRepmat() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m.repeatMatrix(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1.0, result.get(0, 0));
            assertEquals(2.0, result.get(0, 1));
            assertEquals(1.0, result.get(0, 2));
            assertEquals(2.0, result.get(0, 3));
            assertEquals(1.0, result.get(2, 0));
        }

        // ============ Flatten Tests ============

        @Test
        public void testFlatten() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleList flat = m.flatten();
            assertEquals(4, flat.size());
            assertEquals(1.0, flat.get(0));
            assertEquals(2.0, flat.get(1));
            assertEquals(3.0, flat.get(2));
            assertEquals(4.0, flat.get(3));
        }

        @Test
        public void testFlatOp() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            final double[] sum = { 0.0 };
            m.applyOnFlattened(row -> {
                for (double val : row) {
                    sum[0] += val;
                }
            });
            assertEquals(10.0, sum[0]);
        }

        // ============ Stack Tests ============

        @Test
        public void testVstack() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix stacked = m1.stackVertically(m2);
            assertEquals(4, stacked.rowCount());
            assertEquals(2, stacked.columnCount());
            assertEquals(1.0, stacked.get(0, 0));
            assertEquals(5.0, stacked.get(2, 0));
        }

        @Test
        public void testVstack_incompatibleColumns() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void testHstack() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix stacked = m1.stackHorizontally(m2);
            assertEquals(2, stacked.rowCount());
            assertEquals(4, stacked.columnCount());
            assertEquals(1.0, stacked.get(0, 0));
            assertEquals(5.0, stacked.get(0, 2));
        }

        @Test
        public void testHstack_incompatibleRows() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Tests ============

        @Test
        public void testAdd() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix result = m1.add(m2);
            assertEquals(6.0, result.get(0, 0));
            assertEquals(8.0, result.get(0, 1));
            assertEquals(10.0, result.get(1, 0));
            assertEquals(12.0, result.get(1, 1));
        }

        @Test
        public void testAdd_incompatibleSize() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void testSubtract() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m1.subtract(m2);
            assertEquals(4.0, result.get(0, 0));
            assertEquals(4.0, result.get(0, 1));
            assertEquals(4.0, result.get(1, 0));
            assertEquals(4.0, result.get(1, 1));
        }

        @Test
        public void testMultiply() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 2.0, 0.0 }, { 1.0, 2.0 } });
            DoubleMatrix result = m1.multiply(m2);
            assertEquals(4.0, result.get(0, 0));
            assertEquals(4.0, result.get(0, 1));
            assertEquals(10.0, result.get(1, 0));
            assertEquals(8.0, result.get(1, 1));
        }

        @Test
        public void testMultiply_incompatibleSize() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        // ============ Conversion Tests ============

        @Test
        public void testBoxed() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Matrix<Double> boxed = m.boxed();
            assertEquals(Double.valueOf(1.0), boxed.get(0, 0));
            assertEquals(Double.valueOf(4.0), boxed.get(1, 1));
        }

        // ============ ZipWith Tests ============

        @Test
        public void testZipWith_binary() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix result = m1.zipWith(m2, (a, b) -> a + b);
            assertEquals(6.0, result.get(0, 0));
            assertEquals(12.0, result.get(1, 1));
        }

        @Test
        public void testZipWith_ternary() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 } });
            DoubleMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
            assertEquals(7.0, result.get(0, 0));
            assertEquals(13.0, result.get(1, 1));
        }

        // ============ Stream Tests ============

        @Test
        public void testStreamLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleStream stream = m.streamMainDiagonal();
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, result);
        }

        @Test
        public void testStreamRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleStream stream = m.streamAntiDiagonal();
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, result);
        }

        @Test
        public void testStreamH() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleStream stream = m.streamHorizontal();
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, result);
        }

        @Test
        public void testStreamH_withRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleStream stream = m.streamHorizontal(1);
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 3.0, 4.0 }, result);
        }

        @Test
        public void testStreamH_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            DoubleStream stream = m.streamHorizontal(1, 3);
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 3.0, 4.0, 5.0, 6.0 }, result);
        }

        @Test
        public void testStreamV() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleStream stream = m.streamVertical();
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 1.0, 3.0, 2.0, 4.0 }, result);
        }

        @Test
        public void testStreamV_withColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleStream stream = m.streamVertical(0);
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 1.0, 3.0 }, result);
        }

        @Test
        public void testStreamV_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleStream stream = m.streamVertical(1, 3);
            double[] result = stream.toArray();
            assertArrayEquals(new double[] { 2.0, 5.0, 3.0, 6.0 }, result);
        }

        @Test
        public void testStreamR() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<DoubleStream> stream = m.streamRows();
            assertEquals(2, stream.count());
        }

        @Test
        public void testStreamR_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            Stream<DoubleStream> stream = m.streamRows(1, 3);
            assertEquals(2, stream.count());
        }

        @Test
        public void testStreamC() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<DoubleStream> stream = m.streamColumns();
            assertEquals(2, stream.count());
        }

        @Test
        public void testStreamC_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            Stream<DoubleStream> stream = m.streamColumns(1, 3);
            assertEquals(2, stream.count());
        }

        // ============ ForEach Tests ============

        @Test
        public void testForEach() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            final double[] sum = { 0.0 };
            m.forEach(val -> sum[0] += val);
            assertEquals(10.0, sum[0]);
        }

        @Test
        public void testForEach_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            final double[] sum = { 0.0 };
            m.forEach(1, 3, 1, 3, val -> sum[0] += val);
            assertEquals(28.0, sum[0]);
        }

        // ============ Inherited Methods Tests ============

        @Test
        public void testIsEmpty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertTrue(empty.isEmpty());
            DoubleMatrix notEmpty = DoubleMatrix.of(new double[][] { { 1.0 } });
            assertFalse(notEmpty.isEmpty());
        }

        @Test
        public void testIsSameShape() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
            assertTrue(m1.isSameShape(m2));
            assertFalse(m1.isSameShape(m3));
        }

        @Test
        public void testArray() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            double[][] array = m.backingArray();
            assertArrayEquals(new double[] { 1.0, 2.0 }, array[0]);
            assertArrayEquals(new double[] { 3.0, 4.0 }, array[1]);
        }

        @Test
        public void testReshape_singleParam() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 } });
            DoubleMatrix reshaped = m.reshape(3);
            assertEquals(2, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
        }

        @Test
        public void testPointsLU2RD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsMainDiagonal();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsRU2LD() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsAntiDiagonal();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsH() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsHorizontal();
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsH_withRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsHorizontal(1);
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsH_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            Stream<Point> points = m.pointsHorizontal(1, 3);
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsV() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsVertical();
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsV_withColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Point> points = m.pointsVertical(1);
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsV_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            Stream<Point> points = m.pointsVertical(1, 3);
            assertEquals(4, points.count());
        }

        @Test
        public void testPointsR() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Stream<Point>> points = m.pointsRows();
            assertEquals(2, points.count());
        }

        @Test
        public void testPointsC() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Stream<Stream<Point>> points = m.pointsColumns();
            assertEquals(2, points.count());
        }

        @Test
        public void testForEach_biConsumer() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            final int[] count = { 0 };
            m.forEachIndex((i, j) -> count[0]++);
            assertEquals(4, count[0]);
        }

        @Test
        public void testForEach_biObjConsumer() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            final int[] count = { 0 };
            m.forEachIndex((i, j, matrix) -> count[0]++);
            assertEquals(4, count[0]);
        }

        @Test
        public void testAccept() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            final boolean[] called = { false };
            m.accept(matrix -> called[0] = true);
            assertTrue(called[0]);
        }

        @Test
        public void testApply() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            int result = m.apply(matrix -> matrix.rowCount() * matrix.columnCount());
            assertEquals(4, result);
        }

        // ============ Equality Tests ============

        @Test
        public void testHashCode() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void testEquals() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 5.0 } });
            assertTrue(m1.equals(m2));
            assertFalse(m1.equals(m3));
            assertTrue(m1.equals(m1));
            assertFalse(m1.equals(null));
            assertFalse(m1.equals("not a matrix"));
        }

        @Test
        public void testToString() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.contains("1.0"));
            assertTrue(str.contains("4.0"));
        }

        @Test
        public void testPrintln() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            String result = m.println();
            assertNotNull(result);
        }

        @Test
        public void testPrintln_empty() {
            DoubleMatrix m = DoubleMatrix.empty();
            String result = m.println();
            assertNotNull(result);
        }
    }

    @Nested
    @Tag("2512")
    class DoubleMatrix2512Test extends TestBase {

        // ============ Constructor Tests ============

        @Test
        public void test_constructor_withValidArray() {
            double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            DoubleMatrix m = new DoubleMatrix(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0), 0.0);
            assertEquals(4.0, m.get(1, 1), 0.0);
        }

        @Test
        public void test_constructor_withNullArray() {
            DoubleMatrix m = new DoubleMatrix(null);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_constructor_withEmptyArray() {
            DoubleMatrix m = new DoubleMatrix(new double[0][0]);
            assertEquals(0, m.rowCount());
            assertEquals(0, m.columnCount());
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_constructor_withSingleElement() {
            DoubleMatrix m = new DoubleMatrix(new double[][] { { 42.5 } });
            assertEquals(1, m.rowCount());
            assertEquals(1, m.columnCount());
            assertEquals(42.5, m.get(0, 0), 0.0);
        }

        // ============ Factory Method Tests ============

        @Test
        public void test_empty() {
            DoubleMatrix empty = DoubleMatrix.empty();
            assertEquals(0, empty.rowCount());
            assertEquals(0, empty.columnCount());
            assertTrue(empty.isEmpty());
            assertSame(DoubleMatrix.empty(), DoubleMatrix.empty());
        }

        @Test
        public void test_of_withValidArray() {
            double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
            DoubleMatrix m = DoubleMatrix.of(arr);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0), 0.0);
        }

        @Test
        public void test_of_withNullArray() {
            DoubleMatrix m = DoubleMatrix.of((double[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_of_withEmptyArray() {
            DoubleMatrix m = DoubleMatrix.of(new double[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_of_withSingleRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0, 4.0, 5.0 } });
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        // ============ Create Method Tests ============

        @Test
        public void test_create_fromIntArray() {
            int[][] ints = { { 1, 2 }, { 3, 4 } };
            DoubleMatrix m = DoubleMatrix.from(ints);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0), 0.0);
            assertEquals(4.0, m.get(1, 1), 0.0);
        }

        @Test
        public void test_create_fromIntArray_empty() {
            DoubleMatrix m = DoubleMatrix.from(new int[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_create_fromIntArray_null() {
            DoubleMatrix m = DoubleMatrix.from((int[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_create_fromIntArray_nullFirstRow() {
            int[][] ints = { null, { 1, 2 } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(ints));
        }

        @Test
        public void test_create_fromIntArray_differentRowLengths() {
            int[][] ints = { { 1, 2 }, { 3 } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(ints));
        }

        @Test
        public void test_create_fromLongArray() {
            long[][] longs = { { 1L, 2L }, { 3L, 4L } };
            DoubleMatrix m = DoubleMatrix.from(longs);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0), 0.0);
            assertEquals(4.0, m.get(1, 1), 0.0);
        }

        @Test
        public void test_create_fromLongArray_empty() {
            DoubleMatrix m = DoubleMatrix.from(new long[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_create_fromLongArray_null() {
            DoubleMatrix m = DoubleMatrix.from((long[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_create_fromLongArray_nullFirstRow() {
            long[][] longs = { null, { 1L, 2L } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(longs));
        }

        @Test
        public void test_create_fromFloatArray() {
            float[][] floats = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
            DoubleMatrix m = DoubleMatrix.from(floats);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0), 0.0);
            assertEquals(4.0, m.get(1, 1), 0.0);
        }

        @Test
        public void test_create_fromFloatArray_empty() {
            DoubleMatrix m = DoubleMatrix.from(new float[0][0]);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_create_fromFloatArray_null() {
            DoubleMatrix m = DoubleMatrix.from((float[][]) null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_create_fromFloatArray_nullFirstRow() {
            float[][] floats = { null, { 1.0f, 2.0f } };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.from(floats));
        }

        // ============ Random and Repeat Tests ============

        @Test
        public void test_random() {
            DoubleMatrix m = DoubleMatrix.random(5);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
        }

        @Test
        public void test_random_zeroLength() {
            DoubleMatrix m = DoubleMatrix.random(0);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        @Test
        public void test_repeat() {
            DoubleMatrix m = DoubleMatrix.repeat(1, 5, 3.14);
            assertEquals(1, m.rowCount());
            assertEquals(5, m.columnCount());
            for (int i = 0; i < 5; i++) {
                assertEquals(3.14, m.get(0, i), 0.0);
            }
        }

        @Test
        public void test_repeat_zeroLength() {
            DoubleMatrix m = DoubleMatrix.repeat(1, 0, 3.14);
            assertEquals(1, m.rowCount());
            assertEquals(0, m.columnCount());
        }

        // ============ Diagonal Tests ============

        @Test
        public void test_mainDiagonal() {
            double[] diag = { 1.0, 2.0, 3.0 };
            DoubleMatrix m = DoubleMatrix.mainDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 0), 0.0);
            assertEquals(2.0, m.get(1, 1), 0.0);
            assertEquals(3.0, m.get(2, 2), 0.0);
            assertEquals(0.0, m.get(0, 1), 0.0);
            assertEquals(0.0, m.get(1, 0), 0.0);
        }

        @Test
        public void test_mainDiagonal_null() {
            DoubleMatrix m = DoubleMatrix.mainDiagonal(null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_antiDiagonal() {
            double[] diag = { 1.0, 2.0, 3.0 };
            DoubleMatrix m = DoubleMatrix.antiDiagonal(diag);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 2), 0.0);
            assertEquals(2.0, m.get(1, 1), 0.0);
            assertEquals(3.0, m.get(2, 0), 0.0);
            assertEquals(0.0, m.get(0, 0), 0.0);
        }

        @Test
        public void test_antiDiagonal_null() {
            DoubleMatrix m = DoubleMatrix.antiDiagonal(null);
            assertTrue(m.isEmpty());
        }

        @Test
        public void test_diagonal_both() {
            double[] lu2rd = { 1.0, 2.0, 3.0 };
            double[] ru2ld = { 4.0, 5.0, 6.0 };
            DoubleMatrix m = DoubleMatrix.diagonals(lu2rd, ru2ld);
            assertEquals(3, m.rowCount());
            assertEquals(3, m.columnCount());
            assertEquals(1.0, m.get(0, 0), 0.0);
            assertEquals(2.0, m.get(1, 1), 0.0);
            assertEquals(3.0, m.get(2, 2), 0.0);
            assertEquals(4.0, m.get(0, 2), 0.0);
            assertEquals(6.0, m.get(2, 0), 0.0);
        }

        @Test
        public void test_diagonal_differentLengths() {
            double[] lu2rd = { 1.0, 2.0 };
            double[] ru2ld = { 4.0, 5.0, 6.0 };
            assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.diagonals(lu2rd, ru2ld));
        }

        @Test
        public void test_diagonal_bothNull() {
            DoubleMatrix m = DoubleMatrix.diagonals(null, null);
            assertTrue(m.isEmpty());
        }

        // ============ Unbox Test ============

        @Test
        public void test_unbox() {
            Matrix<Double> boxed = Matrix.of(new Double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m = DoubleMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0), 0.0);
            assertEquals(4.0, m.get(1, 1), 0.0);
        }

        @Test
        public void test_unbox_withNulls() {
            Matrix<Double> boxed = Matrix.of(new Double[][] { { 1.0, null }, { null, 4.0 } });
            DoubleMatrix m = DoubleMatrix.unbox(boxed);
            assertEquals(2, m.rowCount());
            assertEquals(2, m.columnCount());
            assertEquals(1.0, m.get(0, 0), 0.0);
            assertEquals(0.0, m.get(0, 1), 0.0);
            assertEquals(0.0, m.get(1, 0), 0.0);
            assertEquals(4.0, m.get(1, 1), 0.0);
        }

        // ============ Component Type Test ============

        @Test
        public void test_componentType() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertEquals(double.class, m.componentType());
        }

        // ============ Get and Set Tests ============

        @Test
        public void test_get_byIndices() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertEquals(1.0, m.get(0, 0), 0.0);
            assertEquals(2.0, m.get(0, 1), 0.0);
            assertEquals(3.0, m.get(1, 0), 0.0);
            assertEquals(4.0, m.get(1, 1), 0.0);
        }

        @Test
        public void test_get_byPoint() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Point p = Point.of(0, 1);
            assertEquals(2.0, m.get(p), 0.0);
        }

        @Test
        public void test_set_byIndices() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.set(0, 1, 9.0);
            assertEquals(9.0, m.get(0, 1), 0.0);
        }

        @Test
        public void test_set_byPoint() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Point p = Point.of(1, 1);
            m.set(p, 9.0);
            assertEquals(9.0, m.get(p), 0.0);
        }

        // ============ Directional Access Tests ============

        @Test
        public void test_upOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble up = m.above(1, 0);
            assertTrue(up.isPresent());
            assertEquals(1.0, up.get(), 0.0);
        }

        @Test
        public void test_upOf_firstRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble up = m.above(0, 0);
            assertFalse(up.isPresent());
        }

        @Test
        public void test_downOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble down = m.below(0, 0);
            assertTrue(down.isPresent());
            assertEquals(3.0, down.get(), 0.0);
        }

        @Test
        public void test_downOf_lastRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble down = m.below(1, 0);
            assertFalse(down.isPresent());
        }

        @Test
        public void test_leftOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble left = m.left(0, 1);
            assertTrue(left.isPresent());
            assertEquals(1.0, left.get(), 0.0);
        }

        @Test
        public void test_leftOf_firstColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble left = m.left(0, 0);
            assertFalse(left.isPresent());
        }

        @Test
        public void test_rightOf() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble right = m.right(0, 0);
            assertTrue(right.isPresent());
            assertEquals(2.0, right.get(), 0.0);
        }

        @Test
        public void test_rightOf_lastColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            OptionalDouble right = m.right(0, 1);
            assertFalse(right.isPresent());
        }

        // ============ Row and Column Access Tests ============

        @Test
        public void test_row() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            double[] row = m.rowView(0);
            assertArrayEquals(new double[] { 1.0, 2.0 }, row, 0.0);
        }

        @Test
        public void test_row_invalidIndex() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.rowView(5));
        }

        @Test
        public void test_column() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            double[] col = m.columnCopy(0);
            assertArrayEquals(new double[] { 1.0, 3.0 }, col, 0.0);
        }

        @Test
        public void test_column_invalidIndex() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.columnCopy(5));
        }

        @Test
        public void test_setRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setRow(0, new double[] { 9.0, 8.0 });
            assertArrayEquals(new double[] { 9.0, 8.0 }, m.rowView(0), 0.0);
        }

        @Test
        public void test_setRow_invalidRowIndex() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(5, new double[] { 1.0, 2.0 }));
        }

        @Test
        public void test_setRow_invalidLength() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new double[] { 1.0 }));
        }

        @Test
        public void test_setColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setColumn(0, new double[] { 9.0, 8.0 });
            assertArrayEquals(new double[] { 9.0, 8.0 }, m.columnCopy(0), 0.0);
        }

        @Test
        public void test_setColumn_invalidColumnIndex() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(5, new double[] { 1.0 }));
        }

        @Test
        public void test_setColumn_invalidLength() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new double[] { 1.0 }));
        }

        // ============ Update Row and Column Tests ============

        @Test
        public void test_updateRow() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateRow(0, x -> x * 2);
            assertArrayEquals(new double[] { 2.0, 4.0 }, m.rowView(0), 0.0);
            assertArrayEquals(new double[] { 3.0, 4.0 }, m.rowView(1), 0.0);
        }

        @Test
        public void test_updateColumn() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateColumn(0, x -> x * 2);
            assertArrayEquals(new double[] { 2.0, 6.0 }, m.columnCopy(0), 0.0);
            assertArrayEquals(new double[] { 2.0, 4.0 }, m.columnCopy(1), 0.0);
        }

        // ============ Diagonal Get/Set Tests ============

        @Test
        public void test_getMainDiagonal() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            double[] diag = m.getMainDiagonal();
            assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, diag, 0.0);
        }

        @Test
        public void test_getMainDiagonal_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            assertThrows(IllegalStateException.class, () -> m.getMainDiagonal());
        }

        @Test
        public void test_setMainDiagonal() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setMainDiagonal(new double[] { 9.0, 8.0 });
            assertArrayEquals(new double[] { 9.0, 8.0 }, m.getMainDiagonal(), 0.0);
        }

        @Test
        public void test_setMainDiagonal_nonSquare() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalStateException.class, () -> m.setMainDiagonal(new double[] { 9.0 }));
        }

        @Test
        public void test_setMainDiagonal_invalidLength() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.setMainDiagonal(new double[] { 9.0 }));
        }

        @Test
        public void test_updateMainDiagonal() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateMainDiagonal(x -> x * 2);
            assertArrayEquals(new double[] { 2.0, 8.0 }, m.getMainDiagonal(), 0.0);
        }

        @Test
        public void test_getAntiDiagonal() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            double[] diag = m.getAntiDiagonal();
            assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, diag, 0.0);
        }

        @Test
        public void test_setAntiDiagonal() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.setAntiDiagonal(new double[] { 9.0, 8.0 });
            assertArrayEquals(new double[] { 9.0, 8.0 }, m.getAntiDiagonal(), 0.0);
        }

        @Test
        public void test_updateAntiDiagonal() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateAntiDiagonal(x -> x * 2);
            double[] diag = m.getAntiDiagonal();
            assertEquals(4.0, diag[0], 0.0);
            assertEquals(6.0, diag[1], 0.0);
        }

        // ============ Update All Tests ============

        @Test
        public void test_updateAll_unaryOperator() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateAll(x -> x * 2);
            assertEquals(2.0, m.get(0, 0), 0.0);
            assertEquals(4.0, m.get(0, 1), 0.0);
            assertEquals(6.0, m.get(1, 0), 0.0);
            assertEquals(8.0, m.get(1, 1), 0.0);
        }

        @Test
        public void test_updateAll_biFunction() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.updateAll((i, j) -> (i + 1) * 10.0 + (j + 1));
            assertEquals(11.0, m.get(0, 0), 0.0);
            assertEquals(12.0, m.get(0, 1), 0.0);
            assertEquals(21.0, m.get(1, 0), 0.0);
            assertEquals(22.0, m.get(1, 1), 0.0);
        }

        // ============ Replace If Tests ============

        @Test
        public void test_replaceIf_predicate() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.replaceIf(x -> x > 2, 99.0);
            assertEquals(1.0, m.get(0, 0), 0.0);
            assertEquals(2.0, m.get(0, 1), 0.0);
            assertEquals(99.0, m.get(1, 0), 0.0);
            assertEquals(99.0, m.get(1, 1), 0.0);
        }

        @Test
        public void test_replaceIf_biPredicate() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.replaceIf((i, j) -> i == j, 99.0);
            assertEquals(99.0, m.get(0, 0), 0.0);
            assertEquals(2.0, m.get(0, 1), 0.0);
            assertEquals(3.0, m.get(1, 0), 0.0);
            assertEquals(99.0, m.get(1, 1), 0.0);
        }

        // ============ Map Tests ============

        @Test
        public void test_map() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m.map(x -> x * 2);
            assertEquals(2.0, result.get(0, 0), 0.0);
            assertEquals(4.0, result.get(0, 1), 0.0);
            assertEquals(6.0, result.get(1, 0), 0.0);
            assertEquals(8.0, result.get(1, 1), 0.0);
            // Original should be unchanged
            assertEquals(1.0, m.get(0, 0), 0.0);
        }

        @Test
        public void test_mapToInt() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.7 }, { 3.2, 4.9 } });
            IntMatrix result = m.mapToInt(x -> (int) x);
            assertEquals(1, result.get(0, 0));
            assertEquals(2, result.get(0, 1));
            assertEquals(3, result.get(1, 0));
            assertEquals(4, result.get(1, 1));
        }

        @Test
        public void test_mapToLong() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.7 }, { 3.2, 4.9 } });
            LongMatrix result = m.mapToLong(x -> (long) x);
            assertEquals(1L, result.get(0, 0));
            assertEquals(2L, result.get(0, 1));
            assertEquals(3L, result.get(1, 0));
            assertEquals(4L, result.get(1, 1));
        }

        @Test
        public void test_mapToObj() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Matrix<String> result = m.mapToObj(x -> String.valueOf(x), String.class);
            assertEquals("1.0", result.get(0, 0));
            assertEquals("2.0", result.get(0, 1));
            assertEquals("3.0", result.get(1, 0));
            assertEquals("4.0", result.get(1, 1));
        }

        // ============ Fill Tests ============

        @Test
        public void test_fill_value() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.fill(9.0);
            assertEquals(9.0, m.get(0, 0), 0.0);
            assertEquals(9.0, m.get(0, 1), 0.0);
            assertEquals(9.0, m.get(1, 0), 0.0);
            assertEquals(9.0, m.get(1, 1), 0.0);
        }

        @Test
        public void test_fill_array() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            m.copyFrom(new double[][] { { 9.0, 8.0 }, { 7.0, 6.0 } });
            assertEquals(9.0, m.get(0, 0), 0.0);
            assertEquals(8.0, m.get(0, 1), 0.0);
            assertEquals(7.0, m.get(1, 0), 0.0);
            assertEquals(6.0, m.get(1, 1), 0.0);
        }

        @Test
        public void test_fill_withOffset() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            m.copyFrom(1, 1, new double[][] { { 99.0 } });
            assertEquals(1.0, m.get(0, 0), 0.0);
            assertEquals(99.0, m.get(1, 1), 0.0);
            assertEquals(9.0, m.get(2, 2), 0.0);
        }

        @Test
        public void test_fill_withOffset_clipsToFit() {
            // fill method clips data to fit within matrix bounds, doesn't throw exception
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            m.copyFrom(0, 0, new double[][] { { 9.0, 8.0, 7.0 } }); // Source has 3 elements but matrix only has 2 columns
            assertEquals(9.0, m.get(0, 0), 0.0);
            assertEquals(8.0, m.get(0, 1), 0.0); // Only first 2 elements are copied
        }

        // ============ Copy Tests ============

        @Test
        public void test_copy() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix copy = m.copy();
            assertEquals(m.rowCount(), copy.rowCount());
            assertEquals(m.columnCount(), copy.columnCount());
            assertEquals(1.0, copy.get(0, 0), 0.0);
            copy.set(0, 0, 99.0);
            assertEquals(1.0, m.get(0, 0), 0.0); // Original unchanged
        }

        @Test
        public void test_copy_withRowRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            DoubleMatrix copy = m.copy(1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(3.0, copy.get(0, 0), 0.0);
            assertEquals(6.0, copy.get(1, 1), 0.0);
        }

        @Test
        public void test_copy_withFullRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            DoubleMatrix copy = m.copy(1, 3, 1, 3);
            assertEquals(2, copy.rowCount());
            assertEquals(2, copy.columnCount());
            assertEquals(5.0, copy.get(0, 0), 0.0);
            assertEquals(9.0, copy.get(1, 1), 0.0);
        }

        @Test
        public void test_copy_invalidRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 5));
        }

        // ============ Extend Tests ============

        @Test
        public void test_extend() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix extended = m.resize(3, 3);
            assertEquals(3, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1.0, extended.get(0, 0), 0.0);
            assertEquals(0.0, extended.get(2, 2), 0.0);
        }

        @Test
        public void test_extend_withDefaultValue() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            DoubleMatrix extended = m.resize(2, 3, 99.0);
            assertEquals(2, extended.rowCount());
            assertEquals(3, extended.columnCount());
            assertEquals(1.0, extended.get(0, 0), 0.0);
            assertEquals(99.0, extended.get(1, 1), 0.0);
            assertEquals(99.0, extended.get(0, 2), 0.0);
        }

        @Test
        public void test_extend_smaller() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleMatrix result = m.resize(1, 2);
            assertEquals(1, result.rowCount());
            assertEquals(2, result.columnCount());
        }

        @Test
        public void test_extend_directional() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix extended = m.extend(1, 1, 1, 1);
            assertEquals(4, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1.0, extended.get(1, 1), 0.0);
            assertEquals(0.0, extended.get(0, 0), 0.0);
        }

        @Test
        public void test_extend_directional_withDefaultValue() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            DoubleMatrix extended = m.extend(1, 1, 1, 1, 99.0);
            assertEquals(3, extended.rowCount());
            assertEquals(4, extended.columnCount());
            assertEquals(1.0, extended.get(1, 1), 0.0);
            assertEquals(99.0, extended.get(0, 0), 0.0);
        }

        // ============ Reverse and Flip Tests ============

        @Test
        public void test_flipInPlaceHorizontally() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            m.flipInPlaceHorizontally();
            assertEquals(3.0, m.get(0, 0), 0.0);
            assertEquals(2.0, m.get(0, 1), 0.0);
            assertEquals(1.0, m.get(0, 2), 0.0);
        }

        @Test
        public void test_flipInPlaceVertically() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            m.flipInPlaceVertically();
            assertEquals(5.0, m.get(0, 0), 0.0);
            assertEquals(3.0, m.get(1, 0), 0.0);
            assertEquals(1.0, m.get(2, 0), 0.0);
        }

        @Test
        public void test_flipHorizontally() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleMatrix flipped = m.flipHorizontally();
            assertEquals(3.0, flipped.get(0, 0), 0.0);
            assertEquals(2.0, flipped.get(0, 1), 0.0);
            assertEquals(1.0, flipped.get(0, 2), 0.0);
            // Original unchanged
            assertEquals(1.0, m.get(0, 0), 0.0);
        }

        @Test
        public void test_flipVertically() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            DoubleMatrix flipped = m.flipVertically();
            assertEquals(5.0, flipped.get(0, 0), 0.0);
            assertEquals(3.0, flipped.get(1, 0), 0.0);
            assertEquals(1.0, flipped.get(2, 0), 0.0);
            // Original unchanged
            assertEquals(1.0, m.get(0, 0), 0.0);
        }

        // ============ Rotate Tests ============

        @Test
        public void test_rotate90() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate90();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(3.0, rotated.get(0, 0), 0.0);
            assertEquals(1.0, rotated.get(0, 1), 0.0);
            assertEquals(4.0, rotated.get(1, 0), 0.0);
            assertEquals(2.0, rotated.get(1, 1), 0.0);
        }

        @Test
        public void test_rotate180() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate180();
            assertEquals(4.0, rotated.get(0, 0), 0.0);
            assertEquals(3.0, rotated.get(0, 1), 0.0);
            assertEquals(2.0, rotated.get(1, 0), 0.0);
            assertEquals(1.0, rotated.get(1, 1), 0.0);
        }

        @Test
        public void test_rotate270() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix rotated = m.rotate270();
            assertEquals(2, rotated.rowCount());
            assertEquals(2, rotated.columnCount());
            assertEquals(2.0, rotated.get(0, 0), 0.0);
            assertEquals(4.0, rotated.get(0, 1), 0.0);
            assertEquals(1.0, rotated.get(1, 0), 0.0);
            assertEquals(3.0, rotated.get(1, 1), 0.0);
        }

        // ============ Transpose Test ============

        @Test
        public void test_transpose() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleMatrix transposed = m.transpose();
            assertEquals(3, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1.0, transposed.get(0, 0), 0.0);
            assertEquals(4.0, transposed.get(0, 1), 0.0);
            assertEquals(2.0, transposed.get(1, 0), 0.0);
            assertEquals(5.0, transposed.get(1, 1), 0.0);
        }

        @Test
        public void test_transpose_square() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix transposed = m.transpose();
            assertEquals(2, transposed.rowCount());
            assertEquals(2, transposed.columnCount());
            assertEquals(1.0, transposed.get(0, 0), 0.0);
            assertEquals(3.0, transposed.get(0, 1), 0.0);
        }

        // ============ Reshape Test ============

        @Test
        public void test_reshape() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            DoubleMatrix reshaped = m.reshape(3, 2);
            assertEquals(3, reshaped.rowCount());
            assertEquals(2, reshaped.columnCount());
            assertEquals(1.0, reshaped.get(0, 0), 0.0);
            assertEquals(2.0, reshaped.get(0, 1), 0.0);
            assertEquals(3.0, reshaped.get(1, 0), 0.0);
            assertEquals(4.0, reshaped.get(1, 1), 0.0);
        }

        @Test
        public void test_reshape_largerSize() {
            // reshape allows different total element count, fills extra positions with zeros
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix reshaped = m.reshape(3, 3); // 4 elements -> 9 positions
            assertEquals(3, reshaped.rowCount());
            assertEquals(3, reshaped.columnCount());
            assertEquals(1.0, reshaped.get(0, 0), 0.0);
            assertEquals(2.0, reshaped.get(0, 1), 0.0);
            assertEquals(3.0, reshaped.get(0, 2), 0.0);
            assertEquals(4.0, reshaped.get(1, 0), 0.0);
            assertEquals(0.0, reshaped.get(1, 1), 0.0); // Extra positions filled with zeros
            assertEquals(0.0, reshaped.get(1, 2), 0.0);
        }

        // ============ Repelem Test ============

        @Test
        public void test_repeatElements() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m.repeatElements(2, 2);
            assertEquals(4, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1.0, result.get(0, 0), 0.0);
            assertEquals(1.0, result.get(0, 1), 0.0);
            assertEquals(1.0, result.get(1, 0), 0.0);
            assertEquals(1.0, result.get(1, 1), 0.0);
            assertEquals(2.0, result.get(0, 2), 0.0);
        }

        @Test
        public void test_repeatElements_invalidRepeats() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatElements(0, 1));
        }

        // ============ Repmat Test ============

        @Test
        public void test_repeatMatrix() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            DoubleMatrix result = m.repeatMatrix(2, 2);
            assertEquals(2, result.rowCount());
            assertEquals(4, result.columnCount());
            assertEquals(1.0, result.get(0, 0), 0.0);
            assertEquals(2.0, result.get(0, 1), 0.0);
            assertEquals(1.0, result.get(0, 2), 0.0);
            assertEquals(2.0, result.get(0, 3), 0.0);
        }

        @Test
        public void test_repeatMatrix_invalidRepeats() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertThrows(IllegalArgumentException.class, () -> m.repeatMatrix(0, 1));
        }

        // ============ Flatten Test ============

        @Test
        public void test_flatten() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleList flat = m.flatten();
            assertEquals(4, flat.size());
            assertEquals(1.0, flat.get(0), 0.0);
            assertEquals(2.0, flat.get(1), 0.0);
            assertEquals(3.0, flat.get(2), 0.0);
            assertEquals(4.0, flat.get(3), 0.0);
        }

        // ============ FlatOp Test ============

        @Test
        public void test_applyOnFlattened() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            AtomicInteger count = new AtomicInteger(0);
            m.applyOnFlattened(row -> count.addAndGet(row.length));
            assertEquals(4, count.get());
        }

        // ============ Vstack and Hstack Tests ============

        @Test
        public void test_stackVertically() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0, 4.0 } });
            DoubleMatrix result = m1.stackVertically(m2);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1.0, result.get(0, 0), 0.0);
            assertEquals(3.0, result.get(1, 0), 0.0);
        }

        @Test
        public void test_vstack_incompatibleCols() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackVertically(m2));
        }

        @Test
        public void test_stackHorizontally() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0 }, { 2.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0 }, { 4.0 } });
            DoubleMatrix result = m1.stackHorizontally(m2);
            assertEquals(2, result.rowCount());
            assertEquals(2, result.columnCount());
            assertEquals(1.0, result.get(0, 0), 0.0);
            assertEquals(3.0, result.get(0, 1), 0.0);
        }

        @Test
        public void test_hstack_incompatibleRows() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0 }, { 4.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.stackHorizontally(m2));
        }

        // ============ Arithmetic Operations Tests ============

        @Test
        public void test_add() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix result = m1.add(m2);
            assertEquals(6.0, result.get(0, 0), 0.0);
            assertEquals(8.0, result.get(0, 1), 0.0);
            assertEquals(10.0, result.get(1, 0), 0.0);
            assertEquals(12.0, result.get(1, 1), 0.0);
        }

        @Test
        public void test_add_incompatibleDimensions() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
        }

        @Test
        public void test_subtract() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix result = m1.subtract(m2);
            assertEquals(4.0, result.get(0, 0), 0.0);
            assertEquals(4.0, result.get(0, 1), 0.0);
            assertEquals(4.0, result.get(1, 0), 0.0);
            assertEquals(4.0, result.get(1, 1), 0.0);
        }

        @Test
        public void test_subtract_incompatibleDimensions() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
        }

        @Test
        public void test_multiply() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 2.0, 0.0 }, { 1.0, 2.0 } });
            DoubleMatrix result = m1.multiply(m2);
            assertEquals(4.0, result.get(0, 0), 0.0);
            assertEquals(4.0, result.get(0, 1), 0.0);
            assertEquals(10.0, result.get(1, 0), 0.0);
            assertEquals(8.0, result.get(1, 1), 0.0);
        }

        @Test
        public void test_multiply_incompatibleDimensions() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0 } });
            assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
        }

        // ============ Boxed Test ============

        @Test
        public void test_boxed() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            Matrix<Double> boxed = m.boxed();
            assertEquals(2, boxed.rowCount());
            assertEquals(2, boxed.columnCount());
            assertEquals(1.0, boxed.get(0, 0), 0.0);
            assertEquals(4.0, boxed.get(1, 1), 0.0);
        }

        // ============ ZipWith Tests ============

        @Test
        public void test_zipWith_binary() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
            DoubleMatrix result = m1.zipWith(m2, (a, b) -> a * b);
            assertEquals(5.0, result.get(0, 0), 0.0);
            assertEquals(12.0, result.get(0, 1), 0.0);
            assertEquals(21.0, result.get(1, 0), 0.0);
            assertEquals(32.0, result.get(1, 1), 0.0);
        }

        @Test
        public void test_zipWith_ternary() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0, 4.0 } });
            DoubleMatrix m3 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 } });
            DoubleMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
            assertEquals(9.0, result.get(0, 0), 0.0);
            assertEquals(12.0, result.get(0, 1), 0.0);
        }

        // ============ Stream Tests ============

        @Test
        public void test_streamHorizontal() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            double sum = m.streamHorizontal().sum();
            assertEquals(10.0, sum, 0.0);
        }

        @Test
        public void test_streamH_byRowIndex() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            double sum = m.streamHorizontal(0).sum();
            assertEquals(3.0, sum, 0.0);
        }

        @Test
        public void test_streamH_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            double sum = m.streamHorizontal(1, 3).sum();
            assertEquals(18.0, sum, 0.0);
        }

        @Test
        public void test_streamVertical() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            double sum = m.streamVertical().sum();
            assertEquals(10.0, sum, 0.0);
        }

        @Test
        public void test_streamV_byColumnIndex() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            double sum = m.streamVertical(0).sum();
            assertEquals(4.0, sum, 0.0);
        }

        @Test
        public void test_streamV_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            double sum = m.streamVertical(1, 3).sum();
            assertEquals(16.0, sum, 0.0);
        }

        @Test
        public void test_streamMainDiagonal() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            double sum = m.streamMainDiagonal().sum();
            assertEquals(5.0, sum, 0.0);
        }

        @Test
        public void test_streamAntiDiagonal() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            double sum = m.streamAntiDiagonal().sum();
            assertEquals(5.0, sum, 0.0);
        }

        @Test
        public void test_streamRows() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            long count = m.streamRows().count();
            assertEquals(2, count);
        }

        @Test
        public void test_streamR_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
            long count = m.streamRows(1, 3).count();
            assertEquals(2, count);
        }

        @Test
        public void test_streamColumns() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            long count = m.streamColumns().count();
            assertEquals(2, count);
        }

        @Test
        public void test_streamC_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
            long count = m.streamColumns(1, 3).count();
            assertEquals(2, count);
        }

        // ============ ForEach Tests ============

        @Test
        public void test_forEach() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            AtomicInteger count = new AtomicInteger(0);
            m.forEach(x -> count.incrementAndGet());
            assertEquals(4, count.get());
        }

        @Test
        public void test_forEach_withRange() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
            AtomicInteger count = new AtomicInteger(0);
            m.forEach(1, 3, 1, 3, x -> count.incrementAndGet());
            assertEquals(4, count.get());
        }

        // ============ Utility Tests ============

        @Test
        public void test_println() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            String result = m.println();
            assertNotNull(result);
            assertTrue(result.length() > 0);
        }

        @Test
        public void test_hashCode_consistent() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertEquals(m1.hashCode(), m2.hashCode());
        }

        @Test
        public void test_equals_same() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            assertEquals(m1, m2);
        }

        @Test
        public void test_equals_different() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 5.0 } });
            assertNotEquals(m1, m2);
        }

        @Test
        public void test_equals_null() {
            DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
            assertNotEquals(m1, null);
        }

        @Test
        public void test_toString() {
            DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
            String str = m.toString();
            assertNotNull(str);
            assertTrue(str.length() > 0);
        }
    }

    @Nested
    class JavadocExampleMatrixGroup1Test_DoubleMatrix extends TestBase {
        @Test
        public void testCharMatrix_toDoubleMatrix() {
            CharMatrix charMatrix = CharMatrix.of(new char[][] { { 'a', 'b' } });
            DoubleMatrix doubleMatrix = charMatrix.toDoubleMatrix();
            // Result: [[97.0, 98.0]]
            assertEquals(97.0, doubleMatrix.get(0, 0));
            assertEquals(98.0, doubleMatrix.get(0, 1));
        }

        @Test
        public void testShortMatrix_toDoubleMatrix() {
            ShortMatrix shortMatrix = ShortMatrix.of(new short[][] { { 1, 2 }, { 3, 4 } });
            DoubleMatrix doubleMatrix = shortMatrix.toDoubleMatrix();
            assertEquals(1.0, doubleMatrix.get(0, 0));
            assertEquals(4.0, doubleMatrix.get(1, 1));
        }
    }

    @Nested
    class JavadocExampleMatrixGroup2Test_DoubleMatrix extends TestBase {
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
    }

    @Nested
    class JavadocExampleMatrixTest_DoubleMatrix extends TestBase {
        // ==================== DoubleMatrix Javadoc Examples ====================

        @Test
        public void testDoubleMatrixEmptyRowCount() {
            DoubleMatrix matrix = DoubleMatrix.empty();
            assertEquals(0, matrix.rowCount());
            assertEquals(0, matrix.columnCount());
        }

        @Test
        public void testDoubleMatrixFlipH() {
            // DoubleMatrix.java: DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0, 2.0, 3.0}});
            // DoubleMatrix flipped = matrix.flipHorizontally();   // returns [[3.0, 2.0, 1.0]], original unchanged
            DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
            DoubleMatrix flipped = matrix.flipHorizontally();
            assertEquals(3.0, flipped.get(0, 0));
            assertEquals(2.0, flipped.get(0, 1));
            assertEquals(1.0, flipped.get(0, 2));
            // Verify original unchanged
            assertEquals(1.0, matrix.get(0, 0));
        }

        @Test
        public void testDoubleMatrixFlipV() {
            // DoubleMatrix.java: DoubleMatrix matrix = DoubleMatrix.of(new double[][] {{1.0}, {2.0}, {3.0}});
            // DoubleMatrix flipped = matrix.flipVertically();   // returns [[3.0], [2.0], [1.0]], original unchanged
            DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0 }, { 2.0 }, { 3.0 } });
            DoubleMatrix flipped = matrix.flipVertically();
            assertEquals(3.0, flipped.get(0, 0));
            assertEquals(2.0, flipped.get(1, 0));
            assertEquals(1.0, flipped.get(2, 0));
            // Verify original unchanged
            assertEquals(1.0, matrix.get(0, 0));
        }

        // ==================== DoubleMatrix diagonal examples ====================

        @Test
        public void testDoubleMatrixMainDiagonal() {
            DoubleMatrix matrix = DoubleMatrix.mainDiagonal(new double[] { 1.0, 2.0, 3.0 });
            assertEquals(3, matrix.rowCount());
            assertEquals(3, matrix.columnCount());
            assertEquals(1.0, matrix.get(0, 0));
            assertEquals(2.0, matrix.get(1, 1));
            assertEquals(3.0, matrix.get(2, 2));
            assertEquals(0.0, matrix.get(0, 1));
        }

        // ==================== DoubleMatrix toString example ====================

        @Test
        public void testDoubleMatrixToString() {
            DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
            assertEquals("[[1.0, 2.0, 3.0], [4.0, 5.0, 6.0]]", matrix.toString());
        }
    }

    @Nested
    class MatrixZeroColumnRowStreamTest_DoubleMatrix extends TestBase {
        @Test
        public void testDoubleMatrixRowsForZeroColumnMatrix() {
            final DoubleMatrix matrix = DoubleMatrix.of(new double[][] { {}, {}, {} });
            final List<double[]> rows = matrix.streamRows().map(DoubleStream::toArray).toList();

            assertEquals(3, rows.size());
            assertArrayEquals(new double[0], rows.get(0));
            assertArrayEquals(new double[0], rows.get(1));
            assertArrayEquals(new double[0], rows.get(2));
        }
    }

    @Nested
    class PrimitiveMatrixUpdateAllNullValidationTest_DoubleMatrix extends TestBase {
        @Test
        public void testDoubleMatrixUpdateAllNullOperator() {
            DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1d, 2d }, { 3d, 4d } });
            DoubleMatrix emptyLike = DoubleMatrix.of(new double[][] { {}, {} });
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.DoubleUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAll((Throwables.IntBiFunction<Double, RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateMainDiagonal((Throwables.DoubleUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.updateAntiDiagonal((Throwables.DoubleUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.DoublePredicate<RuntimeException>) null, 0d));
            assertThrows(IllegalArgumentException.class, () -> matrix.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0d));

            assertThrows(IllegalStateException.class, () -> emptyLike.updateMainDiagonal((Throwables.DoubleUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalStateException.class, () -> emptyLike.updateAntiDiagonal((Throwables.DoubleUnaryOperator<RuntimeException>) null));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.DoublePredicate<RuntimeException>) null, 0d));
            assertThrows(IllegalArgumentException.class, () -> emptyLike.replaceIf((Throwables.IntBiPredicate<RuntimeException>) null, 0d));
        }

        @Test
        public void testDoubleMatrixBinaryOpsRejectNullMatrix() {
            DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1D } });
            assertThrows(IllegalArgumentException.class, () -> matrix.stackVertically(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.stackHorizontally(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.add(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.subtract(null));
            assertThrows(IllegalArgumentException.class, () -> matrix.multiply(null));
        }
    }

}
