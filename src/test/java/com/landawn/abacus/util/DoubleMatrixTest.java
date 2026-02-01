package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;

public class DoubleMatrixTest extends TestBase {

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
        DoubleMatrix matrix = DoubleMatrix.repeat(3.14, 5);
        assertEquals(1, matrix.rowCount());
        assertEquals(5, matrix.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(3.14, matrix.get(0, i));
        }
    }

    @Test
    public void testDiagonalLU2RD() {
        double[] diagonal = { 1.0, 2.0, 3.0 };
        DoubleMatrix matrix = DoubleMatrix.diagonalLU2RD(diagonal);
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
        DoubleMatrix matrix = DoubleMatrix.diagonalRU2LD(diagonal);
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
        DoubleMatrix matrix = DoubleMatrix.diagonal(mainDiag, antiDiag);
        assertEquals(3, matrix.rowCount());
        assertEquals(3, matrix.columnCount());
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(7.0, matrix.get(0, 2));

        // Test with different lengths
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.diagonal(new double[] { 1.0 }, new double[] { 2.0, 3.0 }));
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

        assertTrue(matrix.upOf(0, 0).isEmpty());
        assertEquals(1.0, matrix.upOf(1, 0).orElse(0.0));
    }

    @Test
    public void testDownOf() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertEquals(3.0, matrix.downOf(0, 0).orElse(0.0));
        assertTrue(matrix.downOf(1, 0).isEmpty());
    }

    @Test
    public void testLeftOf() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertTrue(matrix.leftOf(0, 0).isEmpty());
        assertEquals(1.0, matrix.leftOf(0, 1).orElse(0.0));
    }

    @Test
    public void testRightOf() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        assertEquals(2.0, matrix.rightOf(0, 0).orElse(0.0));
        assertTrue(matrix.rightOf(0, 1).isEmpty());
    }

    @Test
    public void testRow() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] row = matrix.row(0);
        assertEquals(2, row.length);
        assertEquals(1.0, row[0]);
        assertEquals(2.0, row[1]);

        assertThrows(IllegalArgumentException.class, () -> matrix.row(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.row(2));
    }

    @Test
    public void testColumn() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] col = matrix.column(0);
        assertEquals(2, col.length);
        assertEquals(1.0, col[0]);
        assertEquals(3.0, col[1]);

        assertThrows(IllegalArgumentException.class, () -> matrix.column(-1));
        assertThrows(IllegalArgumentException.class, () -> matrix.column(2));
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
    public void testGetLU2RD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] diag = matrix.getLU2RD();
        assertEquals(2, diag.length);
        assertEquals(1.0, diag[0]);
        assertEquals(4.0, diag[1]);

        // Test non-square matrix
        DoubleMatrix nonSquare = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.setLU2RD(new double[] { 5.0, 6.0 });
        assertEquals(5.0, matrix.get(0, 0));
        assertEquals(6.0, matrix.get(1, 1));

        assertThrows(IllegalArgumentException.class, () -> matrix.setLU2RD(new double[] { 1.0 }));
    }

    @Test
    public void testUpdateLU2RD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.updateLU2RD(d -> d * 2);
        assertEquals(2.0, matrix.get(0, 0));
        assertEquals(8.0, matrix.get(1, 1));
    }

    @Test
    public void testGetRU2LD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] diag = matrix.getRU2LD();
        assertEquals(2, diag.length);
        assertEquals(2.0, diag[0]);
        assertEquals(3.0, diag[1]);
    }

    @Test
    public void testSetRU2LD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.setRU2LD(new double[] { 5.0, 6.0 });
        assertEquals(5.0, matrix.get(0, 1));
        assertEquals(6.0, matrix.get(1, 0));

        assertThrows(IllegalArgumentException.class, () -> matrix.setRU2LD(new double[] { 1.0 }));
    }

    @Test
    public void testUpdateRU2LD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.updateRU2LD(d -> d * 2);
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
        matrix.fill(fillArr);
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
        matrix.fill(0, 1, fillArr);
        assertEquals(7.0, matrix.get(0, 1));
        assertEquals(8.0, matrix.get(0, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.fill(-1, 0, fillArr));
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

        DoubleMatrix extended = matrix.extend(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(0.0, extended.get(2, 2));
    }

    @Test
    public void testExtendWithDefault() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix extended = matrix.extend(3, 3, -1.0);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(-1.0, extended.get(2, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 3, -1.0));
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

        matrix.reverseH();
        assertEquals(2.0, matrix.get(0, 0));
        assertEquals(1.0, matrix.get(0, 1));
    }

    @Test
    public void testReverseV() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        matrix.reverseV();
        assertEquals(3.0, matrix.get(0, 0));
        assertEquals(1.0, matrix.get(1, 0));
    }

    @Test
    public void testFlipH() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix flipped = matrix.flipH();
        assertEquals(2.0, flipped.get(0, 0));
        assertEquals(1.0, flipped.get(0, 1));

        // Original unchanged
        assertEquals(1.0, matrix.get(0, 0));
    }

    @Test
    public void testFlipV() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix flipped = matrix.flipV();
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
    }

    @Test
    public void testRepelem() {
        double[][] arr = { { 1.0, 2.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix repeated = matrix.repelem(2, 3);
        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertEquals(1.0, repeated.get(0, 0));
        assertEquals(1.0, repeated.get(0, 1));
        assertEquals(1.0, repeated.get(0, 2));
        assertEquals(2.0, repeated.get(0, 3));

        assertThrows(IllegalArgumentException.class, () -> matrix.repelem(0, 1));
    }

    @Test
    public void testRepmat() {
        double[][] arr = { { 1.0, 2.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix tiled = matrix.repmat(2, 3);
        assertEquals(2, tiled.rowCount());
        assertEquals(6, tiled.columnCount());
        assertEquals(1.0, tiled.get(0, 0));
        assertEquals(2.0, tiled.get(0, 1));
        assertEquals(1.0, tiled.get(0, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.repmat(0, 1));
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
        matrix.flatOp(array -> count[0] += array.length);
        assertEquals(4, count[0]);
    }

    @Test
    public void testVstack() {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 3.0, 4.0 } });

        DoubleMatrix stacked = a.vstack(b);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1.0, stacked.get(0, 0));
        assertEquals(3.0, stacked.get(1, 0));

        DoubleMatrix c = DoubleMatrix.of(new double[][] { { 5.0 } });
        assertThrows(IllegalArgumentException.class, () -> a.vstack(c));
    }

    @Test
    public void testHstack() {
        DoubleMatrix a = DoubleMatrix.of(new double[][] { { 1.0 }, { 3.0 } });
        DoubleMatrix b = DoubleMatrix.of(new double[][] { { 2.0 }, { 4.0 } });

        DoubleMatrix stacked = a.hstack(b);
        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals(1.0, stacked.get(0, 0));
        assertEquals(2.0, stacked.get(0, 1));

        DoubleMatrix c = DoubleMatrix.of(new double[][] { { 5.0 } });
        assertThrows(IllegalArgumentException.class, () -> a.hstack(c));
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

        double[] elements = matrix.streamH().toArray();
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

        double[] diagonal = matrix.streamLU2RD().toArray();
        assertEquals(2, diagonal.length);
        assertEquals(1.0, diagonal[0]);
        assertEquals(4.0, diagonal[1]);

        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.streamLU2RD().toArray().length);
    }

    @Test
    public void testStreamRU2LD() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] diagonal = matrix.streamRU2LD().toArray();
        assertEquals(2, diagonal.length);
        assertEquals(2.0, diagonal[0]);
        assertEquals(3.0, diagonal[1]);
    }

    @Test
    public void testStreamHWithRow() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] row = matrix.streamH(1).toArray();
        assertEquals(2, row.length);
        assertEquals(3.0, row[0]);
        assertEquals(4.0, row[1]);
    }

    @Test
    public void testStreamHWithRange() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] elements = matrix.streamH(1, 3).toArray();
        assertEquals(4, elements.length);
        assertEquals(3.0, elements[0]);
        assertEquals(4.0, elements[1]);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamH(-1, 2));
    }

    @Test
    public void testStreamV() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] elements = matrix.streamV().toArray();
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

        double[] col = matrix.streamV(1).toArray();
        assertEquals(2, col.length);
        assertEquals(2.0, col[0]);
        assertEquals(4.0, col[1]);
    }

    @Test
    public void testStreamVWithRange() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        double[] elements = matrix.streamV(1, 3).toArray();
        assertEquals(4, elements.length);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamV(-1, 2));
    }

    @Test
    public void testStreamR() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        List<double[]> rows = matrix.streamR().map(stream -> stream.toArray()).toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).length);
        assertEquals(1.0, rows.get(0)[0]);
        assertEquals(2.0, rows.get(0)[1]);
    }

    @Test
    public void testStreamRWithRange() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        List<double[]> rows = matrix.streamR(1, 3).map(stream -> stream.toArray()).toList();
        assertEquals(2, rows.size());

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamR(-1, 2));
    }

    @Test
    public void testStreamC() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        List<double[]> columnCount = matrix.streamC().map(stream -> stream.toArray()).toList();
        assertEquals(2, columnCount.size());
        assertEquals(2, columnCount.get(0).length);
        assertEquals(1.0, columnCount.get(0)[0]);
        assertEquals(3.0, columnCount.get(0)[1]);
    }

    @Test
    public void testStreamCWithRange() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        List<double[]> columnCount = matrix.streamC(1, 3).map(stream -> stream.toArray()).toList();
        assertEquals(2, columnCount.size());

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.streamC(-1, 2));
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
    public void testPrintln() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        // Just ensure it doesn't throw
        matrix.println();
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
        double totalSum = matrix.streamH().sum();
        assertEquals(45.0, totalSum, 0.0001); // 1+2+3+4+5+6+7+8+9 = 45

        // Test sum of specific row
        double row1Sum = matrix.streamH(1).sum();
        assertEquals(15.0, row1Sum, 0.0001); // 4+5+6 = 15

        // Test sum of specific column
        double col0Sum = matrix.streamV(0).sum();
        assertEquals(12.0, col0Sum, 0.0001); // 1+4+7 = 12

        // Test min/max on streams
        double min = matrix.streamH().min().orElse(0.0);
        assertEquals(1.0, min, 0.0001);

        double max = matrix.streamH().max().orElse(0.0);
        assertEquals(9.0, max, 0.0001);

        // Test average
        double avg = matrix.streamH().average().orElse(0.0);
        assertEquals(5.0, avg, 0.0001);

        // Test statistical operations on diagonal
        double diagonalSum = matrix.streamLU2RD().sum();
        assertEquals(15.0, diagonalSum, 0.0001); // 1+5+9 = 15

        double antiDiagonalSum = matrix.streamRU2LD().sum();
        assertEquals(15.0, antiDiagonalSum, 0.0001); // 3+5+7 = 15
    }

    @Test
    public void testRowColumnStatistics() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        // Test statistics on individual rows
        List<Double> rowSums = matrix.streamR().map(row -> row.sum()).toList();
        assertEquals(3, rowSums.size());
        assertEquals(6.0, rowSums.get(0).doubleValue(), 0.0001); // 1+2+3
        assertEquals(15.0, rowSums.get(1).doubleValue(), 0.0001); // 4+5+6
        assertEquals(24.0, rowSums.get(2).doubleValue(), 0.0001); // 7+8+9

        // Test statistics on individual columns
        List<Double> colSums = matrix.streamC().map(col -> col.sum()).toList();
        assertEquals(3, colSums.size());
        assertEquals(12.0, colSums.get(0).doubleValue(), 0.0001); // 1+4+7
        assertEquals(15.0, colSums.get(1).doubleValue(), 0.0001); // 2+5+8
        assertEquals(18.0, colSums.get(2).doubleValue(), 0.0001); // 3+6+9

        // Test min/max per row
        List<Double> rowMins = matrix.streamR().map(row -> row.min().orElse(0.0)).toList();
        assertEquals(1.0, rowMins.get(0).doubleValue(), 0.0001);
        assertEquals(4.0, rowMins.get(1).doubleValue(), 0.0001);
        assertEquals(7.0, rowMins.get(2).doubleValue(), 0.0001);

        List<Double> rowMaxs = matrix.streamR().map(row -> row.max().orElse(0.0)).toList();
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
        double totalSum = largeSum.streamH().sum();
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
}
