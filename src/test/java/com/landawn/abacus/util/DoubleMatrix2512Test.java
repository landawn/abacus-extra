package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.OptionalDouble;

@Tag("2512")
public class DoubleMatrix2512Test extends TestBase {

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
    public void test_diagonalLU2RD() {
        double[] diag = { 1.0, 2.0, 3.0 };
        DoubleMatrix m = DoubleMatrix.diagonalLU2RD(diag);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0, m.get(0, 0), 0.0);
        assertEquals(2.0, m.get(1, 1), 0.0);
        assertEquals(3.0, m.get(2, 2), 0.0);
        assertEquals(0.0, m.get(0, 1), 0.0);
        assertEquals(0.0, m.get(1, 0), 0.0);
    }

    @Test
    public void test_diagonalLU2RD_null() {
        DoubleMatrix m = DoubleMatrix.diagonalLU2RD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_diagonalRU2LD() {
        double[] diag = { 1.0, 2.0, 3.0 };
        DoubleMatrix m = DoubleMatrix.diagonalRU2LD(diag);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0, m.get(0, 2), 0.0);
        assertEquals(2.0, m.get(1, 1), 0.0);
        assertEquals(3.0, m.get(2, 0), 0.0);
        assertEquals(0.0, m.get(0, 0), 0.0);
    }

    @Test
    public void test_diagonalRU2LD_null() {
        DoubleMatrix m = DoubleMatrix.diagonalRU2LD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_diagonal_both() {
        double[] lu2rd = { 1.0, 2.0, 3.0 };
        double[] ru2ld = { 4.0, 5.0, 6.0 };
        DoubleMatrix m = DoubleMatrix.diagonal(lu2rd, ru2ld);
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
        assertThrows(IllegalArgumentException.class, () -> DoubleMatrix.diagonal(lu2rd, ru2ld));
    }

    @Test
    public void test_diagonal_bothNull() {
        DoubleMatrix m = DoubleMatrix.diagonal(null, null);
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
        OptionalDouble up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1.0, up.get(), 0.0);
    }

    @Test
    public void test_upOf_firstRow() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void test_downOf() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3.0, down.get(), 0.0);
    }

    @Test
    public void test_downOf_lastRow() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void test_leftOf() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1.0, left.get(), 0.0);
    }

    @Test
    public void test_leftOf_firstColumn() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void test_rightOf() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2.0, right.get(), 0.0);
    }

    @Test
    public void test_rightOf_lastColumn() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        OptionalDouble right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    // ============ Row and Column Access Tests ============

    @Test
    public void test_row() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        double[] row = m.row(0);
        assertArrayEquals(new double[] { 1.0, 2.0 }, row, 0.0);
    }

    @Test
    public void test_row_invalidIndex() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(5));
    }

    @Test
    public void test_column() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        double[] col = m.column(0);
        assertArrayEquals(new double[] { 1.0, 3.0 }, col, 0.0);
    }

    @Test
    public void test_column_invalidIndex() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(5));
    }

    @Test
    public void test_setRow() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.setRow(0, new double[] { 9.0, 8.0 });
        assertArrayEquals(new double[] { 9.0, 8.0 }, m.row(0), 0.0);
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
        assertArrayEquals(new double[] { 9.0, 8.0 }, m.column(0), 0.0);
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
        assertArrayEquals(new double[] { 2.0, 4.0 }, m.row(0), 0.0);
        assertArrayEquals(new double[] { 3.0, 4.0 }, m.row(1), 0.0);
    }

    @Test
    public void test_updateColumn() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.updateColumn(0, x -> x * 2);
        assertArrayEquals(new double[] { 2.0, 6.0 }, m.column(0), 0.0);
        assertArrayEquals(new double[] { 2.0, 4.0 }, m.column(1), 0.0);
    }

    // ============ Diagonal Get/Set Tests ============

    @Test
    public void test_getLU2RD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        double[] diag = m.getLU2RD();
        assertArrayEquals(new double[] { 1.0, 5.0, 9.0 }, diag, 0.0);
    }

    @Test
    public void test_getLU2RD_nonSquare() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void test_setLU2RD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.setLU2RD(new double[] { 9.0, 8.0 });
        assertArrayEquals(new double[] { 9.0, 8.0 }, m.getLU2RD(), 0.0);
    }

    @Test
    public void test_setLU2RD_nonSquare() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new double[] { 9.0 }));
    }

    @Test
    public void test_setLU2RD_invalidLength() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new double[] { 9.0 }));
    }

    @Test
    public void test_updateLU2RD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.updateLU2RD(x -> x * 2);
        assertArrayEquals(new double[] { 2.0, 8.0 }, m.getLU2RD(), 0.0);
    }

    @Test
    public void test_getRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        double[] diag = m.getRU2LD();
        assertArrayEquals(new double[] { 3.0, 5.0, 7.0 }, diag, 0.0);
    }

    @Test
    public void test_setRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.setRU2LD(new double[] { 9.0, 8.0 });
        assertArrayEquals(new double[] { 9.0, 8.0 }, m.getRU2LD(), 0.0);
    }

    @Test
    public void test_updateRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        m.updateRU2LD(x -> x * 2);
        double[] diag = m.getRU2LD();
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
        m.fill(new double[][] { { 9.0, 8.0 }, { 7.0, 6.0 } });
        assertEquals(9.0, m.get(0, 0), 0.0);
        assertEquals(8.0, m.get(0, 1), 0.0);
        assertEquals(7.0, m.get(1, 0), 0.0);
        assertEquals(6.0, m.get(1, 1), 0.0);
    }

    @Test
    public void test_fill_withOffset() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } });
        m.fill(1, 1, new double[][] { { 99.0 } });
        assertEquals(1.0, m.get(0, 0), 0.0);
        assertEquals(99.0, m.get(1, 1), 0.0);
        assertEquals(9.0, m.get(2, 2), 0.0);
    }

    @Test
    public void test_fill_withOffset_clipsToFit() {
        // fill method clips data to fit within matrix bounds, doesn't throw exception
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        m.fill(0, 0, new double[][] { { 9.0, 8.0, 7.0 } }); // Source has 3 elements but matrix only has 2 columns
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
        DoubleMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1.0, extended.get(0, 0), 0.0);
        assertEquals(0.0, extended.get(2, 2), 0.0);
    }

    @Test
    public void test_extend_withDefaultValue() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        DoubleMatrix extended = m.extend(2, 3, 99.0);
        assertEquals(2, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1.0, extended.get(0, 0), 0.0);
        assertEquals(99.0, extended.get(1, 1), 0.0);
        assertEquals(99.0, extended.get(0, 2), 0.0);
    }

    @Test
    public void test_extend_smaller() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        DoubleMatrix result = m.extend(1, 2);
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
    public void test_reverseH() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        m.reverseH();
        assertEquals(3.0, m.get(0, 0), 0.0);
        assertEquals(2.0, m.get(0, 1), 0.0);
        assertEquals(1.0, m.get(0, 2), 0.0);
    }

    @Test
    public void test_reverseV() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        m.reverseV();
        assertEquals(5.0, m.get(0, 0), 0.0);
        assertEquals(3.0, m.get(1, 0), 0.0);
        assertEquals(1.0, m.get(2, 0), 0.0);
    }

    @Test
    public void test_flipH() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        DoubleMatrix flipped = m.flipH();
        assertEquals(3.0, flipped.get(0, 0), 0.0);
        assertEquals(2.0, flipped.get(0, 1), 0.0);
        assertEquals(1.0, flipped.get(0, 2), 0.0);
        // Original unchanged
        assertEquals(1.0, m.get(0, 0), 0.0);
    }

    @Test
    public void test_flipV() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        DoubleMatrix flipped = m.flipV();
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
    public void test_repelem() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        DoubleMatrix result = m.repelem(2, 2);
        assertEquals(4, result.rowCount());
        assertEquals(4, result.columnCount());
        assertEquals(1.0, result.get(0, 0), 0.0);
        assertEquals(1.0, result.get(0, 1), 0.0);
        assertEquals(1.0, result.get(1, 0), 0.0);
        assertEquals(1.0, result.get(1, 1), 0.0);
        assertEquals(2.0, result.get(0, 2), 0.0);
    }

    @Test
    public void test_repelem_invalidRepeats() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
    }

    // ============ Repmat Test ============

    @Test
    public void test_repmat() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        DoubleMatrix result = m.repmat(2, 2);
        assertEquals(2, result.rowCount());
        assertEquals(4, result.columnCount());
        assertEquals(1.0, result.get(0, 0), 0.0);
        assertEquals(2.0, result.get(0, 1), 0.0);
        assertEquals(1.0, result.get(0, 2), 0.0);
        assertEquals(2.0, result.get(0, 3), 0.0);
    }

    @Test
    public void test_repmat_invalidRepeats() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
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
    public void test_flatOp() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        AtomicInteger count = new AtomicInteger(0);
        m.flatOp(row -> count.addAndGet(row.length));
        assertEquals(4, count.get());
    }

    // ============ Vstack and Hstack Tests ============

    @Test
    public void test_vstack() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0, 4.0 } });
        DoubleMatrix result = m1.vstack(m2);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(1.0, result.get(0, 0), 0.0);
        assertEquals(3.0, result.get(1, 0), 0.0);
    }

    @Test
    public void test_vstack_incompatibleCols() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0, 2.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void test_hstack() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0 }, { 2.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0 }, { 4.0 } });
        DoubleMatrix result = m1.hstack(m2);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(1.0, result.get(0, 0), 0.0);
        assertEquals(3.0, result.get(0, 1), 0.0);
    }

    @Test
    public void test_hstack_incompatibleRows() {
        DoubleMatrix m1 = DoubleMatrix.of(new double[][] { { 1.0 } });
        DoubleMatrix m2 = DoubleMatrix.of(new double[][] { { 3.0 }, { 4.0 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
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
    public void test_streamH() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        double sum = m.streamH().sum();
        assertEquals(10.0, sum, 0.0);
    }

    @Test
    public void test_streamH_byRowIndex() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        double sum = m.streamH(0).sum();
        assertEquals(3.0, sum, 0.0);
    }

    @Test
    public void test_streamH_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        double sum = m.streamH(1, 3).sum();
        assertEquals(18.0, sum, 0.0);
    }

    @Test
    public void test_streamV() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        double sum = m.streamV().sum();
        assertEquals(10.0, sum, 0.0);
    }

    @Test
    public void test_streamV_byColumnIndex() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        double sum = m.streamV(0).sum();
        assertEquals(4.0, sum, 0.0);
    }

    @Test
    public void test_streamV_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } });
        double sum = m.streamV(1, 3).sum();
        assertEquals(16.0, sum, 0.0);
    }

    @Test
    public void test_streamLU2RD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        double sum = m.streamLU2RD().sum();
        assertEquals(5.0, sum, 0.0);
    }

    @Test
    public void test_streamRU2LD() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        double sum = m.streamRU2LD().sum();
        assertEquals(5.0, sum, 0.0);
    }

    @Test
    public void test_streamR() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        long count = m.streamR().count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamR_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } });
        long count = m.streamR(1, 3).count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamC() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } });
        long count = m.streamC().count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamC_withRange() {
        DoubleMatrix m = DoubleMatrix.of(new double[][] { { 1.0, 2.0, 3.0 } });
        long count = m.streamC(1, 3).count();
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
