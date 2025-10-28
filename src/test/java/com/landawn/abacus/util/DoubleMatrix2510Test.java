package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalDouble;
import com.landawn.abacus.util.stream.DoubleStream;
import com.landawn.abacus.util.stream.Stream;

@Tag("2510")
public class DoubleMatrix2510Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix m = new DoubleMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.0, m.get(0, 0));
        assertEquals(4.0, m.get(1, 1));
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
        DoubleMatrix m = new DoubleMatrix(new double[][] { { 42.5 } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(42.5, m.get(0, 0));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testEmpty() {
        DoubleMatrix empty = DoubleMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());
        assertSame(DoubleMatrix.empty(), DoubleMatrix.empty());
    }

    @Test
    public void testOf_withValidArray() {
        double[][] arr = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        DoubleMatrix m = DoubleMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
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
        DoubleMatrix m = DoubleMatrix.create(ints);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.0, m.get(0, 0));
        assertEquals(4.0, m.get(1, 1));
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
        assertEquals(1.0, m.get(0, 0));
        assertEquals(4.0, m.get(1, 1));
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
        float[][] floats = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        DoubleMatrix m = DoubleMatrix.create(floats);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.0, m.get(0, 0), 0.001);
        assertEquals(4.0, m.get(1, 1), 0.001);
    }

    @Test
    public void testCreateFromFloatArray_withNull() {
        DoubleMatrix m = DoubleMatrix.create((float[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testCreateFromFloatArray_withJaggedArray() {
        float[][] jagged = { { 1.0f, 2.0f }, { 3.0f } };
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.create(jagged));
    }

    @Test
    public void testCreateFromFloatArray_withNullRow() {
        float[][] nullRow = { { 1.0f, 2.0f }, null };
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.create(nullRow));
    }

    @Test
    public void testRandom() {
        DoubleMatrix m = DoubleMatrix.random(5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertNotNull(m.get(0, i));
        }
    }

    @Test
    public void testRandom_withZeroLength() {
        DoubleMatrix m = DoubleMatrix.random(0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void testRepeat() {
        DoubleMatrix m = DoubleMatrix.repeat(3.14, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals(3.14, m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withZeroLength() {
        DoubleMatrix m = DoubleMatrix.repeat(1.0, 0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void testDiagonalLU2RD() {
        DoubleMatrix m = DoubleMatrix.diagonalLU2RD(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.0, m.get(0, 0));
        assertEquals(2.0, m.get(1, 1));
        assertEquals(3.0, m.get(2, 2));
        assertEquals(0.0, m.get(0, 1));
        assertEquals(0.0, m.get(1, 0));
    }

    @Test
    public void testDiagonalLU2RD_withNull() {
        DoubleMatrix m = DoubleMatrix.diagonalLU2RD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonalRU2LD() {
        DoubleMatrix m = DoubleMatrix.diagonalRU2LD(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.0, m.get(0, 2));
        assertEquals(2.0, m.get(1, 1));
        assertEquals(3.0, m.get(2, 0));
        assertEquals(0.0, m.get(0, 0));
        assertEquals(0.0, m.get(2, 2));
    }

    @Test
    public void testDiagonalRU2LD_withNull() {
        DoubleMatrix m = DoubleMatrix.diagonalRU2LD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        DoubleMatrix m = DoubleMatrix.diagonal(new double[] { 1.0, 4.0 }, new double[] { 2.0, 3.0 });
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.0, m.get(0, 0));
        assertEquals(2.0, m.get(0, 1));
        assertEquals(3.0, m.get(1, 0));
        assertEquals(4.0, m.get(1, 1));
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        DoubleMatrix m = DoubleMatrix.diagonal(new double[] { 1.0, 2.0, 3.0 }, null);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.0, m.get(0, 0));
        assertEquals(2.0, m.get(1, 1));
        assertEquals(3.0, m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        DoubleMatrix m = DoubleMatrix.diagonal(null, new double[] { 1.0, 2.0, 3.0 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1.0, m.get(0, 2));
        assertEquals(2.0, m.get(1, 1));
        assertEquals(3.0, m.get(2, 0));
    }

    @Test
    public void testDiagonal_withBothEmpty() {
        DoubleMatrix m = DoubleMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.diagonal(new double[] { 1.0, 2.0 }, new double[] { 1.0, 2.0, 3.0 }));
    }

    @Test
    public void testUnbox() {
        Matrix<Double> boxed = Matrix.of(new Double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix m = DoubleMatrix.unbox(boxed);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1.0, m.get(0, 0));
        assertEquals(4.0, m.get(1, 1));
    }

    @Test
    public void testUnbox_withNullValues() {
        Matrix<Double> boxed = Matrix.of(new Double[][] { { 1.0, null }, { null, 4.0 } });
        DoubleMatrix m = DoubleMatrix.unbox(boxed);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
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
        OptionalDouble up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1.0, up.get());
    }

    @Test
    public void testUpOf_firstRow() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void testDownOf() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3.0, down.get());
    }

    @Test
    public void testDownOf_lastRow() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void testLeftOf() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1.0, left.get());
    }

    @Test
    public void testLeftOf_firstColumn() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void testRightOf() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2.0, right.get());
    }

    @Test
    public void testRightOf_lastColumn() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    @Test
    public void testAdjacent4Points() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        Stream<Point> adjacent = m.adjacent4Points(1, 1);
        assertEquals(4, adjacent.count());
    }

    @Test
    public void testAdjacent4Points_corner() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<Point> adjacent = m.adjacent4Points(0, 0);
        assertEquals(2, adjacent.count());
    }

    @Test
    public void testAdjacent8Points() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        Stream<Point> adjacent = m.adjacent8Points(1, 1);
        assertEquals(8, adjacent.count());
    }

    @Test
    public void testAdjacent8Points_corner() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<Point> adjacent = m.adjacent8Points(0, 0);
        assertEquals(3, adjacent.count());
    }

    // ============ Row/Column Access Tests ============

    @Test
    public void testRow() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        double[] row = m.row(0);
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, row);
    }

    @Test
    public void testRow_invalidIndex() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(2));
    }

    @Test
    public void testColumn() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        double[] col = m.column(0);
        assertArrayEquals(new double[] { 1.0, 4.0 }, col);
    }

    @Test
    public void testColumn_invalidIndex() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
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
        double[] diagonal = m.getLU2RD();
        assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, diagonal);
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.setLU2RD(new double[] { 9.0, 8.0 });
        assertEquals(9.0, m.get(0, 0));
        assertEquals(8.0, m.get(1, 1));
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new double[] { 9.0, 8.0 }));
    }

    @Test
    public void testSetLU2RD_tooShort() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new double[] { 9.0 }));
    }

    @Test
    public void testUpdateLU2RD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.updateLU2RD(x -> x * 2);
        assertEquals(2.0, m.get(0, 0));
        assertEquals(8.0, m.get(1, 1));
    }

    @Test
    public void testGetRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        double[] diagonal = m.getRU2LD();
        assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, diagonal);
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.setRU2LD(new double[] { 9.0, 8.0 });
        assertEquals(9.0, m.get(0, 1));
        assertEquals(8.0, m.get(1, 0));
    }

    @Test
    public void testUpdateRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.updateRU2LD(x -> x + 10);
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
        m.fill(new double[][] { { 7.0, 8.0 }, { 9.0, 10.0 } });
        assertEquals(7.0, m.get(0, 0));
        assertEquals(8.0, m.get(0, 1));
        assertEquals(9.0, m.get(1, 0));
        assertEquals(10.0, m.get(1, 1));
    }

    @Test
    public void testFill_withPosition() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        m.fill(1, 1, new double[][] { { 11.0, 12.0 }, { 13.0, 14.0 } });
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
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(3.0, copy.get(0, 0));
        assertEquals(6.0, copy.get(1, 1));
    }

    @Test
    public void testCopy_subMatrix() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        DoubleMatrix copy = m.copy(1, 3, 1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(5.0, copy.get(0, 0));
        assertEquals(9.0, copy.get(1, 1));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_simple() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1.0, extended.get(0, 0));
        assertEquals(4.0, extended.get(1, 1));
        assertEquals(0.0, extended.get(2, 2));
    }

    @Test
    public void testExtend_withDefaultValue() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix extended = m.extend(3, 3, 9.0);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(9.0, extended.get(2, 2));
    }

    @Test
    public void testExtend_directions() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 5.0 } });
        DoubleMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(5.0, extended.get(1, 1));
        assertEquals(0.0, extended.get(0, 0));
    }

    @Test
    public void testExtend_directionsWithDefault() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 5.0 } });
        DoubleMatrix extended = m.extend(1, 1, 1, 1, 9.0);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(5.0, extended.get(1, 1));
        assertEquals(9.0, extended.get(0, 0));
    }

    // ============ Flip/Reverse Tests ============

    @Test
    public void testReverseH() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.reverseH();
        assertEquals(2.0, m.get(0, 0));
        assertEquals(1.0, m.get(0, 1));
        assertEquals(4.0, m.get(1, 0));
        assertEquals(3.0, m.get(1, 1));
    }

    @Test
    public void testReverseV() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.reverseV();
        assertEquals(3.0, m.get(0, 0));
        assertEquals(4.0, m.get(0, 1));
        assertEquals(1.0, m.get(1, 0));
        assertEquals(2.0, m.get(1, 1));
    }

    @Test
    public void testFlipH() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix flipped = m.flipH();
        assertEquals(2.0, flipped.get(0, 0));
        assertEquals(1.0, flipped.get(0, 1));
        assertEquals(1.0, m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix flipped = m.flipV();
        assertEquals(3.0, flipped.get(0, 0));
        assertEquals(4.0, flipped.get(0, 1));
        assertEquals(1.0, m.get(0, 0));
    }

    // ============ Rotation Tests ============

    @Test
    public void testRotate90() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
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
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
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
        assertEquals(2, reshaped.rows);
        assertEquals(3, reshaped.cols);
        assertEquals(1.0, reshaped.get(0, 0));
        assertEquals(4.0, reshaped.get(1, 0));
    }

    @Test
    public void testReshape_invalidSize() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.reshape(2, 3));
    }

    // ============ Repelem Tests ============

    @Test
    public void testRepelem() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix result = m.repelem(2, 2);
        assertEquals(4, result.rows);
        assertEquals(4, result.cols);
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
        DoubleMatrix result = m.repmat(2, 2);
        assertEquals(4, result.rows);
        assertEquals(4, result.cols);
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
        m.flatOp(row -> {
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
        DoubleMatrix stacked = m1.vstack(m2);
        assertEquals(4, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals(1.0, stacked.get(0, 0));
        assertEquals(5.0, stacked.get(2, 0));
    }

    @Test
    public void testVstack_incompatibleColumns() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } });
        DoubleMatrix stacked = m1.hstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(4, stacked.cols);
        assertEquals(1.0, stacked.get(0, 0));
        assertEquals(5.0, stacked.get(0, 2));
    }

    @Test
    public void testHstack_incompatibleRows() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 5.0 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
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
        DoubleStream stream = m.streamLU2RD();
        double[] result = stream.toArray();
        assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, result);
    }

    @Test
    public void testStreamRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        DoubleStream stream = m.streamRU2LD();
        double[] result = stream.toArray();
        assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, result);
    }

    @Test
    public void testStreamH() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleStream stream = m.streamH();
        double[] result = stream.toArray();
        assertArrayEquals(new double[] { 1.0, 2.0, 3.0, 4.0 }, result);
    }

    @Test
    public void testStreamH_withRow() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleStream stream = m.streamH(1);
        double[] result = stream.toArray();
        assertArrayEquals(new double[] { 3.0, 4.0 }, result);
    }

    @Test
    public void testStreamH_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        DoubleStream stream = m.streamH(1, 3);
        double[] result = stream.toArray();
        assertArrayEquals(new double[] { 3.0, 4.0, 5.0, 6.0 }, result);
    }

    @Test
    public void testStreamV() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleStream stream = m.streamV();
        double[] result = stream.toArray();
        assertArrayEquals(new double[] { 1.0, 3.0, 2.0, 4.0 }, result);
    }

    @Test
    public void testStreamV_withColumn() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleStream stream = m.streamV(0);
        double[] result = stream.toArray();
        assertArrayEquals(new double[] { 1.0, 3.0 }, result);
    }

    @Test
    public void testStreamV_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        DoubleStream stream = m.streamV(1, 3);
        double[] result = stream.toArray();
        assertArrayEquals(new double[] { 2.0, 5.0, 3.0, 6.0 }, result);
    }

    @Test
    public void testStreamR() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<DoubleStream> stream = m.streamR();
        assertEquals(2, stream.count());
    }

    @Test
    public void testStreamR_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        Stream<DoubleStream> stream = m.streamR(1, 3);
        assertEquals(2, stream.count());
    }

    @Test
    public void testStreamC() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<DoubleStream> stream = m.streamC();
        assertEquals(2, stream.count());
    }

    @Test
    public void testStreamC_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        Stream<DoubleStream> stream = m.streamC(1, 3);
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
        double[][] array = m.array();
        assertArrayEquals(new double[] { 1.0, 2.0 }, array[0]);
        assertArrayEquals(new double[] { 3.0, 4.0 }, array[1]);
    }

    @Test
    public void testReshape_singleParam() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 } });
        DoubleMatrix reshaped = m.reshape(3);
        assertEquals(2, reshaped.rows);
        assertEquals(3, reshaped.cols);
    }

    @Test
    public void testPointsLU2RD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<Point> points = m.pointsLU2RD();
        assertEquals(2, points.count());
    }

    @Test
    public void testPointsRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<Point> points = m.pointsRU2LD();
        assertEquals(2, points.count());
    }

    @Test
    public void testPointsH() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<Point> points = m.pointsH();
        assertEquals(4, points.count());
    }

    @Test
    public void testPointsH_withRow() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<Point> points = m.pointsH(1);
        assertEquals(2, points.count());
    }

    @Test
    public void testPointsH_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        Stream<Point> points = m.pointsH(1, 3);
        assertEquals(4, points.count());
    }

    @Test
    public void testPointsV() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<Point> points = m.pointsV();
        assertEquals(4, points.count());
    }

    @Test
    public void testPointsV_withColumn() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<Point> points = m.pointsV(1);
        assertEquals(2, points.count());
    }

    @Test
    public void testPointsV_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        Stream<Point> points = m.pointsV(1, 3);
        assertEquals(4, points.count());
    }

    @Test
    public void testPointsR() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<Stream<Point>> points = m.pointsR();
        assertEquals(2, points.count());
    }

    @Test
    public void testPointsC() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        Stream<Stream<Point>> points = m.pointsC();
        assertEquals(2, points.count());
    }

    @Test
    public void testForEach_biConsumer() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        final int[] count = { 0 };
        m.forEach((i, j) -> count[0]++);
        assertEquals(4, count[0]);
    }

    @Test
    public void testForEach_biObjConsumer() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        final int[] count = { 0 };
        m.forEach((i, j, matrix) -> count[0]++);
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
        int result = m.apply(matrix -> matrix.rows * matrix.cols);
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
