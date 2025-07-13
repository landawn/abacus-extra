package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.landawn.abacus.util.Sheet.Point;

public class DoubleMatrixTest {

    @Test
    public void testConstructor() {
        // Test with null array
        DoubleMatrix matrix1 = new DoubleMatrix(null);
        assertEquals(0, matrix1.rows);
        assertEquals(0, matrix1.cols);

        // Test with valid array
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix2 = new DoubleMatrix(arr);
        assertEquals(2, matrix2.rows);
        assertEquals(2, matrix2.cols);
    }

    @Test
    public void testEmpty() {
        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
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
        assertEquals(2, matrix.rows);
        assertEquals(2, matrix.cols);
    }

    @Test
    public void testCreateFromInt() {
        int[][] intArr = { { 1, 2 }, { 3, 4 } };
        DoubleMatrix matrix = DoubleMatrix.create(intArr);
        assertEquals(2, matrix.rows);
        assertEquals(2, matrix.cols);
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(4.0, matrix.get(1, 1));

        // Test empty
        assertTrue(DoubleMatrix.create((int[][]) null).isEmpty());
    }

    @Test
    public void testCreateFromLong() {
        long[][] longArr = { { 1L, 2L }, { 3L, 4L } };
        DoubleMatrix matrix = DoubleMatrix.create(longArr);
        assertEquals(2, matrix.rows);
        assertEquals(2, matrix.cols);
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(4.0, matrix.get(1, 1));
    }

    @Test
    public void testCreateFromFloat() {
        float[][] floatArr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        DoubleMatrix matrix = DoubleMatrix.create(floatArr);
        assertEquals(2, matrix.rows);
        assertEquals(2, matrix.cols);
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(4.0, matrix.get(1, 1));
    }

    @Test
    public void testRandom() {
        DoubleMatrix matrix = DoubleMatrix.random(5);
        assertEquals(1, matrix.rows);
        assertEquals(5, matrix.cols);
    }

    @Test
    public void testRepeat() {
        DoubleMatrix matrix = DoubleMatrix.repeat(3.14, 5);
        assertEquals(1, matrix.rows);
        assertEquals(5, matrix.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals(3.14, matrix.get(0, i));
        }
    }

    @Test
    public void testDiagonalLU2RD() {
        double[] diagonal = { 1.0, 2.0, 3.0 };
        DoubleMatrix matrix = DoubleMatrix.diagonalLU2RD(diagonal);
        assertEquals(3, matrix.rows);
        assertEquals(3, matrix.cols);
        assertEquals(1.0, matrix.get(0, 0));
        assertEquals(2.0, matrix.get(1, 1));
        assertEquals(3.0, matrix.get(2, 2));
        assertEquals(0.0, matrix.get(0, 1));
    }

    @Test
    public void testDiagonalRU2LD() {
        double[] diagonal = { 1.0, 2.0, 3.0 };
        DoubleMatrix matrix = DoubleMatrix.diagonalRU2LD(diagonal);
        assertEquals(3, matrix.rows);
        assertEquals(3, matrix.cols);
        assertEquals(1.0, matrix.get(0, 2));
        assertEquals(2.0, matrix.get(1, 1));
        assertEquals(3.0, matrix.get(2, 0));
    }

    @Test
    public void testDiagonal() {
        double[] mainDiag = { 1.0, 2.0, 3.0 };
        double[] antiDiag = { 7.0, 8.0, 9.0 };
        DoubleMatrix matrix = DoubleMatrix.diagonal(mainDiag, antiDiag);
        assertEquals(3, matrix.rows);
        assertEquals(3, matrix.cols);
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
        assertEquals(2, unboxed.rows);
        assertEquals(2, unboxed.cols);
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
    public void testAdjacent4Points() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        List<Point> points = matrix.adjacent4Points(1, 1).toList();
        assertEquals(4, points.size());
    }

    @Test
    public void testAdjacent8Points() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        List<Point> points = matrix.adjacent8Points(1, 1).toList();
        assertEquals(8, points.size());
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

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.fill(-1, 0, fillArr));
    }

    @Test
    public void testCopy() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix copy = matrix.copy();
        assertEquals(matrix.rows, copy.rows);
        assertEquals(matrix.cols, copy.cols);
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
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(1.0, copy.get(0, 0));
        assertEquals(3.0, copy.get(1, 0));

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.copy(-1, 2));
    }

    @Test
    public void testCopyWithFullRange() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix copy = matrix.copy(0, 2, 1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(2.0, copy.get(0, 0));
        assertEquals(5.0, copy.get(1, 0));
    }

    @Test
    public void testExtend() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix extended = matrix.extend(3, 3);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(0.0, extended.get(2, 2));
    }

    @Test
    public void testExtendWithDefault() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix extended = matrix.extend(3, 3, -1.0);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(-1.0, extended.get(2, 2));

        assertThrows(IllegalArgumentException.class, () -> matrix.extend(-1, 3, -1.0));
    }

    @Test
    public void testExtendWithDirections() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix extended = matrix.extend(1, 1, 1, 1);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals(0.0, extended.get(0, 0));
    }

    @Test
    public void testExtendWithDirectionsAndDefault() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix extended = matrix.extend(1, 1, 1, 1, -1.0);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
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
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3.0, rotated.get(0, 0));
        assertEquals(1.0, rotated.get(0, 1));
    }

    @Test
    public void testRotate180() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix rotated = matrix.rotate180();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(4.0, rotated.get(0, 0));
        assertEquals(3.0, rotated.get(0, 1));
    }

    @Test
    public void testRotate270() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix rotated = matrix.rotate270();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(2.0, rotated.get(0, 0));
        assertEquals(4.0, rotated.get(0, 1));
    }

    @Test
    public void testTranspose() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix transposed = matrix.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1.0, transposed.get(0, 0));
        assertEquals(4.0, transposed.get(0, 1));
    }

    @Test
    public void testReshape() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        DoubleMatrix reshaped = matrix.reshape(1, 4);
        assertEquals(1, reshaped.rows);
        assertEquals(4, reshaped.cols);
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
        assertEquals(2, repeated.rows);
        assertEquals(6, repeated.cols);
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
        assertEquals(2, tiled.rows);
        assertEquals(6, tiled.cols);
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
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
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
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
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
        assertEquals(2, boxed.rows);
        assertEquals(2, boxed.cols);
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

        List<double[]> cols = matrix.streamC().map(stream -> stream.toArray()).toList();
        assertEquals(2, cols.size());
        assertEquals(2, cols.get(0).length);
        assertEquals(1.0, cols.get(0)[0]);
        assertEquals(3.0, cols.get(0)[1]);
    }

    @Test
    public void testStreamCWithRange() {
        double[][] arr = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        DoubleMatrix matrix = DoubleMatrix.of(arr);

        List<double[]> cols = matrix.streamC(1, 3).map(stream -> stream.toArray()).toList();
        assertEquals(2, cols.size());

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
}