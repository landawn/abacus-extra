package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.Nullable;
import com.landawn.abacus.util.stream.Stream;

/**
 * Comprehensive unit tests for Matrix class covering all public methods.
 */
@Tag("2510")
public class Matrix2510Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        String[][] arr = { { "A", "B" }, { "C", "D" } };
        Matrix<String> m = new Matrix<>(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals("A", m.get(0, 0));
        assertEquals("D", m.get(1, 1));
    }

    @Test
    public void testConstructor_withEmptyArray() {
        Matrix<String> m = new Matrix<>(new String[0][0]);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        Matrix<Integer> m = new Matrix<>(new Integer[][] { { 42 } });
        assertEquals(1, m.rowCount());
        assertEquals(1, m.columnCount());
        assertEquals(42, m.get(0, 0));
    }

    @Test
    public void testConstructor_withNullElements() {
        Matrix<String> m = new Matrix<>(new String[][] { { null, "B" }, { "C", null } });
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertNull(m.get(0, 0));
        assertEquals("B", m.get(0, 1));
    }

    // ============ Factory Method Tests - of() ============

    @Test
    public void testOf_withValidArray() {
        String[][] arr = { { "A", "B" }, { "C", "D" } };
        Matrix<String> m = Matrix.of(arr);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals("A", m.get(0, 0));
    }

    @Test
    public void testOf_withEmptyArray() {
        Matrix<String> m = Matrix.of(new String[0][0]);
        assertTrue(m.isEmpty());
    }

    // ============ Factory Method Tests - repeat() ============


    // ============ Factory Method Tests - diagonal ============

    @Test
    public void testDiagonalLU2RD() {
        Integer[] diag = { 1, 2, 3 };
        Matrix<Integer> m = Matrix.diagonalLU2RD(diag);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        assertNull(m.get(0, 1));
        assertNull(m.get(1, 0));
    }

    @Test
    public void testDiagonalLU2RD_emptyArray() {
        Matrix<String> m = Matrix.diagonalLU2RD(new String[0]);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testDiagonalRU2LD() {
        Integer[] diag = { 1, 2, 3 };
        Matrix<Integer> m = Matrix.diagonalRU2LD(diag);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        assertEquals(1, m.get(0, 2));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 0));
        assertNull(m.get(0, 0));
        assertNull(m.get(2, 2));
    }

    @Test
    public void testDiagonalRU2LD_emptyArray() {
        Matrix<String> m = Matrix.diagonalRU2LD(new String[0]);
        assertEquals(0, m.rowCount());
        assertEquals(0, m.columnCount());
    }

    @Test
    public void testDiagonal_bothDiagonals() {
        Integer[] lu2rd = { 1, 2, 3 };
        Integer[] ru2ld = { 7, 8, 9 };
        Matrix<Integer> m = Matrix.diagonal(lu2rd, ru2ld);
        assertEquals(3, m.rowCount());
        assertEquals(3, m.columnCount());
        // LU2RD diagonal
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
        assertEquals(3, m.get(2, 2));
        // RU2LD diagonal
        assertEquals(7, m.get(0, 2));
        assertEquals(9, m.get(2, 0));
        // LU2RD takes precedence at center
        assertEquals(2, m.get(1, 1));
    }

    @Test
    public void testDiagonal_onlyLU2RD() {
        Integer[] lu2rd = { 1, 2 };
        Matrix<Integer> m = Matrix.diagonal(lu2rd, null);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 0));
        assertEquals(2, m.get(1, 1));
    }

    @Test
    public void testDiagonal_onlyRU2LD() {
        Integer[] ru2ld = { 1, 2 };
        Matrix<Integer> m = Matrix.diagonal(null, ru2ld);
        assertEquals(2, m.rowCount());
        assertEquals(2, m.columnCount());
        assertEquals(1, m.get(0, 1));
        assertEquals(2, m.get(1, 0));
    }

    @Test
    public void testDiagonal_bothNull() {
        assertThrows(IllegalArgumentException.class, () -> Matrix.diagonal(null, null));
    }

    @Test
    public void testDiagonal_differentLengths() {
        Integer[] lu2rd = { 1, 2, 3 };
        Integer[] ru2ld = { 7, 8 };
        assertThrows(IllegalArgumentException.class, () -> Matrix.diagonal(lu2rd, ru2ld));
    }

    // ============ Basic Access Methods ============

    @Test
    public void testComponentType() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b" } });
        assertEquals(String.class, m.componentType());
    }

    @Test
    public void testComponentType_integer() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 } });
        assertEquals(Integer.class, m.componentType());
    }

    @Test
    public void testGet_validIndices() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertEquals("A", m.get(0, 0));
        assertEquals("B", m.get(0, 1));
        assertEquals("C", m.get(1, 0));
        assertEquals("D", m.get(1, 1));
    }

    @Test
    public void testGet_withPoint() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Point p = Point.of(1, 0);
        assertEquals("C", m.get(p));
    }

    @Test
    public void testSet_validIndices() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.set(0, 0, "X");
        m.set(1, 1, "Y");
        assertEquals("X", m.get(0, 0));
        assertEquals("Y", m.get(1, 1));
    }

    @Test
    public void testSet_withPoint() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Point p = Point.of(1, 0);
        m.set(p, "Z");
        assertEquals("Z", m.get(1, 0));
    }

    @Test
    public void testSet_nullValue() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.set(0, 0, null);
        assertNull(m.get(0, 0));
    }

    // ============ Adjacent Element Methods ============

    @Test
    public void testUpOf_validPosition() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Nullable<String> up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals("A", up.get());
    }

    @Test
    public void testUpOf_topEdge() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Nullable<String> up = m.upOf(0, 0);
        assertFalse(up.isPresent());
    }

    @Test
    public void testDownOf_validPosition() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Nullable<String> down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals("C", down.get());
    }

    @Test
    public void testDownOf_bottomEdge() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Nullable<String> down = m.downOf(1, 0);
        assertFalse(down.isPresent());
    }

    @Test
    public void testLeftOf_validPosition() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Nullable<String> left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals("A", left.get());
    }

    @Test
    public void testLeftOf_leftEdge() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Nullable<String> left = m.leftOf(0, 0);
        assertFalse(left.isPresent());
    }

    @Test
    public void testRightOf_validPosition() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Nullable<String> right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals("B", right.get());
    }

    @Test
    public void testRightOf_rightEdge() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Nullable<String> right = m.rightOf(0, 1);
        assertFalse(right.isPresent());
    }

    // ============ Row and Column Access ============

    @Test
    public void testRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String[] row = m.row(0);
        assertArrayEquals(new String[] { "A", "B" }, row);
    }

    @Test
    public void testRow_lastRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String[] row = m.row(1);
        assertArrayEquals(new String[] { "C", "D" }, row);
    }

    @Test
    public void testRow_invalidIndex() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(1));
    }

    @Test
    public void testColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String[] col = m.column(0);
        assertArrayEquals(new String[] { "A", "C" }, col);
    }

    @Test
    public void testColumn_lastColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String[] col = m.column(1);
        assertArrayEquals(new String[] { "B", "D" }, col);
    }

    @Test
    public void testColumn_invalidIndex() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.setRow(0, new String[] { "X", "Y" });
        assertArrayEquals(new String[] { "X", "Y" }, m.row(0));
        assertArrayEquals(new String[] { "C", "D" }, m.row(1));
    }

    @Test
    public void testSetRow_wrongLength() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "X" }));
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "X", "Y", "Z" }));
    }

    @Test
    public void testSetColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.setColumn(0, new String[] { "X", "Y" });
        assertArrayEquals(new String[] { "X", "Y" }, m.column(0));
        assertArrayEquals(new String[] { "B", "D" }, m.column(1));
    }

    @Test
    public void testSetColumn_wrongLength() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "X" }));
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "X", "Y", "Z" }));
    }

    @Test
    public void testUpdateRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
        m.updateRow(0, String::toUpperCase);
        assertEquals("A", m.get(0, 0));
        assertEquals("B", m.get(0, 1));
        assertEquals("c", m.get(1, 0));
    }

    @Test
    public void testUpdateColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
        m.updateColumn(0, String::toUpperCase);
        assertEquals("A", m.get(0, 0));
        assertEquals("b", m.get(0, 1));
        assertEquals("C", m.get(1, 0));
    }

    // ============ Diagonal Methods ============

    @Test
    public void testGetLU2RD() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Integer[] diag = m.getLU2RD();
        assertArrayEquals(new Integer[] { 1, 5, 9 }, diag);
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setLU2RD(new Integer[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 0));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 2));
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new Integer[] { 10, 20 }));
    }

    @Test
    public void testSetLU2RD_wrongLength() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new Integer[] { 10 }));
    }

    @Test
    public void testUpdateLU2RD() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateLU2RD(x -> x * 2);
        assertEquals(2, m.get(0, 0));
        assertEquals(10, m.get(1, 1));
        assertEquals(18, m.get(2, 2));
        assertEquals(2, m.get(0, 1));   // Non-diagonal unchanged
    }

    @Test
    public void testUpdateLU2RD_nonSquare() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.updateLU2RD(x -> x * 2));
    }

    @Test
    public void testGetRU2LD() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Integer[] diag = m.getRU2LD();
        assertArrayEquals(new Integer[] { 3, 5, 7 }, diag);
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.setRU2LD(new Integer[] { 10, 20, 30 });
        assertEquals(10, m.get(0, 2));
        assertEquals(20, m.get(1, 1));
        assertEquals(30, m.get(2, 0));
    }

    @Test
    public void testSetRU2LD_nonSquare() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.setRU2LD(new Integer[] { 10, 20 }));
    }

    @Test
    public void testSetRU2LD_wrongLength() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new Integer[] { 10 }));
    }

    @Test
    public void testUpdateRU2LD() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.updateRU2LD(x -> x * 2);
        assertEquals(6, m.get(0, 2));
        assertEquals(10, m.get(1, 1));
        assertEquals(14, m.get(2, 0));
        assertEquals(2, m.get(0, 1));   // Non-diagonal unchanged
    }

    @Test
    public void testUpdateRU2LD_nonSquare() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(x -> x * 2));
    }

    // ============ Update and Replace Methods ============

    @Test
    public void testUpdateAll_unaryOperator() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
        m.updateAll(x -> x.toUpperCase());
        assertEquals("A", m.get(0, 0));
        assertEquals("B", m.get(0, 1));
        assertEquals("C", m.get(1, 0));
        assertEquals("D", m.get(1, 1));
    }

    @Test
    public void testUpdateAll_unaryOperator_withNull() {
        Matrix<String> m = Matrix.of(new String[][] { { null, "b" }, { "c", null } });
        m.updateAll(x -> x == null ? "NULL" : x.toUpperCase());
        assertEquals("NULL", m.get(0, 0));
        assertEquals("B", m.get(0, 1));
        assertEquals("C", m.get(1, 0));
        assertEquals("NULL", m.get(1, 1));
    }

    @Test
    public void testUpdateAll_biFunction() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 0, 1 }, { 2, 3 } });
        m.updateAll((i, j) -> i * 10 + j);
        assertEquals(0, m.get(0, 0));
        assertEquals(1, m.get(0, 1));
        assertEquals(10, m.get(1, 0));
        assertEquals(11, m.get(1, 1));
    }

    @Test
    public void testReplaceIf_valuePredicate() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, -2, 3 }, { -4, 5, -6 } });
        m.replaceIf(x -> x < 0, 0);
        assertEquals(1, m.get(0, 0));
        assertEquals(0, m.get(0, 1));
        assertEquals(3, m.get(0, 2));
        assertEquals(0, m.get(1, 0));
        assertEquals(5, m.get(1, 1));
        assertEquals(0, m.get(1, 2));
    }

    @Test
    public void testReplaceIf_valuePredicate_nulls() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", null, "c" }, { null, "e", null } });
        m.replaceIf(x -> x == null, "EMPTY");
        assertEquals("a", m.get(0, 0));
        assertEquals("EMPTY", m.get(0, 1));
        assertEquals("c", m.get(0, 2));
        assertEquals("EMPTY", m.get(1, 0));
    }

    @Test
    public void testReplaceIf_positionPredicate() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.replaceIf((i, j) -> i == j, 0);   // Replace diagonal
        assertEquals(0, m.get(0, 0));
        assertEquals(2, m.get(0, 1));
        assertEquals(0, m.get(1, 1));
        assertEquals(0, m.get(2, 2));
    }

    @Test
    public void testReplaceIf_positionPredicate_upperTriangle() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        m.replaceIf((i, j) -> i < j, 0);
        assertEquals(1, m.get(0, 0));
        assertEquals(0, m.get(0, 1));
        assertEquals(0, m.get(0, 2));
        assertEquals(4, m.get(1, 0));
        assertEquals(5, m.get(1, 1));
        assertEquals(0, m.get(1, 2));
    }

    // ============ Map Methods ============

    @Test
    public void testMap_sameType() {
        Matrix<String> m = Matrix.of(new String[][] { { "a", "b" }, { "c", "d" } });
        Matrix<String> mapped = m.map(String::toUpperCase);
        assertEquals("A", mapped.get(0, 0));
        assertEquals("B", mapped.get(0, 1));
        assertEquals("C", mapped.get(1, 0));
        assertEquals("D", mapped.get(1, 1));
    }

    @Test
    public void testMap_differentType() {
        Matrix<String> m = Matrix.of(new String[][] { { "ab", "abc" }, { "a", "abcd" } });
        Matrix<Integer> lengths = m.map(String::length, Integer.class);
        assertEquals(2, lengths.get(0, 0));
        assertEquals(3, lengths.get(0, 1));
        assertEquals(1, lengths.get(1, 0));
        assertEquals(4, lengths.get(1, 1));
    }

    @Test
    public void testMapToBoolean() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { -1, 2 }, { 3, -4 } });
        BooleanMatrix result = m.mapToBoolean(x -> x > 0);
        assertFalse(result.get(0, 0));
        assertTrue(result.get(0, 1));
        assertTrue(result.get(1, 0));
        assertFalse(result.get(1, 1));
    }

    @Test
    public void testMapToByte() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        ByteMatrix result = m.mapToByte(Integer::byteValue);
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(3, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void testMapToChar() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 65, 66 }, { 67, 68 } });
        CharMatrix result = m.mapToChar(x -> (char) x.intValue());
        assertEquals('A', result.get(0, 0));
        assertEquals('B', result.get(0, 1));
        assertEquals('C', result.get(1, 0));
        assertEquals('D', result.get(1, 1));
    }

    @Test
    public void testMapToShort() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        ShortMatrix result = m.mapToShort(Integer::shortValue);
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(3, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void testMapToInt() {
        Matrix<String> m = Matrix.of(new String[][] { { "ab", "abc" }, { "a", "abcd" } });
        IntMatrix result = m.mapToInt(String::length);
        assertEquals(2, result.get(0, 0));
        assertEquals(3, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void testMapToLong() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        LongMatrix result = m.mapToLong(Integer::longValue);
        assertEquals(1L, result.get(0, 0));
        assertEquals(2L, result.get(0, 1));
        assertEquals(3L, result.get(1, 0));
        assertEquals(4L, result.get(1, 1));
    }

    @Test
    public void testMapToFloat() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        FloatMatrix result = m.mapToFloat(Integer::floatValue);
        assertEquals(1.0f, result.get(0, 0), 0.001f);
        assertEquals(2.0f, result.get(0, 1), 0.001f);
        assertEquals(3.0f, result.get(1, 0), 0.001f);
        assertEquals(4.0f, result.get(1, 1), 0.001f);
    }

    @Test
    public void testMapToDouble() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        DoubleMatrix result = m.mapToDouble(Integer::doubleValue);
        assertEquals(1.0, result.get(0, 0), 0.001);
        assertEquals(2.0, result.get(0, 1), 0.001);
        assertEquals(3.0, result.get(1, 0), 0.001);
        assertEquals(4.0, result.get(1, 1), 0.001);
    }

    // ============ Fill Methods ============

    @Test
    public void testFill_singleValue() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.fill("X");
        assertEquals("X", m.get(0, 0));
        assertEquals("X", m.get(0, 1));
        assertEquals("X", m.get(1, 0));
        assertEquals("X", m.get(1, 1));
    }

    @Test
    public void testFill_singleValue_null() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.fill((String) null);
        assertNull(m.get(0, 0));
        assertNull(m.get(0, 1));
        assertNull(m.get(1, 0));
        assertNull(m.get(1, 1));
    }

    @Test
    public void testFill_array() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String[][] newData = { { "X", "Y" }, { "Z", "W" } };
        m.fill(newData);
        assertEquals("X", m.get(0, 0));
        assertEquals("Y", m.get(0, 1));
        assertEquals("Z", m.get(1, 0));
        assertEquals("W", m.get(1, 1));
    }

    @Test
    public void testFill_array_partial() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        String[][] patch = { { "X", "Y" } };
        m.fill(patch);
        assertEquals("X", m.get(0, 0));
        assertEquals("Y", m.get(0, 1));
        assertEquals("C", m.get(0, 2));   // Unchanged
        assertEquals("D", m.get(1, 0));   // Unchanged
    }

    @Test
    public void testFill_array_withOffset() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        String[][] patch = { { "X", "Y" } };
        m.fill(1, 1, patch);
        assertEquals("A", m.get(0, 0));
        assertEquals("X", m.get(1, 1));
        assertEquals("Y", m.get(1, 2));
        assertEquals("G", m.get(2, 0));   // Unchanged
    }

    @Test
    public void testFill_array_withOffset_invalidIndices() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        String[][] patch = { { "X" } };
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
        assertThrows(IllegalArgumentException.class, () -> m.fill(0, -1, patch));
        assertThrows(IllegalArgumentException.class, () -> m.fill(2, 0, patch));
        assertThrows(IllegalArgumentException.class, () -> m.fill(0, 3, patch));
    }

    // ============ Copy Methods ============

    @Test
    public void testCopy() {
        Matrix<String> original = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> copy = original.copy();

        assertEquals(2, copy.rowCount());
        assertEquals(2, copy.columnCount());
        assertEquals("A", copy.get(0, 0));
        assertEquals("D", copy.get(1, 1));

        // Verify independence
        copy.set(0, 0, "X");
        assertEquals("A", original.get(0, 0));
        assertEquals("X", copy.get(0, 0));
    }

    @Test
    public void testCopy_emptyMatrix() {
        Matrix<String> empty = Matrix.of(new String[0][0]);
        Matrix<String> copy = empty.copy();
        assertTrue(copy.isEmpty());
    }

    @Test
    public void testCopy_rowRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
        Matrix<String> subset = m.copy(1, 3);

        assertEquals(2, subset.rowCount());
        assertEquals(2, subset.columnCount());
        assertEquals("C", subset.get(0, 0));
        assertEquals("F", subset.get(1, 1));
    }

    @Test
    public void testCopy_rowRange_singleRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
        Matrix<String> subset = m.copy(1, 2);

        assertEquals(1, subset.rowCount());
        assertEquals(2, subset.columnCount());
        assertEquals("C", subset.get(0, 0));
    }

    @Test
    public void testCopy_fullRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        Matrix<String> submatrix = m.copy(0, 2, 1, 3);

        assertEquals(2, submatrix.rowCount());
        assertEquals(2, submatrix.columnCount());
        assertEquals("B", submatrix.get(0, 0));
        assertEquals("C", submatrix.get(0, 1));
        assertEquals("E", submatrix.get(1, 0));
        assertEquals("F", submatrix.get(1, 1));
    }

    @Test
    public void testCopy_fullRange_singleElement() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> single = m.copy(1, 2, 1, 2);

        assertEquals(1, single.rowCount());
        assertEquals(1, single.columnCount());
        assertEquals("D", single.get(0, 0));
    }

    // ============ Extend Methods ============

    @Test
    public void testExtend_larger() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> extended = m.extend(3, 3);

        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(4, extended.get(1, 1));
        assertNull(extended.get(2, 2));
        assertNull(extended.get(0, 2));
    }

    @Test
    public void testExtend_larger_withDefault() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> extended = m.extend(3, 3, 0);

        assertEquals(3, extended.rowCount());
        assertEquals(3, extended.columnCount());
        assertEquals(1, extended.get(0, 0));
        assertEquals(4, extended.get(1, 1));
        assertEquals(0, extended.get(2, 2));
        assertEquals(0, extended.get(0, 2));
    }

    @Test
    public void testExtend_smaller() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        Matrix<Integer> truncated = m.extend(2, 2);

        assertEquals(2, truncated.rowCount());
        assertEquals(2, truncated.columnCount());
        assertEquals(1, truncated.get(0, 0));
        assertEquals(5, truncated.get(1, 1));
    }

    @Test
    public void testExtend_directionBased() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> extended = m.extend(1, 1, 1, 1);

        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertNull(extended.get(0, 0));   // New top row
        assertEquals(1, extended.get(1, 1));   // Original top-left
        assertEquals(4, extended.get(2, 2));   // Original bottom-right
    }

    @Test
    public void testExtend_directionBased_withDefault() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> extended = m.extend(1, 1, 1, 1, 99);

        assertEquals(4, extended.rowCount());
        assertEquals(4, extended.columnCount());
        assertEquals(99, extended.get(0, 0));   // New top row
        assertEquals(1, extended.get(1, 1));   // Original top-left
        assertEquals(4, extended.get(2, 2));   // Original bottom-right
    }

    // ============ Transformation Methods ============

    @Test
    public void testReverseH() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        m.reverseH();
        assertEquals("C", m.get(0, 0));
        assertEquals("B", m.get(0, 1));
        assertEquals("A", m.get(0, 2));
        assertEquals("F", m.get(1, 0));
    }

    @Test
    public void testReverseV() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
        m.reverseV();
        assertEquals("E", m.get(0, 0));
        assertEquals("F", m.get(0, 1));
        assertEquals("C", m.get(1, 0));
        assertEquals("A", m.get(2, 0));
    }

    @Test
    public void testFlipH() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        Matrix<String> flipped = m.flipH();

        assertEquals("C", flipped.get(0, 0));
        assertEquals("B", flipped.get(0, 1));
        assertEquals("A", flipped.get(0, 2));
        assertEquals("F", flipped.get(1, 0));
        // Original unchanged
        assertEquals("A", m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
        Matrix<String> flipped = m.flipV();

        assertEquals("E", flipped.get(0, 0));
        assertEquals("F", flipped.get(0, 1));
        assertEquals("C", flipped.get(1, 0));
        assertEquals("A", flipped.get(2, 0));
        // Original unchanged
        assertEquals("A", m.get(0, 0));
    }

    @Test
    public void testRotate90() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        Matrix<String> rotated = m.rotate90();

        assertEquals(3, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals("D", rotated.get(0, 0));
        assertEquals("A", rotated.get(0, 1));
        assertEquals("F", rotated.get(2, 0));
        assertEquals("C", rotated.get(2, 1));
    }

    @Test
    public void testRotate180() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        Matrix<String> rotated = m.rotate180();

        assertEquals(2, rotated.rowCount());
        assertEquals(3, rotated.columnCount());
        assertEquals("F", rotated.get(0, 0));
        assertEquals("E", rotated.get(0, 1));
        assertEquals("D", rotated.get(0, 2));
        assertEquals("C", rotated.get(1, 0));
    }

    @Test
    public void testRotate270() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        Matrix<String> rotated = m.rotate270();

        assertEquals(3, rotated.rowCount());
        assertEquals(2, rotated.columnCount());
        assertEquals("C", rotated.get(0, 0));
        assertEquals("F", rotated.get(0, 1));
        assertEquals("A", rotated.get(2, 0));
        assertEquals("D", rotated.get(2, 1));
    }

    @Test
    public void testTranspose() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        Matrix<String> transposed = m.transpose();

        assertEquals(3, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals("A", transposed.get(0, 0));
        assertEquals("D", transposed.get(0, 1));
        assertEquals("B", transposed.get(1, 0));
        assertEquals("E", transposed.get(1, 1));
        assertEquals("C", transposed.get(2, 0));
        assertEquals("F", transposed.get(2, 1));
    }

    @Test
    public void testTranspose_squareMatrix() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> transposed = m.transpose();

        assertEquals(2, transposed.rowCount());
        assertEquals(2, transposed.columnCount());
        assertEquals("A", transposed.get(0, 0));
        assertEquals("C", transposed.get(0, 1));
        assertEquals("B", transposed.get(1, 0));
        assertEquals("D", transposed.get(1, 1));
    }

    // ============ Reshape and Repeat Methods ============

    @Test
    public void testReshape_singleParam() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Matrix<Integer> reshaped = m.reshape(2);

        assertEquals(3, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
        assertEquals(6, reshaped.get(2, 1));
    }

    @Test
    public void testReshape_twoParams() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Matrix<Integer> reshaped = m.reshape(3, 2);

        assertEquals(3, reshaped.rowCount());
        assertEquals(2, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(2, reshaped.get(0, 1));
        assertEquals(3, reshaped.get(1, 0));
        assertEquals(6, reshaped.get(2, 1));
    }

    @Test
    public void testReshape_twoParams_needsPadding() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Matrix<Integer> reshaped = m.reshape(2, 4);

        assertEquals(2, reshaped.rowCount());
        assertEquals(4, reshaped.columnCount());
        assertEquals(1, reshaped.get(0, 0));
        assertEquals(4, reshaped.get(0, 3));
        assertEquals(5, reshaped.get(1, 0));
        assertEquals(6, reshaped.get(1, 1));
        assertNull(reshaped.get(1, 2));   // Padding
    }

    @Test
    public void testRepelem() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> repeated = m.repelem(2, 2);

        assertEquals(4, repeated.rowCount());
        assertEquals(4, repeated.columnCount());
        assertEquals("A", repeated.get(0, 0));
        assertEquals("A", repeated.get(0, 1));
        assertEquals("A", repeated.get(1, 0));
        assertEquals("A", repeated.get(1, 1));
        assertEquals("D", repeated.get(2, 2));
        assertEquals("D", repeated.get(3, 3));
    }

    @Test
    public void testRepelem_invalidArgs() {
        Matrix<String> m = Matrix.of(new String[][] { { "A" } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> repeated = m.repmat(2, 2);

        assertEquals(4, repeated.rowCount());
        assertEquals(4, repeated.columnCount());
        assertEquals("A", repeated.get(0, 0));
        assertEquals("B", repeated.get(0, 1));
        assertEquals("A", repeated.get(0, 2));   // Tiled
        assertEquals("A", repeated.get(2, 0));   // Tiled
        assertEquals("D", repeated.get(3, 3));
    }

    @Test
    public void testRepmat_invalidArgs() {
        Matrix<String> m = Matrix.of(new String[][] { { "A" } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    // ============ Flatten and Stream Methods ============

    @Test
    public void testFlatten() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        List<String> flat = m.flatten();

        assertEquals(4, flat.size());
        assertEquals("A", flat.get(0));
        assertEquals("B", flat.get(1));
        assertEquals("C", flat.get(2));
        assertEquals("D", flat.get(3));
    }

    @Test
    public void testFlatten_emptyMatrix() {
        Matrix<String> m = Matrix.of(new String[0][0]);
        List<String> flat = m.flatten();
        assertTrue(flat.isEmpty());
    }

    @Test
    public void testFlatOp() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 3, 1 }, { 4, 2 } });
        List<Integer> captured = new java.util.ArrayList<>();
        m.flatOp(arr -> {
            for (Integer val : arr) {
                captured.add(val);
            }
        });
        assertEquals(4, captured.size());
    }

    // ============ Stack and Zip Methods ============

    @Test
    public void testVstack() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "C", "D" } });
        Matrix<String> stacked = m1.vstack(m2);

        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals("A", stacked.get(0, 0));
        assertEquals("B", stacked.get(0, 1));
        assertEquals("C", stacked.get(1, 0));
        assertEquals("D", stacked.get(1, 1));
    }

    @Test
    public void testVstack_differentColCount() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "C" } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A" }, { "B" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "C" }, { "D" } });
        Matrix<String> stacked = m1.hstack(m2);

        assertEquals(2, stacked.rowCount());
        assertEquals(2, stacked.columnCount());
        assertEquals("A", stacked.get(0, 0));
        assertEquals("C", stacked.get(0, 1));
        assertEquals("B", stacked.get(1, 0));
        assertEquals("D", stacked.get(1, 1));
    }

    @Test
    public void testHstack_differentRowCount() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A" }, { "B" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "C" } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    @Test
    public void testZipWith_twoMatrices_sameType() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
        Matrix<Integer> result = m1.zipWith(m2, (a, b) -> a + b);

        assertEquals(11, result.get(0, 0));
        assertEquals(22, result.get(0, 1));
        assertEquals(33, result.get(1, 0));
        assertEquals(44, result.get(1, 1));
    }

    @Test
    public void testZipWith_twoMatrices_differentType() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> result = m1.zipWith(m2, (a, b) -> a + b, String.class);

        assertEquals("1A", result.get(0, 0));
        assertEquals("2B", result.get(0, 1));
        assertEquals("3C", result.get(1, 0));
        assertEquals("4D", result.get(1, 1));
    }

    @Test
    public void testZipWith_threeMatrices_sameType() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200 } });
        Matrix<Integer> result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);

        assertEquals(111, result.get(0, 0));
        assertEquals(222, result.get(0, 1));
    }

    @Test
    public void testZipWith_threeMatrices_differentType() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200 } });
        Matrix<String> result = m1.zipWith(m2, m3, (a, b, c) -> "" + a + b + c, String.class);

        assertEquals("110100", result.get(0, 0));
        assertEquals("220200", result.get(0, 1));
    }

    // ============ Stream Methods ============

    @Test
    public void testStreamLU2RD() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Integer> diagonal = m.streamLU2RD().toList();

        assertEquals(3, diagonal.size());
        assertEquals(1, diagonal.get(0));
        assertEquals(5, diagonal.get(1));
        assertEquals(9, diagonal.get(2));
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.streamLU2RD().toList());
    }

    @Test
    public void testStreamRU2LD() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        List<Integer> diagonal = m.streamRU2LD().toList();

        assertEquals(3, diagonal.size());
        assertEquals(3, diagonal.get(0));
        assertEquals(5, diagonal.get(1));
        assertEquals(7, diagonal.get(2));
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } });
        assertThrows(IllegalStateException.class, () -> m.streamRU2LD().toList());
    }

    @Test
    public void testStreamH() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        List<String> elements = m.streamH().toList();

        assertEquals(4, elements.size());
        assertEquals("A", elements.get(0));
        assertEquals("B", elements.get(1));
        assertEquals("C", elements.get(2));
        assertEquals("D", elements.get(3));
    }

    @Test
    public void testStreamH_singleRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        List<String> row = m.streamH(1).toList();

        assertEquals(3, row.size());
        assertEquals("D", row.get(0));
        assertEquals("E", row.get(1));
        assertEquals("F", row.get(2));
    }

    @Test
    public void testStreamH_rowRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
        List<String> elements = m.streamH(1, 3).toList();

        assertEquals(4, elements.size());
        assertEquals("C", elements.get(0));
        assertEquals("D", elements.get(1));
        assertEquals("E", elements.get(2));
        assertEquals("F", elements.get(3));
    }

    @Test
    public void testStreamV() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        List<String> elements = m.streamV().toList();

        assertEquals(4, elements.size());
        assertEquals("A", elements.get(0));
        assertEquals("C", elements.get(1));
        assertEquals("B", elements.get(2));
        assertEquals("D", elements.get(3));
    }

    @Test
    public void testStreamV_singleColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        List<String> col = m.streamV(1).toList();

        assertEquals(2, col.size());
        assertEquals("B", col.get(0));
        assertEquals("E", col.get(1));
    }

    @Test
    public void testStreamV_columnRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        List<String> elements = m.streamV(1, 3).toList();

        assertEquals(4, elements.size());
        assertEquals("B", elements.get(0));
        assertEquals("E", elements.get(1));
        assertEquals("C", elements.get(2));
        assertEquals("F", elements.get(3));
    }

    @Test
    public void testStreamR() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        List<List<String>> rows = m.streamR().map(Stream::toList).toList();

        assertEquals(2, rows.size());
        assertEquals(2, rows.get(0).size());
        assertEquals("A", rows.get(0).get(0));
        assertEquals("B", rows.get(0).get(1));
        assertEquals("C", rows.get(1).get(0));
        assertEquals("D", rows.get(1).get(1));
    }

    @Test
    public void testStreamR_rowRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
        List<List<String>> rows = m.streamR(1, 3).map(Stream::toList).toList();

        assertEquals(2, rows.size());
        assertEquals("C", rows.get(0).get(0));
        assertEquals("E", rows.get(1).get(0));
    }

    @Test
    public void testStreamC() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        List<List<String>> cols = m.streamC().map(Stream::toList).toList();

        assertEquals(2, cols.size());
        assertEquals(2, cols.get(0).size());
        assertEquals("A", cols.get(0).get(0));
        assertEquals("C", cols.get(0).get(1));
        assertEquals("B", cols.get(1).get(0));
        assertEquals("D", cols.get(1).get(1));
    }

    @Test
    public void testStreamC_columnRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        List<List<String>> cols = m.streamC(1, 3).map(Stream::toList).toList();

        assertEquals(2, cols.size());
        assertEquals("B", cols.get(0).get(0));
        assertEquals("C", cols.get(1).get(0));
    }

    // ============ Println and toString ============

    @Test
    public void testPrintln() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String output = m.println();
        assertNotNull(output);
        assertTrue(output.contains("A"));
        assertTrue(output.contains("D"));
    }

    @Test
    public void testToString() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("A"));
        assertTrue(str.contains("D"));
    }

    // ============ Hashcode and Equals ============

    @Test
    public void testHashCode() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void testEquals_same() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertEquals(m1, m2);
    }

    @Test
    public void testEquals_different() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "X" } });
        assertNotSame(m1, m2);
    }
}
