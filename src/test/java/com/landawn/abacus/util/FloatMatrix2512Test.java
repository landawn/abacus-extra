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
import com.landawn.abacus.util.u.OptionalFloat;

@Tag("2512")
public class FloatMatrix2512Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void test_constructor_withValidArray() {
        float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        FloatMatrix m = new FloatMatrix(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0), 0.0f);
        assertEquals(4.0f, m.get(1, 1), 0.0f);
    }

    @Test
    public void test_constructor_withNullArray() {
        FloatMatrix m = new FloatMatrix(null);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_withEmptyArray() {
        FloatMatrix m = new FloatMatrix(new float[0][0]);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_withSingleElement() {
        FloatMatrix m = new FloatMatrix(new float[][] { { 42.5f } });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(42.5f, m.get(0, 0), 0.0f);
    }

    // ============ Factory Method Tests ============

    @Test
    public void test_empty() {
        FloatMatrix empty = FloatMatrix.empty();
        assertEquals(0, empty.rowCount());
        assertEquals(0, empty.columnCount());
        assertTrue(empty.isEmpty());
        assertSame(FloatMatrix.empty(), FloatMatrix.empty());
    }

    @Test
    public void test_of_withValidArray() {
        float[][] arr = { { 1.0f, 2.0f }, { 3.0f, 4.0f } };
        FloatMatrix m = FloatMatrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0), 0.0f);
    }

    @Test
    public void test_of_withNullArray() {
        FloatMatrix m = FloatMatrix.of((float[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_of_withEmptyArray() {
        FloatMatrix m = FloatMatrix.of(new float[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_of_withSingleRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f } });
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
    }

    // ============ Create Method Tests ============

    @Test
    public void test_create_fromIntArray() {
        int[][] ints = { { 1, 2 }, { 3, 4 } };
        FloatMatrix m = FloatMatrix.from(ints);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0), 0.0f);
        assertEquals(4.0f, m.get(1, 1), 0.0f);
    }

    @Test
    public void test_create_fromIntArray_empty() {
        FloatMatrix m = FloatMatrix.from(new int[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_create_fromIntArray_null() {
        FloatMatrix m = FloatMatrix.from((int[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_create_fromIntArray_nullFirstRow() {
        int[][] ints = { null, { 1, 2 } };
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.from(ints));
    }

    @Test
    public void test_create_fromIntArray_differentRowLengths() {
        int[][] ints = { { 1, 2 }, { 3 } };
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.from(ints));
    }

    // ============ Random and Repeat Tests ============

    @Test
    public void test_random() {
        FloatMatrix m = FloatMatrix.random(5);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
    }

    @Test
    public void test_random_zeroLength() {
        FloatMatrix m = FloatMatrix.random(0);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void test_repeat() {
        FloatMatrix m = FloatMatrix.repeat(1, 5, 3.14f);
        assertEquals(1, m.rowCount());
        assertEquals(5, m.columnCount());
        for (int i = 0; i < 5; i++) {
            assertEquals(3.14f, m.get(0, i), 0.0f);
        }
    }

    @Test
    public void test_repeat_zeroLength() {
        FloatMatrix m = FloatMatrix.repeat(1, 0, 3.14f);
        assertEquals(1, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    // ============ Diagonal Tests ============

    @Test
    public void test_diagonalLU2RD() {
        float[] diag = { 1.0f, 2.0f, 3.0f };
        FloatMatrix m = FloatMatrix.diagonalLU2RD(diag);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0f, m.get(0, 0), 0.0f);
        assertEquals(2.0f, m.get(1, 1), 0.0f);
        assertEquals(3.0f, m.get(2, 2), 0.0f);
        assertEquals(0.0f, m.get(0, 1), 0.0f);
        assertEquals(0.0f, m.get(1, 0), 0.0f);
    }

    @Test
    public void test_diagonalLU2RD_null() {
        FloatMatrix m = FloatMatrix.diagonalLU2RD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_diagonalRU2LD() {
        float[] diag = { 1.0f, 2.0f, 3.0f };
        FloatMatrix m = FloatMatrix.diagonalRU2LD(diag);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0f, m.get(0, 2), 0.0f);
        assertEquals(2.0f, m.get(1, 1), 0.0f);
        assertEquals(3.0f, m.get(2, 0), 0.0f);
        assertEquals(0.0f, m.get(0, 0), 0.0f);
    }

    @Test
    public void test_diagonalRU2LD_null() {
        FloatMatrix m = FloatMatrix.diagonalRU2LD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_diagonal_both() {
        float[] lu2rd = { 1.0f, 2.0f, 3.0f };
        float[] ru2ld = { 4.0f, 5.0f, 6.0f };
        FloatMatrix m = FloatMatrix.diagonal(lu2rd, ru2ld);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1.0f, m.get(0, 0), 0.0f);
        assertEquals(2.0f, m.get(1, 1), 0.0f);
        assertEquals(3.0f, m.get(2, 2), 0.0f);
        assertEquals(4.0f, m.get(0, 2), 0.0f);
        assertEquals(6.0f, m.get(2, 0), 0.0f);
    }

    @Test
    public void test_diagonal_differentLengths() {
        float[] lu2rd = { 1.0f, 2.0f };
        float[] ru2ld = { 4.0f, 5.0f, 6.0f };
        assertThrows(IllegalArgumentException.class, () -> FloatMatrix.diagonal(lu2rd, ru2ld));
    }

    @Test
    public void test_diagonal_bothNull() {
        FloatMatrix m = FloatMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    // ============ Unbox Test ============

    @Test
    public void test_unbox() {
        Matrix<Float> boxed = Matrix.of(new Float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m = FloatMatrix.unbox(boxed);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0), 0.0f);
        assertEquals(4.0f, m.get(1, 1), 0.0f);
    }

    @Test
    public void test_unbox_withNulls() {
        Matrix<Float> boxed = Matrix.of(new Float[][] { { 1.0f, null }, { null, 4.0f } });
        FloatMatrix m = FloatMatrix.unbox(boxed);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1.0f, m.get(0, 0), 0.0f);
        assertEquals(0.0f, m.get(0, 1), 0.0f);
        assertEquals(0.0f, m.get(1, 0), 0.0f);
        assertEquals(4.0f, m.get(1, 1), 0.0f);
    }

    // ============ Component Type Test ============

    @Test
    public void test_componentType() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertEquals(float.class, m.componentType());
    }

    // ============ Get and Set Tests ============

    @Test
    public void test_get_byIndices() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertEquals(1.0f, m.get(0, 0), 0.0f);
        assertEquals(2.0f, m.get(0, 1), 0.0f);
        assertEquals(3.0f, m.get(1, 0), 0.0f);
        assertEquals(4.0f, m.get(1, 1), 0.0f);
    }

    @Test
    public void test_get_byPoint() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Point p = Point.of(0, 1);
        assertEquals(2.0f, m.get(p), 0.0f);
    }

    @Test
    public void test_set_byIndices() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.set(0, 1, 9.0f);
        assertEquals(9.0f, m.get(0, 1), 0.0f);
    }

    @Test
    public void test_set_byPoint() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Point p = Point.of(1, 1);
        m.set(p, 9.0f);
        assertEquals(9.0f, m.get(p), 0.0f);
    }

    // ============ Directional Access Tests ============

    @Test
    public void test_upOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1.0f, up.get(), 0.0f);
    }

    @Test
    public void test_upOf_firstRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void test_downOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3.0f, down.get(), 0.0f);
    }

    @Test
    public void test_downOf_lastRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void test_leftOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1.0f, left.get(), 0.0f);
    }

    @Test
    public void test_leftOf_firstColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void test_rightOf() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2.0f, right.get(), 0.0f);
    }

    @Test
    public void test_rightOf_lastColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        OptionalFloat right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    // ============ Row and Column Access Tests ============

    @Test
    public void test_row() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        float[] row = m.row(0);
        assertArrayEquals(new float[] { 1.0f, 2.0f }, row, 0.0f);
    }

    @Test
    public void test_row_invalidIndex() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.row(5));
    }

    @Test
    public void test_column() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        float[] col = m.column(0);
        assertArrayEquals(new float[] { 1.0f, 3.0f }, col, 0.0f);
    }

    @Test
    public void test_column_invalidIndex() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.column(5));
    }

    @Test
    public void test_setRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.setRow(0, new float[] { 9.0f, 8.0f });
        assertArrayEquals(new float[] { 9.0f, 8.0f }, m.row(0), 0.0f);
    }

    @Test
    public void test_setRow_invalidRowIndex() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(5, new float[] { 1.0f, 2.0f }));
    }

    @Test
    public void test_setRow_invalidLength() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new float[] { 1.0f }));
    }

    @Test
    public void test_setColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.setColumn(0, new float[] { 9.0f, 8.0f });
        assertArrayEquals(new float[] { 9.0f, 8.0f }, m.column(0), 0.0f);
    }

    @Test
    public void test_setColumn_invalidColumnIndex() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(5, new float[] { 1.0f }));
    }

    @Test
    public void test_setColumn_invalidLength() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new float[] { 1.0f }));
    }

    // ============ Update Row and Column Tests ============

    @Test
    public void test_updateRow() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateRow(0, x -> x * 2);
        assertArrayEquals(new float[] { 2.0f, 4.0f }, m.row(0), 0.0f);
        assertArrayEquals(new float[] { 3.0f, 4.0f }, m.row(1), 0.0f);
    }

    @Test
    public void test_updateColumn() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateColumn(0, x -> x * 2);
        assertArrayEquals(new float[] { 2.0f, 6.0f }, m.column(0), 0.0f);
        assertArrayEquals(new float[] { 2.0f, 4.0f }, m.column(1), 0.0f);
    }

    // ============ Diagonal Get/Set Tests ============

    @Test
    public void test_getLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        float[] diag = m.getLU2RD();
        assertArrayEquals(new float[] { 1.0f, 5.0f, 9.0f }, diag, 0.0f);
    }

    @Test
    public void test_getLU2RD_nonSquare() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void test_setLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.setLU2RD(new float[] { 9.0f, 8.0f });
        assertArrayEquals(new float[] { 9.0f, 8.0f }, m.getLU2RD(), 0.0f);
    }

    @Test
    public void test_setLU2RD_nonSquare() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new float[] { 9.0f }));
    }

    @Test
    public void test_setLU2RD_invalidLength() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new float[] { 9.0f }));
    }

    @Test
    public void test_updateLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateLU2RD(x -> x * 2);
        assertArrayEquals(new float[] { 2.0f, 8.0f }, m.getLU2RD(), 0.0f);
    }

    @Test
    public void test_getRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        float[] diag = m.getRU2LD();
        assertArrayEquals(new float[] { 3.0f, 5.0f, 7.0f }, diag, 0.0f);
    }

    @Test
    public void test_setRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.setRU2LD(new float[] { 9.0f, 8.0f });
        assertArrayEquals(new float[] { 9.0f, 8.0f }, m.getRU2LD(), 0.0f);
    }

    @Test
    public void test_updateRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateRU2LD(x -> x * 2);
        float[] diag = m.getRU2LD();
        assertEquals(4.0f, diag[0], 0.0f);
        assertEquals(6.0f, diag[1], 0.0f);
    }

    // ============ Update All Tests ============

    @Test
    public void test_updateAll_unaryOperator() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateAll(x -> x * 2);
        assertEquals(2.0f, m.get(0, 0), 0.0f);
        assertEquals(4.0f, m.get(0, 1), 0.0f);
        assertEquals(6.0f, m.get(1, 0), 0.0f);
        assertEquals(8.0f, m.get(1, 1), 0.0f);
    }

    @Test
    public void test_updateAll_biFunction() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.updateAll((i, j) -> (i + 1) * 10.0f + (j + 1));
        assertEquals(11.0f, m.get(0, 0), 0.0f);
        assertEquals(12.0f, m.get(0, 1), 0.0f);
        assertEquals(21.0f, m.get(1, 0), 0.0f);
        assertEquals(22.0f, m.get(1, 1), 0.0f);
    }

    // ============ Replace If Tests ============

    @Test
    public void test_replaceIf_predicate() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.replaceIf(x -> x > 2, 99.0f);
        assertEquals(1.0f, m.get(0, 0), 0.0f);
        assertEquals(2.0f, m.get(0, 1), 0.0f);
        assertEquals(99.0f, m.get(1, 0), 0.0f);
        assertEquals(99.0f, m.get(1, 1), 0.0f);
    }

    @Test
    public void test_replaceIf_biPredicate() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.replaceIf((i, j) -> i == j, 99.0f);
        assertEquals(99.0f, m.get(0, 0), 0.0f);
        assertEquals(2.0f, m.get(0, 1), 0.0f);
        assertEquals(3.0f, m.get(1, 0), 0.0f);
        assertEquals(99.0f, m.get(1, 1), 0.0f);
    }

    // ============ Map Tests ============

    @Test
    public void test_map() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix result = m.map(x -> x * 2);
        assertEquals(2.0f, result.get(0, 0), 0.0f);
        assertEquals(4.0f, result.get(0, 1), 0.0f);
        assertEquals(6.0f, result.get(1, 0), 0.0f);
        assertEquals(8.0f, result.get(1, 1), 0.0f);
        // Original should be unchanged
        assertEquals(1.0f, m.get(0, 0), 0.0f);
    }

    @Test
    public void test_mapToObj() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Matrix<String> result = m.mapToObj(x -> String.valueOf(x), String.class);
        assertEquals("1.0", result.get(0, 0));
        assertEquals("2.0", result.get(0, 1));
        assertEquals("3.0", result.get(1, 0));
        assertEquals("4.0", result.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void test_fill_value() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.fill(9.0f);
        assertEquals(9.0f, m.get(0, 0), 0.0f);
        assertEquals(9.0f, m.get(0, 1), 0.0f);
        assertEquals(9.0f, m.get(1, 0), 0.0f);
        assertEquals(9.0f, m.get(1, 1), 0.0f);
    }

    @Test
    public void test_fill_array() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        m.fill(new float[][] { { 9.0f, 8.0f }, { 7.0f, 6.0f } });
        assertEquals(9.0f, m.get(0, 0), 0.0f);
        assertEquals(8.0f, m.get(0, 1), 0.0f);
        assertEquals(7.0f, m.get(1, 0), 0.0f);
        assertEquals(6.0f, m.get(1, 1), 0.0f);
    }

    @Test
    public void test_fill_withOffset() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        m.fill(1, 1, new float[][] { { 99.0f } });
        assertEquals(1.0f, m.get(0, 0), 0.0f);
        assertEquals(99.0f, m.get(1, 1), 0.0f);
        assertEquals(9.0f, m.get(2, 2), 0.0f);
    }

    @Test
    public void test_fill_withOffset_oversizedArray() {
        // According to javadoc, fill() copies what fits - does not throw when source array is larger
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        m.fill(0, 0, new float[][] { { 9.0f, 8.0f, 7.0f } }); // Third element should be ignored
        assertEquals(9.0f, m.get(0, 0), 0.0f);
        assertEquals(8.0f, m.get(0, 1), 0.0f); // Only copies what fits
    }

    // ============ Copy Tests ============

    @Test
    public void test_copy() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix copy = m.copy();
        assertEquals(m.rowCount(), copy.rowCount());
        assertEquals(m.columnCount(), copy.columnCount());
        assertEquals(1.0f, copy.get(0, 0), 0.0f);
        copy.set(0, 0, 99.0f);
        assertEquals(1.0f, m.get(0, 0), 0.0f); // Original unchanged
    }

    @Test
    public void test_copy_withRowRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        FloatMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(3.0f, copy.get(0, 0), 0.0f);
        assertEquals(6.0f, copy.get(1, 1), 0.0f);
    }

    @Test
    public void test_copy_withFullRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        FloatMatrix copy = m.copy(1, 3, 1, 3);
        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals(5.0f, copy.get(0, 0), 0.0f);
        assertEquals(9.0f, copy.get(1, 1), 0.0f);
    }

    @Test
    public void test_copy_invalidRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 5));
    }

    // ============ Extend Tests ============

    @Test
    public void test_extend() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1.0f, extended.get(0, 0), 0.0f);
        assertEquals(0.0f, extended.get(2, 2), 0.0f);
    }

    @Test
    public void test_extend_withDefaultValue() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix extended = m.extend(2, 3, 99.0f);
        assertEquals(2, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1.0f, extended.get(0, 0), 0.0f);
        assertEquals(99.0f, extended.get(1, 1), 0.0f);
        assertEquals(99.0f, extended.get(0, 2), 0.0f);
    }

    @Test
    public void test_extend_smaller() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        FloatMatrix result = m.extend(1, 2);
        assertEquals(1, result.rowCount());
        assertEquals(2, result.columnCount());
    }

    @Test
    public void test_extend_directional() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(1.0f, extended.get(1, 1), 0.0f);
        assertEquals(0.0f, extended.get(0, 0), 0.0f);
    }

    @Test
    public void test_extend_directional_withDefaultValue() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix extended = m.extend(1, 1, 1, 1, 99.0f);
        assertEquals(3, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(1.0f, extended.get(1, 1), 0.0f);
        assertEquals(99.0f, extended.get(0, 0), 0.0f);
    }

    // ============ Reverse and Flip Tests ============

    @Test
    public void test_reverseH() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        m.reverseH();
        assertEquals(3.0f, m.get(0, 0), 0.0f);
        assertEquals(2.0f, m.get(0, 1), 0.0f);
        assertEquals(1.0f, m.get(0, 2), 0.0f);
    }

    @Test
    public void test_reverseV() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        m.reverseV();
        assertEquals(5.0f, m.get(0, 0), 0.0f);
        assertEquals(3.0f, m.get(1, 0), 0.0f);
        assertEquals(1.0f, m.get(2, 0), 0.0f);
    }

    @Test
    public void test_flipH() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        FloatMatrix flipped = m.flipH();
        assertEquals(3.0f, flipped.get(0, 0), 0.0f);
        assertEquals(2.0f, flipped.get(0, 1), 0.0f);
        assertEquals(1.0f, flipped.get(0, 2), 0.0f);
        // Original unchanged
        assertEquals(1.0f, m.get(0, 0), 0.0f);
    }

    @Test
    public void test_flipV() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        FloatMatrix flipped = m.flipV();
        assertEquals(5.0f, flipped.get(0, 0), 0.0f);
        assertEquals(3.0f, flipped.get(1, 0), 0.0f);
        assertEquals(1.0f, flipped.get(2, 0), 0.0f);
        // Original unchanged
        assertEquals(1.0f, m.get(0, 0), 0.0f);
    }

    // ============ Rotate Tests ============

    @Test
    public void test_rotate90() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(3.0f, rotated.get(0, 0), 0.0f);
        assertEquals(1.0f, rotated.get(0, 1), 0.0f);
        assertEquals(4.0f, rotated.get(1, 0), 0.0f);
        assertEquals(2.0f, rotated.get(1, 1), 0.0f);
    }

    @Test
    public void test_rotate180() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix rotated = m.rotate180();
        assertEquals(4.0f, rotated.get(0, 0), 0.0f);
        assertEquals(3.0f, rotated.get(0, 1), 0.0f);
        assertEquals(2.0f, rotated.get(1, 0), 0.0f);
        assertEquals(1.0f, rotated.get(1, 1), 0.0f);
    }

    @Test
    public void test_rotate270() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals(2.0f, rotated.get(0, 0), 0.0f);
        assertEquals(4.0f, rotated.get(0, 1), 0.0f);
        assertEquals(1.0f, rotated.get(1, 0), 0.0f);
        assertEquals(3.0f, rotated.get(1, 1), 0.0f);
    }

    // ============ Transpose Test ============

    @Test
    public void test_transpose() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        FloatMatrix transposed = m.transpose();
        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1.0f, transposed.get(0, 0), 0.0f);
        assertEquals(4.0f, transposed.get(0, 1), 0.0f);
        assertEquals(2.0f, transposed.get(1, 0), 0.0f);
        assertEquals(5.0f, transposed.get(1, 1), 0.0f);
    }

    @Test
    public void test_transpose_square() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix transposed = m.transpose();
        assertEquals(2, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals(1.0f, transposed.get(0, 0), 0.0f);
        assertEquals(3.0f, transposed.get(0, 1), 0.0f);
    }

    // ============ Reshape Test ============

    @Test
    public void test_reshape() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        FloatMatrix reshaped = m.reshape(3, 2);
        assertEquals(3, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(1.0f, reshaped.get(0, 0), 0.0f);
        assertEquals(2.0f, reshaped.get(0, 1), 0.0f);
        assertEquals(3.0f, reshaped.get(1, 0), 0.0f);
        assertEquals(4.0f, reshaped.get(1, 1), 0.0f);
    }

    @Test
    public void test_reshape_differentSize() {
        // According to javadoc, reshape allows different sizes - excess elements truncated, missing filled with zeros
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix reshaped = m.reshape(3, 3); // 4 elements reshaped to 9 positions
        assertEquals(3, reshaped.rowCount());
        assertEquals(3, reshaped.columnCount());
        // Original elements
        assertEquals(1.0f, reshaped.get(0, 0), 0.0f);
        assertEquals(2.0f, reshaped.get(0, 1), 0.0f);
        assertEquals(3.0f, reshaped.get(0, 2), 0.0f);
        assertEquals(4.0f, reshaped.get(1, 0), 0.0f);
        // Remaining positions filled with zeros
        assertEquals(0.0f, reshaped.get(1, 1), 0.0f);
        assertEquals(0.0f, reshaped.get(1, 2), 0.0f);
        assertEquals(0.0f, reshaped.get(2, 0), 0.0f);
        assertEquals(0.0f, reshaped.get(2, 1), 0.0f);
        assertEquals(0.0f, reshaped.get(2, 2), 0.0f);
    }

    // ============ Repelem Test ============

    @Test
    public void test_repelem() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix result = m.repelem(2, 2);
        assertEquals(4, result.rowCount());
        assertEquals(4, result.columnCount());
        assertEquals(1.0f, result.get(0, 0), 0.0f);
        assertEquals(1.0f, result.get(0, 1), 0.0f);
        assertEquals(1.0f, result.get(1, 0), 0.0f);
        assertEquals(1.0f, result.get(1, 1), 0.0f);
        assertEquals(2.0f, result.get(0, 2), 0.0f);
    }

    @Test
    public void test_repelem_invalidRepeats() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
    }

    // ============ Repmat Test ============

    @Test
    public void test_repmat() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix result = m.repmat(2, 2);
        assertEquals(2, result.rowCount());
        assertEquals(4, result.columnCount());
        assertEquals(1.0f, result.get(0, 0), 0.0f);
        assertEquals(2.0f, result.get(0, 1), 0.0f);
        assertEquals(1.0f, result.get(0, 2), 0.0f);
        assertEquals(2.0f, result.get(0, 3), 0.0f);
    }

    @Test
    public void test_repmat_invalidRepeats() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
    }

    // ============ Flatten Test ============

    @Test
    public void test_flatten() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatList flat = m.flatten();
        assertEquals(4, flat.size());
        assertEquals(1.0f, flat.get(0), 0.0f);
        assertEquals(2.0f, flat.get(1), 0.0f);
        assertEquals(3.0f, flat.get(2), 0.0f);
        assertEquals(4.0f, flat.get(3), 0.0f);
    }

    // ============ FlatOp Test ============

    @Test
    public void test_flatOp() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        AtomicInteger count = new AtomicInteger(0);
        m.flatOp(row -> count.addAndGet(row.length));
        assertEquals(4, count.get());
    }

    // ============ Vstack and Hstack Tests ============

    @Test
    public void test_vstack() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f, 4.0f } });
        FloatMatrix result = m1.vstack(m2);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(1.0f, result.get(0, 0), 0.0f);
        assertEquals(3.0f, result.get(1, 0), 0.0f);
    }

    @Test
    public void test_vstack_incompatibleCols() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void test_hstack() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f }, { 2.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f }, { 4.0f } });
        FloatMatrix result = m1.hstack(m2);
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(1.0f, result.get(0, 0), 0.0f);
        assertEquals(3.0f, result.get(0, 1), 0.0f);
    }

    @Test
    public void test_hstack_incompatibleRows() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f }, { 4.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Operations Tests ============

    @Test
    public void test_add() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix result = m1.add(m2);
        assertEquals(6.0f, result.get(0, 0), 0.0f);
        assertEquals(8.0f, result.get(0, 1), 0.0f);
        assertEquals(10.0f, result.get(1, 0), 0.0f);
        assertEquals(12.0f, result.get(1, 1), 0.0f);
    }

    @Test
    public void test_add_incompatibleDimensions() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void test_subtract() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix result = m1.subtract(m2);
        assertEquals(4.0f, result.get(0, 0), 0.0f);
        assertEquals(4.0f, result.get(0, 1), 0.0f);
        assertEquals(4.0f, result.get(1, 0), 0.0f);
        assertEquals(4.0f, result.get(1, 1), 0.0f);
    }

    @Test
    public void test_subtract_incompatibleDimensions() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void test_multiply() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 2.0f, 0.0f }, { 1.0f, 2.0f } });
        FloatMatrix result = m1.multiply(m2);
        assertEquals(4.0f, result.get(0, 0), 0.0f);
        assertEquals(4.0f, result.get(0, 1), 0.0f);
        assertEquals(10.0f, result.get(1, 0), 0.0f);
        assertEquals(8.0f, result.get(1, 1), 0.0f);
    }

    @Test
    public void test_multiply_incompatibleDimensions() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
    }

    // ============ Boxed Test ============

    @Test
    public void test_boxed() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        Matrix<Float> boxed = m.boxed();
        assertEquals(2, boxed.rowCount());
        assertEquals(2, boxed.columnCount());
        assertEquals(1.0f, boxed.get(0, 0), 0.0f);
        assertEquals(4.0f, boxed.get(1, 1), 0.0f);
    }

    // ============ ToDoubleMatrix Test ============

    @Test
    public void test_toDoubleMatrix() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        DoubleMatrix result = m.toDoubleMatrix();
        assertEquals(2, result.rowCount());
        assertEquals(2, result.columnCount());
        assertEquals(1.0, result.get(0, 0), 0.001);
        assertEquals(4.0, result.get(1, 1), 0.001);
    }

    // ============ ZipWith Tests ============

    @Test
    public void test_zipWith_binary() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f }, { 7.0f, 8.0f } });
        FloatMatrix result = m1.zipWith(m2, (a, b) -> a * b);
        assertEquals(5.0f, result.get(0, 0), 0.0f);
        assertEquals(12.0f, result.get(0, 1), 0.0f);
        assertEquals(21.0f, result.get(1, 0), 0.0f);
        assertEquals(32.0f, result.get(1, 1), 0.0f);
    }

    @Test
    public void test_zipWith_ternary() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 3.0f, 4.0f } });
        FloatMatrix m3 = FloatMatrix.of(new float[][] { { 5.0f, 6.0f } });
        FloatMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
        assertEquals(9.0f, result.get(0, 0), 0.0f);
        assertEquals(12.0f, result.get(0, 1), 0.0f);
    }

    // ============ Stream Tests ============

    @Test
    public void test_streamH() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        double sum = m.streamH().sum();
        assertEquals(10.0, sum, 0.0);
    }

    @Test
    public void test_streamH_byRowIndex() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        double sum = m.streamH(0).sum();
        assertEquals(3.0, sum, 0.0);
    }

    @Test
    public void test_streamH_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        double sum = m.streamH(1, 3).sum();
        assertEquals(18.0, sum, 0.0);
    }

    @Test
    public void test_streamV() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        double sum = m.streamV().sum();
        assertEquals(10.0, sum, 0.0);
    }

    @Test
    public void test_streamV_byColumnIndex() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        double sum = m.streamV(0).sum();
        assertEquals(4.0, sum, 0.0);
    }

    @Test
    public void test_streamV_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f } });
        double sum = m.streamV(1, 3).sum();
        assertEquals(16.0, sum, 0.0);
    }

    @Test
    public void test_streamLU2RD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        double sum = m.streamLU2RD().sum();
        assertEquals(5.0, sum, 0.0);
    }

    @Test
    public void test_streamRU2LD() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        double sum = m.streamRU2LD().sum();
        assertEquals(5.0, sum, 0.0);
    }

    @Test
    public void test_streamR() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        long count = m.streamR().count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamR_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f }, { 5.0f, 6.0f } });
        long count = m.streamR(1, 3).count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamC() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        long count = m.streamC().count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamC_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f } });
        long count = m.streamC(1, 3).count();
        assertEquals(2, count);
    }

    // ============ ForEach Tests ============

    @Test
    public void test_forEach() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        AtomicInteger count = new AtomicInteger(0);
        m.forEach(x -> count.incrementAndGet());
        assertEquals(4, count.get());
    }

    @Test
    public void test_forEach_withRange() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f, 3.0f }, { 4.0f, 5.0f, 6.0f }, { 7.0f, 8.0f, 9.0f } });
        AtomicInteger count = new AtomicInteger(0);
        m.forEach(1, 3, 1, 3, x -> count.incrementAndGet());
        assertEquals(4, count.get());
    }

    // ============ Utility Tests ============

    @Test
    public void test_println() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        String result = m.println();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void test_hashCode_consistent() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void test_equals_same() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        assertEquals(m1, m2);
    }

    @Test
    public void test_equals_different() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        FloatMatrix m2 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 5.0f } });
        assertNotEquals(m1, m2);
    }

    @Test
    public void test_equals_null() {
        FloatMatrix m1 = FloatMatrix.of(new float[][] { { 1.0f, 2.0f } });
        assertNotEquals(m1, null);
    }

    @Test
    public void test_toString() {
        FloatMatrix m = FloatMatrix.of(new float[][] { { 1.0f, 2.0f }, { 3.0f, 4.0f } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }
}
