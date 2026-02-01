package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.stream.IntStream;
import com.landawn.abacus.util.stream.Stream;

@Tag("2512")
public class AbstractMatrix2512Test extends TestBase {

    // ============ Dimensional Methods Tests ============

    @Test
    public void test_isEmpty_emptyMatrix() {
        IntMatrix matrix = IntMatrix.of(new int[0][0]);
        assertTrue(matrix.isEmpty());
    }

    @Test
    public void test_isEmpty_nonEmptyMatrix() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertFalse(matrix.isEmpty());
    }

    @Test
    public void test_isEmpty_zeroRows() {
        IntMatrix matrix = IntMatrix.of(new int[0][5]);
        assertTrue(matrix.isEmpty());
    }

    @Test
    public void test_isEmpty_zeroCols() {
        IntMatrix matrix = IntMatrix.of(new int[][] {});
        assertTrue(matrix.isEmpty());
    }

    // ============ Shape Methods Tests ============

    @Test
    public void test_isSameShape_sameShape() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        assertTrue(m1.isSameShape(m2));
    }

    @Test
    public void test_isSameShape_differentRows() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 } });
        assertFalse(m1.isSameShape(m2));
    }

    @Test
    public void test_isSameShape_differentCols() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6, 7 }, { 8, 9, 10 } });
        assertFalse(m1.isSameShape(m2));
    }

    // ============ forEach Tests ============

    @Test
    public void test_forEach_simpleIteration() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        AtomicInteger sum = new AtomicInteger(0);
        matrix.forEach((i, j) -> sum.addAndGet(matrix.get(i, j)));
        assertEquals(10, sum.get());
    }

    @Test
    public void test_forEach_withMatrix() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        AtomicInteger sum = new AtomicInteger(0);
        matrix.forEach((i, j, m) -> sum.addAndGet(m.get(i, j)));
        assertEquals(10, sum.get());
    }

    @Test
    public void test_forEach_withRegion() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        AtomicInteger sum = new AtomicInteger(0);
        matrix.forEach(0, 2, 0, 2, (i, j) -> sum.addAndGet(matrix.get(i, j)));
        assertEquals(12, sum.get()); // 1+2+4+5
    }

    @Test
    public void test_forEach_withRegionAndMatrix() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        AtomicInteger sum = new AtomicInteger(0);
        matrix.forEach(1, 3, 1, 3, (i, j, m) -> sum.addAndGet(m.get(i, j)));
        assertEquals(28, sum.get()); // 5+6+8+9
    }

    @Test
    public void test_forEach_emptyMatrix() {
        IntMatrix matrix = IntMatrix.of(new int[0][0]);
        AtomicInteger count = new AtomicInteger(0);
        matrix.forEach((i, j) -> count.incrementAndGet());
        assertEquals(0, count.get());
    }

    // ============ Point Stream Tests ============

    @Test
    public void test_adjacent4Points_centerPosition() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Point> points = matrix.adjacent4Points(1, 1).toList();
        assertEquals(4, points.size());
        assertTrue(points.contains(Point.of(0, 1))); // up
        assertTrue(points.contains(Point.of(1, 2))); // right
        assertTrue(points.contains(Point.of(2, 1))); // down
        assertTrue(points.contains(Point.of(1, 0))); // left
    }

    @Test
    public void test_adjacent4Points_topLeftCorner() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = matrix.adjacent4Points(0, 0).toList();
        assertEquals(2, points.size());
        assertTrue(points.contains(Point.of(0, 1))); // right
        assertTrue(points.contains(Point.of(1, 0))); // down
    }

    @Test
    public void test_adjacent4Points_bottomRightCorner() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = matrix.adjacent4Points(1, 1).toList();
        assertEquals(2, points.size());
        assertTrue(points.contains(Point.of(0, 1))); // up
        assertTrue(points.contains(Point.of(1, 0))); // left
    }

    @Test
    public void test_adjacent8Points_centerPosition() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Point> points = matrix.adjacent8Points(1, 1).toList();
        assertEquals(8, points.size());
    }

    @Test
    public void test_adjacent8Points_topLeftCorner() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = matrix.adjacent8Points(0, 0).toList();
        assertEquals(3, points.size());
        assertTrue(points.contains(Point.of(0, 1))); // right
        assertTrue(points.contains(Point.of(1, 1))); // rightDown
        assertTrue(points.contains(Point.of(1, 0))); // down
    }

    @Test
    public void test_adjacent8Points_bottomRightCorner() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = matrix.adjacent8Points(1, 1).toList();
        assertEquals(3, points.size());
        assertTrue(points.contains(Point.of(0, 0))); // leftUp
        assertTrue(points.contains(Point.of(0, 1))); // up
        assertTrue(points.contains(Point.of(1, 0))); // left
    }

    // ============ Diagonal Point Stream Tests ============

    @Test
    public void test_pointsLU2RD_squareMatrix() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Point> points = matrix.pointsLU2RD().toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 2), points.get(2));
    }

    @Test
    public void test_pointsLU2RD_nonSquareMatrix() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> matrix.pointsLU2RD().toList());
    }

    @Test
    public void test_pointsRU2LD_squareMatrix() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Point> points = matrix.pointsRU2LD().toList();
        assertEquals(3, points.size());
        assertEquals(Point.of(0, 2), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 0), points.get(2));
    }

    @Test
    public void test_pointsRU2LD_nonSquareMatrix() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> matrix.pointsRU2LD().toList());
    }

    // ============ Horizontal Point Stream Tests ============

    @Test
    public void test_pointsH_allPoints() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = matrix.pointsH().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(0, 1), points.get(1));
        assertEquals(Point.of(1, 0), points.get(2));
        assertEquals(Point.of(1, 1), points.get(3));
    }

    @Test
    public void test_pointsH_singleRow() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = matrix.pointsH(1).toList();
        assertEquals(2, points.size());
        assertEquals(Point.of(1, 0), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
    }

    @Test
    public void test_pointsH_rowRange() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        List<Point> points = matrix.pointsH(1, 3).toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(1, 0), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(2, 0), points.get(2));
        assertEquals(Point.of(2, 1), points.get(3));
    }

    // ============ Vertical Point Stream Tests ============

    @Test
    public void test_pointsV_allPoints() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = matrix.pointsV().toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 0), points.get(1));
        assertEquals(Point.of(0, 1), points.get(2));
        assertEquals(Point.of(1, 1), points.get(3));
    }

    @Test
    public void test_pointsV_singleColumn() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<Point> points = matrix.pointsV(0).toList();
        assertEquals(2, points.size());
        assertEquals(Point.of(0, 0), points.get(0));
        assertEquals(Point.of(1, 0), points.get(1));
    }

    @Test
    public void test_pointsV_columnRange() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<Point> points = matrix.pointsV(1, 3).toList();
        assertEquals(4, points.size());
        assertEquals(Point.of(0, 1), points.get(0));
        assertEquals(Point.of(1, 1), points.get(1));
        assertEquals(Point.of(0, 2), points.get(2));
        assertEquals(Point.of(1, 2), points.get(3));
    }

    // ============ Row/Column Point Stream Tests ============

    @Test
    public void test_pointsR_allRows() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<List<Point>> rows = matrix.pointsR().map(Stream::toList).toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).size());
        assertEquals(Point.of(0, 0), rows.get(0).get(0));
        assertEquals(Point.of(0, 1), rows.get(0).get(1));
    }

    @Test
    public void test_pointsR_rowRange() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        List<List<Point>> rows = matrix.pointsR(1, 3).map(Stream::toList).toList();
        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).size());
    }

    @Test
    public void test_pointsC_allColumns() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        List<List<Point>> columnCount = matrix.pointsC().map(Stream::toList).toList();
        assertEquals(2, columnCount.size());
        assertEquals(2, columnCount.get(0).size());
        assertEquals(Point.of(0, 0), columnCount.get(0).get(0));
        assertEquals(Point.of(1, 0), columnCount.get(0).get(1));
    }

    @Test
    public void test_pointsC_columnRange() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<List<Point>> columnCount = matrix.pointsC(0, 2).map(Stream::toList).toList();
        assertEquals(2, columnCount.size());
        assertEquals(2, columnCount.get(0).size());
    }

    // ============ Accept and Apply Tests ============

    @Test
    public void test_accept() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        AtomicInteger rows = new AtomicInteger(0);
        matrix.accept(m -> rows.set(m.rowCount()));
        assertEquals(2, rows.get());
    }

    @Test
    public void test_apply() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        int totalElements = matrix.apply(m -> m.rowCount() * m.columnCount());
        assertEquals(4, totalElements);
    }

    @Test
    public void test_apply_returnString() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        String result = matrix.apply(m -> "Matrix " + m.rowCount() + "x" + m.columnCount());
        assertEquals("Matrix 2x2", result);
    }

    // ============ Reshape Tests ============

    @Test
    public void test_reshape_singleParameter() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = matrix.reshape(2);
        assertEquals(3, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
    }

    @Test
    public void test_reshape_singleParameter_withPadding() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = matrix.reshape(4);
        assertEquals(2, reshaped.rowCount());
        assertEquals(4, reshaped.columnCount());
        assertEquals(5, reshaped.get(1, 0));
        assertEquals(6, reshaped.get(1, 1));
        assertEquals(0, reshaped.get(1, 2)); // Padded
        assertEquals(0, reshaped.get(1, 3)); // Padded
    }

    // ============ Component Type Test ============

    @Test
    public void test_componentType_intMatrix() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(int.class, matrix.componentType());
    }

    @Test
    public void test_componentType_doubleMatrix() {
        DoubleMatrix matrix = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertEquals(double.class, matrix.componentType());
    }

    @Test
    public void test_componentType_booleanMatrix() {
        BooleanMatrix matrix = BooleanMatrix.of(new boolean[][] { { true, false }, { false, true } });
        assertEquals(boolean.class, matrix.componentType());
    }

    // ============ Array Method Test ============

    @Test
    public void test_array_returnsInternalArray() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        IntMatrix matrix = IntMatrix.of(arr);
        int[][] returned = matrix.array();
        assertEquals(arr, returned); // Same reference
        returned[0][0] = 999;
        assertEquals(999, matrix.get(0, 0)); // Modification affects matrix
    }

    // ============ Println Test ============

    @Test
    public void test_println_returnsString() {
        IntMatrix matrix = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        String result = matrix.println();
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
    }
}
