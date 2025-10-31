package com.landawn.abacus.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.landawn.abacus.TestBase;
import com.landawn.abacus.util.Sheet.Point;
import com.landawn.abacus.util.u.Nullable;
import com.landawn.abacus.util.stream.Stream;

@Tag("2025")
public class Matrix2025Test extends TestBase {

    // ============ Constructor Tests ============

    @Test
    public void testConstructor_withValidArray() {
        String[][] arr = { { "A", "B" }, { "C", "D" } };
        Matrix<String> m = new Matrix<>(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals("A", m.get(0, 0));
        assertEquals("D", m.get(1, 1));
    }

    @Test
    public void testConstructor_withNullArray() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix<>(null));
    }

    @Test
    public void testConstructor_withEmptyArray() {
        Matrix<String> m = new Matrix<>(new String[0][0]);
        assertEquals(0, m.rows);
        assertEquals(0, m.cols);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testConstructor_withSingleElement() {
        Matrix<String> m = new Matrix<>(new String[][] { { "X" } });
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);
        assertEquals("X", m.get(0, 0));
    }

    @Test
    public void testConstructor_withNullElements() {
        String[][] arr = { { "A", null }, { null, "D" } };
        Matrix<String> m = new Matrix<>(arr);
        assertEquals("A", m.get(0, 0));
        assertNull(m.get(0, 1));
        assertNull(m.get(1, 0));
        assertEquals("D", m.get(1, 1));
    }

    // ============ Factory Method Tests ============

    @Test
    public void testOf_withValidArray() {
        String[][] arr = { { "A", "B" }, { "C", "D" } };
        Matrix<String> m = Matrix.of(arr);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals("A", m.get(0, 0));
    }

    @Test
    public void testOf_withNullArray() {
        Matrix<String> m = Matrix.of(new String[][] {});
        assertTrue(m.isEmpty());

        assertThrows(IllegalArgumentException.class, () -> Matrix.of((String[][]) null));
    }

    @Test
    public void testOf_withEmptyArray() {
        Matrix<String> m = Matrix.of(new String[0][0]);
        assertTrue(m.isEmpty());
    }

    @Test
    public void testOf_withVarargs() {
        Matrix<Integer> m = Matrix.of(new Integer[] { 1, 2, 3 }, new Integer[] { 4, 5, 6 });
        assertEquals(2, m.rows);
        assertEquals(3, m.cols);
        assertEquals(Integer.valueOf(1), m.get(0, 0));
        assertEquals(Integer.valueOf(6), m.get(1, 2));
    }

    @Test
    public void testRepeat_withNonNullElement() {
        Matrix<String> m = Matrix.repeat("X", 5, String.class);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals("X", m.get(0, i));
        }
    }

    @Test
    public void testRepeat_withNullElement() {
        Matrix<String> m = Matrix.repeat(null, 3, String.class);
        assertEquals(1, m.rows);
        assertEquals(3, m.cols);
        for (int i = 0; i < 3; i++) {
            assertNull(m.get(0, i));
        }
    }

    @Test
    public void testRepeat_deprecated() {
        Matrix<Double> m = Matrix.repeat(0.0, 5);
        assertEquals(1, m.rows);
        assertEquals(5, m.cols);
        for (int i = 0; i < 5; i++) {
            assertEquals(0.0, m.get(0, i));
        }
    }

    @Test
    public void testRepeat_deprecated_withNullElement() {
        assertThrows(IllegalArgumentException.class, () -> Matrix.repeat(null, 3));
    }

    @Test
    public void testRepeatNonNull() {
        Matrix<String> m = Matrix.repeatNonNull("Y", 4);
        assertEquals(1, m.rows);
        assertEquals(4, m.cols);
        for (int i = 0; i < 4; i++) {
            assertEquals("Y", m.get(0, i));
        }
    }

    @Test
    public void testRepeatNonNull_withNull() {
        assertThrows(IllegalArgumentException.class, () -> Matrix.repeatNonNull(null, 3));
    }

    @Test
    public void testDiagonalLU2RD() {
        Matrix<Integer> m = Matrix.diagonalLU2RD(new Integer[] { 1, 2, 3 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(Integer.valueOf(1), m.get(0, 0));
        assertEquals(Integer.valueOf(2), m.get(1, 1));
        assertEquals(Integer.valueOf(3), m.get(2, 2));
        assertNull(m.get(0, 1));
        assertNull(m.get(1, 0));
    }

    @Test
    public void testDiagonalRU2LD() {
        Matrix<Integer> m = Matrix.diagonalRU2LD(new Integer[] { 1, 2, 3 });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(Integer.valueOf(1), m.get(0, 2));
        assertEquals(Integer.valueOf(2), m.get(1, 1));
        assertEquals(Integer.valueOf(3), m.get(2, 0));
        assertNull(m.get(0, 0));
        assertNull(m.get(2, 2));
    }

    @Test
    public void testDiagonal_withBothDiagonals() {
        Matrix<String> m = Matrix.diagonal(new String[] { "A", "B", "C" }, new String[] { "X", "Y", "Z" });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals("A", m.get(0, 0));
        assertEquals("B", m.get(1, 1));
        assertEquals("C", m.get(2, 2));
        assertEquals("X", m.get(0, 2));
        assertEquals("Z", m.get(2, 0));
    }

    @Test
    public void testDiagonal_withOnlyMainDiagonal() {
        Matrix<String> m = Matrix.diagonal(new String[] { "A", "B", "C" }, null);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals("A", m.get(0, 0));
        assertEquals("B", m.get(1, 1));
        assertEquals("C", m.get(2, 2));
    }

    @Test
    public void testDiagonal_withOnlyAntiDiagonal() {
        Matrix<String> m = Matrix.diagonal(null, new String[] { "X", "Y", "Z" });
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals("X", m.get(0, 2));
        assertEquals("Y", m.get(1, 1));
        assertEquals("Z", m.get(2, 0));
    }

    @Test
    public void testDiagonal_withBothNull() {
        assertTrue(Matrix.diagonal(new String[] {}, new String[] {}).isEmpty());
        assertTrue(Matrix.diagonal(new String[] {}, null).isEmpty());
        assertTrue(Matrix.diagonal(null, new String[] {}).isEmpty());

        assertThrows(IllegalArgumentException.class, () -> Matrix.diagonal(null, null));
    }

    @Test
    public void testDiagonal_withDifferentLengths() {
        assertThrows(IllegalArgumentException.class, () -> Matrix.diagonal(new String[] { "A", "B" }, new String[] { "X", "Y", "Z" }));
    }

    // ============ Component Type Tests ============

    @Test
    public void testComponentType() {
        Matrix<String> m = Matrix.of(new String[][] { { "A" } });
        assertEquals(String.class, m.componentType());
    }

    @Test
    public void testComponentType_withInteger() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1 } });
        assertEquals(Integer.class, m.componentType());
    }

    // ============ Get/Set Tests ============

    @Test
    public void testGet() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        assertEquals("A", m.get(0, 0));
        assertEquals("E", m.get(1, 1));
        assertEquals("F", m.get(1, 2));
    }

    @Test
    public void testGet_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(2, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, 2));
    }

    @Test
    public void testGetWithPoint() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertEquals("A", m.get(Point.of(0, 0)));
        assertEquals("D", m.get(Point.of(1, 1)));
        assertEquals("B", m.get(Point.of(0, 1)));
    }

    @Test
    public void testSet() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.set(0, 0, "X");
        assertEquals("X", m.get(0, 0));

        m.set(1, 1, "Y");
        assertEquals("Y", m.get(1, 1));
    }

    @Test
    public void testSet_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, "X"));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(2, 0, "X"));
    }

    @Test
    public void testSetWithPoint() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.set(Point.of(0, 0), "Z");
        assertEquals("Z", m.get(Point.of(0, 0)));
    }

    @Test
    public void testSet_withNull() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.set(0, 0, null);
        assertNull(m.get(0, 0));
    }

    // ============ Adjacent Element Tests ============

    @Test
    public void testUpOf() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });

        Nullable<String> up = m.upOf(1, 0);
        assertTrue(up.isPresent());
        assertEquals("A", up.get());

        // Top row has no element above
        Nullable<String> empty = m.upOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testDownOf() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });

        Nullable<String> down = m.downOf(0, 0);
        assertTrue(down.isPresent());
        assertEquals("C", down.get());

        // Bottom row has no element below
        Nullable<String> empty = m.downOf(1, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testLeftOf() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });

        Nullable<String> left = m.leftOf(0, 1);
        assertTrue(left.isPresent());
        assertEquals("A", left.get());

        // Leftmost column has no element to the left
        Nullable<String> empty = m.leftOf(0, 0);
        assertFalse(empty.isPresent());
    }

    @Test
    public void testRightOf() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });

        Nullable<String> right = m.rightOf(0, 0);
        assertTrue(right.isPresent());
        assertEquals("B", right.get());

        // Rightmost column has no element to the right
        Nullable<String> empty = m.rightOf(0, 1);
        assertFalse(empty.isPresent());
    }

    // ============ Row/Column Operations Tests ============

    @Test
    public void testRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        assertArrayEquals(new String[] { "A", "B", "C" }, m.row(0));
        assertArrayEquals(new String[] { "D", "E", "F" }, m.row(1));
    }

    @Test
    public void testRow_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IllegalArgumentException.class, () -> m.row(-1));
        assertThrows(IllegalArgumentException.class, () -> m.row(2));
    }

    @Test
    public void testColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        assertArrayEquals(new String[] { "A", "D" }, m.column(0));
        assertArrayEquals(new String[] { "B", "E" }, m.column(1));
        assertArrayEquals(new String[] { "C", "F" }, m.column(2));
    }

    @Test
    public void testColumn_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IllegalArgumentException.class, () -> m.column(-1));
        assertThrows(IllegalArgumentException.class, () -> m.column(2));
    }

    @Test
    public void testSetRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.setRow(0, new String[] { "X", "Y" });
        assertArrayEquals(new String[] { "X", "Y" }, m.row(0));
        assertArrayEquals(new String[] { "C", "D" }, m.row(1)); // unchanged
    }

    @Test
    public void testSetRow_wrongSize() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "X" }));
        assertThrows(IllegalArgumentException.class, () -> m.setRow(0, new String[] { "X", "Y", "Z" }));
    }

    @Test
    public void testSetColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.setColumn(0, new String[] { "X", "Y" });
        assertArrayEquals(new String[] { "X", "Y" }, m.column(0));
        assertArrayEquals(new String[] { "B", "D" }, m.column(1)); // unchanged
    }

    @Test
    public void testSetColumn_wrongSize() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "X" }));
        assertThrows(IllegalArgumentException.class, () -> m.setColumn(0, new String[] { "X", "Y", "Z" }));
    }

    @Test
    public void testUpdateRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.updateRow(0, x -> x + "1");
        assertArrayEquals(new String[] { "A1", "B1" }, m.row(0));
        assertArrayEquals(new String[] { "C", "D" }, m.row(1)); // unchanged
    }

    @Test
    public void testUpdateColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.updateColumn(0, x -> x + "2");
        assertArrayEquals(new String[] { "A2", "C2" }, m.column(0));
        assertArrayEquals(new String[] { "B", "D" }, m.column(1)); // unchanged
    }

    // ============ Diagonal Operations Tests ============

    @Test
    public void testGetLU2RD() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        assertArrayEquals(new String[] { "A", "E", "I" }, m.getLU2RD());
    }

    @Test
    public void testGetLU2RD_nonSquare() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalStateException.class, () -> m.getLU2RD());
    }

    @Test
    public void testSetLU2RD() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        m.setLU2RD(new String[] { "X", "Y", "Z" });
        assertEquals("X", m.get(0, 0));
        assertEquals("Y", m.get(1, 1));
        assertEquals("Z", m.get(2, 2));
    }

    @Test
    public void testSetLU2RD_nonSquare() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalStateException.class, () -> m.setLU2RD(new String[] { "X" }));
    }

    @Test
    public void testSetLU2RD_arrayTooShort() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        assertThrows(IllegalArgumentException.class, () -> m.setLU2RD(new String[] { "X", "Y" }));
    }

    @Test
    public void testUpdateLU2RD() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        m.updateLU2RD(x -> x + "1");
        assertEquals("A1", m.get(0, 0));
        assertEquals("E1", m.get(1, 1));
        assertEquals("I1", m.get(2, 2));
        assertEquals("B", m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateLU2RD_nonSquare() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalStateException.class, () -> m.updateLU2RD(x -> x + "1"));
    }

    @Test
    public void testGetRU2LD() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        assertArrayEquals(new String[] { "C", "E", "G" }, m.getRU2LD());
    }

    @Test
    public void testGetRU2LD_nonSquare() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalStateException.class, () -> m.getRU2LD());
    }

    @Test
    public void testSetRU2LD() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        m.setRU2LD(new String[] { "X", "Y", "Z" });
        assertEquals("X", m.get(0, 2));
        assertEquals("Y", m.get(1, 1));
        assertEquals("Z", m.get(2, 0));
    }

    @Test
    public void testSetRU2LD_nonSquare() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalStateException.class, () -> m.setRU2LD(new String[] { "X" }));
    }

    @Test
    public void testSetRU2LD_arrayTooShort() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        assertThrows(IllegalArgumentException.class, () -> m.setRU2LD(new String[] { "X", "Y" }));
    }

    @Test
    public void testUpdateRU2LD() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        m.updateRU2LD(x -> x + "2");
        assertEquals("C2", m.get(0, 2));
        assertEquals("E2", m.get(1, 1));
        assertEquals("G2", m.get(2, 0));
        assertEquals("B", m.get(0, 1)); // unchanged
    }

    @Test
    public void testUpdateRU2LD_nonSquare() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalStateException.class, () -> m.updateRU2LD(x -> x + "2"));
    }

    // ============ Transformation Tests ============

    @Test
    public void testUpdateAll() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.updateAll(x -> x + "1");
        assertEquals("A1", m.get(0, 0));
        assertEquals("B1", m.get(0, 1));
        assertEquals("C1", m.get(1, 0));
        assertEquals("D1", m.get(1, 1));
    }

    @Test
    public void testUpdateAll_withIndices() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 0, 0 }, { 0, 0 } });
        m.updateAll((i, j) -> i * 10 + j);
        assertEquals(Integer.valueOf(0), m.get(0, 0));
        assertEquals(Integer.valueOf(1), m.get(0, 1));
        assertEquals(Integer.valueOf(10), m.get(1, 0));
        assertEquals(Integer.valueOf(11), m.get(1, 1));
    }

    @Test
    public void testReplaceIf() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "A" }, { "C", "A", "D" } });
        m.replaceIf(x -> "A".equals(x), "X");
        assertEquals("X", m.get(0, 0));
        assertEquals("B", m.get(0, 1));
        assertEquals("X", m.get(0, 2));
        assertEquals("C", m.get(1, 0));
        assertEquals("X", m.get(1, 1));
        assertEquals("D", m.get(1, 2));
    }

    @Test
    public void testReplaceIf_withIndices() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        m.replaceIf((i, j) -> i == j, "X"); // Replace diagonal
        assertEquals("X", m.get(0, 0));
        assertEquals("X", m.get(1, 1));
        assertEquals("X", m.get(2, 2));
        assertEquals("B", m.get(0, 1)); // unchanged
    }

    @Test
    public void testMap() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> result = m.map(x -> x + "1");
        assertEquals("A1", result.get(0, 0));
        assertEquals("B1", result.get(0, 1));
        assertEquals("C1", result.get(1, 0));
        assertEquals("D1", result.get(1, 1));

        // Original unchanged
        assertEquals("A", m.get(0, 0));
    }

    @Test
    public void testMap_withTypeChange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<Integer> result = m.map(x -> x.charAt(0) - 'A', Integer.class);
        assertEquals(Integer.valueOf(0), result.get(0, 0));
        assertEquals(Integer.valueOf(1), result.get(0, 1));
        assertEquals(Integer.valueOf(2), result.get(1, 0));
        assertEquals(Integer.valueOf(3), result.get(1, 1));
    }

    @Test
    public void testMapToBoolean() {
        Matrix<String> m = Matrix.of(new String[][] { { "true", "false" }, { "false", "true" } });
        BooleanMatrix result = m.mapToBoolean(x -> Boolean.parseBoolean(x));
        assertTrue(result.get(0, 0));
        assertFalse(result.get(0, 1));
        assertFalse(result.get(1, 0));
        assertTrue(result.get(1, 1));
    }

    @Test
    public void testMapToByte() {
        Matrix<String> m = Matrix.of(new String[][] { { "1", "2" }, { "3", "4" } });
        ByteMatrix result = m.mapToByte(x -> Byte.parseByte(x));
        assertEquals((byte) 1, result.get(0, 0));
        assertEquals((byte) 2, result.get(0, 1));
        assertEquals((byte) 3, result.get(1, 0));
        assertEquals((byte) 4, result.get(1, 1));
    }

    @Test
    public void testMapToChar() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        CharMatrix result = m.mapToChar(x -> x.charAt(0));
        assertEquals('A', result.get(0, 0));
        assertEquals('B', result.get(0, 1));
        assertEquals('C', result.get(1, 0));
        assertEquals('D', result.get(1, 1));
    }

    @Test
    public void testMapToShort() {
        Matrix<String> m = Matrix.of(new String[][] { { "10", "20" }, { "30", "40" } });
        ShortMatrix result = m.mapToShort(x -> Short.parseShort(x));
        assertEquals((short) 10, result.get(0, 0));
        assertEquals((short) 20, result.get(0, 1));
        assertEquals((short) 30, result.get(1, 0));
        assertEquals((short) 40, result.get(1, 1));
    }

    @Test
    public void testMapToInt() {
        Matrix<String> m = Matrix.of(new String[][] { { "1", "2" }, { "3", "4" } });
        IntMatrix result = m.mapToInt(x -> Integer.parseInt(x));
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(3, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
    }

    @Test
    public void testMapToLong() {
        Matrix<String> m = Matrix.of(new String[][] { { "100", "200" }, { "300", "400" } });
        LongMatrix result = m.mapToLong(x -> Long.parseLong(x));
        assertEquals(100L, result.get(0, 0));
        assertEquals(200L, result.get(0, 1));
        assertEquals(300L, result.get(1, 0));
        assertEquals(400L, result.get(1, 1));
    }

    @Test
    public void testMapToFloat() {
        Matrix<String> m = Matrix.of(new String[][] { { "1.5", "2.5" }, { "3.5", "4.5" } });
        FloatMatrix result = m.mapToFloat(x -> Float.parseFloat(x));
        assertEquals(1.5f, result.get(0, 0), 0.001f);
        assertEquals(2.5f, result.get(0, 1), 0.001f);
        assertEquals(3.5f, result.get(1, 0), 0.001f);
        assertEquals(4.5f, result.get(1, 1), 0.001f);
    }

    @Test
    public void testMapToDouble() {
        Matrix<String> m = Matrix.of(new String[][] { { "1.5", "2.5" }, { "3.5", "4.5" } });
        DoubleMatrix result = m.mapToDouble(x -> Double.parseDouble(x));
        assertEquals(1.5, result.get(0, 0), 0.001);
        assertEquals(2.5, result.get(0, 1), 0.001);
        assertEquals(3.5, result.get(1, 0), 0.001);
        assertEquals(4.5, result.get(1, 1), 0.001);
    }

    // ============ Fill Tests ============

    @Test
    public void testFill_withValue() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.fill("X");
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                assertEquals("X", m.get(i, j));
            }
        }
    }

    @Test
    public void testFill_withArray() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "A", "A" }, { "A", "A", "A" }, { "A", "A", "A" } });
        String[][] patch = { { "X", "Y" }, { "Z", "W" } };
        m.fill(patch);
        assertEquals("X", m.get(0, 0));
        assertEquals("Y", m.get(0, 1));
        assertEquals("Z", m.get(1, 0));
        assertEquals("W", m.get(1, 1));
        assertEquals("A", m.get(2, 2)); // unchanged
    }

    @Test
    public void testFill_withArrayAtPosition() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "A", "A" }, { "A", "A", "A" }, { "A", "A", "A" } });
        String[][] patch = { { "X", "Y" }, { "Z", "W" } };
        m.fill(1, 1, patch);
        assertEquals("A", m.get(0, 0)); // unchanged
        assertEquals("X", m.get(1, 1));
        assertEquals("Y", m.get(1, 2));
        assertEquals("Z", m.get(2, 1));
        assertEquals("W", m.get(2, 2));
    }

    @Test
    public void testFill_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String[][] patch = { { "X", "Y" }, { "Z", "W" } };
        assertThrows(IllegalArgumentException.class, () -> m.fill(-1, 0, patch));
    }

    // ============ Copy Tests ============

    @Test
    public void testCopy() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> copy = m.copy();
        assertEquals(m.rows, copy.rows);
        assertEquals(m.cols, copy.cols);
        assertEquals("A", copy.get(0, 0));

        // Modify copy shouldn't affect original
        copy.set(0, 0, "X");
        assertEquals("A", m.get(0, 0));
        assertEquals("X", copy.get(0, 0));
    }

    @Test
    public void testCopy_withRowRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        Matrix<String> subset = m.copy(1, 3);
        assertEquals(2, subset.rows);
        assertEquals(3, subset.cols);
        assertEquals("D", subset.get(0, 0));
        assertEquals("I", subset.get(1, 2));
    }

    @Test
    public void testCopy_withRowRange_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(2, 1));
    }

    @Test
    public void testCopy_withFullRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        Matrix<String> submatrix = m.copy(0, 2, 1, 3);
        assertEquals(2, submatrix.rows);
        assertEquals(2, submatrix.cols);
        assertEquals("B", submatrix.get(0, 0));
        assertEquals("F", submatrix.get(1, 1));
    }

    @Test
    public void testCopy_withFullRange_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.copy(0, 2, 0, 3));
    }

    // ============ Extend Tests ============

    @Test
    public void testExtend_larger() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> extended = m.extend(4, 4);
        assertEquals(4, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals("A", extended.get(0, 0));
        assertEquals("D", extended.get(1, 1));
        assertNull(extended.get(3, 3)); // new cells are null
    }

    @Test
    public void testExtend_smaller() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        Matrix<String> truncated = m.extend(2, 2);
        assertEquals(2, truncated.rows);
        assertEquals(2, truncated.cols);
        assertEquals("A", truncated.get(0, 0));
        assertEquals("E", truncated.get(1, 1));
    }

    @Test
    public void testExtend_withDefaultValue() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> extended = m.extend(3, 3, "X");
        assertEquals(3, extended.rows);
        assertEquals(3, extended.cols);
        assertEquals("A", extended.get(0, 0));
        assertEquals("X", extended.get(2, 2)); // new cell
    }

    @Test
    public void testExtend_withNegativeDimensions() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 3, "X"));
        assertThrows(IllegalArgumentException.class, () -> m.extend(3, -1, "X"));
    }

    @Test
    public void testExtend_directional() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        m.println();
        Matrix<String> extended = m.extend(1, 1, 2, 2);
        assertEquals(5, extended.rows); // 1 + 3 + 1
        assertEquals(7, extended.cols); // 2 + 3 + 2

        extended.println();

        // Original values at offset position
        assertEquals("A", extended.get(1, 2));
        assertEquals("F", extended.get(2, 4));

        // New cells are null
        assertNull(extended.get(0, 0));
    }

    @Test
    public void testExtend_directionalWithDefault() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        Matrix<String> extended = m.extend(1, 1, 1, 1, "X");
        assertEquals(5, extended.rows);
        assertEquals(5, extended.cols);

        // Check original values
        assertEquals("A", extended.get(1, 1));

        // Check new values
        assertEquals("X", extended.get(0, 0));
        assertEquals("X", extended.get(4, 4));
    }

    @Test
    public void testExtend_directionalWithNegativeValues() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IllegalArgumentException.class, () -> m.extend(-1, 1, 1, 1, "X"));
    }

    // ============ Reverse/Flip Tests ============

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

        // Original unchanged
        assertEquals("A", m.get(0, 0));
    }

    @Test
    public void testFlipV() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" }, { "E", "F" } });
        Matrix<String> flipped = m.flipV();
        assertEquals("E", flipped.get(0, 0));
        assertEquals("C", flipped.get(1, 0));
        assertEquals("A", flipped.get(2, 0));

        // Original unchanged
        assertEquals("A", m.get(0, 0));
    }

    // ============ Rotation Tests ============

    @Test
    public void testRotate90() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> rotated = m.rotate90();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals("C", rotated.get(0, 0));
        assertEquals("A", rotated.get(0, 1));
        assertEquals("D", rotated.get(1, 0));
        assertEquals("B", rotated.get(1, 1));
    }

    @Test
    public void testRotate180() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> rotated = m.rotate180();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals("D", rotated.get(0, 0));
        assertEquals("C", rotated.get(0, 1));
        assertEquals("B", rotated.get(1, 0));
        assertEquals("A", rotated.get(1, 1));
    }

    @Test
    public void testRotate270() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> rotated = m.rotate270();
        assertEquals(2, rotated.rows);
        assertEquals(2, rotated.cols);
        assertEquals("B", rotated.get(0, 0));
        assertEquals("D", rotated.get(0, 1));
        assertEquals("A", rotated.get(1, 0));
        assertEquals("C", rotated.get(1, 1));
    }

    // ============ Transpose Tests ============

    @Test
    public void testTranspose() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        Matrix<String> transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals("A", transposed.get(0, 0));
        assertEquals("D", transposed.get(0, 1));
        assertEquals("B", transposed.get(1, 0));
        assertEquals("E", transposed.get(1, 1));
        assertEquals("C", transposed.get(2, 0));
        assertEquals("F", transposed.get(2, 1));
    }

    @Test
    public void testTranspose_square() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> transposed = m.transpose();
        assertEquals(2, transposed.rows);
        assertEquals(2, transposed.cols);
        assertEquals("A", transposed.get(0, 0));
        assertEquals("C", transposed.get(0, 1));
        assertEquals("B", transposed.get(1, 0));
        assertEquals("D", transposed.get(1, 1));
    }

    // ============ Reshape Tests ============

    @Test
    public void testReshape() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        Matrix<String> reshaped = m.reshape(1, 9);
        assertEquals(1, reshaped.rows);
        assertEquals(9, reshaped.cols);
        String[] expected = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };
        for (int i = 0; i < 9; i++) {
            assertEquals(expected[i], reshaped.get(0, i));
        }
    }

    @Test
    public void testReshape_back() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        Matrix<String> reshaped = m.reshape(1, 9);
        Matrix<String> reshapedBack = reshaped.reshape(3, 3);
        assertEquals(m, reshapedBack);
    }

    @Test
    public void testReshape_empty() {
        Matrix<String> empty = Matrix.of(new String[0][0]);
        Matrix<String> reshaped = empty.reshape(2, 3);
        assertEquals(2, reshaped.rows);
        assertEquals(3, reshaped.cols);
    }

    // ============ Repeat Tests ============

    @Test
    public void testRepelem() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        Matrix<String> repeated = m.repelem(2, 3);
        assertEquals(2, repeated.rows);
        assertEquals(6, repeated.cols);

        // Check pattern
        assertEquals("A", repeated.get(0, 0));
        assertEquals("A", repeated.get(0, 1));
        assertEquals("A", repeated.get(0, 2));
        assertEquals("B", repeated.get(0, 3));
        assertEquals("B", repeated.get(0, 4));
        assertEquals("B", repeated.get(0, 5));
        assertEquals("A", repeated.get(1, 0)); // second row same as first
    }

    @Test
    public void testRepelem_invalidArguments() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalArgumentException.class, () -> m.repelem(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repelem(1, 0));
    }

    @Test
    public void testRepmat() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> repeated = m.repmat(2, 3);
        assertEquals(4, repeated.rows);
        assertEquals(6, repeated.cols);

        // Check pattern
        assertEquals("A", repeated.get(0, 0));
        assertEquals("B", repeated.get(0, 1));
        assertEquals("A", repeated.get(0, 2)); // repeat starts
        assertEquals("B", repeated.get(0, 3));

        assertEquals("C", repeated.get(1, 0));
        assertEquals("D", repeated.get(1, 1));

        // Check vertical repeat
        assertEquals("A", repeated.get(2, 0)); // vertical repeat starts
        assertEquals("B", repeated.get(2, 1));
    }

    @Test
    public void testRepmat_invalidArguments() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalArgumentException.class, () -> m.repmat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> m.repmat(1, 0));
    }

    // ============ Flatten Tests ============

    @Test
    public void testFlatten() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        List<String> flat = m.flatten();
        assertEquals(9, flat.size());
        List<String> expected = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I");
        assertEquals(expected, flat);
    }

    @Test
    public void testFlatten_empty() {
        Matrix<String> empty = Matrix.of(new String[0][0]);
        List<String> flat = empty.flatten();
        assertTrue(flat.isEmpty());
    }

    @Test
    public void testFlatOp() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        List<Integer> rowLengths = new ArrayList<>();
        m.flatOp(row -> {
            rowLengths.add(row.length);
        });
        assertEquals(1, rowLengths.size());
        assertEquals(9, rowLengths.get(0).intValue());
    }

    // ============ Stack Tests ============

    @Test
    public void testVstack() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "G", "H", "I" }, { "J", "K", "L" } });
        Matrix<String> stacked = m1.vstack(m2);

        assertEquals(4, stacked.rows);
        assertEquals(3, stacked.cols);
        assertEquals("A", stacked.get(0, 0));
        assertEquals("G", stacked.get(2, 0));
        assertEquals("L", stacked.get(3, 2));
    }

    @Test
    public void testVstack_differentColumnCounts() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "C", "D", "E" } });
        assertThrows(IllegalArgumentException.class, () -> m1.vstack(m2));
    }

    @Test
    public void testHstack() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "E", "F" }, { "G", "H" } });
        Matrix<String> stacked = m1.hstack(m2);

        assertEquals(2, stacked.rows);
        assertEquals(4, stacked.cols);
        assertEquals("A", stacked.get(0, 0));
        assertEquals("E", stacked.get(0, 2));
        assertEquals("H", stacked.get(1, 3));
    }

    @Test
    public void testHstack_differentRowCounts() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "E", "F" } });
        assertThrows(IllegalArgumentException.class, () -> m1.hstack(m2));
    }

    // ============ ZipWith Tests ============

    @Test
    public void testZipWith() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
        Matrix<Integer> result = m1.zipWith(m2, (a, b) -> a + b);

        assertEquals(Integer.valueOf(11), result.get(0, 0));
        assertEquals(Integer.valueOf(22), result.get(0, 1));
        assertEquals(Integer.valueOf(33), result.get(1, 0));
        assertEquals(Integer.valueOf(44), result.get(1, 1));
    }

    @Test
    public void testZipWith_withTypeChange() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
        Matrix<String> result = m1.zipWith(m2, (a, b) -> a + "+" + b, String.class);

        assertEquals("1+10", result.get(0, 0));
        assertEquals("2+20", result.get(0, 1));
        assertEquals("3+30", result.get(1, 0));
        assertEquals("4+40", result.get(1, 1));
    }

    @Test
    public void testZipWith_differentShapes() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20, 30 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, (a, b) -> a + b));
    }

    @Test
    public void testZipWith_threeMatrices() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200 }, { 300, 400 } });
        Matrix<Integer> result = m1.zipWith(m2, m3, (a, b, c) -> a + b + c);

        assertEquals(Integer.valueOf(111), result.get(0, 0));
        assertEquals(Integer.valueOf(222), result.get(0, 1));
        assertEquals(Integer.valueOf(333), result.get(1, 0));
        assertEquals(Integer.valueOf(444), result.get(1, 1));
    }

    @Test
    public void testZipWith_threeMatrices_withTypeChange() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200 }, { 300, 400 } });
        Matrix<String> result = m1.zipWith(m2, m3, (a, b, c) -> a + "+" + b + "+" + c, String.class);

        assertEquals("1+10+100", result.get(0, 0));
        assertEquals("2+20+200", result.get(0, 1));
        assertEquals("3+30+300", result.get(1, 0));
        assertEquals("4+40+400", result.get(1, 1));
    }

    @Test
    public void testZipWith_threeMatrices_differentShapes() {
        Matrix<Integer> m1 = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        Matrix<Integer> m2 = Matrix.of(new Integer[][] { { 10, 20 }, { 30, 40 } });
        Matrix<Integer> m3 = Matrix.of(new Integer[][] { { 100, 200, 300 } });
        assertThrows(IllegalArgumentException.class, () -> m1.zipWith(m2, m3, (a, b, c) -> a + b + c));
    }

    // ============ Stream Tests ============

    @Test
    public void testStreamLU2RD() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        List<String> diagonal = m.streamLU2RD().toList();
        assertEquals(3, diagonal.size());
        assertEquals("A", diagonal.get(0));
        assertEquals("E", diagonal.get(1));
        assertEquals("I", diagonal.get(2));
    }

    @Test
    public void testStreamLU2RD_empty() {
        Matrix<String> empty = Matrix.of(new String[0][0]);
        assertEquals(0, empty.streamLU2RD().count());
    }

    @Test
    public void testStreamLU2RD_nonSquare() {
        Matrix<String> nonSquare = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamLU2RD());
    }

    @Test
    public void testStreamRU2LD() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        List<String> antiDiagonal = m.streamRU2LD().toList();
        assertEquals(3, antiDiagonal.size());
        assertEquals("C", antiDiagonal.get(0));
        assertEquals("E", antiDiagonal.get(1));
        assertEquals("G", antiDiagonal.get(2));
    }

    @Test
    public void testStreamRU2LD_empty() {
        Matrix<String> empty = Matrix.of(new String[0][0]);
        assertEquals(0, empty.streamRU2LD().count());
    }

    @Test
    public void testStreamRU2LD_nonSquare() {
        Matrix<String> nonSquare = Matrix.of(new String[][] { { "A", "B" } });
        assertThrows(IllegalStateException.class, () -> nonSquare.streamRU2LD());
    }

    @Test
    public void testStreamH() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        List<String> all = m.streamH().toList();
        assertEquals(6, all.size());
        assertEquals("A", all.get(0));
        assertEquals("B", all.get(1));
        assertEquals("C", all.get(2));
        assertEquals("D", all.get(3));
    }

    @Test
    public void testStreamH_empty() {
        Matrix<String> empty = Matrix.of(new String[0][0]);
        assertEquals(0, empty.streamH().count());
    }

    @Test
    public void testStreamH_withRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        List<String> row1 = m.streamH(1).toList();
        assertEquals(3, row1.size());
        assertEquals("D", row1.get(0));
        assertEquals("E", row1.get(1));
        assertEquals("F", row1.get(2));
    }

    @Test
    public void testStreamH_withRow_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2));
    }

    @Test
    public void testStreamH_withRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        List<String> rows = m.streamH(1, 3).toList();
        assertEquals(6, rows.size());
    }

    @Test
    public void testStreamH_withRange_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamH(2, 1));
    }

    @Test
    public void testStreamV() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        List<String> all = m.streamV().toList();
        assertEquals(6, all.size());
        assertEquals("A", all.get(0));
        assertEquals("D", all.get(1));
        assertEquals("B", all.get(2));
    }

    @Test
    public void testStreamV_empty() {
        Matrix<String> empty = Matrix.of(new String[0][0]);
        assertEquals(0, empty.streamV().count());
    }

    @Test
    public void testStreamV_withColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        List<String> col1 = m.streamV(1).toList();
        assertEquals(2, col1.size());
        assertEquals("B", col1.get(0));
        assertEquals("E", col1.get(1));
    }

    @Test
    public void testStreamV_withColumn_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2));
    }

    @Test
    public void testStreamV_withRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        List<String> cols = m.streamV(1, 3).toList();
        assertEquals(6, cols.size());
    }

    @Test
    public void testStreamV_withRange_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamV(2, 1));
    }

    @Test
    public void testStreamR() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        List<Stream<String>> rows = m.streamR().toList();
        assertEquals(2, rows.size());
        List<String> row0 = rows.get(0).toList();
        assertEquals(3, row0.size());
        assertEquals("A", row0.get(0));
    }

    @Test
    public void testStreamR_empty() {
        Matrix<String> empty = Matrix.of(new String[0][0]);
        assertEquals(0, empty.streamR().count());
    }

    @Test
    public void testStreamR_withRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        List<Stream<String>> rows = m.streamR(1, 3).toList();
        assertEquals(2, rows.size());
    }

    @Test
    public void testStreamR_withRange_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamR(0, 3));
    }

    @Test
    public void testStreamC() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        List<Stream<String>> cols = m.streamC().toList();
        assertEquals(3, cols.size());
    }

    @Test
    public void testStreamC_empty() {
        Matrix<String> empty = Matrix.of(new String[0][0]);
        assertEquals(0, empty.streamC().count());
    }

    @Test
    public void testStreamC_withRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        List<Stream<String>> cols = m.streamC(1, 3).toList();
        assertEquals(2, cols.size());
    }

    @Test
    public void testStreamC_withRange_outOfBounds() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.streamC(0, 3));
    }

    // ============ ForEach Tests ============

    @Test
    public void testForEach() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        List<String> values = new ArrayList<>();
        m.forEach(v -> values.add(v));
        assertEquals(4, values.size());
        assertTrue(values.contains("A"));
        assertTrue(values.contains("D"));
    }

    @Test
    public void testForEach_withRange() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        List<String> values = new ArrayList<>();
        m.forEach(1, 3, 1, 3, v -> values.add(v));
        assertEquals(4, values.size());
        assertTrue(values.contains("E"));
        assertTrue(values.contains("I"));
        assertFalse(values.contains("A"));
    }

    // ============ Object Methods Tests ============

    @Test
    public void testHashCode() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> m3 = Matrix.of(new String[][] { { "A", "B" }, { "X", "D" } });

        assertEquals(m1.hashCode(), m2.hashCode());
        assertNotEquals(m1.hashCode(), m3.hashCode()); // Usually different
    }

    @Test
    public void testEquals() {
        Matrix<String> m1 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> m2 = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> m3 = Matrix.of(new String[][] { { "A", "B" }, { "X", "D" } });
        Matrix<String> m4 = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });

        assertTrue(m1.equals(m1)); // Same object
        assertTrue(m1.equals(m2)); // Same values
        assertFalse(m1.equals(m3)); // Different values
        assertFalse(m1.equals(m4)); // Different dimensions
        assertFalse(m1.equals(null));
        assertFalse(m1.equals("not a matrix"));
    }

    @Test
    public void testToString() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("A") || str.contains("["));
    }

    // ============ Edge Case Tests ============

    @Test
    public void testNullHandling() {
        Matrix<String> m = Matrix.of(new String[][] { { null, "B" }, { "C", null } });

        assertNull(m.get(0, 0));
        assertEquals("B", m.get(0, 1));

        // Test operations with nulls
        m.updateAll(x -> x == null ? "NULL" : x);
        assertEquals("NULL", m.get(0, 0));
        assertEquals("B", m.get(0, 1));
    }

    @Test
    public void testEmptyMatrixOperations() {
        Matrix<String> empty = Matrix.of(new String[0][0]);

        assertTrue(empty.flatten().isEmpty());
        assertEquals(0, empty.copy().rows);
        assertEquals(empty, empty.transpose());
        assertEquals(empty, empty.rotate90());

        Matrix<String> extended = empty.extend(2, 2, "X");
        assertEquals(2, extended.rows);
        assertEquals(2, extended.cols);
        assertEquals("X", extended.get(0, 0));
    }

    @Test
    public void testSingleElementMatrix() {
        Matrix<String> m = Matrix.of(new String[][] { { "X" } });

        assertEquals("X", m.get(0, 0));
        assertEquals(1, m.rows);
        assertEquals(1, m.cols);

        Matrix<String> transposed = m.transpose();
        assertEquals("X", transposed.get(0, 0));
    }

    @Test
    public void testLargeMatrix() {
        Integer[][] data = new Integer[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                data[i][j] = i * 100 + j;
            }
        }
        Matrix<Integer> m = Matrix.of(data);

        assertEquals(100, m.rows);
        assertEquals(100, m.cols);
        assertEquals(Integer.valueOf(9999), m.get(99, 99));
    }

    @Test
    public void testGenericTypeWithCustomObject() {
        class CustomObject {
            String value;

            CustomObject(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }
        }

        CustomObject[][] data = { { new CustomObject("A"), new CustomObject("B") }, { new CustomObject("C"), new CustomObject("D") } };
        Matrix<CustomObject> m = Matrix.of(data);

        assertEquals("A", m.get(0, 0).value);
        assertEquals("D", m.get(1, 1).value);
    }

    @Test
    public void testGenericTypeWithList() {
        List<String> list1 = Arrays.asList("a", "b");
        List<String> list2 = Arrays.asList("c", "d");
        List<String> list3 = Arrays.asList("e", "f");
        List<String> list4 = Arrays.asList("g", "h");

        @SuppressWarnings("unchecked")
        Matrix<List<String>> m = Matrix.of(new List[] { list1, list2 }, new List[] { list3, list4 });

        assertEquals(list1, m.get(0, 0));
        assertEquals(list4, m.get(1, 1));
    }

    @Test
    public void testImmutabilityOfDimensions() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        int originalRows = m.rows;
        int originalCols = m.cols;

        // Operations that return new matrices
        m.transpose();
        m.rotate90();
        m.extend(5, 5);

        // Original dimensions should not change
        assertEquals(originalRows, m.rows);
        assertEquals(originalCols, m.cols);
    }

    @Test
    public void testChainedOperations() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });

        Matrix<Integer> result = m.map(x -> x * 2).map(x -> x + 1).transpose().rotate90();

        assertNotNull(result);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
    }

    // ============ Additional Coverage Tests ============

    @Test
    public void testToDatasetH() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<String> columnNames = Arrays.asList("A", "B", "C");
        Dataset dataset = m.toDatasetH(columnNames);

        assertNotNull(dataset);
        assertEquals(3, dataset.columnNameList().size());
        assertEquals(2, dataset.size());
        assertEquals(Integer.valueOf(1), dataset.getColumn("A").get(0));
        assertEquals(Integer.valueOf(6), dataset.getColumn("C").get(1));
    }

    @Test
    public void testToDatasetH_wrongColumnCount() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<String> columnNames = Arrays.asList("A", "B");
        assertThrows(IllegalArgumentException.class, () -> m.toDatasetH(columnNames));
    }

    @Test
    public void testToDatasetV() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<String> columnNames = Arrays.asList("Row1", "Row2");
        Dataset dataset = m.toDatasetV(columnNames);

        assertNotNull(dataset);
        assertEquals(2, dataset.columnNameList().size());
        assertEquals(3, dataset.size());
        assertEquals(Integer.valueOf(1), dataset.getColumn("Row1").get(0));
        assertEquals(Integer.valueOf(6), dataset.getColumn("Row2").get(2));
    }

    @Test
    public void testToDatasetV_wrongColumnCount() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        List<String> columnNames = Arrays.asList("A", "B", "C");
        assertThrows(IllegalArgumentException.class, () -> m.toDatasetV(columnNames));
    }

    @Test
    public void testExtend_allZeros() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> result = m.extend(0, 0, 0, 0);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals("A", result.get(0, 0));
        assertEquals("D", result.get(1, 1));
    }

    @Test
    public void testExtend_withOnlyUp() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> extended = m.extend(2, 0, 0, 0, "X");
        assertEquals(4, extended.rows);
        assertEquals(2, extended.cols);
        assertEquals("X", extended.get(0, 0));
        assertEquals("X", extended.get(1, 0));
        assertEquals("A", extended.get(2, 0));
    }

    @Test
    public void testExtend_withOnlyLeft() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> extended = m.extend(0, 0, 2, 0, "X");
        assertEquals(2, extended.rows);
        assertEquals(4, extended.cols);
        assertEquals("X", extended.get(0, 0));
        assertEquals("X", extended.get(0, 1));
        assertEquals("A", extended.get(0, 2));
    }

    @Test
    public void testExtend_smaller_nonSquare() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" } });
        Matrix<String> result = m.extend(1, 2);
        assertEquals(1, result.rows);
        assertEquals(2, result.cols);
        assertEquals("A", result.get(0, 0));
        assertEquals("B", result.get(0, 1));
    }

    @Test
    public void testExtend_largerRows_smallerCols() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> result = m.extend(3, 1, "X");
        assertEquals(3, result.rows);
        assertEquals(1, result.cols);
        assertEquals("A", result.get(0, 0));
        assertEquals("C", result.get(1, 0));
        assertEquals("X", result.get(2, 0));
    }

    @Test
    public void testReverseH_singleColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "A" }, { "B" }, { "C" } });
        m.reverseH();
        assertEquals("A", m.get(0, 0));
        assertEquals("B", m.get(1, 0));
        assertEquals("C", m.get(2, 0));
    }

    @Test
    public void testReverseV_singleRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
        m.reverseV();
        assertEquals("A", m.get(0, 0));
        assertEquals("B", m.get(0, 1));
        assertEquals("C", m.get(0, 2));
    }

    @Test
    public void testFill_withNullValue() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.fill((String) null);
        assertNull(m.get(0, 0));
        assertNull(m.get(1, 1));
    }

    @Test
    public void testFill_withArray_emptySource() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String[][] emptySource = {};
        m.fill(emptySource);
        assertEquals("A", m.get(0, 0));
        assertEquals("D", m.get(1, 1));
    }

    @Test
    public void testFill_withArray_largerSource() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String[][] largeSource = { { "X", "Y", "Z" }, { "W", "V", "U" }, { "T", "S", "R" } };
        m.fill(largeSource);
        assertEquals("X", m.get(0, 0));
        assertEquals("Y", m.get(0, 1));
        assertEquals("W", m.get(1, 0));
        assertEquals("V", m.get(1, 1));
    }

    @Test
    public void testFill_atBoundary() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "A", "A" }, { "A", "A", "A" }, { "A", "A", "A" } });
        String[][] patch = { { "X" } };
        m.fill(2, 2, patch);
        assertEquals("A", m.get(0, 0));
        assertEquals("X", m.get(2, 2));
    }

    @Test
    public void testFill_atEdgePosition() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        String[][] patch = { { "X", "Y" } };
        m.fill(1, 0, patch);
        assertEquals("A", m.get(0, 0));
        assertEquals("X", m.get(1, 0));
        assertEquals("Y", m.get(1, 1));
    }

    @Test
    public void testReshape_largerSize() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> reshaped = m.reshape(2, 3);
        assertEquals(2, reshaped.rows);
        assertEquals(3, reshaped.cols);
        assertEquals("A", reshaped.get(0, 0));
        assertEquals("D", reshaped.get(1, 0));
        assertNull(reshaped.get(1, 2));
    }

    @Test
    public void testReshape_singleRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C", "D" } });
        Matrix<String> reshaped = m.reshape(2, 2);
        assertEquals(2, reshaped.rows);
        assertEquals(2, reshaped.cols);
        assertEquals("A", reshaped.get(0, 0));
        assertEquals("B", reshaped.get(0, 1));
        assertEquals("C", reshaped.get(1, 0));
        assertEquals("D", reshaped.get(1, 1));
    }

    @Test
    public void testUpdateRow_withNullFunction() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.updateRow(0, x -> null);
        assertNull(m.get(0, 0));
        assertNull(m.get(0, 1));
        assertEquals("C", m.get(1, 0));
    }

    @Test
    public void testUpdateColumn_withNullFunction() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.updateColumn(0, x -> null);
        assertNull(m.get(0, 0));
        assertNull(m.get(1, 0));
        assertEquals("B", m.get(0, 1));
    }

    @Test
    public void testLength_withNullArray() {
        Matrix<String> m = Matrix.of(new String[][] { { "A" } });
        assertEquals(0, m.length(null));
    }

    @Test
    public void testAdjacentPoints_withPoint() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" }, { "D", "E", "F" }, { "G", "H", "I" } });
        Point p = Point.of(1, 1);
        assertEquals("E", m.get(p));

        // Test setting with point
        m.set(p, "X");
        assertEquals("X", m.get(1, 1));
    }

    @Test
    public void testRotate90_nonSquare_rowsLessThanCols() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
        Matrix<String> rotated = m.rotate90();
        assertEquals(3, rotated.rows);
        assertEquals(1, rotated.cols);
        assertEquals("A", rotated.get(0, 0));
        assertEquals("B", rotated.get(1, 0));
        assertEquals("C", rotated.get(2, 0));
    }

    @Test
    public void testRotate270_nonSquare_rowsLessThanCols() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
        Matrix<String> rotated = m.rotate270();
        assertEquals(3, rotated.rows);
        assertEquals(1, rotated.cols);
        assertEquals("C", rotated.get(0, 0));
        assertEquals("B", rotated.get(1, 0));
        assertEquals("A", rotated.get(2, 0));
    }

    @Test
    public void testTranspose_nonSquare_rowsLessThanCols() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
        Matrix<String> transposed = m.transpose();
        assertEquals(3, transposed.rows);
        assertEquals(1, transposed.cols);
        assertEquals("A", transposed.get(0, 0));
        assertEquals("B", transposed.get(1, 0));
        assertEquals("C", transposed.get(2, 0));
    }

    @Test
    public void testMap_withIndices() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.updateAll((i, j) -> m.get(i, j) + i + j);
        assertEquals("A00", m.get(0, 0));
        assertEquals("B01", m.get(0, 1));
        assertEquals("C10", m.get(1, 0));
        assertEquals("D11", m.get(1, 1));
    }

    @Test
    public void testReplaceIf_noneMatch() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.replaceIf(x -> "Z".equals(x), "X");
        assertEquals("A", m.get(0, 0));
        assertEquals("D", m.get(1, 1));
    }

    @Test
    public void testReplaceIf_withIndices_noneMatch() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        m.replaceIf((i, j) -> i > 10, "X");
        assertEquals("A", m.get(0, 0));
        assertEquals("D", m.get(1, 1));
    }

    @Test
    public void testStreamH_singleRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
        List<String> result = m.streamH(0).toList();
        assertEquals(3, result.size());
        assertEquals("A", result.get(0));
        assertEquals("C", result.get(2));
    }

    @Test
    public void testStreamV_singleColumn() {
        Matrix<String> m = Matrix.of(new String[][] { { "A" }, { "B" }, { "C" } });
        List<String> result = m.streamV(0).toList();
        assertEquals(3, result.size());
        assertEquals("A", result.get(0));
        assertEquals("C", result.get(2));
    }

    @Test
    public void testForEach_fullMatrix() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        List<Integer> collected = new ArrayList<>();
        m.forEach(0, 2, 0, 2, (Throwables.Consumer<Integer, RuntimeException>) collected::add);
        assertEquals(4, collected.size());
        assertTrue(collected.contains(1));
        assertTrue(collected.contains(4));
    }

    @Test
    public void testForEach_emptyRange() {
        Matrix<Integer> m = Matrix.of(new Integer[][] { { 1, 2 }, { 3, 4 } });
        List<Integer> collected = new ArrayList<>();
        m.forEach(0, 0, 0, 0, (Throwables.Consumer<Integer, RuntimeException>) collected::add);
        assertEquals(0, collected.size());
    }

    @Test
    public void testVstack_withSameMatrix() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" } });
        Matrix<String> result = m.vstack(m);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals("A", result.get(0, 0));
        assertEquals("A", result.get(1, 0));
    }

    @Test
    public void testHstack_withSameMatrix() {
        Matrix<String> m = Matrix.of(new String[][] { { "A" }, { "B" } });
        Matrix<String> result = m.hstack(m);
        assertEquals(2, result.rows);
        assertEquals(2, result.cols);
        assertEquals("A", result.get(0, 0));
        assertEquals("A", result.get(0, 1));
    }

    @Test
    public void testCopy_singleRow() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B", "C" } });
        Matrix<String> copy = m.copy(0, 1);
        assertEquals(1, copy.rows);
        assertEquals(3, copy.cols);
        assertEquals("A", copy.get(0, 0));
    }

    @Test
    public void testCopy_singleElement() {
        Matrix<String> m = Matrix.of(new String[][] { { "A", "B" }, { "C", "D" } });
        Matrix<String> copy = m.copy(0, 1, 0, 1);
        assertEquals(1, copy.rows);
        assertEquals(1, copy.cols);
        assertEquals("A", copy.get(0, 0));
    }

    @Test
    public void testFlatOp_emptyMatrix() {
        Matrix<String> empty = Matrix.of(new String[0][0]);
        List<Integer> lengths = new ArrayList<>();
        empty.flatOp(row -> lengths.add(row.length));
        assertEquals(0, lengths.size());
    }
}
