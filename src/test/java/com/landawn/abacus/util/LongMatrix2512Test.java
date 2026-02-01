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
import com.landawn.abacus.util.u.OptionalLong;

@Tag("2512")
public class LongMatrix2512Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void test_constructor_withValidArray() {
        long[][] arr = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix m = new LongMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(4L, m.get(1, 1));
    }

    @Test
    public void test_constructor_withNullArray() {
        LongMatrix m = new LongMatrix(null);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_withEmptyArray() {
        LongMatrix m = new LongMatrix(new long[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_withSingleElement() {
        LongMatrix m = new LongMatrix(new long[][] { { 42L } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(42L, m.get(0, 0));
    }

    // ============ Factory Method Tests ============

    @Test
    public void test_empty() {
        LongMatrix empty = LongMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());
        assertSame(LongMatrix.empty(), LongMatrix.empty());
    }

    @Test
    public void test_of_withValidArray() {
        long[][] arr = { { 1L, 2L }, { 3L, 4L } };
        LongMatrix m = LongMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1L, m.get(0, 0));
    }

    @Test
    public void test_of_withNullArray() {
        LongMatrix m = LongMatrix.of((long[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_of_withEmptyArray() {
        LongMatrix m = LongMatrix.of(new long[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_of_withSingleRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L, 4L, 5L } });
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
    }

    // ============ Create Method Tests ============

    @Test
    public void test_create_fromIntArray() {
        int[][] ints = { { 1, 2 }, { 3, 4 } };
        LongMatrix m = LongMatrix.from(ints);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(4L, m.get(1, 1));
    }

    @Test
    public void test_create_fromIntArray_empty() {
        LongMatrix m = LongMatrix.from(new int[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_create_fromIntArray_null() {
        LongMatrix m = LongMatrix.from((int[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_create_fromIntArray_nullFirstRow() {
        int[][] ints = { null, { 1, 2 } };
        assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(ints));
    }

    @Test
    public void test_create_fromIntArray_differentRowLengths() {
        int[][] ints = { { 1, 2 }, { 3 } };
        assertThrows(IllegalArgumentException.class, () -> LongMatrix.from(ints));
    }

    // ============ Random and Repeat Tests ============

    @Test
    public void test_random() {
        LongMatrix m = LongMatrix.random(5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
    }

    @Test
    public void test_random_zeroLength() {
        LongMatrix m = LongMatrix.random(0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void test_repeat() {
        LongMatrix m = LongMatrix.repeat(42L, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals(42L, m.get(0, i));
        }
    }

    @Test
    public void test_repeat_zeroLength() {
        LongMatrix m = LongMatrix.repeat(42L, 0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    // ============ Range Tests ============

    @Test
    public void test_range() {
        LongMatrix m = LongMatrix.range(0L, 5L);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.row(0));
    }

    @Test
    public void test_range_withStep() {
        LongMatrix m = LongMatrix.range(0L, 10L, 2L);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L }, m.row(0));
    }

    @Test
    public void test_range_empty() {
        LongMatrix m = LongMatrix.range(5L, 5L);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void test_rangeClosed() {
        LongMatrix m = LongMatrix.rangeClosed(0L, 4L);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new long[] { 0L, 1L, 2L, 3L, 4L }, m.row(0));
    }

    @Test
    public void test_rangeClosed_withStep() {
        LongMatrix m = LongMatrix.rangeClosed(0L, 10L, 2L);
        assertEquals(1, m.rows);
        assertEquals(6, m.cols);
        assertArrayEquals(new long[] { 0L, 2L, 4L, 6L, 8L, 10L }, m.row(0));
    }

    @Test
    public void test_rangeClosed_singleValue() {
        LongMatrix m = LongMatrix.rangeClosed(5L, 5L);
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(5L, m.get(0, 0));
    }

    // ============ Diagonal Tests ============

    @Test
    public void test_diagonalLU2RD() {
        long[] diag = { 1L, 2L, 3L };
        LongMatrix m = LongMatrix.diagonalLU2RD(diag);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 2));
        assertEquals(0L, m.get(0, 1));
        assertEquals(0L, m.get(1, 0));
    }

    @Test
    public void test_diagonalLU2RD_null() {
        LongMatrix m = LongMatrix.diagonalLU2RD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_diagonalRU2LD() {
        long[] diag = { 1L, 2L, 3L };
        LongMatrix m = LongMatrix.diagonalRU2LD(diag);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1L, m.get(0, 2));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 0));
        assertEquals(0L, m.get(0, 0));
    }

    @Test
    public void test_diagonalRU2LD_null() {
        LongMatrix m = LongMatrix.diagonalRU2LD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_diagonal_both() {
        long[] lu2rd = { 1L, 2L, 3L };
        long[] ru2ld = { 4L, 5L, 6L };
        LongMatrix m = LongMatrix.diagonal(lu2rd, ru2ld);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(1, 1));
        assertEquals(3L, m.get(2, 2));
        assertEquals(4L, m.get(0, 2));
        assertEquals(6L, m.get(2, 0));
    }

    @Test
    public void test_diagonal_differentLengths() {
        long[] lu2rd = { 1L, 2L };
        long[] ru2ld = { 4L, 5L, 6L };
        assertThrows(IllegalArgumentException.class, () -> LongMatrix.diagonal(lu2rd, ru2ld));
    }

    @Test
    public void test_diagonal_bothNull() {
        LongMatrix m = LongMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    // ============ Unbox Test ============

    @Test
    public void test_unbox() {
        Matrix<Long> boxed = Matrix.of(new Long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m = LongMatrix.unbox(boxed);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(4L, m.get(1, 1));
    }

    @Test
    public void test_unbox_withNulls() {
        Matrix<Long> boxed = Matrix.of(new Long[][] { { 1L, null }, { null, 4L } });
        LongMatrix m = LongMatrix.unbox(boxed);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1L, m.get(0, 0));
        assertEquals(0L, m.get(0, 1));
        assertEquals(0L, m.get(1, 0));
        assertEquals(4L, m.get(1, 1));
    }

    // ============ Component Type Test ============

    @Test
    public void test_componentType() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertEquals(long.class, m.componentType());
    }

    // ============ Get and Set Tests ============

    @Test
    public void test_get_byIndices() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(0, 1));
        assertEquals(3L, m.get(1, 0));
        assertEquals(4L, m.get(1, 1));
    }

    @Test
    public void test_get_byPoint() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        Point p = Point.of(0, 1);
        assertEquals(2L, m.get(p));
    }

    @Test
    public void test_set_byIndices() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.set(0, 1, 9L);
        assertEquals(9L, m.get(0, 1));
    }

    @Test
    public void test_set_byPoint() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        Point p = Point.of(1, 1);
        m.set(p, 9L);
        assertEquals(9L, m.get(p));
    }

    // ============ Directional Access Tests ============

    @Test
    public void test_upOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1L, up.get());
    }

    @Test
    public void test_upOf_firstRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void test_downOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3L, down.get());
    }

    @Test
    public void test_downOf_lastRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void test_leftOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1L, left.get());
    }

    @Test
    public void test_leftOf_firstColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void test_rightOf() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2L, right.get());
    }

    @Test
    public void test_rightOf_lastColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        OptionalLong right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    // ============ Row and Column Access Tests ============

    @Test
    public void test_row() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long[] row = m.row(0);
        assertArrayEquals(new long[] { 1L, 2L }, row);
    }

    @Test
    public void test_row_invalidIndex() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalArgumentException.class, () -> m.row(5));
    }

    @Test
    public void test_column() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long[] col = m.column(0);
        assertArrayEquals(new long[] { 1L, 3L }, col);
    }

    @Test
    public void test_column_invalidIndex() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalArgumentException.class, () -> m.column(5));
    }

    @Test
    public void test_setRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.setRow(0, new long[] { 9L, 8L });
        assertArrayEquals(new long[] { 9L, 8L }, m.row(0));
    }

    @Test
    public void test_setRow_invalidRowIndex() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(5, new long[] { 1L, 2L }));
    }

    @Test
    public void test_setRow_invalidLength() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new long[] { 1L }));
    }

    @Test
    public void test_setColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.setColumn(0, new long[] { 9L, 8L });
        assertArrayEquals(new long[] { 9L, 8L }, m.column(0));
    }

    @Test
    public void test_setColumn_invalidColumnIndex() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(5, new long[] { 1L }));
    }

    @Test
    public void test_setColumn_invalidLength() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new long[] { 1L }));
    }

    // ============ Update Row and Column Tests ============

    @Test
    public void test_updateRow() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateRow(0, x -> x * 2);
        assertArrayEquals(new long[] { 2L, 4L }, m.row(0));
        assertArrayEquals(new long[] { 3L, 4L }, m.row(1));
    }

    @Test
    public void test_updateColumn() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateColumn(0, x -> x * 2);
        assertArrayEquals(new long[] { 2L, 6L }, m.column(0));
        assertArrayEquals(new long[] { 2L, 4L }, m.column(1));
    }

    // ============ Diagonal Get/Set Tests ============

    @Test
    public void test_getLU2RD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] diag = m.getLU2RD();
        assertArrayEquals(new long[] { 1L, 5L, 9L }, diag);
    }

    @Test
    public void test_getLU2RD_nonSquare() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void test_setLU2RD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.setLU2RD(new long[] { 9L, 8L });
        assertArrayEquals(new long[] { 9L, 8L }, m.getLU2RD());
    }

    @Test
    public void test_setLU2RD_nonSquare() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new long[] { 9L }));
    }

    @Test
    public void test_setLU2RD_invalidLength() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new long[] { 9L }));
    }

    @Test
    public void test_updateLU2RD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateLU2RD(x -> x * 2);
        assertArrayEquals(new long[] { 2L, 8L }, m.getLU2RD());
    }

    @Test
    public void test_getRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        long[] diag = m.getRU2LD();
        assertArrayEquals(new long[] { 3L, 5L, 7L }, diag);
    }

    @Test
    public void test_setRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.setRU2LD(new long[] { 9L, 8L });
        assertArrayEquals(new long[] { 9L, 8L }, m.getRU2LD());
    }

    @Test
    public void test_updateRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateRU2LD(x -> x * 2);
        long[] diag = m.getRU2LD();
        assertEquals(4L, diag[0]);
        assertEquals(6L, diag[1]);
    }

    // ============ Update All Tests ============

    @Test
    public void test_updateAll_unaryOperator() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateAll(x -> x * 2);
        assertEquals(2L, m.get(0, 0));
        assertEquals(4L, m.get(0, 1));
        assertEquals(6L, m.get(1, 0));
        assertEquals(8L, m.get(1, 1));
    }

    @Test
    public void test_updateAll_biFunction() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.updateAll((i, j) -> (i + 1) * 10L + (j + 1));
        assertEquals(11L, m.get(0, 0));
        assertEquals(12L, m.get(0, 1));
        assertEquals(21L, m.get(1, 0));
        assertEquals(22L, m.get(1, 1));
    }

    // ============ Replace If Tests ============

    @Test
    public void test_replaceIf_predicate() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.replaceIf(x -> x > 2, 99L);
        assertEquals(1L, m.get(0, 0));
        assertEquals(2L, m.get(0, 1));
        assertEquals(99L, m.get(1, 0));
        assertEquals(99L, m.get(1, 1));
    }

    @Test
    public void test_replaceIf_biPredicate() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.replaceIf((i, j) -> i == j, 99L);
        assertEquals(99L, m.get(0, 0));
        assertEquals(2L, m.get(0, 1));
        assertEquals(3L, m.get(1, 0));
        assertEquals(99L, m.get(1, 1));
    }

    // ============ Map Tests ============

    @Test
    public void test_map() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix result = m.map(x -> x * 2);
        assertEquals(2L, result.get(0, 0));
        assertEquals(4L, result.get(0, 1));
        assertEquals(6L, result.get(1, 0));
        assertEquals(8L, result.get(1, 1));
        // Original should be unchanged
        assertEquals(1L, m.get(0, 0));
    }

    @Test
    public void test_mapToInt() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        IntMatrix result = m.mapToInt(x -> (int) x);
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(3, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void test_mapToDouble() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        DoubleMatrix result = m.mapToDouble(x -> (double) x);
        assertEquals(1.0, result.get(0, 0), 0.0);
        assertEquals(2.0, result.get(0, 1), 0.0);
        assertEquals(3.0, result.get(1, 0), 0.0);
        assertEquals(4.0, result.get(1, 1), 0.0);
    }

    @Test
    public void test_mapToObj() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        Matrix<String> result = m.mapToObj(x -> String.valueOf(x), String.class);
        assertEquals("1", result.get(0, 0));
        assertEquals("2", result.get(0, 1));
        assertEquals("3", result.get(1, 0));
        assertEquals("4", result.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void test_fill_value() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.fill(9L);
        assertEquals(9L, m.get(0, 0));
        assertEquals(9L, m.get(0, 1));
        assertEquals(9L, m.get(1, 0));
        assertEquals(9L, m.get(1, 1));
    }

    @Test
    public void test_fill_array() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        m.fill(new long[][] { { 9L, 8L }, { 7L, 6L } });
        assertEquals(9L, m.get(0, 0));
        assertEquals(8L, m.get(0, 1));
        assertEquals(7L, m.get(1, 0));
        assertEquals(6L, m.get(1, 1));
    }

    @Test
    public void test_fill_withOffset() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        m.fill(1, 1, new long[][] { { 99L } });
        assertEquals(1L, m.get(0, 0));
        assertEquals(99L, m.get(1, 1));
        assertEquals(9L, m.get(2, 2));
    }

    @Test
    public void test_fill_withOffset_arrayLargerThanMatrix() {
        // Test that fill() gracefully handles source arrays larger than the target matrix
        // by copying only what fits (as documented in the javadoc)
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        m.fill(0, 0, new long[][] { { 9L, 8L, 7L } });   // Source array has 3 elements, matrix has 2
        // Should copy only the first 2 elements that fit
        assertEquals(9L, m.get(0, 0));
        assertEquals(8L, m.get(0, 1));
    }

    // ============ Copy Tests ============

    @Test
    public void test_copy() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix copy = m.copy();
        assertEquals(m.rows, copy.rows);
        assertEquals(m.cols, copy.cols);
        assertEquals(1L, copy.get(0, 0));
        copy.set(0, 0, 99L);
        assertEquals(1L, m.get(0, 0));   // Original unchanged
    }

    @Test
    public void test_copy_withRowRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        LongMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(3L, copy.get(0, 0));
        assertEquals(6L, copy.get(1, 1));
    }

    @Test
    public void test_copy_withFullRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        LongMatrix copy = m.copy(1, 3, 1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(5L, copy.get(0, 0));
        assertEquals(9L, copy.get(1, 1));
    }

    @Test
    public void test_copy_invalidRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 5));
    }

    // ============ Extend Tests ============

    @Test
    public void test_extend() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1L, extended.get(0, 0));
        assertEquals(0L, extended.get(2, 2));
    }

    @Test
    public void test_extend_withDefaultValue() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix extended = m.extend(2, 3, 99L);
        assertEquals(2, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1L, extended.get(0, 0));
        assertEquals(99L, extended.get(1, 1));
        assertEquals(99L, extended.get(0, 2));
    }

    @Test
    public void test_extend_smaller() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix result = m.extend(1, 2);
        assertEquals(1, result.rows);
        assertEquals(2, result.cols);
    }

    @Test
    public void test_extend_directional() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals(1L, extended.get(1, 1));
        assertEquals(0L, extended.get(0, 0));
    }

    @Test
    public void test_extend_directional_withDefaultValue() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix extended = m.extend(1, 1, 1, 1, 99L);
        assertEquals(3, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals(1L, extended.get(1, 1));
        assertEquals(99L, extended.get(0, 0));
    }

    // ============ Reverse and Flip Tests ============

    @Test
    public void test_reverseH() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        m.reverseH();
        assertEquals(3L, m.get(0, 0));
        assertEquals(2L, m.get(0, 1));
        assertEquals(1L, m.get(0, 2));
    }

    @Test
    public void test_reverseV() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        m.reverseV();
        assertEquals(5L, m.get(0, 0));
        assertEquals(3L, m.get(1, 0));
        assertEquals(1L, m.get(2, 0));
    }

    @Test
    public void test_flipH() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix flipped = m.flipH();
        assertEquals(3L, flipped.get(0, 0));
        assertEquals(2L, flipped.get(0, 1));
        assertEquals(1L, flipped.get(0, 2));
        // Original unchanged
        assertEquals(1L, m.get(0, 0));
    }

    @Test
    public void test_flipV() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        LongMatrix flipped = m.flipV();
        assertEquals(5L, flipped.get(0, 0));
        assertEquals(3L, flipped.get(1, 0));
        assertEquals(1L, flipped.get(2, 0));
        // Original unchanged
        assertEquals(1L, m.get(0, 0));
    }

    // ============ Rotate Tests ============

    @Test
    public void test_rotate90() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3L, rotated.get(0, 0));
        assertEquals(1L, rotated.get(0, 1));
        assertEquals(4L, rotated.get(1, 0));
        assertEquals(2L, rotated.get(1, 1));
    }

    @Test
    public void test_rotate180() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix rotated = m.rotate180();
        assertEquals(4L, rotated.get(0, 0));
        assertEquals(3L, rotated.get(0, 1));
        assertEquals(2L, rotated.get(1, 0));
        assertEquals(1L, rotated.get(1, 1));
    }

    @Test
    public void test_rotate270() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(2L, rotated.get(0, 0));
        assertEquals(4L, rotated.get(0, 1));
        assertEquals(1L, rotated.get(1, 0));
        assertEquals(3L, rotated.get(1, 1));
    }

    // ============ Transpose Test ============

    @Test
    public void test_transpose() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1L, transposed.get(0, 0));
        assertEquals(4L, transposed.get(0, 1));
        assertEquals(2L, transposed.get(1, 0));
        assertEquals(5L, transposed.get(1, 1));
    }

    @Test
    public void test_transpose_square() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix transposed = m.transpose();
        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1L, transposed.get(0, 0));
        assertEquals(3L, transposed.get(0, 1));
    }

    // ============ Reshape Test ============

    @Test
    public void test_reshape() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        LongMatrix reshaped = m.reshape(3, 2);
        assertEquals(3, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals(1L, reshaped.get(0, 0));
        assertEquals(2L, reshaped.get(0, 1));
        assertEquals(3L, reshaped.get(1, 0));
        assertEquals(4L, reshaped.get(1, 1));
    }

    @Test
    public void test_reshape_expandWithZeroFill() {
        // Test that reshape() can expand to larger dimensions, filling new cells with zeros
        // as documented in the javadoc
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix reshaped = m.reshape(3, 3);   // Expand from 2x2 (4 elements) to 3x3 (9 elements)
        assertEquals(3, reshaped.rows);
        assertEquals(3, reshaped.cols);
        // Verify original elements are preserved
        assertEquals(1L, reshaped.get(0, 0));
        assertEquals(2L, reshaped.get(0, 1));
        assertEquals(3L, reshaped.get(0, 2));
        assertEquals(4L, reshaped.get(1, 0));
        // Verify new cells are filled with zeros
        assertEquals(0L, reshaped.get(1, 1));
        assertEquals(0L, reshaped.get(1, 2));
        assertEquals(0L, reshaped.get(2, 0));
        assertEquals(0L, reshaped.get(2, 1));
        assertEquals(0L, reshaped.get(2, 2));
    }

    // ============ Repelem Test ============

    @Test
    public void test_repelem() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix result = m.repelem(2, 2);
        assertEquals(4, result.rows);
        assertEquals(4, result.cols);
        assertEquals(1L, result.get(0, 0));
        assertEquals(1L, result.get(0, 1));
        assertEquals(1L, result.get(1, 0));
        assertEquals(1L, result.get(1, 1));
        assertEquals(2L, result.get(0, 2));
    }

    @Test
    public void test_repelem_invalidRepeats() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
    }

    // ============ Repmat Test ============

    @Test
    public void test_repmat() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix result = m.repmat(2, 2);
        assertEquals(2, result.rows);
        assertEquals(4, result.cols);
        assertEquals(1L, result.get(0, 0));
        assertEquals(2L, result.get(0, 1));
        assertEquals(1L, result.get(0, 2));
        assertEquals(2L, result.get(0, 3));
    }

    @Test
    public void test_repmat_invalidRepeats() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
    }

    // ============ Flatten Test ============

    @Test
    public void test_flatten() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongList flat = m.flatten();
        assertEquals(4, flat.size());
        assertEquals(1L, flat.get(0));
        assertEquals(2L, flat.get(1));
        assertEquals(3L, flat.get(2));
        assertEquals(4L, flat.get(3));
    }

    // ============ FlatOp Test ============

    @Test
    public void test_flatOp() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        AtomicInteger count = new AtomicInteger(0);
        m.flatOp(row -> count.addAndGet(row.length));
        assertEquals(4, count.get());
    }

    // ============ Vstack and Hstack Tests ============

    @Test
    public void test_vstack() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L } });
        LongMatrix result = m1.vstack(m2);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1L, result.get(0, 0));
        assertEquals(3L, result.get(1, 0));
    }

    @Test
    public void test_vstack_incompatibleCols() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void test_hstack() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L }, { 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L }, { 4L } });
        LongMatrix result = m1.hstack(m2);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1L, result.get(0, 0));
        assertEquals(3L, result.get(0, 1));
    }

    @Test
    public void test_hstack_incompatibleRows() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L }, { 4L } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Operations Tests ============

    @Test
    public void test_add() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix result = m1.add(m2);
        assertEquals(6L, result.get(0, 0));
        assertEquals(8L, result.get(0, 1));
        assertEquals(10L, result.get(1, 0));
        assertEquals(12L, result.get(1, 1));
    }

    @Test
    public void test_add_incompatibleDimensions() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void test_subtract() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix result = m1.subtract(m2);
        assertEquals(4L, result.get(0, 0));
        assertEquals(4L, result.get(0, 1));
        assertEquals(4L, result.get(1, 0));
        assertEquals(4L, result.get(1, 1));
    }

    @Test
    public void test_subtract_incompatibleDimensions() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void test_multiply() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 2L, 0L }, { 1L, 2L } });
        LongMatrix result = m1.multiply(m2);
        assertEquals(4L, result.get(0, 0));
        assertEquals(4L, result.get(0, 1));
        assertEquals(10L, result.get(1, 0));
        assertEquals(8L, result.get(1, 1));
    }

    @Test
    public void test_multiply_incompatibleDimensions() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
    }

    // ============ Boxed Test ============

    @Test
    public void test_boxed() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        Matrix<Long> boxed = m.boxed();
        assertEquals(2, boxed.rows);
        assertEquals(2, boxed.cols);
        assertEquals(1L, boxed.get(0, 0));
        assertEquals(4L, boxed.get(1, 1));
    }

    // ============ Conversion Tests ============

    @Test
    public void test_toFloatMatrix() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        FloatMatrix result = m.toFloatMatrix();
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1.0f, result.get(0, 0), 0.0f);
        assertEquals(4.0f, result.get(1, 1), 0.0f);
    }

    @Test
    public void test_toDoubleMatrix() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        DoubleMatrix result = m.toDoubleMatrix();
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1.0, result.get(0, 0), 0.0);
        assertEquals(4.0, result.get(1, 1), 0.0);
    }

    // ============ ZipWith Tests ============

    @Test
    public void test_zipWith_binary() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 5L, 6L }, { 7L, 8L } });
        LongMatrix result = m1.zipWith(m2, (a, b) -> a * b);
        assertEquals(5L, result.get(0, 0));
        assertEquals(12L, result.get(0, 1));
        assertEquals(21L, result.get(1, 0));
        assertEquals(32L, result.get(1, 1));
    }

    @Test
    public void test_zipWith_ternary() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 3L, 4L } });
        LongMatrix m3 = LongMatrix.of(new long[][] { { 5L, 6L } });
        LongMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
        assertEquals(9L, result.get(0, 0));
        assertEquals(12L, result.get(0, 1));
    }

    // ============ Stream Tests ============

    @Test
    public void test_streamH() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long sum = m.streamH().sum();
        assertEquals(10L, sum);
    }

    @Test
    public void test_streamH_byRowIndex() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long sum = m.streamH(0).sum();
        assertEquals(3L, sum);
    }

    @Test
    public void test_streamH_withRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        long sum = m.streamH(1, 3).sum();
        assertEquals(18L, sum);
    }

    @Test
    public void test_streamV() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long sum = m.streamV().sum();
        assertEquals(10L, sum);
    }

    @Test
    public void test_streamV_byColumnIndex() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long sum = m.streamV(0).sum();
        assertEquals(4L, sum);
    }

    @Test
    public void test_streamV_withRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L } });
        long sum = m.streamV(1, 3).sum();
        assertEquals(16L, sum);
    }

    @Test
    public void test_streamLU2RD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long sum = m.streamLU2RD().sum();
        assertEquals(5L, sum);
    }

    @Test
    public void test_streamRU2LD() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long sum = m.streamRU2LD().sum();
        assertEquals(5L, sum);
    }

    @Test
    public void test_streamR() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long count = m.streamR().count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamR_withRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L }, { 5L, 6L } });
        long count = m.streamR(1, 3).count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamC() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        long count = m.streamC().count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamC_withRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L } });
        long count = m.streamC(1, 3).count();
        assertEquals(2, count);
    }

    // ============ ForEach Tests ============

    @Test
    public void test_forEach() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        AtomicInteger count = new AtomicInteger(0);
        m.forEach(x -> count.incrementAndGet());
        assertEquals(4, count.get());
    }

    @Test
    public void test_forEach_withRange() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L, 3L }, { 4L, 5L, 6L }, { 7L, 8L, 9L } });
        AtomicInteger count = new AtomicInteger(0);
        m.forEach(1, 3, 1, 3, x -> count.incrementAndGet());
        assertEquals(4, count.get());
    }

    // ============ Utility Tests ============

    @Test
    public void test_println() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        String result = m.println();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void test_hashCode_consistent() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void test_equals_same() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        assertEquals(m1, m2);
    }

    @Test
    public void test_equals_different() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        LongMatrix m2 = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 5L } });
        assertNotEquals(m1, m2);
    }

    @Test
    public void test_equals_null() {
        LongMatrix m1 = LongMatrix.of(new long[][] { { 1L, 2L } });
        assertNotEquals(m1, null);
    }

    @Test
    public void test_toString() {
        LongMatrix m = LongMatrix.of(new long[][] { { 1L, 2L }, { 3L, 4L } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }
}
