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
import com.landawn.abacus.util.u.OptionalInt;

@Tag("2512")
public class IntMatrix2512Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void test_constructor_withValidArray() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = new IntMatrix(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void test_constructor_withNullArray() {
        IntMatrix m = new IntMatrix(null);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_withEmptyArray() {
        IntMatrix m = new IntMatrix(new int[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_withSingleElement() {
        IntMatrix m = new IntMatrix(new int[][] { { 42 } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(42, m.get(0, 0));
    }

    // ============ Factory Method Tests ============

    @Test
    public void test_empty() {
        IntMatrix empty = IntMatrix.empty();
        assertEquals(0, empty.rows);
        assertEquals(0, empty.cols);
        assertTrue(empty.isEmpty());
        assertSame(IntMatrix.empty(), IntMatrix.empty());
    }

    @Test
    public void test_of_withValidArray() {
        int[][] arr = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void test_of_withNullArray() {
        IntMatrix m = IntMatrix.of((int[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_of_withEmptyArray() {
        IntMatrix m = IntMatrix.of(new int[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_of_withSingleRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3, 4, 5 } });
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
    }

    // ============ Create Method Tests ============

    @Test
    public void test_create_fromCharArray() {
        char[][] chars = { { 'A', 'B' }, { 'C', 'D' } };
        IntMatrix m = IntMatrix.create(chars);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(65, m.get(0, 0)); // ASCII 'A'
        assertEquals(68, m.get(1, 1)); // ASCII 'D'
    }

    @Test
    public void test_create_fromCharArray_empty() {
        IntMatrix m = IntMatrix.create(new char[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_create_fromCharArray_null() {
        IntMatrix m = IntMatrix.create((char[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_create_fromCharArray_nullFirstRow() {
        char[][] chars = { null, { 'A', 'B' } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.create(chars));
    }

    @Test
    public void test_create_fromCharArray_differentRowLengths() {
        char[][] chars = { { 'A', 'B' }, { 'C' } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.create(chars));
    }

    @Test
    public void test_create_fromByteArray() {
        byte[][] bytes = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.create(bytes);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void test_create_fromByteArray_empty() {
        IntMatrix m = IntMatrix.create(new byte[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_create_fromByteArray_null() {
        IntMatrix m = IntMatrix.create((byte[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_create_fromByteArray_nullFirstRow() {
        byte[][] bytes = { null, { 1, 2 } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.create(bytes));
    }

    @Test
    public void test_create_fromByteArray_differentRowLengths() {
        byte[][] bytes = { { 1, 2 }, { 3 } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.create(bytes));
    }

    @Test
    public void test_create_fromShortArray() {
        short[][] shorts = { { 1, 2 }, { 3, 4 } };
        IntMatrix m = IntMatrix.create(shorts);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void test_create_fromShortArray_empty() {
        IntMatrix m = IntMatrix.create(new short[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_create_fromShortArray_null() {
        IntMatrix m = IntMatrix.create((short[][]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_create_fromShortArray_nullFirstRow() {
        short[][] shorts = { null, { 1, 2 } };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.create(shorts));
    }

    // ============ Random and Repeat Tests ============

    @Test
    public void test_random() {
        IntMatrix m = IntMatrix.random(5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
    }

    @Test
    public void test_random_zeroLength() {
        IntMatrix m = IntMatrix.random(0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void test_repeat() {
        IntMatrix m = IntMatrix.repeat(42, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals(42, m.get(0, i));
        }
    }

    @Test
    public void test_repeat_zeroLength() {
        IntMatrix m = IntMatrix.repeat(42, 0);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    // ============ Range Tests ============

    @Test
    public void test_range() {
        IntMatrix m = IntMatrix.range(0, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void test_range_withStep() {
        IntMatrix m = IntMatrix.range(0, 10, 2);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new int[] { 0, 2, 4, 6, 8 }, m.row(0));
    }

    @Test
    public void test_range_empty() {
        IntMatrix m = IntMatrix.range(5, 5);
        assertEquals(1, m.rows);
        assertEquals(0, m.cols);
    }

    @Test
    public void test_rangeClosed() {
        IntMatrix m = IntMatrix.rangeClosed(0, 4);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        assertArrayEquals(new int[] { 0, 1, 2, 3, 4 }, m.row(0));
    }

    @Test
    public void test_rangeClosed_withStep() {
        IntMatrix m = IntMatrix.rangeClosed(0, 10, 2);
        assertEquals(1, m.rows);
        assertEquals(6, m.cols);
        assertArrayEquals(new int[] { 0, 2, 4, 6, 8, 10 }, m.row(0));
    }

    @Test
    public void test_rangeClosed_singleValue() {
        IntMatrix m = IntMatrix.rangeClosed(5, 5);
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(5, m.get(0, 0));
    }

    // ============ Diagonal Tests ============

    @Test
    public void test_diagonalLU2RD() {
        int[] diag = { 1, 2, 3 };
        IntMatrix m = IntMatrix.diagonalLU2RD(diag);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
    }

    @Test
    public void test_diagonalLU2RD_null() {
        IntMatrix m = IntMatrix.diagonalLU2RD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_diagonalRU2LD() {
        int[] diag = { 1, 2, 3 };
        IntMatrix m = IntMatrix.diagonalRU2LD(diag);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 2));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 0));
        assertEquals(0, m.get(0, 0));
    }

    @Test
    public void test_diagonalRU2LD_null() {
        IntMatrix m = IntMatrix.diagonalRU2LD(null);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_diagonal_both() {
        int[] lu2rd = { 1, 2, 3 };
        int[] ru2ld = { 4, 5, 6 };
        IntMatrix m = IntMatrix.diagonal(lu2rd, ru2ld);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertEquals(4, m.get(0, 2));
        assertEquals(6, m.get(2, 0));
    }

    @Test
    public void test_diagonal_differentLengths() {
        int[] lu2rd = { 1, 2 };
        int[] ru2ld = { 4, 5, 6 };
        assertThrows(IllegalArgumentException.class, () -> IntMatrix.diagonal(lu2rd, ru2ld));
    }

    @Test
    public void test_diagonal_bothNull() {
        IntMatrix m = IntMatrix.diagonal(null, null);
        assertTrue(m.isEmpty());
    }

    // ============ Unbox Test ============

    @Test
    public void test_unbox() {
        Matrix<Integer> boxed = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m = IntMatrix.unbox(boxed);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void test_unbox_withNulls() {
        Matrix<Integer> boxed = Matrix.of(new Integer[][] { { 1, null }, { null, 4 } });
        IntMatrix m = IntMatrix.unbox(boxed);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
    }

    // ============ Component Type Test ============

    @Test
    public void test_componentType() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertEquals(int.class, m.componentType());
    }

    // ============ Get and Set Tests ============

    @Test
    public void test_get_byIndices() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(4, m.get(1, 1));
    }

    @Test
    public void test_get_byPoint() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Point p = Point.of(0, 1);
        assertEquals(2, m.get(p));
    }

    @Test
    public void test_set_byIndices() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.set(0, 1, 9);
        assertEquals(9, m.get(0, 1));
    }

    @Test
    public void test_set_byPoint() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Point p = Point.of(1, 1);
        m.set(p, 9);
        assertEquals(9, m.get(p));
    }

    // ============ Directional Access Tests ============

    @Test
    public void test_upOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals(1, up.get());
    }

    @Test
    public void test_upOf_firstRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void test_downOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals(3, down.get());
    }

    @Test
    public void test_downOf_lastRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void test_leftOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals(1, left.get());
    }

    @Test
    public void test_leftOf_firstColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void test_rightOf() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals(2, right.get());
    }

    @Test
    public void test_rightOf_lastColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        OptionalInt right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    // ============ Row and Column Access Tests ============

    @Test
    public void test_row() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        int[] row = m.row(0);
        assertArrayEquals(new int[] { 1, 2 }, row);
    }

    @Test
    public void test_row_invalidIndex() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.row(5));
    }

    @Test
    public void test_column() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        int[] col = m.column(0);
        assertArrayEquals(new int[] { 1, 3 }, col);
    }

    @Test
    public void test_column_invalidIndex() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.column(5));
    }

    @Test
    public void test_setRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.setRow(0, new int[] { 9, 8 });
        assertArrayEquals(new int[] { 9, 8 }, m.row(0));
    }

    @Test
    public void test_setRow_invalidRowIndex() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(5, new int[] { 1, 2 }));
    }

    @Test
    public void test_setRow_invalidLength() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new int[] { 1 }));
    }

    @Test
    public void test_setColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.setColumn(0, new int[] { 9, 8 });
        assertArrayEquals(new int[] { 9, 8 }, m.column(0));
    }

    @Test
    public void test_setColumn_invalidColumnIndex() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(5, new int[] { 1 }));
    }

    @Test
    public void test_setColumn_invalidLength() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new int[] { 1 }));
    }

    // ============ Update Row and Column Tests ============

    @Test
    public void test_updateRow() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateRow(0, x -> x * 2);
        assertArrayEquals(new int[] { 2, 4 }, m.row(0));
        assertArrayEquals(new int[] { 3, 4 }, m.row(1));
    }

    @Test
    public void test_updateColumn() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateColumn(0, x -> x * 2);
        assertArrayEquals(new int[] { 2, 6 }, m.column(0));
        assertArrayEquals(new int[] { 2, 4 }, m.column(1));
    }

    // ============ Diagonal Get/Set Tests ============

    @Test
    public void test_getLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] diag = m.getLU2RD();
        assertArrayEquals(new int[] { 1, 5, 9 }, diag);
    }

    @Test
    public void test_getLU2RD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void test_setLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.setLU2RD(new int[] { 9, 8 });
        assertArrayEquals(new int[] { 9, 8 }, m.getLU2RD());
    }

    @Test
    public void test_setLU2RD_nonSquare() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new int[] { 9 }));
    }

    @Test
    public void test_setLU2RD_invalidLength() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new int[] { 9 }));
    }

    @Test
    public void test_updateLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateLU2RD(x -> x * 2);
        assertArrayEquals(new int[] { 2, 8 }, m.getLU2RD());
    }

    @Test
    public void test_getRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        int[] diag = m.getRU2LD();
        assertArrayEquals(new int[] { 3, 5, 7 }, diag);
    }

    @Test
    public void test_setRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.setRU2LD(new int[] { 9, 8 });
        assertArrayEquals(new int[] { 9, 8 }, m.getRU2LD());
    }

    @Test
    public void test_updateRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateRU2LD(x -> x * 2);
        int[] diag = m.getRU2LD();
        assertEquals(4, diag[0]);
        assertEquals(6, diag[1]);
    }

    // ============ Update All Tests ============

    @Test
    public void test_updateAll_unaryOperator() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateAll(x -> x * 2);
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(6, m.get(1, 0));
        assertEquals(8, m.get(1, 1));
    }

    @Test
    public void test_updateAll_biFunction() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.updateAll((i, j) -> (i + 1) * 10 + (j + 1));
        assertEquals(11, m.get(0, 0));
        assertEquals(12, m.get(0, 1));
        assertEquals(21, m.get(1, 0));
        assertEquals(22, m.get(1, 1));
    }

    // ============ Replace If Tests ============

    @Test
    public void test_replaceIf_predicate() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.replaceIf(x -> x > 2, 99);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(99, m.get(1, 0));
        assertEquals(99, m.get(1, 1));
    }

    @Test
    public void test_replaceIf_biPredicate() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.replaceIf((i, j) -> i == j, 99);
        assertEquals(99, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
        assertEquals(99, m.get(1, 1));
    }

    // ============ Map Tests ============

    @Test
    public void test_map() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m.map(x -> x * 2);
        assertEquals(2, result.get(0, 0));
        assertEquals(4, result.get(0, 1));
        assertEquals(6, result.get(1, 0));
        assertEquals(8, result.get(1, 1));
        // Original should be unchanged
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void test_mapToLong() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix result = m.mapToLong(x -> (long) x);
        assertEquals(1L, result.get(0, 0));
        assertEquals(2L, result.get(0, 1));
        assertEquals(3L, result.get(1, 0));
        assertEquals(4L, result.get(1, 1));
    }

    @Test
    public void test_mapToDouble() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        DoubleMatrix result = m.mapToDouble(x -> (double) x);
        assertEquals(1.0, result.get(0, 0), 0.0);
        assertEquals(2.0, result.get(0, 1), 0.0);
        assertEquals(3.0, result.get(1, 0), 0.0);
        assertEquals(4.0, result.get(1, 1), 0.0);
    }

    @Test
    public void test_mapToObj() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Matrix<String> result = m.mapToObj(x -> String.valueOf(x), String.class);
        assertEquals("1", result.get(0, 0));
        assertEquals("2", result.get(0, 1));
        assertEquals("3", result.get(1, 0));
        assertEquals("4", result.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void test_fill_value() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.fill(9);
        assertEquals(9, m.get(0, 0));
        assertEquals(9, m.get(0, 1));
        assertEquals(9, m.get(1, 0));
        assertEquals(9, m.get(1, 1));
    }

    @Test
    public void test_fill_array() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        m.fill(new int[][] { { 9, 8 }, { 7, 6 } });
        assertEquals(9, m.get(0, 0));
        assertEquals(8, m.get(0, 1));
        assertEquals(7, m.get(1, 0));
        assertEquals(6, m.get(1, 1));
    }

    @Test
    public void test_fill_withOffset() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.fill(1, 1, new int[][] { { 99 } });
        assertEquals(1, m.get(0, 0));
        assertEquals(99, m.get(1, 1));
        assertEquals(9, m.get(2, 2));
    }

    @Test
    public void test_fill_withOffset_invalidBounds() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.fill(0, 0, new int[][] { { 1, 2, 3 } }));
    }

    // ============ Copy Tests ============

    @Test
    public void test_copy() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix copy = m.copy();
        assertEquals(m.rows, copy.rows);
        assertEquals(m.cols, copy.cols);
        assertEquals(1, copy.get(0, 0));
        copy.set(0, 0, 99);
        assertEquals(1, m.get(0, 0)); // Original unchanged
    }

    @Test
    public void test_copy_withRowRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        IntMatrix copy = m.copy(1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(3, copy.get(0, 0));
        assertEquals(6, copy.get(1, 1));
    }

    @Test
    public void test_copy_withFullRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        IntMatrix copy = m.copy(1, 3, 1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals(5, copy.get(0, 0));
        assertEquals(9, copy.get(1, 1));
    }

    @Test
    public void test_copy_invalidRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 5));
    }

    // ============ Extend Tests ============

    @Test
    public void test_extend() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix extended = m.extend(3, 3);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1, extended.get(0, 0));
        assertEquals(0, extended.get(2, 2));
    }

    @Test
    public void test_extend_withDefaultValue() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix extended = m.extend(2, 3, 99);
        assertEquals(2, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals(1, extended.get(0, 0));
        assertEquals(99, extended.get(1, 1));
        assertEquals(99, extended.get(0, 2));
    }

    @Test
    public void test_extend_smaller() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix result = m.extend(1, 2);
        assertEquals(1, result.rows);
        assertEquals(2, result.cols);
    }

    @Test
    public void test_extend_directional() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix extended = m.extend(1, 1, 1, 1);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals(1, extended.get(1, 1));
        assertEquals(0, extended.get(0, 0));
    }

    @Test
    public void test_extend_directional_withDefaultValue() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix extended = m.extend(1, 1, 1, 1, 99);
        assertEquals(3, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals(1, extended.get(1, 1));
        assertEquals(99, extended.get(0, 0));
    }

    // ============ Reverse and Flip Tests ============

    @Test
    public void test_reverseH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        m.reverseH();
        assertEquals(3, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(1, m.get(0, 2));
    }

    @Test
    public void test_reverseV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        m.reverseV();
        assertEquals(5, m.get(0, 0));
        assertEquals(3, m.get(1, 0));
        assertEquals(1, m.get(2, 0));
    }

    @Test
    public void test_flipH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix flipped = m.flipH();
        assertEquals(3, flipped.get(0, 0));
        assertEquals(2, flipped.get(0, 1));
        assertEquals(1, flipped.get(0, 2));
        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    @Test
    public void test_flipV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        IntMatrix flipped = m.flipV();
        assertEquals(5, flipped.get(0, 0));
        assertEquals(3, flipped.get(1, 0));
        assertEquals(1, flipped.get(2, 0));
        // Original unchanged
        assertEquals(1, m.get(0, 0));
    }

    // ============ Rotate Tests ============

    @Test
    public void test_rotate90() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate90();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(3, rotated.get(0, 0));
        assertEquals(1, rotated.get(0, 1));
        assertEquals(4, rotated.get(1, 0));
        assertEquals(2, rotated.get(1, 1));
    }

    @Test
    public void test_rotate180() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate180();
        assertEquals(4, rotated.get(0, 0));
        assertEquals(3, rotated.get(0, 1));
        assertEquals(2, rotated.get(1, 0));
        assertEquals(1, rotated.get(1, 1));
    }

    @Test
    public void test_rotate270() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix rotated = m.rotate270();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals(2, rotated.get(0, 0));
        assertEquals(4, rotated.get(0, 1));
        assertEquals(1, rotated.get(1, 0));
        assertEquals(3, rotated.get(1, 1));
    }

    // ============ Transpose Test ============

    @Test
    public void test_transpose() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1, transposed.get(0, 0));
        assertEquals(4, transposed.get(0, 1));
        assertEquals(2, transposed.get(1, 0));
        assertEquals(5, transposed.get(1, 1));
    }

    @Test
    public void test_transpose_square() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix transposed = m.transpose();
        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals(1, transposed.get(0, 0));
        assertEquals(3, transposed.get(0, 1));
    }

    // ============ Reshape Test ============

    @Test
    public void test_reshape() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        IntMatrix reshaped = m.reshape(3, 2);
        assertEquals(3, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
        assertEquals(4, reshaped.get(1, 1));
    }

    @Test
    public void test_reshape_invalidSize() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.reshape(3, 3));
    }

    // ============ Repelem Test ============

    @Test
    public void test_repelem() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m.repelem(2, 2);
        assertEquals(4, result.rows);
        assertEquals(4, result.cols);
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(2, result.get(0, 2));
    }

    @Test
    public void test_repelem_invalidRepeats() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
    }

    // ============ Repmat Test ============

    @Test
    public void test_repmat() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix result = m.repmat(2, 2);
        assertEquals(2, result.rows);
        assertEquals(4, result.cols);
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(1, result.get(0, 2));
        assertEquals(2, result.get(0, 3));
    }

    @Test
    public void test_repmat_invalidRepeats() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
    }

    // ============ Flatten Test ============

    @Test
    public void test_flatten() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntList flat = m.flatten();
        assertEquals(4, flat.size());
        assertEquals(1, flat.get(0));
        assertEquals(2, flat.get(1));
        assertEquals(3, flat.get(2));
        assertEquals(4, flat.get(3));
    }

    // ============ FlatOp Test ============

    @Test
    public void test_flatOp() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        AtomicInteger count = new AtomicInteger(0);
        m.flatOp(row -> count.addAndGet(row.length));
        assertEquals(4, count.get());
    }

    // ============ Vstack and Hstack Tests ============

    @Test
    public void test_vstack() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4 } });
        IntMatrix result = m1.vstack(m2);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1, result.get(0, 0));
        assertEquals(3, result.get(1, 0));
    }

    @Test
    public void test_vstack_incompatibleCols() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void test_hstack() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1 }, { 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3 }, { 4 } });
        IntMatrix result = m1.hstack(m2);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1, result.get(0, 0));
        assertEquals(3, result.get(0, 1));
    }

    @Test
    public void test_hstack_incompatibleRows() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3 }, { 4 } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Arithmetic Operations Tests ============

    @Test
    public void test_add() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix result = m1.add(m2);
        assertEquals(6, result.get(0, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(10, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
    }

    @Test
    public void test_add_incompatibleDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void test_subtract() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix result = m1.subtract(m2);
        assertEquals(4, result.get(0, 0));
        assertEquals(4, result.get(0, 1));
        assertEquals(4, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void test_subtract_incompatibleDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void test_multiply() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 2, 0 }, { 1, 2 } });
        IntMatrix result = m1.multiply(m2);
        assertEquals(4, result.get(0, 0));
        assertEquals(4, result.get(0, 1));
        assertEquals(10, result.get(1, 0));
        assertEquals(8, result.get(1, 1));
    }

    @Test
    public void test_multiply_incompatibleDimensions() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3 } });
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
    }

    // ============ Boxed Test ============

    @Test
    public void test_boxed() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> boxed = m.boxed();
        assertEquals(2, boxed.rows);
        assertEquals(2, boxed.cols);
        assertEquals(1, boxed.get(0, 0));
        assertEquals(4, boxed.get(1, 1));
    }

    // ============ Conversion Tests ============

    @Test
    public void test_toLongMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix result = m.toLongMatrix();
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1L, result.get(0, 0));
        assertEquals(4L, result.get(1, 1));
    }

    @Test
    public void test_toFloatMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        FloatMatrix result = m.toFloatMatrix();
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1.0f, result.get(0, 0), 0.0f);
        assertEquals(4.0f, result.get(1, 1), 0.0f);
    }

    @Test
    public void test_toDoubleMatrix() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        DoubleMatrix result = m.toDoubleMatrix();
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals(1.0, result.get(0, 0), 0.0);
        assertEquals(4.0, result.get(1, 1), 0.0);
    }

    // ============ ZipWith Tests ============

    @Test
    public void test_zipWith_binary() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 5, 6 }, { 7, 8 } });
        IntMatrix result = m1.zipWith(m2, (a, b) -> a * b);
        assertEquals(5, result.get(0, 0));
        assertEquals(12, result.get(0, 1));
        assertEquals(21, result.get(1, 0));
        assertEquals(32, result.get(1, 1));
    }

    @Test
    public void test_zipWith_ternary() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 3, 4 } });
        IntMatrix m3 = IntMatrix.of(new int[][] { { 5, 6 } });
        IntMatrix result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);
        assertEquals(9, result.get(0, 0));
        assertEquals(12, result.get(0, 1));
    }

    // ============ Stream Tests ============

    @Test
    public void test_streamH() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        long sum = m.streamH().sum();
        assertEquals(10L, sum);
    }

    @Test
    public void test_streamH_byRowIndex() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        long sum = m.streamH(0).sum();
        assertEquals(3L, sum);
    }

    @Test
    public void test_streamH_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        long sum = m.streamH(1, 3).sum();
        assertEquals(18L, sum);
    }

    @Test
    public void test_streamV() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        long sum = m.streamV().sum();
        assertEquals(10L, sum);
    }

    @Test
    public void test_streamV_byColumnIndex() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        long sum = m.streamV(0).sum();
        assertEquals(4L, sum);
    }

    @Test
    public void test_streamV_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        long sum = m.streamV(1, 3).sum();
        assertEquals(16L, sum);
    }

    @Test
    public void test_streamLU2RD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        long sum = m.streamLU2RD().sum();
        assertEquals(5L, sum);
    }

    @Test
    public void test_streamRU2LD() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        long sum = m.streamRU2LD().sum();
        assertEquals(5L, sum);
    }

    @Test
    public void test_streamR() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        long count = m.streamR().count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamR_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        long count = m.streamR(1, 3).count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamC() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        long count = m.streamC().count();
        assertEquals(2, count);
    }

    @Test
    public void test_streamC_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 } });
        long count = m.streamC(1, 3).count();
        assertEquals(2, count);
    }

    // ============ ForEach Tests ============

    @Test
    public void test_forEach() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        AtomicInteger count = new AtomicInteger(0);
        m.forEach(x -> count.incrementAndGet());
        assertEquals(4, count.get());
    }

    @Test
    public void test_forEach_withRange() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        AtomicInteger count = new AtomicInteger(0);
        m.forEach(1, 3, 1, 3, x -> count.incrementAndGet());
        assertEquals(4, count.get());
    }

    // ============ Utility Tests ============

    @Test
    public void test_println() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        String result = m.println();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void test_hashCode_consistent() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void test_equals_same() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        assertEquals(m1, m2);
    }

    @Test
    public void test_equals_different() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        IntMatrix m2 = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 5 } });
        assertNotEquals(m1, m2);
    }

    @Test
    public void test_equals_null() {
        IntMatrix m1 = IntMatrix.of(new int[][] { { 1, 2 } });
        assertNotEquals(m1, null);
    }

    @Test
    public void test_toString() {
        IntMatrix m = IntMatrix.of(new int[][] { { 1, 2 }, { 3, 4 } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }
}
