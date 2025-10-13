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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalDouble;
import com.landawn.abacus.util.stream.DoubleStream;

@Tag("2025")
public class DoubleMatrix2025Test extends TestBase {

    private static final double DELTA = 0.0001;

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
        DoubleMatrix m = new DoubleMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.5, m.get(0, 0), DELTA);
        assertEquals(4.5, m.get(1, 1), DELTA);
    }

    @Test
    public void testConstructor_withNullArray() {
        DoubleMatrix m = new DoubleMatrix(null);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withEmptyArray() {
        DoubleMatrix m = new DoubleMatrix(new double[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        DoubleMatrix m = new DoubleMatrix(new double[][] { { 42.75 } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(42.75, m.get(0, 0), DELTA);
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());

        // Test singleton
        assertSame(DoubleMatrix.empty(), DoubleMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        double[][] arr = { { 1.5, 2.5 }, { 3.5, 4.5 } };
        DoubleMatrix m = DoubleMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
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
        DoubleMatrix m = DoubleMatrix.create(ints);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.0, m.get(0, 0), DELTA);
        assertEquals(2.0, m.get(0, 1), DELTA);
        assertEquals(3.0, m.get(1, 0), DELTA);
        assertEquals(4.0, m.get(1, 1), DELTA);
    }

    @Test
    public void testCreateFromIntArray_withNull() {
        DoubleMatrix m = DoubleMatrix.create((int[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreateFromIntArray_withEmpty() {
        DoubleMatrix m = DoubleMatrix.create(new int[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreateFromIntArray_withJaggedArray() {
        int[][] jagged = { { 1, 2 }, { 3 } };
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.create(jagged));
    }

    @Test
    public void testCreateFromIntArray_withNullRow() {
        int[][] nullRow = { { 1, 2 }, null };
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.create(nullRow));
    }

    @Test
    public void testCreateFromIntArray_withNullFirstRow() {
        int[][] nullFirstRow = { null, { 1, 2 } };
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.create(nullFirstRow));
    }

    @Test
    public void testCreateFromLongArray() {
        long[][] longs = { { 1L, 2L }, { 3L, 4L } };
        DoubleMatrix m = DoubleMatrix.create(longs);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.0, m.get(0, 0), DELTA);
        assertEquals(4.0, m.get(1, 1), DELTA);
    }

    @Test
    public void testCreateFromLongArray_withNull() {
        DoubleMatrix m = DoubleMatrix.create((long[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreateFromLongArray_withJaggedArray() {
        long[][] jagged = { { 1L, 2L }, { 3L } };
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.create(jagged));
    }

    @Test
    public void testCreateFromLongArray_withNullRow() {
        long[][] nullRow = { { 1L, 2L }, null };
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.create(nullRow));
    }

    @Test
    public void testCreateFromFloatArray() {
        float[][] floats = { { 1.5f, 2.5f }, { 3.5f, 4.5f } };
        DoubleMatrix m = DoubleMatrix.create(floats);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.5, m.get(0, 0), DELTA);
        assertEquals(4.5, m.get(1, 1), DELTA);
    }

    @Test
    public void testCreateFromFloatArray_withNull() {
        DoubleMatrix m = DoubleMatrix.create((float[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreateFromFloatArray_withJaggedArray() {
        float[][] jagged = { { 1.5f, 2.5f }, { 3.5f } };
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.create(jagged));
    }

    @Test
    public void testCreateFromFloatArray_withNullRow() {
        float[][] nullRow = { { 1.5f, 2.5f }, null };
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.create(nullRow));
    }

    @Test
    public void testRandom() {
        DoubleMatrix m = DoubleMatrix.random(5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        // Just verify elements exist (values are random between 0.0 and 1.0)
        for (int i = 0; i < 5; i++) {
            double val = m.get(0, i);
            assertTrue(val >= 0.0 && val < 1.0);
        }
    }

    @Test
    public void testRepeat() {
        DoubleMatrix m = DoubleMatrix.repeat(3.14, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals(3.14, m.get(0, i), DELTA);
        }
    }

    @Test
    public void testDiagonalLU2RD() {
        DoubleMatrix m = DoubleMatrix.diagonalLU2RD(new double[] { 1.5, 2.5, 3.5 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.5, m.get(0, 0), DELTA);
        assertEquals(2.5, m.get(1, 1), DELTA);
        assertEquals(3.5, m.get(2, 2), DELTA);
        assertEquals(0.0, m.get(0, 1), DELTA);
        assertEquals(0.0, m.get(1, 0), DELTA);
    }

    @Test
    public void testDiagonalRU2LD() {
        DoubleMatrix m = DoubleMatrix.diagonalRU2LD(new double[] { 1.5, 2.5, 3.5 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.5, m.get(0, 2), DELTA);
        assertEquals(2.5, m.get(1, 1), DELTA);
        assertEquals(3.5, m.get(2, 0), DELTA);
        assertEquals(0.0, m.get(0, 0), DELTA);
        assertEquals(0.0, m.get(2, 2), DELTA);
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        DoubleMatrix m = DoubleMatrix.diagonal(new double[] { 1.0, 2.0, 3.0 }, new double[] { 4.0, 5.0, 6.0 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.0, m.get(0, 0), DELTA);
        assertEquals(2.0, m.get(1, 1), DELTA);
        assertEquals(3.0, m.get(2, 2), DELTA);
        assertEquals(4.0, m.get(0, 2), DELTA);
        assertEquals(6.0, m.get(2, 0), DELTA);
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        DoubleMatrix m = DoubleMatrix.diagonal(new double[] { 1.5, 2.5, 3.5 }, null);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.5, m.get(0, 0), DELTA);
        assertEquals(2.5, m.get(1, 1), DELTA);
        assertEquals(3.5, m.get(2, 2), DELTA);
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        DoubleMatrix m = DoubleMatrix.diagonal(null, new double[] { 4.5, 5.5, 6.5 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(4.5, m.get(0, 2), DELTA);
        assertEquals(5.5, m.get(1, 1), DELTA);
        assertEquals(6.5, m.get(2, 0), DELTA);
    }

    @Test
    public void testDiagonal_withBothNull() {
        DoubleMatrix m = DoubleMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class,
            () -> DoubleMatrix.diagonal(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0, 5.0 }));
    }

    @Test
    public void testUnbox() {
        Double[][] boxed = { { 1.5, 2.5 }, { 3.5, 4.5 } };
        Matrix<Double> boxedMatrix = Matrix.of(boxed);
        DoubleMatrix unboxed = DoubleMatrix.unbox(boxedMatrix);
        assertEquals(2, unboxed.rows);
        assertEquals(2, unboxed.cols);
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

        OptionalDouble up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1.5, up.get(), DELTA);

        // Top row has no element above
        OptionalDouble empty = m.upOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });

        OptionalDouble down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3.5, down.get(), DELTA);

        // Bottom row has no element below
        OptionalDouble empty = m.downOf(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });

        OptionalDouble left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1.5, left.get(), DELTA);

        // Leftmost column has no element to the left
        OptionalDouble empty = m.leftOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });

        OptionalDouble right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2.5, right.get(), DELTA);

        // Rightmost column has no element to the right
        OptionalDouble empty = m.rightOf(0, 1);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testAdjacent4Points_centerElement() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        List<Point> points = m.adjacent4Points(1, 1).toList();
        assertEquals(4, points.size());
        assertNotNull(points.get(0)); // up
        assertNotNull(points.get(1)); // right
        assertNotNull(points.get(2)); // down
        assertNotNull(points.get(3)); // left
    }

    @Test
    public void testAdjacent4Points_cornerElement() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        List<Point> points = m.adjacent4Points(0, 0).toList();
        assertEquals(4, points.size());
        assertEquals(null, points.get(0)); // up (out of bounds)
        assertNotNull(points.get(1)); // right
        assertNotNull(points.get(2)); // down
        assertEquals(null, points.get(3)); // left (out of bounds)
    }

    @Test
    public void testAdjacent8Points_centerElement() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        List<Point> points = m.adjacent8Points(1, 1).toList();
        assertEquals(8, points.size());
        // All should be non-null for center element
        for (Point p : points) {
            assertNotNull(p);
        }
    }

    @Test
    public void testAdjacent8Points_cornerElement() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        List<Point> points = m.adjacent8Points(0, 0).toList();
        assertEquals(8, points.size());
        assertEquals(null, points.get(0)); // left-up
        assertEquals(null, points.get(1)); // up
        assertEquals(null, points.get(2)); // right-up
        assertNotNull(points.get(3)); // right
        assertNotNull(points.get(4)); // right-down
        assertNotNull(points.get(5)); // down
        assertEquals(null, points.get(6)); // left-down
        assertEquals(null, points.get(7)); // left
    }

    // ============ Row/Column Operations Tests ============

    @Test
    public void testRow() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5, 3.5 }, { 4.5, 5.5, 6.5 } });
        assertArrayEquals(new double[] { 1.5, 2.5, 3.5 }, m.row(0), DELTA);
        assertArrayEquals(new double[] { 4.5, 5.5, 6.5 }, m.row(1), DELTA);
    }

    @Test
    public void testRow_outOfBounds() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(2));
    }

    @Test
    public void testColumn() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5, 3.5 }, { 4.5, 5.5, 6.5 } });
        assertArrayEquals(new double[] { 1.5, 4.5 }, m.column(0), DELTA);
        assertArrayEquals(new double[] { 2.5, 5.5 }, m.column(1), DELTA);
        assertArrayEquals(new double[] { 3.5, 6.5 }, m.column(2), DELTA);
    }

    @Test
    public void testColumn_outOfBounds() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.setRow(0, new double[] { 10.5, 20.5 });
        assertArrayEquals(new double[] { 10.5, 20.5 }, m.row(0), DELTA);
        assertArrayEquals(new double[] { 3.0, 4.0 }, m.row(1), DELTA); // unchanged
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
        assertArrayEquals(new double[] { 10.5, 20.5 }, m.column(0), DELTA);
        assertArrayEquals(new double[] { 2.0, 4.0 }, m.column(1), DELTA); // unchanged
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
        assertArrayEquals(new double[] { 2.0, 4.0 }, m.row(0), DELTA);
        assertArrayEquals(new double[] { 3.0, 4.0 }, m.row(1), DELTA); // unchanged
    }

    @Test
    public void testUpdateColumn() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.updateColumn(0, x -> x + 10.5);
        assertArrayEquals(new double[] { 11.5, 13.5 }, m.column(0), DELTA);
        assertArrayEquals(new double[] { 2.0, 4.0 }, m.column(1), DELTA); // unchanged
    }

    // ============ Diagonal Operations Tests ============

    @Test
    public void testGetLU2RD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, m.getLU2RD(), DELTA);
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        m.setLU2RD(new double[] { 10.5, 20.5, 30.5 });
        assertEquals(10.5, m.get(0, 0), DELTA);
        assertEquals(20.5, m.get(1, 1), DELTA);
        assertEquals(30.5, m.get(2, 2), DELTA);
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new double[] { 1.0 }));
    }

    @Test
    public void testSetLU2RD_arrayTooShort() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new double[] { 1.0, 2.0 }));
    }

    @Test
    public void testUpdateLU2RD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        m.updateLU2RD(x -> x * 10.0);
        assertEquals(10.0, m.get(0, 0), DELTA);
        assertEquals(50.0, m.get(1, 1), DELTA);
        assertEquals(90.0, m.get(2, 2), DELTA);
        assertEquals(2.0, m.get(0, 1), DELTA); // unchanged
    }

    @Test
    public void testUpdateLU2RD_nonSquare() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalStateException.class, () -> m.updateLU2RD(x -> x * 2.0));
    }

    @Test
    public void testGetRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, m.getRU2LD(), DELTA);
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        m.setRU2LD(new double[] { 10.5, 20.5, 30.5 });
        assertEquals(10.5, m.get(0, 2), DELTA);
        assertEquals(20.5, m.get(1, 1), DELTA);
        assertEquals(30.5, m.get(2, 0), DELTA);
    }

    @Test
    public void testSetRU2LD_nonSquare() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalStateException.class, () -> m.setRU2LD(new double[] { 1.0 }));
    }

    @Test
    public void testSetRU2LD_arrayTooShort() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new double[] { 1.0, 2.0 }));
    }

    @Test
    public void testUpdateRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        m.updateRU2LD(x -> x * 10.0);
        assertEquals(30.0, m.get(0, 2), DELTA);
        assertEquals(50.0, m.get(1, 1), DELTA);
        assertEquals(70.0, m.get(2, 0), DELTA);
        assertEquals(2.0, m.get(0, 1), DELTA); // unchanged
    }

    @Test
    public void testUpdateRU2LD_nonSquare() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(x -> x * 2.0));
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
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                assertEquals(99.5, m.get(i, j), DELTA);
            }
        }
    }

    @Test
    public void testFill_withArray() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } });
        double[][] patch = { { 1.5, 2.5 }, { 3.5, 4.5 } };
        m.fill(patch);
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
        m.fill(1, 1, patch);
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
        assertThrows(IndexOutOfBoundsException.class, () -> m.fill(-1, 0, patch));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix copy = m.copy();
        assertEquals(m.rows, copy.rows);
        assertEquals(m.cols, copy.cols);
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
        assertEquals(2, subset.rows);
        assertEquals(3, subset.cols);
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
        assertEquals(2, submatrix.rows);
        assertEquals(2, submatrix.cols);
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
        DoubleMatrix extended = m.extend(4, 4);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals(1.0, extended.get(0, 0), DELTA);
        assertEquals(4.0, extended.get(1, 1), DELTA);
        assertEquals(0.0, extended.get(3, 3), DELTA); // new cells are 0.0
    }

    @Test
    public void testExtend_smaller() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        DoubleMatrix truncated = m.extend(2, 2);
        assertEquals(2, truncated.rows);
        assertEquals(2, truncated.cols);
        assertEquals(1.0, truncated.get(0, 0), DELTA);
        assertEquals(5.0, truncated.get(1, 1), DELTA);
    }

    @Test
    public void testExtend_withDefaultValue() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix extended = m.extend(3, 3, -1.5);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1.0, extended.get(0, 0), DELTA);
        assertEquals(-1.5, extended.get(2, 2), DELTA); // new cell
    }

    @Test
    public void testExtend_withNegativeDimensions() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 3, 0.0));
        assertThrows(IllegalArgumentException.class, () -> m.extend(3, -1, 0.0));
    }

    @Test
    public void testExtend_directional() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        DoubleMatrix extended = m.extend(1, 1, 2, 2);
        assertEquals(5, extended.rows); // 1 + 3 + 1
        assertEquals(7, extended.cols); // 2 + 3 + 2

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
        assertEquals(5, extended.rows);
        assertEquals(5, extended.cols);

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
        m.reverseH();
        assertEquals(3.0, m.get(0, 0), DELTA);
        assertEquals(2.0, m.get(0, 1), DELTA);
        assertEquals(1.0, m.get(0, 2), DELTA);
        assertEquals(6.0, m.get(1, 0), DELTA);
    }

    @Test
    public void testReverseV() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        m.reverseV();
        assertEquals(5.0, m.get(0, 0), DELTA);
        assertEquals(6.0, m.get(0, 1), DELTA);
        assertEquals(3.0, m.get(1, 0), DELTA);
        assertEquals(1.0, m.get(2, 0), DELTA);
    }

    @Test
    public void testFlipH() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        DoubleMatrix flipped = m.flipH();
        assertEquals(3.0, flipped.get(0, 0), DELTA);
        assertEquals(2.0, flipped.get(0, 1), DELTA);
        assertEquals(1.0, flipped.get(0, 2), DELTA);

        // Original unchanged
        assertEquals(1.0, m.get(0, 0), DELTA);
    }

    @Test
    public void testFlipV() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        DoubleMatrix flipped = m.flipV();
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
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3.0, rotated.get(0, 0), DELTA);
        assertEquals(1.0, rotated.get(0, 1), DELTA);
        assertEquals(4.0, rotated.get(1, 0), DELTA);
        assertEquals(2.0, rotated.get(1, 1), DELTA);
    }

    @Test
    public void testRotate180() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix rotated = m.rotate180();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(4.0, rotated.get(0, 0), DELTA);
        assertEquals(3.0, rotated.get(0, 1), DELTA);
        assertEquals(2.0, rotated.get(1, 0), DELTA);
        assertEquals(1.0, rotated.get(1, 1), DELTA);
    }

    @Test
    public void testRotate270() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
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
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
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
        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
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
        assertEquals(1, reshaped.rows);
        assertEquals(9, reshaped.cols);
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
        assertEquals(2, reshaped.rows);
        assertEquals(3, reshaped.cols);
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 } });
        DoubleMatrix repeated = m.repelem(2, 3);
        assertEquals(2, repeated.rows);
        assertEquals(6, repeated.cols);

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
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5 }, { 3.5, 4.5 } });
        DoubleMatrix repeated = m.repmat(2, 3);
        assertEquals(4, repeated.rows);
        assertEquals(6, repeated.cols);

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
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
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
        m.flatOp(row -> {
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
        DoubleMatrix stacked = m1.vstack(m2);

        assertEquals(4, stacked.rows);
        assertEquals(3, stacked.cols);
        assertEquals(1.0, stacked.get(0, 0), DELTA);
        assertEquals(7.0, stacked.get(2, 0), DELTA);
        assertEquals(12.0, stacked.get(3, 2), DELTA);
    }

    @Test
    public void testVstack_differentColumnCounts() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
        DoubleMatrix stacked = m1.hstack(m2);

        assertEquals(2, stacked.rows);
        assertEquals(4, stacked.cols);
        assertEquals(1.0, stacked.get(0, 0), DELTA);
        assertEquals(5.0, stacked.get(0, 2), DELTA);
        assertEquals(8.0, stacked.get(1, 3), DELTA);
    }

    @Test
    public void testHstack_differentRowCounts() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
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

        assertEquals(1, product.rows);
        assertEquals(1, product.cols);
        assertEquals(32.0, product.get(0, 0), DELTA); // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
    }

    // ============ Conversion Tests ============

    @Test
    public void testBoxed() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.5, 2.5, 3.5 }, { 4.5, 5.5, 6.5 } });
        Matrix<Double> boxed = m.boxed();
        assertEquals(2, boxed.rows);
        assertEquals(3, boxed.cols);
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
        double[] diagonal = m.streamLU2RD().toArray();
        assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, diagonal, DELTA);
    }

    @Test
    public void testStreamLU2RD_empty() {
        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.streamLU2RD().toArray().length);
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        DoubleMatrix nonSquare = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        double[] antiDiagonal = m.streamRU2LD().toArray();
        assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, antiDiagonal, DELTA);
    }

    @Test
    public void testStreamRU2LD_empty() {
        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.streamRU2LD().toArray().length);
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        DoubleMatrix nonSquare = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamRU2LD());
    }

    @Test
    public void testStreamH() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        double[] all = m.streamH().toArray();
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }, all, DELTA);
    }

    @Test
    public void testStreamH_empty() {
        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.streamH().toArray().length);
    }

    @Test
    public void testStreamH_withRow() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        double[] row1 = m.streamH(1).toArray();
        assertArrayEquals(new double[] { 4.0, 5.0, 6.0 }, row1, DELTA);
    }

    @Test
    public void testStreamH_withRow_outOfBounds() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2));
    }

    @Test
    public void testStreamH_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        double[] rows = m.streamH(1, 3).toArray();
        assertArrayEquals(new double[] { 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 }, rows, DELTA);
    }

    @Test
    public void testStreamH_withRange_outOfBounds() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2, 1));
    }

    @Test
    public void testStreamV() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        double[] all = m.streamV().toArray();
        assertArrayEquals(new double[] { 1.0, 4.0, 2.0, 5.0, 3.0, 6.0 }, all, DELTA);
    }

    @Test
    public void testStreamV_empty() {
        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.streamV().toArray().length);
    }

    @Test
    public void testStreamV_withColumn() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        double[] col1 = m.streamV(1).toArray();
        assertArrayEquals(new double[] { 2.0, 5.0 }, col1, DELTA);
    }

    @Test
    public void testStreamV_withColumn_outOfBounds() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2));
    }

    @Test
    public void testStreamV_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        double[] cols = m.streamV(1, 3).toArray();
        assertArrayEquals(new double[] { 2.0, 5.0, 8.0, 3.0, 6.0, 9.0 }, cols, DELTA);
    }

    @Test
    public void testStreamV_withRange_outOfBounds() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2, 1));
    }

    @Test
    public void testStreamR() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        List<double[]> rows = m.streamR().map(DoubleStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, rows.get(0), DELTA);
        assertArrayEquals(new double[] { 4.0, 5.0, 6.0 }, rows.get(1), DELTA);
    }

    @Test
    public void testStreamR_empty() {
        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.streamR().count());
    }

    @Test
    public void testStreamR_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        List<double[]> rows = m.streamR(1, 3).map(DoubleStream::toArray).toList();
        assertEquals(2, rows.size());
        assertArrayEquals(new double[] { 4.0, 5.0, 6.0 }, rows.get(0), DELTA);
        assertArrayEquals(new double[] { 7.0, 8.0, 9.0 }, rows.get(1), DELTA);
    }

    @Test
    public void testStreamR_withRange_outOfBounds() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(0, 3));
    }

    @Test
    public void testStreamC() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        List<double[]> cols = m.streamC().map(DoubleStream::toArray).toList();
        assertEquals(3, cols.size());
        assertArrayEquals(new double[] { 1.0, 4.0 }, cols.get(0), DELTA);
        assertArrayEquals(new double[] { 2.0, 5.0 }, cols.get(1), DELTA);
        assertArrayEquals(new double[] { 3.0, 6.0 }, cols.get(2), DELTA);
    }

    @Test
    public void testStreamC_empty() {
        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.streamC().count());
    }

    @Test
    public void testStreamC_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        List<double[]> cols = m.streamC(1, 3).map(DoubleStream::toArray).toList();
        assertEquals(2, cols.size());
        assertArrayEquals(new double[] { 2.0, 5.0, 8.0 }, cols.get(0), DELTA);
        assertArrayEquals(new double[] { 3.0, 6.0, 9.0 }, cols.get(1), DELTA);
    }

    @Test
    public void testStreamC_withRange_outOfBounds() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(0, 3));
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
        // Just ensure it doesn't throw
        m.println();

        DoubleMatrix empty = DoubleMatrix.empty();
        empty.println();
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
        double totalSum = m.streamH().sum();
        assertEquals(45.0, totalSum, DELTA); // 1+2+3+4+5+6+7+8+9 = 45

        // Test sum of specific row
        double row1Sum = m.streamH(1).sum();
        assertEquals(15.0, row1Sum, DELTA); // 4+5+6 = 15

        // Test sum of specific column
        double col0Sum = m.streamV(0).sum();
        assertEquals(12.0, col0Sum, DELTA); // 1+4+7 = 12

        // Test min/max
        double min = m.streamH().min().orElse(0.0);
        assertEquals(1.0, min, DELTA);

        double max = m.streamH().max().orElse(0.0);
        assertEquals(9.0, max, DELTA);

        // Test average
        double avg = m.streamH().average().orElse(0.0);
        assertEquals(5.0, avg, DELTA);

        // Test diagonal operations
        double diagonalSum = m.streamLU2RD().sum();
        assertEquals(15.0, diagonalSum, DELTA); // 1+5+9 = 15

        double antiDiagonalSum = m.streamRU2LD().sum();
        assertEquals(15.0, antiDiagonalSum, DELTA); // 3+5+7 = 15
    }

    @Test
    public void testRowColumnStatistics() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });

        // Test statistics on individual rows
        List<Double> rowSums = m.streamR().map(row -> row.sum()).toList();
        assertEquals(3, rowSums.size());
        assertEquals(6.0, rowSums.get(0), DELTA); // 1+2+3
        assertEquals(15.0, rowSums.get(1), DELTA); // 4+5+6
        assertEquals(24.0, rowSums.get(2), DELTA); // 7+8+9

        // Test statistics on individual columns
        List<Double> colSums = m.streamC().map(col -> col.sum()).toList();
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
        assertEquals(0, empty.copy().rows);
        assertEquals(empty, empty.transpose());
        assertEquals(empty, empty.rotate90());

        DoubleMatrix extended = empty.extend(2, 2, 5.5);
        assertEquals(2, extended.rows);
        assertEquals(2, extended.cols);
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
        double totalSum = largeSum.streamH().sum();
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
        DoubleMatrix m = DoubleMatrix.of(new double[][] {
            { Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY },
            { 3.0, 4.0 }
        });

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
        DoubleMatrix m = DoubleMatrix.of(new double[][] {
            { Double.MAX_VALUE, Double.MIN_VALUE },
            { Double.MIN_NORMAL, 1e-308 }
        });

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
        DoubleMatrix m = DoubleMatrix.of(new double[][] {
            { 0.1 + 0.2, 0.3 },
            { 1.0 / 3.0, 0.333333 }
        });

        // Due to floating point precision, 0.1 + 0.2 != 0.3 exactly
        assertNotEquals(0.3, m.get(0, 0), 1e-15);
        assertEquals(0.3, m.get(0, 0), 1e-10); // But close enough with tolerance

        assertEquals(0.3, m.get(0, 1), DELTA);
    }

    @Test
    public void testConversionAccuracy_FromInt() {
        // Test that large int values convert accurately to double
        int[][] ints = { { Integer.MAX_VALUE, Integer.MIN_VALUE }, { 1000000, -1000000 } };
        DoubleMatrix m = DoubleMatrix.create(ints);

        assertEquals(Integer.MAX_VALUE, m.get(0, 0), DELTA);
        assertEquals(Integer.MIN_VALUE, m.get(0, 1), DELTA);
        assertEquals(1000000.0, m.get(1, 0), DELTA);
        assertEquals(-1000000.0, m.get(1, 1), DELTA);
    }

    @Test
    public void testConversionAccuracy_FromLong() {
        // Test that long values convert to double (may lose precision for very large longs)
        long[][] longs = { { Long.MAX_VALUE, Long.MIN_VALUE }, { 1000000000000L, -1000000000000L } };
        DoubleMatrix m = DoubleMatrix.create(longs);

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
        DoubleMatrix m = DoubleMatrix.create(floats);

        assertEquals(1.5, m.get(0, 0), DELTA);
        assertEquals(2.5, m.get(0, 1), DELTA);
        assertEquals(Float.MAX_VALUE, m.get(1, 0), 1e30); // Large tolerance for float max
        assertEquals(Float.MIN_VALUE, m.get(1, 1), 1e-40);
    }
}
