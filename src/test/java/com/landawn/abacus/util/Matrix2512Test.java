package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.Nullable;
import com.landawn.abacus.util.stream.Stream;

@Tag("2512")
public class Matrix2512Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void test_constructor_withValidArray() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals("a", m.get(0, 0));
        assertEquals("d", m.get(1, 1));
    }

    @Test
    public void test_constructor_withNullArray_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix<>((String[][]) null));
    }

    @Test
    public void test_constructor_withEmptyArray() {
        Matrix<String> m = new Matrix<>(new String[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void test_constructor_withSingleElement() {
        Matrix<Integer> m = new Matrix<>(new Integer[][] { { 42 } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals(42, m.get(0, 0));
    }

    // ============ Factory Method Tests ============

    @Test
    public void test_of_withValidArray() {
        String[][] arr = { { "x", "y", "z" }, { "a", "b", "c" } };
        Matrix<String> m = Matrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(3, m.cols);
        assertEquals("x", m.get(0, 0));
        assertEquals("c", m.get(1, 2));
    }

    @Test
    public void test_of_withSingleRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "one", "two", "three" } });
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
    }

    @Test
    public void test_of_withSingleColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "a" }, { "b" }, { "c" } });
        assertEquals(3, m.rows);
        assertEquals(1, m.cols);
    }

    @Test
    public void test_repeat_withNonNullElement() {
        Matrix<String> m = Matrix.repeat("X", 5, String.class);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals("X", m.get(0, i));
        }
    }

    @Test
    public void test_repeat_withNullElement() {
        Matrix<String> m = Matrix.repeat(null, 3, String.class);
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
        for (int i = 0; i < 3; i++) {
            assertNull(m.get(0, i));
        }
    }

    @Test
    public void test_repeatNonNull_withValidElement() {
        Matrix<Integer> m = Matrix.repeatNonNull(99, 4);
        assertEquals(1, m.rows);
        assertEquals(4, m.cols);
        for (int i = 0; i < 4; i++) {
            assertEquals(99, m.get(0, i));
        }
    }

    @Test
    public void test_repeatNonNull_withNullElement_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> Matrix.repeatNonNull(null, 3));
    }

    @Test
    public void test_diagonalLU2RD_createsMainDiagonal() {
        String[] diag = { "A", "B", "C" };
        Matrix<String> m = Matrix.diagonalLU2RD(diag);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals("A", m.get(0, 0));
        assertEquals("B", m.get(1, 1));
        assertEquals("C", m.get(2, 2));
        assertNull(m.get(0, 1));
        assertNull(m.get(1, 0));
    }

    @Test
    public void test_diagonalRU2LD_createsAntiDiagonal() {
        Integer[] diag = { 1, 2, 3 };
        Matrix<Integer> m = Matrix.diagonalRU2LD(diag);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 2));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 0));
        assertNull(m.get(0, 0));
    }

    @Test
    public void test_diagonal_withBothDiagonals() {
        Integer[] lu = { 1, 2, 3 };
        Integer[] ru = { 7, 8, 9 };
        Matrix<Integer> m = Matrix.diagonal(lu, ru);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));   // Main diagonal takes precedence
        assertEquals(3, m.get(2, 2));
        assertEquals(7, m.get(0, 2));
        assertEquals(9, m.get(2, 0));
    }

    @Test
    public void test_diagonal_withDifferentLengths_throwsException() {
        Integer[] lu = { 1, 2 };
        Integer[] ru = { 3, 4, 5 };
        assertThrows(IllegalArgumentException.class, () -> Matrix.diagonal(lu, ru));
    }

    @Test
    public void test_diagonal_withBothNull_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> Matrix.diagonal(null, null));
    }

    // ============ Component Type Test ============

    @Test
    public void test_componentType_returnsElementClass() {
        Matrix<String> m = Matrix.of(new String[][] { { "a" } });
        assertEquals(String.class, m.componentType());

        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 1 } });
        assertEquals(Integer.class, m2.componentType());
    }

    // ============ Get/Set Tests ============

    @Test
    public void test_get_withIndices() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        assertEquals("a", m.get(0, 0));
        assertEquals("e", m.get(1, 1));
        assertEquals("f", m.get(1, 2));
    }

    @Test
    public void test_get_withPoint() {
        String[][] arr = { { "x", "y" }, { "z", "w" } };
        Matrix<String> m = new Matrix<>(arr);
        Point p = Point.of(1, 0);
        assertEquals("z", m.get(p));
    }

    @Test
    public void test_get_canReturnNull() {
        String[][] arr = { { "a", null }, { null, "d" } };
        Matrix<String> m = new Matrix<>(arr);
        assertNull(m.get(0, 1));
        assertNull(m.get(1, 0));
    }

    @Test
    public void test_set_withIndices() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        m.set(0, 1, "NEW");
        assertEquals("NEW", m.get(0, 1));
    }

    @Test
    public void test_set_withPoint() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Point p = Point.of(1, 1);
        m.set(p, "UPDATED");
        assertEquals("UPDATED", m.get(p));
    }

    @Test
    public void test_set_withNull() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        m.set(0, 0, null);
        assertNull(m.get(0, 0));
    }

    // ============ Directional Navigation Tests ============

    @Test
    public void test_upOf_withElementAbove() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Nullable<String> result = m.upOf(1, 0);
        assertTrue(result.isPresent());
        assertEquals("a", result.get());
    }

    @Test
    public void test_upOf_atTopRow_returnsEmpty() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Nullable<String> result = m.upOf(0, 0);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_downOf_withElementBelow() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Nullable<String> result = m.downOf(0, 1);
        assertTrue(result.isPresent());
        assertEquals("d", result.get());
    }

    @Test
    public void test_downOf_atBottomRow_returnsEmpty() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Nullable<String> result = m.downOf(1, 1);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_leftOf_withElementToLeft() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Nullable<String> result = m.leftOf(0, 1);
        assertTrue(result.isPresent());
        assertEquals("a", result.get());
    }

    @Test
    public void test_leftOf_atLeftColumn_returnsEmpty() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Nullable<String> result = m.leftOf(1, 0);
        assertFalse(result.isPresent());
    }

    @Test
    public void test_rightOf_withElementToRight() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Nullable<String> result = m.rightOf(1, 0);
        assertTrue(result.isPresent());
        assertEquals("d", result.get());
    }

    @Test
    public void test_rightOf_atRightColumn_returnsEmpty() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Nullable<String> result = m.rightOf(0, 1);
        assertFalse(result.isPresent());
    }

    // ============ Row/Column Access Tests ============

    @Test
    public void test_row_returnsCorrectRow() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        String[] row = m.row(1);
        assertArrayEquals(new String[] { "d", "e", "f" }, row);
    }

    @Test
    public void test_row_outOfBounds_throwsException() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b" } });
        assertThrows(IllegalArgumentException.class, () -> m.row(5));
    }

    @Test
    public void test_column_returnsCorrectColumn() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        String[] col = m.column(1);
        assertArrayEquals(new String[] { "b", "e" }, col);
    }

    @Test
    public void test_column_outOfBounds_throwsException() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b" } });
        assertThrows(IllegalArgumentException.class, () -> m.column(5));
    }

    @Test
    public void test_setRow_updatesRow() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        m.setRow(0, new String[] { "X", "Y" });
        assertEquals("X", m.get(0, 0));
        assertEquals("Y", m.get(0, 1));
    }

    @Test
    public void test_setRow_wrongLength_throwsException() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "x", "y", "z" }));
    }

    @Test
    public void test_setColumn_updatesColumn() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        m.setColumn(1, new String[] { "X", "Y" });
        assertEquals("X", m.get(0, 1));
        assertEquals("Y", m.get(1, 1));
    }

    @Test
    public void test_setColumn_wrongLength_throwsException() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "x", "y", "z" }));
    }

    @Test
    public void test_updateRow_appliesFunction() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        m.updateRow(0, x -> x * 10);
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(0, 1));
        assertEquals(3, m.get(1, 0));
    }

    @Test
    public void test_updateColumn_appliesFunction() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        m.updateColumn(0, String::toUpperCase);
        assertEquals("A", m.get(0, 0));
        assertEquals("C", m.get(1, 0));
        assertEquals("b", m.get(0, 1));
    }

    // ============ Diagonal Tests ============

    @Test
    public void test_getLU2RD_returnsMainDiagonal() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } };
        Matrix<String> m = new Matrix<>(arr);
        String[] diag = m.getLU2RD();
        assertArrayEquals(new String[] { "a", "e", "i" }, diag);
    }

    @Test
    public void test_getLU2RD_nonSquare_throwsException() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b", "c" }, { "d", "e", "f" } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void test_setLU2RD_setsMainDiagonal() {
        Integer[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        Matrix<Integer> m = new Matrix<>(arr);
        m.setLU2RD(new Integer[] { 11, 22, 33 });
        assertEquals(11, m.get(0, 0));
        assertEquals(22, m.get(1, 1));
        assertEquals(33, m.get(2, 2));
    }

    @Test
    public void test_setLU2RD_wrongLength_throwsException() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new Integer[] { 1, 2, 3 }));
    }

    @Test
    public void test_updateLU2RD_appliesFunction() {
        Integer[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        Matrix<Integer> m = new Matrix<>(arr);
        m.updateLU2RD(x -> x * 100);
        assertEquals(100, m.get(0, 0));
        assertEquals(500, m.get(1, 1));
        assertEquals(900, m.get(2, 2));
    }

    @Test
    public void test_getRU2LD_returnsAntiDiagonal() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } };
        Matrix<String> m = new Matrix<>(arr);
        String[] diag = m.getRU2LD();
        assertArrayEquals(new String[] { "c", "e", "g" }, diag);
    }

    @Test
    public void test_setRU2LD_setsAntiDiagonal() {
        Integer[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        Matrix<Integer> m = new Matrix<>(arr);
        m.setRU2LD(new Integer[] { 11, 22, 33 });
        assertEquals(11, m.get(0, 2));
        assertEquals(22, m.get(1, 1));
        assertEquals(33, m.get(2, 0));
    }

    @Test
    public void test_updateRU2LD_appliesFunction() {
        Integer[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        Matrix<Integer> m = new Matrix<>(arr);
        m.updateRU2LD(x -> x + 1000);
        assertEquals(1003, m.get(0, 2));
        assertEquals(1005, m.get(1, 1));
        assertEquals(1007, m.get(2, 0));
    }

    // ============ Update/Replace Tests ============

    @Test
    public void test_updateAll_withUnaryOperator() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        m.updateAll(x -> x * 2);
        assertEquals(2, m.get(0, 0));
        assertEquals(4, m.get(0, 1));
        assertEquals(6, m.get(1, 0));
        assertEquals(8, m.get(1, 1));
    }

    @Test
    public void test_updateAll_withBiFunction() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        m.updateAll((i, j) -> i + ":" + j);
        assertEquals("0:0", m.get(0, 0));
        assertEquals("0:1", m.get(0, 1));
        assertEquals("1:0", m.get(1, 0));
        assertEquals("1:1", m.get(1, 1));
    }

    @Test
    public void test_replaceIf_withPredicate() {
        Integer[][] arr = { { 1, 2, 3 }, { 4, 5, 6 } };
        Matrix<Integer> m = new Matrix<>(arr);
        m.replaceIf(x -> x > 3, 99);
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(99, m.get(1, 0));
        assertEquals(99, m.get(1, 2));
    }

    @Test
    public void test_replaceIf_withBiPredicate() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        m.replaceIf((i, j) -> i == j, 0);
        assertEquals(0, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(0, m.get(1, 1));
    }

    // ============ Map Tests ============

    @Test
    public void test_map_createsNewMatrix() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        Matrix<Integer> result = m.map(x -> x * 3);
        assertEquals(3, result.get(0, 0));
        assertEquals(6, result.get(0, 1));
        assertEquals(9, result.get(1, 0));
        assertEquals(12, result.get(1, 1));
        assertEquals(1, m.get(0, 0));   // original unchanged
    }

    @Test
    public void test_map_withTypeConversion() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        Matrix<String> result = m.map(x -> "val" + x, String.class);
        assertEquals("val1", result.get(0, 0));
        assertEquals("val4", result.get(1, 1));
    }

    @Test
    public void test_mapToBoolean_createsBooleanMatrix() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        BooleanMatrix result = m.mapToBoolean(x -> x > 2);
        assertFalse(result.get(0, 0));
        assertFalse(result.get(0, 1));
        assertTrue(result.get(1, 0));
        assertTrue(result.get(1, 1));
    }

    @Test
    public void test_mapToByte_createsByteMatrix() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        ByteMatrix result = m.mapToByte(x -> (byte) x.intValue());
        assertEquals(1, result.get(0, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void test_mapToChar_createsCharMatrix() {
        String[][] arr = { { "A", "B" }, { "C", "D" } };
        Matrix<String> m = new Matrix<>(arr);
        CharMatrix result = m.mapToChar(x -> x.charAt(0));
        assertEquals('A', result.get(0, 0));
        assertEquals('D', result.get(1, 1));
    }

    @Test
    public void test_mapToShort_createsShortMatrix() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        ShortMatrix result = m.mapToShort(x -> (short) x.intValue());
        assertEquals(1, result.get(0, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void test_mapToInt_createsIntMatrix() {
        String[][] arr = { { "1", "2" }, { "3", "4" } };
        Matrix<String> m = new Matrix<>(arr);
        IntMatrix result = m.mapToInt(Integer::parseInt);
        assertEquals(1, result.get(0, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void test_mapToLong_createsLongMatrix() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        LongMatrix result = m.mapToLong(x -> x.longValue());
        assertEquals(1L, result.get(0, 0));
        assertEquals(4L, result.get(1, 1));
    }

    @Test
    public void test_mapToFloat_createsFloatMatrix() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        FloatMatrix result = m.mapToFloat(x -> x.floatValue());
        assertEquals(1.0f, result.get(0, 0));
        assertEquals(4.0f, result.get(1, 1));
    }

    @Test
    public void test_mapToDouble_createsDoubleMatrix() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        DoubleMatrix result = m.mapToDouble(x -> x.doubleValue());
        assertEquals(1.0, result.get(0, 0));
        assertEquals(4.0, result.get(1, 1));
    }

    // ============ Fill Tests ============

    @Test
    public void test_fill_withValue() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        m.fill("X");
        assertEquals("X", m.get(0, 0));
        assertEquals("X", m.get(0, 1));
        assertEquals("X", m.get(1, 0));
        assertEquals("X", m.get(1, 1));
    }

    @Test
    public void test_fill_withArray() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        String[][] b = { { "X", "Y" }, { "Z", "W" } };
        m.fill(b);
        assertEquals("X", m.get(0, 0));
        assertEquals("W", m.get(1, 1));
    }

    @Test
    public void test_fill_withArrayAndOffset() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } };
        Matrix<String> m = new Matrix<>(arr);
        String[][] b = { { "X", "Y" } };
        m.fill(1, 1, b);
        assertEquals("a", m.get(0, 0));
        assertEquals("X", m.get(1, 1));
        assertEquals("Y", m.get(1, 2));
    }

    // ============ Copy Tests ============

    @Test
    public void test_copy_createsIndependentCopy() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> copy = m.copy();
        assertEquals(m.rows, copy.rows);
        assertEquals(m.cols, copy.cols);
        assertEquals("a", copy.get(0, 0));

        copy.set(0, 0, "CHANGED");
        assertEquals("a", m.get(0, 0));   // original unchanged
    }

    @Test
    public void test_copy_withRowRange() {
        String[][] arr = { { "a", "b" }, { "c", "d" }, { "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> copy = m.copy(1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals("c", copy.get(0, 0));
        assertEquals("f", copy.get(1, 1));
    }

    @Test
    public void test_copy_withFullRange() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> copy = m.copy(0, 2, 1, 3);
        assertEquals(2, copy.rows);
        assertEquals(2, copy.cols);
        assertEquals("b", copy.get(0, 0));
        assertEquals("f", copy.get(1, 1));
    }

    // ============ Extend Tests ============

    @Test
    public void test_extend_increasesSize() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> extended = m.extend(3, 3);
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals("a", extended.get(0, 0));
        assertNull(extended.get(2, 2));
    }

    @Test
    public void test_extend_withDefaultValue() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> extended = m.extend(3, 3, "X");
        assertEquals("X", extended.get(2, 2));
    }

    @Test
    public void test_extend_withDirections() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> extended = m.extend(1, 1, 1, 1);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals("a", extended.get(1, 1));
    }

    @Test
    public void test_extend_withDirectionsAndDefault() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> extended = m.extend(1, 1, 1, 1, "Y");
        assertEquals("Y", extended.get(0, 0));
        assertEquals("a", extended.get(1, 1));
    }

    // ============ Reverse/Flip Tests ============

    @Test
    public void test_reverseH_reversesHorizontally() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        m.reverseH();
        assertEquals("c", m.get(0, 0));
        assertEquals("a", m.get(0, 2));
        assertEquals("f", m.get(1, 0));
    }

    @Test
    public void test_reverseV_reversesVertically() {
        String[][] arr = { { "a", "b" }, { "c", "d" }, { "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        m.reverseV();
        assertEquals("e", m.get(0, 0));
        assertEquals("f", m.get(0, 1));
        assertEquals("a", m.get(2, 0));
    }

    @Test
    public void test_flipH_createsNewHorizontallyFlipped() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> flipped = m.flipH();
        assertEquals("c", flipped.get(0, 0));
        assertEquals("a", m.get(0, 0));   // original unchanged
    }

    @Test
    public void test_flipV_createsNewVerticallyFlipped() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> flipped = m.flipV();
        assertEquals("c", flipped.get(0, 0));
        assertEquals("a", m.get(0, 0));   // original unchanged
    }

    // ============ Rotation Tests ============

    @Test
    public void test_rotate90_rotatesClockwise() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> rotated = m.rotate90();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals("c", rotated.get(0, 0));
        assertEquals("a", rotated.get(0, 1));
        assertEquals("d", rotated.get(1, 0));
        assertEquals("b", rotated.get(1, 1));
    }

    @Test
    public void test_rotate180_rotates180Degrees() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> rotated = m.rotate180();
        assertEquals("d", rotated.get(0, 0));
        assertEquals("c", rotated.get(0, 1));
        assertEquals("b", rotated.get(1, 0));
        assertEquals("a", rotated.get(1, 1));
    }

    @Test
    public void test_rotate270_rotatesCounterClockwise() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> rotated = m.rotate270();
        assertEquals("b", rotated.get(0, 0));
        assertEquals("d", rotated.get(0, 1));
        assertEquals("a", rotated.get(1, 0));
        assertEquals("c", rotated.get(1, 1));
    }

    // ============ Transpose Test ============

    @Test
    public void test_transpose_swapsRowsAndColumns() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals("a", transposed.get(0, 0));
        assertEquals("d", transposed.get(0, 1));
        assertEquals("f", transposed.get(2, 1));
    }

    // ============ Reshape Test ============

    @Test
    public void test_reshape_changesShape() {
        Integer[][] arr = { { 1, 2, 3, 4, 5, 6 } };
        Matrix<Integer> m = new Matrix<>(arr);
        Matrix<Integer> reshaped = m.reshape(2, 3);
        assertEquals(2, reshaped.rows);
        assertEquals(3, reshaped.cols);
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(6, reshaped.get(1, 2));
    }

    @Test
    public void test_reshape_incompatibleSize_fillsWithNull() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b", "c" } });
        Matrix<String> reshaped = m.reshape(2, 2);
        assertEquals(2, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals("a", reshaped.get(0, 0));
        assertEquals("b", reshaped.get(0, 1));
        assertEquals("c", reshaped.get(1, 0));
        assertNull(reshaped.get(1, 1));   // Not enough elements, should be null
    }

    // ============ Repelem Test ============

    @Test
    public void test_repelem_repeatsElements() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> repeated = m.repelem(2, 2);
        assertEquals(4, repeated.rows);
        assertEquals(4, repeated.cols);
        assertEquals("a", repeated.get(0, 0));
        assertEquals("a", repeated.get(1, 1));
        assertEquals("d", repeated.get(2, 2));
    }

    // ============ Repmat Test ============

    @Test
    public void test_repmat_repeatsMatrix() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Matrix<String> repeated = m.repmat(2, 2);
        assertEquals(4, repeated.rows);
        assertEquals(4, repeated.cols);
        assertEquals("a", repeated.get(0, 0));
        assertEquals("a", repeated.get(2, 2));
        assertEquals("d", repeated.get(3, 3));
    }

    // ============ Flatten Tests ============

    @Test
    public void test_flatten_returnsAllElements() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        List<String> flattened = m.flatten();
        assertEquals(6, flattened.size());
        assertEquals("a", flattened.get(0));
        assertEquals("f", flattened.get(5));
    }

    @Test
    public void test_flatOp_appliesOperationToEachRow() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        final int[] sum = { 0 };
        m.flatOp(row -> {
            for (Integer val : row) {
                sum[0] += val;
            }
        });
        assertEquals(10, sum[0]);
    }

    // ============ Stack Tests ============

    @Test
    public void test_vstack_stacksVertically() {
        String[][] arr1 = { { "a", "b" } };
        String[][] arr2 = { { "c", "d" } };
        Matrix<String> m1 = new Matrix<>(arr1);
        Matrix<String> m2 = new Matrix<>(arr2);
        Matrix<String> stacked = m1.vstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals("a", stacked.get(0, 0));
        assertEquals("c", stacked.get(1, 0));
    }

    @Test
    public void test_vstack_incompatibleColumns_throwsException() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "a", "b" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "c", "d", "e" } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void test_hstack_stacksHorizontally() {
        String[][] arr1 = { { "a" }, { "b" } };
        String[][] arr2 = { { "c" }, { "d" } };
        Matrix<String> m1 = new Matrix<>(arr1);
        Matrix<String> m2 = new Matrix<>(arr2);
        Matrix<String> stacked = m1.hstack(m2);
        assertEquals(2, stacked.rows);
        assertEquals(2, stacked.cols);
        assertEquals("a", stacked.get(0, 0));
        assertEquals("c", stacked.get(0, 1));
    }

    @Test
    public void test_hstack_incompatibleRows_throwsException() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "a" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "b" }, { "c" } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ Zip Tests ============

    @Test
    public void test_zipWith_withTwoMatrices() {
        Integer[][] arr1 = { { 1, 2 }, { 3, 4 } };
        Integer[][] arr2 = { { 5, 6 }, { 7, 8 } };
        Matrix<Integer> m1 = new Matrix<>(arr1);
        Matrix<Integer> m2 = new Matrix<>(arr2);
        Matrix<Integer> result = m1.zipWith(m2, (a, b) -> a + b);
        assertEquals(6, result.get(0, 0));
        assertEquals(12, result.get(1, 1));
    }

    // ============ Stream Tests ============

    @Test
    public void test_streamLU2RD_streamsDiagonal() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } };
        Matrix<String> m = new Matrix<>(arr);
        List<String> result = m.streamLU2RD().toList();
        assertEquals(Arrays.asList("a", "e", "i"), result);
    }

    @Test
    public void test_streamRU2LD_streamsAntiDiagonal() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" } };
        Matrix<String> m = new Matrix<>(arr);
        List<String> result = m.streamRU2LD().toList();
        assertEquals(Arrays.asList("c", "e", "g"), result);
    }

    @Test
    public void test_streamH_streamsAllElements() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        List<String> result = m.streamH().toList();
        assertEquals(Arrays.asList("a", "b", "c", "d"), result);
    }

    @Test
    public void test_streamH_withRowIndex() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        List<String> result = m.streamH(1).toList();
        assertEquals(Arrays.asList("d", "e", "f"), result);
    }

    @Test
    public void test_streamH_withRowRange() {
        String[][] arr = { { "a", "b" }, { "c", "d" }, { "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        List<String> result = m.streamH(1, 3).toList();
        assertEquals(Arrays.asList("c", "d", "e", "f"), result);
    }

    @Test
    public void test_streamV_streamsAllElementsVertically() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        List<String> result = m.streamV().toList();
        assertEquals(Arrays.asList("a", "c", "b", "d"), result);
    }

    @Test
    public void test_streamV_withColumnIndex() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        List<String> result = m.streamV(1).toList();
        assertEquals(Arrays.asList("b", "e"), result);
    }

    @Test
    public void test_streamV_withColumnRange() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        List<String> result = m.streamV(1, 3).toList();
        assertEquals(Arrays.asList("b", "e", "c", "f"), result);
    }

    @Test
    public void test_streamR_streamsRows() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Stream<Stream<String>> streams = m.streamR();
        assertEquals(2, streams.count());
    }

    @Test
    public void test_streamR_withRange() {
        String[][] arr = { { "a", "b" }, { "c", "d" }, { "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        Stream<Stream<String>> streams = m.streamR(1, 3);
        assertEquals(2, streams.count());
    }

    @Test
    public void test_streamC_streamsColumns() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Stream<Stream<String>> streams = m.streamC();
        assertEquals(2, streams.count());
    }

    @Test
    public void test_streamC_withRange() {
        String[][] arr = { { "a", "b", "c" }, { "d", "e", "f" } };
        Matrix<String> m = new Matrix<>(arr);
        Stream<Stream<String>> streams = m.streamC(1, 3);
        assertEquals(2, streams.count());
    }

    // ============ ForEach Test ============

    @Test
    public void test_forEach_iteratesAllElements() {
        Integer[][] arr = { { 1, 2 }, { 3, 4 } };
        Matrix<Integer> m = new Matrix<>(arr);
        final int[] sum = { 0 };
        m.forEach(val -> sum[0] += val);
        assertEquals(10, sum[0]);
    }

    // ============ Dataset Conversion Tests ============

    @Test
    public void test_toDatasetH_convertsToDataset() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Dataset ds = m.toDatasetH(Arrays.asList("col1", "col2"));
        assertNotNull(ds);
        assertEquals(2, ds.size());
    }

    @Test
    public void test_toDatasetV_convertsToDataset() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        Dataset ds = m.toDatasetV(Arrays.asList("col1", "col2"));
        assertNotNull(ds);
        assertEquals(2, ds.size());
    }

    // ============ Utility Tests ============

    @Test
    public void test_println_returnsString() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        String result = m.println();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void test_hashCode_consistentWithEquals() {
        String[][] arr1 = { { "a", "b" }, { "c", "d" } };
        String[][] arr2 = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m1 = new Matrix<>(arr1);
        Matrix<String> m2 = new Matrix<>(arr2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void test_equals_sameContent() {
        String[][] arr1 = { { "a", "b" }, { "c", "d" } };
        String[][] arr2 = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m1 = new Matrix<>(arr1);
        Matrix<String> m2 = new Matrix<>(arr2);
        assertEquals(m1, m2);
    }

    @Test
    public void test_equals_differentContent() {
        String[][] arr1 = { { "a", "b" }, { "c", "d" } };
        String[][] arr2 = { { "a", "b" }, { "c", "X" } };
        Matrix<String> m1 = new Matrix<>(arr1);
        Matrix<String> m2 = new Matrix<>(arr2);
        assertNotEquals(m1, m2);
    }

    @Test
    public void test_equals_differentDimensions() {
        String[][] arr1 = { { "a", "b" }, { "c", "d" } };
        String[][] arr2 = { { "a", "b", "c" } };
        Matrix<String> m1 = new Matrix<>(arr1);
        Matrix<String> m2 = new Matrix<>(arr2);
        assertNotEquals(m1, m2);
    }

    @Test
    public void test_toString_returnsStringRepresentation() {
        String[][] arr = { { "a", "b" }, { "c", "d" } };
        Matrix<String> m = new Matrix<>(arr);
        String result = m.toString();
        assertNotNull(result);
        assertTrue(result.contains("a"));
        assertTrue(result.contains("d"));
    }
}
