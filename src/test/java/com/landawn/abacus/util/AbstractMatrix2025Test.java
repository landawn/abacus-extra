package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;

/**
 * Comprehensive unit tests for AbstractMatrix.
 * Tests common matrix functionality through concrete subclass implementations.
 */
@Tag("2025")
public class AbstractMatrix2025Test extends TestBase {

    // ============ Basic Property Tests ============

    @Test
    public void testRowsColsCount() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertEquals(2, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(6, m.elementCount());
    }

    @Test
    public void testRowsColsCount_squareMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(4, m.elementCount());
    }

    @Test
    public void testRowsColsCount_singleElement() {
        IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(1, m.elementCount());
    }

    @Test
    public void testRowsColsCount_emptyMatrix() {
        IntMatrix m = IntMatrix.empty();
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertEquals(0, m.elementCount());
    }

    @Test
    public void testComponentType_intMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertEquals(int.class, m.componentType());
    }

    @Test
    public void testComponentType_doubleMatrix() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertEquals(double.class, m.componentType());
    }

    @Test
    public void testComponentType_objectMatrix() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b" } });
        assertEquals(String.class, m.componentType());
    }

    @Test
    public void testArray() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.of(arr);
        int[][] returnedArray = m.array();

        // Should return the same underlying array
        assertNotNull(returnedArray);
        assertEquals(2, returnedArray.length);
        assertArrayEquals(new int[] { 1, 2 }, returnedArray[0]);
        assertArrayEquals(new int[] { 3, 4 }, returnedArray[1]);
    }

    // ============ isEmpty Tests ============

    @Test
    public void testIsEmpty_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testIsEmpty_nonEmptyMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1 } });
        assertFalse(m.isEmpty());
    }

    @Test
    public void testIsEmpty_zeroRows() {
        IntMatrix m = IntMatrix.of(new int[0][0]);
        assertTrue(m.isEmpty());
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy_intMatrix() {
        IntMatrix original = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix copy = original.copy();

        assertEquals(original.rowCount(), copy.rowCount());
        assertEquals(original.columnCount(), copy.columnCount());
        assertEquals(1, copy.get(0, 0));
        assertEquals(4, copy.get(1, 1));

        // Modify copy should not affect original
        copy.set(0, 0, 99);
        assertEquals(1, original.get(0, 0));
        assertEquals(99, copy.get(0, 0));
    }

    @Test
    public void testCopy_doubleMatrix() {
        DoubleMatrix original = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
        DoubleMatrix copy = original.copy();

        assertEquals(original.rowCount(), copy.rowCount());
        assertEquals(original.columnCount(), copy.columnCount());
        assertEquals(1.5, copy.get(0, 0), 0.0001);
        assertEquals(4.5, copy.get(1, 1), 0.0001);
    }

    @Test
    public void testCopy_objectMatrix() {
        Matrix<String> original = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
        Matrix<String> copy = original.copy();

        assertEquals(original.rowCount(), copy.rowCount());
        assertEquals(original.columnCount(), copy.columnCount());
        assertEquals("a", copy.get(0, 0));
        assertEquals("d", copy.get(1, 1));
    }

    @Test
    public void testCopy_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        IntMatrix copy = empty.copy();
        assertTrue(copy.isEmpty());
    }

    @Test
    public void testCopy_withRowRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix subset = m.copy(1, 3);

        assertEquals(2, subset.rowCount());
        assertEquals(3, subset.columnCount());
        assertEquals(4, subset.get(0, 0));
        assertEquals(9, subset.get(1, 2));
    }

    @Test
    public void testCopy_withRowRange_singleRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix subset = m.copy(1, 2);

        assertEquals(1, subset.rowCount());
        assertEquals(3, subset.columnCount());
        assertArrayEquals(new int[] { 4, 5, 6 }, subset.row(0));
    }

    @Test
    public void testCopy_withRowRange_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
    }

    @Test
    public void testCopy_withFullRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix submatrix = m.copy(0, 2, 1, 3);

        assertEquals(2, submatrix.rowCount());
        assertEquals(2, submatrix.columnCount());
        assertEquals(2, submatrix.get(0, 0));
        assertEquals(6, submatrix.get(1, 1));
    }

    @Test
    public void testCopy_withFullRange_entireMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix copy = m.copy(0, 2, 0, 2);

        assertEquals(m, copy);
    }

    @Test
    public void testCopy_withFullRange_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2, 0, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3, 0, 2));
    }

    // ============ Transformation Tests ============

    @Test
    public void testRotate90() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix rotated = m.rotate90();

        assertEquals(3, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(4, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(6, rotated.get(2, 0));
        assertEquals(3, rotated.get(2, 1));
    }

    @Test
    public void testRotate90_squareMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate90();

        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(4, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix rotated = m.rotate180();

        assertEquals(2, rotated.rowCount());
        assertEquals(3, rotated.columnCount());
        assertEquals(6, rotated.get(0, 0));
        assertEquals(5, rotated.get(0, 1));
        assertEquals(4, rotated.get(0, 2));
        assertEquals(3, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
        assertEquals(1, rotated.get(1, 2));
    }

    @Test
    public void testRotate180_squareMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate180();

        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix rotated = m.rotate270();

        assertEquals(3, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3, rotated.get(0, 0));
        assertEquals(6, rotated.get(0, 1));
        assertEquals(1, rotated.get(2, 0));
        assertEquals(4, rotated.get(2, 1));
    }

    @Test
    public void testRotate270_squareMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate270();

        assertEquals(2, rotated.get(0, 0));
        assertEquals(4, rotated.get(0, 1));
        assertEquals(1, rotated.get(1, 0));
        assertEquals(3, rotated.get(1, 1));
    }

    @Test
    public void testTranspose_rectangularMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix transposed = m.transpose();

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
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix transposed = m.transpose();

        assertEquals(2, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1, transposed.get(0, 0));
        assertEquals(3, transposed.get(0, 1));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(4, transposed.get(1, 1));
    }

    @Test
    public void testTranspose_doubleTranspose() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix transposed = m.transpose().transpose();

        assertEquals(m, transposed);
    }

    // ============ Reshape Tests ============

    @Test
    public void testReshape_withNegativeDimensions_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Matrix.of(new String[][] { { "a" } }).reshape(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> Matrix.of(new String[][] { { "a" } }).reshape(1, -1));

        assertThrows(IllegalArgumentException.class, () -> BooleanMatrix.of(new boolean[][] { { true } }).reshape(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> ByteMatrix.of(new byte[][] { { 1 } }).reshape(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> CharMatrix.of(new char[][] { { 'a' } }).reshape(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> ShortMatrix.of(new short[][] { { 1 } }).reshape(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.of(new int[][] { { 1 } }).reshape(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> LongMatrix.of(new long[][] { { 1L } }).reshape(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.of(new float[][] { { 1F } }).reshape(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.of(new double[][] { { 1D } }).reshape(-1, 1));
    }

    @Test
    public void testReshape_withCols() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3, 4 }, { 5, 6, 7, 8 } });
        IntMatrix reshaped = m.reshape(2);

        assertEquals(4, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
    }

    @Test
    public void testReshape_withRowsAndCols() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = m.reshape(3, 2);

        assertEquals(3, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
        assertEquals(4, reshaped.get(1, 1));
        assertEquals(5, reshaped.get(2, 0));
        assertEquals(6, reshaped.get(2, 1));
    }

    @Test
    public void testReshape_toSingleRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix reshaped = m.reshape(1, 4);

        assertEquals(1, reshaped.rowCount());
        assertEquals(4, reshaped.columnCount());
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, reshaped.row(0));
    }

    @Test
    public void testReshape_toSingleColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix reshaped = m.reshape(4, 1);

        assertEquals(4, reshaped.rowCount());
        assertEquals(1, reshaped.columnCount());
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, reshaped.column(0));
    }

    // ============ isSameShape Tests ============

    @Test
    public void testIsSameShape_sameShape() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        assertTrue(m1.isSameShape(m2));
    }

    @Test
    public void testIsSameShape_differentShape() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        assertFalse(m1.isSameShape(m2));
    }

    @Test
    public void testIsSameShape_differentRows() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertFalse(m1.isSameShape(m2));
    }

    @Test
    public void testIsSameShape_differentCols() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        assertFalse(m1.isSameShape(m2));
    }

    @Test
    public void testIsSameShape_emptyMatrices() {
        IntMatrix m1 = IntMatrix.empty();
        IntMatrix m2 = IntMatrix.empty();
        assertTrue(m1.isSameShape(m2));
    }

    // ============ Repelem and Repmat Tests ============

    @Test
    public void testRepelem() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix repeated = m.repelem(2, 3);

        assertEquals(2, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertEquals(1, repeated.get(0, 0));
        assertEquals(1, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2));
        assertEquals(2, repeated.get(0, 3));
        assertEquals(2, repeated.get(0, 4));
        assertEquals(2, repeated.get(0, 5));
    }

    @Test
    public void testRepelem_invalidArguments() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(-1, 1));
    }

    @Test
    public void testRepmat() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix repeated = m.repmat(2, 3);

        assertEquals(4, repeated.rowCount());
        assertEquals(6, repeated.columnCount());
        assertEquals(1, repeated.get(0, 0));
        assertEquals(2, repeated.get(0, 1));
        assertEquals(1, repeated.get(0, 2));
        assertEquals(1, repeated.get(2, 0));
    }

    @Test
    public void testRepmat_invalidArguments() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(-1, 1));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntList flat = m.flatten();

        assertEquals(6, flat.size());
        for (int i = 0; i < 6; i++) {
            assertEquals(i + 1, flat.get(i));
        }
    }

    @Test
    public void testFlatten_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        IntList flat = empty.flatten();
        assertTrue(flat.isEmpty());
    }

    @Test
    public void testFlatten_singleElement() {
        IntMatrix m = IntMatrix.of(new int[][] { { 42 } });
        IntList flat = m.flatten();

        assertEquals(1, flat.size());
        assertEquals(42, flat.get(0));
    }

    // ============ FlatOp Tests ============

    @Test
    public void testFlatOp() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<Integer> values = new ArrayList<>();

        m.flatOp(arr -> {
            for (int val : arr) {
                values.add(val);
            }
        });

        assertEquals(6, values.size());
        assertEquals(1, values.get(0).intValue());
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach_withIndices() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<String> positions = new ArrayList<>();

        m.forEach((i, j) -> positions.add(i + "," + j));

        assertEquals(4, positions.size());
        assertEquals("0,0", positions.get(0));
        assertEquals("0,1", positions.get(1));
        assertEquals("1,0", positions.get(2));
        assertEquals("1,1", positions.get(3));
    }

    @Test
    public void testForEach_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<String> positions = new ArrayList<>();

        m.forEach(1, 3, 1, 3, (i, j) -> positions.add(i + "," + j));

        assertEquals(4, positions.size());
        assertEquals("1,1", positions.get(0));
        assertEquals("1,2", positions.get(1));
        assertEquals("2,1", positions.get(2));
        assertEquals("2,2", positions.get(3));
    }

    @Test
    public void testForEach_withRange_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(-1, 2, 0, 2, (i, j) -> {
        }));
        assertThrows(IndexOutOfBoundsException.class, () -> m.forEach(0, 3, 0, 2, (i, j) -> {
        }));
    }

    @Test
    public void testForEach_withMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Integer> values = new ArrayList<>();

        m.forEach((i, j, matrix) -> values.add(matrix.get(i, j)));

        assertEquals(4, values.size());
        assertEquals(1, values.get(0).intValue());
        assertEquals(2, values.get(1).intValue());
        assertEquals(3, values.get(2).intValue());
        assertEquals(4, values.get(3).intValue());
    }

    @Test
    public void testForEach_withMatrixAndRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Integer> values = new ArrayList<>();

        m.forEach(1, 3, 1, 3, (i, j, matrix) -> values.add(matrix.get(i, j)));

        assertEquals(4, values.size());
        assertEquals(5, values.get(0).intValue());
        assertEquals(6, values.get(1).intValue());
        assertEquals(8, values.get(2).intValue());
        assertEquals(9, values.get(3).intValue());
    }

    // ============ Point Stream Tests ============

    @Test
    public void testPointsLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Point> points = m.pointsLU2RD().toList();

        assertEquals(3, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 2), points.get(2));
    }

    @Test
    public void testPointsLU2RD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.pointsLU2RD());
    }

    @Test
    public void testPointsRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Point> points = m.pointsRU2LD().toList();

        assertEquals(3, points.size());
        assertEquals(Point.of(0, 2), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 0), points.get(2));
    }

    @Test
    public void testPointsRU2LD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.pointsRU2LD());
    }

    @Test
    public void testPointsH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.pointsH().toList();

        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(0, 1), points.get(1));
        assertEquals(Point.of(1, 0), points.get(2));
        assertEquals(Point.of(1, 1), points.get(3));
    }

    @Test
    public void testPointsH_withRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.pointsH(1).toList();

        assertEquals(2, points.size());
        assertEquals(Point.of(1, 0), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
    }

    @Test
    public void testPointsH_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        List<Point> points = m.pointsH(1, 3).toList();

        assertEquals(4, points.size());
        assertEquals(Point.of(1, 0), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 0), points.get(2));
        assertEquals(Point.of(2, 1), points.get(3));
    }

    @Test
    public void testPointsH_withRange_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.pointsH(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.pointsH(0, 3));
    }

    @Test
    public void testPointsV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.pointsV().toList();

        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 0), points.get(1));
        assertEquals(Point.of(0, 1), points.get(2));
        assertEquals(Point.of(1, 1), points.get(3));
    }

    @Test
    public void testPointsV_withColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = m.pointsV(1).toList();

        assertEquals(2, points.size());
        assertEquals(Point.of(0, 1), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
    }

    @Test
    public void testPointsV_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<Point> points = m.pointsV(1, 3).toList();

        assertEquals(4, points.size());
        assertEquals(Point.of(0, 1), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(0, 2), points.get(2));
        assertEquals(Point.of(1, 2), points.get(3));
    }

    @Test
    public void testPointsV_withRange_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.pointsV(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.pointsV(0, 3));
    }

    @Test
    public void testPointsR() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<List<Point>> rowPoints = m.pointsR().map(s -> s.toList()).toList();

        assertEquals(2, rowPoints.size());
        assertEquals(2, rowPoints.get(0).size());
        assertEquals(Point.of(0, 0), rowPoints.get(0).get(0));
        assertEquals(Point.of(0, 1), rowPoints.get(0).get(1));
    }

    @Test
    public void testPointsR_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        List<List<Point>> rowPoints = m.pointsR(1, 3).map(s -> s.toList()).toList();

        assertEquals(2, rowPoints.size());
        assertEquals(Point.of(1, 0), rowPoints.get(0).get(0));
        assertEquals(Point.of(2, 0), rowPoints.get(1).get(0));
    }

    @Test
    public void testPointsR_withRange_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.pointsR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.pointsR(0, 3));
    }

    @Test
    public void testPointsC() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<List<Point>> colPoints = m.pointsC().map(s -> s.toList()).toList();

        assertEquals(2, colPoints.size());
        assertEquals(2, colPoints.get(0).size());
        assertEquals(Point.of(0, 0), colPoints.get(0).get(0));
        assertEquals(Point.of(1, 0), colPoints.get(0).get(1));
    }

    @Test
    public void testPointsC_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<List<Point>> colPoints = m.pointsC(1, 3).map(s -> s.toList()).toList();

        assertEquals(2, colPoints.size());
        assertEquals(Point.of(0, 1), colPoints.get(0).get(0));
        assertEquals(Point.of(0, 2), colPoints.get(1).get(0));
    }

    @Test
    public void testPointsC_withRange_outOfBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.pointsC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.pointsC(0, 3));
    }

    // ============ Element Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] diagonal = m.streamLU2RD().toArray();
        assertArrayEquals(new int[] { 1, 5, 9 }, diagonal);
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] antiDiagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new int[] { 3, 5, 7 }, antiDiagonal);
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.streamRU2LD());
    }

    @Test
    public void testStreamH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] all = m.streamH().toArray();
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, all);
    }

    @Test
    public void testStreamH_withRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new int[] { 4, 5, 6 }, row1);
    }

    @Test
    public void testStreamH_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new int[] { 4, 5, 6, 7, 8, 9 }, rows);
    }

    @Test
    public void testStreamV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] all = m.streamV().toArray();
        assertArrayEquals(new int[] { 1, 4, 2, 5, 3, 6 }, all);
    }

    @Test
    public void testStreamV_withColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new int[] { 2, 5 }, col1);
    }

    @Test
    public void testStreamV_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        int[] columnCount = m.streamV(1, 3).toArray();
        assertArrayEquals(new int[] { 2, 5, 3, 6 }, columnCount);
    }

    @Test
    public void testStreamR() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<int[]> rows = m.streamR().map(s -> s.toArray()).toList();

        assertEquals(2, rows.size());
        assertArrayEquals(new int[] { 1, 2, 3 }, rows.get(0));
        assertArrayEquals(new int[] { 4, 5, 6 }, rows.get(1));
    }

    @Test
    public void testStreamR_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<int[]> rows = m.streamR(1, 3).map(s -> s.toArray()).toList();

        assertEquals(2, rows.size());
        assertArrayEquals(new int[] { 4, 5, 6 }, rows.get(0));
        assertArrayEquals(new int[] { 7, 8, 9 }, rows.get(1));
    }

    @Test
    public void testStreamC() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<int[]> columnCount = m.streamC().map(s -> s.toArray()).toList();

        assertEquals(3, columnCount.size());
        assertArrayEquals(new int[] { 1, 4 }, columnCount.get(0));
        assertArrayEquals(new int[] { 2, 5 }, columnCount.get(1));
        assertArrayEquals(new int[] { 3, 6 }, columnCount.get(2));
    }

    @Test
    public void testStreamC_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<int[]> columnCount = m.streamC(1, 3).map(s -> s.toArray()).toList();

        assertEquals(2, columnCount.size());
        assertArrayEquals(new int[] { 2, 5 }, columnCount.get(0));
        assertArrayEquals(new int[] { 3, 6 }, columnCount.get(1));
    }

    // ============ Accept and Apply Tests ============

    @Test
    public void testAccept() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Integer> sums = new ArrayList<>();

        m.accept(matrix -> {
            int sum = 0;
            for (int i = 0; i < matrix.rowCount(); i++) {
                for (int j = 0; j < matrix.columnCount(); j++) {
                    sum += matrix.get(i, j);
                }
            }
            sums.add(sum);
        });

        assertEquals(1, sums.size());
        assertEquals(10, sums.get(0).intValue());
    }

    @Test
    public void testApply() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        Integer sum = m.apply(matrix -> {
            int total = 0;
            for (int i = 0; i < matrix.rowCount(); i++) {
                for (int j = 0; j < matrix.columnCount(); j++) {
                    total += matrix.get(i, j);
                }
            }
            return total;
        });

        assertEquals(10, sum.intValue());
    }

    @Test
    public void testApply_returnString() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        String result = m.apply(matrix -> "Matrix is " + matrix.rowCount() + "x" + matrix.columnCount());
        assertEquals("Matrix is 2x2", result);
    }

    // ============ Edge Cases ============

    @Test
    public void testEmptyMatrix_operations() {
        IntMatrix empty = IntMatrix.empty();

        assertTrue(empty.isEmpty());
        assertTrue(empty.flatten().isEmpty());
        assertEquals(0, empty.streamH().count());
        assertEquals(0, empty.streamV().count());
        assertEquals(0, empty.streamR().count());
        assertEquals(0, empty.streamC().count());
    }

    @Test
    public void testSingleElement_operations() {
        IntMatrix single = IntMatrix.of(new int[][] { { 42 } });

        assertFalse(single.isEmpty());
        assertEquals(1, single.flatten().size());
        assertEquals(42, single.streamH().findFirst().orElse(0));
    }

    @Test
    public void testLargeMatrix_count() {
        int[][] large = new int[1000][1000];
        IntMatrix m = IntMatrix.of(large);

        assertEquals(1000, m.rowCount());
        assertEquals(1000, m.columnCount());
        assertEquals(1000000L, m.elementCount());
    }

    // ============ Constructor Validation Tests ============

    @Test
    public void testConstructor_nullArray() {
        IntMatrix m = IntMatrix.of(null);
        assertTrue(m.isEmpty());
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testConstructor_inconsistentRowLengths() {
        int[][] jagged = new int[][] { { 1, 2, 3 }, { 4, 5 }, { 7, 8, 9 } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.of(jagged));
    }

    @Test
    public void testConstructor_inconsistentRowLengths_threeRows() {
        int[][] jagged = new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6, 7 } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.of(jagged));
    }

    @Test
    public void testConstructor_singleRowArray() {
        int[][] singleRow = new int[][] { { 1, 2, 3 } };
        IntMatrix m = IntMatrix.of(singleRow);
        assertEquals(1, m.rowCount());
        assertEquals(3, m.columnCount());
    }

    @Test
    public void testConstructor_emptyRowsInArray() {
        int[][] emptyRows = new int[][] { {}, {} };
        IntMatrix m = IntMatrix.of(emptyRows);
        assertEquals(2, m.rowCount());
        assertEquals(0, m.columnCount());
        assertEquals(0, m.elementCount());
    }

    // ============ Reshape Edge Cases ============

    @Test
    public void testReshape_withCols_notEvenlyDivisible() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = m.reshape(4);

        assertEquals(2, reshaped.rowCount());
        assertEquals(4, reshaped.columnCount());
    }

    @Test
    public void testReshape_withCols_evenlyDivisible() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        IntMatrix reshaped = m.reshape(2);

        assertEquals(3, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
    }

    @Test
    public void testReshape_largerThanOriginal() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix reshaped = m.reshape(3, 3);

        assertEquals(3, reshaped.rowCount());
        assertEquals(3, reshaped.columnCount());
        assertEquals(0, reshaped.get(2, 2));
    }

    // ============ Additional Stream Tests ============

    @Test
    public void testStreamH_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        long count = empty.streamH().count();
        assertEquals(0, count);
    }

    @Test
    public void testStreamV_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        long count = empty.streamV().count();
        assertEquals(0, count);
    }

    @Test
    public void testStreamR_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        long count = empty.streamR().count();
        assertEquals(0, count);
    }

    @Test
    public void testStreamC_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        long count = empty.streamC().count();
        assertEquals(0, count);
    }

    // ============ ForEach Edge Cases ============

    @Test
    public void testForEach_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        List<String> positions = new ArrayList<>();

        empty.forEach((i, j) -> positions.add(i + "," + j));

        assertTrue(positions.isEmpty());
    }

    @Test
    public void testForEach_emptyRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<String> positions = new ArrayList<>();

        m.forEach(1, 1, 0, 2, (i, j) -> positions.add(i + "," + j));

        assertTrue(positions.isEmpty());
    }

    @Test
    public void testForEach_withMatrix_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        List<Integer> values = new ArrayList<>();

        empty.forEach((i, j, matrix) -> values.add(matrix.get(i, j)));

        assertTrue(values.isEmpty());
    }

    @Test
    public void testForEach_withMatrixAndRange_emptyRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Integer> values = new ArrayList<>();

        m.forEach(0, 0, 1, 1, (i, j, matrix) -> values.add(matrix.get(i, j)));

        assertTrue(values.isEmpty());
    }

    // ============ Point Stream Edge Cases ============

    @Test
    public void testPointsH_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        long count = empty.pointsH().count();
        assertEquals(0, count);
    }

    @Test
    public void testPointsV_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        long count = empty.pointsV().count();
        assertEquals(0, count);
    }

    @Test
    public void testPointsR_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        long count = empty.pointsR().count();
        assertEquals(0, count);
    }

    @Test
    public void testPointsC_emptyMatrix() {
        IntMatrix empty = IntMatrix.empty();
        long count = empty.pointsC().count();
        assertEquals(0, count);
    }

    // ============ Apply and Accept Error Handling ============

    @Test
    public void testAccept_withException() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        assertThrows(RuntimeException.class, () -> m.accept(matrix -> {
            throw new RuntimeException("Test exception");
        }));
    }

    @Test
    public void testApply_withException() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });

        assertThrows(RuntimeException.class, () -> m.apply(matrix -> {
            throw new RuntimeException("Test exception");
        }));
    }

    // ============ Additional Edge Cases ============

    @Test
    public void testCopy_emptyRowRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        IntMatrix subset = m.copy(1, 1);

        assertEquals(0, subset.rowCount());
        assertEquals(0, subset.columnCount());
        assertTrue(subset.isEmpty());
    }

    @Test
    public void testCopy_emptyRegion() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix subset = m.copy(1, 1, 0, 2);

        assertEquals(0, subset.rowCount());
        assertEquals(0, subset.columnCount());
        assertTrue(subset.isEmpty());
    }

    @Test
    public void testIsSameShape_identicalMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertTrue(m.isSameShape(m));
    }

    @Test
    public void testReshape_sameShape() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix reshaped = m.reshape(2, 2);

        assertEquals(2, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(m, reshaped);
    }
}
